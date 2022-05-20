package com.zhfy.game.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.framework.ComUtil;

import java.util.Properties;

//游戏参数
public class ResGameConfig {

    public int defaultMapBinId ;
    public int freeConquestRoundForYear;
    public int freeConquestDifficulty;
    public int defaultLandBase;
    public boolean editUpdateIsCheat;
    public boolean enableGeneral;
    public int mainBgConquestMaxId;
    public int mainBgEventMaxId;
    public int mainBgEditMaxId;
    public int playerProtectRound;
    public int playerProtectRoundPress;
    public float defaultFloorHexagonAlphaRate;
    //每回合自动随机的外交行为
    public int[] diplomaticBehaviors;
    //群众反对需求 lv= 识字率/10
    public int[] massesOpposeDemandLv0;
    public int[] massesOpposeDemandLv1;
    public int[] massesOpposeDemandLv2;
    public int[] massesOpposeDemandLv3;
    public int[] massesOpposeDemandLv4;
    public int[] massesOpposeDemandLv5;
    public int[] massesOpposeDemandLv6;
    public int[] massesOpposeDemandLv7;
    public int[] massesOpposeDemandLv8;
    public int[] massesOpposeDemandLv9;
    public int[] massesOpposeDemandLv10;
    public int[] defaultConquestModeSort;
    public IntArray diplomaticForAllyInLv0CardIds;
    public IntArray diplomaticForAllyInLv1CardIds;
    public IntArray diplomaticForAllyInLv2CardIds;
    public IntArray diplomaticForNeutralInLv0CardIds;
    public IntArray diplomaticForNeutralInLv1CardIds;
    public IntArray diplomaticForNeutralInLv2CardIds;
    public IntArray diplomaticForEnemyInLv0CardIds;
    public IntArray diplomaticForEnemyInLv1CardIds;
    public IntArray diplomaticForEnemyInLv2CardIds;

    public IntArray diplomaticForBegPeaceCardIds;

    public int unityFeatureChanceForLv;//坦克进攻时可以突进的几率
    public int randomRecruitChance;
    public int randomHungerBuildChance;
    public int reduceNulDamageByNulLvRate;
    public int rEventMaxInRound ;
    public int rEventChance ;
    public int rDialogueMaxInRound ;//每回合生成的随机对话次数
    public int cityBaseHp;//城市血量=城市等级*baseHp
    public float addHpEachRank = 0.05f;
    public int playerAllyNoOperateChane = 50;//玩家盟友升级几率
    public int developAi=5;//当建筑>5 只造兵
    public int declareWarChance=50;
    public int unitArmorBaseBeardamage =62;
    public int airArmorBaseBeardamage =50;
    public int unitArmorForairDenfenceBaseBeardamage =50;

    public float moneyIncomeRate=1.0f;
    public float industryIncomeRate=1.0f;
    public float techIncomeRate=1.0f;
    public float foodIncomeRate=2.0f;
    public float mineralIncomeRate=1.0f;
    public float oilIncomeRate=2.0f;

    public float industryIncomeMaxRate=0.5f;
    public float  techIncomeMaxRate=0.3f;
    public float  foodIncomeMaxRate=2.0f;


    public float allDamageRate=1f;
    //每一个附属国 增加3%的税率
    public int addLegionTaxByColonyRate=3;
    public int legionFeatureLvMax=5;
    public int unitAbilityLvMax =6; //所有单位能够升级的上限
    public int addDamageForAckLv =10; //攻击的加成
    public int airDefenseForAdfLv =5; //减少来自空中的{1}%伤害
    public int airReduceDamageChanceForActLv =3;
    public float critDamageRatio =2f;//暴击后的伤害倍数
    public int addDefChanceForRankLvPoor =5; //有5%*(等级差)的几率豁免受到的伤害减半
    public int addDefForDefLv =5;//只收到1/2的伤害
    public int addDefRateForGroupLv =5;
    public int addCritForAirSpyLv =4;
    //public float reductDamageForBuildDLv =0.05f;
    public float unityReplyHpRatio =0.03f;
    public int addRestoreMoraleChanceForRankLv =4; //有4%*n的几率进行士气消耗
    public int extraReplyMoraleRatio= 4; //额外回复士气最高比例
    public int extraReplyAirGoodsRatio= 1; //额外回复物资最高比例,只有飞机
    public float buildSaveInjuryRatio=0.7f;//建筑豁免伤害比例
    public int addTradeChanceForLegionFinancialLv =6;
    public int tradeSuccessRewardRate =2;//贸易成功时奖励
    public int tradeSuccessRewardTax =2;//贸易成功时奖励
    public float buildReplayHpRate = 0.3f;//建筑回血百分比
    public int downGradeChance=5;//无维护费降级概率
    public float militaryCostRate =2f;//军队维护费

    public int baseActChance=30;
    public int allianceEventFavorLimit=60;
    public int aiEliteLvLimit=5;// 等级或编队>=x级的是精英,精英越多,过回合越慢
    public int playerBuildAutoUpdUnitLvLimit=5;
    public int mergeArmyInheritChance=30;//合并高属性继承几率
    public int surrenderChance=30;//投降几率
    public int rebellionChance=30;//稳定度不够城市的叛乱几率
    public int coloanyRevoltChance=10;//殖民地反抗几率 即每回合开始前附属关系自动掉的几率
    public float legionHelpRate=0.2f;
    public float aiIncomeBlanceRate=0.1f;//当玩家攻陷一座城市,对两者收入差值进行补充
    public int resetUnitMoraleChance =30;//士气恢复几率
    public int resetUnitMoraleMax =50;
    public int unitMoraleMinLimit=25;//士气低落限制
    public int unitHpRateMinLimit=30;
    public int unitHpRateMergeLimit=50;//战略模式下ai会在血量低于50%的情况下合并
    public int unitMoraleMaxLimit=75;//士气高昂显示
    public int obtainFortChance=30;
    public int destroyFortChance=30;
    public int destroyRailwayChance=30;
    public int attackAllyReduceFavor=-30;
    public int reduceFavorWhenDiplomacyFail=-20;
    public int neutralLegionDeclaredWarFavor=50;
    public int skillMaxLv=4;
    public int missileLvDefenAirRate=5;

