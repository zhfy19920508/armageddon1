package com.zhfy.game.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.XmlIntDAO;
import com.zhfy.game.model.content.XmlStringDAO;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * 默认的资源常量,一般不改
 *
 * @xietansheng
 */
public interface ResDefaultConfig {



    /** 固定世界宽度为1280, 高度根据实际屏幕比例换算 */
    public static final float FIX_WORLD_WIDTH = 1280;

    //public static final float worldHeight= 720;//576
    public static final boolean supportMod=true;
    public static final  String Task = "我必须考虑这是不是我此生仅有的机会";
    public static final String ImageKey="key";
    public static final String DEFAULT_CHARS = "√✓✔√×○☆←↖↙↑↓↗→↘。？！，、；：“”‘'（）《》〈〉【】『』「」﹃﹄〔〕…—～﹏￥!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~￡€￥¥";
    public static final int projectId = 1;


    //public static final String EmojiKey = "✐✎✏✑✒✍✉✁✂✃✄✆✉☎☏☑✓✔√☐☒✗✘ㄨ✕✖✖☢☠☣✈★☆✡囍㍿☯☰☲☱☴☵☶☳☷☜☞☝✍☚☛☟✌♤♧♡♢♠♣♥♦☀☁☂❄☃♨웃유❖☽☾☪✿♂♀✪✯☭➳卍卐√×■◆●○◐◑✙☺☻❀⚘♔♕♖♗♘♙♚♛♜♝♞♟♧♡♂♀♠♣♥❤☜☞☎☏⊙◎☺☻☼▧▨♨◐◑↔↕▪▒◊◦▣▤▥▦▩◘◈◇♬♪♩♭♪の★☆→あぃ￡Ю〓§♤♥▶¤✲❈✿✲❈➹☀☂☁【】┱┲❣✚✪✣✤✥✦❉❥❦❧❃❂❁❀✄☪☣☢☠☭ღ▶▷◀◁☀☁☂☃☄★☆☇☈⊙☊☋☌☍ⓛⓞⓥⓔ╬『』∴☀♫♬♩♭♪☆∷﹌の★◎▶☺☻►◄▧▨♨◐◑↔↕↘▀▄█▌◦☼♪の☆→♧ぃ￡❤▒▬♦◊◦♠♣▣۰•❤•۰►◄▧▨♨◐◑↔↕▪▫☼♦⊙●○①⊕◎Θ⊙¤㊣★☆♀◆◇◣◢◥▲▼△▽⊿◤◥✐✌✍✡✓✔✕✖♂♀♥♡☜☞☎☏⊙◎☺☻►◄▧▨♨◐◑↔↕♥♡▪▫☼♦▀▄█▌▐░▒▬♦◊◘◙◦☼♠♣▣▤▥▦▩◘◙◈♫♬♪♩♭♪✄☪☣☢☠♯♩♪♫♬♭♮☎☏☪♈ºº₪¤큐«»™♂✿♥　◕‿-｡　｡◕‿◕｡";
    // public static final float FontScale = 1.7f;
    //public static final int FontSize = 20;
    //public static final float FontLineRate=0.72f;

    public static interface Url {
        public static final String defaultUrl ="http://localhost:8080/default";
        public static final String armageddonUrl= "http://localhost:8080/game1";
    }


    public static interface Sound{

        public static final String 单击 ="click";
        public static final String 关闭 ="close";
        public static final String 切换 ="switch";



        public static final String 取消选择 ="close";
        public static final String 选择 ="click";
        public static final String 报错 ="error";
        public static final String 成功 ="suc";
        public static final String 占领 ="occupy";


        public static final String 飞机移动 ="airMove";
        public static final String 装甲移动 ="armorMove";
        public static final String 步兵移动 ="armyMove";
        public static final String 海军移动 ="navyMove";
        public static final String 防空 ="defenseAir";


        public static final String 轻炮 ="bombard";
        public static final String 重炮 ="explode";
        public static final String 毒气 ="gas";
        public static final String 机枪 ="machineGun";
        public static final String 步枪 ="rifle";
        public static final String 手枪 ="pistol";
    }

    /**
     * 纹理图集
     */
    public static interface Path {
        public static final String PlayerInfoFilePath ="sav/playerinfo.bin";
        public static final String SmallMapPath	= "zip/def_smallmap.xml";
        public static final String ZipFolderPath	= "zip/";
        public static final String SaveFolderPath = "sav/";
        public static final String LogFolderPath = "log/";
        public static final String GeneralFolderPath	= "pixmap/general/";
        public static final String RGeneralFolderPath	= "pixmap/rgeneral/";
        public static final String FlagBgFolderPath	= "pixmap/flagbg/";
        public static final String EventBgFolderPath	= "pixmap/eventbg/";
        public static final String PTFolderPath	= "pixmap/pts/";
        public static final String ViewFolderPath	= "view/";
        public static final String MapFolderPath	= "map/";
        public static final String GameConfigPropertiesPath ="config/config.properties";
        public static final String GameOptionPropertiesPath ="sav/option.properties";


        public static final String lastCountryView	= "view/lastCountryView.png";
        public static final String TempPixmapFolderPath	= "pixmap/temp/";
        //世界地图位置
        public static final String WorldMapPath0 ="image/saga_map.tata";
        public static final String WorldMapPath1 ="image/saga_map.jpg";
    }


    //相关参数
    public static interface Map {

        public static final float MARK_ZOOM=1.2f;
        public static final float UNIT_ZOOM=2.0f;//小于这个是微缩地图,否则为大地图

        public static final float MIN_ZOOM = 0.6f;
        public static final float MIN_ZOOM_2 = 1.0f;
        public static final float MAX_ZOOM = 2.5f;
        public static final int PACK_PIC_SIDE = 512;




