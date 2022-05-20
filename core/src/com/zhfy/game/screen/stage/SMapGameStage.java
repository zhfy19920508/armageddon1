package com.zhfy.game.screen.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameAct;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.framework.tool.CHAsyncTask;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.frameenum.GameOperateState;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.Coord;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.screen.SMapScreen;
import com.zhfy.game.screen.actor.framework.BaseActor;
import com.zhfy.game.screen.actor.framework.BuildActor;
import com.zhfy.game.screen.actor.framework.EffectActorListDAO;
import com.zhfy.game.screen.actor.framework.FacilityActor;
import com.zhfy.game.screen.actor.framework.FortActor;
import com.zhfy.game.screen.actor.framework.ImageActor;
import com.zhfy.game.screen.actor.framework.SMapBGActor;
import com.zhfy.game.screen.actor.framework.SMapFVActor;
import com.zhfy.game.screen.actor.framework.ArmyActor;
import com.zhfy.game.screen.actor.framework.ComActor;
import com.zhfy.game.screen.stage.base.BaseStage;

import java.util.Iterator;

public class SMapGameStage extends BaseStage {


    //实现功能
    //地图对象与stage分离


    public GameAct gameAct;
    SMapBGActor smapBg;
    public SMapFVActor smapFv;
    public EffectActorListDAO effectDAO;
    ImageActor clickImageActor;
   // Fb2Smap sMapDAO;

    int screenId;

    private boolean ifLoop;// 是否循环
    private float x;//玩家起始位置
    private float y;
    private int w;
    //private float clickRefX;
    //private float clickRefY;

    public int getMapW() {
        return w;
    }


    private int h;
    private float lastX;
    private float lastY;
    private float zoom;
    private float zoomMax;
    private float zoomMin;
    private float xMin;
    private float yMin;
    private float xMax;
    private float yMax;

    private int mapW_px;
    private int mapH_px;
    private Array<BaseActor> tempActorList;
    // private HashMap playerMap;

    public boolean ifFixed;//是否固定镜头
    public ArmyActor testActor;
    private int refY;

    //点击区域坐标,需要处理
    public float click_x_px;
    public float click_y_px;
    private float deltaSum;
    public float alphaFlash;
    public float zoomChange;
    public SMapPublicRescource rescource;
    /**
     * 当前界面可见的所有图像集合
     */
    private final Array<ArmyActor> armyActorList = new Array<ArmyActor>();
    private final Array<BuildActor> buildActorList = new Array<BuildActor>();
    // private final Array<FacilityActor> facilityActorList = new Array<FacilityActor>();
    // private final Array<FortActor> fortActorList = new Array<FortActor>();
    private final Array<ComActor> arrowActorList = new Array<ComActor>();
    //private final IntIntMap armyActorIndexMap =new IntIntMap();
    /**
     * 图像对象缓存池, 因为需要频繁生成和移除, 所有使用对象池减少对象的频繁创建
     */
    //private Pool<ArmyActor> armyActorPool;

    //private IntArray updColorIds;
    public IntIntMap selectedHexagons;
    private int j;

    public Coord coord;
    public int selectedRLegionIndex;
    public int selectedHLegionIndex;
    public Fb2Smap.BuildData selectedBuildData;
    public Fb2Smap.FacilityData selectedFacilityData;
    private boolean ifClickArmy;
    public int selectedTargetArmyHexagon;
    public int selectedRegionId;
    public int selectedLastRegionId;
    public int selectedLastHexagon;
    public int selectedTargetHexagon;//选中的建造地点
    public int selectedTargetRegion;//选中的进攻地点
    public Fb2Smap.ArmyData selectedArmy;//
    public Fb2Smap.FacilityData selectedFacility;//
    public Fb2Smap.AirData selectedAir;
    public Fb2Smap.NulcleData selectedNul;
    public Fb2Smap.ConnectData selectedConnect;
    public IntArray selectedHexagonIds; //showArrow 方法过滤筛选的id
    public Fb2Smap.ArmyData copyArmy;
    public int copyRegionLi;

    public float scale;
    public GameOperateState gameOperate;
    public CamerDAO cam;

    public SMapGameStage(MainGame mainGame, Viewport viewport, int screenId) {
        super(mainGame, viewport);
        this.screenId = screenId;
        this.gameOperate = GameOperateState.noOperate;
        this.scale = mainGame.getMapScale();
        init();
    }

