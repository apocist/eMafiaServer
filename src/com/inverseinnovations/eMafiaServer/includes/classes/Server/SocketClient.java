/* eMafiaServer - SocketClient.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.inverseinnovations.eMafiaServer.includes.CmdCompile;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Character;

public class SocketClient extends Thread{
	//Beginning of reserved variables//
	public SocketServer Server;//reference
	private String ipAddress;
	private int clientEID = 0;
	private Socket socket;
	private boolean RUNNING = true;
	private boolean ONLINE = false;
	private BufferedReader in;
	private DataOutputStream out;
	//Login scripts
	public int loginState = 2;//1 = new. 2 = user/password
	public int loginSubState = 1;//used for Loginstate(username,password,new) / holds the write buffer
	public String usernameBit;//used for $temp_name & $new_name in login / defines what type of item writing buffer is saving to
	public String pass1bit;//used for pass1 in login
	public String pass2bit;//used for pass2 in login
	public int Ebit;//new account state(in login/new account)
	/////Game Data below//////
	private int characterEID;
	private long lastInput = 0;//detrimine idleness

	public SocketClient(SocketServer server, int id, Socket socket){
		this.clientEID = id;
		this.socket = socket;
		this.Server = server;
		this.setName("Client "+id+" Socket "+socket);
	}
	public void run(){
		String line , input = "";

		try{
			ipAddress = socket.getInetAddress().getHostAddress();//get ip
			//get socket writing and reading streams
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			//out = new PrintStream(socket.getOutputStream());

			//Send welcome message to client
			Server.Base.Console.info("Client "+this.clientEID+" connected from "+ipAddress);
			Server.onConnect(this);

			//Now start reading input from client
			/*while((line = in.readLine()) != null && !line.equals(".")){
				//reply with the same message, adding some text
				out.println("I got : " + line);
			}*/
			while(RUNNING){
				if((line = in.readLine()) != null){
					try{
						Server.onRead(this,line);
					}
					catch(Exception e){
						Server.Base.Console.warning("Exception on SocketClient during onRead: client "+clientEID);
						Server.Base.Console.printStackTrace(e);
					}
				}
				else{
					break;
				}
			}
			this.offline();
			this.close();
		}
		catch (IOException e){
			Server.Base.Console.warning("IOException on SocketClient(run) "+clientEID+"(Client-side closing bad?)");//XXX happens on client side... set to fine logging?
			this.offline();//XXX may remove this if the player crashed
			this.close();
			//Server.Base.Console.printStackTrace(e);
		}
	}
	public int getClientEID(){
		return this.clientEID;
	}
	public void setCharEID(int eid){
		this.characterEID = eid;
		this.ONLINE = true;
	}
	public int getCharEID(){
		return this.characterEID;
	}
	public String getIPAddress(){
		return this.ipAddress;
	}
	/** Gets last time command submitted(Used to detrimine idlers) */
	public long getLastTime(){
		return this.lastInput;
	}
	/** Sets current time as last time command submitted(Used to detrimine idlers) */
	public void setLastTime(){
		this.lastInput = System.currentTimeMillis();
	}
	public void input(String input){//test
		//Server.testOut(id, input);
	}
	public void send(byte[] output){
		try {
			if(socket.isConnected()){
				out.write(output);
			}
		}
		catch (IOException e) {
			//e.printStackTrace();
			Server.Base.Console.warning(e.getMessage()+" on SocketClient(send) "+clientEID);
		}
	}
	public void offline(){
		if(ONLINE){
			if(this.getCharEID()>0){//FIXME more work on the closing process
				Character me = Server.Base.Game.getCharacter(getCharEID());
				if(me!=null){
					if(me.getInGame()){
						if(me.getMatch().getPhaseMain() == 0 || me.getMatch().getPhaseMain() == 3){//if in setup or endgame
							me.leave();
						}
						else{
							//XXX if match is actually playing...

						}
					}
					else{
						me.getLobby().removeCharacter(me);
					}
					Server.Base.Console.fine(me.getName()+"...quit..bastard!");
					Server.Base.Game.removeCharacter(me);//XXX should not remove if character is left in the playing match
				}
			}
		}
		ONLINE = false;
		RUNNING = false;
		//this.close();
	}
	public void close(){
		try {
			send(CmdCompile.disconnect());
		}catch(Exception e){}
		try {
			Server.Base.Console.fine("Client "+clientEID+" disconnected.");
			socket.close();
			Server.removeClient(this.clientEID);

		}
		catch (IOException e) {
			Server.Base.Console.warning("IOException on SocketClient attempting to close socket");
			Server.Base.Console.printStackTrace(e);
		}
	}
}
