package mirrg.boron.util.string;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PercentEncoding
{

	/**
	 * URLエンコードに似た方式により、文字列を[a-zA-Z0-9%]からなる文字列にエンコードします。
	 * 通常のURLエンコードでは異なる扱いになる一部の記号や空白も"%"表記で置換されます。
	 * ただし、引数escapeにより"%"の代わりとなる文字を指定することができます。
	 */
	public static byte[] encode(byte[] bytes, byte escape)
	{
		ArrayList<Byte> bytes2 = new ArrayList<>();

		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];

			if ('a' <= b && b <= 'z') {
				bytes2.add(b);
			} else if ('A' <= b && b <= 'Z') {
				bytes2.add(b);
			} else if ('0' <= b && b <= '9') {
				bytes2.add(b);
			} else {
				String s = String.format("%02X", b);
				bytes2.add(escape);
				bytes2.add((byte) s.charAt(0));
				bytes2.add((byte) s.charAt(1));
			}

		}

		byte[] bytes3 = new byte[bytes2.size()];
		for (int i = 0; i < bytes2.size(); i++) {
			bytes3[i] = bytes2.get(i);
		}

		return bytes3;
	}

	/**
	 * {@link #encode(byte[], byte)} を文字列型として呼び出すラッパーです。
	 *
	 * @param escape
	 *            この文字は1バイトのASCIIコードで表される文字でなければなりません。
	 * @param charset
	 *            この文字コードはASCIIコードがそのままのバイト列で表現される文字コードでなければなりません。
	 *            例えば、UTF-8やShift_JISは満たしますが、UTF-16は満たしません。
	 */
	public static String encode(String string, char escape, Charset charset)
	{
		return new String(encode(string.getBytes(charset), (byte) escape), charset);
	}

	/**
	 * '%'およびUTF-8によって {@link #encode(String, char, Charset)} を呼び出します。
	 */
	public static String encode(String string)
	{
		return encode(string, '%', StandardCharsets.UTF_8);
	}

	/**
	 * <p>
	 * {@link #encode(byte[])} によってエンコードされた文字列をデコードします。
	 * 入力文字列は次の正規表現に従う必要があります。
	 * そうでない場合の動作は保証されません。
	 * </p>
	 * <p>
	 * <code>(%[a-fA-F0-9][a-fA-F0-9]|.)*</code>
	 * </p>
	 * <p>
	 * ただし、引数escapeにより"%"の代わりとなる文字を指定することができます。
	 * </p>
	 */
	public static byte[] decode(byte[] bytes, byte escape)
	{
		ArrayList<Byte> bytes2 = new ArrayList<>();

		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];

			a:
			if (b == escape) {
				if (i + 2 >= bytes.length) break a;
				byte x = 0;

				byte b1 = bytes[i + 1];
				if ('a' <= b1 && b1 <= 'f') {
					x += (b1 - 'a' + 10) * 16;
				} else if ('A' <= b1 && b1 <= 'F') {
					x += (b1 - 'A' + 10) * 16;
				} else if ('0' <= b1 && b1 <= '9') {
					x += (b1 - '0' + 0) * 16;
				} else {
					break a;
				}

				byte b2 = bytes[i + 2];
				if ('a' <= b2 && b2 <= 'f') {
					x += (b2 - 'a' + 10) * 1;
				} else if ('A' <= b2 && b2 <= 'F') {
					x += (b2 - 'A' + 10) * 1;
				} else if ('0' <= b2 && b2 <= '9') {
					x += (b2 - '0' + 0) * 1;
				} else {
					break a;
				}

				bytes2.add(x);

				i += 2;
				continue;
			}

			bytes2.add((byte) b);

		}

		byte[] bytes3 = new byte[bytes2.size()];
		for (int i = 0; i < bytes2.size(); i++) {
			bytes3[i] = bytes2.get(i);
		}

		return bytes3;
	}

	/**
	 * {@link #decode(byte[], byte)} を文字列型として呼び出すラッパーです。
	 *
	 * @param escape
	 *            この文字は1バイトのASCIIコードで表される文字でなければなりません。
	 * @param charset
	 *            この文字コードはASCIIコードがそのままのバイト列で表現される文字コードでなければなりません。
	 *            例えば、UTF-8やShift_JISは満たしますが、UTF-16は満たしません。
	 */
	public static String decode(String string, char escape, Charset charset)
	{
		return new String(decode(string.getBytes(charset), (byte) escape), charset);
	}

	/**
	 * '%'およびUTF-8によって {@link #decode(String, char, Charset)} を呼び出します。
	 */
	public static String decode(String string)
	{
		return decode(string, '%', StandardCharsets.UTF_8);
	}

}
