package com.zhfy.game.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2History;
import com.zhfy.game.model.framework.PixmapDAO;
import com.zhfy.game.model.framework.PixmapListDAO;

import java.io.IOException;
import java.io.StringWriter;

public class GameFramework {



    //private FSearchTool tool ;//哪个用到哪个初始化
    public MainGame game;

    public GameFramework(MainGame game){
        this.game=game;
    }
    
    /*// 通过screenId获取要读取的图片资源
    public List<StringName> getTextureNameByScreenId(int screenId) {
        List<StringName> strs = new ArrayList<StringName>();
                Array<Element> xmlFiles = ResConfig.gameConfig.getCONFIG_LAYOUT().getElementById(screenId)
                        .getChildrenByNameRecursively("xmlFile");
                *//*for (Element xmlFile : xmlFiles) {
                    strs.add(xmlFile.get("name"));
                }*//*
                for(int j=0,jMax=xmlFiles.size;j<jMax;j++){
                    strs.add(xmlFiles.get(j).get("name"));
                }
        return strs;
    }
*/

    // 根据mapid生成MapBinDAO类 画全图
    public Fb2Map getMapDaoByMapId(int mapId) {
        if(mapId==game.resGameConfig.defaultMapBinId&&game.defaultMapBinDAO!=null){
            return game.defaultMapBinDAO;
        }
        String str = "";
        Element root = game.gameConfig.getDEF_MAP().getElementById(mapId);
        Fb2Map mapBinDao=null;
        if (root.getInt("id") == mapId) {
            byte[] bt;
            str = root.get("name");
            StringBuffer path=new StringBuffer("bin/.bin");
            path.insert(4,str);
            if(game.gameConfig.getPlatform().equals("Desktop")||game.gameConfig.getPlatform().equals("HeadlessDesktop")){

                bt = Gdx.files.local(path.toString()).readBytes();
                //bt = Gdx.files.local("bin/" + str + ".bin").readBytes();
            }else{
                bt = Gdx.files.internal(path.toString()).readBytes();
                //bt = Gdx.files.internal("bin/" + str + ".bin").readBytes();
            }
            try {
                //  mapBinDao=new MapBinDAO(BTLTooL.LoadBtl(ResConfig.Rules.RULE_FB1_MAP,bt));
                mapBinDao=new Fb2Map(game,mapId,bt);


            } catch (Exception e) {
                if(ResDefaultConfig.ifDebug){
                    e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                }else if(!game.gameConfig.getIfIgnoreBug()){
                    game.remindBugFeedBack();
                }
                game.recordLog("GameFramework getMapDaoByMapId ",e);
            }
            bt=null;
            return mapBinDao;
        }
        return mapBinDao;
    }

    //path
    /*public MapBinDAO getMapDaoByPath(String mapBinPath) {
     *//*if(mapId==1){
            //return ResConfig.GameConfig.mapBinDao;
            return game.getMapBinDao();
        }*//*
        String str = "";
        //Element root = game.gameConfig.getDEF_MAP().getElementById(mapId);
        MapBinDAO mapBinDao=null;byte[] bt=getBtByName(mapBinPath);
        try {
            //  mapBinDao=new MapBinDAO(BTLTooL.LoadBtl(ResConfig.Rules.RULE_FB1_MAP,bt));
            mapBinDao=new MapBinDAO(game,mapbt);


        } catch (Exception e) {
            e.printStackTrace();
        }
        bt=null;
        return mapBinDao;
    }*/






    public byte[] getBtByName(String name) {
        byte[] bt=null;
        StringBuilder path=new StringBuilder("bin/");
        path.append(name).append(".bin");
        if(game.gameConfig.equals("Desktop")||game.gameConfig.equals("HeadlessDesktop")){
            bt = Gdx.files.local(path.toString()).readBytes();
        }else{
            bt = Gdx.files.internal(path.toString()).readBytes();
        }
        return bt;
    }

    //根据传入的地图编号保存地图
    public void saveMapDaoByMapId(Fb2Map mapbin, int mapId) {
        XmlReader reader = game.gameConfig.reader;
        String str = "";
        Element root = game.gameConfig.getDEF_MAP().getElementById(mapId);
        int childNum = root.getChildCount();
        str = root.get("name");
        String path="bin/" + str + ".bin";
        mapbin.saveMapBin(path);
    }

