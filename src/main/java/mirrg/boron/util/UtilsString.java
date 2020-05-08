package mirrg.boron.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UtilsString
{

	public static String repeat(char ch, int t)
	{
		StringBuffer sb = new StringBuffer(t);
		for (int i = 0; i < t; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

	public static String repeat(String string, int t)
	{
		StringBuffer sb = new StringBuffer(string.length() * t);
		for (int i = 0; i < t; i++) {
			sb.append(string);
		}
		return sb.toString();
	}

	//

	public static String fillLeft(char ch, String string, int length)
	{
		if (string.length() >= length) return string;
		return repeat(ch, length - string.length()) + string;
	}

	public static String fillLeft(String string, int length)
	{
		return fillLeft(' ', string, length);
	}

	public static String fillRight(char ch, String string, int length)
	{
		if (string.length() >= length) return string;
		return string + repeat(ch, length - string.length());
	}

	public static String fillRight(String string, int length)
	{
		return fillRight(' ', string, length);
	}

	//

	public static String getStackTrace(Exception e)
	{
		StringWriter out = new StringWriter();
		e.printStackTrace(new PrintWriter(out));
		return out.toString();
	}

	//

	/**
	 * 先頭だけを大文字にし、残りは変えません。
	 */
	public static String toUpperCaseHead(String string)
	{
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

}
