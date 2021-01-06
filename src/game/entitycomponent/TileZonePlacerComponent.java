package game.entitycomponent;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;
import game.tile.Tile;
import game.worlds.World;

public class TileZonePlacerComponent extends EntityComponent {

	public World world;
	private short zone;
	public boolean placeSolids = false;
	
	public TileZonePlacerComponent(World world) 
	{
		this.world = world;
	}
	
	@Override
	public String getName() {
		return "tile_zone_placer";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		if(Gdx.input.isButtonPressed(Buttons.LEFT))
		{
			final int mx = (int) (Gdx.input.getX() + world.cameraX) / Tile.SIZE;
			final int my = (int) (Gdx.input.getY() + world.cameraY) / Tile.SIZE;
			if(placeSolids)
			{
				world.setPassable(false, mx, my);
			}
			else world.setZone(zone, mx, my);
		}
		else if(Gdx.input.isButtonPressed(Buttons.RIGHT) && placeSolids)
		{
			final int mx = (int) (Gdx.input.getX() + world.cameraX) / Tile.SIZE;
			final int my = (int) (Gdx.input.getY() + world.cameraY) / Tile.SIZE;
			world.setPassable(true, mx, my);
		}
		if(Gdx.input.isKeyJustPressed(Keys.Z))
		{
			String output = JOptionPane.showInputDialog(null, "Enter zone number: 0-16383", zone);
			try
			{
				zone = Short.parseShort(output);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

}
