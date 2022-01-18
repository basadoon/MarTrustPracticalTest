package com.basaron.fxrates.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.basaron.fxrates.domains.Money;
import com.basaron.fxrates.domains.Rate;
import com.basaron.fxrates.services.interfaces.ForeignExchangeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ForeignExchangeServiceImpl implements ForeignExchangeService {
	private static final String API_LINK = "http://api.exchangeratesapi.io/v1/";
	private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(50);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final WebClient webClient = WebClient.create(API_LINK);
	private Logger logger = LoggerFactory.getLogger(ForeignExchangeServiceImpl.class);
		
	@Value("${forex.api.key}")
	private String apiKey;
	
	@Override
	public JsonNode getAllCurrencies() throws JsonMappingException, JsonProcessingException {
		logger.info("Retrieving All Currencies...");
		String allCurrenciesJson = webClient
                .get()
                .uri("symbols?access_key="+apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block(REQUEST_TIMEOUT);
		JsonNode allCurrencies = MAPPER.readTree(allCurrenciesJson);
		return allCurrencies;
	}

	@Override
	public Map<String, Object> convert(Money convertFrom, String convertToCurrency) throws JsonMappingException, JsonProcessingException, JSONException {
		String response = webClient
                .get()
                .uri("latest?access_key="+apiKey+"&symbols="+convertFrom.getCurrency()+","+convertToCurrency)
                .retrieve()
                .bodyToMono(String.class)
                .block(REQUEST_TIMEOUT);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode responseNode = MAPPER.readTree(response);
		JsonNode fromNode = responseNode.at("/rates/"+convertFrom.getCurrency());
		JsonNode toNode = responseNode.at("/rates/"+convertToCurrency);
		String notFoundMessage = getCurrencyNotFoundMessage(fromNode, toNode, convertFrom.getCurrency(), convertToCurrency);
		if(notFoundMessage != null) {
			throw new IllegalArgumentException(notFoundMessage);
		}
		Money from = new Money(new BigDecimal(fromNode.asDouble()), convertFrom.getCurrency());
		Money to = new Money(new BigDecimal(toNode.asDouble()), convertToCurrency);
		return createConversionResult(convertFrom, Rate.createRateInstance(from, to));
	}
	
	private Map<String, Object> createConversionResult(Money originalMoney, Rate rate) throws JSONException {
		Map<String, Object> conversionResult = new HashMap<>();
		conversionResult.put("result", originalMoney.convert(rate));
		conversionResult.put("rate", rate);
		return conversionResult;
	}
	
	private String getCurrencyNotFoundMessage(JsonNode fromNode, JsonNode toNode, String fromCurrency, String toCurrency) {
		if(!(fromNode.isMissingNode() || toNode.isMissingNode())) {
			return null;
		}
		
		boolean fromNodeIsNull = false;
		StringBuilder message = new StringBuilder("Failed on retrieving the rates for the following currencies: ");
		if(fromNode.isMissingNode()) {
			message.append(fromCurrency);
			fromNodeIsNull = true;
		}
		if(toNode.isMissingNode()) {
			message.append(fromNodeIsNull ? ", "+toCurrency : toCurrency);
		}
		
		return message.toString();
	}
}
