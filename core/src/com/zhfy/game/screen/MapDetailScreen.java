package com.zhfy.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.screen.actor.framework.MapEditBgActor;
import com.zhfy.game.screen.actor.framework.WindowGroup;

/**
 * 主游戏场景（游戏主界面）, 实现 Screen 接口 或者 继承 ScreenAdapter 类 <br/>
 * 这里就展示一张图片代表游戏主界面
 */
public class MapDetailScreen extends ScreenAdapter  {

    private  ShapeRenderer shapeRenderer;
    private MainGame game;
    private Vector2 tempVector2;
    private Stage stage;

    private MapEditBgActor bgActor;

    private Array<WindowGroup> windowGroups;
    private ImageButton button;
    //使用场景
    private int screenId= ResDefaultConfig.Class.MapDetailScreen;

    //private GameFramework framework;

    private int mapId;
    private int functionType;

    //private Sprite mapSprite;

    //private DefMap defMap;

    InputProcessorEvent processorEvent;
    MapListener mapListener;
    InputMultiplexer multiplexer;

    boolean isTouching;
    float touchBaseX;
    float touchBaseY;
    float touch_X;
    float touch_Y;
     float moveX;
     float moveY;
     Label label;
     //中心点位置
     float cx;
     float cy;
     float javaHeap;
     float javaMaxHeap;


  //游戏舞台的基本宽高
   static  float GAMESTAGE_WIDTH;
   static  float GAMESTAGE_HEIGHT;
   //GLProfile  gp = new GrayFilter();
   GLProfiler  gp;

   //游戏参数
   boolean ifGrid;


   //ui
   private Element uiR;
   private XmlReader reader ;
   private Pixmap bgTexture;
   private float tempX,tempY,tempW,tempH;
   //com.badlogic.gdx.utils.Array<Element> buttonEs;
   //private Map tempMap;

   private int i;//function的计数标志,从1开始

    private Input.TextInputListener listener;
    private String instruct;

