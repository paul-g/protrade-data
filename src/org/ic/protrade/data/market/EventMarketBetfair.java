package org.ic.protrade.data.market;

import java.util.ArrayList;
import java.util.List;

public class EventMarketBetfair {
	protected List<EventMarketBetfair> children; // size() > 0 if event, no children if market
	protected String name;
	protected int betfairId;
		
	public EventMarketBetfair(String name, int betfairId) {
		this.name = name;
		this.betfairId = betfairId;
		this.children = new ArrayList<EventMarketBetfair>();
	}
	
	public List<EventMarketBetfair> getChildren() {
		return this.children;
	}
	public int getBetfairId() {
		return this.betfairId;
	}
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name;
	}
}
