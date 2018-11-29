package mirrg.boron.util.logging;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * ログの回収のために各地に提供されるクラスです。
 * このクラスのインスタンスは {@link LogSink#logger(String)} によって提供されます。
 */
public final class Logger
{

	private final LogSink logSink;
	private final String tag;

	Logger(LogSink logSink, String tag)
	{
		this.logSink = logSink;
		this.tag = tag;
	}

	//////////////////////////

	public void println(Supplier<String> sString, Optional<EnumLogLevel> oLogLevel)
	{
		logSink.println(tag, sString, oLogLevel);
	}

	public void println(Supplier<String> sString)
	{
		println(sString, Optional.empty());
	}

	public void println(Supplier<String> sString, EnumLogLevel logLevel)
	{
		println(sString, Optional.of(logLevel));
	}

	public void fatal(Supplier<String> sString)
	{
		println(sString, EnumLogLevel.FATAL);
	}

	public void error(Supplier<String> sString)
	{
		println(sString, EnumLogLevel.ERROR);
	}

	public void warn(Supplier<String> sString)
	{
		println(sString, EnumLogLevel.WARN);
	}

	public void info(Supplier<String> sString)
	{
		println(sString, EnumLogLevel.INFO);
	}

	public void debug(Supplier<String> sString)
	{
		println(sString, EnumLogLevel.DEBUG);
	}

	public void trace(Supplier<String> sString)
	{
		println(sString, EnumLogLevel.TRACE);
	}

	//////////////////////////

	public void println(String string, Optional<EnumLogLevel> oLogLevel)
	{
		println(() -> string, oLogLevel);
	}

	public void println(String string)
	{
		println(() -> string);
	}

	public void println(String string, EnumLogLevel logLevel)
	{
		println(() -> string, logLevel);
	}

	public void fatal(String string)
	{
		fatal(() -> string);
	}

	public void error(String string)
	{
		error(() -> string);
	}

	public void warn(String string)
	{
		warn(() -> string);
	}

	public void info(String string)
	{
		info(() -> string);
	}

	public void debug(String string)
	{
		debug(() -> string);
	}

	public void trace(String string)
	{
		trace(() -> string);
	}

	//////////////////////////

	public void println(Throwable e, Optional<EnumLogLevel> oLogLevel)
	{
		logSink.println(tag, e, oLogLevel);
	}

	public void println(Throwable e)
	{
		println(e, Optional.empty());
	}

	public void println(Throwable e, EnumLogLevel logLevel)
	{
		println(e, Optional.of(logLevel));
	}

	public void fatal(Throwable e)
	{
		println(e, EnumLogLevel.FATAL);
	}

	public void error(Throwable e)
	{
		println(e, EnumLogLevel.ERROR);
	}

	public void warn(Throwable e)
	{
		println(e, EnumLogLevel.WARN);
	}

	public void info(Throwable e)
	{
		println(e, EnumLogLevel.INFO);
	}

	public void debug(Throwable e)
	{
		println(e, EnumLogLevel.DEBUG);
	}

	public void trace(Throwable e)
	{
		println(e, EnumLogLevel.TRACE);
	}

}
