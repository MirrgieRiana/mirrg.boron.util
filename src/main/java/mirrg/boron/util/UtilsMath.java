package mirrg.boron.util;

import java.util.Random;

public class UtilsMath
{

	public static int randomBetween(Random random, int minInclusive, int maxInclusive)
	{
		return (int) (random.nextInt(maxInclusive - minInclusive + 1) + minInclusive);
	}

	public static int randomBetween(int minInclusive, int maxInclusive)
	{
		return (int) (Math.random() * (maxInclusive - minInclusive + 1) + minInclusive);
	}

}
