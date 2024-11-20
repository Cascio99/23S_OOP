package ver2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class BackGroundEnemyService implements Runnable {

	private BufferedImage image;
	private Enemy enemy;

	public BackGroundEnemyService(Enemy enemy) {
		this.enemy = enemy;
		try {
			image = ImageIO.read(new File("image/백그라운드 배경화면(level3).png")); // image를 읽어온다.
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
		// 적의 위치에 따라 색상 확인
		while (enemy.getEnemyState() == EnemyState.LIVE) {
			// 적의 왼쪽 위가 탐지 기준이므로 좌표를 (-10,+32), (+64, +32)
			// Color leftColor = new Color(image.getRGB(player.getX()-10,
			// player.getY()+25));
			// Color rightColor = new Color(image.getRGB(player.getX()+50+15,
			// player.getY()+25));
			Color leftColor = new Color(image.getRGB(enemy.getX() - 10, enemy.getY() + 55));
			Color rightColor = new Color(image.getRGB(enemy.getX() + 110 + 12, enemy.getY() + 55));

			// -1 -1 = -2일때 바닥이 흰색
			int bottomColor = image.getRGB(enemy.getX() + 10, enemy.getY() + 100) // 흰색일때 -1 y좌표 수정
					+ image.getRGB(enemy.getX() + 110 - 10, enemy.getY() + 100);// 흰색일때 -1 y좌표 수정

			// System.out.println("leftcolor : " + leftColor);
			// System.out.println("rightcolor : " + rightColor);

			// 바닥 충돌 확인
			if (bottomColor != -2) { // -2이 흰색 흰색이 아니면 모두 바닥
				// System.out.println("collision to the floor");
				// System.out.println("bottomColor: " + bottomColor);
				enemy.setDown(false);
			} else { // -2 일 때 실행됨 => 내 바닥색깔이 하얀색일 때
				if (!enemy.isUp() && !enemy.isDown()) { // 점프가 아닐 때만 실행해야 정상적인 점프가 가능
					// 한번 떨어질 때 지구 끝까지 떨어지는 버그 해결을 위해 down이 false일 때(down이 실행됬을때도 무한 호출됨)
					// System.out.println("down");
					enemy.down();
				}
			}

			if (leftColor.getRed() == 255 && leftColor.getGreen() == 0 && leftColor.getBlue() == 0) {
				// System.out.println("왼쪽 벽에 충돌함");
				enemy.setLeftWallCrash(true); // 충돌했으므로 true
				enemy.setLeft(false); // 충돌시 못가도록 false
				enemy.setIcon(null);
				LifeCount.life--;
				break;
			} else if (rightColor.getRed() == 255 && rightColor.getGreen() == 0 && rightColor.getBlue() == 0) {
				// System.out.println("오른쪽 벽에 충돌함");
				enemy.setRightWallCrash(true); // 충돌했으므로 true
				enemy.setRight(false); // 충돌시 못가도록 false
			} else { // 왼쪽 벽에도 충돌 안하고 오른쪽 벽에도 충돌 안했을 때
				enemy.setLeftWallCrash(false); // 이거 안해주면 한번 부딪힌 뒤에 움직이질 못함
				enemy.setRightWallCrash(false);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
