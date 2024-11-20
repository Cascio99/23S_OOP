package main;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;

public class InsertCoin extends JLabel {
	InsertCoin() {
		initObject();
		initSetting();
	}

	private void initSetting() {
		setFont(new Font("Serif", Font.BOLD, 30));
		setForeground(new Color(255, 255, 255));
	}

	private void initObject() {
		setText("Press 'I' button to Insert Coin");
		setSize(400, 30);
		setLocation(10, 10);
	}
}
