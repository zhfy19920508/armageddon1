package com.zhfy.game.screen.actor.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.config.ResDefaultConfig.Map;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameFramework;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2History;
import com.zhfy.game.model.framework.Coord;
import com.zhfy.game.model.framework.DrawGridDAO;
import com.zhfy.game.model.framework.DrawGridListDAO;
import com.zhfy.game.model.framework.DrawDAO;
import com.zhfy.game.model.framework.DrawListDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;

import javax.swing.JOptionPane;

public class MapEditBgActor extends BaseActor {
    //使用左上角为顶点前的备份TextureRegionDAO
    //废弃

    MainGame game;
    // 本类用来存放背景地图,获取地图范围,移动,缩放背景地图,确定点击位置等,以及环状地图
    // 主要用于地图编辑器
    private XmlReader.Element defMap;
    private XmlReader.Element defStage;
    //private HistoryDAOEdit historyEdit;
    private int historyGetType;
    private int historyBattleId;
    private Pixmap pixmap;// 临时画布
    private Fb2Map mapBinDAO;
    private Fb2Map.MapHexagon mapHexagon;
    public Fb2History historyDAO;
    private Color srColor;


    private Texture textureLand;// 陆地
    private Sprite spriteLand;// 陆地

    private int mapId;
    private int mapW_px;
    private int mapH_px;
    private float mapW_ref;
    private float mapH_ref;

    private float x;
    private float y;

    private int w;
    private int h;
    public int gridState;//-1无 0 grid 1 region
    private boolean ifStage;//是否加载了stage
    private boolean ifHistory;
    private int historyPage;
    private int historyBeginYear;
    private int mode;//a,b


    private int historyPageMax;
    private float zoom;
    private float zoomMax;
    private float xMin;
    private float yMin;
    private float xMax;
    private float yMax;

    // 使用此判断是否需要重新计算绘制坐标
    private float mx;
    private float my;
    private float mz;

    private float vw;// 玩家屏幕宽
    private float vh;// 玩家屏幕高

    // 横向平移2格还原地图
    // 纵向平移1格还原地图
    // 网格使用
    private float vx;// 视口位置x
    private float vy;// 视口位置y
    private int gw;// 网格循环横向次数,即宽
    private int gh;// 网格循环纵向次数,即高
    private int gx;// 网格横坐标
    private int gy;// 网格纵坐标
    private float dx_px;// 装饰横坐标
    private float dy_px;// 装饰纵坐标

    private DrawGridListDAO drawCycleList;
    private DrawGridDAO drawCycle;
    private DrawListDAO drawLandList;
    private DrawDAO drawLand;
    private List<Coord> coords;//存放shift点击坐标
    private GameFramework gameFramework;
    private List<Integer> aroundIds;
    private List<Coord> aroundCoords;
    private XmlReader.Element defHistory;
    public int historyId;

    private int i;
    private int j;
    private int iMax;
    private int jMax;
    private int f;
    private int fMax;

    private int keyMode = -1;//

    //private int keyModeMax = 6;//0 1层装饰  1 2层装饰 2地块 3建筑 4stage编辑


    private TextureRegion tempRextureRegion;
    private int tempKeyBlockType;//地理区域
    private int tempRegionId;
    private int tempCountryId;
    private int tempKeyTile;//
    private int tempKeyIdx;
    private int tempBuildId;
    private String tempInputValue;
    private int tempBuildLv;
    private int tempFacility;
    private String tempAreaId;//a键输入地名
    private String tempStageId;
    private String tempId;
    private int tempGetType;
    private int tempBattleId;
    private int tempCopyValue;
    public Texture bgT;
    public Sprite bgS;

    /*
     * 点击坐标正确√ 点击出黑圈 √ shift点击同时出黑圈 √ 清除地块 复制1层地块 粘贴1层地块 复制2层地块 粘贴2层地块
     * 1层装饰偏移↑↓←→ 2层装饰偏移↑↓←→ 配合shift一起粘贴地块 复制区块 粘贴区块
     */

    // 绘制陆地使用,大概是1024*1024的大图
    int lw;
    int lh;
    int li,liMax;
    int lx;
    int ly;
    float lx_px = 0;
    float ly_px = 0;

    private int gi;// 网格循环次数
    // 点击坐标
    private Coord coord;
    // 左下角坐标,即x,y的坐标
    private Coord ulcoord;
    //左上坐标奇偶
    private int ul_parity_x;
    private int ul_parity_y;

    // 绘制中的坐标以及id
    private int draw_gx;
    private int draw_gy;
    private int draw_gd;

    //点击坐标
    /*
     * private float vertex_x_px; private float vertex_y_px;
     */

    // 存放图像素材
    //private TextureListDAO textureList;
    // 对图像素材进行操作
    //private SpriteListDAO spriteList;
    // 存放spriteList的顺序{m_n,i}
    //private ObjectIntMap spriteMap;
    // 存放对应的装饰名与id
    //private ObjectMap tilesMap;
    //private TextureRegionListDAO regionList;



    private TextureRegionDAO backTile;
    private TextureRegionDAO foreTile;
    private TextureRegionDAO presetRailway;

    // 文字贴图(默认不支持中文)。
    BitmapFont bitmapFont;
    Color oldColor;
    Color newColor;
    private ShapeRenderer shapeRenderer;

    public float getDx_px() {
        return dx_px;
    }

    public void setDx_px(float dx_px) {
        this.dx_px = dx_px;
    }

    public float getDy_px() {
        return dy_px;
    }

