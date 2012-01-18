package org.ic.protrade.data.market.connection;

import static org.ic.protrade.data.utils.Pair.pair;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.ic.protrade.data.MatchScore;
import org.ic.protrade.data.market.EventBetfair;
import org.ic.protrade.data.market.EventMarketBetfair;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.market.connection.ExchangeAPI.Exchange;
import org.ic.protrade.data.market.connection.InflatedCompleteMarketPrices.InflatedCompletePrice;
import org.ic.protrade.data.market.connection.InflatedCompleteMarketPrices.InflatedCompleteRunner;
import org.ic.protrade.data.market.connection.InflatedMarketPrices.InflatedPrice;
import org.ic.protrade.data.market.connection.InflatedMarketPrices.InflatedRunner;
import org.ic.protrade.data.match.LiveMatch;
import org.ic.protrade.data.utils.Pair;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.Market;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.Runner;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventsResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.MarketSummary;

public class BetfairExchangeHandler extends BetfairConnectionHandler {
	private static Logger log = Logger.getLogger(BetfairExchangeHandler.class);
	private static final String MATCH_ODDS_MARKET_NAME = "Match Odds";
	private static final String SET_BETTING_MARKET_NAME = "Set Betting";
	private static int queriesNumber = 0;

	// returns the match odds market data info
	public static MOddsMarketData getMatchOddsMarketData(LiveMatch match) {
		EventBetfair eventBetfair = match.getEventBetfair();
		int marketId = -1;
		Exchange exchange = Exchange.UK;
		queriesNumber++;
		for (EventMarketBetfair emb : eventBetfair.getChildren()) {
			if (emb instanceof MarketBetfair
					&& emb.getName().equals(MATCH_ODDS_MARKET_NAME)) {
				marketId = emb.getBetfairId();
				exchange = ((MarketBetfair)emb).getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
			}
		}
		MOddsMarketData modds = new MOddsMarketData();
		try {
			if (marketId == -1) {
				queriesNumber++;
				GetEventsResp resp = GlobalAPI.getEvents(apiContext,
						eventBetfair.getBetfairId());
				// add the list of possible markets
				MarketSummary[] markets = resp.getMarketItems()
						.getMarketSummary();
				if (markets == null) {
					markets = new MarketSummary[] {};
				}
				for (MarketSummary ms : markets) {
					if (ms.getMarketName().equals(MATCH_ODDS_MARKET_NAME)) {
						marketId = ms.getMarketId();
						exchange = ms.getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
					}
				}
			}
			// create the string to display the Match Odds
			if (marketId != -1) {
				modds = getMatchOddsData(!match.isNamesSet(), marketId, exchange);
			}
		} catch (Exception e) {
			log.info("Error fetching market info for the match - "
					+ e.getMessage());
		}
		// System.out.println("After Match odds get: " + queriesNumber);
		return modds;
	}

	private static MOddsMarketData getMatchOddsData(boolean getNames,
			int marketId, Exchange exchange) throws Exception {
		MOddsMarketData modds = new MOddsMarketData();
		Market selectedMarket = null;
		if (getNames) {
			selectedMarket = ExchangeAPI.getMarket(exchange, apiContext,
					marketId);
		}
		InflatedMarketPrices prices = ExchangeAPI.getMarketPrices(exchange,
				apiContext, marketId);
		modds.setExchange(exchange.toString());
		// modds.setDate(selectedMarket.getMarketTime().getTime());
		// modds.setMatchStatus(selectedMarket.getMarketStatus().toString());//prices.getMarketStatus()
		modds.setMatchStatus(prices.getMarketStatus());
		// modds.setLocation(selectedMarket.getCountryISO3());
		modds.setDelay(prices.getInPlayDelay());

		int i = 0;
		for (InflatedRunner r : prices.getRunners()) {
			Runner marketRunner = null;
			if (getNames) {
				for (Runner mr : selectedMarket.getRunners().getRunner()) {
					if (mr.getSelectionId() == r.getSelectionId()) {
						marketRunner = mr;
						break;
					}
				}
			}
			if (i == 0) {
				modds.setPl1LastMatchedPrice(r.getLastPriceMatched());
				modds.setPlayer1TotalAmountMatched(r.getTotalAmountMatched());
				if (getNames) {
					modds.setPlayer1(marketRunner.getName());
				}
				modds.setPl1Back(setBackValues(r));
				modds.setPl1Lay(setLayValues(r));
				modds.setPlayer1SelectiondId(r.getSelectionId());
			} else {
				modds.setPl2LastMatchedPrice(r.getLastPriceMatched());
				modds.setPlayer2TotalAmountMatched(r.getTotalAmountMatched());
				if (getNames) {
					modds.setPlayer2(marketRunner.getName());
				}
				modds.setPl2Back(setBackValues(r));
				modds.setPl2Lay(setLayValues(r));
				modds.setPlayer2SelectionId(r.getSelectionId());
			}
			i++;
		}
		return modds;
	}

