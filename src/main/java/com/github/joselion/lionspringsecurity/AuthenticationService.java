package com.github.joselion.lionspringsecurity;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.joselion.lionspringsecurity.core.Account;
import com.github.joselion.lionspringsecurity.core.AccountService;
import com.github.joselion.lionspringsecurity.core.LionSecurityException;
import com.github.joselion.lionspringsecurity.properties.LionSecurityProperties;

@Service
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			if (username == null) {
				throw new UsernameNotFoundException("Username was not provided to the authentication service");
			}
			
			Account account = accountService.populate(username);
			
			if (account == null) {
				throw new UsernameNotFoundException("The username could not be found");
			}
			
			Date today = new Date();
			
			if (account.getIsLocked() && (account.getLockDate().getTime() + securityProperties.getLockTime()) > today.getTime()) {
				this.unlock(account);
			}
			
			return new AuthenticatedAccount(account);
		} catch (LionSecurityException | SQLException e) {
			throw new UsernameNotFoundException("Security Exception: " + e.getMessage(), e);
		}
	}
	
	private void unlock(Account account) throws LionSecurityException, SQLException {
		account.setLockDate(null);
		account.setIsLocked(false);
		account.setAttempts(0);
		accountService.update(account);
	}
	
}
