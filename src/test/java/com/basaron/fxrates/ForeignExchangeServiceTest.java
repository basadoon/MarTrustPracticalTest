package com.basaron.fxrates;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.basaron.fxrates.domains.Money;
import com.basaron.fxrates.services.interfaces.ForeignExchangeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@RunWith(SpringRunner.class )
@SpringBootTest
public class ForeignExchangeServiceTest {
	@Autowired
	ForeignExchangeService foreignExchangeService;
	
	@Test
	public void throwIllegalArgumentOnInvalidToCurrency() throws JsonMappingException, JsonProcessingException {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			foreignExchangeService.convert(new Money(new BigDecimal(8), "USD"), "TEST");
		});
	}
	
	@Test
	public void throwIllegalArgumentOnInvalidFromCurrency() throws JsonMappingException, JsonProcessingException {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			foreignExchangeService.convert(new Money(new BigDecimal(8), "Test"), "USD");
		});
	}
	
	@Test
	public void throwWebClientResponseExceptionOnFailedResponseFromApi() throws JsonMappingException, JsonProcessingException {
		Assertions.assertThrows(WebClientResponseException.class, () -> {
			foreignExchangeService.convert(new Money(new BigDecimal(8), "TEST"), "TEST");
		});
	}
	
	@Test
	public void convertSuccessfully() {
		try {
			Assert.assertNotNull(foreignExchangeService.convert(new Money(new BigDecimal(7), "USD"), "PHP"));
		} catch (Exception e) {
			Assert.fail("Conversion failed");
		}
	}
	
	@Test
	public void getAllCurrencies() {
		try {
			JsonNode response = foreignExchangeService.getAllCurrencies();
			if(response == null || response.at("/success").isMissingNode()) {
				throw new Exception();
			}
			boolean isSuccess = response.at("/success").asBoolean();
			if(!isSuccess) {
				throw new Exception();
			}
		} catch (Exception e) {
			Assert.fail("getAllCurrencies failed");
		}
	}
}