	// returns the set betting market data info
	public static SetBettingMarketData getSetBettingMarketData(LiveMatch match) {
		queriesNumber++;
		EventBetfair eventBetfair = match.getEventBetfair();
		SetBettingMarketData setBettingData = new SetBettingMarketData();
		int marketId = -1;
		Exchange exchange = Exchange.UK;
		for (EventMarketBetfair emb : eventBetfair.getChildren()) {
			if (emb instanceof MarketBetfair
					&& emb.getName().equals(SET_BETTING_MARKET_NAME)) {
				marketId = emb.getBetfairId();
				exchange = ((MarketBetfair)emb).getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
			}
		}
		try {
			if (marketId == -1) {
				queriesNumber++;
				GetEventsResp resp = GlobalAPI.getEvents(apiContext,
						eventBetfair.getBetfairId());
				// add the list of possible markets
				MarketSummary[] markets = resp.getMarketItems()
						.getMarketSummary();
				if (markets == null) {
					markets = new MarketSummary[] {};
				}
				for (MarketSummary ms : markets) {
					if (ms.getMarketName().equals(SET_BETTING_MARKET_NAME)) {
						// marketOdds = ms;
						marketId = ms.getMarketId();
						exchange = ms.getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
					}
				}
			}
			// create the string to display the Match Odds
			if (marketId != -1) {
				setBettingData = getSetBettingData(match, marketId, exchange);
			}
		} catch (Exception e) {
			log.info("Error fetching market info for the match - "
					+ e.getMessage());
		}
		return setBettingData;
	}

	// returns the match odds and the set betting market data info
	public static CompleteMarketData getCompleteMarketData(LiveMatch match) {
		CompleteMarketData completeMarketData = new CompleteMarketData();
		EventBetfair eventBetfair = match.getEventBetfair();
		SetBettingMarketData setBettingData = new SetBettingMarketData();
		MOddsMarketData mOddsData = new MOddsMarketData();
		queriesNumber++;
		int setBettingMarketId = -1;
		int mOddsMarketId = -1;
		Exchange exchange = Exchange.UK;

		try {
			if (setBettingMarketId == -1 || mOddsMarketId == -1) {
				GetEventsResp resp = GlobalAPI.getEvents(apiContext,
						eventBetfair.getBetfairId());
				queriesNumber++;
				// add the list of possible markets
				MarketSummary[] markets = resp.getMarketItems()
						.getMarketSummary();
				if (markets == null) {
					markets = new MarketSummary[] {};
				}
				for (MarketSummary ms : markets) {
					if (ms.getMarketName().equals(SET_BETTING_MARKET_NAME)) {
						setBettingMarketId = ms.getMarketId();
						exchange = ms.getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
					}
					if (ms.getMarketName().equals(MATCH_ODDS_MARKET_NAME)) {
						mOddsMarketId = ms.getMarketId();
					}
				}
			}
			// create the string to display the Match Odds
			if (setBettingMarketId != -1) {
				setBettingData = getSetBettingData(match, setBettingMarketId, exchange);
			}
			if (mOddsMarketId != -1) {
				mOddsData = getMatchOddsData(!match.isNamesSet(), mOddsMarketId, exchange);
			}
		} catch (Exception e) {
			log.info("Error fetching market info for the match - "
					+ e.getMessage());
		}
		completeMarketData.setmOddsMarketData(mOddsData);
		completeMarketData.setSetBettingMarketData(setBettingData);
		return completeMarketData;
	}

