package mirrg.boron.util.event;

import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;

public class EventProviders
{

	private static abstract class EventProvider<L> implements IEventProvider<L>
	{

		private TreeMap<Integer, L> listeners = new TreeMap<>();

		private int index = 0;

		@Override
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

	//

	public static IEventProvider<Runnable> runnable()
	{
		return new EventProvider<Runnable>() {
			@Override
			public Runnable trigger()
			{
				return () -> fire(l -> l.run());
			}
		};
	}

	public static <X> IEventProvider<Consumer<X>> consumer()
	{
		return new EventProvider<Consumer<X>>() {
			@Override
			public Consumer<X> trigger()
			{
				return (x) -> fire(l -> l.accept(x));
			}
		};
	}

	public static <X, Y> IEventProvider<BiConsumer<X, Y>> biConsumer()
	{
		return new EventProvider<BiConsumer<X, Y>>() {
			@Override
			public BiConsumer<X, Y> trigger()
			{
				return (x, y) -> fire(l -> l.accept(x, y));
			}
		};
	}

	public static IEventProvider<IntConsumer> intConsumer()
	{
		return new EventProvider<IntConsumer>() {
			@Override
			public IntConsumer trigger()
			{
				return (x) -> fire(l -> l.accept(x));
			}
		};
	}

	public static IEventProvider<LongConsumer> longConsumer()
	{
		return new EventProvider<LongConsumer>() {
			@Override
			public LongConsumer trigger()
			{
				return (x) -> fire(l -> l.accept(x));
			}
		};
	}

	public static IEventProvider<DoubleConsumer> doubleConsumer()
	{
		return new EventProvider<DoubleConsumer>() {
			@Override
			public DoubleConsumer trigger()
			{
				return (x) -> fire(l -> l.accept(x));
			}
		};
	}

	public static <X> IEventProvider<ObjIntConsumer<X>> onjIntConsumer()
	{
		return new EventProvider<ObjIntConsumer<X>>() {
			@Override
			public ObjIntConsumer<X> trigger()
			{
				return (x, y) -> fire(l -> l.accept(x, y));
			}
		};
	}

	public static <X> IEventProvider<ObjLongConsumer<X>> onjLongConsumer()
	{
		return new EventProvider<ObjLongConsumer<X>>() {
			@Override
			public ObjLongConsumer<X> trigger()
			{
				return (x, y) -> fire(l -> l.accept(x, y));
			}
		};
	}

	public static <X> IEventProvider<ObjDoubleConsumer<X>> onjDoubleConsumer()
	{
		return new EventProvider<ObjDoubleConsumer<X>>() {
			@Override
			public ObjDoubleConsumer<X> trigger()
			{
				return (x, y) -> fire(l -> l.accept(x, y));
			}
		};
	}

}
