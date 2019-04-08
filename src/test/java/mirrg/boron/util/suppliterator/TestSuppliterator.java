package mirrg.boron.util.suppliterator;

import static mirrg.boron.util.suppliterator.ISuppliterator.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import mirrg.boron.util.struct.Tuple;

public class TestSuppliterator
{

	@Test
	public void test()
	{
		Function<String, ISuppliterator<Integer>> f = s -> ofIterator(s.chars()
			.mapToObj(i -> i)
			.collect(Collectors.toCollection(ArrayList::new)).iterator());

		ISuppliterator<Number> flatMap = of(

			of(

				"ee",
				"654",
				"yut436y3"

			)
				.filter(s -> s.length() != 3),

			of(

				"q"

			)

		)
			.apply(ISuppliterator::flatten)
			.flatMap(f)
			.apply(ISuppliterator::cast);

		ArrayList<Number> collection = flatMap
			.toCollection(() -> new ArrayList<Number>());

		assertEquals(Stream.of(
			(int) 'e',
			(int) 'e',
			(int) 'y',
			(int) 'u',
			(int) 't',
			(int) '4',
			(int) '3',
			(int) '6',
			(int) 'y',
			(int) '3',
			(int) 'q').collect(Collectors.toCollection(ArrayList::new)),
			collection);
	}

	@Test
	public void test_apply2()
	{
		assertArrayEquals(new byte[] { 1, 2, 3 }, ISuppliterator.ofBytes(new byte[] { 1, 2, 3 })
			.apply2(s -> s::toByteArray));
		assertEquals("{3=4}", ISuppliterator.of(new Tuple<>(3, 4))
			.apply(ISuppliterator::toMap)
			.toString());
		assertEquals("{3=4}", ISuppliterator.of(new Tuple<>(3, 4))
			.apply2(s -> ISuppliterator::toMap)
			.toString());
	}

	@Test
	public void test2()
	{
		aAE(range(5, 11), 5, 6, 7, 8, 9, 10);
		aAE(range(5, 11).skip(2), 7, 8, 9, 10);
		aAE(range(5, 11).limit(2), 5, 6);
		aAE(range(5, 11).mid(2, 3), 7, 8, 9);
		aAE(range(5, 11).map(i -> i * 2), 10, 12, 14, 16, 18, 20);
		aAE(range(5, 11).filter(i -> i % 2 == 0), 6, 8, 10);
		aAE(range(5, 11).reverse(), 10, 9, 8, 7, 6, 5);
		aAE(of(6, 4, 7, 2, 2, 12, 6, 8, 7).sorted(Comparable::compareTo), 2, 2, 4, 6, 6, 7, 7, 8, 12);

		assertEquals((int) range(5, 11).filter(i -> i % 3 == 0).find().get(), 6);
		assertEquals("5<>6<>7<>8<>9<>10", range(5, 11).join("<>"));
		assertEquals("5678910", range(5, 11).join());
		assertEquals("5<>6<>7<>8<>9<>10", range(5, 11).map(i -> "" + i).collect(Collectors.joining("<>")));
		assertEquals(6L, (long) range(5, 11).collect(Collectors.counting()));
		assertEquals(6, range(5, 11).count());

		assertEquals("3456", range(3, 7).join());
		assertEquals("7654", range(7, 3).join());
		assertEquals("34567", rangeClosed(3, 7).join());
		assertEquals("76543", rangeClosed(7, 3).join());
	}

