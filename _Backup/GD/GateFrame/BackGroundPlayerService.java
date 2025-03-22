package tryToAttack;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

// 메인스레드는 바쁜 상태(키보드 이벤트 처리하느라 바쁨)
// 그래서 얘는 새로 스레드를 만들어서 사용
public class BackGroundPlayerService implements Runnable {

	private BufferedImage image;
	private Player player;
	
	public BackGroundPlayerService(Player player) {
		this.player = player;
		try {
			image = ImageIO.read(new File("image/백그라운드 배경화면(level3).png"));	// image를 읽어온다.
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void run() {
		// 플레이어의 위치에 따라 색상 확인
		while (true) {
			//플레이어의 왼쪽 위가 (0, 0)
			Color leftColor = new Color(image.getRGB(player.getX()-20, player.getY()+55));
			Color rightColor = new Color(image.getRGB(player.getX()+110+25, player.getY()+55));
			Color topColor = new Color(image.getRGB(player.getX() + 55, player.getY()));
			
			//-1 -1 = -2일때 바닥이 흰색
			int bottomColor = image.getRGB(player.getX() + 10, player.getY()+100) //흰색일때 -1
					 +image.getRGB(player.getX() +110 - 10, player.getY()+100);//흰색일때 -1
			
			//System.out.println("leftcolor : " + leftColor);
			//System.out.println("rightcolor : " + rightColor);
			
			//바닥 충돌 확인
			if(bottomColor != -2) {		// -2이 흰색 흰색이 아니면 모두 바닥
				//System.out.println("collision to the floor");
				//System.out.println("bottomColor: " + bottomColor);
				player.setDown(false);
			}else {		// -2 일 때 실행됨 => 내 바닥색깔이 하얀색일 때
				if(!player.isUp() && !player.isDown()) {		// 점프가 아닐 때만 실행해야 정상적인 점프가 가능
				// 한번 떨어질 때 지구 끝까지 떨어지는 버그 해결을 위해 down이 false일 때(down이 실행됬을때도 무한 호출됨)
				//System.out.println("down");
				player.down();
				}
			}
			
			if(leftColor.getRed() == 255 && leftColor.getGreen() == 0 
					&& leftColor.getBlue() == 0) {
				//System.out.println("왼쪽 벽에 충돌함");
				player.setLeftWallCrash(true);	// 충돌했으므로 true
				player.setLeft(false);			// 충돌시 못가도록 false
			}else if(rightColor.getRed() == 255 && rightColor.getGreen() == 0
					&& rightColor.getBlue() == 0){
				//System.out.println("오른쪽 벽에 충돌함");
				player.setRightWallCrash(true); // 충돌했으므로 true
				player.setRight(false);			// 충돌시 못가도록 false
			}else if(topColor.getRed() == 255 && topColor.getGreen() == 0
					&& topColor.getBlue() == 0) {
				player.setTopWallCrash(true);
			}else {	// 왼쪽 벽에도 충돌 안하고 오른쪽 벽에도 충돌 안했을 때
				player.setLeftWallCrash(false);		// 이거 안해주면 한번 부딪힌 뒤에 움직이질 못함
				player.setRightWallCrash(false);
				player.setTopWallCrash(false);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
