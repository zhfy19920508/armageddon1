package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.screen.stage.SMapGameStage;

public class ArmyActor extends BaseActor implements Pool.Poolable {

    private Fb2Smap btl;
    private TextureRegion armyMarkRegion;

    private TextureRegion armyModelR;
    private TextureRegion armyModelRegion;
    private TextureRegion armyModelRegionF;
    private TextureRegionDAO armyModelTR;
    private TextureRegion bgArmyModelR;
    private TextureRegion bgArmyModelRegion;
    private TextureRegion bgArmyModelRegionF;
    private TextureRegionDAO bgArmyModelTR;


    private TextureRegion armyModelR1;
    private TextureRegion armyModelRegion1;
    private TextureRegion armyModelRegionF1;
    private TextureRegionDAO armyModelTR1;
    private TextureRegion bgArmyModelR1;
    private TextureRegion bgArmyModelRegion1;
    private TextureRegion bgArmyModelRegionF1;
    private TextureRegionDAO bgArmyModelTR1;
    private TextureRegion armyModelR2;
    private TextureRegion armyModelRegion2;
    private TextureRegion armyModelRegionF2;
    private TextureRegionDAO armyModelTR2;
    private TextureRegion bgArmyModelR2;
    private TextureRegion bgArmyModelRegion2;
    private TextureRegion bgArmyModelRegionF2;
    private TextureRegionDAO bgArmyModelTR2;
    private TextureRegion armyModelR3;
    private TextureRegion armyModelRegion3;
    private TextureRegion armyModelRegionF3;
    private TextureRegionDAO armyModelTR3;
    private TextureRegion bgArmyModelR3;
    private TextureRegion bgArmyModelRegion3;
    private TextureRegion bgArmyModelRegionF3;
    private TextureRegionDAO bgArmyModelTR3;
    private TextureRegion armyModelR4;
    private TextureRegion armyModelRegion4;
    private TextureRegion armyModelRegionF4;
    private TextureRegionDAO armyModelTR4;
    private TextureRegion bgArmyModelR4;
    private TextureRegion bgArmyModelRegion4;
    private TextureRegion bgArmyModelRegionF4;
    private TextureRegionDAO bgArmyModelTR4;
    private TextureRegion armyModelR5;
    private TextureRegion armyModelRegion5;
    private TextureRegion armyModelRegionF5;
    private TextureRegionDAO armyModelTR5;
    private TextureRegion bgArmyModelR5;
    private TextureRegion bgArmyModelRegion5;
    private TextureRegion bgArmyModelRegionF5;
    private TextureRegionDAO bgArmyModelTR5;
    private TextureRegion armyModelR6;
    private TextureRegion armyModelRegion6;
    private TextureRegion armyModelRegionF6;
    private TextureRegionDAO armyModelTR6;
    private TextureRegion bgArmyModelR6;
    private TextureRegion bgArmyModelRegion6;
    private TextureRegion bgArmyModelRegionF6;
    private TextureRegionDAO bgArmyModelTR6;


    private float modelRefX1;
    private float modelRefY1;
    private float modelScale1;
    private float bgModelRefX1;
    private float bgModelRefY1;
    private float bgModelScale1;
    private float modelRefX2;
    private float modelRefY2;
    private float modelScale2;
    private float bgModelRefX2;
    private float bgModelRefY2;
    private float bgModelScale2;
    private float modelRefX3;
    private float modelRefY3;
    private float modelScale3;
    private float bgModelRefX3;
    private float bgModelRefY3;
    private float bgModelScale3;
    private float modelRefX4;
    private float modelRefY4;
    private float modelScale4;
    private float bgModelRefX4;
    private float bgModelRefY4;
    private float bgModelScale4;
    private float modelRefX5;
    private float modelRefY5;
    private float modelScale5;
    private float bgModelRefX5;
    private float bgModelRefY5;
    private float bgModelScale5;
    private float modelRefX6;
    private float modelRefY6;
    private float modelScale6;
    private float bgModelRefX6;
    private float bgModelRefY6;
    private float bgModelScale6;
    //军衔位置调整为中心点位置用到
    public float centerModelRefX;
    public float centerModelRefY;

    private float modelRefX0;
    private float modelRefY0;
    private float modelScale0;
    private float bgModelRefX0;
    private float bgModelRefY0;
    private float bgModelScale0;
    public TextureRegionDAO armyHpRegionDAO;
    public TextureRegion armyHpRegion;
    public TextureRegionDAO sgArmyHpRegionDAO;
    public TextureRegion sgArmyHpRegion;
    public TextureRegionDAO targetRegionDAO;

    public TextureRegion scArmyRegion;
    public TextureRegion scArmyGroupRegion;
    public TextureRegion scArmyBorderRegion;
    public TextureRegion scArmyFormation;
    public TextureRegion directArrowRegion;


    private CamerDAO cam;

    private int mapW;//格子数,辅助计算坐标
    //private int loopState;
    //private int index;//使用type和index来一起判断 显示的region
    private TextureRegionDAO armyMarkTR;
    private float scale;

    //  private Map<StringName,Integer> coordMap= new HashMap<>();
    private Color legionColor;
    private Color legionFogColor;

    public Fb2Smap.ArmyData armyData;
    //public Fb2Map.MapHexagon hexagonData;
    public Fb2Smap.GeneralData generalData;
    public SMapGameStage.SMapPublicRescource resource;

    //可更新内容
    private float hpRate;
    private float hpRefy;
    private TextureRegionDAO rank;
    public TextureRegion smallGeneral;
    private TextureRegion smallGeneralBg;
    private boolean ifGeneral;
    public int targetState;// 0不显示 1 红箭头 2蓝箭头 3点中 4 标记
    public float targetAngle;

    private TextureRegion flag;
    private float flagW;
    private float flagH;
    private float hpH;

    public boolean ifPlayerCommand;
    private boolean ifFlash;
    private boolean ifDrawAir;
    private boolean ifDrawNul;
    private boolean ifDrawEnergy;
    private boolean ifDrawFormation;
    //  private boolean ifDrawFort;
    private int fortId;
    private int moraleRefY;
    private Color moraleColor;
    private boolean ifCanUpd;

    //private TextureRegionDAO airRegionDAO;
    //private TextureRegionDAO nulRegionDAO;
    // private TextureRegionDAO fortRegionDAO;
    private TextureRegionDAO energyRegionDAO;
    private TextureRegionDAO armyStateDAO;
    private TextureRegionDAO groupDAO;
    private XmlReader.Element modelE;
    private XmlReader.Element modelE1;
    private XmlReader.Element modelE2;
    private XmlReader.Element modelE3;
    private XmlReader.Element modelE4;
    private XmlReader.Element modelE5;
    private XmlReader.Element modelE6;


    public void updArmyModel() {
        int countryId = armyData.getLegionData().getCountryId();
        int nowYear = armyData.getNowYear();
        if(armyData.getArmyType()!=4&&armyData.getArmyType()!=8&&armyData.potionIsSea()){
            if(armyData.getTransportType()==0){
                modelE =resource.carrierE;
            }else {
                modelE = getMainGame().gameConfig.getArmyModel(armyData.getAge(), armyData.getNowYear(), 4,1400+armyData.getTransportType(), countryId,armyData.ifCrossFront(0));
            }
        }else {
            modelE = getMainGame().gameConfig.getArmyModel(armyData.getAge(), armyData.getNowYear(), armyData.getArmyType(), armyData.getUnitArmyId0(), countryId,armyData.ifCrossFront(0));
        }
        if (armyData.isUnitGroup()) {
            int armyId = armyData.getUnitGroupArmyId(1);
            if (armyId == 0) {
                modelE1 = null;
            } else {
                int armyType = armyData.getArmyXmlE(1,true).getInt("type", 0);
                modelE1 = getMainGame().gameConfig.getArmyModel(armyData.getAge(), nowYear, armyType, armyId, countryId,armyData.ifCrossFront(1));
            }
            armyId = armyData.getUnitGroupArmyId(2);
            if (armyId == 0) {
                modelE2 = null;
            } else {
                int armyType = armyData.getArmyXmlE(2,true).getInt("type", 0);
                modelE2 = getMainGame().gameConfig.getArmyModel(armyData.getAge(), nowYear, armyType, armyId, countryId,armyData.ifCrossFront(2));
            }
            armyId = armyData.getUnitGroupArmyId(3);
            if (armyId == 0) {
                modelE3 = null;
            } else {
                int armyType = armyData.getArmyXmlE(3,true).getInt("type", 0);
                modelE3 = getMainGame().gameConfig.getArmyModel(armyData.getAge(), nowYear, armyType, armyId, countryId,armyData.ifCrossFront(3));
            }
            armyId = armyData.getUnitGroupArmyId(4);
            if (armyId == 0) {
                modelE4 = null;
            } else {
                int armyType = armyData.getArmyXmlE(4,true).getInt("type", 0);
                modelE4 = getMainGame().gameConfig.getArmyModel(armyData.getAge(), nowYear, armyType, armyId, countryId,armyData.ifCrossFront(4));
            }
            armyId = armyData.getUnitGroupArmyId(5);
            if (armyId == 0) {
                modelE5 = null;
            } else {
                int armyType = armyData.getArmyXmlE(5,true).getInt("type", 0);
                modelE5 = getMainGame().gameConfig.getArmyModel(armyData.getAge(), nowYear, armyType, armyId, countryId,armyData.ifCrossFront(5));
            }
            armyId = armyData.getUnitGroupArmyId(6);
            if (armyId == 0) {
                modelE6 = null;
            } else {
                int armyType = armyData.getArmyXmlE(6,true).getInt("type", 0);
                modelE6 = getMainGame().gameConfig.getArmyModel(armyData.getAge(), nowYear, armyType, armyId, countryId,armyData.ifCrossFront(6));
            }
        } else {
            modelE1 = null;
            modelE2 = null;
            modelE3 = null;
            modelE4 = null;
            modelE5 = null;
            modelE6 = null;
        }
        switchArmyModel();
    }

    private void setArmyModel(int index, TextureRegionDAO armyModelTR, TextureRegion armyModelR, TextureRegion armyModelRegion, TextureRegion armyModelRegionF, TextureRegionDAO bgArmyModelTR, TextureRegion bgArmyModelR, TextureRegion bgArmyModelRegion, TextureRegion bgArmyModelRegionF) {
        switch (index) {
            case 0:
                this.armyModelTR = armyModelTR;
                this.armyModelR = armyModelR;
                this.armyModelRegion = armyModelRegion;
                this.armyModelRegionF = armyModelRegionF;
                this.bgArmyModelTR = bgArmyModelTR;
                this.bgArmyModelR = bgArmyModelR;
                this.bgArmyModelRegion = bgArmyModelRegion;
                this.bgArmyModelRegionF = bgArmyModelRegionF;
                break;
            case 1:
                armyModelR1 = armyModelR;
                armyModelRegion1 = armyModelRegion;
                armyModelRegionF1 = armyModelRegionF;
                armyModelTR1 = armyModelTR;
                bgArmyModelR1 = bgArmyModelR;
                bgArmyModelRegion1 = bgArmyModelRegion;
                bgArmyModelRegionF1 = bgArmyModelRegionF;
                bgArmyModelTR1 = bgArmyModelTR;
                break;
            case 2:
                armyModelR2 = armyModelR;
                armyModelRegion2 = armyModelRegion;
                armyModelRegionF2 = armyModelRegionF;
                armyModelTR2 = armyModelTR;
                bgArmyModelR2 = bgArmyModelR;
                bgArmyModelRegion2 = bgArmyModelRegion;
                bgArmyModelRegionF2 = bgArmyModelRegionF;
                bgArmyModelTR2 = bgArmyModelTR;
                break;
            case 3:
                armyModelR3 = armyModelR;
                armyModelRegion3 = armyModelRegion;
                armyModelRegionF3 = armyModelRegionF;
                armyModelTR3 = armyModelTR;
                bgArmyModelR3 = bgArmyModelR;
                bgArmyModelRegion3 = bgArmyModelRegion;
                bgArmyModelRegionF3 = bgArmyModelRegionF;
                bgArmyModelTR3 = bgArmyModelTR;
                break;
            case 4:
                armyModelR4 = armyModelR;
                armyModelRegion4 = armyModelRegion;
                armyModelRegionF4 = armyModelRegionF;
                armyModelTR4 = armyModelTR;
                bgArmyModelR4 = bgArmyModelR;
                bgArmyModelRegion4 = bgArmyModelRegion;
                bgArmyModelRegionF4 = bgArmyModelRegionF;
                bgArmyModelTR4 = bgArmyModelTR;
                break;
            case 5:
                armyModelR5 = armyModelR;
                armyModelRegion5 = armyModelRegion;
                armyModelRegionF5 = armyModelRegionF;
                armyModelTR5 = armyModelTR;
                bgArmyModelR5 = bgArmyModelR;
                bgArmyModelRegion5 = bgArmyModelRegion;
                bgArmyModelRegionF5 = bgArmyModelRegionF;
                bgArmyModelTR5 = bgArmyModelTR;
                break;
            case 6:
                armyModelR6 = armyModelR;
                armyModelRegion6 = armyModelRegion;
                armyModelRegionF6 = armyModelRegionF;
                armyModelTR6 = armyModelTR;
                bgArmyModelR6 = bgArmyModelR;
                bgArmyModelRegion6 = bgArmyModelRegion;
                bgArmyModelRegionF6 = bgArmyModelRegionF;
                bgArmyModelTR6 = bgArmyModelTR;
                break;
        }

    }


    //在运输船与本体兵模之间切换
    public void switchArmyModel() {
        if (modelE == null) {
            return;
        }
        //setArmyModel(0,armyModelTR,armyModelR,armyModelRegion,armyModelRegionF,bgArmyModelTR,bgArmyModelR,bgArmyModelRegion,bgArmyModelRegionF);

        armyModelTR = getMainGame().getImgLists().getTextureByName(modelE.get("defaultImgName"));
        armyModelRegion = armyModelTR.getTextureRegion();
        int modelType = modelE.getInt("modelType");
        if (modelType == 1) {//固定方向的兵模
            armyModelRegionF = armyModelRegion;
        }  else if(modelType==2||modelType==0){
            armyModelRegionF = getMainGame().getImgLists().getFlipRegion(armyModelTR.getName());
        }else if(modelType==3){
            armyModelRegionF = getMainGame().getImgLists().getTextureByName(modelE.get("flipImgName")).getTextureRegion();
        }
        String bgName = modelE.get("bgImgName", "");
        if (armyData.getArmyType() != 4 && armyData.getArmyType() != 8 && armyData.potionIsSea()) {
           // armyModelTR = getMainGame().getImgLists().getTextureByName(resource.carrierE.get("defaultImgName", "1401_0_1_0"));;
          //  armyModelRegion = armyModelTR.getTextureRegion();
             bgArmyModelTR = null;
            bgArmyModelR = null;
            bgArmyModelRegion = null;
            bgArmyModelRegionF = null;
        } else {
            if (!ComUtil.isEmpty(bgName)) {
                bgArmyModelTR = getMainGame().getImgLists().getTextureByName(modelE.get("bgImgName"));
                bgArmyModelRegion = bgArmyModelTR.getTextureRegion();
                modelType = modelE.getInt("bgImgModelType", 0);
                if (modelType == 1) {//固定方向的兵模
                    bgArmyModelRegionF = bgArmyModelRegion;
                } else if(modelType==2||modelType==0){
                    bgArmyModelRegionF = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR.getName());
                }else if(modelType==3){
                    bgArmyModelRegionF = getMainGame().getImgLists().getTextureByName(modelE.get("flipBgImgName")).getTextureRegion();
                }
            }
        }

