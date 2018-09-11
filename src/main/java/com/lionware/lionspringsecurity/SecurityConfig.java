package com.lionware.lionspringsecurity;

import java.util.Date;
import java.util.List;

public interface SecurityConfig {
	public Long getUserId(String username, String extra);
	
	public String getPassword(String username, String extra);
	
	public List<String> getRoles(String username, String extra);
	
	public Boolean getIsLoked(String username, String extra);
	
	public Date getLockDate(String username, String extra);
	
	public void setLockDate(String username, Date date, String extra);
	
	public Double getLockTimeMinutes();
	
	public Date getLastFailedAttempt(String username, String extra);
	
	public void setLastFailedAttempt(String username, Date date, String extra);
	
	public Integer getNumberOfAttempts(String username, String extra);
	
	public void setNumberOfAttempts(String username, Integer attempts, String extra);
	
	public Integer getMaxAttempts();
	
	public Double getResetTimeHours();
	
	public void handleSuccess();
	
	public void handleError();
}
