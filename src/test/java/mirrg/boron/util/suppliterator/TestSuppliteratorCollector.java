package mirrg.boron.util.suppliterator;

import static mirrg.boron.util.suppliterator.ISuppliterator.*;
import static mirrg.boron.util.suppliterator.SuppliteratorCollectors.*;
import static mirrg.boron.util.suppliterator.SuppliteratorCollectors.cast;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.Test;

import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.struct.Tuple1;
import mirrg.boron.util.struct.Tuple3;
import mirrg.boron.util.struct.Tuple4;

public class TestSuppliteratorCollector
{

	@Test
	public void test_1()
	{
		{
			assertEquals('9', (char) characters("739184562")
				.collect(teeing(

					max()

				)).x.get());
		}
		{
			Tuple<Optional<Character>, Optional<Character>> t = characters("739184562")
				.collect(teeing(

					SuppliteratorCollectors.<Character> max(),

					SuppliteratorCollectors.<Character> min()

				));
			assertEquals('9', (char) t.x.get());
			assertEquals('1', (char) t.y.get());
		}
		{
			Tuple3<Optional<Character>, Optional<Character>, Long> t = characters("739184562")
				.collect(teeing(

					SuppliteratorCollectors.<Character> max(),

					SuppliteratorCollectors.<Character> min(),

					counting()

				));
			assertEquals('9', (char) t.x.get());
			assertEquals('1', (char) t.y.get());
			assertEquals(9, (long) t.z);
		}
		{
			Tuple4<Optional<Character>, Optional<Character>, Long, Tuple1<Long>> t = characters("739184562")
				.collect(teeing(

					SuppliteratorCollectors.<Character> max(),

					SuppliteratorCollectors.<Character> min(),

					counting(),

					teeing(

						counting()

					)

				));
			assertEquals('9', (char) t.x.get());
			assertEquals('1', (char) t.y.get());
			assertEquals(9, (long) t.z);
			assertEquals(9, (long) t.w.x);
		}
		{
			Tuple4<Optional<Character>, Optional<Character>, Long, Tuple3<Long, String, String>> t = characters("739184562")
				.collect(teeing(

					SuppliteratorCollectors.<Character> max(),

					SuppliteratorCollectors.<Character> min(),

					counting(),

					teeing(

						counting(),

						joining(),

						joining("|")

					)

				));
			assertEquals('9', (char) t.x.get());
			assertEquals('1', (char) t.y.get());
			assertEquals(9, (long) t.z);
			assertEquals(9, (long) t.w.x);
			assertEquals("739184562", t.w.y);
			assertEquals("7|3|9|1|8|4|5|6|2", t.w.z);
		}
		{
			ImmutableArray<Object> t = characters("12345")
				.collect(teeingOf(

					joining(),

					joining("|"),

					joining(","),

					joining("\n"),

					counting(),

					joining(";"),

					joining("-")

				));
			assertEquals(7, t.length());
			assertEquals("12345", t.get(0));
			assertEquals("1|2|3|4|5", t.get(1));
			assertEquals("1,2,3,4,5", t.get(2));
			assertEquals("1\n2\n3\n4\n5", t.get(3));
			assertEquals(5, (long) t.get(4));
			assertEquals("1;2;3;4;5", t.get(5));
			assertEquals("1-2-3-4-5", t.get(6));
		}
	}

	@Test
	public void test_ofCollector()
	{
		Collector<CharSequence, ?, String> a = Collectors.joining(",");
		ISuppliteratorCollector<String, String> b = ofCollector(a);
		assertEquals("1,2,3,4,5", characters("12345")
			.map(c -> Character.toString(c))
			.collect(b));
	}

	@Test
	public void test_cast()
	{
		Supplier<ISuppliteratorCollector<Object, String>> a = () -> joining(",");
		Supplier<ISuppliteratorCollector<Object, CharSequence>> b = () -> cast(a.get());
		@SuppressWarnings("unused")
		Supplier<ISuppliteratorCollector<Character, CharSequence>> c = () -> cast(a.get());
		assertEquals("1,2,3,4,5", characters("12345").collect(a.get()));
		assertEquals("1,2,3,4,5", characters("12345").collect(b.get()));
	}

}
