package game.scene;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;

import game.Game;
import game.Input;
import game.entity.Items;
import game.entity.Recipe;
import game.entity.Recipe.Integrant;
import game.graphics.FontRenderer;
import game.graphics.Sprite;
import game.gui.TiledRenderer;
import game.utils.Color;
import game.utils.Event;
import game.utils.Numbers;

public class SceneCrafting extends Scene {
	
	private int selected = 0;
	private boolean popupsBusy = false;
	private float counter;
	
	@Override
	public void gameLoad() {
		
	}
	
	@Override
	public void gameLoadPost() {
	}
	
	@Override
	public void preEnter() {
		game.world.dead = true;
		/*
		Choices choices = new Choices(new Event() {
			@Override
			public void onChoice(Choices choices, String choice) {
				if(choice.equals("Leave"))
				{
					game.setScene(SceneGame.class);
				}
			}
		}, "Leave");
		choices.setLocation(choices.transform.x, choices.transform.y + 100);
		game.addEntity(choices);
		*/
		selected = 0;
	}

	@Override
	public void render() {
		counter++;
		if(counter >= 180)
			counter = 0;
		
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0.25f, 0.25f, 0.25f, 1.0f);
		List<Recipe> recipes = game.player.recipes;
		if(Input.keyJustPressed(Keys.W) || Input.keyJustPressed(Keys.UP) && popupsBusy == false)
		{
			selected--;
			if(selected < 0)
				selected = recipes.size() - 1;
		}
		if(Input.keyJustPressed(Keys.S) || Input.keyJustPressed(Keys.DOWN) && popupsBusy == false)
		{
			selected++;
			if(selected >= recipes.size())
				selected = 0;
		}
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) && popupsBusy == false)
		{
			game.setScene(SceneGame.class);
			return;
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE) && popupsBusy == false)
		{
			Recipe recipe = recipes.get(selected);
			if(recipe.canPlayerProduce(game.player))
			{
				popupsBusy = true;
				recipe.produce(game.player, 5, new Event() {
					@Override
					public void run() {
						popupsBusy = false;
					}
				});
				if(recipe.oneTimer)
				{
					recipes.remove(selected);
					selected--;
				}
			}
			else
			{
				game.getAudio().getSound("buzzer").play(0.25f);
			}
		}
		
		int lineLength = Gdx.graphics.getHeight() / 48;
		for(int r = selected - lineLength / 2; r < selected + lineLength / 2; r++)
		{
			if(r < 0 || r >= recipes.size())
				continue;
			if(r == selected)
				batch.setColor(1, 1, 1, 1);
			else
				batch.setColor(0.5f, 0.5f, 0.5f, 1);
			Recipe recipe = recipes.get(r);
			float size = r == selected ? 64 : 48;
			float width = (recipe.input.length + recipe.output.length
					+ (recipe.input.length + recipe.output.length - 2)) * (size + 8) + 12;
			float startx = Game.RES_WIDTH / 2 - width / 2;
			float x = startx;
			float y = (48 + 12 + 20) * (r - selected) + Game.RES_HEIGHT / 2 - 48 / 2;
			float padding = 8;
			float plus_yoffset = r == selected ? 18 : 8;
			Map<Integer, BigInteger> itemMemory = new HashMap<Integer, BigInteger>();
			
			for(int i = 0; i < recipe.input.length; i++)
			{
				Integrant ingredient = recipe.input[i];
				boolean hasItem = itemMemory.get(ingredient.item) != null;
				if(game.player.getItemCount(ingredient.item).subtract( 
						(hasItem ? itemMemory.get(ingredient.item) : Numbers.ZERO)).compareTo(ingredient.count) < 0 && r == selected)
				{
					float sin = MathUtils.sinDeg(counter) * 0.5f;
					batch.setColor(0.5f + sin, 0.5f - sin, 0.5f - sin, 1);
				}
				itemMemory.put(ingredient.item, (itemMemory.get(ingredient.item) != null ? 
						itemMemory.get(ingredient.item) : Numbers.ZERO).add(ingredient.count));
				
				Sprite itemSprite = Items.getItemSprite(ingredient.item);
				itemSprite.renderWithOutline(batch, x, y, size, size, 1, Color.black);
				if(ingredient.count.compareTo(Numbers.ONE) > 0) 
				{
					String countString = formatNumString(ingredient.count);
					FontRenderer.drawWithOutline(batch, Game.baseFont, countString, x + size - Game.baseFont.getWidth(countString) * 2 + 4, y + size - 32 + (r != selected ? 8 : 0), 2, 2, Color.black);
				}
				if(r == selected)
					batch.setColor(1, 1, 1, 1);
				else
					batch.setColor(0.5f, 0.5f, 0.5f, 1);
				x += size + padding;
				if(i < recipe.input.length - 1)
				{
					Sprite plus = Sprite.getSprite("plus");
					plus.renderWithOutline(batch, x, y + plus_yoffset, plus.getWidth(), plus.getHeight(), 1, Color.black);
					x += plus.getWidth() + 8;
				}
			}
			Sprite arrow = Sprite.getSprite("crafting_arrow");
			arrow.renderWithOutline(batch, x - 4, y + plus_yoffset, arrow.getWidth() * 2, arrow.getHeight(), 1, Color.black);
			x += arrow.getWidth() * 1.5f + plus_yoffset;
			if(recipe.discovered == false)
				batch.setColor(0, 0, 0, 1);
			for(int o = 0; o < recipe.output.length; o++)
			{
				Integrant result = recipe.output[o];
				Sprite itemSprite = Items.getItemSprite(result.item);
				itemSprite.renderWithOutline(batch, x, y, size, size, 1, Color.black);
				if(result.count.compareTo(Numbers.ONE) > 0) 
				{
					String countString = formatNumString(result.count);
					FontRenderer.drawWithOutline(batch, Game.baseFont, countString, x + size - Game.baseFont.getWidth(countString) * 2 + 4, y + size - 32, 2, 1, Color.black);
				}
				x += size + padding;
				if(o < recipe.output.length - 1)
				{
					Sprite plus = Sprite.getSprite("plus");
					plus.render(batch, x, y + 8, plus.getWidth(), plus.getHeight());
					x += plus.getWidth() + 8;
				}
			}
			batch.setColor(1, 1, 1, 1);
			
			if(r == selected) TiledRenderer.drawTiledBorder(batch, Sprite.getSprite("statborder"), startx - 1, y, width, size, 1);
			y += size + 12;
		}
		
		Sprite.getSprite("escape_identifier").renderWithOutline(batch, 16, 16, 32, 32, 1, Color.black);
	}

	private static String formatNumString(BigInteger number)
	{
		if(number.compareTo(new BigInteger("1000000000")) >= 0)
			return (number.divide(new BigInteger("1000000000"))) + "B";
		if(number.compareTo(new BigInteger("1000000")) >= 0)
			return (number.divide(new BigInteger("1000000"))) + "M";
		if(number.compareTo(new BigInteger("1000")) >= 0)
			return (number.divide(new BigInteger("1000"))) + "K";
		return number.toString();
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
