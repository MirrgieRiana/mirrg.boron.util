package mirrg.boron.util.suppliterator;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;

public interface ISuppliteratorCollector<T, O>
{

	public void accept(T t, int index);

	public O get();

	public default <O2> ISuppliteratorCollector<T, O2> andThen(Function<? super O, ? extends O2> function)
	{
		return new ISuppliteratorCollector<T, O2>() {
			@Override
			public void accept(T t, int index)
			{
				ISuppliteratorCollector.this.accept(t, index);
			}

			@Override
			public O2 get()
			{
				return function.apply(ISuppliteratorCollector.this.get());
			}
		};
	}

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

}
