package com.github.maksmshn.blackjack_server.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.maksmshn.blackjack_server.game.Card.Suit;

public class HandTest {

	@Test
	public void deckValue() {
		/** The value of the whole deck is (1+2+...+10+30)*4=85*4=340 */
		Deck deck = new Deck();
		Hand hand = new Hand(deck.getCards());
		int deckValue =  hand.value();
		assertEquals(340, deckValue);
	}
	
	@Test
	public void addingCards(){
		Deck deck = new Deck();
		Hand hand = new Hand();
		assertEquals(0, hand.cards.size());
		for (int i=0; i<15; i++){
			hand.addCard(deck.pickCard());
		}
		assertEquals(52 - 15, deck.getCards().size());
		assertEquals(15, hand.cards.size());
		//System.out.println(hand);
	}
	
	@Test
	public void variousHandsValues(){
		/** Mainly test whether aces are counted correctly by both low
		 * and normal value methods
		 */
		//AAAA hand
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		Hand hand = new Hand(cards);
		assertEquals(14, hand.value());
		assertEquals(4, hand.lowValue());
		//A,10 hand
		cards.clear();
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.DIAMONDS, Rank.TEN));
		hand = new Hand(cards);
		assertEquals(21, hand.value());
		assertEquals(11, hand.lowValue());
		//A,10,8 hand
		cards.clear();
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.DIAMONDS, Rank.TEN));
		cards.add(new Card(Suit.DIAMONDS, Rank.EIGHT));
		hand = new Hand(cards);
		assertEquals(19, hand.value());
		assertEquals(19, hand.lowValue());
		// A,A,10,8 hand
		cards.clear();
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		cards.add(new Card(Suit.DIAMONDS, Rank.TEN));
		cards.add(new Card(Suit.DIAMONDS, Rank.EIGHT));
		hand = new Hand(cards);
		assertEquals(20, hand.value());
		assertEquals(20, hand.lowValue());
		//10,8,A,A,A hand
		cards.clear();
		cards.add(new Card(Suit.DIAMONDS, Rank.TEN));
		cards.add(new Card(Suit.DIAMONDS, Rank.EIGHT));
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		hand = new Hand(cards);
		assertEquals(21, hand.value());
		assertEquals(21, hand.lowValue());
		cards.clear();
		//7,A,A,A,A hand
		cards.add(new Card(Suit.DIAMONDS, Rank.SEVEN));
		cards.add(new Card(Suit.DIAMONDS, Rank.ACE));
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		hand = new Hand(cards);
		assertEquals(21, hand.value());			
		assertEquals(11, hand.lowValue());
	}

}
