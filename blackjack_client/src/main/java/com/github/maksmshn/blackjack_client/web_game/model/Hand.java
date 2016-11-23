package com.github.maksmshn.blackjack_client.web_game.model;

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

	void addCard(Card card) {
		cards.add(card);
	}

	/** Blackjack value of the hand. */
	public int value() {
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

	@Override
	public String toString() {
		return "Hand : " + cards;
	}

	public List<Card> getCards() {
		return cards;
	}

}
