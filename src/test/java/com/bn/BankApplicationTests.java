package com.bn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bn.repositories.AccountRepository;
import com.bn.repositories.LoanApplicationRepository;
import com.bn.repositories.LoanRepository;
import com.bn.repositories.TransactionRepository;
import com.bn.repositories.UserRepository;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude="
				+ "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
				+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class BankApplicationTests {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private LoanRepository loanRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private LoanApplicationRepository loanApplicationRepository;

	@Test
	void contextLoads() {
	}
}