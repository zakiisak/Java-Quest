package game.scene;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import game.FileLoader;
import game.Game;
import game.utils.FontUtils;
import game.utils.GuiWriter;

public class SceneXmlTester extends Scene {
	
	public SceneXmlTester() {
		super(FileLoader.get("document.xml"));
	}
	
	@Override
	public void gameLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preEnter() {
		System.out.println("Components:");
		// TODO Auto-generated method stub
		for(LocalComponent component : components)
		{
			System.out.println(component.component);
		}
		
	}

	@Override
	public void render() {
		if(Gdx.input.isKeyJustPressed(Keys.S))
		{
			GuiWriter.writeDocument(this, "document.xml");
		}
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		/*
		
		rainbow.increment();
		
		java.awt.Color color = java.awt.Color.getHSBColor(rainbow.getValue(), 1, 1);
		font.setColor((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, 1.0f);
		FontUtils.addText(font, "Game Engine v" + Game.GAME_VERSION, Gdx.graphics.getWidth() / 2, 64, FontUtils.ALLIGN_MIDDLE);
		//FontUtils.draw(font, batch, "Game Engine v" + Game.GAME_VERSION, Gdx.graphics.getWidth() / 2, 64, FontUtils.ALLIGN_MIDDLE);
//		TiledRenderer.drawTiled(batch, Sprite.button_default, 128, 256, 200, 200, 1);
		if(System.currentTimeMillis() - lastTime > 1000)
		{
			lastTime = System.currentTimeMillis();
			Gdx.graphics.setTitle(config.title + " v" + Game.GAME_VERSION  + " | Heap Memory: " + Gdx.app.getNativeHeap());
		}		
		 */
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
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
