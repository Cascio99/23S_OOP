package ver2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class GateFrame extends JFrame {
	private final static int MAXGATES = 12; // 생성할 gate수

	private JLabel backgroundMap;
	private Player player;
	private Gate gate;
	private Enemy enemy;
	private CountDown countDown;
	private LifeCount life;
	private Head head;

	private List<Gate> gateList;
	private List<Enemy> enemyList;

	private boolean enter = false;

	public GateFrame() {
		initSetting();
		initListener();
		pressEnterToStart();
		// initMapObject();
		// initObject();
		// initThread();
//		initThread_enemy();

//		gateGarbageCollector();	//필요 없을 듯
		setVisible(true);
	}

	private void initMapObject() {
		backgroundMap = new JLabel(new ImageIcon("image/배경화면(level 3).png"));
		setContentPane(backgroundMap); // JPanel을 만들지 않고 바로 Frame에 그림 그리기
	}

	private void pressEnterToStart() {
		backgroundMap = new JLabel(new ImageIcon("image/press enter to start.gif"));
		setContentPane(backgroundMap);

		// 엔터를 누를 때 객체들을 생성하고 초기화
	    new Thread(() -> {
	        while (!enter) {
	            try {
	                Thread.sleep(100); // 작은 대기 시간을 주어 CPU 점유율을 낮춥니다.
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        // enter 값이 true가 되면 객체들을 생성하고 초기화합니다.
	        initMapObject();
	        initObject();
	        initThread();
	    }).start();
	}

	private void initObject() {
		player = new Player();
//		enemy = new Enemy();
//		gate = new Gate(player);	//player랑 상호작용(좌표 겹지는지..)하기 위해 player 정보 넘겨야 함
		countDown = new CountDown();

		gateList = new ArrayList<>();
		enemyList = new ArrayList<>();

		life = new LifeCount();
		head = new Head();

		add(player); // panel에 덧붙이는 개념으로 추가
//		add(enemy);
//		add(gate);
		add(countDown);
		add(life);
		add(head);

//		enemy.start();
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
						if (!gateList.isEmpty()) { // 마지막에 생성된 gateList요소를 부셔야 함
							Gate gate = gateList.get(gateList.size() - 1);
//							Gate gate = gateList.get(0);
							gate.BeingAttacked();
						}
					}
					break;
				case KeyEvent.VK_ENTER:
					if (!enter)
						enter = true;
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

	/*
	 * public void gateGarbageCollector() { new Thread(() -> { while (gate != null)
	 * { if (gate.getHp() <= 0) gate.setIcon(null); try { Thread.sleep(1000); }
	 * catch (InterruptedException e) { e.printStackTrace(); } } }).start(); }
	 */

	private void initThread() {
		new Thread(() -> {
			for (int i = 0; i < MAXGATES; i++) {
				try {
					Gate gate = new Gate(player); // 에러 발견: 0603 녹화영상 참고(전에 생성된 gate들의 위치가 15(Gate에서 쓰레드가 15초마다 sleep해서
													// 그런듯)초가 지나면 바뀜)
					gateList.add(gate);
					add(gate);
					gate.start();
					if (gateList.size() > 1) {
						int gateIdx = (gateList.size() - 1) - 1; // 이전에 생성된 gate에서 enemy 생성해야 됨
						if (gateList.get(gateIdx).getHp() > 0) {
							enemy = new Enemy(gateList.get(gateIdx));
//							enemy = new Enemy(gateList.get(0));
							enemyList.add(enemy);
							add(enemy);
							enemy.start();
						}

					}
					Thread.sleep(5000);
//					gateList.remove(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 마지막 gate에서도 생성해줘야 함
			enemy = new Enemy(gateList.get(MAXGATES - 1));
			enemyList.add(enemy);
			add(enemy);
			enemy.start();
		}).start();
		/*
		 * new Thread(()->{ gate.start(); }).start();
		 */
		/*
		 * new Thread(()->{ countDown.start(); }).start();
		 */

		// gateList 관리(list.remove()작업) --> 필요없을 듯? 오히려 성능 나빠질??
//		Timer timer = new Timer(1, new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				for (Gate gate : gateList) {
//					if (gate.getHp() <= 0) {
//						// 게이트 부서지면 gateList에서 remove
//						gateList.remove(gate);
//						break;
//					}
//				}
//			}
//		});
//		timer.start();
		// enemyList 관리
		// 0607_원래 주석 아니었던 거 주석처리함. --> Timer 쓰지 말고 하나로 통일해서 돌아가게....
//		Timer enemyTimer = new Timer(5000, new ActionListener() { // 5000ms로 해버리면 새로 생성된 gate를 관찰하므로(논리에러) 1ms 미리 부셔야 함.
//			public void actionPerformed(ActionEvent e) {
//				if (!gateList.isEmpty()) {
//					int gateIdx = gateList.size() - 1;
//					if (gateList.get(gateIdx).getHp() > 0) {
////					if (gateList.get(0).getHp() > 0) {
//						// gate가 부서지지 않았을 경우 enemy 생성
////						enemy=new Enemy(gate);		//SEGFAULT: this.gate is null..... 왜???
//						enemy = new Enemy(gateList.get(gateIdx));
////						enemy = new Enemy(gateList.get(0));
//						enemyList.add(enemy);
//						add(enemy);
//						enemy.start();
//					}
//				}
//			}
//		});
//		enemyTimer.start();
	}

	public static void main(String[] args) {
		new GateFrame();
	}
}
