package com.github.maksmshn.blackjack_server.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String playerId;
	private String name;
	private double balance = 0.0;
	private Table table;

	public Player(String playerId, String name) {
		this.name = name;
		this.playerId = playerId;
		balance = 0;
		logger.debug("New player {} created", this);
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Table getTable() {
		return table;
	}

	void sitAt(Table table) {
		this.table = table;
		logger.debug("Player {} sit at table {}", this, table);
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}

	public void bet(double bet) {
		if (bet <= balance) {
			balance -= bet;
			Table table = new Table(this, bet);
			sitAt(table);
		} else {
			throw new IllegalArgumentException(
					"Bet must be smaller or equal to balance.");
		}
		logger.trace("Player {} made the following bet: {}", this, bet);
	}

	public void hit() {
		if (!table.isFinished()) {
			table.dealPlayerCard();
		}
	}

	public void stand() {
		table.finishGame();
		balance += table.getPlayerWinnings();
	}

	public void topUp(double cash) {
		if (cash > 0) {
			balance += cash;
		} else {
			throw new IllegalArgumentException(
					"Cannot topup negative/zero sum (" + cash + ").");
		}
		logger.debug("Player {} topped up {}", this, cash);
	}

	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", name=" + name + ", balance="
				+ balance + ", table=" + table + "]";
	}

}
