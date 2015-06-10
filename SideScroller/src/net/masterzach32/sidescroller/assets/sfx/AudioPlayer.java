package net.masterzach32.sidescroller.assets.sfx;

import javax.sound.sampled.*;

import net.masterzach32.sidescroller.main.SideScroller;

public class AudioPlayer {
	
	private Clip clip;
	
	public AudioPlayer(AudioInputStream ais) {
		try {
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if(clip == null) return;
		if(!SideScroller.isSoundEnabled) {
			return;
		} else {
			stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	public void stop() {
		if(clip.isRunning()) clip.stop();
	}
	
	public void close() {
		stop();
		clip.close();
	}
}