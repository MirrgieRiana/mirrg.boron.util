package mirrg.boron.util.event.lib;

import java.util.TreeMap;
import java.util.function.Consumer;

import mirrg.boron.util.event.IEventProvider;
import mirrg.boron.util.event.IRemover;

@Deprecated // TODO 削除
public abstract class EventProvider<L> implements IEventProvider<L>
{

	private TreeMap<Integer, L> listeners = new TreeMap<>();

	private int index = 0;

	public IRemover register(L listener)
	{
		int index2 = index;
		listeners.put(index, listener);
		index++;
		return () -> listeners.remove(index2);
	}

	@Override
	public boolean hasListener()
	{
		return !listeners.isEmpty();
	}

	protected void fire(Consumer<L> acceptor)
	{
		for (L listener : listeners.values()) {
			acceptor.accept(listener);
		}
	}

}
