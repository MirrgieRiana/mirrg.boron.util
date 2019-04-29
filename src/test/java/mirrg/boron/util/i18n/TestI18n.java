package mirrg.boron.util.i18n;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import mirrg.boron.util.i18n.lib.LocalizerEngine;
import mirrg.boron.util.i18n.lib.LocalizerResourceBundle;

public class TestI18n
{

	@Test
	public void test() throws IOException
	{

		// 翻訳ファイルはUTF8で記述できる
		// クラスパス上の任意のパッケージを指定できる
		// エンジンが解決できないものはそのまま表示される
		{
			I18n i18n = new I18n();

			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class, "test", "ja-JP"));
			// "mirrg/boron/util/i18n"に"test_ja.properties"を置けばよい

			assertEquals("あ", i18n.localize("a"));
			assertEquals("い", i18n.localize("b"));
			assertEquals("う", i18n.localize("c"));
			assertEquals("d", i18n.localize("d"));
		}

		// エンジンを複数登録すると後に登録したものが優先される
		{
			I18n i18n = new I18n();

			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class, "test", "en-US"));
			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class, "test", "ja-JP"));

			assertEquals("あ", i18n.localize("a"));
			assertEquals("い", i18n.localize("b"));
			assertEquals("う", i18n.localize("c"));
			assertEquals("D", i18n.localize("d"));
			assertEquals("E", i18n.localize("e"));
			assertEquals("f", i18n.localize("f"));
		}

		// 不正なエンジンを登録しても無視される
		{
			I18n i18n = new I18n();

			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class, "test", "en-US"));
			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class, "test", "ja-JP"));
			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class, "test", "gr-XQ"));

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

			i18n.registerLocalizer(LocalizerResourceBundle.create(TestI18n.class, "test", "en-US"));
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
			localizerEngine.setLocalizer(LocalizerResourceBundle.create(TestI18n.class, "test", "ja-JP"));
			assertEquals("1", sb.toString());

			assertEquals("あ", i18n.localize("a"));
			assertEquals("い", i18n.localize("b"));
			assertEquals("う", i18n.localize("c"));
			assertEquals("D", i18n.localize("d"));
			assertEquals("E", i18n.localize("e"));
		}

	}

}
