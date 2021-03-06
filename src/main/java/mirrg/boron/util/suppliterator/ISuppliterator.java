package mirrg.boron.util.suppliterator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
import mirrg.boron.util.struct.Tuple1;
import mirrg.boron.util.struct.Tuple3;
import mirrg.boron.util.struct.Tuple4;

/**
 * {@link Enumeration} や {@link Iterator} がもつメソッドを1個にまとめたものです。
 * ただし、このインターフェースは非nullの値のみが流れます。
 * 並列処理はサポートしまていせん。
 * なので、速度は期待してはいけない。
 */
public interface ISuppliterator<T> extends Iterable<T>
{

	/**
	 * このメソッドがemptyを返した場合、以降このメソッドを呼び出してはなりません。
	 *
	 * @return
	 *         列挙がリストの末端に達して要素が存在しない場合、empty。
	 * @throws NoSuchElementException
	 *             リストの末尾以降にメソッドを呼び出したとき。
	 */
	public Optional<T> next() throws NoSuchElementException;

	/**
	 * このメソッドがnullを返した場合、以降このメソッドを呼び出してはなりません。
	 *
	 * @return
	 *         列挙がリストの末端に達して要素が存在しない場合、null。
	 * @throws NoSuchElementException
	 *             リストの末尾以降にメソッドを呼び出したとき。
	 */
	public T nullableNext() throws NoSuchElementException;

	// 生成

	public static <T> ISuppliterator<T> empty()
	{
		return of();
	}

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
					if (next == null) throw new NullPointerException("" + array + "[" + index + "]");
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
			private int index = start;

