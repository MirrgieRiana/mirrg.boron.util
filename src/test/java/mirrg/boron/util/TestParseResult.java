package mirrg.boron.util;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

public class TestParseResult
{

	private ParseResult<Integer> r;

	@Test
	public void test()
	{

		r = ParseResult.of(50);
		assertEquals(Optional.of(50), r.oValue);
		assertEquals(true, r.isSuccessful());
		assertEquals(50, (int) r.get());
		assertTrue(r.equals(ParseResult.of(new Integer(50))));
		assertEquals(50, r.hashCode());

		r = ParseResult.error();
		assertEquals(Optional.empty(), r.oValue);
		assertEquals(false, r.isSuccessful());
		assertException(() -> r.get());
		assertTrue(r == ParseResult.<Integer> error());
		assertEquals(0, r.hashCode());

	}

	private static interface A
	{

		public void run() throws Exception;

	}

	private void assertException(A a)
	{
		try {
			a.run();
			fail();
		} catch (Exception e) {

		}
	}

}
