package ver2;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
			 // life 값이 0이 되었을 때 "Game Over" 팝업 창을 표시하고 JFrame 종료
			if(life <= 0) {
				setText(life + "");
	        JOptionPane.showMessageDialog(null, "Game Over", "Game Over", JOptionPane.INFORMATION_MESSAGE);
	        System.exit(0);
			}
		}

		
		
		
		
	}

	public void start() {
		new Thread(this).start();
	}
}
