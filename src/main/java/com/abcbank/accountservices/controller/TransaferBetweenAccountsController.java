package com.abcbank.accountservices.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abcbank.accountservices.controller.request.TransferBetweenAccountsReq;
import com.abcbank.accountservices.entity.Transaction;
import com.abcbank.accountservices.service.TransferBetweenAccountsService;

@RestController
public class TransaferBetweenAccountsController {
	private final TransferBetweenAccountsService transferBetweenAccountsService;

	public TransaferBetweenAccountsController(TransferBetweenAccountsService transferBetweenAccountsService) {
		this.transferBetweenAccountsService = transferBetweenAccountsService;
	}

	@PostMapping("/transfer")
	ResponseEntity<?> transferBetweenAccounts(@RequestBody TransferBetweenAccountsReq transferBetweenAccountsReq) {
		Transaction transaction = transferBetweenAccountsService.transferBetweenAccounts(transferBetweenAccountsReq);
		return ResponseEntity.ok(transaction);
	}
}
