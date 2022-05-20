package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.zhfy.game.MainGame;
import com.zhfy.game.framework.GameUtil;

/**
 *
 * <pre>
 * 二次封装的actor
 *
 * date: 2014-12-11
 * </pre>
 BaseActor chactor  = BaseActor.obtain();// 使用缓存池里面的

 BaseActor chactor = new BaseActor();// 纯新建

 YourActor extends BaseActor {...}

 YourActor youractor  = BaseActor.obtain(YourActor.class);
 */
public class BaseActor extends Actor implements Poolable {


    public BaseActor() {
    }

    private MainGame mainGame;

    public BaseActor(MainGame mainGame) {
        this.mainGame = mainGame;
    }



    public MainGame getMainGame() {
        return mainGame;
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
    }



    /**
     *
     * <pre>
     * 使用缓存池
     *
     * date: 2015-1-3
     * </pre>
     *
     * @author caohao
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseActor> T obtain(Class<T> type) {
        Pool<BaseActor> pool = (Pool<BaseActor>) Pools.get(type);
        BaseActor actor = pool.obtain();
        return (T) actor;
    }

    public static BaseActor obtain() {
        return obtain(BaseActor.class);
    }

    @Override
    public void reset() {
        // 初始化
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





    @Override
    public boolean remove() {
        boolean remove = super.remove();
        if (remove) {
            Pools.free(this);
        }
        return remove;
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



    @Override
    protected void drawDebugBounds(ShapeRenderer shapes) {
        //super.drawDebugBounds(shapes);
        GameUtil.drawActorBounds(this,shapes);
    }
}