package tryToAttack;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Head extends JLabel {
	private ImageIcon head;

	Head() {
		initObject();
		initSetting();
	}

	private void initObject() {
		head = new ImageIcon("image/생명.png");
	}

	private void initSetting() {
		setIcon(head);
		setSize(64, 64);
		setLocation(880, 0);
	}
}
