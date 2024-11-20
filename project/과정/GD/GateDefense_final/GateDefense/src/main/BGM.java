package main;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
//BGM 재생 클래스(게임 플레이 중 나오는 BGM)
public class BGM {
	BGM() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("sound/the-epic-inspiring-153397.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			// 볼륨 조절
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-10.0f);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
