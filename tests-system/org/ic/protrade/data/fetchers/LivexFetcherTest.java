package org.ic.protrade.data.fetchers;

import java.io.IOException;

import org.ic.protrade.data.fetchers.LivexScoreFetcher.LivexMatchType;
import org.junit.Test;

import static org.junit.Assert.*;

public class LivexFetcherTest {
    private static final String NO_SCORE_FETCHED = "No score was fetched!";

    @Test
    public void testFetchScores() throws IOException{
        String score = LivexScoreFetcher.fetchScores(LivexMatchType.ALL);
        assertNotNull(NO_SCORE_FETCHED, score);
        
        score = LivexScoreFetcher.fetchScores(LivexMatchType.YESTERDAY);
        assertNotNull(NO_SCORE_FETCHED, score);
        
        score = LivexScoreFetcher.fetchScores(LivexMatchType.FINISHED);
        assertNotNull(NO_SCORE_FETCHED, score);
    }
}
