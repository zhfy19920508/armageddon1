package com.zhfy.game.framework;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.tool.DataSaveSecurity;
import com.zhfy.game.framework.tool.LazyBitmapFont;
import com.zhfy.game.framework.tool.PropertiesUtil;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.XmlIntDAO;
import com.zhfy.game.model.content.XmlStringDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static com.zhfy.game.config.ResDefaultConfig.DEFAULT_CHARS;


public class GameConfig {
    public boolean ifColor; //true 则显示有fog
    public boolean ifEffect;//是否显示特效
    public boolean ifAutoSave;//是否自动保存
    public boolean ifShield;
    public boolean ifAiActAndNextRound;
    public boolean ifPromptUnitUpd;
    public boolean ifLeisureMode;
    public BitmapFont gameFont;
    public float gameFontScale;
    public XmlReader reader;
    private String Platform;
    private XmlIntDAO CONFIG_SOURCFILE;//读取config_file中的文件生成
    private String MOD_PATH; //  为mod\modName\的格式
    private String MOD_NAME;
    private int MOD_ID;
    public boolean ifMoveInFullScreen;
    public boolean ifConquestReport;
    public boolean shieldAreaName;
    public String shieldCountrys;
    public String invincibleCountrys;
    public float legionNameScale;
    public boolean ifIgnoreBug;

  /*  public int numberWidth;
    public int pointWidth;
    public int letterWidth;*/



    public int File_Potion;//0 interial 1 local 2 exterial
   // private boolean fontIsOriginal;
   // private boolean strIsOriginal;


    private XmlIntDAO CONFIG_FILEPATH;
    private XmlIntDAO CONFIG_LANGUAGE;

    private XmlIntDAO CONFIG_RES;
    private XmlIntDAO CONFIG_LAYOUT;

    private XmlIntDAO DEF_AGE;
    private XmlIntDAO DEF_AREA;
    private XmlIntDAO DEF_ARMY;
   private XmlStringDAO DEF_ARRAY;
    private XmlIntDAO DEF_BATTLE;
    private XmlIntDAO DEF_SPIRIT;
    private XmlIntDAO DEF_CARD;
    private XmlIntDAO DEF_COUNTRY;
    private XmlIntDAO DEF_HEVENT;
    private XmlIntDAO DEF_FACILITY;
    private XmlIntDAO DEF_GENERAL;
    private XmlIntDAO DEF_RGENERAL;
    private XmlIntDAO DEF_HISTORY;
    private XmlIntDAO DEF_MAP;
    private XmlIntDAO DEF_SMALLMAP;
    private XmlIntDAO DEF_STAGE;
    private XmlIntDAO DEF_TERRAIN;
    private XmlIntDAO DEF_TERRAINIMG;
    private XmlIntDAO DEF_WEAPON;
    private XmlIntDAO DEF_WEATHER;
    private XmlIntDAO DEF_RDIALOGUE;
    private XmlStringDAO DEF_MODEL;
    private XmlIntDAO DEF_REVENT;
    private XmlIntDAO DEF_LEGIONFEATURE;

    private XmlIntDAO DEF_CZONE;
    private XmlIntDAO DEF_SKILL;
    private XmlIntDAO DEF_ANIMATION;
    private XmlIntDAO DEF_RTASK;
    private XmlIntDAO DEF_EMPIRE;
    private XmlIntDAO DEF_SAGA;
    private XmlIntDAO DEF_SAGASTAGE;
    private XmlIntDAO DEF_VIEW;
    private XmlIntDAO CONFIG_MOD;
    private XmlIntDAO DEF_UNITFEATURE;
    private XmlIntDAO DEF_LEGIONPOLICY;
    private XmlIntDAO DEF_DIPLOMATICATTITUDE;
    private XmlIntDAO DEF_WONDER;


    private XmlIntDAO DEF_STRATEGICREGION;//strategicRegion
    private XmlIntDAO DEF_SHIELDIMAGE;
    private XmlIntDAO DEF_SHIELDSTR;
    private XmlIntDAO CONFIG_MUSIC;
    private XmlIntDAO CONFIG_RULE;
    private XmlIntDAO CONFIG_CHIEFBUFF;
    private XmlIntDAO CONFIG_CHIEFCONFIG;
    private XmlStringDAO CONFIG_SOUND;
    private XmlStringDAO DEF_EFFECT;
    private XmlIntDAO DEF_REPORT;

    public int mainBgConquestId;
    public int mainBgEventId;
    public int mainBgEditId;

    public XmlReader.Element ruleE;
    public int ruleId;

    private MainGame game;

    public int getModId(){
        return MOD_ID;
    }
    public String getModPath(){
        return MOD_PATH;
    }

    //public XmlIntDAO DefWonder = new XmlIntDAO(reader.parse(Gdx.files.internal("config/def_wonder.xml")));

    private List<Color> COUNTRY_COLORS;
    private ObjectMap<String, String> TERRAINIMG_MAP;
    private XmlStringDAO RULE_MAPBIN;

    //private boolean ifHD;
   // public Preferences playerConfig;
    public DataSaveSecurity playerInfo;

    public PropertiesUtil playerConfig;

    public boolean ifDrawArmyMark;
    public LanguageManager lm;
    public    boolean isExtAvailable;
    public  boolean isLocAvailable;
    //private Array< XmlReader.Element> sortEs;

    public GameConfig(MainGame game) {
        this.game=game;
        this.gameFontScale=1.0f;
        this.legionNameScale=1.0f;
        //TODO 未来这个可以存入云存档
      //  this.playerInfo=Gdx.app.getPreferences("playerInfo");
        playerInfo =new DataSaveSecurity(ResDefaultConfig.Path.PlayerInfoFilePath);

        playerConfig =new  PropertiesUtil(ResDefaultConfig.Path.GameOptionPropertiesPath,true);

        isExtAvailable = Gdx.files.isExternalStorageAvailable();
        isLocAvailable = Gdx.files.isLocalStorageAvailable();

      //  this.playerConfig =Gdx.app.getPreferences(ResDefaultConfig.game);
      //  this.playerConfig = Gdx.files.local(ResDefaultConfig.Path.SaveFolderPath+ResDefaultConfig.game);



        reader = new XmlReader();
        Platform = Gdx.app.getType().toString();
        //ifHD= config.getBoolean("ifHD",true);
        ifColor =  playerConfig.getBoolean("ifColor", true);
        ifEffect=  playerConfig.getBoolean("ifEffect",true);
        ifAutoSave= playerConfig.getBoolean("ifAutoSave",true);
        ifShield= playerConfig.getBoolean("ifShield",!Gdx.app.getType().equals(Application.ApplicationType.Desktop));
        ifAiActAndNextRound= playerConfig.getBoolean("ifAiActAndNextRound",false);
        ifLeisureMode= playerConfig.getBoolean("ifLeisureMode",false);
        ifIgnoreBug= playerConfig.getBoolean("ifIgnoreBug",true);
        ifPromptUnitUpd= playerConfig.getBoolean("ifPromptUnitUpd",false);

        resetModConfig();

        DEF_MAP = new XmlIntDAO(reader.parse(getXmlFileHandle(15)));
        lm = new LanguageManager(); // 初始化语言管理器，加载语言
        loadGameFont();
        mainBgConquestId=ComUtil.getRandom(0,3);
        //pixmap/eventbg 文件夹下有12张图片,记得更新
        mainBgEventId =ComUtil.getRandom(0,3);
        mainBgEditId =ComUtil.getRandom(0,3);
        ruleId=-1;
       // initFontWidth();
        ifMoveInFullScreen=false;
        XmlReader.Element xmlE=getCONFIG_LANGUAGE().getElementById(getLangueageId());
        if(ifShield&&xmlE!=null){
            shieldCountrys=xmlE.get("shieldCountrys","");
            invincibleCountrys=xmlE.get("invincibleCountrys","");
        }else {
            shieldCountrys="";
            invincibleCountrys="";
        }
    }

