package org.ic.protrade.data.market.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ic.protrade.data.MatchScore;
import org.junit.Before;
import org.junit.Test;

public class MatchScoreTest {
	MatchScore matchScore;

	@Before
	public void setUp() {
		matchScore = new MatchScore(2, 1);
	}

	@Test
	public void testFirstPlayerScore() {
		assertEquals("Set score should be 2 - 1", 2, matchScore.getFirstPlayerScore());
	}

	@Test
	public void testSecondPlayerScore() {
		assertEquals("Set score should be 2 - 1", 1, matchScore.getSecondPlayerScore());
	}

	@Test
	public void testFirstPlayerLastName() {
		matchScore.setFirstPlayerLastName("Federer");
		assertEquals("First player should be 'Federer' ", "Federer", matchScore.getFirstPlayerLastName());
	}

	@Test
	public void testEquals() {
		MatchScore secondMatchScore = new MatchScore(2, 1);
		assertTrue("The two different match score objects with the same values should be equal", matchScore.equals(secondMatchScore));
		secondMatchScore = new MatchScore(1, 2);
		assertFalse("Two diferrent valued match score should not be equal", matchScore.equals(secondMatchScore));
	}

	@Test
	public void testHashCode() {
		assertEquals("Hash code is not as expected", 21, matchScore.hashCode());
	}
}