package ver3;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class StartBGM {
	public Clip clip;
	
	StartBGM() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("sound/simple-piano-melody-9834.wav"));
			clip = AudioSystem.getClip();
			clip.open(ais);
			// 볼륨 조절
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
