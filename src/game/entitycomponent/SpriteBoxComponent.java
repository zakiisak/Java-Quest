package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;
import game.graphics.Sprite;
import game.gui.TiledRenderer;
import game.utils.Color;

public class SpriteBoxComponent extends EntityComponent {

	public TransformComponent transform;
	public SizeComponent size;
	public Sprite sprite;
	public Color color = Color.white.cpy();
	public boolean post = false;
	//Sort of a hack to allow the player to be drawn in the 32x32 grid, by setting this value to 32.
	public int translationMultiplier = 1;
	
	public SpriteBoxComponent(Sprite sprite, TransformComponent transform, SizeComponent size)
	{
		this.sprite = sprite;
		this.transform = transform;
		this.size = size;
	}
	
	@Override
	public String getName() {
		return "spritebox";
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
		return !post;
	}
	
	@Override
	public boolean isPostVisible() {
		return post;
	}

	@Override
	public void draw(SpriteBatch batch) {
		com.badlogic.gdx.graphics.Color c = batch.getColor();
		batch.setColor(color.r, color.g, color.b, color.a);
		TiledRenderer.drawTiledBox(batch, sprite, transform.x * translationMultiplier, transform.y * translationMultiplier, size.width, size.height, 1);
		batch.setColor(c);
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		draw(batch);
	}

}
