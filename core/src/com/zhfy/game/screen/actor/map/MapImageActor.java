package com.zhfy.game.screen.actor.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.model.framework.TextureRegionDAO;

/**
 * 游戏中的演员,适用于;六边形地图内
 *
 */
public class MapImageActor extends Actor  implements Pool.Poolable {

    private boolean ifEable;
    private TextureRegion region;
    private float sourceX;//初始位置
    private float sourceY;//初始位置

    private float zoom;

    private int mapW_px;//横向移动需要的值 像素
    private int mapH_px;
    private int mapW;//格子数,辅助计算坐标
    private boolean drawLoop;
    private int id;
    private float scale;

   //private Map<String,Integer> coordMap= new HashMap<>();

    public MapImageActor(TextureRegionDAO regionDAO, int mapW_px,int mapH_px,int mapW,int id,float scale) {
        ifEable=false;
        this.mapW_px=mapW_px;
        this.mapH_px=mapH_px;
        this.mapW=mapW;
        this.region=regionDAO.getTextureRegion();;
        this.drawLoop=false;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        this.id=id;
        this.scale=scale;
        initPotionById(regionDAO.getRefx(),regionDAO.getRefy());
        ifEable=true;
    }

    public MapImageActor(){}




    public void init(TextureRegionDAO regionDAO, int mapW_px, int mapH_px,int mapW, int id,float scale){
        //imgLists.getTextureByName("task_battle")
        this.mapW_px=mapW_px;
        this.mapH_px=mapH_px;
        this.mapW=mapW;
        this.zoom=1f;
        this.region=regionDAO.getTextureRegion();
        this.drawLoop=false;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        this.id=id;
        this.scale=scale;
        initPotionById(regionDAO.getRefx(),regionDAO.getRefy());
        setWidth(region.getRegionWidth());
        setHeight(region.getRegionHeight());
        setX(sourceX*zoom+getX());
        setY(sourceY*zoom+getY());
        //TODO
        //this.x=300;
        //this.y=300;
        ifEable=true;
       // Gdx.app.log("imgActor","id:"+id+" x:"+getHX()+" y:"+getHY());
    }

    //同步坐标
    public void syncPotion(boolean drawLoop){
        if(ifEable){
            //this.x=sourceX*zoom+x;
            //this.y=-sourceY*zoom+y;
            //this.zoom=zoom;
            this.drawLoop=drawLoop;
        }else{
            Gdx.app.error("MapImageActor initError","id"+id);
        }

    }



    /*public ImageActor(TextureRegion region) {
        this.region = region;
        this.drawLoop=false;
        this.mapW=0;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
    }

    public ImageActor(Texture texture) {
        this.region = new TextureRegion(texture);
        this.drawLoop=false;
        this.mapW=0;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
    }*/

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
        if (this.region != null) {
            setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        } else {
            setSize(0, 0);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (region == null || !isVisible()||!isIfEable()) {
            return;
        }

        // 备份 batch 原本的 Color
      //  Color tempBatchColor = batch.getColor();

        // 获取演员的 Color
        //Color color = getColor();

        // 将演员的 Color 结合 parentAlpha 设置到 batch
        //batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // 结合演员的属性绘制表示演员的纹理区域
        batch.draw(
                region,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth()*zoom, getHeight()*zoom,
                getScaleX(), getScaleY(),
                getRotation()
        );
        if(drawLoop){//如果循环,则添加一部分
            batch.draw(
                    region,
                    getX()+getMapW_px(), getY(),
                    getOriginX(), getOriginY(),
                    getWidth()*zoom, getHeight()*zoom,
                    getScaleX(), getScaleY(),
                    getRotation()
            );
        }

        // 还原 batch 原本的 Color
       // batch.setColor(tempBatchColor);
    }




    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public int getMapW_px() {
        return mapW_px;
    }

    public void setMapW_px(int mapW_px) {
        this.mapW_px = mapW_px;
    }

    public boolean isDrawLoop() {
        return drawLoop;
    }

    public void setDrawLoop(boolean drawLoop) {
        this.drawLoop = drawLoop;
    }

    public boolean isIfEable() {
        return ifEable;
    }

    public void setIfEable(boolean ifEable) {
        this.ifEable = ifEable;
    }

    public int getMapW() {
        return mapW;
    }

    public void setMapW(int mapW) {
        this.mapW = mapW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void reset() {
        ifEable=false;
    }

    private void initPotionById(float refx,float refy){
        /*coordMap=GameMap.getDownleftPotionById(mapW,mapH_px, scale,id,coordMap);
        this.sourceX=coordMap.get("sourceX")-refx*ResConfig.Map.MAP_SCALE;
        this.sourceY=coordMap.get("sourceY")-refy*ResConfig.Map.MAP_SCALE;*/
        int x=GameMap.getHX(id,mapW)+1;
        int y=GameMap.getHY(id,mapW)+1;
        this.sourceX=GameMap.getX_pxByHexagon(x,scale,0)-refx* ResDefaultConfig.Map.MAP_SCALE;
        this.sourceY=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true)-refy* ResDefaultConfig.Map.MAP_SCALE;

        //Gdx.app.log("initPotionById","id:"+id+" sourceX:"+sourceX+" sourceY:"+sourceY);
    }


}






