	private static SetBettingMarketData getSetBettingData(LiveMatch match,
			int setBettingMarketId, Exchange exchange) throws Exception {
		SetBettingMarketData setBettingData = new SetBettingMarketData();
		Market selectedMarket = ExchangeAPI.getMarket(exchange, apiContext,
				setBettingMarketId);
		InflatedMarketPrices prices = ExchangeAPI.getMarketPrices(exchange,
				apiContext, selectedMarket.getMarketId());

		for (InflatedRunner r : prices.getRunners()) {
			Runner marketRunner = null;
			for (Runner mr : selectedMarket.getRunners().getRunner()) {
				if (mr.getSelectionId() == r.getSelectionId()) {
					marketRunner = mr;
					break;
				}
			}
			MatchScore matchScore = MatchScore.getMatchScore(marketRunner
					.getName());
			if (matchScore.getFirstPlayerLastName().equals(
					match.getPlayerTwo().getLastname())) {
				matchScore = new MatchScore(matchScore.getSecondPlayerScore(),
						matchScore.getFirstPlayerScore());
			}
			// r.getSelectionId gives the market selection id
			MarketPrices marketPrices = new MarketPrices();
			marketPrices.setBackPrices(setBackValues(r));
			marketPrices.setLayPrices(setLayValues(r));
			setBettingData.addMatchScoreMarketPrices(matchScore, marketPrices);
		}
		return setBettingData;
	}

	// Returns the string containing the given market info
	@SuppressWarnings("unused")
	private static String showMarket(Exchange exch, Market m,
			InflatedMarketPrices prices) {
		String msg = "";
		msg += ("Market: " + m.getName() + "(" + m.getMarketId() + ") on the "
				+ exch + " exchange:")
				+ "\n";
		msg += ("   Start time     : " + m.getMarketTime().getTime()) + "\n";
		msg += ("   Status         : " + m.getMarketStatus()) + "\n";
		msg += ("   Location       : " + m.getCountryISO3()) + "\n";
		msg += ("") + "\n";

		msg += ("Runners:") + "\n";
		for (InflatedRunner r : prices.getRunners()) {
			Runner marketRunner = null;

			for (Runner mr : m.getRunners().getRunner()) {
				if (mr.getSelectionId() == r.getSelectionId()) {
					marketRunner = mr;
					break;
				}
			}

			String bestLay = "";
			if (r.getLayPrices().size() > 0) {
				InflatedPrice p = r.getLayPrices().get(0);
				bestLay = String.format("%,10.2f %s @ %,6.2f",
						p.getAmountAvailable(), prices.getCurrency(),
						p.getPrice());
			}

			String bestBack = "";
			if (r.getBackPrices().size() > 0) {
				InflatedPrice p = r.getBackPrices().get(0);
				bestBack = String.format("%,10.2f %s @ %,6.2f",
						p.getAmountAvailable(), prices.getCurrency(),
						p.getPrice());
			}

			msg += (String
					.format("%20s (%6d): Matched Amount: %,10.2f, Last Matched: %,6.2f, Best Back %s, Best Lay:%s",
							marketRunner.getName(), r.getSelectionId(),
							r.getTotalAmountMatched(), r.getLastPriceMatched(),
							bestBack, bestLay))
					+ "\n";
		}
		msg += ("") + "\n";
		return msg;
	}

	private static ArrayList<Pair<Double, Double>> setBackValues(
			InflatedRunner r) {
		ArrayList<Pair<Double, Double>> result = new ArrayList<Pair<Double, Double>>();
		for (InflatedPrice p : r.getBackPrices()) {
			result.add(pair(p.getPrice(), p.getAmountAvailable()));
		}
		return result;
	}

	private static ArrayList<Pair<Double, Double>> setLayValues(InflatedRunner r) {
		ArrayList<Pair<Double, Double>> result = new ArrayList<Pair<Double, Double>>();
		for (InflatedPrice p : r.getLayPrices()) {
			result.add(pair(p.getPrice(), p.getAmountAvailable()));
		}
		return result;
	}