   /* private void initFontWidth() {
        GlyphLayout label = new GlyphLayout();
        label.setText(gameFont,ComUtil.getSpace(1));
        float spaceW = label.width;
        label.setText(gameFont,"1");
        label.setText(gameFont,"N");
        letterWidth=(int) (label.width/spaceW);
        label.setText(gameFont,"/");
        pointWidth=(int) (label.width/spaceW);
    }*/
    public boolean isShieldCountry(int countryId){//屏蔽国: 该国家无法选择
        if(ifShield&&ComUtil.ifHaveValueInStr(shieldCountrys,countryId)){
            return true;
        }
        return false;
    }
    public boolean isInvincibleCountrys(int countryId){//无敌国,不可选择且中立,并每回合强制重置其历史领土
        if(ifShield&&ComUtil.ifHaveValueInStr(invincibleCountrys,countryId)){
            return true;
        }
        return false;
    }

    private void loadGameFont() {
        XmlReader.Element fontE= getCONFIG_LANGUAGE().getElementById(getLangueageId());
         FreeTypeFontGenerator generator;//TTF字体发生器
        generator=new FreeTypeFontGenerator(getFontFile());
        ifConquestReport=fontE.getBoolean("ifConquestReport",false);
        shieldAreaName=fontE.getBoolean("shieldAreaName",false);
       /* FreeTypeFontGenerator.FreeTypeFontParameter parameter =new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size=fontE.getInt("fontSize");
        parameter.characters=lm.currentLanguageStr;

        Gdx.app.log("characters",parameter.characters);
                gameFont = generator.generateFont(parameter);
        generator.dispose();
        gameFont.getData().setLineHeight(gameFont.getData().lineHeight*fontE.getFloat("fontLineRate"));
        gameFont.getData().markupEnabled=true;
        gameFontScale=fontE.getFloat("fontScale");*/

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size=fontE.getInt("fontSize",34);
        parameter.characters=lm.currentLanguageStr;
        gameFontScale=fontE.getFloat("fontScale",1f);
        legionNameScale=fontE.getFloat("legionNameScale",1f);
        if(!Gdx.app.getType().equals(Application.ApplicationType.Desktop)&&fontE.getBoolean("ifFontChangeInOther",false)){
            gameFontScale=gameFontScale*2f;
            legionNameScale=legionNameScale*2f;
            parameter.size=parameter.size/2;
        }
        gameFont=new LazyBitmapFont(generator,parameter.size,Color.WHITE);
        gameFont.getData().setLineHeight(gameFont.getData().lineHeight*fontE.getFloat("fontLineRate"));
        gameFont.getData().markupEnabled=true;
     //   generator.dispose();
    }

    private FileHandle getFontFile() {
         //
         XmlReader.Element xmlE=getCONFIG_LANGUAGE().getElementById(getLangueageId());
        if(xmlE!=null){
            if(getModId()==0){
                return Gdx.files.internal(xmlE.get("fontPaht"));
            }else {
                FileHandle  fileHandle= Gdx.files.local(getModPath()+xmlE.get("fontPaht"));
                if(!fileHandle.exists()){
                    Gdx.files.internal(xmlE.get("fontPaht")).copyTo(fileHandle);
                }
                return fileHandle;
            }
        }else {
            return Gdx.files.internal(new StringBuilder("font/text_").append(getLangeuageMark()).append(".ttf").toString());
        }
    }

    private FileHandle getXmlFileHandle(int id){
        if(MOD_ID==0){
            return Gdx.files.internal(CONFIG_SOURCFILE.getElementById(id).get("path"));
        }else {
            XmlReader.Element e= CONFIG_FILEPATH.getElementById(id);
            if(e==null||File_Potion==0||!e.getBoolean("canMod")){
                return Gdx.files.internal(CONFIG_SOURCFILE.getElementById(id).get("path"));
            }else{
               /* FileHandle modF=Gdx.files.local(MOD_NAME+ e.get("path"));
                if(modF.exists()){
                    return  modF;
                }else {
                    Gdx.files.internal(CONFIG_SOURCFILE.getElementById(id).get("path")).copyTo(modF);
                    return modF;
                }*/
                FileHandle modF=null;
                switch (File_Potion){
                    case 1:
                        modF=Gdx.files.local(MOD_PATH + e.get("path"));
                        break;
                    case 2:
                        modF=Gdx.files.external(MOD_PATH + e.get("path"));
                        break;
                }
                if(modF!=null&&modF.exists()){
                    return  modF;
                }else {
                    Gdx.files.internal(CONFIG_SOURCFILE.getElementById(id).get("path")).copyTo(modF);
                    return modF;
                }
            }

        }
    }
    public String getModName(){
        return MOD_NAME;
    }





    //重置mod数据
    public void resetModConfig() {
        XmlReader.Element modEs =null; FileHandle sFile,tFile;
        tFile= Gdx.files.local("config_mod.xml");

        if(!tFile.exists()) {
             sFile = Gdx.files.internal("config/config_mod.xml");
             sFile.copyTo(tFile);
        }
        modEs =reader.parse(tFile);

        CONFIG_MOD=new XmlIntDAO(modEs);

        MOD_ID= playerConfig.getInteger("useModId",0);
        if(MOD_ID>=modEs.getChildCount()){
            MOD_ID=0;
            playerConfig.putInteger("useModId",0);
            playerConfig.flush();
        }
        File_Potion =modEs.getChild(MOD_ID).getInt("filePotion");
       // fontIsOriginal=modE.getBoolean("fontIsOriginal");
        MOD_NAME=modEs.getChild(MOD_ID).get("name");
        MOD_PATH ="mod/"+MOD_NAME+"/";
        sFile=Gdx.files.internal("config/config_file.xml");
        CONFIG_SOURCFILE =new XmlIntDAO(reader.parse(sFile));
        if (MOD_ID==0|| !ResDefaultConfig.supportMod) {
            CONFIG_FILEPATH = CONFIG_SOURCFILE;
        }else {
            XmlReader.Element modE=modEs.getChild(MOD_ID);
         /*   config_file作为配置文件,只能从核心去读取
             tFile=Gdx.files.local("mod/"+modE.get("name")+"/config_file.xml");
            if(!tFile.exists()) {
                sFile.copyTo(tFile);
            }
            CONFIG_FILEPATH =new XmlIntDAO(reader.parse(tFile));*/
            CONFIG_FILEPATH = CONFIG_SOURCFILE;
            //设置语言
            int language=ComUtil.getFirstNumber(modE.get("supportLanguage"),-1);
            if(language>0){
                setLanguage(language);
            }
        }

    }

