package mirrg.boron.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.function.IntFunction;

import mirrg.boron.util.suppliterator.ISuppliterator;

public class UtilsFile
{

	public static FileOutputStream getOutputStreamWithMkdirs(File file) throws FileNotFoundException
	{
		file.getAbsoluteFile().getParentFile().mkdirs();
		return new FileOutputStream(file);
	}

	//

	public static ISuppliterator<Path> getFilesRecursive(Path dir) throws IOException
	{
		ArrayList<Path> pathes = new ArrayList<>();

		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException
			{
				pathes.add(path);
				return FileVisitResult.CONTINUE;
			}
		});

		return ISuppliterator.ofIterable(pathes);
	}

	public static ISuppliterator<File> getFilesRecursive(File dir) throws IOException
	{
		return getFilesRecursive(dir.toPath())
			.map(p -> p.toFile());
	}

	//

	public static File roll(IntFunction<File> fFile, int max) throws FileNotFoundException
	{
		for (int i = 0; i <= max; i++) {
			File file = fFile.apply(i);
			if (!file.exists()) return file;
		}
		throw new FileNotFoundException("" + fFile.apply(0));
	}

	public static File roll(File file) throws FileNotFoundException
	{
		return roll(RollFunctions.simple("_", "", file), 99999);
	}

	public static class RollFunctions
	{

		public static IntFunction<File> simple(String prefix, String suffix, File file)
		{
			File parent = file.getAbsoluteFile().getParentFile();

			int index = file.getName().lastIndexOf('.');
			if (index != -1) {
				// 拡張子がある
				String extension = file.getName().substring(index);
				String body = file.getName().substring(0, index);
				return i -> i == 0 ? file : new File(parent, body + prefix + i + suffix + extension);
			} else {
				// 拡張子がない
				return i -> i == 0 ? file : new File(parent, file.getName() + prefix + i + suffix);
			}
		}

	}

}
