package mirrg.boron.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;

public interface ILogHandler
{

	public void log(EnumLogLevel logLevel, String name, Supplier<String> sMessage);

	public default void log(EnumLogLevel logLevel, String name, Supplier<String> sMessage, Throwable e)
	{
		log(logLevel, name, sMessage);
		log(logLevel, name, e);
	}

	public default void log(EnumLogLevel logLevel, String name, Throwable e)
	{
		StringWriter out = new StringWriter();
		e.printStackTrace(new PrintWriter(out));
		log(logLevel, name, () -> out.toString());
	}

	public default Logger getLogger(String name)
	{
		return new Logger(this, name);
	}

	public default Logger getLogger(Class<?> clazz)
	{
		return getLogger(clazz.getName());
	}

}
