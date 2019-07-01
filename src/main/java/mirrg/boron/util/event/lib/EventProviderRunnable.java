package mirrg.boron.util.event.lib;

@Deprecated // TODO 削除
public class EventProviderRunnable extends EventProvider<Runnable>
{

	@Override
	public Runnable trigger()
	{
		return () -> fire(l -> l.run());
	}

}
