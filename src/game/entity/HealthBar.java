package game.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entitycomponent.CentifierComponent;
import game.entitycomponent.CounterComponent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.SpriteComponent;
import game.entitycomponent.StatComponent;
import game.entitycomponent.TransformComponent;
import game.graphics.FontRenderer;
import game.graphics.Sprite;
import game.utils.Color;
import game.utils.Numbers;

public class HealthBar extends Entity {
	public TransformComponent transform;
	public SizeComponent size;
	public StatComponent stats;
	public CounterComponent counter;
	
	private BigInteger lastHp = Numbers.ZERO;
	private boolean positive = false;
	private BigDecimal lastPercentage = new BigDecimal("1");
	private float hpCounter = 60;
	
	public Color color = new Color(0, 1, 0);
	
	public HealthBar(StatComponent stats, float x, float y, float width, float height)
	{
		this.stats = stats;
		transform = new TransformComponent(x, y);
		size = new SizeComponent((int) width, (int) height);
		SpriteComponent sprite = new SpriteComponent(Sprite.white, transform, size);
		sprite.color = new Color(0, 0, 0, 0.6f);
		sprite.outlineWidth = 1;
		counter = new CounterComponent(0, 180, 1);
		
		addComponent(transform);
		addComponent(size);
		addComponent(sprite);
		addComponent(new CentifierComponent(transform, size).disableY());
		addComponent(counter);
	}
	
	public void setBounds(float x, float y, float width, float height)
	{
		transform.x = x;
		transform.y = y;
		size.width = (int) width;
		size.height = (int) height;
	}
	
	
	
	@Override
	public void tick() {
		super.tick();
		if(counter.current >= counter.end)
			counter.current = 0;
		
		if(stats.hp.equals(lastHp) == false)
		{
			if(stats.hp.compareTo(lastHp) > 0)
				positive = true;
			else 
			{
				positive = false;
				hpCounter = 60;
				lastPercentage = new BigDecimal(lastHp).divide(new BigDecimal(stats.maxhp), 3, RoundingMode.UP);
			}
		}
		if(hpCounter > 0)
			hpCounter--;
		
		lastHp = stats.hp;
		
	}
	
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		if(hpCounter > 0)
		{
			if(positive)
				batch.setColor(0, 1, 0, hpCounter / 60.0f);
			else batch.setColor(1, 0, 0, hpCounter / 60.0f);
			Sprite.getSprite("healthbar").render(batch, transform.x, transform.y, (float) size.width * lastPercentage.floatValue(), size.height);
		}
		batch.setColor(color.r, color.g, color.b, 1);
		float percentage = new BigDecimal(stats.hp).divide(new BigDecimal(stats.maxhp), 3, RoundingMode.UP).floatValue();
		Sprite.getSprite("healthbar").render(batch, transform.x, transform.y, (float) size.width * percentage, size.height);
		batch.setColor(1, 1, 1, 1);
		String hpString = Numbers.format(stats.hp) + "#zzzz / " + Numbers.format(stats.maxhp);
		float fontScale = 1;
		if(stats.hp.compareTo(Numbers.ZERO) <= 0)
			batch.setColor(1, 0, 0, 1);
		else if(stats.hp.compareTo(stats.maxhp.divide(Numbers.THREE)) <= 0)
			batch.setColor(1.0f, 0.75f, 0, 1);
		FontRenderer.drawWithOutline(batch, Game.baseFont, hpString, transform.x + size.width / 2 - Game.baseFont.getWidth(hpString) / 2 * fontScale, transform.y + size.height / 2 - Game.baseFont.getHeight(hpString) / 1.5f * fontScale, 1, 1, Color.black);
		batch.setColor(1, 1, 1, 1);
	}
	
	
	
}
