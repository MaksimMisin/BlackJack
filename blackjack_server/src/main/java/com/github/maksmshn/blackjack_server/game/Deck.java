package com.github.maksmshn.blackjack_server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Deck {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private List<Card> cards = new ArrayList<Card>();

	public Deck() {
		for (Card.Suit suit : Card.Suit.values()) {
			for (Rank rank : Rank.values()) {
				cards.add(new Card(suit, rank));
			}
		}
		shuffle();
	}

	public List<Card> getCards() {
		return cards;
	}

	void shuffle() {
		long seed = System.nanoTime();
		Collections.shuffle(cards, new Random(seed));
	}

	Card pickCard() {
		/**
		 * Pick a card from the bottom (0) of the deck.
		 */
		Card out;
		if (cards.size() > 0) {
			out = cards.get(0);
			cards.remove(0);
		} else {
			throw new IllegalStateException(
					"Deck out of cards. This shouldn't happen.");
		}
		logger.trace("The card {} was picked from deck {}", out, this);
		return out;
	}

	@Override
	public String toString() {
		return "Deck [cards=" + cards + "]";
	}

}
