package mirrg.boron.util;

import static mirrg.boron.util.UtilsString.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestUString
{

	@Test
	public void test_rept()
	{
		assertEquals("", repeat("あいう", 0));
		assertEquals("あいう", repeat("あいう", 1));
		assertEquals("あいうあいう", repeat("あいう", 2));
		assertEquals("あいうあいうあいう", repeat("あいう", 3));
		assertEquals("aiuaiuaiu", repeat("aiu", 3));
	}

	@Test
	public void test_fill()
	{
		assertEquals("00013", fillLeft('0', "13", 5));
		assertEquals("13000", fillRight('0', "13", 5));
		assertEquals("134568", fillLeft('0', "134568", 5));
		assertEquals("134568", fillRight('0', "134568", 5));
		assertEquals("   13", fillLeft("13", 5));
		assertEquals("13   ", fillRight("13", 5));
		assertEquals("134568", fillLeft("134568", 5));
		assertEquals("134568", fillRight("134568", 5));
	}

}
