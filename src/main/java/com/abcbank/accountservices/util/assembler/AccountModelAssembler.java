package com.abcbank.accountservices.util.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.abcbank.accountservices.controller.AccountController;
import com.abcbank.accountservices.entity.Account;



@Component
public class AccountModelAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>>{
	@Override
	public EntityModel<Account> toModel(Account account) {
		return EntityModel.of(account,
				linkTo(methodOn(AccountController.class).oneAccount(account.getAccountNumber())).withSelfRel(),
				linkTo(methodOn(AccountController.class).allAccounts()).withRel("accounts"));
	}
}