    /*public void setIfAnimation() {
        if(ifHD){
            config.putBoolean("ifHD",false);
        }else {
            config.putBoolean("ifHD",true);
        }
        config.flush();
        this.ifHD=config.getBoolean("ifHD");
        resetHDConfig();
    }*/



    public String getPlatform() {
        return Platform;
    }

    public XmlIntDAO getCONFIG_RES() {
        if (CONFIG_RES == null) {
            CONFIG_RES = new XmlIntDAO(reader.parse(getXmlFileHandle(0)));
        }
        return CONFIG_RES;
    }

    public XmlIntDAO getCONFIG_MOD() {
        /*XmlReader.Element modEs =null; FileHandle sFile,tFile;
        tFile= Gdx.files.local("config_mod.xml");
        if(!tFile.exists()) {
            tFile = Gdx.files.internal("config/config_mod.xml");
        }
        modEs =reader.parse(tFile);
        if(modEs!=null){
            return modEs;
        }*/
        if(CONFIG_MOD==null){
            resetModConfig();
        }
       return CONFIG_MOD;
    }




    /*public boolean getIfAnimation(){
        //Gdx.app.log("ifHD",ifHD+"");
        return ifHD;
    }*/

    public XmlIntDAO getCONFIG_LAYOUT() {
        if (CONFIG_LAYOUT == null) {
            CONFIG_LAYOUT = new XmlIntDAO(reader.parse(getXmlFileHandle(1)));
        }

        return CONFIG_LAYOUT;
    }

    public XmlIntDAO getDEF_AGE() {
        if (DEF_AGE == null) {
            DEF_AGE = new XmlIntDAO(reader.parse(getXmlFileHandle(2)));
        }

        return DEF_AGE;
    }

    public XmlIntDAO getDEF_AREA() {
        if (DEF_AREA == null) {
            DEF_AREA = new XmlIntDAO(reader.parse(getXmlFileHandle(3)));
        }

        return DEF_AREA;
    }

    public XmlIntDAO getDEF_ARMY() {
        if (DEF_ARMY == null) {
            DEF_ARMY = new XmlIntDAO(reader.parse(getXmlFileHandle(4)));
        }

        return DEF_ARMY;
    }

   public XmlStringDAO getDEF_ARRAY() {
        if (DEF_ARRAY == null) {
            DEF_ARRAY = new XmlStringDAO(reader.parse(getXmlFileHandle(5)));
        }

        return DEF_ARRAY;
    }
    public XmlIntDAO getCONFIG_LANGUAGE() {
        if (CONFIG_LANGUAGE == null) {
            CONFIG_LANGUAGE = new XmlIntDAO(reader.parse(getXmlFileHandle(31)));
        }
        return CONFIG_LANGUAGE;
    }

    public XmlIntDAO getDEF_BATTLE() {
        if (DEF_BATTLE == null) {
            DEF_BATTLE = new XmlIntDAO(reader.parse(getXmlFileHandle(6)));
        }
        return DEF_BATTLE;
    }

    public XmlIntDAO getDEF_SPIRIT() {
        if (DEF_SPIRIT == null) {
            DEF_SPIRIT = new XmlIntDAO(reader.parse(getXmlFileHandle(7)));
        }

        return DEF_SPIRIT;
    }

    public XmlIntDAO getDEF_CARD() {
        if (DEF_CARD == null) {
            DEF_CARD = new XmlIntDAO(reader.parse(getXmlFileHandle(8)));
        }

        return DEF_CARD;
    }

    public XmlIntDAO getDEF_COUNTRY() {
        if (DEF_COUNTRY == null) {
            DEF_COUNTRY = new XmlIntDAO(reader.parse(getXmlFileHandle(9)));
        }

        return DEF_COUNTRY;
    }

    public XmlIntDAO getDEF_HEVENT() {
        if (DEF_HEVENT == null) {
            DEF_HEVENT = new XmlIntDAO(reader.parse(getXmlFileHandle(10)));
        }

        return DEF_HEVENT;
    }

    public XmlIntDAO getDEF_FACILITY() {
        if (DEF_FACILITY == null) {
            DEF_FACILITY = new XmlIntDAO(reader.parse(getXmlFileHandle(11)));
        }

        return DEF_FACILITY;
    }

    public XmlIntDAO getDEF_GENERAL() {
        if (DEF_GENERAL == null) {
            DEF_GENERAL = new XmlIntDAO(reader.parse(getXmlFileHandle(12)));
        }

        return DEF_GENERAL;
    }

    public XmlIntDAO getDEF_HISTORY() {
        if (DEF_HISTORY == null) {
            DEF_HISTORY = new XmlIntDAO(reader.parse(getXmlFileHandle(14)));
        }

        return DEF_HISTORY;
    }

    public XmlIntDAO getDEF_MAP() {
        if (DEF_MAP == null) {
            DEF_MAP = new XmlIntDAO(reader.parse(getXmlFileHandle(15)));
        }
        return DEF_MAP;
    }


    public XmlIntDAO getDEF_SMALLMAP() {
        if (DEF_SMALLMAP == null) {
            DEF_SMALLMAP = new XmlIntDAO(reader.parse(getXmlFileHandle(16)));
        }

        return DEF_SMALLMAP;
    }

    public XmlIntDAO getDEF_STAGE() {
        if (DEF_STAGE == null) {
            DEF_STAGE = new XmlIntDAO(reader.parse(getXmlFileHandle(17)));
        }

        return DEF_STAGE;
    }

    public XmlIntDAO getDEF_TERRAIN() {
        if (DEF_TERRAIN == null) {
            DEF_TERRAIN = new XmlIntDAO(reader.parse(getXmlFileHandle(18)));
        }

        return DEF_TERRAIN;
    }

    public XmlIntDAO getDEF_TERRAINIMG() {
        if (DEF_TERRAINIMG == null) {
            DEF_TERRAINIMG = new XmlIntDAO(reader.parse(getXmlFileHandle(19)));
        }

        return DEF_TERRAINIMG;
    }

