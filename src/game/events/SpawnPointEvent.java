package game.events;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.tile.Tile;
import game.utils.Point;
import game.worlds.DarkWorld;
import game.worlds.Overworld;
import game.worlds.World;

public class SpawnPointEvent extends TileEvent {
	
	private boolean pointAdded = false;
	public static List<SpawnPointEvent> spawnPoints = new ArrayList<SpawnPointEvent>();
	public boolean visible = false;
	
	public SpawnPointEvent()
	{
		spawnPoints.add(this);
	}
	
	public SpawnPointEvent setVisible()
	{
		visible = true;
		return this;
	}
	
	@Override
	public void onAdded(Object owner) {
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	public void addPoint()
	{
		if(pointAdded)
			return;
		String preName = world instanceof Overworld ? "overworld" : (world instanceof DarkWorld ? "dark_world" : null);
		if(preName == null)
			return;
		Object data = Game.instance.player.data.getObject(preName + "_points");
		if(data == null)
		{
			List<Point> points = new ArrayList<Point>();
			points.add(transform.point());
			Game.instance.player.data.setObject(preName + "_points", points);
		}
		else ((List<Point>) data).add(transform.point());
		pointAdded = true;
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
	@Override
	public void onStepped(Game game, final World world, final Player player) {
		super.onStepped(game, world, player);
		MessageBox.addMessage(new MessageBox("This is a", "spawn point"));
		if(player.spawnPoint.x == transform.getIntX() && player.spawnPoint.y == transform.getIntY())
		{
			MessageBox.addMessage(new MessageBox("You already", "have this set", "as spawn point"));
		}
		else
		{
			MessageBox.addMessage(new MessageBox("It costs", "500 G"));
			if(player.canAfford(new BigInteger("500")))
			{
				MessageBox.addMessage(new MessageBox("Buy?").setChoices(new ChoiceEvent() {
					
					@Override
					public void onChoice(MessageBox box, String choice) {
						if(choice.equals("Yes"))
						{
							player.spawnPoint.set(transform.getIntX(), transform.getIntY(), Worlds.getWorldId(world.getClass()));
							player.removeGold(new BigInteger("500"));
							addPoint();
						}
					}
				}, "Yes", "No"));
			}
			else
			{
				MessageBox.addMessage(new MessageBox("You don't ", "have enough"));
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if(visible)
			Tile.tiles[98].draw(batch, transform.getIntX(), transform.getIntY());
	}
	
	
}
