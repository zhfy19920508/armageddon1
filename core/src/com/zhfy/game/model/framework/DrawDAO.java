package com.zhfy.game.model.framework;

import com.badlogic.gdx.graphics.Color;

public class DrawDAO {
    private TextureRegionDAO texture;
   // private String textureName;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private float lx_px;//绘制坐标
    private float ly_px;//绘制
    private float w_px;
    private float h_px;
    private float r;
    private float g;
    private float b;
    public boolean isVisable;


    public float getLx_px() {
        return lx_px;
    }
    public void setLx_px(float lx_px) {
        this.lx_px = lx_px;
    }
    public float getLy_px() {
        return ly_px;
    }
    public void setLy_px(float ly_px) {
        this.ly_px = ly_px;
    }

    /*public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }*/
    public void setTextureRegionDAO(TextureRegionDAO t){
        this.texture=t;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }


    public void setColor(Color color){
        setR(color.r);
        setG(color.g);
        setB(color.b);
    }
    public void setColor(float r,float g,float b){
        setR(r);
        setG(g);
        setB(b);
    }

    public TextureRegionDAO getTextureRegionDAO() {
        return texture;
    }

    public void setTexture(TextureRegionDAO texture) {
        this.texture = texture;
    }

    public float getW_px() {
        return w_px;
    }

    public void setW_px(float w_px) {
        this.w_px = w_px;
    }

    public float getH_px() {
        return h_px;
    }

    public void setH_px(float h_px) {
        this.h_px = h_px;
    }
}
