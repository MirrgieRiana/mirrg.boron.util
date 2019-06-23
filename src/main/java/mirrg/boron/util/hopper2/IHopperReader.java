package mirrg.boron.util.hopper2;

import java.util.Deque;
import java.util.Optional;

/**
 * ホッパーの搬出口からのアイテム取り出しを定義するインターフェースです。
 *
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
public interface IHopperReader<I>
{

	/**
	 * ホッパーの搬出口から最大で指定個数のアイテムを取り出します。
	 * ホッパーの搬出口からは、投入されたアイテムがすべて出てくる保証はありません。
	 * ホッパーの搬出口からは、アイテムが投入された順番に出てくる保証はありません。
	 * ホッパーの搬出口からは、投入していないアイテムが出てくる可能性があります。
	 * このメソッドは処理をブロッキングする可能性があります。
	 * <p>
	 * このメソッドからnullでないキューが返された場合、それは必ず1個以上の要素を持ちます。
	 * ホッパーの搬出口が閉じられた場合、nullが返されます。
	 * <p>
	 * このメソッドは現在処理中のアイテムの個数を増加させます。
	 * 処理が完了したら、戻り値のキューの要素数だけ{@link #minusItemCountProcessing(int)}によって減少させなければなりません。
	 *
	 * @throws InterruptedException
	 *             待機中にスレッドに割り込みが発生した場合
	 * @return キューからアイテムが取り出された場合、それらが格納されているキュー。
	 *         ホッパーが閉じられた場合、null。
	 */
	public Deque<I> pop(int amount) throws InterruptedException;

	/**
	 * このホッパーの処理に最適な回収個数です。
	 * emptyの場合、バッファのサイズの指定は特にありません。
	 */
	public default Optional<Integer> getBucketSizePreferred()
	{
		return Optional.empty();
	}

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
	 * このホッパーの搬出口からアイテムがブロッキングされることなく取り出せるか否かを返します。
	 * このホッパーの搬出口が閉じられており、キューが空になっている場合、常に偽を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 */
	public boolean canPop();

	/**
	 * このホッパーの搬出口が閉じられ、アイテムが二度と取り出せなくなった場合、真を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 * このメソッドの帰り値は真から偽に変わることはありません。
	 */
	public boolean isEmpty();

	/**
	 * このホッパーを排他制御・待機するためのロックオブジェクトを取得します。
	 */
	public Object lock();

}
