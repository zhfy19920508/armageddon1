package com.zhfy.game.model.content.conversion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.framework.tool.FileByte;
import com.zhfy.game.model.content.btl.BtlModule;

import java.io.IOException;
import java.util.HashMap;

public class Fb2History {
    public MasterData masterData;
    public Array<HistoryData> historyDatas;
    public void initFb1History() {
        masterData = new MasterData() ;
        historyDatas = new Array<HistoryData>() ;
    }

    public Fb2History(){
        initFb1History();
    }

    public void setMasterData(MasterData masterData) {

        if (this.masterData==null){this.masterData=new MasterData();}
        this.masterData.setVersion(masterData.getVersion());
        this.masterData.setMapId(masterData.getMapId());
        this.masterData.setYearBegin(masterData.yearBegin);
        this.masterData.setRegionCount(masterData.getRegionCount());
        this.masterData.setYearCount(masterData.getYearCount());
        this.masterData.setHisCount(masterData.hisCount);
    }

    public void addHistoryData(int historyIndex,int year,int regionIndex,int nowCountryIndex,int lastCountryIndex,int getType,int battleId) {
        HistoryData historyData=new HistoryData();
        historyData.setHistoryIndex(historyIndex);
        historyData.setYear(year);
        historyData.setRegionIndex(regionIndex);
        historyData.setNowCountryIndex(nowCountryIndex);
        historyData.setLastCountryIndex(lastCountryIndex);
        historyData.setGetType(getType);
        historyData.setBattleId(battleId);

        historyDatas.add(historyData);
    }

    public void logHistoryInfoByRegionId(int historyPage, int regionId) {
     HistoryData h=   getHistoryDataByPage(historyPage,regionId);
     if(h!=null){
         h.logInfo();
     }
    }

    public void addNewYear() {
        int year=historyDatas.get(masterData.hisCount-1).getYear()+1;
        //Map map=new HashMap();
        for(int i=0;i<masterData.regionCount;i++){
            HistoryData bm1=new HistoryData();
            bm1.setHistoryIndex(masterData.hisCount+i);
            bm1.setYear(year);
            bm1.setRegionIndex(historyDatas.get(masterData.hisCount-(masterData.regionCount-i)).getRegionIndex());
            bm1.setNowCountryIndex(historyDatas.get(masterData.hisCount-(masterData.regionCount-i)).getNowCountryIndex());
            bm1.setLastCountryIndex(historyDatas.get(masterData.hisCount-(masterData.regionCount-i)).getLastCountryIndex());
            bm1.setGetType(historyDatas.get(masterData.hisCount-(masterData.regionCount-i)).getGetType());
            bm1.setBattleId(0);
            historyDatas.add(bm1);
            //map.put(bm1.getBm1_3(),bm1.getBm1_4());
        }
        //regionColors.add(map);
        //
        masterData.setYearCount(masterData.getYearCount()+1);
        //hisCount=hisCount+regionCount;
        masterData.setHisCount(masterData.getHisCount()+ masterData.getRegionCount() );
        Gdx.app.log("history添加新年",year+" i:"+(masterData.getHisCount()- masterData.getRegionCount()));
    }

    public void updHistoryByRegionByPage(int targetPage, int targetRegionId, int sourceRegionId){

        //获取countryId
        int sourceId=getHistoryIndexByPage(targetPage,sourceRegionId);
        int targetId=getHistoryIndexByPage(targetPage,targetRegionId);
        if(targetId==0){
            return;
        }

        int countryId=historyDatas.get(sourceId).getNowCountryIndex();
        //修改bin类
        historyDatas.get(targetId).setNowCountryIndex(countryId);

    }

    public void addCountryByRegionByPage(int targetPage, int targetRegionId, int countryId){

        HistoryData h=getHistoryDataByPage(targetPage,targetRegionId);
       if(h!=null){
           h.setNowCountryIndex(countryId);
           h.setGetType(0);
       }
    }

