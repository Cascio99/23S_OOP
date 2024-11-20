package main;

import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
//enemy: 부서지지 않은 문(gate)에서 나오는 몬스터
public class Enemy extends JLabel implements Movable {
	private int x;
	private int y;

	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

//	게임 설계상 enemy는 왼쪽 벽 충돌만 확인하면 됨
	private boolean leftWallCrash;

	private static final int SPEED = 1;
	private static final int JUMPSPEED = 1;

	private ImageIcon enemyL;	//쫄병
	private ImageIcon chiefL;	//족장

	// gate 위치에서 enemy가 나와야 하므로 생성자에 Gate 객체 필요
	private Gate gate;

	public Enemy(Gate gate) {
		this.gate = gate;
		initObject();
		initSetting();
	}

	// getters and setters
	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isLeftWallCrash() {
		return leftWallCrash;
	}

	public void setLeftWallCrash(boolean leftWallCrash) {
		this.leftWallCrash = leftWallCrash;
	}

	public void start() {
		initBackgroundEnemyService();
		left();
	}

	private void initObject() {
		enemyL = new ImageIcon("image/enemy.gif");
		chiefL = new ImageIcon("image/enemyChief.gif");
	}

	private void initSetting() {
		x = gate.getX();
		y = gate.getY();

		left = right = up = down = false;

		leftWallCrash = false;

		setIcon(enemyL);
		setSize(110, 110);
		setLocation(x, y);
	}

	private void initBackgroundEnemyService() {
		new Thread(new BackGroundEnemyService(this)).start();
	}

	// left(), up()만 필요함
	@Override
	public void left() {
		Random rand = new Random();
		int who = rand.nextInt(2);
		if (who == 0)
			setIcon(enemyL);
		else
			setIcon(chiefL);
		left = true;
		Thread t = new Thread(() -> {
			while (left) {
				x = x - SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					System.out.println("왼쪽 이동중 인터럽트 발생 : " + e.getMessage());
				}
			}
		});
		t.start();
	}

	@Override
	public void down() {
		down = true;
		Thread t = new Thread(() -> {
			while (down) {
				y = y + (JUMPSPEED);
				setLocation(x, y);
				try {
					Thread.sleep(3);
				} catch (Exception e) {
					System.out.println("아래쪽 이동중 인터럽트 발생 : " + e.getMessage());
				}
			}
		});
		t.start();
	}

	@Override
	public void right() {
		// Unreachable_code
	}

	@Override
	public void up() {
		// Unreachable_code
	}
}
