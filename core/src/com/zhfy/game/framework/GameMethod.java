package com.zhfy.game.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.XmlIntDAO;
import com.zhfy.game.model.content.XmlStringDAO;
import com.zhfy.game.model.content.conversion.Fb2Smap;

import java.util.Iterator;

//此用来存放一些单独用到的方法
public  class GameMethod {
    private MainGame game;
    private ObjectMap<String,String> shieldStrMap;
    //计算电量消耗用到的参数
       /* private int card2001EnergyCost;
        private int card2002EnergyCost;
        private int card2003EnergyCost;
        private int card2004EnergyCost;
        private int card2005EnergyCost;
        private int card2006EnergyAfford;
        private int card2007EnergyCost;
        private int card2008EnergyCost;
        private int card2009EnergyCost;
        private int card2010EnergyCost;
        private int card2011EnergyCost;
        private int card2012EnergyCost;
        private int card2013EnergyCost;
        private int card2014EnergyCost;*/


    public GameMethod(MainGame game,ObjectMap<String,String> shieldStrMap){
        this.game=game;
        this.shieldStrMap=shieldStrMap;
        /*card2001EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2001).getInt("energy",1);
        card2002EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2002).getInt("energy",1);
        card2003EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2003).getInt("energy",1);
        card2004EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2004).getInt("energy",1);
        card2005EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2005).getInt("energy",1);
        card2006EnergyAfford=game.gameConfig.getDEF_CARD().getElementById(2006).getInt("energy",1);
        card2007EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2007).getInt("energy",1);
        card2008EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2008).getInt("energy",1);
        card2009EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2009).getInt("energy",1);
        card2010EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2010).getInt("energy",1);
        card2011EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2011).getInt("energy",1);
        card2012EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2012).getInt("energy",1);
        card2013EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2013).getInt("energy",1);
        card2014EnergyCost=game.gameConfig.getDEF_CARD().getElementById(2014).getInt("energy",1);*/

    }

    //incomeRes 收入  根据收入获得可以保存的数量
    public static int getStorageRes(int incomeRes, int financialLvMax) {
        return incomeRes*(10+financialLvMax)/20;
    }

    public static float getLossRate(int financialLvMax) {
        return 1-0.05f*financialLvMax;
    }


    public static boolean ifUnitFeatureCanUpd(int feature) {
        if(feature==7||feature==8||feature==9||feature==10
                ||feature==11||feature==12||feature==13||feature==14||feature==16||feature==18||feature==19||feature==20||feature==21||feature==22||feature==23||feature==24||feature==25||feature==32){
            return true;
        }
        return false;
    }

    //gType  1步兵2炮兵3坦克4海军5空军  0不限制 -1陆军 步炮坦
    //ifFriendly 是否可以招募友好国家的将领
    //recruitType -1不可招募 0,可招募,1可招募但不现实大头像 2只自己国家
    public static Array<XmlReader.Element> getCanRecruitGeneralE(Fb2Smap sMapDAO, int countryId, int armyType, Array<XmlReader.Element> haveCardEList,boolean ifFriendly) {
        if(haveCardEList==null){
            haveCardEList=new Array<>();
        }else {
            haveCardEList.clear();
        }
        if (sMapDAO.generalEs != null && sMapDAO.generalEs.size > 0) {
            for (XmlReader.Element g : sMapDAO.generalEs) {
                if((ComUtil.ifHaveValueInStr(g.get("country"),countryId)|| (ifFriendly&&sMapDAO.ifFriendlyCountry(g.get("country"),countryId))&&g.getInt("ability",0)<sMapDAO.getGame().resGameConfig.recruitGeneralFriendlyAbilityLimit)&&sMapDAO.compareGameTime(false,g.getInt("service", 0), 0)&&sMapDAO.compareGameTime(true,g.getInt("death", 0), 0)&& !sMapDAO.generalIds.contains(g.getInt("id",0))&& DefDAO.ifArmyTypeEqualGeneralType(armyType,g.getInt("type",0))    ){
                    switch (g.getInt("recruitType",0)){
                        case -1: continue;
                        case 2:  if(!ComUtil.ifHaveValueInStr(g.get("country"),countryId)){
                            continue;
                        }
                            break;
                    }
                    haveCardEList.add(g);
                }
            }
        }
        return haveCardEList;
    }

