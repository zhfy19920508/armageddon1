package com.zhfy.game.screen.actor.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.zhfy.game.model.framework.TextureRegionDAO;

/**
 * 游戏中的演员,适用于;ui
 *
 */
public class UIActor extends Actor  implements Pool.Poolable {

    public boolean ifShow;
    private TextureRegion region;

    private float zoom;
    private int id;//
    private String name;
    private int index;//判断位置

    public UIActor(TextureRegionDAO regionDAO , float x, float y, int id, float zoom) {
        ifShow=true;
        this.region=regionDAO.getTextureRegion();
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        this.id=id;
        this.zoom=zoom;
        setX(x);
        setY(y);
    }

    public UIActor(){}

    public UIActor(float x,float y,float zoom){
        this.zoom=zoom;
        setX(x);
        setY(y);
    }


    public void init(TextureRegionDAO regionDAO,float x,float y, int id,float zoom){
        //imgLists.getTextureByName("task_battle")
        ifShow=true;
        this.region=regionDAO.getTextureRegion();
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        this.id=id;
        this.zoom=zoom;
        setWidth(region.getRegionWidth());
        setHeight(region.getRegionHeight());
        setX(x);
        setY(y);
       // Gdx.app.log("imgActor","id:"+id+" x:"+getHX()+" y:"+getHY());
    }

    public void init(TextureRegionDAO regionDAO, int id){
        //imgLists.getTextureByName("task_battle")
        ifShow=true;
        this.zoom=1f;
        this.region=regionDAO.getTextureRegion();
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        this.id=id;
        setWidth(region.getRegionWidth());
        setHeight(region.getRegionHeight());
        // Gdx.app.log("imgActor","id:"+id+" x:"+getHX()+" y:"+getHY());
    }



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
        if (region == null || !isVisible()||!isIfShow()) {
            return;
        }
        // 结合演员的属性绘制表示演员的纹理区域
        batch.draw(
                region,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth()*zoom, getHeight()*zoom,
                getScaleX(), getScaleY(),
                getRotation()
        );

        // 还原 batch 原本的 Color
       // batch.setColor(tempBatchColor);
    }




    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void reset() {
        ifShow=false;
    }

    public boolean isIfShow() {
        return ifShow;
    }

    public void setIfShow(boolean ifShow) {
        this.ifShow = ifShow;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}






























