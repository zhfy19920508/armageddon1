package com.zhfy.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.tool.PropertiesUtil;

public class DesktopLauncher {

	private static PropertiesUtil playerConfig;

	private DesktopLauncher() {
		throw new IllegalStateException("DesktopLauncher class");
	}

	/** 窗口宽度参考值 */
	public static final float PIX_WIDTH = 1280;//1280

	/** 窗口宽高比, 适当调节宽高比可以查看在不同屏幕上的效果, 例如设置为16:12 9:16, 3:4, 2:3 */
	public static final float RATIO =16F /9.0F; //   默认开发比例是16:9

	/** 适当改变缩放比以适应自己的电脑屏幕 */
	public static  float SCALE = 1.0F;


	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.addIcon("pc/icon_32x32.png", Files.FileType.Internal);
		/*
		 * 窗口（实际屏幕）宽高比设置要与视口世界的宽高比相同, 最终显示到屏幕上的内容才不会被压扁或拉长
		 */

		//因为是回合制游戏,所以可以适当降低fps,节约性能
		//config.backgroundFPS=30;
		//config.foregroundFPS=30;
		//MainGame test
			MainGame game=new MainGame();
		playerConfig =new  PropertiesUtil(ResDefaultConfig.Path.GameOptionPropertiesPath,false);
		config.width = (int) (PIX_WIDTH * SCALE);               // 窗口宽度
		config.height = (int) ((PIX_WIDTH / RATIO) * SCALE);    // 窗口高度
		if(playerConfig!=null){
			if(playerConfig.getBoolean("customScreen", false)){//如果是自定义分辨率
				config.width=playerConfig.getInteger("customScreenW", (int) (PIX_WIDTH * SCALE));
				config.height=playerConfig.getInteger("customScreenH", (int) ((PIX_WIDTH / RATIO) * SCALE));
			}else{
				int index= playerConfig.getInteger("screenSizeIndex",ResDefaultConfig.Game.defaultScreenIndex);
				if(index<0||index>=ResDefaultConfig.Game.screenWidth.length){
					index=0;
				}
				config.width=ResDefaultConfig.Game.screenWidth[index];
				config.height=ResDefaultConfig.Game.screenHeight[index];
			}
			config.fullscreen=playerConfig.getBoolean("isFullscreen", false);
		}
		// 默认帧率 60
		config.foregroundFPS=60;
		// 切出时帧率为30
		config.backgroundFPS=30;
		config.samples=4;
		config.resizable=false;
		config.title = ResDefaultConfig.game;
		/*if(game.isFullscreen()){
			config.fullscreen=true;
		}else {
			config.resizable = false;   // 窗口设置为大小不可改变
			config.width =game.getScreenWidth();
			config.height =game.getScreenHeight();
		}*/
		LwjglApplication l=	new LwjglApplication(game, config);




	}
}
