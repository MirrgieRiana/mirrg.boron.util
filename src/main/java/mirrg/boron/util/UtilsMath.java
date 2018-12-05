package mirrg.boron.util;

import java.util.Comparator;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

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

	//

	public static OptionalInt parseInt(String s)
	{
		try {
			return OptionalInt.of(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	public static OptionalInt parseInt(String s, int radix)
	{
		try {
			return OptionalInt.of(Integer.parseInt(s, radix));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	public static OptionalLong parseLong(String s)
	{
		try {
			return OptionalLong.of(Long.parseLong(s));
		} catch (NumberFormatException e) {
			return OptionalLong.empty();
		}
	}

	public static OptionalLong parseLong(String s, int radix)
	{
		try {
			return OptionalLong.of(Long.parseLong(s, radix));
		} catch (NumberFormatException e) {
			return OptionalLong.empty();
		}
	}

	public static OptionalDouble parseDouble(String s)
	{
		try {
			return OptionalDouble.of(Double.parseDouble(s));
		} catch (NumberFormatException e) {
			return OptionalDouble.empty();
		}
	}

	//

	/**
	 * valueをmin以上max以下の数値に丸めます。
	 * minがmaxより大きい場合、maxの方をmin、minの方をmaxとして解釈されます。
	 */
	public static int trim(int value, int min, int max)
	{
		if (min > max) return trim(value, max, min);

		if (value < min) return min;
		if (value > max) return max;
		return value;
	}

	/**
	 * valueをmin以上max以下の数値に丸めます。
	 * minがmaxより大きい場合、maxの方をmin、minの方をmaxとして解釈されます。
	 */
	public static long trim(long value, long min, long max)
	{
		if (min > max) return trim(value, max, min);

		if (value < min) return min;
		if (value > max) return max;
		return value;
	}

	/**
	 * valueをmin以上max以下の数値に丸めます。
	 * minがmaxより大きい場合、maxの方をmin、minの方をmaxとして解釈されます。
	 */
	public static float trim(float value, float min, float max)
	{
		if (min > max) return trim(value, max, min);

		if (value < min) return min;
		if (value > max) return max;
		return value;
	}

	/**
	 * valueをmin以上max以下の数値に丸めます。
	 * minがmaxより大きい場合、maxの方をmin、minの方をmaxとして解釈されます。
	 */
	public static double trim(double value, double min, double max)
	{
		if (min > max) return trim(value, max, min);

		if (value < min) return min;
		if (value > max) return max;
		return value;
	}

	//

	/**
	 * valueがminかmaxと等しい場合に1、minからmaxの範囲内の場合に2、それ以外の場合に0を返します。
	 */
	public static int contains(int value, int min, int max)
	{
		if (min > max) return contains(value, max, min);

		if (value == min) return 1;
		if (value == max) return 1;

		if (value > min && value < max) return 1;

		return 0;
	}

	/**
	 * valueがminかmaxと等しい場合に1、minからmaxの範囲内の場合に2、それ以外の場合に0を返します。
	 */
	public static int contains(long value, long min, long max)
	{
		if (min > max) return contains(value, max, min);

		if (value == min) return 1;
		if (value == max) return 1;

		if (value > min && value < max) return 1;

		return 0;
	}

	/**
	 * valueがminかmaxと等しい場合に1、minからmaxの範囲内の場合に2、それ以外の場合に0を返します。
	 */
	public static int contains(float value, float min, float max)
	{
		if (min > max) return contains(value, max, min);

		if (value == min) return 1;
		if (value == max) return 1;

		if (value > min && value < max) return 1;

		return 0;
	}

	/**
	 * valueがminかmaxと等しい場合に1、minからmaxの範囲内の場合に2、それ以外の場合に0を返します。
	 */
	public static int contains(double value, double min, double max)
	{
		if (min > max) return contains(value, max, min);

		if (value == min) return 1;
		if (value == max) return 1;

		if (value > min && value < max) return 1;

		return 0;
	}

	//

	/**
	 * valueがminからmaxの範囲に収まるように、適切に余りを求めます。
	 * value = -1の場合maxを返し、以下流れに沿って適切な値を返します。
	 * minがmaxよりも大きな値だった場合、minとmaxを逆に解釈します。
	 */
	public static int torus(int value, int min, int max)
	{
		if (min > max) return torus(value, max, min);

		int n = max - min + 1;

		if (value < 0) {
			return (n - 1) - (-value - 1) % n + min;
		} else {
			return value % n + min;
		}
	}

	//

	public static <T, C extends Comparable<? super C>> Comparator<T> createComparator(Function<T, ? extends C> function)
	{
		return (a, b) -> function.apply(a).compareTo(function.apply(b));
	}

	public static <T> Comparator<T> createComparator(ToIntFunction<T> function)
	{
		return (a, b) -> Integer.compare(function.applyAsInt(a), function.applyAsInt(b));
	}

	public static <T> Comparator<T> createComparator(ToLongFunction<T> function)
	{
		return (a, b) -> Long.compare(function.applyAsLong(a), function.applyAsLong(b));
	}

	public static <T> Comparator<T> createComparator(ToDoubleFunction<T> function)
	{
		return (a, b) -> Double.compare(function.applyAsDouble(a), function.applyAsDouble(b));
	}

}
