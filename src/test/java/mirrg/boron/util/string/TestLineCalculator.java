package mirrg.boron.util.string;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestLineCalculator
{

	@Test
	public void test1()
	{
		assertEquals(1, LineCalculator.get("").getLineCount());
		assertEquals(2, LineCalculator.get("\n").getLineCount());
		assertEquals(3, LineCalculator.get("\n\n").getLineCount());
	}

	@Test
	public void test_tableLineNumberToCharacterIndex()
	{
		String text = ""
			+ "\r\n"
			+ "11111\r"
			+ "111\n"
			+ "11111111\r\n"
			+ "11111\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "1111\r"
			+ "\r"
			+ "\r"
			+ "1111\n"
			+ "\n"
			+ "\n";

		LineCalculator lineCalculator = LineCalculator.get(text);

		String[] string = {
			""
		};
		lineCalculator.entrySet().stream().sequential().sorted((a, b) -> {
			if (a.getKey() < b.getKey()) return -1;
			if (a.getKey() > b.getKey()) return 1;
			return 0;
		}).forEach(entry -> {
			string[0] += "[" + entry.getKey() + ":" + +entry.getValue() + "]";
		});

		assertEquals("[1:0][2:2][3:8][4:12][5:22][6:29][7:31][8:33][9:38][10:39][11:40][12:45][13:46][14:47]",
			string[0]);
	}

	@Test
	public void test_getLineNumber()
	{
		String text = ""
			+ "\r\n" // 1
			+ "11111\r" // 2
			+ "111\n" // 3
			+ "11111111\r\n" // 4
			+ "11111\r\n" // 5
			+ "\r\n" // 6
			+ "\r\n" // 7
			+ "1111\r" // 8
			+ "\r" // 9
			+ "\r" // 10
			+ "1111\n" // 11
			+ "\n" // 12
			+ "\n"; // 13
		// 14

		LineCalculator lineCalculator = LineCalculator.get(text);

		assertEquals(14, lineCalculator.getLineCount());

		{
			int i = 1;
			int c = 0;
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 0: \r
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 1: \n
			i++;
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 2: 1
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 3: 1
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 4: 1
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 5: 1
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 6: 1
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 7: \r
			i++;
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 8: 1
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 9: 1
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 10: 1
			assertEquals(i, lineCalculator.getLineNumber(c++)); // 11: \r

			// 最後の文字以降は最後の行扱い
			assertEquals(14, lineCalculator.getLineNumber(text.length())); // 47: \Z
			assertEquals(14, lineCalculator.getLineNumber(text.length() + 10)); // 57: null
		}

		{
			int l = 1;
			assertEquals(0, lineCalculator.getContentLength(l++)); // 1
			assertEquals(5, lineCalculator.getContentLength(l++)); // 2
			assertEquals(3, lineCalculator.getContentLength(l++)); // 3
			assertEquals(8, lineCalculator.getContentLength(l++)); // 4
			assertEquals(5, lineCalculator.getContentLength(l++)); // 5
			assertEquals(0, lineCalculator.getContentLength(l++)); // 6
			assertEquals(0, lineCalculator.getContentLength(l++)); // 7
			assertEquals(4, lineCalculator.getContentLength(l++)); // 8
			assertEquals(0, lineCalculator.getContentLength(l++)); // 9
			assertEquals(0, lineCalculator.getContentLength(l++)); // 10
			assertEquals(4, lineCalculator.getContentLength(l++)); // 11
			assertEquals(0, lineCalculator.getContentLength(l++)); // 12
			assertEquals(0, lineCalculator.getContentLength(l++)); // 13
			assertEquals(0, lineCalculator.getContentLength(l++)); // 14
		}

		assertEquals("11111", lineCalculator.getContent(2));
		assertEquals("", lineCalculator.getContent(6));
		assertEquals("", lineCalculator.getContent(9));
		assertEquals("", lineCalculator.getContent(14));

		{
			int l = 1;
			assertEquals(2, lineCalculator.getLineLength(l++));
			assertEquals(6, lineCalculator.getLineLength(l++));
			assertEquals(4, lineCalculator.getLineLength(l++));
			assertEquals(10, lineCalculator.getLineLength(l++));
			assertEquals(7, lineCalculator.getLineLength(l++));
			assertEquals(2, lineCalculator.getLineLength(l++));
			assertEquals(2, lineCalculator.getLineLength(l++));
			assertEquals(5, lineCalculator.getLineLength(l++));
			assertEquals(1, lineCalculator.getLineLength(l++));
			assertEquals(1, lineCalculator.getLineLength(l++));
			assertEquals(5, lineCalculator.getLineLength(l++));
			assertEquals(1, lineCalculator.getLineLength(l++));
			assertEquals(1, lineCalculator.getLineLength(l++));
			assertEquals(0, lineCalculator.getLineLength(l++));
		}

		assertEquals("11111\r", lineCalculator.getLine(2));
		assertEquals("\r\n", lineCalculator.getLine(6));
		assertEquals("\r", lineCalculator.getLine(9));
		assertEquals("", lineCalculator.getLine(14));

	}

}
