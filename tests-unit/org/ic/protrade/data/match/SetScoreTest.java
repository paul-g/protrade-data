package org.ic.protrade.data.match;

import org.ic.protrade.data.match.SetScore;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetScoreTest extends ScoringTest{

    private static final String SET_NOT_FINISHED = "Set not finished!";
    private static final String SET_FINISHED_TOO_EARLY = "Set finished too early!";
    SetScore score = new SetScore();
    
    @Test
    public void sixZeroWin(){
        
        
        for (int i=0;i<5; i++) {
            score.addPlayerOneGame();
            assertFalse(SET_FINISHED_TOO_EARLY, score.isFinished());
        }
        
        assertSetScoreIs(score, 5 , 0);
        
        score.addPlayerOneGame();
        assertTrue(SET_NOT_FINISHED, score.isFinished());
    }
    
    @Test
    public void sevenFiveWin(){
        
        for (int i=0;i<5; i++) {
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            assertFalse(SET_FINISHED_TOO_EARLY, score.isFinished());
        }

        assertSetScoreIs(score, 5 , 5);
        
        score.addPlayerOneGame();
        assertFalse(SET_FINISHED_TOO_EARLY, score.isFinished());
        
        score.addPlayerOneGame();
        assertTrue(SET_NOT_FINISHED, score.isFinished());
    }
    
    
    @Test
    public void tiebreakTest(){
        
        for (int i=0;i<6; i++) {
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            assertFalse(SET_FINISHED_TOO_EARLY, score.isFinished());
        }
        
        assertSetScoreIs(score, 6 , 6);
        
        for (int i=0;i<6; i++) {
            assertTiebrakeScoreIs(score, i, i);
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            //assertFalse(score.isFinished());
        }
        
        //assertTiebrakeScoreIs(score, i, i);
        
        score.addPlayerOneGame();
        assertFalse(SET_FINISHED_TOO_EARLY, score.isFinished());
        
        score.addPlayerOneGame();
        assertSetScoreIs(score,7,6);
        assertTrue(SET_NOT_FINISHED, score.isFinished());
    }
    
}
