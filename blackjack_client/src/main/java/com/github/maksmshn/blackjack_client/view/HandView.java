package com.github.maksmshn.blackjack_client.view;

import java.util.ArrayList;
import java.util.List;

import com.github.maksmshn.blackjack_client.web_game.model.Card;
import com.github.maksmshn.blackjack_client.web_game.model.Hand;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class HandView {

	private final double cardWidth = 75;
	private final double cardHeight = 100;
	private final double cardCurvature = 10;
	private Hand hand;

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public HandView(Hand hand) {
		this.hand = hand;
	}

	private StackPane toView(Card card) {
		String cardText = card.getRank() + "\nof\n" + card.getSuit();
		Rectangle r = new Rectangle();
		r.setWidth(cardWidth);
		r.setHeight(cardHeight);
		r.setArcWidth(cardCurvature);
		r.setArcHeight(cardCurvature);
		r.setFill(Color.WHITE);
		Label label = new Label(cardText);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setWrapText(true);
		StackPane layout = new StackPane();
		layout.getChildren().setAll(r, label);
		layout.setMaxWidth(cardWidth);
		layout.setMaxHeight(cardHeight);
		return layout;
	}

	public List<StackPane> getCardsView() {
		List<StackPane> cards = new ArrayList<StackPane>();
		for (Card c: hand.getCards()) {
			cards.add(toView(c));
		}
		return cards;
	}

}
