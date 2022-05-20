package com.zhfy.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.GameMethod;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.framework.tool.CHAsyncTask;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.framework.PixmapListDAO;
import com.zhfy.game.screen.actor.base.BaseActor;
import com.zhfy.game.screen.actor.framework.ImageActor;

import java.io.IOException;

/**
 * 开始场景（欢迎界面）, 实现 Screen 接口 或者 继承 ScreenAdapter 类, ScreenAdapter 类空实现了 Screen 接口的所有方法。<br/>
 * 这个场景使用 LibGDX 的官方 logo 居中显示 3 秒钟当做是游戏的欢迎界面。 <br/><br/>
 *
 * PS: 类似 Screen 这样的有许多方法的接口, 更多时候只需要实现其中一两个方法, 往往会有一个对应的便捷的空实现所有接口方法的 XXAdapter 类,
 *     例如 ApplicationListener >> ApplicationAdapter, InputProcessor >> InputAdapter
 */
public class StartScreen implements Screen {

    // 为了方便与 com.zhfy.game.MainGame 进行交互, 创建 Screen 时将 com.zhfy.game.MainGame 作为参数传进来
    private MainGame game;

    private Texture logoTexture;
    private ImageActor loading_bg;
    private ImageActor loading;

    private Stage stage;

    private BaseActor logoActor;
    private float loadingW;

    public int screenId= ResDefaultConfig.Class.StartScreen;

    // 渲染时间步累计变量（当前场景被展示的时间总和）
    private float deltaSum;
    private Pixmap tempPixmap;
    private PixmapListDAO pixmapLists;

    public StartScreen(MainGame mainGame) {
        this.game = mainGame;

        // 在 Screen 中没有 create() 方法, show() 方法有可能被调用多次, 所有一般在构造方法中做一些初始化操作较好

        // 创建 logo 的纹理, 图片 logo.png 的宽高为 300 * 50
        //logoTexture = new Texture(Gdx.files.internal("image/bg_start.png"));
        logoTexture = mainGame.getAssetManager().get("pixmap/bg_start.png",Texture.class);
        loading=new ImageActor(mainGame.getImgLists().getTextureByName("icon_block").getTextureRegion());
        loading_bg=new ImageActor(mainGame.getImgLists().getTextureByName("icon_7block_bg").getTextureRegion());
        // 使用伸展视口创建舞台
        stage = new Stage(new StretchViewport(mainGame.getWorldWidth(),mainGame.getWorldHeight()));

        // 创建 logo 演员
        logoActor = new BaseActor(logoTexture);

        // 将演员设置到舞台中心
        logoActor.setPosition(
                stage.getWidth() / 2 - logoActor.getWidth() / 2,
                stage.getHeight() / 2 - logoActor.getHeight() / 2
        );
        loading_bg.setPosition(
                stage.getWidth() / 2 - loading_bg.getWidth() / 2,
                stage.getHeight() / 8 - loading_bg.getHeight() / 2
        );
        loading.setPosition(
                stage.getWidth() / 2 - loading.getWidth() / 2,
                stage.getHeight() / 8 - loading.getHeight() / 2
        );


        if(game.resGameConfig.defaultMapBinId!=0){
            if(ResDefaultConfig.ifDebug){
                Fb2Map mapbin =game.getGameFramework().getMapDaoByMapId(game.resGameConfig.defaultMapBinId);
                if(mapbin!=null&&game.defaultMapBinDAO!=null&&!game.defaultMapBinDAO.equals(mapbin)){
                    game.defaultMapBinDAO.dispose();
                }
                game.defaultMapBinDAO=mapbin;
            }else{
                CHAsyncTask task=new CHAsyncTask(mainGame.asyncManager) {
                    @Override
                    public void onPreExecute() {
                    }
                    @Override
                    public void onPostExecute(String result) {
                        Gdx.app.log("CHAsyncTask","load defaultMap:"+game.defaultMapBinDAO);
                    }
                    @Override
                    public String doInBackground() {
                        try {
                            Fb2Map mapbin =game.getGameFramework().getMapDaoByMapId(game.resGameConfig.defaultMapBinId);
                            if(mapbin!=null&&game.defaultMapBinDAO!=null&&!game.defaultMapBinDAO.equals(mapbin)){
                                game.defaultMapBinDAO.dispose();
                            }
                            game.defaultMapBinDAO=mapbin;
                        } catch (GdxRuntimeException e) {
                            if(ResDefaultConfig.ifDebug){
                                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                            }else if(!game.gameConfig.getIfIgnoreBug()){
                                game.remindBugFeedBack();
                            }
                            game.recordLog("ResGameConfig ",e);
                        }
                        return null;
                    }
                };
                mainGame.asyncManager.loadTask(task);
            }
        }

        game.progressbarWidth=loading.getRegionWidth();
        // 添加演员到舞台
        stage.addActor(logoActor);

        stage.addActor(loading_bg);

        stage.addActor(loading);


        /*if(game.gameConfig.isExtAvailable){
            GameUtil.copyLogToExteral();
        }*/



        //废弃 初始创建割图任务
         /*
        Array<XmlReader.Element> mapEs= mainGame.gameConfig.getDEF_MAP().e.getChildrenByName("map");
        String definitionType;float scale=game.getMapScale();
        definitionType="hd";
        tempPixmap=new Pixmap(ResConfig.Map.PT_SIDE, ResConfig.Map.PT_SIDE, Pixmap.Format.RGBA8888);
         pixmapLists=game.getGameFramework().getPixmapListByFileName("pm_tiles",game.getAssetManager());

        for(XmlReader.Element mapE:mapEs){
            CHAsyncTask task=new CHAsyncTask(mainGame.asyncManager) {
                @Override
                public void onPreExecute() {

                }
                @Override
                public void onPostExecute(String result) {

                }
                @Override
                public String doInBackground() {
                    try {
                        game.getGameFramework().cupMap(tempPixmap,pixmapLists,mapE,definitionType,scale,true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "ok";
                }
            };
            mainGame.asyncManager.loadTask(task);


            break;
        }*/



    }

