package mirrg.boron.util.hopper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.junit.Test;

import mirrg.boron.util.hopper.lib.Hopper;
import mirrg.boron.util.hopper.lib.HopperRestricted;
import mirrg.boron.util.hopper.lib.HopperThread;
import mirrg.boron.util.hopper.lib.HopperThreadRunnable;
import mirrg.boron.util.hopper.lib.HopperUnreliable;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class TestHopper2
{

	/*
	 * ホッパーをstartして、pushして、closeして、joinするとすべて処理される。
	 */
	@Test
	public void test1() throws InterruptedException
	{
		StringBuilder sb = new StringBuilder();

		Hopper<Runnable> hopper = new HopperRestricted<>(10);
		new HopperThreadRunnable(hopper, 5).start();

		hopper.push(() -> sb.append("a"));
		hopper.push(() -> sb.append("b"));
		hopper.push(() -> sb.append("c"));
		hopper.close();

		hopper.join();

		assertEquals("abc", sb.toString());
	}

	/*
	 * ホッパーに間髪入れずに100個くらい突っ込んでもstart後ならすべて処理される。
	 */
	@Test
	public void test2() throws InterruptedException
	{
		StringBuilder sb = new StringBuilder();

		Hopper<Runnable> hopper = new HopperRestricted<>(10);
		new HopperThreadRunnable(hopper, 5).start();

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

	/*
	 * 処理に時間を要するものも順序正しく処理される。
	 */
	@Test
	public void test3() throws InterruptedException
	{
		StringBuilder sb = new StringBuilder();

		Hopper<Runnable> hopper = new HopperRestricted<>(10);
		new HopperThreadRunnable(hopper, 5).start();

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

	/*
	 * 複数startすると順序は保証されないが、joinすると正しくすべて処理される。
	 */
	@Test
	public void test4() throws InterruptedException
	{
		List<Integer> list = new ArrayList<>();

		Hopper<Runnable> hopper = new HopperRestricted<>(10);
		new HopperThreadRunnable(hopper, 5).start();
		new HopperThreadRunnable(hopper, 5).start();
		new HopperThreadRunnable(hopper, 5).start();
		new HopperThreadRunnable(hopper, 5).start();
		new HopperThreadRunnable(hopper, 5).start();

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

	/*
	 * 10万個くらいアイテムを突っ込んでも処理される。
	 */
	@Test
	public void test5() throws InterruptedException
	{
		List<Integer> list = new ArrayList<>();

		Hopper<Runnable> hopper = new HopperRestricted<>(10);
		new HopperThreadRunnable(hopper, 5).start();
		new HopperThreadRunnable(hopper, 5).start();

		for (int i = 0; i < 100000; i++) {
			int i2 = i;
			hopper.push(() -> {
				synchronized (list) {
					list.add(i2);
				}
			});
		}
		hopper.close();

		hopper.join();

		assertEquals(
			ISuppliterator.range(0, 100000)
				.join(),
			ISuppliterator.ofIterable(list)
				.apply(ISuppliterator::sorted)
				.join());
	}

	/*
	 * HopperThreadを直接継承すると効率的にアイテムを処理できる。
	 */
	@Test
	public void test6() throws InterruptedException
	{
		List<Integer> list = new ArrayList<>();

		Hopper<Integer> hopper = new HopperRestricted<>(10);
		class HopperThreadImpl extends HopperThread<Integer>
		{
			public HopperThreadImpl(IHopper<Integer> hopper)
			{
				super(hopper, 5);
			}

			@Override
			protected void processImpl(Deque<Integer> bucket) throws InterruptedException
			{
				synchronized (list) {
					for (Integer item : bucket) {
						list.add(item);
					}
				}
			}
		}
		new HopperThreadImpl(hopper).start();
		new HopperThreadImpl(hopper).start();

		for (int i = 0; i < 100000; i++) {
			hopper.push(i);
		}
		hopper.close();

		hopper.join();

		assertEquals(
			ISuppliterator.range(0, 100000)
				.join(),
			ISuppliterator.ofIterable(list)
				.apply(ISuppliterator::sorted)
				.join());
	}

	/*
	 * HopperUnreliableは制限を超えたら単に捨てる。
	 */
	@Test
	public void test_HopperUnreliable() throws InterruptedException
	{
		StringBuilder sb = new StringBuilder();

		Hopper<Integer> hopper = new HopperUnreliable<>(10);

		// 先につっこむ
		for (int i = 0; i < 100; i++) {
			hopper.push(i);
		}
		hopper.close();

		// 後から処理スレッド定義
		new HopperThread<Integer>(hopper) {
			@Override
			protected void processImpl(Deque<Integer> bucket) throws InterruptedException
			{
				for (Integer item : bucket) {
					sb.append(item);
				}
			}
		}.start();

		hopper.join();

		assertEquals("0123456789", sb.toString());
	}

	/*
	 * 先頭除去モードのHopperUnreliable
	 */
	@Test
	public void test_HopperUnreliable2() throws InterruptedException
	{
		StringBuilder sb = new StringBuilder();

		Hopper<Integer> hopper = new HopperUnreliable<>(10, true);

		// 先につっこむ
		for (int i = 0; i < 100; i++) {
			hopper.push(i);
		}
		hopper.close();

		// 後から処理スレッド定義
		new HopperThread<Integer>(hopper) {
			@Override
			protected void processImpl(Deque<Integer> bucket) throws InterruptedException
			{
				for (Integer item : bucket) {
					sb.append(item);
				}
			}
		}.start();

		hopper.join();

		assertEquals("90919293949596979899", sb.toString());
	}

}
