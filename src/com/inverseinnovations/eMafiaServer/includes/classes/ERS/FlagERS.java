package com.inverseinnovations.eMafiaServer.includes.classes.ERS;

import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Flag;

public class FlagERS{
	private Flag flag;
	public FlagERS(final Flag flag){
		if(this.flag == null){this.flag = flag;}
	}
	public String getScriptPost(String eventCall){
		return flag.getScriptPost(eventCall);
	}
	public String getScriptPre(String eventCall){
		return flag.getScriptPre(eventCall);
	}
	public void setScriptPost(String eventCall, String script){
		flag.setScriptPost(eventCall, script);
	}
	public void setScriptPre(String eventCall, String script){
		flag.setScriptPre(eventCall, script);
	}
}
