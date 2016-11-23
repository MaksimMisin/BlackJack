package com.github.maksmshn.blackjack_server.webapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.maksmshn.blackjack_server.Properties;
import com.github.maksmshn.blackjack_server.database.Database;
import com.github.maksmshn.blackjack_server.game.Player;

class PlayerService {

	private Map<String, Player> players = Database.getPlayers();
	private boolean securityChecks = Properties.getBoolean("securityChecks");
	private double minimumBet = Properties.getDouble("minimumBet");

	enum Action {
		BET, HIT, STAND
	}

	public PlayerService() {
		// Uncomment to create two mock players.
		// String id1 = Database.newPlayerId();
		// String id2 = Database.newPlayerId();
		// Player john = new Player(id1, "john");
		// Player sam = new Player(id2, "sam");
		// players.put("john", john);
		// players.put("sam", sam);
	}

	private void throwWebApplicationException(Status status, String msg) {
		ErrorMessage erm = new ErrorMessage(status, msg);
		throw new WebApplicationException(erm.toResponse());
	}

	private void throwWebApplicationException(Exception ex) {
		ErrorMessage erm = new ErrorMessage(ex);
		throw new WebApplicationException(erm.toResponse());
	}

	private void checkIfPlayerExists(Player player) {
		if (player == null) {
			throwWebApplicationException(Response.Status.NOT_FOUND,
					"Requested player was not found.");
		}
	}

	private boolean isRequestValid(Player player, ClientRequest request) {
		if (request == null) {
			throwWebApplicationException(Response.Status.BAD_REQUEST,
					"Empty/invalid json request body.");
		}
		if (securityChecks) {
			String playerId = request.getPlayerId();
			if (playerId == null || !player.getPlayerId().equals(playerId)) {
				throwWebApplicationException(Response.Status.UNAUTHORIZED,
						"Provided playerId does not match player name in URI.");
			}
		}
		return true;
	}

	List<ServerReply> getAllPlayers() {
		List<ServerReply> replys = new ArrayList<>();
		for (Player p : players.values()) {
			replys.add(new ServerReply(p));
		}
		return replys;
	}

	ServerReply getPlayer(String name) {
		Player player = players.get(name);
		checkIfPlayerExists(player);
		ServerReply reply = new ServerReply(player);
		reply.addTable(player.getTable());
		return reply;
	}

	/** The only time player receives an ID. */
	ServerReply addPlayer(ClientRequest request) {
		if (request == null) {
			throwWebApplicationException(Response.Status.BAD_REQUEST,
					"Empty/invalid json request body.");
		}
		String name = request.getName();
		if (name == null) {
			throwWebApplicationException(Response.Status.BAD_REQUEST,
					"The name of the new player must be specified.");
		}
		if (players.containsKey(name)) {
			throwWebApplicationException(Response.Status.BAD_REQUEST,
					"The player with such name already exists.");
		}
		String playerId = Database.newPlayerId();
		Player player = new Player(playerId, name);
		players.put(player.getName(), player);
		ServerReply reply = new ServerReply(player);
		reply.setPlayerId(playerId);
		// System.out.println("player " + player + " created.");
		return reply;
	}

	/* Since DELETE cannot have a body we check the validity
	 * of the request by inspecting a header.
	 */
	ServerReply logOut(String name, String playerId) {
		Player player = players.get(name);
		if (securityChecks) {
			if (playerId == null || !player.getPlayerId().equals(playerId)) {
				throwWebApplicationException(Response.Status.UNAUTHORIZED,
						"Provided playerId does not match player name in URI.");
			}
		}
		return new ServerReply(players.remove(name));
	}

	ServerReply topUp(String name, ClientRequest request) {
		Player playingPlayer = players.get(name);
		checkIfPlayerExists(playingPlayer);
		if (isRequestValid(playingPlayer, request)) {
			try {
				playingPlayer.topUp(request.getTopUp());
			} catch (IllegalArgumentException ex) {
				throwWebApplicationException(ex);
			}
		}
		return new ServerReply(playingPlayer);
	}

	ServerReply playerAction(String name, ClientRequest request, Action action) {
		Player playingPlayer = players.get(name);
		checkIfPlayerExists(playingPlayer);
		isRequestValid(playingPlayer, request);
		try {
			switch (action) {
			case BET:
				double bet = request.getBet();
				if (bet >= minimumBet) {
					playingPlayer.bet(bet);
				} else {
					throwWebApplicationException(Status.BAD_REQUEST,
							"The bet is smaller than mimimum: " + minimumBet
									+ ".");
				}
				break;
			case HIT:
				playingPlayer.hit();
				break;
			case STAND:
				playingPlayer.stand();
				break;
			}
		} catch (IllegalStateException | IllegalArgumentException ex) {
			throwWebApplicationException(ex);
		}
		ServerReply reply = new ServerReply(playingPlayer);
		reply.addTable(playingPlayer.getTable());
		return reply;
	}
}
