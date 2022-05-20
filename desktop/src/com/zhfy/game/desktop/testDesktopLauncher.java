package com.zhfy.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zhfy.game.Armageddon;
import com.zhfy.game.test.GrayscaleTest;
import com.zhfy.game.test.ShaderLesson3;

public class testDesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Gray Test";
		cfg.useGL30 = true;
		cfg.width = 640;
		cfg.height = 480;
		cfg.resizable = false;

		//new LwjglApplication(new ShaderLesson3(), cfg);
		new LwjglApplication(new GrayscaleTest(), cfg);
	}

	private testDesktopLauncher() {
		throw new IllegalStateException("GameUtil class");
	}
}
