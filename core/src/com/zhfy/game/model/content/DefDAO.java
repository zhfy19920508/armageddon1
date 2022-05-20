package com.zhfy.game.model.content;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.model.content.conversion.Fb2Smap;

import java.util.ArrayList;
import java.util.List;

public class DefDAO {
    //用来处理def信息类
    /*private MainGame game;

    public DefDAO(MainGame game){
        this.game=game;
    }*/
    private DefDAO() {
        throw new IllegalStateException("DefDAO class");
    }

    //---------------------------------static方法------------------------------------------------
    public static List<Color> getAllCountryColorByXml(XmlIntDAO dao){
        XmlReader.Element root = dao.e;
        List<Color> rs= new ArrayList<Color>();
        Array<XmlReader.Element> xmlFiles =root.getChildrenByName("country");
        for(int c=0;c<xmlFiles.size;c++){
            Color color=new Color((float)xmlFiles.get(c).getInt("r")/255,(float)xmlFiles.get(c).getInt("g")/255,(float)xmlFiles.get(c).getInt("b")/255,(float)xmlFiles.get(c).getInt("a")/255);
            rs.add(color);
            //Gdx.app.log("legionColor","color:"+color+" r:"+Float.parseFloat(bm2.getBm2_49())+" g:"+Float.parseFloat(bm2.getBm2_50())+" b:"+Float.parseFloat(bm2.getBm2_51())+" a:"+Float.parseFloat(bm2.getBm2_52()));
        }
        return  rs;
    }

    //获得装饰Map
    public static ObjectMap<String, String> getTerrainimgMap(XmlIntDAO dao,boolean sort){
        ObjectMap<String, String> terrainimgMap = new ObjectMap<String, String>();
        Array<XmlReader.Element> xmlFiles =dao.e.getChildrenByName("mapterrain");
        for(int i=0;i<xmlFiles.size;i++){
            if(sort){
                terrainimgMap.put( xmlFiles.get(i).get("pixmapTile").replace(".png", ""),xmlFiles.get(i).get("id") + "_" + xmlFiles.get(i).get("idx"));
            }else{
                terrainimgMap.put(xmlFiles.get(i).get("id") + "_" + xmlFiles.get(i).get("idx"), xmlFiles.get(i).get("pixmapTile").replace(".png", ""));
            }
        }
        return terrainimgMap;
    }

    //根据id获取图片名,主要是一些用到
    public static String getImgNameById(int id){
        //3 预览图
        //301战斗中
        //302可参战

        switch(id) {
            case 301:
                return ResDefaultConfig.Image.BATTLE_MARK_1;
            case 302:
                return ResDefaultConfig.Image.BATTLE_MARK_2;
            default:
                return null;
        }
    }



    public static String getNumberIcon(int number) {
        switch (number){

            case 0:return "icon_0";//
            case 1:return "icon_1";//
            case 2:return "icon_2";//
            case 3:return "icon_3";//
            case 4:return "icon_4";//
            case 5:return "icon_5";//
            case 6:return "icon_6";//
            case 7:return "icon_7";//
            case 8:return "icon_8";//
            case 9:return "icon_9";//
        }
        return "icon_max";
    }


    public static String getSCGroupImageName(int number) {
        if(number<=0){
            return "sc_group1";
        }
        if(number>0&&number<=9){
            return "sc_group"+number;
        }
        return "sc_group9";

    }



    //获得绘制标记
    //1 行动标记 5,6,7暂时空缺
    public static String getDrawMark(int drawType) {
        switch (drawType){
            case 0:return "mark_hexagon";
            case 1:return "mark_act";//单位的可移动标志
            case 2:return "markB_nul";//城市的炸弹标志
            case 3:return "markR_energy";//城市的电力标志
            case 4:return "markB_air";//城市的飞机标志
            case 8:return "mark_airborne";//空降标志
            case 9:return "mark_target";//圆形进攻标志 中心为×
            case 10:return "mark_target2";//圆形标记标志 中心为圈
            case 11:return "mark_target3";//方块进攻标志
            case 12:return "mark_reload";//装弹标志
            case 13:return "mark_transport";//火车标志
        }
        return "mark_act";
    }

    public static int getDrawType(int type, int color) {
        switch (type){
            case 0://0 为默认移动坐标标记
                if(color==25){//进攻
                    return 9;
                }
                if(color==10){//交通
                    return 13;
                }
                return 0;
            case 1://圆形标志切换
                if(color==25){
                    return 9;
                }else{
                    return 10;
                }
            case 2://2特武标记,则圆形标志切换
                if(color==25){//进攻
                    return 11;
                }else if(color==7){//装弹
                    return 12;
                }else{
                    return 10;
                }
        }
        return 0;
    }


    public static int getGameEvaluate(int round,int roundMax,int diffcut){
        float f=    (round-diffcut)*1f/roundMax;
        if(f<0.6f){
            return 5;
        }if(f<0.7f){
            return 4;
        }if(f<0.8f){
            return 3;
        }if(f<0.9f){
            return 2;
        }if(f<1f){
            return 1;
        }else {
            return -1;
        }
    }
    //pH玩家据点 allH 总据点
    public static int getGameEvaluateByHinge(int allH, int pH) {
        if(allH==pH){
            return 5;
        }else if(pH==0){
            return -1;
        }
        float f=    allH*1f/pH;
        if(f==1f){
            return 5;
        }if(f>0.8f){
            return 4;
        }if(f>0.6f){
            return 3;
        }if(f>0.4f){
            return 2;
        }if(f>0.2f){
            return 1;
        }
        return -1;
    }

