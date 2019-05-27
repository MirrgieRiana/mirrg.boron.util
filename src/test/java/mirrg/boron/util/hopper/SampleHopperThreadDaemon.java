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
			protected void processImpl(Deque<String> bucket)
			{
				System.out.println("{");
				for (String item : bucket) {
					System.out.println(" { " + item);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw new AssertionError();
					}
					System.out.println(" } " + item);
				}
				System.out.println("}");
			}
		}.start();

		for (int i = 0; i < 30; i++) {
			hopper.push("" + i);
		}

		Thread.sleep(8500);

		System.out.println("Main thread finished");
	}

}
