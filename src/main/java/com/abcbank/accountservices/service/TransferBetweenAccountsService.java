package com.abcbank.accountservices.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.abcbank.accountservices.controller.request.TransferBetweenAccountsReq;
import com.abcbank.accountservices.util.exception.EntityNotFoundException;
import com.abcbank.accountservices.util.exception.InsufficientFundsException;
import com.bankabc.accountservices.entity.Account;
import com.bankabc.accountservices.entity.Transaction;
import com.bankabc.accountservices.repository.TransferBetweenAccountsRepository;

@Service
public class TransferBetweenAccountsService {
	private final TransferBetweenAccountsRepository transactionRepository;
	private final AccountService accountService;

	public TransferBetweenAccountsService(TransferBetweenAccountsRepository transactionRepository, AccountService accountService) {
		super();
		this.transactionRepository = transactionRepository;
		this.accountService = accountService;
	}

	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Transaction transferBetweenAccounts(TransferBetweenAccountsReq transferBetweenAccountsReq)
			throws InsufficientFundsException, EntityNotFoundException {
		// find the two accounts first..
		Account fromAccount = accountService.findByAccountNumber(transferBetweenAccountsReq.getFromAccountNumber());
		Account toAccount = accountService.findByAccountNumber(transferBetweenAccountsReq.getToAccountNumber());
		// validate balance, no currency logic involved!..
		if (!fromAccount.isTreasury()
				&& fromAccount.getAccountBalance().compareTo(transferBetweenAccountsReq.getAmount()) < 0) {
			throw new InsufficientFundsException(fromAccount.getAccountNumber(), transferBetweenAccountsReq.getAmount(),
					fromAccount.getAccountBalance());
		}

		// do transaction..

		fromAccount.setAccountBalance(fromAccount.getAccountBalance().subtract(transferBetweenAccountsReq.getAmount()));
		toAccount.setAccountBalance(toAccount.getAccountBalance().add(transferBetweenAccountsReq.getAmount()));
		accountService.save(fromAccount);
		accountService.save(toAccount);
		return transactionRepository.save(new Transaction(transferBetweenAccountsReq.getAmount(),
				transferBetweenAccountsReq.getCurrency(), transferBetweenAccountsReq.getFromAccountNumber(),
				transferBetweenAccountsReq.getToAccountNumber()));

	}
}