    //根据stageid保存地图
    public void saveSMapDaoBySMapId(Fb2Map mapbin, int stageId) {
        XmlReader reader = game.gameConfig.reader;
        String str = "";
        Element root = game.gameConfig.getDEF_STAGE().getElementById(stageId);
        int childNum = root.getChildCount();
        str = root.get("name");
        String path="bin/" + str + ".bin";
        mapbin.saveMapBin(path);
    }
    
    
    
    /*//根据MapId获得地图属性
    public Element getDefMapByMapId(int id) {
        for(int i=0,iMax=ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").size;i<iMax;i++){
            if(ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").get(i).getInt("id")==id){
                return ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").get(i);
            }
        }
        return null;
    }*/
    
  /*//根据StageId获得属性
    public Element getDefStageByStageId(int id) {
        Array<Element> stages=ResConfig.gameConfig.getDEF_STAGE().getChildrenByName("stage");
        for(int s=0,sMax=stages.size;s<sMax;s++){
            if(stages.get(s).getInt("id")==id){
                return stages.get(s);
            }
        }
        return null;
    }*/


    // 依据id画全图
    public Texture getMapByMapId(int id,AssetManager am,float scale)  {
        // 2.1加载图片资源 一次性使用 用完就删
        //TextureRegionListDAO mapRegionList = getTextureReigonByImgFileName("bt_tiles");
        PixmapListDAO pixmapLists=getPixmapListByFileName("pm_tiles",am);

        Fb2Map mapBinDao=null;

        // 2.2加载地图属性
        Element defMap=null;
        /*for(int i=0,iMax=ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").size;i<iMax;i++){
            if(ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").get(i).getInt("id")==id){
                defMap=ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").get(i);
            }
        }*/
        defMap=game.gameConfig.getDEF_MAP().getElementById(id);

        // 2.3加载bin
        mapBinDao = getMapDaoByMapId(id);

        // 2.4读取map.xml获取地图属性




        // 2.5生成Pixmap
        Pixmap  pixmap=GameMap.createPixmap(defMap.getInt("width"),defMap.getInt("height"),scale);
        //Pixmap  pixmap=GameMap.createPixmapByDefMap(defMap,scale);
        //绘制底纹
        pixmap=GameMap.coverImgByPtimgId(game,pixmap,ResDefaultConfig.Image.DEFAULT_PT);


        long startTime = System.currentTimeMillis();    //获取开始时间
        //绘图
        pixmap=GameMap.getPixmapByDao(game,mapBinDao,pixmap,pixmapLists,0,0,mapBinDao.mapWidth,mapBinDao.mapHeight,scale);
        long endTime = System.currentTimeMillis();    //获取结束时间

        Gdx.app.log("地图构建", " 运行时间:"+(endTime - startTime) + "ms");

        // 再由新的 Pixmap 对象生成一个新的 Texture 对象
        Texture textureMap = new Texture(pixmap, Format.RGBA8888,false);
        if(pixmap!=null) {
            //这里输出截图进行测试
            //PixmapIO.writePNG(Gdx.files.external(defMap.getName()+".png"), pixmap);
            pixmap.dispose();
            //Gdx.app.log("地图图片保存", "成功");
        }
        //clearPixmapListByFileName("pm_tiles");
        return textureMap;
    }

