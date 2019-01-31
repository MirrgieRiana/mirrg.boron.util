package mirrg.boron.util.suppliterator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import mirrg.boron.util.UtilsLambda;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;

/**
 * {@link Enumeration} や {@link Iterator} がもつメソッドを1個にまとめたものです。
 * ただし、このインターフェースは非nullの値のみが流れます。
 * 並列処理はサポートしまていせん。
 */
public interface ISuppliterator<T> extends Iterable<T>
{

	/**
	 * このメソッドがemptyを返した場合、以降このメソッドを呼び出してはなりません。
	 *
	 * @return
	 * 		列挙がリストの末端に達して要素が存在しない場合、empty。
	 * @throws NoSuchElementException
	 *             リストの末尾以降にメソッドを呼び出したとき。
	 */
	public Optional<T> next() throws NoSuchElementException;

	/**
	 * このメソッドがnullを返した場合、以降このメソッドを呼び出してはなりません。
	 *
	 * @return
	 * 		列挙がリストの末端に達して要素が存在しない場合、null。
	 * @throws NoSuchElementException
	 *             リストの末尾以降にメソッドを呼び出したとき。
	 */
	public T nullableNext() throws NoSuchElementException;

	// 生成

	/**
	 * @param array
	 *            nullを含まないTの配列
	 */
	@SafeVarargs
	public static <T> ISuppliterator<T> of(T... array)
	{
		return ofObjArray(array);
	}

	/**
	 * @param array
	 *            nullを含まないTの配列
	 */
	public static <T> ISuppliterator<T> ofObjArray(T[] array)
	{
		return ofObjArray(array, 0, array.length);
	}