	@Test
	public void test_ofRangedArray()
	{
		assertEquals("", ofObjArray(new Object[] {}, 0, 0).join());
		assertEquals("0123", ofObjArray(new Object[] { 0, 1, 2, 3 }, 0, 4).join());
		assertEquals("012", ofObjArray(new Object[] { 0, 1, 2, 3 }, 0, 3).join());
		assertEquals("123", ofObjArray(new Object[] { 0, 1, 2, 3 }, 1, 3).join());
		assertEquals("12", ofObjArray(new Object[] { 0, 1, 2, 3 }, 1, 2).join());

		assertEquals("", ofByteArray(new byte[] {}, 0, 0).join());
		assertEquals("0123", ofByteArray(new byte[] { 0, 1, 2, 3 }, 0, 4).join());
		assertEquals("012", ofByteArray(new byte[] { 0, 1, 2, 3 }, 0, 3).join());
		assertEquals("123", ofByteArray(new byte[] { 0, 1, 2, 3 }, 1, 3).join());
		assertEquals("12", ofByteArray(new byte[] { 0, 1, 2, 3 }, 1, 2).join());

		assertEquals("", ofCharArray(new char[] {}, 0, 0).join());
		assertEquals("0123", ofCharArray(new char[] { '0', '1', '2', '3' }, 0, 4).join());
		assertEquals("012", ofCharArray(new char[] { '0', '1', '2', '3' }, 0, 3).join());
		assertEquals("123", ofCharArray(new char[] { '0', '1', '2', '3' }, 1, 3).join());
		assertEquals("12", ofCharArray(new char[] { '0', '1', '2', '3' }, 1, 2).join());

		assertEquals("", ofShortArray(new short[] {}, 0, 0).join());
		assertEquals("0123", ofShortArray(new short[] { 0, 1, 2, 3 }, 0, 4).join());
		assertEquals("012", ofShortArray(new short[] { 0, 1, 2, 3 }, 0, 3).join());
		assertEquals("123", ofShortArray(new short[] { 0, 1, 2, 3 }, 1, 3).join());
		assertEquals("12", ofShortArray(new short[] { 0, 1, 2, 3 }, 1, 2).join());

		assertEquals("", ofIntArray(new int[] {}, 0, 0).join());
		assertEquals("0123", ofIntArray(new int[] { 0, 1, 2, 3 }, 0, 4).join());
		assertEquals("012", ofIntArray(new int[] { 0, 1, 2, 3 }, 0, 3).join());
		assertEquals("123", ofIntArray(new int[] { 0, 1, 2, 3 }, 1, 3).join());
		assertEquals("12", ofIntArray(new int[] { 0, 1, 2, 3 }, 1, 2).join());

		assertEquals("", ofLongArray(new long[] {}, 0, 0).join());
		assertEquals("0123", ofLongArray(new long[] { 0, 1, 2, 3 }, 0, 4).join());
		assertEquals("012", ofLongArray(new long[] { 0, 1, 2, 3 }, 0, 3).join());
		assertEquals("123", ofLongArray(new long[] { 0, 1, 2, 3 }, 1, 3).join());
		assertEquals("12", ofLongArray(new long[] { 0, 1, 2, 3 }, 1, 2).join());

		assertEquals("", ofFloatArray(new float[] {}, 0, 0).join());
		assertEquals("0123", ofFloatArray(new float[] { 0, 1, 2, 3 }, 0, 4).map(f -> String.format("%.0f", f)).join());
		assertEquals("012", ofFloatArray(new float[] { 0, 1, 2, 3 }, 0, 3).map(f -> String.format("%.0f", f)).join());
		assertEquals("123", ofFloatArray(new float[] { 0, 1, 2, 3 }, 1, 3).map(f -> String.format("%.0f", f)).join());
		assertEquals("12", ofFloatArray(new float[] { 0, 1, 2, 3 }, 1, 2).map(f -> String.format("%.0f", f)).join());

		assertEquals("", ofDoubleArray(new double[] {}, 0, 0).join());
		assertEquals("0123", ofDoubleArray(new double[] { 0, 1, 2, 3 }, 0, 4).map(f -> String.format("%.0f", f)).join());
		assertEquals("012", ofDoubleArray(new double[] { 0, 1, 2, 3 }, 0, 3).map(f -> String.format("%.0f", f)).join());
		assertEquals("123", ofDoubleArray(new double[] { 0, 1, 2, 3 }, 1, 3).map(f -> String.format("%.0f", f)).join());
		assertEquals("12", ofDoubleArray(new double[] { 0, 1, 2, 3 }, 1, 2).map(f -> String.format("%.0f", f)).join());

		assertEquals("", ofBoolArray(new boolean[] {}, 0, 0).join());
		assertEquals("truefalsefalsetrue", ofBoolArray(new boolean[] { true, false, false, true }, 0, 4).join());
		assertEquals("truefalsefalse", ofBoolArray(new boolean[] { true, false, false, true }, 0, 3).join());
		assertEquals("falsefalsetrue", ofBoolArray(new boolean[] { true, false, false, true }, 1, 3).join());
		assertEquals("falsefalse", ofBoolArray(new boolean[] { true, false, false, true }, 1, 2).join());
	}