    public XmlReader.Element getDEF_TERRAINIMG_XMLE(int id,int idx) {
        if (DEF_TERRAINIMG == null) {
            DEF_TERRAINIMG = new XmlIntDAO(reader.parse(getXmlFileHandle(19)));
        }
        for(int i=0,iMax=DEF_TERRAINIMG.e.getChildCount();i<iMax;i++){
            XmlReader.Element xmlE=DEF_TERRAINIMG.e.getChild(i);
            if(xmlE.getInt("id")==id&&xmlE.getInt("idx")==idx){
                return xmlE;
            }
        }


        return null;
    }



    public XmlIntDAO getDEF_WEAPON() {
        if (DEF_WEAPON == null) {
            DEF_WEAPON = new XmlIntDAO(reader.parse(getXmlFileHandle(20)));
        }

        return DEF_WEAPON;
    }

    public XmlIntDAO getDEF_WEATHER() {
        if (DEF_WEATHER == null) {
            DEF_WEATHER = new XmlIntDAO(reader.parse(getXmlFileHandle(21)));
        }
        return DEF_WEATHER;
    }

    public XmlIntDAO getDEF_RDIALOGUE() {
        if (DEF_RDIALOGUE == null) {
            DEF_RDIALOGUE = new XmlIntDAO(reader.parse(getXmlFileHandle(22)));
        }
        return DEF_RDIALOGUE;
    }


    public XmlStringDAO getDEF_MODEL() {
        if (DEF_MODEL == null) {
            DEF_MODEL = new XmlStringDAO(reader.parse(getXmlFileHandle(23)));
        }

        return DEF_MODEL;
    }

    public XmlIntDAO getDEF_REVENT() {
        if (DEF_REVENT == null) {
            DEF_REVENT = new XmlIntDAO(reader.parse(getXmlFileHandle(24)));
        }
        return DEF_REVENT;
    }

    public XmlIntDAO getDEF_LEGIONFEATURE() {
        if (DEF_LEGIONFEATURE == null) {
            DEF_LEGIONFEATURE = new XmlIntDAO(reader.parse(getXmlFileHandle(25)));
        }
        return DEF_LEGIONFEATURE;
    }


    public XmlIntDAO getDEF_CZONE() {
        if (DEF_CZONE == null) {
            DEF_CZONE = new XmlIntDAO(reader.parse(getXmlFileHandle(26)));
        }

        return DEF_CZONE;
    }

    public XmlIntDAO getDEF_SKILL() {
        if (DEF_SKILL == null) {
            DEF_SKILL = new XmlIntDAO(reader.parse(getXmlFileHandle(27)));
        }
        return DEF_SKILL;
    }

    public XmlIntDAO getDEF_ANIMATION() {
        if (DEF_ANIMATION == null) {
            DEF_ANIMATION = new XmlIntDAO(reader.parse(getXmlFileHandle(28)));
        }
        return DEF_ANIMATION;
    }



    public XmlIntDAO getDEF_RTASK() {
        if (DEF_RTASK == null) {
            DEF_RTASK = new XmlIntDAO(reader.parse(getXmlFileHandle(29)));
        }
        return DEF_RTASK;
    }
    public XmlIntDAO getDEF_EMPIRE() {
        if (DEF_EMPIRE == null) {
            DEF_EMPIRE = new XmlIntDAO(reader.parse(getXmlFileHandle(30)));
        }
        return DEF_EMPIRE;
    }

    public XmlIntDAO getDEF_SAGA() {
        if (DEF_SAGA == null) {
            DEF_SAGA = new XmlIntDAO(reader.parse(getXmlFileHandle(32)));
        }
        return DEF_SAGA;
    }

    public XmlIntDAO getDEF_SAGASTAGE() {
        if (DEF_SAGASTAGE == null) {
            DEF_SAGASTAGE = new XmlIntDAO(reader.parse(getXmlFileHandle(33)));
        }
        return DEF_SAGASTAGE;
    }

    public XmlIntDAO getDEF_VIEW() {
        if (DEF_VIEW == null) {
            DEF_VIEW = new XmlIntDAO(reader.parse(getXmlFileHandle(34)));
        }
        return DEF_VIEW;
    }


    public XmlIntDAO getDEF_UNITFEATURE() {
        if (DEF_UNITFEATURE == null) {
            DEF_UNITFEATURE = new XmlIntDAO(reader.parse(getXmlFileHandle(35)));
        }
        return DEF_UNITFEATURE;
    }

    public XmlIntDAO getDEF_LEGIONPOLICY() {
        if (DEF_LEGIONPOLICY == null) {
            DEF_LEGIONPOLICY = new XmlIntDAO(reader.parse(getXmlFileHandle(36)));
        }
        return DEF_LEGIONPOLICY;
    }
    public XmlIntDAO getDEF_DIPLOMATICATTITUDE() {
        if (DEF_DIPLOMATICATTITUDE == null) {
            DEF_DIPLOMATICATTITUDE = new XmlIntDAO(reader.parse(getXmlFileHandle(37)));
        }
        return DEF_DIPLOMATICATTITUDE;
    }


    public XmlIntDAO getDEF_WONDER() {
        if (DEF_WONDER == null) {
            DEF_WONDER = new XmlIntDAO(reader.parse(getXmlFileHandle(38)));
        }
        return DEF_WONDER;
    }

    //DEF_STRATEGICREGION
    public XmlIntDAO getDEF_STRATEGICREGION() {
        if (DEF_STRATEGICREGION == null) {
            DEF_STRATEGICREGION = new XmlIntDAO(reader.parse(getXmlFileHandle(39)));
        }
        return DEF_STRATEGICREGION;
    }

    public XmlIntDAO getDEF_SHIELDIMAGE(){
        if (DEF_SHIELDIMAGE == null) {
            DEF_SHIELDIMAGE = new XmlIntDAO(reader.parse(getXmlFileHandle(40)));
        }
        return DEF_SHIELDIMAGE;
    }
    public XmlIntDAO getDEF_SHIELDSTR(){
        if (DEF_SHIELDIMAGE == null) {
            DEF_SHIELDIMAGE = new XmlIntDAO(reader.parse(getXmlFileHandle(41)));
        }
        return DEF_SHIELDIMAGE;
    }
    public XmlIntDAO getCONFIG_MUSIC(){
        if (CONFIG_MUSIC == null) {
            CONFIG_MUSIC = new XmlIntDAO(reader.parse(getXmlFileHandle(42)));
        }
        return CONFIG_MUSIC;
    }
    public XmlIntDAO getDEF_REPORT(){
        if (DEF_REPORT == null) {
            DEF_REPORT = new XmlIntDAO(reader.parse(getXmlFileHandle(48)));
        }
        return DEF_REPORT;
    }


