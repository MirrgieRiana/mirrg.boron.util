package mirrg.boron.util.string;

import static org.junit.Assert.*;

import org.junit.Test;

import mirrg.boron.util.string.NumericComparator.Token;
import mirrg.boron.util.struct.ImmutableArray;

public final class TestNumericComparator
{

	@Test
	public void test()
	{

		assertEquals(nc(), nc(""));
		assertEquals(nc(t('a')), nc("a"));
		assertEquals(nc(t(1)), nc("1"));
		assertEquals(nc(t(1234567890)), nc("1234567890"));
		assertEquals(nc(t(159)), nc("00159"));
		assertEquals(nc(t('a'), t('b')), nc("ab"));
		assertEquals(nc(t('a'), t('b'), t(1562)), nc("ab01562"));
		assertEquals(nc(t('a'), t('b'), t(1562), t('c'), t('d')), nc("ab01562cd"));
		assertEquals(nc(
			t('['), t('+'), t(1), t(']'),
			t('['), t('-'), t(2), t(']'),
			t('['), t(34), t('.'), t(56), t(']')), nc("[+1][-2][34.56]"));

		assertEquals(0, nc("[+1][-2][34.56]").compareTo(nc("[+1][-2][34.56]")));
		assertNotEquals(0, nc("[+1][-2][34.56]").compareTo(nc("1][-2][34.56]")));
		assertNotEquals(0, nc("[+1][-2][34.56]").compareTo(nc("[-1][-2][34.56]")));
		assertNotEquals(0, nc("[+1][-2][34.56]").compareTo(nc("[+1] [-2][34.56]")));
		assertNotEquals(0, nc("[+1][-2][34.56]").compareTo(nc("[+1][-2][35.56]")));
		assertNotEquals(0, nc("[+1][-2][34.56]").compareTo(nc("[+1][-2][34.560]")));

		assertTrue(nc("11").compareTo(nc("10")) > 0);
		assertTrue(nc("10").compareTo(nc("2")) > 0);
		assertTrue(nc("a10").compareTo(nc("a2")) > 0);
		assertTrue(nc("100a").compareTo(nc("10a")) > 0);
		assertTrue(nc("100 ").compareTo(nc("10 ")) > 0);
		assertTrue(nc("[100]").compareTo(nc("[10]")) > 0);
		assertTrue(nc("a1").compareTo(nc("1a")) > 0);
		assertTrue(nc("a1").compareTo(nc("99999a")) > 0);
		assertTrue(nc("10a").compareTo(nc("10")) > 0);
		assertTrue(nc("a").compareTo(nc("")) > 0);
		assertTrue(nc("1").compareTo(nc("")) > 0);
		assertTrue(nc("0.070").compareTo(nc("0.8")) > 0);
		assertTrue(nc("-0").compareTo(nc("+0")) > 0);
		assertTrue(nc("-0.00").compareTo(nc("+0.00")) > 0);
		assertTrue(nc("1").compareTo(nc("-1")) > 0);
		assertTrue(nc("0x1000").compareTo(nc("0x200A")) > 0);
		assertTrue(nc("aaaa").compareTo(nc("aaa0a")) > 0);

	}

	private static Token t(char ch)
	{
		return new Token(ch);
	}

	private static Token t(int i)
	{
		return new Token(i);
	}

	private static NumericComparator nc(Token... tokens)
	{
		return new NumericComparator(ImmutableArray.of(tokens));
	}

	private static NumericComparator nc(String string)
	{
		return NumericComparator.split(string);
	}

}
