package tryToAttack;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		countOver = true;
	}

	public void start() {
		new Thread(this).start();
	}
}
