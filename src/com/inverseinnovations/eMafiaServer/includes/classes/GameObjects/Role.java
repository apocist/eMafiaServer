/* eMafiaServer - Role.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes.GameObjects;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import com.inverseinnovations.eMafiaServer.includes.scriptProcess;
import com.inverseinnovations.eMafiaServer.includes.classes.ERS.RoleERS;


public class Role extends GameObject{
	public Match Match;
	//private id;
	//Static Variables from Database
	private int version;
	private String affiliation;
	private String[] category;// = array();
	//private int actionOrder = 100;//last by default
	private String actionCat;
	//private int numPossibleNightTargets;//targets 0-2 for nightaction(how many buttons should appear if any)
	//private int numPossibleDayTargets;//targets 0-2 for dayaction(how many buttons should appear if any)
	/** Who you are able to target with button 1
	 0 = No one
	 1 = Everyone
	 2 = Everyone except self
	 3 = Only self
	 */
	public int targetablesNight1;
	public int targetablesNight2;
	public int targetablesDay1;
	public int targetablesDay2;
	//Dynamic Variables
	private int playernum;
	private RoleERS roleERS = null;
	private boolean isAlive = true;
	private boolean mayGameEnd = false;
	private boolean victoryMet = false;
	private int hp = 1;
	private Map<String, Flag> flags = new LinkedHashMap<String, Flag>();
	public List<String> deathDesc = new ArrayList<String>();//each death desc is added
	public List<String> deathTypes = new ArrayList<String>();//records every reason for death. Lynched, Shot, Burned, ???
	private List<Integer> visitedBy = new ArrayList<Integer>();//each player the role was visited by that last night
	private int target1 = 0;//day/night action targets
	private int target2 = 0;//day/night action targets
	private boolean onTeam = false;
	private String teamName;
	private boolean teamWin = false;
	private Map<String, Integer> customVarInt = new LinkedHashMap<String, Integer>();
	private Map<String, String> customVarString = new LinkedHashMap<String, String>();
	private Map<String, Boolean> customVarBoolean = new LinkedHashMap<String, Boolean>();
	private Map<String, String> ersScript = new LinkedHashMap<String, String>();

 	public Role(final Match match, int eid, String name, int type, String affiliation, String[] category){
		super(eid, name, type);
		this.Match = match;
		this.affiliation = affiliation;
		this.category = category;
	}

	public void setMatch(Match match){
		this.Match = match;
	}
	public Match getMatch(){
		return this.Match;
	}
	public int getVersion(){
		return version;
	}
	public void setVersion(int version){
		this.version = version;
	}
	public String getAffiliation(){
		return this.affiliation;
	}

	public String[] getCategory(){
		return this.category;
	}
	/*public int getActionOrder(){
		return actionOrder;
	}
	public void setActionOrder(int order){
		this.actionOrder = order;
	}*/
	public String getActionCat(){
		return actionCat;
	}
	public void setActionCat(String cat){
		this.actionCat = cat;
	}
 	public void setPlayerNum(int num){
		this.playernum = num;
	}
	public int getPlayerNum(){
		return this.playernum;
	}
	public int getHp(){
		return this.hp;
	}
	public void setHp(int value){
		this.hp = value;
		if(this.hp <= 0){setIsAlive(false);}
	}
	public Map<String, Flag> getFlags(){
		return flags;
	}
 	public void addFlag(Flag flag){
		flags.put(flag.getName(), flag);
	}
	public void addFlag(String name){
		Flag flag = new Flag(name);
		flags.put(flag.getName(), flag);
	}
	public void removeFlag(String name){
		if(flags.containsKey(name)){
			flags.remove(name);
		}
	}
	public Flag getFlag(String name){
		Flag theReturn = null;
		if(flags.containsKey(name)){
			theReturn = flags.get(name);
		}
		return theReturn;
	}
	public boolean hasFlag(String name){
		boolean theReturn = false;
		if(flags.containsKey(name)){
			theReturn = true;
		}
		return theReturn;
	}
	public Map<String, String> getScriptMap(){
		return ersScript;
	}
	/**Returns the requested script for the specified event. Returns null if no existing event or script.
	 * @param eventCall
	 * @return ERS Script
	 */
	public String getScript(String eventCall){
		String theReturn = null;
		if(StringUtils.isNotEmpty(eventCall)){
			if(ersScript.containsKey(eventCall)){
				theReturn = ersScript.get(eventCall);
			}
		}
		return theReturn;
	}
	/**Saves an ERS Script for the specified event
	 * @param eventCall
	 * @param script
	 */
	public void setScript(String eventCall, String script){
		if(StringUtils.isNotEmpty(eventCall)){
			ersScript.put(eventCall, script);
		}
	}
	/**Is role suppose to be on a team?
	 * @return
	 */
	public boolean getOnTeam(){
		return onTeam;
	}
	/**Sets whether role is suppose to be on a team
	 * @param bool
	 */
	public void setOnTeam(boolean bool){
		onTeam = bool;
	}
	public Team getTeam(){
		Team team = null;
		if(getOnTeam()){
			team = Match.getTeam(getTeamName());
		}
		return team;
	}
	/**Get the team name of role(If has one), The name is
	 * set by role setup options or if empty, auto set to Affiliation
	 * @return
	 */
	public String getTeamName(){
		return teamName;
	}
	/**Sets the team name for the role
	 * @param name
	 */
	public void setTeamName(String name){
		teamName = name;
	}
	/**Is win conditions shared by team?*/
	public boolean getTeamWin(){
		return teamWin;
	}
	/**Set if win conditions are shared by team*/
	public void setTeamWin(boolean share){
		teamWin = share;
	}
	/**Is game allowed to end?*/
	public boolean getMayGameEnd(){
		return mayGameEnd;
	}
	/**Set if game is allowed to end using these conditions*/
	public void setMayGameEnd(boolean end){
		mayGameEnd = end;
	}
	/**Has the role won?*/
	public boolean getVictory(){
		return victoryMet;
	}
	/**Set if role won or not*/
	public void setVictory(boolean win){
		victoryMet = win;
	}
	/**Return if role is alive or not*/
	public boolean isAlive(){
		return isAlive;
	}
	/**Set if role is alive or not*/
	public void setIsAlive(boolean bool){
		this.isAlive = bool;
	}
