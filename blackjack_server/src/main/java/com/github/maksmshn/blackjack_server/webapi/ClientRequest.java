package com.github.maksmshn.blackjack_server.webapi;

import javax.xml.bind.annotation.XmlRootElement;

/** A generic player request.
 * Depending on the request, any field might be null, the
 * PlayerService will attempt to deal with it anyway.
 */
@XmlRootElement
public class ClientRequest {
	public String playerId;
	public String name;
	public double topUp;
	public double bet;

	public ClientRequest() {
	}

	ClientRequest(String name) {
		this.name = name;
	}

	ClientRequest(String name, String playerId) {
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