	@Test
	public void test_cast()
	{
		ISuppliterator<Integer> suppliterator1 = ISuppliterator.of(1, 2, 3, 4, 5);

		// castをapplyに入れるとインデントを変更せずにキャストできる
		ISuppliterator<Number> suppliterator2 = suppliterator1
			.apply(ISuppliterator::cast);

		assertEquals("12345", suppliterator2.join());
	}

	private void aAE(ISuppliterator<Integer> actual, int... expected)
	{
		assertArrayEquals(expected, actual.toIntArray(i -> i));
	}

	@Test
	public void test_sorted()
	{
		String[] ss = { "10", "-200", "5", "69", "35" };
		String e = "5,10,69,35,-200";

		assertEquals("-200,10,35,5,69", ISuppliterator.of(ss).sortedObj(s -> s).join(","));
		assertEquals(e, ISuppliterator.of(ss).sortedInt(s -> s.length()).join(","));
		assertEquals(e, ISuppliterator.of(ss).sortedLong(s -> s.length()).join(","));
		assertEquals(e, ISuppliterator.of(ss).sortedDouble(s -> s.length()).join(","));

		assertEquals("1a2b3c", ISuppliterator.ofIterable(ISuppliterator.of(
			new Tuple<>(1, "a"),
			new Tuple<>(2, "b"),
			new Tuple<>(3, "c"))
			.apply(ISuppliterator::toMap).entrySet())
			.sortedObj(e2 -> e2.getKey())
			.map(e2 -> e2.getKey() + e2.getValue())
			.join());

	}

	@Test
	public void test_before()
	{
		assertEquals("12345", ISuppliterator.of(3, 4, 5).before(1, 2).join());
		assertEquals("34512", ISuppliterator.of(3, 4, 5).after(1, 2).join());
	}

	@Test
	public void test_indexed()
	{
		assertEquals("012", ISuppliterator.of("a", "b", "c").map((s, i) -> i).join());
		assertEquals("ac", ISuppliterator.of("a", "b", "c").filter((s, i) -> i != 1).join());
		new Object() {
			private String s = "";

			private void run()
			{
				assertEquals("abc", ISuppliterator.of("a", "b", "c").peek((s2, i) -> s += i).join());
				assertEquals("012", s);
			}
		}.run();
		assertEquals("abbccc", ISuppliterator.of("a", "b", "c")
			.map((s, i) -> ISuppliterator.range(0, i + 1)
				.map(i2 -> s)
				.join())
			.join());
		assertEquals("0a1b2c", ISuppliterator.of("a", "b", "c").indexed().map(t -> "" + t.x + t.y).join());
		new Object() {
			private String s = "";

			private void run()
			{
				ISuppliterator.of("a", "b", "c").forEach((s2, i) -> s += i);
				assertEquals("012", s);
			}
		}.run();
	}

	@Test
	public void test_distinct()
	{
		assertArrayEquals(
			ISuppliterator.of(5, 9, 4, 8, 6, 2, 1, 7, 3)
				.apply2(s -> s::toIntArray),
			ISuppliterator.of(5, 9, 4, 8, 6, 2, 5, 1, 6, 4, 8, 5, 7, 8, 4, 3, 2, 6, 1)
				.distinct()
				.apply2(s -> s::toIntArray));
		assertArrayEquals(
			ISuppliterator.of(5, 9, 8, 6, 2)
				.apply2(s -> s::toIntArray),
			ISuppliterator.of(5, 9, 4, 8, 6, 2, 5, 1, 6, 4, 8, 5, 7, 8, 4, 3, 2, 6, 1)
				.distinct(i -> i % 5)
				.apply2(s -> s::toIntArray));
	}

	/////////////////////////////////////

	@Test
	public void test_toByteArray()
	{
		byte[] a = new byte[50000];
		byte[] b = new byte[50000];

		for (int i = 0; i < a.length; i++) {
			a[i] = (byte) i;
			b[i] = (byte) ((a[i] + 23) ^ 0x73);
		}

		byte[] c = ISuppliterator.ofByteArray(a).map(v -> v + 23).toByteArray(v -> (byte) (v ^ 0x73));

		assertArrayEquals(b, c);
		assertArrayEquals(a, ISuppliterator.ofBytes(a).apply2(s -> s::toByteArray));
	}

