package mirrg.boron.util.logging;

import static org.junit.Assert.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Test;

import mirrg.boron.util.logging.lib.LogHandlerConsole;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class TestLogger
{

	@Test
	public void test1() throws Exception
	{

		// 出力先
		ArrayList<String> list = new ArrayList<>();

		// ログ出力
		Logger logger = new LogHandlerConsole(list::add) {
			{
				formatter = DateTimeFormatter.ofPattern("*");
			}
		}.getLogger("Test");

		logger.fatal("001");
		logger.error("002");
		logger.warn("003");
		logger.info("004");
		logger.debug("005");
		logger.trace("006");

		assertEquals(ISuppliterator.of(
			"* FATAL [Test] 001",
			"* ERROR [Test] 002",
			"* WARN  [Test] 003",
			"* INFO  [Test] 004",
			"* DEBUG [Test] 005",
			"* TRACE [Test] 006").toCollection(ArrayList::new), list);

	}

}
