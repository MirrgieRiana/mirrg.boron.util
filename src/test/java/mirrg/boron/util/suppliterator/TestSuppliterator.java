package mirrg.boron.util.suppliterator;

import static mirrg.boron.util.suppliterator.ISuppliterator.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

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

}