    public void replaceCountryByRegionByPage(int targetPage, int targetRegionId, int countryId){
        int targetId=getHistoryIndexByPage(targetPage,targetRegionId);
        int sourceCountry=historyDatas.get(targetId).getNowCountryIndex();
        int c;
        for(int i=0;i<masterData.regionCount;i++){
            //c=hisCount-(regionCount-i);
            c=targetPage*masterData.regionCount+i;
            if(historyDatas.get(c).getNowCountryIndex()==sourceCountry){
                historyDatas.get(c).setNowCountryIndex(countryId);
                //Gdx.app.log("replaceCountry","c:"+c+" i:"+i+" targetRegionId:"+targetRegionId);
            }
        }
    }
    public void addTypeByRegionAndPage(int targetPage, int targetRegionId, int type){
        int targetId=getHistoryIndexByPage(targetPage,targetRegionId);
      historyDatas.get(targetId).setGetType(type);
    }
    public void updGetTypeByRegionByPage(int targetPage, int targetRegionId, int tempGetType){
        int targetId=getHistoryIndexByPage(targetPage,targetRegionId);
        if(targetId==0){
            Gdx.app.error("updGetTypeByRegionByPage",targetPage+":"+targetRegionId);
            return;
        }
        //修改bin类
        historyDatas.get(targetId).setGetType(tempGetType);
    }
    public void updBattleByRegionByPage(int targetPage, int targetRegionId, int battle){
        int targetId=getHistoryIndexByPage(targetPage,targetRegionId);
        if(targetId==0){
            Gdx.app.error("updBattleByRegionByPage",targetPage+":"+targetRegionId);
            return;
        }
        //修改bin类
        historyDatas.get(targetId).setBattleId(battle);
    }
    public void addBattleByRegionByPage(int targetPage, int targetRegionId, int battle){
        int targetId=getHistoryIndexByPage(targetPage,targetRegionId);
        historyDatas.get(targetId).setGetType(1);
        historyDatas.get(targetId).setBattleId(battle);
    }

    public void addNewRegion(int newRegion,int sourceRegion) {
        if(!regionSortMap.containsKey(newRegion)&&regionSortMap.containsKey(sourceRegion)&&newRegion>=0&&sourceRegion>=0&&newRegion!=sourceRegion){
            regionSortMap.put(newRegion,masterData.regionCount);
        }else{
            Gdx.app.error("history addNewRegion is error",newRegion+":"+sourceRegion+regionSortMap.containsKey(newRegion)+":"+regionSortMap.containsKey(sourceRegion));
            return;
        }
        HistoryData h,h2;int potion,index=masterData.regionCount;
        for(int i=masterData.yearCount-1;i>=0;i--){
             h=getHistoryDataByPage(i,sourceRegion);
             potion=getHistoryIndexByPage(i,index);
            h2=new HistoryData(h,potion,newRegion);
            historyDatas.insert(potion,h2);
        }

        masterData.setRegionCount(masterData.getRegionCount()+1);
        masterData.resetHisCount();
        masterData.sortHistoryIndex();
    }

    public void removeRegion(int region) {
        if(regionSortMap.containsKey(region)){
            HistoryData h; int index=regionSortMap.get(region);
            for(int i=masterData.yearCount-1;i>=0;i--){
                historyDatas.removeIndex(getHistoryIndexByPage(i,index));
            }
            masterData.setRegionCount(masterData.getRegionCount()-1);
            masterData.resetHisCount();
            masterData.sortHistoryIndex();
            regionSortMap.remove(region);
        }else{
            Gdx.app.error("history removeRegion is error",region+"");
            return;
        }


    }


    public void replaceRegion(int regionId, int replaceId) {
        if(regionSortMap.containsKey(regionId)&&regionId!=replaceId){
            HistoryData h; int index=regionSortMap.get(regionId);
            for(int i=masterData.yearCount-1;i>=0;i--){
                h=getHistoryDataByPage(i,regionId);
                h.setRegionIndex(replaceId);
            }
            regionSortMap.remove(regionId);
            regionSortMap.put(replaceId,index);
        }else{
            Gdx.app.error("history removeRegion is error",regionId+":"+replaceId);
            return;
        }
    }


