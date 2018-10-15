package com.lionware.lionspringsecurity.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lionware.lionspringsecurity.properties.AccountEntityProperties;
import com.lionware.lionspringsecurity.properties.LionSecurityProperties;

@Service
public class AccountService {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());

	@Transactional
	public Account populate(String username) throws LionSecurityException, SQLException {
		if (dataSource == null) {
			throw new LionSecurityException("Could not find a default datasource bean");
		}
		
		return queryByUsername(new Account(), username);
	}
	
	@Transactional
	public Account update(Account account) throws LionSecurityException, SQLException {
		if (dataSource == null) {
			throw new LionSecurityException("Could not find a default datasource bean");
		}
		
		AccountEntityProperties props = securityProperties.getAccountEntity();
		List<String> columnsSql = new ArrayList<>();
		columnsSql.add(props.getLastAttempt() + "=" + this.getDateOrNull(account.getLastAttempt()));
		columnsSql.add(props.getAttempts() + "=" + account.getAttempts());
		columnsSql.add(props.getIsLocked() + "=" + account.getIsLocked());
		columnsSql.add(props.getLockDate() + "=" + this.getDateOrNull(account.getLockDate()));
		
		boolean isDone = false;
		
		while(!isDone && columnsSql.size() > 0) {
			String statement = String.join(", ", columnsSql);
			String query = "UPDATE " + props.getTableName() + " SET " + statement + " WHERE id=" + account.getId() + ";";
			
			try {
				dataSource.getConnection().prepareStatement(query).executeUpdate();
				isDone = true;
			} catch (PSQLException e) {
				if (e.getMessage().contains("of relation \"account\" does not exist")) {
					logger.log(Level.WARNING, e.getMessage().replaceAll("\\n", " ") + " -> ignoring update for this column...");
					columnsSql.remove(columnsSql.size()  - 1);
					continue;
				} else {
					throw e;
				}
			}
		}
		
		return queryByUsername(account, account.getUsername());
	}
	
	@Transactional
	private Account queryByUsername(Account account, String username) throws SQLException {
		AccountEntityProperties props = securityProperties.getAccountEntity();
		
		String query = MessageFormat.format("SELECT * FROM {0} WHERE {1}=''{2}'';", props.getTableName(), props.getUsername(), username);
		ResultSet result = dataSource
			.getConnection()
			.prepareStatement(query)
			.executeQuery();
		
		
		
		if (result.isBeforeFirst()) {
			if (result.next()) {
				account.setId(result.getLong(props.getId()));
				account.setUsername(result.getString(props.getUsername()));
				account.setPassword(result.getString(props.getPassword()));
				this.assignIfExists(account::setRoles, this.listWrapper(result), props::getRoles);
				this.assignIfExists(account::setIsEnabled, result::getBoolean, props::getIsEnabled);
				this.assignIfExists(account::setIsLocked, result::getBoolean, props::getIsLocked);
				this.assignIfExists(account::setLockDate, result::getTimestamp, props::getLockDate);
				this.assignIfExists(account::setLastAttempt, result::getTimestamp, props::getLastAttempt);
				this.assignIfExists(account::setAttempts, result::getInt, props::getAttempts);
				this.assignIfExists(account::setAccountExpired, result::getBoolean, props::getAccountExpired);
				this.assignIfExists(account::setCredentialsExpired, result::getBoolean, props::getCredentialsExpired);
			}
		} else {
			account = null;
		}
		
		return account;
	}
	
	private <T> void assignIfExists(Consumer<T> setter, ThrowingFunction<String, T> getter, Supplier<String> propGetter) {
		T value = getter.apply(propGetter.get());
		setter.accept(value);
	}
	
	private ThrowingFunction<String, List<String>> listWrapper(ResultSet result) {
		return t -> {
			return Arrays.asList(
				(String[])result.getArray(t).getArray()
			);
		};
	}
	
	@FunctionalInterface
	private interface ThrowingFunction<T, R> extends Function<T, R> {
		@Override
		default R apply(T t) {
			try {
				return applyThrows(t);
			} catch (Exception e) {
				return null;
			}
		}
		
		R applyThrows(T t) throws Exception;
	}
	
	private String getDateOrNull(Date date) {
		if (date != null) {
			return "'" + date.toString() + "'";
		}
		
		return "NULL";
	}
}
