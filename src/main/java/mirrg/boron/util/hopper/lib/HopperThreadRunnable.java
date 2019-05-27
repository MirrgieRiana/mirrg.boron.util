package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.IHopperReader;

public class HopperThreadRunnable extends HopperThread<Runnable>
{

	public HopperThreadRunnable(IHopperReader<Runnable> hopper)
	{
		super(hopper);
	}

	public HopperThreadRunnable(IHopperReader<Runnable> hopper, int bucketSize)
	{
		super(hopper, bucketSize);
	}

	@Override
	protected void processImpl(Deque<Runnable> bucket)
	{
		for (Runnable item : bucket) {
			item.run();
		}
	}

}
