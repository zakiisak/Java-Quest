package game.utils;

import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import game.Game;

public class ImageUtils {
	
	//This must take place after all rendering that should happen, has happened.
	public static Pixmap takeScreenshot()
	{
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Game.RES_WIDTH, Game.RES_HEIGHT, true);
		
		Pixmap pixmap = new Pixmap(Game.RES_WIDTH, Game.RES_HEIGHT, Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		return pixmap;
	}
	
	public static int[] getFrameBufferPixels (int x, int y, int w, int h, boolean flipY) {
		Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
		final IntBuffer pixels = BufferUtils.newIntBuffer(w * h * 4);
		Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_INT, pixels);
		final int numBytes = w * h * 4;
		int[] lines = new int[numBytes];
		if (flipY) {
			final int numBytesPerLine = w * 4;
			for (int i = 0; i < h; i++) {
				pixels.position((h - i - 1) * numBytesPerLine);
				pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
			}
		} else {
			pixels.clear();
			pixels.get(lines);
		}
		return lines;

	}
	
	public static void savePixmap(String path, Pixmap pixmap)
	{
		PixmapIO.writePNG(Gdx.files.local(path), pixmap);
		pixmap.dispose();
	}
	
	private static int clamp(int src, int min, int max)
	{
		if(src < min)
			return min;
		if(src > max)
			return max;
		return src;
	}
	
	public static void blur( int[] in, int[] out, int width, int height, int radius ) {
        int widthMinus1 = width-1;
        int tableSize = 2*radius+1;
        int divide[] = new int[256*tableSize];

        for ( int i = 0; i < 256*tableSize; i++ )
            divide[i] = i/tableSize;

        int inIndex = 0;
        
        for ( int y = 0; y < height; y++ ) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for ( int i = -radius; i <= radius; i++ ) {
                int rgb = in[inIndex + clamp(i, 0, width-1)];
                tr += (rgb >> 24) & 0xff;
                tg += (rgb >> 16) & 0xff;
                tb += (rgb >> 8) & 0xff;
                ta += rgb & 0xff;
            }

            for ( int x = 0; x < width; x++ ) {
                out[ outIndex ] = (divide[tr] << 24) | (divide[tg] << 16) | (divide[tb] << 8) | divide[ta];

                int i1 = x+radius+1;
                if ( i1 > widthMinus1 )
                    i1 = widthMinus1;
                int i2 = x-radius;
                if ( i2 < 0 )
                    i2 = 0;
                int rgb1 = in[inIndex+i1];
                int rgb2 = in[inIndex+i2];
                
                tr += ((rgb1 >> 24) & 0xff)-((rgb2 >> 24) & 0xff);
                tg += ((rgb1 & 0xff0000)-(rgb2 & 0xff0000)) >> 16;
                tb += ((rgb1 & 0xff00)-(rgb2 & 0xff00)) >> 8;
                ta += (rgb1 & 0xff)-(rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }
	
	public static void blur( byte[] in, byte[] out, int width, int height, int radius ) {
        int widthMinus1 = width-1;
        int tableSize = 2*radius+1;
        byte divide[] = new byte[256*tableSize];

        for ( int i = 0; i < 256*tableSize; i++ )
            divide[i] = (byte) (i/tableSize);

        int inIndex = 0;
        
        for ( int y = 0; y < height; y++ ) {
            int outIndex = y;
            byte ta = 0, tr = 0, tg = 0, tb = 0;

            for ( int i = -radius; i <= radius; i++ ) {
                int rgb = in[inIndex + clamp(i, 0, width-1)];
                tr += (rgb >> 24) & 0xff;
                tg += (rgb >> 16) & 0xff;
                tb += (rgb >> 8) & 0xff;
                ta += rgb & 0xff;
            }

            for ( int x = 0; x < width; x++ ) {
            	out[outIndex] = divide[tr];
            	out[outIndex + 1] = divide[tg];
            	out[outIndex + 2] = divide[tb];
            	out[outIndex + 3] = divide[ta];

                int i1 = x+radius+1;
                if ( i1 > widthMinus1 )
                    i1 = widthMinus1;
                int i2 = x-radius;
                if ( i2 < 0 )
                    i2 = 0;
                int rgb1 = in[inIndex+i1];
                int rgb2 = in[inIndex+i2];
                
                tr += ((rgb1 >> 24) & 0xff)-((rgb2 >> 24) & 0xff);
                tg += ((rgb1 & 0xff0000)-(rgb2 & 0xff0000)) >> 16;
                tb += ((rgb1 & 0xff00)-(rgb2 & 0xff00)) >> 8;
                ta += (rgb1 & 0xff)-(rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }
	
}