			@Override
			public Character nullableNextImpl()
			{
				if (index < start + length) {
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
			private int index = start;

			@Override
			public Short nullableNextImpl()
			{
				if (index < start + length) {
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
			private int index = start;

			@Override
			public Integer nullableNextImpl()
			{
				if (index < start + length) {
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
			private int index = start;

			@Override
			public Long nullableNextImpl()
			{
				if (index < start + length) {
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
			private int index = start;

			@Override
			public Float nullableNextImpl()
			{
				if (index < start + length) {
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
			private int index = start;

			@Override
			public Double nullableNextImpl()
			{
				if (index < start + length) {
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
			private int index = start;

			@Override
			public Boolean nullableNextImpl()
			{
				if (index < start + length) {
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
				if (iterator.hasNext()) {
					T next = iterator.next();
					if (next == null) throw new NullPointerException("" + iterator);
					return next;
				} else {
					return null;
				}
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
	 * 0から始まり、1ずつ増大する指定長さの整数列を生成します。
	 * <p>
	 * このメソッドは{@code range(0, length)}という呼び出しと等価です。
	 */
	public static ISuppliterator<Integer> range(int length)
	{
		return range(0, length);
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

	public static ISuppliterator<Character> characters(CharSequence string, int start, int length)
	{
		return new SuppliteratorNullableBase<Character>() {
			private int i = start;

			@Override
			public Character nullableNextImpl()
			{
				if (i < start + length) {
					Character ch = string.charAt(i);
					i++;
					return ch;
				} else {
					return null;
				}
			}
		};
	}

	public static ISuppliterator<Character> characters(CharSequence string)
	{
		return characters(string, 0, string.length());
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
				if (next != null) {
					O next2 = mapper.apply(next);
					if (next2 == null) throw new NullPointerException("" + mapper);
					return next2;
				} else {
					return null;
				}
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
				if (next != null) {
					O next2 = mapper.apply(next, i - 1);
					if (next2 == null) throw new NullPointerException("" + mapper);
					return next2;
				} else {
					return null;
				}
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

	public default <O> ISuppliterator<O> mapIfPresent(Function<? super T, Optional<? extends O>> mapper)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<O>() {
			@Override
			public O nullableNextImpl()
			{
				while (true) {
					T next = this2.nullableNext();
					if (next == null) return null;
					Optional<? extends O> oO = mapper.apply(next);
					if (oO.isPresent()) return oO.get();
				}
			}
		};
	}

	public default <O> ISuppliterator<O> mapIfPresent(ObjIntToObjFunction<? super T, Optional<? extends O>> mapper)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<O>() {
			private int i = 0;

			@Override
			public O nullableNextImpl()
			{
				while (true) {
					i++;
					T next = this2.nullableNext();
					if (next == null) return null;
					Optional<? extends O> oO = mapper.apply(next, i);
					if (oO.isPresent()) return oO.get();
				}
			}
		};
	}

	public default <O> ISuppliterator<O> mapIfNotNull(Function<? super T, ? extends O> mapper)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<O>() {
			@Override
			public O nullableNextImpl()
			{
				while (true) {
					T next = this2.nullableNext();
					if (next == null) return null;
					O nO = mapper.apply(next);
					if (nO != null) return nO;
				}
			}
		};
	}

	public default <O> ISuppliterator<O> mapIfNotNull(ObjIntToObjFunction<? super T, ? extends O> mapper)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<O>() {
			private int i = 0;

			@Override
			public O nullableNextImpl()
			{
				while (true) {
					i++;
					T next = this2.nullableNext();
					if (next == null) return null;
					O nO = mapper.apply(next, i);
					if (nO != null) return nO;
				}
			}
		};
	}

	@Deprecated // TODO 削除
	public default <O> ISuppliterator<O> filterInstance(Class<? extends O> clazz)
	{
		return filter(clazz::isInstance)
			.map(clazz::cast);
	}

	public default <O extends T> ISuppliterator<O> filterInstance2(Class<? extends O> clazz)
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

	// TODO 名称変更：before
	public default ISuppliterator<T> before1(T t)
	{
		return before(ISuppliterator.of(t));
	}

	// TODO 名称変更：befores
	@SuppressWarnings("unchecked")
	public default ISuppliterator<T> before(T... ts)
	{
		return before(ISuppliterator.of(ts));
	}

	public default ISuppliterator<T> before(ISuppliterator<T> ts)
	{
		return ISuppliterator.concat(ts, this);
	}

	// TODO 名称変更：after
	public default ISuppliterator<T> after1(T t)
	{
		return after(ISuppliterator.of(t));
	}

	// TODO 名称変更：afters
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

	public default <O> O apply2(Function<? super ISuppliterator<T>, Function<? super ISuppliterator<T>, ? extends O>> function)
	{
		return function.apply(this).apply(this);
	}

	public default <I> ISuppliterator<T> applyIf(boolean condition, Function<? super ISuppliterator<T>, ? extends ISuppliterator<T>> function)
	{
		return condition ? function.apply(this) : this;
	}

	public default <I> ISuppliterator<T> applyIf(Predicate<? super ISuppliterator<T>> pCondition, Function<? super ISuppliterator<T>, ? extends ISuppliterator<T>> function)
	{
		return applyIf(pCondition.test(this), function);
	}

	public default <I> ISuppliterator<T> applyEach(Iterable<? extends I> list, BiFunction<? super ISuppliterator<T>, ? super I, ? extends ISuppliterator<T>> function)
	{
		ISuppliterator<T> now = this;
		for (I i : list) {
			now = function.apply(now, i);
		}
		return now;
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

	/**
	 * このSuppliteratorを{@code length}ごとに切り分けた部分Suppliteratorの列を生成します。
	 * 例えば{@code 1,2,3,4,5,6,7,8,9,10}を3ごとにスライスした場合、{@code (1,2,3),(4,5,6),(7,8,9),(10)}
	 * のように切り分けられたSuppliteratorのSuppliteratorが返されます。
	 * <p>
	 * このメソッドは呼び出し時にSuppliteratorのすべての要素にアクセスし、配列として保持します。
	 */
	public default ISuppliterator<ISuppliterator<T>> slice(int length)
	{
		ImmutableArray<T> immutableArray = toImmutableArray();

		int fulls = immutableArray.length() / length; // 10 / 3 == 3
		int mod = immutableArray.length() % length; // 10 % 3 == 1

		if (mod == 0) {
			return range(0, fulls) // 0,1,2
				.map(i -> immutableArray.suppliterator(i * length, length)); // (0,1,2),(3,4,5),(6,7,8)
		} else {
			return concat(
				range(0, fulls) // 0,1,2
					.map(i -> immutableArray.suppliterator(i * length, length)), // (0,1,2),(3,4,5),(6,7,8)
				of(immutableArray.suppliterator(fulls * length, mod))); // (9)
		}
	}

	/**
	 * 指定の値を間に挟み込みます。
	 *
	 * @param value
	 *            間に挟み込まれる値
	 */
	public default ISuppliterator<T> sandwich(T value)
	{
		ISuppliterator<T> this2 = this;
		return new SuppliteratorNullableBase<T>() {
			private boolean first = true;
			private boolean inSeparator;
			private T next;

			@Override
			protected T nullableNextImpl()
			{
				if (first) {
					next = this2.nullableNext();
					if (next == null) return null;
					first = false;
					inSeparator = true;
					return next;
				} else if (inSeparator) {
					next = this2.nullableNext();
					if (next == null) return null;
					inSeparator = false;
					return value;
				} else {
					inSeparator = true;
					return next;
				}
			}
		};
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
	 * 降順にソートします。
	 */
	public default ISuppliterator<T> sortedDescending(Comparator<? super T> comparator)
	{
		return sorted(comparator).reverse();
	}

	/**
	 * 昇順にソートします。
	 *
	 * @param function
	 *            この関数は要素ごとに1度ずつ呼び出されます。
	 */
	public default <C extends Comparable<? super C>> ISuppliterator<T> sortedObj(Function<? super T, ? extends C> function)
	{
		List<Tuple<T, C>> list = map(t -> new Tuple<>(t, (C) function.apply(t))).toList();
		list.sort((a, b) -> a.y.compareTo(b.y));
		return ofIterable(list).map(t -> t.x);
	}

	/**
	 * 降順にソートします。
	 *
	 * @param function
	 *            この関数は要素ごとに1度ずつ呼び出されます。
	 */
	public default <C extends Comparable<? super C>> ISuppliterator<T> sortedObjDescending(Function<? super T, ? extends C> function)
	{
		return sortedObj(function).reverse();
	}

	/**
	 * 昇順にソートします。
	 *
	 * @param function
	 *            この関数は要素ごとに1度ずつ呼び出されます。
	 */
	public default ISuppliterator<T> sortedInt(ToIntFunction<? super T> function)
	{
		class Tuple
		{
			T x;
			int y;

			Tuple(T x, int y)
			{
				this.x = x;
				this.y = y;
			}
		}

		List<Tuple> list = map(t -> new Tuple(t, function.applyAsInt(t))).toList();
		list.sort((a, b) -> Integer.compare(a.y, b.y));
		return ofIterable(list).map(t -> t.x);
	}

	/**
	 * 降順にソートします。
	 *
	 * @param function
	 *            この関数は要素ごとに1度ずつ呼び出されます。
	 */
	public default ISuppliterator<T> sortedIntDescending(ToIntFunction<? super T> function)
	{
		return sortedInt(function).reverse();
	}

	/**
	 * 昇順にソートします。
	 *
	 * @param function
	 *            この関数は要素ごとに1度ずつ呼び出されます。
	 */
	public default ISuppliterator<T> sortedLong(ToLongFunction<? super T> function)
	{
		class Tuple
		{
			T x;
			long y;

			Tuple(T x, long y)
			{
				this.x = x;
				this.y = y;
			}
		}

		List<Tuple> list = map(t -> new Tuple(t, function.applyAsLong(t))).toList();
		list.sort((a, b) -> Long.compare(a.y, b.y));
		return ofIterable(list).map(t -> t.x);
	}

	/**
	 * 降順にソートします。
	 *
	 * @param function
	 *            この関数は要素ごとに1度ずつ呼び出されます。
	 */
	public default ISuppliterator<T> sortedLongDescending(ToLongFunction<? super T> function)
	{
		return sortedLong(function).reverse();
	}

	/**
	 * 昇順にソートします。
	 *
	 * @param function
	 *            この関数は要素ごとに1度ずつ呼び出されます。
	 */
	public default ISuppliterator<T> sortedDouble(ToDoubleFunction<? super T> function)
	{
		class Tuple
		{
			T x;
			double y;

			Tuple(T x, double y)
			{
				this.x = x;
				this.y = y;
			}
		}

		List<Tuple> list = map(t -> new Tuple(t, function.applyAsDouble(t))).toList();
		list.sort((a, b) -> Double.compare(a.y, b.y));
		return ofIterable(list).map(t -> t.x);
	}

	/**
	 * 降順にソートします。
	 *
	 * @param function
	 *            この関数は要素ごとに1度ずつ呼び出されます。
	 */
	public default ISuppliterator<T> sortedDoubleDescending(ToDoubleFunction<? super T> function)
	{
		return sortedDouble(function).reverse();
	}

	/**
	 * 昇順にソートします。
	 */
	public static <T extends Comparable<? super T>> ISuppliterator<T> sorted(ISuppliterator<T> suppliterator)
	{
		List<T> list = suppliterator.toList();
		list.sort(T::compareTo);
		return ofIterable(list);
	}

	public default ISuppliterator<T> shuffle()
	{
		List<T> list = toList();
		Collections.shuffle(list);
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

	/**
	 * 既に現れたものと等しいものを取り除きます。
	 */
	public default ISuppliterator<T> distinct()
	{
		Set<T> set = new HashSet<>();
		return filter(t -> {
			if (set.contains(t)) {
				return false;
			} else {
				set.add(t);
				return true;
			}
		});
	}

	/**
	 * 既に現れたものと、比較オブジェクト同士が等しいものを取り除きます。
	 */
	public default <O> ISuppliterator<T> distinct(Function<? super T, ? extends O> function)
	{
		Set<O> set = new HashSet<>();
		return filter(t -> {
			if (set.contains(function.apply(t))) {
				return false;
			} else {
				set.add(function.apply(t));
				return true;
			}
		});
	}

	/**
	 * このメソッドは、呼び出された段階ですべての要素にアクセスし、その内容を配列に保持します。
	 */
	public default ISuppliterator<T> repeat(int times)
	{
		ImmutableArray<T> array = toImmutableArray();
		return range(0, times)
			.flatMap(i -> array.suppliterator());
	}

	// 終端操作

	public static class IndexedObject<T>
	{

		public final T value;
		public final int index;

		public IndexedObject(T value, int index)
		{
			this.value = value;
			this.index = index;
		}

		@Override
		public String toString()
		{
			return "[" + value + ", " + index + "]";
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + index;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			IndexedObject<?> other = (IndexedObject<?>) obj;
			if (index != other.index) return false;
			if (value == null) {
				if (other.value != null) return false;
			} else if (!value.equals(other.value)) return false;
			return true;
		}

	}

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
			T next = nullableNext();
			if (next != null) {
				consumer.accept(next, i);
			} else {
				break;
			}
			i++;
		}
	}

	public default Optional<T> find()
	{
		return next();
	}

	public default Optional<T> find(Predicate<? super T> predicate)
	{
		return filter(predicate).next();
	}

	public default Optional<IndexedObject<T>> findWithIndex(Predicate<? super T> predicate)
	{
		int i = 0;
		while (true) {
			T next = nullableNext();
			if (next == null) return Optional.empty();
			if (predicate.test(next)) return Optional.of(new IndexedObject<>(next, i));
			i++;
		}
	}

	/**
	 * 指定されたオブジェクトに等しいオブジェクトのインデックスを返します。
	 * オブジェクトが見つからない場合には-1を返します。
	 */
	public default int indexOf(T t)
	{
		int i = 0;
		while (true) {
			T next = nullableNext();
			if (next == null) return -1;
			if (next.equals(t)) return i;
			i++;
		}
	}

	public default Optional<T> first()
	{
		return next();
	}

	public default Optional<T> last()
	{
		T previous = null;
		while (true) {
			T next = nullableNext();
			if (next == null) return Optional.ofNullable(previous);
			previous = next;
		}
	}

	public default Optional<T> get(int index)
	{
		for (int i = 0; i < index; i++) {
			T next = nullableNext();
			if (next == null) return Optional.empty();
		}
		return next();
	}

	public default <O> O collect(ICollectorFactory<? super T, ? extends O> collectorFactory)
	{
		ICollector<? super T, ? extends O> suppliteratorCollector = collectorFactory.create();

		int i = 0;
		while (true) {
			T next = nullableNext();
			if (next != null) {
				suppliteratorCollector.accept(next, i);
			} else {
				break;
			}
			i++;
		}
		return suppliteratorCollector.get();
	}

	public default <O1> Tuple1<O1> collects(
		ICollectorFactory<? super T, ? extends O1> cf1)
	{
		ICollector<? super T, ? extends O1> c1 = cf1.create();

		int i = 0;
		while (true) {
			T next = nullableNext();
			if (next != null) {
				c1.accept(next, i);
			} else {
				break;
			}
			i++;
		}
		return new Tuple1<>(c1.get());
	}

	public default <O1, O2> Tuple<O1, O2> collects(
		ICollectorFactory<? super T, ? extends O1> cf1,
		ICollectorFactory<? super T, ? extends O2> cf2)
	{
		ICollector<? super T, ? extends O1> c1 = cf1.create();
		ICollector<? super T, ? extends O2> c2 = cf2.create();

		int i = 0;
		while (true) {
			T next = nullableNext();
			if (next != null) {
				c1.accept(next, i);
				c2.accept(next, i);
			} else {
				break;
			}
			i++;
		}
		return new Tuple<>(c1.get(), c2.get());
	}

	public default <O1, O2, O3> Tuple3<O1, O2, O3> collects(
		ICollectorFactory<? super T, ? extends O1> cf1,
		ICollectorFactory<? super T, ? extends O2> cf2,
		ICollectorFactory<? super T, ? extends O3> cf3)
	{
		ICollector<? super T, ? extends O1> c1 = cf1.create();
		ICollector<? super T, ? extends O2> c2 = cf2.create();
		ICollector<? super T, ? extends O3> c3 = cf3.create();

		int i = 0;
		while (true) {
			T next = nullableNext();
			if (next != null) {
				c1.accept(next, i);
				c2.accept(next, i);
				c3.accept(next, i);
			} else {
				break;
			}
			i++;
		}
		return new Tuple3<>(c1.get(), c2.get(), c3.get());
	}

	public default <O1, O2, O3, O4> Tuple4<O1, O2, O3, O4> collects(
		ICollectorFactory<? super T, ? extends O1> cf1,
		ICollectorFactory<? super T, ? extends O2> cf2,
		ICollectorFactory<? super T, ? extends O3> cf3,
		ICollectorFactory<? super T, ? extends O4> cf4)
	{
		ICollector<? super T, ? extends O1> c1 = cf1.create();
		ICollector<? super T, ? extends O2> c2 = cf2.create();
		ICollector<? super T, ? extends O3> c3 = cf3.create();
		ICollector<? super T, ? extends O4> c4 = cf4.create();

		int i = 0;
		while (true) {
			T next = nullableNext();
			if (next != null) {
				c1.accept(next, i);
				c2.accept(next, i);
				c3.accept(next, i);
				c4.accept(next, i);
			} else {
				break;
			}
			i++;
		}
		return new Tuple4<>(c1.get(), c2.get(), c3.get(), c4.get());
	}

	public default <R, A> R collect(Collector<? super T, A, R> collector)
	{
		A a = collector.supplier().get();
		BiConsumer<A, ? super T> accumulator = collector.accumulator();
		forEach(t -> accumulator.accept(a, t));
		return collector.finisher().apply(a);
	}

	public default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator)
	{
		R r = supplier.get();
		forEach(t -> accumulator.accept(r, t));
		return r;
	}

	public default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner)
	{
		return collect(supplier, accumulator);
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

	//

	public default byte[] toByteArray(ToByteFunction<? super T> function)
	{
		List<Tuple<byte[], Integer>> list = new ArrayList<>();

		{
			boolean closed = false;
			while (!closed) {
				byte[] array = new byte[4096];
				int length = 0;

				while (true) {
					if (length >= array.length) break;

					T t = nullableNext();
					if (t == null) {
						closed = true;
						break;
					}
					array[length] = function.applyAsByte(t);
					length++;

				}

				list.add(new Tuple<>(array, length));
			}
		}

		byte[] result = new byte[list.stream().mapToInt(t -> t.y).sum()];
		int start = 0;
		for (Tuple<byte[], Integer> tuple : list) {
			System.arraycopy(tuple.x, 0, result, start, tuple.y);
			start += tuple.y;
		}

		return result;
	}

	public default byte[] toByteArray(ISuppliterator<Byte> suppliterator)
	{
		return suppliterator.toByteArray(v -> v);
	}

	@FunctionalInterface
	public interface ToByteFunction<T>
	{

		public byte applyAsByte(T value);

	}

	//

	public default char[] toCharArray(ToCharFunction<? super T> function)
	{
		List<Tuple<char[], Integer>> list = new ArrayList<>();

		{
			boolean closed = false;
			while (!closed) {
				char[] array = new char[4096];
				int length = 0;

				while (true) {
					if (length >= array.length) break;

					T t = nullableNext();
					if (t == null) {
						closed = true;
						break;
					}
					array[length] = function.applyAsChar(t);
					length++;

				}

				list.add(new Tuple<>(array, length));
			}
		}

		char[] result = new char[list.stream().mapToInt(t -> t.y).sum()];
		int start = 0;
		for (Tuple<char[], Integer> tuple : list) {
			System.arraycopy(tuple.x, 0, result, start, tuple.y);
			start += tuple.y;
		}

		return result;
	}

	public default char[] toCharArray(ISuppliterator<Character> suppliterator)
	{
		return suppliterator.toCharArray(v -> v);
	}

	@FunctionalInterface
	public interface ToCharFunction<T>
	{

		public char applyAsChar(T value);

	}

	//

	public default short[] toShortArray(ToShortFunction<? super T> function)
	{
		List<Tuple<short[], Integer>> list = new ArrayList<>();

		{
			boolean closed = false;
			while (!closed) {
				short[] array = new short[4096];
				int length = 0;

				while (true) {
					if (length >= array.length) break;

					T t = nullableNext();
					if (t == null) {
						closed = true;
						break;
					}
					array[length] = function.applyAsShort(t);
					length++;

				}

				list.add(new Tuple<>(array, length));
			}
		}

		short[] result = new short[list.stream().mapToInt(t -> t.y).sum()];
		int start = 0;
		for (Tuple<short[], Integer> tuple : list) {
			System.arraycopy(tuple.x, 0, result, start, tuple.y);
			start += tuple.y;
		}

		return result;
	}

	public default short[] toShortArray(ISuppliterator<Short> suppliterator)
	{
		return suppliterator.toShortArray(v -> v);
	}

	@FunctionalInterface
	public interface ToShortFunction<T>
	{

		public short applyAsShort(T value);

	}

	//

	public default int[] toIntArray(ToIntFunction<? super T> function)
	{
		List<Tuple<int[], Integer>> list = new ArrayList<>();

		{
			boolean closed = false;
			while (!closed) {
				int[] array = new int[4096];
				int length = 0;

				while (true) {
					if (length >= array.length) break;

					T t = nullableNext();
					if (t == null) {
						closed = true;
						break;
					}
					array[length] = function.applyAsInt(t);
					length++;

				}

				list.add(new Tuple<>(array, length));
			}
		}

		int[] result = new int[list.stream().mapToInt(t -> t.y).sum()];
		int start = 0;
		for (Tuple<int[], Integer> tuple : list) {
			System.arraycopy(tuple.x, 0, result, start, tuple.y);
			start += tuple.y;
		}

		return result;
	}

	public default int[] toIntArray(ISuppliterator<Integer> suppliterator)
	{
		return suppliterator.toIntArray(v -> v);
	}

	//

	public default long[] toLongArray(ToLongFunction<? super T> function)
	{
		List<Tuple<long[], Integer>> list = new ArrayList<>();

		{
			boolean closed = false;
			while (!closed) {
				long[] array = new long[4096];
				int length = 0;

				while (true) {
					if (length >= array.length) break;

					T t = nullableNext();
					if (t == null) {
						closed = true;
						break;
					}
					array[length] = function.applyAsLong(t);
					length++;

				}

				list.add(new Tuple<>(array, length));
			}
		}

		long[] result = new long[list.stream().mapToInt(t -> t.y).sum()];
		int start = 0;
		for (Tuple<long[], Integer> tuple : list) {
			System.arraycopy(tuple.x, 0, result, start, tuple.y);
			start += tuple.y;
		}

		return result;
	}

	public default long[] toLongArray(ISuppliterator<Long> suppliterator)
	{
		return suppliterator.toLongArray(v -> v);
	}

	//

	public default float[] toFloatArray(ToFloatFunction<? super T> function)
	{
		List<Tuple<float[], Integer>> list = new ArrayList<>();

		{
			boolean closed = false;
			while (!closed) {
				float[] array = new float[4096];
				int length = 0;

				while (true) {
					if (length >= array.length) break;

					T t = nullableNext();
					if (t == null) {
						closed = true;
						break;
					}
					array[length] = function.applyAsFloat(t);
					length++;

				}

				list.add(new Tuple<>(array, length));
			}
		}

		float[] result = new float[list.stream().mapToInt(t -> t.y).sum()];
		int start = 0;
		for (Tuple<float[], Integer> tuple : list) {
			System.arraycopy(tuple.x, 0, result, start, tuple.y);
			start += tuple.y;
		}

		return result;
	}

	public default float[] toFloatArray(ISuppliterator<Float> suppliterator)
	{
		return suppliterator.toFloatArray(v -> v);
	}

	@FunctionalInterface
	public interface ToFloatFunction<T>
	{

		public float applyAsFloat(T value);

	}

	//

	public default double[] toDoubleArray(ToDoubleFunction<? super T> function)
	{
		List<Tuple<double[], Integer>> list = new ArrayList<>();

		{
			boolean closed = false;
			while (!closed) {
				double[] array = new double[4096];
				int length = 0;

				while (true) {
					if (length >= array.length) break;

					T t = nullableNext();
					if (t == null) {
						closed = true;
						break;
					}
					array[length] = function.applyAsDouble(t);
					length++;

				}

				list.add(new Tuple<>(array, length));
			}
		}

		double[] result = new double[list.stream().mapToInt(t -> t.y).sum()];
		int start = 0;
		for (Tuple<double[], Integer> tuple : list) {
			System.arraycopy(tuple.x, 0, result, start, tuple.y);
			start += tuple.y;
		}

		return result;
	}

	public default double[] toDoubleArray(ISuppliterator<Double> suppliterator)
	{
		return suppliterator.toDoubleArray(v -> v);
	}

	//

	public default boolean[] toBoolArray(Predicate<? super T> function)
	{
		List<Tuple<boolean[], Integer>> list = new ArrayList<>();

		{
			boolean closed = false;
			while (!closed) {
				boolean[] array = new boolean[4096];
				int length = 0;

				while (true) {
					if (length >= array.length) break;

					T t = nullableNext();
					if (t == null) {
						closed = true;
						break;
					}
					array[length] = function.test(t);
					length++;

				}

				list.add(new Tuple<>(array, length));
			}
		}

		boolean[] result = new boolean[list.stream().mapToInt(t -> t.y).sum()];
		int start = 0;
		for (Tuple<boolean[], Integer> tuple : list) {
			System.arraycopy(tuple.x, 0, result, start, tuple.y);
			start += tuple.y;
		}

		return result;
	}

	public default boolean[] toBoolArray(ISuppliterator<Boolean> suppliterator)
	{
		return suppliterator.toBoolArray(v -> v);
	}

	//

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

	// collector redirections

	public default Optional<T> max(Comparator<? super T> comparator)
	{
		return collect(SuppliteratorCollectors.max(comparator));
	}

	public default Optional<T> min(Comparator<? super T> comparator)
	{
		return collect(SuppliteratorCollectors.min(comparator));
	}

	public default <C extends Comparable<? super C>> Optional<T> max(Function<? super T, ? extends C> function)
	{
		return collect(SuppliteratorCollectors.max(function));
	}

	public default <C extends Comparable<? super C>> Optional<T> min(Function<? super T, ? extends C> function)
	{
		return collect(SuppliteratorCollectors.min(function));
	}

	public default <C> Optional<T> max(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return collect(SuppliteratorCollectors.max(function, comparator));
	}

	public default <C> Optional<T> min(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return collect(SuppliteratorCollectors.min(function, comparator));
	}

	public default Optional<IndexedObject<T>> maxWithIndex(Comparator<? super T> comparator)
	{
		return collect(SuppliteratorCollectors.maxWithIndex(comparator));
	}

	public default Optional<IndexedObject<T>> minWithIndex(Comparator<? super T> comparator)
	{
		return collect(SuppliteratorCollectors.minWithIndex(comparator));
	}

	public default <C extends Comparable<? super C>> Optional<IndexedObject<T>> maxWithIndex(Function<? super T, ? extends C> function)
	{
		return collect(SuppliteratorCollectors.maxWithIndex(function));
	}

	public default <C extends Comparable<? super C>> Optional<IndexedObject<T>> minWithIndex(Function<? super T, ? extends C> function)
	{
		return collect(SuppliteratorCollectors.minWithIndex(function));
	}

	public default <C> Optional<IndexedObject<T>> maxWithIndex(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return collect(SuppliteratorCollectors.maxWithIndex(function, comparator));
	}

	public default <C> Optional<IndexedObject<T>> minWithIndex(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return collect(SuppliteratorCollectors.minWithIndex(function, comparator));
	}

	//

	public default long count()
	{
		long i = 0;
		while (nullableNext() != null) {
			i++;
		}
		return i;
	}

	public default boolean isEmpty()
	{
		return nullableNext() == null;
	}

	//

	public default String join()
	{
		StringBuilder sb = new StringBuilder();
		T next;
		while ((next = nullableNext()) != null) {
			sb.append(next);
		}
		return sb.toString();
	}

	public default String join(CharSequence delimiter)
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

	public default String join(Function<? super T, ? extends CharSequence> function)
	{
		StringBuilder sb = new StringBuilder();
		T next;
		while ((next = nullableNext()) != null) {
			sb.append(function.apply(next));
		}
		return sb.toString();
	}

	public default String join(Function<? super T, ? extends CharSequence> function, CharSequence delimiter)
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
			sb.append(function.apply(next));
		}
		return sb.toString();
	}

}

class ISuppliteratorLocal
{

	static void checkRange(int lengthOfArray, int start, int length)
	{
		if (start < 0) throw new ArrayIndexOutOfBoundsException("" + start + " < 0");
		if (start > lengthOfArray) throw new ArrayIndexOutOfBoundsException("" + start + " > " + lengthOfArray);
		if (length < 0) throw new ArrayIndexOutOfBoundsException("" + length + " < " + 0);
		if (start + length > lengthOfArray) throw new ArrayIndexOutOfBoundsException("" + start + " + " + length + " > " + lengthOfArray);
	}

}