        //分割区域边长
        public static final int PT_SIDE = 1024;


        public static final int MAPLOOP_REFX=19;

        public static final int HEXAGON_WIDTH = 74;
        public static final int HEXAGON_HEIGHT = 64;
        public static final int HEXAGON_HEIGHT_REF = 32;
        public static final int GRID_WIDTH =56;//为六边形不重复的长
        public static final float MAP_SCALE = 1f;
        public static final int EFFECT_HEXAGON_REFX = 3;
        public static final float UNITMODEL_SCALE = 0.5f;

        //平铺地图里的格子宽,长
        public static final int PT_GRID_WIDTH=(int) (PT_SIDE/(GRID_WIDTH*MAP_SCALE)+1);
        public static final int PT_GRID_HEIGHT=(int) (PT_SIDE/(HEXAGON_HEIGHT*MAP_SCALE)+1);

        //获取range时的顺序
        public static final int[] GRID_RANGE_SORT_ARRAY ={3, 6, 5, 4, 1, 2} ;


        public static final int[] POTION_SORT_DIRECT_1 ={1, 2, 3, 4, 6, 5} ;//↖
        public static final int[] POTION_SORT_DIRECT_2 ={2, 3, 4, 6, 5, 1} ;//↑
        public static final int[] POTION_SORT_DIRECT_3 ={3, 4, 6, 5, 1, 2} ;//↗
        public static final int[] POTION_SORT_DIRECT_4 ={5, 1, 2, 3, 4, 6} ;//↙
        public static final int[] POTION_SORT_DIRECT_5 ={6, 5, 1, 2, 3, 4} ;//↓
        public static final int[] POTION_SORT_DIRECT_6 ={4, 6, 5, 1, 2, 3} ;//↘



        public static final int[] ARMY_POTION_FORMATION_DIRECT_0 ={0} ;
        public static final int[] ARMY_POTION_FORMATION_DIRECT_1 ={1, 2} ;
        public static final int[] ARMY_POTION_FORMATION_DIRECT_2 ={2, 3} ;
        public static final int[] ARMY_POTION_FORMATION_DIRECT_3 ={3, 4} ;
        public static final int[] ARMY_POTION_FORMATION_DIRECT_4 ={1, 5} ;
        public static final int[] ARMY_POTION_FORMATION_DIRECT_5 ={5, 6} ;
        public static final int[] ARMY_POTION_FORMATION_DIRECT_6 ={4, 6} ;

        //世界地图位置
        public static final int WORLD_MAP_X=0;
        public static final int WORLD_MAP_Y=0;
        public static final int WORLD_MAP_W=1024;
        public static final int WORLD_MAP_H=575;
        int VERSION = 3;
    }



    //rule规则
    public static interface Rules {
        public static final String RULE_GG1_BTL = "rule/rule_gg1_btl.xml";
        public static final String RULE_FB1_STAGE = "rule/rule_fb1_stage.xml";
        public static final String RULE_FB1_BTL = "rule/rule_fb1_btl.xml";
        public static final String RULE_FB1_MAP = "rule/rule_fb1_map.xml";
        public static final String RULE_FB1_SMAP = "rule/rule_fb1_smap.xml";
        public static final String RULE_FB2_HISTORY = "rule/rule_fb2_history.xml";
        public static final String RULE_FB2_MAP = "rule/rule_fb2_map.xml";
        public static final String RULE_FB2_BTL = "rule/rule_fb2_smap.xml";
    }
    public static interface Effect{
        public static final float popupCard=0.2F;

        public static final String effect_timeTravel = "timeTravel";
        public static final String effect_gas = "gas";
        public static final String effect_generalStrike = "generalStrike";
        public static final String effec_waterStrike = "waterStrike";
        public static final String effect_nuclearbomb = "nuclearbomb";
        public static final String effect_tankmangun = "tankmangun";
        public static final String effect_lightgun = "lightgun";
        public static final String effect_mgunfire = "mgunfire";

    }

    public static interface Game{
        public static final int ageMax=1;
        //默认是11,从0计数

        public static final int[] screenWidth={ 1920,1680,1600,1600,1440,1360,1280,1280,1280,1024} ;
        public static final int[] screenHeight={1080,1050,1024, 900, 900, 768,1024, 720, 960, 768} ;

        public static final int defaultScreenIndex=6;

    }



    //一些重要的图片信息
    public static interface Image {

        public static final String LV_PENTAGRAM="icon_lvpentagram";
        public static final String OP_BLOCK="icon_7block";
        public static final  String GENERAL_IMAGE_PLAYER = "player";
        public static final  String GENERAL_IMAGE_ASSISTANT = "Assistant";



        public static final int BORDER_IMAGE_REFW=20;
        public static final int BORDER_IMAGE_REFH=20;
        //----废弃----
        public static final String ARROW_BLUE="arrow_blue";//
        public static final String ARROW_RED="arrow_red";//



        public static final String BATTLE_MARK_1 = "battle_mark1";//战斗中  101
        public static final String BATTLE_MARK_2 = "battle_mark2";//可参战  102

        public static final String BATTLE_CITY_1="city_lv1_1";
        public static final String BATTLE_CITY_2="city_lv2_1";
        public static final String BATTLE_CITY_3="city_lv3_1";
        public static final String BATTLE_CITY_4="city_lv4_1";
        public static final String BATTLE_CITY_5="city_lv5_1";

        public static final int CARD_WIDTH=202;
        public static final int CARD_HEIGHT=234;


        public static final String TARGET_TAG_3="arrow_green";//进攻
        public static final String TARGET_TAG_4="arrow_yellow";//目标
        public static final String TARGET_TAG_5="taskMarket_red";//已选定目标
        public static final String MISS_REMARK="miss_remark";//缺失目标显示的标志