    public void writeMapByMapId(int id,AssetManager am,float scale)  {
        // 2.1加载图片资源 一次性使用 用完就删
        //TextureRegionListDAO mapRegionList = getTextureReigonByImgFileName("bt_tiles");
        PixmapListDAO pixmapLists=getPixmapListByFileName("pm_tiles",am);

        Fb2Map mapBinDao=null;

        // 2.2加载地图属性
        Element defMap=null;
        /*for(int i=0,iMax=ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").size;i<iMax;i++){
            if(ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").get(i).getInt("id")==id){
                defMap=ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").get(i);
            }
        }*/
        defMap=game.gameConfig.getDEF_MAP().getElementById(id);

        // 2.3加载bin
        mapBinDao = getMapDaoByMapId(id);

        // 2.4读取map.xml获取地图属性




        // 2.5生成Pixmap
        Pixmap  pixmap=GameMap.createPixmap(defMap.getInt("width"),defMap.getInt("height"),scale);
        //Pixmap  pixmap=GameMap.createPixmapByDefMap(defMap,scale);
        //绘制底纹
        pixmap=GameMap.coverImgByPtimgId(game,pixmap,ResDefaultConfig.Image.DEFAULT_PT);


        long startTime = System.currentTimeMillis();    //获取开始时间
        //绘图
        pixmap=GameMap.getPixmapByDao(game,mapBinDao,pixmap,pixmapLists,0,0,mapBinDao.mapWidth,mapBinDao.mapHeight,scale);
        long endTime = System.currentTimeMillis();    //获取结束时间

        Gdx.app.log("地图构建", " 运行时间:"+(endTime - startTime) + "ms");

        // 再由新的 Pixmap 对象生成一个新的 Texture 对象

        if(pixmap!=null) {
            //这里输出截图进行测试
            PixmapIO.writePNG(Gdx.files.local(ResDefaultConfig.Path.MapFolderPath+defMap.get("name")+".png"), pixmap);
            pixmap.dispose();
            Gdx.app.log("地图图片保存", "成功");
        }else{
            Gdx.app.error("地图图片保存", "失败");
        }
        return;
    }
    public void writeSeaMapByMapId(int id,AssetManager am,float scale)  {
        // 2.1加载图片资源 一次性使用 用完就删
        //TextureRegionListDAO mapRegionList = getTextureReigonByImgFileName("bt_tiles");
        PixmapListDAO pixmapLists=getPixmapListByFileName("pm_tiles",am);

        Fb2Map mapBinDao=null;

        // 2.2加载地图属性
        Element defMap=null;
        /*for(int i=0,iMax=ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").size;i<iMax;i++){
            if(ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").get(i).getInt("id")==id){
                defMap=ResConfig.gameConfig.getDEF_MAP().getChildrenByName("map").get(i);
            }
        }*/
        defMap=game.gameConfig.getDEF_MAP().getElementById(id);

        // 2.3加载bin
        mapBinDao = getMapDaoByMapId(id);

        // 2.4读取map.xml获取地图属性




        // 2.5生成Pixmap
        Pixmap  pixmap=GameMap.createPixmap(defMap.getInt("width"),defMap.getInt("height"),scale);
        //Pixmap  pixmap=GameMap.createPixmapByDefMap(defMap,scale);
        //绘制底纹
        //pixmap=GameMap.coverImgByPtimgId(game,pixmap,ResDefaultConfig.Image.DEFAULT_PT);


        long startTime = System.currentTimeMillis();    //获取开始时间
        //绘图
        pixmap=GameMap.getPixmapByDaoForSea(game,mapBinDao,pixmap,pixmapLists,0,0,mapBinDao.mapWidth,mapBinDao.mapHeight,scale);
        long endTime = System.currentTimeMillis();    //获取结束时间

        Gdx.app.log("地图构建", " 运行时间:"+(endTime - startTime) + "ms");

        // 再由新的 Pixmap 对象生成一个新的 Texture 对象

        if(pixmap!=null) {
            //这里输出截图进行测试
            PixmapIO.writePNG(Gdx.files.local(ResDefaultConfig.Path.MapFolderPath+defMap.get("name")+".png"), pixmap);
            pixmap.dispose();
            Gdx.app.log("地图图片保存", "成功");
        }else{
            Gdx.app.error("地图图片保存", "失败");
        }
        return;
    }
    // 根据条件画图 散图
    //ifUnload 是否卸载绘图资源
    public Pixmap getPixmapDecorateByDao(Pixmap pixmap, Fb2Map mapBinDao, AssetManager am, float scale, boolean ifUnload)  {
        // 2.1加载图片资源 一次性使用 用完就删
        //TextureRegionListDAO mapRegionList = getTextureReigonByImgFileName("bt_tiles");
        PixmapListDAO pixmapLists=getPixmapListByFileName("pm_tiles",am);

        // 2.2加载地图属性

        long startTime = System.currentTimeMillis();    //获取开始时间
        //绘图  MapBinDAO mapBinDAO, Pixmap pixmap, PixmapListDAO pixmapLists,  int beginIndex, int EndIndex, float scale
        pixmap=GameMap.getPixmapByDao(game,mapBinDao,pixmap,pixmapLists,0,0,mapBinDao.mapWidth,mapBinDao.mapHeight,scale);
        long endTime = System.currentTimeMillis();    //获取结束时间
        Gdx.app.log("地图构建", " 运行时间:"+(endTime - startTime) + "ms");

        //clearPixmapListByFileName("pm_tiles");
        if(ifUnload){//卸载其资源  如果不卸载,记得最后卸载
            GameUtil.unloadPixmapByFileName(game,"pm_tiles",am);
        }
        pixmapLists=null;
        return pixmap;
    }