    //主数据 128
    public class MasterData {
        private int Version;//版本
        public int getVersion(){ return Version;}
        public void setVersion(int Version){ this.Version= ComUtil.limitValue(Version,0,65525);}
        private int mapId;//使用的地图文件
        public int getMapId(){ return mapId;}
        public void setMapId(int mapId){ this.mapId=ComUtil.limitValue(mapId,0,65525);}
        private int yearBegin;//开始年份
        public int getYearBegin(){ return yearBegin;}
        public void setYearBegin(int yearBegin){ this.yearBegin=yearBegin;}
        private int regionCount;//地区总数
        public int getRegionCount(){ return regionCount;}
        public void setRegionCount(int regionCount){ this.regionCount=regionCount;}
        private int yearCount;//记录年条数
        public int getYearCount(){ return yearCount;}
        public void setYearCount(int yearCount){ this.yearCount=yearCount;}
        private int hisCount;//总记录条数
        public int getHisCount(){ return hisCount;}
        public void setHisCount(int hisCount){ this.hisCount=hisCount;}

        public void resetHisCount() {
            setHisCount(regionCount*yearCount);
        }

        public void sortHistoryIndex() {
            for(int i=0,iMax=historyDatas.size;i<iMax;i++){
                HistoryData h=historyDatas.get(i);
                h.setHistoryIndex(i);
            }
        }
    }
    //历史条数
    public class HistoryData {
        private int historyIndex;//历史序列
        public int getHistoryIndex(){ return historyIndex;}
        public void setHistoryIndex(int historyIndex){ this.historyIndex=historyIndex;}
        private int year;//年份
        public int getYear(){ return year;}
        public void setYear(int year){ this.year=ComUtil.limitValue(year,0,65525);}
        private int regionIndex;//地区id
        public int getRegionIndex(){ return regionIndex;}
        public void setRegionIndex(int regionIndex){ this.regionIndex=ComUtil.limitValue(regionIndex,0,65525);}
        private int nowCountryIndex;//当前年份归属国家
        public int getNowCountryIndex(){ return nowCountryIndex;}
        public void setNowCountryIndex(int nowCountryIndex){ this.nowCountryIndex=ComUtil.limitValue(nowCountryIndex,0,65525);}
        private int lastCountryIndex;//战斗后的归属国家
        public int getLastCountryIndex(){ return lastCountryIndex;}
        public void setLastCountryIndex(int lastCountryIndex){ this.lastCountryIndex=ComUtil.limitValue(lastCountryIndex,0,65525);}
        private int getType;//获得方式
        public int getGetType(){ return getType;}
        public void setGetType(int getType){ this.getType=ComUtil.limitValue(getType,0,255);}
        private int battleId;//对战国家
        public int getBattleId(){ return battleId;}
        public void setBattleId(int battleId){ this.battleId=ComUtil.limitValue(battleId,0,65525);}


        public HistoryData(){

        }

        public HistoryData(HistoryData h2,int historyIndex,int regionIndex){
            this.historyIndex=historyIndex;
            this.year=h2.year;
            this.regionIndex=regionIndex;
            this.nowCountryIndex=h2.nowCountryIndex;
            this.lastCountryIndex=h2.lastCountryIndex;
            this.getType=h2.getType;
            this.battleId=h2.battleId;
        }

        public void logInfo() {
            Gdx.app.log(
                    "historyInfo",
                    "historyIndex:"+historyIndex+
                            " year:"+year+
                            " regionIndex:"+regionIndex+
                            " nowCountryIndex:"+nowCountryIndex+
                            " lastCountryIndex:"+lastCountryIndex+
                            " getType:"+getType+
                            " battleId:"+battleId
            );
        }
    }
    //-------------------------------自定义新增变量-----------------------------------
    public HashMap<Integer,Integer> regionSortMap;


