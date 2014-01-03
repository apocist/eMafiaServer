/* eMafiaServer - Character.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes.GameObjects;

import java.util.Map;
import java.util.Vector;

import com.inverseinnovations.eMafiaServer.includes.*;
import com.inverseinnovations.eMafiaServer.includes.classes.Game;
import com.inverseinnovations.eMafiaServer.includes.classes.Server.SocketClient;


public class Character extends GameObject{
	public Game Game;
	private Integer connection;
	private Integer accountId;
	private int usergroup;
	private String avatar;
	private int location;// Lobby/Match EID
	private boolean ingame;//true/false
	private Vector<String> keywords = new Vector<String>();
	private int playernum; //playernum ingame

	public Character(Game game, String name, int usergroup) {
		super(0, name, Constants.TYPE_GAMEOB_CHAR);
		this.Game = game;
		Map<Integer, Character> gameChars = Game.getCharacters();
		for (int i = Game.char_counter; ; i++){
			if (!gameChars.containsKey(i)){
				this.setEID(i);
				Game.char_counter++;
				break;
			}
		}
		this.location = 1;//start off in lobby 1
		this.ingame = false;//not in a game by default...false = lobby
		this.usergroup = usergroup;
		Game.addCharacter(this);

	}
	public void setAccountID(Integer id){
		this.accountId = id;
	}
	public Integer getAccountId(){
		return this.accountId;
	}
	public void setUsergroup(int usergroup){
		this.usergroup = usergroup;
	}
	public Usergroup getUsergroup(){
		return Game.getUsergroup(usergroup);
	}
	public void setAvatar(String path){
		this.avatar = path;
	}
	public String getAvatar(){
		return avatar;
	}
	private void setInGame(boolean ingame){//should never be called manuelly
		this.ingame = ingame;
	}
	public boolean getInGame(){
		return this.ingame;
	}
	public void setPlayerNum(int playernum){
		this.playernum = playernum;
	}
	public int getPlayerNum(){
		return this.playernum;
	}
	public void setLocation(Lobby lobby){
		this.location = lobby.getEID();
		this.setInGame(false);
	}
	public void setLocation(Match match){
		this.location = match.getEID();
		this.setInGame(true);
	}
	public int getLocation(){
		return this.location;
	}
	public Lobby getLobby(){
		if(getInGame()){return null;}
		else{return Game.getLobby(getLocation());}
	}
	public Match getMatch(){
		if(getInGame()){return Game.getMatch(getLocation());}
		else{return null;}
	}
	public boolean joinMatch(Match match){//Join the match (the object) not eid
		if(match.getNumChars() < match.getSetting("max_chars")){//if theres room in the match
			this.getLobby().removeCharacter(this);
			this.setLocation(match);//set to new room
			match.addCharacter(this);
			this.send(CmdCompile.chatScreen("<hr>"));
			return true;
		}
		else{return false;}
	}
	public void leave(){
		if(getInGame()){
			this.getMatch().removeCharacter(this);
			this.getMatch().gameCancel();
			if(this.getType() != Constants.TYPE_GAMEOB_NPC){
				if(getEID() == getMatch().getHostId()){
					//Game.Base.Console.debug("Chara WAS the host!");
					if(!getMatch().findNewHost()){//end match if no new host found
						//Game.Base.Console.debug("Couldn't find new host");
						getMatch().endMatch();
					}
				}
				send(CmdCompile.closeLayer("matchSetup"));//remove lobby chat and list
				send(CmdCompile.enterLobby());//open Lobby,client will call look
			}
		}
	}
	public void leaveMatch(){//leave the match, join lobby FIXME:test npcs
		if(getInGame()){
			this.leave();
			if(this.getType() != Constants.TYPE_GAMEOB_NPC){
				this.setLocation(Game.getLobby(1));//join lobby 1
				this.getLobby().addCharacter(this);
				this.setInGame(false);
				this.send(CmdCompile.chatScreen("<hr>"));
			}
		}
	}
	public void setConnection(Integer id){
		this.connection = id;
	}
	public SocketClient getConnection(){
		if(connection != null) return Game.getConnection(connection);
		else return null;
	}
	public void setOffline(){//XXX need to perform chara deletion if no connection
		SocketClient cConn = this.getConnection();
		if(cConn != null) cConn.offline();
	}
	public void send(byte[] message){//this is basiclly sendDirect atm
		SocketClient cConn = this.getConnection();
		if(cConn != null){
			cConn.send(message);
		}
	}
	public void sendDirect(byte[] message){
		SocketClient cConn = this.getConnection();
		if(cConn != null){
			cConn.send(message);
		}
	}
}
