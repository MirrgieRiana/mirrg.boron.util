package mirrg.boron.util.struct;

public final class Struct3<X, Y, Z>
{

	public X x;
	public Y y;
	public Z z;

	public static <X, Y, Z> Struct3<X, Y, Z> of(X x, Y y, Z z)
	{
		return new Struct3<>(x, y, z);
	}

	public Struct3()
	{

	}

	public Struct3(X x, Y y, Z z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
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

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + ", " + z + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
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
		Struct3<?, ?, ?> other = (Struct3<?, ?, ?>) obj;
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

	public <T> Struct4<T, X, Y, Z> addFirst(T t)
	{
		return new Struct4<>(t, x, y, z);
	}

	public <T> Struct4<X, Y, Z, T> addLast(T t)
	{
		return new Struct4<>(x, y, z, t);
	}

	public Struct2<Y, Z> removeFirst()
	{
		return new Struct2<>(y, z);
	}

	public Struct2<X, Y> removeLast()
	{
		return new Struct2<>(x, y);
	}

	public Tuple3<X, Y, Z> toTuple()
	{
		return new Tuple3<>(x, y, z);
	}

}
