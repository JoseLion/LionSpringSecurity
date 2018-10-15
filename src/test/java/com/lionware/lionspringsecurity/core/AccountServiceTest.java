package com.lionware.lionspringsecurity.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.lionware.lionspringsecurity.Helpers;
import com.lionware.lionspringsecurity.MockConfiguration;
import com.lionware.lionspringsecurity.properties.LionSecurityProperties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ MockConfiguration.class, LionSecurityProperties.class })
@Import(AccountService.class)
public class AccountServiceTest {
	
	@SpyBean
	private AccountService accountService;
	
	@SpyBean
	private DataSource dataSource;
	
	@SpyBean
	private LionSecurityProperties securityProperties;
	
	@SpyBean
	private Helpers helpers;
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
	
	@Before
	public void beforeEach() throws SQLException {
		helpers.dropAccountTable();
	}
	
	@Test
	public void whenUsernameIsNull_shouldReturnNull() throws LionSecurityException, SQLException {
		helpers.createBasicAccountTable();
		Account account = accountService.populate(null);
		
		assertThat(account).isNull();
	}
	
	@Test
	public void whenTableHasBasicColumns_shouldPopulateAccountInstance() throws LionSecurityException, SQLException {
		helpers.createBasicAccountTable();
		helpers.insertBasicAccount("mock-user@test.com", "1234");
		Account account = accountService.populate("mock-user@test.com");
		
		assertThat(account).isNotNull();
		assertThat(account.getId()).isGreaterThan(0L);
		assertThat(account.getUsername()).isEqualTo("mock-user@test.com");
		assertThat(account.getPassword()).isEqualTo("1234");
	}
	
	@Test
	public void whenTableHasBasicColumns_shouldUpdateTheAccountRecord() throws LionSecurityException, SQLException {
		helpers.createBasicAccountTable();
		helpers.insertBasicAccount("mock-user@test.com", "1234");
		Account account = accountService.populate("mock-user@test.com");
		Date mockDate = new Date(1538864181455L);
		Long recordId = new Long(account.getId());
		
		account.setLastAttempt(mockDate);
		account.setAttempts(3);
		account.setIsLocked(true);
		
		accountService.update(account);
		
		assertThat(account).isNotNull();
		assertThat(account.getId()).isEqualTo(recordId);
		assertThat(account.getLastAttempt()).isNull();
		assertThat(account.getAttempts()).isZero();
		assertThat(account.getIsLocked()).isFalse();
	}
	
	@Test
	public void whenTableHasCompleteColumns_shouldPopulateAccountInstance() throws LionSecurityException, SQLException, ParseException {
		helpers.createCompleteAccountTable();
		helpers.insertCompleteAccount("mock-user@test.com", "1234");
		Account account = accountService.populate("mock-user@test.com");
		Long mockMillis = dateFormat.parse("2018-10-05 00:25:03-0500").getTime();
		
		assertThat(account).isNotNull();
		assertThat(account.getId()).isGreaterThan(0L);
		assertThat(account.getUsername()).isEqualTo("mock-user@test.com");
		assertThat(account.getPassword()).isEqualTo("1234");
		assertThat(account.getRoles()).contains("admin", "user");
		assertThat(account.getIsEnabled()).isEqualTo(true);
		assertThat(account.getIsLocked()).isFalse();
		assertThat(account.getLockDate().getTime()).isEqualTo(mockMillis);
		assertThat(account.getLastAttempt().getTime()).isEqualTo(mockMillis);
		assertThat(account.getAttempts()).isEqualTo(1);
		assertThat(account.getAccountExpired()).isEqualTo(false);
		assertThat(account.getCredentialsExpired()).isEqualTo(false);
	}
	
	@Test
	public void whenTableHasCompleteColumns_shouldUpdateTheAccountRecord() throws LionSecurityException, SQLException {
		helpers.createCompleteAccountTable();
		helpers.insertCompleteAccount("mock-user@test.com", "1234");
		Account account = accountService.populate("mock-user@test.com");
		Date mockDate = new Date(1538864181455L);
		Long recordId = new Long(account.getId());
		
		account.setLastAttempt(mockDate);
		account.setAttempts(3);
		account.setIsLocked(true);
		account.setLockDate(mockDate);
		
		account = accountService.update(account);
		
		assertThat(account).isNotNull();
		assertThat(account.getId()).isEqualTo(recordId);
		assertThat(account.getLastAttempt()).isEqualToIgnoringMillis(mockDate);
		assertThat(account.getAttempts()).isEqualTo(3);
		assertThat(account.getIsLocked()).isEqualTo(true);
		assertThat(account.getLockDate()).isEqualToIgnoringMillis(mockDate);
	}
	
	@Test
	public void whenTableHasCompleteColumns_shouldUpdateTheAccountRecordToNull() throws LionSecurityException, SQLException {
		helpers.createCompleteAccountTable();
		helpers.insertCompleteAccount("mock-user@test.com", "1234");
		Account account = accountService.populate("mock-user@test.com");
		Long recordId = new Long(account.getId());
		
		account.setLastAttempt(null);
		account.setLockDate(null);
		
		account = accountService.update(account);
		
		assertThat(account).isNotNull();
		assertThat(account.getId()).isEqualTo(recordId);
		assertThat(account.getLastAttempt()).isNull();
		assertThat(account.getLockDate()).isNull();
	}

}
