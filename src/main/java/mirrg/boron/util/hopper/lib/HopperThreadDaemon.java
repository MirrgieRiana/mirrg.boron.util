package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.IHopper;

/**
 * 回収はデーモンスレッドで、処理は非デーモンスレッドで行うホッパースレッドです。
 * アイテムが残っている状態でJVMが終了するとき、そのアイテムは単に無視されます。
 * 永続的に閉じられないホッパーの処理に使用しても、スレッドが残留せず、かつ処理が途中で中断させることがありません。
 */
public abstract class HopperThreadDaemon<I> extends HopperThread<I>
{

	public HopperThreadDaemon(IHopper<I> hopper, int bucketSize)
	{
		super(hopper, bucketSize);
	}

	public HopperThreadDaemon(IHopper<I> hopper)
	{
		super(hopper);
	}

	private volatile boolean isDaemonOnly = false;
	private final Object lock2 = new Object();
	private boolean isRunning = false;

	@Override
	protected void initThread()
	{
		super.initThread();

		// デーモンスレッドが取り残されてからJVMが終了するまでの一瞬の間に非デーモンスレッドを作ると、
		// 非デーモンスレッドが走っているにも関わらずJVMが終了する対策
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {

			isDaemonOnly = true;

			synchronized (lock2) {
				while (isRunning) {
					try {
						lock2.wait();
					} catch (InterruptedException e) {
						return;
					}
				}
			}

		}));

		thread.setDaemon(true);
	}

	@Override
	protected void process(Deque<I> bucket) throws InterruptedException
	{

		if (isDaemonOnly) {
			return;
		}

		Thread thread2;

		synchronized (lock2) {

			thread2 = new Thread(() -> {
				try {
					super.process(bucket);
				} catch (InterruptedException e) {

				}
			});
			thread2.setDaemon(false);
			thread2.start();

			isRunning = true;

		}

		try {

			try {
				thread2.join();
			} catch (InterruptedException e) {
				thread2.interrupt();
				thread2.join();
				throw e;
			}

		} finally {
			synchronized (lock2) {
				isRunning = false;
				lock2.notifyAll();
			}
		}

	}

}
