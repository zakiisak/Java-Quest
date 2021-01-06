package game.scene;

import java.util.ArrayList;
import java.util.List;

import game.Game;

public class SceneManager {
	
	protected Game game;
	protected int activeScene = 0;
	protected List<Scene> scenes = new ArrayList<Scene>();
	
	public SceneManager(Game game)
	{
		this.game = game;
		//addScene(new SceneGameEngine());
		//addScene(new SceneFontTest());
		//addScene(new SceneXmlTester());
		addScene(new SceneGame());
		addScene(new SceneBattle());
		addScene(new SceneEditor());
		addScene(new SceneItems());
		addScene(new SceneTeleport());
		addScene(new SceneCrafting());
		addScene(new SceneTitle());
		
	}
	
	public Scene getActiveScene()
	{
		return scenes.get(activeScene);
	}
	
	public void preEnter()
	{
		scenes.get(activeScene).setBounds(0, 0, Game.RES_WIDTH, Game.RES_HEIGHT);
		scenes.get(activeScene).preEnter();
	}
	
	public void gameLoadAll()
	{
		for(Scene scene : scenes)
			scene.gameLoad();
	}
	
	public void gameLoadAllPost()
	{
		for(Scene scene : scenes)
			scene.gameLoadPost();
	}
	
	public void resize(int width, int height)
	{
		scenes.get(activeScene).resizeDocument(width, height);
		scenes.get(activeScene).resize(width, height);
	}
	
	public void render()
	{
		scenes.get(activeScene).render();
		scenes.get(activeScene).render(game.getSpriteBatch());
	}
	
	public void pause()
	{
		scenes.get(activeScene).pause();
	}
	
	public void resume()
	{
		scenes.get(activeScene).resume();
	}
	
	public void disposeAll()
	{
		for(Scene scene : scenes)
			scene.dispose();
		scenes.clear();
	}
	
	public void setScene(Class<? extends Scene> c)
	{
		
		int index;
		for(index = 0; index < scenes.size(); index++)
			if(scenes.get(index).getClass().getName().equals(c.getName()))
				break;
		this.activeScene = index;
		scenes.get(activeScene).preEnter();
	}
	
	/**
	 * @param scene
	 * @return the id of the newly added scene.
	 */
	public int addScene(Scene scene)
	{
		scene.game = game;
		scene.batch = game.getSpriteBatch();
		scene.audio = game.getAudio();
		scene.config = game.config;
		scene.scenes = this;
		scene.sceneId = scenes.size() - 1;
		scenes.add(scene);
		return scenes.size() - 1;
	}
	
	public Scene getScene(int index)
	{
		return scenes.get(index);
	}
	
}
