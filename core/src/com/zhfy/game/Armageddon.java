package com.zhfy.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Armageddon extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	AssetManager am;
	boolean ifDraw;

	@Override
	public void create () {
		batch = new SpriteBatch();

		//am=new AssetManager();
		//am=new AssetManager(new ExtraFileHandleResolver());
		//img = i.getTexture("badlogic.bin");
		am.load("ic_launcher-web.png",Texture.class);
		ifDraw=false;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if(ifDraw){
			batch.draw(img, 0, 0);
		}else if(am.update()) {
			// updata()返回true,证明所有资源加载完成
			// 可以执行对应的操作了
			img=am.get("ic_launcher-web.png",Texture.class);

			ifDraw=true;
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		am.dispose();
	}
}
