package game.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MonsterTable {
	
	private static JFrame frame;
	private static JTable table;
	
	public static void main(String[] args)
	{
		/*
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		*/
		
		frame = new JFrame();
		frame.setLayout(new GridLayout(1, 0));
		frame.setSize(700, 400);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		table = new JTable(200, 7);
		table.getColumnModel().getColumn(0).setHeaderValue("Name");
		table.getColumnModel().getColumn(1).setHeaderValue("Hp");
		table.getColumnModel().getColumn(2).setHeaderValue("Atk");
		table.getColumnModel().getColumn(3).setHeaderValue("Gold");
		table.getColumnModel().getColumn(4).setHeaderValue("Xp");
		table.getColumnModel().getColumn(5).setHeaderValue("Zone");
		table.getColumnModel().getColumn(6).setHeaderValue("Sprite");
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));
        table.setFillsViewportHeight(true);
        
		JScrollPane scrollPane = new JScrollPane(table);
		frame.pack();
		frame.setSize(700, 400);
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Would you like to save?");
				if(result == JOptionPane.YES_OPTION)
				{
					if(!save())
					{
						JOptionPane.showMessageDialog(null, "An error occurred while saving!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						System.exit(0);
					}
				}
				else if(result == JOptionPane.CANCEL_OPTION)
				{
					return;
				}
				else if(result == JOptionPane.NO_OPTION)
				{
					System.exit(0);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		System.out.println(table.getModel());
		frame.add(scrollPane);
		load();
		scrollPane.repaint();
	}
	
	private static void load()
	{
		File[] files = new File("res/enemies/").listFiles();
		String[] stats = new String[files.length * 7];
		int index = 0;
		for(int i = 0; i < files.length; i++)
		{
			File f = files[i];
			ObjectInputStream stream = beginReading(f);
			try {
				String name = (String )stream.readObject();
				String hp = (String )stream.readObject();
				String atk = (String )stream.readObject();
				String gold = (String )stream.readObject();
				String xp = (String )stream.readObject();
				String zone = (String )stream.readObject();
				String spriteField = (String )stream.readObject();
				
				stats[index + 0] = name;
				stats[index + 1] = hp;
				stats[index + 2] = atk;
				stats[index + 3] = gold;
				stats[index + 4] = xp;
				stats[index + 5] = zone;
				stats[index + 6] = spriteField;
				index += 7;
				
				table.getModel().setValueAt(name, i, 0);
				table.getModel().setValueAt(hp, i, 1);
				table.getModel().setValueAt(atk, i, 2);
				table.getModel().setValueAt(gold, i, 3);
				table.getModel().setValueAt(xp, i, 4);
				table.getModel().setValueAt(zone, i, 5);
				table.getModel().setValueAt(spriteField, i, 6);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			endReading(stream);
		}
		
		/*
		List<Integer> indecies = new ArrayList<Integer>();
		
		int lowest = 100000; //Just a high number
		while(indecies.size() < stats.length / 7)
		{
			for(int i = 0; i < stats.length; i += 7)
			{
				if(indecies.contains(i)) continue;
				String zone = stats[i + 5];
				int lowestZone = getLowest(zone);
				if(indecies.size() > 0)
				{
					for(int k = indecies.size() - 1; k >= 0; k--)
					{
						int zoneNumber = getLowest(stats[indecies.get(k) + 5]);
						if(zoneNumber < lowestZone)
						{
							indecies.add(k + 1, lowestZone);
							continue;
						}
						if(lowestZone < zoneNumber)
						{
							boolean added = false;
							for(int j = k; j >= 0; j--)
							{
								int zn = getLowest(stats[indecies.get(j) + 5]);
								if(lowestZone < zn)
									continue;
								if(zn >= lowestZone)
								{
									indecies.add(j + 1, lowestZone);
									added = true;
									break;
								}
							}
							if(added == false)
								indecies.add(0, lowestZone);
						}
					}
				}
				else indecies.add(i);
				
			}
		}
		for(int ii = 0; ii < indecies.size(); ii++)
		{
			int i = indecies.get(ii);
			table.getModel().setValueAt(stats[i + 0], i, 0);
			table.getModel().setValueAt(stats[i + 1], i, 1);
			table.getModel().setValueAt(stats[i + 2], i, 2);
			table.getModel().setValueAt(stats[i + 3], i, 3);
			table.getModel().setValueAt(stats[i + 4], i, 4);
			table.getModel().setValueAt(stats[i + 5], i, 5);
			table.getModel().setValueAt(stats[i + 6], i, 6);
		}
		*/
	}
	
	private static int min(int a, int b)
	{
		return a < b ? a : (b < a ? b : a);
	}
	
	private static int getLowest(String zone)
	{
		String[] splitter = zone.split(",");
		int lowest = 100000;
		for(int i = 0; i < splitter.length; i++)
		{
			String section = splitter[i];
			if(section.contains(";"))
			{
				String[] toSplitter = section.split(";");
				int from = Integer.parseInt(toSplitter[0]);
				int to = Integer.parseInt(toSplitter[1]);
				if(from < lowest || to < lowest)
					lowest = min(from, to);
			}
			else
			{
				int singularZone = Integer.parseInt(section);
				if(singularZone < lowest)
					lowest = singularZone;
			}
		}
		return lowest;
	}
	
	private static boolean save()
	{
		System.out.println("table value: " + table.getModel().getValueAt(0, 0));
		
		int length = 0;
		for(int i = 0; i < table.getRowCount(); i++)
		{
			Object value = table.getModel().getValueAt(i, 0);
			System.out.println("value: " + value);
			boolean empty = false;
			if(value == null)
				empty = true;
			else if(value.toString().replaceAll(" ", "").length() < 1)
				empty = true;
			if(empty)
			{
				System.out.println("i is: " + i);
				length = i;
				break;
			}
		}
		for(int r = 0; r < length; r++)
		{
			ObjectOutputStream stream = beginSave(new File("res/enemies/" + table.getModel().getValueAt(r, 6) + ".enf"));
			if(stream == null) continue;
			for(int c = 0; c < 7; c++)
			{
				try {
					stream.writeObject(table.getModel().getValueAt(r, c));
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			endSaving(stream);
		}
		return true;
	}
	
	public static ObjectInputStream beginReading(File handle)
	{
		FileInputStream fin = null;
		ObjectInputStream ois = null;

		try {

			fin = new FileInputStream(handle);
			ois = new ObjectInputStream(fin);
			return ois;
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	
	public static void endReading(ObjectInputStream stream)
	{
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ObjectOutputStream beginSave(File file)
	{
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {
			fout = new FileOutputStream(file);
			oos = new ObjectOutputStream(fout);
			return oos;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void endSaving(ObjectOutputStream stream)
	{
		try
		{
			stream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@SafeVarargs
	public static void save(File file, Object... arrays)
	{
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(file);
			oos = new ObjectOutputStream(fout);
			for(int i = 0; i < arrays.length; i++)
				oos.writeObject(arrays[i]);

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	private static class EnemyData
	{
		public String name;
		public String hp;
		public String atk;
		public String gold;
		public String xp;
		public String sprite;
		public String zone;
	}
	
}
