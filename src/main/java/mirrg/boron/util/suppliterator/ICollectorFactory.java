package mirrg.boron.util.suppliterator;

import java.util.function.Function;

public interface ICollectorFactory<T, O>
{

	public ICollector<T, O> create();

	public default <O2> ICollectorFactory<T, O2> andThen(Function<? super O, ? extends O2> function)
	{
		return () -> {
			ICollector<T, O> collector = create();
			return new ICollector<T, O2>() {
				@Override
				public void accept(T t, int index)
				{
					collector.accept(t, index);
				}

				@Override
				public O2 get()
				{
					return function.apply(collector.get());
				}
			};
		};
	}

}
