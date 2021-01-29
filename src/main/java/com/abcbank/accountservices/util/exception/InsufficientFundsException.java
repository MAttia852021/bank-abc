package com.abcbank.accountservices.util.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1972492269960807949L;

	public InsufficientFundsException(Long accountNumber, BigDecimal TransferAmount, BigDecimal accountBalance) {

		super(String.format("Insufficient Funds in account with Id %s, transfer Amount %s, Account Balance %s",
				accountNumber, TransferAmount, accountBalance));
	}

}
