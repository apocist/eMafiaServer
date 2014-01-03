/* eMafiaServer - Flag.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2013  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes.classes.GameObjects;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.inverseinnovations.eMafiaServer.includes.Constants;
import com.inverseinnovations.eMafiaServer.includes.classes.ERS.FlagERS;


public class Flag extends GameObject{
	private FlagERS flagERS = null;
	private Map<String, String> ersScriptPre = new LinkedHashMap<String, String>();
	private Map<String, String> ersScriptPost = new LinkedHashMap<String, String>();

	public Flag(String name) {
		super(0, name, Constants.TYPE_GAMEOB_FLAG);

	}
	/**Returns the requested script for BEFORE the specified event. Returns null if no existing event or script.
	 * @param eventCall
	 * @return ERS Script
	 */
	public String getScriptPre(String eventCall){
		String theReturn = null;
		if(StringUtils.isNotEmpty(eventCall)){
			if(ersScriptPre.containsKey(eventCall)){
				theReturn = ersScriptPre.get(eventCall);
			}
		}
		return theReturn;
	}
	/**Saves an ERS Script for BEFORE the specified event
	 * @param eventCall
	 * @param script
	 */
	public void setScriptPre(String eventCall, String script){
		if(StringUtils.isNotEmpty(eventCall)){
			ersScriptPre.put(eventCall, script);
		}
	}
	/**Returns the requested script for AFTER the specified event. Returns null if no existing event or script.
	 * @param eventCall
	 * @return ERS Script
	 */
	public String getScriptPost(String eventCall){
		String theReturn = null;
		if(StringUtils.isNotEmpty(eventCall)){
			if(ersScriptPost.containsKey(eventCall)){
				theReturn = ersScriptPost.get(eventCall);
			}
		}
		return theReturn;
	}
	/**Saves an ERS Script for AFTER the specified event
	 * @param eventCall
	 * @param script
	 */
	public void setScriptPost(String eventCall, String script){
		if(StringUtils.isNotEmpty(eventCall)){
			ersScriptPost.put(eventCall, script);
		}
	}
	/**If this flag has a Post script*/
	public boolean isScriptedPost(){
		boolean theReturn = true;
		if(ersScriptPost.isEmpty()){
			theReturn = false;
		}
		return theReturn;
	}
	/**If this flag has a Pre script*/
	public boolean isScriptedPre(){
		boolean theReturn = true;
		if(ersScriptPre.isEmpty()){
			theReturn = false;
		}
		return theReturn;
	}
	public boolean isScripte(){
		boolean theReturn = false;
		if(this.isScriptedPre()){
			theReturn = true;
		}
		else if(this.isScriptedPost()){
			theReturn = true;
		}
		return theReturn;
	}



	public FlagERS getERSClass(){
		if(this.flagERS == null){
			this.flagERS = new FlagERS(this);
		}
		return this.flagERS;
	}

}
