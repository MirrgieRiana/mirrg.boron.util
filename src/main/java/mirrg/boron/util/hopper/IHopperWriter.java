package mirrg.boron.util.hopper;

import java.io.UncheckedIOException;

/**
 * ホッパーの搬入口へのアイテムの投入を定義するインターフェースです。
 *
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
public interface IHopperWriter<I>
{

	/**
	 * ホッパーの搬入口にアイテムを投入します。
	 * ホッパーの搬入口に投入されたアイテムが確実に処理される保証はありません。
	 * ホッパーの搬入口に投入されたアイテムが追加順に処理される保証はありません。
	 * このメソッドは処理をブロッキングする可能性があります。
	 *
	 * @param item
	 *            nullでないオブジェクト
	 * @throws NullPointerException
	 *             投入したオブジェクトがnullであった場合
	 * @throws IllegalStateException
	 *             このホッパーが既に閉じられている場合
	 * @throws UncheckedIOException
	 *             何らかの理由によりアイテムの挿入に失敗した場合
	 * @throws InterruptedException
	 *             待機中にスレッドに割り込みが発生した場合
	 */
	public void push(I item) throws NullPointerException, InterruptedException, IllegalStateException, UncheckedIOException;

	/**
	 * ホッパーの搬入口を閉じ、入力が今後行われないことを示します。
	 * 既にホッパーの搬入口が閉じられていた場合、このメソッドは何も行いません。
	 */
	public void close();

	/**
	 * このホッパーの搬入口にアイテムをブロッキングされることなく投入できるか否かを返します。
	 * このホッパーの搬入口が閉じられていた場合、常に偽を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 */
	public boolean canPush();

	/**
	 * このホッパーの搬入口が閉じられ、アイテムが二度と入れられなくなった場合、真を返します。
	 * ロック状態で呼び出されない限り、このメソッドの戻り値の正しさは保証されません。
	 * このメソッドの返り値は真から偽に変わることはありません。
	 */
	public boolean isClosed();

	/**
	 * このホッパーを排他制御・待機するためのロックオブジェクトを取得します。
	 */
	public Object lock();

}