        if (modelE1 != null) {
            armyModelTR1 = getMainGame().getImgLists().getTextureByName(modelE1.get("defaultImgName"));
            armyModelRegion1 = armyModelTR1.getTextureRegion();
            modelType = modelE1.getInt("modelType");
            if (modelType == 1) {//固定方向的兵模
                armyModelRegionF1 = armyModelRegion1;
            } else if(modelType==2||modelType==0){
                armyModelRegionF1 = getMainGame().getImgLists().getFlipRegion(armyModelTR1.getName());
            }else if(modelType==3){
                armyModelRegionF1 = getMainGame().getImgLists().getTextureByName(modelE1.get("flipImgName")).getTextureRegion();
            }
            bgName = modelE1.get("bgImgName", "");
            if (!ComUtil.isEmpty(bgName)) {
                bgArmyModelTR1 = getMainGame().getImgLists().getTextureByName(modelE1.get("bgImgName"));
                bgArmyModelRegion1 = bgArmyModelTR1.getTextureRegion();
                modelType = modelE1.getInt("bgImgModelType", 0);
                if (modelType == 1) {//固定方向的兵模
                    bgArmyModelRegionF1 = bgArmyModelRegion1;
                } else if(modelType==2||modelType==0){
                    bgArmyModelRegionF1 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR1.getName());
                }else if(modelType==3){
                    bgArmyModelRegionF1 = getMainGame().getImgLists().getTextureByName(modelE1.get("flipBgImgName")).getTextureRegion();
                }
            }
        }
        if (modelE2 != null) {
            armyModelTR2 = getMainGame().getImgLists().getTextureByName(modelE2.get("defaultImgName"));
            armyModelRegion2 = armyModelTR2.getTextureRegion();
            modelType = modelE2.getInt("modelType");
            if (modelType == 1) {//固定方向的兵模
                armyModelRegionF2 = armyModelRegion2;
            } else if(modelType==2||modelType==0){
                armyModelRegionF2 = getMainGame().getImgLists().getFlipRegion(armyModelTR2.getName());
            }else if(modelType==3){
                armyModelRegionF2 = getMainGame().getImgLists().getTextureByName(modelE2.get("flipImgName")).getTextureRegion();
            }
            bgName = modelE2.get("bgImgName", "");
            if (!ComUtil.isEmpty(bgName)) {
                bgArmyModelTR2 = getMainGame().getImgLists().getTextureByName(modelE2.get("bgImgName"));
                bgArmyModelRegion2 = bgArmyModelTR2.getTextureRegion();
                modelType = modelE2.getInt("bgImgModelType", 0);
                if (modelType == 1) {//固定方向的兵模
                    bgArmyModelRegionF2 = bgArmyModelRegion2;
                } else if(modelType==2||modelType==0){
                    bgArmyModelRegionF2 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR2.getName());
                }else if(modelType==3){
                    bgArmyModelRegionF2 = getMainGame().getImgLists().getTextureByName(modelE2.get("flipBgImgName")).getTextureRegion();
                }
            }
        }
        if (modelE3 != null) {
            armyModelTR3 = getMainGame().getImgLists().getTextureByName(modelE3.get("defaultImgName"));
            armyModelRegion3 = armyModelTR3.getTextureRegion();
            modelType = modelE3.getInt("modelType");
            if (modelType == 1) {//固定方向的兵模
                armyModelRegionF3 = armyModelRegion3;
            } else if(modelType==2||modelType==0){
                armyModelRegionF3 = getMainGame().getImgLists().getFlipRegion(armyModelTR3.getName());
            }else if(modelType==3){
                armyModelRegionF3 = getMainGame().getImgLists().getTextureByName(modelE3.get("flipImgName")).getTextureRegion();
            }
            bgName = modelE3.get("bgImgName", "");
            if (!ComUtil.isEmpty(bgName)) {
                bgArmyModelTR3 = getMainGame().getImgLists().getTextureByName(modelE3.get("bgImgName"));
                bgArmyModelRegion3 = bgArmyModelTR3.getTextureRegion();
                modelType = modelE3.getInt("bgImgModelType", 0);
                if (modelType == 1) {//固定方向的兵模
                    bgArmyModelRegionF3 = bgArmyModelRegion3;
                } else if(modelType==2||modelType==0){
                    bgArmyModelRegionF3 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR3.getName());
                }else if(modelType==3){
                    bgArmyModelRegionF3 = getMainGame().getImgLists().getTextureByName(modelE3.get("flipBgImgName")).getTextureRegion();
                }
            }
        }
        if (modelE4 != null) {
            armyModelTR4 = getMainGame().getImgLists().getTextureByName(modelE4.get("defaultImgName"));
            armyModelRegion4 = armyModelTR4.getTextureRegion();
            modelType = modelE4.getInt("modelType");
            if (modelType == 1) {//固定方向的兵模
                armyModelRegionF4 = armyModelRegion4;
            } else if(modelType==2||modelType==0){
                armyModelRegionF4 = getMainGame().getImgLists().getFlipRegion(armyModelTR4.getName());
            }else if(modelType==3){
                armyModelRegionF4 = getMainGame().getImgLists().getTextureByName(modelE4.get("flipImgName")).getTextureRegion();
            }
            bgName = modelE4.get("bgImgName", "");
            if (!ComUtil.isEmpty(bgName)) {
                bgArmyModelTR4 = getMainGame().getImgLists().getTextureByName(modelE4.get("bgImgName"));
                bgArmyModelRegion4 = bgArmyModelTR4.getTextureRegion();
                modelType = modelE4.getInt("bgImgModelType", 0);
                if (modelType == 1) {//固定方向的兵模
                    bgArmyModelRegionF4 = bgArmyModelRegion4;
                } else if(modelType==2||modelType==0){
                    bgArmyModelRegionF4 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR4.getName());
                }else if(modelType==3){
                    bgArmyModelRegionF4 = getMainGame().getImgLists().getTextureByName(modelE4.get("flipBgImgName")).getTextureRegion();
                }
            }
        }
        if (modelE5 != null) {
            armyModelTR5 = getMainGame().getImgLists().getTextureByName(modelE5.get("defaultImgName"));
            armyModelRegion5 = armyModelTR5.getTextureRegion();
            modelType = modelE5.getInt("modelType");
            if (modelType == 1) {//固定方向的兵模
                armyModelRegionF5 = armyModelRegion5;
            } else if(modelType==2||modelType==0){
                armyModelRegionF5 = getMainGame().getImgLists().getFlipRegion(armyModelTR5.getName());
            }else if(modelType==3){
                armyModelRegionF5 = getMainGame().getImgLists().getTextureByName(modelE5.get("flipImgName")).getTextureRegion();
            }
            bgName = modelE5.get("bgImgName", "");
            if (!ComUtil.isEmpty(bgName)) {
                bgArmyModelTR5 = getMainGame().getImgLists().getTextureByName(modelE5.get("bgImgName"));
                bgArmyModelRegion5 = bgArmyModelTR5.getTextureRegion();
                modelType = modelE5.getInt("bgImgModelType", 0);
                if (modelType == 1) {//固定方向的兵模
                    bgArmyModelRegionF5 = bgArmyModelRegion5;
                } else if(modelType==2||modelType==0){
                    bgArmyModelRegionF5 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR5.getName());
                }else if(modelType==3){
                    bgArmyModelRegionF5 = getMainGame().getImgLists().getTextureByName(modelE5.get("flipBgImgName")).getTextureRegion();
                }
            }
        }
        if (modelE6 != null) {
            armyModelTR6 = getMainGame().getImgLists().getTextureByName(modelE6.get("defaultImgName"));
            armyModelRegion6 = armyModelTR6.getTextureRegion();
            modelType = modelE6.getInt("modelType");
            if (modelType == 1) {//固定方向的兵模
                armyModelRegionF6 = armyModelRegion6;
            } else if(modelType==2||modelType==0){
                armyModelRegionF6 = getMainGame().getImgLists().getFlipRegion(armyModelTR6.getName());
            }else if(modelType==3){
                armyModelRegionF6 = getMainGame().getImgLists().getTextureByName(modelE6.get("flipImgName")).getTextureRegion();
            }
            bgName = modelE6.get("bgImgName", "");
            if (!ComUtil.isEmpty(bgName)) {
                bgArmyModelTR6 = getMainGame().getImgLists().getTextureByName(modelE6.get("bgImgName"));
                bgArmyModelRegion6 = bgArmyModelTR6.getTextureRegion();
                modelType = modelE6.getInt("bgImgModelType", 0);
                if (modelType == 1) {//固定方向的兵模
                    bgArmyModelRegionF6 = bgArmyModelRegion6;
                } else if(modelType==2||modelType==0){
                    bgArmyModelRegionF6 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR6.getName());
                }else if(modelType==3){
                    bgArmyModelRegionF6 = getMainGame().getImgLists().getTextureByName(modelE6.get("flipBgImgName")).getTextureRegion();
                }
            }
        }
           /* if (armyData.isUnitGroup()) {//组合部队
                if (armyData.getUnitGroupArmyId(1) != 0) {
                    setArmyModel(1, armyModelTR1, armyModelR1, armyModelRegion1, armyModelRegionF1, bgArmyModelTR1, bgArmyModelR1, bgArmyModelRegion1, bgArmyModelRegionF1);
                }
                if (armyData.getUnitGroupArmyId(2) != 0) {
                    setArmyModel(2, armyModelTR2, armyModelR2, armyModelRegion2, armyModelRegionF2, bgArmyModelTR2, bgArmyModelR2, bgArmyModelRegion2, bgArmyModelRegionF2);
                }
                if (armyData.getUnitGroupArmyId(3) != 0) {
                    setArmyModel(3, armyModelTR3, armyModelR3, armyModelRegion3, armyModelRegionF3, bgArmyModelTR3, bgArmyModelR3, bgArmyModelRegion3, bgArmyModelRegionF3);
                }
                if (armyData.getUnitGroupArmyId(4) != 0) {
                    setArmyModel(4, armyModelTR4, armyModelR4, armyModelRegion4, armyModelRegionF4, bgArmyModelTR4, bgArmyModelR4, bgArmyModelRegion4, bgArmyModelRegionF4);
                }
                if (armyData.getUnitGroupArmyId(5) != 0) {
                    setArmyModel(5, armyModelTR5, armyModelR5, armyModelRegion5, armyModelRegionF5, bgArmyModelTR5, bgArmyModelR5, bgArmyModelRegion5, bgArmyModelRegionF5);
                }
                if (armyData.getUnitGroupArmyId(6) != 0) {
                    setArmyModel(6, armyModelTR6, armyModelR6, armyModelRegion6, armyModelRegionF6, bgArmyModelTR6, bgArmyModelR6, bgArmyModelRegion6, bgArmyModelRegionF6);
                }
            }else{
                if (modelE1 != null) {
                    armyModelTR1 = getMainGame().getImgLists().getTextureByName(modelE1.get("defaultImgName"));
                    armyModelRegion1 = armyModelTR1.getTextureRegion();
                      modelType = modelE1.getInt("modelType");
                    if (modelType == 1) {//固定方向的兵模
                        armyModelRegionF1 = armyModelRegion1;
                    } else if(modelType==2||modelType==0){
                        armyModelRegionF1 = getMainGame().getImgLists().getFlipRegion(armyModelTR1.getName());
                    }else if(modelType==3){
                        armyModelRegionF1 = getMainGame().getImgLists().getTextureByName(modelE1.get("flipImgName")).getTextureRegion();
                    }
                    bgName = modelE1.get("bgImgName", "");
                    if (!ComUtil.isEmpty(bgName)) {
                        bgArmyModelTR1 = getMainGame().getImgLists().getTextureByName(modelE1.get("bgImgName"));
                        bgArmyModelRegion1 = bgArmyModelTR1.getTextureRegion();
                        modelType = modelE1.getInt("bgImgModelType", 0);
                        if (modelType == 1) {//固定方向的兵模
                            bgArmyModelRegionF1 = bgArmyModelRegion1;
                        } else if(modelType==2||modelType==0){
                            bgArmyModelRegionF1 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR1.getName());
                        }else if(modelType==3){
                            bgArmyModelRegionF1 = getMainGame().getImgLists().getTextureByName(modelE1.get("flipBgImgName")).getTextureRegion();
                        }
                    }
                }
                if (modelE2 != null) {
                    armyModelTR2 = getMainGame().getImgLists().getTextureByName(modelE2.get("defaultImgName"));
                    armyModelRegion2 = armyModelTR2.getTextureRegion();
                    modelType = modelE2.getInt("modelType");
                    if (modelType == 1) {//固定方向的兵模
                        armyModelRegionF2 = armyModelRegion2;
                    } else if(modelType==2||modelType==0){
                        armyModelRegionF2 = getMainGame().getImgLists().getFlipRegion(armyModelTR2.getName());
                    }else if(modelType==3){
                        armyModelRegionF2 = getMainGame().getImgLists().getTextureByName(modelE2.get("flipImgName")).getTextureRegion();
                    }
                    bgName = modelE2.get("bgImgName", "");
                    if (!ComUtil.isEmpty(bgName)) {
                        bgArmyModelTR2 = getMainGame().getImgLists().getTextureByName(modelE2.get("bgImgName"));
                        bgArmyModelRegion2 = bgArmyModelTR2.getTextureRegion();
                        modelType = modelE2.getInt("bgImgModelType", 0);
                        if (modelType == 1) {//固定方向的兵模
                            bgArmyModelRegionF2 = bgArmyModelRegion2;
                        } else if(modelType==2||modelType==0){
                            bgArmyModelRegionF2 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR2.getName());
                        }else if(modelType==3){
                            bgArmyModelRegionF2 = getMainGame().getImgLists().getTextureByName(modelE2.get("flipBgImgName")).getTextureRegion();
                        }
                    }
                }
                if (modelE3 != null) {
                    armyModelTR3 = getMainGame().getImgLists().getTextureByName(modelE3.get("defaultImgName"));
                    armyModelRegion3 = armyModelTR3.getTextureRegion();
                    modelType = modelE3.getInt("modelType");
                    if (modelType == 1) {//固定方向的兵模
                        armyModelRegionF3 = armyModelRegion3;
                    } else if(modelType==2||modelType==0){
                        armyModelRegionF3 = getMainGame().getImgLists().getFlipRegion(armyModelTR3.getName());
                    }else if(modelType==3){
                        armyModelRegionF3 = getMainGame().getImgLists().getTextureByName(modelE3.get("flipImgName")).getTextureRegion();
                    }
                     bgName = modelE3.get("bgImgName", "");
                    if (!ComUtil.isEmpty(bgName)) {
                        bgArmyModelTR3 = getMainGame().getImgLists().getTextureByName(modelE3.get("bgImgName"));
                        bgArmyModelRegion3 = bgArmyModelTR3.getTextureRegion();
                        modelType = modelE3.getInt("bgImgModelType", 0);
                        if (modelType == 1) {//固定方向的兵模
                            bgArmyModelRegionF3 = bgArmyModelRegion3;
                        } else if(modelType==2||modelType==0){
                            bgArmyModelRegionF3 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR3.getName());
                        }else if(modelType==3){
                            bgArmyModelRegionF3 = getMainGame().getImgLists().getTextureByName(modelE3.get("flipBgImgName")).getTextureRegion();
                        }
                    }
                }
                if (modelE4 != null) {
                    armyModelTR4 = getMainGame().getImgLists().getTextureByName(modelE4.get("defaultImgName"));
                    armyModelRegion4 = armyModelTR4.getTextureRegion();
                       modelType = modelE4.getInt("modelType");
                    if (modelType == 1) {//固定方向的兵模
                        armyModelRegionF4 = armyModelRegion4;
                    } else if(modelType==2||modelType==0){
                        armyModelRegionF4 = getMainGame().getImgLists().getFlipRegion(armyModelTR4.getName());
                    }else if(modelType==3){
                        armyModelRegionF4 = getMainGame().getImgLists().getTextureByName(modelE4.get("flipImgName")).getTextureRegion();
                    }
                     bgName = modelE4.get("bgImgName", "");
                    if (!ComUtil.isEmpty(bgName)) {
                        bgArmyModelTR4 = getMainGame().getImgLists().getTextureByName(modelE4.get("bgImgName"));
                        bgArmyModelRegion4 = bgArmyModelTR4.getTextureRegion();
                        modelType = modelE4.getInt("bgImgModelType", 0);
                        if (modelType == 1) {//固定方向的兵模
                            bgArmyModelRegionF4 = bgArmyModelRegion4;
                        } else if(modelType==2||modelType==0){
                            bgArmyModelRegionF4 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR4.getName());
                        }else if(modelType==3){
                            bgArmyModelRegionF4 = getMainGame().getImgLists().getTextureByName(modelE4.get("flipBgImgName")).getTextureRegion();
                        }
                    }
                }
                if (modelE5 != null) {
                    armyModelTR5 = getMainGame().getImgLists().getTextureByName(modelE5.get("defaultImgName"));
                    armyModelRegion5 = armyModelTR5.getTextureRegion();
                      modelType = modelE5.getInt("modelType");
                    if (modelType == 1) {//固定方向的兵模
                        armyModelRegionF5 = armyModelRegion5;
                    } else if(modelType==2||modelType==0){
                        armyModelRegionF5 = getMainGame().getImgLists().getFlipRegion(armyModelTR5.getName());
                    }else if(modelType==3){
                        armyModelRegionF5 = getMainGame().getImgLists().getTextureByName(modelE5.get("flipImgName")).getTextureRegion();
                    }
                     bgName = modelE5.get("bgImgName", "");
                    if (!ComUtil.isEmpty(bgName)) {
                        bgArmyModelTR5 = getMainGame().getImgLists().getTextureByName(modelE5.get("bgImgName"));
                        bgArmyModelRegion5 = bgArmyModelTR5.getTextureRegion();
                        modelType = modelE5.getInt("bgImgModelType", 0);
                        if (modelType == 1) {//固定方向的兵模
                            bgArmyModelRegionF5 = bgArmyModelRegion5;
                        } else if(modelType==2||modelType==0){
                            bgArmyModelRegionF5 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR5.getName());
                        }else if(modelType==3){
                            bgArmyModelRegionF5 = getMainGame().getImgLists().getTextureByName(modelE5.get("flipBgImgName")).getTextureRegion();
                        }
                    }
                }
                if (modelE6 != null) {
                    armyModelTR6 = getMainGame().getImgLists().getTextureByName(modelE6.get("defaultImgName"));
                    armyModelRegion6 = armyModelTR6.getTextureRegion();
                      modelType = modelE6.getInt("modelType");
                    if (modelType == 1) {//固定方向的兵模
                        armyModelRegionF6 = armyModelRegion6;
                    } else if(modelType==2||modelType==0){
                        armyModelRegionF6 = getMainGame().getImgLists().getFlipRegion(armyModelTR6.getName());
                    }else if(modelType==3){
                        armyModelRegionF6 = getMainGame().getImgLists().getTextureByName(modelE6.get("flipImgName")).getTextureRegion();
                    }
                    bgName = modelE6.get("bgImgName", "");
                    if (!ComUtil.isEmpty(bgName)) {
                        bgArmyModelTR6 = getMainGame().getImgLists().getTextureByName(modelE6.get("bgImgName"));
                        bgArmyModelRegion6 = bgArmyModelTR6.getTextureRegion();
                        modelType = modelE6.getInt("bgImgModelType", 0);
                        if (modelType == 1) {//固定方向的兵模
                            bgArmyModelRegionF6 = bgArmyModelRegion6;
                        } else if(modelType==2||modelType==0){
                            bgArmyModelRegionF6 = getMainGame().getImgLists().getFlipRegion(bgArmyModelTR6.getName());
                        }else if(modelType==3){
                            bgArmyModelRegionF6 = getMainGame().getImgLists().getTextureByName(modelE6.get("flipBgImgName")).getTextureRegion();
                        }
                    }
                }
            }*/

        armyMarkTR = getMainGame().getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(armyData.getUnitArmyId0()));
        this.armyMarkRegion = armyMarkTR.getTextureRegion();
        scArmyRegion = getMainGame().getImgLists().getTextureByName(DefDAO.getSCNameByArmyId(armyData.getUnitArmyId0())).getTextureRegion();
        int formationId=DefDAO.getArmyFormationId(armyData);
        if(formationId>0){
            scArmyFormation = getMainGame().getImgLists().getTextureByName("mark_formation_" + formationId).getTextureRegion();
        }else {
            scArmyFormation = null;
        }

        //scArmyFormation=getMainGame().getImgLists().getTextureByName("mark_formation_63").getTextureRegion();
        updArmyModelDirect();
    }

    private void updRank() {
        /*if(armyData.getHexagonIndex()==43985){
            int s=0;
        }*/
        generalData = armyData.getGeneralData();
        if (armyData.getGeneralIndex() != 0) {
            ifGeneral = true;
            if (btl.masterData.getPlayerMode() == 2||!getMainGame().resGameConfig.enableGeneral) {//军团模式下
                if (generalData.getState() == 0) {//将军
                    rank = getMainGame().getImgLists().getTextureByName("level_ngeneral");
                } else {
                    rank = getMainGame().getImgLists().getTextureByName("level_rgeneral");
                }
            } else {
                rank = getMainGame().getImgLists().getTextureByName("g_" + armyData.getLegionData().getMedal()); //TODO
            }
        } else if (armyData.getArmyRank() > 0) {
            ifGeneral = false;
            rank = getMainGame().getImgLists().getTextureByName(DefDAO.getLevelMark(armyData.getArmyRank()));
        } else {
            ifGeneral = false;
            rank = null;
        }
        if (getMainGame().resGameConfig.enableGeneral&&generalData.getState() == 0 && ifGeneral) {
            smallGeneral = getMainGame().getImgLists().getTextureByName(generalData.getSmallGeneralImageName()).getTextureRegion();
            smallGeneralBg = getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForSmallGeneralBg(generalData.getCamp())).getTextureRegion();
        } else {
            smallGeneral = null;
        }
        if (smallGeneral != null) {
            sgArmyHpRegionDAO = getMainGame().getImgLists().getTextureByName("sg_" + DefDAO.getImageNameForArmyHp(getMainGame().getSMapDAO(), armyData));
            int hw = armyHpRegionDAO.getTextureRegion().getRegionWidth();
            float hpScale = armyData.getArmyHpNow() * 1f / armyData.getArmyHpMax();
            int hy = (int) (hw - hw * hpScale);
            sgArmyHpRegion = sgArmyHpRegionDAO.getNewRegion(0, 0, hw, hw - hy);
        }
    }

    public ArmyActor() {
    }


    public void init(Fb2Smap btl, MainGame game, CamerDAO cam, int mapW, float scale, int hexagon, int armyId, SMapGameStage.SMapPublicRescource resource) {
        if (this.btl == null) {
            this.btl = btl;
            setMainGame(game);
            this.mapW = mapW;
        }
        this.cam = cam;
        //this.hexagon=hexagon;
        armyMarkTR = game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(armyId));

        this.armyMarkRegion = armyMarkTR.getTextureRegion();
        //this.loopState =0;
        this.resource = resource;
        setSize(this.resource.armyBottomRegionDAO.getW(), this.resource.armyBottomRegionDAO.getH());
        setScale(scale);
        this.scale = scale;
        armyData = btl.armyHDatas.get(hexagon);
        armyData.armyActor = this;
     Fb2Map.MapHexagon hexagonData = armyData.getHexagonData();
        generalData = armyData.getGeneralData();
        //initPotionById();
        setX(hexagonData.source_x);
        setY(hexagonData.source_y);
        //markRefX=-armyMarkTR.getRefx()*scale;
        //markRefY=-armyMarkTR.getRefy()*scale;
        Fb2Smap.LegionData al = armyData.getLegionData();


        updArmyModel();
        flag = game.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(al.getCountryId())).getTextureRegion();
        flagW = flag.getRegionWidth() * 0.4f;
        flagH = flag.getRegionHeight() * 0.4f;

        armyHpRegionDAO = game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyHp(game.getSMapDAO(), armyData));

        scArmyBorderRegion = game.getImgLists().getTextureByName(DefDAO.getImageNameForSCBorder(game.getSMapDAO(), armyData.getLegionIndex())).getTextureRegion();
        int hw = armyHpRegionDAO.getTextureRegion().getRegionWidth();
        float hpScale = armyData.getArmyHpNow() * 1f / armyData.getArmyHpMax();

        int hy = (int) (hw - hw * hpScale);
        armyHpRegion = armyHpRegionDAO.getNewRegion(0, 0, hw, hw - hy);
        //chassisRegion=game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyChassis(game.getSMapDAO(),armyData)).getTextureRegion();
        legionColor = al.getColor();
        legionFogColor = al.getFogColor();
        energyRegionDAO = game.getImgLists().getTextureByName("markR_energy");
        armyStateDAO = game.getImgLists().getTextureByName("army_state");
        updRank();
        update();
    }


    public boolean ifDraw() {
        if (armyMarkRegion == null || !cam.ifDrawHexagon(armyData.getHexagonIndex()) || !isVisible() || armyData == null || armyData.getArmyHpNow() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);
        } catch (Exception e) {
          Fb2Map.MapHexagon hexagonData= armyData.getHexagonData();
            setPosition(hexagonData.source_x, hexagonData.source_y);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (true || resource.drawType > 1 || armyMarkRegion == null || !cam.ifDrawHexagon(armyData.getHexagonIndex()) || !isVisible() || (resource.drawType == 1 && resource.armyActorState == 0) || armyData == null || armyData.getArmyHpNow() == 0) {
            return;
        }
        // 还原 batch 原本的 Color
        // batch.setColor(tempBatchColor);
    }

    public void batchSoldierCard(Batch batch) {
        float alphaFlash = 0.5f;
        float zoomChange= resource.getZoomChange();
        Color color = getColor();

        if (ifFlash) {
            alphaFlash = resource.getAlphaFlash();
        }
        float sourceX = getX() - resource.scBorderRegionDAO.getRefx() ;
        float sourceY = getY() - resource.scBorderRegionDAO.getRefy();

        if (cam.loopState == 1 || cam.loopState == 0) {

            if (ifDrawFormation&&scArmyFormation != null) {
                batch.setColor(legionFogColor.r, legionFogColor.g, legionFogColor.b, resource.getAlphaFlash());
                batch.draw(
                        scArmyFormation,
                        -18+(getX() - scArmyFormation.getRegionWidth() / 2) + (1 - zoomChange)*1.5f * scArmyFormation.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                        -18+(getY() - scArmyFormation.getRegionHeight() / 2) + (1 - zoomChange) *1.5f* scArmyFormation.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2 ,
                        getOriginX(), getOriginY(),
                        scArmyFormation.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, scArmyFormation.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                        zoomChange*1.5f , zoomChange*1.5f ,
                        getRotation()
                );
            }
            if(directArrowRegion!=null){
               // float tx=getMainGame().sMapScreen.tempActor.getX();
               // float ty=getMainGame().sMapScreen.tempActor.getY();
                batch.setColor(1, 1, 1, resource.getAlphaFlash()/2);
                batch.draw(
                        directArrowRegion,
                        -36+(getX() - directArrowRegion.getRegionWidth() / 2) + (1 - zoomChange)*2f * directArrowRegion.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                        -36+(getY() - directArrowRegion.getRegionHeight() / 2) + (1 - zoomChange) *2f* directArrowRegion.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2 ,
                        getOriginX(), getOriginY(),
                        directArrowRegion.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, directArrowRegion.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                        zoomChange*2f , zoomChange*2f ,
                        getRotation()
                );
            }
            batch.setColor(legionFogColor.r, legionFogColor.g, legionFogColor.b, 1f);

            batch.draw(
                    resource.scBorderRegionDAO.getTextureRegion(),
                    sourceX,-2+ sourceY,
                    getOriginX(), getOriginY(),
                    resource.scBorderRegionDAO.getTextureRegion().getRegionWidth(), resource.scBorderRegionDAO.getTextureRegion().getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
            if (ifFlash) {
                batch.setColor(color.r, color.g, color.b, alphaFlash);
            } else {
                batch.setColor(color);
            }


            batch.draw(
                    scArmyBorderRegion,
                    sourceX,-2+ sourceY,
                    getOriginX(), getOriginY(),
                    resource.scBorderRegionDAO.getTextureRegion().getRegionWidth(), resource.scBorderRegionDAO.getTextureRegion().getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );


            batch.setColor(legionColor.r, legionColor.g, legionColor.b, 1);
            batch.draw(
                    scArmyGroupRegion,
                    sourceX + 19, sourceY +40,
                    getOriginX(), getOriginY(),
                    scArmyGroupRegion.getRegionWidth(), scArmyGroupRegion.getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
            if(scArmyRegion!=null){
                batch.draw(
                        scArmyRegion,
                        sourceX + 7, sourceY + 16,
                        getOriginX(), getOriginY(),
                        scArmyRegion.getRegionWidth(), scArmyRegion.getRegionHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }

            if (smallGeneral != null) {
                batch.draw(
                        resource.sc_generalRegion,
                        sourceX + 7, sourceY + 37,
                        getOriginX(), getOriginY(),
                        resource.sc_generalRegion.getRegionWidth(), resource.sc_generalRegion.getRegionHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }

            //hp
            batch.setColor(Color.GREEN);
            batch.draw(
                    resource.sc_attributeBorderRegion,
                    sourceX + 6, sourceY + 9,
                    getOriginX(), getOriginY(),
                    resource.sc_attributeBorderRegion.getRegionWidth() * hpRate, resource.sc_attributeBorderRegion.getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );

            batch.setColor(moraleColor);
            //morale
            batch.draw(
                    resource.sc_attributeBorderRegion,
                    sourceX + 6, sourceY + 5,
                    getOriginX(), getOriginY(),
                    resource.sc_attributeBorderRegion.getRegionWidth() * armyData.getMoraleRate(), resource.sc_attributeBorderRegion.getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
            batch.setColor(color);


        }
        if (cam.loopState == 1 || cam.loopState == 2) {

            if (ifDrawFormation&&scArmyFormation != null) {
                batch.setColor(legionFogColor.r, legionFogColor.g, legionFogColor.b, resource.getAlphaFlash());
                batch.draw(
                        scArmyFormation,
                        cam.getMapW_px()-18+(getX() - scArmyFormation.getRegionWidth() / 2) + (1 - zoomChange)*1.5f * scArmyFormation.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                        -18+(getY() - scArmyFormation.getRegionHeight() / 2) + (1 - zoomChange) *1.5f* scArmyFormation.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2 ,
                        getOriginX(), getOriginY(),
                        scArmyFormation.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, scArmyFormation.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                        zoomChange*1.5f , zoomChange*1.5f ,
                        getRotation()
                );
            }
            /**/

            if(directArrowRegion!=null){
                batch.setColor(1, 1, 1, resource.getAlphaFlash()/2);
                batch.draw(
                        directArrowRegion,
                        cam.getMapW_px()-36+(getX() - directArrowRegion.getRegionWidth() / 2) + (1 - zoomChange)*2f * directArrowRegion.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                        -36+(getY() - directArrowRegion.getRegionHeight() / 2) + (1 - zoomChange) *2f* directArrowRegion.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2 ,
                        getOriginX(), getOriginY(),
                        directArrowRegion.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, directArrowRegion.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                        zoomChange*2f , zoomChange*2f ,
                        getRotation()
                );
            }
            batch.setColor(legionFogColor.r, legionFogColor.g, legionFogColor.b, 1f);

            batch.draw(
                    resource.scBorderRegionDAO.getTextureRegion(),
                    cam.getMapW_px()+sourceX,-2+ sourceY,
                    getOriginX(), getOriginY(),
                    resource.scBorderRegionDAO.getTextureRegion().getRegionWidth(), resource.scBorderRegionDAO.getTextureRegion().getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
            if (ifFlash) {
                batch.setColor(color.r, color.g, color.b, alphaFlash);
            } else {
                batch.setColor(color);
            }


            batch.draw(
                    scArmyBorderRegion,
                    cam.getMapW_px()+sourceX,-2+ sourceY,
                    getOriginX(), getOriginY(),
                    resource.scBorderRegionDAO.getTextureRegion().getRegionWidth(), resource.scBorderRegionDAO.getTextureRegion().getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );


            batch.setColor(legionColor.r, legionColor.g, legionColor.b, 1);
            batch.draw(
                    scArmyGroupRegion,
                    cam.getMapW_px()+sourceX + 19, sourceY +40,
                    getOriginX(), getOriginY(),
                    scArmyGroupRegion.getRegionWidth(), scArmyGroupRegion.getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
            if(scArmyRegion!=null){
                batch.draw(
                        scArmyRegion,
                        cam.getMapW_px()+sourceX + 7, sourceY + 16,
                        getOriginX(), getOriginY(),
                        scArmyRegion.getRegionWidth(), scArmyRegion.getRegionHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if (smallGeneral != null) {
                batch.draw(
                        resource.sc_generalRegion,
                        cam.getMapW_px()+sourceX + 7, sourceY + 37,
                        getOriginX(), getOriginY(),
                        resource.sc_generalRegion.getRegionWidth(), resource.sc_generalRegion.getRegionHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }

            //hp
            batch.setColor(Color.GREEN);
            batch.draw(
                    resource.sc_attributeBorderRegion,
                    cam.getMapW_px()+sourceX + 6, sourceY + 9,
                    getOriginX(), getOriginY(),
                    resource.sc_attributeBorderRegion.getRegionWidth() * hpRate, resource.sc_attributeBorderRegion.getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );

            batch.setColor(moraleColor);
            //morale
            batch.draw(
                    resource.sc_attributeBorderRegion,
                    cam.getMapW_px()+sourceX + 6, sourceY + 5,
                    getOriginX(), getOriginY(),
                    resource.sc_attributeBorderRegion.getRegionWidth() * armyData.getMoraleRate(), resource.sc_attributeBorderRegion.getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
            batch.setColor(color);
        }

        if (ifDrawAir) {
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        resource.airRegionDAO.getTextureRegion(),
                        //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                        getX() +20 , -35+getY()  ,
                        resource.airRegionDAO.getRefx(),  resource.airRegionDAO.getRefy(),
                        resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(
                        resource.airRegionDAO.getTextureRegion(),
                        //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                        cam.getMapW_px() +getX() + 20, -35+getY()  ,
                        resource.airRegionDAO.getRefx(),  resource.airRegionDAO.getRefy(),
                        resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
        }
        if (ifDrawNul) {
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        resource.nulRegionDAO.getTextureRegion(),
                        -36+getX(), -35+getY() ,
                        resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                        resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(
                        resource.nulRegionDAO.getTextureRegion(),
                        cam.getMapW_px()-36+getX(), -35+getY() ,
                        resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                        resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
        }
        if (ifDrawEnergy) {
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        energyRegionDAO.getTextureRegion(),
                        -4+getX() + - energyRegionDAO.getRefx() , -25+getY()  - energyRegionDAO.getRefy() ,
                        getOriginX(), getOriginY(),
                        energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(
                        energyRegionDAO.getTextureRegion(),
                        cam.getMapW_px()-4+getX() + - energyRegionDAO.getRefx() , -25+getY()  - energyRegionDAO.getRefy() ,
                        getOriginX(), getOriginY(),
                        energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
        }

        batch.setColor(color);
    }

    private void batchArmyMark(Batch batch, Color color, float parentAlpha, float alphaFlash, float zoomChange) {

        float scale = 1.0f;
        boolean ifDrawG = false;
        if (smallGeneral != null && cam.getZoom() > ResDefaultConfig.Map.MARK_ZOOM) {
            scale = cam.getZoom();
            ifDrawG = true;
        }
        if (resource.armyActorState == 1) {
            if (!ifDrawG) {
                return;
            }
            if (cam.loopState == 1 || cam.loopState == 0) {
                batch.draw(
                        smallGeneralBg,
                        getX() - 22, getY() - 22 + hpRefy * getScaleY() * scale,
                        30, 31,//手工测出来的.....
                        //game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                        smallGeneralBg.getRegionWidth(), smallGeneralBg.getRegionHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        180
                );
                batch.draw(
                        smallGeneral,
                        getX() - 6, getY() - 3,
                        getOriginX() + smallGeneral.getRegionWidth() / 2, getOriginY() + smallGeneral.getRegionHeight() / 2,
                        smallGeneral.getRegionWidth(), smallGeneral.getRegionHeight(),
                        getScaleX() * cam.getZoom(), getScaleY() * cam.getZoom(),
                        getRotation()
                );
                if (sgArmyHpRegion != null) {
                    batch.draw(
                            sgArmyHpRegion,
                            getX() - 22, getY() - 22 + (int) (hpRefy * getScaleY() * scale),
                            // getX() +getMainGame().sMapScreen.tempActor.getX(), getY() +getMainGame().sMapScreen.tempActor.getY()+ hpRefy * getScaleY()*scale   ,
                            30, 31,//手工测出来的.....
                            //game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                            sgArmyHpRegion.getRegionWidth(), sgArmyHpRegion.getRegionHeight(),
                            getScaleX() * scale, getScaleY() * scale,
                            180
                    );
                }
            }
            if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                batch.draw(
                        smallGeneralBg,
                        cam.getMapW_px() + getX() - 22, getY() - 22 + (int) (hpRefy * getScaleY() * scale),//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        30, 31,//手工测出来的.....
                        //game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                        sgArmyHpRegion.getRegionWidth(), sgArmyHpRegion.getRegionHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        180
                );
                batch.draw(
                        smallGeneral,
                        cam.getMapW_px() + getX() - 6, getY() - 3,
                        getOriginX() + smallGeneral.getRegionWidth() / 2, getOriginY() + smallGeneral.getRegionHeight() / 2,
                        smallGeneral.getRegionWidth(), smallGeneral.getRegionHeight(),
                        getScaleX() * cam.getZoom(), getScaleY() * cam.getZoom(),
                        getRotation()
                );
                if (sgArmyHpRegion != null) {
                    batch.draw(
                            sgArmyHpRegion,
                            cam.getMapW_px() + getX() - 22, getY() - 22 + (int) (hpRefy * getScaleY() * scale),//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                            30, 31,//手工测出来的.....
                            //game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                            sgArmyHpRegion.getRegionWidth(), sgArmyHpRegion.getRegionHeight(),
                            getScaleX() * scale, getScaleY() * scale,
                            180
                    );
                }
            }
        } else if (resource.armyActorState == 2) {

            if (!ifDrawG) {
                //绘制底牌

                /*   */
                batch.setColor(1f - legionColor.r, 1f - legionColor.g, 1f - legionColor.b, parentAlpha);

                // 结合演员的属性绘制表示演员的纹理区域
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            resource.armyBottomRegionDAO.getTextureRegion(),
                            getX() + resource.markRefx, getY() + resource.markRefy,
                            getOriginX(), getOriginY(),
                            getWidth(), getHeight(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                    batch.draw(
                            resource.armyBottomRegionDAO.getTextureRegion(),
                            cam.getMapW_px() + getX() + resource.markRefx, getY() + resource.markRefy,
                            getOriginX(), getOriginY(),
                            getWidth(), getHeight(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                batch.setColor(color.r, color.g, color.b, alphaFlash);


                //绘制兵牌或国旗
                //batch.setColor(color.r, color.g, color.b, 0.5f);
                if (cam.loopState == 0 || cam.loopState == 1) {
                    // 结合演员的属性绘制表示演员的纹理区域
                    batch.draw(
                            armyMarkRegion,
                            getX() - armyMarkTR.getRefx() + 15, getY() - armyMarkTR.getRefy() + 18,
                            getOriginX(), getOriginY(),
                            armyMarkRegion.getRegionWidth(), armyMarkRegion.getRegionHeight(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                    batch.draw(
                            armyMarkRegion,
                            cam.getMapW_px() + getX() - armyMarkTR.getRefx() + 15, getY() - armyMarkTR.getRefy() + 18,
                            getOriginX(), getOriginY(),
                            armyMarkRegion.getRegionWidth(), armyMarkRegion.getRegionHeight(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
            }

            batch.setColor(color.r, color.g, color.b, parentAlpha);

            if (rank != null) {//绘制军衔
                // 将演员的 Color 结合 parentAlpha 设置到 batch

                if (ifGeneral && armyData.getGameMode() != 2) {
                    if (!ifDrawG) {
                      /* if(ifFlash){
                           batch.setColor(color.r,color.g,color.b,alphaFlash);
                       }else {
                           batch.setColor(Color.WHITE);
                       }*/
                        if (cam.loopState == 1 || cam.loopState == 0) {
                            batch.draw(//+resource.markRefx
                                    rank.getTextureRegion(),
                                    getX() - rank.getRefx() * 0.8f + 16, getY() - rank.getRefy() * 0.8f + 21,
                                    getOriginX(), getOriginY(),
                                    rank.getW(), rank.getH(),
                                    getScaleX() * 0.8f, getScaleY() * 0.8f,
                                    getRotation()
                            );
                        }
                        if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                            batch.draw(
                                    rank.getTextureRegion(),
                                    cam.getMapW_px() + getX() - rank.getRefx() * 0.8f + 17, getY() - rank.getRefy() * 0.8f + 21,
                                    getOriginX(), getOriginY(),
                                    rank.getW(), rank.getH(),
                                    getScaleX() * 0.8f, getScaleY() * 0.8f,
                                    getRotation()
                            );
                        }
                        batch.setColor(color);
                    }
                } else {
                    if (cam.loopState == 1 || cam.loopState == 0) {
                        batch.draw(
                                rank.getTextureRegion(),
                                //getX()+resource.markRefx + 20, getY()+resource.markRefy + 16 ,
                                getX() - rank.getRefx() + 16, getY() - rank.getRefy() + 15,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                getScaleX(), getScaleY(),
                                getRotation()
                        );
                    }
                    if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                        batch.draw(
                                rank.getTextureRegion(),
                                cam.getMapW_px() + getX() - rank.getRefx() + 16, getY() - rank.getRefy() + 15,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                getScaleX(), getScaleY(),
                                getRotation()
                        );
                    }
                }
            }

            //绘制血条
            // 结合演员的属性绘制表示演员的纹理区域
            if (cam.loopState == 1 || cam.loopState == 0) {
                if (!ifDrawG) {
                    batch.draw(
                            armyHpRegion,
                            getX() - 19, getY() - 17 + hpRefy * getScaleY() * scale,
                            //    getX() + getMainGame().sMapScreen.tempActor.getX(), getY() + getMainGame().sMapScreen.tempActor.getY() + hpRefy * getScaleY() * scale,
                            30, 31,//手工测出来的.....
                            //,,
                            armyHpRegion.getRegionWidth(), armyHpRegion.getRegionHeight(),
                            getScaleX() * scale, getScaleY() * scale,
                            180
                    );
                }
                batch.setColor(moraleColor);

                batch.draw(
                        resource.moraleRegionDAO.getTextureRegion(),
                        getX() - 15 * scale, getY() - 3,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        getOriginX() + resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), getOriginY() + (resource.moraleRegionDAO.getTextureRegion().getRegionHeight()) / 2,//手工测出来的.....
                        resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY,
                        getScaleX() * scale, getScaleY() * scale,
                        getRotation()
                );
                batch.setColor(color);


                batch.setColor(color.r, color.g, color.b, 1.0f);
                if (groupDAO != null) {
                    if (smallGeneral == null) {
                        batch.draw(
                                groupDAO.getTextureRegion(),
                                getX() + (-28 + 15 + 21 + 23) * scale, getY() + -29 - 10 + 18 + 16,
                                getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                                groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() * scale, getScaleY() * scale,
                                getRotation()
                        );
                    } else {
                        batch.draw(
                                groupDAO.getTextureRegion(),
                                getX() + (-28 + 15 + 21 + 23 - 5) * scale, getY() + -29 - 10 + 18 + 16 - 7,
                                getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                                groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() * scale, getScaleY() * scale,
                                getRotation()
                        );
                    }
                }
            }


            if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                if (!ifDrawG) {
                    batch.draw(
                            armyHpRegion,
                            cam.getMapW_px() + getX() - 19, getY() - 17 + hpRefy * getScaleY() * scale,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                            30, 31,//手工测出来的.....
                            //game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                            armyHpRegion.getRegionWidth(), armyHpRegion.getRegionHeight(),
                            getScaleX() * scale, getScaleY() * scale,
                            180
                    );
                }
                batch.setColor(moraleColor);
                batch.draw(
                        resource.moraleRegionDAO.getTextureRegion(),
                        cam.getMapW_px() + getX() - 15 * scale, getY() - 3,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        getOriginX() + resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), getOriginY() + (resource.moraleRegionDAO.getTextureRegion().getRegionHeight()) / 2,//手工测出来的.....
                        resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY,
                        getScaleX() * scale, getScaleY() * scale,
                        getRotation()
                );
                batch.setColor(color);


                batch.setColor(color.r, color.g, color.b, 1.0f);
                if (groupDAO != null) {
                    if (smallGeneral == null) {
                        batch.draw(
                                groupDAO.getTextureRegion(),
                                cam.getMapW_px() + getX() + (-28 + 15 + 21 + 23) * scale, getY() + -29 - 10 + 18 + 16,
                                getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                                groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() * scale, getScaleY() * scale,
                                getRotation()
                        );
                    } else {
                        batch.draw(
                                groupDAO.getTextureRegion(),
                                cam.getMapW_px() + getX() + (-28 + 15 + 21 + 23 - 5) * scale, getY() + -29 - 10 + 18 + 16 - 7,
                                getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                                groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() * scale, getScaleY() * scale,
                                getRotation()
                        );
                    }
                }
            }

            if (ifPlayerCommand && targetState != 0) {//绘制目标
                // 结合演员的属性绘制表示演员的纹理区域
                if (cam.loopState == 1 || cam.loopState == 0) {
                    batch.draw(
                            targetRegionDAO.getTextureRegion(),
                            //getX()+markRefX - game.gameConfig.targetRefv , getY()+markRefY -game.gameConfig.targetRefv,
                            getX() + resource.markRefx, getY() + resource.markRefy,
                            targetRegionDAO.getRefx(), targetRegionDAO.getRefy(),
                            getWidth(), getHeight(),
                            getScaleX() * scale, getScaleY() * scale,
                            targetAngle
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                    batch.draw(

                            targetRegionDAO.getTextureRegion(),
                            cam.getMapW_px() + getX() + resource.markRefx, getY() + resource.markRefy,
                            targetRegionDAO.getRefx(), targetRegionDAO.getRefy(),
                            getWidth(), getHeight(),
                            getScaleX() * scale, getScaleY() * scale,
                            targetAngle
                    );
                }
            }


            if (ifDrawEnergy) {
                if (cam.loopState == 1 || cam.loopState == 0) {
                    batch.draw(
                            energyRegionDAO.getTextureRegion(),
                            getX() + -28 - energyRegionDAO.getRefx() + 15 + 15 + 10, getY() + -29 - energyRegionDAO.getRefy() - 10 + 18 + 16,
                            energyRegionDAO.getTextureRegion().getRegionWidth() / 2, energyRegionDAO.getTextureRegion().getRegionHeight() / 2,
                            energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                            (getScaleX() + cam.getZoom()) * scale, (getScaleY() + cam.getZoom()) * scale,
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {
                    batch.draw(
                            energyRegionDAO.getTextureRegion(),
                            cam.getMapW_px() + getX() + -28 - energyRegionDAO.getRefx() + 15 + 15 + 10, getY() + -29 - energyRegionDAO.getRefy() - 10 + 18 + 16,
                            energyRegionDAO.getTextureRegion().getRegionWidth() / 2, energyRegionDAO.getTextureRegion().getRegionHeight() / 2,
                            energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                            (getScaleX() + cam.getZoom()) * scale, (getScaleY() + cam.getZoom()) * scale,
                            getRotation()
                    );
                }
            }
        }

    }


    private void batchArmyModel(Batch batch, Color color, float parentAlpha, float alphaFlash, float zoomChange) {
        batch.setColor(Color.WHITE);
        if (resource.armyActorState == 1) {//bt_army

            if (targetState == 3) {
                batch.setColor(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, alphaFlash);
            } else {
                batch.setColor(1, 1, 1, alphaFlash);
            }
            if (ifFlash && ifPlayerCommand && armyData.getArmyRound() == 0) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            armyStateDAO.getTextureRegion(),
                            (getX() + 15 - armyStateDAO.getRefx() + 50) + (1 - zoomChange) * armyStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                            (getY() + 18 - armyStateDAO.getRefy() + 32) + (1 - zoomChange) * armyStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2,
                            getOriginX(), getOriginY(),
                            armyStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange, zoomChange,
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {
                    batch.draw(
                            armyStateDAO.getTextureRegion(),
                            cam.getMapW_px() + (getX() + 15 - armyStateDAO.getRefx() + 50) + (1 - zoomChange) * armyStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                            (getY() + 18 - armyStateDAO.getRefy() + 32) + (1 - zoomChange) * armyStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2,
                            getOriginX(), getOriginY(),
                            armyStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange, zoomChange,
                            getRotation()
                    );
                }
            }

            batch.setColor(1, 1, 1, 1);

            if (armyData.isPlayer() && !ifFlash) {
                batch.setColor(Color.LIGHT_GRAY);
            } else {
                batch.setColor(DefDAO.getColorByModelMorale(getMainGame(), armyData.getArmyMorale()));
            }
            //setArmyModel(0,armyModelTR,armyModelR,armyModelRegion,armyModelRegionF,bgArmyModelTR,bgArmyModelR,bgArmyModelRegion,bgArmyModelRegionF);

            if (armyData.isUnitGroup()) {//组合部队

                if (cam.loopState == 0 || cam.loopState == 1) {
                    //战线  1↖ 2↑ 3↗ 4↙ 5↓ 6↘  位置 1← 2↖ 3↗ 4→ 5↙ 6↘ 绘制顺序 2,3,1,0,4,5,6
                    if (modelE2 != null && armyModelR2 != null) {
                        if (bgArmyModelTR2 != null) {
                            batch.draw(
                                    bgArmyModelR2,
                                    getX() + bgModelRefX2 + 10, getY() + bgModelRefY2 + 33,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR2.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR2.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale2, getScaleY() * modelScale2,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR2,
                                getX() + modelRefX2 + 10, getY() + modelRefY2 + 33,
                                getOriginX(), getOriginY(),
                                armyModelR2.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR2.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale2, getScaleY() * bgModelScale2,
                                getRotation()
                        );
                    }
                    if (modelE3 != null && armyModelR3 != null) {
                        if (bgArmyModelTR3 != null) {
                            batch.draw(
                                    bgArmyModelR3,
                                    getX() + bgModelRefX3 + 32, getY() + bgModelRefY3 + 33,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR3.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR3.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale3, getScaleY() * modelScale3,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR3,
                                getX() + modelRefX3 + 32, getY() + modelRefY3 + 33,
                                getOriginX(), getOriginY(),
                                armyModelR3.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR3.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale3, getScaleY() * bgModelScale3,
                                getRotation()
                        );
                    }
                    if (modelE1 != null && armyModelR1 != null) {
                        if (bgArmyModelTR1 != null) {
                            batch.draw(
                                    bgArmyModelR1,
                                    getX() + bgModelRefX1 - 1, getY() + bgModelRefY1 + 16,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR1.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR1.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale1, getScaleY() * modelScale1,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR1,
                                getX() + modelRefX1 - 1, getY() + modelRefY1 + 16,
                                getOriginX(), getOriginY(),
                                armyModelR1.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR1.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale1, getScaleY() * bgModelScale1,
                                getRotation()
                        );
                    }
                    if (bgArmyModelTR != null) {
                        batch.draw(
                                bgArmyModelR,
                                getX() + bgModelRefX0 + 21, getY() + bgModelRefY0 + 16,
                                getOriginX(), getOriginY(),
                                bgArmyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * modelScale0, getScaleY() * modelScale0,
                                getRotation()
                        );
                    }
                    batch.draw(
                            armyModelR,
                            getX() + modelRefX0 + 21, getY() + modelRefY0 + 16,//36 27
                            getOriginX(), getOriginY(),
                            armyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            getScaleX() * bgModelScale0, getScaleY() * bgModelScale0,
                            getRotation()
                    );
                    if (modelE4 != null && armyModelR4 != null) {
                        if (bgArmyModelTR4 != null) {
                            batch.draw(
                                    bgArmyModelR4,
                                    getX() + bgModelRefX4 + 43, getY() + bgModelRefY4 + 16,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR4.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR4.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale4, getScaleY() * modelScale4,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR4,
                                getX() + modelRefX4 + 43, getY() + modelRefY4 + 16,
                                getOriginX(), getOriginY(),
                                armyModelR4.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR4.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale4, getScaleY() * bgModelScale4,
                                getRotation()
                        );
                    }
                    if (modelE5 != null && armyModelR5 != null) {
                        if (bgArmyModelTR5 != null) {
                            batch.draw(
                                    bgArmyModelR5,
                                    getX() + bgModelRefX5 + 10, getY() + bgModelRefY5 - 1,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR5.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR5.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale5, getScaleY() * modelScale5,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR5,
                                getX() + modelRefX5 + 10, getY() + modelRefY5 - 1,
                                getOriginX(), getOriginY(),
                                armyModelR5.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR5.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale5, getScaleY() * bgModelScale5,
                                getRotation()
                        );
                    }
                    if (modelE6 != null && armyModelR6 != null) {
                        if (bgArmyModelTR6 != null) {
                            batch.draw(
                                    bgArmyModelR6,
                                    getX() + bgModelRefX6 + 32, getY() + bgModelRefY6 - 1,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR6.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR6.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale6, getScaleY() * modelScale6,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR6,
                                getX() + modelRefX6 + 32, getY() + modelRefY6 - 1,
                                getOriginX(), getOriginY(),
                                armyModelR6.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR6.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale6, getScaleY() * bgModelScale6,
                                getRotation()
                        );
                    }
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分 TODO
                }
            } else {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    if (bgArmyModelTR != null) {
                        batch.draw(
                                bgArmyModelR,
                                getX() + bgModelRefX0 + 21, getY() + bgModelRefY0 + 18,
                                getOriginX(), getOriginY(),
                                bgArmyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * modelScale0, getScaleY() * modelScale0,
                                getRotation()
                        );
                    }
                    // 结合演员的属性绘制表示演员的纹理区域
                    batch.draw(
                            armyModelR,
                            getX() + modelRefX0 + 21, getY() + modelRefY0 + 18,
                            getOriginX(), getOriginY(),
                            armyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            getScaleX() * bgModelScale0, getScaleY() * bgModelScale0,
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                    if (bgArmyModelTR != null) {
                        batch.draw(
                                bgArmyModelR,
                                cam.getMapW_px() + getX() + bgModelRefX0 + 21, getY() + bgModelRefY0 + 18,
                                getOriginX(), getOriginY(),
                                bgArmyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * modelScale0, getScaleY() * modelScale0,
                                getRotation()
                        );
                    }
                    batch.draw(
                            armyModelR,
                            cam.getMapW_px() + getX() + modelRefX0 + 21, getY() + modelRefY0 + 18,
                            getOriginX(), getOriginY(),
                            armyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            getScaleX() * bgModelScale0, getScaleY() * bgModelScale0,
                            getRotation()
                    );
                }
            }
            batch.setColor(color);
        } else if (resource.armyActorState == 2) {//bt_flags
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        flag,
                        getX() + 9, getY() - 13,
                        //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                        getOriginX(), getOriginY(),
                        flagW, flagH,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if (cam.loopState == 1 || cam.loopState == 2) {
                batch.draw(
                        flag,
                        getX() + 9 + cam.getMapW_px(), getY() - 13,
                        // getX()+getMapW_px()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                        getOriginX(), getOriginY(),
                        flagW, flagH,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
        } else if (resource.armyActorState == 3) {


            batch.setColor(color);
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        armyHpRegion,
                        //getX()-getMainGame().sMapScreen.tempActor.getX(), getY()-getMainGame().sMapScreen.tempActor.getY()+ hpRefy*getScaleY()*0.4f,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        getX() - 13, getY() - 37 + hpRefy * getScaleY() * 0.4f,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        21, 22,//手工测出来的.....
                        // getMainGame.sMapScreen.tempActor.getX(),getMainGame().sMapScreen.tempActor.getY(),
                        flagW, hpH,
                        getScaleX(), getScaleY(),
                        180
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(
                        armyHpRegion,
                        cam.getMapW_px() + getX() - 13, getY() - 37 + hpRefy * getScaleY() * 0.4f,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        21, 22,//手工测出来的.....
                        // game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                        flagW, hpH,
                        getScaleX(), getScaleY(),
                        180
                );
            }


            batch.setColor(moraleColor);
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        resource.moraleRegionDAO.getTextureRegion(),
                        getX() + 5, getY() - 10,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        getOriginX(), getOriginY(),//手工测出来的.....
                        resource.moraleRegionDAO.getTextureRegion().getRegionWidth() * 0.4f, (resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY) * 0.4f,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(
                        resource.moraleRegionDAO.getTextureRegion(),
                        cam.getMapW_px() + getX() + 5, getY() - 10,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        getOriginX(), getOriginY(),//手工测出来的.....
                        resource.moraleRegionDAO.getTextureRegion().getRegionWidth() * 0.4f, (resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY) * 0.4f,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }

            batch.setColor(color);


            if (ifDrawAir) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            resource.airRegionDAO.getTextureRegion(),
                            //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                            getX() - resource.airRegionDAO.getRefx() +31, getY()  - resource.airRegionDAO.getRefy() - 5,
                            getOriginX(), getOriginY(),
                            resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            resource.airRegionDAO.getTextureRegion(),
                            //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                            cam.getMapW_px() + getX() - resource.airRegionDAO.getRefx() +31, getY()  - resource.airRegionDAO.getRefy() - 5,
                            getOriginX(), getOriginY(),
                            resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
            }
            if (ifDrawNul) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            resource.nulRegionDAO.getTextureRegion(),
                            getX()  - resource.nulRegionDAO.getRefx() -17, getY()  - resource.nulRegionDAO.getRefy() - 5,
                            getOriginX(), getOriginY(),
                            resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            resource.nulRegionDAO.getTextureRegion(),
                            cam.getMapW_px() + getX()  - resource.nulRegionDAO.getRefx() -17, getY()  - resource.nulRegionDAO.getRefy() - 5,
                            getOriginX(), getOriginY(),
                            resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
            }
            if (ifDrawEnergy) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            energyRegionDAO.getTextureRegion(),
                            getX()  - energyRegionDAO.getRefx() + 15 , getY()  - energyRegionDAO.getRefy() - 5,
                            getOriginX(), getOriginY(),
                            energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            energyRegionDAO.getTextureRegion(),
                            cam.getMapW_px() + getX()  - energyRegionDAO.getRefx() + 15 , getY()  - energyRegionDAO.getRefy() - 5,
                            getOriginX(), getOriginY(),
                            energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
            }

            batch.setColor(1, 1, 1, 1);

            if (groupDAO != null) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            groupDAO.getTextureRegion(),
                            getX() + 27, getY() + -12,
                            getOriginX(), getOriginY(),
                            groupDAO.getTextureRegion().getRegionWidth() * 0.7f, groupDAO.getTextureRegion().getRegionHeight() * 0.7f,
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            groupDAO.getTextureRegion(),
                            cam.getMapW_px() + getX() + 27, getY() + -12,
                            getOriginX(), getOriginY(),
                            groupDAO.getTextureRegion().getRegionWidth() * 0.7f, groupDAO.getTextureRegion().getRegionHeight() * 0.7f,
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
            }
            //大比例时的军衔绘制
            if (rank != null) {//绘制军衔
                // 将演员的 Color 结合 parentAlpha 设置到 batch

                if (ifGeneral) {
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        batch.draw(//+resource.markRefx
                                rank.getTextureRegion(),
                                getX() - rank.getRefx() * 0.3f + 19, getY() - rank.getRefy() * 0.3f + 27,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                0.3f, 0.3f,
                                getRotation()
                        );
                    }
                    if (cam.loopState == 2 || cam.loopState == 1) {
                        batch.draw(//+resource.markRefx
                                rank.getTextureRegion(),
                                cam.getMapW_px() + getX() - rank.getRefx() * 0.3f + 19, getY() - rank.getRefy() * 0.3f + 27,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                0.3f, 0.3f,
                                getRotation()
                        );
                    }
                } else {
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        batch.draw(//+resource.markRefx
                                rank.getTextureRegion(),
                                getX() - rank.getRefx() + 19, getY() - rank.getRefy() + 15,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                getScaleX(), getScaleY(),
                                getRotation()
                        );
                    }
                    if (cam.loopState == 2 || cam.loopState == 1) {
                        batch.draw(//+resource.markRefx
                                rank.getTextureRegion(),
                                cam.getMapW_px() + getX() - rank.getRefx() + 19, getY() - rank.getRefy() + 15,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                getScaleX(), getScaleY(),
                                getRotation()
                        );
                    }
                }


            }


        } else if (resource.armyActorState == 4) {
            if (smallGeneral == null) {
                return;
            }
            if (resource.needLucency(getHexagon())) {
                return;
            }
            if (cam.loopState == 1 || cam.loopState == 0) {
                batch.draw(
                        resource.smallBgArrowRegionDAO.getTextureRegion(),
                        getX() + 11, getY() + 33,
                        getOriginX(), getOriginY(),
                        resource.smallBgArrowRegionDAO.getW(), resource.smallBgArrowRegionDAO.getH(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
                batch.draw(
                        smallGeneralBg,
                        getX() + 2, getY() + 40,
                        getOriginX(), getOriginY(),
                        smallGeneralBg.getRegionWidth(), smallGeneralBg.getRegionHeight(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
                batch.draw(
                        smallGeneral,
                        getX() + 5, getY() + 43,
                        getOriginX(), getOriginY(),
                        smallGeneral.getRegionWidth(), smallGeneral.getRegionHeight(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
            }
            if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                batch.draw(
                        resource.smallBgArrowRegionDAO.getTextureRegion(),
                        cam.getMapW_px() + getX() + 11, getY() + 33,
                        getOriginX(), getOriginY(),
                        resource.smallBgArrowRegionDAO.getW(), resource.smallBgArrowRegionDAO.getH(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
                batch.draw(
                        smallGeneralBg,
                        cam.getMapW_px() + getX() + 2, getY() + 40,
                        getOriginX(), getOriginY(),
                        smallGeneralBg.getRegionWidth(), smallGeneralBg.getRegionHeight(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
                batch.draw(
                        smallGeneral,
                        cam.getMapW_px() + getX() + 5, getY() + 43,
                        getOriginX(), getOriginY(),
                        smallGeneral.getRegionWidth(), smallGeneral.getRegionHeight(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
            }
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
        int  x = (armyData.getHexagonIndex() % btl.masterData.getWidth()) + 1;
        int  y = (armyData.getHexagonIndex() / btl.masterData.getWidth()) + 1;
        sourceX= GameMap.getX_pxByHexagon(x,scale,0);
        sourceY=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        //核心坐标
    }*/

    public void logInfo() {
       /* Gdx.app.log("armyActor","hpRate:"+hpRate+
                " h:"+armyHpRegion.getSprite().getRegionHeight()+
                " hpRefy:"+(armyHpRegion.getSprite().getRegionHeight()-armyHpRegion.getSprite().getRegionHeight())
        +" y:"+getHY()
        +" armyHpRegion_y:"+armyHpRegion_y);*/
        Gdx.app.log("armyActorAttribute", armyData.getAllAttributes());
    }


    public void updHpColor() {
        Fb2Smap.LegionData al = armyData.getLegionData();
        flag = getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(al.getCountryId())).getTextureRegion();
        armyHpRegionDAO = getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForArmyHp(getMainGame().getSMapDAO(), armyData));

        int hw = armyHpRegionDAO.getTextureRegion().getRegionWidth();
        float hpScale = armyData.getArmyHpNow() * 1f / armyData.getArmyHpMax();
        int hy = (int) (hw - hw * hpScale);
        armyHpRegion = armyHpRegionDAO.getNewRegion(0, 0, hw, hw - hy);
        //chassisRegion=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForArmyChassis(getMainGame().getSMapDAO(),armyData)).getTextureRegion();
        armyHpRegion.setRegionHeight((int) (armyHpRegionDAO.getH() * hpRate));
        hpRate = (float) armyData.getArmyHpNow() / armyData.getArmyHpMax();
        hpH = (int) (armyHpRegionDAO.getH() * hpRate * 0.4f);
        hpRefy = (int) (armyHpRegion.getRegionHeight() - armyHpRegionDAO.getH());
    }


    public boolean updateMorale() {
        if (armyData.getArmyHpNow() <= 0) {
            armyData.armyDeath(true);
            return false;
        }
        moraleRefY = (100 - armyData.getArmyMorale()) * resource.moraleRegionDAO.getH() / 100;
        resetFlash();
        /*if(armyData.getHexagonIndex()==23604){
            int s=0;
        }*/
        moraleColor = DefDAO.getColorByMoraleBar(getMainGame(), armyData.getArmyMorale());
        return true;
    }

    public boolean update() {

        if (armyData.getArmyHpNow() <= 0) {
            armyData.armyDeath(true);
            return false;
        }
        moraleRefY = (100 - armyData.getArmyMorale()) * resource.moraleRegionDAO.getH() / 100;

        ifPlayerCommand = armyData.playerCanCommand();

        hpRate = (float) armyData.getArmyHpNow() / armyData.getArmyHpMax();
        if(armyHpRegionDAO==null||armyHpRegion==null){
           updHpColor();
        }
        armyHpRegion.setRegionHeight((int) (armyHpRegionDAO.getH() * hpRate));
        hpH = (int) (armyHpRegionDAO.getH() * hpRate * 0.4f);
        hpRefy = (int) (armyHpRegion.getRegionHeight() - armyHpRegionDAO.getH());
        scArmyBorderRegion = getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForSCBorder(getMainGame().getSMapDAO(), armyData.getLegionIndex())).getTextureRegion();
        int group = armyData.getUnitGroupPower();
        if (armyData.isUnitGroup()) {
            if (group == 0) {
            } else if (group > 9) {
                groupDAO = getMainGame().getImgLists().getTextureByName("icon_max");
            } else {
                groupDAO = getMainGame().getImgLists().getTextureByName(DefDAO.getNumberIcon(group));
            }
        } else {
            if (armyData.getUnitWealv0Value() > 0) {
                if (armyData.getUnitWealv0Value() == armyData.getUnitTechLv(0)) {
                    groupDAO = getMainGame().getImgLists().getTextureByName("icon_max");
                } else {
                    groupDAO = getMainGame().getImgLists().getTextureByName(DefDAO.getNumberIcon(armyData.getUnitWealv0Value()));
                }
            }
        }

        scArmyGroupRegion = getMainGame().getImgLists().getTextureByName(DefDAO.getSCGroupImageName(group)).getTextureRegion();
        if (ifPlayerCommand) {
            resetTarget();
        }
        int arrowdirect=armyData.getArrowDirect();
        if(arrowdirect>0){
            directArrowRegion= getMainGame().getImgLists().getTextureByName("direct_arrow_"+arrowdirect).getTextureRegion();
           // directArrowRegion= getMainGame().getImgLists().getTextureByName("hexagon1").getTextureRegion();
        }else {
            directArrowRegion=null;
        }


        /* if(game.gameConfig.getIfAnimation()){*/

        /*}*/


        updDrawInfo();
        updRank();
        updateMorale();
        resetUpdMark();
        updArmyModelDirect();
        return true;
    }

    public void resetUpdMark() {
         if (armyData.isPlayer() && armyData.getGameMode() < 2) {
            if (armyData.legionCanUpdLv()) {
                ifCanUpd = true;
            } else {
                ifCanUpd = false;
            }
        } else {
            ifCanUpd = false;
        }
    }

    public boolean getIfCanUpd(){
        if(!getMainGame().gameConfig.getIfPromptUnitUpd()){
            return false;
        }else if(armyData.isPlayer()&&armyData.getPlayerLegionData().getMoney()<100){
            return false;
        }else {
            return ifCanUpd;
        }
    }


    public void updDrawInfo() {
        if (armyData.isPlayerAlly() || armyData.isEditMode(true)) {
            Fb2Smap.BuildData b = armyData.getHBuildData();
            if (b != null) {
                ifDrawAir = armyData.getAirCount() > 0 || b.getBuildActor().ifDrawAir;
                ifDrawNul = armyData.getNucleIndex() != -1 || b.getBuildActor().ifDrawNul;
                ifDrawEnergy = b.getBuildActor().ifDrawEnergy;
            } else {
                ifDrawAir = armyData.getAirCount() > 0;
                ifDrawNul = armyData.getNucleIndex() != -1;
                ifDrawEnergy = false;
            }
        } else {
            ifDrawAir = false;
            ifDrawNul = false;
            ifDrawEnergy = false;
        }
        if(armyData.isUnitGroup()&&armyData.getUnitGroup()>1&&!armyData.potionIsSea()){
            ifDrawFormation=true;
        }else {
            ifDrawFormation=false;
        }
        resetFlash();
    }

    public void setFlash(boolean v) {
        ifFlash = v;
    }

    public int getHexagon() {
        return armyData.getHexagonIndex();
    }


    //targetState  0不显示 1 红箭头 2蓝箭头 3点中  4标记(无箭头灰色圈)
    public void setTargetState(int state, float angle) {
        switch (state) {
            case 1:
                targetRegionDAO = getMainGame().getImgLists().getTextureByName("army_target_r");
                break;
            case 2:
                targetRegionDAO = getMainGame().getImgLists().getTextureByName("army_target_g");
                break;
            case 3:
                targetRegionDAO = getMainGame().getImgLists().getTextureByName("army_click");
                break;
            case 4:
                targetRegionDAO = getMainGame().getImgLists().getTextureByName("army_target_n");
                break;
        }
        targetState = state;
        targetAngle = angle;
    }

    //ifOccupy 只影响播放的音效
    public void actorMoveHexagon(int id,boolean ifOccupy) {
        Fb2Map.MapHexagon hexagonData=armyData.getHexagonData();
        Fb2Smap.BuildData b1 = btl.getBuildDataByRegion(hexagonData.getHexagonIndex());
        Fb2Smap.BuildData b2 = btl.getBuildDataByRegion(id);
        updArmyModelDirect();
        //2.位移
       /* if (armyData.getArmyType() != 4 && armyData.getArmyType() != 8 && ((btl.ifSea(hexagonData.getHexagonIndex()) && !btl.ifSea(id)) || (!btl.ifSea(hexagonData.getHexagonIndex()) && btl.ifSea(id)))) {

        }*/
        updArmyModel();
        //updateDrawFort(hexagonData.getHexagonIndex());
        hexagonData = armyData.getHexagonData();
        //updateDrawFort(hexagonData.getHexagonIndex());
        updRank();
        //initPotionById();
        if(resource.hexagons.contains(armyData.getHexagonIndex())){
            if((armyData.getRoundState()==0||(armyData.getRoundState()==4&&armyData.isPlayer()))){
                try {
                    MoveToAction action = Actions.moveTo(hexagonData.source_x, hexagonData.source_y, 0.3f);
                    addAction(action);
                } catch (Exception e) {
                    setPosition(hexagonData.source_x, hexagonData.source_y);
                }
            }else{
                try {
                    MoveToAction action = Actions.moveTo(hexagonData.source_x, hexagonData.source_y, 0f);
                    addAction(action);
                } catch (Exception e) {
                    setPosition(hexagonData.source_x, hexagonData.source_y);
                }
            }
        }  else {
            setPosition(hexagonData.source_x, hexagonData.source_y);
        }


        if (armyData.isPlayerAlly()) {
            if (b1 != null) {
                b1.getBuildActor().updDrawInfo();
            }
            if (b2 != null) {
                b2.getBuildActor().updDrawInfo();
            }
        }
        if(armyData.isPlayerOperaRound()){
            if(ifOccupy){
                getMainGame().playSound(ResDefaultConfig.Sound.占领);
            }else if(armyData.potionIsSea()||armyData.getArmyType()==4||armyData.getArmyType()==8){
                getMainGame().playSound(ResDefaultConfig.Sound.海军移动);
            }else if(armyData.getArmyType()==1){
                getMainGame().playSound(ResDefaultConfig.Sound.步兵移动);
            }else if(armyData.getArmyType()==5||armyData.getArmyType()==7) {
                getMainGame().playSound(ResDefaultConfig.Sound.飞机移动);
            }else {
                getMainGame().playSound(ResDefaultConfig.Sound.装甲移动);
            }
        }


    }

    public void resetPotion(int id) {
        Fb2Map.MapHexagon hexagonData=armyData.getHexagonData();
        Fb2Smap.BuildData b1 = btl.getBuildDataByRegion(hexagonData.getHexagonIndex());
        Fb2Smap.BuildData b2 = btl.getBuildDataByRegion(id);
        updArmyModelDirect();
        //2.位移
        if (/*game.gameConfig.getIfAnimation()&&*/armyData.getArmyType() != 4 && armyData.getArmyType() != 8 && ((btl.ifSea(hexagonData.getHexagonIndex()) && !btl.ifSea(id)) || (!btl.ifSea(hexagonData.getHexagonIndex()) && btl.ifSea(id)))) {
            updArmyModel();
        }
        //updateDrawFort(hexagonData.getHexagonIndex());
        hexagonData = armyData.getHexagonData();
        //updateDrawFort(hexagonData.getHexagonIndex());
        updRank();
        setX(hexagonData.source_x);
        setY(hexagonData.source_y);
        // update();
        if (armyData.isPlayerAlly()) {
            if (b1 != null) {
                b1.getBuildActor().updDrawInfo();
            }
            if (b2 != null) {
                b2.getBuildActor().updDrawInfo();
            }
        }
        resetFlash();
    }

    public void resetFlash() {
       /* if (armyData.getHexagonIndex() == 9454) {
            int s = 0;
        }*/
        if (ifPlayerCommand && armyData.getArmyRound() == 0) {
            if (armyData.getGameMode() == 2) {
                if (armyData.getArmyType() == 6 || !armyData.isPlayer()) {
                    ifFlash = false;
                } else {
                    ifFlash = true;
                }
            } else {
                if (armyData.getArmyMorale() < getMainGame().resGameConfig.unitMoraleMinLimit) {
                    ifFlash = false;
                } else if (!armyData.ifHaveFeature(15) && !armyData.ifHaveFeature(14) && armyData.getIfAttack() == 1) {
                    ifFlash = false;
                } else if (armyData.getIfMove() == 0 && armyData.getArmyType() != 6) {
                    ifFlash = true;
                } else if (armyData.haveCanAtkEnemy()) {
                    ifFlash = true;
                } else {
                    ifFlash = false;
                }
            }

        } else {
            ifFlash = false;
        }
    }


    public void moveHexagon(int id) {
        armyData.armyMove(id);
    }

    public void resetHexagon(){
       Fb2Map.MapHexagon hexagonData=armyData.getHexagonData();
        setPosition(hexagonData.source_x, hexagonData.source_y);
    }

    public IntIntMap moveHexagonAndGetTarget(IntIntMap rs, int id) {
        moveHexagon(id);
        //3.判断有无可进攻目标
        //public IntIntMap getHexagonsToSelected(int hexagon,int getGoodsType,int value, int cardType,int useType, IntIntMap rs) {
        return btl.getPotion_CanAttackHexagons(armyData, rs);

    }


    public void resetHpColor() {
        armyHpRegionDAO = getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForArmyHp(getMainGame().getSMapDAO(), armyData));
        int hw = armyHpRegionDAO.getTextureRegion().getRegionWidth();
        float hpScale = armyData.getArmyHpNow() * 1f / armyData.getArmyHpMax();
        int hy = (int) (hw - hw * hpScale);
        armyHpRegion = armyHpRegionDAO.getNewRegion(0, hy, hw, hw - hy);
        //chassisRegion=getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForArmyChassis(getMainGame().getSMapDAO(),armyData)).getTextureRegion();
        scArmyBorderRegion = getMainGame().getImgLists().getTextureByName(DefDAO.getImageNameForSCBorder(getMainGame().getSMapDAO(), armyData.getLegionIndex())).getTextureRegion();
        resetFlash();
    }

    public void armyDeath() {
        AlphaAction action2 = Actions.alpha(0.0F, 2.0F);
        addAction(action2);
        RunnableAction removeAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                cam.rescource.getArmyActorList().removeValue(ArmyActor.this, false);
            }
        });
        //2秒以后恢复原来的模型
        addAction(Actions.sequence(Actions.delay(2.0F), removeAction));
    }

    public void resetTarget() {
        if (armyData.getArmyType() != 6 && btl.ifGridIsPass(armyData.getTargetRegion())) {
            //float   rotation=      GameUtil.getRotationByHexagon(smapBg.getW(),smapBg.getMapH_px(),armyActor.getHexagon(),selectedTargetRegion);
            float rotation = GameUtil.getRotationByHexagon(mapW, cam.getMapH_px(), armyData.getHexagonIndex(), armyData.getTargetRegion(), scale);
            //设置兵种箭头,角度
            if (btl.ifAllyByHexagon(armyData.getHexagonIndex(), armyData.getTargetRegion())) {
                setTargetState(2, rotation);
            } else {
                setTargetState(1, rotation);
            }
        } else {
            setTargetState(0, 0);
        }
    }


    public boolean getIfFlash() {
        return ifFlash;
    }




  /*  public void updateDrawFort(int hexagon) {
       Fb2Smap.FortData f =getMainGame().getSMapDAO().getFortDataByHexagon(hexagon);
        if(f!=null){
            fortId=f.getFortId();
            ifDrawFort=true;
           fortRegionDAO=getMainGame().getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(fortId));
            f.fortActorToDrawFort();
        }else {
            fortId=-1;
            ifDrawFort=false;
        }
    }*/

    /*public void setIfDrawFort(boolean b) {
        this.ifDrawFort=b;
    }*/


    //选中的时候,根据兵模类型替换兵模
    public void clickModel() {
       /* */
        if (modelE == null /*||modelE.getInt("modelType")!=2||!ifFlash*/ || (armyData.getArmyType() != 4 && armyData.getArmyType() != 8 && armyData.potionIsSea())) {
            return;
        }
       if(ResDefaultConfig.ifDebug){
            updArmyModel();
        } /**/
        //setArmyModel(0,armyModelTR,armyModelR,armyModelRegion,armyModelRegionF,bgArmyModelTR,bgArmyModelR,bgArmyModelRegion,bgArmyModelRegionF);
     //  float damageTime=0;
        if(armyData.isUnitGroup()&&!armyData.potionIsSea()){

            if(cam.loopState==0||cam.loopState==1) {
                if(modelE2!=null&&!ComUtil.isEmpty(modelE2.get("attackImgName",""))){
                    armyModelTR2 = getMainGame().getImgLists().getTextureByName(modelE2.get("attackImgName", modelE2.get("defaultImgName")));
                    armyModelRegion2 = armyModelTR2.getTextureRegion();
                    armyModelRegionF2 = getMainGame().getImgLists().getFlipRegion(armyModelTR2.getName());
                }
                if(modelE3!=null&&!ComUtil.isEmpty(modelE3.get("attackImgName",""))){
                    armyModelTR3 = getMainGame().getImgLists().getTextureByName(modelE3.get("attackImgName", modelE3.get("defaultImgName")));
                    armyModelRegion3 = armyModelTR3.getTextureRegion();
                    armyModelRegionF3 = getMainGame().getImgLists().getFlipRegion(armyModelTR3.getName());
                }
                if(modelE1!=null&&!ComUtil.isEmpty(modelE1.get("attackImgName",""))){
                    armyModelTR1 = getMainGame().getImgLists().getTextureByName(modelE1.get("attackImgName", modelE1.get("defaultImgName")));
                    armyModelRegion1 = armyModelTR1.getTextureRegion();
                    armyModelRegionF1 = getMainGame().getImgLists().getFlipRegion(armyModelTR1.getName());
                }
                if(modelE4!=null&&!ComUtil.isEmpty(modelE4.get("attackImgName",""))){
                    armyModelTR4 = getMainGame().getImgLists().getTextureByName(modelE4.get("attackImgName", modelE4.get("defaultImgName")));
                    armyModelRegion4 = armyModelTR4.getTextureRegion();
                    armyModelRegionF4 = getMainGame().getImgLists().getFlipRegion(armyModelTR4.getName());
                }
                if(modelE!=null&&!ComUtil.isEmpty(modelE.get("attackImgName",""))){
                    armyModelTR = getMainGame().getImgLists().getTextureByName(modelE.get("attackImgName", modelE.get("defaultImgName")));
                    armyModelRegion = armyModelTR.getTextureRegion();
                    armyModelRegionF = getMainGame().getImgLists().getFlipRegion(armyModelTR.getName());
                }
                if(modelE5!=null&&!ComUtil.isEmpty(modelE5.get("attackImgName",""))){
                    armyModelTR5 = getMainGame().getImgLists().getTextureByName(modelE5.get("attackImgName", modelE5.get("defaultImgName")));
                    armyModelRegion5 = armyModelTR5.getTextureRegion();
                    armyModelRegionF5 = getMainGame().getImgLists().getFlipRegion(armyModelTR5.getName());
                }
                if(modelE6!=null&&!ComUtil.isEmpty(modelE6.get("attackImgName",""))){
                    armyModelTR6 = getMainGame().getImgLists().getTextureByName(modelE6.get("attackImgName", modelE6.get("defaultImgName")));
                    armyModelRegion6 = armyModelTR6.getTextureRegion();
                    armyModelRegionF6 = getMainGame().getImgLists().getFlipRegion(armyModelTR6.getName());
                }
            }
        }else {
            if(modelE!=null){
                armyModelTR = getMainGame().getImgLists().getTextureByName(modelE.get("attackImgName", modelE.get("defaultImgName")));
                armyModelRegion = armyModelTR.getTextureRegion();
                armyModelRegionF = getMainGame().getImgLists().getFlipRegion(armyModelTR.getName());
            }
        }
        updArmyModelDirect();

      //测试用方法 点中时播放一遍开火动画
      //  drawAttack(0,0);
      //  drawStrike(getAttackStrike(),damageTime);
    }





    public void drawStrike(String strikeName, float atTime) {
        if (!getMainGame().gameConfig.ifEffect||!cam.inScreen(armyData.getHexagonIndex())) {
            return;
        }
        if (armyData.potionIsSea()) {
            armyData.getEffectStage().effectDAO.darwEffect("waterStrike", armyData.getHexagonIndex(), 0, 0, 0, atTime, false);
        } else {
            armyData.getEffectStage().effectDAO.darwEffect(strikeName, armyData.getHexagonIndex(), 0, 0, 0, atTime, false);
        }


        RunnableAction resetModelAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                updArmyModel();
                //Gdx.app.log("恢复兵模","123");
            }
        });
        //2秒以后恢复原来的模型
        addAction(Actions.sequence(Actions.delay(atTime + 1), resetModelAction));
    }

    public String getAttackStrike() {
        //获得造成伤害后目标的受击类型
        if (modelE != null) {
          int  modeType= modelE.getInt("modelType", 0);
            XmlReader.Element xml=null;
            if(modeType == 1){
                if(armyData.getArmyDirection()==0){
                    xml=  modelE.getChildByName("attackL");
                }else {
                    xml=  modelE.getChildByName("attackR");
                }
            }else {
                xml=  modelE.getChildByName("attack");
            }
            if(xml==null){
                return "generalStrike";
            }
            return xml.get("strike", "generalStrike");
        }
        return "generalStrike";
    }


    //修改游戏位置与修改游戏位置
    private void updArmyModelDirect() {
        if (modelE == null) {
            return;
        }
        initModelPotion();
        //  modelRefV =-armyModelTR.getRefx()* ResDefaultConfig.Map.MAP_SCALE*scale;
        modelScale0 = modelE.getFloat("scale", 1);
        //setArmyModel(0,armyModelTR,armyModelR,armyModelRegion,armyModelRegionF,bgArmyModelTR,bgArmyModelR,bgArmyModelRegion,bgArmyModelRegionF);
        //边缘的数量
        int bc = armyData.getUnitGroup()-1;
        //如果是已组合的部队,对其位置进行调整
        if (armyData.isUnitGroup() && bc > 0) {
            float scale=0.7f ;
            scale+=armyData.getUnitGroupSum()*0.004f;
            boolean b1=modelE1!=null&&modelE2!=null;//↖
            boolean b2=modelE2!=null&&modelE3!=null;//↑
            boolean b3=modelE3!=null&&modelE4!=null;//↗
            boolean b4=modelE1!=null&&modelE5!=null;//↙
            boolean b5=modelE5!=null&&modelE6!=null;//↓
            boolean b6=modelE6!=null&&modelE4!=null;//↘
           if(bc==1){
                if(modelE1!=null){
                    addModeRefXValue(3,6,0);
                    addModeRefYValue(0,0,0);
                }else if(modelE2!=null){
                    addModeRefXValue(3,6,0);
                    addModeRefYValue(-3,-6,-3);
                }else if(modelE3!=null){
                    addModeRefXValue(-3,-6,0);
                    addModeRefYValue(-3,-6,-3);
                }else if(modelE4!=null){
                    addModeRefXValue(0,-6,-3);
                    addModeRefYValue(0,0,0);
                }else if(modelE5!=null){
                    addModeRefXValue(3,6,0);
                    addModeRefYValue(3,6,3);
                }else if(modelE6!=null){
                    addModeRefXValue(-3,-6,0);
                    addModeRefYValue(3,6,3);
                }
               scale+=0.05f;
           }else {
               int v=(9-bc)/2;//3~2
               if((!b1||!b4)&&(modelE1==null||modelE2==null||modelE5==null)){//左上,或左下无边界存在
                   addModeRefXValue(-v*3,-v*2,-v);
               }
               /*if(modelE1==null&&modelE2==null&&modelE5==null){
                   addModeRefXValue(v,v*2,v*3);
               }*/
               if((!b3||!b6)&&(modelE3==null||modelE4==null||modelE6==null)){//右上,或右下无边界存在
                   addModeRefXValue(v,v*2,v*3);
               }
               /*if(modelE3==null&&modelE4==null&&modelE6==null){//左上,或左下无边界存在
                   addModeRefXValue(-v*3,-v*2,-v);
               }*/
               if(modelE2==null&&modelE3==null){//上部无单位,向上移动
                   addModeRefYValue(v*2,v*2,v);
               }
               if(modelE5==null&&modelE6==null){//下部无单位,向下部移动
                   addModeRefYValue(-v,-v*2,-v*2);
               }
               if(modelE1==null){
                   addModeRefXValue(0,-v*2,0);
               }else if(modelE4==null){
                   addModeRefXValue(0,v*2,0);
               }

               if(bc==2){
                   scale+=0.04f;
               }else if(bc==3){
                   scale+=0.03f;
               }else if(bc==4){
                   scale+=0.02f;
               }else if(bc==5){
                   scale+=0.01f;
               }
           }

           scale*=  ResDefaultConfig.Map.UNITMODEL_SCALE;
            bgModelScale0 *= scale;
            modelScale0 *= scale;
            bgModelScale1 *= scale;
            modelScale1 *= scale;
            bgModelScale2 *= scale;
            modelScale2 *= scale;
            bgModelScale3 *= scale;
            modelScale3 *= scale;
            bgModelScale4 *= scale;
            modelScale4 *= scale;
            bgModelScale5 *= scale;
            modelScale5 *= scale;
            bgModelScale6 *= scale;
            modelScale6 *= scale;
        }else{
            bgModelScale0+=armyData.getUnitGroup0Lv()*0.03;
            modelScale0+=armyData.getUnitGroup0Lv()*0.03;
            bgModelScale0 *=  ResDefaultConfig.Map.UNITMODEL_SCALE;
            modelScale0 *=  ResDefaultConfig.Map.UNITMODEL_SCALE;
        }
        modelRefY0 += (-armyModelTR.getRefy() + modelE.getInt("y", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale0;
        int rightRefX=-2;
        if (armyData.getArmyDirection() == 0 || modelE.getInt("modelType", 0) == 1) {
            armyModelR = armyModelRegion;
            modelRefX0 += -armyModelTR.getRefx() * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale0 + modelE.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale0;
        } else {
            armyModelR = armyModelRegionF;
            modelRefX0 +=rightRefX -(armyModelTR.getW() - armyModelTR.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale0 + modelE.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale0;
        }
        if (bgArmyModelTR != null) {
            bgModelScale0 *= modelE.getFloat("bgScale", 1);
            bgModelRefY0 += (-bgArmyModelTR.getRefy() + modelE.getInt("bgY", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale0;
            if (armyData.getArmyDirection() == 0 || modelE.getInt("bgImgModelType", 0) == 1) {
                bgArmyModelR = bgArmyModelRegion;
                bgModelRefX0 += (-bgArmyModelTR.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale0 + modelE.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale0;
            } else {
                bgArmyModelR = bgArmyModelRegionF;
                bgModelRefX0 += -(bgArmyModelTR.getW() - bgArmyModelTR.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale0 + modelE.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale0;
            }
        } else {
            bgModelRefX0 = 0;
            bgModelRefY0 = 0;
            bgModelScale0 *= 1;
        }
        if (armyData.isUnitGroup()) {//组合部队
            if (armyModelTR1 != null&&modelE1!=null) {
                modelScale1 *= modelE1.getFloat("scale", 1);
                modelRefY1 += (-armyModelTR1.getRefy() + modelE1.getInt("y", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale1;
                if (armyData.getArmyDirection() == 0 || modelE1.getInt("modelType", 0) == 1) {
                    armyModelR1 = armyModelRegion1;
                    modelRefX1 += -armyModelTR1.getRefx() * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale1 + modelE1.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale1;
                } else {
                    armyModelR1 = armyModelRegionF1;
                    modelRefX1 += rightRefX-(armyModelTR1.getW() - armyModelTR1.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale1 + modelE1.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale1;
                }
                if (bgArmyModelTR1 != null) {
                    bgModelScale1 *= modelE1.getFloat("bgScale", 1);
                    bgModelRefY1 += (-bgArmyModelTR1.getRefy() + modelE1.getInt("bgY", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale1;
                    if (armyData.getArmyDirection() == 0 || modelE1.getInt("bgImgModelType", 0) == 1) {
                        bgArmyModelR1 = bgArmyModelRegion1;
                        bgModelRefX1 += (-bgArmyModelTR1.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale1 + modelE1.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale1;
                    } else {
                        bgArmyModelR1 = bgArmyModelRegionF1;
                        bgModelRefX1 += rightRefX-(bgArmyModelTR1.getW() - bgArmyModelTR1.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale1 + modelE1.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale1;
                    }
                }
            }
            if (armyModelTR2 != null&&modelE2!=null) {
                modelScale2 *= modelE2.getFloat("scale", 1);
                modelRefY2 += (-armyModelTR2.getRefy() + modelE2.getInt("y", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale2;
                if (armyData.getArmyDirection() == 0 || modelE2.getInt("modelType", 0) == 1) {
                    armyModelR2 = armyModelRegion2;
                    modelRefX2 += -armyModelTR2.getRefx() * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale2 + modelE2.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale2;
                } else {
                    armyModelR2 = armyModelRegionF2;
                    modelRefX2 += rightRefX-(armyModelTR2.getW() - armyModelTR2.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale2 + modelE2.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale2;
                }
                if (bgArmyModelTR2 != null) {
                    bgModelScale2 *= modelE2.getFloat("bgScale", 1);
                    bgModelRefY2 += (-bgArmyModelTR2.getRefy() + modelE2.getInt("bgY", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale2;
                    if (armyData.getArmyDirection() == 0 || modelE2.getInt("bgImgModelType", 0) == 1) {
                        bgArmyModelR2 = bgArmyModelRegion2;
                        bgModelRefX2 += (-bgArmyModelTR2.getRefx() + modelE2.getInt("bgX", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale2;
                    } else {
                        bgArmyModelR2 = bgArmyModelRegionF2;
                        bgModelRefX2 += rightRefX-(bgArmyModelTR2.getW() - bgArmyModelTR2.getRefx() + modelE2.getInt("bgX", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale2;
                    }
                }
            }
            if (armyModelTR3 != null&&modelE3!=null) {
                modelScale3 *= modelE3.getFloat("scale", 1);
                modelRefY3 += (-armyModelTR3.getRefy() + modelE3.getInt("y", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale3;
                if (armyData.getArmyDirection() == 0 || modelE3.getInt("modelType", 0) == 1) {
                    armyModelR3 = armyModelRegion3;
                    modelRefX3 += -armyModelTR3.getRefx() * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale3 + modelE3.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale3;
                } else {
                    armyModelR3 = armyModelRegionF3;
                    modelRefX3 += rightRefX-(armyModelTR3.getW() - armyModelTR3.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale3 + modelE3.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale3;
                }
                if (bgArmyModelTR3 != null) {
                    bgModelScale3 *= modelE3.getFloat("bgScale", 1);
                    bgModelRefY3 += (-bgArmyModelTR3.getRefy() + modelE3.getInt("bgY", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale3;
                    if (armyData.getArmyDirection() == 0 || modelE3.getInt("bgImgModelType", 0) == 1) {
                        bgArmyModelR3 = bgArmyModelRegion3;
                        bgModelRefX3 += (-bgArmyModelTR3.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale3 + modelE3.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale3;
                    } else {
                        bgArmyModelR3 = bgArmyModelRegionF3;
                        bgModelRefX3 +=rightRefX -(bgArmyModelTR3.getW() - bgArmyModelTR3.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale3 + modelE3.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale3;
                    }
                }
            }
            if (armyModelTR4 != null&&modelE4!=null) {
                modelScale4 *= modelE4.getFloat("scale", 1);
                modelRefY4 += (-armyModelTR4.getRefy() + modelE4.getInt("y", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale4;
                if (armyData.getArmyDirection() == 0 || modelE4.getInt("modelType", 0) == 1) {
                    armyModelR4 = armyModelRegion4;
                    modelRefX4 += -armyModelTR4.getRefx() * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale4 + modelE4.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale4;
                } else {
                    armyModelR4 = armyModelRegionF4;
                    modelRefX4 += rightRefX-(armyModelTR4.getW() - armyModelTR4.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale4 + modelE4.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale4;
                }
                if (bgArmyModelTR4 != null) {
                    bgModelScale4 *= modelE4.getFloat("bgScale", 1);
                    bgModelRefY4 += (-bgArmyModelTR4.getRefy() + modelE4.getInt("bgY", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale4;
                    if (armyData.getArmyDirection() == 0 || modelE4.getInt("bgImgModelType", 0) == 1) {
                        bgArmyModelR4 = bgArmyModelRegion4;
                        bgModelRefX4 += (-bgArmyModelTR4.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale4 + modelE4.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale4;
                    } else {
                        bgArmyModelR4 = bgArmyModelRegionF4;
                        bgModelRefX4 += rightRefX-(bgArmyModelTR4.getW() - bgArmyModelTR4.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale4 + modelE4.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale4;
                    }
                }
            }
            if (armyModelTR5 != null&&modelE5!=null) {
                modelScale5 *= modelE5.getFloat("scale", 1);
                modelRefY5 += (-armyModelTR5.getRefy() + modelE5.getInt("y", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale5;
                if (armyData.getArmyDirection() == 0 || modelE5.getInt("modelType", 0) == 1) {
                    armyModelR5 = armyModelRegion5;
                    modelRefX5 += -armyModelTR5.getRefx() * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale5 + modelE5.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale5;
                } else {
                    armyModelR5 = armyModelRegionF5;
                    modelRefX5 += rightRefX-(armyModelTR5.getW() - armyModelTR5.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale5 + modelE5.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale5;
                }
                if (bgArmyModelTR5 != null) {
                    bgModelScale5 *= modelE5.getFloat("bgScale", 1);
                    bgModelRefY5 += (-bgArmyModelTR5.getRefy() + modelE5.getInt("bgY", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale5;
                    if (armyData.getArmyDirection() == 0 || modelE5.getInt("bgImgModelType", 0) == 1) {
                        bgArmyModelR5 = bgArmyModelRegion5;
                        bgModelRefX5 += (-bgArmyModelTR5.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale5 + modelE5.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale5;
                    } else {
                        bgArmyModelR5 = bgArmyModelRegionF5;
                        bgModelRefX5 += rightRefX-(bgArmyModelTR5.getW() - bgArmyModelTR5.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale5 + modelE5.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale5;
                    }
                }
            }
            if (armyModelTR6 != null&&modelE6!=null) {
                modelScale6 *= modelE6.getFloat("scale", 1);
                modelRefY6 += (-armyModelTR6.getRefy() + modelE6.getInt("y", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale6;
                if (armyData.getArmyDirection() == 0 || modelE6.getInt("modelType", 0) == 1) {
                    armyModelR6 = armyModelRegion6;
                    modelRefX6 += -armyModelTR6.getRefx() * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale6 + modelE6.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale6;
                } else {
                    armyModelR6 = armyModelRegionF6;
                    modelRefX6 += rightRefX-(armyModelTR6.getW() - armyModelTR6.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale6 + modelE6.getInt("x", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * modelScale6;
                }
                if (bgArmyModelTR6 != null) {
                    bgModelScale6 *= modelE6.getFloat("bgScale", 1);
                    bgModelRefY6 += (-bgArmyModelTR6.getRefy() + modelE6.getInt("bgY", 0)) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale6;
                    if (armyData.getArmyDirection() == 0 || modelE6.getInt("bgImgModelType", 0) == 1) {
                        bgArmyModelR6 = bgArmyModelRegion6;
                        bgModelRefX6 += (-bgArmyModelTR6.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale6 + modelE6.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale6;
                    } else {
                        bgArmyModelR6 = bgArmyModelRegionF6;
                        bgModelRefX6 += rightRefX-(bgArmyModelTR6.getW() - bgArmyModelTR6.getRefx()) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale6 + modelE6.getInt("bgX", 0) * ResDefaultConfig.Map.MAP_SCALE * scale * bgModelScale6;
                    }
                }
            }
        }
    }

    private void addModeRefXValue(int v) {
        bgModelRefX0+=v;
        modelRefX0+=v;
        bgModelRefX1+=v;
        modelRefX1+=v;
        bgModelRefX2+=v;
        modelRefX2+=v;
        bgModelRefX3+=v;
        modelRefX3+=v;
        bgModelRefX4+=v;
        modelRefX4+=v;
        bgModelRefX5+=v;
        modelRefX5+=v;
        bgModelRefX6+=v;
        modelRefX6+=v;
        centerModelRefX+=v;
    }
    private void addModeRefYValue(int v) {
        bgModelRefY0+=v;
        modelRefY0+=v;
        bgModelRefY1+=v;
        modelRefY1+=v;
        bgModelRefY2+=v;
        modelRefY2+=v;
        bgModelRefY3+=v;
        modelRefY3+=v;
        bgModelRefY4+=v;
        modelRefY4+=v;
        bgModelRefY5+=v;
        modelRefY5+=v;
        bgModelRefY6+=v;
        modelRefY6+=v;
        centerModelRefY+=v;
    }
    //从左到右
    private void addModeRefXValue(int v1,int v2,int v3) {
        bgModelRefX1+=v1;
        modelRefX1+=v1;
        bgModelRefX2+=v1;
        modelRefX2+=v1;
        bgModelRefX5+=v1;
        modelRefX5+=v1;
        bgModelRefX0+=v2;
        modelRefX0+=v2;
        bgModelRefX3+=v3;
        modelRefX3+=v3;
        bgModelRefX4+=v3;
        modelRefX4+=v3;
        bgModelRefX6+=v3;
        modelRefX6+=v3;
        centerModelRefX+=v2;
    }
    //从上到下
    private void addModeRefYValue(int v1,int v2,int v3) {
        bgModelRefY2+=v1;
        modelRefY2+=v1;
        bgModelRefY3+=v1;
        modelRefY3+=v1;
        bgModelRefY0+=v2;
        modelRefY0+=v2;
        bgModelRefY1+=v2;
        modelRefY1+=v2;
        bgModelRefY4+=v2;
        modelRefY4+=v2;
        bgModelRefY5+=v3;
        modelRefY5+=v3;
        bgModelRefY6+=v3;
        modelRefY6+=v3;
        centerModelRefY+=v2;
    }
    private void initModelPotion() {
        centerModelRefX=0;
        centerModelRefY=0;
        modelRefX0 = 0;
        modelRefY0 = 0;
        modelScale0 = 1;
        bgModelRefX0 = 0;
        bgModelRefY0 = 0;
        bgModelScale0 = 1;
        modelRefX1 = 0;
        modelRefY1 = 0;
        modelScale1 = 1;
        bgModelRefX1 = 0;
        bgModelRefY1 = 0;
        bgModelScale1 = 1;
        modelRefX2 = 0;
        modelRefY2 = 0;
        modelScale2 = 1;
        bgModelRefX2 = 0;
        bgModelRefY2 = 0;
        bgModelScale2 = 1;
        modelRefX3 = 0;
        modelRefY3 = 0;
        modelScale3 = 1;
        bgModelRefX3 = 0;
        bgModelRefY3 = 0;
        bgModelScale3 = 1;
        modelRefX4 = 0;
        modelRefY4 = 0;
        modelScale4 = 1;
        bgModelRefX4 = 0;
        bgModelRefY4 = 0;
        bgModelScale4 = 1;
        modelRefX5 = 0;
        modelRefY5 = 0;
        modelScale5 = 1;
        bgModelRefX5 = 0;
        bgModelRefY5 = 0;
        bgModelScale5 = 1;
        modelRefX6 = 0;
        modelRefY6 = 0;
        modelScale6 = 1;
        bgModelRefX6 = 0;
        bgModelRefY6 = 0;
        bgModelScale6 = 1;
    }

    public void drawArmyMark(Batch batch) {


        float scale = 1.0f;
        Color color = getColor();

        float alphaFlash = 0.5f;

        if (ifFlash) {
            alphaFlash = resource.getAlphaFlash();
        }
        if (ifDrawAir) {
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        resource.airRegionDAO.getTextureRegion(),
                        //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                        getX() +20 , -35+getY()  ,
                        resource.airRegionDAO.getRefx(),  resource.airRegionDAO.getRefy(),
                        resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(
                        resource.airRegionDAO.getTextureRegion(),
                        //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                        cam.getMapW_px() +getX() + 20, -35+getY()  ,
                        resource.airRegionDAO.getRefx(),  resource.airRegionDAO.getRefy(),
                        resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
        }
        if (ifDrawNul) {
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        resource.nulRegionDAO.getTextureRegion(),
                        -36+getX(), -35+getY() ,
                        resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                        resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(
                        resource.nulRegionDAO.getTextureRegion(),
                        cam.getMapW_px()-36+getX(), -35+getY() ,
                        resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                        resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                        getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                        getRotation()
                );
            }
        }

        //绘制底牌
       // float refx=4;//getMainGame().sMapScreen.tempActor.getX();
        /*   */
        batch.setColor(1f - legionColor.r, 1f - legionColor.g, 1f - legionColor.b, 1f);

        // 结合演员的属性绘制表示演员的纹理区域
        if (cam.loopState == 0 || cam.loopState == 1) {
            batch.draw(
                    resource.armyBottomRegionDAO.getTextureRegion(),
                    getX() + resource.markRefx-16, getY() + resource.markRefy-20,
                    getOriginX(), getOriginY(),
                    getWidth(), getHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
        }
        if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
            batch.draw(
                    resource.armyBottomRegionDAO.getTextureRegion(),
                    cam.getMapW_px() + getX() + resource.markRefx-16, getY() + resource.markRefy-20,
                    getOriginX(), getOriginY(),
                    getWidth(), getHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
        }
        batch.setColor(color.r, color.g, color.b, alphaFlash);


        //绘制兵牌或国旗
        //batch.setColor(color.r, color.g, color.b, 0.5f);
        if (cam.loopState == 0 || cam.loopState == 1) {
            // 结合演员的属性绘制表示演员的纹理区域
            batch.draw(
                    armyMarkRegion,
                    getX() - armyMarkTR.getRefx() -1, getY() - armyMarkTR.getRefy() -2,
                    getOriginX(), getOriginY(),
                    armyMarkRegion.getRegionWidth(), armyMarkRegion.getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
        }
        if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
            batch.draw(
                    armyMarkRegion,
                    cam.getMapW_px() + getX() - armyMarkTR.getRefx() -1, getY() - armyMarkTR.getRefy() -2,
                    getOriginX(), getOriginY(),
                    armyMarkRegion.getRegionWidth(), armyMarkRegion.getRegionHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
        }
        batch.setColor(color);

        if (rank != null) {//绘制军衔
            // 将演员的 Color 结合 parentAlpha 设置到 batch

            if (ifGeneral && armyData.getGameMode() != 2) {
                if (cam.loopState == 1 || cam.loopState == 0) {
                    batch.draw(//+resource.markRefx
                            rank.getTextureRegion(),
                            getX() - rank.getRefx() * 0.8f , getY() - rank.getRefy() * 0.8f + 1,
                            getOriginX(), getOriginY(),
                            rank.getW(), rank.getH(),
                            getScaleX() * 0.8f, getScaleY() * 0.8f,
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                    batch.draw(
                            rank.getTextureRegion(),
                            cam.getMapW_px() +getX() - rank.getRefx() * 0.8f , getY() - rank.getRefy() * 0.8f + 1,
                            getOriginX(), getOriginY(),
                            rank.getW(), rank.getH(),
                            getScaleX() * 0.8f, getScaleY() * 0.8f,
                            getRotation()
                    );
                }
                batch.setColor(color);
            } else {
                if (cam.loopState == 1 || cam.loopState == 0) {
                    batch.draw(
                            rank.getTextureRegion(),
                            //getX()+resource.markRefx + 20, getY()+resource.markRefy + 16 ,
                            getX() - rank.getRefx() , getY() - rank.getRefy() +-5,
                            getOriginX(), getOriginY(),
                            rank.getW(), rank.getH(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                    batch.draw(
                            rank.getTextureRegion(),
                            cam.getMapW_px() + getX() - rank.getRefx() , getY() - rank.getRefy() +-5,
                            getOriginX(), getOriginY(),
                            rank.getW(), rank.getH(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
            }
        }

        //绘制血条
        // 结合演员的属性绘制表示演员的纹理区域
        if (cam.loopState == 1 || cam.loopState == 0) {
            batch.draw(
                    armyHpRegion,
                    getX() - 35, getY() + hpRefy * getScaleY() -37,
                    //    getX() + getMainGame().sMapScreen.tempActor.getX(), getY() + getMainGame().sMapScreen.tempActor.getY() + hpRefy * getScaleY() * scale,
                    30, 31,//手工测出来的.....
                    //,,
                    armyHpRegion.getRegionWidth(), armyHpRegion.getRegionHeight(),
                    getScaleX() * scale, getScaleY() * scale,
                    180
            );
            batch.setColor(moraleColor);
            if (!getIfCanUpd()) {

                batch.draw(
                        resource.moraleRegionDAO.getTextureRegion(),
                        getX() - 31, getY() -23,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        getOriginX() + resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), getOriginY() + (resource.moraleRegionDAO.getTextureRegion().getRegionHeight()) / 2,//手工测出来的.....
                        resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY,
                        getScaleX() * scale, getScaleY() * scale,
                        getRotation()
                );
            }
            batch.setColor(color);


            if (groupDAO != null) {
                if (smallGeneral == null) {
                    batch.draw(
                            groupDAO.getTextureRegion(),
                            getX() + 20, getY() + -20,
                            getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                            groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() * 1.5f, getScaleY() * 1.5f,
                            getRotation()
                    );
                } else {
                    batch.draw(
                            groupDAO.getTextureRegion(),
                            getX() + 25, getY() + -26,
                            getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                            groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() * 2f, getScaleY() * 2f,
                            getRotation()
                    );
                }
            }
        }


        if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
            batch.draw(
                    armyHpRegion,
                    cam.getMapW_px()+getX() - 35, getY() + hpRefy * getScaleY() -37,
                    //    getX() + getMainGame().sMapScreen.tempActor.getX(), getY() + getMainGame().sMapScreen.tempActor.getY() + hpRefy * getScaleY() * scale,
                    30, 31,//手工测出来的.....
                    //,,
                    armyHpRegion.getRegionWidth(), armyHpRegion.getRegionHeight(),
                    getScaleX() * scale, getScaleY() * scale,
                    180
            );
            if (!getIfCanUpd()) {
                batch.setColor(moraleColor);

                batch.draw(
                        resource.moraleRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX() - 31, getY() -23,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        getOriginX() + resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), getOriginY() + (resource.moraleRegionDAO.getTextureRegion().getRegionHeight()) / 2,//手工测出来的.....
                        resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY,
                        getScaleX() * scale, getScaleY() * scale,
                        getRotation()
                );
            }
            batch.setColor(color);


            if (groupDAO != null) {
                if (smallGeneral == null) {
                    batch.draw(
                            groupDAO.getTextureRegion(),
                            cam.getMapW_px()+getX() + 20, getY() + -20,
                            getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                            groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() * 1.5f, getScaleY() * 1.5f,
                            getRotation()
                    );
                } else {
                    batch.draw(
                            groupDAO.getTextureRegion(),
                            cam.getMapW_px()+getX() + 25, getY() + -26,
                            getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                            groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() * 2f, getScaleY() * 2f,
                            getRotation()
                    );
                }
            }
        }

        if (ifPlayerCommand && targetState != 0) {//绘制目标
            // 结合演员的属性绘制表示演员的纹理区域
            if (cam.loopState == 1 || cam.loopState == 0) {
                batch.draw(
                        targetRegionDAO.getTextureRegion(),
                        //getX()+markRefX - game.gameConfig.targetRefv , getY()+markRefY -game.gameConfig.targetRefv,
                        getX() + resource.markRefx-16, getY() + resource.markRefy-20,
                        targetRegionDAO.getRefx(), targetRegionDAO.getRefy(),
                        getWidth(), getHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        targetAngle
                );
            }
            if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                batch.draw(

                        targetRegionDAO.getTextureRegion(),
                        cam.getMapW_px() + getX() + resource.markRefx-16, getY() + resource.markRefy-20,
                        targetRegionDAO.getRefx(), targetRegionDAO.getRefy(),
                        getWidth(), getHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        targetAngle
                );
            }
        }


        if (ifDrawEnergy) {
            if (cam.loopState == 1 || cam.loopState == 0) {
                batch.draw(
                        energyRegionDAO.getTextureRegion(),
                        getX()- energyRegionDAO.getRefx() , getY()  - energyRegionDAO.getRefy() - 25,
                        energyRegionDAO.getTextureRegion().getRegionWidth() / 2, energyRegionDAO.getTextureRegion().getRegionHeight() / 2,
                        energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                        (getScaleX() + cam.getZoom()) * scale, (getScaleY() + cam.getZoom()) * scale,
                        getRotation()
                );
            }
            if (cam.loopState == 1 || cam.loopState == 2) {
                batch.draw(
                        energyRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+getX()- energyRegionDAO.getRefx(), getY()  - energyRegionDAO.getRefy() - 25,
                        energyRegionDAO.getTextureRegion().getRegionWidth() / 2, energyRegionDAO.getTextureRegion().getRegionHeight() / 2,
                        energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                        (getScaleX() + cam.getZoom()) * scale, (getScaleY() + cam.getZoom()) * scale,
                        getRotation()
                );
            }
        }
        if (getIfCanUpd()) {
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(//+resource.markRefx
                        resource.markUpdLvRegionDAO.getTextureRegion(),
                        getX() - resource.markUpdLvRegionDAO.getRefx() - 23, getY() - resource.markUpdLvRegionDAO.getRefy() + -14,
                        getOriginX(), getOriginY(),
                        resource.markUpdLvRegionDAO.getW(), resource.markUpdLvRegionDAO.getH(),
                        getScaleX() * scale, getScaleY() * scale,
                        getRotation()
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(//+resource.markRefx
                        resource.markUpdLvRegionDAO.getTextureRegion(),
                        cam.getMapW_px() + getX() - resource.markUpdLvRegionDAO.getRefx() -23, getY() - resource.markUpdLvRegionDAO.getRefy() + -14,
                        getOriginX(), getOriginY(),
                        resource.markUpdLvRegionDAO.getW(), resource.markUpdLvRegionDAO.getH(),
                        getScaleX() * scale, getScaleY() * scale,
                        getRotation()
                );
            }
        }
    }

    public void drawGeneralMark(Batch batch, int count) {
        Color color = getColor();
        float scale = 1.5f;
        if (count == 0) {//绘制人物
            if (cam.loopState == 1 || cam.loopState == 0) {
                /**/
                batch.draw(
                        smallGeneralBg,
                        getX() -16, getY() -12,
                        getOriginX() + smallGeneral.getRegionWidth() / 2, getOriginY() + smallGeneral.getRegionHeight() / 2,//手工测出来的.....
                        //getMainGame().sMapScreen.tempActor.getX(),getMainGame().sMapScreen.tempActor.getY(),
                        smallGeneralBg.getRegionWidth(), smallGeneralBg.getRegionHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        180
                );
                batch.draw(
                        smallGeneral,
                        getX() -22, getY() + -17,
                        getOriginX() + smallGeneral.getRegionWidth() / 2, getOriginY() + smallGeneral.getRegionHeight() / 2,
                        smallGeneral.getRegionWidth(), smallGeneral.getRegionHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        getRotation()
                );

            }
            if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                batch.draw(
                        smallGeneralBg,
                        cam.getMapW_px() + getX() -16, getY() + -12,
                        getOriginX() + smallGeneral.getRegionWidth() / 2, getOriginY() + smallGeneral.getRegionHeight() / 2,//手工测出来的.....
                        //getMainGame().sMapScreen.tempActor.getX(),getMainGame().sMapScreen.tempActor.getY(),
                        smallGeneralBg.getRegionWidth(), smallGeneralBg.getRegionHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        180
                );
                batch.draw(
                        smallGeneral,
                        cam.getMapW_px() + getX() - 22, getY() + -17,
                        getOriginX() + smallGeneral.getRegionWidth() / 2, getOriginY() + smallGeneral.getRegionHeight() / 2,
                        smallGeneral.getRegionWidth(), smallGeneral.getRegionHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        getRotation()
                );
            }
        } else if (count == 1) {//绘制相关士气,血条
//绘制血条
            // 结合演员的属性绘制表示演员的纹理区域
            if (cam.loopState == 1 || cam.loopState == 0) {
                batch.draw(
                        armyHpRegion,
                        getX() - 39, getY() - 37 + (int) (hpRefy * getScaleY() * scale),
                        //    getX() + getMainGame().sMapScreen.tempActor.getX(), getY() + getMainGame().sMapScreen.tempActor.getY() + hpRefy * getScaleY() * scale,
                        30, 31,//手工测出来的.....
                        //,,
                        armyHpRegion.getRegionWidth(), armyHpRegion.getRegionHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        180
                );
                if (!getIfCanUpd()) {
                    batch.setColor(moraleColor);

                    batch.draw(
                            resource.moraleRegionDAO.getTextureRegion(),
                            getX() - 45, getY() - 23,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                            getOriginX() + resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), getOriginY() + (resource.moraleRegionDAO.getTextureRegion().getRegionHeight()) / 2,//手工测出来的.....
                            resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY,
                            getScaleX() * scale, getScaleY() * scale,
                            getRotation()
                    );
                }
            }
            if (cam.loopState == 1 || cam.loopState == 2) {
                batch.draw(
                        armyHpRegion,
                        cam.getMapW_px()+getX() - 39, getY() - 37 + (int) (hpRefy * getScaleY() * scale),
                        //    getX() + getMainGame().sMapScreen.tempActor.getX(), getY() + getMainGame().sMapScreen.tempActor.getY() + hpRefy * getScaleY() * scale,
                        30, 31,//手工测出来的.....
                        //,,
                        armyHpRegion.getRegionWidth(), armyHpRegion.getRegionHeight(),
                        getScaleX() * scale, getScaleY() * scale,
                        180
                );
                if (!getIfCanUpd()) {
                    batch.setColor(moraleColor);
                    batch.draw(
                            resource.moraleRegionDAO.getTextureRegion(),
                            cam.getMapW_px()+getX() - 45, getY() - 23,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                            getOriginX() + resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), getOriginY() + (resource.moraleRegionDAO.getTextureRegion().getRegionHeight()) / 2,//手工测出来的.....
                            resource.moraleRegionDAO.getTextureRegion().getRegionWidth(), resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY,
                            getScaleX() * scale, getScaleY() * scale,
                            getRotation()
                    );
                }
            }
                batch.setColor(color);

                if (ifDrawAir) {
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        batch.draw(
                                resource.airRegionDAO.getTextureRegion(),
                                //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                                getX() +20 , -35+getY()  ,
                                resource.airRegionDAO.getRefx(),  resource.airRegionDAO.getRefy(),
                                resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                                getRotation()
                        );
                    }
                    if (cam.loopState == 2 || cam.loopState == 1) {
                        batch.draw(
                                resource.airRegionDAO.getTextureRegion(),
                                //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                                cam.getMapW_px() +getX() + 20, -35+getY()  ,
                                resource.airRegionDAO.getRefx(),  resource.airRegionDAO.getRefy(),
                                resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                                getRotation()
                        );
                    }
                }
                if (ifDrawNul) {
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        batch.draw(
                                resource.nulRegionDAO.getTextureRegion(),
                                -36+getX(), -35+getY() ,
                                resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                                resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                                getRotation()
                        );
                    }
                    if (cam.loopState == 2 || cam.loopState == 1) {
                        batch.draw(
                                resource.nulRegionDAO.getTextureRegion(),
                                cam.getMapW_px()-36+getX(), -35+getY() ,
                                resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                                resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                                getRotation()
                        );
                    }
                }
                if (cam.loopState == 0 || cam.loopState == 1) {
                    if (groupDAO != null) {
                        batch.draw(
                                groupDAO.getTextureRegion(),
                                getX() + 28, getY() + -26,
                                getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                                groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() * 2f, getScaleY() * 2f,
                                getRotation()
                        );
                    }
                }
                if (cam.loopState == 1 || cam.loopState == 2) {
                    if (groupDAO != null) {
                        batch.draw(
                                groupDAO.getTextureRegion(),
                                cam.getMapW_px()+getX() + 28, getY() + -26,
                                getOriginX() + groupDAO.getTextureRegion().getRegionWidth() / 2, getOriginY() + groupDAO.getTextureRegion().getRegionHeight() / 2,
                                groupDAO.getTextureRegion().getRegionWidth(), groupDAO.getTextureRegion().getRegionHeight(),
                                getScaleX() * 2f, getScaleY() * 2f,
                                getRotation()
                        );
                    }
                }



            if (ifPlayerCommand && targetState != 0) {//绘制目标
                // 结合演员的属性绘制表示演员的纹理区域
                if (cam.loopState == 1 || cam.loopState == 0) {
                    batch.draw(
                            targetRegionDAO.getTextureRegion(),
                            //getX()+markRefX - game.gameConfig.targetRefv , getY()+markRefY -game.gameConfig.targetRefv,
                            getX() + resource.markRefx-20, getY() + resource.markRefy-20,
                            targetRegionDAO.getRefx(), targetRegionDAO.getRefy(),
                            getWidth(), getHeight(),
                            getScaleX() * scale, getScaleY() * scale,
                            targetAngle
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                    batch.draw(

                            targetRegionDAO.getTextureRegion(),
                            cam.getMapW_px() + getX() + resource.markRefx-20, getY() + resource.markRefy-20,
                            targetRegionDAO.getRefx(), targetRegionDAO.getRefy(),
                            getWidth(), getHeight(),
                            getScaleX() * scale, getScaleY() * scale,
                            targetAngle
                    );
                }
            }
            if (getIfCanUpd()) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(//+resource.markRefx
                            resource.markUpdLvRegionDAO.getTextureRegion(),
                            getX() - resource.markUpdLvRegionDAO.getRefx() - 40, getY() - resource.markUpdLvRegionDAO.getRefy() - 28,
                            getOriginX(), getOriginY(),
                            resource.markUpdLvRegionDAO.getW(), resource.markUpdLvRegionDAO.getH(),
                            getScaleX() * scale, getScaleY() * scale,
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(//+resource.markRefx
                            resource.markUpdLvRegionDAO.getTextureRegion(),
                            cam.getMapW_px() + getX() - resource.markUpdLvRegionDAO.getRefx() - 40, getY() - resource.markUpdLvRegionDAO.getRefy() -28,
                            getOriginX(), getOriginY(),
                            resource.markUpdLvRegionDAO.getW(), resource.markUpdLvRegionDAO.getH(),
                            getScaleX() * scale, getScaleY() * scale,
                            getRotation()
                    );
                }
            }

            if (ifDrawEnergy) {
                if (cam.loopState == 1 || cam.loopState == 0) {
                    batch.draw(
                            energyRegionDAO.getTextureRegion(),
                            getX() - energyRegionDAO.getRefx()-4, getY() - energyRegionDAO.getRefy() - 25,
                            energyRegionDAO.getTextureRegion().getRegionWidth() / 2, energyRegionDAO.getTextureRegion().getRegionHeight() / 2,
                            energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                            (getScaleX() + cam.getZoom()) * scale, (getScaleY() + cam.getZoom()) * scale,
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {
                    batch.draw(
                            energyRegionDAO.getTextureRegion(),
                            cam.getMapW_px() + getX() - energyRegionDAO.getRefx()-4, getY() - energyRegionDAO.getRefy() - 25,
                            energyRegionDAO.getTextureRegion().getRegionWidth() / 2, energyRegionDAO.getTextureRegion().getRegionHeight() / 2,
                            energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                            (getScaleX() + cam.getZoom()) * scale, (getScaleY() + cam.getZoom()) * scale,
                            getRotation()
                    );
                }
            }


        }
    }


    //drawState 0底图 1国旗 2血条 3将军
    public void drawArmyModel(Batch batch, int drawState) {
        float alphaFlash = 0.5f;
       // float zoomChange = 0.5f;

        float zoomChange = resource.getZoomChange();
        if (ifFlash) {
            alphaFlash = resource.getAlphaFlash();
            //zoomChange= 1f;
        }
        if(armyData.getHexagonIndex()==23604){
            int s=0;
        }
/*
        float refX= getMainGame().sMapScreen.tempActor.getX();
        float refY= getMainGame().sMapScreen.tempActor.getY();*/


       /* float refX= getMainGame().sMapScreen.tempActor.getX();
        float refY= getMainGame().sMapScreen.tempActor.getY();*/
        // 备份 batch 原本的 Color
        //  Color tempBatchColor = batch.getColor();

        // 获取演员的 Color
        batch.setColor(Color.WHITE);
        Color color = getColor();

        //只绘制兵牌那种小地图

        if (drawState == 0) {//bt_army

            if (targetState == 3) {
                batch.setColor(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, resource.getAlphaFlash());
            } else if(ifDrawFormation&&scArmyFormation!= null){
                if(ifFlash&&ifPlayerCommand){
                    batch.setColor(1, 1, 1, resource.getAlphaFlash());
                }else{
                    batch.setColor(legionColor.r, legionColor.g, legionColor.b, resource.getAlphaFlash());
                }
            } else {
                batch.setColor(1, 1, 1, resource.getAlphaFlash());
            }

            if(ifDrawFormation&&scArmyFormation!= null){
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            scArmyFormation,
                            -18+(getX() - scArmyFormation.getRegionWidth() / 2) + (1 - zoomChange)*1.5f * scArmyFormation.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                            -18+(getY() - scArmyFormation.getRegionHeight() / 2) + (1 - zoomChange) *1.5f* scArmyFormation.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2 ,
                            getOriginX(), getOriginY(),
                            scArmyFormation.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, scArmyFormation.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange*1.5f , zoomChange*1.5f ,
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {
                    batch.draw(
                            scArmyFormation,
                            cam.getMapW_px()-18+(getX() - scArmyFormation.getRegionWidth() / 2) + (1 - zoomChange)*1.5f * scArmyFormation.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                            -18+(getY() - scArmyFormation.getRegionHeight() / 2) + (1 - zoomChange) *1.5f* scArmyFormation.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2 ,
                            getOriginX(), getOriginY(),
                            scArmyFormation.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, scArmyFormation.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange*1.5f , zoomChange*1.5f ,
                            getRotation()
                    );
                }
            }else if(ifFlash){
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            armyStateDAO.getTextureRegion(),
                            48+getX()  - armyStateDAO.getRefx() + (1 - zoomChange) * armyStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 4,
                            32+getY()  - armyStateDAO.getRefy()  + (1 - zoomChange) * armyStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 4,
                            getOriginX(), getOriginY(),
                            armyStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange/2, zoomChange/2,
                            getRotation()
                    );
                }
                if (cam.loopState == 1 || cam.loopState == 2) {
                    batch.draw(
                            armyStateDAO.getTextureRegion(),
                            cam.getMapW_px()+48+getX()  - armyStateDAO.getRefx() + (1 - zoomChange) * armyStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 4,
                            32+getY()  - armyStateDAO.getRefy()  + (1 - zoomChange) * armyStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 4,
                            getOriginX(), getOriginY(),
                            armyStateDAO.getTextureRegion().getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyStateDAO.getTextureRegion().getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange/2, zoomChange/2,
                            getRotation()
                    );
                }
            }
            if(directArrowRegion!=null){
                batch.setColor(1, 1, 1,  resource.getAlphaFlash()/2);
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            directArrowRegion,
                            -18+(getX() - directArrowRegion.getRegionWidth() / 2) + (1 - zoomChange)*1.5f * directArrowRegion.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                            -18+(getY() - directArrowRegion.getRegionHeight() / 2) + (1 - zoomChange) *1.5f* directArrowRegion.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2 ,
                            getOriginX(), getOriginY(),
                            directArrowRegion.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, directArrowRegion.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange*1.5f , zoomChange*1.5f ,
                            getRotation()
                    );
                }

                if (cam.loopState == 1 || cam.loopState == 2) {
                    batch.draw(
                            directArrowRegion,
                            cam.getMapW_px()-18+(getX() - directArrowRegion.getRegionWidth() / 2) + (1 - zoomChange)*1.5f * directArrowRegion.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE / 2,
                            -18+(getY() - directArrowRegion.getRegionHeight() / 2) + (1 - zoomChange) *1.5f* directArrowRegion.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE / 2 ,
                            getOriginX(), getOriginY(),
                            directArrowRegion.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, directArrowRegion.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                            zoomChange*1.5f , zoomChange*1.5f ,
                            getRotation()
                    );
                }
            }

           /* if (armyData.isPlayer() && !ifFlash) {
                batch.setColor(Color.LIGHT_GRAY);
            } else {
                batch.setColor(DefDAO.getColorByModelMorale(getMainGame(), armyData.getArmyMorale()));
            }*/
            //setArmyModel(0,armyModelTR,armyModelR,armyModelRegion,armyModelRegionF,bgArmyModelTR,bgArmyModelR,bgArmyModelRegion,bgArmyModelRegionF);

            if(armyData.getArmyRound()>1){
                batch.setColor(Color.GRAY);
            }else {
                batch.setColor(DefDAO.getColorByModelMorale(getMainGame(), armyData.getArmyMorale()));
            }


            if (armyData.isUnitGroup() && !armyData.potionIsSea()) {//组合部队

                if (cam.loopState == 0 || cam.loopState == 1) {
                    //战线  1↖ 2↑ 3↗ 4↙ 5↓ 6↘  位置 1← 2↖ 3↗ 4→ 5↙ 6↘ 绘制顺序 2,3,1,0,4,5,6
                    if (modelE2 != null && armyModelR2 != null) {
                        if (bgArmyModelTR2 != null) {
                            batch.draw(
                                    bgArmyModelR2,
                                    -10+ getX() + bgModelRefX2 , 15+getY() + bgModelRefY2 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR2.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR2.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale2, getScaleY() * modelScale2,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR2,
                                -10+getX() + modelRefX2 , 15+getY() + modelRefY2 ,
                                getOriginX(), getOriginY(),
                                armyModelR2.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR2.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale2, getScaleY() * bgModelScale2,
                                getRotation()
                        );
                    }
                    if (modelE3 != null && armyModelR3 != null) {
                        if (bgArmyModelTR3 != null) {
                            batch.draw(
                                    bgArmyModelR3,
                                    12+ getX() + bgModelRefX3, 15+getY() + bgModelRefY3 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR3.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR3.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale3, getScaleY() * modelScale3,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR3,
                                12+getX() + modelRefX3 , 15+getY() + modelRefY3 ,
                                getOriginX(), getOriginY(),
                                armyModelR3.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR3.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale3, getScaleY() * bgModelScale3,
                                getRotation()
                        );
                    }
                    if (modelE1 != null && armyModelR1 != null) {
                        if (bgArmyModelTR1 != null) {
                            batch.draw(
                                    bgArmyModelR1,
                                    -21+getX() + bgModelRefX1 , -2+getY() + bgModelRefY1 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR1.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR1.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale1, getScaleY() * modelScale1,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR1,
                                -21+getX() + modelRefX1 , -2+getY() + modelRefY1,
                                getOriginX(), getOriginY(),
                                armyModelR1.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR1.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale1, getScaleY() * bgModelScale1,
                                getRotation()
                        );
                    }
                    if (modelE4 != null && armyModelR4 != null) {
                        if (bgArmyModelTR4 != null) {
                            batch.draw(
                                    bgArmyModelR4,
                                    23+   getX() + bgModelRefX4 , -2+getY() + bgModelRefY4 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR4.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR4.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * bgModelScale4, getScaleY() * bgModelScale4,
                                    getRotation()
                            );
                        }
                        if (armyModelR4 != null) {
                            batch.draw(
                                    armyModelR4,
                                    23+ getX() + modelRefX4 , -2+getY() + modelRefY4 ,
                                    getOriginX(), getOriginY(),
                                    armyModelR4.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR4.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale4, getScaleY() * modelScale4,
                                    getRotation()
                            );
                        }
                    }
                    if (bgArmyModelTR != null) {
                        batch.draw(
                                bgArmyModelR,
                                1+getX() + bgModelRefX0 , -2+getY() + bgModelRefY0,
                                getOriginX(), getOriginY(),
                                bgArmyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale0, getScaleY() * bgModelScale0,
                                getRotation()
                        );
                    }
                    if (armyModelR != null) {
                        batch.draw(
                                armyModelR,
                                1+getX() + modelRefX0 , -2+getY() + modelRefY0 ,//36 27
                                getOriginX(), getOriginY(),
                                armyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * modelScale0, getScaleY() * modelScale0,
                                getRotation()
                        );
                    }
                    if (modelE5 != null && armyModelR5 != null) {
                        if (bgArmyModelTR5 != null) {
                            batch.draw(
                                    bgArmyModelR5,
                                    -10+    getX() + bgModelRefX5 , -19+getY() + bgModelRefY5 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR5.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR5.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * bgModelScale5, getScaleY() * bgModelScale5,
                                    getRotation()
                            );
                        }
                        if (armyModelR5 != null) {
                            batch.draw(
                                    armyModelR5,
                                    -10+    getX() + modelRefX5 , -19+getY() + modelRefY5 ,
                                    getOriginX(), getOriginY(),
                                    armyModelR5.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR5.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale5, getScaleY() * modelScale5,
                                    getRotation()
                            );
                        }
                    }
                    if (modelE6 != null && armyModelR6 != null) {
                        if (bgArmyModelTR6 != null) {
                            batch.draw(
                                    bgArmyModelR6,
                                    12+ getX() + bgModelRefX6, -19+getY() + bgModelRefY6 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR6.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR6.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * bgModelScale6, getScaleY() * bgModelScale6,
                                    getRotation()
                            );
                        }
                        if (armyModelR6 != null) {
                            batch.draw(
                                    armyModelR6,
                                    12+ getX() + modelRefX6 , -19+getY() + modelRefY6 ,
                                    getOriginX(), getOriginY(),
                                    armyModelR6.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR6.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale6, getScaleY() * modelScale6,
                                    getRotation()
                            );
                        }
                    }

                }
                if (cam.loopState == 1 || cam.loopState == 2) {
                    //战线  1↖ 2↑ 3↗ 4↙ 5↓ 6↘  位置 1← 2↖ 3↗ 4→ 5↙ 6↘ 绘制顺序 2,3,1,0,4,5,6
                    if (modelE2 != null && armyModelR2 != null) {
                        if (bgArmyModelTR2 != null) {
                            batch.draw(
                                    bgArmyModelR2,
                                    cam.getMapW_px()+-10+ getX() + bgModelRefX2 , 15+getY() + bgModelRefY2 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR2.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR2.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale2, getScaleY() * modelScale2,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR2,
                                cam.getMapW_px()+-10+getX() + modelRefX2 , 15+getY() + modelRefY2 ,
                                getOriginX(), getOriginY(),
                                armyModelR2.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR2.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale2, getScaleY() * bgModelScale2,
                                getRotation()
                        );
                    }
                    if (modelE3 != null && armyModelR3 != null) {
                        if (bgArmyModelTR3 != null) {
                            batch.draw(
                                    bgArmyModelR3,
                                    cam.getMapW_px()+12+ getX() + bgModelRefX3, 15+getY() + bgModelRefY3 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR3.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR3.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale3, getScaleY() * modelScale3,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR3,
                                cam.getMapW_px()+12+getX() + modelRefX3 , 15+getY() + modelRefY3 ,
                                getOriginX(), getOriginY(),
                                armyModelR3.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR3.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale3, getScaleY() * bgModelScale3,
                                getRotation()
                        );
                    }
                    if (modelE1 != null && armyModelR1 != null) {
                        if (bgArmyModelTR1 != null) {
                            batch.draw(
                                    bgArmyModelR1,
                                    cam.getMapW_px()+-21+getX() + bgModelRefX1 , -2+getY() + bgModelRefY1 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR1.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR1.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale1, getScaleY() * modelScale1,
                                    getRotation()
                            );
                        }
                        batch.draw(
                                armyModelR1,
                                cam.getMapW_px()+-21+getX() + modelRefX1 , -2+getY() + modelRefY1,
                                getOriginX(), getOriginY(),
                                armyModelR1.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR1.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale1, getScaleY() * bgModelScale1,
                                getRotation()
                        );
                    }
                    if (modelE4 != null && armyModelR4 != null) {
                        if (bgArmyModelTR4 != null) {
                            batch.draw(
                                    bgArmyModelR4,
                                    cam.getMapW_px()+23+   getX() + bgModelRefX4 , -2+getY() + bgModelRefY4 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR4.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR4.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * bgModelScale4, getScaleY() * bgModelScale4,
                                    getRotation()
                            );
                        }
                        if (armyModelR4 != null) {
                            batch.draw(
                                    armyModelR4,
                                    cam.getMapW_px()+23+ getX() + modelRefX4 , -2+getY() + modelRefY4 ,
                                    getOriginX(), getOriginY(),
                                    armyModelR4.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR4.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale4, getScaleY() * modelScale4,
                                    getRotation()
                            );
                        }
                    }
                    if (bgArmyModelTR != null) {
                        batch.draw(
                                bgArmyModelR,
                                cam.getMapW_px()+1+getX() + bgModelRefX0 , -2+getY() + bgModelRefY0,
                                getOriginX(), getOriginY(),
                                bgArmyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale0, getScaleY() * bgModelScale0,
                                getRotation()
                        );
                    }
                    if (armyModelR != null) {
                        batch.draw(
                                armyModelR,
                                cam.getMapW_px()+1+getX() + modelRefX0 , -2+getY() + modelRefY0 ,//36 27
                                getOriginX(), getOriginY(),
                                armyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * modelScale0, getScaleY() * modelScale0,
                                getRotation()
                        );
                    }
                    if (modelE5 != null && armyModelR5 != null) {
                        if (bgArmyModelTR5 != null) {
                            batch.draw(
                                    bgArmyModelR5,
                                    cam.getMapW_px()+-10+    getX() + bgModelRefX5 , -19+getY() + bgModelRefY5 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR5.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR5.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * bgModelScale5, getScaleY() * bgModelScale5,
                                    getRotation()
                            );
                        }
                        if (armyModelR5 != null) {
                            batch.draw(
                                    armyModelR5,
                                    cam.getMapW_px()+-10+    getX() + modelRefX5 , -19+getY() + modelRefY5 ,
                                    getOriginX(), getOriginY(),
                                    armyModelR5.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR5.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale5, getScaleY() * modelScale5,
                                    getRotation()
                            );
                        }
                    }
                    if (modelE6 != null && armyModelR6 != null) {
                        if (bgArmyModelTR6 != null) {
                            batch.draw(
                                    bgArmyModelR6,
                                    cam.getMapW_px()+12+ getX() + bgModelRefX6, -19+getY() + bgModelRefY6 ,
                                    getOriginX(), getOriginY(),
                                    bgArmyModelR6.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR6.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * bgModelScale6, getScaleY() * bgModelScale6,
                                    getRotation()
                            );
                        }
                        if (armyModelR6 != null) {
                            batch.draw(
                                    armyModelR6,
                                    cam.getMapW_px()+12+ getX() + modelRefX6 , -19+getY() + modelRefY6 ,
                                    getOriginX(), getOriginY(),
                                    armyModelR6.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR6.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                    getScaleX() * modelScale6, getScaleY() * modelScale6,
                                    getRotation()
                            );
                        }
                    }
                }
            } else {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    if (bgArmyModelTR != null) {
                        batch.draw(
                                bgArmyModelR,
                                1+getX() + bgModelRefX0 , -2+getY() + bgModelRefY0 ,
                                getOriginX(), getOriginY(),
                                bgArmyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale0, getScaleY() * bgModelScale0,
                                getRotation()
                        );
                    }
                    // 结合演员的属性绘制表示演员的纹理区域
                    if (armyModelR != null) {
                        batch.draw(
                                armyModelR,
                                1+getX() + modelRefX0 , -2+getY() + modelRefY0 ,
                                getOriginX(), getOriginY(),
                                armyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * modelScale0, getScaleY() * modelScale0,
                                getRotation()
                        );
                    }
                }
                if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                    if (bgArmyModelTR != null) {
                        batch.draw(
                                bgArmyModelR,
                                cam.getMapW_px()+1+getX() + bgModelRefX0 , -2+getY() + bgModelRefY0 ,
                                getOriginX(), getOriginY(),
                                bgArmyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, bgArmyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * bgModelScale0, getScaleY() * bgModelScale0,
                                getRotation()
                        );
                    }
                    // 结合演员的属性绘制表示演员的纹理区域
                    if (armyModelR != null) {
                        batch.draw(
                                armyModelR,
                                cam.getMapW_px()+1+getX() + modelRefX0 , -2+getY() + modelRefY0 ,
                                getOriginX(), getOriginY(),
                                armyModelR.getRegionWidth() * ResDefaultConfig.Map.MAP_SCALE, armyModelR.getRegionHeight() * ResDefaultConfig.Map.MAP_SCALE,
                                getScaleX() * modelScale0, getScaleY() * modelScale0,
                                getRotation()
                        );
                    }
                }
                batch.setColor(color);
            }
        } else if (drawState == 1) {//bt_flags
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        flag,
                        -9+getX() , -33+getY() ,
                        //   getX()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                        getOriginX(), getOriginY(),
                        flagW, flagH,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if (cam.loopState == 1 || cam.loopState == 2) {
                batch.draw(
                        flag,
                        cam.getMapW_px()+-9+getX()  , -33+getY() ,
                        // getX()+getMapW_px()- game.sMapScreen.tempActor.getX(), getY()- game.sMapScreen.tempActor.getY(),
                        getOriginX(), getOriginY(),
                        flagW, flagH,
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
        } else if (drawState == 2) {


            batch.setColor(color);
            if (cam.loopState == 0 || cam.loopState == 1) {
                batch.draw(
                        armyHpRegion,
                        //getX()-getMainGame().sMapScreen.tempActor.getX(), getY()-getMainGame().sMapScreen.tempActor.getY()+ hpRefy*getScaleY()*0.4f,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        -31+getX() , -57+getY()  + hpRefy * getScaleY() * 0.4f,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        21, 22,//手工测出来的.....
                        // getMainGame.sMapScreen.tempActor.getX(),getMainGame().sMapScreen.tempActor.getY(),
                        flagW, hpH,
                        getScaleX(), getScaleY(),
                        180
                );
            }
            if (cam.loopState == 2 || cam.loopState == 1) {
                batch.draw(
                        armyHpRegion,
                        cam.getMapW_px() +-31+ getX(), -57+getY()  + hpRefy * getScaleY() * 0.4f,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                        21, 22,//手工测出来的.....
                        // game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),
                        flagW, hpH,
                        getScaleX(), getScaleY(),
                        180
                );
            }


            if (!getIfCanUpd()) {
                batch.setColor(moraleColor);
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            resource.moraleRegionDAO.getTextureRegion(),
                            -13+getX() , -30+getY() ,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                            getOriginX(), getOriginY(),//手工测出来的.....
                            resource.moraleRegionDAO.getTextureRegion().getRegionWidth() * 0.4f, (resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY) * 0.4f,
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            resource.moraleRegionDAO.getTextureRegion(),
                            cam.getMapW_px() +-13+ getX() , -30+getY() ,//armyHpRegion.getSprite().getRegionHeight()*getScaleY()*hpRate
                            getOriginX(), getOriginY(),//手工测出来的.....
                            resource.moraleRegionDAO.getTextureRegion().getRegionWidth() * 0.4f, (resource.moraleRegionDAO.getTextureRegion().getRegionHeight() - moraleRefY) * 0.4f,
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
            }


            batch.setColor(color);


            if (ifDrawAir) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            resource.airRegionDAO.getTextureRegion(),
                            //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                            getX() +20 , -35+getY()  ,
                            resource.airRegionDAO.getRefx(),  resource.airRegionDAO.getRefy(),
                            resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            resource.airRegionDAO.getTextureRegion(),
                            //  getX()+airRegionDAO.getRefx()-flagW*2, getY()+airRegionDAO.getRefy()+armyHpRegion.getRegionHeight()*0.4f,
                            cam.getMapW_px() +getX() + 20, -35+getY()  ,
                            resource.airRegionDAO.getRefx(),  resource.airRegionDAO.getRefy(),
                            resource.airRegionDAO.getTextureRegion().getRegionWidth(), resource.airRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
            }
            if (ifDrawNul) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            resource.nulRegionDAO.getTextureRegion(),
                            -36+getX(), -35+getY() ,
                            resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                            resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            resource.nulRegionDAO.getTextureRegion(),
                            cam.getMapW_px()-36+getX(), -35+getY() ,
                            resource.nulRegionDAO.getRefx(), resource.nulRegionDAO.getRefy(),
                            resource.nulRegionDAO.getTextureRegion().getRegionWidth(), resource.nulRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
            }
            if (ifDrawEnergy) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            energyRegionDAO.getTextureRegion(),
                            -4+getX() + - energyRegionDAO.getRefx() , -25+getY()  - energyRegionDAO.getRefy() ,
                            getOriginX(), getOriginY(),
                            energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            energyRegionDAO.getTextureRegion(),
                            cam.getMapW_px()-4+getX() + - energyRegionDAO.getRefx() , -25+getY()  - energyRegionDAO.getRefy() ,
                            getOriginX(), getOriginY(),
                            energyRegionDAO.getTextureRegion().getRegionWidth(), energyRegionDAO.getTextureRegion().getRegionHeight(),
                            getScaleX() + cam.getZoom(), getScaleY() + cam.getZoom(),
                            getRotation()
                    );
                }
            }

            batch.setColor(1, 1, 1, 1);

            if (groupDAO != null) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(
                            groupDAO.getTextureRegion(),
                            8+getX() , -31+getY() ,
                            getOriginX(), getOriginY(),
                            groupDAO.getTextureRegion().getRegionWidth() * 0.7f, groupDAO.getTextureRegion().getRegionHeight() * 0.7f,
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(
                            groupDAO.getTextureRegion(),
                            cam.getMapW_px()+8+getX() , -31+getY() ,
                            getOriginX(), getOriginY(),
                            groupDAO.getTextureRegion().getRegionWidth() * 0.7f, groupDAO.getTextureRegion().getRegionHeight() * 0.7f,
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
            }
            //大比例时的军衔绘制
            if (rank != null) {//绘制军衔
                // 将演员的 Color 结合 parentAlpha 设置到 batch

                if (ifGeneral) {
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        batch.draw(//+resource.markRefx
                                rank.getTextureRegion(),
                                1+ getX() - rank.getRefx() * 0.3f , -5+getY() - rank.getRefy() * 0.2f ,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                0.3f, 0.3f,
                                getRotation()
                        );
                    }
                    if (cam.loopState == 1 || cam.loopState == 2) {
                        batch.draw(//+resource.markRefx
                                rank.getTextureRegion(),
                                cam.getMapW_px() +1+ getX() - rank.getRefx() * 0.3f , -5+getY() - rank.getRefy() * 0.2f ,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                0.3f, 0.3f,
                                getRotation()
                        );
                    }
                } else {
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        batch.draw(//+resource.markRefx
                                rank.getTextureRegion(),
                                1+getX() - rank.getRefx()*0.7f , -5+getY() - rank.getRefy()*0.7f ,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                0.7f, 0.7f,
                                getRotation()
                        );
                    }
                    if (cam.loopState == 2 || cam.loopState == 1) {
                        batch.draw(//+resource.markRefx
                                rank.getTextureRegion(),
                                cam.getMapW_px() +1+ getX() - rank.getRefx()*0.7f , -5+getY() - rank.getRefy()*0.7f ,
                                getOriginX(), getOriginY(),
                                rank.getW(), rank.getH(),
                                0.7f, 0.7f,
                                getRotation()
                        );
                    }
                }
            }
            if (getIfCanUpd()) {
                if (cam.loopState == 0 || cam.loopState == 1) {
                    batch.draw(//+resource.markRefx
                            resource.markUpdLvRegionDAO.getTextureRegion(),
                            -20+getX() - resource.markUpdLvRegionDAO.getRefx(), -25+getY() - resource.markUpdLvRegionDAO.getRefy(),
                            getOriginX(), getOriginY(),
                            resource.markUpdLvRegionDAO.getW(), resource.markUpdLvRegionDAO.getH(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                if (cam.loopState == 2 || cam.loopState == 1) {
                    batch.draw(//+resource.markRefx
                            resource.markUpdLvRegionDAO.getTextureRegion(),
                            cam.getMapW_px() +-20+ getX() - resource.markUpdLvRegionDAO.getRefx(), -25+getY() - resource.markUpdLvRegionDAO.getRefy() ,
                            getOriginX(), getOriginY(),
                            resource.markUpdLvRegionDAO.getW(), resource.markUpdLvRegionDAO.getH(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
            }


        } else if (drawState == 3) {
            if (smallGeneral == null) {
                return;
            }
            if (resource.needLucency(getHexagon())) {
                return;
            }
            if (cam.loopState == 1 || cam.loopState == 0) {
                batch.draw(
                        resource.smallBgArrowRegionDAO.getTextureRegion(),
                        -9+getX() , 13+getY() ,
                        getOriginX(), getOriginY(),
                        resource.smallBgArrowRegionDAO.getW(), resource.smallBgArrowRegionDAO.getH(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
                batch.draw(
                        smallGeneralBg,
                        -18+getX() , 20+getY() ,
                        getOriginX(), getOriginY(),
                        smallGeneralBg.getRegionWidth(), smallGeneralBg.getRegionHeight(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
                batch.draw(
                        smallGeneral,
                        -15+getX() , 23+getY() ,
                        getOriginX(), getOriginY(),
                        smallGeneral.getRegionWidth(), smallGeneral.getRegionHeight(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
            }
            if (cam.loopState == 1 || cam.loopState == 2) {//如果循环,则添加一部分
                batch.draw(
                        resource.smallBgArrowRegionDAO.getTextureRegion(),
                        cam.getMapW_px()+-9+getX() , 13+getY() ,
                        getOriginX(), getOriginY(),
                        resource.smallBgArrowRegionDAO.getW(), resource.smallBgArrowRegionDAO.getH(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
                batch.draw(
                        smallGeneralBg,
                        cam.getMapW_px()+-18+getX() , 20+getY() ,
                        getOriginX(), getOriginY(),
                        smallGeneralBg.getRegionWidth(), smallGeneralBg.getRegionHeight(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
                batch.draw(
                        smallGeneral,
                        cam.getMapW_px()+-15+getX() , 23+getY() ,
                        getOriginX(), getOriginY(),
                        smallGeneral.getRegionWidth(), smallGeneral.getRegionHeight(),
                        getScaleX() * 0.7f, getScaleY() * 0.7f,
                        getRotation()
                );
            }
        }
    }


    //绘制攻击  TODO 一些固定方向的兵模
    // -1远程 0全部 1↖ 2↑ 3↗ 4↙ 5↓ 6↘
    public void drawAttack(int direct,float damageTime) {
        if(!cam.inScreen(armyData.getHexagonIndex())){
            return;
        }
        int modeType=0;
        XmlReader.Element xml=null;
        //组合部队
        if(armyData.isUnitGroup()&&!armyData.potionIsSea()){
            if(cam.loopState==0||cam.loopState==1) {
                if(modelE2!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(2)>1)||direct==0||direct==1||direct==2)){
                    modeType= modelE2.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE2.getChildByName("attackL");
                        }else {
                            xml=  modelE2.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE2.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR2!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                getX() + modelRefX2 + 10 - 20
                                , getY() + (modelRefY2 + 35) - 20, armyModelR2.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale2);
                    }
                    armyModelTR2 = getMainGame().getImgLists().getTextureByName(modelE2.get("attackImgName",armyModelTR2.getName()));
                    armyModelRegion2 = armyModelTR2.getTextureRegion();
                    armyModelRegionF2 = getMainGame().getImgLists().getFlipRegion(armyModelTR2.getName());
                }
                if(modelE3!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(3)>1)||direct==0||direct==2||direct==3)){
                    modeType= modelE3.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE3.getChildByName("attackL");
                        }else {
                            xml=  modelE3.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE3.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR3!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                getX() + modelRefX3 + 32 - 20
                                , getY() + (modelRefY3 + 35) - 20, armyModelR3.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale3);
                    }
                    armyModelTR3 = getMainGame().getImgLists().getTextureByName(modelE3.get("attackImgName", armyModelTR3.getName()));
                    armyModelRegion3 = armyModelTR3.getTextureRegion();
                    armyModelRegionF3 = getMainGame().getImgLists().getFlipRegion(armyModelTR3.getName());
                }
                if(modelE1!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(1)>1)||direct==0||direct==1||direct==4)){
                    modeType= modelE1.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE1.getChildByName("attackL");
                        }else {
                            xml=  modelE1.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE1.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR1!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                getX() + modelRefX1 + -1 - 20
                                , getY() + (modelRefY1 + 18) - 20, armyModelR1.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale1);
                    }
                    armyModelTR1 = getMainGame().getImgLists().getTextureByName(modelE1.get("attackImgName", armyModelTR1.getName()));
                    armyModelRegion1 = armyModelTR1.getTextureRegion();
                    armyModelRegionF1 = getMainGame().getImgLists().getFlipRegion(armyModelTR1.getName());
                }
                if(modelE4!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(4)>1)||direct==0||direct==3||direct==6)){
                    modeType= modelE4.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE4.getChildByName("attackL");
                        }else {
                            xml=  modelE4.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE4.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR4!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                getX() + modelRefX4 + 43 - 20
                                , getY() + (modelRefY4 + 18) - 20, armyModelR4.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale4);
                    }
                    armyModelTR4 = getMainGame().getImgLists().getTextureByName(modelE4.get("attackImgName",armyModelTR4.getName()));
                    armyModelRegion4 = armyModelTR4.getTextureRegion();
                    armyModelRegionF4 = getMainGame().getImgLists().getFlipRegion(armyModelTR4.getName());
                }
                if(modelE!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(0)>1)||!armyData.ifUnitGroupIsFormation(direct,false))){
                   modeType= modelE.getInt("modelType", 0);
                   if(modeType == 1||modeType == 3){
                       if(armyData.getArmyDirection()==0){
                           xml=  modelE.getChildByName("attackL");
                       }else {
                           xml=  modelE.getChildByName("attackR");
                       }
                   }else {
                       xml=  modelE.getChildByName("attack");
                   }
                   if(xml!=null&&armyModelR!=null&&armyData!=null){
                       damageTime+=0.01f;
                       getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                       getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                               getX() + modelRefX0 + 21-20
                               , getY() + (modelRefY0 + 18)-20, armyModelR.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                               xml.getChildrenByName("fire"), modelScale0);
                   }
                   if(!ComUtil.isEmpty(modelE.get("attackImgName",""))){
                       armyModelTR = getMainGame().getImgLists().getTextureByName(modelE.get("attackImgName", armyModelTR.getName()));
                       armyModelRegion = armyModelTR.getTextureRegion();
                       armyModelRegionF = getMainGame().getImgLists().getFlipRegion(armyModelTR.getName());
                   }
                }
                if(modelE5!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(5)>1)||direct==0||direct==4||direct==5)){
                    modeType= modelE5.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE5.getChildByName("attackL");
                        }else {
                            xml=  modelE5.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE5.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR5!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                getX() + modelRefX5 + 10 - 20
                                , getY() + (modelRefY5 + 1) - 20, armyModelR5.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale5);
                    }
                    armyModelTR5 = getMainGame().getImgLists().getTextureByName(modelE5.get("attackImgName",armyModelTR5.getName()));
                    armyModelRegion5 = armyModelTR5.getTextureRegion();
                    armyModelRegionF5 = getMainGame().getImgLists().getFlipRegion(armyModelTR5.getName());
                }
                if(modelE6!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(6)>1)||direct==0||direct==5||direct==6)){
                    modeType= modelE6.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE6.getChildByName("attackL");
                        }else {
                            xml=  modelE6.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE6.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR6!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                getX() + modelRefX6 + 32 - 20
                                , getY() + (modelRefY6 + 1) - 20, armyModelR6.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale6);
                    }
                    armyModelTR6 = getMainGame().getImgLists().getTextureByName(modelE6.get("attackImgName", armyModelTR6.getName()));
                    armyModelRegion6 = armyModelTR6.getTextureRegion();
                    armyModelRegionF6 = getMainGame().getImgLists().getFlipRegion(armyModelTR6.getName());
                }
            }
            if(cam.loopState==1||cam.loopState==2) {
                if(modelE2!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(2)>1)||direct==0||direct==1||direct==2)){
                    modeType= modelE2.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE2.getChildByName("attackL");
                        }else {
                            xml=  modelE2.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE2.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR2!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX2 + 10 - 20
                                , getY() + (modelRefY2 + 35) - 20, armyModelR2.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale2);
                    }
                    armyModelTR2 = getMainGame().getImgLists().getTextureByName(modelE2.get("attackImgName", armyModelTR2.getName()));
                    armyModelRegion2 = armyModelTR2.getTextureRegion();
                    armyModelRegionF2 = getMainGame().getImgLists().getFlipRegion(armyModelTR2.getName());
                }
                if(modelE3!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(3)>1)||direct==0||direct==2||direct==3)){
                    modeType= modelE3.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE3.getChildByName("attackL");
                        }else {
                            xml=  modelE3.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE3.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR3!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX3 + 32 - 20
                                , getY() + (modelRefY3 + 35) - 20, armyModelR3.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale3);
                    }
                    armyModelTR3 = getMainGame().getImgLists().getTextureByName(modelE3.get("attackImgName", armyModelTR3.getName()));
                    armyModelRegion3 = armyModelTR3.getTextureRegion();
                    armyModelRegionF3 = getMainGame().getImgLists().getFlipRegion(armyModelTR3.getName());
                }
                if(modelE1!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(1)>1)||direct==0||direct==1||direct==4)){
                    modeType= modelE1.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE1.getChildByName("attackL");
                        }else {
                            xml=  modelE1.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE1.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR1!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX1 + -1 - 20
                                , getY() + (modelRefY1 + 18) - 20, armyModelR1.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale1);
                    }
                    armyModelTR1 = getMainGame().getImgLists().getTextureByName(modelE1.get("attackImgName", armyModelTR1.getName()));
                    armyModelRegion1 = armyModelTR1.getTextureRegion();
                    armyModelRegionF1 = getMainGame().getImgLists().getFlipRegion(armyModelTR1.getName());
                }
                if(modelE4!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(4)>1)||direct==0||direct==3||direct==6)){
                    modeType= modelE4.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE4.getChildByName("attackL");
                        }else {
                            xml=  modelE4.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE4.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR4!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX4 + 43 - 20
                                , getY() + (modelRefY4 + 18) - 20, armyModelR4.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale4);
                    }
                    armyModelTR4 = getMainGame().getImgLists().getTextureByName(modelE4.get("attackImgName", armyModelTR4.getName()));
                    armyModelRegion4 = armyModelTR4.getTextureRegion();
                    armyModelRegionF4 = getMainGame().getImgLists().getFlipRegion(armyModelTR4.getName());
                }
                if(modelE!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(0)>1)||!armyData.ifUnitGroupIsFormation(direct,false))){
                    modeType= modelE.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE.getChildByName("attackL");
                        }else {
                            xml=  modelE.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX0 + 21 - 20
                                , getY() + (modelRefY0 + 18) - 20, armyModelR.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale0);
                    }
                    armyModelTR = getMainGame().getImgLists().getTextureByName(modelE.get("attackImgName", armyModelTR.getName()));
                    armyModelRegion = armyModelTR.getTextureRegion();
                    armyModelRegionF = getMainGame().getImgLists().getFlipRegion(armyModelTR.getName());
                }
                if(modelE5!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(5)>1)||direct==0||direct==4||direct==5)){
                    modeType= modelE5.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE5.getChildByName("attackL");
                        }else {
                            xml=  modelE5.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE5.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR5!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX5 + 10 - 20
                                , getY() + (modelRefY5 + 1) - 20, armyModelR5.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale5);
                    }
                    armyModelTR5 = getMainGame().getImgLists().getTextureByName(modelE5.get("attackImgName",armyModelTR5.getName()));
                    armyModelRegion5 = armyModelTR5.getTextureRegion();
                    armyModelRegionF5 = getMainGame().getImgLists().getFlipRegion(armyModelTR5.getName());
                }
                if(modelE6!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(6)>1)||direct==0||direct==5||direct==6)){
                    modeType= modelE6.getInt("modelType", 0);
                    if(modeType == 1){
                        if(armyData.getArmyDirection()==0){
                            xml=  modelE6.getChildByName("attackL");
                        }else {
                            xml=  modelE6.getChildByName("attackR");
                        }
                    }else {
                        xml=  modelE6.getChildByName("attack");
                    }
                    if(xml!=null&&armyModelR6!=null&&armyData!=null) {
                        damageTime+=0.01f;
                        getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX6 + 32 - 20
                                , getY() + (modelRefY6 + 1) - 20, armyModelR6.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale6);
                    }
                    armyModelTR6 = getMainGame().getImgLists().getTextureByName(modelE6.get("attackImgName", armyModelTR6.getName()));
                    armyModelRegion6 = armyModelTR6.getTextureRegion();
                    armyModelRegionF6 = getMainGame().getImgLists().getFlipRegion(armyModelTR6.getName());
                }
            }
        }else {
            if(modelE!=null&&((direct==-1&&armyData.getUnitGroupMaxRange(0)>1)||direct>=0)) {
                modeType = modelE.getInt("modelType", 0);
                if (modeType == 1) {
                    if (armyData.getArmyDirection() == 0) {
                        xml = modelE.getChildByName("attackL");
                    } else {
                        xml = modelE.getChildByName("attackR");
                    }
                } else {
                    xml = modelE.getChildByName("attack");
                }
                if(xml!=null&&armyModelR!=null&&armyData!=null) {
                    damageTime+=0.01f;
                    getMainGame().playSound(getStage(),xml.get("sound","bombard"),damageTime+xml.getFloat("atSoundTimie",0f));
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX0 + 21 - 20
                                , getY() + (modelRefY0 + 18) - 20, armyModelR.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale0);
                    }
                    if (cam.loopState == 1 || cam.loopState == 2) {
                        getMainGame().sMapScreen.smapGameStage.effectDAO.drawFiresEffect(-1,
                                cam.getMapW_px() + getX() + modelRefX0 + 21 - 20
                                , getY() + (modelRefY0 + 18) - 20, armyModelR.getRegionWidth(), damageTime, armyData.getArmyDirection() == 1,
                                xml.getChildrenByName("fire"), modelScale0);
                    }
                }
                armyModelTR = getMainGame().getImgLists().getTextureByName(modelE.get("attackImgName", armyModelTR.getName()));
                armyModelRegion = armyModelTR.getTextureRegion();
                armyModelRegionF = getMainGame().getImgLists().getFlipRegion(armyModelTR.getName());
            }
        }

        //setArmyModel(0,armyModelTR,armyModelR,armyModelRegion,armyModelRegionF,bgArmyModelTR,bgArmyModelR,bgArmyModelRegion,bgArmyModelRegionF);

       updArmyModelDirect();
        RunnableAction resetModelAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                updArmyModel();
                //Gdx.app.log("恢复兵模","123");
            }
        });
        //2秒以后恢复原来的模型
       addAction(Actions.sequence(Actions.delay(damageTime + 1), resetModelAction));
    }

    public boolean ifDrawGeneral(){
        return smallGeneral!=null;
    }


    public boolean inScreen() {
        if(cam!=null&&cam.inScreen(armyData.getHexagonIndex())){
            return true;
        }
        return false;
    }
}

