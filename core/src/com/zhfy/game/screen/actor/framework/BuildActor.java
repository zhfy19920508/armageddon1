    package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.screen.stage.SMapGameStage;

public class BuildActor extends BaseActor implements Pool.Poolable {

    /*private  int airRegionRefY;
    private  int airRegionRefX;
    private  int nulRegionRefY;
    private  int nulRegionRefX;
    private  int energyRegionRefY;
    private  int energyRegionRefX;*/
    //private TextureRegionDAO airRegionDAO;
    //private TextureRegionDAO nulRegionDAO;
    //private TextureRegionDAO energyRegionDAO;

    public TextureRegion mainFlag;
    private TextureRegion flag;
    private float flagW;
    private float flagH;
    private float hpH;
    private float hpRefy;
    public float regionForcesRateF;
    public TextureRegion buildHpRegion;
    public TextureRegion forcesHpRegion;
    public float forcesHpRegionRefy;

    private TextureRegionDAO buildRegionDAO;
    /*private int buildRegionRefX;
    private int buildRegionRefY;*/

    public Fb2Smap.BuildData buildData;

    //private float sourceX;//初始位置
    //private float sourceY;//初始位置

    private CamerDAO cam;
    //private String cityName;
    //private Label label;
    private Fb2Map.MapHexagon hexagonData;
    private TextureRegionDAO markRegionDAO;
   // private int drawState; //0城市 1城市血量+城市等级   3城市名

    private int mapW;//格子数,辅助计算坐标


    private boolean ifSee;
    private boolean ifFlash;
    public boolean ifDrawAir;
    public boolean ifDrawNul;
    public boolean ifDrawEnergy;
    private TextureRegion pointRegion;
    private TextureRegionDAO buildStateDAO;
    //private TextureRegion airRegion;
    //private TextureRegion nulRegion;
    //private TextureRegion energyRegion;
    //private boolean ifHaveArmy;
    private SMapGameStage.SMapPublicRescource resource;

    public BuildActor( MainGame game, Fb2Smap.BuildData buildData,  int mapW, float scale, CamerDAO cam, SMapGameStage.SMapPublicRescource resource) {
       // this.region = buildData.getRegionId();
        //this.btl=btl;
        setMainGame(game);
       // this.mapW_px=mapW_px;
      //  this.mapH_px=mapH_px;
        this.mapW=mapW;
        this.cam=cam;
        this.buildData=buildData;
        buildRegionDAO= game.getImgLists().getTextureByName(DefDAO.getImageNameForBuildModel(buildData));
        this.resource=resource;
        /*this.buildRegion =t.getTextureRegion();
        this.buildRegionRefX= (int) (-t.getRefx()*scale);
        this.buildRegionRefY= (int) (-t.getRefy()*scale);*/

        //this.loopState =0;
        setSize(this.buildRegionDAO.getTextureRegion().getRegionWidth(), this.buildRegionDAO.getTextureRegion().getRegionHeight());
        setScale(scale);
       //initPotionById();
        hexagonData=buildData.getHexagonData();
        setX(hexagonData.source_x);
        setY(hexagonData.source_y);
        buildStateDAO=game.getImgLists().getTextureByName("mark_target2");
       update();

        buildData.setBuildActor(this);
        updDrawInfo();
    }

