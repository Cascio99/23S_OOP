package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GateFrame extends JFrame {
	private static final int MAXGATES = 12; // 생성할 gate수
	private static final int GATECOOLTIME = 5000;

	private JLabel backgroundMap;
	private Player player;
	private Gate gate;
	private Enemy enemy;
	private CountDown countDown;
	private LifeCount life;
	private Head head;
	private InsertCoin coin;
	private StartBGM sBGM;

	private List<Gate> gateList;
	private List<Enemy> enemyList;

	private boolean enter = false;

	public GateFrame() {
		initSetting();
		initListener();
		pressEnterToStart();
		setVisible(true);
	}

	private void pressEnterToStart() {
		backgroundMap = new JLabel(new ImageIcon("image/PRESS_ENTER_TO_START.gif"));
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

	private void initMapObject() {
		backgroundMap = new JLabel(new ImageIcon("image/backgroundMap.png"));
		setContentPane(backgroundMap); // JPanel을 만들지 않고 바로 Frame에 그림 그리기
	}

	private void initObject() {
		player = new Player();
		countDown = new CountDown();

		gateList = new ArrayList<>();
		enemyList = new ArrayList<>();

		life = new LifeCount();
		head = new Head();
		coin = new InsertCoin();

		new BGM();

		add(player); // backgroundMap 위에 덧붙이는 개념으로 추가
		add(countDown);
		add(life);
		add(head);
		add(coin);

		countDown.start();
		life.start();
	}

	private void initSetting() {
		setSize(1024, 768);
		getContentPane().setLayout(null); // absolute 레이아웃(자유롭게 그림을 그릴 수 있다)

		setLocationRelativeTo(null); // Frame이 실행될때 가운데로 나오게 해준다.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창이 꺼지면 JVM도 같이 종료하기

		sBGM = new StartBGM();
	}

	private void initListener() {
		addKeyListener(new KeyAdapter() { // KeyPressed, KeyReleased만 필요하므로 어댑터 이용

			// 키보드 클릭 이벤트 핸들러
			@Override
			public void keyPressed(KeyEvent e) {
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
						if (!gateList.isEmpty()) { // 마지막에 생성된 gateList의 요소를 부셔야 함
							gate = gateList.get(gateList.size() - 1);
							gate.BeingAttacked();
						}
					}
					break;
				case KeyEvent.VK_ENTER:
					if (!enter)
						enter = true;

					if (enter)
						sBGM.clip.close();
				case KeyEvent.VK_I:
					LifeCount.life += 3;
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

	private void initThread() {
		new Thread(() -> {
			for (int i = 0; i < MAXGATES; i++) {
				try {// player랑 상호작용(좌표 겹지는지..)하기 위해 player 정보 넘겨야 함
					Gate gate = new Gate(player);
					// gateList에 gate들 추가
					gateList.add(gate);
					add(gate); // 화면에 추가
					gate.start(); // gate 쓰레드 시작
					if (gateList.size() > 1) {
						int gateIdx = (gateList.size() - 1) - 1; // `이전에 생성된 gate`에서 enemy 생성해야 됨
						if (gateList.get(gateIdx).getHp() > 0) {
							enemy = new Enemy(gateList.get(gateIdx));
							enemyList.add(enemy);
							add(enemy);
							enemy.start();
						}
					}
					// gate 생성 쿨타임: 5초
					Thread.sleep(GATECOOLTIME);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 마지막 gate 못 부순 경우에도 enemy 생성해줘야 함
			int gateLastIdx = (gateList.size() - 1) - 1;
			if (gateList.get(gateLastIdx).getHp() > 0) {
				enemy = new Enemy(gateList.get(MAXGATES - 1));
				enemyList.add(enemy);
				add(enemy);
				enemy.start();
			}
		}).start();
	}

	public static void main(String[] args) {
		new GateFrame();
	}
}
