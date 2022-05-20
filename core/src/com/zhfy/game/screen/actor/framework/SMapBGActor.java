package com.zhfy.game.screen.actor.framework;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.IntArray;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.Coord;
import com.zhfy.game.model.framework.SpriteDAO;
import com.zhfy.game.model.framework.SpriteListDAO;

public class SMapBGActor extends BaseActor {
    //用来显示底层的特效



    ArrayList<SpriteDAO> mainSprites;


   //private ShapeRenderer shapeRenderer;
    public CamerDAO cam;
    public int showType;//0颜色图 1阵营图

    private int w;
    private int h;
    private int i,iMax;


    private float x;
    private float y;
    private float zoom;
    private int mapW_px;
    private int mapH_px;

    private float vx;
    private float vy;
    private int refY;
    private boolean ifHaveMapImage;
    private boolean ifHavePtImage;

    private AssetManager am;
    private int screenId;
    private boolean ifNeedUnload;//是否需要卸载,在资源加载完后卸载,如果未加载完退出,在退出时顺带清理资源


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

    private Map<String,Integer> tempMap;




    // 颜色层
    private SpriteDAO spriteGrid;
    private SpriteListDAO spriteGrids;
    private int gw;
    private int gh;
    private MainGame game;
    private Fb2Smap smapDao;
    private Fb2Map mapDao;
    private Coord coord;
    private IntArray tempIds;

    //计时器
  //  private float actDeltaSum;
   // private boolean actDeltaSumD;
    private float scale;
    private int potionHexagon;
    private int potionRange;

    private float time;
    private float timeMax;
    private Pixmap pixmap;//test
    private Texture texture;

    public SMapBGActor(MainGame game, CamerDAO cam, int screenId, Fb2Smap smapDao,Fb2Map mapDao, float scale, int mapW_px, int mapH_px, int refY,Coord coord) {
        super();
        init( game,  cam,   screenId,  smapDao,mapDao,  scale,  mapW_px,  mapH_px,  refY,coord);
    }

    public void init(MainGame game, CamerDAO cam, int screenId, Fb2Smap smapDao, Fb2Map mapDao,float scale, int mapW_px, int mapH_px, int refY,Coord coord){
        this.smapDao=smapDao;
        this.mapDao=mapDao;
        this.scale=scale;
        this.game=game;
        this.am=game.getAssetManager();
        ifNeedUnload=false;
        this.screenId=screenId;
        //drawMarkList =new DrawListDAO();
        this.x=0;
        this.y=0;
        this.zoom=1f;
        this.showType =0;
        this.coord=coord;
        //this.loopState =loopState;
        /*{
            // 绘制主要图
            textureMain=GameMap.mergeGameMapByZip(game,game.getMapId());
            spriteMain= new Sprite(textureMain);
        }*/

        this.w = smapDao.masterData.getWidth();
        this.h = smapDao.masterData.getHeight();
        this.mapW_px=mapW_px;
        this.mapH_px=mapH_px;
        this.refY=refY;
        ;
        this.cam = cam;
        //this.shapeRenderer=shapeRenderer;
        //mainSprites =GameMap.getGameMapSprites(game,mapW_px,mapH_px, mainSprites,true,refY);
        mainSprites=GameMap.getPTMapSprites(game,  game.getSMapDAO().masterData.getMapLandBase(), mapW_px,mapH_px, mainSprites,true,refY);
        cam.setMapSprites(mainSprites);
        setSize(mapW_px, mapH_px);

        timeMax=5;
       /* pixmap=new Pixmap(1250, 1250, Pixmap.Format.RGBA8888);
        texture = new Texture(pixmap);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(Color.RED);*/
        ifHaveMapImage=mapDao!=null&&mapDao.mapImage!=null;
        ifHavePtImage=mapDao!=null&&mapDao.floorImage!=null;
    }


