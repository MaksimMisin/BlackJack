package com.github.maksmshn.blackjack_server.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeckTest {

	@Test
	public void deskCreation() {
		Deck deck = new Deck();
		System.out.println(deck);
		int length = deck.getCards().size();
		assertEquals(52, length);
	}
	
	@Test
	public void cardPicking() {
		Deck deck = new Deck();
		for (int i=0; i<52; i++){
			deck.pickCard();
		}
		int deck_size = deck.getCards().size();
		assertEquals(0, deck_size);
	}
		
	@Test(expected = IllegalStateException.class)
	public void tooFewCards() {
		Deck deck = new Deck();
		for (int i=0; i<53; i++){
			deck.pickCard();
		}
	}


}
