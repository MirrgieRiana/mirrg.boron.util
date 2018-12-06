package mirrg.boron.util.string;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mirrg.boron.util.struct.ImmutableArray;

public final class NumericComparator implements Comparable<NumericComparator>
{

	public static final Comparator<String> COMPARATOR = (a, b) -> split(a).compareTo(split(b));

	public static int compareWithNumberImpl(String a, String b)
	{
		return split(a).compareTo(split(b));
	}

	//

	private static final Pattern PATTERN = Pattern.compile("[0-9]+|[^0-9]");

	public static NumericComparator split(String string)
	{
		ArrayList<Token> list = new ArrayList<>();
		Matcher matcher = PATTERN.matcher(string);
		while (matcher.find()) {
			String s = matcher.group();
			Token token;
			if ('0' <= s.charAt(0) && s.charAt(0) <= '9') {
				token = new Token(new BigInteger(s), getZeros(s));
			} else {
				token = new Token(s.charAt(0));
			}
			list.add(token);
		}
		return new NumericComparator(ImmutableArray.ofList(list));
	}

	private static int getZeros(String string)
	{
		int i;
		for (i = 0; i < string.length(); i++) {
			if (string.charAt(i) != '0') break;
		}
		return i;
	}

	//

	public final ImmutableArray<Token> tokens;

	public NumericComparator(ImmutableArray<Token> tokens)
	{
		this.tokens = tokens;
	}

	@Override
	public int compareTo(NumericComparator o)
	{
		int i = 0;
		while (true) {
			Token a = i < tokens.length() ? tokens.get(i) : null;
			Token b = i < o.tokens.length() ? o.tokens.get(i) : null;
			if (a == null && b == null) {
				return 0;
			} else if (a != null && b != null) {
				int c = a.compareTo(b);
				if (c != 0) return c;
			} else {
				return a == null ? -1 : 1;
			}
			i++;
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		NumericComparator other = (NumericComparator) obj;
		if (tokens == null) {
			if (other.tokens != null) return false;
		} else if (!tokens.equals(other.tokens)) return false;
		return true;
	}

	public static final class Token implements Comparable<Token>
	{

		public final char ch;
		public final BigInteger i;
		public final int zeros;
		public final boolean isInteger;

		public Token(char ch)
		{
			this.ch = ch;
			this.i = null;
			this.zeros = 0;
			this.isInteger = false;
		}

		public Token(BigInteger i, int zeros)
		{
			this.ch = '0';
			this.i = i;
			this.zeros = zeros;
			this.isInteger = true;
		}

		@Override
		public int compareTo(Token o)
		{
			if (isInteger && o.isInteger) {
				int a = i.compareTo(o.i);
				if (a != 0) return a;
				return Integer.compare(zeros, o.zeros);
			} else {
				return Character.compare(ch, o.ch);
			}
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ch;
			result = prime * result + i.hashCode();
			result = prime * result + (isInteger ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			Token other = (Token) obj;
			if (ch != other.ch) return false;
			if (!Objects.equals(i, other.i)) return false;
			if (isInteger != other.isInteger) return false;
			return true;
		}

	}

}
