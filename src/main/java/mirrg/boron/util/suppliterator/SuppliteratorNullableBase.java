package mirrg.boron.util.suppliterator;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class SuppliteratorNullableBase<T> implements ISuppliterator<T>
{

	private boolean closed = false;

	@Override
	public final T nullableNext()
	{
		if (closed) throw new NoSuchElementException();
		T next = nullableNextImpl();
		if (next == null) closed = true;
		return next;
	}

	@Override
	public final Optional<T> next()
	{
		return Optional.ofNullable(nullableNext());
	}

	/**
	 * このメソッドは一度nullを返すと以降呼び出されることはありません。
	 */
	protected abstract T nullableNextImpl();

}
