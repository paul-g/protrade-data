package org.ic.protrade.data.match;

import static org.junit.Assert.assertEquals;

import org.ic.protrade.data.match.SetScore;
import org.junit.Ignore;

@Ignore
public class ScoringTest {

    public ScoringTest() {
        super();
    }

    protected void assertSetScoreIs(SetScore score, int playerOneGames,
            int playerTwoGames) {
        assertEquals(playerOneGames, score.getPlayerOneGames());
        assertEquals(playerTwoGames, score.getPlayerTwoGames());
    }

    protected void assertTiebrakeScoreIs(SetScore score, int playerOnePoints,
            int playerTwoPoints) {
        assertEquals(playerOnePoints, score.getTiebreakPointsPlayerOne());
        assertEquals(playerTwoPoints, score.getTiebreakPointsPlayerTwo());
    }
}