package com.github.maksmshn.blackjack_server.database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.maksmshn.blackjack_server.game.Player;

public class DatabaseTest {

	@Test
	public void testGetPlayers() {
		Map<String, Player> existingPlayers = Database.getPlayers();
		int numOfOldPlayers = existingPlayers.size();
		for (int i = 0; i < 100; i++){
			Player p = new Player(Integer.toString(i), "testPlayer" + i);
			existingPlayers.put(Integer.toString(i), p);
		}
		int numOfNewPlayers = existingPlayers.size();
		assertEquals(numOfNewPlayers, numOfOldPlayers + 100);
	}

	@Test
	public void testNewPlayerId() {
		String id;
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < 100; i++){
			id = Database.newPlayerId();
			assertFalse(ids.contains(id));
			ids.add(id);
		}
	}
}
