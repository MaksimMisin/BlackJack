package com.github.maksmshn.blackjack_client.web_game.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientRequest {
	public String playerId;
	public String name;
	public double topUp;
	public double bet;

	public ClientRequest() {
	}

	public ClientRequest(String name) {
		this.name = name;
	}

	public ClientRequest(String name, String playerId) {
		this.name = name;
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}

	public double getTopUp() {
		return topUp;
	}

	public void setTopUp(double topUp) {
		this.topUp = topUp;
	}

	public double getBet() {
		return bet;
	}

	public void setBet(double bet) {
		this.bet = bet;
	}

	@Override
	public String toString() {
		return "ClientRequest [playerId=" + playerId + ", name=" + name
				+ ", topUp=" + topUp + ", bet=" + bet + "]";
	}

}
