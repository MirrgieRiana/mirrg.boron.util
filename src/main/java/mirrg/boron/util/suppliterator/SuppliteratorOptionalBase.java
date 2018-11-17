package mirrg.boron.util.suppliterator;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class SuppliteratorOptionalBase<T> implements ISuppliterator<T>
{

	private boolean closed = false;

	@Override
	public final Optional<T> next()
	{
		if (closed) throw new NoSuchElementException();
		Optional<T> next = nextImpl();
		if (!next.isPresent()) closed = true;
		return next;
	}

	@Override
	public final T nullableNext()
	{
		return next().orElse(null);
	}

	/**
	 * このメソッドは一度emptyを返すと以降呼び出されることはありません。
	 */
	protected abstract Optional<T> nextImpl();

}
