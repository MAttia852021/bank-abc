package com.abcbank.accountservices.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.abcbank.accountservices.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}