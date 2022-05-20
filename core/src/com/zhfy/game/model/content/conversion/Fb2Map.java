package com.zhfy.game.model.content.conversion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.framework.GameMethod;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.framework.tool.FileByte;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.XmlIntDAO;
import com.zhfy.game.model.content.XmlStringDAO;
import com.zhfy.game.model.content.ZHIntMap;
import com.zhfy.game.model.content.btl.BtlModule;
import com.zhfy.game.model.framework.Coord;
import com.zhfy.game.model.framework.TextureRegionDAO;

public class Fb2Map {

    public int mapId;
    public int mapVersion;// 2-地图版本
    public int mapWidth;// 4-长
    public int mapHeight;// 4-宽
    public int regionCount;
    public int ifLat;//是否开启纬度功能,0关闭 1世界维度 2局部纬度
    //纬度60以上极圈 40-20 温带 20-0 热带
    public int lat60N;//北纬60
    public int lat40N;//北纬40
    public int lat20N;//北纬20
    public int lat0;//赤道
    public int lat20S;//南纬20
    public int lat40S;//南纬40
    public int lat60S;//南纬60
    public int topLat;//局部纬度时地图顶端纬度
    public int footLat;//局部纬度时地图底端纬度
    public Fb2Smap btl;

    public Array<MapHexagon> mapHexagons;//
    public ZHIntMap<MapRegion> regionDatas;

    public XmlReader.Element mapE;
    public IntIntMap regionMap;//{region:i}
    public Texture floorImage;
    public float floorHexagonAlphaRate;
    public Texture mapImage;
    public int mapRefX;
    public int mapRefY;
    public float alphaRate;
    public boolean onlyZoomShow;//0 只在缩放图加载 1完全使用
    private IntArray coastGrid;//海陆地块
    //  public IntArray regionGrid;//存放所有region3 除了负数
    public IntArray tempIntArray;
    public IntIntMap tempIntIntMap;

    //存放寻路节点
    private IntArray openList;
    private IntArray closeList;
    private IntArray ids;

    //寻路算法使用的变量
    private int beginX, beginY, endX, endY, tempIndex;
    private boolean isFind;
    //private HexagonDAO hexagons;//寻路算法
    private MainGame game;

    public Fb2Map() {
    }