    //根据条件给陆地打孔 废弃
   /* public Pixmap getLandByDAO(MapBinDAO mapBinDAO, Pixmap pixmap, int mapx, int mapy,int mapw,int maph) {
        //加载打孔图片
        PixmapListDAO pixmapLists=getPixmapListByFileName("pm_shadow");
        pixmap=GameMap.getLandByDAO(mapBinDAO,pixmap,pixmapLists,mapx,mapy,mapw,maph);
        //clearPixmapListByFileName("pm_shadow");
        return pixmap;
    }*/


    //加载内存图片PixmapListDAO pixmapLists 目标pm_tiles 则传入pm_tiles TODO
    public  PixmapListDAO getPixmapListByFileName(String fileName,AssetManager am) {
        //1读取路径下说明文件
        XmlReader reader = game.gameConfig.reader;
        StringBuilder path=new StringBuilder("pixmap/");
        path.append(fileName.substring(3)).append("/").append(fileName).append(".xml");
        Element root = reader
                .parse(Gdx.files.internal(path.toString()));
        /*Element root = reader
                .parse(Gdx.files.internal("pixmap/"+fileName.substring(3)+"/" + fileName + ".xml"));*/
        // 每个图片添加的时候都要加使用场景,多场景用;分割screenid="1"
        Array<Element> images = root.getChildrenByNameRecursively("sprite");
        //验证pixmapLists是否有东西
        PixmapListDAO   pixmapLists=new PixmapListDAO();
        //2根据说明文件添加到pixmapLists
        for (int i=0,iMax=images.size;i<iMax;i++) {
            PixmapDAO pixmapDAO = new PixmapDAO();
            //pixmapDAO.setPixmap( new Pixmap(Gdx.files.internal("pixmap/"+fileName.substring(3)+"/" +image.get("n"))));;

            if(am.contains("pixmap/"+fileName.substring(3)+"/" +images.get(i).get("n"),Pixmap.class)){
                pixmapDAO.setPixmap(am.get("pixmap/"+fileName.substring(3)+"/" +images.get(i).get("n"),Pixmap.class));
            }else{
                pixmapDAO.setPixmap( new Pixmap(Gdx.files.internal("pixmap/"+fileName.substring(3)+"/" +images.get(i).get("n"))));
            }

            pixmapDAO.setRefx(images.get(i).getInt("refx"));
            pixmapDAO.setRefy(images.get(i).getInt("refy"));
            pixmapDAO.setName(images.get(i).get("n").replace(".png", ""));
            pixmapLists.add(pixmapDAO);
        }
        //pixmapLists.check();
        return pixmapLists;
    }

