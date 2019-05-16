package mirrg.boron.util.struct;

public abstract class IntArrayBase implements IIntArrayView
{

	@Override
	public String toString()
	{
		return "[" + join(", ") + "]";
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		for (int i = 0; i < length(); i++) {
			result = 31 * result + get(i);
		}
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof IIntArrayView)) return false;
		IIntArrayView other = (IIntArrayView) obj;
		if (length() != other.length()) return false;
		for (int i = 0; i < length(); i++) {
			if (get(i) != other.get(i)) return false;
		}
		return true;
	}

}
