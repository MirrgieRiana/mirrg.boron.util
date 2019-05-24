package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.HopperEntry;
import mirrg.boron.util.hopper.IHopper;

/**
 * 回収はデーモンスレッドで、処理は非デーモンスレッドで行うホッパースレッドです。
 * 永続的に閉じられないホッパーの処理に使用しても、処理が途中で中断させることがありません。
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

	@Override
	protected void initThread()
	{
		super.initThread();
		thread.setDaemon(true);
	}

	@Override
	protected void process(Deque<HopperEntry<I>> bucket) throws InterruptedException
	{
		Thread thread2 = new Thread(() -> {
			try {
				super.process(bucket);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		thread2.setDaemon(false);
		thread2.start();
		try {
			thread2.join();
		} catch (InterruptedException e) {
			thread2.interrupt();
			thread2.join();
		}
	}

}
