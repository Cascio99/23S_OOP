package tryToAttack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Gate extends JLabel{
	//위치
	private int x;
	private int y;
	
	//체력
	private int hp;
	
	//image 변수
	private ImageIcon gateImage;
	
	//필요한 객체들
	private boolean hit;
	
	public Gate() {		//생성자
		initBackGroundGateService();
		initObject();
		initSetting();
	}
	
	public void start() {
		
	}
	
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
		//gateImage = new ImageIcon(/*이미지*/);
	}
	
	public void initSetting() {	
		// 초기 위치
		x = 200;
		y = 535;
		
		// 체력
		hp = 5;
		hit = false;
		
		//setIcon(/*이미지 추가*/);
		setSize(50,50);			// Icon 크기는 50,50
		setLocation(x, y);		// 처음 위치
	}
	
	private void initBackGroundGateService() {
		new Thread(new BackGroundGateService(this)).start();
	}
	
	public void BeingAttacked() {
		if(hit) {
			hp--;
		}
	}
}
