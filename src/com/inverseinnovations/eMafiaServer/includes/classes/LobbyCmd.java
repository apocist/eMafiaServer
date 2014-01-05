/* eMafiaServer - LobbyCmd.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.inverseinnovations.eMafiaServer.includes.CmdCompile;
import com.inverseinnovations.eMafiaServer.includes.StringFunctions;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Character;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Lobby;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Match;
import com.inverseinnovations.eMafiaServer.includes.classes.Server.SocketClient;

/**
 * Provides list of all commands a Character may call when inside a Lobby<br>
 * All method names must be appended to CMDLIST[] to be callable
 */
public class LobbyCmd {
	public static String[] CMDLIST = {
		//basic commands
		//"help","look","match",
		"charaupdate","match","refresh","refreshplist","say","quit",
		//admin commands
		//"_show_commands","_shutdown","timer_add","_setupbots","_makenpc","_force"
		//experimental commands
		"test","var_dump"
	};
	public static void charaupdate(Character c, String phrase) {
		String[] ephrase = phrase.split(" ");
		//int[] intPhrase = new int[ephrase.length];
		//int loop = 0;
		for(String charaString:ephrase){
			if(StringFunctions.isInteger(charaString)){
				Character chara = c.Game.getCharacter(Integer.parseInt(charaString));
				if(chara != null){
					c.send(CmdCompile.charaUpdate(chara));
				}
			}
		}
	}
	public static void match(Character c, String phrase) {
		String[] ephrase = phrase.split(" ");
		Map<Integer, Match> matchs = c.Game.getMatchs();
		Match match;
		if(ephrase.length > 0){
			if(ephrase[0].equals("list")){
				c.send(CmdCompile.refreshMList(c.Game.getMatchs()));
			}
			else if(ephrase[0].equals("join") && ephrase.length > 1){
				match = c.Game.getMatch(Integer.parseInt(ephrase[1]));
				if(match != null){
					if(c.joinMatch(match)){//attempt to join + check character joined
						c.send(CmdCompile.closeLayer("lobby"));//remove lobby layer
						c.send(CmdCompile.enterMatch());//open matchSetup,client will call look
					}
					else{
						c.send(CmdCompile.chatScreen("Match "+ephrase[1]+" is full."));
					}
				}
				else{
					c.send(CmdCompile.chatScreen("That match doesn't even exist!"));
				}
			}
			else if(ephrase[0].equals("create")){
				match = new Match(c.Game, "New Match");
				LobbyCmd.match(c, "join "+match.getEID());
			}
		}
	}
	public static void refresh(Character c, String phrase) {
		refreshplist(c, phrase);
		match(c, "list");
		return;
	}
	public static void refreshplist(Character c, String phrase) {//get player list
		List<Character> charas = c.getLobby().getPlayerList();
		for(Character chara:charas){
			c.send(CmdCompile.charaUpdate(chara));
		}
		c.send(CmdCompile.refreshPList(charas));
		return;
	}
	public static void say(Character c, String phrase){//need to remove..copyrighted
		//later will add a wholoe chat channel function that this
		c.getLobby().send(phrase,"roomSay",c);
		return;
	}
	public static void quit(Character c, String phrase) {//dissconnecting command
		c.setOffline();
		return;
	}
	public static void var_dump(Character c, String phrase){
		c.Game.Base.Console.warning("");
		c.Game.Base.Console.warning("    ==== Variable Dump ====");
		c.Game.Base.Console.warning("  --- Client Connections ---");
		for (Entry<Integer, SocketClient> entry : c.Game.Base.Server.getClients().entrySet()){ //c.Game.Base.Server.getClients().entrySet()){
			c.Game.Base.Console.warning("Client: "+entry.getValue().getClientEID()+" | Char: "+entry.getValue().getCharEID()+" | IP: "+entry.getValue().getIPAddress());
		}
		c.Game.Base.Console.warning("  --- Characters ---");
		String location = "";
		for (Entry<Integer, Character> entry : c.Game.getCharacters().entrySet()){ //c.Game.Base.Server.getClients().entrySet()){
			if(entry.getValue().getInGame()){location = "Match ";}else{location = "Lobby ";}
			c.Game.Base.Console.warning("EID: "+entry.getValue().getEID()+" | Name: "+entry.getValue().getName()+" | Location: "+location+entry.getValue().getLocation());// + "/" + entry.getValue();
		}
		c.Game.Base.Console.warning("  --- Lobbys ---");
		for (Entry<Integer, Lobby> entry : c.Game.getLobbys().entrySet()){ //c.Game.Base.Server.getClients().entrySet()){
			c.Game.Base.Console.warning("EID: "+entry.getValue().getEID()+" | Name: "+entry.getValue().getName()+" | Players: "+entry.getValue().getNumChars());// + "/" + entry.getValue();
		}
		c.Game.Base.Console.warning("  --- Matchs ---");
		for (Entry<Integer, Match> entry : c.Game.getMatchs().entrySet()){ //c.Game.Base.Server.getClients().entrySet()){
			c.Game.Base.Console.warning("EID: "+entry.getValue().getEID()+" | Name: "+entry.getValue().getName()+" | Players: "+entry.getValue().getNumChars());// + "/" + entry.getValue();
		}
	}

}