    public XmlIntDAO getCONFIG_RULE(){
        if (CONFIG_RULE == null) {
            CONFIG_RULE = new XmlIntDAO(reader.parse(getXmlFileHandle(43)));
        }
        return CONFIG_RULE;
    }

    public XmlIntDAO getCONFIG_CHIEFBUFF(){
        if (CONFIG_CHIEFBUFF == null) {
            CONFIG_CHIEFBUFF = new XmlIntDAO(reader.parse(getXmlFileHandle(44)));
        }
        return CONFIG_CHIEFBUFF;
    }

    public XmlIntDAO getCONFIG_CHIEFCONFIG(int stageId){
        if (CONFIG_CHIEFCONFIG == null||CONFIG_CHIEFCONFIG.e.getInt("id",-1)==stageId) {
            CONFIG_CHIEFCONFIG = new XmlIntDAO(getXmlEById(reader.parse(getXmlFileHandle(45)),stageId));
        }
        return CONFIG_CHIEFCONFIG;
    }

    private XmlReader.Element getXmlEById(XmlReader.Element parse, int id) {
        for(int i=0;i<parse.getChildCount();i++){
            XmlReader.Element xmlE=parse.getChild(i);
            if(xmlE.getInt("id",-1)==id){
                return xmlE;
            }
        }
        return parse;
    }

    public XmlStringDAO getCONFIG_SOUND() {
        if (CONFIG_SOUND == null) {
            CONFIG_SOUND = new XmlStringDAO(reader.parse(getXmlFileHandle(46)));
        }
        return CONFIG_SOUND;
    }
    public XmlStringDAO getDEF_EFFECT() {
        if (DEF_EFFECT == null) {
            DEF_EFFECT = new XmlStringDAO(reader.parse(getXmlFileHandle(47)));
        }
        return DEF_EFFECT;
    }

    public List<Color> getCOUNTRY_COLORS() {
        if (COUNTRY_COLORS == null) {
            COUNTRY_COLORS = DefDAO.getAllCountryColorByXml(getDEF_COUNTRY());
        }
        return COUNTRY_COLORS;
    }

    public ObjectMap<String,String> getTERRAINIMG_MAP(boolean sort) {
        if (TERRAINIMG_MAP == null) {
            TERRAINIMG_MAP = DefDAO.getTerrainimgMap(getDEF_TERRAINIMG(),sort);
        }
        return TERRAINIMG_MAP;
    }

    public XmlStringDAO getRULE_MAPBIN() {
        if (RULE_MAPBIN == null) {
            RULE_MAPBIN = new XmlStringDAO(reader.parse(Gdx.files.internal("rule/rule_fb2_map.xml")));
        }
        return RULE_MAPBIN;
    }

    public void setIfDrawArmyMark(float zoom){
        if(/*getIfAnimation()&&*/ zoom<0.85f){
           ifDrawArmyMark=false;
        }else {
            ifDrawArmyMark=true;
        }
    }


    public void setLanguage(int value) {
        playerConfig.putInteger("language",value);
        playerConfig.flush();
    }

    public int getLangueageId(){
        return playerConfig.getInteger("language",2);
    }

    public XmlIntDAO getDEF_RGENERAL() {
        if (DEF_RGENERAL == null) {
            DEF_RGENERAL = new XmlIntDAO(reader.parse(getXmlFileHandle(13)));
        }
        return DEF_RGENERAL;
    }

    public void setDEF_RGENERAL(XmlIntDAO DEF_RGENERAL) {
        this.DEF_RGENERAL = DEF_RGENERAL;
    }

    //检查是否有mod文件,如果没有,则复制过去  oPath
    public void checkModFile(String oPath,String nPath) {
        FileHandle nfile = Gdx.files.local(nPath);
        if(!nfile.exists()) {
            Gdx.files.internal(oPath).copyTo( nfile);
        }

    }

    //通过res文件的name获得路径
    public String getFileNameForPath(int resId,String fileName) {
        XmlReader.Element e=getCONFIG_RES().getElementById(resId);
        for(int i=0,iMax=e.getChildCount();i<iMax;i++){
            XmlReader.Element e1=  e.getChild(i);
            if(e1.get("name").equals(fileName)){
                if(e1.getBoolean("ifOriginal")){
                    return e1.get("res");
                }else{
                    return getModPath()+e1.get("res");
                }
            }
        }
        return null;
    }
    public String getLangeuageMark(){
        String rs=lm.getLanguageLocal().toString().toLowerCase();
        return rs;
    }

    public void setIfColor() {
        if(ifColor){
            ifColor =false;
        }else{
            ifColor =true;
        }
        playerConfig.putBoolean("ifColor", ifColor);
        playerConfig.flush();
    }



    public void setIfEffect() {
        if(ifEffect){
            ifEffect=false;
        }else{
            ifEffect=true;
        }
        playerConfig.putBoolean("ifEffect",ifEffect);
        playerConfig.flush();
    }


    public void setIfAutoSave() {
        if(ifAutoSave){
            ifAutoSave=false;
        }else{
            ifAutoSave=true;
        }
        playerConfig.putBoolean("ifAutoSave",ifAutoSave);
        playerConfig.flush();
    }
    public void setIfAiActAndNextRound() {
        if(ifAiActAndNextRound){
            ifAiActAndNextRound=false;
        }else{
            ifAiActAndNextRound=true;
        }
        playerConfig.putBoolean("ifAiActAndNextRound",ifAiActAndNextRound);
        playerConfig.flush();
    }

    public void setIfPromptUnitUpd() {
        if(ifPromptUnitUpd){
            ifPromptUnitUpd=false;
        }else{
            ifPromptUnitUpd=true;
        }
        playerConfig.putBoolean("ifPromptUnitUpd",ifPromptUnitUpd);
        playerConfig.flush();
    }


    public void setLeisureMode() {
        if(ifLeisureMode||game.resGameConfig.setLeisureFalse){
            ifLeisureMode=false;
        }else{
            ifLeisureMode=true;
        }
        playerConfig.putBoolean("ifLeisureMode",ifLeisureMode);
        playerConfig.flush();
    }
    public void setIfIgnoreBug(){
        if(ifIgnoreBug){
            ifIgnoreBug=false;
        }else{
            ifIgnoreBug=true;
        }
        playerConfig.putBoolean("ifIgnoreBug",ifIgnoreBug);
        playerConfig.flush();
    }

    public boolean getIfIgnoreBug(){
        return ifIgnoreBug;
    }


    public boolean getIfPromptUnitUpd() {return ifPromptUnitUpd;}
    public boolean getIfAiActAndNextRoundfAutoSave() {return ifAiActAndNextRound;}

    public boolean getIfAutoSave() {return ifAutoSave;}
    public boolean getIfColor() {
        return ifColor;
    }


