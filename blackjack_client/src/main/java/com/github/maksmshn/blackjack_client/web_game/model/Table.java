package com.github.maksmshn.blackjack_client.web_game.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Table {

	// The attributes are public so they can be
	// converted to JSON
	public double bet;
	public Hand playerHand = new Hand();
	public Hand dealerHand = new Hand();
	public boolean finished = false;
	public double playerWinnings = 0;

	public Table() {
	}

	public double getBet() {
		return bet;
	}

	public Hand getPlayerHand() {
		return playerHand;
	}

	public Hand getDealerHand() {
		return dealerHand;
	}

	public boolean isFinished() {
		return finished;
	}

	public double getPlayerWinnings() {
		return playerWinnings;
	}

}