    public int navyModelChangeYear=1885;

    public int aiNotActWhenCityHpRate =10;
    //单位士气变化
    public int unitMoraleChangeValueMax =15;

    public int dropDamageRateEachFortLv=2;
    //飞机侦查到目标时对其的士气影响
    public int airScoutForMoraleChangeMax =20;
    public  int aiWorldHelpRate = 10;//ai世界援助比例
    public int cityStabilityTriggerValue=30; //城市稳定度触发惩罚的阀值
    public int cityStabilityChangeValueMax=60;
    public int addCityStabilityEachRound;

    public int[] aiCanBuildAirId={1502,1503,1504,1506};
    //城市方针 建设顺序
    // public int[] buildPolicy1={2003,2001,2007,2002,2008,2004,2005,2006};
    public int[] buildPolicy2={2007,2001,2002,2003,2008,2004,2005,2006};
    public int[] buildPolicy2_1={2001,2007,2002,2003,2008,2004,2005,2006};
    public int[] buildPolicy2_2={2005,2007,2001,2002,2003,2008,2004,2006};
    public int[] buildPolicy2_3={2003,2007,2001,2002,2008,2004,2005,2006};
    public int[] buildPolicy2_4={2008,2007,2001,2002,2003,2004,2005,2006};
    public int[] buildPolicy2_5={2004,2007,2001,2002,2003,2008,2005,2006};
    public int[] buildPolicy3={2014,2010,2003,2007,2008,2004,2009,2011,2012,2013};
    public int[] buildPolicy4={2011,2008,2004};
    public int[] buildPolicy5={2013,2008,2004};
    public int[] buildPolicy6={2012,2008,2004};

    public int[] buildFortIdForAge0={5001,5002,5003};
    public int[] buildFortIdForAge1={5001,5002,5002,5005,5005,5006};
    public int[] buildFortIdForAge2={5001,5002,5002,5005,5005,5006,5007};

    public int buildFortChance = 30;
    public int wonderAddGameRound=5;//非征服模式下奇观增加的游戏回合


    public int nuclearTeleportChance = 30;
    public int airTeleportChance = 40;


    public int addEnergyEachEnergyLv =10;

    //科技等级升级上限
    public int cardUpdMax_unitLv =10;
    public int cardUpdMax_foodLv=10;
    public int cardUpdMax_cityLv =10;
    public int cardUpdMax_industryLv =10;
    public int cardUpdMax_techLv=10;
    public int cardUpdMax_eneryLv =15;
    public int cardUpdMax_transportLv =10;


    public int cardUpdMax_airportLv =10;
    public int cardUpdMax_supplyLv=10;
    public int cardUpdMax_defenceLv=10;
    public int cardUpdMax_missileLv=10;
    public int cardUpdMax_nuclearLv=10;
    public int cardUpdMax_financialLv =10;
    public int cardUpdMax_tradeLv=10;
    public int cardUpdMax_cultureLv=10;
    public int cardUpdMax_miracle =10;
    public int cardUpdMax_militaryAcademyLv =10;


    public int cardUpdMax_infantryCardLv =10;
    public int cardUpdMax_armorCardLv =10;
    public int cardUpdMax_artilleryCardLv =10;
    public int cardUpdMax_navyCardLv =10;
    public int cardUpdMax_airCardLv =10;
    public int cardUpdMax_nuclearCardLv =10;
    public int cardUpdMax_missileCardLv =10;
    public int cardUpdMax_submarineCardLv =10;
    public int cardUpdMax_defenceCardLv =10;
    public int cardUpdMax_generalCardLv =10;

    //每一级增加的兵种数量
    /*public int cardUpdNum_infantryCard=8;
    public int cardUpdNum_armorCard=4;
    public int cardUpdNum_artilleryCard=6;
    public int cardUpdNum_navyCard=4;


    public int cardUpdNum_submarineCard=4;
   */
    public int cardUpdNum_airCard=4;
    public int cardUpdNum_nuclearCard=4;
    public int cardUpdNum_defenceCard =8;
    public int cardUpdNum_missileCard=4;
    public int cardUpdNum_generalCard=2;

    //强征效果
    public int tactic_ImposeTax_cityStability=10;
    public int tactic_ImposeTax_cityTax=10;
    public int tactic_Inspiring_unityMorale=3;
    public int tactic_MartialLaw_cityStability =4;
    //这个是鼓舞,和散谣的士气参数
    public int cityStabilityChangeRate = 2;
    public int tactic_RepairBuild_hpRate =5;
    public int tactic_NuclearBlasting_damage =50;
    public int tactic_SpreadRumors_chance=5;
    public int tactic_FirstAid_hpRate=7;
    public int tactic_Bombardment_damageRate=5;
    public int tactic_SpyingMessage_chance=7;
    public int tactic_againActForLegion_chance=4;


    public int addAtkEachRank=2;
    public int addDefEachRank=1;
    public int unityRankMax=6;
    public int generalRankMax =15;
    public int recruitGeneralFriendlyAbilityLimit;

    //设置使用卡牌作为触发的决议的每个卡牌等级增加的触发几率
    public int addLegionPolicyChanceEachCardTechLv =5;
    //进攻倍数
    public float weaponEffectRate = 1.0f;

