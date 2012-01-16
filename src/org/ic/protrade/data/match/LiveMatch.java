package org.ic.protrade.data.match;

import java.util.ArrayList;

import org.ic.protrade.data.exceptions.MatchNotFinishedException;
import org.ic.protrade.data.market.EventBetfair;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.market.connection.BetfairExchangeHandler;

/**
 * An upcoming or in play match
 * 
 * @author pg1709
 * 
 */
public class LiveMatch extends Match {

	private EventBetfair eventBetfair;
	private boolean namesSet = false;

	public boolean isNamesSet() {
		return namesSet;
	}

	public LiveMatch(String player1, String player2, EventBetfair eb) {
		this.score = new Score();
		String name = eb.getName();
		String[] names = name.split(" v ");
		if (names.length == 2) {
			this.player1.setLastname(names[0]);
			this.player2.setLastname(names[1]);
			// System.out.println(player1 + "  --- " + player2 + " ---" +
			// names[0]+names[1]);
		}
		this.eventBetfair = eb;
		setMarketDatas(new ArrayList<MOddsMarketData>());
	}

	@Override
	public String toString() {
		// return this.player1 + " vs " + this.player2;
		return this.eventBetfair.getName();
	}

	public void setEventBetfair(EventBetfair eventBetfair) {
		this.eventBetfair = eventBetfair;
	}

	public EventBetfair getEventBetfair() {
		return eventBetfair;
	}

	@Override
	public void addMarketData(MOddsMarketData marketData) {
		this.marketDatas.add(marketData);
		if (!namesSet && marketData.getPlayer1() != null) {
			setPlayerNames(marketData);
			namesSet = true;
		}
	}

	private void setPlayerNames(MOddsMarketData marketData) {
		int lastNameIndex = marketData.getPlayer1().lastIndexOf(' ');
		if (lastNameIndex > 0) {
			this.player1.setFirstname(marketData.getPlayer1().substring(0,
					lastNameIndex));
			this.player1.setLastname(marketData.getPlayer1().substring(
					lastNameIndex + 1));
			lastNameIndex = marketData.getPlayer2().lastIndexOf(' ');
			this.player2.setFirstname(marketData.getPlayer2().substring(0,
					lastNameIndex));
			this.player2.setLastname(marketData.getPlayer2().substring(
					lastNameIndex + 1));
		} else {
			this.player1.setLastname(marketData.getPlayer1());
			this.player2.setLastname(marketData.getPlayer2());
		}
	}

	@Override
	public boolean isInPlay() {
		if (this.getLastMarketData() == null)
			this.addMarketData(BetfairExchangeHandler
					.getCompressedMatchOddsMarketData(this));
		return this.getLastMarketData().getDelay() > 0;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public PlayerEnum getWinner() throws MatchNotFinishedException {
		return this.score.getWinner();
	}

	@Override
	public boolean isFromFile() {
		return false;
	}

	/*
	 * public int getMatchOddsMarketId() throws MarketNotFoundException{ for
	 * (EventMarketBetfair emb : this.eventBetfair.getChildren()) { if (emb
	 * instanceof MarketBetfair && emb.getName().equals("name")) return
	 * emb.getBetfairId(); } throw new MarketNotFoundException(); }
	 */
}