    public MapDetailScreen(MainGame mainGame)  {
        this.game=mainGame;

         GAMESTAGE_WIDTH=game.getWorldWidth();
         GAMESTAGE_HEIGHT=game.getWorldHeight();
        tempVector2=new Vector2();
         //ui
         {
             reader = game.gameConfig.reader;
             uiR=game.gameConfig.getCONFIG_LAYOUT().getElementById(screenId);//GameLayout.getXmlERootByScreenId(screenId);
             //defaultUiE=GameUtil.getXmlEByName(uiR, ResConfig.StringName.defaultWindowName);
         }


        //初始化游戏参数
         {
             ifGrid=false;
             functionType=0;
         }
        mapId=mainGame.getMapId();




        //1根据mapid读取地图DAO
        //2加载地图图片,生成图片作为背景
        /*textureMap=framework.getMapByMapId(mapId);
        defMap=framework.getDefMapByMapId(mapId);
        mapW=defMap.getWidth();
        mapH=defMap.getHeight();
        mapSprite = new Sprite(textureMap);*/

        //3增加拖动等功能,可以拖动地图
        //4替换地图dao的地图素材
        //5保存地图


        gp =new GLProfiler(Gdx. graphics);
        gp.enable();

        isTouching=false;
        multiplexer = new InputMultiplexer();

        // 使用伸展视口创建舞台
        stage = new Stage(new StretchViewport(GAMESTAGE_WIDTH, GAMESTAGE_HEIGHT));
        // 将输入处理设置到舞台（必须设置, 否则点击按钮没效果）

        multiplexer.addProcessor(stage);
        // 创建游戏人物演员
        //bgActor = new MapEditBgActor(new TextureRegion(mapSprite),0,0,mapW,mapH,GAMESTAGE_WIDTH, GAMESTAGE_HEIGHT);

        //this.shapeRenderer = new ShapeRenderer();
        bgActor = new MapEditBgActor(shapeRenderer,mapId,GAMESTAGE_WIDTH, GAMESTAGE_HEIGHT,mainGame);



        // 添加演员到舞台
        stage.addActor(bgActor);

        listener= new Input.TextInputListener() {
            @Override
            public void input(String text) {
                instruct=text;
                bgActor.parseCommand(text);
            }
            @Override
            public void canceled() {

            }
        };

         i=1;
            // tempX=ui.get(u).getInt("x");tempY=ui.get(u).getInt("y");tempW=ui.get(u).getInt("w");tempH=ui.get(u).getInt("h");

             // 添加演员到舞台

             //遍历window的buttons按钮
        if (uiR.getChildCount() > 0) {
            windowGroups = new Array<>();
            for (int i = 0; i < uiR.getChildCount(); i++) {
                WindowGroup w = WindowGroup.obtain(WindowGroup.class);
                w.init(game, uiR.getChild(i), stage.getWidth(), stage.getHeight(),tempVector2);
                //WindowGroup w=new WindowGroup(game,uiR.getChild(i),stage.getWidth(),stage.getHeight());//MainGame mainGame, XmlReader.Element groupE,int stageW,int stageH
                stage.addActor(w);
                windowGroups.add(w);
            }
            windowFunction(game.functionMap);
        }

        Gdx.input.setInputProcessor(stage);

        //文字示例
         label=new Label("FPS:"+Gdx.graphics.getFramesPerSecond(),new LabelStyle(game.defaultFont, null));
        //Label label=new Label("FPS:"+(int)(1+Math.random()*(10-1+1)),new LabelStyle(new BitmapFont(), null));

        label.setWidth(200);//设置每行的宽度
        label.setWrap(true);//开启换行
        label.setPosition(20, 140);
        stage.addActor(label);

        processorEvent = new InputProcessorEvent();
        mapListener=new MapListener();

       // Gdx.app.log("平台", Gdx.app.getGoodsType()+"");
        switch (Gdx.app.getType()) {
            case Desktop:// Code for Desktop applicationbreak;
                multiplexer.addProcessor(processorEvent);break;
            case Android:// Code for Android applicationbreak;
                multiplexer.addProcessor(new GestureDetector(mapListener));break;
            case WebGL:// Code for WebGL applicationbreak;
                multiplexer.addProcessor(processorEvent);break;
            default:// Unhandled (new?) platform applicationbreak;
                multiplexer.addProcessor(processorEvent);
                multiplexer.addProcessor(new GestureDetector(mapListener));break;
         }/**/
        Gdx.input.setInputProcessor(multiplexer);
    }

