package com.bankabc.accountservices.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.abcbank.accountservices.controller.TransaferBetweenAccountsController;
import com.abcbank.accountservices.controller.request.TransferBetweenAccountsReq;
import com.abcbank.accountservices.entity.Account;
import com.abcbank.accountservices.entity.Transaction;
import com.abcbank.accountservices.service.TransferBetweenAccountsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = TransaferBetweenAccountsController.class)
public class TransaferBetweenAccountsControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TransferBetweenAccountsService transferBetweenAccountsService;

	private static Account fromAccount;
	private static Account toAccount;
	private static TransferBetweenAccountsReq transferRequest;
	private static Transaction transaction;

	@BeforeAll
	static void initializeData() {
		fromAccount = new Account(1L, "FromAcctTest", Currency.getInstance("USD"), BigDecimal.ONE, false);
		toAccount = new Account(2L, "ToAcctTest", Currency.getInstance("USD"), BigDecimal.ZERO, false);

		transferRequest = new TransferBetweenAccountsReq();
		transferRequest.setAmount(BigDecimal.ONE);
		transferRequest.setFromAccountNumber(fromAccount.getAccountNumber());
		transferRequest.setCurrency(Currency.getInstance("USD"));
		transferRequest.setToAccountNumber(toAccount.getAccountNumber());

		transaction = new Transaction(BigDecimal.ONE, Currency.getInstance("USD"), 1L, 2L);
	}

	@Test
	void transferBetweenAccounts_whenValidMethodAndUrlAndContentTypeAndRequestBody_thenReturns200() throws Exception {
		// given
		// ...
		// when
		mockMvc.perform(post("/transfer").contentType("application/json")
				.content(objectMapper.writeValueAsString(transferRequest)))
				// then
				.andExpect(status().isOk());
	}

	@Test
	void transferBetweenAccounts_whenValidInput_thenCallsDoTransfer() throws Exception {
		// given
		// ...
		// when
		mockMvc.perform(post("/transfer").contentType("application/json")
				.content(objectMapper.writeValueAsString(transferRequest)));
		// then
		ArgumentCaptor<TransferBetweenAccountsReq> transferArgumentCaptor = ArgumentCaptor
				.forClass(TransferBetweenAccountsReq.class);
		verify(transferBetweenAccountsService, times(1)).transferBetweenAccounts(transferArgumentCaptor.capture());
		assertThat(transferArgumentCaptor.getValue().getAmount()).isEqualTo(BigDecimal.ONE);
		assertThat(transferArgumentCaptor.getValue().getFromAccountNumber()).isEqualTo(1L);
		assertThat(transferArgumentCaptor.getValue().getToAccountNumber()).isEqualTo(2L);
	}

	@Test
	void transferBetweenAccounts_whenValidInput_thenReturnsRequestBody() throws Exception {
		// given
		given(transferBetweenAccountsService.transferBetweenAccounts(transferRequest)).willReturn(transaction);
		// when
		MvcResult mvcResult = mockMvc.perform(post("/transfer").contentType("application/json")
				.content(objectMapper.writeValueAsString(transferRequest))).andReturn();
		// then
		String expectedResponseBody = objectMapper.writeValueAsString(transaction);
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
	}
}
