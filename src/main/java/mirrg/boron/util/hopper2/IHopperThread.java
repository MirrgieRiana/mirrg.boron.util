package mirrg.boron.util.hopper2;

/**
 * ホッパーを処理するためのスレッドです。
 * ホッパーには複数のホッパースレッドを適用することができます。
 *
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
public interface IHopperThread<I>
{

	/**
	 * 新しくスレッドを生成し、ホッパーを継続的に処理させます。
	 * このメソッドは一つのホッパースレッドに対して1度だけ呼び出すことができます。
	 *
	 * @throws IllegalStateException
	 *             このホッパースレッドが既に開始されていた場合
	 */
	public Thread start();

}