    //-------------------------------自定义新增方法-----------------------------------


    //存在的国家:id序列
    public HashMap<Integer,Integer> getCountryTransMap(int year){
        HashMap<Integer,Integer> rs=new HashMap<>();
        rs.put(0,0);//设定中立国为第一个国家
        for(HistoryData h:historyDatas){
            if(h.getYear()==year){
                if(!rs.containsKey(h.getNowCountryIndex())){
                        rs.put(h.getNowCountryIndex(),rs.size());
                }
            }
        }
        return rs;
    }

    public void initRegionSortMap(){
        if(regionSortMap==null){
            regionSortMap = new HashMap();
            for (int i = 0; i < masterData.regionCount; i++) {
                regionSortMap.put(historyDatas.get(i).getRegionIndex(), i);
                //Gdx.app.log("RegionSortMap",bin.getBm1().get(i).getBm1_3()+":"+i);
            }
        }/*else{
            regionSortMap.clear();
        }*/

    }

    public int getHistoryIndexByPage(int historyPage, int index) {

        return  index + historyPage * masterData.regionCount;
    }
    public int getHistoryIndex(int year, int index) {
        int rs = 0;
        int historyPage=year-masterData.yearBegin;
        /*if(regionSortMap==null){
            initRegionSortMap();
        }*/
        rs = index + historyPage * masterData.regionCount;

        /*try {
            rs = regionSortMap.get(RegionId) + historyPage * masterData.regionCount;
        } catch (Exception e) {
            Gdx.app.error("获取historyId出错1", "historyPage:" + historyPage + " RegionId:" + RegionId);
        }*/
        return rs;
    }

    public int getHistoryIndexBySort(int year, int regionId) {
        int rs = -1;
        int historyPage=year-masterData.yearBegin;
        if(regionSortMap==null){
            initRegionSortMap();
        }


       try {
            rs = regionSortMap.get(regionId) + historyPage * masterData.regionCount;
        } catch (Exception e) {
           if(ResDefaultConfig.ifDebug){
               e.printStackTrace();
           }
            Gdx.app.error("获取historyId出错2", "historyPage:" + historyPage + " RegionId:" + regionId);
        }
        return rs;
    }


