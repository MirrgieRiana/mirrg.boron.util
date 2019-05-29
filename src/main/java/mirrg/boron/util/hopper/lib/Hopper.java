package mirrg.boron.util.hopper.lib;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import mirrg.boron.util.hopper.IHopper;

/**
 * 無制限ホッパーです。
 * アイテムはいくらでも搬入可能で、ブロッキングは行われません。
 *
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
public class Hopper<I> implements IHopper<I>
{

	protected final Object lock = new Object();
	protected boolean isClosed = false;
	protected Deque<I> queue = new ArrayDeque<>();
	protected int itemCountProcessing = 0;

	//

	@Override
	public void push(I item) throws InterruptedException
	{
		Objects.requireNonNull(item);
		synchronized (lock) {

			// キュー空き待ち
			while (true) {

				// このホッパーは既に閉じられている
				if (isClosed()) throw new IllegalStateException("Closed hopper");

				// 空きが生まれた
				if (canPush()) break;

				lock.wait();

			}

			// キューに追加
			queue.addLast(item);

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

		}
	}

	@Override
	public void close()
	{
		synchronized (lock) {

			// このホッパーは既に閉じられている
			if (isClosed()) return;

			// ホッパーを閉じる
			isClosed = true;

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

		}
	}

	@Override
	public Deque<I> pop(int amount) throws InterruptedException
	{
		synchronized (lock) {

			// キュー追加待ち
			while (true) {

				// このホッパーはもうアイテムを取り出すことができない
				if (isEmpty()) return null;

				// アイテムが生まれた
				if (canPop()) break;

				lock.wait();

			}

			// 掬う
			Deque<I> result = popImpl(amount);
			plusItemCountProcessing(result.size());

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

			return result;
		}
	}

	/**
	 * このメソッドは{@link #canPop()}が真のときに呼び出されなければなりません。
	 * このメソッド内では処理中のアイテムの個数を増加させません。
	 * このメソッドはロック状態で呼び出されなければなりません。
	 */
	protected Deque<I> popImpl(int amount)
	{
		Deque<I> result;
		if (amount > queue.size()) {
			// 一度に掬える

			result = queue;
			queue = new ArrayDeque<>();

		} else {
			// 一度では掬えない

			result = new ArrayDeque<>(amount);
			for (int i = 0; i < amount; i++) {
				result.addLast(queue.removeFirst());
			}

		}
		return result;
	}

	@Override
	public void plusItemCountProcessing(int amount)
	{
		synchronized (lock) {

			itemCountProcessing += amount;

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

		}
	}

	@Override
	public boolean canPush()
	{
		synchronized (lock) {
			return !isClosed();
		}
	}

	@Override
	public boolean canPop()
	{
		synchronized (lock) {
			return !queue.isEmpty();
		}
	}

	@Override
	public boolean isClosed()
	{
		synchronized (lock) {
			return isClosed;
		}
	}

	@Override
	public boolean isEmpty()
	{
		synchronized (lock) {
			return isClosed() && queue.isEmpty();
		}
	}

	@Override
	public boolean isFinished()
	{
		synchronized (lock) {
			return isEmpty() && itemCountProcessing == 0;
		}
	}

	@Override
	public Object lock()
	{
		return lock;
	}

}