    public static String getGameResultEvaluate(int rs) {
        switch (rs){
            case 1:return "gameresult_d";
            case 2:return "gameresult_c";
            case 3:return "gameresult_b";
            case 4:return "gameresult_a";
            case 5:return "gameresult_s";
        }
        return "gameresult_d";
    }

    public static String getImageNameForCardType(Fb2Smap.BuildData b,int cardType) {
        switch (cardType){
            case 0:return "icon_deflv";
            case 1:return "icon_tech_infantry";
            case 2:return "icon_tech_artillery";
            case 3:return "icon_tech_armor";
            case 4:return "icon_tech_navy";
            case 5:return "icon_tech_air";
            case 6:return "icon_tech_defence";
            case 7:return "icon_tech_nuclear";
            case 8:return "icon_tech_submarine";
            case 9:return getImageNameByRegionType(b);
            case 10:return getImageNameByRegionType(b);
            case 11:return "mark_tech";
            case 12:return getImageNameByForeignType(b.getForgeinTypeForPlayer());
            case 13:return "icon_tech_citypolicy";
            case 14:return "icon_tech_shelter";
            case 15:return "mark_industry";
            case 16:return "mark_money";
        }
        return "icon_hp";
    }

    //外交类型 0无 1同盟 2互不侵犯  -1敌对
    private static String getImageNameByForeignType(int forgeinType) {
        switch (forgeinType){
            case 2:return "icon_ally";
            case 1:return "icon_peace";
            case -1:return "icon_enemy";
        }
        return "icon_noForeign";
    }

    public static String getImageNameForCardHp(int armyGroup) {
        switch (armyGroup){
            case 0:return "card_hp1";
            case 1:return "card_hp1";
            case 2:return "card_hp2";
            case 3:return "card_hp3";
            case 4:return "card_hp4";
            case 5:return "card_hp5";
        }
        return "card_hp5";
    }
    //根据兵种类型转化为将军类型(1步2炮3骑兵4海军5空军)
    public static int getGeneralTypeByArmyType(int age,int armyType) {
        if(armyType==1||(age<0&&armyType==3)){
            return 1;
        }else if(armyType==2){
            return 2;
        }else if(armyType==3){
            return 3;
        }else if(armyType==4||armyType==8){
            return 4;
        }else if(armyType==5){
            return 5;
        }else {
            return -1;
        }
    }
    //隐藏部分颜色
    public static Color transColorForDrawMark(int value) {
        if(ResDefaultConfig.ifDebug){
            return getColor(value);
        }
        if(value==18||value==8){
            return getColor(6);
        }else {
            return getColor(value);
        }
        // return getColor(value);
    }

    public static String getImageNameForBuildModel(Fb2Smap.BuildData b) {
        if(b.isCapital()){//首都
            return "build_city_6";
        }else if(b.getBuildType()==1){//港口
            return "city_port";
        }else if(b.getBuildType()==2||b.ifSea()){//海岛
            return "city_island";
        }else if(b.getCityLvNow()>8){//大城市
            return "build_city_5";
        }else if(b.getCityLvNow()>6){//大城市
            return "build_city_4";
        }else if(b.getCityLvNow()>4){//大城市
            return "build_city_2";
        }else if(b.getCityLvNow()>2){//大城市
            return "build_city_2";
        }else if(b.getCityLvNow()>0){//大城市
            return "build_city_1";
        }else{//普通城市
            return "build_city_0";
        }
    }




    public static String getImageNameForBuildPoint(Fb2Smap.BuildData buildData) {
        if(buildData.isPlayer()){
            return "mark_point_g";
        }else if(buildData.isPlayerAlly()){
            return "mark_point_b";
        }else if(buildData.getLegionData().getInternIndex()==0){
            return "mark_point_w";
        }else {
            return "mark_point_r";
        }
    }

    public static String getImageNameForBuildPoint(int h) {
        if(h==1){
            return "mark_point_g";
        }else if(h==-1){
            return "mark_point_r";
        }else {
            return "mark_point_b";
        }
    }


    //1.士气上升
    //2.建造
    //3.恐惧
    //4.金钱
    //5.间谍
    //6.电力
    //7.戒严
    //8.强行军
    //9.急救
    //10.开炮
    //11.自毁
    //12.训练
    //TODO
    public static String getImageNameByEffectId(int effectId) {
        switch (effectId){
            case 1:return "unit_face0";
            case 2:return "animation_build";
            case 3:return "unit_face5";
            case 1501:return "fighter";
            case 1502:return "fighter";
            case 1503:return "fighter";
            case 1504:return "bomber";
            case 1505:return "bomber";
            case 1506:return "bomber";
            case 1507:return "bomber";
        }
        return "animation_build";
    }

    public static  String getImageNameBySkill(int type,int skillId) {
        if(type==0){
            return "unitFeature_"+skillId;
        }else {
            return "unitSkill_"+skillId;
        }
    }

    //buildPolarity 城市特性
    //buildPolicy 城市方针 当2的时候根据城市特性调整建造
    public static int[] getBuildIdsForBuildPolicy(MainGame game,int buildPolarity,int buildPolicy) {
        switch (buildPolicy){
            /*  case 1:
                  return ResConfig.Game.buildPolicy1;*/
            case 2:
                if(buildPolarity==1){
                    return game.resGameConfig.buildPolicy2_1;
                }else if(buildPolarity==2){
                    return game.resGameConfig.buildPolicy2_2;
                }else if(buildPolarity==3){
                    return game.resGameConfig.buildPolicy2_3;
                }else if(buildPolarity==4){
                    return game.resGameConfig.buildPolicy2_4;
                }else if(buildPolarity==5){
                    return game.resGameConfig.buildPolicy2_5;
                }else{
                    return game.resGameConfig.buildPolicy2;
                }
            case 3:return game.resGameConfig.buildPolicy3;
            case 4:return game.resGameConfig.buildPolicy4;
            case 5:return game.resGameConfig.buildPolicy5;
            case 6:return game.resGameConfig.buildPolicy6;
        }
        return null;
    }

