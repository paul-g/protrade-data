package org.ic.protrade.data.demo;

import java.io.*;
import java.util.List;

import org.apache.log4j.Logger;
import org.ic.protrade.data.exceptions.*;
import org.ic.protrade.data.fetchers.*;
import org.ic.protrade.data.fetchers.LivexScoreFetcher.LivexMatchType;
import org.ic.protrade.data.fetchers.Match;
import org.ic.protrade.data.market.connection.*;
import org.ic.protrade.data.match.*;
import org.ic.protrade.data.parsers.LivexScoreParser;

public final class RecordingDemo {

	private static final Logger log = Logger.getLogger(RecordingDemo.class);
	private static String player1;
	private static String player2;

	private static int timeout;
	private static String username;
	private static String recordPath;
	private static FileOutputStream fout;

	private RecordingDemo() {
	}

	public static void main(final String args[]) throws IOException, LoginFailedException {
		if (args.length == 5) {
			player1 = args[0];
			player2 = args[1];
			timeout = Integer.parseInt(args[2]);
			username = args[3];
			recordPath = args[4];
		} else {
			System.out
					.format("Usage: %s <playerOneLastname> <playerTwoLastname> <updateIntervalMs> <betfairUsername> <pathToRecordingFile>\n",
							RecordingDemo.class.getSimpleName());
			System.exit(1);
		}

		final File f = new File(recordPath);
		fout = new FileOutputStream(f);

		log.info("Starting score fetch");

		final ConsoleWrapper console = new ConsoleWrapper(System.console());

		if (console.passwordMaskingDisabled()) {
			System.out.println("A problem occurred during initialization! Password masking is disabled.");
		}

		final String password = new String(console.readLine("%s's password: ", username));

		System.out.println("Logging into Betfair...");
		BetfairConnectionHandler.login(username, password);
		System.out.println("Login successful!");

		final List<Tournament> tournaments = BetfairConnectionHandler.getTournamentsData();

		LiveMatch match = null;

		for (final Tournament t : tournaments) {
			for (final LiveMatch m : t.getMatches()) {

				final String p1Name = m.getPlayerOne().getLastname();
				final String p2Name = m.getPlayerTwo().getLastname();
				// System.out.format(" '%s' '%s' '%s' \n", m.getName(), p1Name, p2Name);
				if (((p1Name.equals(player1) && p2Name.equals(player2)) || (p1Name.equals(player2) && p2Name
						.equals(player1)))) {
					match = m;
					break;
				}
			}
			if (match != null) {
				break;
			}
		}

		if (match != null) {
			System.out.format("Match found on betfair: '%s'\n", match.getName());
			System.out.println("Getting market data...");
			match.addMarketData(BetfairExchangeHandler.getCompressedMatchOddsMarketData(match));

			System.out.format("Starting recording in: '%s'\n", recordPath);

			fout.write(new String("Time, Player, LPM, Points, Set1, Set2, Set3, Set4, Set5, Server\n").getBytes());
			while (true) {
				try {
					fetchScores(match);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Match not found on Betfair!");
		}

	}

	private static void fetchScores(final LiveMatch liveMatch) throws IOException {
		String scores = null;
		try {
			Thread.sleep(timeout);
		} catch (final InterruptedException e1) {
			e1.printStackTrace();
		}

		liveMatch.addMarketData(BetfairExchangeHandler.getCompressedMatchOddsMarketData(liveMatch));
		Match match = new Match("", "", "", "");
		try {
			scores = LivexScoreFetcher.fetchScores(LivexMatchType.ALL);

			if (scores != null) {
				match = LivexScoreParser.getMatch(player1, player2, scores);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final NoSuchMatchException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		final String data = writeData(match, liveMatch);
		System.out.print(data);
		fout.write(data.getBytes());
		fout.flush();
	}

	private static String writeData(final Match match, final LiveMatch liveMatch) {
		final long timestamp = System.currentTimeMillis();
		final String player1Data = String.format(" %d, %s, %s, %s\n", timestamp, liveMatch.getPlayerOne(), liveMatch
				.getLastMarketData().getLastPriceMatched(PlayerEnum.PLAYER1), match.getPlayer1Score());
		final String player2Data = String.format(" %d, %s, %s, %s\n", timestamp, liveMatch.getPlayerTwo(), liveMatch
				.getLastMarketData().getLastPriceMatched(PlayerEnum.PLAYER2), match.getPlayer2Score());
		return player1Data + player2Data;
	}
}