    public  void updPoint(){
        if(buildData.getMasterData().getPlayerMode()!=1){//统帅和军团
            if(buildData.isCapital()){
                // ifFlash=true;
                TextureRegionDAO t1=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForBuildPoint(buildData));
                pointRegion= t1.getTextureRegion();
            }else {
                pointRegion=null;
            }
        }else {
            if(buildData.getHinge()!=0){
                TextureRegionDAO t1=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForBuildPoint(buildData.getFD()));
                pointRegion= t1.getTextureRegion();
            }else {
                pointRegion=null;
            }
        }
    }





    public void updDrawInfo(){
        Fb2Smap.ArmyData a=buildData.getRegionUnit();
        ifDrawAir=/*(buildData.isEditMode(true)||buildData.isPlayer())&&*/buildData.getAirCount()>0;
        ifDrawNul=/*(buildData.isEditMode(true)||buildData.isPlayer())&&*/buildData.getNuclearCount()>0;
        ifDrawEnergy=(buildData.isEditMode(true)||buildData.isPlayer())&&buildData.ifEnergyOverload();
        if(a!=null&&a.armyActor!=null){
            a.armyActor.updDrawInfo();
        }
        if(buildData.isPlayer()&&buildData.getBuildType()!=2&&buildData.getBuildRound()==0){
            ifFlash=true;
        }else {
            ifFlash=false;
        }
    }

    public BuildActor(){}

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (true||resource.buildState==0||buildRegionDAO == null ||buildRegionDAO.getTextureRegion()==null|| !cam.ifDrawHexagon(hexagonData.getHexagonIndex())||!isVisible()/*||cam.getCamera().zoom>0.85*/) {
            return;
        }
    }
    /*public void setLoopState(int loopState) {
        this.loopState = loopState;
    }*/



    public int getMapW() {
        return mapW;
    }

    public void setMapW(int mapW) {
        this.mapW = mapW;
    }

    @Override
    public void reset() {
            super.reset();
    }

    public boolean getIfFlash() {
        return ifFlash;
    }

    public void update(){
        Fb2Smap.LegionData legionData=buildData.getLegionData();
        int age=buildData.getAge();
        if(buildData.ifSea()){
            markRegionDAO =getMainGame().getImgLists().getTextureByName("city_island");
        }else if(buildData.isCapital()||buildData.getHinge()!=0){
            markRegionDAO =getMainGame().getImgLists().getTextureByName("mark_capital");
        }else if(buildData.canBuildWonder()){
            markRegionDAO =getMainGame().getImgLists().getTextureByName("mark_wonder");
        }else  if(buildData.getBuildName()!=0&&buildData.getCityLvNow()>(3+age)){
            markRegionDAO =getMainGame().getImgLists().getTextureByName("mark_largeCity");
        }else if(buildData.getCityLvNow()<1+age){
            markRegionDAO =getMainGame().getImgLists().getTextureByName("mark_smallCity");
        }else{
            markRegionDAO =getMainGame().getImgLists().getTextureByName("mark_mediumCity");
        }

        buildRegionDAO= getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForBuildModel(buildData));
        setSize(this.buildRegionDAO.getTextureRegion().getRegionWidth(), this.buildRegionDAO.getTextureRegion().getRegionHeight());
        flag=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(legionData.getCountryId())).getTextureRegion();
        flagW=flag.getRegionWidth()*0.4f;flagH=flag.getRegionHeight()*0.4f;


       TextureRegionDAO armyHpRegionDAO=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForHp(getMainGame().getSMapDAO(),buildData.getLegionIndex()));

        float hpScale=buildData.getCityHpNow()*1f/buildData.getCityHpMax();
        int hw= armyHpRegionDAO.getTextureRegion().getRegionWidth();
        int hy= (int) (hw-hw*hpScale);
        buildHpRegion=armyHpRegionDAO.getNewRegion(0,0 ,hw,hw-hy);
        buildHpRegion.setRegionHeight((int) (armyHpRegionDAO.getH()*hpScale));
        hpH=armyHpRegionDAO.getH()*hpScale*0.4f;
        hpRefy=buildHpRegion.getRegionHeight()-armyHpRegionDAO.getH();


        updDrawInfo();

       /*if(buildData.getRegionId()==9061){
       boolean a1=    buildData.getHexagonData().getIfFog()==1;
           boolean a2=buildData.ifHaveAllyUnit(buildData.getPlayerLegionIndex());
           boolean a3=(buildData.isPlayerAlly()&&buildData.getBuildStatus()!=0);
           int s=0;
        }*/

        //区域兵力更新
        ifSee=buildData.getHexagonData().getIfFog()==1||buildData.ifHaveAllyUnit(buildData.getPlayerLegionIndex(),true)||(buildData.isPlayerAlly()&&buildData.getBuildStatus()!=0);  //0不可见 1可见
        if(ifSee){
            Fb2Smap.LegionData mL=buildData.getMainLegionData();
            if(mL==null){
                mainFlag=null;
                regionForcesRateF=0;
            }else{
                mainFlag=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(mL.getCountryId())).getTextureRegion();
                regionForcesRateF=buildData.getRegionForcesRateF(mL);
                armyHpRegionDAO=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForHp(getMainGame().getSMapDAO(),mL.getLegionIndex()));
            }
            hw= armyHpRegionDAO.getTextureRegion().getRegionWidth();
            if(regionForcesRateF==1.0f){
                forcesHpRegion=armyHpRegionDAO.getTextureRegion();
                forcesHpRegionRefy=0;
            }else{
                hy= (int) (hw*regionForcesRateF);
                forcesHpRegion=armyHpRegionDAO.getNewRegion(0,0,hw, hy);
                forcesHpRegionRefy=forcesHpRegion.getRegionHeight()-armyHpRegionDAO.getH();
            }
        }else if(buildData.getPlayerLegionIndex()==0&&!resource.isEditMode(false)){
          if(buildData.getLegionData().ifEnable()&&buildData.isCapital()){
              mainFlag=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(legionData.getCountryId())).getTextureRegion();
          }else{
              mainFlag=null;
          }
            regionForcesRateF=0;
            forcesHpRegionRefy=0;
        }else{
            mainFlag=null;
            regionForcesRateF=0;
            forcesHpRegionRefy=0;
        }

        updPoint();


    }

    public void drawStrike(String strikeName, float atTime) {
        buildData.getEffectStage().effectDAO.darwEffect(strikeName,buildData.getRegionId(),0,0,0,atTime,false);
    }
    //绘制军力
    //0 绘制国旗
    //1 绘制军力
    public void drawMilitaryFlag(Batch batch,int count) {
        if(mainFlag!=null){
            if(count==0){
                if(cam.loopState==0||cam.loopState==1) {
                    batch.draw(
                            mainFlag,
                            getX()-26, getY()-23,
                            //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                            getOriginX()+mainFlag.getRegionWidth()/2 ,getOriginY()+ mainFlag.getRegionHeight() /2,
                            mainFlag.getRegionWidth() ,mainFlag.getRegionHeight() ,
                            2.3f,2.3f,
                            getRotation()
                    );
                }
                if(cam.loopState==1||cam.loopState==2){
                    batch.draw(
                            mainFlag,
                            cam.getMapW_px()+getX()-26, getY()-23,
                            // getX()+getMapW_px()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                            getOriginX()+mainFlag.getRegionWidth()/2 ,getOriginY()+ mainFlag.getRegionHeight() /2,
                            mainFlag.getRegionWidth() ,mainFlag.getRegionHeight() ,
                            // cam.getZoom(), cam.getZoom(),
                            2.3f,2.3f,
                            getRotation()
                    );
                }
            }else if(count==1&&regionForcesRateF>0){
                if(cam.loopState==0||cam.loopState==1) {
                    batch.draw(
                            forcesHpRegion,
                            //  getX()-15.5f, getY()-32.5f+ hpRefy*getScaleY(),//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                            // 52.5f, 55f,//手工测出来的.....
                            //getX()+3f,getY()-15.5f+ forcesHpRegionRefy*cam.getZoom(),
                            //getX() +  getMainGame().sMapScreen.tempActor.getX(), getY() +getMainGame().sMapScreen.tempActor.getY() +   forcesHpRegionRefy *cam.getZoom()+9f,
                            getX() -40, getY() -43+   forcesHpRegionRefy *2.3f,
                            30, 31,
                            //    getMainGame().sMapScreen.tempActor.getX(),getMainGame().sMapScreen.tempActor.getY(),
                            forcesHpRegion.getRegionWidth(),  forcesHpRegion.getRegionHeight() ,
                            2.3f,2.3f,
                            //  cam.getZoom(), cam.getZoom(),
                            180
                    );
                }
                if(cam.loopState==2||cam.loopState==1) {
                    batch.draw(
                            forcesHpRegion,
                            cam.getMapW_px()+ getX() -40, getY() -43+   forcesHpRegionRefy*2.3f,
                            30, 31,
                            //    getMainGame().sMapScreen.tempActor.getX(),getMainGame().sMapScreen.tempActor.getY(),
                            forcesHpRegion.getRegionWidth(),  forcesHpRegion.getRegionHeight() ,
                            2.3f,2.3f,
                            //   cam.getZoom(), cam.getZoom(),
                            180
                    );
                }
            }
        }else if(count==2&&!buildData.ifSea()){
            if(cam.loopState==0||cam.loopState==1) {
                batch.draw(
                        markRegionDAO.getTextureRegion(),
                        getX()-markRegionDAO.getRefx()*2+-1, getY()-markRegionDAO.getRefy()*2+-1,
                        //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                        getOriginX() ,getOriginY(),
                        markRegionDAO.getW() , markRegionDAO.getH() ,
                        2f, 2f,
                        getRotation()
                );
            }
            if(cam.loopState==1||cam.loopState==2){
                batch.draw(
                        markRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX()-markRegionDAO.getRefx()*2+-1, getY()-markRegionDAO.getRefy()*2+-1,
                        //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                        getOriginX() ,getOriginY(),
                        markRegionDAO.getW() , markRegionDAO.getH() ,
                        2f, 2f,
                        getRotation()
                );
            }
        }
    }




    public void drawBuildMark(Batch batch) {
        //float refX=getMainGame().sMapScreen.tempActor.getX();
        //float refY=getMainGame().sMapScreen.tempActor.getY();
        if(cam.loopState==0||cam.loopState==1) {
            batch.draw(
                    markRegionDAO.getTextureRegion(),
                    getX()-markRegionDAO.getRefx()*2+-1, getY()-markRegionDAO.getRefy()*2+-1,
                    //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                    getOriginX(),getOriginY(),
                    markRegionDAO.getW() , markRegionDAO.getH() ,
                    2f, 2f,
                    getRotation()
            );
        }
        if(cam.loopState==1||cam.loopState==2){
            batch.draw(
                    markRegionDAO.getTextureRegion(),
                    cam.getMapW_px()+getX()-markRegionDAO.getRefx()*2+-1, getY()-markRegionDAO.getRefy()*2+-1,
                    //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                    getOriginX() ,getOriginY(),
                    markRegionDAO.getW() , markRegionDAO.getH() ,
                    2f, 2f,
                    getRotation()
            );
        }
        //绘制飞机标示
        if(ifDrawAir){
            if(cam.loopState==0||cam.loopState==1) {
                batch.draw(
                        resource.airRegionDAO.getTextureRegion(),
                        getX()+20, getY()+-35,
                        resource.airRegionDAO.getRefx(), resource.airRegionDAO.getRefy(),
                        resource.airRegionDAO.getTextureRegion().getRegionWidth(),resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                        getRotation()
                );
            }
            if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                batch.draw(
                        resource.airRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX()+20, getY()+-35,
                        resource.airRegionDAO.getRefx(), resource.airRegionDAO.getRefy(),
                        resource.airRegionDAO.getTextureRegion().getRegionWidth(),resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                        getRotation()
                );
            }
        }

        if(ifDrawNul){////getX() + -28 - nulRegionDAO.getRefx() + 15 - 25 , getY() + -29 - nulRegionDAO.getRefy() - 10 + 18 + 16,
            if(cam.loopState==0||cam.loopState==1) {
                batch.draw(
                        resource.nulRegionDAO.getTextureRegion(),
                        getX()-36, getY()+-35,
                        resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                        resource.nulRegionDAO.getTextureRegion().getRegionWidth(),resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                        getRotation()
                );
            }
            if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                batch.draw(
                        resource.nulRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX()-36, getY()+-35,
                        resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                        resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                        getRotation()
                );
            }
        }
        if(ifDrawEnergy) {
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        resource.energyRegionDAO.getTextureRegion(),
                        getX() + -4, getY() + -25,
                        resource.energyRegionDAO.getRefx(), resource.energyRegionDAO.getRefy(),
                        resource.energyRegionDAO.getTextureRegion().getRegionWidth(), resource.energyRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
            if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                batch.draw(
                        resource.energyRegionDAO.getTextureRegion(),
                        cam.getMapW_px() +getX() + -4, getY() + -25,
                        getOriginX(), getOriginY(),
                        resource.energyRegionDAO.getTextureRegion().getRegionWidth(), resource.energyRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
        }
    }


