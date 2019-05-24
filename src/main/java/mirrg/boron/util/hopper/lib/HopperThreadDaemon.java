package mirrg.boron.util.hopper.lib;

import mirrg.boron.util.hopper.IHopper;

/**
 * 回収はデーモンスレッドで、処理は非デーモンスレッドで行うホッパースレッドです。
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
	protected void run() throws InterruptedException
	{
		Thread thread2 = new Thread(() -> {
			try {
				super.run();
			} catch (InterruptedException e) {

			}
		});
		thread2.start();
		thread2.setDaemon(false);
		thread2.join();
	}

}