    public void setVSync() {
        boolean ifVSync= playerConfig.getBoolean("ifVSync",false);
        playerConfig.putBoolean("ifVSync",!ifVSync);
        playerConfig.flush();
    }
    public boolean getVSync(){
        return playerConfig.getBoolean("ifVSync",false);
    }


    public void setShieldVistable() {
        ifShield=!playerConfig.getBoolean("ifShield",false);
        playerConfig.putBoolean("ifShield",ifShield);
        playerConfig.flush();
    }

    public void initCountryColor() {
    }

    public FileHandle getConfigProperties(boolean ifSource) {
        if(ifSource||getModId()==0){
            return Gdx.files.internal(ResDefaultConfig.Path.GameConfigPropertiesPath);
        }else {
            FileHandle  fileHandle= Gdx.files.local(getModPath()+ResDefaultConfig.Path.GameConfigPropertiesPath);
            if(!fileHandle.exists()){
                Gdx.files.internal(ResDefaultConfig.Path.GameConfigPropertiesPath).copyTo(fileHandle);
            }
            return fileHandle;
        }
    }

    public ObjectMap<String,String> getShieldImageTransMap() {
        XmlReader.Element xEs=getDEF_SHIELDIMAGE().getElementById(getLangueageId());
        if(xEs!=null&&xEs.getChildCount()>0){
            ObjectMap<String,String> rs=new ObjectMap<>();
            for(int i=0,iMax=xEs.getChildCount();i<iMax;i++){
                XmlReader.Element xE=xEs.getChild(i);
                rs.put(xE.get("name",""),xE.get("shieldImage",""));
            }
            return rs;
        }
        return null;
    }
    public ObjectMap<String,String> getShieldStrTransMap() {
        XmlReader.Element xEs=getDEF_SHIELDSTR().getElementById(getLangueageId());
        if(xEs!=null&&xEs.getChildCount()>0){
            ObjectMap<String,String> rs=new ObjectMap<>();
            for(int i=0,iMax=xEs.getChildCount();i<iMax;i++){
                XmlReader.Element xE=xEs.getChild(i);
                rs.put(xE.get("name",""),xE.get("shieldStr",""));
            }
            return rs;
        }
        return null;
    }

    public boolean ifCanUseInLanguage(String useLanguage) {
        if(useLanguage.equals("-1")||ComUtil.ifHaveValueInStr(useLanguage, getLangueageId())){
            return true;
        }
        return false;
    }

    public int geRandomtMusicId(int type,int lastId) {
        Array<XmlReader.Element> xmlEs=getCONFIG_MUSIC().e.getChildrenByName("music");
        int musicId=lastId;
        IntArray rs=game.tempUtil.getTempIntArray();
        for(int i=0;i<xmlEs.size;i++){
            XmlReader.Element e=xmlEs.get(i);
            int id=e.getInt("id");
            if(e.getInt("type",-1)==type&&id!=lastId){
                rs.add(id);
            }
        }
        if(rs.size>0){
            musicId=rs.random();
            game.tempUtil.disposeTempIntArray(rs);
        }
        Gdx.app.log("geRandomtMusicId",lastId+":"+musicId+":"+type);
        return musicId;
    }

    public FileHandle getMusicFile(int musicId){
        XmlReader.Element e=getCONFIG_MUSIC().getElementById(musicId);
        if(e!=null){
            return Gdx.files.internal(e.get("file"));
        }
        return Gdx.files.internal("audio/jokull.mp3");
    }

    public FileHandle getSoundFile(String soundStr){
        XmlReader.Element e=getCONFIG_SOUND().getElementById(soundStr);
        if(e!=null){
            return Gdx.files.internal(e.get("file"));
        }
        return Gdx.files.internal("audio/click.mp3");
    }

    public XmlReader.Element getBMRuleXmlE(int load) {
        int ruleId=load/100;
        XmlReader.Element e=getCONFIG_RULE().getElementById(ruleId);
        if(e!=null){
            XmlReader.Element ruleE=getRuleXmlE1(ruleId);
            ruleId=load%100;
            if(ruleE!=null&&ruleE.getChildCount()>ruleId){
                return ruleE.getChild(ruleId);
            }
        }
        return null;
    }

    private XmlReader.Element getRuleXmlE1(int ruleId) {
        if(ruleId==this.ruleId){
            return ruleE;
        }
        XmlReader.Element e=getCONFIG_RULE().getElementById(ruleId);
        if(e!=null){
            ruleE= reader.parse(Gdx.files.internal(e.get("path")));
        }else{
            ruleE=null;
            this.ruleId=-1;
            Gdx.app.error("getRuleXmlE1 error",ruleId+"");
        }
        return ruleE;
    }

    public XmlReader.Element getChiefBuffXmlE(int chiefType,int chiefId) {
        Array<XmlReader.Element> pEs=game.gameConfig.getCONFIG_CHIEFBUFF().e.getChildrenByName("chiefBuff");
        for(int i=0;i<pEs.size;i++){
            XmlReader.Element pE=pEs.get(i);
            if(pE.getInt("type",0)==chiefType&&pE.getInt("index")==chiefId){
                return pE;
            }
        }
        return null;
    }
    //isNoFont 是否是前排
    public XmlReader.Element getArmyModel(int age,int year,int armyType,int armyId, int countryId,boolean isFont) {
      Array<XmlReader.Element> xmlEs= getDEF_MODEL().e.getChildrenByName("model");
      /*if(countryId==28){
          int s=0;
      }*/
        if(armyType==4&&year>game.resGameConfig.navyModelChangeYear&&age==0){
            age=1;
        }
        for(int i=0;i<xmlEs.size;i++){
            XmlReader.Element xmlE=xmlEs.get(i);
            int armyAge=xmlE.getInt("age",-1);
           //过滤木帆船
            /*if(armyType==4&&year>=game.resGameConfig.navyModelChangeYear){
                armyAge=1;
            }*/

            if( (age==armyAge||armyAge==-1)&&armyId==xmlE.getInt("armyId",-1)&&(ComUtil.ifHaveValueInStr(xmlE.get("country"),countryId))){
                if(xmlE.getInt("modelType",0)==2){
                    if(isFont){
                        return xmlE;
                    }
                }else {
                    return xmlE;
                }
           }
        }
        //如果是木船
        if(armyType==4&&year<game.resGameConfig.navyModelChangeYear&&age==0){
            return getDEF_MODEL().getElementById(armyId+"_0");
        }else if(getDEF_MODEL().contain(armyId+"_"+age)){
            return getDEF_MODEL().getElementById(armyId+"_"+age);
        }else if(getDEF_MODEL().contain(armyId+"")){
            return getDEF_MODEL().getElementById(armyId+"");
        }else if(getDEF_MODEL().contain(armyId+"_0")){
            return getDEF_MODEL().getElementById(armyId+"_0");
        }
        return null;
    }

