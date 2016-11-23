package com.github.maksmshn.blackjack_server.webapi;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/players")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

	PlayerService playerService = new PlayerService();

	@GET
	public List<ServerReply> getPlayers() {
		return playerService.getAllPlayers();
	}

	@POST
	public Response addPlayer(ClientRequest requst, @Context UriInfo uriInfo) {
		ServerReply reply = playerService.addPlayer(requst);
		String name = reply.getName();
		URI pathToPlayer = uriInfo.getAbsolutePathBuilder().path(name).build();
		return Response.created(pathToPlayer).entity(reply).build();
	}

	@GET
	@Path("/{playerName}")
	public ServerReply getPlayer(@PathParam("playerName") String playerName) {
		return playerService.getPlayer(playerName);
	}

	/* If security checks are on, delete request should contain
	 * playerId parameter in header.
	 */
	@DELETE
	@Path("/{playerName}")
	public ServerReply deletePlayer(@PathParam("playerName") String playerName,
			@HeaderParam("playerId") String playerId) {
		return playerService.logOut(playerName, playerId);
	}

	@POST
	@Path("/{playerName}/topUp")
	public ServerReply addMoney(@PathParam("playerName") String playerName,
			ClientRequest request) {
		return playerService.topUp(playerName, request);
	}

	@POST
	@Path("/{playerName}/bet")
	public ServerReply bet(@PathParam("playerName") String playerName,
			ClientRequest request) {
		return playerService.playerAction(playerName, request,
				PlayerService.Action.BET);
	}

	@POST
	@Path("/{playerName}/hit")
	public ServerReply hit(@PathParam("playerName") String playerName,
			ClientRequest request) {
		return playerService.playerAction(playerName, request,
				PlayerService.Action.HIT);
	}

	@POST
	@Path("/{playerName}/stand")
	public ServerReply stand(@PathParam("playerName") String playerName,
			ClientRequest request) {
		return playerService.playerAction(playerName, request,
				PlayerService.Action.STAND);
	}

}
