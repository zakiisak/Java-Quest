package game.utils;

import java.math.BigInteger;

public class MathUtils {
	public static float getAngle(Point p1, Point target) {
		float x1 = (float) p1.x + 0.5f;
		float y1 = (float) p1.y + 0.5f;
		float x2 = (float) target.x + 0.5f;
		float y2 = (float) target.y + 0.5f;
	    float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}
	
	public static float dist(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	public static float sin(double degrees)
	{
		return (float) Math.sin(Math.toRadians(degrees));
	}

	private static BigInteger newtonIteration(BigInteger n, BigInteger x0)
	{
	    final BigInteger x1 = n.divide(x0).add(x0).shiftRight(1);
	    return x0.equals(x1)||x0.equals(x1.subtract(BigInteger.ONE)) ? x0 : newtonIteration(n, x1);
	}
	
	public static BigInteger sqrt(final BigInteger number)
	{
	    if(number.signum() == -1)
	        throw new ArithmeticException("We can only calculate the square root of positive numbers.");
	    return newtonIteration(number, BigInteger.ONE);
	}

	public static BigInteger bigIntSqRootFloor(BigInteger x) throws IllegalArgumentException {
		if (x.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException("Negative argument.");
		}
		// square roots of 0 and 1 are trivial and
		// y == 0 will cause a divide-by-zero exception
		if (x.equals(BigInteger.ZERO) || x.equals(BigInteger.ONE)) {
			return x;
		} // end if
		BigInteger two = BigInteger.valueOf(2L);
		BigInteger y;
		// starting with y = x / 2 avoids magnitude issues with x squared
		for (y = x.divide(two); y.compareTo(x.divide(y)) > 0; y = ((x.divide(y)).add(y)).divide(two))
			;
		return y;
	} // end bigIntSqRootFloor

	public static BigInteger bigIntSqRootCeil(BigInteger x) throws IllegalArgumentException {
		if (x.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException("Negative argument.");
		}
		// square roots of 0 and 1 are trivial and
		// y == 0 will cause a divide-by-zero exception
		if (x == BigInteger.ZERO || x == BigInteger.ONE) {
			return x;
		} // end if
		BigInteger two = BigInteger.valueOf(2L);
		BigInteger y;
		// starting with y = x / 2 avoids magnitude issues with x squared
		for (y = x.divide(two); y.compareTo(x.divide(y)) > 0; y = ((x.divide(y)).add(y)).divide(two))
			;
		if (x.compareTo(y.multiply(y)) == 0) {
			return y;
		} else {
			return y.add(BigInteger.ONE);
		}
	}
	
	public static int max(int a, int b)
	{
		if(b > a)
			return a;
		return b;
			
	}
}
