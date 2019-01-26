package mirrg.boron.util.struct;

public class UtilsStruct
{

	public static Struct0 struct()
	{
		return Struct0.instance;
	}

	public static <X> Struct1<X> struct(X x)
	{
		return new Struct1<>(x);
	}

	public static <X, Y> Struct2<X, Y> struct(X x, Y y)
	{
		return new Struct2<>(x, y);
	}

	public static <X, Y, Z> Struct3<X, Y, Z> struct(X x, Y y, Z z)
	{
		return new Struct3<>(x, y, z);
	}

	public static <X, Y, Z, W> Struct4<X, Y, Z, W> struct(X x, Y y, Z z, W w)
	{
		return new Struct4<>(x, y, z, w);
	}

	public static Tuple0 tuple()
	{
		return Tuple0.instance;
	}

	public static <X> Tuple1<X> tuple(X x)
	{
		return new Tuple1<>(x);
	}

	public static <X, Y> Tuple<X, Y> tuple(X x, Y y)
	{
		return new Tuple<>(x, y);
	}

	public static <X, Y, Z> Tuple3<X, Y, Z> tuple(X x, Y y, Z z)
	{
		return new Tuple3<>(x, y, z);
	}

	public static <X, Y, Z, W> Tuple4<X, Y, Z, W> tuple(X x, Y y, Z z, W w)
	{
		return new Tuple4<>(x, y, z, w);
	}

}
