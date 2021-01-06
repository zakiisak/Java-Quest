package game.main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import game.Game;
import game.entity.Items;
import game.entity.Items.Item;
import game.entity.Player;
import game.entity.Worlds;
import game.entitycomponent.StatComponent;
import game.gui.Component;
import game.utils.Numbers;

public class CreatorWindow extends JFrame {
	
	public CreatorWindow() {
		System.out.println(convertToCamelCase("hej med dig!"));
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 800);
		setLocationRelativeTo(null);
		addComponents();
		setVisible(true);
	}
	
	public static CreatorWindow window;
	
	public static void main(String[] args)
	{
		Game.CREATOR_WINDOW = true;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.initialBackgroundColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		config.samples = 0;
		config.width = 32;
		config.height = 32;
		config.x = -3123213;
		config.y = -3123213;
		config.resizable = false;
		config.title = "Java Quest";
//		config.addIcon("res/icon32.png", FileType.Local);
		game = new Game();
		game.config = config;
		new LwjglApplication(game, config);
	}
	
	private JButton generate;
	private static Game game;
	
	private JTextField name;
	private JTextField hp;
	private JTextField gold;
	
	private JTextField atk;
	private JTextField lvl;
	private JTextField strength;
	private JTextField vitality;
	private JTextField intelligence;
	private JPanel panel;
	
	
	private String lastLvl;
	
	private void addComponents()
	{
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, getWidth(), getHeight());
//		load = addButton("Load", 5, 5);
//		save = addButton("Save", 5 + load.getWidth() + 10, 5);
		generate = addButton("Generate", getWidth() - 160, 5);
		generate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				JOptionPane.showMessageDialog(null, "Game Saved!", "Game Saved", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		name = addField("Name", 60, 60, false);
		name.setText(System.getProperty("user.name"));
		
		hp = addField("HP", name.getX() + name.getWidth() + 30, 60, false); hp.setText("10");
		//gold = addField("Gold (min-max)", name.getX() + name.getWidth() + 30, 60, false); gold.setText("3-12");
		
		atk = addField("Attack", 300, 60, false); atk.setText("1");
		lvl = addField("Lvl", 300, 60 + 40, false); lvl.setText("1");
		gold = addField("Gold", 300, 60 + 80, false); gold.setText("1");
		
		
		StatComponent stats = game.player.stats;
		hp.setText(stats.maxhp.toString());
		atk.setText(stats.atk_min.toString());
		lvl.setText(stats.lvl.toString());
		gold.setText(stats.gold.toString());
		name.setText(game.player.name);
		lastLvl = lvl.getText();
		
		for(int key : game.player.possessedItems.keySet())
		{
			long amount = game.player.possessedItems.get(key);
			itemAmounts.put(key, "" + amount);
		}
		
		new javax.swing.Timer(100, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(lvl.getText().equals(lastLvl) == false)
				{
					System.out.println("not equal");
					try
					{
						System.out.println("level: " + lvl.getText());
						game.player.xpMemory = BigInteger.ZERO;
						game.player.stats.set(new StatComponent());
						StatComponent stats = game.player.stats;
						stats.hp = new BigInteger("10");
						stats.lvl = BigInteger.ONE;
						stats.maxhp = new BigInteger("10");
						stats.xplimit = new BigInteger("10");
						stats.atk_min = new BigInteger("2");
						stats.atk_max = new BigInteger("3");
						stats.xp = new BigInteger(lvl.getText()).multiply(Numbers.TEN).subtract(Numbers.FIVE).pow(2).subtract(new BigInteger("25")).divide(new BigInteger("20"));
						game.player.checkLevelUp();
						
						atk.setText(stats.atk_min.toString());
						hp.setText(stats.maxhp.toString());
						System.out.println("mugger");
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				
				}
				lastLvl = lvl.getText();
			}
		}).start();
		
		items();
		
		add(panel);
		
	}
	
	public void items()
	{
		List<Item> items = Items.availableItems;
		System.out.println("Item Size: " + items.size());
		for(int i = 0; i < items.size(); i++)
		{
			int x = 30 + (i % 4) * 160;
			int y = 200 + (i / 4) * 40;
			addItem(items.get(i).id, x, y);
		}
	}
	
	private void save()
	{
		Player player = game.player;
		player.name = name.getText();
		game.player.xpMemory = BigInteger.ZERO;
		game.player.stats.set(new StatComponent());
		StatComponent stats = game.player.stats;
		stats.hp = new BigInteger("10");
		stats.lvl = BigInteger.ONE;
		stats.maxhp = new BigInteger("10");
		stats.xplimit = new BigInteger("10");
		stats.atk_min = new BigInteger("2");
		stats.atk_max = new BigInteger("3");
		player.stats.xp = new BigInteger(lvl.getText()).multiply(Numbers.TEN).subtract(Numbers.FIVE).pow(2).subtract(new BigInteger("25")).divide(new BigInteger("20"));
		player.checkLevelUp();
		player.stats.maxhp = new BigInteger(hp.getText());
		player.stats.hp = new BigInteger(hp.getText());
		player.stats.atk_min = new BigInteger(atk.getText());
		player.stats.atk_max = new BigInteger(atk.getText()).add(BigInteger.ONE);
		player.addGold(new BigInteger(gold.getText()));
		
		System.out.println(player.stats);
		
		for(int key : itemAmounts.keySet())
		{
			String amount = itemAmounts.get(key);
			if(amount != null)
			{
				System.out.println("adding item amount: " + key + " " + itemAmounts.get(key));
				player.possessedItems.put(key, Long.parseLong(itemAmounts.get(key)));
			}
		}
		
		game.saveGame();
	}
	/*
	private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        List dirs = new ArrayList();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList classes = new ArrayList();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }
	*/
	
	private JButton addButton(String text, int x, int y)
	{
		JButton button = new JButton();
		button.setText(text);
		button.setBounds(x, y, text.length() * 18, 22);
		panel.add(button);
		return button;
	}
	
	private int getTextWidth(String text, Font font)
	{
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
		return textwidth;
	}
	
	private JLabel addLabel(String label, int x, int y)
	{
		JLabel jLabel = new JLabel();
		jLabel.setText(label);
		jLabel.setBounds(x + 2, y, label.length() * 18, 20);
		panel.add(jLabel);
		return jLabel;
	}
	
	
	private JTextField addField(String label, int x, int y, boolean horizontalLabel)
	{
		JTextField field = new JTextField();
		field.setBounds(x, y, 100, 22);
		JLabel jLabel = new JLabel();
		jLabel.setText(label);
		if(horizontalLabel)
			jLabel.setBounds(x - getTextWidth(label, jLabel.getFont()) - 6, y, label.length() * 18, 20);
		else
			jLabel.setBounds(x + 2, y - 18, label.length() * 18, 20);
		panel.add(field);
		panel.add(jLabel);
		return field;
	}
	
	private JTextArea addArea(String label, int x, int y)
	{
		JTextArea field = new JTextArea();
		field.setBounds(x, y, 225, 100);
		JLabel jLabel = new JLabel();
		jLabel.setText(label);
		jLabel.setBounds(x + 2, y - 20, label.length() * 18, 20);
		JScrollPane pane = new JScrollPane(field);
		pane.setBounds(field.getX(), field.getY(), field.getWidth(), field.getHeight());
		panel.add(pane);
		panel.add(jLabel);
		return field;
	}
	
	private JComboBox addDropdown(String label, int x, int y, boolean horizontalLabel, String... options)
	{
		JComboBox dd = new JComboBox(new DefaultComboBoxModel(options));
		dd.setBounds(x, y, 100, 22);
		JLabel jLabel = new JLabel();
		jLabel.setText(label);
		if(horizontalLabel)
			jLabel.setBounds(x - getTextWidth(label, jLabel.getFont()) - 6, y, label.length() * 18, 20);
		else
			jLabel.setBounds(x + 2, y - 18, label.length() * 18, 20);
		panel.add(dd);
		panel.add(jLabel);
		return dd;
	}
	
	private String convertToCamelCase(String str)
	{
		StringBuilder newString = new StringBuilder();
		newString.append(Character.toUpperCase(str.charAt(0)));
		int offset = 0;
		boolean makeUpper = false;
		for(int i = 0; i < str.length(); i++)
		{
			char nextChar = i < str.length() - 1 ? str.charAt(i + 1) : '\0';
			if(nextChar == ' ')
			{
				makeUpper = true;
			}
			else if(nextChar != '\0')
			{
				if(makeUpper)
					nextChar = Character.toUpperCase(nextChar);
				newString.append(nextChar);
				makeUpper = false;
			}
		}
		return newString.toString();
	}
	
	private static Map<Integer, String> itemAmounts = new HashMap<Integer, String>();
	
	private void addItem(final int itemId, int x, int y)
	{
		final String name = Items.getItemName(itemId);
		final JButton button = addButton("x" + (itemAmounts.get(itemId) != null ? itemAmounts.get(itemId) : "0") + " " + name, x, y);
		button.setSize(150, 20);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog("Enter amount of " + name + " you want to have");
				if(input == null)
					return;
				if(input.replaceAll(" ", "").isEmpty())
					return;
				
				//Ensures that the input is passable
				BigInteger amount = new BigInteger(input);
				itemAmounts.put(itemId, input);
				button.setText("x" + amount + " " + name);
			}
		});
	}
	
}
