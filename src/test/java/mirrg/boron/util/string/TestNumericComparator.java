package mirrg.boron.util.string;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import mirrg.boron.util.string.NumericComparator.Token;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.suppliterator.ISuppliterator;

public final class TestNumericComparator
{

	@Test
	public void test()
	{

		assertEquals(nc(), nc(""));
		assertEquals(nc(t('a')), nc("a"));
		assertEquals(nc(t(1)), nc("1"));
		assertEquals(nc(t(1234567890)), nc("1234567890"));
		assertEquals(nc(t("123456789012345678901234567890", 0)), nc("123456789012345678901234567890"));
		assertEquals(nc(t(159, 2)), nc("00159"));
		assertEquals(nc(t('a'), t('b')), nc("ab"));
		assertEquals(nc(t('a'), t('b'), t(1562, 1)), nc("ab01562"));
		assertEquals(nc(t('a'), t('b'), t(1562, 1), t('c'), t('d')), nc("ab01562cd"));
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
		assertTrue(nc("a").compareTo(nc("0a")) > 0);
		assertTrue(nc("a").compareTo(nc("")) > 0);
		assertTrue(nc("1").compareTo(nc("")) > 0);
		assertTrue(nc("0.070").compareTo(nc("0.8")) > 0);
		assertTrue(nc("-0").compareTo(nc("+0")) > 0);
		assertTrue(nc("-0.00").compareTo(nc("+0.00")) > 0);
		assertTrue(nc("1").compareTo(nc("-1")) > 0);
		assertTrue(nc("0x1000").compareTo(nc("0x200A")) > 0);
		assertTrue(nc("aaaa").compareTo(nc("aaa0a")) > 0);
		assertTrue(nc("00").compareTo(nc("0")) > 0);
		assertTrue(nc("01").compareTo(nc("1")) > 0);
		assertTrue(nc("1234567890123456789010").compareTo(nc("123456789012345678902")) > 0);

	}

	@Test
	public void test2()
	{
		assertEquals(
			ISuppliterator.of(
				"",

				"0",
				"0a",
				"0a0",
				"0a1",
				"0aa",

				"00",
				"00a",

				"000",

				"1",
				"1a",
				"1a0",
				"1a1",
				"1aa",

				"01",
				"01a",

				"001",
				"10",
				"10a",
				"010",
				"11",
				"11a",
				"011",
				"100",
				"101",
				"110",
				"111",

				"a",
				"a0",
				"a0a",
				"a00",
				"a1",
				"a1a",
				"a01",
				"a10",
				"a11",
				"aa",
				"aa0",
				"aa1",
				"aaa")
				.toImmutableArray(),
			ISuppliterator.of(
				"",

				"a",
				"1",
				"0",

				"aa",
				"a1",
				"a0",
				"1a",
				"11",
				"10",
				"0a",
				"01",
				"00",

				"aaa",
				"aa1",
				"aa0",
				"a1a",
				"a11",
				"a10",
				"a0a",
				"a01",
				"a00",
				"1aa",
				"1a1",
				"1a0",
				"11a",
				"111",
				"110",
				"10a",
				"101",
				"100",
				"0aa",
				"0a1",
				"0a0",
				"01a",
				"011",
				"010",
				"00a",
				"001",
				"000")
				.sorted(NumericComparator.COMPARATOR)
				.toImmutableArray());

	}

	private static Token t(char ch)
	{
		return new Token(ch);
	}

	private static Token t(int i)
	{
		return new Token(BigInteger.valueOf(i), 0);
	}

	private static Token t(int i, int zeros)
	{
		return new Token(BigInteger.valueOf(i), zeros);
	}

	private static Token t(String i, int zeros)
	{
		return new Token(new BigInteger(i), zeros);
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