        public static final String GAMERS_VICTORY="victory";
        public static final String GAMERS_GAMEOVER="gameover";

        public static final String BG_GRAY="bg_gray";

        //默认底图
        public static final int DEFAULT_PT = 1;
    }
    //一些重要的配置的String信息
    public static interface StringName {
        public static final String markTrue="√";
        public static final String markFalse="×";
        public static final String[][] RomanStr = {
                {"","I","II","III","IV","V","VI","VII","VIII","IX"},  // 个位数举例
                {"","X","XX","XXX","XL","L","LX","LXX","LXXX","XC"},  // 十位数举例
                {"","C","CC","CCC","CD","D","DC","DCC","DCCC","CM"},  // 百位数举例
                {"","M","MM","MMM"}  // 千位数举例
        };

        public static final String infantryBs="infantryBs";
        public static final String artilleryBs="artilleryBs";
        public static final String armourBs="armourBs";
        public static final String shipBs="shipBs";
        public static final String airBs="airBs";
        public static final String fortBs="fortBs";
        public static final String specialBs="specialBs";
        public static final String submarineBs="submarineBs";
        public static final String infantryBo="infantryBo";
        public static final String artilleryBo="artilleryBo";
        public static final String armourBo="armourBo";
        public static final String shipBo="shipBo";
        public static final String airBo="airBo";
        public static final String fortBo="fortBo";
        public static final String specialBo="specialBo";
        public static final String submarineBo="submarineBo";
        public static final String movementCost="movementCost";
        public static final String defInf="defInf";
        public static final String defArt="defArt";
        public static final String defArm="defArm";
        public static final String defAir="defAir";
        public static final String defShip="defShip";
        public static final String exrLos="exrLos";
        public static final String noBorder= "000";//非边界的值


        public static final String  firstOpenPause="firstOpenPause";
        public static final String    firstOpenSpirit="firstOpenSpirit";
        public static final String  firstOpenLegionFeature="firstOpenLegionFeature";
        public static final String         firstOpenDailyTask="firstOpenDailyTask";
        public static final String firstOpenResolution="firstOpenResolution";
        public static final String         firstOpenConquestTask="firstOpenConquestTask";
        public static final String  firstOpenRegionInfo="firstOpenRegionInfo";
        public static final String         firstSelectCountry="firstSelectCountry";
        public static final String firstMainAndMineralUse="firstMainAndMineralUse";
        public static final String         firstMainAndOilUse="firstMainAndOilUse";
        public static final String firstMainAndAmmoUse="firstMainAndAmmoUse";
        public static final String         firstMainAndEnergyUse="firstMainAndEnergyUse";
        public static final String firstOpenWonder="firstOpenWonder";
        public static final String         firstOpenDiplomacy="firstOpenDiplomacy";
        public static final String firstUpdTech="firstUpdTech";
        public static final String        firstOpenLegionTech="firstOpenLegionTech";
        public static final String firstOpenUnitInfo="firstOpenUnitInfo";
        public static final String         firstMergeUnit="firstMergeUnit";

        //界面的名字,与layout对应
        public static final String defaultWindowName="default";
        //public static final java.lang.String functionId="FUNCTION_ID";
        public static final String stageId="STAGE_ID";
        public static final String selectCountryBattleGroup ="selectCountryBattleGroup";
        public static final String selectCountryConquestGroup ="selectCountryConquestGroup";

        //一些数字
        public static final String zero="0";
        public static final String one="1";
        public static final String two="2";

        public static final int ArmyType_infantry=1;
        public static final int ArmyType_artillery=2;
        public static final int ArmyType_armour=3;
        public static final int ArmyType_ship=4;
        public static final int ArmyType_air=5;
        public static final int ArmyType_fort=6;
        public static final int ArmyType_special=7;
        public static final int ArmyType_submarine=8;
        public static final String slash="/";


        public static final String NULL="null";

        public static final String colon=":";
        public static final String space1=" ";
        public static final String space2="  ";
        public static final String space3="   ";
        public static final String space4="    ";
        public static final String space5="     ";
        public static final String space6="      ";
        public static final String space7="       ";
        public static final String space8="        ";
        public static final String space9="         ";
        public static final String space10="         ";
        public static final String space11="          ";
        public static final String space12="           ";
        public static final String space13="            ";
        public static final String space14="             ";
        public static final String space15="              ";
        public static final String space16="               ";
        public static final String space17="                ";
        public static final String space18="                 ";
        public static final String space19="                  ";
        public static final String space20="                   ";
        public static final String space21="                    ";
        public static final String space22="                     ";
        public static final String space23="                      ";
        public static final String space24="                       ";
        public static final String space25="                        ";
        public static final String space26="                         ";
        public static final String space27="                          ";
        public static final String space28="                           ";
        public static final String space29="                            ";
        public static final String space30="                             ";


        public static final String DefArrayId_card="0";//defArray中的 存放卡牌的id
        public static final String DefArrayId_combatLeftCountry="1";
        public static final String DefArrayId_combatRightCountry="2";
    }

    //资源参数
    public static interface Class {
        public static final int StartScreen=0;
        public static final int MapDetailScreen=71;
        public static final int SMapScreen=81;
        public static final int GeneralScreen=-1;


        public static final int MainScreen=1;
        public static final int EmpireScreen=3;
        public static final int ConquestScreen=4;
        public static final int HistoryScreen=5;
        public static final int OptionScreen=6;//废弃
        public static final int MapEditScreen =7;
        public static final int HQScreen=8;
        public static final int SagaScreen =9;





        public static final int ScreenDefaultGroupId=0;
        public static final int GeneralScreenConquestLoadGroupId =1;
        public static final int GeneralScreenConquestFreeYearGroupId =2;
        public static final int GeneralScreenConquestPromptGroupId =3;
        public static final int GeneralScreenOptionGroupId=1; //废弃



