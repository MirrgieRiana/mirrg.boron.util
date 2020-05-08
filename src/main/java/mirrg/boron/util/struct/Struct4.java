package mirrg.boron.util.struct;

public final class Struct4<X, Y, Z, W>
{

	public X x;
	public Y y;
	public Z z;
	public W w;

	public static <X, Y, Z, W> Struct4<X, Y, Z, W> of(X x, Y y, Z z, W w)
	{
		return new Struct4<>(x, y, z, w);
	}

	public Struct4()
	{

	}

	public Struct4(X x, Y y, Z z, W w)
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

	public void setX(X x)
	{
		this.x = x;
	}

	public Y getY()
	{
		return y;
	}

	public void setY(Y y)
	{
		this.y = y;
	}

	public Z getZ()
	{
		return z;
	}

	public void setZ(Z z)
	{
		this.z = z;
	}

	public W getW()
	{
		return w;
	}

	public void setW(W w)
	{
		this.w = w;
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
		Struct4<?, ?, ?, ?> other = (Struct4<?, ?, ?, ?>) obj;
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
	public <T> Struct5<T, X, Y, Z, W> addFirst(T t)
	{
		return new Struct5<>(t, x, y, z, w);
	}

	public <T> Struct5<X, Y, Z, W, T> addLast(T t)
	{
		return new Struct5<>(x, y, z, w, t);
	}
	*/

	public Struct3<Y, Z, W> removeFirst()
	{
		return new Struct3<>(y, z, w);
	}

	public Struct3<X, Y, Z> removeLast()
	{
		return new Struct3<>(x, y, z);
	}

	public Tuple4<X, Y, Z, W> toTuple()
	{
		return new Tuple4<>(x, y, z, w);
	}

}
