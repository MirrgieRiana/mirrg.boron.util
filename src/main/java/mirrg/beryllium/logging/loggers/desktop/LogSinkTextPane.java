package mirrg.beryllium.logging.loggers.desktop;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SizeRequirements;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import mirrg.beryllium.logging.loggers.text.LogSinkTextBase;
import mirrg.boron.util.logging.EnumLogLevel;

/**
 * Swingコンポーネントとして出力するロガーです。
 * 表示可能な最大行数を決めることができます。
 * 大量のログが出力される可能性がある場合は、別途ファイル出力などと併用してください。
 * このクラスへの出力処理は非常に重く、
 * 大量のログを出力させるとプログラムの動作に悪影響を及ぼす可能性があります。
 */
@Deprecated // TODO 別モジュールに移動
public class LogSinkTextPane extends LogSinkTextBase
{

	public final JPanel component;
	public final MTextPane textPane;
	public final JScrollPane scrollPane;

	public LogSinkTextPane(int maxLines)
	{
		component = new JPanel();
		component.setLayout(new CardLayout());

		{
			textPane = new MTextPane(maxLines);
			scrollPane = new JScrollPane(textPane);

			// TextPaneを無改行にする副作用で幅が余ってると色が変になる対策に強引に背景色を揃える
			scrollPane.getViewport().setBackground(textPane.getBackground());
			textPane.setOpaque(false);

			// 異常な自動スクロールを禁止
			textPane.setCaret(new DefaultCaret() {
				@Override
				protected void adjustVisibility(Rectangle nloc)
				{

				}
			});

			component.add(scrollPane);
		}
	}

	@Override
	public void println(String tag, Supplier<String> sString, Optional<EnumLogLevel> oLogLevel)
	{
		textPane.println(tag, sString.get(), oLogLevel);
	}

	public class MTextPane extends JTextPane
	{

		private int maxLines;

		private DefaultStyledDocument document;
		public final Style STYLE_FATAL;
		public final Style STYLE_ERROR;
		public final Style STYLE_WARN;
		public final Style STYLE_INFO;
		public final Style STYLE_DEBUG;
		public final Style STYLE_TRACE;

		public MTextPane(int maxLines)
		{
			this.maxLines = maxLines;

			document = new DefaultStyledDocument();
			STYLE_FATAL = document.addStyle("fatal", document.getStyle(StyleContext.DEFAULT_STYLE));
			StyleConstants.setForeground(STYLE_FATAL, Color.white);
			StyleConstants.setBackground(STYLE_FATAL, Color.red);
			STYLE_ERROR = document.addStyle("error", document.getStyle(StyleContext.DEFAULT_STYLE));
			StyleConstants.setForeground(STYLE_ERROR, Color.red);
			STYLE_WARN = document.addStyle("warn", document.getStyle(StyleContext.DEFAULT_STYLE));
			StyleConstants.setForeground(STYLE_WARN, Color.decode("#ff8800"));
			STYLE_INFO = document.addStyle("info", document.getStyle(StyleContext.DEFAULT_STYLE));
			StyleConstants.setForeground(STYLE_INFO, Color.black);
			STYLE_DEBUG = document.addStyle("debug", document.getStyle(StyleContext.DEFAULT_STYLE));
			StyleConstants.setForeground(STYLE_DEBUG, Color.decode("#44aaaa"));
			STYLE_TRACE = document.addStyle("trace", document.getStyle(StyleContext.DEFAULT_STYLE));
			StyleConstants.setForeground(STYLE_TRACE, Color.decode("#aaaaaa"));

			setStyledDocument(document);
			setEditable(false);
			setFont(new Font(Font.MONOSPACED, Font.PLAIN, getFont().getSize()));
		}

		//

		private ArrayDeque<Integer> lineLengths = new ArrayDeque<>();
		private boolean isFirst = true;

		public void clear()
		{
			lineLengths.clear();
			isFirst = true;
			setText("");
		}

