package org.ic.protrade.data.demo;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.ic.protrade.data.exceptions.NoSuchMatchException;
import org.ic.protrade.data.fetchers.LivexScoreFetcher.LivexMatchType;
import org.ic.protrade.data.fetchers.Match;
import org.ic.protrade.data.fetchers.LivexScoreFetcher;
import org.ic.protrade.data.parsers.LivexScoreParser;

public final class ScoreDemo {

	private static final int REPETITIONS = 10000;
	private static final Logger log = Logger.getLogger(ScoreDemo.class);
	private static String player1 = "DimitrovGrigor";
	private static String player2 = "NalbandianDavid";

	private static String scores = null;

	private ScoreDemo() {
	}

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

	private static String prevScorePlayer1 = "";
	private static String prevScorePlayer2 = "";

	private static void fetchScores() {
		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				log.debug("Starting to fetch scores");
				scores = LivexScoreFetcher.fetchScores(LivexMatchType.ALL);
				log.debug("Scores fetched: " + scores);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (scores != null) {

				System.out.println("Updating score...");
				boolean changed = false;
				try {
					Match match = LivexScoreParser.getMatch(player1, player2,
							scores);
					log.info("Parsed score: " + match.toString());

					if (!match.getPlayer1Score().equals(prevScorePlayer1)) {
						prevScorePlayer1 = new String(match.getPlayer1Score());
						changed = true;
					}

					if (!match.getPlayer2Score().equals(prevScorePlayer2)) {
						prevScorePlayer2 = new String(match.getPlayer2Score());
						changed = true;
					}

					if (changed) {
						System.out.println("New score: " + match.toString());
					} else {
						System.out.println("No change!");
					}

				} catch (NoSuchMatchException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}

			}
		}
	}
}
