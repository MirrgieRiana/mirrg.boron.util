package mirrg.boron.util.hopper;

import java.util.ArrayDeque;
import java.util.Deque;

import mirrg.boron.util.struct.Struct1;

/**
 * ホッパーはイベントスレッドに処理を自動的に行わせるためのクラスです。
 */
public final class Hopper
{

	private final int capacity;
	private final int bucketSize;

	public Hopper()
	{
		this(1000, 100);
	}

	/**
	 * @param capacity
	 *            キューの最大処理保持数です。
	 * @param bucketSize
	 *            一度の処理でキューから取り除かれる処理の個数です。
	 */
	public Hopper(int capacity, int bucketSize)
	{
		this.capacity = capacity;
		this.bucketSize = bucketSize;
	}

	//

	private final Object lock = new Object();
	private Deque<Runnable> nQueue = new ArrayDeque<>();
	private int processingCount = 0;

	/**
	 * ホッパーの動作を行います。
	 * このメソッドは、このホッパーが閉じられるまで処理をブロッキングします。
	 * このメソッドは複数のスレッドから同時に呼び出すことができます。
	 * 処理の途中で例外が発生した場合、動作は保証されません。
	 * このメソッドを単一のスレッドから呼び出した場合、キューの順番に処理されることが保証されます。
	 */
	public void run() throws InterruptedException
	{
		while (true) {

			// 掬う処理
			Deque<Runnable> nBucket;
			synchronized (lock) {

				// キュー追加待ち
				while (nQueue != null && nQueue.isEmpty()) {
					lock.wait();
				}

				// 掬う
				if (nQueue == null) {
					nBucket = null;
				} else {
					if (nQueue.size() > bucketSize) {
						// 一度には掬えない

						nBucket = new ArrayDeque<>();
						for (int i = 0; i < bucketSize; i++) {
							nBucket.addLast(nQueue.removeFirst());
						}
					} else {
						// 一度に掬える

						nBucket = nQueue;
						nQueue = new ArrayDeque<>();
					}
					processingCount += nBucket.size();
				}

				// 空きが増えたので通知
				lock.notifyAll();

			}

			// 掬ったものの処理
			if (nBucket == null) {
				// 無を掬った場合は終了

				break;
			} else {
				// 掬ったものがあったので処理する

				try {
					for (Runnable runnable : nBucket) {
						runnable.run();
					}
				} finally {
					synchronized (lock) {
						processingCount -= nBucket.size();
					}
				}

			}

		}
	}

	private static final Struct1<Integer> counter = new Struct1<>(0);

	/**
	 * 新しくデーモンでないスレッドを生成し、{@link #run()}を呼び出させます。
	 */
	public Thread start()
	{
		int id;
		synchronized (counter) {
			id = counter.x;
			counter.x++;
		}
		return start("Hopper" + id);
	}

	public Thread start(String name)
	{
		Thread thread = new Thread(() -> {
			try {
				run();
			} catch (InterruptedException e) {

			}
		}, name);
		thread.setDaemon(false);
		thread.start();
		return thread;
	}

	/**
	 * @param capacity
	 *            -1のとき、容量制限を無視する。
	 */
	private void invoke0(Runnable runnable, boolean isLater, int capacity) throws InterruptedException
	{
		synchronized (lock) {

			// キュー空き待ち
			while (nQueue != null && (capacity != -1 && capacity - nQueue.size() <= 0)) {
				lock.wait();
			}

			// このホッパーはこれ以上使えない
			if (nQueue == null) throw new IllegalStateException("Closed hopper");

			// キューに追加
			if (!isLater) {
				nQueue.addFirst(runnable);
			} else {
				nQueue.addLast(runnable);
			}

			// 項目が増えたので通知
			lock.notifyAll();

		}
	}

	/**
	 * 指定の処理を処理待ちリストの先頭に追加します。
	 * 指定の処理は、現在キューにたまっているあらゆる処理よりも先に実行されます。
	 * キューの容量が足りない場合、このスレッドはキューに空きが生まれるまでブロッキングされます。
	 *
	 * @throws IllegalStateException
	 *             このホッパーが既に閉じられている場合。
	 */
	public void invokeSoon(Runnable runnable) throws InterruptedException
	{
		invoke0(runnable, false, capacity);
	}

	/**
	 * 指定の処理を処理待ちリストの末尾に追加します。
	 * 指定の処理は、現在キューにたまっているあらゆる処理よりも後に実行されます。
	 * キューの容量が足りない場合、このスレッドはキューに空きが生まれるまでブロッキングされます。
	 *
	 * @throws IllegalStateException
	 *             このホッパーが既に閉じられている場合。
	 */
	public void invoke(Runnable runnable) throws InterruptedException
	{
		invoke0(runnable, true, capacity);
	}

	/**
	 * 現在処理中の処理の終了後、このホッパーを閉じます。
	 * まだ処理されていない待機中の処理は処理されずに破棄されます。
	 * ホッパーが閉じられた後に処理を追加しようとすると、invoke系メソッド側で例外が発生します。
	 */
	public void closeSoon()
	{
		synchronized (lock) {

			// キュー待ちはしない

			// このホッパーは既にこれ以上使えない
			if (nQueue == null) return;

			// キューを閉じる
			nQueue = null;

			// キューの状態が変わったので通知
			lock.notifyAll();

		}
	}

	/**
	 * 現在たまっているすべての処理の終了後、このホッパーを閉じます。
	 */
	public void close()
	{
		try {
			invoke0(() -> {
				closeSoon();
			}, true, -1);
		} catch (InterruptedException e) {
			throw new AssertionError();
		}
	}

	/**
	 * このホッパーが閉じられ、すべての処理が完了するまで処理をブロッキングします。
	 */
	public void join() throws InterruptedException
	{
		synchronized (lock) {

			// クローズ待ち
			while (nQueue != null || processingCount > 0) {
				lock.wait();
			}

		}
	}

	/**
	 * このホッパーが閉じられていることを保証します。
	 * このメソッドが偽を返した直後でも、そうでないことは保証できません。
	 */
	public boolean isClosed()
	{
		synchronized (lock) {
			return nQueue == null;
		}
	}

	/**
	 * このホッパーが閉じられ、すべての処理が完了していることを保証します。
	 * このメソッドが偽を返した直後でも、そうでないことは保証できません。
	 */
	public boolean isStopped()
	{
		synchronized (lock) {
			return nQueue == null && processingCount == 0;
		}
	}

}
