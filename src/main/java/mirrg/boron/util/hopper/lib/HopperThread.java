package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.HopperEntry;
import mirrg.boron.util.hopper.IHopper;
import mirrg.boron.util.hopper.IHopperThread;
import mirrg.boron.util.struct.Struct1;

public abstract class HopperThread<I> implements IHopperThread<I>
{

	protected final IHopper<I> hopper;
	protected final int bucketSize;

	/**
	 * ホッパーが指定する最適なbucketSizeを使用してホッパースレッドを生成します。
	 */
	public HopperThread(IHopper<I> hopper)
	{
		this(hopper, hopper.getBucketSizePreferred().orElse(100));
	}

	/**
	 * @param bucketSize
	 *            一度の取り出しでキューから取り出されるアイテムの個数です。
	 */
	public HopperThread(IHopper<I> hopper, int bucketSize)
	{
		this.hopper = hopper;
		this.bucketSize = bucketSize;
	}

	//

	protected static final Struct1<Integer> counter = new Struct1<>(0);

	protected void initThread()
	{
		int id;
		synchronized (counter) {
			id = counter.x;
			counter.x++;
		}
		thread = new Thread(() -> {
			try {
				run();
			} catch (InterruptedException e) {

			}
		}, "Hopper" + id);
		thread.setDaemon(false);
	}

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
	 * ホッパーの動作を行います。
	 * このメソッドは、このホッパーが閉じられ、このスレッドの処理が終わるまでブロッキングします。
	 */
	protected void run() throws InterruptedException
	{
		while (true) {

			// 掬う処理
			Deque<HopperEntry<I>> nBucket = hopper.pop(bucketSize);

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

	protected void process(Deque<HopperEntry<I>> bucket) throws InterruptedException
	{
		processImpl(bucket);
	}

	/**
	 * アイテムの処理を行います。
	 * 処理の途中で例外が発生した場合の動作は実装に依存します。
	 */
	protected abstract void processImpl(Deque<HopperEntry<I>> bucket) throws InterruptedException;

}
