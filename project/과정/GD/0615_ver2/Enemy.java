package ver2;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

enum EnemyState {
	LIVE, DEAD;
}

public class Enemy extends JLabel implements Movable {
	private int x;
	private int y;

	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	private EnemyState enemyState;

	private EnemyDirection enemyDirection; // 왼쪽만 볼거라 필요 없음(일단 구현)

	private boolean leftWallCrash;
	private boolean rightWallCrash;

	private static final int SPEED = 1;
	private static final int JUMPSPEED = 1;

	private ImageIcon enemyR;
	private ImageIcon enemyL;
	private ImageIcon chiefL;

	private Gate gate; // gate 위치에서 enemy가 나와야 하므로 생성자에 Gate 객체 필요

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

	public boolean isRightWallCrash() {
		return rightWallCrash;
	}

	public EnemyState getEnemyState() {
		return this.enemyState;
	}

	public void setLeftWallCrash(boolean leftWallCrash) {
		this.leftWallCrash = leftWallCrash;
	}

	public void setRightWallCrash(boolean rightWallCrash) {
		this.rightWallCrash = rightWallCrash;
	}

	public void start() {
		initBackgroundEnemyService();
		enemyState = EnemyState.LIVE;
		enemyDirection = EnemyDirection.LEFT;
		left();
	}

	private void initObject() {
		enemyR = new ImageIcon("image/좀비(걷기 오른쪽).gif");
		enemyL = new ImageIcon("image/좀비(걷기 왼쪽).gif");
		chiefL = new ImageIcon("image/족장걷기(왼쪽).gif");
	}

	private void initSetting() {
//		x = 600;
//		y = 150;
		x = gate.getX();
		y = gate.getY();

		left = right = up = down = false;

		leftWallCrash = rightWallCrash = false;

		setIcon(enemyR);
		setSize(110, 110);
		setLocation(x, y);
	}

	private void initBackgroundEnemyService() {
		new Thread(new BackGroundEnemyService(this)).start();
	}

	@Override
	public void up() {
		System.out.println("Enemy going UP");
		up = true;
		Thread t = new Thread(() -> {
			for (int i = 0; i < 130 / JUMPSPEED; i++) {
				y = y - (JUMPSPEED);
				setLocation(x, y);
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					System.out.println("위쪽 이동중 인터럽트 발생 : " + e.getMessage());
				}
			}
			up = false;
			down();
		});
		t.start();
	}

	@Override
	public void down() {
		System.out.println("Enemy going DOWN");
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
	public void left() {
		System.out.println("Enemy going LEFT");
		enemyDirection = EnemyDirection.LEFT;
		Random rand = new Random();
		int who = rand.nextInt(2);
		if(who == 0)
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
	public void right() {
		System.out.println("Enemy going RIGHT");
		enemyDirection = EnemyDirection.RIGHT;
		setIcon(enemyR);
		right = true;
		Thread t = new Thread(() -> {
			while (right) {
				x = x + SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					System.out.println("오른쪽 이동중 인터럽트 발생 : " + e.getMessage());
				}
			}
		});
		t.start();
	}

}
