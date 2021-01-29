package com.bankabc.accountservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankabc.accountservices.entity.Transaction;

public interface TransferBetweenAccountsRepository extends JpaRepository<Transaction, Long> {

}
