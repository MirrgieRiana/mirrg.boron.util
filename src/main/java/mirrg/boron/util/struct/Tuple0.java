package mirrg.boron.util.struct;

public final class Tuple0
{

	public final static Tuple0 instance = new Tuple0();

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

	public <T> Tuple1<T> addFirst(T t)
	{
		return new Tuple1<>(t);
	}

	public <T> Tuple1<T> addLast(T t)
	{
		return new Tuple1<>(t);
	}

	public Struct0 toStruct()
	{
		return Struct0.instance;
	}

}