    public void setDy_px(float dy_px) {
        this.dy_px = dy_px;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public MapEditBgActor(ShapeRenderer shapeRenderer,int mapId, float vw, float vh, MainGame game) {
        super();
        this.game=game;
        gridState=-1;  ifStage=false;
        // 获取defMap
        this.gameFramework=game.getGameFramework();
        defMap = game.gameConfig.getDEF_MAP().getElementById(mapId);;
        mapBinDAO = gameFramework.getMapDaoByMapId(mapId);
        //mapBinDAO.initCoastBorderByRegion();
        this.bitmapFont=game.defaultFont;
        this.shapeRenderer=shapeRenderer;
        this.mode=0;
        srColor=new Color();
        {
            historyId=-1;
            this.mapId = mapId;
            this.x = 0;
            this.y = 0;
            this.w = defMap.getInt("width");
            this.h = defMap.getInt("height");
            this.vw = vw;
            this.vh = vh;
            this.zoom = 1.0f;
            // 实际宽高
            this.mapW_px = GameMap.getWidth(w);
            this.mapH_px = GameMap.getHeight(h);

            this.mx = 10;
            this.my = this.y;
            this.mz = this.zoom;
            this.zoomMax = ((vw / (mapW_px - (Map.GRID_WIDTH * Map.MAP_SCALE) * 1 / 3) < vh / (mapH_px - (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) ? vh / (mapH_px - (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) : vw / (mapW_px - (Map.GRID_WIDTH * Map.MAP_SCALE) * 1 / 3)) / Map.MAP_SCALE) + 0.1f;
            this.xMax = (this.mapW_px * Map.MAP_SCALE * zoom - vw / zoom + (1 - zoom) * this.mapW_px * Map.MAP_SCALE) ;
            this.yMax = (this.mapH_px * Map.MAP_SCALE * zoom - vh / zoom + (1 - zoom) * this.mapH_px * Map.MAP_SCALE) - (Map.HEXAGON_HEIGHT * Map.MAP_SCALE)*2;
            this.yMin = -(Map.HEXAGON_HEIGHT * Map.MAP_SCALE)*2;
            // float   camHScope =vh/2 ;
            //  yMin=(camHScope*zoom+ ResDefaultConfig.Map.HEXAGON_HEIGHT_REF* ResDefaultConfig.Map.MAP_SCALE) ;
            //  yMax=(mapH_px- ResDefaultConfig.Map.HEXAGON_HEIGHT_REF* ResDefaultConfig.Map.MAP_SCALE + camHScope *zoom-vh*zoom);

            this.xMin = 0;
            coords = new ArrayList<Coord>();

            drawCycle = new DrawGridDAO();
            drawCycleList = new DrawGridListDAO();
            drawLand = new DrawDAO();
            drawLandList = new DrawListDAO();

            Gdx.app.log("", "w:" + w + " h:" + h + " mapW_px:" + mapW_px + " mapH_px:" + mapH_px + " zoomMax:" + zoomMax);
        }

        {
            // 绘制画布转为陆地 陆地制作一个pixmap大小的地图,实时绘制

            //textureLand = new Texture(Gdx.files.internal("pixmap/pts/pt1.png"));
            textureLand = game.getAssetManager().get("pixmap/pts/pt_1.png",Texture.class);
            spriteLand = new Sprite(textureLand);
        }

        {
            //bitmapFont = new BitmapFont();
            bitmapFont.setColor(Color.RED);
            bitmapFont.getData().setScale(0.8f * zoom);



            // 绘制画布转为装饰
            //textureList = new TextureListDAO();
            ////textureList.addPixmapByFileName(game.gameConfig.reader,"pm_tiles",game.getAssetManager());
            //textureList.addPixmapByFileName(game.gameConfig.reader,"pm_tiles",game.getAssetManager());

            //regionList=new TextureRegionListDAO();
            //regionList.addRegionByByFileName("bt_tiles");

            //spriteList = new SpriteListDAO();
            //spriteMap = spriteList.add(textureList);
            //tilesMap=game.gameConfig.getTERRAINIMG_MAP(true);
            //game.getImgLists().addRegionByUpdName(tilesMap);
        }

        {
            // 绘制画布转为海洋
        }

        {
            // 实时绘制网格
            // textureGrid = new
            // Texture(Gdx.files.internal("pixmap/tiles/grid.png"));
            // spriteGrid = new Sprite(textureGrid);
        }



        setSize(this.mapW_px * Map.MAP_SCALE, this.mapH_px * Map.MAP_SCALE);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 点击图片，图片隐藏![这里写图片描述](https://img-blog.csdn.net/20160425220710777)
                // super.clicked(event, x, y);
                // Gdx.app.log("点击的精灵位置1", "x:" + (getHX()/getZoom()+x)+" y:"
                // +(getHY()/getZoom()+ y));

                // 由于要在actor等中使用坐标绘图,所以采用libgdx的actor坐标系
                // 左下为0,0,向右上方向负数扩展
                //Gdx.app.log("点击背景位置1", "x:" + (getHX()-x)+" y:" + (getHY()- y)+" zoom:" + zoom);
                //Gdx.app.log("点击背景位置1", "bgx:" + getHX()+" bgy:" + getHY()+"zoom:" + zoom+" x:" + x+" y:" + y);
                // Gdx.app.log("点击背景位置2", "actX:" + (getHX()-x)/getZoom()+"
                // actY:"+ (getHY()-y)/getZoom());

                //Gdx.app.log("点击坐标", "imgX:" + (getHX()-xMin/ getZoom() - (x)/ getZoom()) + " imgY:" + getImgY((getHY()-yMin/ getZoom() - (y)/ getZoom())));
                {//点击坐标
                    coord = GameMap.getHotCell2(getX() - (x) / getZoom(), getImgY(getY() - yMin - (y) / getZoom()));
                    coordInit(coord);
                    if(!mapBinDAO.ifGridIsPass(coord.getId())){
                        return;
                    }

                    Gdx.app.log("坐标", "zoom:"+zoom+" imgX:" + coord.getX() + " imgY:" + coord.getY() + " id:" + coord.getId()+" regionId:"+mapBinDAO.getMapbin().get(coord.getId()).getRegionId()+" ");
                    mapBinDAO.getMapbin().get(coord.getId()).log(coord.getId());
                    if(ifStage&&keyMode==4){
                        //stageEdit.logStageInfoByRegionId(mapBinDAO.getMapbin().get(coord.getId()).getRegionId());
                    }else if(ifHistory&&keyMode==5){
                        // historyEdit.logHistoryInfoByRegionId(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId());
                        historyDAO.logHistoryInfoByRegionId(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId());
                    }else {
                        //Gdx.app.log("地形", " blockType:" + mapBinDAO.getMapbin().get(coord.getid()).getBlockType() + " backTile:" + mapBinDAO.getMapbin().get(coord.getid()).getBackTile() + " backIdx:" + mapBinDAO.getMapbin().get(coord.getid()).getBackIdx());
                        //     Gdx.app.log("建筑", " buildId:" + mapBinDAO.getMapbin().get(coord.getId()).getBuildId() + " buildLv:" + mapBinDAO.getMapbin().get(coord.getId()).getBuildLv() + " facility:" + mapBinDAO.getMapbin().get(coord.getId()).getFacility() + " areaIdx:" + mapBinDAO.getMapbin().get(coord.getId()).getAreaId()+ " terrain:" + mapBinDAO.getMapbin().get(coord.getId()).getBackTile());
                    }

                    //判断是否点击shift
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        //如果是持续点击,则添加
                        coords.add(coord);

                    } else {
                        coords.clear();
                        coords.add(coord);
                        /*{//测试点击
                            aroundIds= mapBinDAO.getAroundIdsById(coord.getid(),0);
                            aroundCoords=GameMap.addCoordByIds(aroundIds, w);
                            coordsInit(aroundCoords);
                            coords.addAll(aroundCoords);
                        }*/
                        /*{   aroundIds=new ArrayList<Integer>();
                            aroundIds.add(mapBinDAO.getShortAroundId(mapBinDAO.getAroundIdsById(coord.getid(), 7),mapBinDAO.getMapbin().get(coord.getid()).getPolitical()));
                            aroundCoords=GameMap.addCoordByIds(aroundIds, w);
                            coordsInit(aroundCoords);
                            coords.addAll(aroundCoords);
                        }*/
                    }
                    fMax = coords.size();
                }
                //Gdx.app.log("顶点坐标", "imgX:"+vertex_x_px+" imgY:"+vertex_y_px);
                //Gdx.app.log("顶点坐标2", "X:"+getHX()+" Y:"+getHY());
                //Gdx.app.log("顶点坐标3", "X:"+getStaticImgX(x)+" Y:"+getStaticImgY(y));
            }
        });
        loadBgMapImage(defMap.get("name"));
        //loadBgMapImage("world_map.png");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        {
            {

                // 实时绘制网格
                if ((mx != x || my != y || mz != zoom)) {
                    drawCycleList.clear();
                    drawLandList.clear();
                    //显示的范围,跨屏显示+3
                    gw = (int) (((vw / (Map.GRID_WIDTH * Map.MAP_SCALE * getZoom())) + 3));
                    gh = (int) (((vh / (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * getZoom())) + 4));
                    ulcoord = GameMap.getHotCell(ulcoord,(getImgX()), getImgY(), zoom,1.0f);

                    ul_parity_x = (ulcoord.getX() & 1);
                    ul_parity_y = (ulcoord.getY() & 1);
                    { // 或根据玩家屏幕大小限制缩放比例
                        // 绘制循环陆地,如果比例尺大于一定值,则绘制三层
                        lw = (int) (spriteLand.getWidth() / getZoom()/ vw  + 2);
                        lh = (int) (spriteLand.getHeight() /getZoom() / vh  + 1);
                        //lh = lh < 2 ? 2 : lh;
                        //lw=3;
                        //lh=2;
                        //有区域,屏幕需要显示三块
                        if((getX() * zoom % (spriteLand.getWidth() * getZoom()) + 2*spriteLand.getWidth() * getZoom())<(ulcoord.getX() * zoom+vw)  ){
                            lw++;
                        }

                        for (li = 0,liMax=lw*lh; li < liMax; li++) {
                            lx = (li % lw) + 1;
                            ly = (li / lw) + 1;
                            lx_px = getX() * zoom % (spriteLand.getWidth() * getZoom()) + spriteLand.getWidth() * getZoom() * (lx - 1);
                            ly_px = getY() * zoom % (spriteLand.getHeight() * getZoom()) + spriteLand.getHeight() * getZoom() * (ly - 1);

                            drawLand = new DrawDAO();
                            drawLand.setLx_px(lx_px);
                            drawLand.setLy_px(ly_px);
                            drawLandList.add(drawLand);
                            // Gdx.app.log("spriteLand:"+li," lx:"+lx+" ly:"+ly+" lx_px:"+lx_px+" ly_px:"+ly_px );
                        }
                    }

                    for (gi = 0; gi < gw * gh; gi++) {
                        gx = (gi % gw) + 1;
                        gy = (gi / gw) + 1;
                        dx_px = -getImgX() * zoom + ((int) ((getImgX() * this.zoom) / (Map.GRID_WIDTH * Map.MAP_SCALE * this.zoom)) - 1) * (Map.GRID_WIDTH * Map.MAP_SCALE * this.zoom) + (((gx - 1) * Map.GRID_WIDTH *zoom* Map.MAP_SCALE));
                        dy_px = -(vh % (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) + ((vh) / zoom) * (zoom - 1) + getImgY() * zoom + vh / zoom
                                - ((((int) ((getImgY() * zoom) / (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) - 1) * (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) + (((gy - 1) * Map.HEXAGON_HEIGHT + ((gx & 1) == 1 ? 0 : ((ul_parity_x & 1) == 1 ? Map.HEXAGON_HEIGHT_REF : -Map.HEXAGON_HEIGHT_REF))) * zoom * Map.MAP_SCALE) + ((ul_parity_x & 1) == 1 ? -Map.HEXAGON_HEIGHT_REF : 0) * Map.MAP_SCALE * zoom);

                        // 要显示的id,以及对应的图片
                        // 绘制的x坐标
                        draw_gx = ulcoord.getX() + gx - 2;
                        draw_gy = ulcoord.getY() + gy - gh;
                        draw_gd = w * draw_gy + draw_gx;

                        if (draw_gx < w && draw_gy < h && draw_gd < mapBinDAO.getMapbin().size && draw_gd >= 0) {

                            mapHexagon = mapBinDAO.getMapbin().get(draw_gd);
                            //if(mapBins.getBackIdx()!=0||mapBins.getForeIdx()!=0) {
                            if(mapHexagon.getBackTile() ==0&& mapHexagon.getBackIdx()==0){
                                backTile=null;
                            }else {
                                backTile = game.getImgLists().getTextureByName(mapHexagon.getBackTile() + "_" + mapHexagon.getBackIdx());
                            }
                            if(mapHexagon.getForeTile() ==0&& mapHexagon.getForeIdx()==0){
                                foreTile=null;
                            }else {
                                foreTile=game.getImgLists().getTextureByName(mapHexagon.getForeTile() + "_" + mapHexagon.getForeIdx());
                            }
                            if(mapHexagon.getPresetRailway() ==0){
                                presetRailway=null;
                            }else {
                                presetRailway=game.getImgLists().getTextureByName("10_" + mapHexagon.getPresetRailWayId());
                            }

                            drawCycle = new DrawGridDAO();
                            /*if(draw_gd==51535){
                                int s=0;
                            }*/
                            drawCycle.setDraw_gx(draw_gx);
                            drawCycle.setDraw_gy(draw_gy);
                            drawCycle.setDraw_gd(draw_gd);
                            drawCycle.setDx_px(dx_px);
                            drawCycle.setDy_px(dy_px);
                            drawCycle.setRegionId(mapHexagon.getRegionId());
                            if (backTile!=null) {
                                drawCycle.setBackTile(backTile.getTextureRegion());
                                drawCycle.setBack_x_px(dx_px +  mapHexagon.getBackRefX() * zoom * Map.MAP_SCALE - backTile.getRefx() * zoom * Map.MAP_SCALE);
                                drawCycle.setBack_y_px(dy_px + mapHexagon.getBackRefY() * zoom * Map.MAP_SCALE - backTile.getRefy() * zoom * Map.MAP_SCALE);
                                drawCycle.setBack_w_px(backTile.getTextureRegion().getRegionWidth());
                                drawCycle.setBack_h_px(backTile.getTextureRegion().getRegionHeight() );
                            }

                            if (foreTile!=null ) {
                                drawCycle.setForeTile(foreTile.getTextureRegion());
                                drawCycle.setFore_x_px(dx_px +  mapHexagon.getForeRefX() * zoom * Map.MAP_SCALE -foreTile.getRefx() * zoom * Map.MAP_SCALE);
                                drawCycle.setFore_y_px(dy_px +  mapHexagon.getForeRefY() * zoom * Map.MAP_SCALE - foreTile.getRefy() * zoom * Map.MAP_SCALE);
                                drawCycle.setFore_w_px(foreTile.getTextureRegion().getRegionWidth() );
                                drawCycle.setFore_h_px(foreTile.getTextureRegion().getRegionHeight() );
                            }
                            if(presetRailway!=null){
                                drawCycle.setPresetRailway(presetRailway.getTextureRegion());
                                drawCycle.setPresetRailway_x_px(dx_px  -presetRailway.getRefx() * zoom * Map.MAP_SCALE);
                                drawCycle.setPresetRailway_y_px(dy_px  - presetRailway.getRefy() * zoom * Map.MAP_SCALE);
                            }


                            if(keyMode==10&&mapHexagon.getRegionId()==draw_gd&& mapHexagon.getRegionStrategicRegion()!=0){
                                if(mapHexagon.getregionAreaId()!=0){
                                    drawCycle.setStr(game.gameMethod.getStrValueT("areaname_"+ mapHexagon.getregionAreaId())+"\n"+game.gameMethod.getStrValueT("strategicRegion_" + mapHexagon.getRegionStrategicRegion())+":"+ mapHexagon.getRegionStrategicRegion());
                                }else {
                                    drawCycle.setStr(game.gameMethod.getStrValueT("strategicRegion_" + mapHexagon.getRegionStrategicRegion())+":"+ mapHexagon.getRegionStrategicRegion());
                                }
                            }else{
                                if(mapHexagon.getregionAreaId()!=0){
                                    drawCycle.setStr(game.gameMethod.getStrValueT("areaname_"+ mapHexagon.getregionAreaId()));
                                }else{
                                    drawCycle.setStr(null);
                                }
                            }

                            drawCycleList.add(drawCycle);

                            //}
                            mapHexagon = null;
                        }
                        iMax = drawCycleList.size();
                        jMax = drawLandList.size();
                        mx = x;
                        my = y;
                        mz = zoom;
                        if(bgT!=null) {
                            updLoadMPotion();
                        }

                    }/**/
                    /*jMax = drawLandList.size();
                    mx = x;
                    my = y;
                    mz = zoom;*/

                } /*
                 * else { //如果不移动,降低绘图帧率节约 try {
                 * drawInterval=(long)(1000/30-Gdx.graphics.getDeltaTime())*3;
                 * Thread.sleep(drawInterval); } catch (InterruptedException
                 * e) { //e.printStackTrace(); } }
                 */

                for (j = 0; j < jMax; j++) {
                    //batch.setColor(DefDAO.getColor(j));
                    batch.draw(spriteLand, drawLandList.get(j).getLx_px(), drawLandList.get(j).getLy_px(), getOriginX(), getOriginY(), spriteLand.getWidth(), spriteLand.getHeight(), getZoom(), getZoom(), getRotation());
                    //batch.setColor(Color.WHITE);
                }
               /* if(keyMode==2||keyMode==3 ) {
                    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                    shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
                    GameMap.drawLegionMapRegionForMapEditByCamShapeRenderer(mapBinDAO, shapeRenderer, drawCycleList, getScaleX(), true, mapW_px, mapH_px);
                }*/


                for (i = 0; i < iMax; i++) {
                    mapHexagon = mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd());

                    if (drawCycleList.get(i).getBackTile() !=null) {
                        batch.draw(drawCycleList.get(i).getBackTile(), drawCycleList.get(i).getBack_x_px(), drawCycleList.get(i).getBack_y_px(), getOriginX(), getOriginY(), drawCycleList.get(i).getBack_w_px(), drawCycleList.get(i).getBack_h_px(), getZoom(), getZoom(), getRotation());
                    }
                    if (drawCycleList.get(i).getForeTile() !=null) {
                        batch.draw(drawCycleList.get(i).getForeTile(), drawCycleList.get(i).getFore_x_px(), drawCycleList.get(i).getFore_y_px(), getOriginX(), getOriginY(), drawCycleList.get(i).getFore_w_px(), drawCycleList.get(i).getFore_h_px(), getZoom(), getZoom(), getRotation());
                    }
                    if (drawCycleList.get(i).getPresetRailway() !=null) {
                        batch.setColor(1,1,1,0.5f);
                        batch.draw(drawCycleList.get(i).getPresetRailway(), drawCycleList.get(i).getPresetRailway_x_px(), drawCycleList.get(i).getPresetRailway_y_px(), getOriginX(), getOriginY(), drawCycleList.get(i).getPresetRailway().getRegionWidth(),drawCycleList.get(i).getPresetRailway().getRegionHeight(), getZoom(), getZoom(), getRotation());
                        batch.setColor(Color.WHITE);
                    }
                    if(gridState==0){
                        tempRextureRegion=game.getImgLists().getTextureByName("grid").getTextureRegion();
                        batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                                tempRextureRegion.getRegionHeight() , getZoom(), getZoom(), getRotation());

                    }

               /* if(keyMode==4&&ifStage){
                        if(drawCycleList.get(i).getRegionId()!=-1) {
                            //oldColor = batch.getColor();
                            newColor = stageEdit.getColorByRegion(drawCycleList.get(i).getRegionId());
                            batch.setColor(newColor);
                                *//*batch.draw(spriteList.get(spriteMap.get("hexagon",0)).getSprite(), drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), spriteList.get(spriteMap.get("hexagon",0)).getSprite().getWidth() * Map.MAP_SCALE,
                                        spriteList.get(spriteMap.get("hexagon",0)).getSprite().getHeight() * Map.MAP_SCALE, getZoom(), getZoom(), getRotation());
                                *//*tempRextureRegion=game.getImgLists().getTextureByName("hexagon").getTextureRegion();
                            batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                                    tempRextureRegion.getRegionHeight() , getZoom(), getZoom(), getRotation());


                            batch.setColor(Color.WHITE);
                            *//* *//*
                            if (drawCycleList.get(i).getDraw_gd() == drawCycleList.get(i).getRegionId()) {//绘制核心区
                                   *//* batch.draw(spriteList.get(spriteMap.get("circle",0)).getSprite(), drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), spriteList.get(spriteMap.get("circle",0)).getSprite().getWidth() * Map.MAP_SCALE,
                                            spriteList.get(spriteMap.get("circle",0)).getSprite().getHeight() * Map.MAP_SCALE, getZoom(), getZoom(), getRotation());
*//*
                                tempRextureRegion=game.getImgLists().getTextureByName("circle").getTextureRegion();
                                batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                                        tempRextureRegion.getRegionHeight(), getZoom(), getZoom(), getRotation());


                            }
                        }

                    }else */if(keyMode==5&&ifHistory){
                        if(drawCycleList.get(i).getRegionId()!=-1) {
                            //oldColor = batch.getColor();

                            Fb2History.HistoryData his= historyDAO.getHistoryDataByPage(historyPage,drawCycleList.get(i).getRegionId());
                            if(his==null){
                                continue;
                            }
                            newColor =game.gameConfig.getCOUNTRY_COLORS().get(his.getNowCountryIndex() );
                            batch.setColor(newColor);
                                /*batch.draw(spriteList.get(spriteMap.get("hexagon",0)).getSprite(), drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), spriteList.get(spriteMap.get("hexagon",0)).getSprite().getWidth() * Map.MAP_SCALE,
                                        spriteList.get(spriteMap.get("hexagon",0)).getSprite().getHeight() * Map.MAP_SCALE, getZoom(), getZoom(), getRotation());
                                */tempRextureRegion=game.getImgLists().getTextureByName("hexagon").getTextureRegion();
                            batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                                    tempRextureRegion.getRegionHeight() , getZoom(), getZoom(), getRotation());


                            batch.setColor(Color.WHITE);
                            /* */
                            if (drawCycleList.get(i).getDraw_gd() == drawCycleList.get(i).getRegionId()) {//绘制核心区
                                if(mode==0) {
                                        /*batch.draw(spriteList.get(spriteMap.get("circle",0)).getSprite(), drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), spriteList.get(spriteMap.get("circle",0)).getSprite().getWidth() * Map.MAP_SCALE,
                                                spriteList.get(spriteMap.get("circle",0)).getSprite().getHeight() * Map.MAP_SCALE, getZoom(), getZoom(), getRotation());
                                        */tempRextureRegion=game.getImgLists().getTextureByName("circle").getTextureRegion();
                                    batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                                            tempRextureRegion.getRegionHeight() , getZoom(), getZoom(), getRotation());


                                }else if(mode==1){
                                    //  historyGetType=historyDAO.get.get(historyEdit.getHistoryIndex(historyPage,drawCycleList.get(i).getRegionId())).getBm1_6();
                                    //  historyBattleId=historyDAO.getBm1().get(historyEdit.getHistoryIndex(historyPage,drawCycleList.get(i).getRegionId())).getBm1_7();
                                    historyGetType=historyDAO.getHistoryDataByPage(historyPage,drawCycleList.get(i).getRegionId()).getGetType();
                                    historyBattleId=historyDAO.getHistoryDataByPage(historyPage,drawCycleList.get(i).getRegionId()).getBattleId();
                                    if(historyGetType==0){
                                            /*batch.draw(spriteList.get(spriteMap.get("gettype-0",0)).getSprite(), drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), spriteList.get(spriteMap.get("gettype-0",0)).getSprite().getWidth() * Map.MAP_SCALE,
                                                    spriteList.get(spriteMap.get("gettype-0",0)).getSprite().getHeight() * Map.MAP_SCALE, getZoom(), getZoom(), getRotation());
                                            */tempRextureRegion=game.getImgLists().getTextureByName("gettype-0").getTextureRegion();
                                        batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                                                tempRextureRegion.getRegionHeight() , getZoom(), getZoom(), getRotation());

                                    }else if(historyGetType==1){
                                            /*batch.draw(spriteList.get(spriteMap.get("gettype-1",0)).getSprite(), drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), spriteList.get(spriteMap.get("gettype-1",0)).getSprite().getWidth() * Map.MAP_SCALE,
                                                    spriteList.get(spriteMap.get("gettype-1",0)).getSprite().getHeight() * Map.MAP_SCALE, getZoom(), getZoom(), getRotation());
*/
                                        tempRextureRegion=game.getImgLists().getTextureByName("gettype-1").getTextureRegion();
                                        batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                                                tempRextureRegion.getRegionHeight() , getZoom(), getZoom(), getRotation());


                                        if(historyBattleId!=0){
                                            bitmapFont.draw(batch, "id:" + historyBattleId, drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                                        }
                                    }else if(historyGetType==2){

                                        tempRextureRegion=game.getImgLists().getTextureByName("gettype-2").getTextureRegion();
                                        batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                                                tempRextureRegion.getRegionHeight() , getZoom(), getZoom(), getRotation());


                                           /* batch.draw(spriteList.get(spriteMap.get("gettype-2",0)).getSprite(), drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), spriteList.get(spriteMap.get("gettype-2",0)).getSprite().getWidth() * Map.MAP_SCALE,
                                                    spriteList.get(spriteMap.get("gettype-2",0)).getSprite().getHeight() * Map.MAP_SCALE, getZoom(), getZoom(), getRotation());
                                        */}

                                }
                            }
                        }
                    }


