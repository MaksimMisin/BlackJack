package com.github.maksmshn.blackjack_client.web_game;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.github.maksmshn.blackjack_client.web_game.model.ClientRequest;
import com.github.maksmshn.blackjack_client.web_game.model.ServerReply;

public class RESTClient {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static Client client;
	/** Configure client */
	static {
		ClientConfig configuration = new ClientConfig();
		configuration.property(ClientProperties.CONNECT_TIMEOUT, 10000);
	    configuration.property(ClientProperties.READ_TIMEOUT, 10000);
	    client = ClientBuilder.newClient(configuration);
	}
	private final String applicationPath = "/blackjack_server/webapi/players";
	private WebTarget baseTarget;
	private WebTarget playerTarget;
	private String username;
	private String playerId;

	/** Set up path to server.
	 * @throws MalformedURLException */
	public RESTClient(String serverAddress, String username)
			throws URISyntaxException, MalformedURLException {
		logger.trace("Creating new instance of rest client.");
		this.username = username;
		//strip possible / from the end of address and add http if necessary
		if (serverAddress.endsWith("/")) {
			serverAddress = serverAddress.substring(0, serverAddress.length() - 1);
		}
		if (!serverAddress.startsWith("http://")) {
			serverAddress = "http://" + serverAddress;
		}
		URL serverURL = new URL(serverAddress);
		URL path = new URL(serverURL, applicationPath);
		baseTarget = client.target(path.toURI());
	}

	/** Returns null on successful login, otherwise returns an
	 * error message.
	 */
	public String login(){
		ClientRequest login = new ClientRequest(username);
	    Response response = baseTarget
	    		.request(MediaType.APPLICATION_JSON)
	    		.post(Entity.json(login));
	    if (response.getStatus() != 201) {
	    	logger.info(
	    			"Unsuccessful login attempt for {} into {}",
	    			username, baseTarget.toString());
	    	if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
	    		return "The login was unsuccessful, but the server did not"
	    				+ "return an error. You might want ot check URL: "
	    				+ baseTarget.toString() +  " with your browser";
	    	} else {
	    		return response.readEntity(String.class);
	    	}
	    } else {
	    	//successful login
	    	logger.info("User {} successfully loged in",username);
	    	ServerReply sr = response.readEntity(ServerReply.class);
	    	playerId = sr.getPlayerId();
	    	playerTarget = baseTarget.path(username);
	    	return null;
	    }
	}

	/** GET serverURL/webapi/players/{username} */
	public String getPlayerInfo(){
		return playerTarget
				.request(MediaType.APPLICATION_JSON)
				.get(String.class);
	}

	/** POST serverURL/webapi/players/{username}/topUp */
	public String topUp(double amount){
		ClientRequest cr = new ClientRequest(username, playerId);
		cr.setTopUp(amount);
		WebTarget topup = playerTarget.path("topUp");
		return topup
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(cr))
				.readEntity(String.class);
	}

	/** POST serverURL/webapi/players/{username}/bet */
	public String bet(double betAmount){
		ClientRequest cr = new ClientRequest(username, playerId);
		cr.setBet(betAmount);
		WebTarget bet = playerTarget.path("bet");
		return bet
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(cr))
				.readEntity(String.class);
	}

	/** POST serverURL/webapi/players/{username}/stand */
	public String stand(){
		ClientRequest cr = new ClientRequest(username, playerId);
		WebTarget stand = playerTarget.path("stand");
		return stand
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(cr))
				.readEntity(String.class);
	}

	/** POST serverURL/webapi/players/{username}/hit */
	public String hit(){
		ClientRequest cr = new ClientRequest(username, playerId);
		WebTarget hit = playerTarget.path("hit");
		return hit
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(cr))
				.readEntity(String.class);
	}

	/** DELETE serverURL/webapi/players/{username}/hit
	 * Note that for the delete verb the playerId should
	 * be included into the header and not body.*/
	public String logout(){
		return playerTarget
				.request(MediaType.APPLICATION_JSON)
				.header("playerId", playerId)
				.delete()
				.readEntity(String.class);
	}

	public String getApplicationPath() {
		return applicationPath;
	}

	public static Client getClient() {
		return client;
	}

	public WebTarget getBaseTarget() {
		return baseTarget;
	}

	public WebTarget getPlayerTarget() {
		return playerTarget;
	}

	public String getUsername() {
		return username;
	}

	public String getPlayerId() {
		return playerId;
	}


}
