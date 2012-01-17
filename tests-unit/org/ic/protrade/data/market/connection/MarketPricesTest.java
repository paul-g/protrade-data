package org.ic.protrade.data.market.connection;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.ic.protrade.data.utils.Pair;
import org.junit.Before;
import org.junit.Test;

public class MarketPricesTest {
	MarketPrices marketPrices;

	@Before
	public void setUp() {
		marketPrices = new MarketPrices(new ArrayList<Pair<Double, Double>>(),
				new ArrayList<Pair<Double, Double>>());
	}

	@Test
	public void testBackPrices() {
		assertEquals("There should be no back prices!", 0, marketPrices.getBackPrices().size());
	}

	@Test
	public void testLayPrices() {
		assertEquals("There should be no lay prices!", 0, marketPrices.getLayPrices().size());
	}
}
