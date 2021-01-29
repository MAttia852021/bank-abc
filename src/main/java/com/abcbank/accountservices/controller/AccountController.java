package com.abcbank.accountservices.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abcbank.accountservices.service.AccountService;
import com.abcbank.accountservices.util.assembler.AccountModelAssembler;
import com.bankabc.accountservices.entity.Account;

@RestController
public class AccountController {
	private final AccountService accountService;
	private final AccountModelAssembler accountModelAssembler;

	public AccountController(AccountService accountService, AccountModelAssembler accountModelAssembler) {
		this.accountModelAssembler = accountModelAssembler;
		this.accountService = accountService;

	}

	@GetMapping("/accounts")
	public CollectionModel<EntityModel<Account>> allAccounts() {
		List<EntityModel<Account>> accounts = accountService.findAll().stream().map(accountModelAssembler::toModel)
				.collect(Collectors.toList());
		return CollectionModel.of(accounts, linkTo(methodOn(AccountController.class).allAccounts()).withSelfRel());
	}

	@GetMapping("/accounts/{accountNumber}")
	public EntityModel<Account> oneAccount(@PathVariable Long accountNumber) {
		return accountModelAssembler.toModel(accountService.findByAccountNumber(accountNumber));
	}

	@PostMapping("/accounts")
	ResponseEntity<?> newAccount(@RequestBody Account account) {
		EntityModel<Account> entityModel = accountModelAssembler.toModel(accountService.save(account));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@PutMapping("/accounts/{accountNumber}")
	ResponseEntity<?> replaceAccount(@RequestBody Account newAccount, @PathVariable Long accountNumber) {
		Account updatedAccount = accountService.updateAccount(accountNumber, newAccount);
		EntityModel<Account> accountEntityModel = accountModelAssembler.toModel(updatedAccount);
		return ResponseEntity.created(accountEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(accountEntityModel);
	}

}
