package mirrg.boron.util.logging;

import java.util.function.Supplier;

/**
 * ログの回収のために各地に提供されるクラスです。
 * このクラスのインスタンスは {@link ILogHandler#getLogger(String)} によって提供されます。
 */
public final class Logger
{

	private final ILogHandler logHandler;
	private final String name;

	Logger(ILogHandler logHandler, String name)
	{
		this.logHandler = logHandler;
		this.name = name;
	}

	//////////////////////////

	public final void fatal(Supplier<String> sMessage)
	{
		logHandler.log(EnumLogLevel.FATAL, name, sMessage);
	}

	public final void fatal(String message)
	{
		logHandler.log(EnumLogLevel.FATAL, name, () -> message);
	}

	public final void fatal(Supplier<String> sMessage, Throwable e)
	{
		logHandler.log(EnumLogLevel.FATAL, name, sMessage, e);
	}

	public final void fatal(String message, Throwable e)
	{
		logHandler.log(EnumLogLevel.FATAL, name, () -> message, e);
	}

	public final void fatal(Throwable e)
	{
		logHandler.log(EnumLogLevel.FATAL, name, e);
	}

	//

	public final void error(Supplier<String> sMessage)
	{
		logHandler.log(EnumLogLevel.ERROR, name, sMessage);
	}

	public final void error(String message)
	{
		logHandler.log(EnumLogLevel.ERROR, name, () -> message);
	}

	public final void error(Supplier<String> sMessage, Throwable e)
	{
		logHandler.log(EnumLogLevel.ERROR, name, sMessage, e);
	}

	public final void error(String message, Throwable e)
	{
		logHandler.log(EnumLogLevel.ERROR, name, () -> message, e);
	}

	public final void error(Throwable e)
	{
		logHandler.log(EnumLogLevel.ERROR, name, e);
	}

	//

	public final void warn(Supplier<String> sMessage)
	{
		logHandler.log(EnumLogLevel.WARN, name, sMessage);
	}

	public final void warn(String message)
	{
		logHandler.log(EnumLogLevel.WARN, name, () -> message);
	}

	public final void warn(Supplier<String> sMessage, Throwable e)
	{
		logHandler.log(EnumLogLevel.WARN, name, sMessage, e);
	}

	public final void warn(String message, Throwable e)
	{
		logHandler.log(EnumLogLevel.WARN, name, () -> message, e);
	}

	public final void warn(Throwable e)
	{
		logHandler.log(EnumLogLevel.WARN, name, e);
	}

	//

	public final void info(Supplier<String> sMessage)
	{
		logHandler.log(EnumLogLevel.INFO, name, sMessage);
	}

	public final void info(String message)
	{
		logHandler.log(EnumLogLevel.INFO, name, () -> message);
	}

	public final void info(Supplier<String> sMessage, Throwable e)
	{
		logHandler.log(EnumLogLevel.INFO, name, sMessage, e);
	}

	public final void info(String message, Throwable e)
	{
		logHandler.log(EnumLogLevel.INFO, name, () -> message, e);
	}

	public final void info(Throwable e)
	{
		logHandler.log(EnumLogLevel.INFO, name, e);
	}

	//

	public final void debug(Supplier<String> sMessage)
	{
		logHandler.log(EnumLogLevel.DEBUG, name, sMessage);
	}

	public final void debug(String message)
	{
		logHandler.log(EnumLogLevel.DEBUG, name, () -> message);
	}

	public final void debug(Supplier<String> sMessage, Throwable e)
	{
		logHandler.log(EnumLogLevel.DEBUG, name, sMessage, e);
	}

	public final void debug(String message, Throwable e)
	{
		logHandler.log(EnumLogLevel.DEBUG, name, () -> message, e);
	}

	public final void debug(Throwable e)
	{
		logHandler.log(EnumLogLevel.DEBUG, name, e);
	}

	//

	public final void trace(Supplier<String> sMessage)
	{
		logHandler.log(EnumLogLevel.TRACE, name, sMessage);
	}

	public final void trace(String message)
	{
		logHandler.log(EnumLogLevel.TRACE, name, () -> message);
	}

	public final void trace(Supplier<String> sMessage, Throwable e)
	{
		logHandler.log(EnumLogLevel.TRACE, name, sMessage, e);
	}

	public final void trace(String message, Throwable e)
	{
		logHandler.log(EnumLogLevel.TRACE, name, () -> message, e);
	}

	public final void trace(Throwable e)
	{
		logHandler.log(EnumLogLevel.TRACE, name, e);
	}

}
