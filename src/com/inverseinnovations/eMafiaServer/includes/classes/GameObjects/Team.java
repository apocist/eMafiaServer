/* eMafiaServer - Team.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes.GameObjects;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.inverseinnovations.eMafiaServer.includes.Constants;
import com.inverseinnovations.eMafiaServer.includes.classes.ERS.TeamERS;


public class Team extends GameObject{
	private Match match;
	private TeamERS teamERS = null;;
	private boolean victory;
	private boolean mayGameEnd;
	private Map<String, String> ersScript = new LinkedHashMap<String, String>();
	private ArrayList<Integer> teammates = new ArrayList<Integer>();
	private Map<String, Integer> customVarInt = new LinkedHashMap<String, Integer>();
	private Map<String, String> customVarString = new LinkedHashMap<String, String>();
	private Map<String, Boolean> customVarBoolean = new LinkedHashMap<String, Boolean>();
	private Map<String, LinkedHashMap<Integer, Integer>> poll = new LinkedHashMap<String, LinkedHashMap<Integer, Integer>>();

	public Team(final Match match,String name){
		super(0, name, Constants.TYPE_GAMEOB_TEAM);
		if(this.match == null){this.match = match;}
	}

	public Match getMatch(){
		return this.match;
	}
	public String getScript(String eventCall){
		String theReturn = null;
		if(StringUtils.isNotEmpty(eventCall)){
			if(this.ersScript.containsKey(eventCall)){
				theReturn = this.ersScript.get(eventCall);
			}
		}
		return theReturn;
	}
	public void setScript(String eventCall, String script){
		if(StringUtils.isNotEmpty(eventCall)){
			this.ersScript.put(eventCall, script);
		}
	}
	public boolean getMayGameEnd(){
		return mayGameEnd;
	}
	public void setMayGameEnd(boolean end){
		mayGameEnd = end;
	}
	public boolean getVictory(){
		return victory;
	}
	public void setVictory(boolean vict){
		victory = vict;
	}
	public ArrayList<Integer> getTeammates(){
		return teammates;
	}
	public ArrayList<Integer> getAliveTeammates(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int num :getTeammates()){
			if(match.getPlayerRole(num).isAlive()){
				list.add(num);
			}
		}
		return list;
	}
	public void addTeammate(int playerNum){
		if(!teammates.contains(playerNum)){
			teammates.add(playerNum);
		}
	}
	public void removeTeammate(int playerNum){
		if(teammates.contains(playerNum)){
			teammates.remove(playerNum);
		}
	}
	public boolean isRoleExist(int roleId){
		boolean theReturn = false;
		Integer[] players = ((Integer[]) getTeammates().toArray());
		for(int i = 0; i < players.length; i++){
			if(match.getPlayerRole(players[i]) != null){
				if(match.getPlayerRole(players[i]).getEID() == roleId){
					theReturn = true;
					break;
				}
			}
		}
		return theReturn;
	}
	public int numRoleAlive(int roleId){
		int theReturn = 0;
		Integer[] players = ((Integer[]) getTeammates().toArray());
		for(int i = 0; i < players.length; i++){
			Role role = match.getPlayerRole(players[i]);
			if(role != null){
				if(role.getEID() == roleId){
					if(role.isAlive()){
						theReturn++;
					}
				}
			}
		}
		return theReturn;
	}
	public boolean isRoleAlive(int roleId){
		boolean theReturn = false;
		Integer[] players = ((Integer[]) getTeammates().toArray());
		for(int i = 0; i < players.length; i++){
			Role role = match.getPlayerRole(players[i]);
			if(role != null){
				if(role.getEID() == roleId){
					if(role.isAlive()){
						theReturn = true;
						break;
					}
				}
			}
		}
		return theReturn;
	}
	public void setCustomVarBoolean(String var,boolean value){
		customVarBoolean.put(var, value);
	}
	public void setCustomVarInt(String var,int value){
		customVarInt.put(var, value);
	}
	public void setCustomVarString(String var,String value){
		customVarString.put(var, value);
	}
	public boolean getCustomVarBoolean(String var){
		boolean theReturn = false;
		if(customVarBoolean.containsKey(var)){
			theReturn = customVarBoolean.get(var);
		}
		return theReturn;
	}
 	public int getCustomVarInt(String var){
		int theReturn = 0;
		if(customVarInt.containsKey(var)){
			theReturn = customVarInt.get(var);
		}
		return theReturn;
	}
	public String getCustomVarString(String var){
		String theReturn = null;
		if(customVarString.containsKey(var)){
			theReturn = customVarString.get(var);
		}
		return theReturn;
	}
	public void pollClear(String pollName){
		if(poll.containsKey(pollName)){
			poll.get(pollName).clear();
		}
	}
	public void pollVoteAdd(String pollName, int option){
		if(!poll.containsKey(pollName)){
			poll.put(pollName, new LinkedHashMap<Integer, Integer>());
		}
		LinkedHashMap<Integer, Integer> thePoll = poll.get(pollName);
		if(thePoll.containsKey(option)){
			thePoll.put(option,thePoll.get(option) + 1);
		}
		else{
			thePoll.put(option,1);
		}
	}
	public void pollVoteRemove(String pollName, int option){
		if(!poll.containsKey(pollName)){
			poll.put(pollName, new LinkedHashMap<Integer, Integer>());
		}
		LinkedHashMap<Integer, Integer> thePoll = poll.get(pollName);
		if(thePoll.containsKey(option)){
			if(thePoll.get(option) >= 1){
				thePoll.put(option,thePoll.get(option) - 1);
			}
		}
		else{
			thePoll.put(option,0);
		}
	}
	public int pollHighestVote(String pollName){
		int highestOp = 0;
		int value = 0;
		if(poll.containsKey(pollName)){
			LinkedHashMap<Integer, Integer> thePoll = poll.get(pollName);
			for(int key : thePoll.keySet()){
				if(thePoll.get(key) > value){
					value = thePoll.get(key);
					highestOp = key;
				}
			}
		}
		return highestOp;
	}

 	public TeamERS getERSClass(){
		if(this.teamERS == null){
			this.teamERS = new TeamERS(this);
		}
		return this.teamERS;
	}
}
