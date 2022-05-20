package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.screen.stage.SMapGameStage;

public class FortActor extends  BaseActor implements Pool.Poolable {


    private MainGame game;
    private TextureRegionDAO fortRegionDAO;
    private Fb2Smap.FortData fortData;
    private Fb2Map.MapHexagon hexagonData;
    private  int fortRefX;

    private CamerDAO cam;

    private int mapW;//格子数,辅助计算坐标

    //private int drawState;//0 底层绘制  1 顶层绘制
    private SMapGameStage.SMapPublicRescource recource;

    public FortActor(MainGame game, Fb2Smap.FortData fortData, int mapW, float scale, CamerDAO cam,SMapGameStage.SMapPublicRescource recource) {
        init(game,fortData,mapW,scale,cam,recource);
    }



    public void init( MainGame game, Fb2Smap.FortData fortData,int mapW, float scale, CamerDAO cam,SMapGameStage.SMapPublicRescource recource) {
        this.recource=recource;

        this.game=game;
        this.mapW=mapW;
        this.cam=cam;
        this.fortData =fortData;
        this.hexagonData=fortData.getHexagonData();
        this.fortData.fortActor=this;
       update();
    }

    /*public void setDrawState(int value,boolean ifDawArmy){
        this.drawState=value;
        if(ifDawArmy){
            game.getSMapDAO().armyActorToDrawFort(fortData.getHexagonIndex(),fortData.getFortId());
        }
    }
*/
    public void update(){
        //fortRegionDAO= game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(fortData.getFortId()));

        if(fortData.getFortId()==5005){
            if(fortData.getHexagonData().getBlockType()==1){
                fortRegionDAO= game.getImgLists().getTextureByName("army_5005_2");
            }else{
                fortRegionDAO= game.getImgLists().getTextureByName("army_5005");
            }
            fortRefX=fortRegionDAO.getRefx();
        }else if(fortData.getFortId()==5004||fortData.getFortId()==5006||fortData.getFortId()==5007){
            Fb2Smap.ArmyData a=fortData.getArmyData();
            if(a==null){
                if(fortData.getLegionIndex()==0){
                    fortRegionDAO= game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(fortData.getFortId()));
                    fortRefX=fortRegionDAO.getRefx();
                }else {
                    fortRegionDAO= game.getImgLists().getFilpRegionDAO(DefDAO.getArmyModeNameByArmyId(fortData.getFortId()));
                    fortRefX= fortRegionDAO.getW()-fortRegionDAO.getRefx();
                }
            }else{
                if(a.getArmyDirection()==0){
                    fortRegionDAO= game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(fortData.getFortId()));
                    fortData.setLegionIndex(0);
                    fortRefX=fortRegionDAO.getRefx();
                }else {
                    fortRegionDAO= game.getImgLists().getFilpRegionDAO(DefDAO.getArmyModeNameByArmyId(fortData.getFortId()));
                    fortData.setLegionIndex(1);

                   // float scale=getScaleX();
                    fortRefX= fortRegionDAO.getW()-fortRegionDAO.getRefx();
                }
            }
        }else{
            fortRegionDAO= game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(fortData.getFortId()));
            fortRefX=fortRegionDAO.getRefx();
        }

       /* this.fortRegion =t.getTextureRegion();
        this.fortRegionRefX = (int) (-t.getRefx()*scale);
        this.fortRegionRefY = (int) (-t.getRefy()*scale);*/
        setSize(this.fortRegionDAO.getTextureRegion().getRegionWidth(), this.fortRegionDAO.getTextureRegion().getRegionHeight());
        //initPotionById();
        setX(hexagonData.source_x);
        setY(hexagonData.source_y);
        //setDrawState(fortData.getDrawState(),true);
    }


    public FortActor(){}

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (true||recource.drawType>2||fortRegionDAO == null||!cam.ifDrawHexagon(hexagonData.getHexagonIndex()) || !isVisible()||recource.buildState==0) {
            return;
        }
     //   float alphaFlash=1.0f;

        /*if(drawState==0)*/{//0 底层绘制
            if(cam.loopState==0||cam.loopState==1) {
                batch.draw(
                        fortRegionDAO.getTextureRegion(),
                        getX()- fortRefX+15, getY()- fortRegionDAO.getRefy()+18,
                        getOriginX(), getOriginY(),
                        getWidth() , getHeight() ,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                batch.draw(
                        fortRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX()-fortRefX +15, getY()- fortRegionDAO.getRefy()+18,
                        getOriginX(), getOriginY(),
                        getWidth(), getHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
        }/*else{// 1 顶层绘制
            if(cam.loopState==0||cam.loopState==1) {
                batch.draw(
                        fortRegionDAO.getTextureRegion(),
                        getX()- fortRegionDAO.getRefx()+15, getY()- fortRegionDAO.getRefy()+18,
                        getOriginX(), getOriginY(),
                        getWidth() , getHeight() ,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                batch.draw(
                        fortRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX()- fortRegionDAO.getRefx() +15, getY()- fortRegionDAO.getRefy()+18,
                        getOriginX(), getOriginY(),
                        getWidth(), getHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
        }*/

        // 还原 batch 原本的 Color
        // batch.setColor(tempBatchColor);
    }


    public void drawFort(Batch batch) {
      //  float refX=game.sMapScreen.tempActor.getX();
       // float refY=game.sMapScreen.tempActor.getY();
        batch.setColor(Color.WHITE);
            if(cam.loopState==0||cam.loopState==1) {
                batch.draw(
                        fortRegionDAO.getTextureRegion(),
                        getX()- fortRefX, getY()- fortRegionDAO.getRefy(),
                        getOriginX(), getOriginY(),
                        getWidth() , getHeight() ,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if(cam.loopState==1||cam.loopState==2){//如果循环,则添加一部分
                batch.draw(
                        fortRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX()-fortRefX , getY()- fortRegionDAO.getRefy(),
                        getOriginX(), getOriginY(),
                        getWidth(), getHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
        }

        // 还原 batch 原本的 Color
        // batch.setColor(tempBatchColor);

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

    /*private void initPotionById(){
        //float refx =0;float refy=0;
        //  float refx = armyMarkTR.getRefx();float refy= armyMarkTR.getRefy();
        int  x = (fortData.getHexagonIndex() % btl.masterData.getWidth()) + 1;
        int  y = (fortData.getHexagonIndex() / btl.masterData.getWidth()) + 1;
        sourceX= GameMap.getX_pxByHexagon(x,scale,0);
        sourceY=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        //核心坐标
    }*/

    public boolean inScreen() {
        if(cam!=null&&cam.inScreen(fortData.getHexagonIndex())){
            return true;
        }
        return false;
    }

}