    public boolean ifPassTerrain = true;
    //如果为true,则在靠近
    public boolean ifUnitArrow = true;
    //如果为true,则在靠近
    public boolean ifPromptFirstOperate = true;
    public boolean ifRailwayCostByTransportLv = true;
    //如果是河流或海岸 true 当移动力高于  false必须停下
    public boolean ifPassWaterBeach =true;
    //士气转换为伤害倍数
    public float moraleTransformRate;

    //是否开启地形参数
    public boolean ifTerrainEffect;
    //是否开启合兵
    public boolean ifUnitGroup;


    //非争霸模式每隔xx回合会检测一次玩家实力,如果玩家实力过大,则周边国家会联合反对他
    public int triggerBorderCountryHostilePlayerRound=40;
    public int cityStabilityChangeLimit=5;
    public int buildRobelForStabilityLimit = 30;
    public int buildRobelForLegionStabilityChange=-10;

    public int recruitUnitBaseMorale;
    public float recruitUnitLegionStabilityMoraleRate;
    public float recruitUnitBuildStabilityMoraleRate;
    public float recruitUnitCultureLvMoraleRate;

    public boolean ifCreateSeaWaveEachRound;
    public boolean ifCanRecruitFriendlyCountryGeneral;

    public boolean setLeisureFalse;
    public boolean onlyGeneralWonder;
    public float allIncomeRate;
    public float capitalRegionRate;
    public float coreRegionRate;
    public float majorRegionRate;
    public float sameZoneRegionRate;
    public float defaultRegionRate;
    public float otherRegionRate;
    public float unityGroupHaveAllFormationStrikeAndReduceRate;
    public float unityGroupHaveAllFormationStrikeRate;
    public float unityGroupNotOneFormationStrikeRate;
    public float unityGroupNotAllFormationStrikeRate;
    public float generalUnitFrontStrikeRate;
    public float generalUnitSideStrikeRate;
    public float generalUnitBackStrikeRate;

    public float cardType0PriceRateForLv;
    public float cardType1PriceRateForLv;
    public float cardType2PriceRateForLv;
    public float cardType3PriceRateForLv;
    public float cardType4PriceRateForLv;
    public float cardType5PriceRateForLv;
    public float cardType6PriceRateForLv;
    public float cardType7PriceRateForLv;
    public float cardType8PriceRateForLv;
    public float cardType9PriceRateForLv;
    public float cardType10PriceRateForLv;
    public float cardType11PriceRateForLv;

    //交易上限
    public int tradeResMax;

    public int tradeLvExtraAddMoneyRate;
    public int industryLvExtraAddIndustryRate;
    public int techLvExtraAddTechRate;
    public int cityLvExtraAddFoodRate;


    public int cityBasePopulation;
    public int populationLimitTrigger;
    public int populationLimitMax;
    public int populationLimitMin;



    public XmlReader.Element defaultTechCMDOption_1800;
    public XmlReader.Element defaultTechCMDOption_1840;
    public XmlReader.Element defaultTechCMDOption_1890;
    public XmlReader.Element defaultTechCMDOption_1900;
    public XmlReader.Element defaultTechCMDOption_1930;
    public XmlReader.Element defaultTechCMDOption_1950;
    public int addMineralEachIndustryLv;
    public int addOilEachTechLv;



    //军团模式下每一个铁路等级提供的再次行动的几率为5
    public int legionUnitMoveChanceForTransportLv=5;

    //基础陆军
    public int[] baseArmyIdInAge0={1101,1201,1603};
    public int[] baseArmyIdInAge1={1102,1202,1301,1604};

    private Properties sourceProperties;
    private Properties modProperties;
    private MainGame game;
    private boolean isSource;
    public ResGameConfig(MainGame game,FileHandle fileHandle){//用原版的初始化
        this.game=game;
        sourceProperties = new Properties();
        setSourcePro(fileHandle);
        setProValue(true);
    }

    private void setSourcePro(FileHandle fileHandle) {
        try {
            sourceProperties.load(fileHandle.read());
        } catch (Exception e) {
            //有可能是modid不对导致的
            if(ResDefaultConfig.ifDebug){
                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
            }else if(!game.gameConfig.getIfIgnoreBug()){
                game.remindBugFeedBack();
            }
            game.recordLog("ResGameConfig ",e);
        }
    }
    public void setModPro(FileHandle fileHandle){
        if(modProperties==null){
            modProperties = new Properties();
        }
        try {
            modProperties.load(fileHandle.read());
        } catch (Exception e) {
            //有可能是modid不对导致的
            if(ResDefaultConfig.ifDebug){
                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
            }else if(!game.gameConfig.getIfIgnoreBug()){
                game.remindBugFeedBack();
            }
            game.recordLog("ResGameConfig ",e);
        }
    }

    private String getPropertValue(String value){
        if(isSource){
            return sourceProperties.getProperty(value);
        }else{
            if(modProperties!=null&&modProperties.containsKey(value)){
                return modProperties.getProperty(value);
            }else {
                Gdx.app.error("config.properties not value",value);
                return sourceProperties.getProperty(value);
            }
        }
    }

