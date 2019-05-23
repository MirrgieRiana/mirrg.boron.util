package mirrg.boron.util.hopper.lib;

public abstract class HopperRestrictedBase<I> extends HopperBase<I>
{

	protected final int capacity;

	public HopperRestrictedBase()
	{
		this.capacity = 100;
	}

	/**
	 * @param capacity
	 *            キューの最大処理保持数です。
	 */
	public HopperRestrictedBase(int capacity)
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

}
