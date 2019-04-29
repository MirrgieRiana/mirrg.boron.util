package mirrg.boron.util.i18n;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

import mirrg.boron.util.i18n.lib.LocalizerEngine;
import mirrg.boron.util.i18n.lib.LocalizerResourceBundle;

public class TestI18n
{

	@Test
	public void test() throws IOException
	{

		// エンジンが解決できないものはそのまま表示される
		{
			I18n i18n = new I18n();

			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class.getPackage().getName() + ".test", Locale.forLanguageTag("ja-JP")));

			assertEquals("あ", i18n.localize("a"));
			assertEquals("い", i18n.localize("b"));
			assertEquals("う", i18n.localize("c"));
			assertEquals("d", i18n.localize("d"));
		}

		// エンジンを複数登録すると後に登録したものが優先される
		{
			I18n i18n = new I18n();

			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class.getPackage().getName() + ".test", Locale.forLanguageTag("en-US")));
			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class.getPackage().getName() + ".test", Locale.forLanguageTag("ja-JP")));

			assertEquals("あ", i18n.localize("a"));
			assertEquals("い", i18n.localize("b"));
			assertEquals("う", i18n.localize("c"));
			assertEquals("D", i18n.localize("d"));
			assertEquals("E", i18n.localize("e"));
			assertEquals("f", i18n.localize("f"));
		}

		// ローカライザーは動的に置き換えることができる
		// ローカライザーを置き換えるとイベントが通知される
		{
			I18n i18n = new I18n();

			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class.getPackage().getName() + ".test", Locale.forLanguageTag("en-US")));
			LocalizerEngine localizerEngine = new LocalizerEngine();
			i18n.registerLocalizerEngine(localizerEngine);

			StringBuilder sb = new StringBuilder();
			i18n.epChanged().register(() -> sb.append("1"));

			assertEquals("A", i18n.localize("a"));
			assertEquals("B", i18n.localize("b"));
			assertEquals("C", i18n.localize("c"));
			assertEquals("D", i18n.localize("d"));
			assertEquals("E", i18n.localize("e"));

			assertEquals("", sb.toString());
			localizerEngine.setLocalizer(LocalizerResourceBundle.create(TestI18n.class.getPackage().getName() + ".test", Locale.forLanguageTag("ja-JP")));
			assertEquals("1", sb.toString());

			assertEquals("あ", i18n.localize("a"));
			assertEquals("い", i18n.localize("b"));
			assertEquals("う", i18n.localize("c"));
			assertEquals("D", i18n.localize("d"));
			assertEquals("E", i18n.localize("e"));
		}

	}

}
