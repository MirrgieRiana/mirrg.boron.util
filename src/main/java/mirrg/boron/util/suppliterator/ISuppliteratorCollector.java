package mirrg.boron.util.suppliterator;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

import mirrg.boron.util.suppliterator.ISuppliterator.IndexedObject;

public interface ISuppliteratorCollector<T, O>
{

	public void accept(T t, int index);

	public O get();

	//

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

	public static <T> ISuppliteratorCollector<T, Optional<T>> max(Comparator<? super T> comparator)
	{
		return new ISuppliteratorCollector<T, Optional<T>>() {
			private T valueMax = null;

			@Override
			public void accept(T t, int index)
			{
				if (valueMax == null) {
					valueMax = t;
				} else {
					if (comparator.compare(t, valueMax) > 0) {
						valueMax = t;
					}
				}
			}

			@Override
			public Optional<T> get()
			{
				return valueMax != null ? Optional.of(valueMax) : Optional.empty();
			}
		};
	}

	public static <T> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> maxWithIndex(Comparator<? super T> comparator)
	{
		return new ISuppliteratorCollector<T, Optional<IndexedObject<T>>>() {
			private T valueMax = null;
			private int indexMax = -1;

			@Override
			public void accept(T t, int index)
			{
				if (valueMax == null) {
					valueMax = t;
					indexMax = index;
				} else {
					if (comparator.compare(t, valueMax) > 0) {
						valueMax = t;
						indexMax = index;
					}
				}
			}

			@Override
			public Optional<IndexedObject<T>> get()
			{
				return valueMax != null ? Optional.of(new IndexedObject<>(valueMax, indexMax)) : Optional.empty();
			}
		};
	}

	public static <T> ISuppliteratorCollector<T, Optional<T>> min(Comparator<? super T> comparator)
	{
		return new ISuppliteratorCollector<T, Optional<T>>() {
			private T valueMin = null;

			@Override
			public void accept(T t, int index)
			{
				if (valueMin == null) {
					valueMin = t;
				} else {
					if (comparator.compare(t, valueMin) < 0) {
						valueMin = t;
					}
				}
			}

			@Override
			public Optional<T> get()
			{
				return valueMin != null ? Optional.of(valueMin) : Optional.empty();
			}
		};
	}

	public static <T> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> minWithIndex(Comparator<? super T> comparator)
	{
		return new ISuppliteratorCollector<T, Optional<IndexedObject<T>>>() {
			private T valueMin = null;
			private int indexMin = -1;

			@Override
			public void accept(T t, int index)
			{
				if (valueMin == null) {
					valueMin = t;
					indexMin = index;
				} else {
					if (comparator.compare(t, valueMin) < 0) {
						valueMin = t;
						indexMin = index;
					}
				}
			}

			@Override
			public Optional<IndexedObject<T>> get()
			{
				return valueMin != null ? Optional.of(new IndexedObject<>(valueMin, indexMin)) : Optional.empty();
			}
		};
	}

	public static <T extends Comparable<? super T>> ISuppliteratorCollector<T, Optional<T>> max()
	{
		return max((a, b) -> a.compareTo(b));
	}

	public static <T extends Comparable<? super T>> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> maxWithIndex()
	{
		return maxWithIndex((a, b) -> a.compareTo(b));
	}

	public static <T extends Comparable<? super T>> ISuppliteratorCollector<T, Optional<T>> min()
	{
		return min((a, b) -> a.compareTo(b));
	}

	public static <T extends Comparable<? super T>> ISuppliteratorCollector<T, Optional<IndexedObject<T>>> minWithIndex()
	{
		return minWithIndex((a, b) -> a.compareTo(b));
	}

}
