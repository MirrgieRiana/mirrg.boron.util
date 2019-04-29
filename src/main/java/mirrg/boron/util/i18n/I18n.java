package mirrg.boron.util.i18n;

import java.util.ArrayDeque;
import java.util.Deque;

import mirrg.boron.util.event.lib.EventProviderRunnable;
import mirrg.boron.util.i18n.lib.LocalizerEngine;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class I18n
{

	/**
	 * 優先度の高い順に格納されている。
	 */
	private Deque<ILocalizerEngine> localizerEngines = new ArrayDeque<>();

	/**
	 * 現在登録されているローカライザーを優先度の高い順に返します。
	 */
	public synchronized ISuppliterator<ILocalizer> getLocalizers()
	{
		return ISuppliterator.ofIterable(localizerEngines)
			.mapIfPresent(le -> le.getLocalizer());
	}

	/**
	 * ローカライザーを最も優先度の高いものとして追加します。
	 */
	public synchronized void registerLocalizer(ILocalizer localizer)
	{
		registerLocalizerEngine(new LocalizerEngine(localizer));
	}

	/**
	 * ローカライザーエンジンを最も優先度の高いものとして追加します。
	 */
	public synchronized void registerLocalizerEngine(ILocalizerEngine localizerEngine)
	{
		localizerEngines.addFirst(localizerEngine);
		localizerEngine.epChanged().register(() -> epChanged.trigger().run());
	}

	private EventProviderRunnable epChanged = new EventProviderRunnable();

	public EventProviderRunnable epChanged()
	{
		return epChanged;
	}

	public synchronized String localize(String unlocalizedString)
	{
		for (ILocalizer localizer : getLocalizers()) {
			if (localizer.canLocalize(unlocalizedString)) {
				return localizer.localize(unlocalizedString);
			}
		}
		System.err.println("[Warn] Unknown i18n key: " + unlocalizedString);
		return unlocalizedString;
	}

}
