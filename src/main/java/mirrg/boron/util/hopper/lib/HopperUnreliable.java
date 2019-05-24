package mirrg.boron.util.hopper.lib;

import java.util.Optional;

import mirrg.boron.util.hopper.HopperEntry;

/**
 * 制限付きホッパーです。
 * キューが満杯のときにアイテムを追加しようとすると、そのアイテムはキューに追加されず単に無視されます。
 * そのため、アイテムの追加は必ずブロッキングされることなく行われます。
 */
public class HopperUnreliable<I> extends Hopper<I>
{

	protected final int capacity;

	public HopperUnreliable()
	{
		this.capacity = 100;
	}

	/**
	 * @param capacity
	 *            キューの最大処理保持数です。
	 */
	public HopperUnreliable(int capacity)
	{
		this.capacity = capacity;
	}

	@Override
	public void push(I item) throws InterruptedException
	{
		synchronized (lock) {

			// このホッパーは既に閉じられている
			if (isClosed()) throw new IllegalStateException("Closed hopper");

			// キューに空きがない場合は単に捨てる
			if (!canPush()) return;
			if (!(capacity - queue.size() > 0)) return;

			// キューに追加
			queue.addLast(new HopperEntry<>(item));

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

		}
	}

	@Override
	public Optional<Integer> getBucketSizePreferred()
	{
		return Optional.of(capacity);
	}

}
