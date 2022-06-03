package com.inverseinnovations.eMafiaServer.includes.classes.GameObjects;

//import eMafiaServer.*;
import com.inverseinnovations.eMafiaServer.includes.*;
import com.inverseinnovations.eMafiaServer.includes.classes.*;

public class Usergroup extends GameObject{
	public Game Game;
	private String hexcolor;

	/**
	 * Data holder for permissions, maintained by Game()
	 * @param eid Usergroup Id
	 * @param name Usergroup Name
	 * @param hexcolor Font Color
	 */
	public Usergroup(final Game game, int eid, String name, String hexcolor) {
		super(eid, name, Constants.TYPE_GAMEOB_USERGROUP);
		this.Game = game;
		this.hexcolor = hexcolor;
		Game.addUsergroup(this);
	}

	/**
	 * Returns the font color in hex
	 * @return String hex color
	 */
	public String getHexcolor(){
		return this.hexcolor;
	}
}
