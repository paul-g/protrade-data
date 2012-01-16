package org.ic.protrade.data.market.connection;

import java.util.Date;

// Class for storing the connection status and other details of an API connection
public class APIContext {
	// The session token
	private String token;
	
	// The Last time a call was made.
	private Date lastCall;
	
	public String getToken() {
		return token;
	}
	
	public Date getLastCall() {
		return lastCall;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setLastCall(Date lastCall) {
		this.lastCall = lastCall;
	}
}