    @Override
    public void show() {
        deltaSum = 0;
    }

    @Override
    public void render(float delta) {
        game.getAssetManager().update();
        loading.setWidth (loadingW);
        //  Gdx.app.log("loading",game.getAssetManager().getProgress()*loadingW+":"+loadingW);
        // 累计渲染时间步
        /**/ deltaSum += delta;

        // 开始场景展示时间超过 1 秒, 通知 com.zhfy.game.MainGame 切换场景（启动主游戏界面）
        if (game.getAssetManager().isFinished()) {
            // game.setMapBinDao(new MapBinDAO(BTLTooL.LoadBtlNoThrow(ResConfig.Rules.RULE_FB1_MAP,Gdx.files.internal("bin/world_map.bin").readBytes())));
            // game.initGameData();
            if(game.asyncManager.update()){
                game.showGameScreen(screenId,1);
                return;
            }else {
                loadingW=game.asyncManager.getProgress()*game.progressbarWidth;
            }
        }else {
            //Gdx.app.log("loading", game.getAssetManager().getProgress()+"");
            loadingW=game.getAssetManager().getProgress()*game.progressbarWidth;
        }

        // 使用黑色清屏
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 更新舞台逻辑
        stage.act();
        // 绘制舞台
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        // 场景被销毁时释放资源
        if (stage != null) {
            stage.dispose();
            stage=null;
        }
        if (logoTexture != null) {
            logoTexture.dispose();
            logoTexture=null;
        }
        if (tempPixmap != null) {
            tempPixmap.dispose();
            tempPixmap=null;
        }
        if (pixmapLists != null) {
            pixmapLists.dispose();
            pixmapLists=null;
        }

    }

}