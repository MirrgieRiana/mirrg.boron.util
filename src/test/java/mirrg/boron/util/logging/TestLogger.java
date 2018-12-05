package mirrg.boron.util.logging;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.junit.Test;

import mirrg.boron.util.suppliterator.ISuppliterator;

public class TestLogger
{

	@Test
	public void test1() throws Exception
	{

		// 出力先
		ArrayList<String> list = new ArrayList<>();

		// ログ出力
		Logger logger = new ILogHandler() {
			@Override
			public void log(EnumLogLevel logLevel, String name, Supplier<String> sMessage)
			{
				list.add(String.format("%-5s [%s] %s",
					logLevel,
					name,
					sMessage.get()));
			}
		}.getLogger("Test");

		logger.fatal("001");
		logger.error("002");
		logger.warn("003");
		logger.info("004");
		logger.debug("005");
		logger.trace("006");

		assertEquals(list, ISuppliterator.of(
			"FATAL [Test] 001",
			"ERROR [Test] 002",
			"WARN  [Test] 003",
			"INFO  [Test] 004",
			"DEBUG [Test] 005",
			"TRACE [Test] 006").toCollection(ArrayList::new));

	}

}
