package com.github.maksmshn.blackjack_client.view;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.ws.rs.ProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.maksmshn.blackjack_client.MainApp;
import com.github.maksmshn.blackjack_client.web_game.RESTClient;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    TextField serverName;
    @FXML
    TextField playerName;
    @FXML
    Button loginButton;
    @FXML
    Label statusMessage;

    private MainApp mainApp;
    private RESTClient api;

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }

	public RESTClient getApi() {
		return api;
	}

	/**
	 * Initialize login window and register login button and
	 * text field listeners.
	 */
	@FXML
	public void initialize() {
		serverName.setText("localhost:8080");

		// Clear statusMessage when either of the fields are focused.
		serverName.focusedProperty().addListener((obs, o, n) ->
			statusMessage.setText(""));
		playerName.focusedProperty().addListener((obs, o, n) ->
			statusMessage.setText(""));

		//On click, check that name and server are valid and
		//connectable. If yes, show the main window.
		loginButton.setOnAction( e -> {
			if (isPlayerNameValid() && canConnectToTheServer()){
				mainApp.showGameWindow(api);
			}
		});
	}

	/** Check connection to the server.*/
	private boolean canConnectToTheServer() {
		String username = playerName.getText();
		String server = serverName.getText();
		logger.trace("Player name is {}",username);
		logger.trace("Server name is {}",server);
		String serverResponse = "Could not connect.";
		try {
			logger.debug("Trying to establish server conection");
			api = new RESTClient(server, username);
			serverResponse = api.login();
		} catch (URISyntaxException | MalformedURLException e) {
			logger.info("{} is an incorrect URL.", server);
			statusMessage.setText("Malformed server name.");
			statusMessage.setVisible(true);
			return false;
		} catch (ProcessingException e) {
			logger.info("Could not establish connection to {}", server);
			statusMessage.setText("Could not connect to the server.");
			statusMessage.setVisible(true);
			return false;
		}
		logger.info("Server response to login attempt {}",serverResponse);
		if (serverResponse != null){
			mainApp.showError("Login problem", serverResponse);
			return false;
		}
		return true;

	}

	/** Check player username.*/
	private boolean isPlayerNameValid() {
		String name = playerName.getText();
		logger.trace("Player name is {}",name);
		if (name.isEmpty()){
			logger.debug("The name is empty.");
			statusMessage.setText(
					"The name cannot be empty!");
			statusMessage.setVisible(true);
			return false;
		}
		//regex to match only alphanumeric names
		String alphanum = "^[a-zA-Z0-9]*$";
		boolean isNameValid = name.matches(alphanum);
		logger.trace("Checking name via regex.");
		if (!isNameValid) {
			statusMessage.setText(
					"Username can contain only alphanumeric characters.");
			statusMessage.setVisible(true);
		}
		return isNameValid;
	}

}
