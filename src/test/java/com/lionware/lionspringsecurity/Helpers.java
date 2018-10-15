package com.lionware.lionspringsecurity;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lionware.lionspringsecurity.properties.AccountEntityProperties;
import com.lionware.lionspringsecurity.properties.LionSecurityProperties;

@Service
public class Helpers {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	public void createBasicAccountTable() throws SQLException {
		AccountEntityProperties props = securityProperties.getAccountEntity();
		String create = "CREATE TABLE " + props.getTableName() + "(" +
			props.getId() + " serial PRIMARY KEY, " +
			props.getUsername() + " text NOT NULL, " +
			props.getPassword() + " text NOT NULL, " +
			"CONSTRAINT uk_username UNIQUE(username)" +
		");";
		
		dataSource.getConnection().prepareStatement(create).execute();
	}
	
	public void insertBasicAccount(String username, String password) throws SQLException {
		AccountEntityProperties props = securityProperties.getAccountEntity();
		String insert = "INSERT INTO " + props.getTableName() + "(" +
			props.getUsername() + ", " +
			props.getPassword() +
		") VALUES(" +
			"'" + username + "', " +
			"'" + password + "' " +
		");";
		
		dataSource.getConnection().prepareStatement(insert).executeUpdate();
	}
	
	public void createCompleteAccountTable() throws SQLException {
		AccountEntityProperties props = securityProperties.getAccountEntity();
		String create = "CREATE TABLE " + props.getTableName() + "(" +
			props.getId() + " serial PRIMARY KEY, " +
			props.getUsername() + " text NOT NULL, " +
			props.getPassword() + " text NOT NULL, " +
			props.getRoles() + " text[], " +
			props.getIsLocked() + " boolean NOT NULL DEFAULT FALSE, " +
			props.getLockDate() + " timestamp with time zone, " +
			props.getLastAttempt() + " timestamp with time zone, " +
			props.getAttempts() + " numeric NOT NULL DEFAULT 0, " +
			props.getIsEnabled() + " boolean NOT NULL DEFAULT TRUE, " +
			props.getAccountExpired() + " boolean NOT NULL DEFAULT FALSE, " +
			props.getCredentialsExpired() + " boolean NOT NULL DEFAULT FALSE, " +
			"CONSTRAINT uk_username UNIQUE(username)" +
		");";
		
		dataSource.getConnection().prepareStatement(create).execute();
	}
	
	public void insertCompleteAccount(String username, String password) throws SQLException {
		AccountEntityProperties props = securityProperties.getAccountEntity();
		String insert = "INSERT INTO " + props.getTableName() + "(" +
			props.getUsername() + ", " +
			props.getPassword() + ", " +
			props.getRoles() + ", " +
			props.getIsEnabled() + ", " +
			props.getIsLocked() + ", " +
			props.getLockDate() + ", " +
			props.getLastAttempt() + ", " +
			props.getAttempts() +
		") VALUES(" +
			"'" + username + "', " +
			"'" + password + "', " +
			"'{admin,user}', " +
			"TRUE, " +
			"FALSE, " +
			"'2018-10-05 00:25:03-05', " +
			"'2018-10-05 00:25:03-05', " +
			"1" +
		");";
		
		dataSource.getConnection().prepareStatement(insert).executeUpdate();
	}
	
	public void dropAccountTable() throws SQLException {
		dataSource.getConnection()
			.prepareStatement("DROP TABLE IF EXISTS \"" + securityProperties.getAccountEntity().getTableName() + "\" CASCADE;")
			.execute();
	}
}
