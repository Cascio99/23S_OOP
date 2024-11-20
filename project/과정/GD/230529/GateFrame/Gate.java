package tryToAttack;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Gate extends JLabel implements Runnable{
	//위치
	private int x;
	private int y;
	
	//체력
	private int hp;
	
	//image 변수
	private ImageIcon gateImage;
	
	//필요한 객체들
	private boolean hit;
	private int max = 900;			// 랜덤 범위 지정할 때 필요한 변수들
	private int min = 100;
	
	public Gate() {		//생성자
		initObject();
		initSetting();
	}
	
	/*public void start() {
		initBackGroundGateService();
	}*/
	
	//getters and setters
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
		gateImage = new ImageIcon("image/gate1.png");
	}
	
	public void initSetting() {	
		// 초기 위치
		x = 600;
		y = 570;			// 570이 1층 높이
		
		// 체력
		hp = 5;
		hit = false;
		
		setIcon(gateImage);
		setSize(154,128);			// Icon 크기는 50,50
		setLocation(x, y);		// 처음 위치
	}
	
	public void BeingAttacked() {
		if(hit) {
			hp--;
		}
	}

	@Override
	public void run() {
		while(!CountDown.countOver) {	// countDown이 종료되면 랜덤 생성 종료
			try {
				Random rand = new Random();
				x = rand.nextInt(max - min) + min;		// 화면 밖으로 안 나가도록 랜덤값 생성
				setLocation(x, y);
				Thread.sleep(5000);
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
