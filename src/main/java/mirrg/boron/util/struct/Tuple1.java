package mirrg.boron.util.struct;

public final class Tuple1<X>
{

	public final X x;

	public static <X> Tuple1<X> of(X x)
	{
		return new Tuple1<>(x);
	}

	public Tuple1(X x)
	{
		this.x = x;
	}

	public X getX()
	{
		return x;
	}

	public <X2> Tuple1<X2> deriveX(X2 x)
	{
		return new Tuple1<>(x);
	}

	@Override
	public String toString()
	{
		return "[" + x + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Tuple1<?> other = (Tuple1<?>) obj;
		if (x == null) {
			if (other.x != null) return false;
		} else if (!x.equals(other.x)) return false;
		return true;
	}

	public <T> Tuple<T, X> addFirst(T t)
	{
		return new Tuple<>(t, x);
	}

	public <T> Tuple<X, T> addLast(T t)
	{
		return new Tuple<>(x, t);
	}

	public Tuple0 removeFirst()
	{
		return Tuple0.instance;
	}

	public Tuple0 removeLast()
	{
		return Tuple0.instance;
	}

	public Struct1<X> toStruct()
	{
		return new Struct1<>(x);
	}

}
