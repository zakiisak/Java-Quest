package game.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.Input;
import game.entity.Creatures;
import game.entity.Items;
import game.entity.NetPlayer;
import game.entity.Worlds;
import game.events.TeleporterMerchant;
import game.graphics.FontRenderer;
import game.graphics.Sprite;
import game.utils.Color;
import game.utils.Point;
import game.worlds.Overworld;

public class SceneTeleport extends Scene {
	
	public static String worldMap = "overworld";
	public static int selectedPoint = 0;
	
	private int tick = 30;
	private static boolean white = true;
	
	@Override
	public void gameLoad() {
		// TODO Auto-generated method stub
		
	}
	
	public List<TeleportPoint> teleportPoints = new ArrayList<TeleportPoint>();
	
	@Override
	public void gameLoadPost() {
		super.gameLoadPost();
		Object overworldMapPoints = game.player.data.getObject("overworld" + "_points");
		if(overworldMapPoints != null)
		{
			try
			{
				((List<Point>)overworldMapPoints).add(new Point(25, 67));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else 
		{
			List<Point> points = new ArrayList<Point>();
			points.add(new Point(25, 67));
			game.player.data.setObject("overworld" + "_points", points);
		}
		Object darkworldMapPoints = game.player.data.getObject("dark_world" + "_points");
		if(overworldMapPoints != null)
		{
			try
			{
				((List<Point>)darkworldMapPoints).add(new Point(50, 49));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else 
		{
			List<Point> points = new ArrayList<Point>();
			points.add(new Point(50, 49));
			game.player.data.setObject("dark_world" + "_points", points);
		}
	}

	@Override
	public void preEnter() {
		game.world.dead = true;
		tick = 30;
		white = true;
		selectedPoint = 0;
	}
	
	private static abstract class TeleportPoint
	{
		public Point point;
		
		public TeleportPoint(Point point)
		{
			this.point = point;
		}
		
		public abstract void tick();
		public abstract void draw(SpriteBatch batch, float x, float y, boolean selected);
	}
	
	private static class StaticTeleportPoint extends TeleportPoint
	{

		public StaticTeleportPoint(Point point) {
			super(point);
		}

		@Override
		public void tick() {}

		@Override
		public void draw(SpriteBatch batch, float x, float y, boolean selected) {
			if(selected)
			{
				if(white)
					Sprite.getSprite("tppoint").render(batch, x - 4, y - 4, 16, 16);
				else 
					Sprite.getSprite("tppoint2").render(batch, x - 4, y - 4, 16, 16);
			}
			else
				Sprite.getSprite("tppoint2").render(batch, x - 4, y - 4, 16, 16);
		}
	}
	
	private static class PlayerTeleportPoint extends TeleportPoint
	{
		private NetPlayer player;

		public PlayerTeleportPoint(NetPlayer player) {
			super(player.transform.point());
			this.player = player;
		}

		@Override
		public void tick() {
			this.point = player.transform.point();
		}

		@Override
		public void draw(SpriteBatch batch, float x, float y, boolean selected) {
			if(selected)
			{
				if(white)
					Sprite.getSprite("tppoint").render(batch, x - 4, y - 4, 16, 16);
				else 
					Sprite.getSprite("tppoint2").render(batch, x - 4, y - 4, 16, 16);
			}
			else
				Sprite.getSprite("tppoint2").render(batch, x - 4, y - 4, 16, 16);
			FontRenderer.drawWithOutline(batch, Game.baseFont, player.name, x - Game.baseFont.getWidth(player.name) / 2 + 4, y - 6 - 6, 1, 1, Color.black);
		}
	}
	
	private void addBaseTeleportPoints()
	{
		if(game.world instanceof Overworld)
		{
			teleportPoints.add(new StaticTeleportPoint(new Point(72, 75)));
//			teleportPoints.add(new StaticTeleportPoint(new Point(25, 99 - 32)));
		}
	}

	@Override
	public void render() {
		tick--;
		if(tick <= 0)
		{
			white = !white;
			tick = 30;
		}
		
		int size = 1;
		Object worldMapPoints = game.player.data.getObject(worldMap + "_points");
		if(worldMapPoints != null)
		{
			List<Point> points = (List<Point>) worldMapPoints;
			
			List<Point> pointsUntilNow = new ArrayList<Point>();
			for(int i = 0; i < points.size(); i++)
			{
				Point point = points.get(i);
				boolean equals = false;
				for(int k = 0; k < pointsUntilNow.size(); k++)
				{
					Point otherPoint = pointsUntilNow.get(k);
					if(otherPoint.x == point.x && otherPoint.y == point.y)
					{
						equals = true;
						break;
					}
				}
				if(equals == false)
					pointsUntilNow.add(point);
			}
			points.clear();
			for(int i = 0; i < pointsUntilNow.size(); i++)
			{
				points.add(pointsUntilNow.get(i));
			}
			
			int length = teleportPoints.size();
			teleportPoints.clear();
			addBaseTeleportPoints();
			//Add spawnpoints
			for(int i = 0; i < points.size(); i++)
			{
				Point point = points.get(i);
				teleportPoints.add(new StaticTeleportPoint(point));
			}
			if(game.player.possessItem(Items.TELEPORT_UPGRADE))
			{
				//Add player teleportations
				for(int i = 0; i < game.client.clients.size(); i++)
				{
					NetPlayer player = game.client.clients.get(i);
					if(player.world == game.instance.world.id)
					{
						teleportPoints.add(new PlayerTeleportPoint(player));
					}
				}
			}
			if(length != teleportPoints.size())
			{
				if(length > teleportPoints.size())
				{
					int diff = length - teleportPoints.size();
					selectedPoint -= diff;
					if(selectedPoint < 0)
						selectedPoint = 0;
					if(selectedPoint >= teleportPoints.size())
						selectedPoint = teleportPoints.size() - 1;
				}
			}
		}
		else
		{
			teleportPoints.clear();
			addBaseTeleportPoints();
		}
		size = teleportPoints.size();
		
		
		if(Input.keyJustPressed(Keys.W) || Input.keyJustPressed(Keys.UP) || Input.keyJustPressed(Keys.LEFT) || Input.keyJustPressed(Keys.A))
		{
			selectedPoint--;
			if(selectedPoint < 0)
				selectedPoint = size - 1;
			tick = 30;
			white = true;
		}
		if(Input.keyJustPressed(Keys.S) || Input.keyJustPressed(Keys.DOWN) || Input.keyJustPressed(Keys.RIGHT) || Input.keyJustPressed(Keys.D))
		{
			selectedPoint++;
			if(selectedPoint >= size)
				selectedPoint = 0;
			tick = 30;
			white = true;
		}
		if(Input.keyJustPressed(Keys.SPACE))
		{
			if(worldMapPoints != null)
			{
				TeleportPoint tp = teleportPoints.get(selectedPoint);
				Point point = tp.point;
				Worlds.transferPlayer(game.instance.world.getClass(), point.x, point.y);
				if(tp instanceof PlayerTeleportPoint)
				{
					game.player.input.cancelResetCoordinates();
				}
				game.setScene(SceneGame.class);
				game.getAudio().getSound("teleport").play();
			}
		}
		if(Input.keyJustPressed(Keys.ESCAPE))
		{
			game.setScene(SceneGame.class);
		}
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		if(worldMap.toLowerCase().equals("overworld"))
			GL11.glClearColor(55 / 255f, 138 / 255f, 209 / 255f, 1.0f);
		if(worldMap.toLowerCase().equals("dark_world"))
			GL11.glClearColor(200f / 255f, 117f / 255f, 46 / 255f, 1.0f);
		batch.setColor(1, 1, 1, 1);
		Sprite.getSprite(worldMap).render(batch, Game.RES_WIDTH / 2 - 400 / 2, Game.RES_HEIGHT / 2 - 400 / 2, 400, 400);
		if(worldMapPoints != null)
		{
			@SuppressWarnings("unchecked")
			List<Point> spawnPoints = (List<Point>) worldMapPoints;
			if(spawnPoints.size() > 0)
			{
				for(int i = 0; i < teleportPoints.size(); i++)
				{
					TeleportPoint point = teleportPoints.get(i);
					float x = Game.RES_WIDTH / 2 - 400 / 2 + point.point.x * 4 - 4;
					float y = Game.RES_HEIGHT / 2 - 400 / 2 + point.point.y * 4 - 4;
					point.tick();
					point.draw(batch, x, y, i == selectedPoint);
					
				}
			}
		}
		Sprite.getSprite("escape_identifier").renderWithOutline(batch, 16, 16, 32, 32, 1, Color.black);
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
