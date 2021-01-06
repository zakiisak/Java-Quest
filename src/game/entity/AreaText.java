package game.entity;

import game.Game;
import game.entitycomponent.CounterComponent;
import game.entitycomponent.CounterComponent.Event;
import game.entitycomponent.LabelComponent;
import game.entitycomponent.TransformComponent;
import game.scene.SceneBattle;
import game.scene.SceneGame;

public class AreaText extends Entity {
	
	private static AreaText current = null;
	
	public LabelComponent areaLabel;
	public LabelComponent levelLabel;
	public CounterComponent counter;
	public CounterComponent counter2;
	public boolean cancelable = true;
	
	public AreaText(String area, String level)
	{
		if(current != null)
		{
			if(current.cancelable == false)
			{
				dead = true;
				return;
			}
			current.dead = true;
			current = null;
		}
		areaLabel = new LabelComponent(new TransformComponent(Game.RES_WIDTH / 2, 30), area, Game.baseFont).setCentered();
		areaLabel.scale = 2;
		levelLabel = new LabelComponent(new TransformComponent(Game.RES_WIDTH / 2, 30 + Game.baseFont.getHeight(level) * 2 + 5), level, Game.baseFont).setCentered();
		areaLabel.outlineWidth = 1;
		levelLabel.outlineWidth = 1;
		addComponent(areaLabel);
		addComponent(levelLabel);
		addComponent(counter = new CounterComponent(0, 90, 1));
		addComponent(counter2 = new CounterComponent(0, 260, 1));
		counter2.endEvent = new Event() {
			@Override
			public void endReached(CounterComponent component) {
				counter.end = 180;
				counter.endEvent = new Event() {
					@Override
					public void endReached(CounterComponent component) {
						dead = true;
					}
				};
			}
		};
		current = this;
	}
	
	public AreaText setNotCancelable()
	{
		this.cancelable = false;
		return this;
	}
	
	@Override
	public void tick() {
		if(dead)
			return;
		if(Game.instance.getSceneManager().getActiveScene() instanceof SceneBattle)
			dead = true;
		super.tick();
		float alpha = (float) Math.sin(Math.toRadians(counter.current));
		areaLabel.color.a = alpha;
		areaLabel.outlineColor.a = alpha;
		levelLabel.color.a = alpha;
		levelLabel.outlineColor.a = alpha;
	}
	
}
