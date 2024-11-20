package tryToAttack;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GateFrame extends JFrame {

	private JLabel backgroundMap;
	private Player player;
	private Gate gate;
	private Enemy enemy;
	private CountDown countDown;
	private LifeCount life;
	private Head head;
	
	private List<Gate> gateList;
	

	public GateFrame() {
		initObject();
		initSetting();
		initListener();
		initThread();
		gateGarbageCollector();
		setVisible(true);
	}

	private void initObject() {
		backgroundMap = new JLabel(new ImageIcon("image/배경화면(level 3).png"));

		setContentPane(backgroundMap); // JPanel을 만들지 않고 바로 Frame에 그림 그리기

		player = new Player();
		enemy = new Enemy();
//		gate = new Gate(player);	//player랑 상호작용(좌표 겹지는지..)하기 위해 player 정보 넘겨야 함
		countDown = new CountDown();
		gateList = new ArrayList<>();
		
		life=new LifeCount();
		head=new Head();
		
		add(player); // panel에 덧붙이는 개념으로 추가
		add(enemy);
//		add(gate);
		add(countDown);
		add(life);
		add(head);
		
		
		enemy.start();
		countDown.start();
		life.start();

//		gate.start();
	}

	private void initSetting() {
		setSize(1024, 768);
		getContentPane().setLayout(null); // absolute 레이아웃(자유롭게 그림을 그릴 수 있다)

		setLocationRelativeTo(null); // Frame이 실행될때 가운데로 나오게 해준다.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창이 꺼지면 JVM도 같이 종료하기
	}

	private void initListener() {
		addKeyListener(new KeyAdapter() { // KeyPressed만 필요하므로 어댑터 이용

			// 키보드 클릭 이벤트 핸들러
			@Override
			public void keyPressed(KeyEvent e) {
				// System.out.println(e.getKeyCode());
				// <- : 37 -> : 39 위: 38 아래: 40(keyCode)

				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (!player.isLeft() && !player.isLeftWallCrash()) { // leftWallCrash가 false일 때 갈 수 있으므로
						player.left();
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (!player.isRight() && !player.isRightWallCrash()) { // right가 참이 아닐 때만 실행되게 해서 중복 실행 방지
						player.right(); // leftWallCrash가 false일 때 갈 수 있으므로
					}
					break;
				case KeyEvent.VK_UP:
					if (!player.isUp() && !player.isDown()) {
						player.up();
					}
					break;
				case KeyEvent.VK_SPACE:
					if (!player.isAttack()) {
						player.attack();
						if(!gateList.isEmpty()) {
							Gate gate=gateList.get(0);
							gate.BeingAttacked();
						}
					}
					break;
				}
			}

			// 키보드 해제 이벤트 핸들러
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					player.setLeft(false);
					player.leftStop();
					break;
				case KeyEvent.VK_RIGHT:
					player.setRight(false);
					player.rightStop();
					break;
				case KeyEvent.VK_UP:
					break;
				case KeyEvent.VK_SPACE:
					player.setAttack(false);
					break;
				}
			}
		});
	}
	public void gateGarbageCollector() {
		new Thread(()->{
			while (gate != null) {
				if (gate.getHp() <= 0)
					gate.setIcon(null);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


	private void initThread() {
		new Thread(()->{
			while(true) {
				try {
					gate=new Gate(player);
					gateList.add(gate);
					add(gate);
					gate.start();
					Thread.sleep(5000);
					gateList.remove(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		/*
		 * new Thread(()->{ gate.start(); }).start();
		 */
		/*
		 * new Thread(()->{ countDown.start(); }).start();
		 */
	}

	public static void main(String[] args) {
		new GateFrame();
	}
}
