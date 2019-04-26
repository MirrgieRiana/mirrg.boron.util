package mirrg.boron.util.suppliterator;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collector;

import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.struct.Tuple1;
import mirrg.boron.util.struct.Tuple3;
import mirrg.boron.util.struct.Tuple4;
import mirrg.boron.util.suppliterator.ISuppliterator.IndexedObject;

public class SuppliteratorCollectors
{

	public static <T, A, R> ISuppliteratorCollector<T, R> ofCollector(Collector<T, A, R> collector)
	{
		return new ISuppliteratorCollector<T, R>() {
			private A a = collector.supplier().get();
			private BiConsumer<A, ? super T> accumulator = collector.accumulator();

			@Override
			public void accept(T t, int index)
			{
				accumulator.accept(a, t);
			}

			@Override
			public R get()
			{
				return collector.finisher().apply(a);
			}
		};
	}

	public static <T, O1> ISuppliteratorCollector<T, Tuple1<O1>> teeing(
		ISuppliteratorCollector<? super T, ? extends O1> sc1)
	{
		return new ISuppliteratorCollector<T, Tuple1<O1>>() {
			@Override
			public void accept(T t, int index)
			{
				sc1.accept(t, index);
			}

			@Override
			public Tuple1<O1> get()
			{
				return new Tuple1<>(sc1.get());
			}
		};
	}

	public static <T, O1, O2> ISuppliteratorCollector<T, Tuple<O1, O2>> teeing(
		ISuppliteratorCollector<? super T, ? extends O1> sc1,
		ISuppliteratorCollector<? super T, ? extends O2> sc2)
	{
		return new ISuppliteratorCollector<T, Tuple<O1, O2>>() {
			@Override
			public void accept(T t, int index)
			{
				sc1.accept(t, index);
				sc2.accept(t, index);
			}

			@Override
			public Tuple<O1, O2> get()
			{
				return new Tuple<>(sc1.get(), sc2.get());
			}
		};
	}

	public static <T, O1, O2, O3> ISuppliteratorCollector<T, Tuple3<O1, O2, O3>> teeing(
		ISuppliteratorCollector<? super T, ? extends O1> sc1,
		ISuppliteratorCollector<? super T, ? extends O2> sc2,
		ISuppliteratorCollector<? super T, ? extends O3> sc3)
	{
		return new ISuppliteratorCollector<T, Tuple3<O1, O2, O3>>() {
			@Override
			public void accept(T t, int index)
			{
				sc1.accept(t, index);
				sc2.accept(t, index);
				sc3.accept(t, index);
			}

			@Override
			public Tuple3<O1, O2, O3> get()
			{
				return new Tuple3<>(sc1.get(), sc2.get(), sc3.get());
			}
		};
	}

	public static <T, O1, O2, O3, O4> ISuppliteratorCollector<T, Tuple4<O1, O2, O3, O4>> teeing(
		ISuppliteratorCollector<? super T, ? extends O1> sc1,
		ISuppliteratorCollector<? super T, ? extends O2> sc2,
		ISuppliteratorCollector<? super T, ? extends O3> sc3,
		ISuppliteratorCollector<? super T, ? extends O4> sc4)
	{
		return new ISuppliteratorCollector<T, Tuple4<O1, O2, O3, O4>>() {
			@Override
			public void accept(T t, int index)
			{
				sc1.accept(t, index);
				sc2.accept(t, index);
				sc3.accept(t, index);
				sc4.accept(t, index);
			}

			@Override
			public Tuple4<O1, O2, O3, O4> get()
			{
				return new Tuple4<>(sc1.get(), sc2.get(), sc3.get(), sc4.get());
			}
		};
	}

	//

	private static class SuppliteratorCollectorCompareBase<T, C> implements ISuppliteratorCollector<T, Optional<IndexedObject<T>>>
	{

		private final Function<? super T, ? extends C> function;
		private final BiPredicate<? super C, ? super C> pIsGreater;

