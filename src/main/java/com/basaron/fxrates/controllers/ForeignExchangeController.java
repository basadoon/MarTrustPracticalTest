package com.basaron.fxrates.controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.basaron.fxrates.domains.Money;
import com.basaron.fxrates.services.interfaces.ForeignExchangeService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/forex")
public class ForeignExchangeController {
	private Logger logger = LoggerFactory.getLogger(ForeignExchangeController.class);
	private ForeignExchangeService foreignExchangeService;
	
	public ForeignExchangeController(ForeignExchangeService foreignExchangeService) {
		this.foreignExchangeService = foreignExchangeService;
	}
	
	@GetMapping("/currencies")
	public ResponseEntity<Object> getAllCurrencies() {
		try {
			return new ResponseEntity<Object>(foreignExchangeService.getAllCurrencies(), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<Object>(createResponseBody("Failed on retrieving all currencies.", HttpStatus.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/convert")
	public ResponseEntity<Object> convert(@RequestParam String from, @RequestParam String to, @RequestParam Double value) {
		try {
			Money fromMoney = new Money(new BigDecimal(value), from);
			return new ResponseEntity<Object>(foreignExchangeService.convert(fromMoney, to), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<Object>(createResponseBody("Failed on converting money.", HttpStatus.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<Object>(createResponseBody(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JSONException e) {
			return new ResponseEntity<Object>(createResponseBody("Failed building response.", HttpStatus.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Map<String, String> createResponseBody(String message, HttpStatus status) {
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("message", message);
		responseBody.put("code", String.valueOf(status.value()));
		return responseBody;
	}
	
}