    /*public void reset(MainGame mainGame)  {
        this.game=mainGame;

        GAMESTAGE_WIDTH=game.getWorldWidth();
        GAMESTAGE_HEIGHT=game.getWorldHeight();

        //ui
        {
            reader = game.gameConfig.reader;
            uiR=game.gameConfig.getCONFIG_LAYOUT().getElementById(screenId);//GameLayout.getXmlERootByScreenId(screenId);
            //defaultUiE=GameUtil.getXmlEByName(uiR, ResConfig.StringName.defaultWindowName);
        }


        //初始化游戏参数
        {
            ifGrid=false;
        }
        mapId=mainGame.getMapId();




        //1根据mapid读取地图DAO
        //2加载地图图片,生成图片作为背景
        *//*textureMap=framework.getMapByMapId(mapId);
        defMap=framework.getDefMapByMapId(mapId);
        mapW=defMap.getWidth();
        mapH=defMap.getHeight();
        mapSprite = new Sprite(textureMap);*//*

        //3增加拖动等功能,可以拖动地图
        //4替换地图dao的地图素材
        //5保存地图


        gp =new GLProfiler(Gdx. graphics);
        gp.enable();

        isTouching=false;
        multiplexer = new InputMultiplexer();

        // 使用伸展视口创建舞台
        stage = new Stage(new StretchViewport(GAMESTAGE_WIDTH, GAMESTAGE_HEIGHT));
        // 将输入处理设置到舞台（必须设置, 否则点击按钮没效果）

        multiplexer.addProcessor(stage);
        // 创建游戏人物演员
        //bgActor = new MapEditBgActor(new TextureRegion(mapSprite),0,0,mapW,mapH,GAMESTAGE_WIDTH, GAMESTAGE_HEIGHT);


        bgActor = new MapEditBgActor(mapId,GAMESTAGE_WIDTH, GAMESTAGE_HEIGHT,mainGame);


        *//*bgActor.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("点击的精灵位置1", "x:" + x+" y:" + y);
             }
        });*//*

        // 添加演员到舞台
        stage.addActor(bgActor);

        //根据screenId初始化资源
         *//*
         imgUpList=new TextureRegionListDAO();
         imgDownList=new TextureRegionListDAO();

        {
            imgUpList.add(imgLists.getTextureByName("cbtn_back"));
            imgUpList.add(imgLists.getTextureByName("cbtn_load"));
            imgUpList.add(imgLists.getTextureByName("mbtn_blank1"));
            imgUpList.add(imgLists.getTextureByName("mbtn_blank2"));
            imgUpList.add(imgLists.getTextureByName("mbtn_blank3"));
            imgUpList.add(imgLists.getTextureByName("mbtn_blank4"));
        }
        {
            imgDownList.add(imgLists.getTextureByName("cbtn_back"));
            imgDownList.add(imgLists.getTextureByName("cbtn_load"));
            imgDownList.add(imgLists.getTextureByName("mbtn_blank1"));
            imgDownList.add(imgLists.getTextureByName("mbtn_blank2"));
            imgDownList.add(imgLists.getTextureByName("mbtn_blank3"));
            imgDownList.add(imgLists.getTextureByName("mbtn_blank4"));
        }

        {
            //设定按钮
            for(int i=0;i<imgUpList.size();i++) {
                button = new ImageButton(new TextureRegionDrawable(new TextureRegion(imgUpList.get(i).getTextureRegion())),new TextureRegionDrawable(new TextureRegion(imgDownList.get(i).getTextureRegion())),new TextureRegionDrawable(new TextureRegion(imgDownList.get(i).getTextureRegion())));
                button.setSize(imgUpList.get(i).getTextureRegion().getRegionWidth(), imgUpList.get(i).getTextureRegion().getRegionHeight());
                button.setPosition(imgUpList.get(i).getRefx(),imgUpList.get(i).getRefy());
                //把按钮监听放到function(i)里了;
                function(i);
                stage.addActor(button);
            }
        }*//*

        i=1;
        // tempX=ui.get(u).getInt("x");tempY=ui.get(u).getInt("y");tempW=ui.get(u).getInt("w");tempH=ui.get(u).getInt("h");

        // 添加演员到舞台

        //遍历window的buttons按钮
        if (uiR.getChildCount() > 0) {
            windowGroups = new Array<>();
            for (int i = 0; i < uiR.getChildCount(); i++) {
                WindowGroup w = WindowGroup.obtain(WindowGroup.class);
                w.init(game, uiR.getChild(i), stage.getWidth(), stage.getHeight());
                //WindowGroup w=new WindowGroup(game,uiR.getChild(i),stage.getWidth(),stage.getHeight());//MainGame mainGame, XmlReader.Element groupE,int stageW,int stageH
                stage.addActor(w);
                windowGroups.add(w);
            }
            windowFunction(game.functionMap);
        }



        //文字示例
        label=new Label("FPS:"+Gdx.graphics.getFramesPerSecond(),new LabelStyle(game.defaultFont, null));
        //Label label=new Label("FPS:"+(int)(1+Math.random()*(10-1+1)),new LabelStyle(new BitmapFont(), null));

        label.setWidth(200);//设置每行的宽度
        label.setWrap(true);//开启换行
        label.setPosition(20, 40);
        stage.addActor(label);

        processorEvent = new InputProcessorEvent();
        mapListener=new MapListener();

        // Gdx.app.log("平台", Gdx.app.getGoodsType()+"");
        switch (Gdx.app.getType()) {
            case Desktop:// Code for Desktop applicationbreak;
                multiplexer.addProcessor(processorEvent);break;
            case Android:// Code for Android applicationbreak;
                multiplexer.addProcessor(new GestureDetector(mapListener));break;
            case WebGL:// Code for WebGL applicationbreak;
                multiplexer.addProcessor(processorEvent);break;
            default:// Unhandled (new?) platform applicationbreak;
                multiplexer.addProcessor(processorEvent);
                multiplexer.addProcessor(new GestureDetector(mapListener));break;
        }*//**//*
        Gdx.input.setInputProcessor(multiplexer);


    }*/


