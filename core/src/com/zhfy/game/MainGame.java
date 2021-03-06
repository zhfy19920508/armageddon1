package com.zhfy.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.config.ResGameConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameConfig;
import com.zhfy.game.framework.GameFramework;
import com.zhfy.game.framework.GameLayout;
import com.zhfy.game.framework.GameMethod;
import com.zhfy.game.framework.GameNet;
import com.zhfy.game.framework.TempUtil;
import com.zhfy.game.framework.tool.CHAsyncManager;
import com.zhfy.game.framework.tool.CHAsyncTask;
import com.zhfy.game.framework.tool.EncryptTextureLoader;
import com.zhfy.game.framework.tool.GifAnimationLoader;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2History;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.AnimationListDAO;
import com.zhfy.game.model.framework.TextureRegionListDAO;
import com.zhfy.game.screen.SMapScreen;
import com.zhfy.game.screen.GeneralScreen;
import com.zhfy.game.screen.LoadingScreen;
import com.zhfy.game.screen.MapDetailScreen;
import com.zhfy.game.screen.StartScreen;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.GameUtil;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class MainGame  extends Game   {

    // ?????????????????????????????? 1024 * 768, ??????????????????????????????StretchViewport???
    /*public  final float WORLD_WIDTH = 1024;
    public  final float WORLD_HEIGHT = 768;*/
	
    /** ???????????? */
    private float worldWidth;
    /** ???????????? */
    private float worldHeight;

;

    /** ??????????????? */
    private AssetManager assetManager;

    //private GameData gameData;
    private TextureRegionListDAO imgLists;
    private AnimationListDAO animationLists;
    
    private StartScreen startScreen;
    public LoadingScreen loadingScreen;
    public MapDetailScreen mapDetailScreen;
    public SMapScreen sMapScreen;
    public GeneralScreen generalScreen;
    private Fb2Smap sMapDAO;
    public Fb2Map defaultMapBinDAO;//???????????????bin ???resGameConfig ?????? defaultMapBinId


    public  BitmapFont defaultFont;//????????????
    public int musicVoice;
    public int soundVoice;
    public int moveSpeed;
    public TempUtil tempUtil;
    public boolean saveOk;

    //??????????????????mod???????????????,???????????????????????????
    // ????????????
    private Music music;
    private int musicId;
    private int lastMusicId;

	private int mapId;
    private int stageId;
    private int screenId;
    public String mapFolder;
    public  XmlReader.Element defStage;
    public GameLayout gameLayout;
    public GameConfig gameConfig;
    public int progressbarWidth;
    public GameMethod gameMethod;
    public ResGameConfig resGameConfig;
    public GameNet gameNet ;
    public ObjectMap<String, Sound> soundList;
    public ShapeRenderer shapeRenderer;
    //private LogTime logTime;

    //????????????????????????
    //private   MapBinDAO mapBinDao;


    private GameFramework gameFramework;

    //????????????id??????,?????????????????????????????????
   // public IntArray tempIds;
    public LinkedHashSet<Object> set; //list???????????????????????????
    public ObjectMap<Button, XmlReader.Element>  functionMap;
    public SpriteBatch batch;
    public CHAsyncManager asyncManager;
    public OrderedMap<String, TextureRegion> tempTextureRegions;
    public  DisplayMode[] dms;


    //public IntMap<TextureRegion> flagBgTextureRegions;
    //public IntMap<TextureRegion> eventBgTextureRegions;




    @Override
	public void create () {

        gameConfig=new GameConfig(this);
        /*if(ResDefaultConfig.ifDebug||!gameConfig.ifIgnoreBug){
            //Gdx.app.log("??????", ResDefaultConfig.Task);
            Gdx.app.setLogLevel(Logger.DEBUG);
        }else{
            Gdx.app.setLogLevel(Logger.NONE);
        }*/


        gameFramework=new GameFramework(this);

        if(ComUtil.getTime()>= ResDefaultConfig.effectiveDate){
            Gdx.app.error("??????","??????????????????????????????");
            gameFramework.deleteAllSaveFile();
            return;
        }
        tempUtil=new TempUtil(this);
        batch=new SpriteBatch();
        soundList=new ObjectMap<>();
        shapeRenderer=new ShapeRenderer();
        dms=  Gdx.graphics.getDisplayModes();
        //???????????????????????????
        //assetManager = new AssetManager(new ExtraFileHandleResolver());
        //Gdx.app.log("Gdx.app.getType",Gdx.app.getType().toString());
        /*if(Gdx.app.getType().toString().equals("Android")){
            assetManager=new AssetManager();
        }else{
            assetManager=new AssetManager(new ExtraFileHandleResolver());
        }*/
       // assetManager=new AssetManager(new ExtraFileHandleResolver());
        assetManager=new AssetManager();
        assetManager.setLoader(Texture.class,
                ".tata",
                new EncryptTextureLoader(new InternalFileHandleResolver())
        );
        assetManager.setLoader(Animation.class,
                ".gif",
                new GifAnimationLoader(new InternalFileHandleResolver())
        );
        gameLayout=new GameLayout(this);
        resGameConfig=new ResGameConfig(this,gameConfig.getConfigProperties(true));
        if(gameConfig.getModId()!=0){//????????????
            resGameConfig.setModPro(gameConfig.getConfigProperties(false));
            resGameConfig.setProValue(false);
        }
        gameMethod=new GameMethod(this,gameConfig.getShieldStrTransMap());
        gameNet=new GameNet();
       // gameNet.test();
        //????????????
        assetManager=GameUtil.loadResByConfig(this,assetManager, -1);
        assetManager.finishLoading();
        asyncManager=new CHAsyncManager(this);

        screenId= ResDefaultConfig.Class.StartScreen;
        imgLists=new TextureRegionListDAO(this,gameConfig.getShieldImageTransMap());
        imgLists=GameUtil.loadTextureReigonByScreenId(this, imgLists,screenId,getAssetManager(),false);
        animationLists=new AnimationListDAO(this);

        tempTextureRegions =new OrderedMap<>();
        //flagBgTextureRegions=new IntMap<>();
        //eventBgTextureRegions=new IntMap<>();

        initScreenConfig();

        saveOk=true;

        // ??????????????????
        setStartScreen(new StartScreen(this));
        // ?????????????????????????????????
        setScreen(getStartScreen());


        assetManager=GameUtil.loadResByConfig(this,assetManager, 0);

        //tempIds = new IntArray();
        set=new LinkedHashSet<Object>();
        functionMap=new ObjectMap();//??????????????????screen???function??????,???????????????,???????????????????????????
        defaultFont=new BitmapFont();//????????????
        defaultFont.getData().markupEnabled=true;


         musicVoice=gameConfig.playerConfig.getInteger("music",4);
         soundVoice=gameConfig.playerConfig.getInteger("sound",4);
        moveSpeed=gameConfig.playerConfig.getInteger("speed",4);
        musicId=gameConfig.geRandomtMusicId(0,0);
        music = Gdx.audio.newMusic(gameConfig.getMusicFile(musicId));
        music.setLooping(true);
        music.setVolume(musicVoice*1f/7);
        if(musicVoice==0){
            music.pause();
        }else {
            music.play();
        }

        //Gdx.app.setLogLevel(Application.LOG_DEBUG);
        //gameFramework.testXmlWriter();

        //???????????????
        Gdx.gl20.glLineWidth(3);

      //  XmlReader.Element xE=gameConfig.getDEF_SPIRIT().getElementById(0);
       // Gdx.app.log("test rule",ComUtil.getIntergerValueByRuleStr( xE.get("rule"),xE.getInt("min",0),xE.getInt("max",100),1,1,1)+"");
      //  gameConfig.testMethod_logXmlE();
       /* ShaderProgram shader= ShaderForGray.getShader();

        batch = new SpriteBatch(1000, shader);
        batch.setShader(shader);*/
    }



    /**
     * ??????????????????????????????????????????????????????????????????
     * ???beforeScreenId???-2???,???????????????
     */
  public void showGameScreen(int beforeScreenId,int nextScreenId) {

        asyncManager.logTask();
      //??????????????????????????????????????????
      functionMap.clear();


      screenId=nextScreenId;
      //?????????????????????
      //logTime.reStar();
       boolean needLoad;
       if(beforeScreenId==nextScreenId&&!getAssetManager().update()&&!asyncManager.update()){
           needLoad=true;
       }else{
           needLoad= GameUtil.ifNeedLoad(this,beforeScreenId,nextScreenId);
       }
      //logTime.log("????????????????????????");

      if(beforeScreenId!=nextScreenId&&beforeScreenId==81&&sMapDAO!=null){
          sMapDAO.dispose();
          sMapDAO=null;
      }
      disposeScreen(beforeScreenId);
     //logTime.log("?????????????????????");
        if(needLoad||!asyncManager.update()) {
            loadingScreen = new LoadingScreen(this, assetManager, beforeScreenId, nextScreenId);
            setScreen(loadingScreen);
            //??????????????????????????????sound
            disposeSound();
            //logTime.log("??????????????????");
        }else {
            toScreen(nextScreenId);
            //logTime.log("??????????????????");
        }
        if (getStartScreen() != null) {
            // ?????? StartScreen ????????????????????????????????????, ????????????????????????,
            // ??????????????? SMapScreen ??????????????? StartScreen ??? dispose() ???????????????????????????
            getStartScreen().dispose();

            // ???????????????, ??????????????????, ?????????????????? dispose() ??????
            setStartScreen(null);
        }
    }
    

    
    
    
    public void toScreen(int nextScreenId) {
        screenId=nextScreenId;
        animationLists.clear();

        if(nextScreenId==81){   //????????????????????????????????????
            XmlReader.Element xmlE=gameConfig.getDEF_STAGE().getElementById(stageId);
           int musicId=xmlE.getInt("musicId",0);
            if(musicId==-1){
                int bgmType=gameConfig.getCONFIG_LAYOUT().getElementById(screenId).getInt("bgmType");
                setMusicByType(bgmType);
            }else {
               setMusicById(musicId,true);
            }
        }else {
            int bgmType=gameConfig.getCONFIG_LAYOUT().getElementById(screenId).getInt("bgmType");
            setMusicByType(bgmType);
        }

        //????????????
        imgLists=GameUtil.loadTextureReigonByScreenId(this,imgLists, nextScreenId,getAssetManager(),true);
        if(nextScreenId==71) {
            mapDetailScreen = new MapDetailScreen(this);
            setScreen(mapDetailScreen);
        }else if(nextScreenId==81){
            sMapScreen = new SMapScreen(this);
            setScreen(sMapScreen);
        }else {
            generalScreen= new GeneralScreen(this,nextScreenId);
            setScreen(generalScreen);
        }
        if(loadingScreen!=null){
            loadingScreen.dispose();
            loadingScreen=null;
        }
    }
    private void disposeScreen(int beforeScreenId) {
      //???beforeScreenId???-2???,???????????????
      if(beforeScreenId==-2){
          return;
      }

        if(beforeScreenId==-1){//????????????
            if(sMapDAO!=null){
                    sMapDAO.dispose();
                sMapDAO=null;
            }
            if (getStartScreen() != null) {
                getStartScreen().dispose();
                setStartScreen(null);
            }
            if (mapDetailScreen != null) {
                mapDetailScreen.dispose();
                mapDetailScreen = null;
            }
            if (sMapScreen != null) {
                sMapScreen.dispose();
                sMapScreen = null;
            }
            if (generalScreen != null) {
                generalScreen.dispose();
                generalScreen = null;
            }
        }else if(beforeScreenId==71) {
            if (mapDetailScreen != null) {
                mapDetailScreen.dispose();
                mapDetailScreen=null;
            }
        }else if(beforeScreenId==81) {
            if (sMapScreen != null) {
                sMapScreen.dispose();
                sMapScreen=null;
            }
        }else if(beforeScreenId==0) {
            if (startScreen != null) {
                startScreen.dispose();
                startScreen=null;
            }
        }else {
            if (generalScreen != null) {
                generalScreen.dispose();
                generalScreen=null;
            }else{
                Gdx.app.error("noDisposeScreen","beforeScreenId:"+beforeScreenId);
            }

        }
    }

    @Override
    public void dispose() {
        super.dispose(); // super.dispose() ????????????, ?????????????????????????????????????????????????????? hide ?????????

        batch.dispose();
        //public OrderedMap<String, TextureRegion> tempTextureRegions;
        //public IntMap<TextureRegion> flagBgTextureRegions;
        //public IntMap<TextureRegion> eventBgTextureRegions;

        /*for(TextureRegion r: tempTextureRegions.values()){
            if(r!=null){
                r.getTexture().dispose();
                r=null;
            }
        }*/

        clearAllTempTextureRegions();
        disposeSound();
        // ???????????????????????????
        if(asyncManager!=null){
            asyncManager.clearTask();
            asyncManager.dispose();
            asyncManager=null;
        }
        if (assetManager != null) {
            assetManager.clear();
            assetManager.dispose();
            assetManager=null;
        }
        defaultFont.dispose();
         music.dispose();

        if(sMapDAO!=null){
            sMapDAO.dispose();
            sMapDAO=null;
        }
        if(defaultMapBinDAO!=null){
            defaultMapBinDAO.dispose();
            defaultMapBinDAO=null;
        }
        if(gameConfig!=null){
            gameConfig.dispose();
            gameConfig=null;
        }
        if(shapeRenderer!=null){
            shapeRenderer.dispose();
            shapeRenderer=null;
        }

        // ?????????????????????, ???????????????????????????????????????
        disposeScreen(-1);
    }


    public StartScreen getStartScreen() {
        return startScreen;
    }


    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }


    public float getWorldWidth() {
        return worldWidth;
    }


    public void setWorldWidth(float worldWidth) {
        this.worldWidth = worldWidth;
    }




    public float getWorldHeight() {
        return worldHeight;
    }


    public void setWorldHeight(float worldHeight) {
        this.worldHeight = worldHeight;
    }


    public AssetManager getAssetManager() {
        return assetManager;
    }


    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }




    public int getStageId() {
        return stageId;
    }


    public void setStageId(int stageId) {
        this.stageId = stageId;
        this.defStage=gameConfig.getDEF_STAGE().getElementById(stageId);
    }
    //type 0????????????(??????btl??????????????????btl??????) 1???????????????
    public void loadSMapDAO(final int stagetId,final int loadMode, final int year, final boolean ifNew){
        int historyId= gameConfig.getDEF_STAGE().getElementById( getStageId()).getInt("historyId");
        setMapId( gameConfig.getDEF_HISTORY().getElementById(historyId).getInt("mapId"));
      if(ResDefaultConfig.ifDebug){
          setSMapDAO(stagetId,loadMode,year,ifNew);
      }else{
          //TODO ????????????btl?????????,????????????????????????
          CHAsyncTask task=new CHAsyncTask("loadSMapDAO",999) {
              @Override
              public void onPreExecute() { }
              @Override
              public void onPostExecute(String result) {}
              @Override
              public String doInBackground() {setSMapDAO(stageId,loadMode,year,ifNew);
                  return null;
              }
          };
          asyncManager.loadTask(task);
          asyncManager.update();
      }
    }

    public void restarSmapDAO() {
        if(ResDefaultConfig.ifDebug){
           reLoadSmapDAO();
        }else{
            //TODO ????????????btl?????????,????????????????????????
            CHAsyncTask task=new CHAsyncTask("restarSmapDAO",100) {
                @Override
                public void onPreExecute() {}
                @Override
                public void onPostExecute(String result) {}

                @Override
                public String doInBackground() {
                    reLoadSmapDAO();
                    return null;
                }
            };
            asyncManager.loadTask(task);
            asyncManager.update();
        }
    }

    private void reLoadSmapDAO(){
      int mode=0;
      if(sMapDAO!=null){
          mode=sMapDAO.controlMode;
      }
        XmlReader.Element defStage = gameConfig.getDEF_STAGE().getElementById(stageId);
        StringBuilder path = null;
        Boolean ifGameEpisode = false;
        int freeConquestYear = 0;
        if (stageId == 0) {
            freeConquestYear = gameConfig.playerConfig.getInteger("freeConquestYear",sMapDAO.masterData.getBeginYear());
        }
        if (sMapDAO != null) {
            ifGameEpisode = sMapDAO.masterData.getGameEpisode() != 0;
            if(stageId==0){
                freeConquestYear=sMapDAO.masterData.getBeginYear();
            }
        }

        boolean ifInit = false;
      //  boolean ifSave = false;
        int gameType = defStage.getInt("btlType");
        if (ifGameEpisode||gameType==1) {//??????,??????_e???????????????
            path = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
            path.append(defStage.get("name")).append("_e.btl");
            ifInit = Gdx.files.local(path.toString()).exists();
         //   ifSave = gameConfig.playerInfo.getBoolean(stageId + "_ifSave", false);

        } else {// if (gameConfig.playerInfo.getBoolean(getStageId() + "_ifNew", false))
            path = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
            path.append(defStage.get("name")).append("_n.btl");
            ifInit = Gdx.files.local(path.toString()).exists();
           // ifSave = gameConfig.playerInfo.getBoolean(stageId + "_ifSave", false);
        }
        if(!ifInit/*||!ifSave*/){//?????????

            //???????????? ?????????????????????????????????
            //int year= defStage.getInt("year");
            path = new StringBuilder("btl/");
            path.append(defStage.get("name")).append(".btl");
            boolean ifHaveO = Gdx.files.local(path.toString()).exists();
            boolean ifHaveI = Gdx.files.internal(path.toString()).exists();

            if (ifHaveO) {//
                sMapDAO = new Fb2Smap(this, Gdx.files.local(path.toString()).readBytes(), stageId);
                /*if (gameType ==0) {//???????????????????????????
                    sMapDAO.btlInitForConquest();
                }*///TODO??????????????????
            }else  if(ifHaveI){
                sMapDAO = new Fb2Smap(this, Gdx.files.internal(path.toString()).readBytes(), stageId);
            }else {
                int historyId=defStage.getInt("historyId");
                XmlReader.Element defHistory = gameConfig.getDEF_HISTORY().getElementById(historyId);
            //    Fb2History historyDao = new Fb2History(gameFramework.getBtByName(defHistory.get("name")));
                Fb2History historyDao =gameFramework.getHistory(historyId,freeConquestYear);
                Fb2Map mapBinDAO = gameFramework.getMapDaoByMapId(historyDao.masterData.getMapId());
                if (gameType == 0) {//???????????? 0
                    sMapDAO = new Fb2Smap();

                    sMapDAO.createBtlForFreeConquest(this, historyDao, mapBinDAO, stageId, defStage, defHistory, freeConquestYear);
                    //sMapDAO.createBtlForBlankConquest(this,historyDao, mapBinDAO, stageId,defStage,defHistory,freeConquestYear);
                } else if (gameType == 1) {//????????????
                    sMapDAO = new Fb2Smap();
                    sMapDAO.createBtlForEmpire(this, historyDao, mapBinDAO, stageId, defStage, defHistory);
                }
                historyDao = null;
            }
        }else {//????????????
            sMapDAO = new Fb2Smap(this, Gdx.files.local(path.toString()).readBytes(), stageId);
        }
        sMapDAO.controlMode=mode;
    }





