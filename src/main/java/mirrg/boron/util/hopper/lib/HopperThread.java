package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.IHopperReader;
import mirrg.boron.util.hopper.IHopperThread;
import mirrg.boron.util.struct.Struct1;

/**
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
public abstract class HopperThread<I> implements IHopperThread<I>
{

	protected final IHopperReader<I> hopper;
	protected final int bucketSize;

	/**
	 * ホッパーが指定する最適なbucketSizeを使用してホッパースレッドを生成します。
	 */
	public HopperThread(IHopperReader<I> hopper)
	{
		this(hopper, hopper.getBucketSizePreferred().orElse(100));
	}

	/**
	 * @param bucketSize
	 *            一度の取り出しでキューから取り出されるアイテムの個数です。
	 */
	public HopperThread(IHopperReader<I> hopper, int bucketSize)
	{
		this.hopper = hopper;
		this.bucketSize = bucketSize;
	}

	//

	protected Thread thread = null;

	@Override
	public Thread start()
	{
		if (thread == null) {
			initThread();
			thread.start();
			return thread;
		} else {
			throw new IllegalStateException("Already started");
		}
	}

	/**
	 * アイテム搬出を行うスレッドを初期化します。
	 */
	protected void initThread()
	{
		thread = new Thread(() -> {
			try {
				run();
			} catch (InterruptedException e) {

			}
		}, "Hopper Thread " + nextId());
		thread.setDaemon(false);
	}

	protected static final Struct1<Integer> counter = new Struct1<>(0);

	protected int nextId()
	{
		synchronized (counter) {
			int id = counter.x;
			counter.x++;
			return id;
		}
	}

	/**
	 * ホッパーの動作を行います。
	 * このメソッドは、このホッパーの搬出口が閉じられ、このスレッドの処理が終わるまでブロッキングします。
	 * このメソッドはシングルスレッドで呼び出されることが保証されます。
	 */
	protected void run() throws InterruptedException
	{
		while (true) {

			// 掬う処理
			Deque<I> nBucket = hopper.pop(bucketSize);

			// 掬ったものの処理
			if (nBucket == null) {
				// 無を掬った場合は終了

				break;
			} else {
				// 掬ったものがあったので処理する

				try {
					process(nBucket);
				} finally {
					hopper.minusItemCountProcessing(nBucket.size());
				}

			}

		}
	}

	/**
	 * このメソッドはシングルスレッドで呼び出されることが保証されます。
	 */
	protected void process(Deque<I> bucket)
	{
		processImpl(bucket);
	}

	/**
	 * アイテムの処理を行います。
	 * 処理の途中で例外が発生した場合の動作は実装に依存します。
	 * このメソッドはシングルスレッドで呼び出されることが保証されます。
	 */
	protected abstract void processImpl(Deque<I> bucket);

}
