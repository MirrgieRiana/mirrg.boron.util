package mirrg.boron.util.struct;

import java.util.Map.Entry;

public final class Tuple<X, Y> implements Entry<X, Y>
{

	public final X x;
	public final Y y;

	public Tuple(X x, Y y)
	{
		this.x = x;
		this.y = y;
	}

	public X getX()
	{
		return x;
	}

	public Tuple<X, Y> deriveX(X x)
	{
		return new Tuple<>(x, y);
	}

	public Y getY()
	{
		return y;
	}

	public Tuple<X, Y> deriveY(Y y)
	{
		return new Tuple<>(x, y);
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Tuple<?, ?> other = (Tuple<?, ?>) obj;
		if (x == null) {
			if (other.x != null) return false;
		} else if (!x.equals(other.x)) return false;
		if (y == null) {
			if (other.y != null) return false;
		} else if (!y.equals(other.y)) return false;
		return true;
	}

	@Override
	public X getKey()
	{
		return x;
	}

	@Override
	public Y getValue()
	{
		return y;
	}

	@Override
	public Y setValue(Y value)
	{
		throw new UnsupportedOperationException("setValue");
	}

	public <T> Tuple3<T, X, Y> addFirst(T t)
	{
		return new Tuple3<>(t, x, y);
	}

	public <T> Tuple3<X, Y, T> addLast(T t)
	{
		return new Tuple3<>(x, y, t);
	}

	public Tuple1<Y> removeFirst()
	{
		return new Tuple1<>(y);
	}

	public Tuple1<X> removeLast()
	{
		return new Tuple1<>(x);
	}

	public Struct2<X, Y> toStruct()
	{
		return new Struct2<>(x, y);
	}

}
