package mirrg.boron.util.hopper;

import java.util.Deque;
import java.util.Optional;

/**
 * ホッパーはイベントスレッドに処理を自動的に行わせるためのクラスです。
 * ホッパーは内部に待機中のアイテムを格納するキューを持ちます。
 * <p>
 * ホッパーは閉じられるまで開かれている状態です。
 * ホッパーが開かれている場合、アイテムを入れることができます。
 * ホッパーが開かれているか否かを確認するには{@link #isOpening()}を使います。
 * <p>
 * ホッパーを閉じるには、{@link #close()}または{@link #closeSoon()}を呼び出します。
 * ホッパーが閉ざされた場合、それ以上アイテムを入れることはできません。
 * ホッパーが閉ざされているか否かを確認するには{@link #isClosed()}を使います。
 * <p>
 * ホッパーは閉ざされた後も、キューからアイテムがすべて取り除かれるまではアイテムを取り出すことができます。
 * ホッパーが空になった場合、それ以上アイテムを取り出すことはできません。
 * ホッパーが空であるか否かを確認するには{@link #isEmpty()}を使います。
 * <p>
 * ホッパーが空になった後も、それをスレッドが処理している間はホッパーの処理は完了しません。
 * ホッパーの処理が完了した場合、このホッパーの内容に依存している後続の処理に移ることができます。
 * ホッパーの処理が完了しているか否かを確認するには{@link #isFinished()}を使います。
 * <p>
 * {@link #join()}を利用すると、ホッパーの処理が完了するまで待機することができます。
 */
public interface IHopper<I>
{

	/**
	 * アイテムをキューの末尾に追加します。
	 * アイテムは、現在キューにたまっているすべてのアイテムが処理された後に処理されます。
	 * <p>
	 * キューの容量が足りない場合、このスレッドはキューに空きが生まれるまでブロッキングします。
	 *
	 * @throws IllegalStateException
	 *             このホッパーが既に閉じられている場合。
	 */
	public void push(I item) throws InterruptedException;

	/**
	 * キューを空にします。
	 */
	public void clear();

	/**
	 * キューを空にし、ホッパーを閉じます。
	 */
	public default void closeSoon()
	{
		synchronized (lock()) {
			clear();
			close();
		}
	}

	/**
	 * ホッパーを閉じます。
	 */
	public void close();

	/**
	 * キューから最大で指定個数のアイテムを取り出します試みます。
	 * <p>
	 * このメソッドは、ホッパーが閉じられていない限り、キューから少なくとも一つのアイテムを削除します。
	 * キューが空で、ホッパーが閉じられていない場合、キューにアイテムが追加されるか、ホッパーが閉じられるまで処理をブロッキングします。
	 * キューが空で、ホッパーが閉じられてた場合、nullを返します。
	 * <p>
	 * このメソッドは現在処理中のアイテムの個数を増加させます。
	 * 処理が完了したら、{@link #minusItemCountProcessing(int)}によって減少させなければなりません。
	 *
	 * @return キューからアイテムが取り出された場合、それらが格納されているキュー。
	 *         ホッパーが閉じられた場合、null。
	 */
	public Deque<HopperEntry<I>> pop(int amount) throws InterruptedException;

	/**
	 * 現在処理中のアイテムの個数を増加させます。
	 */
	public void plusItemCountProcessing(int amount);

	/**
	 * 現在処理中のアイテムの個数を減少させます。
	 */
	public default void minusItemCountProcessing(int amount)
	{
		plusItemCountProcessing(-amount);
	}

	/**
	 * このホッパーにアイテムがブロッキングなしに入れられるか否かを返します。
	 * このホッパーが閉じられていた場合、常に偽を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 */
	public boolean canPush();

	/**
	 * このホッパーからアイテムがブロッキングなしに取り出せるか否かを返します。
	 * このホッパーが閉じられており、キューが空になっている場合、常に偽を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 */
	public boolean canPop();

	/**
	 * このホッパーが開いているか否かを返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 * ただし、このメソッドの帰り値は偽から真に変わることはありません。
	 */
	public default boolean isOpening()
	{
		return !isClosed();
	}

	/**
	 * このホッパーが閉じられているか否かを返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 * ただし、このメソッドの帰り値は真から偽に変わることはありません。
	 */
	public boolean isClosed();

	/**
	 * このホッパーが閉じられており、かつキューが空である場合に真を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 * ただし、このメソッドの帰り値は真から偽に変わることはありません。
	 */
	public boolean isEmpty();

	/**
	 * このホッパーが閉じられており、かつキューが空であり、かつすべての処理が完了している場合に真を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 * ただし、このメソッドの帰り値は真から偽に変わることはありません。
	 */
	public boolean isFinished();

	/**
	 * このホッパーが閉じられ、すべての処理が完了するまで処理をブロッキングします。
	 */
	public default void join() throws InterruptedException
	{
		Object lock = lock();
		synchronized (lock) {
			while (!isFinished()) {
				lock.wait();
			}
		}
	}

	/**
	 * このホッパーの排他制御するためのロックオブジェクトを返します。
	 */
	public Object lock();

	/**
	 * このホッパーの処理に最適な回収用バッファのサイズです。
	 * emptyの場合、バッファのサイズの指定は特にありません。
	 */
	public default Optional<Integer> getBucketSizePreferred()
	{
		return Optional.empty();
	}

}