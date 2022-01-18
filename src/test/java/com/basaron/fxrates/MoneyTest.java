package com.basaron.fxrates;

import java.math.BigDecimal;

import org.junit.Test;

import com.basaron.fxrates.domains.Money;
import com.basaron.fxrates.domains.Rate;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MoneyTest extends TestCase {
	@Test
	public void testConversion() {
		Money usdRate = new Money(new BigDecimal(1.139166), "USD");
		Money phpRate = new Money(new BigDecimal(58.667632), "PHP");
		Rate rate = Rate.createRateInstance(usdRate, phpRate);
		Money money = new Money(new BigDecimal(8), "USD");
		Money expected = new Money(new BigDecimal(412).setScale(2), "PHP");
		Money actual = money.convert(rate);
		Assert.assertEquals(expected, actual);
	}
}