		/**
		 * @param function
		 *            nullにした場合、無検査キャストによりソートキーが抽出されます。
		 */
		public SuppliteratorCollectorCompareBase(Function<? super T, ? extends C> function, BiPredicate<? super C, ? super C> pIsGreater)
		{
			this.function = function;
			this.pIsGreater = pIsGreater;
		}

		private T valueMax = null;
		private C specMax = null;
		private int indexMax = -1;

		@SuppressWarnings("unchecked")
		@Override
		public void accept(T t, int index)
		{
			C spec = function != null ? function.apply(t) : (C) t;
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

	public static <T extends Comparable<? super T>> ISuppliteratorCollector<T, Optional<T>> max()
	{
		return SuppliteratorCollectors.<T> maxWithIndex().andThen(o -> o.map(io -> io.value));
	}

	public static <T extends Comparable<? super T>> ISuppliteratorCollector<T, Optional<T>> min()
	{
		return SuppliteratorCollectors.<T> minWithIndex().andThen(o -> o.map(io -> io.value));
	}

	public static <T> ISuppliteratorCollector<T, Optional<T>> max(Comparator<? super T> comparator)
	{
		return SuppliteratorCollectors.<T> maxWithIndex(comparator).andThen(o -> o.map(io -> io.value));
	}

	public static <T> ISuppliteratorCollector<T, Optional<T>> min(Comparator<? super T> comparator)
	{
		return SuppliteratorCollectors.<T> minWithIndex(comparator).andThen(o -> o.map(io -> io.value));
	}

	public static <T, C extends Comparable<? super C>> ISuppliteratorCollector<T, Optional<T>> max(Function<? super T, ? extends C> function)
	{
		return SuppliteratorCollectors.<T, C> maxWithIndex(function).andThen(o -> o.map(io -> io.value));
	}

	public static <T, C extends Comparable<? super C>> ISuppliteratorCollector<T, Optional<T>> min(Function<? super T, ? extends C> function)
	{
		return SuppliteratorCollectors.<T, C> minWithIndex(function).andThen(o -> o.map(io -> io.value));
	}

	public static <T, C> ISuppliteratorCollector<T, Optional<T>> max(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return SuppliteratorCollectors.<T, C> maxWithIndex(function, comparator).andThen(o -> o.map(io -> io.value));
	}

	public static <T, C> ISuppliteratorCollector<T, Optional<T>> min(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return SuppliteratorCollectors.<T, C> minWithIndex(function, comparator).andThen(o -> o.map(io -> io.value));
	}

	public static <T extends Comparable<? super T>> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> maxWithIndex()
	{
		return new SuppliteratorCollectorCompareBase<T, T>(null, (a, b) -> a.compareTo(b) > 0);
	}

	public static <T extends Comparable<? super T>> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> minWithIndex()
	{
		return new SuppliteratorCollectorCompareBase<T, T>(null, (a, b) -> a.compareTo(b) < 0);
	}

	public static <T> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> maxWithIndex(Comparator<? super T> comparator)
	{
		return new SuppliteratorCollectorCompareBase<T, T>(null, (a, b) -> comparator.compare(a, b) > 0);
	}

	public static <T> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> minWithIndex(Comparator<? super T> comparator)
	{
		return new SuppliteratorCollectorCompareBase<T, T>(null, (a, b) -> comparator.compare(a, b) < 0);
	}

	public static <T, C extends Comparable<? super C>> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> maxWithIndex(Function<? super T, ? extends C> function)
	{
		return new SuppliteratorCollectorCompareBase<>(function, (a, b) -> a.compareTo(b) > 0);
	}

	public static <T, C extends Comparable<? super C>> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> minWithIndex(Function<? super T, ? extends C> function)
	{
		return new SuppliteratorCollectorCompareBase<>(function, (a, b) -> a.compareTo(b) < 0);
	}

	public static <T, C> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> maxWithIndex(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return new SuppliteratorCollectorCompareBase<>(function, (a, b) -> comparator.compare(a, b) > 0);
	}

	public static <T, C> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> minWithIndex(Function<? super T, ? extends C> function, Comparator<? super C> comparator)
	{
		return new SuppliteratorCollectorCompareBase<>(function, (a, b) -> comparator.compare(a, b) < 0);
	}

}
