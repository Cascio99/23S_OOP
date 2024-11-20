package main;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class CountDown extends JLabel implements Runnable {
	public static boolean countOver;

	CountDown() {
		initObject();
		initSetting();
	}

	private void initObject() {
		setFont(new Font("Serif", Font.BOLD, 80));
		setForeground(new Color(255, 255, 255));
	}

	private void initSetting() {
		countOver = false;
		setSize(100, 100);
		setLocation(470, 0);
	}

	@Override
	public void run() {
		for (int i = 60; i >= 0; i--) {
			setText(i + "");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		countOver = true;
		JOptionPane.showMessageDialog(null, "You Win!", "You Win!", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	public void start() {
		new Thread(this).start();
	}
}
