package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;
import game.graphics.Sprite;
import game.gui.TiledRenderer;
import game.utils.Color;

public class SpriteComponent extends EntityComponent {

	public TransformComponent transform;
	public SizeComponent size;
	public Sprite sprite;
	public Color color = Color.white.cpy();
	public boolean post = false;
	//Sort of a hack to allow the player to be drawn in the 32x32 grid, by setting this value to 32.
	public int translationMultiplier = 1;
	public boolean visible = true;
	public Color outlineColor = new Color(0, 0, 0);
	public float outlineWidth = 0;
	
	public boolean tiled = false;
	public int tiledScale = 1;
	
	public SpriteComponent(Sprite sprite, TransformComponent transform, SizeComponent size)
	{
		this.sprite = sprite;
		this.transform = transform;
		this.size = size;
	}
	
	@Override
	public String getName() {
		return "sprite";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isVisible() {
		return !post && visible;
	}
	
	@Override
	public boolean isPostVisible() {
		return post && visible;
	}

	@Override
	public void draw(SpriteBatch batch) {
		com.badlogic.gdx.graphics.Color c = batch.getColor();
		if(outlineWidth > 0)
		{
			batch.setColor(outlineColor.r, outlineColor.g, outlineColor.b, outlineColor.a);
			sprite.render(batch, (int) (transform.x * translationMultiplier) - outlineWidth, (transform.y * translationMultiplier), size.width, size.height);
			sprite.render(batch, (int) (transform.x * translationMultiplier), (transform.y * translationMultiplier) - outlineWidth, size.width, size.height);
			sprite.render(batch, (int) (transform.x * translationMultiplier) + outlineWidth, (transform.y * translationMultiplier), size.width, size.height);
			sprite.render(batch, (int) (transform.x * translationMultiplier), (transform.y * translationMultiplier) + outlineWidth, size.width, size.height);
		
			//Corner
			sprite.render(batch, (int) (transform.x * translationMultiplier) - outlineWidth, (transform.y * translationMultiplier) - outlineWidth, size.width, size.height);
			sprite.render(batch, (int) (transform.x * translationMultiplier) + outlineWidth, (transform.y * translationMultiplier) - outlineWidth, size.width, size.height);
			sprite.render(batch, (int) (transform.x * translationMultiplier) + outlineWidth, (transform.y * translationMultiplier + outlineWidth), size.width, size.height);
			sprite.render(batch, (int) (transform.x * translationMultiplier) - outlineWidth, (transform.y * translationMultiplier) + outlineWidth, size.width, size.height);
		}
		batch.setColor(color.r, color.g, color.b, color.a);
		if(tiled)
		{
			TiledRenderer.drawTiledBox(batch, sprite, transform.x * translationMultiplier, transform.y * translationMultiplier, size.width, size.height, tiledScale);
		}
		else
			sprite.render(batch, transform.x * translationMultiplier, transform.y * translationMultiplier, size.width, size.height);
		batch.setColor(c);
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		draw(batch);
	}

}
