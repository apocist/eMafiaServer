/* eMafiaServer - SocketServer.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes.Server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.inverseinnovations.eMafiaServer.*;
import com.inverseinnovations.eMafiaServer.includes.*;
import com.inverseinnovations.eMafiaServer.includes.classes.*;
/**Manages connectivity between server and clients*/
public class SocketServer {
	public  Base Base;
	private ServerSocket socket = null;
	private String address;//server up
	private int port;//server port
	private int max_clients;//max allowed connected at once
	private Map<Integer, SocketClient> clients = new HashMap<Integer, SocketClient>();
	private int client_counter = 0;//stores last client eid

	/**
	 * Prepares SocketServer for client connections
	 * @param address server ip address
	 * @param port server port
	 * @param max_clients max allowed connected at onece
	 */
	public SocketServer(Base base, String address, int port, int max_clients){
		this.Base = base;
		this.address = address;
		this.port = port;
		this.max_clients = max_clients;
		//this.clients = new SocketClient[max_clients];
	}
	/**
	 * Initalizes SocketServer and allows clients to connect
	 */
	public void create(){
		// Create a TCP Stream socket
		try{
			this.socket = new ServerSocket();
			this.socket.setReuseAddress(true);
			this.socket.bind(new InetSocketAddress(this.address,this.port), this.max_clients);
			this.socket.isBound();


			long time = System.nanoTime();
			boolean bound = false;

			// Bind the socket to an address/port
			while (time + 1000000000 > System.nanoTime() && !bound){
				Base.Console.config("Attempting to bind to port "+this.port+"...");
				bound = this.socket.isBound();
				if (bound)
					break;
				else
					Thread.sleep(1000);

			}

			if(!bound){
				Base.Console.severe("Could not bind...");
				this.socket.close();
			}
			//need method to accept incoming connections
			// Start listening for connections
			if(bound)Base.Console.config("Socket server started at address "+this.socket.getLocalSocketAddress()+"");

			// Make it non-blocking //
			//socket_set_nonblock($this->socket);
		}
		catch(Exception e){
			Base.Console.severe("Socket Server Creation Error!");
			Base.Console.printStackTrace(e);
		}
	}
	/**
	 * Invokes client listener...should only be called once(At server start up)
	 */
	public void listen(){
		new client_listener(this).start();
	}
	/**
	 * Creates thread to listen and accept incoming connections (Creating threaded SocketClient)
	 * then assigns them to clients variable...should only be
	 * called from listen()
	 */
	private class client_listener extends Thread{
		private SocketServer Server;

		client_listener(SocketServer server){
			this.Server = server;
			this.setName("Client Listener");
			this.setPriority(4);//below normal
		}

		public void run(){
			while(true){
				try {
					Socket ready = socket.accept();
					//TODO first check if the socket already exists...later..
					if(Server.clients.size() < Server.max_clients){
						for (int i = Server.client_counter; i < 50; i++){
							if(!Server.clients.containsKey(i)){
								Server.setClient(i, new SocketClient(Server, i, ready));
								Server.getClient(i).start();
								Server.client_counter++;
								break;
							}
						}
					}
					else{Server.Base.Console.warning("Too many clients connected, connection denied!");}
				}
				catch (IOException e){
					Base.Console.severe("IO error when attempting allow incoming connections, server may already be running!");
					Base.Console.printStackTrace(e);
					break;
				}
			}
		}
	}
	/**
	 * Action to perform once client connects to server
	 * Called by SocketClient itself(different thread)
	 * @param cObj Client Connection(this)
	 */
	public void onConnect(SocketClient cObj){
		cObj.send(CmdCompile.connected(Base.Settings.CLIENT_BUILD));
		cObj.send(CmdCompile.loginPrompt());
		//cObj.send("What was your name..?(\"new\" for new character)");
	}
	/**
	 * Accepts input from SocketClient
	 * @param cObj Client Connection(this)
	 * @param data Client input submitted
	 */
	public void onRead(SocketClient cObj, String data){
		String[] split = data.split("\n");

		for(String temp : split){
			if(temp != null){
				this.parseClientInput(cObj, temp);
			}
		}
	}
	/**
	 * Either prepares input for commands or for logging in
	 * @param cObj Client Connection(SocketClient)
	 * @param cdata Client command statement
	 */
	public void parseClientInput(SocketClient cObj, String cdata){
		cdata = StringFunctions.stripEnds(cdata);
		if (cObj.getCharEID() == 0){//character doesnt exist, player is in login screen
			//----------Logging in here------------
			LoginHandler.parse(Base, cObj,cdata);
			//------------Login done---------------
			return;
		}
		else{
			// Don't let the user spam us //
			//if (System.currentTimeMillis() - cObj.getLastTime() <= 0.0002) {return;}
			cObj.setLastTime();
			String[] fsplit = cdata.split(" ", 2);//split off only first word..0 is command, 1 is parameters
			com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Character c = Base.Game.getCharacter(cObj.getCharEID());
			if (c==null) return;

			//if($c->playerstate == 0){//if player is in a normal state
				if (fsplit[0].trim() == ""){
					//c.send("Do nothing on your own time...");
					return;
				}
				else{
					//cmdHandler::processCmd($c->getEID(),$command,$data,$cObject->isJava());
					if(fsplit.length == 1){
						CmdHandler.processCmd(c,fsplit[0],null);
					}
					else{
						CmdHandler.processCmd(c,fsplit[0],fsplit[1]);
					}
					return;
				}
			/*}
			else if($c->playerstate == 1){//if player is in a writing buffer
				writeHandler::whereTo($c,$cdata);
				return true;
			}
			else{return;}*/
		}
	}
	public SocketClient getClient(int id){
		if (this.clients.containsKey(id)){return this.clients.get(id);}
		else{return null;}
	}
	public Map<Integer, SocketClient> getClients(){
		return this.clients;
	}
	public void setClient(int id, SocketClient cObj){
		this.clients.put(id, cObj);
	}
	public void removeClient(int id){
		this.clients.remove(id);
	}
}
