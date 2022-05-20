package com.zhfy.game.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AfterAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.framework.tool.EncryTool;
import com.zhfy.game.framework.tool.ShaderForScaleGray;
import com.zhfy.game.model.content.XmlIntDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.screen.actor.framework.AnimationActor;
import com.zhfy.game.screen.actor.framework.ImageActor;
import com.zhfy.game.screen.actor.framework.WindowGroup;
import com.zhfy.game.screen.stage.base.BaseStage;


import java.util.Iterator;

import static com.zhfy.game.config.ResDefaultConfig.Class.GeneralScreenConquestLoadGroupId;

/**
 * 主游戏场景（游戏主界面）, 实现 Screen 接口 或者 继承 ScreenAdapter 类 <br/>
 * 这里就展示一张图片代表游戏主界面
 */
public class GeneralScreen extends ScreenAdapter {

    private MainGame game;


    private Texture manTexture;

    private ImageActor bgImage;
    // private float clickBgX;
    // private float clickBgY;
    private boolean ifBanOperation;


    private BaseStage stage;
    private AnimationActor fireAnimationActor;

    private ImageButton button;
    //使用场景
    private int screenId = ResDefaultConfig.Class.GeneralScreen;
    //uiRoot
    private Element uiR;
    //ui
    //private List<Element> ui;
    private Element tempE;
    private float tempX, tempY, tempW, tempH;
    private Actor tempActor;
    float time;

    //Array<Element> buttonEs;
    //private Map tempMap;
    private int i;//function的计数标志,从1开始
    //private GameFramework framework;

    private Array<WindowGroup> windowGroups;
    private int state;
    private int tempValue;
    private int tempValue2;
    Label label;
    float javaHeap;
    float javaMaxHeap;
    public String  commandStr;
    public String  logStr;
    private Input.TextInputListener listener;
    private int listenerType;//0指令 1 userName 2 password 3 email 4 test code 5 自定义分辨率w 6自定义分辨率h
    private String instruct;
    private boolean ifInputVisable;
    private WindowGroup dialogueWindow;
    private int dialogueWindowFunctionIndex;
    private Vector2 tempVector2;
    private ShaderProgram shader;
    float grayscale = 0f;
    //通用对话框

    public WindowGroup getDialogueWindow(){
        return dialogueWindow;
    }

