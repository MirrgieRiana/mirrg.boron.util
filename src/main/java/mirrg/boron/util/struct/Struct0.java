package mirrg.boron.util.struct;

public final class Struct0
{

	public final static Struct0 instance = new Struct0();

	public static Struct0 of()
	{
		return instance;
	}

	@Override
	public String toString()
	{
		return "[]";
	}

	@Override
	public int hashCode()
	{
		return 1;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		return true;
	}

	public <T> Struct1<T> addFirst(T t)
	{
		return new Struct1<>(t);
	}

	public <T> Struct1<T> addLast(T t)
	{
		return new Struct1<>(t);
	}

	public Tuple0 toTuple()
	{
		return Tuple0.instance;
	}

}
