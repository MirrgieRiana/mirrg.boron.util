package mirrg.boron.util.struct;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

import org.junit.Test;

public class TestImmutableArray
{

	@Test
	public void test1()
	{
		ImmutableArray<String> array1;
		{
			array1 = ImmutableArray.of("a", "b");
			assertEquals(2, array1.length());
			assertEquals("a", array1.get(0));
			assertEquals("b", array1.get(1));
		}

		ImmutableArray<String> array2;
		{
			String[] array = {
				"a", "b",
			};
			array2 = ImmutableArray.of(array);
			array[0] = "c";
			array[1] = "d";
			assertEquals(2, array2.length());
			assertEquals("a", array2.get(0));
			assertEquals("b", array2.get(1));
		}

		ImmutableArray<String> array3;
		{
			ArrayList<String> array = new ArrayList<String>() {
				{
					add("a");
					add("b");
				}
			};
			array3 = ImmutableArray.ofList(array);
			array.set(0, "c");
			array.set(1, "d");
			assertEquals(2, array3.length());
			assertEquals("a", array3.get(0));
			assertEquals("b", array3.get(1));
		}

		assertEquals(array1, array2);
		assertEquals(array2, array3);
		assertEquals(array1, array3);

		ImmutableArray<String> array4 = ImmutableArray.of("a", "c");
		assertNotEquals(array1, array4);
		assertNotEquals(array2, array4);
		assertNotEquals(array3, array4);
		assertNotEquals(array4, array1);
		assertNotEquals(array4, array2);
		assertNotEquals(array4, array3);

		ImmutableArray<String> array5 = ImmutableArray.of("a");
		assertNotEquals(array1, array5);
		assertNotEquals(array2, array5);
		assertNotEquals(array3, array5);
		assertNotEquals(array4, array5);
		assertNotEquals(array5, array1);
		assertNotEquals(array5, array2);
		assertNotEquals(array5, array3);
		assertNotEquals(array5, array4);

		ImmutableArray<String> array6 = ImmutableArray.of("a", "b", "c");
		assertNotEquals(array1, array6);
		assertNotEquals(array2, array6);
		assertNotEquals(array3, array6);
		assertNotEquals(array4, array6);
		assertNotEquals(array5, array6);
		assertNotEquals(array6, array1);
		assertNotEquals(array6, array2);
		assertNotEquals(array6, array3);
		assertNotEquals(array6, array4);
		assertNotEquals(array6, array5);

		ArrayList<String> list1 = Collections.list(array6.items());
		assertEquals(array6.length(), list1.size());
		assertEquals(array6.get(0), list1.get(0));
		assertEquals(array6.get(1), list1.get(1));
		assertEquals(array6.get(2), list1.get(2));

		String[] list2 = array6.stream().toArray(String[]::new);
		assertEquals(array6.length(), list2.length);
		assertEquals(array6.get(0), list2[0]);
		assertEquals(array6.get(1), list2[1]);
		assertEquals(array6.get(2), list2[2]);

		ArrayList<String> list3 = new ArrayList<>();
		array6.forEach((Consumer<String>) list3::add);
		assertEquals(array6.length(), list3.size());
		assertEquals(array6.get(0), list3.get(0));
		assertEquals(array6.get(1), list3.get(1));
		assertEquals(array6.get(2), list3.get(2));

		String[] array7 = ImmutableArray.of("a", "b", "c").toArray(String[]::new);
		assertEquals(array7.length, 3);
		assertEquals(array7[0], "a");
		assertEquals(array7[1], "b");
		assertEquals(array7[2], "c");

		ArrayList<String> array8 = ImmutableArray.of("a", "b", "c").toCollection(ArrayList::new);
		assertEquals(array8.size(), 3);
		assertEquals(array8.get(0), "a");
		assertEquals(array8.get(1), "b");
		assertEquals(array8.get(2), "c");

	}

	@Test
	public void test_suppliterator()
	{
		assertEquals("12345", ImmutableArray.of(1, 2, 3, 4, 5).suppliterator().join());
		assertEquals("34", ImmutableArray.of(1, 2, 3, 4, 5).suppliterator(2, 2).join());
	}

