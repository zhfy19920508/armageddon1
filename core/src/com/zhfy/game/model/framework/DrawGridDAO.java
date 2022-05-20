package com.zhfy.game.model.framework;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

//循环绘制的功能
public class DrawGridDAO {
    private int draw_gx;
    private int draw_gy;
    private int draw_gd;

    private float dx_px;
    private float dy_px;
    
    private float back_x_px;
    private float back_y_px;
    private float back_w_px;
    private float back_h_px;
    
    private float fore_x_px;
    private float fore_y_px;
    private float fore_w_px;
    private float fore_h_px;
    
    private int regionId;//核心地块
    private String str;

    
    private TextureRegion backTile;
    private TextureRegion foreTile;
    private TextureRegion presetRailway;


    private float presetRailway_x_px;
    private float presetRailway_y_px;


    public TextureRegion getPresetRailway() {
        return presetRailway;
    }

    public void setPresetRailway(TextureRegion presetRailway) {
        this.presetRailway = presetRailway;
    }


    public float getPresetRailway_x_px() {
        return presetRailway_x_px;
    }

    public void setPresetRailway_x_px(float presetRailway_x_px) {
        this.presetRailway_x_px = presetRailway_x_px;
    }

    public float getPresetRailway_y_px() {
        return presetRailway_y_px;
    }

    public void setPresetRailway_y_px(float presetRailway_y_px) {
        this.presetRailway_y_px = presetRailway_y_px;
    }

    public int getDraw_gx() {
        return draw_gx;
    }
    
    public void setDraw_gx(int draw_gx) {
        this.draw_gx = draw_gx;
    }
    
    public int getDraw_gy() {
        return draw_gy;
    }
    
    public void setDraw_gy(int draw_gy) {
        this.draw_gy = draw_gy;
    }
    
    public int getDraw_gd() {
        return draw_gd;
    }
    
    public void setDraw_gd(int draw_gd) {
        this.draw_gd = draw_gd;
    }
    
    public TextureRegion getBackTile() {
        return backTile;
    }
    
    public void setBackTile(TextureRegion backTile) {
        this.backTile = backTile;
    }
    
    public TextureRegion getForeTile() {
        return foreTile;
    }
    
    public void setForeTile(TextureRegion foreTile) {
        this.foreTile = foreTile;
    }
    
    public float getBack_x_px() {
        return back_x_px;
    }
    
    public void setBack_x_px(float back_x_px) {
        this.back_x_px = back_x_px;
    }
    
    public float getBack_y_px() {
        return back_y_px;
    }
    
    public void setBack_y_px(float back_y_px) {
        this.back_y_px = back_y_px;
    }
    
    public float getBack_w_px() {
        return back_w_px;
    }
    
    public void setBack_w_px(float back_w_px) {
        this.back_w_px = back_w_px;
    }
    
    public float getBack_h_px() {
        return back_h_px;
    }
    
    public void setBack_h_px(float back_h_px) {
        this.back_h_px = back_h_px;
    }
    
    public float getFore_x_px() {
        return fore_x_px;
    }
    
    public void setFore_x_px(float fore_x_px) {
        this.fore_x_px = fore_x_px;
    }
    
    public float getFore_y_px() {
        return fore_y_px;
    }
    
    public void setFore_y_px(float fore_y_px) {
        this.fore_y_px = fore_y_px;
    }
    
    public float getFore_w_px() {
        return fore_w_px;
    }
    
    public void setFore_w_px(float fore_w_px) {
        this.fore_w_px = fore_w_px;
    }
    
    public float getFore_h_px() {
        return fore_h_px;
    }
    
    public void setFore_h_px(float fore_h_px) {
        this.fore_h_px = fore_h_px;
    }

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

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
