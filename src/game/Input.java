package game;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import game.entity.Player;
import game.gui.TextWriter;

public class Input extends InputAdapter {
	
	private static TextWriter activeTextWriter;
	public static Set<Integer> justPressedKeysThisFrame = new HashSet<Integer>();
	
	public static void bindTextWriter(TextWriter component)
	{
		activeTextWriter = component;
	}
	
	public static void unbindTextWriter()
	{
		activeTextWriter = null;
	}
	
	public static TextWriter getBoundTextWriter()
	{
		return activeTextWriter;
	}
	
	
	public boolean keyTyped(char character) {
		if(activeTextWriter != null) activeTextWriter.input(character);
		return false;
	}
	
	public static void inputStatistics()
	{
		Player player = Game.instance.player;
		if(player == null)
			return;
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
		{
			player.data.setObject("", ((BigInteger)player.data.getObject("space_pressed", BigInteger.ZERO)).add(BigInteger.ONE));
		}
	}
	
	public static boolean keyJustPressed(int key)
	{
		if(justPressedKeysThisFrame.contains(key))
			return false;
		boolean pressed = Gdx.input.isKeyJustPressed(key);
		if(pressed)
		{
			justPressedKeysThisFrame.add(key);
		}
		return pressed;
	}
	
}