    public int getCardType(int mergeId) {
        XmlReader.Element xml=getDEF_CARD().getElementById(mergeId);
        if(xml!=null){
            return xml.getInt("type",0);
        }
        return -1;
    }

    public boolean ifBRecord(String str) {
        return playerConfig.getBoolean(str,false);
    }
    public void addBRecord(String str){
        playerConfig.putBoolean(str, true);
        playerConfig.flush();
    }


    public class LanguageManager {
        private IntMap<I18NBundle> languages;
        private FileHandle  fileHandle;
        private I18NBundle currentLanguage;
        private String currentLanguageStr; //当前语言所有不重复的字符


        public LanguageManager() {
            languages = new IntMap<I18NBundle>();
            /*此处获取的默认语言格式，和后面保存的语言格式不统一，在正式使用时会做一个转换
             * 或者语言和I18NBundle对象不保存在ObjectMap里面，用别的数据结构，此处不考虑这种情况
             * */
            int languageId= playerConfig.getInteger("language",2);
            if(getModId()==0){
                fileHandle= Gdx.files.internal("str/stringtable");
            }else {
                fileHandle= Gdx.files.local(getModPath()+"str/stringtable");
                /*if(!fileHandle.exists()){
                    Gdx.files.internal("str/stringtable").copyTo(fileHandle);
                }*/
                FileHandle tempFile= GameUtil.toFileHandle(fileHandle, getLanguageLocal(languageId));
                if(!tempFile.exists()){
                    GameUtil.toFileHandle(Gdx.files.internal("str/stringtable"), getLanguageLocal(languageId)).copyTo(tempFile);
                }
            }
             //语言 1英文2中3繁体

            //loadLanguage(1, GameUtil.toFileHandle(fileHandle, getLanguageLocal(1))); // 加载文字文件

           // loadLanguage(1, fileHandle, getLanguageLocal(1));
            loadLanguage(languageId, fileHandle, getLanguageLocal(languageId));
            currentLanguage=languages.get(languageId);
            currentLanguageStr=getLanguageStr();

        }

        private String getLanguageStr() {
            XmlIntDAO xmlE=getDEF_SHIELDSTR();
            StringBuilder stringBuilder=new StringBuilder();
            if(xmlE!=null&&xmlE.contain(getLangueageId())){
                stringBuilder.append(xmlE.getElementById(getLangueageId()));
            }

            FileHandle file;
            //字库
            if(getModId()==0){
                file= Gdx.files.internal("str/stringtable_"+ getLanguageLocal()+".properties");
            }else {
                file= Gdx.files.local(getModPath()+"str/stringtable_"+ getLanguageLocal()+".properties");
            }
            if(stringBuilder.length()>0){
                if(file.exists()){
                    return ComUtil.removeRepeatChar(DEFAULT_CHARS+file.readString(ResDefaultConfig.charset)+ ResDefaultConfig.nowVersioninState+ ResDefaultConfig.lastVersioninState+stringBuilder.toString());
                }else{
                    return ComUtil.removeRepeatChar(DEFAULT_CHARS+ ResDefaultConfig.nowVersioninState+ ResDefaultConfig.lastVersioninState+stringBuilder.toString());
                }
            }else {
                if(file.exists()){
                    return ComUtil.removeRepeatChar(DEFAULT_CHARS+file.readString(ResDefaultConfig.charset)+ ResDefaultConfig.nowVersioninState+ ResDefaultConfig.lastVersioninState);
                }else{
                    return ComUtil.removeRepeatChar(DEFAULT_CHARS+ ResDefaultConfig.nowVersioninState+ ResDefaultConfig.lastVersioninState);
                }
            }

        }

        public Locale getLanguageLocal(){
            int id= getLangueageId();
            XmlReader.Element xmlE=getCONFIG_LANGUAGE().getElementById(id);
            if(xmlE!=null){
                return new Locale(xmlE.get("localMark1","zh"),xmlE.get("localMark2","CN"));
            }else {
                switch (id){
                    case 1:return Locale.UK;
                    case 2:return Locale.SIMPLIFIED_CHINESE;
                    case 3:return Locale.TRADITIONAL_CHINESE;
                    case 4:return Locale.JAPANESE;
                    case 5:return Locale.KOREAN;
                }
            }
            return Locale.CHINA;
        }

        public Locale getLanguageLocal(int id){
            switch (id){
                case 1:return Locale.UK;
                case 2:return Locale.SIMPLIFIED_CHINESE;
                case 3:return Locale.TRADITIONAL_CHINESE;
                case 4:return Locale.JAPANESE;
                case 5:return Locale.KOREAN;
            }
            return Locale.CHINA;
        }

        public void resetLanguage(){
            int id= playerConfig.getInteger("language",2);
            currentLanguage=languages.get(id);
            loadGameFont();
           // currentLanguage=languages.get(config.getInteger("language"));
        }


        /**
         * 储存语言及其对应的I18NBundle对象，方便以后获取
         * @param languageId
         * @param fileHandle  properties文件对象
         * @param locale   国家编码
         */
        public void loadLanguage(int languageId, FileHandle fileHandle, Locale locale) {
            if (languageId!=0 && fileHandle != null && locale != null)
                languages.put(languageId, I18NBundle.createBundle(fileHandle, locale));
        }

        public void loadLanguage(int id, FileHandle fileHandle) {
            if (id!=0 && fileHandle != null)
                languages.put(id, I18NBundle.createBundle(fileHandle));
        }



        public void removeLanguage(int id, I18NBundle bundle) {
            if (id != 0  && bundle != null)
                languages.remove(id);
        }


        public I18NBundle getBundle() {
            if(lm.currentLanguage==null){
                lm.resetLanguage();
            }
            return currentLanguage;
        }




        public void dispose() {
            Iterator<IntMap.Entry<I18NBundle>> it = languages.iterator();
            while (it.hasNext()) {
                IntMap.Entry<I18NBundle> c = it.next();
                if (c.value != null) {
                    c = null;
                }
            }
        }


    }



    public void dispose(){
        if(gameFont !=null){
            gameFont.dispose();
            gameFont =null;
        }
        lm.dispose();

    }

    public XmlReader.Element getEmpireXmlE(int empireId,int stageId){
        XmlReader.Element empireE= getDEF_EMPIRE().getElementById(empireId);
       if(empireE==null){
           return null;
       }
        Array<XmlReader.Element> stageEs= empireE.getChildrenByName("stage");
        XmlReader.Element e=null;
        for(int i=0,iMax=stageEs.size;i<iMax;i++){
            e=stageEs.get(i);
            if(e.getInt("id")==stageId){
                return e;
            }
        }
        return null;
    }

