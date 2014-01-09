/* eMafiaServer - scriptProcess.java
GNU GENERAL PUBLIC LICENSE V3*/
package com.inverseinnovations.eMafiaServer.includes;

import java.util.concurrent.*;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Match;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Role;
import com.inverseinnovations.eMafiaServer.includes.classes.GameObjects.Team;
import org.mozilla.javascript.*;

public class scriptProcess {
	private boolean scriptDebugging = true;
	//static ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");

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
					Context cx = createContext();
					Scriptable globalScope = new ImporterTopLevel(cx);

					//js.put("match", match.getERSClass());
					globalScope.put("match", globalScope, match.getERSClass());
					if(role != null){
						//js.put("self", role.getERSClass());
						globalScope.put("self", globalScope, role.getERSClass());
					}
					if(visitor != null){
						//js.put("visitor", visitor.getERSClass());
						globalScope.put("visitor", globalScope, visitor.getERSClass());
					}
					if(team != null){
						//js.put("team", team.getERSClass());
						globalScope.put("team", globalScope, team.getERSClass());
					}
					//js.eval(string);
					cx.evaluateString(globalScope, string, "Script", 1, null);
					return true;
				}
			};
			ExecutorService executorService = Executors.newCachedThreadPool();

			Future<Boolean> task = executorService.submit(callable);
			try{
				// ok, wait for 15 seconds max
				Boolean result = task.get(15, TimeUnit.SECONDS);
				if(scriptDebugging){match.Game.Base.Console.debug("Script finished with completely");}
			}
			catch (ExecutionException e) {
				String theScriptor = "Unknown";
				if(team != null){
					theScriptor = "Team "+team.getName();
				}
				else if(role != null){
					theScriptor = "Role "+role.getName();
				}
				else{
					theScriptor = "Match";
				}
				String msg = "Script RuntimeException from "+theScriptor+"...: "+e.getMessage()+"\n From Script:\n"+string;
				match.Game.Base.Console.warning(msg);
				match.send(CmdCompile.genericPopup(msg));
				//TODO need to 'wrap' msg to fit window
				e.printStackTrace();//Don't want this spamming the Console
			}
			catch (TimeoutException e) {
				match.Game.Base.Console.warning("Script timeout...");
			}
			catch (InterruptedException e) {
				match.Game.Base.Console.warning("Script interrupted");
			}
		}
		return;
	}
	private static Context createContext(){
		Context cx = Context.enter();
		cx.setClassShutter(new ClassShutter(){
			public boolean visibleToScripts(String className){
					if (className.startsWith("com.inverseinnovations.eMafiaServer.")|| className.startsWith("java.lang.")) {
						return true;
					}
					return false;
			}
		});
		return cx;
	}

}