    @Override
    public void render(float delta) {


        game.batch.begin();
        handleInput();
        stage.act();
        stage.draw();
        //Gdx.app.log("FPS:", Gdx.graphics.getFramesPerSecond()+"");

        javaHeap=(Math.round(Gdx.app.getJavaHeap()/1024/1024*10)/10);
        if(javaHeap>javaMaxHeap) {
            javaMaxHeap=javaHeap;
        }

        label.setText("FPS:"+Gdx.graphics.getFramesPerSecond());
        label.getText().appendLine("");
        label.getText().appendLine("javaHeap:"+javaHeap+"m/"+javaMaxHeap+"m");
        label.getText().appendLine("drawCalls:"+gp.getDrawCalls());
        label.getText().appendLine("nativeHeap:"+Math.round(Gdx.app.getNativeHeap()/1024/1024*10)/10+"m");
        //label.getText().appendLine("inputKeyMode:"+transkeyMode(bgActor.getKeyMode()));//快捷键模式
        gp.reset();

        game.batch.end();
    }



    public void dispose() {
        super.dispose();
        // 场景被销毁时释放资源
        /*if (textureMap != null) {
            textureMap.dispose();
        }*/


        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
        if (stage != null) {
            stage.dispose();
            stage =null;
        }
        if (bgActor !=null) {
            bgActor.dispose();
            bgActor=null;
        }
    }

    public void windowFunction(ObjectMap<Button, Element> map) {
        for (Button b : map.keys()) {
            function(b, map.get(b).getInt("functionId"), map.get(b).getInt("value"));
            //Gdx.app.log("windowFunction:"+b.getImage().getName(),map.get(b).get("remark")+" functionId:"+map.get(b).getInt("functionId")+" v:"+map.get(b).getInt("value"));
        }
    }

