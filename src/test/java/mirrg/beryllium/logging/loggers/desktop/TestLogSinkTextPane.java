package mirrg.beryllium.logging.loggers.desktop;

import static org.junit.Assert.*;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.Test;

@SuppressWarnings("deprecation")
class TestLogSinkTextPane
{

	@Test
	public void test_LogSinkTextPane() throws Exception
	{
		JFrame frame = new JFrame();
		frame.setLayout(new CardLayout());
		LogSinkTextPane logSink = new LogSinkTextPane(8);
		logSink.logger("Test").fatal("001");
		logSink.logger("Test").fatal("001");
		logSink.logger("Test").fatal("001");
		logSink.logger("Test").fatal("001");
		logSink.logger("Test").fatal("001");
		logSink.logger("Test").error("002");
		logSink.logger("Test").warn("003");
		logSink.logger("Test").info("004");
		logSink.logger("Test").debug("005");
		logSink.logger("Test").trace("006");
		logSink.scrollPane.setPreferredSize(new Dimension(300, 200));
		frame.add(logSink.component);
		Thread.sleep(1000);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		if (!logSink.textPane.getText().matches(""
			+ ".{23} \\[FATAL] \\[Test] 001" + System.lineSeparator()
			+ ".{23} \\[FATAL] \\[Test] 001" + System.lineSeparator()
			+ ".{23} \\[FATAL] \\[Test] 001" + System.lineSeparator()
			+ ".{23} \\[ERROR] \\[Test] 002" + System.lineSeparator()
			+ ".{23} \\[WARN]  \\[Test] 003" + System.lineSeparator()
			+ ".{23} \\[INFO]  \\[Test] 004" + System.lineSeparator()
			+ ".{23} \\[DEBUG] \\[Test] 005" + System.lineSeparator()
			+ ".{23} \\[TRACE] \\[Test] 006")) {
			fail();
		}
		Thread.sleep(1000);
		frame.dispose();
	}

}
