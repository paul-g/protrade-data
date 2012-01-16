package org.ic.protrade.data.market.connection;

import java.util.HashMap;

import org.ic.protrade.data.MatchScore;

public class SetBettingMarketData {
	private HashMap<MatchScore, MarketPrices> matchScoreMarketData;

	public SetBettingMarketData() {
		this.matchScoreMarketData = new HashMap<MatchScore, MarketPrices>();
	}

	public void addMatchScoreMarketPrices(MatchScore matchScore,
			MarketPrices marketPrices) {
		this.matchScoreMarketData.put(matchScore, marketPrices);
		/*
		 * System.out.println("Added set score market data: " +
		 * matchScore.toString() + " with prices " + marketPrices.toString());
		 */
	}

	public MarketPrices getMatchScoreMarketPrices(MatchScore matchScore) {
		if (this.matchScoreMarketData.containsKey(matchScore))
			return this.matchScoreMarketData.get(matchScore);
		return null;
	}

	public MarketPrices getMatchScoreMarketPrices(int firstPlayerScore,
			int secondPlayerScore) {
		MatchScore matchScore = new MatchScore(firstPlayerScore,
				secondPlayerScore);
		return getMatchScoreMarketPrices(matchScore);
	}

	public HashMap<MatchScore, MarketPrices> getMatchScoreMarketData() {
		return matchScoreMarketData;
	}

	public void setMatchScoreMarketData(
			HashMap<MatchScore, MarketPrices> matchScoreMarketData) {
		this.matchScoreMarketData = matchScoreMarketData;
	}
}
