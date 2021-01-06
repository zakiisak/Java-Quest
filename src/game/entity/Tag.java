package game.entity;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Tag implements Serializable {
	
	protected Map<String, Object> tags = new HashMap<String, Object>();
	
	public boolean hasKey(String key)
	{
		return tags.containsKey(key);
	}
	
	public void removeKey(String key)
	{
		tags.remove(key);
	}
	
	public Set<String> getKeySet()
	{
		return tags.keySet();
	}
	
	public Set<Entry<String, Object>> getEntrySet()
	{
		return tags.entrySet();
	}
	
	public Collection<Object> getValues()
	{
		return tags.values();
	}
	
	public Map<String, Object> getMap()
	{
		return tags;
	}
	
	@Override
	public String toString() {
		String str = super.toString() + ":\n[\n";
		for(String key : getKeySet())
		{
			Object value = getValue(key);
			str += "key=" + key + ",val=" + value + "\n";
		}
		str += "\n]";
		return str;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value assigned to the given key, or null if it doesn't exist.
	 */
	public Object getValue(String key)
	{
		return tags.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns 0
	 */
	public int getInteger(String key)
	{
		Object obj = tags.get(key);
		if(obj instanceof Integer)
		{
			return (Integer) obj;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns 0
	 */
	public float getFloat(String key)
	{
		Object obj = tags.get(key);
		if(obj instanceof Float)
		{
			return (Float) obj;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns 0
	 */
	public double getDouble(String key)
	{
		Object obj = tags.get(key);
		if(obj instanceof Double)
		{
			return (Double) obj;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns 0
	 */
	public long getLong(String key)
	{
		Object obj = tags.get(key);
		if(obj instanceof Long)
		{
			return (Long) obj;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns 0
	 */
	public short getShort(String key)
	{
		Object obj = tags.get(key);
		if(obj instanceof Short)
		{
			return (Short) obj;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns 0
	 */
	public char getChar(String key)
	{
		Object obj = tags.get(key);
		if(obj instanceof Character)
		{
			return (Character) obj;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns false
	 */
	public boolean getBoolean(String key)
	{
		Object obj = tags.get(key);
		if(obj instanceof Boolean)
		{
			return (Boolean) obj;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param key
	 * @return the value assigned to the given key, or the given defaultValue if it doesn't exist.
	 */
	public Object getValue(String key, Object defaultValue)
	{
		if(hasKey(key))
			return tags.get(key);
		else return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns the given defaultValue
	 */
	public int getInteger(String key, int defaultValue)
	{
		Object obj = tags.get(key);
		if(obj instanceof Integer)
		{
			return (Integer) obj;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns the given defaultValue
	 */
	public float getFloat(String key, float defaultValue)
	{
		Object obj = tags.get(key);
		if(obj instanceof Float)
		{
			return (Float) obj;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns the given defaultValue
	 */
	public double getDouble(String key, double defaultValue)
	{
		Object obj = tags.get(key);
		if(obj instanceof Double)
		{
			return (Double) obj;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns the given defaultValue
	 */
	public long getLong(String key, long defaultValue)
	{
		Object obj = tags.get(key);
		if(obj instanceof Long)
		{
			return (Long) obj;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns the given defaultValue
	 */
	public short getShort(String key, short defaultValue)
	{
		Object obj = tags.get(key);
		if(obj instanceof Short)
		{
			return (Short) obj;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns the given defaultValue
	 */
	public char getChar(String key, char defaultValue)
	{
		Object obj = tags.get(key);
		if(obj instanceof Character)
		{
			return (Character) obj;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns the given defaultValue
	 */
	public boolean getBoolean(String key, boolean defaultValue)
	{
		Object obj = tags.get(key);
		if(obj instanceof Boolean)
		{
			return (Boolean) obj;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns the given defaultValue
	 */
	public String getString(String key, String defaultValue)
	{
		Object obj = tags.get(key);
		if(obj instanceof String)
		{
			return (String) obj;
		}
		return defaultValue;
	}
	
	
	/**
	 * 
	 * @param key
	 * @return the value of the key if it exists, otherwise returns null
	 */
	public String getString(String key)
	{
		Object obj = tags.get(key);
		if(obj instanceof String)
		{
			return (String) obj;
		}
		return null;
	}
	
	public Object getObject(String key)
	{
		return tags.get(key);
	}
	
	public Object getObject(String key, Object defaultValue)
	{
		Object value = tags.get(key);
		if(value != null)
		{
			return value;
		}
		return defaultValue;
	}
	
	/**
	 * Assigns a value to the given key.
	 * @param key
	 * @param value
	 */
	public void setInteger(String key, int value)
	{
		tags.put(key, value);
	}
	
	/**
	 * Assigns a value to the given key.
	 * @param key
	 * @param value
	 */
	public void setFloat(String key, float value)
	{
		tags.put(key, value);
	}
	
	/**
	 * Assigns a value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDouble(String key, double value)
	{
		tags.put(key, value);
	}
	
	/**
	 * Assigns a value to the given key.
	 * @param key
	 * @param value
	 */
	public void setLong(String key, long value)
	{
		tags.put(key, value);
	}
	
	/**
	 * Assigns a value to the given key.
	 * @param key
	 * @param value
	 */
	public void setShort(String key, short value)
	{
		tags.put(key, value);
	}
	
	/**
	 * Assigns a value to the given key.
	 * @param key
	 * @param value
	 */
	public void setChar(String key, char value)
	{
		tags.put(key, value);
	}
	
	/**
	 * Assigns a value to the given key.
	 * @param key
	 * @param value
	 */
	public void setString(String key, String value)
	{
		tags.put(key, value);
	}
	
	public void setObject(String key, Object obj)
	{
		tags.put(key, obj);
	}
	
	/**
	 * Assigns a value to the given key.
	 * @param key
	 * @param value
	 */
	public void setBoolean(String key, boolean value)
	{
		tags.put(key, value);
	}
	
	/**
	 * Assigns a default value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDefaultInteger(String key, int value)
	{
		if(hasKey(key)) return;
		tags.put(key, value);
	}
	
	/**
	 * Assigns a default value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDefaultFloat(String key, float value)
	{
		if(hasKey(key)) return;
		tags.put(key, value);
	}
	
	/**
	 * Assigns a default value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDefaultDouble(String key, double value)
	{
		if(hasKey(key)) return;
		tags.put(key, value);
	}
	
	/**
	 * Assigns a default value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDefaultLong(String key, long value)
	{
		if(hasKey(key)) return;
		tags.put(key, value);
	}
	
	/**
	 * Assigns a default value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDefaultShort(String key, short value)
	{
		if(hasKey(key)) return;
		tags.put(key, value);
	}
	
	/**
	 * Assigns a default value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDefaultChar(String key, char value)
	{
		if(hasKey(key)) return;
		tags.put(key, value);
	}
	
	/**
	 * Assigns a default value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDefaultString(String key, String value)
	{
		if(hasKey(key)) return;
		tags.put(key, value);
	}
	
	/**
	 * Assigns a default value to the given key.
	 * @param key
	 * @param value
	 */
	public void setDefaultBoolean(String key, boolean value)
	{
		if(hasKey(key)) return;
		tags.put(key, value);
	}
	
	public void set(Tag tag)
	{
		tags.clear();
		Set<String> keyset = tag.getKeySet();
		for(String key : keyset)
		{
			Object value = tag.getValue(key);
			tags.put(key, value);
		}
	}
	
	
}
