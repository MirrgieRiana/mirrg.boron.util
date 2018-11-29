package mirrg.beryllium.logging.loggers.text;

import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Supplier;

import mirrg.boron.util.logging.EnumLogLevel;

public class LogSinkPrintStream extends LogSinkTextBase
{

	private PrintStream out;

	public LogSinkPrintStream(PrintStream out)
	{
		this.out = out;
	}

	@Override
	public void println(String tag, Supplier<String> sString, Optional<EnumLogLevel> oLogLevel)
	{
		out.println(formatter.format(tag, sString.get(), oLogLevel));
	}

}
