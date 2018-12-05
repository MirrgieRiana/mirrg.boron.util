package mirrg.boron.util.event;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import org.junit.Test;

import mirrg.boron.util.event.IEventProvider;

public class Test2
{

	@Test
	public void test1()
	{
		ArrayList<String> list = new ArrayList<>();

		IEventProvider<BiConsumer<String, String>> eventProvider = IEventProvider.biConsumer();

		eventProvider.register((a, b) -> list.add(a + b));

		eventProvider.trigger().accept("2", "f");
		eventProvider.trigger().accept("1", "b");
		eventProvider.trigger().accept("4", "m");
		eventProvider.trigger().accept("6", "e");

		assertEquals("2f,1b,4m,6e", String.join(",", list));
	}

}
