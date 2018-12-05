package mirrg.boron.util.logging.lib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.function.Supplier;

import mirrg.boron.util.logging.EnumLogLevel;
import mirrg.boron.util.logging.ILogHandler;

public class LogHandlerConsole implements ILogHandler
{

	private final Consumer<String> out;

	public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss.SSS");

	public LogHandlerConsole(Consumer<String> out)
	{
		this.out = out;
	}

	@Override
	public void log(EnumLogLevel logLevel, String name, Supplier<String> sMessage)
	{
		out.accept(String.format("%s %-5s [%s] %s",
			formatter.format(LocalDateTime.now()),
			logLevel,
			name,
			sMessage.get()));
	}

}
