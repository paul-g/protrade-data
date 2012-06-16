package org.ic.protrade.data.parsers;

import org.apache.log4j.Logger;
import org.ic.protrade.data.exceptions.NoSuchMatchException;
import org.ic.protrade.data.fetchers.Match;

public final class LivexScoreParser {

	private static final Logger log = Logger.getLogger(LivexScoreParser.class);
	private static final int SKIP_TO_SCORE = 5;

	private LivexScoreParser(){}
	
	public static Match getMatch(String player1Name,
			String player2Name, String scoresString)
			throws NoSuchMatchException {

		String tournament = null;

		String[] all = splitAll(scoresString);

		log.debug("Split scores. Searching for match...");
		for (String string : all) {
			if (string.contains(player1Name) && string.contains(player2Name)) {
				tournament = string;
				log.debug(tournament);
			}
		}

		if (tournament == null) {
			throw new NoSuchMatchException();
		}

		log.debug("Match search finished. Match found!");

		String[] data = tournament.split("\\*");

		String tournamentName = data[1];

		log.debug("Tournament: " + tournamentName);

		String player1 = (data[2].contains(player1Name) ? player1Name : player2Name);
		log.debug("Player 1: " + player1);

		String player2 = (data[3].contains(player2Name) ? player2Name : player1Name);
		log.debug("Player 2: " + player2);

		StringBuilder player1Score = new StringBuilder();
		StringBuilder player2Score = new StringBuilder();

		for (int i = 4 + SKIP_TO_SCORE; i < 4 + SKIP_TO_SCORE + 10; i += 2) {
			player1Score.append(data[i]+",");
			player2Score.append(data[i + 1]+",");
		}

		log.debug("Player1 score : " + player1Score.toString());
		log.debug("Player2 score : " + player2Score.toString());

		return new Match(player1, player2, player1Score.toString(),
				player2Score.toString());
	}

	private static String[] splitAll(String scoresString) {
		return scoresString.split("OFF/OFF");
	}
}
