package com.github.maksmshn.blackjack_client.view;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maksmshn.blackjack_client.MainApp;
import com.github.maksmshn.blackjack_client.util.NumberTextField;
import com.github.maksmshn.blackjack_client.web_game.RESTClient;
import com.github.maksmshn.blackjack_client.web_game.model.ErrorMessage;
import com.github.maksmshn.blackjack_client.web_game.model.Hand;
import com.github.maksmshn.blackjack_client.web_game.model.ServerReply;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;


public class GameWindowController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/** FXML elements of the UI */
    @FXML
    private Label playerMoney;
    @FXML
    private FlowPane cardArea;
    @FXML
    private FlowPane dealerPane;
    @FXML
    private FlowPane playerPane;
    @FXML
    private Slider betSlider;
    @FXML
    private NumberTextField topUpField;
    @FXML
    private NumberTextField betField;
    @FXML
    private Label dealerScore;
    @FXML
    private Label playerScore;
    @FXML
    private Label gameMessage;

    // Reference to the other classes.
    private MainApp mainApp;
	private RESTClient api;
	private ObjectMapper mapper;

	// Double comparison tollerance
	private double eps = 1.0e-7;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setApi(RESTClient api) {
		this.api = api;
	}

    public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

    @FXML
    private void handleTopUp() {
		logger.debug("Trying to top up: {}",topUpField.getText());
		if (!topUpField.getText().isEmpty()) {
			double amount = Double.parseDouble(topUpField.getText());
			updateGameWindow(api.topUp(amount));
		}
    }

    @FXML
    private void handleBet() {
		logger.debug("Trying to bet: {}",betField.getText());
		if (!betField.getText().isEmpty()) {
			double amount = Double.parseDouble(betField.getText());
			//Extra check to reduce communication with server
			if (amount <= Double.parseDouble(playerMoney.getText())) {
				updateGameWindow(api.bet(amount));
			} else {
				mainApp.showError("Game error",
						"The bet cannot exceed the balance.");
			}
		}
    }

    @FXML
    private void handleBetPlusOne() {
		logger.trace("Adding +1 to the bet.");
		if (!betField.getText().isEmpty()) {
			double amount = Double.parseDouble(betField.getText());
			betField.setText(String.format("%.2f", amount + 1));
		} else {
			betField.setText("1");
		}
    }

    @FXML
    private void handleBetPlusTen() {
		logger.trace("Adding +10 to the bet.");
		if (!betField.getText().isEmpty()) {
			double amount = Double.parseDouble(betField.getText());
			betField.setText(String.format("%.2f", amount + 10));
		} else {
			betField.setText("10");
		}
    }

    @FXML
    private void handleStand() {
		logger.debug("Player stands on his hand");
		updateGameWindow(api.stand());
    }

    @FXML
    private void handleHit() {
		logger.debug("Player hits!");
		updateGameWindow(api.hit());
    }

	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	playerMoney.setText("0");
    	playerScore.setText("");
    	dealerScore.setText("");
    	gameMessage.setText("To start the game top up some cash and press bet!");
    	betSlider.setDisable(true);
    	// To avoid rounding problems set ticks
    	betSlider.setMajorTickUnit(0.01);
    	betSlider.setSnapToTicks(true);

    	//Bellow adds listeners to UI controls...

    	//Automatically resize card area
    	cardArea.widthProperty().addListener((obs, oldWidth, newWidth) -> {
    			dealerPane.setPrefWidth(newWidth.doubleValue());
    			playerPane.setPrefWidth(newWidth.doubleValue());
    	});

    	//Bet slider
    	betSlider.valueProperty().addListener((sl, oldValue, newValue) -> {
    		betField.setText(String.format("%.2f", newValue));
    	});
    }

    private void updateGameWindow(String response) {
    	logger.info("The server response is: {}",response);
    	ServerReply sr;
    	try {
			sr = mapper.readValue(response, ServerReply.class);
	    	processServerReply(sr);
    	} catch (IOException e) {
    		String errorMessage;
			try {
				ErrorMessage er = mapper.readValue(response, ErrorMessage.class);
				errorMessage = er.getErrorMessage();
			} catch (IOException e1) {
				errorMessage = response;
			}
    		mainApp.showError("Sever error", errorMessage);
    	}
    }

    private void processServerReply(ServerReply sr) {
    	logger.debug("Sever reply is {}", sr);
    	// Round down to nearest .01.
    	// This way we don't confuse player with .001 left overs.
    	double balance = Math.floor(sr.getBalance()*100)/100;
    	playerMoney.setText(String.format("%.2f", balance));
    	if (balance > 0.0) {
	    	betSlider.setDisable(false);
	    	betSlider.setMax(balance);
    	} else {
    		betSlider.setDisable(true);
    	}
    	if (sr.getTable() != null) {
    		gameMessage.setText("");
    		Hand dealerHand = sr.getTable().getDealerHand();
    		Hand playerHand = sr.getTable().getPlayerHand();
    		//Add cards to panes
	    	HandView dealerHandView = new HandView(dealerHand);
	    	HandView playerHandView = new HandView(playerHand);
    		dealerPane.getChildren().clear();
    		playerPane.getChildren().clear();
	    	dealerPane.getChildren().addAll(dealerHandView.getCardsView());
	    	playerPane.getChildren().addAll(playerHandView.getCardsView());
	    	//Update score
	    	dealerScore.setText(Integer.toString(dealerHand.value()));
	    	playerScore.setText(Integer.toString(playerHand.value()));
	    	//Check if the game is finished
	    	if (sr.getTable().isFinished()) {
	    		double bet = sr.getTable().getBet();
	    		double winnings = sr.getTable().getPlayerWinnings();
	    		// Display game message
	    		if (winnings > bet + eps) {
	    			gameMessage.setText(
	    					String.format("Amazing, you won %.2f!", winnings));
	    		} else if (winnings < bet - eps) {
	    			gameMessage.setText("Too bad, you lost.");
	    		} else {
	    			gameMessage.setText("It's a draw! Your bet has been refunded.");
	    		}
	    		gameMessage.setText(gameMessage.getText() +
	    				" Press bet to play again.");
	    	}
    	}
    }
}


