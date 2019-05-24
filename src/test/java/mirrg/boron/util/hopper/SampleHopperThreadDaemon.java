package mirrg.boron.util.hopper;

import java.util.Deque;

import mirrg.boron.util.hopper.lib.Hopper;
import mirrg.boron.util.hopper.lib.HopperThreadDaemon;

public class SampleHopperThreadDaemon
{

	public static void main(String[] args) throws Exception
	{

		IHopper<String> hopper = new Hopper<>();
		new HopperThreadDaemon<String>(hopper, 4) {
			@Override
			protected void processImpl(Deque<HopperEntry<String>> bucket) throws InterruptedException
			{
				for (HopperEntry<String> entry : bucket) {
					System.out.println("Start" + entry.item);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw null;
					}
					System.out.println("Finish" + entry.item);
				}
			}
		}.start();

		for (int i = 0; i < 20; i++) {
			hopper.push("" + i);
		}

		Thread.sleep(8500);

		System.out.println("Main thread finished");
	}

}
