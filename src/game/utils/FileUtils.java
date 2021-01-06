package game.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.files.FileHandle;

public class FileUtils {
	
	
	@SafeVarargs
	public static void save(FileHandle handle, Object... arrays)
	{
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(handle.file());
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
	
	public static ObjectInputStream beginReading(FileHandle handle)
	{
		FileInputStream fin = null;
		ObjectInputStream ois = null;

		try {

			ois = new ObjectInputStream(handle.read());
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
	
}
