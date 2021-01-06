package game.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import game.Game;
import game.Input;
import game.entitycomponent.CounterComponent;
import game.entitycomponent.LabelComponent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.SpriteBoxComponent;
import game.entitycomponent.SpriteComponent;
import game.entitycomponent.TransformComponent;
import game.graphics.Font;
import game.graphics.Sprite;
import game.utils.Color;
import game.utils.Event;

public class MessageBox extends Entity {
	
	public static List<MessageBox> queue = new ArrayList<MessageBox>();
	
	public static MessageBox current()
	{
		return queue.size() > 0 ? queue.get(0) : null;
	}
	
	public static void addMessage(MessageBox message)
	{
		queue.add(message);
	}
	
	//Returns the last message box created here
	public static MessageBox addMessage(String... lines)
	{
		if(lines.length == 0) return null;
		MessageBox msgBoxReference = null;
		for(int i = 0; i <= lines.length / 3 || i == 0; i++)
		{
			int index = i * 3;
			int length = lines.length - index;
			if(length < 1) 
				continue;
			if(length > 3)
				length = 3;
			String[] message = new String[length];
			for(int k = 0; k < length; k++)
			{
				String line = lines[index + k];
				message[k] = line;
			}
			MessageBox box = new MessageBox(message);
			addMessage(box);
			msgBoxReference = box;
		}
		return msgBoxReference;
	}
	
	public static void moveQueue()
	{
		if(queue.size() > 0)
		{
			queue.remove(0);
		}
			
	}
	
	public boolean messageBoxActive()
	{
		return current() != null;
	}
	
	
	public Font font;
	public float textScale = 2.0f;
	private TransformComponent transform;
	private SizeComponent size;
	private CounterComponent counter;
	private LabelComponent label;
	private String fullString;
	private String[] choices;
	private ChoiceEvent choiceEvent;
	public Choices choicesEntity;
	public int preSelectedChoice = 0;
	
	private SpriteComponent doneIdentifier;
	private boolean doneAdded = false;
	private float doneIdentifierCounter;
	private int lastLength = 0;
	
	public Event messageDoneEvent = null;
	
	public MessageBox(String... lines) {
		font = Game.baseFont;
		fullString = "";
		for(int i = 0; i < lines.length; i++)
			fullString += lines[i] + (i < lines.length - 1 ? "\n" : "");
		transform = new TransformComponent();
		size = new SizeComponent();
		size.set((int) Math.min(Game.RES_WIDTH - 8, 300), (int) (font.lineHeight * textScale * 3));
		
		addComponent(transform);
		addComponent(size);
		addComponent(new SpriteBoxComponent(Sprite.getSprite("statborder"), transform, size));
		addComponent(label = new LabelComponent(transform, "", font));
		label.scale = textScale;
		label.xoffset = 2;
		label.yoffset = 8;
		addComponent(counter = new CounterComponent(0, fullString.length(), 0.5f));
		
		doneIdentifier = new SpriteComponent(Sprite.getSprite("arrow"), new TransformComponent(), new SizeComponent(16, 16));
		doneIdentifier.color = new Color(1, 0, 0);
		doneIdentifier.post = true;
	}
	
	public MessageBox setChoices(ChoiceEvent event, String... choices)
	{
		this.choices = choices;
		this.choiceEvent = event;
		return this;
	}
	
	public MessageBox setDoneEvent(Event doneEvent)
	{
		this.messageDoneEvent = doneEvent;
		return this;
	}
	
	@Override
	public void tick() {
		transform.set(Game.RES_WIDTH / 2 - size.width / 2, Game.RES_HEIGHT - size.height - 12);
		super.tick();
		if(counter.current <= fullString.length())
		{
			label.text = fullString.substring(0, (int) counter.current);
			if(label.text.length() != lastLength && label.text.charAt(label.text.length() - 1) != ' ')
			{
				Game.instance.getAudio().get("letter").play();
			}
			lastLength = label.text.length();
		}
		if(label.text.length() == fullString.length() && !doneAdded)
		{
			addComponent(doneIdentifier);
			doneAdded = true;
			if(queue.size() < 2)
			{
//				doneIdentifier.transform.y += doneIdentifier.size.height;
				doneIdentifier.size.height *= -1;
			}
			if(choices != null)
			{
				Game.instance.addEntity(choicesEntity = new Choices(new Choices.Event() {
					@Override
					public void onChoice(Choices choices, String choice) {
						if(choiceEvent != null)
							choiceEvent.onChoice(MessageBox.this, choice);
						kill();
					}
				}, choices));
				choicesEntity.choice = preSelectedChoice;
			}
				
		}
		if(doneAdded)
		{
			doneIdentifierCounter++;
			if(doneIdentifierCounter > 30)
			{
				doneIdentifier.visible = !doneIdentifier.visible;
				doneIdentifierCounter = 0;
			}
		}
		doneIdentifier.transform.centify(true, false, doneIdentifier.size);
		doneIdentifier.transform.y = Game.RES_HEIGHT - 21 + (doneIdentifier.size.height < 0 ? 21 : 0);
		if(Input.keyJustPressed(Keys.SPACE) && doneAdded && choices == null)
		{
			kill();
		}
	}
	
	private void kill()
	{
		dead = true;
		moveQueue();
		if(current() == null)
			Game.instance.getAudio().get("text_done").play();
		if(messageDoneEvent != null)
			messageDoneEvent.run();
	}
	
	public static interface ChoiceEvent
	{
		public void onChoice(MessageBox box, String choice);
	}
	
}
