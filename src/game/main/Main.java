package game.main;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import game.Game;
import game.entitycomponent.StatComponent;
import game.utils.FileUtils;
import game.utils.MathUtils;
import game.utils.Numbers;

public class Main {
	
	
	public static void resizeAndSave(FileHandle handle, int srcWidth, int srcHeight, short[] src_backgroundids, byte[] src_backgroundsprites, short[] src_foregroundids, byte[] src_foregroundsprites, int newWidth, int newHeight)
	{
		System.out.println("srcWidth: " + srcWidth);
		System.out.println("srcHeight: " + srcHeight);
		
		short[] backgroundIds = new short[newWidth * newHeight];
		short[] foregroundIds = new short[newWidth * newHeight];
		byte[] backgroundSprites = new byte[newWidth * newHeight];
		byte[] foregroundSprites = new byte[newWidth * newHeight];
		
		fill(src_backgroundids, backgroundIds, srcWidth, srcHeight, newWidth, newHeight, true);
		fill(src_foregroundids, foregroundIds, srcWidth, srcHeight, newWidth, newHeight, true);
		fill(src_backgroundsprites, backgroundSprites, srcWidth, srcHeight, newWidth, newHeight, false);
		fill(src_backgroundsprites, foregroundSprites, srcWidth, srcHeight, newWidth, newHeight, false);
		
		FileUtils.save(handle, newWidth, newHeight, backgroundIds, backgroundSprites, foregroundIds, foregroundSprites);
	}
	
	public static void fill(short[] src, short[] dest, int origWidth, int origHeight, int destWidth, int destHeight, boolean flip)
	{
		for(int x = 0; x < origWidth; x++)
		{
			for(int y = 0; y < origHeight; y++)
			{
				dest[x + y * destWidth] = src[x + (flip ? (origHeight - 1 - y) : y) * origWidth];
			}
		}
		for(int x = origWidth; x < destWidth; x++)
		{
			for(int y = origHeight; y < destHeight; y++)
			{
				dest[x + y * destWidth] = src[1 + 1 * origWidth];
			}
		}
			
	}
	
	private static void fill(byte[] src, byte[] dest, int origWidth, int origHeight, int destWidth, int destHeight, boolean flip)
	{
		for(int x = 0; x < origWidth; x++)
		{
			for(int y = 0; y < origHeight; y++)
			{
				dest[x + y * destWidth] = src[x + (flip ? (origHeight - 1 - y) : y) * origWidth];
			}
		}
		
		for(int x = origWidth; x < destWidth; x++)
		{
			for(int y = origHeight; y < destHeight; y++)
			{
				dest[x + y * destWidth] = src[0 + 0 * origWidth];
			}
		}
	}
	
	public static double levels(double xp)
	{
		return (5+Math.sqrt(5*(5+4 * xp))) / 10;
	}
	
	
	public static void main(String[] args) throws URISyntaxException, IOException
	{
		
		
		/*
		BigInteger bigint = new BigInteger("1");
		BigInteger bigint2 = new BigInteger("2");
		BigDecimal dec1 = new BigDecimal("2.3718947329437472384378237492374897234374892374892374923723742374237834723947239477234792742374738472397347238478924789");
		BigDecimal dec2 = new BigDecimal("2.3718947329437472384378237492374897234374892374892374923723742374237834723947239477234792742374738472397347238478924789389573459734857348957398573485349");
		BigInteger sum = bigint.add(bigint2);
		BigDecimal dec3 = dec1.multiply(dec2);
		System.out.println("int sum: " + sum);
		System.out.println(dec1 + " * " + dec2 + " = " + dec3);
		*/
		
//		System.out.println(new BigDecimal("1").divide(new BigDecimal("3"), 3, RoundingMode.UP));
//		System.out.println(levels(10));
//		System.out.println(MathUtils.bigIntSqRootFloor(new BigInteger("10000000000000000000")));
		StatComponent stats = new StatComponent();
//		stats.lvl = Numbers.FIVE;
		stats.xp = new BigInteger("10");
		BigInteger levels = Numbers.FIVE.add(MathUtils.bigIntSqRootFloor(Numbers.FIVE.multiply(stats.xp.add(Numbers.ONE).multiply(Numbers.FOUR)).add(Numbers.FIVE))).divide(Numbers.TEN);
		BigInteger xp = levels.multiply(Numbers.TEN).subtract(Numbers.FIVE).pow(2).subtract(new BigInteger("25")).divide(new BigInteger("20"));
		System.out.println("new level: " + levels);
		System.out.println("xp: " + xp);
//		System.exit(0);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.initialBackgroundColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		config.samples = 0;
		config.width = Game.RES_WIDTH;
		config.height = Game.RES_HEIGHT;
		config.resizable = false;
		config.title = "Java Quest";
		config.addIcon("res/icon32.png", FileType.Local);
		Game game = new Game();
		game.config = config;
		new LwjglApplication(game, config);
	}
	
}