//////////////////////////
/////////Targeting////////
//////////////////////////
	/**Add a playerNum to have being visited this role/player*/
 	public void addVisitedBy(int playerNum){
		if(!visitedBy.contains(playerNum)){ visitedBy.add(playerNum);}
	}
	/**Returns the visitedBy list*/
	public List<Integer> getVisitedBy(int playerNum){
		return visitedBy;
	}
	/**Sets to have being visited by no one. */
	public void clearVisitedBy(){
		visitedBy.clear();
	}
	/**Processes this Role as having been visited by the provided
	 * @param visitor the Role visitor this role
	 */
	public void visited(Role visitor){
		addVisitedBy(visitor.getPlayerNum());
		for(Flag flag : getFlags().values()){//TODO flags test if work
			if(flag.isScriptedPre()){
				new scriptProcess(flag.getScriptPre("onVisit"), this, visitor);
			}
		}
		if(StringUtils.isNotEmpty(getScript("onVisit"))){
			new scriptProcess(getScript("onVisit"), this, visitor);
		}
		for(Flag flag : getFlags().values()){//TODO flags test if work
			if(flag.isScriptedPost()){
				new scriptProcess(flag.getScriptPost("onVisit"), this, visitor);
			}
		}
	}
	public void setTarget1(int target){
		this.target1 = target;
	}
	public int getTarget1(){
		return target1;
	}
	public void setTarget2(int target){
		this.target2 = target;
	}
	public int getTarget2(){
		return target2;
	}
	/**Sets target 1 and 2 to 0 (none)*/
	public void clearTargets(){
		setTarget1(0);setTarget2(0);
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
	public void send(String message){
		getMatch().sendToPlayerNum(getPlayerNum(), message);
	}
	public RoleERS getERSClass(){
		if(this.roleERS == null){
			this.roleERS = new RoleERS(this);
		}
		return this.roleERS;
	}
}
