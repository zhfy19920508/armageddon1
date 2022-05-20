package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;
import com.zhfy.game.MainGame;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.screen.stage.SMapGameStage;

public class FacilityActor extends BaseActor implements Pool.Poolable {


    private MainGame game;
    private TextureRegionDAO facilityRegionDAO;

    private Fb2Smap.FacilityData facilityData;


    private CamerDAO cam;
    private Fb2Map.MapHexagon hexagonData;
   // private int drawState; //0城市 1城市血量+城市等级   3城市名


    private int mapW;//格子数,辅助计算坐标



    private SMapGameStage.SMapPublicRescource resource;

    public FacilityActor(MainGame game,  Fb2Smap.FacilityData facilityData, int mapW_px, float scale, CamerDAO cam, SMapGameStage.SMapPublicRescource resource) {
       init(game,  facilityData, mapW_px,scale, cam, resource);
    }

    public void init( MainGame game, Fb2Smap.FacilityData facilityData, int mapW, float scale, CamerDAO cam, SMapGameStage.SMapPublicRescource resource){

        this.game=game;
        this.mapW=mapW;
        this.cam=cam;
        this.facilityData=facilityData;
        facilityData.facilityActor=this;
        //TODO
        facilityRegionDAO = game.getImgLists().getTextureByName(game.gameConfig.getDEF_FACILITY().getElementById(facilityData.getFacilityId()).get("image"));
        this.resource=resource;
        setSize(this.facilityRegionDAO.getTextureRegion().getRegionWidth(), this.facilityRegionDAO.getTextureRegion().getRegionHeight());
        setScale(scale);
        //initPotionById();
        hexagonData=facilityData.getHexagonData();
        setX(hexagonData.source_x);
        setY(hexagonData.source_y);
    }



    public void update(){
        facilityRegionDAO = game.getImgLists().getTextureByName(game.gameConfig.getDEF_FACILITY().getElementById(facilityData.getFacilityId()).get("image"));


    }



    public FacilityActor(){}

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (true||resource.buildState==0||facilityRegionDAO == null || facilityRegionDAO.getTextureRegion()==null|| !cam.ifDrawHexagon(hexagonData.getHexagonIndex())||!isVisible()/*||cam.getCamera().zoom>0.85*/) {
            return;
        }

               // float alphaFlash=((SMapGameStage) getStage()).alphaFlash*0.7f;
               // float zoomChange= ((SMapGameStage) getStage()).zoomChange*2.2f;




        batch.setColor(1.0f,1.0f,1.0f,1.0f);
        if(resource.buildState==1){
            if(cam.loopState==0||cam.loopState==1) {
                batch.draw(
                        facilityRegionDAO.getTextureRegion(),
                        getX()- facilityRegionDAO.getRefx()+15, getY()- facilityRegionDAO.getRefy()+18,
                        getOriginX(), getOriginY(),
                        getWidth() , getHeight() ,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }

            if(cam.loopState==2||cam.loopState==1) {
                batch.draw(
                        facilityRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX()- facilityRegionDAO.getRefy()+15, getY()- facilityRegionDAO.getRefy()+18,
                        getOriginX(), getOriginY(),
                        getWidth(), getHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }

        }
    }
    public void drawFacility(Batch batch){
        batch.setColor(Color.WHITE);
        if(cam.loopState==0||cam.loopState==1) {
            batch.draw(
                    facilityRegionDAO.getTextureRegion(),
                    getX()-facilityRegionDAO.getRefx(), getY()- facilityRegionDAO.getRefy(),
                    getOriginX(), getOriginY(),
                    getWidth() , getHeight() ,
                    getScaleX(), getScaleY(),
                    getRotation()
            );
        }

        if(cam.loopState==2||cam.loopState==1) {
            batch.draw(
                    facilityRegionDAO.getTextureRegion(),
                    cam.getMapW_px()+getX()- facilityRegionDAO.getRefx(), getY()- facilityRegionDAO.getRefy(),
                    getOriginX(), getOriginY(),
                    getWidth(), getHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
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

    /*private void initPotionById(){
        //float refx =0;float refy=0;
      //  float refx = armyMarkTR.getRefx();float refy= armyMarkTR.getRefy();
        int  x = (buildData.getRegionId() % btl.masterData.getWidth()) + 1;
        int  y = (buildData.getRegionId() / btl.masterData.getWidth()) + 1;
        sourceX= GameMap.getX_pxByHexagon(x,scale,0);
        sourceY=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        //核心坐标
         }*/


    public boolean inScreen() {
        if(cam!=null&&cam.inScreen(facilityData.getHexagonIndex())){
            return true;
        }
        return false;
    }

}