	@Test
	public void test_of()
	{

		assertEquals("[null]", String.join("", ImmutableArray.of("[", null, "]").toArray(String[]::new)));

		{
			String[] array = new String[] { "a", "b", "c" };

			assertEquals("abc", ImmutableArray.of("a", "b", "c").suppliterator().join());
			assertEquals("a", ImmutableArray.of("a").suppliterator().join());
			assertEquals("", ImmutableArray.of().suppliterator().join());
			assertEquals("abc", ImmutableArray.ofObjArray(array).suppliterator().join());
			assertEquals("abc", ImmutableArray.ofObjArray(array, 0, 3).suppliterator().join());
			assertEquals("bc", ImmutableArray.ofObjArray(array, 1, 2).suppliterator().join());
			assertEquals("ab", ImmutableArray.ofObjArray(array, 0, 2).suppliterator().join());
			assertEquals("b", ImmutableArray.ofObjArray(array, 1, 1).suppliterator().join());
			assertEquals("", ImmutableArray.ofObjArray(array, 0, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofObjArray(array, 1, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofObjArray(array, 2, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofObjArray(array, 3, 0).suppliterator().join());
		}

		{
			byte[] array = new byte[] { 1, 2, 3 };

			assertEquals("123", ImmutableArray.ofBytes((byte) 1, (byte) 2, (byte) 3).suppliterator().join());
			assertEquals("1", ImmutableArray.ofBytes((byte) 1).suppliterator().join());
			assertEquals("", ImmutableArray.ofBytes().suppliterator().join());
			assertEquals("123", ImmutableArray.ofByteArray(array).suppliterator().join());
			assertEquals("123", ImmutableArray.ofByteArray(array, 0, 3).suppliterator().join());
			assertEquals("23", ImmutableArray.ofByteArray(array, 1, 2).suppliterator().join());
			assertEquals("12", ImmutableArray.ofByteArray(array, 0, 2).suppliterator().join());
			assertEquals("2", ImmutableArray.ofByteArray(array, 1, 1).suppliterator().join());
			assertEquals("", ImmutableArray.ofByteArray(array, 0, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofByteArray(array, 1, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofByteArray(array, 2, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofByteArray(array, 3, 0).suppliterator().join());
		}

		{
			char[] array = new char[] { '1', '2', '3' };

			assertEquals("123", ImmutableArray.ofCharacters('1', '2', '3').suppliterator().join());
			assertEquals("1", ImmutableArray.ofCharacters('1').suppliterator().join());
			assertEquals("", ImmutableArray.ofCharacters().suppliterator().join());
			assertEquals("123", ImmutableArray.ofCharArray(array).suppliterator().join());
			assertEquals("123", ImmutableArray.ofCharArray(array, 0, 3).suppliterator().join());
			assertEquals("23", ImmutableArray.ofCharArray(array, 1, 2).suppliterator().join());
			assertEquals("12", ImmutableArray.ofCharArray(array, 0, 2).suppliterator().join());
			assertEquals("2", ImmutableArray.ofCharArray(array, 1, 1).suppliterator().join());
			assertEquals("", ImmutableArray.ofCharArray(array, 0, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofCharArray(array, 1, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofCharArray(array, 2, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofCharArray(array, 3, 0).suppliterator().join());
		}

		{
			short[] array = new short[] { 1, 2, 3 };

			assertEquals("123", ImmutableArray.ofShorts((short) 1, (short) 2, (short) 3).suppliterator().join());
			assertEquals("1", ImmutableArray.ofShorts((short) 1).suppliterator().join());
			assertEquals("", ImmutableArray.ofShorts().suppliterator().join());
			assertEquals("123", ImmutableArray.ofShortArray(array).suppliterator().join());
			assertEquals("123", ImmutableArray.ofShortArray(array, 0, 3).suppliterator().join());
			assertEquals("23", ImmutableArray.ofShortArray(array, 1, 2).suppliterator().join());
			assertEquals("12", ImmutableArray.ofShortArray(array, 0, 2).suppliterator().join());
			assertEquals("2", ImmutableArray.ofShortArray(array, 1, 1).suppliterator().join());
			assertEquals("", ImmutableArray.ofShortArray(array, 0, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofShortArray(array, 1, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofShortArray(array, 2, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofShortArray(array, 3, 0).suppliterator().join());
		}

		{
			int[] array = new int[] { 1, 2, 3 };

			assertEquals("123", ImmutableArray.ofIntegers(1, 2, 3).suppliterator().join());
			assertEquals("1", ImmutableArray.ofIntegers(1).suppliterator().join());
			assertEquals("", ImmutableArray.ofIntegers().suppliterator().join());
			assertEquals("123", ImmutableArray.ofIntArray(array).suppliterator().join());
			assertEquals("123", ImmutableArray.ofIntArray(array, 0, 3).suppliterator().join());
			assertEquals("23", ImmutableArray.ofIntArray(array, 1, 2).suppliterator().join());
			assertEquals("12", ImmutableArray.ofIntArray(array, 0, 2).suppliterator().join());
			assertEquals("2", ImmutableArray.ofIntArray(array, 1, 1).suppliterator().join());
			assertEquals("", ImmutableArray.ofIntArray(array, 0, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofIntArray(array, 1, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofIntArray(array, 2, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofIntArray(array, 3, 0).suppliterator().join());
		}

		{
			long[] array = new long[] { 1, 2, 3 };

			assertEquals("123", ImmutableArray.ofLongs(1, 2, 3).suppliterator().join());
			assertEquals("1", ImmutableArray.ofLongs(1).suppliterator().join());
			assertEquals("", ImmutableArray.ofLongs().suppliterator().join());
			assertEquals("123", ImmutableArray.ofLongArray(array).suppliterator().join());
			assertEquals("123", ImmutableArray.ofLongArray(array, 0, 3).suppliterator().join());
			assertEquals("23", ImmutableArray.ofLongArray(array, 1, 2).suppliterator().join());
			assertEquals("12", ImmutableArray.ofLongArray(array, 0, 2).suppliterator().join());
			assertEquals("2", ImmutableArray.ofLongArray(array, 1, 1).suppliterator().join());
			assertEquals("", ImmutableArray.ofLongArray(array, 0, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofLongArray(array, 1, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofLongArray(array, 2, 0).suppliterator().join());
			assertEquals("", ImmutableArray.ofLongArray(array, 3, 0).suppliterator().join());
		}

		{
			float[] array = new float[] { 1, 2, 3 };

			assertEquals("123", ImmutableArray.ofFloats(1, 2, 3).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("1", ImmutableArray.ofFloats(1).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofFloats().suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("123", ImmutableArray.ofFloatArray(array).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("123", ImmutableArray.ofFloatArray(array, 0, 3).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("23", ImmutableArray.ofFloatArray(array, 1, 2).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("12", ImmutableArray.ofFloatArray(array, 0, 2).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("2", ImmutableArray.ofFloatArray(array, 1, 1).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofFloatArray(array, 0, 0).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofFloatArray(array, 1, 0).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofFloatArray(array, 2, 0).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofFloatArray(array, 3, 0).suppliterator().join(n -> String.format("%.0f", n)));
		}

		{
			double[] array = new double[] { 1, 2, 3 };

			assertEquals("123", ImmutableArray.ofDoubles(1, 2, 3).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("1", ImmutableArray.ofDoubles(1).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofDoubles().suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("123", ImmutableArray.ofDoubleArray(array).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("123", ImmutableArray.ofDoubleArray(array, 0, 3).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("23", ImmutableArray.ofDoubleArray(array, 1, 2).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("12", ImmutableArray.ofDoubleArray(array, 0, 2).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("2", ImmutableArray.ofDoubleArray(array, 1, 1).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofDoubleArray(array, 0, 0).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofDoubleArray(array, 1, 0).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofDoubleArray(array, 2, 0).suppliterator().join(n -> String.format("%.0f", n)));
			assertEquals("", ImmutableArray.ofDoubleArray(array, 3, 0).suppliterator().join(n -> String.format("%.0f", n)));
		}

	}

	@Test
	public void test_empty()
	{
		assertEquals("", ImmutableArray.empty().suppliterator().join());
	}

}
