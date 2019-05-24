package mirrg.boron.util.hopper.lib;

import java.util.Optional;

/**
 * 制限付きホッパーです。
 * キューが満杯のときにアイテムを追加しようとすると、キューに空きが生まれるまでブロッキングします。
 */
public class HopperRestricted<I> extends Hopper<I>
{

	protected final int capacity;

	public HopperRestricted()
	{
		this.capacity = 100;
	}

	/**
	 * @param capacity
	 *            キューの最大処理保持数です。
	 */
	public HopperRestricted(int capacity)
	{
		this.capacity = capacity;
	}

	@Override
	public boolean canPush()
	{
		synchronized (lock) {
			return super.canPush() && capacity - queue.size() > 0;
		}
	}

	@Override
	public Optional<Integer> getBucketSizePreferred()
	{
		return Optional.of(capacity);
	}

}
