package game.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageInverser {
	
	public static void inverseAndWriteImage(BufferedImage image, String name)
	{
		for(int x = 0; x < image.getWidth(); x++)
		{
			for(int y = 0; y < image.getHeight(); y++)
			{
				int rgba = image.getRGB(x, y);
		        int a = ((rgba >> 24) & 0xff);
		        int r = 255 - ((rgba >> 16) & 0xff);
		        int g = 255 - ((rgba >> 8) & 0xff);
		        int b = 255 - (rgba & 0xff);
		        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
			}
		}
		try {
			ImageIO.write(image, "PNG", new File("res/enemy_sprites/inv_" + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		File slime = new File("res/enemy_sprites/slime.png");
		File[] images = new File("res/enemy_sprites/").listFiles();
		for(int i = 0; i < images.length; i++)
		{
			File img = images[i];
			if(img.getPath().toLowerCase().endsWith(".png"))
			{
				String name = img.getName().split("\\.")[0];
				try {
					BufferedImage bufferedImg = ImageIO.read(img);
					inverseAndWriteImage(bufferedImg, name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
