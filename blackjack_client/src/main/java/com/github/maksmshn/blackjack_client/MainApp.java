package com.github.maksmshn.blackjack_client;

import javafx.application.Application;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maksmshn.blackjack_client.view.GameWindowController;
import com.github.maksmshn.blackjack_client.view.LoginController;
import com.github.maksmshn.blackjack_client.view.RootLayoutController;
import com.github.maksmshn.blackjack_client.web_game.RESTClient;
import com.github.maksmshn.blackjack_client.web_game.model.ErrorMessage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainApp extends Application {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObjectMapper mapper = new ObjectMapper();

    public ObjectMapper getMapper() {
		return mapper;
	}

	/** The "main" method. */
    @Override
    public void start(Stage primaryStage) {
    	logger.info("Starting Blackjack client!");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Blackjack!");

        initRootLayout();
        startLogin();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
        	logger.debug("Trying to load root layout");
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            //Set main app
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            logger.error("Falied to load root layout. Quiting",e);
            throw new RuntimeException("Falied to load root layout",e);
        }
    }

    /**
     * Show login window.
     */
    public void startLogin() {
		try {
			logger.info("Opening logging window");
			//Load login window
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("/fxml/Login.fxml"));
	        AnchorPane loginWindow;
			loginWindow = (AnchorPane) loader.load();
	        rootLayout.setCenter(loginWindow);

	        //access to the main app
	        LoginController controller = loader.getController();
	        controller.setMainApp(this);

		} catch (IOException e) {
            logger.error("Falied to load login window. Quiting",e);
            throw new RuntimeException("Falied to load login window",e);
		}
    }

    /**
     * Shows the game window inside the root layout.
     */
    public void showGameWindow(RESTClient api) {
        try {
            // Load game window.
        	logger.info("Starting new game for user {}", api.getUsername());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/GameWindow.fxml"));
            AnchorPane gameWindow = (AnchorPane) loader.load();
            rootLayout.setCenter(gameWindow);

            // Give the controller access to the main app and api.
            GameWindowController controller = loader.getController();
            controller.setMainApp(this);
            controller.setApi(api);
            controller.setMapper(mapper);

        } catch (IOException e) {
            logger.warn("Falied to load game window. Trying to continue.",e);
        }
    }

    /**
     * Warn user about something.
     */
    public void showError(String description, String error){
    	//Check if this is one of the built in error messages
    	logger.debug("Displaying error message {}",error);
    	try {
			ErrorMessage er = mapper.readValue(error, ErrorMessage.class);
			error = er.getErrorMessage();
		} catch (IOException e) {
			// This is likely a server error message
			// Print all of it.
		}
    	// Display error window
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("Error!");
    	alert.setHeaderText(description);
    	alert.setContentText(error);

    	alert.showAndWait();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
