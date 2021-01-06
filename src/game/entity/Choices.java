package game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.Input;
import game.entitycomponent.LabelComponent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.SpriteComponent;
import game.entitycomponent.TransformComponent;
import game.graphics.Font;
import game.graphics.Sprite;

public class Choices extends Entity {
	
	public Event event;
	public TransformComponent transform;
	public SizeComponent size;
	public LabelComponent label;
	public int choice = 0;
	public String[] choices;
	public float lineHeight;
	public boolean active = true;
	public boolean tickable = true;
	public boolean visible = true;
	public boolean choicesRecursive = true;
	public boolean keyJustCheck = true;
	public boolean soundsEnabled = true;
	public boolean[] holdDownChoices; //Requires to hold down space for 1 second
	public int holdDownCounter = 0;
	public boolean blockIfMessage = false;
	
	public Choices(Event event, String... choices)
	{
		this.event = event;
		holdDownChoices = new boolean[choices.length];
		for(int i = 0; i < choices.length; i++)
		{
			if(choices[i].startsWith("§"))
			{
				choices[i] = choices[i].substring(1);
				holdDownChoices[i] = true;
			}
		}
		this.choices = choices;
		Font font = Game.baseFont;
		float scale = 2;
		lineHeight = font.lineHeight * scale;
		float maxWidth = 0;
		for(String choice : choices)
		{
			float width = font.getWidth(choice) * scale;
			if(width > maxWidth)
			{
				maxWidth = width;
			}
		}
		maxWidth += 48;
		
		String fullString = "";
		for(int i = 0; i < choices.length; i++)
			fullString += choices[i] + (i < choices.length - 1 ? "\n" : "");
		
		size = new SizeComponent((int) maxWidth, (int) (font.lineHeight * scale * choices.length));
		transform = new TransformComponent(Game.RES_WIDTH / 2 - maxWidth / 2, Game.RES_HEIGHT - font.lineHeight * scale * 3 - 32 - size.height);
		SpriteComponent sprite = new SpriteComponent(Sprite.getSprite("statborder"), transform, size);
		sprite.tiled = true;
		label = new LabelComponent(new TransformComponent(transform.x + 24, transform.y + 6), fullString, font);
		label.scale = 2;
		addComponent(transform);
		addComponent(transform);
		addComponent(sprite);
		addComponent(label);
	}
	
	public void setLocation(float x, float y){
		transform.set(x, y);
		label.transform.set(x + 24, y + 6);
	}
	
	@Override
	public void tick() {
		if(active == false || tickable == false || (MessageBox.current() != null && blockIfMessage)) return;
		super.tick();
		int choiceBefore = choice;
		if(Input.keyJustPressed(Keys.W) || Input.keyJustPressed(Keys.UP))
		{
			choice--;
			if(choice < 0)
				choice = choicesRecursive ? choices.length - 1 : 0;
		}
		if(Input.keyJustPressed(Keys.S) || Input.keyJustPressed(Keys.DOWN))
		{
			choice++;
			if(choice >= choices.length)
				choice = choicesRecursive ? 0 : choices.length - 1;
		}
		if(holdDownChoices[choice])
		{
			if(Gdx.input.isKeyPressed(Keys.SPACE))
			{
				holdDownCounter += 3;
				if(holdDownCounter >= 60)
				{
					dead = true;
					event.onChoice(this, choices[choice]);
					if(soundsEnabled) Game.instance.getAudio().getSound("switch").play(0.5f, 1.0f);
				}
					
			}
			else holdDownCounter = 0;
		}
		else
		{
			holdDownCounter = 0;
			if(keyJustCheck ? Input.keyJustPressed(Keys.SPACE) : Gdx.input.isKeyPressed(Keys.SPACE))
			{
				dead = true;
				event.onChoice(this, choices[choice]);
				if(soundsEnabled) Game.instance.getAudio().getSound("switch").play(0.5f, 1.0f);
			}
		}
		if(choiceBefore != choice)
		{
			Game.instance.getAudio().getSound("switch").play(0.5f, 1.5f);
		}
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		if(active == false || visible == false) return;
		super.drawPost(batch);
		Sprite.getSprite("hori_arrow").render(batch, transform.x + 4, transform.y + 6 + lineHeight * choice, 16, 16);
		Sprite.getSprite("hori_arrow").render(batch, transform.x + 4 + Game.baseFont.getWidth(choices[choice]) * 2 + 16 + 24, transform.y + 6 + lineHeight * choice, -16, 16);
		if(holdDownCounter > 0)
		{
			batch.setColor(1, 1, 1, 1);
			Sprite.getSprite("white").render(batch, transform.x + 24, transform.y + lineHeight * choice + lineHeight - 10, (Game.baseFont.getWidth(choices[choice]) * 2) * (holdDownCounter / 60.0f), 4);
		}
	}
	
	
	public static interface Event
	{
		public void onChoice(Choices choices, String choice);
	}
	
}