    public GeneralScreen(MainGame mainGame, int screenId) {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
        //获取传参
        this.game = mainGame;
        // 创建背景纹理, 图片 bg_main.png
        tempValue = 0;
        this.screenId = screenId;
        uiR = game.gameConfig.getCONFIG_LAYOUT().getElementById(screenId);
        //defaultUiE=GameUtil.getXmlEByName(uiR, ResConfig.StringName.defaultWindowName);
        tempVector2=new Vector2();

        //  manTexture = GameUtil.getBgTextureByStr(game,uiR.get("bg"));


        manTexture = game.getAssetManager().get(game.gameConfig.getFileNameForPath(uiR.getInt("resId"), uiR.get("bg")), Texture.class);
        //stages=new ArrayList<Stage>();

        //获取对应图片
        shader=ShaderForScaleGray.getShader();
        // i=1;
        stage = new BaseStage(mainGame, new StretchViewport(mainGame.getWorldWidth(), mainGame.getWorldHeight()),new SpriteBatch(1000, shader)) ;
        stage.getBatch().setShader(shader);
        // 添加演员到舞台
        // 创建游戏人物演员
        bgImage = new ImageActor(new TextureRegion(manTexture));
        stage.addActor(bgImage);

        //ifFull 是否铺满,默认是
       /* if (uiR.getBoolean("ifFull", true)) {
            bgImage.setSize(mainGame.getWorldWidth(), mainGame.getWorldHeight());
        }*/
        switch (uiR.get("bgType","")){
            case "full":
                bgImage.setSize(mainGame.getWorldWidth(), mainGame.getWorldHeight());
                break;
            case "tile":
                bgImage.setTitle(mainGame.getWorldWidth(), mainGame.getWorldHeight());
                break;
            case "fixHeightAndCrossMove":
                bgImage.setScale(stage.getHeight()/bgImage.getHeight());
                bgImage.setLoop(0,bgImage.getWidth()* bgImage.getScaleX());

                // Runnable 动作
                RunnableAction runnable = Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        bgImage.setX(bgImage.getX()-0.5f);
                        if(bgImage.getLoopW()+bgImage.getX()<=0){
                            bgImage.setX(0);
                            bgImage.setLoopState(0);
                        }else if(bgImage.getX()+stage.getWidth()<bgImage.getWidth()){//向左平移超过,显示1
                            bgImage.setLoopState(1);
                        }
                    }
                });
                SequenceAction sequence = Actions.sequence(runnable);
                // 重复动作: 重复执行 sequence
                RepeatAction repeatAction = Actions.forever(sequence);

                // 执行重复动作
                bgImage.addAction(repeatAction);
                break;

        }



        if(game.gameConfig.ifEffect){
            Animation fireAnimation=game.getAssetManager().get("gif/fire_1.gif",Animation.class);
            if(fireAnimation!=null){
                fireAnimation.setPlayMode(Animation.PlayMode.LOOP);
                fireAnimationActor=new AnimationActor(fireAnimation);
                fireAnimationActor.setScaleYByAnimationH( mainGame.getWorldHeight());
                fireAnimationActor.setScaleXByAnimationW(mainGame.getWorldWidth() );
                fireAnimationActor.setColor(1,1,1,0.5f);
                stage.addActor(fireAnimationActor);
            }
        }

        //inputPattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$");

        ifInputVisable=true;
        //指令功能监视
        listener= new Input.TextInputListener() {
            @Override
            public void input(String text) {

                ifInputVisable=true;

                switch (listenerType){
                    case 0:
                        instruct=text;
                        parseCommand(text); break;
                    case 1://userName
                        WindowGroup w=    windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);

                        if(w!=null){
                            TextButton tb= w.getTButton(0);
                            if(tb!=null){
                                tb.getLabel().setColor(Color.WHITE);
                                tb.getLabel().setText(text);
                                if(ComUtil.isEmpty(text)||!text.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$")){
                                    tb.getLabel().setColor(Color.RED);
                                }
                            }
                        }
                        break;
                    case 2://password
                        w=    windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                        if(w!=null){
                            TextButton tb= w.getTButton(1);
                            if(tb!=null){
                                tb.getLabel().setColor(Color.WHITE);
                                tb.getLabel().setText(text);
                                if(ComUtil.isEmpty(text)||!text.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$")){
                                    tb.getLabel().setColor(Color.RED);
                                }
                            }
                        }
                        break;
                    case 3://email
                        w=    windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                        if(w!=null){
                            TextButton tb= w.getTButton(1);
                            if(tb!=null){
                                tb.getLabel().setColor(Color.WHITE);
                                tb.getLabel().setText(text);
                                if(ComUtil.isEmpty(text)||!text.matches("^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$")){
                                    tb.getLabel().setColor(Color.RED);
                                }
                            }
                        }
                        break;
                    case 4://test code

                        try {
                            if(ComUtil.isNumeric(text)){
                                String dtc2= EncryTool.encrypt(text,ComUtil.getTime()+"");
                                String dtc=game.gameConfig.playerInfo.get("testCode","-1");
                                if( dtc.equals(dtc2)){
                                    game.gameConfig.playerInfo.put("ifTestCode",true);
                                    ifBanOperation=false;
                                }else {
                                    game.gameConfig.playerInfo.put("ifTestCode",false);
                                    Gdx.app.exit();
                                }
                            }
                        } catch (Exception e) {
                            Gdx.app.exit();
                        }


                        break;
                    case 5:    //screen w
                        if(ComUtil.isNumeric(text)){
                            game.gameConfig.playerConfig.put("customScreenW",text);
                            game.gameConfig.playerConfig.flush();
                            listenerType=6;
                            Gdx.input.getTextInput(listener, "set screen hight", "","");
                        }
                        break;
                    case 6:    //screen h
                        if(ComUtil.isNumeric(text)){
                            game.gameConfig.playerConfig.put("customScreenH",text);
                            game.gameConfig.playerConfig.put("customScreen",true);
                            game.gameConfig.playerConfig.flush();
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                            game.gameLayout.updOptionWindowForScreen(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                        }
                        break;

                    default:
                        Gdx.app.error("no avilable input",text);
                        break;
                }

            }

            @Override
            public void canceled() {
                ifInputVisable=true;

                switch (listenerType){
                    case 4:
                        game.gameConfig.playerInfo.put("ifTestCode",false); Gdx.app.exit(); break;
                    case 5:
                    case 6:
                        game.gameConfig.playerConfig.put("customScreen",false);
                        game.gameConfig.playerConfig.flush();
                        game.gameLayout.updOptionWindowForScreen(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                        break;
                }

            }
        };

        if (uiR.getChildCount() > 0) {
            windowGroups = new Array<>();
            for (int i = 0; i < uiR.getChildCount(); i++) {
                WindowGroup w = WindowGroup.obtain(WindowGroup.class);
                w.init(game, uiR.getChild(i), stage.getWidth(), stage.getHeight(),tempVector2);
                //WindowGroup w=new WindowGroup(game,uiR.getChild(i),stage.getWidth(),stage.getHeight());//MainGame mainGame, XmlReader.Element groupE,int stageW,int stageH
                stage.addActor(w);
                windowGroups.add(w);
            }
            //创建一个用于对话框的通用window
            dialogueWindow = WindowGroup.obtain(WindowGroup.class);
            Element xE= game.gameConfig.getDefaultWindowE(1);
            if(xE!=null){
                dialogueWindow.init(game, xE, stage.getWidth(), stage.getHeight(),tempVector2);
                stage.addActor(dialogueWindow);
            }

            windowFunction(game.functionMap);
        }
        // 将输入处理设置到舞台（必须设置, 否则点击按钮没效果）
        Gdx.input.setInputProcessor(stage);
        /*// 使用伸展视口创建舞台
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
        }
        //测试框架
        //framework.getStagesByScreenId(screenId);
        */

        //MainGame game,Window dialogWindow,String windowBg,Color fontColor,String text,float x,float y


        //文字示例
        label=new Label("",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        label.setWidth(500);//设置每行的宽度
        label.setWrap(true);//开启换行
        label.setPosition(10, 100);
        stage.addActor(label);

        state=-1;
        tempValue=-1;
        tempValue2=-1;



        initGroup();

        if(ResDefaultConfig.ifTest){//进行验证码验证
            try {
                boolean ifOk=game.gameConfig.playerInfo.getBoolean("ifTestCode",false);
                String core=game.gameConfig.playerInfo.get("testCode","-1");
                if(core.equals("-1")){//未输入过
                    ifBanOperation=true;
                    String testCode=ComUtil.formmatString(ComUtil.getRandom(0,99999999)+"",8,true,"0");
                    String time=ComUtil.getTime()+"";
                    String dtc= EncryTool.encrypt(testCode,time);
                    game.gameConfig.playerInfo.put("testCode",dtc);
                    instruct=dtc;
                    Gdx.input.getTextInput(listener, "need test code:"+dtc, instruct,"");
                    listenerType=4;
                    Gdx.app.log("need testCode",dtc+":"+testCode);
                }else if(!ifOk){//输入错误
                    ifBanOperation=true;
                    String dtc=   game.gameConfig.playerInfo.get("testCode","none error");
                    instruct=dtc;
                    Gdx.input.getTextInput(listener, "need test code:"+dtc, instruct,"");
                    listenerType=4;
                    Gdx.app.log("need testCode",dtc);
                }/*else{//正式

                }*/
            } catch (Exception e) {
                Gdx.app.exit();
            }

        }
    }
    //命令解析
    public void parseCommand(String text) {
        commandStr=text;
        //Gdx.app.log("parseCommand",text);
        //单信息命令
        switch (text){
            case "logSagaGroupInfo":
                logSagaGroupForViewStr();
                return;
            case "setDebugActorFlipX":
                if(tempActor!=null){
                    GameUtil.filpActor(tempActor,false,true);
                }
                return;
            case "setDebugActorFlipY":
                if(tempActor!=null){
                    GameUtil.filpActor(tempActor,false,true);
                }
                return;
        }

        String[] rs=text.split(" ");

        if(rs.length==2){//备份存档
            switch (rs[0]){
                case "backupSav": game.backupSav(rs[1]);game.sMapScreen.commandStr=text+" updOk"; return;
            }
        }else if(rs.length==4){
            String type=rs[0];

            switch (type){
                case "test":// test windowId b/i/l actorId
                    WindowGroup w=windowGroups.get(Integer.parseInt(rs[1]));
                    int value=Integer.parseInt(rs[3]);
                    switch(rs[2]){
                        case "t"://textbutton

                            setDebugActor(text,w.getTButton(value));
                            commandStr="ok";
                            break;
                        case "b"://button

                            setDebugActor(text,w.getButton(value));
                            commandStr="ok";
                            break;
                        case "i"://image
                            setDebugActor(text,w.getImage(value));
                            commandStr="ok";
                            break;
                        case "l"://label
                            setDebugActor(text,w.getLabel(value));
                            commandStr="ok";
                            break;
                        default:
                            closeDebug();
                            commandStr="closeDebug";
                            break;
                    }
                case "addImage": {
                    TextureRegionDAO t = game.getImgLists().getTextureByName(rs[3]);
                    if (t == null) {
                        commandStr = "error command :imageName is error ";
                        return;
                    }
                    int wId = -1;
                    if (ComUtil.isNumeric(rs[1])) {
                        wId = Integer.parseInt(rs[1]);
                    }
                    if (wId == -1 || wId >= windowGroups.size) {
                        commandStr = "error command :windowIndex is error ";
                        return;
                    }
                    WindowGroup w1 = windowGroups.get(wId);
                    if (w1 == null) {
                        commandStr = "error command :WindowGroup is error ";
                        return;
                    }
                    int imgId = -1;
                    if (ComUtil.isNumeric(rs[2])) {
                        imgId = Integer.parseInt(rs[2]);
                    }
                    if (imgId == -1) {
                        commandStr = "error command :imgId is error ";
                        return;
                    }
                    Image i = new Image(t.getTextureRegion());
                    i.setName(t.getName());
                    w1.addImg(imgId, i);
                    label.setPosition(100,100);
                    tempActor=i;
                    tempActor.setDebug(true);
                }
                break;
                case "addLabel": {
                    int wId = -1;
                    if (ComUtil.isNumeric(rs[1])) {
                        wId = Integer.parseInt(rs[1]);
                    }
                    if (wId == -1 || wId >= windowGroups.size) {
                        commandStr = "error command :windowIndex is error ";
                        return;
                    }
                    WindowGroup w1 = windowGroups.get(wId);
                    if (w1 == null) {
                        commandStr = "error command :WindowGroup is error ";
                        return;
                    }
                    int labelId = -1;
                    if (ComUtil.isNumeric(rs[2])) {
                        labelId = Integer.parseInt(rs[2]);
                    }
                    if (labelId == -1) {
                        commandStr = "error command :labelId is error ";
                        return;
                    }
                    Label label=new Label("",new Label.LabelStyle(game.gameConfig.gameFont,Color.BLACK));
                    label.setFontScale(game.gameConfig.gameFontScale*1.5f);
                    label.setAlignment(Align.center);
                    label.setText(rs[3]);
                    w1.addLabel(labelId, label);
                    label.setPosition(100,100);
                    tempActor=label;
                    tempActor.setDebug(true);
                }
                break;



                default:  commandStr="error command :type is invalid "+rs[0];
                    return;
            }
        }else {
            commandStr="error command for length by split space";
        }

    }



    //打印信息 logSagaGroupInfo
    private void logSagaGroupForViewStr() {
        if(game.gameConfig.getPlatform().equals("Desktop")||game.gameConfig.getPlatform().equals("HeadlessDesktop")){
            StringBuilder b=new StringBuilder();
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId);
            Image i=w.getImage(0);
            b.append("<view id=\"").append(game.getStageId()).append("\" x=\"").append(((int)i.getX())).append("\" y=\"").append(((int)i.getY())).append("\" scale=\"").append(i.getScaleX()).append("\" remark=\"").append(game.gameMethod.getStrValue("stage_name_"+game.getStageId())).append("\" >");
            Iterator<IntMap.Entry<Label>> itL = w.labels.iterator();
            while (itL.hasNext()) {
                IntMap.Entry<Label> c = itL.next();
                if (c.value != null&&c.key>=100) {
                    b.append("\n").append("\t<mark id=\"").append(c.key).append("\" type=\"label\" name=\"").append(c.value.getText()).append("\" scale=\"").append(c.value.getScaleX()).append("\" rotation=\"").append(c.value.getRotation()).append("\" x=\"").append(((int)c.value.getX())).append("\" y=\"").append(((int)c.value.getY())).append("\"  />");
                }
            }


            Iterator<IntMap.Entry<Image>> itI = w.images.iterator();
            while (itI.hasNext()) {
                IntMap.Entry<Image> c = itI.next();
                if (c.value != null&&c.key>=100) {
                    b.append("\n").append("<mark id=\"").append(c.key).append("\" type=\"image\" name=\"").append(c.value.getName()).append("\" scale=\"").append(c.value.getScaleX()).append("\" rotation=\"").append(c.value.getRotation()).append("\" x=\"")
                            .append(((int)c.value.getX())).append("\" y=\"").append(((int)c.value.getY())).append("\" flipX=\"false\" flipY=\"false\"/>");
                }
            }
            b.append("\n</view>");
            ComUtil.copyStrForPCClipboard(b.toString());
            openDialog(0,4,1,0);

        }
    }

    public void setDebugActor(String name, Actor a) {
        if(tempActor!=null){
            Gdx.app.log("setDebugActor:"+tempActor.getName(), "x:"+tempActor.getX()+" y:"+tempActor.getY()+" refx:"+tempW+" refy:"+tempH);
            tempActor.setDebug(false);
        }
        tempActor=a;
        tempX=0;
        tempY=0;
        tempW=0;
        tempH=0;
        if(tempActor!=null){
            tempActor.setDebug(true);
            tempActor.setName(name);
        }
    }

    public void closeDebug() {
        if(tempActor!=null){
            Gdx.app.log("setDebugActor:"+tempActor.getName(), "x:"+tempActor.getX()+" y:"+tempActor.getY()+" refx:"+tempW+" refy:"+tempH);
            tempActor.setDebug(false);
        }
        tempActor=null;
        tempX=0;
        tempY=0;
        tempW=0;
        tempH=0;
    }



    @Override
    public void render(float delta) {
        try {
            time += Gdx.graphics.getDeltaTime();
            grayscale =  0.7f+ ((float)Math.sin(time)/2f+0.5f)/4;

            handleInput();

            // 红色清屏
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            shader.setUniformf("grayscale", grayscale);
            game.batch.begin();
            if(ResDefaultConfig.ifDebug){
                javaHeap=(Math.round(Gdx.app.getJavaHeap()/1024/1024*10)/10);
                if(javaHeap>javaMaxHeap) {
                    javaMaxHeap=javaHeap;
                }

                label.setText("FPS:"+Gdx.graphics.getFramesPerSecond()+" "+game.getWorldWidth()+"*"+game.getWorldHeight());
                label.getText().appendLine("grayscale:"+grayscale);
                label.getText().appendLine("javaHeap:"+javaHeap+"m/"+javaMaxHeap+"m");
                label.getText().appendLine("nativeHeap:"+Math.round(Gdx.app.getNativeHeap()/1024/1024*10)/10+"m");
                if(!game.saveOk){
                    if(game.asyncManager.getTaskSize()==0){
                        game.saveOk=true;
                    }else {
                        label.getText().appendLine("game saving...");
                    }
                }
                label.getText().appendLine(commandStr);
                label.getText().appendLine(logStr);
            }else {
                if(!game.saveOk){
                    if(game.asyncManager.getTaskSize()==0){
                        game.saveOk=true;
                    }else {
                        label.getText().appendLine("game saving...");
                    }
                }
            }


            if (stage != null) {
                stage.act();// 更新舞台逻辑
            }

            if (stage != null) {
                stage.draw(); // 绘制舞台
                //BlurUtils.blur(stage);
            }

            game.batch.end();
        } catch (Exception e) {
            if(ResDefaultConfig.ifDebug){
                e.printStackTrace();
            }else if(!game.gameConfig.getIfIgnoreBug()){
                game.remindBugFeedBack();
            }
            game.recordLog("GeneralScreen render ",e);
        }

    }

    public void dispose() {
        super.dispose();
        // 场景被销毁时释放资源
        /*if (manTexture != null) {
            manTexture.dispose();
        }*/
        for (WindowGroup w : windowGroups) {
            w.remove();
            w=null;
        }
        game.clearAllTempTextureRegions();
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        if(dialogueWindow!=null){
            dialogueWindow=null;
        }
    }

    public void windowFunction(ObjectMap<Button, Element> map) {
        for (Button b : map.keys()) {
            Element xmlE=map.get(b);
            function(b, xmlE.getInt("functionId"), xmlE.getInt("value"),xmlE.get("sound","click"));
            //Gdx.app.log("windowFunction:"+b.getImage().getName(),map.get(b).get("remark")+" functionId:"+map.get(b).getInt("functionId")+" v:"+map.get(b).getInt("value"));
        }
    }

    //Window
    //    4011: 重开存档位置
    // 4012: 读取存档
    //    4013: 关闭选择存档窗口
    //   6000:ifHD
    //   6001:music -
    //   6002:music +
    //   6003:sound -
    //   6004:sound +


    //实现的功能
    /*
    0:帝国/战役
    1:征服
    2:指挥官
    3:设置
    4:地图
    5:返回主页
    6:地图跳入(i)
    7:跳入详细地图
    8:
    9:
    10:

     */

    public void function(final Button button, int functionId, final int value, final String sound) {

        //  int i=map.get(ResConfig.StringName.functionId);
        switch (functionId) {
            case 0:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        //Gdx.app.log("点击了第1个按钮", "x:" + x+" y:" + y);
                        Gdx.app.log("点击了第0个按钮", "x:" + x + " y:" + y);
                        //TODO 暂时禁用传奇saga功能
                    /**/  if(value==4){
                            return;
                         }
                      /*  //安卓禁用地图编辑功能
                        if(  value==6&&Gdx.app.getType()== Application.ApplicationType.Android){
                            return;
                        }*/
                        /*if(screenId==ResDefaultConfig.Class.MainScreen&&windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainPromptGroupId).isVisible()){
                            return;
                        }*/
                        ifBanOperation=true;
                        if (value == -1) {
                            game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
                            return;
                        }else if(value==81){//返回smapscreen
                            if(game.getSMapDAO()==null){
                                game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
                            }else{
                                game.showGameScreen(screenId, ResDefaultConfig.Class.SMapScreen);
                            }
                            return;
                        }
                        WindowGroup w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainDefaultGroupId);

                        w.setOrigin(button.getX() + button.getWidth() / 2, button.getY() + button.getHeight() / 2);
                        ScaleToAction scaleTo = Actions.scaleTo(10.0F, 10.0F, 0.5F);


                        // Runnable 动作
                        RunnableAction runnable = Actions.run(new Runnable() {
                            @Override
                            public void run() {

                                int targetScreen = 0;
                                switch (value) {
                                    case 0:
                                        targetScreen = 0;
                                        break;//TODO
                                    case 1:
                                        targetScreen = ResDefaultConfig.Class.EmpireScreen;
                                        break;
                                    case 2:
                                        targetScreen = ResDefaultConfig.Class.ConquestScreen;
                                        break;
                                    case 3:
                                        targetScreen = ResDefaultConfig.Class.HistoryScreen;
                                        break;
                                    case 4:
                                        targetScreen = ResDefaultConfig.Class.SagaScreen;
                                        break;
                                    case 5:
                                        targetScreen = ResDefaultConfig.Class.HQScreen;
                                        break;
                                    case 6:
                                        targetScreen = ResDefaultConfig.Class.MapEditScreen;
                                }

                                game.showGameScreen(screenId, targetScreen);
                            }
                        });

                        AfterAction afterAction = Actions.after(runnable);
                        w.addAction(scaleTo);
                        w.addAction(afterAction);


                        //game.showGameScreen(screenId,3);
                    }
                });
                break;
            case 1://TODO
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        Gdx.app.error("点击了第1个空按钮", "x:" + x + " y:" + y);

                        game.playSound(sound);

                    }
                });
                break;
            case 2://TODO
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }

                        game.playSound(sound);
                        Gdx.app.error("点击了第2个空按钮", "x:" + x + " y:" + y);


                    }
                });
                break;
            case 3:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        WindowGroup w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenEmpireDialogueGroupId);
                        int stageEpisode = game.gameConfig.playerConfig.getInteger(value + "_gameEpisode", 0);
                        Element e = game.gameConfig.getEmpireXmlE(value, stageEpisode);
                        boolean ifSave = game.ifStageFileExist(value);
                        if(e==null){
                            stageEpisode=0;
                            e =  game.gameConfig.getEmpireXmlE(value, stageEpisode);
                            ifSave=false;
                            if(e==null){
                                return;
                            }
                        }
                        game.gameLayout.updEmpireDialogueGroupId(w, e.getInt("playerCountry"), e.getInt("generalId"), "stage_content_" + value  + ComUtil.formmatNumber(stageEpisode,2,true,"0"), ifSave,value);
                        game.setStageId(value);
                    }
                });
                break;
            case 4://跳入游戏
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        // Gdx.app.log("setStageId","stageId:"+map.get(ResConfig.StringName.stageId));
                        if(value==0){
                            game.setStageId(0);
                            boolean ifSave = game.ifStageFileExist(0);

                            if (ifSave) {//弹框
                                tempValue = -1;
                                state = 0;
                                WindowGroup w = windowGroups.get(GeneralScreenConquestLoadGroupId);
                                w.hidButton(3);

                                //Preferences li=game.gameConfig.config;
                                // Gdx.app.error("game.gameConfig.config","c:"+game.gameConfig.config.getString((game.getStageId()+"_roundNow")));
                                w.setVisibleForEffect(true);
                                w.setImageRegion(ResDefaultConfig.Class.SMapScreenSelectCountryGroupImageFlagId, game.getImgLists().getTextureByName("flag_" + game.gameConfig.playerConfig.getInteger(game.getStageId() + "_country", 0)).getTextureRegion());
                                int year=game.gameConfig.playerConfig.getInteger(game.getStageId() + "_year",-1);
                             if(year==-1){
                                 w.setLabelText(0,  game.gameMethod.getStrValueT("stage_name_none"));
                             }else{
                                 w.setLabelText(0, game.gameMethod.getStrValue("stage_name_0")+"-"+year);
                             }
                                w.setLabelText(1, game.gameConfig.playerConfig.getInteger((game.getStageId() + "_roundNow"), 0) + "");
                                w.setLabelText(2, game.gameConfig.playerConfig.getInteger((game.getStageId() + "_regionNum"), 0) + "");
                            } else {
                                WindowGroup w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestFreeYearGroupId);
                                w.setVisibleForEffect(true);
                                if (tempValue <= 0) {
                                    tempValue = 1860;
                                    w.setLabelText(1, tempValue + "");
                                    w.setLabelText(0, game.gameMethod.getStrValueT("stage_name_0"));
                                }
                                // game.loadSMapDAO(-1,0);
                                //  game.showGameScreen(screenId,81);
                            }
                        }else {
                            game.setStageId(value);
                            boolean ifSave = game.ifStageFileExist(value);;
                            boolean ifNew;
                            if (value < 1000) {
                                ifNew = game.gameConfig.playerConfig.getBoolean(game.getStageId() + "_ifNew",false);
                            } else {
                                ifNew = false;
                            }
                            if (ifSave || ifNew) {//弹框
                                if (ifSave) {
                                    state = 0;
                                } else if (ifNew) {
                                    state = 1;
                                }
                                WindowGroup w = windowGroups.get(GeneralScreenConquestLoadGroupId);
                                if (game.gameConfig.playerConfig.getBoolean(game.getStageId() + "_ifNew", false)) {
                                    w.showButton(3);
                                } else {//不显示
                                    w.hidButton(3);
                                }

                                //Preferences li=game.gameConfig.config;
                                // Gdx.app.error("game.gameConfig.config","c:"+game.gameConfig.config.getString((game.getStageId()+"_roundNow")));
                                w.setVisibleForEffect(true);
                                w.setImageRegion(ResDefaultConfig.Class.SMapScreenSelectCountryGroupImageFlagId, game.getImgLists().getTextureByName("flag_" + game.gameConfig.playerConfig.getInteger(game.getStageId() + "_country", 0)).getTextureRegion());
                                int gameMode=game.gameConfig.playerConfig.getInteger(game.getStageId() + "_mode",-1);
                            if(gameMode==-1){
                                w.setLabelText(0,  game.gameMethod.getStrValueT("stage_name_none"));
                            }else{
                                w.setLabelText(0, game.gameMethod.getStrValue("stage_name_"+value)+"-"+game.gameMethod.getStrValue("stage_mode_"+gameMode));
                            }

                                w.setLabelText(1, game.gameConfig.playerConfig.getInteger((game.getStageId() + "_roundNow"), 0) + "");
                                w.setLabelText(2, game.gameConfig.playerConfig.getInteger((game.getStageId() + "_regionNum"), 0) + "");

                            } else {
                                game.loadSMapDAO(value,0,-1, false);
                                game.showGameScreen(screenId, 81);
                            }
                        }
                    }
                });
                break;
            case 5://返回
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        //game.showGameScreen(6);
                        game.showGameScreen(screenId, 1);
                    }
                });
                break;
            case 6://地图跳入
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        //game.showGameScreen(6);
                        game.showGameScreen(screenId, 7);
                    }
                });
                break;
            case 7://地图编辑
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        // game.setMapId(Integer.parseInt(map.get("ID").toString()));
                        game.setMapId(value + 1);
                        game.showGameScreen(screenId, 71);
                    }
                });
                break;


            case 10:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }

                        game.playSound(sound);
                    }
                });
                break;

            case 13:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        WindowGroup w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestFreeYearGroupId);
                        if (value == 0) {
                            tempValue = 0;
                            w.setVisibleForEffect(false);
                        } else if (value == 1) {//上一年
                            if (tempValue > 1840) {
                                tempValue--;
                                w.setLabelText(1, tempValue + "");
                            }
                        } else if (value == 2) {//下一年
                            if (tempValue < 1920) {
                                tempValue++;
                                w.setLabelText(1, tempValue + "");
                            }
                        } else if (value == 3) {
                            game.gameConfig.playerConfig.putInteger("freeConquestYear", tempValue);
                            game.gameConfig.playerConfig.flush();
                            game.loadSMapDAO(game.getStageId(),0,tempValue, false);
                            game.showGameScreen(screenId, 81);
                        }
                    }
                });
                break;

            case 14:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        //Gdx.app.log("点击了第1个按钮", "x:" + x+" y:" + y);

                        switch (value) {
                            case 1://重开存档

                                if (tempValue ==0) {
                                    //game.gameConfig.playerInfo.putBoolean(game.getStageId() + "_ifSave", false);
                                    int stageEpisode = game.gameConfig.playerConfig.getInteger(game.getStageId() + "_gameEpisode", 0);
                                    game.deleteSav(stageEpisode>0?false:true);
                                    game.loadSMapDAO(game.getStageId() ,0,-1, false);
                                    game.showGameScreen(screenId, 81);
                                } else {
                                    windowGroups.get(1).setVisible(false);
                               //     windowGroups.get(2).setVisible(true);
                                    WindowGroup w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestFreeYearGroupId);
                                    w.setVisibleForEffect(true);
                                    if (tempValue <= 0) {
                                        tempValue = 1860;
                                        w.setLabelText(1, tempValue + "");
                                        w.setLabelText(0, game.gameMethod.getStrValueT("stage_name_0"));
                                    }
                                }

                                break;
                            case 2://读取 如果是没有存档的情况下,默认打开新世界
                                if (state == 0) {
                                    game.loadSMapDAO(game.getStageId(),0,-1, false);
                                } else {
                                    game.gameConfig.playerConfig.putBoolean(game.getStageId() + "_ifNew", false);
                                    game.gameConfig.playerConfig.flush();
                                    game.loadSMapDAO(game.getStageId(),0,-1, true);
                                }
                                game.showGameScreen(screenId, 81);
                                break;
                            case 3:
                                windowGroups.get(1).setVisibleForEffect(false);
                                break;
                            case 4://打开新世界
                                game.gameConfig.playerConfig.putBoolean(game.getStageId() + "_ifNew", false);
                                game.gameConfig.playerConfig.flush();
                                game.loadSMapDAO(game.getStageId(),0,-1, true);
                                game.showGameScreen(screenId, 81);
                                break;
                        }


                    }
                });
                break;

            case 100:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if(value==0){
                             WindowGroup w=   windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainPromptGroupId);
                             w.setVisibleForEffect(true);
                            w.setLabelText(0,game.gameMethod.getStrValueT("prompt_main_title"));
                            w.setScrollLabel(1,game.gameConfig.getMainInfoStr());
                        }else if(value==1){//指令
                            ifInputVisable=false;
                            listenerType=0;
                            Gdx.input.getTextInput(listener, "Console", instruct,"");
                        }else if(value==2){//作者信息介绍
                            WindowGroup w=   windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainPromptGroupId);
                            w.setVisibleForEffect(true);
                            w.setLabelText(0,game.gameMethod.getStrValueT("prompt_works_title"));
                            w.setScrollLabel(1,game.gameMethod.getStrValueT("prompt_works_info"));
                        }else {
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainPromptGroupId).setVisibleForEffect(false);
                        }
                    }
                });
                break;

            case 101:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        //game.showGameScreen(screenId, ResDefaultConfig.Class.OptionScreen);
                        if(value==0){//设置主页面
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).setVisibleForEffect(true);
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).setVisible(false);
                            game.gameLayout.updOptionWindow(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                        }else if(value==-1){//关闭
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).setVisibleForEffect(false);
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).setVisibleForEffect(false);
                        }else if(value==1){//切换到设置副页面
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).setVisibleForEffect(true);
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).setVisible(false);
                        }
                    }
                });
                break;
            case 102:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        switch (value) {
                            case 0://恢复默认
                                updOptionWindowForMod(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                break;
                            case 1:
                                game.setMusicVoice(game.musicVoice - 1);
                                game.gameConfig.playerConfig.putInteger("music", game.musicVoice);
                                game.gameConfig.playerConfig.flush();
                                game.gameLayout.updOptionWindow(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                                break;
                            case 2:
                                game.setMusicVoice(game.musicVoice + 1);
                                game.gameConfig.playerConfig.putInteger("music", game.musicVoice);
                                game.gameConfig.playerConfig.flush();
                                game.gameLayout.updOptionWindow(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                                break;
                            case 3:
                                game.setSoundVoice(game.soundVoice - 1);
                                game.gameConfig.playerConfig.putInteger("sound", game.soundVoice);
                                game.gameConfig.playerConfig.flush();
                                game.gameLayout.updOptionWindow(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                                break;
                            case 4:
                                game.setSoundVoice(game.soundVoice + 1);
                                game.gameConfig.playerConfig.putInteger("sound", game.soundVoice);
                                game.gameConfig.playerConfig.flush();
                                game.gameLayout.updOptionWindow(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                                break;
                            case 5://上一个mod
                                updOptionWindowForMod(-1);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                break;
                            case 6://下一个mod
                                updOptionWindowForMod(1);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                break;
                            case 7://全屏/窗口
                                if(Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
                                    if (game.gameConfig.playerConfig.getBoolean("isFullscreen", false)) {
                                        game.gameConfig.playerConfig.putBoolean("isFullscreen", false);
                                        game.gameConfig.playerConfig.flush();
                                    } else {
                                        game.gameConfig.playerConfig.putBoolean("isFullscreen", true);
                                        game.gameConfig.playerConfig.flush();
                                    }
                                    game.gameLayout.updOptionWindowForScreen(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                }
                                break;
                            case 8://分辨率调整 向左
                                int index= game.gameConfig.playerConfig.getInteger("screenSizeIndex",ResDefaultConfig.Game.defaultScreenIndex);
                                if(index-1<0){
                                    index=ResDefaultConfig.Game.screenHeight.length-1;
                                }else {
                                    index--;
                                }
                                game.gameConfig.playerConfig.putInteger("screenSizeIndex",index);
                                game.gameConfig.playerConfig.putBoolean("customScreen",false);
                                game.gameConfig.playerConfig.flush();
                                game.gameLayout.updOptionWindowForScreen(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                break;
                            case 9://分辨率调整 向右
                                index= game.gameConfig.playerConfig.getInteger("screenSizeIndex",ResDefaultConfig.Game.defaultScreenIndex);
                                if(index+1>=ResDefaultConfig.Game.screenHeight.length){
                                    index=0;
                                }else {
                                    index++;
                                }
                                game.gameConfig.playerConfig.putInteger("screenSizeIndex",index);
                                game.gameConfig.playerConfig.putBoolean("customScreen",false);
                                game.gameConfig.playerConfig.flush();
                                game.gameLayout.updOptionWindowForScreen(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                //123
                                break;
                            case 11://休闲模式
                                game.gameConfig.setLeisureMode();
                                game.gameLayout.updOption2Window(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId));
                                break;
                            case 12://敏感屏蔽
                                game.gameConfig.setShieldVistable();
                                game.gameLayout.updOption2Window(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId));
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                break;
                            case 13://缩略图颜色
                                game.gameConfig.setIfColor();
                                game.gameLayout.updOption2Window(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId));
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                break;
                            case 14://垂直同步
                                game.gameConfig.setVSync();
                                game.gameLayout.updOption2Window(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId));
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                                break;
                            case 15://忽略错误报告
                                game.gameConfig.setIfIgnoreBug();
                                game.gameLayout.updOption2Window(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId));
                                break;
                            case 16://自定义分辨率
                                listenerType=5;
                                Gdx.input.getTextInput(listener, "set screen width", "","");
                                break;
                        }
                    }
                });
                break;
            case 103://语言选择 1英2中3日
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if (value != 0) {
                            //设定语言
                            game.gameConfig.setLanguage(value);
                            game.gameConfig.clearFirstPrompt();
                            //设定样式
                            updOptionScreenLanguageUI(value);
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId).showLabel(0);
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).showLabel(2);
                        }
                    }
                });
                break;

            case 104://显示商品  GeneralScreenMainGoodsGroupId
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if(value==0){
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainGoodsGroupId).setVisibleForEffect(true);
                        }else {
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainGoodsGroupId).setVisibleForEffect(false);
                        }
                    }
                });
                break;

            case 105://打开关闭账户
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if(game.gameNet==null||game.gameNet.getStatus()==-1){//无网络或找不到服务器
                            openDialog(1,7,1,0);
                            return;
                        }else if(game.gameNet.getStatus()==-2){//服务器维护
                            openDialog(1,8,1,0);
                            return;
                        }else if(game.gameNet.getStatus()==1){//已登陆
                            openDialog(1,13,1,0);
                            return;
                        }else if(game.gameNet.getStatus()==0){ //未登陆
                            tempValue=400;
                            if(value==0){
                                WindowGroup w=windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                                w.setTBLabel(0,"usname");
                                w.setTBLabel(1,"password");
                                w.showButton(1);
                                w.setButtonImage(2,game.getImgLists().getTextureByName("button_login"));
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId).setVisibleForEffect(true);
                            }else {
                                windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId).setVisibleForEffect(false);
                            }
                        }else {
                            openDialog(1,14,1,0);
                            return;
                        }
                    }
                });
                break;
            case 106://账户输入信息 1 usname 2 password
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        if (ifInputVisable) {
                            game.playSound(sound);
                            ifInputVisable=false;
                            listenerType=value;
                            String lastStr="";
                            String tiltle="";
                            String hint="It is a string of 6 to 8 letters or numbers";
                            switch (value){
                                case 1:
                                    WindowGroup w=windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                                    if(w!=null){
                                        lastStr=w.getTButton(0).getLabel().getText().toString();
                                        tiltle="usname";
                                        if(ComUtil.isEmpty(lastStr)&&!lastStr.equals("usname")){
                                            hint=game.gameMethod.getStrValue("title_account_prompt1");
                                        }else{
                                            lastStr="";
                                            hint="";
                                        }
                                    }
                                    break;
                                case 2:
                                    w=windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                                    if(w!=null){
                                        lastStr=w.getTButton(1).getLabel().getText().toString();
                                        tiltle="password";
                                        if(ComUtil.isEmpty(lastStr)&&!lastStr.equals("password")){
                                            hint=game.gameMethod.getStrValue("title_account_prompt1");
                                        }else{
                                            lastStr="";
                                            hint="";
                                        }
                                    }
                                    break;
                            }
                            Gdx.input.getTextInput(listener, tiltle, lastStr,hint);
                        }
                    }
                });
                break;


            case 107://显示注册界面
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                        if(tempValue==400){//显示注册界面
                            w.setTBLabel(0,"usname");
                            w.setTBLabel(1,"email");
                            w.hidButton(1);
                            w.setButtonImage(2,game.getImgLists().getTextureByName("button_register"));
                            tempValue=401;
                        }
                    }
                });
                break;
            case 108://登陆
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);

                        if(tempValue==400){//注册
                            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                            Label l1=w.getTButton(0).getLabel();
                            Label l2=w.getTButton(1).getLabel();


                            if(l1.getColor().equals(Color.RED)
                                    ||l2.getColor().equals(Color.RED)
                            ){
                                w.setVisible(false);
                                openDialog(1,5,1,0);
                            }
                            if(l1.getText().equals("usname")){
                                w.setVisible(false);
                                openDialog(1,6,1,0);
                            }
                        }else if(tempValue==401){//提交准备登陆
                            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                            Label l1=w.getTButton(0).getLabel();
                            //用户名
                            if(l1.getColor().equals(Color.RED)){
                                openDialog(0,11,1,0);
                                return;
                            }
                            if(l1.getText().equals("usname")){
                                w.setVisible(false);
                                openDialog(1,12,1,0);
                            }
                            String   str=w.getTButton(1).getLabel().getText().toString();
                            // 验证邮箱是否合法
                            if(!ComUtil.isEmail(str)){
                                openDialog(0,11,1,0);
                                return;
                            }


                            //TODO 验证邮箱是否有重复注册

                            str=w.getTButton(0).getLabel().getText().toString();


                            //TODO 验证用户名是否重复注册


                            //发送并提示接收邮箱验证码


                        }else if(tempValue==402){//提交验证码等信息 进行注册





                        }

                    }
                });
                break;



            case 400:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if(value==0){
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestPromptGroupId).setVisibleForEffect(true);
                        }else {
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestPromptGroupId).setVisibleForEffect(false);
                        }
                    }
                });
                break;
            case 500:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        game.gameLayout.updHistoryGroupForDialogue(windowGroups.get(0), tempE.getChild(value));
                    }
                });
                break;


            case 501:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if (value == 1) {//上一年
                            if (tempValue == 0) {
                                tempValue = tempValue2 - 1;
                            } else {
                                tempValue--;
                            }
                        } else if (value == 2) {//下一年
                            if (tempValue >= tempValue2 - 1) {
                                tempValue = 0;
                            } else {
                                tempValue++;
                            }
                        }
                        Gdx.app.log("next year:" + value, tempValue + ":" + tempValue2);
                        tempE = game.gameConfig.getDEF_BATTLE().e.getChild(tempValue);
                        game.gameLayout.initHistoryGroupForMain(windowGroups.get(0), tempE);
                    }
                });
                break;
            case 502:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if(value==0){
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenHistoryPromptGroupId).setVisibleForEffect(true);

                        }else{
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenHistoryPromptGroupId).setVisibleForEffect(false);
                        }
                    }
                });

                break;
            case 700://  1 map 2 stage
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        switch (value) {
                            case 1:
                                tempE=game.gameConfig.getDEF_MAP().e;
                                tempValue=0;
                                tempValue2=tempE.getChildCount();
                                game.gameLayout.updMapEditGroup(windowGroups.get(0),tempE.getChild(tempValue),1);
                                state=1;
                                break;

                            case 2:
                                tempE=game.gameConfig.getDEF_STAGE().e;
                                tempValue=0;
                                tempValue2=tempE.getChildCount();
                                game.gameLayout.updMapEditGroup(windowGroups.get(0),tempE.getChild(tempValue),2);
                                state=2;
                                break;
                        }
                    }
                });

                break;
            case 701://0 loadMap  1 lastIndex  2nextIndex
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        switch (value){
                            case 0:
                                if(state==1){
                                    game.setMapId(tempE.getChild(tempValue).getInt("id"));
                                    game.showGameScreen(screenId, 71);
                                }else{//战役
                                    game.setStageId(tempE.getChild(tempValue).getInt("id"));
                                    game.loadSMapDAO(game.getStageId(),1,-1, false);
                                    game.showGameScreen(screenId, 81);
                                }
                                break;
                            case 1:if (tempValue == 0) {
                                tempValue = tempValue2 - 1;
                            } else {
                                tempValue--;
                            }
                                game.gameLayout.updMapEditGroup(windowGroups.get(0),tempE.getChild(tempValue),state);
                                break;
                            case 2:
                                if (tempValue >= tempValue2 - 1) {
                                    tempValue = 0;
                                } else {
                                    tempValue++;
                                }
                                game.gameLayout.updMapEditGroup(windowGroups.get(0),tempE.getChild(tempValue),state);
                                break;
                        }
                    }
                });
                break;

            case 801:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if(value==0){
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenHQPromptGroupId).setVisibleForEffect(true);
                        }else{
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenHQPromptGroupId).setVisibleForEffect(false);
                        }
                    }
                });
                break;
            case 900:  button.addListener(new ClickListener() {//展示saga选择
                public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                    game.playSound(sound);
                    //返回
                    sagaGroupBack();
                }
            });
                break;

            case 901:
                button.addListener(new ClickListener() {//展示saga选择
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if(!windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaSelectGroupId).ifSlide){
                            showSagaStageGroup(value);
                        }
                    }
                });
                break;
            case 902:
                button.addListener(new ClickListener() {//展示saga选择
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        //TODO 反复点击,即进入stage
                        if(tempValue2==value){


                            Array<Element> xmlE=  game.gameConfig.getSagaStageXmlEs(state,tempValue);
                            if(xmlE==null||xmlE.size==0){
                                return;
                            }
                            Element xml=xmlE.get(value);
                            game.gameConfig.playerConfig.putInteger("lastSagaGeneralId_"+state+"_"+tempValue,xml.getInt("general",-1));
                            game.gameConfig.playerConfig.putInteger("lastSagaCountryId_"+state+"_"+tempValue,xml.getInt("country",-1));

                            game.gameConfig.playerConfig.putInteger("lastGeneral",xml.getInt("general",-1));
                            game.gameConfig.playerConfig.putInteger("lastCountry",xml.getInt("country",0));

                            game.gameConfig.playerConfig.flush();


                            // windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaSelectGroup).get

                        }else{
                            clickSagaStage(value);
                        }
                    }
                });
                break;
            case 903:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        //展示框
                        if(value==-1){
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaPromptGroupId).setVisible(false);
                        }else{
                            windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaPromptGroupId).setVisible(true);
                        }
                    }
                });
                break;
            case 904:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        //展示框
                        if(value==1){//上一年
                            state--;
                            if(state<0){
                                state=ResDefaultConfig.Game.ageMax;
                            }
                        }else if(value==2){//下一年
                            state++;
                            if(state>ResDefaultConfig.Game.ageMax){
                                state=0;
                            }
                        }
                        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId);
                        game.gameLayout.updSagaAgeSelected(w,state);
                        w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaSelectGroupId);
                        game.gameLayout.initSagaSelectGroup(game.generalScreen,w,state);
                    }
                });
            case 10001:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        if(value==0){//0关闭 1确认 2取消
                            dialogueWindow.setVisible(false);
                        }else{
                            switch (dialogueWindowFunctionIndex){
                                case 0:
                                    dialogueWindow.setVisible(false);
                                    break;
                                case 1:
                                    if(value==1){//TODO 退出  https://www.icode9.com/content-1-294886.html
                                        Gdx.app.exit();
                                    }else {
                                        dialogueWindow.setVisible(false);
                                    }
                                    break;

                            }
                        }
                    }
                });
                break;
            default:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {  if(ifBanOperation){return; }
                        game.playSound(sound);
                        Gdx.app.log("点击了其他按钮", "x:" + x + " y:" + y);
                    }
                });
                break;
        }
    }

    private void updOptionWindowForMod(int type) {
        if(type==0){//恢复默认
            game.gameConfig.playerConfig.putInteger("useModId",0);
            game.gameConfig.playerConfig.flush();

            FileHandle file= Gdx.files.local("config_mod.xml");
            if(file.exists()){
                file.delete();
            }
            game.gameConfig.resetModConfig();
            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).setLabelText(0,game.gameConfig.getModName());
        }else{
            XmlIntDAO modX=game.gameConfig.getCONFIG_MOD();
            if(modX==null||modX.e.getChildCount()==0){
                return;
            }

            int index=game.gameConfig.playerConfig.getInteger("useModId",0)+type;
            if(index<0){
                index=modX.e.getChildCount()-1;
            }
            if(index==modX.e.getChildCount()){
                index=0;
            }
            game.gameConfig.playerConfig.putInteger("useModId",index);
            game.gameConfig.playerConfig.flush();
            game.gameConfig.resetModConfig();
            windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId).setLabelText(0,game.gameConfig.getModName());
        }

    }


    private void openDialog(int titleId, int textId, int style,int functionIndex) {
        if(dialogueWindow==null){
            return;
        }
        dialogueWindowFunctionIndex=functionIndex;
        if(style==0){
            dialogueWindow.showButton(1);
            dialogueWindow.showButton(2);
        }else if(style==1){
            dialogueWindow.hidButton(1);
            dialogueWindow.showButton(2);
        }
        dialogueWindow.setLabelText(0,game.gameMethod.getStrValue("window_title_"+titleId));
        dialogueWindow.setLabelText(1,game.gameMethod.getStrValue("window_text_"+textId));
        dialogueWindow.setVisible(true);
    }

    //TODO 将来字库等确定好后 根据mod支持的字库显示不同的语言选项
    private void updOptionScreenLanguageUI(int value) {
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId);
        ImageButton b1 = w.getButton(value + 10);
        ImageButton b2 = w.getButton(10);
        b2.setX(b1.getX()-4);
        b2.setY(b1.getY() + w.getButtonXmlE(10).getInt("refy", 0)-3);


        //b2.setWidth(b1.getWidth());
        //b2.setHeight(b1.getHeight());
    }


    private void initGroup() {
        WindowGroup w;
        switch (screenId) {
            case ResDefaultConfig.Class.MainScreen:
                w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainDefaultGroupId);
                game.gameLayout.updMainGroup(w);
                w.setImageToFixImage(3,0, ResDefaultConfig.Image.BORDER_IMAGE_REFW, ResDefaultConfig.Image.BORDER_IMAGE_REFH);
                if(game.gameNet==null||game.gameNet.getStatus()==-1){
                    w.hidButton(9);
                }else{
                    w.showButton(9);
                }
                w =  windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainPromptGroupId);

                if(ResDefaultConfig.ifTest||ResDefaultConfig.ifDebug||!game.gameConfig.ifIgnoreBug){
                    w.showButton(1);
                }else {
                    w.hidButton(1);
                }
                game.gameLayout.updOptionWindow(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOptionGroupId));
                game.gameLayout.updOption2Window(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainOption2GroupId));
                int language = game.gameConfig.getLangueageId();
                updOptionScreenLanguageUI(language);

                w =  windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainAccountsGroupId);
                game.gameLayout.updAccountGroup(w,0);
                break;
            case ResDefaultConfig.Class.OptionScreen://废弃

            case ResDefaultConfig.Class.ConquestScreen:
                tempValue=0;
                tempValue2=0;
                state=0;
                //判断是否显示新世界按钮
                w = windowGroups.get(GeneralScreenConquestLoadGroupId);
                if (game.gameConfig.playerConfig.getBoolean(game.getStageId() + "_ifNew", false)) {
                    w.showButton(3);
                } else {//不显示
                    w.hidButton(3);
                }
                w = windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId);
                game.gameLayout.initConquestGroupForStar(w);
                w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestPromptGroupId);
                game.gameLayout.initConquestGroupForPrompt(w);
                break;
            case ResDefaultConfig.Class.EmpireScreen:
                tempValue=0;
                tempValue2=0;
                state=0;
                w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenEmpireStageGroupId);
                game.gameLayout.initEmpireGroupForMain(w);
                w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenEmpireDialogueGroupId);
                game.gameLayout.updEmpireDialogueGroupId(w, 0, 0, "empire_prologue", true,-1);
                break;
            case ResDefaultConfig.Class.HistoryScreen:
                state=0;
                //page
                tempValue = 0;
                //pageSize
                tempValue2 = game.gameConfig.getDEF_BATTLE().e.getChildCount();
                tempE = game.gameConfig.getDEF_BATTLE().e.getChild(tempValue);
                game.gameLayout.initHistoryGroupForMain(windowGroups.get(0), tempE);
                //game.gameLayout.updHistoryGroupForDialogue(tempE,-1);
                w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenHistoryPromptGroupId);
                game.gameLayout.initHistoryGroupForPrompt(w);
                break;
            case ResDefaultConfig.Class.MapEditScreen:
                w = windowGroups.get(0);
                game.gameLayout.initMapEditGroup(w);
                state=0;
                break;

            case ResDefaultConfig.Class.HQScreen://210318
                w = windowGroups.get(0);
                game.gameLayout.initHQGroup(w,windowGroups.get(1));

                w = windowGroups.get(2);
                w.setLabelText(0,game.gameMethod.getStrValueT("prompt_hq_title"));
                w.setScrollLabel(1,game.gameMethod.getStrValueT("prompt_hq_info"));
                break;
            case ResDefaultConfig.Class.SagaScreen: //state为age  tempValue为选择的saga tempValue2位选择的子stage
                w = windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId);
                //  manTexture = game.getAssetManager().get(game.gameConfig.getFileNameForPath(uiR.getInt("resId"), uiR.get("bg")), Texture.class);
                if(game.getAssetManager().contains(ResDefaultConfig.Path.WorldMapPath0)){
                    w.setImageRegion(0,new TextureRegion(game.getAssetManager().get(ResDefaultConfig.Path.WorldMapPath0, Texture.class)));
                }
                if(game.getAssetManager().contains(ResDefaultConfig.Path.WorldMapPath1)){
                    w.setImageRegion(0,new TextureRegion(game.getAssetManager().get(ResDefaultConfig.Path.WorldMapPath1, Texture.class)));
                }
                state=0;
                game.gameLayout.updSagaAgeSelected(w,state);
                w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaSelectGroupId);
                game.gameLayout.initSagaSelectGroup(this,w,state);
                break;
            default:
                Gdx.app.log("待处理screen", screenId + "");
        }
    }
    private void handleInput(){
        if(ifBanOperation){
            return;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)||Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){//点击返回键
            screenBack();
            return;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.MENU)){//点击返回键
            if(!game.saveOk){
                openDialog(0,15,1,0);
                return;
            }
            openDialog(0,2,0,1);
            return;
        }
        if(!ResDefaultConfig.ifDebug){
            return;
        }
        if (ifInputVisable&&(Gdx.input.isKeyJustPressed(Input.Keys.T)|| Gdx.input.isKeyPressed(Input.Keys.NUMPAD_5))) {
            ifInputVisable=false;
            listenerType=0;
            Gdx.input.getTextInput(listener, "Console", instruct,"");
        }
        if(tempActor!=null) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)) {
                tempActor.setX(tempActor.getX() - 1f);
                tempW = tempW - 1f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)) {
                tempActor.setX(tempActor.getX() + 1f);
                tempW = tempW + 1f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
                tempActor.setY(tempActor.getY() - 1f);
                tempH = tempH - 1f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)) {
                tempActor.setY(tempActor.getY() + 1f);
                tempH = tempH + 1f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_7)) {
                tempActor.setScale(tempActor.getScaleX() + 0.1f);
            } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_9)) {
                tempActor.setScale(tempActor.getScaleX() - 0.1f);
            } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)) {
                tempActor.setRotation(tempActor.getRotation() + 1f);
            } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_3)) {
                tempActor.setRotation(tempActor.getRotation() - 1f);
            }
            logStr = "x:" + tempActor.getX() + " y:" + tempActor.getY() + " scale:" + tempActor.getScaleX()+" rotation:"+tempActor.getRotation();
        }
    }


    private void showSagaStageGroup(int value) {
        //Gdx.app.log("selectSagaStage","sagaStage:"+value);
        tempValue=value;
        if(value==-1){
            windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaSelectGroupId).setVisible(true);
            windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaStageGroupId).setVisible(false);
            windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId).hidImage(1);
            return;
        }

        WindowGroup  w= windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaStageGroupId);
        if(  game.gameLayout.updSagaStageGroup(this,w,state,tempValue,windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId).getImage(1).getX())){
            windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaSelectGroupId).setVisible(false);
            windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaStageGroupId).setVisible(true);
            w = windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId);
            w.showImage(0);
            w.showImage(1);
            w.showButton(0);

            //默认打开第一个
            clickSagaStage(0);
        }else{
            tempValue=-1;
            tempValue2=-1;
            game.gameLayout.hidSagaGroupDiogiue(w);
            return;
        }
    }

    //saga页面的返回  state为age  tempValue为选择的saga
    private void sagaGroupBack(){
        if(tempValue==-1){//返回上一页
            game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
            return;
        }else{//返回上个sagaSelect
            WindowGroup w= windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId);
            //屏蔽掉生成的标示
            w.hideAllLabel();
            w.hideAllImage();
            w.hidButton(1);
            //更新页面
            game.gameLayout.updSagaAgeSelected(w,state);
            windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaStageGroupId).setVisible(false);
            windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaSelectGroupId).setVisible(true);
            tempValue=-1;
            updSagaGroupBorderSelected(tempValue2,-1);
            tempValue2=-1;
            tempE=null;

            w = windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaSelectGroupId);
            game.gameLayout.initSagaSelectGroup(game.generalScreen,w,state);
        }
    }



    private void updSagaGroupView(Element stageXmlE) {
        int stageId= stageXmlE.getInt("id");
        WindowGroup w= windowGroups.get(ResDefaultConfig.Class.ScreenDefaultGroupId);
        w.hideAll();
        w.showButton(0);
        w.showButton(1);
        w.showImage(0);
        w.showImage(1);

        Element xmlE=game.gameConfig.getDEF_VIEW().getElementById(stageId);
        if(xmlE==null){
            w.hidImage(0);
            w.hidButton(1);
            game.gameLayout.hidSagaGroupDiogiue(w);
            return;
        }else {
            game.gameLayout.updSagaGroupView(w,xmlE,(int)(w.getImage(1).getX()),(int) (w.getStage().getHeight()));
            game.gameLayout.updSagaGroupDialogue(w,stageXmlE);

        }
    }

    //选择按钮后 lastIndex →,nextIndex ←
    private void updSagaGroupBorderSelected(int lastIndex,int nextIndex){
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaStageGroupId);
        //float moveX,float moveY,float second,int[] imageIds,int[] buttonIds,int[] labelIds,int[] tButtonIds
        if(lastIndex!=-1){
            w.moveActor(40,0,0.5f, new int[]{lastIndex+100, lastIndex + 200, lastIndex + 300, lastIndex + 400},new int[]{lastIndex+100},new int[]{lastIndex+100},null);
        }
        if(nextIndex!=-1){
            w.moveActor(-40,0,0.5f, new int[]{nextIndex+100, nextIndex + 200, nextIndex + 300, nextIndex + 400},new int[]{nextIndex+100},new int[]{nextIndex+100},null);
        }

    }


    private void clickSagaStage(int index) {
        Array<Element> xmlEs=  game.gameConfig.getSagaStageXmlEs(state,tempValue);
        if(xmlEs==null||xmlEs.size==0){
            return;
        }
        Element useXmlE=xmlEs.get(index);
        //如果默认的不能选取,重新选择一个
        if(useXmlE==null||!game.gameConfig.ifCanUseInLanguage(useXmlE.get("useLanguage","-1"))){
            for(int i=0;i<xmlEs.size;i++){
                useXmlE=xmlEs.get(i);
                if(game.gameConfig.ifCanUseInLanguage(useXmlE.get("useLanguage","-1"))){
                    index=i;
                    break;
                }
            }
        }
        if( useXmlE!=null){
            int stageId=useXmlE.getInt("id",-1);
            if(stageId!=-1){
                updSagaGroupBorderSelected(tempValue2,index);
                updSagaGroupView(useXmlE);
            }
            tempValue2=index;
            game.setStageId(stageId);
        }
    }

    private void screenBack() {
        if(dialogueWindow.isVisible()){
            dialogueWindow.setVisible(false);
            return;
        }
        switch (screenId){
            case ResDefaultConfig.Class.MainScreen:
                //1.关闭group界面
                //2.弹出退出游戏框
                if(windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainPromptGroupId).isVisible()){
                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenMainPromptGroupId).setVisible(false);
                }else {
                    if(!game.saveOk){
                        openDialog(0,15,1,0);
                        return;
                    }
                    openDialog(0,2,0,1);
                }
                break;
            case ResDefaultConfig.Class.EmpireScreen:
                //返回主页
                game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
                break;
            case ResDefaultConfig.Class.ConquestScreen:
                //关闭说明框和战役选择框,加载存档框
                //返回主页
                if(windowGroups.get(GeneralScreenConquestLoadGroupId).isVisible()){
                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestLoadGroupId).setVisible(false);
                }else  if(windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestFreeYearGroupId).isVisible()){
                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestFreeYearGroupId).setVisible(false);
                }else  if(windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestPromptGroupId).isVisible()){
                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenConquestPromptGroupId).setVisible(false);
                }else {
                    game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
                }
                break;
            case ResDefaultConfig.Class.HistoryScreen:
                //关闭说明框
                //返回主页
                if(windowGroups.get(ResDefaultConfig.Class.GeneralScreenHistoryPromptGroupId).isVisible()){
                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenHistoryPromptGroupId).setVisible(false);
                }else {
                    game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
                }
                break;
            case ResDefaultConfig.Class.OptionScreen:
                //返回主页
                game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
                break;
            case ResDefaultConfig.Class.MapEditScreen:
                //返回主页
                game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
                break;
            case ResDefaultConfig.Class.HQScreen:
                //关闭说明框
                //返回主页
                if(windowGroups.get(ResDefaultConfig.Class.GeneralScreenHQPromptGroupId).isVisible()){
                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenHQPromptGroupId).setVisible(false);
                }else {
                    game.showGameScreen(screenId, ResDefaultConfig.Class.MainScreen);
                }
                break;
            case ResDefaultConfig.Class.SagaScreen:
                //关闭说明框
                //saga的返回上层
                if(windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaPromptGroupId).isVisible()){
                    windowGroups.get(ResDefaultConfig.Class.GeneralScreenSagaPromptGroupId).setVisible(false);
                }else {
                    sagaGroupBack();
                }
                break;

            default:
                Gdx.app.log("clickBackButton is error","screenId:"+screenId);
                break;
        }
    }

    public void remindBugFeedBack() {
        openDialog(0,3,1,0);
    }



    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        Gdx.app.log("resize","screenId:"+screenId);
    }
}