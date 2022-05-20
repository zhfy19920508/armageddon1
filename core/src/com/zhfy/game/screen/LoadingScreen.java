package com.zhfy.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.screen.actor.framework.ImageActor;

public class LoadingScreen implements Screen {
 
	MainGame game;
	
	Stage stage;
	
	AssetManager manager;
    
    boolean isPlay = false;
    private int nextScreenId;
	//private BaseActor logoActor;
	private ImageActor loading_bg;
	private ImageActor loading;
	private ImageActor viewImgBg;
	private ImageActor viewImg;
	private Label label;
	private float timeSum;
	private float progress_s;//假的进度
	private float progress_m;//原进度
	
	public LoadingScreen(MainGame game,AssetManager am,int beforeScreenId,int nextScreenId) {
		//game.getLogTime().reStar();
		this.game = game;
		this.manager=am;
		this.nextScreenId =nextScreenId;
		timeSum=0;
      //资源管理器进行卸载旧资源,加新资源
        manager=GameUtil.loadResByScreen(game,manager, beforeScreenId, nextScreenId);
       // texture = GameUtil.getBgTextureByScreenId(game,2,manager);
		loading=new ImageActor(game.getImgLists().getTextureByName("icon_block").getTextureRegion());
		loading_bg=new ImageActor(game.getImgLists().getTextureByName("icon_7block_bg").getTextureRegion());

		stage = new Stage(new StretchViewport(game.getWorldWidth(),game.getWorldHeight()));
		loading.setPosition(
				stage.getWidth() / 2 - loading.getWidth() / 2,
				stage.getHeight() / 8 - loading.getHeight() / 2
		);
		loading_bg.setPosition(
				stage.getWidth() / 2 - loading_bg.getWidth() / 2,
				stage.getHeight() / 8 - loading_bg.getHeight() / 2
		);

		if(nextScreenId== ResDefaultConfig.Class.SMapScreen&&game.gameConfig.ifEffect){
			String imgName=game.gameConfig.getDEF_STAGE().getElementById(game.getStageId()).get("name");
			TextureRegion r=game.getPreviewTextureRegion(imgName);
			if(r!=null){
				viewImg=new ImageActor(r);
				float h=stage.getHeight()*0.6f;
				float w=viewImg.getWidth()*h/viewImg.getHeight();
				viewImg.setSize(w,h);

				viewImg.setPosition(
						stage.getWidth() / 2 - viewImg.getWidth() / 2,
						stage.getHeight() / 2 - viewImg.getHeight() / 2
				);

				viewImg.setName(imgName);

				viewImgBg=new ImageActor(game.getImgLists().getTextureByName("colorBlock_3").getTextureRegion());
				viewImgBg.setWidth(viewImg.getWidth()+ ResDefaultConfig.Image.BORDER_IMAGE_REFW);
				viewImgBg.setHeight(viewImg.getHeight()+ ResDefaultConfig.Image.BORDER_IMAGE_REFH);
				viewImgBg.setX(viewImg.getX()- ResDefaultConfig.Image.BORDER_IMAGE_REFW/2);
				viewImgBg.setY(viewImg.getY()- ResDefaultConfig.Image.BORDER_IMAGE_REFH/2);
				stage.addActor(viewImgBg);
				stage.addActor(viewImg);

				loading.setPosition(
						stage.getWidth() / 2 - loading.getWidth() / 2,
						viewImg.getY() - loading.getHeight() / 2+25
				);
				loading_bg.setPosition(
						stage.getWidth() / 2 - loading_bg.getWidth() / 2,
						viewImg.getY() - loading_bg.getHeight() / 2+25
				);
			}
		}

			label = new Label("", new Label.LabelStyle(game.gameConfig.gameFont, Color.WHITE));
			label.setAlignment(Align.left);
			label.setWrap(true);
			label.setWidth(stage.getWidth()/1.5f);
			if(nextScreenId== ResDefaultConfig.Class.SMapScreen){
				label.setText(game.getStageIntroduce());
			}else{
				label.setText(game.gameMethod.getDialogueStr(13,ComUtil.getRandom(1,game.gameConfig.getDEF_RDIALOGUE().getElementById(13).getInt("count") ) ,""));
			}

		if(viewImg==null) {
			label.setFontScale( game.gameConfig.gameFontScale);
			label.setPosition(
					stage.getWidth() / 2 - label.getWidth() / 2,
					stage.getHeight() / 2 - label.getHeight() / 2
			);
		}else{
			label.setFontScale( game.gameConfig.gameFontScale*0.7f);
			label.setPosition(
					stage.getWidth() / 2 - label.getWidth() / 2,
					stage.getHeight()/10
			);
		}
			stage.addActor(label);

		//loadingW=loading.getRegionWidth();
		// 添加演员到舞台
		//stage.addActor(logoActor);
		stage.addActor(loading_bg);

		stage.addActor(loading);
	}


