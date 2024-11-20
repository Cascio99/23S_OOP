package main;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;

//insertcoin: 목숨 3개 늘려줄 코인 넣는 방법: I 누르면 됨 --> 텍스트 띄울 클래스
public class InsertCoin extends JLabel {
	InsertCoin() {
		initObject();
		initSetting();
	}

	private void initSetting() {
		setFont(new Font("Serif", Font.BOLD, 30));
		setForeground(new Color(0, 0, 0));
	}

	private void initObject() {
		setText("Press 'I' button to Insert Coin");
		setSize(400, 30);
		setLocation(10, 10);
	}
}
