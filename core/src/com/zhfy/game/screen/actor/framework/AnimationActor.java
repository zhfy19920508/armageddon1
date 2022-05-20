package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.zhfy.game.MainGame;
import com.zhfy.game.screen.stage.SMapGameStage;

/**
 * 动画演员
 * 
 * @xietansheng
 */
public class AnimationActor extends ImageActor {

    private Animation animation;

    /** 是否播放动画, 如果不播放动画, 则固定显示指定的帧 */
    private boolean isPlayAnimation = true;
    
    /** 不播放动画时固定显示的帧索引 */
    private int fixedShowKeyFrameIndex;

    /** 时间步 delta 的累加值 */
    private float stateTime;

    private int animationW;
    private int animationH;

    public AnimationActor() {
    }

    public AnimationActor(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        this(new Animation(frameDuration, keyFrames));
    }
//TODO
    /*public AnimationActor(float frameDuration, TextureRegion... keyFrames) {
        this(new Animation(frameDuration, keyFrames));
    }*/


    public AnimationActor(Animation animation) {
    	setAnimation(animation);
      TextureRegion  region = (TextureRegion) animation.getKeyFrame(stateTime);
        animationW=region.getRegionWidth();
        animationH=region.getRegionHeight();
    }

    public void  setAnimation(Animation animation,float worldW,float worldH, Color color,boolean keepScale ){
        setAnimation(animation);
        TextureRegion  region = (TextureRegion) animation.getKeyFrame(stateTime);



        animationW=region.getRegionWidth();
        animationH=region.getRegionHeight();


        if(keepScale){
                float v=Math.max(worldW,worldH);
            setScaleXByAnimationW( v);
            setScaleYByAnimationH( v);
        }else{
            setScaleXByAnimationW( worldW);
            setScaleYByAnimationH( worldH);
        }

        setColor(color);
    }



    public int getAnimationW() {
        return animationW;
    }

    public void setScaleXByAnimationW(float animationW) {
        setScaleX(animationW*1f/this.animationW);
    }

    public int getAnimationH() {
        return animationH;
    }

    public void setScaleYByAnimationH(float animationH) {
        setScaleY(animationH*1f/this.animationH);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation != null) {
            TextureRegion region = null;
            if (isPlayAnimation) {
            	// 如果需要播放动画, 则累加时间步, 并按累加值获取需要显示的关键帧
                stateTime += delta;
                if(stateTime>animation.getKeyFrames().length&&animation.getPlayMode().equals(Animation.PlayMode.LOOP)){
                    stateTime=0;
                }
                region = (TextureRegion) animation.getKeyFrame(stateTime);
            } else {
            	// 不需要播放动画, 则获取 fixedShowKeyFrameIndex 指定的关键帧 
                TextureRegion[] keyFrames = (TextureRegion[]) animation.getKeyFrames();
                if (fixedShowKeyFrameIndex >= 0 && fixedShowKeyFrameIndex < keyFrames.length) {
                    region = keyFrames[fixedShowKeyFrameIndex];
                }
            }

            // 设置当前需要显示的关键帧
            setRegion(region);
            setHeight(0);
            setWidth(0);
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
        // 默认先显示第 0 帧
        /*if (this.animation != null) {
            TextureRegion[] keyFrames = (TextureRegion[]) this.animation.getKeyFrames();
            if (keyFrames.length > 0) {
                setRegion(keyFrames[0]);
            }
        }*/
    }

    public boolean isPlayAnimation() {
        return isPlayAnimation;
    }

    /**
     * 设置是否需要播放动画 <br/>
     * 
     * true: 按 Animation 对象指定的播放模式播放动画 <br/>
     * false: 不播放动画, 始终显示 fixedShowKeyFrameIndex 指定的关键帧 <br/>
     */
    public void setPlayAnimation(boolean isPlayAnimation) {
        this.isPlayAnimation = isPlayAnimation;
    }

    public int getFixedShowKeyFrameIndex() {
        return fixedShowKeyFrameIndex;
    }

    /**
     * 设置不播放动画时固定显示的关键帧索引
     */
    public void setFixedShowKeyFrameIndex(int fixedShowKeyFrameIndex) {
        this.fixedShowKeyFrameIndex = fixedShowKeyFrameIndex;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
       // super.draw(batch, parentAlpha);

        if (getRegion() == null || !isVisible()) {
            return;
        }
        float alphaFlash=parentAlpha;
        if(getIfFlash()){
            alphaFlash=  getCam().getAlphaFlash();
        }else{
            alphaFlash=parentAlpha;
        }

        //float zoomChange=0.5f;

       /* if(getIfFlash()){
            alphaFlash=  resource.getAlphaFlash();
          //  zoomChange= resource.getZoomChange();
        }*/


        // 备份 batch 原本的 Color
        //Color tempBatchColor = batch.getColor();
        // 将演员的 Color 结合 parentAlpha 设置到 batch
        Color tempBatchColor = getColor();
        if(tempBatchColor!=Color.WHITE){
            batch.setColor(tempBatchColor.r,tempBatchColor.g, tempBatchColor.b, tempBatchColor.a);
        }else{
            batch.setColor( batch.getColor().r,  batch.getColor().g,  batch.getColor().b, alphaFlash);
        }

        // 结合演员的属性绘制表示演员的纹理区域

        if(getCam()!=null){
            if(getCam().loopState==0){
                batch.draw(   getRegion(),  getX(), getY(), getOriginX(), getOriginY(), getRegion().getRegionWidth(),getRegion().getRegionHeight(), getScaleX(), getScaleY(), getRotation());
            }else if(getCam().loopState==1){
                batch.draw(   getRegion(),  getX(), getY(), getOriginX(), getOriginY(), getRegion().getRegionWidth(),getRegion().getRegionHeight(), getScaleX(), getScaleY(), getRotation());
                batch.draw(   getRegion(),  getX()+getCam().getMapW_px(), getY(), getOriginX(), getOriginY(), getRegion().getRegionWidth(),getRegion().getRegionHeight(), getScaleX(), getScaleY(), getRotation());

            }else if(getCam().loopState==2){
                batch.draw(   getRegion(),  getCam().getMapW_px()+getX(), getY(), getOriginX(), getOriginY(),  getRegion().getRegionWidth(),getRegion().getRegionHeight(), getScaleX(), getScaleY(), getRotation());
            }
        }else{
            batch.draw(   getRegion(),  getX(), getY(), getOriginX(), getOriginY(),  getRegion().getRegionWidth(),getRegion().getRegionHeight(), getScaleX(), getScaleY(), getRotation());
        }


        // 还原 batch 原本的 Color
        batch.setColor(Color.WHITE);





    }
}























