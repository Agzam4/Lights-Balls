package game;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	transient private Clip clip;
	transient private boolean isPlaying = false;

	public Sound(String path) {
		try {
			AudioInputStream ais = null;
			try {
				ais = AudioSystem.getAudioInputStream(
						Sound.class.getResource(path));
			} catch (Exception e) {
				e.printStackTrace();
				try {
					ais = AudioSystem.getAudioInputStream(Sound.class.getResourceAsStream(path));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false
					);
			AudioInputStream dais =
					AudioSystem.getAudioInputStream(
							decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public void play(int loops) {
		play(loops, 0);
	}

	long lastPlay = 0;

	long playRefresh = 100;

	public void play(int loops, int framePosition) {
		long now = System.nanoTime();
		if(now-lastPlay > playRefresh) {
			lastPlay = now;
			Thread player = new Thread(() -> {
				try {
					if(clip == null) return;
					stop();
					clip.setFramePosition(framePosition);
					clip.loop(loops);
					clip.start();
					isPlaying = true;
				} catch (Exception e) {
				}
			});
			player.start();
		}
	}

	public void justplay() {
//		Thread player = new Thread(() -> {
			try {
				if(clip == null) return;
//				stop();
				clip.setFramePosition(0);
				clip.loop(0);
				clip.start();
				isPlaying = true;
			} catch (Exception e) {
			}
//		});
//		player.start();
	}

	public void stop() {
		isPlaying = false;
		try {
			if(clip.isRunning()) clip.stop();
		} catch (Exception e) {
		}
	}

	public void close() {
		isPlaying = false;
		try {
			stop();
			clip.close();
		} catch (Exception e) {
		}
	}

	public void setVolume(float volume) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
		gainControl.setValue(20f * (float) Math.log10(volume));
	}

	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void setPlayRefresh(long playRefresh) {
		this.playRefresh = playRefresh;
	}

}