	/**
	 * @param array
	 *            nullを含まないTの配列
	 */
	public static <T> ISuppliterator<T> ofObjArray(T[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<T>() {
			private int index = start;

			@Override
			public T nullableNextImpl()
			{
				if (index < start + length) {
					T next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//

	public static ISuppliterator<Byte> ofBytes(byte... array)
	{
		return ofByteArray(array);
	}

	// TODO on version up
	/**
	 * このメソッドは将来削除されます。
	 */
	@Deprecated
	public static ISuppliterator<Byte> ofArray(byte... array)
	{
		return ofByteArray(array, 0, array.length);
	}

	public static ISuppliterator<Byte> ofByteArray(byte[] array)
	{
		return ofByteArray(array, 0, array.length);
	}

	public static ISuppliterator<Byte> ofByteArray(byte[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<Byte>() {
			private int index = start;

			@Override
			public Byte nullableNextImpl()
			{
				if (index < start + length) {
					byte next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//

	public static ISuppliterator<Character> ofCharacters(char... array)
	{
		return ofCharArray(array);
	}

	// TODO on version up
	/**
	 * このメソッドは将来削除されます。
	 */
	@Deprecated
	public static ISuppliterator<Character> ofArray(char... array)
	{
		return ofCharArray(array, 0, array.length);
	}

	public static ISuppliterator<Character> ofCharArray(char[] array)
	{
		return ofCharArray(array, 0, array.length);
	}

	public static ISuppliterator<Character> ofCharArray(char[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<Character>() {
			private int index = 0;

			@Override
			public Character nullableNextImpl()
			{
				if (index < array.length) {
					char next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//

	public static ISuppliterator<Short> ofShorts(short... array)
	{
		return ofShortArray(array);
	}

	// TODO on version up
	/**
	 * このメソッドは将来削除されます。
	 */
	@Deprecated
	public static ISuppliterator<Short> ofArray(short... array)
	{
		return ofShortArray(array, 0, array.length);
	}

	public static ISuppliterator<Short> ofShortArray(short[] array)
	{
		return ofShortArray(array, 0, array.length);
	}

	public static ISuppliterator<Short> ofShortArray(short[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<Short>() {
			private int index = 0;

			@Override
			public Short nullableNextImpl()
			{
				if (index < array.length) {
					short next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//

	public static ISuppliterator<Integer> ofIntegers(int... array)
	{
		return ofIntArray(array);
	}

	// TODO on version up
	/**
	 * このメソッドは将来削除されます。
	 */
	@Deprecated
	public static ISuppliterator<Integer> ofArray(int... array)
	{
		return ofIntArray(array, 0, array.length);
	}

	public static ISuppliterator<Integer> ofIntArray(int[] array)
	{
		return ofIntArray(array, 0, array.length);
	}

	public static ISuppliterator<Integer> ofIntArray(int[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<Integer>() {
			private int index = 0;

			@Override
			public Integer nullableNextImpl()
			{
				if (index < array.length) {
					int next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//

	public static ISuppliterator<Long> ofLongs(long... array)
	{
		return ofLongArray(array);
	}

	// TODO on version up
	/**
	 * このメソッドは将来削除されます。
	 */
	@Deprecated
	public static ISuppliterator<Long> ofArray(long... array)
	{
		return ofLongArray(array, 0, array.length);
	}

	public static ISuppliterator<Long> ofLongArray(long[] array)
	{
		return ofLongArray(array, 0, array.length);
	}

	public static ISuppliterator<Long> ofLongArray(long[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<Long>() {
			private int index = 0;

			@Override
			public Long nullableNextImpl()
			{
				if (index < array.length) {
					long next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//

	public static ISuppliterator<Float> ofFloats(float... array)
	{
		return ofFloatArray(array);
	}

	// TODO on version up
	/**
	 * このメソッドは将来削除されます。
	 */
	@Deprecated
	public static ISuppliterator<Float> ofArray(float... array)
	{
		return ofFloatArray(array, 0, array.length);
	}

	public static ISuppliterator<Float> ofFloatArray(float[] array)
	{
		return ofFloatArray(array, 0, array.length);
	}

	public static ISuppliterator<Float> ofFloatArray(float[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<Float>() {
			private int index = 0;

			@Override
			public Float nullableNextImpl()
			{
				if (index < array.length) {
					float next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//

	public static ISuppliterator<Double> ofDoubles(double... array)
	{
		return ofDoubleArray(array);
	}

	// TODO on version up
	/**
	 * このメソッドは将来削除されます。
	 */
	@Deprecated
	public static ISuppliterator<Double> ofArray(double... array)
	{
		return ofDoubleArray(array, 0, array.length);
	}

	public static ISuppliterator<Double> ofDoubleArray(double[] array)
	{
		return ofDoubleArray(array, 0, array.length);
	}

	public static ISuppliterator<Double> ofDoubleArray(double[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<Double>() {
			private int index = 0;

			@Override
			public Double nullableNextImpl()
			{
				if (index < array.length) {
					double next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//

	public static ISuppliterator<Boolean> ofBooleans(boolean... array)
	{
		return ofBoolArray(array);
	}

	// TODO on version up
	/**
	 * このメソッドは将来削除されます。
	 */
	@Deprecated
	public static ISuppliterator<Boolean> ofArray(boolean... array)
	{
		return ofBoolArray(array, 0, array.length);
	}

	public static ISuppliterator<Boolean> ofBoolArray(boolean[] array)
	{
		return ofBoolArray(array, 0, array.length);
	}

	public static ISuppliterator<Boolean> ofBoolArray(boolean[] array, int start, int length)
	{
		ISuppliteratorLocal.checkRange(array.length, start, length);

		return new SuppliteratorNullableBase<Boolean>() {
			private int index = 0;

			@Override
			public Boolean nullableNextImpl()
			{
				if (index < array.length) {
					boolean next = array[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	//////////////

	/**
	 * @param iterator
	 *            nullを含まないTのIterator
	 */
	public static <T> ISuppliterator<T> ofIterator(Iterator<T> iterator)
	{
		return new SuppliteratorNullableBase<T>() {
			@Override
			public T nullableNextImpl()
			{
				return iterator.hasNext() ? iterator.next() : null;
			}
		};
	}

	/**
	 * @param iterable
	 *            nullを含まないTのIterable
	 */
	public static <T> ISuppliterator<T> ofIterable(Iterable<T> iterable)
	{
		return ofIterator(iterable.iterator());
	}

	/**
	 * @param enumeration
	 *            nullを含まないTのEnumeration
	 */
	public static <T> ISuppliterator<T> ofEnumeration(Enumeration<T> enumeration)
	{
		return ofIterator(new Iterator<T>() {
			@Override
			public T next()
			{
				return enumeration.nextElement();
			}

			@Override
			public boolean hasNext()
			{
				return enumeration.hasMoreElements();
			}
		});
	}

	/**
	 * このメソッドは呼び出し時にストリームのすべての要素にアクセスします。
	 *
	 * @param stream
	 *            nullを含まないTのStream
	 */
	public static <T> ISuppliterator<T> ofStream(Stream<T> stream)
	{
		return ofIterable(stream.collect(Collectors.toCollection(ArrayList::new)));
	}

	/**
	 * このメソッドは呼び出し時にストリームのすべての要素にアクセスします。
	 */
	public static ISuppliterator<Integer> ofStream(IntStream stream)
	{
		return ofStream(stream.mapToObj(x -> x));
	}

	/**
	 * このメソッドは呼び出し時にストリームのすべての要素にアクセスします。
	 */
	public static ISuppliterator<Long> ofStream(LongStream stream)
	{
		return ofStream(stream.mapToObj(x -> x));
	}

	/**
	 * このメソッドは呼び出し時にストリームのすべての要素にアクセスします。
	 */
	public static ISuppliterator<Double> ofStream(DoubleStream stream)
	{
		return ofStream(stream.mapToObj(x -> x));
	}

	@SafeVarargs
	public static <T> ISuppliterator<T> concat(ISuppliterator<T>... suppliterators)
	{
		return flatten(of(suppliterators));
	}

	/**
	 * 開始値が終了値より大きい場合はデクリメントします。
	 */
	public static ISuppliterator<Integer> range(int startInclusive, int endExclusive)
	{
		if (startInclusive > endExclusive) {
			return new SuppliteratorNullableBase<Integer>() {
				private int i = startInclusive;

				@Override
				public Integer nullableNextImpl()
				{
					if (i > endExclusive) {
						int i2 = i;
						i--;
						return i2;
					} else {
						return null;
					}
				}
			};
		} else {
			return new SuppliteratorNullableBase<Integer>() {
				private int i = startInclusive;

				@Override
				public Integer nullableNextImpl()
				{
					if (i < endExclusive) {
						int i2 = i;
						i++;
						return i2;
					} else {
						return null;
					}
				}
			};
		}
	}

	/**
	 * 開始値が終了値より大きい場合はデクリメントします。
	 */
	public static ISuppliterator<Integer> rangeClosed(int startInclusive, int endInclusive)
	{
		if (startInclusive > endInclusive) {
			return new SuppliteratorNullableBase<Integer>() {
				private int i = startInclusive;

				@Override
				public Integer nullableNextImpl()
				{
					if (i >= endInclusive) {
						int i2 = i;
						i--;
						return i2;
					} else {
						return null;
					}
				}
			};
		} else {
			return new SuppliteratorNullableBase<Integer>() {
				private int i = startInclusive;

				@Override
				public Integer nullableNextImpl()
				{
					if (i <= endInclusive) {
						int i2 = i;
						i++;
						return i2;
					} else {
						return null;
					}
				}
			};
		}
	}

	// 中間操作

	public default <O> ISuppliterator<O> map(Function<? super T, ? extends O> mapper)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<O>() {
			@Override
			public O nullableNextImpl()
			{
				T next = this2.nullableNext();
				return next != null ? mapper.apply(next) : null;
			}
		};
	}

	public default <O> ISuppliterator<O> map(ObjIntToObjFunction<? super T, ? extends O> mapper)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<O>() {
			private int i = 0;

			@Override
			public O nullableNextImpl()
			{
				i++;
				T next = this2.nullableNext();
				return next != null ? mapper.apply(next, i - 1) : null;
			}
		};
	}

	@FunctionalInterface
	public static interface ObjIntToObjFunction<I, O>
	{

		public O apply(I i, int index);

	}

	public default ISuppliterator<T> filter(Predicate<? super T> predicate)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<T>() {
			@Override
			public T nullableNextImpl()
			{
				while (true) {
					T next = this2.nullableNext();
					if (next == null) return null;
					if (predicate.test(next)) return next;
				}
			}
		};
	}

	public default ISuppliterator<T> filter(ObjIntPredicate<? super T> predicate)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<T>() {
			private int i = 0;

			@Override
			public T nullableNextImpl()
			{
				while (true) {
					i++;
					T next = this2.nullableNext();
					if (next == null) return null;
					if (predicate.test(next, i - 1)) return next;
				}
			}
		};
	}

	@FunctionalInterface
	public static interface ObjIntPredicate<T>
	{

		public boolean test(T t, int index);

	}

	public default <O> ISuppliterator<O> filterInstance(Class<? extends O> clazz)
	{
		return filter(clazz::isInstance)
			.map(clazz::cast);
	}

	public static <T> ISuppliterator<T> filterPresent(ISuppliterator<Optional<T>> suppliterator)
	{
		return suppliterator
			.filter(Optional::isPresent)
			.map(Optional::get);
	}

	public default ISuppliterator<T> peek(Consumer<? super T> consumer)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<T>() {
			@Override
			public T nullableNextImpl()
			{
				T next = this2.nullableNext();
				if (next != null) consumer.accept(next);
				return next;
			}
		};
	}

	public default ISuppliterator<T> peek(ObjIntConsumer<? super T> consumer)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<T>() {
			private int i = 0;

			@Override
			public T nullableNextImpl()
			{
				i++;
				T next = this2.nullableNext();
				if (next != null) consumer.accept(next, i - 1);
				return next;
			}
		};
	}

	public default ISuppliterator<T> limit(int length)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<T>() {
			private int i = 0;

			@Override
			public T nullableNextImpl()
			{
				if (i >= length) {
					return null;
				} else {
					i++;
					return this2.nullableNext();
				}
			}
		};
	}

	public default ISuppliterator<T> skip(int start)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<T>() {
			private int i = 0;

			@Override
			public T nullableNextImpl()
			{
				for (; i < start; i++) {
					this2.nullableNext();
				}
				return this2.nullableNext();
			}
		};
	}

	public default ISuppliterator<T> mid(int start, int length)
	{
		return skip(start).limit(length);
	}

	@SuppressWarnings("unchecked")
	public default ISuppliterator<T> before(T... ts)
	{
		return before(ISuppliterator.of(ts));
	}

	public default ISuppliterator<T> before(ISuppliterator<T> ts)
	{
		return ISuppliterator.concat(ts, this);
	}

	@SuppressWarnings("unchecked")
	public default ISuppliterator<T> after(T... ts)
	{
		return after(ISuppliterator.of(ts));
	}

	public default ISuppliterator<T> after(ISuppliterator<T> ts)
	{
		return ISuppliterator.concat(this, ts);
	}

	public default <O> O apply(Function<? super ISuppliterator<T>, ? extends O> function)
	{
		return function.apply(this);
	}

	public static <T> ISuppliterator<T> flatten(ISuppliterator<? extends ISuppliterator<T>> suppliterator)
	{
		return new SuppliteratorNullableBase<T>() {
			private ISuppliterator<T> current = null;

			@Override
			public T nullableNextImpl()
			{
				while (true) {
					if (current == null) {
						current = suppliterator.nullableNext();
						if (current == null) return null;
					}
					T next = current.nullableNext();
					if (next != null) {
						return next;
					} else {
						current = null;
					}
				}
			}
		};
	}

	public default <O> ISuppliterator<O> flatMap(Function<? super T, ? extends ISuppliterator<O>> mapper)
	{
		return flatten(map(mapper));
	}

	public default <O> ISuppliterator<O> flatMap(ObjIntToObjFunction<? super T, ? extends ISuppliterator<O>> mapper)
	{
		return flatten(map(mapper));
	}

	public default ISuppliterator<T> reverse()
	{
		return ofIterator(toCollection(ArrayDeque::new).descendingIterator());
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sorted(Comparator<? super T> comparator)
	{
		List<T> list = toList();
		list.sort(comparator);
		return ofIterable(list);
	}

	/**
	 * 昇順にソートします。
	 */
	public default <C extends Comparable<? super C>> ISuppliterator<T> sortedObj(Function<? super T, ? extends C> function)
	{
		List<T> list = toList();
		list.sort((a, b) -> function.apply(a).compareTo(function.apply(b)));
		return ofIterable(list);
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sortedInt(ToIntFunction<? super T> function)
	{
		List<T> list = toList();
		list.sort((a, b) -> {
			int a2 = function.applyAsInt(a);
			int b2 = function.applyAsInt(b);
			return a2 > b2
				? 1
				: a2 < b2
					? -1
					: 0;
		});
		return ofIterable(list);
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sortedLong(ToLongFunction<? super T> function)
	{
		List<T> list = toList();
		list.sort((a, b) -> {
			long a2 = function.applyAsLong(a);
			long b2 = function.applyAsLong(b);
			return a2 > b2
				? 1
				: a2 < b2
					? -1
					: 0;
		});
		return ofIterable(list);
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sortedDouble(ToDoubleFunction<? super T> function)
	{
		List<T> list = toList();
		list.sort((a, b) -> {
			double a2 = function.applyAsDouble(a);
			double b2 = function.applyAsDouble(b);
			return Double.compare(a2, b2);
		});
		return ofIterable(list);
	}

	@SuppressWarnings("unchecked")
	public static <I extends O, O> ISuppliterator<O> cast(ISuppliterator<I> suppliterator)
	{
		return (ISuppliterator<O>) suppliterator;
	}

	public default ISuppliterator<Tuple<Integer, T>> indexed()
	{
		return map((t, i) -> new Tuple<>(i, t));
	}

	// 終端操作

	@Override
	public default void forEach(Consumer<? super T> consumer)
	{
		while (true) {
			T next = nullableNext();
			if (next != null) {
				consumer.accept(next);
			} else {
				break;
			}
		}
	}

	public default void forEach(ObjIntConsumer<? super T> consumer)
	{
		int i = 0;

		while (true) {
			i++;
			T next = nullableNext();
			if (next != null) {
				consumer.accept(next, i - 1);
			} else {
				break;
			}
		}
	}

	public default Optional<T> find()
	{
		return next();
	}

	public default long count()
	{
		long i = 0;
		while (nullableNext() != null) {
			i++;
		}
		return i;
	}

	public default <R, A> R collect(Collector<? super T, A, R> collector)
	{
		A r = collector.supplier().get();
		BiConsumer<A, ? super T> accumulator = collector.accumulator();
		forEach(t -> accumulator.accept(r, t));
		return collector.finisher().apply(r);
	}

	public default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner)
	{
		R r = supplier.get();
		forEach(t -> accumulator.accept(r, t));
		return r;
	}

	public default String join(String delimiter)
	{
		StringBuilder sb = new StringBuilder();
		T next;
		boolean isFirst = true;
		while ((next = nullableNext()) != null) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(delimiter);
			}
			sb.append(next);
		}
		return sb.toString();
	}

	public default String join()
	{
		StringBuilder sb = new StringBuilder();
		T next;
		while ((next = nullableNext()) != null) {
			sb.append(next);
		}
		return sb.toString();
	}

	// 変換

	public default <C extends Collection<? super T>> C toCollection(Supplier<? extends C> sCollection)
	{
		C collection = sCollection.get();
		forEach(collection::add);
		return collection;
	}

	public default Collection<T> toCollection()
	{
		return toList();
	}

	public default List<T> toList()
	{
		return toCollection(ArrayList::new);
	}

	public static <K, V, M extends Map<? super K, ? super V>> M toMap(ISuppliterator<? extends Entry<? extends K, ? extends V>> suppliterator, Supplier<? extends M> sMap)
	{
		M map = sMap.get();
		for (Entry<? extends K, ? extends V> entry : suppliterator) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	public static <K, V> Map<K, V> toMap(ISuppliterator<? extends Entry<? extends K, ? extends V>> suppliterator)
	{
		return toMap(suppliterator, HashMap::new);
	}

	public default T[] toArray(IntFunction<T[]> sArray)
	{
		List<T> list = toList();
		return list.toArray(sArray.apply(list.size()));
	}

	public default ImmutableArray<T> toImmutableArray()
	{
		return ImmutableArray.ofList(toList());
	}

	public default int[] toIntArray(ToIntFunction<? super T> function)
	{
		return streamToInt(function)
			.toArray();
	}

	public default long[] toLongArray(ToLongFunction<? super T> function)
	{
		return streamToLong(function)
			.toArray();
	}

	public default double[] toDoubleArray(ToDoubleFunction<? super T> function)
	{
		return streamToDouble(function)
			.toArray();
	}

	public default Stream<T> stream()
	{
		return UtilsLambda.toStream(iterator());
	}

	public default IntStream streamToInt(ToIntFunction<? super T> function)
	{
		return stream()
			.mapToInt(function);
	}

	public default LongStream streamToLong(ToLongFunction<? super T> function)
	{
		return stream()
			.mapToLong(function);
	}

	public default DoubleStream streamToDouble(ToDoubleFunction<? super T> function)
	{
		return stream()
			.mapToDouble(function);
	}

	@Override
	public default Iterator<T> iterator()
	{
		ISuppliterator<T> this2 = this;
		return new Iterator<T>() {
			private boolean isFirst = true;
			private T next;

			@Override
			public T next()
			{
				T t = next;
				next = this2.nullableNext();
				return t;
			}

			@Override
			public boolean hasNext()
			{
				if (isFirst) {
					isFirst = false;
					next = this2.nullableNext();
				}
				return next != null;
			}
		};
	}

	public default Enumeration<T> enumerate()
	{
		Iterator<T> iterator = iterator();
		return new Enumeration<T>() {
			@Override
			public T nextElement()
			{
				return iterator.next();
			}

			@Override
			public boolean hasMoreElements()
			{
				return iterator.hasNext();
			}
		};
	}

}

class ISuppliteratorLocal
{

	static void checkRange(int lengthOfArray, int start, int length)
	{
		if (start < 0) throw new ArrayIndexOutOfBoundsException("" + start + " < 0");
		if (start >= lengthOfArray) throw new ArrayIndexOutOfBoundsException("" + start + " >= " + lengthOfArray);
		if (start + length > lengthOfArray) throw new ArrayIndexOutOfBoundsException("" + start + " + " + length + " > " + lengthOfArray);
	}

}
