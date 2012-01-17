package org.ic.protrade.data.match;

import org.ic.protrade.data.match.Score;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScoreTest extends ScoringTest {
    
    private Score score = new Score();

    @Test
    public void fortyZeroWin(){
        int expectedPoints[] = {15,30,40};
        for (int i=0;i<3;i++){
            score.addPlayerTwoPoint();
            assertGameScoreIs(0,0);
            assertPointsScoreIs(0, expectedPoints[i]);
        }
        score.addPlayerTwoPoint();
        assertGameScoreIs(0,1);
        assertPointsScoreIs(0,0);
    }
    
    @Test
    public void fiftyFortyWin(){
        int max = 3;
        
        int expectedPoints[] = {15,30,40};
        for (int i=0;i<max;i++){
            score.addPlayerTwoPoint();
            assertGameScoreIs(0,0);
            assertPointsScoreIs(0, expectedPoints[i]);
        }
        
        
        int expectedPoints2[] = {15,30,40};
        for (int i=0;i<max;i++){
            score.addPlayerOnePoint();
            assertGameScoreIs(0,0);
            assertPointsScoreIs(expectedPoints2[i], 40);
        }
        
        score.addPlayerTwoPoint();
        assertGameScoreIs(0,0);
        
        score.addPlayerOnePoint();
        assertGameScoreIs(0,0);
        
        score.addPlayerOnePoint();
        assertGameScoreIs(0,0);
        
        score.addPlayerOnePoint();
        assertGameScoreIs(1,0);
        
        assertPointsScoreIs(0,0);
    }
    
    @Test
    public void newSet(){
        int [] expected = {15,30,40, 0};
        for (int i=0;i<6;i++){
            assertGameScoreIs(i, 0);
            for (int j=0;j<4;j++){
                assertGameScoreIs(i, 0);
                score.addPlayerOnePoint();
                assertPointsScoreIs(expected[j], 0);
            }
        }

        assertGameScoreIs(0,0);
        
        assertNull("There should not be a set zero!", score.getSetScore(0));
        assertNull("Third set should not have been played!", score.getSetScore(3));
        
        assertSetScoreIs(score.getSetScore(1), 6, 0);
    }
    
    @Test
    public void threeSetMatchWin(){
       int [] expected = {15,30,40, 0};
       for (int k=0;k<2;k++) {
        for (int i=0;i<6;i++){
            assertGameScoreIs(i, 0);
            for (int j=0;j<4;j++){
                assertGameScoreIs(i, 0);
                score.addPlayerOnePoint();
                assertPointsScoreIs(expected[j], 0);
            }
        }
        assertEquals("Set score is not correct", k+1, score.getPlayerOneSets());
       }
       
       assertTrue("Match not finished!", score.isFinished());
    }
    
    @Test
    public void fiveSetMatchWin(){
       score = new Score(5);
       int [] expected = {15,30,40, 0};
       for (int k=0;k<3;k++) {
        for (int i=0;i<6;i++){
            assertGameScoreIs(i, 0);
            for (int j=0;j<4;j++){
                assertGameScoreIs(i, 0);
                score.addPlayerOnePoint();
                assertPointsScoreIs(expected[j], 0);
            }
        }
        assertEquals("Incorrect set score", k+1, score.getPlayerOneSets());
       }
       
       assertTrue("Match not finished!", score.isFinished());
       
       // try to add more points but it shouldn't affect the score
       // since game is finished
       score.addPlayerOnePoint();
       assertEquals("Points score was not reset!", 0, score.getPlayerOnePoints());
       assertPointsScoreIs(0,0);
       assertSetScoreIs(score.getSetScore(3), 6, 0);
    }
    
    private void assertPointsScoreIs(int playerOnePoints, int playerTwoPoints){
        assertEquals("Player one points score does not match!" , playerOnePoints, score.getPlayerOnePoints());
        assertEquals("Player two Points score does not match!", playerTwoPoints, score.getPlayerTwoPoints());
    }
    
    private void assertGameScoreIs(int playerOneGames, int playerTwoGames){
        assertEquals("Player one game score does not match!", playerOneGames, score.getCurrentSetScore().getPlayerOneGames());
        assertEquals("Player two game score does not match!", playerTwoGames, score.getCurrentSetScore().getPlayerTwoGames());
    }
    
}