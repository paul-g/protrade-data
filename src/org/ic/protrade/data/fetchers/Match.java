package org.ic.protrade.data.fetchers;

import org.ic.protrade.data.match.PlayerEnum;

public class Match {

	private static final String UNKNOWN_SCORE = "-1,-1,-1,-1,-1,-1,-1";
	private static final String UNKOWN_PLAYER = "Unkown Unkown";

	private String player1 = UNKOWN_PLAYER;
	private String player2 = UNKOWN_PLAYER;
	private String player1Score = UNKNOWN_SCORE;
	private String player2Score = UNKNOWN_SCORE;
	private PlayerEnum server;

	public Match(final String player1, final String player2, final String player1Score, final String player2Score) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.player1Score = player1Score;
		this.player2Score = player2Score;
	}

	public Match() {
	}

	public Match(final String player1, final String player2, final String player1Score, final String player2Score,
			final PlayerEnum server) {
		this(player1, player2, player1Score, player2Score);
		this.server = server;
	}

	public String getPlayerScore(final PlayerEnum player) {
		if (player == PlayerEnum.PLAYER1)
			return getPlayer1Score();
		return getPlayer2Score();
	}

	public String getPlayer1Score() {
		return player1Score;
	}

	public void setPlayer1Score(final String player1Score) {
		this.player1Score = player1Score;
	}

	public String getPlayer2Score() {
		return player2Score;
	}

	public void setPlayer2Score(final String player2Score) {
		this.player2Score = player2Score;
	}

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(final String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(final String player2) {
		this.player2 = player2;
	}

	@Override
	public String toString() {
		String match = "\n";
		match += makeLine(player1, player1Score);
		match += makeLine(player2, player2Score);
		return match;
	}

	private String makeLine(final String string1, final String string2) {
		return string1 + "\t\t\t" + string2 + "\n";
	}

	public PlayerEnum getServingPlayer() {
		return server;
	}

	public int isPlayerServing(final PlayerEnum p) {
		if (server == null)
			return -1;
		if (server.equals(p))
			return 1;
		return 0;
	}
}
