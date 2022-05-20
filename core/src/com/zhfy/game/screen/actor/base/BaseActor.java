package com.zhfy.game.screen.actor.base;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BaseActor extends Actor {

    private TextureRegion region;

    public BaseActor(TextureRegion region) {
        super();
        this.region = region;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
    }
    public BaseActor(Texture region) {
        super();
        this.region = new TextureRegion(region);
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
    }


    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (region == null || !isVisible()) {
            return;
        }
        batch.draw(
                region, 
                getX(), getY(), 
                getOriginX(), getOriginY(), 
                getWidth(), getHeight(), 
                getScaleX(), getScaleY(), 
                getRotation()
        );
    }
}