package game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import game.FileLoader;

public class Gui {
	public static BitmapFont DefaultFont = new BitmapFont(true);//FontLoader.loadFontWithOutlineAndShadow(FileLoader.get("AGENCYR.TTF"), 15, Color.WHITE, new Color(0, 0, 0, 0.25f), 1, false, new Color(0, 0, 0, 0.15f), 1, 1);
	
	
	public static void requestCursorChange()
	{
		Gdx.input.cancelVibrate();
	}
	
}
