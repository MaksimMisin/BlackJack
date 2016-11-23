package com.github.maksmshn.blackjack_server.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.maksmshn.blackjack_server.game.Card.Suit;

public class TableTest {

	// *****************************
	// Player related tests
	// *****************************

	@Test
	public void sitPlayer(){
		Player john = new Player("1","John");
		Table table = new Table(john, 0.5);
		System.out.println(table);
		assertEquals(table.getPlayerHand().cards.size(), 2);
		assertEquals(table.getDealerHand().cards.size(), 1);
	}
	
	@Test
	public void dealCardsToPlayer(){
		Player john = new Player("1","John");
		Table table = new Table(john, 0.5);
		john.sitAt(table);
		for (int i = 0; i < 10; i++){
			table.dealPlayerCard();
		}
		Hand johnsHand = john.getTable().getPlayerHand();
		assertEquals(10 + 2, johnsHand.cards.size());
	}	
	
	// *****************************
	// Dealer play related tests
	// *****************************

	@Test
	public void hitBelow17() {
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.EIGHT));
		cards.add(new Card(Suit.HEARTS, Rank.EIGHT));
		Hand hand = new Hand(cards);
		Table table = new Table();
		table.setDealerHand(hand);
		table.dealerPlay();
		int handSize = table.getDealerHand().cards.size();  
		assertTrue(handSize > 2);
	}
	
	@Test
	public void standAbove17() {
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.HEARTS, Rank.EIGHT));
		Hand hand = new Hand(cards);
		Table table = new Table();
		table.setDealerHand(hand);
		table.dealerPlay();
		assertEquals(hand, table.getDealerHand());
	}
	
	@Test
	public void hitSoft17() {
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.SIX));
		Hand hand = new Hand(cards);
		Table table = new Table();
		table.setDealerHand(hand);
		table.dealerPlay();
		System.out.println(table.getDealerHand() + " " + table.getDealerHand().value());
		int handSize = table.getDealerHand().cards.size();  
		assertTrue(handSize > 2);
	}
	
	@Test
	public void multipleAces() {
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.FIVE));
		Hand hand = new Hand(cards);
		Table table = new Table();
		table.setDealerHand(hand);
		table.dealerPlay();
		int handSize = table.getDealerHand().cards.size();  
		assertTrue(handSize > 3);
	}
	
	@Test
	public void hard17WithAces() {
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		cards.add(new Card(Suit.HEARTS, Rank.FIVE));
		cards.add(new Card(Suit.HEARTS, Rank.TEN));
		Hand hand = new Hand(cards);
		Table table = new Table();
		table.setDealerHand(hand);
		table.dealerPlay();
		assertEquals(hand, table.getDealerHand());
	}
	// *****************************
	// Betting and victory decision tests
	// *****************************
	
	@Test
	public void playerPickedALotOfCards(){
		double bet = 100;
		Player john = new Player("1","John");
		Table table = new Table(john, bet);
		john.sitAt(table);
		for (int i = 0; i<11; i++){
			john.hit();
		}
		assertEquals(true, table.isFinished());
		assertEquals(0, table.getPlayerWinnings(), 0.00001);
	}
	
	@Test
	public void playerLost(){
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.TWO));
		cards.add(new Card(Suit.HEARTS, Rank.TWO));
		Hand hand = new Hand(cards);
		List<Card> cards2 = new ArrayList<>();
		cards2.add(new Card(Suit.CLUBS, Rank.TEN));
		cards2.add(new Card(Suit.HEARTS, Rank.ACE));
		Hand hand2 = new Hand(cards2);
		Table table = new Table();
		table.setBet(30.0);
		table.setPlayerHand(hand);
		table.setDealerHand(hand2);
		table.finishGame();
		assertEquals(true, table.isFinished());
		assertEquals(0.0, table.getPlayerWinnings(), 0.00001);
	}
	
	@Test
	public void playerWon(){
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.CLUBS, Rank.JACK));
		cards.add(new Card(Suit.HEARTS, Rank.TWO));
		Hand hand = new Hand(cards);
		List<Card> cards2 = new ArrayList<>();
		cards2.add(new Card(Suit.CLUBS, Rank.TEN));
		cards2.add(new Card(Suit.HEARTS, Rank.ACE));
		Hand hand2 = new Hand(cards2);
		Table table = new Table();
		table.setBet(30.0);
		table.setPlayerHand(hand2);
		table.setDealerHand(hand);
		table.finishGame();
		assertEquals(true, table.isFinished());
		assertEquals(60.0, table.getPlayerWinnings(), 0.00001);
	}

	
	@Test
	public void playerGotBlackjack(){
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		Hand hand = new Hand(cards);
		Table table = new Table();
		table.setBet(30.0);
		table.setPlayerHand(hand);
		table.finishGame();
		assertEquals(true, table.isFinished());
		assertTrue(table.getPlayerWinnings() >= 30.0);
	}
	
	@Test
	public void draw(){
		List<Card> cards = new ArrayList<>();
		cards.add(new Card(Suit.CLUBS, Rank.TEN));
		cards.add(new Card(Suit.HEARTS, Rank.ACE));
		Hand hand = new Hand(cards);
		Table table = new Table();
		table.setBet(30.0);
		table.setPlayerHand(hand);
		table.setDealerHand(hand);
		table.finishGame();
		assertEquals(true, table.isFinished());
		assertEquals(30.0, table.getPlayerWinnings(), 0.00001);
	}
}
