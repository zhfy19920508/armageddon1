package com.zhfy.game.model.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.conversion.Fb2Map;

public class Coord {
    //定义坐标 开始均为0

    public Coord(){
        this.zoom=1f;
    }

    public Coord(int x,int y){
        this.x=x;//至少为0
        this.y=y;//至少为0
        this.zoom=1f;
    }

    public Coord initCoord(int x,int y){
        this.x=x;//至少为0
        this.y=y;//至少为0
        this.zoom=1f;
        return this;
    }

    public Coord(int x,int y,int id){
        this.x=x;//至少为0
        this.y=y;//至少为0
        this.id=id;
        this.zoom=1f;
    }

    public Coord(int x,int y,int id,int regionId){
        this.x=x;//至少为0
        this.y=y;//至少为0
        this.id=id;
        this.regionId=regionId;
        this.zoom=1f;
    }
    public Coord(int x,int y,float m,float n){
        this.x=x;//至少为0
        this.y=y;//至少为0
        this.vertexX=m;
        this.vertexY=n;
        this.centerX=vertexX+ ResDefaultConfig.Map.HEXAGON_WIDTH* ResDefaultConfig.Map.MAP_SCALE/2;
        this.centerY=vertexY+ ResDefaultConfig.Map.HEXAGON_HEIGHT* ResDefaultConfig.Map.MAP_SCALE/2;
        this.refX=0;
        this.refY=0;
        this.zoom=1f;
    }
    
    public int x;
    public int y;
    public int regionId;
    //以下数据用来保证,顶点坐标始终位于中心
    private float halfWidth;//半宽
    private float halfHight;//半高
    private float refX;
    private float refY;
    private float zoom;
    
    
    public int getRegionId() {
        return regionId;
    }
    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public void logCoord(){
        Gdx.app.log("coordLog","x:"+x+" y:"+y+" id:"+id+" regionId:"+regionId+" halfWidth:"+ halfWidth +" halfHight:"+ halfHight +" refX:"+refX+" refY:"+refY+" zoom:"+zoom
        +" vertexX:"+getVertexX()+" vertexY:"+getVertexY() );
    }


    public float vertexX;//左上顶点坐标
    public float vertexY;//左上顶点坐标
    public float centerX;
    public float centerY;
    public int id;
    public int sourceX;
    public int sourceY;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public float getVertexX() {
        if(zoom==1f){
            return vertexX   ;
        }else{
            return vertexX+(1-zoom)* halfWidth +zoom*refX;
        }
    }
    public void setVertexX(float vertexX) {
        this.vertexX = vertexX;
    }
    public float getVertexY() {
        if(zoom==1f){
            return vertexY   ;
        }else{
            return vertexY+(1-zoom)* halfHight +zoom*refY;
        }
    }
    public void setVertexY(float vertexY) {
        this.vertexY = vertexY;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    
  //将id转为coord(x,y,id,regionId) 
    public void initCoord(int id, Fb2Map mapbins, float mapHegiht) {
        this.y = (int) id / mapbins.getMapWidth();
        this.x = id - y * mapbins.getMapWidth();
        this.id=id;
        this.vertexX=x* ResDefaultConfig.Map.GRID_WIDTH* ResDefaultConfig.Map.MAP_SCALE;
        this.vertexY=mapHegiht-(float)(y+1+((x % 2)==1?0.5:0))* ResDefaultConfig.Map.HEXAGON_HEIGHT* ResDefaultConfig.Map.MAP_SCALE;
        this.regionId=mapbins.getMapbin().get(id).getRegionId();
        this.centerX=vertexX+ ResDefaultConfig.Map.HEXAGON_WIDTH* ResDefaultConfig.Map.MAP_SCALE/2;
        this.centerY=vertexY+ ResDefaultConfig.Map.HEXAGON_HEIGHT* ResDefaultConfig.Map.MAP_SCALE/2;
    }

    //将id转为coord(x,y,id,regionId)
    public void initCoord(int id, Fb2Map mapbins, float mapHegiht, int width, int height, int refX, int refY, float zoom) {
        this.y = (int) id / mapbins.getMapWidth();
        this.x = id - y * mapbins.getMapWidth();
        this.id=id;
        this.vertexX=x* ResDefaultConfig.Map.GRID_WIDTH* ResDefaultConfig.Map.MAP_SCALE;
        this.vertexY=mapHegiht-(float)(y+1+((x % 2)==1?0.5:0))* ResDefaultConfig.Map.HEXAGON_HEIGHT* ResDefaultConfig.Map.MAP_SCALE;
        this.regionId=mapbins.getMapbin().get(id).getRegionId();
        this.centerX=vertexX+width* ResDefaultConfig.Map.MAP_SCALE;
        this.centerY=vertexY+height* ResDefaultConfig.Map.MAP_SCALE;
        this.halfWidth =width* ResDefaultConfig.Map.MAP_SCALE/2;
        this.halfHight =height* ResDefaultConfig.Map.MAP_SCALE/2;
        this.refX=refX* ResDefaultConfig.Map.MAP_SCALE;
        this.refY=refY* ResDefaultConfig.Map.MAP_SCALE;
        this.zoom=zoom;
    }

    public void initCoord(int x,int y,int hexagon){
        setX(x);
        setY(y);
        setId(hexagon);
    }

    private Coord converCoord(int id, Fb2Map mapbins) {
        this.y = (int) id / mapbins.getMapWidth();
        this.x = id - y * mapbins.getMapWidth();
        return new Coord(x,y,id,mapbins.getMapbin().get(id).getRegionId());
    }

    
    //将id转为coord
    /*public List<Coord> converCoords(List<Integer> ids,MapBinDAO mapbins) {
        int m;
        int mMax = ids.size();
        List<Coord> rsIds = new ArrayList<Coord>();
        for (m = 0; m < mMax; m++) {
            rsIds.add(converCoord(ids.get(m),mapbins));
        }
        return rsIds;
    }*/
    public Array<Coord> converCoords(Array<Integer> ids, Fb2Map mapbins) {
        int m;
        int mMax = ids.size;
        Array<Coord> rsIds = new Array<Coord>();
        for (m = 0; m < mMax; m++) {
            rsIds.add(converCoord(ids.get(m),mapbins));
        }
        return rsIds;
    }



    //给予正常的坐标 左上为起点向右下延伸
    public void setVByMap(int x,int y,float scale,float mapHegiht,int mapW){
        this.x=x;
        this.y=y;
        this.vertexX=x* ResDefaultConfig.Map.GRID_WIDTH* ResDefaultConfig.Map.MAP_SCALE* scale;
        this.vertexY=mapHegiht-(float)(y+1+((x % 2)==1?0.5:0))* ResDefaultConfig.Map.HEXAGON_HEIGHT* ResDefaultConfig.Map.MAP_SCALE*scale;
        this.id=x+y*mapW;
        this.centerX=vertexX+ ResDefaultConfig.Map.HEXAGON_WIDTH* ResDefaultConfig.Map.MAP_SCALE/2;
        this.centerY=vertexY+ ResDefaultConfig.Map.HEXAGON_HEIGHT* ResDefaultConfig.Map.MAP_SCALE/2;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
