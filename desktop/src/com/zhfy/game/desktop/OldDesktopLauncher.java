package com.zhfy.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zhfy.game.Armageddon;

public class OldDesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Armageddon(), config);
	}

	private OldDesktopLauncher() {
		throw new IllegalStateException("GameUtil class");
	}
}
