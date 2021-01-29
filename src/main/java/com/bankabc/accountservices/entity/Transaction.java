package com.bankabc.accountservices.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Transaction {
	private @Id @GeneratedValue Long transactionId;
	private BigDecimal transactionAmount;
	private Currency transactionCurrency;
	private Long fromAccountNumber;
	private Long toAccountNumber;
	private Timestamp transactionTime;

	public Transaction(BigDecimal transactionAmount, Currency transactionCurrency, Long fromAccountNumber,
			Long toAccountNumber) {
		this.transactionAmount = transactionAmount;
		this.transactionCurrency = transactionCurrency;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.transactionTime = new Timestamp(System.currentTimeMillis());
	}
	
	

}
