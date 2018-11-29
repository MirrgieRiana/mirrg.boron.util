package mirrg.beryllium.logging.loggers.text;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import mirrg.boron.util.logging.EnumLogLevel;

public interface ILogFormatter
{

	public String format(String tag, String string, Optional<EnumLogLevel> oLogLevel);

	//

	public static final ILogFormatter DEFAULT = new LogFormatterSimple(DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss.SSS"));

}
