package mirrg.boron.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class TestUtilsFile
{

	@Test
	public void test_roll() throws IOException
	{
		File dir = new File("src/test/resources/" + TestUtilsFile.class.getPackage().getName().replace('.', '/'));

		// 拡張子無しファイル
		{
			File file = new File(dir, "a");

			// 普通に呼び出すと99999まで生成する
			assertEquals(new File(dir, "a_11").getAbsoluteFile(), UtilsFile.roll(file));

			// 10まで埋まっているときにmaxを10で呼び出すと失敗する
			try {
				UtilsFile.roll(UtilsFile.RollFunctions.simple("_", "", file), 10);
				fail();
			} catch (FileNotFoundException e) {

			}

			// 11なら11が返ってくる
			assertEquals(new File(dir, "a_11").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("_", "", file), 11));

			// プレフィクスが違えばヒットしないのでセーフ
			assertEquals(new File(dir, "a-1").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("-", "", file), 11));
			assertEquals(new File(dir, "a_1-").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("_", "-", file), 11));

		}

		// 拡張子ありファイル
		{
			File file = new File(dir, "a.txt");

			// 拡張子の前に番号が振られる
			assertEquals(new File(dir, "a_11.txt").getAbsoluteFile(), UtilsFile.roll(file));
			assertEquals(new File(dir, "a_11.txt").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("_", "", file), 11));
			assertEquals(new File(dir, "a-1.txt").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("-", "", file), 11));
			assertEquals(new File(dir, "a_1-.txt").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("_", "-", file), 11));

		}

		// 二重拡張子ファイル
		{
			File file = new File(dir, "a.txt.csv");

			// 最後の拡張子の直前に番号が振られる
			assertEquals(new File(dir, "a.txt_11.csv").getAbsoluteFile(), UtilsFile.roll(file));
			assertEquals(new File(dir, "a.txt_11.csv").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("_", "", file), 11));
			assertEquals(new File(dir, "a.txt-1.csv").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("-", "", file), 11));
			assertEquals(new File(dir, "a.txt_1-.csv").getAbsoluteFile(), UtilsFile.roll(UtilsFile.RollFunctions.simple("_", "-", file), 11));

		}

	}

}
