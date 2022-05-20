package com.zhfy.game.model.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.zhfy.game.MainGame;
import com.zhfy.game.framework.tool.CHAsyncTask;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.screen.stage.SMapGameStage;

import java.util.ArrayList;

public class CamerDAO {

    public boolean ifMove;
    private float camX;
    private float camY;
    private float camZ;

    private boolean ifLoop;
    private float camZoomMax;
    private float camZoomMin;
    private float camWScope;
    private float camHScope;
    private int mapW_px;
    private int mapH_px;
    private float vw;
    private float vh;
    private int mapW;
    private int mapH;
    private OrthographicCamera camera;
    public int loopState;//0 绘制左边 1绘制两侧 2只绘制右边
    public Array<Boolean> drawAreas;// 绘制区域
    private int refY;
    private ArrayList<SpriteDAO> mapSprites;
    private float scale;
    private MainGame game;
    public SMapGameStage.SMapPublicRescource rescource;
    //地图移动后计算这几个坐标
    private Coord tempCoord;
    //左上坐标
    public int csx;
    public int csy;
    //右下坐标
    public int cex;
    public int cey;
    //坐标宽高
    public int cw;
    public int ch;
    //public boolean ifTest;



    public CamerDAO (MainGame game, OrthographicCamera camera, int w, int h, float viewW, float viewH, int mapW_px, int mapH_px, float zoomMin, float zoomMax, int refY, float scale, boolean ifLoop, SMapGameStage.SMapPublicRescource rescource){
        init(game,camera,w,h,viewW,viewH,mapW_px,mapH_px,zoomMin,zoomMax,refY,scale,ifLoop,rescource);
    }

    public void init(MainGame game,OrthographicCamera camera,int w,int h,float viewW,float viewH,int mapW_px,int mapH_px,float zoomMin,float zoomMax,int refY,float scale,boolean ifLoop, SMapGameStage.SMapPublicRescource rescource){
        this.game=game;
        this.camera=camera;
        this.mapW=w;
        this.mapH=h;
        vw=viewW;
        vh=viewH;
        camWScope =viewW/2;
        camHScope =viewH/2 ;
        this.mapW_px =mapW_px;
        this.mapH_px =mapH_px;
        this.rescource=rescource;
        //int zoomMaxx=mapW_px/mapH_px<mapH_px/mapW_px?mapH_px/mapW_px:mapW_px/mapH_px;
        //camZoomMax=zoomMax< zoomMaxx? zoomMax:zoomMaxx;
        camZoomMax=zoomMax;


      /*  if(camZoomMax>ResDefaultConfig.Map.REGION_ZOOM){
            camZoomMax=ResDefaultConfig.Map.REGION_ZOOM;
        }*/

        camZoomMin=zoomMin;
        this.refY=refY;
        this.scale=scale;
        this.ifLoop=ifLoop;
        initDrawAreas();
        game.gameConfig.setIfDrawArmyMark(camera.zoom);
        ifMove=false;
    }


    private void initDrawAreas(){
        if(drawAreas==null){
            drawAreas=new Array<>();
        }else {
            drawAreas.clear();
        }
        //根据地图大小来绘制
        int w = (int) ((mapW_px / ResDefaultConfig.Map.PT_SIDE) + 1);
        int h = (int) ((mapH_px / ResDefaultConfig.Map.PT_SIDE) + 1);
        int spliteCount = w * h;
        for(int i=0;i<spliteCount;i++){
            drawAreas.add(false);
        }
    }

    private boolean ifXInScope(int x){
        if(x>=getSX()&&x<=getEX()){
            return true;
        }else {
            return false;
        }
    }
    private boolean ifYInScope(int y){
        if(y>=getSY()&&y<=getEY()){
            return true;
        }else {
            return false;
        }
    }

    public void asyncMoveCameraToHexagon(int id, int moveSecond ){
        asyncMoveCameraToHexagon2(id,moveSecond,0,0,0);
    }


    //id 要移动到的坐标位置,moveSecond 移动用时
    // zoomstate:0 无变化 1 放大  2 缩小