    public static Color getColorForBuildPolicy(int policy) {
        switch (policy){
            case 1: return Color.GREEN;
            case 2: return Color.ORANGE;
            case 3: return Color.RED;
            case 4: return Color.BLUE;
            case 5: return Color.PURPLE;
            case 6: return Color.CYAN;
        }


        return Color.CLEAR;
    }

    //camp -1无势力 0中立 1自己 2盟友  3敌人 4附属国 5宗主国 7友好非盟友 8好感度低但非敌对
    public static Color getColorForCamp(int camp) {
        switch (camp){
            case 0: return Color.GRAY;
            case 1: return Color.GREEN;
            case 2: return Color.BLUE;
            case 3: return Color.RED;
            case 4: return Color.CHARTREUSE;
            case 5: return Color.NAVY;
            case 6: return Color.LIGHT_GRAY;
            case 7: return Color.CYAN;
            case 8: return Color.ORANGE;
        }
        return Color.WHITE;
    }

    //camp -1无势力 0中立 1自己 2盟友  3敌人 4附属国 5宗主国
    public static Color getMapSelectColorByCamp(int camp) {
        switch (camp){
            case 0: return Color.WHITE;
            case 1: return Color.GREEN;
            case 2: return Color.BLUE;
            case 3: return Color.RED;
            case 4: return Color.CYAN;
            case 5: return Color.NAVY;
            case 6: return Color.LIGHT_GRAY;
            case 7: return Color.SKY;
        }
        return Color.WHITE;
    }



    public static int[] getAIBuildFortArray(MainGame game,int age) {
        switch (age){
            case 0:return game.resGameConfig.buildFortIdForAge0;
            case 1:return game.resGameConfig.buildFortIdForAge1;
            case 2:return game.resGameConfig.buildFortIdForAge2;
        }
        return game.resGameConfig.buildFortIdForAge0;
    }





   /* //TODO 根据国家科技获得可以显示的卡牌id 随后添加筛选条件 废弃
    public static List<XmlReader.Element> getCardByCountry(){
        List<Integer> rs=new ArrayList<Integer>();
        rs.add(1101);
        List<XmlReader.Element> rsE=new ArrayList<XmlReader.Element>();
        XmlReader.Element cardEs= ResConfig.gameConfig.getDEF_CARD().e;
        for(int i=0;i<cardEs.getChildCount();i++){
            if(rs.contains(cardEs.getChild(i).getInt("id"))){
                rsE.add(cardEs.getChild(i));
            }
        }
        return rsE;
    }*/

    /*//根据状态id状态图片名
    public static StringName getState(int targetState){
        StringName  rs=null;
        switch(targetState){
            case 1:
                rs=ResConfig.Image.TARGET_TAG_1;
                break;
            case 2:
                rs=ResConfig.Image.TARGET_TAG_2;
                break;
            case 3:
                rs=ResConfig.Image.TARGET_TAG_3;
                break;
            case 4:
                rs=ResConfig.Image.TARGET_TAG_4;
                break;
            case 5:
                rs=ResConfig.Image.TARGET_TAG_5;
                break;
        }
        return rs;
    }*/


   /* //城市血量=(城市等级+1)*baseHp
    public static int getCityHpMaxByCityLv(int cityLv){
                return game.resGameConfig.cityBaseHp*(cityLv+1);
    }*/





    //获取color
    public static Color getColor(String colorName){
        switch (colorName){
            case "WHITE": return Color.WHITE;
            case "LIGHT_GRAY": return Color.LIGHT_GRAY;
            case "GRAY": return Color.GRAY;
            case "DARK_GRAY": return Color.DARK_GRAY;
            case "BLACK": return Color.BLACK;
            case "CLEAR": return Color.CLEAR;
            case "BLUE": return Color.BLUE;
            case "NAVY": return Color.NAVY;
            case "ROYAL": return Color.ROYAL;
            case "SLATE": return Color.SLATE;
            case "SKY": return Color.SKY;
            case "CYAN": return Color.CYAN;
            case "TEAL": return Color.TEAL;
            case "GREEN": return Color.GREEN;
            case "CHARTREUSE": return Color.CHARTREUSE;
            case "LIME": return Color.LIME;
            case "FOREST": return Color.FOREST;
            case "OLIVE": return Color.OLIVE;
            case "YELLOW": return Color.YELLOW;
            case "GOLD": return Color.GOLD;
            case "GOLDENROD": return Color.GOLDENROD;
            case "ORANGE": return Color.ORANGE;
            case "BROWN": return Color.BROWN;
            case "TAN": return Color.TAN;
            case "FIREBRICK": return Color.FIREBRICK;
            case "RED": return Color.RED;
            case "SCARLET": return Color.SCARLET;
            case "CORAL": return Color.CORAL;
            case "SALMON": return Color.SALMON;
            case "PINK": return Color.PINK;
            case "MAGENTA": return Color.MAGENTA;
            case "PURPLE": return Color.PURPLE;
            case "VIOLET": return Color.VIOLET;
            case "MAROON": return Color.MAROON;
        }
        return Color.WHITE;
    }