    public  boolean ifHaveFeature(Fb2Smap.LegionData l,int cardId, int featureId) {
        XmlReader.Element armyE = game.gameConfig.getDEF_ARMY().getElementById(cardId);
        if (armyE!=null&& ComUtil.ifHaveValueInStr(armyE.get("feature"),featureId)) {
            XmlReader.Element xE = game.gameConfig.getDEF_UNITFEATURE().getElementById(featureId);
            if(xE != null &&l.getAge() >= xE.getInt("reqAge", 0)){
                String useMode = xE.get("useMode", "-1");
                if (!useMode.equals("-1") && !ComUtil.ifHaveValueInStr(useMode, l.getPlayerMode())) {
                    return false;
                }
                int requareCardId = xE.getInt("reqCardId", 0);
                if (requareCardId != 0) {
                    int lv = xE.getInt("reqCardLv", 0);
                    if (GameMethod.getCardLv(l, null, requareCardId) >= lv) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }else{
                return true;
            }
        } else {
            return false;
        }
    }


    public  int getUnitFeatureEffect(int feature,int featureLv) {
        XmlReader.Element xmlE=game.gameConfig.getDEF_UNITFEATURE().getElementById(feature);
        if(xmlE!=null){
            return (int) (xmlE.getFloat("baseEffect",0)+xmlE.getFloat("updEffect",0)*featureLv);
        }
        return 100;
    }


    //获取可以建造的地标
    //tyep  -1不限制 0普通地标 2建筑可以建造指定类型卡牌时 1唯一地标 3特大油田 4特大矿场
    public static Array<XmlReader.Element> getWonderE(Fb2Smap getsMapDAO, Fb2Smap.LegionData legionData, Fb2Smap.BuildData b, int type,Array<XmlReader.Element> rs) {
        if(rs==null){
            rs=new Array<>();
        }else{
            rs.clear();
        }
        if(b.getBuildName()!=0){//
            XmlReader.Element aE= getsMapDAO.getGame().gameConfig.getDEF_AREA().getElementById(b.getBuildName());
            if(aE!=null){
                String wonderStr=aE.get("wonder","");
                String[] wonderStrs=wonderStr.split(",");
                if(wonderStrs.length>0){
                    for(int i=0,iMax=wonderStrs.length;i<iMax;i++){
                        try {
                            if(!ComUtil.isNumeric(wonderStrs[i])){
                                continue;
                            }
                            int wonderId=Integer.parseInt(wonderStrs[i]);
                            if(wonderId==b.getBuildWonder()){
                                continue;
                            }

                            XmlReader.Element x=getsMapDAO.getGame().gameConfig.getDEF_WONDER().getElementById(wonderId);
                            if(x!=null&&!rs.contains(x,false)&&getsMapDAO.getAge()>=  x.getInt("age",0)){
                                String reqCountry=x.get("country","-1");
                                if(!reqCountry.equals("-1")&&!ComUtil.ifHaveValueInStr(reqCountry,legionData.getCountryId())){
                                    continue;
                                }
                                int state=x.getInt("state",0);
                                if(type!=-1){
                                    if(state!=type){
                                        continue;
                                    }
                                }
                                switch (state){
                                    case 0://普通地标
                                        rs.add(x);
                                        break;
                                    case 2://指定建筑可以建造某种类型的单位 值为effect
                                        if(b.canBuildCardType(x.getInt("effect",-1))){
                                            rs.add(x);
                                        }
                                        break;
                                    case 1://唯一地标
                                        if(!getsMapDAO.getGame().resGameConfig.onlyGeneralWonder&&!getsMapDAO.wonderIDatas.contains(wonderId)){
                                            rs.add(x);
                                        }
                                        break;
                                    case 3://特大油田
                                        if(b.getOilLv()>=5){
                                            rs.add(x);
                                        }
                                        break;
                                    case 4://特大矿场
                                        if(b.getMineralLv()>=5){
                                            rs.add(x);
                                        }
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            if(ResDefaultConfig.ifDebug){
                                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                            }else if(!getsMapDAO.getGame().gameConfig.getIfIgnoreBug()){
                                getsMapDAO.getGame().recordLog("GameMethod getWonderE ",e);
                                getsMapDAO.getGame().remindBugFeedBack();
                            }else {
                                getsMapDAO.getGame().recordLog("GameMethod getWonderE ",e);
                                continue;
                            }
                        }
                    }


                }
            }
        }
        Array<XmlReader.Element> elementArray=getsMapDAO.getGame().gameConfig.getDEF_WONDER().e.getChildrenByName("wonder");

        //增加普通的地标
        for(int i=0;i<elementArray.size;i++){
            XmlReader.Element wE=elementArray.get(i);
            int wonderId=wE.getInt("id",0);
            if(wonderId==b.getBuildWonder()){
                continue;
            }
            if(!rs.contains(wE,false)&&getsMapDAO.getAge()>=  wE.getInt("age",0)){
                int state=wE.getInt("state",0);
                switch (state){
                    case 0://普通地标
                        rs.add(wE);
                        break;
                    case 3://特大油田
                        if(b.getOilLv()>=5){
                            rs.add(wE);
                        }
                        break;
                    case 4://特大矿场
                        if(b.getMineralLv()>=5){
                            rs.add(wE);
                        }
                        break;
                }
            }
        }
        return rs;
    }


    //获取可以建造的地标
    public static Array<XmlReader.Element> getCanBuySpiritE(Fb2Smap sMapDAO, Fb2Smap.LegionData legionData, Fb2Smap.BuildData b, Array<XmlReader.Element> rs) {
        if(rs==null){
            rs=new Array<>();
        }else{
            rs.clear();
        }
        Array<XmlReader.Element> elementArray= sMapDAO.getGame().gameConfig.getDEF_SPIRIT().e.getChildrenByName("spirit");
        boolean ifHaveRepeatSpirit=false;
        for(int i=0;i<elementArray.size;i++){
            XmlReader.Element sE=elementArray.get(i);
            int spiritId=sE.getInt("id",0);
            int spiritType=sE.getInt("type",0);// type -1 不可用  0通用 1秩序 2强权 3 可购买可重叠 4 可购买不可重叠 5仅赠送可重叠 6仅赠送不可重叠
            if(sMapDAO.spiritMap.containsKey(spiritId) &&(spiritType==4||spiritType==6)){
                ifHaveRepeatSpirit=true;
                break;
            }
        }
        for(int i=0;i<elementArray.size;i++){
            XmlReader.Element sE=elementArray.get(i);
            int spiritId=sE.getInt("id",0);
            int spiritType=sE.getInt("type",0);// type -1 不可用  0通用 1秩序 2强权 3 可购买可重叠 4 可购买不可重叠 5仅赠送可重叠 6仅赠送不可重叠
            if(!sMapDAO.spiritMap.containsKey(spiritId)){
                String getCountrys=sE.get("getCountrys","-1");
                if(!getCountrys.equals("-1")&&!ComUtil.ifHaveValueInStr(getCountrys,legionData.getCountryId())){
                    continue;
                }
                switch (spiritType){
                    case 3:
                        rs.add(sE);
                        break;
                    case 4:
                        if(!ifHaveRepeatSpirit){
                            rs.add(sE);
                        }
                        break;
                    case 5://TODO 将来增加一个数据判断是否获得了赠送的奇物
                        rs.add(sE);
                        break;
                    case 6://TODO 将来增加一个数据判断是否获得了赠送的奇物
                        if(!ifHaveRepeatSpirit){
                            rs.add(sE);
                        }
                        break;

                }
            }
        }
        return rs;
    }



    public  String getCardInfoStr(Fb2Smap.LegionData legion, Fb2Smap.BuildData build, int cardId, int buyCardType) {
        if(buyCardType==17){
            XmlReader.Element x=game.gameConfig.getDEF_WONDER().getElementById(cardId);
            return    getStrValueT("buildwonder_info_"+cardId)+"\n"+getStrValueT("supplement_buildwonder","buildwonder_type_"+x.getInt("state",1),x.getInt("star",1));
        }else if(buyCardType==19){
            if(legion.getSpiritMap()==null){
                return "";
            }
            return game.gameMethod.getStrValue("spiritEffectInfo_"+cardId,legion.getSpiritMap().get(cardId,0));
        }

        XmlReader.Element x=game.gameConfig.getDEF_CARD().getElementById(cardId);
        int round=0;
        int unitLv=0;
        int readyRound=0;
        int cardType=x.getInt("type",0);
        if(x!=null){
            round=x.getInt("round",0);
            unitLv=legion.getCardTechLv(cardId,cardType)+1;
        }

        XmlReader.Element xE=game.gameConfig.getDEF_ARMY().getElementById(cardId);
        int age=legion.getAge();
        int grade=0;
        int population=0;
        int cardLv=getCardLv(legion,build,cardId);

        if(xE!=null){
            grade=legion.getArmyGradeLv(cardId,cardType);/*xE .getInt("grade",0)+(unitLv+2)/3*/;
            population=xE.getInt("population");
            readyRound=xE.getInt("round");
            /*if(legion.isUnitGroup(xE.getInt("type",-1))){
                population=1;
            }*/
        }

        switch (cardId){

            case 1003:
            case 1005:
            case 1008:
            case 1011:
                if(legion.getPlayerMode()==2){
                    return  getStrValueT("card_info_"+cardId+"_2",grade,round,population);
                }else{
                    return  getStrValueT("card_info_"+cardId,grade,round);
                }
            case 1001:return  getStrValueT("card_info_"+cardId,grade,round);
            case 1002:return  getStrValueT("card_info_"+cardId,grade,round);
            case 1004:return  getStrValueT("card_info_"+cardId,grade,round);
            case 1006:return  getStrValueT("card_info_"+cardId,grade,round);
            case 1101:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1102:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1103:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1104:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1105:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1106:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1201:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1202:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1203:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1204:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1205:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1206:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1207:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1301:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1302:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1303:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1304:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1305:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1306:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1401:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1402:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1403:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1404:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1405:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1406:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1407:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1408:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1501:return  getStrValueT("card_info_"+cardId,round,population,grade,readyRound);
            case 1502:return  getStrValueT("card_info_"+cardId,round,population,grade,readyRound);
            case 1503:return  getStrValueT("card_info_"+cardId,round,population,grade,readyRound);
            case 1504:return  getStrValueT("card_info_"+cardId,round,population,grade,readyRound);
            case 1505:return  getStrValueT("card_info_"+cardId,round,population,grade,readyRound);
            case 1506:return  getStrValueT("card_info_"+cardId,round,population,grade,readyRound);
            case 1507:return  getStrValueT("card_info_"+cardId,round,population,grade,readyRound);
            case 1601:return  getStrValueT("card_info_"+cardId,grade,round,readyRound);
            case 1602:return  getStrValueT("card_info_"+cardId,grade,round,readyRound);
            case 1603:return  getStrValueT("card_info_"+cardId,grade,round,readyRound);
            case 1604:return  getStrValueT("card_info_"+cardId,grade,round,readyRound);
            case 1605:return  getStrValueT("card_info_"+cardId,grade,round,readyRound);
            case 1606:return  getStrValueT("card_info_"+cardId,grade,round,readyRound);
            case 1701:return  getStrValueT("card_info_"+cardId,round,readyRound);
            case 1702:return  getStrValueT("card_info_"+cardId,round,readyRound);
            case 1703:return  getStrValueT("card_info_"+cardId,round,readyRound);
            case 1704:return  getStrValueT("card_info_"+cardId,round,readyRound);
            case 1705:return  getStrValueT("card_info_"+cardId,round,readyRound);
            case 1801:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1802:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1803:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 1804:return  getStrValueT("card_info_"+cardId,grade,round,population,readyRound);
            case 2001:return  getStrValueT("card_info_"+cardId,round);
            case 2002:
                if(legion.getPlayerMode()==0){
                    return  getStrValueT("card_info_2002",round,cardLv);
                }else{
                    return  getStrValueT("card_info_2002_1",round,cardLv);
                }
            case 2003:return  getStrValueT("card_info_"+cardId,round);
            case 2004:return  getStrValueT("card_info_"+cardId,round);
            case 2005:return  getStrValueT("card_info_"+cardId,round);
            case 2006:return  getStrValueT("card_info_"+cardId,round);
            case 2007://城市中只有一个设施的等级与其持平,否则升级回合翻倍
                if(build!=null&&build.ifBuildLvIsOnlyTop(2007)){
                    int poor=ComUtil.max(1,build.getBuildCardLvPoorForCityLv());
                    return  getStrValueT("card_info_"+cardId,poor+round);
                }else {
                    return  getStrValueT("card_info_"+cardId,round);
                }
            case 2008:
                if(legion.ifEffective(1)){
                    return  getStrValueT("card_info_"+cardId,round);
                }else {
                    return  getStrValueT("card_info_2008_1",round);
                }
            case 2009:return  getStrValueT("card_info_"+cardId,round);
            case 2010:return  getStrValueT("card_info_"+cardId,round);
            case 2011:return  getStrValueT("card_info_"+cardId,round);
            case 2012:
                if(legion.getAge()<2){
                    return  getStrValueT("card_info_"+cardId,round);
                }else{
                    return  getStrValueT("card_info_2012_2",round);
                }
            case 2013:return  getStrValueT("card_info_"+cardId,round);
            case 2014:return  getStrValueT("card_info_"+cardId,round);

            //当前可在移动损耗不大于{build.getTransportLvNow()}的地块修建交通线,研究回合1~{build.getTransportLvNow()+1}
            case 2015:
                if(build.ifClick()){//是否被选中
                    return  getStrValueT("card_info_"+cardId,build.getTransportLvNow()+1,build.getTransportLvNow());
                }else{
                    return  getStrValueT("card_info_2015_1",build.getTransportLvNow()+1);
                }
            case 3001:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),game.resGameConfig.cityLvExtraAddFoodRate*(legion.getCityLvMax()+1),game.resGameConfig.foodIncomeMaxRate*100);


            case 3002:
                if( legion.ifEffective(1)){
                    return  getStrValueT("card_info_3002_1",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),game.resGameConfig.industryLvExtraAddIndustryRate*(legion.getIndustLvMax()+1),game.resGameConfig.industryIncomeMaxRate*100);
                }else{
                    return  getStrValueT("card_info_3002",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),game.resGameConfig.industryLvExtraAddIndustryRate*(legion.getIndustLvMax()+1),game.resGameConfig.industryIncomeMaxRate*100);
                }
            case 3003:
                if( legion.ifEffective(2)){
                    return  getStrValueT("card_info_3003_1",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),game.resGameConfig.techLvExtraAddTechRate*(legion.getTechLvMax()+1),game.resGameConfig.techIncomeMaxRate*100);
                }else{
                    return  getStrValueT("card_info_3003",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),game.resGameConfig.techLvExtraAddTechRate*(legion.getTechLvMax()+1),game.resGameConfig.techIncomeMaxRate*100);
                }
            case 3004:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3005:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3006:
                if(legion.getGameMode()==2){return  getStrValueT("card_info_3006_2",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));}return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),getCardLv(legion,null,cardId)+1,getCardLv(legion,null,cardId)+1,(getCardLv(legion,null,cardId)+1+2)/4);
            case 3007:  if(legion.getGameMode()==2){return  getStrValueT("card_info_3007_2",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));}return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),getCardLv(legion,null,cardId)+1,getCardLv(legion,null,cardId)+1,(getCardLv(legion,null,cardId)+1+2)/4);
            case 3008:  if(legion.getGameMode()==2){return  getStrValueT("card_info_3008_2",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));}return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),getCardLv(legion,null,cardId)+1,getCardLv(legion,null,cardId)+1,(getCardLv(legion,null,cardId)+1+2)/4);
            case 3009:  if(legion.getGameMode()==2){return  getStrValueT("card_info_3009_2",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));}return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),getCardLv(legion,null,cardId)+1,getCardLv(legion,null,cardId)+1,legion.getFortLvMax()+1);
            case 3010:  if(legion.getGameMode()==2){return  getStrValueT("card_info_3010_2",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));}return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),getCardLv(legion,null,cardId)+1,getCardLv(legion,null,cardId)+1,(getCardLv(legion,null,cardId)+1+2)/4);
            case 3011:  if(legion.getGameMode()==2){return  getStrValueT("card_info_3011_2",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));}return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),getCardLv(legion,null,cardId)+1,getCardLv(legion,null,cardId)+1,(getCardLv(legion,null,cardId)+1+2)/4);
            case 3012:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3013:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3014:
                if(legion.getGameMode()==2){return  getStrValueT("card_info_3014_2",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
                }else if(age<2){
                    return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
                }else {
                    return  getStrValueT("card_info_3014_1",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),getCardLv(legion,null,cardId)+1,getCardLv(legion,null,cardId)+1);
                }
            case 3015:if(legion.getGameMode()==2){return  getStrValueT("card_info_3015_2",round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));}return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(getCardLv(legion,null,cardId)+1)*2,getCardLv(legion,null,cardId)+1);
            case 3016:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3017:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),game.resGameConfig.tradeLvExtraAddMoneyRate*legion.getTradeLvMax());
            case 3018:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3019:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3020:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(unitLv+2)/3);
            case 3021:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(unitLv+2)/3);
            case 3022:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(unitLv+2)/3);
            case 3023:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(unitLv+2)/3);
            case 3024:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(unitLv+2)/3);
            case 3025:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3026:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(unitLv+2)/3);
            case 3027:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(unitLv+2)/3);
            case 3028:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()),(unitLv+2)/3);
            case 3029:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));
            case 3030:return  getStrValueT("card_info_"+cardId,round+legion.getUpdTechLegionRound(cardId,legion.isPlayer()));

            case 5005:return  getStrValueT("card_info_"+cardId,legion.getDrapRound());
        }
        return  getStrValueT("card_info_"+cardId);
    }


    public int getBuildEnergyCost(Fb2Smap.BuildData build){
        return build.getTradeLvNow()*game.gameConfig.getDEF_CARD().getElementById(2001).getInt("energy",1)+
                build.getCultureLvNow()*game.gameConfig.getDEF_CARD().getElementById(2002).getInt("energy",1)+
                build.getTransportLvNow()*game.gameConfig.getDEF_CARD().getElementById(2003).getInt("energy",1)+
                build.getTechLvNow()*game.gameConfig.getDEF_CARD().getElementById(2004).getInt("energy",1)+
                build.getFoodLvNow()*game.gameConfig.getDEF_CARD().getElementById(2005).getInt("energy",1)+
                build.getCityLvNow()*game.gameConfig.getDEF_CARD().getElementById(2007).getInt("energy",1)+
                build.getIndustryLvNow()*game.gameConfig.getDEF_CARD().getElementById(2008).getInt("energy",1)+
                build.getSupplyLvNow()*game.gameConfig.getDEF_CARD().getElementById(2009).getInt("energy",1)+
                build.getDefenceLvNow()*game.gameConfig.getDEF_CARD().getElementById(2010).getInt("energy",1)+
                build.getAirLvNow()*game.gameConfig.getDEF_CARD().getElementById(2011).getInt("energy",1)+
                build.getMissileLvNow()*game.gameConfig.getDEF_CARD().getElementById(2012).getInt("energy",1)+
                build.getNuclearLvNow()*game.gameConfig.getDEF_CARD().getElementById(2013).getInt("energy",1)+
                build.getArmyLvNow()*game.gameConfig.getDEF_CARD().getElementById(2014).getInt("energy",1);
    }
    public boolean ifEnergyOverload(Fb2Smap.BuildData build){
        if(build.getCityLvNow()>4&&getBuildEnergyCost(build)>build.getBuildEnergyAfford()){
            return true;
        }else {
            return false;
        }
    }




    //检测卡牌是否
    //检测卡牌是否被锁住,暂时只检查 建筑类
    public static boolean buildCardIfLock(MainGame game,Fb2Smap.LegionData legionData, Fb2Smap.BuildData build, int cardId) {
        if(cardId<3000&&build==null){
            return true;
        }
        switch (cardId) {
            case 2001:if ((build.isCapital())&&build.getTradeLvNow()< ComUtil.limitValue(legionData.getTradeLvMax()+2,0, game.resGameConfig.cardUpdMax_tradeLv ) ) {
                return false;
            } else if (legionData.getLegionFeatureEffect(1)!=0&&build.getTradeLvNow()<ComUtil.limitValue(legionData.getTradeLvMax()+legionData.getLegionFeatureEffect(1),0, game.resGameConfig.cardUpdMax_tradeLv )  ) {
                return false;
            }else if (build.getTradeLvNow() >= legionData.getTradeLvMax()) {
                return true;
            }else {
                return false;
            }
            case 2002:
                if ((build.isCapital())&&build.getCultureLvNow()< ComUtil.limitValue(legionData.getCultureLvMax()+2,0, game.resGameConfig.cardUpdMax_cultureLv)) {
                    return false;
                }else if (legionData.getLegionFeatureEffect(2)!=0&&build.getCultureLvNow()<ComUtil.limitValue(legionData.getCultureLvMax()+legionData.getLegionFeatureEffect(2),0, game.resGameConfig.cardUpdMax_cultureLv )  ) {
                    return false;
                } else if (build.getCultureLvNow() >= legionData.getCultureLvMax()) {
                    return true;
                }else {
                    return false;
                }
            case 2003:if ((build.isCapital())&&build.getTransportLvNow() < ComUtil.limitValue(legionData.getTransportLvMax()+2,0, game.resGameConfig.cardUpdMax_transportLv)) {
                return false;
            }else if (legionData.getLegionFeatureEffect(3)!=0&&build.getTransportLvNow()<ComUtil.limitValue(legionData.getTransportLvMax()+legionData.getLegionFeatureEffect(3),0, game.resGameConfig.cardUpdMax_transportLv )  ) {
                return false;
            }  else if (build.getTransportLvNow() >= legionData.getTransportLvMax()) {
                return true;
            }else {
                return false;
            }
            case 2004:
                if ((build.isCapital())&&build.getTechLvNow()< ComUtil.limitValue(legionData.getTechLvMax()+2,0, game.resGameConfig.cardUpdMax_techLv)) {
                    return false;
                }else  if (legionData.getLegionFeatureEffect(4)!=0&&build.getTechLvNow()<ComUtil.limitValue(legionData.getTechLvMax()+legionData.getLegionFeatureEffect(4),0, game.resGameConfig.cardUpdMax_techLv)  ) {
                    return false;
                }else
                if (build.getTechLvNow() >= legionData.getTechLvMax()) {
                    return true;
                }else {
                    return false;
                }
            case 2005:if ((build.isCapital())&&build.getFoodLvNow()< ComUtil.limitValue(legionData.getCityLvMax()+2,0, game.resGameConfig.cardUpdMax_foodLv)) {
                return false;
            }else if (legionData.getLegionFeatureEffect(5)!=0&&build.getFoodLvNow()<ComUtil.limitValue(legionData.getCityLvMax()+legionData.getLegionFeatureEffect(5),0, game.resGameConfig.cardUpdMax_foodLv )  ) {
                return false;
            }  else if (build.getFoodLvNow() >= legionData.getCityLvMax()) {
                return true;
            }else {
                return false;
            }/**/
            case 2006:if ((build.isCapital())&&build.getEnergyLvNow()< ComUtil.limitValue(legionData.getEnergyLvMax()+2,0, game.resGameConfig.cardUpdMax_eneryLv)) {
                return false;
            }else if (legionData.getLegionFeatureEffect(6)!=0&&build.getEnergyLvNow()<ComUtil.limitValue(legionData.getEnergyLvMax()+legionData.getLegionFeatureEffect(6),0, game.resGameConfig.cardUpdMax_eneryLv )  ) {
                return false;
            }  else if (build.getEnergyLvNow() >= legionData.getEnergyLvMax()) {
                return true;
            } else {
                return false;
            }
            case 2007:if (build.isCapital()&&build.getCityLvNow()< ComUtil.limitValue(legionData.getCityLvMax()+2,0, game.resGameConfig.cardUpdMax_cityLv)) {
                return false;
            }else if (legionData.getLegionFeatureEffect(7)!=0&&build.getCityLvNow()<ComUtil.limitValue(legionData.getCityLvMax()+legionData.getLegionFeatureEffect(7),0, game.resGameConfig.cardUpdMax_cityLv )  ) {
                return false;
            }  else if (build.getCityLvNow() >= legionData.getCityLvMax()) {
                return true;
            } else {
                return false;
            }
            case 2008:if ((build.isCapital())&&build.getIndustryLvNow()< ComUtil.limitValue(legionData.getIndustLvMax()+2,0, game.resGameConfig.cardUpdMax_industryLv)) {
                return false;
            }else if (legionData.getLegionFeatureEffect(8)!=0&&build.getIndustryLvNow()<ComUtil.limitValue(legionData.getIndustLvMax()+legionData.getLegionFeatureEffect(8),0, game.resGameConfig.cardUpdMax_industryLv )  ) {
                return false;
            }  else if (build.getIndustryLvNow() >= legionData.getIndustLvMax()) {
                return true;
            }else {
                return false;
            }
            case 2009:if ((build.isCapital())&&build.getSupplyLvNow()< ComUtil.limitValue(legionData.getSupplyLvMax()+2,0, game.resGameConfig.cardUpdMax_supplyLv)) {
                return false;
            }else if (legionData.getLegionFeatureEffect(9)!=0&&build.getSupplyLvNow()<ComUtil.limitValue(legionData.getSupplyLvMax()+legionData.getLegionFeatureEffect(9),0, game.resGameConfig.cardUpdMax_supplyLv )  ) {
                return false;
            }  else if (build.getSupplyLvNow() >= legionData.getSupplyLvMax()) {
                return true;
            } else {
                return false;
            }
            case 2010:
                if (legionData.getLegionFeatureEffect(10)!=0&&build.getDefenceLvNow()<ComUtil.limitValue(legionData.getDefenceLvMax()+legionData.getLegionFeatureEffect(10),0, game.resGameConfig.cardUpdMax_defenceLv)  ) {
                    return false;
                }else
                if (build.getDefenceLvNow() >= legionData.getDefenceLvMax()) {
                    return true;
                }else {
                    return false;
                }
            case 2011:
                if (legionData.getLegionFeatureEffect(11)!=0&&build.getAirLvNow()<ComUtil.limitValue(legionData.getAirLvMax()+legionData.getLegionFeatureEffect(11),0, game.resGameConfig.cardUpdMax_airportLv )  ) {
                    return false;
                } else  if (build.getAirLvNow() >= legionData.getAirLvMax()) {
                    return true;
                } else {
                    return false;
                }
            case 2012:
                if (legionData.getLegionFeatureEffect(12)!=0&&build.getMissileLvNow()<ComUtil.limitValue(legionData.getMissileLvMax()+legionData.getLegionFeatureEffect(12),0, game.resGameConfig.cardUpdMax_missileLv )  ) {
                    return false;
                } else if (build.getMissileLvNow() >= legionData.getMissileLvMax()) {
                    return true;
                }else {
                    return false;
                }
            case 2013:
                if (legionData.getLegionFeatureEffect(13)!=0&&build.getNuclearLvNow()<ComUtil.limitValue(legionData.getNuclearLvMax()+legionData.getLegionFeatureEffect(13),0, game.resGameConfig.cardUpdMax_nuclearLv )  ) {
                    return false;
                } else if (build.getNuclearLvNow() >= legionData.getNuclearLvMax()) {
                    return true;
                }else {
                    return false;
                }
            case 2014:if ((build.isCapital())&&build.getArmyLvNow() < ComUtil.limitValue(legionData.getCityLvMax()+2,0, game.resGameConfig.cardUpdMax_cityLv)) {
                return false;
            }else if (legionData.getLegionFeatureEffect(14)!=0&&build.getArmyLvNow()<ComUtil.limitValue(legionData.getCityLvMax()+legionData.getLegionFeatureEffect(14),0, game.resGameConfig.cardUpdMax_cityLv )  ) {
                return false;
            }  else if (build.getArmyLvNow() >= legionData.getCityLvMax()) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }


    //检测卡牌是否缺少能源
    public static boolean buildCardIfNoEnemy(MainGame game,Fb2Smap.LegionData legionData, Fb2Smap.BuildData build, int cardId) {
        if(cardId<3000&&build==null){
            return true;
        }
        switch (cardId) {
            case 2001:
            case 2002:
            case 2003:
            case 2004:
            case 2005:
            case 2007:
            case 2008:
            case 2009:
            case 2010:
            case 2011:
            case 2012:
            case 2013:
            case 2014:
                if(build.ifEnergyOverload())  {
                    return true;
                }else {
                    return false;
                }
        }
        return false;
    }


    //检测卡牌是否满级暂时只检查 科技类
    public static int getCardLv(Fb2Smap.LegionData legionData, Fb2Smap.BuildData build, int cardId) {
        if(legionData==null||(build==null&&cardId>=2000&&cardId<3000)){
            return 0;
        }
        switch (cardId) {
          /*  case 1001: return ;
            case 1002: return ;
            case 1003: return ;
            case 1004: return ;
            case 1005: return ;
            case 1006: return ;*/
            case 1101: return legionData.getInfantryLvMax();
            case 1102: return legionData.getInfantryLvMax();
            case 1103: return legionData.getInfantryLvMax();
            case 1104: return legionData.getInfantryLvMax();
            case 1105: return legionData.getInfantryLvMax();
            case 1201: return legionData.getCannonLvMax();
            case 1202: return legionData.getCannonLvMax();
            case 1203: return legionData.getCannonLvMax();
            case 1204: return legionData.getCannonLvMax();
            case 1205: return legionData.getCannonLvMax();
            case 1206: return legionData.getCannonLvMax();
            case 1207: return legionData.getCannonLvMax();
            case 1301: return legionData.getTankLvMax();
            case 1302: return legionData.getTankLvMax();
            case 1303: return legionData.getTankLvMax();
            case 1304: return legionData.getTankLvMax();
            case 1305: return legionData.getTankLvMax();
            case 1306: return legionData.getTankLvMax();
            case 1400: return legionData.getNavyLvMax();
            case 1401: return legionData.getNavyLvMax();
            case 1402: return legionData.getNavyLvMax();
            case 1403: return legionData.getNavyLvMax();
            case 1404: return legionData.getNavyLvMax();
            case 1405: return legionData.getNavyLvMax();
            case 1406: return legionData.getNavyLvMax();
            case 1407: return legionData.getNavyLvMax();
            case 1408: return legionData.getNavyLvMax();
            case 1501: return legionData.getAirLvMax();
            case 1502: return legionData.getAirLvMax();
            case 1503: return legionData.getAirLvMax();
            case 1504: return legionData.getAirLvMax();
            case 1505: return legionData.getAirLvMax();
            case 1506: return legionData.getAirLvMax();
            case 1507: return legionData.getAirLvMax();
            case 1601: return legionData.getDefenceLvMax();
            case 1602: return legionData.getDefenceLvMax();
            case 1603: return legionData.getDefenceLvMax();
            case 1604: return legionData.getDefenceLvMax();
            case 1605: return legionData.getDefenceLvMax();
            case 1606: return legionData.getMissileLvMax();
            case 1701: return legionData.getNuclearLvMax();
            case 1702: return legionData.getNuclearLvMax();
            case 1703: return legionData.getNuclearLvMax();
            case 1704: return legionData.getNuclearLvMax();
            case 1705: return legionData.getNuclearLvMax();
            case 1801: return legionData.getNavyLvMax();
            case 1802: return legionData.getNavyLvMax();
            case 1803: return legionData.getNavyLvMax();
            case 1804: return legionData.getNavyLvMax();
            case 2001:
                return build.getTradeLvNow();
            case 2002:
                return build.getCultureLvNow();
            case 2003:
                return build.getTransportLvNow();
            case 2004:
                return build.getTechLvNow();
            case 2005:
                return build.getFoodLvNow();
            case 2006:
                return build.getEnergyLvNow();
            case 2007:
                return build.getCityLvNow();
            case 2008:
                return build.getIndustryLvNow();
            case 2009:
                return build.getSupplyLvNow();
            case 2010:
                return build.getDefenceLvNow();
            case 2011:
                return build.getAirLvNow();
            case 2012:
                return build.getMissileLvNow();
            case 2013:
                return build.getNuclearLvNow();
            case 2014:
                return build.getArmyLvNow();
            case 3001:
                return legionData.getCityLvMax();
            case 3002:
                return legionData.getIndustLvMax();
            case 3003:
                return legionData.getTechLvMax();
            case 3004:
                return legionData.getEnergyLvMax();
            case 3005:
                return legionData.getTransportLvMax();
            case 3006:
                return legionData.getInfantryLvMax();
            case 3007:
                return legionData.getCannonLvMax();
            case 3008:
                return legionData.getTankLvMax();
            case 3009:
                return legionData.getFortLvMax();
            case 3010:
                return legionData.getNavyLvMax();
            case 3011:
                return legionData.getAirLvMax();
            case 3012:
                return legionData.getSupplyLvMax();
            case 3013:
                return legionData.getDefenceLvMax();
            case 3014:
                return legionData.getMissileLvMax();
            case 3015:
                return legionData.getNuclearLvMax();
            case 3016:
                return legionData.getFinancialLvMax();
            case 3017:
                return legionData.getTradeLvMax();
            case 3018:
                return legionData.getCultureLvMax();
            case 3019:
                return legionData.getMiracleNow();
            case 3020:
                return legionData.getInfantryCardMax();
            case 3021:
                return legionData.getArmorCardMax();
            case 3022:
                return legionData.getArtilleryCardMax();
            case 3023:
                return legionData.getNavyCardMax();
            case 3024:
                return legionData.getAirCardMax();
            case 3025:
                return legionData.getNuclearCardMax();
            case 3026:
                return legionData.getMissileCardMax();
            case 3027:
                return legionData.getSubmarineCardMax();
            case 3028:
                return legionData.getDefenceCardMax();
            case 3029:
                return legionData.getGeneralCardMax();
            case 3030:
                return legionData.getMilitaryAcademyLv();
           /* case 3101: if(){ return true;}else{  return false; }
            case 3102: if(){ return true;}else{  return false; }
            case 3103: if(){ return true;}else{  return false; }
            case 3104: if(){ return true;}else{  return false; }
            case 3105: if(){ return true;}else{  return false; }*/

        }
        return 0;
    }
    public static void setCardLv(Fb2Smap.LegionData legionData, Fb2Smap.BuildData build, int cardId,int v) {
        switch (cardId) {
          /*  case 1001:  ;
            case 1002:  ;
            case 1003:  ;
            case 1004:  ;
            case 1005:  ;
            case 1006:  ;*/
            case 1101:  legionData.setInfantryLvMax(v);
            case 1102:  legionData.setInfantryLvMax(v);
            case 1103:  legionData.setInfantryLvMax(v);
            case 1104:  legionData.setInfantryLvMax(v);
            case 1105:  legionData.setInfantryLvMax(v);
            case 1201:  legionData.setCannonLvMax(v);
            case 1202:  legionData.setCannonLvMax(v);
            case 1203:  legionData.setCannonLvMax(v);
            case 1204:  legionData.setCannonLvMax(v);
            case 1205:  legionData.setCannonLvMax(v);
            case 1206:  legionData.setCannonLvMax(v);
            case 1207:  legionData.setCannonLvMax(v);
            case 1301:  legionData.setArmorCardMax(v);
            case 1302:  legionData.setArmorCardMax(v);
            case 1303:  legionData.setArmorCardMax(v);
            case 1304:  legionData.setArmorCardMax(v);
            case 1305:  legionData.setArmorCardMax(v);
            case 1306:  legionData.setArmorCardMax(v);
            case 1400:  legionData.setNavyLvMax(v);
            case 1401:  legionData.setNavyLvMax(v);
            case 1402:  legionData.setNavyLvMax(v);
            case 1403:  legionData.setNavyLvMax(v);
            case 1404:  legionData.setNavyLvMax(v);
            case 1405:  legionData.setNavyLvMax(v);
            case 1406:  legionData.setNavyLvMax(v);
            case 1407:  legionData.setNavyLvMax(v);
            case 1408:  legionData.setNavyLvMax(v);
            case 1501:  legionData.setAirLvMax(v);
            case 1502:  legionData.setAirLvMax(v);
            case 1503:  legionData.setAirLvMax(v);
            case 1504:  legionData.setAirLvMax(v);
            case 1505:  legionData.setAirLvMax(v);
            case 1506:  legionData.setAirLvMax(v);
            case 1507:  legionData.setAirLvMax(v);
            case 1601:  legionData.setNavyLvMax(v);
            case 1602:  legionData.setNavyLvMax(v);
            case 1603:  legionData.setNavyLvMax(v);
            case 1604:  legionData.setNavyLvMax(v);
            case 1605:  legionData.setNavyLvMax(v);
            case 1606:  legionData.setNavyLvMax(v);
            case 1701:  legionData.setNuclearLvMax(v);
            case 1702:  legionData.setNuclearLvMax(v);
            case 1703:  legionData.setNuclearLvMax(v);
            case 1704:  legionData.setNuclearLvMax(v);
            case 1705:  legionData.setNuclearLvMax(v);
            case 1801:  legionData.setNavyLvMax(v);
            case 1802:  legionData.setNavyLvMax(v);
            case 1803:  legionData.setNavyLvMax(v);
            case 1804:  legionData.setNavyLvMax(v);
            case 2001:
                build.setTradeLvNow(v);
            case 2002:
                build.setCultureLvNow(v);
            case 2003:
                build.setTransportLvNow(v);
            case 2004:
                build.setTechLvNow(v);
            case 2005:
                build.setFoodLvNow(v);
            case 2006:
                build.setEnergyLvNow(v);
            case 2007:
                build.setCityLvNow(v);
            case 2008:
                build.setIndustryLvNow(v);
            case 2009:
                build.setSupplyLvNow(v);
            case 2010:
                build.setDefenceLvNow(v);
            case 2011:
                build.setAirLvNow(v);
            case 2012:
                build.setMissileLvNow(v);
            case 2013:
                build.setNuclearLvNow(v);
            case 2014:
                build.setArmyLvNow(v);
            case 3001:
                legionData.setCityLvMax(v);
            case 3002:
                legionData.setIndustLvMax(v);
            case 3003:
                legionData.setTechLvMax(v);
            case 3004:
                legionData.setEnergyLvMax(v);
            case 3005:
                legionData.setTransportLvMax(v);
            case 3006:
                legionData.setInfantryLvMax(v);
            case 3007:
                legionData.setCannonLvMax(v);
            case 3008:
                legionData.setTankLvMax(v);
            case 3009:
                legionData.setFortLvMax(v);
            case 3010:
                legionData.setNavyLvMax(v);
            case 3011:
                legionData.setAirLvMax(v);
            case 3012:
                legionData.setSupplyLvMax(v);
            case 3013:
                legionData.setDefenceLvMax(v);
            case 3014:
                legionData.setMissileLvMax(v);
            case 3015:
                legionData.setNuclearLvMax(v);
            case 3016:
                legionData.setFinancialLvMax(v);
            case 3017:
                legionData.setTradeLvMax(v);
            case 3018:
                legionData.setCultureLvMax(v);
            case 3019:
                legionData.setMiracleNow(v);
            case 3020:
                legionData.setInfantryCardMax(v);
            case 3021:
                legionData.setArmorCardMax(v);
            case 3022:
                legionData.setArtilleryCardMax(v);
            case 3023:
                legionData.setNavyCardMax(v);
            case 3024:
                legionData.setAirCardMax(v);
            case 3025:
                legionData.setNuclearCardMax(v);
            case 3026:
                legionData.setMissileCardMax(v);
            case 3027:
                legionData.setSubmarineCardMax(v);
            case 3028:
                legionData.setDefenceCardMax(v);
            case 3029:
                legionData.setGeneralCardMax(v);
            case 3030:
                legionData.setMilitaryAcademyLv(v);
           /* case 3101: if(){  true;}else{   false; }
            case 3102: if(){  true;}else{   false; }
            case 3103: if(){  true;}else{   false; }
            case 3104: if(){  true;}else{   false; }
            case 3105: if(){  true;}else{   false; }*/

        }
    }
    //int buildType, int cityLv, int indLv, int techLv, int airLv,int age,
    public static Array<XmlReader.Element> getCardEByFilterCardE(Fb2Smap smapDAO, Fb2Smap.LegionData legionData, Fb2Smap.BuildData build, int buyType,boolean checkBuildLimit, Array<XmlReader.Element> filterCardE, Array<XmlReader.Element> rs,boolean ifShowMaxCard){


        if(rs==null){
            rs= new Array<>();
        }else{
            rs.clear();
        }
        {
            // XmlReader.Element root = ResConfig.gameConfig.getDEF_CARD().e;
            int age=smapDAO.worldData.getWorldAge();
            if(build.getBuildType()==2&&!smapDAO.isEditMode(true)){
                return rs;
            }
            for(int i=0;i<filterCardE.size;i++){
                XmlReader.Element cardE=filterCardE.get(i);
                int cardId=cardE.getInt("id");
                if(!ifShowMaxCard&&cardIfMax(smapDAO.getGame(),legionData,build,cardId)){
                        continue;
                }
                int cardType=cardE.getInt("type");
               /* if((cardType==9||cardType==10)&&!build.ifCanBuildCardForCityLv(cardId)){
                    continue;
                }*/
                if(build.getBuildType()==1 && ( conformCardEByCardType(cardE,buyType) )&&(cardType ==4||cardType ==8)
                        &&age>=cardE.getInt("age")){
                    if(checkBuildLimit){
                        if(build.canBuyCard(cardId)){
                            rs.add(filterCardE.get(i));
                        }
                    }else{
                        rs.add(filterCardE.get(i));
                    }
                }else {
                    if( conformCardEByCardType(cardE,buyType) /*&&*/
                            &&age>=cardE.getInt("age")) {
                        if(checkBuildLimit){
                            if(build.canBuyCard(cardId)){
                                rs.add(filterCardE.get(i));
                            }
                        }else{
                            rs.add(filterCardE.get(i));
                        }
                    }
                }
            }
            if(rs.size<=0){
                Gdx.app.error("getCanBuildCardByLegion","cardType:"+buyType);
            }
            // Gdx.app.log("getCard","size:"+rs.size);
            return rs;
        }
    }

    //cardType 0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡 -1全部  -2全部军队 -3全部陆军 -4全部海军 -5 全部建筑用卡牌 -6全部陆军和海军
    //buildType 0城市 1海港 2海洋(海洋不能建造) 3临海城市
    public static Array<XmlReader.Element> getCardE(MainGame game, Fb2Smap smapDAO, Fb2Smap.LegionData legionData, Fb2Smap.BuildData build, int cardType, Array<XmlReader.Element> rs){
        if(rs==null){
            rs= new Array<>();
        }else{
            rs.clear();
        }

        // Gdx.app.log("getCard","size:"+rs.size);
        // return getCardEByFilterCardE( cardType, buildType,  cityLv,  indLv,  techLv,  airLv,  age,ResConfig.gameConfig.getDEF_CARD().e.getChildrenByName("card"),rs);
        return  getCardEByFilterCardE( smapDAO,legionData, build, cardType,false,  game.gameConfig.getDEF_CARD().e.getChildrenByName("card"), rs,smapDAO.isEditMode(true));

    }




    //根据时代和玩法获得可用的科技卡牌
    public static Array<XmlReader.Element> initPublicLegionCardE(MainGame game,Fb2Smap btl,  Array<XmlReader.Element> rs){
        if(rs==null){
            rs= new Array<>();
        }else{
            rs.clear();
        }
        int age=btl.getAge();
        int playerMode=btl.masterData.getPlayerMode();
        XmlReader.Element root = game.gameConfig.getDEF_CARD().e;
        Array<XmlReader.Element> xmlFiles =root.getChildrenByName("card");
        XmlReader.Element xE;
        for(int i=0;i<xmlFiles.size;i++) {
            xE= xmlFiles.get(i);
            if (age >= xE.getInt("age")) {
                String useMode=xE.get("useMode","-1");
                if(useMode.equals("-1")||ComUtil.ifHaveValueInStr(useMode,playerMode)){
                    rs.add(xE);
                }
                     /*String countrys=xE.get("useCountrys","-1");
                   if(useMode.equals("-1")||ComUtil.ifHaveValueInStr(useMode,playerMode)){
                        if(countrys.equals("-1")||ComUtil.ifHaveValueInStr(countrys,l.getCountryId())){
                            rs.add(xE);
                        }
                    }*/
            }
        }
        //如果不开启一些特性,则去除一些卡牌
        if(!btl.ifSystemEffective(7)){
            GameUtil.removeXmlEById(rs,2006);
            GameUtil.removeXmlEById(rs,3004);
        }


        // Gdx.app.log("getCard","size:"+rs.size);



        return rs;
    }





    public static Array<XmlReader.Element> getCanBuildCardByLegion(MainGame game,Fb2Smap smapDAO,Fb2Smap.LegionData l, Fb2Smap.BuildData b,  Array<XmlReader.Element> rs){
        if(rs==null){
            rs=new Array<>();
        }else{
            rs.clear();
        }
        Array<XmlReader.Element> cardE=getCardE(game,smapDAO,l,b,-1,rs);
        for(int i=cardE.size-1;i>=0;i--){
            if(cardIfMax(game,l,b,cardE.get(i).getInt("id"))){
                cardE.removeIndex(i);
            }
        }
        return cardE;
    }

    public static IntArray getCanBuildCardByLegion(MainGame game,Fb2Smap smap,Fb2Smap.LegionData l, Fb2Smap.BuildData b,  int buyType, IntArray rs,boolean limitArmyCardBuy){
        if(rs==null){
            rs=new IntArray();
        }else{
            rs.clear();
        }
        int cardId,cardType;
        for(int i=l.varLegionCanBuildCardE.size-1;i>=0;i--){
            XmlReader.Element armyE=l.varLegionCanBuildCardE.get(i);
            cardId=armyE.getInt("id");
            cardType=armyE.getInt("type");
            if(cardId==1406){
                int s=0;
            }
            boolean buildCanBuyCard;
            if(limitArmyCardBuy){
                buildCanBuyCard=b.canBuyCard(cardId);
            }else{
                if(cardType>=1&&cardType<=8){
                    XmlReader.Element cardE=game.gameConfig.getDEF_CARD().getElementById(cardId);
                    if(cardE==null){
                        buildCanBuyCard= false;
                    }else{
                        int age=cardE.getInt("age");
                        if(b.getCityHpNow()==0||b.getCityHpMax()==0||age>l.getAge()){
                            buildCanBuyCard=false;
                        }else{
                            buildCanBuyCard=true;
                        }
                    }
                }else{
                    buildCanBuyCard=b.canBuyCard(cardId);
                }
            }
            if(!cardIfMax(game,l,b,cardId)&&buildCanBuyCard){
                if(conformCardEByCardType(armyE,buyType)&&!rs.contains(cardId)){
                    switch (cardType){
                        case 5://飞机
                            if(smap.masterData.getPlayerMode()==2||b.canRecruitAir()){
                                rs.add(cardId);
                            }
                            break;
                        case 6://防御建筑
                            if((b.ifBorderRegionWar()||b.getBuildStatus()!=0||(b.getCityHpNow()!=b.getCityHpMax()&&b.getCityHpMax()>0)||b.ifHaveEnemyUnit())){
                                rs.add(cardId);
                            }
                            break;
                        case 7://弹头武器
                            if(smap.masterData.getPlayerMode()==2||b.canRecruitNul()){
                                rs.add(cardId);
                            }
                            break;
                        default:
                            rs.add(cardId);
                            break;
                    }
                }
            }
        }
        if(rs.contains(1406)){
            int s=0;
        }
        return rs;
    }

    //是否符合cardType
    //type 0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11国策卡 12外交卡 -1全部  -2全部军队 -3全部陆军 -4全部海军 -5 全部建筑用卡牌 -6全部陆军和海军 -7除海军以外的其他部队
    public static boolean conformCardEByCardType(XmlReader.Element cardE, int buyType){
        int cardEType=cardE.getInt("type");
        switch (buyType){

            case -1: return true;
            case -2:if(cardEType>=1&&cardEType<=8){
                return true;
            }break;
            case -3:if( cardEType==1||cardEType==2||cardEType==3||cardEType==6){
                return true;
            }break;
            case -4:if( cardEType==4||cardEType==8){
                return true;
            }break;
            case -5:if( cardEType==9||cardEType==10){
                return true;
            }break;
            case -6:if( cardEType==1||cardEType==2||cardEType==3||cardEType==6||cardEType==4||cardEType==8){
                return true;
            }break;
            case -7:if(cardEType>=1&&cardEType<8&&cardEType!=4){
                return true;
            }break;
            default:if(cardEType==buyType){
                return true;
            }break;
        }
        return false;
    }


    //获得可以升级的等级   type:1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
   /* public static int getWeaponLvMax(int type, Fb2Smap.LegionData legionData){
        switch (type){
            case 1:return legionData.getInfantryLvMax();
            case 2:return legionData.getCannonLvMax();
            case 3:return legionData.getTankLvMax();
            case 4:return legionData.getNavyLvMax();
            case 5:return legionData.getAirLvMax();
            case 6:return legionData.getFortLvMax();
            case 7:return legionData.getNuclearLvMax();
            case 8:return legionData.getNavyLvMax();
            default:return 0;
        }
    }*/




    //检测卡牌是否满级
    public static boolean cardIfMax(MainGame game,Fb2Smap.LegionData legion, Fb2Smap.BuildData build, int cardId) {
        if(!game.gameConfig.getDEF_CARD().contain(cardId)){
            return false;
        }
        XmlReader.Element xmlE=game.gameConfig.getDEF_CARD().getElementById(cardId);
        if(xmlE==null){
            return false;
        }
        int cardType=xmlE.getInt("type",0);
            switch (cardType){
                case 1:
                case 2:
                case 3:
                case 4:
                case 8:
                    if(legion.getPopulationNow()> legion.getPopulationMax()){return true;}else{return false;}
                case 5:
                    if(legion.varAirNum>=legion.getAirCardNum()){ return true;}else{  return false; }
                case 6:
                    if(cardId==1606){
                        if(legion.varMissileNum>=legion.getMissileCardNum()){ return true;}else{  return false; }
                    }
                    if(legion.varDefenceNum>=legion.getDefenceCardNum()){ return true;}else{  return false; }
                case 7:
                    if(legion.varNuclearNum>=legion.getNuclearCardNum()){ return true;}else{  return false; }
            }

        switch (cardId) {
            case 2001:
                if (build!=null&&build.getTradeLvNow() >= game.resGameConfig.cardUpdMax_tradeLv) {
                    return true;
                } else {
                    return false;
                }
            case 2002:
                if (build!=null&&build.getCultureLvNow() >= game.resGameConfig.cardUpdMax_cultureLv) {
                    return true;
                } else {
                    return false;
                }
            case 2003:
                if (build!=null&&build.getTransportLvNow() >= game.resGameConfig.cardUpdMax_transportLv) {
                    return true;
                } else {
                    return false;
                }
            case 2004:
                if (build!=null&&build.getTechLvNow() >= game.resGameConfig.cardUpdMax_techLv) {
                    return true;
                } else {
                    return false;
                }
            case 2005:
                if (build!=null&&build.getFoodLvNow() >= game.resGameConfig.cardUpdMax_foodLv) {
                    return true;
                } else {
                    return false;
                }
            case 2006:
                if (build!=null&&build.getEnergyLvNow() >= game.resGameConfig.cardUpdMax_eneryLv) {
                    return true;
                } else {
                    return false;
                }
            case 2007:
                if (build!=null&&build.getCityLvNow() >= game.resGameConfig.cardUpdMax_cityLv) {
                    return true;
                } else {
                    return false;
                }
            case 2008:
                if (build!=null&&build.getIndustryLvNow() >= game.resGameConfig.cardUpdMax_industryLv) {
                    return true;
                } else {
                    return false;
                }
            case 2009:
                if (build!=null&&build.getSupplyLvNow() >= game.resGameConfig.cardUpdMax_supplyLv) {
                    return true;
                } else {
                    return false;
                }
            case 2010:
                if (build!=null&&build.getDefenceLvNow() >= game.resGameConfig.cardUpdMax_defenceLv) {
                    return true;
                } else {
                    return false;
                }
            case 2011:
                if (build!=null&&build.getAirLvNow() >= game.resGameConfig.cardUpdMax_airportLv) {
                    return true;
                } else {
                    return false;
                }
            case 2012:
                if (build!=null&&build.getMissileLvNow() >= game.resGameConfig.cardUpdMax_missileLv) {
                    return true;
                } else {
                    return false;
                }
            case 2013:
                if (build!=null&&build.getNuclearLvNow() >= game.resGameConfig.cardUpdMax_nuclearLv) {
                    return true;
                } else {
                    return false;
                }
            case 2014:
                if (build!=null&&build.getArmyLvNow() >= game.resGameConfig.cardUpdMax_unitLv) {
                    return true;
                } else {
                    return false;
                }
            case 3001:
                if (legion.getCityLvMax() >= game.resGameConfig.cardUpdMax_cityLv) {
                    return true;
                } else {
                    return false;
                }
            case 3002:
                if (legion.getIndustLvMax() >= game.resGameConfig.cardUpdMax_industryLv) {
                    return true;
                } else {
                    return false;
                }
            case 3003:
                if (legion.getTechLvMax() >= game.resGameConfig.cardUpdMax_techLv) {
                    return true;
                } else {
                    return false;
                }
            case 3004:
                if (legion.getEnergyLvMax() >= game.resGameConfig.cardUpdMax_eneryLv) {
                    return true;
                } else {
                    return false;
                }
            case 3005:
                if (legion.getTransportLvMax() >= game.resGameConfig.cardUpdMax_transportLv) {
                    return true;
                } else {
                    return false;
                }
            case 3006:
                if (legion.getInfantryLvMax() >= game.resGameConfig.cardUpdMax_unitLv) {
                    return true;
                } else {
                    return false;
                }
            case 3007:
                if (legion.getCannonLvMax() >= game.resGameConfig.cardUpdMax_unitLv) {
                    return true;
                } else {
                    return false;
                }
            case 3008:
                if (legion.getTankLvMax() >= game.resGameConfig.cardUpdMax_unitLv) {
                    return true;
                } else {
                    return false;
                }
            case 3009:
                if (legion.getFortLvMax() >= game.resGameConfig.cardUpdMax_unitLv) {
                    return true;
                } else {
                    return false;
                }
            case 3010:
                if (legion.getNavyLvMax() >= game.resGameConfig.cardUpdMax_unitLv) {
                    return true;
                } else {
                    return false;
                }
            case 3011:
                if (legion.getAirLvMax() >= game.resGameConfig.cardUpdMax_unitLv) {
                    return true;
                } else {
                    return false;
                }
            case 3012:
                if (legion.getSupplyLvMax() >= game.resGameConfig.cardUpdMax_supplyLv) {
                    return true;
                } else {
                    return false;
                }
            case 3013:
                if (legion.getDefenceLvMax() >= game.resGameConfig.cardUpdMax_defenceLv) {
                    return true;
                } else {
                    return false;
                }
            case 3014:
                if (legion.getMissileLvMax() >= game.resGameConfig.cardUpdMax_missileLv) {
                    return true;
                } else {
                    return false;
                }
            case 3015:
                if (legion.getNuclearLvMax() >= game.resGameConfig.cardUpdMax_nuclearLv) {
                    return true;
                } else {
                    return false;
                }
            case 3016:
                if (legion.getFinancialLvMax() >= game.resGameConfig.cardUpdMax_financialLv) {
                    return true;
                } else {
                    return false;
                }
            case 3017:
                if (legion.getTradeLvMax() >= game.resGameConfig.cardUpdMax_tradeLv) {
                    return true;
                } else {
                    return false;
                }
            case 3018:
                if (legion.getCultureLvMax() >= game.resGameConfig.cardUpdMax_cultureLv) {
                    return true;
                } else {
                    return false;
                }
            case 3019:
                if (legion.getMiracleNow() >= game.resGameConfig.cardUpdMax_miracle) {
                    return true;
                } else {
                    return false;
                }
            case 3020:
                if (legion.getInfantryCardMax() >= game.resGameConfig.cardUpdMax_infantryCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3021:
                if (legion.getArmorCardMax() >= game.resGameConfig.cardUpdMax_armorCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3022:
                if (legion.getArtilleryCardMax() >= game.resGameConfig.cardUpdMax_artilleryCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3023:
                if (legion.getNavyCardMax() >= game.resGameConfig.cardUpdMax_navyCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3024:
                if (legion.getAirCardMax() >= game.resGameConfig.cardUpdMax_airCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3025:
                if (legion.getNuclearCardMax() >= game.resGameConfig.cardUpdMax_nuclearCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3026:
                if (legion.getMissileCardMax() >= game.resGameConfig.cardUpdMax_missileCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3027:
                if (legion.getSubmarineCardMax() >= game.resGameConfig.cardUpdMax_submarineCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3028:
                if (legion.getDefenceCardMax() >= game.resGameConfig.cardUpdMax_defenceCardLv) {
                    return true;
                } else {
                    return false;
                }
            case 3029:
                if (legion.getGeneralCardMax() >= game.resGameConfig.cardUpdMax_generalCardLv) {
                    return true;
                } else {
                    return false;
                }

            case 3030:
                if (legion.getMilitaryAcademyLv() >= game.resGameConfig.cardUpdMax_militaryAcademyLv) {
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }



    public static  Array<XmlReader.Element> getRandCardE(Array<XmlReader.Element> cardE,int num,int round){
        if(cardE.size<=num){
            return cardE;
        }
        round=(round%(cardE.size/num))*num;
        /*while(cardE.size!=num){
            int i=(cardE.size-1)%round;
            //Gdx.app.log("","r:"+round+" size:"+cardE.size+" i:"+i);
            cardE.removeIndex(i);
        }*/


        for(int i=cardE.size-1;i>=0;i--){
            if(i>=round&&i<round+num){
                // Gdx.app.log("","r:"+round+" size:"+cardE.size+" n:"+num+" i:"+i);
            }else{
                cardE.removeIndex(i);
            }
        }

        return cardE;
    }
    //entergy-food:当好感度处于两者之间时才出现该卡牌   entergy<card<=food
    public static  Array<XmlReader.Element> getForeignCardE(Fb2Smap smapDAO, Fb2Smap.LegionData legionDataA,Fb2Smap.LegionData legionDataB, Array<XmlReader.Element> filterCardE, Array<XmlReader.Element> rs){


        if(rs==null){
            rs= new Array<>();
        }else{
            rs.clear();
        }
        if(legionDataA.getLegionIndex()==0||legionDataB.getLegionIndex()==0){
            return rs;
        }
        // XmlReader.Element root = ResConfig.gameConfig.getDEF_CARD().e;
        Fb2Smap.ForeignData f=smapDAO.getForeignData(legionDataA.getLegionIndex(),legionDataB.getLegionIndex());
        int foreignType=smapDAO.getForeignType(legionDataA,legionDataB,f.getFavorValue());
        int favor;
        for(int i=0;i<filterCardE.size;i++){
            XmlReader.Element xmlE=filterCardE.get(i);
            if(xmlE.getInt("type")==12&&ComUtil.ifValueBetween( f.getFavorValue(),xmlE.getInt("min"), xmlE.getInt("max"),true)  ){
                rs.add(xmlE);
            }
        }
        if(rs.size<=0){
            Gdx.app.error("getForeignCardE","cardType:"+12);
        }
        // Gdx.app.log("getCard","size:"+rs.size);
        return rs;
    }

    public static  Array<XmlReader.Element> getRandCardE(MainGame game, Fb2Smap.LegionData l, Fb2Smap.BuildData b,IntArray canTechCardEIds, int num, int round, Array<XmlReader.Element> cardE,boolean showCardIfMax){
        if(cardE==null){
            cardE=new Array<>();
        }else{
            cardE.clear();
        }
        if(canTechCardEIds.size<=num){
            for(int i=0;i<canTechCardEIds.size;i++){
                XmlReader.Element xmlE=game.gameConfig.getDEF_CARD().getElementById(canTechCardEIds.get(i));
                if(xmlE!=null){
                    int id=xmlE.getInt("id");
                    if(!showCardIfMax&&cardIfMax(game,l,b,id)){
                        continue;
                    }
                    cardE.add(xmlE);
                }
            }
            return cardE;
        }
        round=(round%(canTechCardEIds.size/num))*num;
        /*while(cardE.size!=num){
            int i=(cardE.size-1)%round;
            //Gdx.app.log("","r:"+round+" size:"+cardE.size+" i:"+i);
            cardE.removeIndex(i);
        }*/

        for(int i=round,iMax=num+round;i<iMax;i++){
            // Gdx.app.log("","r:"+round+" size:"+cardE.size+" n:"+num+" i:"+i);
            XmlReader.Element xmlE=game.gameConfig.getDEF_CARD().getElementById(canTechCardEIds.get(i));
            if(xmlE!=null){
                cardE.add(xmlE);
            }
        }
        return cardE;
    }



    public static int getMaxLvArmyId(Fb2Smap.LegionData l,IntArray armyIds){
        int maxTechArmyId=0;
        int maxTechLv=0;
        for(int i=0;i<armyIds.size;i++){
            int armyId=armyIds.get(i);
            if(getTechLvByArmyId(l,i)>maxTechLv){
                maxTechArmyId=i;
                maxTechLv=getTechLvByArmyId(l,i);
            }
        }
        if(maxTechArmyId==0){
            return armyIds.random();
        }
        return maxTechArmyId;
    }


    public static int getTechLvByArmyId(Fb2Smap.LegionData l,int armyId){
        switch (armyId){
            case 1101: return l.getInfantryLvMax();
            case 1102: return l.getInfantryLvMax();
            case 1103: return l.getInfantryLvMax();
            case 1104: return l.getInfantryLvMax();
            case 1107: return l.getInfantryLvMax();
            case 1201: return l.getCannonLvMax();
            case 1202: return l.getCannonLvMax();
            case 1203: return l.getCannonLvMax();
            case 1204: return l.getCannonLvMax();
            case 1205: return l.getCannonLvMax();
            case 1206: return l.getCannonLvMax();
            case 1207: return l.getCannonLvMax();
            case 1301: return l.getTankLvMax();
            case 1302: return l.getTankLvMax();
            case 1303: return l.getTankLvMax();
            case 1304: return l.getTankLvMax();
            case 1305: return l.getTankLvMax();
            case 1306: return l.getTankLvMax();
            case 1400: return l.getNavyLvMax();
            case 1401: return l.getNavyLvMax();
            case 1402: return l.getNavyLvMax();
            case 1403: return l.getNavyLvMax();
            case 1404: return l.getNavyLvMax();
            case 1405: return l.getNavyLvMax();
            case 1406: return l.getNavyLvMax();
            case 1407: return l.getNavyLvMax();
            case 1408: return l.getNavyLvMax();
            case 1501: return l.getAirLvMax();
            case 1502: return l.getAirLvMax();
            case 1503: return l.getAirLvMax();
            case 1504: return l.getAirLvMax();
            case 1505: return l.getAirLvMax();
            case 1506: return l.getAirLvMax();
            case 1507: return l.getAirLvMax();
            case 1601: return l.getNavyLvMax();
            case 1602: return l.getNavyLvMax();
            case 1603: return l.getNavyLvMax();
            case 1604: return l.getNavyLvMax();
            case 1605: return l.getNavyLvMax();
            case 1606: return l.getNavyLvMax();
            case 1701: return l.getNuclearLvMax();
            case 1702: return l.getNuclearLvMax();
            case 1703: return l.getNuclearLvMax();
            case 1704: return l.getNuclearLvMax();
            case 1705: return l.getNuclearLvMax();
            case 1801: return l.getNavyLvMax();
            case 1802: return l.getNavyLvMax();
            case 1803: return l.getNavyLvMax();
            case 1804: return l.getNavyLvMax();
        }
        return -1;
    }

    public static int getTechNumIdByArmyId(MainGame game,int armyId){
        int type=game.gameConfig.getDEF_ARMY().getElementById(armyId).getInt("type");
        switch (type){
            case 1: return 3020;
            case 2: return 3022;
            case 3: return 3021;
            case 4: return 3023;
            case 5: return 3024;
            case 6: return 3028;
            case 7: return 3025;
            case 8: return 3027;
        }
        return -1;
    }

    //根据军团的卡库来筛选出可用的军团卡牌

    public static void resetLegionCanBuildCardE(Fb2Smap.LegionData l, Array<XmlReader.Element> publicCardE){
        if(l.isPlayer()){
            int p=0;
        }
        if(l.varLegionCanBuildCardE==null) {
            l.varLegionCanBuildCardE=new Array<XmlReader.Element>();
        }else {
            l.varLegionCanBuildCardE.clear();
        }
        for(XmlReader.Element cardE:publicCardE ){
            String useCountrys=cardE.get("useCountrys","-1");
            if(!useCountrys.equals("-1")&&!ComUtil.ifHaveValueInStr(useCountrys,l.getCountryId())) {
                continue;
            }

            int cardType=cardE.getInt("type");
            switch (cardType){
                case 5:if(l.varAirNum<l.getAirCardNum()){
                    l.varLegionCanBuildCardE.add(cardE);
                }break;
                case 6:if(cardE.getInt("id")==1606){
                    if(l.varMissileNum<l.getMissileCardNum()) {
                        l.varLegionCanBuildCardE.add(cardE);
                    }
                }else {
                    if(l.varDefenceNum<l.getDefenceCardNum()) {
                        l.varLegionCanBuildCardE.add(cardE);
                    }
                }
                    break;
                case 7:if(l.varNuclearNum<l.getNuclearCardNum()){
                    l.varLegionCanBuildCardE.add(cardE);
                }break;

               /* case 1:if(l.varInfantryNum<l.getInfantryCardNum()){
                    l.varLegionCanBuildCardE.add(cardE);
                }break;
                case 2:if(l.varArtilleryNum<l.getArtilleryCardNum()){
                    l.varLegionCanBuildCardE.add(cardE);
                }break;
                case 3:if(l.varArmorNum<l.getArmorCardNum()){
                    l.varLegionCanBuildCardE.add(cardE);
                }break;
                case 4:if(l.varNavyNum<l.getNavyCardNum()){
                    l.varLegionCanBuildCardE.add(cardE);
                }break;


                case 8:if(l.varSubmarineNum<l.getSubmarineCardNum()){
                    l.varLegionCanBuildCardE.add(cardE);
                }break;*/
                case 1:
                case 2:
                case 3:
                case 4:
                case 8:
                    if(l.getPopulationNow()<l.getPopulationMax()){   l.varLegionCanBuildCardE.add(cardE);   }
                    break;
                default:
                    l.varLegionCanBuildCardE.add(cardE);
            }
                /*if(rsE.size==0){
                    Gdx.app.error("cardE",l.getBuildPolicy()+" size is 0,cardId"+cardE.getInt("id"));
                }*/
        }

    }

    //这个把过滤后的存入rs结果集
    public static Array<XmlReader.Element> resetLegionCanBuildCardE(Fb2Smap.LegionData l, Array<XmlReader.Element> filterCardE, Array<XmlReader.Element> rsE){
        if(rsE==null){
            rsE=new Array<>();
        }else {
            rsE.clear();
        }

        for(XmlReader.Element cardE:filterCardE ){
            /*switch (cardE.getInt("type")){
                case 1:if(l.varInfantryNum<l.getInfantryCardNum()){
                    rsE.add(cardE);
                }break;
                case 2:if(l.varArtilleryNum<l.getArtilleryCardNum()){
                    rsE.add(cardE);
                }break;
                case 3:if(l.varArmorNum<l.getArmorCardNum()){
                    rsE.add(cardE);
                }break;
                case 4:if(l.varNavyNum<l.getNavyCardNum()){
                    rsE.add(cardE);
                }break;
                case 5:if(l.varAirNum<l.getAirCardNum()){
                    rsE.add(cardE);
                }break;
                case 6:if(cardE.getInt("id")==1606){
                    if(l.varMissileNum<l.getMissileCardNum()) {
                        rsE.add(cardE);
                    }
                }else {
                    if(l.varDefenceNum<l.getDefenceCardNum()) {
                        rsE.add(cardE);
                    }
                }
                case 7:if(l.varNuclearNum<l.getNuclearCardNum()){
                    rsE.add(cardE);
                }break;
                case 8:if(l.varSubmarineNum<l.getSubmarineCardNum()){
                    rsE.add(cardE);
                }break;
               // default: rsE.add(cardE);
            }*/
            switch (cardE.getInt("type")){
                case 5:if(l.varAirNum<l.getAirCardNum()){
                    rsE.add(cardE);
                }break;
                case 6:if(cardE.getInt("id")==1606){
                    if(l.varMissileNum<l.getMissileCardNum()) {
                        rsE.add(cardE);
                    }
                }else {
                    if(l.varDefenceNum<l.getDefenceCardNum()) {
                        rsE.add(cardE);
                    }
                }
                    break;
                case 7:
                    if(l.varNuclearNum<l.getNuclearCardNum()) {
                        rsE.add(cardE);
                    }
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 8:
                    if(l.getPopulationNow()<l.getPopulationMax()){   rsE.add(cardE); }
                    break;
                default:
                    rsE.add(cardE);
            }
        }
        return  rsE;
    }

    //page 从1开始
    public static Array<XmlReader.Element> getElementByPage(Array<XmlReader.Element> rs,int page, int pageCount) {
        int index=(page-1)*pageCount;
        //1 删除该标号之前的
        if(rs.size>index){
            for(int i=index-1;i>0;i--){
                rs.removeIndex(i);
            }
        }
        //2 删除该标号之后的
        if(pageCount<rs.size){
            for(int i=rs.size-1;i>pageCount;i--){
                rs.removeIndex(i);
            }
        }
        //rs.removeValue(null,true);
        return rs;
    }

    public static Array<XmlReader.Element> getElementByPage(Array<XmlReader.Element> cardEs,Array<XmlReader.Element> rs,int page, int pageCount) {
        if(rs==null){
            rs=new Array<>();
        }else {
            rs.clear();
        }

        int index=(page-1)*pageCount;
        for(int iMax=pageCount+index;index<iMax;index++){
            if(index<cardEs.size){
                rs.add(cardEs.get(index));
            }else {
                break;
            }
        }
        //rs.removeValue(null,true);
        return rs;
    }

    public  float getRateByCardType(Fb2Smap.ForeignData f, Fb2Smap.BuildData b, int cardType, Fb2Smap.LegionData l) {

        switch (cardType){
            case 1:return l.getInfantryCardMax()*1f/game.resGameConfig.cardUpdMax_infantryCardLv;
            case 2:return l.getArtilleryCardMax()*1f/game.resGameConfig.cardUpdMax_artilleryCardLv;
            case 3:return l.getArmorCardMax()*1f/game.resGameConfig.cardUpdMax_armorCardLv;
            case 4:return l.getNavyCardMax()*1f/game.resGameConfig.cardUpdMax_navyCardLv;
            case 5:return l.getAirCardMax()*1f/game.resGameConfig.cardUpdMax_airCardLv;
            case 6:return l.getGeneralCardMax()*1f/game.resGameConfig.cardUpdMax_generalCardLv;
            case 7:return l.getNuclearCardMax()*1f/l.getNuclearCardNum();
            case 8:return l.getSubmarineCardMax()*1f/game.resGameConfig.cardUpdMax_submarineCardLv;
            case 9:return b.getCityHpNow()*1f/b.getCityHpMax();
            case 10:return b.getCityHpNow()*1f/b.getCityHpMax();
            case 11:return l.getTradeCount()*1f/(1+l.getFinancialLvMax());
            case 12:return f.getFavorValue()*1f/100;
            default: return 1;
        }
    }

    public static int getForeignCardChance(int cardId, int chance,int AStability ,int BStability) {
        int v=chance;
        if(chance==100){
            return 100;
        }
        switch (cardId){
            case 3101:v= chance+ComUtil.limitValue((AStability-BStability)/2,-chance,chance  );break;
            case 3102:v= chance+ComUtil.limitValue((BStability-AStability),-chance,chance  );break;
            case 3103:v= chance+ComUtil.limitValue((BStability-AStability)/3,-chance,chance  );break;
            case 3104:v= chance+ComUtil.limitValue((AStability-BStability),-chance,chance  );break;
            case 3105:v= chance+ComUtil.limitValue(Math.abs(AStability-BStability)/2,0,chance  );break;
            case 3106:v= chance+ComUtil.limitValue(Math.abs(AStability-BStability)/2,0,chance  );break;
            case 3107:v= chance+ComUtil.limitValue(Math.abs(AStability-BStability)/2,0,chance  );break;
            case 3108:v= chance+ComUtil.limitValue((AStability-BStability)/2,-chance,chance  );break;
            case 3109:v= chance+ComUtil.limitValue((AStability-BStability)/3,-chance,chance  );break;
            case 3110:v= chance+ComUtil.limitValue((AStability-BStability)/2,-chance,chance  );break;
            case 3111:v= chance+ComUtil.limitValue((AStability-BStability)/2,-chance,chance  );break;
            case 3112:v= chance+ComUtil.limitValue((AStability-BStability)/2,-chance,chance  );break;
            case 3113:v= chance+ComUtil.limitValue((AStability-BStability)/3,-chance,chance  );break;
        }
        if(v<0){
            v=1;
        }else if(v>=100){
            v=100;
        }
        return v;
    }


    public static void reduceTechLv(Fb2Smap.LegionData l, Fb2Smap.BuildData build, int cardId){

        switch (cardId) {
            //贸易
            case 2001:
                if (build == null) {
                    return;
                }
                build.reduceTradeLvNow();
                break;
            //文化
            case 2002:
                if (build == null) {
                    return;
                }
                build.reduceCultureLvNow();
                break;
            //交通
            case 2003:
                if (build == null) {
                    return;
                }
                build.reduceTransportLvNow();
                break;
            //学校
            case 2004:
                if (build == null) {
                    return;
                }
                build.reduceTechLvNow();
                break;
            //原料
            case 2005:
                if (build == null) {
                    return;
                }
                build.reduceMateriLvNow();
                break;
            //能源
            case 2006:
                if (build == null) {
                    return;
                }
                build.reduceEnergyLvNow();
                break;
            //城市
            case 2007:
                if (build == null) {
                    return;
                }
                build.reduceCityLvNow();
                break;
            //工业
            case 2008:
                if (build == null) {
                    return;
                }
                build.reduceIndustLvNow();
                break;
            //补给
            case 2009:
                if (build == null) {
                    return;
                }
                build.reduceSupplyLvNow();
                break;
            //城防
            case 2010:
                if (build == null) {
                    return;
                }
                build.reduceDefenceLvNow();
                break;
            //飞机厂
            case 2011:
                if (build == null) {
                    return;
                }
                build.reduceAirLvNow();
                break;
            //防空/飞弹
            case 2012:
                if (build == null) {
                    return;
                }
                build.reduceMissileLvNow();
                break;
            //秘密武器
            case 2013:
                if (build == null) {
                    return;
                }
                build.reduceNuclearLvNow();
                break;
            //军营
            case 2014:
                if (build == null) {
                    return;
                }
                build.reduceArmyLvNow();
                break;
            //城市可提升等级
            case 3001:
                l.reduceCityLvMax();
                break;
            //工业可提升等级
            case 3002:
                l.reduceIndustLvMax();
                break;
            //研究可提升等级
            case 3003:
                l.reduceTechLvMax();
                break;
            //能源可提升等级
            case 3004:
                l.reduceEnergyLvMax();
                break;
            //交通可提升等级
            case 3005:
                l.reduceTransportLvMax();
                break;
            //建造士兵军衔上限
            case 3006:
                l.reduceInfantryLvMax();
                break;
            //建造炮兵军衔上限
            case 3007:
                l.reduceCannonLvMax();
                break;
            //建造坦克军衔上限
            case 3008:
                l.reduceTankLvMax();
                break;
            //建造要塞军衔上限
            case 3009:
                l.reduceFortLvMax();
                break;
            //建造海军军衔上限
            case 3010:
                l.reduceNavyLvMax();
                break;
            //建造空军军衔上限
            case 3011:
                l.reduceAirLvMax();
                break;
            //城市升级补给上限
            case 3012:
                l.reduceSupplyLvMax();
                break;
            //城市城防升级上限
            case 3013:
                l.reduceDefenceLvMax();
                break;
            //城市导弹升级上限
            case 3014:
                l.reduceMissileLvMax();
                break;
            //城市超武升级上限
            case 3015:
                l.reduceNuclearLvMax();
                break;
            //城市贸易科技上限
            case 3016:
                l.reduceFinancialLvMax();
                break;
            //城市贸易规模上限
            case 3017:
                l.reduceTradeLvMax();
                break;
            //城市文化科技上限
            case 3018:
                l.reduceCultureLvMax();
                break;
            //航天科技
            case 3019:
                l.reduceMiracleLvNow();
                break;
            //步兵卡库
            case 3020:
                l.reduceInfantryCardMax();
                break;
            //装甲卡库
            case 3021:
                l.reduceArmorCardMax();
                break;
            //火炮卡库
            case 3022:
                l.reduceArtilleryCardMax();
                break;
            //海军卡库
            case 3023:
                l.reduceNavyCardMax();
                break;
            //空军卡库
            case 3024:
                l.reduceAirCardMax();
                break;
            //超武卡库
            case 3025:
                l.reduceNuclearCardMax();
                break;
            //导弹卡库
            case 3026:
                l.reduceMissileCardMax();
                break;
            //潜艇卡库
            case 3027:
                l.reduceSubmarineCardMax();
                break;
            //战术卡库
            case 3028:
                l.reduceDefenceCardMax();
                break;
            //将领卡库
            case 3029:
                l.reduceGeneralCardMax();
                break;
            //刷新数量
            case 3030:
                l.reduceMilitaryAcademyLv();
                break;
        }
    }

    public int getEnergyLvToAfford(int energyCost) {
        return (energyCost/game.resGameConfig.addEnergyEachEnergyLv +1);
    }

    //获得国家属性的效果值
    public int getLegionFeatureValue (int id,int lv){
        XmlReader.Element xl=  game.gameConfig.getDEF_LEGIONFEATURE().getElementById(id);
        if(xl!=null){
            return xl.getInt("effectValue")*lv;
        }
        return 0;
    }

    //根据兵种_国家 获取兵模名
    public  String getModelName(int armyId,int model){
        XmlStringDAO dao=game.gameConfig.getDEF_MODEL();
        String armyId_countryId=new StringBuilder().append(armyId).append("_").append(model).toString();
        String defaultModel=new StringBuilder().append(armyId).append("_").append(0).toString();

        if(dao.contain(armyId_countryId)){
            return dao.getElementById(armyId_countryId).get("imgName");
        }else if(dao.contain(defaultModel)){
            return dao.getElementById(defaultModel).get("imgName");
        }else{
            return ResDefaultConfig.Image.MISS_REMARK;
        }
    }
    //根据年份获取age
    public  int getAgeByYear(int year){
        Array<XmlReader.Element> xmlFiles= game.gameConfig.getDEF_AGE().e.getChildrenByName("age");
        int age=0;
        for(int i=0;i<xmlFiles.size;i++){
            if(year>=xmlFiles.get(i).getInt("beginYear")){
                age=xmlFiles.get(i).getInt("id");
            }else{
                return age;
            }
        }
        return age;
    }

    //获取font TODO 后续补充
    public BitmapFont getFont(String fontName){
        switch (fontName){
            case "default": return game.defaultFont;
            case "textFont": return game.gameConfig.gameFont;
        }

        return game.defaultFont;
    }

    //根据legion判断其可以升级的科技 cardIds 只获得cardtype=11的
    public  IntArray getCanUpdTech(Array<XmlReader.Element> xmlFiles,int age, IntArray cardIds){
        if(cardIds==null){
            cardIds=new IntArray();
        }else{
            cardIds.clear();
        }
        //  Array<XmlReader.Element> xmlFiles =game.gameConfig.getDEF_CARD().e.getChildrenByName("card");
        for(XmlReader.Element c:xmlFiles  ){
            if(c.getInt("type")==11&&age>=c.getInt("age")){
                cardIds.add(c.getInt("id"));
            }
        }
        return cardIds;
    }

    public  int getWeaponValueByArmyId(int armyId,int targetArmyId,int weaponLv){
        if(armyId>0&&targetArmyId>0){
            XmlReader.Element xmlE1=game.gameConfig.getDEF_ARMY().getElementById(armyId);
            XmlReader.Element xmlE2=game.gameConfig.getDEF_ARMY().getElementById(targetArmyId);
            if(xmlE1!=null&&xmlE2!=null){
                return getWeaponValue(xmlE1.getInt("weapon",101),xmlE1.getInt("type",1),weaponLv);
            }
        }
        return 0;
    }


    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
    public  int getWeaponValue(int weaponId,int targetType,int weaponLv){
        XmlReader.Element armyE=game.gameConfig.getDEF_WEAPON().getElementById(weaponId);
        if(armyE!=null){
            switch (targetType){
                case 1:return (int) ((armyE.getInt("infantryBs")+armyE.getInt("infantryBo")*weaponLv)* game.resGameConfig.weaponEffectRate);
                case 2:return (int) ((armyE.getInt("artilleryBs")+armyE.getInt("artilleryBo")*weaponLv)* game.resGameConfig.weaponEffectRate);
                case 3:return (int) ((armyE.getInt("armourBs")+armyE.getInt("armourBo")*weaponLv)* game.resGameConfig.weaponEffectRate);
                case 4:return (int) ((armyE.getInt("shipBs")+armyE.getInt("shipBo")*weaponLv)* game.resGameConfig.weaponEffectRate);
                case 5:return (int) ((armyE.getInt("airBs")+armyE.getInt("airBo")*weaponLv)* game.resGameConfig.weaponEffectRate);
                case 6:return (int) ((armyE.getInt("fortBs")+armyE.getInt("fortBo")*weaponLv)* game.resGameConfig.weaponEffectRate);
                case 7:return (int) ((armyE.getInt("specialBs")+armyE.getInt("specialBs")*weaponLv)* game.resGameConfig.weaponEffectRate);
                case 8:return (int) ((armyE.getInt("submarineBs")+armyE.getInt("submarineBo")*weaponLv)* game.resGameConfig.weaponEffectRate);
            }
        }
        return 0;
    }

    public float getWeaponValue(Fb2Smap.ArmyData army,int direct, int targetType) {
        if(army.isUnitGroup()){
            int potion1= getArmyFomationPotion(army,direct,0);
            int potion2= getArmyFomationPotion(army,direct,1);
            Fb2Smap.LegionData l=army.getLegionData();
            float rs=0;int i=0;
            XmlReader.Element xmlE1=army.getArmyXmlE(potion1,false);
            if(xmlE1!=null){
                rs= getWeaponValue(xmlE1.getInt("weapon",101),targetType,ComUtil.min(army.getUnitGroupWealLv(potion1)+army.getUnitGroupSameArmyIdCount(potion1),l.getCardTechLv(xmlE1.getInt("id"),xmlE1.getInt("type"))));
                i++;
            }
            XmlReader.Element xmlE2=army.getArmyXmlE(potion2,false);
            if(xmlE2!=null){
                rs+= getWeaponValue(xmlE2.getInt("weapon",101),targetType,ComUtil.min(army.getUnitGroupWealLv(potion2)+army.getUnitGroupSameArmyIdCount(potion2),l.getCardTechLv(xmlE2.getInt("id"),xmlE2.getInt("type"))));
                i++;
            }

            if(i==0){
                XmlReader.Element xmlE=army.getArmyXmlE(0,false);
                if(xmlE!=null){
                    return getWeaponValue(xmlE.getInt("weapon",101),targetType,ComUtil.min(army.getUnitWealv0()+army.getUnitGroupSameArmyIdCount(0),l.getCardTechLv(xmlE.getInt("id"),xmlE.getInt("type"))));
                }
                return 0;
            }else {
                return rs/i;
            }
        }else {
            XmlReader.Element xmlE=army.getArmyXmlE(0,false);
            if(xmlE!=null){
                return getWeaponValue(xmlE.getInt("weapon",101),targetType,army.getUnitWealv0());
            }
            return 0;
        }
    }

    public int getArmyFomationPotion(Fb2Smap.ArmyData armyData, int direct, int i) {
        int armyId=0;
        switch (direct){
            case 1:
                if(i==0){
                    return 1;
                }else{
                    return 2;
                }
            case 2:
                if(i==0){
                    return 2;
                }else{
                    return 3;
                }
            case 3:
                if(i==0){
                    return 3;
                }else{
                    return 4;
                }
            case 4:
                if(i==0){
                    return 5;
                }else{
                    return 1;
                }
            case 5:
                if(i==0){
                    return 6;
                }else{
                    return 5;
                }
            case 6:
                if(i==0){
                    return 4;
                }else{
                    return 6;
                }
            default:
                return 0;
        }



    }


    // 获得提示详情  参考 https://www.huangyunkun.com/2014/05/29/libgdx-i18n/
    public String getPromptStrT(int v, int type, Object... args){
        String key=null;
        //type 0标题,1 详情 2 效果
        switch (type){
            case 0:key="prompt_title_"+v;break;
            case 1:key="prompt_detail_"+v;break;
            case 2:key="prompt_effect_"+v;break;
        }
        I18NBundle bundle=game.gameConfig.lm.getBundle();
        for (int i = 0; i < args.length; i++){
            if(!ComUtil.isNumeric(args[i].toString())){
                try {
                    args[i]=bundle.get(args[i].toString());
                }catch (Exception e){
                    args[i]=args[i].toString();
                }
            }
        }
        return replaceShieldStr(bundle.format(key, args));
    }

    public String getPromptStr(int v, int type, Object... args){
        String key=null;
        //type 0标题,1 详情 2 效果
        switch (type){
            case 0:key="prompt_title_"+v;break;
            case 1:key="prompt_detail_"+v;break;
            case 2:key="prompt_effect_"+v;break;
        }
       /* I18NBundle bundle=game.gameConfig.lm.getBundle();
        for (int i = 0; i < args.length; i++){
            if(!ComUtil.isNumeric(args[i].toString())){
                try {
                    args[i]=bundle.get(args[i].toString());
                }catch (Exception e){
                    args[i]=args[i].toString();
                }
            }
        }*/
        return getStrValueT(key,args);
    }


    public String getStrValueT(String key){
        if(key==null||key.equals("")){
            return "";
        }


        String[] rs=key.split(",");
        I18NBundle lm=game.gameConfig.lm.getBundle();
        if(rs.length==0){
            return lm.get(key);
        }

        java.lang.StringBuilder b=new java.lang.StringBuilder();
        for(int i=0,iMax=rs.length;i<iMax;i++){
            try {
                b.append(lm.get(rs[i])).append(",");
            } catch (Exception e) {
                b.append(rs[i]).append(",");
            }
        }
        if(b.length()>1){
            b.deleteCharAt(b.length()-1);
        }
        return replaceShieldStr(b.toString());
    }

    public String replaceShieldStr(String str){
        if(!game.gameConfig.ifShield||shieldStrMap==null||ComUtil.isEmpty(str)){
            return str;
        }
        /**/shieldStrMap.iterator();
        Iterator<ObjectMap.Entry<String,String>> it = shieldStrMap.iterator();
        while (it.hasNext()) {
           ObjectMap.Entry<String,String> c = it.next();
           if(str.indexOf(c.key)!=-1){
               str= str.replace(c.key,c.value);
           }
        }
        return str;
    }


    /*public  String getStrValueNoFormat(String key,Object... args){
        I18NBundle bundle=game.gameConfig.lm.getBundle();
        return bundle.format(key, args);
    }*/




    public String getStrValueT(String key, Object... args){
        if(key==null){
            Gdx.app.error("getStrValue has error",key+"");
        }
        I18NBundle bundle=game.gameConfig.lm.getBundle();
        String s;
        if(args!=null){
            for (int i = 0; i < args.length; i++){
                if(args[i]==null){
                    s="";
                }else {
                    s=args[i].toString();
                }
                if(!ComUtil.isNumeric(s)&&!ComUtil.isEmpty(s)){
                    try {
                        args[i]=bundle.get(args[i].toString());
                    }catch (Exception e){
                        args[i]=args[i].toString();
                        if(ResDefaultConfig.ifDebug){
                            Gdx.app.error("getStrValueT no str",args[i]+"");
                        }
                    }
                }
            }
            try{
                return   replaceShieldStr(bundle.format(key, args));
            }catch (Exception e){
                Gdx.app.error("error getStrValueT",key);
                if(ResDefaultConfig.ifDebug){
                    e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                }else if(!game.gameConfig.getIfIgnoreBug()){
                    game.remindBugFeedBack();
                }
                game.recordLog("GameMethod getStrValueT ",e);
                return "error str for"+key;
            }
        }else {
            return replaceShieldStr(bundle.format(key));
        }
    }
    public String getStrValueAndHaveDefault(String key,String defaultStr, Object... args){
        if(key==null){
            Gdx.app.error("getStrValue has error",key+"");
        }
        I18NBundle bundle=game.gameConfig.lm.getBundle();
        try {
            if(args!=null){
                return replaceShieldStr(bundle.format(key, args));
            }else {
                return replaceShieldStr(bundle.format(key));
            }
        }catch (Exception e) {
            return defaultStr;
        }
    }
    public String getStrValue(String key, Object... args){
        if(key==null){
            Gdx.app.error("getStrValue has error",key+"");
        }
        I18NBundle bundle=game.gameConfig.lm.getBundle();
        try {
            if(args!=null){
                return replaceShieldStr(bundle.format(key, args));
            }else {
                return replaceShieldStr(bundle.format(key));
            }
        }catch (Exception e) {
            if(ResDefaultConfig.ifDebug){
                return "error strValue:"+key;
            }
            return key;
        }
    }

    public String getCardName( Fb2Smap.LegionData l, Fb2Smap.BuildData b,int cardId,int cardType) {
        if(cardType==17){
            return getStrValue("buildwonder_name_"+cardId);
        }else if(cardType==19){
            return getStrValue("spiritEffectName_"+cardId);
        }
        switch (cardId){
            case 2001:return getStrValueT("card_name_"+cardId,b.getTradeLvNow(),b.canUpdTradeLv());
            case 2002:return getStrValueT("card_name_"+cardId,b.getCultureLvNow(),b.canUpdCultureLv());
            case 2003:return getStrValueT("card_name_"+cardId,b.getTransportLvNow(),b.canUpdTransportLv());
            case 2004:return getStrValueT("card_name_"+cardId,b.getTechLvNow(),b.canUpdTechLv());//
            case 2005:return getStrValueT("card_name_"+cardId,b.getFoodLvNow(),b.canUpdFoodLv());
            case 2006:return getStrValueT("card_name_"+cardId,b.getEnergyLvNow(),b.canUpdEnergyLv());
            case 2007:return getStrValueT("card_name_"+cardId,b.getCityLvNow(),b.canUpdCityLv());
            case 2008:return getStrValueT("card_name_"+cardId,b.getIndustryLvNow(),b.canUpdIndustryLv());
            case 2009:return getStrValueT("card_name_"+cardId,b.getSupplyLvNow(),b.canUpdSupplyLv());
            case 2010:return getStrValueT("card_name_"+cardId,b.getDefenceLvNow(),b.canUpdDefenceLv());
            case 2011:return getStrValueT("card_name_"+cardId,b.getAirLvNow(),b.canUpdAirLv());
            case 2012:return getStrValueT("card_name_"+cardId,b.getMissileLvNow(),b.canUpdMissileLv());
            case 2013:return getStrValueT("card_name_"+cardId,b.getNuclearLvNow(),b.canUpdNuclearLv());
            case 2014:return getStrValueT("card_name_"+cardId,b.getArmyLvNow(),b.canUpdArmyLv());
            case 3001:return getStrValueT("card_name_"+cardId,l.getCityLvMax(), game.resGameConfig.cardUpdMax_cityLv);
            case 3002:return getStrValueT("card_name_"+cardId,l.getIndustLvMax(), game.resGameConfig.cardUpdMax_industryLv);
            case 3003:return getStrValueT("card_name_"+cardId,l.getTechLvMax(), game.resGameConfig.cardUpdMax_techLv);
            case 3004:return getStrValueT("card_name_"+cardId,l.getEnergyLvMax(), game.resGameConfig.cardUpdMax_eneryLv);
            case 3005:return getStrValueT("card_name_"+cardId,l.getTransportLvMax(), game.resGameConfig.cardUpdMax_transportLv);
            case 3006:return getStrValueT("card_name_"+cardId,l.getInfantryLvMax(), game.resGameConfig.cardUpdMax_unitLv);
            case 3007:return getStrValueT("card_name_"+cardId,l.getCannonLvMax(), game.resGameConfig.cardUpdMax_unitLv);
            case 3008:return getStrValueT("card_name_"+cardId,l.getTankLvMax(), game.resGameConfig.cardUpdMax_unitLv);
            case 3009:return getStrValueT("card_name_"+cardId,l.getFortLvMax(), game.resGameConfig.cardUpdMax_unitLv);
            case 3010:return getStrValueT("card_name_"+cardId,l.getNavyLvMax(), game.resGameConfig.cardUpdMax_unitLv);
            case 3011:return getStrValueT("card_name_"+cardId,l.getAirLvMax(), game.resGameConfig.cardUpdMax_unitLv);
            case 3012:return getStrValueT("card_name_"+cardId,l.getSupplyLvMax(), game.resGameConfig.cardUpdMax_supplyLv);
            case 3013:return getStrValueT("card_name_"+cardId,l.getDefenceLvMax(), game.resGameConfig.cardUpdMax_defenceLv);
            case 3014:
                if(l.getAge()<2){
                    return getStrValueT("card_name_"+cardId,l.getMissileLvMax(), game.resGameConfig.cardUpdMax_missileLv);
                }else{
                    return getStrValueT("card_name_3014_2", game.resGameConfig.cardUpdMax_missileLv);
                }


            case 3015:return getStrValueT("card_name_"+cardId,l.getNuclearLvMax(), game.resGameConfig.cardUpdMax_nuclearLv);
            case 3016:return getStrValueT("card_name_"+cardId,l.getFinancialLvMax(), game.resGameConfig.cardUpdMax_financialLv);
            case 3017:return getStrValueT("card_name_"+cardId,l.getTradeLvMax(), game.resGameConfig.cardUpdMax_tradeLv);
            case 3018:return getStrValueT("card_name_"+cardId,l.getCultureLvMax(), game.resGameConfig.cardUpdMax_cultureLv);
            case 3019:return getStrValueT("card_name_"+cardId,l.getMiracleNow(), game.resGameConfig.cardUpdMax_miracle);
            case 3020:return getStrValueT("card_name_"+cardId,l.getInfantryCardMax(), game.resGameConfig.cardUpdMax_infantryCardLv);
            case 3021:return getStrValueT("card_name_"+cardId,l.getArmorCardMax(), game.resGameConfig.cardUpdMax_armorCardLv);
            case 3022:return getStrValueT("card_name_"+cardId,l.getArtilleryCardMax(), game.resGameConfig.cardUpdMax_artilleryCardLv);
            case 3023:return getStrValueT("card_name_"+cardId,l.getNavyCardMax(), game.resGameConfig.cardUpdMax_navyCardLv);
            case 3024:return getStrValueT("card_name_"+cardId,l.getAirCardMax(), game.resGameConfig.cardUpdMax_airCardLv);
            case 3025:return getStrValueT("card_name_"+cardId,l.getNuclearCardMax(), game.resGameConfig.cardUpdMax_nuclearCardLv);
            case 3026:return getStrValueT("card_name_"+cardId,l.getMissileCardMax(), game.resGameConfig.cardUpdMax_missileCardLv);
            case 3027:return getStrValueT("card_name_"+cardId,l.getSubmarineCardMax(), game.resGameConfig.cardUpdMax_submarineCardLv);
            case 3028:return getStrValueT("card_name_"+cardId,l.getDefenceCardMax(), game.resGameConfig.cardUpdMax_defenceCardLv);
            case 3029:return getStrValueT("card_name_"+cardId,l.getGeneralCardMax(), game.resGameConfig.cardUpdMax_generalCardLv);
            case 3030:return getStrValueT("card_name_"+cardId,l.getMilitaryAcademyLv(), game.resGameConfig.cardUpdMax_militaryAcademyLv);
        }
        return  getStrValueT("card_name_"+cardId);
    }
    // 根据countryId拼接出  美国,法国,德国,日本... 等字符串
    public String getPromptStrByCountry(IntArray countryIds,int count) {
        count=count-1;
        StringBuilder rs=new StringBuilder();
        if(countryIds.size==0){
            return getStrValueT("prompt_none");
        }
        for(int i=0,iMax=countryIds.size;i<iMax;i++){
            if(countryIds.get(i)!=0){
                rs.append(getStrValueT("country_name_"+countryIds.get(i)));
                if(i>=count){
                    rs.append("...");
                    break;
                }else if(i!=iMax-1){
                    rs.append(",");
                }
            }
        }
        return rs.toString();
    }

    public String getCountryStrsByCountry(String countrys,int count) {
        if(ComUtil.isEmpty(countrys)){
            return getStrValueT("prompt_none");
        }
        count=count-1;
        StringBuilder rs=new StringBuilder();
        String[] strs = countrys.split(",");
        for(int i=0,iMax=strs.length;i<iMax;i++){
            int countryId=Integer.parseInt(strs[i]);
            if(countryId!=0){
                rs.append(getStrValueT("country_name_"+countryId));
                if(i>=count){
                    rs.append("...");
                    break;
                }else if(i!=iMax-1){
                    rs.append(",");
                }
            }
        }
        return rs.toString();
    }

    //type 0:剧情对话 1将领晋升 v我方 2将领杀敌 3阵亡 4补充 5占领建筑 6求援 7秒杀 v敌人 8.占领首都 v敌人
    public String getDialogueStr(int type,int dialogueId,String value) {
        if(ComUtil.isEmpty(value)){
            return getStrValueT(new StringBuilder("dialogue_").append(type).append("_").append(dialogueId).toString());
        }else {
            return getStrValueT(new StringBuilder("dialogue_").append(type).append("_").append(dialogueId).toString(),value);
        }
    }




    public String getPromptStrForNeedVip(int reqVipLv) {
        return "need vip"+reqVipLv+" to unlock";
    }

    //"Lv11        12/12"
    public String getPromptStrForGetTechByType(Fb2Smap.ForeignData f, Fb2Smap.BuildData b, int cardType, Fb2Smap.LegionData l) {
        int lv=0;
        StringBuilder rs=null;
        StringBuilder str=new StringBuilder();
        int length=8;
        switch (cardType){
            case 1:lv=l.getInfantryLvMax();str=str.append(l.varInfantryNum).append(ResDefaultConfig.StringName.slash).append(l.getInfantryCardMax());
                if(lv<10){length=length+1;} if(l.varInfantryNum<10){length=length+1;} break;
            case 2:lv=l.getCannonLvMax();str=str.append(l.varArtilleryNum).append(ResDefaultConfig.StringName.slash).append(l.getArtilleryCardMax());
                if(lv<10){length=length+1;} if(l.varArtilleryNum<10){length=length+1;} break;
            case 3:lv=l.getTankLvMax();str=str.append(l.varArmorNum).append(ResDefaultConfig.StringName.slash).append(l.getArmorCardMax());
                if(lv<10){length=length+1;} if(l.varArmorNum<10){length=length+1;} break;
            case 4:lv=l.getNavyLvMax();str=str.append(l.varNavyNum).append(ResDefaultConfig.StringName.slash).append(l.getNavyCardMax());
                if(lv<10){length=length+1;} if(l.varNavyNum<10){length=length+1;} break;
            case 5:lv=l.getAirLvMax();str=str.append(l.varAirNum).append(ResDefaultConfig.StringName.slash).append(l.getAirCardMax());
                if(lv<10){length=length+1;} if(l.varAirNum<10){length=length+1;} break;
            case 6:
                lv=l.getDefenceLvMax();str=str.append(l.varDefenceNum).append(ResDefaultConfig.StringName.slash).append(l.getDefenceCardMax());
                if(lv<10){length=length+1;} if(l.varDefenceNum<10){length=length+1;} break;
            case 7:lv=l.getNuclearLvMax();str=str.append(l.varNuclearNum).append(ResDefaultConfig.StringName.slash).append(l.getNuclearCardMax());
                if(lv<10){length=length+1;} if(l.varNuclearNum<10){length=length+1;} break;
            case 8:lv=l.getNavyLvMax();str=str.append(l.varSubmarineNum).append(ResDefaultConfig.StringName.slash).append(l.getSubmarineCardMax());
                if(lv<10){length=length+1;} if(l.varSubmarineNum<10){length=length+1;} break;
            case 9:if(b==null){return "";}lv=b.getCityLvNow();str=str.append(b.getCityHpNow()).append(ResDefaultConfig.StringName.slash).append(b.getCityHpMax());
                if(lv<10){length=length+1;} if(b.getCityHpNow()<10){length=length+1;}else if(b.getCityHpNow()>99&&b.getCityHpNow()<1000){length=length-2;}else if(b.getCityHpNow()>999&&b.getCityHpNow()<10000){length=length-4;} break;
            case 10:if(b==null){return "";}lv=b.getCityLvNow();str=str.append(b.getCityHpNow()).append(ResDefaultConfig.StringName.slash).append(b.getCityHpMax());
                if(lv<10){length=length+1;} if(b.getCityHpNow()<10){length=length+1;}else if(b.getCityHpNow()>99&&b.getCityHpNow()<1000){length=length-2;}else if(b.getCityHpNow()>999&&b.getCityHpNow()<10000){length=length-4;} break;
            case 11://lv=l.getTradeLvMax();str=str.append(l.getTradeCount()).append(ResConfig.StringName.slash).append(1 + l.getTradeLvMax());
                // if(lv<10){length=length+1;} if(l.getTradeCount()<10){length=length+1;} break;
                return  ComUtil.getSpace(18)+str.append(l.getTradeCount()).append(ResDefaultConfig.StringName.slash).append(1 + l.getFinancialLvMax()).toString();
            case 12:
                return ComUtil.getSpace(16)+str.append(f.getFavorValue()).append(ResDefaultConfig.StringName.slash).append(100).toString();
            default: return "";
        }
        rs=new StringBuilder();
        return rs.append("Lv").append(ComUtil.formmatNumber(lv,2,true," ")).append(ComUtil.getSpace(length)).append(str).toString();
    }


    public String getPromptStrForTechLvByType(Fb2Smap.ForeignData f, Fb2Smap.BuildData b, int cardType, Fb2Smap.LegionData l) {
        int lv=0;
        StringBuilder rs=null;
        int length=8;
        switch (cardType){
            case 1:lv=l.getInfantryLvMax(); break;
            case 2:lv=l.getCannonLvMax(); break;
            case 3:lv=l.getTankLvMax();break;
            case 4:lv=l.getNavyLvMax();break;
            case 5:lv=l.getAirLvMax();break;
            case 6:
                lv=l.getDefenceLvMax(); break;
            case 7:lv=l.getNuclearLvMax(); break;
            case 8:lv=l.getNavyLvMax();break;
            case 9:if(b==null){return "";}lv=b.getCityLvNow();break;
            case 10:if(b==null){return "";}lv=b.getCityLvNow();break;
            default: return "";
        }
        rs=new StringBuilder();
        return rs.append("Lv").append(lv).toString();
    }


    public String getPromptStrForTechByType(Fb2Smap.ForeignData f, Fb2Smap.BuildData b, int cardType, Fb2Smap.LegionData l) {
        int lv=0;
        StringBuilder rs=null;
        StringBuilder str=new StringBuilder();
        int length=8;
        switch (cardType){
            case 1:lv=l.getInfantryLvMax();str=str.append(l.varInfantryNum).append(ResDefaultConfig.StringName.slash).append(l.getInfantryCardMax());
                if(lv<10){length=length+1;} if(l.varInfantryNum<10){length=length+1;} break;
            case 2:lv=l.getCannonLvMax();str=str.append(l.varArtilleryNum).append(ResDefaultConfig.StringName.slash).append(l.getArtilleryCardMax());
                if(lv<10){length=length+1;} if(l.varArtilleryNum<10){length=length+1;} break;
            case 3:lv=l.getTankLvMax();str=str.append(l.varArmorNum).append(ResDefaultConfig.StringName.slash).append(l.getArmorCardMax());
                if(lv<10){length=length+1;} if(l.varArmorNum<10){length=length+1;} break;
            case 4:lv=l.getNavyLvMax();str=str.append(l.varNavyNum).append(ResDefaultConfig.StringName.slash).append(l.getNavyCardMax());
                if(lv<10){length=length+1;} if(l.varNavyNum<10){length=length+1;} break;
            case 5:lv=l.getAirLvMax();str=str.append(l.varAirNum).append(ResDefaultConfig.StringName.slash).append(l.getAirCardMax());
                if(lv<10){length=length+1;} if(l.varAirNum<10){length=length+1;} break;
            case 6:
                lv=l.getDefenceLvMax();str=str.append(l.varDefenceNum).append(ResDefaultConfig.StringName.slash).append(l.getDefenceCardMax());
                if(lv<10){length=length+1;} if(l.varDefenceNum<10){length=length+1;} break;
            case 7:lv=l.getNuclearLvMax();str=str.append(l.varNuclearNum).append(ResDefaultConfig.StringName.slash).append(l.getNuclearCardMax());
                if(lv<10){length=length+1;} if(l.varNuclearNum<10){length=length+1;} break;
            case 8:lv=l.getNavyLvMax();str=str.append(l.varSubmarineNum).append(ResDefaultConfig.StringName.slash).append(l.getSubmarineCardMax());
                if(lv<10){length=length+1;} if(l.varSubmarineNum<10){length=length+1;} break;
            case 9:if(b==null){return "";}lv=b.getCityLvNow();str=str.append(b.getCityHpNow()).append(ResDefaultConfig.StringName.slash).append(b.getCityHpMax());
                if(lv<10){length=length+1;} if(b.getCityHpNow()<10){length=length+1;}else if(b.getCityHpNow()>99&&b.getCityHpNow()<1000){length=length-2;}else if(b.getCityHpNow()>999&&b.getCityHpNow()<10000){length=length-4;} break;
            case 10:if(b==null){return "";}lv=b.getCityLvNow();str=str.append(b.getCityHpNow()).append(ResDefaultConfig.StringName.slash).append(b.getCityHpMax());
                if(lv<10){length=length+1;} if(b.getCityHpNow()<10){length=length+1;}else if(b.getCityHpNow()>99&&b.getCityHpNow()<1000){length=length-2;}else if(b.getCityHpNow()>999&&b.getCityHpNow()<10000){length=length-4;} break;
            case 11://lv=l.getTradeLvMax();str=str.append(l.getTradeCount()).append(ResConfig.StringName.slash).append(1 + l.getTradeLvMax());
                // if(lv<10){length=length+1;} if(l.getTradeCount()<10){length=length+1;} break;
                return  str.append(l.getTradeCount()).append(ResDefaultConfig.StringName.slash).append(1 + l.getFinancialLvMax()).toString();
            case 12:
                return str.append(f.getFavorValue()).append(ResDefaultConfig.StringName.slash).append(100).toString();
            default: return "";
        }
        return str.toString();
    }

    //999     999
    //不同比例的数字,会导致其位置有所不同,需要注意
    public String getPromptStrForGetPrice(int v1, int v2) {
        return ComUtil.formmatNumber(v1,3,true,"  ")+ComUtil.getSpace(7)+ComUtil.formmatNumber(v2,3,true,"  ");
    }

    //1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
    public int getLegionEffectValueForUnitDamage(Fb2Smap.LegionData legionData,int cardType,int cardId,int terrainIndex) {
        int rs=0;
        switch (cardType){
            //步兵
            case 1:rs=legionData.getLegionFeatureEffect(59); break;
            //炮兵
            case 2:rs=legionData.getLegionFeatureEffect(60); break;
            //坦克
            case 3:rs=legionData.getLegionFeatureEffect(61); break;
            //船只
            case 4:rs=legionData.getLegionFeatureEffect(62); break;
            //飞机
            case 5:rs=legionData.getLegionFeatureEffect(63); break;
           /* //超武
            case 7:legionData.getFeatureEffect(41); break;*/
            //潜艇
            case 8:rs=legionData.getLegionFeatureEffect(64); break;
            case 6:
                if(cardId==1606){//飞弹
                    rs=legionData.getLegionFeatureEffect(65);
                }else {//要塞
                    rs= legionData.getLegionFeatureEffect(66);
                }
                break;
        }

        switch (terrainIndex){
            case 4:
            case 5:
                rs=rs+legionData.getLegionFeatureEffect(86);
                break;
            case 6:
            case 8:
                rs=rs+legionData.getLegionFeatureEffect(87);
                break;
            case 9:
                rs=rs+legionData.getLegionFeatureEffect(88);
                break;
            case 2:
            case 11:
                rs=rs+legionData.getLegionFeatureEffect(89);
                break;
        }
        return rs;
    }

    //暴击
    public int getLegionEffectValueForUnitCrit(Fb2Smap.LegionData legionData, int cardType, int cardId) {
        int crit=0;
        switch (cardType){
            //步兵
            case 1:crit=legionData.getLegionFeatureEffect(67); break;
            //炮兵
            case 2:crit=legionData.getLegionFeatureEffect(68); break;
            //坦克
            case 3:crit=legionData.getLegionFeatureEffect(69); break;
            //船只
            case 4:crit=legionData.getLegionFeatureEffect(70); break;
            //飞机
            case 5:crit=legionData.getLegionFeatureEffect(71); break;
           /* //超武
            case 7:crit=legionData.getLegionFeatureEffect(41); break;*/
            //潜艇
            case 8:crit=legionData.getLegionFeatureEffect(72); break;
            case 6:
                if(cardId==1606){//飞弹
                    crit=legionData.getLegionFeatureEffect(73);
                }else {//要塞
                    crit=legionData.getLegionFeatureEffect(74);
                }
                break;
        }
        return crit;
    }

    public int getLegionEffectValueForUnitAbility(Fb2Smap.LegionData l, int armyType, int armyId) {
        int armyAbility=0;
        switch (armyType){
            case 1:
                armyAbility=l.getLegionFeatureEffect(51);  break;
            case 2:
                armyAbility=l.getLegionFeatureEffect(52); break;
            case 3:
                armyAbility=l.getLegionFeatureEffect(53); break;
            case 4:
                armyAbility=l.getLegionFeatureEffect(54); break;
            case 5:
                armyAbility=l.getLegionFeatureEffect(55); break;
            case 8:
                armyAbility=l.getLegionFeatureEffect(56); break;
            case 6:
                if(armyId==1606){
                    armyAbility=l.getLegionFeatureEffect(57);
                }else {
                    armyAbility=l.getLegionFeatureEffect(58);
                }
                break;
        }
        return armyAbility;
    }

    public int getLegionEffectValueForUnitGameValue(Fb2Smap.LegionData l, int armyType, int armyId,int gameValue) {

        switch (armyType){
            case 1:gameValue=gameValue-l.getLegionFeatureEffect(41);break;
            case 2:gameValue=gameValue-l.getLegionFeatureEffect(42);break;
            case 3:gameValue=gameValue-l.getLegionFeatureEffect(43);break;
            case 4:gameValue=gameValue-l.getLegionFeatureEffect(44);break;
            case 5:gameValue=gameValue-l.getLegionFeatureEffect(45);break;
            case 7:gameValue=gameValue-l.getLegionFeatureEffect(48);break;
            case 8:gameValue=gameValue-l.getLegionFeatureEffect(46);break;
            case 6:
                if(armyId==1606){
                    gameValue=gameValue-l.getLegionFeatureEffect(47);
                }else {
                    gameValue=gameValue-l.getLegionFeatureEffect(49);
                }
                break;
        }
        if(gameValue<0){gameValue=1;}
        return gameValue;
    }

    public int getLegionEffectValueForUnitLv(Fb2Smap.LegionData l, int armyType, int armyId) {
        int armyLv=0;
        switch (armyType){
            case 1:
                armyLv=l.getLegionFeatureEffect(75);  break;
            case 2:
                armyLv=l.getLegionFeatureEffect(76); break;
            case 3:
                armyLv=l.getLegionFeatureEffect(77); break;
            case 4:
                armyLv=l.getLegionFeatureEffect(78); break;
            case 5:
                armyLv=l.getLegionFeatureEffect(79); break;
            case 8:
                armyLv=l.getLegionFeatureEffect(80); break;
            case 6:
                if(armyId==1606){
                    armyLv=l.getLegionFeatureEffect(81);
                }else {
                    armyLv=l.getLegionFeatureEffect(82);
                }
                break;
        }
        return armyLv;
    }

    public String getStrByLegionFeatureText(int legionFeature, int legionFeatureLv, int legionFeatureEffect) {
        //String name=getStrValue("legionfeature_name_"+legionFeature);
        //String effect=getStrValue("legionfeature_info_"+legionFeatureEffect);
        //return   ComUtil.formmatString(name+ComUtil.converNumToRoman(legionFeatureLv)+ResConfig.StringName.colon ,24,0,ResConfig.StringName.space1)    +effect+legionFeatureLv;
        return getStrValueT("legionfeature_name_"+legionFeature)+ComUtil.converNumToRoman(legionFeatureLv)+ ResDefaultConfig.StringName.colon+ getStrValueT("legionfeature_info_"+legionFeature,legionFeatureEffect);
    }

    /*public String getStrByTask(Fb2Smap.TaskData t) {
     *//*if(t.getTaskType()==2){
            XmlReader.Element lpE=game.gameConfig.getDEF_LEGIONPOLICY().getElementById(t.getTaskIndex());
            if(lpE!=null&&!lpE.get("country","-1").equals("-1")){

            }else {
                return game.gameMethod.getStrValueT(
                        t.getTaskNameStr(),ComUtil.converNumToRoman(t.getTaskLv()))+":"
                        + getStrValueT("task_info_"+t.getTaskIndex(),t.getCountNow(),t.getCountMax(),t.getTaskRound(),t.getTaskStr());
            }


        }else{
            return game.gameMethod.getStrValueT(
                    t.getTaskNameStr(),ComUtil.converNumToRoman(t.getTaskLv()))+":"
                    + getStrValueT("task_info_"+t.getTaskIndex(),t.getCountNow(),t.getCountMax(),t.getTaskRound(),t.getTaskStr());
        }*//*
        return game.gameMethod.getStrValueT(
                t.getTaskNameStr(),ComUtil.converNumToRoman(t.getTaskLv()))+":"
                +t.getTaskInfoStr();
    }*/


    public String getStrValueForResource(int bontyType,int value) {
        switch (bontyType){
            case 0:return  getStrValueT("resource_name_money",value);
            case 1:return  getStrValueT("resource_name_industry",value);
            case 2:return  getStrValueT("resource_name_tech",value);
            case 3:return  getStrValueT("resource_name_food",value);
            case 4:return  getStrValueT("resource_name_mineral",value);
            case 5:return  getStrValueT("resource_name_oil",value);
        }
        return "";
    }


    //获得除了帝国模式外的评分  210318
    public int getStageScoreExceptEmpire(int stageId) {
        return game.gameConfig.playerConfig.getInteger(stageId+"_0_score",0);
    }


    //获得帝国评分 210318
    public int getEmpireScore(int empireId) {
        XmlReader.Element empireE= game.gameConfig.getDEF_EMPIRE().getElementById(empireId);
        Array<XmlReader.Element> stageEs= empireE.getChildrenByName("stage");
        if(stageEs.size==0){
            return 0;
        }
        XmlReader.Element e=null;
        int rs=0;
        for(int i=0,iMax=stageEs.size;i<iMax;i++){
            rs=rs+game.gameConfig.playerConfig.getInteger(empireId+"_"+i+"_score",0);
        }
        if(stageEs.size==0){
            return 0;
        }
        return rs/stageEs.size;
        /*if(rs>=4){
            return 3;
        }else if(rs>=3){
            return 2;
        }else if(rs>=2){
            return 1;
        }else{
            return 0;
        }*/
    }




    public int getDrawTypeByAir(int airId, int hexagon1, int hexagon2) {
        //先刨除移动到己方这种情况
        //当目标是己方的时候就是定位标志
        if(game.getSMapDAO().ifAllyByHexagon(hexagon1,hexagon2)&&game.getSMapDAO().masterData.getPlayerMode()!=2){
            return 10;
        }

        switch (airId){
            case 1501://侦察机
                return 10;
            case 1502://战斗机
                return 9;
            case 1503://攻击机
                return 10;
            case 1504://轰炸机
                return 11;
            case 1505://运输机
                return 8;
            case 1506://战略轰炸机
                return 11;
            case 1507://预警机
                return 11;
        }


        return 1;
    }

    //str 是否包含li
    public Boolean ifScriptTriggerLiOk(String str,int li){
        if(str.equals("-1")||li==-1||ComUtil.ifHaveValueInStr(str,li)){
            return true;
        }
        return false;
    }


    //str type,value1,value2.....
    //type 0 介于  1 value大于等于value1  2  value小于等于value2
    public Boolean ifScriptTriggerStrOk(String str, int value){
        if(str.equals("-1")  ){
            return true;
        }


        if(ComUtil.isNumeric(str)&&Integer.parseInt(str)==value){
            return true;
        }
        String[] strs = str.split(",");
        if(!ComUtil.isNumeric(strs[0])){
            return false;
        }
        int type=Integer.parseInt(strs[0]);

        switch (type){
            case 0://介于
                if(strs.length!=3||!ComUtil.isNumeric(strs[1])||!ComUtil.isNumeric(strs[2])){
                    return false;
                }
                int v1=Integer.parseInt(strs[1]);
                int v2=Integer.parseInt(strs[2]);
                return ComUtil.ifValueBetween(value,v1,v2,true);
            case 1://1大于等于
                if(strs.length!=2||!ComUtil.isNumeric(strs[1])){
                    return false;
                }
                v1=Integer.parseInt(strs[1]);
                if( value >= v1){
                    return true;
                }

                break;
            case 2:// 2小于等于
                if(strs.length!=2||!ComUtil.isNumeric(strs[1])){
                    return false;
                }
                v1=Integer.parseInt(strs[1]);
                if( value <= v1){
                    return true;
                }
                break;

        }

        return false;
    }



/*
 //获得符合条件的游戏剧本
    //eventId 不能为-1
    通用判断条件:
    //ifRepeat 是否重复
    //preEvent 前置事件 ,分割 可以多个
    //triggerRound  触发回合 -1则不判断,  单个数字时 相等则true  type,v1,v2  0:介于   type,v1 1: v大于等于v1  2:v小于等于v1
    //triggerYear 触发年代 -1则不判断,  单个数字时 相等则true  type,v1,v2  0:介于 type,v1 1: v大于等于v1  2:v小于等于v1


    特殊判断条件:
    //trigger 判断条件类型
    //triggerLegionIndex 触发国家,配合一些判断条件使用 0为玩家
    //有以下判断条件:
    default:默认  判断 triggerLegionIndex或 triggerCountry 可多写,triggerLegionIndex可多写
    legionMoreThanResource:军团的资源>=指定条件  triggerValue: 长度6 金钱|工业|科技|食物|矿产|石油  -1则不判断
    legionLessThanResource:军团的资源<=指定条件  triggerValue: 长度6 金钱|工业|科技|食物|矿产|石油  -1则不判断
    legionMoreThanRegionPercentage:军团的占领进度>= triggerValue
    legionLessThanRegionPercentage:军团的占领<= triggerValue
    legionOccupyHexagon:军团是否占领某地块 如果是玩家,则玩家同盟占领也计算在内 triggerLegionIndex可多写,也可为0
    legionOccupyRegion:军团是否占领某区域  如果是玩家,则玩家同盟占领也计算在内 triggerLegionIndex可多写,也可为0


 */




    public Array<XmlReader.Element> getSMapScreenScipt(XmlIntDAO script, Fb2Smap btl, Array<XmlReader.Element> rs) {
        if(rs==null){
            rs=new Array<>();
        }else {
            rs.clear();
        }
        Array<XmlReader.Element> se=  script.e.getChildrenByName("script");
        Fb2Smap.LegionData playerLegion=btl.getPlayerLegionData();



        String type;
        for(XmlReader.Element x:se){
            int gameEpisode=x.getInt("gameEpisode",-1);
            if(gameEpisode!=-1&&gameEpisode!=btl.masterData.getGameEpisode()){
                continue;
            }
            int playerTriggerType=x.getInt("playerTriggerType",-1);
            String triggerLegionIndexs=x.get("triggerLegionIndex","");
            boolean ifPlayerTriggerOk=false;
             switch (playerTriggerType){
                 case -1://忽略
                     ifPlayerTriggerOk=true;
                     break;
                 case 0://玩家为空
                     if((playerLegion==null||playerLegion.getLegionIndex()==0)){
                         ifPlayerTriggerOk=true;
                     }
                     break;
                 case 1://玩家在triggerLegionIndex中
                     if((ComUtil.ifHaveValueInStr(triggerLegionIndexs,playerLegion.getLegionIndex()))){
                         ifPlayerTriggerOk=true;
                     }
                     break;
                 case 2://玩家不在triggerLegionIndex中
                     if(((!ComUtil.ifHaveValueInStr(triggerLegionIndexs,playerLegion.getLegionIndex())))){
                         ifPlayerTriggerOk=true;
                     }
                     break;
             }
             if(!ifPlayerTriggerOk){
                 continue;
             }
            int eventId=x.getInt("id",-1);
            if(eventId==-1){
                continue;
            }
            String preEvent=x.get("preEvent","-1");
            //是否触发过前置事件
            if(!btl.ifTriggerAllPreEvent(preEvent,x.getInt("preTriggerType",0))){
                continue;
            }
            String mutualEvent=x.get("mutualEvent","-1");
            //是否触发过互斥事件
            if(btl.ifTriggerOneMutualEvent(mutualEvent)){
                continue;
            }


            Boolean ifRepeat =x.getBoolean("ifRepeat",false);
            if(!ifRepeat&&btl.scriptIDatas.contains(eventId)){
                continue;
            }

            if(eventId==12){
                int s=0;
                Gdx.app.log("load event",eventId+"");
            }
            if(!ifScriptTriggerStrOk(x.get("triggerRound","-1"),btl.masterData.getRoundNow())){
                continue;
            }
            if(!ifScriptTriggerStrOk(x.get("triggerYear","-1"),btl.getNowYear())){
                continue;
            }
            if(!ifScriptTriggerLiOk(x.get("modeType","-1"),btl.masterData.getPlayerMode())){
                continue;
            }

            if(x.getInt("checkSystemEffective",-1)!=-1){
               if(!btl.ifSystemEffective(x.getInt("checkSystemEffective",-1))){
                   continue;
               }
            }



            if(x.get("checkOpenGroup",null)!=null&&game.sMapScreen!=null){
                if(!game.sMapScreen.checkOpenGroup(x.get("checkOpenGroup",""),x.getBoolean("checkOnlyOpenGroup",true))){
                    continue;
                }
            }

            type=x.get("trigger");
            switch (type){
                case "noRecord":
                    if(!game.gameConfig.playerConfig.getBoolean(x.get("triggerValue",""),false)){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        continue;
                    }
                    break;
                case "default":
                    //  boolean b1=x.get("triggerLegionIndex","-2").equals("-2")&&x.get("triggerCountry","-2").equals("-2");
                    //  boolean b2=ifScriptTriggerLiOk(x.get("triggerLegionIndex","-1"),playerLegion.getLegionIndex());
                    //  boolean b3=ifScriptTriggerLiOk(x.get("triggerCountry","-1"),playerLegion.getCountryId());
                    if( (ifScriptTriggerLiOk(x.get("triggerLegionIndex","-1"),playerLegion.getLegionIndex()))&&(ifScriptTriggerLiOk(x.get("triggerCountry","-1"),playerLegion.getCountryId()))){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        continue;
                    }
                    break;
                case "regionIfContral"://某个地区是否被控制
                    int li=x.getInt("triggerLegionIndex",-1);
                    if(li==-1){
                        int country=x.getInt("triggerCountry",-1);
                        if(country==-1){
                            continue;
                        }
                        Fb2Smap.LegionData l=btl.getLegionByCountry(country,true);
                        if(l==null){
                            continue;
                        }
                        li=l.getLegionIndex();
                    }
                    Fb2Smap.BuildData b=btl.getBuildDataByRegion(x.getInt("region",-1));
                    if(b==null){
                        continue;
                    }
                    if(b.isAlly(li)){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        continue;
                    }
                    break;
                case "srRegionIfAiControl"://如果srRegion都是ai控制的
                    if(btl.srRegionIfAIControl(x.getInt("triggerValue",-1))){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        continue;
                    }
                    break;
                case "countrysIsAi"://如果这些都不是玩家
                    if(btl.countrysIsAi(x.get("triggerCountry","-1"))){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        continue;
                    }
                    break;

                case "legionsIsAi"://如果这些都不是玩家
                    if(btl.legionsIsAi(x.get("triggerLegionIndex","-1"))){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        continue;
                    }
                    break;
                case "srRegionIfHaveCountry":
                    if( (ifScriptTriggerLiOk(x.get("triggerLegionIndex","-1"),playerLegion.getLegionIndex()))&&(ifScriptTriggerLiOk(x.get("triggerCountry","-1"),playerLegion.getCountryId()))){
                        String[] st=x.get("triggerValue","").split(",");
                        if(st.length!=2||!ComUtil.isNumeric(st[0])||!ComUtil.isNumeric(st[1])){
                            continue ;
                        }
                        int country=Integer.parseInt(st[1]);
                        if(country==-1){
                            continue;
                        }
                        Fb2Smap.LegionData l=btl.getLegionByCountry(country,true);
                        if(l==null){
                            continue;
                        }
                        li=l.getLegionIndex();
                        if(btl.srRegionIfHaveLi(Integer.parseInt(st[0]),li)){
                            rs.add(x);
                            if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                            continue;
                        }
                    }
                    break;
                case "regionIfNotContral"://某个地区是否被控制
                    li=x.getInt("triggerLegionIndex",-1);
                    if(li==-1){
                        int country=x.getInt("triggerCountry",-1);
                        if(country==-1){
                            continue;
                        }
                        Fb2Smap.LegionData l=btl.getLegionByCountry(country,true);
                        if(l==null){
                            continue;
                        }
                        li=l.getLegionIndex();
                    }
                    b=btl.getBuildDataByRegion(x.getInt("triggerValue",-1));
                    if(b==null){
                        continue;
                    }
                    if(!b.isAlly(li)){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        continue;
                    }
                    break;
                case "legionMoreThanResource":
                    String[] triggerValue = x.get("triggerValue").split(",");
                    String  tempStr=triggerValue[0];
                    int money,industry,tech,food,mineral,oil;
                    if(ComUtil.isNumeric(tempStr)){
                        money=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempStr=triggerValue[1];
                    if(ComUtil.isNumeric(tempStr)){
                        industry=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempStr=triggerValue[2];
                    if(ComUtil.isNumeric(tempStr)){
                        tech=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempStr=triggerValue[3];
                    if(ComUtil.isNumeric(tempStr)){
                        food=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }

                    tempStr=triggerValue[4];
                    if(ComUtil.isNumeric(tempStr)){
                        mineral=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempStr=triggerValue[5];
                    if(ComUtil.isNumeric(tempStr)){
                        oil=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }

                    int tempInt=x.getInt("triggerLegionIndex",-1);
                    Fb2Smap.LegionData legionData;
                    legionData=btl.getLegionDataByLi(tempInt);


                    if(legionData!=null&&(money==-1||legionData.getMoney()>=money)&&(industry==-1||legionData.getIndustry()>=industry)&&(tech==-1||legionData.getTech()>=tech)&&(food==-1||legionData.getFood()>=food)&&(mineral==-1||legionData.getMineral()>=mineral)&&(oil==-1||legionData.getOil()>=oil)){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                    }
                    break;
                case "legionLessThanResource":
                    triggerValue = x.get("triggerValue").split(",");
                    tempStr=triggerValue[0];

                    if(ComUtil.isNumeric(tempStr)){
                        money=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempStr=triggerValue[1];
                    if(ComUtil.isNumeric(tempStr)){
                        industry=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempStr=triggerValue[2];
                    if(ComUtil.isNumeric(tempStr)){
                        tech=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempStr=triggerValue[3];
                    if(ComUtil.isNumeric(tempStr)){
                        food=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }

                    tempStr=triggerValue[4];
                    if(ComUtil.isNumeric(tempStr)){
                        mineral=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempStr=triggerValue[5];
                    if(ComUtil.isNumeric(tempStr)){
                        oil=Integer.parseInt(tempStr);
                    }else {
                        break;
                    }
                    tempInt=x.getInt("triggerLegionIndex",-1);
                    if(tempInt==0){
                        legionData=playerLegion;
                    }else{
                        legionData=btl.getLegionDataByLi(tempInt);
                    }
                    if(legionData!=null&&(money==-1||legionData.getMoney()<=money)&&(industry==-1||legionData.getIndustry()<=industry)&&(tech==-1||legionData.getTech()<=tech)&&(food==-1||legionData.getFood()<=food)
                            &&(mineral==-1||legionData.getMineral()<=mineral)&&(oil==-1||legionData.getOil()<=oil)){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                    }
                    break;
                case "legionMoreThanRegionPercentage":
                    tempStr=x.get("triggerValue");
                    tempInt= x.getInt("triggerLegionIndex",-1);
                    if(tempInt==0){
                        legionData=playerLegion;
                    }else{
                        legionData=btl.getLegionDataByLi(tempInt);
                    }
                    tempInt=-1;
                    if(ComUtil.isNumeric(tempStr)){
                        tempInt=Integer.parseInt(tempStr);
                    }
                    if(tempInt!=-1&&legionData!=null&& tempInt>=legionData.getWorldPress()){
                        rs.add(x);
                    }
                    break;
                case "legionLessThanRegionPercentage":
                    tempStr=x.get("triggerValue");
                    tempInt= x.getInt("triggerLegionIndex",-1);
                    if(tempInt==0){
                        legionData=playerLegion;
                    }else{
                        legionData=btl.getLegionDataByLi(tempInt);
                    }


                    if(ComUtil.isNumeric(tempStr)){
                        tempInt=Integer.parseInt(tempStr);
                    }
                    if(tempInt!=-1&& tempInt<=legionData.getWorldPress()){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                    }
                    break;
                case "legionOccupyHexagon":
                    tempStr=x.get("triggerValue");
                    tempInt=-1;
                    if(ComUtil.isNumeric(tempStr)){
                        tempInt=Integer.parseInt(tempStr);
                    }
                    if(x.get("triggerLegionIndex").equals("0")){
                        if(btl.ifAllyPlayerByLi(btl.getLegionIndexByHexagon(tempInt))){
                            rs.add(x);
                            if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        }
                    }else{
                        if(ifScriptTriggerLiOk(x.get("triggerLegionIndex"),btl.getLegionIndexByHexagon(tempInt))){
                            rs.add(x);
                            if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        }
                    }
                    break;

                case "legionOccupyRegion":
                    tempStr=x.get("triggerValue");
                    tempInt=-1;
                    if(ComUtil.isNumeric(tempStr)){
                        tempInt=Integer.parseInt(tempStr);
                    }
                    if(x.get("triggerLegionIndex").equals("0")){
                        Fb2Smap.BuildData   buildData=btl.getBuildDataByRegion(btl.getRegionId(tempInt));
                        if(buildData!=null&&buildData.isPlayerAlly()){
                            rs.add(x);
                            if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        }
                    }else{
                        Fb2Smap.BuildData buildData=btl.getBuildDataByRegion(btl.getRegionId(tempInt));
                        if(buildData!=null&&ifScriptTriggerLiOk(x.get("triggerLegionIndex"),buildData.getLegionIndex())){
                            rs.add(x);
                            if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                        }
                    }
                    break;


                case "legionStatility":
                    tempStr=x.get("triggerValue");
                    tempInt= x.getInt("triggerCountry",-1);
                    if(tempInt!=-1){
                        if(tempInt==0){
                            legionData=playerLegion;
                        }else{
                            legionData=btl.getLegionByCountry(tempInt,true);
                        }
                    }else{
                        tempInt= x.getInt("triggerLegionIndex",-1);
                        if(tempInt==0){
                            legionData=playerLegion;
                        }else{
                            legionData=btl.getLegionDataByLi(tempInt);
                        }
                    }
                    if(legionData!=null&&ComUtil.ifValueBetWeenStrs(tempStr,legionData.getStability(),true)){
                        rs.add(x);
                        if(!ifRepeat){btl.addTriggerScriptIndex(eventId);}
                    }
                    break;
            }
        }


        return rs;
    }



    //获得符合条件的游戏剧本
    public Array<XmlReader.Element> getSMapScreenScipt(XmlIntDAO script, Fb2Smap btl, Array<XmlReader.Element> rs,int eventIndex) {
        if(rs==null){
            rs=new Array<>();
        }else {
            rs.clear();
        }
        if(eventIndex==-1){
            return rs;
        }
        Array<XmlReader.Element> se=  script.e.getChildrenByName("script");


        for(XmlReader.Element x:se){
            int eventId=x.getInt("id",-1);
            if(eventId==-1){
                continue;
            }
            String preEvent=x.get("preEvent","-1");
            if(!btl.ifTriggerAllPreEvent(preEvent,x.getInt("preTriggerType",0))){
                continue;
            }
            if(x.get("trigger").equals("eventSelected")&&eventId== eventIndex  ){
                rs.add(x);
                btl.addTriggerScriptIndex(eventId);
            }
        }


        return rs;
    }

    //area 0未知 1亚2欧3北美4南美5非6大洋洲
    //gameResult 1~5 d~s
    public void recordConquestAchievement(int area, int gameResult) {
        if(area==0||gameResult<=0||area>6){
            return;
        }
        int result=0;
        switch (area){
            case 1:result=game.gameConfig.playerConfig.getInteger("AsiaConquestNum",0);break;
            case 2:result=game.gameConfig.playerConfig.getInteger("EuropeConquestNum",0);break;
            case 3:result=game.gameConfig.playerConfig.getInteger("NorthAmericaConquestNum",0);break;
            case 4:result=game.gameConfig.playerConfig.getInteger("SouthAmericaConquestNum",0);break;
            case 5:result=game.gameConfig.playerConfig.getInteger("AfricaConquestNum",0);break;
            case 6:result=game.gameConfig.playerConfig.getInteger("OceaniaConquestNum",0);break;
        }
        if(result<=0){
            result++;
        }else if(result>0&&result<=1&&gameResult>1){
            result++;
        }else if(result>1&&result<=3&&gameResult>3){
            result++;
        }else if(result>3&&result<=6&&gameResult>=5){
            result++;
        }else if(gameResult>=4){
            result++;
        }else {
            return;
        }
        switch (area){
            case 1:game.gameConfig.playerInfo.put("AsiaConquestNum",result);break;
            case 2:game.gameConfig.playerInfo.put("EuropeConquestNum",result);break;
            case 3:game.gameConfig.playerInfo.put("NorthAmericaConquestNum",result);break;
            case 4:game.gameConfig.playerInfo.put("SouthAmericaConquestNum",result);break;
            case 5:game.gameConfig.playerInfo.put("AfricaConquestNum",result);break;
            case 6:game.gameConfig.playerInfo.put("OceaniaConquestNum",result);break;
        }
        game.gameConfig.playerInfo.flush();
    }



    //str:gamePrompt_
    public String getMergeStr(String str,int star,int count) {
        StringBuilder rs=new StringBuilder();
        // int iMax=game.gameConfig.getDEF_RDIALOGUE().getElementById(13).getInt("count");

        for(int i=star;i<count;i++){
            rs.appendLine(getStrValueT(str+i));
        }
        return rs.toString();
    }


    public String getStrValueForHQExplain() {
        int rank=game.gameConfig.getPlayerRank();

        int  c1=game.gameConfig.playerConfig.getInteger("EuropeConquestNum",0);
        int  c2=game.gameConfig.playerConfig.getInteger("AfricaConquestNum",0);
        int  c3=game.gameConfig.playerConfig.getInteger("AsiaConquestNum",0);
        int  c4=game.gameConfig.playerConfig.getInteger("OceaniaConquestNum",0);
        int  c5=game.gameConfig.playerConfig.getInteger("NorthAmericaConquestNum",0);
        int  c6=game.gameConfig.playerConfig.getInteger("SouthAmericaConquestNum",0);
        int c0=c1+c2+c3+c4+c5+c6;
        return getStrValueT("prompt_hq_explain_1","rankName_"+rank,c0,c1,c2,c3,c4,c5,c6);

    }

    //获得适应高度
    public int getAdaptHeight(float hightRatio){//hightRatio * uiStageHeight / 100
        return (int) (hightRatio*game.getWorldHeight()/100);
    }

    //默认开发高为720,默认开发比例是16:9
    public int getDefaultHeight(float hightRatio){//hightRatio * uiStageHeight / 100
        return (int) (hightRatio*720/100);
    }

    //判断玩家是否需要建议
    public boolean ifNeedAdvice(int adviceIndex) {
        switch (adviceIndex){
            case 0:
                if(game.getSMapDAO().chiefData!=null&&game.getSMapDAO().chiefData.varHarmonyChange<0){
                    return true;
                }
                break;
            case 1:
                if(game.getSMapDAO().chiefData!=null&&game.getSMapDAO().chiefData.vaAreaStabilityChange<0){
                    return true;
                }
                break;
            case 2:
                if(game.getSMapDAO().ifSystemEffective(16)&&game.getSMapDAO().getPlayerLegionData()!=null&&game.getSMapDAO().getPlayerAmbition()==100){
                    return true;
                }
                break;
            case 3:
                if(game.getSMapDAO().getPlayerLegionData()!=null&&game.getSMapDAO().getPlayerLegionData().ifFoodUnderSupply()&&game.getSMapDAO().getPlayerLegionData().ifAllResourceGreaterThanValue(100)){
                    return true;
                }
                break;
            case 4:
                if(game.getSMapDAO().getPlayerLegionData()!=null&&game.getSMapDAO().getPlayerLegionData().getLegionRound()==0&&game.getSMapDAO().getPlayerLegionData().ifAllResourceGreaterThanValue(100)&ifHaveLegionTechCardCanBuy(game.getSMapDAO(),game.getSMapDAO().getPlayerLegionData())){
                    return true;
                }
                break;
            case 5:
                if(game.getSMapDAO().getPlayerLegionData()!=null&&game.getSMapDAO().getPlayerLegionData().getCanBuyLegionFeaturePotion()!=0){
                    return true;
                }
                break;
            case 6:
                if(game.getSMapDAO().getPlayerLegionData()!=null&&game.getSMapDAO().getPlayerLegionData().getTradeCount()!=0&&game.getSMapDAO().getPlayerLegionData().ifAllResourceGreaterThanValue(100)){
                    return true;
                }
                break;
            case 7:
                if(game.getSMapDAO().getPlayerLegionData().ifThanResMax()&&game.getSMapDAO().getPlayerLegionData().getFinancialLvMax()< game.resGameConfig.cardUpdMax_financialLv&&game.getSMapDAO().getPlayerLegionData().ifAllResourceGreaterThanValue(100)&&game.getSMapDAO().canPayCardPrice(game.getSMapDAO().getPlayerLegionData(),null,3016)){
                    return true;
                }
                break;
            case 8:
                if(game.getSMapDAO().getPlayerLegionData().ifFullArmyNum()&&game.getSMapDAO().getPlayerLegionData().ifAllResourceGreaterThanValue(100)&&ifCanUpdLegionGroupCard(game.getSMapDAO(),game.getSMapDAO().getPlayerLegionData())){
                    return true;
                }
                break;
            case 9:
                if(game.getSMapDAO().ifPlayerHaveCanCommandBuild()){
                    return true;
                }
                break;
            case 10:
                if(game.getSMapDAO().ifPlayerHaveCanCommandUnit()){
                    return true;
                }
                break;
            case 11:
                if(game.getSMapDAO().ifPlayerHaveCanSubmitTask()){
                    return true;
                }
                break;
            case 12:
                if(game.getSMapDAO().getPlayerLegionData()!=null&&game.getSMapDAO().getPlayerLegionData().incomeIndustry>game.getSMapDAO().getPlayerLegionData().incomeMineral){
                    return true;
                }
                break;

        }
        return false;
    }







    /*public  boolean ifCardCanBuy(Fb2Smap smap,  XmlReader.Element cardE, Fb2Smap.LegionData legion, Fb2Smap.BuildData build, Fb2Smap.ForeignData f){

        int cardType=cardE.getInt("type",-1);
        int cardId=cardE.getInt("id",-1);
        if(cardType==-1||cardId==-1){
            return false;
        }
        boolean ifCanBuy=true;
        //对进行几次判断
        if((cardType<=10&&build.getBuildRound()>0)){//如果是地块卡牌,且地块研究回合>0,则显示round标记
            ifCanBuy=false;
           // str.append(game.gameMethod.getStrValue("carderror_prompt_1"));
        }else if(cardType==11&&legion.getLegionRound()>0){//如果是科技卡牌,且研究回合>0,则显示round标记
            ifCanBuy=false;
            // str.append(game.gameMethod.getStrValue("carderror_prompt_2"));
        }else if(cardType==12&&legion.getTradeCount()==0){//如果是外交卡牌且交易次数为0,,则显示round标记
            ifCanBuy=false;
            //str.append(game.gameMethod.getStrValue("carderror_prompt_3"));
        }else if(build!=null&&build.getHexagonToRecruitCardTIA1(cardType,cardId)==-1  ){//如果是单位卡牌,判断是否有建造空间
            ifCanBuy=false;
            //str.append(game.gameMethod.getStrValue("carderror_prompt_4"));
        }
        if(ifCanBuy){
            if(GameMethod.cardIfMax(smap.getGame(),legion,build,cardId)){//判断卡牌是否超过等级上限
                ifCanBuy=false;
                //  str.append(game.gameMethod.getStrValue("carderror_prompt_5"));
            }else if(GameMethod.buildCardIfLock(smap.getGame(),legion,build,cardId)){//判断卡牌是否超过科技限制
                ifCanBuy=false;
                //str.append(game.gameMethod.getStrValue("carderror_prompt_6"));
            }
        }
        if(cardType==12){//外交卡牌显示的样式

            //int selectCardChance=cardE.getInt("tech",0);
            int selectCardFavorPrice=0;
            if(cardE.getInt("priceType",0)==0){

                //industry 为0的为普通卡牌,money=比例+基础,其他三项均为比例


                int money=ComUtil.limitValue(legion.getMoney()*cardE.getInt("moneyRateCost",0)/100+cardE.getInt("money",0),0,999);
                int industry=ComUtil.limitValue(legion.getIndustry()*cardE.getInt("industryRateCost",0)/100,0,999);
                int food=ComUtil.limitValue(legion.getFood()*cardE.getInt("foodRateCost",0)/100,0,999);
                int tech=ComUtil.limitValue(legion.getTech()*cardE.getInt("techRateCost",0)/100,0,999);

                if(ifCanBuy&&(money>legion.getMoney()||industry>legion.getIndustry()||tech>legion.getTech()||food>legion.getFood())){
                    ifCanBuy=false;
                    //str.append(game.gameMethod.getStrValue("carderror_prompt_7"));
                }



            }else {  //industry为1的为好感度卡牌,只moneyRateCost有效

                selectCardFavorPrice=cardE.getInt("moneyRateCost",0);


                if(ifCanBuy&&f.getFavorValue()<selectCardFavorPrice){
                    ifCanBuy=false;
                    //  str.append(game.gameMethod.getStrValue("carderror_prompt_7"));
                }
            }
        }else {

            //boolean  selectCard1CanBuild=false;
            //boolean   selectCard2CanBuild=false;
            //设置显示卡牌价格样式,价格
            // selectCardPotion = DefDAO.getCardIdByCardPotion(selectCardType,selectCardPotion);

            boolean ifConquest=game.getSMapDAO().masterData.getPlayerMode()==0;
            int money = cardE.getInt("money", 0)+legion.incomeMoney*cardE.getInt("moneyRateCost", 0)/100;
            int industry = cardE.getInt("industry", 0)+legion.incomeIndustry*cardE.getInt("industryRateCost", 0)/100;
            int tech = cardE.getInt("tech", 0)+legion.incomeTech*cardE.getInt("techRateCost", 0)/100;
            int  food= cardE.getInt("food", 0)+legion.incomeFood*cardE.getInt("foodRateCost", 0)/100;


            money= DefDAO.getCardPrice(legion,build,0,money,cardType,cardId,+GameMethod.getCardLv(legion,build,cardId),ifConquest);
            industry=DefDAO.getCardPrice(legion,build,1,industry,cardType,cardId,+GameMethod.getCardLv(legion,build,cardId),ifConquest);
            tech=DefDAO.getCardPrice(legion,build,2,tech,cardType,cardId,+GameMethod.getCardLv(legion,build,cardId),ifConquest);
            food=DefDAO.getCardPrice(legion,build,3,food,cardType,cardId,+GameMethod.getCardLv(legion,build,cardId),ifConquest);

            if(ifCanBuy&&(money>legion.getMoney()||industry>legion.getIndustry()||tech>legion.getTech()||food>legion.getFood())){
                ifCanBuy=false;
                // str.append(game.gameMethod.getStrValue("carderror_prompt_7"));
            }
        }
        return ifCanBuy;
    }
*/
    //只要有一张可以购买就返回true
    public boolean  ifCardListCanBuy(Fb2Smap smap,Array<XmlReader.Element> cardList, Fb2Smap.LegionData legion, Fb2Smap.BuildData build){
        for(int i=0;i<cardList.size;i++){
           /* if(ifCardCanBuy(smap,cardList.get(i),legion,build,f)){
                return true;
            }*/
            XmlReader.Element xmlE=cardList.get(i);
            if(xmlE!=null&&smap.canPayCardPrice(legion,build,xmlE.getInt("id",0))){
                return true;
            }
        }


        return false;
    }


    public boolean ifHaveLegionTechCardCanBuy(Fb2Smap smap, Fb2Smap.LegionData l) {
        if(game.sMapScreen!=null){
            Array<XmlReader.Element> haveCardEList=game.sMapScreen.haveCardEList;
            haveCardEList=GameMethod.getRandCardE(game,l,null, smap.publicLegionCanUpdTechId, 99, smap.masterData.getRoundNow(),haveCardEList,false);
            return ifCardListCanBuy(smap,haveCardEList,l,null);
        }
        return false;
    }


    public boolean ifCanUpdLegionGroupCard(Fb2Smap smap, Fb2Smap.LegionData l) {
        if(game.sMapScreen!=null){
            for(int i=3020;i<3030;i++) {
                if(smap.canPayCardPrice(l,null,i)){
                    return true;
                }
            }
        }
        return false;
    }

    // 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
    public int getTerrainEffect(XmlReader.Element terrainE, int armyType) {
        switch (armyType){
            case 1:return terrainE.getInt("defInf");
            case 2:return terrainE.getInt("defArt");
            case 3:return terrainE.getInt("defArm");
            case 5:
                return terrainE.getInt("defAir");
            case 4:
            case 8:return terrainE.getInt("defShip");
        }
        return 100;
    }

    public MainGame getGame(){
        return game;
    }








    //fcode规则 使用 二进制表达,即111111然后转二进制
    //fcode关于边界的规定
    //将e社的easyTechBorder转为我们想要的规定,注意转后记得更改图片顺序
    private int easyTechBorderTransFCode(int borderCode) {
        switch (borderCode) {
            case -1:
                return 0; //000000 即为完整海
            case 60:
                return 1; //000001 即右下角为陆
            case 56:
                return 2; //000010 即下面为陆
            case 52:
                return 3; //000011
            case 48:
                return 4; //000100
            case 44:
                return 5; //000101
            case 40:
                return 6; //000110
            case 36:
                return 7; //000111
            case 62:
                return 8; //001000
            case 58:
                return 9; //001001
            case 54:
                return 10; //001010
            case 50:
                return 11; //001011
            case 46:
                return 12; //001100
            case 42:
                return 13; //001101
            case 38:
                return 14; //001110
            case 34:
                return 15; //001111
            case 63:
                return 16; //010000
            case 59:
                return 17; //010001
            case 55:
                return 18; //010010
            case 51:
                return 19; //010011
            case 47:
                return 20; //010100
            case 43:
                return 21; //010101
            case 39:
                return 22; //010110
            case 35:
                return 23; //010111
            case 61:
                return 24; //011000
            case 57:
                return 25; //011001
            case 53:
                return 26; //011010
            case 49:
                return 27; //011011
            case 45:
                return 28; //011100
            case 41:
                return 29; //011101
            case 37:
                return 30; //011110
            case 33:
                return 31; //011111
            case 32:
                return 32; //100000
            case 28:
                return 33; //100001
            case 24:
                return 34; //100010
            case 20:
                return 35; //100011
            case 16:
                return 36; //100100
            case 12:
                return 37; //100101
            case 8:
                return 38; //100110
            case 4:
                return 39; //100111
            case 30:
                return 40; //101000
            case 26:
                return 41; //101001
            case 22:
                return 42; //101010
            case 18:
                return 43; //101011
            case 14:
                return 44; //101100
            case 10:
                return 45; //101101
            case 6:
                return 46; //101110
            case 2:
                return 47; //101111
            case 31:
                return 48; //110000
            case 27:
                return 49; //110001
            case 23:
                return 50; //110010
            case 19:
                return 51; //110011
            case 15:
                return 52; //110100
            case 11:
                return 53; //110101
            case 7:
                return 54; //110110
            case 3:
                return 55; //110111
            case 29:
                return 56; //111000
            case 25:
                return 57; //111001
            case 21:
                return 58; //111010
            case 17:
                return 59; //111011
            case 13:
                return 60; //111100
            case 9:
                return 61; //111101
            case 5:
                return 62; //111110
            case 1:
                return 63; //111111

        }
        return 0;
    }

    public static int getFBorderIdByString(String borderCode) {
        switch (borderCode) {
            case "000000":
                return 0; // 即为完整海
            case "000001":
                return 1; // 即右下角为陆
            case "000010":
                return 2; // 即下面为陆
            case "000011":
                return 3; //
            case "000100":
                return 4; //
            case "000101":
                return 5; //
            case "000110":
                return 6; //
            case "000111":
                return 7; //
            case "001000":
                return 8; //
            case "001001":
                return 9; //
            case "001010":
                return 10; //
            case "001011":
                return 11; //
            case "001100":
                return 12; //
            case "001101":
                return 13; //
            case "001110":
                return 14; //
            case "001111":
                return 15; //
            case "010000":
                return 16; //
            case "010001":
                return 17; //
            case "010010":
                return 18; //
            case "010011":
                return 19; //
            case "010100":
                return 20; //
            case "010101":
                return 21; //
            case "010110":
                return 22; //
            case "010111":
                return 23; //
            case "011000":
                return 24; //
            case "011001":
                return 25; //
            case "011010":
                return 26; //
            case "011011":
                return 27; //
            case "011100":
                return 28; //
            case "011101":
                return 29; //
            case "011110":
                return 30; //
            case "011111":
                return 31; //
            case "100000":
                return 32; //
            case "100001":
                return 33; //
            case "100010":
                return 34; //
            case "100011":
                return 35; //
            case "100100":
                return 36; //
            case "100101":
                return 37; //
            case "100110":
                return 38; //
            case "100111":
                return 39; //
            case "101000":
                return 40; //
            case "101001":
                return 41; //
            case "101010":
                return 42; //
            case "101011":
                return 43; //
            case "101100":
                return 44; //
            case "101101":
                return 45; //
            case "101110":
                return 46; //
            case "101111":
                return 47; //
            case "110000":
                return 48; //
            case "110001":
                return 49; //
            case "110010":
                return 50; //
            case "110011":
                return 51; //
            case "110100":
                return 52; //
            case "110101":
                return 53; //
            case "110110":
                return 54; //
            case "110111":
                return 55; //
            case "111000":
                return 56; //
            case "111001":
                return 57; //
            case "111010":
                return 58; //
            case "111011":
                return 59; //
            case "111100":
                return 60; //
            case "111101":
                return 61; //
            case "111110":
                return 62; //
            case "111111":
                return 63; //

        }
        return 0;
    }


    public static String transFBorderIdForString(int borderIdF) {
        switch (borderIdF) {
            case 0:
                return "000000";
            case 1:
                return "000001";
            case 2:
                return "000010";
            case 3:
                return "000011";
            case 4:
                return "000100";
            case 5:
                return "000101";
            case 6:
                return "000110";
            case 7:
                return "000111";
            case 8:
                return "001000";
            case 9:
                return "001001";
            case 10:
                return "001010";
            case 11:
                return "001011";
            case 12:
                return "001100";
            case 13:
                return "001101";
            case 14:
                return "001110";
            case 15:
                return "001111";
            case 16:
                return "010000";
            case 17:
                return "010001";
            case 18:
                return "010010";
            case 19:
                return "010011";
            case 20:
                return "010100";
            case 21:
                return "010101";
            case 22:
                return "010110";
            case 23:
                return "010111";
            case 24:
                return "011000";
            case 25:
                return "011001";
            case 26:
                return "011010";
            case 27:
                return "011011";
            case 28:
                return "011100";
            case 29:
                return "011101";
            case 30:
                return "011110";
            case 31:
                return "011111";
            case 32:
                return "100000";
            case 33:
                return "100001";
            case 34:
                return "100010";
            case 35:
                return "100011";
            case 36:
                return "100100";
            case 37:
                return "100101";
            case 38:
                return "100110";
            case 39:
                return "100111";
            case 40:
                return "101000";
            case 41:
                return "101001";
            case 42:
                return "101010";
            case 43:
                return "101011";
            case 44:
                return "101100";
            case 45:
                return "101101";
            case 46:
                return "101110";
            case 47:
                return "101111";
            case 48:
                return "110000";
            case 49:
                return "110001";
            case 50:
                return "110010";
            case 51:
                return "110011";
            case 52:
                return "110100";
            case 53:
                return "110101";
            case 54:
                return "110110";
            case 55:
                return "110111";
            case 56:
                return "111000";
            case 57:
                return "111001";
            case 58:
                return "111010";
            case 59:
                return "111011";
            case 60:
                return "111100";
            case 61:
                return "111101";
            case 62:
                return "111110";
            case 63:
                return "111111";
        }
        return "000000";
    }



    public static int getFBorderId(int borderCode) {
        switch (borderCode) {
            case 0:
                return 0; // 即为完整海
            case 1:
                return 1; // 即右下角为陆
            case 10:
                return 2; // 即下面为陆
            case 11:
                return 3; //
            case 100:
                return 4; //
            case 101:
                return 5; //
            case 110:
                return 6; //
            case 111:
                return 7; //
            case 1000:
                return 8; //
            case 1001:
                return 9; //
            case 1010:
                return 10; //
            case 1011:
                return 11; //
            case 1100:
                return 12; //
            case 1101:
                return 13; //
            case 1110:
                return 14; //
            case 1111:
                return 15; //
            case 10000:
                return 16; //
            case 10001:
                return 17; //
            case 10010:
                return 18; //
            case 10011:
                return 19; //
            case 10100:
                return 20; //
            case 10101:
                return 21; //
            case 10110:
                return 22; //
            case 10111:
                return 23; //
            case 11000:
                return 24; //
            case 11001:
                return 25; //
            case 11010:
                return 26; //
            case 11011:
                return 27; //
            case 11100:
                return 28; //
            case 11101:
                return 29; //
            case 11110:
                return 30; //
            case 11111:
                return 31; //
            case 100000:
                return 32; //
            case 100001:
                return 33; //
            case 100010:
                return 34; //
            case 100011:
                return 35; //
            case 100100:
                return 36; //
            case 100101:
                return 37; //
            case 100110:
                return 38; //
            case 100111:
                return 39; //
            case 101000:
                return 40; //
            case 101001:
                return 41; //
            case 101010:
                return 42; //
            case 101011:
                return 43; //
            case 101100:
                return 44; //
            case 101101:
                return 45; //
            case 101110:
                return 46; //
            case 101111:
                return 47; //
            case 110000:
                return 48; //
            case 110001:
                return 49; //
            case 110010:
                return 50; //
            case 110011:
                return 51; //
            case 110100:
                return 52; //
            case 110101:
                return 53; //
            case 110110:
                return 54; //
            case 110111:
                return 55; //
            case 111000:
                return 56; //
            case 111001:
                return 57; //
            case 111010:
                return 58; //
            case 111011:
                return 59; //
            case 111100:
                return 60; //
            case 111101:
                return 61; //
            case 111110:
                return 62; //
            case 111111:
                return 63; //
        }
        return 0;
    }


    public static int transFBorderId(int borderIdF) {
        switch (borderIdF) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 10;
            case 3:
                return 11;
            case 4:
                return 100;
            case 5:
                return 101;
            case 6:
                return 110;
            case 7:
                return 111;
            case 8:
                return 1000;
            case 9:
                return 1001;
            case 10:
                return 1010;
            case 11:
                return 1011;
            case 12:
                return 1100;
            case 13:
                return 1101;
            case 14:
                return 1110;
            case 15:
                return 1111;
            case 16:
                return 10000;
            case 17:
                return 10001;
            case 18:
                return 10010;
            case 19:
                return 10011;
            case 20:
                return 10100;
            case 21:
                return 10101;
            case 22:
                return 10110;
            case 23:
                return 10111;
            case 24:
                return 11000;
            case 25:
                return 11001;
            case 26:
                return 11010;
            case 27:
                return 11011;
            case 28:
                return 11100;
            case 29:
                return 11101;
            case 30:
                return 11110;
            case 31:
                return 11111;
            case 32:
                return 100000;
            case 33:
                return 100001;
            case 34:
                return 100010;
            case 35:
                return 100011;
            case 36:
                return 100100;
            case 37:
                return 100101;
            case 38:
                return 100110;
            case 39:
                return 100111;
            case 40:
                return 101000;
            case 41:
                return 101001;
            case 42:
                return 101010;
            case 43:
                return 101011;
            case 44:
                return 101100;
            case 45:
                return 101101;
            case 46:
                return 101110;
            case 47:
                return 101111;
            case 48:
                return 110000;
            case 49:
                return 110001;
            case 50:
                return 110010;
            case 51:
                return 110011;
            case 52:
                return 110100;
            case 53:
                return 110101;
            case 54:
                return 110110;
            case 55:
                return 110111;
            case 56:
                return 111000;
            case 57:
                return 111001;
            case 58:
                return 111010;
            case 59:
                return 111011;
            case 60:
                return 111100;
            case 61:
                return 111101;
            case 62:
                return 111110;
            case 63:
                return 111111;
        }
        return 0;
    }


    //useCards为可对这个国家使用的card,scardIds为策略卡
    //从两个intArray中寻找交集,并随机获取数值
    public int getRandOne(IntArray useCards,IntArray sCardIds) {
        IntArray rs=game.tempUtil.getTempIntArray();
        for(int i=0;i<useCards.size;i++){
            int c=useCards.get(i);
            if(sCardIds.contains(c)){
                rs.add(c);
            }
        }
        int cardId=-1;
        if(rs.size>0){
            cardId=rs.random();
        }
        game.tempUtil.disposeTempIntArray(rs);
        return cardId;
    }

    public int getRandOneForUseCard( Array<XmlReader.Element> useCards,IntArray sCardIds) {
        IntArray rs=game.tempUtil.getTempIntArray();
        for(int i=0;i<useCards.size;i++){
            int c=useCards.get(i).getInt("id",-1);
            if(c!=-1&&sCardIds.contains(c)){
                rs.add(c);
            }
        }
        int cardId=-1;
        if(rs.size>0){
            cardId=rs.random();
        }
        game.tempUtil.disposeTempIntArray(rs);
        return cardId;
    }

    //type 0随机 1最大攻击 -1最小攻击
    public int getUnitDamage(Fb2Smap.LegionData legionData, Fb2Smap.BuildData buildData,int armyId, int type){
        if(armyId==0){
            return 0;
        }
        XmlReader.Element aE=  game.gameConfig.getDEF_ARMY().getElementById(armyId);
        int d=1;
        if(aE!=null){
            int addDamage=0;
            if(buildData!=null&&buildData.getLegionIndex()==legionData.getLegionIndex()&&buildData.getBuildWonder()!=0){
                XmlReader.Element xE=game.gameConfig.getDEF_WONDER().getElementById(buildData.getBuildWonder());
                if(xE!=null){
                    int function=xE.getInt("fucntion",0);
                    int effect=xE.getInt("effect",0);
                    int value=xE.getInt("value",0);
                    if(function==5&&effect==3){
                        addDamage=value;
                    }
                }
            }
            int attackRate= (int) (game.resGameConfig.allDamageRate*100);
            if(legionData.isPlayer()){
                if(legionData.getCM()==1){
                    attackRate*=5;
                }else if(game.gameConfig.ifLeisureMode){
                    attackRate*=2;
                }
            }

            if(legionData.getSpiritMap()!=null&&legionData.ifEffective(16)){
                if(legionData.getSpiritMap().containsKey(41)){
                    attackRate=attackRate*2;
                }
            }
            int armyType=aE.getInt("type",-1);
            switch (armyType){
                case 1://步兵
                    addDamage=addDamage+getCardLv(legionData,null,3006);
                    if(type==1){
                        return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                    }else if(type==-1){
                        return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                    }else if(type==0){
                        return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                    }
                    break;
                case 2://炮兵
                    addDamage=addDamage+getCardLv(legionData,null,3007);
                    if(type==1){
                        return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                    }else if(type==-1){
                        return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                    }else if(type==0){
                        return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                    }
                    break;
                case 3://装甲
                    addDamage=addDamage+getCardLv(legionData,null,3008);
                    if(type==1){
                        return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                    }else if(type==-1){
                        return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                    }else if(type==0){
                        return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                    }
                    break;
                case 4://舰船
                    addDamage=addDamage+getCardLv(legionData,null,3010);
                    if(type==1){
                        return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                    }else if(type==-1){
                        return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                    }else if(type==0){
                        return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                    }
                    break;
                case 5://空军
                    addDamage=addDamage+getCardLv(legionData,null,3011);
                    if(type==1){
                        return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                    }else if(type==-1){
                        return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                    }else if(type==0){
                        return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                    }
                    break;
                case 6://要塞
                    if(armyId==1606){
                        addDamage=addDamage+getCardLv(legionData,null,3014);
                        if(type==1){
                            return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                        }else if(type==-1){
                            return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                        }else if(type==0){
                            return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                        }
                    }else{
                        addDamage=addDamage+getCardLv(legionData,null,3009);
                        if(type==1){
                            return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                        }else if(type==-1){
                            return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                        }else if(type==0){
                            return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                        }
                    }
                    break;
                case 7://弹头
                    addDamage=addDamage+getCardLv(legionData,null,3015)*2;
                    if(type==1){
                        return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                    }else if(type==-1){
                        return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                    }else if(type==0){
                        return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                    }
                    break;
                case 8://潜艇
                    addDamage=addDamage+getCardLv(legionData,null,3010);
                    if(type==1){
                        return (aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
                    }else if(type==-1){
                        return (aE.getInt("minAtk",0)+addDamage)*attackRate/100;
                    }else if(type==0){
                        return (ComUtil.getRandom(aE.getInt("minAtk",0),aE.getInt("maxAtk",0))+addDamage)*attackRate/100;
                    }
                    break;
            }
        }
        return d;
    }



    public int getUnitDamageByMorale(Fb2Smap.LegionData legionData, Fb2Smap.BuildData buildData,int armyId, int morale){
        if(armyId==0){
            return 0;
        }
        XmlReader.Element aE=  game.gameConfig.getDEF_ARMY().getElementById(armyId);
        int d=1;
        if(aE!=null){
            int addDamage=0;
            if(buildData!=null&&buildData.getLegionIndex()==legionData.getLegionIndex()&&buildData.getBuildWonder()!=0){
                XmlReader.Element xE=game.gameConfig.getDEF_WONDER().getElementById(buildData.getBuildWonder());
                if(xE!=null){
                    int function=xE.getInt("fucntion",0);
                    int effect=xE.getInt("effect",0);
                    int value=xE.getInt("value",0);
                    if(function==5&&effect==3){
                        addDamage=value;
                    }
                }
            }
            int attackRate= (int) (game.resGameConfig.allDamageRate*100);
            if(legionData.isPlayer()){
                if(legionData.getCM()==1){
                    attackRate*=5;
                }else if(game.gameConfig.ifLeisureMode){
                    attackRate*=2;
                }
            }

            if(legionData.getSpiritMap()!=null&&legionData.ifEffective(16)){
                if(legionData.getSpiritMap().containsKey(41)){
                    attackRate=attackRate*2;
                }
            }
            int armyType=aE.getInt("type",-1);
            switch (armyType){
                case 1://步兵
                    addDamage=addDamage+getCardLv(legionData,null,3006);
                    break;
                case 2://炮兵
                    addDamage=addDamage+getCardLv(legionData,null,3007);
                    break;
                case 3://装甲
                    addDamage=addDamage+getCardLv(legionData,null,3008);
                    break;
                case 4://舰船
                    addDamage=addDamage+getCardLv(legionData,null,3010);
                    break;
                case 5://空军
                    addDamage=addDamage+getCardLv(legionData,null,3011);
                    break;
                case 6://要塞
                    if(armyId==1606){
                        addDamage=addDamage+getCardLv(legionData,null,3014);
                    }else{
                        addDamage=addDamage+getCardLv(legionData,null,3009);
                    }
                    break;
                case 7://弹头
                    addDamage=addDamage+getCardLv(legionData,null,3015)*2;
                    break;
                case 8://潜艇
                    addDamage=addDamage+getCardLv(legionData,null,3010);
                    break;
            }
            int baseDamage=(aE.getInt("minAtk",0)+addDamage)*attackRate/100;
            int maxDamage=(aE.getInt("maxAtk",0)+addDamage)*attackRate/100;
            return baseDamage+(maxDamage-baseDamage)*morale/100;
        }
        return d;
    }


    //type 0随机 1最大攻击 -1最小攻击 2平均攻击
    public int getUnitGroupUnitDamage(Fb2Smap.LegionData legionData,  Fb2Smap.ArmyData army, int type,int targetHexagon){
        int direct=army.getDirectByBorderId(targetHexagon);

        int attackRate= (int) (game.resGameConfig.allDamageRate*100);
        if(legionData.getSpiritMap()!=null&&legionData.ifEffective(16)){
            if(legionData.getSpiritMap().containsKey(41)){
                attackRate=attackRate*2;
            }
        }
        if(legionData.isPlayer()){
            if(legionData.getCM()==1){
                attackRate*=5;
            }else if(game.gameConfig.ifLeisureMode){
                attackRate*=2;
            }
        }
        //-2 最大最小攻击 -1 自身  0 未知 1左上 2上 3左下 4左下 5下 6右下
        if(direct==-2){
            if(type==2){
                return (army.armyMinAttack+army.armyMaxAttack)*attackRate/200;
            }else if(type==1){
                return army.armyMaxAttack*attackRate/100;
            }else if(type==-1){
                return army.armyMinAttack*attackRate/100;
            }else if(type==0){
                return ComUtil.getRandom(army.armyMinAttack,army.armyMaxAttack)*attackRate/100;
            }
        }else  if(direct==0){
            if(type==2){//平均攻击
                return (army.getUnitGroupAvgAttack(0))*attackRate/100;
            }else if(type==1){//最大攻击
                return (army.getUnitGroupMaxAttack(0))*attackRate/100;
            }else if(type==-1){//最小攻击
                return (army.getUnitGroupMinAttack(0))*attackRate/100;
            }else if(type==0){//随机
                return (army.getUnitGroupRandomAttack(0))*attackRate/100;
            }
        }else if(direct==-1){//远程
            return (army.getUnitGroupAttackForRange(targetHexagon,type))*attackRate/100;
        }else{
            if(type==0){
                return (army.getArmyFormationAvgAttack(direct))*attackRate/100;
            }else if(type==1){
                return (army.getArmyFormationMaxAttack(direct))*attackRate/100;
            }else if(type==-1){
                return (army.getArmyFormationMinAttack(direct))*attackRate/100;
            }else if(type==0){
                return (army.getArmyFormationRandomAttack(direct))*attackRate/100;
            }
        }
        return 1;
    }

    //type 0随机 1最大攻击 -1最小攻击 2平均攻击
    public int getUnitGroupUnitDamageByMorale(Fb2Smap.LegionData legionData,  Fb2Smap.ArmyData army, int morale,int targetHexagon){
        int direct=army.getDirectByBorderId(targetHexagon);

        int attackRate= (int) (game.resGameConfig.allDamageRate*100);
        if(legionData.getSpiritMap()!=null&&legionData.ifEffective(16)){
            if(legionData.getSpiritMap().containsKey(41)){
                attackRate=attackRate*2;
            }
        }
        if(legionData.isPlayer()){
            if(legionData.getCM()==1){
                attackRate*=5;
            }else if(game.gameConfig.ifLeisureMode){
                attackRate*=2;
            }
        }
        //-2 最大最小攻击 -1 自身  0 未知 1左上 2上 3左下 4左下 5下 6右下

        int minDamage=0;
        int maxDamage=0;
        if(direct==-2){
            maxDamage= army.armyMaxAttack*attackRate/100;
            minDamage= army.armyMinAttack*attackRate/100;
        }else  if(direct==0){
            maxDamage= (army.getUnitGroupMaxAttack(0))*attackRate/100;
            minDamage= (army.getUnitGroupMinAttack(0))*attackRate/100;
        }else if(direct==-1){//远程
            maxDamage= (army.getUnitGroupAttackForRange(targetHexagon,1))*attackRate/100;
            minDamage= (army.getUnitGroupAttackForRange(targetHexagon,-1))*attackRate/100;
        }else{
            maxDamage= (army.getArmyFormationMaxAttack(direct))*attackRate/100;
            minDamage= (army.getArmyFormationMinAttack(direct))*attackRate/100;
        }
        return minDamage+(maxDamage-minDamage)*morale/100;
    }

    public String getUnitTypeForFeatureStr(Fb2Smap.ArmyData army) {
        if(army.getUnitArmyId0()==1606){
            return getStrValueT("feature_missile");
        }else if(army.getUnitArmyId0()==1301){
            return getStrValueT("feature_panzer");
        }
        switch (army.getArmyType()){//兵种类型  1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
            case 1:return getStrValueT("feature_infantry");
            case 2:return getStrValueT("feature_artillery");
            case 3:return getStrValueT("feature_tank");
            case 4:return getStrValueT("feature_ship");
            case 5:return getStrValueT("feature_air");
            case 6:return getStrValueT("feature_fort");
            case 7:return getStrValueT("feature_nuclear");
            case 8:return getStrValueT("feature_submarine");
        }
        return "";
    }

    public String getUnitFeatureStr(Fb2Smap.ArmyData armyData, int index) {
        if(armyData.armyXmlE0 !=null){
            String[] str=armyData.armyXmlE0.get("feature").split(",");
            if(index<str.length&&ComUtil.isNumeric(str[index])){
                int featureId=Integer.parseInt(str[index]);
                if(armyData.ifHaveFeature(featureId)){
                    if(ifUnitFeatureCanUpd(featureId)){
                        return  getStrValueT("feature_name_"+featureId)+":"+getStrValueT("feature_info_"+featureId+"_2",getUnitFeatureEffect(featureId, armyData.getFeatureLv(featureId)));
                    }else {
                        return  getStrValueT("feature_name_"+featureId)+":"+getStrValueT("feature_info_"+featureId);
                    }
                }else {
                    return  getStrValueT("feature_name_"+featureId)+":"+getStrValueT("feature_info_inactive");
                }
            }
        }
        return "";
    }
    public String getUnitFeatureStr(Fb2Smap.AirData airData, int index) {
        if(airData.airXmlE!=null){
            String[] str=airData.airXmlE.get("feature").split(",");
            if(index<str.length&&ComUtil.isNumeric(str[index])){
                int featureId=Integer.parseInt(str[index]);
                if(airData.ifHaveAirFeature(featureId)){
                    if(ifUnitFeatureCanUpd(featureId)){
                        return getStrValueT("feature_name_"+featureId)+":"+getStrValueT("feature_info_"+featureId+"_2",getUnitFeatureEffect(featureId, airData.getAirFeatureLv(featureId)));
                    }else {
                        return  getStrValueT("feature_name_"+featureId)+":"+getStrValueT("feature_info_"+featureId);
                    }
                }else {
                    return  getStrValueT("feature_name_"+featureId)+":"+getStrValueT("feature_info_inactive");
                }
            }
        }
        return "";
    }


    public String getUnitSkillStr(Fb2Smap.ArmyData armyData, int index) {
        int skillId=armyData.getSkillIdByIndex(index);
        if(skillId>0){
            return  getStrValueT("skill_name_"+skillId)+":"+getStrValueT("skill_info_"+skillId,armyData.getSkillEffect(skillId),armyData.getSkillChance(skillId));
        }
        return "";
    }


    public String getUnitSkillStr(Fb2Smap.AirData air, int index) {
        int skillId=air.getSkillIdByIndex(index);
        if(skillId>0){
            return  getStrValueT("skill_name_"+skillId)+":"+getStrValueT("skill_info_"+skillId,air.getSkillEffect(skillId),air.getSkillChance(skillId));
        }
        return "";
    }

    public boolean ifCanUpdSkillLv(int skillId, int skillLv) {
        XmlReader.Element xmlE=game.gameConfig.getDEF_SKILL().getElementById(skillId);
        if(xmlE==null){
            return false;
        }
        if(skillLv>=game.resGameConfig.skillMaxLv|| !xmlE.getBoolean("ifUpd",true) ){
            return false;
        }
        return true;
    }



    //获取功能设置
    public String getChiefBuffStr(Fb2Smap sMapDAO, int chiefType,int chiefValue,boolean ifHasSelect) {
        //int chiefValue=sMapDAO.chiefData.getChiefValue(chiefType);
        XmlReader.Element xE=null;
        Array<XmlReader.Element> pEs=game.gameConfig.getCONFIG_CHIEFBUFF().e.getChildrenByName("chiefBuff");
        int c=11;
        for(int i=0;i<pEs.size;i++){
            XmlReader.Element pE=pEs.get(i);
            if(pE.getInt("type",0)==chiefType&&pE.getInt("index")==chiefValue){
                xE=pE;
                break;
            }
        }
        if(xE!=null){
            /*chief_effect_general1=月金钱收益{0} moneyChange
                    chief_effect_general2=月稳定度{0} stabilityChange
                    chief_effect_general3=月和谐度{0} harmonyChange
                    chief_effect_general4=和谐度消耗{0} harmonyCost*/
            StringBuilder sb=new StringBuilder();
            int v=xE.getInt("moneyChange",0);
            if(v!=0){
                sb.append(getStrValue("chief_effect_general1",ComUtil.getSymbolNumer(v)));
            }
            v=xE.getInt("stabilityChange",0);
            if(v!=0){
                if(sb.length>0){sb.append(",");}
                sb.append(getStrValue("chief_effect_general2",ComUtil.getSymbolNumer(v)));
            }
            v=xE.getInt("harmonyChange",0);
            if(v!=0){
                if(sb.length>0){sb.append(",");}
                sb.append(getStrValue("chief_effect_general3",ComUtil.getSymbolNumer(v)));
            }
            if(ifHasSelect){//变更损耗
                v=xE.getInt("harmonyLoss",0);
                if(v!=0){
                    if(sb.length>0){sb.append(",");}//变更点数损耗{0}
                    sb.append(getStrValue("chief_effect_general4",xE.getInt("harmonyLoss",0),ComUtil.getSymbolNumer(v)));
                }
            }else{//政策价格
                v=xE.getInt("harmonyCost",0);
                if(v!=0){
                    if(sb.length>0){sb.append(",");}//变革点数花费{0}
                    sb.append(getStrValue("chief_effect_general5",xE.getInt("harmonyCost",0),ComUtil.getSymbolNumer(v)));
                }
            }

            v=xE.getInt("specialEffectValue",0);
            String str=getStrValue("chief_effect_"+chiefType+"_"+chiefValue,ComUtil.getSymbolNumer(v),v-game.resGameConfig.cityStabilityChangeLimit,v+game.resGameConfig.cityStabilityChangeLimit);
            if(!ComUtil.isEmpty(str)){
                if(sb.length>0){sb.append(",");}
                sb.append(str);
            }
            if(sb.length>0){
                sb.insert(0,getStrValue("chiefName_"+chiefType+"_"+chiefValue)+":");
            }

            return sb.toString();
        }else{
            return "";
        }
    }
    //123 当前政治点{0},月政治点{1},当前稳定度{2},月稳定度{3},金钱效率{4}%,资源效率{5}%,部队恢复效率{6}%,食物消耗{7}%,区域稳定度低于{8}时,增加{9}~{10}稳定度,战争支持度{11},识字率{12}%
    public String getChiefStateStr(Fb2Smap btl) {
        Fb2Smap.LegionData l=btl.getPlayerLegionData();

  /*      Array<XmlReader.Element> pEs=game.gameConfig.getCONFIG_CHIEFBUFF().e.getChildrenByName("chiefBuff");
        int c=11;
        for(int i=0;i<pEs.size;i++){
            XmlReader.Element pE=pEs.get(i);
            if(pE.getInt("type",0)==chiefType&&pE.getInt("index")==chiefValue){
                xE=pE;
                break;
            }
        }

      int  v=xE.getInt("specialEffectValue",0);
        String str=getStrValue("chief_effect_"+chiefType+"_"+chiefValue,ComUtil.getSymbolNumer(v),v-game.resGameConfig.cityStabilityChangeLimit,v+game.resGameConfig.cityStabilityChangeLimit);
      */
        return getStrValue("chief_state0",btl.getChiefHarmony(),ComUtil.getSymbolNumer(btl.chiefData.varHarmonyChange),l.getStability(),ComUtil.getSymbolNumer(btl.chiefData.varLegionStabilityChange),ComUtil.getSymbolNumer(btl.chiefData.varMoneyEfficiencyChange),
                ComUtil.getSymbolNumer(btl.chiefData.varResEfficiencyChange),ComUtil.getSymbolNumer(btl.chiefData.varUnitRestoreEfficiencyChange),ComUtil.getSymbolNumer(btl.chiefData.varUnitFoodCoastChange),
                game.resGameConfig.cityStabilityChangeValueMax,(btl.chiefData.vaAreaStabilityChange-game.resGameConfig.cityStabilityChangeLimit),(btl.chiefData.vaAreaStabilityChange+game.resGameConfig.cityStabilityChangeLimit),
                ComUtil.getSymbolNumer(btl.chiefData.varWarSupportChange),btl.chiefData.getLiteracy()
        )+"\n"+btl.chiefData.getChiefInfoStr(1)
                +"\n"+btl.chiefData.getChiefInfoStr(2);
              /*  +"\n"+getStrValue("chief_effect_"+1+"_"+btl.chiefData.getChiefValue(1))
                +"\n"+getStrValue("chief_effect_"+2+"_"+btl.chiefData.getChiefValue(2));*/
    }


    public String getArmyFormationInfoStr(XmlReader.Element xmlE) {

        StringBuilder sb=new StringBuilder();
        int tv=xmlE.getInt("MDB",0);
        if(tv!=0){
            sb.append(getStrValue("armyFormationInfo_MDB",ComUtil.getSymbolNumer(tv)));
        }
        tv=xmlE.getInt("FDB",0);
        if(tv!=0){
            if(sb.length>0){sb.append(",");}
            sb.append(getStrValue("armyFormationInfo_FDB",ComUtil.getSymbolNumer(tv)));
        }
        tv=xmlE.getInt("RDB",0);
        if(tv!=0){
            if(sb.length>0){sb.append(",");}
            sb.append(getStrValue("armyFormationInfo_RDB",ComUtil.getSymbolNumer(tv)));
        }
        tv=xmlE.getInt("RCC",0);
        if(tv!=0){
            if(sb.length>0){sb.append(",");}
            sb.append(getStrValue("armyFormationInfo_RCC",ComUtil.getSymbolNumer(tv)));
        }
        tv=xmlE.getInt("ADB",0);
        if(tv!=0){
            if(sb.length>0){sb.append(",");}
            sb.append(getStrValue("armyFormationInfo_ADB",ComUtil.getSymbolNumer(tv)));
        }
        return sb.toString();
    }


    public  String formmatStringForSpace(Label label,String str, int length, boolean ifLeft){
        float base=getCharLengthBySpace(label,ResDefaultConfig.StringName.space1);
        int num = Math.round (getCharLengthBySpace(label,str)/base);
         length= Math.round (getCharLengthBySpace(label,ComUtil.getSpace(length))/base);
        if (num == length) {
            return str;
        } else if (num< length) {
            for (int i = 0, iMax = length - num; i < iMax; i++) {
                if (ifLeft) {
                    str = ResDefaultConfig.StringName.space1 + str;
                } else {
                    str = str + ResDefaultConfig.StringName.space1;
                }
            }
           /* if(num==2){
                if (ifLeft) {
                    str = ResDefaultConfig.StringName.space1 + str;
                } else {
                    str = str + ResDefaultConfig.StringName.space1;
                }
            }*/
            Gdx.app.log("formmatString:"+ num+" "+(length - num),str);
            return str;
        } else {
            return ComUtil.formmatString(str,length,ifLeft,ResDefaultConfig.StringName.space1);
        }
    }


    private float getCharLengthBySpace(Label label,String str) {
        label.setText(str);
        return label.getPrefWidth();
    }
    /*private int getCharLength(String str) {
        int rs=0;
        //str.length()*game.gameConfig.spaceNumberWidth;
        for (int i = 0; i < str.length(); i++) {
            char tmp = str.charAt(i);
              if (tmp == ' ') {
                rs +=1;
            }else if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) {
                rs +=game.gameConfig.letterWidth;
            } else if ((tmp >= '0') && (tmp <= '9')) {
                rs +=game.gameConfig.numberWidth;
            } else if (ComUtil.isCnSymbol(tmp)) {
                rs +=4;
            } else if (ComUtil.isEnSymbol(tmp)) {
                rs +=2;
            }else {//是否语言 TODO
                rs +=4;
            }
        }
        return rs;
    }*/
   /* private int getCharLengthBySpace(String str) {
        int rs=0;
       float base=GameUtil.getTextWidth(game.gameConfig.gameFont,ComUtil.getSpace(1));
        for (int i = 0; i < str.length(); i++) {
           String str1 = str.substring(i,i+1);
            rs+=GameUtil.getTextWidth(game.gameConfig.gameFont,str1)/base;
        }
        return rs;
    }*/
    //拥有的特性
    public String getUnitFeatureStrs(String str){
        if(ComUtil.isEmpty(str)){
            return getStrValue("feature_name_0");
        }
        String[] featureStrs=str.split(",");
        StringBuilder sb=new StringBuilder();
        if(featureStrs.length>0){
            for(int i=0,iMax=featureStrs.length;i<iMax;i++){
                if(sb.length>0){
                    sb.append(",");
                }
               sb.append(getStrValue("feature_name_"+featureStrs[i]));
            }
        }
        return sb.toString();
    }

    public String getSkillStr(int i) {
        return getStrValue("skill_name_"+i);
    }


    public String getFeatureStr(int i) {
        return getStrValue("feature_name_"+i);
    }

   /* //type 0 title ,1 content 感觉没什么必要
    public String getScriptEventStr(int scriptEvent,int type,XmlReader.Element xmlE,Fb2Smap fb2Smap){
        switch (scriptEvent){
            case 0://
                if(type==0){//title {0}的邀请

                }else{//content 是否加入{0}军队

                }
            break;
            case 1://
                if(type==0){//title 要求{0}划分边界

                }else{//content 是否要求清国与我们明确边界划分

                }
                break;
            case 2://
                if(type==0){//title 同意{0}划分边界

                }else{//content 是否同意{0}的边界划分的要求

                }
                break;
            case 3://
                if(type==0){//title

                }else{//content

                }
                break;
            case 4://
                if(type==0){//title {0}的联合

                }else{//content {1}想要联合各德意志邦国形成以其为主导的国家,是否同意

                }
                break;
            case 5://
                if(type==0){//title

                }else{//content

                }
                break;
            case 6://
                if(type==0){//title

                }else{//content

                }
                break;
            case 7://
                if(type==0){//title

                }else{//content

                }
                break;


        }


        return null;
    }*/
}