//count 0底图 1国旗 2hp
    public void drawBuild(Batch batch, int count) {
        float alphaFlash=resource.getAlphaFlash()*0.7f;
                /*if(ifFlash){
                    batch.setColor(1.0f,1.0f,1.0f,((SMapGameStage) getStage()).alphaFlash);
                }*/
        float zoomChange= resource.getZoomChange()*1.2f;
        batch.setColor(Color.WHITE);
       if(count==0){
            if(buildData.getBuildPolicy()!=0){
                Color c=DefDAO.getColorForBuildPolicy(buildData.getBuildPolicy());
                batch.setColor(c.r,c.g,c.b,alphaFlash/2);
            }else{
                batch.setColor(1,1,1,alphaFlash/2);
            }


            // 结合演员的属性绘制表示演员的纹理区域

            if(ifFlash){
                if(cam.loopState==0||cam.loopState==1) {
                    batch.draw(
                            buildStateDAO.getTextureRegion(),
                            getX()+(-44)* ResDefaultConfig.Map.MAP_SCALE+(1-zoomChange)* buildStateDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE  ,
                            getY()+(-36)* ResDefaultConfig.Map.MAP_SCALE+(1-zoomChange)* buildStateDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE   ,
                            getOriginX(), getOriginY(),
                            buildStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, buildStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange, zoomChange,
                            getRotation()
                    );
                }
                if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                    batch.draw(
                            buildStateDAO.getTextureRegion(),
                            cam.getMapW_px()+getX()+(-44)* ResDefaultConfig.Map.MAP_SCALE+(1-zoomChange)* buildStateDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE  ,
                            getY()+(-36)* ResDefaultConfig.Map.MAP_SCALE+(1-zoomChange)* buildStateDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE   ,
                            getOriginX(), getOriginY(),
                            buildStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, buildStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange, zoomChange,
                            getRotation()
                    );
                }
            }

            if(buildData.isPlayer()&&buildData.getBuildRound()!=0){
                batch.setColor(Color.GRAY);
            }else {
                batch.setColor(Color.WHITE);
            }

            if(cam.loopState==0||cam.loopState==1) {
                batch.draw(
                        buildRegionDAO.getTextureRegion(),
                        getX() - buildRegionDAO.getRefx() + -3, getY() - buildRegionDAO.getRefy() ,
                        getOriginX(), getOriginY(),
                        getWidth(), getHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if(cam.loopState==1||cam.loopState==2) {
                batch.draw(
                        buildRegionDAO.getTextureRegion(),
                        cam.getMapW_px() + getX() - buildRegionDAO.getRefx() + -3, getY() - buildRegionDAO.getRefy() ,
                        getOriginX(), getOriginY(),
                        getWidth(), getHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            batch.setColor(Color.WHITE);
            if(pointRegion!=null){
                batch.setColor(1,1,1,alphaFlash);
                if(cam.loopState==0||cam.loopState==1) {
                    batch.draw(
                            pointRegion,
                            //getX()-pointRegion.getRegionWidth()/2+getMainGame().sMapScreen.tempActor.getX(),getY()-pointRegion.getRegionWidth()/2+getMainGame().sMapScreen.tempActor.getY(),
                            getX()-pointRegion.getRegionWidth()/2+-33, getY()-pointRegion.getRegionWidth()/2-14,
                            getOriginX(), getOriginY(),
                            getWidth() , getHeight() ,
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                    batch.draw(
                            pointRegion,
                            cam.getMapW_px()+getX()-pointRegion.getRegionWidth()/2+-33, getY()-pointRegion.getRegionWidth()/2-14,
                            getOriginX(), getOriginY(),
                            getWidth(), getHeight(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                batch.setColor(Color.WHITE);
            }


        }else if(count==1){//bt_flags
            if(resource.drawType<2){
                if(hexagonData.armyData==null&&buildData.getBuildType()!=2&&ifSee){
                    if(cam.loopState==0||cam.loopState==1) {
                        batch.draw(
                                flag,
                                getX()+-9, getY()-33,
                                //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                                getOriginX(), getOriginY(),
                                flagW ,flagH ,
                                getScaleX(), getScaleY(),
                                getRotation()
                        );
                    }
                    if(cam.loopState==1||cam.loopState==2){
                        batch.draw(
                                flag,
                                getX()+-9+cam.getMapW_px(), getY()-33,
                                // getX()+getMapW_px()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                                getOriginX(), getOriginY(),
                                flagW ,flagH ,
                                getScaleX(), getScaleY(),
                                getRotation()
                        );
                    }
                }
            }else{
                if(mainFlag!=null){
                    if(cam.loopState==0||cam.loopState==1) {
                        batch.draw(
                                mainFlag,
                                getX()-24, getY()-15,
                                //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                                getOriginX()+mainFlag.getRegionWidth()/2 ,getOriginY()+ mainFlag.getRegionHeight() /2,
                                mainFlag.getRegionWidth() ,mainFlag.getRegionHeight() ,
                                2.3f,2.3f,
                                getRotation()
                        );
                    }
                    if(cam.loopState==1||cam.loopState==2){
                        batch.draw(
                                mainFlag,
                                cam.getMapW_px()+getX()-24, getY()-15,
                                // getX()+getMapW_px()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                                getOriginX()+mainFlag.getRegionWidth()/2 ,getOriginY()+ mainFlag.getRegionHeight() /2,
                                mainFlag.getRegionWidth() ,mainFlag.getRegionHeight() ,
                                // cam.getZoom(), cam.getZoom(),
                                2.3f,2.3f,
                                getRotation()
                        );
                    }
                }
            }

        }else if(count==2){//hp
            if(resource.drawType<2){
                if(hexagonData.armyData==null&&buildData.getBuildType()!=2&&ifSee){
                    if(buildData.getCityHpNow()>0&&!buildData.ifSea()){
                        if(cam.loopState==0||cam.loopState==1) {
                            batch.draw(
                                    buildHpRegion,
                                    getX()-31, getY()-57+ hpRefy*getScaleY()*0.4f,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                                    21, 22,//手工测出来的.....
                                    // game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                                    flagW,  hpH ,
                                    getScaleX(), getScaleY(),
                                    180
                            );
                        }
                        if(cam.loopState==2||cam.loopState==1) {
                            batch.draw(
                                    buildHpRegion,
                                    cam.getMapW_px()+getX()-31, getY()-57+ hpRefy*getScaleY()*0.4f,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                                    21, 22,//手工测出来的.....
                                    // game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                                    flagW,  hpH ,
                                    getScaleX(), getScaleY(),
                                    180
                            );
                        }
                    }
                }

                //绘制飞机标示
                if(ifDrawAir){
                    if(cam.loopState==0||cam.loopState==1) {
                        batch.draw(
                                resource.airRegionDAO.getTextureRegion(),
                                getX()+20, getY()+-35,
                                resource.airRegionDAO.getRefx(), resource.airRegionDAO.getRefy(),
                                resource.airRegionDAO.getTextureRegion().getRegionWidth(),resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                                getRotation()
                        );
                    }
                    if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                        batch.draw(
                                resource.airRegionDAO.getTextureRegion(),
                                cam.getMapW_px()+getX()+20, getY()+-35,
                                resource.airRegionDAO.getRefx(), resource.airRegionDAO.getRefy(),
                                resource.airRegionDAO.getTextureRegion().getRegionWidth(),resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                                getRotation()
                        );
                    }
                }

                if(ifDrawNul){////getX() + -28 - nulRegionDAO.getRefx() + 15 - 25 , getY() + -29 - nulRegionDAO.getRefy() - 10 + 18 + 16,
                    if(cam.loopState==0||cam.loopState==1) {
                        batch.draw(
                                resource.nulRegionDAO.getTextureRegion(),
                                getX()+-36, getY()+-35,
                                resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                                resource.nulRegionDAO.getTextureRegion().getRegionWidth(),resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                                getRotation()
                        );
                    }
                    if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                        batch.draw(
                                resource.nulRegionDAO.getTextureRegion(),
                                cam.getMapW_px()+getX()+-36, getY()+-35,
                                getOriginX(), getOriginY(),
                                resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                                getRotation()
                        );
                    }
                }
                if(ifDrawEnergy){
                    if(cam.loopState==0||cam.loopState==1) {
                        batch.draw(
                                resource.energyRegionDAO.getTextureRegion(),
                                getX()-resource.energyRegionDAO.getRefx()-4, getY()-resource.energyRegionDAO.getRefy()-25,
                                getOriginX(), getOriginY(),
                                resource.energyRegionDAO.getTextureRegion().getRegionWidth(),resource.energyRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                                getRotation()
                        );
                    }
                    if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                        batch.draw(
                                resource.energyRegionDAO.getTextureRegion(),
                                cam.getMapW_px()+getX()-resource.energyRegionDAO.getRefx()-4, getY()-resource.energyRegionDAO.getRefy()-25,
                                getOriginX(), getOriginY(),
                                resource.energyRegionDAO.getTextureRegion().getRegionWidth(),resource.energyRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX()+cam.getZoom(), getScaleY()+cam.getZoom(),
                                getRotation()
                        );
                    }
                }
            }else {
                if(mainFlag!=null){
                    if(regionForcesRateF>0){
                        if(cam.loopState==0||cam.loopState==1) {
                            batch.draw(
                                    forcesHpRegion,
                                    //  getX()-15.5f, getY()-32.5f+ hpRefy*getScaleY(),//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                                    // 52.5f, 55f,//手工测出来的.....
                                    //getX()+3f,getY()-15.5f+ forcesHpRegionRefy*cam.getZoom(),
                                    //getX() +  getMainGame().sMapScreen.tempActor.getX(), getY() +getMainGame().sMapScreen.tempActor.getY() +   forcesHpRegionRefy *cam.getZoom()+9f,
                                    getX() -38, getY() -35+   forcesHpRegionRefy *2.3f,
                                    30, 31,
                                    //    getMainGame().sMapScreen.tempActor.getX(),getMainGame().sMapScreen.tempActor.getY(),
                                    forcesHpRegion.getRegionWidth(),  forcesHpRegion.getRegionHeight() ,
                                    2.3f,2.3f,
                                    //  cam.getZoom(), cam.getZoom(),
                                    180
                            );
                        }
                        if(cam.loopState==2||cam.loopState==1) {
                            batch.draw(
                                    forcesHpRegion,
                                    cam.getMapW_px()+ getX() -38, getY() -35+   forcesHpRegionRefy*2.3f,
                                    30, 31,
                                    //    getMainGame().sMapScreen.tempActor.getX(),getMainGame().sMapScreen.tempActor.getY(),
                                    forcesHpRegion.getRegionWidth(),  forcesHpRegion.getRegionHeight() ,
                                    2.3f,2.3f,
                                    //   cam.getZoom(), cam.getZoom(),
                                    180
                            );
                        }
                    }
                }else {
                    if(!buildData.ifSea()&&buildData.getLegionIndex()!=0){
                        if(cam.loopState==0||cam.loopState==1) {
                            batch.draw(
                                    markRegionDAO.getTextureRegion(),
                                    getX()-markRegionDAO.getRefx()*2+-1, getY()-markRegionDAO.getRefy()*2+-1,
                                    //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                                    getOriginX() ,getOriginY(),
                                    markRegionDAO.getW() , markRegionDAO.getH() ,
                                    2f, 2f,
                                    getRotation()
                            );
                        }
                        if(cam.loopState==1||cam.loopState==2){
                            batch.draw(
                                    markRegionDAO.getTextureRegion(),
                                    cam.getMapW_px()+getX()-markRegionDAO.getRefx()*2+-1, getY()-markRegionDAO.getRefy()*2+-1,
                                    //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                                    getOriginX()+ markRegionDAO.getRefx() ,getOriginY()+ markRegionDAO.getRefy(),
                                    markRegionDAO.getW() , markRegionDAO.getH() ,
                                    2f, 2f,
                                    getRotation()
                            );
                        }
                    }
                }

            }
        }
    }

    public boolean inDrawScope() {
        return cam.ifDrawHexagon(buildData.getHexagonIndex());
    }


    public boolean inScreen() {
        if(cam!=null&&cam.inScreen(buildData.getHexagonIndex())){
            return true;
        }
        return false;
    }
}

