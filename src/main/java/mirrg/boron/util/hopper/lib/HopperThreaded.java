package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.HopperEntry;
import mirrg.boron.util.struct.Struct1;

public abstract class HopperThreaded<I> extends HopperRestrictedBase<I>
{

	protected final int bucketSize;

	public HopperThreaded()
	{
		this.bucketSize = capacity;
	}

	/**
	 * @param bucketSize
	 *            一度の取り出しでキューから取り出されるアイテムの個数です。
	 *            多くの場合、capacityと等しい値にすることで最適なパフォーマンスとなります。
	 * @see HopperRestrictedBase#HopperRestrictedBase(int)
	 */
	public HopperThreaded(int capacity, int bucketSize)
	{
		super(capacity);
		this.bucketSize = bucketSize;
	}

	//

	protected static final Struct1<Integer> counter = new Struct1<>(0);

	protected Thread createThread()
	{
		int id;
		synchronized (counter) {
			id = counter.x;
			counter.x++;
		}
		Thread thread = new Thread(() -> {
			try {
				run();
			} catch (InterruptedException e) {

			}
		}, "Hopper" + id);
		thread.setDaemon(false);
		return thread;
	}

	/**
	 * 新しくデーモンでないスレッドを生成し、ホッパーを継続的に処理させます。
	 * このメソッドは複数呼び出すことができ、ホッパーは呼び出した数のスレッドで並列に処理されます。
	 */
	public Thread start()
	{
		Thread thread = createThread();
		thread.start();
		return thread;
	}

	/**
	 * ホッパーの動作を行います。
	 * このメソッドは、このホッパーが閉じられ、このスレッドの処理が終わるまでブロッキングします。
	 * このメソッドは複数のスレッドから同時に呼び出すことができます。
	 * 処理の途中で例外が発生した場合、動作は保証されません。
	 * このメソッドを単一のスレッドから呼び出した場合、キューの順番に処理されることが保証されます。
	 */
	protected void run() throws InterruptedException
	{
		while (true) {

			// 掬う処理
			Deque<HopperEntry<I>> nBucket = pop(bucketSize);

			// 掬ったものの処理
			if (nBucket == null) {
				// 無を掬った場合は終了

				break;
			} else {
				// 掬ったものがあったので処理する

				try {
					process(nBucket);
				} finally {
					synchronized (lock) {

						minusItemCountProcessing(nBucket.size());

						// 状態が変わったので通知
						lock.notifyAll();

					}
				}

			}

		}
	}

	protected abstract void process(Deque<HopperEntry<I>> bucket);

}
