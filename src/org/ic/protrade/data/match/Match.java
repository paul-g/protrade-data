package org.ic.protrade.data.match;

import static org.ic.protrade.data.match.PlayerEnum.casePlayer;

import java.util.*;

import org.ic.protrade.data.MatchScore;
import org.ic.protrade.data.exceptions.MatchNotFinishedException;
import org.ic.protrade.data.market.MOddsMarketData;

public abstract class Match {
	protected Player player1 = new Player();
	protected Player player2 = new Player();
	protected Score score = new Score();
	protected List<MOddsMarketData> marketDatas;
	protected String filename = null;
	protected String setBettingFilename = null;
	private int currentSet = -1;
	private List<MatchScore> impossibleScores = new ArrayList<MatchScore>();

	public abstract boolean isInPlay();

	public abstract String getName();

	public abstract PlayerEnum getWinner() throws MatchNotFinishedException;

	public abstract void addMarketData(MOddsMarketData data);

	public abstract boolean isFromFile();

	// match based statistics should be moved to a separate object
	private String[] playerOneWonLost;
	private String[] playerTwoWonLost;

	private Map<String, String[][]> statisticsMap = new HashMap<String, String[][]>();

	public Map<String, String[][]> getStatisticsMap() {
		return statisticsMap;
	}

	public void setStatisticsMap(final Map<String, String[][]> statistics) {
		statisticsMap = statistics;
	}

	public String[] getPlayerOneWonLost() {
		return playerOneWonLost;
	}

	public void setPlayerOneWonLost(final String[] playerOneWonLost) {
		this.playerOneWonLost = playerOneWonLost;
	}

	public String[] getPlayerTwoWonLost() {
		return playerTwoWonLost;
	}

	public void setPlayerTwoWonLost(final String[] playerTwoWonLost) {
		this.playerTwoWonLost = playerTwoWonLost;
	}

	public Player getPlayer(final PlayerEnum player) {
		return casePlayer(player, player1, player2);
	}

	public Player getPlayerOne() {
		return player1;
	}

	public Player getPlayerTwo() {
		return player2;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(final Score score) {
		this.score = score;
	}

	public double getLastPriceMatched(final PlayerEnum player) {
		return (getLastMarketData() == null ? -1.0 : getLastMarketData().getLastPriceMatched(player));
	}

	public MOddsMarketData getLastMarketData() {
		if (marketDatas.size() == 0)
			return null;
		return marketDatas.get(marketDatas.size() - 1);
	}

	public void setMarketDatas(final List<MOddsMarketData> marketDatas) {
		this.marketDatas = marketDatas;
	}

	public List<MOddsMarketData> getMarketDatas() {
		return marketDatas;
	}

	public String getFilename() {
		return filename;
	}

	public String getScoreAsString(final PlayerEnum player) {
		return score.toString(player);
	}

	public void setPlayer1(final Player player) {
		player1 = player;
	}

	public void setPlayer2(final Player player) {
		player2 = player;
	}

	public PlayerEnum getServer() {
		return score.getServer();
	}

	public String getSetBettingFilename() {
		return setBettingFilename;
	}

	public void setSetBettingFilename(final String setBettingFilename) {
		this.setBettingFilename = setBettingFilename;
	}

	public void setCurrentSet(final int currentSet) {
		this.currentSet = currentSet;
	}

	public int getCurrentSet() {
		return currentSet;
	}

	public void setImpossibleScores(final List<MatchScore> impossibleScores) {
		this.impossibleScores = impossibleScores;
	}

	public List<MatchScore> getImpossibleScores() {
		return impossibleScores;
	}

	public String getStatus() {
		return isInPlay() ? "In Progress" : "Not In Progress";
	}

}
