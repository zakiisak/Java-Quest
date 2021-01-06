package game.scene;

import java.lang.reflect.Field;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import game.Game;
import game.Input;
import game.entity.Choices;
import game.entity.Choices.Event;
import game.entity.Items;
import game.entity.Items.Item;
import game.graphics.FontRenderer;
import game.graphics.Sprite;
import game.gui.TiledRenderer;
import game.utils.Color;
import game.utils.Numbers;

import static game.entity.Items.availableItems;

public class SceneItems extends Scene {
	
	private static int lineWidth = 11;
	
	public static int selected = 0;
	
	@Override
	public void gameLoad() {
		Field[] fields = Items.class.getDeclaredFields();
		System.out.println("Fields:");
		for(int i = 0; i < fields.length; i++)
		{
			try {
				Field field = fields[i];
				System.out.println(fields[i].getName());
				Object variable = field.get(null);
				if(variable instanceof Integer)
				{
					Item item = new Item(field.getName().replaceAll("_", " "), (int) variable);
					availableItems.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("----------------------------\n");
	}
	
	@Override
	public void gameLoadPost() {
	}
	
	@Override
	public void preEnter() {
		game.world.dead = true;
		Choices choices = new Choices(new Event() {
			@Override
			public void onChoice(Choices choices, String choice) {
				if(choice.equals("Leave"))
				{
					game.setScene(SceneGame.class);
				}
			}
		}, "Leave");
		choices.setLocation(choices.transform.x, choices.transform.y +76);
		game.addEntity(choices);
	}

	@Override
	public void render() {
		lineWidth = 11;
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0.25f, 0.25f, 0.25f, 0.25f);
		game.player.getComponent("gold_remover").tick();
		if(Input.keyJustPressed(Keys.W) || Input.keyJustPressed(Keys.UP))
		{
			if(selected >= lineWidth)
				selected -= lineWidth;
		}
		if(Input.keyJustPressed(Keys.S) || Input.keyJustPressed(Keys.DOWN))
		{
			if(selected < availableItems.size() - lineWidth)
				selected += lineWidth;
		}
		if(Input.keyJustPressed(Keys.A) || Input.keyJustPressed(Keys.LEFT))
		{
			if(selected >= 0)
			{
				selected--;
				if(selected % lineWidth == lineWidth - 1)
					selected += lineWidth;
			}
		}
		if(selected < 0)
			selected = 0;
		if(selected >= availableItems.size())
			selected = availableItems.size() - 1;
		if(Input.keyJustPressed(Keys.D) || Input.keyJustPressed(Keys.RIGHT))
		{
			if(selected < availableItems.size() - 1)
			{
				selected++;
				if(selected % lineWidth == 0)
					selected -= lineWidth;
			}
		}
		int x = Game.RES_WIDTH / 2 - (56 * lineWidth) / 2;
		int y = Game.RES_HEIGHT / 2 - (availableItems.size() / lineWidth * 56);
		for(int i = 0; i < availableItems.size(); i++)
		{
			Item item = availableItems.get(i);
			boolean hasOwned = game.player.possessedItems.get(item.id) != null;
			if(hasOwned)
			{
				if(game.player.getItemCount(item.id).compareTo(Numbers.ZERO) > 0)
					batch.setColor(1, 1, 1, 1);
				else
					batch.setColor(0.5f, 0.5f, 0.5f, 1);
			}
			else
				batch.setColor(0, 0, 0, 1);
			Items.getItemSprite(item.id).render(batch, x + (i % lineWidth) * 56, y + (i / lineWidth) * 56, 48, 48);
		}
		batch.setColor(1, 1, 1, 1);
		TiledRenderer.drawTiledBorder(batch, Sprite.getSprite("statborder"), x + (selected % lineWidth) * 56, y + (selected / lineWidth) * 56, 48, 48, 1);
		String str = "";
		if(game.player.possessedItems.get(availableItems.get(selected).id) != null)
		{
			long count = game.player.possessedItems.get(availableItems.get(selected).id); 
			str = availableItems.get(selected).name + (count != 1 ? " x" + count : "");
			
		}
		else str = "???";
		FontRenderer.drawWithOutline(batch, Game.baseFont, str, Game.RES_WIDTH / 2 - Game.baseFont.getWidth(str), Game.RES_HEIGHT - 140, 2, 1, Color.black);
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
