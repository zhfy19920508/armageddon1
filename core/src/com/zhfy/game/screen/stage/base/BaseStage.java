package com.zhfy.game.screen.stage.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.screen.actor.framework.EffectActorListDAO;


/**
 * 舞台基类
 *
 * @author xietansheng
 */
public class BaseStage extends Stage {

    private final Vector2 tempCoords = new Vector2();
    private MainGame mainGame;

    /** 舞台是否可见（是否更新和绘制） */
    private boolean visible = true;

    public BaseStage(MainGame mainGame, Viewport viewport) {
        super(viewport);
        this.mainGame = mainGame;
    }

    public BaseStage(MainGame mainGame, Viewport viewport,Batch batch) {
        super(viewport,batch);
        this.mainGame = mainGame;
    }
    public MainGame getMainGame() {
        return mainGame;
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void draw() {
        try {
            super.draw();
        } catch (Exception e) {
           /* if(ResDefaultConfig.ifDebug){
                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
            }*/
            Gdx.app.error("Base stage draw","");
            super.getBatch().end();
        }

    }

    public void clearNullActor() {
        Array<Actor> actors =getActors();
        for (int i = actors.size-1; i>=0; i--) {
            Actor a=actors.get(i);
            if(a==null){
                this.getActors().removeIndex(i);
            }
        }
    }

    @Override
    public Actor hit(float stageX, float stageY, boolean touchable) {
        try {
            getRoot().parentToLocalCoordinates(tempCoords.set(stageX, stageY));
            return getRoot().hit(tempCoords.x, tempCoords.y, touchable);
        } catch (Exception e) {
            Gdx.app.error("Base stage hit","");
            //getRoot().removeActor(this);
            Array<Actor> actors=getActors();
            for (int i = actors.size - 1; i >= 0; i--) {
                Actor child = actors.get(i);
                if(child==null){
                    actors.removeIndex(i);
                }
            }
           return null;
        }
    }
}
















