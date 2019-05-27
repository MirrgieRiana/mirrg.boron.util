package mirrg.boron.util.hopper;

import java.awt.CardLayout;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Deque;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import mirrg.boron.util.hopper.lib.HopperSwapping;
import mirrg.boron.util.hopper.lib.HopperSwapping.FileIO;
import mirrg.boron.util.hopper.lib.HopperThread;

public class TestHopperSwapping
{

	private static volatile boolean end = false;

	public static void main(String[] args) throws Exception
	{
		HopperSwapping<String> hopper = new HopperSwapping<>(new FileIO<String>() {
			@Override
			public void write(OutputStream out, String item) throws IOException
			{
				new ObjectOutputStream(out).writeObject(item);
			}

			@Override
			public Optional<String> read(InputStream in) throws IOException
			{
				try {
					return Optional.ofNullable((String) new ObjectInputStream(in).readObject());
				} catch (EOFException e) {
					return Optional.empty();
				} catch (ClassNotFoundException e) {
					throw new IOException(e);
				}
			}
		}, new File("tmp/TestHopperSwapping"), 10);

		hopper.init();

		new HopperThread<String>(hopper) {
			int waitNs = 0;
			long i = 0;

			@Override
			protected void processImpl(Deque<String> bucket)
			{
				for (String item : bucket) {
					if (i % 1000 == 0) waitNs = (int) (Math.random() * 2_000_000);

					try {
						Thread.sleep(waitNs / 1_000_000, waitNs % 1_000_000);
					} catch (InterruptedException e) {
						throw new AssertionError();
					}
					//System.out.println(item);

					if (!item.equals("" + i)) {
						System.out.println("Unmatch: " + i + ": " + item);
					}

					if ((i + 1) % 100 == 0) System.out.println("Pop: " + (i + 1));
					i++;
				}
			}
		}.start();

		Thread thread = new Thread(() -> {
			int waitNs = 0;
			int i = 0;
			while (!end) {
				if (i % 500 == 0) waitNs = (int) (Math.random() * 2_100_000);

				try {
					Thread.sleep(waitNs / 1_000_000, waitNs % 1_000_000);
				} catch (InterruptedException e) {
					throw new AssertionError();
				}

				hopper.push("" + i);

				if ((i + 1) % 100 == 0) System.out.println("Push: " + (i + 1));
				i++;
			}
		});

		{
			JFrame frame = new JFrame();
			frame.setLayout(new CardLayout());
			JButton button = new JButton();
			button.addActionListener(e -> {
				frame.dispose();
				end = true;
				try {
					thread.join();
				} catch (InterruptedException e1) {
					throw new AssertionError();
				}
				hopper.term();
			});
			frame.add(button);
			frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			frame.setSize(300, 300);
			frame.setVisible(true);
		}

		thread.start();

	}

}
