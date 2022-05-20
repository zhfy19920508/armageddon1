package com.zhfy.game.screen.actor.chFrame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

/** <pre>
  * 二次封装的actor
  * 
  * date: 2014-12-11
  * </pre>
  * 
  * @author caohao */
public class CHActor extends Actor implements Poolable {
private int tag;

private Texture bgTexture;

private TextureRegion bgTextureRegion;

private Sprite sprite;

public CHActor () {
        }

/**
  * 绑定一个精灵，此时CHActor的属性会同步到精灵上。
  * @param sprite
  */
public void bindSprite (Sprite sprite) {
        this.sprite = sprite;
        setSize(this.sprite.getWidth(), this.sprite.getHeight());
        setOrigin(Align.center);
        }

@Override
public void draw (Batch batch, float parentAlpha) {
        if (clipRectangleArr.size > 0) {
        for (Rectangle cRectangle : clipRectangleArr) {
        this.drawCore(batch, parentAlpha, cRectangle);
        }
        } else {
        this.drawCore(batch, parentAlpha, null);
        }
        }

/**
  * 核心绘制
  * @param batch
  * @param parentAlpha
  * @param clipRectangle
  */
private void drawCore (Batch batch, float parentAlpha, Rectangle clipRectangle) {
        boolean clipok = false;
        // 开始裁剪
        if (clipRectangle != null) {
        batch.flush(); // 绘制之前添加的元素，如果不添加此处代码，后面的裁剪会导致之前的纹理也会被裁剪
        clipok = clipBegin(getX() + clipRectangle.x, getY() + clipRectangle.y, clipRectangle.width, clipRectangle.height);
        }

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);

        float x = getX();
        float y = getY();
        float scaleX = getScaleX();
        float scaleY = getScaleY();

        float width = getWidth();
        float height = getHeight();

        if (bgTexture != null) {
        batch.draw(bgTexture, x, y, getOriginX(), getOriginY(), getWidth(), getHeight(), scaleX, scaleY, getRotation(), 0, 0,
        (int)width, (int)height, false, false);
        }

        if (bgTextureRegion != null) {
        if (bgTextureRegion instanceof Sprite) {
        Sprite sprite = (Sprite)bgTextureRegion;
        sprite.setColor(batch.getColor());
        sprite.setOrigin(getOriginX(), getOriginY());
        sprite.setPosition(x, y);
        sprite.setScale(scaleX, scaleY);
        sprite.setSize(width, height);
        sprite.setRotation(getRotation());
        sprite.draw(batch);
        } else {
        batch.draw(bgTextureRegion, x, y, getOriginX(), getOriginY(), width, height, scaleX, scaleY, getRotation());
        }
        }

        if (sprite != null) {
        sprite.setColor(color);
        sprite.setOrigin(getOriginX(), getOriginY());
        sprite.setPosition(x, y);
        sprite.setScale(scaleX, scaleY);
        sprite.setSize(width, height);
        sprite.setRotation(getRotation());
        sprite.draw(batch);
        }

        // 绘制完背景后进行其他内容绘制
        drawAfterBg(batch);

        // 提交裁剪内容
        if (clipok) {
        batch.flush();
        clipEnd();
        }
        }

public void drawAfterBg (Batch batch) {
        };

public void setBgTexture (Texture bgTexture) {
        this.bgTexture = bgTexture;
        if (bgTexture != null) {
        setSize(bgTexture.getWidth(), bgTexture.getHeight());
        }
        setOrigin(Align.center);
        }

/** <pre>
  * 使用缓存池
  * 
  * date: 2015-1-3
  * </pre>
  * 
  * @author caohao
  * @return */
@SuppressWarnings("unchecked")
public static <T extends CHActor> T obtain (Class<T> type) {
        Pool<CHActor> pool = (Pool<CHActor>)Pools.get(type);
        CHActor actor = pool.obtain();
        actor.setBgTexture(null);
        return (T)actor;
        }

public static CHActor obtain () {
        return obtain(CHActor.class);
        }

@Override
public void reset () {
        this.bgTexture = null;
        this.bgTextureRegion = null;
        clipRectangleArr.clear();
        setScale(1);
        setRotation(0);
        clear();
        setUserObject(null);
        this.setColor(new Color(1, 1, 1, 1));
        setStage(null);
        setParent(null);
        setVisible(true);
        setName(null);
        setOrigin(Align.center);
        setPosition(0, 0);
        }

public Texture getBgTexture () {
        return bgTexture;
        }

public TextureRegion getBgTextureRegion () {
        return bgTextureRegion;
        }

public void setBgTextureRegion (TextureRegion textureRegion) {
        this.bgTextureRegion = textureRegion;
        if (bgTextureRegion != null) {
        if (bgTextureRegion instanceof Sprite) {
        Sprite sprite = (Sprite)bgTextureRegion;
        setSize(sprite.getWidth(), sprite.getHeight());
        } else if (bgTextureRegion instanceof AtlasRegion) {
       // AtlasRegion atlasRegion = (AtlasRegion)bgTextureRegion;
       // bgTextureRegion = Chao.plistCenter.createSprite(atlasRegion);
        Sprite sprite = (Sprite)bgTextureRegion;
        setSize(sprite.getWidth(), sprite.getHeight());
        } else {
        setSize(bgTextureRegion.getRegionWidth(), bgTextureRegion.getRegionHeight());
        }
        }

        setOrigin(Align.center);
        }

@Override
public boolean remove () {
        boolean remove = super.remove();
        if (remove) {
        Pools.free(this);
        }
        return remove;
        }

public int getTag () {
        return tag;
        }

public void setTag (int tag) {
        this.tag = tag;
        }

private Array<Rectangle> clipRectangleArr = new Array<Rectangle>();

/**
  * 添加裁剪矩形，范围为当前Actor的显示区域即：(0,0)~(w,h) 原点为左下角
  * @param rectangle
  */
public void addClipRectangle (Rectangle rectangle) {
        clipRectangleArr.add(rectangle);
        }

public Array<Rectangle> getClipRectangleArr () {
        return clipRectangleArr;
        }

public Sprite getSprite () {
        return sprite;
        }

private Rectangle boundsRectangle;

@Override
protected void sizeChanged () {
        if (boundsRectangle == null) {
        boundsRectangle = new Rectangle();
        }
        boundsRectangle.width = getWidth();
        boundsRectangle.height = getHeight();
        }

/**
  * 用于碰撞检测的矩形区域
  * @return
  */
public Rectangle getBoundsRectangle () {
        boundsRectangle.x = getX();
        boundsRectangle.y = getY();
        return boundsRectangle;
        }

        }