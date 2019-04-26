package mirrg.boron.util.suppliterator;

import java.util.function.Function;

/**
 * 一つのコレクタインスタンスを使いまわした場合、前に行われたイテレーションの内容が影響を及ぼす可能性があります。
 */
public interface ISuppliteratorCollector<T, O>
{

	/**
	 * このコレクタの内部状態を初期化します。
	 */
	public void init();

	public void accept(T t, int index);

	public O get();

	public default <O2> ISuppliteratorCollector<T, O2> andThen(Function<? super O, ? extends O2> function)
	{
		return new ISuppliteratorCollector<T, O2>() {
			@Override
			public void init()
			{
				ISuppliteratorCollector.this.init();
			}

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
