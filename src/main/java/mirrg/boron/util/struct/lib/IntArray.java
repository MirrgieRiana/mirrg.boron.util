package mirrg.boron.util.struct.lib;

import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import mirrg.boron.util.struct.IIntArrayView;
import mirrg.boron.util.suppliterator.ISuppliterator;

/**
 * 内部にint配列の実体への参照を持つint配列ビューです。
 */
public final class IntArray extends IntArrayBase
{

	public final int[] array;
	public final int start;
	public final int length;
	private final boolean just;

	public IntArray(int length)
	{
		this(new int[length]);
	}

	public IntArray(int[] array)
	{
		this(array, 0, array.length);
	}

	public IntArray(int[] array, int start, int length)
	{
		checkRange(array.length, start, length);
		this.array = array;
		this.start = start;
		this.length = length;
		this.just = start == 0 && length == array.length;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		for (int i = start; i < start + length; i++) {
			result = 31 * result + array[i];
		}
		return result;
	}

	//

	@Override
	public int length()
	{
		return length;
	}

	@Override
	public int get(int index)
	{
		return array[index + start];
	}

	@Deprecated // 実験的。 TODO test
	public void set(int index, int value)
	{
		array[index + start] = value;
	}

	@Override
	public void copyTo(int startSrc, int[] dest, int startDest, int length)
	{
		checkRange(this.length, startSrc, length);
		checkRange(dest.length, startDest, length);
		System.arraycopy(array, startSrc + start, dest, startDest, length);
	}

	@Deprecated // 実験的。 TODO test
	public void copyTo(int startSrc, IntArray dest, int startDest, int length)
	{
		checkRange(this.length, startSrc, length);
		checkRange(dest.length, startDest, length);
		System.arraycopy(array, startSrc + start, dest.array, startDest + dest.start, length);
	}

	@Deprecated // 実験的。 TODO test
	public void copyFrom(int[] src, int startSrc, int startDest, int length)
	{
		checkRange(src.length, startSrc, length);
		checkRange(this.length, startDest, length);
		System.arraycopy(src, startSrc, array, startDest + start, length);
	}

	//

	@Deprecated // 実験的。 TODO test
	@Override
	public IIntArrayView subArray(int start, int length)
	{
		checkRange(this.length, start, length);
		return new IntArray(array, this.start + start, length);
	}

	//

	@Deprecated // 実験的。 TODO test
	@Override
	public void forEachInt(IntConsumer consumer)
	{
		for (int i = 0; i < length; i++) {
			consumer.accept(array[i]);
		}
	}

	//

	@Deprecated // 実験的。 TODO test
	@Override
	public IntStream stream()
	{
		if (just) return IntStream.of(array);
		return super.stream();
	}

	@Deprecated // 実験的。 TODO test
	@Override
	public ISuppliterator<Integer> suppliterator()
	{
		return ISuppliterator.ofIntArray(array, 0, length);
	}

}