                    if(gridState==0||(gridState==1&&drawCycleList.get(i).getDraw_gd()==drawCycleList.get(i).getRegionId())){
                        bitmapFont.draw(batch, " x:" + drawCycleList.get(i).getDraw_gx(), drawCycleList.get(i).getDx_px() + 14 * zoom, drawCycleList.get(i).getDy_px() + 30 * zoom);
                        bitmapFont.draw(batch, "id:" + drawCycleList.get(i).getDraw_gd(), drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                        bitmapFont.draw(batch, " y:" + drawCycleList.get(i).getDraw_gy(), drawCycleList.get(i).getDx_px() + 14 * zoom, drawCycleList.get(i).getDy_px() + 10 * zoom);

                        if (gridState==1&&drawCycleList.get(i).getDraw_gd()==drawCycleList.get(i).getRegionId()&&drawCycleList.get(i).getStr() != null) {
                            game.gameConfig.gameFont.draw(batch, drawCycleList.get(i).getStr(), drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                        }
                    }

                }

                for (i = 0; i < iMax; i++) {
                    mapHexagon = mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd());
                    if (/*(keyMode==0&&mode==1) ||*/keyMode == 2 || keyMode == 3||keyMode == 7||keyMode == 8||keyMode == 9) {
                        if (drawCycleList.get(i).getRegionId() != -1) {
                            newColor = new Color(GameUtil.getColorByNum(drawCycleList.get(i).getRegionId()));
                            batch.setColor(newColor);
                            tempRextureRegion = game.getImgLists().getTextureByName("hexagon").getTextureRegion();
                            batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth(),
                                    tempRextureRegion.getRegionHeight(), getZoom(), getZoom(), getRotation());
                            batch.setColor(Color.WHITE);

                            //MapBinDAO mapBinDAO, ShapeRenderer shapeRenderer,DrawGridListDAO drawGridListDAO, float scale, Boolean ifDrawSea, int mapW_px, int mapH_px
                        }


                        if (mapHexagon.regionLineBorderTile!=null) {//
                            batch.draw(mapHexagon.regionLineBorderTile.getTextureRegion(), drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), mapHexagon.regionLineBorderTile.getTextureRegion().getRegionWidth(),
                                    mapHexagon.regionLineBorderTile.getTextureRegion().getRegionHeight(), getZoom(), getZoom(), getRotation());
                        }
                        if (mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getRegionMineralLv() == 4) {//

                            tempRextureRegion = game.getImgLists().getTextureByName("circle2").getTextureRegion();
                            batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth(),
                                    tempRextureRegion.getRegionHeight(), getZoom(), getZoom(), getRotation());
                        }


                        if (drawCycleList.get(i).getDraw_gd() == drawCycleList.get(i).getRegionId()) {//绘制核心区
                            if (mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getBackTile() == 1 || mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getBackTile() == 2) {

                                tempRextureRegion = game.getImgLists().getTextureByName("circle3").getTextureRegion();
                                batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth(),
                                        tempRextureRegion.getRegionHeight(), getZoom(), getZoom(), getRotation());
                                bitmapFont.draw(batch, mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getRegionId() + "", drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);

                            } else {

                                tempRextureRegion = game.getImgLists().getTextureByName("circle").getTextureRegion();
                                batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth(),
                                        tempRextureRegion.getRegionHeight(), getZoom(), getZoom(), getRotation());

                            }
                            if (drawCycleList.get(i).getStr() != null) {
                                game.gameConfig.gameFont.draw(batch, drawCycleList.get(i).getStr(), drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);

                            }
                            if(keyMode==7){//dep
                                int depLv=mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getRegionDepLv();
                                if(depLv>0){
                                    bitmapFont.draw(batch, depLv + "", drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                                }
                            }  else if(keyMode==8){//mineral
                                int mineralLv=mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getRegionMineralLv();
                                if(mineralLv>0){
                                    bitmapFont.draw(batch, mineralLv + "", drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                                }
                            }  else if(keyMode==9){//oil
                                int oilLv=mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getRegionOilLv();
                                if(oilLv>0){
                                    bitmapFont.draw(batch, oilLv + "", drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                                }
                            }  else if(keyMode==11){//food
                                int foodLv=mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getRegionFoodLv();
                                if(foodLv>0){
                                    bitmapFont.draw(batch, foodLv + "", drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                                }
                            }  else {
                                bitmapFont.draw(batch, mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getRegionId() + "", drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                            }

                        }
                        if (drawCycleList.get(i).getRegionId() == -1) {
                            bitmapFont.draw(batch, "ff", drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                        }
                        if (keyMode == 3 && drawCycleList.get(i).getStr() != null) {//&&drawCycleList.get(i).getPolitical()!=drawCycleList.get(i).getDraw_gd()
                            if (keyMode == 3 && drawCycleList.get(i).getStr() != null) {//&&drawCycleList.get(i).getPolitical()!=drawCycleList.get(i).getDraw_gd()
                                game.gameConfig.gameFont.draw(batch, drawCycleList.get(i).getStr(), drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                            }
                        }
                    } else if (keyMode == 6) {//气候
                        if (drawCycleList.get(i).getRegionId() != -1) {
                            newColor = DefDAO.getColorForCliamZone(mapHexagon.getRegionClimatZone());
                            batch.setColor(newColor.r,newColor.g,newColor.b,0.3f);
                            tempRextureRegion = game.getImgLists().getTextureByName("hexagon").getTextureRegion();
                            batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth(),
                                    tempRextureRegion.getRegionHeight(), getZoom(), getZoom(), getRotation());
                            batch.setColor(Color.WHITE);

                            //MapBinDAO mapBinDAO, ShapeRenderer shapeRenderer,DrawGridListDAO drawGridListDAO, float scale, Boolean ifDrawSea, int mapW_px, int mapH_px
                        }
                    }else if (keyMode == 10) {//sr
                        if (drawCycleList.get(i).getRegionId() != -1) {
                            int sr=mapBinDAO.getMapbin().get(drawCycleList.get(i).getDraw_gd()).getRegionStrategicRegion();
                            if(sr==0){
                                newColor = new Color(GameUtil.getColorByNum(drawCycleList.get(i).getRegionId()));
                                batch.setColor(newColor);
                            }else{
                                // srColor =GameUtil.getColorForSR(srColor,sr);
                                GameUtil.setColorForSR(batch,null,sr,-1);
                                // batch.setColor(srColor.r,srColor.g,srColor.b,0.7f);
                            }


                            tempRextureRegion = game.getImgLists().getTextureByName("hexagon").getTextureRegion();
                            batch.draw(tempRextureRegion, drawCycleList.get(i).getDx_px(), drawCycleList.get(i).getDy_px(), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth(),
                                    tempRextureRegion.getRegionHeight(), getZoom(), getZoom(), getRotation());
                            batch.setColor(Color.WHITE);


                            if(drawCycleList.get(i).getDraw_gd() == drawCycleList.get(i).getRegionId()&&sr!=0&&drawCycleList.get(i).getStr()!=null) {
                                //bitmapFont.draw(batch, sr + "", drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 20 * zoom);
                                game.gameConfig.gameFont.draw(batch,drawCycleList.get(i).getStr() , drawCycleList.get(i).getDx_px() + 11 * zoom, drawCycleList.get(i).getDy_px() + 30 * zoom);

                            }
                            //MapBinDAO mapBinDAO, ShapeRenderer shapeRenderer,DrawGridListDAO drawGridListDAO, float scale, Boolean ifDrawSea, int mapW_px, int mapH_px
                        }
                    }
                }
                for (f = 0; f < fMax; f++) {
                    if(fMax==1){
                        tempRextureRegion=game.getImgLists().getTextureByName("hexagonSelected").getTextureRegion();
                    }else{
                        tempRextureRegion=game.getImgLists().getTextureByName("fog").getTextureRegion();
                    }
                    batch.draw(tempRextureRegion, getStaticImgX(coords.get(f).getVertexX()), getStaticImgY(coords.get(f).getVertexY()), getOriginX(), getOriginY(), tempRextureRegion.getRegionWidth() ,
                            tempRextureRegion.getRegionHeight() , getZoom(), getZoom(), getRotation());
                }
                //测试 绘制 顶点坐标
                /*if(gridState==1){

                }*/

                if(bgT !=null){
                    bgS.draw(batch);
                }

            }
        }
        //快捷键
        keyInput();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        if (x > xMin) {
            x = xMin;
        } else if (x < -(xMax)) {
            x = -(xMax);
        }
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        if (y > yMin+(Map.HEXAGON_HEIGHT * Map.MAP_SCALE)) {
            y = yMin+(Map.HEXAGON_HEIGHT * Map.MAP_SCALE);
        } else if (y < -yMax) {
            y = -yMax;
        }
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public float getVw() {
        return vw;
    }

    public void setVw(float vw) {
        this.vw = vw;
    }

    public float getVh() {
        return vh;
    }

    public void setVh(float vh) {
        this.vh = vh;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom, float cx, float cy) {
        this.xMax = (this.mapW_px * Map.MAP_SCALE * zoom - vw / zoom + (1 - zoom) * this.mapW_px * Map.MAP_SCALE) ;
        this.yMax = (this.mapH_px * Map.MAP_SCALE * zoom - vh / zoom + (1 - zoom) * this.mapH_px * Map.MAP_SCALE) - (Map.HEXAGON_HEIGHT * Map.MAP_SCALE);
        this.xMin = 0;
        this.yMin = -(Map.HEXAGON_HEIGHT * Map.MAP_SCALE);
        cx = (getX() - cx);
        cy = (getY() - cy);

        if (zoom < zoomMax) {
            zoom = zoomMax;
        } else if (zoom > 1.4f) {
            zoom = 1.4f;
        } else if (zoom < 0.5f) {
            zoom = 0.5f;
        } else {
            setX(x + (cx - x) * (zoom - this.zoom) / this.zoom);
            setY(y + (cy - y) * (zoom - this.zoom) / this.zoom);
            this.zoom = zoom;
        }
        //Gdx.app.log("阻止缩放", " zoomMax:" + zoomMax + " zoom:" + zoom);

    }

    // 将↓→坐标(左上角为0,0,越向右下越大)转为←↓act坐标(左下角为0,0,越向右上越小)
    public float getActXCoordByImgX(float print_x) {
        return (-print_x);
    }

    public float getActYCoordByImgY(float print_y) {
        return (print_y - this.mapH_px);
    }

    // 将←↓act坐标(左下角为0,0,越向右上越小)转化为↓→坐标(左上角为0,0,越向右下越大)
    // 将↓→坐标(左上角为0,0,越向右下越大)转为←↓act坐标(左下角为0,0,越向右上越小)
    public float getImgX(float print_x) {
        if (print_x != 0f) {
            return (-print_x);
        } else {
            return 0f;
        }
    }

    public float getImgY(float print_y) {
        return (this.mapH_px * Map.MAP_SCALE + print_y);
    }

    public float getMapYByImgY(float print_y) {
        return (print_y - this.mapH_px * Map.MAP_SCALE);
    }

    //获取实际坐标
    public float getImgX() {
        if (this.x != 0f) {
            return (-this.x);
        } else {
            return 0f;
        }
    }

    public float getImgY() {
        return (this.mapH_px * Map.MAP_SCALE + this.y);
    }

    // 清理5张图层
    public void dispose() {
        /*if (textureLand != null) {
            textureLand.dispose();
        }*/

        if (defMap != null) {
            defMap = null;
        }
        if(bgT !=null){
            bgT.dispose();
            bgT =null;
        }
    }


    public Pixmap getPixmap() {
        return pixmap;
    }

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }

    public Fb2Map getMapBinDAO() {
        return mapBinDAO;
    }

    public void setMapBinDAO(Fb2Map mapBinDAO) {
        this.mapBinDAO = mapBinDAO;
    }

    public Texture getTextureLand() {
        return textureLand;
    }

    public void setTextureLand(Texture textureLand) {
        this.textureLand = textureLand;
    }

    public Sprite getSpriteLand() {
        return spriteLand;
    }

    public void setSpriteLand(Sprite spriteLand) {
        this.spriteLand = spriteLand;
    }


    public float getZoomMax() {
        return zoomMax;
    }

    public void setZoomMax(float zoomMax) {
        this.zoomMax = zoomMax;
    }

    public int getGw() {
        return gw;
    }

    public void setGw(int gw) {
        this.gw = gw;
    }

    public int getGh() {
        return gh;
    }

    public void setGh(int gh) {
        this.gh = gh;
    }

    public int getMapW_px() {
        return mapW_px;
    }

    public void setMapW_px(int mapW_px) {
        this.mapW_px = mapW_px;
    }

    public int getMapH_px() {
        return mapH_px;
    }

    public void setMapH_px(int mapH_px) {
        this.mapH_px = mapH_px;
    }


    public Coord getUlcoord() {
        return ulcoord;
    }

    public void setUlcoord(Coord ulcoord) {
        this.ulcoord = ulcoord;
    }

    public void saveMapBin() {
        gameFramework.saveMapDaoByMapId(mapBinDAO, mapId);
        if(ifHistory&&historyDAO!=null){
            /*try {
                BTLTooL.saveBtl(game,historyDAO.getBTL(), ResDefaultConfig.Rules.RULE_FB2_HISTORY,"bin/"+defHistory.get("name")+".bin"); //BTLDAO btl,StringName rulePath,  StringName filePath
            } catch (IOException e) {
                e.printStackTrace();
                Gdx.app.error("history","保存失败:"+defHistory.get("name"));
            }*/
            historyDAO.save("bin/"+defHistory.get("name")+".bin");
        }

    }

    //假如静态绘制,通过此方法获得绘制坐标
    public float getStaticImgX(float x) {
        return (this.x - x) * zoom;
    }

    public float getStaticImgY(float y) {
        return (this.y - y) * zoom;
    }

    public int getKeyMode() {
        return keyMode;
    }

    public void setKeyMode(int keyMode) {
        this.keyMode = keyMode;
    }

    /*public int getKeyModeMax() {
        return keyModeMax;
    }

    public void setKeyModeMax(int keyModeMax) {
        this.keyModeMax = keyModeMax;
    }
*/




    //快捷键 TODO
    private void keyInput() {
        /*
         * 0 1层地块,带地理属性,wasd变为1层装饰偏移,c复制(复制地理属性),v粘贴,x清除(不清除地理属性)
         * 1 2层地块,wasd变为2层装饰偏移,c复制(复制地理属性),v粘贴(批量),x清除
         * 2 粘贴区块,x重置为当前id,c复制(不复制地理属性),v粘贴(批量),a替换该区块为当前地块,f 替换包起来的空白地块为ff
         * 3 粘贴建筑,x清除,c复制建筑信息,v粘贴建筑信息 a输入地名id b输入建筑id
         */
        {
            if (Gdx.input.isKeyJustPressed(Input.Keys.INSERT)) {
                String  tempIntPut= JOptionPane.showInputDialog("请输入要跳转到的坐标");
                if(ComUtil.isNumeric(tempIntPut)){
                    moveToHexagonPotion(Integer.parseInt(tempIntPut));
                }else{
                    JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                }
            }



            //快捷键
            switch (keyMode){
                case 0:
                    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon.getBackRefXValue()==0){
                            mapHexagon.setBackRefX(101);
                        }else if(mapHexagon.getBackRefXValue()+1==0){
                            return;
                        }
                        mapHexagon.setBackRefX(mapHexagon.getBackRefXValue()+1);
                        mx=1;
                    }else
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon.getBackRefXValue()==0){
                            mapHexagon.setBackRefX(101);
                        }else if(mapHexagon.getBackRefXValue()-1==0){
                            return;
                        }
                        mapHexagon.setBackRefX(mapHexagon.getBackRefXValue()-1);
                        mx=1;
                    }else
                    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon.getBackRefYValue()==0){
                            mapHexagon.setBackRefY(101);
                        }else if(mapHexagon.getBackRefYValue()-1==0){
                            return;
                        }
                        mapHexagon.setBackRefY(mapHexagon.getBackRefYValue()-1);
                        mx=1;
                    }else
                    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon.getBackRefYValue()==0){
                            mapHexagon.setBackRefY(101);
                        }else if(mapHexagon.getBackRefYValue()+1==0){
                            return;
                        }
                        mapHexagon.setBackRefY(mapHexagon.getBackRefYValue()+1);
                        mx=1;
                    }else if(mode==0){
                        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                            tempAreaId= JOptionPane.showInputDialog("请输入backTile");
                            if(tempAreaId==null){return;}
                            if(ComUtil.isNumeric(tempAreaId)){
                                int backTile=Integer.parseInt(tempAreaId);
                                tempAreaId= JOptionPane.showInputDialog("请输入backId");
                                if(ComUtil.isNumeric(tempAreaId)){
                                    int backId=Integer.parseInt(tempAreaId);
                                    XmlReader.Element  x=    game.gameConfig.getDEF_TERRAINIMG_XMLE(backTile,backId);
                                    if(x!=null){
                                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                                        mapHexagon.setBackRefX(0);
                                        mapHexagon.setBackRefY(0);
                                        mapHexagon.setBackTile(backTile);
                                        mapHexagon.setBackIdx(backId);
                                        mapHexagon.setBlockType(x.getInt("type"));
                                    }else{
                                        JOptionPane.showMessageDialog(null, "fail", "请输入有效内容", JOptionPane.ERROR_MESSAGE);
                                    }

                                }else{
                                    JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                            }
                        }else
                        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                            mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                            mapHexagon.setBackRefX(0);
                            mapHexagon.setBackRefY(0);
                            mapHexagon.setBackTile(0);
                            mapHexagon.setBackIdx(0);
                            mx=1;
                        }else
                        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                            tempKeyBlockType=mapBinDAO.getMapbin().get(coord.getId()).getBlockType();
                            tempKeyTile=mapBinDAO.getMapbin().get(coord.getId()).getBackTile();
                            tempKeyIdx=mapBinDAO.getMapbin().get(coord.getId()).getBackIdx();
                            Gdx.app.log("C","BlockType:"+tempKeyBlockType+" Tile:"+tempKeyTile+ " Id:"+tempKeyIdx);

                            mx=1;
                        }else
                        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                            //mapBinDAO.getMapbin().get(coord.getid()).setBlockType(tempKeyBlockType);
                            //mapBinDAO.getMapbin().get(coord.getid()).setBackTile(tempKeyTile);
                            //mapBinDAO.getMapbin().get(coord.getid()).setBackIdx(tempKeyIdx);
                            for(Coord coord:coords) {
                                Gdx.app.log("V","BlockType:"+tempKeyBlockType+" Tile:"+tempKeyTile+ " Id:"+tempKeyIdx);
                                mapBinDAO.getMapbin().get(coord.getId()).setBlockType(tempKeyBlockType);
                                mapBinDAO.getMapbin().get(coord.getId()).setBackTile(tempKeyTile);
                                mapBinDAO.getMapbin().get(coord.getId()).setBackIdx(tempKeyIdx);
                            }
                            mapBinDAO.autoCoast(coord.getId(),true);
                            mx=1;
                        }
                    }else if(mode==1){ //river 河流,fore为11的类型,行动遇到后终止操作.
                        //c copy v  x clear z create
                        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                            mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                            mapHexagon.setBackTile(11);
                            mapHexagon.setBackIdx(0);
                            mapHexagon.setBackRefX(0);
                            mapHexagon.setBackRefY(0);
                            mapBinDAO.autoRiver( coord.getId(),true );
                            mx=1;
                        }else if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                            mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                            tempKeyTile= mapHexagon.getBackTile();
                            tempKeyIdx= mapHexagon.getBackIdx();

                        }else if(Gdx.input.isKeyJustPressed(Input.Keys.V)){
                            mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                            mapHexagon.setBackTile(tempKeyTile);
                            mapHexagon.setBackIdx(tempKeyIdx);
                            mapHexagon.setBackRefX(0);
                            mapHexagon.setBackRefY(0);
                            if(mapHexagon.getBlockType()!=1){
                                mapHexagon.setBlockType(0);
                                mapHexagon.setForeTile(0);
                                mapHexagon.setForeIdx(0);
                                mapHexagon.setForeRefX(0);
                                mapHexagon.setForeRefY(0);
                            }
                            mapBinDAO.autoRiver( coord.getId(),true );
                            mx=1;
                        }if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                            mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                            mapHexagon.setBackRefX(0);
                            mapHexagon.setBackRefY(0);
                            mapHexagon.setBackTile(0);
                            mapHexagon.setBackIdx(0);
                            mapBinDAO.autoRiver( coord.getId(),true );
                            mx=1;
                        }
                    }

                    break;

                case 1:


                    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon.getForeRefXValue()==0){
                            mapHexagon.setForeRefX(101);
                        }else if(mapHexagon.getForeRefXValue()+1==0){
                            return;
                        }
                        mapHexagon.setForeRefX(mapHexagon.getForeRefXValue()+1);
                        mx=1;
                    }else
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon.getForeRefXValue()==0){
                            mapHexagon.setForeRefX(101);
                        }else if(mapHexagon.getForeRefXValue()-1==0){
                            return;
                        }
                        mapHexagon.setForeRefX(mapHexagon.getForeRefXValue()-1);
                        mx=1;
                    }else
                    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon.getForeRefYValue()==0){
                            mapHexagon.setForeRefY(101);
                        }else if(mapHexagon.getForeRefYValue()-1==0){
                            return;
                        }
                        mapHexagon.setForeRefY(mapHexagon.getForeRefYValue()-1);
                        mx=1;
                    }else
                    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon.getForeRefYValue()==0){
                            mapHexagon.setForeRefY(101);
                        }else if(mapHexagon.getForeRefYValue()+1==0){
                            return;
                        }
                        mapHexagon.setForeRefY(mapHexagon.getForeRefYValue()+1);
                        mx=1;
                    }



                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        mapBinDAO.getMapbin().get(coord.getId()).setForeRefX(0);
                        mapBinDAO.getMapbin().get(coord.getId()).setForeRefY(0);
                        mapBinDAO.getMapbin().get(coord.getId()).setForeTile(0);
                        mapBinDAO.getMapbin().get(coord.getId()).setForeIdx(0);
                        mx=1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                        tempKeyTile=mapBinDAO.getMapbin().get(coord.getId()).getForeTile();
                        tempKeyIdx=mapBinDAO.getMapbin().get(coord.getId()).getForeIdx();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                        //mapBinDAO.getMapbin().get(coord.getid()).setForeTile(tempKeyTile);
                        //mapBinDAO.getMapbin().get(coord.getid()).setForeIdx(tempKeyIdx);
                        for(Coord coord:coords) {
                            mapBinDAO.getMapbin().get(coord.getId()).setForeTile(tempKeyTile);
                            mapBinDAO.getMapbin().get(coord.getId()).setForeIdx(tempKeyIdx);
                        }
                        mx=1;
                    }
                    break;

                case 2://x addNewRegion
                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon ==null){return;}
                        int r= mapBinDAO.getMapbin().get(coord.getId()).getRegionId();
                        if(ifHistory&&historyDAO!=null&&coord.getId()!=r){
                            historyDAO.addNewRegion(coord.getId(),r);
                        }
                        mapBinDAO.getMapbin().get(coord.getId()).setRegionId(coord.getId());
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon ==null){return;}
                        tempRegionId = mapBinDAO.getMapbin().get(coord.getId()).getRegionId();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        mapBinDAO.resetCoastBorder(coord.getId());
                        if(mapHexagon ==null){return;}
                        int r;
                        for (Coord coord : coords) {
                            r= mapBinDAO.getMapbin().get(coord.getId()).getRegionId();
                            //如果有加载的历史,且 覆盖的是一个region,则查找并清除该region的信息
                            if(ifHistory&&historyDAO!=null&&coord.getId()==r){
                                historyDAO.removeRegion(r);
                            }
                            mapBinDAO.getMapbin().get(coord.getId()).setRegionId(tempRegionId);
                            mapBinDAO.resetCoastBorder(coord.getId());
                        }
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon ==null){return;}
                        //不能修改有地名的地块
                        if(mapBinDAO.getMapbin().get(coords.get(0).getId()).getRegionId()!=-1){
                            if(ifHistory&&historyDAO!=null){
                                historyDAO.replaceRegion( mapBinDAO.getMapbin().get(coords.get(0).getId()).getRegionId()  ,coords.get(0).getId());
                            }
                            mapBinDAO.replaceRegionIdById(coords.get(0).getId());
                            mx = 1;
                        }else{
                            Gdx.app.log("修改地块","tempRegionId:"+tempRegionId+" areaId:"+mapBinDAO.getMapbin().get(tempRegionId).getregionAreaId());
                        }
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon ==null){return;}
                        //saveMapBin();
                        if(ifHistory&&historyDAO!=null){
                            // historyDAO.replaceRegion( mapBinDAO.getMapbin().get(coords.get(0).getId()).getRegionId(),coords.get(0).getId());
                            mapBinDAO.replaceRegionIdForFFArea(historyDAO,coords.get(0).getId(),tempRegionId);
                        }else{
                            mapBinDAO.replaceRegionIdForFFArea(null,coords.get(0).getId(),tempRegionId);
                        }
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                        mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
                        if(mapHexagon ==null){return;}
                        if(mapHexagon.getPresetRailway()==0){
                            mapHexagon.setPresetRailway(1);
                        }else {
                            mapHexagon.setPresetRailway(0);
                        }
                        mx = 1;
                    }
                    break;
                case 3:
                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        mapBinDAO.getMapbin().get(coord.getId()).setRegionAreaId(0);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                        tempCopyValue = mapBinDAO.getRegionMapbinId(coord.getId()).getregionAreaId();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                        mapBinDAO.getRegionMapbinId(coord.getId()).setRegionAreaId(tempCopyValue);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        tempInputValue= JOptionPane.showInputDialog("请输入areaId:");
                        if(tempInputValue==null){return;}
                        if(ComUtil.isNumeric(tempInputValue)){
                            mapBinDAO.getRegionMapbinId(coord.getId()).setRegionAreaId(Integer.parseInt(tempInputValue));
                        }else{
                            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    break;
                case 4:
                    //L
                    if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {//加载stage
                        tempStageId= JOptionPane.showInputDialog("请输入加载的stageId:");
                        if(ComUtil.isNumeric(tempStageId)){
                            defStage = game.gameConfig.getDEF_STAGE().getElementById(Integer.parseInt(tempStageId));
                            if(defStage!=null){
                               /* stageDAO=gameFramework.getBtlDaoByStageId(defStage.getInt("id"));
                                if(defStage!=null&&stageDAO.getBm0().getBm0_10()==mapId){
                                    ifStage=true;
                                    Gdx.app.log("加载stage成功",defStage.get("name"));
                                    stageEdit=new StageDAOEdit(stageDAO,mapBinDAO);
                                }else{
                                    Gdx.app.log("加载stage失败",stageDAO.getBm0().getBm0_10()+":"+mapId);
                                    ifStage=false;
                                }*/
                            }else{
                                ifStage=false;
                                JOptionPane.showMessageDialog(null, "fail", "加载stage失败", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            ifStage=false;
                            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    /*if(ifStage){
                        //I initCity
                        //U updBuildLegion
                        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                            tempId= JOptionPane.showInputDialog("请输入初始化所有城市后默认归属的国家id");
                            if(ComUtil.isNumeric(tempId)){
                                stageEdit.batchAddCity(Integer.parseInt(tempId));
                            }else{
                                JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        if(Gdx.input.isKeyJustPressed(Input.Keys.U)){
                            tempId= JOptionPane.showInputDialog("请输入该区块要归属的国家index");
                            if(ComUtil.isNumeric(tempId)){
                                stageEdit.updBuildLegion(mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));
                            }else{
                                JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                            tempRegionId = mapBinDAO.getMapbin().get(coord.getId()).getRegionId();
                        }
                        if(Gdx.input.isKeyJustPressed(Input.Keys.V)){
                            stageEdit.updBuildLegionByRegion(mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),tempRegionId);
                        }
                    }*/



                    break;
                case 5://load history

                    if(ifHistory){

                        //B backYear
                        //N nextYear

                        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
                            lastHistoryPage();
                            game.mapDetailScreen.setEditMode(5);
                        }
                        if(Gdx.input.isKeyJustPressed(Input.Keys.N)){
                            nextHistoryPage();
                            game.mapDetailScreen.setEditMode(5);
                        }
                        if(mode==0){

                            //Y addNewYear
                            //C copy
                            //V paste
                            //U addCountry
                            //R replaceCountry
                            if(Gdx.input.isKeyJustPressed(Input.Keys.Y)){
                                //historyEdit.addNewYear();
                                historyDAO.addNewYear();
                                historyPageMax=historyPageMax+1;
                            }
                            if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                                if(mapBinDAO.getMapbin().get(coord.getId()).getRegionId()!=-1){
                                    tempCountryId = historyDAO.getHistoryDataByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId()).getNowCountryIndex();  //mapBinDAO.getMapbin().get(coord.getid()).getPolitical();
                                }
                            }
                            if(Gdx.input.isKeyJustPressed(Input.Keys.V)){
                                //historyEdit.updHistoryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),tempCountryId);
                                // historyDAO.updHistoryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),tempCountryId);
                                if(coords!=null&&coords.size()>1){
                                    for(Coord coord:coords){
                                        historyDAO.addCountryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),tempCountryId);
                                    }
                                }else{
                                    historyDAO.addCountryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),tempCountryId);
                                }

                            }
                            if(Gdx.input.isKeyJustPressed(Input.Keys.U)){
                                tempId= JOptionPane.showInputDialog("请输入该区块要归属的国家id");
                                if(ComUtil.isNumeric(tempId)){//targetPage,int targetRegionId,int countryId
                                    //historyEdit.addCountryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));
                                    historyDAO.addCountryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));


                                }else{
                                    JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
                                tempId= JOptionPane.showInputDialog("请输入该区块所属国家要替换的国家id");
                                if(ComUtil.isNumeric(tempId)){
                                    //historyEdit.addCountryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getid()).getPolitical(),Integer.parseInt(tempId));
                                    //historyEdit.replaceCountryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));
                                    historyDAO.replaceCountryByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));

                                }else{
                                    JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else if(mode==1){
                            //c
                            //v
                            //u
                            //r 复制battleId
                            //t 粘贴battleId
                            //i 输入battle
                          /*  if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                                if(mapBinDAO.getMapbin().get(coord.getid()).getPolitical()!=-1){
                                    tempGetType = historyDAO.getBm1().get(historyEdit.getHistoryIndex(historyPage,mapBinDAO.getMapbin().get(coord.getid()).getPolitical())).getBm1_6();  //mapBinDAO.getMapbin().get(coord.getid()).getPolitical();
                                }
                            }
                            if(Gdx.input.isKeyJustPressed(Input.Keys.V)){
                                historyEdit.updGetTypeByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getid()).getPolitical(),tempGetType);
                            }*/
                            if(Gdx.input.isKeyJustPressed(Input.Keys.U)){
                                tempId= JOptionPane.showInputDialog("请输入该区块要修改的type");
                                if(ComUtil.isNumeric(tempId)){//targetPage,int targetRegionId,int countryId
                                    //historyEdit.addTypeByRegionAndPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));

                                    historyDAO.addTypeByRegionAndPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));

                                }else{
                                    JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                                if(mapBinDAO.getMapbin().get(coord.getId()).getRegionId()!=-1){
                                    tempGetType =   tempCountryId = historyDAO.getHistoryData(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId()).getGetType();  //mapBinDAO.getMapbin().get(coord.getid()).getPolitical();
                                    tempBattleId =   tempCountryId = historyDAO.getHistoryData(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId()).getBattleId();  //mapBinDAO.getMapbin().get(coord.getid()).getPolitical();
                                }
                            }
                            if(Gdx.input.isKeyJustPressed(Input.Keys.V)){
                                historyDAO.updGetTypeByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),tempGetType);
                                historyDAO.updBattleByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),tempBattleId);
                            }
                            if(Gdx.input.isKeyJustPressed(Input.Keys.I)){
                                tempId= JOptionPane.showInputDialog("请输入该区块的battle");
                                if(ComUtil.isNumeric(tempId)){//targetPage,int targetRegionId,int countryId
                                    historyDAO.addBattleByRegionByPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));
                                }else{
                                    JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                    break;

                case 6:
                    if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
                        tempId= JOptionPane.showInputDialog("请输入该区块要修改的气候类型");
                        if(ComUtil.isNumeric(tempId)){//targetPage,int targetRegionId,int countryId
                            //historyEdit.addTypeByRegionAndPage(historyPage,mapBinDAO.getMapbin().get(coord.getId()).getRegionId(),Integer.parseInt(tempId));
                            mapBinDAO.setRegionZone(coord.id,Integer.parseInt(tempId));
                            // mapBinDAO.mapBins.get(coord.getId()).setZone(Integer.parseInt(tempId));
                        }else{
                            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                        if(mapBinDAO.getMapbin().get(coord.getId()).getRegionId()!=-1){
                            tempCopyValue =  mapBinDAO.getMapbin().get(coord.getId()).getRegionClimatZone();  //mapBinDAO.getMapbin().get(coord.getid()).getPolitical();
                        }
                    }
                    if(Gdx.input.isKeyJustPressed(Input.Keys.V)){
                        mapBinDAO.setRegionZone(coord.id,tempCopyValue);
                    }
                    break;

                case 7://1 depLv
                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        mapBinDAO.getMapbin().get(coord.getId()).setRegionDepLv(0);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                        tempCopyValue = mapBinDAO.getRegionMapbinId(coord.getId()).getRegionDepLv();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                        mapBinDAO.getRegionMapbinId(coord.getId()).setRegionDepLv(tempCopyValue);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        tempInputValue= JOptionPane.showInputDialog("请输入发展等级:");
                        if(tempInputValue==null){return;}
                        if(ComUtil.isNumeric(tempInputValue)){
                            mapBinDAO.getRegionMapbinId(coord.getId()).setRegionDepLv(Integer.parseInt(tempInputValue));
                        }else{
                            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                        }
                    }


                    break;

                case 8://2 mineralLv
                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        mapBinDAO.getMapbin().get(coord.getId()).setRegionMineralLv(0);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                        tempCopyValue=    mapBinDAO.getRegionMapbinId(coord.getId()).getRegionMineralLv();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                        mapBinDAO.getRegionMapbinId(coord.getId()).setRegionMineralLv(tempCopyValue);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        tempInputValue= JOptionPane.showInputDialog("请输入矿产等级:");
                        if(tempInputValue==null){return;}
                        if(ComUtil.isNumeric(tempInputValue)){
                            mapBinDAO.getRegionMapbinId(coord.getId()).setRegionMineralLv(Integer.parseInt(tempInputValue));
                        }else{
                            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                        }
                    }


                    break;

                case 9://3 oilLv
                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        mapBinDAO.getMapbin().get(coord.getId()).setRegionOilLv(0);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                        tempCopyValue=   mapBinDAO.getRegionMapbinId(coord.getId()).getRegionOilLv();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                        mapBinDAO.getRegionMapbinId(coord.getId()).setRegionOilLv(tempCopyValue);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        tempInputValue= JOptionPane.showInputDialog("请输入石油等级:");
                        if(tempInputValue==null){return;}
                        if(ComUtil.isNumeric(tempInputValue)){
                            mapBinDAO.getRegionMapbinId(coord.getId()).setRegionOilLv(Integer.parseInt(tempInputValue));
                        }else{
                            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                        }
                    }


                    break;

                case 10://3 oilLv
                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        mapBinDAO.setRegionSR(coord.getId(),0);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                        tempCopyValue=   mapBinDAO.getRegionMapbinId(coord.getId()).getRegionStrategicRegion();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                        mapBinDAO.setRegionSR(coord.getId(),tempCopyValue);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        tempInputValue= JOptionPane.showInputDialog("请输入SR序号:");
                        if(tempInputValue==null){return;}
                        if(ComUtil.isNumeric(tempInputValue)){
                            mapBinDAO.setRegionSR(coord.getId(),Integer.parseInt(tempInputValue));
                        }else{
                            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                        }
                    }


                    break;

                case 11://3 foodLv
                    if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                        mapBinDAO.getMapbin().get(coord.getId()).setRegionFoodLv(0);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                        tempCopyValue=   mapBinDAO.getRegionMapbinId(coord.getId()).getRegionFoodLv();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                        mapBinDAO.getRegionMapbinId(coord.getId()).setRegionFoodLv(tempCopyValue);
                        mx = 1;
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        tempInputValue= JOptionPane.showInputDialog("请输入食物等级(0~15):");
                        if(tempInputValue==null){return;}
                        if(ComUtil.isNumeric(tempInputValue)){
                            mapBinDAO.getRegionMapbinId(coord.getId()).setRegionFoodLv(Integer.parseInt(tempInputValue));
                        }else{
                            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
                        }
                    }


                    break;
            }
        }
    }

    public void setCoord(int hexagon){
        coords.clear();
        int cell_x=-GameMap.getHX(hexagon,getW());
        int cell_y=GameMap.getHY(hexagon,getW())+1;
        float   vertex_x_px = cell_x * Map.GRID_WIDTH * Map.MAP_SCALE;
        float   vertex_y_px = cell_x % 2 != 0 ? (cell_y + 1) * Map.HEXAGON_HEIGHT * Map.MAP_SCALE : (cell_y + 0.5f) * Map.HEXAGON_HEIGHT * Map.MAP_SCALE;

        coord = new Coord(-cell_x, cell_y - 1, vertex_x_px, vertex_y_px);
        coordInit(coord);
        coords.add(coord);
    }

    public void coordInit(Coord coord) {
        coord.setVertexY(getMapYByImgY(coord.vertexY));
        coord.setId(w * coord.getY() + coord.getX());
    }
    public void coordsInit(List<Coord> coords) {
        for(Coord coord:coords) {
            coord.setVertexY(getMapYByImgY(coord.vertexY));
            coord.setId(w * coord.getY() + coord.getX());
        }
    }
    public int getHistoryPage() {
        return historyPage;
    }
    public void nextHistoryPage(){
        if(historyPage<historyPageMax-1){
            historyPage=historyPage+1;
        }
    }
    public  void lastHistoryPage(){
        if(historyPage<1){
            historyPage=0;
        }else{
            historyPage=historyPage-1;
        }
    }


    public int getHistoryBeginYear() {
        return historyBeginYear;
    }

    public int getMode() {
        return mode;
    }
    public void setMode(int mode){
        this.mode=mode;
    }

    //输出图片
    public void writePNG(){
        pixmap = GameMap.createPixmap(defMap.getInt("width"),defMap.getInt("height"),1f);;
        pixmap=GameMap.coverImgByPtimgId(game,pixmap, 1);
        gameFramework.getPixmapDecorateByDao(pixmap, mapBinDAO,game.getAssetManager(), 1.0f, false);
        GameMap.getPixmapByDaoForAreaColor(mapBinDAO,pixmap,1.0f,mapBinDAO.getCoastGrid());

        PixmapIO.writePNG(Gdx.files.external(defMap.getName()+".png"), pixmap);
        pixmap.dispose();
    }

    public void savePreview() {
        if(!game.gameConfig.isExtAvailable){
            return;
        }
        XmlReader.Element root = game.gameConfig.getDEF_MAP().getElementById(mapId);
        int childNum = root.getChildCount();
        String str = root.get("name");
        String path= ResDefaultConfig.Path.ViewFolderPath + str + ".png";
        GameMap.drawPixmapForPriviewMap(getMapBinDAO(),path);
    }




    public void loadHostory() {
        // tempId= JOptionPane.showInputDialog("请输入加载的historyId:");
        if(ComUtil.isNumeric(tempId)){
            historyId=Integer.parseInt(tempId);
            defHistory=game.gameConfig.getDEF_HISTORY().getElementById(historyId);
            if(defHistory!=null){
                //historyDAO=gameFramework.getBinDaoById(defHistory.get("name"),ResConfig.Rules.RULE_FB2_HISTORY);

                historyDAO=gameFramework.getHistory(historyId);
                if(defHistory!=null&&historyDAO.masterData.getMapId()==mapId){
                    ifHistory=true;
                    //Gdx.app.log("加载history成功",defHistory.get("name"));
                    historyPage=0;
                    historyBeginYear=historyDAO.masterData.getYearBegin();
                    historyPageMax=historyDAO.masterData.getHisCount()/historyDAO.masterData.getRegionCount();
                    //historyEdit=new HistoryDAOEdit(game,historyDAO,mapBinDAO);
                    game.mapDetailScreen.setEditMode(5);
                    JOptionPane.showMessageDialog(null, "successful", "加载history成功", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    // Gdx.app.log("加载history失败",historyDAO.getBm0().getBm0_2()+":"+mapId);
                    JOptionPane.showMessageDialog(null, "fail", "加载history失败", JOptionPane.ERROR_MESSAGE);
                    ifHistory=false;
                }
            }else{
                historyId=-1;
                ifHistory=false;
                JOptionPane.showMessageDialog(null, "fail", "加载history失败", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            ifHistory=false;
            JOptionPane.showMessageDialog(null, "fail", "请输入数字", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void parseCommand(String text) {
        final String[] rs=text.split(" ");
        String type=rs[0];

        switch (type){
            case "loadHistory":
                if(ComUtil.isNumeric(rs[1])){
                    tempId=rs[1];
                    loadHostory();
                }
                break;
            case "logCountryRegion":
                if(ComUtil.isNumeric(rs[1])){
                    logCountryAllRegion(Integer.parseInt(rs[1]));
                }
                break;
            case "loadBgImg":
                if(!ComUtil.isEmpty(rs[1])){
                    loadBgMapImage(rs[1]);
                }else{
                    if(bgT !=null){
                        bgT.dispose();
                        bgT =null;
                    }
                }
                break;

            case "resetMap":
                if(!ComUtil.isEmpty(rs[1])){
                    String[] ws=rs[1].split(",");
                    if(ComUtil.isNumeric(ws[0])&&ComUtil.isNumeric(ws[1])&&ComUtil.isNumeric(ws[2])){
                        mapBinDAO.resetMap(Integer.parseInt(ws[0]),Integer.parseInt(ws[1]),Integer.parseInt(ws[2]));
                    }
                }
                break;
        }
    }


    //加载背景图片
    public void loadBgMapImage(String imgName) {
        FileHandle f= Gdx.files.local(ResDefaultConfig.Path.MapFolderPath+imgName+".png");
        if(f.exists()){
            bgT =new Texture(f);
            // Gdx.app.log("loadBgMapImage","ok");
            bgS = new Sprite(bgT);
            bgS.setAlpha(0.5f);
            updLoadMPotion();
            return;
        }
        f= Gdx.files.local(ResDefaultConfig.Path.MapFolderPath+imgName+".jpg");
        if(f.exists()){
            bgT =new Texture(f);
            // Gdx.app.log("loadBgMapImage","ok");
            bgS = new Sprite(bgT);
            bgS.setAlpha(0.5f);
            updLoadMPotion();
            return;
        }
    }

    private void updLoadMPotion() {
        //loadMapS.setAlpha(0.5f);
        //loadMapS.setRegionWidth((int) xMax);
        //loadMapS.setRegionHeight((int) yMax);
        //int mapYMax=(int) ((this.mapH_px * Map.MAP_SCALE * zoom)+ (1 - zoom) * this.mapH_px * Map.MAP_SCALE);
        //this.xMax = (this.mapW_px * Map.MAP_SCALE * zoom - vw / zoom + (1 - zoom) * this.mapW_px * Map.MAP_SCALE);
        //this.yMax = (this.mapH_px * Map.MAP_SCALE * zoom - vh / zoom + (1 - zoom) * this.mapH_px * Map.MAP_SCALE);
        int w_px= (int) ( getWidth()* zoom);
        int h_px= (int) ( getHeight()*  zoom);
        // int h_px=;
        // Gdx.app.log("", "mapYMax:"+mapYMax+" w:"+w+" h:"+h+" w_px:"+w_px);
        bgS.setSize(w_px, h_px);
        bgS.setX(x*zoom);
        bgS.setY(y*zoom-35*zoom);
    }


    //同步history的数据 通过mapbinDAO
    public void synchronousHistoryRegionByMapbinDAO(){
        Fb2Map mapBinDAO=getMapBinDAO();
        Fb2History oldH=historyDAO;


        Fb2History newH=new Fb2History();
        int regionCount=mapBinDAO.getRegionCount();
        int yearCount=oldH.masterData.getYearCount();
        int beginYear=oldH.masterData.getYearBegin();
        newH.setMasterData( oldH.masterData);
        newH.masterData.setRegionCount(regionCount);
        newH.masterData.setHisCount(yearCount*regionCount);
        int historyIndex=0, yearIndex, regionIndex, nowCountryIndex, lastCountryIndex, getType, battleId,r;

        for( yearIndex=0;yearIndex<yearCount;yearIndex++){
            for(r=0;r<regionCount;r++){
                regionIndex=mapBinDAO.regionDatas.getByIndex(r).getRegion();
                Fb2History.HistoryData th=oldH.getHistoryDataByPage(yearIndex,regionIndex);
                if(th==null){//如果获得不了该数据,则获取父类数据,给默认值...
                    newH.addHistoryData(historyIndex, yearIndex+beginYear, regionIndex,0,0,0,0);
                }else{
                    newH.addHistoryData(historyIndex, yearIndex+beginYear, regionIndex, th.getNowCountryIndex(), th.getLastCountryIndex(), th.getGetType(), th.getBattleId());
                }
                historyIndex++;
            }
        }
        historyDAO=newH;

        Gdx.app.log("synchronousHistoryRegionByMapbinDAO", "mapBinDAORegionCount:"+mapBinDAO.getRegionCount()+" regionCount:"+historyDAO.masterData.getRegionCount()+" yearCount:"+historyDAO.masterData.getYearCount()+" hisCount:"+historyDAO.masterData.getHisCount());
        //int s=0;
    }


    public void moveToBigRegionPotion(){
        if(coord==null){
            coord = GameMap.getHotCell2(0 / getZoom(), getImgY(0 / getZoom()));
            coordInit(coord);
            coord.setId(0);
        }
        int id=mapBinDAO.getEmptyRegion(coord.id);
        //   int id=mapBinDAO.getBigRegionId(coord.id);
        //int id=mapBinDAO.getSmallRegionId(coord.id);
        if(id>=0){

            Gdx.app.log("BigRegion",id+"");
            moveToHexagonPotion(  mapBinDAO.getRegionById(id));
        }


    }



    public void moveToHexagonPotion(int hexagon){


        if(!mapBinDAO.ifGridIsPass(hexagon)){
            return;
        }
        setCoord(hexagon);


        int    gx = (hexagon % mapBinDAO.getMapWidth()) + 1;
        int   gy =   (hexagon / mapBinDAO.getMapWidth()) + 1;
        float    dx_px = -getImgX() * zoom + ((int) ((getImgX() * this.zoom) / (Map.GRID_WIDTH * Map.MAP_SCALE * this.zoom)) - 1) * (Map.GRID_WIDTH * Map.MAP_SCALE * this.zoom) + (((gx - 1) * Map.GRID_WIDTH *zoom* Map.MAP_SCALE));
        float   dy_px = -(vh % (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) + ((vh) / zoom) * (zoom - 1) + getImgY() * zoom + vh / zoom
                - ((((int) ((getImgY() * zoom) / (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) - 1) * (Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) + (((gy - 1) * Map.HEXAGON_HEIGHT + ((gx & 1) == 1 ? 0 : ((ul_parity_x & 1) == 1 ? Map.HEXAGON_HEIGHT_REF : -Map.HEXAGON_HEIGHT_REF))) * zoom * Map.MAP_SCALE) + ((ul_parity_x & 1) == 1 ? -Map.HEXAGON_HEIGHT_REF : 0) * Map.MAP_SCALE * zoom);

        setX(vw*zoom/2-dx_px);
        setY(vh*zoom/2-(yMax+dy_px));
    }

    //临时方法
    public void tempMethod() {
        // tempMethod_updHistoryDataByOldData();
        //  mapBinDAO.autoSetClimatZoneByLat();
        // gameFramework.writeMapByMapId(game.getMapId(),game.getAssetManager(),game.getMapScale());
        //  mapBinDAO.resetRegionAreaInfo();
        //mapBinDAO.temp_logAreaName();
        //mapBinDAO.saveNewFormat();
        //    mapBinDAO.tempExchangeLv();
        //  mapBinDAO.updRegionLvByArea();
        //   mapBinDAO.zoomMap();

        // Fb2Map nM=new Fb2Map(game,mapId,Gdx.files.local("bin/world_map2_f.bin").readBytes());

        //  mapBinDAO.tempMethod(nM);
        mapBinDAO.fixRiver();


        Gdx.app.log("tempMethod is ok",mapId+"");
    }


    /*//临时方法,对接旧数据以实现region的转移
    public void tempMethod_updHistoryDataByOldData(){
        MapBinDAO m1=gameFramework.getMapDaoByPath("world_map_o");
        Fb2History h1= gameFramework.getHistoryByPath("word_his_o");
        historyDAO.updHistoryData(h1,m1,mapBinDAO);
    }*/



    public void checkRegion() {
        mapBinDAO.checkRegion(defMap.getBoolean("ifLoop",false));
    }

    public void logCountryAllRegion(int country) {
        if(historyDAO==null){
            return;
        }
        int nowYear=getHistoryPage()+getHistoryBeginYear();
        IntArray a=game.tempUtil.getTempIntArray();
        for(Fb2History.HistoryData h:historyDAO.historyDatas){
            if(h.getYear()==nowYear&&h.getNowCountryIndex()==country &&!a.contains(h.getRegionIndex())){
                a.add(h.getRegionIndex());
            }
        }
        Gdx.app.log(nowYear+":"+country,a.toString());
        game.tempUtil.disposeTempIntArray(a);

    }


    public void copyBackTitle(){
        if(coord!=null){
            mapHexagon =mapBinDAO.getMapbin().get(coord.getId());
            if(mapHexagon!=null){
                tempKeyTile= mapHexagon.getBackTile();
                tempKeyIdx= mapHexagon.getBackIdx();
            }
        }
    }

}