    public Fb2History(byte[] bt){
        initFb1History();
        int line = 0;
        StringBuilder buf = new StringBuilder();
        for (byte d : bt) {if (line % 1 == 0) {buf.append(String.format("%02x", d));line++; } }
        int sum=0;int h=0;int w=0;int tag=0;  int c=0;
        this.masterData.Version= GameUtil.getCoverStr(buf,tag,2); tag+=2;
        this.masterData.mapId= GameUtil.getCoverStr(buf,tag,4); tag+=4;
        this.masterData.yearBegin= GameUtil.getCoverStr(buf,tag,4); tag+=4;
        this.masterData.regionCount= GameUtil.getCoverStr(buf,tag,4); tag+=4;
        this.masterData.yearCount= GameUtil.getCoverStr(buf,tag,4); tag+=4;
        this.masterData.hisCount= GameUtil.getCoverStr(buf,tag,8); tag+=8;
        sum=w*h;
        c=masterData.hisCount;for(int i=0;i<c;i++){
            HistoryData historyData=new HistoryData();
            historyData.setHistoryIndex(GameUtil.getCoverStr(buf,tag,8)); tag+=8;
            historyData.setYear(GameUtil.getCoverStr(buf,tag,4)); tag+=4;
            historyData.setRegionIndex(GameUtil.getCoverStr(buf,tag,8)); tag+=8;
            historyData.setNowCountryIndex(GameUtil.getCoverStr(buf,tag,4)); tag+=4;
            historyData.setLastCountryIndex(GameUtil.getCoverStr(buf,tag,4)); tag+=4;
            historyData.setGetType(GameUtil.getCoverStr(buf,tag,2)); tag+=2;
            historyData.setBattleId(GameUtil.getCoverStr(buf,tag,4)); tag+=4;
            this.historyDatas.add(historyData);
        }
        bt=null;buf=null; //initForRandomGeneral();
    }
    public void  save(String path){
        this.masterData.hisCount=historyDatas.size;
        try {  FileByte out = new FileByte();
            out.write(masterData.Version,2);
            out.write(masterData.mapId,4);
            out.write(masterData.yearBegin,4);
            out.write(masterData.regionCount,4);
            out.write(masterData.yearCount,4);
            out.write(masterData.hisCount,8); for(int i=0,iMax=historyDatas.size;i<iMax;i++){
                out.write(historyDatas.get(i).getHistoryIndex(),8);
                out.write(historyDatas.get(i).getYear(),4);
                out.write(historyDatas.get(i).getRegionIndex(),8);
                out.write(historyDatas.get(i).getNowCountryIndex(),4);
                out.write(historyDatas.get(i).getLastCountryIndex(),4);
                out.write(historyDatas.get(i).getGetType(),2);
                out.write(historyDatas.get(i).getBattleId(),4);
            }
            FileHandle file = Gdx.files.local(path); file.writeBytes(out.getByte(), false);
        } catch (Exception e) {
            GameUtil.recordLog("Fb2History save",e);
            Gdx.app.error("Fb2History save","is error");
        } System.out.println("Ok");
    }
    public HistoryData getHistoryDataByPage(int page, int region){
        int year=masterData.getYearBegin()+page;
        return getHistoryData(year,region);
    }

    public HistoryData getHistoryData(int year, int region){
        //year 为0,或year不在范围,则取year最靠前的年代
        if(year==0||year<masterData.yearBegin||year>masterData.getYearBegin()+masterData.getYearCount()){
           return null;
        }
        /*initRegionSortMap();
        Integer r=regionSortMap.get(region);
        HistoryData h=null;
        if(r!=null){
           h=historyDatas.get( getHistoryIndex(year,r));
        }else{
            Gdx.app.log("regionSortMap ","not region:"+region);
        }*/

        int index = getHistoryIndexBySort(year, region);
        if(index!=-1) {
            return historyDatas.get(index);
        }else{
            return null;
        }
    }


    //获取某个年代的历史集

    public Array<HistoryData> getHistoryDatas(int country,int year,Array rs){
        if(rs==null){
            rs=new Array();
        }else {
            rs.clear();
        }
        //year 为0,或year不在范围,则取year最靠前的年代
        if(year==0||year<masterData.yearBegin||year>masterData.getYearBegin()+masterData.getYearCount()){
            for(HistoryData h:historyDatas){
                if(h.getNowCountryIndex()==country){
                    year=h.getYear();
                    break;
                }
            }
        }
        for(int i=0;i<masterData.regionCount;i++){
            HistoryData h=historyDatas.get( getHistoryIndex(year,i));
            if(h.getNowCountryIndex()==country){
                rs.add(h);
            }
        }
        return rs;
    }



    //更新游戏数据
    public void  updHistoryData(Fb2History oldHistoryData, Fb2Map oldMapBinDAO, Fb2Map newMapBinDAO){
        HistoryData newHis,oldHis;
        if(masterData.regionCount!=newMapBinDAO.getRegionCount()){
            Gdx.app.error("updHistoryData",masterData.regionCount+":"+newMapBinDAO.getRegionCount());
            return;
        }

        for(int year=0,yearMax=masterData.yearCount,riMax=masterData.regionCount,ri;year<yearMax;year++){
            for( ri=0;ri<riMax;ri++){
                newHis=getHistoryDataByPage(year,newMapBinDAO.regionDatas.getByIndex(ri).getRegion());
                oldHis=oldHistoryData.getHistoryDataByPage(year,oldMapBinDAO.getRegionById(newHis.getRegionIndex()));
                if(oldHis!=null){
                    newHis.setNowCountryIndex(oldHis.getNowCountryIndex());
                    newHis.setLastCountryIndex(oldHis.getLastCountryIndex());
                    newHis.setGetType(oldHis.getGetType());
                    newHis.setBattleId(oldHis.getBattleId());
                }

            }
        }
    }

