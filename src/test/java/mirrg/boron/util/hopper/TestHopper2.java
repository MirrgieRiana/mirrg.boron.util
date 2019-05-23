package mirrg.boron.util.hopper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mirrg.boron.util.hopper.lib.HopperRunnable;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class TestHopper2
{

	@Test
	public void test1() throws InterruptedException
	{
		StringBuffer sb = new StringBuffer();

		HopperRunnable hopper = new HopperRunnable(10, 5);
		hopper.start();

		hopper.push(() -> sb.append("a"));
		hopper.push(() -> sb.append("b"));
		hopper.push(() -> sb.append("c"));
		hopper.close();

		hopper.join();

		assertEquals("abc", sb.toString());
	}

	@Test
	public void test2() throws InterruptedException
	{
		StringBuffer sb = new StringBuffer();

		HopperRunnable hopper = new HopperRunnable(10, 5);
		hopper.start();

		ISuppliterator.range(0, 100)
			.forEach(i -> {
				try {
					hopper.push(() -> sb.append("" + i));
				} catch (InterruptedException e) {
					throw null;
				}
			});
		hopper.close();

		hopper.join();

		assertEquals(ISuppliterator.range(0, 100)
			.join(), sb.toString());
	}

	@Test
	public void test3() throws InterruptedException
	{
		StringBuffer sb = new StringBuffer();

		HopperRunnable hopper = new HopperRunnable(10, 5);
		hopper.start();

		for (int i = 0; i < 20; i++) {
			int i2 = i;
			System.out.println("Register: " + i2);
			hopper.push(() -> {
				try {
					System.out.println("Start: " + i2);
					Thread.sleep(10);
					sb.append("" + i2);
					System.out.println("Finish: " + i2);
				} catch (InterruptedException e) {
					throw null;
				}
			});
			System.out.println("Registered: " + i2);
		}
		hopper.close();

		hopper.join();

		assertEquals(ISuppliterator.range(0, 20)
			.join(), sb.toString());
	}

	@Test
	public void test4() throws InterruptedException
	{
		List<Integer> list = new ArrayList<>();

		HopperRunnable hopper = new HopperRunnable(10, 5);
		hopper.start();
		hopper.start();
		hopper.start();
		hopper.start();
		hopper.start();

		for (int i = 0; i < 100; i++) {
			int i2 = i;
			System.out.println("Register: " + i2);
			hopper.push(() -> {
				try {
					System.out.println("Start: " + i2);
					Thread.sleep(10);
					synchronized (list) {
						list.add(i2);
					}
					System.out.println("Finish: " + i2);
				} catch (InterruptedException e) {
					throw null;
				}
			});
			System.out.println("Registered: " + i2);
		}
		hopper.close();

		hopper.join();

		assertEquals(
			ISuppliterator.range(0, 100)
				.join(),
			ISuppliterator.ofIterable(list)
				.apply(ISuppliterator::sorted)
				.join());
	}

}