	private void updLabelInfo(){
		if(nextScreenId== ResDefaultConfig.Class.SMapScreen&&game.getStageId()<1000){//征服时更新征服技巧,否则更新名人名言
			label.setText(game.getStageIntroduce());
			label.setText(game.gameMethod.getDialogueStr(16,ComUtil.getRandom(1,game.gameConfig.getDEF_RDIALOGUE().getElementById(16).getInt("count") ) ,null));
		}else{
			label.setText(game.gameMethod.getDialogueStr(13,ComUtil.getRandom(1,game.gameConfig.getDEF_RDIALOGUE().getElementById(13).getInt("count") ) ,null));
		}
	}
	
	@Override
	public void dispose() {
	    stage.dispose();
		stage=null;
		if(viewImg!=null){
			game.removeTempTextureRegion(viewImg.getName());
			viewImg=null;
		}
	}
 
	@Override
	public void hide() {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void pause() {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void render(float arg0) {

        Gdx.gl.glClearColor( 0,0,0,1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		game.batch.begin();
        stage.act();
        stage.draw();
        timeSum+=arg0;
        if( manager.isFinished() &&timeSum>1.5f){
			if(game.asyncManager.update()){
				game.toScreen(nextScreenId);
				game.batch.end();
				return;
			}else {
				if(progress_m!=game.asyncManager.getProgress()){
					progress_m=game.asyncManager.getProgress();
					progress_s=progress_m;
				}else{
					if(progress_s>=1){
						progress_s=0;updLabelInfo();
					}else if(progress_s<0.4f){
						progress_s=progress_s+0.001f;
						if(progress_s>0.4f){
							updLabelInfo();
						}
					}else if(progress_s<0.7f){
						progress_s=progress_s+0.0005f;
						if(progress_s>0.7f){
							updLabelInfo();
						}
					}else{
						progress_s=progress_s+0.0003f;
					}
				}
				loading.setWidth (progress_s*game.progressbarWidth);
			}
        }else {
        	if(progress_m!=manager.getProgress()){
				progress_m=manager.getProgress();
				progress_s=progress_m;
			}else{
				if(progress_s>=1){
					progress_s=0;
					updLabelInfo();
				}else if(progress_s<0.4f){
					progress_s=progress_s+0.001f;
					if(progress_s>0.4f){
						updLabelInfo();
					}
				}else if(progress_s<0.7f){
					progress_s=progress_s+0.0005f;
					if(progress_s>0.7f){
						updLabelInfo();
					}
				}else{
					progress_s=progress_s+0.0003f;
				}
			}
			manager.update();
			loading.setWidth (progress_s*game.progressbarWidth);
		}
	    game.batch.end();
	    
		/*Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(texture,0,0);
		batch.end();
		stage.act();
		stage.draw();*/
		
	}
 
	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void resume() {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void show() {
		/*stage = new Stage();
		texture = GameUtil.getBgTextureByScreenId(2,manager);
		batch = new SpriteBatch();
		
		stage.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				game.setScreen(game.getStartScreen());
				return true;
			}
		});
		
		Gdx.input.setInputProcessor(stage);*/
		
	}
 
}