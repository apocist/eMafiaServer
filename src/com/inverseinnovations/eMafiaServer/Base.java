/* eMafiaServer - Base.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer;

import com.inverseinnovations.eMafiaServer.includes.*;
import com.inverseinnovations.eMafiaServer.includes.classes.*;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.*;
import com.inverseinnovations.eMafiaServer.includes.classes.Server.*;

public class Base {
	public int program_faults = 0;
	public Console Console = new Console(this);
	public Settings Settings = new Settings(this);
	public SocketServer Server = new SocketServer(this, Settings.SERVER_HOST, Settings.SERVER_PORT, Settings.SERVER_MAX_CLIENTS);
	public MySqlDatabaseHandler MySql = new MySqlDatabaseHandler(this, Settings.MYSQL_URL, Settings.MYSQL_USER, Settings.MYSQL_PASS);
	public SC2MafiaAPI ForumAPI = new SC2MafiaAPI(this, Settings.APIURL, Settings.APIKEY, "eMafiaServer Debugging atm - Hi Nick", Constants.VERSION);
	public Game Game = new Game(this);

	public Base(){
		if(program_faults == 0){
			Server.create();
			//Load usergroups from the database
			MySql.loadUsergroups(Game);

			// Setup a lobby for players to join //
			new Lobby(Game, 1, "The Game Lobby");//This shall remain
			//put 2 matchs up for debugging..remove later(will remain until release to public)
			new Match(Game, "A First Match");
			new Match(Game, "Another Match");
			Server.listen();//should only be called once
			Game.setGamePaused(false);//game is now unpaused and ready to play

			Console.config("eMafia Server "+Constants.VERSION+" is now up and running!");
		}
		else{
			Console.severe("eMafia Server is experiencing errors and cannot continue.");
		}
		//This is just to keep the server running
		while (Game.isRunning()){
			try {
				Thread.sleep(2000);//2 secs, so the server checks if its to end every 2 seconds
			} catch (InterruptedException e) {
				Console.severe("Base Thread sleep error");
				Console.printStackTrace(e);
			}
		}
		Console.warning("The eMafia Server is no longer running.");
		//TODO save the log here at program close
		System.exit(0);
	}

	public static void main(String[] args) {
		Base eMafiaServer = new Base();
	}
}
