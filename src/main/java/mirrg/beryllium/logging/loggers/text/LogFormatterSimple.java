package mirrg.beryllium.logging.loggers.text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import mirrg.boron.util.logging.EnumLogLevel;

public class LogFormatterSimple implements ILogFormatter
{

	private DateTimeFormatter formatter;

	public LogFormatterSimple(DateTimeFormatter formatter)
	{
		this.formatter = formatter;
	}

	@Override
	public String format(String tag, String string, Optional<EnumLogLevel> oLogLevel)
	{
		return String.format("%s %-7s [%s] %s",
			formatter.format(LocalDateTime.now()),
			oLogLevel
				.map(l -> "[" + l.name() + "]")
				.orElse(""),
			tag,
			string);
	}

}
