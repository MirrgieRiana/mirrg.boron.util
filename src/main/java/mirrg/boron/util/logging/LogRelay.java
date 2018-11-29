package mirrg.boron.util.logging;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * ログを複数の出力先に分配するクラスです。
 */
public final class LogRelay extends LogSink
{

	public LogRelay(LogSink... logSinks)
	{
		for (LogSink logSink : logSinks) {
			register(logSink);
		}
	}

	//

	private ArrayList<LogSink> logSinks = new ArrayList<>();

	public void register(LogSink logSink)
	{
		logSinks.add(logSink);
	}

	public void remove(LogSink logSink)
	{
		logSinks.remove(logSink);
	}

	//

	@Override
	public void println(String tag, Supplier<String> sString, Optional<EnumLogLevel> oLogLevel)
	{
		for (LogSink logSink : logSinks) {
			logSink.println(tag, sString, oLogLevel);
		}
	}

	@Override
	public void println(String tag, Throwable e, Optional<EnumLogLevel> oLogLevel)
	{
		for (LogSink logSink : logSinks) {
			logSink.println(tag, e, oLogLevel);
		}
	}

}
