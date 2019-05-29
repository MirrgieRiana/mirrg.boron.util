package mirrg.boron.util.hopper.lib;

import java.util.Objects;
import java.util.Optional;

/**
 * 制限付きホッパーです。
 * キューが満杯のときにアイテムを搬入しようとすると、
 * そのアイテムがキューに追加されないか、または先頭のアイテムがキューから削除されます。
 * そのため、アイテムの搬入は必ずブロッキングされることなく行われます。
 *
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
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
	public void push(I item)
	{
		Objects.requireNonNull(item);
		synchronized (lock) {

			// キュー空き待ち
			while (true) {

				// このホッパーは既に閉じられている
				if (isClosed()) throw new IllegalStateException("Closed hopper");

				// 空きが生まれた
				if (canPush()) break;

				throw new AssertionError();

			}

			if (hasSpace()) {
				// スペースがある場合

				// キューに追加
				queue.addLast(item);

				// ホッパーの状態が変わったので通知
				lock.notifyAll();

			} else {
				// スペースがない場合

				if (!excludeOlder) {
					// 満杯時に投入されたアイテムを捨てるモードの場合

					// キューに空きがない場合は単に何もせず捨てる

				} else {
					// 満杯時に先頭のアイテムを捨てるモードの場合

					// キューに空きがない場合は先頭のアイテムを捨てる
					queue.pop();

					// キューに追加
					queue.addLast(item);

					// ホッパーの状態が変わったので通知
					lock.notifyAll();

				}

			}

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
