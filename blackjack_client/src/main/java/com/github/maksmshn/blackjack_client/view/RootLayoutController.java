package com.github.maksmshn.blackjack_client.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.maksmshn.blackjack_client.MainApp;

import javafx.fxml.FXML;

public class RootLayoutController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

    /**
     * Show login screen.
     */
    @FXML
    private void handleLogIn() {
    	logger.debug("Returning to login screen.");
    	mainApp.startLogin();
    }

    /**
     * Send to a web page.
     */
    @FXML
    private void handleAbout() {
    	logger.debug("Directing to a web page");
    	mainApp.getHostServices().showDocument("https://github.com/MaksMshn/BlackJack");
    }

	/**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
    	logger.info("Exiting gracefully");
        System.exit(0);
    }


}
