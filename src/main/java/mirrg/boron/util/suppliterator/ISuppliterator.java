package mirrg.boron.util.suppliterator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
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
import java.util.stream.StreamSupport;

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
	 * @param ts
	 *            nullを含まないTの配列
	 */
	@SafeVarargs
	public static <T> ISuppliterator<T> of(T... ts)
	{
		return new SuppliteratorNullableBase<T>() {
			private int index = 0;

			@Override
			public T nullableNextImpl()
			{
				if (index < ts.length) {
					T next = ts[index];
					index++;
					return next;
				} else {
					return null;
				}
			}
		};
	}

	/**
	 * @param iterator
	 *            nullを含まないTのIterator
	 */
	public static <T> ISuppliterator<T> of(Iterator<T> iterator)
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
	public static <T> ISuppliterator<T> of(Iterable<T> iterable)
	{
		return of(iterable.iterator());
	}

	/**
	 * @param enumeration
	 *            nullを含まないTのEnumeration
	 */
	public static <T> ISuppliterator<T> of(Enumeration<T> enumeration)
	{
		return of(new Iterator<T>() {
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
	public static <T> ISuppliterator<T> of(Stream<T> stream)
	{
		return of((Iterable<T>) stream.collect(Collectors.toCollection(ArrayList::new)));
	}

	/**
	 * このメソッドは呼び出し時にストリームのすべての要素にアクセスします。
	 */
	public static ISuppliterator<Integer> of(IntStream stream)
	{
		return of(stream.mapToObj(x -> x));
	}

	/**
	 * このメソッドは呼び出し時にストリームのすべての要素にアクセスします。
	 */
	public static ISuppliterator<Long> of(LongStream stream)
	{
		return of(stream.mapToObj(x -> x));
	}

	/**
	 * このメソッドは呼び出し時にストリームのすべての要素にアクセスします。
	 */
	public static ISuppliterator<Double> of(DoubleStream stream)
	{
		return of(stream.mapToObj(x -> x));
	}

	@SafeVarargs
	public static <T> ISuppliterator<T> concat(ISuppliterator<T>... suppliterators)
	{
		return flatten(of(suppliterators));
	}

	public static ISuppliterator<Integer> range(int startInclusive, int endExclusive)
	{
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

	public default <O> ISuppliterator<O> apply(Function<? super ISuppliterator<T>, ? extends ISuppliterator<O>> function)
	{
		return function.apply(this);
	}

	public static <T> ISuppliterator<T> flatten(ISuppliterator<ISuppliterator<T>> suppliterator)
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
		return of(toCollection(ArrayDeque::new).descendingIterator());
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sorted(Comparator<? super T> comparator)
	{
		ArrayList<T> list = toCollection();
		list.sort(comparator);
		return of(list);
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sortedObj(Function<? super T, Comparable<? super T>> function)
	{
		ArrayList<T> list = toCollection();
		list.sort((a, b) -> function.apply(a).compareTo(b));
		return of(list);
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sortedInt(ToIntFunction<? super T> function)
	{
		ArrayList<T> list = toCollection();
		list.sort((a, b) -> {
			int a2 = function.applyAsInt(a);
			int b2 = function.applyAsInt(b);
			return a2 > b2
				? 1
				: a2 < b2
					? -1
					: 0;
		});
		return of(list);
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sortedLong(ToLongFunction<? super T> function)
	{
		ArrayList<T> list = toCollection();
		list.sort((a, b) -> {
			long a2 = function.applyAsLong(a);
			long b2 = function.applyAsLong(b);
			return a2 > b2
				? 1
				: a2 < b2
					? -1
					: 0;
		});
		return of(list);
	}

	/**
	 * 昇順にソートします。
	 */
	public default ISuppliterator<T> sortedDouble(ToDoubleFunction<? super T> function)
	{
		ArrayList<T> list = toCollection();
		list.sort((a, b) -> {
			double a2 = function.applyAsDouble(a);
			double b2 = function.applyAsDouble(b);
			return Double.compare(a2, b2);
		});
		return of(list);
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

	public default ArrayList<T> toCollection()
	{
		return toCollection(ArrayList::new);
	}

	public default T[] toArray(IntFunction<T[]> sArray)
	{
		ArrayList<T> list = toCollection();
		return list.toArray(sArray.apply(list.size()));
	}

	public default ImmutableArray<T> toImmutableArray()
	{
		return new ImmutableArray<>(toCollection());
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
		return StreamSupport.stream(
			Spliterators.spliteratorUnknownSize(
				iterator(),
				Spliterator.ORDERED), false);
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
