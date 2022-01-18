package com.basaron.fxrates.domains;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
	private BigDecimal value;
	private String currency;
	
	private Money() { }
	
	public Money(BigDecimal value, String currency) {
		this.value = value;
		this.currency = currency;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public Money convert(Rate rate) {
//		Not sure with the rounding part, tried searching and alot of people said that half even is the most common and unbiased for money
		return new Money(rate.getToRate().getValue().multiply(value).setScale(2, RoundingMode.HALF_EVEN), rate.getToRate().getCurrency());
	}

	@Override
	public int hashCode() {
		return Objects.hash(currency, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		return Objects.equals(currency, other.currency) && Objects.equals(value, other.value);
	}
}
