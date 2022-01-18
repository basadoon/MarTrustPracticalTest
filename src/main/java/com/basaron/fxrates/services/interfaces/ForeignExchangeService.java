package com.basaron.fxrates.services.interfaces;

import java.util.Map;

import org.json.JSONException;

import com.basaron.fxrates.domains.Money;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface ForeignExchangeService {
	public JsonNode getAllCurrencies() throws JsonMappingException, JsonProcessingException;
	public Map<String, Object> convert(Money convertFrom, String convertToCurrency) throws JsonMappingException, JsonProcessingException, JSONException;
}
