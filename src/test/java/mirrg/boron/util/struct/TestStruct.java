package mirrg.boron.util.struct;

import static mirrg.boron.util.struct.UtilsStruct.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestStruct
{

	@Test
	public void test_new()
	{
		assertEquals("[]", new Tuple0().toString());
		assertEquals("[1]", new Tuple1<>(1).toString());
		assertEquals("[1, 2]", new Tuple<>(1, 2).toString());
		assertEquals("[1, 2, 3]", new Tuple3<>(1, 2, 3).toString());
		assertEquals("[1, 2, 3, 4]", new Tuple4<>(1, 2, 3, 4).toString());

		assertEquals("[]", Tuple0.of().toString());
		assertEquals("[1]", Tuple1.of(1).toString());
		assertEquals("[1, 2]", Tuple.of(1, 2).toString());
		assertEquals("[1, 2, 3]", Tuple3.of(1, 2, 3).toString());
		assertEquals("[1, 2, 3, 4]", Tuple4.of(1, 2, 3, 4).toString());

		assertEquals("[]", new Struct0().toString());
		assertEquals("[1]", new Struct1<>(1).toString());
		assertEquals("[1, 2]", new Struct2<>(1, 2).toString());
		assertEquals("[1, 2, 3]", new Struct3<>(1, 2, 3).toString());
		assertEquals("[1, 2, 3, 4]", new Struct4<>(1, 2, 3, 4).toString());

		assertEquals("[]", Struct0.of().toString());
		assertEquals("[1]", Struct1.of(1).toString());
		assertEquals("[1, 2]", Struct2.of(1, 2).toString());
		assertEquals("[1, 2, 3]", Struct3.of(1, 2, 3).toString());
		assertEquals("[1, 2, 3, 4]", Struct4.of(1, 2, 3, 4).toString());

		assertEquals("[]", new Struct0().toString());
		assertEquals("[null]", new Struct1<>().toString());
		assertEquals("[null, null]", new Struct2<>().toString());
		assertEquals("[null, null, null]", new Struct3<>().toString());
		assertEquals("[null, null, null, null]", new Struct4<>().toString());
	}

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
