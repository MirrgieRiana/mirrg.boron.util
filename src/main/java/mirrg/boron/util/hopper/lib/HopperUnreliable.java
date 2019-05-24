package mirrg.boron.util.hopper.lib;

import java.util.Optional;

import mirrg.boron.util.hopper.HopperEntryItem;

/**
 * 制限付きホッパーです。
 * キューが満杯のときにアイテムを追加しようとすると、
 * そのアイテムがキューに追加されないか、または先頭のアイテムがキューから削除されます。
 * そのため、アイテムの追加は必ずブロッキングされることなく行われます。
 */
public class HopperUnreliable<I> extends Hopper<I>
{

	protected final int capacity;
	protected final boolean excludeOlder;

	public HopperUnreliable()
	{
		this(100);
	}

	/**
	 * @param excludeOlder
	 *            trueの場合、キューにアイテムが入る代わりに最も古いアイテムが削除されます。
	 */
	public HopperUnreliable(boolean excludeOlder)
	{
		this(100, excludeOlder);
	}

	/**
	 * @param capacity
	 *            キューの最大処理保持数です。
	 */
	public HopperUnreliable(int capacity)
	{
		this(capacity, false);
	}

	/**
	 * @param capacity
	 *            キューの最大処理保持数です。
	 * @param excludeOlder
	 *            trueの場合、キューにアイテムが入る代わりに最も古いアイテムが削除されます。
	 */
	public HopperUnreliable(int capacity, boolean excludeOlder)
	{
		this.capacity = capacity;
		this.excludeOlder = excludeOlder;
	}

	@Override
	public void push(I item) throws InterruptedException
	{
		synchronized (lock) {

			// このホッパーは既に閉じられている
			if (isClosed()) throw new IllegalStateException("Closed hopper");

			if (!excludeOlder) {

				// キューに空きがない場合は単に捨てる
				if (!canPush()) return;
				if (!(capacity - queue.size() > 0)) return;

				// キューに追加
				queue.addLast(new HopperEntryItem<>(item));

			} else {

				// キューに空きがない場合は先頭のアイテムを捨てる
				if (!canPush()) return;
				if (!(capacity - queue.size() > 0)) queue.pop();

				// キューに追加
				queue.addLast(new HopperEntryItem<>(item));

			}

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