    public Array<XmlReader.Element> getSagaXmlEs(int age){
        XmlReader.Element sageE= getDEF_SAGA().getElementById(age);
        if(sageE==null){
            return null;
        }
        Array<XmlReader.Element> stageEs= sageE.getChildrenByName("saga");
        return stageEs;
    }

    public Array<XmlReader.Element> getSagaStageXmlEs(int age,int sagaId){
        sagaId=1000*age+sagaId;
        XmlReader.Element sageE= getDEF_SAGASTAGE().getElementById(sagaId);
        if(sageE==null){
            return null;
        }
        Array<XmlReader.Element> stageEs= sageE.getChildrenByName("stage");
        sortXmlEs(stageEs);
        return stageEs;
    }

    //依据xmlEs对其排序
    public  void sortXmlEs(Array<XmlReader.Element> xmlEs){
        xmlEs.sort(new Comparator<XmlReader.Element>() {
            @Override
            public int compare(XmlReader.Element o1, XmlReader.Element o2) {
                int i1 = o1.getInt("sort",9999);
                int i2 = o2.getInt("sort",9999);
                return i1 - i2;
            }
        });
    }




    //获得玩家爵位  =8个帝国通关模式*2-1
    //0~15
    //TODO
    public int getPlayerRank(){
        int score=0;
        int scoreSum=0;
        for(int i=101;i<=108;i++){
            score=game.gameMethod.getEmpireScore(i)/5*2-1;
            if(score>0){
                scoreSum=score+scoreSum;
            }
        }
        if(scoreSum>15){scoreSum=15;}
        return scoreSum;
    }

    //TODO
    public int getPlayerModel(){



        return 9999;
    }

    //TODO
    public boolean costPlayerModel(int v){

        return true;
    }


    //TODO
    public int getVip() {
        return playerConfig.getInteger("vip",3);
    }


    //TODO 对接系统按语言分层
    //返回更新说明
    public String getMainInfoStr() {
        String rs=  game.gameMethod.getStrValue("prompt_main_info_1")+ ResDefaultConfig.version
                +"\n"+game.gameMethod.getStrValue("prompt_main_info_2")+ ResDefaultConfig.effectiveDate;
        rs+="\n"+game.gameMethod.getStrValue("prompt_main_info_3")+(int)game.getWorldWidth()+"*"+(int)game.getWorldHeight();
        if(ifLeisureMode){
            rs+="\n\n"+game.gameMethod.getStrValue("prompt_main_info_4");
        }
        if(ifShield&&!ComUtil.isEmpty(shieldCountrys)&&!ComUtil.isEmpty(invincibleCountrys)){
            rs+="\n\n"+game.gameMethod.getStrValue("prompt_main_info_5",game.gameMethod.getCountryStrsByCountry(shieldCountrys,999),game.gameMethod.getCountryStrsByCountry(invincibleCountrys,999));
        }
        rs+="\n\n"+game.gameMethod.getStrValue("prompt_main_info_6");//bug提取和反馈
        if(getLangueageId()==2){
            rs+="\n"+game.gameMethod.getStrValue("prompt_main_info_7");
            rs+="\n\n"+ ResDefaultConfig.nowVersioninState+"\n\n"+game.gameMethod.getStrValue("prompt_main_info_8");
            rs+="\n\n"+ResDefaultConfig.lastVersioninState;
        }
        return rs;
    }





    public void checkGeneralImage() {
        Array<XmlReader.Element> gEs=getDEF_GENERAL().e.getChildrenByName("general");
        for(int i=0;i<gEs.size;i++){
            XmlReader.Element gE=gEs.get(i);
            String imgName=gE.get("image");
            FileHandle f=Gdx.files.internal(ResDefaultConfig.Path.GeneralFolderPath+imgName+".tata");
            if(!f.exists()){
                Gdx.app.error("checkGeneralImage Error1",imgName);
            }else {
                TextureRegionDAO t=game.getImgLists().getTextureByName("sg_"+imgName);
                if(t==null){
                    Gdx.app.error("checkGeneralImage Error2",imgName);
                }
            }
        }
    }

    public XmlReader.Element getDefaultWindowE(int windowIdId){
        XmlReader.Element xmlE=  getCONFIG_LAYOUT().getElementById(-1);
        Array<XmlReader.Element> windowEs=xmlE.getChildrenByName("window");
        for(int i=0;i<windowEs.size;i++){
            XmlReader.Element windowE=windowEs.get(i);
            if(windowE.getInt("id",-1)==windowIdId){
                return windowE;
            }
        }
        return null;
    }


    public void testMethod_logXmlE(){
        XmlReader.Element xmlEs=getDEF_MODEL().e;
        System.out.println(xmlEs.toString());
        System.out.println("---------------------------------------------------------------");
        for(int i=0;i<xmlEs.getChildCount();i++){
            XmlReader.Element xmlE=xmlEs.getChild(i);
            xmlE.setAttribute("x",xmlE.getInt("x",0)/2+"");
            xmlE.setAttribute("y",xmlE.getInt("y",0)/2+"");
            xmlE.setAttribute("bgx",xmlE.getInt("bgx",0)/2+"");
            xmlE.setAttribute("bgy",xmlE.getInt("bgy",0)/2+"");
            for(int j=0;j<xmlE.getChildCount();j++){
                XmlReader.Element xmlCE=xmlE.getChild(j);
                xmlCE.setAttribute("x",xmlCE.getInt("x",0)/2+"");
                xmlCE.setAttribute("y",xmlCE.getInt("y",0)/2+"");
                xmlCE.setAttribute("bgx",xmlCE.getInt("bgx",0)/2+"");
                xmlCE.setAttribute("bgy",xmlCE.getInt("bgy",0)/2+"");
                for(int k=0;k<xmlCE.getChildCount();k++){
                    XmlReader.Element xmlCE1=xmlCE.getChild(k);
                    xmlCE1.setAttribute("x",xmlCE1.getInt("x",0)/2+"");
                    xmlCE1.setAttribute("y",xmlCE1.getInt("y",0)/2+"");
                    xmlCE1.setAttribute("bgx",xmlCE1.getInt("bgx",0)/2+"");
                    xmlCE1.setAttribute("bgy",xmlCE1.getInt("bgy",0)/2+"");
                }
            }
        }
        System.out.println(xmlEs.toString());
    }

    public void clearFirstPrompt(){
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenPause, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenSpirit, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenLegionFeature, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenDailyTask, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenResolution, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenConquestTask, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenRegionInfo, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstSelectCountry, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstMainAndMineralUse, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstMainAndOilUse, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstMainAndAmmoUse, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstMainAndEnergyUse, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenWonder, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenDiplomacy, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstUpdTech, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenLegionTech, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstOpenUnitInfo, false);
        playerConfig.putBoolean(ResDefaultConfig.StringName.firstMergeUnit, false);
        playerConfig.flush();
    }
}
