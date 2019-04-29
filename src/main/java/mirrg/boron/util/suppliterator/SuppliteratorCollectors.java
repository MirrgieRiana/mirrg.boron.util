package mirrg.boron.util.suppliterator;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.struct.Tuple1;
import mirrg.boron.util.struct.Tuple3;
import mirrg.boron.util.struct.Tuple4;
import mirrg.boron.util.suppliterator.ISuppliterator.IndexedObject;

public class SuppliteratorCollectors
{

	public static <T, A, R> ICollectorFactory<T, R> ofStreamCollector(Collector<T, A, R> streamCollector)
	{
		Supplier<A> supplier = streamCollector.supplier();
		BiConsumer<A, ? super T> accumulator = streamCollector.accumulator();
		Function<A, R> finisher = streamCollector.finisher();

		return () -> new ICollector<T, R>() {
			private A a = supplier.get();

			@Override
			public void accept(T t, int index)
			{
				accumulator.accept(a, t);
			}

			@Override
			public R get()
			{
				return finisher.apply(a);
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <TI, OI extends OO, TO extends TI, OO> ICollectorFactory<TO, OO> cast(ICollectorFactory<TI, OI> collectorFactory)
	{
		return (ICollectorFactory<TO, OO>) collectorFactory;
	}

	public static <T, O1> ICollectorFactory<T, Tuple1<O1>> teeing(
		ICollectorFactory<? super T, ? extends O1> cf1)
	{
		return () -> new ICollector<T, Tuple1<O1>>() {
			ICollector<? super T, ? extends O1> c1 = cf1.create();

			@Override
			public void accept(T t, int index)
			{
				c1.accept(t, index);
			}

			@Override
			public Tuple1<O1> get()
			{
				return new Tuple1<>(c1.get());
			}
		};
	}

	public static <T, O1, O2> ICollectorFactory<T, Tuple<O1, O2>> teeing(
		ICollectorFactory<? super T, ? extends O1> cf1,
		ICollectorFactory<? super T, ? extends O2> cf2)
	{
		return () -> new ICollector<T, Tuple<O1, O2>>() {
			ICollector<? super T, ? extends O1> c1 = cf1.create();
			ICollector<? super T, ? extends O2> c2 = cf2.create();

			@Override
			public void accept(T t, int index)
			{
				c1.accept(t, index);
				c2.accept(t, index);
			}

			@Override
			public Tuple<O1, O2> get()
			{
				return new Tuple<>(c1.get(), c2.get());
			}
		};
	}

	public static <T, O1, O2, O3> ICollectorFactory<T, Tuple3<O1, O2, O3>> teeing(
		ICollectorFactory<? super T, ? extends O1> cf1,
		ICollectorFactory<? super T, ? extends O2> cf2,
		ICollectorFactory<? super T, ? extends O3> cf3)
	{
		return () -> new ICollector<T, Tuple3<O1, O2, O3>>() {
			ICollector<? super T, ? extends O1> c1 = cf1.create();
			ICollector<? super T, ? extends O2> c2 = cf2.create();
			ICollector<? super T, ? extends O3> c3 = cf3.create();

			@Override
			public void accept(T t, int index)
			{
				c1.accept(t, index);
				c2.accept(t, index);
				c3.accept(t, index);
			}

			@Override
			public Tuple3<O1, O2, O3> get()
			{
				return new Tuple3<>(c1.get(), c2.get(), c3.get());
			}
		};
	}

	public static <T, O1, O2, O3, O4> ICollectorFactory<T, Tuple4<O1, O2, O3, O4>> teeing(
		ICollectorFactory<? super T, ? extends O1> cf1,
		ICollectorFactory<? super T, ? extends O2> cf2,
		ICollectorFactory<? super T, ? extends O3> cf3,
		ICollectorFactory<? super T, ? extends O4> cf4)
	{
		return () -> new ICollector<T, Tuple4<O1, O2, O3, O4>>() {
			ICollector<? super T, ? extends O1> c1 = cf1.create();
			ICollector<? super T, ? extends O2> c2 = cf2.create();
			ICollector<? super T, ? extends O3> c3 = cf3.create();
			ICollector<? super T, ? extends O4> c4 = cf4.create();

			@Override
			public void accept(T t, int index)
			{
				c1.accept(t, index);
				c2.accept(t, index);
				c3.accept(t, index);
				c4.accept(t, index);
			}

			@Override
			public Tuple4<O1, O2, O3, O4> get()
			{
				return new Tuple4<>(c1.get(), c2.get(), c3.get(), c4.get());
			}
		};
	}

	@SafeVarargs
	public static <T, O> ICollectorFactory<T, ImmutableArray<O>> teeingOf(ICollectorFactory<? super T, ? extends O>... cfs)
	{
		return () -> new ICollector<T, ImmutableArray<O>>() {
			private ImmutableArray<ICollector<? super T, ? extends O>> cs = ISuppliterator.ofObjArray(cfs)
				.<ICollector<? super T, ? extends O>> map(cf -> cf.create())
				.toImmutableArray();

			@Override
			public void accept(T t, int index)
			{
				for (ICollector<? super T, ? extends O> c : cs) {
					c.accept(t, index);
				}
			}

			@Override
			public ImmutableArray<O> get()
			{
				return ISuppliterator.ofIterable(cs)
					.map(c -> (O) c.get())
					.toImmutableArray();
			}
		};
	}

	public static <T, O> ICollectorFactory<T, ImmutableArray<O>> teeing(Iterable<? extends ICollectorFactory<? super T, ? extends O>> cfs)
	{
		return () -> new ICollector<T, ImmutableArray<O>>() {
			private ImmutableArray<ICollector<? super T, ? extends O>> cs = ISuppliterator.ofIterable(cfs)
				.<ICollector<? super T, ? extends O>> map(cf -> cf.create())
				.toImmutableArray();

			@Override
			public void accept(T t, int index)
			{
				for (ICollector<? super T, ? extends O> c : cs) {
					c.accept(t, index);
				}
			}

			@Override
			public ImmutableArray<O> get()
			{
				return ISuppliterator.ofIterable(cs)
					.map(c -> (O) c.get())
					.toImmutableArray();
			}
		};
	}

	//

	private static class SuppliteratorCollectorCompareBase<T, C> implements ICollector<T, Optional<IndexedObject<T>>>
	{

		private final Function<? super T, ? extends C> nFunction;
		private final BiPredicate<? super C, ? super C> pIsGreater;

		/**
		 * @param nFunction
		 *            nullにした場合、無検査キャストによりソートキーが抽出されます。
		 */
		public SuppliteratorCollectorCompareBase(Function<? super T, ? extends C> nFunction, BiPredicate<? super C, ? super C> pIsGreater)
		{
			this.nFunction = nFunction;
			this.pIsGreater = pIsGreater;
		}

		private T valueMax = null;
		private C specMax = null;
		private int indexMax = -1;

		@SuppressWarnings("unchecked")
		@Override
		public void accept(T t, int index)
		{
			C spec = nFunction != null ? nFunction.apply(t) : (C) t;
			if (valueMax == null || pIsGreater.test(spec, specMax)) {
				valueMax = t;
				specMax = spec;
				indexMax = index;
			}
		}

		@Override
		public Optional<IndexedObject<T>> get()
		{
			return valueMax != null ? Optional.of(new IndexedObject<>(valueMax, indexMax)) : Optional.empty();
		}

	}

	public static <T extends Comparable<? super T>> ICollectorFactory<T, Optional<T>> max()
	{
		return SuppliteratorCollectors.<T> maxWithIndex().andThen(o -> o.map(io -> io.value));
	}

	public static <T extends Comparable<? super T>> ICollectorFactory<T, Optional<T>> min()
	{
		return SuppliteratorCollectors.<T> minWithIndex().andThen(o -> o.map(io -> io.value));
	}

	public static <T> ICollectorFactory<T, Optional<T>> max(Comparator<? super T> comparator)
	{
		return SuppliteratorCollectors.<T> maxWithIndex(comparator).andThen(o -> o.map(io -> io.value));
	}

	public static <T> ICollectorFactory<T, Optional<T>> min(Comparator<? super T> comparator)
	{
		return SuppliteratorCollectors.<T> minWithIndex(comparator).andThen(o -> o.map(io -> io.value));
	}

	public static <T, C extends Comparable<? super C>> ICollectorFactory<T, Optional<T>> max(Function<? super T, ? extends C> function)
	{
		return SuppliteratorCollectors.<T, C> maxWithIndex(function).andThen(o -> o.map(io -> io.value));
	}

	public static <T, C extends Comparable<? super C>> ICollectorFactory<T, Optional<T>> min(Function<? super T, ? extends C> function)
	{
		return SuppliteratorCollectors.<T, C> minWithIndex(function).andThen(o -> o.map(io -> io.value));
	}

	public static <T, C> ICollectorFactory<T, Optional<T>> max(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return SuppliteratorCollectors.<T, C> maxWithIndex(function, comparator).andThen(o -> o.map(io -> io.value));
	}

	public static <T, C> ICollectorFactory<T, Optional<T>> min(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return SuppliteratorCollectors.<T, C> minWithIndex(function, comparator).andThen(o -> o.map(io -> io.value));
	}

	public static <T extends Comparable<? super T>> ICollectorFactory<T, Optional<IndexedObject<T>>> maxWithIndex()
	{
		return () -> new SuppliteratorCollectorCompareBase<T, T>(null, (a, b) -> a.compareTo(b) > 0);
	}

	public static <T extends Comparable<? super T>> ICollectorFactory<T, Optional<IndexedObject<T>>> minWithIndex()
	{
		return () -> new SuppliteratorCollectorCompareBase<T, T>(null, (a, b) -> a.compareTo(b) < 0);
	}

	public static <T> ICollectorFactory<T, Optional<IndexedObject<T>>> maxWithIndex(Comparator<? super T> comparator)
	{
		return () -> new SuppliteratorCollectorCompareBase<T, T>(null, (a, b) -> comparator.compare(a, b) > 0);
	}

	public static <T> ICollectorFactory<T, Optional<IndexedObject<T>>> minWithIndex(Comparator<? super T> comparator)
	{
		return () -> new SuppliteratorCollectorCompareBase<T, T>(null, (a, b) -> comparator.compare(a, b) < 0);
	}

	public static <T, C extends Comparable<? super C>> ICollectorFactory<T, Optional<IndexedObject<T>>> maxWithIndex(Function<? super T, ? extends C> function)
	{
		return () -> new SuppliteratorCollectorCompareBase<>(function, (a, b) -> a.compareTo(b) > 0);
	}

	public static <T, C extends Comparable<? super C>> ICollectorFactory<T, Optional<IndexedObject<T>>> minWithIndex(Function<? super T, ? extends C> function)
	{
		return () -> new SuppliteratorCollectorCompareBase<>(function, (a, b) -> a.compareTo(b) < 0);
	}

	public static <T, C> ICollectorFactory<T, Optional<IndexedObject<T>>> maxWithIndex(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return () -> new SuppliteratorCollectorCompareBase<>(function, (a, b) -> comparator.compare(a, b) > 0);
	}

	public static <T, C> ICollectorFactory<T, Optional<IndexedObject<T>>> minWithIndex(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return () -> new SuppliteratorCollectorCompareBase<>(function, (a, b) -> comparator.compare(a, b) < 0);
	}

	//

	private static class SuppliteratorCollectorCountingBase<T> implements ICollector<T, Long>
	{

		private long count = 0;

		@Override
		public void accept(T t, int index)
		{
			count++;
		}

		@Override
		public Long get()
		{
			return count;
		}

	}

	public static <T> ICollectorFactory<T, Long> counting()
	{
		return () -> new SuppliteratorCollectorCountingBase<>();
	}

	//

	private static class SuppliteratorCollectorJoiningBase implements ICollector<Object, String>
	{

		private final CharSequence nDelimiter;

		public SuppliteratorCollectorJoiningBase(CharSequence nDelimiter)
		{
			this.nDelimiter = nDelimiter;
		}

		private StringBuilder sb = new StringBuilder();
		private boolean isFirst = true;

		@Override
		public void accept(Object t, int index)
		{
			if (nDelimiter != null) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(nDelimiter);
				}
			}
			sb.append(t);
		}

		@Override
		public String get()
		{
			return sb.toString();
		}

	}

	public static <T> ICollectorFactory<Object, String> joining()
	{
		return () -> new SuppliteratorCollectorJoiningBase(null);
	}

	public static <T> ICollectorFactory<Object, String> joining(CharSequence delimiter)
	{
		return () -> new SuppliteratorCollectorJoiningBase(delimiter);
	}

}