	@Test
	public void test_toCharArray()
	{
		char[] a = new char[50000];
		char[] b = new char[50000];

		for (int i = 0; i < a.length; i++) {
			a[i] = (char) i;
			b[i] = (char) ((a[i] + 23) ^ 0x8573);
		}

		char[] c = ISuppliterator.ofCharArray(a).map(v -> v + 23).toCharArray(v -> (char) (v ^ 0x8573));

		assertArrayEquals(b, c);
		assertArrayEquals(a, ISuppliterator.ofCharacters(a).apply2(s -> s::toCharArray));
	}

	@Test
	public void test_toShortArray()
	{
		short[] a = new short[50000];
		short[] b = new short[50000];

		for (int i = 0; i < a.length; i++) {
			a[i] = (short) i;
			b[i] = (short) ((a[i] + 23) ^ 0x8573);
		}

		short[] c = ISuppliterator.ofShortArray(a).map(v -> v + 23).toShortArray(v -> (short) (v ^ 0x8573));

		assertArrayEquals(b, c);
		assertArrayEquals(a, ISuppliterator.ofShortArray(a).apply2(s -> s::toShortArray));
	}

	@Test
	public void test_toIntArray()
	{
		int[] a = new int[50000];
		int[] b = new int[50000];

		for (int i = 0; i < a.length; i++) {
			a[i] = (int) i;
			b[i] = (int) ((a[i] + 23) ^ 0x85948673);
		}

		int[] c = ISuppliterator.ofIntArray(a).map(v -> v + 23).toIntArray(v -> (int) (v ^ 0x85948673));

		assertArrayEquals(b, c);
		assertArrayEquals(a, ISuppliterator.ofIntArray(a).apply2(s -> s::toIntArray));
	}

	@Test
	public void test_toLongArray()
	{
		long[] a = new long[50000];
		long[] b = new long[50000];

		for (int i = 0; i < a.length; i++) {
			a[i] = (long) i;
			b[i] = (long) ((a[i] + 23) ^ 0x8593834584235673L);
		}

		long[] c = ISuppliterator.ofLongArray(a).map(v -> v + 23).toLongArray(v -> (long) (v ^ 0x8593834584235673L));

		assertArrayEquals(b, c);
		assertArrayEquals(a, ISuppliterator.ofLongArray(a).apply2(s -> s::toLongArray));
	}

	@Test
	public void test_toFloatArray()
	{
		float[] a = new float[50000];
		float[] b = new float[50000];

		for (int i = 0; i < a.length; i++) {
			a[i] = (float) i;
			b[i] = (float) ((a[i] + 23) * 23.5325F);
		}

		float[] c = ISuppliterator.ofFloatArray(a).map(v -> v + 23).toFloatArray(v -> (float) (v * 23.5325F));

		assertArrayEquals(b, c, 0.001F);
		assertArrayEquals(a, ISuppliterator.ofFloatArray(a).apply2(s -> s::toFloatArray), 0.001F);
	}

	@Test
	public void test_toDoubleArray()
	{
		double[] a = new double[50000];
		double[] b = new double[50000];

		for (int i = 0; i < a.length; i++) {
			a[i] = (double) i;
			b[i] = (double) ((a[i] + 23) * 23.5325);
		}

		double[] c = ISuppliterator.ofDoubleArray(a).map(v -> v + 23).toDoubleArray(v -> (double) (v * 23.5325));

		assertArrayEquals(b, c, 0.001F);
		assertArrayEquals(a, ISuppliterator.ofDoubleArray(a).apply2(s -> s::toDoubleArray), 0.001F);
	}

	@Test
	public void test_toBoolArray()
	{
		boolean[] a = new boolean[50000];
		boolean[] b = new boolean[50000];

		for (int i = 0; i < a.length; i++) {
			a[i] = i % 2 == 0;
			b[i] = !a[i];
		}

		boolean[] c = ISuppliterator.ofBoolArray(a).map(v -> v).toBoolArray(v -> !v);

		assertArrayEquals(b, c);
		assertArrayEquals(a, ISuppliterator.ofBoolArray(a).apply2(s -> s::toBoolArray));
	}

}
