package game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.gui.Document;
import game.sound.AudioManager;
import game.utils.GuiReader;

public abstract class Scene extends Document {
	
	public Scene() {
		super(0, 0, Game.RES_WIDTH, Game.RES_HEIGHT);
		border = null; //Disables border around the scene.
	}
	
	public Scene(FileHandle designXmlFile)
	{
		Document document = GuiReader.readDocument(designXmlFile);
		this.setBounds(document);
		this.components = document.getComponents();
	}
	

	protected int sceneId = -1;
	protected Game game;
	protected LwjglApplicationConfiguration config;
	protected SpriteBatch batch;
	protected AudioManager audio;
	protected SceneManager scenes;
	
	public abstract void gameLoad();
	public void gameLoadPost() {}
	public abstract void preEnter();
	public abstract void render();
	public abstract void resize(int width, int height);
	public abstract void dispose();
	public abstract void pause();
	public abstract void resume();
	
	
	/**
	 * @return the index of an array of a scene manager.<br>
	 * If the scene has not been added to a scene manager, <br>
	 * this method returns -1.
	 */
	public int getSceneId()
	{
		return sceneId;
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public SpriteBatch getSpriteBatch()
	{
		return batch;
	}
	
	public AudioManager getAudio()
	{
		return audio;
	}
	
	/**
	 * Convenience method for game.setScene();
	 * @param id
	 */
	protected void setScene(Class<? extends Scene> sceneId)
	{
		game.setScene(sceneId);
	}
	
}