    //实现的功能
    public void function(Button button, int functionId,final int value) {

        switch(functionId) {
            case 0://返回
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        //Gdx.app.log("点击了第1个按钮", "x:" + x+" y:" + y);
                        if(value==-1){
                           game.showGameScreen(screenId,1);return;
                        }else{//展开子项
                           WindowGroup w=windowGroups.get(0);
                            Element  tbE=uiR.getChild(0).getChildByName("tbuttons").getChild(value);

                            if(tbE!=null){
                                functionType=value;
                                Array<Element> tbEs=tbE.getChildrenByNameRecursively("ct");
                               int tbEc=tbEs.size;
                                for(int i=0,iMax=10;i<=iMax;i++){
                                    if(i<tbEc){
                                        w.setTBLabel(i,game.gameMethod.getStrValueAndHaveDefault(tbEs.get(i).get("text"),tbEs.get(i).get("text")));
                                    }else{
                                        w.hidTButton(i);
                                    }
                                }
                                switch (value){
                                    case 0:  setEditMode(-1);  break;
                                    case 1:  setEditMode(-1);  break;
                                    case 2:  setEditMode(0);  break;
                                    case 3:  setEditMode(2);  break;
                                    case 4:  setEditMode(-1);  break;
                                    case 5:  setEditMode(5);  break;
                                    case 6:  setEditMode(-1);  break;
                                }
                            }else{
                                functionType=0;
                                setEditMode(-1);
                            }
                        }


                    }
                });
                break;
            case 1://分列功能
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("点击了第2个按钮1", "x:" + x+" y:" + y+" mapId:"+mapId);

                        listFuntion(functionType,value);

                        /*if(ifGrid==false) {
                        ifGrid=true;
                    }else {
                        ifGrid=false;
                    }
                    bgActor.setIfGrid(ifGrid);*/
                    }
                });
                break;
            case 2://保存bin
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("点击了第3个按钮2", "x:" + x+" y:" + y+" mapId:"+mapId);
                        //bgActor.saveMapBin();
                    }
                });
                break;
            case 3://随机装饰(除特殊装饰外) 
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("点击了第4个按钮", "x:" + x+" y:" + y+" mapId:"+mapId);
                       // bgActor.getMapBinDAO().randomDecoration();
                    }
                });
                break;
            case 4:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("点击了第4个按钮", "x:" + x+" y:" + y+" mapId:"+mapId);
                       // bgActor.getMapBinDAO().checkDecoration(); //随机增加2层装饰

                    }
                });
                break;
            case 5:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("点击了第4个按钮", "x:" + x+" y:" + y+" mapId:"+mapId);
                       // bgActor.getMapBinDAO().resetRegionId();//重制省区

                    }
                });
                break;
            case 6:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("点击了第5个按钮", "x:" + x+" y:" + y+" mapId:"+mapId);

                        //bgActor.getMapBinDAO().cutRegion(); //自动绘制省区
                        //bgActor.getMapBinDAO().cutRegionForE(200); //自动绘制省区 e社
                        //bgActor.getMapBinDAO().updHabourForE();//修改海港,打印
                            //bgActor.writePNG();
                        //bgActor.getMapBinDAO().logLandRegionAndParentIsSeaRegion();
                    }
                });
                break;
            case 7:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("点击了第5个按钮", "x:" + x+" y:" + y+" mapId:"+mapId);

                        //bgActor.getMapBinDAO().cutRegion(); //自动绘制省区
                        //bgActor.getMapBinDAO().cutRegionForE(200); //自动绘制省区 e社
                        //bgActor.getMapBinDAO().updHabourForE();//修改海港,打印
                        //bgActor.writePNG();
                        //GameMap.drawPixmapForPriviewMap(bgActor.getMapBinDAO());
                    }
                });
                break;


            default://
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("点击了默认按钮", "x:" + x+" y:" + y);
                    }
                });
                break;
        }
    }



    public void resize(int width, int height) {
        // use true here to center the camera
        // that's what you probably want in case of a UI
        stage.getViewport().update(width, height, false);
    }


    //电脑
    class InputProcessorEvent implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            /*if (keycode == Keys.BACK) {
                // 处理返回事件
            } else if (keycode == Keys.MENU) {
                // 处理菜单事件
            }*/
            return true;  // 如果此处设置为false那么不会执行key up
        }

            @Override
            public boolean keyUp(int keycode) {
                return true;
            }
            @Override
            public boolean keyTyped(char character) {  // 可以输出按键的字母和数字，不过貌似不好使
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                cx=screenX;
                cy=screenY;
                return true;
            }

        @Override
            public boolean scrolled(float amountX, float amountY) {
            if(amountY>0) {
                bgActor.setZoom(bgActor.getZoom()+0.01f,cx,cy);
            }else if(amountY<0){
                bgActor.setZoom(bgActor.getZoom()-0.01f,cx,cy);
            }

            return false;
        }

    }
    //触摸屏


    class MapListener implements GestureListener{

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            Gdx.app.log("touchDown", "x:" + x+" y:" + y);
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            Gdx.app.log("tap", "x:" + x+" y:" + y);
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            Gdx.app.log("longPress", "x:" + x+" y:" + y);
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            Gdx.app.log("fling", "velocityX:" + velocityX+" velocityY:" + velocityY);
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            Gdx.app.log("touchDown", "x:" + x+" y:" + y);
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            Gdx.app.log("touchDown", "x:" + x+" y:" + y);
            return false;
        }

        @Override
        public boolean zoom (float originalDistance, float currentDistance){
            Gdx.app.log("zoom", "originalDistance:" + originalDistance+" currentDistance:" + currentDistance);
           //TODO 触摸缩放事件
            return false;
        }

        @Override
        public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){
            Gdx.app.log("pinch", "");
            return false;
        }
        @Override
        public void pinchStop () {
            Gdx.app.log("pinchStop", "");
        }

    }

    private void handleInput() {

        // justTouched 是开始按下手指的第一个点。
           if (Gdx.input.justTouched() && isTouching == false) {
               isTouching = true;
               touchBaseX = Gdx.input.getX(0);
               touchBaseY = Gdx.input.getY(0);
               //touchBaseX += cam.position.x - GAMESTAGE_WIDTH / 2;
               //Gdx.app.log("触摸", "1");

               // isTouched 是结束时，手指按下的点。
           } else if (Gdx.input.isTouched(0) && isTouching == true) {
               touch_X = Gdx.input.getX(0);
               touch_Y = Gdx.input.getY(0);
                moveX=(touchBaseX-touch_X)/20;
                moveY=(touch_Y-touchBaseY)/20;
               if(moveX>50) {
                   moveX=50;
               }
               if(moveX<-50) {
                   moveX=-50;
               }
               if(moveY>50) {
                   moveY=50;
               }
               if(moveY<-50) {
                   moveY=-50;
               }
               bgActor.setX(bgActor.getX()-moveX);
               bgActor.setY(bgActor.getY()-moveY);
           }else {
               isTouching =false;
           }
           /*
           if (Gdx.input.isKeyPressed(Input.Keys.A)) {
             bgActor.setZoom(bgActor.getZoom()+0.0001f,cx,cy);
           }
           if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
               bgActor.setZoom(bgActor.getZoom()-0.0001f,cx,cy);
           }*/

           if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
               bgActor.setX(bgActor.getX()+1);
               Gdx.app.log("bgActorPotion",bgActor.getX()+":"+bgActor.getY());
           }
           if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
               bgActor.setX(bgActor.getX()-1);
           }
           if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
               bgActor.setY(bgActor.getY()+1);
           }
           if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
               bgActor.setY(bgActor.getY()-1);
           }
           /*//切换快捷键模式
           if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
               if(bgActor.getKeyMode()+1<bgActor.getKeyModeMax()) {
                   bgActor.setKeyMode(bgActor.getKeyMode()+1);
               }else {
                   bgActor.setKeyMode(0);
               }
           }*/
       }




    private void listFuntion(int functionType, int value) {
        switch (functionType){
            case 0:
                switch (value){
                    //保存
                    case 0:  bgActor.saveMapBin();
                    break;
                    case 1: bgActor.savePreview();
                    break;
                    case 2: game.saveMapForPng();
                        break;
                    case 3: ;
                        bgActor.getMapBinDAO().zoomMap();
                    break;
                    case 4: bgActor.tempMethod();
                        break;
                }
                break;
            case 1:
                switch (value){
                    //隐藏全部辅助显示效果
                    case 0:  bgActor.gridState=-1;  break;
                    //显示网格
                    case 1: bgActor.gridState=0;  break;
                    //显示region
                    case 2: bgActor.gridState=1;  break;

                    case 3:instruct="loadBgImg ";
                        Gdx.input.getTextInput(listener, "please input ImgName (have suffix)", instruct,null); break;
                }
                break;
            case 2:
                switch (value){
                    //随机全部装饰 TODO
                    case 0: bgActor.getMapBinDAO().randomDecoration(0);  break;
                    //只编辑1级装饰
                    case 1: setEditMode(0);bgActor.setMode(0); break;
                    //随机1级装饰样式
                    case 2: bgActor.getMapBinDAO().randomDecoration(1); break;
                    //只编辑2级装饰
                    case 3: setEditMode(1);bgActor.setMode(0); break;
                    //随机添加2级装饰
                    case 4:  bgActor.getMapBinDAO().checkDecoration(); break;
                    //随机2级装饰样式
                    case 5: bgActor.getMapBinDAO().randomDecoration(2); break;
                    //自动海陆
                    case 6: bgActor.getMapBinDAO().resetAllCoastBorder(); break;
                    //只编辑河流
                    case 7: setEditMode(0);bgActor.setMode(1); break;
                    //修复河流延长问题
                    case 8: setEditMode(1);bgActor.setMode(1);
                        bgActor.copyBackTitle();break;
                }
                break;
            case 3:
                switch (value){
                    //清除全部region
                    case 0:bgActor.getMapBinDAO().clearAllRegion();  break;
                    //重置region到初始状态
                    case 1: bgActor.getMapBinDAO().resetRegionId(); break;
                    //自动生成region
                    case 2: bgActor.getMapBinDAO().cutRegion();  break;

                    case 3: bgActor.moveToBigRegionPotion();  break;
                    case 4: bgActor.checkRegion();  break;
                    case 5:   bgActor.getMapBinDAO().checkIsletRegion();break;
                }
                break;
            case 4:
                switch (value){
                    //管理建筑名称
                    case 0: setEditMode(3); break;
                  /*  //检查地名洲际和重复
                    case 1:bgActor.getMapBinDAO().checkBuildName();  break;*/

                    case 1:setEditMode(6); break;


                    //管理发展潜力
                    case 2: setEditMode(7); break;
                    //管理矿产
                    case 3: setEditMode(8); break;

                    case 4: setEditMode(11); break;
                    //管理石油
                    case 5: setEditMode(9); break;
                    //管理战略区域
                    case 6: setEditMode(10); break;
                    case 7: bgActor.getMapBinDAO().checkSR();break;

                }
                break;
            //TODO
            case 5:
                    if(value!=0&&bgActor.historyDAO==null){return;}
                switch (value){
                    case 0:
                        instruct="loadHistory ";
                        Gdx.input.getTextInput(listener, "please input historyIndex", instruct,null);
                        break;

                    //同步历史
                    case 1:bgActor.synchronousHistoryRegionByMapbinDAO(); break;


                    case 2:
                        instruct="logCountryRegion ";
                        Gdx.input.getTextInput(listener, "please input countryIndex", instruct,null);
                    break;

                    case 3:
                        bgActor.historyDAO.saveNowYearHistoryBins(game,bgActor.historyId,bgActor.getHistoryPage()+bgActor.getHistoryBeginYear() );
                        break;
                    case 4:
                        bgActor.historyDAO.saveAllYearHistoryBins(game,bgActor.historyId);
                        break;
                }
                break;
            case 6:
                switch (value){
                    case 0:  break;
                }
                break;
            case 7:
                switch (value){
                    case 0:  break;

                }
                break;
        }
    }

    public void setEditMode(int state){
        bgActor.setKeyMode(state);
        String s;
        switch (state){
            case -1:s="none";break;
            case 0:s="tile1";break;
            case 1:s="tile2";break;
            case 2:s="region";break;
            case 3:s="area";break;
            case 4:s="stage";break;
            case 5:s="history:"+(bgActor.getHistoryPage()+bgActor.getHistoryBeginYear());break;
            case 6:s="climateZone";break;
            case 7:s="dep";break;
            case 8:s="mineral";break;
            case 9:s="oil";break;
            case 10:s="SR";break;
            case 11:s="food";break;

            default:s="?";
        }
       s=s+" mode:"+bgActor.getMode();
        windowGroups.get(0).setLabelText(0,s);
    }


}