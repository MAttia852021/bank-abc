package com.abcbank.accountservices.controller.request;

import java.math.BigDecimal;
import java.util.Currency;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class TransferBetweenAccountsReq {

	private Long fromAccountNumber;

	private Long toAccountNumber;

	private BigDecimal amount;

	private Currency currency;

	

	public Long getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(Long fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public Long getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(Long toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	

}
