package mirrg.boron.util.hopper.lib;

import java.util.Optional;

/**
 * 制限付きホッパーです。
 * キューが満杯のときにアイテムを搬入しようとすると、キューに空きが生まれるまでブロッキングします。
 *
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
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
			return super.canPush() && hasSpace();
		}
	}

	public boolean hasSpace()
	{
		return capacity - queue.size() > 0;
	}

	@Override
	public Optional<Integer> getBucketSizePreferred()
	{
		return Optional.of(capacity);
	}

}
