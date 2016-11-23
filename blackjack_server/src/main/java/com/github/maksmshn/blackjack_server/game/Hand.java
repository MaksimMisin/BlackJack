package com.github.maksmshn.blackjack_server.game;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class Hand {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public List<Card> cards;

	public Hand() {
		this.cards = new ArrayList<Card>();
	}

	/** Only used in tests. */
	Hand(List<Card> cards) {
		this.cards = cards;
	}

	void addCard(Card card) {
		cards.add(card);
	}

	/** Blackjack value of the hand. */
	int value() {
		int value = 0;
		int aces = 0;
		for (Card card : cards) {
			Rank rank = card.getRank();
			value += rank.value;
			if (rank.equals(Rank.ACE)) {
				aces += 1;
			}
		}
		// if value exceeds 21, subtract 10 for potentially ace
		if (value > 21 && aces > 0) {
			double deficit = (value - 21) / 10.0;
			int acesRequired = (int) Math.ceil(deficit);
			if (acesRequired <= aces) {
				value = value - acesRequired * 10;
			} else {
				value = value - aces * 10;
			}
		}
		logger.trace("Hand {} has value {}", this, value);
		return value;
	}

	/** Hand value with aces counted as 1 */
	int lowValue() {
		int lowValue = 0;
		for (Card card : cards) {
			if (card.getRank().equals(Rank.ACE)) {
				lowValue += 1;
			} else {
				lowValue += card.getRank().value;
			}
		}
		return lowValue;
	}

	@Override
	public String toString() {
		return "Hand : " + cards;
	}

}