    public static Color getColor(int i){

        switch (i){
            case 0: return Color.WHITE;
            case 1: return Color.LIGHT_GRAY;
            case 2: return Color.GRAY;
            case 3: return Color.DARK_GRAY;
            case 4: return Color.BLACK;
            case 5: return Color.CLEAR;
            case 6: return Color.BLUE;
            case 7: return Color.NAVY;
            case 8: return Color.ROYAL;
            case 9: return Color.SLATE;
            case 10: return Color.SKY;
            case 11: return Color.CYAN;
            case 12: return Color.TEAL;
            case 13: return Color.GREEN;
            case 14: return Color.CHARTREUSE;
            case 15: return Color.LIME;
            case 16: return Color.FOREST;
            case 17: return Color.OLIVE;
            case 18: return Color.YELLOW;
            case 19: return Color.GOLD;
            case 20: return Color.GOLDENROD;
            case 21: return Color.ORANGE;
            case 22: return Color.BROWN;
            case 23: return Color.TAN;
            case 24: return Color.FIREBRICK;
            case 25: return Color.RED;
            case 26: return Color.SCARLET;
            case 27: return Color.CORAL;
            case 28: return Color.SALMON;
            case 29: return Color.PINK;
            case 30: return Color.MAGENTA;
            case 31: return Color.PURPLE;
            case 32: return Color.VIOLET;
            case 33: return Color.MAROON;
        }
        return Color.WHITE;
    }
    //0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡
    public static float getCardLvRate(MainGame game,int cardType){
        switch(cardType){
            case 1: return game.resGameConfig.cardType1PriceRateForLv;
            case 2: return game.resGameConfig.cardType2PriceRateForLv;
            case 3: return game.resGameConfig.cardType3PriceRateForLv;
            case 4: return game.resGameConfig.cardType4PriceRateForLv;
            case 5: return game.resGameConfig.cardType5PriceRateForLv;
            case 6: return game.resGameConfig.cardType6PriceRateForLv;
            case 7: return game.resGameConfig.cardType7PriceRateForLv;
            case 8: return game.resGameConfig.cardType8PriceRateForLv;
            case 9: return game.resGameConfig.cardType9PriceRateForLv;
            case 10: return game.resGameConfig.cardType10PriceRateForLv;
            case 11: return game.resGameConfig.cardType11PriceRateForLv;
        }
        return 1;
    }

