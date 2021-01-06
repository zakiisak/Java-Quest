package game.graphics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shaders {
	
	public static ShaderProgram invertedShader;
	public static ShaderProgram defaultShader;
	public static ShaderProgram grayShader;
	public static ShaderProgram titleShader;
	
	public static Map<String, ShaderProgram> shaders = new HashMap<String, ShaderProgram>();
	
	public static void init(SpriteBatch batch)
	{
		defaultShader = batch.getShader();
		addShader(defaultShader, "default");
		invertedShader();
		grayShader();
		titleShader();
	}
	
	public static ShaderProgram get(String shader)
	{
		return shaders.get(shader);
	}
	
	public static void addShader(ShaderProgram shader, String name)
	{
		shaders.put(name, shader);
	}
	
	private static void titleShader()
	{
		String vertexShader = //
				  "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
			String fragmentShader = //
				  "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "uniform float brightX;\n" //
				+ "uniform float gray;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  float dist = v_texCoords.x - brightX; \n" //
				+ "  if(dist < 0.0) { dist *= -1.0; }\n" //
				+ "  float brightness = 0.1 / dist; \n" //
				+ "  vec4 tc = texture2D(u_texture, v_texCoords) * v_color;\n" //
//				+ "  if(dist > 0.5) { \n" //
//				+ "      brightness = 0.0; } \n" //
				+ "  float light = tc.x * 0.21 + tc.y * 0.72 + tc.z * 0.07; \n" //
				+ "  if(gray > 0.5) { gl_FragColor = vec4(light * brightness, light * brightness, light * brightness, tc.w); } \n" //
				+ "  else { gl_FragColor = vec4(tc.x * brightness, tc.y * brightness, tc.z * brightness, tc.w); } \n" //
				+ "}";
			titleShader = new ShaderProgram(vertexShader, fragmentShader);
			if (titleShader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + titleShader.getLog());
			addShader(titleShader, "title");
	}
	
	private static void grayShader()
	{
		String vertexShader = //
				  "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
			String fragmentShader = //
				  "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  vec4 tc = texture2D(u_texture, v_texCoords) * v_color;\n" //
				+ "  float light = tc.x * 0.21 + tc.y * 0.72 + tc.z * 0.07; \n" //
				+ "  gl_FragColor = vec4(light, light, light, tc.w); \n" //
				+ "}";
			grayShader = new ShaderProgram(vertexShader, fragmentShader);
			if (grayShader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + grayShader.getLog());
			addShader(grayShader, "gray");
	}
	
	private static void invertedShader()
	{
		String vertexShader = //
			  "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "uniform mat4 u_projTrans;\n" //
			+ "varying vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "\n" //
			+ "void main()\n" //
			+ "{\n" //
			+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
			+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "}\n";
		String fragmentShader = //
			  "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" //
			+ "varying LOWP vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "uniform sampler2D u_texture;\n" //
			+ "void main()\n"//
			+ "{\n" //
			+ "  vec4 tc = texture2D(u_texture, v_texCoords);\n" //
			+ "  vec3 inverted = vec3((1.0 - tc.x), (1.0 - tc.y), (1.0 - tc.z)); \n" //
			+ "  gl_FragColor = v_color * vec4(inverted, tc.w); \n" //
			+ "}";
		invertedShader = new ShaderProgram(vertexShader, fragmentShader);
		if (invertedShader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + invertedShader.getLog());
		addShader(invertedShader, "inverted");
	}
	
	public static void dispose()
	{
		for(ShaderProgram program : shaders.values())
		{
			program.dispose();
		}
	}
	
}
