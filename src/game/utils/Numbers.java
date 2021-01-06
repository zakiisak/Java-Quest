package game.utils;

import java.math.BigInteger;

public class Numbers {
	
	public static final BigInteger ZERO = new BigInteger("0");
	public static final BigInteger ONE = new BigInteger("1");
	public static final BigInteger TWO = new BigInteger("2");
	public static final BigInteger THREE = new BigInteger("3");
	public static final BigInteger FOUR = new BigInteger("4");
	public static final BigInteger FIVE = new BigInteger("5");
	public static final BigInteger SIX = new BigInteger("6");
	public static final BigInteger SEVEN = new BigInteger("7");
	public static final BigInteger EIGHT = new BigInteger("8");
	public static final BigInteger NINE = new BigInteger("9");
	public static final BigInteger TEN = new BigInteger("10");
	public static final BigInteger HUNDRED = new BigInteger("100");
	
	private static String[] formats = new String[] {"M", "T", "Qn", "Sp", "Nn", "Ud", "Td",
			"Qi", "Sd", "Nv", "Un", "Tr", "Qu", "Se", "Oc", "Tg", "Du", "Qg", "Ss", "Og", "Qg", "?"};
	
	public static String format(BigInteger integer)
	{
		if(integer.compareTo(new BigInteger("1000000")) < 0)
			return integer.toString();
		String num = "1";
		while(new BigInteger(num).compareTo(integer) < 0)
		{
			num += "000000";
		}
		num = num.substring(0, num.length() - 6);
		int index = (num.length() - 1) / 6 - 1;
		while(index < 0)
			index++;
		String questions = "";
		while(index > formats.length - 1)
		{
			questions += '?';
			index--;
		}
		String suffix = "#f0ff" + formats[index] + questions;
		
		return integer.divide(new BigInteger(num)) + suffix;
	}
	
	
}
