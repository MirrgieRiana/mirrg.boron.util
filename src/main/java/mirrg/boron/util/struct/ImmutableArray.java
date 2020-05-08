package mirrg.boron.util.struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mirrg.boron.util.suppliterator.ISuppliterator;
import mirrg.boron.util.suppliterator.SuppliteratorNullableBase;

public final class ImmutableArray<T> implements Iterable<T>, IntFunction<T>
{

	private static final ImmutableArray<?> EMPTY = ImmutableArray.of();

	private final T[] array;
	private boolean hasNull;

	private ImmutableArray(T[] array)
	{
		this.array = array;
		this.hasNull = false;
	}

	private ImmutableArray(T[] array, boolean hasNull)
	{
		this.array = array;
		this.hasNull = hasNull;
	}

	//

	@SuppressWarnings("unchecked")
	public static <T> ImmutableArray<T> empty()
	{
		return (ImmutableArray<T>) EMPTY;
	}

	@SafeVarargs
	public static <T> ImmutableArray<T> of(T... array)
	{
		return ofObjArray(array);
	}

	public static <T> ImmutableArray<T> ofObjArray(T[] array)
	{
		return ofObjArray(array, 0, array.length);
	}

	@SuppressWarnings("unchecked")
	public static <T> ImmutableArray<T> ofObjArray(T[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		T[] array2 = (T[]) new Object[length];
		boolean hasNull = false;
		Class<?> clazz = array.getClass().getComponentType();
		for (int i = 0; i < length; i++) {
			T t = array[i + start];
			if (t != null) {
				if (!clazz.isInstance(t)) {
					throw new ClassCastException();
				}
			} else {
				hasNull = true;
			}
			array2[i] = t;
		}
		return new ImmutableArray<>(array2, hasNull);
	}

	@Deprecated// TODO 削除
	public static ImmutableArray<Byte> ofArray(byte... array)
	{
		return ofByteArray(array);
	}

	public static ImmutableArray<Byte> ofBytes(byte... array)
	{
		return ofByteArray(array);
	}

	public static ImmutableArray<Byte> ofByteArray(byte[] array)
	{
		return ofByteArray(array, 0, array.length);
	}

	public static ImmutableArray<Byte> ofByteArray(byte[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		Byte[] array2 = new Byte[length];
		for (int i = 0; i < length; i++) {
			array2[i] = array[i + start];
		}
		return new ImmutableArray<>(array2);
	}

	@Deprecated// TODO 削除
	public static ImmutableArray<Character> ofArray(char... array)
	{
		return ofCharArray(array);
	}

	public static ImmutableArray<Character> ofCharacters(char... array)
	{
		return ofCharArray(array);
	}

	public static ImmutableArray<Character> ofCharArray(char[] array)
	{
		return ofCharArray(array, 0, array.length);
	}

	public static ImmutableArray<Character> ofCharArray(char[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		Character[] array2 = new Character[length];
		for (int i = 0; i < length; i++) {
			array2[i] = array[i + start];
		}
		return new ImmutableArray<>(array2);
	}

	@Deprecated// TODO 削除
	public static ImmutableArray<Short> ofArray(short... array)
	{
		return ofShortArray(array);
	}

	public static ImmutableArray<Short> ofShorts(short... array)
	{
		return ofShortArray(array);
	}

	public static ImmutableArray<Short> ofShortArray(short[] array)
	{
		return ofShortArray(array, 0, array.length);
	}

	public static ImmutableArray<Short> ofShortArray(short[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		Short[] array2 = new Short[length];
		for (int i = 0; i < length; i++) {
			array2[i] = array[i + start];
		}
		return new ImmutableArray<>(array2);
	}

	@Deprecated// TODO 削除
	public static ImmutableArray<Integer> ofArray(int... array)
	{
		return ofIntArray(array);
	}

	public static ImmutableArray<Integer> ofIntegers(int... array)
	{
		return ofIntArray(array);
	}

	public static ImmutableArray<Integer> ofIntArray(int[] array)
	{
		return ofIntArray(array, 0, array.length);
	}

	public static ImmutableArray<Integer> ofIntArray(int[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		Integer[] array2 = new Integer[length];
		for (int i = 0; i < length; i++) {
			array2[i] = array[i + start];
		}
		return new ImmutableArray<>(array2);
	}

	@Deprecated// TODO 削除
	public static ImmutableArray<Long> ofArray(long... array)
	{
		return ofLongArray(array);
	}

	public static ImmutableArray<Long> ofLongs(long... array)
	{
		return ofLongArray(array);
	}

	public static ImmutableArray<Long> ofLongArray(long[] array)
	{
		return ofLongArray(array, 0, array.length);
	}

	public static ImmutableArray<Long> ofLongArray(long[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		Long[] array2 = new Long[length];
		for (int i = 0; i < length; i++) {
			array2[i] = array[i + start];
		}
		return new ImmutableArray<>(array2);
	}

	@Deprecated// TODO 削除
	public static ImmutableArray<Float> ofArray(float... array)
	{
		return ofFloatArray(array);
	}

	public static ImmutableArray<Float> ofFloats(float... array)
	{
		return ofFloatArray(array);
	}

	public static ImmutableArray<Float> ofFloatArray(float[] array)
	{
		return ofFloatArray(array, 0, array.length);
	}

	public static ImmutableArray<Float> ofFloatArray(float[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		Float[] array2 = new Float[length];
		for (int i = 0; i < length; i++) {
			array2[i] = array[i + start];
		}
		return new ImmutableArray<>(array2);
	}

	@Deprecated// TODO 削除
	public static ImmutableArray<Double> ofArray(double... array)
	{
		return ofDoubleArray(array);
	}

	public static ImmutableArray<Double> ofDoubles(double... array)
	{
		return ofDoubleArray(array);
	}

	public static ImmutableArray<Double> ofDoubleArray(double[] array)
	{
		return ofDoubleArray(array, 0, array.length);
	}

	public static ImmutableArray<Double> ofDoubleArray(double[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		Double[] array2 = new Double[length];
		for (int i = 0; i < length; i++) {
			array2[i] = array[i + start];
		}
		return new ImmutableArray<>(array2);
	}

	@Deprecated// TODO 削除
	public static ImmutableArray<Boolean> ofArray(boolean... array)
	{
		return ofBoolArray(array);
	}

	public static ImmutableArray<Boolean> ofBooleans(boolean... array)
	{
		return ofBoolArray(array);
	}

	public static ImmutableArray<Boolean> ofBoolArray(boolean[] array)
	{
		return ofBoolArray(array, 0, array.length);
	}

	public static ImmutableArray<Boolean> ofBoolArray(boolean[] array, int start, int length)
	{
		checkRange(array.length, start, length);

		Boolean[] array2 = new Boolean[length];
		for (int i = 0; i < length; i++) {
			array2[i] = array[i + start];
		}
		return new ImmutableArray<>(array2);
	}

	static void checkRange(int lengthOfArray, int start, int length)
	{
		if (start < 0) throw new ArrayIndexOutOfBoundsException("" + start + " < 0");
		if (start > lengthOfArray) throw new ArrayIndexOutOfBoundsException("" + start + " > " + lengthOfArray);
		if (length < 0) throw new ArrayIndexOutOfBoundsException("" + length + " < " + 0);
		if (start + length > lengthOfArray) throw new ArrayIndexOutOfBoundsException("" + start + " + " + length + " > " + lengthOfArray);
	}

	//

	@SuppressWarnings("unchecked")
	public static <T> ImmutableArray<T> ofList(List<? extends T> array)
	{
		T[] array2 = (T[]) new Object[array.size()];
		int i = 0;
		for (T t : array) {
			array2[i] = t;
			i++;
		}
		return new ImmutableArray<>(array2);
	}

	public static <T> ImmutableArray<T> ofStream(Stream<? extends T> stream)
	{
		return ofList((List<? extends T>) stream.collect(Collectors.toCollection(ArrayList::new)));
	}

	public static <T> ImmutableArray<T> ofIterable(Iterable<? extends T> iterable)
	{
		ArrayList<T> list = new ArrayList<>();
		Iterator<? extends T> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return ofList(list);
	}

	public static <T> ImmutableArray<T> ofEnumeration(Enumeration<? extends T> enumeration)
	{
		ArrayList<T> list = new ArrayList<>();
		while (enumeration.hasMoreElements()) {
			list.add(enumeration.nextElement());
		}
		return ofList(list);
	}

	//

	public int length()
	{
		return array.length;
	}

	@Override
	public T apply(int index)
	{
		return array[index];
	}

	public T get(int index)
	{
		return array[index];
	}

	//

	public T[] toArray(IntFunction<T[]> arraySupplier)
	{
		T[] array2 = arraySupplier.apply(array.length);
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	public <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory)
	{
		C collection = collectionFactory.get();
		for (int i = 0; i < array.length; i++) {
			collection.add(array[i]);
		}
		return collection;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>() {
			private int i = 0;

			@Override
			public boolean hasNext()
			{
				return i < array.length;
			}

			@Override
			public T next()
			{
				return array[i++];
			}
		};
	}

	public ISuppliterator<T> suppliterator()
	{
		return new SuppliteratorNullableBase<T>() {
			private int i = 0;

			@Override
			public T nullableNextImpl()
			{
				int i2 = i;
				i++;
				return i2 < array.length ? array[i2] : null;
			}
		};
	}

	/**
	 * @throws NullPointerException
	 *             この配列がnull要素を持つ場合
	 */
	public ISuppliterator<T> suppliterator(int start, int length)
	{
		if (hasNull) throw new NullPointerException();
		return new SuppliteratorNullableBase<T>() {
			private int i = start;

			@Override
			public T nullableNextImpl()
			{
				int i2 = i;
				i++;
				return i2 < start + length ? array[i2] : null;
			}
		};
	}

	public Enumeration<T> items()
	{
		return new Enumeration<T>() {

			private int i = 0;

			@Override
			public boolean hasMoreElements()
			{
				return i < array.length;
			}

			@Override
			public T nextElement()
			{
				return array[i++];
			}

		};
	}

	/**
	 * @deprecated 代わりに {@link #items()} を使用してください。
	 */
	@Deprecated // TODO delete
	public Enumeration<T> values()
	{
		return items();
	}

	public Stream<T> stream()
	{
		return Stream.of(array);
	}

	// TODO 削除：重複メソッド
	@Deprecated
	public ISuppliterator<T> toSuppliterator()
	{
		return ISuppliterator.ofIterable(this);
	}

	// TODO 名称変更：ArrayList#add(T, int)とあいまいになる
	@Deprecated
	public void forEach(ObjIntConsumer<T> consumer)
	{
		for (int i = 0; i < array.length; i++) {
			consumer.accept(array[i], i);
		}
	}

	//

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) sb.append(", ");
			sb.append(array[i].toString());
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		for (int i = 0; i < array.length; i++) {
			result = prime * result + ((array[i] == null) ? 0 : array[i].hashCode());
		}
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ImmutableArray<?> other = (ImmutableArray<?>) obj;
		if (array.length != other.array.length) return false;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				if (other.array[i] != null) return false;
			} else if (!array[i].equals(other.array[i])) return false;
		}
		return true;
	}

}