    public void asyncMoveCameraToHexagon(int id,  final int moveSecond,  final float targetZoom ){
        int x=GameMap.getHX(id,mapW);
        int y=GameMap.getHY(id,mapW);
        float targetX_px=GameMap.getX_pxByHexagon(x,scale,0);
        float targetY_px=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        final float  moveX;
        final int timeRate=10;
        if(camera.position.x>mapW_px){
            moveX=(targetX_px-(camera.position.x-mapW_px))/(moveSecond*timeRate);
        }else {
            moveX=(targetX_px-camera.position.x)/(moveSecond*timeRate);
        }

        final float  moveY=(targetY_px-camera.position.y)/(moveSecond*timeRate);//Gdx.app.log("asyncMoveCameraToHexagon",camera.position.x+"->"+targetX_px+" "+ camera.position.y+"->"+targetY_px);

        CHAsyncTask task=new CHAsyncTask("asyncMoveCameraToHexagon",moveSecond*2) {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(String result) {
                Gdx.app.log("zoomChange",targetZoom+":"+camera.zoom);
            }
            @Override
            public String doInBackground() {

                float zoomv = 0;
                /*if(zoomstate==1){
                    zoomv=-0.01f;
                }else if(zoomstate==2){
                    zoomv=0.01f;
                }*/
                if(targetZoom>getZoom()){
                    zoomv=(targetZoom-getZoom())/(moveSecond*timeRate);
                }else {
                    zoomv=(targetZoom-getZoom())/(moveSecond*timeRate);
                }


                // if(zoomMin==0){zMi=getCamZoomMin(); }else{zMi=zoomMin;}
                // if(zoomMax==0){zMa=getCamZoomMax(); }else{zMa=zoomMax;}
                for(int i=0,iMax=moveSecond*timeRate;i<iMax;i++){
                    try {
                        Thread.sleep(timeRate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    translateCam(moveX ,moveY,true);
                    //logCamScope();
                    if( this.ifTimeOut()){
                        break;
                    }
                }
                if(zoomv!=0){
                    while (setZoomChange(zoomv)){
                        try {
                            Thread.sleep(timeRate);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(zoomv>0){
                            if(camera.zoom>targetZoom){
                                break;
                            }
                        }else{
                            if(camera.zoom<targetZoom) {
                                break;
                            }
                        }
                        if( this.ifTimeOut()){
                            break;
                        }
                        //logCamScope();
                    }
                }
                return null;
            }
        };
        game.asyncManager.loadTask(task);
        game.asyncManager.update();
    }



    public void asyncMoveCameraToHexagon2(int id, final int moveSecond, final int zoomstate, final float zoomMin, final float zoomMax ){
      /*if(zoomstate==2){
          updDrawScope(zoomMax);
      }*/
        if(id<0){
            return;
        }
        Gdx.app.log("asyncMoveCameraToHexagon2",id+":"+moveSecond+":"+zoomstate+":"+zoomMin+":"+zoomMax);
        int x=GameMap.getHX(id,mapW);
        int y=GameMap.getHY(id,mapW);
        float targetX_px=GameMap.getX_pxByHexagon(x,scale,0);
        float targetY_px=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        final float  moveX;
        final int timeRate=10;
        if(camera.position.x>mapW_px){
            moveX=(targetX_px-(camera.position.x-mapW_px))/(moveSecond*timeRate);
        }else {
            moveX=(targetX_px-camera.position.x)/(moveSecond*timeRate);
        }

        final float  moveY=(targetY_px-camera.position.y)/(moveSecond*timeRate);//Gdx.app.log("asyncMoveCameraToHexagon",camera.position.x+"->"+targetX_px+" "+ camera.position.y+"->"+targetY_px);





        CHAsyncTask task=new CHAsyncTask("asyncMoveCameraToHexagon2",moveSecond*2) {
            @Override
            public void onPreExecute() {
            }

            @Override
            public void onPostExecute(String result) {
                updDrawAreas();
            }

            @Override
            public String doInBackground() {


                float zoomv = 0;
                float zMi=0;float zMa = 0;
                if(zoomMin==0){zMi=getCamZoomMin(); }else{zMi=zoomMin;}
                if(zoomMax==0){zMa=getCamZoomMax(); }else{zMa=zoomMax;}
                if(zoomstate==10){
                    //zoomv=-0.01f;

                    zoomv= (zoomMin-camera.zoom)/(moveSecond*timeRate);
                }else if(zoomstate==2){
                    //zoomv=0.01f;
                    zoomv= (zoomMax-camera.zoom)/(moveSecond*timeRate);
                }

                if(zoomstate!=0){
                    while (setZoom(zoomv)){
                        try {
                            Thread.sleep(timeRate);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(camera.zoom>zMa){
                            break;
                        }else if(camera.zoom<zMi) {
                            break;
                        }
                        if(this.ifTimeOut()){
                            break;
                        }
                        //logCamScope();
                    }
                }

                for(int i=0,iMax=moveSecond*timeRate;i<iMax;i++){
                    try {
                        Thread.sleep(timeRate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    translateCam(moveX ,moveY,true);
                    if(this.ifTimeOut()){
                        break;
                    }
                    //logCamScope();
                }
                return null;
            }

        };
        game.asyncManager.loadTask(task);
        game.asyncManager.update();
    }

    private boolean setZoom(float zoomv) {
        //  float oldX= camera.position.x;float oldY= camera.position.y;float oldZoom= camera.zoom;
        setZoomChangeForMouse(game.getWorldWidth()/2,game.getWorldHeight()/2,zoomv);
        if(camera.zoom<camZoomMin){
            camera.zoom=camZoomMin;
            return false;
        }else if(camera.zoom>camZoomMax){
            camera.zoom=camZoomMax;
            return false;
        }else {
            return true;
        }
    }

    public boolean setZoomChange(float v){
        Boolean rs;
        if(camera.zoom<camZoomMin||camera.zoom+v<camZoomMin){
            camera.zoom=camZoomMin;
            rs= false;
        }else if(camera.zoom>camZoomMax||camera.zoom+v>camZoomMax){
            camera.zoom=camZoomMax;
            rs= false;
        }else {
            camera.zoom +=v;
            rs= true;
        }
        rescource.updByCam( camera.zoom);
        translateCam(0,0,true);
        return rs;
    }

    private void updDrawScope(float zoom) {
        if(mapSprites!=null){
            for(int i=0,iMax=mapSprites.size();i<iMax;i++){
                SpriteDAO s=mapSprites.get(i);
                /*if (i==19) {
                    drawAreas.set(i,true);
                }*/
                boolean xb;
                if(ifLoop){
                    xb=(ComUtil.ifIntervalOverlap(getSXByZoom(zoom),getEXByZoom(zoom),s.getRefx(), (int) (s.getRefx()+s.getWidth()))
                            ||ComUtil.ifIntervalOverlap(getSXByZoom(zoom),getEXByZoom(zoom), (int) (s.getRefx()+mapW_px), (int) (s.getRefx()+s.getWidth()+mapW_px))
                    );
                }else {
                    xb=ComUtil.ifIntervalOverlap(getSXByZoom(zoom),getEXByZoom(zoom),s.getRefx(), (int) (s.getRefx()+s.getWidth()));
                }
                boolean yb=ComUtil.ifIntervalOverlap(getSYByZoom(zoom),getEYByZoom(zoom),s.getRefy(), (int) (s.getRefy()+s.getHeight()));
                if(xb&&yb){
                    drawAreas.set(i,true);
                }else {
                    drawAreas.set(i,false);
                }
                //  Gdx.app.log("updDrawAreas",i+":"+drawAreas.get(i)+ " x:" + s.getRefx() + " y:" + s.getRefy() + " x2:" + (s.getRefx()+s.getWidth()) + " y2:" + (s.getRefy()+s.getHeight())+" sx:"+getSX()+" ex:"+getEX()+" sy:"+getSY()+" ey:"+getEY()+" xb:"+xb+" yb:"+yb);
            }
        }
        //计算坐标
        //  if(ifTest){
        tempCoord = GameMap.getHotCell(tempCoord,getSXByZoom(zoom), mapH_px-getEYByZoom(zoom), 1f, scale, mapW, 0, 0);
        csx=tempCoord.x;
        csy=tempCoord.y;


        tempCoord = GameMap.getHotCell(tempCoord,getEXByZoom(zoom),mapH_px-getSYByZoom(zoom) , 1f, scale, mapW, 0, 0);



        cex=tempCoord.x;
        cey=tempCoord.y;

        cw=cex-csx;
        ch=cey-csy;
    }

    public void moveCameraToHexagon(int id, int moveSecond ){
        moveCameraToHexagon(id,moveSecond,0,0,0);
    }


    public void moveCameraToHexagon(int id, int moveSecond, int zoomstate, float zoomMin, float zoomMax ){
        int x=GameMap.getHX(id,mapW);
        int y=GameMap.getHY(id,mapW);
        float targetX_px=GameMap.getX_pxByHexagon(x,scale,0);
        float targetY_px=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        float moveX;
        int timeRate=10;
        if(camera.position.x>mapW_px){
            moveX=(targetX_px-(camera.position.x-mapW_px))/(moveSecond*timeRate);
        }else {
            moveX=(targetX_px-camera.position.x)/(moveSecond*timeRate);
        }

        float  moveY=(targetY_px-camera.position.y)/(moveSecond*timeRate);//Gdx.app.log("asyncMoveCameraToHexagon",camera.position.x+"->"+targetX_px+" "+ camera.position.y+"->"+targetY_px);



        float zoomv = 0;
        if(zoomstate==1){
            zoomv=-0.01f;
        }else if(zoomstate==2){
            zoomv=0.01f;
        }
        float zMi=0;float zMa = 0;
        if(zoomMin==0){zMi=getCamZoomMin(); }else{zMi=zoomMin;}
        if(zoomMax==0){zMa=getCamZoomMax(); }else{zMa=zoomMax;}
        for(int i=0,iMax=moveSecond*timeRate;i<iMax;i++){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            translateCam(moveX ,moveY,true);
            //logCamScope();
        }
        if(zoomstate!=0){
            while (setZoomChange(zoomv)){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(camera.zoom>zMa){
                    break;
                }else if(camera.zoom<zMi) {
                    break;
                }
                //logCamScope();
            }

        }
    }

    public void updDrawAreas(){
        if(mapSprites!=null){
            for(int i=0,iMax=mapSprites.size();i<iMax;i++){
                SpriteDAO s=mapSprites.get(i);
                /*if (i==19) {
                    drawAreas.set(i,true);
                }*/
                boolean xb;
                if(ifLoop){
                    xb=(ComUtil.ifIntervalOverlap(getSX(),getEX(),s.getRefx(), (int) (s.getRefx()+s.getWidth()))
                            ||ComUtil.ifIntervalOverlap(getSX(),getEX(), (int) (s.getRefx()+mapW_px), (int) (s.getRefx()+s.getWidth()+mapW_px))
                    );
                }else {
                    xb=ComUtil.ifIntervalOverlap(getSX(),getEX(),s.getRefx(), (int) (s.getRefx()+s.getWidth()));
                }
                boolean yb=ComUtil.ifIntervalOverlap(getSY(),getEY(),s.getRefy(), (int) (s.getRefy()+s.getHeight()));
                if(xb&&yb){
                    drawAreas.set(i,true);
                }else {
                    drawAreas.set(i,false);
                }
                //  Gdx.app.log("updDrawAreas",i+":"+drawAreas.get(i)+ " x:" + s.getRefx() + " y:" + s.getRefy() + " x2:" + (s.getRefx()+s.getWidth()) + " y2:" + (s.getRefy()+s.getHeight())+" sx:"+getSX()+" ex:"+getEX()+" sy:"+getSY()+" ey:"+getEY()+" xb:"+xb+" yb:"+yb);
            }
        }
    }

    public int translateCam(float x,float y,boolean ifRefersh){
        return  setPotion(camera.position.x+x,camera.position.y+y,ifRefersh);
        // camera.translate(x,y,0);
    }
    //!对x,y进行检查  loopState 绘制左边 1绘制两侧 2只绘制右边
    public int setPotion(float x,float y,boolean ifRefersh){
        loopState =0;//0 绘制左边 1绘制两侧 2只绘制右边
        if(ifLoop) {
            //1 在左地图内
            if(x>= camWScope *camera.zoom&&x<= mapW_px - camWScope *camera.zoom){
                loopState =0;
                // Gdx.app.log("setPotion0","x:"+x+" y:"+y);
                //2 在右地图向左地图滑动
            }else if(x>= camWScope *camera.zoom+ mapW_px ){
                x=x- mapW_px;
                loopState =0;
                //  Gdx.app.log("setPotion1","x:"+x+" y:"+y);
                //3 向左划出左边界
            }else if(x< camWScope*camera.zoom){
                //   Gdx.app.log("setPotion2","x:"+x+" y:"+y);
                loopState =1;
                x=x+ mapW_px;
                //4 向右划出左地图的右边界
            }else if(x> mapW_px - camWScope *camera.zoom&&x< camWScope *camera.zoom+ mapW_px){///划入右边,接近循环
                loopState =1;
                //  Gdx.app.log("setPotion3","x:"+x+" y:"+y);
            }else {
                Gdx.app.error("setPotionError","未知位置 x:"+x+" y:"+y);
            }
        }else{
            if(x< camWScope *camera.zoom){
                x= camWScope *camera.zoom;
            }else if(x>(mapW_px + camWScope *camera.zoom-vw*camera.zoom)){
                x=(mapW_px + camWScope *camera.zoom-vw*camera.zoom);
            }
        }

        float yMin=(camHScope*camera.zoom+ ResDefaultConfig.Map.HEXAGON_HEIGHT_REF* ResDefaultConfig.Map.MAP_SCALE) ;
        float yMax=(mapH_px- ResDefaultConfig.Map.HEXAGON_HEIGHT_REF* ResDefaultConfig.Map.MAP_SCALE + camHScope *camera.zoom-vh*camera.zoom);
        if(y< yMin){
            y= yMin;
        }else if(y>yMax){
            y=yMax;
        }
        if(!ifRefersh){
            ifRefersh=ifCrossBorder( camera.position.x, camera.position.y,x,y);
        }
        camera.position.x=x;
        camera.position.y=y;
        //   Gdx.app.log("setPotion","x:"+x+" y:"+y+" w:"+getCamera().viewportWidth*camera.zoom+" h:"+getCamera().viewportHeight*camera.zoom);
        if(ifRefersh){
            updDrawAreas();
        }
        //计算坐标
        //  if(ifTest){
        tempCoord = GameMap.getHotCell(tempCoord,getSX(), mapH_px-getEY(), 1f, scale, mapW, 0, 0);
        csx=tempCoord.x;
        csy=tempCoord.y;


        tempCoord = GameMap.getHotCell(tempCoord,getEX(),mapH_px-getSY() , 1f, scale, mapW, 0, 0);



        cex=tempCoord.x;
        cey=tempCoord.y;

        cw=cex-csx;
        ch=cey-csy;

        // }

        /**/ if(rescource!=null&&rescource.getSMapDAO()!=null){
            if(rescource.hexagons==null){rescource.hexagons=new IntArray();}else {rescource.hexagons.clear();}
            if(rescource.allMapHexagons==null){rescource.allMapHexagons=new Array<>();}else {rescource.allMapHexagons.clear();}
            if(rescource.tileMapHexagons ==null){rescource.tileMapHexagons =new Array<>();}else {rescource.tileMapHexagons.clear();}
            if(rescource.seaMapHexagons==null){rescource.seaMapHexagons=new Array<>();}else {rescource.seaMapHexagons.clear();}
            if(rescource.legionDatas==null){rescource.legionDatas=new IntArray();}else {rescource.legionDatas.clear();}
            if(rescource.buildDatas ==null){rescource.buildDatas =new Array();}else {rescource.buildDatas.clear();}
            if(rescource.facilityDatas ==null){rescource.facilityDatas =new Array();}else {rescource.facilityDatas.clear();}
            if(rescource.fortDatas ==null){rescource.fortDatas =new Array();}else {rescource.fortDatas.clear();}

            rescource.buildDrawState0=false; rescource.buildDrawState1=false; rescource.buildDrawState2=false;
            rescource.armyDrawState0 =false;rescource.armyDrawState1 =true;
            int i,j=0,iMax = cw + 2, jMax = ch + 3;
            int id,xId,yId;
            for (; j < jMax; j++) {
                for (i = 0; i < iMax; i++) {
                    xId = csx + i;
                    if (rescource.getSMapDAO().ifLoop && xId > rescource.getSMapDAO().masterData.getWidth()) {
                        xId = xId - rescource.getSMapDAO().masterData.getWidth();
                    }
                    yId = csy + j;
                    id = xId - 1 + (yId - 1) * rescource.getSMapDAO().masterData.getWidth();
                    if (id >= 0 && id < rescource.getSMapDAO().hexagonDatas.size) {
                        Fb2Map.MapHexagon h = rescource.getSMapDAO().hexagonDatas.get(id);
                        if(h==null){
                            continue;
                        }
                        if(h.isSea()){
                            rescource.seaMapHexagons.add(h);
                            if(rescource.drawType==0&&game.gameConfig.ifEffect&&h.facilityData==null&&h.getBlockType()==1&&h.getBackTile()==2){
                                rescource.tileMapHexagons.add(h);
                            }
                        }else {
                            rescource.tileMapHexagons.add(h);
                        }
                        rescource.hexagons.add(h.getHexagonIndex());
                        rescource.allMapHexagons.add(h);
                        int li=h.getRegionLegionIndex();
                        if(!rescource.legionDatas.contains(li)){
                            rescource.legionDatas.add(li);
                        }
                        if(h.getHexagonIndex()==h.getRegionId()&&h.buildData!=null){
                            rescource.buildDatas.add(h.buildData);
                            if(rescource.drawType==2){
                                if(h.buildData.getBuildActor().mainFlag!=null){
                                    rescource.buildDrawState0=true;
                                    if(h.buildData.getBuildActor().regionForcesRateF>0){
                                        rescource.buildDrawState1=true;
                                    }
                                }else if(!h.isSea()){
                                    rescource.buildDrawState2=true;
                                }
                            }else if(rescource.drawType==1){
                                rescource.buildDrawState0=true;
                            }else if(rescource.drawType==0){
                                rescource.buildDrawState0=true;
                                if(h.armyData!=null&&h.armyData.armyActor==null){
                                    rescource.buildDrawState1=true;
                                }
                            }
                        }
                        if(rescource.drawType<2){
                            if(h.armyData!=null&&h.armyData.armyActor!=null){
                                if(rescource.drawType==1){
                                    if(h.armyData.armyActor.ifDrawGeneral()){
                                        rescource.armyDrawState1=true;
                                    }else {
                                        rescource.armyDrawState0=true;
                                    }
                                }else {
                                    rescource.armyDrawState0=true;
                                    if(h.armyData.armyActor.ifDrawGeneral()){
                                        rescource.armyDrawState1=true;
                                    }
                                }
                            }
                            if(h.fortData!=null&&h.fortData.fortActor!=null){
                                rescource.fortDatas.add(h.fortData);
                                rescource.fortDrawState=true;
                            }
                            if(h.facilityData!=null&&h.facilityData.facilityActor!=null){
                                rescource.facilityDatas.add(h.facilityData);
                                rescource.facilityDrawState=true;
                            }
                        }
                    }
                }
            }
        }
        // logViewInfo();
        return loopState;
    }
    public void logViewInfo(){
        Gdx.app.log("setPotion","csx:"+csx+" csy:"+csy+" cw:"+cw+" ch:"+ch+" loopState:"+loopState);
    }

    public boolean getIfMove() {
        if(camX!=camera.position.x||camY!=camera.position.y||camZ!=camera.zoom){
            camX=camera.position.x;
            camY=camera.position.y;
            camZ=camera.zoom;
            return true;
        }else {
            return false;
        }
    }

    //中心点缩放 根据鼠标中心点缩放
    public int setZoomChangeForMouse(float cx, float cy, float zoomC){

        if(camera.zoom<camZoomMin){
            camera.zoom=camZoomMin;
            rescource.updByCam( camera.zoom);
            return translateCam(0,0,false);
        }else if(camera.zoom>camZoomMax){
            camera.zoom=camZoomMax;
            rescource.updByCam( camera.zoom);
            return translateCam(0,0,false);
        }
        if(camera.zoom+zoomC<camZoomMin||camera.zoom+zoomC>camZoomMax){
            return translateCam(0,0,false);
        }
        camera.zoom +=zoomC;
        rescource.updByCam( camera.zoom);
        //Gdx.app.log("Zoom","cx:"+cx+" cy:"+cy+" zoomV:"+zoomV+" zoom:"+camera.zoom);
        return  setPotion(camera.position.x+(vw/2-cx)*zoomC,camera.position.y-(vh/2-cy)*zoomC,true);// -(cy-vh)
    }

    public int setZoomForMouse(float cx, float cy, float zoomV){
        if(camera.zoom<camZoomMin){
            camera.zoom=camZoomMin;
            rescource.updByCam( camera.zoom);
            return translateCam(0,0,false);
        }else if(camera.zoom>camZoomMax){
            camera.zoom=camZoomMax;
            rescource.updByCam( camera.zoom);
            return translateCam(0,0,false);
        }
        if(zoomV<camZoomMin||zoomV>camZoomMax){
            return translateCam(0,0,false);
        }
        float zoomC=zoomV- camera.zoom;
        rescource.updByCam( camera.zoom);
        //Gdx.app.log("Zoom","cx:"+cx+" cy:"+cy+" zoomV:"+zoomV+" zoom:"+camera.zoom);
        return  setPotion(camera.position.x+(vw/2-cx)*zoomC,camera.position.y-(vh/2-cy)*zoomC,true);// -(cy-vh)
    }

    public float getZoom(){
        return camera.zoom;
    }

    public float getCamZoomMax() {
        return camZoomMax;
    }

    public void setCamZoomMax(float camZoomMax) {
        this.camZoomMax = camZoomMax;
    }

    public float getCamZoomMin() {
        return camZoomMin;
    }

    public void setCamZoomMin(float camZoomMin) {
        this.camZoomMin = camZoomMin;
    }

    public float getCamWScope() {
        return camWScope;
    }

    public void setCamWScope(float camWScope) {
        this.camWScope = camWScope;
    }

    public float getCamHScope() {
        return camHScope;
    }

    public void setCamHScope(float camHScope) {
        this.camHScope = camHScope;
    }

    public float getMapW_px() {
        return mapW_px;
    }

    public void setMapW_px(int mapW_px) {
        this.mapW_px = mapW_px;
    }

    public float getMapH_px() {
        return mapH_px;
    }

    public void setMapH_px(int mapH_px) {
        this.mapH_px = mapH_px;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void logCamScope(){//x,y以左上为原点的范围
        Gdx.app.log("camScope"," x:"+camera.position.x+" y:"+(camera.position.y)+" fy:"+(mapH_px -camera.position.y)+" w:"+vw*camera.zoom +" h:"+vh*camera.zoom+ " zoom:"+camera.zoom+" sx:"+getSX()+" sy:"+getSY()+" ex:"+getEX()+" ey:"+getEY());
        /*if(mapSprites!=null){
            for(int i=0,iMax=drawAreas.size;i<iMax;i++){
                if(drawAreas.get(i)){

                }
            }
        }*/

    }
    //左上为原点
    public int getSX(float x){
        return (int) (x-vw*camera.zoom /2);
    }
    public int getSY(float y){
        return (int) (y-vh*camera.zoom/2);
    }
    public int getEX(float x){
        return (int) (x+vw*camera.zoom /2);
    }
    public int getEY(float y){
        return (int) (y+vh*camera.zoom/2);
    }
    public int getSX(){
        return (int) (camera.position.x-vw*camera.zoom /2);
    }
    public int getSY(){
        return (int) (camera.position.y-vh*camera.zoom/2);
    }
    public int getEX(){
        return (int) (camera.position.x+vw*camera.zoom /2);
    }
    public int getEY(){
        return (int) (camera.position.y+vh*camera.zoom/2);
    }


    //左上为原点
    public int getSXByZoom(float x,float zoom){
        return (int) (x-vw*zoom /2);
    }
    public int getSYByZoom(float y,float zoom){
        return (int) (y-vh*zoom/2);
    }
    public int getEXByZoom(float x,float zoom){
        return (int) (x+vw*zoom /2);
    }
    public int getEYByZoom(float y,float zoom){
        return (int) (y+vh*zoom/2);
    }
    public int getSXByZoom(float zoom){
        return (int) (camera.position.x-vw*zoom /2);
    }
    public int getSYByZoom(float zoom){
        return (int) (camera.position.y-vh*zoom/2);
    }
    public int getEXByZoom(float zoom){
        return (int) (camera.position.x+vw*zoom /2);
    }
    public int getEYByZoom(float zoom){
        return (int) (camera.position.y+vh*zoom/2);
    }


    public void setMapSprites(ArrayList<SpriteDAO> mapSprites) {
        this.mapSprites=mapSprites;
        updDrawAreas();
    }

    //判断 移动时判断是否跨界
    private boolean ifCrossBorder(float x1,float y1,float x2,float y2){


        int area1=GameMap.getAreaId(mapSprites,mapW_px,getSX(x1),getSY(y1));
        int area2=GameMap.getAreaId(mapSprites,mapW_px,getEX(x1),getSY(y1));
        int area3=GameMap.getAreaId(mapSprites,mapW_px,getSX(x1),getEY(y1));
        int area4=GameMap.getAreaId(mapSprites,mapW_px,getEX(x1),getEY(y1));
        int area11=GameMap.getAreaId(mapSprites,mapW_px,getSX(x2),getSY(y2));
        int area12=GameMap.getAreaId(mapSprites,mapW_px,getEX(x2),getSY(y2));
        int area13=GameMap.getAreaId(mapSprites,mapW_px,getSX(x2),getEY(y2));
        int area14=GameMap.getAreaId(mapSprites,mapW_px,getEX(x2),getEY(y2));

        if(area1==area11&&area2==area12&&area3==area13&&area4==area14){
            return false;
        }
        return true;
    }


    public boolean ifDrawHexagon(int hexagon){
        if(loopState==0){
            int x= GameMap.getHX(hexagon,mapW);
            int y= GameMap.getHY(hexagon,mapW);
            if(x>=csx&&x<=csx+cw+2&&y>=csy&&y<=csy+ch){
                return true;
            }
        }else if(loopState==1){
            int x= GameMap.getHX(hexagon,mapW);
            int y= GameMap.getHY(hexagon,mapW);
            if(x>=csx&&x<=csx+cw+2&&y>=csy&&y<=csy+ch){
                return true;
            }
            x= GameMap.getHX(hexagon,mapW)+mapW;
            if(x>=csx&&x<=csx+cw+2&&y>=csy&&y<=csy+ch){
                return true;
            }
        }else if(loopState==2){
            int x= GameMap.getHX(hexagon,mapW)+mapW;
            int y= GameMap.getHY(hexagon,mapW);
            if(x>=csx&&x<=csx+cw+2&&y>=csy&&y<=csy+ch){
                return true;
            }
        }
        return false;
    }


    public float getAlphaFlash() {
        return rescource.getAlphaFlash();
    }

    public boolean inScreen(int hexagon1) {
        if(rescource.hexagons.contains(hexagon1)){
            return true;
        }
        return false;
    }
}
