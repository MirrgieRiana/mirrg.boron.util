package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.IHopperReader;

/**
 * 回収はデーモンスレッドで、処理は非デーモンスレッドで行うホッパースレッドです。
 * アイテムが残っている状態でJVMが終了するとき、そのアイテムは単に無視されます。
 * 永続的に閉じられないホッパーの処理に使用しても、スレッドが残留せず、かつ処理が途中で中断させることがありません。
 */
public abstract class HopperThreadDaemon<I> extends HopperThread<I>
{

	public HopperThreadDaemon(IHopperReader<I> hopper, int bucketSize)
	{
		super(hopper, bucketSize);
	}

	public HopperThreadDaemon(IHopperReader<I> hopper)
	{
		super(hopper);
	}

	private final Object lock2 = new Object();
	private boolean isRunning = false;
	private boolean isShutdowned = false;

	@Override
	protected void initThread()
	{
		super.initThread();

		// デーモンスレッドが取り残されてからJVMが終了するまでの一瞬の間に非デーモンスレッドを作ると、
		// 非デーモンスレッドが走っているにも関わらずJVMが終了する対策
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {

			synchronized (lock2) {

				// シャットダウンフラグをON
				isShutdowned = true;

				// 実行中なら終了するまで待機する
				while (isRunning) {
					try {
						lock2.wait();
					} catch (InterruptedException e) {
						throw new AssertionError();
					}
				}

			}

		}));

		thread.setDaemon(true);
	}

	@Override
	protected void process(Deque<I> bucket)
	{

		Thread thread2;

		synchronized (lock2) {

			// シャットダウンしているなら何も行わない
			if (isShutdowned) return;

			// スレッド初期化
			thread2 = new Thread(() -> {
				super.process(bucket);
			}, "Hopper Processing Thread " + nextId());
			thread2.setDaemon(false);
			thread2.start();

			// 実行中フラグをON
			isRunning = true;

		}

		try {

			// 処理の終了まで待機
			try {
				thread2.join();
			} catch (InterruptedException e) {
				throw new AssertionError();
			}

		} finally {
			synchronized (lock2) {

				// 実行中フラグをOFF
				isRunning = false;

				lock2.notifyAll();

			}
		}

	}

}
