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
			System.out.format("\nUsage:\n");
			System.out
					.format("   %s <playerOneLastname> <playerTwoLastname> <updateIntervalMs> <betfairUsername> <pathToRecordingFile>\n",
							RecordingDemo.class.getSimpleName());
			help();
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
				if (((p1Name.contains(player1) && p2Name.contains(player2)) || (p1Name.contains(player2) && p2Name
						.contains(player1)))) {
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
		} else {
			System.out.format("Match not found on betfair. Market data will not be recorded!\n");
		}

		fout.write(new String("Time, Player, LPM, Points, Set1, Set2, Set3, Set4, Set5, Server\n").getBytes());

		while (true) {
			try {
				fetchScores(match);
			} catch (final Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	private static void help() {
		System.out.format("\n");
		System.out.format("   playerOne[Two]Lastname - e.g. Djokovic (order is not important)\n");
		System.out.format("   updateIntervalMs       - the interval at which to record data in milliseconds\n");
		System.out
				.format("   betfairUsername        - your betfair user account, you will be prompted for your password at startup\n");
		System.out
				.format("   pathToRecordingFile    - the absolute path to the file which will contain the recording\n");
		System.out.format("\n");
		System.out.format("Example:\n");
		final String command = "java -cp \"build/prod:lib/*\" " + RecordingDemo.class.getName();
		System.out.format("   %s Djokovic Nadal 2000 john /home/doe/recording.dat", command);
		System.out.println();
		System.out.println();
		System.out
				.format("     This will try to record betfair & score data from a match between Djokovic & Nadal. Data will be \n");
		System.out
				.format("     recorded every 2 seconds, using the betfair account \"john\" and stored in the file recording.dat.\n\n");
		System.out.println();
		System.out.format("Recorded fields:\n");
		System.out.format("   Two entries are recorded at every update, one for each player.\n");
		System.out.format("     time              - system time at which the recording was taken\n");
		System.out.format("     player            - the player's name\n");
		System.out.format("     LPM               - lowest price matched\n");
		System.out.format("     Points, Set[1..5] - match score\n");
		System.out.format("     isServing         - 1 if player is serving, 0 if opponent is serving, or -1 \n");
		System.out.format("                         if this is undefined (e.g. when the match is over)\n\n");

	}

	private static void fetchScores(final LiveMatch liveMatch) throws IOException, NoSuchMatchException {
		String scores = null;
		try {
			Thread.sleep(timeout);
		} catch (final InterruptedException e1) {
			e1.printStackTrace();
		}

		if (liveMatch != null) {
			liveMatch.addMarketData(BetfairExchangeHandler.getCompressedMatchOddsMarketData(liveMatch));
		}

		Match match = new Match();

		scores = LivexScoreFetcher.fetchScores(LivexMatchType.ALL);
		if (scores != null) {
			match = LivexScoreParser.getMatch(player1, player2, scores);
		}

		final String data = writeData(match, liveMatch);
		System.out.print(data);
		fout.write(data.getBytes());
		fout.flush();
	}

	private static String writeData(final Match match, final LiveMatch liveMatch) {
		final long timestamp = System.currentTimeMillis();

		String data = "";

		for (final PlayerEnum p : PlayerEnum.values()) {

			final String playerOne = (liveMatch == null ? getDefaultPlayerName(p) : liveMatch.getPlayer(p).toString());

			final double lastPriceMatched = (liveMatch == null ? -1.0 : liveMatch.getLastPriceMatched(p));
			data += String.format(" %d, %s, %s, %s, %d\n", timestamp, playerOne, lastPriceMatched,
					match.getPlayerScore(p), match.isPlayerServing(p));
		}

		return data;
	}

	private static String getDefaultPlayerName(final PlayerEnum p) {
		if (p == PlayerEnum.PLAYER1)
			return player1;
		return player2;
	}
}
