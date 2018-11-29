package mirrg.boron.util.logging;

import java.util.Optional;
import java.util.function.Supplier;

public final class LogFilter extends LogSink
{

	private final LogSink logSink;
	private final EnumLogLevel logLevelMin;

	public LogFilter(LogSink logSink, EnumLogLevel logLevelMin)
	{
		this.logSink = logSink;
		this.logLevelMin = logLevelMin;
	}

	//

	@Override
	public void println(String tag, Supplier<String> sString, Optional<EnumLogLevel> oLogLevel)
	{
		if (oLogLevel.isPresent() && oLogLevel.get().level > logLevelMin.level) return;
		logSink.println(tag, sString, oLogLevel);
	}

	@Override
	public void println(String tag, Throwable e, Optional<EnumLogLevel> oLogLevel)
	{
		if (oLogLevel.isPresent() && oLogLevel.get().level > logLevelMin.level) return;
		logSink.println(tag, e, oLogLevel);
	}

}
