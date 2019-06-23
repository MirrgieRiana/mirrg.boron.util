package mirrg.boron.util.hopper2.lib;

import java.util.Deque;

import mirrg.boron.util.hopper2.IHopperReader;

/**
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
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
