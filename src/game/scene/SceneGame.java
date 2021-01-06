package game.scene;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import game.Game;
import game.entity.EntityGameover;
import game.entity.Items;
import game.entity.Player;
import game.entity.Worlds;
import game.graphics.Shaders;
import game.utils.Event;
import game.utils.ImageUtils;

public class SceneGame extends Scene {
	
	@Override
	public void gameLoad() {
	}
	
	public static void loadIfSave()
	{
		Game game = Game.instance;
		if(game.worldSaveExists())
		{
			game.world.added = false;
			game.player = game.world.playerReference;
		}
		else
		{
			game.world = Worlds.getActiveWorld();
			System.out.println("new player");
			game.player = new Player(game, game.world);
			game.world.playerReference = game.player;
			game.world.addEntity(game.player);
		}
	
	}
	
	@Override
	public void gameLoadPost() {
		loadIfSave();
	}
	
	@Override
	public void preEnter() {
		if(!game.world.added)
		{
			game.world.dead = false;
			game.addEntity(game.world);
		}
		game.world.onEntered();
		game.player.onWorldLoaded();
	}
	
	private boolean gameover;

	@Override
	public void render() {
		if(Gdx.input.isKeyJustPressed(Keys.H))
			ImageUtils.savePixmap("screenshot.png", ImageUtils.takeScreenshot());
		if(Gdx.input.isKeyJustPressed(Keys.L))
		{
			game.saveGame();
		}
		if(game.player.possessItem(Items.BOOK_OF_COLORS))
		{
			if(batch.getShader() != Shaders.defaultShader)
			{
				batch.setShader(Shaders.defaultShader);
			}
		}
		else
		{
			if(batch.getShader() != Shaders.grayShader)
			{
				batch.setShader(Shaders.grayShader);
				System.out.println("Gray shader");
			}
		}
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		if(game.player.killed && gameover == false)
		{
			EntityGameover go = new EntityGameover(new Event() {
				
				@Override
				public void run() {
					gameover = false;
				}
			});
			gameover = true;
			game.addEntity(go);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void dispose() {
		
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
