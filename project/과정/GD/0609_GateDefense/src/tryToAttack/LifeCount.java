package tryToAttack;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class LifeCount extends JLabel implements Runnable {
	public static int life;

	LifeCount() {
		initObject();
		initSetting();
	}

	private void initObject() {
		setFont(new Font("Serif", Font.BOLD, 30));
		setForeground(new Color(255, 255, 255));

	}

	private void initSetting() {
		life = 3; // 초기 life는 3으로 시작
		setSize(30, 30);
		setLocation(930, 15);

	}

	@Override
	public void run() {
		// if(몬스터가 맵의 제일 왼쪽에 도착하면)
		// --life;
		while (true) {
			setText(life + "");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void start() {
		new Thread(this).start();
	}
}
