package mirrg.boron.util.suppliterator;

import java.util.function.Function;

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

}