    //只获取当前year的信息



    public void saveNowYearHistoryBins(MainGame game,int historyId,int year) {
       /* try {
           // BTLTooL.saveBtl(game,getBTL(year), ResDefaultConfig.Rules.RULE_FB2_HISTORY,"bin/history_"+historyId+"/"+year+".bin"); //BTLDAO btl,StringName rulePath,  StringName filePath

        } catch (IOException e) {
            e.printStackTrace();
            Gdx.app.error("history","保存失败:"+historyId);
        }*/
        Fb2History h=new Fb2History();
        h.masterData.setVersion(this.masterData.getVersion());
        h.masterData.setMapId(this.masterData.getVersion());
        h.masterData.setYearBegin(year);
        h.masterData.setRegionCount(this.masterData.getRegionCount());
        h.masterData.setYearCount(1);
        h.masterData.setHisCount(this.masterData.getRegionCount());



        for(int i=0,iMax=historyDatas.size;i<iMax;i++){
            HistoryData oh=historyDatas.get(i);
            if(oh.getYear()==year){
                HistoryData nh=new HistoryData();
                nh.setHistoryIndex(h.historyDatas.size);
                nh.setYear(year);
                nh.setRegionIndex(oh.getRegionIndex());
                nh.setNowCountryIndex(oh.getNowCountryIndex());
                nh.setLastCountryIndex(oh.getLastCountryIndex());
                nh.setGetType(oh.getType);
                nh.setBattleId(oh.getBattleId());
                h.historyDatas.add(nh);
            }
        }
        h.save("bin/history_"+historyId+"/"+year+".bin");
    }


    public void saveAllYearHistoryBins(MainGame game,int historyId) {
        for(int i=0;i<masterData.getYearCount();i++){
            saveNowYearHistoryBins(game,historyId,masterData.getYearBegin()+i);
        }
    }


    public void transBtlModuleByMasterData(MasterData masterData, BtlModule bm){
        bm.setMode(200);
        bm.setBm1(masterData.Version);
        bm.setBm2(masterData.mapId);
        bm.setBm3(masterData.yearBegin);
        bm.setBm4(masterData.regionCount);
        bm.setBm5(masterData.yearCount);
        bm.setBm6(masterData.hisCount);
    }
    public void transMasterDataByBtlModule(BtlModule bm,MasterData masterData){
        masterData.Version=bm.getBm1();
        masterData.mapId=bm.getBm2();
        masterData.yearBegin=bm.getBm3();
        masterData.regionCount=bm.getBm4();
        masterData.yearCount=bm.getBm5();
        masterData.hisCount=bm.getBm6();
    }
    public void transBtlModuleByHistoryData(HistoryData historyData,BtlModule bm){
        bm.setMode(201);
        bm.setBm1(historyData.historyIndex);
        bm.setBm2(historyData.year);
        bm.setBm3(historyData.regionIndex);
        bm.setBm4(historyData.nowCountryIndex);
        bm.setBm5(historyData.lastCountryIndex);
        bm.setBm6(historyData.getType);
        bm.setBm7(historyData.battleId);
    }
    public void transHistoryDataByBtlModule(BtlModule bm,HistoryData historyData){
        historyData.historyIndex=bm.getBm1();
        historyData.year=bm.getBm2();
        historyData.regionIndex=bm.getBm3();
        historyData.nowCountryIndex=bm.getBm4();
        historyData.lastCountryIndex=bm.getBm5();
        historyData.getType=bm.getBm6();
        historyData.battleId=bm.getBm7();
    }
}