    private void init() {
        //  StringBuilder path = new StringBuilder(ResConfig.Path.SavFolderPath);
        //  path.append(getMainGame().defStage.get("name")).append(".btl");
       Fb2Smap sMapDAO = getMainGame().getSMapDAO();
        if(sMapDAO==null){
            return;
        }
        gameAct=new GameAct(getMainGame(),sMapDAO);
        ifLoop =gameAct.getBtl().ifLoop;
        this.x = 0;
        this.y = 0;
        this.w = gameAct.getBtl().masterData.getWidth();
        this.h = gameAct.getBtl().masterData.getHeight();
        this.zoom = 1f;
        // 实际宽高
        mapW_px = GameMap.getW_px(w, scale);
        mapH_px = GameMap.getH_px(w, h, scale);
        if(ifLoop){//如果循环,进行偏移,以方便地块连接
            mapW_px= (int) (mapW_px- ResDefaultConfig.Map.MAPLOOP_REFX* ResDefaultConfig.Map.MAP_SCALE);
        }


        //   this.zoomMax = mapW_px/getMainGame().getWorldWidth();  //ResDefaultConfig.Map.MAX_ZOOM
        //   this.zoomMax = mapH_px/getMainGame().getWorldHeight();
        //设置地图范围为可以扩大的最大的1/2

        resetMapZooMLimit();


        rescource=new SMapPublicRescource();

        this.refY = mapH_px - (mapH_px / ResDefaultConfig.Map.PT_SIDE) * ResDefaultConfig.Map.PT_SIDE;
        this.xMax = mapW_px * this.zoom - getMainGame().getWorldWidth();
        this.yMax = mapH_px * this.zoom - getMainGame().getWorldHeight();
        this.yMin = 0;
        this.xMin = 0;
        cam = new CamerDAO(getMainGame(), ((OrthographicCamera) getCamera()), w, h, getViewport().getWorldWidth(), getViewport().getWorldHeight(), getMapW_px(), getMapH_px(), this.zoomMin, this.zoomMax, refY, scale, getIfLoop(),rescource);

        {
            int c=gameAct.getBtl().getPlayerLegionData().getCapitalId();
            int x = GameMap.getHX(c, w);
            int y = GameMap.getHY(c, w);
            if(coord==null){
                coord=new Coord();
            }
            coord.initCoord(x, y, c);
            coord.setRegionId(c);
        }



        /*PerspectiveCamera p  = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        p.position.set(10f,10f,10f);
        p.lookAt(0,0,0);
        p.near = 1f;
        p.far = 300f;*/
        smapBg = new SMapBGActor(getMainGame(), cam,  screenId, sMapDAO,gameAct.getBtl().mapbin,scale, mapW_px, mapH_px, refY,coord);
        if(gameAct.getBtl().mapbin.mapImage!=null){
            this.zoomMax =ResDefaultConfig.Map.MAX_ZOOM*2;
            cam.setCamZoomMax(zoomMax);
        }
        addActor(smapBg);

        //updColorIds = new IntArray();
        if(selectedHexagons==null){
            selectedHexagons = new IntIntMap();
        }else{
            selectedHexagons.clear();
        }
        if(tempActorList==null){
            tempActorList = new Array();
        }else{
            tempActorList.clear();
        }
        updAllBuildActor();
        updAllFacilityActor();
        updAllArmyActor();
        updAllFortActor();
        for (int i = 0; i < 16; i++) {
            ComActor comActor = BaseActor.obtain(ComActor.class);
            addActor(comActor);
            arrowActorList.add(comActor);
        }

        //smapFv = new SMapFVActor(getMainGame(),this,cam, screenId,sMapDAO,scale,mapW_px,mapH_px);
        //addActor(smapFv);
        selectedHexagonIds = new IntArray();
        clickImageActor = new ImageActor(cam,getMainGame().getImgLists().getTextureByName("map_selected"), scale * ResDefaultConfig.Map.MAP_SCALE);
        clickImageActor.addChangeSizeAction(clickImageActor.getRefx(),clickImageActor.getRefy(),0.8f,1.2f,2);

        //  clickImageActor = new ImageActor(cam,getMainGame().getImgLists().getTextureByName("fog"), 1f);

        addActor(clickImageActor);
        if(getsMapDAO().masterData.getPlayerMode()==2){
            clickImageActor.setVisible(false);
        }
        cam.setZoomChange(1.5f);
        setPotionInPlayerCaptial();




        //gameAct.getBtl().updColorRegions.clear();
        addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(getMainGame().sMapScreen.ifBanOperation){return false;}
                if(ifFixed){return false;}
                if(gameOperate==GameOperateState.noOperate){
                    if (button == Input.Buttons.RIGHT) {
                        //更新当前点击位置
                        updClick(x,y);
                        //更新坐标
                        getSMapScreen().showClickInfo();
                        getMainGame().playSound(ResDefaultConfig.Sound.选择);
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //  Gdx.app.log("test",getSMapScreen().isTouching+":"+getSMapScreen().isDrag);
                if (ifFixed) {
                    return;
                }
                if(getMainGame().sMapScreen.ifBanOperation){
                    return;
                }
                if(coord!=null){
                    selectedLastHexagon=coord.id;
                }

                Gdx.app.log("smapGameStage click",x+":"+y);
                //test 测试点击是否准确 当点击以后,对所有位置进行遍历,遍历其上和下各200的位置,计算其坐标和归属,并绘制
                /*if(x<1250&&y<1250){
                    int sx=(int)x;
                    int sy=(int)y;
                    coord = GameMap.getHotCell(coord, smapBg.getImgXCoordByActX((getX() - sx) / getZoom()), smapBg.getImgYCoordByActY((getY() - sy) / getZoom()), zoom, scale, smapBg.getW(), 0, 0);
                    int sId=coord.getId();
                    for(int cx=sx-50;cx<sx+50;cx+=5){
                        for(int cy=sy-50;cy<sy+50;cy+=5){
                            coord = GameMap.getHotCell(coord, smapBg.getImgXCoordByActX((getX() - cx+10) / getZoom()), smapBg.getImgYCoordByActY((getY() - cy) / getZoom()), zoom, scale, smapBg.getW(), 0, 0);
                            smapBg.drawTextureByPixmap(coord.getId()==sId,cx,cy);
                        }
                    }
                }*/
                coord = GameMap.getHotCell(coord, smapBg.getImgXCoordByActX((getX() - x+10) / getZoom()), smapBg.getImgYCoordByActY((getY() - y) / getZoom()), zoom, scale, smapBg.getW(), 0, 0);
                coord.setRegionId(gameAct.getBtl().getRegionId(coord.getId()));
                //错误记录
                try {
                    clickGameStage(coord);
                } catch (Exception e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace();
                    }else if(!getMainGame().gameConfig.getIfIgnoreBug()){
                        getMainGame().remindBugFeedBack();
                    }
                    getMainGame().recordLog("ResGameConfig clickGameStage",e);
                    getSMapScreen().showLegionTechUI(getPlayerLegion());
                }
            }
        });
    }

    public void updClick(float x,float y){
        if(coord!=null){
            selectedLastHexagon=coord.id;
        }
        coord = GameMap.getHotCell(coord, smapBg.getImgXCoordByActX((getX() - x+10) / getZoom()), smapBg.getImgYCoordByActY((getY() - y) / getZoom()), zoom, scale, smapBg.getW(), 0, 0);
        coord.setRegionId(gameAct.getBtl().getRegionId(coord.getId()));
        //错误记录
        try {
            clickGameStage(coord);
        } catch (Exception e) {
            if(ResDefaultConfig.ifDebug){
                e.printStackTrace();
            }else if(!getMainGame().gameConfig.getIfIgnoreBug()){
                getMainGame().remindBugFeedBack();
            }
            getMainGame().recordLog("ResGameConfig clickGameStage",e);
            getSMapScreen().showLegionTechUI(getPlayerLegion());
        }
    }
    public void updClick(){
        clickGameStage(coord);
    }

    private void resetMapZooMLimit() {
        //this.zoomMax = ComUtil.min( mapW_px/getMainGame().getWorldWidth(),mapH_px/getMainGame().getWorldHeight())/2;
        this.zoomMax =ResDefaultConfig.Map.MAX_ZOOM;
        if(gameAct.getBtl().masterData.getPlayerMode()==2){
            this.zoomMin = ResDefaultConfig.Map.MIN_ZOOM_2;
        }else {
            this.zoomMin = ResDefaultConfig.Map.MIN_ZOOM;
        }
        if(cam!=null){
            cam.setCamZoomMax(zoomMax);
            cam.setCamZoomMin(zoomMin);
            if(cam.getZoom()<zoomMin){
                cam.setZoomChange(zoomMin);
            }
        }
    }

    public Fb2Smap.LegionData getSelectLegionData(){
        return   getsMapDAO().legionDatas.get(selectedRLegionIndex);
    }

    private void clickGameStage(Coord coord) {
        if(!gameAct.getBtl().ifGridIsPass(coord.id)){
            return;
        }
        smapFv.airRangeScale=0;
        rescource.clickId=coord.id;
        if(getsMapDAO().masterData.getPlayerMode()!=2&&getMainGame().sMapScreen!=null&&getMainGame().sMapScreen.defaultWindow.getButton(13).isVisible()){
            getMainGame().sMapScreen.hidUnitBackButton();
        }
        updSelectedInfo(coord);
        if(ResDefaultConfig.ifDebug&&selectedArmy!=null&&selectedArmy.armyActor!=null){
            selectedArmy.armyActor.updArmyModel();
        }
        if(getsMapDAO().masterData.getPlayerMode()!=2){
            //显示空降按钮
            if (gameAct.getBtl().getLastUnitHexagon()==-1&&selectedArmy!=null&&selectedArmy.isPlayer()&&selectedArmy.ifCanTransport()&&selectedArmy.getArmyRound()==0&&selectedBuildData!=null&&selectedBuildData.getReadyAir(3,selectedArmy.getHexagonIndex())!=null) {
                getMainGame().sMapScreen.defaultWindow.setButtonImage(13,getMainGame().getImgLists().getTextureByName("circleButton_airborne"));
            }else if(selectedArmy!=null&&selectedBuildData!=null&&selectedArmy.isPlayer()&&selectedArmy.ifCanTransport()&&selectedBuildData.isPlayer()&&gameAct.getBtl().ifSystemEffective(8)&&selectedBuildData.haveTactics(2011)&&selectedBuildData.getBuildRound()==0){
                getMainGame().sMapScreen.defaultWindow.setButtonImage(13,getMainGame().getImgLists().getTextureByName("circleButton_airborne"));
            }else if(selectedArmy!=null&&selectedArmy.canDeloyTrap()){//显示船只的布雷按钮
                getMainGame().sMapScreen.defaultWindow.setButtonImage(13,getMainGame().getImgLists().getTextureByName("circleButton_deloyTrap"));
            } else {
                getMainGame().sMapScreen.hidUnitBackButton();
            }
        }
        if (getPlayerLegionIndex() == 0&&!isEditMode(false)) {
            if (selectedRLegionIndex > 0) {
                Fb2Smap.LegionData l =getSelectLegionData();
                if (l.ifEnable()&&!getMainGame().gameConfig.isShieldCountry(l.getCountryId())&&!getMainGame().gameConfig.isInvincibleCountrys(l.getCountryId()) ) {
                    getMainGame().sMapScreen.showCountryToSelected( );
                }
            } else {
                getMainGame().sMapScreen.hiddenWindowGroup(ResDefaultConfig.Class.SMapScreenDialoguePromptGroupId);
            }
        } else {//游戏阶段   &&gameAct.getBtl().armyHDatas.get(selectedArmyActorIndex).getArmyRound()==0

            //smapFv.circleMark.isVisable=false;
            smapBg.setPotion(-1, -1);
            hidArrow();
            //特殊的,判断正常状态是否双击
            if (gameOperate == GameOperateState.noOperate) {
               /* if(getsMapDAO().masterData.getPlayerMode()!=2&&rescource.drawType<2&&selectedLastHexagon==coord.getId()){//点击同一个地块
                    getSMapScreen().showClickInfo();
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }else*/ if(getsMapDAO().masterData.getPlayerMode()==2){//军团模式
                    if(rescource.drawType<2&&selectedBuildData!=null&&selectedBuildData.canLegionAct()){
                        clickRegionForLegionAct(selectedBuildData);
                    }
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }else if(rescource.drawType==1&&selectedBuildData!=null&&selectedBuildData.regionHavePlayerCanCommderArmy()){
                    clickRegionForCanCommderArmy(selectedBuildData);
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }else if (getSelectedArmy() != null&&rescource.drawType<2) {//如果单击
                    /*if( gameOperate==GameOperateState.selectMultipleUnitToAct){
                        if(!selectedHexagonIds.contains(coord.id)){
                            selectedHexagonIds.add(coord.id);
                            selectedArmy.armyActor.setTargetState(3,0);
                        }else {
                            selectedHexagonIds.removeIndex(coord.id);
                            selectedArmy.armyActor.resetTarget();
                        }
                    }else */if(selectedLastHexagon!=selectedArmy.getHexagonIndex()||  (rescource.drawType==0 &&!ifClickArmy)){
                        clickPlayerArmy(selectedArmy, coord.id);
                    }   else if(rescource.drawType==1 &&selectedBuildData.isPlayer()){
                        gameOperate=GameOperateState.selectMultipleUnitToAct;
                        selectedHexagonIds.add(selectedArmy.getHexagonIndex());
                        if( selectedArmy.armyActor!=null){
                            selectedArmy.armyActor.setTargetState(3,0);
                        }
                    }
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                    return;
                }else {
                    selectedTargetArmyHexagon = -1;
                    getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                }
            }else if (gameOperate == GameOperateState.selectRegionToLegionAct) {//选择位置为行动目标
                if(selectedLastRegionId==coord.getRegionId()){
                    selectedTargetRegion=-1;
                    resetDefaultState();
                    getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                }else if(selectedHexagons.containsKey(coord.getRegionId())){//如果包含可行动位置
                    selectedTargetRegion=coord.getRegionId();
                    if(selectedHexagons.get(coord.getRegionId(),0)==26){
                        setAllCanActAirActor(selectedLastRegionId,selectedTargetRegion);
                    }else{
                        setAllCanActArmyActor(selectedLastRegionId,selectedTargetRegion);
                    }
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                } else{
                    selectedTargetRegion=coord.getRegionId();
                    setAllCanActArmyActorToTarget(selectedLastRegionId);
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }
            }else if(gameOperate == GameOperateState.selectMultipleUnitToLegionAct){//选择行动单位
                if(gameAct.getBtl().masterData.getPlayerMode()==2&&coord.getRegionId()==selectedTargetRegion){
                    legionAct();
                }else if(selectedArmy!=null&&selectedArmy.armyActor!=null&&selectedArmy.getLegionIndex()==getPlayerLegionIndex()){
                    if(selectedArmy.armyActor.targetState==3||selectedArmy.armyActor.targetState==4){
                        if(selectedArmy.armyActor.targetState==4){
                            selectedArmy.armyActor.setTargetState(3,0);
                        }else if(selectedArmy.armyActor.targetState==3){
                            selectedArmy.armyActor.setTargetState(4,0);
                        }
                        boolean ifClick=false;
                        for(ArmyActor a:armyActorList){
                            if(a.armyData.getArmyType()==6){  continue;}
                            if(a.targetState==3){
                                ifClick=true;
                                break;
                            }
                        }
                        if(ifClick){
                            getMainGame().sMapScreen.defaultWindow.showButton(13);
                        }else{
                            getMainGame().sMapScreen.defaultWindow.hidButton(13);
                        }
                    }
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }else{
                    restorePlayerArmyActorState();
                    getMainGame().sMapScreen.defaultWindow.hidButton(13);
                    resetDefaultState();
                    getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                }
            }else if(gameOperate == GameOperateState.selectMultipleUnitToLegionTarget){
                if(gameAct.getBtl().masterData.getPlayerMode()==2&&coord.getRegionId()==selectedTargetRegion){
                    legionSetTarget();
                }else  if(selectedArmy!=null&&selectedArmy.armyActor!=null&&selectedArmy.getLegionIndex()==getPlayerLegionIndex()){
                    if(selectedArmy.armyActor.targetState==3||selectedArmy.armyActor.targetState==4){
                        if(selectedArmy.armyActor.targetState==4){
                            selectedArmy.armyActor.setTargetState(3,0);

                        }else if(selectedArmy.armyActor.targetState==3){
                            selectedArmy.armyActor.setTargetState(4,0);
                        }
                        boolean ifClick=false;
                        for(ArmyActor a:armyActorList){
                            if(a.armyData.getArmyType()==6){  continue;}
                            if(a.targetState==3){
                                ifClick=true;
                                break;
                            }
                        }
                        if(ifClick){
                            getMainGame().sMapScreen.defaultWindow.showButton(13);
                        }else{
                            getMainGame().sMapScreen.defaultWindow.hidButton(13);
                        }
                    }
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }  else{
                    restorePlayerArmyActorState();
                    getMainGame().sMapScreen.defaultWindow.hidButton(13);
                    resetDefaultState();
                    getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                }
            }  else if(gameOperate==GameOperateState.selectMultipleUnitToAct){
                if(selectedArmy!=null){
                    if(selectedArmy.playerCanCommand()&&selectedArmy.armyActor!=null&&selectedArmy.getArmyType()!=6){
                        if(selectedHexagonIds.contains(coord.id)){
                            selectedHexagonIds.removeValue(coord.id);
                            selectedArmy.armyActor.resetTarget();
                        }else{
                            selectedHexagonIds.add(coord.id);
                            selectedArmy.armyActor.setTargetState(3,0);
                        }
                    }
                }else{
                    for(int i=0,iMax=selectedHexagonIds.size;i<iMax;i++){
                        Fb2Smap.ArmyData a=gameAct.getBtl().getArmyDataByHexagon(selectedHexagonIds.get(i));
                        if(a!=null&&a.armyActor!=null){
                            if(a.playerCanCommand()){
                                playerArmyAct(a.armyActor,selectedRegionId);
                            }else {
                                a.armyActor.resetTarget();
                            }
                        }
                    }
                    resetDefaultState();
                    getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                }
            } else if (gameOperate == GameOperateState.selectHexagonToLegionUnitAirborne) {//战略空降
                if (selectedHexagonIds.contains(coord.getRegionId())) {//
                    //playerAirborne(gameAct.getBtl().getArmyDataByHexagon(selectedLastHexagon),selectedAir, coord.id);
                    // Fb2Smap.ArmyData a = gameAct.getBtl().getArmyDataByHexagon(selectedLastHexagon);
                    //int cardId,int region0,int region1,int hexagon,boolean ifDraw
                    getMainGame().sMapScreen.useTactics(1011, selectedLastRegionId, coord.id, -1, true);
                }
                resetDefaultState();
            }else if (gameOperate == GameOperateState.selectHexagonToTactic) {

                if (selectedHexagons.containsKey(coord.id)) {//选中单位
                    switch (getMainGame().sMapScreen.selectCardId) {
                        case 1003://急行
                            if(gameAct.getBtl().masterData.getPlayerMode()==2){//集体急行
                                getMainGame().sMapScreen.useTactics(getMainGame().sMapScreen.selectCardId,selectedTargetRegion,  coord.getRegionId(), -1, true);
                                resetDefaultState();
                                getMainGame().playSound(ResDefaultConfig.Sound.成功);
                            }else{
                                if (selectedTargetRegion == -1) {
                                    if (selectedArmy == null) {
                                        resetDefaultState();
                                        return;
                                    }
                                    selectedTargetRegion = selectedArmy.getHexagonIndex();
                                    //继续选择急行的目标地块
                                    IntIntMap rs = getsMapDAO().getPotionForBorderRegionTIIM1(selectedArmy.getRegionId(), 4, selectedHexagons);
                                    rs.remove(selectedArmy.getRegionId(),0);
                                    smapFv.setDrawMark(rs,getClickId(), 0);
                                } else {//int cardId,int region0,int region1,int hexagon,boolean ifDraw
                                    Fb2Smap.ArmyData a = gameAct.getBtl().getArmyDataByHexagon(selectedLastRegionId);
                                    getMainGame().sMapScreen.useTactics(getMainGame().sMapScreen.selectCardId, a.getRegionId(), coord.id, a.getHexagonIndex(), true);
                                    resetDefaultState();
                                }
                            }
                            break;
                        case 1004://散谣 1
                            getMainGame().sMapScreen.useTactics(getMainGame().sMapScreen.selectCardId,selectedTargetRegion,  coord.id, -1, true);
                            resetDefaultState();
                            break;
                        case 1005://间谍 1
                            getMainGame().sMapScreen.useTactics(getMainGame().sMapScreen.selectCardId,selectedTargetRegion,  coord.id, -1, true);
                            resetDefaultState();
                            break;
                        case 1008://强行军2
                            getMainGame().sMapScreen.useTactics(getMainGame().sMapScreen.selectCardId,selectedTargetRegion,  -1, coord.id, true);
                            resetDefaultState();
                            break;
                        case 1009://急救2
                            getMainGame().sMapScreen.useTactics(getMainGame().sMapScreen.selectCardId,selectedTargetRegion,  -1, coord.id, true);
                            resetDefaultState();
                            break;
                        case 1011://空运
                           /* if (selectedTargetRegion == -1) {
                                if (selectedArmy == null||!selectedArmy.ifCanTransport()) {
                                    resetDefaultState();
                                    return;
                                }
                                selectedTargetRegion = selectedArmy.getHexagonIndex();
                                //继续选择急行的目标地块
                                IntIntMap rs = getsMapDAO().getPotionForBorderRegionTIIM1(selectedArmy.getRegionId(), 0, selectedHexagons);
                                smapFv.setDrawMark(rs,getClickId(), 0);
                            } else {//int cardId,int region0,int region1,int hexagon,boolean ifDraw
                                Fb2Smap.ArmyData a = gameAct.getBtl().getArmyDataByHexagon(selectedTargetRegion);
                                getMainGame().sMapScreen.useTactics(getMainGame().sMapScreen.selectCardId, a.getRegionId(), coord.id, a.getHexagonIndex(), true);
                                resetDefaultState();
                            }*/
                            break;
                        case 1012://爆破 1
                            getMainGame().sMapScreen.useTactics(getMainGame().sMapScreen.selectCardId,selectedTargetRegion,  coord.id, -1, true);
                            resetDefaultState();
                            break;
                    }
                    getMainGame().playSound(ResDefaultConfig.Sound.成功);
                } else {
                    resetDefaultState();
                    getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                }
            } else if (gameOperate == GameOperateState.selectHexagonToArmyAtk && selectedTargetArmyHexagon != -1) {
                Fb2Smap.ArmyData a = gameAct.getBtl().getArmyDataByHexagon(selectedTargetArmyHexagon);
                if (a != null&&gameAct.getBtl().ifHaveCanAtkEnemy(a.getLegionIndex(),coord.id,true)) {
                    ArmyActor attackActor = a.armyActor;
                    if (attackActor != null && attackActor.getHexagon() != coord.getId() && !gameAct.getBtl().ifAllyByHexagon(attackActor.getHexagon(), coord.getId())) {
                        playerUnitAttack(attackActor, coord.getId(),true);
                    } /*else {
                        gameAct.getBtl().army_Standby(attackActor.armyData, true);
                    }*/
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }
                resetDefaultState();
            } else if (gameOperate == GameOperateState.selectHexagonToArmyAct) {
                Fb2Smap.ArmyData armyData= gameAct.getBtl().getArmyDataByHexagon(selectedTargetArmyHexagon);
                if (armyData == null||armyData.armyActor==null) {
                    resetDefaultState();
                    return;
                }
                ArmyActor a =armyData.armyActor;
                if (a.getHexagon() == coord.id) {
                    ifClickArmy=false;
                    resetDefaultState();
                } else if (selectedHexagons.containsKey(coord.id)) {
                    a.armyData.setTargetRegion(-1);
                    a.resetTarget();
                    int v = selectedHexagons.get(coord.id, -1);
                    int targetHexagon=coord.id;
                    smapFv.clearDrawMark();//25 敌人 6蓝 13建筑 8?  18黄 7合兵
                    if( v==8){
                        int canMoveHexagon=gameAct.getBtl().getPlayerCanMoveHexagon(armyData,targetHexagon);
                        if(canMoveHexagon!=targetHexagon){
                            targetHexagon=canMoveHexagon;
                            v=8;
                        }else if(gameAct.getBtl().ifBorderHaveHiddenEnemy(targetHexagon,armyData.getLegionIndex())){
                            v=8;
                        }
                    }
                    if(v ==8){//未现形目标
                        int hexagon=   gameAct.getBtl().getBorderHiddenEnemyHexagon(a.armyData,targetHexagon);
                        //   targetHexagon=gameAct.getBtl().getPlayerCanMoveHexagon(a.armyData,targetHexagon);
                        a.moveHexagon(targetHexagon);
                        selectedHexagons.clear();
                        selectedTargetArmyHexagon = targetHexagon;
                        Fb2Smap.ArmyData tArmy=gameAct.getBtl().getArmyDataByHexagon(hexagon);
                        //被偷袭
                        if(tArmy!=null&&ComUtil.ifGet(ComUtil.max(tArmy.getArmyMorale(),30))&&tArmy.armyActor!=null&&gameAct.getBtl().ifCanAtk(tArmy.getLegionIndex(),a.armyData.getLegionIndex())&&tArmy.ifInArmyRange(a.armyData.getHexagonIndex())){
                            playerUnitAttack(tArmy.armyActor, armyData.getHexagonIndex(),false);
                            armyData.addArmyRound(1);
                            resetDefaultState();
                        }else if (a.armyData.getIfAttack()==0&&selectedHexagons.size > 0) {
                            smapFv.setDrawMark(selectedHexagons,getClickId(), 0);
                            gameOperate = GameOperateState.selectHexagonToArmyAtk;
                        } else {
                            //gameAct.getBtl().army_Standby(a.armyData, true);
                            resetDefaultState();
                        }
                    } else
                    if (v == 25) {//直接进攻
                        if (a.armyData != null&&a.armyData.getIfAttack()==0) {
                            playerUnitAttack(a, targetHexagon,true);
                        }
                        resetDefaultState();
                    } else if (v == 6 || v == 13) {//蓝/绿
                        a.moveHexagon(targetHexagon);
                        selectedTargetArmyHexagon = targetHexagon;
                        if(gameAct.getBtl().getLastUnitHexagon()==-1){
                            //gameAct.getBtl().army_Standby(a.armyData, true);
                        }else{
                            getMainGame().sMapScreen.showUnitBackButton();
                        }
                        resetDefaultState();
                    }else if (v ==10) {//快速移动
                        gameAct.getBtl().transportMoveForHexagon(a.armyData,targetHexagon);
                        selectedHexagons.clear();
                        resetDefaultState();
                    } else if (v == 18) {//黄色
                        selectedHexagons.clear();
                        a.moveHexagonAndGetTarget(selectedHexagons, targetHexagon);
                        selectedTargetArmyHexagon = targetHexagon;
                        if (a.armyData.getIfAttack()==0&&selectedHexagons.size > 0) {
                            smapFv.setDrawMark(selectedHexagons,getClickId(), 0);
                            gameOperate = GameOperateState.selectHexagonToArmyAtk;
                            a.clickModel();
                        } else {
                            //gameAct.getBtl().army_Standby(a.armyData, true);
                            resetDefaultState();
                        }
                    } else if (v == 7) {//合并
                        // a.moveHexagonAndGetTarget(selectedHexagons,coord.id);
                        // smapFv.setDrawMark(selectedHexagons,1);
                        // gameOperate=GameOperateState.selectHexagonToArmyAtk;

                        if (gameAct.getBtl().army_Merge(a.armyData, gameAct.getBtl().getArmyDataByHexagon(targetHexagon))) {
                            //armyActorClear(selectedArmy.armyActor);
                            a.moveHexagon(targetHexagon);
                            // getMainGame().sMapScreen.showArmyInfoUI(a.armyData);
                            resetDefaultState();
                            selectedTargetArmyHexagon = targetHexagon;
                            if (a.armyData.getArmyType() != 6) {
                                //setUpdAnimation(coord.id);
                                effectDAO.darwEffect(2,targetHexagon, -1,0.3f);
                                //初始教程
                                if(getMainGame().resGameConfig.ifPromptFirstOperate&&getsMapDAO().masterData.getBtlType()==0){
                                    if(!getMainGame().gameConfig.ifBRecord(ResDefaultConfig.StringName.firstMergeUnit)){
                                        gameAct.getBtl().addAssistantDialogueData(getMainGame().gameMethod.getStrValueT("prompt_dialogue_54"),false);
                                        getMainGame().gameConfig.addBRecord(ResDefaultConfig.StringName.firstMergeUnit);
                                    }
                                }
                            } else {
                                //setBuildAnimation(coord.id);
                                effectDAO.darwFaceEffect( targetHexagon, 1);

                            }
                        }
                    } else {
                        // gameAct.getBtl().army_Standby(a.armyData, true);
                        resetDefaultState();
                        getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                        Gdx.app.error("可行动目标:" + v, "没有找到对应的处理方案");
                    }
                    //判断是否有可进攻目标,有则显示,无则下一步
                    if(a.armyData.ifHaveFeature(15)||a.armyData.ifHaveFeature(14)){
                        if(a.armyData.getIfAttack()==1&&a.armyData.getIfMove()==1){
                            a.armyData.addArmyRound( 1);
                        }
                    }else  if(a.armyData.getIfAttack()==1){
                        a.armyData.addArmyRound( 1);
                    }
                    a.armyData.setTargetRegion(-1);
                    getMainGame().sMapScreen.updRegionBorderGroup(-1, 1);

                } else if (coord.id != -1) {
                    if(rescource.drawType==1){
                        playerArmyAct(a,selectedRegionId);
                    }
                    resetDefaultState();
                }
                hidArrow();
            } else if (gameOperate == GameOperateState.selectHexagonToBuild) {
                if (selectedHexagons.containsKey(coord.id)) {
                    selectedTargetHexagon = coord.id;
                    if (getMainGame().sMapScreen.selectCardType == 0) {

                    } else {
                        getMainGame().sMapScreen.buyCard();
                    }
                    smapFv.clearDrawMark();
                    selectedHexagons.clear();
                    resetDefaultState();

                    getMainGame().playSound(ResDefaultConfig.Sound.成功);
                } else {
                    getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                    resetDefaultState();
                }
            } else if (gameOperate == GameOperateState.selectRegionToAirTarget && selectedAir != null) {
                if (selectedHexagonIds.contains(coord.id)) {//
                    playerAirAct(selectedAir, coord.id);
                }else if(   selectedBuildData.isPlayer()&&selectedBuildData.getBuildType()!=2 &&selectedBuildData.getAirCount()<4){
                    int round=selectedAir.getMoveNeedRound(selectedBuildData.getRegionId());
                    if(round>0){
                        // addPromptData(game.gameMethod.getPromptStrT(35,0),game.gameMethod.getPromptStrT(35,1,l.legionName,favor,game.gameMethod.getStrValueForResource(v1,v2)),"",bType,li,li2,v1,v2);
                        //String title,String content,String effect,int type,int li,int li2,int value,int value2
                        gameAct.getBtl().addPromptData(getMainGame().gameMethod.getPromptStrT(73,0),getMainGame().gameMethod.getPromptStrT(73,1,selectedBuildData.getRegionAreaStr(),round),getMainGame().gameMethod.getPromptStrT(73,2),18,getPlayerLegion().getLegionIndex(),getPlayerLegion().getLegionIndex(),selectedAir.getAirIndex(),selectedBuildData.getRegionId(),true);
                    }
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }
                resetDefaultState();
            }else if(gameOperate == GameOperateState.selectHexagonToAirborne&& selectedAir != null){
                if (selectedHexagonIds.contains(coord.id)) {//
                    playerAirborne(gameAct.getBtl().getArmyDataByHexagon(selectedLastHexagon),selectedAir, coord.id);
                }
                resetDefaultState();
            }else if( gameOperate == GameOperateState.selectHexagonToAirborneTactical){
                if (selectedHexagonIds.contains(coord.id)) {//
                    //playerAirborne(gameAct.getBtl().getArmyDataByHexagon(selectedLastHexagon),selectedAir, coord.id);
                    Fb2Smap.ArmyData a = gameAct.getBtl().getArmyDataByHexagon(selectedLastHexagon);
                    //int cardId,int region0,int region1,int hexagon,boolean ifDraw
                    getMainGame().sMapScreen.useTactics(1011, a.getHexagonIndex(), coord.id, a.getHexagonIndex(), true);
                    getMainGame().playSound(ResDefaultConfig.Sound.成功);
                }
                resetDefaultState();
            } else if (gameOperate == GameOperateState.selectRegionToNulTarget && selectedNul != null) {
                if (selectedHexagons.containsKey(coord.id)) {//
                    playerNulAct(selectedNul, coord.id, selectedHexagons.get(coord.id, 0));
                }else if(   selectedBuildData.isPlayer()&&selectedBuildData.getBuildType()!=2 &&selectedBuildData.getNuclearCount()<4){
                    int round=selectedNul.getMoveNeedRound(selectedBuildData.getRegionId());
                    if(round>0){
                        // addPromptData(game.gameMethod.getPromptStrT(35,0),game.gameMethod.getPromptStrT(35,1,l.legionName,favor,game.gameMethod.getStrValueForResource(v1,v2)),"",bType,li,li2,v1,v2);
                        //String title,String content,String effect,int type,int li,int li2,int value,int value2
                        gameAct.getBtl().addPromptData(getMainGame().gameMethod.getPromptStrT(73,0),getMainGame().gameMethod.getPromptStrT(73,1,selectedBuildData.getRegionAreaStr(),round),getMainGame().gameMethod.getPromptStrT(73,2),19,getPlayerLegion().getLegionIndex(),getPlayerLegion().getLegionIndex(),selectedNul.getNucleIndex(),selectedBuildData.getRegionId(),true);
                    }
                    getMainGame().playSound(ResDefaultConfig.Sound.选择);
                }
                resetDefaultState();
            } else {
                getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                resetDefaultState();
            }
        }
    }

    private void setAllCanActArmyActorToTarget(int regionId) {
        int c=0;
        Fb2Smap.ArmyData firstArmy=null;
        for (ArmyActor armyActor : armyActorList) {
            armyActor.setVisible(true);
            if (armyActor.armyData.getArmyType()!=6&&armyActor.armyData.getLegionIndex() == getPlayerLegionIndex() &&armyActor.armyData.getRegionId()==regionId&&armyActor.armyData.getArmyRound()==0&&armyActor.armyData.canActRegion(regionId)) {
                armyActor.setTargetState(3,0);
                if(c==0){
                    firstArmy=armyActor.armyData;
                }
                c++;
            }
        }
        if(c==1&&firstArmy!=null){
            firstArmy.setTargetRegion(selectedTargetRegion);
            // gameAct.getBtl().army_legionActToTarget(firstArmy);
            firstArmy.armyActor.resetTarget();
            resetDefaultState();
            getMainGame().sMapScreen.hidUnitBackButton();
        }else if(c>0){
            hidArrow();
            showArrow(regionId,coord.getRegionId());
            getMainGame().sMapScreen.defaultWindow.showButton(13);
            gameOperate = GameOperateState.selectMultipleUnitToLegionTarget;
        }else{
            resetDefaultState();
            getMainGame().sMapScreen.hidUnitBackButton();
        }

    }

    //设置所有可以行动的act
    private void setAllCanActArmyActor(int regionId,int targetRegion) {
        int c=0;
        Fb2Smap.ArmyData firstArmy=null;
        for (ArmyActor armyActor : armyActorList) {
            armyActor.setVisible(true);
           /*if(armyActor.getHexagon()==12407){
                int s=0;
            }*/
            if (armyActor.armyData.getArmyType()!=6&&armyActor.armyData.getLegionIndex() == getPlayerLegionIndex() &&armyActor.armyData.getRegionId()==regionId&&armyActor.armyData.getArmyRound()==0&&armyActor.armyData.canActRegion(targetRegion)) {
                armyActor.setTargetState(3,0);
                if(c==0){
                    firstArmy=armyActor.armyData;
                }
                c++;
            }
        }
        if(c==1&&firstArmy!=null){
            legionAct();
        }else if(c>0){
            hidArrow();
            showArrow(regionId,coord.getRegionId());
            getMainGame().sMapScreen.defaultWindow.showButton(13);
            smapFv.clearDrawMark();
            gameOperate = GameOperateState.selectMultipleUnitToLegionAct;
        }else{
            resetDefaultState();
            getMainGame().sMapScreen.hidUnitBackButton();
        }
    }

    private void setAllCanActAirActor(int regionId,int targetRegion) {
        int c=0;
        Fb2Smap.ArmyData firstArmy=null;
        for (ArmyActor armyActor : armyActorList) {
            armyActor.setVisible(true);
           /* if(armyActor.getHexagon()==19423){
                int s=0;
            }*/
            if (armyActor.armyData.getArmyType()==5&&armyActor.armyData.getLegionIndex() == getPlayerLegionIndex() &&armyActor.armyData.getRegionId()==regionId&&armyActor.armyData.getArmyRound()==0
                    &&armyActor.armyData.ifInArmyRange(targetRegion)) {
                armyActor.setTargetState(3,0);
                if(c==0){
                    firstArmy=armyActor.armyData;
                }
                c++;
            }
        }
        if(c==1&&firstArmy!=null){//直接进攻
            gameAct.getBtl().legionUnit_Attack(gameAct.getBtl().getBuildDataByRegion(targetRegion),firstArmy,true);
            firstArmy.armyActor.resetTarget();
            getMainGame().sMapScreen.defaultWindow.hidButton(13);
            resetDefaultState();
        }else if(c>0){
            hidArrow();
            showArrow(regionId,coord.getRegionId());
            getMainGame().sMapScreen.defaultWindow.showButton(13);
            gameOperate = GameOperateState.selectMultipleUnitToLegionAct;
        }else{
            resetDefaultState();
            getMainGame().sMapScreen.hidUnitBackButton();
        }
    }

    private void clickRegionForCanCommderArmy(Fb2Smap.BuildData b) {
        if(gameAct.getBtl().regionHexagonMap.containsKey(b.getRegionId())){
            IntArray hs=gameAct.getBtl().regionHexagonMap.get(b.getRegionId());

            if(hs!=null) {
                boolean ifArmy = false;//全部都可以移动
                boolean ifNavy = false;//仅可以移到到海洋
                //     boolean ifAir = false;//可以移动到我方区域,敌方驻兵区域
                int maxRange = 0;
                for (int i = 0, iMax = hs.size; i < iMax; i++) {
                    Fb2Smap.ArmyData a = gameAct.getBtl().getArmyDataByHexagon(hs.get(i));
                    if (a != null && a.isPlayer() && a.getArmyRound() == 0) {
                        if (a.getArmyType() == 4 || a.getArmyType() == 8) {
                            ifNavy = true;
                        } /*else if (a.getArmyType() == 5 || a.getArmyType() == 7) {
                            ifAir = true;
                            int armyMaxR = a.getMaxRange();
                            if (armyMaxR > maxRange) {
                                maxRange = armyMaxR;
                            }
                        } */else if (a.getArmyType() != 6) {
                            ifArmy = true;
                        }
                    }
                }
                showArrow(b.getRegionId(), gameAct.getBtl().getConnectData(selectedRegionId), gameAct.getBtl().getHexagonCheckType(ifArmy,ifNavy,true), false,true,selectedHexagons);
            }

            selectedHexagonIds.clear();
            gameOperate=GameOperateState.selectMultipleUnitToAct;
            for(int i=0,iMax=hs.size;i<iMax;i++){
                Fb2Smap.ArmyData a=gameAct.getBtl().getArmyDataByHexagon(hs.get(i));
                if(a!=null&&a.playerCanCommand() && a.getArmyRound() == 0&&a.getArmyType()!=6){
                    if( a.armyActor!=null){
                        selectedHexagonIds.add(a.getHexagonIndex());
                        a.armyActor.setTargetState(3,0);
                    }
                }
            }
        }
    }

    private void clickRegionForLegionAct(Fb2Smap.BuildData b) {
        if(b==null||!b.isPlayer()){return;}
        IntArray hexagonArray = gameAct.getBtl().regionHexagonMap.get(coord.getRegionId());
        if(hexagonArray!=null){
            boolean ifArmy=false;//全部都可以移动
            boolean ifNavy=false;//仅可以移到到海洋
            boolean ifAir=false;//可以移动到我方区域,敌方驻兵区域
            int maxRange=0;
            for (int i = 0, iMax = hexagonArray.size; i < iMax; i++) {
                Fb2Smap.ArmyData a=gameAct.getBtl().getArmyDataByHexagon(hexagonArray.get(i));
                if(a!=null&&a.isPlayer()&&a.getArmyRound()==0){
                    if(a.getArmyType()==4||a.getArmyType()==8){
                        ifNavy=true;
                    }else if(a.getArmyType()==5||a.getArmyType()==7){
                        ifAir=true;
                        int armyMaxR=a.getMaxRange();
                        if(armyMaxR>maxRange){
                            maxRange=armyMaxR;
                        }
                    }else if(a.getArmyType()!=6){
                        ifArmy=true;
                    }
                }
            }

            if(ifArmy||ifNavy||ifAir){
                //  IntIntMap rs = gameAct.getBtl().getPotionForLegionCanActTIIM1(b,  true, selectedHexagons);
                //0无限制 1只指向海洋区域 2.只指向有空缺飞机的区域 3.只指向有空缺超武区域
                showArrow(b.getRegionId(), gameAct.getBtl().getConnectData(selectedRegionId), gameAct.getBtl().getHexagonCheckType(ifArmy,ifNavy,ifAir), false,true,selectedHexagons);
            }
            selectedHexagons.remove(b.getRegionId(),-1);
            // smapFv.setDrawMark(rs, getClickId(),0);
            if(selectedHexagons.size>0){
                if(ifAir&&maxRange>0){ //战略模式:飞机可以进攻范围
                    addAirAttackPotion(b,maxRange);
                }
                gameOperate = GameOperateState.selectRegionToLegionAct;
            }
        }
    }

    //添加飞机进攻位置
    private void addAirAttackPotion(Fb2Smap.BuildData build, int maxRange) {
        selectedHexagonIds.clear();
        gameAct.getBtl().getIdsInRange(build.getRegionId(),build.getLegionIndex(),0,maxRange,true,0,true,3,false,false,false,false,selectedHexagonIds);
        //遍历 selectedHexagons
        for(int i=0;i<selectedHexagonIds.size;i++){
            int id=selectedHexagonIds.get(i);
            if(!selectedHexagons.containsKey(id)){
                selectedHexagons.put(id,26);
                smapFv.addDrawMark(id,9,26);
            }
        }
    }


    private void playerArmyAct(ArmyActor a,int selectedRegionId) {
        if(isEditMode(false)){
            a.armyData.setTargetRegion(selectedRegionId);
        }else if(a.armyData.getArmyType()==6){

        }else if (a.armyData.getIfSeaForMove()) {
            if (gameAct.getBtl().ifSeaArea(selectedRegionId)) {
                a.armyData.setTargetRegion(selectedRegionId);
                gameAct.getBtl().army_ActToTarget(a.armyData,false);
            }
        } else {
            a.armyData.setTargetRegion(selectedRegionId);
            gameAct.getBtl().army_ActToTarget(a.armyData,false);
        }
        a.resetTarget();
        resetDefaultState();
    }


    //点击可行动兵种
    public boolean clickPlayerArmy(Fb2Smap.ArmyData armyData, int id) {
        if(rescource.drawType>1){
            ifClickArmy=false;
            return ifClickArmy;
        }
        selectedTargetArmyHexagon = armyData.getHexagonIndex();
        if(armyData.getArmyMorale()< getMainGame().resGameConfig.unitMoraleMinLimit     ){
            if(armyData.armyActor!=null&&armyData.getMaxRange()>1){
                smapFv.setDrawCircle(armyData.getHexagonIndex(),armyData.getMinRange(),armyData.getMaxRange());
            }
            ifClickArmy=false;
            return ifClickArmy;
        }
        if( gameOperate==GameOperateState.selectMultipleUnitToAct){
            IntArray hs=gameAct.getBtl().regionHexagonMap.get(gameAct.getBtl().getRegionId(id));
            if(hs!=null&&hs.size>0){
                for(int i=0,iMax=hs.size;i<iMax;i++){
                    Fb2Smap.ArmyData a=gameAct.getBtl().getArmyDataByHexagon(hs.get(i));
                    if(a!=null&&a.playerCanCommand() && a.getArmyRound() == 0&&a.getArmyType()!=6){
                        if( a.armyActor!=null){
                            selectedHexagonIds.add(a.getHexagonIndex());
                            a.armyActor.resetTarget();
                        }
                    }
                }
            }
        }

        if (armyData.playerCanCommand()&&armyData.armyActor!=null) {//是玩家未行动单位
            if (armyData.getArmyRound() == 0) {
                if(rescource.drawType==0){
                    hidArrow();
                    IntIntMap rs = gameAct.getBtl().getPotionForArmyCanActTIIM1(armyData, id, 0,true,true, selectedHexagons,!getsMapDAO().ifSystemEffective(22));
                    if (rs.size > 0) {
                        smapFv.setDrawMark(rs, getClickId(),0);
                        gameOperate = GameOperateState.selectHexagonToArmyAct;
                    }
                }else {
                    if (armyData.getArmyType() != 6) {
                        showArrow(armyData.getHexagonIndex(), gameAct.getBtl().getConnectData(selectedRegionId), armyData.getIfSeaForMove() == true ? 1 : 0, false,false,null);
                        gameOperate = GameOperateState.selectHexagonToArmyAct;
                        armyData.armyActor.setTargetState(3,0);
                    }
                }
                ifClickArmy=true;
                armyData.armyActor.clickModel();
            } else if (armyData.getTargetRegion() >= 0) {
                hidArrow();
                if(rescource.drawType==1){
                    selectedConnect = gameAct.getBtl().getConnectData(armyData.getHexagonIndex());
                    showArrow(armyData.getHexagonIndex(), armyData.getTargetRegion());
                }
                ifClickArmy=false;
            }else{
                hidArrow();
                ifClickArmy=false;
            }
        }
        if(armyData.armyActor!=null&&armyData.getMaxRange()>1){
            smapFv.setDrawCircle(armyData.getHexagonIndex(),armyData.getMinRange(),armyData.getMaxRange());
        }
        return ifClickArmy;
    }

    private void updSelectedInfo(Coord coord) {
        click_x_px = GameMap.getX_pxByHexagon(coord.x + 1, scale,0)+4;
        click_y_px = GameMap.getY_pxByHexagon(coord.x + 1, coord.y + 1, mapH_px, scale, 0, true)+2;
        //selectedCountryIndex = gameAct.getBtl().getCountryByRegion(coord.id);
        selectedRLegionIndex = gameAct.getBtl().getLegionIndexByRegion(coord.id);
        selectedHLegionIndex = gameAct.getBtl().getLegionIndexByHexagon(coord.id);
        selectedLastRegionId = selectedRegionId;
        selectedRegionId = gameAct.getBtl().getRegionId(coord.id);
        selectedBuildData = gameAct.getBtl().getBuildDataByRegion(selectedRegionId);

        // coord.logCoord();
        Fb2Map.MapHexagon m=getsMapDAO().hexagonDatas.get(coord.id);
        if(ResDefaultConfig.ifDebug&&m!=null&&selectedBuildData!=null){
            m.updHexagonBorderAttribute();
            Gdx.app.log("click", " li:" + selectedRLegionIndex + " coord:" + coord.id + " x:"+coord.getX() +" y:"+coord.getY()+  " r:" + selectedRegionId + " bi:" + selectedBuildData.getBuildIndex() + " coast:" +m.getIfCoast()
                    +" click_x_px:"+click_x_px+" click_y_px:"+click_y_px+" :"+m.getAllAttributes());
        }

        //Gdx.app.log("clickBuild",getSelectBuildData().getAllAttributes());
        //cam.logCamScope();
        selectedFacility = getsMapDAO().facilityHDatas.get(coord.id);
        if(selectedArmy!=null&&selectedArmy.armyActor!=null){
            if((selectedHexagons==null||selectedHexagons.get(coord.id,-1)!=25)){
                selectedArmy.armyActor.updArmyModel();
            }
            if( gameOperate!=GameOperateState.selectMultipleUnitToAct&&gameOperate!=GameOperateState.selectMultipleUnitToLegionAct){
                selectedArmy.armyActor.resetTarget();
            }
        }
        selectedArmy = getsMapDAO().getArmyDataByHexagon(coord.id);
        if (selectedArmy != null) {
            Gdx.app.log("clickPlayerArmy", selectedArmy.getAllAttributes());
            //armyActor.logInfo();
        }
        if (selectedBuildData != null) {
            Gdx.app.log("clickBuild", selectedBuildData.getAllAttributes());
        }
        if (selectedFacilityData != null) {
            Gdx.app.log("clickFacility", selectedFacilityData.getAllAttributes());
        }
        if (getPlayerLegionIndex() != 0||isEditMode(false)) {
            // smapBg.flashRegionByClickHexagon(coord.id);
            //clickImageActor.setScale(1.0f);
           /* if(getMainGame().sMapScreen!=null){
                clickImageActor.setCenter(click_x_px+getMainGame().sMapScreen.tempActor.getX(), click_y_px+getMainGame().sMapScreen.tempActor.getY());
            }else {
                clickImageActor.setCenter(click_x_px, click_y_px);
            }*/
            clickImageActor.setCenter(click_x_px, click_y_px);
            if(smapBg.showType ==0){;//0颜色图 1阵营图
                clickImageActor.setColor(DefDAO.getMapSelectColorByCamp(gameAct.getBtl().getCampByHexagon(coord.id)));
            }else  if(smapBg.showType==1) {
                clickImageActor.setColor(gameAct.getBtl().getColorForRegion(coord.id,false,false,false));
            }else if(smapBg.showType==2) {
                clickImageActor.setColor(gameAct.getBtl().getColorForRegion(coord.id,false,false,false));
            } else  {
                clickImageActor.setColor(gameAct.getBtl().getColorForRegion(coord.id,false,false,false));
            }
            if(getMainGame().sMapScreen!=null){
                //根据敌我更新按钮ui
                getMainGame().sMapScreen.updButtonForDefaultGroup();
                //根据点击地块情况更新左上角显示情况
                getMainGame().sMapScreen.updResourceForRegion();
                if(rescource.drawType<2&&getsMapDAO().masterData.getPlayerMode()!=2){
                    //根据点击地块情况更新右边栏显示情况
                    getMainGame().sMapScreen.updRegionBorderGroup(-1, 1);
                }else if(rescource.drawType<=1&&getsMapDAO().masterData.getPlayerMode()==2&&selectedArmy!=null){
                   getSMapScreen().updRightBorderInfoForLegionUnit();
                } else{
                    getMainGame().sMapScreen.hidRightBorderInfo();
                }

                //更新提示
                getMainGame().sMapScreen.updPromptText();
            }

        }
        if(getSMapScreen()!=null){
            getSMapScreen().selectStr = "hexagon:" + coord.id + " legionIndex:" + selectedRLegionIndex
                    + " x:"+coord.getX()
                    + " y:"+coord.getY()
                    + " countryId:" + getsMapDAO().legionDatas.get(selectedRLegionIndex).getCountryId()
                    + " ifRiver"+  (getsMapDAO().getHexagonData(coord.id).getBackTile()==11);
        }
        //TODO 测试特效  int hexagon,int refx,int refy,int rot,boolean ifFlip
        /**/  if(effectDAO!=null){
          /*if((coord.getY()&1)==1){
                effectDAO.darwEffect("mgunfire",coord.id,0,0,30,0,true);
            }else {
                effectDAO.darwEffect("mgunfire",coord.id,0,0,30,0,false);
            }*/
            // effectDAO.darwEffect("timeTravel",coord.id,0,0,0,0,false);
            //effectDAO.darwSkillEffect(coord.id,1,1);

        }
    }

    public SMapScreen getSMapScreen() {
        return getMainGame().sMapScreen;
    }

    public ArmyActor getArmyActor(int hexagon) {
        for (ArmyActor a : armyActorList) {
            if (a.armyData.getHexagonIndex() == hexagon) {
                return a;
            }
        }
        return null;
    }

    //兵种解散
    public void armyDissolve(Fb2Smap.ArmyData army,Boolean ifTure) {
        if (army.armyActor != null) {
            armyActorClear(army.armyActor);
            army.dissolve(ifTure);
            resetDefaultState();
        } else {
            Gdx.app.log("armyDissolve2", army.getAllAttributes());
            // Gdx.app.log("armyDissolve2", army.armyActor.armyData.getAllAttributes());
        }
    }

    public void airDissolve(Fb2Smap.AirData air) {
        if (air != null) {
            air.dissolve();
        }
    }

    private void playerUnitAttack(ArmyActor attackActor, int targetId,boolean ifStrike) {
        if(ifStrike){
            gameAct.getBtl().army_Attack(attackActor.armyData, targetId);
        }else{
            gameAct.getBtl().army_Attack(attackActor.armyData, targetId,1f,0f,0f);
        }

        if (!attackActor.update()) {
            if(attackActor.armyData!=null){
                attackActor.armyDeath();
            }
            armyActorClear(attackActor);
        }
        ;
        Fb2Smap.ArmyData armyData2 = getsMapDAO().getArmyDataByHexagon(targetId);
        if (armyData2 != null && armyData2.armyActor != null) {
            if (!armyData2.armyActor.update()) {
                armyData2.armyActor.armyDeath();
                armyActorClear(armyData2.armyActor);
            }
        }
        //gameAct.getBtl().combatInfos.clear();
        // getMainGame().sMapScreen.defaultWindow.hidButton(10);
    }


    public void setPotionInPlayerCaptial() {
        int c = getPlayerLegion().getCapitalId();
        if (gameAct.getBtl().ifGridIsPass(c)) {
            int x = GameMap.getHX(c, w) + 1;
            int y = GameMap.getHY(c, w) + 1;
            cam.setPotion(GameMap.getX_pxByHexagon(x, scale, 0), GameMap.getY_pxByHexagon(x, y, mapH_px, scale, 0, true), false);
            if(coord==null){
                coord=new Coord();
            }
            coord.initCoord(x, y, c);
            coord.setRegionId(gameAct.getBtl().getRegionId(coord.getId()));
            updSelectedInfo(coord);
        }else{
            clickImageActor.setCenter(-1000,-1000);
        }
    }


    //重置为默认状态
    public void resetDefaultState() {
        gameOperate = GameOperateState.noOperate;
        selectedHexagons.clear();
        smapFv.clearDrawMark();
        /*if(selectedArmy!=null&&selectedArmy.armyActor!=null){
            selectedArmy.armyActor.resetTarget();
            selectedArmy.armyActor.resetModel();
        }*/
        /*if (gameAct.getBtl().updColorRegions.size > 0) {
            updMapRegion();
        }*/
        if(selectedArmy!=null&&selectedArmy.armyActor!=null){
            selectedArmy.armyActor.resetTarget();
        }
        Fb2Smap.ArmyData a=getsMapDAO().getArmyDataByHexagon(selectedLastHexagon);
        if(a!=null&&a.armyActor!=null){
            a.armyActor.resetTarget();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (smapBg != null) {
            smapBg.dispose();
            smapBg = null;
        }
        if (smapFv != null) {
            smapFv.dispose();
            smapFv = null;
        }
        if(rescource!=null){
            if(rescource.hexagons !=null){
                rescource.hexagons.clear();
                rescource.hexagons =null;
            }
            if(rescource.tileMapHexagons !=null){
                rescource.tileMapHexagons.clear();
                rescource.tileMapHexagons =null;
            }
            if(rescource.seaMapHexagons!=null){
                rescource.seaMapHexagons.clear();
                rescource.seaMapHexagons=null;
            }
            if(rescource.allMapHexagons!=null){
                rescource.allMapHexagons.clear();
                rescource.allMapHexagons=null;
            }
            if(rescource.legionDatas!=null){
                rescource.legionDatas.clear();
                rescource.legionDatas=null;
            }
            if(rescource.buildDatas !=null){
                rescource.buildDatas.clear();
                rescource.buildDatas =null;
            }
            if(rescource.facilityDatas !=null){
                rescource.facilityDatas.clear();
                rescource.facilityDatas =null;
            }
            if(rescource.fortDatas !=null){
                rescource.fortDatas.clear();
                rescource.fortDatas =null;
            }
        }
    }

    public SMapBGActor getSmapBg() {
        return smapBg;
    }

    public void setSmapBg(SMapBGActor smapBg) {
        this.smapBg = smapBg;
    }


    public float getX() {
        return x;
    }

    //loopState 0绘制左边 1绘制两侧 2只绘制右边
    public void setX(float x) {
        if (ifLoop) {////判断当前坐标是否需要循环显示右边
            if (x <= xMin && x >= (-xMax * zoom + getMainGame().getWorldWidth())) {
                //loopState = 0;
                //Gdx.app.log("x","1");
            } else if (x > xMin) {//从起点向左
                //loopState = 1;

                x = (-xMax - getMainGame().getWorldWidth()) + x;
                // Gdx.app.log("x","2");
            } else if (x < (-xMax + getMainGame().getWorldWidth()) && x > (-xMax)) {//划入右边,接近循环
                //loopState = 2;
                // Gdx.app.log("x","3");
            } else if (x <= (-xMax - getMainGame().getWorldWidth())) {//划出并还原
                //loopState = 02;
                x = x + xMax + getMainGame().getWorldWidth();
                //  Gdx.app.log("x","4");
            }/**/
        } else {
            if (x > xMin) {
                x = xMin;
            } else if (x < -(xMax)) {
                x = -(xMax);
            }
        }
        lastX = this.x;
        this.x = x;
        //syncPotion();
    }

    public float getZoomMax() {
        return zoomMax;
    }

    public void setZoomMax(float zoomMax) {
        this.zoomMax = zoomMax;
    }

    public float getZoom() {
        return zoom;
    }


    public boolean setZoom(float zoom, float cx, float cy) {

        if (zoom < zoomMax && zoom > zoomMin) {
            this.xMax = mapW_px * zoom - getMainGame().getWorldWidth();
            ;
            this.yMax = mapH_px * zoom - getMainGame().getWorldHeight();
            this.xMin = 0;
            this.yMin = 0;
        }
        if (zoom > zoomMax) {
            zoom = zoomMax;
            return false;

        } else if (zoom < zoomMin) {
            zoom = zoomMin;
            return false;
        } else {
            setX(x + ((x - cx) * (zoom - this.zoom)) / zoom);
            setY(y + ((y - (getMainGame().getWorldHeight() - cy)) * (zoom - this.zoom)) / zoom);
            this.zoom = zoom;
            return true;
        }
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        if (y > yMin) {
            lastY = this.y;
            y = yMin;
            //Gdx.app.log("y","1");
        } else if (y < -yMax) {
            lastY = this.y;
            y = -yMax;
            //Gdx.app.log("y","2");
        }
        lastY = this.y;
        this.y = y;
        //syncPotion();
        //Gdx.app.log("y","Y"+y+" yMax:"+yMax+" zoom:"+zoom);
    }


    public int getMapW_px() {
        return mapW_px;
    }

    public void setMapW_px(int mapW_px) {
        this.mapW_px = mapW_px;
    }

    public int getMapH_px() {
        return mapH_px;
    }

    public void setMapH_px(int mapH_px) {
        this.mapH_px = mapH_px;
    }


    // 同步移动,现在改为相机,只负责通知是否循环地图
    /*private void syncPotion() {
        //smapBg.setLoopState(loopState);
        //smapBg.syncPotion(getHX(),getHY(),getZoom(),loopState);
        *//*for (ArmyActor mapImage : armyActorList) {
            if (mapImage.isVisible()) {
                mapImage.setLoopState(loopState);
            }
        }*//*
    }*/

    /*public void setLoopState(int loopState) {
        this.loopState = loopState;
        syncPotion();
    }*/

    public boolean getIfLoop() {
        return ifLoop;
    }

    public void armyActorClear(ArmyActor bmActor) {
        if (bmActor == null) {
            return;
        }
        // 从舞台中清除图像
        getRoot().removeActor(bmActor);
        // 从集合中清除该对象
        armyActorList.removeValue(bmActor, false);
        // 回收对象(放回到对象池中)
        //armyActorPool.free(bmActor);
        bmActor.remove();
        bmActor.clearListeners();
        //  Gdx.app.log("clearListener", "1");
    }

    public void fortActorClear(FortActor fActor) {
        if (fActor == null) {
            return;
        }
        // 从舞台中清除图像
        getRoot().removeActor(fActor);
        fActor.remove();
        fActor.clearListeners();

        Gdx.app.log("clearListener", "1");
    }


    //设置玩家
    public void setPlayerLegionIndex(int legionIndex) {
        if(getPlayerLegionIndex()==legionIndex){
            return;
        }
        //gameAct.getBtl().getSmapBin().getBm0().setBm0_12(playerCountry);
        gameAct.getBtl().setPlayerLegionIndex(legionIndex);
        gameAct.getBtl().initStarDialogue();
        gameAct.getBtl().updInfoByBuild();
        gameAct.getBtl().initHEventEs();
        gameAct.getBtl().resetPLegionRank();
        updAllArmyActor();
        if(getsMapDAO().masterData.getPlayerMode()==2){
            clickImageActor.setVisible(false);
        }
        resetMapZooMLimit();
        if(gameAct.getBtl().mapbin.mapImage!=null){
            this.zoomMax =ResDefaultConfig.Map.MAX_ZOOM*2;
            cam.setCamZoomMax(zoomMax);
        }
        getMainGame().save(getMainGame().getSMapDAO().masterData.getBtlType()==1?0:2);
        Gdx.app.log("setPlayerLegionIndex", getPlayerLegion().getAllAttributes());
    }
    public void setIfFixed(boolean rs){
        ifFixed=rs;
    }





    public void createNewWorld() {
        CHAsyncTask task = new CHAsyncTask("createNewWorld",999) {
            @Override
            public void onPreExecute() {
                getMainGame().saveOk=false;
            }

            @Override
            public void onPostExecute(String result) {
                getMainGame().saveOk=true;
            }
            @Override
            public String doInBackground() {
                gameAct.getBtl().saveNewWorldBin();
                return null;
            }
        };
        getMainGame().asyncManager.loadTask(task);
        getMainGame().asyncManager.update();
    }

    public int getPlayerLegionIndex() {
        return gameAct.getBtl().masterData.getPlayerLegionIndex();
    }

    public Fb2Smap.LegionData getPlayerLegion() {
        return gameAct.getBtl().getPlayerLegionData();
    }


    public Fb2Smap getsMapDAO() {
        return gameAct.getBtl();
    }


    public int getSelectedActualTerrain() {
        if(coord.id!=-1){
            return gameAct.getBtl().hexagonDatas.get(coord.id).getActualTerrain();
        }
        if(gameAct.getBtl().ifGridIsPass(selectedLastHexagon)){
            return  gameAct.getBtl().hexagonDatas.get(selectedLastHexagon).getActualTerrain();
        }
        return 0;
    }


    public int getSelectedRegionTerrain() {
        return gameAct.getBtl().getTerrainType(selectedRegionId);
    }


    public Fb2Smap.BuildData getSelectBuildData() {
        return selectedBuildData;
    }

    public boolean createArmyActor(int hexagon, int armyId) {
        Fb2Smap.ArmyData a = gameAct.getBtl().armyHDatas.get(hexagon);
        if(a==null){
            return false;
        }
        if (a.armyActor != null && a.getUnitArmyId0() == armyId) {
            if (a.getHexagonIndex() != hexagon) {
                a.armyActor.moveHexagon(hexagon);
            }
            return true;
        }
        ArmyActor armyActor = BaseActor.obtain(ArmyActor.class);
        armyActor.init(getsMapDAO(), getMainGame(), cam,  smapBg.getW(), scale, hexagon, armyId,rescource);
        addActor(armyActor);
        armyActorList.add(armyActor);
        Fb2Smap.BuildData b=a.getBuildData();
        if(b!=null){
            b.updActor();
        }
        return true;
    }

    public void createFacilityActor(Fb2Smap.FacilityData f) {

        if (f!=null&&f.facilityActor != null ) {
            return;
        }
        FacilityActor fa = BaseActor.obtain(FacilityActor.class);
        // armyActor.init(getsMapDAO(), getMainGame(), cam, smapBg.getMapW_px(), smapBg.getMapH_px(), smapBg.getW(), scale, hexagon, armyId,rescource);
        //Fb2Smap btl, MainGame game, Fb2Smap.FacilityData facilityData, int mapW_px, int mapH_px, int mapW, float scale, CamerDAO cam, SMapGameStage.SMapPublicRescource resource
        fa.init(getMainGame(),f, smapBg.getW(),scale, cam,rescource);
        addActor(fa);
        rescource.facilityDrawState=true;
        //facilityActorList.add(fa);
        // fa.setZIndex(0);
    }

    public void hidArrow() {
        for (ComActor c : arrowActorList) {
            c.setVisible(false);
        }
    }


    //limitType //0无限制 1只指向海洋区域 2.只指向有空缺飞机的区域 3.只指向有空缺超武区域 4 我方区域或敌方有单位的区域 5 海洋区域或  我方区域或敌方有单位的区域
    //ifAllyLimit 是否限定指向同盟国  ,true不能指向同盟国 false 不管
    //ifAllyPassLimit如果指向自己或盟友的build时,是否检查有空余位置
    public void showArrow(int hexagon, Fb2Smap.ConnectData c, int limitType, boolean ifAllyLimit,boolean ifAllyPassLimit,IntIntMap rs) {
        if(c==null){
            return;
        }
        //如果rs不为null,则保存相关记录
        if(rs!=null){
            rs.clear();
        }


        int x = (hexagon % smapBg.getW()) + 1;
        int y = (hexagon / smapBg.getW()) + 1;

        float x_potion = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        float y_potion = mapH_px - (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));

        float sourceX = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        float sourceY = (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));

        float targetX;
        float targetY;
        float rotation;
        // smapBg.drawHexagon(c.getRegionMainIndex(), Color.BLACK);
        int region=gameAct.getBtl().getRegionId(hexagon);

        for (int i = 0,iMax=c.getRegionConnectCount(); i < iMax; i++) {
            //int targetRegion = c.getConnectValue(i, limitType);
            int targetRegion = gameAct.getBtl().checkHexagon(c.getConnectValue(i+1,0),limitType);
            Fb2Smap.BuildData b=gameAct.getBtl().getBuildDataByRegion(targetRegion);
            if (targetRegion == -1||b==null) {//拦截海洋
                continue;
            }
            if (ifAllyLimit && !b.isPlayer() &&b.isPlayerAlly()) {
                continue;
            }
            if(ifAllyPassLimit&&b.isPlayerAlly()&&!b.haveLegionActGrid(0)){
                continue;
            }
            ComActor comActor = arrowActorList.get(i);
            comActor.clearActions();
            comActor.setVisible(true);
            comActor.setScale(0.5f);
            if (gameAct.getBtl().ifAllyByHexagon(hexagon, targetRegion)) {
                comActor.init(getMainGame(),cam, smapBg.getMapW_px(), ResDefaultConfig.Image.ARROW_BLUE);
                if(rs!=null){//可通行
                    rs.put(targetRegion, 6); //蓝色
                }
            } else {
                comActor.init(getMainGame(),cam, smapBg.getMapW_px(), ResDefaultConfig.Image.ARROW_RED);
                if(rs!=null){
                    if (b.ifHaveLegionAllyUnit(true)) {//攻击
                        rs.put(targetRegion,25);
                    }else{//占领
                        rs.put(targetRegion, 13);
                    }
                }
            }
            //设定位置
            //    GameUtil.setComActorPotionByHexagon(comActor, smapBg.getW(), smapBg.getMapH_px(), scale,c.getRegionMainIndex());

            //设定角度  ComActor actor,int mapW,int mapH_px,float scale,int sourceHexagon,int targetHexagon
            // GameUtil.setComActorRotation(comActor,smapBg.getW(),smapBg.getMapH_px(),c.getRegionMainIndex(),targetRegionDAO);
            x = (targetRegion % smapBg.getW()) + 1;
            y = (targetRegion / smapBg.getW()) + 1;
            targetX = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
            targetY = (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
            rotation = (float) ComUtil.getRadian(targetX, targetY, sourceX, sourceY);
            comActor.setRotation(rotation);
            //   Gdx.app.log("show arrow",targetRegion+"("+targetX+","+targetY+")-->"+hexagon+"("+sourceX+","+sourceY+") r:"+rotation+" cr:"+comActor.getRotation());
            //获得距离
            float distance =0;
            if(cam.loopState==1){
                if(gameAct.getBtl().ifRegionInBorder(region,1)){

                    if(gameAct.getBtl().ifRegionInBorder(targetRegion,2)){
                        comActor.setCenterPotionAndOriginByTextureRegionRef(mapW_px-x_potion+ ResDefaultConfig.Map.MAP_SCALE* ResDefaultConfig.Map.GRID_WIDTH, y_potion);
                        distance = (float) ComUtil.getDistance(targetX, targetY, sourceX, sourceY)-mapW_px;
                    }else{
                        comActor.setCenterPotionAndOriginByTextureRegionRef(x_potion+mapW_px, y_potion);
                        distance = (float) ComUtil.getDistance(targetX, targetY, sourceX, sourceY);
                    }
                }else if(gameAct.getBtl().ifRegionInBorder(region,2)&&gameAct.getBtl().ifRegionInBorder(targetRegion,1)){//
                    distance = (float) ComUtil.getDistance(targetX, targetY, sourceX, sourceY)-mapW_px;
                    comActor.setCenterPotionAndOriginByTextureRegionRef(x_potion, y_potion);
                }else{
                    distance = (float) ComUtil.getDistance(targetX, targetY, sourceX, sourceY);
                    comActor.setCenterPotionAndOriginByTextureRegionRef(x_potion, y_potion);
                }
            }else{
                distance = (float) ComUtil.getDistance(targetX, targetY, sourceX, sourceY);
                comActor.setCenterPotionAndOriginByTextureRegionRef(x_potion, y_potion);
            }

            //指向比率
            float rate = distance / (comActor.textureRegionDAO.getTextureRegion().getRegionHeight());
            comActor.setScaleY(rate / 3);
            //添加动作组

            comActor.getColor().a = 1.0F;
            // float yRate=ComUtil.min(rate,1.5f);
            SequenceAction sequence = Actions.sequence(
                    Actions.scaleTo(ComUtil.min(rate,2.0f), rate, 1.0F),
                    Actions.alpha(0.0F, 2.0F),
                    // Runnable 复位
                    Actions.scaleTo(ComUtil.min(rate,2.0f) / 3,rate / 6, 0.0F),
                    Actions.alpha(1.0F, 0F)
            );
            RepeatAction repeatAction = Actions.forever(sequence);

            comActor.addAction(repeatAction);

            // smapBg.drawHexagon(targetRegionDAO,Color.RED);
            //   Gdx.app.log("arrowPoint","c:"+c.getRegionConnectCount()+":"+i+" region:"+c.getRegionMainIndex()+"->"+targetRegion+" rotation:"+comActor.getRotation() +" rate:"+rate+" distance:"+distance);
        }

    }


    public void tempActorListClear() {
        if (tempActorList.size > 0) {
            Iterator<BaseActor> it = tempActorList.iterator();
            while (it.hasNext()) {
                BaseActor actor=it.next();
                // 从舞台中清除图像
                getRoot().removeActor(actor);
                actor.remove();
            }
            tempActorList.clear();
        }
    }

    public void showTempArrow(int hexagon, int targetRegion) {
        int x = (hexagon % smapBg.getW()) + 1;
        int y = (hexagon / smapBg.getW()) + 1;

        float x_potion = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        float y_potion = mapH_px - (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));

        float sourceX = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        float sourceY = (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));

        float targetX;
        float targetY;

        // smapBg.drawHexagon(c.getRegionMainIndex(), Color.BLACK);
        // int targetRegion= DefDAO.getConnectRegion(c,i);
        ComActor comActor = BaseActor.obtain(ComActor.class);
        addActor(comActor);
        tempActorList.add(comActor);
        comActor.clearActions();
        comActor.setVisible(true);
        comActor.setScale(0.5f);
        if (gameAct.getBtl().ifAllyByHexagon(hexagon, targetRegion)) {
            comActor.init(getMainGame(),cam, smapBg.getMapW_px(), ResDefaultConfig.Image.ARROW_BLUE);
        } else {
            comActor.init(getMainGame(),cam, smapBg.getMapW_px(), ResDefaultConfig.Image.ARROW_RED);
        }
        //设定位置
        //    GameUtil.setComActorPotionByHexagon(comActor, smapBg.getW(), smapBg.getMapH_px(), scale,c.getRegionMainIndex());
        comActor.setCenterPotionAndOriginByTextureRegionRef(x_potion, y_potion);
        //设定角度  ComActor actor,int mapW,int mapH_px,float scale,int sourceHexagon,int targetHexagon
        // GameUtil.setComActorRotation(comActor,smapBg.getW(),smapBg.getMapH_px(),c.getRegionMainIndex(),targetRegionDAO);
        x = (targetRegion % smapBg.getW()) + 1;
        y = (targetRegion / smapBg.getW()) + 1;
        targetX = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        targetY = (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
        comActor.setRotation((float) ComUtil.getRadian(targetX, targetY, sourceX, sourceY));

        //获得距离
        float distance = (float) ComUtil.getDistance(targetX, targetY, sourceX, sourceY);

        //指向比率
        float rate = distance / (comActor.textureRegionDAO.getTextureRegion().getRegionHeight());
        comActor.setScaleY(rate / 3);
        //添加动作组

        comActor.getColor().a = 1.0F;

       /* SequenceAction sequence = Actions.sequence(
                Actions.scaleTo(rate,rate, 1.0F),
                Actions.alpha(0.0F, 2.0F),
                // Runnable 复位
                Actions.scaleTo(rate/3, rate / 6, 0.0F),
                Actions.alpha(1.0F, 0F)

        );*/
        SequenceAction sequence = Actions.sequence(
                Actions.scaleTo(ComUtil.min(rate,2.0f), rate, 1.0F),
                Actions.alpha(0.0F, 2.0F),
                // Runnable 复位
                Actions.scaleTo(ComUtil.min(rate,2.0f) / 3,rate / 6, 0.0F),
                Actions.alpha(1.0F, 0F)
        );
        RepeatAction repeatAction = Actions.forever(sequence);
        comActor.addAction(repeatAction);
        // smapBg.drawHexagon(targetRegionDAO,Color.RED);
        ;
        //Gdx.app.log("arrowPoint","c:"+c.getRegionConnectCount()+":"+i+" region:"+c.getRegionMainIndex()+"->"+targetRegion+" rotation:"+comActor.getRotation() +" rate:"+rate+" distance:"+distance);
    }


    public void showArrow(int hexagon, int targetRegion) {

        int x = (hexagon % smapBg.getW()) + 1;
        int y = (hexagon / smapBg.getW()) + 1;

        float x_potion = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        float y_potion = mapH_px - (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));

        float sourceX = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        float sourceY = (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));

        float targetX;
        float targetY;

        // smapBg.drawHexagon(c.getRegionMainIndex(), Color.BLACK);


        // int targetRegion= DefDAO.getConnectRegion(c,i);
        ComActor comActor = arrowActorList.get(0);
        comActor.clearActions();
        comActor.setVisible(true);
        comActor.setScale(0.5f);
        if (gameAct.getBtl().ifAllyByHexagon(hexagon, targetRegion)) {
            comActor.init(getMainGame(),cam, smapBg.getMapW_px(), ResDefaultConfig.Image.ARROW_BLUE);
        } else {
            comActor.init(getMainGame(),cam, smapBg.getMapW_px(), ResDefaultConfig.Image.ARROW_RED);
        }
        //设定位置
        //    GameUtil.setComActorPotionByHexagon(comActor, smapBg.getW(), smapBg.getMapH_px(), scale,c.getRegionMainIndex());
        comActor.setCenterPotionAndOriginByTextureRegionRef(x_potion, y_potion);
        //设定角度  ComActor actor,int mapW,int mapH_px,float scale,int sourceHexagon,int targetHexagon
        // GameUtil.setComActorRotation(comActor,smapBg.getW(),smapBg.getMapH_px(),c.getRegionMainIndex(),targetRegionDAO);
        x = (targetRegion % smapBg.getW()) + 1;
        y = (targetRegion / smapBg.getW()) + 1;
        targetX = (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        targetY = (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
        comActor.setRotation((float) ComUtil.getRadian(targetX, targetY, sourceX, sourceY));

        //获得距离
        float distance = (float) ComUtil.getDistance(targetX, targetY, sourceX, sourceY);

        //指向比率
        float rate = distance / (comActor.textureRegionDAO.getTextureRegion().getRegionHeight());
        comActor.setScaleY(rate / 3);
        //添加动作组

        comActor.getColor().a = 1.0F;

        /*SequenceAction sequence = Actions.sequence(
                Actions.scaleTo(rate, rate, 1.0F),
                Actions.alpha(0.0F, 2.0F),
                // Runnable 复位
                Actions.scaleTo(rate / 3,rate / 6, 0.0F),
                Actions.alpha(1.0F, 0F)
        );*/
        SequenceAction sequence = Actions.sequence(
                Actions.scaleTo(ComUtil.min(rate,2.0f), rate, 1.0F),
                Actions.alpha(0.0F, 2.0F),
                // Runnable 复位
                Actions.scaleTo(ComUtil.min(rate,2.0f) / 3,rate / 6, 0.0F),
                Actions.alpha(1.0F, 0F)
        );
        RepeatAction repeatAction = Actions.forever(sequence);

        comActor.addAction(repeatAction);

        // smapBg.drawHexagon(targetRegionDAO,Color.RED);
        ;
        //Gdx.app.log("arrowPoint","c:"+c.getRegionConnectCount()+":"+i+" region:"+c.getRegionMainIndex()+"->"+targetRegion+" rotation:"+comActor.getRotation() +" rate:"+rate+" distance:"+distance);


    }

    /*public boolean toCommand(){
        if( gameOperate==GameOperateState.noOperate&& selectedRLegionIndex ==playerLegionIndex&&gameAct.getBtl().playerCanArmyAct(selectedRegionId)){//如果点击的是我方归属的一个未行动的兵,则显示相关标记
            gameOperate=GameOperateState.clickRegionToArmyAct;
            //Gdx.app.log("chlickRegion1","selectedArmyActorIndex:"+ selectedArmyActorIndex);
            //指向邻近省区的蓝色箭头
            selectConnect =gameAct.getBtl().getBorderRegion(selectedRegionId);
            hidArmyActorForRegion(selectedRegionId);
            showArrow(-1,selectConnect,0);
        } else  if(gameOperate==GameOperateState.selectPlayerArmyToCommand){//如果是移动兵种

            boolean ifBlueArrow= gameAct.getBtl().ifAllyByRegion(selectedRegionId,selectedTargetRegion);
            float rotation;
            //设置对应兵种数据到目标区域
            for(ArmyActor armyActor:armyActorList){
                if(getsMapDAO().masterData.getPlayerLegionIndex()!=armyActor.armyData.getBuildPolicy()){
                    continue;
                }
                if(getsMapDAO().hexagonDatas.get(armyActor.getHexagon()).getRegionId()==selectedRegionId){
                    armyActor.setVisible(true);
                    if(armyActor.targetState==4){
                        armyActor.armyData.setTargetRegion(selectedTargetRegion);
                        rotation=      GameUtil.getRotationByHexagon(smapBg.getW(),smapBg.getMapH_px(),armyActor.getHexagon(),selectedTargetRegion,scale);
                        //设置兵种箭头,角度
                        if(ifBlueArrow){
                            armyActor.setTargetState(2,rotation);
                        }else{
                            armyActor.setTargetState(1,rotation);
                        }
                    }else{
                        armyActor.restoreTargetArrow();
                    }
                }else{
                    armyActor.restoreTargetArrow();
                }
            }
            //恢复状态
            gameOperate=GameOperateState.noOperate;
            selectedTargetRegion=0;
            hidArrow();
        }else if(gameOperate==GameOperateState.clickRegionToArmyAct){
            gameOperate=GameOperateState.noOperate;
            hidArrow();
            for(ArmyActor armyActor:armyActorList){
                armyActor.setVisible(true);
            }
        }else {
            return false;
        }
        return true;
    }*/

    public void hidArmyActorForRegion(int region) {
        for (ArmyActor armyActor : armyActorList) {
            if (getsMapDAO().hexagonDatas.get(armyActor.getHexagon()).getRegionId() == region) {
                armyActor.setVisible(false);
            }
        }
    }

    public void showArmyActorForRegionAndCanSelect(int region) {
        for (ArmyActor armyActor : armyActorList) {
            if (getsMapDAO().hexagonDatas.get(armyActor.getHexagon()).getRegionId() == region) {
                armyActor.setVisible(true);
            }
        }
    }

    public void restorePlayerArmyActorState() {
        for (ArmyActor armyActor : armyActorList) {
            armyActor.setVisible(true);
            if (armyActor.armyData.getLegionIndex() == getPlayerLegionIndex() ) {
                armyActor.resetTarget();
            }
        }
    }

    private void resetFV(Actor actor) {
        getRoot().swapActor(smapFv, actor);
    }


    public void updAllBuildActor() {

      /*  Iterator<IntMap.Entry<Fb2Smap.BuildData>> it = gameAct.getBtl().buildRDatas.iterator();
        while (it.hasNext()) {
            IntMap.Entry<Fb2Smap.BuildData> c = it.next();
            Fb2Smap.BuildData buildData = c.value;*/
        for(int bi=0;bi<gameAct.getBtl().buildRDatas.size();bi++) {
            Fb2Smap.BuildData b=gameAct.getBtl().buildRDatas.getByIndex(bi);
            if (b == null) {
                continue;
            }
            if(b.getBuildActor()==null){
                BuildActor buildActor = new BuildActor( getMainGame(), b, smapBg.getW(), scale, cam, rescource);
                addActor(buildActor);
                buildActorList.add(buildActor);
            }else{
                b.updActor();
            }

        }
    }

    public void updAllFacilityActor() {
        Iterator<IntMap.Entry<Fb2Smap.FacilityData>> it = gameAct.getBtl().facilityHDatas.iterator();
        while (it.hasNext()) {
            Fb2Smap.FacilityData facilityData = it.next().value;
            if (facilityData == null) {
                continue;
            }
            if(facilityData.facilityActor==null){
                createFacilityActor(facilityData);
            }
        }
    }

    public void updAllFortActor() {

        Iterator<IntMap.Entry<Fb2Smap.FortData>> it = gameAct.getBtl().fortHDatas.iterator();
        while (it.hasNext()) {
            Fb2Smap.FortData fortData = it.next().value;
            if (fortData == null) {
                continue;
            }
            //Fb2Smap btl, MainGame game, Fb2Smap.BuildData buildData, int mapW_px, int mapH_px, int mapW, float scale, CamerDAO cam
            //    FortActor fortActor = new FortActor(getsMapDAO(), getMainGame(), fortData, mapW_px, mapH_px, smapBg.getW(), scale, cam);
            if(fortData.fortActor==null){
                FortActor fortActor = BaseActor.obtain(FortActor.class);
                fortActor.init( getMainGame(), fortData,  smapBg.getW(), scale, cam,rescource);
                addActor(fortActor);
                /*if(!fortActorList.contains(fortActor,false)){
                    fortActorList.add(fortActor);
                }*/
            }else{
                fortData.fortActor.update();
            }
        }
    }

    public void updAllArmyActor() {

       for  (int i=0;i<gameAct.getBtl().hexagonDatas.size;i++) {
           Fb2Map.MapHexagon h=gameAct.getBtl().hexagonDatas.get(i);
           if(h==null){
               continue;
           }
            Fb2Smap.ArmyData armyData=h.armyData;
            if (armyData == null) {
                continue;
            }
            if (getsMapDAO().masterData.getIfFog() == 1) {//开启迷雾
                if (!getsMapDAO().ifAllyPlayerByLi(armyData.getLegionIndex()) && h.getIfFog() == 0) {
                    try {
                        armyData.removeActor();
                    } catch (Exception e) {
                    }
                    continue;
                }
            }
            if (armyData.armyActor != null) {
                armyData.armyActor.resetHexagon();
            } else {
                createArmyActor(armyData.getHexagonIndex(), armyData.getUnitArmyId0());
            }
            if(armyData.armyActor != null){
                armyData.armyActor.updHpColor();
                armyData.armyActor.update();
                armyData.armyActor.updArmyModel();
            }
        }
    }


    /*public void updMapRegion() {
        smapBg.updColorByIds(getsMapDAO().getUpdColorRegion(updColorIds));
        updColorIds.clear();
    }*/



    //保存下一个国家
    public void openNextWorldConfig() {
        if (getsMapDAO().masterData.getNextStageId() != 0) {
            getMainGame().gameConfig.playerConfig.putBoolean(getsMapDAO().masterData.getNextStageId() + "_ifNew", true);
            getMainGame().gameConfig.playerConfig.flush();
        }
    }


    public void showCanBuildHexagon(int cardId, int cardType) {
        IntIntMap rs = gameAct.getBtl().getHexagonToBuild(selectedRegionId,  cardType,cardId, selectedHexagons);
        if (rs.size > 0) {
            if(rescource.drawType>0){
                cam.asyncMoveCameraToHexagon(selectedRegionId,  1, 0.7f);//asyncMoveCameraToHexagon(int id,  final int moveSecond,  final float targetZoom ){
            }
            smapFv.setDrawMark(rs,getClickId(), 0);
            gameOperate = GameOperateState.selectHexagonToBuild;
        }
    }






    /*public void setUpdAnimation(int hexagon){
        clickImageActor.SetTextureRegionDAO(getMainGame().getImgLists().getTextureByName("animation_upd"));

        int  x=GameMap.getHX(hexagon,w)+1;
        int y=GameMap.getHY(hexagon,w)+1;
      float x_px=GameMap.getX_pxByHexagon(x,scale,0);
      float y_px=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        clickImageActor.setCenter(x_px,y_px);


        RunnableAction action3 = Actions.run(new Runnable() {
            @Override public void run() {resetClick();}
        });
        DelayAction delay = Actions.delay(1.0F);
        // 顺序动作, 先闪烁n秒,然后消失还原
        SequenceAction sequenceAction = Actions.sequence(delay, action3);

        // 执行顺序动作
        clickImageActor.addAction(sequenceAction);

    }*/

    public void resetClick() {
        clickImageActor.SetTextureRegionDAO(getMainGame().getImgLists().getTextureByName("map_selected"));

        clickImageActor.setScale(scale * ResDefaultConfig.Map.MAP_SCALE);
        clickImageActor.setCenter(click_x_px, click_y_px);
    }

    /*public void setBuildAnimation(int hexagon){
        clickImageActor.SetTextureRegionDAO(getMainGame().getImgLists().getTextureByName("animation_build"));

        int  x=GameMap.getHX(hexagon,w)+1;
        int y=GameMap.getHY(hexagon,w)+1;
        float x_px=GameMap.getX_pxByHexagon(x,scale,0);
        float y_px=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        clickImageActor.setCenter(x_px,y_px);

        RunnableAction action3 = Actions.run(new Runnable() {
            @Override public void run() {resetClick();}
        });

        DelayAction delay = Actions.delay(1.0F);
        // 顺序动作, 先闪烁n秒,然后消失还原
        SequenceAction sequenceAction = Actions.sequence(delay, action3);

        // 执行顺序动作
        clickImageActor.addAction(sequenceAction);
    }*/

    public void updClickActor() {
        clickImageActor.setZIndex(99999);
    }

    public void setCoord(int hexagon) {
        int x = GameMap.getHX(hexagon, w);
        int y = GameMap.getHY(hexagon, w);
        coord.initCoord(x, y, hexagon);
        coord.setRegionId(gameAct.getBtl().getRegionId(coord.getId()));
        updSelectedInfo(coord);
    }

    public void initPotion() {
        if (coord == null) {
            coord = new Coord();
        }
        int hexagon = getPlayerLegion().getCapitalId();
        int x = GameMap.getHX(hexagon, w);
        int y = GameMap.getHY(hexagon, w);
        coord.initCoord(x, y, hexagon);
        coord.setRegionId(gameAct.getBtl().getRegionId(coord.getId()));
    }

    public void selectedNextUnit() {
        if(rescource.drawType<2){ //0 绘制兵模 1 绘制标志 2 绘制国旗
            armyActorList.shuffle();
            for (int i = 0, iMax = armyActorList.size; i < iMax; i++) {
                ArmyActor a = armyActorList.get(i);
                if(rescource.drawType==1&&a.armyData.getArmyType()==6){//在小图标状态的下一个单位应该跳过防御建筑
                    continue;
                }
                if(rescource.drawType==1&&isEditMode(false)){
                    if (a.armyData.playerCanCommand()&&a.armyData.getArmyType()!=6) {
                        if (a.armyData.getTargetRegion()==-1) {
                            selectedArmyAndMoveCam(a);
                            return;
                        }
                    }
                }else if(getsMapDAO().masterData.getPlayerMode()==2){
                    if (a.getIfFlash()&&a.armyData.getTargetRegion()==-1&&a.armyData.getLegionIndex()==getsMapDAO().masterData.getPlayerLegionIndex()) {
                        if (a.armyData.getArmyRound() == 0) {
                            selectedArmyAndMoveCam(a);
                            return;
                        }
                    }
                }else {
                    if (a.getIfFlash()&&a.armyData.playerCanCommand()) {
                        if (a.armyData.getArmyRound() == 0) {
                            selectedArmyAndMoveCam(a);
                            return;
                        }
                    }
                }
            }
        }else{
            buildActorList.shuffle();
            for (int i = 0, iMax = buildActorList.size; i < iMax; i++) {
                BuildActor b = buildActorList.get(i);
                if(b!=null&&b.buildData.getLegionIndex()==getPlayerLegionIndex()&&b.buildData.getBuildStatus()!=0){
                    selectedCoordAndMoveCam(b.buildData.getRegionId());
                    return;
                }
            }
        }
    }

    public void selectedArmyByHexagon(int hexagon){
        Fb2Smap.ArmyData a=gameAct.getBtl().getArmyDataByHexagon(hexagon);
        if(a!=null&&a.armyActor!=null){
            selectedArmyAndMoveCam(a.armyActor);
        }
    }


    private void selectedArmyAndMoveCam(ArmyActor a) {
        hidArrow();
        setCoord(a.getHexagon());
        cam.asyncMoveCameraToHexagon2(a.getHexagon(),/* click_x_px, click_y_px,*/ 2, 0, 0.8f, 0);
        if(gameAct.getBtl().masterData.getPlayerMode()!=2) {
            clickPlayerArmy(a.armyData, coord.id);
        }else {
            clickRegionForLegionAct(selectedBuildData);
        }
    }

    private void selectedCoordAndMoveCam(int hexagon) {
        hidArrow();
        setCoord(hexagon);
        cam.asyncMoveCameraToHexagon2(hexagon,/* click_x_px, click_y_px,*/ 2, 0, 0.8f, 0);
    }

    private void playerAirAct(Fb2Smap.AirData air, int targetHexagon) {
        if (air.getAirRound() != 0) {
            return;
        }
        Fb2Smap.ArmyData armyData = gameAct.getBtl().getArmyDataByHexagon(targetHexagon);

        if (armyData != null && armyData.getLegionIndex() == air.getLegionIndex()&&armyData.getAirCount()<4 &&   (armyData.ifHaveFeature(2)||armyData.getArmyType()==4) ) {
            int hexagon=air.getHexagon();
            if (armyData.loadAir(air)) {
                air.addAirRound(1);
                getSMapScreen().smapEffectStage.drawEffect(air.getAirId(),hexagon,targetHexagon,1.5f);
                getMainGame().playSound(this,ResDefaultConfig.Sound.飞机移动,1.5f);
                return;
            }
        }
      int region  =gameAct.getBtl().getRegionId(targetHexagon);
        Fb2Smap.BuildData buildData = gameAct.getBtl().getBuildDataByRegion(region);
        if ((armyData==null||armyData.getLegionIndex()==air.getLegionIndex())&&buildData != null && buildData.getLegionIndex() == air.getLegionIndex()) {
            gameAct.getBtl().air_Move(air, region);
            air.addAirRound(1);
            getMainGame().playSound(this,ResDefaultConfig.Sound.飞机移动,0f);
        } else {
            if (gameAct.getBtl().air_Attack(air, targetHexagon)) {
                selectedAir.setTargetRegion(selectedRegionId);
                getMainGame().playSound(this,ResDefaultConfig.Sound.飞机移动,0f);
                getMainGame().playSound(this,ResDefaultConfig.Sound.轻炮,0.7f);
            }else if (gameAct.getBtl().air_Attack(air, region)) {
                selectedAir.setTargetRegion(selectedRegionId);
                getMainGame().playSound(this,ResDefaultConfig.Sound.飞机移动,0f);
                getMainGame().playSound(this,ResDefaultConfig.Sound.轻炮,0.7f);
            } else {
                gameAct.getBtl().air_Standby(air);
                air.addAirRound(1);
                getMainGame().playSound(this,ResDefaultConfig.Sound.取消选择,0f);
            }
            if(air.getAirGoodsNow()==0){
                air.addAirRound(1);
            }
            //TODO 飞机进攻特效绘制
        }
        resetDefaultState();
    }


    private void playerAirborne(Fb2Smap.ArmyData army, Fb2Smap.AirData air, int id) {
        if (air==null||army==null||air.getAirRound() != 0) {
            return;
        }
        if (army != null&&gameAct.getBtl().air_airborne(army,air,id)  ) {
            army.addArmyRound(1);
            getSMapScreen().smapEffectStage.drawEffect(air.getAirId(),army.getHexagonIndex(),id,1.5f);
            return;
        }

    }

    //actType:7,可部署单位 13,可移动位置 25 可进攻位置
    private void playerNulAct(Fb2Smap.NulcleData nul, int targetHexagon, int actType) {
        boolean rs = false;
        switch (actType) {
            case 7:
                Fb2Smap.ArmyData armyData = gameAct.getBtl().getArmyDataByHexagon(targetHexagon);
                if (armyData != null) {
                    rs = armyData.loadNul(nul);
                    if (rs) {
                        getSMapScreen().smapEffectStage.drawEffect(1501, nul.getRegionId(), targetHexagon,1.5f);
                        nul.addNucleRound(1);
                        nul.remove(1);
                        getMainGame().playSound(ResDefaultConfig.Sound.占领);
                    }
                } else {
                    getMainGame().playSound(ResDefaultConfig.Sound.取消选择);
                    rs = false;
                }
                break;
            case 13:
                rs = gameAct.getBtl().nul_Move(nul, targetHexagon);
                if (rs) {
                    getMainGame().playSound(ResDefaultConfig.Sound.飞机移动);
                    nul.addNucleRound(1);
                }
                break;
            case 25:
                getMainGame().playSound(this,ResDefaultConfig.Sound.毒气,0f);
                rs = gameAct.getBtl().nul_Attack(nul, targetHexagon);
                break;

            default:
                Gdx.app.error("playerNulAct none actType", nul.getNucleIndex() + ":" + targetHexagon);
        }
    }




    //创建防御工事showArrow actor
    public void createFortActor(Fb2Smap.FortData fortData) {
        //  FortActor f = new FortActor(getsMapDAO(), getMainGame(), fortData, mapW_px, mapH_px, smapBg.getW(), scale, cam,rescource);
        FortActor fortActor = BaseActor.obtain(FortActor.class);
        fortActor.init( getMainGame(), fortData,  smapBg.getW(), scale, cam,rescource);
        // fortActor.setDrawState(fortData.getDrawState(),true);
        addActor(fortActor);
        rescource.fortDrawState=true;
       /* if(!fortActorList.contains(fortActor,false)){
            fortActorList.add(fortActor);
        }*/
    }

    public void selectedGameStage(int hexagon) {
        int x = GameMap.getHX(hexagon, w);
        int y = GameMap.getHY(hexagon, w);
        if(coord==null){
            coord=new Coord();
        }
        coord.initCoord(x, y, hexagon);
        coord.setRegionId(gameAct.getBtl().getRegionId(coord.getId()));
        clickGameStage(coord);
    }



    /* //根据目标集合来显示箭头
     public void showArrow(int hexagon,IntArray targetIds){



         int x = (hexagon % smapBg.getW()) + 1;
         int y = (hexagon / smapBg.getW()) + 1;

         float  x_potion=  (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
         float  y_potion=mapH_px-  (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));

         float sourceX =(scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
         float sourceY =  (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));

         float targetX;
         float targetY;
         float rotation;
         // smapBg.drawHexagon(c.getRegionMainIndex(), Color.BLACK);


         for(int i=0,iMax=targetIds.size;i<iMax;i++){
             int targetRegion=targetIds.get(i);
             if(targetRegion==-1){//拦截海洋
                 continue;
             }


             ComActor comActor = comActorList.get(i);
             comActor.clearActions();
             comActor.setVisible(true);
             comActor.setScale(0.5f);
             if(gameAct.getBtl().ifAllyByHexagon(hexagon,targetRegion)){
                 comActor.init(getMainGame(),smapBg.getMapW_px(),ResConfig.Image.ARROW_BLUE);
             }else{
                 comActor.init(getMainGame(),smapBg.getMapW_px(),ResConfig.Image.ARROW_RED);
             }
             //设定位置
             //    GameUtil.setComActorPotionByHexagon(comActor, smapBg.getW(), smapBg.getMapH_px(), scale,c.getRegionMainIndex());
             comActor.setCenterPotion(x_potion,y_potion);
             //设定角度  ComActor actor,int mapW,int mapH_px,float scale,int sourceHexagon,int targetHexagon
             // GameUtil.setComActorRotation(comActor,smapBg.getW(),smapBg.getMapH_px(),c.getRegionMainIndex(),targetRegionDAO);
             x=(targetRegion % smapBg.getW()) + 1;
             y = (targetRegion / smapBg.getW()) + 1;
             targetX = (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
             targetY =  (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));
             rotation=(float) ComUtil.getRadian(targetX,targetY,sourceX,sourceY);
             comActor.setRotation(rotation);
             //   Gdx.app.log("show arrow",targetRegion+"("+targetX+","+targetY+")-->"+hexagon+"("+sourceX+","+sourceY+") r:"+rotation+" cr:"+comActor.getRotation());
             //获得距离
             float distance= (float) ComUtil.getDistance(targetX,targetY,sourceX,sourceY);

             //指向比率
             float rate=distance/(comActor.textureRegion.getTextureRegion().getRegionHeight());
             comActor.setScaleY(rate/3);
             //添加动作组

             comActor.getColor().a = 1.0F;

             SequenceAction sequence = Actions.sequence(
                     Actions.scaleTo(0.5F, rate, 1.0F),
                     Actions.alpha(0.0F, 2.0F),
                     // Runnable 复位
                     Actions.scaleTo(0.5F, rate/3, 0.0F),
                     Actions.alpha(1.0F, 0F)

             );
             RepeatAction repeatAction = Actions.forever(sequence);

             comActor.addAction(repeatAction);

             // smapBg.drawHexagon(targetRegionDAO,Color.RED);
             ;
             //    Gdx.app.log("arrowPoint","c:"+c.getRegionConnectCount()+":"+i+" region:"+c.getRegionMainIndex()+"->"+targetRegion+" rotation:"+comActor.getRotation() +" rate:"+rate+" distance:"+distance);
         }

     }*/
    @Override
    public void act(float delta) {

        try {
            super.act(delta);
        } catch (Exception e) {
            /*if(ResDefaultConfig.ifDebug){
                e.printStackTrace();
            }*/
        }
        deltaSum += delta;
        if (deltaSum < 2.0f) {
            if (deltaSum < 1.0f) {
                alphaFlash += delta;
            } else if (alphaFlash < 0) {
                alphaFlash += delta;
            } else {
                alphaFlash -= delta;
            }
            zoomChange+=delta/5;
        } else {
            deltaSum = 0f;
            zoomChange=0.5f;
        }
    }

    //重写绘制方法,当绘制为指定类型时,使用分层绘制保证drawCall的一致性
    @Override
    public void draw() {
        super.draw();
        try {
            drawGame(getBatch());
        } catch (Exception e) {
            if(ResDefaultConfig.ifDebug){
                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
            }else if(!getMainGame().gameConfig.getIfIgnoreBug()){
                getMainGame().remindBugFeedBack();
            }
            getMainGame().recordLog("ResGameConfig draw ",e);
             if(getsMapDAO().roundState==0){

            }else {
                getSMapScreen().resetPlayerStar();
                getSMapScreen().showLegionTechUI(getPlayerLegion());
                getSMapScreen().showSaveUI();
            }
        }
      /*  rescource.buildState=0;
       if(rescource.drawType<2){
            super.getBatch().begin();
            rescource.buildState=1;
            for (BuildActor b : buildActorList) {
                if (b != null) {
                    b.draw(super.getBatch(), 1);
                }
            }
            for (FacilityActor f : facilityActorList) {
                if (f != null) {
                    f.draw(super.getBatch(), 1);
                }
            }
            for (FortActor fort : fortActorList) {
                if (fort != null) {
                    fort.draw(super.getBatch(), 1);
                }
            }
            super.getBatch().end();
        }



       if(rescource.drawType==0){//兵模
            rescource.setArmyActorState(0);
            super.draw();
            if( getMainGame().gameConfig.ifEffect){
                for (int i = 1,iMax= 5; i < iMax; i++) {
                    rescource.setArmyActorState(i);
                    super.getBatch().begin();
                    for (ArmyActor a : armyActorList) {
                        if (a != null) {
                            a.draw(super.getBatch(), 1);
                        }
                    }
                    if(i==2||i==3){
                        rescource.buildState=i;
                        for (BuildActor b : buildActorList) {
                            if (b != null) {
                                b.draw(super.getBatch(), 1);
                            }
                        }
                    }
                    super.getBatch().end();
                }
            }else{
                for (int i = 1,iMax=2; i < iMax; i++) {
                    rescource.setArmyActorState(i);
                    super.getBatch().begin();
                    for (ArmyActor a : armyActorList) {
                        if (a != null) {
                            a.draw(super.getBatch(), 1);
                        }
                    }
                    super.getBatch().end();
                }
            }

        }else   if(rescource.drawType==1||rescource.drawType==2){//标志
           super.draw();
           for (int i = 1; i < 4; i++) {
               rescource.setArmyActorState(i);
               super.getBatch().begin();

               for (ArmyActor a : armyActorList) {
                   if (a != null) {
                       a.draw(super.getBatch(), 1);
                   }
               }

               if(i==2||i==3){
                   rescource.buildState=i;
                   for (BuildActor b : buildActorList) {
                       if (b != null) {
                           b.draw(super.getBatch(), 1);
                       }
                   }
               }
               super.getBatch().end();
           }
       }else if(rescource.drawType==3){
           super.draw();
           rescource.buildState=1;
           super.getBatch().begin();
           for (BuildActor b : buildActorList) {
               if (b != null) {
                   b.draw(super.getBatch(), 1);
               }
           }
           super.getBatch().end();
       }else{
           super.draw();
       }*/
    }

    public void hidAllBuildActor() {
        for(BuildActor build:buildActorList){
            build.setVisible(false);
        }
    }

    public void clickMultipleUnitToAct() {
        if(selectedArmy!=null&&selectedArmy.playerCanCommand()&&selectedArmy.armyActor!=null&&selectedArmy.getArmyType()!=6){
            if(selectedHexagonIds.contains(coord.id)){
                selectedHexagonIds.removeValue(coord.id);
                selectedArmy.armyActor.resetTarget();
            }else{
                selectedHexagonIds.add(coord.id);
                selectedArmy.armyActor.setTargetState(3,0);
            }
        }
    }
    public void clickNotTargetMultipleUnitToAct() {
        if(gameAct.getBtl().ifGridIsPass(selectedRegionId)){
            selectedHexagonIds.clear();
            IntArray rs=gameAct.getBtl().regionHexagonMap.get(selectedRegionId);
            for(int i=0;i<rs.size;i++){
                Fb2Smap.ArmyData a=gameAct.getBtl().getArmyDataByHexagon(rs.get(i));
                if(a!=null&&a.playerCanCommand()&&a.armyActor!=null&&a.getArmyType()!=6&&a.getTargetRegion()==-1){
                    selectedHexagonIds.add(a.getHexagonIndex());
                    a.armyActor.setTargetState(3,0);
                }
            }
        }
    }

    public int getClickId() {
        if(coord==null){
            return -1;
        }
        return coord.id;
    }

    public void legionAct() {
        Fb2Smap.BuildData b=gameAct.getBtl().getBuildDataByRegion(selectedTargetRegion);
        if(b!=null){
            Array<Fb2Smap.ArmyData> armyDataArrays =getsMapDAO().tempArmyDataArray;
            if(armyDataArrays==null){
                armyDataArrays=new Array<>();
            }else{
                armyDataArrays.clear();
            }
            for(ArmyActor a:armyActorList){
                if(a.armyData.getArmyType()==6){  continue;}
                if(a.targetState==3){
                    a.armyData.setTargetRegion(-1);
                    armyDataArrays.add(a.armyData);
                }
            }
            // int targetRegion=coord.getRegionId();
            if(armyDataArrays.size>0){
                gameAct.getBtl().legionUnit_Act(armyDataArrays.get(0).getBuildData(),b,armyDataArrays,true);
            }
        }
        restorePlayerArmyActorState();
        getMainGame().sMapScreen.defaultWindow.hidButton(13);
        resetDefaultState();
    }

    public void legionSetTarget(){
        Fb2Smap.BuildData b=gameAct.getBtl().getBuildDataByRegion(selectedTargetRegion);
        if(b!=null){
            for(ArmyActor a:armyActorList){
                if(a.armyData.getArmyType()==6){  continue;}
                if(a.targetState==3){
                    a.armyData.setTargetRegion(selectedTargetRegion);
                }
            }
        }
        restorePlayerArmyActorState();
        getMainGame().sMapScreen.defaultWindow.hidButton(13);
        resetDefaultState();
    }

    public void setLegionTarget() {
        Fb2Smap.BuildData b=gameAct.getBtl().getBuildDataByRegion(selectedTargetRegion);
        if(b!=null){
            Array<Fb2Smap.ArmyData> armyDataArrays =getsMapDAO().tempArmyDataArray;
            if(armyDataArrays==null){
                armyDataArrays=new Array<>();
            }else{
                armyDataArrays.clear();
            }
            for(ArmyActor a:armyActorList){
                if(a.armyData.getArmyType()==6){  continue;}
                if(a.targetState==3){
                    a.armyData.setTargetRegion(selectedTargetRegion);
                    //a.resetTarget();
                }
            }
        }
        restorePlayerArmyActorState();
        getMainGame().sMapScreen.defaultWindow.hidButton(13);
        resetDefaultState();
    }

    public Fb2Smap.StrategicData getSelectLegioStrategicnData() {
        if(gameAct.getBtl().strategicDatas.size>0){
            return  getsMapDAO().strategicDatas.get(selectedRLegionIndex);
        }
        return null;
    }

    public Fb2Smap.GeneralData getSelectGeneralData() {
        if(selectedArmy!=null){
            return selectedArmy.getGeneralData();
        }else if(selectedAir!=null){
            return selectedAir.getGeneralData();
        }
        return null;
    }

    public Fb2Smap.FortData getSelectFortData() {
        return gameAct.getBtl().getFortDataByHexagon(coord.getId());
    }

    public Fb2Smap.ForeignData getSelectForeignData() {
        return gameAct.getBtl().getForeignData(selectedRLegionIndex);
    }


    //粘贴兵种
    public void pasteArmy(int hexagon) {
        Fb2Smap.ArmyData a=gameAct.getBtl().getArmyDataByHexagon(hexagon);
        if(copyArmy==null){
            if(a!=null){//抹除该单位
                a.armyDeath(true);
            }
        }else {
            //修改该单位
            gameAct.getBtl().pastArmy(copyArmy,hexagon);
        }
    }

    public Fb2Smap.ArmyData getSelectedArmy() {
         selectedArmy = getsMapDAO().getArmyDataByHexagon(coord.id);
        return selectedArmy;
    }

    public int getPlayMode() {
        return getsMapDAO().masterData.getPlayerMode();
    }

    public int getBtlType() {
        return getsMapDAO().masterData.getBtlType();
    }


    public class SMapPublicRescource {

        //建筑用的标志
        public  TextureRegionDAO airRegionDAO ;
        public  TextureRegionDAO nulRegionDAO ;
        public  TextureRegionDAO energyRegionDAO ;
        public TextureRegionDAO  moraleRegionDAO ;
        public TextureRegionDAO smallBgArrowRegionDAO ;
        public TextureRegionDAO preRailwayRegionDAO ;
        public  TextureRegionDAO spiritNoneDAO ;
        //兵种底牌
        public TextureRegionDAO armyBottomRegionDAO;
        public XmlReader.Element carrierE;
        public TextureRegionDAO scBorderRegionDAO;
        public TextureRegionDAO markUpdLvRegionDAO;

        public TextureRegion sc_generalRegion;
        public TextureRegion sc_attributeBorderRegion;
        public IntArray hexagons;
        public Array<Fb2Map.MapHexagon> tileMapHexagons;
        public Array<Fb2Map.MapHexagon> seaMapHexagons;
        public Array<Fb2Map.MapHexagon> allMapHexagons;
        public Array<Fb2Smap.BuildData> buildDatas;
        // public Array<Fb2Smap.ArmyData> armyDatas;
        public Array<Fb2Smap.FacilityData> facilityDatas;
        public Array<Fb2Smap.FortData> fortDatas;
        public IntArray legionDatas;

        /*
            //drawType 2: buildDrawState0 绘制国旗 buildDrawState1 绘制血量  buildDrawState2 绘制建筑标志

            //drawType 1:buildDrawState0 绘制建筑 armyDrawData0绘制普通步兵 armyDrawData1绘制将军

            //drawType 0:buildDrawState0 绘制建筑 buildDrawState1绘制血条国旗

         */
        public boolean buildDrawState0,buildDrawState1,buildDrawState2, armyDrawState0, armyDrawState1,facilityDrawState,fortDrawState;




        public float markRefx;
        public float markRefy;
        public float generalRefx;
        public float generalRefy;
        //ublic float generalArrowRefy;


        //public TextureRegionDAO sgBgRegionDAO;
        //public TextureRegionDAO sgBorderRegionDAO;
        //public TextureRegionDAO sgBorderArrowRegionDAO;

        public int armyActorState;

        public int buildState;

        //0 绘制兵模 1 绘制标志 2 绘制国旗 3势力 不绘制
        public int drawType;

        public TextureRegionDAO hexagonDAO;
        public TextureRegionDAO hexagonDAO1;
        public int clickId;
        // public TextureRegionDAO regionStrategyDAO;

        public TextureRegionDAO countryBorderDAO11;
        public TextureRegionDAO countryBorderDAO12;
        public TextureRegionDAO countryBorderDAO13;
        public TextureRegionDAO countryBorderDAO14;
        public TextureRegionDAO countryBorderDAO15;
        public TextureRegionDAO countryBorderDAO16;

        public int techNumWidthMax;
        public int cardWidth;
        public int cardHeight;
        public int lvPentagramWidth;
        public int armyHpWidth;

        public SMapPublicRescource(){
            if(gameAct.getBtl().getAge()>1){//二战运输船
                carrierE=getMainGame().gameConfig.getDEF_MODEL().getElementById("1400_2");
            }else if(gameAct.getBtl().getAge()>0||getsMapDAO().getNowYear()>=getMainGame().resGameConfig.navyModelChangeYear){//一战运输船
                carrierE=getMainGame().gameConfig.getDEF_MODEL().getElementById("1400_1");
            }else {//近代运输船
                carrierE=getMainGame().gameConfig.getDEF_MODEL().getElementById("1401_0");
            }
            spiritNoneDAO= getMainGame().getImgLists().getTextureByName("spirit_none");
            airRegionDAO = getMainGame().getImgLists().getTextureByName("markB_air");
            nulRegionDAO = getMainGame().getImgLists().getTextureByName("markB_nul");
            energyRegionDAO = getMainGame().getImgLists().getTextureByName("markR_energy");
            armyBottomRegionDAO =getMainGame().getImgLists().getTextureByName("armyBottom");

            preRailwayRegionDAO=getMainGame().getImgLists().getTextureByName("10_0");
            markUpdLvRegionDAO=getMainGame().getImgLists().getTextureByName("mark_updlv");
            markRefx=-armyBottomRegionDAO.getRefx()+15;
            markRefy=-armyBottomRegionDAO.getRefy()+18;


            scBorderRegionDAO=getMainGame().getImgLists().getTextureByName("sc_border");
            sc_generalRegion=getMainGame().getImgLists().getTextureByName("sc_general").getTextureRegion();

            sc_attributeBorderRegion=getMainGame().getImgLists().getTextureByName("sc_attributeBorder").getTextureRegion();



            //sgBgRegionDAO = getMainGame().getImgLists().getTextureByName("sg_bg");
            //sgBorderRegionDAO = getMainGame().getImgLists().getTextureByName("sg_border");
            //sgBorderArrowRegionDAO = getMainGame().getImgLists().getTextureByName("sg_borderArrow");
            moraleRegionDAO=getMainGame().getImgLists().getTextureByName("morale_bar");
            hexagonDAO=getMainGame().getImgLists().getTextureByName("hexagon");
            hexagonDAO1=getMainGame().getImgLists().getTextureByName("hexagon1");

            smallBgArrowRegionDAO=getMainGame().getImgLists().getTextureByName("sg_arrow");

            countryBorderDAO11= getMainGame().getImgLists().getTextureByName("countryExternalBorder_01");
            countryBorderDAO12= getMainGame().getImgLists().getTextureByName("countryExternalBorder_02");
            countryBorderDAO13= getMainGame().getImgLists().getTextureByName("countryExternalBorder_03");
            countryBorderDAO14= getMainGame().getImgLists().getTextureByName("countryExternalBorder_04");
            countryBorderDAO15= getMainGame().getImgLists().getTextureByName("countryExternalBorder_05");
            countryBorderDAO16= getMainGame().getImgLists().getTextureByName("countryExternalBorder_06");


            techNumWidthMax= getMainGame().getImgLists().getTextureByName("tech_num_max").getTextureRegion().getRegionWidth();
            cardWidth = (int) (getMainGame().getImgLists().getTextureByName("card_border").getTextureRegion().getRegionWidth());
            cardHeight = (int) (getMainGame().getImgLists().getTextureByName("card_border").getTextureRegion().getRegionHeight());
            lvPentagramWidth=getMainGame().getImgLists().getTextureByName("icon_lvpentagram").getTextureRegion().getRegionWidth();
            armyHpWidth=getMainGame().getImgLists().getTextureByName("army_hp_g").getTextureRegion().getRegionWidth();

            if(cam!=null){
                updByCam(cam.getZoom());
            }else {
                updByCam(1.0f);
            }
        }

        public void updByCam(float zoom){
            //drawType
            //0 绘制兵模
            //1 绘制标志
            //2 绘制地块兵力
            //3 绘制城市,国家名
            if(gameAct.getBtl().masterData.getPlayerMode()!=2&&zoom< ResDefaultConfig.Map.MARK_ZOOM){
                drawType =0;
            }else if(zoom< ResDefaultConfig.Map.UNIT_ZOOM){
                drawType =1;
            }else if(zoom<=ResDefaultConfig.Map.MAX_ZOOM){
                drawType =2;
            }else {
                drawType =3;
            }
            if(clickImageActor!=null){
                if(drawType>0){
                    clickImageActor.setVisible(false);
                }else {
                    clickImageActor.setVisible(true);
                }
            }
            //  Gdx.app.log("updByCam:",drawType+":"+zoom);
        }

        //设置兵种绘制图形类型
        public void setArmyActorState(int v){
            armyActorState=v;
        }

        public float getAlphaFlash(){
            return alphaFlash;
        }

        public boolean needLucency(int hexagon) {
            if(clickId!=-1&&clickId+getMapW()==hexagon){
                return true;
            }
            return false;
        }

        public float getZoomChange() {
            return zoomChange;
        }

        public boolean isEditMode(boolean ifOnlyNeutral) {
            return gameAct.getBtl().isEditMode(ifOnlyNeutral);
        }

        public Fb2Smap getSMapDAO() {
            return gameAct.getBtl();
        }

        public Array<ArmyActor> getArmyActorList() {
            return armyActorList;
        }
    }

    public boolean isEditMode(boolean ifOnlyNeutral) {
        return gameAct.getBtl().isEditMode(ifOnlyNeutral);
    }



    //撤回单位移动
    public void withdrawUnitMove() {
        Fb2Smap.ArmyData a=gameAct.getBtl().getArmyDataByHexagon(coord.id);
        if(gameAct.getBtl().getLastUnitHexagon()!=-1&&a!=null&&a.playerCanCommand()&&a.getIfMove()!=0&&a.armyActor!=null){
            if(a.getArmyType()!=1){
                a.getLegionData().addOil(2);
            }
            a.armyMove(gameAct.getBtl().getLastUnitHexagon());
            a.setArmyRound(0);
            a.setIfMove(0);
            a.armyActor.update();
        }
        gameAct.getBtl().resetLastUnitHexagon();
        getMainGame().sMapScreen.hidUnitBackButton();
    }


    public void drawGame(Batch batch){
        batch.setColor(Color.WHITE);
        //绘制兵模
        //drawType 0 兵模 设施模 建模 防御模

        if(cam.rescource.drawType==0){
            //绘制底图  build fort faility

            if(getMainGame().gameConfig.ifEffect){
                batch.begin();
                if(cam.rescource.buildDrawState0){//绘制build底图
                    for(int i=cam.rescource.buildDatas.size-1;i>=0;i--){
                        Fb2Smap.BuildData b=cam.rescource.buildDatas.get(i);
                        if(batch==null||b==null||b.getBuildActor()==null){
                            break;
                        }
                        b.getBuildActor().drawBuild(batch,0);
                    }
                }
                if(cam.rescource.fortDrawState){//绘制fort底图
                    for(int i=cam.rescource.fortDatas.size-1;i>=0;i--){
                        Fb2Smap.FortData f=cam.rescource.fortDatas.get(i);
                        if(batch==null||f==null||f.fortActor==null){
                            break;
                        }
                        f.fortActor.drawFort(batch);
                    }
                }
                if(cam.rescource.facilityDrawState){//绘制faility底图
                    for(int i=cam.rescource.facilityDatas.size-1;i>=0;i--){
                        Fb2Smap.FacilityData f=cam.rescource.facilityDatas.get(i);
                        if(batch==null||f==null||f.facilityActor==null){
                            break;
                        }
                        f.facilityActor.drawFacility(batch);
                    }
                }
                //兵种底图 army
                if(cam.rescource.armyDrawState0){
                    for(int i=armyActorList.size-1;i>=0;i--){
                        ArmyActor armyActor=armyActorList.get(i);
                        if(armyActor==null){
                            armyActorList.removeIndex(i);
                            continue;
                        }
                        if(batch==null||!armyActor.ifDraw()){continue;}
                        armyActor.drawArmyModel(batch,0);
                    }
                }
                try {
                    batch.end();
                } catch (Exception e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace();
                    }
                }
                batch.begin();
                //build 国旗  army 国旗
                if(cam.rescource.buildDrawState0){//绘制build底图
                    for(int i=cam.rescource.buildDatas.size-1;i>=0;i--){
                        Fb2Smap.BuildData b=cam.rescource.buildDatas.get(i);
                        if(batch==null||b==null||b.getBuildActor()==null){
                            break;
                        }
                        b.getBuildActor().drawBuild(batch,1);
                    }
                }
                if(cam.rescource.armyDrawState0){
                    for(int i=armyActorList.size-1;i>=0;i--){
                        ArmyActor armyActor=armyActorList.get(i);
                        if(armyActor==null){
                            armyActorList.removeIndex(i);
                            continue;
                        }
                        if(batch==null||!armyActor.ifDraw()){continue;}
                        armyActor.drawArmyModel(batch,1);
                    }
                }
                try {
                    batch.end();
                } catch (Exception e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace();
                    }
                }
                batch.begin();
                //兵种等级等杂项 army
                if(cam.rescource.buildDrawState0){
                    for(int i=cam.rescource.buildDatas.size-1;i>=0;i--){
                        Fb2Smap.BuildData b=cam.rescource.buildDatas.get(i);
                        if(batch==null||b==null||b.getBuildActor()==null){
                            break;
                        }
                        b.getBuildActor().drawBuild(batch,2);
                    }
                }
                try {
                    batch.end();
                } catch (Exception e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace();
                    }
                }
                batch.begin();
                if(cam.rescource.armyDrawState0){
                    for(int i=armyActorList.size-1;i>=0;i--){
                        ArmyActor armyActor=armyActorList.get(i);
                        if(armyActor==null){
                            armyActorList.removeIndex(i);
                            continue;
                        }
                        if(batch==null||!armyActor.ifDraw()){continue;}
                        armyActor.drawArmyModel(batch,2);
                    }
                }
                try {
                    batch.end();
                } catch (Exception e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace();
                    }
                }
                batch.begin();
                //army 将军
                if(cam.rescource.armyDrawState1){
                    for(int i=armyActorList.size-1;i>=0;i--){
                        ArmyActor armyActor=armyActorList.get(i);
                        if(armyActor==null){
                            armyActorList.removeIndex(i);
                            continue;
                        }
                        if(batch==null||!armyActor.ifDraw()){continue;}
                        armyActor.drawArmyModel(batch,3);
                    }
                }
                //错误记录
                try {
                    batch.end();
                } catch (Exception e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace();
                    }
                }
            }else{//简化模式
                batch.begin();
                if(cam.rescource.buildDrawState0){//绘制build底图
                    for(int i=cam.rescource.buildDatas.size-1;i>=0;i--){
                        Fb2Smap.BuildData b=cam.rescource.buildDatas.get(i);
                        if(batch==null||b==null||b.getBuildActor()==null){
                            break;
                        }
                        b.getBuildActor().drawBuildMark(batch);
                    }
                }
                if(cam.rescource.fortDrawState){//绘制fort底图
                    for(int i=cam.rescource.fortDatas.size-1;i>=0;i--){
                        Fb2Smap.FortData f=cam.rescource.fortDatas.get(i);
                        if(batch==null||f==null||f.fortActor==null){
                            break;
                        }
                        f.fortActor.drawFort(batch);
                    }
                }
                if(cam.rescource.facilityDrawState){//绘制faility底图
                    for(int i=cam.rescource.facilityDatas.size-1;i>=0;i--){
                        Fb2Smap.FacilityData f=cam.rescource.facilityDatas.get(i);
                        if(batch==null||f==null||f.facilityActor==null){
                            break;
                        }
                        f.facilityActor.drawFacility(batch);
                    }
                }
                try {
                    batch.end();
                } catch (Exception e) {

                }
                batch.begin();
                //兵种底图 army
                if(cam.rescource.armyDrawState0){
                    for(int i=armyActorList.size-1;i>=0;i--){
                        ArmyActor armyActor=armyActorList.get(i);
                        if(armyActor==null){
                            armyActorList.removeIndex(i);
                            continue;
                        }
                        if(batch==null||!armyActor.ifDraw()){continue;}
                        armyActor.batchSoldierCard(batch);
                    }
                }
                try {
                    batch.end();
                } catch (Exception e) {

                }
            }
        }else if(cam.rescource.drawType==1){//1 建模 设施模 兵标模
            batch.begin();
            if(cam.rescource.buildDrawState0){
                for(int i=cam.rescource.buildDatas.size-1;i>=0;i--){
                    if (i >= cam.rescource.buildDatas.size) {
                        break;
                    }
                    Fb2Smap.BuildData b = cam.rescource.buildDatas.get(i);
                    if (b == null) {
                        break;
                    }
                    if(batch==null||b==null||b.getBuildActor()==null){
                        break;
                    }
                    b.getBuildActor().drawBuildMark(batch);
                }
            }

            if(cam.rescource.armyDrawState0){//绘制普通兵种
                for(int i=armyActorList.size-1;i>=0;i--){
                    ArmyActor armyActor=armyActorList.get(i);
                    if(armyActor==null){
                        armyActorList.removeIndex(i);
                        continue;
                    }
                    if(batch==null||!armyActor.ifDraw()||armyActor.smallGeneral!=null){continue;}
                    armyActor.drawArmyMark(batch);
                }
            }

            batch.end();
            batch.begin();
            if(cam.rescource.armyDrawState1){//绘制将军
                for(int j=0;j<2;j++){
                    for(int i=armyActorList.size-1;i>=0;i--){
                        ArmyActor armyActor=armyActorList.get(i);
                        if(armyActor==null){
                            armyActorList.removeIndex(i);
                           continue;
                        }
                        if(batch==null||!armyActor.ifDraw()||armyActor.smallGeneral==null){continue;}
                        armyActor.drawGeneralMark(batch,j);
                    }
                }
            }

            batch.end();
        }else if(cam.rescource.drawType==2){//2 建模

            batch.begin();
            for(int j=0;j<3;j++) {
                if(j==0&&!cam.rescource.buildDrawState0){
                    continue;
                }
                if(j==1&&!cam.rescource.buildDrawState1){
                    continue;
                }
                if(j==2&&!cam.rescource.buildDrawState2){
                    continue;
                }
                for(int i=cam.rescource.buildDatas.size-1;i>=0;i--){
                    try {
                        if (i >= cam.rescource.buildDatas.size) {
                            break;
                        }
                        Fb2Smap.BuildData b = cam.rescource.buildDatas.get(i);
                        if (b == null) {
                            break;
                        }
                        if(batch==null||b==null||b.getBuildActor()==null){
                            break;
                        }
                        b.getBuildActor().drawMilitaryFlag(batch,j);
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            try {
                batch.end();
            } catch (Exception e) {

            }
        }
    }

    public void updAllArmyActorUpdMark(){
        for(int i=0;i<armyActorList.size;i++){
            ArmyActor armyActor=armyActorList.get(i);
            if(armyActor!=null){
                armyActor.updDrawInfo();
            }
        }
    }
}
