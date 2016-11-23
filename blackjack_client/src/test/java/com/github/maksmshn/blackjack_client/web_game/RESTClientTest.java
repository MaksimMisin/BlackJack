package com.github.maksmshn.blackjack_client.web_game;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.github.maksmshn.blackjack_client.web_game.model.ErrorMessage;
import com.github.maksmshn.blackjack_client.web_game.model.ServerReply;
import com.github.maksmshn.blackjack_client.web_game.model.Table;


public class RESTClientTest {

	final String playerName = "testPlayer000";
	final String testServer = "http://localhost:8080";
	RESTClient rc;
	ObjectMapper mapper = new ObjectMapper();

	public RESTClientTest() throws MalformedURLException, URISyntaxException {
        rc = new RESTClient(testServer, playerName);
	}

    @Test
    public void testRESTClient()
            throws MalformedURLException, URISyntaxException {
        assertEquals(rc.getUsername(), playerName);
        assertEquals(testServer + "/blackjack_server/webapi/players",
                rc.getBaseTarget().getUri().toString());
    }

    @Test
    public void testLogin() throws MalformedURLException, URISyntaxException {
    	try {
	        String loginm = rc.login();
	        assertEquals(null, loginm);
		} finally {
			rc.logout();
		}
    }

    @Test
    public void testGetPlayerInfo() throws JsonParseException, JsonMappingException, IOException {
    	try {
	    	rc.login();
	    	String info = rc.getPlayerInfo();
	    	ServerReply sr = mapper.readValue(info, ServerReply.class);
	    	assertEquals(playerName, sr.getName());
	    	assertEquals(0, sr.getBalance(), 1.0e-8);
		} finally {
			rc.logout();
		}
    }

    @Test
    public void testTopUp() throws JsonParseException, JsonMappingException, IOException {
    	try {
	    	rc.login();
	    	double amount = 30.0;
	    	String json = rc.topUp(amount);
	    	ServerReply sr = mapper.readValue(json, ServerReply.class);
	    	assertEquals(amount, sr.getBalance(), 1.0e-8);
		} finally {
			rc.logout();
		}
    }

    @Test
    public void testBet() throws JsonParseException, JsonMappingException, IOException {
	    try {
	        rc.login();
	        rc.topUp(30.0);
	        String json = rc.bet(10.0);
	        ServerReply sr = mapper.readValue(json, ServerReply.class);
	        Table table = sr.getTable();
	        assertEquals(10.0, table.getBet(), 1.0e-8);
	        assertFalse(table.isFinished());
	        assertEquals(1, table.getDealerHand().getCards().size());
	        assertEquals(2, table.getPlayerHand().getCards().size());
	        assertEquals(0, table.getPlayerWinnings(), 1.0e-8);
		} finally {
			rc.logout();
		}
    }

    @Test
    public void testStand() throws JsonParseException, JsonMappingException, IOException {
    	try {
	        rc.login();
	        rc.topUp(30.0);
	        rc.bet(10.0);
	        String json = rc.stand();
	        ServerReply sr = mapper.readValue(json, ServerReply.class);
	        Table table = sr.getTable();
	        assertEquals(2, table.getPlayerHand().getCards().size());
	        assertTrue(table.getDealerHand().getCards().size() > 1);
	        assertTrue(table.isFinished());
		} finally {
			rc.logout();
		}
    }

    @Test
    public void testHit() throws JsonParseException, JsonMappingException, IOException {
    	try {
	        rc.login();
	        rc.topUp(30.0);
	        rc.bet(10.0);
	        //Hit enough times to make sure that the game is finished
	        for (int i=0; i < 10; i++) {
	        	rc.hit();
	        }
	        String json = rc.hit();
	        ServerReply sr = mapper.readValue(json, ServerReply.class);
	        Table table = sr.getTable();
	        assertTrue(table.isFinished());
	        assertTrue(table.getPlayerHand().getCards().size() > 1);
	        assertEquals(1, table.getDealerHand().getCards().size());
	        assertEquals(0, table.getPlayerWinnings(), 1.0e-8);
    	} finally {
    		rc.logout();
    	}
    }

    @Test
    public void testLoginWithExistingName()
    		throws URISyntaxException, JsonParseException, JsonMappingException, IOException{
    	rc.login();
    	RESTClient rc2 = new RESTClient(testServer, playerName);
    	String json = rc2.login();
    	try {
    		ServerReply sr = mapper.readValue(json, ServerReply.class);
	    	System.out.println(sr);
    	} catch (UnrecognizedPropertyException e) {
    		ErrorMessage er = mapper.readValue(json, ErrorMessage.class);
    		assertEquals(400, er.getStatusCode());
    	} finally {
    		rc.logout();
    	}
    }

}
