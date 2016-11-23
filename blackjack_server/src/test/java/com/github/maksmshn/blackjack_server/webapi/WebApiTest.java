package com.github.maksmshn.blackjack_server.webapi;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import com.github.maksmshn.blackjack_server.database.Database;
import com.github.maksmshn.blackjack_server.game.Hand;
import com.github.maksmshn.blackjack_server.game.Player;
import com.github.maksmshn.blackjack_server.game.Table;

import static org.junit.Assert.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;


/**
 * The JAX-RS tests were created using the tutorials from:
 * https://github.com/rest-assured/rest-assured/wiki/Usage
 * https://semaphoreci.com/community/tutorials/testing-rest-endpoints-using-rest-assured
 *
 */
public class WebApiTest {

	static Response response;
	static Map<String, Player> players = Database.getPlayers();
	Map<String, ServerReply> testReplies;
	String testPlayerName = "testPlayer";

	void createTestPlayers(int numberOfPlayers) {
		// Add mock players to run tests with
        testReplies = new HashMap<String, ServerReply>();
        for (int i = 0; i < numberOfPlayers; i++) {
        	String name = "testPlayer" + i;
            ClientRequest addPlayer = new ClientRequest(name);
            response  =
        		given().
        			contentType("application/json").
        			body(addPlayer).
        		when().
            		post("/players");
            assertEquals(201, response.getStatusCode());
            ServerReply addedPlayer = response.as(ServerReply.class);
            testReplies.put(name, addedPlayer);
        }
	}

	void removeTestPlayers() {
		for (ServerReply reply : testReplies.values()) {
			response =
					given().
						header("playerId", reply.getPlayerId()).
					when().
						delete("/players/" + reply.getName());
			assertEquals(200, response.getStatusCode());
		}
		testReplies.clear();
	}

	@BeforeClass
	public static void configureRestAssuredAndAddPlayers() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.basePath = "/blackjack_server/webapi";
        RestAssured.port = 8080;
	}

	@Test
	public void testAddingAndGetingPlayers() {
		//Check who is here
		response =
				when().
					get("/players").
				then().
					contentType(ContentType.JSON).
				extract().response();
		assertEquals(200, response.getStatusCode());
		ServerReply[] existingPlayersReplies = response.as(ServerReply[].class);
		//Create mock players and check them
		int mockPlayersToAdd = 5;
		createTestPlayers(mockPlayersToAdd);
		response =
				when().
					get("/players").
				then().
					contentType(ContentType.JSON).
				extract().response();
		assertEquals(200, response.getStatusCode());
		ServerReply[] addedPlayersReplies = response.as(ServerReply[].class);
		int numberOfAddedPlayers = addedPlayersReplies.length -
									existingPlayersReplies.length;
		assertEquals(mockPlayersToAdd, numberOfAddedPlayers);
		for (int i = 0; i < mockPlayersToAdd; i++) {
			ServerReply reply = addedPlayersReplies[i];
			String name = reply.getName();
			ServerReply originaReply = testReplies.get(name);
			assertEquals(reply.getBalance(), originaReply.getBalance(), 1.0e-6);
		}
		removeTestPlayers();
	}

	@Test
	public void testGetSinglePlayer() {
		createTestPlayers(1);
		String name = testPlayerName + 0;
		ServerReply reply = testReplies.get(name);
		response = get("/players/" + name);
		when().
			get("/players/" + name).
		then().assertThat().
			statusCode(200).
			body("name",equalTo(reply.getName())).
			body("balance",is(0.0f));
		removeTestPlayers();
	}

	@Test
	public void testAddMoney() {
		double topUp = 30;
		createTestPlayers(1);
		String name = testPlayerName + 0;
		ServerReply reply = testReplies.get(name);
		ClientRequest request = new ClientRequest(
				reply.getName(), reply.getPlayerId());
		request.setTopUp(topUp);
		given().
			contentType(ContentType.JSON).
			body(request).
		when().
			post("/players/" + name + "/topUp").
		then().
			statusCode(200).
			body("name",equalTo(reply.getName())).
			body("balance",equalTo((float) topUp));
		removeTestPlayers();
	}

	@Test
	public void testGameActions() {
		double topUp = 30;
		double bet = 3;
		createTestPlayers(1);
		String name = testPlayerName + 0;
		ServerReply reply = testReplies.get(name);
		ClientRequest request = new ClientRequest(
				reply.getName(), reply.getPlayerId());
		// Try to top up
		request.setTopUp(topUp);
		given().
			contentType(ContentType.JSON).
			body(request).
		when().
			post("/players/" + name + "/topUp").
		then().
			statusCode(200).
			body("name",equalTo(reply.getName())).
			body("balance",equalTo((float) topUp));
		request.setBet(bet);
		// Bet some money
		response =
			given().
				contentType(ContentType.JSON).
				body(request).
			when().
				post("/players/" + name + "/bet");
		ServerReply sr = response.as(ServerReply.class);
		assertEquals(topUp - bet, sr.getBalance(), 1.0e-6);
		Table table = sr.getTable();
		assertEquals(table.getBet(), bet, 1.0e-6);
		assertFalse(table.isFinished());
		Hand pHand = table.getPlayerHand();
		Hand dHand = table.getDealerHand();
		assertEquals(pHand.cards.size(), 2);
		assertEquals(dHand.cards.size(), 1);
		// Stand on your cards
		response =
			given().
				contentType(ContentType.JSON).
				body(request).
			when().
				post("/players/" + name + "/stand");
		sr = response.as(ServerReply.class);
		table = sr.getTable();
		assertEquals(table.getBet(), bet, 1.0e-6);
		assertTrue(table.isFinished());
		pHand = table.getPlayerHand();
		dHand = table.getDealerHand();
		assertEquals(pHand.cards.size(), 2);
		assertTrue(dHand.cards.size() > 1);
		// Bet again
		given().
			contentType(ContentType.JSON).
			body(request).
		when().
			post("/players/" + name + "/bet");
		// Try hitting
		response =
			given().
				contentType(ContentType.JSON).
				body(request).
			when().
				post("/players/" + name + "/hit");
		sr = response.as(ServerReply.class);
		table = sr.getTable();
		assertEquals(table.getBet(), bet, 1.0e-6);
		pHand = table.getPlayerHand();
		assertEquals(pHand.cards.size(), 3);
		removeTestPlayers();
	}

	@Test
	public void smallBet(){
		float topUp = 5.0f;
		createTestPlayers(1);
		String name = testPlayerName + 0;
		ServerReply reply = testReplies.get(name);
		ClientRequest request = new ClientRequest(
				reply.getName(), reply.getPlayerId());
		// Try to top up
		request.setTopUp(topUp);
		given().
			contentType(ContentType.JSON).
			body(request).
		when().
			post("/players/" + name + "/topUp").
		then().
			statusCode(200).
			body("name",equalTo(reply.getName())).
			body("balance",equalTo(topUp));
		request.setBet(1.0e-9);
		// Bet too little
		given().
			contentType(ContentType.JSON).
			body(request).
		when().
			post("/players/" + name + "/bet").
		then().
			statusCode(400);
		removeTestPlayers();
	}
}
