package game.utils;

import java.io.Serializable;

public class Color implements Serializable {
	
	public static final Color white = new Color(1, 1, 1);
	public static final Color black = new Color(0, 0, 0);
	public static final Color red = new Color(1, 0, 0);
	public static final Color green = new Color(0, 1, 0);
	public static final Color blue = new Color(0, 0, 1);
	
	public float r, g, b, a;
	
	public Color()
	{
		
	}
	
	public Color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(float r, float g, float b)
	{
		this(r, g, b, 1);
	}

	public Color(Color color)
	{
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
	}
	
	public Color(int color)
	{
		set(color);
	}
	
	public void set(int rgba)
	{
        r = (float) ((rgba >> 24) & 0xff) / 255f;
        g = (float) ((rgba >> 16) & 0xff) / 255f;
        b = (float) ((rgba >> 8) & 0xff) / 255f;
        a = (float) (rgba & 0xff) / 255f;
	}
	
	public void set(Color color)
	{
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
	}
	
	public Color cpy()
	{
		return new Color(this);
	}
	
	public static Color getSineRainbow(float degrees)
	{
		float r = 0.5f;
		float g = 0.5f;
		float b = 0.5f;
		r += (float) Math.sin(Math.toRadians(degrees)) * 0.5f;
		g += (float) Math.sin(Math.toRadians(degrees + 120)) * 0.5f;
		b += (float) Math.sin(Math.toRadians(degrees + 240)) * 0.5f;
		return new Color(r, g, b);
	}
	
	public static Color getHSB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
            case 0:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (t * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 1:
                r = (int) (q * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 2:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (t * 255.0f + 0.5f);
                break;
            case 3:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (q * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 4:
                r = (int) (t * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 5:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (q * 255.0f + 0.5f);
                break;
            }
        }
        return new Color(r, g, b);
    }
	
}