    //isSource 是否是原版,如果是非原版,这个值缺失,则使用原版的值
    public void setProValue( boolean ifSource) {
        isSource=ifSource;
        defaultMapBinId=   Integer.parseInt(getPropertValue("defaultMapBinId"));
        freeConquestRoundForYear=Integer.parseInt(getPropertValue("freeConquestRoundForYear"));
        freeConquestDifficulty=Integer.parseInt(getPropertValue("freeConquestDifficulty"));
        defaultLandBase=Integer.parseInt(getPropertValue("defaultLandBase"));
        editUpdateIsCheat=Boolean.parseBoolean(getPropertValue("editUpdateIsCheat"));
        enableGeneral=Boolean.parseBoolean(getPropertValue("enableGeneral"));
        mainBgConquestMaxId=Integer.parseInt(getPropertValue("mainBgConquestMaxId"));
        mainBgEventMaxId=Integer.parseInt(getPropertValue("mainBgEventMaxId"));
        mainBgEditMaxId=Integer.parseInt(getPropertValue("mainBgEditMaxId"));
        playerProtectRound=Integer.parseInt(getPropertValue("playerProtectRound"));
        playerProtectRoundPress=Integer.parseInt(getPropertValue("playerProtectRoundPress"));
        defaultFloorHexagonAlphaRate=Float.parseFloat(getPropertValue("defaultFloorHexagonAlphaRate"));
        diplomaticBehaviors= ComUtil.stringTransIntArray(getPropertValue("diplomaticBehaviors"));
        massesOpposeDemandLv0= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv0"));
        massesOpposeDemandLv1= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv1"));
        massesOpposeDemandLv2= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv2"));
        massesOpposeDemandLv3= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv3"));
        massesOpposeDemandLv4= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv4"));
        massesOpposeDemandLv5= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv5"));
        massesOpposeDemandLv6= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv6"));
        massesOpposeDemandLv7= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv7"));
        massesOpposeDemandLv8= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv8"));
        massesOpposeDemandLv9= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv9"));
        massesOpposeDemandLv10= ComUtil.stringTransIntArray(getPropertValue("massesOpposeDemandLv10"));
        defaultConquestModeSort = ComUtil.stringTransIntArray(getPropertValue("defaultConquestModeSort"));
        diplomaticForAllyInLv0CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForAllyInLv0CardIds"));
        diplomaticForAllyInLv1CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForAllyInLv1CardIds"));
        diplomaticForAllyInLv2CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForAllyInLv2CardIds"));
        diplomaticForNeutralInLv0CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForNeutralInLv0CardIds"));
        diplomaticForNeutralInLv1CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForNeutralInLv1CardIds"));
        diplomaticForNeutralInLv2CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForNeutralInLv2CardIds"));
        diplomaticForEnemyInLv0CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForEnemyInLv0CardIds"));
        diplomaticForEnemyInLv1CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForEnemyInLv1CardIds"));
        diplomaticForEnemyInLv2CardIds= ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForEnemyInLv2CardIds"));
        diplomaticForBegPeaceCardIds=ComUtil.stringTransGdxIntArray(getPropertValue("diplomaticForBegPeaceCardIds"));
        aiCanBuildAirId= ComUtil.stringTransIntArray(getPropertValue("aiCanBuildAirId"));
        buildPolicy2= ComUtil.stringTransIntArray(getPropertValue("buildPolicy2"));
        buildPolicy2_1= ComUtil.stringTransIntArray(getPropertValue("buildPolicy2_1"));
        buildPolicy2_2= ComUtil.stringTransIntArray(getPropertValue("buildPolicy2_2"));
        buildPolicy2_3= ComUtil.stringTransIntArray(getPropertValue("buildPolicy2_3"));
        buildPolicy2_4= ComUtil.stringTransIntArray(getPropertValue("buildPolicy2_4"));
        buildPolicy2_5= ComUtil.stringTransIntArray(getPropertValue("buildPolicy2_5"));
        buildPolicy3= ComUtil.stringTransIntArray(getPropertValue("buildPolicy3"));
        buildPolicy4= ComUtil.stringTransIntArray(getPropertValue("buildPolicy4"));
        buildPolicy5= ComUtil.stringTransIntArray(getPropertValue("buildPolicy5"));
        buildPolicy6= ComUtil.stringTransIntArray(getPropertValue("buildPolicy6"));
        buildFortIdForAge0= ComUtil.stringTransIntArray(getPropertValue("buildFortIdForAge0"));
        buildFortIdForAge1= ComUtil.stringTransIntArray(getPropertValue("buildFortIdForAge1"));
        buildFortIdForAge2= ComUtil.stringTransIntArray(getPropertValue("buildFortIdForAge2"));
        buildPolicy3= ComUtil.stringTransIntArray(getPropertValue("buildPolicy3"));
        buildPolicy3= ComUtil.stringTransIntArray(getPropertValue("buildPolicy3"));
        buildPolicy3= ComUtil.stringTransIntArray(getPropertValue("buildPolicy3"));
        buildPolicy3= ComUtil.stringTransIntArray(getPropertValue("buildPolicy3"));
        baseArmyIdInAge0= ComUtil.stringTransIntArray(getPropertValue("baseArmyIdInAge0"));
        baseArmyIdInAge1= ComUtil.stringTransIntArray(getPropertValue("baseArmyIdInAge1"));

        addHpEachRank=Float.parseFloat(getPropertValue("addHpEachRank"));
        moneyIncomeRate=Float.parseFloat(getPropertValue("moneyIncomeRate"));
        industryIncomeRate=Float.parseFloat(getPropertValue("industryIncomeRate"));
        techIncomeMaxRate=Float.parseFloat(getPropertValue("techIncomeMaxRate"));
        foodIncomeMaxRate=Float.parseFloat(getPropertValue("foodIncomeMaxRate"));
        industryIncomeMaxRate=Float.parseFloat(getPropertValue("industryIncomeMaxRate"));
        techIncomeRate=Float.parseFloat(getPropertValue("techIncomeRate"));
        foodIncomeRate=Float.parseFloat(getPropertValue("foodIncomeRate"));
        mineralIncomeRate=Float.parseFloat(getPropertValue("mineralIncomeRate"));
        oilIncomeRate=Float.parseFloat(getPropertValue("oilIncomeRate"));
        //reductDamageForBuildDLv=Float.parseFloat(getPropertValue("reductDamageForBuildDLv"));
        unityReplyHpRatio=Float.parseFloat(getPropertValue("unityReplyHpRatio"));
        buildSaveInjuryRatio=Float.parseFloat(getPropertValue("buildSaveInjuryRatio"));
        buildReplayHpRate=Float.parseFloat(getPropertValue("buildReplayHpRate"));
        militaryCostRate=Float.parseFloat(getPropertValue("militaryCostRate"));
        legionHelpRate=Float.parseFloat(getPropertValue("legionHelpRate"));
        aiIncomeBlanceRate=Float.parseFloat(getPropertValue("aiIncomeBlanceRate"));
        weaponEffectRate=Float.parseFloat(getPropertValue("weaponEffectRate"));
        moraleTransformRate=Float.parseFloat(getPropertValue("moraleTransformRate"));
        allIncomeRate=Float.parseFloat(getPropertValue("allIncomeRate"));

        capitalRegionRate=Float.parseFloat(getPropertValue("capitalRegionRate"));
        coreRegionRate=Float.parseFloat(getPropertValue("coreRegionRate"));
        majorRegionRate=Float.parseFloat(getPropertValue("majorRegionRate"));
        sameZoneRegionRate=Float.parseFloat(getPropertValue("sameZoneRegionRate"));
        defaultRegionRate=Float.parseFloat(getPropertValue("defaultRegionRate"));
        otherRegionRate=Float.parseFloat(getPropertValue("otherRegionRate"));
        unityGroupHaveAllFormationStrikeAndReduceRate=Float.parseFloat(getPropertValue("unityGroupHaveAllFormationStrikeAndReduceRate"));
        unityGroupHaveAllFormationStrikeRate=Float.parseFloat(getPropertValue("unityGroupHaveAllFormationStrikeRate"));
        unityGroupNotOneFormationStrikeRate =Float.parseFloat(getPropertValue("unityGroupNotOneFormationStrikeRate"));
        unityGroupNotAllFormationStrikeRate =Float.parseFloat(getPropertValue("unityGroupNotAllFormationStrikeRate"));
        generalUnitFrontStrikeRate=Float.parseFloat(getPropertValue("generalUnitFrontStrikeRate"));
        generalUnitSideStrikeRate =Float.parseFloat(getPropertValue("generalUnitSideStrikeRate"));
        generalUnitBackStrikeRate =Float.parseFloat(getPropertValue("generalUnitBackStrikeRate"));


        cardType0PriceRateForLv =Float.parseFloat(getPropertValue("cardType0PriceRateForLv"));
        cardType1PriceRateForLv=Float.parseFloat(getPropertValue("cardType1PriceRateForLv"));
        cardType2PriceRateForLv=Float.parseFloat(getPropertValue("cardType2PriceRateForLv"));
        cardType3PriceRateForLv=Float.parseFloat(getPropertValue("cardType3PriceRateForLv"));
        cardType4PriceRateForLv=Float.parseFloat(getPropertValue("cardType4PriceRateForLv"));
        cardType5PriceRateForLv=Float.parseFloat(getPropertValue("cardType5PriceRateForLv"));
        cardType6PriceRateForLv=Float.parseFloat(getPropertValue("cardType6PriceRateForLv"));
        cardType7PriceRateForLv=Float.parseFloat(getPropertValue("cardType7PriceRateForLv"));
        cardType8PriceRateForLv=Float.parseFloat(getPropertValue("cardType8PriceRateForLv"));
        cardType9PriceRateForLv=Float.parseFloat(getPropertValue("cardType9PriceRateForLv"));
        cardType10PriceRateForLv=Float.parseFloat(getPropertValue("cardType10PriceRateForLv"));
        cardType11PriceRateForLv=Float.parseFloat(getPropertValue("cardType11PriceRateForLv"));

        ifPassTerrain=Boolean.parseBoolean(getPropertValue("ifPassTerrain"));
        ifUnitArrow=Boolean.parseBoolean(getPropertValue("ifUnitArrow"));
        ifPromptFirstOperate=Boolean.parseBoolean(getPropertValue("ifPromptFirstOperate"));
        ifRailwayCostByTransportLv=Boolean.parseBoolean(getPropertValue("ifRailwayCostByTransportLv"));
        ifPassWaterBeach=Boolean.parseBoolean(getPropertValue("ifPassWaterBeach"));
        ifTerrainEffect=Boolean.parseBoolean(getPropertValue("ifTerrainEffect"));
        ifUnitGroup =Boolean.parseBoolean(getPropertValue("ifUnitGroup"));
        unityFeatureChanceForLv =Integer.parseInt(getPropertValue("unityFeatureChanceForLv"));
        randomRecruitChance=Integer.parseInt(getPropertValue("randomRecruitChance"));
        randomHungerBuildChance=Integer.parseInt(getPropertValue("randomHungerBuildChance"));
        reduceNulDamageByNulLvRate=Integer.parseInt(getPropertValue("reduceNulDamageByNulLvRate"));
        rEventMaxInRound=Integer.parseInt(getPropertValue("rEventMaxInRound"));
        rEventChance=Integer.parseInt(getPropertValue("rEventChance"));
        rDialogueMaxInRound=Integer.parseInt(getPropertValue("rDialogueMaxInRound"));
        cityBaseHp=Integer.parseInt(getPropertValue("cityBaseHp"));
        playerAllyNoOperateChane =Integer.parseInt(getPropertValue("playerAllyNoOperateChane"));
        developAi=Integer.parseInt(getPropertValue("developAi"));
        declareWarChance=Integer.parseInt(getPropertValue("declareWarChance"));
        unitArmorBaseBeardamage =Integer.parseInt(getPropertValue("unitArmorBaseBeardamage"));
        airArmorBaseBeardamage =Integer.parseInt(getPropertValue("airArmorBaseBeardamage"));
        unitArmorForairDenfenceBaseBeardamage=Integer.parseInt(getPropertValue("unitArmorForairDenfenceBaseBeardamage"));
        allDamageRate=Float.parseFloat(getPropertValue("allDamageRate"));
        addLegionTaxByColonyRate=Integer.parseInt(getPropertValue("addLegionTaxByColonyRate"));
        legionFeatureLvMax=Integer.parseInt(getPropertValue("legionFeatureLvMax"));
        unitAbilityLvMax=Integer.parseInt(getPropertValue("unitAbilityLvMax"));
        addDamageForAckLv =Integer.parseInt(getPropertValue("addDamageForAckLv"));
        airDefenseForAdfLv=Integer.parseInt(getPropertValue("airDefenseForAdfLv"));
        airReduceDamageChanceForActLv=Integer.parseInt(getPropertValue("airReduceDamageChanceForActLv"));
        critDamageRatio =Float.parseFloat(getPropertValue("critDamageRatio"));
        addDefChanceForRankLvPoor =Integer.parseInt(getPropertValue("addDefChanceForRankLvPoor"));
        addDefForDefLv=Integer.parseInt(getPropertValue("addDefForDefLv"));
        addDefRateForGroupLv=Integer.parseInt(getPropertValue("addDefRateForGroupLv"));
        addCritForAirSpyLv=Integer.parseInt(getPropertValue("addCritForAirSpyLv"));
        addRestoreMoraleChanceForRankLv =Integer.parseInt(getPropertValue("addRestoreMoraleChanceForRankLv"));
        extraReplyMoraleRatio=Integer.parseInt(getPropertValue("extraReplyMoraleRatio"));
        extraReplyAirGoodsRatio=Integer.parseInt(getPropertValue("extraReplyAirGoodsRatio"));
        addTradeChanceForLegionFinancialLv =Integer.parseInt(getPropertValue("addTradeChanceForLegionFinancialLv"));
        tradeSuccessRewardRate=Integer.parseInt(getPropertValue("tradeSuccessRewardRate"));
        tradeSuccessRewardTax=Integer.parseInt(getPropertValue("tradeSuccessRewardTax"));
        downGradeChance=Integer.parseInt(getPropertValue("downGradeChance"));
        baseActChance=Integer.parseInt(getPropertValue("baseActChance"));
        allianceEventFavorLimit=Integer.parseInt(getPropertValue("allianceEventFavorLimit"));
        aiEliteLvLimit=Integer.parseInt(getPropertValue("aiEliteLvLimit"));
        playerBuildAutoUpdUnitLvLimit=Integer.parseInt(getPropertValue("playerBuildAutoUpdUnitLvLimit"));
        mergeArmyInheritChance=Integer.parseInt(getPropertValue("mergeArmyInheritChance"));
        surrenderChance=Integer.parseInt(getPropertValue("surrenderChance"));
        rebellionChance=Integer.parseInt(getPropertValue("rebellionChance"));
        coloanyRevoltChance=Integer.parseInt(getPropertValue("coloanyRevoltChance"));
        resetUnitMoraleChance=Integer.parseInt(getPropertValue("resetUnitMoraleChance"));
        resetUnitMoraleMax=Integer.parseInt(getPropertValue("resetUnitMoraleMax"));
        unitMoraleMinLimit=Integer.parseInt(getPropertValue("unitMoraleMinLimit"));
        unitHpRateMinLimit=Integer.parseInt(getPropertValue("unitHpRateMinLimit"));
        unitHpRateMergeLimit=Integer.parseInt(getPropertValue("unitHpRateMergeLimit"));
        unitMoraleMaxLimit=Integer.parseInt(getPropertValue("unitMoraleMaxLimit"));
        obtainFortChance=Integer.parseInt(getPropertValue("obtainFortChance"));
        destroyFortChance=Integer.parseInt(getPropertValue("destroyFortChance"));
        destroyRailwayChance=Integer.parseInt(getPropertValue("destroyRailwayChance"));
        attackAllyReduceFavor=Integer.parseInt(getPropertValue("attackAllyReduceFavor"));
        reduceFavorWhenDiplomacyFail=Integer.parseInt(getPropertValue("reduceFavorWhenDiplomacyFail"));
        neutralLegionDeclaredWarFavor=Integer.parseInt(getPropertValue("neutralLegionDeclaredWarFavor"));
        skillMaxLv=Integer.parseInt(getPropertValue("skillMaxLv"));
        missileLvDefenAirRate=Integer.parseInt(getPropertValue("missileLvDefenAirRate"));
        navyModelChangeYear=Integer.parseInt(getPropertValue("navyModelChangeYear"));
        aiNotActWhenCityHpRate =Integer.parseInt(getPropertValue("aiNotActWhenCityHpRate"));
        unitMoraleChangeValueMax=Integer.parseInt(getPropertValue("unitMoraleChangeValueMax"));
        dropDamageRateEachFortLv=Integer.parseInt(getPropertValue("dropDamageRateEachFortLv"));
        airScoutForMoraleChangeMax=Integer.parseInt(getPropertValue("airScoutForMoraleChangeMax"));
        aiWorldHelpRate=Integer.parseInt(getPropertValue("aiWorldHelpRate"));
        cityStabilityTriggerValue=Integer.parseInt(getPropertValue("cityStabilityTriggerValue"));
        cityStabilityChangeValueMax=Integer.parseInt(getPropertValue("cityStabilityChangeValueMax"));
        addCityStabilityEachRound=Integer.parseInt(getPropertValue("addCityStabilityEachRound"));
        buildFortChance=Integer.parseInt(getPropertValue("buildFortChance"));
        wonderAddGameRound=Integer.parseInt(getPropertValue("wonderAddGameRound"));
        nuclearTeleportChance=Integer.parseInt(getPropertValue("nuclearTeleportChance"));
        airTeleportChance=Integer.parseInt(getPropertValue("airTeleportChance"));


        addEnergyEachEnergyLv=Integer.parseInt(getPropertValue("addEnergyEachEnergyLv"));
        cardUpdMax_unitLv=Integer.parseInt(getPropertValue("cardUpdMax_unitLv"));
        cardUpdMax_foodLv=Integer.parseInt(getPropertValue("cardUpdMax_foodLv"));
        cardUpdMax_cityLv=Integer.parseInt(getPropertValue("cardUpdMax_cityLv"));
        cardUpdMax_industryLv=Integer.parseInt(getPropertValue("cardUpdMax_industryLv"));
        cardUpdMax_techLv=Integer.parseInt(getPropertValue("cardUpdMax_techLv"));
        cardUpdMax_eneryLv=Integer.parseInt(getPropertValue("cardUpdMax_eneryLv"));
        cardUpdMax_transportLv=Integer.parseInt(getPropertValue("cardUpdMax_transportLv"));
        cardUpdMax_airportLv=Integer.parseInt(getPropertValue("cardUpdMax_airportLv"));
        cardUpdMax_supplyLv=Integer.parseInt(getPropertValue("cardUpdMax_supplyLv"));
        cardUpdMax_defenceLv=Integer.parseInt(getPropertValue("cardUpdMax_defenceLv"));
        cardUpdMax_missileLv=Integer.parseInt(getPropertValue("cardUpdMax_missileLv"));
        cardUpdMax_nuclearLv=Integer.parseInt(getPropertValue("cardUpdMax_nuclearLv"));
        cardUpdMax_financialLv=Integer.parseInt(getPropertValue("cardUpdMax_financialLv"));
        cardUpdMax_tradeLv=Integer.parseInt(getPropertValue("cardUpdMax_tradeLv"));
        cardUpdMax_cultureLv=Integer.parseInt(getPropertValue("cardUpdMax_cultureLv"));
        cardUpdMax_miracle=Integer.parseInt(getPropertValue("cardUpdMax_miracle"));
        cardUpdMax_militaryAcademyLv =Integer.parseInt(getPropertValue("cardUpdMax_militaryAcademyLv"));
        cardUpdMax_infantryCardLv=Integer.parseInt(getPropertValue("cardUpdMax_infantryCardLv"));
        cardUpdMax_armorCardLv=Integer.parseInt(getPropertValue("cardUpdMax_armorCardLv"));
        cardUpdMax_artilleryCardLv=Integer.parseInt(getPropertValue("cardUpdMax_artilleryCardLv"));
        cardUpdMax_navyCardLv=Integer.parseInt(getPropertValue("cardUpdMax_navyCardLv"));
        cardUpdMax_airCardLv=Integer.parseInt(getPropertValue("cardUpdMax_airCardLv"));
        cardUpdMax_nuclearCardLv=Integer.parseInt(getPropertValue("cardUpdMax_nuclearCardLv"));
        cardUpdMax_missileCardLv=Integer.parseInt(getPropertValue("cardUpdMax_missileCardLv"));
        cardUpdMax_submarineCardLv=Integer.parseInt(getPropertValue("cardUpdMax_submarineCardLv"));
        cardUpdMax_defenceCardLv=Integer.parseInt(getPropertValue("cardUpdMax_defenceCardLv"));
        cardUpdMax_generalCardLv=Integer.parseInt(getPropertValue("cardUpdMax_generalCardLv"));


      /*  cardUpdNum_infantryCard=Integer.parseInt(getPropertValue("cardUpdNum_infantryCard"));
        cardUpdNum_armorCard=Integer.parseInt(getPropertValue("cardUpdNum_armorCard"));
        cardUpdNum_artilleryCard=Integer.parseInt(getPropertValue("cardUpdNum_artilleryCard"));
        cardUpdNum_navyCard=Integer.parseInt(getPropertValue("cardUpdNum_navyCard"));


        cardUpdNum_submarineCard=Integer.parseInt(getPropertValue("cardUpdNum_submarineCard"));
        */
        cardUpdNum_airCard=Integer.parseInt(getPropertValue("cardUpdNum_airCard"));
        cardUpdNum_nuclearCard=Integer.parseInt(getPropertValue("cardUpdNum_nuclearCard"));
        cardUpdNum_defenceCard=Integer.parseInt(getPropertValue("cardUpdNum_defenceCard"));
        cardUpdNum_missileCard=Integer.parseInt(getPropertValue("cardUpdNum_missileCard"));
        cardUpdNum_generalCard=Integer.parseInt(getPropertValue("cardUpdNum_generalCard"));
        tactic_ImposeTax_cityStability=Integer.parseInt(getPropertValue("tactic_ImposeTax_cityStability"));
        tactic_ImposeTax_cityTax=Integer.parseInt(getPropertValue("tactic_ImposeTax_cityTax"));
        tactic_Inspiring_unityMorale=Integer.parseInt(getPropertValue("tactic_Inspiring_unityMorale"));
        tactic_MartialLaw_cityStability=Integer.parseInt(getPropertValue("tactic_MartialLaw_cityStability"));
        cityStabilityChangeRate=Integer.parseInt(getPropertValue("cityStabilityChangeRate"));
        tactic_RepairBuild_hpRate=Integer.parseInt(getPropertValue("tactic_RepairBuild_hpRate"));
        tactic_NuclearBlasting_damage=Integer.parseInt(getPropertValue("tactic_NuclearBlasting_damage"));
        tactic_SpreadRumors_chance=Integer.parseInt(getPropertValue("tactic_SpreadRumors_chance"));
        tactic_FirstAid_hpRate=Integer.parseInt(getPropertValue("tactic_FirstAid_hpRate"));
        tactic_Bombardment_damageRate=Integer.parseInt(getPropertValue("tactic_Bombardment_damageRate"));
        tactic_SpyingMessage_chance=Integer.parseInt(getPropertValue("tactic_SpyingMessage_chance"));
        tactic_againActForLegion_chance=Integer.parseInt(getPropertValue("tactic_againActForLegion_chance"));
        addAtkEachRank=Integer.parseInt(getPropertValue("addAtkEachRank"));
        addDefEachRank=Integer.parseInt(getPropertValue("addDefEachRank"));
        unityRankMax=Integer.parseInt(getPropertValue("unityRankMax"));
        generalRankMax =Integer.parseInt(getPropertValue("generalRankMax"));
        recruitGeneralFriendlyAbilityLimit=Integer.parseInt(getPropertValue("recruitGeneralFriendlyAbilityLimit"));
        addLegionPolicyChanceEachCardTechLv =Integer.parseInt(getPropertValue("addLegionPolicyChanceEachCardTechLv"));
        tradeResMax=Integer.parseInt(getPropertValue("tradeResMax"));

        triggerBorderCountryHostilePlayerRound=Integer.parseInt(getPropertValue("triggerBorderCountryHostilePlayerRound"));
        cityStabilityChangeLimit=Integer.parseInt(getPropertValue("cityStabilityChangeLimit"));
        buildRobelForStabilityLimit=Integer.parseInt(getPropertValue("buildRobelForStabilityLimit"));
        buildRobelForLegionStabilityChange=Integer.parseInt(getPropertValue("buildRobelForLegionStabilityChange"));
        recruitUnitBaseMorale=Integer.parseInt(getPropertValue("recruitUnitBaseMorale"));
        recruitUnitBuildStabilityMoraleRate =Float.parseFloat(getPropertValue("recruitUnitBuildStabilityMoraleRate"));
        recruitUnitLegionStabilityMoraleRate =Float.parseFloat(getPropertValue("recruitUnitLegionStabilityMoraleRate"));
        recruitUnitCultureLvMoraleRate=Integer.parseInt(getPropertValue("recruitUnitCultureLvMoraleRate"));

        ifCreateSeaWaveEachRound=Boolean.parseBoolean(getPropertValue("ifCreateSeaWaveEachRound"));
        ifCanRecruitFriendlyCountryGeneral=Boolean.parseBoolean(getPropertValue("ifCanRecruitFriendlyCountryGeneral"));
        setLeisureFalse=Boolean.parseBoolean(getPropertValue("setLeisureFalse"));
        onlyGeneralWonder=Boolean.parseBoolean(getPropertValue("onlyGeneralWonder"));
        tradeLvExtraAddMoneyRate=Integer.parseInt(getPropertValue("tradeLvExtraAddMoneyRate"));
        industryLvExtraAddIndustryRate=Integer.parseInt(getPropertValue("industryLvExtraAddIndustryRate"));
        techLvExtraAddTechRate=Integer.parseInt(getPropertValue("techLvExtraAddTechRate"));
        cityLvExtraAddFoodRate=Integer.parseInt(getPropertValue("cityLvExtraAddFoodRate"));

        cityBasePopulation=Integer.parseInt(getPropertValue("cityBasePopulation"));
        populationLimitTrigger =Integer.parseInt(getPropertValue("populationLimitTrigger"));
        populationLimitMax=Integer.parseInt(getPropertValue("populationLimitMax"));
        populationLimitMin=Integer.parseInt(getPropertValue("populationLimitMin"));

        defaultTechCMDOption_1800=game.gameConfig.reader.parse(getPropertValue("defaultTechCMDOption_1800"));
        defaultTechCMDOption_1840=game.gameConfig.reader.parse(getPropertValue("defaultTechCMDOption_1840"));
        defaultTechCMDOption_1890=game.gameConfig.reader.parse(getPropertValue("defaultTechCMDOption_1890"));
        defaultTechCMDOption_1900=game.gameConfig.reader.parse(getPropertValue("defaultTechCMDOption_1900"));
        defaultTechCMDOption_1930=game.gameConfig.reader.parse(getPropertValue("defaultTechCMDOption_1930"));
        defaultTechCMDOption_1950=game.gameConfig.reader.parse(getPropertValue("defaultTechCMDOption_1950"));

        addMineralEachIndustryLv=Integer.parseInt(getPropertValue("addMineralEachIndustryLv"));
        addOilEachTechLv=Integer.parseInt(getPropertValue("addOilEachTechLv"));


        legionUnitMoveChanceForTransportLv=Integer.parseInt(getPropertValue("legionUnitMoveChanceForTransportLv"));

        if(setLeisureFalse){
            game.gameConfig.ifLeisureMode=false;
        }


        game.gameConfig.mainBgConquestId=ComUtil.getRandom(0,mainBgConquestMaxId);
        //pixmap/eventbg 文件夹下有12张图片,记得更新
        game.gameConfig.mainBgEventId =ComUtil.getRandom(0,mainBgEventMaxId);
        game.gameConfig.mainBgEditId =ComUtil.getRandom(0,mainBgEditMaxId);
    }

    //群众反对需求 lv= 识字率/10
    public int[] getMassesOpposeDemands(int literacy) {
        switch (literacy/10){
            case 0: return massesOpposeDemandLv0;
            case 1: return massesOpposeDemandLv1;
            case 2: return massesOpposeDemandLv2;
            case 3: return massesOpposeDemandLv3;
            case 4: return massesOpposeDemandLv4;
            case 5: return massesOpposeDemandLv5;
            case 6: return massesOpposeDemandLv6;
            case 7: return massesOpposeDemandLv7;
            case 8: return massesOpposeDemandLv8;
            case 9: return massesOpposeDemandLv9;
            case 10: return massesOpposeDemandLv10;
        }
        return  massesOpposeDemandLv10;
    }
}
