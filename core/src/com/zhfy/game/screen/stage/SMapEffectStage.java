package com.zhfy.game.screen.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;
import com.zhfy.game.MainGame;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.screen.actor.framework.AnimationActor;
import com.zhfy.game.screen.actor.framework.BuildActor;
import com.zhfy.game.screen.actor.framework.EffectActorListDAO;
import com.zhfy.game.screen.actor.framework.SMapBGActor;
import com.zhfy.game.screen.actor.framework.SMapFVActor;
import com.zhfy.game.screen.stage.base.BaseStage;

import java.util.Iterator;

public class SMapEffectStage extends BaseStage {


    //实现功能
    //地图对象与stage分离

    public  SMapFVActor smapFv;  //用来显示一些前景特效
    public SMapBGActor smapBg;

    public EffectActorListDAO effectDAO;
    private SMapGameStage gameStage;



    public SMapEffectStage(MainGame mainGame,SMapGameStage gameStage) {
        super(mainGame, gameStage.getViewport());
        this.gameStage=gameStage;
        smapFv = new SMapFVActor(getMainGame(),this,gameStage.cam, gameStage.screenId,gameStage.getsMapDAO(),gameStage.scale,gameStage.getMapW_px(),gameStage.getMapH_px());
        addActor(smapFv);
        gameStage.smapFv=smapFv;
        smapBg=gameStage.smapBg;
        effectDAO =new EffectActorListDAO(mainGame,this,gameStage.cam,gameStage.scale,gameStage.getMapW_px(),gameStage.getMapH_px(),gameStage.getMapW());
        gameStage.effectDAO = effectDAO;
        //addCityName();
    }

    //因为城市名显示会导致 cell暴增,所以暂时不启用
    /*private void addCityName() {
        Iterator<IntMap.Entry<Fb2Smap.BuildData>> itB1 = gameStage.sMapDAO.buildRDatas.iterator();
        while (itB1.hasNext()) {
           Fb2Smap.BuildData buildData =itB1.next().value;
            BuildActor b = buildData.getActor();
            if(buildData.getBuildName()!=0){
              Label  label=new Label(  buildData.areaName,new Label.LabelStyle(getMainGame().gameConfig.gameFont, Color.WHITE));
                //"124563987258,12456382236874,123654236",new LabelStyle(new BitmapFont(), null)
                label.setX(b.getX());
                label.setY(b.getY());
                label.setAlignment(Align.center);
                label.setFontScale(0.3f);
                addActor(label);
            }
        }
    }*/


    public void drawEffect(int effectId,int hexagon1,int hexagon2,float delaySeconds){
        effectDAO.darwEffect(effectId,hexagon1,hexagon2,delaySeconds);
    }
    public void drawUnitHp(int hexagon, int changeHp,float time){
         smapFv.drawNumerChange(hexagon,changeHp,time,changeHp>0?Color.GREEN:Color.RED,true);
    }

    public void drawMorale(int hexagon, int changeHp,float time){
        smapFv.drawNumerChange(hexagon,changeHp,time,Color.BLUE,false);
    }


    public void drawSkill(int hexagon,int skillId){
        effectDAO.darwSkillEffect(hexagon,1,skillId);
    }
    public void drawFeature(int hexagon,int skillId){
        effectDAO.darwSkillEffect(hexagon,0,skillId);
    }

    public void drawMark(IntIntMap ids,int clickId, int type) {
        smapFv.setDrawMark(ids,clickId,type);
    }

    public void drawFace(int hexagon,int face){
        effectDAO.darwFaceEffect(hexagon,face);
    }

}
