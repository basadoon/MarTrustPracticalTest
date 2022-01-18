package com.basaron.fxrates.domains;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Rate {
	private Money fromRate;
	private Money toRate;
	
	private Rate() {}
	
	private Rate(Money fromRate, Money toRate) {
		this.fromRate = fromRate;
		this.toRate = toRate;
	}
	
	public static Rate createRateInstance(Money from, Money to) {
		Money fromRate = new Money(new BigDecimal(1), from.getCurrency());
		BigDecimal toValue = fromRate.getValue().divide(from.getValue(), 50, RoundingMode.HALF_EVEN)
				.multiply(to.getValue()).setScale(2, RoundingMode.HALF_EVEN);
		Money toRate = new Money(toValue, to.getCurrency());
		return new Rate(fromRate, toRate);
	}
	
	public Money getFromRate() {
		return fromRate;
	}
	
	public Money getToRate() {
		return toRate;
	}
}