    //buyType  //-1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标 18定制战舰 19奇物 -7全部可招募单位
    public static int getCardPrice(Fb2Smap.LegionData legion, Fb2Smap.BuildData build,int priceType,int cardPrice, int buyType, int cardId, int cardLv){
        int min=0;if(cardPrice>0){min=1; }
        if(legion.isEditMode(true)){
            return 0;
        }
        boolean ifLegionFeature=legion.ifEffective(10);
        int discount=0;
        if(buyType==17){
            XmlReader.Element xE=legion.getGame().gameConfig.getDEF_WONDER().getElementById(cardId);
            float rate=xE.getInt("star",1)*1f/20;
            if(xE!=null){
                switch (priceType){
                    case 0:
                        rate=legion.getGame().resGameConfig.moneyIncomeRate*rate;break;
                    case 1:
                        rate=legion.getGame().resGameConfig.industryIncomeRate*rate;break;
                    case 2:
                        rate=legion.getGame().resGameConfig.techIncomeRate*rate;break;
                    case 3:
                        rate=legion.getGame().resGameConfig.foodIncomeRate*rate;break;
                }
            }
            cardPrice= (int) (Math.min(legion.getIncome(priceType),9999)*rate);
        }


        int cardType=0;
        XmlReader.Element cardE=legion.getGame().gameConfig.getDEF_CARD().getElementById(cardId);
        if(cardE!=null){
            cardType=cardE.getInt("type",0);
        }
        if(legion.isPlayer()&&(cardType==9||cardType==10)){
            int v=legion.getChiefSpecialEffect(13);//民用设施价格降低
            if(v>=0&&cardType==9){
                discount-=v;
            }else {
                v=legion.getChiefSpecialEffect(14);//招募军队价格降低
                if(v>=0&&cardType==10){
                    discount-=v;
                }
            }
        }


        if(build!=null&&build.getBuildWonder()!=0){
            XmlReader.Element xE=legion.getGame().gameConfig.getDEF_WONDER().getElementById(build.getBuildWonder());
            if(xE!=null){
                int function=xE.getInt("fucntion",0);
                int effect=xE.getInt("effect",0);
                int value=xE.getInt("value",0);
                if(function==3&&effect==cardType){
                    discount-=value;
                }
            }
        }
        //招募的价格根据战争支持度变化
        if(legion.isPlayer()){
            if(cardType>=1&&cardType<=8){
                int warSupport=legion.getWarSupport();
                discount+= (50-warSupport);
            }
        }

        if(build!=null&&build.getLegionIndex()==legion.getLegionIndex()&&legion.ifHaveSpirit(16)){
            if(legion.isCoreAreaRegion(build.getRegionId())&&(cardType>=1&&cardType<=8)){
                cardPrice=cardPrice/2;
            }else {
                discount+=legion.getSpiritValue(16);
                cardPrice=cardPrice*ComUtil.max(10,legion.getSpiritValue(16))/100;
            }
        }
        //1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
        if(ifLegionFeature){
            switch (cardType){
                case 1:cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(32)+discount)/100; break;
                case 2:cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(33)+discount)/100; break;
                case 3:cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(34)+discount)/100; break;
                case 4:cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(35)+discount)/100; break;
                case 5:cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(36)+discount)/100; break;
                case 7:cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(39)+discount)/100; break;
                case 8:cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(37)+discount)/100; break;
                case 6:
                    if(cardId==1606){
                        cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(38)+discount)/100;
                    }else {
                        cardPrice=cardPrice*(100-legion.getLegionFeatureEffect(40)+discount)/100;;
                    }
                    break;
                default:
                    cardPrice=cardPrice*(100+discount)/100; break;
            }
        }
        int addV=0;
        //如果是招募部队并且该部队的人口超过控制数,则进行招募溢价
        if(legion.getPlayerMode()==0&&legion.isPlayer()&&priceType==0&&cardType>=1&&cardType<=8&&legion.getPopulationNow()>(legion.getGame().resGameConfig.populationLimitTrigger+legion.getWarSupport())  &&legion.getPopulationNow()*2>legion.getPopulationMax()){//如果某种类型的部队招募超过一半,则进行招募溢价
            addV= legion.getUnitNumByType(cardType)/2;
        }
       /* //如果招募的是可组合部队
        if(legion.isUnitGroup(cardType)){
            cardPrice= (int) (cardPrice*legion.getGame().resGameConfig.unityGroupNorAllFormationStrikeRate);
        }*/
        if( build!=null&&cardType<=10){
            float rate= build.getRegionRate();
            if(cardPrice>0){
                cardPrice= (int) (cardPrice*rate);
                if(cardPrice==0){cardPrice=1;}
            }
        }
        return  ComUtil.max((int)(cardPrice*DefDAO.getCardLvRate(legion.getGame(),cardType)*(1+cardLv))+addV,min);
    }
    //获得兵模image通过cardId TODO
    public static String getArmyModeNameByArmyId(int armyId){
        return "army_"+armyId;
    }
    public static String getSCNameByArmyId(int armyId){
        return "sc_"+armyId;
    }


    public static int getAttackChance(int chance,int age,int extraValue){
        return chance+20*(age+1)+extraValue;
    }


    //根据当前kill获得下一级的升级总数
    /*public static int getKillSumForUpd(int kill){
        int killSum=1;
        int i=0;
        do{
            killSum=killSum+i;
            i++;
        }while (killSum<kill);
        return killSum;
    }*/


    public static String getImageNameForArmyRank(int lv){
        if(lv>=7){
            return "level_7";
        }
        return "level_"+lv;
    }
    public static String getImageNameForCountryFlag(int country){
        return "flag_"+country;
    }
    //TODO
    public static String getImageNameForPromptTitle(int promptTitleType){
        switch (promptTitleType){
            case 1:return "iconTitle_trade";
            case 2:return "mark_tech";
            case 3:return "icon_deflv";
        }
        return "iconTitle_trade";
    }





    public static String getColorBlockByArmyType(int armyType){
        switch (armyType){
            case 1:return "colorBlock_11";
            case 2:return "colorBlock_12";
            case 3:return "colorBlock_13";
            case 4:return "colorBlock_14";
            case 5:return "colorBlock_15";
            case 6:return "colorBlock_16";
            case 7:return "colorBlock_17";
            case 8:return "colorBlock_18";
        }


        return "colorBlock_5";
    }

    public static String getImageNameForArmyGroup(int group){
        if(group>4){
            group=4;
        }else if(group<1) {
            group=1;
        }
        return "army_group"+group;
    }
    //TODO
    public static String getImageNameForSmallGeneral(int general){
        return "general_0";
    }


    public static String getImageNameForArmyMorale(MainGame game,int morale){
        if(morale>game.resGameConfig.unitMoraleMaxLimit){
            return "morale_bar_r";
        }else if(morale<game.resGameConfig.unitMoraleMinLimit){
            return "morale_bar_b";
        }else {
            return "morale_bar_g";
        }
    }
    //1~5 铜色 6~10 银色 11~15 金色
    public static String getImageNameForStarBar(int value){
        if(value==0){
            return "icon_lvpentagram_0";
        }else if(value<6){
            return "icon_lvpentagram_1";
        }else if(value<11){
            return "icon_lvpentagram_2";
        }else {
            return "icon_lvpentagram_3";
        }
    }

    //得到血条名称
    public static String getImageNameForArmyHp(Fb2Smap btl  , Fb2Smap.ArmyData army){
        String rs=null;
        if(army.playerCanCommand()){
            rs=  "army_hp_g";
        }else {
            rs= getImageNameForHp(btl,army.getLegionIndex());
        }
        return rs;
    }



    public static String getImageNameForAirHp(Fb2Smap btl  , Fb2Smap.AirData air){
        if(air.playerCanCommand()){
            return "army_hp_g";
        }else {
            return getImageNameForHp(btl,air.getLegionIndex());
        }
    }





    public static String getImageNameForHp(Fb2Smap btl  , int legionIndex ){
        if(legionIndex==btl.masterData.getPlayerLegionIndex()){
            return "army_hp_g";
        }
        int fd=btl.getForeignDegree(legionIndex,btl.masterData.getPlayerLegionIndex());
        if(fd==1){
            return "army_hp_b";
        }else if(fd==0){
            return "army_hp_n";
        }else if(fd==-1){
            return "army_hp_r";
        }else {
            return "army_hp_n";
        }
    }


    public static String getImageNameForSCBorder(Fb2Smap btl  , int legionIndex){
        int favor=0;
        if(btl.masterData.getPlayerMode()==0){
            Fb2Smap.ForeignData f=btl.getForeignData(legionIndex);
            if(f!=null){
                favor=f.getFavorValue();
            }
        }
        return getImageNameForSCBorder(btl,legionIndex,favor);
    }


    public static String getImageNameForSCBorder(Fb2Smap btl  , int legionIndex ,int favor){
        if(legionIndex==btl.masterData.getPlayerLegionIndex()){
            return "sc_border_green";
        }
        Fb2Smap.LegionData l=btl.legionDatas.get(legionIndex);
        int ArmyIntern=l.getInternIndex();
        int playerIntern=btl.getPlayerLegionData().getInternIndex();
        if(l.getSuzerainLi()==btl.getPlayerLegionData().getLegionIndex()){
            return "sc_border_cyan";
        }else if(ArmyIntern==playerIntern){
            return "sc_border_blue";
        }else if(ArmyIntern==0||(favor>30)){
            return "sc_border_black";
        }else {
            return "sc_border_red";
        }
    }


    public static String getImageNameForCard(MainGame game,int cardId,int cardType){
        //地标
        if(cardType==17){
            String cardImageName="wonder_"+cardId;
            if(game.getImgLists().contains(cardImageName)){
                return cardImageName;
            }else {
                return "card_miss";
            }
        }else if(cardType==19) {
            String cardImageName="card_spirit_"+cardId;
            if(game.getImgLists().contains(cardImageName)){
                return cardImageName;
            }else {
                return "card_miss";
            }
        }else  if(game.gameConfig.getDEF_CARD().getElementById(cardId)!=null){
            int age=game.getSMapDAO().worldData.getWorldAge();
            String cardImageName="card_"+cardId+"_"+age;
            if(cardType==4&&game.getSMapDAO().getNowYear()>=game.resGameConfig.navyModelChangeYear){
                return "card_"+cardId;
            }else if(game.getImgLists().contains(cardImageName)){
                return cardImageName;
            }else if(game.getImgLists().contains("card_"+cardId)){
                return "card_"+cardId;
            }else {
                return "card_miss";
            }
        }else {
            return "card_miss";
        }
    }







    public static String getImageNameByRegionType(Fb2Smap.BuildData build){
        if(build.getBuildType()==2){
            return "regionType_sea";
        }else if(build.isCapital()&&build.getBuildWonder()<100){
            return "regionType_capital";
        }else if(build.getCityLvNow()==0){
            return "regionType_village";
        }else if(build.getBuildWonder()==0){
            return "regionType_city";
        } else {
            MainGame game=build.getMainGame();
            if(game.gameConfig.ifEffect){
                String name= "wonder_"+(build.getBuildWonder());
                if(game.getImgLists().contains(name)){
                    return name;
                }
            }else{
                return "regionType_city";
            }
            return "regionType_village";
        }
    }

    public static String getImageNameByResourceType(int type){
        switch (type){
            case 0:return "mark_money";
            case 1:return "mark_industry";
            case 2:return "mark_tech";
            case 3:return "mark_material";
            case 4:return "mark_mineral";
            case 5:return "mark_oil";
            default:return "mark_money";
        }
    }
    //9999:     9999
    public static String splitStrForTradeValue(int v,int b){
        // String v1= ComUtil.formmatNumber(v,4,true," ")+":     "+ ComUtil.formmatNumber(b,4,true," ");
        //  Gdx.app.log("v1",v1);
        return ComUtil.formmatNumber(v,4,true,"  ")+":      "+ ComUtil.formmatNumber(b,4,true,"  ");
    }

    //拼接成类似内容Lv1              5/10      cost
    public static String getPromptStrForTechInfo(int lv, int num, int max, int cost){
        StringBuilder n=new StringBuilder();
        int spaceLength=9;
        String space;
        if(lv>9){
            spaceLength-=2;
        }
        if(num>9){
            spaceLength-=2;
        }

        if(spaceLength==9){
            space= ResDefaultConfig.StringName.space8;
        }else if(spaceLength==7){
            space= ResDefaultConfig.StringName.space6;
        }else {
            space= ResDefaultConfig.StringName.space4;
        }
        if(max>99){
            n.append("Lv:").append(lv).append(space).append(num).append("/").append(max).append(ResDefaultConfig.StringName.space3).append("$:").append(ComUtil.formmatNumber(cost,4,true," "));

        }else if(max>9){
            n.append("Lv:").append(lv).append(space).append(num).append("/").append(max).append(ResDefaultConfig.StringName.space5).append("$:").append(ComUtil.formmatNumber(cost,4,true," "));

        }else {
            n.append("Lv:").append(lv).append(space).append(num).append("/").append(max).append(ResDefaultConfig.StringName.space7).append("$:").append(ComUtil.formmatNumber(cost,4,true," "));

        }
        return n.toString();
    }


    public static Color getColorByModelMorale(MainGame game,int armyMorale) {
        if(armyMorale<game.resGameConfig.unitMoraleMinLimit){
            return Color.BLUE;
        }else if(armyMorale<game.resGameConfig.unitMoraleMaxLimit){
            return Color.WHITE;
        }else {
            return Color.RED;
        }
    }
    public static Color getColorByMoraleBar(MainGame game,int armyMorale) {
        if(armyMorale<game.resGameConfig.unitMoraleMinLimit){
            return Color.BLUE;
        }else if(armyMorale<game.resGameConfig.unitMoraleMaxLimit){
            return Color.CHARTREUSE;
        }else {
            return Color.RED;
        }
    }


    //0 50 1 100 2 150 3 200 4 250 5 300
    public static int getRateByDifficut(int difficult) {
        if(difficult<=0){return 50;}
        return (difficult+1)*50;
    }

    public static int getRateByDifficut(int rate,int difficult) {
        if(difficult<=0){return rate;}
        return difficult*rate;
    }
    //获得奖励
    public static int getBontyByDifficut(int bonty,int difficult) {
        switch (difficult){
            case 1:return bonty*2;
            case 2:return bonty;
            case 3:return bonty/2;
        }
        return bonty;
    }
    public static Color getColorByBackTile(int backTile) {
        //-1不可通过的地方,0平地,1海洋,2海岸,3桥梁,4丘陵,5山地,6森林,7人为装饰,8自然装饰,9海洋装饰,10道路,11河流
        //河流,道路应该放在fore层
        switch (backTile){
            case -1:return Color.BLACK;
            case 0: return Color.LIGHT_GRAY;
            case 1: return Color.SKY;
            case 2: return Color.NAVY;
            case 3: return Color.BLACK;
            case 4: return Color.DARK_GRAY;
            case 5: return Color.GRAY;
            case 6: return Color.GREEN;
            case 7: return Color.YELLOW;
            case 8: return Color.BROWN;
            case 9: return Color.BLUE;
            case 10: return Color.RED;
        }
        return Color.WHITE;
    }




    //MAROON 橘色 PURPLE 紫色  PINK 粉色

    public static Color getColorForCliamZone(int zone) {
        switch (zone){
            case -1:return Color.BLACK;//
            case 0: return Color.NAVY;//默认,一般为海洋
            case 1: return Color.LIME;//热带雨林气候
            case 2: return Color.CYAN;//热带季风气候
            case 3: return Color.YELLOW;//亚热带季风气候
            case 4: return Color.MAGENTA;//温带海洋性气候
            case 5: return Color.DARK_GRAY;//温带大陆性气候
            case 6: return Color.CORAL;//高山气候
            case 7: return Color.GREEN;//热带草原气候
            case 8: return Color.GOLD;//热带沙漠气候
            case 9: return Color.BLUE;//地中海气候
            case 10: return Color.ORANGE;//温带季风气候
            case 11: return Color.PURPLE;//极地气候


           /* case 1: return Color.BLACK;//平原    DARK_GRAY
            case 2: return Color.MAROON;//沙漠  TAN
            case 3: return Color.;//冷带     ROYAL
            case 4: return Color.;//山地  BROWN
            case 5: return Color.LIGHT_GRAY;//丘陵  OLIVE
            case 6: return Color.GREEN;//森林  CHARTREUSE
            case 7: return Color.;//树林  LIME
            case 8: return Color.ROYAL;//热带雨林  GREEN
            case 9: return Color.SLATE;//沼泽/湿地  SKY
            case 10: return Color.SKY;
            case 11: return Color.TEAL;
            case 12: return Color.CHARTREUSE;
            case 13: return Color.;
            case 14: return Color.FOREST;
            case 15: return Color.OLIVE;
            case 16: return Color.BROWN;
            case 17: return Color.TAN;
            case 18: return Color.FIREBRICK;
            case 19: return Color.SCARLET;
            case 20: return Color.;
            case 21: return Color.SALMON;
            case 22: return Color.;
            case 23: return Color.;
            case 24: return Color.VIOLET;*/





        }
        return Color.WHITE;
    }


    public static String getImageNameFlagBorderByDifficulty(int gameDifficulty) {
        switch (gameDifficulty) {
            case 1:
                return "flagBorder_1";
            case 2:
                return "flagBorder_2";
            case 3:
                return "flagBorder_3";
            default:
                return "flagBorder_1";
        }
    }
    public static String getImageNameFlagBorderByScore(int gameDifficulty) {
        switch (gameDifficulty) {
            case 1:
            case 2:
                return "flagBorder_1";
            case 3:
            case 4:
                return "flagBorder_2";
            case 5:
                return "flagBorder_3";
            default:
                return "flagBorder_1";
        }
    }

    public static String getImageNameFlagBorderB(int gameDifficulty) {
        switch (gameDifficulty) {
            case 1:
                return "flagBorderB_1";
            case 2:
                return "flagBorderB_2";
            case 3:
                return "flagBorderB_3";
            default:
                return "flagBorderB_0";
        }
    }

    public static String getImageNameForSmallGeneralBg(int c) {

        switch (c){
            case 0:return "sg_bg0";
            case 1:return "sg_bg1";
            case 2:return "sg_bg2";
            case 3:return "sg_bg3";
            case 4:return "sg_bg4";
            case 5:return "sg_bg5";
            case 6:return "sg_bg6";
            default: return "sg_bg6";
        }



    }

    public static String getLineBorder(boolean ifRegionBorder1, boolean ifRegionBorder2, boolean ifRegionBorder3) {
        if(ifRegionBorder1&&!ifRegionBorder2&&!ifRegionBorder3){
            return "regionBorder_1";
        }else if(ifRegionBorder1&&ifRegionBorder2&&!ifRegionBorder3){
            return "regionBorder_2";
        }else if(ifRegionBorder1&&ifRegionBorder2&&ifRegionBorder3){
            return "regionBorder_3";
        }else if(ifRegionBorder1&&!ifRegionBorder2&&ifRegionBorder3){
            return "regionBorder_4";
        }else if(!ifRegionBorder1&&ifRegionBorder2&&!ifRegionBorder3){
            return "regionBorder_5";
        }else if(!ifRegionBorder1&&ifRegionBorder2&&ifRegionBorder3){
            return "regionBorder_6";
        }else if(!ifRegionBorder1&&!ifRegionBorder2&&ifRegionBorder3){
            return "regionBorder_7";
        }else {
            return null;
        }
    }



    public static Color getColorByBuildAirforcePolicy(int airforcePolicy) {
        switch (airforcePolicy){
            case 0: return Color.DARK_GRAY;//空军策略:主动出击
            case 1: return Color.RED;//空军策略:全力出击
            case 2: return Color.YELLOW;//空军策略:有限出击
            case 3: return Color.GREEN;//空军策略:全体待机
        }

        return Color.DARK_GRAY;//默认,一般为海洋

    }

    public static float getRoundRate(int gameDifficulty) {
        switch (gameDifficulty){
            case 1:return 0.75f;
            case 2:return 0.8f;
            case 3:return 0.85f;
        }
        return 1f;
    }

    public static String getLevelMark(int armyRank) {
        if(armyRank>5){
            return "level_6";
        }
        return "level_"+armyRank;
    }

    public static String getImageNameArmyFeatureByArmy(Fb2Smap.ArmyData army) {
        if(army.getUnitArmyId0()==1606){
            return "feature_missile";
        }else if(army.getUnitArmyId0()==1301){
            return "feature_panzer";
        }
        switch (army.getArmyType()){//兵种类型  1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
            case 1:return "feature_infantry";
            case 2:return "feature_artillery";
            case 3:if(army.getAge()<2){
                return "feature_tank";
            }else {
                return "feature_tank_2";
            }
            case 4:return "feature_ship";
            case 5:return "feature_air";
            case 6:return "feature_fort";
            case 7:return "feature_nuclear";
            case 8:return "feature_submarine";
        }
        return "feature_infantry";
    }

    public static String getConceptStr(int playerAmbition) {
        if(playerAmbition==100){
            return "concept_name_n";
        }else if(playerAmbition>100){
            return "concept_name_r";
        }else {
            return "concept_name_b";
        }
    }

    public static String getDifficultStr(int gameDifficulty) {
        if(gameDifficulty<=1){
            return "difficult_name_1";
        }else if(gameDifficulty>=3){
            return "difficult_name_3";
        }else {
            return "difficult_name_2";
        }
    }

    public static String getAreaMark(Fb2Smap.BuildData build) {
        Fb2Smap.LegionData pl=build.getPlayerLegion();
        if(pl.isMajorAreaRegion(build.getRegionId())){
            return "areaMark_2";
        }else if(pl.isCoreAreaRegion(build.getRegionId())){
            return "areaMark_3";
        }else if(build.isPlayer()){
            if(build.ifSea()||build.getCityLvNow()==0){
                return "areaMark_0";
            }else{
                return "areaMark_1";
            }
        }
        return "areaMark_0";
    }

    //1步兵2炮兵3坦克4海军5空军  0不限制 -1陆军 步炮坦
    public static boolean ifArmyTypeEqualGeneralType(int armyType, int gType) {
        if(armyType==0||gType==0||(armyType==-1&&(gType==1||gType==2||gType==3))|| (armyType==gType&&(gType==1||gType==2||gType==3||gType==5))||(gType==4&&(armyType==4||armyType==8))){
            return true;
        }
        return false;
    }

    //获得阵线 阵线的方向跟fbord的方向不同 1 ↖ 2 ↙  3 ↓  4 ↘ 5 ↗ 6↑
    public static int getArmyFormationId(Fb2Smap.ArmyData armyData) {
        return  GameUtil.getFBorderId(armyData.ifUnitGroupIsFormation(1,false),armyData.ifUnitGroupIsFormation(4,false),armyData.ifUnitGroupIsFormation(5,false),armyData.ifUnitGroupIsFormation(6,false),armyData.ifUnitGroupIsFormation(3,false),armyData.ifUnitGroupIsFormation(2,false));
    }

    public static int[] getArmyDirectByDirect(int formationDirect) {
        switch (formationDirect){
            case 1:return ResDefaultConfig.Map.ARMY_POTION_FORMATION_DIRECT_1;
            case 2:return ResDefaultConfig.Map.ARMY_POTION_FORMATION_DIRECT_2;
            case 3:return ResDefaultConfig.Map.ARMY_POTION_FORMATION_DIRECT_3;
            case 4:return ResDefaultConfig.Map.ARMY_POTION_FORMATION_DIRECT_4;
            case 5:return ResDefaultConfig.Map.ARMY_POTION_FORMATION_DIRECT_5;
            case 6:return ResDefaultConfig.Map.ARMY_POTION_FORMATION_DIRECT_6;
        }
        return ResDefaultConfig.Map.ARMY_POTION_FORMATION_DIRECT_0;
    }
    //获得相反的方向
    public static int getContraryDirect(int direct) {
        switch (direct){
            case 1:return 6;
            case 2:return 5;
            case 3:return 4;
            case 4:return 3;
            case 5:return 2;
            case 6:return 1;
        }
        return 0;
    }

    public static float getMoveZoom(int moveSpeed) {
        switch (moveSpeed){
            case 0:return 0.2f;
            case 1:return 0.4f;
            case 2:return 0.6f;
            case 3:return 0.8f;
            case 4:return 1.0f;
            case 5:return 1.2f;
            case 6:return 1.4f;
            case 7:return 1.6f;
        }
        return 1f;
    }
    //-2部队,-3空军,-4核弹,-5即将结束 -6军团
    public static String getProgressMark(int countryId) {
       if(countryId==-2){
           return  "roundMark_unit";
        }else if(countryId==-3){
            return  "roundMark_air";
        }else if(countryId==-4){
           return  "roundMark_nul";
       }else if(countryId==-5){
           return  "roundMark_end";
       }else if(countryId==-6){
           return  "roundMark_legion";
       }else{
          return  "flag_"+countryId;
       }
    }




    /*
    //获得border的邻边方向
    public static int getDirectByBorder(int countryBorder) {
        switch (countryBorder){
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
            case 0:
                return 0;
        }

        return 0;
    }*/
}
