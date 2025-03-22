package main;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//CountDown: 게임 플레이 시간 == 60초
public class CountDown extends JLabel implements Runnable {
	public static boolean countOver;

	CountDown() {
		initObject();
		initSetting();
	}

	private void initObject() {
		setFont(new Font("Serif", Font.BOLD, 80));
		setForeground(new Color(0, 0, 0));
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
		JOptionPane.showMessageDialog(null, "You saved the village!", "You win!", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	public void start() {
		new Thread(this).start();
	}
}
