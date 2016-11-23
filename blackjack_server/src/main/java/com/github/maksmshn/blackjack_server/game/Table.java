package com.github.maksmshn.blackjack_server.game;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class Table {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	// The attributes are public so they can be
	// converted to JSON
	public double bet;
	public Hand playerHand = new Hand();
	public Hand dealerHand = new Hand();
	private Deck deck = new Deck();
	public boolean finished = false;
	public double playerWinnings = 0;

	Table(Player player, double bet) {
		/** Start the game */
		this.bet = bet;
		dealerHand.addCard(deck.pickCard());
		playerHand.addCard(deck.pickCard());
		playerHand.addCard(deck.pickCard());
	}

	public Table() {
	}

	public Hand getDealerHand() {
		return dealerHand;
	}

	// Only used in tests
	void setPlayerHand(Hand playerHand) {
		this.playerHand = playerHand;
	}

	public Hand getPlayerHand() {
		return playerHand;
	}

	public double getBet() {
		return bet;
	}

	// Only used in tests
	void setBet(double bet) {
		this.bet = bet;
	}

	public double getPlayerWinnings() {
		return playerWinnings;
	}

	public boolean isFinished() {
		return finished;
	}

	// Only used in tests
	void setDealerHand(Hand dealerHand) {
		this.dealerHand = dealerHand;
	}

	void dealPlayerCard() {
		/** Player picks a card. */
		playerHand.addCard(deck.pickCard());
		if (playerHand.value() > 21) {
			finished = true;
			playerWinnings = 0;
		}
	}

	/**
	 * The player is finished. Dealer picks cards. The optimal strategy is to
	 * stand on 18 or higher and to stand on 17 if you don't have aces counted
	 * as 11.
	 */
	void dealerPlay() {
		while (dealerHand.value() < 17) {
			dealerHand.addCard(deck.pickCard());
		}
		if (dealerHand.value() == 17) {
			if (dealerHand.lowValue() < 17) {
				dealerHand.addCard(deck.pickCard());
				this.dealerPlay();
			}
		}
	}

	/** Dealer picks up cards and winnings are computed. */
	void finishGame() {
		if (!finished) {
			dealerPlay();
			finished = true;
			if (dealerHand.value() > 21) {
				playerWinnings = bet * 2;
			} else if (playerHand.value() <= 21
					&& playerHand.value() > dealerHand.value()) {
				playerWinnings = bet * 2;
			} else if (playerHand.value() == dealerHand.value()) {
				playerWinnings = bet;
			} else {
				playerWinnings = 0;
			}
		}
		logger.debug("The game near table {} is now finished.", this);

	}

	@Override
	public String toString() {
		return "Table [bet=" + bet + ", playerHand=" + playerHand + ", dealerHand=" + dealerHand + ", finished="
				+ finished + ", playerWinnings=" + playerWinnings + "]";
	}

}
