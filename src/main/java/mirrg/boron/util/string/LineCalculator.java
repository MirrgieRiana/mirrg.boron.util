package mirrg.boron.util.string;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 行番号と行頭の文字番号の対応。行番号は1から始まり、文字番号は0から始まる。
 */
public class LineCalculator
{

	private static Pattern lineBreak = Pattern.compile("(?:\n|\r\n?)");

	public static LineCalculator get(String text)
	{
		LineCalculator lineCalculator = new LineCalculator();
		Matcher matcher = lineBreak.matcher(text);

		lineCalculator.text = text;
		lineCalculator.lines = 1;

		//
		lineCalculator.lineNumberToCharacterIndex.put(1, 0);
		lineCalculator.entries.add(new int[] {
			1, 0,
		});
		//

		int indexPreviousStartIndex = 0;
		while (matcher.find()) {

			int indexStartMatch = matcher.start();
			int lengthLineBreak = matcher.group(0).length();

			//
			lineCalculator.lineNumberToCharacterIndex.put(lineCalculator.lines + 1, indexStartMatch + lengthLineBreak);
			lineCalculator.entries.add(new int[] {
				lineCalculator.lines + 1, indexStartMatch + lengthLineBreak,
			});
			lineCalculator.contentLengthes.add(indexStartMatch - indexPreviousStartIndex);
			lineCalculator.lineLengthes.add(indexStartMatch + lengthLineBreak - indexPreviousStartIndex);
			indexPreviousStartIndex = indexStartMatch + lengthLineBreak;
			//

			lineCalculator.lines++;
		}

		//
		lineCalculator.contentLengthes.add(text.length() - indexPreviousStartIndex);
		lineCalculator.lineLengthes.add(text.length() - indexPreviousStartIndex);
		//

		return lineCalculator;
	}

	//

	private String text;
	private Hashtable<Integer, Integer> lineNumberToCharacterIndex = new Hashtable<>();
	private ArrayList<int[]> entries = new ArrayList<>();
	private ArrayList<Integer> contentLengthes = new ArrayList<>();
	private ArrayList<Integer> lineLengthes = new ArrayList<>();
	private int lines;

	private LineCalculator()
	{

	}

	public String getText()
	{
		return text;
	}

	/**
	 * 最後の改行の直後も1行としてカウントされます。
	 * すなわち、"\n"の行数は2です。
	 */
	public int getLineCount()
	{
		return lines;
	}

	/**
	 * 文字列長以上の値を入れた場合、最後の行扱いになる。
	 */
	public int getLineNumber(int characterIndex)
	{
		if (characterIndex < 0) {
			throw new IllegalArgumentException("characterIndex must be >= 0: " + characterIndex);
		}
		if (characterIndex >= text.length()) {
			return entries.get(entries.size() - 1)[0];
		}

		int now = 0;
		for (int[] entry : entries) {
			if (characterIndex >= entry[1]) {
				now = entry[0];
			} else {
				return now;
			}
		}

		return now;
	}

	public Set<Entry<Integer, Integer>> entrySet()
	{
		return lineNumberToCharacterIndex.entrySet();
	}

	public int getStartIndex(int lineNumber)
	{
		return lineNumberToCharacterIndex.get(lineNumber);
	}

	public int getContentLength(int lineNumber)
	{
		return contentLengthes.get(lineNumber - 1);
	}

	public String getContent(int lineNumber)
	{
		return text.substring(getStartIndex(lineNumber), getStartIndex(lineNumber) + getContentLength(lineNumber));
	}

	public int getLineLength(int lineNumber)
	{
		return lineLengthes.get(lineNumber - 1);
	}

	public String getLine(int lineNumber)
	{
		return text.substring(getStartIndex(lineNumber), getStartIndex(lineNumber) + getLineLength(lineNumber));
	}

}
