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
	 * There should only be one(1) for now
	 * @param back reference to Game()
	 * @param id  eID of the Lobby..defaults to 1 but may expand towards more lobbys
	 * @param name name of the Lobby
	 */
	public Lobby(Game game, int id, String name) {
		super(id, name, Constants.TYPE_GAMEOB_LOBBY);
		this.Game = game;
		Game.addLobby(this);
	}
	/**
	 * Adds Character to the Lobby and notifies all clients in the Lobby except the Chara itself
	 */
	public void addCharacter(Character chara){
		this.characters.put(chara.getEID(), chara);
		this.send(CmdCompile.charaUpdate(chara));
		this.send(CmdCompile.playerEnter(chara),chara);
	}
	/**
	 * Removes Character from the Lobby and notifies all clients in the Lobby
	 */
	public void removeCharacter(Character chara){
		this.characters.remove(chara.getEID());
		this.send(CmdCompile.playerLeave(chara),chara);
	}
	/**
	 * Returns a Map of all Characters i the Lobby
	 */
	public Map<Integer, Character> getCharacters(){
		return this.characters;
	}
	/**
	 * Returns the number of Characters in the Lobby
	 */
	public int getNumChars(){
		return this.characters.size();
	}
	/**
	 * Returns a List of all Characters in the Lobby
	 */
	public List<Character> getPlayerList(){
		List<Character> list = new ArrayList<Character>();
		for(Character player : this.characters.values()){
			list.add(player);
		}
		return list;
	}
	/**
	 * Sends a CmdHandler.Command to all Characters in the Lobby
	 */
	public void send(byte[] message){
		this.send(message,(String)null);
	}
	/**
	 * Sends a CmdHandler.Command to all Characters in the Lobby EXCEPT this Character
	 * @param except This character is excluded
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
	/**Sends a Character 'said' lobby chat to all Characters
	 * @param message the chat spoken
	 * @param mType
	 * @param from Outputted from which Character
	 */
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
