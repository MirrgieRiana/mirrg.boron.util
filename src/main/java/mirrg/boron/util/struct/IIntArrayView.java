package mirrg.boron.util.struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import mirrg.boron.util.struct.lib.IntArray;
import mirrg.boron.util.suppliterator.ISuppliterator;

/**
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
public interface IIntArrayView extends Iterable<Integer>
{

	/**
	 * 配列の長さが変わることは原則としてありません。
	 */
	public int length();

	/**
	 * 要素の値が変わるかどうかは、実装クラスに依存します。
	 */
	public int get(int index);

	public void copyTo(int startSrc, int[] dest, int startDest, int length);

	public default void checkRange(int lengthOfArray, int start, int length)
	{
		if (start < 0) throw new ArrayIndexOutOfBoundsException("" + start + " < 0");
		if (start > lengthOfArray) throw new ArrayIndexOutOfBoundsException("" + start + " > " + lengthOfArray);
		if (length < 0) throw new ArrayIndexOutOfBoundsException("" + length + " < 0");
		if (start + length > lengthOfArray) throw new ArrayIndexOutOfBoundsException("" + start + " + " + length + " > " + lengthOfArray);
	}

	// ■■■■■■■■ 中間操作

	/**
	 * このメソッドは遅延評価かつメモ化されるビューを返します。
	 * このメソッドによって返された配列の要素が一度評価されるまでは、もとの配列の要素は参照されません。
	 * このメソッドによって返された配列の要素に一度アクセスすると、配列内に値を保持し、二度目以降のアクセス時にはそれを返します。
	 */
	@Deprecated // 実験的。 TODO test
	public default IIntArrayView map(IntUnaryOperator function)
	{
		boolean[] initialized = new boolean[length()];
		int[] array = new int[length()];
		IIntArrayView parent = this;
		return new IIntArrayView() {
			@Override
			public int length()
			{
				return parent.length();
			}

			@Override
			public int get(int index)
			{
				if (!initialized[index]) {
					initialized[index] = true;
					array[index] = function.applyAsInt(parent.get(index));
				}
				return array[index];
			}

			@Override
			public void copyTo(int startSrc, int[] dest, int startDest, int length)
			{
				parent.copyTo(startSrc, dest, startDest, length);
				for (int i = startDest; i < startDest + length; i++) {
					dest[i] = function.applyAsInt(dest[i]);
				}
			}
		};
	}

	@Deprecated // 実験的。 TODO test
	public default IIntArrayView subArray(int start, int length)
	{
		checkRange(length(), start, length);
		IIntArrayView parent = this;
		return new IIntArrayView() {
			@Override
			public int length()
			{
				return length;
			}

			@Override
			public int get(int index)
			{
				return parent.get(index + start);
			}

			@Override
			public void copyTo(int startSrc, int[] dest, int startDest, int length)
			{
				parent.copyTo(startSrc + start, dest, startDest, length);
			}
		};
	}

	// ■■■■■■■■ 終端操作

	@Deprecated // 実験的。 TODO test
	public default void forEachInt(IntConsumer consumer)
	{
		for (int i = 0; i < length(); i++) {
			consumer.accept(get(i));
		}
	}

	/**
	 * <code>join(",")</code>という呼び出しと同一です。
	 */
	public default String join()
	{
		return join(",");
	}

	public default String join(CharSequence separator)
	{
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (int i = 0; i < length(); i++) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(separator);
			}
			sb.append(get(i));
		}
		return sb.toString();
	}

	// ■■■■■■■■ 型変換

	@Deprecated // 実験的。 TODO test
	public default int[] toArray()
	{
		int[] array = new int[length()];
		copyTo(0, array, 0, length());
		return array;
	}

	@Deprecated // 実験的。 TODO test
	public default List<Integer> toList()
	{
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < length(); i++) {
			list.add(get(i));
		}
		return list;
	}

	@Deprecated // 実験的。 TODO test
	@Override
	public default Iterator<Integer> iterator()
	{
		return new Iterator<Integer>() {
			int i = 0;
			final int end = length();

			@Override
			public Integer next()
			{
				try {
					return get(i);
				} finally {
					i++;
				}
			}

			@Override
			public boolean hasNext()
			{
				return i >= end;
			}
		};
	}

	@Deprecated // 実験的。 TODO test
	public default IntStream stream()
	{
		return IntStream.range(0, length())
			.map(i -> get(i));
	}

	@Deprecated // 実験的。 TODO test
	public default ISuppliterator<Integer> suppliterator()
	{
		return ISuppliterator.range(0, length())
			.map(i -> get(i));
	}

	/**
	 * この配列ビューのコピーを作ります。
	 * このメソッドは、この配列ビューのすべての要素を評価します。
	 */
	@Deprecated // 実験的。 TODO test
	public default IntArray copy()
	{
		return new IntArray(toArray());
	}

}
