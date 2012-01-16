package org.ic.protrade.data.parsers;

import java.io.IOException;

import org.ic.protrade.data.NoSuchMatchException;
import org.ic.protrade.data.fetchers.Match;
import org.junit.Test;
import static org.junit.Assert.*;

public class LivexScoreParserTest extends ParserTest {

    private static final String INCORRECT_SCORES = "Incorrect score!";
    static final String player1 = "Tomic Bernard";
    static final String player2 = "Verdasco Fernando";

    @Test
    public void testFetchScores() throws IOException, NoSuchMatchException {
        String test = getTestString(TestManager.getLivexScoreTestPath());
        Match m = LivexScoreParser.getMatch(player1, player2, test);
        assertEquals(INCORRECT_SCORES, "46.3667", m.getPlayer1Score());
        assertEquals(INCORRECT_SCORES, "67425", m.getPlayer2Score());
        
        m = LivexScoreParser.getMatch("Azarenka Victoria", "Watson Heather", test);
        assertEquals(INCORRECT_SCORES, "66000", m.getPlayer1Score());
        assertEquals(INCORRECT_SCORES, "10000", m.getPlayer2Score());
    }
}
