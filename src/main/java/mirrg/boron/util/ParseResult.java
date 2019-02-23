package mirrg.boron.util;

import java.util.Optional;

public final class ParseResult<T>
{

	public final Optional<T> oValue;

	private ParseResult()
	{
		this.oValue = Optional.empty();
	}

	private ParseResult(T value)
	{
		this.oValue = Optional.of(value);
	}

	//

	private static final ParseResult<?> ERROR = new ParseResult<>();

	public static <T> ParseResult<T> error()
	{
		@SuppressWarnings("unchecked")
		ParseResult<T> t = (ParseResult<T>) ERROR;
		return t;
	}

	public static <T> ParseResult<T> of(T value)
	{
		return new ParseResult<>(value);
	}

	//

	public T get()
	{
		return oValue.get();
	}

	public boolean isSuccessful()
	{
		return oValue.isPresent();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParseResult<?> other = (ParseResult<?>) obj;
		if (oValue == null) {
			if (other.oValue != null) return false;
		} else if (!oValue.equals(other.oValue)) return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		return oValue.hashCode();
	}

	public String toString()
	{
		return isSuccessful()
			? String.format("ParseResult[%s]", get())
			: "ParseResult.error";
	}

}
