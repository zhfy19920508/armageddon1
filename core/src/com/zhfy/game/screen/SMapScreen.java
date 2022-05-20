package com.zhfy.game.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.framework.GameMethod;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.framework.tool.CHAsyncTask;
import com.zhfy.game.framework.tool.LogTime;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.XmlIntDAO;
import com.zhfy.game.model.content.btl.BtlModule;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.frameenum.GameOperateState;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.screen.actor.framework.AnimationActor;
import com.zhfy.game.screen.actor.framework.WindowGroup;
import com.zhfy.game.screen.stage.SMapEffectStage;
import com.zhfy.game.screen.stage.SMapGameStage;
import com.zhfy.game.screen.stage.base.BaseStage;

import static com.zhfy.game.model.frameenum.GameOperateState.selectHexagonToAirborne;
import static com.zhfy.game.model.frameenum.GameOperateState.selectHexagonToAirborneTactical;
import static com.zhfy.game.model.frameenum.GameOperateState.selectHexagonToTactic;
import static com.zhfy.game.model.frameenum.GameOperateState.selectRegionToAirTarget;
import static com.zhfy.game.model.frameenum.GameOperateState.selectRegionToNulTarget;


/**
 * 主游戏场景（游戏主界面）, 实现 Screen 接口 或者 继承 ScreenAdapter 类 <br/>
 * 这里就展示一张图片代表游戏主界面
 */
public class SMapScreen extends ScreenAdapter  {

    private MainGame game;
    public SMapGameStage smapGameStage;
    public SMapEffectStage smapEffectStage;
    private BaseStage uiStage;
    public WindowGroup defaultWindow;
    private String instruct;
    private XmlIntDAO script;
    private Array<Element> scriptXmlEs;
    private int scriptStep;
    private int cmdStep;
    public boolean ifBanOperation;
    private int promptTextIndex;
    private BtlModule bm;//通用编辑模式
    private Input.TextInputListener bmListener;
    private int bmIndex;
    private float moveZoom;
    LogTime logTime;
   // private IntArray liList;//执行过后ai的记录
    // private  WarpUtils warpUtils;
    //private Stage tempStage;
    public boolean ifSave;
    private Input.TextInputListener listener;
    public boolean ifEnd;
    public int saveState;//-1未保存 0保存中 1保存结束
    public boolean ifRoundInEnd;
    private int result;
    private float  endDeltaSum;
    //使用场景
    private int screenId= ResDefaultConfig.Class.SMapScreen;
    // InputEvent tempInput;

    InputProcessorEvent processorEvent;
    MapListener mapListener;
    InputMultiplexer multiplexer;
    public boolean isTouching;
    private float touchBaseX;
    private float touchBaseY;
    private float touch_X;
    private float touch_Y;
    private float moveX;
    private float moveY;
    public boolean ifZoom;
    private float oldCurrentDistance;
    private float oldCurrentZoom;
    //private float oldCurrentDistanceZ;
    //private float oldCurrentDistanceT;
    private boolean ifInputVisable;
    private int dialogueFunctionIndex;//0 选择国家 1返回 -1仅提示

    Label label;

    //Label gameTimeLabel;
    //中心点位置
    float cx;
    float cy;
    float javaHeap;
    float javaMaxHeap;
    boolean ifLoop;

    //游戏舞台的基本宽高
    static  float GAMESTAGE_WIDTH;
    static  float GAMESTAGE_HEIGHT;
    GLProfiler  gp;



    //ui
    private Element uiR;
    //private XmlReader reader ;
    //private String bgTexture;
    public float tempX,tempY,tempW,tempH;
    public Actor tempActor;

    //  private Array<Element> buttonEs;
    //  private Array<Element> ImageEs;

    private Array<WindowGroup> windowGroups;


    public Array<WindowGroup> getWindowGroups() {
        return windowGroups;
    }
//private Map tempMap;

    //private int i;//function的计数标志,从1开始
    // private SMapBGActor mapActor;

    public Image progressCountryImage;
    public int lastRoundCountryId;

    //选择国家弹框
    private int selectCardPotion;//选中的卡牌 1-7
    public int selectCardType;//-1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标  18定制战舰 19奇物
    // 101陆军加载核弹  102空军加载核弹
    private int selectTextGroupType;  //textGoup 0特性 1任务 2国策 3征服任务  achieveGroup 0奇物 1荣誉 2为陆军招募将军 3为空军招募将军
    public int selectCardId;
    private int adviceIndex;



    //当点击地块的时候获取地块详情
    private Array<Fb2Smap.ArmyData>  selectArmyList;
    private Array<Fb2Smap.ArmyData>  selectNavyList;
    private Array<Fb2Smap.AirData> selectAirList;
    private Array<Fb2Smap.NulcleData> selectNulList;
    private int selectListPage;
    private int selectListIndex;//1-4
    private int selectListSumPage;
    private int tradeInfoSumPage;
    private int tradeInfoNowPage;
    public int selectListType;//右边栏 -1空 0战术卡 1陆军 2海军 3空军 4特殊武器
    public Array<Element> haveCardEList;//未分页的卡牌
    private Array<Element> selectCardEList;//选中的卡牌
    private int cardSumPage;
    private int cardNowPage;
    private Image image;
    public String selectStr;
    public String  commandStr;
    public TextButton promptText;
    public Vector2 tempVector2;
    //public ImageButton scriptButton_c;
    //public ImageButton scriptButton_n;
    //形势图
    public Pixmap priviewMapPixmap;
    public Texture priviewMapTexture;
    public Pixmap politicalMapPixmap;
    public Texture politicalMapTexture;

    //是否模糊渲染游戏背景
    public boolean ifWarp;

    public int lastWeatherId;
    public AnimationActor weatherAnimationActor;




    public SMapScreen(MainGame mainGame)  {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
        logTime=new LogTime();
        ifRoundInEnd=false;
        //logTime.setDebugTime(3000);
        //liList=new IntArray();
        selectCardId =0;
        this.ifWarp=false;
        this.selectCardType=-1;
        this.selectCardPotion=0;
        bm=new BtlModule();
        this.game=mainGame;
        GAMESTAGE_WIDTH=game.getWorldWidth();
        GAMESTAGE_HEIGHT=game.getWorldHeight();
        ifZoom=false;
        saveState=-1;
        //ui
        uiR=game.gameConfig.getCONFIG_LAYOUT().getElementById(screenId);
        adviceIndex=0;
        moveZoom=DefDAO.getMoveZoom(game.moveSpeed);
        int  defaultId =GameUtil.getXmlEByName(uiR, ResDefaultConfig.StringName.defaultWindowName).getInt("id");

        gp =new GLProfiler(Gdx. graphics);
        gp.enable();

        isTouching=false;
        multiplexer = new InputMultiplexer();
        //根据screenId初始化资源
        //获取对应图片

        uiStage = new BaseStage(game,new StretchViewport(GAMESTAGE_WIDTH, GAMESTAGE_HEIGHT));


        multiplexer.addProcessor(uiStage);

        if(game.gameConfig.ifEffect){
            weatherAnimationActor=new AnimationActor();
            uiStage.addActor(weatherAnimationActor);
        }
        selectStr="";
        commandStr="";

        // 使用伸展视口创建舞台
        smapGameStage = new SMapGameStage(mainGame,new StretchViewport(game.getWorldWidth(),game.getWorldHeight()),screenId);//FillViewport
        if(smapGameStage==null){
            game.showGameScreen(screenId,1);
            return;
        }
        lastRoundCountryId=smapGameStage.getsMapDAO().roundCountryIndex;
        //warpUtils=new WarpUtils(smapGameStage);
        smapEffectStage=new SMapEffectStage(mainGame,smapGameStage);
        smapGameStage.getsMapDAO().gameStage=smapGameStage;
        smapGameStage.getsMapDAO().effectStage=smapEffectStage;
        smapGameStage.getsMapDAO().logTime=logTime;
        priviewMapPixmap =  GameMap.getPriviewMapBySMapDAO(priviewMapPixmap,smapGameStage.getsMapDAO(),0);
        loadDefaultTexture();

        // 将输入处理设置到舞台（必须设置, 否则点击按钮没效果）
        multiplexer.addProcessor(smapGameStage);


        progressCountryImage=GameUtil.initImage(progressCountryImage,game,"flag_"+smapGameStage.getPlayerLegion().getCountryId(),100,0,0,0,uiStage.getWidth(),uiStage.getHeight(),true);
        uiStage.addActor(progressCountryImage);
        progressCountryImage.setVisible(false);


        ifLoop= smapGameStage.getIfLoop();
        // i=1;
        //tempX=ui.get(u).getInt("x");tempY=ui.get(u).getInt("y");tempW=ui.get(u).getInt("w");tempH=ui.get(u).getInt("h");
        tempActor=new Actor();
        //遍历window的buttons按钮


        //如果大于1,则初始化另外的window
        if(uiR.getChildCount()>0){
            windowGroups=new Array<>();
            for(int i=0;i<uiR.getChildCount();i++){
                // WindowGroup w=new WindowGroup(game,uiR.getChild(i),uiStage.getWidth(),uiStage.getHeight());//MainGame mainGame, XmlReader.Element groupE,int stageW,int stageH
                Element uiRE=uiR.getChild(i);
                if(uiRE.getBoolean("onlyEditMode",false)){
                    if(smapGameStage.isEditMode(false)){
                        WindowGroup w=WindowGroup.obtain(WindowGroup.class);
                        w.init(game,uiRE,uiStage.getWidth(),uiStage.getHeight(),tempVector2);
                        uiStage.addActor(w);
                        windowGroups.add(w);
                    }else{
                        windowGroups.add(null);
                    }
                }else{
                    WindowGroup w=WindowGroup.obtain(WindowGroup.class);
                    w.init(game,uiRE,uiStage.getWidth(),uiStage.getHeight(),tempVector2);
                    uiStage.addActor(w);
                    windowGroups.add(w);
                }
            }
            function(game.functionMap);
        }

        defaultWindow=  windowGroups.get(defaultId);

        defaultWindow.setVisible(true);
//initTextButton(TextButton button, MainGame game,String imgName,String fontName,int x,int y,int w,int h ,float fontScale, float uiStageWidth, float uiStageHeight,boolean ifWrap,String color, String align,boolean ifBorder,int refX,int refY) {




        initWindowGroup();
        ifInputVisable=true;
        //指令功能监视
        listener= new Input.TextInputListener() {
            @Override
            public void input(String text) {
                instruct=text;
                ifInputVisable=true;
                smapGameStage.getsMapDAO().parseCommand(text);
            }

            @Override
            public void canceled() {
                ifInputVisable=true;
            }
        };

        bmListener= new Input.TextInputListener() {
            @Override
            public void input(String text) {
                ifInputVisable=true;
                if(ComUtil.isNumeric(text)){
                    TextButton tb=windowGroups.get(ResDefaultConfig.Class.SMapScreenEditGroupId).getTButton(bmIndex);
                    if(tb!=null){
                        tb.setText(text);
                        bm.setBmValue(bmIndex+50*(cardNowPage),Integer.parseInt(text));
                    }
                }else {
                    game.sMapScreen.commandStr="Only numbers can be entered";
                }
            }
            @Override
            public void canceled() {
                ifInputVisable=true;
            }
        };

        if(smapGameStage.isEditMode(true)){//编辑器
            initDefaultWindowForResource();
            updResourceForPlayer();
        }else  if(smapGameStage.getPlayerLegionIndex()==0){
            // hiddenWindowGroup(ResConfig.Class.SMapScreenResourceGroupId);
            hidPlayerResource();
            hidRegionResourceInfo();
            hidRightBorderInfo();
        }else if(smapGameStage.getPlayerLegion().getCapitalId()!=-1){
            initDefaultWindowForResource();
            updResourceForPlayer();
            // smapGameStage.cam.asyncMoveCameraToHexagon(29650,1);
        }
      //  game.gameLayout.setDefaultGroupUIColor(defaultWindow,smapGameStage.getPlayerLegion().getColor());
        smapGameStage.initPotion();


        //文字示例
        label=new Label("FPS:"+Gdx.graphics.getFramesPerSecond(),new LabelStyle(game.defaultFont, null));
        label.setWidth(200);//设置每行的宽度
        label.setWrap(true);//开启换行
        label.setPosition(10, 100);
        uiStage.addActor(label);

        if(ResDefaultConfig.ifDebug||smapGameStage.isEditMode(false)){
            label.setVisible(true);
        }else {
            label.setVisible(false);
        }


        promptText =GameUtil.initTextButton(promptText,game,"colorBlock_2","textFont",50,50,0,0,0.5f*game.gameConfig.gameFontScale,uiStage.getWidth(),uiStage.getHeight(),true,"WHITE","center","center",true,0,-50);
        promptText.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //Gdx.app.log("点击了第1个按钮", "x:" + x+" y:" + y);
                promptText.setVisible(false);
            }
        });
        promptText.setVisible(false);
        tempVector2=new Vector2(0,0);
        uiStage.addActor(promptText);

        //smapGameStage.hidAllBuildActor();
        // hidCommonUi();



       /* gameTimeLabel=new Label(smapGameStage.getSMapDAO().getNowYearStr(),new LabelStyle(mainGame.getFont(), null));
        gameTimeLabel.setPosition(GAMESTAGE_WIDTH/2, GAMESTAGE_HEIGHT*0.95f);
        uiStage.addActor(gameTimeLabel);*/





        processorEvent = new InputProcessorEvent();
        mapListener=new MapListener();

        //Gdx.app.log("平台", Gdx.app.getGoodsType().toString());
        switch (Gdx.app.getType()) {
            case Desktop:// Code for Desktop applicationbreak;
                multiplexer.addProcessor(processorEvent);
                break;
            case Android:// Code for Android applicationbreak;
                multiplexer.addProcessor(new GestureDetector(mapListener));
                break;
            case WebGL:// Code for WebGL applicationbreak;
                multiplexer.addProcessor(processorEvent);break;
            default:// Unhandled (new?) platform applicationbreak;
                multiplexer.addProcessor(processorEvent);
                multiplexer.addProcessor(new GestureDetector(mapListener));break;
        }
        Gdx.input.setInputProcessor(multiplexer);





        selectArmyList=new Array<>();
        selectNavyList=new Array<>();
        selectAirList=new Array<>();
        selectNulList=new Array<>();
        haveCardEList=new Array<>();
        selectCardEList=new Array<>();
        //tradeInfoSumPage
        {
            /*tradeInfoSumPage=0;
            for(int i=0,iMax=smapGameStage.getsMapDAO().tradeDatas.size;i<iMax;i++){
                tradeInfoSumPage=tradeInfoSumPage+1;
            }*/
            tradeInfoSumPage= smapGameStage.getsMapDAO().tradeDatas.size/10+1;
            tradeInfoNowPage=1;
        }

        //如果是0则关闭
        if(game.getStageId()==0){
            windowGroups.get(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId).hidButton(4);
        }


        loadScriptFile();
        getScript();
        if(script!=null||smapGameStage.isEditMode(false)){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId);
            w.hidButton(0);
            w.hidButton(1);
        }
       /* if(script!=null||smapGameStage.isEditMode(false)){
            //ImageButton button, MainGame game,int x,int y, int w,int h,int refx,int refy,int blank,String imgUpName,String imgDownName,boolean ifBorder, float uiStageWidth, float uiStageHeight
            scriptButton_c=GameUtil.initImageButton(scriptButton_c,game,100,0,0,0,-150,35,-1,"circleButton_close","circleButton_close",false,uiStage.getWidth(),uiStage.getHeight());
            scriptButton_c.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if(smapGameStage.isEditMode(true)){
                        if(smapGameStage.rescource.drawType<2){//复制兵种
                            smapGameStage.copyArmy=smapGameStage.selectedArmy;
                        }else {//复制归属
                            smapGameStage.copyRegionLi=smapGameStage.selectedRLegionIndex;
                        }
                    }else {
                        game.asyncManager.clearTask();
                        game.showGameScreen(screenId,1);
                    }
                }
            });
            scriptButton_c.setVisible(false);
            uiStage.addActor(scriptButton_c);

            scriptButton_n=GameUtil.initImageButton(scriptButton_n,game,100,0,0,0,-90,35,-1,"circleButton_next","circleButton_next",false,uiStage.getWidth(),uiStage.getHeight());
            scriptButton_n.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if(smapGameStage.isEditMode(true)){
                        if(smapGameStage.rescource.drawType<2){//粘贴兵种,不会复制将军信息
                            smapGameStage.pasteArmy(smapGameStage.coord.getId());
                        }else {//粘贴归属
                            smapGameStage.getsMapDAO().setRegionAllLegionIndex(smapGameStage.selectedRegionId,smapGameStage.copyRegionLi,true);
                        }
                    }else {
                        executeScripts(false);
                    }
                }
            });
            scriptButton_n.setVisible(false);
            uiStage.addActor(scriptButton_n);
        }*/
        //进度按钮
        //Image image, MainGame mainGame, String imgName, float imgX, float imgY, int imgW, int imgH, float uiStageWidth, float uiStageHeight,boolean ifBorder


        smapGameStage.cam.asyncMoveCameraToHexagon(smapGameStage.getPlayerLegion().getCapitalId(),5);
        executeAllScripts(false);

        //executeScripts(false);
        //test
        //testForGeneralDialogueGroup();

        // initTempInput();
        if(smapGameStage.isEditMode(false)){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId);
            w.setButtonImageNotChangeSize(0,game.getImgLists().getTextureByName("circleButton_copy").getTextureRegion());
            w.setButtonImageNotChangeSize(1,game.getImgLists().getTextureByName("circleButton_past").getTextureRegion());
            /*scriptButton_c.setVisible(true);
            TextureRegion region=game.getImgLists().getTextureByName("circleButton_copy").getTextureRegion();
            ((TextureRegionDrawable)scriptButton_c.getStyle().imageUp).setRegion(region);
            ((TextureRegionDrawable)scriptButton_c.getStyle().imageDown).setRegion(region);
            ((TextureRegionDrawable)scriptButton_c.getStyle().imageChecked).setRegion(region);

            scriptButton_n.setVisible(true);
             region=game.getImgLists().getTextureByName("circleButton_past").getTextureRegion();
            ((TextureRegionDrawable)scriptButton_n.getStyle().imageUp).setRegion(region);
            ((TextureRegionDrawable)scriptButton_n.getStyle().imageDown).setRegion(region);
            ((TextureRegionDrawable)scriptButton_n.getStyle().imageChecked).setRegion(region);*/
        }
        updButtonPrompt();
    }



    private void hidPlayerResource() {
        for(int i=0;i<=4;i++){
            defaultWindow.hidImage(i);
        }
        for(int i=0;i<=24;i++){
            defaultWindow.hidButton(i);
        }
        for(int i=0;i<=6;i++){
            defaultWindow.hidLabel(i);
        }
    }
    private void showPlayerResource() {
        for(int i=0;i<=4;i++){
            defaultWindow.showImage(i);
        }
        for(int i=0;i<=24;i++){
            defaultWindow.showButton(i);
        }
        for(int i=0;i<=6;i++){
            defaultWindow.showLabel(i);
        }
    }

    private void hidRegionResourceInfo() {
        defaultWindow.hidImage(1000);
        defaultWindow.hidImage(1001);
        defaultWindow.hidImage(1002);
        defaultWindow.hidImage(1015);
        for(int i=1000;i<=1001;i++){
            defaultWindow.hidButton(i);
        }
        for(int i=1000;i<=1013;i++){
            defaultWindow.hidLabel(i);
        }
    }

    private void showRegionResourceInfo() {
        defaultWindow.showImage(1000);
        defaultWindow.showImage(1001);
        defaultWindow.showImage(1002);
        defaultWindow.showImage(1015);
        for(int i=1000;i<=1001;i++){
            defaultWindow.showButton(i);
        }
        for(int i=1000;i<=1013;i++){
            defaultWindow.showLabel(i);
        }
    }

    public void hidRightBorderInfo(){
        if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
            defaultWindow.showButton(14);
        }else {
            defaultWindow.hidButton(14);
        }
        if(smapGameStage.getsMapDAO().masterData.getPlayerLegionIndex()==0){
            defaultWindow.hidButton(14);
        }
        for(int i=2000;i<=2036;i++){
            defaultWindow.hidImage(i);
        }
        for(int i=2000;i<=2006;i++){
            defaultWindow.hidButton(i);
        }
        for(int i=3001;i<=3008;i++){
            defaultWindow.hidButton(i);
        }
        for(int i=3001;i<=3004;i++){
            defaultWindow.hidLabel(i);
        }
    }
    private void showRightBorderInfo(){
        defaultWindow.hidButton(14);
        for(int i=2000;i<=2036;i++){
            defaultWindow.showImage(i);
        }
        for(int i=2000;i<=2006;i++){
            defaultWindow.showButton(i);
        }
    }


   /* private void initTempInput(){
        tempInput=new InputEvent();
        tempInput.setStage(uiStage);
        tempInput.setType(InputEvent.Type.touchDown);
        tempInput.setBubbles(false);
    }*/

    //加载默认的资源
    private void loadDefaultTexture() {
        if(smapGameStage.getsMapDAO().generalIDatas.size>0){
            if(game.tempTextureRegions.containsKey(smapGameStage.getsMapDAO().generalIDatas.get(0).getGeneralImageName())){
                getGeneralTextureRegion(smapGameStage.getsMapDAO().generalIDatas.get(0));
            }
        }

        //加载玩家头像
        if(smapGameStage.getsMapDAO().masterData.getBtlType()==1){
            game.getGeneralTextureRegion(0, ResDefaultConfig.Image.GENERAL_IMAGE_PLAYER);
        }
    }

    /*private void testForGeneralDialogueGroup() {
        hidCommonUi();hidUi(false);
        WindowGroup w=windowGroups.get(ResConfig.Class.SMapScreenGeneralDialogueGroupId);
        w.setVisible(true);
        w.setImageRegion(1,game.getFlagBgTextureRegion(0));
        w.setImageRegionNotChangeSize(2,game.getGeneralTextureRegion(0,"player"));
        w.setLabelText(0,game.gameMethod.getStrValue("generalName_general"));

        w.setLabelText(1,"普鲁士已经打败了奥地利,现在正在整合其他德意志王国资源.我们无法容忍一个统一的德意志王国在我们旁边崛起.皇帝已经集结了军队,准备干涉普鲁士人的扩张.指挥官阁下,虽然我们拥有欧洲最强的军队,但普鲁士人的意志非常坚决,这会是一场困难的战斗.皇帝已经集结了军队,准备干涉普鲁士人的扩张.指挥官阁下,虽然我们拥有欧洲最强的军队,但普鲁士人的意志非常坚决,这会是一场困难的战斗",true);

    }*/

    @Override
    public void render(float delta) {
        try {

            //当有4秒没有操作时,重新计算缩放
          /*  if(oldCurrentDistance!=0){
                oldCurrentDistanceT=oldCurrentDistance;
                oldCurrentDistanceT+=delta;
                if(oldCurrentDistanceT >2){
                    oldCurrentDistanceT =0;
                    oldCurrentDistance=0;
                }
            }*/



            handleInput();

            try {
                game.batch.begin();
            } catch (IllegalStateException e) {
                game.batch.end();
                game.batch.begin();
            }


            smapGameStage.act();
            smapGameStage.draw();
            /* 暂停时高斯模糊背景 暂时不知道解决方案
             */
           /*if(ifWarp){
                warpUtils.blur( game.batch,smapGameStage);
            }else {

            }*/
            smapEffectStage.act();
            smapEffectStage.draw();

            uiStage.act();
            uiStage.draw();
            //Gdx.app.log("FPS:", Gdx.graphics.getFramesPerSecond().toString);
            /**/if( !game.asyncManager.update() ){
                //defaultWindow.setImageRegionWidth(3,game.progressbarWidth*game.asyncManager.getProgress(),1f);
            }

            if(ResDefaultConfig.ifDebug||smapGameStage.isEditMode(false)){
                javaHeap=(Math.round(Gdx.app.getJavaHeap()/1024/1024*10)/10);
                if(javaHeap>javaMaxHeap) {
                    javaMaxHeap=javaHeap;
                }

                label.setText("FPS:"+Gdx.graphics.getFramesPerSecond());
                label.getText().appendLine("");
                label.getText().appendLine("javaHeap:"+javaHeap+"m/"+javaMaxHeap+"m");
                label.getText().appendLine("drawCalls:"+gp.getDrawCalls()+" "+(smapGameStage.cam.cw+2)+" "+(smapGameStage.cam.ch+3)+" "+((smapGameStage.cam.cw+2)*(smapGameStage.cam.ch+3)));
                label.getText().appendLine("nativeHeap:"+Math.round(Gdx.app.getNativeHeap()/1024/1024*10)/10+"m");
                if(!game.saveOk){
                    label.getText().appendLine("game saving...");
                }
                label.getText().appendLine(selectStr);
                label.getText().appendLine(commandStr);
                gp.reset();
            }

            /**/
            //gameTimeLabel.setText(smapGameStage.getSMapDAO().getNowYearStr());

            game.batch.end();

            if(ifEnd){
                endDeltaSum-=delta;
                game.asyncManager.clearTask();
                if(endDeltaSum<0){
                  /*  if(result>0&&smapGameStage.getsMapDAO().masterData.getBtlType()==1&&smapGameStage.getsMapDAO().nextStageForEmpire()){
                        //帝国模式需要额外保存一份
                        game.save(true);
                        ifSave=true;
                        game.showGameScreen(screenId,1);
                    }else{
                        game.showGameScreen(screenId,1);
                    }*/
                    if(result>0&&smapGameStage.getsMapDAO().masterData.getBtlType()==1){
                        //帝国模式需要额外保存一份
                        if(saveState==1){ //-1未保存 0保存中 1保存结束
                            game.showGameScreen(screenId,1);
                        }else if(saveState==-1){
                            saveState=0;
                            asyncNextEmpireAndSave();
                        }
                    }else{
                        game.showGameScreen(screenId,1);
                    }
                }
            }

            if(smapGameStage!=null){
                if(smapGameStage.getsMapDAO().roundState==-1){
                    resetPlayerStar();
                }else if(smapGameStage.getsMapDAO().roundState!=1){
                    if(smapGameStage.getsMapDAO().roundCountryIndex==-1){
                        progressCountryImage.setVisible(false);
                        smapGameStage.getsMapDAO().roundCountryIndex=smapGameStage.getPlayerLegion().getCountryId();
                        nextRoundEnd();
                    }else if(smapGameStage.getsMapDAO().roundCountryIndex!=lastRoundCountryId){
                        TextureRegionDAO t=game.getImgLists().getTextureByName(DefDAO.getProgressMark(smapGameStage.getsMapDAO().roundCountryIndex));
                        if(t!=null){
                            lastRoundCountryId=smapGameStage.getsMapDAO().roundCountryIndex;
                            ((TextureRegionDrawable)  progressCountryImage.getDrawable()).setRegion(t.getTextureRegion());
                            progressCountryImage.setVisible(true);
                        }
                    }else if(ifSciptEnd()&&smapGameStage.getsMapDAO().roundState==0&&!ifHaveGroupOpen()){//如果在剧本结束的情况下主界面一个都没有打开的且不是执行状态
                            showLegionTechUI(smapGameStage.getPlayerLegion());
                    }else if(smapGameStage.getsMapDAO().roundState==4&&!progressCountryImage.isVisible()){
                            resetPlayerStar();
                    }
                }
            }


        } catch (Exception e) {

            if(ResDefaultConfig.ifDebug){
                e.printStackTrace();
            }else if(!game.gameConfig.getIfIgnoreBug()){
                game.remindBugFeedBack();
            }
            game.recordLog("SMapScreen render ",e);
            resetPlayerStar();
            showLegionTechUI(smapGameStage.getPlayerLegion());
            showSaveUI();
        }
    }

    private void asyncNextEmpireAndSave() {
        final CHAsyncTask task = new CHAsyncTask("save",999) {
         //   final   Fb2Smap fb2Smap=game.getSMapDAO();
           // int stageId=game.getStageId();
            @Override
            public void onPreExecute() {
            }

            @Override
            public void onPostExecute(String result) {
            }
            @Override
            public String doInBackground() {
                if( smapGameStage.getsMapDAO().nextStageForEmpire()){
                    game.saveConfig(game.getSMapDAO());
                    game.getSMapDAO().saveSmapBin(game.getStageId(),1);
                }
                ifEnd=true;
                saveState=1;
                return null;
            }
        };
        game.asyncManager.loadTask(task);
        game.asyncManager.update();

    }


    public void resetPlayerStar(){
        progressCountryImage.setVisible(false);
        smapGameStage.getsMapDAO().roundState=0;
        smapGameStage.getsMapDAO().roundCountryIndex=smapGameStage.getPlayerLegion().getCountryId();
        adviceIndex=0;
        updButtonPrompt();
        smapGameStage.updAllBuildActor();
        smapGameStage.updAllArmyActor();
        smapGameStage.updClickActor();
        updResourceForPlayer();
        smapGameStage.setIfFixed(false);
        defaultWindow.setVisible(true);
    }

    public void dispose() {
        super.dispose();
        for(WindowGroup w:windowGroups){
            w.remove();
            w=null;
        }
        //清除全部将军图集资源
       // game.clearAllTempTextureRegions();
        // 场景被销毁时释放资源
        if (uiStage != null) {
            uiStage.dispose();
            uiStage=null;
        }
        if (smapGameStage != null) {
            smapGameStage.dispose();
            smapGameStage =null;
        }
        if(priviewMapPixmap !=null){
            priviewMapPixmap.dispose();
            priviewMapPixmap =null;
        }
        if(politicalMapPixmap!=null){
            politicalMapPixmap.dispose();
            politicalMapPixmap =null;
        }
        if(priviewMapTexture !=null){
            priviewMapTexture.dispose();
            priviewMapTexture =null;
        }
        if(politicalMapTexture!=null){
            politicalMapTexture.dispose();
            politicalMapTexture =null;
        }

        /*if(warpUtils!=null){
            warpUtils.dispose();
            warpUtils=null;
        }*/

    }



    //如果传null,则返回指挥官
    public TextureRegion getGeneralTextureRegion(Fb2Smap.GeneralData g) {
        if(g.isPlayer()&&(g.getState()==1||(g.gE!=null&&g.gE.getInt("recruitType",0)==1))){
            return  game.getGeneralTextureRegion(0, ResDefaultConfig.Image.GENERAL_IMAGE_PLAYER);
        }
        if(g.getGeneralId()==0||(g.gE!=null&&g.gE.getInt("recruitType",0)==1)){
            return game.getImgLists().getTextureByName("general_0").getTextureRegion();
        }
        return game.getGeneralTextureRegion(g.getState(),g.getGeneralImageName());
    }
    public void function(ObjectMap<Button, Element> map){
        for (Button b: map.keys()) {
            Element xmlE=map.get(b);
            try {
                function(b,xmlE.getInt("functionId"),xmlE.getInt("value"),xmlE.get("sound","click"));
            }catch (Exception e){
                if(ResDefaultConfig.ifDebug){
                    e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                }else if(!game.gameConfig.getIfIgnoreBug()){
                    game.remindBugFeedBack();
                }
                game.recordLog("smapscreen function ",e);
            }

        }
    }


    //实现的功能
    public void function(final Button button, int functionId, final int value, final String sound) throws  Exception{

        //int i=map.get(ResConfig.StringName.functionId);
        switch(functionId) {
            case -1: if(ifBanOperation){return;}//通用关闭按钮
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        hidUi(true);
                    }
                });
                break;
            case 0://基础布局按键
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        if(ifBanOperation){return;}
                        game.playSound(sound);
                        switch (value){
                            case 0:
                                showPauseGroup();ifWarp=true;
                                break;
                            case 1:
                                //0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡
                                showCardUi(11);ifWarp=true;
                                break;
                            case 2:
                                if(smapGameStage.isEditMode(true)){
                                    smapGameStage.getsMapDAO().transBtlModuleByMasterData(smapGameStage.getsMapDAO().masterData,bm);
                                    cardNowPage=0;
                                    showEditGroup();
                                }else {
                                    next(false);
                                }
                                break;
                            case 3:
                                //0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡
                                showCardUi(9);ifWarp=true;
                                break;
                            case 4: //0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡
                                showCardUi(10);ifWarp=true;
                                break;
                            case 5:
                                //0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡 -2全部可招募单位
                                showCardUi(-2);ifWarp=true;
                                break;
                            case 6: //0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡
                                if(smapGameStage.isEditMode(true)){
                                    smapGameStage.getsMapDAO().transBtlModuleByBuildData(smapGameStage.selectedBuildData,bm);
                                    cardNowPage=0;
                                    showEditGroup();
                                }else {
                                    showCardUi(13);
                                    ifWarp = true;
                                    if (smapGameStage.selectedBuildData != null) {
                                        selectedCard(smapGameStage.selectedBuildData.getBuildPolicy() + 1);
                                    }
                                }
                                break;
                            case 7:
                                if(smapGameStage.isEditMode(true)){
                                    smapGameStage.getsMapDAO().transBtlModuleByLegionData(smapGameStage.getSelectLegionData(),bm);
                                    cardNowPage=0;
                                    showEditGroup();
                                }else{
                                    showLegionTechUI(smapGameStage.getsMapDAO().getPlayerLegionData());ifWarp=true;
                                }
                                break;
                            case 8:
                                showCardUi(12);ifWarp=true;
                                break;
                            case 9:ifWarp=true;
                                if(smapGameStage.getsMapDAO().ifSystemEffective(16)&&smapGameStage.getsMapDAO().ifAllyPlayerByLi(smapGameStage.selectedRLegionIndex)&&smapGameStage.selectedBuildData.getBuildRound()==0){
                                    //可以在盟国的区域上为其援建民用设施
                                    if(smapGameStage.getsMapDAO().ifHaveSpirit(6)){
                                        showCardUi(9);
                                    }else if(smapGameStage.getsMapDAO().ifHaveSpirit(14)&&smapGameStage.selectedBuildData.isCapital()){//军用设施
                                        showCardUi(1);
                                    }else{
                                        showLegionTechUI(smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex));
                                    }
                                    /*int ambition=smapGameStage.getsMapDAO().getPlayerAmbition();
                                    if(ambition<100){//王权
                                        showCardUi(9);
                                    }else if(ambition>100&&smapGameStage.selectedBuildData.isCapital()){//霸权
                                        showCardUi(1);
                                    }else {//王权
                                        showTechUI(smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex));
                                    }*/
                                   // showTechUI(smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex));
                                }else{
                                    showLegionTechUI(smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex));
                                }
                                break;
                            case 10:
                                if(smapGameStage.isEditMode(false)){
                                    showCountryToSelected();
                                }else if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0&&smapGameStage.getsMapDAO().masterData.getBtlType()==0){
                                    selectTextGroupType=0;
                                    selectListPage=1;
                                    showAchievementGroupId();ifWarp=true;
                                }
                                break;
                            case 11://详情
                                showClickInfo();
                                break;

                            case 12:
                                //0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡
                                showCardUi(14);ifWarp=true;
                                break;
                            case 13:
                                if(smapGameStage.gameOperate==GameOperateState.selectMultipleUnitToLegionAct){//军团行动
                                    smapGameStage.legionAct();
                                }else if(smapGameStage.gameOperate==GameOperateState.selectMultipleUnitToLegionTarget){//军团行动
                                smapGameStage.legionSetTarget();
                                }else if(smapGameStage.selectedArmy!=null&&smapGameStage.selectedArmy.isPlayer()&&smapGameStage.selectedArmy.ifCanTransport()&&smapGameStage.selectedBuildData!=null&&smapGameStage.selectedBuildData.isPlayer()&&smapGameStage.getsMapDAO().ifSystemEffective(8)&&smapGameStage.selectedBuildData.haveTactics(2011)&&smapGameStage.selectedBuildData.getBuildRound()==0){
                                    //战术空降
                                    smapGameStage.selectedHexagonIds =smapGameStage.selectedBuildData.getAirborneHexagons(smapGameStage.selectedHexagonIds,0);
                                    smapGameStage.smapFv.setDrawMarkForAir(1505,smapGameStage.selectedBuildData.getRegionId(),smapGameStage.selectedHexagonIds);
                                    smapGameStage.gameOperate= selectHexagonToAirborneTactical;

                                } else if(smapGameStage.selectedArmy!=null&&smapGameStage.selectedBuildData!=null&&smapGameStage.selectedArmy.ifCanTransport()){//
                                    //运输机空降
                                    Fb2Smap.AirData transportAir=smapGameStage.selectedBuildData.getReadyAir(30,smapGameStage.selectedArmy.getHexagonIndex());
                                    if(transportAir!=null){
                                        smapGameStage.selectedHexagonIds =transportAir.getCanActRegions(smapGameStage.selectedHexagonIds,2);
                                        smapGameStage.smapFv.setDrawMarkForAir(1505,transportAir.getRegionId(),smapGameStage.selectedHexagonIds);
                                        smapGameStage.selectedAir=transportAir;
                                        smapGameStage.gameOperate= selectHexagonToAirborne;
                                    }
                                }else if(smapGameStage.selectedArmy!=null&&smapGameStage.selectedArmy.isPlayer()&&smapGameStage.selectedArmy.canDeloyTrap()){//布雷
                                    smapGameStage.getsMapDAO().fort_build(5005,smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getArmyRank());
                                    smapGameStage.getsMapDAO().payCardPrice(smapGameStage.getPlayerLegion(),smapGameStage.selectedBuildData,5005,true);

                                    smapGameStage.resetDefaultState();
                                }
                                else if(smapGameStage.gameOperate==GameOperateState.selectMultipleUnitToLegionTarget){//军团目标
                                    smapGameStage.setLegionTarget();
                                }else if(smapGameStage.getsMapDAO().getLastUnitHexagon()!=-1){
                                    smapGameStage.withdrawUnitMove();
                                }  break;
                            case 14:
                                helpPrompt();ifWarp=true;
                                break;
                            case 15:selectTextGroupType=0;
                                showTextWindow();ifWarp=true;
                                break;
                            case 16:
                                showTradeInfoUI( tradeInfoNowPage);ifWarp=true;
                                break;
                            case 17:
                                showIntroductionGroup(true);ifWarp=true;
                                break;
                            case 18:
                                if(smapGameStage.getsMapDAO().masterData.getBtlType()==0&&smapGameStage.getsMapDAO().masterData.getPlayerMode()!=0&&smapGameStage.getsMapDAO().stageId!=0){
                                    selectTextGroupType=3;
                                }else{
                                    selectTextGroupType=1;
                                }
                                showTextWindow();ifWarp=true;
                                break;
                            case 19: smapGameStage.selectedNextUnit();
                                break;
                            case 20:
                                if (ifInputVisable) {
                                    ifInputVisable=false;
                                    Gdx.input.getTextInput(listener, "Console", instruct,"");
                                }
                                break;
                            case 21:
                                /*showCardUi(16);
                                if(smapGameStage.selectedBuildData!=null){
                                    selectedCard(smapGameStage.getsMapDAO().masterData.getForeignPolicy()+1);
                                }*/
                                showChiefGroup();
                                break;

                            case 22://放大
                                smapGameStage.cam.setZoomChange(-0.5f);
                                break;

                            case 23://缩小
                                smapGameStage.cam.setZoomChange(0.5f);
                                break;

                            case 24://设置行动
                                if(smapGameStage.getPlayerLegion().getMoney()==0){
                                    if(game.gameConfig.ifAiActAndNextRound&&smapGameStage.getsMapDAO().roundState==0){
                                        next(true);
                                    }
                                    return;
                                }
                                if(ifRoundInEnd){
                                    game.asyncManager.clearTask();
                                    ifRoundInEnd=false;
                                    resetPlayerStar();
                                    return;
                                }
                                if(smapGameStage.getsMapDAO().roundState!=0){
                                    return;
                                }
                                smapGameStage.getsMapDAO().roundState=3;
                               /* if(defaultWindow.getButton(24).isVisible()){
                                    defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai").getTextureRegion());
                                }*/
                                smapGameStage.getsMapDAO().resetPLegionRank();
                                smapGameStage.getsMapDAO().nextRoundAct(smapGameStage.getPlayerLegion(),true);
                                smapGameStage.getsMapDAO().roundState=0;
                                updResourceForPlayer();
                                //如果是测试状态,则直接下一回合
                                if(game.gameConfig.ifAiActAndNextRound){
                                    next(true);
                                }
                                /*if(defaultWindow.getButton(24).isVisible()){
                                    defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai2").getTextureRegion());
                                }*/
                                break;
                        }
                    }
                });
                break;

            case 1001:// text 点击提示
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        if(value==-1||promptText.isVisible()){
                            promptText.setVisible(false);
                        }else{
                            if(smapGameStage.isEditMode(true)&&(value==28||value==27)){
                                if(value==28){//兵种
                                    if(selectListType==3){//飞机
                                        Fb2Smap.AirData air=selectAirList.get((selectListPage-1)*4+selectListIndex-1);
                                        smapGameStage.getsMapDAO().transBtlModuleByAirData(air,bm);
                                    }else if(selectListType==4){//弹头
                                        Fb2Smap.NulcleData nulData=selectNulList.get((selectListPage-1)*4+selectListIndex-1);
                                        smapGameStage.getsMapDAO().transBtlModuleByNulcleData(nulData,bm);
                                    }else{
                                        Fb2Smap.ArmyData armyData=smapGameStage.selectedArmy;
                                        smapGameStage.getsMapDAO().transBtlModuleByArmyData(armyData,bm);
                                    }
                                }else {//将军
                                    if(selectListType==3){//飞机
                                        Fb2Smap.AirData air=selectAirList.get((selectListPage-1)*4+selectListIndex-1);
                                        if(air!=null){
                                            if(air.getGeneralIndex()==0){
                                                air.updForGeneral();
                                                return;
                                            }
                                            smapGameStage.getsMapDAO().transBtlModuleByGeneralData(air.getGeneralData(),bm);
                                        }
                                    }else if(selectListType==4){
                                        setPromptText(value);
                                        return;
                                    }else{
                                        Fb2Smap.ArmyData armyData=smapGameStage.selectedArmy;
                                        if(armyData!=null){
                                            if(armyData.getGeneralIndex()==0){
                                                armyData.updForGeneral();
                                                armyData.armyActor.update();
                                                return;
                                            }
                                            smapGameStage.getsMapDAO().transBtlModuleByGeneralData(armyData.getGeneralData(),bm);
                                        }
                                    }
                                }
                                cardNowPage=0;
                                showEditGroup();
                            }else {
                                setPromptText(value);
                            }
                        }
                    }
                });
                break;
            case 1002://点击单位,右边栏
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
                           if (value==1&&game.getSMapDAO().masterData.getPlayerMode()==2&&smapGameStage.selectedBuildData!=null&&smapGameStage.selectedArmy!=null&&((smapGameStage.selectedBuildData.isPlayer()&&smapGameStage.selectedBuildData.getBuildRound()==0)||smapGameStage.selectedBuildData.isEditMode(true))&&smapGameStage.selectedArmy.getArmyType()!=6&&smapGameStage.selectedArmy.getArmyType()!=7&&smapGameStage.selectedArmy.getGeneralIndex()==0&&smapGameStage.getPlayerLegion().ifCanRecruitGeneral()){
                                int armyType=smapGameStage.selectedArmy.getArmyType();//armyType  1步兵2炮兵3坦克4海军5空军  0不限制 -1陆军 步炮坦
                                if(armyType==1||armyType==2||armyType==3){
                                    armyType=-1;
                                }else if(armyType==4||armyType==8){
                                    armyType=4;
                                }else if(armyType==5){
                                    armyType=5;
                                }else{
                                    armyType=0;
                                }
                                showCanRecruitGeneralGroup(smapGameStage.selectedArmy.getHexagonIndex(),armyType);
                            }
                            return;
                        }
                        game.playSound(sound);

                        if(value==10){
                            updRightBorderGroupType();
                        }else if(value==11){//上一页
                            updRightBorderForLastPage();
                        }else if(value==12){//下一页
                            updRightBorderForNextPage();
                        }else {
                            //点击右边栏卡牌
                            clickRightBorderCard(value);
                        }
                    }
                });
                break;

            case 1003://上将
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        switch (value){
                            case 0://显示将领或为其配置将领
                                if(selectListType==3){//选择的是飞机
                                    Fb2Smap.AirData airData=smapGameStage.selectedAir;
                                    if(airData!=null){
                                        if((smapGameStage.isEditMode(true)||airData.isPlayer())&&airData.canRecruitGeneral()){
                                            if(!game.sMapScreen.showCanRecruitGeneralGroup(smapGameStage.selectedAir.getAirIndex(),5)){
                                                airData.updForGeneral();
                                                if(!smapGameStage.isEditMode(true)){
                                                    smapGameStage.selectedBuildData.addBuildRound(1);
                                                }
                                                hidUi(false);
                                            }
                                        }else if(smapGameStage.isEditMode(true)&&airData.getGeneralIndex()!=0){
                                            smapGameStage.getsMapDAO().transBtlModuleByGeneralData(airData.getGeneralData(),bm);
                                            cardNowPage=0;
                                            showEditGroup();
                                        }else {
                                            setPromptText(27);
                                        }
                                    }
                                }else {//选择的是单位
                                    Fb2Smap.ArmyData armyData=smapGameStage.selectedArmy;
                                    if(armyData!=null){
                                        if((smapGameStage.isEditMode(true)||armyData.isPlayer())&&armyData.canRecruitGeneral()){
                                            if(!game.sMapScreen.showCanRecruitGeneralGroup(armyData.getHexagonIndex(),armyData.potionIsSea()?4:-1)){
                                                armyData.updForGeneral();
                                                if(!smapGameStage.isEditMode(true)){
                                                    smapGameStage.selectedBuildData.addBuildRound(1);
                                                }
                                                hidUi(false);
                                            }
                                        }else if(smapGameStage.isEditMode(true)&&armyData.getGeneralIndex()!=0){
                                            smapGameStage.getsMapDAO().transBtlModuleByGeneralData(armyData.getGeneralData(),bm);
                                            cardNowPage=0;
                                            showEditGroup();
                                        }else {
                                            setPromptText(27);
                                        }
                                    }
                                }

                                break;





                        }
                    }
                });
                break;






            case 1://gamecardGroup  点击显示的卡牌
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        switch (value){
                            case 0:  buyCardReady(); break;
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:selectedCard(value); break;
                            case 8:
                           /* if(selectCardType==16){
                              WindowGroup w=  windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId);
                                smapGameStage.getsMapDAO().masterData.setBf20( smapGameStage.getsMapDAO().masterData.getBf20()-5);
                                w.setImageRegionWidth(10,w.getImage(9).getWidth()/100*smapGameStage.getsMapDAO().masterData.getBf20(),1f);
                                w.setLabelText(0,ComUtil.getSpace(18)+smapGameStage.getsMapDAO().masterData.getBf20()+"%");
                            }else */{
                                if(cardNowPage>1){
                                    cardNowPage=cardNowPage-1;
                                }else {
                                    cardNowPage=cardSumPage;
                                }
                                showCardUi( selectCardType);
                            }
                            break;
                            case 9:
                           /* if(selectCardType==16){
                                WindowGroup w=  windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId);
                                smapGameStage.getsMapDAO().masterData.setBf20( smapGameStage.getsMapDAO().masterData.getBf20()+5);
                                w.setImageRegionWidth(10,w.getImage(9).getWidth()/100*smapGameStage.getsMapDAO().masterData.getBf20(),1f);
                                w.setLabelText(0,ComUtil.getSpace(18)+smapGameStage.getsMapDAO().masterData.getBf20()+"%");
                            }else*/ {
                                if(cardNowPage==cardSumPage){
                                    cardNowPage=1;
                                }else {
                                    cardNowPage=cardNowPage+1;
                                }
                                showCardUi( selectCardType);
                            }
                            break;
                            case 11: showCardUi(1);
                                break;
                            case 12: showCardUi(2);
                                break;
                            case 13: showCardUi(3);
                                break;
                            case 14: showCardUi(4);
                                break;
                            case 15: showCardUi(5);
                                break;
                            case 16: showCardUi(6);
                                break;
                            case 17: showCardUi(7);
                                break;
                            case 18://-1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标
                                if(selectCardType==9){//如果是民用建筑,切换到地标
                                    showCardUi(17);
                                }else if(selectCardType==17){//如果是地标,切换到民用建筑
                                    showCardUi(9);
                                }else if(selectCardType==13){//如果是城市方针,切换到空军方针
                                    showCardUi(16);
                                    if(smapGameStage.selectedBuildData!=null){
                                        selectedCard(smapGameStage.selectedBuildData.getAirforcePolicy()+1);
                                    }
                                }else if(selectCardType==16){//如果是空军方针,切换到城市方针
                                    showCardUi(13);
                                    if(smapGameStage.selectedBuildData!=null){
                                        selectedCard(smapGameStage.selectedBuildData.getBuildPolicy()+1);
                                    }
                                }else{
                                    showCardUi(8);
                                }
                                break;
                            case 19://编辑功能
                                //-1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标
                                if(selectCardType==12){
                                    smapGameStage.getsMapDAO().transBtlModuleByForeignData(smapGameStage.getSelectForeignData(),bm);
                                    cardNowPage=0;
                                    showEditGroup();
                                }
                                break;
                        }


                    }
                });
                break;

            case 2:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        //Gdx.app.log("点击了第6个按钮", "x:" + x+" y:" + y);
                        if(value==1){
                            showNextPrompt(true);
                        }else if(value==2){
                            showNextPrompt(false);
                        }
                        // showGeneralDialogueGroup();
                    }
                });
                break;
            case 4:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        if(value==0){
                            smapGameStage.hidArrow();
                            useTacticsReady();
                        }else{
                            game.gameLayout.updRegionBuildInfo(windowGroups.get(ResDefaultConfig.Class.SMapScreenRegionBuildGroupId),smapGameStage.getsMapDAO(), smapGameStage.getSelectBuildData(),value);
                            selectCardId = value - 1000;
                            selectCardType=0;
                        }
                    }
                });
                break;
            case 5:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation||(!ifClickPlayerUnit()&&value!=5)){return;}
                        game.playSound(sound);
                        boolean ifReset=false;
                        switch (value){
                            case 1:
                                //    armyDissolve();
                                if(smapGameStage.selectedAir!=null){
                                    if(smapGameStage.selectedAir.getGeneralIndex()==0){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81,0),game.gameMethod.getPromptStrT(81,1),"",22,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),smapGameStage.selectedBuildData.getRegionId(),true);
                                        ifReset=true;
                                    }
                                }else if(smapGameStage.selectedArmy!=null){
                                    if(smapGameStage.selectedArmy.getGeneralIndex()==0){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81,0),game.gameMethod.getPromptStrT(81,1),"",23,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.selectedBuildData.getRegionId(),true);
                                        ifReset=true;
                                    }
                                }
                                //  Gdx.app.log("兵种界面","解散");
                                break;
                            case 2://升级武器
                                if(smapGameStage.selectedBuildData.isPlayer()&&smapGameStage.selectedBuildData.getBuildRound()==0){
                                    if(smapGameStage.selectedAir!=null){
                                        if(smapGameStage.selectedAir.getWeaLv()<smapGameStage.selectedBuildData.getArmyLvNow()&&smapGameStage.selectedAir.getWeaLv()<smapGameStage.selectedAir.getUnitTechLv()){
                                            smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedAir.getUnitAbility(0),smapGameStage.selectedAir.getUnitAbility(0)+1),"",24,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),smapGameStage.selectedBuildData.getRegionId(),true);
                                            ifReset=true;
                                        }
                                    }else if(smapGameStage.selectedArmy!=null){
                                        if(smapGameStage.selectedArmy.getUnitWealv0Value()<smapGameStage.selectedBuildData.getArmyLvNow()&&smapGameStage.selectedArmy.getUnitWealv0Value()<smapGameStage.selectedArmy.getUnitTechLv(0)){
                                            smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedArmy.getUnitWealv0Value(),smapGameStage.selectedArmy.getUnitWealv0Value()+1),"",25,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.selectedBuildData.getRegionId(),true);
                                            ifReset=true;
                                        }
                                    }
                                }
                                // Gdx.app.log("兵种界面","升级武器");
                                break;
                            case 3://上将 配置将领
                                if(smapGameStage.selectedBuildData.isPlayer()&&smapGameStage.selectedBuildData.getBuildRound()==0&&smapGameStage.getPlayerLegion().ifCanRecruitGeneral()){
                                    if(smapGameStage.selectedAir!=null){
                                        if(smapGameStage.selectedAir.getGeneralIndex()==0){
                                            if( smapGameStage.getPlayerLegion().ifHaveCanRecruitGeneral(5,game.resGameConfig.ifCanRecruitFriendlyCountryGeneral)){
                                                game.sMapScreen.showCanRecruitGeneralGroup(smapGameStage.selectedAir.getAirIndex(),5);
                                            }else{
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(83,0),game.gameMethod.getPromptStrT(83,1),"",26,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),smapGameStage.selectedBuildData.getRegionId(),true);
                                                ifReset=true;
                                            }

                                        }
                                    }else if(smapGameStage.selectedArmy!=null&&smapGameStage.selectedArmy.getArmyType()!=6){
                                        if(smapGameStage.selectedArmy.getGeneralIndex()==0){
                                            if(smapGameStage.getPlayerLegion().ifHaveCanRecruitGeneral(-1,game.resGameConfig.ifCanRecruitFriendlyCountryGeneral)){
                                                game.sMapScreen.showCanRecruitGeneralGroup(smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.selectedArmy.potionIsSea()?4:-1);
                                            }else{
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(83,0),game.gameMethod.getPromptStrT(83,1),"",27,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.selectedBuildData.getRegionId(),true);
                                                ifReset=true;
                                            }
                                        }
                                    }
                                }
                                //    Gdx.app.log("兵种界面","上将");
                                break;
                            case 4://加载弹头
                                hidUi(false);
                                unitLoadNul();
                                Gdx.app.log("兵种界面","加载弹头");
                                break; /**/
                            case 5://切换显示技能
                                if(cardNowPage==0){
                                    cardNowPage=1;
                                }else {
                                    cardNowPage=0;
                                }
                                if(smapGameStage.selectedAir!=null){
                                    game.gameLayout.updAirInfoWindowForFeature(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId),smapGameStage.selectedAir,cardNowPage);
                                }else if(smapGameStage.selectedArmy!=null){
                                    game.gameLayout.updArmyInfoWindowForFeature(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId),smapGameStage.selectedArmy,cardNowPage);
                                }
                                break;
                            case 6://切换标签 切到阵型界面
                                windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId).setVisible(false);
                                showArmyFormationWindow(smapGameStage.selectedArmy);
                                break;
                        }
                        if(ifReset){
                            smapGameStage.updClick();
                            hidRightBorderInfo();
                        }
                    }
                });
                break;
            case 6://成就系统 与 奇物系统的展示
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        if(selectTextGroupType==0){
                            if(value==0){
                                selectTextGroupType=1;
                                showAchievementGroupId();ifWarp=true;
                            }else{//奇物说明
                                game.gameLayout.updAchievementGroupForSpiritInfo(windowGroups.get(ResDefaultConfig.Class.SMapScreenAchievementGroupId),smapGameStage.getsMapDAO(),selectListPage,value);
                            }
                        }else if(selectTextGroupType==1){
                            if(value==0){
                                selectTextGroupType=0;
                                selectListPage=1;
                                showAchievementGroupId();ifWarp=true;
                            }
                        }else if(selectTextGroupType==2||selectTextGroupType==3){//将领说明
                            selectListIndex=value;
                            game.gameLayout.updAchievementGroupForGeneralInfo(windowGroups.get(ResDefaultConfig.Class.SMapScreenAchievementGroupId),smapGameStage.getsMapDAO(),selectCardEList,value);
                        }
                    }
                });
                break;
            case 601:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        if (ifBanOperation) { return;}
                        game.playSound(sound);

                        if(selectTextGroupType==0){//奇物
                            if (value ==1) {//下一页
                                selectTextGroupType = 0;
                                selectListSumPage = (smapGameStage.getsMapDAO().spiritMap.size - 1) / 20 + 1;
                                if (selectListSumPage == 1) {
                                    return;
                                } else if (selectListPage < selectListSumPage) {
                                    selectListPage++;
                                } else {
                                    selectListPage = 1;
                                }
                                showAchievementGroupId();
                                ifWarp = true;
                            } else {//上一页
                                selectTextGroupType = 0;
                                selectListSumPage = (smapGameStage.getsMapDAO().spiritMap.size - 1) / 20 + 1;
                                if (selectListSumPage == 1) {
                                    return;
                                } else if (selectListPage <= 1) {
                                    selectListPage = selectListSumPage;
                                } else {
                                    selectListPage--;
                                }
                                showAchievementGroupId();
                                ifWarp = true;
                            }
                        }else if(selectTextGroupType==2||selectTextGroupType==3){// 2为陆军招募将军 3为空军招募将军
                            if (value ==1) {//下一页
                                if (selectListSumPage == 1) {
                                    return;
                                } else if (selectListPage < selectListSumPage) {
                                    selectListPage++;
                                } else {
                                    selectListPage = 1;
                                }
                                showRecruitGeneralGroup();
                                ifWarp = true;
                            } else {//上一页
                                if (selectListSumPage == 1) {
                                    return;
                                } else if (selectListPage <= 1) {
                                    selectListPage = selectListSumPage;
                                } else {
                                    selectListPage--;
                                }
                                showRecruitGeneralGroup();
                                ifWarp = true;
                            }
                        }

                    }
                });
                break;
            case 602:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        if (ifBanOperation) { return;}
                        game.playSound(sound);
                        if(selectTextGroupType==0){//奇物购买
                            showCardUi(19);
                        }else if(selectTextGroupType==2){//陆军将领购买
                            Element gE=selectCardEList.get(selectListIndex-1);
                            smapGameStage.getsMapDAO().armyUpdForGeneral(smapGameStage.selectedArmy,gE);
                            if(!smapGameStage.isEditMode(true)){
                                smapGameStage.selectedBuildData.addBuildRound(1);
                            }
                            hidUi(true);
                        }else if(selectTextGroupType==3){//空军将领购买
                            Element gE=selectCardEList.get(selectListIndex-1);
                            smapGameStage.getsMapDAO().airUpdForGeneral(smapGameStage.selectedAir,gE);
                            if(!smapGameStage.isEditMode(true)){
                                smapGameStage.selectedBuildData.addBuildRound(1);
                            }
                            //hidUi(true);
                        }
                    }
                });
                break;

            case 8:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        switch (value){
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                                int index=(tradeInfoNowPage-1)*10+value-1;
                                Fb2Smap.TradeData t= smapGameStage.getsMapDAO().tradeDatas.get(index);
                                int chance=0;
                                Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(smapGameStage.selectedTargetArmyHexagon);
                                if(a!=null&&a.getGeneralIndex()!=0&&a.isPlayer()&&a.getArmyRound()==0&&a.getPotionLegionIndex()==t.getLegionIndex()){
                                    Fb2Smap.GeneralData g=a.getGeneralData();
                                    chance=ComUtil.limitValue(ComUtil.getRandom(1,a.getArmyRank()*g.getPolitical()),0,40);
                                    if(g.getState()==0){
                                        smapGameStage.getsMapDAO().addDialogueData(a.getCountryId(),g,15,"",true);
                                    }
                                    a.addArmyRound(1);
                                }
                                //对盟国贸易时增加额外的{0}%成功率
                                if(smapGameStage.getsMapDAO().ifHaveSpirit(21)){
                                    if(smapGameStage.getsMapDAO().ifAllyPlayerByLi(t.getLegionIndex())){
                                        chance=chance+smapGameStage.getsMapDAO().getSpiritValue(21);
                                    }
                                }


                                int rs= smapGameStage.getsMapDAO().legion_Trade(smapGameStage.getPlayerLegion(), t,chance);
                                smapGameStage.getsMapDAO().checkTask(rs>0,smapGameStage.getPlayerLegionIndex(),11,1);
                                smapGameStage.getsMapDAO().checkTask(rs<=0,smapGameStage.getPlayerLegionIndex(),30,1);

                                hidUi(false);
                                showGeneralDialogueGroupAndPromptGroup();
                                updResourceForPlayer();
                                // showPromptGroup(t.getBuildPolicy(),1,game.gameMethod.getPromptStrForGetFavor(rs));
                                break;
                            case 18:if(tradeInfoNowPage>1){
                                tradeInfoNowPage=tradeInfoNowPage-1;
                            }else {
                                tradeInfoNowPage=tradeInfoSumPage;
                            }
                                showTradeInfoUI( tradeInfoNowPage);
                                break;
                            case 19:if(tradeInfoNowPage==tradeInfoSumPage){
                                tradeInfoNowPage=1;
                            }else {
                                tradeInfoNowPage=tradeInfoNowPage+1;
                            }
                                showTradeInfoUI( tradeInfoNowPage);
                                break;

                        }
                    }
                });
                break;
            case 9://文本概括
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);

                        if(selectTextGroupType==0){
                            smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),12,1);
                            switch (value){
                                case 0://切换
                                    selectTextGroupType=2;
                                    showTextWindow();ifWarp=true;
                                    return;
                                case 1:smapGameStage.getPlayerLegion().updFeatureLv(1); break;
                                case 2:smapGameStage.getPlayerLegion().updFeatureLv(2); break;
                                case 3:smapGameStage.getPlayerLegion().updFeatureLv(3); break;
                                case 4:smapGameStage.getPlayerLegion().updFeatureLv(4); break;
                                case 5:smapGameStage.getPlayerLegion().updFeatureLv(5); break;
                                case 6:smapGameStage.getPlayerLegion().refreshFeature(1); break;
                                case 7:smapGameStage.getPlayerLegion().refreshFeature(2); break;
                                case 8:smapGameStage.getPlayerLegion().refreshFeature(3); break;
                                case 9:smapGameStage.getPlayerLegion().refreshFeature(4); break;
                                case 10:smapGameStage.getPlayerLegion().refreshFeature(5); break;
                            }
                            hidUi(false);
                            showPromptDialogueGroupForLegionFeature(value);
                        }else if(selectTextGroupType==1){//提交任务
                            String b=null; switch (value){
                                case 0://切换
                                    selectTextGroupType=3;
                                    showTextWindow();ifWarp=true;
                                    return;
                                case 1:b=smapGameStage.getsMapDAO().submitTask(0); break;
                                case 2:b=smapGameStage.getsMapDAO().submitTask(1); break;
                                case 3:b=smapGameStage.getsMapDAO().submitTask(2); break;
                                case 4:b=smapGameStage.getsMapDAO().submitTask(3); break;
                                case 5:b=smapGameStage.getsMapDAO().submitTask(4); break;
                                case 6:smapGameStage.getsMapDAO().refreshTask(0); break;
                                case 7:smapGameStage.getsMapDAO().refreshTask(1); break;
                                case 8:smapGameStage.getsMapDAO().refreshTask(2); break;
                                case 9:smapGameStage.getsMapDAO().refreshTask(3); break;
                                case 10:smapGameStage.getsMapDAO().refreshTask(4); break;
                            }
                            int i=value>5?value-6:value-1;
                            hidUi(false);
                            showPromptDialogueGroupForTask(smapGameStage.getsMapDAO().taskDatas.get(i),b);
                            game.gameLayout.updAmbitionForSMapScreen(defaultWindow,smapGameStage.getsMapDAO());
                        }else if(selectTextGroupType==2){//提交国策
                            switch (value){
                                case 0://切换
                                    selectTextGroupType=0;
                                    showTextWindow();ifWarp=true;
                                    return;
                                case 1:smapGameStage.getsMapDAO().settleLegionPolicy(5); break;
                                case 2:smapGameStage.getsMapDAO().settleLegionPolicy(6); break;
                                case 3:smapGameStage.getsMapDAO().settleLegionPolicy(7); break;
                                case 4:smapGameStage.getsMapDAO().settleLegionPolicy(8); break;
                                case 5:smapGameStage.getsMapDAO().settleLegionPolicy(9); break;
                            }
                        }else if(selectTextGroupType==3){
                            if(value==0){
                                selectTextGroupType=1;
                                showTextWindow();ifWarp=true; return;
                            }else if(value==11){
                                showConquestQuickResult();return;
                            }
                        }
                    }
                });
                break;

            case 10:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        // functionIndex 0 选择国家 1是否退出游戏
                        //value 0关闭 1确认 2取消
                        if(value==0){
                            // int c=smapGameStage.selectedCountryIndex;
                            hiddenWindowGroup(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId);
                        }else if(dialogueFunctionIndex==0){
                            if(value==1){
                                if( smapGameStage.selectedRLegionIndex !=0||smapGameStage.isEditMode(false)){

                                    if(smapGameStage.isEditMode(false)){

                                        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId);
                                        if( smapGameStage.selectedRLegionIndex==0){
                                            w.showButton(0);
                                            w.showButton(1);
                                        }else {
                                            w.hidButton(0);
                                            w.hidButton(1);
                                        }
                                    }
                                    smapGameStage.setPlayerLegionIndex( smapGameStage.selectedRLegionIndex);//设置军团
                                    hiddenWindowGroup(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId);
                                    // drawViewMap(smapGameStage.getSmapBg().ifDrawType);
                                    //saveScreenshot();
                                    //  switchMapColor();
                                    showGeneralDialogueGroupAndPromptGroup();
                                    initDefaultWindowForResource();
                                    updResourceForPlayer();
                                    //smapGameStage.cam.asyncMoveCameraToHexagon2(smapGameStage. getPlayerLegion().getCapitalId(),1,2, smapGameStage.cam.getCamZoomMin(), smapGameStage.cam.getCamZoomMax());
                                    if(!smapGameStage.isEditMode(false)){
                                        smapGameStage.cam.asyncMoveCameraToHexagon(smapGameStage.getPlayerLegion().getCapitalId(),1);
                                    }
                                    if(script!=null){
                                        scriptXmlEs=    game.gameMethod.getSMapScreenScipt(script,smapGameStage.getsMapDAO(),scriptXmlEs); ;
                                        scriptStep=0;
                                        cmdStep=-1;
                                        executeAllScripts(true);
                                    }
                                    resetPlayerStar();
                                    // executeScripts(false);
                                }
                            }else if(value==2){
                                hiddenWindowGroup(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId);
                            }else if(value==3){
                                smapGameStage.getsMapDAO().nextGameDifficulty();
                                game.gameLayout.updSelectCountryGroupForGameDifficulty(windowGroups.get(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId),smapGameStage.getsMapDAO());
                            }else if(value==4){
                                smapGameStage.getsMapDAO().nextPlayerMode();
                                game.gameLayout.updSelectCountryGroupForGameMode(windowGroups.get(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId),smapGameStage.getsMapDAO(),smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex));
                            }
                        }else if(dialogueFunctionIndex==1){
                            if(value==1){
                                Gdx.app.exit();
                            }
                        }
                    }
                });
                break;







            case 11://暂停界面
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        //Gdx.app.log("点击了第1个按钮", "x:" + x+" y:" + y);
                        if(value==0){//关闭暂停界面
                            // WindowGroup w=windowGroups.get(ResConfig.Class.SMapScreenPauseGroupId);
                            // w.setVisible(false);
                            hidUi(true);
                        }else if(value==1){//跳转到设置界面
                            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId);
                            w.setVisible(false);
                            w=windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId);
                            w.setVisibleForEffect(true);
                            game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));

                        }else if(value==2){//重新开始
                            // WindowGroup w=windowGroups.get(ResConfig.Class.SMapScreenPauseGroupId);
                            // w.setVisible(false);
                            restarGame();
                        }else if(value==3){//主页
                            //smapGameStage.saveTest();
                            saveAndExit();
                        }else if(value==4){
                            switchMapColor();
                        }

                    }
                });
                break;


            case 12://设置界面 TODO
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        switch (value){
                            case 0://关闭标签,返回暂停界面
                                WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId);
                                w.setVisible(false);
                                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId);
                                w.setVisibleForEffect(true);
                                break;
                            case 1://音乐按钮-
                                game.setMusicVoice(game.musicVoice - 1);
                                game.gameConfig.playerConfig.putInteger("music", game.musicVoice);
                                game.gameConfig.playerConfig.flush();

                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                break;
                            case 2://音乐按钮+
                                game.setMusicVoice(game.musicVoice + 1);
                                game.gameConfig.playerConfig.putInteger("music", game.musicVoice);
                                game.gameConfig.playerConfig.flush();

                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                break;
                            case 3://声效按钮-
                                game.setSoundVoice(game.soundVoice - 1);
                                game.gameConfig.playerConfig.putInteger("sound", game.soundVoice);
                                game.gameConfig.playerConfig.flush();

                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                break;
                            case 4://声效按钮+
                                game.setSoundVoice(game.soundVoice + 1);
                                game.gameConfig.playerConfig.putInteger("sound", game.soundVoice);
                                game.gameConfig.playerConfig.flush();

                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                break;


                            case 5://鼠标减速
                                game.setMoveSpeed(game.moveSpeed - 1);
                                game.gameConfig.playerConfig.putInteger("speed", game.moveSpeed);
                                game.gameConfig.playerConfig.flush();

                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                moveZoom=DefDAO.getMoveZoom(game.moveSpeed);
                                break;
                            case 6://鼠标加速
                                game.setMoveSpeed(game.moveSpeed + 1);
                                game.gameConfig.playerConfig.putInteger("speed", game.moveSpeed);
                                game.gameConfig.playerConfig.flush();
                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                moveZoom=DefDAO.getMoveZoom(game.moveSpeed);
                                break;
                            case 7://是否开启特效
                                game.gameConfig.setIfEffect();
                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                smapGameStage.updAllArmyActor();
                                smapGameStage.updClickActor();
                                break;
                            case 8:
                                game.gameConfig.setIfAutoSave();

                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId)); break;
                            case 9:
                                game.gameConfig.setIfAiActAndNextRound();
                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                break;
                            case 10:
                                game.gameConfig.setIfPromptUnitUpd();
                                game.gameLayout.updSMapScreenOptionWindow(game,windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId));
                                break;
                        }

                    }
                });
                break;

            case 13:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenChiefGroupId);
                        switch (value){
                            case 0://提交修改
                                if(smapGameStage.getsMapDAO().submitPolityBill(bm)){//提交成功
                                    smapGameStage.getsMapDAO().addPromptDialogue(game.gameMethod.getStrValueT("prompt_dialogue_40",smapGameStage.getsMapDAO().getChiefHarmony()),false   );
                                    smapGameStage.getsMapDAO().transBtlModuleByChiefData(smapGameStage.getsMapDAO().chiefData,bm);
                                    smapGameStage.getsMapDAO().checkMassesDemand();
                                }else{//提交失败
                                    smapGameStage.getsMapDAO().addPromptDialogue(game.gameMethod.getStrValueT("prompt_dialogue_41",smapGameStage.getsMapDAO().getChiefHarmony()),false   );
                                }
                                break;
                            case 1://刷新
                                smapGameStage.getsMapDAO().transBtlModuleByChiefData(smapGameStage.getsMapDAO().chiefData,bm);
                                game.gameLayout.updChiefGroup(w,smapGameStage.getsMapDAO(),bm,cardNowPage);
                                break;
                            case 2://切页
                                if(cardNowPage==0){
                                    cardNowPage=1;
                                }else {
                                    cardNowPage=0;
                                }
                                game.gameLayout.updChiefGroup(w,smapGameStage.getsMapDAO(),bm,cardNowPage);
                                break;
                        }
                    }
                });
                break;
            case 131://点击政策查看子选项
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenChiefGroupId);
                        selectCardId=value+cardNowPage*10;
                        game.gameLayout.showPolityGroupForSelectPolity(w,smapGameStage.getsMapDAO(),selectCardId,bm.getBMValue(selectCardId+2));

                    }
                });
                break;
            case 132: //子选项政策详情
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenChiefGroupId);
                        //获取选项,根据颜色判断下一步
                        TextButton tb=w.getTButton(value+11);
                        if(!tb.getLabel().getColor().equals(Color.LIGHT_GRAY)){
                            game.gameLayout.updChiefGroupForSelectChild(w,smapGameStage.getsMapDAO(),bm,cardNowPage,selectCardId,value);
                        }
                        //WindowGroup w, Fb2Smap sMapDAO, BtlModule bm,int chiefType,int chiefTypeValue
                    }
                });
                break;
            case 14:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        switch (value){
                            case 0://确认
                                confirmEditGroup();
                                break;
                            case 1://切页
                                if(cardNowPage==0){
                                    cardNowPage=1;
                                }else {
                                    cardNowPage=0;
                                }
                                showEditGroup();
                                break;
                        }
                    }
                });
                break;


            case 141://编辑btl框体
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        updEditGroup(value);
                    }
                });
                break;
            case 15:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);

                        switch (value){
                            case 0://切换到兵种详情
                                if( smapGameStage.selectedArmy==null){return;}
                                windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).setVisible(false);
                                showArmyInfoUI(smapGameStage.selectedArmy);
                                break;
                            case 1://切换兵种阵型策略 TODO
                                if( smapGameStage.selectedArmy==null){return;}
                                int index= smapGameStage.selectedArmy.getArmyFormation();
                              /*if(index>20){
                                  index=0;
                              }else if(index==1){
                                  index=21;
                              } else {
                                  index++;
                              }*/
                                if(index==0){
                                    index++;
                                }else if(index==25){
                                    index=0;
                                }else{
                                    if(  smapGameStage.selectedArmy.getArmyDirection()==0){//左
                                        if(index==1){
                                            index=22;
                                        }else if(index==22){
                                            index=21;
                                        }else if(index==21){
                                            index=24;
                                        }else if(index==24){
                                            index=25;
                                        }else{
                                            index=0;
                                        }
                                    }else {//右
                                        if(index==1){
                                            index=22;
                                        }else if(index==22){
                                            index=23;
                                        }else if(index==23){
                                            index=26;
                                        }else if(index==26){
                                            index=25;
                                        }else{
                                            index=0;
                                        }
                                    }
                                }
                                smapGameStage.selectedArmy.setArmyFormation(index);
                                WindowGroup w= windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                                if(smapGameStage.selectedArmy.getArmyFormation()==0){
                                    w.setTBLabelAndFixWidth(0,game.gameMethod.getStrValueT("noun_freeformat"),10);
                                }else if(smapGameStage.selectedArmy.getArmyFormation()==1){
                                    w.setTBLabelAndFixWidth(0,game.gameMethod.getStrValueT("noun_fixedformat"),10);
                                }else if(smapGameStage.selectedArmy.getArmyFormation()>20){
                                    w.setTBLabelAndFixWidth(0,game.gameMethod.getStrValueT("noun_sortformat"+smapGameStage.selectedArmy.getArmyFormation()),10);
                                }else {
                                    w.hidTButton(0);
                                }
                                break;
                            case 2:
                                w= windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                                if(selectListType==3&&smapGameStage.selectedAir!=null){
                                    if(cardNowPage==0){cardNowPage=1;}else{cardNowPage=0;}
                                    game.gameLayout.updAirWindowForFeature(w,smapGameStage.selectedAir,cardNowPage);
                                }else if(smapGameStage.selectedArmy!=null){
                                    if(cardNowPage==0){cardNowPage=1;}else{cardNowPage=0;}
                                    game.gameLayout.updArmyWindowForFeature(w,smapGameStage.selectedArmy,cardNowPage);
                                }
                                break;
                        }
                    }
                });
                break;
            case 151://升级编制
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        if(selectListType==3){
                            if( smapGameStage.selectedAir==null|| !(smapGameStage.selectedAir.isPlayer()||smapGameStage.isEditMode(true))){
                                return;
                            }
                        }else {
                            if(smapGameStage.selectedArmy==null|| !(smapGameStage.selectedArmy.isPlayer()||smapGameStage.isEditMode(true))){
                                return;
                            }
                        }

                        switch (value){
                            case 0://升级编制0
                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(97,0),game.gameMethod.getPromptStrT(97,1,smapGameStage.selectedArmy.getUnitGroupGroupLv(0),smapGameStage.selectedArmy.getUnitGroupGroupLv(0)+1),"",29,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),0,true);
                                break;
                            case 1://升级编制1
                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(97,0),game.gameMethod.getPromptStrT(97,1,smapGameStage.selectedArmy.getUnitGroupGroupLv(1),smapGameStage.selectedArmy.getUnitGroupGroupLv(1)+1),"",29,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),1,true);
                                break;
                            case 2://升级编制2
                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(97,0),game.gameMethod.getPromptStrT(97,1,smapGameStage.selectedArmy.getUnitGroupGroupLv(2),smapGameStage.selectedArmy.getUnitGroupGroupLv(2)+1),"",29,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),2,true);
                                break;
                            case 3://升级编制3
                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(97,0),game.gameMethod.getPromptStrT(97,1,smapGameStage.selectedArmy.getUnitGroupGroupLv(3),smapGameStage.selectedArmy.getUnitGroupGroupLv(3)+1),"",29,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),3,true);
                                break;
                            case 4://升级编制4
                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(97,0),game.gameMethod.getPromptStrT(97,1,smapGameStage.selectedArmy.getUnitGroupGroupLv(4),smapGameStage.selectedArmy.getUnitGroupGroupLv(4)+1),"",29,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),4,true);
                                break;
                            case 5://升级编制5
                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(97,0),game.gameMethod.getPromptStrT(97,1,smapGameStage.selectedArmy.getUnitGroupGroupLv(5),smapGameStage.selectedArmy.getUnitGroupGroupLv(5)+1),"",29,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),5,true);
                                break;
                            case 6://升级编制6
                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(97,0),game.gameMethod.getPromptStrT(97,1,smapGameStage.selectedArmy.getUnitGroupGroupLv(6),smapGameStage.selectedArmy.getUnitGroupGroupLv(6)+1),"",29,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),6,true);
                                break;
                            case 7://升级武器等级0
                                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).getTButton(1).getLabel().getColor().equals(Color.GREEN)){
                                    if(smapGameStage.selectedArmy!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedArmy.getUnitGroupWealLv(0),smapGameStage.selectedArmy.getUnitGroupWealLv(0)+1),"",25,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),0,true);
                                    }else if(smapGameStage.selectedAir!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedAir.getUnitAbility(0),smapGameStage.selectedAir.getUnitAbility(0)+1
                                        ),"",24,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),0,true);
                                    }
                                }
                                break;
                            case 8://升级武器等级1
                                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).getTButton(2).getLabel().getColor().equals(Color.GREEN)){
                                    if(smapGameStage.selectedArmy!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedArmy.getUnitGroupWealLv(1),smapGameStage.selectedArmy.getUnitGroupWealLv(1)+1),"",25,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),1,true);
                                    }else if(smapGameStage.selectedAir!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedAir.getUnitAbility(1),smapGameStage.selectedAir.getUnitAbility(1)+1),"",24,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),1,true);
                                    }
                                }
                                break;
                            case 9://升级武器等级2
                                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).getTButton(3).getLabel().getColor().equals(Color.GREEN)){
                                    if(smapGameStage.selectedArmy!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedArmy.getUnitGroupWealLv(2),smapGameStage.selectedArmy.getUnitGroupWealLv(2)+1),"",25,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),2,true);
                                    }else if(smapGameStage.selectedAir!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedAir.getUnitAbility(2),smapGameStage.selectedAir.getUnitAbility(2)+1),"",24,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),2,true);
                                    }
                                }
                                break;
                            case 10://升级武器等级3
                                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).getTButton(4).getLabel().getColor().equals(Color.GREEN)){
                                    if(smapGameStage.selectedArmy!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedArmy.getUnitGroupWealLv(3),smapGameStage.selectedArmy.getUnitGroupWealLv(3)+1),"",25,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),3,true);
                                    }else if(smapGameStage.selectedAir!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedAir.getUnitAbility(3),smapGameStage.selectedAir.getUnitAbility(3)+1),"",24,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),3,true);
                                    }
                                }
                                break;
                            case 11://升级武器等级4
                                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).getTButton(5).getLabel().getColor().equals(Color.GREEN)){
                                    if(smapGameStage.selectedArmy!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedArmy.getUnitGroupWealLv(4),smapGameStage.selectedArmy.getUnitGroupWealLv(4)+1),"",25,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),4,true);
                                    }else if(smapGameStage.selectedAir!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedAir.getUnitAbility(4),smapGameStage.selectedAir.getUnitAbility(4)+1),"",24,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),4,true);
                                    }
                                }
                                break;
                            case 12://升级武器等级5
                                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).getTButton(6).getLabel().getColor().equals(Color.GREEN)){
                                    if(smapGameStage.selectedArmy!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedArmy.getUnitGroupWealLv(5),smapGameStage.selectedArmy.getUnitGroupWealLv(5)+1),"",25,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),5,true);
                                    }else if(smapGameStage.selectedAir!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedAir.getUnitAbility(5),smapGameStage.selectedAir.getUnitAbility(5)+1),"",24,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),5,true);
                                    }
                                }
                                break;
                            case 13://升级武器等级6
                                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).getTButton(7).getLabel().getColor().equals(Color.GREEN)){
                                    if(smapGameStage.selectedArmy!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedArmy.getUnitGroupWealLv(6),smapGameStage.selectedArmy.getUnitGroupWealLv(6)+1),"",25,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getHexagonIndex(),6,true);
                                    }else if(smapGameStage.selectedAir!=null){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(82,0),
                                                game.gameMethod.getPromptStrT(82,1,smapGameStage.selectedAir.getUnitAbility(6),smapGameStage.selectedAir.getUnitAbility(6)+1),"",24,smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),6,true);
                                    }
                                }
                                break;
                            case 14://解散编队0
                                if(selectListType!=3) {
                                    if(smapGameStage.selectedArmy!=null) {
                                       if(smapGameStage.selectedArmy.getUnitGroupSum()>1&&smapGameStage.selectedArmy.getUnitGroupGroupLv(0) > 1){//重整
                                            smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81, 0), game.gameMethod.getPromptStrT(81, 1), "", 23, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 0,true);
                                        }else if( smapGameStage.selectedArmy.getUnitGroupSum()==1){//解散
                                            smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(107, 0), game.gameMethod.getPromptStrT(107, 1), "", 23, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 0,true);
                                        }
                                    }
                                }else {//升级空军物资
                                    if(smapGameStage.selectedAir!=null&&smapGameStage.selectedAir.isEmptyBuildRound()&&smapGameStage.selectedAir.canUpdGoods()&&smapGameStage.selectedAir.canPayRes()){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(99, 0), game.gameMethod.getPromptStrT(99, 1), "", 32, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedAir.getAirIndex(), 0,true);
                                    }
                                }
                                break;
                            case 15://解散编队1
                                if(selectListType==3){
                                    if(smapGameStage.selectedAir!=null&&smapGameStage.selectedAir.isEmptyBuildRound()){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedAir.getUnitAbility(1),smapGameStage.selectedAir.getUnitAbility(1)+1), "", 31, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),1,true);
                                    }
                                }else {
                                    if(smapGameStage.selectedArmy!=null) {
                                        if (smapGameStage.selectedArmy.isUnitGroup() ) {
                                            if(  smapGameStage.selectedArmy.getUnitGroupGroupLv(1) > 0){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81, 0), game.gameMethod.getPromptStrT(81, 1), "", 23, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 1,true);
                                            }
                                        }else {//升级能力
                                            if(smapGameStage.selectedArmy.isEmptyBuildRound()&&smapGameStage.selectedArmy.getUnitWealv1() <= smapGameStage.selectedArmy.getUnitWealv0Value()){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedArmy.getUnitAbility(1),smapGameStage.selectedArmy.getUnitAbility(1)+1), "", 30, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 1,true);
                                            }
                                        }
                                    }
                                }
                                break;
                            case 16://解散编队2
                                if(selectListType==3){
                                    if(smapGameStage.selectedAir!=null&&smapGameStage.selectedAir.isEmptyBuildRound()){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedAir.getUnitAbility(2),smapGameStage.selectedAir.getUnitAbility(2)+1), "", 31, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),2,true);
                                    }
                                }else {
                                    if(smapGameStage.selectedArmy!=null) {
                                        if (smapGameStage.selectedArmy.isUnitGroup() ) {
                                            if( smapGameStage.selectedArmy.getUnitGroupGroupLv(2) > 0){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81, 0), game.gameMethod.getPromptStrT(81, 1), "", 23, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 2,true);
                                            }
                                        }else {//升级能力
                                            if( smapGameStage.selectedArmy.isEmptyBuildRound()&&smapGameStage.selectedArmy.getUnitWealv2() <= smapGameStage.selectedArmy.getUnitWealv0Value()){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedArmy.getUnitAbility(2),smapGameStage.selectedArmy.getUnitAbility(2)+1), "", 30, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 2,true);
                                            }
                                        }
                                    }
                                }
                                break;
                            case 17://解散编队3
                                if(selectListType==3){
                                    if(smapGameStage.selectedAir!=null&&smapGameStage.selectedAir.isEmptyBuildRound()){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedAir.getUnitAbility(3),smapGameStage.selectedAir.getUnitAbility(3)+1), "", 31, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),3,true);
                                    }
                                }else {
                                    if(smapGameStage.selectedArmy!=null){
                                        if (smapGameStage.selectedArmy.isUnitGroup() ) {
                                            if (smapGameStage.selectedArmy.getUnitGroupGroupLv(3) > 0) {
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81, 0), game.gameMethod.getPromptStrT(81, 1), "", 23, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 3,true);
                                            }
                                        }else {//升级能力
                                            if( smapGameStage.selectedArmy.isEmptyBuildRound()&&smapGameStage.selectedArmy.getUnitWealv3() <= smapGameStage.selectedArmy.getUnitWealv0Value()){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedArmy.getUnitAbility(3),smapGameStage.selectedArmy.getUnitAbility(3)+1), "", 30, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 3,true);
                                            }
                                        }
                                    }
                                }
                                break;
                            case 18://解散编队4
                                if(selectListType==3){
                                    if(smapGameStage.selectedAir!=null&&smapGameStage.selectedAir.isEmptyBuildRound()){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedAir.getUnitAbility(4),smapGameStage.selectedAir.getUnitAbility(4)+1), "", 31, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),4,true);
                                    }
                                }else {
                                    if(smapGameStage.selectedArmy!=null) {

                                        if (smapGameStage.selectedArmy.isUnitGroup() ) {
                                            if (smapGameStage.selectedArmy.getUnitGroupGroupLv(4) > 0) {
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81, 0), game.gameMethod.getPromptStrT(81, 1), "", 23, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 4,true);
                                            }
                                        }else {//升级能力
                                            if( smapGameStage.selectedArmy.isEmptyBuildRound()&&smapGameStage.selectedArmy.getUnitWealv4() <= smapGameStage.selectedArmy.getUnitWealv0Value()){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedArmy.getUnitAbility(4),smapGameStage.selectedArmy.getUnitAbility(4)+1), "", 30, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 4,true);
                                            }
                                        }
                                    }
                                }
                                break;
                            case 19://解散编队5
                                if(selectListType==3){
                                    if(smapGameStage.selectedAir!=null&&smapGameStage.selectedAir.isEmptyBuildRound()){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedAir.getUnitAbility(5),smapGameStage.selectedAir.getUnitAbility(5)+1), "", 31, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),5,true);
                                    }
                                }else {
                                    if(smapGameStage.selectedArmy!=null) {

                                        if (smapGameStage.selectedArmy.isUnitGroup() ) {
                                            if( smapGameStage.selectedArmy.getUnitGroupGroupLv(5) > 0){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81, 0), game.gameMethod.getPromptStrT(81, 1), "", 23, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 5,true);
                                            }
                                        }else {//升级能力
                                            if( smapGameStage.selectedArmy.isEmptyBuildRound()&&smapGameStage.selectedArmy.getUnitWealv5() <= smapGameStage.selectedArmy.getUnitWealv0Value()){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedArmy.getUnitAbility(5),smapGameStage.selectedArmy.getUnitAbility(5)+1), "", 30, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 5,true);
                                            }
                                        }
                                    }
                                }
                                break;
                            case 20://解散编队6
                                if(selectListType==3){
                                    if(smapGameStage.selectedAir!=null&&smapGameStage.selectedAir.isEmptyBuildRound()){
                                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedAir.getUnitAbility(6),smapGameStage.selectedAir.getUnitAbility(6)+1), "", 31, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedAir.getAirIndex(),6,true);
                                    }
                                }else {
                                    if(smapGameStage.selectedArmy!=null) {
                                        if (smapGameStage.selectedArmy.isUnitGroup() ) {
                                            if (smapGameStage.selectedArmy.getUnitGroupGroupLv(6) > 0) {
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(81, 0), game.gameMethod.getPromptStrT(81, 1), "", 23, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 6,true);
                                            }
                                        }else {//升级能力
                                            if( smapGameStage.selectedArmy.isEmptyBuildRound()&&smapGameStage.selectedArmy.getUnitWealv6() <= smapGameStage.selectedArmy.getUnitWealv0Value()){
                                                smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(98, 0), game.gameMethod.getPromptStrT(98, 1,smapGameStage.selectedArmy.getUnitAbility(6),smapGameStage.selectedArmy.getUnitAbility(6)+1), "", 30, smapGameStage.getPlayerLegionIndex(), smapGameStage.getPlayerLegionIndex(), smapGameStage.selectedArmy.getHexagonIndex(), 6,true);
                                            }
                                        }
                                    }
                                }
                                break;
                        }
                    }
                });
                break;
            case 152://海军
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        showPromptTextForUnitArmyWeapon(value);
                    }
                });
                break;
            case 16:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        showPromptGroup(value);
                    }
                });
                break;

            case 17:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        showGeneralDialogueGroupAndPromptGroup();
                    }
                });
                break;


            case 18:
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        switch (value){
                            case 0://复制/关闭
                                if(smapGameStage.isEditMode(true)){
                                    if(smapGameStage.rescource.drawType<2){//复制兵种
                                        smapGameStage.copyArmy=smapGameStage.selectedArmy;
                                    }else {//复制归属
                                        smapGameStage.copyRegionLi=smapGameStage.selectedRLegionIndex;
                                    }
                                }else {
                                    game.asyncManager.clearTask();
                                    game.showGameScreen(screenId,1);
                                }
                                break;
                            case 1://状态/下一个
                                if(smapGameStage.isEditMode(true)){
                                    if(smapGameStage.rescource.drawType<2){//粘贴兵种,不会复制将军信息
                                        smapGameStage.pasteArmy(smapGameStage.coord.getId());
                                    }else {//粘贴归属
                                        smapGameStage.getsMapDAO().setRegionAllLegionIndex(smapGameStage.selectedRegionId,smapGameStage.copyRegionLi,true);
                                    }
                                }else {
                                    executeScripts(false);
                                }
                                break;
                        }
                    }
                });
                break;



            /*case 19://教程按钮,唯一不受ifBanOperation 影响的按钮
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        if(value==0){//下一步
                            executeScripts();
                        }else{//关闭
                            game.asyncManager.clearTask();
                            game.showGameScreen(screenId,1);
                        }
                    }
                });
                break;*/


            default://查询绘制省区
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) { if(ifBanOperation){return;}
                        game.playSound(sound);
                        Gdx.app.log("点击了默认按钮", "x:" + x+" y:" + y);
                    }
                });
                break;
        }
    }

    public void showClickInfo() {
        if(selectListType==3&&selectListIndex!=0){
            if(selectAirList.size>0){
                Fb2Smap.AirData airData= selectAirList.get((selectListPage-1)*4+selectListIndex-1);
                showAirInfoUI(airData);ifWarp=true;
            }else if(smapGameStage.selectedAir!=null){
                showAirInfoUI(smapGameStage.selectedAir);ifWarp=true;
            }
        }else if(smapGameStage.getsMapDAO().masterData.getPlayerMode()!=2&&smapGameStage.getSelectedArmy()!=null&& smapGameStage.selectedArmy.armyActor!=null){
            //showArmyInfoUI(smapGameStage.selectedArmy);
            showArmyFormationWindow(smapGameStage.selectedArmy);
        }else {
            //showRegionArmyInfoUI(smapGameStage.selectedRegionId,-1,1);
            showRegionBuildGroup(smapGameStage.getSelectBuildData());ifWarp=true;
        }
    }


    private void showChiefGroup() {
        hidUi(false);
        hidCommonUi();
        smapGameStage.setIfFixed(true);
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenChiefGroupId);
        if(smapGameStage.getsMapDAO().chiefData==null){
            smapGameStage.getsMapDAO().initChief();
        }
        if(!w.getImage(1).isVisible()||smapGameStage.getsMapDAO().getIfNeedDrawViewMap()||politicalMapTexture==null){
            drawViewMap(smapGameStage.getSmapBg().showType);
            smapGameStage.getsMapDAO().setIfNeedDrawViewMap(false);
        }
        // GameMap.getPriviewMapBySMapDAO(priviewMapPixmap,smapGameStage.getsMapDAO(),type);
        politicalMapTexture.draw(politicalMapPixmap,0,0);
        w.setImageRegionNotChangeSize(1,new TextureRegion(politicalMapTexture));
        cardNowPage=0;
        smapGameStage.getsMapDAO().transBtlModuleByChiefData(smapGameStage.getsMapDAO().chiefData,bm);
        game.gameLayout.updChiefGroup(w,smapGameStage.getsMapDAO(),bm,cardNowPage);
        //w.setLabelText(0,game.gameMethod.getChiefStateStr(smapGameStage.getsMapDAO()));
        w.setScrollLabel(0,game.gameMethod.getChiefStateStr(smapGameStage.getsMapDAO()));
        w.setVisibleForEffect(true);
    }


    //打开内容编辑,需要提前准备好bm和编辑目标和编辑页数
    //编辑目标
    //
    private void showEditGroup() {
        hidUi(false);
        hidCommonUi();
        smapGameStage.setIfFixed(true);
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenEditGroupId);
        game.gameLayout.updEditGroup(w,smapGameStage.getsMapDAO(),bm,cardNowPage);
        w.setVisibleForEffect(true);
    }

    //确认修改内容
    private void confirmEditGroup(){
        updFb2SmapByBtlModule();
        cardNowPage=0;
        hidUi(false);
    }

    private void updEditGroup(int value){
        TextButton tb=windowGroups.get(ResDefaultConfig.Class.SMapScreenEditGroupId).getTButton(value);
        if (!tb.getLabel().getColor().equals(Color.RED)&&ifInputVisable) {
            Element x=  game.gameConfig.getBMRuleXmlE(bm.getMode()).getChild(value+50*(cardNowPage)-1);
            if(x!=null){
                ifInputVisable=false;
                bmIndex=value;
                Gdx.input.getTextInput(bmListener, "updBM "+x.get("name","")+":"+x.get("remark",""),bm.getBMValue(value+50*(cardNowPage))+"","");
            }
        }
    }




    public void saveAndExit() {
        if(!ifSave){
            game.save(game.getSMapDAO().masterData.getBtlType()==1?0:2);
        }
        game.getGameFramework().saveStagePreview();
        game.asyncManager.clearTask();
        game.showGameScreen(screenId,1);
    }

    //截屏
    private void saveScreenshot() {
        //把首都放在核心位置
        //  smapGameStage.setPotionInPlayerCaptial();
        //偏移地图到最小比例  int id, final int moveSecond, final int zoomstate, final float zoomMin, final float zoomMax

        CHAsyncTask task=new CHAsyncTask("saveScreenshot",10) {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(String result) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                    }else if(!game.gameConfig.getIfIgnoreBug()){
                        game.remindBugFeedBack();
                    }
                    game.recordLog("SMapScreen onPostExecute ",e);
                }
                game.getGameFramework().saveSmapScreenshot(ResDefaultConfig.Path.lastCountryView);
                showGeneralDialogueGroupAndPromptGroup();
                getScript();
                initDefaultWindowForResource();
                updResourceForPlayer();
            }
            @Override
            public String doInBackground() {
                smapGameStage.cam.asyncMoveCameraToHexagon2(smapGameStage. getPlayerLegion().getCapitalId(),1,2, smapGameStage.cam.getCamZoomMin(), smapGameStage.cam.getCamZoomMax());
                return null;
            }

        };
        game.asyncManager.loadTask(task);
        game.asyncManager.update();
    }


    private void showPauseGroup() {
        hidCommonUi();
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId);
        w.setVisibleForEffect(true);
        smapGameStage.setIfFixed(true);
        if(!w.getImage(1).isVisible()||smapGameStage.getsMapDAO().getIfNeedDrawViewMap()){
            drawViewMap(smapGameStage.getSmapBg().showType);
            smapGameStage.getsMapDAO().setIfNeedDrawViewMap(false);
        }
        //初始教程
        if(game.resGameConfig.ifPromptFirstOperate&&smapGameStage.getsMapDAO().masterData.getBtlType()==0&&!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenPause)&&checkOpenGroup("openPauseGroup",true)){
            addAssistantDialogue("prompt_dialogue_52",false);
            game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenPause);
        }
    }


    private void showAchievementGroupId() {
        hidCommonUi();
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenAchievementGroupId);
        if(selectTextGroupType==0){
            game.gameLayout.updAchievementWindowForSpirit(w,smapGameStage.rescource,smapGameStage.getsMapDAO(),selectListPage);
        }else if(selectTextGroupType==1){
            game.gameLayout.updAchievementWindowForMedal(w,smapGameStage.getsMapDAO());
        }
        w.setVisibleForEffect(true);
        smapGameStage.setIfFixed(true);
       // executeScripts(false);
        //初始教程
        if(game.resGameConfig.ifPromptFirstOperate&&smapGameStage.getsMapDAO().masterData.getBtlType()==0){
            if(checkOpenGroup("openAchieveGroupForSpirit",true)){
                if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenSpirit)){
                    addAssistantDialogue("prompt_dialogue_48",false);
                    game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenSpirit);
                }
            }
        }
    }

    private void showPromptDialogueGroupForLegionFeature(int value) {
        if(value<6){//升级技能
            smapGameStage.getsMapDAO().addPromptDialogue(game.gameMethod.getStrValueT("prompt_dialogue_1","legionfeature_name_"+smapGameStage.getPlayerLegion().getLegionFeatureByIndex(value))  ,true );
        }else {//习得技能
            value=value%5;
            smapGameStage.getsMapDAO().addPromptDialogue(game.gameMethod.getStrValueT("prompt_dialogue_2","legionfeature_name_"+smapGameStage.getPlayerLegion().getLegionFeatureByIndex(value))+ComUtil.converNumToRoman(smapGameStage.getPlayerLegion().getLegionFeatureLvByIndex(value))  ,true  );
        }
        //  showGeneralDialogueGroupAndPromptGroup();
    }

    private void showPromptDialogueGroupForTask(Fb2Smap.TaskData t, String str) {
        if(str!=null&&t.getBontyValue()!=0){//任务结算
            smapGameStage.getsMapDAO().addPromptDialogue(game.gameMethod.getStrValueT("prompt_dialogue_3",t.getBontyStr(),t.getTaskNameStr()) ,true  );
        }else {//刷新任务
            smapGameStage.getsMapDAO().addPromptDialogue(game.gameMethod.getStrValueT("prompt_dialogue_4",t.getTaskNameStr()) ,true   );
        }
        //   showGeneralDialogueGroupAndPromptGroup();
    }

    //0 legionFeature 1 task
    private void showTextWindow() {
        hidCommonUi();
        WindowGroup window= windowGroups.get(ResDefaultConfig.Class.SMapScreenTextGroupId);
        window.setVisibleForEffect(true);
        smapGameStage.setIfFixed(true);
        if(selectTextGroupType==0){ //textGoup 0特性 1日常任务 2国策 3征服任务
            game.gameLayout.updTextWindowForLegionFeature(window,smapGameStage.getPlayerLegion());
        }else if(selectTextGroupType==1){
            smapGameStage.getsMapDAO().checkPlayerResource();
            game.gameLayout.updTextWindowForTask(window,smapGameStage.getPlayerLegion(),smapGameStage.getsMapDAO().taskDatas);
        }else if(selectTextGroupType==2){
            game.gameLayout.updTextWindowForLegionPolicy(window,smapGameStage.getPlayerLegion(),smapGameStage.getsMapDAO().taskDatas);
        }else if(selectTextGroupType==3){
            game.gameLayout.updTextWindowForConquestTask(window,smapGameStage.getsMapDAO());
        }
        //初始教程
        if(game.resGameConfig.ifPromptFirstOperate&&smapGameStage.getsMapDAO().masterData.getBtlType()==0){
            if(checkOpenGroup("openTextGroupForLegionFeature",true)){//0特性
                if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenLegionFeature)){
                    addAssistantDialogue("prompt_dialogue_56",false);
                    game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenLegionFeature);
                }
            }else  if(checkOpenGroup("openTextGroupForDailyTask",true)){//1日常任务
                if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenDailyTask)){
                    addAssistantDialogue("prompt_dialogue_46",false);
                    game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenDailyTask);
                }
            }else if(checkOpenGroup("openTextGroupForResolution",true)){//2国策
                if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenResolution)){
                    addAssistantDialogue("prompt_dialogue_57",false);
                    game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenResolution);
                }
            }else if(checkOpenGroup("openTextGroupForConquestTask",true)){//3征服任务
                if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenConquestTask)){
                    addAssistantDialogue("prompt_dialogue_47",false);
                    game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenConquestTask);
                }
            }
        }
    }


    private void showRegionBuildGroup(Fb2Smap.BuildData selectBuildData) {
        if(selectBuildData==null){return;}
        hidCommonUi();
        WindowGroup window= windowGroups.get(ResDefaultConfig.Class.SMapScreenRegionBuildGroupId);
        window.setVisibleForEffect(true);
        smapGameStage.setIfFixed(true);
        game.gameLayout.updRegionBuildGroup(smapGameStage.rescource,window,selectBuildData);
        //初始教程
        if(game.resGameConfig.ifPromptFirstOperate&&smapGameStage.getsMapDAO().masterData.getBtlType()==0&&checkOpenGroup("openRegionInfoGroup",true)){
            if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenRegionInfo)){
                addAssistantDialogue("prompt_dialogue_58",false);
                game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenRegionInfo);
            }
        }
    }


    public void showNextPrompt(boolean select){
        if(smapGameStage.getsMapDAO().promptDatas.size>0){
            Fb2Smap.PromptData p=smapGameStage.getsMapDAO().promptDatas.get(0);
            boolean ifBreak=smapGameStage.getsMapDAO().executeDiplomacySelected( p,select,true);
            smapGameStage.getsMapDAO().promptDatas.removeValue(p,false);
            if(ifBreak){
                return;
            }
            smapGameStage.getsMapDAO().promptDatas.removeValue(p,false);
            showGeneralDialogueGroupAndPromptGroup();
        }else {
            hidUi(true);
        }
    }


    public void showIntroductionGroup(boolean ifPrompt) {
        hidCommonUi();
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenIntroductionGroupId);
        w.setVisibleForEffect(true);

        //updPromptWindow(MainGame game, WindowGroup w, String title, String detail, String effect, IntArray leftCountry,IntArray rightCountry)
        game.gameLayout.updIntroductionWindow(w,smapGameStage.getsMapDAO(),ifPrompt);
       /*if(smapGameStage.getsMapDAO().promptDatas==null|| smapGameStage.getsMapDAO().promptDatas.size==0){
       //    Fb2Smap.LegionData l= smapGameStage.getPlayerLegion();
         //  game.gameLayout.updPromptWindow(w,game.gameMethod.getStrValue(l.legionName), smapGameStage.getsMapDAO().getPromptInfo(),game.gameMethod.getStrValue("prompt_gameend",smapGameStage.getsMapDAO().getRemainRound()));
       }else {
           Fb2Smap.PromptData promptData= smapGameStage.getsMapDAO().promptDatas.get(0);
         //  game.gameLayout.updPromptWindow(w,promptData.getTitle(),promptData.getContent(),promptData.getEffect());
       //    game.gameLayout.updPromptWindow(w,promptData.getTitle(),promptData.getContent(),promptData.getEffect());

           smapGameStage.getsMapDAO().promptDatas.removeIndex(0);
       }*/
        smapGameStage.setIfFixed(true);
    }


    public void showGameResultGroup(int rs) {
        hidCommonUi();
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGameResultGroupId);
        w.setVisible(true);
        if(rs==-1){
            w.getImage(1).setVisible(true);
        }else {
            w.getImage(2).setVisible(true);
            w.getImage(3).setVisible(true);
            w.setImageRegion(3,game.getImgLists().getTextureByName(DefDAO.getGameResultEvaluate(rs)));
        }
        Gdx.app.log("warResult",":"+rs);
    }

    /*public boolean showGeneralDialogueGroup(){
        if(smapGameStage.getsMapDAO().dialogueDatas.size>0){
            hidCommonUi();
            WindowGroup w=windowGroups.get(ResConfig.Class.SMapScreenGeneralDialogueGroupId);
            w.setVisible(true);
            game.gameLayout.updGeneralDialogueGroup(w, smapGameStage.getsMapDAO().dialogueDatas.get(0));
            smapGameStage.getsMapDAO().dialogueDatas.removeIndex(0);
            return true;
        }else{
            hidUi();
            return false;
        }
    }*/

    public void resize(int width, int height) {
        // use true here to center the camera
        // that's what you probably want in case of a UI

        uiStage.getViewport().update(width, height, false);
        //uiStage.getViewport().setWorldSize(width, height);
        /*for(WindowGroup w:windowGroups){
            w.resize(uiStage.getWidth(),uiStage.getHeight());
        }*/
        //   smapGameStage.getViewport().setWorldSize(uiStage.getWidth(),uiStage.getHeight());
        //smapGameStage.getViewport().update(width, height, false);/**/
        /**/
        //Gdx.app.log("resize1",width+":"+height);
        //Gdx.app.log("resize2",uiStage.getWidth()+":"+uiStage.getHeight());
    }

    public void setDebugActor(String name,Actor a) {
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

    public void showUnitBackButton() {
        defaultWindow.setButtonImage(13,game.getImgLists().getTextureByName("circleButton_back"));
    }

    public void hidUnitBackButton() {
        defaultWindow.hidButton(13);
        smapGameStage.getsMapDAO().resetLastUnitHexagon();
    }

    public void executeEvent(int eventId) {
        scriptXmlEs=    game.gameMethod.getSMapScreenScipt(script,smapGameStage.getsMapDAO(),scriptXmlEs,eventId); ;
        scriptStep=0;
        cmdStep=-1;
        executeScripts(true);
    }

    //展示一个界面,用来显示可以招募的将领
    //armyType  1步兵2炮兵3坦克4海军5空军  0不限制 -1陆军 步炮坦
    public boolean showCanRecruitGeneralGroup(int unitValue, int armyType) {
        if(!game.resGameConfig.enableGeneral){
            return false;
        }
        if(armyType==5&&smapGameStage.getsMapDAO().masterData.getPlayerMode()!=2){
            smapGameStage.selectedAir=smapGameStage.getsMapDAO().getAirData(unitValue);
            selectTextGroupType=3;
        }else{
            smapGameStage.selectedArmy=smapGameStage.getsMapDAO().getArmyDataByHexagon(unitValue);
            selectTextGroupType=2;
        }
        //存储所有可以招募的
        // haveCardEList=smapGameStage.getsMapDAO().generalEs;
        haveCardEList=GameMethod.getCanRecruitGeneralE(smapGameStage.getsMapDAO(),smapGameStage.getPlayerLegion().getCountryId(),armyType,haveCardEList,game.resGameConfig.ifCanRecruitFriendlyCountryGeneral);
        if(haveCardEList.size==0){
            return false;
        }
        selectListSumPage=(GameUtil.getArraySize(haveCardEList)-1)/20+1;
        if(selectListPage==0||selectListPage>selectListSumPage){
            selectListPage=1;
        }
        showRecruitGeneralGroup();
        return true;
    }

    public void showRecruitGeneralGroup(){
        hidCommonUi();
        closeOtherGroupExceptSpecified(0);
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenAchievementGroupId);
        selectCardEList= GameMethod.getElementByPage(haveCardEList,selectCardEList,selectListPage,20);
        game.gameLayout.updAchievementWindowForGeneral(w,smapGameStage.rescource,smapGameStage.getsMapDAO(),selectCardEList,selectListPage,selectListSumPage,haveCardEList.size);
        w.setVisible(true);
        smapGameStage.setIfFixed(true);
    }

    public void updRightBorderInfoForLegionUnit() {
        game.gameLayout.updRegionBorderGroupForLegionUnitInfo(defaultWindow,smapGameStage.rescource,smapGameStage.selectedArmy);
    }


    //电脑
    class InputProcessorEvent implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            if(ifBanOperation||smapGameStage==null){return false;}
            if(smapGameStage!=null&&smapGameStage.ifFixed){return false;}

            if(smapGameStage.isEditMode(true)){
                if (keycode == Input.Keys.C) {
                    if(smapGameStage.rescource.drawType<2){//复制兵种
                        smapGameStage.copyArmy=smapGameStage.selectedArmy;
                    }else {//复制归属
                        smapGameStage.copyRegionLi=smapGameStage.selectedRLegionIndex;
                    }
                }
                if (keycode == Input.Keys.V) {
                    if(smapGameStage.rescource.drawType<2){//粘贴兵种,不会复制将军信息
                        smapGameStage.pasteArmy(smapGameStage.coord.getId());
                    }else {//粘贴归属
                        smapGameStage.getsMapDAO().setRegionAllLegionIndex(smapGameStage.selectedRegionId,smapGameStage.copyRegionLi,true);
                    }
                }
                if (keycode == Input.Keys.R) {//铁路
                    Fb2Map.MapHexagon mapHexagon=smapGameStage.getsMapDAO().getHexagonData(smapGameStage.coord.id);
                    if(mapHexagon!=null){
                        if(mapHexagon.getOtherTile()==0){//粘贴兵种,不会复制将军信息
                            mapHexagon.setOtherTile(1);
                        }else {//粘贴归属
                            mapHexagon.setOtherTile(0);
                        }
                        smapGameStage.getsMapDAO().updRailwayRailwayId(mapHexagon.getHexagonIndex(),0);
                    }
                }
                /*if (keycode == Input.Keys.T) {//预设铁路
                    Fb2Map.MapHexagon mapHexagon=smapGameStage.getsMapDAO().getHexagonData(smapGameStage.coord.id);
                    if(mapHexagon!=null){
                        if(mapHexagon.getPresetRailway()==1){//粘贴兵种,不会复制将军信息
                            mapHexagon.setPresetRailway(0);
                        }else {//粘贴归属
                            mapHexagon.setPresetRailway(1);
                        }
                        smapGameStage.getsMapDAO().updRailwayRailwayId(mapHexagon.getHexagonIndex(),1);
                    }
                }*/
            }

            if (keycode == Input.Keys.HOME) {
                game.asyncManager.clearTask();
                game.showGameScreen(screenId,1);
            }
            if (keycode == Input.Keys.SPACE) {
                smapGameStage.selectedNextUnit();
            }



            if (keycode == Input.Keys.SHIFT_LEFT) {
                if(smapGameStage.gameOperate==GameOperateState.noOperate){
                    //当手指按下左shift时进入
                    if(smapGameStage.getsMapDAO().masterData.getPlayerMode()!=2){
                        smapGameStage.gameOperate=GameOperateState.selectMultipleUnitToAct;
                    }
                    Gdx.app.log("keyDown",keycode+":"+ smapGameStage.gameOperate);
                    //smapGameStage.selectedHexagonIds.clear();
                    //smapGameStage.clickMultipleUnitToAct();
                }
            }



            /*if (keycode == Keys.BACK) {
                // 处理返回事件
            } else if (keycode == Keys.MENU) {
                // 处理菜单事件
            }*/
            return true;  // 如果此处设置为false那么不会执行key up
        }

        @Override
        public boolean keyUp(int keycode) {
            if(ifBanOperation){return false;}
            if(smapGameStage!=null&&smapGameStage.ifFixed){return false;}
            if (keycode == Input.Keys.SHIFT_LEFT) {
                if(smapGameStage.gameOperate==GameOperateState.selectMultipleUnitToAct){
                    //当手指松开shift时进入
                    smapGameStage.gameOperate=GameOperateState.noOperate;
                    smapGameStage.resetDefaultState();
                    smapGameStage.restorePlayerArmyActorState();
                    Gdx.app.log("keyUp",keycode+":"+ smapGameStage.gameOperate);
                }

            }

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
            /*if(ifBanOperation){return false;}
            if(smapGameStage!=null&&smapGameStage.ifFixed){return false;}
            if(smapGameStage.gameOperate==GameOperateState.noOperate){
                if (button == Input.Buttons.RIGHT) {
                    int rx=smapGameStage.cam.getSX()+screenX;
                    int ry=smapGameStage.cam.getEY()-screenY;
                    int sx=smapGameStage.cam.getSX();
                    int sy=smapGameStage.cam.getSY();
                    int ex=smapGameStage.cam.getEX();
                    int ey=smapGameStage.cam.getEY();



                    Gdx.app.log("right click",rx+":"+ry);
                    //更新当前点击位置
                   // smapGameStage.updClick(rx,ry);
                    //更新坐标
                   // showClickInfo();
                    game.playSound(ResDefaultConfig.Sound.选择);
                }
            }*/
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            if(ifBanOperation){return true;}
            cx=screenX;
            cy=screenY;
            return true;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            if(ifBanOperation||smapGameStage==null){return false;}
            if(smapGameStage!=null&&smapGameStage.ifFixed){return false;}
            if(smapGameStage!=null&&smapGameStage.cam==null){
                return false;
            }

            if(amountY!=0&&(smapGameStage.gameOperate.ifNeedRestore())){
                smapGameStage.resetDefaultState();
            }
            if(amountY>0) {
                smapGameStage.cam.setZoomChangeForMouse(cx,cy,0.3f*amountY);
                return true;
            }else if(amountY<0){
                smapGameStage.cam.setZoomChangeForMouse(cx,cy,0.3f*amountY);
                return true;
            }
            //Gdx.app.log("", mapActor.getZoom()+"");
            return false;
        }
    }
    //触摸屏 手势


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
            Gdx.app.log("pan", "x:" + x+" y:" + y);
          //  smapGameStage.cam.translateCam(-deltaX * oldCurrentZoom,deltaY * oldCurrentZoom,true);
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            Gdx.app.log("panStop", "x:" + x+" y:" + y);
        //   oldCurrentZoom =   smapGameStage.cam.getZoom();
            return false;
        }
        //当用户执行缩放手势时调用。原始距离为手势时的距离，以像素为单位开始
        // @param initialDistance手势开始时手指之间的距离。
        //@param distance当前手指之间的距离。
        @Override
        public boolean zoom (float initialDistance, float distance){
            if(ifBanOperation){return false;}
            if(smapGameStage==null||smapGameStage.ifFixed){return false;}
            /*if(oldCurrentDistance==0){
                smapGameStage.cam.setZoomChangeForMouse(cx,cy,0.006f*(originalDistance-currentDistance));
            }else{
                smapGameStage.cam.setZoomChangeForMouse(cx,cy,0.006f*(oldCurrentDistance-currentDistance));
            }
            oldCurrentDistance=currentDistance;*/
         //   smapGameStage.cam.setZoomChangeForMouse(cx,cy,((initialDistance / distance)*smapGameStage.cam.getZoom()-smapGameStage.cam.getZoom()) );
            if(initialDistance> distance){
                smapGameStage.cam.setZoomChangeForMouse(cx,cy,ComUtil.max((initialDistance / distance)*smapGameStage.cam.getZoom()-smapGameStage.cam.getZoom(),0.1f) );
            }else {
                smapGameStage.cam.setZoomChangeForMouse(cx,cy,ComUtil.max((initialDistance / distance)*smapGameStage.cam.getZoom()-smapGameStage.cam.getZoom(),-0.1f) );
            }


            if(initialDistance!=0&&distance!=0&&(smapGameStage.gameOperate.ifNeedRestore())){
                smapGameStage.resetDefaultState();
            }

            Gdx.app.log("zoom", "oldCurrentDistance:" + oldCurrentDistance+" distance:" + distance+" zoom:"+ smapGameStage.cam.getZoom());
            ifZoom=true;
            return false;
        }

        @Override //当用户执行缩放手势时调用。报告两个涉及手指的初始位置和他们的当前位置
        public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){
            Gdx.app.log("pinch", "");
            cx=Math.abs(firstPointer.x-secondPointer.x)/2;
            cy=Math.abs(firstPointer.y-secondPointer.y)/2;
            return false;
        }
        @Override //停止缩放
        public void pinchStop () {
            Gdx.app.log("pinchStop", "");
            ifZoom=false;
            oldCurrentDistance=0;

        }

    }


    private void handleInput() {
        if(ifBanOperation){return ;}


        if(Gdx.input.isKeyJustPressed(Input.Keys.MENU)||Gdx.input.isKeyJustPressed(Input.Keys.HOME)){//退出
            ifReturn();
            return;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)||Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){//点击返回键
            screenBack();
            return;
        }


        if(ifZoom){return;}
        if (ifInputVisable&&Gdx.input.isKeyJustPressed(Input.Keys.T)&& (ResDefaultConfig.ifDebug||ResDefaultConfig.ifTest ) ) {
            ifInputVisable=false;
        /*boolean  rs= smapGameStage.cam.ifTest;
            smapGameStage.cam.ifTest=!rs;
            Gdx.app.log("test", "rs:"+smapGameStage.cam.ifTest);

            int i = 0,  j = 0,id,iMax = smapGameStage.cam.cw,jMax=smapGameStage.cam.ch;
            int x,y;
            for (; j<jMax; j++) {
                for (i=0;i<iMax; i++) {
                    x = smapGameStage.cam.csx + i;
                    y = smapGameStage.cam.csy + j;
                    id = x + y * smapGameStage.getMapW();
                     Gdx.app.log("setColor","id:"+id+" x:"+x+" y:"+y);
                }
            }
*/
            Gdx.input.getTextInput(listener, "Console", instruct,"");
        }


        if(tempActor!=null){
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A)) {
                tempActor.setX(tempActor.getX()-1f);
                tempW=tempW-1f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D)) {
                tempActor.setX(tempActor.getX()+1f);
                tempW=tempW+1f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)||Gdx.input.isKeyPressed(Input.Keys.S)) {
                tempActor.setY(tempActor.getY()-1f);
                tempH=tempH-1f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)||Gdx.input.isKeyPressed(Input.Keys.W)) {
                tempActor.setY(tempActor.getY()+1f);
                tempH=tempH+1f;   //  Gdx.app.log("UP","testY:"+smapGameStage.testActor.testY);
            }

          /*  if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
               if(smapGameStage.cam!=null){
                   smapGameStage.cam.getCamera().position.z++;
               }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                if(smapGameStage.cam!=null){
                    smapGameStage.cam.getCamera().position.z--;
                }
            }*/
            /*if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
                tempActor.setScale(tempActor.getScaleX()+0.1f);

            }

            if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
                tempActor.setScale(tempActor.getScaleX()-0.1f);
            }*/
        }


        if(smapGameStage.ifFixed){return ;}

        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)) {
            tempX=tempX-1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)) {
            tempX=tempX+1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)) {
            tempY=tempY+1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
            tempY=tempY-1f;
        }



        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A)) {
            //windowGroups.get(7).setX( windowGroups.get(7).getHX()-1);
            if(tempActor!=null){
                tempActor.setX(tempActor.getX()-1f);
                tempW=tempW-1f;
            }else{
                smapGameStage.cam.translateCam(-20*smapGameStage.cam.getZoom()*moveZoom,0,false);
            }

            //smapGameStage.testActor.testX=smapGameStage.testActor.testX-0.1f;
            //    Gdx.app.log("LEFT","testX:"+smapGameStage.testActor.testX);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D)) {
            //  windowGroups.get(7).setX( windowGroups.get(7).getHX()+1);
            if(tempActor!=null){
                tempActor.setX(tempActor.getX()+1f);
                tempW=tempW+1f;
            }else{
                smapGameStage.cam.translateCam(20*smapGameStage.cam.getZoom()*moveZoom,0,false);
            }


            //smapGameStage.testActor.testX=smapGameStage.testActor.testX+0.1f;
            //    Gdx.app.log("RIGHT","testX:"+smapGameStage.testActor.testX);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)||Gdx.input.isKeyPressed(Input.Keys.S)) {
            //  windowGroups.get(7).setY( windowGroups.get(1).getHY()+1);
            // Gdx.app.log("l","x:"+windowGroups.get(7).getHX()+" y:"+windowGroups.get(7).getHY());
            //smapGameStage.testActor.testY=smapGameStage.testActor.testY+0.1f;
            if(tempActor!=null){
                tempActor.setY(tempActor.getY()-1f);
                tempH=tempH-1f;
            }else{
                smapGameStage.cam.translateCam(0,-20*smapGameStage.cam.getZoom()*moveZoom,false);
            }



            //    Gdx.app.log("DOWN","testY:"+smapGameStage.testActor.testY);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)||Gdx.input.isKeyPressed(Input.Keys.W)) {
            //   windowGroups.get(7).setY( windowGroups.get(7).getHY()-1);
            // Gdx.app.log("l","x:"+windowGroups.get(7).getHX()+" y:"+windowGroups.get(7).getHY());
            // smapGameStage.testActor.testY=smapGameStage.testActor.testY-0.1f;
            if(tempActor!=null){
                tempActor.setY(tempActor.getY()+1f);
                tempH=tempH+1f;
            }else{
                smapGameStage.cam.translateCam(0,20*smapGameStage.cam.getZoom()*moveZoom,false);
            }
            //  Gdx.app.log("UP","testY:"+smapGameStage.testActor.testY);
        }

        float screenX=Gdx.input.getX(0);
        float screenY= Gdx.input.getY(0);
        if(game.gameConfig.ifMoveInFullScreen&&smapGameStage!=null){

            //Gdx.app.log("mouseMoved",screenX+":"+screenY);
            if(screenX<10){//左
                smapGameStage.cam.translateCam(-20*smapGameStage.cam.getZoom()*moveZoom,0,false);
            }else if(screenX>uiStage.getWidth()-10){//右
                smapGameStage.cam.translateCam(20*smapGameStage.cam.getZoom()*moveZoom,0,false);
            }
            if(screenY<10){//上
                smapGameStage.cam.translateCam(0,20*smapGameStage.cam.getZoom()*moveZoom,false);
            }else if(screenY>uiStage.getHeight()-10){//下
                smapGameStage.cam.translateCam(0,-20*smapGameStage.cam.getZoom()*moveZoom,false);
            }
        }

        // justTouched 是开始按下手指的第一个点。
        if (Gdx.input.justTouched() && isTouching == false) {
            isTouching = true;
            touchBaseX = screenX;
            touchBaseY = screenY;
            //touchBaseX += cam.position.x - GAMESTAGE_WIDTH / 2;
            //Gdx.app.log("触摸", "1");

            // isTouched 是结束时，手指按下的点。
        } else if (Gdx.input.isTouched(0) && isTouching == true) {
            touch_X = screenX;
            touch_Y = screenY;
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
            if((Math.abs(moveX)>1||Math.abs(moveY)>1)&&(smapGameStage.gameOperate.ifNeedRestore())){
                smapGameStage.resetDefaultState();
            }/*else{
                Gdx.app.error("move",moveX+":"+moveY);
            }*/
            smapGameStage.cam.translateCam(moveX*smapGameStage.cam.getZoom()*moveZoom, moveY*smapGameStage.cam.getZoom()*moveZoom,false);
        }else {
            isTouching =false;
        }
           /*
           if (Gdx.input.isKeyPressed(Input.Keys.A)) {
             mapActor.setZoom(mapActor.getZoom()+0.0001f,cx,cy);
           }
           if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
               mapActor.setZoom(mapActor.getZoom()-0.0001f,cx,cy);
           }*/

        /**/

        /*if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            //ifBegin=true;
            smapGameStage.actForRound();
        }*/

    }
    //弹框并询问是否返回
    private void ifReturn() {
        dialogueFunctionIndex=1;
        game.gameLayout.updSmapDialogueGroup(windowGroups.get(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId),0,2,0);
    }

    //返回上一层
    private void screenBack() {
        if(smapGameStage.getsMapDAO().roundState==0){
            //如果有未关闭的窗口,直接关闭,否则进入待机状态
            for(int i=1;i<windowGroups.size-1;i++){
                WindowGroup w=windowGroups.get(i);
                if(w.isVisible()){
                    hidUi(true);
                    smapGameStage.resetDefaultState();
                    return;
                }
            }
            showPauseGroup();ifWarp=true;
            smapGameStage.resetDefaultState();
            return;
        }

    }


    //征服使用 选择国家
    public void showCountryToSelected(){
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId);
        Fb2Smap.LegionData l=smapGameStage.getSelectLegionData();
        if(w.isVisible()||l.getLegionIndex()==smapGameStage.getPlayerLegionIndex()){
            w.setVisibleForEffect(false);
        }else{
            w.setVisibleForEffect(true);
            game.gameLayout.updSmapSelectCountryGroup(w,smapGameStage.getsMapDAO(),l);
        }
        dialogueFunctionIndex=0;
        //Gdx.app.log("showCountryToSelected",countryId+":"+legionIndex+":"+i+":"+windowGroups.get(i).isVisible());
        //初始教程
        if(game.resGameConfig.ifPromptFirstOperate&&smapGameStage.getsMapDAO().masterData.getBtlType()==0&&!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstSelectCountry)){
            addAssistantDialogue("prompt_dialogue_53",false);
            game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstSelectCountry);
        }
    }

    public void hiddenWindowGroup(int i){
        windowGroups.get(i).setVisibleForEffect(false);
    }
    public void showWindowGroup(int i){
        windowGroups.get(i).setVisibleForEffect(true);
    }


    //展示资源条以及基础按钮等
    public void initDefaultWindowForResource(){
        showRegionResourceInfo();
        hidRightBorderInfo();
        initForGameDefaultWindow();
        /*if(smapGameStage.isEditMode(false)){
            initForEditDefaultWindow();
        }else {

        }*/
        if(Gdx.app.getType().equals(Application.ApplicationType.Desktop)){
            defaultWindow.hidImage(2050);
            defaultWindow.hidImage(2051);
        }else {
            game.gameLayout.initDefaultGroupForSlide(defaultWindow,this);
        }
    }


    private void initForGameDefaultWindow() {
        defaultWindow.showImage(0);
        defaultWindow.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(smapGameStage.getPlayerLegion().getCountryId())));
        //设置是否显示难度
        if(smapGameStage.getsMapDAO().masterData.getBtlType()==0){//征服
            if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
                defaultWindow.setButtonImageNotChangeSize(10,game.getImgLists().getTextureByName(DefDAO.getImageNameFlagBorderB(smapGameStage.getsMapDAO().masterData.getGameDifficulty())).getTextureRegion());
                defaultWindow.resetButtonPotion(10,0,100,150,150,84,-75,false);
            }else  if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==1){
                defaultWindow.setButtonImageNotChangeSize(10,game.getImgLists().getTextureByName(DefDAO.getImageNameFlagBorderByDifficulty(smapGameStage.getsMapDAO().masterData.getGameDifficulty())).getTextureRegion());
                defaultWindow.resetButtonPotion(10,0,100,150,150,52,-52,false);
            }else{
                if(smapGameStage.isEditMode(false)){
                    defaultWindow.setButtonImageNotChangeSize(10,game.getImgLists().getBlankRegionDAO(DefDAO.getImageNameFlagBorderByDifficulty(smapGameStage.getsMapDAO().masterData.getGameDifficulty())).getTextureRegion());
                    defaultWindow.resetButtonPotion(10,0,100,150,150,52,-52,false);
                }else{
                    defaultWindow.hidButton(10);
                }
            }
        }else {
            if(smapGameStage.isEditMode(false)){
                defaultWindow.setButtonImageNotChangeSize(10,game.getImgLists().getBlankRegionDAO(DefDAO.getImageNameFlagBorderByDifficulty(smapGameStage.getsMapDAO().masterData.getGameDifficulty())).getTextureRegion());
                defaultWindow.resetButtonPotion(10,0,100,150,150,52,-52,false);
            }else{
                defaultWindow.hidButton(10);
            }
        }
        defaultWindow.showButton(0);
        defaultWindow.showButton(1);
        defaultWindow.showButton(2);
        defaultWindow.showButton(7);
        defaultWindow.showButton(11);
        defaultWindow.showButton(17);
        //征服下的 战斗模式 不能使用任务
        if(smapGameStage.isEditMode(false)){
            defaultWindow.hidButton(18);
        }else if(smapGameStage.getsMapDAO().ifSystemEffective(15)){
            defaultWindow.showButton(18);
        }else{
            if(smapGameStage.getsMapDAO().masterData.getBtlType()==0&&smapGameStage.getsMapDAO().stageId!=0){
                defaultWindow.showButton(18);
            }else{
                defaultWindow.hidButton(18);
            }
        }
        defaultWindow.showButton(19);
        defaultWindow.showButton(22);
        defaultWindow.showButton(23);
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId);
        if(smapGameStage.isEditMode(true)){
            w.showButton(0);
            w.showButton(1);
        }else {
            w.hidButton(0);
            w.hidButton(1);
        }
        if(smapGameStage.isEditMode(false)){
            defaultWindow.hidButton(24);
        }else{
            if(smapGameStage.getPlayerLegion().getMoney()==0){
                defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai").getTextureRegion());
            }else {
                defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai2").getTextureRegion());
            }
        }

        if(!smapGameStage.isEditMode(false)&&smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
            defaultWindow.showButton(14);
            defaultWindow.showButton(15);
            defaultWindow.showButton(16);
            defaultWindow.showButton(21);
            //针对霸权王权显示
            //defaultWindow.showButton(21);
            defaultWindow.showImage(2);
            defaultWindow.showImage(3);
            defaultWindow.setButtonImage(20,game.getImgLists().getTextureByName("g_"+smapGameStage.getPlayerLegion().getMedal()).getTextureRegion(),1.5f);
            game.gameLayout.updAmbitionForSMapScreen(defaultWindow,smapGameStage.getsMapDAO());

            defaultWindow.resetImagePotion(0,0,100,0,0,294,-75,false);
            defaultWindow.resetButtonPotion(7,0,100,0,0,294,-75,false);
        }

        defaultWindow.showLabel(4);
        defaultWindow.showLabel(5);

        if(smapGameStage.getsMapDAO().masterData.getPlayerMode()!=0){
            //重置人口标签位置
            defaultWindow.resetImagePotion(4,50,100,0,0,0,-102,false);
            defaultWindow.resetLabelPotion(6,50,100,0,0,0,-103,false);
            Image image=   defaultWindow.getImage(4);
            Label label=defaultWindow.getLabel(6);
            image.setY(image.getY()+52);
            label.setY(label.getY()+52);
            defaultWindow.setImageRegion(0,game.getImgLists().getTextureByName("resource_bar").getTextureRegion());

            defaultWindow.resetImagePotion(0,0,100,0,0,384,-52,false);
            defaultWindow.resetImagePotion(1,0,100,150,150,52,-52,false);
            defaultWindow.replaceButtonImage(7,game.getImgLists().getBlankRegionDAO("resource_bar"));
            defaultWindow.resetButtonPotion(7,0,100,0,0,384,-52,false);
            defaultWindow.resetLabelPotion(0,0,100,0,0,181,-52,false);
            defaultWindow.resetLabelPotion(1,0,100,0,0,324,-52,false);
            defaultWindow.resetLabelPotion(2,0,100,0,0,463,-52,false);
            defaultWindow.resetLabelPotion(3,0,100,0,0,600,-52,false);

            defaultWindow.hidLabel(4);
            defaultWindow.hidLabel(5);
        }

        defaultWindow.showImage(4);

        if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
            defaultWindow.setButtonImageNotChangeSize(13,game.getImgLists().getTextureByName("circleButton_ok").getTextureRegion());
            hidUnitBackButton();
        }
    }



    public int getMode(){
        return smapGameStage.getsMapDAO().controlMode;
    }

    //更新玩家的资源条数据显示
    public void updResourceForPlayer(){

        Fb2Smap.LegionData l= smapGameStage.getsMapDAO().getPlayerLegionData();


        if(l.isThanMoneyMax()){
            defaultWindow.setLabelText(0,l.getMoney()+"",Color.RED);
        }else {
            defaultWindow.setLabelText(0,l.getMoney()+"",Color.WHITE);
        }
        if(l.isThanIndustryMax()){
            defaultWindow.setLabelText(1,l.getIndustry()+"",Color.RED);
        }else {
            defaultWindow.setLabelText(1,l.getIndustry()+"",Color.WHITE);
        }
        if(l.isThanTechMax()){
            defaultWindow.setLabelText(2,l.getTech()+"",Color.RED);
        }else {
            defaultWindow.setLabelText(2,l.getTech()+"",Color.WHITE);
        }
        if(l.isThanFoodMax()){
            defaultWindow.setLabelText(3,l.getFood()+"",Color.RED);
        }else {
            defaultWindow.setLabelText(3,l.getFood()+"",Color.WHITE);
        }

        if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
            //如果产出小于工业
            if(l.getMineral()<l.incomeIndustry){
                defaultWindow.setLabelText(4,l.getMineral()+"",Color.RED);
            }else{
                if(l.isThanMineralMax()){
                    defaultWindow.setLabelText(4,l.getMineral()+"",Color.RED);
                }else {
                    defaultWindow.setLabelText(4,l.getMineral()+"",Color.WHITE);
                }
            }
            if(smapGameStage.getsMapDAO().getAge()>0){
                if(l.getOil()<0){
                    defaultWindow.setLabelText(5,l.getOil()+"",Color.RED);
                }else{
                    if(l.isThanOilMax()){
                        defaultWindow.setLabelText(5,l.getOil()+"",Color.RED);
                    }else {
                        defaultWindow.setLabelText(5,l.getOil()+"",Color.WHITE);
                    }
                }
            }else{
                defaultWindow.setLabelText(5,"-",Color.WHITE);
            }

        }
        int pn=l.getPopulationNow();
        int pm=l.getPopulationMax();
        if(pn>pm){
            defaultWindow.setLabelText(6,pn+"/"+pm,Color.RED);
        }else{
            defaultWindow.setLabelText(6,pn+"/"+pm,Color.WHITE);
        }
        game.gameLayout.updAmbitionForSMapScreen(defaultWindow,smapGameStage.getsMapDAO());
        smapGameStage.updAllArmyActorUpdMark();
    }
    //显示区域资源
    public void updResourceForRegion(){
        Fb2Smap.BuildData selectBuildData= smapGameStage.selectedBuildData;
        if(selectBuildData==null){
            return;
        }

        setWeatherAnimationEffect(selectBuildData.getWeatherId());
        lastWeatherId=selectBuildData.getWeatherId();
        defaultWindow.setVisible(true);
        //  w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameByRegionType(selectBuildData)));

        int rate= smapGameStage.getsMapDAO().getAllRate(selectBuildData, smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex));
        Fb2Smap.LegionData l=selectBuildData.getLegionData();
        defaultWindow.setLabelText(1000,selectBuildData.getCityLvNow()+"");
        defaultWindow.setLabelText(1001,selectBuildData.getIndustryLvNow()+"");
        defaultWindow.setLabelText(1002,selectBuildData.getDefenceLvNow()+"");
        defaultWindow.setLabelText(1003,selectBuildData.getMissileLvNow()+"");
        defaultWindow.setLabelText(1004, selectBuildData.getIncomeMoney(rate)+"");
        defaultWindow.setLabelText(1005,selectBuildData.getIncomeIndustry(rate)+"");
        defaultWindow.setLabelText(1006,selectBuildData.getIncomeTech(rate)+"");

        if(!selectBuildData.isSeaArea()&&selectBuildData.ifHunger()){
            defaultWindow.setLabelText(1007,selectBuildData.getIncomeFood(rate)+"",Color.RED);
        }else{
            defaultWindow.setLabelText(1007,selectBuildData.getIncomeFood(rate)+"",Color.WHITE);
        }
        defaultWindow.setLabelText(1008,selectBuildData.getAirCount()+"");
        defaultWindow.setLabelText(1009,selectBuildData.getNuclearCount()+"");

        defaultWindow.setLabelText(1010,selectBuildData.getBuildReplyHp()+"");

        defaultWindow.setLabelText(1011,selectBuildData.getBuildRound()+"");

        if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
            //矿石
            defaultWindow.setLabelText(1012,selectBuildData.getIncomeMinterial(rate)+"");
            if(smapGameStage.getsMapDAO().getAge()>0){
                //油田
                defaultWindow.setLabelText(1013,selectBuildData.getIncomeOil(rate)+"");
            }else{
                defaultWindow.setLabelText(1013,"-");
            }
        }else{
            defaultWindow.setLabelText(1012,"-");
            defaultWindow.setLabelText(1013,"-");
        }


        // promptText.setVisible(false);

        if(smapGameStage.getsMapDAO().ifAllyByLi(selectBuildData.getLegionIndex(), smapGameStage.getPlayerLegionIndex())){
            defaultWindow.setImageRegion(1002,game.getImgLists().getTextureByName("region_hpbar_green"));
        }else {
            defaultWindow.setImageRegion(1002,game.getImgLists().getTextureByName("region_hpbar_red"));
        }
        float width=defaultWindow.getImage(1015).getWidth();
        // w.setImageRegionWidth(3,selectBuildData.getHpRateF()*width);
        if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
            defaultWindow.setImageRegionWidth(1001,selectBuildData.getCityStabilityRate()*width,1f);
        }else{
            defaultWindow.hidImage(1001);
        }

        defaultWindow.setImageRegionWidth(1002,selectBuildData.getHpRateF()*width,1f);
        // w.setImageRegion(4,game.getImgLists().getTextureByName("g_"+l.getInternMedal()).getTextureRegion());
        // w.setImageRegionNotChangeSize(4,game.getImgLists().getTextureByName("flag_"+l.getInternCounctry()).getTextureRegion());
        //边缘设定
        // w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName("flagBorder_1").getTextureRegion());

        //w.setImageRegion(1,game.getImgLists().getTextureByName("flag_"+l.getCountryId()).getTextureRegion());
        TextureRegionDAO r=game.getImgLists().getTextureByName("icon_weather"+selectBuildData.getWeatherId());
        //势力圈国家
        if(selectBuildData.getInfluenceLi()>0&&selectBuildData.getLegionIndex()!=selectBuildData.getInfluenceLi()&&selectBuildData.getSuzLegionIndex()!=selectBuildData.getInfluenceLi()){
            defaultWindow.setButtonImageNotChangeSize(1002,game.getImgLists().getTextureByName("flag_"+selectBuildData.getInfluenceLegionData().getCountryId()).getTextureRegion());
        }else {
            defaultWindow.hidButton(1002);
        }
        if(r==null){
            defaultWindow.hidButton(1000);
        }else{
            defaultWindow.setButtonImageNotChangeSize(1000,r.getTextureRegion());
        }
        // w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName(DefDAO.getImageNameByRegionType(selectBuildData)));
        defaultWindow.setButtonImageNotChangeSize(1001,game.getImgLists().getTextureByName(DefDAO.getImageNameByRegionType(selectBuildData)).getTextureRegion());
    }


    public void hidUi(Boolean ifExecuteScripts){
        //windowGroups.get(ResConfig.Class.SMapScreenGameCardGroupId).setVisible(false);
        //windowGroups.get(ResConfig.Class.SMapScreenPromptGroupId).setVisible(false);
        progressCountryImage.setVisible(false);
        closeOtherGroupExceptSpecified(0);
        smapGameStage.setIfFixed(false);
        selectCardType=-1;
        if(selectCardPotion!=0){
            smapGameStage.resetDefaultState();
            selectCardPotion=0;
        }
        if(defaultWindow!=null){
            defaultWindow.setVisible(true);
        }

        if(ifExecuteScripts){
            executeScripts(false);
        }
        promptText.setVisible(false);
        ifWarp=false;
        updButtonPrompt();

        //初始教程
        if(game.resGameConfig.ifPromptFirstOperate&&smapGameStage.getsMapDAO().masterData.getBtlType()==0){
            if(smapGameStage.getsMapDAO().ifSystemEffective(1)&&!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstMainAndMineralUse)){
                addAssistantDialogue("prompt_dialogue_43",false);
                game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstMainAndMineralUse);
            }
            if(smapGameStage.getsMapDAO().ifSystemEffective(2)&&!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstMainAndOilUse)){
                addAssistantDialogue("prompt_dialogue_44",false);
                game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstMainAndOilUse);
            }
            if(smapGameStage.getsMapDAO().ifSystemEffective(3)&&!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstMainAndAmmoUse)){
                addAssistantDialogue("prompt_dialogue_45",false);
                game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstMainAndAmmoUse);
            }
            if(smapGameStage.getsMapDAO().ifSystemEffective(7)&&!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstMainAndEnergyUse)){
                addAssistantDialogue("prompt_dialogue_59",false);
                game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstMainAndEnergyUse);
            }
        }
    }


    public void hidGeneralDialogueGroup(Boolean ifExecuteScripts){
        progressCountryImage.setVisible(false);
        smapGameStage.setIfFixed(false);
        windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId).setVisible(false);
        windowGroups.get(ResDefaultConfig.Class.SMapScreenIntroductionGroupId).setVisible(false);
        if(defaultWindow!=null){
            defaultWindow.setVisible(true);
        }
        if(ifExecuteScripts){
            executeScripts(false);
        }
        promptText.setVisible(false);
        ifWarp=false;
        updButtonPrompt();

    }

    /*
      显示主要游戏弹框,这几个弹框为互斥关系 如果要全部关闭不打开则输入 0,true
       cardType  //-1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标 18定制战舰 19奇物 -7全部可招募单位
        page //展示的页数
     */
    public void showCardUi(int cardType){
        hidCommonUi();
        int buildType;

        if(smapGameStage.getSelectBuildData()==null&&cardType!=11){hidUi(false);return;}
        //如果是显示全部单位类型,则判断是否有海军
            if(cardType==-2) {
                if (smapGameStage.getSelectBuildData().getFacilityHexagon(1) == -1) {
                    cardType = -7;
                }
            }
        if(cardType==11){
            buildType=0;
        }else {
            buildType = smapGameStage.getSelectBuildData().getBuildType();
        }

        //建筑类型 -0城市 1海港 2海洋(海洋不能建造) 3临海城市
        if(buildType==2&&!smapGameStage.isEditMode(true)){
            hidUi(false); return;}
        closeOtherGroupExceptSpecified(ResDefaultConfig.Class.SMapScreenGameCardGroupId);
        WindowGroup window= windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId);
        if(smapGameStage.isEditMode(false)&&cardType==12){
            window.showButton(19);
        }else{
            window.hidButton(19);
        }
        if(cardType>=1||cardType<=8){
            game.gameLayout.updCardUIForLabelPotion(window,cardType);
        }

        Fb2Smap.BuildData b= smapGameStage.selectedBuildData;
        Fb2Smap.LegionData l= smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex);
        Fb2Smap.LegionData pl=smapGameStage.getPlayerLegion();
        pl.resetLegionCanBuildCardE();
        int pli=smapGameStage.getPlayerLegion().getLegionIndex();
        if(l!=null&&smapGameStage.isEditMode(true)){
            pli=l.getLegionIndex();
        }
        if(cardType==selectCardType){

        }else if(cardType==12){//如果是外交,则获取外交卡
            Fb2Smap.ForeignData f=smapGameStage.getsMapDAO().getForeignData( smapGameStage.selectedRLegionIndex);
            //Fb2Smap smapDAO, Fb2Smap.LegionData legionDataA,Fb2Smap.LegionData legionDataB, Array<XmlReader.Element> filterCardE, Array<XmlReader.Element> rs
            if(f!=null&&(smapGameStage.getPlayerLegion().getSuzerainLi()==0|| smapGameStage.getPlayerLegion().getSuzerainLi()== smapGameStage.selectedRLegionIndex)){
                haveCardEList=GameMethod.getForeignCardE(smapGameStage.getsMapDAO(), smapGameStage.getPlayerLegion(), smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex), smapGameStage.getPlayerLegion().varLegionCanBuildCardE,haveCardEList);
                if(smapGameStage.getsMapDAO().ifAllyPlayerByLi(smapGameStage.selectedRLegionIndex)){
                    GameUtil.removeXmlEById(haveCardEList,3103);
                }
                //设定势力圈
                if(b.getLegionIndex()!=0&&b.getInfluenceLi()!=pli&&b.ifBorderLi(pli,false)&&smapGameStage.getsMapDAO().getForeignDegree(b.getLegionIndex(),pli)!=1&&smapGameStage.getPlayerLegion().getAllArmySumNum()>b.getLegionData().getAllArmySumNum()){
                    haveCardEList.add(game.gameConfig.getDEF_CARD().getElementById(3115));
                }
                if(smapGameStage.getPlayerLegion().getSuzerainLi()!=0&&smapGameStage.getPlayerLegion().getSuzerainLi()== smapGameStage.selectedRLegionIndex){
                    haveCardEList.add(game.gameConfig.getDEF_CARD().getElementById(3116));
                }
                //购买奇物
                if(smapGameStage.getsMapDAO().ifSystemEffective(16)&&smapGameStage.getsMapDAO().getPlayerAmbition()!=100&&f!=null&&!f.isSelf()&&f.getFavorValue()>90&&smapGameStage.getsMapDAO().canGetSpirit(smapGameStage.selectedRLegionIndex)){
                    haveCardEList.add(game.gameConfig.getDEF_CARD().getElementById(3117));
                }
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0&&f.isAlly()&&f.getFavorValue()>=70&&smapGameStage.getSelectBuildData().getBuildRound()==0&&smapGameStage.getSelectBuildData().canPreorderNavy()){
                    haveCardEList.add(game.gameConfig.getDEF_CARD().getElementById(3118));
                }
                //移除求和,攻击协定等,增加撕毁条约
                if(f.getForeignType()>0){
                    GameUtil.removeXmlEById(haveCardEList,3110);
                    GameUtil.removeXmlEById(haveCardEList,3111);
                    GameUtil.removeXmlEById(haveCardEList,3112);
                    GameUtil.removeXmlEById(haveCardEList,3114);
                    haveCardEList.add(game.gameConfig.getDEF_CARD().getElementById(3119));
                }
            }else {
                haveCardEList.clear();
            }
        }else if(cardType==11){//如果是国策,则随机取n个数值
            //selectCardEList=GameMethod.getRandCardE(game, smapGameStage.getsMapDAO().publicLegionCanUpdTechId, ComUtil.limitValue(  smapGameStage.getsMapDAO().getPlayerLegionData().getRecruitGeneralLvNow()+3,3,8),smapGameStage.getsMapDAO().masterData.getRoundNow(),selectCardEList);
            //  selectCardEList=GameMethod.getRandCardE(game, smapGameStage.getsMapDAO().publicLegionCanUpdTechId, ComUtil.limitValue(  smapGameStage.getsMapDAO().getPlayerLegionData().getRecruitGeneralLvNow()+4,5,14),smapGameStage.getsMapDAO().masterData.getRoundNow(),selectCardEList);
            haveCardEList=GameMethod.getRandCardE(game,smapGameStage.getPlayerLegion(),b, smapGameStage.getsMapDAO().publicLegionCanUpdTechId, 99, smapGameStage.getsMapDAO().masterData.getRoundNow(),haveCardEList,false);

        }else if(smapGameStage.getsMapDAO().masterData.getPlayerMode()!=2&&(cardType==4||cardType==8)&&smapGameStage.selectedBuildData.getFacilityHexagon(1)==-1&&!smapGameStage.isEditMode(true)){
            haveCardEList.clear();
            haveCardEList.add(game.gameConfig.getDEF_CARD().getElementById(6001));
            cardType=15;
            selectCardType=cardType;
        }else if(cardType==17){//如果是地标
            haveCardEList=GameMethod.getWonderE(smapGameStage.getsMapDAO(), smapGameStage.getPlayerLegion(),b,-1,haveCardEList);
        }else  if(cardType==18){//定制战舰
            haveCardEList=GameMethod.getCardEByFilterCardE(smapGameStage.getsMapDAO(), smapGameStage.getPlayerLegion(),b,
                    4, false,smapGameStage.getPlayerLegion().varLegionCanBuildCardE,haveCardEList,false);
        }else  if(cardType==19){
            haveCardEList=GameMethod.getCanBuySpiritE(smapGameStage.getsMapDAO(), smapGameStage.getPlayerLegion(),b,haveCardEList);
        } else{
            //Fb2Smap smapDAO, Fb2Smap.BuildData build,int cardType, Array<XmlReader.Element> filterCardE,Array<XmlReader.Element> rs
            haveCardEList=GameMethod.getCardEByFilterCardE(smapGameStage.getsMapDAO(), smapGameStage.getPlayerLegion(),b,
                    cardType, cardType<0,smapGameStage.getPlayerLegion().varLegionCanBuildCardE,haveCardEList,false);

            if(smapGameStage.getsMapDAO().masterData.getPlayerMode()!=2&&((cardType==-7&&haveCardEList.size>0)||((cardType==4||cardType==8)&&smapGameStage.isEditMode(true)))&&!smapGameStage.selectedBuildData.ifSea() &&smapGameStage.selectedBuildData.getFacilityHexagon(1)==-1&&smapGameStage.selectedBuildData.canBuildNavy()  ){
                haveCardEList.add(game.gameConfig.getDEF_CARD().getElementById(6001));
            }
            if(cardType==9){//如果是民用建筑,判断是否要移除铁路
                if(!smapGameStage.getsMapDAO().ifSystemEffective(22)||!b.canBuildRailwayCard()||b.isWar()){
                    GameUtil.removeXmlEById(haveCardEList,2015);
                }
            }
            if( cardType==14){//可以在工事中修铁路
                if(smapGameStage.getsMapDAO().ifSystemEffective(22)&&b.getTransportLvNow()>0&&smapGameStage.selectedArmy!=null&&smapGameStage.selectedBuildData!=null&&smapGameStage.selectedBuildData.getLegionIndex()==smapGameStage.selectedArmy.getLegionIndex()){
                    Fb2Map.MapHexagon h=smapGameStage.selectedArmy.getHexagonData();
                    if(h!=null&&h.getBlockType()!=1&&h.getOtherTile()==0&&smapGameStage.selectedArmy.getArmyRank()>h.getMoveCost()/**/){
                        haveCardEList.add(game.gameConfig.getDEF_CARD().getElementById(2015));
                    }
                }
            }
            //工事中可以修铁路
        }
        //更新卡牌ui等级 TODO
        // updCardLabelLv();

        //设置位置
        //GameUtil.setCardWindowsCardPotion(game,window,selectCardEList,uiStage.getWidth(),uiStage.getHeight());
        //SMapGameStage smapGameStage,WindowGroup window, int cardType, int buildType

        //1.更新卡牌sum
        cardSumPage=(GameUtil.getArraySize(haveCardEList)-1)/7+1;
        if(cardNowPage==0||cardNowPage>cardSumPage){
            cardNowPage=1;
        }
        selectCardPotion=0;
        selectCardEList= GameMethod.getElementByPage(haveCardEList,selectCardEList,cardNowPage,7);
        selectCardType=cardType;
        game.gameLayout.updCardWindow( smapGameStage,window,smapGameStage.getsMapDAO().getLegionDataByLi(pli),b,selectCardEList,cardType,buildType,cardNowPage,cardSumPage);
        if(selectCardEList.size>0){
            selectedCard(1);
        }else {
            selectedCard(0);
        }
        //右下角显示关闭标签
        window.getButton(10).setVisible(true);
        smapGameStage.setIfFixed(true);
        window.setVisibleForEffect(true);


        //初始教程
        if(game.resGameConfig.ifPromptFirstOperate&&smapGameStage.getsMapDAO().masterData.getBtlType()==0){
            if(checkOpenGroup("openCardGroupForWonder",true)){//奇观
                if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenWonder)){
                    addAssistantDialogue("prompt_dialogue_42",false);
                       game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenWonder);
                }
            }else  if(checkOpenGroup("openCardGroupForDiplomacy",true)){//外交
                if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenDiplomacy)){
                    addAssistantDialogue("prompt_dialogue_51",false);
                        game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenDiplomacy);
                }
            }else if(checkOpenGroup("openCardGroupForLegionTech",true)){//科技
                if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstUpdTech)){
                    addAssistantDialogue("prompt_dialogue_55",false);
                       game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstUpdTech);
                }
            }
        }
    }

    private void selectedCard(int potion) {
        promptText.setVisible(false);
        if(selectCardType<100){
            if(potion>0&&potion-1<GameUtil.getArraySize(selectCardEList)){
                selectCardPotion=potion;
            }
        }else if(selectCardType==101||selectCardType==102){//单位选择加载核弹
            if(potion>0&&potion-1<selectNulList.size){
                selectCardPotion=potion;
            }
        }
        if(selectCardPotion==potion){
            game.gameLayout.selectedCard(smapGameStage,windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId),selectNulList,selectCardEList,selectCardType,potion);
        }
    }


    public void buyCardReady(){
        //WindowGroup w=windowGroups.get(ResConfig.Class.SMapScreenGameCardGroupId);
        //12,13加载核弹
        if(selectCardType==101){
            Fb2Smap.ArmyData a=smapGameStage.selectedArmy;
            Fb2Smap.NulcleData n=selectNulList.get(selectCardPotion-1);
            if(a.loadNul(n)){
                n.remove(1);
            }
            hidUi(false);
        }else  if(selectCardType==102){
            Fb2Smap.AirData a=selectAirList.get((selectListPage-1)*4+selectListIndex-1);
            Fb2Smap.NulcleData n=selectNulList.get(selectCardPotion-1);
            if(a.loadNul(n)){
                n.remove(1);
            }
            hidUi(false);
        }else if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
            smapGameStage.selectedTargetHexagon= smapGameStage.selectedRegionId;
            buyCard();
        }else{
            Element cardE=selectCardEList.get(selectCardPotion-1);
            int cardId=cardE.getInt("id")   ;
            int  cardType=cardE.getInt("type",selectCardType)   ;
            //建造位置为
            if( ((cardId==2015||(cardType==14&&smapGameStage.coord.getId()!=smapGameStage.selectedRegionId) )&&smapGameStage.selectedArmy!=null&&smapGameStage.selectedArmy.getArmyRound()==0)     ){
                smapGameStage.selectedTargetHexagon= smapGameStage.selectedArmy.getHexagonIndex();
                buyCard();
            }else if( (cardId==2015&& (smapGameStage.selectedBuildData!=null&&!smapGameStage.selectedBuildData.ifSea()&&smapGameStage.selectedBuildData.ifRegionHaveRailway() ))||cardType==6||cardType==15||  (cardType==14&&smapGameStage.selectedArmy!=null&&smapGameStage.selectedBuildData!=null&&smapGameStage.selectedArmy.getHexagonIndex()==smapGameStage.selectedBuildData.getRegionId())   ){//只有设施和要塞可以选择建造位置
                closeOtherGroupExceptSpecified(0);
                smapGameStage.setIfFixed(false);
                smapGameStage.showCanBuildHexagon(cardId,cardType);
            }else {
                smapGameStage.selectedTargetHexagon= smapGameStage.selectedRegionId;
                buyCard();
            }
        }
    }

    public void buyCard(){
        int index=selectCardPotion-1;
        if(index<0||index>selectCardEList.size){
            smapGameStage.resetDefaultState();
        }else {
            buyCard(selectCardEList.get(index));
        }
    }



    public void buyCard(Element  cardE){
        int cardId=cardE.getInt("id");
        this.selectCardId =cardId;
        int  cardType=cardE.getInt("type",selectCardType);

        Fb2Smap.LegionData legion= smapGameStage.getPlayerLegion();
        if(cardType!=12&&smapGameStage.isEditMode(true)){
            legion=smapGameStage.getSelectLegionData();
            if(legion==null){
                legion=smapGameStage.getPlayerLegion();
            }
        }
        int tLi= smapGameStage.selectedRLegionIndex;
        Fb2Smap.LegionData tLegion= smapGameStage.getsMapDAO().getLegionDataByLi(tLi);
        int selectCardChance=0; int favor=0;String effect;
        int money=0,industry=0,tech=0,food=0;

        {//-1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标
            smapGameStage.getsMapDAO().addHarmor(-1);
        }

        if(cardType==12){//外交卡牌显示的样式
            if(smapGameStage.getsMapDAO().ifHaveSpirit(15)&&cardId==3108&&smapGameStage.getsMapDAO().isSuzLegion(tLi,legion.getLegionIndex())){
                selectCardChance=100;
            }else if(cardId==3108){
                if(smapGameStage.getsMapDAO().checkRegionCanLegionMerge(legion,tLegion)){
                    selectCardChance=smapGameStage.getsMapDAO().getLegionMergeChance(tLi,legion.getLegionIndex());//获得合并概率
                }else{
                    selectCardChance=0;
                }
            }else if(cardId==3115){
                selectCardChance=smapGameStage.getsMapDAO().getSetInfluenceChance(smapGameStage.selectedBuildData,legion.getLegionIndex());//获得设定势力圈的概率
            }else if(cardId==3116){
                selectCardChance=smapGameStage.getsMapDAO().getIndependentChance(legion);//获得设定势力圈的概率
            }else{
                selectCardChance=cardE.getInt("tech",0);
            }
            if(cardE.getInt("priceType",0)==0){
              /*  money=ComUtil.limitValue(cardE.getInt("money",0)+legion.getMoney()*cardE.getInt("moneyRateCost",0)/100,0,999);
                industry=ComUtil.limitValue(legion.getIndustry()*cardE.getInt("industryRateCost",0)/100,0,999);
                tech=ComUtil.limitValue(legion.getTech()*cardE.getInt("techRateCost",0)/100,0,999);
                food=ComUtil.limitValue(legion.getFood()*cardE.getInt("foodRateCost",0)/100,0,999);*/

                money = cardE.getInt("money", 0) + legion.incomeMoney * cardE.getInt("moneyRateCost", 0) / 100;
                industry = cardE.getInt("industry", 0) + legion.incomeIndustry * cardE.getInt("industryRateCost", 0) / 100;
                food = cardE.getInt("food", 0) + legion.incomeFood * cardE.getInt("foodRateCost", 0) / 100;
                tech = cardE.getInt("tech", 0) + legion.incomeTech * cardE.getInt("techRateCost", 0) / 100;

                cardType = cardE.getInt("type");
                money = ComUtil.limitValue(DefDAO.getCardPrice(legion, smapGameStage.selectedBuildData, 0, money, cardType, cardId, +GameMethod.getCardLv(legion, smapGameStage.selectedBuildData, cardId)), 0, 999);
                industry = ComUtil.limitValue(DefDAO.getCardPrice(legion, smapGameStage.selectedBuildData, 1, industry, cardType, cardId, +GameMethod.getCardLv(legion, smapGameStage.selectedBuildData, cardId)), 0, 999);
                tech = ComUtil.limitValue(DefDAO.getCardPrice(legion, smapGameStage.selectedBuildData, 2, tech, cardType, cardId, +GameMethod.getCardLv(legion, smapGameStage.selectedBuildData, cardId)), 0, 999);
                food = ComUtil.limitValue(DefDAO.getCardPrice(legion, smapGameStage.selectedBuildData, 3, food, cardType, cardId, +GameMethod.getCardLv(legion, smapGameStage.selectedBuildData, cardId)), 0, 999);

                legion.buyCard(money,industry,tech,food);
            }else {
                favor=cardE.getInt("moneyRateCost",0);
            }

        }else  if(selectCardType==19){//购买奇物
            int cardPrice=game.gameConfig.getDEF_SPIRIT().getElementById(cardId).getInt("rule",100);
            if(!game.gameConfig.costPlayerModel(cardPrice)){
                return;
            }
        }else {
            money = cardE.getInt("money", 0)+legion.incomeMoney*cardE.getInt("moneyRateCost", 0)/100;
            industry = cardE.getInt("industry", 0)+legion.incomeIndustry*cardE.getInt("industryRateCost", 0)/100;
            food = cardE.getInt("food", 0)+legion.incomeFood*cardE.getInt("foodRateCost", 0)/100;
            tech = cardE.getInt("tech", 0)+legion.incomeTech*cardE.getInt("techRateCost", 0)/100;
            //boolean ifLegionFeature=smapGameStage.getsMapDAO().masterData.getPlayerMode()==0;
            money = ComUtil.limitValue(DefDAO.getCardPrice(legion, smapGameStage.selectedBuildData, 0, money, cardType, cardId, +GameMethod.getCardLv(legion, smapGameStage.selectedBuildData, cardId)), 0, 999);
            industry = ComUtil.limitValue(DefDAO.getCardPrice(legion, smapGameStage.selectedBuildData, 1, industry, cardType, cardId, +GameMethod.getCardLv(legion, smapGameStage.selectedBuildData, cardId)), 0, 999);
            tech = ComUtil.limitValue(DefDAO.getCardPrice(legion, smapGameStage.selectedBuildData, 2, tech, cardType, cardId, +GameMethod.getCardLv(legion, smapGameStage.selectedBuildData, cardId)), 0, 999);
            food = ComUtil.limitValue(DefDAO.getCardPrice(legion, smapGameStage.selectedBuildData, 3, food, cardType, cardId, +GameMethod.getCardLv(legion, smapGameStage.selectedBuildData, cardId)), 0, 999);

            legion.buyCard(money,industry,tech,food);
        }

int cardTechId=0; int round=1;

        Fb2Smap.ForeignData f=null;
        if(cardType==12){
            f= smapGameStage.getsMapDAO().getForeignData(tLi);
            // selectCardChance= smapGameStage.getsMapDAO().getChanceByCivilSpec(smapGameStage.playerLegionIndex,li,selectCardChance);
            selectCardChance=selectCardChance+f.getFavorValue()/10;
            smapGameStage.getsMapDAO().checkTask(!smapGameStage.getsMapDAO().ifAllyPlayerByLi(tLi),smapGameStage.getPlayerLegionIndex(),19,1);
            smapGameStage.getsMapDAO().checkTask(smapGameStage.getsMapDAO().ifAllyPlayerByLi(tLi),smapGameStage.getPlayerLegionIndex(),27,1);

            Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(smapGameStage.selectedTargetArmyHexagon);
            if(a!=null&&a.getGeneralIndex()!=0&&a.isPlayer()&&a.getArmyRound()==0){
                Fb2Smap.GeneralData g=a.getGeneralData();
                selectCardChance=selectCardChance+ComUtil.limitValue(ComUtil.getRandom(1,a.getArmyRank())*g.getPolitical(),0,40);
                if(g.getState()==0){
                    smapGameStage.getsMapDAO().addDialogueData(a.getCountryId(),g,15,"",true);
                }
                a.addArmyRound(1);
            }
            //对盟友的外交成功率提高{0}%,对敌军的外交成功率降低{0}%
            if(selectCardChance!=100&&smapGameStage.getsMapDAO().ifSystemEffective(16)&&smapGameStage.getsMapDAO().ifHaveSpirit(5)){
                int fd=smapGameStage.getsMapDAO().getForeignDegree(smapGameStage.getPlayerLegionIndex(),tLi);
                if(fd==1){
                    selectCardChance=selectCardChance+smapGameStage.getsMapDAO().getSpiritValue(5);
                }else if(fd==-1){
                    selectCardChance=selectCardChance-smapGameStage.getsMapDAO().getSpiritValue(5);
                }
            }
            if(f.getForeignType()==-1&&(cardId==3103||cardId==3105||cardId==3106||cardId==3107||cardId==3108||cardId==3113)){
                selectCardChance=0;
            }

        }
        //先计算回合是因为防止升级后导致回合计算失误
        if(cardId==2015){
            round=  smapGameStage.getsMapDAO().getBuildRailWayRound(smapGameStage.selectedBuildData,smapGameStage.selectedTargetHexagon);
        }else{
            round= smapGameStage.getsMapDAO().getRound(cardId,cardType);
        }

        if(!smapGameStage.isEditMode(true)){
            if(cardType==12){//整体类
                legion.reduceTradeCount();
            }else if(cardType==11){//科技类
                round=round+legion.getUpdTechLegionRound(cardId,true);
                legion.addStability(round/2);
                Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(legion.getCapitalId());
                if(a!=null&&a.getGeneralIndex()!=0&&a.isPlayer()&&a.getArmyRound()==0){
                    Fb2Smap.GeneralData g=a.getGeneralData();
                    round=ComUtil.limitValue(round-ComUtil.getRandom(1,g.getPolitical()),0,999);
                    if(g.getState()==0){
                        smapGameStage.getsMapDAO().addDialogueData(a.getCountryId(),g,15,"",true);
                    }
                    a.addArmyRound(1);
                }//研究军团科技时有{0}%几率使研究回合减半
                if(smapGameStage.getsMapDAO().ifTriggerSpirit(1)){
                    round=ComUtil.max(round/2,1);
                }
                legion.addLegionRound(round);

                if(smapGameStage.getsMapDAO().ifHaveSpirit(18)){
                    //研究军团科技增加额外的军团稳定度{0}
                    legion.addStability(smapGameStage.getsMapDAO().getSpiritValue(18));
                }
                smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),13,legion.getLegionRound());
               if(getMode()==0){//普通模式下延迟生效科技
                    smapGameStage.getsMapDAO().masterData.setPlayerResearchingCardId(cardId);
                }
            }else if(cardType==14||cardId==2015){//地块工事类
                Fb2Smap.ArmyData a=smapGameStage.selectedArmy;
                if(a!=null&&a.getArmyRound()==0){
                    a.addArmyRound(round);
                }else if(smapGameStage.getSelectBuildData()!=null&&smapGameStage.getSelectBuildData().getLegionIndex()==smapGameStage.getPlayerLegionIndex()){
                    smapGameStage.getSelectBuildData().addBuildRound(round);
                }
            }else  {//地块类
                //船只和潜艇以设施港口1为生产位置
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()!=2&&(cardType==4||cardType==8)&&!smapGameStage.isEditMode(true)){
                    smapGameStage.selectedTargetHexagon=smapGameStage.selectedBuildData.getFacilityHexagon(1);
                }
                if(cardType==1||cardType==2||cardType==3||cardType==4||cardType==6||cardType==8){
                    // smapGameStage.setBuildAnimation(smapGameStage.selectedTargetHexagon);
                    smapEffectStage.drawFace(smapGameStage.selectedTargetHexagon,1);
                    //招募兵种后有{0}%几率不增加冷却回合
                    if(  smapGameStage.getsMapDAO().ifTriggerSpirit(31)){
                        round=0;
                    }
                }else {
                    // smapGameStage.setBuildAnimation(smapGameStage.selectedTargetRegion);
                    smapEffectStage.drawFace(smapGameStage.selectedTargetRegion,1);
                }
                //地方建筑生产     将领位于建筑位置上，且将领单位回合不为0
                if(cardType==9||cardType==10){
                    Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(smapGameStage.selectedRegionId);
                    if(a!=null&&a.getGeneralIndex()!=0&&a.isPlayer()&&a.getArmyRound()==0){
                        Fb2Smap.GeneralData g=a.getGeneralData();
                        round=ComUtil.limitValue(round-ComUtil.getRandom(1,g.getPolitical()),0,999);
                        if(g.getState()==0){
                            smapGameStage.getsMapDAO().addDialogueData(a.getCountryId(),g,15,"",true);
                        }
                        a.addArmyRound(1);
                    }
                }
                if(cardId==2007&&smapGameStage.getSelectBuildData().ifBuildLvIsOnlyTop(2007)){//升级城市等级时如果城市是该区域设施唯一最高级
                    int poor=ComUtil.max(1,smapGameStage.getSelectBuildData().getBuildCardLvPoorForCityLv());
                    round+=poor;
                }
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
                    smapGameStage.selectedBuildData.addCityStability(round*game.resGameConfig.addCityStabilityEachRound,game.resGameConfig.cityStabilityChangeValueMax);
                }
                smapGameStage.getSelectBuildData().addBuildRound(round);
            }
        }


        //先关闭窗口,如果后面需要提示会自动提示的

        //selectCardChance=100;
        int v=smapGameStage.getsMapDAO().getChiefSpecialEffect(16);
        if(v>=0){
            smapGameStage.selectedBuildData.addCityStability(v);
        }

        if(selectCardType==19){
            smapGameStage.getsMapDAO().addSpiritId(cardId,false);
        }else if(selectCardType==17){
            smapGameStage.selectedBuildData.setBuildWonder(cardId);
        }else if(cardType==12&&ComUtil.ifGet(100-selectCardChance)){ //如果是外加卡牌,计算失败率
            smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(4,0),
                    game.gameMethod.getPromptStrT(4,1,"country_name_"+legion.getCountryId()),
                    game.gameMethod.getPromptStrT(4,2),legion.getLegionIndex(),tLegion.getLegionIndex(),false
            );
            if(favor!=0){
                favor=-favor;f.addFavor(favor);
            }
            //外交成功将提高和谐度,外交失败将降低和谐度 失败
            if(smapGameStage.getsMapDAO().getChiefSpecialEffect(11)>=0){
                smapGameStage.getsMapDAO().addHarmor(-1);
            }
        }else{
            //外交成功将提高和谐度,外交失败将降低和谐度 成功
            if(cardType==12&&smapGameStage.getsMapDAO().getChiefSpecialEffect(11)>=0){
                smapGameStage.getsMapDAO().addHarmor(3);
            }
            int hexagon=-1;
            //设置卡牌效果
            switch(cardId){
                //轻步兵
                case 1101:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //精英步兵
                case 1102:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //机动步兵
                case 1103:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //机枪步兵
                case 1104:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //装甲步兵
                case 1105:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //掷弹步兵
                case 1106:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //轻炮
                case 1201:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //火炮
                case 1202:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //野战炮
                case 1203:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //火箭炮
                case 1204:
                    hexagon=  smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //自行走炮
                case 1205:
                    hexagon=smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //导弹车
                case 1206:
                    hexagon=smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //战略导弹车
                case 1207:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //装甲车
                case 1301:
                    hexagon=smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //轻坦克
                case 1302:
                    hexagon=smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //中坦克
                case 1303:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //重坦克
                case 1304:
                    hexagon=smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //要塞坦克
                case 1305:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //主战坦克
                case 1306:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //护卫舰
                case 1401:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    if(selectCardType==18){
                        Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                        if(a!=null){
                            a.setLegionIndex(smapGameStage.getPlayerLegionIndex());
                            a.updActor();
                        }
                    }
                    break;
                //驱逐舰
                case 1402:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    if(selectCardType==18){
                        Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                        if(a!=null){
                            a.setLegionIndex(smapGameStage.getPlayerLegionIndex());
                        }
                    }
                    break;
                //巡洋舰
                case 1403:
                    hexagon=  smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    if(selectCardType==18){
                        Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                        if(a!=null){
                            a.setLegionIndex(smapGameStage.getPlayerLegionIndex());
                        }
                    }
                    break;
                //战列舰
                case 1404:
                    hexagon=   smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    if(selectCardType==18){
                        Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                        if(a!=null){
                            a.setLegionIndex(smapGameStage.getPlayerLegionIndex());
                        }
                    }
                    break;
                //重型战列舰
                case 1405:
                    hexagon= smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    if(selectCardType==18){
                        Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                        if(a!=null){
                            a.setLegionIndex(smapGameStage.getPlayerLegionIndex());
                        }
                    }
                    break;
                //轻航空母舰
                case 1406:
                    hexagon=  smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    if(selectCardType==18){
                        Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                        if(a!=null){
                            a.setLegionIndex(smapGameStage.getPlayerLegionIndex());
                        }
                    }
                    break;
                //航空母舰
                case 1407:
                    hexagon=  smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    if(selectCardType==18){
                        Fb2Smap.ArmyData a=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                        if(a!=null){
                            a.setLegionIndex(smapGameStage.getPlayerLegionIndex());
                        }
                    }
                    break;
                //海岸炮
                case 1601:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //大海岸炮
                case 1602:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //碉堡
                case 1603:
                    hexagon= smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //要塞
                case 1604:
                    hexagon=smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //巨型要塞
                case 1605:
                    hexagon=smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //飞弹
                case 1606:
                    hexagon=smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;

                //战斗潜艇
                case 1801:
                    hexagon=smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //超级战斗潜艇
                case 1802:
                    hexagon=smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //导弹潜艇
                case 1803:
                    hexagon=smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //战略导弹潜艇
                case 1804:
                    hexagon=smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);

                    break;
                //侦察机
                case 1501:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //战斗机
                case 1502:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //攻击机
                case 1503:
                    smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //轰炸机
                case 1504:
                    smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //运输机
                case 1505:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //战略轰炸机
                case 1506:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //预警机
                case 1507:
                    smapGameStage.getsMapDAO().recruit(smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //催泪弹
                case 1701:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //毒气弹
                case 1702:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //原子弹
                case 1703:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //氢弹
                case 1704:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;
                //三相弹
                case 1705:
                    smapGameStage.getsMapDAO().recruit( smapGameStage.selectedTargetHexagon,cardId,-1);
                    break;

                //贸易
                case 2001:
                    smapGameStage.selectedBuildData.updTradeLvNow();  smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //文化
                case 2002:
                    if(smapGameStage.getsMapDAO().chiefData!=null){
                        smapGameStage.getsMapDAO().chiefData.addLiteracy(smapGameStage.selectedBuildData.getCultureLvNow());
                    }
                    smapGameStage.selectedBuildData.updCultureLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //交通
                case 2003:
                    smapGameStage.selectedBuildData.updTransportLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //学校
                case 2004:
                    smapGameStage.selectedBuildData.updTechLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //原料
                case 2005:
                    smapGameStage.selectedBuildData.updFoodLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //能源
                case 2006:
                    smapGameStage.selectedBuildData.updEnergyLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //城市
                case 2007:
                    smapGameStage.selectedBuildData.updCityLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //工业
                case 2008:
                    smapGameStage.selectedBuildData.updIndustLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //补给
                case 2009:
                    smapGameStage.selectedBuildData.updSupplyLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //城防
                case 2010:
                    smapGameStage.selectedBuildData.updDefenceLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //飞机厂
                case 2011:
                    smapGameStage.selectedBuildData.updAirLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //防空/飞弹
                case 2012:
                    smapGameStage.selectedBuildData.updMissileLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //秘密武器
                case 2013:
                    smapGameStage.selectedBuildData.updNuclearLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //军营
                case 2014:
                    smapGameStage.selectedBuildData.updArmyLvNow();smapGameStage.selectedBuildData.updActor();
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //铁路
                case 2015:
                    smapGameStage.getsMapDAO().buildRailway( smapGameStage.selectedTargetHexagon);
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),4,1);
                    break;
                //城市可提升等级 改成延迟生效,所以在非普欧婷模式下,才会在这里升级科技
                case 3001:
                    if(getMode()>0){
                    legion.updCityLvMax();}break;
                //工业可提升等级
                case 3002:
                    if(getMode()>0){
                    legion.updIndustLvMax();}break;
                //研究可提升等级
                case 3003:
                    if(getMode()>0){
                    legion.updTechLvMax();}break;
                //能源可提升等级
                case 3004:
                    if(getMode()>0){
                    legion.updEnergyLvMax();}break;
                //交通可提升等级
                case 3005:
                    if(getMode()>0){
                    legion.updTransportLvMax();}break;
                //建造士兵军衔上限
                case 3006:
                    if(getMode()>0){
                    legion.updInfantryLvMax();}break;
                //建造炮兵军衔上限
                case 3007:
                    if(getMode()>0){
                    legion.updCannonLvMax();}break;
                //建造坦克军衔上限
                case 3008:
                    if(getMode()>0){
                    legion.updTankLvMax();}break;
                //建造要塞军衔上限
                case 3009:
                    if(getMode()>0){
                    legion.updFortLvMax();}break;
                //建造海军军衔上限
                case 3010:
                    if(getMode()>0){
                    legion.updNavyLvMax();}break;
                //建造空军军衔上限
                case 3011:
                    if(getMode()>0){
                    legion.updAirLvMax();}break;
                //城市升级补给上限
                case 3012:
                    if(getMode()>0){
                    legion.updSupplyLvMax();}break;
                //城市城防升级上限
                case 3013:
                    if(getMode()>0){
                    legion.updDefenceLvMax();}break;
                //城市导弹升级上限
                case 3014:
                    if(getMode()>0){
                    legion.updMissileLvMax();}break;
                //城市超武升级上限
                case 3015:
                    if(getMode()>0){
                    legion.updNuclearLvMax();}break;
                //军团金融科技上限
                case 3016:
                    if(getMode()>0){
                    legion.updFinancialLvMax();}break;
                //城市贸易规模上限
                case 3017:
                    if(getMode()>0){
                    legion.updTradeLvMax();}break;
                //城市文化科技上限
                case 3018:
                    if(getMode()>0){
                    legion.updCultureLvMax();}break;
                //威望
                case 3019:
                    if(getMode()>0){
                    legion.updMiracleLvNow();}break;
                //步兵卡库
                case 3020:
                    if(getMode()>0){
                    legion.updInfantryCardMax();}break;
                //装甲卡库
                case 3021:
                    if(getMode()>0){
                    legion.updArmorCardMax();}break;
                //火炮卡库
                case 3022:
                    if(getMode()>0){
                    legion.updArtilleryCardMax();}break;
                //海军卡库
                case 3023:
                    if(getMode()>0){
                    legion.updNavyCardMax();}break;
                //空军卡库
                case 3024:
                    if(getMode()>0){
                    legion.updAirCardMax();}break;
                //超武卡库
                case 3025:
                    if(getMode()>0){
                    legion.updNuclearLvMax();}break;
                //导弹卡库
                case 3026:
                    if(getMode()>0){
                    legion.updMissileCardMax();}break;
                //潜艇卡库
                case 3027:
                    if(getMode()>0){
                    legion.updSubmarineCardMax();}break;
                //战术卡库
                case 3028:
                    if(getMode()>0){
                    legion.updDefenceCardMax();}break;
                //将领卡库
                case 3029:
                    if(getMode()>0){
                    legion.updGeneralCardMax();}break;
                //刷新数量
                case 3030:
                    if(getMode()>0){
                    legion.updMilitaryAcademyLv();}break;
                //礼物
                case 3101:favor=f.getABSCivilSpecFavor();f.addFavor(favor);
                    smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(5,0),
                            game.gameMethod.getPromptStrT(5,1,favor,f.getFavorValue()),
                            game.gameMethod.getPromptStrT(5,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                    );
                    smapGameStage.getsMapDAO().addThankRDialogue(tLi);
                    break;
                //警告
                case 3102:favor=-f.getABSCivilSpecFavor();
                    f.addFavor(favor);
                    smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(5,0),
                            game.gameMethod.getPromptStrT(5,1,favor,f.getFavorValue()),
                            game.gameMethod.getPromptStrT(5,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                    );
                    smapGameStage.getsMapDAO().addWarnRDialogue(tLi);
                    break;
                //结盟
                case 3103:favor=f.getABSCivilSpecFavor()*2;f.addFavor(favor);
                    if(!smapGameStage.getsMapDAO().legion_AllianceTIA1(smapGameStage.getPlayerLegion(), smapGameStage.getsMapDAO().legionDatas.get(tLi),true)){
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(4,0),
                                game.gameMethod.getPromptStrT(4,1,"country_name_"+legion.getCountryId()),
                                game.gameMethod.getPromptStrT(4,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true);
                    }
                    smapGameStage.getsMapDAO().addThankRDialogue(tLi);
                    //在btl方法 中直接记录了
                    break;
                //宣战
                case 3104:favor=-30;f.addFavor(favor);
                    if(tLegion!=null){
                        if(  !smapGameStage.getsMapDAO().legion_DeclareWar(smapGameStage.getPlayerLegion(), smapGameStage.getsMapDAO().legionDatas.get(tLi),ComUtil.ifGet(smapGameStage.getsMapDAO().getChiefReputation()))){
                            smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(4,0),
                                    game.gameMethod.getPromptStrT(4,1,"country_name_"+legion.getCountryId()),
                                    game.gameMethod.getPromptStrT(4,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                            );
                        }else{
                            smapGameStage.getsMapDAO().legion_removeAllBuildInfuce(smapGameStage.getPlayerLegion().getLegionIndex(),tLi);
                        }
                    }
                    smapGameStage.getsMapDAO().addWarnRDialogue(tLi);break;
                //交流
                case 3105:
                    cardTechId= smapGameStage.getsMapDAO().legionExchangeTechTIA1(legion,tLegion);
                    favor=f.getABSCivilSpecFavor();
                    if (cardTechId==0) {
                        f.addFavor(favor);
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(5,0),
                                game.gameMethod.getPromptStrT(5,1,favor,f.getFavorValue()),
                                game.gameMethod.getPromptStrT(5,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                        );
                    }else {
                        favor=favor*2;f.addFavor(favor);
                        round=ComUtil.limitValue(legion.getUpdTechLegionRound(cardTechId,false),1,255);
                        legion.addLegionRound(round);
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(3,0),
                                game.gameMethod.getPromptStrT(3,1,tLegion.legionName,game.gameMethod.getCardName(tLegion,tLegion.getCapital(),cardTechId,selectCardType),favor,f.getFavorValue()),
                                game.gameMethod.getPromptStrT(3,2,round),legion.getLegionIndex(),tLegion.getLegionIndex(),true

                        );
                    }
                    smapGameStage.getsMapDAO().addThankRDialogue(tLi);
                    break;
                //扶持
                case 3106:favor=f.getABSCivilSpecFavor();f.addFavor(favor);
                    tLegion.addMoney(money);
                    tLegion.addIndustry(industry);
                    tLegion.addFood(food);
                    tLegion.addTech(tech);
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),24,1);
                    smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(8,0),
                            game.gameMethod.getPromptStrT(8,1,tLegion.legionName,favor,f.getFavorValue()),
                            game.gameMethod.getStrValueT("prompt_effect_8",money,industry,tech,food),legion.getLegionIndex(),tLegion.getLegionIndex(),true);
                    smapGameStage.getsMapDAO().addThankRDialogue(tLi);break;
                //求援
                case 3107:favor=f.getABSCivilSpecFavor();f.addFavor(favor);
                    smapGameStage.getsMapDAO().legion_Support(smapGameStage.getPlayerLegion(),tLegion,favor);
                    smapGameStage.getsMapDAO().checkTask(true,smapGameStage.getPlayerLegionIndex(),33,1);
                    break;
                //合并
                case 3108:
                    if(!smapGameStage.getsMapDAO().legion_Merge(smapGameStage.getPlayerLegion(),tLegion,true)){
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(4,0),
                                game.gameMethod.getPromptStrT(4,1,"country_name_"+legion.getCountryId()),
                                game.gameMethod.getPromptStrT(4,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                        );
                    }
                    break;
                //窃取科技
                case 3109:    favor=-ComUtil.getRandom(5,15);f.addFavor(favor); cardTechId= smapGameStage.getsMapDAO().legionExchangeTechTIA1(smapGameStage.getPlayerLegion(),tLegion);
                    if (cardTechId==0) {
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(11,0),
                                game.gameMethod.getPromptStrT(11,1,tLegion.legionName,favor,f.getFavorValue()),
                                game.gameMethod.getPromptStrT(11,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                        );
                    }else {
                        round=ComUtil.limitValue(legion.getUpdTechLegionRound(cardTechId,false),1,255);
                        legion.addLegionRound(round);
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(12,0),
                                game.gameMethod.getPromptStrT(12,1,tLegion.legionName,game.gameMethod.getCardName(tLegion,tLegion.getCapital(),cardTechId,selectCardType),favor,f.getFavorValue()),
                                game.gameMethod.getPromptStrT(12,2,round),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                        );
                    }
                    smapGameStage.getsMapDAO().addWarnRDialogue(tLi);
                    break;
                //和约
                case 3110:    /*favor=ComUtil.getRandom(5,20);f.addFavor(favor);
                    smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(5,0),
                            game.gameMethod.getPromptStrT(5,1,favor,f.getFavor()),
                            game.gameMethod.getPromptStrT(5,2)
                    );*/
                    if(! smapGameStage.getsMapDAO().legion_BegPeace(smapGameStage.getPlayerLegion(),tLegion)){
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(4,0),
                                game.gameMethod.getPromptStrT(4,1,"country_name_"+legion.getCountryId()),
                                game.gameMethod.getPromptStrT(4,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true);
                    }
                    smapGameStage.getsMapDAO().addThankRDialogue(tLi);
                    break;
                //暴动
                case 3111:    favor=-ComUtil.getRandom(15,30);f.addFavor(favor);
                    effect= smapGameStage.getsMapDAO().legion_Riot(smapGameStage.getsMapDAO().legionDatas.get(tLi),null,"prompt_detail_13");
                    if(effect==null){
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(11,0),
                                game.gameMethod.getPromptStrT(11,1,tLegion.legionName,favor,f.getFavorValue()),
                                game.gameMethod.getPromptStrT(11,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                        );
                    }else {
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(13,0,tLegion.legionName),
                                effect,
                                game.gameMethod.getPromptStrT(13,2,tLegion.legionName,favor,f.getFavorValue()),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                        );
                    }
                    smapGameStage.getsMapDAO().addWarnRDialogue(tLi);
                    break;
                //制裁
                case 3112:    favor=ComUtil.getRandom(10,20);f.reduceFavor(favor);
                    v=favor/2;
                    tLegion.reduceTaxBonus(v);
                    tLegion.reduceStability(v);
                    smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(14,0),
                            game.gameMethod.getPromptStrT(14,1,tLegion.legionName,v),
                            game.gameMethod.getPromptStrT(14,2,tLegion.legionName,favor,f.getFavorValue()),legion.getLegionIndex(),tLegion.getLegionIndex(),true
                    );
                    smapGameStage.getsMapDAO().addWarnRDialogue(tLi);
                    break;
                case 3113:
                    if(f.getForeignType()==1){
                        f.setForeignValue(f.getForeignValue()+10);
                    }else{
                        f.setForeignType(1);
                        f.setForeignValue(10);
                    }
                    smapGameStage.getsMapDAO().addAssistantDialogueData(game.gameMethod.getStrValue("prompt_dialogue_35",tLegion.legionName,f.getForeignValue()),true);
                    break;
                case 3114:
                    smapGameStage.getsMapDAO().legion_inviteAttack(legion,tLegion);
                    break;
                case 3115:
                    smapGameStage.selectedBuildData.setInfluenceLi(legion.getLegionIndex());
                    smapGameStage.getsMapDAO().addWarnRDialogue(tLi);
                    break;
                case 3116:
                    smapGameStage.getsMapDAO().legion_Independent(smapGameStage.getPlayerLegion());
                    smapGameStage.getsMapDAO().addWarnRDialogue(tLi);
                    break;
                case 3117:
                    if(!smapGameStage.getsMapDAO().addSpiritId( tLegion.getSpiritId(),true)){
                        smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(4,0),
                                game.gameMethod.getPromptStrT(4,1,"country_name_"+legion.getCountryId()),
                                game.gameMethod.getPromptStrT(4,2),legion.getLegionIndex(),tLegion.getLegionIndex(),true);
                    }
                    break;
                case 3118:
                    showCardUi(18);  return;
                case 3119:
                    smapGameStage.getsMapDAO().legion_separate(legion.getLegionIndex(),tLi,50);
                    smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getPromptStrT(109,0),
                            game.gameMethod.getPromptStr(109,1,tLegion.legionName),
                            game.gameMethod.getPromptStrT(109,2,tLegion.legionName),legion.getLegionIndex(),tLegion.getLegionIndex(),true);
                    break;
                case  4000:   smapGameStage.selectedBuildData.setBuildPolicy(0);break;
                case  4001:   smapGameStage.selectedBuildData.setBuildPolicy(1);break;
                case  4002:   smapGameStage.selectedBuildData.setBuildPolicy(2);break;
                case  4003:   smapGameStage.selectedBuildData.setBuildPolicy(3);break;
                case  4004:   smapGameStage.selectedBuildData.setBuildPolicy(4);break;
                case  4005:   smapGameStage.selectedBuildData.setBuildPolicy(5);break;
                case  4006:   smapGameStage.selectedBuildData.setBuildPolicy(6);break;

                case 5001:
                    if( smapGameStage.selectedArmy!=null){
                        smapGameStage.getsMapDAO().fort_build(5001,smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getArmyRank());
                    }else{
                        smapGameStage.getsMapDAO().fort_build(5001,smapGameStage.selectedTargetHexagon,smapGameStage.getPlayerLegionIndex(),smapGameStage.getSelectBuildData().getDefenceLvNow());
                    }
                    break;
                case 5002:
                    if( smapGameStage.selectedArmy!=null){
                        smapGameStage.getsMapDAO().fort_build(5002,smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getArmyRank());
                    }else{
                        smapGameStage.getsMapDAO().fort_build(5002,smapGameStage.selectedTargetHexagon,smapGameStage.getPlayerLegionIndex(),smapGameStage.getSelectBuildData().getDefenceLvNow());
                    }
                    break;
                case 5003:
                    if( smapGameStage.selectedArmy!=null){
                        smapGameStage.getsMapDAO().fort_build(5003,smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getArmyRank());
                    }else{
                        smapGameStage.getsMapDAO().fort_build(5003,smapGameStage.selectedTargetHexagon,smapGameStage.getPlayerLegionIndex(),smapGameStage.getSelectBuildData().getDefenceLvNow());
                    }
                    break;
                case 5004:
                    if( smapGameStage.selectedArmy!=null){
                        smapGameStage.getsMapDAO().fort_build(5004,smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getArmyRank());
                    }else{
                        smapGameStage.getsMapDAO().fort_build(5004,smapGameStage.selectedTargetHexagon,smapGameStage.getPlayerLegionIndex(),smapGameStage.getSelectBuildData().getDefenceLvNow());
                    }
                    break;
                case 5005:
                    if( smapGameStage.selectedArmy!=null){
                        smapGameStage.getsMapDAO().fort_build(5005,smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getArmyRank());
                    }else{
                        smapGameStage.getsMapDAO().fort_build(5005,smapGameStage.selectedTargetHexagon,smapGameStage.getPlayerLegionIndex(),smapGameStage.getSelectBuildData().getDefenceLvNow());
                    }
                    break;
                case 5006:
                    if( smapGameStage.selectedArmy!=null){
                        smapGameStage.getsMapDAO().fort_build(5006,smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getArmyRank());
                    }else{
                        smapGameStage.getsMapDAO().fort_build(5006,smapGameStage.selectedTargetHexagon,smapGameStage.getPlayerLegionIndex(),smapGameStage.getSelectBuildData().getDefenceLvNow());
                    }
                    break;
                case 5007:
                    if( smapGameStage.selectedArmy!=null){
                        smapGameStage.getsMapDAO().fort_build(5007,smapGameStage.selectedArmy.getHexagonIndex(),smapGameStage.getPlayerLegionIndex(),smapGameStage.selectedArmy.getArmyRank());
                    }else{
                        smapGameStage.getsMapDAO().fort_build(5007,smapGameStage.selectedTargetHexagon,smapGameStage.getPlayerLegionIndex(),smapGameStage.getSelectBuildData().getDefenceLvNow());
                    }
                    break;
                case 6001:  smapGameStage.getsMapDAO().buildFacility(smapGameStage.selectedTargetHexagon,1);

                    break;

                case 7000:  smapGameStage.selectedBuildData.setAirforcePolicy(cardId-7000);

                    break;
                case 7001:  smapGameStage.selectedBuildData.setAirforcePolicy(cardId-7000);

                    break;
                case 7002:  smapGameStage.selectedBuildData.setAirforcePolicy(cardId-7000);

                    break;
                case 7003:  smapGameStage.selectedBuildData.setAirforcePolicy(cardId-7000);

                    break;

            }
        }
        if(smapGameStage.selectedBuildData!=null){
            smapGameStage.selectedBuildData.updAllActorUpdMarkByRegion();
        }
        windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId).setVisible(false);
        smapGameStage.setIfFixed(false);
        executeScripts(false);
        // hidUi(true);
        // Gdx.app.log("BuyCard","yes");

        if(smapGameStage.selectedBuildData!=null){
            smapGameStage.selectedBuildData.getBuildActor().updDrawInfo();
        }


        // updCardLabelLv();//更新该地块的等级数据
        updResourceForPlayer();//更新资源条数据
        updResourceForRegion();//更新地块显示数据



        //showGeneralDialogueGroupAndPromptGroup();
       /* if(cardType==12){
            showPromptGroup();
        }else {

        }*/
        if(cardType==14){
            updButtonForDefaultGroup();
        }
        updButtonPrompt();
    }

    public void showGeneralDialogueGroup(int generalId,int flagBg,String text){
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId);
        game.gameLayout.updGeneralDialogueGroup(w,generalId,flagBg,text);
        w.setVisible(true);
    }
    public void showGeneralDialogueGroup(boolean ifHidOther){
        if(smapGameStage.getsMapDAO().dialogueDatas.size>0){
            if(ifHidOther){
                hidUi(false);hidCommonUi();
            }
            smapGameStage.setIfFixed(true);
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId);

            game.gameLayout.updGeneralDialogueGroup(w, smapGameStage.getsMapDAO().dialogueDatas.get(0));
            w.setVisible(true);
            smapGameStage.getsMapDAO().dialogueDatas.removeIndex(0);
        }
    }

    public boolean showGeneralDialogueGroupAndPromptGroup() {
        smapGameStage.resetDefaultState();
        brightenAdvicePotion(-1);
        if(smapGameStage.getsMapDAO().dialogueDatas.size>0){
            hidUi(false);hidCommonUi();
            smapGameStage.setIfFixed(true);
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId);

            game.gameLayout.updGeneralDialogueGroup(w, smapGameStage.getsMapDAO().dialogueDatas.get(0));
            w.setVisible(true);
            smapGameStage.getsMapDAO().dialogueDatas.removeIndex(0);
            return true;
        }else if( smapGameStage.getsMapDAO().promptDatas.size>0){
            hidUi(false);hidCommonUi();
            smapGameStage.setIfFixed(true);
            showIntroductionGroup(true);
            return true;
        }else{
            hidGeneralDialogueGroup(true);
            return false;
        }
    }




    private void nextRound(){
        if(smapGameStage.getsMapDAO().ifCheckCheat()){
            smapGameStage.getsMapDAO().masterData.setIfCheat(1);
        }
        progressCountryImage.setVisible(true);
        smapGameStage.hidArrow();
        ifRoundInEnd=false;
        logTime.reStar();
        smapGameStage.getsMapDAO().nextRoundReady(1);
        logTime.log("ready calculate");
        //执行玩家周边国家
        smapGameStage.getsMapDAO().nextRoundPlayerBorderAct();
        smapGameStage.getsMapDAO().nextRoundEndForOnlyPlayer();
        smapGameStage.getsMapDAO().roundState=4;
        smapGameStage.clearNullActor();
        smapGameStage.updAllFacilityActor();
        smapGameStage.updAllFortActor();
        smapGameStage.updAllBuildActor();
        smapGameStage.updAllArmyActor();
        smapGameStage.updClickActor();
        updResourceForPlayer();
        //恢复部分界面
        hidUi(false);
        hidSaveUI();
        //执行ai操作
        for(int i = 0, iMax = smapGameStage.getsMapDAO().legionDatas.size; i<iMax; i++){
            Fb2Smap.LegionData legion= smapGameStage.getsMapDAO().legionDatas.get(i);
            /*if(!liList.contains(legion.getLegionIndex())){
                smapGameStage.getsMapDAO().nextRoundAct(legion);
                liList.add(legion.getLegionIndex());
            }*/
            smapGameStage.getsMapDAO().nextRoundAct(legion,false);
            //Gdx.app.log("doInBackground",legion.getAllAttributes());
        }
        logTime.log("legionAct");
        ifRoundInEnd=true;
        smapGameStage.getsMapDAO().allUnitAct(false);
        logTime.log("allUnitAct");
        smapGameStage.getsMapDAO().nextRoundEnd(false);
        logTime.log("end nextRoundEnd");
        ifRoundInEnd=false;
        smapGameStage.clearNullActor();
        logTime.log("end clearNullActor");
        smapGameStage.updAllFacilityActor();
        logTime.log("end updAllFacilityActor");
        smapGameStage.updAllFortActor();
        logTime.log("end updAllFortActor");
        smapGameStage.updAllBuildActor();
        logTime.log("end updAllBuildActor");
        smapGameStage.updAllArmyActor();
        logTime.log("end updAllArmyActor");
        smapGameStage.updClickActor();
        smapGameStage.getsMapDAO().masterData.setIfHaveAutoBuild(0);
        game.playSound(ResDefaultConfig.Sound.成功);
        updResourceForPlayer();
    }

    private void nextRoundByAsync( ){
        CHAsyncTask task=new CHAsyncTask("nextRoundByAsync",0) {
            @Override
            public void onPreExecute() {
            }
            @Override
            public void onPostExecute(String result) {
                //Gdx.app.log("onPostExecute","end");
            }
            @Override
            public String doInBackground() {
                nextRound();
                return null;
            }
        };
        game.asyncManager.loadTask(task);
    }
    private void nextRoundEnd(){
        if(smapGameStage.getsMapDAO().roundState==0){
            return;
        }
        Gdx.app.log("tempUtil useCount",game.tempUtil.getUseCount()+"");
       // game.tempUtil.clearAllUse();
        game.tempUtil.dispose();
        game.tempTextureRegions.clear();
        smapGameStage.tempActorListClear();
        /**/
        smapGameStage.getsMapDAO().roundState=0;
        adviceIndex=0;
        updButtonPrompt();

        smapGameStage.updAllArmyActor();
        smapGameStage.updClickActor();
        updResourceForPlayer();
        hidUi(false);
        showSaveUI();
        smapGameStage.selectedLastHexagon=-1;
        smapGameStage.setIfFixed(false);
        //胜利检测   -1 失败 0 未获胜 1~5 d~s
        final int  gameResult= smapGameStage.getsMapDAO().getGameResult();
        //int gameResult=3;
        if(gameResult!=0){
            //只有征服模式下的征服模式才有资格生成新世界
            showLegionEnd(gameResult,smapGameStage.getsMapDAO().stageId!=0&&smapGameStage.getsMapDAO().masterData.getPlayerMode()==0&&smapGameStage.getsMapDAO().masterData.getBtlType()==0);
        }else/* if(!game.gameConfig.ifColor)*/{
            smapGameStage.tempActorListClear();
            nextRoundEndShow();
        }/*else{
            IntArray rs= smapGameStage.getsMapDAO().setRoundBegionArrowAnimation(smapGameStage,game.tempUtil.getTempIntArray());

            if(rs.size==0) {//循环展示rs的位置
                smapGameStage.tempActorListClear();
                game.tempUtil.disposeTempIntArray(rs);
                nextRoundEndShow();

            }else{
                //Gdx.app.log("move ",rs.size+":");
                asyncRoundBeginToShowArrow(rs,2,1,1.3f,0);
            }
        }*/
        if(getScript()){
            executeAllScripts(false);
        }else{
            smapGameStage.getsMapDAO().masterData.setGameStatu(1);
        }
    }

    public void drawViewMap(){
        int type=smapGameStage.getSmapBg().showType;
        type++;
        if(type>=3){
            type=0;
        }
        drawViewMap(type);
    }

    //drawType 0颜色图 1阵营图 3
    public void drawViewMap(int type) {
        if(priviewMapTexture!=null){
            GameMap.getPriviewMapBySMapDAO(priviewMapPixmap,smapGameStage.getsMapDAO(),type);
            priviewMapTexture.draw(priviewMapPixmap,0,0);
            windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId).setImageRegionNotChangeSize(1,new TextureRegion(priviewMapTexture));
        }
        if(smapGameStage.getsMapDAO().masterData.getIfChief()==1){

            if(politicalMapPixmap==null){
                //初始化预览图大小
                WindowGroup  w=windowGroups.get(ResDefaultConfig.Class.SMapScreenChiefGroupId);
                politicalMapPixmap=GameMap.getPriviewMapForPlayerTerritory(politicalMapPixmap,smapGameStage.getsMapDAO());
                //如果是null,需要对其大小最后进行适应
                politicalMapTexture =  w.setImageToFixSize(1, politicalMapPixmap,false);
                //  w.setImageToFixImage(2,1, ResDefaultConfig.Image.BORDER_IMAGE_REFW, ResDefaultConfig.Image.BORDER_IMAGE_REFH);
                w.setImageToFixImage(2,1, 0, 0);
                w.setImageToFixImage(5,1, ResDefaultConfig.Image.BORDER_IMAGE_REFW, ResDefaultConfig.Image.BORDER_IMAGE_REFH);
                w.setImageToFix(3,ResDefaultConfig.Image.BORDER_IMAGE_REFW, ResDefaultConfig.Image.BORDER_IMAGE_REFH);
                game.gameLayout.initForChiefGroup(w);
                politicalMapTexture.draw(politicalMapPixmap,0,0);
            }else {
                GameMap.getPriviewMapForPlayerTerritory(politicalMapPixmap,smapGameStage.getsMapDAO());
                politicalMapTexture.draw(politicalMapPixmap,0,0);
                windowGroups.get(ResDefaultConfig.Class.SMapScreenChiefGroupId).setImageRegionNotChangeSize(1,new TextureRegion(politicalMapTexture));
            }
        }
    }


    public void nextRoundEndShow(){
        if(game.gameConfig.ifAutoSave){
            game.save(game.getSMapDAO().masterData.getBtlType()==1?0:2);
            ifSave=true;
        }
        if(showGeneralDialogueGroupAndPromptGroup()){

        }else {
            showLegionTechUI(smapGameStage.getsMapDAO().getPlayerLegionData());
        }
        showSaveUI();
    }


    public void showLegionTechUI(Fb2Smap.LegionData l){
        hidCommonUi();
        closeOtherGroupExceptSpecified(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
        WindowGroup window= windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
        if(!window.isVisible()){
            game.gameLayout.updTechWindow(window,smapGameStage.rescource,l);
            smapGameStage.setIfFixed(true);
            window.setVisibleForEffect(true);
            //初始教程
            if(smapGameStage.getsMapDAO().masterData.getBtlType()==0&&!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenLegionTech)&&checkOpenGroup("openLegionTechGroup",true)){
                addAssistantDialogue("prompt_dialogue_49",false);
                game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenLegionTech);
            }
        }else{
            smapGameStage.setIfFixed(false);
            window.setVisibleForEffect(false);
        }
    }

    //除了指定的group不变,其他关闭
    public void closeOtherGroupExceptSpecified(int specifiedGroup){

        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenGameCardGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenIntroductionGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenIntroductionGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenLegionTechGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenRegionBuildGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenRegionBuildGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenArmyInfoGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenAchievementGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenAchievementGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenTradeInfoGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenTradeInfoGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenTextGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenTextGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenPauseGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenOptionGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }//SMapScreenArmyFormationGroupId
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenArmyFormationGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenPromptGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPromptGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenEditGroupId){
            WindowGroup w=   windowGroups.get(ResDefaultConfig.Class.SMapScreenEditGroupId);
            if(w!=null&&w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
        if(specifiedGroup!= ResDefaultConfig.Class.SMapScreenChiefGroupId){
            WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenChiefGroupId);
            if(w.isVisible()){
                w.setVisibleForEffect(false);
            }
        }
    }






    public void showTradeInfoUI(int page){
        hidCommonUi();
        WindowGroup window= windowGroups.get(ResDefaultConfig.Class.SMapScreenTradeInfoGroupId);
        window.setVisibleForEffect(true);
        smapGameStage.setIfFixed(true);
        if(page>tradeInfoSumPage){
            page=1;
        }
        game.gameLayout.updTradeInfoWindow(game,window, smapGameStage.getsMapDAO().tradeDatas,page,tradeInfoSumPage);
    }


    public void showArmyInfoUI(Fb2Smap.ArmyData army){
        hidCommonUi();
        WindowGroup window= windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
        window.setVisibleForEffect(true);
        smapGameStage.setIfFixed(true);
        if(cardNowPage!=0&&cardNowPage!=1){
            cardNowPage=0;
        }
        game.gameLayout.updArmyInfoWindow(smapGameStage.rescource,window,army,cardNowPage);

    }
    private void showArmyFormationWindow(Fb2Smap.ArmyData army) {
        if(army.getArmyType()==4||army.getArmyType()==8){
            selectListType=2;
        }else{
            selectListType=1;
        }
        hidCommonUi();
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
        w.setVisibleForEffect(true);
        smapGameStage.setIfFixed(true);
        if(cardNowPage!=0&&cardNowPage!=1){
            cardNowPage=0;
        }
        if(army.isUnitGroup()){
            game.gameLayout.updUnityArmyWindow(smapGameStage.rescource,w,army,cardNowPage);
        }else {
            game.gameLayout.updSingleArmyWindow(smapGameStage.rescource,w,army,cardNowPage);
        }
        //初始教程
        if(smapGameStage.getsMapDAO().masterData.getBtlType()==0&&checkOpenGroup("openUnitInfoGroup",true)){
            if(!game.gameConfig.ifBRecord(ResDefaultConfig.StringName.firstOpenUnitInfo)){
                addAssistantDialogue("prompt_dialogue_50",false);
                game.gameConfig.addBRecord(ResDefaultConfig.StringName.firstOpenUnitInfo);
            }
        }
    }

    public void showArmyFormationWindow(){
        Fb2Smap.ArmyData army=smapGameStage.selectedArmy;
        if(army==null){
            return;
        }
        showArmyFormationWindow(army);
    }

    public void showAirInfoUI(Fb2Smap.AirData air){
        hidCommonUi();
        WindowGroup window= windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
        window.setVisibleForEffect(true);
        smapGameStage.setIfFixed(true);
        if(cardNowPage!=0&&cardNowPage!=1){
            cardNowPage=0;
        }
        game.gameLayout.updAirWindow(smapGameStage.rescource,window,air,cardNowPage);
    }

    public boolean ifClickPlayerUnit(){
        if(smapGameStage.selectedArmy!=null){
            //  int i=(selectListPage-1)*4+selectListIndex-1;
            //   Fb2Smap.AirData  air=selectAirList.get((selectListPage-1)*4+selectListIndex-1);
            if(smapGameStage.selectedArmy==null||smapGameStage.selectedArmy.getLegionIndex()!=smapGameStage.getPlayerLegionIndex()){
                return false;
            }
        }else if(smapGameStage.selectedAir!=null){
            //Fb2Smap.ArmyData army= smapGameStage.selectedArmy;
            if(smapGameStage.selectedAir==null||smapGameStage.selectedAir.getLegionIndex()!=smapGameStage.getPlayerLegionIndex()){
                return false;
            }
        }else if(smapGameStage.selectedNul!=null){
            //Fb2Smap.ArmyData army= smapGameStage.selectedArmy;
            if(smapGameStage.selectedNul==null||smapGameStage.selectedNul.getLegionIndex()!=smapGameStage.getPlayerLegionIndex()){
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

      /*  public void armyDissolve(){
            Fb2Smap.ArmyData army=null;
            Fb2Smap.AirData air=null;
            int hexagon=-1;
            if(selectListType==3&&selectListIndex>0&&selectListIndex<=4){
              //  int i=(selectListPage-1)*4+selectListIndex-1;
                air=selectAirList.get((selectListPage-1)*4+selectListIndex-1);
                smapGameStage.getsMapDAO().checkTask(air.getHpRateF()==1,smapGameStage.playerLegionIndex,25,1);
            }else {
                army= smapGameStage.selectedArmy;
                smapGameStage.getsMapDAO().checkTask(army.getHpRateF()==1,smapGameStage.playerLegionIndex,25,1);
            }

            if(army!=null){
                smapGameStage.armyDissolve(army,true);
                hexagon=army.getHexagonIndex();
            }else if(air!=null){
                smapGameStage.airDissolve(air);
                hexagon=air.getRegionId();
            }
            *//*if(hexagon!=-1){
                Fb2Smap.BuildData build= smapGameStage.getsMapDAO().getBuildDataByRegion(hexagon);
                build.setBuildRound(build.getBuildRound()+1);
               // smapGameStage.setUpdAnimation(region);
                smapEffectStage.drawEffect(2,hexagon,-1);
            }*//*
        }*/


    /*    public void updArmyWeapon(){
            Fb2Smap.ArmyData army=null;
            Fb2Smap.AirData air=null;
            if(selectListType==3&&selectListIndex>0&&selectListIndex<=4){
                //int i=(selectListPage-1)*4+selectListIndex-1;
                air=selectAirList.get((selectListPage-1)*4+selectListIndex-1);
            }else {
                army= smapGameStage.selectedArmy;
            }

            int region=-1;
            if(army!=null){
                if( army.updWeaLv()){
                    region=army.getRegionId();
                }

            }else if(air!=null){
                if( air.updWeaLv()){
                    region=air.getRegionId();
                }
            }

            if(region!=-1){
                Fb2Smap.BuildData build= smapGameStage.getsMapDAO().getBuildDataByRegion(region);
                build.setBuildRound(build.getBuildRound()+1);
           //     smapGameStage.setUpdAnimation(region);
                smapEffectStage.drawEffect(2,region,-1,0.3f);
            }
            else {
                Gdx.app.error("updArmyWeaponError2","army is null");
            }

        }*/


    public GameOperateState getGameOperateState(){
        return smapGameStage.gameOperate;
    }



    //隐藏通用ui
    public void hidCommonUi(){
        promptText.setVisible(false);
        defaultWindow.setVisible(false);
        // windowGroups.get(ResConfig.Class.SMapScreenResourceGroupId).setVisible(false);
        //windowGroups.get(ResConfig.Class.SMapScreenRightBorderGroupId).setVisible(false);

        brightenAdvicePotion(-1);
        adviceIndex=0;
        updButtonPrompt();
        if(smapGameStage.getsMapDAO().roundState==0){
            ifSave=false;
        }
    }


    //显示边缘情况
    public void updRegionBorderGroup(int type,int page) {
        if(smapGameStage.getsMapDAO().setArmyListByRegion(smapGameStage.selectedRegionId, smapGameStage.getPlayerLegionIndex(),selectArmyList,selectNavyList,selectAirList,selectNulList)){
            showRightBorderInfo();
            selectListPage=page;
            selectListIndex=0;
            //右边栏 -1空 0战术卡 1陆军 2海军 3空军 4特殊武器
            if((type==1||type==-1)&&selectArmyList.size>0){
                selectListType=1;
                selectListSumPage=(selectArmyList.size-1)/4+1;
                game.gameLayout.updBorderWindowByArmy(smapGameStage.rescource,defaultWindow,selectArmyList,selectListPage);
            }else  if((type==2||type==-1)&&selectNavyList.size>0){
                selectListType=2;
                selectListSumPage=(selectNavyList.size-1)/4+1;
                game.gameLayout.updBorderWindowByArmy(smapGameStage.rescource,defaultWindow,selectNavyList,selectListPage);
            }else if((type==3||type==-1)&&selectAirList.size>0){
                selectListType=3;
                selectListSumPage=(selectAirList.size-1)/4+1;
                game.gameLayout.updBorderWindowByAir(smapGameStage.rescource,defaultWindow,selectAirList,selectListPage);
            }else if((type==4||type==-1)&&selectNulList.size>0){
                selectListType=4;
                selectListSumPage=(selectNulList.size-1)/4+1;
                game.gameLayout.updBorderWindowByNul(defaultWindow,selectNulList,selectListPage);
            }else {
                Gdx.app.error("updRegionBorderGroup1","no army");
            }
            if(GameUtil.ifHaveMultipleArrays(1,selectArmyList,selectNavyList,selectAirList,selectNulList)){
                defaultWindow.setButtonImageNotChangeSize(2000,game.getImgLists().getTextureByName("rightborder_button_"+selectListType).getTextureRegion());
            }else {//如果是空
                defaultWindow.setButtonImageNotChangeSize(2000,game.getImgLists().getTextureByName("rightborder_button_n").getTextureRegion());
            }
        }else {
            hidRightBorderInfo();
        }
    }

    //点击右边栏
    private void clickRightBorderCard(int index) {
        if(index==0){
            hidRightBorderInfo();
            return;
        }
      //  smapGameStage.smapFv.circleMark.isVisable=false;
        smapGameStage.smapFv.airRangeScale=0f;
        selectListIndex=index;
        Fb2Smap.AirData airData=null;
        Fb2Smap.NulcleData nulData=null;
        Fb2Smap.ArmyData armyData=null;
        if(smapGameStage.selectedArmy!=null&& smapGameStage.selectedArmy.armyActor!=null){
            smapGameStage.selectedArmy.armyActor. updArmyModel();
        }
        if(selectListType==1){//陆军
            armyData=selectArmyList.get((selectListPage-1)*4+index-1);
            if(smapGameStage.selectedArmy!=null&&smapGameStage.selectedArmy.armyActor!=null){
                smapGameStage.selectedArmy.armyActor.resetTarget();
            }
            smapGameStage.selectedArmy=armyData;
            smapGameStage.setCoord(armyData.getHexagonIndex());
            smapGameStage.clickPlayerArmy(armyData, smapGameStage.coord.id);
            if(armyData.ifHaveFeature(1)){
                smapGameStage.getSmapBg().setPotion(armyData.getHexagonIndex(),armyData.getMaxRange());
            }else {
                smapGameStage.getSmapBg().setPotion(-1,-1);
            }
        }else if(selectListType==2){//海军
            armyData=selectNavyList.get((selectListPage-1)*4+index-1);
            smapGameStage.selectedArmy=armyData;
            smapGameStage.setCoord(armyData.getHexagonIndex());
            smapGameStage.clickPlayerArmy(armyData, smapGameStage.coord.id);
            smapGameStage.getSmapBg().setPotion(-1,-1);
        }else if(selectListType==3){//空军
            airData=selectAirList.get((selectListPage-1)*4+index-1);
            selectStr="airIndex:"+airData.getAirIndex();
            //smapGameStage.selectedConnect= smapGameStage.getsMapDAO().getConnectData(airData.getRegionId());
            smapGameStage.selectedAir=airData;
            if(airData.getAirRound()==0){
                smapGameStage.selectedHexagonIds.clear();
                armyData=airData.armyData;
                smapGameStage.selectedHexagonIds =airData.getCanActRegions(smapGameStage.selectedHexagonIds,0);
                smapGameStage.smapFv.setDrawMarkForAir(airData.getAirId(),airData.getRegionId(),smapGameStage.selectedHexagonIds);
                smapGameStage.gameOperate= selectRegionToAirTarget;
                smapGameStage.smapFv.setDrawCircle(airData.getHexagon(),airData.getMinRange(),airData.getMaxRange());
            }
            if(airData.getArmyHexagon()!=-1){
                smapGameStage.getSmapBg().setPotion(airData.getArmyHexagon(),airData.getMaxRange());
            }else {
                smapGameStage.getSmapBg().setPotion(airData.getRegionId(),airData.getMaxRange());
            }
        }else if(selectListType==4){//特殊
            nulData=selectNulList.get((selectListPage-1)*4+index-1);
            if(nulData.getNucleRound()>0){ return;}
            // smapGameStage.selectedConnect= smapGameStage.getsMapDAO().getConnectData(nulData.getRegionId());
            //  smapGameStage.showArrow(-1, smapGameStage.selectedConnect,3,true);
            smapGameStage.selectedHexagons.clear();
            smapGameStage.selectedNul=nulData;
            smapGameStage.selectedHexagons=smapGameStage.getsMapDAO().getNulCanActPotionAndSetColorTIIM1(nulData,smapGameStage.selectedHexagons);
            smapEffectStage.drawMark(  smapGameStage.selectedHexagons,smapGameStage.getClickId(),2);

            smapGameStage.smapFv.setDrawCircle(nulData.getRegionId(),0,nulData.getActRange());

            // smapGameStage.smapFv.setDrawMarkForAirAndNul(nulData.getNuclearId(),nulData.getRegionId(),smapGameStage.selectedHexagons);
            smapGameStage.gameOperate= selectRegionToNulTarget;
            smapGameStage.getSmapBg().setPotion(-1,-1);
        }
        smapGameStage.selectedAir=airData;
        smapGameStage.selectedNul=nulData;
    }

    private void updRightBorderForLastPage(){
        if(selectListSumPage ==1){
            return;
        }else if(selectListPage<=1){
            updRegionBorderGroup(selectListType,selectListSumPage);
        }else {
            updRegionBorderGroup(selectListType,selectListPage-1);
        }
    }

    private void updRightBorderForNextPage(){
        if(selectListSumPage ==1){
            return;
        }else if(selectListPage<selectListSumPage){
            updRegionBorderGroup(selectListType,selectListPage+1);
        }else {
            updRegionBorderGroup(selectListType,1);
        }
    }

    //切换右边栏的类型
    private void updRightBorderGroupType(){
        for(int i=0;i<4;i++){
            selectListType++;
            if(selectListType>4){
                selectListType=1;
            }
            if(getSelectListCount(selectListType)){
                break;
            }
        }
        smapGameStage.resetDefaultState();
        updRegionBorderGroup(selectListType,1);
    }

    private boolean getSelectListCount(int type){
        switch (type){
            case 1:if(selectArmyList.size>0){return true;}else {return false;}
            case 2:if(selectNavyList.size>0){return true;}else {return false;}
            case 3:if(selectAirList.size>0){return true;}else {return false;}
            case 4:if(selectNulList.size>0){return true;}else {return false;}
        }
        return false;
    }


    public void asyncRoundBeginToShowArrow(final IntArray ids, final int moveSecond, final int zoomstate, final float zoomMin, final float zoomMax ){
        CHAsyncTask task=new CHAsyncTask("asyncRoundBeginToShowArrow",0) {
            @Override
            public void onPreExecute() {
                for(int i=0,iMax=ids.size;i<iMax;i++){
                    smapGameStage.cam.moveCameraToHexagon(ids.get(i),moveSecond,zoomstate,zoomMin,zoomMax);
                    try {
                        Thread.sleep(500*moveSecond);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                game.tempUtil.disposeTempIntArray(ids);
            }

            @Override
            public void onPostExecute(String result) {
                smapGameStage.tempActorListClear();
                nextRoundEndShow();
            }
            @Override
            public String doInBackground() {
                return null;
            }
        };
        game.asyncManager.loadTask(task);
        game.asyncManager.update();
    }



    public void unitLoadNul(){
        Fb2Smap.ArmyData army=null;
        Fb2Smap.AirData air=null;
        if(selectListType==3&&selectListIndex>0&&selectListIndex<=4){
            air=selectAirList.get((selectListPage-1)*4+selectListIndex-1);
        }else {
            army= smapGameStage.selectedArmy;
        }

        int region=-1;
        if(army!=null){
            //army.updArmyAbility(6);
            region=army.getRegionId();
            //MainGame game, SMapGameStage smapStage, WindowGroup window, Fb2Smap.BuildData b, Array<Fb2Smap.NulcleData> nulcles
            game.gameLayout.updCardWindowForArmyLoadNul(smapGameStage,windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId),army.getBuildData(),army,selectNulList);
            showCardUiToLoad();
            selectCardType=101;
        }else if(air!=null){
            // air.updWeaLv();
            game.gameLayout.updCardWindowForAirLoadNul(smapGameStage,windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId),air.getBuildData(),air,selectNulList);

            selectCardPotion=0;
            showCardUiToLoad();
            selectCardType=102;
        }

        else {
            Gdx.app.error("updArmyWeaponError2","army is null");
        }

    }


    //打开卡牌界面作为load功能使用
    public void showCardUiToLoad(){
        hidCommonUi();
        closeOtherGroupExceptSpecified(ResDefaultConfig.Class.SMapScreenGameCardGroupId);
        WindowGroup window= windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId);

        //右下角显示关闭标签
        window.getButton(10).setVisible(true);
        smapGameStage.setIfFixed(true);
        window.setVisible(true);
        selectedCard(0);
    }


    private void useTacticsReady(){

        int hexagon=-1;
        boolean ifDraw=true; IntIntMap rs;
        switch (selectCardId){
            case 1001:useTactics(selectCardId,smapGameStage.selectedBuildData.getRegionId(),-1,hexagon,ifDraw);break;
            case 1002:useTactics(selectCardId,smapGameStage.selectedBuildData.getRegionId(),-1,hexagon,ifDraw);break;
            case 1003:
                if (smapGameStage.getsMapDAO().masterData.getPlayerMode()==2) {
                    smapGameStage.gameOperate=selectHexagonToTactic;
                    smapGameStage.selectedTargetRegion=smapGameStage.selectedBuildData.getRegionId();
                    rs = smapGameStage.getsMapDAO().getPotionForBorderRegionTIIM1(smapGameStage.selectedBuildData.getRegionId(),4,smapGameStage.selectedHexagons);
                    rs.remove(smapGameStage.selectedBuildData.getRegionId(),-1);
                    smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                }else{
                    smapGameStage.gameOperate=selectHexagonToTactic;
                    smapGameStage.selectedTargetRegion=-1;
                    rs = smapGameStage.getsMapDAO().getPotionForCanSelectedArmyTIIM1(smapGameStage.selectedBuildData.getRegionId(),1,smapGameStage.selectedHexagons);
                    smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                }
                break;
            case 1004:smapGameStage.gameOperate=selectHexagonToTactic;
                smapGameStage.selectedTargetRegion=smapGameStage.selectedBuildData.getRegionId();
                rs = smapGameStage.getsMapDAO().getPotionForBorderRegionTIIM1(smapGameStage.selectedBuildData.getRegionId(),3,smapGameStage.selectedHexagons);
                smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                break;
            case 1005:smapGameStage.gameOperate=selectHexagonToTactic;
                smapGameStage.selectedTargetRegion=smapGameStage.selectedBuildData.getRegionId();
                rs = smapGameStage.getsMapDAO().getPotionForBorderRegionTIIM1(smapGameStage.selectedBuildData.getRegionId(),3,smapGameStage.selectedHexagons);
                smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                break;
            case 1006:useTactics(selectCardId,smapGameStage.selectedBuildData.getRegionId(),-1,hexagon,ifDraw);break;
            case 1007:useTactics(selectCardId,smapGameStage.selectedBuildData.getRegionId(),-1,hexagon,ifDraw);break;
            case 1008:
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
                    useTactics(selectCardId,smapGameStage.selectedBuildData.getRegionId(),-1,hexagon,ifDraw);break;
                }else{
                    smapGameStage.gameOperate=selectHexagonToTactic;
                    smapGameStage.selectedTargetRegion=smapGameStage.selectedBuildData.getRegionId();
                    rs = smapGameStage.getsMapDAO().getPotionForCanSelectedArmyTIIM1(smapGameStage.selectedBuildData.getRegionId(),2,smapGameStage.selectedHexagons);
                    smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                }
                break;
            case 1009:smapGameStage.gameOperate=selectHexagonToTactic;
                smapGameStage.selectedTargetRegion=smapGameStage.selectedBuildData.getRegionId();
                rs = smapGameStage.getsMapDAO().getPotionForCanSelectedArmyTIIM1(smapGameStage.selectedBuildData.getRegionId(),3,smapGameStage.selectedHexagons);
                smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                break;
            case 1010:useTactics(selectCardId,smapGameStage.selectedBuildData.getRegionId(),-1,hexagon,ifDraw);break;
            case 1011:
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
                    //
                    //smapGameStage.selectedTargetRegion=smapGameStage.selectedBuildData.getRegionId();
                    //rs = smapGameStage.getsMapDAO().getPotionForBorderRegionTIIM1(smapGameStage.selectedBuildData.getRegionId(),4,smapGameStage.selectedHexagons);
                    //rs.remove(smapGameStage.selectedBuildData.getRegionId(),-1);

                    smapGameStage.gameOperate=GameOperateState.selectHexagonToLegionUnitAirborne;
                    //smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                    smapGameStage.selectedHexagonIds =smapGameStage.selectedBuildData.getAirborneHexagons(smapGameStage.selectedHexagonIds,1);
                    smapGameStage.smapFv.setDrawMarkForAir(1505,smapGameStage.selectedBuildData.getRegionId(),smapGameStage.selectedHexagonIds);
                    //  smapGameStage.gameOperate= selectHexagonToAirborneTactical;

                }else{
                    smapGameStage.gameOperate=selectHexagonToTactic;
                    smapGameStage.selectedTargetRegion=-1;
                    rs = smapGameStage.getsMapDAO().getPotionForCanSelectedArmyTIIM1(smapGameStage.selectedBuildData.getRegionId(),1,smapGameStage.selectedHexagons);
                    smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                }
                break;
            case 1012:smapGameStage.gameOperate=selectHexagonToTactic;
                smapGameStage.selectedTargetRegion=smapGameStage.selectedBuildData.getRegionId();
                rs = smapGameStage.getsMapDAO().getPotionForBorderRegionTIIM1(smapGameStage.selectedBuildData.getRegionId(),3,smapGameStage.selectedHexagons);
                smapGameStage.smapFv.setDrawMark(rs,smapGameStage.getClickId(), 0);
                break;
            case 1013:useTactics(selectCardId,smapGameStage.selectedBuildData.getRegionId(),-1,hexagon,ifDraw);break;
            case 1014:useTactics(selectCardId,smapGameStage.selectedBuildData.getRegionId(),-1,hexagon,ifDraw);break;
        }

        hidUi(true);


    }

    //使用战术卡  region0使用战术卡的来源坐标 region1目标坐标
    public void useTactics(int cardId,int region0,int region1,int hexagon,boolean ifDraw) {

        // Gdx.app.log("useTactics",selectCardId+"");

        /*int region=0;
        int hexagon=0;
        int lv=0;
        boolean ifDraw=true;*/
        int lv=smapGameStage.getsMapDAO().getBuildDataByRegion(region0).getTacticsLv(selectCardId);

        switch (cardId){
            case 1001:smapGameStage.getsMapDAO().tactic_ImposeTax(region0,lv,ifDraw);break;
            case 1002:smapGameStage.getsMapDAO().tactic_Inspiring(region0,lv,ifDraw);break;
            case 1003:
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
                    smapGameStage.getsMapDAO().tactic_QuickLegionTravel(region0,region1,lv,ifDraw);
                }else{
                    smapGameStage.getsMapDAO().tactic_QuickTravel(hexagon,region1,lv,ifDraw);
                }
                break;
            case 1004:smapGameStage.getsMapDAO().tactic_SpreadRumors(region1,lv,ifDraw);break;
            case 1005:
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
                    smapGameStage.getsMapDAO().tactic_SpyingMessageForLegionUnit(region1,lv,ifDraw);
                }else {
                    smapGameStage.getsMapDAO().tactic_SpyingMessage(region1,lv,ifDraw);
                }
                break;
            case 1006:smapGameStage.getsMapDAO().tactic_EmergencyPower(region0,lv,ifDraw);break;
            case 1007:smapGameStage.getsMapDAO().tactic_MartialLaw(region0,lv,ifDraw);break;


            case 1008:
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
                    smapGameStage.getsMapDAO().tactic_ActAgainForLegionUnit(region0,lv,ifDraw);
                }else{
                    smapGameStage.getsMapDAO().tactic_ActAgain(hexagon,lv,ifDraw);
                }
                break;
            case 1009:smapGameStage.getsMapDAO().tactic_FirstAid(hexagon,lv,ifDraw);break;
            case 1010:smapGameStage.getsMapDAO().tactic_RepairBuild(region0,lv,ifDraw);break;
            case 1011:
                if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==2){
                    smapGameStage.getsMapDAO().tactic_AirFreightForLegionUnit(region0,region1,lv,ifDraw);
                }else{
                    smapGameStage.getsMapDAO().tactic_AirFreight(region0,region1,lv,ifDraw);
                }
                break;
            case 1012:smapGameStage.getsMapDAO().tactic_Bombardment(region0,region1,lv,ifDraw);break;
            case 1013:smapGameStage.getsMapDAO().tactic_NuclearBlasting(region0,lv,ifDraw);break;
            case 1014:smapGameStage.getsMapDAO().tactic_MilitaryTraining(region0,lv,ifDraw);break;
        }
        Fb2Smap.BuildData b=smapGameStage.getsMapDAO().getBuildDataByRegion(region0);
        if(smapGameStage.getsMapDAO().ifTriggerSpirit(30)){

        }else {
            b.addBuildRound(1);
        }
    }


    public void autoOption() {
        Fb2Smap.LegionData pl=smapGameStage.getPlayerLegion();
        int money=pl.getMoney();
        int industry=pl.getIndustry();
        int tech=pl.getTech();
        int food=pl.getFood();
        String b= smapGameStage.getsMapDAO().autoPlayerRegionBuild();
        if(ComUtil.isEmpty(b)){
            b=game.gameMethod.getStrValueT("prompt_none");
        }
        smapGameStage.getsMapDAO().addPromptData(
                game.gameMethod.getStrValueT("prompt_Report"),
                b,
                game.gameMethod.getStrValueT("prompt_reportEnd",money-pl.getMoney(),industry-pl.getIndustry(),tech-pl.getTech(),food-pl.getFood()),smapGameStage.getPlayerLegionIndex(),smapGameStage.getPlayerLegionIndex(),false
        );
        //showPromptGroup(true);

        updResourceForPlayer();
    }



    public void updPromptText(){
        if(promptText.isVisible()){
            setPromptText(promptTextIndex);
        }
    }


    //value
    public void setPromptText( int value) {
        ImageButton button=null;
        Image image=null;
        promptTextIndex=value;

        Fb2Smap.ArmyData army=null;
        Fb2Smap.AirData air=null;
        Element xmlE=null;
        Fb2Smap.LegionData pl=smapGameStage.getPlayerLegion();
        Fb2Smap.LegionData sl=smapGameStage.getSelectLegionData();
        tempVector2.x=0;
        tempVector2.y=0;
        promptText.setScale(1);
        promptText.getLabel().setScale(1);
        promptText.getLabel().setFontScale( 0.5f*game.gameConfig.gameFontScale);
        //XmlReader.Element ui;
        int lv=0;
        if(value>=1&&value<=8||value==21||value==30||value==31||value==32||value==33||value==34){//兵种类
            if(selectListType==3&&selectListIndex>0&&selectListIndex<=4){
                //  int i=(selectListPage-1)*4+selectListIndex-1;
                air=selectAirList.get((selectListPage-1)*4+selectListIndex-1);
            }else {
                army= smapGameStage.selectedArmy;
            }
        }else if(selectListType==3&&smapGameStage.selectedAir!=null){
            air=smapGameStage.selectedAir;
        }else if( smapGameStage.selectedArmy!=null){
            army= smapGameStage.selectedArmy;
        }
        switch (value){
            case 0://weather
                if(smapGameStage.selectedBuildData==null){return;}
               // button =defaultWindow.getButton(1000);
                int wId=smapGameStage.selectedBuildData.getWeatherId();
                xmlE= game.gameConfig.getDEF_WEATHER().getElementById(wId);
                image=defaultWindow.getImage(1000);
                //game.gameLayout.setTButtonTextByXmlE(prpmptText,xmlE,button.getX(),button.getY());

                //button.getX()+button.getWidth(),image.getY(),image.getImageHeight(),10,2

               // promptText.getLabel().setFontScale( 0.5f*game.gameConfig.gameFontScale/defaultWindow.getScaleY());

                promptText.getLabel().setFontScale( 0.8f*game.gameConfig.gameFontScale*defaultWindow.getScaleY());
                tempVector2.setZero();
                tempVector2=image.localToStageCoordinates(tempVector2);
                game.gameLayout.setTButtonTextAndPotionAndAdaptWidth(promptText,tempVector2.x+image.getWidth()*defaultWindow.getScaleX(),tempVector2.y,image.getWidth()*defaultWindow.getScaleX(),image.getHeight()*defaultWindow.getScaleY(),10,1,game.gameMethod.getStrValue("text_prompt_0",ComUtil.getSymbolNumer(xmlE.getInt("move",0)),ComUtil.getSymbolNumer(xmlE.getInt("view",0)),ComUtil.getSymbolNumer(xmlE.getInt("morale",0)),xmlE.getInt("armyEfficiency",100),xmlE.getInt("airEfficiency",100),xmlE.getInt("moneyExtraRate",0)+100,xmlE.getInt("industryExtraRate",0)+100,xmlE.getInt("techExtraRate",0)+100,xmlE.getInt("foodExtraRate",0)+100,xmlE.getInt("replyRate",100)));
                promptText.getLabel().setAlignment(Align.center);
                break;



            case 1://acklv
                WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(4);


                tempVector2=button.localToStageCoordinates(tempVector2);
                float  bX=tempVector2.x+71*w.getScaleX();
                float  bY=tempVector2.y;
                float bW=(button.getWidth()-71)*w.getScaleX();
                float bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());



                if(army!=null){//army     (TextButton t, float x, float y, float w, int columnCount, String text
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_1",army.getUnitGroup1Lv(),   army.getUnitGroup1Lv()* game.resGameConfig.addDamageForAckLv +game.gameMethod.getLegionEffectValueForUnitCrit(smapGameStage.getPlayerLegion(),army.getArmyType(),army.getUnitArmyId0()), game.resGameConfig.critDamageRatio));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,button.getY(),bW,bH,game.gameMethod.getStrValueT("text_prompt_1",air.getAckLv(),air.getAckLv()* game.resGameConfig.addDamageForAckLv +game.gameMethod.getLegionEffectValueForUnitCrit(smapGameStage.getPlayerLegion(),air.getAirType(),air.getAirId()), game.resGameConfig.critDamageRatio));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 2://resLv
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(5);


                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());


                if(army!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_2",army.getUnitGroup3Lv(), game.resGameConfig.resetUnitMoraleMax, game.resGameConfig.resetUnitMoraleChance +army.getUnitGroup3Lv()* game.resGameConfig.addRestoreMoraleChanceForRankLv, game.resGameConfig.extraReplyMoraleRatio*army.getUnitGroup3Lv(),game.resGameConfig.cityStabilityChangeValueMax,ComUtil.limitValue(army.getUnitGroup3Lv()*3,15,30)));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_2_1",air.getSpyLv(),air.getSpyLv()* game.resGameConfig.extraReplyAirGoodsRatio, game.resGameConfig.resetUnitMoraleMax, game.resGameConfig.resetUnitMoraleChance +air.getSpyLv()* game.resGameConfig.addRestoreMoraleChanceForRankLv, game.resGameConfig.extraReplyMoraleRatio*air.getSpyLv()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 3://actlv
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(6);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());


                if(army!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_3",army.getUnitGroup5Lv(),army.getUnitGroup5Lv()/3+1));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_3_1",air.getActLv()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 4://deflv
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(7);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());


                if(army!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_4",army.getUnitGroup2Lv(),army.getUnitGroup2Lv()* game.resGameConfig.addDefChanceForRankLvPoor));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_4",air.getDefLv(),air.getDefLv()* game.resGameConfig.addDefChanceForRankLvPoor));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 5://suplv
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(8);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());


                if(army!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_5",army.getUnitGroup4Lv(),(int)(army.getArmyHpMax()*game.resGameConfig.unityReplyHpRatio *(army.getUnitGroup4Lv()+1)),ComUtil.limitValue(army.getUnitGroup4Lv()*3,10,30)));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_5_1",air.getSupLv(),(int)(air.getAirHpMax()*game.resGameConfig.unityReplyHpRatio *(air.getSupLv()+1))));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 6://weaponlv
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(9);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());


                if(army!=null){//army
                    xmlE=game.gameConfig.getDEF_WEAPON().getElementById( army.armyXmlE0.getInt("weapon"));
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_6",army.getUnitWealv0Value()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  air.airXmlE.getInt("weapon"));
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_6",air.getWeaLv()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 7://commander
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(10);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());


                if(army!=null){//army
                    Fb2Smap.GeneralData g=army.getGeneralData();
                    if(g!=null){
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_7","rankName_"+army.getArmyRank(),g.getAbilityValue(),g.getPolitical()));
                        promptText.setVisible(true);
                        promptText.getLabel().setAlignment(Align.center);
                    }else{
                        promptText.setVisible(false);
                    }
                }else if(air!=null){//air
                    Fb2Smap.GeneralData g=air.getGeneralData();
                    if(g!=null){
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_7","rankName_"+air.getAirRank(),g.getAbilityValue(),g.getPolitical()));
                        promptText.setVisible(true);
                        promptText.getLabel().setAlignment(Align.center);
                    }else{
                        promptText.setVisible(false);
                    }
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 8://armyType
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(11);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(army!=null){//army
                    // xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(army.getArmyId()).getInt("weapon"));
                    int weaponId= army.armyXmlE0.getInt("weapon",100);

                    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_8","weapon_name_"+weaponId,game.gameMethod.getWeaponValue(weaponId,1,army.getUnitWealv0Value()),
                            game.gameMethod.getWeaponValue(weaponId,2,army.getUnitWealv0Value()),
                            game.gameMethod.getWeaponValue(weaponId,3,army.getUnitWealv0Value()),
                            game.gameMethod.getWeaponValue(weaponId,4,army.getUnitWealv0Value()),
                            game.gameMethod.getWeaponValue(weaponId,5,army.getUnitWealv0Value()),
                            game.gameMethod.getWeaponValue(weaponId,6,army.getUnitWealv0Value()),
                            game.gameMethod.getWeaponValue(weaponId,8,army.getUnitWealv0Value())));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    int  weaponId= air.airXmlE.getInt("weapon",100);
                    //  xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(air.getAirId()).getInt("weapon"));
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_8","weapon_name_"+weaponId,game.gameMethod.getWeaponValue(weaponId,1,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,2,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,3,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,4,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,5,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,6,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,8,air.getWeaLv())));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 9://路线说明
                image=defaultWindow.getImage(2);

                //   int ambition=smapGameStage.getsMapDAO().getPlayerAmbition();
                //   if(ambition==100){  promptText.setVisible(false);return;}
                if(image==null||smapGameStage.getsMapDAO().chiefData==null){
                    return;
                }
                String str;
              /*  if(ambition>100){//霸权 51~100
                    str=game.gameMethod.getStrValueT("text_prompt_9_r",(ambition-49)/4,60-ambition/2,(ambition-49)/2+5,ambition/2+30,(ambition-49)/4+10,(ambition-49)/2*pl.varRegionCount/100,ambition/2,(126-ambition)*pl.varRegionCount/100,ambition);
                }else{//王权 49~0
                    str=game.gameMethod.getStrValueT("text_prompt_9_b",(51-ambition)/4+20,(52-ambition)/4+1,5+(50-ambition)/3,(ambition+21)/10,50-ambition,(52-ambition)/4+1,(51-ambition)/2,ambition+50);
                 }*/
                //路线:{0},威望:{1}\n政治点:{2},月政治点:{3}\n当前稳定度:{4},月稳定度:{5}\n战争支持度:{6},恶名:{7}\n金钱效率{8}%,资源效率{9}%\n识字率{10}%,人力效率:{11}%\n军事实力:{12},国家影响力:{13}
                tempVector2.setZero();
                promptText.getLabel().setFontScale( 0.8f*game.gameConfig.gameFontScale*defaultWindow.getScaleY());
                tempVector2=image.localToStageCoordinates(tempVector2);
                str=game.gameMethod.getStrValueT("text_prompt_9",DefDAO.getConceptStr(smapGameStage.getsMapDAO().getPlayerAmbition()),smapGameStage.getsMapDAO().getPlayerAmbitionValue(),
                        smapGameStage.getsMapDAO().getChiefHarmony(),smapGameStage.getsMapDAO().chiefData.varHarmonyChange,smapGameStage.getsMapDAO().getPlayerLegionData().getStability(),smapGameStage.getsMapDAO().chiefData.varLegionStabilityChange,
                        smapGameStage.getsMapDAO().chiefData.getWarSupport(),smapGameStage.getsMapDAO().chiefData.getReputation(),smapGameStage.getsMapDAO().chiefData.varMoneyEfficiencyChange,smapGameStage.getsMapDAO().chiefData.varResEfficiencyChange,
                        smapGameStage.getsMapDAO().chiefData.getLiteracy(),smapGameStage.getsMapDAO().getPlayerLegionData().getPopEfficency(),
                        smapGameStage.getsMapDAO().getPlayerLegionData().getAllArmySumNum(), smapGameStage.getsMapDAO().playerInfo.worldProgress
                );
                // game.gameLayout.setTButtonTextAndPotionAndAdaptHeight(promptText,image.getX()-image.getWidth()/2,image.getY(),image.getWidth()*2,str);
                promptText.getLabel().setAlignment(Align.center,Align.left);
                game.gameLayout.setTButtonTextAndPotionAndAdaptHeight(promptText,tempVector2.x,tempVector2.y,image.getWidth()*defaultWindow.getScaleX(),str);
                promptText.setVisible(true);
                break;
            case 10:
                if(smapGameStage.selectedBuildData==null){return;}
                button =defaultWindow.getButton(1000);
                wId=smapGameStage.selectedBuildData.getBuildWonder();
                String wondStr=null;
                if(smapGameStage.selectedBuildData.isCapital()){
                    wondStr=game.gameMethod.getStrValueT("buildType_info_capital");
                }else{
                    wondStr= game.gameMethod.getStrValueT("buildwonder_info_"+wId);
                }

                if(game.resGameConfig.ifTerrainEffect){
                    image=defaultWindow.getImage(1000);
                    tempVector2.setZero();
                    tempVector2=image.localToStageCoordinates(tempVector2);
                    xmlE=game.gameConfig.getDEF_TERRAIN().getElementById(smapGameStage.getSelectedActualTerrain());

                    promptText.getLabel().setFontScale( 0.8f*game.gameConfig.gameFontScale*defaultWindow.getScaleY());
                    if(xmlE!=null){

                        game.gameLayout.setTButtonTextAndPotionAndAdaptWidth(promptText,tempVector2.x+image.getWidth()*defaultWindow.getScaleX(),tempVector2.y,image.getWidth()*defaultWindow.getScaleX(),image.getHeight()*defaultWindow.getScaleY(),10,2,wondStr+game.gameMethod.getStrValueT("buildwonder_info_1000",xmlE.getInt("defInf"),xmlE.getInt("defArt"),xmlE.getInt("defArm"),xmlE.getInt("defAir"),xmlE.getInt("defShip"),xmlE.getInt("exrLos")));

                        promptText.getLabel().setAlignment(Align.center);
                    }else{
                        promptText.setVisible(false);
                    }
                }else {
                    tempVector2.setZero();
                    tempVector2=button.localToStageCoordinates(tempVector2);
                    game.gameLayout.setTButtonTextAndPotionAndAdaptWidth(promptText,tempVector2.x+button.getWidth()*defaultWindow.getScaleX(),tempVector2.y,button.getWidth()*defaultWindow.getScaleX(),button.getHeight()*defaultWindow.getScaleY(),
                            10,2,wondStr);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;

            case 11://步兵
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(1);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_11",pl.getInfantryLvMax(),pl.getInfantryCardMax(),pl.varInfantryNum,pl.varInfantryCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 12://炮兵
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(2);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_12",pl.getCannonLvMax(),pl.getArtilleryCardMax(),pl.varArtilleryNum,pl.varArtilleryCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 13://装甲
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(3);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_13",pl.getTankLvMax(),pl.getArmorCardMax(),pl.varArmorNum,pl.varArmorCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 14://舰船
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(4);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_14",pl.getNavyLvMax(),pl.getNavyCardMax(),pl.varNavyNum,pl.varNavyCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 15://飞机
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(5);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_15",pl.getAirLvMax(),pl.getAirCardMax(),pl.varAirNum,pl.varAirCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 16://潜艇
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(6);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_16",pl.getNavyLvMax(),pl.getSubmarineCardMax(),pl.varSubmarineNum,pl.varSubmarineCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 17://飞弹
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(7);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_17",pl.getMissileLvMax(),pl.getMissileCardMax(),pl.varMissileNum,pl.varMissileCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 18://弹头
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(8);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_18",pl.getNuclearLvMax(),pl.getNuclearCardMax(),pl.varNuclearNum,pl.varNuclearCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 19://要塞
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(9);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_19",pl.getDefenceLvMax(),pl.getDefenceCardMax(),pl.varDefenceNum,pl.varDefenceCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 20://将领
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(10);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(pl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_20",pl.getMilitaryAcademyLv(),pl.getGeneralCardMax(),pl.varGeneralNum,pl.varGeneralCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;



            case 21://装甲
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId);
                button =w.getButton(12);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());


                if(army!=null){//army
                    xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  army.armyXmlE0.getInt("weapon"));
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_21",army.getArmorByDirect(),100-100*game.resGameConfig.unitArmorBaseBeardamage/(game.resGameConfig.unitArmorBaseBeardamage+army.getArmorByDirect())    ));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  air.airXmlE.getInt("weapon"));
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_21",air.getArmor(),100-100*game.resGameConfig.airArmorBaseBeardamage/(game.resGameConfig.airArmorBaseBeardamage+air.getArmor())));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;



            case 22://生命
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(1);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                army=smapGameStage.selectedArmy;

                if(army!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_22",army.getArmyHpNow(),army.getArmyHpMax(),army.getArmyKills(),army.getKillSum(),army.getPopulation()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(smapGameStage.selectedAir!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_22",smapGameStage.selectedAir.getAirHpNow(),smapGameStage.selectedAir.getAirHpMax(),smapGameStage.selectedAir.getAirKills(),smapGameStage.selectedAir.getKillSum(),smapGameStage.selectedAir.getPopulation()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;



            case 23://防空
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(2);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                army=smapGameStage.selectedArmy;
                if(army!=null){//army
                    if(army.isUnitGroup()){
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_23",ComUtil.getSymbolNumer(army.airStrikeBonus), army.getArmorByDirect()*(100+army.airDefendBonus)/100,100-100*game.resGameConfig.unitArmorForairDenfenceBaseBeardamage/(game.resGameConfig.unitArmorForairDenfenceBaseBeardamage+army.getArmorByDirect()*(100+army.airDefendBonus)/100)));
                    }else {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_21",army.getArmorByDirect(),100-100*game.resGameConfig.unitArmorBaseBeardamage/(game.resGameConfig.unitArmorBaseBeardamage+army.getArmorByDirect())    ));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(smapGameStage.selectedAir!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_21",smapGameStage.selectedAir.getArmor(),100-100*game.resGameConfig.airArmorBaseBeardamage/(game.resGameConfig.airArmorBaseBeardamage+smapGameStage.selectedAir.getArmor())    ));
                }else{
                    promptText.setVisible(false);
                }
                break;


            case 24://射程
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(3);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                army=smapGameStage.selectedArmy;
                if(army!=null){//army
                    if(army.isUnitGroup()){
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_24",army.getMinRange(),army.getMaxRange(),army.rangedAttack,army.rangeCritChance));
                    }else{
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_24_1",army.getMinRange(),army.getMaxRange()));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(smapGameStage.selectedAir!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_24_1",smapGameStage.selectedAir.getMinRange(),smapGameStage.selectedAir.getMaxRange()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 25://攻击
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(4);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                army=smapGameStage.selectedArmy;

                if(army!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_25",army.armyMinAttack,army.armyMaxAttack));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(smapGameStage.selectedAir!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_25",smapGameStage.selectedAir.getMinAttack(),smapGameStage.selectedAir.getMaxAttack()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 26://移动
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(5);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                army=smapGameStage.selectedArmy;
                if(army!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_26",army.getMovement(),army.getSearchRange()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(smapGameStage.selectedAir!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_26_1",smapGameStage.selectedAir.getAirGoodsNow(),smapGameStage.selectedAir.getAirGoodsMax()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 27://commander
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(6);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());


                if(army!=null){//army
                    Fb2Smap.GeneralData g=army.getGeneralData();
                    if(g!=null){
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_7","rankName_"+army.getArmyRank(),g.getAbilityValue(),g.getPolitical()));
                        promptText.setVisible(true);
                        promptText.getLabel().setAlignment(Align.center);
                    }else{
                        promptText.setVisible(false);
                    }
                }else if(smapGameStage.selectedAir!=null){//air
                    Fb2Smap.GeneralData g=smapGameStage.selectedAir.getGeneralData();
                    if(g!=null){
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_7","rankName_"+smapGameStage.selectedAir.getAirRank(),g.getAbilityValue(),g.getPolitical()));
                        promptText.setVisible(true);
                        promptText.getLabel().setAlignment(Align.center);
                    }else{
                        promptText.setVisible(false);
                    }
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 28://armyType
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(7);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(army!=null){//army
                    // xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(army.getArmyId()).getInt("weapon"));
                    int weaponId= army.armyXmlE0.getInt("weapon",100);
                    int weaLv=army.getUnitWealv0Value();
                    if(army.isUnitGroup()){
                        weaLv+=army.getUnitGroupSameArmyIdCount(0);
                    }
                    Fb2Smap.LegionData al=army.getLegionData();
                    weaLv=ComUtil.min(weaLv,al.getCardTechLv(army.armyXmlE0.getInt("id"),army.armyXmlE0.getInt("type")));
                    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_8","weapon_name_"+weaponId,game.gameMethod.getWeaponValue(weaponId,1,weaLv),
                            game.gameMethod.getWeaponValue(weaponId,2,weaLv),
                            game.gameMethod.getWeaponValue(weaponId,3,weaLv),
                            game.gameMethod.getWeaponValue(weaponId,4,weaLv),
                            game.gameMethod.getWeaponValue(weaponId,5,weaLv),
                            game.gameMethod.getWeaponValue(weaponId,6,weaLv),
                            game.gameMethod.getWeaponValue(weaponId,8,weaLv)));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    int  weaponId= air.airXmlE.getInt("weapon",100);
                    //  xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(air.getAirId()).getInt("weapon"));
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_8","weapon_name_"+weaponId,game.gameMethod.getWeaponValue(weaponId,1,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,2,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,3,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,4,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,5,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,6,air.getWeaLv()),
                            game.gameMethod.getWeaponValue(weaponId,8,air.getWeaLv())));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 29://commander
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(6);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl==null){
                    promptText.setVisible(false);
                    break;
                }
               Fb2Smap.BuildData sb=sl.getCapital();
                if(sb==null){
                    promptText.setVisible(false);
                    break;
                }
                army=sb.getRegionUnit();
                if(army!=null&&army.getLegionIndex()==sl.getLegionIndex()){//army
                    Fb2Smap.GeneralData g=army.getGeneralData();
                    if(g!=null){
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_7","rankName_"+army.getArmyRank(),g.getAbilityValue(),g.getPolitical()));
                        promptText.setVisible(true);
                        promptText.getLabel().setAlignment(Align.center);
                    }else{
                        promptText.setVisible(false);
                    }
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 30://装甲
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(31);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(army!=null){//army

                    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            army.getArmyTypeStr()+":"+game.gameMethod.getUnitTypeForFeatureStr(army));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH, game.gameMethod.getStrValueT("armyType_5")+":"+
                            game.gameMethod.getStrValueT("feature_air"));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 31://技能1
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(32);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(army!=null){//army
                    if(cardNowPage==0){
                        // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                                game.gameMethod.getUnitFeatureStr(army,0));
                    }else {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                                game.gameMethod.getUnitSkillStr(army,0));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    if(cardNowPage==0) {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX - bW / 4, bY, bW, bH,
                                game.gameMethod.getUnitFeatureStr(air, 0));
                    }else{
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX - bW / 4, bY, bW, bH,
                                game.gameMethod.getUnitSkillStr(air, 0));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 32://技能2
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(33);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(army!=null){//army

                    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                    if(cardNowPage==0){
                        // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                                game.gameMethod.getUnitFeatureStr(army,1));
                    }else {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                                game.gameMethod.getUnitSkillStr(army,1));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    //  xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(air.getAirId()).getInt("weapon"));
                    if(cardNowPage==0) {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX - bW / 4, bY, bW, bH,
                                game.gameMethod.getUnitFeatureStr(air, 1));
                    }else{
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX - bW / 4, bY, bW, bH,
                                game.gameMethod.getUnitSkillStr(air, 1));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 33://技能3
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(34);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(army!=null){//army

                    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                    if(cardNowPage==0){
                        // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                                game.gameMethod.getUnitFeatureStr(army,2));
                    }else {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                                game.gameMethod.getUnitSkillStr(army,2));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    if(cardNowPage==0) {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX - bW / 4, bY, bW, bH,
                                game.gameMethod.getUnitFeatureStr(air, 2));
                    }else{
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX - bW / 4, bY, bW, bH,
                                game.gameMethod.getUnitSkillStr(air, 2));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 34://技能4
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                button =w.getButton(35);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(army!=null){//army
                    // xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(army.getArmyId()).getInt("weapon"));

                    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                    if(cardNowPage==0){
                        // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                                game.gameMethod.getUnitFeatureStr(army,3));
                    }else {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                                game.gameMethod.getUnitSkillStr(army,3));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(air!=null){//air
                    //  xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(air.getAirId()).getInt("weapon"));
                    if(cardNowPage==0) {
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX - bW / 4, bY, bW, bH,
                                game.gameMethod.getUnitFeatureStr(air, 3));
                    }else{
                        game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX - bW / 4, bY, bW, bH,
                                game.gameMethod.getUnitSkillStr(air, 3));
                    }
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 35:
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(1);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_35",sl.varInfantryNum+sl.varArtilleryNum+sl.varArmorNum,sl.getPopulationMax(),sl.varInfantryCost+sl.varArtilleryCost+sl.varArmorCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 36:
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(2);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_36",sl.varNavyNum+sl.varSubmarineNum,sl.getPopulationMax(),sl.varNavyCost+sl.varSubmarineCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 37:
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(3);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_37",sl.varDefenceNum,sl.getDefenceCardNum(),sl.varDefenceCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 38:
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(4);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_38",sl.varAirNum,sl.getAirCardNum(),sl.varAirCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 39:
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(5);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x+71*w.getScaleX();
                bY=tempVector2.y;
                bW=(button.getWidth()-71)*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_39",sl.varNuclearNum,sl.getNuclearCardNum(),sl.varNuclearCost));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 40://
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(15);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                int featureId=sl.getLegionFeatureByIndex(1);
                int featureLv=sl.getLegionFeatureLvByIndex(1);
                int featureEffect=sl.getLegionFeatureEffectByIndex(1);
                if(sl!=null&&featureId!=0){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrByLegionFeatureText(featureId,featureLv,featureEffect));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 41://
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(16);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                featureId=sl.getLegionFeatureByIndex(2);
                featureLv=sl.getLegionFeatureLvByIndex(2);
                featureEffect=sl.getLegionFeatureEffectByIndex(2);
                if(sl!=null&&featureId!=0){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrByLegionFeatureText(featureId,featureLv,featureEffect));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 42://
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(17);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                featureId=sl.getLegionFeatureByIndex(3);
                featureLv=sl.getLegionFeatureLvByIndex(3);
                featureEffect=sl.getLegionFeatureEffectByIndex(3);
                if(sl!=null&&featureId!=0){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrByLegionFeatureText(featureId,featureLv,featureEffect));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 43://
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(18);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                featureId=sl.getLegionFeatureByIndex(4);
                featureLv=sl.getLegionFeatureLvByIndex(4);
                featureEffect=sl.getLegionFeatureEffectByIndex(4);
                if(sl!=null&&featureId!=0){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrByLegionFeatureText(featureId,featureLv,featureEffect));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 44://
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(19);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                featureId=sl.getLegionFeatureByIndex(5);
                featureLv=sl.getLegionFeatureLvByIndex(5);
                featureEffect=sl.getLegionFeatureEffectByIndex(5);
                if(sl!=null&&featureId!=0){//army
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrByLegionFeatureText(featureId,featureLv,featureEffect));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;

            case 45://armyType
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(7);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=button.getWidth()*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){//army
                    // xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(army.getArmyId()).getInt("weapon"));

                    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_45",sl.getLevel(),sl.getStability(), sl.getCityLvMax(),sl.getIndustLvMax(),sl.getTechLvMax(),sl.getEnergyLvMax(),sl.getTransportLvMax(),sl.getMissileLvMax(),sl.getTradeLvMax(),sl.getCultureLvMax(),sl.getNuclearLvMax()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 46://编队等级{0},武器等级{1}  物资等级{0},武器等级{1},下回合恢复{2}物资
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                image =w.getImage(10);
                tempVector2=image.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=image.getWidth()*w.getScaleX()*2;
                bH=image.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if((selectListType==1||selectListType==2)&&smapGameStage.selectedArmy!=null){//编队等级{0},武器等级{1}
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_46",smapGameStage.selectedArmy.getUnitGroup(),smapGameStage.selectedArmy.getUnitWealv0Value(),smapGameStage.selectedArmy.getWealMaxLv()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(selectListType==3&&smapGameStage.selectedAir!=null){//物资等级{0},武器等级{1},下回合恢复{2}物资
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_46_1",smapGameStage.selectedAir.getAirGoodsNow(),smapGameStage.selectedAir.getAirGoodsMax(),smapGameStage.selectedAir.getWeaLv(),smapGameStage.selectedAir.getWealMaxLv(),smapGameStage.selectedAir.getAirReplyHp()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else {
                    promptText.setVisible(false);
                }
                break;
            case 47://火力等级{0},伤害提高{1}%
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                image =w.getImage(11);
                tempVector2=image.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=image.getWidth()*w.getScaleX()*2;
                bH=image.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if((selectListType==1||selectListType==2)&&smapGameStage.selectedArmy!=null){//火力等级{0},伤害提高{1}%
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_47",smapGameStage.selectedArmy.getUnitWealv1(),smapGameStage.selectedArmy.getUnitWealv1()*game.resGameConfig.addDamageForAckLv));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(selectListType==3&&smapGameStage.selectedAir!=null){//火力等级{0},伤害提高{1}%
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_47",smapGameStage.selectedAir.getAckLv(),smapGameStage.selectedAir.getAckLv()*game.resGameConfig.addDamageForAckLv));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else {
                    promptText.setVisible(false);
                }
                break;
            case 48://防空等级{0},减少来自空中的{1}%伤害
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                image =w.getImage(12);
                tempVector2=image.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=image.getWidth()*w.getScaleX()*2;
                bH=image.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if((selectListType==1||selectListType==2)&&smapGameStage.selectedArmy!=null){//防空等级{0},减少来自空中的{1}%伤害
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_48",smapGameStage.selectedArmy.getUnitWealv2(),smapGameStage.selectedArmy.getUnitWealv2()*game.resGameConfig.airDefenseForAdfLv));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(selectListType==3&&smapGameStage.selectedAir!=null){//侦查等级{0},增加额外的{0}暴击率
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_48_1",smapGameStage.selectedAir.getSpyLv(),smapGameStage.selectedAir.getSpyLv()*game.resGameConfig.addCritForAirSpyLv));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else {
                    promptText.setVisible(false);
                }
                break;

            case 49://补给等级{0},下一回合回复{1}血量与{2}士气
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                image =w.getImage(13);
                tempVector2=image.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=image.getWidth()*w.getScaleX()*2;
                bH=image.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if((selectListType==1||selectListType==2)&&smapGameStage.selectedArmy!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_49",smapGameStage.selectedArmy.getUnitWealv3(),smapGameStage.selectedArmy.getArmyReplyHp()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(selectListType==3&&smapGameStage.selectedAir!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_49",smapGameStage.selectedAir.getSupLv(),smapGameStage.selectedAir.getAirReplyHp()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else {
                    promptText.setVisible(false);
                }
                break;
            case 50://防御等级{0},防御力{1},减免受到的{0}%地面伤害
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                image =w.getImage(14);
                tempVector2=image.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=image.getWidth()*w.getScaleX()*2;
                bH=image.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if((selectListType==1||selectListType==2)&&smapGameStage.selectedArmy!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_50",smapGameStage.selectedArmy.getUnitWealv4(),smapGameStage.selectedArmy.getUnitWealv4()*game.resGameConfig.addDefForDefLv));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(selectListType==3&&smapGameStage.selectedAir!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_50",smapGameStage.selectedAir.getDefLv(),smapGameStage.selectedAir.getDefLv()*game.resGameConfig.addDefForDefLv));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else {
                    promptText.setVisible(false);
                }
                break;
            case 51://移动等级{0},增加额外的{1}移动力
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                image =w.getImage(15);
                tempVector2=image.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=image.getWidth()*w.getScaleX()*2;
                bH=image.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if((selectListType==1||selectListType==2)&&smapGameStage.selectedArmy!=null){//移动等级{0},增加额外的{1}移动力
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_51",smapGameStage.selectedArmy.getUnitWealv5(),smapGameStage.selectedArmy.getUnitWealv5()/3));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(selectListType==3&&smapGameStage.selectedAir!=null){//移动等级{0},增加额外的{1}行动范围
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_51_1",smapGameStage.selectedAir.getEngLv(),smapGameStage.selectedAir.getEngLv()/3));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else {
                    promptText.setVisible(false);
                }
                break;
            case 52://行动等级{0},战斗时,行动等级高的单位将优先结算战斗
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
                image =w.getImage(16);
                tempVector2=image.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=image.getWidth()*w.getScaleX()*2;
                bH=image.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if((selectListType==1||selectListType==2)&&smapGameStage.selectedArmy!=null){//行动等级{0},战斗时,行动等级高的单位将优先结算战斗
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_52",smapGameStage.selectedArmy.getUnitWealv6()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else if(selectListType==3&&smapGameStage.selectedAir!=null){//行动等级{0},战斗时,行动等级高的单位将优先结算战斗
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT("text_prompt_52_1",smapGameStage.selectedAir.getActLv(),smapGameStage.selectedAir.getActLv()*game.resGameConfig.airReduceDamageChanceForActLv));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else {
                    promptText.setVisible(false);
                }
                break;
            case 53://血量
                w=defaultWindow;
                button =w.getButton(3001);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                promptText.getLabel().setFontScale( 0.8f*game.gameConfig.gameFontScale*defaultWindow.getScaleY());
                if(smapGameStage.selectedArmy!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_53",smapGameStage.selectedArmy.getArmyHpNow(),smapGameStage.selectedArmy.getArmyHpMax(),smapGameStage.selectedArmy.getArmyKills(),smapGameStage.selectedArmy.getKillSum(),smapGameStage.selectedArmy.getArmyMorale()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 54://攻击
                w=defaultWindow;
                button =w.getButton(3002);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                promptText.getLabel().setFontScale( 0.8f*game.gameConfig.gameFontScale*defaultWindow.getScaleY());
                if(smapGameStage.selectedArmy!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_25",army.getMinAttack(),army.getMaxAttack()));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 55://防御
                w=defaultWindow;
                button =w.getButton(3003);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                promptText.getLabel().setFontScale( 0.8f*game.gameConfig.gameFontScale*defaultWindow.getScaleY());
                if(smapGameStage.selectedArmy!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_21",smapGameStage.selectedArmy.getArmorByDirect(),100-100*game.resGameConfig.unitArmorBaseBeardamage/(game.resGameConfig.unitArmorBaseBeardamage+smapGameStage.selectedArmy.getArmorByDirect())    ));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 56://武器等级
                w=defaultWindow;
                button =w.getButton(3004);
                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX();
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                promptText.getLabel().setFontScale( 0.75f*game.gameConfig.gameFontScale*defaultWindow.getScaleY());
                if(smapGameStage.selectedArmy!=null){
                    int  weaponId= smapGameStage.selectedArmy.armyXmlE0.getInt("weapon",100);
                    int weaLv=smapGameStage.selectedArmy.getWealMaxLv();
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,game.gameMethod.getStrValueT("text_prompt_8", "weapon_name_" + weaponId, game.gameMethod.getWeaponValue(weaponId, 1, weaLv),
                            game.gameMethod.getWeaponValue(weaponId, 2, weaLv),
                            game.gameMethod.getWeaponValue(weaponId, 3, weaLv),
                            game.gameMethod.getWeaponValue(weaponId, 4, weaLv),
                            game.gameMethod.getWeaponValue(weaponId, 5, weaLv),
                            game.gameMethod.getWeaponValue(weaponId, 6, weaLv),
                            game.gameMethod.getWeaponValue(weaponId, 8, weaLv)));
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }else{
                    promptText.setVisible(false);
                }
                break;
            case 57://军团科技介绍
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(22);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT( "text_prompt_57",sl.getMilitaryAcademyLv(),sl.getGeneralCardMax(),sl.varGeneralNum,sl.getGeneralCardNum())//军团军校等级{0}\n将军建制等级{1}
                    );
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;
            case 58://军团科技介绍
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(23);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT( "text_prompt_58",pl.getTankLvMax(),pl.getArmorCardMax())//装甲武器等级{0}\n装甲建制等级{1}
                    );
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;
            case 59://军团科技介绍
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(24);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT( "text_prompt_59",sl.getInfantryLvMax(),sl.getInfantryCardMax())//步兵武器等级{0}\n步兵建制等级{1}
                    );
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;
            case 60://军团科技介绍
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(25);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT( "text_prompt_60",sl.getCannonLvMax(),sl.getArtilleryCardMax())//火炮武器等级{0}\n火炮建制等级{1}
                    );
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;
            case 61://军团科技介绍
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(26);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT( "text_prompt_61",sl.getNavyLvMax(),sl.getNavyCardMax())//海军武器等级{0}\n海军建制等级{1}
                    );
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;
            case 62://军团科技介绍
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(27);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT( "text_prompt_62",sl.getAirLvMax(),sl.getAirCardMax(),sl.varAirNum,sl.getAirCardNum())//空军武器等级{0}\n空军建制等级{1}
                    );
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;
            case 63://军团科技介绍
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId);
                button =w.getButton(28);

                tempVector2=button.localToStageCoordinates(tempVector2);
                bX=tempVector2.x;
                bY=tempVector2.y;
                bW=(button.getWidth())*w.getScaleX()*2;
                bH=button.getHeight()*w.getScaleX();
                promptText.setScale(w.getScaleX());
                promptText.getLabel().setScale(w.getScaleX());
                if(sl!=null){
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX-bW/4,bY,bW,bH,
                            game.gameMethod.getStrValueT( "text_prompt_63",sl.getDefenceLvMax(),sl.getDefenceCardMax(),sl.varDefenceNum,sl.getDefenceCardNum())//要塞武器等级{0}\n要塞建制等级{1}
                    );
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;
            case 64://显示选择的卡牌信息
                int cardId=selectCardPotion-1;
                if(cardId>=0&&cardId<selectCardEList.size){
                    cardId=(selectCardEList.get(cardId).getInt("id",-1));
                }
                xmlE=game.gameConfig.getDEF_ARMY().getElementById(cardId);
                if(xmlE!=null){
                    w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId);
                    image =w.getImage(28);
                    tempVector2=image.localToStageCoordinates(tempVector2);
                    bX=tempVector2.x;
                    bY=tempVector2.y;
                    bW=(image.getWidth())*w.getScaleX();
                    bH=image.getHeight()*w.getScaleX();
                    promptText.setScale(w.getScaleX());
                    promptText.getLabel().setScale(w.getScaleX());
                    promptText.getLabel().setWrap(false);
                    promptText.getLabel().setFontScale( 1f*game.gameConfig.gameFontScale);
                    /**/   game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,
                            game.gameMethod.getStrValue( "unitAttribute",
                               //     game.gameMethod.formmatStringForSpace(promptText.getLabel(),"轻步兵",20,false),game.gameMethod.formmatStringForSpace(promptText.getLabel(),"步兵",20,false),game.gameMethod.formmatStringForSpace(promptText.getLabel(),"机枪",10,false),
                                    game.gameMethod.formmatStringForSpace(promptText.getLabel(),xmlE.get("strength","0"),20,false),  game.gameMethod.formmatStringForSpace(promptText.getLabel(),xmlE.get("minAtk","0")+"~"+xmlE.get("maxAtk","0"),20,false),   game.gameMethod.formmatStringForSpace(promptText.getLabel(),xmlE.get("armor","0"),10,false),
                                    game.gameMethod.formmatStringForSpace(promptText.getLabel(),xmlE.get("searchRange","1"),20,false),  game.gameMethod.formmatStringForSpace(promptText.getLabel(),xmlE.get("minAtkrange","0")+"~"+xmlE.get("maxAtkrange","0"),20,false),   game.gameMethod.formmatStringForSpace(promptText.getLabel(),xmlE.get("movement","0"),10,false),
                                    game.gameMethod.getUnitFeatureStrs(xmlE.get("feature"))
                            )//要塞武器等级{0}\n要塞建制等级{1}
                    );
                    promptText.getLabel().setWrap(true);
                    /*
                    game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText,bX,bY,bW,bH,
                            game.gameMethod.formmatStringForSpace("1005/1005",20,false)+"\n"
                            + game.gameMethod.formmatStringForSpace("1",20,false)//要塞武器等级{0}\n要塞建制等级{1}
                    );*/
                    promptText.setVisible(true);
                    promptText.getLabel().setAlignment(Align.center);
                }
                break;
            default:
                promptText.setVisible(false);
        }
    }
    private void loadScriptFile() {
        FileHandle file=  Gdx.files.internal("script/"+game.gameConfig.getDEF_STAGE().getElementById(game.getStageId()).get("name")+".xml");
        if(file.exists()){
            script=new XmlIntDAO(game.gameConfig.reader.parse(file));
           /* if(script!=null){
                game.gameMethod.clearSMapScreenScript(script,smapGameStage.getsMapDAO());
            }*/
        }
    }


    private boolean getScript() {

        if(script!=null){
            scriptXmlEs=    game.gameMethod.getSMapScreenScipt(script,smapGameStage.getsMapDAO(),scriptXmlEs);
            scriptStep=0;
            cmdStep=-1;
            return true;
        }else{
            scriptStep=-1;
            cmdStep=-1;
            return false;
        }
    }

    private void executeAllScripts(boolean ifForce){
        while (true) {
            boolean rs=executeScripts(ifForce);
            if(rs){
                break;
            }
        }
        //这个执行的其实是当条的脚本
        //smapGameStage.getsMapDAO().masterData.setGameStatu(1);
    }
private boolean ifSciptEnd(){
        if(script==null|| smapGameStage.getsMapDAO().masterData.getGameStatu()==1){
            return true;
        }
        return false;
}

    //返回 是否结束
    private boolean executeScripts(boolean ifForce) {
        if(scriptXmlEs==null||scriptXmlEs.size==0){
            return true;
        }
        if((ifForce||smapGameStage.getsMapDAO().masterData.getGameStatu()==0)&&script!=null){

            if(scriptStep!=-1){
                //  game.gameMethod.getSMapScreenScipt(script,smapGameStage.getsMapDAO(),scriptXmlEs); ;

                if(scriptXmlEs!=null&&scriptXmlEs.size>0&&scriptStep<scriptXmlEs.size){
                    cmdStep++;
                    Element scriptXmlE=  scriptXmlEs.get(scriptStep);
                    int eventId=scriptXmlE.getInt("id",0);
                /* if(!scriptXmlE.getBoolean("ifRepeat",false)&&smapGameStage.getsMapDAO().eventIDatas.contains(eventId)){
                     cmdStep=-1;
                     scriptStep++;
                     return false;
                 }*/
                    if(cmdStep>=scriptXmlE.getChildCount()){
                        if(scriptStep>=scriptXmlEs.size){
                            cmdStep=-1;
                            scriptStep=-1;
                            scriptXmlEs.clear();
                            smapGameStage.getsMapDAO().masterData.setGameStatu(1);
                            return true;
                        }else{
                            cmdStep=-1;
                            scriptStep++;
                            return false;
                        }
                    }
                    Element cmdsE=scriptXmlE.getChild(cmdStep);


                    //执行脚本
                    Array<Element> cmds= cmdsE.getChildrenByName("cmd");
                    for(Element cmd:cmds){
                        Gdx.app.log("executeCmd:",cmd.toString());
                        executeScriptCmd(cmd);
                    }
                    if(cmdsE.getBoolean("ifBreak",false)){
                        //  scriptXmlEs.clear();
                        return true;
                    }
                    if(scriptStep<scriptXmlEs.size){
                        return false;
                    }else{
                        return true;
                    }
                }else{
                    cmdStep=-1;
                    scriptStep=-1;
                    scriptXmlEs.clear();
                    smapGameStage.getsMapDAO().masterData.setGameStatu(1);
                    return true;
                }
            }else{
                cmdStep=-1;
                scriptStep=-1;
                scriptXmlEs.clear();
                smapGameStage.getsMapDAO().masterData.setGameStatu(1);
                return true;
            }
        }else{
            return true;
        }

    }

    //脚本方法不应该使用异步,因为会使得脚本方法误判
    //执行脚本
    private void executeScriptCmd(final Element cmd) {
        Fb2Smap.LegionData l;
        Fb2Smap.BuildData b;
        Fb2Smap.ArmyData ar;
        Fb2Smap.AirData ai;
        Fb2Smap.ForeignData f;
        int hexagon,cardId,tempValue,region;
        commandStr="executeScriptCmd:"+cmd.get("action","");
        switch (cmd.get("action","")){
            case "setPlayerAmbition":
                smapGameStage.getsMapDAO().setPlayerAmbition(cmd.getInt("ambition",100));
                break;
            case "resetAllBuildType":
                smapGameStage.getsMapDAO().resetAllBuildType();
                break;
            case "endAllTask":
                smapGameStage.getsMapDAO().endAllTask();
                break;
            case "refreshTrade":
                smapGameStage.getsMapDAO().refreshTrade();
                break;
            case "saveStagePreview":
                game.getGameFramework().saveStagePreview();
                break;
            case "camChange":
                smapGameStage.cam.asyncMoveCameraToHexagon(cmd.getInt("hexagon"),cmd.getInt("moveSecond"),cmd.getFloat("targetZoom"));
                //smapGameStage.selectedGameStage(cmd.getInt("hexagon"));
                break;
            case "resetDefaultState":
                smapGameStage.resetDefaultState();
                break;
            case "camMove":
                smapGameStage.cam.asyncMoveCameraToHexagon(cmd.getInt("hexagon"),cmd.getInt("moveSecond"));
                break;
            case "disableOperation":
                ifBanOperation =true;
                break;
            case "enableOperation":
                ifBanOperation =false;
                break;
            case "showDialogue":
                showGeneralDialogueGroup(cmd.getInt("generalId"),cmd.getInt("flagBg"),game.gameMethod.getStrValueT("dialogue_"+game.getStageId()+"_"+cmd.getInt("dialogueId",0)));
                break;
            case "hidDialogue":
                windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId).setVisible(false);
                break;
            case "showScriptButton":
                WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId);
                w.showButton(0);
                w.showButton(1);
                break;
            case "hidScriptButton":
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId);
                w.hidButton(0);
                w.hidButton(1);
                break;
            case "brightenScriptButton":
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId);
                w.getButton(0).setDebug(true);
                w.getButton(1).setDebug(true);
                break;
            case "restoreScriptButton":
                w=windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId);
                w.getButton(0).setDebug(false);
                w.getButton(1).setDebug(false);
                break;
            case "brightenImage":
                windowGroups.get(cmd.getInt("groupId")).getImage(cmd.getInt("targetId")).setDebug(true);
                break;
            case "restoreImage":
                windowGroups.get(cmd.getInt("groupId")).getImage(cmd.getInt("targetId")).setDebug(false);
                break;
            case "brightenButton":
                windowGroups.get(cmd.getInt("groupId")).getButton(cmd.getInt("targetId")).setDebug(true);
                break;
            case "restoreButton":
                windowGroups.get(cmd.getInt("groupId")).getButton(cmd.getInt("targetId")).setDebug(false);
                break;
            case "brightenLabel":
                windowGroups.get(cmd.getInt("groupId")).getLabel(cmd.getInt("targetId")).setDebug(true);
                break;
            case "restoreLabel":
                windowGroups.get(cmd.getInt("groupId")).getLabel(cmd.getInt("targetId")).setDebug(false);
                break;
            case "brightenTButton":
                windowGroups.get(cmd.getInt("groupId")).getTButton(cmd.getInt("targetId")).setDebug(true);
                break;
            case "restoreTButton":
                windowGroups.get(cmd.getInt("groupId")).getTButton(cmd.getInt("targetId")).setDebug(false);
                break;
            case "waitClickPromptsAndDialogues":
                if( (windowGroups.get(ResDefaultConfig.Class.SMapScreenIntroductionGroupId).isVisible()&&smapGameStage.getsMapDAO().promptDatas.size>0)      ||(windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId).isVisible()&&   smapGameStage.getsMapDAO().dialogueDatas.size>0)){
                    cmdStep--;
                }else{
                    executeScripts(false);
                }
                break;
            case "waitClickPrompts":
                if( (windowGroups.get(ResDefaultConfig.Class.SMapScreenIntroductionGroupId).isVisible()&&smapGameStage.getsMapDAO().promptDatas.size>0)     ){
                    cmdStep--;
                }else{
                    executeScripts(false);
                }
                break;
            case "waitClickDialogues":
                if( windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId).isVisible()&&   smapGameStage.getsMapDAO().dialogueDatas.size>0){
                    cmdStep--;
                }else{
                    executeScripts(false);
                }
                break;
            case "endScript"://结束
                smapGameStage.getsMapDAO().masterData.setGameStatu(1);
                cmdStep=-1;
                scriptStep=-1;
                scriptXmlEs.clear();
                break;
            case "hidUI":
                hidUi(false);
                break;
            case "selectedHexagon":
                smapGameStage.setCoord(cmd.getInt("hexagon"));
                break;
            case "clickArmy":
                smapGameStage.selectedArmyByHexagon(cmd.getInt("hexagon"));
                break;
            case "clickHexagon":
                smapGameStage.selectedGameStage(cmd.getInt("hexagon"));
                break;
            case "clickButton":
                Button button=windowGroups.get(cmd.getInt("groupId")).getButton(cmd.getInt("buttonId"));
                Boolean oldState=ifBanOperation;
                //tempInput.setListenerActor(button);
                ifBanOperation=false;
                if(button==null){
                    return;
                }
                Vector2 p=new Vector2();
                button.localToScreenCoordinates(p);


                int x= (int)(p.x+button.getWidth()/2);
                int y=(int)(p.y-button.getHeight()/2);
                //x= (int) (button.getX()+button.getWidth()/2);
                //y= (int) (uiStage.getHeight()-(button.getY()+button.getHeight()/2));


                uiStage.touchDown(x,y,1,0);
                uiStage.touchUp(x,y,1,0);

                ifBanOperation=oldState;
                //tempInput.setListenerActor(null);
                break;
            case "resetUnityState":
                ar=smapGameStage.getsMapDAO().getArmyDataByHexagon(cmd.getInt("hexagon"));
                if(ar!=null){
                    ar.setArmyRound(0);
                    ar.setIfMove(0);
                    ar.setIfAttack(0);
                    ar.setArmyHpNow(ar.getArmyHpMax());
                    ar.setArmyMoraleChange(50, game.resGameConfig.resetUnitMoraleMax);
                }
                break;
            case "resetAllState":
                smapGameStage.getsMapDAO().resetAllLegionState();
                break;
            case "addLegionTech":
                l=smapGameStage.getsMapDAO().getLegionDataByLi(cmd.getInt("legionIndex"));
                smapGameStage.getsMapDAO().legion_UpdTech(l,null,cmd.getInt("techId"));
                break;
            case "addRegionTech":
                l=smapGameStage.getsMapDAO().getLegionDataByLi(cmd.getInt("legionIndex"));
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(smapGameStage.getsMapDAO().getRegionId(cmd.getInt("regionId",-1)));
                smapGameStage.getsMapDAO().legion_UpdTech(l,b,cmd.getInt("techId"));
                break;
            case "setLegionData":
                l=smapGameStage.getsMapDAO().getLegionDataByLi(cmd.getInt("legionIndex"));
                l.setInternIndex(cmd.getInt("internindex",l.getInternIndex()));
                l.setSuzerainLi(cmd.getInt("suzerainli",l.getSuzerainLi()));
                l.setCountryId(cmd.getInt("countryid",l.getCountryId()));
                l.setTargetAreaZone(cmd.getInt("targetArea",l.getTargetAreaZone()));
                l.setMoney(cmd.getInt("money",l.getMoney()));
                l.setIndustry(cmd.getInt("industry",l.getIndustry()));
                l.setFood(cmd.getInt("food",l.getFood()));
                l.setTradeCount(cmd.getInt("tradecount",l.getTradeCount()));
                l.setTech(cmd.getInt("tech",l.getTech()));
                l.setMineral(cmd.getInt("mineral",l.getMineral()));
                l.setOil(cmd.getInt("oil",l.getOil()));
                l.setLevel(cmd.getInt("level",l.getLevel()));
                l.setMedal(cmd.getInt("medal",l.getMedal()));
                l.setLifeBonus(cmd.getInt("lifebonus",l.getLifeBonus()));
                l.setTaxBonus(cmd.getInt("taxbonus",l.getTaxBonus()));
                l.setRGeneralId(cmd.getInt("rgeneralid",l.getRGeneralId()));
                l.setCityLvMax(cmd.getInt("citylvmax",l.getCityLvMax()));
                l.setIndustLvMax(cmd.getInt("industlvmax",l.getIndustLvMax()));
                l.setTechLvMax(cmd.getInt("techlvmax",l.getTechLvMax()));
                l.setEnergyLvMax(cmd.getInt("energylvmax",l.getEnergyLvMax()));
                l.setTransportLvMax(cmd.getInt("transportlvmax",l.getTransportLvMax()));
                l.setInfantryLvMax(cmd.getInt("infantrylvmax",l.getInfantryLvMax()));
                l.setCannonLvMax(cmd.getInt("cannonlvmax",l.getCannonLvMax()));
                l.setTankLvMax(cmd.getInt("tanklvmax",l.getTankLvMax()));
                l.setFortLvMax(cmd.getInt("fortlvmax",l.getFortLvMax()));
                l.setNavyLvMax(cmd.getInt("navylvmax",l.getNavyLvMax()));
                l.setAirLvMax(cmd.getInt("airlvmax",l.getAirLvMax()));
                l.setSupplyLvMax(cmd.getInt("supplylvmax",l.getSupplyLvMax()));
                l.setDefenceLvMax(cmd.getInt("defencelvmax",l.getDefenceLvMax()));
                l.setMissileLvMax(cmd.getInt("missilelvmax",l.getMissileLvMax()));
                l.setNuclearLvMax(cmd.getInt("nuclearlvmax",l.getNuclearLvMax()));
                l.setFinancialLvMax(cmd.getInt("financiallvmax",l.getFinancialLvMax()));
                l.setTradeLvMax(cmd.getInt("tradelvmax",l.getTradeLvMax()));
                l.setCultureLvMax(cmd.getInt("culturelvmax",l.getCultureLvMax()));
                l.setMiracleNow(cmd.getInt("miraclenow",l.getMiracleNow()));
                l.setMilitaryAcademyLv(cmd.getInt("militaryAcademyLv",l.getMilitaryAcademyLv()));
                l.setInfantryCardMax(cmd.getInt("infantrycardmax",l.getInfantryCardMax()));
                l.setArmorCardMax(cmd.getInt("armorcardmax",l.getArmorCardMax()));
                l.setArtilleryCardMax(cmd.getInt("artillerycardmax",l.getArtilleryCardMax()));
                l.setNavyCardMax(cmd.getInt("navycardmax",l.getNavyCardMax()));
                l.setAirCardMax(cmd.getInt("aircardmax",l.getAirCardMax()));
                l.setNuclearCardMax(cmd.getInt("nuclearcardmax",l.getNuclearCardMax()));
                l.setMissileCardMax(cmd.getInt("missilecardmax",l.getMissileCardMax()));
                l.setSubmarineCardMax(cmd.getInt("submarinecardmax",l.getSubmarineCardMax()));
                l.setDefenceCardMax(cmd.getInt("defencecardmax",l.getDefenceCardMax()));
                l.setGeneralCardMax(cmd.getInt("generalcardmax",l.getGeneralCardMax()));
                l.setLegionRound(cmd.getInt("round",l.getLegionRound()));
                l.setCivilSpec(cmd.getInt("civilspec",l.getCivilSpec()));
                l.setRed(cmd.getInt("red",l.getRed()));
                l.setGreen(cmd.getInt("green",l.getGreen()));
                l.setBlue(cmd.getInt("blue",l.getBlue()));
                l.setAlpha(cmd.getInt("alpha",l.getAlpha()));
                l.setLegionFeature1(cmd.getInt("legionfeature1",l.getLegionFeature1()));
                l.setLegionFeatureLv1(cmd.getInt("legionfeaturelv1",l.getLegionFeatureLv1()));
                l.setLegionFeature2(cmd.getInt("legionfeature2",l.getLegionFeature2()));
                l.setLegionFeatureLv2(cmd.getInt("legionfeaturelv2",l.getLegionFeatureLv2()));
                l.setLegionFeature3(cmd.getInt("legionfeature3",l.getLegionFeature3()));
                l.setLegionFeatureLv3(cmd.getInt("legionfeaturelv3",l.getLegionFeatureLv3()));
                l.setLegionFeature4(cmd.getInt("legionfeature4",l.getLegionFeature4()));
                l.setLegionFeatureLv4(cmd.getInt("legionfeaturelv4",l.getLegionFeatureLv4()));
                l.setLegionFeature5(cmd.getInt("legionfeature5",l.getLegionFeature5()));
                l.setLegionFeatureLv5(cmd.getInt("legionfeaturelv5",l.getLegionFeatureLv5()));

                break;
            case "addLegionResource":
                l=smapGameStage.getsMapDAO().getLegionDataByLi(cmd.getInt("legionIndex"));
                l.addMoney(cmd.getInt("money",0));
                l.addIndustry(cmd.getInt("industry",0));
                l.addFood(cmd.getInt("food",0));
                l.addTech(cmd.getInt("tech",0));
                l.addStability(cmd.getInt("stability",0));
                l.addTaxBonus(cmd.getInt("taxBonus",0));
                l.addMiracle(cmd.getInt("miracle",0));
                l.addMineral(cmd.getInt("mineral",0));
                l.addOil(cmd.getInt("oil",0));
                if(l.isPlayerCommand()){
                    updResourceForPlayer();
                }
                break;
            case "setSelectedAir":
                if(smapGameStage.selectedAir!=null){
                    ai=smapGameStage.selectedAir;
                    tempValue=cmd.getInt("legionIndex",-1);
                    if(tempValue!=-1){ai.setLegionIndex(tempValue);}
                    tempValue=cmd.getInt("airId",-1);
                    if(tempValue!=-1){ai.setAirId(tempValue);}
                    tempValue=cmd.getInt("airType",-1);
                    if(tempValue!=-1){ai.setAirType(tempValue);}
                    tempValue=cmd.getInt("engLv",-1);
                    if(tempValue!=-1){ai.setEngLv(tempValue);}
                    tempValue=cmd.getInt("airKills",-1);
                    if(tempValue!=-1){ai.setAirKills(tempValue);}
                    tempValue=cmd.getInt("airHpMax",-1);
                    if(tempValue!=-1){ai.setAirHpMax(tempValue);}
                    tempValue=cmd.getInt("airHpNow",-1);
                    if(tempValue!=-1){ai.setAirHpNow(tempValue);}
                    tempValue=cmd.getInt("ackLv",-1);
                    if(tempValue!=-1){ai.setAckLv(tempValue);}


                    tempValue=cmd.getInt("defLv",-1);
                    if(tempValue!=-1){ai.setDefLv(tempValue);}
                    tempValue=cmd.getInt("spyLv",-1);
                    if(tempValue!=-1){ai.setSpyLv(tempValue);}
                    tempValue=cmd.getInt("supLv",-1);
                    if(tempValue!=-1){ai.setSupLv(tempValue);}
                    tempValue=cmd.getInt("actLv",-1);
                    if(tempValue!=-1){ai.setActLv(tempValue);}
                    tempValue=cmd.getInt("weaLv",-1);
                    if(tempValue!=-1){ai.setWeaLv(tempValue);}
                    tempValue=cmd.getInt("skillId1",-1);
                    if(tempValue!=-1){ai.setSkillId1(tempValue);}
                    tempValue=cmd.getInt("skillId2",-1);
                    if(tempValue!=-1){ai.setSkillId2(tempValue);}
                    tempValue=cmd.getInt("skillId3",-1);
                    if(tempValue!=-1){ai.setSkillId3(tempValue);}
                    tempValue=cmd.getInt("skillId4",-1);
                    if(tempValue!=-1){ai.setSkillId4(tempValue);}
                    tempValue=cmd.getInt("generalIndex",-1);
                    if(tempValue!=-1){ai.setGeneralIndex(tempValue);}
                    tempValue=cmd.getInt("airRank",-1);
                    if(tempValue!=-1){ai.setAirRank(tempValue);}
                    tempValue=cmd.getInt("airMorale",-1);
                    if(tempValue!=-1){ai.setAirMoraleValue(tempValue);}
                    tempValue=cmd.getInt("skillLv1",-1);
                    if(tempValue!=-1){ai.setSkillLv1(tempValue);}
                    tempValue=cmd.getInt("skillLv2",-1);
                    if(tempValue!=-1){ai.setSkillLv2(tempValue);}
                    tempValue=cmd.getInt("skillLv3",-1);
                    if(tempValue!=-1){ai.setSkillLv3(tempValue);}
                    tempValue=cmd.getInt("skillLv4",-1);
                    if(tempValue!=-1){ai.setSkillLv4(tempValue);}
                    tempValue=cmd.getInt("airRound",-1);
                    if(tempValue!=-1){ai.setAirRound(tempValue);}
                    tempValue=cmd.getInt("airAi",-1);
                    if(tempValue!=-1){ai.setAirAi(tempValue);}

                    tempValue=cmd.getInt("targetRegion",-1);
                    if(tempValue!=-1){ai.setTargetRegion(tempValue);}
                    tempValue=cmd.getInt("nucleIndex",-1);
                    if(tempValue!=-1){ai.setNucleIndex(tempValue);}
                    tempValue=cmd.getInt("goodsMax",-1);
                    if(tempValue!=-1){ai.setAirGoodsMax(tempValue);}
                    tempValue=cmd.getInt("goodsNow",-1);
                    if(tempValue!=-1){ai.setAirGoodsNow(tempValue);}
                    tempValue=cmd.getInt("ifMove",-1);
                    if(tempValue!=-1){ai.setIfMove(tempValue);}
                    tempValue=cmd.getInt("ifAttack",-1);
                    if(tempValue!=-1){ai.setIfAttack(tempValue);}
                    tempValue=cmd.getInt("airBuff1",-1);
                    if(tempValue!=-1){ai.setAirBuff1(tempValue);}
                    tempValue=cmd.getInt("airBuff2",-1);
                    if(tempValue!=-1){ai.setAirBuff2(tempValue);}
                    tempValue=cmd.getInt("airBuff3",-1);
                    if(tempValue!=-1){ai.setAirBuff3(tempValue);}
                    tempValue=cmd.getInt("gameValue",-1);
                    if(tempValue!=-1){ai.setGameValue(tempValue);}
                    tempValue=cmd.getInt("airEquip1",-1);
                    if(tempValue!=-1){ai.setAirEquip1(tempValue);}
                    tempValue=cmd.getInt("airEquip2",-1);
                    if(tempValue!=-1){ai.setAirEquip2(tempValue);}
                    tempValue=cmd.getInt("airEquip3",-1);
                    if(tempValue!=-1){ai.setAirEquip3(tempValue);}
                    tempValue=cmd.getInt("airEquip4",-1);
                    if(tempValue!=-1){ai.setAirEquip4(tempValue);}
                    tempValue=cmd.getInt("airSpecialType",-1);
                    if(tempValue!=-1){ai.setAirSpecialType(tempValue);}
                    tempValue=cmd.getInt("airEquip1Lv",-1);
                    if(tempValue!=-1){ai.setAirEquip1Lv(tempValue);}
                    tempValue=cmd.getInt("airEquip2Lv",-1);
                    if(tempValue!=-1){ai.setAirEquip2Lv(tempValue);}
                    tempValue=cmd.getInt("airEquip3Lv",-1);
                    if(tempValue!=-1){ai.setAirEquip3Lv(tempValue);}
                    tempValue=cmd.getInt("airEquip4Lv",-1);
                    if(tempValue!=-1){ai.setAirEquip4Lv(tempValue);}
                    tempValue=cmd.getInt("airSpecialTypeLv",-1);
                    if(tempValue!=-1){ai.setAirSpecialTypeLv(tempValue);}

                }
                break;
            case "addLegionIncome":
                l=smapGameStage.getsMapDAO().getLegionDataByLi(cmd.getInt("legionIndex"));
                l.addIncome(cmd.getInt("count",1));
                break;
            case "clearDialogues":
                smapGameStage.getsMapDAO().dialogueDatas.clear();
                break;
            case "hidPromptText":
                promptText.setVisible(false);
                break;
            case "resetRoundMax":
                smapGameStage.getsMapDAO().resetRoundMax();
                break;
            case "resetBuildState":
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(smapGameStage.getsMapDAO().getRegionId(cmd.getInt("regionId")));
                b.setBuildRound(0);
                break;
            case "resetSelectBuildState":
                b=smapGameStage.selectedBuildData;
                if(b!=null){
                    b.setBuildRound(0);
                }
                break;
            case "setBuildData":
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(smapGameStage.getsMapDAO().getRegionId(cmd.getInt("regionId")));
                tempValue=cmd.getInt("round",-1);
                if(tempValue!=-1){b.setBuildRound(tempValue);}
                tempValue=cmd.getInt("airLv",-1);
                if(tempValue!=-1){b.setAirLvNow(tempValue);}
                tempValue=cmd.getInt("armyLv",-1);
                if(tempValue!=-1){b.setArmyLvNow(tempValue);}
                tempValue=cmd.getInt("cityLv",-1);
                if(tempValue!=-1){b.setCityLvNow(tempValue);}
                tempValue=cmd.getInt("cityStability",-1);
                if(tempValue!=-1){b.setCityStability(tempValue);}
                tempValue=cmd.getInt("cityTax",-1);
                if(tempValue!=-1){b.setCityTax(tempValue);}
                tempValue=cmd.getInt("cultureLv",-1);
                if(tempValue!=-1){b.setCultureLvNow(tempValue);}
                tempValue=cmd.getInt("defenceLv",-1);
                if(tempValue!=-1){b.setDefenceLvNow(tempValue);}
                tempValue=cmd.getInt("developLv",-1);
                if(tempValue!=-1){b.setDevelopLv(tempValue);}
                tempValue=cmd.getInt("energyLv",-1);
                if(tempValue!=-1){b.setEnergyLvNow(tempValue);}
                tempValue=cmd.getInt("industryLv",-1);
                if(tempValue!=-1){b.setIndustryLvNow(tempValue);}
                tempValue=cmd.getInt("foodLv",-1);
                if(tempValue!=-1){b.setFoodLvNow(tempValue);}
                tempValue=cmd.getInt("missileLv",-1);
                if(tempValue!=-1){b.setMissileLvNow(tempValue);}
                tempValue=cmd.getInt("nuclearLv",-1);
                if(tempValue!=-1){b.setNuclearLvNow(tempValue);}
                tempValue=cmd.getInt("supplyLv",-1);
                if(tempValue!=-1){b.setSupplyLvNow(tempValue);}
                tempValue=cmd.getInt("techLv",-1);
                if(tempValue!=-1){b.setTechLvNow(tempValue);}
                tempValue=cmd.getInt("tradeLv",-1);
                if(tempValue!=-1){b.setTradeLvNow(tempValue);}
                tempValue=cmd.getInt("transportLv",-1);
                if(tempValue!=-1){b.setTransportLvNow(tempValue);}
                tempValue=cmd.getInt("weatherId",-1);
                if(tempValue!=-1){b.setWeatherId(tempValue);}
                tempValue=cmd.getInt("cityStability",-1);
                if(tempValue!=-1){b.setCityStability(tempValue);}
                break;
            case "updBuildData":
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(smapGameStage.getsMapDAO().getRegionId(cmd.getInt("regionId")));
                if(b==null){
                    return;
                }
                tempValue=cmd.getInt("airLv",-99);
                if(tempValue!=-99){b.setAirLvNow(b.getAirLvNow()+tempValue);}
                tempValue=cmd.getInt("armyLv",-99);
                if(tempValue!=-99){b.setArmyLvNow(b.getArmyLvNow()+tempValue);}
                tempValue=cmd.getInt("cityLv",-99);
                if(tempValue!=-99){b.setCityLvNow(b.getCityLvNow()+tempValue);}
                tempValue=cmd.getInt("cityStability",-99);
                if(tempValue!=-99){b.setCityStability(b.getCityStability()+tempValue);}
                tempValue=cmd.getInt("cityTax",-99);
                if(tempValue!=-99){b.setCityTax(b.getCityTax()+tempValue);}
                tempValue=cmd.getInt("cultureLv",-99);
                if(tempValue!=-99){b.setCultureLvNow(b.getCultureLvNow()+tempValue);}
                tempValue=cmd.getInt("defenceLv",-99);
                if(tempValue!=-99){b.setDefenceLvNow(b.getDefenceLvNow()+tempValue);}
                tempValue=cmd.getInt("developLv",-99);
                if(tempValue!=-99){b.setDevelopLv(b.getDevelopLv()+tempValue);}
                tempValue=cmd.getInt("energyLv",-99);
                if(tempValue!=-99){b.setEnergyLvNow(b.getEnergyLvNow()+tempValue);}
                tempValue=cmd.getInt("industryLv",-99);
                if(tempValue!=-99){b.setIndustryLvNow(b.getIndustryLvNow()+tempValue);}
                tempValue=cmd.getInt("foodLv",-99);
                if(tempValue!=-99){b.setFoodLvNow(b.getFoodLvNow()+tempValue);}
                tempValue=cmd.getInt("missileLv",-99);
                if(tempValue!=-99){b.setMissileLvNow(b.getMissileLvNow()+tempValue);}
                tempValue=cmd.getInt("nuclearLv",-99);
                if(tempValue!=-99){b.setNuclearLvNow(b.getNuclearLvNow()+tempValue);}
                tempValue=cmd.getInt("supplyLv",-99);
                if(tempValue!=-99){b.setSupplyLvNow(b.getSupplyLvNow()+tempValue);}
                tempValue=cmd.getInt("techLv",-99);
                if(tempValue!=-99){b.setTechLvNow(b.getTechLvNow()+tempValue);}
                tempValue=cmd.getInt("tradeLv",-99);
                if(tempValue!=-99){b.setTradeLvNow(b.getTradeLvNow()+tempValue);}
                tempValue=cmd.getInt("transportLv",-99);
                if(tempValue!=-99){b.setTransportLvNow(b.getTransportLvNow()+tempValue);}
                tempValue=cmd.getInt("cityStability",-99);
                if(tempValue!=-99){b.setCityStability(b.getCityStability()+tempValue);}
                if(b.getBuildActor()!=null){
                    b.getBuildActor().update();
                }
                break;
            case "removeArmy":
                ar=smapGameStage.getsMapDAO().getArmyDataByHexagon(cmd.getInt("hexagon"));
                if(ar!=null){
                    ar.armyDeath(true);
                }
                break;
            case "exitGame":
                game.showGameScreen(screenId,1);
                break;
            case "saveAndExit":
                saveAndExit();
                break;
            case "setFavor":
                f=smapGameStage.getsMapDAO().getForeignData(cmd.getInt("legionIndex1"),cmd.getInt("legionIndex2"));
                if(f!=null){
                    f.setFavorValue(cmd.getInt("favor",50));
                    int value=cmd.getInt("foreignType",-2);
                    if(value!=-2){
                        f.setForeignType(value);
                        value=cmd.getInt("foreignValue",-1);
                        if(value!=-1){
                            f.setForeignValue(value);
                        }
                    }
                }
                break;
            case "createAir":
                region=smapGameStage.getsMapDAO().getRegionId(cmd.getInt("region"));
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(region);
                if(b!=null&&b.getAirCount()<4){
                    cardId=cmd.getInt("cardId");
                    smapGameStage.getsMapDAO().recruit( region,cardId,-1);
                }
                break;
            case "createNul":
                region=smapGameStage.getsMapDAO().getRegionId(cmd.getInt("region"));
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(region);
                if(b!=null&&b.getNuclearCount()<4){
                    cardId=cmd.getInt("cardId");
                    smapGameStage.getsMapDAO().recruit(region,cardId,-1);
                }
                break;
            case "resetAirState":
                region=smapGameStage.getsMapDAO().getRegionId(cmd.getInt("region"));
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(region);
                b.resetAllAirState();
                break;
            case "updAirLv":
                region=smapGameStage.getsMapDAO().getRegionId(cmd.getInt("region"));
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(region);
                b.updAllAirLv(cmd.getInt("lv",0));
                break;
            case "updUnitLv":
                ar=smapGameStage.getsMapDAO().getArmyDataByHexagon(cmd.getInt("hexagon"));
                if(ar!=null){
                    ar.randomUpdArmyWealv(cmd.getInt("lv",0));
                }
                break;
            case "resetNulState":
                region=smapGameStage.getsMapDAO().getRegionId(cmd.getInt("region"));
                b=smapGameStage.getsMapDAO().getBuildDataByRegion(region);
                b.resetAllNulState();
                break;
            case "createArmy":
                hexagon=cmd.getInt("hexagon");
                ar=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                if(ar!=null){
                    ar.armyDeath(true);
                }
                cardId=cmd.getInt("cardId");
                smapGameStage.getsMapDAO().recruit( hexagon,cardId,cmd.getInt("targetRegion",-1));
                ar=smapGameStage.getsMapDAO().getArmyDataByHexagon(hexagon);
                if(ar!=null&&cmd.getBoolean("ifMaxGroup",false)){
                    ar.setUnitGroupGroupLv(0,9,false);
                }
                ar.resetArmyHpMax();
                if(cmd.getBoolean("ifGeneral",false)){
                    ar.updForGeneral();
                }
                if(ar.ifNeedCreateActor()){
                    smapGameStage.createArmyActor(hexagon,cardId);
                }
                break;
            case "switchRightBorderForAir":
                selectListType=3;
                smapGameStage.resetDefaultState();
                updRegionBorderGroup(selectListType,cmd.getInt("page",1));
                break;
            case "switchRightBorderForNul":
                selectListType=4;
                smapGameStage.resetDefaultState();
                updRegionBorderGroup(selectListType,cmd.getInt("page",1));
                break;
            case "switchRightBorderForArmy":
                selectListType=1;
                smapGameStage.resetDefaultState();
                updRegionBorderGroup(selectListType,cmd.getInt("page",1));
                break;
            case "switchRightBorderForNavy":
                selectListType=2;
                smapGameStage.resetDefaultState();
                updRegionBorderGroup(selectListType,cmd.getInt("page",1));
                break;
            case "clickRightBorderCard":
                clickRightBorderCard(cmd.getInt("index",1));
                break;

            case "addSelectPromptData":
                smapGameStage.getsMapDAO().addSelectPromptDataByScript(cmd);
                break;
            case "changePlayerCountry":
                l=smapGameStage.getsMapDAO().getLegionByCountry(cmd.getInt("countryId",-1),cmd.getBoolean("mustHaveRegion",true));
                if(l!=null){
                    smapGameStage.setPlayerLegionIndex(l.getLegionIndex());
                }
                break;
            case "legion_mobilization":
                l=smapGameStage.getsMapDAO().getLegionDataByLi(cmd.getInt("li",-1));
                if(l!=null){
                    smapGameStage.getsMapDAO().legion_mobilization(l.getLegionIndex(),cmd.getInt("targetLi",-1),cmd.getInt("mobilizationType",-3),cmd.getBoolean("ifForce",false));
                }
                break;

            case "changePlayerLegion":
                l=smapGameStage.getsMapDAO().getLegionDataByLi(cmd.getInt("legionIndex",-1));
                if(l!=null){
                    smapGameStage.setPlayerLegionIndex(l.getLegionIndex());
                }
                break;
            case "openPromptGroup":
                showGeneralDialogueGroupAndPromptGroup();
                break;
            case   "executeHBuff":
                smapGameStage.getsMapDAO().takeHBuffTIA1(cmd.get("country","-1"),cmd.getBoolean("ifPlayer",false),cmd.getInt("eventId",0) ,cmd.getInt("function", 0), cmd.getInt("count", 0), cmd.getInt("chance", 0), cmd.get("value", "0"),true);
                break;
            case "createCountry"://int countryId,int internIndex,int capitalId,int tax,int res,int generalNum,String updRegionStr
                smapGameStage.getsMapDAO().createCountry(cmd.getInt("country",0), cmd.getInt("internIndex",0),cmd.getInt("suzerainCountry",0),cmd.getInt("capitalId",0),cmd.getInt("tax",100),cmd.getInt("res",0),cmd.getInt("generalNum",0), cmd.getInt("eventId",0), cmd.getInt("updRegionType",0),cmd.get("updRegionStr","0"),cmd.getBoolean("ifChangeAll",true));
                break;
            case "setPlayerStar":
                resetPlayerStar();
                break;
            case   "replaceFlagAndColor": //int li,int country, int r, int g, int b, int a
                smapGameStage.getsMapDAO().replaceFlagAndColor(cmd.getInt("sourceCountry",-1),cmd.getInt("targetCountry",0),cmd.getInt("r",0) ,cmd.getInt("g", 0), cmd.getInt("b", 0), cmd.getInt("a", 0),cmd.getBoolean("mustHaveRegion",true));
                break;

            case "countryDeclareWar"://国家宣战
                smapGameStage.getsMapDAO().countryDeclareWar(cmd.getInt("sourceCountry",-1),cmd.getInt("targetCountry",-1),cmd.getBoolean("ifRelevancy",true));
                break;
            case "counrysPeace"://多个国家间和平
                if(smapGameStage.getsMapDAO().countrysPeace(cmd.get("countrys","-1"),cmd.getInt("masterCountry",-1),cmd.getInt("enemyCountry",-1),cmd.getInt("round",10),cmd.getBoolean("ifChangeArea",false))){
                    smapGameStage.getsMapDAO().takeHEvent(cmd.getInt("eventId",0),cmd.getInt("chance",50), cmd.getBoolean("ifForce",false));
                }
                break;
            case "triggerEvent"://想要条约事件,需要把chance改成101
                smapGameStage.getsMapDAO().takeHEvent(cmd.getInt("eventId",0),cmd.getInt("chance",50), cmd.getBoolean("ifForce",false));
                break;
            case "triggerEventForCountry"://想要条约事件,需要把chance改成101
                String countryStr=cmd.get("countrys","-1");
                if(countryStr.equals("-1")||ComUtil.ifHaveValueInStr(countryStr,smapGameStage.getPlayerLegion().getCountryId())){
                    smapGameStage.getsMapDAO().takeHEvent(cmd.getInt("eventId",0),cmd.getInt("chance",50), cmd.getBoolean("ifForce",false));
                }
                break;


            case "synchronouByHistory":
                smapGameStage.getsMapDAO().synchronouByHistory();
                break;

            case "setLegionTargetArea":
                smapGameStage.getsMapDAO().setLegionTargetAreaByCountrys(cmd.get("countryId","-1"),cmd.getInt("targetArea",0));
                break;
            case "initForFreeStarcraftMode":
                smapGameStage.getsMapDAO().initForFreeStarcraftMode();
                break;
            case "setAllLegionNeutral":
                smapGameStage.getsMapDAO().setAllLegionNeutral();
                break;
            case "setActLegion":
                smapGameStage.getsMapDAO().setActLegion(cmd.get("countryId","-1"),cmd.getInt("internCountry",0));
                break;
            case "addRoundDataForJoinBattle"://n回合后参战
                String pc=cmd.get("playerCountry","-1");
                if(pc.equals("-1")||ComUtil.ifHaveValueInStr(pc,smapGameStage.getPlayerLegion().getCountryId())){
                    smapGameStage.getsMapDAO().addRoundDataForJoinBattle(cmd.getInt("sourceCountry",-1),cmd.getInt("internCountry",-1),cmd.getInt("triggerYear",-1));
                }
                break;
            case "setPlayerVictory":
                pc=cmd.get("playerCountry","-1");
                if(pc.equals("-1")||ComUtil.ifHaveValueInStr(pc,smapGameStage.getPlayerLegion().getCountryId())){
                    smapGameStage.getsMapDAO().setPlayerVictory(cmd.getInt("victory",0),cmd.getInt("target",-1));
                }
                break;
            case "setWarId":
                smapGameStage.getsMapDAO().masterData.setWarId(cmd.getInt("id",0));
                break;
            case "createUnit"://createUnit(int incomeCount,boolean ifCreateGeneralUnit
                final int incomeCount=   cmd.getInt("incomeCount",3);
                final boolean ifCreateGeneralUnit=cmd.getBoolean("ifCreateGeneralUnit",true);
                hidUi(false);
                hidCommonUi();
                smapGameStage.setIfFixed(true);
                smapGameStage.getsMapDAO().createUnit(incomeCount,ifCreateGeneralUnit);
                final boolean ifRestartPlayer= cmd.getBoolean("ifRestartPlayer",true);;
                //异步方法

                CHAsyncTask task=new CHAsyncTask("nextRoundByAsync",0) {
                    @Override
                    public void onPreExecute() {
                    }

                    @Override
                    public void onPostExecute(String result) {
                        //Gdx.app.log("onPostExecute","end");
                    }
                    @Override
                    public String doInBackground() {
                        smapGameStage.getsMapDAO().createUnit(incomeCount,ifCreateGeneralUnit);
                        if(ifRestartPlayer){
                            resetPlayerStar();
                        }
                        return null;
                    }
                };
                game.asyncManager.loadTask(task);
                break;
            case "setRegionAllLegionIndex":
                smapGameStage.getsMapDAO().setRegionAllLegionIndexByCMD(cmd.getInt("region",-1),cmd.get("checkCountrys","-1"),cmd.getInt("country",-1),cmd.getBoolean("mustHaveRegion",true),cmd.getBoolean("ifChangeAll",true));
                break;
            case "setCountrySuzerain":
                smapGameStage.getsMapDAO().setCountrySuzerain(cmd.get("checkCountrys","-1"),cmd.getInt("country",-1),cmd.getInt("suzerainCountry",-1));
                break;
            case "setSRRegionAllLegionIndex":
                smapGameStage.getsMapDAO().setSRRegionAllLegionIndexByCMD(cmd.getInt("region",-1),cmd.get("checkCountrys","-1"),cmd.getInt("country",-1),cmd.getBoolean("mustHaveRegion",true),cmd.getBoolean("ifChangeAll",true));
                break;
            case "legionExChangeAreaForCountry":
                smapGameStage.getsMapDAO().legionExChangeAreaForCountry(cmd.getInt("sourceCountry",-1),cmd.getInt("targetCountry",-1),cmd.getInt("srRegion",-1));
                break;
            case "legionExChangeAreaForLi":
                smapGameStage.getsMapDAO().legionExChangeAreaForLi(cmd.getInt("sourceLi",-1),cmd.getInt("targetLi",-1),cmd.getInt("srRegion",-1));
                break;
            case "signContractByCountry":
                smapGameStage.getsMapDAO().signContractByCountry(cmd.getInt("country1",-1),cmd.getInt("country2",-1),cmd.getInt("round",5),cmd.getBoolean("mustHaveRegion",true));
                break;
            case "signContractByLi":
                smapGameStage.getsMapDAO().signContractByLi(cmd.getInt("li1",-1),cmd.getInt("li2",-1),cmd.getInt("round",5));
                break;
            case "addSRRegionExChangePromptData"://添加领土交涉事件
                smapGameStage.getsMapDAO().addSRRegionExChangePromptDataByScript(cmd);
                break;

            case "legionSurrender"://国家投降
                smapGameStage.getsMapDAO().legion_Surrender(cmd.get("sourceLegions","-1"),cmd.getInt("targetLegion",-1),cmd.getBoolean("resetPlayer",false));
                break;
            case "countrySurrender":
                smapGameStage.getsMapDAO().legion_CountrySurrender(cmd.get("sourceCountrys","-1"),cmd.getInt("targetCountry",-1),cmd.getBoolean("resetPlayer",false));
                break;
            case "legionGetSRRegion":
                smapGameStage.getsMapDAO().legion_GetSRRegion(cmd.getInt("legionIndex",-1),cmd.getBoolean("ifCoreSRRegion",true),cmd.getBoolean("ifMajorSRRegion",true),cmd.getBoolean("ifChangeAll",true));
                break;
            case "addRecord":
               /* game.gameConfig.playerConfig.putBoolean(cmd.get("value","null"), true);
                game.gameConfig.playerConfig.flush();*/
                break;
            case "addAssistantDialogue":
                smapGameStage.getsMapDAO().addAssistantDialogueData(game.gameMethod.getStrValueT(cmd.get("str","")),cmd.getBoolean("ifHidOther",true));
                break;
        }
    }


    public void addAssistantDialogue(String str,boolean ifHidOther){
        smapGameStage.getsMapDAO().addAssistantDialogueData(game.gameMethod.getStrValueT(str),ifHidOther);
    }



    public void restarGame(){
        game.restarSmapDAO();
        game.showGameScreen(screenId,screenId);
    }

    //执行 帮助
    private void helpPrompt() {
        try {
            brightenAdvicePotion(-1);
            for(int i=adviceIndex;i<13;i++){
                if(game.gameMethod.ifNeedAdvice(i)){
                    brightenAdvicePotion(i);
                    showGeneralDialogueGroup(0,smapGameStage.getPlayerLegion().getCountryId(),game.gameMethod.getStrValueT("advice_conquest_"+i));
                    adviceIndex=i+1;
                    return;
                }
            }
            showGeneralDialogueGroup(0,smapGameStage.getPlayerLegion().getCountryId(),game.gameMethod.getStrValueT("advice_conquest_100","dialogue_16_"+ComUtil.getRandom(1,game.gameConfig.getDEF_RDIALOGUE().getElementById(16).getInt("count") )));
        } catch (Exception e) {
            if(ResDefaultConfig.ifDebug){
                e.printStackTrace();
            }else {
                brightenAdvicePotion(-1);
            }
            game.recordLog("SMapScreen helpPrompt ",e);
            Gdx.app.error("helpPrompt is error","pleact see log");
        }
    }

    //advice_conquest_ + i
    private void brightenAdvicePotion(int i) {
        if(smapGameStage.getsMapDAO().roundState!=0/*||scriptStep!=-1*/){
            return;
        }
        switch (i){
            //0 政治点较低,在统帅界面设置较高的官员工资水平可以增加政治点的回合增长
            case 0:
                defaultWindow.getButton(21).setDebug(true);
                break;
            //1 区域稳定度增长较低,在统帅界面调整税率和工时制度可以改变区域稳定度增长
            case 1:
                defaultWindow.getButton(21).setDebug(true);
                break;
            //2 你的游戏路线还没有确定,红方代表强权,蓝方代表秩序.你可以通过完成右上角的任务来确定你要走的路线,不同的路线会有不同的属性加成和效果
            case 2:
                defaultWindow.getImage(2).setDebug(true);
                defaultWindow.getButton(18).setDebug(true);
                break;

            //3 你的食物产出小于消耗,建议你升级区域的城市等级或原料等级来增加食物产量,你也可以通过解散多余部队或者贸易来解决
            case 3:
                defaultWindow.getLabel(3).setDebug(true);
                defaultWindow.getButton(3).setDebug(true);
                defaultWindow.getButton(16).setDebug(true);
                break;
            //4 你有可以提升的军团科技,可通过左下角的按钮来进入科技页面升级
            case 4:
                defaultWindow.getButton(1).setDebug(true);
                break;
            //5 你有未使用的威望点,可以点击右上角的军团特性按钮进入特性页面来使用
            case 5:
                defaultWindow.getButton(15).setDebug(true);
                break;
            //6 你有未使用的外贸次数,可以进行贸易,刷新任务或选择国家进行外交
            case 6:
                defaultWindow.getButton(16).setDebug(true);
                defaultWindow.getButton(18).setDebug(true);
                defaultWindow.getButton(8).setDebug(true);
                break;
            //7 你的资源产出已经超过了存储上限,你可以通过提升军团科技的金融等级来提高存储上限
            case 7:
                defaultWindow.getImage(0).setDebug(true);
                defaultWindow.getButton(1).setDebug(true);
                break;
            //8 你有部队编制已满,你需要提高人口才能进行招募
            case 8:
                defaultWindow.getButton(1).setDebug(true);
                break;
            //9 你还有多个区域可进行操作,你也可以给其设置方针,在下一回合或快速内政时其会优先执行
            case 9:
                defaultWindow.getButton(6).setDebug(true);
                //defaultWindow.getButton(14).setDebug(true);
                break;
            //10 你还有多个单位没有设置行动目标,你可以点击右上角的快速指挥按钮,为其设定目标区域
            case 10:
                defaultWindow.getButton(19).setDebug(true);
                break;
            //11 你有可以提交的任务,完成任务可以提升你所选路线的特殊效果
            case 11:
                defaultWindow.getButton(18).setDebug(true);
                defaultWindow.getButton(20).setDebug(true);
                break;

            //12 你的矿产产量小于你的工业产量,这会制约你的工业值增长,你可以在区域修建挖矿场或者通过贸易来获得临时的矿物,来提升你的下回合的工业值产量
            case 12:
                defaultWindow.getLabel(1).setDebug(true);
                defaultWindow.getLabel(4).setDebug(true);
                defaultWindow.getButton(3).setDebug(true);
                defaultWindow.getButton(16).setDebug(true);
                break;

            default:
                defaultWindow.getImage(0).setDebug(false);
                defaultWindow.getImage(2).setDebug(false);
                defaultWindow.getLabel(1).setDebug(false);
                defaultWindow.getLabel(3).setDebug(false);
                defaultWindow.getLabel(4).setDebug(false);
                defaultWindow.getButton(1).setDebug(false);
                defaultWindow.getButton(3).setDebug(false);
                defaultWindow.getButton(6).setDebug(false);
                defaultWindow.getButton(8).setDebug(false);
                defaultWindow.getButton(15).setDebug(false);
                defaultWindow.getButton(16).setDebug(false);
                defaultWindow.getButton(18).setDebug(false);
                defaultWindow.getButton(19).setDebug(false);
                defaultWindow.getButton(20).setDebug(false);
                defaultWindow.getButton(21).setDebug(false);
                break;
        }
    }


    private void initWindowGroup(){
        //初始化暂停界面的小预览图
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId);
        priviewMapTexture=  w.setImageToFixSize(1, priviewMapPixmap,false);
        w.setImageButtonToFixImage(4,1, 0,0);
        w.setImageToFixImage(2,1, ResDefaultConfig.Image.BORDER_IMAGE_REFW, ResDefaultConfig.Image.BORDER_IMAGE_REFH);
        w=windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
        //为阵型设置拖动功能
        game.gameLayout.initArmyGroup(w);
        //设置横幅长度
        w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGameResultGroupId);
        w.getImage(0).setWidth(uiStage.getWidth());

        //w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId);


    }


    private void switchMapColor() {
        drawViewMap(smapGameStage.getSmapBg().switchMapColorShow());
    }


    //ifForceNext 是否强行开始下一回合
    public void next(Boolean ifForceNext) {
        if(smapGameStage.getsMapDAO().roundState!=0){
            return;
        }
        ifSave=false;
        //如果可以自动绘制region的build
        if(!ifForceNext&&smapGameStage.getsMapDAO().ifCanAutoPlayerRegionBuild()&&smapGameStage.getsMapDAO().masterData.getPlayerMode()!=0){
            //String title,String content,String effect,int type,int li,int li2,int value,int value2
            smapGameStage.getsMapDAO().createGeneralDiplomaticBehaviors(14);
        }else{
            hidUi(false);
            hidCommonUi();
            //错误记录
            try {
                nextRoundReady();
            } catch (Exception e) {
                if(ResDefaultConfig.ifDebug){
                    e.printStackTrace();
                }else if(!game.gameConfig.ifIgnoreBug){
                    game.remindBugFeedBack();
                }
                game.recordLog("SMapScreen next ",e);
                showLegionTechUI(smapGameStage.getPlayerLegion());
                showSaveUI();
            }
        }
    }


    public void remindBugFeedBack(){
        hidUi(false);
        dialogueFunctionIndex=-1;
        game.gameLayout.updSmapDialogueGroup(windowGroups.get(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId),0,3,1);
    }


    private void nextRoundReady(){
        hidCommonUi();
        windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId).hidImage(1);
        smapGameStage.setIfFixed(true);
        smapGameStage.getsMapDAO().roundState=2;
        // Fb2Smap.HexagonData h1=smapGameStage.getsMapDAO().hexagonDatas.get(-1);
        //异步方法
       /* if(ResDefaultConfig.ifDebug){
            nextRound();
        }else{
            nextRoundByAsync();
        }*/
        nextRoundByAsync();
        // Gdx.app.log("nextRound player legion1",  smapGameStage.getPlayerLegion().getAllAttributes());
    }

    //需要提前将结果存入目标
    public void showLegionEnd(final int gameResult,boolean ifCreateNew){
        result=gameResult;
        ifBanOperation=true;
        if(gameResult<0){
            smapGameStage.getsMapDAO().masterData.setPlayerLegionIndex(0);
        }else{
            game.deleteSav(true);
        }
        if(gameResult!=-1){
            //记录玩家游戏成绩
            String stageN=game.getStageId() + "_"+smapGameStage.getsMapDAO().masterData.getGameEpisode()+"_score";
            int sc=game.gameConfig.playerConfig.getInteger(stageN,0);
            if(smapGameStage.getsMapDAO().masterData.getIfCheat()==0){
                if(gameResult>sc){
                    game.gameConfig.playerInfo.put(stageN, gameResult);
                    game.gameConfig.playerInfo.flush();
                }
                if(gameResult>0&&smapGameStage.getsMapDAO().masterData.getBtlType()==0&&game.getStageId()!=0){
                    if(smapGameStage.getsMapDAO().masterData.getBtlType()==0){ //如果是征服,记录玩家所在的区
                        game.gameMethod.recordConquestAchievement(smapGameStage.getsMapDAO().getPlayerIntercontinentalZone(),gameResult);
                    }
                    if(ifCreateNew){
                        if(smapGameStage.getsMapDAO().masterData.getNextStageId()!=0&&
                                smapGameStage.getsMapDAO().masterData.getRoundNow()>= smapGameStage.getsMapDAO().masterData.getRoundMax()){
                            smapGameStage.openNextWorldConfig();
                            smapGameStage.createNewWorld();
                        }
                    }
                }
            }
        }



        ifEnd=true;
        endDeltaSum=20;
        hidUi(false);
        smapGameStage.getsMapDAO().dialogueDatas.clear();
        RunnableAction openUI = Actions.run(new Runnable() {
            @Override
            public void run() {
                showGameResultGroup(gameResult);
                endDeltaSum=3f;
            }
        });
        if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
            showIntroductionGroup(true);
            DelayAction delay = Actions.delay(5.0F);
            SequenceAction sequenceAction = Actions.sequence(delay,openUI );
            // 执行顺序动作
            smapGameStage.addAction(sequenceAction);
        }else{
            endDeltaSum=10;
            SequenceAction sequenceAction = Actions.sequence(openUI );
            // 执行顺序动作
            smapGameStage.addAction(sequenceAction);
        }
        smapGameStage.setIfFixed(true);
        ifBanOperation=true;
    }

    public void setWeatherAnimationEffect(int weatherId){
        return;
        /*if(weatherAnimationActor!=null&&weatherId!=lastWeatherId){
            if(game.gameConfig.ifEffect&&smapGameStage.rescource.drawType==0){
                if(weatherId==2||weatherId==4){
                    Animation fireAnimation=game.getAssetManager().get("gif/weather_rain.gif",Animation.class);
                    weatherAnimationActor.setVisible(true);
                    fireAnimation.setPlayMode(Animation.PlayMode.LOOP);
                    Color c;
                    if(weatherId==2){
                        c=new Color(1,1,1,0.4f   );
                    }else{
                        c=new Color(1,1,1,0.2f   );
                    }
                    weatherAnimationActor.setAnimation(fireAnimation,game.getWorldWidth(),game.getWorldHeight(),c,true);
                }else if(weatherId==3||weatherId==7){
                    Animation fireAnimation=game.getAssetManager().get("gif/weather_snow.gif",Animation.class);
                    weatherAnimationActor.setVisible(true);
                    fireAnimation.setPlayMode(Animation.PlayMode.LOOP);
                    Color c;
                    if(weatherId==3){
                        c=new Color(1,1,1,0.4f   );
                    }else{
                        c=new Color(1,1,1,0.2f   );
                    }
                    weatherAnimationActor.setAnimation(fireAnimation,game.getWorldWidth(),game.getWorldHeight(),c,true);
                }else{
                    weatherAnimationActor.setVisible(false);
                }
            }else{
                weatherAnimationActor.setVisible(false);
            }
        }*/

    }



    private void updFb2SmapByBtlModule() {
        switch (bm.getMode()){
            case 100://主信息
                smapGameStage.getsMapDAO().transMasterDataByBtlModule(bm,smapGameStage.getsMapDAO().masterData);
                break;
            case 101://世界信息
                smapGameStage.getsMapDAO().transWorldDataByBtlModule(bm,smapGameStage.getsMapDAO().worldData);
                break;
            case 102://军团
                smapGameStage.getsMapDAO().transLegionDataByBtlModule(bm,smapGameStage.getSelectLegionData());
                break;
            case 103://军团战略
                smapGameStage.getsMapDAO().transStrategicDataByBtlModule(bm,smapGameStage.getSelectLegioStrategicnData());
                break;
            case 104://将军
                smapGameStage.getsMapDAO().transGeneralDataByBtlModule(bm,smapGameStage.getSelectGeneralData());
                break;
            case 105:
                smapGameStage.getsMapDAO().transBuildDataByBtlModule(bm,smapGameStage.getSelectBuildData());
                break;
            case 106:
                smapGameStage.getsMapDAO().transFacilityDataByBtlModule(bm,smapGameStage.selectedFacility);
                break;
            case 107:
                smapGameStage.getsMapDAO().transNulcleDataByBtlModule(bm,smapGameStage.selectedNul);
                break;
            case 108:
                smapGameStage.getsMapDAO().transAirDataByBtlModule(bm,smapGameStage.selectedAir);
                break;
            case 109:
                smapGameStage.getsMapDAO().transArmyDataByBtlModule(bm,smapGameStage.selectedArmy);
                break;
            case 110:
                smapGameStage.getsMapDAO().transFortDataByBtlModule(bm,smapGameStage.getSelectFortData());
                break;
            case 111:
                smapGameStage.getsMapDAO().transForeignDataByBtlModule(bm,smapGameStage.getSelectForeignData());
                break;
           /* case 112:
                smapGameStage.getsMapDAO().transInternDataByBtlModule(bm,smapGameStage.getSelectBuildData());
                break;
            case 113:
                smapGameStage.getsMapDAO().transBuildDataByBtlModule(bm,smapGameStage.getSelectBuildData());
                break;
            case 114:
                smapGameStage.getsMapDAO().transBuildDataByBtlModule(bm,smapGameStage.getSelectBuildData());
                break;
            case 115:
                smapGameStage.getsMapDAO().transBuildDataByBtlModule(bm,smapGameStage.getSelectBuildData());
                break;*/
        }
    }

    //更新游戏提示
    public void updButtonPrompt(){
        if(smapGameStage.getPlayerLegionIndex()==0||smapGameStage.isEditMode(false)){
            return;
        }
        //冷却回合为0 borderButtonP_card 显示提示
        if(defaultWindow.getButton(1).isVisible()){
            if(smapGameStage.getPlayerLegionIndex()!=0&&smapGameStage.getPlayerLegion().getLegionRound()==0&&smapGameStage.getPlayerLegion().getMoney()>0){
                defaultWindow.setButtonImageNotChangeSize(1,game.getImgLists().getTextureByName("borderButtonP_card").getTextureRegion());
            }else {
                defaultWindow.setButtonImageNotChangeSize(1,game.getImgLists().getTextureByName("borderButton_card").getTextureRegion());
            }
        }
        //外贸次数大于0 统帅,贸易 borderButtonP_searchbuild borderButtonP_trade  显示提示

        if(defaultWindow.getButton(21).isVisible()){
            if(smapGameStage.getPlayerLegionIndex()!=0&&smapGameStage.getPlayerLegion().getTradeCount()>0){
                defaultWindow.setButtonImageNotChangeSize(21,game.getImgLists().getTextureByName("borderButtonP_searchbuild").getTextureRegion());
            }else {
                defaultWindow.setButtonImageNotChangeSize(21,game.getImgLists().getTextureByName("borderButton_searchbuild").getTextureRegion());
            }
        }
        if(defaultWindow.getButton(16).isVisible()){
            if(smapGameStage.getPlayerLegionIndex()!=0&&smapGameStage.getPlayerLegion().getTradeCount()>0){
                defaultWindow.setButtonImageNotChangeSize(16,game.getImgLists().getTextureByName("borderButtonP_trade").getTextureRegion());
            }else {
                defaultWindow.setButtonImageNotChangeSize(16,game.getImgLists().getTextureByName("borderButton_trade").getTextureRegion());
            }
        }
        //有可提交任务 任务栏 borderButtonP_task 显示提示
        if(defaultWindow.getButton(18).isVisible()){
            if(smapGameStage.getPlayerLegionIndex()!=0&&(smapGameStage.getsMapDAO().ifPlayerHaveCanSubmitTask()||(smapGameStage.getsMapDAO().chiefData!=null&&smapGameStage.getsMapDAO().masterData.getIfChief()==1&&smapGameStage.getsMapDAO().chiefData.getPlayerAmbition()==100) )){
                defaultWindow.setButtonImageNotChangeSize(18,game.getImgLists().getTextureByName("borderButtonP_task").getTextureRegion());
            }else {
                defaultWindow.setButtonImageNotChangeSize(18,game.getImgLists().getTextureByName("borderButton_task").getTextureRegion());
            }
        }
        //威望点>0 且有可以升级的决议或者军团特性 borderButtonP_legionFeature 显示提示
        if(defaultWindow.getButton(15).isVisible()){
            if(smapGameStage.getPlayerLegionIndex()!=0&&(smapGameStage.getsMapDAO().getPlayerLegionData().getCanBuyLegionFeaturePotion()!=0||smapGameStage.getsMapDAO().ifPlayerHaveCanSubmitResolution())){
                defaultWindow.setButtonImageNotChangeSize(15,game.getImgLists().getTextureByName("borderButtonP_legionFeature").getTextureRegion());
            }else {
                defaultWindow.setButtonImageNotChangeSize(15,game.getImgLists().getTextureByName("borderButton_legionFeature").getTextureRegion());
            }
        }
        //如果有多余提示 borderButtonP_prompt 显示提示
        if(defaultWindow.getButton(17).isVisible()){
            if(smapGameStage.getsMapDAO().promptDatas!=null&&smapGameStage.getsMapDAO().promptDatas.size>0){
                defaultWindow.setButtonImageNotChangeSize(17,game.getImgLists().getTextureByName("borderButtonP_prompt").getTextureRegion());
            }else {
                defaultWindow.setButtonImageNotChangeSize(17,game.getImgLists().getTextureByName("borderButton_prompt").getTextureRegion());
            }
        }

    }


    public void showPromptTextForUnitArmyWeapon(int potion) {
        WindowGroup w = windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId);
        ImageButton button = w.getButton(7);
        tempVector2.set(0,0);
        tempVector2 = button.localToStageCoordinates(tempVector2);
        float bX = tempVector2.x;
        float bY = tempVector2.y;
        float bW = button.getWidth() * w.getScaleX();
        float bH = button.getHeight() * w.getScaleX();
        promptText.setScale(w.getScaleX());
        promptText.getLabel().setScale(w.getScaleX());
        if (smapGameStage.selectedArmy != null) {//army
            // xmlE=game.gameConfig.getDEF_WEAPON().getElementById(  game.gameConfig.getDEF_ARMY().getElementById(army.getArmyId()).getInt("weapon"));
            int armyId ,weaLv;
            if(potion==-1){
                armyId=1400+smapGameStage.selectedArmy.getTransportType();
                weaLv=smapGameStage.selectedArmy.getUnitWealv0();
            }else{
               armyId= smapGameStage.selectedArmy.getUnitGroupArmyId(potion);
                weaLv  = smapGameStage.selectedArmy.getUnitGroupWealLv(potion)+smapGameStage.selectedArmy.getUnitGroupSameArmyIdCount(potion);
            }
            if (armyId == 0) {
                armyId = smapGameStage.selectedArmy.getUnitArmyId0();
                weaLv = smapGameStage.selectedArmy.getUnitWealv0Value()+smapGameStage.selectedArmy.getUnitGroupSameArmyIdCount(0);
            }
            XmlReader.Element xmlE=game.gameConfig.getDEF_ARMY().getElementById(armyId);
            Fb2Smap.LegionData al=smapGameStage.selectedArmy.getLegionData();
            weaLv=ComUtil.min(weaLv,al.getCardTechLv(xmlE.getInt("id"),xmlE.getInt("type")));
            int weaponId = xmlE.getInt("weapon", 100);


            // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
            game.gameLayout.setTButtonTextAndPotionAndAdapt(promptText, bX, bY, bW, bH, game.gameMethod.getStrValueT("text_prompt_8", "weapon_name_" + weaponId, game.gameMethod.getWeaponValue(weaponId, 1, weaLv),
                    game.gameMethod.getWeaponValue(weaponId, 2, weaLv),
                    game.gameMethod.getWeaponValue(weaponId, 3, weaLv),
                    game.gameMethod.getWeaponValue(weaponId, 4, weaLv),
                    game.gameMethod.getWeaponValue(weaponId, 5, weaLv),
                    game.gameMethod.getWeaponValue(weaponId, 6, weaLv),
                    game.gameMethod.getWeaponValue(weaponId, 8, weaLv)));
            promptText.setVisible(true);
            promptText.getLabel().setAlignment(Align.center);
        }
    }


    private void showConquestQuickResult() {
        final int gameResult=smapGameStage.getsMapDAO().getConquestQuickResult();
        if(gameResult>0){
            game.deleteSav(true);
            smapGameStage.getsMapDAO().addPromptData(game.gameMethod.getStrValueT("end_name_9",smapGameStage.getPlayerLegion().legionName),
                    game.gameMethod.getStrValueT("end_info_q_1"),
                    game.gameMethod.getStrValueT("end_effect_f"),smapGameStage.getPlayerLegion().getLegionIndex(),smapGameStage.getPlayerLegion().getLegionIndex(),false
            );
            ifEnd=true;
            endDeltaSum=20;
            hidUi(false);
            smapGameStage.getsMapDAO().dialogueDatas.clear();
            RunnableAction openUI = Actions.run(new Runnable() {
                @Override
                public void run() {
                    showGameResultGroup(gameResult);
                    endDeltaSum=3f;
                }
            });
            if(smapGameStage.getsMapDAO().masterData.getPlayerMode()==0){
                showIntroductionGroup(true);
                DelayAction delay = Actions.delay(5.0F);
                SequenceAction sequenceAction = Actions.sequence(delay,openUI );
                // 执行顺序动作
                smapGameStage.addAction(sequenceAction);
            }else{
                endDeltaSum=10;
                SequenceAction sequenceAction = Actions.sequence(openUI );
                // 执行顺序动作
                smapGameStage.addAction(sequenceAction);
            }
            smapGameStage.setIfFixed(true);
            ifBanOperation=true;
        }






    }



    private void showPromptGroup(int value) {
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPromptGroupId);
        int cardId=selectCardPotion-1;
        if(cardId>=0&&cardId<selectCardEList.size){
            cardId=(selectCardEList.get(cardId).getInt("id",-1));
        }
        switch (value){
            case 1://卡牌功能
                //-1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标  18定制战舰 19奇物  101陆军加载核弹  102空军加载核弹
                Gdx.app.log("showPromptGroup","prompt cardGroup:"+selectCardType+" cardId:"+cardId);
                switch (selectCardType){
                    case 15:
                    case -7:
                        if(cardId==6001){//15港口等设施
                            game.gameLayout.updPromptWindow(w,"1_15");
                            break;
                        }
                    case -2:
                    //这个展示招募的属性,在招募的属性中可以展示对应百科
                    case 1://1步兵
                    case 2://2炮兵
                    case 3://3坦克
                    case 4://4船只
                    case 5://5飞机
                    case 6://6要塞
                    case 7://7超武
                    case 8://8潜艇
                    case 18://18定制战舰
                        Gdx.app.log("showPromptGroup","prompt Fort");
                        setPromptText(64);
                        return;
                    case 9://9民用建筑卡
                    case 10://10军用建筑卡
                        game.gameLayout.updPromptWindow(w,"1_9");
                        break;

                    case 11://11势力科技卡
                        Gdx.app.log("showPromptGroup","prompt Technology Group");
                        game.gameLayout.updPromptWindow(w,"1_11");
                        break;
                    case 12://12外交卡
                        Gdx.app.log("showPromptGroup","prompt Diplomatic Group");
                        game.gameLayout.updPromptWindow(w,"1_12");
                        break;
                    case 13://13城市方针
                        Gdx.app.log("showPromptGroup","prompt City policy Group");
                        game.gameLayout.updPromptWindow(w,"1_13");
                        break;
                    case 14://14工事
                        Gdx.app.log("showPromptGroup","prompt Defense Group");
                        game.gameLayout.updPromptWindow(w,"1_14");
                        break;
                    case 16://16空军方针
                        Gdx.app.log("showPromptGroup","prompt Air policy Group");
                        game.gameLayout.updPromptWindow(w,"1_16");
                        break;
                    case 17://17地标
                        Gdx.app.log("showPromptGroup","prompt Landmark Group");
                        game.gameLayout.updPromptWindow(w,"1_17");
                        break;
                    case 19://19奇物
                        Gdx.app.log("showPromptGroup","prompt Buy Spirit Group");
                        game.gameLayout.updPromptWindow(w,"1_19");
                        break;
                    case 101://101陆军加载核弹
                    case 102: //102空军加载核弹
                        Gdx.app.log("showPromptGroup","prompt Air load Nul Group");
                        game.gameLayout.updPromptWindow(w,"1_101");
                        break;
                }
                break;
            case 2:
                game.gameLayout.updPromptWindowForPlayerMode(w,smapGameStage.getsMapDAO());
                break;
            case 3://军团概括
                Gdx.app.log("showPromptGroup","prompt legionInfoGroup");
                game.gameLayout.updPromptWindow(w,"3");
                break;
            case 4://区域概括
                Gdx.app.log("showPromptGroup","prompt legionInfoGroup");
                game.gameLayout.updPromptWindow(w,"4");
                break;
            case 6://奇物/成就/将领
                switch (selectTextGroupType){
                    case 0://0奇物 1荣誉 2为陆军招募将军 3为空军招募将军
                        Gdx.app.log("showPromptGroup","prompt spirptGroup");
                        game.gameLayout.updPromptWindow(w,"6_0");
                        break;
                    case 1:
                        Gdx.app.log("showPromptGroup","prompt achievementGroup");
                        game.gameLayout.updPromptWindow(w,"6_1");
                        break;
                    case 2:
                    case 3:
                        Gdx.app.log("showPromptGroup","prompt recruitGeneralGroup");
                        game.gameLayout.updPromptWindow(w,"6_2");
                        break;
                }
                break;
            case 8://贸易
                Gdx.app.log("showPromptGroup","prompt tradeGroup");
                game.gameLayout.updPromptWindow(w,"8");
                break;
            case 9://selectTextGroupType 决议/军团特性/任务/征服任务   0特性 1任务 2决议 3征服任务
                switch (selectTextGroupType){
                    case 0:
                        Gdx.app.log("showPromptGroup","prompt legionFeatureGroup");
                        game.gameLayout.updPromptWindow(w,"9_0");
                        break;
                    case 1:
                        Gdx.app.log("showPromptGroup","prompt roundTaskGroup");
                        game.gameLayout.updPromptWindow(w,"9_1");
                        break;
                    case 2:
                        Gdx.app.log("showPromptGroup","prompt resolutionGroup");
                        game.gameLayout.updPromptWindow(w,"9_2");
                        break;
                    case 3:
                        Gdx.app.log("showPromptGroup","prompt conquestTaskGroup");
                        game.gameLayout.updPromptWindow(w,"9_3");
                        break;
                }
                break;
            case 11:
                Gdx.app.log("showPromptGroup","prompt pauseGroup");
                game.gameLayout.updPromptWindow(w,"11");
                break;
            case 13://统帅
                Gdx.app.log("showPromptGroup","prompt chiefInfoGroup");
                game.gameLayout.updPromptWindow(w,"13");
                break;
            case 15://单位详情界面 selectListType;//右边栏 -1空 0战术卡 1陆军 2海军 3空军 4特殊武器
                switch (selectListType){
                  /*  case 0:
                        Gdx.app.log("showPromptGroup","prompt Tactical Group");
                        game.gameLayout.updPromptWindow(w,-1);
                        break;*/
                    case 1:
                        if(smapGameStage.selectedArmy!=null){
                            if(!smapGameStage.selectedArmy.isPlayerAlly()){
                                game.gameLayout.updPromptWindow(w,"15_0");
                            }else if(smapGameStage.selectedArmy.getArmyType()==6){
                                Gdx.app.log("showPromptGroup","prompt Defence Group");
                                game.gameLayout.updPromptWindow(w,"15_1_6");
                            }else{
                                Gdx.app.log("showPromptGroup","prompt Army Group");
                                game.gameLayout.updPromptWindow(w,"15_1");
                            }
                        }
                        break;
                    case 2:
                        Gdx.app.log("showPromptGroup","prompt Navy Group");
                        game.gameLayout.updPromptWindow(w,"15_2");
                        break;
                    case 3:
                        Gdx.app.log("showPromptGroup","prompt Air Group");
                        game.gameLayout.updPromptWindow(w,"15_3");
                        break;
                    case 4:
                        Gdx.app.log("showPromptGroup","prompt Nul Group");
                        game.gameLayout.updPromptWindow(w,"15_4");
                        break;
                }
                break;
        }
        hidUi(false);
        hidCommonUi();
        smapGameStage.setIfFixed(true);
        w.setVisibleForEffect(true);
    }


    public boolean checkOpenGroup(String checkGroup,boolean ifOnly){
        WindowGroup w;
        switch (checkGroup){
            case "openMain"://
                if(ifGroupOpen(0,ifOnly)){
                    return true;
                }
                break;
            //selectCardType -1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标  18定制战舰 19奇物
            case "openCardGroupForWonder"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenGameCardGroupId,ifOnly)&&selectCardType==17){
                    return true;
                }
                break;
            case "openCardGroupForDiplomacy"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenGameCardGroupId,ifOnly)&&selectCardType==12){
                    return true;
                }
                break;
            case "openCardGroupForLegionTech"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenGameCardGroupId,ifOnly)&&selectCardType==11){
                    return true;
                }
                break;
            case "openLegionTechGroup"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenLegionTechGroupId,ifOnly)){
                    return true;
                }
                break;
            case "openRegionInfoGroup":
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenRegionBuildGroupId,ifOnly)){
                    return true;
                }
                break;
            // selectTextGroupType 决议/军团特性/日常任务/征服任务   0特性 1任务 2决议 3征服任务
            case "openTextGroupForLegionFeature"://特性
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenTextGroupId,ifOnly)&&selectTextGroupType==0){
                    return true;
                }
                break;
            case "openTextGroupForDailyTask"://日常任务
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenTextGroupId,ifOnly)&&selectTextGroupType==1){
                    return true;
                }
                break;
            case "openTextGroupForResolution"://决议
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenTextGroupId,ifOnly)&&selectTextGroupType==2){
                    return true;
                }
                break;
            case "openTextGroupForConquestTask"://征服任务
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenTextGroupId,ifOnly)&&selectTextGroupType==3){
                    return true;
                }
                break;
            //achieveGroup 0奇物 1荣誉 2为陆军招募将军 3为空军招募将军
            case "openAchieveGroupForSpirit"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenAchievementGroupId,ifOnly)&&selectTextGroupType==0){
                    return true;
                }
                break;
            case "openAchieveGroupForAchieve"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenAchievementGroupId,ifOnly)&&selectTextGroupType==1){
                    return true;
                }
                break;
            case "openAchieveGroupForRecruitArmyGeneral"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenAchievementGroupId,ifOnly)&&selectTextGroupType==2){
                    return true;
                }
                break;
            case "openAchieveGroupForRecruitAirGeneral"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenAchievementGroupId,ifOnly)&&selectTextGroupType==3){
                    return true;
                }
                break;
            case "openAchieveGroupForRecruitGeneral"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenAchievementGroupId,ifOnly)&&(selectTextGroupType==2||selectTextGroupType==3)){
                    return true;
                }
                break;
            case "openUnitInfoGroup"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId,ifOnly)){
                    return true;
                }
                break;
            case "openPauseGroup"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenPauseGroupId,ifOnly)){
                    return true;
                }
                break;
            case "openSelectCountryGroup"://
                if(ifGroupOpen(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId,ifOnly)&&dialogueFunctionIndex==0){
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean ifHaveGroupOpen(){
        for(int i=0;i<ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId+1;i++){
            if(ifGroupOpen(i,false)){
                return true;
            }
        }
        return false;
    }

    private boolean ifGroupOpen(int groupId,boolean ifOnly) {
        if(groupId>=0&&groupId<windowGroups.size&&windowGroups.get(groupId).isVisible()){
            if(ifOnly){
                if(defaultWindow.isVisible()&&groupId!=0){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenGameCardGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenIntroductionGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenIntroductionGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenLegionTechGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenLegionTechGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenRegionBuildGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenRegionBuildGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyInfoGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenArmyInfoGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenAchievementGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenAchievementGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenGameResultGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenGameResultGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenTradeInfoGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenTradeInfoGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenTextGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenTextGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenPauseGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenOptionGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenChiefGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenChiefGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenEditGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenEditGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenArmyFormationGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenArmyFormationGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenPromptGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenPromptGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenGeneralDialogueGroupId){return false;}
                if(windowGroups.get(ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId).isVisible()&&groupId!=ResDefaultConfig.Class.SMapScreenDefaultShowGroupGroupId){return false;}
                return true;
            }else {
                return true;
            }
        }
        return false;
    }



    //更新默认按钮   展示操作按钮 分为我方,敌方 的区域收入以及类型
    public void updButtonForDefaultGroup(){

        if(smapGameStage.isEditMode(true)){
            defaultWindow.showButton(3);
            defaultWindow.showButton(4);

            defaultWindow.showButton(6);
            defaultWindow.hidButton(8);
            defaultWindow.hidButton(9);

            if(smapGameStage.getsMapDAO().masterData.getPlayerMode()!=2&&smapGameStage.selectedArmy!=null&&smapGameStage.selectedArmy.canBuildFort()){
                defaultWindow.showButton(12);
                defaultWindow.hidButton(5);
            }else{
                defaultWindow.showButton(5);
                defaultWindow.hidButton(12);
            }
        }else if(smapGameStage.selectedBuildData.getBuildType()==2||smapGameStage.selectedRLegionIndex ==0){//海
            defaultWindow.hidButton(3);
            defaultWindow.hidButton(4);
            defaultWindow.hidButton(5);
            defaultWindow.hidButton(6);
            defaultWindow.hidButton(8);
            defaultWindow.hidButton(9);
            defaultWindow.hidButton(12);
            //玩家点击自己
        }else if(smapGameStage.selectedRLegionIndex == smapGameStage.getPlayerLegionIndex()){
            defaultWindow.showButton(3);
            defaultWindow.showButton(4);

            defaultWindow.showButton(6);
            defaultWindow.hidButton(8);
            defaultWindow.hidButton(9);

            if(smapGameStage.getsMapDAO().masterData.getPlayerMode()!=2&&smapGameStage.selectedArmy!=null&&smapGameStage.selectedArmy.canBuildFort()){
                defaultWindow.showButton(12);
                defaultWindow.hidButton(5);
            }else{
                defaultWindow.showButton(5);
                defaultWindow.hidButton(12);
            }
            //玩家点击其他国家
        }else{
            defaultWindow.hidButton(3);
            defaultWindow.hidButton(4);
            defaultWindow.hidButton(5);
            defaultWindow.hidButton(6);
            if(smapGameStage.getsMapDAO().ifSystemEffective(4)){
                if(smapGameStage.getsMapDAO().roundState==0){
                    defaultWindow.showButton(8);
                    if(smapGameStage.getsMapDAO().ifSystemEffective(16)&&smapGameStage.getsMapDAO().ifAllyPlayerByLi(smapGameStage.selectedRLegionIndex)&&smapGameStage.selectedBuildData.getBuildRound()==0){
                    //对科技概况的显示标志进行更新
                        if(smapGameStage.getsMapDAO().ifHaveSpirit(6)){ //可以在盟国的区域上为其援建民用设施
                            defaultWindow.setButtonImageNotChangeSize(9,game.getImgLists().getTextureByName("borderButton_city").getTextureRegion());
                        }else if(smapGameStage.getsMapDAO().ifHaveSpirit(14)&&smapGameStage.selectedBuildData.isCapital()){//军用设施
                            defaultWindow.setButtonImageNotChangeSize(9,game.getImgLists().getTextureByName("borderButton_recruit").getTextureRegion());
                        }else {//普通
                            defaultWindow.setButtonImageNotChangeSize(9,game.getImgLists().getTextureByName("borderButton_tech").getTextureRegion());
                        }
                    }else {
                        defaultWindow.setButtonImageNotChangeSize(9,game.getImgLists().getTextureByName("borderButton_tech").getTextureRegion());
                    }
                }else{
                    defaultWindow.hidButton(8);
                    defaultWindow.hidButton(9);
                }
            }
            defaultWindow.hidButton(12);
        }
        if(smapGameStage.getPlayerLegion().getMoney()==0){
            defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai").getTextureRegion());
        }else {
            defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai2").getTextureRegion());
        }
        if(smapGameStage.getsMapDAO().roundState==0){
            showSaveUI();
        }else{
            hidSaveUI();
        }
    }


    private void hidSaveUI(){
        defaultWindow.hidButton(2);
        defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai").getTextureRegion());


        if(smapGameStage.getPlayMode()==0){//统帅模式
            defaultWindow.hidButton(21);//统帅
            defaultWindow.hidButton(15);//特性
            defaultWindow.hidButton(16);//贸易
        }
        defaultWindow.hidButton(18);//任务

        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId);
        w.hidButton(2);
        w.hidButton(3);

        if(smapGameStage.isEditMode(true)){
            defaultWindow.hidButton(14);
            defaultWindow.hidButton(18);
            defaultWindow.hidButton(24);
        }

    }

    public void showSaveUI() {
        defaultWindow.showButton(2);//下一回合
        if(smapGameStage.getPlayerLegion().getMoney()==0){
            defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai").getTextureRegion());
        }else {
            defaultWindow.setButtonImageNotChangeSize(24,game.getImgLists().getTextureByName("circleButton_ai2").getTextureRegion());
        }

        if(smapGameStage.getPlayMode()==0){//统帅模式
            defaultWindow.showButton(21);//统帅
            defaultWindow.showButton(15);//特性
            defaultWindow.showButton(16);//贸易
        }
        if(smapGameStage.getsMapDAO().ifSystemEffective(15)){//任务
            defaultWindow.showButton(18);
        }else{
            if(smapGameStage.getsMapDAO().masterData.getBtlType()==0&&smapGameStage.getsMapDAO().stageId!=0){
                defaultWindow.showButton(18);
            }else{
                defaultWindow.hidButton(18);
            }
        }
        WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenPauseGroupId);
        w.showButton(2);
        w.showButton(3);


        if(smapGameStage.isEditMode(true)){
            defaultWindow.hidButton(14);
            defaultWindow.hidButton(18);
            defaultWindow.hidButton(24);
        }
    }



}