package mirrg.boron.event.impl;

import java.util.function.BiConsumer;

public class EventProviderBiConsumer<E1, E2> extends EventProvider<BiConsumer<E1, E2>>
{

	@Override
	public BiConsumer<E1, E2> trigger()
	{
		return (e1, e2) -> fire(l -> l.accept(e1, e2));
	}

}
