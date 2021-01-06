package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;
import game.graphics.Font;
import game.graphics.FontRenderer;
import game.utils.Color;

public class LabelComponent extends EntityComponent {
	
	public TransformComponent transform;
	public Font font;
	public String text;
	public Color color = Color.white.cpy();
	public boolean drawPost = false;
	public boolean centered = false;
	public float scale = 1.0f;
	public float xoffset, yoffset;
	public float outlineWidth = 0;
	public Color outlineColor = Color.black.cpy();
	
	public LabelComponent(TransformComponent transform, String text, Font font)
	{
		this.transform = transform;
		this.text = text;
		this.font = font;
	}
	
	public LabelComponent setCentered()
	{
		centered = true;
		return this;
	}
	
	@Override
	public String getName() {
		return "label";
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
	public void draw(SpriteBatch batch) {
		com.badlogic.gdx.graphics.Color c = batch.getColor();
		float x = transform.getX() - (centered ? (font.getWidth(text) * scale) / 2 : 0) + xoffset;
		float y = transform.getY() - (font.getHeight(text) * scale) / 2 + yoffset;
		if(outlineWidth > 0)
		{
			batch.setColor(outlineColor.r, outlineColor.g, outlineColor.b, outlineColor.a);
			FontRenderer.draw(batch, font, text, x - outlineWidth, y, scale);
			FontRenderer.draw(batch, font, text, x, y - outlineWidth, scale);
			FontRenderer.draw(batch, font, text, x + outlineWidth, y, scale);
			FontRenderer.draw(batch, font, text, x, y + outlineWidth, scale);
			
		}
		batch.setColor(color.r, color.g, color.b, color.a);
		FontRenderer.draw(batch, font, text, x, y, scale);
		batch.setColor(c);
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		draw(batch);
	}
	
	@Override
	public boolean isVisible() {
		return !drawPost;
	}
	
	@Override
	public boolean isPostVisible() {
		return drawPost;
	}
	
}
