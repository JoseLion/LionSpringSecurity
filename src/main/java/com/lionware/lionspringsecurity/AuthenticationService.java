package com.lionware.lionspringsecurity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class AuthenticationService implements UserDetailsService {
	@Autowired
	private LionSecurityFactory lionSecurityFactory;
	
	private String extra;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null) {
			throw new UsernameNotFoundException(LionSecurityConst.USER_NOT_FOUND);
		}
		
		Boolean isLocked = false;
		
		if (lionSecurityFactory.getConfig().getLastFailedAttempt(username, this.getExtra()) != null) {
			if (lionSecurityFactory.getConfig().getResetTimeHours() != null) {
				Date today = new Date();
				Double timeToReset = lionSecurityFactory.getConfig().getResetTimeHours() * 60.0 * 60.0 * 1000.0;
				
				if ((lionSecurityFactory.getConfig().getLastFailedAttempt(username, this.getExtra()).getTime() + timeToReset) < today.getTime()) {
					lionSecurityFactory.getConfig().setLastFailedAttempt(username, null, this.getExtra());
					lionSecurityFactory.getConfig().setNumberOfAttempts(username, 0, this.getExtra());
				}
			}
		}
		
		if (lionSecurityFactory.getConfig().getLockDate(username, this.getExtra()) != null) {
			isLocked = true;
			
			if (lionSecurityFactory.getConfig().getLockTimeMinutes() != null) {
				Date today = new Date();
				Double timeToUnlock = lionSecurityFactory.getConfig().getLockTimeMinutes() * 60.0 * 1000.0;
				
				if (isLocked) {
					if ((lionSecurityFactory.getConfig().getLockDate(username, this.getExtra()).getTime() + timeToUnlock) < today.getTime()) {
						isLocked = false;
						unlockUser(username);
					}
				}
			} else {
				isLocked = false;
				unlockUser(username);
			}
		}
		
		return new AuthenticatedUser(lionSecurityFactory.getConfig().getUserId(username, this.getExtra()), username, lionSecurityFactory.getConfig().getPassword(username, this.getExtra()), lionSecurityFactory.getConfig().getRoles(username, this.getExtra()), lionSecurityFactory.getConfig().getIsLoked(username, this.getExtra()));
	}
	
	private void unlockUser(String username) {
		lionSecurityFactory.getConfig().setLockDate(username, null, this.getExtra());
		lionSecurityFactory.getConfig().setNumberOfAttempts(username, 0, this.getExtra());
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
}