    //definitionType hd,sd
    public void cupMap(Pixmap tempPixmap,PixmapListDAO pixmapLists,Element mapE,String definitionType,float scale,boolean ifLand) throws IOException {

        String mapName=mapE.get("name");
        String  path="bin/"+mapName+".bin";
        boolean exists = Gdx.files.internal(path).exists();
        if(exists){
            String folderName=mapName+"_"+definitionType;
            if(game.gameConfig.playerConfig.getBoolean(mapName+"_"+definitionType,true)&& Gdx.files.local(ResDefaultConfig.Path.ZipFolderPath+folderName+"/").isDirectory()  ){
                return;
            }
            // Gdx.app.log("createMapImage","mapId:"+mapE.get("id")+" is ok");
            int  mapId=mapE.getInt("id");
            // 2.3加载bin
            Fb2Map mapBinDao = getMapDaoByMapId(mapId);
            // 2.4读取map.xml获取地图属性

            // 2.5生成Pixmap
            Pixmap   pixmap=GameMap.createPixmap(mapE.getInt("width"),mapE.getInt("height"),scale);;
            //绘制底纹
            if(ifLand){
                pixmap=GameMap.coverImgByPtimgId(game,pixmap,ResDefaultConfig.Image.DEFAULT_PT);
            }
            //绘图
            pixmap=GameMap.getPixmapByDao(game,mapBinDao,pixmap,pixmapLists,0,0,mapBinDao.mapWidth,mapBinDao.mapHeight,scale);

            // 再由新的 Pixmap 对象生成一个新的 Texture 对象
            /**/if(pixmap!=null) {
                //截取地图为1024*1024并记录和测试 TODO
                //  PixmapIO.writePNG(Gdx.files.external(mapE.get("name")+".png"), pixmap);
                GameMap.splitePixmap(folderName,pixmap,tempPixmap);

                pixmap.dispose();
                game.gameConfig.playerConfig.putBoolean(folderName,true);
                game.gameConfig.playerConfig.flush();
                //Gdx.app.log("地图图片保存", mapName+"_"+definitionType+"成功");
            }else {
                game.gameConfig.playerConfig.putBoolean(folderName,false);
                game.gameConfig.playerConfig.flush();
            }
            //PixmapIO.writePNG(Gdx.files.external(mapE.get("name")+".png"), pixmap);
        }else {
            Gdx.app.error("createMapImage","mapId:"+mapE.get("id")+" is error:"+path);
        }
    }


    public void  cutAllMapImage() throws IOException {
        //1 遍历defmap
        Array<Element> mapEs= game.gameConfig.getDEF_MAP().e.getChildrenByName("map");
        boolean exists;String path;int mapId;
        Fb2Map mapBinDao;  Pixmap  pixmap;Pixmap tempPixmap=null;
        PixmapListDAO pixmapLists=getPixmapListByFileName("pm_tiles",game.getAssetManager());
        String mapName;
        StringWriter stringWriter;
        stringWriter = new StringWriter();
        XmlWriter xmlWriter = new XmlWriter( stringWriter );
        xmlWriter.element("SmallMaps");
        tempPixmap=new Pixmap(ResDefaultConfig.Map.PT_SIDE, ResDefaultConfig.Map.PT_SIDE, Format.RGBA8888);
        float scale;
        String definitionType="hd";
        /*if( game.gameConfig.getIfAnimation()){
            definitionType="hd";
        }else{
            definitionType="sd";
        }*/
        scale=game.getMapScale();
        for(Element mapE:mapEs){
            mapName=mapE.get("name");
            path="bin/"+mapName+".bin";
            exists = Gdx.files.internal(path).exists();

            if(exists){
                if(game.gameConfig.playerConfig.getBoolean(mapName+"_"+definitionType,false)){
                    continue;
                }
                // Gdx.app.log("createMapImage","mapId:"+mapE.get("id")+" is ok");
                mapId=mapE.getInt("id");
                // 2.3加载bin
                mapBinDao = getMapDaoByMapId(mapId);
                // 2.4读取map.xml获取地图属性

                // 2.5生成Pixmap
                pixmap=GameMap.createPixmap(mapE.getInt("width"),mapE.getInt("height"),scale);;
                //绘制底纹
                pixmap=GameMap.coverImgByPtimgId(game,pixmap,ResDefaultConfig.Image.DEFAULT_PT);
                //绘图
                pixmap=GameMap.getPixmapByDao(game,mapBinDao,pixmap,pixmapLists,0,0,mapBinDao.mapWidth,mapBinDao.mapHeight,scale);

                // 再由新的 Pixmap 对象生成一个新的 Texture 对象
                Texture textureMap = new Texture(pixmap, Format.RGBA8888,false);
                if(pixmap!=null) {
                    xmlWriter.element("Maps").attribute("id",mapId).attribute("name",mapName+"_"+definitionType).attribute("width",pixmap.getWidth())
                            .attribute("height",pixmap.getHeight());

                    //截取地图为1024*1024并记录和测试 TODO
                    //  PixmapIO.writePNG(Gdx.files.external(mapE.get("name")+".png"), pixmap);
                    GameMap.splitePixmapAndWriteXml(mapName+"_"+definitionType,pixmap,tempPixmap,xmlWriter);
                    xmlWriter.pop();
                    pixmap.dispose();
                    game.gameConfig.playerConfig.putBoolean(mapName+"_"+definitionType,true);
                    game.gameConfig.playerConfig.flush();
                    Gdx.app.log("地图图片保存", mapName+"_"+definitionType+"成功");
                }else {
                    game.gameConfig.playerConfig.putBoolean(mapName+"_"+definitionType,false);
                    game.gameConfig.playerConfig.flush();
                }
                // break;
            }else {
                Gdx.app.error("createMapImage","mapId:"+mapE.get("id")+" is error:"+path);
            }
        }
        xmlWriter.close();
        FileHandle file = Gdx.files.local( ResDefaultConfig.Path.SmallMapPath );
        file.writeString( stringWriter.toString(), false );
        if(tempPixmap!=null){
            tempPixmap.dispose();
        }

    }



