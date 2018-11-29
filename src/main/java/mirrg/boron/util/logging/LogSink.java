package mirrg.boron.util.logging;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * ログの出力先です。
 * 実際にログを取るには、 {@link #logger(String)} を呼び出して得られるロガークラスを経由してください。
 */
public abstract class LogSink
{

	/**
	 * @param string
	 *            複数行である場合があります。
	 *            その場合、改行コードは {@link System#lineSeparator()} に依存します。
	 */
	public abstract void println(String tag, Supplier<String> sString, Optional<EnumLogLevel> oLogLevel);

	public abstract void println(String tag, Throwable e, Optional<EnumLogLevel> oLogLevel);

	public final Logger logger(String tag)
	{
		return new Logger(this, tag);
	}

	public final LogSink filter(EnumLogLevel logLevel)
	{
		return new LogFilter(this, logLevel);
	}

}
