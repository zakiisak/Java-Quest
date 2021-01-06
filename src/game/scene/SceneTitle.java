package game.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import game.Game;
import game.entity.Choices;
import game.entity.Items;
import game.graphics.Shaders;
import game.graphics.Sprite;
import game.utils.Color;
import game.utils.Event;
import game.utils.Rand;

public class SceneTitle extends Scene {
	
	private static class Particle
	{
		public static Sprite sprite;
		public float x, y, size, mx, my;
		public int life, startLife;
		public Color color = new Color(1, 0.5f + Rand.rng.nextFloat() * 0.25f, 0.0f);
		
		public void tick()
		{
			if(life > 0)
				life--;
			x += mx;
			y += my;
			color.a = ((float) life / (float) startLife) * (1.0f - brightX);
			if(color.a < 0)
				color.a = 0;
		}
		
		public void draw(SpriteBatch batch)
		{
			batch.setColor(color.r, color.g, color.b, color.a);
			sprite.render(batch, x, y, size, size);
		}
		
		public Particle(float x, float y, float size, float speed)
		{
			this.x = x - size / 2;
			this.y = y - size / 2;
			this.size = size;
			mx = MathUtils.sinDeg(Rand.rng.nextInt(360)) * speed;
			my = MathUtils.sinDeg(Rand.rng.nextInt(360)) * speed;
			life = 30 + Rand.rng.nextInt(40);
			this.startLife = life;
		}
	}
	
	private List<Particle> particles = new ArrayList<Particle>();
	private List<Particle> embers = new ArrayList<Particle>();
	private Choices choices;
	private Event animationFinishEvent;
	
	@Override
	public void gameLoad() {
		
	}
	
	@Override
	public void gameLoadPost() {
		Particle.sprite = Sprite.getSprite("title_particle_2");
	}
	
	@Override
	public void preEnter() {
		game.world.dead = true;
		game.player.statDraw.dead = true;
		particles.clear();
		
		String[] choiceArray;
		if(game.worldSaveExists())
		{
			choiceArray = new String[] {"Load Game", "New Game"};
		}
		else choiceArray = new String[] {"New Game"};
		
		choices = new Choices(new Choices.Event() {
			
			@Override
			public void onChoice(Choices choices, String choice) {
				if(choice.equals("Load Game"))
				{
					animationFinishEvent = new Event() {
						@Override
						public void run() {
							game.setScene(SceneGame.class);
					}};
				}
				else if(choice.equals("New Game"))
				{
					animationFinishEvent = new Event() {
						@Override
						public void run() {
							game.newGame();
							game.setScene(SceneGame.class);
					}};
				}
				particles.clear();
			}
		}, choiceArray);
		choices.blockIfMessage = true;
		game.addEntity(choices);
		Shaders.titleShader.begin();
		Shaders.titleShader.setUniformf("gray", game.player.possessItem(Items.BOOK_OF_COLORS) ? 0.0f : 1.0f);
		Shaders.titleShader.end();
	}
	
	private float counter;
	private float aniCounter = 140;
	private float brightness = 0.25f;
	private static float brightX = 0;
	private float brightXSpeed = 0.007f;
	
	@Override
	public void render() {
		
		float widthPercentage = (float) Game.RES_WIDTH / 640.0f;
		float heightPercentage = (float) Game.RES_HEIGHT / 480.0f;
		if(animationFinishEvent == null)
		{
			int count = 1;
			for(int i = 0; i < count; i++)
			{
				float x = 80 + Rand.rng.nextFloat() * 470.0f * widthPercentage;
				float y = 42 + Rand.rng.nextFloat() * 232.0f * heightPercentage;
				float size = 128f * widthPercentage + Rand.rng.nextFloat() * 0.0f;
				Particle particle = new Particle(x, y, size, 2);
				particles.add(particle);
			}
		}
		//Animation
		else
		{
			brightX += brightXSpeed;
			if(aniCounter > 150)
			{
				/*
				brightness = 0.1f;
				Particle particle = new Particle(20, 42 + 128, 256, 2);
				particle.life *= 2;
				particle.mx = 5.0f;
				particle.my = -0.25f;
				particles.add(particle);
				*/
			}
			else if(aniCounter == 40)
			{
				brightXSpeed *= 1.5f;
			}
			aniCounter--;
			if(aniCounter < -20)
			{
				animationFinishEvent.run();
			}

		}
		
		final float startX = 40f;
		final float startY = 22f;
		final float spanWidth = 550.0f * widthPercentage;
		final float spanHeight = 280.0f * heightPercentage;
		final float spanWidthRadius = spanWidth / 2f;
		final float spanHeightRadius = spanHeight / 2f;
		final float centerX = startX + spanWidth / 2f;
		final float centerY = startY + spanHeight / 2f;
		final float speed = 2.0f;
		for(int i = 0; i < 5 * (widthPercentage + heightPercentage); i++)
		{
			
			float x = startX + Rand.rng.nextFloat() * spanWidth;
			float y = startY + Rand.rng.nextFloat() * spanHeight;
			float size = 8 * widthPercentage;
			Particle particle = new Particle(x, y, size, speed);
			float mx = (x - centerX) / spanWidthRadius;
			float my = (y - centerY) / spanHeightRadius;
			particle.color = new Color(1, 0.25f, 0, 1.0f);
			particle.mx = mx + (Rand.rng.nextFloat() - 0.5f) * speed;
			particle.my = my + (Rand.rng.nextFloat() - 0.5f) * speed * 2;
			embers.add(particle);
		}
		
		counter++;
		if(counter > 179)
			counter = 0;
		
		choices.setLocation(Game.RES_WIDTH / 2 - choices.size.width / 2 , Game.RES_HEIGHT - 130);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		
		
		
		for(int i = 0; i < embers.size(); i++)
		{
			Particle p = embers.get(i);
			p.tick();
			if(p.life < 1)
			{
				embers.remove(i);
				i--;
				continue;
			}
			p.draw(batch);
		}
		
		final float brightness = MathUtils.sinDeg(counter) * this.brightness * 2 + this.brightness;
		batch.setColor(brightness, brightness, brightness, 1);
		batch.setShader(animationFinishEvent != null ? Shaders.titleShader : (game.player.possessItem(Items.BOOK_OF_COLORS) ? Shaders.defaultShader : Shaders.grayShader));
		if(animationFinishEvent != null) Shaders.titleShader.setUniformf("brightX", brightX);
		Sprite.getSprite("jq_flame").render(batch, 0, 0, Game.RES_WIDTH, Game.RES_HEIGHT);
		Sprite.getSprite("jq_text").render(batch, 0, 0, Game.RES_WIDTH, Game.RES_HEIGHT); 
		batch.setShader(game.player.possessItem(Items.BOOK_OF_COLORS) ? Shaders.defaultShader : Shaders.grayShader);
		batch.setColor(1, 1, 1, 1);
		
		
		batch.setBlendFunction(GL11.GL_DST_COLOR, GL11.GL_ONE);
		for(int i = 0; i < particles.size(); i++)
		{
			Particle p = particles.get(i);
			p.tick();
			if(p.life < 1)
			{
				particles.remove(i);
				i--;
				continue;
			}
			p.draw(batch);
		}
		batch.setColor(1, 1, 1, 1);
		batch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); 
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
