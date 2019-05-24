package mirrg.boron.util.hopper;

import java.util.Deque;

import mirrg.boron.util.hopper.lib.Hopper;
import mirrg.boron.util.hopper.lib.HopperThreadDaemon;

public class SampleHopperThreadDaemon
{

	/**
	 * Startのあと、ちゃんとFinishが表示されて終了したら成功。
	 */
	public static void main(String[] args) throws Exception
	{

		IHopper<String> hopper = new Hopper<>();
		new HopperThreadDaemon<String>(hopper, 4) {
			@Override
			protected void processImpl(Deque<String> bucket) throws InterruptedException
			{
				for (String item : bucket) {
					System.out.println("Start " + item);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw null;
					}
					System.out.println("Finish " + item);
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
