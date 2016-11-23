package com.github.maksmshn.blackjack_server.database;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.maksmshn.blackjack_server.game.Player;

/**
 * A basic database for all players.
 */
public class Database {

	private static Map<String, Player> players = new HashMap<>();

	public static Map<String, Player> getPlayers() {
		return players;
	}

	public static String newPlayerId() {
		return UUID.randomUUID().toString();
	}

}
