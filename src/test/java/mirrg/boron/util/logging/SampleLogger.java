package mirrg.boron.util.logging;

import mirrg.boron.util.logging.lib.LogHandlerConsole;

public class SampleLogger
{

	public static void main(String[] args)
	{
		new LogHandlerConsole(System.out::println).getLogger("Main").info("a", new RuntimeException("b"));
	}

}