        public static final int GeneralScreenMainPromptGroupId =1;
        public static final int GeneralScreenMainOptionGroupId =2;
        public static final int GeneralScreenMainGoodsGroupId =3;
        public static final int GeneralScreenMainAccountsGroupId =4;
        public static final int GeneralScreenMainOption2GroupId =5;
        public static final int GeneralScreenEmpireDialogueGroupId=0;
        public static final int GeneralScreenEmpireStageGroupId=1;

        public static final int GeneralScreenHistoryPromptGroupId =1;
        public static final int GeneralScreenMainDefaultGroupId =0;
        public static final int GeneralScreenHQDefaultGroupId =0;
        public static final int GeneralScreenHQMarkGroupId =1;
        public static final int GeneralScreenHQPromptGroupId =2;
        public static final int  SMapScreenSelectCountryGroupImageFlagId=1;
        public static final int SMapScreenGameCardGroupId =1;
        public static final int SMapScreenIntroductionGroupId =2;//介绍价值
        public static final int SMapScreenLegionTechGroupId =3;
        public static final int SMapScreenRegionBuildGroupId =4;
        public static final int SMapScreenArmyInfoGroupId =5;
        public static final int SMapScreenAchievementGroupId =6;
        public static final int SMapScreenGameResultGroupId =7;
        public static final int SMapScreenTradeInfoGroupId =8;
        public static final int SMapScreenTextGroupId =9;
        public static final int SMapScreenDialoguePromptGroupId =10;
        public static final int SMapScreenPauseGroupId =11;
        public static final int SMapScreenOptionGroupId =12;
        public static final int SMapScreenChiefGroupId =13;
        public static final int SMapScreenEditGroupId =14;
        public static final int SMapScreenArmyFormationGroupId =15;
        public static final int SMapScreenPromptGroupId =16;
        public static final int SMapScreenGeneralDialogueGroupId =17;
        public static final int SMapScreenDefaultShowGroupGroupId =18;


        public static final int GeneralScreenSagaSelectGroupId =1;
        public static final int GeneralScreenSagaStageGroupId =2;
        public static final int GeneralScreenSagaPromptGroupId =3;

    }


   /* public static interface Tool{

        public static final DecimalFormat Df0000=new DecimalFormat("0000");;
    }
*/


    //TODO 发布时把这个改成 false;
    public boolean ifDebug=false;
    //TODO 发给其他人测试的时候用这个
    public boolean ifTest=true;
    public static final int effectiveDate=20220705;

    public static final String game="armageddon1";
    public static final String charset="UTF-8";
    public static final String version="0.8.1 1\n";
    public static final String nowVersioninState
            ="机制调整:组合单位在陆地进行远程进攻时,如果其士气为高昂(>=75),则重置其士气到普通(50)\n" +
            "大炮台与要塞变更为工业时代才能建造\n" +
            "增加一个用来规定港口建造条件的参数\n" +
            "更新1894剧本"
            ;


    public static final String lastVersioninState =

