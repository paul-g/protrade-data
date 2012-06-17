package org.ic.protrade.data.demo;

import java.io.Console;
import java.util.Scanner;

public class ConsoleWrapper {

	private final Console console;

	public ConsoleWrapper(final Console console) {
		this.console = console;
	}

	public char[] readLine(final String msg, final Object... arguments) {
		if (console != null)
			return console.readPassword(msg, arguments);
		else {
			System.out.format(msg, arguments);
			final Scanner sc = new Scanner(System.in);
			final String password = sc.next();
			System.out.println("\n");
			return password.toCharArray();
		}
	}

	public boolean passwordMaskingDisabled() {
		return console == null;
	}

}