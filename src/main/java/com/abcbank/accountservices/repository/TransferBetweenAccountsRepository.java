package com.abcbank.accountservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcbank.accountservices.entity.Transaction;

public interface TransferBetweenAccountsRepository extends JpaRepository<Transaction, Long> {

}
