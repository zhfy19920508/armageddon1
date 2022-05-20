package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.zhfy.game.config.ResDefaultConfig;

public class ImageDecrypt {
	private static String Key = ResDefaultConfig.ImageKey;

	// pixmap 矢量图
	private static Pixmap pixmap = null;

	// texture 解密后的图片
	private static Texture texture = null;

	// 读取文件的输入流
	private static FileHandle inputStream = null;
	//写入文件、资源字节数组
	private static byte[] allBytes = null;

	// 获取加密后的texture，注意路径 与转义字符。

	private ImageDecrypt() {
		throw new IllegalStateException("GameUtil class");
	}



	public static Texture getTexture(String path) {

		try {

			inputStream = Gdx.files.internal(path);

			allBytes = inputStream.readBytes();
			int byteCount = 0;
			for (int i = 0; i < allBytes.length; i++) {

				// 每个字节异或密码，请保证解密时密码前后相同
				byteCount++;
				if (byteCount <= 20) {
					// 加密20个字节,停止解密密
					allBytes[i] ^= Key.hashCode();
				} else {
					allBytes[i] = allBytes[i];
				}
			}

		} catch (Exception e) {
			if(ResDefaultConfig.ifDebug){
				e.printStackTrace();
			}
		} finally {

		}

		pixmap = new Pixmap(allBytes, 0, allBytes.length);
		texture = new Texture(pixmap);

		return texture;
	}


}
