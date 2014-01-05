/* eMafiaServer - CmdHandler.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes;

import java.lang.reflect.InvocationTargetException;

import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Character;

public class CmdHandler {
	/**
	 * Performs commands based on location the character is at(match or lobby),
	 * and detrimines whether the command even exists or not.
	 * @param c Character doing the command
	 * @param command The snytax
	 * @param phrase parameters for snytax
	 */
	@SuppressWarnings("unused")
	public static void processCmd(Character c,String command,String phrase){
		//c.Game.Base.Console.debug("processCmd started: "+command+" "+phrase);
		Class<?> clas = null;
		if(c.getInGame() == false){clas = LobbyCmd.class;}
		else{clas = MatchCmd.class;}//disable until it exists
		if(clas != null){
			if(!command.startsWith("-")){
				if(phrase != null){command += " "+phrase;}
				if(clas.equals(LobbyCmd.class)){LobbyCmd.say(c,command);}
				else if(clas.equals(MatchCmd.class)){MatchCmd.say(c,command);}
				return;
			}
			else{
				command = command.toLowerCase().substring(1);
				//c.Game.Base.Console.finest("prepped '"+command+" "+phrase+"' for invoke");
				if(clas.equals(LobbyCmd.class)){
					for(String fullword : LobbyCmd.CMDLIST){
						//if(command.substring(1).equals(fullword.substring(0, command.length()))){//more processing version(allows short cmds)(debug only)
						if(command.equals(fullword)){//less processing version(for releases)
							doInvoke(grabMethod(clas,fullword),clas,c,phrase);
						}
					}
				}
				else if(clas.equals(MatchCmd.class)){
					for(String fullword : MatchCmd.CMDLIST){
						//if(command.substring(1).equals(fullword.substring(0, command.length()))){//more processing version(allows short cmds)(debug only)
						if(command.equals(fullword)){//less processing version(for releases)
							doInvoke(grabMethod(clas,fullword),clas,c,phrase);
						}
					}
				}
				return;
			}
		}
		//c.send(command+" is not understood.");
		return;
	}

	private static java.lang.reflect.Method grabMethod(Object clas, String name){
		java.lang.reflect.Method method = null;
		try {
			@SuppressWarnings("rawtypes")
			Class[] par=new Class[2];
			par[0]=Character.class;
			par[1]=String.class;
			method = ((Class<?>) clas).getMethod(name,par);
		}
		catch (SecurityException e) {}
		catch (NoSuchMethodException e) {}
		return method;
	}
	private static void doInvoke(java.lang.reflect.Method method, Object clas, Character c, String phrase){
		if(method != null){
			try {
					Object[] arg=new Object[2];
					arg[0]=c;
					arg[1]=phrase;
					method.invoke(clas,arg);
			}
			catch (IllegalArgumentException e) {}
			catch (IllegalAccessException e) {}
			catch (InvocationTargetException e) {}
		}
	}
}
