package mirrg.boron.util.struct;

public final class Tuple4<X, Y, Z, W>
{

	public final X x;
	public final Y y;
	public final Z z;
	public final W w;

	public static <X, Y, Z, W> Tuple4<X, Y, Z, W> of(X x, Y y, Z z, W w)
	{
		return new Tuple4<>(x, y, z, w);
	}

	public Tuple4(X x, Y y, Z z, W w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public X getX()
	{
		return x;
	}

	public <X2> Tuple4<X2, Y, Z, W> deriveX(X2 x)
	{
		return new Tuple4<>(x, y, z, w);
	}

	public Y getY()
	{
		return y;
	}

	public <Y2> Tuple4<X, Y2, Z, W> deriveY(Y2 y)
	{
		return new Tuple4<>(x, y, z, w);
	}

	public Z getZ()
	{
		return z;
	}

	public <Z2> Tuple4<X, Y, Z2, W> deriveZ(Z2 z)
	{
		return new Tuple4<>(x, y, z, w);
	}

	public W getW()
	{
		return w;
	}

	public <W2> Tuple4<X, Y, Z, W2> deriveW(W2 w)
	{
		return new Tuple4<>(x, y, z, w);
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + ", " + z + ", " + w + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((w == null) ? 0 : w.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		result = prime * result + ((z == null) ? 0 : z.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Tuple4<?, ?, ?, ?> other = (Tuple4<?, ?, ?, ?>) obj;
		if (w == null) {
			if (other.w != null) return false;
		} else if (!w.equals(other.w)) return false;
		if (x == null) {
			if (other.x != null) return false;
		} else if (!x.equals(other.x)) return false;
		if (y == null) {
			if (other.y != null) return false;
		} else if (!y.equals(other.y)) return false;
		if (z == null) {
			if (other.z != null) return false;
		} else if (!z.equals(other.z)) return false;
		return true;
	}

	/*
	public <T> Tuple5<T, X, Y, Z, W> addFirst(T t)
	{
		return new Tuple5<>(t, x, y, z, w);
	}

	public <T> Tuple5<X, Y, Z, W, T> addLast(T t)
	{
		return new Tuple5<>(x, y, z, w, t);
	}
	*/

	public Tuple3<Y, Z, W> removeFirst()
	{
		return new Tuple3<>(y, z, w);
	}

	public Tuple3<X, Y, Z> removeLast()
	{
		return new Tuple3<>(x, y, z);
	}

	public Struct4<X, Y, Z, W> toStruct()
	{
		return new Struct4<>(x, y, z, w);
	}

}
