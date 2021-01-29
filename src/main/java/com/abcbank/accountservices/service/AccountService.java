package com.abcbank.accountservices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abcbank.accountservices.util.exception.EntityNotFoundException;
import com.bankabc.accountservices.entity.Account;
import com.bankabc.accountservices.repository.AccountRepository;

@Service
public class AccountService {
	private final AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	public Account findByAccountNumber(Long accountNumber) throws EntityNotFoundException {
		return accountRepository.findById(accountNumber)
				.orElseThrow(() -> new EntityNotFoundException(accountNumber, Account.class.getSimpleName()));
	}

	public Account save(Account account) {
		return accountRepository.save(account);
	}

	public Account updateAccount(Long accountNumber, Account updatedAccount) {
		//update or save new..
		return accountRepository.findById(accountNumber) //
				.map(account -> {
					account.setAccountBalance(updatedAccount.getAccountBalance());
					account.setCurrency(updatedAccount.getCurrency());
					account.setAccountName(updatedAccount.getAccountName());
					//don't update treasury..
					return accountRepository.save(account);
				}) //
				.orElseGet(() -> {
					updatedAccount.setAccountNumber(accountNumber);
					return accountRepository.save(updatedAccount);
				});
	}
}
