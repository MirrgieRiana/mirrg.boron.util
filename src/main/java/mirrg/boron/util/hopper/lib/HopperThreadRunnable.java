package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.IHopper;

public class HopperThreadRunnable extends HopperThread<Runnable>
{

	public HopperThreadRunnable(IHopper<Runnable> hopper)
	{
		super(hopper);
	}

	public HopperThreadRunnable(IHopper<Runnable> hopper, int bucketSize)
	{
		super(hopper, bucketSize);
	}

	@Override
	protected void processImpl(Deque<Runnable> bucket) throws InterruptedException
	{
		for (Runnable item : bucket) {
			item.run();
		}
	}

}