    /*
    //测试xml写出
    public void testXmlWriter(){
        StringWriter stringWriter;
        try{
            stringWriter = new StringWriter();
            //stringWriter.append( "" );
            XmlWriter xmlWriter = new XmlWriter( stringWriter );
            xmlWriter.element("SmallMaps").element("Maps").attribute("id","0").attribute("name","world").attribute("width","3308")
                    .attribute("height","1536");
            //开始写入各个地图的坐标
            xmlWriter.element("map").attribute("n","1.png").attribute("x","0").attribute("y","0").pop()
                    .element("map") .attribute("n","2.png").attribute("x","1000").attribute("y","0").pop();
            xmlWriter.close();
            FileHandle file = Gdx.files.local( ResConfig.Path.SmallMapPath );
            file.writeString( stringWriter.toString(), false );
            System.out.println( stringWriter.toString() );
        } catch(IOException e){
            e.printStackTrace();
        }
    }*/

    public Fb2History getHistory(int historyId){
        Element defHistory =game.gameConfig.getDEF_HISTORY().getElementById(historyId);
        return new Fb2History(getBtByName(defHistory.get("name")));
    }


    //取得分历史数据
    public Fb2History getHistory(int historyId,int year){
        return new Fb2History(getHB(historyId,year));
    }

    private byte[] getHB(int historyId, int year) {
        byte[] bt=null;
        StringBuilder path=new StringBuilder("bin/history_");//"bin/history_"+historyId+"/"+year+".bin"
        path.append(historyId).append("/").append(year).append(".bin");
        if(game.gameConfig.equals("Desktop")||game.gameConfig.equals("HeadlessDesktop")){
            bt = Gdx.files.local(path.toString()).readBytes();
        }else{
            bt = Gdx.files.internal(path.toString()).readBytes();
        }
        return bt;
    }





    public Fb2History getHistoryByPath(String binPath){
        return new Fb2History(getBtByName(binPath));
    }


    public void deleteExternalViewFile(String path) {
        if(game.gameConfig.isExtAvailable){
            FileHandle f=Gdx.files.external(path);
            if(f.exists()){
                f.delete();
            }
        }
    }


    public void saveStagePreview(){
        if(!game.gameConfig.isExtAvailable){
            return;
        }
        Element root = game.gameConfig.getDEF_STAGE().getElementById( game.getStageId());
        String str = root.get("name");
        String path= ResDefaultConfig.Path.ViewFolderPath + str + ".png";
        GameMap.drawLegionPriviewMapBySMapDAO(game.getSMapDAO(),path);
    }


    //TODO 删除所有存档数据,将来用来配合服务器做作弊检查
    public void deleteAllSaveFile(){
        FileHandle f=Gdx.files.local(ResDefaultConfig.Path.SaveFolderPath);
        if(f.isDirectory()){
            f.deleteDirectory();
        }
    }


    public void saveSmapScreenshot(String path) {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);


        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Format.RGBA8888);
        //pixmap=GameMap.coverImgByPtimgId(game,pixmap,ResDefaultConfig.Image.DEFAULT_PT);
        pixmap.setBlending(Pixmap.Blending.None);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        PixmapIO.writePNG(Gdx.files.external(path), pixmap);
        pixmap.dispose();
        // Gdx.app.log("FY", "come here");
    }
}
