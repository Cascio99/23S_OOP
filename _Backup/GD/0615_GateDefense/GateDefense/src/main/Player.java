package main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Player extends JLabel implements Movable {
	private int HP = 5;

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	// 플레이어 좌표(위치)
	private int x;
	private int y;

	// 움직이는 상태
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	// 정지한 상태
	private boolean move;

	// 공격하는 상태
	private boolean attack;

	// 벽에 충돌한 상태
	private boolean leftWallCrash;
	private boolean rightWallCrash;
	private boolean topWallCrash;

	// 플레이어 속도 상태
	private static final int SPEED = 4;
	private static final int JUMP_SPEED = 2;

	// 플레이어가 보고 있는 방향(왼쪽, 오른쪽)
	private PlayerDirection playerDirection;
	private ImageIcon playerMoveR, playerMoveL;
	private ImageIcon playerR, playerL;
	private ImageIcon playerAttackR, playerAttackL;

	public Player() { // 생성자
		initObject();
		initSetting();
		initBackGroundPlayerService();
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

	public void setMove(boolean move) {
		this.move = move;
	}

	public boolean isMove() {
		return move;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	public boolean isAttack() {
		return attack;
	}

	public boolean isLeftWallCrash() {
		return leftWallCrash;
	}

	public boolean isRightWallCrash() {
		return rightWallCrash;
	}

	public boolean isTopWallCrash() {
		return topWallCrash;
	}

	public void setLeftWallCrash(boolean leftWallCrash) {
		this.leftWallCrash = leftWallCrash;
	}

	public void setRightWallCrash(boolean rightWallCrash) {
		this.rightWallCrash = rightWallCrash;
	}

	public void setTopWallCrash(boolean topWallCrash) {
		this.topWallCrash = topWallCrash;
	}

	private void initObject() {
		playerR = new ImageIcon("image/playerR.png");
		playerL = new ImageIcon("image/playerL.png");
		playerMoveR = new ImageIcon("image/playerMoveR.gif");
		playerMoveL = new ImageIcon("image/playerMoveL.gif");
		playerAttackR = new ImageIcon("image/playerAttackR.gif");
		playerAttackL = new ImageIcon("image/playerAttackL.gif");
	}

	private void initSetting() {
		// 초기 위치
		x = 80;
		y = 600;

		// 초기에 움직이지 않으므로 false
		left = right = up = down = false;
		move = false;

		leftWallCrash = rightWallCrash = topWallCrash = false;

		attack = false;

		playerDirection = PlayerDirection.RIGHT;

		setIcon(playerR); // 처음에 오른쪽을 보고 있음
		setSize(110, 110); // Icon 크기는 50,50
		setLocation(x, y);
	}

	private void initBackGroundPlayerService() { // player를 넘겨준다
		new Thread(new BackGroundPlayerService(this)).start(); // this가 player이므로 this 넘겨줌
	}

	// 공격!!
	public void attack() {
		attack = true;
		new Thread(() -> {
			if (playerDirection == PlayerDirection.LEFT) { // player가 왼쪽을 보고 있을 때
				while (attack && (playerDirection == PlayerDirection.LEFT)) { // 계속 왼쪽 오른쪽 와리가리 하는 버그 해결을 위해 &&()추가
					System.out.println("[Player] attack left");
					setIcon(playerAttackL);
					try {
						Thread.sleep(170); // 공격 속도
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				setIcon(playerL);

			} else if (playerDirection == PlayerDirection.RIGHT) { // player가 오른쪽을 보고 있을 때
				while (attack && (playerDirection == PlayerDirection.RIGHT)) {
					System.out.println("[Player] attack right");
					setIcon(playerAttackR);
					try {
						Thread.sleep(170);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				setIcon(playerR);
			}
		}).start();
	}

	// movable interface 구현메서드들(이벤트 핸들러)
	@Override
	public void left() {
		System.out.println("[Player] move left");
		playerDirection = PlayerDirection.LEFT;
		left = true;
		new Thread(() -> { // Thread 안의 interface Runnable을 람다식으로 구현
			while (left) {
				setIcon(playerMoveL);
				x -= SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);// 0.01초
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void right() {
		System.out.println("[Player] move right");
		playerDirection = PlayerDirection.RIGHT;
		right = true;
		// move = true;
		new Thread(() -> { // Thread 안의 interface Runnable을 람다식으로 구현
			while (right) {
				setIcon(playerMoveR);
				x += SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10); // 0.01초
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// left + up, right + up 두가지가 가능
	@Override
	public void up() {
		System.out.println("[Player] move up");
		up = true;
		new Thread(() -> {
			for (int i = 0; i < 190 / JUMP_SPEED; i++) { // 끝이 있으므로 while이 아닌 for로 구현, 점프 여유 필요
				y -= JUMP_SPEED; // 화면 상단이 0이므로 '-'해야함
				setLocation(x, y);
				if (isTopWallCrash() == true)
					break;
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			up = false; // left,right 처럼 키보드를 뗄 때 false를 주지 않고 직접 준다.
			down(); // 바로 down을 이어준다.
		}).start();
	}

	@Override
	public void down() {
		System.out.println("[Player] move down");
		down = true;
		new Thread(() -> {
			while (down) { // 여긴 멈춰야하니까 while로 구현
				y += JUMP_SPEED; // 화면 상단이 0이므로 '+'해야함
				setLocation(x, y);
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			down = false; // left,right 처럼 키보드를 뗄 때 false를 주지 않고 직접 준다.
		}).start();
	}

	public void leftStop() {
		System.out.println("[Player] leftStop");
		setIcon(playerL);
	}

	public void rightStop() {
		System.out.println("[Player] rightStop");
		setIcon(playerR);
	}
}
