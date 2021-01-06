package game.scene;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.FileLoader;
import game.Game;
import game.entity.Entity;
import game.entity.EntityGameover;
import game.entity.Player;
import game.entitycomponent.MovementComponent;
import game.entitycomponent.TileZonePlacerComponent;
import game.graphics.FontRenderer;
import game.tile.Tile;
import game.utils.Event;
import game.utils.FileUtils;
import game.utils.ImageUtils;
import game.worlds.World;

public class SceneEditor extends Scene {
	
	private boolean drawSolids = false;
	private boolean drawData = true;
	
	@Override
	public void gameLoad() {
		
	}
	
	@Override
	public void gameLoadPost() {
		game.addEntity(new Entity()
		{
			@Override
			public void drawPost(SpriteBatch batch) {
				if(Game.debug == false) return;
				int mx = (int) (game.world.cameraX + Gdx.input.getX()) / Tile.SIZE;
				int my = (int) (game.world.cameraY + Gdx.input.getY()) / Tile.SIZE;
				short bt = game.world.getBackgroundTile(mx, my);
				short ft = game.world.getForegroundTile(mx, my);
				FontRenderer.draw(batch, Game.dmgFont, "" + game.player.transform.x + ", " + game.player.transform.y + "\nbt: " + bt + ", ft: " + ft, 32, 50, 1);
			}
		});
	}
	
	@Override
	public void preEnter() {
		//game.world = new World(FileLoader.get("world"));
		//game.player = new Player(game, game.world);
		
		((MovementComponent) game.player.getComponent("movement")).noclip = true;
		game.player.removeComponent("step_encounter");
		game.player.addComponent(new TileZonePlacerComponent(game.world));
		
		//game.world.playerReference = game.player;
		//game.world.addEntity(game.player);
		game.addEntity(new Entity()
		{
			@Override
			public void drawPost(SpriteBatch batch) {
				if(game.getSceneManager().getActiveScene() instanceof SceneEditor && drawData)
				{
					if(drawSolids)
					{
						game.world.drawSolids(batch);
					}
					else
					{
						game.world.drawZoneOverlay(batch);
					}
				}
			}
			
			
		});
		if(!game.world.added)
		{
			game.world.dead = false;
			game.addEntity(game.world);
		}
	}
	
	private boolean gameover;
	
	@Override
	public void render() {
		if(Gdx.input.isKeyJustPressed(Keys.H))
			ImageUtils.savePixmap("screenshot.png", ImageUtils.takeScreenshot());
		if(Gdx.input.isKeyJustPressed(Keys.P))
		{
			drawSolids = !drawSolids;
			((TileZonePlacerComponent) game.player.getComponent("tile_zone_placer")).placeSolids = drawSolids;
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
		{
			drawData = !drawData;
			((MovementComponent)game.player.getComponent("movement")).noclip = drawData;
		}
		if(Gdx.input.isKeyJustPressed(Keys.ENTER))
		{
			FileUtils.save(Gdx.files.local(game.world.path + "_data.dat"), game.world.zones, game.world.passables);
			System.out.println("saving data");
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
