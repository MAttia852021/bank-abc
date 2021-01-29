package com.bankabc.accountservices.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abcbank.accountservices.entity.Account;
import com.abcbank.accountservices.repository.AccountRepository;
import com.abcbank.accountservices.service.AccountService;
import com.abcbank.accountservices.util.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
	private AccountService accountService;

	@Mock
	private AccountRepository accountRepository;

	private Account account;

	@BeforeEach
	public void initializeData() {
		accountService = new AccountService(accountRepository);
		account = new Account(1L, "testingAccount", Currency.getInstance("USD"), BigDecimal.ONE, false);
	}

	@Test
	public void findByAccountNumber_whenExists_returnsTheAccount() {
		// given
		Long accountNumber = 1L;
		given(accountRepository.findById(accountNumber)).willReturn(Optional.of(account));
		// when
		Account actualAccount = accountService.findByAccountNumber(accountNumber);
		// then
		assertThat(actualAccount).isEqualTo(account);
	}

	@Test
	public void findByAccountNumber_whenNotExists_throwsEntityNotFoundByIdException() {
		// given
		Long accountNumber = 1L;
		given(accountRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when
		try {
			accountService.findByAccountNumber(accountNumber);
		}
		// then
		catch (EntityNotFoundException ex) {
			assertThat(ex.getMessage()).isEqualTo(
					String.format("Entity %s ,with Id %s not found", Account.class.getSimpleName(), accountNumber));
		}
	}

	@Test
	public void save_whenValidInput_returnsTheAccount() {
		// given
		given(accountRepository.save(account)).willReturn(account);
		// when
		Account actualAccount = accountService.save(account);
		// then
		assertThat(actualAccount).isEqualTo(account);
	}

	@Test
	public void findAll_whenCalled_returnsAccountRepositoryOutput() {
		// given
		List<Account> accounts = new ArrayList<>();
		given(accountRepository.findAll()).willReturn(accounts);
		// when
		List<Account> actualAccounts = accountService.findAll();
		// then
		assertThat(actualAccounts).isEqualTo(accounts);
	}

	@Test
	public void updateAccount_whenCalledWithoutTreasuryData_editTheAccountAndReturnsIt() {
		// given
		Long accountNumber = account.getAccountNumber();
		Account updatedAccount = account;
		updatedAccount.setAccountName("UpdatedTestAccount");
		updatedAccount.setAccountBalance(BigDecimal.TEN);
		given(accountRepository.findById(accountNumber)).willReturn(Optional.of(account));
		given(accountRepository.save(account)).willReturn(account);
		// when
		Account actualAccount = accountService.updateAccount(accountNumber, updatedAccount);
		// then
		assertThat(actualAccount.getAccountBalance()).isEqualTo(updatedAccount.getAccountBalance());
		assertThat(actualAccount.getAccountName()).isEqualTo(updatedAccount.getAccountName());
		assertThat(actualAccount.isTreasury()).isEqualTo(false);
		assertThat(actualAccount.getAccountNumber()).isEqualTo(accountNumber);
	}

	@Test
	public void editById_whenCalledWithTreasuryData_doNotEditTreasury() {
		// given
		Long accountNumber = account.getAccountNumber();
		Account updatedAccount = account;
		updatedAccount.setIsTreasury(true);
		given(accountRepository.findById(accountNumber)).willReturn(Optional.of(account));
		given(accountRepository.save(account)).willReturn(account);
		// when
		Account actualAccount = accountService.updateAccount(accountNumber, updatedAccount);
		// then
		assertThat(actualAccount.isTreasury()).isEqualTo(false);
	}

}