		private void printlnDirectlyImpl(String line, AttributeSet attributeSet)
		{
			try {

				JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
				int bottom = scrollBar.getValue() + scrollBar.getVisibleAmount() - scrollBar.getMaximum();

				// 行追加
				lineLengths.addLast(line.length());
				if (isFirst) {
					isFirst = false;
					document.insertString(document.getLength(), line, attributeSet);
				} else {
					document.insertString(document.getLength(), "\n" + line, attributeSet);
				}

				// 行あふれを消す
				while (lineLengths.size() > maxLines) {
					int length = lineLengths.removeFirst();
					document.remove(0, length + 1);
				}

				SwingUtilities.invokeLater(() -> {
					scrollBar.setValue(bottom - scrollBar.getVisibleAmount() + scrollBar.getMaximum());
				});

			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
		}

		// 自動改行禁止

		@Override
		protected EditorKit createDefaultEditorKit()
		{
			return new StyledEditorKit() {
				@Override
				public ViewFactory getViewFactory()
				{
					return new ViewFactory() {
						@Override
						public View create(Element elem)
						{
							String kind = elem.getName();
							if (kind != null) {
								if (kind.equals(AbstractDocument.ContentElementName)) {
									return new LabelView(elem);
								} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
									return new ParagraphView(elem) {
										@Override
										protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r)
										{
											SizeRequirements req = super.calculateMinorAxisRequirements(axis, r);
											req.minimum = req.preferred;
											return req;
										}

										@Override
										public int getFlowSpan(int index)
										{
											return Integer.MAX_VALUE;
										}
									};
								} else if (kind.equals(AbstractDocument.SectionElementName)) {
									return new BoxView(elem, View.Y_AXIS);
								} else if (kind.equals(StyleConstants.ComponentElementName)) {
									return new ComponentView(elem);
								} else if (kind.equals(StyleConstants.IconElementName)) {
									return new IconView(elem);
								}
							}

							// default to text display
							return new LabelView(elem);
						}
					};
				}
			};
		}

		//

		/**
		 * このメソッドはどのスレッドからでも呼び出すことができます。
		 * このメソッドは書き込みが完了するまで処理をブロッキングします。
		 */
		public void println(String tag, String string, Optional<EnumLogLevel> oLogLevel)
		{
			Style style = null;
			if (oLogLevel.isPresent()) {
				switch (oLogLevel.get()) {
					case FATAL:
						style = STYLE_FATAL;
						break;
					case ERROR:
						style = STYLE_ERROR;
						break;
					case WARN:
						style = STYLE_WARN;
						break;
					case INFO:
						style = STYLE_INFO;
						break;
					case DEBUG:
						style = STYLE_DEBUG;
						break;
					case TRACE:
						style = STYLE_TRACE;
						break;
				}
			}

			println(formatter.format(tag, string, oLogLevel), style);
		}

		/**
		 * このメソッドはどのスレッドからでも呼び出すことができます。
		 * このメソッドは書き込みが完了するまで処理をブロッキングします。
		 */
		public void println(String string, Color foreColor)
		{
			println(string, a -> {
				StyleConstants.setForeground(a, foreColor);
			});
		}

		/**
		 * このメソッドはどのスレッドからでも呼び出すことができます。
		 * このメソッドは書き込みが完了するまで処理をブロッキングします。
		 */
		public void println(String string, Color foreColor, Color backColor)
		{
			println(string, a -> {
				StyleConstants.setForeground(a, foreColor);
				StyleConstants.setBackground(a, backColor);
			});
		}

		/**
		 * このメソッドはどのスレッドからでも呼び出すことができます。
		 * このメソッドは書き込みが完了するまで処理をブロッキングします。
		 */
		public void println(String string, Consumer<SimpleAttributeSet> styleSetter)
		{
			SimpleAttributeSet attributeSet = new SimpleAttributeSet();
			styleSetter.accept(attributeSet);
			println(string, attributeSet);
		}

		/**
		 * このメソッドはどのスレッドからでも呼び出すことができます。
		 * このメソッドは書き込みが完了するまで処理をブロッキングします。
		 */
		public void println(String string)
		{
			println(string, (AttributeSet) null);
		}

		/**
		 * このメソッドはどのスレッドからでも呼び出すことができます。
		 * このメソッドは書き込みが完了するまで処理をブロッキングします。
		 */
		public void println(String string, AttributeSet attributeSet)
		{
			for (String line : string.split("\\r\\n|\\r|\\n")) {
				if (SwingUtilities.isEventDispatchThread()) {
					printlnDirectlyImpl(line, attributeSet);
				} else {
					try {
						SwingUtilities.invokeAndWait(() -> {
							printlnDirectlyImpl(line, attributeSet);
						});
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

	}

}
