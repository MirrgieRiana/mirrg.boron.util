package mirrg.boron.util.logging;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Test;

import mirrg.beryllium.logging.io.OutputStreamLogging;
import mirrg.beryllium.logging.loggers.text.LogSinkPrintWriter;

public class TestLogger
{

	@Test
	public void test_fromPrintWriter() throws Exception
	{

		// 出力先
		StringWriter out = new StringWriter();

		// ログ出力
		Logger logger = new LogSinkPrintWriter(new PrintWriter(out)).logger("Test");
		logger.fatal("001");
		logger.error("002");
		logger.warn("003");
		logger.info("004");
		logger.debug("005");
		logger.trace("006");

		assertTrue(out.toString().matches(""
			+ ".{23} \\[FATAL] \\[Test] 001" + System.lineSeparator()
			+ ".{23} \\[ERROR] \\[Test] 002" + System.lineSeparator()
			+ ".{23} \\[WARN]  \\[Test] 003" + System.lineSeparator()
			+ ".{23} \\[INFO]  \\[Test] 004" + System.lineSeparator()
			+ ".{23} \\[DEBUG] \\[Test] 005" + System.lineSeparator()
			+ ".{23} \\[TRACE] \\[Test] 006" + System.lineSeparator()));

	}

	@Test
	public void test_OutputStreamLogging() throws Exception
	{
		ArrayList<String> strings = new ArrayList<>();

		test0(strings, "UTF-8");
		test0(strings, "Shift-JIS");
		test0(strings, "Unicode");

		{
			try (PrintStream out2 = new PrintStream(new OutputStreamLogging(new LogSink() {
				@Override
				public void println(String tag, Supplier<String> sString, Optional<EnumLogLevel> oLogLevel)
				{
					strings.add("[" + tag + "] " + sString.get());
				}

				@Override
				public void println(String tag, Throwable e, Optional<EnumLogLevel> oLogLevel)
				{

				}
			}.logger("Test"), "Unicode"), true, "Unicode")) {

				out2.println("abc");
				assertEquals(1, strings.size());
				assertEquals("[Test] abc", strings.get(0));
				strings.clear();

				out2.println("def");
				assertEquals(1, strings.size());
				assertEquals("[Test] def", strings.get(0));
				strings.clear();

				out2.println("ghi");
				assertEquals(1, strings.size());
				assertEquals("[Test] ghi", strings.get(0));
				strings.clear();

			}

			assertEquals(1, strings.size());
			assertEquals("[Test] ", strings.get(0));
			strings.clear();
		}

	}

	private void test0(ArrayList<String> strings, String charset) throws UnsupportedEncodingException
	{
		try (PrintStream out2 = new PrintStream(new OutputStreamLogging(new LogSink() {
			@Override
			public void println(String tag, Supplier<String> sString, Optional<EnumLogLevel> oLogLevel)
			{
				strings.add("[" + tag + "] " + sString.get());
			}

			@Override
			public void println(String tag, Throwable e, Optional<EnumLogLevel> oLogLevel)
			{

			}
		}.logger("Test"), charset), true, charset)) {

			out2.print("あいうえお\nかきく\rけ\r\nこ");
			out2.flush();
			assertEquals(3, strings.size());
			assertEquals("[Test] あいうえお", strings.get(0));
			assertEquals("[Test] かきく", strings.get(1));
			assertEquals("[Test] け", strings.get(2));
			strings.clear();

		}

		assertEquals(1, strings.size());
		assertEquals("[Test] こ", strings.get(0));
		strings.clear();
	}

}
