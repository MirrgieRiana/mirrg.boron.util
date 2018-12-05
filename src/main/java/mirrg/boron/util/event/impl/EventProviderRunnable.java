package mirrg.boron.util.event.impl;

public class EventProviderRunnable extends EventProvider<Runnable>
{

	@Override
	public Runnable trigger()
	{
		return () -> fire(l -> l.run());
	}

}