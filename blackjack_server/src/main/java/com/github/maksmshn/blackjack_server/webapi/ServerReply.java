package com.github.maksmshn.blackjack_server.webapi;

import javax.xml.bind.annotation.XmlRootElement;

import com.github.maksmshn.blackjack_server.game.*;

/* A generic server reply.
 * Will only send playerId once, during player creation.
 * The table field might be null as well.
 * The balance and name are always present.
 */
@XmlRootElement
public class ServerReply {
	public String playerId;
	public String name;
	public double balance;
	public Table table;

	public ServerReply(){
	}

	public ServerReply(Player player){
		this.name = player.getName();
		this.balance = player.getBalance();
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public double getBalance() {
		return balance;
	}

	public Table getTable() {
		return table;
	}

	public void addTable(Table table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return "ServerReply [playerId=" + playerId + ", name=" + name
				+ ", balance=" + balance + ", table=" + table + "]";
	}
}
