/* eMafiaServer - Lobby.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes.GameObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inverseinnovations.eMafiaServer.includes.*;
import com.inverseinnovations.eMafiaServer.includes.classes.*;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Character;

/**Lobby object a beginning holder of characters to allow movement to Matches.*/
public class Lobby extends GameObject{
	public Game Game;
	private Map<Integer, Character> characters = Collections.synchronizedMap(new LinkedHashMap<Integer, Character>());

	/**Creates Lobby object
	 * There should only be one(1)
	 * @param id = the eID of Lobby..defaults to 1 but my expand
	 */
	public Lobby(Game game, int id, String name) {
		super(id, name, Constants.TYPE_GAMEOB_LOBBY);
		this.Game = game;
		Game.addLobby(this);
	}
	public void addCharacter(Character chara){//putting whole character reference in array instead of just name now
		this.characters.put(chara.getEID(), chara);
			this.send(CmdCompile.charaUpdate(chara));
		this.send(CmdCompile.playerEnter(chara),chara);
	}
	public void removeCharacter(Character chara){
		this.characters.remove(chara.getEID());
		this.send(CmdCompile.playerLeave(chara),chara);
	}
	public Map<Integer, Character> getCharacters(){
		return this.characters;
	}
	public int getNumChars(){
		return this.characters.size();
	}
	public List<Character> getPlayerList(){
		List<Character> list = new ArrayList<Character>();
		for(Character player : this.characters.values()){
			list.add(player);
		}
		return list;
	}
	public void send(byte[] message){
		this.send(message,(String)null);
	}
	/**
	 * Send to all except this chara
	 */
	public void send(byte[] message, Character except){
		int exception = except.getEID();
		for(Character chara : this.characters.values()){
			try{
				if(chara.getEID()!=exception){chara.send(message);}//, mType);
			}
			catch (Exception e){
				Game.Base.Console.warning("Failed to send to char "+chara.getEID()+": "+e.getMessage());
			}

		}
	}
	public void send(byte[] message, String mType){
		for(Character chara : this.characters.values()){
			try{
				chara.send(message);//, mType);
			}
			catch (Exception e){
				Game.Base.Console.warning("Failed to send to char "+chara.getEID()+": "+e.getMessage());
			}

		}
	}
	public void send(String message, String mType, Character from){//assuming is mType is roomsay for now
		for(Character chara : this.characters.values()){
			try{
				chara.send(CmdCompile.chatScreen2(from, message));//, mType);
			}
			catch (Exception e){
				Game.Base.Console.warning("Failed to send to char "+chara.getEID()+": "+e.getMessage());
			}

		}
	}
}