    @Override
    public void act(float delta) {
        super.act(delta);

        /*if(ifFlashClickRegion){
            if(actDeltaSumD){
                actDeltaSum+=delta;
                if(actDeltaSum>1){
                    actDeltaSumD=false;
                }
            }else{
                actDeltaSum-=delta;
                if(actDeltaSum<0){
                    actDeltaSumD=true;
                }
            }
        }*/
        time=time+delta;
        if(time>timeMax){
            time=0;
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (/*spriteMain == null ||*/ !isVisible()) {
            return;
        }
        {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //float actDeltaSum=((SMapGameStage) getStage()).alphaFlash;
            //底图
           // batch.flush();
            if(ifHaveMapImage&&(!game.gameConfig.ifEffect||(mapDao.onlyZoomShow&&cam.rescource.drawType>0)||!mapDao.onlyZoomShow)){
                batch.setColor(Color.WHITE);
                if(cam.loopState==0||cam.loopState==1){
                  //  batch.draw(mapImage,game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),cam.getMapW_px(),cam.getMapH_px());
                    batch.draw(mapDao.mapImage,mapDao.mapRefX,mapDao.mapRefY,cam.getMapW_px(),cam.getMapH_px());
                }
                if(cam.loopState==1||cam.loopState==2){
               //     batch.draw(mapImage,game.sMapScreen.tempActor.getX()+cam.getMapW_px(),game.sMapScreen.tempActor.getY(),cam.getMapW_px(),cam.getMapH_px());
                    batch.draw(mapDao.mapImage,mapDao.mapRefX+cam.getMapW_px(),mapDao.mapRefY,cam.getMapW_px(),cam.getMapH_px());
                }
                try{
                    GameMap.drawMapTileForMap(game,batch,smapDao,cam,scale, mapW_px,mapH_px, showType,cam.rescource.drawType>1,coord.getRegionId(),mapDao.alphaRate);
                }catch (Exception e){
                    Gdx.app.error("bgactor drawMapTileForMap","");
                }
            }else {
                if(ifHavePtImage){
                    batch.setColor(Color.WHITE);
                    if(cam.loopState==0||cam.loopState==1){
                        //  batch.draw(mapImage,game.sMapScreen.tempActor.getX(),game.sMapScreen.tempActor.getY(),cam.getMapW_px(),cam.getMapH_px());
                        batch.draw(mapDao.floorImage,0,0,cam.getMapW_px(),cam.getMapH_px());
                    }
                    if(cam.loopState==1||cam.loopState==2){
                        //     batch.draw(mapImage,game.sMapScreen.tempActor.getX()+cam.getMapW_px(),game.sMapScreen.tempActor.getY(),cam.getMapW_px(),cam.getMapH_px());
                        batch.draw(mapDao.floorImage,cam.getMapW_px(),0,cam.getMapW_px(),cam.getMapH_px());
                    }
                }else {
                    for(i=cam.drawAreas.size-1;i>=0;i--){
                        if (cam.drawAreas.get(i)) {
                            SpriteDAO s=mainSprites.get(i);
                            if(cam.loopState==0){
                                batch.draw(s.getSprite(), s.getRefx(), s.getRefy(), getOriginX(), getOriginY(), s.getWidth(), s.getHeight(),
                                        getZoom(), getZoom(), getRotation());
                                //  Gdx.app.log("drawS", s.getName() + ": x:" + s.getRefx() + " y:" + s.getRefy());
                            }else if(cam.loopState==1){
                                batch.draw(s.getSprite(), s.getRefx()+mapW_px*this.zoom, s.getRefy(), getOriginX(), getOriginY(), s.getWidth(), s.getHeight(),
                                        getZoom(), getZoom(), getRotation());
                                batch.draw(s.getSprite(), s.getRefx(), s.getRefy(), getOriginX(), getOriginY(), s.getWidth(), s.getHeight(),
                                        getZoom(), getZoom(), getRotation());
                            }else if(cam.loopState==2){
                                batch.draw(s.getSprite(), s.getRefx()+mapW_px*this.zoom, s.getRefy(), getOriginX(), getOriginY(), s.getWidth(), s.getHeight(),
                                        getZoom(), getZoom(), getRotation());
                            }
                        }
                    }
                }
                try{
                    GameMap.drawAllTileForMap(game,batch,smapDao,cam,scale, mapW_px,mapH_px, showType,cam.rescource.drawType>1,coord.getRegionId());
                }catch (Exception e){
                    Gdx.app.error("bgactor drawAllTileForMap","");
                }
            }



        //    GameMap.old_drawAllTileForMap(game,batch,smapDao,cam,scale, mapW_px,mapH_px, showType,cam.rescource.drawType>1,coord.getRegionId());
            if(texture!=null){
                batch.setColor(1,1,1,1);
                batch.draw(texture,0,0,texture.getWidth(),texture.getHeight());
            }


           /* if(cam.rescource.drawType>1){
                GameMap.drawLegionNameForMap(game,batch,smapDao,cam);
            }*/
            //batch.flush();
        }

    }


    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
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



    // 将↓→坐标(左上角为0,0,越向右下越大)转为←↓act坐标(左下角为0,0,越向右上越小)
    public float getActXCoordByImgX(float print_x) {
        return (-print_x);
    }

    public float getActYCoordByImgY(float print_y) {
        return (print_y - mapH_px);
    }

    // 将←↓act坐标(左下角为0,0,越向右上越小)转化为↓→坐标(左上角为0,0,越向右下越大)
    // 将↓→坐标(左上角为0,0,越向右下越大)转为←↓act坐标(左下角为0,0,越向右上越小)
    public float getImgXCoordByActX(float print_x) {
        /*if(print_x!=0f) {
            return (-print_x);
        }else {
            return 0f;
        }*/
        print_x=-print_x;
        //Gdx.app.log("print_x:","print_x:"+print_x+ " rgw:"+getTextureMain().getWidth());
        if(print_x< mapW_px){
            return print_x;
        }else{
            return print_x- mapW_px;
        }
    }



    // 清理5张图层
    public void dispose() {
        /*if(pixmapCountry!=null){
            pixmapCountry.dispose();
            pixmapCountry=null;
        }*/
        /*if (textureMain != null) {
            textureMain.dispose();
            textureMain=null;
        }*/
        /*if (textureCountry!=null){
            textureCountry.dispose();
            textureCountry=null;
        }
        if(pixmapClickRegion!=null){
            pixmapClickRegion.dispose();
            pixmapClickRegion=null;
        }
        if(textureClickRegion!=null){
            textureClickRegion.dispose();
            textureClickRegion=null;
        }*/


        /*if (textureCapital!=null){
            textureCapital.dispose();
            textureCapital=null;
        }*/
        if (ifNeedUnload) {
            GameUtil.unloadSingleResByScreenId(game,am, screenId);
        }
        if(pixmap!=null){
            pixmap.dispose();
            pixmap=null;
        }
        if(texture!=null){
            texture.dispose();
            texture=null;
        }
    }



   /* public Texture getTextureMain() {
        return textureMain;
    }

    public void setTextureMain(Texture textureMain) {
        this.textureMain = textureMain;
    }*/


    /*public Sprite getSpriteMain() {
        return spriteMain;
    }

    public void setSpriteMain(Sprite spriteMain) {
        this.spriteMain = spriteMain;
    }*/


    public SpriteDAO getSpriteGrid() {
        return spriteGrid;
    }

    public void setSpriteGrid(SpriteDAO spriteGrid) {
        this.spriteGrid = spriteGrid;
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

    public SpriteListDAO getSpriteGrids() {
        return spriteGrids;
    }

    public void setSpriteGrids(SpriteListDAO spriteGrids) {
        this.spriteGrids = spriteGrids;
    }



    public void syncPotion(float x, float y, float zoom,int loopState){
        this.x=x;
        this.y=y;
        this.zoom=zoom;
        //this.loopState =loopState;
    }

    public int getMapW_px() {
        return mapW_px;
    }

    public int getMapH_px() {
        return mapH_px;
    }

    /*public void setLoopState(int loopState) {
        this.loopState = loopState;
    }*/
     //GameMap.drawPixmapForLegionColor(game.getMapBinDao(),pixmapCountry,scale,game.getMapBinDao().getCoastGrid(), ResConfig.GameConfig.COUNTRY_COLORS,regionColors);

    //updIds 要修改的id集合,不是region
    public void updColorByIds(IntArray mapUpdIds){
       // pixmapCountry.setColor(0,0,0,0);
       // pixmapCountry.fill();
        //pixmapCountry=GameMap.updColorByRegion(smapDao,pixmapCountry, scale,mapUpdIds,false,true);
       // pixmapCountry=GameMap.drawBuildForSmapByHexagons(smapDao,am,pixmapCountry,scale,mapUpdIds);
       // textureCountry.draw(pixmapCountry,0,0);
    }

    /*public void flashRegionByClickHexagon(int hexagon){
        if(clickRegion!=hexagon){
            clickRegion=hexagon;
            tempIds= smapDao.regionHexagonMap.get(smapDao.getRegionId(hexagon));
            if(tempIds!=null&&tempIds.size>0){
               *//* pixmapClickRegion.setColor(0,0,0,0);
                pixmapClickRegion.fill();
                pixmapClickRegion=GameMap.updColorByRegion(smapDao,pixmapClickRegion, scale,updIds,true,false);
                //绘制首都
               // pixmapClickRegion=GameMap.updateCapital(smapDao,am,pixmapClickRegion, scale,w,mapH_px);
                textureClickRegion.draw(pixmapClickRegion,0,0);
           *//*
                ifFlashClickRegion=true;
            }else {
                ifFlashClickRegion=false;
            }
        }else{
            ifFlashClickRegion=false;
        }

    }*/




    public float getImgYCoordByActY(float print_y) {
        return (mapH_px + print_y);
    }


    public void drawHexagon(int hexagon, Color color){
      //  GameMap.drawPixmapForHexagon(smapDao,pixmapCountry,scale,hexagon,color);
      //  textureCountry.draw(pixmapCountry,0,0);
    }

    public int switchMapColorShow() {
        showType++;
        if(showType >=2){//3是显示区划
            showType =0;
        }
        return showType;
    }

    /*public void removeDrawMark(int index){
        if(drawMarkList !=null&& drawMarkList.size()>index&&index>=0){
            drawMarkList.remove(index);
        }
    }
    public void clearDrawMark(){
        if(drawMarkList !=null&& drawMarkList.size()>0){
            drawMarkList.clear();
        }
    }*/

/*
    public void setDrawMark(IntIntMap drawHexagon, int drawType){
        clearDrawMark();
        if(drawHexagon==null||drawHexagon.size==0){return;}
        Iterator<IntIntMap.Entry> it = drawHexagon.iterator();
        while (it.hasNext()) {
            IntIntMap.Entry c= it.next();
            int id=c.key;
            if(id==-1){
                continue;
            }
            drawMark =new DrawDAO();
            // drawMark.setTextureName(DefDAO.getDrawMark(drawType));
            drawMark.setTextureRegionDAO(game.getImgLists().getTextureByName(DefDAO.getDrawMark(drawType)));
            int x = (id % w) + 1;
            int y = (id / w) + 1;

            drawMark.setLx_px(GameMap.getX_pxByHexagon(x,scale,0)-drawMark.getTextureRegionDAO().getRefx()*scale);
            drawMark.setLy_px(GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true)-drawMark.getTextureRegionDAO().getRefy()*scale);
            drawMark.setW_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionWidth()*scale);
            drawMark.setH_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionHeight()*scale);
            drawMark.setColor(DefDAO.getColor(c.value));
            drawMarkList.add(drawMark);
            //Gdx.app.log("itNext",x+":"+y);
        }

    }*/

   /*public void updCombatRegion(){
        if(drawMarkList ==null){
            drawMarkList =new DrawListDAO();
        }else{
            drawMarkList.clear();
        }

        if(tempMap==null){
            tempMap=new HashMap<>();
        }else{
            tempMap.clear();
        }
       for(Fb2Smap.CombatInfo c:smapDao.combatInfos){
            if(c!=null){
                drawMark =new DrawDAO();
                drawMark.setTextureName("mark_point");
               // tempMap= GameMap.getDownleftPotionById(w,mapH_px, scale,c.getPolitical(),tempMap);
              //  drawMark.setLx_px(tempMap.get("sourceX")-game.getImgLists().getTextureByName(drawMark.getTextureName()).getRefx());
               // drawMark.setLy_px(tempMap.get("sourceY")-game.getImgLists().getTextureByName(drawMark.getTextureName()).getRefy());
                int x = (c.getPolitical() % w) + 1;
                int y = (c.getPolitical() / w) + 1;

                drawMark.setLx_px(GameMap.getX_pxByHexagon(x,scale,0)-game.getImgLists().getTextureByName(drawMark.getTextureName()).getRefx());
                drawMark.setLy_px(GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true)-game.getImgLists().getTextureByName(drawMark.getTextureName()).getRefy());

                drawMark.setColor(Color.RED);
                }
                drawMarkList.add(drawMark);
                        Gdx.app.log("updCombatRegion","CombatInfo c:"+c.getAllAttributes());
            }
        }*/




    /*public void showArrow(Fb2Smap.ConnectData connect){
        //准备显示素材
        if(arrowSprite.getSpriteList().size==0){
            for(int i=1,iMax=16;i<=iMax;i++){
                arrowSprite.add(game.getImgLists().getTextureByName("arrow_blue"),"regionMinorIndex"+i);
            }
        }
    }*/

    public void setPotion(int hexagon,int range){
        potionHexagon=hexagon;
        potionRange=range;
    }

    public void drawTextureByPixmap(boolean ifTrue,int px, int py) {
        if(px<1250&&py<1250){
            pixmap.setBlending(Pixmap.Blending.None);
            if(ifTrue){
                pixmap.setColor(Color.YELLOW);
            }else {
                pixmap.setColor(Color.RED);
            }
            pixmap.drawPixel(px, 1250-py);
            texture.draw(pixmap, 0, 0);
        }
    }


}