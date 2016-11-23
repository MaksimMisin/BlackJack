package com.github.maksmshn.blackjack_client.view;

import com.github.maksmshn.blackjack_client.MainApp;

import javafx.fxml.FXML;

public class RootLayoutController {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

    /**
     * Show login screen.
     */
    @FXML
    private void handleLogIn() {
    	mainApp.startLogin();
    }

    /**
     * Send to a web page.
     */
    @FXML
    private void handleAbout() {
    	mainApp.getHostServices().showDocument("https://github.com/MaksMshn/BlackJack");
    }

	/**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }


}
