package com.zhfy.game.framework;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.XmlIntDAO;
import com.zhfy.game.model.content.btl.BtlModule;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.model.framework.TextureRegionListDAO;
import com.zhfy.game.screen.GeneralScreen;
import com.zhfy.game.screen.SMapScreen;
import com.zhfy.game.screen.actor.framework.WindowGroup;
import com.zhfy.game.screen.stage.SMapGameStage;

import java.util.Iterator;

public class GameLayout {
    //本工具类主要存储涉及到libgdx的ui自定义组件
    public MainGame game;


    public GameLayout(MainGame game){
        this.game=game;
    }

    public  void updIntroductionWindow(WindowGroup w, Fb2Smap smap, boolean ifPrompt) {
        // w.setImageRegion(1,game.getImgLists().getTextureByName(DefDAO.getImageNameForPromptTitle(promptTitleType)).getTextureRegion());
        // w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(game.getSMapDAO().getCountryIdByli(li))).getTextureRegion());
        // w.setLabelText(0,promptText);
        String title,  detail,  effect;
        if(ifPrompt&&smap.promptDatas!=null&&smap.promptDatas.size>0){
            Fb2Smap.PromptData p=smap.promptDatas.get(0);
            title=p.getTitle();
            detail=p.getContent();
            effect=p.getEffect();
            if(p.getType()==0){
                //w.hidImage(1);
                //w.hidImage(2);
                if(p.getLi()==-1||p.getLi()==0||p.getLi2()==-1||p.getLi2()==0){
                    w.hidImage(1);
                    w.hidImage(2);
                }else{
                    w.setImageRegion(1,game.getImgLists().getTextureByName("flag_"+game.getSMapDAO().getLegionDataByLi(p.getLi()).getCountryId()));
                    w.setImageRegion(2,game.getImgLists().getTextureByName("flag_"+game.getSMapDAO().getLegionDataByLi(p.getLi2()).getCountryId()));
                }
                w.hidButton(2);
                w.showButton(0);
            }else{
                Gdx.app.log("test updPromptWindow",p.toString());
                if(p.getLi()==-1){
                    w.hidImage(1);
                    w.hidImage(2);
                }else{
                    w.setImageRegion(1,game.getImgLists().getTextureByName("flag_"+game.getSMapDAO().getPlayerLegionData().getCountryId()));
                    w.setImageRegion(2,game.getImgLists().getTextureByName("flag_"+game.getSMapDAO().getLegionDataByLi(p.getLi()).getCountryId()));
                }
                w.hidButton(0);
                w.showButton(2);
            }
            w.hidButton(3);
        }else {
            w.showButton(3);
            if(smap.stageId==0){//争霸
                if(smap.masterData.getWarId()==0){
                    title=smap.masterData.getBeginYear()+"-"+smap.getPlayerLegionData().legionName;
                }else{
                    title= game.gameMethod.getStrValue("warName_"+smap.masterData.getWarId())+"-"+smap.getPlayerLegionData().legionName;
                }
                detail=smap.getTargetStr();
                Label l=  w.getLabel(1);
                if(l!=null){
                    l.setScale(2.0f);
                }
                effect=game.gameMethod.getStrValueT("prompt_gameend",smap.getRemainRound());
            }else  if(smap.masterData.getBtlType()==0){//征服
                title=smap.getPlayerLegionData().legionName;
                detail=smap.getPlayerInfo();
                effect=game.gameMethod.getStrValueT("prompt_gameend",smap.getRemainRound());
            }else if(smap.masterData.getBtlType()==1){//帝国
                title=game.gameMethod.getStrValueT(new StringBuilder("stage_name_").append(smap.stageId).append(ComUtil.formmatNumber(smap.masterData.getGameEpisode(),2,true,"0")).toString());
                detail=game.gameMethod.getStrValueT(new StringBuilder("stage_content_").append(smap.stageId).append(ComUtil.formmatNumber(smap.masterData.getGameEpisode(),2,true,"0")).toString());
                effect=smap.getTargetStr()+"  "+game.gameMethod.getStrValueT("prompt_gameend",+smap.getRemainRound());
            }else{//其他
                title=smap.getStageName();
                detail=game.gameMethod.getStrValueT("stage_content_"+smap.stageId);
                effect=game.gameMethod.getStrValueT("prompt_gameend",smap.getRemainRound());
            }
            w.hidImage(1);
            w.hidImage(2);
            w.hidButton(2);
            w.showButton(0);
        }
        w.setLabelText(0,title);
        w.setScrollLabel(1,detail);
        w.setLabelText(2,effect,true);

    }




