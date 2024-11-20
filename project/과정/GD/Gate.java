package tryToAttack2;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Gate extends JLabel implements Runnable {
	// 위치
	private int x;
	private int y;

	// 체력
	private int hp;

	// image 변수
	private ImageIcon gateImage;

	// 필요한 객체들
	private boolean hit;
	private int max = 900; // 랜덤 범위 지정할 때 필요한 변수들
	private int min = 100;

	private Player player; // 좌표 겹치는지 확인하려면 필요

	public Gate(Player player) { // 생성자
		this.player = player;
		initObject();
		initSetting();
	}

	/*
	 * public void start() { initBackGroundGateService(); }
	 */

	// getters and setters
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean getHit() {
		return hit;
	}

	public void initObject() {
		gateImage = new ImageIcon("image/gate4.png");
	}

	public void initSetting() {
		// 초기 위치
		x = 600;
		y = 570; // 570이 1층 높이

		// 체력
		hp = 5;
		hit = false;

		setIcon(gateImage);
		setSize(154, 128);
		setLocation(x, y); // 처음 위치
	}

	public void BeingAttacked() {
		new Thread(() -> {
			while(Math.abs(x - player.getX()) < 150 && Math.abs(y - player.getY()) < 150
					&& player.isAttack()) {
				if (hp > 0) {
					System.out.println("BeingAttacked!");
					hp--;
				} else {
					ImageIcon destroy = new ImageIcon("image/게이트 파괴5.png");
					setIcon(destroy);
				}
				if (this.hp <= 0) { // gate hp 0이 되면 사라져야 함
					System.out.println("gate hp: 0");
					gateImage = new ImageIcon("image/게이트 파괴5.png");
					setIcon(gateImage);
					this.remove(this);
				} else if (this.hp == 4) { // 아직 gate 안 부서짐
					System.out.println("gate hp: 4");
					hp--;
					gateImage = new ImageIcon("image/게이트 파괴1.png");
					setIcon(gateImage);
				} else if (this.hp == 3) { // 아직 gate 안 부서짐
					System.out.println("gate hp: 3");
					hp--;
					gateImage = new ImageIcon("image/게이트 파괴2.png");
					setIcon(gateImage);
				} else if (this.hp == 2) { // 아직 gate 안 부서짐
					System.out.println("gate hp: 2");
					hp--;
					gateImage = new ImageIcon("image/게이트 파괴3.png");
					setIcon(gateImage);
				} else if (this.hp == 1) { // 아직 gate 안 부서짐
					System.out.println("게이트 피: 1");
					hp--;
					gateImage = new ImageIcon("image/게이트 파괴4.png");
					setIcon(gateImage);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	@Override
	public void run() {
		while (!CountDown.countOver) { // countDown이 종료되면 랜덤 생성 종료
			try {
				Random rand = new Random();
				x = rand.nextInt(max - min) + min; // 화면 밖으로 안 나가도록 랜덤값 생성
				setLocation(x, y);
				Thread.sleep(15000); // 15초 동안 게이트 부셔야 함!(stage1: 3번 때리면 깨짐)
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
