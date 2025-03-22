package main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
//head: 화면 우상단에 이미지(주인공 목숨)를 띄우기 위한 클래스
public class Head extends JLabel {
	private ImageIcon head;

	Head() {
		initObject();
		initSetting();
	}

	private void initObject() {
		head = new ImageIcon("image/life.png");
	}

	private void initSetting() {
		setIcon(head);
		setSize(64, 64);
		setLocation(880, 0);
	}
}
