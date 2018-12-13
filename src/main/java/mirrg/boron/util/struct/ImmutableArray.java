package mirrg.boron.util.struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mirrg.boron.util.suppliterator.ISuppliterator;
import mirrg.boron.util.suppliterator.SuppliteratorNullableBase;

public final class ImmutableArray<T> implements Iterable<T>
{

	private final T[] array;

	private ImmutableArray(T[] array)
	{
		this.array = array;
	}

	//

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> ImmutableArray<T> of(T... array)
	{
		T[] array2 = (T[]) new Object[array.length];
		Class<?> clazz = array.getClass().getComponentType();
		for (int i = 0; i < array.length; i++) {
			if (!clazz.isInstance(array[i])) throw new ClassCastException();
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	//

	public static ImmutableArray<Byte> ofArray(byte... array)
	{
		Byte[] array2 = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	public static ImmutableArray<Character> ofArray(char... array)
	{
		Character[] array2 = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	public static ImmutableArray<Short> ofArray(short... array)
	{
		Short[] array2 = new Short[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	public static ImmutableArray<Integer> ofArray(int... array)
	{
		Integer[] array2 = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	public static ImmutableArray<Long> ofArray(long... array)
	{
		Long[] array2 = new Long[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	public static ImmutableArray<Float> ofArray(float... array)
	{
		Float[] array2 = new Float[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	public static ImmutableArray<Double> ofArray(double... array)
	{
		Double[] array2 = new Double[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	public static ImmutableArray<Boolean> ofArray(boolean... array)
	{
		Boolean[] array2 = new Boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = array[i];
		}
		return new ImmutableArray<>(array2);
	}

	//

	@SuppressWarnings("unchecked")
	public static <T> ImmutableArray<T> ofList(List<? extends T> array)
	{
		T[] array2 = (T[]) new Object[array.size()];
		int i = 0;
		for (T t : array) {
			array2[i] = t;
			i++;
		}
		return new ImmutableArray<>(array2);
	}

	public static <T> ImmutableArray<T> ofStream(Stream<? extends T> stream)
	{
		return ofList((List<? extends T>) stream.collect(Collectors.toCollection(ArrayList::new)));
	}

	public static <T> ImmutableArray<T> ofIterable(Iterable<? extends T> iterable)
	{
		ArrayList<T> list = new ArrayList<>();
		Iterator<? extends T> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return ofList(list);
	}

	public static <T> ImmutableArray<T> ofEnumeration(Enumeration<? extends T> enumeration)
	{
		ArrayList<T> list = new ArrayList<>();
		while (enumeration.hasMoreElements()) {
			list.add(enumeration.nextElement());
		}
		return ofList(list);
	}

	//

	public int length()
	{
		return array.length;
	}

	public T get(int index)
	{
		return array[index];
	}

	//

	public T[] toArray(IntFunction<T[]> arraySupplier)
	{
		T[] array2 = arraySupplier.apply(array.length);
		System.arraycopy(array, 0, array2, 0, array.length);
		return array2;
	}

	public <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory)
	{
		C collection = collectionFactory.get();
		for (int i = 0; i < array.length; i++) {
			collection.add(array[i]);
		}
		return collection;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>() {
			private int i = 0;

			@Override
			public boolean hasNext()
			{
				return i < array.length;
			}

			@Override
			public T next()
			{
				return array[i++];
			}
		};
	}

	public ISuppliterator<T> suppliterator()
	{
		return new SuppliteratorNullableBase<T>() {
			private int i = 0;

			@Override
			public T nullableNextImpl()
			{
				int i2 = i;
				i++;
				return i2 < array.length ? array[i2] : null;
			}
		};
	}

	public Enumeration<T> values()
	{
		return new Enumeration<T>() {

			private int i = 0;

			@Override
			public boolean hasMoreElements()
			{
				return i < array.length;
			}

			@Override
			public T nextElement()
			{
				return array[i++];
			}

		};
	}

	public Stream<T> stream()
	{
		return Stream.of(array);
	}

	public ISuppliterator<T> toSuppliterator()
	{
		return ISuppliterator.ofIterable(this);
	}

	public void forEach(ObjIntConsumer<T> consumer)
	{
		for (int i = 0; i < array.length; i++) {
			consumer.accept(array[i], i);
		}
	}

	//

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) sb.append(", ");
			sb.append(array[i].toString());
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		for (int i = 0; i < array.length; i++) {
			result = prime * result + ((array[i] == null) ? 0 : array[i].hashCode());
		}
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ImmutableArray<?> other = (ImmutableArray<?>) obj;
		if (array.length != other.array.length) return false;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				if (other.array[i] != null) return false;
			} else if (!array[i].equals(other.array[i])) return false;
		}
		return true;
	}

}
