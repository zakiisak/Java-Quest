package game.utils;

import java.math.BigInteger;
import java.util.Random;

public class Rand {
	
	public static final Random rng = new Random();
	
	public static BigInteger next(BigInteger n)
	{
		BigInteger result = new BigInteger(n.bitLength(), rng);
	    while( result.compareTo(n) >= 0 ) {
	        result = new BigInteger(n.bitLength(), rng);
	    }
	    return result;
	}
	
}
