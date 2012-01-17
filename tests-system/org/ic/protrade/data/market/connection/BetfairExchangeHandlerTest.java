package org.ic.protrade.data.market.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.ic.protrade.data.MatchScore;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.match.LiveMatch;
import org.junit.Test;

public class BetfairExchangeHandlerTest extends BetfairConnectionTest {
	private static final String SET_MARKET_DATA_WAS_NULL = "Set market data was null";
    private static final String MATCH_SCORE_MISSMATCH = "Match score missmatch";
    private static final String MARKET_DATA_WAS_NULL = "Market data was null";
    private static final String TOURNAMENT_WAS_NULL = "Tournament was null";

    @Test
	public void testMarketOdds() {
		if (setUp) {
			List<Tournament> tours = BetfairConnectionHandler
					.getTournamentsData();
			assertNotNull(TOURNAMENT_WAS_NULL, tours);
			for (Tournament t : tours) {
				MOddsMarketData data;
				for (LiveMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getMatchOddsMarketData(m);
					assertNotNull(MARKET_DATA_WAS_NULL, data);
					break;
				}
			}
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}

	@Test
	public void testSetBetting() {
		if (setUp) {
			List<Tournament> tours = BetfairConnectionHandler
					.getTournamentsData();
			assertNotNull(TOURNAMENT_WAS_NULL, tours);
			for (Tournament t : tours) {
				LiveMatch match = new LiveMatch("Federer", "Djokovic",
						t.getEventBetfair());
				SetBettingMarketData data = BetfairExchangeHandler
						.getSetBettingMarketData(match);
				assertNotNull(SET_MARKET_DATA_WAS_NULL, data);
				for (LiveMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getSetBettingMarketData(m);
					assertNotNull(SET_MARKET_DATA_WAS_NULL, data);
					break;
				}
			}
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}

	@Test
	public void testCompleteMarket() {
		if (setUp) {
			List<Tournament> tours = BetfairConnectionHandler
					.getTournamentsData();
			assertNotNull(TOURNAMENT_WAS_NULL, tours);
			for (Tournament t : tours) {
				LiveMatch match = new LiveMatch("Federer", "Djokovic",
						t.getEventBetfair());
				CompleteMarketData data = BetfairExchangeHandler
						.getCompleteMarketData(match);
				assertNotNull(MARKET_DATA_WAS_NULL, data);
				for (LiveMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getCompleteMarketData(m);
					assertNotNull(MARKET_DATA_WAS_NULL, data);
					break;
				}
			}
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}

	@Test
	public void testGetMatchScore() {
		String runnerName = "Federer 2 - 0";
		MatchScore matchScore = MatchScore.getMatchScore(runnerName);
		assertEquals(MATCH_SCORE_MISSMATCH, 2, matchScore.getFirstPlayerScore());
		assertEquals(MATCH_SCORE_MISSMATCH, 0, matchScore.getSecondPlayerScore());
		assertEquals(MATCH_SCORE_MISSMATCH, "Federer", matchScore.getFirstPlayerLastName());
	}
}
