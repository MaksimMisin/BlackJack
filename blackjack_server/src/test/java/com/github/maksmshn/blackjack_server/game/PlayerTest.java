package com.github.maksmshn.blackjack_server.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.maksmshn.blackjack_server.game.Card.Suit;

public class PlayerTest {

	@Test
	public void testTopUp() {
		Player john = new Player("1", "John");
		for (int i = 0; i < 100; i++){
			john.topUp(3.14);
		}
		assertEquals(314.0, john.getBalance(), 0.00001);
	}
	
	@Test
	public void testVictory() {
		Player john = new Player("1", "John");
		john.topUp(500);
		john.bet(320);
		Table table = john.getTable();
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		Hand hand = new Hand(cards);
		List<Card> cards2 = new ArrayList<>();
		cards2.add(new Card(Suit.CLUBS, Rank.TEN));
		cards2.add(new Card(Suit.HEARTS, Rank.ACE));
		Hand hand2 = new Hand(cards2);
		table.setDealerHand(hand);
		table.setPlayerHand(hand2);
		john.stand();
		assertEquals(true, table.isFinished());
		assertEquals(640, table.getPlayerWinnings(), 1.0e-8);
		assertEquals(500 + 320, john.getBalance(), 1.0e-8);
	}
	
	@Test
	public void testDefeat() {
		Player john = new Player("1","John");
		john.topUp(500);
		john.bet(320);
		Table table = john.getTable();
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		Hand hand = new Hand(cards);
		List<Card> cards2 = new ArrayList<>();
		cards2.add(new Card(Suit.CLUBS, Rank.TEN));
		cards2.add(new Card(Suit.HEARTS, Rank.ACE));
		Hand hand2 = new Hand(cards2);
		table.setDealerHand(hand2);
		table.setPlayerHand(hand);
		john.stand();
		assertEquals(true, table.isFinished());
		assertEquals(0, table.getPlayerWinnings(), 1.0e-8);
		assertEquals(500 - 320, john.getBalance(), 1.0e-8);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void notEnoughMoney(){
		Player john = new Player("1","John");
		john.topUp(500);
		john.bet(3200);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void negativeMoney(){
		Player john = new Player("1","John");
		john.topUp(0);
	}
	

	

}
