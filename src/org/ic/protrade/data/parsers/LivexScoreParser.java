package org.ic.protrade.data.parsers;

import org.apache.log4j.Logger;
import org.ic.protrade.data.exceptions.NoSuchMatchException;
import org.ic.protrade.data.fetchers.Match;
import org.ic.protrade.data.match.PlayerEnum;

public final class LivexScoreParser {

	private static final Logger log = Logger.getLogger(LivexScoreParser.class);
	private static final int SKIP_TO_SCORE = 9;
	private static final int PL2_POINTS_OFFSET = 8;
	private static int PL1_POINTS_OFFSET = 7;

	private LivexScoreParser() {
	}

	public static Match getMatch(final String player1Name, final String player2Name, final String scoresString)
			throws NoSuchMatchException {

		String match = null;

		final String[] all = scoresString.split("\\@");

		log.debug("Split scores. Searching for match...");
		for (final String string : all) {
			if (string.contains(player1Name) && string.contains(player2Name)) {
				match = string;
				log.debug(match);
			}
		}

		if (match == null)
			throw new NoSuchMatchException();

		log.debug("Match search finished. Match found!");

		final String[] data = match.split("\\*");

		final String tournamentName = data[1];

		log.debug("Tournament: " + tournamentName);

		final String player1 = (data[2].contains(player1Name) ? player1Name : player2Name);
		log.debug("Player 1: " + player1);

		final String player2 = (data[3].contains(player2Name) ? player2Name : player1Name);
		log.debug("Player 2: " + player2);

		final StringBuilder player1Score = new StringBuilder();
		final StringBuilder player2Score = new StringBuilder();

		player1Score.append(data[PL1_POINTS_OFFSET]);
		player2Score.append(data[PL2_POINTS_OFFSET]);

		for (int i = SKIP_TO_SCORE; i < (SKIP_TO_SCORE + 10); i += 2) {
			player1Score.append(", " + data[i]);
			player2Score.append(", " + data[i + 1]);
		}

		log.debug("Player1 score : " + player1Score.toString());
		log.debug("Player2 score : " + player2Score.toString());

		PlayerEnum server = null;
		if ((data[4].equals("1")) || (data[4].equals("3"))) {
			server = PlayerEnum.PLAYER1;
		} else if ((data[4].equals("2")) || (data[4].equals("4"))) {
			server = PlayerEnum.PLAYER2;
		}
		return new Match(player1, player2, player1Score.toString(), player2Score.toString(), server);
	}
}
