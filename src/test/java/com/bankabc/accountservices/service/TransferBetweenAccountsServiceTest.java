package com.bankabc.accountservices.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abcbank.accountservices.controller.request.TransferBetweenAccountsReq;
import com.abcbank.accountservices.entity.Account;
import com.abcbank.accountservices.entity.Transaction;
import com.abcbank.accountservices.repository.TransferBetweenAccountsRepository;
import com.abcbank.accountservices.service.AccountService;
import com.abcbank.accountservices.service.TransferBetweenAccountsService;
import com.abcbank.accountservices.util.exception.InsufficientFundsException;

@ExtendWith(MockitoExtension.class)
public class TransferBetweenAccountsServiceTest {

	private TransferBetweenAccountsService transferBetweenAccountsService;

	@Mock
	private TransferBetweenAccountsRepository transferRepository;

	@Mock
	private AccountService accountService;

	private static Account fromAccount;
	private static Account toAccount;
	private static TransferBetweenAccountsReq transferRequest;

	@BeforeAll
	static void initializeData() {
		fromAccount = new Account(1L, "FromAcctTest", Currency.getInstance("USD"), BigDecimal.ONE, false);
		toAccount = new Account(2L, "ToAcctTest", Currency.getInstance("USD"), BigDecimal.ZERO, false);

		transferRequest = new TransferBetweenAccountsReq();
		transferRequest.setAmount(BigDecimal.ONE);
		transferRequest.setFromAccountNumber(fromAccount.getAccountNumber());
		transferRequest.setCurrency(Currency.getInstance("USD"));
		transferRequest.setToAccountNumber(toAccount.getAccountNumber());
	}

	@BeforeEach
	void initTransferServer() {
		transferBetweenAccountsService = new TransferBetweenAccountsService(transferRepository, accountService);
	}

	@Test
	void transferBetweenAccounts_whenFundsAvail_doTheTransfer() {
		// given

		when(accountService.findByAccountNumber(fromAccount.getAccountNumber())).thenReturn(fromAccount);
        when(accountService.findByAccountNumber(toAccount.getAccountNumber())).thenReturn(toAccount);
        when(accountService.save(any(Account.class))).then(returnsFirstArg());
        when(transferRepository.save(any(Transaction.class))).then(returnsFirstArg());
		transferBetweenAccountsService.transferBetweenAccounts(transferRequest);
		// then
		assertThat(fromAccount.getAccountBalance()).isEqualTo(BigDecimal.ZERO);
		assertThat(toAccount.getAccountBalance()).isEqualTo(BigDecimal.ONE);
	}

	@Test
	void transferBetweenAccounts_whenNoFunds_NoTreasuryAccount_throwError() {
		// given
		fromAccount.setAccountBalance(BigDecimal.ZERO);
		
		
		try {
			transferBetweenAccountsService.transferBetweenAccounts(transferRequest);
		}
		// then
		catch (InsufficientFundsException e) {
			assertThat(fromAccount.getAccountBalance()).isEqualTo(BigDecimal.ZERO);
			assertThat(toAccount.getAccountBalance()).isEqualTo(BigDecimal.ZERO);
			assertThat(e.getMessage())
					.isEqualTo(String.format("Insufficient Funds in account with Id %s, transfer Amount %s, Account Balance %s",
							fromAccount.getAccountNumber(), transferRequest.getAmount(), fromAccount.getAccountBalance()));
		}
	}

	@Test
	void doTransfer_whenNoFunds_FromTreasuryAccount_doTheTransfer() {
		// given
		fromAccount.setIsTreasury(true);
		fromAccount.setAccountBalance(BigDecimal.ZERO);
		when(accountService.findByAccountNumber(fromAccount.getAccountNumber())).thenReturn(fromAccount);
        when(accountService.findByAccountNumber(toAccount.getAccountNumber())).thenReturn(toAccount);
        when(accountService.save(any(Account.class))).then(returnsFirstArg());
        when(transferRepository.save(any(Transaction.class))).then(returnsFirstArg());
		// when
        transferBetweenAccountsService.transferBetweenAccounts(transferRequest);
		// then
		assertThat(fromAccount.getAccountBalance()).isEqualTo(BigDecimal.valueOf(-1));
		assertThat(toAccount.getAccountBalance()).isEqualTo(BigDecimal.ONE);
	}
}
