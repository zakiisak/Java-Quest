package game;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import game.entity.Worlds;

public class Console {
	
	private static ScriptEngine engine;
	
	private static Map<String, Object> savedVariables = new HashMap<String, Object>();
	public static String lastSelection = "";
	
	private static String getClass(String input, Class clazz)
	{
		input += " var " + clazz.getSimpleName() + " = Java.type(\"" + clazz.getName() + "\");";
		clazz.getName();
		return input;
	}
	
	public static void eval(String input)
	{
		if(engine == null)
		{
			engine = new ScriptEngineManager().getEngineByName("JavaScript");
		}
		if(engine == null)
		{
			return;
		}
		lastSelection = input;
		for(Entry<String, Object> entry : savedVariables.entrySet())
		{
			engine.put(entry.getKey(), entry.getValue());
		}
		engine.put("game", Game.instance);
		engine.put("player", Game.instance.player);
		engine.put("world", Game.instance.world);
		try {
			String classes = "";
			classes = getClass(classes, System.class);
			classes = getClass(classes, BigInteger.class);
			classes = getClass(classes, Worlds.class);
			classes += " load(\"nashorn:mozilla_compat.js\"); importPackage(Packages.game); "
					+ "importPackage(Packages.game.battle); "
					+ "importPackage(Packages.game.entitycomponent); "
					+ "importPackage(Packages.game.events); "
					+ "importPackage(Packages.game.graphics); "
					+ "importPackage(Packages.game.gui); "
					+ "importPackage(Packages.game.main); "
					+ "importPackage(Packages.game.net); "
					+ "importPackage(Packages.game.scene); "
					+ "importPackage(Packages.game.sound); "
					+ "importPackage(Packages.game.tile); "
					+ "importPackage(Packages.game.utils); "
					+ "importPackage(Packages.game.worlds); ";
			engine.eval(classes + input);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		
		for(Entry<String, Object> entry : bindings.entrySet())
		{
			savedVariables.put(entry.getKey(), entry.getValue());
		}
	}
	
}
