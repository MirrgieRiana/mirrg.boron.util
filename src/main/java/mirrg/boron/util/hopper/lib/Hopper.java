package mirrg.boron.util.hopper.lib;

import java.util.ArrayDeque;
import java.util.Deque;

import mirrg.boron.util.hopper.HopperEntry;
import mirrg.boron.util.hopper.HopperEntryItem;
import mirrg.boron.util.hopper.IHopper;

public class Hopper<I> implements IHopper<I>
{

	protected final Object lock = new Object();
	protected boolean isClosed = false;
	protected Deque<HopperEntry<I>> queue = new ArrayDeque<>();
	protected int itemCountProcessing = 0;

	//

	@Override
	public void push(I item) throws InterruptedException
	{
		synchronized (lock) {

			// キュー空き待ち
			while (true) {

				// このホッパーは既に閉じられている
				if (isClosed()) throw new IllegalStateException("Closed hopper");

				if (canPush()) break;

				lock.wait();

			}

			// キューに追加
			queue.addLast(new HopperEntryItem<>(item));

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

		}
	}

	@Override
	public void clear()
	{
		synchronized (lock) {

			// このホッパーは既に空
			if (queue.isEmpty()) return;

			queue.clear();

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
	public Deque<HopperEntry<I>> pop(int amount) throws InterruptedException
	{
		synchronized (lock) {

			// キュー追加待ち
			while (true) {

				// このホッパーはもうアイテムを取り出すことができない
				if (isEmpty()) return null;

				if (canPop()) break;

				lock.wait();

			}

			// 掬う
			Deque<HopperEntry<I>> result = popImpl(amount);
			plusItemCountProcessing(result.size());

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

			return result;
		}
	}

	/**
	 * キューから指定個数までのアイテムの削除を試みます。
	 * このメソッドの呼び出し時、キューに少なくとも一つのアイテムが格納されていることが保証されます。
	 * このメソッド内では処理中のアイテムを増加させてはなりません。
	 */
	protected Deque<HopperEntry<I>> popImpl(int amount)
	{
		Deque<HopperEntry<I>> result;
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
