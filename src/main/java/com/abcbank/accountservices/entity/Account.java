package com.abcbank.accountservices.entity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Account {
	// ideally this should be two fields one for the id and one for the account
	// number,
	// where there should be a logic to construct the account number based on
	// information like : customer number, branch, account type, etc...
	// to simplify the solution will use the accountNumber as the Id
	private @Id @GeneratedValue Long accountNumber;
	private String accountName;
	private Currency currency;
	private BigDecimal accountBalance;
	private boolean isTreasury;

	public Account(Long accountNumber, String accountName, Currency currency, BigDecimal accountBalance,
			boolean isTreasury) {
		this.accountBalance = accountBalance;
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.currency = currency;
		this.isTreasury = isTreasury;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public boolean isTreasury() {
		return isTreasury;
	}

	public void setIsTreasury(boolean isTreasury) {
		this.isTreasury = isTreasury;
	}

	@Override
	public String toString() {
		return "Account{" + "Account Number=" + this.accountNumber + ", name Name='" + this.accountName + '\''
				+ ", Account Balance='" + this.accountBalance + '\'' + ", Currency='" + this.currency.getCurrencyCode()
				+ ", Is Treasury='" + this.isTreasury + '\'' + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Account))
			return false;
		Account account = (Account) o;
		return Objects.equals(this.accountNumber, account.accountNumber)
				&& Objects.equals(this.accountName, account.accountName)
				&& Objects.equals(this.currency, account.currency);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.accountNumber, this.accountName, this.currency);
	}
}