	public static MOddsMarketData getCompressedMatchOddsMarketData(
			LiveMatch match) {
		EventBetfair eventBetfair = match.getEventBetfair();
		int marketId = -1;
		Exchange exchange = Exchange.UK;
		queriesNumber++;
		for (EventMarketBetfair emb : eventBetfair.getChildren()) {
			if (emb instanceof MarketBetfair
					&& emb.getName().equals(MATCH_ODDS_MARKET_NAME)) {
				marketId = emb.getBetfairId();
				exchange = ((MarketBetfair)emb).getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
			}
		}
		MOddsMarketData modds = new MOddsMarketData();
		try {
			if (marketId == -1) {
				queriesNumber++;
				GetEventsResp resp = GlobalAPI.getEvents(apiContext,
						eventBetfair.getBetfairId());
				// add the list of possible markets
				MarketSummary[] markets = resp.getMarketItems()
						.getMarketSummary();
				if (markets == null) {
					markets = new MarketSummary[] {};
				}
				for (MarketSummary ms : markets) {
					if (ms.getMarketName().equals(MATCH_ODDS_MARKET_NAME)) {
						// marketOdds = ms;
						marketId = ms.getMarketId();
						exchange = ms.getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
					}
				}
			}
			// create the string to display the Match Odds
			if (marketId != -1) {
				int pl1SelectionId = match.getLastMarketData() == null ? 0 : match.getLastMarketData().getPlayer1SelectiondId();
				modds = getCompressedMatchOddsData(!match.isNamesSet(),
						marketId, exchange, match.getPlayerOne().getLastname(), pl1SelectionId);
			}
		} catch (Exception e) {
			log.info("Error fetching market info for the match - "
					+ e.getMessage());
		}
		return modds;
	}

	private static MOddsMarketData getCompressedMatchOddsData(boolean getNames,
			int marketId, Exchange exchange, String pl1LastName, int pl1SelectionId) throws Exception {
		MOddsMarketData modds = new MOddsMarketData();
		Market selectedMarket = null;
		if (getNames)
			selectedMarket = ExchangeAPI.getMarket(exchange, apiContext,
					marketId);
		InflatedCompleteMarketPrices prices = ExchangeAPI
				.getCompleteMarketPrices(exchange, apiContext, marketId);

		modds.setExchange(exchange.toString());
		modds.setDelay(prices.getInPlayDelay());

		int i = 0;
		for (InflatedCompleteRunner r : prices.getRunners()) {
			Runner marketRunner = null;
			if (getNames) {
				for (Runner mr : selectedMarket.getRunners().getRunner()) {
					if (mr.getSelectionId() == r.getSelectionId()) {
						marketRunner = mr;
						break;
					}
				}
			}
			if ( (getNames && marketRunner.getName().contains(pl1LastName)) 
				|| r.getSelectionId() == pl1SelectionId ) {
				modds.setPl1LastMatchedPrice(r.getLastPriceMatched());
				modds.setPlayer1TotalAmountMatched(r.getTotalAmountMatched());
				if (getNames)
					modds.setPlayer1(marketRunner.getName());
				int startIndex = getBestBackIndex(r);
				modds.setPl1Back(setBackValues(r, startIndex));
				modds.setPl1Lay(setLayValues(r, startIndex + 1));
				modds.setPlayer1SelectiondId(r.getSelectionId());
			} else {
				modds.setPl2LastMatchedPrice(r.getLastPriceMatched());
				modds.setPlayer2TotalAmountMatched(r.getTotalAmountMatched());
				if (getNames)
					modds.setPlayer2(marketRunner.getName());
				int startIndex = getBestBackIndex(r);
				modds.setPl2Back(setBackValues(r, startIndex));
				modds.setPl2Lay(setLayValues(r, startIndex + 1));
				modds.setPlayer2SelectionId(r.getSelectionId());
			}
			i++;
		}
		return modds;
	}

	private static int getBestBackIndex(InflatedCompleteRunner r) {
		int i;
		for (i = 0; i < r.getPrices().size() - 1; i++)
			if (r.getPrices().get(i + 1).getLayAmountAvailable() > 0)
				return i;
		return i;
	}

	private static ArrayList<Pair<Double, Double>> setBackValues(
			InflatedCompleteRunner r, int startIndex) {
		ArrayList<Pair<Double, Double>> result = new ArrayList<Pair<Double, Double>>();
		int i = 0;
		while (startIndex > 0 && i < 3) {
			InflatedCompletePrice p = r.getPrices().get(startIndex);
			result.add(pair(p.getPrice(), p.getBackAmountAvailable()));
			startIndex--; i++;
		}		
		return result;
	}

	private static ArrayList<Pair<Double, Double>> setLayValues(
			InflatedCompleteRunner r, int startIndex) {
		ArrayList<Pair<Double, Double>> result = new ArrayList<Pair<Double, Double>>();
		int i = 0;
		while (startIndex < r.getPrices().size() && i < 3) {
			InflatedCompletePrice p = r.getPrices().get(startIndex);
			result.add(pair(p.getPrice(), p.getLayAmountAvailable()));
			startIndex++; i++;
		}
		return result;
	}
}