            "0.8.0\n"+ "对1856的脚本事件进行修正\n" +
                    "修复保存时兵模方向出错问题\n" +
                    "更新骑兵与巨型要塞兵模\n"
                    +"移动单位导致其武器显示错误的bug\n" +
                    "英文字库丢字问题(使用暂代方案)\n" +
                    "对兵模错位进行修复\n"+
                    "把首次默认语言改成中文\n" +
                    "步兵,机动步兵价格调整\n" +
                    "统帅外交托管后,忽略资源赠予的报告\n" +
                    "修改初始地图风格设置\n" +
                    "主界面的UI文字显示优化\n"+
                    "主界面的分辨率添加一个自定义分辨率功能\n" +
                    "修复帝国模式英俄加载失败的bug\n" +
                    "调整帝国模式日本的初始状态\n" +
                    "统帅随机拉拢事件增加条件:好感度60以上\n" +
                    "优化帝国模式结算初时的卡顿问题\n" +
                    "修复托管操作的文本提示问题\n"+
                    "修复编辑器不能自由切换国家的bug\n" +
                    "修复编辑器在军团模式下右上角按钮的问题\n" +
                    "*修复快速建造栏与兵种建造栏的价格不同的bug\n"+
                    "修复城市叛乱有兵会导致卡兵的bug,现在如果城市叛乱,该区域的所有军团都会跟着叛乱\n" +
                    "修复军团背离时好感度计算可能错误的问题\n" +
                    "领土交涉事件中,增加武力要求领土,武力索要领土事件\n" +
                    "提高初始招募等级类的军团特性,其效果会受到招募地的指挥部等级限制\n" +
                    "德国在帝国模式与自由争霸模式的颜色更换\n"+
                    "自定义征服中,把不能选择国家,去除其首都国旗显示\n" +
                    "使用星星来标示选择国家时的剧本难度,三星为困难,一星为简单\n" +
                    "优化到下回合时,单位点击的操作\n" +
                    "优化兵种界面切换阵型的字体标签问题\n" +
                    "设置帝国模式日本部分关卡的单位不会被覆盖\n"+
                    "机制调整:玩家的组合单位在彻底行动后无法再调整战线\n" +
                    "统帅模式中,部分的外交托管不会再处理与敌对国家的外交缓和事件\n" +
                    "*弱点的机制调整:\n" +
                    "除要塞以为的地面单位在进攻之后显示进攻箭头，进攻箭头的背面就是其弱点。\n" +
                    "部队受到弱点攻击后有较大概率无法进行反击。\n" +
                    "在陆地上时，如果是组合单位且其弱点部分没有战线且受到的是近战攻击，还会有不同的效果：\n" +
                    "判断进攻部队的当前生命值的2倍是否大于目标的当前生命值时：\n" +
                    "将目标击退。如果目标准备回合为0,处于无法击退或被击退到山地，则额外受到10%当前生命值的伤害;如果消灭目标，则有几率移动到目标位置，并恐惧周围敌军。\n" +
                    "否则：目标单位额外受到10%生命上限的伤害;如果消灭目标后，则使士气为高昂。\n" +
                    "修正部分的兵模显示错误\n" +
                    "修复目标箭头不消失的bug\n" +
                    "修复人口导致的可建造卡牌无法更新的bug\n" +
                    "增加机制:如果主要领土或核心领土被占领,则失去一定的国家稳定度\n" +
                    "优化宣战时文本\n" +
                    "调整统帅的月凝聚力加成数值\n" +
                    "去除单位合并之后重置攻击的机制\n" +
                    "放开文化卡牌在全模式的建造,并优化文化的文本说明\n" +
                    "统帅战争支持度机制调整:战争支持度范围为0~100;玩家失去城市时,减少对应城市等级数值的战争支持度;玩家占领城市时,获得1战争支持度;玩家单位死亡时,减少对应军衔数值的战争支持度;单位价格=单位价格*(150-战争支持度)/100;\n" +
                    "编队的同兵种武器等级加成削弱\n"+
                    "增加攻击检测\n" +
                    "修复和谈时失去占领的地区单位控制权的bug\n" +
                    "修复迂回,冲击技能和击退机制可能到海里的bug\n" +
                    "增加工业时代的火炮,燃油,能源机制的启用条件,玩家的军团科技等级之和超过30\n"+
                    "暴击机制修改:士气或暴击率的最大值在>70(unitMoraleMaxLimit)的状态下,才有几率触发暴击,触发暴击后如果士气高于50(resetUnitMoraleMax),则士气重置为该值\n" +
                    "切换语言之后将重置首次操作提示\n" +
                    "使mod模式文件config.properties可以对缺失兼容,如果缺失会使用原版,并在控制台进行提示\n" +
                    "调整步兵战线属性,削弱火炮\n" +
                    "ai对玩家自动结盟事件只有在其好感度>60(allianceEventFavorLimit)时,才能触发\n" +
                    "ai对玩家的合并事件只有在其自身稳定度<50并且与玩家是邻国且双方实力差距在3倍以上且核心领土有丢失时,才能触发\n" +
                    "优化了备用存档保存机制:帝国模式仅是在生成游戏或更新剧本的时候备份一次备用存档 其他模式都是任意保存的时候都把旧存档备份\n" +
                    "增加指令，启用备用存档 【useBackUpSav x】,[x]指存档的stageId\n" +
                    "区域指挥部等级机制增加:可以提高招募的單位的初始武器等級\n"+
                    "增加弹出特效\n" +
                    "统帅模式增加包围网强化事件:玩家的世界大战\n" +
                    "config.properties增加editUpdateIsCheat,默认为true,值为true会将编辑器修改后的btl视为已作弊\n" +
                    "修复远程攻击不正确结算的bug\n" +
                    "修复陆军单位护航后的攻击错误问题\n" +
                    "武器效率变更:提高武器基础效率,降低升级后提升效率,保持满级后效率与原版持平\n" +
                    "增加机制:准备回合大于1的单位显示为灰色,其受到攻击后不能反击\n" +
                    "增加机制:处于敌对区域不能回血\n" +
                    "机制变更:组合单位进行远程攻击,并且目标非建筑非要塞时,根据攻击距离对其伤害进行削弱\n" +
                    "机制变更:伤害不再是随机值,而是与士气挂钩:伤害=基础伤害+(最高伤害-基础伤害)*士气/100\n" +
                    "降低要塞属性\n" +
                    "优化控制台伤害记录输出\n" +
                    "完善1856脚本事件"+

