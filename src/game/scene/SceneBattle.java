package game.scene;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import game.Game;
import game.entity.Items;
import game.graphics.Shaders;
import game.graphics.Sprite;

public class SceneBattle extends Scene {
	
	public static String battle_back = "battle_back";
	
	
	@Override
	public void gameLoad() {
	}
	
	private Sprite background;
	
	public void gameLoadPost() 
	{
	}

	@Override
	public void preEnter() {
		game.world.dead = true;
		background = Sprite.getSprite(battle_back);
	}

	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		ShaderProgram defShader = batch.getShader();
		if(game.world.customShader != null) 
		{
			batch.setShader(game.player.possessItem(Items.BOOK_OF_COLORS) == false ? Shaders.grayShader : game.world.getShader());
		}
		background.render(batch, 0, 0, Game.RES_WIDTH, Game.RES_HEIGHT);
		if(game.world.customShader != null) batch.setShader(defShader);
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
