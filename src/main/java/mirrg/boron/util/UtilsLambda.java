package mirrg.boron.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import mirrg.boron.util.struct.Struct1;

public class UtilsLambda
{

	public static <T> T get(T t, Consumer<? super T> consumer)
	{
		consumer.accept(t);
		return t;
	}

	public static <T> T get(Supplier<? extends T> supplier)
	{
		return supplier.get();
	}

	//

	/**
	 * 添え字番号付きfor。
	 */
	public static <T> void forEach(Stream<T> stream, ObjIntConsumer<? super T> consumer)
	{
		Struct1<Integer> sIndex = new Struct1<>(0);
		stream
			.sequential()
			.forEach(object -> {
				consumer.accept(object, sIndex.x);
				sIndex.x++;
			});
	}

	/**
	 * 添え字番号付きmap。
	 */
	public static <I, O> Stream<O> map(Stream<I> stream, ObjIntToObj<? super I, ? extends O> function)
	{
		Struct1<Integer> sIndex = new Struct1<>(0);
		return stream.sequential().map(object -> {
			O o2 = function.apply(object, sIndex.x);
			sIndex.x++;
			return o2;
		});
	}

	@FunctionalInterface
	public static interface ObjIntToObj<O1, O2>
	{

		public O2 apply(O1 o1, int i);

	}

	/**
	 * 添え字番号付きfilter。
	 */
	public static <T> Stream<T> filter(Stream<T> stream, ObjIntToBoolean<? super T> predicate)
	{
		Struct1<Integer> sIndex = new Struct1<>(0);
		return stream.sequential().filter(object -> {
			boolean flag = predicate.test(object, sIndex.x);
			sIndex.x++;
			return flag;
		});
	}

	@FunctionalInterface
	public static interface ObjIntToBoolean<O>
	{

		public boolean test(O o, int i);

	}

	//

	public static <T> Stream<T> toStream(Iterator<T> iterator)
	{
		return StreamSupport.stream(
			Spliterators.spliteratorUnknownSize(
				iterator,
				Spliterator.ORDERED), false);
	}

	public static <T> Stream<T> toStream(Enumeration<T> enumeration)
	{
		return toStream(toIterator(enumeration));
	}

	public static <T> Iterator<T> toIterator(Enumeration<T> enumeration)
	{
		return new Iterator<T>() {
			@Override
			public T next()
			{
				return enumeration.nextElement();
			}

			@Override
			public boolean hasNext()
			{
				return enumeration.hasMoreElements();
			}
		};
	}

	public static <T> Enumeration<T> toEnumeration(Iterator<T> iterator)
	{
		return new Enumeration<T>() {
			@Override
			public T nextElement()
			{
				return iterator.next();
			}

			@Override
			public boolean hasMoreElements()
			{
				return iterator.hasNext();
			}
		};
	}

	//

	public static IntStream rangeReverse(int minInclusive, int maxExclusive)
	{
		return IntStream.range(minInclusive, maxExclusive)
			.map(i -> maxExclusive - i + minInclusive - 1);
	}

	/**
	 * このメソッドは一旦ストリームのすべての要素を取得します。
	 */
	public static <T> Stream<T> reverse(Stream<T> stream)
	{
		ArrayList<T> list = stream
			.collect(Collectors.toCollection(ArrayList::new));
		return rangeReverse(0, list.size())
			.mapToObj(list::get);
	}

	public static <I, O> Stream<O> filterClass(Stream<I> stream, Class<O> clazz)
	{
		return stream
			.filter(clazz::isInstance)
			.map(clazz::cast);
	}

	public static <T> Stream<T> filterPresent(Stream<Optional<T>> soT)
	{
		return soT
			.filter(oT -> oT.isPresent())
			.map(oT -> oT.get());
	}

}
