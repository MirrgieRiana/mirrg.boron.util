package mirrg.boron.util.event;

import static org.junit.Assert.*;

import java.util.function.Consumer;

import org.junit.Test;

public class Test1
{

	private static class EventRegistryTest1
	{

		public final IEventProvider<Runnable> event1 = IEventProvider.runnable();

		public final IEventProvider<Runnable> event2 = IEventProvider.runnable();

		public void register(EventRegistryTest1 other)
		{
			event1.register(other.event1.trigger());
			event2.register(other.event2.trigger());
		}

	}

	@Test
	public void test1()
	{
		StringBuffer sb = new StringBuffer();
		EventRegistryTest1 erTest1Main = new EventRegistryTest1();
		Consumer<String> tester = s -> {
			sb.setLength(0);
			erTest1Main.event1.trigger().run();
			erTest1Main.event2.trigger().run();
			erTest1Main.event1.trigger().run();
			assertEquals(s, sb.toString());
		};

		//

		Runnable l1 = () -> sb.append("1");
		Runnable l2 = () -> sb.append("2");
		Runnable l3 = () -> sb.append("3");
		Runnable l4 = () -> sb.append("4");
		Runnable l5 = () -> sb.append("5");

		erTest1Main.event1.register(l1);

		tester.accept("11");

		IRemover remover1 = erTest1Main.event1.register(l2);

		tester.accept("1212");

		erTest1Main.event2.register(l3);

		tester.accept("12312");

		remover1.remove();

		tester.accept("131");

		//

		EventRegistryTest1 erTest1Sub = new EventRegistryTest1();

		erTest1Main.register(erTest1Sub);
		erTest1Sub.event1.register(l4);

		tester.accept("14314");

		erTest1Main.event1.register(l5);

		tester.accept("1453145");

	}

}
