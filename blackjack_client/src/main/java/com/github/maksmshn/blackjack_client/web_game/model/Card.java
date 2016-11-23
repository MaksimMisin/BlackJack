package com.github.maksmshn.blackjack_client.web_game.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Card {

	public enum Suit {
		HEARTS, DIAMONDS, SPADES, CLUBS
	}

	private Suit suit;
	private Rank rank;

	public Card() {
	}

	Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "Card [" + rank + " of " + suit + "]";
	}

}
