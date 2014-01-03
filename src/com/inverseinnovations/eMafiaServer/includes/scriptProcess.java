/* eMafiaServer - scriptProcess.java
GNU GENERAL PUBLIC LICENSE V3
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaServer.includes;

import java.util.concurrent.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;

import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Match;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Role;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Team;


public class scriptProcess {
	private boolean scriptDebugging = true;
	static ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");



	public scriptProcess(final String string, final Match match){
		this(string, match, null, null, null);
	}
	public scriptProcess(final String string, final Team team){
		this(string, team.getMatch(), null, null, team);
	}
	public scriptProcess(final String string, final Role role){
		this(string, role.getMatch(), role, null, role.getTeam());
	}
	public scriptProcess(final String string, final Role role, final Role visitor){
		this(string, role.getMatch(), role, visitor, role.getTeam());
	}
	public scriptProcess(final String string, final Match match, final Role role, final Role visitor, final Team team){
		if(StringUtils.isNotBlank(string)){
			if(scriptDebugging){
				match.Game.Base.Console.debug("SCRIPT: doing script:");
				match.Game.Base.Console.debug(string);
			}
			Callable<Boolean> callable = new Callable<Boolean>() {
				public Boolean call() throws Exception {
					//return myMethod();
					js.put("match", match.getERSClass());
					if(role != null){
						js.put("self", role.getERSClass());
					}
					if(visitor != null){
						js.put("visitor", visitor.getERSClass());
					}
					if(team != null){
						js.put("team", team.getERSClass());
					}
					js.eval(string);
					return true;
				}
			};
			ExecutorService executorService = Executors.newCachedThreadPool();

			Future<Boolean> task = executorService.submit(callable);
			try {
				// ok, wait for 30 seconds max
				Boolean result = task.get(30, TimeUnit.SECONDS);
				if(scriptDebugging){match.Game.Base.Console.debug("Script finished with completely");}
			} catch (ExecutionException e) {
				String theScriptor = "";
				if(team != null){
					theScriptor = "team "+team.getName();
				}
				else if(role != null){
					theScriptor = "Role "+role.getName();
				}
				else{
					theScriptor = "Match";
				}
				String msg = "Script RuntimeException from "+theScriptor+"...: "+e.getMessage();
				match.Game.Base.Console.warning(msg);
				match.send(CmdCompile.genericPopup(msg));
				e.printStackTrace();
			} catch (TimeoutException e) {
				match.Game.Base.Console.warning("Script timeout...");
			} catch (InterruptedException e) {
				match.Game.Base.Console.warning("Script interrupted");
			}
		}
		return;
	}
}
