package game.entitycomponent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.Entity;
import game.entity.Items;
import game.graphics.FontRenderer;
import game.graphics.Sprite;
import game.gui.TiledRenderer;
import game.utils.Color;
import game.utils.Numbers;

public class StatDrawComponent extends Entity {
	public StatComponent stats;
	public boolean drawXp = false;
	public float width;
	public float height = 40;
	
	private BigInteger lastHp = Numbers.ZERO;
	private float overlayCounter = 0;
	private boolean positive = false;
	public boolean drawHealth = false;
	
	private float lastPercentage;
	private float hpCounter = 60;
	
	private static DecimalFormat formatter = new DecimalFormat("0.##");
	
	public StatDrawComponent(StatComponent stats)
	{
		this.stats = stats;
		lastHp = stats.hp;
	}
	@Override
	public void tick() {
		if(overlayCounter > 0)
		{
			overlayCounter--;
		}
		if(stats.hp != lastHp)
		{
			overlayCounter = 60;
			if(stats.hp.compareTo(lastHp) > 0)
				positive = true;
			else 
			{
				positive = false;
				if(drawHealth)
				{
					hpCounter = 60;
					lastPercentage = new BigDecimal(lastHp).divide(new BigDecimal(stats.maxhp), 3, RoundingMode.UP).floatValue();
				}
			}
		}
		if(hpCounter > 0)
			hpCounter--;
		
		lastHp = stats.hp;
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		draw(batch, 0, 0, null);
		Items.getItemSprite(46).renderWithOutline(batch, Game.RES_WIDTH - 48, Game.RES_HEIGHT - 48, 32, 32, 1, Color.black);
		batch.setColor(1, 0.75f, 0, 1);
		boolean formatNumbers = Game.instance.player.data.getBoolean("stats_formatNumbers", false);
		String goldString = formatNumbers ? Numbers.format(stats.gold) : stats.gold.toString();//formatter.format(new BigDecimal(stats.gold).divide(new BigDecimal(Game.instance.player.getEnemyMultiplier()), 3, RoundingMode.HALF_UP));
		float scale = 2;
		FontRenderer.drawWithOutline(batch, Game.baseFont, goldString, Game.RES_WIDTH - 48 - 4 - Game.baseFont.getWidth(goldString) * scale, Game.RES_HEIGHT - 32 - Game.baseFont.lineHeight / 2 * scale , scale, 1, Color.black);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void draw(SpriteBatch batch, float xoffset, float yoffset, String name)
	{
		boolean formatNumbers = Game.instance.player.data.getBoolean("stats_formatNumbers", false);
		String hpString = "HP: " + (formatNumbers ? Numbers.format(stats.hp) : stats.hp) + " / " + (formatNumbers ? Numbers.format(stats.maxhp) : stats.maxhp);
		String xpString = "XP: " + (formatNumbers ? Numbers.format(stats.xp) : stats.xp) + " / " + (formatNumbers ? Numbers.format(stats.xplimit) : stats.xplimit);
		String lvlString = "Lv: " + (formatNumbers ? Numbers.format(stats.lvl) : stats.lvl);
		float width = Math.max(100, Math.max(Game.baseFont.getWidth(hpString) + 2, Game.baseFont.getWidth(drawXp ? xpString : lvlString)));
		width = Math.max(width, name != null ? Game.baseFont.getWidth(name) : 0);
		if(name != null)
			height = 40 + Game.baseFont.lineHeight;
		this.width = width;
		float x = 4 + xoffset;
		float y = Game.RES_HEIGHT - height - 4 + yoffset;
		batch.setColor(1, 1, 1, 1);
		if(drawHealth)
		{
			float percentage = new BigDecimal(stats.hp).divide(new BigDecimal(stats.maxhp), 3, RoundingMode.UP).floatValue();
			batch.setColor(0, 0, 0, 1);
			Sprite.getSprite("white").render(batch, x, y, width, height);
			if(hpCounter > 0)
			{
				if(positive)
					batch.setColor(0, 1, 0, hpCounter / 60.0f);
				else batch.setColor(1, 0, 0, hpCounter / 60.0f);
				Sprite.getSprite("healthbar").render(batch, x, y, width * lastPercentage, height);
			}
			batch.setColor(0, 1, 0, 1);
			Sprite.getSprite("healthbar").render(batch, x, y, width * percentage, height);
			batch.setColor(1, 1, 1, 1);
			TiledRenderer.drawTiledBorder(batch, Sprite.getSprite("statborder"), x, y, width, height, 1);
		}
		else
		{
			TiledRenderer.drawTiledBox(batch, Sprite.getSprite("statborder"), x, y, width, height, 1);
		}
		if(overlayCounter > 0)
		{
			float percentage = overlayCounter / 60.0f;
			if(positive)
				batch.setColor((1.0f - percentage), 1.0f, (1.0f - percentage), 1.0f);
			else
			batch.setColor(1.0f, (1.0f - percentage), (1.0f - percentage), 1.0f);
		}
		else if(stats.hp.compareTo(Numbers.ZERO) <= 0)
			batch.setColor(1, 0, 0, 1);
		else if(stats.hp.compareTo(stats.maxhp.divide(Numbers.THREE)) <= 0)
			batch.setColor(1.0f, 0.75f, 0, 1);
		FontRenderer.draw(batch, Game.baseFont, hpString, x + 2, y + 2);
		batch.setColor(1, 1, 1, 1);
		FontRenderer.draw(batch, Game.baseFont, drawXp ? xpString : lvlString, x + 2, y + 2 + Game.baseFont.lineHeight);
		if(name != null) FontRenderer.draw(batch, Game.baseFont, name, x + 2, y + 2 + Game.baseFont.lineHeight * 2);
	}
}
