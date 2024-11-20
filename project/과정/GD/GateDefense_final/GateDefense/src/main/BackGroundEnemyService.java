package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//Service: 색 구별에 의한 벽 충돌 감지 위한 클래스
public class BackGroundEnemyService implements Runnable {

	private BufferedImage image;
	private Enemy enemy;

	public BackGroundEnemyService(Enemy enemy) {
		this.enemy = enemy;
		try {
			image = ImageIO.read(new File("image/backgroundMapService.png")); // image를 읽어온다.
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
		// 적의 위치에 따라 색상 확인
		while (true) {
			// 적의 좌상단이 탐지 기준점
			Color leftColor = new Color(image.getRGB(enemy.getX() - 10, enemy.getY() + 55));

			// (-1) + (-1) = -2일때 바닥이 흰색
			int bottomColor = image.getRGB(enemy.getX() + 20, enemy.getY() + 100) // 흰색일때 -1 y좌표 수정
					+ image.getRGB(enemy.getX() + 130 - 20, enemy.getY() + 100);// 흰색일때 -1 y좌표 수정

			// 1. 바닥 충돌 확인
			if (bottomColor != -2) { // -2이 흰색 흰색이 아니면 모두 바닥
				enemy.setDown(false);
			} else { // -2 일 때 실행됨 => 내 바닥색깔이 하얀색일 때
				if (!enemy.isUp() && !enemy.isDown()) { // 점프가 아닐 때만 실행해야 정상적인 점프가 가능
					// 한번 떨어질 때 지구 끝까지 떨어지는 버그 해결을 위해 down이 false일 때(down이 실행됬을때도 무한 호출됨)
					enemy.down();
				}
			}
			// 2. 왼쪽 벽 충돌 확인
			if (leftColor.getRed() == 255 && leftColor.getGreen() == 0 && leftColor.getBlue() == 0) {
				System.out.println("[Enemy] 왼쪽 벽에 충돌함");
				enemy.setLeftWallCrash(true); // 충돌했으므로 true
				enemy.setLeft(false); // 충돌시 못가도록 false
				enemy.setIcon(null);

				// enemy가 왼쪽 벽 뚫고 나가면 마을 주민들 위험해지므로 player 목숨 1개 감소시키기
				LifeCount.life--;
				// 메모리 효율 위해 enemy 쓰레드 종료
				break;
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
