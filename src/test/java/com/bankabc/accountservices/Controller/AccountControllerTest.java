package com.bankabc.accountservices.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.abcbank.accountservices.controller.AccountController;
import com.abcbank.accountservices.service.AccountService;
import com.abcbank.accountservices.util.assembler.AccountModelAssembler;
import com.bankabc.accountservices.entity.Account;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AccountModelAssembler accountModelAssembler;

	@MockBean
	private AccountService accountService;

	private static Account account;
	private static EntityModel<Account> accountEntityModel;

	@BeforeAll
	static void initializeData() {
		account = new Account(1L, "testingAccount", Currency.getInstance("USD"), BigDecimal.ONE, false);
		accountEntityModel = EntityModel.of(account);
	}

	private void setAccountEntityModel() {
		accountEntityModel = EntityModel.of(account,
				linkTo(methodOn(AccountController.class).oneAccount(account.getAccountNumber())).withSelfRel(),
				linkTo(methodOn(AccountController.class).allAccounts()).withRel("accounts"));
	}

	@Test
	public void allAccounts_whenValidMethodAndUrlAndPathVariable_thenReturns200() throws Exception {
		// given
		// ...
		// when
		mockMvc.perform(get("/accounts"))
				// then
				.andExpect(status().isOk());
	}

	@Test
	public void allAccounts_whenValidInput_thenCallsAccountServerFindAll() throws Exception {

		// when
		mockMvc.perform(get("/accounts"));
		// then
		verify(accountService, times(1)).findAll();
	}

	@Test
	public void oneAccount_whenValidMethodAndUrlAndPathVariable_thenReturns200() throws Exception {

		// when
		mockMvc.perform(get("/accounts/{accountNumber}", account.getAccountNumber()))
				// then
				.andExpect(status().isOk());
	}

	@Test
	public void oneAccount_whenValidInput_thenCallsAccountServerFindById() throws Exception {
		// given
		Long accountNumber = account.getAccountNumber();
		// when
		mockMvc.perform(get("/accounts/{accountNumber}", accountNumber));
		// then
		ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		verify(accountService, times(1)).findByAccountNumber(longArgumentCaptor.capture());
		assertThat(longArgumentCaptor.getValue()).isEqualTo(accountNumber);
	}

	@Test
	void oneAccount_whenValidInput_thenReturnsRequestedAccountResource() throws Exception {
		// given
		Long accountNumber = account.getAccountNumber();
		setAccountEntityModel();
		given(accountService.findByAccountNumber(any(Long.class))).willReturn(account);
		given(accountModelAssembler.toModel(account)).willReturn(accountEntityModel);
		// when
		ResultActions resultActions = mockMvc.perform(get("/accounts/{accountNumber}", accountNumber));
		// then
		resultActions.andExpect(jsonPath("accountNumber")
				.value(Objects.requireNonNull(accountEntityModel.getContent()).getAccountNumber()));
		resultActions.andExpect(jsonPath("accountName").value(accountEntityModel.getContent().getAccountName()));
		resultActions.andExpect(jsonPath("currency").value(accountEntityModel.getContent().getCurrency().toString()));
		resultActions.andExpect(jsonPath("accountBalance").value(accountEntityModel.getContent().getAccountBalance()));
		resultActions.andExpect(jsonPath("isTreasury").value(accountEntityModel.getContent().isTreasury()));
		resultActions.andExpect(jsonPath("_links.self.href")
				.value(accountEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri().toString()));
		resultActions.andExpect(jsonPath("_links.accounts.href")
				.value(accountEntityModel.getRequiredLink("accounts").toUri().toString()));
	}

	@Test
	public void newAccount_whenValidMethodAndUrlAndRequestBody_thenReturns201() throws Exception {
		// given
		setAccountEntityModel();
		given(accountService.save(account)).willReturn(account);
		given(accountModelAssembler.toModel(account)).willReturn(accountEntityModel);
		// when
		ResultActions resultActions = mockMvc.perform(
				post("/accounts").contentType("application/json").content(objectMapper.writeValueAsString(account)));
		// then
		resultActions.andExpect(status().isCreated());
	}

	@Test
	public void newAccount_whenInvalidRequestBody_thenReturns400() throws Exception {
		
		// when
		ResultActions resultActions = mockMvc.perform(post("/accounts"));
		// then
		resultActions.andExpect(status().isBadRequest());
	}

	@Test
	public void newAccount_whenValidInput_thenCallsAccountServerSave() throws Exception {
		// given
		setAccountEntityModel();
		given(accountService.save(account)).willReturn(account);
		given(accountModelAssembler.toModel(account)).willReturn(accountEntityModel);
		// when
		mockMvc.perform(
				post("/accounts").contentType("application/json").content(objectMapper.writeValueAsString(account)));
		// then
		ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
		verify(accountService, times(1)).save(accountArgumentCaptor.capture());
		assertThat(accountArgumentCaptor.getValue()).isEqualTo(account);
	}

	@Test
	void newAccount_whenValidInput_thenReturnsTheSavedAccount() throws Exception {
		// given
		setAccountEntityModel();
		given(accountService.save(account)).willReturn(account);
		given(accountModelAssembler.toModel(account)).willReturn(accountEntityModel);
		// when
		ResultActions resultActions = mockMvc.perform(
				post("/accounts").contentType("application/json").content(objectMapper.writeValueAsString(account)));
		// then
		resultActions.andExpect(jsonPath("accountNumber")
				.value(Objects.requireNonNull(accountEntityModel.getContent()).getAccountNumber()));
		resultActions.andExpect(jsonPath("accountName").value(accountEntityModel.getContent().getAccountName()));
		resultActions.andExpect(jsonPath("currency").value(accountEntityModel.getContent().getCurrency().toString()));
		resultActions.andExpect(jsonPath("accountBalance").value(accountEntityModel.getContent().getAccountBalance()));
		resultActions.andExpect(jsonPath("istreasury").value(accountEntityModel.getContent().isTreasury()));
		resultActions.andExpect(jsonPath("_links.self.href")
				.value(accountEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri().toString()));
		resultActions.andExpect(jsonPath("_links.accounts.href")
				.value(accountEntityModel.getRequiredLink("accounts").toUri().toString()));
	}

	@Test
	public void replaceAccount_whenValidMethodAndUrlAndRequestBody_thenReturns201() throws Exception {
		// given
		Long accountNumber = account.getAccountNumber();
		setAccountEntityModel();
		given(accountService.updateAccount(accountNumber, account)).willReturn(account);
		given(accountModelAssembler.toModel(account)).willReturn(accountEntityModel);
		// when
		ResultActions resultActions = mockMvc.perform(put("/accounts/{accountNumber}", accountNumber)
				.contentType("application/json").content(objectMapper.writeValueAsString(account)));
		// then
		resultActions.andExpect(status().isCreated());
	}

	@Test
	void replaceAccount_whenValidInput_thenReturnsTheEditedAccount() throws Exception {
		// given
		Long accountNumber =account.getAccountNumber();
		setAccountEntityModel();
		given(accountService.updateAccount(accountNumber, account)).willReturn(account);
		given(accountModelAssembler.toModel(account)).willReturn(accountEntityModel);
		// when
		ResultActions resultActions = mockMvc.perform(put("/accounts/{accountNumber}", accountNumber)
				.contentType("application/json").content(objectMapper.writeValueAsString(account)));
		// then
		resultActions.andExpect(jsonPath("accountNumber")
				.value(Objects.requireNonNull(accountEntityModel.getContent()).getAccountNumber()));
		resultActions.andExpect(jsonPath("accountName").value(accountEntityModel.getContent().getAccountName()));
		resultActions.andExpect(jsonPath("currency").value(accountEntityModel.getContent().getCurrency().toString()));
		resultActions.andExpect(jsonPath("accountBalance").value(accountEntityModel.getContent().getAccountBalance()));
		resultActions.andExpect(jsonPath("istreasury").value(accountEntityModel.getContent().isTreasury()));
		resultActions.andExpect(jsonPath("_links.self.href")
				.value(accountEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri().toString()));
		resultActions.andExpect(jsonPath("_links.accounts.href")
				.value(accountEntityModel.getRequiredLink("accounts").toUri().toString()));
	}

}