    public  void updEffectPromptWindow( WindowGroup w, int li, int promptTitleType, String promptText) {
        w.setImageRegion(1,game.getImgLists().getTextureByName(DefDAO.getImageNameForPromptTitle(promptTitleType)).getTextureRegion());
        w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(game.getSMapDAO().getCountryIdByli(li))).getTextureRegion());
        w.setLabelText(0,promptText);
    }
    public  void updTechWindow(WindowGroup w, SMapGameStage.SMapPublicRescource res, Fb2Smap.LegionData l) {
        Fb2Smap.ArmyData a=l.getCapitalArmyData();
        w.setImageRegionNotChangeSize(1,game.getFlagBgTextureRegion(l.getCountryId()));
        Fb2Smap.GeneralData g;
        if(a!=null){
            g=a.getGeneralData();


            if(a.getArmyRank()==0){
                w.hidImage(18);
            }else if(a.getArmyRank()>15){
                w.setImageRegion(4,game.getImgLists().getTextureByName("rank_15"));
            }else {
                w.setImageRegion(4,game.getImgLists().getTextureByName("rank_"+a.getArmyRank()));
            }
            if(g.isPlayer()&&g.getState()==0){
                if(g.getGeneralIndex()!=0){
                    w.setImageRegionNotChangeSize(3, game.getGeneralTextureRegion(g.getGeneralId()));
                    w.showImage(2);
                    w.setLabelText(0,g.getGeneralName());
                }else {
                    w.setImageRegionNotChangeSize(3,game.getGeneralTextureRegion(0, ResDefaultConfig.Image.GENERAL_IMAGE_PLAYER));
                }
            } else {
                w.setImageRegionNotChangeSize(3,game.getImgLists().getTextureByName("general_0").getTextureRegion());
                w.hidImage(2);
                w.hidLabel(0);
            }

        }else {
            if(l.isPlayer()){
                w.setImageRegionNotChangeSize(3,game.getGeneralTextureRegion(0, ResDefaultConfig.Image.GENERAL_IMAGE_PLAYER));
            }else {
                w.setImageRegionNotChangeSize(3,game.getImgLists().getTextureByName("general_0").getTextureRegion());
            }
            w.hidImage(4);
            w.hidImage(2);
            w.hidLabel(0);
        }
        w.setImageByTargetImage(5,5,game.getImgLists().getTextureByName("g_"+l.getMedal()),0,0);

        if(l.ifEffective(10)){
            //军团特性
            for(int i=1;i<=5;i++){
                int featureId=l.getLegionFeatureByIndex(i);
                int featureLv=l.getLegionFeatureLvByIndex(i);
                XmlReader.Element xml=game.gameConfig.getDEF_LEGIONFEATURE().getElementById(featureId);
                if(featureId>0&&xml!=null&&l.ifEffective(10)){//军团特性
                    w.setImageRegionForCenter(i+5,game.getImgLists().getTextureByName(xml.get("image")));
                    if(featureLv>=4){
                        w.setImageRegionForCenter(i+10,game.getImgLists().getTextureByName("icon_max"));
                    }else {
                        w.setImageRegionForCenter(i+10,game.getImgLists().getTextureByName(DefDAO.getNumberIcon(featureLv)));
                    }
                }else {
                    w.hidImage(i+5);
                    w.hidImage(i+10);
                }
                w.setButtonImageNotChangeSize(i+14,game.getImgLists().getTextureByName("button_skill").getTextureRegion());
                w.setImageRegionNotChangeSize(i+15,game.getImgLists().getTextureByName("skillBorder").getTextureRegion());
            }
        }else{
            Array<Fb2Smap.GeneralData> gs=l.getGeneralDatas(game.tempUtil.getTempArray(),5);
            for(int i=1;i<=5;i++){
                w.hidImage(i+5);
                w.hidImage(i+10);
                if(i-1<gs.size){
                    Fb2Smap.GeneralData g1=gs.get(i-1);
                    XmlReader.Element gE=game.gameConfig.getDEF_GENERAL().getElementById(g1.getGeneralId());
                    if(gE!=null){
                        w.setButtonImageToFitImageSize(i+14,i+5,game.getImgLists().getTextureByName("sg_"+gE.get("image")).getTextureRegion(),0.92f,0.92f);
                        w.setImageRegionNotChangeSize(i+15,game.getImgLists().getTextureByName("flag_"+l.getCountryId()).getTextureRegion());
                    }
                }else {
                    w.hidButton(i+14);
                    w.hidImage(i+15);
                }
            }
            game.tempUtil.disposeTempArray(gs);
        }

        //国家 指令输入框
        w.setButtonImageNotChangeSize(20,game.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(l.getCountryId())).getTextureRegion());

        w.setLabelText(1,l.getUnitArmyNum());
        w.setLabelText(2,l.getUnitNavyNum());
        w.setLabelText(3,l.varDefenceNum+l.varMissileNum);
        w.setLabelText(4,l.varAirNum);
        w.setLabelText(5,l.varNuclearNum);
        w.setLabelText(6,l.getMilitaryAcademyLv());
        w.setLabelText(7,l.getTankLvMax());
        w.setLabelText(8,l.getInfantryLvMax());
        w.setLabelText(9,l.getCannonLvMax());
        w.setLabelText(10,l.getNavyLvMax());
        w.setLabelText(11,l.getAirLvMax());
        w.setLabelText(12,l.getDefenceLvMax());
        w.setLabelText(13,l.getGeneralCardMax());
        w.setLabelText(14,l.getArmorCardMax());
        w.setLabelText(15,l.getInfantryCardMax());
        w.setLabelText(16,l.getArtilleryCardMax());
        w.setLabelText(17,l.getNavyCardMax());
        w.setLabelText(18,l.getAirCardMax());
        w.setLabelText(19,l.getDefenceCardMax());
        w.setLabelText(20,ComUtil.getSymbolNumer(l.incomeMoney));
        w.setLabelText(21,ComUtil.getSymbolNumer(l.incomeIndustry));
        w.setLabelText(22,ComUtil.getSymbolNumer(l.incomeTech));
        w.setLabelText(23,ComUtil.getSymbolNumer(l.incomeFood));
        if(l.ifEffective(1)){
            w.setLabelText(24,ComUtil.getSymbolNumer(l.incomeMineral));
        }else {
            w.setLabelText(24,"-");
        }
        if(l.ifEffective(2)){
            w.setLabelText(25,ComUtil.getSymbolNumer(l.incomeOil));
        }else {
            w.setLabelText(25,"-");
        }
        w.setLabelText(26,l.getCityLvMax());
        w.setLabelText(27,l.getIndustLvMax());
        w.setLabelText(28,l.getTechLvMax());
        if(l.ifEffective(7)){
            w.setLabelText(29,l.getEnergyLvMax());
        }else {
            w.setLabelText(29,"-");
        }
        w.setLabelText(30,l.getTransportLvMax());
        w.setLabelText(31,l.getMissileLvMax());
        w.setLabelText(32,l.getTradeLvMax());
        w.setLabelText(33,l.getCultureLvMax());
        w.setLabelText(34,l.getNuclearLvMax());
    }

    //index 1-7
    private  void setCardPrice(SMapGameStage smapStage,TextureRegionListDAO imgRs, GameMethod defDAO, WindowGroup tw, XmlReader.Element cardE, Fb2Smap.LegionData legion, Fb2Smap.BuildData build, Fb2Smap.ForeignData f, int cardType, int cardId, int index){
        StringBuilder errorStr=new StringBuilder();
        StringBuilder featureStr=null;
        boolean ifCanBuy=true;


        if(cardType==19){
            String  cardPriceName="price_bar2";
            int modelPrice=cardE.getInt("rule",50);
            tw.setImageRegionNotChangeSize(index,imgRs.getTextureByName(cardPriceName));

            if(ifCanBuy&&game.gameConfig.getPlayerModel()<modelPrice){
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_14"));
            }
            if(ifCanBuy){
                tw.setLabelText(index,ComUtil.getSpace(5)+modelPrice+"", Color.WHITE,2f);
            }else {     tw.setLabelText(index,ComUtil.getSpace(5)+modelPrice+"", Color.RED,2f);
            }
            tw.setName("");
            /*if(modelPrice>=100){
                //tw.setLabelRefPotion(index,0,-10);
            }else if(modelPrice>=10){
               // tw.setLabelRefPotion(index,6,-10);
            }else {
              //  tw.setLabelRefPotion(index,12,-10);
            }*/
            tw.setLabelPotionByImageButton(index,index,83,-48);
            return;
        }else if(cardType==11){        //对其进行特性说明
            // 科技
            featureStr=new StringBuilder();
            StringBuilder featureStr2=new StringBuilder();
            //循环单位特性,查找要求科技
            XmlReader.Element xEs = game.gameConfig.getDEF_UNITFEATURE().e;
            int minLv=999;
            int minId=-1;
            for(int i=0;i<xEs.getChildCount();i++){
                XmlReader.Element xE=xEs.getChild(i);
                if(legion.getAge()<xE.getInt("reqAge", 0)){
                    continue;
                }
                String useMode=xE.get("useMode","-1");
                if(!useMode.equals("-1")&&!ComUtil.ifHaveValueInStr(useMode,legion.getPlayerMode())){
                    continue;
                }
                int reqCardId=xE.getInt("reqCardId");
                int reqCardLv=xE.getInt("reqCardLv");
                if(reqCardId==cardId){
                    int featureId=xE.getInt("id");
                    if(reqCardLv>GameMethod.getCardLv(legion,null,cardId)){
                        if(reqCardLv<minLv){
                            minLv=reqCardLv;
                            minId=featureId;
                        }
                    } else  if (GameMethod.ifUnitFeatureCanUpd(featureId)) {
                        if(featureStr2.length()>0){featureStr2.append(",");}
                        featureStr2.append(game.gameMethod.getStrValue("feature_name_"+featureId));
                    }
                }
            }
            if(featureStr2.length()>0){
                featureStr.append(game.gameMethod.getStrValue("card_unlock_info2",featureStr2));
            }
            if(minId>0){
                if(featureStr.length()>0){
                    featureStr.append("\n");
                }
                featureStr.append(game.gameMethod.getStrValueT("card_unlock_info",minLv,"feature_name_"+minId));
            }
        }else if((cardType>=1&&cardType<=8)||cardType==-2||cardType==-7){//兵种卡牌
            featureStr=new StringBuilder();
            //循环兵种属性,查找已有特性,解析并拼接
            XmlReader.Element armyE=game.gameConfig.getDEF_ARMY().getElementById(cardId);
            if(armyE!=null){
                String features=armyE.get("feature","0");
                if(!features.equals("0")){
                    String[] strs = features.split(",");
                    for (int i = 0; i < strs.length; i++) {
                        if(ComUtil.isNumeric(strs[i])){
                            int feature=Integer.parseInt(strs[i]);
                            XmlReader.Element xEs = game.gameConfig.getDEF_UNITFEATURE().getElementById(feature);
                            if (xEs!=null&&legion.getAge()>=xEs.getInt("reqAge")&&GameMethod.getCardLv(legion,null,xEs.getInt("reqCardId"))>=xEs.getInt("reqCardLv")) {
                                if(featureStr.length()>0){featureStr.append("\n");}
                                featureStr.append(game.gameMethod.getStrValue("feature_name_"+feature)).append(":").append(game.gameMethod.getStrValue("feature_info_"+feature));
                            }
                        }
                    }
                }
            }
        }

        //对进行几次判断
        if((cardType<=10&&build.getBuildRound()>0)){//如果是地块卡牌,且地块研究回合>0,则显示round标记
            ifCanBuy=false;
            if(errorStr.length()>0){errorStr.append(",");}
            errorStr.append(game.gameMethod.getStrValue("carderror_prompt_1"));
        }else if(cardType==11&&legion.getLegionRound()>0){//如果是科技卡牌,且研究回合>0,则显示round标记
            ifCanBuy=false;
            if(errorStr.length()>0){errorStr.append(",");}
            errorStr.append(game.gameMethod.getStrValue("carderror_prompt_2"));
        }else if(cardType==12&&legion.getTradeCount()==0){//如果是外交卡牌且交易次数为0,,则显示round标记
            ifCanBuy=false;
            if(errorStr.length()>0){errorStr.append(",");}
            errorStr.append(game.gameMethod.getStrValue("carderror_prompt_3"));
        }else if(build!=null&&build.getHexagonToRecruitCardTIA1(cardType,cardId)==-1  ){//如果是单位卡牌,判断是否有建造空间
            ifCanBuy=false;
            if(errorStr.length()>0){errorStr.append(",");}
            errorStr.append(game.gameMethod.getStrValue("carderror_prompt_4"));
        }else if(build.getCityHpNow()==0&& cardType>=1&&cardType<=8){
            ifCanBuy=false;
            if(errorStr.length()>0){errorStr.append(",");}
            errorStr.append(game.gameMethod.getStrValue("carderror_prompt_12"));
        }

        if(ifCanBuy){
            if(GameMethod.buildCardIfNoEnemy(game,legion,build,cardId)){
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_13"));
            }else if(GameMethod.cardIfMax(smapStage.getMainGame(),legion,build,cardId)){//判断卡牌是否超过等级上限
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_5"));
            }else if(GameMethod.buildCardIfLock(game,legion,build,cardId)){//判断卡牌是否超过科技限制
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_6"));
            }else if((cardType==9||cardType==10)&&!build.ifCanBuildCardForCityLv(cardId)){
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_15"));
            }
        }
        if(ifCanBuy&&build!=null){
            int reqLv=cardE.getInt("reqCityLv",0);
            if(reqLv>build.getCityLvNow()){//判断卡牌是否超过等级上限
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_8",reqLv));
            }
            reqLv=cardE.getInt("reqIndLv",0);
            if(reqLv>build.getIndustryLvNow()){//判断卡牌是否超过等级上限
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_9",reqLv));
            }
            reqLv=cardE.getInt("reqTechLv",0);
            if(reqLv>build.getTechLvNow()){//判断卡牌是否超过等级上限
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_10",reqLv));
            }
            reqLv=cardE.getInt("reqAirLv",0);
            if(reqLv>build.getAirLvNow()){//判断卡牌是否超过等级上限
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_11",reqLv));
            }
        }

        String cardPriceName="price_bar1";
        //ifLastFavor 上一个是不是favor样式,如果不是使用偏移,否则使用
        Gdx.app.log("getName",tw.getImage(index).getName());
        if(cardType==12){//外交卡牌显示的样式

            //int selectCardChance=cardE.getInt("tech",0);
            int selectCardFavorPrice=0;
            if(cardE.getInt("priceType",0)==0){
                cardPriceName="price_bar0";
                //priceType 为0的为普通卡牌,money=比例+基础,其他三项均为比例


                int money = cardE.getInt("money", 0) + legion.incomeMoney * cardE.getInt("moneyRateCost", 0) / 100;
                int industry = cardE.getInt("industry", 0) + legion.incomeIndustry * cardE.getInt("industryRateCost", 0) / 100;
                int food = cardE.getInt("food", 0) + legion.incomeFood * cardE.getInt("foodRateCost", 0) / 100;
                int tech = cardE.getInt("tech", 0) + legion.incomeTech * cardE.getInt("techRateCost", 0) / 100;


                money = ComUtil.limitValue(DefDAO.getCardPrice(legion, build, 0, money, cardType, cardId, +GameMethod.getCardLv(legion, build, cardId)), 0, 999);
                industry = ComUtil.limitValue(DefDAO.getCardPrice(legion, build, 1, industry, cardType, cardId, +GameMethod.getCardLv(legion, build, cardId)), 0, 999);
                tech = ComUtil.limitValue(DefDAO.getCardPrice(legion, build, 2, tech, cardType, cardId, +GameMethod.getCardLv(legion, build, cardId)), 0, 999);
                food = ComUtil.limitValue(DefDAO.getCardPrice(legion, build, 3, food, cardType, cardId, +GameMethod.getCardLv(legion, build, cardId)), 0, 999);





                if(ifCanBuy&&(money>legion.getMoney()||industry>legion.getIndustry()||tech>legion.getTech()||food>legion.getFood())){
                    ifCanBuy=false;
                    if(errorStr.length()>0){errorStr.append(",");}
                    errorStr.append(game.gameMethod.getStrValue("carderror_prompt_7"));
                }


                if(ifCanBuy){
                    tw.setLabelText(index,defDAO.getPromptStrForGetPrice(money,industry),defDAO.getPromptStrForGetPrice(tech,food),Color.WHITE,1.25f);
                }else {
                    tw.setLabelText(index,defDAO.getPromptStrForGetPrice(money,industry),defDAO.getPromptStrForGetPrice(tech,food),Color.RED,1.25f);
                }
           //     tw.setLabelRefPotion(index,0,0);

                tw.setLabelPotionByImageButton(index,index,93,-38);
                   /* if(ifLastFavor){//好像触发不了
                     Label l= tw.getLabel(index);
                     l.setY(l.getY()-refY);
                     l.setX(l.getX()-refX);
                    }*/
            }else {  //industry为1的为好感度卡牌,只moneyRateCost有效

                selectCardFavorPrice=cardE.getInt("moneyRateCost",0);


                if(ifCanBuy&&f.getFavorValue()<selectCardFavorPrice){
                    ifCanBuy=false;
                    if(errorStr.length()>0){errorStr.append(",");}
                    errorStr.append(game.gameMethod.getStrValue("carderror_prompt_7"));
                }


                if(ifCanBuy){
                    tw.setLabelText(index,ComUtil.getSpace(5)+selectCardFavorPrice+"", Color.WHITE,2f);
                }else {     tw.setLabelText(index,ComUtil.getSpace(5)+selectCardFavorPrice+"", Color.RED,2f);
                }
                tw.setLabelPotionByImageButton(index,index,83,-48);
            }
        }else {
            cardPriceName="price_bar0";
            //boolean  selectCard1CanBuild=false;
            //boolean   selectCard2CanBuild=false;
            //设置显示卡牌价格样式,价格
            // selectCardPotion = DefDAO.getCardIdByCardPotion(selectCardType,selectCardPotion);

            int money = cardE.getInt("money", 0) + legion.incomeMoney * cardE.getInt("moneyRateCost", 0) / 100;
            int industry = cardE.getInt("industry", 0) + legion.incomeIndustry * cardE.getInt("industryRateCost", 0) / 100;
            int food = cardE.getInt("food", 0) + legion.incomeFood * cardE.getInt("foodRateCost", 0) / 100;
            int tech = cardE.getInt("tech", 0) + legion.incomeTech * cardE.getInt("techRateCost", 0) / 100;


            money = ComUtil.limitValue(DefDAO.getCardPrice(legion, build, 0, money, cardType, cardId, +GameMethod.getCardLv(legion, build, cardId)), 0, 999);
            industry = ComUtil.limitValue(DefDAO.getCardPrice(legion, build, 1, industry, cardType, cardId, +GameMethod.getCardLv(legion, build, cardId)), 0, 999);
            tech = ComUtil.limitValue(DefDAO.getCardPrice(legion, build, 2, tech, cardType, cardId, +GameMethod.getCardLv(legion, build, cardId)), 0, 999);
            food = ComUtil.limitValue(DefDAO.getCardPrice(legion, build, 3, food, cardType, cardId, +GameMethod.getCardLv(legion, build, cardId)), 0, 999);

            if(ifCanBuy&&(money>legion.getMoney()||industry>legion.getIndustry()||tech>legion.getTech()||food>legion.getFood())){
                ifCanBuy=false;
                if(errorStr.length()>0){errorStr.append(",");}
                errorStr.append(game.gameMethod.getStrValue("carderror_prompt_7"));
            }

            if(ifCanBuy){
                tw.setLabelText(index,defDAO.getPromptStrForGetPrice(money,industry),defDAO.getPromptStrForGetPrice(tech,food),Color.WHITE,1.25f);
            }else {
                tw.setLabelText(index,defDAO.getPromptStrForGetPrice(money,industry),defDAO.getPromptStrForGetPrice(tech,food),Color.RED,1.25f);
            }
            /*if(ifLastFavor){
                Label la= tw.getLabel(index);
                la.setY(la.getY()-refY);
                la.setX(la.getX()-refX);
            }*/
            //tw.setLabelRefPotion(index,0,0);
            tw.setLabelPotionByImageButton(index,index,93,-38);
        }
        tw.setImageRegionNotChangeSize(index,imgRs.getTextureByName(cardPriceName));
        /*if(!ifCanBuy&&cardType!=12){
            tw.getImage(index).setName(str.toString());
        }*/
         if(featureStr!=null&&featureStr.length()>0){
             if(errorStr.length()>0){
                 tw.getImage(index).setName(featureStr.append("\n[RED]").append(errorStr).toString());
             }else{
                 tw.getImage(index).setName(featureStr.toString());
             }
        }else if(errorStr.length()>0){
            tw.getImage(index).setName(errorStr.insert(0,"[RED]").toString());
        }else{
            tw.getImage(index).setName("");
        }
    }

    private  void setCardPriceToZero(TextureRegionListDAO imgRs, GameMethod defDAO, WindowGroup tw,  int index,boolean ifCanBuy){

        String cardPriceName="price_bar0";

        if(ifCanBuy){
            tw.setLabelText(index,defDAO.getPromptStrForGetPrice(0,0),defDAO.getPromptStrForGetPrice(0,0),Color.WHITE,1.2f);
        }else {
            tw.setLabelText(index,defDAO.getPromptStrForGetPrice(0,0),defDAO.getPromptStrForGetPrice(0,0),Color.RED,1.2f);
        }
        tw.setImageRegionNotChangeSize(index,imgRs.getTextureByName(cardPriceName).getTextureRegion());

        tw.setLabelPotionByImageButton(index,index,93,-38);
    }



    //index 0隐藏, 1选择第一个;
    public  void setCardSelected(WindowGroup window,int index){
        for(int i=1;i<=7;i++){
            ImageButton button=window.getButton(i);
            if(i==index){
                button.setChecked(true);
            }else {
                button.setChecked(false);
            }
        }

    }

    public  void updCardWindow( SMapGameStage smapStage, WindowGroup window,  Fb2Smap.LegionData l,Fb2Smap.BuildData b, Array<XmlReader.Element> cardEs, int cardType, int buildType, int nowPage, int sumPage) {

        //if(true){return ;}
        float cardWidth=smapStage.rescource.cardWidth;
        //  boolean ifConquest=game.getSMapDAO().masterData.getPlayerMode()==0;
        //  Fb2Smap.LegionData l=smapStage.getPlayerLegion();
        Fb2Smap.ForeignData f=null;
        if(cardType==12){
            f=smapStage.getsMapDAO().getForeignData(smapStage.selectedRLegionIndex);
        }
       // window.setLabelText(0, game.gameMethod.getPromptStrForGetTechByType(f,b,cardType,l));//"Lv11        12/12"

        window.setLabelText(0,game.gameMethod.getPromptStrForTechLvByType(f,b,cardType,l));
        window.setLabelText(12,game.gameMethod.getPromptStrForTechByType(f,b,cardType,l));


        float r=game.gameMethod.getRateByCardType(f,b,cardType,l);
        r=r>1f?1:r;
        window.setImageRegionWidth(10,r*window.getImage(9).getWidth(),1f);
        int cardECount=GameUtil.getArraySize(cardEs);
        int lv=0;
        for(int i=1,iMax=7;i<=iMax;i++){
            if(i<=cardECount&&cardEs.get(i-1)!=null){
                Gdx.app.log("setCardPrice",(i-1)+":"+cardECount);
                int cardId=cardEs.get(i-1).getInt("id");
                //TextureRegionListDAO imgRs, DefDAO defDAO, WindowGroup tw, XmlReader.Element cardE, Fb2Smap.LegionData legion, Fb2Smap.BuildData build, Fb2Smap.ForeignData f,int age, int cardType, int cardId, int index, boolean ifCanBuy
                setCardPrice(smapStage,game.getImgLists(),game.gameMethod,window,cardEs.get(i-1),l,b,f,cardType,cardId,i);
                window.setImageRegionNotChangeSize(10+i,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,cardId,cardType)).getTextureRegion());
                lv=GameMethod.getCardLv(l,b,cardId);
                setCardStar(window,20+i,lv,cardWidth);
                //window.setImageRegion(20+i,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,cardId)).getTextureRegion());
                // window.setImageRegion();
                window.showButton(i);
            }else {
                //window.setLabelText(i,"","", Color.RED,1.2f);
                window.hidLabel(i);
                // window.getButton(i).setVisible(false);
                window.hidImage(10+i);
                window.hidImage(20+i);
                window.hidButton(i);
                // window.getImage(i).setVisible(false);
            }
        }

        if(cardECount==0){
            window.hidLabel(8);
            window.hidLabel(9);
            window.hidLabel(10);
            window.hidLabel(11);
            window.hidButton(0);
            //window.hidLabel(0);
        }else {
            //   int cardId=cardEs.get(0).getInt("id");
            //  updCardInfo(game,window,cardId);
            window.setLabelText(11,nowPage+"/"+sumPage);
            window.showButton(0);
        }

        //window.getLabel(9).setWidth(200);
        int round=0;
        if(cardType==-7||(cardType>0&&cardType<11)){
            round=b.getBuildRound();
        }else {
            round=l.getLegionRound();
        }
        window.setLabelText(10,round+"");
        if(sumPage==1){
            window.hidButton(8);
            window.hidButton(9);
        }else {
            window.showButton(8);
            window.showButton(9);
        }
        window.setImageRegionNotChangeSize(8,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardType(b,cardType)).getTextureRegion());

        //window.setImageRegion(8,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardType(b,cardType)));

        //判断是否是造兵的,如果是造兵的则显示相关标签按钮,区分海陆
        if((cardType>=1&&cardType<=8) || cardType==15||cardType==-2||cardType==-7){ //15为设施
            boolean canBuildNavy=b.canRecruitNavy();
            if(buildType==1){
                window.getButton(14).setVisible(true);//舰船
                if(smapStage.getsMapDAO().worldData.getWorldAge()>=1){
                    window.getButton(18).setVisible(true);//潜艇
                }
            }else if(buildType==0){
                window.getButton(11).setVisible(true);//步兵
                window.getButton(12).setVisible(true);//炮兵
                if(smapStage.getsMapDAO().worldData.getWorldAge()>=1){
                    window.getButton(13).setVisible(true);//坦克
                    if(b.getAirCount()<4){
                        window.getButton(15).setVisible(true);//飞机
                    }
                    if(b.getNuclearCount()<4){
                        window.getButton(17).setVisible(true);//超武导弹
                    }
                }
                window.getButton(16).setVisible(true);//要塞

            }else if(buildType==3||cardType==15){
                window.getButton(11).setVisible(true);//步兵
                window.getButton(12).setVisible(true);//炮兵
                window.getButton(14).setVisible(true);//舰船


                if(smapStage.getsMapDAO().worldData.getWorldAge()>=1){
                    window.getButton(13).setVisible(true);//坦克
                    if(b.getAirCount()<4){
                        window.getButton(15).setVisible(true);//飞机
                    }
                    if(b.getNuclearCount()<4){
                        window.getButton(17).setVisible(true);//超武导弹
                    }
                    window.getButton(18).setVisible(true);//潜艇
                }
                window.getButton(16).setVisible(true);//要塞
            }
            if(smapStage.isEditMode(true)){
                window.getButton(11).setVisible(true);
                window.getButton(12).setVisible(true);
                window.getButton(13).setVisible(true);
                window.getButton(14).setVisible(true);
                window.getButton(15).setVisible(true);
                window.getButton(16).setVisible(true);
                window.getButton(17).setVisible(true);
                window.getButton(18).setVisible(true);
            }
            if(!canBuildNavy){
                window.hidButton(14);
                window.hidButton(18);
            }

            if(b.ifSea()){
                window.getButton(16).setVisible(true);//要塞
            }

        /*    Array<XmlReader.Element>  playerCard=GameMethod.getCardEByFilterCardE(smapStage.getsMapDAO(),smapStage.getPlayerLegion(),b,
                    -1,false,smapStage.getsMapDAO().publicLegionCardEs,smapStage.getsMapDAO().tempCardArray);*/
            Array<XmlReader.Element>  playerCard=l.varLegionCanBuildCardE;
            //关闭无法使用的卡牌标签项
            if(!smapStage.isEditMode(true)){
                boolean isVisible=false;
                for(int i=11,iMax=18;i<=iMax;i++){
                    Button button=window.getButton(i);
                    if(button.isVisible()){
                        isVisible=false;
                        for(XmlReader.Element cardE:playerCard){
                            if(cardE.getInt("type")+10==i){
                                isVisible=true;
                                break;
                            }
                        }
                        button.setVisible(isVisible);
                    }
                }
            }


        }else if(cardType==17){

        }else{
            window.getButton(11).setVisible(false);
            window.getButton(12).setVisible(false);
            window.getButton(13).setVisible(false);
            window.getButton(14).setVisible(false);
            window.getButton(15).setVisible(false);
            window.getButton(16).setVisible(false);
            window.getButton(17).setVisible(false);
//-1空  0战术 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇 9民用建筑卡 10军用建筑卡 11科技卡 12外交卡 13城市方针 14防御设施 15 港口等设施 16空军方针 17地标
            if( (b.getCityLvNow()>1&&((cardType==9||cardType==17)&&b.getBuildRound()==0&&game.getSMapDAO().ifSystemEffective(19))||(b.getAirCount()>0&&(cardType==13||cardType==16)&&game.getSMapDAO().getAge()>0&&game.getSMapDAO().masterData.getPlayerMode()!=2)   )){
                window.getButton(18).setVisible(true);
            }else{
                window.getButton(18).setVisible(false);
            }
        }
    }


    public void selectedCard(SMapGameStage smapGameStage, WindowGroup w,Array<Fb2Smap.NulcleData> selectNulList,Array< XmlReader.Element> selectCardEList,int selectCardType,int potion){
        //WindowGroup w=windowGroups.get(ResDefaultConfig.Class.SMapScreenGameCardGroupId);
        //  int selectCardPotion;
        int cardId=-1;
        w.setScrollLabel(9,"");
        Fb2Smap.LegionData pl= smapGameStage.getPlayerLegion();
        if(selectCardType!=12&&smapGameStage.isEditMode(true)){
            pl=smapGameStage.getSelectLegionData();
            if(pl==null){
                pl= smapGameStage.getPlayerLegion();
            }
        }
        if(selectCardType<100){
            if(potion>0&&potion-1<GameUtil.getArraySize(selectCardEList)){
                // selectCardPotion=potion;
                setCardSelected(w,potion);
                //game.gameLayout.updCardInfo(game,w,selectCardEList.get(potion-1).getInt("id",-1));
                XmlReader.Element cardE=selectCardEList.get(potion-1);
                cardId=cardE.getInt("id",-1);
                w.setLabelText(8,game.gameMethod.getCardName(pl, smapGameStage.getSelectBuildData(),cardId,selectCardType));
                if(cardE.getInt("type",selectCardType)==12){
                    Fb2Smap.LegionData targetLegion= smapGameStage.getsMapDAO().legionDatas.get(smapGameStage.selectedRLegionIndex);
                    int  selectCardChance=GameMethod.getForeignCardChance(cardId,cardE.getInt("tech",0),pl.getStability(),targetLegion.getStability());
                    if(cardId==3108){
                        if(smapGameStage.getsMapDAO().checkRegionCanLegionMerge(smapGameStage.selectedRLegionIndex,pl.getLegionIndex())){
                            selectCardChance=smapGameStage.getsMapDAO().getLegionMergeChance(smapGameStage.selectedRLegionIndex,pl.getLegionIndex());
                        }else{
                            selectCardChance=0;
                        }
                    }else if(cardId==3115){
                        selectCardChance=smapGameStage.getsMapDAO().getSetInfluenceChance(smapGameStage.selectedBuildData,pl.getLegionIndex());
                    }else if(cardId==3116){
                        selectCardChance=smapGameStage.getsMapDAO().getSetInfluenceChance(smapGameStage.selectedBuildData,pl.getLegionIndex());
                    }
                    if(cardId==3117){
                        int spiritId=targetLegion.getSpiritId();
                        if(w.getLabel(potion).getColor().equals(Color.RED)){
                            w.setScrollLabel(9,game.gameMethod.getStrValue("card_info_"+cardId,selectCardChance,game.gameMethod.getStrValue("spiritEffectName_"+spiritId),game.gameMethod.getStrValue("spiritEffectInfo_"+spiritId,smapGameStage.getsMapDAO().getPlayerAmbitionValue(),pl.getStability() ))+"\n"+w.getImage(potion).getName());
                        }else{
                            w.setScrollLabel(9,game.gameMethod.getStrValue("card_info_"+cardId,selectCardChance,game.gameMethod.getStrValue("spiritEffectName_"+spiritId),game.gameMethod.getStrValue("spiritEffectInfo_"+spiritId,smapGameStage.getsMapDAO().getPlayerAmbitionValue(),pl.getStability() )));
                        }
                    }else {
                        if(w.getLabel(potion).getColor().equals(Color.RED)){
                            w.setScrollLabel(9,game.gameMethod.getStrValueT("card_info_"+cardId,selectCardChance)+"\n"+w.getImage(potion).getName());
                        }else{
                            w.setScrollLabel(9,game.gameMethod.getStrValueT("card_info_"+cardId,selectCardChance));
                        }

                    }
                }else {
                    XmlReader.Element xmlE=game.gameConfig.getDEF_ARMY().getElementById(cardId);
                    if(xmlE!=null&&pl.isUnitGroup(xmlE.getInt("type",-1))){

                        if(w.getLabel(potion).getColor().equals(Color.RED)){
                            //给不能购买的卡牌补充说明  RCC="0" MDB="5" RDB="0" FDB="7" ADB="0"
                            w.setScrollLabel(9,game.gameMethod.getCardInfoStr(pl,smapGameStage.selectedBuildData,cardId,selectCardType)
                                    +"\n" +game.gameMethod.getArmyFormationInfoStr(xmlE)
                                    +"\n"+w.getImage(potion).getName());
                        }else if(!ComUtil.isEmpty(w.getImage(potion).getName())){
                            w.setScrollLabel(9,game.gameMethod.getCardInfoStr(pl,smapGameStage.selectedBuildData,cardId,selectCardType)
                                    +"\n" +game.gameMethod.getArmyFormationInfoStr(xmlE)
                                    +"\n"+w.getImage(potion).getName());
                        }else {
                            w.setScrollLabel(9,game.gameMethod.getCardInfoStr(pl,smapGameStage.selectedBuildData,cardId,selectCardType)
                                    +"\n" +game.gameMethod.getArmyFormationInfoStr(xmlE)
                            );
                        }
                    }else {
                        if(w.getLabel(potion).getColor().equals(Color.RED)){
                            //给不能购买的卡牌补充说明
                            w.setScrollLabel(9,game.gameMethod.getCardInfoStr(pl,smapGameStage.selectedBuildData,cardId,selectCardType)+"\n"+w.getImage(potion).getName());
                        }else if(!ComUtil.isEmpty(w.getImage(potion).getName())){
                            w.setScrollLabel(9,game.gameMethod.getCardInfoStr(pl,smapGameStage.selectedBuildData,cardId,selectCardType)+"\n"+w.getImage(potion).getName());
                        }else {
                            w.setScrollLabel(9,game.gameMethod.getCardInfoStr(pl,smapGameStage.selectedBuildData,cardId,selectCardType));
                        }
                    }

                }

                if(w.getLabel(potion).getColor().equals(Color.RED)&&!smapGameStage.rescource.isEditMode(true)){
                    w.hidButton(0);
                }else {
                    w.showButton(0);
                }
            }else {
                w.getButton(1).setChecked(false);
                w.getButton(2).setChecked(false);
                w.getButton(3).setChecked(false);
                w.getButton(4).setChecked(false);
                w.getButton(5).setChecked(false);
                w.getButton(6).setChecked(false);
                w.getButton(7).setChecked(false);
            }


            /*if(selectCardType==16&&cardId!=-1){
                int foreignBudget=  smapGameStage.getsMapDAO().masterData.getBf20();
                switch (cardId){
                    case 7000:
                        w.hidButton(8);
                        w.hidButton(9);
                        w.hidLabel(0);
                        w.setImageRegionWidth(10,w.getImage(9).getWidth(),1f);
                        break;
                    case 7001:
                    case 7002:
                    case 7003:
                    case 7004:
                    case 7005:
                    case 7006:
                        w.showButton(8);
                        w.showButton(9);
                        if(foreignBudget==0){
                            foreignBudget=50;
                            smapGameStage.getsMapDAO().masterData.setBf20(20);
                        }
                        w.setImageRegionWidth(10,w.getImage(9).getWidth()/100*foreignBudget,1f);
                        w.setLabelText(0,ComUtil.getSpace(18)+foreignBudget+"%");
                        break;
                }
            }*/


        }else if(selectCardType==101||selectCardType==102){//单位选择加载核弹
            if(potion>0&&potion-1<selectNulList.size){
                //selectCardPotion=potion;
                game.gameLayout.setCardSelected(w,potion);
                //game.gameLayout.updCardInfo(game,w,selectCardEList.get(potion-1).getInt("id",-1));
                Fb2Smap.NulcleData n=selectNulList.get(potion-1);
                cardId=n.getNuclearId();
                w.setLabelText(8,game.gameMethod.getCardName(pl, smapGameStage.getSelectBuildData(),cardId,selectCardType));
                w.setScrollLabel(9,game.gameMethod.getCardInfoStr(pl,smapGameStage.selectedBuildData,cardId,selectCardType));
                if(w.getLabel(potion).getColor().equals(Color.RED)){
                    w.hidButton(0);
                }else {
                    w.showButton(0);
                }
            }else {
                w.getButton(1).setChecked(false);
                w.getButton(2).setChecked(false);
                w.getButton(3).setChecked(false);
                w.getButton(4).setChecked(false);
                w.getButton(5).setChecked(false);
                w.getButton(6).setChecked(false);
                w.getButton(7).setChecked(false);
            }
        }
    }

    public  void updGeneralDialogueGroup( WindowGroup w, Fb2Smap.DialogueData d) {
        w.setImageRegionNotChangeSize(1,game.getFlagBgTextureRegion(d.getCountryId()));
        w.setImageRegionNotChangeSize(2,game.getGeneralTextureRegion(d.getGeneralState(),d.getGeneralImageName()));
        if(d.getGeneralState()==0&&game.resGameConfig.enableGeneral){
            w.setLabelText(0,game.gameMethod.getStrValueT("generalName_"+d.getGeneralId()));
        }else {
            w.setLabelText(0,game.gameMethod.getStrValueT("generalName_general"));
        }
        w.setLabelText(1,d.getDialogueContent(),true);
    }
    public  void updGeneralDialogueGroup( WindowGroup w, int generalId,int flagBg,String text) {
        w.setImageRegionNotChangeSize(1,game.getFlagBgTextureRegion(flagBg));
        XmlReader.Element g=  game.gameConfig.getDEF_GENERAL().getElementById(generalId);
        if(g==null){
            g= game.gameConfig.getDEF_GENERAL().getElementById(0);
        }
        w.setImageRegionNotChangeSize(2,game.getGeneralTextureRegion(0,g.get("image")));

        if(game.resGameConfig.enableGeneral){
            w.setLabelText(0,game.gameMethod.getStrValueT("generalName_"+g.getInt("id")));
        }else {
            w.setLabelText(0,game.gameMethod.getStrValueT("generalName_general"));
        }
        w.setLabelText(1,text,true);
    }
    private  void setCardStar( WindowGroup window,int imageId, int lv,float starWidth ){
        if(lv==0) {
            window.hidImage(imageId);
        }else if(lv<=5){
            int l= (int) (((lv-1)%5+1)*starWidth/5f);

            window.setImageRegion(imageId,game.getImgLists().getTextureByName("card_lv1").getNewRegion(0,0, (int) (((lv-1)%5+1)*starWidth/5f),-1 ),1.2f);
        }else {
            window.setImageRegion(imageId,game.getImgLists().getTextureByName("card_lv2").getNewRegion(0,0, (int) (((lv-1)%5+1)*starWidth/5f),-1 ),1.2f);
        }
    }

    //更新地区建筑的基础信息
    public  void updRegionBuildGroup(SMapGameStage.SMapPublicRescource res, WindowGroup window, Fb2Smap.BuildData build) {
        float starWidth=res.cardWidth;
        setCardStar(window,1,build.getCityLvNow(),starWidth);
        setCardStar(window,2,build.getIndustryLvNow(),starWidth);
        setCardStar(window,3,build.getTechLvNow(),starWidth);
        setCardStar(window,4,build.getFoodLvNow(),starWidth);
        setCardStar(window,5,build.getTransportLvNow(),starWidth);
        setCardStar(window,6,build.getTradeLvNow(),starWidth);
        setCardStar(window,7,build.getCultureLvNow(),starWidth);
        setCardStar(window,8,build.getArmyLvNow(),starWidth);
        setCardStar(window,9,build.getDefenceLvNow(),starWidth);
        setCardStar(window,10,build.getEnergyLvNow(),starWidth);
        setCardStar(window,11,build.getSupplyLvNow(),starWidth);
        setCardStar(window,12,build.getAirLvNow(),starWidth);
        setCardStar(window,13,build.getMissileLvNow(),starWidth);
        setCardStar(window,14,build.getNuclearLvNow(),starWidth);
        Fb2Smap.LegionData l=build.getLegionData();
       // window.setLabelText(0, game.gameMethod.getPromptStrForGetTechByType(null,build,9,l));

        window.setLabelText(0,game.gameMethod.getPromptStrForTechLvByType(null,build,9,l));
        window.setLabelText(5,game.gameMethod.getPromptStrForTechByType(null,build,9,l));
        window.setLabelText(3,build.getBuildRound()+"");
        window.setImageRegionNotChangeSize(15,game.getImgLists().getTextureByName(DefDAO.getImageNameByRegionType(build)).getTextureRegion());

        int rate=build.getAllRate();    // build.getLegionData().getLegionTax();
        window.setLabelText(4,rate+"");

        if(build.srName==null){
            if(build.getBuildName()==0){// 区域名称 - 区域类型 -标号
                //window.hidLabel(1);
                window.setLabelText(1,build.getAreaTypeStr()+ game.gameMethod.getStrValue(DefDAO.getAreaMark(build)));
            }else { //区域名称 - 城市名称 -标号
                window.setLabelText(1,build.areaName+ game.gameMethod.getStrValue(DefDAO.getAreaMark(build)));
            }
        }else {
            if(build.getBuildName()==0){// 区域名称 - 区域类型 -标号
                //window.hidLabel(1);
                window.setLabelText(1,build.srName+" - "+build.getAreaTypeStr()+ game.gameMethod.getStrValue(DefDAO.getAreaMark(build)));
            }else { //区域名称 - 城市名称 -标号
                window.setLabelText(1,build.srName+" - "+build.areaName+ game.gameMethod.getStrValue(DefDAO.getAreaMark(build)));
            }
        }


        //build.getBuildPolarityStr(),
        if(build.getBuildWonder()==0){
            window.setLabelText(2,game.gameMethod.getStrValueT("build_info_0",
                    build.getIncomeMoney(rate),build.getIncomeIndustry(rate),
                    build.getIncomeTech(rate),build.getIncomeFood(rate),
                    build.getDevelopLv(),build.getVarEnergyCost(),build.getBuildEnergyAfford()
                    ,build.getAirCount(),build.getNuclearCount(),build.getBuildStateInfo()
            ),true);
        }else{
            window.setLabelText(2,game.gameMethod.getStrValueT("build_info_0",
                    build.getIncomeMoney(rate),build.getIncomeIndustry(rate),
                    build.getIncomeTech(rate),build.getIncomeFood(rate),
                    build.getDevelopLv(),build.getVarEnergyCost(),build.getBuildEnergyAfford()
                    ,build.getAirCount(),build.getNuclearCount(),build.getBuildStateInfo()
            )+"\n"+build.getBuildWonderStr(),true);
        }


        window.hidButton(15);
        //更新主要信息
//        updRegionBuildInfo(window,build,0,rate);
    }

    public  void updRegionBuildInfo(WindowGroup window,Fb2Smap smap, Fb2Smap.BuildData build, int cardId) {

        window.setLabelText(1,game.gameMethod.getCardName(build.getLegionData(),build,cardId,0));
        String str=game.gameMethod.getStrValueT("build_info_"+cardId);

        if(smap.ifSystemEffective(8)&&build.haveTactics(cardId)&&build.getBuildRound()==0&&build.isPlayer()){
            if(smap.masterData.getPlayerMode()==2&&(cardId==2003||cardId==2005||cardId==2008||cardId==2011)){
                str= str+"\n"+game.gameMethod.getStrValueT("card_name_"+(cardId-1000))+":"+game.gameMethod.getStrValueT("card_info_"+(cardId-1000)+"_2");
                window.showButton(15);
            }else{
                str= str+"\n"+game.gameMethod.getStrValueT("card_name_"+(cardId-1000))+":"+game.gameMethod.getStrValueT("card_info_"+(cardId-1000));
                if(cardId!=2011){
                    window.showButton(15);
                }else{
                    window.hidButton(15);
                }
            }
        }else{
            window.hidButton(15);
        }
        window.setLabelText(2,str);
    }


    public  void updArmyInfoWindow(SMapGameStage.SMapPublicRescource res, WindowGroup window, Fb2Smap.ArmyData army, int showType){
        int shw=res.lvPentagramWidth;
        window.setImageRegion(1,game.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(game.getSMapDAO().legionDatas.get(army.getLegionIndex()).getCountryId())));
        window.setImageRegionNotChangeSize(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,army.getUnitArmyId0(),army.getArmyType())  ).getTextureRegion());
        window.setImageByTargetImage(4,3,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getUnitArmyId0())),0,0);
        int hw= res.armyHpWidth;
        float scale=army.getArmyHpNow()*1f/army.getArmyHpMax();
        int hy= (int) (hw-hw*scale);
        window.setImageRegion(5,game.getImgLists().getNewRegion(DefDAO.getImageNameForArmyHp(game.getSMapDAO(),army),0,hy,hw,hw-hy));


        if(army.getArmyRank()==0){
            window.hidImage(6);
        }else  if(army.getArmyRank()<=6){
            window.setImageRegion(6,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(army.getArmyRank())).getTextureRegion());
        }else {
            window.setImageRegion(6,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(6)).getTextureRegion());
        }
        Fb2Smap.LegionData l=game.getSMapDAO().legionDatas.get(army.getLegionIndex());
        if(army.getGeneralIndex()==0){
            window.setImageRegionNotChangeSize(7,game.getFlagBgTextureRegion(0));
        }else{
            window.setImageRegionNotChangeSize(7,game.getFlagBgTextureRegion(l.getCountryId()));
        }
        window.setButtonImageNotChangeSize(14,game.getImgLists().getTextureByName("flag_"+l.getCountryId()).getTextureRegion());
        Fb2Smap.GeneralData g=army.getGeneralData();
        window.setImageRegionNotChangeSize(17,game.sMapScreen.getGeneralTextureRegion(g));
        if(army.getArmyRank()==0){
            window.hidImage(18);
        }else if(army.getArmyRank()>15){
            window.setImageRegion(18,game.getImgLists().getTextureByName("rank_15"));
        }else {
            window.setImageRegion(18,game.getImgLists().getTextureByName("rank_"+army.getArmyRank()));
        }
        window.setImageRegion(19,game.getImgLists().getTextureByName("g_"+l.getMedal()));

        String morale=DefDAO.getImageNameForArmyMorale(game,army.getArmyMorale());

        TextureRegionDAO moraleRD=game.getImgLists().getTextureByName(morale);

        window.setImageRegion(9,moraleRD.getNewRegion(0,0,-1, (int) (moraleRD.getH()*army.getMoraleRate()/2)));


        //计算显示长度 shw,原5个五角星长度 hw计算后长度
        if(army.getUnitGroup1Lv()>0){
            hw=((army.getUnitGroup1Lv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(11,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(army.getUnitGroup1Lv()),0,0,hw,-1));
        }else {
            window.hidImage(11);
        }
        if(army.getUnitGroup2Lv()>0){
            hw=((army.getUnitGroup2Lv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(12,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(army.getUnitGroup2Lv()),0,0,hw,-1));
        }else {
            window.hidImage(12);
        }
        if(army.getUnitGroup3Lv()>0){
            hw=((army.getUnitGroup3Lv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(13,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(army.getUnitGroup3Lv()),0,0,hw,-1));

        }else {
            window.hidImage(13);
        }
        if(army.getUnitGroup4Lv()>0){
            hw=((army.getUnitGroup4Lv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(14,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(army.getUnitGroup4Lv()),0,0,hw,-1));
        }else {
            window.hidImage(14);
        }
        if(army.getUnitGroup5Lv()>0){
            hw=((army.getUnitGroup5Lv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(15,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(army.getUnitGroup5Lv()),0,0,hw,-1));

        }else {
            window.hidImage(15);
        }
        if(army.getUnitGroup6Lv()>0){
            hw=((army.getUnitGroup6Lv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(16,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(army.getUnitGroup6Lv()),0,0,hw,-1));
        }else {
            window.hidImage(16);
        }
        hw=army.getUnitWealv1()*shw/5;
        if(hw>shw){hw=shw; }//编队

        window.setLabelText(0,army.getArmyHpNow()+"");


        int armyId=army.getUnitArmyId0();
        //根据海陆计算伤害差,如果是非4,8的则伤害削减
        if(army.getArmyType()!=4&&army.getArmyType()!=8&&army.potionIsSea()){
            armyId=army.getTransportType()+1400;
        }
        Fb2Smap.BuildData build=army.getBuildData();
        //如果可以升级武器等级,则使用红字标注,否则正常白字
        if(((build.isPlayer()&&build.getBuildRound()==0)||build.isEditMode(true))&&army.getUnitWealv0Value()<build.getArmyLvNow()&&army.getUnitWealv0Value()<army.getUnitTechLv(0)){
            window.setLabelText(1,((int)(game.gameMethod.getUnitDamage(army.getLegionData(),build,army.getUnitArmyId0(),-1)*army.getGroupRate())+army.getArmyRank())+"~"+((int)(game.gameMethod.getUnitDamage(army.getLegionData(),build,armyId,1)*army.getGroupRate())+army.getUnitAbility()* game.resGameConfig.addAtkEachRank),Color.RED);
        }else{
            window.setLabelText(1,((int)(game.gameMethod.getUnitDamage(army.getLegionData(),build,army.getUnitArmyId0(),-1)*army.getGroupRate())+army.getArmyRank())+"~"+((int)(game.gameMethod.getUnitDamage(army.getLegionData(),build,armyId,1)*army.getGroupRate())+army.getUnitAbility()* game.resGameConfig.addAtkEachRank),Color.WHITE);
        }
        //如果可以招募将领,则使用红字标注,否则白字
        if(((build.isPlayer()&&build.getBuildRound()==0)||build.isEditMode(true))&&army.getGeneralIndex()==0&&l.ifCanRecruitGeneral()){
            window.setLabelText(2,army.getArmyKills()+"/"+army.getKillSum(),Color.GREEN);
        }else {
            window.setLabelText(2,army.getArmyKills()+"/"+army.getKillSum(),Color.WHITE);
        }
        window.setLabelText(3,army.getGeneralName());
        window.setLabelText(4,army.getArmorByDirect()+"");


        window.setImageRegion(20,game.getImgLists().getTextureByName(DefDAO.getImageNameArmyFeatureByArmy(army)).getTextureRegion());

        //更新技能


        //更新编队
        if(!army.canUpdWeaLv()){
            window.setImageRegionForCenter(8,game.getImgLists().getTextureByName("icon_max"));
        }else {
            window.setImageRegionForCenter(8,game.getImgLists().getTextureByName(DefDAO.getNumberIcon(army.getUnitWealv1())));
        }
        //能否加载核弹头
        //判断能否加载核武器
        if(army.isPlayer()&&army.getArmyRound()==0&&army.haveNulCanLoad()){
            window.getButton(13).setVisible(true);
        }else {
            window.getButton(13).setVisible(false);
        }
        updArmyInfoWindowForFeature(window,army,showType);
        if(army.isUnitGroup()){
            window.showButton(35);
        }else {
            window.hidButton(35);
        }
    }


    public void updArmyInfoWindowForFeature(WindowGroup window,Fb2Smap.ArmyData armyData, int showType) {
        if(showType==0){
            String features=armyData.armyXmlE0.get("feature","");
            String[] strs= features.split(",");
            for (int i = 0; i < 4; i++) {
                if (i < strs.length && ComUtil.isNumeric(strs[i])) {
                    int featureId = Integer.parseInt(strs[i]);
                    window.setImageRegionNotChangeSize(21 + i, game.getImgLists().getTextureByName("unitFeature_" + featureId).getTextureRegion());
                    window.showButton(31 + i);
                    //更新技能等级
                    if (armyData.ifHaveFeature(featureId)) {
                        if (GameMethod.ifUnitFeatureCanUpd(featureId)) {
                            window.setImageRegionForCenter(25 + i, game.getImgLists().getTextureByName(DefDAO.getNumberIcon(armyData.getFeatureLv(featureId))));
                        } else {
                            window.setImageRegionForCenter(25 + i, game.getImgLists().getTextureByName("icon_max"));
                        }
                    } else {
                        window.hidImage(25 + i);
                    }
                } else {
                    window.hidImage(21 + i);
                    window.hidImage(25 + i);
                    window.hidButton(31 + i);
                }
            }
        }else {
            for (int i = 0; i < 4; i++) {
                int skillId=armyData.getSkillIdByIndex(i);
                int skillLv=armyData.getSkillLvByIndex(i);
                if (skillId>0) {
                    window.setImageRegionNotChangeSize(21 + i, game.getImgLists().getTextureByName("unitSkill_" + skillId).getTextureRegion());
                    window.showButton(31 + i);
                    //更新技能等级
                    if(skillLv==0){
                        window.hidImage(25 + i);
                    }else  if (game.gameMethod.ifCanUpdSkillLv(skillId,skillLv)) {
                        window.setImageRegionForCenter(25 + i, game.getImgLists().getTextureByName(DefDAO.getNumberIcon(skillLv)));
                    } else {
                        window.setImageRegionForCenter(25 + i, game.getImgLists().getTextureByName("icon_max"));
                    }
                } else {
                    window.hidImage(21 + i);
                    window.hidImage(25 + i);
                    window.hidButton(31 + i);
                }
            }
        }
    }


    public  void updAirInfoWindow(SMapGameStage.SMapPublicRescource res, WindowGroup window, Fb2Smap.AirData air,  int showType){

        int shw=res.lvPentagramWidth;
        //设置框体颜色为卡牌颜色
        //window.replaceImageByImageName(game.getImgLists().getTextureByName(DefDAO.getColorBlockByArmyType(army.getArmyType())),"colorBlock_5");

        window.setImageRegion(1,game.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(game.getSMapDAO().legionDatas.get(air.getLegionIndex()).getCountryId())));

        //window.setImageRegion(2,game.getImgLists().getTextureByName(game.gameConfig.getDEF_CARD().getElementById(army.getArmyId()).get("image")   ));
        window.setImageRegionNotChangeSize(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,air.getAirId(),5)  ).getTextureRegion());

        window.setImageByTargetImage(4,3,game.getImgLists().getTextureByName("army_1501"),0,0);

        int hw= res.armyHpWidth;
        //int hw= (int) window.getImage(3).getDrawable().getMinWidth();
        float scale=air.getHpRateF();
        int hy= (int) (hw-hw*scale);
        window.setImageRegion(5,game.getImgLists().getNewRegion(DefDAO.getImageNameForAirHp(game.getSMapDAO(),air),0,hy,hw,hw-hy));

        if(air.getAirRank()==0){
            window.hidImage(6);
        }else  if(air.getAirRank()<=6){
            window.setImageRegion(6,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(air.getAirRank())).getTextureRegion());
        }else {
            window.setImageRegion(6,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(6)).getTextureRegion());
        }
        //window.setImageRegion(8,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyGroup(army.getArmyGroup())));
        Fb2Smap.LegionData l=game.getSMapDAO().legionDatas.get(air.getLegionIndex());
        if(air.getGeneralIndex()==0){
            window.setImageRegionNotChangeSize(7,game.getFlagBgTextureRegion(0));
        }else{
            window.setImageRegionNotChangeSize(7,game.getFlagBgTextureRegion(l.getCountryId()));
        }
        /*if(game.gameConfig.getIfAnimation()){
            if(air.getGeneralIndex()==0){
                window.setImageRegionNotChangeSize(7,game.sMapScreen.getFlagBgTextureRegion(0));
            }else{
                window.setImageRegionNotChangeSize(7,game.sMapScreen.getFlagBgTextureRegion(l.getCountryId()));
            }
        }else {
            window.setImageRegionNotChangeSize(7,game.sMapScreen.getFlagBgTextureRegion(0));
        }*/
       /* Fb2Smap.GeneralData g=game.getSMapDAO().generalIDatas.get(air.getGeneralIndex());
        if(g==null||air.getGeneralIndex()==0||g.getState()==1){
            window.setImageRegion(17,game.getImgLists().getTextureByName("general_0"));
        }else {
            window.setImageRegionNotChangeSize(17,game.sMapScreen.getGeneralTextureRegion(g));
        }*/

        window.setButtonImageNotChangeSize(14,game.getImgLists().getTextureByName("flag_"+l.getCountryId()).getTextureRegion());
        Fb2Smap.GeneralData g=air.getGeneralData();
        window.setImageRegionNotChangeSize(17,game.sMapScreen.getGeneralTextureRegion(g));
        window.setImageRegion(18,game.getImgLists().getTextureByName("rank_"+air.getAirRank()));
        window.setImageRegion(19,game.getImgLists().getTextureByName("g_"+l.getMedal()));


        String morale=DefDAO.getImageNameForArmyMorale(game,air.getAirMorale());

        TextureRegionDAO moraleRD=game.getImgLists().getTextureByName(morale);

        window.setImageRegion(9,moraleRD.getNewRegion(0,0,-1, (int) (moraleRD.getH()*air.getAirMoraleRate()/2)));


        //计算显示长度 shw,原5个五角星长度 hw计算后长度
        if(air.getAckLv()>0){
            hw=((air.getAckLv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(11,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(air.getAckLv()),0,0,hw,-1));
        }else {
            window.hidImage(11);
        }
        if(air.getDefLv()>0){
            hw=((air.getDefLv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(12,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(air.getDefLv()),0,0,hw,-1));
        }else {
            window.hidImage(12);
        }
        if(air.getSpyLv()>0){
            hw=((air.getSpyLv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(13,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(air.getSpyLv()),0,0,hw,-1));

        }else {
            window.hidImage(13);
        }
        if(air.getSupLv()>0){
            hw=((air.getSupLv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(14,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(air.getSupLv()),0,0,hw,-1));

        }else {
            window.hidImage(14);
        }
        if(air.getActLv()>0){
            hw=((air.getActLv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(15,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(air.getActLv()),0,0,hw,-1));

        }else {
            window.hidImage(15);
        }
        if(air.getWeaLv()>0){
            hw=((air.getWeaLv()-1)%5+1)*shw/5;
            if(hw>shw){hw=shw; }
            window.setImageRegion(16,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(air.getWeaLv()),0,0,hw,-1));
        }else {
            window.hidImage(16);
        }
        //星级 直接按rank算
        hw=((air.getAirRank()-1)%5+1)*shw/5;

        if(hw>shw){hw=shw; }
        // window.setImageRegion(8,game.getImgLists().getNewRegion(DefDAO.getImageNameForStarBar(air.getAirRank()),0,0,hw,-1));
        Fb2Smap.BuildData build=game.getSMapDAO().getBuildDataByRegion(game.getSMapDAO().getRegionId(air.getRegionId()));
        //window.setImageRegion(8,game.getImgLists().getNewRegion(ResConfig.Image.LV_PENTAGRAM,0,0,hw,-1));
        window.setLabelText(0,air.getAirHpNow()+"");
        XmlReader.Element armyE=game.gameConfig.getDEF_ARMY().getElementById(air.getAirId());
        //如果可以升级武器等级,则使用红字标注,否则正常白字
        if(build.isPlayer()&&build.getBuildRound()==0&&air.getWeaLv()<build.getArmyLvNow()&&air.getWeaLv()<air.getUnitTechLv()){
            window.setLabelText(1,((int)(game.gameMethod.getUnitDamage(air.getLegionData(),build,air.getAirId(),-1)*air.getRankRate())+air.getAirRank())+"~"+((int)(game.gameMethod.getUnitDamage(air.getLegionData(),build,air.getAirId(),1)*air.getRankRate())+air.getAirAbility()* game.resGameConfig.addAtkEachRank),Color.RED);
        }else{
            window.setLabelText(1,((int)(game.gameMethod.getUnitDamage(air.getLegionData(),build,air.getAirId(),-1)*air.getRankRate())+air.getAirRank())+"~"+((int)(game.gameMethod.getUnitDamage(air.getLegionData(),build,air.getAirId(),1)*air.getRankRate())+air.getAirAbility()* game.resGameConfig.addAtkEachRank),Color.WHITE);
        }
        //如果可以招募将领,则使用红字标注,否则白字
        if(build.isPlayer()&&build.getBuildRound()==0&&air.getGeneralIndex()==0&&l.ifCanRecruitGeneral()){
            window.setLabelText(2,air.getAirKills()+"/"+air.getKillSum(),Color.GREEN);
        }else {
            window.setLabelText(2,air.getAirKills()+"/"+air.getKillSum(),Color.WHITE);
        }
        window.setLabelText(3,air.getGeneralName());
        window.setLabelText(4,air.getArmor()+"");
        //  window.setLabelText(4,"-"+(int)(army.getBuildPolicy()*ResConfig.Game.militaryCostRate));
        window.setImageRegion(20,game.getImgLists().getTextureByName("feature_air").getTextureRegion());



        window.hidImage(8);
        //判断能否加载核武器
        if(air.isPlayer()&&air.getAirRound()==0&&air.haveNulCanLoad()){
            window.getButton(13).setVisible(true);
        }else {
            window.getButton(13).setVisible(false);
        }
        updAirInfoWindowForFeature(window,air,showType);
        window.hidButton(35);
    }

    public void updAirInfoWindowForFeature(WindowGroup window,Fb2Smap.AirData air, int showType) {

        if(showType==0){
            String features=air.airXmlE.get("feature");
            String[] strs= features.split(",");
            for (int i = 0; i < 4; i++) {
                if (i<strs.length&&ComUtil.isNumeric(strs[i])) {
                    int featureId = Integer.parseInt(strs[i]);
                    window.setImageRegionNotChangeSize(21+i,game.getImgLists().getTextureByName("unitFeature_"+featureId).getTextureRegion());
                    window.showButton(31+i);
                    //更新技能等级
                    if (air.ifHaveAirFeature(featureId)) {
                        if (GameMethod.ifUnitFeatureCanUpd(featureId)) {
                            window.setImageRegionForCenter(25+i, game.getImgLists().getTextureByName(DefDAO.getNumberIcon(air.getAirFeatureLv(featureId))));
                        } else {
                            window.setImageRegionForCenter(25+i, game.getImgLists().getTextureByName("icon_max"));
                        }
                    } else {
                        window.hidImage(25 + i);
                    }
                }else {
                    window.hidImage(21+i);
                    window.hidImage(25 + i);
                    window.hidButton(31+i);
                }
            }
        }else {
            for (int i = 0; i < 4; i++) {
                int skillId=air.getSkillIdByIndex(i);
                int skillLv=air.getSkillLvByIndex(i);
                if (skillId>0&&skillLv>0) {
                    window.setImageRegionNotChangeSize(21 + i, game.getImgLists().getTextureByName("unitSkill_" + skillId).getTextureRegion());
                    window.showButton(31 + i);
                    //更新技能等级
                    if(skillLv==0){
                        window.hidImage(25 + i);
                    }else if (game.gameMethod.ifCanUpdSkillLv(skillId,skillLv)) {
                        window.setImageRegionForCenter(25 + i, game.getImgLists().getTextureByName(DefDAO.getNumberIcon(skillLv)));
                    } else {
                        window.setImageRegionForCenter(25 + i, game.getImgLists().getTextureByName("icon_max"));
                    }
                } else {
                    window.hidImage(21 + i);
                    window.hidImage(25 + i);
                    window.hidButton(31 + i);
                }
            }
        }
    }
    //       game.gameLayout.updSMapScreenOptionWindow(windowGroups.get(ResDefaultConfig.Class.SMapScreenOptionGroupId), game.musicVoice, game.soundVoice, game.moveSpeed, game.gameConfig.getIfColor(), game.gameConfig.ifEffect, game.gameConfig.ifAutoSave);
    public void updSMapScreenOptionWindow(MainGame game,WindowGroup window) {
        if(window!=null){
            float    hw=(int) window.getImage(4).getDrawable().getMinWidth();
            float hh=(int) window.getImage(4).getDrawable().getMinHeight();
            window.setImageRegion(1,game.getImgLists().getNewRegion(ResDefaultConfig.Image.OP_BLOCK,0,0,(int)hw/7*game.soundVoice,(int)hh));
            window.setImageRegion(2,game.getImgLists().getNewRegion(ResDefaultConfig.Image.OP_BLOCK,0,0,(int)hw/7*game.musicVoice,(int)hh));
            window.setImageRegion(3,game.getImgLists().getNewRegion(ResDefaultConfig.Image.OP_BLOCK,0,0,(int)hw/7*game.moveSpeed,(int)hh));
            window.getButton(7).getImage().setVisible(game.gameConfig.ifEffect);
            window.getButton(8).getImage().setVisible(game.gameConfig.ifAutoSave);
            window.getButton(9).getImage().setVisible(game.gameConfig.ifAiActAndNextRound);  /**/
            window.getButton(10).getImage().setVisible(game.gameConfig.ifPromptUnitUpd);
        }
    }

    public void updOptionWindow(WindowGroup window ) {
        if(window!=null){
            int music=   game.musicVoice;
            int sound=   game.soundVoice;
            float hw=(int) window.getImage(3).getDrawable().getMinWidth();
            float hh=(int) window.getImage(3).getDrawable().getMinHeight();
            window.setImageRegion(1,game.getImgLists().getNewRegion(ResDefaultConfig.Image.OP_BLOCK,0,0,(int)hw/7*sound,(int)hh));
            window.setImageRegion(2,game.getImgLists().getNewRegion(ResDefaultConfig.Image.OP_BLOCK,0,0,(int)hw/7*music,(int)hh));
            //window.getButton(0).getImage().setVisible(ifAnimation);
            window.setLabelText(0,game.gameConfig.getModName());
            updOptionWindowForScreen(window);
        }
    }
    public void updOptionWindowForScreen(WindowGroup window ) {

        //如果可以调整分辨率
        if(Gdx.app.getType().equals(Application.ApplicationType.Desktop)){
            window.showButton(20);
            window.showButton(21);
            window.showButton(22);
            window.showButton(23);
            int index=game.gameConfig.playerConfig.getInteger("screenSizeIndex",ResDefaultConfig.Game.defaultScreenIndex);
            if(index<0||index>=game.dms.length){
                index=0;
            }
            //如果是全屏
            if(game.gameConfig.playerConfig.getBoolean("isFullscreen", false)){//如果是全屏,将字体颜色设置为灰色
                window.setButtonImageNotChangeSize(20,game.getImgLists().getTextureByName("icon_option_zoomout").getTextureRegion());
                if(game.gameConfig.playerConfig.getBoolean("customScreen",false)){
                    window.setLabelText(1,game.gameConfig.playerConfig.getInteger("customScreenW",ResDefaultConfig.Game.screenWidth[index])+"*"+game.gameConfig.playerConfig.getInteger("customScreenH",ResDefaultConfig.Game.screenHeight[index]),Color.LIGHT_GRAY);
                }else{
                    window.setLabelText(1,ResDefaultConfig.Game.screenWidth[index]+"*"+ResDefaultConfig.Game.screenHeight[index],Color.LIGHT_GRAY);
                }
            }else {
                window.setButtonImageNotChangeSize(20,game.getImgLists().getTextureByName("icon_option_zoomin").getTextureRegion());
                if(game.gameConfig.playerConfig.getBoolean("customScreen",false)){
                    window.setLabelText(1,game.gameConfig.playerConfig.getInteger("customScreenW",ResDefaultConfig.Game.screenWidth[index])+"*"+game.gameConfig.playerConfig.getInteger("customScreenH",ResDefaultConfig.Game.screenHeight[index]),Color.DARK_GRAY);
                }else{
                    window.setLabelText(1,ResDefaultConfig.Game.screenWidth[index]+"*"+ResDefaultConfig.Game.screenHeight[index],Color.DARK_GRAY);
                }
            }
        }else{
            window.setButtonImageNotChangeSize(20,game.getImgLists().getTextureByName("icon_option_zoomin").getTextureRegion());
            window.setLabelText(1,(int)game.getWorldWidth()+"*"+(int)game.getWorldHeight());
            window.hidButton(21);
            window.hidButton(22);
            window.hidButton(23);
        }
    }

    //创建
    public void updTradeInfoWindow(MainGame game,WindowGroup window, Array<Fb2Smap.TradeData> tradeDatas, int page,int sumPage) {
        int index,hy;
        int hw= (int) window.getImage(2).getDrawable().getMinWidth();
        int tradeCount=game.getSMapDAO().getPlayerLegionData().getTradeCount();
        for(int i=0,iMax=10;i<iMax;i++){
            index=i+(page-1)*10;
            if(index<tradeDatas.size){
                Fb2Smap.TradeData t=tradeDatas.get(index);
                Fb2Smap.ForeignData f=game.getSMapDAO().getForeignData(t.getLegionIndex());
                Fb2Smap.LegionData l=game.getSMapDAO().legionDatas.get(t.getLegionIndex());
                //Gdx.app.log("123",":"+(5*i+1));
                window.getImage(5*i+1).setVisible(true);
                window.getImage(5*i+2).setVisible(true);
                window.getImage(5*i+3).setVisible(true);
                window.getImage(5*i+4).setVisible(true);
                window.getLabel(i+1).setVisible(true);
                window.setImageRegion(5*i+1,game.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(game.getSMapDAO().getCountryIdByli(t.getLegionIndex()))).getTextureRegion());
                float scale=f.getFavorValue()*1f/100;
                hy= (int) (hw-hw*scale);
                window.setImageRegion(5*i+2,game.getImgLists().getNewRegion(DefDAO.getImageNameForHp(game.getSMapDAO(),t.getLegionIndex()),0,hy,hw,hw-hy));
                window.setImageRegion(5*i+3,game.getImgLists().getTextureByName(DefDAO.getImageNameByResourceType(t.getGoodsType())).getTextureRegion());
                window.setImageRegion(5*i+4,game.getImgLists().getTextureByName(DefDAO.getImageNameByResourceType(t.getPriceType())).getTextureRegion());
                window.setLabelText(i+1,DefDAO.splitStrForTradeValue(t.getGoodsValue()* game.resGameConfig.tradeSuccessRewardRate,t.getPriceValue()));
              //  window.setLabelText(i+1,DefDAO.splitStrForTradeValue(9999,9999));


                if(t.getTradeChance()==0||tradeCount==0||t.getLegionIndex()==game.getSMapDAO().masterData.getPlayerLegionIndex()||f.getFavorValue()<=30){
                    if(l.getInternIndex()==0){//中立国可以交易资源
                       /* window.getImage(5*i+5).setVisible(false);
                        if(t.playerCanTrade()){
                            window.getImage(5*i+5).setVisible(false);
                            window.getButton(i+1).setVisible(true);
                        }else{
                            window.getImage(5*i+5).setVisible(true);
                            window.getButton(i+1).setVisible(false);
                        }*/
                        window.getImage(5*i+5).setVisible(true);
                        window.getButton(i+1).setVisible(false);
                    }else {
                        window.getImage(5*i+5).setVisible(true);
                        window.getButton(i+1).setVisible(false);
                    }
                }else {
                    if(t.playerCanTrade()){
                        window.getImage(5*i+5).setVisible(false);
                        window.getButton(i+1).setVisible(true);
                    }else{
                        window.getImage(5*i+5).setVisible(true);
                        window.getButton(i+1).setVisible(false);
                    }
                }
            }else {
                window.getImage(5*i+1).setVisible(false);
                window.getImage(5*i+2).setVisible(false);
                window.getImage(5*i+3).setVisible(false);
                window.getImage(5*i+4).setVisible(false);
                window.getImage(5*i+5).setVisible(false);
                window.getButton(i+1).setVisible(false);
                window.getLabel(i+1).setVisible(false);
            }
        }
        window.setLabelText(0,page+"/"+sumPage);
        window.setLabelText(11,tradeCount+"");
    }




    public  void updBorderWindowByArmy(SMapGameStage.SMapPublicRescource res, WindowGroup w, Array<Fb2Smap.ArmyData> selectArmyList, int page) {
        //if(true){return ;}
        Image image=w.getImage(2001);
        float cardWidth=image.getWidth();
        Fb2Smap.ArmyData army;
        Fb2Smap.LegionData legion=game.getSMapDAO().getPlayerLegionData();
        String morale;
        if(selectArmyList.size>4){
            w.showButton(2005);
            w.showButton(2006);
        }else{
            w.hidButton(2005);
            w.hidButton(2006);
        }

        for(int i,m,j=1;j<=4;j++){
            i=(page-1)*4+j-1;
            m=(j-1)*5;
            if(i<selectArmyList.size){//显示数据
                //showArmyInfoForArmy(window,armyData.get(i),j);
                army=selectArmyList.get(i);
                if(army.getArmyRound()==0){
                    w.setButtonImageNotChangeSize(2000+j,game.getImgLists().getTextureByName("card_border").getTextureRegion());
                }else{
                    w.setButtonImageNotChangeSize(2000+j,game.getImgLists().getTextureByName("card_ShaderBorder").getTextureRegion());
                }
                //卡牌
                w.setImageRegionNotChangeSize(2000+1+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,army.getUnitArmyId0(),army.getArmyType())).getTextureRegion());
                Fb2Smap.GeneralData g=army.getGeneralData();
                //将领
                if(g!=null&&army.getGeneralIndex()!=0){
                    if(g.getState()==0){
                        //w.setImageRegionNotChangeSize(2000+3+m,game.getImgLists().getTextureByName(g.getSmallGeneralImageName()).getTextureRegion());
                        w.setImageRegion(2000+3+m,game.getImgLists().getTextureByName(g.getSmallGeneralImageName()).getTextureRegion(),1.5f);
                        w.showImage(2000+28+j);
                    }else{
                        //  w.setImageRegionNotChangeSize(2000+3+m,game.getImgLists().getTextureByName("smallgeneral_0").getTextureRegion());
                        w.setImageRegion(2000+3+m,game.getImgLists().getTextureByName("g_"+legion.getMedal()).getTextureRegion(),1.5f);
                        w.hidImage(2000+28+j);
                    }
                }else {
                    w.hidImage(2000+3+m);
                    w.hidImage(2000+28+j);
                }
                //血条
                //  w.setImageRegion(2+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardHp(army.getArmyGroup())),cardWidth* army.getHpRateF(),0);
                w.setImageRegionWidth(2000+2+m,cardWidth* army.getHpRateF(),1f);
                //等级
                if(army.getArmyRank()>0){
                    w.setImageRegion(2000+4+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(army.getArmyRank())).getTextureRegion(),1.5f);
                }else {
                    w.hidImage(2000+4+m);
                }
                // morale=DefDAO.getImageNameForArmyMorale(army.getArmyMorale());
                /*//兵种士气 0正常 1上升 2降低 3大降 4混乱
                if(morale!=null){
                    w.setImageRegion(5+m,game.getImgLists().getTextureByName(morale));
                }else {
                    w.hidImage(5+m);
                }*/
                if(army.getNucleIndex()!=-1){
                    w.setImageRegionNotChangeSize(2000+5+m,game.getImgLists().getTextureByName("button_loadnuclear").getTextureRegion());
                }else if(army.getAirCount()>0){
                    w.setImageRegionNotChangeSize(2000+5+m,game.getImgLists().getTextureByName("button_loadair").getTextureRegion());
                }else {
                    w.hidImage(2000+5+m);
                }
                //兵种 显示的是士气
                w.setImageRegionWidth(2000+20+j,game.getImgLists().getTextureByName("card_bar").getTextureRegion(),cardWidth* army.getArmyMorale()/100,1f);
                int uc=army.getUnitGroupPower();
                //兵种编队
                if(uc>=10){
                    w.setImageRegion(2000+24+j,game.getImgLists().getTextureByName("card_groupborder").getTextureRegion(),1.8f);
                }else if(uc>5){
                    w.setImageRegion(2000+24+j,game.getImgLists().getTextureByName("card_groupborder1").getNewRegion(0,0,-1, (int) (res.cardHeight/5*(uc-5))),1.8f);
                }else {
                    w.setImageRegion(2000+24+j,game.getImgLists().getTextureByName("card_groupborder").getNewRegion(0,0,-1, (int) (res.cardHeight/5*uc)),1.8f);
                }
                w.setImageRegionHeight(2032+j,image.getHeight()* army.getExpRateF(),1f);
                w.showButton(2000+j);
            }else {//隐藏数据
                w.hidImage(2000+1+m);
                w.hidImage(2000+2+m);
                w.hidImage(2000+3+m);
                w.hidImage(2000+4+m);
                w.hidImage(2000+5+m);
                w.hidImage(2000+20+j);
                w.hidImage(2000+24+j);
                w.hidImage(2000+28+j);
                w.hidButton(2000+j);
                w.hidImage(2032+j);
            }
            i++;
        }
    }
    public  void updBorderWindowByAir(SMapGameStage.SMapPublicRescource res, WindowGroup w, Array<Fb2Smap.AirData> selectAirList, int page) {
        Image image=w.getImage(2001);
        float cardWidth=image.getWidth();
        Fb2Smap.AirData air;
        Fb2Smap.LegionData legion=game.getSMapDAO().getPlayerLegionData();
        String morale;
        if(selectAirList.size>4){
            w.showButton(2000+5);
            w.showButton(2000+6);
        }else{
            w.hidButton(2000+5);
            w.hidButton(2000+6);
        }
        for(int i,m,j=1;j<=4;j++){
            i=(page-1)*4+j-1;
            m=(j-1)*5;
            if(i<selectAirList.size){//显示数据
                //showArmyInfoForArmy(window,armyData.get(i),j);
                air=selectAirList.get(i);
                if(air.getAirRound()==0){
                    w.setButtonImageNotChangeSize(2000+j,game.getImgLists().getTextureByName("card_border").getTextureRegion());
                }else{
                    w.setButtonImageNotChangeSize(2000+j,game.getImgLists().getTextureByName("card_ShaderBorder").getTextureRegion());
                }
                //卡牌
                w.setImageRegionNotChangeSize(2000+1+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,air.getAirId(),5)).getTextureRegion());

                //将领

                Fb2Smap.GeneralData g=air.getGeneralData();
                //将领
                if(g!=null&&air.getGeneralIndex()!=0){
                    if(g.getState()==0) {
                        w.setImageRegion(2000+3+m,game.getImgLists().getTextureByName(g.getSmallGeneralImageName()).getTextureRegion(),1.5f);
                        w.showImage(2000 + 28 + j);
                    } else{
                        w.setImageRegion(2000+3+m,game.getImgLists().getTextureByName("g_"+legion.getMedal()).getTextureRegion(),1.5f);
                        w.hidImage(2000+28+j);
                    }
                }else {
                    w.hidImage(2000+3+m);
                    w.hidImage(2000+28+j);
                }
                //血条
                //  w.setImageRegion(2+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardHp(army.getArmyGroup())),cardWidth* army.getHpRateF(),0);
                w.setImageRegionWidth(2000+2+m,image.getHeight()* air.getHpRateF(),1f);
                //等级
                if(air.getAirRank()>0){
                    w.setImageRegion(2000+4+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(air.getAirRank())).getTextureRegion(),1.5f);
                }else {
                    w.hidImage(2000+4+m);
                }
                /*morale=DefDAO.getImageNameForArmyMorale(air.getAirMorale());
                //兵种士气 0正常 1上升 2降低 3大降 4混乱
                if(morale!=null){
                    w.setImageRegion(5+m,game.getImgLists().getTextureByName(morale));
                }else {
                    w.hidImage(5+m);
                }*/
                if(air.getNucleIndex()!=-1){
                    w.setImageRegionNotChangeSize(2000+5+m,game.getImgLists().getTextureByName("button_loadnuclear").getTextureRegion());
                }else {
                    w.hidImage(2000+5+m);
                }


                //兵种气力  空军显示的是物资
                w.setImageRegionWidth(2000+20+j,game.getImgLists().getTextureByName("card_bar").getTextureRegion(),cardWidth* air.getAirMorale()/100,1f);
                //兵种编队
                if(air.getAirGoodsNow()>=10){
                    w.setImageRegion(2000+24+j,game.getImgLists().getTextureByName("card_groupborder").getTextureRegion(),1.8f);
                }else if(air.getAirGoodsNow()>5){
                    w.setImageRegion(2000+24+j,game.getImgLists().getTextureByName("card_groupborder1").getNewRegion(0,0,-1, (int) (res.cardHeight/5*(air.getAirGoodsNow()-5))),1.8f);
                }else {
                    w.setImageRegion(2000+24+j,game.getImgLists().getTextureByName("card_groupborder").getNewRegion(0,0,-1, (int) (res.cardHeight/5*air.getAirGoodsNow())),1.8f);
                }
                // w.hidImage(24+j);
                //经验
                w.setImageRegionHeight(2032+j,image.getHeight()* air.getExpRateF(),1f);
                w.showButton(2000+j);
            }else {//隐藏数据
                w.hidImage(2000+1+m);
                w.hidImage(2000+2+m);
                w.hidImage(2000+3+m);
                w.hidImage(2000+4+m);
                w.hidImage(2000+5+m);
                w.hidImage(2000+20+j);
                w.hidImage(2000+24+j);
                w.hidImage(2000+28+j);
                w.hidButton(2000+j);
                w.hidImage(2032+j);
            }
            i++;
        }
    }
    public  void updBorderWindowByNul( WindowGroup w, Array<Fb2Smap.NulcleData> selectNulList, int page) {
        Fb2Smap.NulcleData nul;
        Fb2Smap.LegionData legion=game.getSMapDAO().getPlayerLegionData();
        if(selectNulList.size>4){
            w.showButton(2000+5);
            w.showButton(2000+6);
        }else{
            w.hidButton(2000+5);
            w.hidButton(2000+6);
        }
        for(int i,m,j=1;j<=4;j++){
            i=(page-1)*4+j-1;
            m=(j-1)*5;
            if(i<selectNulList.size){//显示数据
                //showArmyInfoForArmy(window,armyData.get(i),j);
                nul=selectNulList.get(i);
                if(nul.getNucleRound()==0){
                    w.setButtonImageNotChangeSize(2000+j,game.getImgLists().getTextureByName("card_border").getTextureRegion());
                }else{
                    w.setButtonImageNotChangeSize(2000+j,game.getImgLists().getTextureByName("card_ShaderBorder").getTextureRegion());
                }
                //卡牌
                w.setImageRegionNotChangeSize(2000+1+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,nul.getNuclearId(),7)).getTextureRegion());
                w.hidImage(2000+2+m);
                w.hidImage(2000+3+m);
                w.hidImage(2000+4+m);
                w.hidImage(2000+5+m);
                w.hidImage(2000+20+j);
                w.hidImage(2000+24+j);
                w.hidImage(2000+28+j);
                w.showButton(2000+j);
                w.hidImage(2032+j);
            }else {//隐藏数据
                w.hidImage(2000+1+m);
                w.hidImage(2000+2+m);
                w.hidImage(2000+3+m);
                w.hidImage(2000+4+m);
                w.hidImage(2000+5+m);
                w.hidImage(2000+20+j);
                w.hidImage(2000+24+j);
                w.hidImage(2000+28+j);
                w.hidButton(2000+j);
                w.hidImage(2032+j);
            }
            i++;
        }
    }



    public  void updCardWindowForArmyLoadNul( SMapGameStage smapStage, WindowGroup window, Fb2Smap.BuildData b, Fb2Smap.ArmyData a, Array<Fb2Smap.NulcleData> nulcles) {
        int cardType=7;
        Fb2Smap.LegionData l=smapStage.getPlayerLegion();

       // window.setLabelText(0, game.gameMethod.getPromptStrForGetTechByType(null,b,cardType,l));//"Lv11        12/12"


        window.setLabelText(0,game.gameMethod.getPromptStrForTechLvByType(null,b,cardType,l));
        window.setLabelText(12,game.gameMethod.getPromptStrForTechByType(null,b,cardType,l));
        float r=game.gameMethod.getRateByCardType(null,b,cardType,l);
        r=r>1f?1:r;
        window.setImageRegionWidth(10,r*window.getImage(9).getWidth(),1f);



        int cardECount=nulcles.size;
        //对进行几次判断



        int lv=0;
        for(int i=1,iMax=7;i<=iMax;i++){
            if(i<=nulcles.size){
                Gdx.app.log("setCardPrice",(i-1)+":"+cardECount);
                Fb2Smap.NulcleData n=nulcles.get(i-1);
                int cardId=n.getNuclearId();
                //TextureRegionListDAO imgRs, DefDAO defDAO, WindowGroup tw, XmlReader.Element cardE, Fb2Smap.LegionData legion, Fb2Smap.BuildData build, Fb2Smap.ForeignData f,int age, int cardType, int cardId, int index, boolean ifCanBuy
                if(a.ifCanLoadNul(cardId)){
                    setCardPriceToZero(game.getImgLists(),game.gameMethod,window,i,true);
                }else {
                    setCardPriceToZero(game.getImgLists(),game.gameMethod,window,i,false);
                }
                window.setImageRegionNotChangeSize(10+i,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,cardId,cardType)).getTextureRegion());
                //lv=GameMethod.getCardLv(l,b,cardId);
                //setCardStar(game,window,20+i,lv,cardWidth);
                window.hidImage(20+i);
                //window.setImageRegion(20+i,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,cardId)).getTextureRegion());
                // window.setImageRegion();
            }else {
                //window.setLabelText(i,"","", Color.RED,1.2f);
                window.hidLabel(i);
                // window.getButton(i).setVisible(false);
                window.hidImage(10+i);
                window.hidImage(20+i);
                // window.getImage(i).setVisible(false);
            }
        }

        if(cardECount==0){
            window.hidLabel(8);
            window.hidLabel(9);
            window.hidLabel(10);
            window.hidLabel(11);
            window.hidButton(0);
            //window.hidLabel(0);
        }else {
            //   int cardId=cardEs.get(0).getInt("id");
            //  updCardInfo(game,window,cardId);
            window.setLabelText(11,1+"/"+1);
            window.showButton(0);
        }

        //window.getLabel(9).setWidth(200);
        int round=0;
        if(cardType>0&&cardType<11){
            round=b.getBuildRound();
        }else {
            round=l.getLegionRound();
        }
        window.setLabelText(10,round+"");
        window.hidButton(8);
        window.hidButton(9);
        window.setImageRegionNotChangeSize(8,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardType(b,cardType)).getTextureRegion());

        //window.setImageRegion(8,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardType(b,cardType)));

        window.getButton(11).setVisible(false);
        window.getButton(12).setVisible(false);
        window.getButton(13).setVisible(false);
        window.getButton(14).setVisible(false);
        window.getButton(15).setVisible(false);
        window.getButton(16).setVisible(false);
        window.getButton(17).setVisible(false);
        window.getButton(18).setVisible(false);
    }




    public  void updCardWindowForAirLoadNul( SMapGameStage smapStage, WindowGroup window, Fb2Smap.BuildData b, Fb2Smap.AirData a, Array<Fb2Smap.NulcleData> nulcles) {
        int cardType=7;
        Fb2Smap.LegionData l=smapStage.getPlayerLegion();

       // window.setLabelText(0, game.gameMethod.getPromptStrForGetTechByType(null,b,cardType,l));//"Lv11        12/12"

        window.setLabelText(0,game.gameMethod.getPromptStrForTechLvByType(null,b,cardType,l));
        window.setLabelText(12,game.gameMethod.getPromptStrForTechByType(null,b,cardType,l));
        float r=game.gameMethod.getRateByCardType(null,b,cardType,l);
        r=r>1f?1:r;
        window.setImageRegionWidth(10,r*window.getImage(9).getWidth(),1f);



        int cardECount=nulcles.size;
        //对进行几次判断

        int lv=0;
        for(int i=1,iMax=7;i<=iMax;i++){
            if(i<=nulcles.size){
                Gdx.app.log("setCardPrice",(i-1)+":"+cardECount);
                Fb2Smap.NulcleData n=nulcles.get(i-1);
                int cardId=n.getNuclearId();
                //TextureRegionListDAO imgRs, DefDAO defDAO, WindowGroup tw, XmlReader.Element cardE, Fb2Smap.LegionData legion, Fb2Smap.BuildData build, Fb2Smap.ForeignData f,int age, int cardType, int cardId, int index, boolean ifCanBuy
                if(a.ifAirCanLoadNul(cardId)){
                    setCardPriceToZero(game.getImgLists(),game.gameMethod,window,i,true);
                }else {
                    setCardPriceToZero(game.getImgLists(),game.gameMethod,window,i,false);
                }


                // window.setImageRegion(10+i,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,cardId)).getTextureRegion());

                window.setImageRegionNotChangeSize(10+i,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,cardId,cardType)).getTextureRegion());

                //lv=GameMethod.getCardLv(l,b,cardId);
                //setCardStar(game,window,20+i,lv,cardWidth);
                window.hidImage(20+i);
                //window.setImageRegion(20+i,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,cardId)).getTextureRegion());
                // window.setImageRegion();
            }else {
                //window.setLabelText(i,"","", Color.RED,1.2f);
                window.hidLabel(i);
                // window.getButton(i).setVisible(false);
                window.hidImage(10+i);
                window.hidImage(20+i);
                // window.getImage(i).setVisible(false);
            }
        }

        if(cardECount==0){
            window.hidLabel(8);
            window.hidLabel(9);
            window.hidLabel(10);
            window.hidLabel(11);
            window.hidButton(0);
            //window.hidLabel(0);
        }else {
            //   int cardId=cardEs.get(0).getInt("id");
            //  updCardInfo(game,window,cardId);
            window.setLabelText(11,1+"/"+1);
            window.showButton(0);
        }

        //window.getLabel(9).setWidth(200);
        int round=0;
        if(cardType>0&&cardType<11){
            round=b.getBuildRound();
        }else {
            round=l.getLegionRound();
        }
        window.setLabelText(10,round+"");
        window.hidButton(8);
        window.hidButton(9);
        window.setImageRegionNotChangeSize(8,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardType(b,cardType)).getTextureRegion());
        //window.setImageRegion(8,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardType(b,cardType)));
        window.getButton(11).setVisible(false);
        window.getButton(12).setVisible(false);
        window.getButton(13).setVisible(false);
        window.getButton(14).setVisible(false);
        window.getButton(15).setVisible(false);
        window.getButton(16).setVisible(false);
        window.getButton(17).setVisible(false);
        window.getButton(18).setVisible(false);
    }

    //
    public void updTextWindowForLegionFeature(WindowGroup window, Fb2Smap.LegionData playerLegion) {
        if(/*playerLegion.getPlayerAmbition()==100||*/playerLegion.getTasks().size<6){
            window.hidButton(11);
        }else{
            window.showButton(11);
        }
        window.hidButton(12);
        window.setImageRegion(11,game.getImgLists().getTextureByName("flag_"+playerLegion.getCountryId()));
        XmlIntDAO xmlE= game.gameConfig.getDEF_LEGIONFEATURE();
        window.setLabelText(0,game.gameMethod.getStrValueT("resource_name_miracle",playerLegion.getMiracleNow()));
        boolean ifF=true;
        for(int i=1;i<=5;i++){

            int legionFeature=playerLegion.getLegionFeatureByIndex(i);
            int legionFeatureLv=playerLegion.getLegionFeatureLvByIndex(i);
            int legionFeatureEffect=playerLegion.getLegionFeatureEffectByIndex(i);
            if (legionFeature==0) {
                window.hidImage(i);
                window.hidImage(i+5);
                window.hidButton(i);
                window.hidButton(i+5);
                window.hidImage(i+20);
                if(playerLegion.getMiracleNow()>0&&ifF){
                    window.showButton(i+5);
                    ifF=false;
                }else{
                    window.hidButton(i+5);
                }
                window.hidLabel(i);
            }else {
                window.setImageRegionNotChangeSize(i,game.getImgLists().getTextureByName(xmlE.getElementById(legionFeature).get("image")).getTextureRegion());
                window.showImage(i+5);
                window.showImage(i+20);
                if(playerLegion.getMiracleNow()>0&&playerLegion.getMiracleNow()>=legionFeatureLv){
                    if(legionFeatureLv<5){
                        window.showButton(i);
                    }else{
                        window.hidButton(i);
                    }
                    if(legionFeatureLv>1){
                        window.showButton(i+5);
                    }else{
                        window.hidButton(i+5);
                    }

                }else {
                    window.hidButton(i);
                    window.hidButton(i+5);
                }
                window.setScrollLabel(i,game.gameMethod.getStrByLegionFeatureText(legionFeature,legionFeatureLv,legionFeatureEffect));
            }
        }
    }

    public void updTextWindowForLegionPolicy(WindowGroup window, Fb2Smap.LegionData playerLegion,Array<Fb2Smap.TaskData> taskDatas) {
        window.showButton(11);
        window.hidButton(12);
        window.setImageRegion(11,game.getImgLists().getTextureByName("flag_"+playerLegion.getCountryId()));
        if(game.getSMapDAO().masterData.getPlayerMode()==0){
            window.setLabelText(0,game.gameMethod.getStrValueT("resource_name_miracle",playerLegion.getMiracleNow()));
        }else{
            window.hidLabel(0);
        }
        for(int i=0;i<5;i++){
            if(i<taskDatas.size){
                Fb2Smap.TaskData t=taskDatas.get(i+5);
                if(game.getSMapDAO().masterData.getPlayerMode()==0){
                   /* if(playerLegion.getMiracleNow()>0&&t.getTaskType()!=2&&playerLegion.getLegionRound()==0){
                       window.setButtonImageNotChangeSize(i+6,game.getImgLists().getTextureByName("borderButton_refresh").getTextureRegion());
                    }else{
                        window.hidButton(i+6);
                    }*/
                    window.hidButton(i+6);
                    if(playerLegion.getMiracleNow()>0&&playerLegion.getMiracleNow()>=t.getStar()&&playerLegion.getLegionRound()==0){
                        window.showButton(i+1);
                    }else{
                        window.hidButton(i+1);
                    }
                }else {
                    window.hidButton(i+1);
                    window.hidButton(i+6);
                }
                window.setImageRegionNotChangeSize(i+1,game.getImgLists().getTextureByName("icon_task_star"+t.getStar()));
                window.showImage(i+21);
                window.showImage(i+6);
                window.setScrollLabel(i+1,t.getTaskNameStr()+":"+t.getTaskInfoStr());
            }else {
                window.hidButton(i+1);
                window.hidButton(i+6);
                window.hidImage(i+1);
                window.hidImage(i+6);
                window.hidImage(i+21);
                window.hidLabel(i+1);
            }
        }
    }

    public void updTextWindowForTask(WindowGroup window, Fb2Smap.LegionData playerLegion,Array<Fb2Smap.TaskData> taskDatas) {
        if(game.getSMapDAO().masterData.getBtlType()==0){
            window.showButton(11);
        }else{
            window.hidButton(11);
        }
        window.hidButton(12);
        window.setImageRegion(11,game.getImgLists().getTextureByName("flag_"+playerLegion.getCountryId()));
        if(game.getSMapDAO().masterData.getPlayerMode()==0){
            window.setLabelText(0,game.gameMethod.getStrValueT("resource_name_refreshcount",playerLegion.getTradeCount()));
        }else{
            window.hidLabel(0);
        }
        for(int i=0;i<5;i++){
            if(i<taskDatas.size){
                Fb2Smap.TaskData t=taskDatas.get(i);
                if(game.getSMapDAO().masterData.getPlayerMode()==0){
                    if(playerLegion.getTradeCount()>0&&t.getTaskType()!=2){
                        window.setButtonImageNotChangeSize(i+6,game.getImgLists().getTextureByName("borderButton_refresh").getTextureRegion());
                    }else{
                        window.hidButton(i+6);
                    }
                    t.initVirAttribute();
                    if(t.getCountNow()==t.getCountMax()){
                        window.showButton(i+1);
                    }else{
                        window.hidButton(i+1);
                    }
                }else {
                    window.hidButton(i+1);
                    window.hidButton(i+6);
                }
                window.setImageRegionNotChangeSize(i+1,game.getImgLists().getTextureByName("icon_task_star"+t.getStar()));
                window.showImage(i+6);
                window.showImage(i+21);
                window.setScrollLabel(i+1,t.getTaskNameStr()+":"+t.getTaskInfoStr());
            }else {
                window.hidButton(i+1);
                window.hidButton(i+6);
                window.hidImage(i+1);
                window.hidImage(i+6);
                window.hidImage(i+21);
                window.hidLabel(i+1);
            }
        }
    }


    public void updTextWindowForConquestTask(WindowGroup window, Fb2Smap sMapDAO) {
        if(sMapDAO.masterData.getBtlType()==0&&sMapDAO.masterData.getPlayerMode()!=0&&sMapDAO.stageId!=0){
            window.hidButton(11);
        }else{
            window.showButton(11);
        }
        Fb2Smap.LegionData pl=sMapDAO.getPlayerLegionData();
        window.setImageRegion(11,game.getImgLists().getTextureByName("flag_"+pl.getCountryId()));
        if(game.getSMapDAO().masterData.getPlayerMode()==0){
            window.setLabelText(0,game.gameMethod.getStrValue("resource_name_refreshcount",pl.getTradeCount()));
        }else{
            window.hidLabel(0);
        }
        window.showButton(21);
        window.showButton(22);
        window.showButton(23);
        window.showButton(24);
        window.showButton(25);

        window.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName("unitSkill_108"));
        window.setImageRegionNotChangeSize(2,game.getImgLists().getTextureByName("unitSkill_19"));
        window.setImageRegionNotChangeSize(3,game.getImgLists().getTextureByName("unitSkill_61"));
        window.setImageRegionNotChangeSize(4,game.getImgLists().getTextureByName("unitSkill_39"));
        window.setImageRegionNotChangeSize(5,game.getImgLists().getTextureByName("unitFeature_15"));
        window.showImage(6);
        window.showImage(7);
        window.showImage(8);
        window.showImage(9);
        window.showImage(10);
        int t=0;
        for(int i=0;i<5;i++){
            window.setScrollLabel(1+i,sMapDAO.getCheckConquestStr(i+1));
            if(sMapDAO.checkConquestTask(i+1)){//全部领土掌握且控制不少于15快区域
                window.setButtonImageNotChangeSize(6+i,game.getImgLists().getTextureByName("circleButton_ok").getTextureRegion());
                t++;
            }else {
                window.setButtonImageNotChangeSize(6+i,game.getImgLists().getTextureByName("circleButton_close").getTextureRegion());
            }
        }
        int round= (int) (sMapDAO.masterData.getRoundMax()* DefDAO.getRoundRate(sMapDAO.masterData.getGameDifficulty()));
        if(t>0&&sMapDAO.getRoundProgress()>10&&sMapDAO.masterData.getRoundNow()<=round){
            t=Math.min(t,sMapDAO.masterData.getGameDifficulty()+2);
            window.setButtonImageNotChangeSize(12,game.getImgLists().getTextureByName(DefDAO.getGameResultEvaluate(t)).getTextureRegion());
        }else {
            window.hidButton(12);
        }
        window.hidButton(1);
        window.hidButton(2);
        window.hidButton(3);
        window.hidButton(4);
        window.hidButton(5);
        window.hidLabel(0);

    }


    public void initEmpireGroupForMain(WindowGroup w){
        TextureRegion blankT =null;
        XmlReader.Element xmlE=  game.gameConfig.getDEF_EMPIRE().e;
        for(int i=0;i<xmlE.getChildCount();i++) {
            XmlReader.Element stageE=xmlE.getChild(i);
            if(stageE==null){continue;}
            int stageId=stageE.getInt("id",0);
            int country=stageE.getInt("country",-1);
            if(!game.gameConfig.ifCanUseInLanguage(stageE.get("useLanguage","-1"))||(country>0&&game.gameConfig.ifShield&&ComUtil.ifHaveValueInStr(game.gameConfig.shieldCountrys,country)   )     ){
                w.hidButton(stageId);
                w.hidImage(stageId);
            }else{
                ImageButton ib = w.getButton(stageId);
                int score = game.gameMethod.getEmpireScore(stageId);

                if (ib != null && score > 0) {
                    w.setButtonImageNotChangeSize(stageId, game.getImgLists().getTextureByName(DefDAO.getImageNameFlagBorderByScore(score)).getTextureRegion());
                } else {
                    if(blankT==null){
                        blankT = game.getImgLists().getBlankRegionDAO("flagBorder_1").getTextureRegion();
                    }
                    w.setButtonImageNotChangeSize(stageId, blankT);
                }
            }
        }
       /* XmlReader.Element xmlE=  game.gameConfig.getDEF_STAGE().e;
        for(int i=0;i<xmlE.getChildCount();i++) {
            XmlReader.Element stageE=xmlE.getChild(i);
            if(stageE==null||stageE.getInt("btlType",0)!=1|| !game.gameConfig.ifCanUseInLanguage(stageE.get("useLanguage","-1"))){
                continue;
            }
            int stageId=stageE.getInt("id",0);

            ImageButton ib = w.getButton(stageId);
            int score = game.gameMethod.getEmpireScore(stageId);

            if (ib != null && score > 0) {
                w.setButtonImageNotChangeSize(stageId, game.getImgLists().getTextureByName(DefDAO.getImageNameFlagBorderByScore(score)).getTextureRegion());
            } else {
                if(blankT==null){
                    blankT = game.getImgLists().getTextureByName("flagBorder_1").cpyBlank().getTextureRegion();
                }
                w.setButtonImageNotChangeSize(stageId, blankT);
            }
        }*/
    }


    public void updEmpireDialogueGroupId(WindowGroup w, int countryId,int generalId, String diogiueStr, boolean ifSave,int stageId) {
        //test if(true){ return;}
        w.setImageRegion(1,game.getFlagBgTextureRegion(countryId));
        XmlReader.Element gE=  game.gameConfig.getDEF_GENERAL().getElementById(generalId);
        w.setImageRegion(2,game.getGeneralTextureRegion(0,gE.get("image")));
        w.setLabelText(0,game.gameMethod.getStrValueT("generalName_"+gE.getInt("id")));
        w.setScrollLabel(1,game.gameMethod.getStrValueT(diogiueStr));
        if(countryId==0){
            w.hidButton(1);
            w.hidButton(2);
        }else if(ifSave){
            w.showButton(1);
            w.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("button_restar").getTextureRegion());
        }else{
            w.hidButton(1);
            w.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("selectCountry_confirm").getTextureRegion());
        }

        if(stageId==-1){
            w.hidImage(0);
        }else {
            int score=game.gameMethod.getEmpireScore(stageId);
            //score=stageId-1000;
            if(score==0){
                w.hidImage(0);
            }else{
                w.setImageRegion(0,game.getImgLists().getTextureByName(DefDAO.getGameResultEvaluate(score)).getTextureRegion());
            }
        }



      /*  for (int i=3;i<=10;i++) {
            Image image=w.getImage(i);
            image.setVisible(true);
            image.setOrigin(Align.center);
            RotateByAction rotateBy = Actions.rotateBy(360.0F, 3.0F);
            RepeatAction repeatAction = Actions.forever(rotateBy);
            image.addAction(repeatAction);
        }*/

    }

    //
    public void updAchievementWindowForMedal(WindowGroup w, Fb2Smap btl) {
        int asiaACount=0;
        int asiaPCount=0;
        int europeACount=0;
        int europePCount=0;
        int northAmericaACount=0;
        int northAmericaPCount=0;
        int southAmericaACount=0;
        int southAmericaPCount=0;
        int africaACount=0;
        int africaPCount=0;
        int oceaniaACount=0;
        int oceaniaPCount=0;


        for(int i=21;i<=40;i++){
            w.hidImage(i);
        }
        for(int i=1;i<=23;i++){
            w.hidButton(i);
        }
        w.hidImage(10);
        /*Iterator<IntMap.Entry<Fb2Smap.BuildData>> itb = btl.buildRDatas.iterator();
        while (itb.hasNext()) {
            Fb2Smap.BuildData b = itb.next().value;*/
        for(int bi=0;bi< btl.buildRDatas.size();bi++) {
            Fb2Smap.BuildData b= btl.buildRDatas.getByIndex(bi);
            if(b!=null&&b.getIntercontinentalZone()!=0){
                switch (b.getIntercontinentalZone()){
                    case 1:
                        asiaACount++;
                        if(b.isPlayerAlly()){asiaPCount++;}
                        break;
                    case 2:
                        europeACount++;
                        if(b.isPlayerAlly()){europePCount++;}
                        break;
                    case 3:
                        northAmericaACount++;
                        if(b.isPlayerAlly()){northAmericaPCount++;}
                        break;
                    case 4:
                        southAmericaACount++;
                        if(b.isPlayerAlly()){southAmericaPCount++;}
                        break;
                    case 5:
                        africaACount++;
                        if(b.isPlayerAlly()){africaPCount++;}
                        break;
                    case 6:
                        oceaniaACount++;
                        if(b.isPlayerAlly()){oceaniaPCount++;}
                        break;
                }
            }
        }

        int rank=game.gameConfig.getPlayerRank();
        int asiaR=asiaACount==0?0:asiaPCount*100/asiaACount;
        int europeR=europeACount==0?0:europePCount*100/europeACount;
        int northAmericaR=northAmericaACount==0?0:northAmericaPCount*100/northAmericaACount;
        int southAmericaR=southAmericaACount==0?0:southAmericaPCount*100/southAmericaACount;
        int africaR=africaACount==0?0:africaPCount*100/africaACount;
        int oceaniaR=oceaniaACount==0?0:oceaniaPCount*100/oceaniaACount;
        w.setLabelText(0,btl.getStageName()+" "+btl.masterData.getRoundNow()+"/"+btl.masterData.getRoundMax());
        w.setLabelText(1,game.gameMethod.getStrValueT("prompt_achievement","rankName_"+rank,btl.playerInfo.worldProgress,europeR,africaR,asiaR,oceaniaR,northAmericaR,southAmericaR),true);
        if(asiaR>90){
            w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName("medal_Asia_3").getTextureRegion());
        }else if(asiaR>70){
            w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName("medal_Asia_2").getTextureRegion());
        }else if(asiaR>50){
            w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName("medal_Asia_1").getTextureRegion());
        }else{
            w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName("medal_Asia_0").getTextureRegion());
        }
        if(europeR>90){
            w.setImageRegionNotChangeSize(2,game.getImgLists().getTextureByName("medal_Europe_3").getTextureRegion());
        }else if(europeR>70){
            w.setImageRegionNotChangeSize(2,game.getImgLists().getTextureByName("medal_Europe_2").getTextureRegion());
        }else if(europeR>50){
            w.setImageRegionNotChangeSize(2,game.getImgLists().getTextureByName("medal_Europe_1").getTextureRegion());
        }else{
            w.setImageRegionNotChangeSize(2,game.getImgLists().getTextureByName("medal_Europe_0").getTextureRegion());
        }
        if(northAmericaR>90){
            w.setImageRegionNotChangeSize(3,game.getImgLists().getTextureByName("medal_NorthAmerica_3").getTextureRegion());
        }else if(northAmericaR>70){
            w.setImageRegionNotChangeSize(3,game.getImgLists().getTextureByName("medal_NorthAmerica_2").getTextureRegion());
        }else if(northAmericaR>50){
            w.setImageRegionNotChangeSize(3,game.getImgLists().getTextureByName("medal_NorthAmerica_1").getTextureRegion());
        }else{
            w.setImageRegionNotChangeSize(3,game.getImgLists().getTextureByName("medal_NorthAmerica_0").getTextureRegion());
        }
        if(southAmericaR>90){
            w.setImageRegionNotChangeSize(4,game.getImgLists().getTextureByName("medal_SouthAmerica_3").getTextureRegion());
        }else if(southAmericaR>70){
            w.setImageRegionNotChangeSize(4,game.getImgLists().getTextureByName("medal_SouthAmerica_2").getTextureRegion());
        }else if(southAmericaR>50){
            w.setImageRegionNotChangeSize(4,game.getImgLists().getTextureByName("medal_SouthAmerica_1").getTextureRegion());
        }else{
            w.setImageRegionNotChangeSize(4,game.getImgLists().getTextureByName("medal_SouthAmerica_0").getTextureRegion());
        }
        if(africaR>90){
            w.setImageRegionNotChangeSize(5,game.getImgLists().getTextureByName("medal_Africa_3").getTextureRegion());
        }else if(africaR>70){
            w.setImageRegionNotChangeSize(5,game.getImgLists().getTextureByName("medal_Africa_2").getTextureRegion());
        }else if(africaR>50){
            w.setImageRegionNotChangeSize(5,game.getImgLists().getTextureByName("medal_Africa_1").getTextureRegion());
        }else{
            w.setImageRegionNotChangeSize(5,game.getImgLists().getTextureByName("medal_Africa_0").getTextureRegion());
        }
        if(oceaniaR>90){
            w.setImageRegionNotChangeSize(6,game.getImgLists().getTextureByName("medal_Oceania_3").getTextureRegion());
        }else if(oceaniaR>70){
            w.setImageRegionNotChangeSize(6,game.getImgLists().getTextureByName("medal_Oceania_2").getTextureRegion());
        }else if(oceaniaR>50){
            w.setImageRegionNotChangeSize(6,game.getImgLists().getTextureByName("medal_Oceania_1").getTextureRegion());
        }else{
            w.setImageRegionNotChangeSize(6,game.getImgLists().getTextureByName("medal_Oceania_0").getTextureRegion());
        }

        if(rank>0){
            w.setImageRegionNotChangeSize(7,game.getImgLists().getTextureByName("rank_"+rank).getTextureRegion());
        }else{
            w.setImageRegionNotChangeSize(7,game.getImgLists().getTextureByName("rank_1").getTextureRegion());
        }
        w.setImageRegion(8,game.getImgLists().getTextureByName("flag_"+btl.getPlayerLegionData().getCountryId()));
        w.setImageRegion(9,game.getImgLists().getTextureByName(DefDAO.getImageNameFlagBorderByDifficulty(btl.masterData.getGameDifficulty())).getTextureRegion());

    }



    public void updAchievementWindowForSpirit(WindowGroup w,SMapGameStage.SMapPublicRescource res, Fb2Smap btl,int page) {

        for(int i=21;i<=40;i++){
            w.hidImage(i);
        }
        for(int i=1;i<=9;i++){
            w.hidImage(i);
        }
        if(game.gameNet.getStatus()==1){//奇物开关
            w.showButton(23);
        }else {
            w.hidImage(23);
        }
        w.showButton(0);
        w.hidImage(7);
        w.hidImage(8);
        w.hidImage(9);
        w.hidImage(10);
        if(btl.getPlayerAmbition()==100){
            w.setLabelText(0,btl.getStageName());
        }else{
            w.setLabelText(0,btl.getStageName()+" "+btl.getGame().gameMethod.getStrValueT(DefDAO.getConceptStr(btl.getPlayerAmbition())));
        }

        //游戏难度{0},拥有奇物{1},威望值{2}
        int spiritC=0;
        int ambition=btl.getPlayerAmbitionValue();
        if(btl.spiritMap!=null&&btl.spiritMap.size>0){
            spiritC=btl.spiritMap.size;
        }
        w.setLabelText(1,game.gameMethod.getStrValueT("prompt_spirit",DefDAO.getDifficultStr(btl.masterData.getGameDifficulty()),spiritC,ambition),true);
        for(int i=1;i<=20;i++){
            w.setButtonImageNotChangeSize(i,res.spiritNoneDAO.getTextureRegion());
        }
        if(btl.spiritMap!=null&&btl.spiritMap.size>0){
            Iterator<IntIntMap.Entry> itS = btl.spiritMap.iterator();
            int bi=(page-1)*20+1,i=1; //1~20
            int bc=0;
            while (itS.hasNext()) {
                int  spiritId = itS.next().key;
                if(i>=bi&&i<bi+20){
                    bc++;
                    w.setButtonImageNotChangeSize(bc,game.getImgLists().getTextureByName("spirit_"+spiritId).getTextureRegion());
                    Gdx.app.log("updAchievementWindowForSpirit", i+":"+spiritId);
                }
                if(bc>20){
                    break;
                }
                i++;
            }
            if(btl.spiritMap.size>20) {
                w.showButton(21);
                w.showButton(22);
            }else {
                w.hidButton(21);
                w.hidButton(22);
            }
        }else{
            for(int i=1;i<=20;i++){
                w.setButtonImageNotChangeSize(i,res.spiritNoneDAO.getTextureRegion());
            }
            w.hidButton(21);
            w.hidButton(22);
        }
    }
    public void updMainGroup(WindowGroup w) {
        w.setX(0);
        w.setY(0);
        //左上角
        w.setImageRegionNotChangeSize(1,game.getFlagBgTextureRegion(game.gameConfig.playerConfig.getInteger("lastCountry",0)));

        int empireId=game.gameConfig.playerConfig.getInteger("lastEmpire",-1);
        if(empireId==-1){empireId=ComUtil.getRandom(0,7);
            game.gameConfig.playerConfig.putInteger("lastEmpire",empireId);
            game.gameConfig.playerConfig.flush();
        }
        if(game.gameConfig.ifEffect&&game.resGameConfig.enableGeneral){
            w.setButtonImageNotChangeSize(0,game.getGeneralTextureRegion(game.gameConfig.playerConfig.getInteger("lastGeneral",-1)));
        }else {
            w.setButtonImageNotChangeSize(0,game.getGeneralTextureRegion(-1));
        }

        w.setButtonImageNotChangeSize(1,game.getImgLists().getTextureByName("mainempire_"+empireId).getTextureRegion());
        w.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("mainconquest_"+game.gameConfig.mainBgConquestId).getTextureRegion());
        w.setButtonImageNotChangeSize(3,game.getEventBgTextureRegion(game.gameConfig.mainBgEventId));
        /*TextureRegion region=game.getLastCountryTextureRegion();
        if(region==null){
            w.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("mainoption_"+game.gameConfig.mainBgOptionId).getTextureRegion());
        }else {
            w.setButtonImageNotChangeSize(4,region);
        }*/
        w.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("mainoption_"+game.gameConfig.mainBgEditId).getTextureRegion());




        //右下角
        w.setImageRegionNotChangeSize(2,game.getFlagBgTextureRegion(game.gameConfig.playerConfig.getInteger("lastAchievementFlagBg",0)));
        // w.setImageRegionNotChangeSize(4,game.getImgLists().getTextureByName(game.gameConfig.config.getString("lastAchievement","medal_Africa_0")).getTextureRegion());
        //让勋章呈比例缩放
        float tw=w.getImage(2).getWidth();
        float th=w.getImage(2).getHeight();

        float ts=tw*100/th;
        TextureRegion t=game.getImgLists().getTextureByName(game.gameConfig.playerConfig.getString("lastAchievement","medal_Africa_0")).getTextureRegion();
        float ts2=t.getRegionWidth()*100/t.getRegionHeight();
        float tw2=ts2*th/100;

        w.setButtonImageRegion(5,t,tw2,th);
        float c=(tw-tw2)/2;
        w.buttonToOffset(5,c,0);


    }

    public void initConquestGroupForStar(WindowGroup w) {

        //设置游戏条
        float width=w.getButton(1).getImage().getDrawable().getMinWidth();
        int score=game.gameConfig.playerConfig.getInteger("1_0_score",0);
        if(score==0){
            w.hidButton(1);
        }else if(score<3){//铜
            w.setButtonImage(1,game.getImgLists().getTextureByName("icon_lvpentagram_1").getNewRegion(0,0, (int) (width*score/5),-1));
        }else if(score<5){
            w.setButtonImage(1,game.getImgLists().getTextureByName("icon_lvpentagram_2").getNewRegion(0,0, (int) (width*score/5),-1));
        }score=game.gameConfig.playerConfig.getInteger("2_0_score",0);
        if(score==0){
            w.hidButton(2);
        }else if(score<3){//铜
            w.setButtonImage(2,game.getImgLists().getTextureByName("icon_lvpentagram_1").getNewRegion(0,0, (int) (width*score/5),-1));
        }else if(score<5){
            w.setButtonImage(2,game.getImgLists().getTextureByName("icon_lvpentagram_2").getNewRegion(0,0, (int) (width*score/5),-1));
        }score=game.gameConfig.playerConfig.getInteger("3_0_score",0);
        if(score==0){
            w.hidButton(3);
        }else if(score<3){//铜
            w.setButtonImage(3,game.getImgLists().getTextureByName("icon_lvpentagram_1").getNewRegion(0,0, (int) (width*score/5),-1));
        }else if(score<5){
            w.setButtonImage(3,game.getImgLists().getTextureByName("icon_lvpentagram_2").getNewRegion(0,0, (int) (width*score/5),-1));
        }score=game.gameConfig.playerConfig.getInteger("4_0_score",0);
        if(score==0){
            w.hidButton(4);
        }else if(score<3){//铜
            w.setButtonImage(4,game.getImgLists().getTextureByName("icon_lvpentagram_1").getNewRegion(0,0, (int) (width*score/5),-1));
        }else if(score<5){
            w.setButtonImage(4,game.getImgLists().getTextureByName("icon_lvpentagram_2").getNewRegion(0,0, (int) (width*score/5),-1));
        }
    }


    public void initHistoryGroupForMain(WindowGroup w, XmlReader.Element historyE) {
        XmlReader.Element gE=  game.gameConfig.getDEF_GENERAL().getElementById(0);
        w.setImageRegion(1,game.getFlagBgTextureRegion(0));
        w.setImageRegion(2,game.getGeneralTextureRegion(0,gE.get("image")));

        w.hidButton(1);
        w.hidButton(2);
        w.hidButton(3);
        w.hidButton(4);
        int hs=historyE.getChildCount();
        for(int i=0;i<10;i++){
            if(i<hs){
                XmlReader.Element hE=historyE.getChild(i);
                if(game.gameConfig.ifCanUseInLanguage(hE.get("useLanguage","-1"))){
                    w.setButtonImage(1001+i,game.getImgLists().getTextureByName("historyType_"+hE.getInt("type",0)));
                    w.resetButtonPotion(1001+i,hE.getInt("x",0),hE.getInt("y",0),0,0,0,0,false);
                }else{
                    w.hidButton(1001+i);
                }
            }else{
                w.hidButton(1001+i);
            }
        }
        w.setLabelText(0,game.gameMethod.getStrValueT("generalName_"+gE.getInt("id")));
        w.setLabelText(1,game.gameMethod.getStrValueT("history_prologue"),true);
        w.setLabelText(2,historyE.get("id"));
        game.setStageId(-1);

    }

    public void updHistoryGroupForDialogue(WindowGroup w,XmlReader.Element hE){
        w.setImageRegionNotChangeSize(2,game.getEventBgTextureRegion(hE.getInt("eventbg",0)));
        int stageId=hE.getInt("id");
        game.setStageId(stageId);
        w.setLabelText(0,game.gameMethod.getStrValueT("stage_name_"+stageId));
        w.setScrollLabel(1,game.gameMethod.getStrValueT("stage_prologue_"+stageId));


        XmlReader.Element sE=game.gameConfig.getDEF_STAGE().getElementById(stageId);
        if(sE==null){
            w.hidButton(1);
            w.hidButton(2);
            w.hidButton(3);
            w.hidButton(4);
            w.hidImage(3);
        }else{
            if(game.gameConfig.playerConfig.getBoolean(stageId+"_ifSave",false)){
                w.showButton(1);
                w.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("button_restar").getTextureRegion());
            }else{
                w.hidButton(1);
                w.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("selectCountry_confirm").getTextureRegion());
            }
            int score=game.gameMethod.getStageScoreExceptEmpire(stageId);
            if(score==0){
                w.hidImage(3);
            }else{
                w.setImageRegion(3,game.getImgLists().getTextureByName(DefDAO.getGameResultEvaluate(score)).getTextureRegion());
            }
            TextureRegion r=game.getPreviewTextureRegion(sE.get("name"));
            if(r!=null){
                w.setImageButtonToFixSize(4, r,false);
                w.setImageButtonToFixImageButton(3,4, ResDefaultConfig.Image.BORDER_IMAGE_REFW, ResDefaultConfig.Image.BORDER_IMAGE_REFH);
            }else {
                w.hidButton(3);
                w.hidButton(4);
            }

        }
    }


    public void initMapEditGroup(WindowGroup w) {
        w.hidButton(1);
        w.hidButton(2);
        w.hidButton(3);
        XmlReader.Element gE=  game.gameConfig.getDEF_GENERAL().getElementById(0);
        w.setImageRegion(1,game.getFlagBgTextureRegion(0));
        w.setImageRegion(2,game.getGeneralTextureRegion(0,gE.get("image")));
        w.setLabelText(0,game.gameMethod.getStrValueT("generalName_"+gE.getInt("id")));

        w.setLabelText(1,game.gameMethod.getStrValueT("mapEdit_prologue"),true);
        w.hidImage(3);

    }

    //type 1 map 2 edit
    public void updMapEditGroup(WindowGroup w,XmlReader.Element mapE,int type){
        w.showButton(1);
        w.showButton(2);

        w.setLabelText(2,mapE.get("name"));
        w.setLabelText(1,mapE.toString());

        //  w.setImageRegionAndCentered(3,game.getPreviewTextureRegion(mapE.get("name")),1);


        w.setImageToFixSize(4, game.getPreviewTextureRegion(mapE.get("name")),false);
        w.setImageToFixImage(3,4, ResDefaultConfig.Image.BORDER_IMAGE_REFW, ResDefaultConfig.Image.BORDER_IMAGE_REFH);
        if(type==1){
            if(game.gameConfig.getPlatform().equals("Desktop")||game.gameConfig.getPlatform().equals("HeadlessDesktop")){
                w.showButton(3);
            }else{
                w.hidButton(3);
            }
        }else if(type==2){
            if(mapE.getInt("btlType",0)==1||mapE.getInt("id",-1)==0){
                w.hidButton(3);
            }else {
                w.showButton(3);
            }
        }else {
            w.hidButton(3);
        }

    }

    public void setTButtonTextAndPotionAndAdaptWidth(TextButton t, float x, float y, float minW, float h, int refW, int lineCount, String text) {

        if(t!=null) {
            if (!ComUtil.isEmpty(text)) {
                t.setVisible(true);
                t.getLabel().setText(text);
                t.getLabel().setHeight(h);
                // t.getLabel().setWidth(w);
                t.getLabel().setWrap(false);
                float width = t.getLabel().getPrefWidth() / lineCount + refW;
                if(width<minW){
                    width=minW;
                }
                t.setWidth(width);
                t.setHeight(h);
                t.setX(x);
                t.setY(y);
                //t.getLabel().setDebug(true);
                t.getLabel().setWrap(true);
               /* if(t.getHeight()<t.getLabel().getPrefHeight()){
                    t.setHeight(t.getPrefHeight());
                }*/
                /*if(ifAdaptFont){
                    //t.getLabel().setFontScale(1f);
                    float lh=t.getLabel().getPrefHeight();
                    float fh=t.getLabel().getFontScaleY();
                    float ft=t.getLabel().getStyle().font.getLineHeight();

                    float fontScale=t.getHeight()/t.getLabel().getStyle().font.getLineHeight();
                    t.getLabel().setFontScale(  fontScale);
                    setTButtonTextAndPotionAndAdaptWidth( t,  x,  y,  minW,  h,  refW,  lineCount,  text, false);
                }*/
            } else {
                t.setVisible(false);
            }
        }
    }


    //needTest
    public void setTButtonTextAndPotionAndAdaptHeight(TextButton t, float x, float y, float w, String text) {

        if(t!=null){
            if(!ComUtil.isEmpty(text)){
                t.setVisible(true);
                t.getLabel().setText(text);
                t.getLabel().setWrap(true);
                t.setWidth(w);
                t.getLabel().setWidth(w);
                float height=t.getLabel().getPrefHeight();
                t.setHeight(height);
                t.setX(x);
                t.setY(y-height);
            }else {
                t.setVisible(false);
            }
        }
    }

    public void setTButtonTextAndPotionAndAdapt(TextButton t, float x, float y, float w, float h, String text) {

        if(t!=null){
            if(!ComUtil.isEmpty(text)){
                t.setVisible(true);
                t.getLabel().setText(text);
                t.getLabel().setWidth(w);
                //t.getLabel().setWrap(false);
                //  float height=t.getLabel().getPrefHeight()/columnCount+10;
                t.setWidth(w);
                t.setHeight(h);
                float height=t.getLabel().getPrefHeight();
                if(height>t.getHeight()){
                    t.setHeight(height);
                }
                t.setX(x);
                t.setY(y);
                t.getLabel().setWrap(true);
            }else {
                t.setVisible(false);
            }
        }
    }




    public void updSmapSelectCountryGroup(WindowGroup w, Fb2Smap sMap, Fb2Smap.LegionData legion) {
        w.setImageRegion(ResDefaultConfig.Class.SMapScreenSelectCountryGroupImageFlagId,game.getImgLists().getTextureByName("flag_"+legion.getCountryId()).getTextureRegion());
        w.setLabelText(0,game.gameMethod.getStrValue("stage_mode_"+sMap.masterData.getPlayerMode())+"-"+legion.legionName);

        w.setLabelText(1,legion.varRegionCount+"");
        w.hidLabel(2);
        if(sMap.masterData.getGameDifficulty()==0){
            sMap.masterData.setGameDifficulty(1);
        }
        updSelectCountryGroupForGameDifficulty(w,sMap);
        updSelectCountryGroupForGameMode(w,sMap,legion);

    }

    public void updSmapDialogueGroup(WindowGroup dialogueWindow,int titleId, int textId, int style) {


        if(dialogueWindow==null){
            return;
        }
        if(style==0){
            dialogueWindow.showButton(1);
            dialogueWindow.showButton(2);
        }else if(style==1){
            dialogueWindow.hidButton(1);
            dialogueWindow.showButton(2);
        }
        dialogueWindow.hidButton(3);
        dialogueWindow.hidButton(4);
        dialogueWindow.hidImage(0);
        dialogueWindow.hidImage(1);

        dialogueWindow.setLabelText(0,game.gameMethod.getStrValue("window_title_"+titleId));
        dialogueWindow.hidLabel(1);
        dialogueWindow.setLabelText(2,game.gameMethod.getStrValue("window_text_"+textId));
        dialogueWindow.setVisible(true);
    }




    public void updSelectCountryGroupForGameDifficulty(WindowGroup w, Fb2Smap sMap) {
        switch (sMap.masterData.getGameDifficulty()){
            case 1:w.setButtonImageNotChangeSize(3,game.getImgLists().getTextureByName("flagStar_1").getTextureRegion()); break;
            case 2:w.setButtonImageNotChangeSize(3,game.getImgLists().getTextureByName("flagStar_2").getTextureRegion());break;
            case 3:w.setButtonImageNotChangeSize(3,game.getImgLists().getTextureByName("flagStar_3").getTextureRegion());break;
            default: w.hidButton(3);break;
        }
    }

    public void updSelectCountryGroupForGameMode(WindowGroup w, Fb2Smap sMap, Fb2Smap.LegionData legion) {
        switch (sMap.masterData.getPlayerMode()){
            case 0:w.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("icon_peace").getTextureRegion()); break;
            case 1:w.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("icon_enemy").getTextureRegion()); break;
            case 2:w.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("icon_ally").getTextureRegion()); break;
            default: w.hidButton(4);break;
        }
        if(legion.isEditMode(false)){
            w.hidButton(4);
        }
        w.setLabelText(0,game.gameMethod.getStrValue("stage_mode_"+sMap.masterData.getPlayerMode())+"-"+legion.legionName);
    }

    public void initConquestGroupForPrompt(WindowGroup w) {
        w.setLabelText(0,game.gameMethod.getStrValueT("prompt_conquest_title"));
        w.setScrollLabel(1,game.gameMethod.getStrValueT("prompt_conquest_info"));
        //  w.setScrollLabel(1,game.gameMethod.getMergeStr("gamePrompt_",1,38));
    }
    public void initHistoryGroupForPrompt(WindowGroup w) {
        w.setLabelText(0,game.gameMethod.getStrValueT("prompt_history_title"));
        w.setScrollLabel(1,game.gameMethod.getStrValueT("prompt_history_info"));
    }

    public void updAmbitionForSMapScreen(WindowGroup w, Fb2Smap sMapDAO) {
        if(!sMapDAO.ifSystemEffective(16)){
            return;
        }
        int ambition=sMapDAO.getPlayerAmbition();
        Image image=w.getImage(2);
        if(image!=null){
            int width= (int) (image.getWidth()*ambition/200/1.5f);
            w.setImageRegion(3,game.getImgLists().getNewRegion("icon_bluebar",0,0,width,-1),1.5f);
        }

    }



    //210318
    public void initHQGroup(WindowGroup w,WindowGroup w2) {
        int result=game.gameConfig.playerConfig.getInteger("NorthAmericaConquestNum",0);
        if(result>=6){
            w2.setButtonImageNotChangeSize(1,game.getImgLists().getTextureByName("medal_NorthAmerica_3").getTextureRegion());
        }else if(result>=3){
            w2.setButtonImageNotChangeSize(1,game.getImgLists().getTextureByName("medal_NorthAmerica_2").getTextureRegion());
        }else if(result>=1){
            w2.setButtonImageNotChangeSize(1,game.getImgLists().getTextureByName("medal_NorthAmerica_1").getTextureRegion());
        }else {
            w2.setButtonImageNotChangeSize(1,game.getImgLists().getTextureByName("medal_NorthAmerica_0").getTextureRegion());
        }


        result=game.gameConfig.playerConfig.getInteger("SouthAmericaConquestNum",0);
        if(result>=6){
            w2.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("medal_SouthAmerica_3").getTextureRegion());
        }else if(result>=3){
            w2.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("medal_SouthAmerica_2").getTextureRegion());
        }else if(result>=1){
            w2.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("medal_SouthAmerica_1").getTextureRegion());
        }else {
            w2.setButtonImageNotChangeSize(2,game.getImgLists().getTextureByName("medal_SouthAmerica_0").getTextureRegion());
        }


        result=game.gameConfig.playerConfig.getInteger("EuropeConquestNum",0);
        if(result>=6){
            w2.setButtonImageNotChangeSize(3,game.getImgLists().getTextureByName("medal_Europe_3").getTextureRegion());
        }else if(result>=3){
            w2.setButtonImageNotChangeSize(3,game.getImgLists().getTextureByName("medal_Europe_2").getTextureRegion());
        }else if(result>=1){
            w2.setButtonImageNotChangeSize(3,game.getImgLists().getTextureByName("medal_Europe_1").getTextureRegion());
        }else {
            w2.setButtonImageNotChangeSize(3,game.getImgLists().getTextureByName("medal_Europe_0").getTextureRegion());
        }


        result=game.gameConfig.playerConfig.getInteger("AfricaConquestNum",0);
        if(result>=6){
            w2.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("medal_Africa_3").getTextureRegion());
        }else if(result>=3){
            w2.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("medal_Africa_2").getTextureRegion());
        }else if(result>=1){
            w2.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("medal_Africa_1").getTextureRegion());
        }else {
            w2.setButtonImageNotChangeSize(4,game.getImgLists().getTextureByName("medal_Africa_0").getTextureRegion());
        }


        result=game.gameConfig.playerConfig.getInteger("AsiaConquestNum",0);
        if(result>=6){
            w2.setButtonImageNotChangeSize(5,game.getImgLists().getTextureByName("medal_Asia_3").getTextureRegion());
        }else if(result>=3){
            w2.setButtonImageNotChangeSize(5,game.getImgLists().getTextureByName("medal_Asia_2").getTextureRegion());
        }else if(result>=1){
            w2.setButtonImageNotChangeSize(5,game.getImgLists().getTextureByName("medal_Asia_1").getTextureRegion());
        }else {
            w2.setButtonImageNotChangeSize(5,game.getImgLists().getTextureByName("medal_Asia_0").getTextureRegion());
        }


        result=game.gameConfig.playerConfig.getInteger("OceaniaConquestNum",0);
        if(result>=6){
            w2.setButtonImageNotChangeSize(6,game.getImgLists().getTextureByName("medal_Oceania_3").getTextureRegion());
        }else if(result>=3){
            w2.setButtonImageNotChangeSize(6,game.getImgLists().getTextureByName("medal_Oceania_2").getTextureRegion());
        }else if(result>=1){
            w2.setButtonImageNotChangeSize(6,game.getImgLists().getTextureByName("medal_Oceania_1").getTextureRegion());
        }else {
            w2.setButtonImageNotChangeSize(6,game.getImgLists().getTextureByName("medal_Oceania_0").getTextureRegion());
        }
        result=game.gameConfig.getPlayerRank();
        if(result<=0||result>15){
            w.hidButton(7);
        }else {
            w.setButtonImage(7,game.getImgLists().getTextureByName("rank_"+result));
        }

        w.setLabelText(0,game.gameMethod.getStrValueForHQExplain(),true);
    }

    //该group小组由五个要素组成 背景image1 国旗背景image2 完成度image3 将军image4 勋章image5 名称label 1
    public void initSagaSelectGroup(GeneralScreen generalScreen, WindowGroup w, int age) {
        w.hideAll();
        Array<XmlReader.Element>  sagaEs= game.gameConfig.getSagaXmlEs(age);
        int borderW= (int) (w.getStage().getWidth()*0.05f);
        int imgH=(int) (w.getStage().getHeight()*0.4);
        int imgW= (int) (imgH*1f/140*162);
        int imgX=borderW;
        int imgY= (int) (w.getStage().getHeight()-imgH)/2+40;
        float scale=imgW*1f/162;
        TextureRegionDAO t=game.getImgLists().getTextureByName("icon_lvpentagram_1");
        float starW=t.getW();
        int score=0;
        w.setX(0);w.setY(0);
        for(int i=0;i<sagaEs.size;i++){
            XmlReader.Element sagaE=sagaEs.get(i);
            if(!game.gameConfig.ifCanUseInLanguage(sagaE.get("useLanguage","-1"))){
                continue;
            }
            Image bgImg=w.getImage(i+100);
            if(bgImg==null){
                bgImg=new Image(game.getImgLists().getTextureByName("colorBlock_3").getTextureRegion());
                bgImg.setName("colorBlock_3");
                w.addImg(i+100,bgImg);
            }
            bgImg.setVisible(true);
            Image flagImg=w.getImage(i+200);
            if(flagImg==null){
                flagImg=new Image();
                flagImg.setWidth(imgW);
                flagImg.setHeight(imgH);
                w.addImg(i+200,flagImg);
            }
            flagImg.setX(imgX);
            flagImg.setY(imgY);
            w.setImageToFixImage(i+100,i+200,ResDefaultConfig.Image.BORDER_IMAGE_REFW/2,ResDefaultConfig.Image.BORDER_IMAGE_REFW/2, ResDefaultConfig.Image.BORDER_IMAGE_REFH/2, ResDefaultConfig.Image.BORDER_IMAGE_REFH/2+60);

            int countryId= game.gameConfig.playerConfig.getInteger("lastSagaCountryId_"+age+"_"+i,-1);
            if(countryId==-1){
                countryId=sagaE.getInt("country");
            }
            w.setImageRegionNotChangeSize(i+200,game.getFlagBgTextureRegion(countryId));

            int generalId= game.gameConfig.playerConfig.getInteger("lastSagaGeneralId_"+age+"_"+i,-1);
            if(generalId==-1){
                generalId=sagaE.getInt("general");
            }

            Image generalImage=w.getImage(i+300);
            TextureRegion region=game.getGeneralTextureRegion(generalId);
            if(generalImage==null){
                generalImage = new Image(region);
                w.addImg(i+300,generalImage);
                //generalScreen.function(generalImage,901,i);
            }else{
                w.setImageRegionNotChangeSize(i+300,region);
            }
            w.setImageToFixImage(i+300,i+200,0,0);


            Image starImage=w.getImage(i+400);
            if(starImage==null){
                starImage = new Image(t.getTextureRegion());
                w.addImg(i+400,starImage);
                //generalScreen.function(starImage,901,i);
            }
            starImage.setX(imgX);
            starImage.setY(imgY);
            //故事得分 0~15
            score=game.gameConfig.playerConfig.getInteger("saga_"+age+"_"+i,score);

            if(score==0){
                w.hidImage(i+400);
            }else {
                w.setImageRegion(i+400,game.getImgLists().getTextureByName(DefDAO.getImageNameForStarBar(score)).getNewRegion(0,0, (int) (((score-1)%5+1)*starW/5),-1),1.5f);
            }
            Image medalImage=w.getImage(i+500);
            TextureRegionDAO regionDAO=game.getImgLists().getTextureByName("g_"+game.gameConfig.getDEF_COUNTRY().getElementById(sagaE.getInt("country")).getInt("medal",0));
            if(regionDAO!=null){
                if(medalImage==null){
                    medalImage = new Image(regionDAO.getTextureRegion());
                    w.addImg(i+500,medalImage);
                }
                w.setImageForScale(i+500,regionDAO,imgX-40+imgW, imgY+40,0,-w.getY(),scale);
            }


            Label label=w.getLabel(i+100);
            if(label==null){
                label=new Label("",new Label.LabelStyle(game.gameConfig.gameFont,Color.BLACK));
                label.setFontScale(game.gameConfig.gameFontScale*1.5f);
                label.setAlignment(Align.center);

                label.setWidth(imgW);
                w.addLabel(i+100,label);
            }
            label.setX(imgX);
            label.setY(imgY-40);
            w.setLabelText(i+100,game.gameMethod.getStrValue("saga_title_"+age+"_"+i));


            ImageButton button=w.getButton(i+100);
            if(button==null){
                regionDAO=game.getImgLists().getTextureByName("colorBlock_0");
                button = new ImageButton(new TextureRegionDrawable(regionDAO.getTextureRegion()), new TextureRegionDrawable(regionDAO.getTextureRegion()), new TextureRegionDrawable(regionDAO.getTextureRegion()));

                w.addImgButton(i+100,button);
                generalScreen.function(button,901,i,"switch");
                button.setWidth(bgImg.getWidth());
                button.setHeight(bgImg.getHeight());

            }
            button.setX(bgImg.getX());
            button.setY(bgImg.getY());
            button.setVisible(true);

            imgX=imgX+imgW+borderW;

        }
        w.setVisible(true);

        w.setWidth(imgX);
        w.setHeight(imgH);
        //actor不足则设置group在中间
        if(imgX<w.getStage().getWidth()){
            w.setX((w.getStage().getWidth()-imgX)/2);
            w.setMobileListener(-1,-1);
        }else{
            w.setMobileListener(imgX,-1);
        }
        w.setGroupPotion(0,imgY);



        //w.setDebug(true);
    }

    public void updSagaAgeSelected(WindowGroup w,int age){
        w.hidImage(0);
        w.hidImage(1);
        w.showButton(10);
        w.showButton(11);
        w.showLabel(10);
        w.setLabelText(10,game.gameMethod.getStrValue("age_name_"+age));
    }

    //重置saga的stage组
    //Image i+100 按钮边框  i+200 国旗 1+300 辅助线 i+400 star
    //label i+100 剧本标题
    public boolean updSagaStageGroup(GeneralScreen generalScreen, WindowGroup w, int age, int defaultSelected, float rightBorderX) {
        w.hideAll();
        Array<XmlReader.Element>  stageEs= game.gameConfig.getSagaStageXmlEs(age,defaultSelected);
        if(stageEs==null||stageEs.size==0){
            return false;
        }

        TextureRegionDAO regionDAO=game.getImgLists().getTextureByName(stageEs.get(0).get("sideImage","sideframe_0"));

        w.setX(0);
        w.setY(0);
        float imgH=regionDAO.getH();
        float imgW=regionDAO.getW();
        float borderH= 10;
        float imgY=w.getStage().getHeight()- borderH-imgH;
        regionDAO=game.getImgLists().getTextureByName("card_lv2");
        float starW=regionDAO.getW();
        rightBorderX=rightBorderX-10;
        // w.setY(imgY-(imgH+borderH)*stageEs.size);
        for(int i=0;i<stageEs.size;i++){
            XmlReader.Element stageE=stageEs.get(i);
            if(!game.gameConfig.ifCanUseInLanguage(stageE.get("useLanguage","-1"))){
                continue;
            }
            Image borderImage=w.getImage(i+100);
            regionDAO=game.getImgLists().getTextureByName(stageEs.get(i).get("sideImage","sideframe_0"));
            if(borderImage==null){
                borderImage = new Image(new TextureRegionDrawable(regionDAO.getTextureRegion()));

                w.addImg(i+100,borderImage);
            }else {
                borderImage.setVisible(true);
                w.setImageRegion(i+100,regionDAO);
            }
            borderImage.setX(rightBorderX);
            borderImage.setY(imgY-w.getY());
            // borderImage.setDebug(true);
            Image flagImage=w.getImage(i+200);
            regionDAO=game.getImgLists().getTextureByName("flag_"+stageEs.get(i).getInt("country",0));
            if(flagImage==null){
                flagImage = new Image(new TextureRegionDrawable(regionDAO.getTextureRegion()));

                w.addImg(i+200,flagImage);
            }else {
                flagImage.setVisible(true);
                w.setImageRegion(i+200,regionDAO);
            }
            flagImage.setX(rightBorderX+10);
            flagImage.setY(borderImage.getY()+borderImage.getHeight()/2-flagImage.getHeight()/2);

            Image lineImage=w.getImage(i+300);
            regionDAO=game.getImgLists().getTextureByName("icon_lvpentagram_0");
            if(lineImage==null){
                lineImage = new Image(new TextureRegionDrawable(regionDAO.getTextureRegion()));

                w.addImg(i+300,lineImage);
            }
            lineImage.setX(rightBorderX+70);
            lineImage.setY(borderImage.getY()+5);
            lineImage.setVisible(true);

            //TODO
            int  score=game.gameConfig.playerConfig.getInteger(stageE.getInt("id",0)+"_0_score",0);
            Image starImage=w.getImage(i+400);
            regionDAO=game.getImgLists().getTextureByName("card_lv2");
            if(starImage==null){
                starImage = new Image(new TextureRegionDrawable(regionDAO.getTextureRegion()));

                w.addImg(i+400,starImage);
            }

            if(score==0){
                w.hidImage(i+400);
            }else {
                w.setImageRegion(i+400,game.getImgLists().getTextureByName("card_lv2").getNewRegion(0,0, (int) (starW*score/5),-1),1f);
            }
            starImage.setX(rightBorderX+100);
            starImage.setY(borderImage.getY()+10);

            Label label=w.getLabel(i+100);
            if(label==null){
                label=new Label("",new Label.LabelStyle(game.gameConfig.gameFont,Color.BLACK));
                label.setFontScale(game.gameConfig.gameFontScale*0.8f);
                label.setAlignment(Align.center);

                label.setWidth(lineImage.getWidth());
                w.addLabel(i+100,label);
            }
            w.setLabelText(i+100,game.gameMethod.getStrValue("stage_name_"+stageE.getInt("id")));
            label.setX(lineImage.getX());
            label.setY(borderImage.getY()+45);


            ImageButton button=w.getButton(i+100);
            if(button==null){
                regionDAO=game.getImgLists().getTextureByName("colorBlock_0");
                button = new ImageButton(new TextureRegionDrawable(regionDAO.getTextureRegion()), new TextureRegionDrawable(regionDAO.getTextureRegion()), new TextureRegionDrawable(regionDAO.getTextureRegion()));

                w.addImgButton(i+100,button);
                generalScreen.function(button,902,i,ResDefaultConfig.Sound.成功);
                button.setWidth(borderImage.getWidth());
                button.setHeight(borderImage.getHeight());
            }
            button.setX(borderImage.getX());
            button.setY(borderImage.getY());
            button.setVisible(true);
            //button.setDebug(true);


            imgY=imgY-imgH-borderH;
        }



        //  w.setDebug(true);

        //设置group大小位置
        w.setWidth(imgW);
        w.setHeight(w.getStage().getHeight()-imgY+imgH);
        if(w.getHeight()>w.getStage().getHeight()){
            w.setMobileListener(-1,imgY+imgH);
        }else{
            w.setMobileListener(-1,-1);
        }


        w.setGroupPotion(w.getStage().getWidth()-imgW,imgY+imgH);


        return true;
    }

    public void hidSagaGroupView(WindowGroup w){

    }


    //设置saga窗口的世界地图位置
    public void updSagaGroupView(WindowGroup w, XmlReader.Element xmlE, float viewW, float viewH) {
        w.hideAllLabel();
        w.hideAllImage();

        w.showImage(0);
        w.showImage(1);
        //设置背景地图位置
        Image worldImg=w.getImage(0);
        float scale=xmlE.getFloat("scale",2f);
        worldImg.setScale(scale);
        float x=xmlE.getInt("x",0);
        float y=xmlE.getInt("y",0);

        float mapWMin=viewW-worldImg.getWidth()*scale;
        float mapHMin=viewH-worldImg.getHeight()*scale;

        if(x>0){
            x=0;
        }else if(x<mapWMin){//-1520
            x=mapWMin;
        }
        if(y>0){
            y=0;
        }else if(y<mapHMin){
            y=mapHMin;
        }
        worldImg.setPosition(x,y);


        //设置view中的内容
        Array<XmlReader.Element> markEs=xmlE.getChildrenByName("mark");
        if(markEs==null||markEs.size==0){
            return;
        }
        w.createLabels();
        for(int i=0;i<markEs.size;i++){
            XmlReader.Element markE=markEs.get(i);
            int id=markE.getInt("id");
            scale=markE.getFloat("scale");
            String name=markE.get("name");
            x=markE.getInt("x");
            y=markE.getInt("y");


            if(markE.get("type").equals("label")){
                Label label=w.getLabel(id);
                if(label==null){
                    label=new Label("",new Label.LabelStyle(game.defaultFont,Color.BLACK));
                    label.setAlignment(Align.center);
                    w.addLabel(id,label);
                }
                label.setFontScale(scale);
                label.setText(name);
                label.setX(x);
                label.setY(y);
                label.setRotation(markE.getFloat("rotation"));
                label.setVisible(true);
            }else if(markE.get("type").equals("image")){
                Image image=w.getImage(id);
                TextureRegion r=game.getImgLists().getTextureByName(name).getTextureRegion();
                if(r==null){
                    w.hidImage(id);
                }else {
                    r.flip(markE.getBoolean("flipX",false),markE.getBoolean("flipY",false));//翻转
                    if(image==null){
                        image = new Image(r);
                        image.setName(name);
                        w.addImg(id,image);
                    }else{
                        ((TextureRegionDrawable)image.getDrawable()).setRegion(r);
                    }
                    image.setX(x);
                    image.setY(y);
                    image.setScale(scale);
                    image.setRotation(markE.getFloat("rotation"));
                    image.setVisible(true);
                }
            }
        }

    }


    public void hidSagaGroupDiogiue(WindowGroup w) {
        w.hidImage(20);
        w.hidImage(21);
        w.hidImage(22);
        w.hidImage(23);
        w.hidImage(24);
        w.hidLabel(20);
        w.hidLabel(21);
    }
    //dialogue
    public void updSagaGroupDialogue(WindowGroup w, XmlReader.Element xmlE) {
        w.showImage(20);
        w.showImage(23);
        w.showImage(24);

        //国旗背景
        w.setImageRegionNotChangeSize(21,game.getFlagBgTextureRegion(xmlE.getInt("country",0)));
        //将军头像
        w.setImageRegionNotChangeSize(22,game.getGeneralTextureRegion(xmlE.getInt("general",-1)));
        //人物名
        w.setLabelText(20,game.gameMethod.getStrValueT("generalName_"+(xmlE.getInt("general",0))));
        //对话
        w.setScrollLabel(21,game.gameMethod.getStrValueT("stage_content_"+(xmlE.getInt("id",0))));

    }

    //当点击兵种时更新标签位置
    public void updCardUIForLabelPotion(WindowGroup window, int cardType) {
        cardType=10+cardType;
        Button b=window.getButton(10);
        for(int i=11;i<=18;i++){
            if(cardType==i){
                window.getButton(i).setSize(b.getMinWidth()*1.1f,b.getMinHeight()*1.2f);
            }else {
                window.getButton(i).setSize(b.getWidth(),b.getHeight());
            }
        }
    }


    //type 0初始化 1注册
    public void updAccountGroup(WindowGroup w,int type) {

        switch (type){
            case 0:
                w.setLabelText(0,game.gameMethod.getStrValue("title_account_login"));
                break;
            case 1:
                w.setLabelText(1,game.gameMethod.getStrValue("title_account_register"));
                break;
        }

    }

    //编辑内容
    public void updEditGroup(WindowGroup w,Fb2Smap smap, BtlModule bm, int cardNowPage) {
        XmlReader.Element r=game.gameConfig.getBMRuleXmlE(bm.getMode());
        if(r!=null){
            if(r.getChildCount()>50){
                w.showButton(1);
                w.showButton(2);
                w.showTButton(0);
            }else {
                w.hidButton(1);
                w.hidButton(2);
                w.hidTButton(0);
            }
            w.setVisible(true);
            w.setLabelText(0,r.get("name",""));
            for(int i=0;i<50;i++){
                int index=cardNowPage*50;
                if(index+i<r.getChildCount()){
                    XmlReader.Element e=r.getChild(index+i);
                    w.setLabelText(1+i,e.get("name"));
                    int useMode=e.getInt("useMode",-1);
                    TextButton tb= w.getTButton(1+i);
                    tb.setVisible(true);
                    tb.getLabel().setText(bm.getBMValue(index+i+1));
                    if(useMode==-1||useMode==smap.masterData.getPlayerMode()){
                        tb.getLabel().setColor(Color.WHITE);
                    }else{
                        tb.getLabel().setColor(Color.RED);
                    }
                }else {
                    w.hidTButton(1+i);
                    w.hidLabel(1+i);
                }
            }
        }else {
            w.setVisible(false);
        }
    }

    //对政体进行排序
    public void initForChiefGroup(WindowGroup w) {
        //对位置进行调整
        Image image=w.getImage(0);
        float sX=image.getX()-25;//20
        float sW=image.getWidth();//1239
        image=w.getImage(3);
        float x1=image.getX();//182
        float w1=image.getWidth();//240
        image=w.getImage(5);
        float x2=image.getX();//260
        float w2=image.getWidth();//760
        float xs=(sW-w2-w1)/2;//80
        float  refX1=sX+xs;
        float refX2=sX+sW-w2-xs;
        refX1=refX1-x1;
        refX2=refX2-x2;

        //右边地图部分
        w.addImgPotionForX(1,refX2);
        w.addImgPotionForX(2,refX2);
        w.addImgPotionForX(5,refX2);

        //左边说明部分
        w.addImgPotionForX(3,refX1);

        w.addLabelPotionForX(1,refX1);
        w.addLabelPotionForX(2,refX1);
        w.addLabelPotionForX(3,refX1);
        w.addLabelPotionForX(4,refX1);
        w.addLabelPotionForX(5,refX1);
        w.addLabelPotionForX(6,refX1);
        w.addLabelPotionForX(7,refX1);
        w.addLabelPotionForX(8,refX1);
        w.addLabelPotionForX(9,refX1);
        w.addLabelPotionForX(10,refX1);

        w.addTButtonPotionForX(1,refX1);
        w.addTButtonPotionForX(2,refX1);
        w.addTButtonPotionForX(3,refX1);
        w.addTButtonPotionForX(4,refX1);
        w.addTButtonPotionForX(5,refX1);
        w.addTButtonPotionForX(6,refX1);
        w.addTButtonPotionForX(7,refX1);
        w.addTButtonPotionForX(8,refX1);
        w.addTButtonPotionForX(9,refX1);
        w.addTButtonPotionForX(10,refX1);
        w.addTButtonPotionForX(11,refX1);
        w.addTButtonPotionForX(12,refX1);
        w.addTButtonPotionForX(13,refX1);
        w.addTButtonPotionForX(14,refX1);
        w.addTButtonPotionForX(15,refX1);
        w.addTButtonPotionForX(16,refX1);
        w.addTButtonPotionForX(17,refX1);

    /*  //  w.getImage(1).setX(x2);
      //  w.getImage(2).setX(x2);
        w.getImage(3).setX(x1);
        w.getImage(4).setX(x1);
        w.getImage(5).setX(x1);*/
        for(int i=11;i<=17;i++){
            w.setTButtonXAndWToFixImage(i,1);
        }



        w.hidTButton(11);
        w.hidTButton(12);
        w.hidTButton(13);
        w.hidTButton(14);
        w.hidTButton(15);
        w.hidTButton(16);
        w.hidTButton(17);

        w.setTBLabel(18,game.gameMethod.getStrValueT("noun_refresh"));
    }

    public void updChiefGroup(WindowGroup w, Fb2Smap sMapDAO, BtlModule bm, int page) {
        boolean ifChange=false;
        //政策名称
        for(int i=0;i<10;i++){
            int index=i+1;
            int chiefType=(index+page*10);
            //bm是从3开始  chiefData.getChiefValue是从1开始
            int chiefTypeS=bm.getBMValue(chiefType+2);//sMapDAO.chiefData.getChiefValue(chiefType);
            if(chiefType==sMapDAO.chiefData.getMassesDemandType()){
                w.setLabelText(index,game.gameMethod.getStrValue("chiefName_"+chiefType),Color.RED);
            }else{
                w.setLabelText(index,game.gameMethod.getStrValue("chiefName_"+chiefType),Color.BLACK);
            }

            w.setTBLabel(index,game.gameMethod.getStrValue("chiefName_"+chiefType+"_"+chiefTypeS));
            int chiefId=sMapDAO.chiefData.getChiefValue(chiefType);
            if(chiefId==chiefTypeS){
                w.getTButton(index).getLabel().setColor(Color.WHITE);
            }else{
                w.getTButton(index).getLabel().setColor(Color.GREEN);
            }
        }
        if(!ifChange){
            for(int i=0;i<20;i++){
                int index=i+1;
                int chiefType=index;
                //bm是从3开始  chiefData.getChiefValue是从1开始
                int chiefTypeS=bm.getBMValue(chiefType+2);
                int chiefId=sMapDAO.chiefData.getChiefValue(chiefType);
                if(chiefId!=chiefTypeS){
                    ifChange=true;
                    break;
                }
            }
        }



        w.setTBLabel(0,(page+1)+"/2");

        w.hidTButton(11);
        w.hidTButton(12);
        w.hidTButton(13);
        w.hidTButton(14);
        w.hidTButton(15);
        w.hidTButton(16);
        w.hidTButton(17);

        if(ifChange){
            if(sMapDAO.getPlayerLegionData().getTradeCount()>0){
                w.showButton(1);
            }else{
                w.hidButton(1);
            }
            int h=sMapDAO.getChangeHarmony(bm);
            w.setLabelText(0,game.gameMethod.getStrValue("chief_state1",ComUtil.getSymbolNumer(h),h==0?100:ComUtil.limitValue(sMapDAO.getChiefHarmony()+h,0,100),sMapDAO.getPlayerLegionData().getTradeCount()));//123
        }else {
            w.hidButton(1);
            //w.setLabelText(0,game.gameMethod.getChiefStateStr(sMapDAO));
            w.setScrollLabel(0,game.gameMethod.getChiefStateStr(sMapDAO));
        }
    }
    //点击政策查看子选项
    public void showPolityGroupForSelectPolity(WindowGroup w, Fb2Smap sMapDAO, int chiefType,int selectedChiefIndex) {
        w.hidTButton(11);
        w.hidTButton(12);
        w.hidTButton(13);
        w.hidTButton(14);
        w.hidTButton(15);
        w.hidTButton(16);
        w.hidTButton(17);
        Array<XmlReader.Element> pEs=game.gameConfig.getCONFIG_CHIEFBUFF().e.getChildrenByName("chiefBuff");
        int c=11;
        //之前的政策选项
        int chiefId=sMapDAO.chiefData.getChiefValue(chiefType);
        //   int chiefId=sMapDAO.chiefData.getChiefValue(chiefType);
        XmlReader.Element xmlE=game.gameConfig.getChiefBuffXmlE(chiefType,chiefId);
        for(int i=0;i<pEs.size;i++){
            XmlReader.Element pE=pEs.get(i);
            if(c>17){
                break;
            }
            if(pE.getInt("type",0)==chiefType){
                int index=pE.getInt("index");
                //  w.setTBLabel(c,game.gameMethod.getStrValue("chiefName_"+chiefType+"_"+index));
                w.setTBLabel(c,game.gameMethod.getChiefBuffStr(sMapDAO,chiefType,index,chiefId==index));

                //当前被选择的选项 暗绿色
                //改革前的选项调整为
                //可以选择的选项 白色
                //不可选择的选项 红色
                if(index==selectedChiefIndex){
                    w.getTButton(c).getLabel().setColor(Color.GREEN);//
                }else if(chiefId==index){
                    w.getTButton(c).getLabel().setColor(Color.CHARTREUSE);
                }else if(xmlE.get("developId").equals("-1")||ComUtil.ifHaveValueInStr(xmlE.get("developId"),index)){
                    w.getTButton(c).getLabel().setColor(Color.WHITE);
                }else {
                    w.getTButton(c).getLabel().setColor(Color.LIGHT_GRAY);
                }
                //c 应该放最后
                c++;
            }
        }
    }

    //选择子选项后
    public void updChiefGroupForSelectChild(WindowGroup w, Fb2Smap sMapDAO, BtlModule bm, int page, int chiefType, int chiefTypeValue) {
        bm.setBmValue(chiefType+2,chiefTypeValue);
        updChiefGroup(w,sMapDAO,bm,page);
    }

    public void updAchievementGroupForSpiritInfo(WindowGroup w, Fb2Smap btl, int page, int index) {

        int sort=(page-1)*20+index;
        Iterator<IntIntMap.Entry> itS = btl.spiritMap.iterator();
        int i=1;

        if(game.gameNet.isLanding()){
            w.showButton(23);
        }else {
            w.hidButton(23);
        }
        w.showButton(0);
        while (itS.hasNext()) {
            //   int  spiritId = itS.next().key;
            IntIntMap.Entry sE=itS.next();
            int  spiritId =sE.key;
            if(i==sort){
                w.setLabelText(0,game.gameMethod.getStrValue("spiritEffectName_"+spiritId));
                if(btl.getPlayerAmbition()==100){
                    w.setLabelText(1,game.gameMethod.getStrValue("spiritEffectInfo_error"));
                }else {
                    w.setLabelText(1,game.gameMethod.getStrValue("spiritEffectInfo_"+spiritId,sE.value));
                }
                ImageButton b=w.getButton(index);
                Image image= w.getImage(10);
                image.setVisible(true);
                image.setX(b.getX());
                image.setY(b.getY());
                return;
            }
            i++;
        }


    }

    public void updAchievementWindowForGeneral(WindowGroup w, SMapGameStage.SMapPublicRescource rescource, Fb2Smap sMapDAO, Array<XmlReader.Element> xmlEs,int selectListPage,int sumpage,int allSize) {
        for(int i=1;i<=10;i++){
            w.hidImage(i);
        }
        /*for(int i=21;i<=40;i++){
            w.showImage(i);
        }*/
        for(int i=1;i<=20;i++){
            if(i-1<xmlEs.size){
                w.showImage(i+20);
                //  w.setButtonImageNotChangeSize(i,game.getGeneralTextureRegion(xmlEs.get(i-1).getInt("id",0)));
                w.setButtonImageToFitImageSize(i,i+20,game.getGeneralTextureRegion(xmlEs.get(i-1).getInt("id",0)),0.84f,0.84f);
            }else {
                w.hidImage(i+20);
                w.hidButton(i);
            }
        }
        if(selectListPage<sumpage){
            w.showButton(21);
            w.showButton(22);
        }else {
            w.hidButton(21);
            w.hidButton(22);
        }
        w.hidButton(0);
        w.hidButton(23);

        //将领数量{0}/{1},可备选历史将领{2}
        w.setLabelText(0,game.gameMethod.getStrValue("prompt_admiral_title" ));
        w.setLabelText(1,game.gameMethod.getStrValue("prompt_admiral_info",sMapDAO.getPlayerLegionData().varGeneralNum,sMapDAO.getPlayerLegionData().getGeneralCardNum(),allSize ));

    }

    public void updAchievementGroupForGeneralInfo(WindowGroup w, Fb2Smap btl, Array<XmlReader.Element> xmlEs, int index) {

        int i=1;
        XmlReader.Element gE=xmlEs.get(index-1);
        Button b=w.getButton(index);
        //选中将军
        Image image= w.getImage(10);
        image.setVisible(true);
        image.setX(b.getX());
        image.setY(b.getY());;

        w.setLabelText(0,game.gameMethod.getStrValue("generalName_"+gE.getInt("id",0)));//将领名称
        //{0}指挥官({1}~{2}),历史军衔{3},政治能力{4},战斗能力{5}
        w.setLabelText(1,game.gameMethod.getStrValue("prompt_general_info",
                game.gameMethod.getStrValue("prompt_generalType_"+gE.getInt("type",0),0),
                gE.get("service","1800"),
                gE.get("death","2020"),
                game.gameMethod.getStrValue("rankName_"+gE.getInt("rank",0),0),
                gE.getInt("political",0),
                gE.getInt("ability",0)
        ));
        w.showButton(23);
    }



    //更新阵型界面
    public  void updUnityArmyWindow(SMapGameStage.SMapPublicRescource res, WindowGroup w, Fb2Smap.ArmyData army, int showType){
        Fb2Smap.LegionData l=army.getLegionData();
        Fb2Smap.GeneralData g=army.getGeneralData();
        int unitGroup=army.getUnitGroup();
        //启用图标

        for(int i=24;i<=30;i++){
            w.showImage(i);
        }
        for(int i=41;i<=45;i++){
            w.showImage(i);
        }
        //共用处理
        for(int i=0;i<7;i++){
            int armyId=army.getUnitGroupArmyId(i);
            int armyType=army.getUnitGroupArmyType(i);
            if(armyId==0){
                w.setImageRegion(10+i,game.getImgLists().getBlankRegionDAO("armyBottom2"));
                w.hidImage(17+i);
                w.hidTButton(1+i);
                w.hidTButton(8+i);
                w.hidButton(15+i);
            }else {
                w.setImageRegion(10+i,game.getImgLists().getTextureByName("armyBottom2"));
                w.setImageByTargetImage(17+i,10+i,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getUnitGroupArmyId(i))),0,0);
                //重新地位解散按钮位置
                w.setTButtonPotionToFixTButton(8+i,1+i,74,0);
                if(army.isPlayer()){//升级武器
                    if(army.legionCanUpdUnitGroupWealLv(i)){
                        w.setTBLabel(1+i,ComUtil.min((army.getUnitGroupWealLv(i)+army.getUnitGroupSameArmyIdCount(i)),l.getCardTechLv(armyId,armyType))+"",Color.GREEN);
                    }else {
                        w.setTBLabel(1+i,ComUtil.min((army.getUnitGroupWealLv(i)+army.getUnitGroupSameArmyIdCount(i)),l.getCardTechLv(armyId,armyType))+"",Color.WHITE);
                    }
                    w.setTBLabel(8+i,army.getUnitGroupGroupLv(i)+"",Color.WHITE);//解散编队
                 //   w.setTButtonPotionToFixTButton(8+i,1+i,74,0);
                    if(army.legionCanUpdUnitGroupGroupLv(i)){
                        w.showButton(15+i);
                    }else {
                        w.hidButton(15+i);
                    }
                }else {
                    w.setTBLabel(1+i,ComUtil.min((army.getUnitGroupWealLv(i)+army.getUnitGroupSameArmyIdCount(i)),l.getCardTechLv(armyId,armyType))+"",Color.WHITE);//升级武器
                    w.setTBLabel(8+i,army.getUnitGroupGroupLv(i)+"",Color.WHITE);//解散编队
                    w.hidButton(15+i);
                }

            }
        }



        //image
        //卡牌兵种
        w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,army.getUnitArmyId0(),army.getArmyType())  ).getTextureRegion());
        //兵种标志_等级
        if(army.getArmyRank()==0){
            w.hidImage(2);
        }else  if(army.getArmyRank()<=6){
            w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(army.getArmyRank())).getTextureRegion());
        }else {
            w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(6)).getTextureRegion());
        }
        //将军国家
        if(army.isPlayer()&&army.getArmyType()!=6&&army.canRecruitGeneral()&&army.getGeneralIndex()==0){
            w.setLabelColor(0,Color.GREEN);
        }else{
            w.setLabelColor(0,Color.WHITE);
        }
        w.setImageRegionNotChangeSize(3,game.getFlagBgTextureRegion(l.getCountryId()));

        //士气
        String morale=DefDAO.getImageNameForArmyMorale(game,army.getArmyMorale());
        TextureRegionDAO moraleRD=game.getImgLists().getTextureByName(morale);
        w.setImageRegion(4,moraleRD.getNewRegion(0,0,-1, (int) (moraleRD.getH()*army.getMoraleRate()/2)));

        //将军
        w.setImageRegionNotChangeSize(5,game.sMapScreen.getGeneralTextureRegion(g));
        if(army.getArmyRank()==0){
            w.hidImage(6);
        }else if(army.getArmyRank()>15){
            w.setImageRegion(6,game.getImgLists().getTextureByName("rank_15"));
        }else {
            w.setImageRegion(6,game.getImgLists().getTextureByName("rank_"+army.getArmyRank()));
        }
        w.setImageByTargetImage(7,5,game.getImgLists().getTextureByName("g_"+l.getMedal()),77,-56);

        if(army.getTransportType()==0){
            w.hidImage(8);
          //  w.hidImage(9);
            w.hidButton(23);
        }else{
            w.showImage(8);
            w.setButtonImageByTargetImage(23,8,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getTransportType()+1400)),0,0);
        }


        w.setImageRegion(31,game.getImgLists().getTextureByName(DefDAO.getImageNameArmyFeatureByArmy(army)).getTextureRegion());

        //更新编队
        int uc=army.getUnitGroupSum();
        if(uc<1){
            w.hidImage(36);
        }else if(uc>9){
            w.setImageRegionForCenter(36,game.getImgLists().getTextureByName("icon_max"));
        }else {
            w.setImageRegionForCenter(36,game.getImgLists().getTextureByName(DefDAO.getNumberIcon(uc)));
        }


        //button
        //单位国家
        w.setButtonImageNotChangeSize(8,game.getImgLists().getTextureByName("flag_"+l.getCountryId()).getTextureRegion());


        //能否加载核弹头
        //判断能否加载核武器
        if(army.isPlayer()&&army.getArmyRound()==0&&army.haveNulCanLoad()){
            w.getButton(9).setVisible(true);
        }else {
            w.getButton(9).setVisible(false);
        }
        w.setButtonImageNotChangeSize(11,game.getImgLists().getTextureByName("icon_anti-aircraft-gun").getTextureRegion());
        updArmyWindowForFeature(w,army,showType);

        //label
        updArmyFormationLabelInfo(w,army);



        //TB
        if(unitGroup==1||(!army.isPlayer()&&!army.isEditMode(true)) ||army.getArmyType()==6){
            w.hidTButton(0);
        }else {
            if(army.getArmyFormation()==0){
                w.setTBLabelAndFixWidth(0,game.gameMethod.getStrValueT("noun_freeformat"),10);
            }else if(army.getArmyFormation()==1){
                w.setTBLabelAndFixWidth(0,game.gameMethod.getStrValueT("noun_fixedformat"),10);
            }else if(army.getArmyFormation()>20){
                w.setTBLabelAndFixWidth(0,game.gameMethod.getStrValueT("noun_sortformat"+army.getArmyFormation()),10);
            }else {
                if(army.isUnitGroup()){
                    army.setArmyFormation(0);
                    w.setTBLabelAndFixWidth(0,game.gameMethod.getStrValueT("noun_freeformat"),10);
                }else {
                    w.hidTButton(0);
                }
            }
        }
    }

    public void updArmyWindowForFeature(WindowGroup window, Fb2Smap.ArmyData armyData, int showType) {
        if(showType==0){
            String features=armyData.armyXmlE0.get("feature","");
            String[] strs= features.split(",");
            for (int i = 0; i < 4; i++) {
                if (i < strs.length && ComUtil.isNumeric(strs[i])) {
                    int featureId = Integer.parseInt(strs[i]);
                    window.setImageRegionNotChangeSize(32 + i, game.getImgLists().getTextureByName("unitFeature_" + featureId).getTextureRegion());
                    window.showButton(32 + i);
                    //更新技能等级
                    if (armyData.ifHaveFeature(featureId)) {
                        if (GameMethod.ifUnitFeatureCanUpd(featureId)) {
                            window.setImageRegionForCenter(37 + i, game.getImgLists().getTextureByName(DefDAO.getNumberIcon(armyData.getFeatureLv(featureId))));
                        } else {
                            window.setImageRegionForCenter(37 + i, game.getImgLists().getTextureByName("icon_max"));
                        }
                        window.setButtonImageNotChangeSize(32+i,game.getImgLists().getTextureByName("button_skill").getTextureRegion());
                    } else {
                        window.hidImage(37 + i);
                        window.setButtonImageNotChangeSize(32+i,game.getImgLists().getTextureByName("button_skill_shadow").getTextureRegion());
                    }
                } else {
                    window.setButtonImageNotChangeSize(32+i,game.getImgLists().getTextureByName("button_skill").getTextureRegion());
                    window.hidImage(32 + i);//底色 31
                    window.hidImage(37 + i);//等级 36
                    window.hidButton(32 + i);//技能 31
                }
            }
        }else {
            for (int i = 0; i < 4; i++) {
                int skillId=armyData.getSkillIdByIndex(i);
                int skillLv=armyData.getSkillLvByIndex(i);
                window.setButtonImageNotChangeSize(32+i,game.getImgLists().getTextureByName("button_skill").getTextureRegion());
                if (skillId>0) {
                    window.setImageRegionNotChangeSize(32 + i, game.getImgLists().getTextureByName("unitSkill_" + skillId).getTextureRegion());
                    window.showButton(32 + i);
                    //更新技能等级
                    if(skillLv==0){
                        window.hidImage(37 + i);
                    }else  if (game.gameMethod.ifCanUpdSkillLv(skillId,skillLv)) {
                        window.setImageRegionForCenter(37 + i, game.getImgLists().getTextureByName(DefDAO.getNumberIcon(skillLv)));
                    } else {
                        window.setImageRegionForCenter(37 + i, game.getImgLists().getTextureByName("icon_max"));
                    }
                } else {
                    window.hidImage(32 + i);
                    window.hidImage(37 + i);
                    window.hidButton(32 + i);
                }
            }
        }
    }

    //初始化设定拖动按钮
    public void initArmyGroup(final WindowGroup w) {

        for(int i=17;i<=23;i++){
            final Image image=w.getImage(i);
            final int potion1 = i-17;
            image.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if(game.sMapScreen!=null){
                        if(game.sMapScreen.selectListType==1&&(game.sMapScreen.smapGameStage.selectedArmy==null||game.sMapScreen.smapGameStage.selectedArmy.getArmyType()==4||game.sMapScreen.smapGameStage.selectedArmy.getArmyType()==8 )){ return;}//右边栏 -1空 0战术卡 1陆军 2海军 3空军 4特殊武器
                        if(game.sMapScreen.selectListType==2&&(game.sMapScreen.smapGameStage.selectedArmy==null||(game.sMapScreen.smapGameStage.selectedArmy.getArmyType()!=4&&game.sMapScreen.smapGameStage.selectedArmy.getArmyType()!=8)  )){ return;}
                        if(game.sMapScreen.selectListType==3&&game.sMapScreen.smapGameStage.selectedAir==null){ return;}
                        if(game.sMapScreen.smapGameStage.selectedArmy!=null&&game.sMapScreen.smapGameStage.selectedArmy.isUnitGroup()){//如果是组合部队
                            game.sMapScreen.showPromptTextForUnitArmyWeapon(potion1);
                        }else{
                            game.sMapScreen.setPromptText(46+potion1);
                        }
                    }
                }
            });
            image.addListener(new DragListener(){
                public void drag (InputEvent event, float x, float y, int pointer) {
                    if(game.sMapScreen.ifBanOperation){return;}
                    if(game.sMapScreen!=null&&game.sMapScreen.selectListType==1&&game.sMapScreen.smapGameStage.selectedArmy!=null&&game.sMapScreen.smapGameStage.selectedArmy.isUnitGroup()&&((game.sMapScreen.smapGameStage.selectedArmy.isPlayer()&&game.sMapScreen.smapGameStage.selectedArmy.getArmyRound()==0&&(game.sMapScreen.smapGameStage.selectedArmy.getIfMove()==0||game.sMapScreen.smapGameStage.selectedArmy.getIfAttack()==0))||game.sMapScreen.smapGameStage.isEditMode(true))){
                        if(game.sMapScreen.smapGameStage.selectedArmy.getIfMove()==1&&game.sMapScreen.smapGameStage.selectedArmy.getArmyType()==6){
                            return;
                        }
                        image.moveBy(x - image.getWidth() / 2, y - image.getHeight() / 2);
                    }
                }
                public void dragStop (InputEvent event, float x, float y, int pointer) {
                    if(game.sMapScreen.ifBanOperation){return;}
                    if(game.sMapScreen!=null&&game.sMapScreen.selectListType==1&&game.sMapScreen.smapGameStage.selectedArmy!=null&&game.sMapScreen.smapGameStage.selectedArmy.isUnitGroup()&&((game.sMapScreen.smapGameStage.selectedArmy.isPlayer()&&game.sMapScreen.smapGameStage.selectedArmy.getArmyRound()==0&&(game.sMapScreen.smapGameStage.selectedArmy.getIfMove()==0||game.sMapScreen.smapGameStage.selectedArmy.getIfAttack()==0))||game.sMapScreen.smapGameStage.isEditMode(true))){
                        int potion2=-1;
                        Image targetImage=null;
                        for(int j=10;j<=16;j++){
                            targetImage=w.getImage(j);
                            // targetImage.setDebug(true);
                            if(!image.equals(targetImage)){
                                if(ComUtil.IsOverlap(image.getX(),image.getY(),image.getWidth(),image.getHeight(),targetImage.getX(),targetImage.getY(),targetImage.getWidth(),targetImage.getHeight())){
                                    potion2=j-10;
                                    break;
                                }
                            }
                        }
                        Fb2Smap.ArmyData army=game.sMapScreen.smapGameStage.selectedArmy;
                        if(army!=null&&targetImage!=null&&potion2>=0){
                            Fb2Smap.LegionData l=army.getLegionData();

                            //两个image交换位置
                            if(army.replaceUnitGroupPotion(potion1 ,potion2)){
                                for(int i=0;i<7;i++){
                                    int armyId=army.getUnitGroupArmyId(i);
                                    if(armyId==0){
                                        w.setImageRegion(10+i,game.getImgLists().getBlankRegionDAO("armyBottom2"));
                                        w.hidImage(17+i);
                                        w.hidTButton(1+i);
                                        w.hidTButton(8+i);
                                    }else {
                                        w.setImageRegion(10+i,game.getImgLists().getTextureByName("armyBottom2"));
                                        w.setImageByTargetImage(17+i,10+i,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getUnitGroupArmyId(i))),0,0);
                                     if(army.isPlayer()){//升级武器
                                            if(army.legionCanUpdUnitGroupWealLv(i)){
                                                w.setTBLabel(1+i,ComUtil.min((army.getUnitGroupWealLv(i)+army.getUnitGroupSameArmyIdCount(i)),l.getCardTechLv(armyId,army.getArmyType()))+"",Color.GREEN);
                                            }else {
                                                w.setTBLabel(1+i,ComUtil.min((army.getUnitGroupWealLv(i)+army.getUnitGroupSameArmyIdCount(i)),l.getCardTechLv(armyId,army.getArmyType()))+"",Color.WHITE);
                                            }
                                            w.setTBLabel(8+i,army.getUnitGroupGroupLv(i)+"",Color.WHITE);//解散编队
                                            //   w.setTButtonPotionToFixTButton(8+i,1+i,74,0);
                                            if(army.legionCanUpdUnitGroupGroupLv(i)){
                                                w.showButton(15+i);
                                            }else {
                                                w.hidButton(15+i);
                                            }
                                        }else {
                                            w.setTBLabel(1+i,ComUtil.min((army.getUnitGroupWealLv(i)+army.getUnitGroupSameArmyIdCount(i)),l.getCardTechLv(armyId,army.getArmyType()))+"",Color.WHITE);//升级武器
                                            w.setTBLabel(8+i,army.getUnitGroupGroupLv(i)+"",Color.WHITE);//解散编队
                                            w.hidButton(15+i);
                                        }
                                    }
                                }
                                if(potion2==0||potion1==0){
                                    game.sMapScreen.showArmyFormationWindow();
                                }
                                if(army.getArmyType()==6){
                                    army.setIfMove(1);
                                }
                                game.playSound(ResDefaultConfig.Sound.占领);
                            }else{
                                w.setImageByTargetImage(17+potion1,10+potion1,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getUnitGroupArmyId(potion1))),0,0);
                            }
                        }else {
                            w.setImageByTargetImage(17+potion1,10+potion1,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getUnitGroupArmyId(potion1))),0,0);
                        }
                        if(army.isUnitGroup()){
                            updArmyFormationLabelInfo(w,army);
                        }
                    }

                }
            });
        }
    }

    public void updPromptWindow(WindowGroup w,String id){
        w.setLabelText(0,game.gameMethod.getStrValueT(  "gamePromptTitle_"+id));
        w.setScrollLabel(1,game.gameMethod.getStrValueT(  "gamePromptInfo_"+id));
    }
    public void updPromptWindowForPlayerMode(WindowGroup w, Fb2Smap sMapDAO) {
        w.setLabelText(0,game.gameMethod.getStrValueT(  "gamePromptTitle_2",  "stage_mode_"+sMapDAO.masterData.getPlayerMode()));
        w.setScrollLabel(1,
                game.gameMethod.getStrValueT("gamePromptInfo_2_0",
                       "age_name_"+sMapDAO.getAge(),
                        sMapDAO.masterData.getGameDifficulty(),
                        sMapDAO.masterData.getIfColor()==1?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse,
                        sMapDAO.masterData.getIfFog()==1?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse,
                        (sMapDAO.masterData.getIfChief()==1&&sMapDAO.chiefData==null)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse,
                        sMapDAO.masterData.getMapId(),
                        sMapDAO.masterData.getWidth(),
                        sMapDAO.masterData.getHeight(),
                        sMapDAO.getNowYear(),
                        sMapDAO.masterData.getRoundForYear(),
                        sMapDAO.masterData.getIncomeRate(),
                        sMapDAO.masterData.getAirAddRange(),
                        sMapDAO.masterData.getUnitAddMove(),
                        sMapDAO.masterData.getGameEpisode(),
                        sMapDAO.masterData.getIfCheat()==1?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                )+
                game.gameMethod.getStrValueT("gamePromptInfo_2_1",
                 sMapDAO.ifSystemEffective(0)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(1)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(2)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(3)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(4)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(5)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(6)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(7)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(8)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(9)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(10)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(11)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(12)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(13)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(14)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(15)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(16)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(17)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(18)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(19)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(20)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(21)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(22)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(23)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse
                ,sMapDAO.ifSystemEffective(24)?ResDefaultConfig.StringName.markTrue:ResDefaultConfig.StringName.markFalse));
    }



    public  void updSingleArmyWindow(SMapGameStage.SMapPublicRescource res, WindowGroup w, Fb2Smap.ArmyData army, int showType){
        Fb2Smap.LegionData l=army.getLegionData();
        Fb2Smap.GeneralData g=army.getGeneralData();
        //屏蔽图标
        for(int i=24;i<=30;i++){
            w.hidImage(i);
        }
        for(int i=41;i<=45;i++){
            w.hidImage(i);
        }

        //共用处理

        w.hidImage(11);
        w.hidImage(12);
        w.hidImage(13);
        w.hidImage(14);
        w.hidImage(15);
        w.hidImage(16);

        w.setImageRegion(10,game.getImgLists().getTextureByName("armyBottom2"));
        w.setImageByTargetImage(17,10,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getUnitGroupArmyId(0))),0,0);


        w.setImageRegionAndToFixImage(18,11,game.getImgLists().getTextureByName("unitSkill_67"),0,0);//火力
        w.setImageRegionAndToFixImage(19,12,game.getImgLists().getTextureByName("unitFeature_19"),0,0);//防空
        w.setImageRegionAndToFixImage(20,13,game.getImgLists().getTextureByName("unitSkill_89"),0,0);//补给
        w.setImageRegionAndToFixImage(21,14,game.getImgLists().getTextureByName("unitSkill_56"),0,0);//防御
        w.setImageRegionAndToFixImage(22,15,game.getImgLists().getTextureByName("unitSkill_113"),0,0);//引擎
        w.setImageRegionAndToFixImage(23,16,game.getImgLists().getTextureByName("unitSkill_20"),0,0);//行动
        //武器等级
        if(army.isPlayer()&&army.legionCanUpdUnitGroupWealLv(0)){
            w.setTBLabel(1,army.getUnitGroupWealLv(0)+"",Color.GREEN);
        }else{
            w.setTBLabel(1,army.getUnitGroupWealLv(0)+"",Color.WHITE);
        }
        w.hidTButton(2);
        w.hidTButton(3);
        w.hidTButton(4);
        w.hidTButton(5);
        w.hidTButton(6);
        w.hidTButton(7);
        //编队
        w.setTBLabel(8,army.getUnitGroupGroupLv(0)+"",Color.WHITE);

        //火力
        if(!army.isPlayer()||army.getUnitWealv1()>army.getUnitWealv0Value()||!army.isEmptyBuildRound()){
            w.setTBLabel(9,army.getUnitWealv1()+"",Color.WHITE);
        }else {
            w.setTBLabel(9,army.getUnitWealv1()+"",Color.GREEN);
        }
        w.setTButtonPotionToFixTButton(9,2,37,-22);
        //防空
        if(!army.isPlayer()||army.getUnitWealv2()>army.getUnitWealv0Value()||!army.isEmptyBuildRound()){
            w.setTBLabel(10,army.getUnitWealv2()+"",Color.WHITE);
        }else {
            w.setTBLabel(10,army.getUnitWealv2()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(10,3,37,-22);
        //补给
        if(!army.isPlayer()||army.getUnitWealv3()>army.getUnitWealv0Value()||!army.isEmptyBuildRound()){
            w.setTBLabel(11,army.getUnitWealv3()+"",Color.WHITE);
        }else {
            w.setTBLabel(11,army.getUnitWealv3()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(11,4,37,-22);
        //防御
        if(!army.isPlayer()||army.getUnitWealv4()>army.getUnitWealv0Value()||!army.isEmptyBuildRound()){
            w.setTBLabel(12,army.getUnitWealv4()+"",Color.WHITE);
        }else {
            w.setTBLabel(12,army.getUnitWealv4()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(12,5,37,-22);
        //引擎
        if(!army.isPlayer()||army.getUnitWealv5()>army.getUnitWealv0Value()||!army.isEmptyBuildRound()){
            w.setTBLabel(13,army.getUnitWealv5()+"",Color.WHITE);
        }else {
            w.setTBLabel(13,army.getUnitWealv5()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(13,6,37,-22);
        //行动
        if(!army.isPlayer()||army.getUnitWealv6()>army.getUnitWealv0Value()||!army.isEmptyBuildRound()){
            w.setTBLabel(14,army.getUnitWealv6()+"",Color.WHITE);
        }else {
            w.setTBLabel(14,army.getUnitWealv6()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(14,7,37,-22);


        w.hidButton(15);
        w.hidButton(16);
        w.hidButton(17);
        w.hidButton(18);
        w.hidButton(19);
        w.hidButton(20);
        w.hidButton(21);



        //image
        //卡牌兵种
        w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,army.getUnitArmyId0(),army.getArmyType())  ).getTextureRegion());
        //兵种标志_等级
        if(army.getArmyRank()==0){
            w.hidImage(2);
        }else  if(army.getArmyRank()<=6){
            w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(army.getArmyRank())).getTextureRegion());
        }else {
            w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(6)).getTextureRegion());
        }
        //将军国家
        if(army.isPlayer()&&army.getArmyType()!=6&&army.canRecruitGeneral()&&army.getGeneralIndex()==0){
            w.setLabelColor(0,Color.GREEN);
        }else{
            w.setLabelColor(0,Color.WHITE);
        }
        w.setImageRegionNotChangeSize(3,game.getFlagBgTextureRegion(l.getCountryId()));
        //士气
        String morale=DefDAO.getImageNameForArmyMorale(game,army.getArmyMorale());
        TextureRegionDAO moraleRD=game.getImgLists().getTextureByName(morale);
        w.setImageRegion(4,moraleRD.getNewRegion(0,0,-1, (int) (moraleRD.getH()*army.getMoraleRate()/2)));

        //将军
        w.setImageRegionNotChangeSize(5,game.sMapScreen.getGeneralTextureRegion(g));
        if(army.getArmyRank()==0){
            w.hidImage(6);
        }else if(army.getArmyRank()>15){
            w.setImageRegion(6,game.getImgLists().getTextureByName("rank_15"));
        }else {
            w.setImageRegion(6,game.getImgLists().getTextureByName("rank_"+army.getArmyRank()));
        }
        w.setImageByTargetImage(7,5,game.getImgLists().getTextureByName("g_"+l.getMedal()),77,-56);
        if(army.getTransportType()==0||army.getArmyType()==4||army.getArmyType()==5||army.getArmyType()==6||army.getArmyType()==7||army.getArmyType()==8){
            w.hidImage(8);
            w.hidButton(23);
        }else{
            w.showImage(8);
           // w.setImageByTargetImage(9,8,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getTransportType()+1400)),0,0);
            w.setButtonImageByTargetImage(23,8,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(army.getTransportType()+1400)),0,0);
        }

        w.setImageRegion(31,game.getImgLists().getTextureByName(DefDAO.getImageNameArmyFeatureByArmy(army)).getTextureRegion());

        //更新编队
        int uc=army.getUnitGroupPower();
        if(uc<1){
            w.hidImage(36);
        }else if(uc>9){
            w.setImageRegionForCenter(36,game.getImgLists().getTextureByName("icon_max"));
        }else {
            w.setImageRegionForCenter(36,game.getImgLists().getTextureByName(DefDAO.getNumberIcon(uc)));
        }


        //button
        //单位国家
        w.setButtonImageNotChangeSize(8,game.getImgLists().getTextureByName("flag_"+l.getCountryId()).getTextureRegion());


        //能否加载核弹头
        //判断能否加载核武器
        if(army.isPlayer()&&army.getArmyRound()==0&&army.haveNulCanLoad()){
            w.getButton(9).setVisible(true);
        }else {
            w.getButton(9).setVisible(false);
        }
        updArmyWindowForFeature(w,army,showType);

        //label
        w.setLabelText(0,army.getGeneralName());//将军名
        w.setLabelText(1,army.getArmyHpNow());//生命
        if(army.isUnitGroup()){
            w.setButtonImageNotChangeSize(11,game.getImgLists().getTextureByName("icon_anti-aircraft-gun").getTextureRegion());
            w.setLabelText(2,army.armyAttack*(army.airStrikeBonus)/100);//空中
        }else {
            w.setButtonImageNotChangeSize(11,game.getImgLists().getTextureByName("icon_healing-shield").getTextureRegion());
            w.setLabelText(2,army.getArmorByDirect());//空中
        }

        w.setLabelText(3,army.getMinRange()+"~"+army.getMaxRange());//射程
        w.setLabelText(4,army.getMinAttack()+"~"+army.getMaxAttack());//攻击

        w.setButtonImageNotChangeSize(20,game.getImgLists().getTextureByName("icon_walking-boot").getTextureRegion());
        w.setLabelText(5,army.getMovement());//移动
        w.hidLabel(6);
        w.hidLabel(7);
        w.hidLabel(8);
        w.hidLabel(9);
        w.hidLabel(10);
        w.hidLabel(11);
        w.hidLabel(12);
        w.hidLabel(13);
        w.hidLabel(14);
        w.hidLabel(15);
        w.hidLabel(16);
        w.hidLabel(17);



        //TB
        w.hidTButton(0);
    }


    public  void updAirWindow(SMapGameStage.SMapPublicRescource res, WindowGroup w, Fb2Smap.AirData air, int showType){
        Fb2Smap.LegionData l=air.getLegionData();
        Fb2Smap.GeneralData g=air.getGeneralData();
        //屏蔽图标
        for(int i=24;i<=30;i++){
            w.hidImage(i);
        }
        for(int i=41;i<=45;i++){
            w.hidImage(i);
        }

        //共用处理

        w.hidImage(11);
        w.hidImage(12);
        w.hidImage(13);
        w.hidImage(14);
        w.hidImage(15);
        w.hidImage(16);

        w.setImageRegion(10,game.getImgLists().getTextureByName("armyBottom2"));
        w.setImageByTargetImage(17,10,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(air.getAirId())),0,0);


        w.setImageRegionAndToFixImage(18,11,game.getImgLists().getTextureByName("unitFeature_8"),0,0);//火力
        w.setImageRegionAndToFixImage(19,12,game.getImgLists().getTextureByName("unitSkill_15"),0,0);//侦查
        w.setImageRegionAndToFixImage(20,13,game.getImgLists().getTextureByName("unitSkill_19"),0,0);//补给
        w.setImageRegionAndToFixImage(21,14,game.getImgLists().getTextureByName("unitSkill_88"),0,0);//防御
        w.setImageRegionAndToFixImage(22,15,game.getImgLists().getTextureByName("unitSkill_16"),0,0);//移动
        w.setImageRegionAndToFixImage(23,16,game.getImgLists().getTextureByName("unitSkill_107"),0,0);//敏捷

        //武器等级
        if((air.isPlayer()||air.isEditMode(true))&&air.isEmptyBuildRound()&&air.canUpdWeaLv()&&air.canPayRes()){
            w.setTBLabel(1,air.getWeaLv()+"",Color.GREEN);
        }else{
            w.setTBLabel(1,air.getWeaLv()+"",Color.WHITE);
        }
        w.hidTButton(2);
        w.hidTButton(3);
        w.hidTButton(4);
        w.hidTButton(5);
        w.hidTButton(6);
        w.hidTButton(7);
        //物资
        if((air.isPlayer()||air.isEditMode(true))&&air.isEmptyBuildRound()&&air.canUpdGoods()&&air.canPayRes()){
            w.setTBLabel(8,air.getAirGoodsNow()+"",Color.GREEN);
        }else{
            w.setTBLabel(8,air.getAirGoodsNow()+"",Color.WHITE);
        }

        //火力
        if(!air.isPlayer()||air.getAckLv()>=air.getWeaLv()||!air.isEmptyBuildRound()){
            w.setTBLabel(9,air.getAckLv()+"",Color.WHITE);
        }else {
            w.setTBLabel(9,air.getAckLv()+"",Color.GREEN);
        }
        w.setTButtonPotionToFixTButton(9,2,37,-22);
        //侦查
        if(!air.isPlayer()||air.getSpyLv()>=air.getWeaLv()||!air.isEmptyBuildRound()){
            w.setTBLabel(10,air.getSpyLv()+"",Color.WHITE);
        }else {
            w.setTBLabel(10,air.getSpyLv()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(10,3,37,-22);
        //补给
        if(!air.isPlayer()||air.getSupLv()>=air.getWeaLv()||!air.isEmptyBuildRound()){
            w.setTBLabel(11,air.getSupLv()+"",Color.WHITE);
        }else {
            w.setTBLabel(11,air.getSupLv()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(11,4,37,-22);
        //防御
        if(!air.isPlayer()||air.getDefLv()>=air.getWeaLv()||!air.isEmptyBuildRound()){
            w.setTBLabel(12,air.getDefLv()+"",Color.WHITE);
        }else {
            w.setTBLabel(12,air.getDefLv()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(12,5,37,-22);
        //引擎
        if(!air.isPlayer()||air.getEngLv()>=air.getWeaLv()||!air.isEmptyBuildRound()){
            w.setTBLabel(13,air.getEngLv()+"",Color.WHITE);
        }else {
            w.setTBLabel(13,air.getEngLv()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(13,6,37,-22);
        //行动
        if(!air.isPlayer()||air.getActLv()>=air.getWeaLv()||!air.isEmptyBuildRound()){
            w.setTBLabel(14,air.getActLv()+"",Color.WHITE);
        }else {
            w.setTBLabel(14,air.getActLv()+"",Color.GREEN);
        }

        w.setTButtonPotionToFixTButton(14,7,37,-22);


        w.hidButton(15);
        w.hidButton(16);
        w.hidButton(17);
        w.hidButton(18);
        w.hidButton(19);
        w.hidButton(20);
        w.hidButton(21);
        w.hidButton(23);


        //image
        //卡牌兵种
        w.setImageRegionNotChangeSize(1,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,air.getAirId(),5)  ).getTextureRegion());
        //兵种标志_等级
        if(air.getAirRank()==0){
            w.hidImage(2);
        }else  if(air.getAirRank()<=6){
            w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(air.getAirRank())).getTextureRegion());
        }else {
            w.setImageRegion(2,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(6)).getTextureRegion());
        }
        //将军国家
        if(air.isPlayer()&&air.canRecruitGeneral()&&air.getGeneralIndex()==0){
            w.setLabelColor(0,Color.GREEN);
        }else{
            w.setLabelColor(0,Color.WHITE);
        }

        w.setImageRegionNotChangeSize(3,game.getFlagBgTextureRegion(l.getCountryId()));
        //士气
        String morale=DefDAO.getImageNameForArmyMorale(game,air.getAirMorale());
        TextureRegionDAO moraleRD=game.getImgLists().getTextureByName(morale);
        w.setImageRegion(4,moraleRD.getNewRegion(0,0,-1, (int) (moraleRD.getH()*air.getAirMoraleRate()/2)));

        //将军
        w.setImageRegionNotChangeSize(5,game.sMapScreen.getGeneralTextureRegion(g));
        if(air.getAirRank()==0){
            w.hidImage(6);
        }else if(air.getAirRank()>15){
            w.setImageRegion(6,game.getImgLists().getTextureByName("rank_15"));
        }else {
            w.setImageRegion(6,game.getImgLists().getTextureByName("rank_"+air.getAirRank()));
        }
        w.setImageByTargetImage(7,5,game.getImgLists().getTextureByName("g_"+l.getMedal()),77,-56);

        if(air.armyData==null){
            w.hidImage(8);
            w.hidImage(9);
        }else{
            w.showImage(8);
            w.setImageByTargetImage(9,8,game.getImgLists().getTextureByName(DefDAO.getArmyModeNameByArmyId(air.armyData.getUnitGroupArmyId(0))),0,0);
        }


        w.setImageRegion(31,game.getImgLists().getTextureByName("feature_air").getTextureRegion());

        //更新物资
        int uc=air.getAirRank();
        if(uc<1){
            w.hidImage(36);
        }else if(uc>9){
            w.setImageRegionForCenter(36,game.getImgLists().getTextureByName("icon_max"));
        }else {
            w.setImageRegionForCenter(36,game.getImgLists().getTextureByName(DefDAO.getNumberIcon(uc)));
        }


        //button
        //单位国家
        w.setButtonImageNotChangeSize(8,game.getImgLists().getTextureByName("flag_"+l.getCountryId()).getTextureRegion());


        //能否加载核弹头
        //判断能否加载核武器
        if(air.isPlayer()&&air.getAirRound()==0&&air.haveNulCanLoad()){
            w.getButton(9).setVisible(true);
        }else {
            w.getButton(9).setVisible(false);
        }
        updAirWindowForFeature(w,air,showType);

        //label
        w.setLabelText(0,air.getGeneralName());//将军名
        w.setLabelText(1,air.getAirHpNow());//生命
        w.setButtonImageNotChangeSize(11,game.getImgLists().getTextureByName("icon_healing-shield").getTextureRegion());
        w.setLabelText(2,air.getArmor());//空中

        w.setLabelText(3,air.getMinRange()+"~"+air.getMaxRange());//射程
        w.setLabelText(4,air.getMinAttack()+"~"+air.getMaxAttack());//攻击
        w.setButtonImageNotChangeSize(14,game.getImgLists().getTextureByName("unitSkill_82").getTextureRegion());
        w.setLabelText(5,air.getAirGoodsNow()+"/"+air.getAirGoodsMax());
        w.hidLabel(6);
        w.hidLabel(7);
        w.hidLabel(8);
        w.hidLabel(9);
        w.hidLabel(10);
        w.hidLabel(11);
        w.hidLabel(12);
        w.hidLabel(13);
        w.hidLabel(14);
        w.hidLabel(15);
        w.hidLabel(16);
        w.hidLabel(17);

        //TB
        w.hidTButton(0);
    }


    public void updAirWindowForFeature(WindowGroup window, Fb2Smap.AirData airData, int showType) {
        if(showType==0){
            String features=airData.airXmlE.get("feature","");
            String[] strs= features.split(",");
            for (int i = 0; i < 4; i++) {
                if (i < strs.length && ComUtil.isNumeric(strs[i])) {
                    int featureId = Integer.parseInt(strs[i]);
                    window.setImageRegionNotChangeSize(32 + i, game.getImgLists().getTextureByName("unitFeature_" + featureId).getTextureRegion());
                    window.showButton(32 + i);
                    //更新技能等级
                    if (airData.ifHaveAirFeature(featureId)) {
                        if (GameMethod.ifUnitFeatureCanUpd(featureId)) {
                            window.setImageRegionForCenter(37 + i, game.getImgLists().getTextureByName(DefDAO.getNumberIcon(airData.getAirFeatureLv(featureId))));
                        } else {
                            window.setImageRegionForCenter(37 + i, game.getImgLists().getTextureByName("icon_max"));
                        }
                        window.setButtonImageNotChangeSize(32+i,game.getImgLists().getTextureByName("button_skill").getTextureRegion());
                    } else {
                        window.setButtonImageNotChangeSize(32+i,game.getImgLists().getTextureByName("button_skill_shadow").getTextureRegion());
                        window.hidImage(37 + i);
                    }
                } else {
                    window.hidImage(32 + i);//底色 31
                    window.hidImage(37 + i);//等级 36
                    window.hidButton(32 + i);//技能 31
                    window.setButtonImageNotChangeSize(32+i,game.getImgLists().getTextureByName("button_skill").getTextureRegion());
                }
            }
        }else {
            for (int i = 0; i < 4; i++) {
                int skillId=airData.getSkillIdByIndex(i);
                int skillLv=airData.getSkillLvByIndex(i);
                window.setButtonImageNotChangeSize(32+i,game.getImgLists().getTextureByName("button_skill").getTextureRegion());
                if (skillId>0) {
                    window.setImageRegionNotChangeSize(32 + i, game.getImgLists().getTextureByName("unitSkill_" + skillId).getTextureRegion());
                    window.showButton(32 + i);
                    //更新技能等级
                    if(skillLv==0){
                        window.hidImage(37 + i);
                    }else  if (game.gameMethod.ifCanUpdSkillLv(skillId,skillLv)) {
                        window.setImageRegionForCenter(37 + i, game.getImgLists().getTextureByName(DefDAO.getNumberIcon(skillLv)));
                    } else {
                        window.setImageRegionForCenter(37 + i, game.getImgLists().getTextureByName("icon_max"));
                    }
                } else {
                    window.hidImage(32 + i);
                    window.hidImage(37 + i);
                    window.hidButton(32 + i);
                }
            }
        }
    }


    private void updArmyFormationLabelInfo(WindowGroup w, Fb2Smap.ArmyData army) {

        w.setLabelText(0,army.getGeneralName());//将军名
        w.setLabelText(1,army.getArmyHpNow());//生命
        w.setLabelText(2,army.armyAttack*(army.airStrikeBonus)/100);//空中
        w.setLabelText(3,army.getMinRange()+"~"+army.getMaxRange());//射程
        w.setLabelText(4,army.getMinAttack()+"~"+army.getMaxAttack());//攻击
        w.setLabelText(5,army.getMovement());//移动
        w.setLabelText(6,army.formationAttack1);//进攻↖
        w.setLabelText(7,army.formationAttack2);//进攻↑
        w.setLabelText(8,army.formationAttack3);//进攻↗
        w.setLabelText(9,army.formationAttack4);//进攻↙
        w.setLabelText(10,army.formationAttack5);//进攻↓
        w.setLabelText(11,army.formationAttack6);//进攻↘
        w.setLabelText(12,army.formationArmor1);//防御↖
        w.setLabelText(13,army.formationArmor2);//防御↑
        w.setLabelText(14,army.formationArmor3);//防御↗
        w.setLabelText(15,army.formationArmor4);//防御↙
        w.setLabelText(16,army.formationArmor5);//防御↓
        w.setLabelText(17,army.formationArmor6);//防御↘

    }

    //根据原生坐标转化为当前坐标
    //原生坐标x为1280
    public float transXBySX(float x) {
        float tw=game.getWorldWidth();
        return tw/1280*x;
    }
    //根据原生坐标转化为当前坐标
    //原生坐标x为1280
    public float transYBySY(float y) {
        //  float th= game.getWorldWidth()*game.getWorldHeight()/1280;
        float th=1280/game.getWorldWidth()*game.getWorldHeight();
        return  game.getWorldHeight()/th*y ;
    }


    public void initDefaultGroupForSlide(final WindowGroup w, final SMapScreen sMapScreen) {
        final Image slide_bg=w.getImage(2050);//滑动条
        final Image slide_knob=w.getImage(2051);//滑动按钮
        final float knobY=slide_knob.getY();
        w.showImage(2050);
        w.showImage(2051);
        slide_knob.addListener(new DragListener(){
            public void drag (InputEvent event, float x, float y, int pointer) {
                if(sMapScreen.ifBanOperation){return;}
                if(slide_knob.getY()>slide_bg.getY()-slide_knob.getHeight()/2&&slide_knob.getY()<slide_bg.getY()+slide_bg.getHeight()-slide_knob.getHeight()/2){
                    slide_knob.moveBy(0,y);
                }
                if(y>0){
                    sMapScreen.smapGameStage.cam.setZoomChange(-0.06f);
                }else if(y<0) {
                    sMapScreen.smapGameStage.cam.setZoomChange(0.06f);
                }
                sMapScreen.ifZoom=true;
            }
            public void dragStop (InputEvent event, float x, float y, int pointer) {
                if(sMapScreen.ifBanOperation){return;}
                slide_knob.setY(knobY);
                sMapScreen.ifZoom=false;
            }
        });

    }

    public void updRegionBorderGroupForLegionUnitInfo(WindowGroup w, SMapGameStage.SMapPublicRescource res, Fb2Smap.ArmyData army) {
        if(army==null){
            return;
        }
        Image image=w.getImage(2001);
        float cardWidth=image.getWidth();
        Fb2Smap.LegionData legion=game.getSMapDAO().getPlayerLegionData();
        Fb2Smap.BuildData build =army.getBuildData();
        w.showImage(2000);
        w.hidButton(2005);
        w.hidButton(2006);

        for(int i=3001;i<=3008;i++){
            w.showButton(i);
        }
        if(army.getArmyType()>=0&&army.getArmyType()<=4){
            w.setButtonImageNotChangeSize(2000,game.getImgLists().getTextureByName("rightborder_button_"+army.getArmyType()).getTextureRegion());
        }else{
            w.setButtonImageNotChangeSize(2000,game.getImgLists().getTextureByName("rightborder_button_n").getTextureRegion());
        }
        for(int i,m,j=1;j<=4;j++){
            i=j-1;
            m=(j-1)*5;
            if(i<1){//显示数据
                //showArmyInfoForArmy(window,armyData.get(i),j);
                if(army.getArmyRound()==0){
                    w.setButtonImageNotChangeSize(2000+j,game.getImgLists().getTextureByName("card_border").getTextureRegion());
                }else{
                    w.setButtonImageNotChangeSize(2000+j,game.getImgLists().getTextureByName("card_ShaderBorder").getTextureRegion());
                }
                //卡牌
                w.setImageRegionNotChangeSize(2000+1+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForCard(game,army.getUnitArmyId0(),army.getArmyType())).getTextureRegion());
                Fb2Smap.GeneralData g=army.getGeneralData();
                //将领
                if(g!=null&&army.getGeneralIndex()!=0){
                    if(g.getState()==0){
                        //w.setImageRegionNotChangeSize(2000+3+m,game.getImgLists().getTextureByName(g.getSmallGeneralImageName()).getTextureRegion());
                        w.setImageRegion(2000+3+m,game.getImgLists().getTextureByName(g.getSmallGeneralImageName()).getTextureRegion(),1.5f);
                        w.setImageRegion(2000+28+j,game.getImgLists().getTextureByName(DefDAO.getImageNameForSmallGeneralBg(g.getCamp())).getTextureRegion(),1.5f);
                    }else{
                        //  w.setImageRegionNotChangeSize(2000+3+m,game.getImgLists().getTextureByName("smallgeneral_0").getTextureRegion());
                        w.setImageRegion(2000+3+m,game.getImgLists().getTextureByName("g_"+legion.getMedal()).getTextureRegion(),1.5f);
                        w.hidImage(2000+28+j);
                    }
                }else if(((build.isPlayer()&&build.getBuildRound()==0)||build.isEditMode(true))&&army.getArmyType()!=6&&army.getArmyType()!=7&&army.getGeneralIndex()==0&&legion.ifCanRecruitGeneral()){//如果可以上将
                    w.setImageRegion(2000+3+m,game.getImgLists().getTextureByName("smallgeneral_0").getTextureRegion(),1.5f);
                    w.hidImage(2000+28+j);
                }else {
                    w.hidImage(2000+3+m);
                    w.hidImage(2000+28+j);
                }
                //血条
                //  w.setImageRegion(2+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForCardHp(army.getArmyGroup())),cardWidth* army.getHpRateF(),0);
                w.setImageRegionWidth(2000+2+m,cardWidth* army.getHpRateF(),1f);
                //等级
                if(army.getArmyRank()>0){
                    w.setImageRegion(2000+4+m,game.getImgLists().getTextureByName(DefDAO.getImageNameForArmyRank(army.getArmyRank())).getTextureRegion(),1.5f);
                }else {
                    w.hidImage(2000+4+m);
                }
                // morale=DefDAO.getImageNameForArmyMorale(army.getArmyMorale());
                /*//兵种士气 0正常 1上升 2降低 3大降 4混乱
                if(morale!=null){
                    w.setImageRegion(5+m,game.getImgLists().getTextureByName(morale));
                }else {
                    w.hidImage(5+m);
                }*/
                if(army.getNucleIndex()!=-1){
                    w.setImageRegionNotChangeSize(2000+5+m,game.getImgLists().getTextureByName("button_loadnuclear").getTextureRegion());
                }else if(army.getAirCount()>0){
                    w.setImageRegionNotChangeSize(2000+5+m,game.getImgLists().getTextureByName("button_loadair").getTextureRegion());
                }else {
                    w.hidImage(2000+5+m);
                }
                //兵种 显示的是士气
                w.setImageRegionWidth(2000+20+j,game.getImgLists().getTextureByName("card_bar").getTextureRegion(),cardWidth* army.getArmyMorale()/100,1f);
                // int uc=army.getUnitGroup();
                //兵种编队
                w.hidImage(2000+24+j);
                /*if(uc>5){
                    w.setImageRegion(2000+24+j,game.getImgLists().getTextureByName("card_groupborder").getTextureRegion(),1.2f);
                }else {
                    w.setImageRegion(2000+24+j,game.getImgLists().getTextureByName("card_groupborder").getNewRegion(0,0,-1, (int) (cardHeight/5*uc)),1.2f     );
                }*/

                w.setLabelText(3001,army.getArmyHpNow());
                w.setLabelText(3002,(army.getMinAttack()+army.getMaxAttack())/2);
                w.setLabelText(3003,army.getArmorByDirect());
                w.setLabelText(3004,army.getWealMaxLv());
                w.setImageRegionHeight(2032+j,image.getHeight()* army.getExpRateF(),1f);
                w.showButton(2000+j);
            }else {//隐藏数据
                w.hidImage(2000+1+m);
                w.hidImage(2000+2+m);
                w.hidImage(2000+3+m);
                w.hidImage(2000+4+m);
                w.hidImage(2000+5+m);
                w.hidImage(2000+20+j);
                w.hidImage(2000+24+j);
                w.hidImage(2000+28+j);
                w.hidButton(2000+j);
                w.hidImage(2032+j);
            }
            i++;
        }


    }
    //第二个设置模式的显示
    public void updOption2Window(WindowGroup w) {
        w.getButton(1).getImage().setVisible(game.gameConfig.ifLeisureMode);
        w.getButton(2).getImage().setVisible(game.gameConfig.ifShield);
        w.getButton(3).getImage().setVisible(game.gameConfig.ifColor);
        w.getButton(4).getImage().setVisible(game.gameConfig.getVSync());;
        w.getButton(5).getImage().setVisible(game.gameConfig.ifIgnoreBug);
        //int jc=  Gdx.graphics.getBufferFormat().samples;
    }

    public void setDefaultGroupUIColor(WindowGroup w,Color color) {
        Color c=new Color(color.r,color.g,color.b,1);
        w.setImageColor(0,c);
        w.setImageColor(4,c);
        w.setImageColor(2050,c);
        w.setImageColor(2051,c);
        w.setButtonColor(0,c);
        w.setButtonColor(1,c);
        w.setButtonColor(2,c);
        w.setButtonColor(3,c);
        w.setButtonColor(4,c);
        w.setButtonColor(5,c);
        w.setButtonColor(6,c);
        w.setButtonColor(7,c);
        w.setButtonColor(8,c);
        w.setButtonColor(9,c);
        w.setButtonColor(11,c);
        w.setButtonColor(12,c);
        w.setButtonColor(13,c);
        w.setButtonColor(15,c);
        w.setButtonColor(16,c);
        w.setButtonColor(17,c);
        w.setButtonColor(18,c);
        w.setButtonColor(21,c);
        w.setButtonColor(22,c);
        w.setButtonColor(23,c);
        w.setButtonColor(14,c);
        w.setButtonColor(24,c);
        w.setButtonColor(19,c);


    }
}
