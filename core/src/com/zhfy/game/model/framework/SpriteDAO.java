package com.zhfy.game.model.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteDAO {
    private Sprite  sprite ;
    private TextureRegion region;
    private String name ;
    private int height;
    private int width;
    private int refx ;
    private int refy ;
    

    public SpriteDAO ( Sprite  sprite, String name,int refx, int refy){
       this.sprite=sprite ;
        this.name=name ;
        this.refx=refx ;
        this.refy =refy;
        this.height=sprite.getRegionHeight();
        this.width=sprite.getRegionWidth();
    }
    public SpriteDAO(){

    }
    public SpriteDAO ( TextureRegionDAO regionDAO){
        this.sprite=new Sprite(regionDAO.getTextureRegion()) ;
        this.region=regionDAO.getTextureRegion();
        this.name=regionDAO.getName() ;
        this.refx=regionDAO.getRefx() ;
        this.refy =regionDAO.getRefy();
        this.height=sprite.getRegionHeight();
        this.width=sprite.getRegionWidth();
    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Sprite getSprite() {
        return sprite;
    }


    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    public int getRefx() {
        return refx;
    }
    public void setRefx(int refx) {
        this.refx = refx;
    }
    public int getRefy() {
        return refy;
    }
    public void setRefy(int refy) {
        this.refy = refy;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public TextureRegion getRegion() {
        return region;
    }
}
