/* eMafiaServer - Game.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes;


import java.util.HashMap;
import java.util.Map;

import com.inverseinnovations.eMafiaServer.*;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.*;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Character;
import com.inverseinnovations.eMafiaServer.includes.classes.Server.*;
/**Manages characters,matches,and the lobby*/
public class Game {
	public Base Base;//back reference to parent

	public boolean GAME_IS_RUNNING = true;
	public boolean GAME_PAUSED = true;
	private long start_time; // Game instance start time //
	/** Connects (Game)Character EID to Character class*/
	private Map<Integer, Character> characters = new HashMap<Integer, Character>();
	public int char_counter = 1;//stores last character eid
	private Map<Integer, Lobby> lobbys = new HashMap<Integer, Lobby>();
	private Map<Integer, Match> matches = new HashMap<Integer, Match>();
	private HashMap<Integer, Usergroup> usergroups = new HashMap<Integer, Usergroup>();
	private String roleCategories;

	/**
	 * Prepares main Game handler
	 */
	public Game(Base base){
		this.Base = base;
		this.start_time = System.nanoTime();
	}
	/**
	 * Assigns a Usergroup to Game(),
	 * @param userg Usergroup
	 */
	public void addUsergroup(Usergroup userg){
		this.usergroups.put(userg.getEID(), userg);
	}
	public Usergroup getUsergroup(int id){
		if (this.usergroups.containsKey(id)){return this.usergroups.get(id);}
		else{return null;}
	}
	/**
	 * Assigns a Lobby to Game()
	 * @param l Lobby
	 */
	public void addLobby(Lobby l){
		this.lobbys.put(l.getEID(), l);
		Base.Console.debug("\""+l.getName()+"\" lobby created");
	}
	public void removeLobby(Lobby l){
		this.lobbys.remove(l.getEID());
	}
	public Lobby getLobby(int id){
		if (this.lobbys.containsKey(id)){return this.lobbys.get(id);}
		else{return null;}
	}
	public Map<Integer, Lobby> getLobbys(){
		return this.lobbys;
	}
	public void addMatch(Match m){
		this.matches.put(m.getEID(), m);
		Base.Console.fine("\""+m.getName()+"\" match created");
		//TODO add match to clients
	}
	public void removeMatch(Match m){
		this.matches.remove(m.getEID());
		//TODO remove match from clients
	}
	public Match getMatch(int id){
		if (this.matches.containsKey(id)){return this.matches.get(id);}
		else{return null;}
	}
	public Map<Integer, Match> getMatchs(){
		return this.matches;
	}
	public void addCharacter(Character c){
		Base.Console.debug(c.getName()+" added to game");
		this.characters.put(c.getEID(), c);
	}
	public void removeCharacter(Character c){
		this.characters.remove(c.getEID());
	}
	public Character getCharacter(int id){
		if (this.characters.containsKey(id)){return this.characters.get(id);}
		else{return null;}
	}
	public Map<Integer, Character> getCharacters(){
		return this.characters;
	}
	/** Returns SocketClient bound that the given client id */
	public SocketClient getConnection(int id){
			return Base.Server.getClient(id);
	}
	/**
	 * Sets if the game is online or offline. Setting this to false will make the application close
	 * @param state false = offline/true = online
	 */
	public void setGameRunning(boolean state){
		if(state)this.GAME_IS_RUNNING = true;
		else this.GAME_IS_RUNNING = false;
	}
	/**
	 * Is the game even on? Value change from setGameRunning
	 * @return true/false
	 */
	public boolean isRunning(){
		return this.GAME_IS_RUNNING;
	}
	/**
	 * Set is game is accepting play input
	 * @param state false = not paused/true = paused
	 */
	public void setGamePaused(boolean state){
		if(state)this.GAME_PAUSED = true;
		this.GAME_PAUSED = false;
	}
	/**
	 * Is the game accept player input?
	 * @return true/false
	 */
	public boolean isPaused(){
		return this.GAME_PAUSED;
	}
}
