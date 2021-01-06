package game.scene;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import game.FileLoader;
import game.Game;
import game.graphics.FontLoader;
import game.utils.FontUtils;
import game.utils.Incrementer;

public class SceneFontTest extends Scene {
	
	private BitmapFont font;
	private Incrementer rainbow = new Incrementer(0, 0, 1.0f, 0.001f, Incrementer.LIMIT_START_FROM_MIN);
	
	@Override
	public void gameLoad() {
		font = FontLoader.loadFontWithOutline(FileLoader.get("AGENCYR.ttf"), 110, Color.BLACK, 2, false);
	}

	@Override
	public void preEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		rainbow.increment();
		
		java.awt.Color color = java.awt.Color.getHSBColor(rainbow.getValue(), 1, 1);
		font.setColor((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, 1.0f);
		FontUtils.addText(font, "Game Engine v" + Game.GAME_VERSION, Game.RES_WIDTH / 2, 64, FontUtils.ALLIGN_MIDDLE);
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void dispose() {
		font.dispose();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
