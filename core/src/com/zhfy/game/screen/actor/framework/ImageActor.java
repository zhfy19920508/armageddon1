package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.screen.stage.SMapGameStage;

/**
 * 自定义演员
 *
 * @xietansheng
 */
public class ImageActor extends BaseActor {
    private int drawMethod;//0 默认 1平铺
    private int iW;
    private int iH;
    private TextureRegion region;
    private float refx;
    private float refy;
    private boolean ifFlash;
    private CamerDAO cam;
    private int loopState=0;//这个只有在cam无效的情况下才有效
    private float loopW=0;


    public void setLoop(int loopState,float loopW){
        this.loopState=loopState;
        this.loopW=loopW;
    }

    public void setLoopState(int loopState){
        this.loopState=loopState;
    }

    public ImageActor() {
    }

    public ImageActor(TextureRegion region) {
        this.region = region;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        this.loopState=0;
        this.loopW=0;
    }

    public ImageActor(CamerDAO cam,TextureRegionDAO regionDAO, float scale) {
        this.region = regionDAO.getTextureRegion();
        this.refx=regionDAO.getRefx();
        this.refy=regionDAO.getRefy();
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        this.setScale(scale);
        this.cam=cam;
        this.loopState=0;
        this.loopW=0;
    }

    public void SetTextureRegionDAO(TextureRegionDAO regionDAO){
        this.region = regionDAO.getTextureRegion();
        this.refx=regionDAO.getRefx()*getScaleX();
        this.refy=regionDAO.getRefy()*getScaleX();
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
    }

    public float getRefx() {
        return refx;
    }

    public void setRefx(float refx) {
        this.refx = refx;
    }

    public float getRefy() {
        return refy;
    }

    public void setRefy(float refy) {
        this.refy = refy;
    }

    public boolean isIfFlash() {
        return ifFlash;
    }

    public void setIfFlash(boolean ifFlash) {
        this.ifFlash = ifFlash;
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
    public void setRegionWidth(int width){
        region.setRegionWidth(width);
    }
    public int getRegionWidth(){
        return region.getRegionWidth();
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (region == null || !isVisible()) {
            return;
        }
        float alphaFlash=1.0f;
        if(ifFlash){
            alphaFlash= cam.getAlphaFlash();
        }else{
            alphaFlash=parentAlpha;
        }
        // 备份 batch 原本的 Color
        //Color tempBatchColor = batch.getColor();
        // 将演员的 Color 结合 parentAlpha 设置到 batch
        Color tempBatchColor = getColor();
       if(tempBatchColor!=Color.WHITE){
           batch.setColor(tempBatchColor.r,tempBatchColor.g, tempBatchColor.b, tempBatchColor.a);
       }else{
           batch.setColor( batch.getColor().r,  batch.getColor().g,  batch.getColor().b, alphaFlash);
       }
        int loopState=0;
        float loopW=0;
        if(cam!=null){ // 结合演员的属性绘制表示演员的纹理区域
            loopState=cam.loopState;
            loopW=cam.getMapW_px();
        }else{
            loopState=this.loopState;
            loopW=this.loopW;
        }
        if(drawMethod==1){//平铺绘制方法

            for(int w=0;w<iW;w++){
                for(int h=0;h<iH;h++){
                    if(loopState==0){
                        batch.draw(   region,  getX()+w*region.getRegionWidth(), getY()+h*region.getRegionHeight(), getOriginX(), getOriginY(),region.getRegionWidth(), region.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
                    }else if(loopState==1){
                        batch.draw(   region,  getX()+w*region.getRegionWidth(), getY()+h*region.getRegionHeight(), getOriginX(), getOriginY(),  region.getRegionWidth(), region.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
                        batch.draw(   region,  getX()+w*region.getRegionWidth()+loopW, getY()+h*region.getRegionHeight(), getOriginX(), getOriginY(), region.getRegionWidth(), region.getRegionHeight(), getScaleX(), getScaleY(), getRotation());

                    }else if(loopState==2){
                        batch.draw(   region,  loopW+getX()+w*region.getRegionWidth(), getY()+h*region.getRegionHeight(), getOriginX(), getOriginY(), region.getRegionWidth(), region.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
                    }
                }
            }
        }else {
            if(loopState==0){
                batch.draw(   region,  getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            }else if(loopState==1){
                batch.draw(   region,  getX(), getY(), getOriginX(), getOriginY(),  getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
                batch.draw(   region,  getX()+loopW, getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            }else if(loopState==2){
                batch.draw(   region,  loopW+getX(), getY(), getOriginX(), getOriginY(),  getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            }
        }



        // 还原 batch 原本的 Color
        batch.setColor(Color.WHITE);
    }

    /****** 下面是便捷设置坐标的方法封装 ******/

    /**
     * 设置上边界的坐标
     * @param topY
     */
    public void setTopY(float topY) {
        setY(topY - getHeight());
    }

    /**
     * 获取上边界的坐标
     * @return
     */
    public float getTopY() {
        return getY() + getHeight();
    }

    /**
     * 设置右边界的坐标
     * @param rightX
     */
    public void setRightX(float rightX) {
        setX(rightX - getWidth());
    }

    /**
     * 获取右边的坐标
     * @return
     */
    public float getRightX() {
        return getX() + getWidth();
    }

    /**
     * 设置中心点坐标
     * @param centerX
     * @param centerY
     */
    public void setCenter(float centerX, float centerY) {
        setCenterX(centerX);
        setCenterY(centerY);
    }

    /**
     * 设置水平方向中心点坐标
     * @param centerX
     */
    public void setCenterX(float centerX) {
        if(refx==0){
            setX(centerX - getWidth() / 2.0F);
        }else {
            setX(centerX-refx);
        }

    }

    /**
     * 设置竖直方向中心点坐标
     * @param centerY
     */
    public void setCenterY(float centerY) {
        if(refy==0){
            setY(centerY - getHeight() / 2.0F);
        }else {
            setY(centerY-refy);
        }
    }

    public void setRepeatFlashAction(float duration) {

        SequenceAction sequence = Actions.sequence(
                Actions.alpha(0.0F, duration),
                Actions.alpha(1.0F, duration)
        );
        RepeatAction repeatAction = Actions.forever(sequence);
        addAction(repeatAction);
    }

    public void addChangeSizeAction(float oX, float oY, float minScale, float maxScale, float time) {

        this.setOrigin(oX, oY);
        time=time/2;
        SequenceAction sequence = Actions.sequence(
                Actions.scaleTo(minScale*getScaleX(), minScale*getScaleY(), time),
                Actions.scaleTo(maxScale*getScaleX(), maxScale*getScaleY(), time)
        );
        RepeatAction repeatAction = Actions.forever(sequence);
        addAction(repeatAction);
    }

    public boolean getIfFlash(){
        return ifFlash;
    }

    public CamerDAO getCam(){
        return cam;
    }

    public void setTitle(float worldWidth, float worldHeight) {
        setSize(worldWidth,worldHeight);
        drawMethod=1;
         iW= (int) (1+(getWidth()/region.getRegionWidth()));
         iH= (int) (1+(getHeight()/region.getRegionHeight()));
    }

    public float getLoopW() {
        return loopW;
    }
}






