            "0.7.4\n"+ "重新优化招募时对友好将领的选择:如果国家存活:需要判断好感度和是否敌对,否则判断历史国家关系\n" +
                    "优化单位撤回检查\n" +
                    "当部队满队后在其中一个部队有编队或者武器等级可以合成其对应的一队单位组合来升级\n" +
                    "*修复单位组合后编队等级与武器等级弄反了的bug\n" +
                    "*重新优化内存\n" +
                    "增加全部单位行动回合的显示标记\n" +
                    "ai操作回合优化\n" +
                    "优化弹框时间的提示时机\n" +
                    "禁止在非完全回合使用外交等功能\n" +
                    "更新特殊两个奇物拥有时的ui显示\n" +
                    "优化单位兵模更新机制\n" +
                    "修复初始不能造兵的bug\n" +
                    "单位武器等级升级提示\n" +
                    "玩家不再成为ai其贸易的目标了\n" +
                    "修复脚本创建国家失败的bug\n"+
                    "修复单位护航船兵模切换的bug\n" +
                    "舰船和潜艇血量调整\n" +
                    "部分卡牌的价格调整\n" +
                    "修复城市血量为0时,首次打开仍然可以招募的bug\n" +
                    "优化迷雾机制\n" +
                    "玩家援助事件调整为只在前20%回合可出现\n" +
                    "增加防卡机制:如果玩家在最后阶段(右下角为兵或者星星)卡主,可以点击托管按钮直接强制结束\n" +
                    "修复自动建造时部队提升属性的兵种显示bug\n" +
                    "增加声音提示在回合彻底结束后\n" +
                    "修复两个区域的洲际错误问题\n" +
                    "*军团模式增加将领招募功能\n" +
                    "优化军团模式兵种合并效果\n"+
                    "部分将领图像处理\n" +
                    "合并机制优化:单个兵种之间可以通过合兵来补充血量\n" +
                    "合兵机制:部队护航的时候,对护航目标的编队和血量进行更新\n" +
                    "优化不宣而战机制:玩家在统帅模式不能再不宣而战了\n" +
                    "修复和平状态仍然可以再战的bug\n" +
                    "调整维护费结算顺序\n" +
                    "随机结盟事件前增加了条件判定\n" +
                    "征服游戏模式顺序调整为军团>>争霸>>统帅\n" +
                    "针对互保协定,增加外交卡牌:撕毁条约\n" +
                    "历史事件效果调整:如果历史事件不涉及玩家但是显示,则不现实其效果\n" +
                    "满级卡牌(科技,建筑设施为10级,如果是单位,则是看其人口占用)不再显示\n" +
                    "更新单位的战线显示规则\n" +
                    "增加攻击方向机制,攻击敌人的攻击方向的背面将必然先手并且敌人将大概率无法反击\n"+
                    "要塞单位无攻击方向\n"+
                    "当一个部队连续修整5回合以上时,隐藏其攻击方向\n"+
                    "修复教程\n" +
                    "优化提示字体大小显示\n" +
                    "对接多语言\n" +
                    "优化保存机制(为适配steam)\n" +
                    "优化多线程方法\n" +
                    "修复帝国模式卡关的bug\n" +
                    "pc右键功能增加:查看点击区域详情\n" +
                    "非中文版去除更新报告\n" +
                    "修复撕毁条约卡牌图片问题\n" +
                    "增加兵种血条颜色的更新\n" +
                    "修复点击地标建造闪退的bug\n" +
                    "优化游戏保存信息\n" +
                    "修复合兵时有几率吞兵,士气下降的bug\n" +
                    "修复升编提示错误\n" +
                    "优化护甲的减伤计算\n"+
                    "修复验证码错误问题\n" +
                    "屏蔽模式增加对帝国模式显示的验证\n" +
                    "最低人口下限改为300\n" +
                    "过滤掉附属国的部分随机外交事件\n" +
                    "完善技能融合\n" +
                    "修复任务提交不更新说明的bug\n" +
                    "优化任务:壮士暮年奖励和时间\n" +
                    "修复通过征服任务胜利不删除存档的bug\n" +
                    "优化执行报告中的数据\n" +
                    "存档时增加对外交数据的验证\n" +
                    "统帅模式增加了争议议案的颜色提示\n" +
                    "统帅的国体选项改成国家策略选项\n" +
                    "增加敬请期待,将后两个征服遮盖\n" +
                    "修复部分兵模出错的bug\n"+
                    "设置中的提示改成右靠齐\n" +
                    "修改帝国,传奇,历史的对话为可拖动\n" +
                    "修复hq界面提示被遮挡的bug\n"+
                    "统帅描述bug修复\n"+
                    "部分文字描述改为滑动"+