    public Fb2Map(MainGame game, int mapId, byte[] bt) {
        this.game = game;
        this.mapId = mapId;
        coastGrid = new IntArray();
        //regionGrid = new IntArray();
        XmlStringDAO ruleE = game.gameConfig.getRULE_MAPBIN();
        int line = 0;
        StringBuilder buf = new StringBuilder();
        for (byte d : bt) {
            if (line % 1 == 0) {
                buf.append(String.format("%02x", d));
                line++;
            }
        }
        int sum = 0;
        int h = 0;
        int w = 0;
        int tag = 0;
        //   XmlReader.Element tempRE;
        Array<XmlReader.Element> re;
        int c = 0;
        //bm0 初始数据获取
        {
            // tempRE= ruleE.getElementById("bm0");
            re = ruleE.getElementById("bm0").getChildrenByName("bm");
            this.mapVersion = GameUtil.getCoverStr(buf, tag, 4);
            tag += 4;
            this.mapWidth = GameUtil.getCoverStr(buf, tag, 4);
            tag += 4;
            w = mapWidth;
            this.mapHeight = GameUtil.getCoverStr(buf, tag, 4);
            tag += 4;
            h = mapHeight;
            sum = w * h;

            this.regionCount = GameUtil.getCoverStr(buf, tag, 8);
            tag += 8;


            this.mapHexagons = new Array<>();
        }
        re = ruleE.getElementById("bm1").getChildrenByName("bm");
        c = sum;
        for (int i = 0; i < c; i++) {
            MapHexagon mapHexagon = new MapHexagon();
            mapHexagon.setBlockType(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setBackTile(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setBackIdx(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setBackRefX(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setBackRefY(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setForeTile(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setForeIdx(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setForeRefX(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setForeRefY(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setPresetRailway(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.ifCoast = (GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setRegionId(GameUtil.getCoverStr(buf, tag, 8));
            tag += 8;
            mapHexagon.setHexagonIndex(i);


            this.mapHexagons.add(mapHexagon);
           /* if (!regionGrid.contains(mapHexagon.getRegionId()) && mapHexagon.getRegionId() >= 0) {
                regionGrid.add(mapHexagon.getRegionId());
            }*/
        }
        regionDatas = new ZHIntMap<>();
        for (int i = 0; i < regionCount; i++) {
            MapRegion mapRegion = new MapRegion();
            mapRegion.setRegion(GameUtil.getCoverStr(buf, tag, 8));
            tag += 8;
            mapRegion.setClimateZone(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapRegion.setDepLv(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapRegion.setMineralLv(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapRegion.setFoodLv(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapRegion.setOilLv(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapRegion.setAreaId(GameUtil.getCoverStr(buf, tag, 4));
            tag += 4;
            mapRegion.setStrategicRegion(GameUtil.getCoverStr(buf, tag, 4));
            tag += 4;
            regionDatas.put(mapRegion.getRegion(), mapRegion);
        }

        this.coastGrid = getIdsByAround(12);
        buf = null;
        bt = null;
        init();
    }


    public int getMapVersion() {
        return mapVersion;
    }

    public void setMapVersion(int mapVersion) {
        this.mapVersion = mapVersion;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public Array<MapHexagon> getMapbin() {
        return mapHexagons;
    }

    public void setMapbin(Array<MapHexagon> MapBin) {
        this.mapHexagons = MapBin;
    }

    public void initRegionId() {
        int i, iMax;
        iMax = mapHexagons.size;
        for (i = 0; i < iMax; i++) {
            mapHexagons.get(i).setRegionId(i);
        }
    }

    //随机装饰(除特殊装饰,以及ref有值的外)  
    public void randomDecoration(int type) {
        Random rand = new Random();
        //储存地块最高范围
        Map map = GameMap.getDecorateRandMaxMap(game); //Map(id_type+"_min",idx_min) Map(id_type+"_max",idx_max)
        //循环遍历所有地块
        int i;
        int iLength = mapHexagons.size;
        int vMin, vMax;
        for (i = 0; i < iLength; i++) {
            /*
             * if(mapBins.get(i).getBackTile()==5) { Gdx.app.log("mapBins.i:",
             * "imin:"+(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).
             * getBlockType()+"_min")+" imax:"+(mapBins.get(i).getBackTile()+"_"+
             * mapBins.get(i).getBlockType()+"_max")); }
             */
            if (mapHexagons.get(i).getBackTile() == 11) {
                continue;
            }

            if ((type == 0 || type == 1) && map.containsKey(mapHexagons.get(i).getBackTile() + "_" + mapHexagons.get(i).getBlockType() + "_min") && map.containsKey(mapHexagons.get(i).getBackTile() + "_" + mapHexagons.get(i).getBlockType() + "_max") && mapHexagons.get(i).getBackRefX() == 0 && mapHexagons.get(i).getBackRefY() == 0) {

                vMin = (Integer) map.get(mapHexagons.get(i).getBackTile() + "_" + mapHexagons.get(i).getBlockType() + "_min");
                vMax = (Integer) map.get(mapHexagons.get(i).getBackTile() + "_" + mapHexagons.get(i).getBlockType() + "_max");
                //Gdx.app.log("", " backTile:"+mapBins.get(i).getBackTile()+" type:"+mapBins.get(i).getBackTile()+" max:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_max")+" min:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_min")+" backIdx:"+backIdx);
                mapHexagons.get(i).setBackIdx(rand.nextInt(vMax - vMin + 1) + vMin);
            }
            if ((type == 0 || type == 2) && map.containsKey(mapHexagons.get(i).getForeTile() + "_" + mapHexagons.get(i).getBlockType() + "_min") && map.containsKey(mapHexagons.get(i).getForeTile() + "_" + mapHexagons.get(i).getBlockType() + "_max") && mapHexagons.get(i).getForeRefX() == 0 && mapHexagons.get(i).getForeRefY() == 0) {
                vMin = (Integer) map.get(mapHexagons.get(i).getForeTile() + "_" + mapHexagons.get(i).getBlockType() + "_min");
                vMax = (Integer) map.get(mapHexagons.get(i).getForeTile() + "_" + mapHexagons.get(i).getBlockType() + "_max");
                //Gdx.app.log("", " backTile:"+mapBins.get(i).getBackTile()+" type:"+mapBins.get(i).getBackTile()+" max:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_max")+" min:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_min")+" backIdx:"+backIdx);
                mapHexagons.get(i).setForeIdx(rand.nextInt(vMax - vMin + 1) + vMin);
            }
        }
    }

    //随机增加2层装饰
    public void checkDecoration() {
        int foreId, vMin, vMax;
        Random rand = new Random();
        //储存地块最高范围
        Map map = GameMap.getDecorateRandMaxMap(game);
        //循环遍历所有地块
        int i, type, y;
        int iLength = mapHexagons.size;
        XmlReader.Element xmlE;
        Object o1;
        MapHexagon m;
        for (i = 0; i < iLength; i++) {
            //配对规则
            //4丘陵  6
            //5山地  6
            //6森林  6
            m = mapHexagons.get(i);
            y = GameMap.getHY(i, mapWidth);
            if (((ifLat == 1 && y < lat60N) || (ifLat == 1 && y > lat60S) || m.getRegionData().getClimateZone() == 11 || m.getRegionData().getClimateZone() == 6)) {
                if (m.getBackTile() == 6) {
                    m.setBlockType(4);
                    vMin = (Integer) map.get(m.getBackTile() + "_" + m.getBlockType() + "_min");
                    vMax = (Integer) map.get(m.getBackTile() + "_" + m.getBlockType() + "_max");
                    //Gdx.app.log("", " backTile:"+mapBins.get(i).getBackTile()+" type:"+mapBins.get(i).getBackTile()+" max:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_max")+" min:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_min")+" backIdx:"+backIdx);
                    m.setBackIdx(rand.nextInt(vMax - vMin + 1) + vMin);
                }
               /* if(m.getForeTile()==6){
                    vMin =  (Integer) map.get(m.getForeTile() + "_" + m.getBlockType() + "_min");
                    vMax = (Integer) map.get( m.getForeTile() + "_" + m.getBlockType() + "_max");
                    //Gdx.app.log("", " backTile:"+mapBins.get(i).getBackTile()+" type:"+mapBins.get(i).getBackTile()+" max:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_max")+" min:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_min")+" backIdx:"+backIdx);
                    m.setForeIdx(rand.nextInt(vMax - vMin + 1) + vMin);
                }*/
            }
            /*if(y>lat60N&&y<lat40S&&m.getBackTile()==5&&m.getBlockType()==8&&ComUtil.ifGet(60)){
                m.setBlockType(5);
                vMin =  (Integer) map.get(m.getBackTile() + "_" + m.getBlockType() + "_min");
                vMax = (Integer) map.get( m.getBackTile() + "_" + m.getBlockType() + "_max");
                //Gdx.app.log("", " backTile:"+mapBins.get(i).getBackTile()+" type:"+mapBins.get(i).getBackTile()+" max:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_max")+" min:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_min")+" backIdx:"+backIdx);
                m.setBackIdx(rand.nextInt(vMax - vMin + 1) + vMin);
            }*/
            xmlE = game.gameConfig.getDEF_TERRAINIMG_XMLE(m.getBackTile(), m.getBackIdx());


            if (m.getBackTile() == 11) {
                continue;
            } else if (xmlE != null) {
                type = xmlE.getInt("type", m.getBlockType());
                switch (m.getBackTile()) {
                    case 4:
                        m.setBlockType(type);
                        break;
                    case 5:
                        m.setBlockType(type);
                        break;
                    case 6:
                        m.setBlockType(type);
                        break;
                    case 7:
                        m.setBlockType(type);
                        break;
                    case 8:
                        m.setBlockType(type);
                        break;
                    case 9:
                        m.setBlockType(type);
                        break;
                }
            }

            if (rand.nextInt(100) < 31 && map.containsKey(m.getBackTile() + "_" + m.getBlockType() + "_min") && map.containsKey(m.getBackTile() + "_" + m.getBlockType() + "_max") && m.getBackTile() > 3 && m.getBackTile() < 7) {


                // foreId = 4 + rand.nextInt(3);
                foreId = 6;
                m.setForeTile(foreId);
                o1 = map.get(foreId + "_" + m.getBlockType() + "_min");
                if (o1 != null && m.getForeRefX() == 0 && m.getForeRefY() == 0) {
                    vMin = (Integer) map.get(foreId + "_" + m.getBlockType() + "_min");
                    vMax = (Integer) map.get(foreId + "_" + m.getBlockType() + "_max");
                    //Gdx.app.log("", " backTile:"+mapBins.get(i).getBackTile()+" type:"+mapBins.get(i).getBackTile()+" max:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_max")+" min:"+map.get(mapBins.get(i).getBackTile()+"_"+mapBins.get(i).getBlockType()+"_min")+" backIdx:"+backIdx);
                    m.setForeIdx(rand.nextInt(vMax - vMin + 1) + vMin);
                }
            } else {
                m.setForeTile(0);
                m.setForeIdx(0);
                m.setForeRefX(0);
                m.setForeRefY(0);
            }
        }
    }


    //规则 有建筑或有地名的区块=id 否则为0
    public void resetRegionId() {
        for (int i = regionDatas.size() - 1; i >= 0; i--) {
            MapRegion region = regionDatas.getByIndex(i);
            if (region != null) {
                if (region.getAreaId() == 0 && region.getOilLv() == 0 && region.getMineralLv() == 0 && region.getDepLv() == 0 && region.getFoodLv() == 0) {
                    mapHexagons.get(i).setRegionId(-1);
                } else {
                    mapHexagons.get(i).setRegionId(i);
                }
            }
        }
    }

    //根据纬度生成随机气候 TODO
    public void randomClimate() {

    }

    //随机海岸河流 TODO
    public void randomCoastRever() {

    }

    //完全随机地图 TODO
    public void randomAll(int i) {

    }

    //重新切割省区
    // chance为随机取地块的概率 最高获取概率,最低默认为10 如果不容许有海洋地块,则多抽0.1
    public void cutRegion() {
        int chance = 5;
        Random rand = new Random();
        int tempId, exct = 0, i = 0, iMax = 0, j, jMax, rsct, riSize;
        IntArray regions = null;
        IntArray temps = null;
        Map regionCountMap = new HashMap();
        Boolean ifSea = false;

        for (j = 0; j < 3; j++) {
            exct = 0;
            regions = getRegions(j);
            Gdx.app.log("阶段1", "核心地块数" + regions.size + " 获得地块次:" + j + " 地块:" + regions.toString()); //循环 挨个对核心地块进行循环
            while (exct < 20 - j && regions.size != 0) {
                for (i = regions.size - 1; i >= 0; i--) {
                    tempId = getRandomRegionId(regions.get(i));
                    if (!ifSea) {//判断海洋,移除
                        if (tempId >= 0 && mapHexagons.get(tempId).getBlockType() == 1) {
                            tempId = -1;
                        }
                    }
                    if (tempId >= 0) {
                        /*riSize = getIdByRegion(tempId).size();
                        if (riSize > (13 + Math.random() * 5)) {
                            regions.remove(i);
                        }else */
                        if (ifCoordDistance(tempId, regions.get(i), 5)) {//判断坐标的位置是否大于x>5或y>5
                            regions.removeIndex(i);
                        } else {
                            mapHexagons.get(tempId).setRegionId(regions.get(i));
                        }
                    } else {
                        regions.removeIndex(i);
                    }
                }
                Gdx.app.log("阶段2", "核心地块数" + regions.size + " 循环次数:" + exct);
                exct++;
            }
        }
        //循环2 自定义随机地块 // 随机一定条件取核心值
        //循环挨个核心地块循环 //如果核心地块周围有非核心地块的空地且面积不超过一定值,则染色(条件可放宽),否则移除,直到移除全部地块
        {
            regions = getRegionIdsByChance((int) chance, ifSea);
            rsct = getRegionForIdIs0();
            Gdx.app.log("阶段3", "剩余绘制数" + rsct + " regions:" + regions.toString());
            for (i = 0; i < regions.size; i++) {
                int rg = regions.get(i);
                if (!ifSea && mapHexagons.get(rg).getBlockType() == 1) {

                } else {
                    mapHexagons.get(rg).setRegionId(rg);
                }

            }
        }


        int rgc = 0;//可用地块
        int l = 0;
        while (rsct > 0) {//空白地块不能等于0
            jMax = regions.size;
            if (jMax == 0) {
                break;
            }
            for (j = jMax - 1; j >= 0; j--) {//regions.get(j)为当前地块值
                //定义当前地块为特定地块 //循环填充周围地块
                iMax = (int) (10 + Math.random() * 10);
                for (i = 0; i < iMax; i++) {
                    rgc = getRandomRegionId(regions.get(j));
                    if (rgc == -1) {
                        regions.removeIndex(regions.get(j));
                        break;
                    } else {
                        mapHexagons.get(rgc).setRegionId(regions.get(j));
                    }
                }
            }
            l++;
            rsct = getRegionForIdIs0();
            Gdx.app.log("阶段3", "剩余绘制数" + rsct + " 核心地块数:" + regions.size);
            if (regions.size < 10 && l > 20) {
                //如果绘制核心小于10,则重新获得绘制核心
                regions = getRegionIdsByChance((int) chance, ifSea);
                l = 0;
            }
        }

        /* 自检,合并不规范地块 */
        {
            //0查看所有region为0的地块并合并到周围地块
            {
                regions = getIdsForBlankRegion();
                for (i = 0; i < regions.size; i++) {//rsCoords.size()  rand.nextInt(
                    Integer region = regions.get(i);
                    temps = getAroundIdById(region, 9, null);
                    if (temps.size != 0) {
                        mapHexagons.get(region).setRegionId(temps.get(rand.nextInt(temps.size)));
                    } else {
                        //此时剩下的应该是碎小的岛屿或水洞
                        temps = getAroundIdById(region, 0, null);
                        if (temps.size != 0) {
                            mapHexagons.get(region).setRegionId(temps.get(rand.nextInt(temps.size)));
                        } else {
                            Gdx.app.log("警告:有空白地块无法合并到周围地块:", region + "");
                        }

                    }
                }
            }
            //对所有region数量进行检查
            //Map.Entry entry;
            int rgct, argId;
            IntArray argIds, regionCounts;
            {
                checkRegion(false);

                regionCountMap = getRegionCountMap();
                regionCounts = ComUtil.getKeyByMap(regionCountMap);
                for (int o = 0; o < regionCounts.size; o++) {
                    Integer rgId = regionCounts.get(o);
                    if (regionCountMap.containsKey(rgId) && regionCountMap.get(rgId) != null) {
                        rgct = Integer.parseInt(regionCountMap.get(rgId).toString());
                        if (rgct < 7) {
                            if (mapHexagons.get(rgId).getRegionMineralLv() == 1) {
                                //0:低于5的城市合并周围的最低地块普通或建筑地块(小于5)
                                argIds = getAroundRegionId(rgId);//获取周围地块
                                for (int a = 0; a < argIds.size; a++) {
                                    Integer tarId = argIds.get(a);
                                    if (Integer.parseInt(regionCountMap.get(tarId).toString()) < 6 && mapHexagons.get(tarId).getRegionMineralLv() != 1 && mapHexagons.get(tarId).getBlockType() != 1) {
                                        updateRegionIds(rgId, tarId);
                                        regionCountMap.put(rgId, (Integer.parseInt(regionCountMap.get(tarId).toString()) + Integer.parseInt(regionCountMap.get(rgId).toString())));
                                        regionCountMap.remove(tarId);
                                        break;
                                    }
                                }
                            } else if (mapHexagons.get(rgId).getRegionMineralLv() == 4) {
                                if (ifSea) {//如果容许海洋,则合并
                                    //1:低于5的海港合并周围的最低海洋地块(小于5)
                                    argIds = getAroundRegionId(rgId);//获取周围地块
                                    for (int a = 0; a < argIds.size; a++) {
                                        Integer tarId = argIds.get(a);
                                        if (Integer.parseInt(regionCountMap.get(tarId).toString()) < 6 && mapHexagons.get(tarId).getRegionMineralLv() != 4 && mapHexagons.get(tarId).getBlockType() == 1) {
                                            updateRegionIds(rgId, tarId);
                                            regionCountMap.put(rgId, (Integer.parseInt(regionCountMap.get(tarId).toString()) + Integer.parseInt(regionCountMap.get(rgId).toString())));
                                            regionCountMap.remove(tarId);
                                            break;
                                        }
                                    }
                                } else {
                                    //否则把海港归到最近的地块
                                    updHabourForE();
                                }
                            } else if (mapHexagons.get(rgId).getBlockType() != 1 && mapHexagons.get(rgId).getRegionMineralLv() == 0) {
                                //2:低于5的陆地地块合并到周围地块数量最低的陆地地块
                                argIds = getAroundRegionId(rgId);//获取周围地块
                                for (int a = 0; a < argIds.size; a++) {
                                    Integer tarId = argIds.get(a);
                                    if (Integer.parseInt(regionCountMap.get(tarId).toString()) < 6 && mapHexagons.get(tarId).getRegionMineralLv() != 4 && mapHexagons.get(tarId).getBlockType() != 1) {
                                        updateRegionIds(rgId, tarId);
                                        regionCountMap.put(rgId, (Integer.parseInt(regionCountMap.get(tarId).toString()) + Integer.parseInt(regionCountMap.get(rgId).toString())));
                                        regionCountMap.remove(tarId);
                                        break;
                                    }
                                }

                            } else if (mapHexagons.get(rgId).getBlockType() == 1 && ifSea) {
                                //3:低于5的海洋地块合并到周围地块数量最低的海洋地块

                                argIds = getAroundRegionId(rgId);//获取周围地块
                                for (int a = 0; a < argIds.size; a++) {
                                    Integer tarId = argIds.get(a);
                                    if (Integer.parseInt(regionCountMap.get(tarId).toString()) < 6 && mapHexagons.get(tarId).getRegionMineralLv() != 4 && mapHexagons.get(tarId).getBlockType() == 1) {
                                        updateRegionIds(rgId, tarId);
                                        regionCountMap.put(rgId, (Integer.parseInt(regionCountMap.get(tarId).toString()) + Integer.parseInt(regionCountMap.get(rgId).toString())));
                                        regionCountMap.remove(tarId);
                                        break;
                                    }
                                }
                            } else if (mapHexagons.get(rgId).getBlockType() != 1 && mapHexagons.get(rgId).getRegionMineralLv() != 1) {
                                //4:低于5的非城市建筑陆地地块合并到最近的城市地块

                                argIds = getAroundRegionId(rgId);//获取周围地块
                                for (int a = 0; a < argIds.size; a++) {
                                    Integer tarId = argIds.get(a);
                                    if (Integer.parseInt(regionCountMap.get(tarId).toString()) < 6 && mapHexagons.get(tarId).getRegionMineralLv() != 4 && mapHexagons.get(tarId).getBlockType() != 1) {
                                        updateRegionIds(rgId, tarId);
                                        regionCountMap.put(rgId, (Integer.parseInt(regionCountMap.get(tarId).toString()) + Integer.parseInt(regionCountMap.get(rgId).toString())));
                                        regionCountMap.remove(tarId);
                                        break;
                                    }
                                }

                            } else {
                                Gdx.app.log("警告:未检测到的地块:", rgId + "");
                            }
                        }
                    }
                }
            }/**/


            //0查看所有孤岛类地块并合并到周围地块
            {
                //自动合并孤岛类地块
                mergeIslandGridByRegion();
                regions = getIdsForBlankRegion();
                for (i = 0; i < regions.size; i++) {//rsCoords.size()  rand.nextInt(
                    Integer region = regions.get(i);
                    if (mapHexagons.get(region).getBlockType() == 1) {
                        temps = getAroundIdById(region, 3, null);
                    } else {
                        temps = getAroundIdById(region, 4, null);
                    }
                    if (temps.size != 0) {
                        mapHexagons.get(region).setRegionId(temps.get(rand.nextInt(temps.size)));
                    } else {
                        Gdx.app.log("警告:有空白地块无法合并到周围地块:", region + "");
                    }
                }
            }
            {//如果不需要海则清除
                if (!ifSea) {
                    for (MapHexagon mapbin : mapHexagons) {
                        if (mapbin.getBlockType() == 1 && mapbin.getRegionMineralLv() != 4) {
                            mapbin.setRegionId(-1);
                        }
                    }
                }
            }
            Gdx.app.log("执行完成", "");
        }
    }


    //重新切割省区 
    public void cutRegionForE(int maxC) {
        Random rand = new Random();
        int tempId, exct = 0, i = 0, iMax = 0, j, jMax, rsct, riSize;
        IntArray regions = null;
        IntArray temps = null;
        Map regionCountMap = new HashMap();

        {
            exct = 0;
            regions = getRegions(1);
            Gdx.app.log("阶段1", "核心地块数" + regions.size + "获得地块类:" + 1); //循环 挨个对核心地块进行循环
            while (exct < maxC && regions.size != 0) {
                for (i = regions.size - 1; i >= 0; i--) {
                    tempId = getRegionIdForE(regions.get(i));
                    if (tempId >= 0) {
                        mapHexagons.get(tempId).setRegionId(regions.get(i));
                    } else {
                        regions.removeIndex(i);
                    }
                }
                Gdx.app.log("阶段2", "核心地块数" + regions.size + " 循环次数:" + exct);
                exct++;
            }
        }


        //打印空白陆地地块和海洋有港口的地块
        // logNoSeaBlankGrid();
    }

    //把region是target的修改为 rgId
    private void updateRegionIds(Integer rgId, Integer targetId) {
        //交换前检查合不合法
        if (mapHexagons.get(mapHexagons.get(rgId).getRegionId()).getRegionId() != mapHexagons.get(rgId).getRegionId()) {
            Gdx.app.log("警告:不合法,修正前", "rgId:" + rgId + " targetId:" + targetId);
            rgId = mapHexagons.get(mapHexagons.get(rgId).getRegionId()).getRegionId();
            Gdx.app.log("警告:不合法,修正后", "rgId:" + rgId + " targetId:" + targetId);
        }
        for (MapHexagon mapHexagon : mapHexagons) {
            if (mapHexagon.getRegionId() == targetId) {
                mapHexagon.setRegionId(rgId);
            }
        }

    }

    public IntArray getAroundRegionId(int rgId) {
        IntArray regions = new IntArray();
        IntArray tempId;
        int i, iMax = mapHexagons.size;
        int blockType = mapHexagons.get(rgId).getBlockType();
        for (i = 0; i < iMax; i++) {
            if (mapHexagons.get(i).getRegionId() == rgId) {
                if (blockType != 1) {
                    //陆地
                    tempId = getAroundIdById(i, 4, null);
                } else {
                    //海洋
                    tempId = getAroundIdById(i, 3, null);
                }
                for (int j = 0; j < tempId.size; j++) {
                    Integer id = tempId.get(j);
                    if (mapHexagons.get(id).getRegionId() != rgId) {
                        regions.add(mapHexagons.get(id).getRegionId());
                    }
                }
            }
        }
        tempId = new IntArray();//去重
        for (i = 0; i < regions.size; i++) {
            Integer in = regions.get(i);
            if (!tempId.contains(in)) {
                tempId.add(in);
            }
        }
        return tempId;
    }

    //根据条件获得核心地块
    private IntArray getRegions(int i) {
        //0 全部核心地块 只限城市和海港
        //1 只限于陆地的核心地块 只限城市
        //2 只限于陆地的核心地块 只限工厂且未被覆盖
        //3 只限于陆地的核心地块 只限机场且未被覆盖
        //4 只限于陆地的核心地块 只限油库且未被覆盖
        //5 只限于海洋的核心地块 只限海港
        //6 全部核心地块,不限类型
        int m;
        int mMax = mapHexagons.size;
        IntArray regions = new IntArray();
        if (i == 0) {
            for (m = 0; m < mMax; m++) {
                if (mapHexagons.get(m).getregionAreaId() != 0 || (mapHexagons.get(m).getRegionMineralLv() == 1 || mapHexagons.get(m).getRegionMineralLv() == 4)) {
                    regions.add(m);
                }
            }
        } else if (i == 1) {
            for (m = 0; m < mMax; m++) {
                if (mapHexagons.get(m).getBlockType() != 1 && (mapHexagons.get(m).getregionAreaId() != 0 || (mapHexagons.get(m).getRegionMineralLv() == 1))) {
                    regions.add(m);
                }
            }
        } else if (i == 2) {
            for (m = 0; m < mMax; m++) {
                if (mapHexagons.get(m).getBlockType() != 1 && (mapHexagons.get(m).getRegionId() == m || mapHexagons.get(m).getRegionId() == -1) && (mapHexagons.get(m).getregionAreaId() != 0 || (mapHexagons.get(m).getRegionMineralLv() == 2))) {
                    regions.add(m);
                }
            }
        } else if (i == 3) {
            for (m = 0; m < mMax; m++) {
                if (mapHexagons.get(m).getBlockType() != 1 && (mapHexagons.get(m).getRegionId() == m || mapHexagons.get(m).getRegionId() == -1) && (mapHexagons.get(m).getregionAreaId() != 0 || (mapHexagons.get(m).getRegionMineralLv() == 3))) {
                    regions.add(m);
                }
            }
        } else if (i == 4) {
            for (m = 0; m < mMax; m++) {
                if (mapHexagons.get(m).getBlockType() != 1 && (mapHexagons.get(m).getRegionId() == m || mapHexagons.get(m).getRegionId() == -1) && (mapHexagons.get(m).getregionAreaId() != 0 || (mapHexagons.get(m).getRegionMineralLv() == 5))) {
                    regions.add(m);
                }
            }
        } else if (i == 5) {
            for (m = 0; m < mMax; m++) {
                if (mapHexagons.get(m).getBlockType() == 1 && (mapHexagons.get(m).getregionAreaId() != 0 || (mapHexagons.get(m).getRegionMineralLv() == 4))) {
                    regions.add(m);
                }
            }
        } else if (i == 6) {
            for (m = 0; m < mMax; m++) {
                if (!regions.contains(mapHexagons.get(m).getRegionId())) {
                    regions.add(m);
                }
            }
        }
        return regions;
    }

    //根据id获得周围6边的地块id
    /*
     * rsIds指出的结果集
     * type 0全部 1获取region为0的地块 3获取周围的有region的海洋地块 4获取周围的有region的陆地地块
     * 5只获取region为0的海洋地块 6只获取region为0的陆地地块 7只获取region为0的陆地平原地块
     * 8只获取region为0的陆地非平原地块 9根据id区分海洋陆地,获取地块
     * 10 获取周围是陆地的且region相同的地块 11获取周围是海洋的地块
     * 12获取沿海陆地地块  13获取周围陆地地块 14获取周围铁路地块
     * rsIds不能是ids
     */
    public IntArray getAroundIdById(int id, int type, IntArray rsIds) {


        if (type == 0 || type == -1) {
            if (rsIds == null) {
                rsIds = new IntArray();
            } else {
                rsIds.clear();
            }
        } else {
            if (ids == null) {
                ids = new IntArray();
            } else {
                ids.clear();
            }
        }
        if (type != 0 && rsIds != null && rsIds == ids) {
            Gdx.app.error("getAroundIdById error", "ids不能作为结果集,因为他已经被使用了");
        }
        boolean ifLoop = isLoop();


        boolean top = false;
        boolean foot = false;
        boolean left = false;
        boolean right = false;
        //判断处于哪个边
        int y = (int) id / mapWidth;
        int x = id - y * mapWidth;
        int t1, t2, t3, t4, t5, t6;
        boolean ifParity = (x & 1) == 1;
        if (ifParity) {
            t1 = id - 1;
            t2 = id - mapWidth;
            t3 = id + 1;
            t4 = id + mapWidth - 1;
            t5 = id + mapWidth;
            t6 = id + mapWidth + 1;
        } else {
            t1 = id - mapWidth - 1;
            t2 = id - mapWidth;
            t3 = id - mapWidth + 1;
            t4 = id - 1;
            t5 = id + mapWidth;
            t6 = id + 1;
        }
        if (x == 0) {
            left = true;
        }
        if (x == mapWidth - 1) {
            right = true;
        }
        if (y == 0) {
            top = true;
        }
        if (y == mapHeight - 1) {
            foot = true;
        }
        if (type == 0) {
            if (ifLoop) {
                if (ifParity) {
                    rsIds.add(t1);
                    if (!top) {
                        rsIds.add(t2);
                    }
                    rsIds.add(t3);
                    if (!foot) {
                        rsIds.add(t4);
                        rsIds.add(t5);
                        rsIds.add(t6);
                    }
                } else {
                    if (!top) {
                        rsIds.add(t1);
                        rsIds.add(t2);
                        rsIds.add(t3);
                    }
                    rsIds.add(t4);
                    if (!foot) {
                        rsIds.add(t5);
                    }
                    rsIds.add(t6);
                }
            } else {
                if (ifParity) {
                    if (!left) {
                        rsIds.add(t1);
                    }
                    if (!top) {
                        rsIds.add(t2);
                    }
                    if (!right) {
                        rsIds.add(t3);
                    }
                    if (!foot && !left) {
                        rsIds.add(t4);
                    }
                    if (!foot) {
                        rsIds.add(t5);
                    }
                    if (!foot && !right) {
                        rsIds.add(t6);
                    }
                } else {
                    if (!top && !left) {
                        rsIds.add(t1);
                    }
                    if (!top) {
                        rsIds.add(t2);
                    }
                    if (!top && !right) {
                        rsIds.add(t3);
                    }
                    if (!left) {
                        rsIds.add(t4);
                    }
                    if (!foot) {
                        rsIds.add(t5);
                    }
                    if (!right) {
                        rsIds.add(t6);
                    }
                }

            }

        } else {
            if (ifLoop) {
                if (ifParity) {
                    ids.add(t1);
                    if (!top) {
                        ids.add(t2);
                    }
                    ids.add(t3);
                    if (!foot) {
                        ids.add(t4);
                        ids.add(t5);
                        ids.add(t6);
                    }
                } else {
                    if (!top) {
                        ids.add(t1);
                        ids.add(t2);
                        ids.add(t3);
                    }
                    ids.add(t4);
                    if (!foot) {
                        ids.add(t5);
                    }
                    ids.add(t6);
                }
            } else {
                if (ifParity) {
                    if (!left) {
                        ids.add(t1);
                    }
                    if (!top) {
                        ids.add(t2);
                    }
                    if (!right) {
                        ids.add(t3);
                    }
                    if (!foot && !left) {
                        ids.add(t4);
                    }
                    if (!foot) {
                        ids.add(t5);
                    }
                    if (!foot && !right) {
                        ids.add(t6);
                    }
                } else {
                    if (!top && !left) {
                        ids.add(t1);
                    }
                    if (!top) {
                        ids.add(t2);
                    }
                    if (!top && !right) {
                        ids.add(t3);
                    }
                    if (!left) {
                        ids.add(t4);
                    }
                    if (!foot) {
                        ids.add(t5);
                    }
                    if (!right) {
                        ids.add(t6);
                    }
                }
            }
        }


        //type 0全部 1只读取region为0的值  5只获取region为0的海洋地块 6只获取region为0的陆地地块
        //7只获取region为0的陆地平原地块  8只获取region为0的陆地非平原地块
        //10 获取周围是陆地的且region相同的地块 11获取周围是海洋的地块
        //12获取沿海陆地地块 13获取周围陆地地块 14获取周围铁路地块
        if (type == 0 || type == -1) {
            return rsIds;
        } else {
            if (rsIds == null) {
                rsIds = new IntArray();
            } else {
                rsIds.clear();
            }
            MapHexagon mapRHexagon = getMapHexagon(id);
            for (int i = 0; i < ids.size; i++) {
                int id2 = ids.get(i);
                MapHexagon mapHexagon = getMapHexagon(id2);
                if (type == 1 && mapHexagon.getRegionId() == -1) {
                    rsIds.add(id2);
                } else if (type == 6 && mapHexagon.getRegionId() == -1 && mapHexagon.getBlockType() != 1) {
                    rsIds.add(id2);
                } else if (type == 5 && mapHexagon.getRegionId() == -1 && (mapRHexagon.getBlockType() == 1 || mapHexagon.getBackTile() == 1)) {
                    rsIds.add(id2);
                } else if (type == 7 && mapHexagon.getRegionId() == -1 && mapHexagon.getBlockType() != 1 && mapHexagon.getBackTile() == 0) {
                    rsIds.add(id2);
                } else if (type == 8 && mapHexagon.getRegionId() == -1 && mapHexagon.getBlockType() != 1 && mapHexagon.getBackTile() != 0) {
                    rsIds.add(id2);
                } else if (type == 3 && mapHexagon.getRegionId() != -1 && (mapRHexagon.getBlockType() == 1)) {
                    rsIds.add(id2);
                } else if (type == 4 && mapHexagon.getRegionId() != -1 && (mapRHexagon.getBlockType() != 1)) {
                    rsIds.add(id2);
                } else if (type == 9 && ((mapHexagons.get(id).getBlockType() != 1) == (mapRHexagon.getBlockType() != 1))) {
                    rsIds.add(id2);
                } else if (type == 10 && (mapRHexagon.getBlockType() != 1 && mapHexagon.getRegionId() == mapHexagons.get(id).getRegionId())) {
                    rsIds.add(id2);
                } else if (type == 11 && (mapRHexagon.getBlockType() == 1)) {
                    rsIds.add(id2);
                } else if (type == 12 && (mapRHexagon.getBlockType() == 1) && (mapHexagons.get(id).getBlockType() != 1)) {
                    rsIds.add(id2);
                } else if (type == 13 && mapHexagon.getBlockType() != 1) {
                    rsIds.add(id2);
                } else if (type == 14 && mapHexagon.getRailWayId() == 1) {
                    rsIds.add(id2);
                }
            }
            return rsIds;
        }
    }

    public boolean isLoop() {
        boolean ifLoop = false;
        if (mapId != 0) {
            XmlReader.Element xmlE = game.gameConfig.getDEF_MAP().getElementById(mapId);
            if (xmlE != null && mapWidth == xmlE.getInt("width", 0)) {
                ifLoop = xmlE.getBoolean("ifLoop", false);
            }
        }
        return ifLoop;
    }

    public IntArray getAroundIdByIds(IntArray ids, int type) {
        IntArray rss = new IntArray();
        for (int i = 0; i < ids.size; i++) {
            Integer id = ids.get(i);
            IntArray rs = getAroundIdById(id, type, null);
            rss.addAll(rs);
        }
        return rss;
    }

    //临边地块定义 id小于当前的地块,且属性一致(即同是陆地,或同是海洋)
    public IntArray getBorderIdById(int id, IntArray rsIds) {

        if (rsIds == null) {
            rsIds = new IntArray();
        } else {
            rsIds.clear();
        }
        boolean top = false;
        boolean foot = false;
        boolean left = false;
        boolean right = false;
        //判断处于哪个边
        int y = (int) id / mapWidth;
        int x = id - y * mapWidth;
        int t1, t2, t3, t4, t5, t6;

        if ((x & 1) == 1) {
            t1 = id - 1;
            t2 = id - mapWidth;
            t3 = id + 1;
            t4 = id + mapWidth - 1;
            t5 = id + mapWidth;
            t6 = id + mapWidth + 1;
        } else {
            t1 = id - mapWidth - 1;
            t2 = id - mapWidth;
            t3 = id - mapWidth + 1;
            t4 = id - 1;
            t5 = id + mapWidth;
            t6 = id + 1;
        }
        if (x == 0) {
            left = true;
        }
        if (x == mapWidth - 1) {
            right = true;
        }
        if (y == 0) {
            top = true;
        }
        if (y == mapHeight - 1) {
            foot = true;
        }
        if (!top && !left && t1 < id && ifSameBlock(id, t1) && !ifSameRegion(id, t1)) {
            rsIds.add(t1);
        }
        if (!top && t2 < id && ifSameBlock(id, t2) && !ifSameRegion(id, t2)) {
            rsIds.add(t2);
        }
        if (!top && !right && t3 < id && ifSameBlock(id, t3) && !ifSameRegion(id, t3)) {
            rsIds.add(t3);
        }
        if (!foot && !left && t4 < id && ifSameBlock(id, t4) && !ifSameRegion(id, t4)) {
            rsIds.add(t4);
        }
        if (!foot && t5 < id && ifSameBlock(id, t5) && !ifSameRegion(id, t5)) {
            rsIds.add(t5);
        }
        if (!foot && !right && t6 < id && ifSameBlock(id, t6) && !ifSameRegion(id, t6)) {
            rsIds.add(t6);
        }
        return rsIds;

    }

    //只区分海陆
    private boolean ifSameBlock(int a, int b) {
        if (mapHexagons.get(a).getBlockType() == 1 && mapHexagons.get(b).getBlockType() == 1) {
            return true;
        } else if (mapHexagons.get(a).getBlockType() != 1 && mapHexagons.get(b).getBlockType() != 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean ifSameRegion(int a, int b) {
        if (mapHexagons.get(a).getRegionId() == mapHexagons.get(b).getRegionId()) {
            return true;
        } else {
            return false;
        }


    }


    private IntArray getIdByRegion(int regionId) {
        int m;
        int mMax = mapHexagons.size;
        IntArray rs = new IntArray();

        for (m = 0; m < mMax; m++) {
            if (mapHexagons.get(m).getRegionId() == regionId) {
                int s = m;
                rs.add(s);
            }
        }
        return rs;
    }

    //该方法遗失
    //aroundNum 获得周围6边的次数
    public IntArray getNumAroundIdsById(int id, int type, IntArray rsIds, int aroundNum, LinkedHashSet<Object> set) {
        if (rsIds == null) {
            rsIds = new IntArray();
        } else {
            rsIds.clear();
        }
        IntArray repeatList = new IntArray();
        IntArray rs = getAroundIdById(id, type, null);
        rsIds.addAll(rs);
        repeatList.add(id);
        int tempId;
        for (int i = 1, j; i < aroundNum; i++) {
            for (j = 0; j < rs.size; j++) {
                tempId = rs.get(j);
                if (!repeatList.contains(tempId)) {
                    rs = getAroundIdById(tempId, type, null);
                    rsIds.addAll(rs);

                    // GameUtil.listAddNoDup(rsIds, rs, set);
                    repeatList.add(tempId);
                }
            }
        }
        return rsIds;
    }

    //-1找不到
    //获得要扩展的id
    private int getRandomRegionId(int r) {
        IntArray rsIds;
        //1判断自身是海洋还是陆地
        if (mapHexagons.get(r).getBlockType() == 1) {//海
            //获取周边的海洋地块
            rsIds = getAroundIdById(r, 5, null);
            if (rsIds.size == 0) {
                rsIds = getAroundIdByIds(getIdByRegion(r), 5);
                if (rsIds == null || rsIds.size == 0) {
                    //如果周围没有空余地块,则使用相邻的相邻地块判断
                    return -1;
                }
            }
            //根据条件随机获取其中一个地块id
            //return(rsIds.get(rand.nextInt(rsIds.size())));
            return getShortAroundId(rsIds, mapHexagons.get(r).getRegionId());
        } else {//陆
            //获取周边陆地地块
            rsIds = getAroundIdById(r, 6, null);
            if (rsIds.size != 0 && ComUtil.ifGet(30)) {
                return getShortAroundId(rsIds, mapHexagons.get(r).getRegionId());
            } else {
                rsIds = getAroundIdByIds(getIdByRegion(r), 6);
                if (rsIds != null && rsIds.size == 0 && ComUtil.ifGet(50)) {
                    //如果周围没有空余地块,则使用相邻的相邻地块判断
                    rsIds = getAroundIdByIds(getIdByRegion(r), 7);
                    if (rsIds != null && rsIds.size == 0) {
                        //如果周围没有空余地块,则使用相邻的相邻地块判断
                        rsIds = getAroundIdByIds(getIdByRegion(r), 8);
                        if (rsIds != null && rsIds.size == 0) {
                            return -1;
                        }
                    }
                }
                if (rsIds == null) {
                    return -1;
                }
                return getShortAroundId(rsIds, mapHexagons.get(r).getRegionId());
            }

        }
    }


    //-1找不到
    //获得要扩展的id
    public int getRegionIdForE(int r) {
        IntArray rsIds;
        IntArray tempIds;
        //1判断自身是海洋还是陆地
        if (mapHexagons.get(r).getBlockType() == 1) {//海
            //获取周边的海洋地块
            rsIds = getAroundIdById(r, 5, null);
            if (rsIds.size == 0) {
                rsIds = getAroundIdByIds(getIdByRegion(r), 5);
                if (rsIds == null || rsIds.size == 0) {
                    //如果周围没有空余地块,则使用相邻的相邻地块判断
                    return -1;
                }
            }
            //根据条件随机获取其中一个地块id
            //return(rsIds.get(rand.nextInt(rsIds.size())));
            return getShortAroundId(rsIds, mapHexagons.get(r).getRegionId());
        } else {//陆
            //获取周边陆地地块
            rsIds = getAroundIdById(r, 6, null);
            if (rsIds.size == 0) {
                tempIds = getIdByRegion(r);
                rsIds = getAroundIdByIds(tempIds, 6);
                if (rsIds == null || rsIds.size < 3) {
                    //如果周围没有空余地块,则使用相邻的相邻地块判断
                    //Gdx.app.log("清除核心", r+"");
                    return -1;
                }
            }
            int id = getShortAroundId(rsIds, mapHexagons.get(r).getRegionId());
            return id;
        }
    }


    //chance 最高获取概率,最低默认为10 如果不容许有海洋地块,则多抽0.1
    private IntArray getRegionIdsByChance(int chance, boolean ifSea) {
        if (!ifSea) {
            chance = (int) (chance * 1.2);
        }


        int cutSide = 5;
        Random rand = new Random();

        int i, j, m, rsMax, rpMax;
        int mMax = mapHexagons.size;//chance<(int)(Math.random()*(100)
        IntArray rs = new IntArray();
        IntArray result = new IntArray();
        //获取全部为空的地块
        for (m = 0; m < mMax; m++) {
            if (mapHexagons.get(m).getRegionId() == -1) {
                if (ifSea) {
                    rs.add(m);
                } else if (!ifSea && mapHexagons.get(m).getBlockType() != 1) {
                    rs.add(m);
                }
            }
        }
        if (rs.size == 0) {
            return rs;
        }
        rsMax = rs.size;
        rpMax = rsMax * chance / 100;
        /*List<Coord> coords = converCoords(rs);
        List<Coord> rsCoords = new ArrayList<Coord>();
        rs = new ArrayList<Integer>();
        for (i = mapWidth; i >= 0; i = i - cutSide) {
            for (j = mapHeight; j >= 0; j = j - cutSide) {
                for (m = coords.size() - 1; m >= 0; m--) {
                    if (coords.get(m).getHX() > i && coords.get(m).getHY() > j) {
                        rsCoords.add(coords.get(m));
                        coords.remove(m);
                    }
                    if (rsCoords.size()>0) {
                        rs.add(rsCoords.get(rand.nextInt(rsCoords.size())).getid());
                        rsCoords.clear();
                        rs=ComUtil.getNewList(rs);
                    }
                    if(rs.size()>rsMax*chance/100){
                        break;
                    }
                    if (coords.size() == 0) {
                        break;
                    }
                }
            }
        }*/
        int tempId;
        for (i = 0; i < rpMax; i++) {
            //显示数字并将其从列表中删除,从而实现不重复.
            tempId = new Random().nextInt(rs.size);
            rs.removeIndex(tempId);
            result.add(tempId);
        }


        Gdx.app.log("获得随机核心", rsMax + ":" + result.size);
        return result;
    }

    //获取空白值
    private int getRegionForIdIs0() {
        int ct = 0, m, mMax = mapHexagons.size;
        for (m = 0; m < mMax; m++) {
            if (mapHexagons.get(m).getRegionId() == -1) {
                ct++;
            }
        }
        return ct;
    }

    //获得空白地块
    private IntArray getIdsForBlankRegion() {
        int m;
        int mMax = mapHexagons.size;
        IntArray rs = new IntArray();
        for (m = 0; m < mMax; m++) {
            if (mapHexagons.get(m).getRegionId() == -1) {
                rs.add(m);
            }
        }
        return rs;
    }

    //将id转为coord(x,y,id,regionId) 
    private Coord converCoord(int id) {
        int y = (int) id / mapWidth;
        int x = id - y * mapWidth;
        return new Coord(x, y, id, mapHexagons.get(id).getRegionId());
    }

    //将id转为coord
    private Array<Coord> converCoords(IntArray ids) {
        int m;
        int mMax = ids.size;
        Array<Coord> rsIds = new Array<Coord>();
        for (m = 0; m < mMax; m++) {
            int s = m;
            rsIds.add(converCoord(ids.get(s)));
        }
        return rsIds;
    }

    //获得最近的地块
    public int getShortAroundId(IntArray ids, int regionId) {
        // Gdx.app.log("求最近的地块,regionId", regionId+"");
        Array<Coord> coords = converCoords(ids);
        int i, j, iMax = coords.size, tempId = -1;
        double jl, jl2;
        Coord coord = converCoord(regionId);
        for (i = 0; i < iMax; i++) {
            for (j = 0; j < iMax; j++) {
                jl = Math.pow((coords.get(i).getX() - coord.getX()), 2) + Math.pow((coords.get(i).getY() - coord.getY()), 2);
                jl2 = (Math.pow((coords.get(j).getX() - coord.getX()), 2) + Math.pow((coords.get(j).getY() - coord.getY()), 2));
                //Gdx.app.log("求最近的地块,交换前", "i:"+coords.get(i).getid()+" 距离系数:"+jl);
                // Gdx.app.log("求最近的地块,交换前", "j:"+coords.get(i).getid()+" 距离系数:"+jl2);

                if (jl < jl2) { // 交换两数的位置
                    //Gdx.app.log("求最近的地块,交换前", "i:"+coords.get(i).getid()+" j:"+coords.get(j).getid());
                    //ComUtil.swap(coords, i, j);
                    coords.swap(i, j);
                    //Gdx.app.log("求最近的地块,交换后", "i:"+coords.get(i).getid()+" j:"+coords.get(j).getid());
                }
            }
        }
        //Gdx.app.log("求最近的地块,最前", coords.get(0).getid()+"");
        //Gdx.app.log("求最近的地块,最后", coords.get(coords.size()-1).getid()+"");
        Random rand = new Random();
        {//去除非同一势力 TODO
            for (i = coords.size - 1; i >= 0; i--) {
                if (!ifComLegion(coords.get(i).getId(), coord.getId())) {
                    coords.removeIndex(i);
                }
            }
        }


        if (coords.size > 2) {
            tempId = coords.get(rand.nextInt(2)).getId();
        } else if (coords.size == 0) {
            return -1;
        } else {
            tempId = coords.get(0).getId();
        }
        // 1.8的写法  取出List中的对象的属性值
        //List<Integer> xs = coords.stream().map(Coord::getHX).collect(Collectors.toList());
        //List<Integer> ys = coords.stream().map(Coord::getHY).collect(Collectors.toList());

        IntArray xs = new IntArray();
        IntArray ys = new IntArray();

        for (Coord cd : coords) {
            xs.add(cd.getX());
            ys.add(cd.getY());
        }

        int maxX = GameUtil.maxValue(xs);
        int minX = GameUtil.minValue(xs);
        int maxY = GameUtil.maxValue(ys);
        int minY = GameUtil.minValue(ys);

        if ((maxX - minX) > 3 || (maxY - minY) > 3) {
            if ((maxY - minY) != 0 && (maxX - minX) != 0 && ((maxX - minX) / (maxY - minY) > 2 || (maxY - minY) / (maxX - minX) > 2) || getRegionCountByRegionId(regionId) < ((maxX - minX) * (maxY - minY) / 3)) {
                return -1;
            }
        }
        /*if(getRegionCountByRegionId(regionId)>(10 + Math.random() * 5)) {
            return -1;
        }*/
        return tempId;
    }

    //获取各region的数量记录为map
    private Map getRegionCountMap() {
        Map rsMap = new HashMap();
        for (MapHexagon mapHexagon : mapHexagons) {
            if (!rsMap.containsKey(mapHexagon.getRegionId())) {
                rsMap.put(mapHexagon.getRegionId(), 1);
            } else {
                rsMap.put(mapHexagon.getRegionId(), Integer.parseInt(rsMap.get(mapHexagon.getRegionId()).toString()) + 1);
            }
        }
        return rsMap;
    }

    //获取通过region获取region的数量
    private int getRegionCountByRegionId(int regionId) {
        int c = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            if (mapHexagon.getRegionId() == regionId) {
                c++;
            }
        }
        return c;
    }

    //通过多个region获取ids
    public IntArray getIdsByRegionIds(IntArray regionIds) {
        IntArray rs = new IntArray();
        int c = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            if (regionIds.contains(mapHexagon.getRegionId())) {
                rs.add(c);
            }
            c++;
        }
        return rs;
    }

    //通过region获取ids
    public IntArray getIdsByRegionId(int regionId) {
        IntArray rs = new IntArray();
        int c = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            if (mapHexagon.getRegionId() == regionId) {
                rs.add(c);
            }
            c++;
        }
        return rs;
    }

    //获取所有region
    public IntArray getAllRegionIds() {
        IntArray rs = new IntArray();
        for (MapHexagon mapHexagon : mapHexagons) {
            if (mapHexagon.getRegionId() != -1 && !rs.contains(mapHexagon.getRegionId())) {
                rs.add(mapHexagon.getRegionId());
            }
        }
        return rs;
    }

    //合并孤岛类地块
    private void mergeIslandGridByRegion() {
        int i, iMax = mapHexagons.size, rsI = 0;
        int rz;
        IntArray rs;
        int tempRegionId;
        for (i = 0; i < iMax; i++) {
            //获得结果值
            rs = getAroundIdById(i, 9, null);
            // Collections.sort(rs);
            rs.sort();
            rsI = 0;
            //判断是否是孤岛
            for (int j = 0; j < rs.size; j++) {
                Integer id = rs.get(j);
                if (mapHexagons.get(id).getRegionId() != mapHexagons.get(i).getRegionId()) {
                    rsI++;
                }
            }
            if (rsI > 4) {
                rz = GameUtil.getMostRepeatValue(rs);
                //rz = ComUtil.getListMostRepeatData(rs);
                //updateRegionIds(mapBins.get(Integer.parseInt(rz)).getPolitical(),i);
                mapHexagons.get(i).setRegionId(mapHexagons.get(rz).getRegionId());
            }
            rs.clear();
            rs = null;
        }
    }


    public static void main(String[] args) {
        int i;

        for (i = 0; i < 30; i++) {
            System.out.println("sj:" + (int) (10 + Math.random() * 15));
        }

    }

    //检查所属的区块是不是合法的region,如果不是给周围的地块 TODO测试
    public void checkRegion(Boolean ifLoop) {
        int i, iMax = mapHexagons.size;
        String rsI;
        IntArray rs = new IntArray();
        int j, jMax;
        IntArray rs2 = new IntArray();
        Boolean isRegion;
        int aroundType = 0;
        if (ifLoop) {
            aroundType = -1;
        }
        for (i = 0; i < iMax; i++) {
            //获得结果值
            rs = getAroundIdById(i, 0, rs);
            /*Collections.sort(rs);
            if (mapBins.get(mapBins.get(i).getRegionId()).getRegionId() != mapBins.get(i).getRegionId()) {
                rsI = ComUtil.getListMostRepeatData(rs);
                if (rsI != null && rsI != "") {
                    //Gdx.app.log("所属区域region不合法:", "i:"+i);
                    updateRegionIds(mapBins.get(Integer.parseInt(rsI)).getRegionId(), mapBins.get(i).getRegionId());
                }
            }*/
            isRegion = (mapHexagons.get(i).getRegionId() == i);
            if (isRegion) {
                rs2.add(i);
                //*.监测region是否邻边
                for (j = 0, jMax = rs.size; j < jMax; j++) {
                    int region = mapHexagons.get(rs.get(j)).getRegionId();
                    if (region != i) {
                        Gdx.app.log("checkRegion1", i + ":处于边缘,请查看是否需要调整位置");
                        break;
                    }
                    if (!ifGridIsPass(region)) {
                        Gdx.app.error("checkRegion2", i + ":region不合法:" + region);
                        break;
                    }
                }
            }

        }


        //*.检查region是否相邻

        {
            //获取邻边

            rs.clear();
            //遍历rs,扩充到rs2中
            for (i = 0, iMax = rs2.size; i < iMax; i++) {
                rs = getAroundIdById(rs2.get(i), 0, rs);
                for (j = 0, jMax = rs.size; j < jMax; j++) {
                    int id = rs.get(j);
                    if (!rs2.contains(id) && mapHexagons.get(id).getRegionId() == mapHexagons.get(rs2.get(i)).getRegionId()) {
                        //rs2存放所有region的结果 对其结果进行扩充
                        rs2.add(id);
                        iMax = rs2.size;
                    }
                }
            }
            // Gdx.app.log("checkRegion2",rs.size()+":"+rs2.size());
            for (i = 0, iMax = mapHexagons.size; i < iMax; i++) {
                if (!rs2.contains(i)) {
                    Gdx.app.log("checkRegion2", i + ":" + mapHexagons.get(i).getRegionId() + ":坐标与region不相邻,请查看是否正确");
                }
            }
        }
    }

    //打印空白的非海洋地块(包括港口)
    public void logNoSeaBlankGrid() {
        int i = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            if (mapHexagon.getRegionId() == -1 && (mapHexagon.getBackTile() != 1 && mapHexagon.getBackTile() != 2)) {
                Gdx.app.log("空白地块", i + "");
            }
            i++;
        }
    }


    public void logAllBlankGrid() {
        int i = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            if (mapHexagon.getRegionId() == -1) {
                Gdx.app.log("空白地块", i + "");
            }
            i++;
        }
    }


    //验证势力是否一致   r源id  i目标id
    private boolean ifComLegion(int r, int i) {
        int sourceLegion = mapHexagons.get(r).getRegionOilLv();
        int regionLegion = mapHexagons.get(i).getRegionOilLv();
        if (sourceLegion == regionLegion) {
            return true;
        } else if (sourceLegion == 255) {
            return true;
        } else {
            return false;
        }

    }

    //将海港城市归为最近的陆地城市
    private void updHabourRegionForE() {
        IntArray habour = getRegions(5);
        IntArray rsIds, tempIds;
        int id;
        Random rand = new Random();
        for (int i = 0; i < habour.size; i++) {
            Integer r = habour.get(i);
            rsIds = getAroundIdById(r, 4, null);
            if (rsIds.size == 0) {
                Gdx.app.log("警告", r + ":海港周围为空");
            } else {
                id = rsIds.get(rand.nextInt(rsIds.size));
                if (id != -1 && mapHexagons.get(r).getRegionId() == r) {
                    mapHexagons.get(r).setRegionId(mapHexagons.get(id).getRegionId());
                }
            }
        }
    }


    //将海洋的省区去除
    private void delSeaRegionForE() {
        for (MapHexagon b : getMapbin()) {
            if ((b.getBackTile() == 1 || b.getBackTile() == 2) && b.getRegionId() != -1) {
                Gdx.app.log("海洋的省区", b.getRegionId() + "");
                b.setRegionId(-1);
            }
        }
    }

    public void updHabourForE() {

        /*int i;
        int iMax = mapBins.size;
        for (i = 0; i < iMax; i++) {
            if (mapBins.get(i).getBuildId() == 4) {
                mapBins.get(i).setPolitical(i);
            }
        }*/
        delSeaRegionForE();
        //updHabourRegionForE();
        logNoSeaBlankGrid();
    }

    //替换当前的地区的所有相同区块为一个id的所在区块
    public void replaceRegionIdById(int id) {

        int regionId = mapHexagons.get(id).getRegionId();
        MapHexagon mapHexagon = mapHexagons.get(regionId);
        MapRegion r = mapHexagon.getRegionData();
        if (r != null) {
            r.setRegion(id);
        }

        Gdx.app.log("replaceRegion", regionId + ":" + id);
        for (MapHexagon mapHexagon1 : mapHexagons) {
            if (mapHexagon1.getRegionId() == regionId) {
                mapHexagon1.setRegionId(id);
            }
        }
    }

    //过滤,只留存指定region的id
    private IntArray getTargetRegionByids(IntArray o, int targetRegion) {
        IntArray rs = new IntArray();
        for (int c = 0; c < o.size; c++) {
            Integer i = o.get(c);
            MapHexagon m = getMapHexagon(i);
            ;
            if (m != null && m.getRegionId() == targetRegion) {
                rs.add(i);
            }
        }
        return rs;
    }


    //替换当前的地块以及与其相邻的地块的region
    public void replaceRegionIdForFFArea(Fb2History his, int id, int regionId) {
        //替换不穿海,只替换该区域的
        IntArray rs = new IntArray();
        MapHexagon m = getMapHexagon(id);
        if (m == null) {
            return;
        }
        int tempRegion = m.getRegionId();

        IntArray tempI;
        /*if(mapBins.get(id).getPolitical()!=-1){
            return;
        }else*/
        {
            IntArray tempIds = getTargetRegionByids(getAroundIdById(id, 9, null), tempRegion);
            rs.addAll(tempIds);
            int n = 0;
            do {
                tempIds = GameUtil.clone(rs, tempIds);
                for (int i1 = 0; i1 < tempIds.size; i1++) {
                    int i = tempIds.get(i1);
                    tempI = getTargetRegionByids(getAroundIdById(i, 9, null), tempRegion);
                    rs.removeAll(tempI);
                    rs.addAll(tempI);
                }
                n = n + 1;
                if (n > 5) {
                    break;
                }
            } while (GameUtil.ifIntArrayContainIntArrayByInteger(rs, tempIds));


            for (int i1 = 0; i1 < tempIds.size; i1++) {
                int i = tempIds.get(i1);
                if (his != null && mapHexagons.get(i).getRegionId() == i) {
                    his.replaceRegion(i, regionId);
                }
                mapHexagons.get(i).setRegionId(regionId);
                resetCoastBorder(i);

            }
        }
    }

    //批量获取符合某种条件的地块,条件参看getAroundIdById
    public IntArray getIdsByAround(int type) {
        IntArray rsGrid = new IntArray();
        IntArray rs;
        int id = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            rs = getAroundIdById(id, type, null);
            if (rs.size > 0) {
                rsGrid.add(id);
            }
            id = id + 1;
        }
        return rsGrid;
    }

    public IntIntMap getRegionMap() {
        if (regionMap == null) {
            regionMap = new IntIntMap();
            int i = 0;
            for (MapHexagon mapHexagon : mapHexagons) {
                if (mapHexagon.getRegionId() == i && mapHexagon.getRegionId() != -1) {
                    regionMap.put(mapHexagon.getRegionId(), i);
                }
                i = i + 1;
            }
        }
        return regionMap;
    }

    /*public int getIdByRegionId(int regionId){
        if(regionMap==null){
            getRegionMap();
        }
        if(regionMap.containsKey(regionId)){
            return (int) regionMap.get(regionId);
        }
        return -1;
    }*/


    public IntArray getCoastGrid() {
        return coastGrid;
    }

    public void setCoastGrid(IntArray coastGrid) {
        this.coastGrid = coastGrid;
    }

    /* public void initSearchPath(HexagonDAO hexagons){
         this.hexagons=hexagons;
     };
 */
    public Map<Integer, Integer> getAreaRigonMap() {
        Map<Integer, Integer> rs = new HashMap<>();
        for (MapHexagon bin : mapHexagons) {
            if (bin.getregionAreaId() != 0) {
                rs.put(bin.getregionAreaId(), bin.getRegionId());
            }
        }
        return rs;
    }


    //寻路算法  返回的结果为 反向结果

    /**
     * @param type {@link #getAroundIdById}
     */
    public IntArray getMoveGrid(int beginIndex, int endIndex, int type, IntArray rs) {
        if (rs == null) {
            rs = new IntArray();
        } else {
            rs.clear();
        }
        //重置寻路信息
        for (MapHexagon mapbin : getMapbin()) {
            mapbin.parent = null;
            mapbin.F = 0;
            mapbin.G = 0;
            mapbin.H = 0;
        }
        // int legionIndex=hexagons.getBuildPolicy(beginIndex);
        int legionIndex = -1;//用来判断那个地方是否有单位,需要重写 TODO
        if (!ifGridIsPass(beginIndex) || !ifGridIsPass(endIndex)) {
            Gdx.app.error("getMoveGrid1", "起点或者终点有误");
            return rs;
        }
        if (type == 1) {
            if (ifSea(beginIndex) || ifSea(endIndex)) {
                Gdx.app.error("getMoveGrid2", "起点或者终点有误");
                return rs;
            }
        } else if (type == 2) {
            if (!ifSea(beginIndex) || !ifSea(endIndex)) {
                Gdx.app.error("getMoveGrid3", "起点或者终点有误");
                return rs;
            }
        } else if (type == 3) {
            if (!ifRailway(beginIndex) || !ifRailway(endIndex)) {
                Gdx.app.error("getMoveGrid4", "起点或者终点有误");
                return rs;
            }
        }
        if (openList == null) {
            openList = new IntArray();
        } else {
            openList.clear();
        }
        if (closeList == null) {
            closeList = new IntArray();
        } else {
            closeList.clear();
        }
        isFind = makeOne(beginIndex, endIndex, type, rs);
        tempIndex = -1;
        long count = 0;
        while (isFind == false && openList.size > 0) {
            count++;
            tempIndex = findMinFinOpen();
            if (tempIndex != -1) {
                isFind = makeOne(tempIndex, endIndex, type, rs);
            }
        }
        //  Gdx.app.log("getMoveGrid 计算"+count+"次: "+isFind+" "+beginIndex+":"+ endIndex," o:"+openList.size+" c:"+closeList.size+" r:"+rs.size+" ids:"+rs.toString());
        if (!isFind) {
            rs.clear();
        } else {
            rs.reverse();
            Gdx.app.log("getMoveGrid 计算" + count + "次:  " + beginIndex + ":" + endIndex + ":" + rs.size, " ids:" + rs.toString());
        }
        return rs;
    }

    /**
     * @param type {@link #getAroundIdById}
     */
    boolean makeOne(int curC, int desC, int type, IntArray rs) {
        //将当前节点从openList中移除
        closeList.add(curC);
        //将当前节点从openList中移除
        openList.removeValue((Integer) curC);
        IntArray nearListC = new IntArray();
        nearListC = getAroundIdById(curC, type, nearListC);
        MapHexagon curCHexagon = getMapHexagon(curC);
        ;
        if (curCHexagon == null) {
            return false;
        }
        //Gdx.app.log("nearListC",curC+":"+nearListC.size()+":"+nearListC.toString());
        for (int i1 = 0; i1 < nearListC.size; i1++) {
            Integer id = nearListC.get(i1);
            if (!ifGridIsPass(id)) {
                continue;
            }
            //如果该点在地图中
            MapHexagon temp = getMapHexagon(id);
            if (temp != null && /*(i == desC||hexagons.isPass(legionIndex,i)) &&*/ !closeList.contains(id)) {
                int tempG = curCHexagon.G + temp.speed;
                if (tempG < temp.G || temp.parent == null) {
                    temp.parent = curCHexagon;
                    temp.G = tempG;
                    temp.H = calcuH(temp.getHexagonIndex(), desC);
                    temp.F = temp.G + temp.H;
                }
                if (!openList.contains(id)) {
                    openList.add(id);
                    //Gdx.app.log("addI",curC+":"+i);
                }
                //找到目标节点
                if (id == desC) {
                    addToPath(rs, temp);
                    //Gdx.app.log("找到目标节点",curC+":"+rs.size());
                    nearListC.clear();
                    return true;
                }
            }
        }
        nearListC.clear();
        return false;
    }


    public void addToPath(IntArray pathC, MapHexagon mapHexagon) {
        pathC.add(mapHexagon.getHexagonIndex());
        if (mapHexagon.parent != null) {
            // Gdx.app.log("addToPath", "pathC:" + pathC.toString() + " id:" + mapHexagon.getHexagonIndex() + " parent:" + mapHexagon.parent.getHexagonIndex());
            addToPath(pathC, mapHexagon.parent);
        }
    }


    private int calcuH(int srcC, int desC) {

        //计算坐标
        beginX = srcC % mapWidth;
        beginY = srcC / mapWidth;
        endX = desC % mapWidth;
        endY = desC / mapWidth;


        int srcCX = beginX * 10;
        int srcCY = -beginY * 10;
        if (beginX % 2 != 0) {
            srcCY -= 5;
        }
        int desCX = endX * 10;
        int desCY = -endY * 10;
        if (endX % 2 != 0) {
            desCY -= 5;
        }
        double _x = Math.abs(srcCX - desCX);
        double _y = Math.abs(srcCY - desCY);
        return (int) Math.sqrt(_x * _x + _y * _y);
    }

    private int findMinFinOpen() {
        //先找最小值
        int minF = Integer.MAX_VALUE;
        for (int i = 0; i < openList.size; i++) {
            MapHexagon m = getMapHexagon(openList.get(i));
            if (m != null && m.F < minF) {
                minF = m.F;
            }
        }
        for (int i = 0; i < openList.size; i++) {
            MapHexagon m = getMapHexagon(openList.get(i));
            if (m != null && m.F == minF) {
                return (openList.get(i));
            }
        }
        return -1;
    }

    //判断两个坐标的距离  des-src>dis true
    private boolean ifCoordDistance(int srcC, int desC, int distance) {
        //计算坐标
        beginX = srcC % mapWidth;
        beginY = srcC / mapWidth;
        endX = desC % mapWidth;
        endY = desC / mapWidth;
        if (Math.abs(endX - beginX) > distance || Math.abs(endY - beginY) > distance) {
            //Gdx.app.log("判断两个坐标的距离 ",srcC+":"+desC+":"+true);
            return true;
        } else {
            //Gdx.app.log("判断两个坐标的距离 ",srcC+":"+desC+":"+false);
            return false;
        }
    }

    public int getTerrainId(int hexagon) {
        MapHexagon m = getMapHexagon(hexagon);
        if (m != null) {
            return m.getBackTile();
        }
        return 0;
    }


    //返回n范围内的region的id
    //type
    /*
     * type 0全部 1获取region为0的地块 3获取周围的有region的海洋地块 4获取周围的有region的陆地地块
     * 5只获取region为0的海洋地块 6只获取region为0的陆地地块 7只获取region为0的陆地平原地块
     * 8只获取region为0的陆地非平原地块 9根据id区分海洋陆地,获取地块
     * 10 获取周围是陆地的地块 11获取周围是海洋的地块
     * 12获取沿海陆地地块 13根据id区分海陆获取全部地块
     */

    /*public IntArray getScopeIdByRegion(int regionId, IntArray rsIds, LinkedHashSet<Object> set, int type) {
        rsIds = getNumAroundIdsById(regionId, type, rsIds, ResConfig.Game.buildScope, set);
        for (int i = rsIds.size- 1; i >= 0; i--) {
            if (getMapbin().get(rsIds.get(i)).getRegionId() != regionId) {
                rsIds.removeIndex(i);
            }
        }
        //Gdx.app.log("getScopeIdByRegion",regionId+":"+rsIds.toString());
        return rsIds;
    }*/

    //初始化地图位置
    public void initSourcePotion(float scale, int mapH_px) {
        int j = 0, x, y;
        for (MapHexagon m : mapHexagons) {
            x = (j % mapWidth) + 1;
            y = (j / mapWidth) + 1;
            m.setSourceX((int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2)));
            m.setSourceY(mapH_px - (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2)));
            j = j + 1;
        }
    }

    public int getSourceX(int i) {
        return mapHexagons.get(i).getSourceX();
    }

    public int getSourceY(int i) {
        return mapHexagons.get(i).getSourceY();
    }

    public int getRegionById(int id) {
        return mapHexagons.get(id).getRegionId();
    }

    public boolean isRegion(int hexagon) {
        if (hexagon != -1 && mapHexagons.get(hexagon).getRegionId() == hexagon) {
            return true;
        } else {
            return false;
        }
    }

    //有问题
    /*public void initBorderByRegion(){
        for(int i=0;i<getMapbin().size();i++){
            getMapbin().get(i).setBorder(getBorder(i,true));
        }
        //因为有一遍未必能判定出来,所以要执行第二遍
       for(int i=0;i<getMapbin().size();i++){
           if(!mapBins.get(i).getBorder().equals(ResConfig.StringName.noBorder)){
               getMapbin().get(i).setBorder(getBorder(i,false));
           }
        }
    }*/

    //设置海陆的边缘地块
    public void initCoastBorderByRegion() {
        for (int i = 0; i < getMapbin().size; i++) {
            MapHexagon m = getMapHexagon(i);
            if (m != null) {
                m.setBorder(getCoastBorder(i));
                if (getCoastGrid().contains(i)) {
                    m.ifCoast = GameMethod.getFBorderIdByString(m.getBorder());
                }
            }
        }
    }

    /*if (mapBin.getCoastGrid().contains(hexagonData.getHexagonIndex())) {
        hexagonData.setIfCoast(MapBinDAO.getFBorderIdByString(m.getBorder()));
    }*/


    private String getCoastBorder(int id) {

        /*if (id == 49156) {
            Gdx.app.log("createMapbin", "i:" + id);
        }*/
        StringBuffer rs;


        if (!coastGrid.contains(id)) {
            return "000000";
        }


        rs = new StringBuffer();

        boolean top = false;
        boolean foot = false;
        boolean left = false;
        boolean right = false;
        //判断处于哪个边
        int y = (int) id / mapWidth;
        int x = id - y * mapWidth;
        int t1, t2, t3, t4, t5, t6;
        boolean ifParity = (x & 1) == 1;
        if (ifParity) {
            t1 = id - 1;
            t2 = id - mapWidth;
            t3 = id + 1;
            t4 = id + mapWidth - 1;
            t5 = id + mapWidth;
            t6 = id + mapWidth + 1;
        } else {
            t1 = id - mapWidth - 1;
            t2 = id - mapWidth;
            t3 = id - mapWidth + 1;
            t4 = id - 1;
            t5 = id + mapWidth;
            t6 = id + 1;
        }


        if (x == 0) {
            left = true;
        }
        if (x == mapWidth - 1) {
            right = true;
        }
        if (y == 0) {
            top = true;
        }
        if (y == mapHeight - 1) {
            foot = true;
        }

        if (ifParity) {
            rs.append(ifSameBlock(t1, id) == true ? 0 : 1);
            if (top) {
                rs.append(0);
            } else {
                rs.append(ifSameBlock(t2, id) == true ? 0 : 1);
            }
            rs.append(ifSameBlock(t3, id) == true ? 0 : 1);
            if (foot) {
                rs.append(0);
                rs.append(0);
                rs.append(0);
            } else {
                rs.append(ifSameBlock(t4, id) == true ? 0 : 1);
                rs.append(ifSameBlock(t5, id) == true ? 0 : 1);
                rs.append(ifSameBlock(t6, id) == true ? 0 : 1);
            }
        } else {
            if (top) {
                rs.append(0);
                rs.append(0);
                rs.append(0);
            } else {
                rs.append(ifSameBlock(t1, id) == true ? 0 : 1);
                rs.append(ifSameBlock(t2, id) == true ? 0 : 1);
                rs.append(ifSameBlock(t3, id) == true ? 0 : 1);
            }
            rs.append(ifSameBlock(t4, id) == true ? 0 : 1);
            if (foot) {
                rs.append(0);
            } else {
                rs.append(!foot && ifSameBlock(t5, id) == true ? 0 : 1);
            }
            rs.append(ifSameBlock(t6, id) == true ? 0 : 1);
        }
        return rs.toString();
    }


    //获取临边特征值
    private String getBorder(int id, boolean ifFirst) {

        if (id == 259) {
            Gdx.app.log("createMapbin", "i:" + id);
        }/**/
        StringBuffer rs;
        if (ifFirst) {
            rs = new StringBuffer();
        } else {
            rs = new StringBuffer(mapHexagons.get(id).getBorder());
        }


        boolean top = false;
        boolean foot = false;
        boolean left = false;
        boolean right = false;
        //判断处于哪个边
        int y = (int) id / mapWidth;
        int x = id - y * mapWidth;
        int t1, t2, t3, t6;
        boolean ifParity = (x & 1) == 1;
        if (ifParity) {
            t1 = id - 1;
            t2 = id - mapWidth;
            t3 = id + 1;
            t6 = id + mapWidth + 1;
        } else {
            t1 = id - mapWidth - 1;
            t2 = id - mapWidth;
            t3 = id - mapWidth + 1;
            t6 = id + 1;
        }
        if (x == 0) {
            left = true;
        }
        if (x == mapWidth - 1) {
            right = true;
        }
        if (y == 0) {
            top = true;
        }
        if (y == mapHeight - 1) {
            foot = true;
        }

        if (ifParity) {
            if (ifFirst && !left && ifSameBlock(id, t1) && !ifSameRegion(id, t1)) {
                if (mapHexagons.get(t1).getBorder().substring(2, 3).equals(ResDefaultConfig.StringName.zero)) {//如果左上也是边界,则绘制另一种形状
                    rs.append(1);
                } else {
                    rs.append(2);
                }
            } else if (!ifFirst) {
                rs.append(mapHexagons.get(id).getBorder().substring(0, 1));
            } else {
                rs.append(0);
            }
            if (!top && ifSameBlock(id, t2) && !ifSameRegion(id, t2)) {
                if (ifFirst) {
                    rs.append(1);
                } else if (mapHexagons.get(t3).getBorder().substring(0, 1).equals(ResDefaultConfig.StringName.zero)) {
                    rs.append(1);
                } else {
                    rs.append(2);
                }
            } else {
                rs.append(0);
            }

            if (!right && ifSameBlock(id, t3) && !ifSameRegion(id, t3)) {
                if (ifFirst) {
                    rs.append(1);
                } else if (t6 < mapHexagons.size && mapHexagons.get(t6).getBorder() != null && mapHexagons.get(t6).getBorder().substring(0, 1).equals(ResDefaultConfig.StringName.two)) {
                    rs.append(1);
                } else {
                    rs.append(2);
                }

            } else {
                rs.append(0);
            }
        } else {
            if (ifFirst && !top && !left && ifSameBlock(id, t1) && !ifSameRegion(id, t1)) {
                if (mapHexagons.get(t1).getBorder().substring(2, 3).equals(ResDefaultConfig.StringName.zero)) {//如果左上也是边界,则绘制另一种形状
                    rs.append(1);
                } else {
                    rs.append(2);
                }
            } else if (!ifFirst) {
                rs.append(mapHexagons.get(id).getBorder().substring(0, 1));
            } else {
                rs.append(0);
            }
            if (!top && ifSameBlock(id, t2) && !ifSameRegion(id, t2)) {
                if (ifFirst) {
                    rs.append(1);
                } else if (mapHexagons.get(t3).getBorder().substring(0, 1).equals(ResDefaultConfig.StringName.zero)) {
                    rs.append(1);
                } else {
                    rs.append(2);
                }
            } else {
                rs.append(0);
            }

            if (!top && !right && ifSameBlock(id, t3) && !ifSameRegion(id, t3)) {
                if (ifFirst) {
                    rs.append(1);
                } else if (t6 < mapHexagons.size && mapHexagons.get(t6).getBorder() != null && mapHexagons.get(t6).getBorder().substring(0, 1).equals(ResDefaultConfig.StringName.one)) {
                    rs.append(1);
                } else {
                    rs.append(2);
                }
            } else {
                rs.append(0);
            }
        }
        return rs.toString();
    }

    private MapHexagon getBorderHexagon(int id,int direct){
        int tid=getIdInDirect(id,direct);
        if(tid!=id&&ifGridIsPass(tid)){
            return getMapHexagon(tid);
        }
        return null;
    }

    //获得坐标某个方向的点的id,如不存在,则返回-1
    //direct 左上1,上2,右上3,左下4,下5,右下6

    private int getIdInDirect(int id, int direct) {

        boolean top = false;
        boolean foot = false;
        boolean left = false;
        boolean right = false;
        //判断处于哪个边
        int y = (int) id / mapWidth;
        int x = id - y * mapWidth;
        int t1, t2, t3, t4, t5, t6;
        boolean ifParity = (x & 1) == 1;
        if (ifParity) {
            t1 = id - 1;
            t2 = id - mapWidth;
            t3 = id + 1;
            t4 = id + mapWidth - 1;
            t5 = id + mapWidth;
            t6 = id + mapWidth + 1;
        } else {
            t1 = id - mapWidth - 1;
            t2 = id - mapWidth;
            t3 = id - mapWidth + 1;
            t4 = id - 1;
            t5 = id + mapWidth;
            t6 = id + 1;
        }
        if (x == 0) {
            left = true;
        }
        if (x == mapWidth - 1) {
            right = true;
        }
        if (y == 0) {
            top = true;
        }
        if (y == mapHeight - 1) {
            foot = true;
        }
        if (ifParity) {
            if (direct == 1 && t1 > 0 && t1 < mapHexagons.size/* !left */) {
                return (t1);
            }
            if (direct == 2 && t2 > 0 && t2 < mapHexagons.size/* && !top */) {
                return (t2);
            }
            if (direct == 3 && t3 > 0 && t3 < mapHexagons.size/* && !right */) {
                return (t3);
            }
            if (direct == 4 && t4 > 0
                    && t4 < mapHexagons.size/* && !foot && !left */) {
                return (t4);
            }
            if (direct == 5 && t5 > 0 && t5 < mapHexagons.size/* && !foot */) {
                return (t5);
            }
            if (direct == 6 && t6 > 0
                    && t6 < mapHexagons.size/* && !foot && !right */) {
                return (t6);
            }
        } else {
            if (direct == 1 && t1 > 0
                    && t1 < mapHexagons.size/* && !top && !left */) {
                return (t1);
            }
            if (direct == 2 && t2 > 0 && t2 < mapHexagons.size/* && !top */) {
                return (t2);
            }
            if (direct == 3 && t3 > 0
                    && t3 < mapHexagons.size/* && !top && !right */) {
                return (t3);
            }
            if (direct == 4 && t4 > 0 && t4 < mapHexagons.size/* && !left */) {
                return (t4);
            }
            if (direct == 5 && t5 > 0 && t5 < mapHexagons.size/* && !foot */) {
                return (t5);
            }
            if (direct == 6 && t6 > 0 && t6 < mapHexagons.size/* && !right */) {
                return (t6);
            }
        }
        return -1;
    }


    public boolean isWater(int hexagon) {
        if (mapHexagons.get(hexagon).getBackTile() == 1 || mapHexagons.get(hexagon).getBackTile() == 2) {
            return true;
        }
        return false;
    }
/*
    //获得所有相连的region
    public List<Integer> getConnectRegion(boolean ifLoop,int region){
        return null;
    }*/

    //获得所有相连的region
    public IntMap<IntArray> getAllConnectRegion(boolean ifLoop) {
        IntMap<IntArray> rs = new IntMap<>();
        IntArray tempRs = new IntArray();
        //List<Integer> repeatIdsList=new ArrayList<>();//排斥列
        int aroundType = 0;//0不循环地图 1循环地图
        if (ifLoop) {
            aroundType = -1;
        }
        int i = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            if (mapHexagon.getRegionId() != -1) {
                if (!rs.containsKey(mapHexagon.getRegionId())) {
                    rs.put(mapHexagon.getRegionId(), new IntArray());
                }
                IntArray aroundIds = getAroundIdById(i, aroundType, tempRs);
                for (int j1 = 0; j1 < aroundIds.size; j1++) {
                    Integer j = aroundIds.get(j1);
                    if (j != -1 && j < mapHexagons.size && mapHexagons.get(j).getRegionId() != -1 && mapHexagons.get(j).getRegionId() != mapHexagon.getRegionId() && !rs.get(mapHexagon.getRegionId()).contains(mapHexagons.get(j).getRegionId())) {
                        rs.get(mapHexagon.getRegionId()).add(mapHexagons.get(j).getRegionId());
                        if (!rs.containsKey(mapHexagons.get(j).getRegionId())) {
                            rs.put(mapHexagons.get(j).getRegionId(), new IntArray());
                        }
                        if (!rs.get(mapHexagons.get(j).getRegionId()).contains(mapHexagons.get(i).getRegionId())) {
                            rs.get(mapHexagons.get(j).getRegionId()).add(mapHexagons.get(i).getRegionId());
                        }
                    }
                }
            }
            i++;
        }
        return rs;
    }


    public ObjectMap<Integer, IntArray> getConnectRegions(boolean ifLoop) {
        ObjectMap<Integer, IntArray> rs = new ObjectMap<>();
        IntArray tempRs = new IntArray();
        //List<Integer> repeatIdsList=new ArrayList<>();//排斥列
        int aroundType = 0;//0不循环地图 1循环地图
        if (ifLoop) {
            aroundType = -1;
        }
        int i = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            if (mapHexagon.getRegionId() != -1) {
                if (!rs.containsKey(mapHexagon.getRegionId())) {
                    rs.put(mapHexagon.getRegionId(), new IntArray());
                }
                IntArray aroundIds = getAroundIdById(i, aroundType, tempRs);
                for (int j1 = 0; j1 < aroundIds.size; j1++) {
                    Integer j = aroundIds.get(j1);
                    if (j != -1 && j < mapHexagons.size && mapHexagons.get(j).getRegionId() != -1 && mapHexagons.get(j).getRegionId() != mapHexagon.getRegionId() && !rs.get(mapHexagon.getRegionId()).contains(mapHexagons.get(j).getRegionId())) {
                        rs.get(mapHexagon.getRegionId()).add(mapHexagons.get(j).getRegionId());
                        if (!rs.containsKey(mapHexagons.get(j).getRegionId())) {
                            rs.put(mapHexagons.get(j).getRegionId(), new IntArray());
                        }
                        if (!rs.get(mapHexagons.get(j).getRegionId()).contains(mapHexagons.get(i).getRegionId())) {
                            rs.get(mapHexagons.get(j).getRegionId()).add(mapHexagons.get(i).getRegionId());
                        }
                    }
                }
            }
            i++;
        }
        return rs;
    }

    public void logLandRegionAndParentIsSeaRegion() {
        int i = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            if ((mapHexagon.getBackTile() != 1 && mapHexagon.getBackTile() != 2) && (mapHexagons.get(mapHexagon.getRegionId()).getBackTile() == 1 || mapHexagons.get(mapHexagon.getRegionId()).getBackTile() == 2)) {
                Gdx.app.log("陆地地块但核心地块却是海洋地块", i + "");
            }
            i++;
        }
        Gdx.app.log("扫描完毕", "");
    }


    //是否是沿海城市地块
    public boolean ifCoastCity(int region) {
        if (region <= 0) {
            return false;
        }
        int i = 0;
        for (MapHexagon mapHexagon : mapHexagons) {
            i++;
           /* if(region==16587&&i==16217){
                Gdx.app.log("test",region+":"+mapBin);
            }*/


            if (mapHexagon.getRegionId() == region && mapHexagons.get(mapHexagon.getRegionId()).getBackTile() != 1 && mapHexagon.getBackTile() == 1) {
                return true;
            }
        }
        return false;
    }

    //查重
    public void checkName(int mapId) {
        XmlIntDAO an = new XmlIntDAO(game.gameConfig.reader.parse(Gdx.files.internal("config/def_areaconversion.xml")));
        XmlReader.Element anE;
        boolean ifSave = false;
        for (MapHexagon m : mapHexagons) {
            anE = an.getElementById(m.getregionAreaId());
            MapRegion region = m.getRegionData();
            if (anE != null && region != null) {
                region.setAreaId(anE.getInt("cid"));
                ifSave = true;
            }
        }
        if (ifSave) {
            game.getGameFramework().saveMapDaoByMapId(this, mapId);
        }
    }


    public void clearAllRegion() {
        int i;
        int iMax = mapHexagons.size;
        for (i = 0; i < iMax; i++) {
            mapHexagons.get(i).setRegionId(-1);
        }
    }

    //自动海陆
    public void checkCoast() {
        /*for (int i = 0; i < mapHexagons.size; i++) {
            autoCoast(i, false);
        }*/

    }


    //生成海岸
    // Gdx.app.log("autoCoast", "id:"+i+" coastId:"+c);
    public void autoCoast(int id, boolean ifAround) {
        IntArray tempIds = new IntArray();
        if (ifAround) {
            IntArray ids = getAroundIdById(id, 0, null);
            ids.add(id);

            for (int i1 = 0; i1 < ids.size; i1++) {
                Integer i = ids.get(i1);

                int c = getCoastId(i, tempIds);
                if (c != -1) {
                    mapHexagons.get(i).setBackTile(2);
                    mapHexagons.get(i).setBackIdx(c);
                    mapHexagons.get(i).setBlockType(1);
                } else if (mapHexagons.get(i).getBlockType() == 1) {
                    mapHexagons.get(i).setBackTile(1);
                    mapHexagons.get(i).setBackIdx(0);
                }
            }

        } else {
            /*
             * int s; if(id==7309) { s=1; } if (ifBorder(id)) { dealBorder(id);
             * } else
             */
            {
                int c = getCoastId(id, tempIds);
                // Gdx.app.log("autoCoast", "id:"+id+" coastId:"+c);
                if (c != -1) {
                    mapHexagons.get(id).setBackTile(2);
                    mapHexagons.get(id).setBackIdx(c);
                    mapHexagons.get(id).setBlockType(1);
                } else if (mapHexagons.get(id).getBlockType() == 1) {
                    mapHexagons.get(id).setBackTile(1);
                    mapHexagons.get(id).setBackIdx(0);
                }
            }

        }
    }


    private int getCoastBorderId(int id, IntArray ids) {

        if (ids == null) {
            ids = new IntArray();
        } else {
            ids.clear();
        }
        boolean t1 = ifSameByTypeInCoastBorder(id, 1);
        boolean t2 = ifSameByTypeInCoastBorder(id, 2);
        boolean t3 = ifSameByTypeInCoastBorder(id, 3);
        boolean t4 = ifSameByTypeInCoastBorder(id, 4);
        boolean t5 = ifSameByTypeInCoastBorder(id, 5);
        boolean t6 = ifSameByTypeInCoastBorder(id, 6);
        return GameUtil.getEBorderId(t1, t2, t3, t4, t5, t6);

    }

    public boolean ifSameByTypeInCoastBorder(int idA, int direct) {
        int idB = getIdInDirect(idA, direct);

        // Gdx.app.log("ifSameByTypeInCoastBorder", "idA:"+idA+" idB:"+idB+" direct:"+direct+ " aG:"+getBlockType(idA)+ " bG:"+getBlockType(idB));
        if (idB == -1) {
            return true;
        } else if (idB >= 0 && idB < mapHexagons.size && mapHexagons.get(idA).getBlockType() == 1 && mapHexagons.get(idB).getBlockType() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean ifSameByTypeInRiverBorder(int idA, int direct) {
        int idB = getIdInDirect(idA, direct);

        //Gdx.app.log("", "idA:"+idA+" idB:"+idB+" direct:"+direct+ " aG:"+getBlockType(idA)+ " bG:"+getBlockType(idB));
        if (idB == -1) {
            return true;
        } else if (idB >= 0 && idB < mapHexagons.size && mapHexagons.get(idA).getBackTile() == 11 && mapHexagons.get(idB).getBackTile() == 11) {
            return true;
        } else {
            return false;
        }
    }

    private int getBlockType(int idA) {
        if (idA == -1) {
            return -1;
        }
        return mapHexagons.get(idA).getBlockType();
    }

    public int getIfLat() {
        return ifLat;
    }

    public void setIfLat(int ifLat) {
        this.ifLat = ifLat;
    }

    public int getLat60N() {
        return lat60N;
    }

    public void setLat60N(int lat60N) {
        this.lat60N = lat60N;
    }

    public int getLat40N() {
        return lat40N;
    }

    public void setLat40N(int lat40N) {
        this.lat40N = lat40N;
    }

    public int getLat20N() {
        return lat20N;
    }

    public void setLat20N(int lat20N) {
        this.lat20N = lat20N;
    }

    public int getLat0() {
        return lat0;
    }

    public void setLat0(int lat0) {
        this.lat0 = lat0;
    }

    public int getLat20S() {
        return lat20S;
    }

    public void setLat20S(int lat20S) {
        this.lat20S = lat20S;
    }

    public int getLat40S() {
        return lat40S;
    }

    public void setLat40S(int lat40S) {
        this.lat40S = lat40S;
    }

    public int getLat60S() {
        return lat60S;
    }

    public void setLat60S(int lat60S) {
        this.lat60S = lat60S;
    }

    public int getTopLat() {
        return topLat;
    }

    public void setTopLat(int topLat) {
        this.topLat = topLat;
    }

    public int getFootLat() {
        return footLat;
    }

    public void setFootLat(int footLat) {
        this.footLat = footLat;
    }


    public void checkBuildName() {

        XmlIntDAO an = new XmlIntDAO(game.gameConfig.reader.parse(Gdx.files.internal("config/def_areaconversion.xml")));
        XmlReader.Element anE;
        int i = 0;
        for (MapHexagon m : mapHexagons) {
            anE = an.getElementById(m.getregionAreaId());
            MapRegion region = m.getRegionData();
            if (anE != null && m != null) {
                region.setAreaId(anE.getInt("cid"));
            }
            XmlReader.Element aE = game.gameConfig.getDEF_AREA().getElementById(m.getregionAreaId());
            if (m.getregionAreaId() != 0 && aE.getInt("continent") == 0) {
                Gdx.app.error("areaNameNoContinent", "hexagon:" + i + " id:" + aE.getInt("id") + " name:" + aE.get("name"));
            }
            i++;


        }


    }


    public void autoRiver(int id, boolean ifAround) {
        IntArray tempIds = new IntArray();
        if (ifAround) {
            IntArray ids = getAroundIdById(id, 0, null);
            ids.add(id);

            for (int i1 = 0; i1 < ids.size; i1++) {
                Integer i = ids.get(i1);
                int c = getRiverId(i, tempIds);
                if (c != -999) {
                    if (mapHexagons.get(i).getBlockType() == 1) {
                        mapHexagons.get(i).setBackTile(11);
                        mapHexagons.get(i).setBackIdx(0);
                        mapHexagons.get(i).setBackRefX(0);
                        mapHexagons.get(i).setBackRefY(0);
                    } else if (c == -1) {
                        mapHexagons.get(i).setBackTile(11);
                        mapHexagons.get(i).setBackIdx(63);
                        mapHexagons.get(i).setBackRefX(0);
                        mapHexagons.get(i).setBackRefY(0);
                    } else {
                        mapHexagons.get(i).setBackTile(11);
                        mapHexagons.get(i).setBackIdx(c);
                        mapHexagons.get(i).setBackRefX(0);
                        mapHexagons.get(i).setBackRefY(0);
                    }
                } else {

                }
            }

        } else {
            /*
             * int s; if(id==7309) { s=1; } if (ifBorder(id)) { dealBorder(id);
             * } else
             */
            int c = getRiverId(id, tempIds);
            // Gdx.app.log("autoCoast", "id:"+id+" coastId:"+c);
            if (c != -999) {
                if (mapHexagons.get(id).getBlockType() == 1) {
                    mapHexagons.get(id).setBackTile(11);
                    mapHexagons.get(id).setBackIdx(0);
                    mapHexagons.get(id).setBackRefX(0);
                    mapHexagons.get(id).setBackRefY(0);
                } else if (c == -1) {
                    mapHexagons.get(id).setBackTile(11);
                    mapHexagons.get(id).setBackIdx(63);
                    mapHexagons.get(id).setBackRefX(0);
                    mapHexagons.get(id).setBackRefY(0);
                } else {
                    mapHexagons.get(id).setBackTile(11);
                    mapHexagons.get(id).setBackIdx(c);
                    mapHexagons.get(id).setBackRefX(0);
                    mapHexagons.get(id).setBackRefY(0);
                }
            }

        }
        int c = getRiverId(id, tempIds);
        if (c != -999) {
            if (mapHexagons.get(id).getBlockType() == 1) {
                mapHexagons.get(id).setBackTile(11);
                mapHexagons.get(id).setBackIdx(0);
                mapHexagons.get(id).setBackRefX(0);
                mapHexagons.get(id).setBackRefY(0);
            } else if (c == -1) {
                mapHexagons.get(id).setBackTile(11);
                mapHexagons.get(id).setBackIdx(63);
                mapHexagons.get(id).setBackRefX(0);
                mapHexagons.get(id).setBackRefY(0);
            } else {
                mapHexagons.get(id).setBackTile(11);
                mapHexagons.get(id).setBackIdx(c);
                mapHexagons.get(id).setBackRefX(0);
                mapHexagons.get(id).setBackRefY(0);
            }
        } else {

        }
    }

    private int getCoastId(Integer id, IntArray ids) {
        if (ifGridIsPass(id)&&mapHexagons.get(id).getBlockType() != 1) {//
            return -1;
        }
        int rs = getCoastBorderId(id, ids);
        return rs - 1;

    }

    private int getRiverId(Integer id, IntArray ids) {
        if (mapHexagons.get(id).getBackTile() != 11) {//
            return -999;
        }
        int rs = getRiverBorderId(id, ids);

        //根据自己的坐标来进行偏移
        return rs - 1;

    }


    private int getRiverBorderId(int id, IntArray ids) {

        if (ids == null) {
            ids = new IntArray();
        } else {
            ids.clear();
        }
        //左上1,上2,右上3,左下4,下5,右下6
        boolean t1 = ifSameByTypeInRiverBorder(id, 1);
        boolean t2 = ifSameByTypeInRiverBorder(id, 2);
        boolean t3 = ifSameByTypeInRiverBorder(id, 3);
        boolean t4 = ifSameByTypeInRiverBorder(id, 4);
        boolean t5 = ifSameByTypeInRiverBorder(id, 5);
        boolean t6 = ifSameByTypeInRiverBorder(id, 6);

        if ((t4 || t6) && t5) {
            t5 = false;
        }
        if ((t1 || t3) && t2) {
            t2 = false;
        }
        return GameUtil.getEBorderId(t1, t2, t3, t4, t5, t6);
    }


    public int getRegionCount() {
        return regionDatas.size();
    }


    public void saveMapBin(String path) {
        //保存为新格式
        try {
            //检查格式
            IntArray regions = game.tempUtil.getTempIntArray();
            for (int i = 0, iMax = getMapbin().size; i < iMax; i++) {
                MapHexagon hexagon = getMapHexagon(i);
                if (hexagon != null && hexagon.getRegionId() >= 0 && hexagon.getRegionId() < iMax && !regions.contains(hexagon.getRegionId())) {
                    regions.add(hexagon.getRegionId());
                }
            }
            for (int i = regionDatas.size() - 1; i >= 0; i--) {
                MapRegion region = regionDatas.getByIndex(i);
                if (region != null) {
                    if (!regions.contains(region.getRegion())) {
                        regionDatas.remove(region.getRegion());
                        Gdx.app.log("saveMapBin remove region", region.getRegion() + "");
                        continue;
                    }
                }
            }
            game.tempUtil.disposeTempIntArray(regions);

            //FileOutputStream fs_out = new FileOutputStream(Path);//"D://test.bin"
            FileByte out = new FileByte();
            out.writeShort(mapVersion);// 4
            out.writeShort(mapWidth);// 4
            out.writeShort(mapHeight);// 4
            out.writeInt(regionDatas.size());// 8

            for (int i = 0, iMax = getMapbin().size; i < iMax; i++) {
                MapHexagon hexagon = getMapHexagon(i);
                if (hexagon != null) {
                    out.writeByte(hexagon.getBlockType());// 2
                    out.writeByte(hexagon.getBackTile());// 2
                    out.writeByte(hexagon.getBackIdx());// 2
                    out.writeByte(hexagon.getBackRefXValue());// 2
                    out.writeByte(hexagon.getBackRefYValue());// 2
                    out.writeByte(hexagon.getForeTile());// 2
                    out.writeByte(hexagon.getForeIdx());// 2
                    out.writeByte(hexagon.getForeRefXValue());// 2
                    out.writeByte(hexagon.getForeRefYValue());// 2
                    out.writeByte(hexagon.getPresetRailway());// 2
                    out.writeByte(hexagon.ifCoast);// 2
                    out.writeInt(hexagon.getHexagonDataByRegion().getRegionId());// 8
                    if (hexagon.getRegionId() >= 0 && hexagon.getRegionId() < iMax && !regions.contains(hexagon.getRegionId())) {
                        regions.add(hexagon.getRegionId());
                    }
                }
            }
            if (regionDatas.size() > 0) {
                for (int i = 0, iMax = regionDatas.size(); i < iMax; i++) {
                    MapRegion region = regionDatas.getByIndex(i);
                    if (region != null) {
                        out.writeInt(region.getRegion());// 8
                        out.writeByte(region.getClimateZone());// 2
                        out.writeByte(region.getDepLv());//2
                        out.writeByte(region.getMineralLv());//2
                        out.writeByte(region.getFoodLv());// 2
                        out.writeByte(region.getOilLv());//2
                        out.writeShort(region.getAreaId());//4
                        out.writeShort(region.getStrategicRegion());//4
                    }
                }
            }


            FileHandle file = Gdx.files.local(path);
            file.writeBytes(out.getByte(), false);
            //out.writeInt(i);//8位  2147483647
            //out.writeShort(i2);//4位 32769
            //out.writeByte(i3);//2位  127

        } catch (FileNotFoundException fe) {
            System.err.println(fe);
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
        System.out.println("Ok");
    }


    public boolean ifRegionBorder(int id, int direct) {
        int bId = getBorderIdByDirect(id, direct);
        if (bId == -1) {
            return false;
        }
        int c1 = mapHexagons.get(bId).getRegionId();
        int c2 = mapHexagons.get(id).getRegionId();
        if (id != bId && c1 != c2) {
            return true;
        }
        return false;
    }

    public boolean ifSea(int hexagon) {
        if (hexagon >= 0 && hexagon < mapHexagons.size && mapHexagons.get(hexagon).getBlockType() == 1) {
            return true;
        }
        return false;
    }

    public int getCoast(int hexagon) {
        if (hexagon >= 0 && hexagon < mapHexagons.size && mapHexagons.get(hexagon).getIfCoast() > 0) {
            return mapHexagons.get(hexagon).getIfCoast();
        }
        return -1;
    }

    public int getBorderIdByDirect(int id, int direct) {
        if (direct == 0) {
            return id;
        }

        boolean top = false;
        boolean foot = false;
        boolean left = false;
        boolean right = false;
        //判断处于哪个边
        int y = (int) id / getMapWidth();
        int x = id - y * getMapWidth();
        int t1, t2, t3, t4, t5, t6;
        boolean ifParity = (x & 1) == 1;
        if (ifParity) {
            t1 = id - 1;
            t2 = id - getMapWidth();
            t3 = id + 1;
            t4 = id + getMapWidth() - 1;
            t5 = id + getMapWidth();
            t6 = id + getMapWidth() + 1;
        } else {
            t1 = id - getMapWidth() - 1;
            t2 = id - getMapWidth();
            t3 = id - getMapWidth() + 1;
            t4 = id - 1;
            t5 = id + getMapWidth();
            t6 = id + 1;
        }
        //如果不循环
        if ((btl != null && !btl.ifLoop) || !isLoop()) {
            if (x == 0) {
                left = true;
            }
            if (x == getMapWidth() - 1) {
                right = true;
            }
        }
        if (y == 0) {
            top = true;
        }
        if (y == getMapWidth() - 1) {
            foot = true;
        }

        if (ifParity) {
            if (!left) {
                if (direct == 1) {
                    return (t1);
                }
            }
            if (!top) {
                if (direct == 2) {
                    return (t2);
                }
            }
            if (!right) {
                if (direct == 3) {
                    return (t3);
                }
            }
            if (!foot && !left) {
                if (direct == 4) {
                    return (t4);
                }
            }
            if (!foot) {
                if (direct == 5) {
                    return (t5);
                }
            }
            if (!foot && !right) {
                if (direct == 6) {
                    return (t6);
                }
            }
        } else {
            if (!top && !left) {
                if (direct == 1) {
                    return (t1);
                }
            }
            if (!top) {
                if (direct == 2) {
                    return (t2);
                }
            }
            if (!top && !right) {
                if (direct == 3) {
                    return (t3);
                }
            }
            if (!left) {
                if (direct == 4) {
                    return (t4);
                }
            }
            if (!foot) {
                if (direct == 5) {
                    return (t5);
                }
            }
            if (!right) {
                if (direct == 6) {
                    return t6;
                }
            }
        }
        return -1;
    }


    public boolean ifDrawTrapezoidForDirect(int id, int directType) {
        //如果x方向是海,并且x方也是coast 则为true
        int bId, cId;
        switch (directType) {
            case 1:
                bId = getBorderIdByDirect(id, 1);
                cId = getBorderIdByDirect(id, 2);
                if (ifSea(bId) && getCoast(cId) > 0) {
                    return true;
                } else {
                    return false;
                }
            case 3:
                bId = getBorderIdByDirect(id, 3);
                cId = getBorderIdByDirect(id, 2);
                if (ifSea(bId) && getCoast(cId) > 0) {
                    return true;
                } else {
                    return false;
                }
            case 4:
                bId = getBorderIdByDirect(id, 4);
                cId = getBorderIdByDirect(id, 5);
                if (ifSea(bId) && getCoast(cId) > 0) {
                    return true;
                } else {
                    return false;
                }
            case 6:
                bId = getBorderIdByDirect(id, 6);
                cId = getBorderIdByDirect(id, 5);
                if (ifSea(bId) && getCoast(cId) > 0) {
                    return true;
                } else {
                    return false;
                }
        }


        return false;
    }


    public void init() {
        if (tempIntArray == null) {
            tempIntArray = new IntArray();
        } else {
            tempIntArray.clear();
        }
        if (tempIntIntMap == null) {
            tempIntIntMap = new IntIntMap();
        } else {
            tempIntIntMap.clear();
        }
        int mapH_px = GameMap.getH_px(getMapWidth(), getMapHeight(), 1);
     /*   float mapTile_refX= ResDefaultConfig.Map.MAPTILE_REFX* ResDefaultConfig.Map.MAP_SCALE;
        float mapTile_refY= ResDefaultConfig.Map.MAPTILE_REFY* ResDefaultConfig.Map.MAP_SCALE;*/
        float mapTile_refX = 0;
        float mapTile_refY = 0;
        //initCoastBorderByRegion();
        for (int i = 0; i < getMapbin().size; i++) {
            MapHexagon m = getMapHexagon(i);
            if (m != null) {
                m.setBorder(getCoastBorder(i));
                if (getCoastGrid().contains(i)) {
                    m.ifCoast = GameMethod.getFBorderIdByString(m.getBorder());
                }
            }
            updVir(i, mapH_px, mapTile_refX, mapTile_refY);
        }
        logMapInfo();
        mapE = game.gameConfig.getDEF_MAP().getElementById(mapId);
        if (mapE != null) {
            ifLat = mapE.getInt("ifLat", 0);
            lat60N = mapE.getInt("lat60N", 0);
            lat40N = mapE.getInt("lat40N", 0);
            lat20N = mapE.getInt("lat20N", 0);
            lat0 = mapE.getInt("lat0", 0);
            lat20S = mapE.getInt("lat20S", 0);
            lat40S = mapE.getInt("lat40S", 0);
            lat60S = mapE.getInt("lat60S", 0);
            topLat = mapE.getInt("topLat", 0);
            footLat = mapE.getInt("footLat", 0);
            //ifLoop=xE.getBoolean("ifLoop",false);
        }
        mapRefX = 0;
        mapRefY = 0;
        onlyZoomShow = true;
        alphaRate = 0f;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                mapImage = GameMap.getMapImage(game, mapE);
                if (mapImage != null) {
                    if (mapE != null) {
                        mapRefX = mapE.getInt("imageRefX", 0);
                        mapRefY = mapE.getInt("imageRefY", 0);
                        onlyZoomShow = mapE.getBoolean("onlyZoomShow", true);
                        if (game.gameConfig.ifColor && !ComUtil.isEmpty(mapE.get("colorImage", ""))) {
                            alphaRate = mapE.getFloat("colorAlphaRate", mapE.getFloat("alphaRate", 1f));
                        } else {
                            alphaRate = mapE.getFloat("alphaRate", 1f);
                        }
                    }
                }
                floorImage = GameMap.getFloorMapImage(game, mapE);
                if (mapE != null) {
                    floorHexagonAlphaRate = mapE.getFloat("floorHexagonAlphaRate", 1f);
                }else{
                    floorHexagonAlphaRate=game.resGameConfig.defaultFloorHexagonAlphaRate;
                }
            }
        });
    }

    private void logMapInfo() {
        //左下坐标
        int id1 = GameMap.getId(0, mapHeight - 1, mapWidth);
        MapHexagon mapHexagon1 = getMapHexagon(id1);
        //右上坐标
        int id2 = GameMap.getId(mapWidth - 1, 0, mapWidth);
        MapHexagon mapHexagon2 = getMapHexagon(id2);
        float x1 = mapHexagon1.source_x - ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE;
        float y1 = mapHexagon1.source_y - ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE;
        float x2 = mapHexagon2.source_x - ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE;
        float y2 = mapHexagon2.source_y - ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE;

        Gdx.app.log("logMapInfo", "起点:" + id1 + " " + x1 + " " + y1 + " 终点:" + id2 + " " + x2 + " " + y2 + " 宽:" + (x2 - x1 + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE) + " 高:" + (y2 - y1 + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF * ResDefaultConfig.Map.MAP_SCALE * 2));
    }


    public void resetCoastBorder(int id) {
        int mapH_px = GameMap.getH_px(getMapWidth(), getMapHeight(), 1);
        float mapTile_refX = 0;
        float mapTile_refY = 0;
        IntArray ids = getAroundIdById(id, 0, null);
        ids.add(id);
        for (int i1 = 0; i1 < ids.size; i1++) {
            Integer i = ids.get(i1);
            MapHexagon m = mapHexagons.get(i);
            m.setBorder(getCoastBorder(i));
            if (getCoastGrid().contains(i)) {
                m.ifCoast = GameMethod.getFBorderIdByString(m.getBorder());
            }
            updVir(i, mapH_px, mapTile_refX, mapTile_refY);
        }
    }

    public void resetAllCoastBorder() {
        int mapH_px = GameMap.getH_px(getMapWidth(), getMapHeight(), 1);
        float mapTile_refX = 0;
        float mapTile_refY = 0;
        //IntArray ids = getAroundIdById(id, 0, null);
        // ids.add(id);
        for (int i = 0; i < mapHexagons.size; i++) {
            MapHexagon m = mapHexagons.get(i);
            m.setBorder(getCoastBorder(i));
            if (getCoastGrid().contains(i)) {
                m.ifCoast = GameMethod.getFBorderIdByString(m.getBorder());
            }
        }
        for (int i = 0; i < mapHexagons.size; i++) {
            MapHexagon m = mapHexagons.get(i);
            m.setBorder(getCoastBorder(i));
            if (getCoastGrid().contains(i)) {
                m.ifCoast = GameMethod.getFBorderIdByString(m.getBorder());
            }
            /*if(m.ifCoast==0&&m.getBackTile()==2){
                m.setBackTile(1);
                m.setBackIdx(0);
            }*/
            autoCoast(m.getHexagonIndex(), true);
            updVir(i, mapH_px, mapTile_refX, mapTile_refY);
        }
    }


    public int getEmptyRegion(int id) {
        for (int i = id + 1, iMax = mapHexagons.size; i < iMax; i++) {
            if (!ifAroundHaveSameRegion(i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean ifAroundHaveSameRegion(int id) {
        IntArray ids = getAroundIdById(id, 0, null);
        int region = getRegionById(id);
        for (int i1 = 0; i1 < ids.size; i1++) {
            Integer i = ids.get(i1);
            if (getRegionById(i) == region) {
                return true;
            }
        }
        return false;
    }


    public int getBigRegionId(int i) {
        tempIntIntMap.clear();
        int c, r;
        for (int iMax = mapHexagons.size; i < iMax; i++) {
            MapHexagon m = mapHexagons.get(i);
            r = m.getRegionId();
            c = 1;
            if (tempIntIntMap.containsKey(r)) {
                c = tempIntIntMap.get(r, 0) + 1;
                tempIntIntMap.put(r, c);
            } else {
                tempIntIntMap.put(r, c);
            }
            if (ifSea(r)) {
                if (c > 40) {
                    return r;
                }
            } else {
                if (c > 30) {
                    return r;
                }
            }
            if (i > iMax) {
                break;
            }
        }
       /* Iterator<IntIntMap.Entry> it = tempIntIntMap.iterator();
            int c1=0,c2=0;
        while (it.hasNext()) {
          r=it.next().key;
            try {
                c=it.next().value;
            } catch (Exception e) {
                continue;
            }
            if(ifSea(r)){
                if(c>50){
                    c1++;
                    //  return r;
                }
            }else{
                if(c>40){
                    c2++;
                    //return r;
                }
            }
        }*/

        return -1;
    }


    public int getSmallRegionId(int i) {
        tempIntIntMap.clear();
        int c, r;
        for (int iMax = mapHexagons.size; i < iMax; i++) {
            MapHexagon m = mapHexagons.get(i);
            r = m.getRegionId();
            c = 1;
            if (tempIntIntMap.containsKey(r)) {
                c = tempIntIntMap.get(r, 0) + 1;
                tempIntIntMap.put(r, c);
            } else {
                tempIntIntMap.put(r, c);
            }

            if (i > iMax) {
                break;
            }
        }
        Iterator<IntIntMap.Entry> it = tempIntIntMap.iterator();
        int c1 = 0, c2 = 0;
        while (it.hasNext()) {
            r = it.next().key;
            try {
                c = it.next().value;
            } catch (Exception e) {
                continue;
            }
            if (ifSea(r)) {
                if (c < 5) {
                    c1++;
                    Gdx.app.log("SmallRegion", r + ":" + c);
                    return r;
                }
            } else {
                if (c < 5) {
                    c2++;
                    Gdx.app.log("SmallRegion", r + ":" + c);
                    return r;
                }
            }
        }

        return -1;
    }

    public void setRegionZone(int id, int zone) {
        int region = getRegionById(id);
        if (region == -1) {
            return;
        }
        MapRegion regionData = regionDatas.getByKey(region);
        if (regionData == null && region < mapHexagons.size) {
            regionData = new MapRegion();
            regionData.setRegion(region);
            regionDatas.put(region, regionData);
        }
        regionData.setClimateZone(zone);
    }

    public void setRegionSR(int id, int value) {
        int region = getRegionById(id);
        if (region == -1) {
            return;
        }
        MapRegion regionData = regionDatas.getByKey(region);
        if (regionData == null && region < mapHexagons.size) {
            regionData = new MapRegion();
            regionData.setRegion(region);
            regionDatas.put(region, regionData);
        }
        regionData.setStrategicRegion(value);
    }


    public void autoSetClimatZoneByLat() {
        if (ifLat != 1) {
            return;
        }
        int y;
        for (int i = 0, iMax = mapHexagons.size; i < iMax; i++) {
            MapHexagon m = mapHexagons.get(i);
            if (m.getBlockType() != 1 && m.getRegionClimatZone() == 0 && m.getRegionId() > 0) {
                y = GameMap.getHY(m.getRegionId(), mapWidth);
                if (y > lat60N) {
                    setRegionZone(m.getRegionId(), 11);
                } else if (y > lat40N) {
                    setRegionZone(m.getRegionId(), 5);
                } else if (y > lat20N) {
                    setRegionZone(m.getRegionId(), 5);
                } else if (y > lat0) {
                    setRegionZone(m.getRegionId(), 8);
                } else if (y > lat20S) {
                    setRegionZone(m.getRegionId(), 7);
                } else if (y > lat40S) {
                    setRegionZone(m.getRegionId(), 8);
                } else if (y > lat60S) {
                    setRegionZone(m.getRegionId(), 11);
                } else {
                    setRegionZone(m.getRegionId(), 0);
                }
            }

        }


    }

    public boolean ifGridIsPass(int id) {
        if (id < 0 || id >= getMapHeight() * getMapWidth()) {
            return false;
        }
        return true;
    }


    public void updVir(int id, int mapH_px, float mapTile_refX, float mapTile_refY) {
        MapHexagon m = mapHexagons.get(id);
        m.regionLineBorderTile = game.getImgLists().getTextureByName(DefDAO.getLineBorder(ifRegionBorder(id, 1, false), ifRegionBorder(id, 2, false), ifRegionBorder(id, 3, false)));
        int sx = GameMap.getHX(id, getMapWidth()) + 1;
        int sy = GameMap.getHY(id, getMapWidth()) + 1;
        //float  mapH_px=GameMap.getH_px(masterData.getWidth(), masterData.getHeight(), 1);
        //因为地图的边缘相接部分并不是取最右,而是右数第二个坐标相接来实现地图循环
        m.source_x = GameMap.getX_pxByHexagon(sx, 1, mapTile_refX);
        m.source_y = GameMap.getY_pxByHexagon(sx, sy, mapH_px, 1, mapTile_refY, true);
        /*if(m.getHexagonIndex()==55130){
            int s=0;
        }*/
        TextureRegionDAO tt;
        if (m.getBackTile() != 0) {
            tt = game.getImgLists().getTextureByName(m.getBackTile() + "_" + m.getBackIdx());
            if (tt != null) {
                m.tile1 = tt;
            }
        }
        if (m.getForeTile() != 0) {
            tt = game.getImgLists().getTextureByName(m.getForeTile() + "_" + m.getForeIdx());
            if (tt != null) {
                m.tile2 = tt;
            }
        }

        //
        // m.ifDrawColor =m.ifDrawColor();
        m.ifSeaLand = ifSeaLand(m.getRegionId());
    }


    public boolean ifSeaLand(int Id) {
        //  regionId=getRegionId(regionId);
        if (ifGridIsPass(Id) && !ifSea(Id) && mapHexagons.get(Id).ifCoast == 63) {
            return true;
        }
        return false;
    }

    public boolean ifRegionSeaLand(int regionId) {
        regionId = getRegionId(regionId);
        if (ifGridIsPass(regionId) && !ifSea(regionId) && mapHexagons.get(regionId).ifCoast == 63) {
            return true;
        }
        return false;
    }

    public boolean ifRegionBorder(int id, int direct, boolean ifHaveCoast) {
        int bId = getBorderIdByDirect(id, direct);
        if (bId == -1) {
            return false;
        }
        int c1 = getRegionId(bId);
        int c2 = getRegionId(id);
        if (ifHaveCoast) {
            if (bId != -1 && bId != id && c1 != c2 && c1 != -1 && c2 != -1) {
                return true;
            }
        } else {
            if (bId != -1 && bId != id && c1 != c2 && c1 != -1 && c2 != -1 && ifSameLandOrSea(id, bId)) {
                return true;
            }
        }


        return false;
    }

    private boolean ifSameLandOrSea(int id1, int id2) {
        int t1 = mapHexagons.get(id1).getBlockType();
        int t2 = mapHexagons.get(id2).getBlockType();
        if ((t1 == 1 && t2 != 1) || (t1 != 1 && t2 == 1)) {
            return false;
        } else {
            return true;
        }
    }

    private int getRegionId(int id) {
        if (!ifGridIsPass(id)) {
            return -1;
        }
        return mapHexagons.get(id).getRegionId();

    }


    public MapHexagon getRegionMapbinId(int i) {
        MapHexagon m = mapHexagons.get(i);
        if (m != null && m.getRegionId() != 0 && ifGridIsPass(m.getRegionId())) {
            return mapHexagons.get(m.getRegionId());
        }
        return m;
    }

    //寻找没有找到sr的问题
    public void checkSR() {
        int i = 0;
        IntArray tempIntArray = game.tempUtil.getTempIntArray();
        for (MapHexagon m : mapHexagons) {
            if (m.getRegionId() == i && m.getRegionStrategicRegion() == 0 && !tempIntArray.contains(m.getRegionId())) {
                tempIntArray.add(m.getRegionId());
            }
            i++;
        }
        Gdx.app.log("region sr is 0", tempIntArray.toString());
        game.tempUtil.disposeTempIntArray(tempIntArray);
    }


    public void checkIsletRegion() {
        int i = 0;
        IntArray rs = game.tempUtil.getTempIntArray();
        IntArray rs2 = game.tempUtil.getTempIntArray();
        for (i = 0; i < mapHexagons.size; i++) {
            //获得结果值
            rs = getAroundIdById(i, 0, rs);
            MapHexagon mapHexagon = mapHexagons.get(i);
            int js = 0;
            int index;
            //判断是否是孤岛
            for (int j = 0; j < rs.size; j++) {
                index = rs.get(j);
                if (ifGridIsPass(index)) {
                    MapHexagon mapHexagon2 = mapHexagons.get(index);
                    if (mapHexagon2.getRegionId() == mapHexagon.getRegionId()) {
                        js++;
                    }
                }
            }
            if (js == 0) {
                rs2.add(mapHexagon.getRegionId());
            }
        }
        Gdx.app.log("checkRegion", rs2.toString());

        game.tempUtil.disposeTempIntArray(rs);
        game.tempUtil.disposeTempIntArray(rs2);

    }

    public MapRegion getMapRegion(int regionId) {
        return regionDatas.getByKey(regionId);
    }

    public void init(Fb2Smap smap) {
        this.game = game;
        this.mapId = mapId;
        coastGrid = new IntArray();
        //regionGrid = new IntArray();
        XmlStringDAO ruleE = game.gameConfig.getRULE_MAPBIN();
        //bm0 初始数据获取
        {
            this.mapVersion = ResDefaultConfig.Map.VERSION;
            this.mapId = smap.masterData.getMapId();
            this.mapWidth = smap.masterData.getWidth();
            this.mapHeight = smap.masterData.getHeight();
            this.regionCount = smap.buildRDatas.size();
            this.mapHexagons = smap.hexagonDatas;
        }
        /*re = ruleE.getElementById("bm1").getChildrenByName("bm");
        c = sum;
        for (int i = 0; i < c; i++) {
            MapHexagon mapHexagon = new MapHexagon();
            mapHexagon.setBlockType(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setBackTile(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setBackIdx(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setBackRefX(GameUtil.getCoverStr(buf, tag, 2));
            tag +=2;
            mapHexagon.setBackRefY(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setForeTile(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setForeIdx(GameUtil.getCoverStr(buf, tag, 2));
            tag +=2;
            mapHexagon.setForeRefX(GameUtil.getCoverStr(buf, tag,2));
            tag += 2;
            mapHexagon.setForeRefY(GameUtil.getCoverStr(buf, tag,2));
            tag += 2;
            mapHexagon.setTilePass(GameUtil.getCoverStr(buf, tag,2));
            tag += 2;
            mapHexagon.ifCoast=(GameUtil.getCoverStr(buf, tag, 2));
            tag += 2;
            mapHexagon.setRegionId(GameUtil.getCoverStr(buf, tag, 8));
            tag +=8;




            this.mapHexagons.add(mapHexagon);
           *//* if (!regionGrid.contains(mapHexagon.getRegionId()) && mapHexagon.getRegionId() >= 0) {
                regionGrid.add(mapHexagon.getRegionId());
            }*//*
        }*/
        regionDatas = new ZHIntMap<>();
        for (int i = 0; i < regionCount; i++) {
            Fb2Smap.BuildData build = smap.buildRDatas.getByIndex(i);
            if (build != null) {
                MapRegion mapRegion = new MapRegion();
                mapRegion.setRegion(build.getRegionId());
                mapRegion.setClimateZone(build.getClimateZone());
                mapRegion.setDepLv(build.getDevelopLv());
                mapRegion.setMineralLv(build.getMineralLv());
                mapRegion.setFoodLv(build.getFoodLvNow());
                mapRegion.setOilLv(build.getOilLv());
                mapRegion.setAreaId(build.getAreaZone());
                mapRegion.setStrategicRegion(build.getStrategicRegion());
                regionDatas.put(mapRegion.getRegion(), mapRegion);
            }

        }
        this.coastGrid = getIdsByAround(12);
        init();
    }

    public void updAllHexagonBorderAttribute() {
        for (int i = 0; i < mapHexagons.size; i++) {
            MapHexagon h = mapHexagons.get(i);
            h.updHexagonBorderAttribute();
        }
    }


    public void fixRiver() {
        for (int i = 0, iMax = getMapbin().size; i < iMax; i++) {
            MapHexagon hexagon = getMapHexagon(i);
            if (hexagon != null && hexagon.getForeTile() == 11) {
                hexagon.setBackTile(hexagon.getForeTile());
                hexagon.setBackIdx(hexagon.getForeIdx());
                hexagon.setBackRefX(0);
                hexagon.setBackRefY(0);
                hexagon.setForeTile(0);
                hexagon.setForeIdx(0);
                hexagon.setForeRefX(0);
                hexagon.setForeRefY(0);
            }
        }


    }


    //TODO
    public void resetMap(int w, int h, int defaultT) {


    }

    //根据other来更新地图的数据
    public void initOtherMapTile() {
        for (int i = 0; i < mapHexagons.size; i++) {
            MapHexagon h = mapHexagons.get(i);
            /*if(h.getBlockType()==1&&h.getBackTile()==1&&!h.ifAroundGridIsCoastSea()){
                if(ComUtil.ifGet(20)){
                    h.setForeTile(9);
                    h.setForeIdx(ComUtil.getRandom(1,8));
                    h.setOtherTile(90+h.getForeIdx());
                    h.setTile2(game.getImgLists().getTextureByName(h.getForeTile()+"_"+h.getForeIdx()));
                }else{
                    h.setOtherTile(0);
                    h.setForeTile(0);
                    h.setForeIdx(0);
                    h.setTile2(null);
                }
            }*/
            if (h.getOtherTile() > 90 && h.getOtherTile() < 100) {
                h.setForeTile(9);
                h.setForeIdx(h.getOtherTile() - 90);
                h.setTile2(game.getImgLists().getTextureByName(h.getForeTile() + "_" + h.getForeIdx()));
            }
        }

    }






   /* public void saveNewFormat() {

        XmlReader reader = game.gameConfig.reader;
        String str = "";
        XmlReader.Element root = game.gameConfig.getDEF_MAP().getElementById(mapId);
        int childNum = root.getChildCount();
        str = root.get("name");
        String path="bin/" + str + "_n.bin";

        //保存为新格式
        try {
            IntArray regionIntArray=game.tempUtil.getTempIntArray();
            for (int i = 0, iMax = getMapbin().size; i < iMax; i++) {
                MapHexagon hexagon=getMapbin().get(i);
                if(hexagon.getRegionId()!=-1&&!regionIntArray.contains(hexagon.getRegionId())){
                    regionIntArray.add(hexagon.getRegionId());
                }
            }
            //FileOutputStream fs_out = new FileOutputStream(Path);//"D://test.bin"
            FileByte out = new FileByte();
            out.writeShort(mapVersion);// 4
            out.writeShort(mapWidth);// 4
            out.writeShort(mapHeight);// 4
            out.writeInt(regionIntArray.size);// 8

            for (int i = 0, iMax = getMapbin().size; i < iMax; i++) {
                MapHexagon hexagon=getMapbin().get(i);
                out.writeByte(hexagon.getBlockType());// 2
                out.writeByte(hexagon.getBackTile());// 2
                out.writeByte(hexagon.getBackIdx());// 2
                out.writeByte(hexagon.getBackRefX());// 2
                out.writeByte(hexagon.getBackRefY());// 2
                out.writeByte(hexagon.getForeTile());// 2
                out.writeByte(hexagon.getForeIdx());// 2
                out.writeByte(hexagon.getForeRefX());// 2
                out.writeByte(hexagon.getForeRefY());// 2
                out.writeByte(hexagon.getTilePass());// 2
                out.writeByte(hexagon.ifCoast);// 2
                out.writeInt(hexagon.getHexagonDataByRegion().getRegionId());// 8
            }
          if(regionIntArray.size>0){
              regionIntArray.clear();
              for (int i = 0, iMax = getMapbin().size; i < iMax; i++) {
                  MapHexagon hexagon=getMapbin().get(i);
                  if(hexagon.getRegionId()!=-1&&!regionIntArray.contains(hexagon.getRegionId())){
                      regionIntArray.add(hexagon.getRegionId());
                      out.writeInt(hexagon.getHexagonDataByRegion().getRegionId());// 8
                      out.writeByte(hexagon.getClimateZone());// 2
                      out.writeByte(hexagon.getHexagonDataByRegion().getTempClimateId());// 2
                      out.writeByte(hexagon.getHexagonDataByRegion().getRegionMineralLv());//2
                      out.writeByte(hexagon.getHexagonDataByRegion().getRegionDepLv());//2
                      out.writeByte(hexagon.getHexagonDataByRegion().getRegionOilLv());//2
                      out.writeShort(hexagon.getHexagonDataByRegion().getregionAreaId());//4
                      out.writeShort(hexagon.getHexagonDataByRegion().getTempStrategicRegion());//4
                  }
              }
          }

            game.tempUtil.disposeTempIntArray(regionIntArray);

            FileHandle file = Gdx.files.local(path);
            file.writeBytes(out.getByte(), false);
            //out.writeInt(i);//8位  2147483647
            //out.writeShort(i2);//4位 32769
            //out.writeByte(i3);//2位  127

        } catch (FileNotFoundException fe) {
            System.err.println(fe);
        } catch (IOException ioe) {
            System.err.println(ioe);
        }


    }*/

    /*public void temp_logAreaName() {
        int i = 0;
        for (MapHexagon m : mapHexagons) {
            if (m.getAreaId() >= 3000) {
                Gdx.app.log("temp_logAreaName", i + ":" + m.getRegionId() + ":" + m.getAreaId());
            }
            i++;
        }
    }*/
    public static class MapRegion {
        private int region;
        private int climateZone;
        private int depLv;//建筑等级(最高建造等级=时代等级+此等级)
        private int mineralLv;//建筑类型1城市2工厂3机场4军港5油库
        private int foodLv;// 气候类型
        private int oilLv;//设施
        private int areaId;//区域名称 无为0
        public int strategicRegion;

        public int getRegion() {
            return region;
        }

        public void setRegion(int region) {
            this.region = region;
        }

        public int getClimateZone() {
            return climateZone;
        }

        public void setClimateZone(int climateZone) {
            this.climateZone = climateZone;
        }

        public int getFoodLv() {
            return foodLv;
        }

        public void setFoodLv(int foodLv) {
            this.foodLv = ComUtil.limitValue(foodLv, 0, 15);
        }

        public int getStrategicRegion() {
            return strategicRegion;
        }

        public void setStrategicRegion(int strategicRegion) {
            this.strategicRegion = strategicRegion;
        }

        public int getMineralLv() {
            return mineralLv;
        }

        public void setMineralLv(int mineralLv) {
            this.mineralLv = mineralLv;
        }

        public int getDepLv() {
            return depLv;
        }

        public void setDepLv(int depLv) {
            this.depLv = depLv;
        }

        public int getOilLv() {
            return oilLv;
        }

        public void setOilLv(int oilLv) {
            this.oilLv = oilLv;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }
    }


    public MapHexagon getNewMapHexagon() {
        return new MapHexagon();
    }


    public class MapHexagon {

        public MapHexagon() {

        }

        private int blockType;// terrainimg的type -1不启用 0底图通用 1海洋 3固定(海岸,桥梁等) 4雪地 5绿山 6热带草地 7沙漠 8黄山
        private int backTile;// 底层地形 terrainimg的id -1不可通过的地方,0平地,1海洋,2海岸,3桥梁,4丘陵,5山地,6森林,7人为装饰,8自然装饰,9海洋装饰,10道路,11河流
        private int backIdx;// 图像编号 terrainimg的idx
        private int backRefX;// 偏移横
        private int backRefY;// 偏移纵
        private int foreTile;// 面层地形 terrainimg的id
        private int foreIdx;// 图像编号 terrainimg的idx
        private int foreRefX;// 底层偏移x
        private int foreRefY;// 底层偏移y
        private int presetRailway;// 预置铁路信息
        //  private int climateZone;// 0无 1平原 2沙漠 3冷带 4山地 5丘陵 6森林 7树林 8热带雨林 9沼泽/湿地 10海洋
        private int regionId;// 核心地块


        private int sourceX;//坐标x位置
        private int sourceY;//坐标y位置
        private String border;//临边  0000  各个位置0代表不绘制,1代表绘制

        //寻路算法给的值
        public MapHexagon parent;
        public int speed;
        public int F;
        public int G;
        public int H;


        public TextureRegionDAO regionLineBorderTile;

        public int getSourceX() {
            return sourceX;
        }

        public void setSourceX(int sourceX) {
            this.sourceX = sourceX;
        }

        public int getSourceY() {
            return sourceY;
        }

        public void setSourceY(int sourceY) {
            this.sourceY = sourceY;
        }

        public String getBorder() {
            return border;
        }

        public void setBorder(String border) {
            this.border = border;
        }

        //--------------------------------temp---------------------------------------
        private int otherTile;// 其他装饰 0无 1铁路 20~29海洋
        private int ifClimateZone;// 0无 1平原 2沙漠 3冷带 4山地 5丘陵 6森林 7树林 8热带雨林 9沼泽/湿地 10海洋
        private int hexagonIndex;//地块坐标
        private int legionIndex;//军团归属  当一个建筑的血量被清空时,该军团归属归到0
        private int ifFog; //是否可见 0不可见 1可见
        private TextureRegionDAO tile1;
        private TextureRegionDAO tile2;
        public float source_x;
        public float source_y;
        // private boolean ifDraw;

        public Fb2Smap.ArmyData armyData;
        public Fb2Smap.FacilityData facilityData;
        public Fb2Smap.BuildData buildData;
        public Fb2Smap.FortData fortData;


        //不能用这个判断,这个有间隔性,无法准确判断
        public int countryBorder;

        //   public TextureRegionDAO regionLineBorderTile;
        public TextureRegionDAO countryLineBorderTile;
        public TextureRegionDAO countryInnerBorderTile;
        public TextureRegionDAO countryOuterBorderTile;
        public TextureRegionDAO railwayTile;
        public boolean ifRailWayMark;

        public Color countryInnerBorderColor;
        public Color countryOuterBorderColor;

        public Color countryBorderColor1;
        public Color countryBorderColor2;
        public Color countryBorderColor3;
        public Color countryBorderColor4;
        public Color countryBorderColor5;
        public Color countryBorderColor6;

        public boolean ifSeaLand;
        public boolean ifMoreCountryBorder;
        public boolean isCountryBorder;//是否是国界


        //--------------------------------temp---------------------------------------

        public void initGrid() {
            this.blockType = 0;// terrainimg的type -1不可通过的地方,0平地,1海洋,2海岸,3桥梁,4丘陵,5山地,6森林,7人为装饰,8自然装饰,9海洋装饰,10道路,11河流
            this.backTile = 0;// 底层地形 terrainimg的id -1不可逾越物
            this.backIdx = 0;// 图像编号 terrainimg的idx
            this.backRefX = 0;// 偏移横
            this.backRefY = 0;// 偏移纵
            this.foreTile = 0;// 面层地形 terrainimg的id
            this.foreIdx = 0;// 图像编号 terrainimg的idx
            this.foreRefX = 0;// 底层偏移x
            this.foreRefY = 0;// 底层偏移y
            this.presetRailway = 0;// 预置铁路信息
            // this.climateZone = 0;// 陆边缘
            this.regionId = 0;// 核心地块
            //this.tempClimateId = 0;// 气候类型
            //this.tempMineralLv = 0;//建筑类型1城市2工厂3机场4军港5油库

            //this.tempDepLv = 0;//建筑等级(最高建造等级=时代等级+此等级)
            //this.tempOilLv = 0;//设施
            //this.tempAreaId = 0;//区域名称 无为0
            this.sourceX = 0;//坐标x位置
            this.sourceY = 0;//坐标y位置
            //    this.strategicRegion = 0;
            this.border = "";
            this.parent = null;

        }

        public void log(int id) {
            Gdx.app.log("MapBin:" + id, " blockType:" + blockType
                            + " backTile:" + backTile
                            + " backIdx:" + backIdx
                            + " backRefX:" + backRefX
                            + " backRefY:" + backRefY
                            + " foreTile:" + foreTile
                            + " foreIdx:" + foreIdx
                            + " foreRefX:" + foreRefX
                            + " foreRefY:" + foreRefY
                            + " ifCoast:" + ifCoast
                    //  + " area:" + tempAreaId

            );

        }

        public int ifCoast;


        public MapHexagon getHexagonDataByRegion() {
            if (regionId < 0) {
                return this;
            }
            return mapHexagons.get(regionId);
        }

        public MapHexagon getHexagonDataByBorder(int direct) {
            int id = getBorderIdByDirect(hexagonIndex, direct);
            return getMapHexagon(id);
        }

        public MapRegion getRegionData() {
            if (regionId < 0) {
                return null;
            }
            MapRegion region = regionDatas.getByKey(regionId);
            if (region == null && regionId < mapHexagons.size) {
                region = new MapRegion();
                region.setRegion(regionId);
                regionDatas.put(regionId, region);
            }
            return region;
        }

        public int getRegionMineralLv() {
            MapRegion region = getRegionData();
            if (region != null) {
                return region.getMineralLv();
            }
            return 0;
        }

        public int getregionAreaId() {
            MapRegion region = getRegionData();
            if (region != null) {
                return region.getAreaId();
            }
            return 0;
        }

        public int getRegionOilLv() {
            MapRegion region = getRegionData();
            if (region != null) {
                return region.getOilLv();
            }
            return 0;
        }

        public int getRegionDepLv() {
            MapRegion region = getRegionData();
            if (region != null) {
                return region.getDepLv();
            }
            return 0;
        }

        public int getRegionClimatZone() {
            MapRegion region = getRegionData();
            if (region != null) {
                return region.getClimateZone();
            }
            return 0;
        }

        public int getRegionStrategicRegion() {
            MapRegion region = getRegionData();
            if (region != null) {
                return region.getStrategicRegion();
            }
            return 0;
        }

        public void setRegionAreaId(int v) {
            MapRegion region = getRegionData();
            if (region != null) {
                region.setAreaId(v);
            }
        }

        public void setRegionDepLv(int v) {
            MapRegion region = getRegionData();
            if (region != null) {
                region.setDepLv(v);
            }
        }

        public void setRegionMineralLv(int v) {
            MapRegion region = getRegionData();
            if (region != null) {
                region.setMineralLv(v);
            }
        }

        public void setRegionOilLv(int v) {
            MapRegion region = getRegionData();
            if (region != null) {
                region.setOilLv(v);
            }
        }

        public int getRegionFoodLv() {
            MapRegion region = getRegionData();
            if (region != null) {
                region.getFoodLv();
            }
            return 0;
        }

        public void setRegionFoodLv(int v) {
            MapRegion region = getRegionData();
            if (region != null) {
                region.setFoodLv(v);
            }
        }


        public int getBlockType() {
            return blockType;
        }

        //获得实际地形
        public int getActualTerrain() {
            if (foreTile == 10 || foreTile == 9 || foreTile == 12) {
                return foreTile;
            }
            return backTile;
        }

        public void setBlockType(int blockType) {
            this.blockType = blockType;
        }

        public int getBackTile() {
            return backTile;
        }

        public void setBackTile(int backTile) {
            this.backTile = backTile;
        }

        public int getBackIdx() {
            return backIdx;
        }

        public void setBackIdx(int backIdx) {
            this.backIdx = backIdx;
        }

        public int getBackRefX() {
            if (backRefX == 0) {
                return 0;
            } else {
                return backRefX - 101;
            }
        }

        public int getBackRefXValue() {
            return backRefX;
        }

        public void setBackRefX(int backRefX) {
            this.backRefX = ComUtil.limitValue(backRefX, 0, 201);
        }

        public int getBackRefY() {
            if (backRefY == 0) {
                return 0;
            } else {
                return backRefY - 101;
            }
        }

        public int getBackRefYValue() {
            return backRefY;
        }

        //0 维持不变 否则偏移为 v-101
        public void setBackRefY(int backRefY) {
            this.backRefY = ComUtil.limitValue(backRefY, 0, 201);
        }

        public int getForeTile() {
            return foreTile;
        }

        public void setForeTile(int foreTile) {
            this.foreTile = foreTile;
        }

        public int getForeIdx() {
            return foreIdx;
        }

        public void setForeIdx(int foreIdx) {
            this.foreIdx = foreIdx;
        }

        public int getForeRefX() {
            //  return foreRefX;
            if (foreRefX == 0) {
                return 0;
            } else {
                return foreRefX - 101;
            }
        }

        public int getForeRefXValue() {
            return foreRefX;
        }

        public void setForeRefX(int foreRefX) {
            this.foreRefX = ComUtil.limitValue(foreRefX, 0, 201);
        }


        public int getForeRefYValue() {
            return foreRefY;
        }

        public int getForeRefY() {
            // return foreRefY;
            if (foreRefY == 0) {
                return 0;
            } else {
                return foreRefY - 101;
            }
        }

        public void setForeRefY(int foreRefY) {
            this.foreRefY = ComUtil.limitValue(foreRefY, 0, 201);
        }

        public int getPresetRailway() {
            return presetRailway;
        }

        public void setPresetRailway(int presetRailway) {
            this.presetRailway = presetRailway;
        }

        public int getIfClimateZone() {
            return ifClimateZone;
        }

        public void setIfClimateZone(int ifClimateZone) {
            this.ifClimateZone = ifClimateZone;
        }

        public int getRegionId() {
            return regionId;
        }

        public void setRegionId(int regionId) {
            this.regionId = regionId;
        }

        public int getOtherTile() {
            return otherTile;
        }

        public void setOtherTile(int otherTile) {
            this.otherTile = otherTile;
        }

        public TextureRegionDAO getTile1() {
            return tile1;
        }


        public void setTile1(TextureRegionDAO tile1) {
            this.tile1 = tile1;
        }

        public TextureRegionDAO getTile2() {
            return tile2;
        }

        public void setTile2(TextureRegionDAO tile2) {
            this.tile2 = tile2;
        }


        public int getHexagonIndex() {
            return hexagonIndex;
        }

        public void setHexagonIndex(int hexagonIndex) {
            this.hexagonIndex = hexagonIndex;
        }


        public int getLegionIndex() {
            if (this.buildData != null) {
                return this.buildData.getLegionIndex();
            }
            if (this.armyData != null) {
                return this.armyData.getLegionIndex();
            }
            return legionIndex;
        }

        public int getRegionLegionIndex() {
            return btl.getLegionIndexByRegion(regionId);
        }

        public void setLegionIndexValue(int legionIndex) {
            if (btl != null && legionIndex > btl.legionDatas.size) {
                Gdx.app.log("setLegionIndex error", legionIndex + ":" + btl.legionDatas.size);
            }
            this.legionIndex = legionIndex;
        }

        public void setAllLegionIndex(int legionIndex) {
            if (btl != null && legionIndex > btl.legionDatas.size) {
                Gdx.app.log("setLegionIndex error", legionIndex + ":" + btl.legionDatas.size);
            }
            this.legionIndex = legionIndex;
            if(buildData!=null){
                buildData.setLegionIndex(legionIndex);
            }
            if(armyData!=null){
                armyData.setLegionIndex(legionIndex);
            }
            if(fortData!=null){
                fortData.setLegionIndex(legionIndex);
            }
        }


        public int getIfFog() {
            if (btl != null && (btl.masterData.getIfFog() == 0 || btl.isEditMode(false))) {
                return 1;
            }
            return ifFog;
        }

        public void setIfFog(int ifFog) {
            this.ifFog = ifFog;
        }

        //------------------辅助功能-----------------------------------


        public void initHexagonData() {
            this.hexagonIndex = 0;
            this.regionId = -1;
            this.blockType = 0;
            this.legionIndex = 0;
            this.ifFog = 0;
        }


        //public TextureRegionDAO countryOuterBorderTile;


        // public boolean countryBorder1;
        //   public boolean countryBorder2;
        //   public boolean countryBorder3;
        // public boolean countryBorder4;
        // public boolean countryBorder5;
        //  public boolean countryBorder6;


        /*public boolean getIfDraw(){
            return ifDraw;
        }*/


        //可以强制移动的地形  0平地,1海洋,2海岸,3桥梁,4丘陵,5山地,6森林,7人为装饰,8自然装饰,9海洋装饰,10道路,11河流
        public boolean ifCanForceMove(int hexagon, int mov) {
            int costM = getMoveCost();
            if ((getForeTile() == 10 || getBackTile() == 3)) {
                if (mov >= costM - 1) {
                    return true;
                }
            } else if (getBackTile() == 2) {
                if (game.resGameConfig.ifPassWaterBeach) {
                    if (ifBorder(hexagon)) {
                        return true;
                    } else if (mov >= costM + 1) {
                        return true;
                    }
                } else {
                    if (ifBorder(hexagon)) {
                        return true;
                    }
                }
            }/*else if(!ifBorderIdCanPass(hexagon)){
                if(ifBorder(hexagon)){
                    return true;
                }
            }*/ else if (mov >= costM) {
                return true;
            } else if (game.resGameConfig.ifPassTerrain) {
                if (ifBorder(hexagon)) {
                    return true;
                }
            }
            return false;
        }


        public boolean ifBorder(int hexagon) {

            boolean top = false;
            boolean foot = false;
            boolean left = false;
            boolean right = false;
            //判断处于哪个边
            int y = (int) this.hexagonIndex / btl.masterData.getWidth();
            int x = this.hexagonIndex - y * btl.masterData.getWidth();
            int t1, t2, t3, t4, t5, t6;
            boolean ifParity = (x & 1) == 1;
            if (ifParity) {
                t1 = this.hexagonIndex - 1;
                t2 = hexagonIndex - btl.masterData.getWidth();
                t3 = hexagonIndex + 1;
                t4 = hexagonIndex + btl.masterData.getWidth() - 1;
                t5 = hexagonIndex + btl.masterData.getWidth();
                t6 = hexagonIndex + btl.masterData.getWidth() + 1;
            } else {
                t1 = hexagonIndex - btl.masterData.getWidth() - 1;
                t2 = hexagonIndex - btl.masterData.getWidth();
                t3 = hexagonIndex - btl.masterData.getWidth() + 1;
                t4 = hexagonIndex - 1;
                t5 = hexagonIndex + btl.masterData.getWidth();
                t6 = hexagonIndex + 1;
            }
            if (x == 0) {
                left = true;
            }
            if (x == btl.masterData.getWidth() - 1) {
                right = true;
            }
            if (y == 0) {
                top = true;
            }
            if (y == btl.masterData.getWidth() - 1) {
                foot = true;
            }
            if ((btl != null && btl.ifLoop) || isLoop()) {
                if (ifParity) {
                    if (t1 == hexagon) {
                        return true;
                    }
                    if (!top) {
                        if (t2 == hexagon) {
                            return true;
                        }
                    }
                    if (t3 == hexagon) {
                        return true;
                    }
                    if (!foot) {
                        if (t4 == hexagon || t5 == hexagon || t6 == hexagon) {
                            return true;
                        }
                    }
                } else {
                    if (!top) {
                        if (t1 == hexagon || t2 == hexagon || t3 == hexagon) {
                            return true;
                        }
                    }
                    if (t4 == hexagon) {
                        return true;
                    }
                    if (!foot) {
                        if (t5 == hexagon) {
                            return true;
                        }
                    }
                    if (t6 == hexagon) {
                        return true;
                    }
                }
            } else {
                if (ifParity) {
                    if (!left) {
                        if (t1 == hexagon) {
                            return true;
                        }
                    }
                    if (!top) {
                        if (t2 == hexagon) {
                            return true;
                        }
                    }
                    if (!right) {
                        if (t3 == hexagon) {
                            return true;
                        }
                    }
                    if (!foot && !left) {
                        if (t4 == hexagon) {
                            return true;
                        }
                    }
                    if (!foot) {
                        if (t5 == hexagon) {
                            return true;
                        }
                    }
                    if (!foot && !right) {
                        if (t6 == hexagon) {
                            return true;
                        }
                    }
                } else {
                    if (!top && !left) {
                        if (t1 == hexagon) {
                            return true;
                        }
                    }
                    if (!top) {
                        if (t2 == hexagon) {
                            return true;
                        }
                    }
                    if (!top && !right) {
                        if (t3 == hexagon) {
                            return true;
                        }
                    }
                    if (!left) {
                        if (t4 == hexagon) {
                            return true;
                        }
                    }
                    if (!foot) {
                        if (t5 == hexagon) {
                            return true;
                        }
                    }
                    if (!right) {
                        if (t6 == hexagon) {
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        //获得阵营 -1无势力 0中立偏敌对 1自己 2盟友  3敌人 4附属国 5宗主国 6中立偏友好 7友好非盟友 8非战斗但关系度低
        public int getCamp() {
            if (regionId == -1) {
                return -1;
            } else if (btl.ifNeutralCampByRegion(regionId)) {
                return -1;
            }
            Fb2Smap.LegionData l = btl.getLegionDataByHexagon(regionId);
            Fb2Smap.LegionData pl = btl.getPlayerLegionData();
            Fb2Smap.ForeignData f = btl.getForeignData(l.getLegionIndex(), pl.getLegionIndex());
            int fd = btl.getForeignDegree(l.getLegionIndex(), pl.getLegionIndex());
            if (fd == -1) {
                if (f != null) {
                    if (f.getForeignType() == -1) {
                        return 3;
                    } else {
                        if (f.getFavorValue() < 30) {
                            return 8;
                        } else {
                            return 0;
                        }
                    }
                } else {
                    return 3;
                }
            } else if (l.getLegionIndex() == pl.getLegionIndex()) {
                return 1;
            } else if (l.getLegionIndex() == pl.getSuzerainLi()) {
                return 5;
            } else if (pl.getLegionIndex() == l.getSuzerainLi()) {
                return 4;
            } else if (fd == 1) {
                if (f != null) {
                    if (f.isSameCamp()) {
                        return 2;
                    } else {
                        return 7;
                    }
                } else {
                    return 2;
                }
            } else {
                if (f != null) {
                    if (f.isSameCamp()) {
                        return 2;
                    } else if (f.getFavorValue() > 70) {
                        return 7;
                    } else if (f.getFavorValue() > 50) {
                        return 6;
                    } else if (f.getFavorValue() > 30) {
                        return 0;
                    } else {
                        if (f.getForeignType() == -1) {
                            return 3;
                        } else {
                            return 8;
                        }
                    }
                } else {
                    if (pl.getInternIndex() != l.getInternIndex() && l.getLegionIndex() != 0) {
                        return 3;
                    } else {
                        return -1;
                    }
                }
            }
        }

        public int getIfCoast() {
            return ifCoast;
        }

        public void setIfCoast(int c) {
            this.ifCoast = c;
        }

        public void initVirAttribute() {
            // this.ifDraw=false;
            updHexagonBorderAttribute();
            if (btl != null) {
                if (this.getHexagonIndex() == this.getRegionId()) {
                    this.buildData = btl.getBuildDataByRegion(this.getRegionId());
                    this.buildData.updActor();
                } else {
                    this.buildData = null;
                }
                this.armyData = btl.getArmyDataByHexagon(hexagonIndex);
                this.facilityData = btl.getFacilityDataByHexagon(hexagonIndex);
                this.fortData = btl.getFortDataByHexagon(hexagonIndex);
            } else {
                this.buildData = null;
                this.armyData = null;
                this.facilityData = null;
                this.fortData = null;
            }
        }

        public void updHexagonBorderAttribute() {
            Color defaultColor = null;
            /**/
            speed = getMoveCost();
            boolean isSea = isSea();
            boolean ifBorderRegion = ifBorderRegion();
            boolean countryBorder1 = btl.ifCountryBorderX(getHexagonIndex(), 1);//1↗ 2→ 3↘ 4↙ 5← 6↖
            boolean countryBorder2 = btl.ifCountryBorderX(getHexagonIndex(), 2);
            boolean countryBorder3 = btl.ifCountryBorderX(getHexagonIndex(), 3);
            boolean countryBorder4 = btl.ifCountryBorderX(getHexagonIndex(), 4);
            boolean countryBorder5 = btl.ifCountryBorderX(getHexagonIndex(), 5);
            boolean countryBorder6 = btl.ifCountryBorderX(getHexagonIndex(), 6);
            /**/

            countryBorder = GameUtil.getFBorderId(countryBorder1, countryBorder2, countryBorder3, countryBorder4, countryBorder5, countryBorder6);
            isCountryBorder = ifCountryBorder();

            //  regionLineBorderTile=game.getImgLists().getTextureByName(DefDAO.getLineBorder(ifRegionBorder(getHexagonIndex(),1,false),ifRegionBorder(getHexagonIndex(),2,false),ifRegionBorder(getHexagonIndex(),3,false)));
            regionLineBorderTile = game.getImgLists().getTextureByName(
                    DefDAO.getLineBorder(
                            (countryBorder5 || countryBorder6 || btl.ifCountryBorderX(getBorderIdByDirect(getHexagonIndex(), 1), 2)) ? false : ifRegionBorder(getHexagonIndex(), 1, true),
                            (countryBorder1 || countryBorder6 || btl.ifCountryBorderX(getBorderIdByDirect(getHexagonIndex(), 2), 3) || btl.ifCountryBorderX(getBorderIdByDirect(getHexagonIndex(), 2), 4)) ? false : ifRegionBorder(getHexagonIndex(), 2, true),
                            (countryBorder1 || countryBorder2 || btl.ifCountryBorderX(getBorderIdByDirect(getHexagonIndex(), 3), 4)) ? false : ifRegionBorder(getHexagonIndex(), 3, true)));
            //国家边界与海岸边界
            countryLineBorderTile = game.getImgLists().getTextureByName(DefDAO.getLineBorder(btl.ifCountryBorder(getHexagonIndex(), 1, true), btl.ifCountryBorder(getHexagonIndex(), 2, true), btl.ifCountryBorder(getHexagonIndex(), 3, true)));

            ifMoreCountryBorder = ifMoreCountryBorder();
            int RailWayId=getRailWayId();
            if (otherTile == 1) {
                railwayTile = game.getImgLists().getTextureByName("10_" + RailWayId);
                if(RailWayId==0){
                    ifRailWayMark=false;
                }else {
                    ifRailWayMark=true;
                }
            } else {
                railwayTile = null;
                ifRailWayMark=false;
            }


            if (countryBorder == 0) {
                countryInnerBorderTile = null;
                countryOuterBorderTile = null;
                countryInnerBorderColor = null;
                countryOuterBorderColor = null;
            } else {
                countryInnerBorderTile = game.getImgLists().getTextureByName("countryInteralBorder_" + countryBorder);
                int fbBorderId=
                        GameUtil.getFBorderId(countryBorder1&&(!ifBorderIdRegionIsSea(hexagonIndex,2)&&!ifBorderIdRegionIsSea(hexagonIndex,3)),
                                countryBorder2&&(!ifBorderIdRegionIsSea(hexagonIndex,3)&&!ifBorderIdRegionIsSea(hexagonIndex,6)),
                                countryBorder3&&(!ifBorderIdRegionIsSea(hexagonIndex,5)&&!ifBorderIdRegionIsSea(hexagonIndex,6)),
                                countryBorder4&&(!ifBorderIdRegionIsSea(hexagonIndex,4)&&!ifBorderIdRegionIsSea(hexagonIndex,5)),
                                countryBorder5&&(!ifBorderIdRegionIsSea(hexagonIndex,1)&&!ifBorderIdRegionIsSea(hexagonIndex,4)),
                                countryBorder6&&(!ifBorderIdRegionIsSea(hexagonIndex,1)&&!ifBorderIdRegionIsSea(hexagonIndex,2)));
                if(fbBorderId>0){
                    countryOuterBorderTile = game.getImgLists().getTextureByName("countryExternalBorder_" +fbBorderId);
                }else {
                    countryOuterBorderTile = null;
                }
                boolean ifHaveInner = getRegionId() != -1 && btl.getLegionIndexByRegion(getRegionId()) != 0 && countryInnerBorderTile != null && (!getRegionHexagonData().ifSeaLand || !isSea);
                if (ifHaveInner) {
                    countryInnerBorderColor = btl.getBorderColorForRegion(getHexagonIndex(), defaultColor);
                } else {
                    countryInnerBorderColor = defaultColor;
                }
                boolean ifHaveOuter = countryOuterBorderTile != null;
                if (ifHaveOuter) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                    countryOuterBorderColor = getOuterColor(
                            countryBorder1&&(!ifBorderIdRegionIsSea(hexagonIndex,2)&&!ifBorderIdRegionIsSea(hexagonIndex,3)),
                            countryBorder2&&(!ifBorderIdRegionIsSea(hexagonIndex,3)&&!ifBorderIdRegionIsSea(hexagonIndex,6)),
                            countryBorder3&&(!ifBorderIdRegionIsSea(hexagonIndex,5)&&!ifBorderIdRegionIsSea(hexagonIndex,6)),
                            countryBorder4&&(!ifBorderIdRegionIsSea(hexagonIndex,4)&&!ifBorderIdRegionIsSea(hexagonIndex,5)),
                            countryBorder5&&(!ifBorderIdRegionIsSea(hexagonIndex,1)&&!ifBorderIdRegionIsSea(hexagonIndex,4)),
                            countryBorder6&&(!ifBorderIdRegionIsSea(hexagonIndex,1)&&!ifBorderIdRegionIsSea(hexagonIndex,2)),




                            defaultColor);
                    //countryOuterBorderColor.a=1f;
                } else {
                    countryOuterBorderColor = defaultColor;
                }/**/
            }
            //1↖ 2上 3↗ 4↙ 5↓ 6↘
            int borderId1 = getBorderIdByDirect(getHexagonIndex(), 1);
            int borderId2 = getBorderIdByDirect(getHexagonIndex(), 2);
            int borderId3 = getBorderIdByDirect(getHexagonIndex(), 3);
            int borderId4 = getBorderIdByDirect(getHexagonIndex(), 4);
            int borderId5 = getBorderIdByDirect(getHexagonIndex(), 5);
            int borderId6 = getBorderIdByDirect(getHexagonIndex(), 6);
            //1↖ 2上 3↗ 4↙ 5↓ 6↘
            MapHexagon hb1 = btl.getHexagonData(borderId1);
            MapHexagon hb2 = btl.getHexagonData(borderId2);
            MapHexagon hb3 = btl.getHexagonData(borderId3);
            MapHexagon hb4 = btl.getHexagonData(borderId4);
            MapHexagon hb5 = btl.getHexagonData(borderId5);
            MapHexagon hb6 = btl.getHexagonData(borderId6);
            int li0=getRegionLegionIndex();
            int li1 = -1, li2 = -1, li3 = -1, li4 = -1, li5 = -1, li6 = -1;
            if (hb1 != null && hb1.getRegionHexagonData().getBlockType() != 1) {
                li1 = hb1.getRegionLegionIndex();
            }
            if (hb2 != null && hb2.getRegionHexagonData().getBlockType() != 1) {
                li2 = hb2.getRegionLegionIndex();
            }
            if (hb3 != null && hb3.getRegionHexagonData().getBlockType() != 1) {
                li3 = hb3.getRegionLegionIndex();
            }
            if (hb4 != null && hb4.getRegionHexagonData().getBlockType() != 1) {
                li4 = hb4.getRegionLegionIndex();
            }
            if (hb5 != null && hb5.getRegionHexagonData().getBlockType() != 1) {
                li5 = hb5.getRegionLegionIndex();
            }
            if (hb6 != null && hb6.getRegionHexagonData().getBlockType() != 1) {
                li6 = hb6.getRegionLegionIndex();
            }

            /*if(getHexagonIndex()==8494){
                int s=0;
            }*/

            //1↗ 2→ 3↘ 4↙ 5← 6↖
            if (countryBorder1 && hb2 != null && hb3 != null&&(li2!=0&&li3!=0) &&((li2 == li3) || (!hb2.ifMoreCountryBorder && !hb3.ifMoreCountryBorder))) {
                if (getRegionId() != -1 && (!hb2.ifSeaLand || (!isSea && hb2.isSea()) || (isSea && !hb2.isSea())) && (!hb3.ifSeaLand || (!isSea && hb3.isSea()) || (isSea && !hb3.isSea()))) {
                    countryBorderColor1 = btl.getBorderColorForRegion(borderId2, defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                    countryBorder1 = true;
                } else {
                    countryBorderColor1 = null;
                    countryBorder1 = false;
                }
            } else {
                countryBorderColor1 = null;
                countryBorder1 = false;
            }
            if (countryBorder2 && hb3 != null && hb6 != null&&(li3!=0&&li6!=0)&& ((li3 == li6) || (!hb3.ifMoreCountryBorder && !hb6.ifMoreCountryBorder))) {
                if (getRegionId() != -1 && (!hb3.ifSeaLand || (!isSea && hb3.isSea()) || (isSea && !hb3.isSea())) && (!hb6.ifSeaLand || (!isSea && hb6.isSea()) || (isSea && !hb6.isSea()))) {
                    countryBorderColor2 = btl.getBorderColorForRegion(borderId3, defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                    countryBorder2 = true;
                } else {
                    countryBorderColor2 = null;
                    countryBorder2 = false;
                }
            } else {
                countryBorderColor2 = null;
                countryBorder2 = false;
            }

            if (countryBorder3 && hb5 != null && hb6 != null&&(li5!=0&&li6!=0) &&((li5 == li6) || (!hb5.ifMoreCountryBorder && !hb6.ifMoreCountryBorder))) {
                if (getRegionId() != -1 && (!hb5.ifSeaLand || (!isSea && hb5.isSea()) || (isSea && !hb5.isSea())) && (!hb6.ifSeaLand || (!isSea && hb6.isSea()) || (isSea && !hb6.isSea()))) {
                    countryBorderColor3 = btl.getBorderColorForRegion(borderId6, defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                    countryBorder3 = true;
                } else {
                    countryBorderColor3 = null;
                    countryBorder3 = false;
                }
            } else {
                countryBorderColor3 = null;
                countryBorder3 = false;
            }
            if (countryBorder4 && hb4 != null && hb5 != null&&(li4!=0&&li5!=0) && ((li4 == li5) || (!hb4.ifMoreCountryBorder && !hb5.ifMoreCountryBorder))) {
                if (getRegionId() != -1 && (!hb4.ifSeaLand || (!isSea && hb4.isSea()) || (isSea && !hb4.isSea())) && (!hb5.ifSeaLand || (!isSea && hb5.isSea()) || (isSea && !hb5.isSea()))) {
                    countryBorderColor4 = btl.getBorderColorForRegion(borderId5, defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                    countryBorder4 = true;
                } else {
                    countryBorderColor4 = null;
                    countryBorder4 = false;
                }
            } else {
                countryBorderColor4 = null;
                countryBorder4 = false;
            }

            if (countryBorder5 && hb1 != null && hb4 != null&&(li1!=0&&li4!=0) && ((li1 == li4) || (!hb1.ifMoreCountryBorder && !hb4.ifMoreCountryBorder))) {
                if (getRegionId() != -1 && (!hb1.ifSeaLand || (!isSea && hb1.isSea()) || (isSea && !hb1.isSea())) && (!hb4.ifSeaLand || (!isSea && hb4.isSea()) || (isSea && !hb4.isSea()))) {
                    countryBorderColor5 = btl.getBorderColorForRegion(borderId4, defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                    countryBorder5 = true;
                } else {
                    countryBorderColor5 = null;
                    countryBorder5 = false;
                }
            } else {
                countryBorderColor5 = null;
                countryBorder5 = false;
            }
            if (countryBorder6 && hb1 != null && hb2 != null&&(li1!=0&&li2!=0) && ((li1 == li2) || (!hb1.ifMoreCountryBorder && !hb2.ifMoreCountryBorder))) {
                if (getRegionId() != -1 && (!hb1.ifSeaLand || (!isSea && hb1.isSea()) || (isSea && !hb1.isSea())) && (!hb2.ifSeaLand || (!isSea && hb2.isSea()) || (isSea && !hb2.isSea()))) {
                    countryBorderColor6 = btl.getBorderColorForRegion(borderId1, defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                    countryBorder6 = true;
                } else {
                    countryBorderColor6 = null;
                    countryBorder6 = false;
                }
            } else {
                countryBorderColor6 = null;
                countryBorder6 = false;
            }


        }

        private boolean ifBorderIdRegionIsSea(int hexagonIndex, int i) {
           MapHexagon hexagon=getBorderHexagon(hexagonIndex,i);
            if(hexagon!=null){
                MapHexagon m=hexagon.getRegionHexagonData();
                if(m!=null&&m.isSea()){
                    return true;
                }
            }
            return false;
        }

        private boolean ifBorderRegion() {
            for (int d = 1; d <= 6; d++) {
                int borderId = getBorderIdByDirect(getHexagonIndex(), d);
                if (borderId == getRegionId()) {
                    return true;
                }
            }
            return false;
        }

        public int getRailWayId() {
            // 1 ↖ 2 ↙  3 ↓  4 ↘ 5 ↗ 6↑
            boolean b1 = ifRailway(getIdInDirect(hexagonIndex, 1));
            boolean b2 = ifRailway(getIdInDirect(hexagonIndex, 2));
            boolean b3 = ifRailway(getIdInDirect(hexagonIndex, 3));
            boolean b4 = ifRailway(getIdInDirect(hexagonIndex, 4));
            boolean b5 = ifRailway(getIdInDirect(hexagonIndex, 5));
            boolean b6 = ifRailway(getIdInDirect(hexagonIndex, 6));

            if ((b4 || b2) && b1) {
                b1 = false;
            }
            if ((b3 || b5) && b6) {
                b6 = false;
            }
            int railwayId = GameUtil.getFBorderId(b1, b4, b5, b6, b3, b2);
            //    Gdx.app.log("railwayTile",hexagonIndex+":"+railwayId+":"+b1+" "+b2+" "+b3+" "+b4+" "+b5+" "+b6+" ");
            return railwayId;
        }

        public int getPresetRailWayId() {
            // 1 ↖ 2 ↙  3 ↓  4 ↘ 5 ↗ 6↑
            boolean b0 = ifPresetRailway(hexagonIndex);
            boolean b1 = ifPresetRailway(getIdInDirect(hexagonIndex, 1));
            boolean b2 = ifPresetRailway(getIdInDirect(hexagonIndex, 2));
            boolean b3 = ifPresetRailway(getIdInDirect(hexagonIndex, 3));
            boolean b4 = ifPresetRailway(getIdInDirect(hexagonIndex, 4));
            boolean b5 = ifPresetRailway(getIdInDirect(hexagonIndex, 5));
            boolean b6 = ifPresetRailway(getIdInDirect(hexagonIndex, 6));


            /*if ((b6 || b2) && b3) {
                b3 = false;
            }*/
            if ((b4 || b2) && b1) {
                b1 = false;
            }

            if ((b3 || b5) && b6) {
                b6 = false;
            }

            /*if ((b6 || b2) && b3) {
                b3 = false;
            }
            if ((b1 || b5) && b4) {
                b4 = false;
            }*/

           /* if(b0&&b2&&b5){//|
                if(!(b1&&b6)&&!(b3&&b4)){
                    int borderDirect=getMostBorderRailWayId(hexagonIndex,1);
                    if(borderDirect!=0){
                        if(b1&&b4){
                            if(borderDirect!=1){b1=false;}
                            if(borderDirect!=4){b4=false;}
                        }
                        if(b3&&b6){
                            if(borderDirect!=3){b3=false;}
                            if(borderDirect!=6){b6=false;}
                        }
                    }else {
                        if(b1){  b1=false; }
                        if(b3){  b3=false; }
                        if(b4){  b4=false; }
                        if(b6){  b6=false; }
                    }
                }
            }
            if(b0&&b1&&b6){// \
                if(!(b2&&b5)&&!(b3&&b4)){
                    int borderDirect=getMostBorderRailWayId(hexagonIndex,1);
                    if(borderDirect!=0){
                        if(b2&&b3){
                            if(borderDirect!=2){b2=false;}
                            if(borderDirect!=3){b3=false;}
                        }
                        if(b4&&b5){
                            if(borderDirect!=4){b4=false;}
                            if(borderDirect!=5){b5=false;}
                        }
                    }
                }
            }
            if(b0&&b3&&b4){// /
                if(!(b1&&b6)&&!(b2&&b5)){
                    int borderDirect=getMostBorderRailWayId(hexagonIndex,1);
                    if(borderDirect!=0){
                        if(b1&&b2){
                            if(borderDirect!=1){b1=false;}
                            if(borderDirect!=2){b2=false;}
                        }
                       if(b5&&b6){
                           if(borderDirect!=5){b5=false;}
                           if(borderDirect!=6){b6=false;}
                       }

                    }
                }
            }*/


            int railwayId = GameUtil.getFBorderId(b1, b4, b5, b6, b3, b2);
            //   Gdx.app.log("railwayTile",hexagonIndex+":"+railwayId+":"+b1+" "+b2+" "+b3+" "+b4+" "+b5+" "+b6+" ");
            return railwayId;
        }

        private boolean ifCountryBorder() {//是否
            /*if(hexagonIndex==16650){
                int s=0;
            }*/
            int rli = getRegionLegionIndex();
            for (int i = 0; i <= 6; i++) {
                int id = getBorderIdByDirect(hexagonIndex, i);
                int li = btl.getLegionIndexByRegion(id);
                if (!ifSea(id) && li != rli) {
                    return true;
                }
                if (!ifSea(hexagonIndex) && ifSea(id) && li != rli) {
                    return true;
                }

            }
            return false;
        }

        private boolean ifMoreCountryBorder() {//是否有两个以上的国家
            int i1 = -1;
            int i2 = -1;
            boolean isSea = isSea();
            MapHexagon regionHexagon = getRegionHexagonData();
            if (regionHexagon != null) {
                for (int i = 0; i <= 6; i++) {
                    int id = getBorderIdByDirect(hexagonIndex, i);
                    Fb2Smap.BuildData b = btl.getBuildDataByRegion(Fb2Map.this.getRegionId(id));
                    //HexagonData h=hexagonDatas.get(id);
                    if (b != null) {
                        int v = b.getLegionIndex();
                        if(b.potionIsSea()){
                            v=0;
                        }
                        if (b.getLegionIndex() != regionHexagon.getLegionIndex() && isSea && b.getLegionIndex() != 0) {
                            return true;
                        }
                        if (i1 == -1 || i1 == v) {
                            i1 = v;
                        } else if (i2 == -1 || i2 == v) {
                            i2 = v;
                        } else if (i1 != v && i2 != v) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public String getAllAttributes() {
            return " hexagonIndex:" + hexagonIndex
                    + " source_x:" + source_x
                    + " source_y:" + source_y
                    + " blockType:" + blockType
                    + " backTile:" + backTile
                    + " backIdx:" + backIdx
                    + " backRefX:" + backRefX
                    + " backRefY:" + backRefY
                    + " foreTile:" + foreTile
                    + " foreIdx:" + foreIdx
                    + " foreRefX:" + foreRefX
                    + " foreRefY:" + foreRefY
                    + " prisetRailway:" + presetRailway
                    + " zone:" + ifClimateZone
                    + " regionId:" + regionId
                    + " ifRailway:" + otherTile
                    + " ifCoast:" + ifCoast
                    + " ifFog:" + ifFog
                    + " isCountryBorder:" + isCountryBorder
                    + " ifMoreCountryBorder:" + ifMoreCountryBorder
                    + " countryBorder:" + countryBorder
                    + " countryBorder1:" + (countryBorderColor1 != null)
                    + " countryBorder2:" + (countryBorderColor2 != null)
                    + " countryBorder3:" + (countryBorderColor3 != null)
                    + " countryBorder4:" + (countryBorderColor4 != null)
                    + " countryBorder5:" + (countryBorderColor5 != null)
                    + " countryBorder6:" + (countryBorderColor6 != null)
                    + " ifSeaLand:" + (ifSeaLand(getHexagonIndex()))
                    + " countryInnerBorderTile:" + (countryInnerBorderTile != null)
                    + " countryInnerBorderColor:" + (countryInnerBorderColor != null)
                    + " countryOuterBorderTile:" + (countryOuterBorderTile != null)
                    + " countryOuterBorderColor:" + (countryOuterBorderColor != null)


                    ;
                  /*  +" countryBorder1:"+(countryBorder1)
                    +" countryBorder2:"+(countryBorder2)
                    +" countryBorder3:"+(countryBorder3)
                    +" countryBorder4:"+(countryBorder4)
                    +" countryBorder5:"+(countryBorder5)
                    +" countryBorder6:"+(countryBorder6);*/
        }


        public boolean getIfDrawWarLine() {
            Fb2Smap.BuildData b = btl.getBuildDataByRegion(regionId);
            MapHexagon h = btl.getHexagonData(regionId);
            if (h != null && b != null && !b.ifSea() && (b.isPlayerAlly() || h.getIfFog() == 1) && b.getCityHpMax() > 0 && b.getCityHpMax() > b.getCityHpNow()) {
                return true;
            }
            return false;
        }

        //// 1步兵 2炮兵 3坦克 4船只 5飞机 6要塞 7超武 8潜艇
        public boolean canRecruit(int armyType, int cardId) {
            Fb2Smap.BuildData b = getBuildData();
            if (b.getCityHpNow() == 0) {
                return false;
            }
            Fb2Smap.ArmyData a;
            switch (armyType) {
                case 1:
                case 2:
                case 3:
                    a = btl.armyHDatas.get(hexagonIndex);
                    if (a == null && !ifSea(hexagonIndex)) {
                        return true;
                    }
                    break;
                case 6:
                    a = btl.armyHDatas.get(hexagonIndex);
                    if (a == null && hexagonIndex != regionId && !ifSea(hexagonIndex)) {
                        return true;
                    }
                    break;
                case 4:
                case 8:
                    a = btl.armyHDatas.get(hexagonIndex);
                    if (a == null && ifSea(hexagonIndex)) {
                        return true;
                    }

                    break;
            }
            return false;
        }

        //周围是否有coast
        public boolean ifAroundGridIsCoastSea() {
            for (int i = 0; i < 6; i++) {
                int h = getBorderIdByDirect(hexagonIndex, i);
                if (ifGridIsPass(h) && btl.hexagonDatas.get(h).getBackTile() == 2) {
                    return true;
                }
            }

            return false;
        }

        public MapHexagon getRegionHexagonData() {
            if (btl.ifGridIsPass(regionId)) {
                return mapHexagons.get(regionId);
            }
            return this;
        }

        public Fb2Smap.BuildData getBuildData() {
            return btl.getBuildDataByRegion(regionId);
        }

        public Color getOuterColor(boolean countryBorder1, boolean countryBorder2, boolean countryBorder3, boolean countryBorder4, boolean countryBorder5, boolean countryBorder6, Color defaultColor) {
            if (hexagonIndex == 7354) {
                int s = 0;
            }
            //应该优先获取有陆地的
            MapHexagon bh  =getDirectForColor(this.getHexagonIndex(),countryBorder1, countryBorder2, countryBorder3, countryBorder4, countryBorder5, countryBorder6,false);//1↗ 2→ 3↘ 4↙ 5← 6↖
           // MapHexagon h = btl.getHexagonData(borderId);
            if(bh==null){
                return defaultColor;
            }
           //boolean b1 = h != null && !h.ifSeaLand;
           // boolean b2 = h != null && (isSea() && !h.isSea());
            //   boolean b3=h!=null&&(!isSea()&&h.isSea());

            if (bh != null && this.getRegionId() != -1 && (!bh.ifSeaLand || isSea() && !bh.isSea())) {
                defaultColor = btl.getBorderColorForRegion(bh.getHexagonIndex(), defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
            }
            return defaultColor;
        }

        //adjoinType 1:|  2:/ 3:\
        public boolean ifAdjoinRiver(int adjoinType) {
            if (getBackTile() == 11) {
                switch (adjoinType) {
                    case 1:
                        if (btl.ifIsRiver(getBorderIdByDirect(hexagonIndex, 2)) || btl.ifIsRiver(getBorderIdByDirect(hexagonIndex, 5))) {//1↖ 2上 3↗ 4↙ 5↓ 6↘
                            return true;
                        }
                        return false;
                    case 2:
                        if (btl.ifIsRiver(getBorderIdByDirect(hexagonIndex, 3)) || btl.ifIsRiver(getBorderIdByDirect(hexagonIndex, 4))) {//1↖ 2上 3↗ 4↙ 5↓ 6↘
                            return true;
                        }
                        return false;
                    case 3:
                        if (btl.ifIsRiver(getBorderIdByDirect(hexagonIndex, 1)) || btl.ifIsRiver(getBorderIdByDirect(hexagonIndex, 6))) {//1↖ 2上 3↗ 4↙ 5↓ 6↘
                            return true;
                        }
                        return false;
                }
            }
            return false;
        }


        //是否被河流阻隔
        public boolean ifRiverPass(int targetHexagon) {
            if (otherTile == 1) {
                return false;
            }
            //1↖ 2↑ 3↗ 4↙ 5↓ 6↘
            int direct = btl.getDirect(hexagonIndex, targetHexagon);
            if (direct == 0) {
                return false;
            }
            boolean b0 = ifRiver(hexagonIndex);
            boolean b1 = ifRiver(targetHexagon);
            boolean rs = false;
            switch (direct) {
                case 1:  //1↖
                    if (b0 && b1) {
                        if (ifBorderIdIsRiver(hexagonIndex, 4)) {
                            rs = true;
                        } else {
                            rs = false;
                        }
                    } else if (b0) {//green is river
                        if (ifBorderIdIsRiver(hexagonIndex, 4)) {
                            rs = true;
                        }
                    } else if (b1) {//red is river
                        if (ifBorderIdIsRiver(targetHexagon, 5)) {
                            rs = true;
                        }
                    }
                    break;
                case 2: //2↑
                    if (b0 && b1) {
                        if (ifBorderIdIsRiver(hexagonIndex, 4)) {
                            rs = true;
                        }

                        if (ifBorderIdIsRiver(hexagonIndex, 1) && ifBorderIdIsRiver(hexagonIndex, 2)) {
                            rs = true;
                        }
                    } else if (b0) {//green is river
                        if (!(ifBorderIdIsRiver(hexagonIndex, 3) && ifBorderIdIsRiver(hexagonIndex, 5))) {
                            rs = true;
                        }
                    } else if (b1) {//red is river

                    }
                    break;
                case 3://3↗
                    if (b0 && b1) {
                        if (ifBorderIdIsRiver(hexagonIndex, 3) && ifBorderIdIsRiver(hexagonIndex, 5)) {
                            rs = true;
                        }
                        if (ifBorderIdIsRiver(targetHexagon, 2) && ifBorderIdIsRiver(targetHexagon, 5)) {
                            rs = true;
                        }
                    } else if (b0) {//green is river
                        if (!(ifBorderIdIsRiver(hexagonIndex, 2) && ifBorderIdIsRiver(hexagonIndex, 4))) {
                            rs = true;
                        }
                    } else if (b1) {//red is river

                    }
                    break;
                case 4://4↙
                    if (b0 && b1) {
                        if (ifBorderIdIsRiver(targetHexagon, 3) && ifBorderIdIsRiver(targetHexagon, 5)) {
                            rs = true;
                        }
                        if (ifBorderIdIsRiver(hexagonIndex, 5)) {
                            rs = true;
                        }
                    } else if (b0) {//green is river

                    } else if (b1) {//red is river
                        if (ifBorderIdIsRiver(targetHexagon, 2) && ifBorderIdIsRiver(targetHexagon, 4) && ifBorderIdIsRiver(targetHexagon, 5)) {
                            rs = false;
                        } else {
                            rs = true;
                        }
                    }
                    break;
                case 5://5↓
                    if (b0 && b1) {
                        if (ifBorderIdIsRiver(targetHexagon, 2) && ifBorderIdIsRiver(targetHexagon, 4)) {
                            rs = true;
                        }
                        if (ifBorderIdIsRiver(targetHexagon, 1)) {
                            rs = true;
                        }
                    } else if (b0) {//green is river

                    } else if (b1) {//red is river
                        if (!(ifBorderIdIsRiver(targetHexagon, 3) && ifBorderIdIsRiver(targetHexagon, 5))) {
                            rs = true;
                        }
                    }
                    break;
                case 6://6↘
                    if (b0 && b1) {
                        if (ifBorderIdIsRiver(hexagonIndex, 5)) {
                            rs = true;
                        }
                    } else if (b0) {//green is river
                        if (ifBorderIdIsRiver(hexagonIndex, 5)) {
                            rs = true;
                        }
                    } else if (b1) {//red is river
                        if (ifBorderIdIsRiver(targetHexagon, 4)) {
                            rs = true;
                        }
                    }
                    break;
            }
            //  Gdx.app.log("test river pass "+hexagonIndex+":"+targetHexagon,direct+"  "+b0+":"+b1+" "+rs);
            return rs;
        }

        public int getX() {
            return GameMap.getHX(hexagonIndex, mapWidth);
        }

        public int getY() {
            return GameMap.getHY(hexagonIndex, mapWidth);
        }

        public boolean isSea() {
            if (getBlockType() == 1) {
                return true;
            }
            return false;
        }

        public Fb2Smap.LegionData getLegionData() {
            if (btl != null) {
                return btl.getLegionDataByLi(legionIndex);
            }
            return null;
        }

        public boolean isCoreRegion() {
            if (btl != null) {
                return btl.getLegionDataByHexagon(regionId).isCoreAreaRegion(regionId);
            }
            return false;
        }

        public boolean isMajorRegion() {
            if (btl != null) {
                return btl.getLegionDataByHexagon(regionId).isMajorAreaRegion(regionId);
            }
            return false;
        }

        //当前可在移动损耗不大于{build.getTransportLvNow()}的地块修建交通线
        public boolean canBuildRailway(int transportLvNow) {
            if (btl != null && btl.isEditMode(true)) {
                return true;
            }
            int cost = getMoveCost();
            if (cost > transportLvNow || transportLvNow == 0) {
                return false;
            }
            return true;
        }

        public int getMoveCost() {
            return Fb2Map.this.getMoveCost(hexagonIndex);
        }

        public Fb2Smap.BuildData getRegionBuildData() {
            if (btl != null) {
                return btl.getBuildDataByRegion(getRegionId());
            }
            return null;
        }

        public boolean ifDrawColor() {
            MapHexagon rH = getHexagonDataByRegion();
            if (rH != null) {
                if (!rH.isSea()) {
                    if (rH.ifCoast == 63) {
                        return true;
                    }
                } else {//判断相邻的有没有是陆地的
                    MapHexagon bH = getHexagonDataByBorder(1);
                    if (bH != null && !bH.isSea()) {
                        return true;
                    }
                    bH = getHexagonDataByBorder(2);
                    if (bH != null && !bH.isSea()) {
                        return true;
                    }
                    bH = getHexagonDataByBorder(3);
                    if (bH != null && !bH.isSea()) {
                        return true;
                    }
                    bH = getHexagonDataByBorder(4);
                    if (bH != null && !bH.isSea()) {
                        return true;
                    }
                    bH = getHexagonDataByBorder(5);
                    if (bH != null && !bH.isSea()) {
                        return true;
                    }
                    bH = getHexagonDataByBorder(6);
                    if (bH != null && !bH.isSea()) {
                        return true;
                    }
                }
            }


            return false;
        }

        public boolean isSeaRegion() {
            MapHexagon region=getRegionHexagonData();
            if(region!=null&&region.isSea()){
                return true;
            }
            return false;
        }

        public boolean ifAlly() {
            return btl.ifAllyPlayerByLi(getLegionIndex());
        }
    }


    //countryBorder  //1↗ 2→ 3↘ 4↙ 5← 6↖
    //result 1↖ 2上 3↗ 4↙ 5↓ 6↘
    //ifHaveSeaRegion false,需要检测对面的核心是否是岛屿
    public  Fb2Map.MapHexagon getDirectForColor(int hexagon, boolean countryBorder1, boolean countryBorder2, boolean countryBorder3, boolean countryBorder4, boolean countryBorder5, boolean countryBorder6, boolean ifHaveSeaRegion) {
        if(btl==null){
            return null;
        }
        if(ifHaveSeaRegion){
            if(countryBorder1){
               return getBorderHexagon(hexagon,2);
            }else if(countryBorder2){
                return getBorderHexagon(hexagon,3);
            }else if(countryBorder3){
                return getBorderHexagon(hexagon,6);
            }else if(countryBorder4){
                return getBorderHexagon(hexagon,5);
            }else if(countryBorder5){
                return getBorderHexagon(hexagon,4);
            }else if(countryBorder6){
                return getBorderHexagon(hexagon,1);
            }
        }else{
            if(countryBorder1){
               MapHexagon m= getBorderHexagon(hexagon,2);
               if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                   return m;
               }
                m= getBorderHexagon(hexagon,3);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
            }else  if(countryBorder2){
                MapHexagon m= getBorderHexagon(hexagon,3);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
                m= getBorderHexagon(hexagon,6);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
            }else  if(countryBorder3){
                MapHexagon m= getBorderHexagon(hexagon,5);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
                m= getBorderHexagon(hexagon,6);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
            }else if(countryBorder4){
                MapHexagon m= getBorderHexagon(hexagon,4);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
                m= getBorderHexagon(hexagon,5);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
            }else   if(countryBorder5){
                MapHexagon m= getBorderHexagon(hexagon,1);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
                m= getBorderHexagon(hexagon,4);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
            }else  if(countryBorder6){
                MapHexagon m= getBorderHexagon(hexagon,1);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
                m= getBorderHexagon(hexagon,2);
                if(m!=null&&(!m.isSea()||!m.isSeaRegion())){
                    return m;
                }
            }
        }
        return null;
    }


    //获得周边最大的点 type 0铁路 1预设
    private int getMostBorderRailWayId(int hexagon, int type) {
        int mostC = 0;
        int direct = 0;
        for (int d = 1; d <= 6; d++) {
            int borderId = getBorderIdByDirect(hexagon, d);
            int c = getMostBorderRailWayCount(borderId, type);
            if (c > mostC) {
                mostC = c;
                direct = d;
            }

        }
        return direct;
    }

    private int getMostBorderRailWayCount(int hexagon, int type) {
        int c = 0;
        for (int d = 1; d <= 6; d++) {
            int borderId = getBorderIdByDirect(hexagon, d);
            MapHexagon mapHexagon = getMapHexagon(borderId);
            if (mapHexagon != null) {
                if (type == 0) {
                    if (mapHexagon.getOtherTile() == 1) {
                        c++;
                    }
                } else {
                    if (mapHexagon.getPresetRailway() == 1) {
                        c++;
                    }
                }
            }
        }
        return c;
    }

    private MapHexagon getMapHexagon(int id) {
        if (ifGridIsPass(id)) {
            return mapHexagons.get(id);
        }
        return null;
    }


    private boolean ifBorderIdIsRiver(int targetHexagon, int i) {
        return ifRiver(getIdInDirect(targetHexagon, i));
    }

    //exculeDirect 默认排除的流向
    private int getRiverFlow(int hexagon, int exculeDirect) {
        for (int d = 1; d <= 6; d++) {
            if (d != exculeDirect) {
                int id = getBorderIdByDirect(hexagon, d);
                if (ifRiver(id)) {
                    return d;
                }
            }
        }
        if (exculeDirect >= 1 && exculeDirect <= 6) {
            return exculeDirect;
        }
        return 0;
    }


    private boolean ifRiver(int hexagonIndex) {
        MapHexagon m = getMapHexagon(hexagonIndex);
        if (m != null) {
            return m.getBackTile() == 11;
        }
        return false;
    }

    public void tempExchangeLv() {
        for (int i = 0; i < regionDatas.size(); i++) {
            MapRegion region = regionDatas.getByIndex(i);
            int o1 = region.getDepLv();
            region.setDepLv(region.getFoodLv());
            region.setFoodLv(o1);
        }
    }


    public void updRegionLvByArea() {

        for (int i = 0; i < regionDatas.size(); i++) {
            MapRegion region = regionDatas.getByIndex(i);
            if (region != null && region.getAreaId() != 0) {
                XmlReader.Element xE = game.gameConfig.getDEF_AREA().getElementById(region.getAreaId());
                if (xE != null) {
                    int v = xE.getInt("depLv", 0);
                    if (v > region.depLv) {
                        region.depLv = v;
                    }
                    v = xE.getInt("mineralLv", 1);
                    if (v > region.mineralLv) {
                        region.mineralLv = v;
                    }
                    v = xE.getInt("oilLv", 0);
                    if (v > region.oilLv) {
                        region.oilLv = v;
                    }
                    v = xE.getInt("foodLv", 0);
                    if (v > region.foodLv) {
                        region.foodLv = v;
                    }
                }
            }
        }
    }


    //将地图放大1倍

    public Fb2Map zoomMap() {
        Fb2Map nm = new Fb2Map();
        nm.mapVersion = this.mapVersion;
        nm.mapWidth = this.mapWidth * 2;
        nm.mapHeight = this.mapHeight * 2;
        nm.mapHexagons = new Array<>();
        nm.regionDatas = new ZHIntMap<>();


        for (int y = 0; y < nm.mapHeight; y++) {
            for (int x = 0; x < nm.mapWidth; x++) {
                int id = GameMap.getId(x, y, nm.mapWidth);
                MapHexagon mapHexagon = new MapHexagon();
                MapHexagon oMapHexagon = mapHexagons.get(GameMap.getId(x / 2, y / 2, mapWidth));
                mapHexagon.setBlockType(oMapHexagon.getBlockType());
                mapHexagon.setBackTile(oMapHexagon.getBackTile());
                mapHexagon.setBackIdx(oMapHexagon.getBackIdx());
                mapHexagon.setBackRefX(oMapHexagon.getBackRefX());
                mapHexagon.setBackRefY(oMapHexagon.getBackRefY());
                mapHexagon.setForeTile(oMapHexagon.getForeTile());
                mapHexagon.setForeIdx(oMapHexagon.getForeIdx());
                mapHexagon.setForeRefX(oMapHexagon.getForeRefX());
                mapHexagon.setForeRefY(oMapHexagon.getForeRefY());
                mapHexagon.setPresetRailway(oMapHexagon.getPresetRailway());
                mapHexagon.ifCoast = (oMapHexagon.getIfCoast());
                mapHexagon.setRegionId(GameMap.getZoomId(oMapHexagon.getRegionId(), mapWidth));
                mapHexagon.setHexagonIndex(id);
                nm.mapHexagons.add(mapHexagon);
                if (oMapHexagon.getHexagonIndex() >= 5500 && oMapHexagon.getHexagonIndex() < 5600) {
                    Gdx.app.log("zoomMap", oMapHexagon.getHexagonIndex() + ": " + oMapHexagon.getX() + "_" + oMapHexagon.getY() + " "
                            + mapHexagon.getHexagonIndex() + ":" + GameMap.getHX(mapHexagon.getHexagonIndex(), nm.mapWidth) + "_" + GameMap.getHY(mapHexagon.getHexagonIndex(), nm.mapWidth) + " " + x + "_" + y);
                }
            }
        }

        for (int i = 0; i < regionCount; i++) {
            MapRegion mapRegion = new MapRegion();
            MapRegion oMapRegion = regionDatas.getByIndex(i);
            mapRegion.setRegion(GameMap.getZoomId(oMapRegion.getRegion(), mapWidth));
            mapRegion.setClimateZone(oMapRegion.getClimateZone());
            mapRegion.setDepLv(oMapRegion.getDepLv());
            mapRegion.setMineralLv(oMapRegion.getMineralLv());
            mapRegion.setFoodLv(oMapRegion.getFoodLv());
            mapRegion.setOilLv(oMapRegion.getOilLv());
            mapRegion.setAreaId(oMapRegion.getAreaId());
            mapRegion.setStrategicRegion(oMapRegion.getStrategicRegion());
            nm.regionDatas.put(mapRegion.getRegion(), mapRegion);
        }


        this.mapWidth = nm.mapWidth;
        this.mapHeight = nm.mapHeight;
        mapHexagons = nm.mapHexagons;
        regionDatas = nm.regionDatas;
        init();
        resetAllCoastBorder();


        return nm;
    }


    public void transBtlModuleByRegionData(MapRegion regionData, BtlModule bm) {
        bm.setMode(2);
        bm.setBm1(regionData.region);
        bm.setBm2(regionData.climateZone);
        bm.setBm3(regionData.depLv);
        bm.setBm4(regionData.mineralLv);
        bm.setBm5(regionData.foodLv);
        bm.setBm6(regionData.oilLv);
        bm.setBm7(regionData.areaId);
        bm.setBm8(regionData.strategicRegion);
    }

    public void transRegionDataByBtlModule(BtlModule bm, MapRegion regionData) {
        regionData.region = bm.getBm1();
        regionData.climateZone = bm.getBm2();
        regionData.depLv = bm.getBm3();
        regionData.mineralLv = bm.getBm4();
        regionData.foodLv = bm.getBm5();
        regionData.oilLv = bm.getBm6();
        regionData.areaId = bm.getBm7();
        regionData.strategicRegion = bm.getBm8();
    }


    public void transBtlModuleByHexagonData(MapHexagon hexagonData, BtlModule bm) {
        bm.setMode(1);
        bm.setBm1(hexagonData.blockType);
        bm.setBm2(hexagonData.backTile);
        bm.setBm3(hexagonData.backIdx);
        bm.setBm4(hexagonData.backRefX);
        bm.setBm5(hexagonData.backRefY);
        bm.setBm6(hexagonData.foreTile);
        bm.setBm7(hexagonData.foreIdx);
        bm.setBm8(hexagonData.foreRefX);
        bm.setBm9(hexagonData.foreRefY);
        bm.setBm10(hexagonData.presetRailway);
        bm.setBm11(hexagonData.ifCoast);
        bm.setBm12(hexagonData.regionId);
    }

    public void transHexagonDataByBtlModule(BtlModule bm, MapHexagon hexagonData) {
        hexagonData.blockType = bm.getBm1();
        hexagonData.backTile = bm.getBm2();
        hexagonData.backIdx = bm.getBm3();
        hexagonData.backRefX = bm.getBm4();
        hexagonData.backRefY = bm.getBm5();
        hexagonData.foreTile = bm.getBm6();
        hexagonData.foreIdx = bm.getBm7();
        hexagonData.foreRefX = bm.getBm8();
        hexagonData.foreRefY = bm.getBm9();
        hexagonData.presetRailway = bm.getBm10();
        hexagonData.ifCoast = bm.getBm11();
        hexagonData.regionId = bm.getBm12();
    }

    public int getMoveCost(int hexagon) {
        if (!ifGridIsPass(hexagon)) {
            return 9999;
        }
        MapHexagon h = mapHexagons.get(hexagon);
        int tile = h.getBackTile();
        boolean isRailway = false;
        if (tile == 1) {
            if (h.getForeTile() == 9) {
                tile = 9;
            }
        } else if (h.getBlockType() != 1) {
            if (h.getOtherTile() == 1) {
                tile = 10;
                if (btl != null && game.resGameConfig.ifRailwayCostByTransportLv) {
                    isRailway = true;
                }
            }
        }
        int rs = game.gameConfig.getDEF_TERRAIN().getElementById(tile).getInt("movementCost", 2);
        if (isRailway) {  //rs 默认铁路消耗为2 启动后 结果为0~2:3 3~6:2 7~10:1
            Fb2Smap.BuildData b = btl.getBuildDataByRegion(h.getRegionId());
            if (b != null) {
                int lv = b.getTransportLvNow();
                return rs + (2 - (lv + 5) / 4);
            } else {
                return rs;
            }
        } else {
            return rs;
        }

    }

    public boolean ifRailway(int hexagon) {
        if (hexagon >= 0 && hexagon < mapHexagons.size && mapHexagons.get(hexagon).getOtherTile() == 1) {
            return true;
        }
        return false;
    }


    public boolean ifPresetRailway(int hexagon) {
        if (hexagon >= 0 && hexagon < mapHexagons.size && mapHexagons.get(hexagon).getPresetRailway() == 1) {
            return true;
        }
        return false;
    }


    public boolean ifBorderRailway(int id) {
        for (int d = 1; d <= 6; d++) {
            int borderId = getBorderIdByDirect(id, d);
            if (ifGridIsPass(borderId) && ifRailway(borderId)) {
                if (btl != null) {
                    if (btl.getLegionIndexByRegion(id) == btl.getLegionIndexByRegion(borderId)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }


    public void dispose() {
        if (mapImage != null) {
            mapImage.dispose();
            mapImage = null;
        }
        if (floorImage != null) {
            floorImage.dispose();
            floorImage = null;
        }
    }
}