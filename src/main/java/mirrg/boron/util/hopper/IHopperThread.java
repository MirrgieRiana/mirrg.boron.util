package mirrg.boron.util.hopper;

/**
 * ホッパーを処理するためのスレッドです。
 * ホッパーには複数のホッパースレッドを適用することができます。
 */
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
