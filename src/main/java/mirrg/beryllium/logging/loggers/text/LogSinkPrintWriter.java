package mirrg.beryllium.logging.loggers.text;

import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.Supplier;

import mirrg.boron.util.logging.EnumLogLevel;

public class LogSinkPrintWriter extends LogSinkTextBase
{

	private PrintWriter out;

	public LogSinkPrintWriter(PrintWriter out)
	{
		this.out = out;
	}

	@Override
	public void println(String tag, Supplier<String> sString, Optional<EnumLogLevel> oLogLevel)
	{
		out.println(formatter.format(tag, sString.get(), oLogLevel));
	}

}
