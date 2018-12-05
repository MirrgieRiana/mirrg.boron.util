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

}
