package mirrg.boron.util.hopper;

public class HopperEvent
{

	public final Hopper hopper;

	public HopperEvent(Hopper hopper)
	{
		this.hopper = hopper;
	}

	public static class Bucket extends HopperEvent
	{

		public final int itemCountOld;
		public final int itemCountRemoved;
		public final int itemCountNew;

		public Bucket(Hopper hopper, int itemCountOld, int itemCountRemoved, int itemCountNew)
		{
			super(hopper);
			this.itemCountOld = itemCountOld;
			this.itemCountRemoved = itemCountRemoved;
			this.itemCountNew = itemCountNew;
		}

	}

	public static class ThreadStop extends HopperEvent
	{

		public ThreadStop(Hopper hopper)
		{
			super(hopper);
		}

	}

}
