package mirrg.beryllium.logging.loggers.desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import mirrg.beryllium.logging.loggers.text.LogSinkPrintStream;
import mirrg.boron.util.logging.LogRelay;
import mirrg.boron.util.logging.Logger;

@SuppressWarnings("deprecation")
public class SampleLoggerTextPaneLong
{

	public static void main(String[] args) throws InterruptedException
	{
		JFrame frame = new JFrame();

		LogRelay logRelay = new LogRelay(new LogSinkPrintStream(System.out));
		Logger logger = logRelay.logger("Test");

		frame.setLayout(new BorderLayout());
		LogSinkTextPane logSinkTextPane = new LogSinkTextPane(50);
		logRelay.register(logSinkTextPane);
		logSinkTextPane.scrollPane.setPreferredSize(new Dimension(300, 200));
		frame.add(logSinkTextPane.component);

		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);

		while (true) {
			logger.info("Line Line Line Line Line Line Line Line Line Line Line Line");
			Thread.sleep(100);
		}
	}

}
