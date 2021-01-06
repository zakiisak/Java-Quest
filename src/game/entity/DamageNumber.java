package game.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

import game.Game;
import game.entitycomponent.LabelComponent;
import game.entitycomponent.TransformComponent;
import game.utils.Color;
import game.utils.Numbers;

public class DamageNumber extends Entity {
	private static Random random = new Random();
	
	private TransformComponent transform;
	private LabelComponent label;
	
	private float mx, my, damper = 0.95f;
	private int life;
	
	public DamageNumber(float x, float y, BigInteger damage)
	{
		if(Game.instance.player.possessItem(Items.BOOK_OF_DAMAGE_NUMBERS) == false)
		{
			dead = true;
			return;
		}
		transform = new TransformComponent();
		transform.set(x, y);
		
		addComponent(transform);
		addComponent(label = new LabelComponent(transform, Numbers.format(damage), Game.dmgFont).setCentered());
		label.color = new Color(1, 60f / 255f, 11f / 255f, 1);
		label.drawPost = true;
		float angle = random.nextFloat() * 360f;
		final float speed = 5.0f;
		mx = (float) Math.cos(Math.toRadians(angle)) * speed;
		my = (float) Math.sin(Math.toRadians(angle)) * speed;
		life = 120;
	}
	
	@Override
	public void tick() {
		if(Game.instance.player.possessItem(Items.BOOK_OF_DAMAGE_NUMBERS) == false)
		{
			dead = true;
			return;
		}
		super.tick();
		transform.x += mx;
		transform.y += my;
		mx *= damper;
		my *= damper;
		life--;
		if(life <= 0)
		{
			label.scale *= 0.75f;
			if(label.scale <= 0.05)
				dead = true;
		}
	}
	
}
