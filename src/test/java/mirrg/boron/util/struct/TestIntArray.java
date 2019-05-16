package mirrg.boron.util.struct;

import static org.junit.Assert.*;

import java.util.function.Consumer;

import org.junit.Test;

public class TestIntArray
{

	@Test
	public void test_1()
	{
		IIntArrayView iav = ia2(1, 2, 3, 4, 5);

		assertEquals(5, iav.length());
		assertException(() -> iav.get(-1));
		assertEquals(1, iav.get(0));
		assertEquals(2, iav.get(1));
		assertEquals(3, iav.get(2));
		assertEquals(4, iav.get(3));
		assertEquals(5, iav.get(4));
		assertException(() -> iav.get(5));
		assertCopyTo(5, a -> iav.copyTo(0, a, 0, 5), ia(1, 2, 3, 4, 5));
		assertCopyTo(5, a -> iav.copyTo(0, a, 0, 3), ia(1, 2, 3, 0, 0));
		assertCopyTo(5, a -> iav.copyTo(2, a, 0, 3), ia(3, 4, 5, 0, 0));
		assertCopyTo(5, a -> iav.copyTo(2, a, 0, 1), ia(3, 0, 0, 0, 0));
		assertCopyTo(5, a -> iav.copyTo(0, a, 2, 3), ia(0, 0, 1, 2, 3));
		assertCopyTo(5, a -> iav.copyTo(2, a, 2, 3), ia(0, 0, 3, 4, 5));
		assertCopyTo(3, a -> iav.copyTo(0, a, 0, 3), ia(1, 2, 3));
		assertCopyTo(3, a -> iav.copyTo(2, a, 0, 3), ia(3, 4, 5));
		assertCopyTo(3, a -> iav.copyTo(0, a, 0, 0), ia(0, 0, 0));
		assertCopyTo(3, a -> iav.copyTo(5, a, 0, 0), ia(0, 0, 0));
		assertCopyTo(0, a -> iav.copyTo(0, a, 0, 0), ia());
		assertCopyTo(0, a -> iav.copyTo(5, a, 0, 0), ia());
		assertCopyTo(10, a -> iav.copyTo(0, a, 0, 5), ia(1, 2, 3, 4, 5, 0, 0, 0, 0, 0));
		assertCopyTo(10, a -> iav.copyTo(0, a, 5, 5), ia(0, 0, 0, 0, 0, 1, 2, 3, 4, 5));
		assertCopyTo(10, a -> iav.copyTo(0, a, 7, 3), ia(0, 0, 0, 0, 0, 0, 0, 1, 2, 3));
		assertCopyToException(10, a -> iav.copyTo(0, a, 0, 6));
		assertCopyToException(10, a -> iav.copyTo(0, a, 0, -1));
		assertCopyToException(10, a -> iav.copyTo(0, a, -1, 5));
		assertCopyToException(10, a -> iav.copyTo(0, a, 6, 5));
		assertCopyToException(10, a -> iav.copyTo(-1, a, 0, 5));
		assertCopyToException(10, a -> iav.copyTo(1, a, 0, 5));
		assertCopyToException(10, a -> iav.copyTo(5, a, 0, 1));
		assertCopyToException(10, a -> iav.copyTo(6, a, 0, 0));

	}

	private static int[] ia(int... array)
	{
		return array;
	}

	private static IntArray ia2(int... array)
	{
		return new IntArray(array);
	}

	private static void assertException(Runnable runnable)
	{
		try {
			runnable.run();
			fail();
		} catch (RuntimeException e) {

		}
	}

	private static void assertCopyTo(int arrayLength, Consumer<int[]> copier, int[] expected)
	{
		int[] array2 = new int[arrayLength];
		copier.accept(array2);
		assertArrayEquals(expected, array2);
	}

	private static void assertCopyToException(int arrayLength, Consumer<int[]> copier)
	{
		int[] array2 = new int[arrayLength];
		assertException(() -> copier.accept(array2));
	}

	@Test
	public void test_join()
	{

		assertEquals("1,2,3,4,5", ia2(1, 2, 3, 4, 5).join());
		assertEquals("1,2,3,4,5", ia2(1, 2, 3, 4, 5).join(","));
		assertEquals("12345", ia2(1, 2, 3, 4, 5).join(""));
		assertEquals("1|2|3|4|5", ia2(1, 2, 3, 4, 5).join("|"));
		assertEquals("[1, 2, 3, 4, 5]", ia2(1, 2, 3, 4, 5).toString());

		assertEquals("1", ia2(1).join());
		assertEquals("1", ia2(1).join(","));
		assertEquals("1", ia2(1).join(""));
		assertEquals("1", ia2(1).join("|"));
		assertEquals("[1]", ia2(1).toString());

		assertEquals("", ia2().join());
		assertEquals("", ia2().join(","));
		assertEquals("", ia2().join(""));
		assertEquals("", ia2().join("|"));
		assertEquals("[]", ia2().toString());

	}

}
