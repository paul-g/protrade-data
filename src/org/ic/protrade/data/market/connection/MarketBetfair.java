package org.ic.protrade.data.market.connection;

import org.ic.protrade.data.market.EventMarketBetfair;

public class MarketBetfair extends EventMarketBetfair {
	// private String player1, player2;
	private int exchangeId;

	public MarketBetfair(String name, int id, int exchangeId) {
		super(name, id);
		this.exchangeId = exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}

	public int getExchangeId() {
		return exchangeId;
	}
}