            "0.7.3\n"+"安卓右边滑动栏放大一倍\n" +
                    "重新优化部分方法,极大的降低报错几率\n" +
                    "战报系统的错误位置调整\n" +
                    "安卓的字体绘制优化\n" +
                    "修改报错日志输出位置\n" +
                    "将领可招募时以绿色字体显示\n" +
                    "优化错误记录机制\n" +
                    "优化存档保存机制\n" +
                    "精简ai的操作,加快游戏速度\n" +
                    "重置人口机制,部队组合后人口占用降低\n" +
                    "调整移动力设置\n"+
                    "轻步兵调整为支援步兵\n"+
                    "下一回合的机制修改:将优先进行玩家及其周边势力的回合,玩家可以操作但不能退出,在全部运行完后玩家才可进行退出等操作\n"+
                    "修复一个导致ai无法进攻的bug\n"+
                    "修复初始回合无法造兵的bug\n"+
                    "优化贸易机制\n"+
                    "修复单位的文本错误\n"+
                    "修复玩家部分单位的士气显示错误\n"+
                    "修复收入bug\n"+
                    "调整ui按钮及其显示策略\t\n" +
                    "更新城市区域的军事建造方针策略-->发展城市的军事类建设或训练单位属性,当周边或区域处于战争状态时会自动招募军队,用红色标记\n" +
                    "优化组队合并后技能升级机制:以技能等级总数最高的单位为技能主体,如有栏位,则顺序融合次级单位技能,如果次级单位技能与主题单位技能一致且其可升级.最后如果单位技能和小于军衔和,则随机补全技能\n" +
                    "优化单位界面将领的军章标志显示\n" +
                    "修复开火bug\n" +
                    "优化游戏模式的切入\n" +
                    "自定义模式可使用的模式现在更新为争霸和军团\n" +
                    "修复自定义模式进入错误的bug\n" +
                    "优化ai的屯兵策略\n" +
                    "增加征服中争霸模式占领敌人首都后获得其全部领土\n" +
                    "增加征服中有外交的模式占领敌人首都后如果其凝聚力过低,其有几率投降或成为附属国\n" +
                    "脚本中的创造单位指令增加一个是否恢复界面的变量和功能\n" +
                    "配置选项中增加一个能否组合的设置\n" +
                    "屏蔽设置中增加一个是否取消地名显示的配置\n" +
                    "更新异步任务组件,增加筛选掉重复任务的功能\n" +
                    "在执行玩家下回合的时候，增加涉及到的相关区域计算\n" +
                    "修复单位在有组合阵亡时属性不变的bug\n"+
            "0.7.2\n"+
                    "名人名言更新\n" +
                    "增加城市设施栏位建造限制:城市设施可建造的数量由城市的城市等级决定\n" +
                    "增加玩家在统帅模式秩序阵营时,当有弱于玩家势力的非敌对国的核心或首都受到非玩家盟友占领时,会寻求玩家庇护/同盟/支持的事件\n" +
                    "修改印度奇物为中立奇物,效果为:增加单位休整时恢复效率\n" +
                    "修改单位休整回复的血量至多为其血量上限的50%\n" +
                    "增加战报系统\n" +
                    "强化组合单位合并功能:当两支部队均为组合部队且编队数量和小于8时就可以合并\n" +
                    "重置历史事件系统\n" +
                    "增加突袭设定:如果进攻的目标背面,且目标非要塞类且是近战攻击,则有(我方士气-(目标士气+驻守城市血量百分比)/2)概率目标无法反击\n" +
                    "设置增加 是否显示单位升级提示\n" +
                    "设置增加 忽略bug报告\n" +
                    "实装了各种声音效果\n" +
                    "重置预览地图风格 由橘黄-->白蓝\n" +
                    "增加单位战线变换仅在其自身回合内有效\n" +
                    "优化国界算法\n" +
                    "优化金融等级对资源的保存计算: 玩家资源上限=资源收入*(10+金融等级)/20,超出部分减半\n" +
                    "重新优化单位组合后技能规则\n" +
                    "规则变更:区域升级城市等级时,如果该区域的城市等级是唯一最高级,则升级回合增加\n" +
                    "优化军团模式选择操作\n" +
                    "优化伤害结算动画\n" +
                    "优化军团模式科技升级说明\n" +
                    "优化了字体绘制方法\n" +
                    "优化了所有的字体效果\n" +
                    "美化小黑人将领标志\n" +
                    "追加国家名显示大小配置,该配置会影响国家名大小与伤害显示大小\n" +
                    "优化卡牌价格在不同分辨率的显示\n" +
                    "优化贸易字体显示\n" +
                    "增加贸易最低价格为1\n" +
                    "对帝国界面和荣誉界面进行分辨率适配\n" +
                    "修复飞机攻击优先级bug\n" +
                    "修复无法查看飞机技能的bug\n" +
                    "修复奇物数据bug\n" +
                    "修复运输船模型显示位置\n" +
                    "修复单位有护航时攻击属性在海洋显示错误的bug\n" +
                    "修复单位组合可能导致护航船丢失的bug\n" +
                    "修复顿河区域河流显示问题\n" +
                    "修复军团模式托管ai无效的bug\n" +
                    "修复安卓手机端缩放条bug\n" +
                    "修复loading页面加载\n" +
                    "修复右边栏单位经验条对飞机显示错误的bug\n" +
                    "修复编辑器切换页面失效的bug\n" +
                    "修复国家合并的概率显示bug\n" +
                    "修复建造卡牌错误的bug\n" +
                    "修复收入溢出计算错误的bug\n" +
                    "修复一个导致ai选择卡牌错误的bug\n"+
            "0.7.1\n"+
                    "\t名词变更:威望点-->声望点\n" +
                    "\t调整资源加载\n" +
                    "\t调整目标单位为山地地形时,坦克的碾压特性不能触发\n" +
                    "\t\n" +
                    "\t增加部分军团科技界面的说明\n" +
                    "\t增加组合兵种的精英步兵根据站位显示不同模型\n" +
                    "\t增加提示给征服中的部分初次操作\n" +
                    "\t增加非pc端游戏内界面右侧缩放滑动条\n" +
                    "\t增加百科\n" +
                    "\t增加征服缩略图,并优化相关显示\n" +
                    "\t增加右边卡牌栏经验条显示\n" +
                    "\t增加编队对单位防御力的加成\n" +
                    "\t增加pc全屏鼠标移到地图边缘可以移动游戏地图\n" +
                    "\t增加将领根据其所属来增加对应战斗属性\n" +
                    "\t主页面设置增加敏感屏蔽选项\n" +
                    "\t主页面设置增加切换缩略图风格\n" +
                    "\t主页面设置增加垂直同步选项\n" +
                    "\t主页面设置增加休闲模式\n" +
                    "\t设置增加自动保存功能\n" +
                    "\t增加点击先单位的射程范围效果\n" +
                    "\t增加游戏内快捷键快速定位功能\n" +
                    "\t资源管理增加锁设定\n" +
                    "\t优化决议检查\n" +
                    "\t优化编队显示 \n" +
                    "\t优化经验升级机制\n" +
                    "\t优化要塞兵模\n" +
                    "\t优化游戏主界面ui适配到1920*1080分辨率\n" +
                    "\t优化编队解散功能\n" +
                    "\t优化国家边界显示问题\n" +
                    "\t优化自由阵型算法\n" +
                    "\t优化点击地块闪烁\n" +
                    "\t优化触摸缩放,虽然仍然不算好用\n" +
                    "\t优化地图坐标计算方式\n" +
                    "\t优化上将流程\n" +
                    "\t优化地块详情资源显示\n" +
                    "\t优化鼠标滑轮缩放\n" +
                    "\t优化技能特效图标显示\n" +
                    "\t优化主界面背景图\n" +
                    "\t优化主界面的附界面增加遮盖\n" +
                    "\t优化主界面颜色显示\n" +
                    "\t优化地块颜色显示\n" +
                    "\t优化主界面ui适配算法\n" +
                    "\t优化单位兵模比例显示\n" +
                    "\t优化地铁标志显示\n" +
                    "\t优化对mod的语言支持\n" +
                    "\t优化统帅模式的初始统帅政策信息\n" +
                    "\t优化大部分卡牌画面\n" +
                    "\t修复ui错位bug\n" +
                    "\t修复兵模特效切换导致的闪退bug\n" +
                    "\t修复pc多选行动的操作bug\n" +
                    "\t修复单位升级图标bug\n" +
                    "\t修复单位修整时士气满额增加的bug\n" +
                    "\t修复兵牌的编队显示bug\n" +
                    "\t修复当单位到达可战斗位置时选择其他位置,模型不复位的bug\n" +
                    "\t修复单位详情ui变形问题\n" +
                    "\t修复游戏结束时的横幅长度\n"+
            "0.7.0\n"+
            "名词变更:融洽度-->政治点数;国家稳定度-->凝聚力\n" +
            "无限宣战bug\n" +
            "修复兵牌模式战线显示bug\n" +
            "修复决议bug 威望不足可以选择决议\n" +
            "增加百科功能(暂未完成)\n" +
            "修复战线防御数值bug\n" +
            "增加征服目标\n" +
            "修复外交卡无法使用的原因未显示的bug\n" +
            "优化小地块操作方式\n" +
            "优化在非军团模式的标志姿态下对部队的指挥操作\n" +
            "优化非组合类部队能力的升级\n" +
            "修复部分单位属性bug\n" +
            "修改单位能力储存位置\n" +
            "重置单位进攻的方法\n" +
            "移动版暂时移除地图读取功能\n" +
            "增加设置屏幕拖动速度\n" +
            "增加ai屯兵设置\n" +
            "修复小国在科技界面头像显示出现的bug\n" +
            "兵种属性界面增加护航船的攻击效率显示\n" +
            "重修收入价值\n" +
            "重写远程攻击\n" +
            "远程攻击随根据距离削弱\n" +
            "优化右边栏飞机ui显示\n" +
            "增加初次打开招募界面时显示所有可招募兵种\n" +
            "修复右边栏船类卡片显示bug\n" +
            "修复士气颜色导致的显示bug\n" +
            "优化兵模位置显示\n" +
            "军衔显示位置优化\n" +
            "优化统帅界面政治点数说明\n" +
            "增加陆军如果可以组合,则可以直接建造\n" +
            "重绘地图素材\n" +
            "优化组合单位可行动时用战线标示来显示\n" +
            "修改玩家军团科技升级后需要等到其研究回合至0后才能生效\n"+
            "0.6.9\n进攻战线效果调整,当有完全战线的一侧攻击无战线的一侧时才产生暴击\n" +
                    "修复军团模式有可能单位无法操作的bug\n" +
                    "修复官员提议太靠前无法实时更新的bug\n" +
                    "重新优化自动战线算法\n" +
                    "修复组合部队无奇观效果加成的bug\n"+
            "0.6.8\n增加随机海浪\n" +
                    "修复统帅模式科技收入的bug\n" +
                    "修复军团模式的攻击计算bug\n" +
                    "修复重开加载时使用手势闪退的bug\n" +
                    "优化将领对单位的攻击加成\n" +
                    "优化白令海峡的海洋色块展示\n" +
                    "编辑器不再能直接修改模式\n" +
                    "开始选择不同模式时根据模式调整游戏战力\n" +
                    "军团模式增加单位说明\n" +
                    "军团模式单位的计算武器等级调整为军团的攻击等级\n" +
                    "统帅模式自动建造的使用比例调整为根据收入而不是拥有\n" +
                    "被玩家宣战时对被选战国进行一次强化"
                    + "0.6.7\n非宣战低关系度国家颜色变为橘色\n" +
                    "组合单位价格和血量不再按比例降低\n" +
                    "进攻非组合单位,如果是近战且其无任何防线,则按其组合数量的倍数增加伤害\n" +
                    "组合单位中的同单位会增加对应的武器等级\n" +
                    "组合单位的最高武器等级上限锁定为9\n" +
                    "修复组合单位受击达到一定伤害无法消除编队的bug\n" +
                    "海洋中的组合部队不会再遭受倍击\n" +
                    "增加被玩家宣战后的宣言\n"+

