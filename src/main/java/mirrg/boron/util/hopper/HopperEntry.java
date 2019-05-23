package mirrg.boron.util.hopper;

public class HopperEntry<I>
{

	public final I item;

	public HopperEntry()
	{
		this(null);
	}

	public HopperEntry(I item)
	{
		this.item = item;
	}

}
