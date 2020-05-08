package mirrg.boron.util.event;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 単一の種類のイベントが流れるイベントプロバイダーです。
 * このイベントプロバイダーは複数種類のイベントを統合的に扱うことができない代わりに、
 * 簡潔で高速なイベント処理を提供します。
 * また、イベントのクラスにとらわれない自由なイベントの定義ができます。
 */
public interface IEventProvider<L>
{

	/**
	 * リスナーをリスナーリストの末尾に追加します。
	 * 追加されたリスナーは必ずこれまでに追加されたリスナーよりも後に呼び出されます。
	 * 既に追加されているリスナーを追加しようとした場合、
	 * そのリスナーは多重に登録され一度のイベントで複数回呼び出されます。
	 *
	 * @return
	 *         これを実行するとイベントの登録が解除されます。
	 */
	public IRemover register(L listener);

	public L trigger();

	public boolean hasListener();

	//

	/**
	 * @deprecated このAPIは移動されます。
	 *             代わりに{@link EventProviders}に含まれるAPIを使用してください。
	 */
	@Deprecated // TODO 削除
	public static IEventProvider<Runnable> runnable()
	{
		return EventProviders.runnable();
	}

	/**
	 * @deprecated このAPIは移動されます。
	 *             代わりに{@link EventProviders}に含まれるAPIを使用してください。
	 */
	@Deprecated // TODO 削除
	public static <E> IEventProvider<Consumer<E>> consumer()
	{
		return EventProviders.consumer();
	}

	/**
	 * @deprecated このAPIは移動されます。
	 *             代わりに{@link EventProviders}に含まれるAPIを使用してください。
	 */
	@Deprecated // TODO 削除
	public static <E1, E2> IEventProvider<BiConsumer<E1, E2>> biConsumer()
	{
		return EventProviders.biConsumer();
	}

}