                    "0.6.6\n显示首都标识\n" +
                    "修复民兵和陆军组合导致陆军消失的bug\n" +
                    "修复可以进入隐藏的敌军的陆军位置的bug\n" +
                    "玩家导致ai敌对增加提示\n" +
                    "修复关系度高敌对导致外交卡无法使用的bug\n" +
                    "修复解散无效的bug\n"+
                    "修复移动视野更新延迟的bug\n"+
                    "优化ai托管,只会进攻好感度小于30的敌对国家\n"+
                    "增加ai对工业,研究所的建造概率\n"+
                    "增加ai伏击玩家\n"+
                    "增加高级兵种的建造要求\n"+

                    "0.6.5\n修复玩家进攻时有隐藏单位会自动移动到目标点的bug\n" +
                    "优化阵营显示色\n" +
                    "修复随机对话显示bug\n" +
                    "国家名修复\n" +
                    "优化进攻动画伤害绘制时间\n" +
                    "优化地形格子显示\n" +
                    "优化战线效果说明\n" +
                    "优化将军头像背景在缩放模式的显示问题\n"+
                    "0.6.4\n"+
                    "修复国家概括文本bug\n" +
                    "优化文本bug显示,而不是闪退\n" +
                    "优化国家概括将领的显示\n" +
                    "优化决议改善关系,签订契约的bug\n" +
                    "优化阵营色显示\n" +
                    "优化ai托管策略\n" +
                    "0.6.3\n修复加载bug\n" +
                    "修复设置effect的闪烁bug\n" +
                    "修复优化攻击效率计算导致的卡bug\n" +
                    "修复绘制优化导致的bug\n"
                    +"0.6.2:\n1.修复取消自动建造时ui显示的bug\n" +
                    "2.修复重置血量计算错误的bug\n" +
                    "3.调整要塞的血量,攻击\n" +
                    "4.优化战线的攻击效率结算\n" +
                    "5.增加指令提取存档\n"

                    + "0.6.1:\n修复组合部队攻击错误\n" +
                    "\t修复攻击建筑不显示攻击效果\n" +
                    "\t修复ai组合部队较少的现象\n" +
                    "\t修复bug重启?需测试\n" +
                    "\t重置修复大部分ui\n" +
                    "\t修复工事,设施建造不显示的bug"
            ;

}




















