package org.ic.protrade.data.demo;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.ic.protrade.data.exceptions.NoSuchMatchException;
import org.ic.protrade.data.fetchers.LivexScoreFetcher.LivexMatchType;
import org.ic.protrade.data.fetchers.Match;
import org.ic.protrade.data.fetchers.LivexScoreFetcher;
import org.ic.protrade.data.parsers.LivexScoreParser;

public final class ScoreDemo {

	private static final int REPETITIONS = 1;
	private static final Logger log = Logger.getLogger(ScoreDemo.class);
	private static String player1 = "CilicMarin";
	private static String player2 = "QuerreySam";
	
	private static String scores = null;

	private ScoreDemo(){}
	
	public static void main(String args[]) {
		if (args.length == 2) {
			player1 = args[0];
			player2 = args[1];
		}
		log.info("Starting score fetch");
		for (int i = 0; i < REPETITIONS; i++) {
			fetchScores();
		}
		log.info("Score fetched finished");

	}

	private static void fetchScores() {
		try {
			log.debug("Starting to fetch scores");
			scores = LivexScoreFetcher.fetchScores(LivexMatchType.YESTERDAY);
			log.debug("Scores fetched: " + scores);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (scores != null) {
			try {
				/*
				 * LivexMatch match = SimpleScoreParser.searchForMatch(
				 * "NieminenJarkko", "BenneteauJulien", scores);
				 */
				Match match = LivexScoreParser.getMatch(player1,
						player2, scores);
				log.info("Parsed score: " + match.toString());
			} catch (NoSuchMatchException e) {
				e.printStackTrace();
			}
		}
	}
}
