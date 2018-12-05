package mirrg.boron.util;

import static org.junit.Assert.*;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.Test;

public class TestUtilsMath
{

	@Test
	public void test_parse()
	{
		assertEquals(OptionalInt.of(198), UtilsMath.parseInt("198"));
		assertEquals(OptionalInt.of(198), UtilsMath.parseInt("+198"));
		assertEquals(OptionalInt.of(-198), UtilsMath.parseInt("-198"));
		assertEquals(OptionalInt.of(Integer.parseInt("0198")), UtilsMath.parseInt("0198"));
		assertEquals(OptionalInt.of(198), UtilsMath.parseInt("0198", 10));
		assertEquals(OptionalInt.empty(), UtilsMath.parseInt("53264738926553252", 10));
		assertEquals(OptionalInt.empty(), UtilsMath.parseInt("-53264738926553252", 10));

		assertEquals(OptionalLong.of(198), UtilsMath.parseLong("198"));
		assertEquals(OptionalLong.of(198), UtilsMath.parseLong("+198"));
		assertEquals(OptionalLong.of(-198), UtilsMath.parseLong("-198"));
		assertEquals(OptionalLong.of(Integer.parseInt("0198")), UtilsMath.parseLong("0198"));
		assertEquals(OptionalLong.of(198), UtilsMath.parseLong("0198", 10));
		assertEquals(OptionalLong.of(53264738926553252L), UtilsMath.parseLong("53264738926553252", 10));
		assertEquals(OptionalLong.of(-53264738926553252L), UtilsMath.parseLong("-53264738926553252", 10));

		assertEquals(OptionalInt.empty(), UtilsMath.parseInt(" 198"));
		assertEquals(OptionalInt.empty(), UtilsMath.parseInt("198 "));
		assertEquals(OptionalInt.empty(), UtilsMath.parseInt("198i"));
		assertEquals(OptionalInt.empty(), UtilsMath.parseInt("198.0"));

		assertEquals(OptionalDouble.of(1.1), UtilsMath.parseDouble("1.1"));
		assertEquals(OptionalDouble.of(.1), UtilsMath.parseDouble(".1"));
		assertEquals(OptionalDouble.of(1.), UtilsMath.parseDouble("1."));
		assertEquals(OptionalDouble.of(0), UtilsMath.parseDouble("0"));
		assertEquals(OptionalDouble.of(-0.0), UtilsMath.parseDouble("-0.0"));
		assertEquals(OptionalDouble.of(-0.0), UtilsMath.parseDouble("  -0.0  "));

		assertTrue(UtilsMath.parseDouble("NaN").isPresent());
		assertTrue(UtilsMath.parseDouble("+NaN").isPresent());
		assertTrue(UtilsMath.parseDouble("-NaN").isPresent());
		assertTrue(UtilsMath.parseDouble("Infinity").isPresent());
		assertTrue(UtilsMath.parseDouble("+Infinity").isPresent());
		assertTrue(UtilsMath.parseDouble("-Infinity").isPresent());

		assertEquals(OptionalDouble.of(-1.5E4), UtilsMath.parseDouble("-1.5E4"));
		assertEquals(OptionalDouble.of(-1.5e4), UtilsMath.parseDouble("-1.5e4"));
		assertEquals(OptionalDouble.of(-1.5e-4), UtilsMath.parseDouble("-1.5e-4"));

		assertFalse(UtilsMath.parseDouble("").isPresent());
		assertFalse(UtilsMath.parseDouble("a").isPresent());
		assertFalse(UtilsMath.parseDouble("5,4").isPresent());
	}

}