/*public MapBinDAO getMapBinDao() {
        return mapBinDao;
    }

    public void setMapBinDao(MapBinDAO mapBinDao) {
        this.mapBinDao = mapBinDao;
    }
*/

    public int getMapId() {
        return mapId;
    }


    public void setMapId(int mapId) {
        this.mapId = mapId;
        /*if(gameConfig.getIfAnimation()){
             mapFolder= gameConfig.getDEF_MAP().getElementById(mapId).get("name")+"_hd";
        }else {
            mapFolder=gameConfig.getDEF_MAP().getElementById(mapId).get("name")+"_sd";
        }*/
        mapFolder= gameConfig.getDEF_MAP().getElementById(mapId).get("name")+"_hd";

    }


    public GameFramework getGameFramework() {
        return gameFramework;
    }

    public void setGameFramework(GameFramework gameFramework) {
        this.gameFramework = gameFramework;
    }

    public TextureRegionListDAO getImgLists() {
        return imgLists;
    }

    public void setImgLists(TextureRegionListDAO imgLists) {
        this.imgLists = imgLists;
    }

    public int getScreenId(){
      return screenId;
    }

    /*public GameData getGameData() {
        return gameData;
    }
    public void initGameData(){
      if(gameData==null){
          gameData=new GameData();
      }
    }
*/
    //yyyymm

    public int getMusicVoice() {
        return musicVoice;
    }

    public void setMusicVoice(int musicVoice) {
        if(this.musicVoice==0&&musicVoice>0){
            music.play();
        }else if(musicVoice==0){
            music.pause();
        }
        this.musicVoice = ComUtil.limitValue(musicVoice,0,7);
        music.setVolume(this.musicVoice*1f/7);

    }

    public int getSoundVoice() {
        return soundVoice;
    }

    public void setSoundVoice(int soundVoice) {
        this.soundVoice =  ComUtil.limitValue(soundVoice,0,7);
    }


    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed =  ComUtil.limitValue(moveSpeed,0,7);
    }


    public float getMapScale(){
       /* if(gameConfig.getIfAnimation()){
            return ResConfig.Map.MAP_HD_SCALE;
        }else {
            return ResConfig.Map.MAP_SD_SCALE;
        }*/
        return ResDefaultConfig.Map.MAP_SCALE;
    }



    //mode ??????????????????,????????????????????? 0?????? 1????????????
    //freeConquestYear ??????????????????????????????????????????
    //type 0?????? 1?????????
    public void setSMapDAO(int stageId,int mode,int freeConquestYear,boolean ifNew) {
        this.stageId=stageId;
        XmlReader.Element  defStage = gameConfig.getDEF_STAGE().getElementById(stageId);
        StringBuilder path = null;
        if(ifNew){
            path = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
            path.append(defStage.get("name")).append("_n.btl");
        }else{
            path  = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
            path.append(defStage.get("name")).append(".btl");
        }
        String filePath=path.toString();
        boolean ifInit = Gdx.files.local(filePath).exists();
       // boolean ifSave =gameConfig.playerInfo.getBoolean(stageId+"_ifSave",false);

         if ( (!ifInit|| !(ifInit&&mode!=1) ||freeConquestYear>0) && !(mode==1&&ifInit)) {//????????????????????????
            //???????????? ?????????????????????????????????
            //int year= defStage.getInt("year");
            path = new StringBuilder("btl/");
                    path.append(defStage.get("name")).append(".btl");
             filePath=path.toString();
             ifInit=Gdx.files.internal(filePath).exists();
            int btlType=defStage.getInt("btlType");

            if(ifInit){//
                sMapDAO = new Fb2Smap(this, Gdx.files.internal(filePath).readBytes(), stageId);
                //?????????????????????, ??????btlInitForBlankConquest ?????? btlInitForConquest ?????????????????? masterData 0 resetMapData 1 ?????????,??????esc????????????
                //masterData 0 resetMapData 1 ?????????0 ???regionName?????????,?????????region????????? ??????1
                //masterData 0 synchronouByHistory 0 ???????????????????????????

                /*if(mode==0){//??????????????????????????????
                   *//* if(btlType==0){//???????????????????????????
                    sMapDAO.btlInitForConquest();
                      //  sMapDAO.btlInitForBlankConquest();
                    }*//*
                }else{//???????????????????????????
                  //  sMapDAO.btlInitForBlankConquest();
                    sMapDAO.controlMode=2;
                }*/
            }else {
                int historyId=defStage.getInt("historyId");
                XmlReader.Element defHistory = gameConfig.getDEF_HISTORY().getElementById(historyId);
                if(defHistory==null){
                    return;
                }
             //   freeConquestYear=defStage.getInt("year");
                if(freeConquestYear<=0){
                         if(btlType==1){
                             XmlReader.Element empireE=gameConfig.getEmpireXmlE(stageId,0);
                             if(empireE!=null){
                                 freeConquestYear=empireE.getInt("year");
                             }
                        }
                }
                Fb2History historyDao =gameFramework.getHistory(historyId,freeConquestYear);
                Fb2Map mapBinDAO = gameFramework.getMapDaoByMapId(historyDao.masterData.getMapId());
                if(btlType==0){//???????????? 0
                    sMapDAO = new Fb2Smap();
                  sMapDAO.createBtlForFreeConquest(this,historyDao, mapBinDAO, stageId,defStage,defHistory,freeConquestYear);
                   // sMapDAO.createBtlForBlankConquest(this,historyDao, mapBinDAO, stageId,defStage,defHistory,freeConquestYear);
                }else if(btlType==1){//????????????
                    sMapDAO = new Fb2Smap();
                    sMapDAO.createBtlForEmpire(this,historyDao, mapBinDAO, stageId,defStage,defHistory);
                }
                historyDao = null;
            }
            getGameFramework().deleteExternalViewFile(ResDefaultConfig.Path.ViewFolderPath + defStage.get("name")+ ".png");
        } else {//????????????
             try {
                 sMapDAO = new Fb2Smap(this, Gdx.files.local(filePath).readBytes(), stageId);
             } catch (Exception e) {
                 if(ResDefaultConfig.ifDebug){
                     e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                 }else {   path = new StringBuilder("btl/");
                     path.append(defStage.get("name")).append("_e.btl");
                     filePath=path.toString();
                     boolean ifHave=Gdx.files.internal(filePath).exists();
                     if(ifHave) {//
                         sMapDAO = new Fb2Smap(this, Gdx.files.local(filePath).readBytes(), stageId);
                     }else{
                         path = new StringBuilder("btl/");
                         path.append(defStage.get("name")).append(".btl");
                         filePath=path.toString();
                          ifHave=Gdx.files.internal(filePath).exists();
                         if(ifHave) {//
                             sMapDAO = new Fb2Smap(this, Gdx.files.internal(filePath).readBytes(), stageId);
                         }
                     }
                 }
             }
        }
        if(mode==1){//??????????????????????????????
            sMapDAO.controlMode=2;
        }
    }


    public TextureRegion getFlagBgTextureRegion(int countryId){
        String imgName;
        if(!gameConfig.ifEffect){
            imgName="flagbg_0";
        }else {
            imgName ="flagbg_"+countryId;
        }

        if(tempTextureRegions.containsKey(imgName)){
            return tempTextureRegions.get(imgName);
        }else {
            TextureRegion r=null;
            r=new TextureRegion(new Texture(Gdx.files.internal(ResDefaultConfig.Path.FlagBgFolderPath+imgName+".png")));
            tempTextureRegions.put(imgName,r);
            return r;
        }
    }

    public TextureRegion getPreviewTextureRegion(String imgName){
        if(tempTextureRegions.containsKey(imgName)){
            return tempTextureRegions.get(imgName);
        }else {
            TextureRegion r=null;
            String path= ResDefaultConfig.Path.ViewFolderPath+imgName+".png";
            FileHandle f=Gdx.files.external(path);
            if(f.exists()){
                r=new TextureRegion(new Texture(f));
                tempTextureRegions.put(imgName,r);
                return r;
            }else{
                f=Gdx.files.local(path);
                if(f.exists()){
                    r=new TextureRegion(new Texture(f));
                    tempTextureRegions.put(imgName,r);
                    return r;
                }else {
                    f=Gdx.files.internal(path);
                    if(f.exists()){
                        r=new TextureRegion(new Texture(f));
                        tempTextureRegions.put(imgName,r);
                        return r;
                    }else {
                        return null;
                    }
                }
            }
        }
    }

    public void removeTempTextureRegion(String imgName){
        if(tempTextureRegions.containsKey(imgName)) {
           TextureRegion t=  tempTextureRegions.remove(imgName);
           if(t!=null){
               t.getTexture().dispose();
               t=null;
           }
        }
    }


    public TextureRegion getEventBgTextureRegion(int eventImgId){
        if(eventImgId==-1){
            return tempTextureRegions.get("Assistant");
        }
        String imgName="event_"+eventImgId;
        if(tempTextureRegions.containsKey(imgName)){
            return tempTextureRegions.get(imgName);
        }else {
            TextureRegion r=null;
            r=new TextureRegion(new Texture(Gdx.files.internal(ResDefaultConfig.Path.EventBgFolderPath+imgName+".png")));
            tempTextureRegions.put(imgName,r);
            return r;
        }
    }
    public TextureRegion getPTTextureRegion(int ptId){
        String imgName="pt_"+ptId;
        if(tempTextureRegions.containsKey(imgName)){
            return tempTextureRegions.get(imgName);
        }else {
            TextureRegion r=null;
            r=new TextureRegion(new Texture(Gdx.files.internal(ResDefaultConfig.Path.PTFolderPath+imgName+".png")));
            tempTextureRegions.put(imgName,r);
            return r;
        }
    }


    public TextureRegion getGeneralTextureRegion(int generalId){
        if(generalId==-1){
            return getGeneralTextureRegion(0,"player");
        }else{
            return getGeneralTextureRegion(0,gameConfig.getDEF_GENERAL().getElementById(generalId).get("image"));
        }
    }

    public TextureRegion getGeneralTextureRegion(int state,String imgName ) {
        if(tempTextureRegions.containsKey(imgName)){
           // Gdx.app.log("getG",imgName);
            return tempTextureRegions.get(imgName);
        }else {
            TextureRegion r=null;
            if(!imgName.equals(ResDefaultConfig.Image.GENERAL_IMAGE_PLAYER)   &&!imgName.equals(ResDefaultConfig.Image.GENERAL_IMAGE_ASSISTANT)   &&(state!=0||!resGameConfig.enableGeneral||  (!gameConfig.ifEffect&&!imgName.equals("player")&&!imgName.equals("Assistant")) )){
                // Gdx.app.log("loadG1",imgName);
                // r=new TextureRegion(new Texture(Gdx.files.internal(ResDefaultConfig.Path.RGeneralFolderPath+imgName+".png")));
                return imgLists.getTextureByName("general_0").getTextureRegion();
            }else {
                    // Gdx.app.log("loadG0",imgName);
                FileHandle f=Gdx.files.internal(ResDefaultConfig.Path.GeneralFolderPath+imgName+".tata");
                if(f.exists()){
                    r=new TextureRegion(GameUtil.transTataToTexture(f));
                }else {
                    f=Gdx.files.internal(ResDefaultConfig.Path.GeneralFolderPath+imgName+".png");
                    if(f.exists()){
                        r=new TextureRegion(new Texture(f));
                    }
                }
                if(r!=null){
                    tempTextureRegions.put(imgName,r);
                }
                return r;
            }
        }
    }


    public TextureRegion getLastCountryTextureRegion(){
        String imgName="lastCountryView";
        if(tempTextureRegions.containsKey(imgName)){
            // Gdx.app.log("getG",imgName);
            return tempTextureRegions.get(imgName);
        }else {
            FileHandle f=Gdx.files.external(ResDefaultConfig.Path.lastCountryView);
            if(f.exists()){
                Pixmap pixmap = new Pixmap(f);
              Texture texture= new Texture(pixmap);
                TextureRegion   region = new TextureRegion(texture,  (texture.getWidth()-texture.getHeight())/2  ,0,texture.getHeight(),texture.getHeight()     );
                tempTextureRegions.put(imgName,region);
                return region;
            }
        }
        return null;
    }


    public void clearAllTempTextureRegions() {
      //  for(TextureRegion t:tempTextureRegions.values()){

           // Iterator<String, TextureRegion> itb = tempTextureRegions.iterator();
        Iterator<ObjectMap.Entry<String,TextureRegion>> itb = tempTextureRegions.iterator();
            while (itb.hasNext()) {
                TextureRegion t=itb.next().value;
            if(t!=null&& t.getTexture()!=null){
                t.getTexture().dispose();
                t=null;
            }
        }
        tempTextureRegions.clear();
    }


    //????????????stage???????????????,?????????????????????????????????
    public String getStageIntroduce() {
        if(getStageId()<1000){//??????
            return gameMethod.getDialogueStr(16,ComUtil.getRandom(1,gameConfig.getDEF_RDIALOGUE().getElementById(16).getInt("count") ) ,null);
        }/*else if(getStageId()<2000){//??????
            int stageEpisode = gameConfig.playerConfig.getInteger(getStageId() + "_gameEpisode", 0);
            XmlReader.Element e =gameConfig.getEmpireXmlE(getStageId(), stageEpisode);
           return gameMethod.getStrValue("empire_content_" + getStageId() + "_" + stageEpisode);
        }*/else{//??????
            return gameMethod.getStrValue("stage_prologue_"+getStageId());
        }
    }

    public void remindBugFeedBack(){
        if(!ResDefaultConfig.ifDebug&&gameConfig.getIfIgnoreBug()){

        }else if(screenId==ResDefaultConfig.Class.SMapScreen&&sMapScreen!=null){
            sMapScreen.remindBugFeedBack();
        }else if(screenId==ResDefaultConfig.Class.MapDetailScreen&&mapDetailScreen!=null){//TODO

        }else if(generalScreen!=null&&generalScreen.getDialogueWindow()!=null){
            generalScreen.remindBugFeedBack();
        }
    }

    //????????????????????????
    public void setMusicById(int id,boolean ifForceLoop){
        Gdx.app.log("playMusic",id+":"+musicVoice);
        if(musicId!=id||!music.isLooping()){
           lastMusicId=musicId;
            musicId=id;
            music.pause();
            music.dispose();
            music = Gdx.audio.newMusic(gameConfig.getMusicFile(musicId));
         //   music.setLooping(true);
            music.setVolume(musicVoice*1.5f/7);
            if(musicVoice==0){
                music.pause();
            }else {
                music.play();
            }
           if(gameConfig.getCONFIG_MUSIC().getElementById(musicId).get("play","loop").equals("loop")||ifForceLoop){
               music.setLooping(true);
           }else{
               music.setLooping(false);
               music.setOnCompletionListener(new Music.OnCompletionListener() {
                   @Override
                   public void onCompletion(Music aMusic) {
                        int musicType=gameConfig.getCONFIG_MUSIC().getElementById(musicId).getInt("type",0);
                       int musicId=gameConfig.geRandomtMusicId(musicType,lastMusicId);
                       setMusicById(musicId,false);
                   }
               });
           }
        }
    }
    public void setMusicByType(int type){
        int musicType=gameConfig.getCONFIG_MUSIC().getElementById(musicId).getInt("type",0);
       if(type!=musicType){
        int musicId=gameConfig.geRandomtMusicId(type,this.musicId);
           setMusicById(musicId,false);
       }
    }


    public void disposeSound(){
        Iterator<ObjectMap.Entry<String,Sound>> it = soundList.iterator();
        while (it.hasNext()) {
         Sound sound = it.next().value;
            if (sound == null) {
                continue;
            }
            sound.stop();
            sound.dispose();
            sound=null;
        }
        soundList.clear();
    }

    public void playSound(Stage stage, final String soundName, float delayTime){
        RunnableAction resetModelAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                playSound(soundName);
                //Gdx.app.log("????????????","123");
            }
        });
        //2??????????????????????????????
        stage.addAction(Actions.sequence(Actions.delay(delayTime), resetModelAction));
    }

    public void playSound(String soundName){
      // Gdx.app.log("playSound:",soundName);

        if(soundVoice==0){
            return;
        }
        try {
            float volume=soundVoice*1.5f/7;
            if(soundList.containsKey(soundName)){
                Sound sound=soundList.get(soundName);
                if(sound!=null){
                    sound.play(volume);
                }
            }else if(gameConfig.getCONFIG_SOUND().contain(soundName)){
                Sound sound = Gdx.audio.newSound(gameConfig.getSoundFile(soundName));
                sound.play(volume);
                soundList.put(soundName,sound);
            }
        } catch (Exception e) {
            if(ResDefaultConfig.ifDebug){
                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
            }
        }
    }


    //ExtraType 0????????? 1???????????? 2????????????????????????
    public void save(final int backupType) {

       // asyncManager.clearTask();//?????????????????????????????????????????????
       /**/final CHAsyncTask task = new CHAsyncTask("save",999) {
         final   Fb2Smap fb2Smap=sMapDAO;
            @Override
            public void onPreExecute() {
                saveOk=false;
                if(getSMapDAO().ifCheckCheat()){
                    fb2Smap.masterData.setIfCheat(1);
                }
            }

            @Override
            public void onPostExecute(String result) {
                saveOk=true;
            }
            @Override
            public String doInBackground() {
                saveConfig(fb2Smap);
                fb2Smap.saveSmapBin(getStageId(),backupType);
                return null;
            }
        };
        asyncManager.loadTask(task);
        asyncManager.update();

      //  sMapDAO.save();
    }

    public void saveConfig(Fb2Smap sMapDAO) {
        gameConfig.playerConfig.putBoolean(getStageId() + "_ifSave", true);
        gameConfig.playerConfig.putBoolean(getStageId() + "_ifCheat", sMapDAO.masterData.getIfCheat()!=0);
        gameConfig.playerConfig.putInteger(getStageId() + "_country",sMapDAO.getPlayerLegionData().getCountryId());
        gameConfig.playerConfig.putInteger(getStageId() + "_regionNum", sMapDAO.masterData.varPlayerRegionCount);
        gameConfig.playerConfig.putInteger(getStageId() + "_beginYear", sMapDAO.masterData.getBeginYear());
        gameConfig.playerConfig.putInteger(getStageId() + "_roundNow", sMapDAO.masterData.getRoundNow());
        gameConfig.playerConfig.putInteger(getStageId() + "_roundMax", sMapDAO.masterData.getRoundMax());
      //  gameConfig.playerConfig.putInteger(getStageId() + "_mode", sMapDAO.masterData.getPlayerMode());
        gameConfig.playerConfig.putInteger(getStageId() + "_gameEpisode", sMapDAO.masterData.getGameEpisode());
        //gameConfig.config.putString(getStageId()+"_saveTime",getTime());
      /*  if(sMapDAO.stageId==0){//????????????????????????
          //  gameConfig.playerConfig.putString(getStageId() + "_stageName", sMapDAO.getStageName()+"-"+sMapDAO.masterData.getBeginYear());
        }else {


        //    gameConfig.playerConfig.putString(getStageId() + "_stageName", sMapDAO.getStageName()+"-"+gameMethod.getStrValueT("stage_mode_"+sMapDAO.masterData.getPlayerMode()));
        }*/

        gameConfig.playerConfig.putInteger(getStageId() + "_year", sMapDAO.masterData.getBeginYear());
        gameConfig.playerConfig.putInteger(getStageId() + "_mode", sMapDAO.masterData.getPlayerMode());
        gameConfig.playerConfig.putInteger("lastCountry",sMapDAO.getPlayerLegionData().getCountryId());
        gameConfig.playerConfig.putInteger("lastGeneral",sMapDAO.getPlayerLastGeneral());

        if(sMapDAO.masterData.getPlayerMode()==1&&sMapDAO.masterData.getBtlType()==1){
            gameConfig.playerConfig.putInteger("lastEmpire",getStageId()-101);
        }else if(sMapDAO.masterData.getPlayerMode()==0){
            int countryLv=sMapDAO.getPlayerCountryLv();

            if(countryLv==0){
                gameConfig.playerConfig.putInteger("lastAchievementFlagBg",0);
            }else{
                gameConfig.playerConfig.putInteger("lastAchievementFlagBg",sMapDAO.getPlayerLegionData().getCountryId());
            }
            gameConfig.playerConfig.putString("lastAchievement",sMapDAO.getPlayerAchievementImg(countryLv));
        }

        gameConfig.playerConfig.flush();

    }

    public void clearSaveConfig() {
        gameConfig.playerConfig.putBoolean(getStageId() + "_ifSave", false);
        gameConfig.playerConfig.putBoolean(getStageId() + "_ifCheat", false);
        gameConfig.playerConfig.putInteger(getStageId() + "_gameEpisode", 0);
        gameConfig.playerConfig.flush();
    }

    public Fb2Smap getSMapDAO() {
        return sMapDAO;
    }
    //?????????????????????
    public void initScreenConfig() {
        /*if(true){//test
            Gdx.graphics.setWindowedMode(1280, 591);
            worldWidth = Gdx.graphics.getWidth();
            worldHeight =  Gdx.graphics.getHeight();
            return;
        }*/


        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            /*if(gameConfig.playerConfig.getBoolean("isFullscreen", false)){
                int index= gameConfig.playerConfig.getInteger("screenSizeIndex",ResDefaultConfig.Game.defaultScreenIndex);
                if(index<0||index>=ResDefaultConfig.Game.screenWidth.length){
                    index=0;
                }
                DisplayMode displayMode= Gdx.graphics.getDisplayMode();
                for(int i=0;i<dms.length;i++){
                    DisplayMode dm=dms[i];
                    if(dm.width==ResDefaultConfig.Game.screenWidth[index]&&dm.height==ResDefaultConfig.Game.screenHeight[index]){
                        displayMode=dm;
                        break;
                    }
                }
                Gdx.graphics.setFullscreenMode(displayMode);
                gameConfig.ifMoveInFullScreen=true;
            }else {
                int index= gameConfig.playerConfig.getInteger("screenSizeIndex",ResDefaultConfig.Game.defaultScreenIndex);
                if(index<0||index>=ResDefaultConfig.Game.screenWidth.length){
                    index=0;
                }
                Gdx.graphics.setWindowedMode(ResDefaultConfig.Game.screenWidth[index],  ResDefaultConfig.Game.screenHeight[index]);
            }*/
            worldWidth = Gdx.graphics.getWidth();
            worldHeight =  Gdx.graphics.getHeight();
            Gdx.graphics.setForegroundFPS(120);
        }else{
            worldWidth = ResDefaultConfig.FIX_WORLD_WIDTH;
            worldHeight = Gdx.graphics.getHeight() * worldWidth / Gdx.graphics.getWidth();
        }
        Gdx.graphics.setVSync(gameConfig.getVSync());
    }
    //????????????
    public void backupSav(String stageId) {
        if(ComUtil.isNumeric(stageId)){
            int id=Integer.parseInt(stageId);
         XmlReader.Element defStage;
            if(id==-1){
                defStage=this.defStage;
            }else{
                defStage=   gameConfig.getDEF_STAGE().getElementById(id);;
            }
         if(defStage!=null){
             StringBuilder  path  = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
             path.append(defStage.get("name")).append(".btl");
             FileHandle f= Gdx.files.local(path.toString());
             FileHandle f2= Gdx.files.external(path.toString());
            if(f.exists()){
                f.copyTo(f2);
            }
         }
        }
    }

    public void useBackupSav(String stageId){
        if(ComUtil.isNumeric(stageId)){
            int id=Integer.parseInt(stageId);
            XmlReader.Element defStage;
            if(id==-1){
                defStage=this.defStage;
            }else{
                defStage=   gameConfig.getDEF_STAGE().getElementById(id);;
            }
            if(defStage!=null){
                String  path  = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath).append(defStage.get("name")).append(".btl").toString();
                String ePath=new StringBuilder(ResDefaultConfig.Path.SaveFolderPath).append(defStage.get("name")).append("_e.btl").toString();

                FileHandle sf= Gdx.files.local(path);
                FileHandle ef= Gdx.files.local(ePath);
                if(ef.exists()){
                    ef.copyTo(sf);
                }
            }
        }
    }


    public void saveMapForPng() {
        gameFramework.writeMapByMapId(getMapId(),getAssetManager(),getMapScale());
    }


    public void saveSeaMapForPng() {
        gameFramework.writeSeaMapByMapId(getMapId(),getAssetManager(),getMapScale());
    }

    @Override
    public void render() {
        try {
            super.render();
        } catch (Exception e) {
            if(ResDefaultConfig.ifDebug){
                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
            }else if(!gameConfig.getIfIgnoreBug()){
                remindBugFeedBack();
            }
            recordLog("MainGame render ",e);
            if(sMapScreen!=null){
                sMapScreen.resetPlayerStar();
                sMapScreen.showLegionTechUI(sMapScreen.smapGameStage.getPlayerLegion());
                sMapScreen.showSaveUI();
            }
        }
    }


    public void recordLog(String str,Throwable t){
        if(ResDefaultConfig.ifDebug||ResDefaultConfig.ifTest||!gameConfig.ifIgnoreBug){
            GameUtil.recordLog(str,t);
        }
    }

    public void deleteSav(boolean ifOther) {
        if(defStage==null){
            return;
        }
        StringBuilder  path  = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
        path.append(defStage.get("name")).append(".btl");
        FileHandle f= Gdx.files.local(path.toString());
        if(f.exists()){
            f.delete();
        }
        if(ifOther){
            path  = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
            path.append(defStage.get("name")).append("_n.btl");
            f= Gdx.files.local(path.toString());
            if(f.exists()){
                f.delete();
            }
            path  = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
            path.append(defStage.get("name")).append("_e.btl");
            f= Gdx.files.local(path.toString());
            if(f.exists()){
                f.delete();
            }
        }

        gameConfig.playerConfig.putBoolean(getStageId() + "_ifSave", false);
        gameConfig.playerConfig.putBoolean(getStageId() + "_ifCheat", false);
        gameConfig.playerConfig.putInteger(getStageId() + "_gameEpisode", 0);
        gameConfig.playerConfig.flush();
    }

    public boolean ifStageFileExist(int stageId) {
        StringBuilder  path  = new StringBuilder(ResDefaultConfig.Path.SaveFolderPath);
        XmlReader.Element defStage= gameConfig.getDEF_STAGE().getElementById(stageId);
        if(defStage!=null){
            path.append(defStage.get("name")).append(".btl");
            FileHandle f= Gdx.files.local(path.toString());
            if(f.exists()){
                return true;
            }
        }
        return false;
    }
}
