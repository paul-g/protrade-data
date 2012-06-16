package org.ic.protrade.data.fetchers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.log4j.Logger;

public final class LivexScoreFetcher {

	private static final Logger log = Logger.getLogger(LivexScoreFetcher.class);

	private static final String FINISHED_URL = "http://www.livexscores.com/xml/tfinished.txt";
	private static final String ALL_URL = "http://www.livexscores.com/xml/tall.txt";
	private static final String IN_PLAY_URL = "http://www.livexscores.com/xml/tzmena.txt";
	private static final String YESTERDAY_URL = "http://www.livexscores.com/xml/tyesterday.txt";

	private LivexScoreFetcher() {
	}

	public static String fetchScores(LivexMatchType matchType)
			throws IOException {
		String address;
		switch (matchType) {
		case YESTERDAY:
			address = YESTERDAY_URL;
			break;
		case FINISHED:
			address = FINISHED_URL;
			break;
		case IN_PLAY:
			address= IN_PLAY_URL;
		default:
			address = ALL_URL;
			break;
		}

		URL url = new URL(address);
		URLConnection conn = url.openConnection();

		StringBuilder scoresBuilder = new StringBuilder();
		InputStream is = conn.getInputStream();
		Scanner sc = new Scanner(is);
		while (sc.hasNext()) {
			scoresBuilder.append(sc.next());
		}
		return scoresBuilder.toString();
	}

	public enum LivexMatchType {
		YESTERDAY, FINISHED, IN_PLAY, ALL;
	}

}
