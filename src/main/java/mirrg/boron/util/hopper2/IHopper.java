package mirrg.boron.util.hopper2;

/**
 * ホッパーはイベントスレッドに処理を自動的に行わせるためのクラスです。
 * ホッパーは内部に待機中のアイテムを格納するキューを持ちます。
 * <p>
 * ホッパーは、搬入口が閉ざされているか否か（{@link #isClosed()}）の状態を持ちます。
 * ホッパーの搬入口が閉ざされているときにアイテムを入れようとすると、{@link IllegalStateException}が発生します。
 * ホッパーの搬入口を閉じるには{@link #close()}を呼び出します。
 * ホッパーの搬入口は一旦閉ざされた後再び開くことはありません。
 * ホッパーの搬入口が閉ざされたとき、多くの場合、遅れて搬出口も閉ざされます。
 * <p>
 * ホッパーは、搬出口が閉ざされているか否か（{@link #isEmpty()}）の状態を持ちます。
 * ホッパーの搬出口が閉ざされているときにアイテムを取り出そうとすると、nullが返されます。
 * ホッパーの搬出口はホッパー自身の状態制御によって閉ざされます。
 * ホッパーの搬出口は一旦閉ざされた後再び開くことはありません。
 * <p>
 * ホッパーは、処理がすべて完了したか否か（{@link #isFinished()}）の状態を持ちます。
 * {@link #join()}を利用すると、ホッパーの処理が完了するまで待機することができます。
 *
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
public interface IHopper<I> extends IHopperWriter<I>, IHopperReader<I>
{

	/**
	 * このホッパーの搬出口が閉じられており、かつすべての処理が完了している場合に真を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 * このメソッドの帰り値は真から偽に変わることはありません。
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
	 * このホッパーを排他制御・待機するためのロックオブジェクトを取得します。
	 */
	@Override
	public Object lock();

}
