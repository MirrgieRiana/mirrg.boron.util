package mirrg.boron.util.hopper.lib;

import java.util.Deque;

import mirrg.boron.util.hopper.HopperEntry;

public class HopperRunnable extends HopperThreaded<Runnable>
{

	public HopperRunnable()
	{
		super();
	}

	public HopperRunnable(int capacity, int bucketSize)
	{
		super(capacity, bucketSize);
	}

	@Override
	protected void process(Deque<HopperEntry<Runnable>> bucket)
	{
		for (HopperEntry<Runnable> entry : bucket) {
			entry.item.run();
		}
	}

}
