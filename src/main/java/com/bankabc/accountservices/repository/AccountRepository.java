package com.bankabc.accountservices.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bankabc.accountservices.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}