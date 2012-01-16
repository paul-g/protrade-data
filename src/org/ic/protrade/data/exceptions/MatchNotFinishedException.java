package org.ic.protrade.data.exceptions;

public class MatchNotFinishedException extends Exception {
	private static final long serialVersionUID = 1L;

	public MatchNotFinishedException() {
		super("Match is not finished");
	}
}
