package mirrg.boron.util.suppliterator;

/**
 * 前に行われたイテレーションの内容が影響を及ぼすため、このインスタンスは使用の都度 {@link ICollectorFactory}
 * によって生成し直される必要があります。
 */
public interface ICollector<T, O>
{

	public void accept(T t, int index);

	public O get();

}
