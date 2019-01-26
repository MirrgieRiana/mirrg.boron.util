package mirrg.boron.util.struct;

import static mirrg.boron.util.struct.UtilsStruct.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestStruct
{

	@Test
	public void test_conversion()
	{
		assertEquals(tuple(1, 2, 3, 4), tuple().addLast(1).addLast(2).addLast(3).addLast(4));
		assertEquals(tuple(1, 2, 3, 4), tuple().addFirst(4).addFirst(3).addFirst(2).addFirst(1));
		assertEquals(struct(1, 2, 3, 4), struct().addLast(1).addLast(2).addLast(3).addLast(4));
		assertEquals(struct(1, 2, 3, 4), struct().addFirst(4).addFirst(3).addFirst(2).addFirst(1));

		assertNotEquals(struct(), struct().toTuple());
		assertNotEquals(struct(1), struct(1).toTuple());
		assertNotEquals(struct(1, 2), struct(1, 2).toTuple());
		assertNotEquals(struct(1, 2, 3), struct(1, 2, 3).toTuple());
		assertNotEquals(struct(1, 2, 3, 4), struct(1, 2, 3, 4).toTuple());
		assertNotEquals(tuple(), tuple().toStruct());
		assertNotEquals(tuple(1), tuple(1).toStruct());
		assertNotEquals(tuple(1, 2), tuple(1, 2).toStruct());
		assertNotEquals(tuple(1, 2, 3), tuple(1, 2, 3).toStruct());
		assertNotEquals(tuple(1, 2, 3, 4), tuple(1, 2, 3, 4).toStruct());
		assertEquals(tuple(), struct().toTuple());
		assertEquals(tuple(1), struct(1).toTuple());
		assertEquals(tuple(1, 2), struct(1, 2).toTuple());
		assertEquals(tuple(1, 2, 3), struct(1, 2, 3).toTuple());
		assertEquals(tuple(1, 2, 3, 4), struct(1, 2, 3, 4).toTuple());
		assertEquals(struct(), tuple().toStruct());
		assertEquals(struct(1), tuple(1).toStruct());
		assertEquals(struct(1, 2), tuple(1, 2).toStruct());
		assertEquals(struct(1, 2, 3), tuple(1, 2, 3).toStruct());
		assertEquals(struct(1, 2, 3, 4), tuple(1, 2, 3, 4).toStruct());

		assertEquals(struct(2, 3, 4), struct(1, 2, 3, 4).removeFirst());
		assertEquals(struct(3, 4), struct(2, 3, 4).removeFirst());
		assertEquals(struct(4), struct(3, 4).removeFirst());
		assertEquals(struct(), struct(4).removeFirst());
		assertEquals(tuple(2, 3, 4), tuple(1, 2, 3, 4).removeFirst());
		assertEquals(tuple(3, 4), tuple(2, 3, 4).removeFirst());
		assertEquals(tuple(4), tuple(3, 4).removeFirst());
		assertEquals(tuple(), tuple(4).removeFirst());
		assertEquals(struct(1, 2, 3), struct(1, 2, 3, 4).removeLast());
		assertEquals(struct(1, 2), struct(1, 2, 3).removeLast());
		assertEquals(struct(1), struct(1, 2).removeLast());
		assertEquals(struct(), struct(1).removeLast());
		assertEquals(tuple(1, 2, 3), tuple(1, 2, 3, 4).removeLast());
		assertEquals(tuple(1, 2), tuple(1, 2, 3).removeLast());
		assertEquals(tuple(1), tuple(1, 2).removeLast());
		assertEquals(tuple(), tuple(1).removeLast());
	}

}
