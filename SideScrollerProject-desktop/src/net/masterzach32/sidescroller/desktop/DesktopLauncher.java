package net.masterzach32.sidescroller.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.masterzach32.sidescroller.SideScrollerProject;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SideScroller Project Build 0.2.7";
		config.samples = 8;
		config.useGL30 = true;
	    config.width = 1280;
	    config.height = 720;
	    config.vSyncEnabled = false;
		new LwjglApplication(new SideScrollerProject(), config);
	}
}