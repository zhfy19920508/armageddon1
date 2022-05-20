package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.zhfy.game.MainGame;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.TextureRegionDAO;

public class ComActor extends  BaseActor implements Pool.Poolable {

  /* 可以直接使用该类来在batch中绘制
   ShapeDrawer drawer = new ShapeDrawer(batch, region);
            batch.begin();
drawer.line(0, 0, 100, 100);
batch.end();*/
    private MainGame game;
    private TextureRegion region;
    public TextureRegionDAO textureRegionDAO;
    private TextureRegion bgRegion;
    public TextureRegionDAO bgTextureRegionDAO;
    private int mapW_px;
    public boolean alive;
    private CamerDAO cam;

    public ComActor(){
        this.alive = false;

    }

    public ComActor(MainGame game,CamerDAO cam, int mapW_px , String regionName) {
        super();
        this.game=game;
        this.textureRegionDAO = game.getImgLists().getTextureByName(regionName);
        this.region= textureRegionDAO.getTextureRegion();
        this.mapW_px=mapW_px;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        setColor(Color.WHITE);
        this.alive =true;
        this.cam =cam;
    }

    public void init(MainGame game,CamerDAO cam, int mapW_px ,String regionName) {
        this.game=game;
        this.textureRegionDAO = game.getImgLists().getTextureByName(regionName);
        this.region= textureRegionDAO.getTextureRegion();
        this.mapW_px=mapW_px;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        setColor(Color.WHITE);
        //clearActions();
        this.alive =true;
        this.cam =cam;
    }

    public void init(MainGame game,CamerDAO cam, int mapW_px ,String regionName,boolean ifFlip) {
        this.game=game;
        if(ifFlip){
            this.textureRegionDAO = game.getImgLists().getFilpRegionDAO(regionName);
        }else {
            this.textureRegionDAO = game.getImgLists().getTextureByName(regionName);
        }
        this.region= textureRegionDAO.getTextureRegion();
        this.mapW_px=mapW_px;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        setColor(Color.WHITE);
        //clearActions();
        this.alive =true;
        this.cam =cam;
    }
    public void init(MainGame game, int mapW_px ,String regionName,float scale) {
        this.game=game;
        this.textureRegionDAO = game.getImgLists().getTextureByName(regionName);
        this.region= textureRegionDAO.getTextureRegion();
        this.mapW_px=mapW_px;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        setColor(Color.WHITE);
        //clearActions();
        this.alive =true;
        setScale(scale);
    }

    public void setBgRegion(String regionName){
        if(regionName==null){
            bgTextureRegionDAO =null;
            this.bgRegion =null;
        }else {
            bgTextureRegionDAO = game.getImgLists().getTextureByName(regionName);
            this.bgRegion = bgTextureRegionDAO.getTextureRegion();
        }
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
        boolean batchBlendState=batch.isBlendingEnabled();

        /*
         * 先把 batch 原本的 color 保存起来, 因为 batch 是从外部传进来的, 最好不要改变它原本的状态,
         * 但在这里需要重新设置 batch 的 color, 所以先保存起来, 等当前方法执行完时再将 batch 原本的 color 设置回去。
         */
        Color tempBatchColor = batch.getColor();

        /*
         * 实际上演员并没有单独的 alpha 属性, alpha 包含在颜色(color)属性中, rgba color 中的 a 表示 alpha;
         * 演员有 alpha 值, 而父节点(舞台/演员组)中也有 alpha 值(parentAlpha)。 由于最终在演员节点中才真正把纹理
         * 绘制在屏幕上, 才是真正绘制的地方, 而父节点一般用于组织演员, 不会直接绘制任何纹理, 透明度 alpha 值只有在绘制
         * 时才能体现出来, 所以父节点无法体现自己的 alpha 值, 因此父节点会将自己的 alpha 值(就是draw方法中的参数 parentAlpha)
         * 传递给它自己的所有子节点，即最终直接绘制纹理的演员, 让演员结合自身的 alpha 值在绘制时综合体现。
         */

        // 获取演员的 color 属性
        Color color = getColor();

        /*
         * 处理 color/alpha 属性, 即将演员的 rgba color 设置到纹理画布 batch。
         * 其中的 alpha 需要结合演员和父节点的 alpha, 即演员的 alpha 与父节点的 alpha 相乘,
         * 例如父节点的 alpha 为 0.5, 演员的 alpha 为 0.5, 那么最终的显示效果就是 0.5 * 0.5 = 0.25
         */
       batch.setColor(color.r, color.g, color.b,  color.a);
        if(cam!=null){
            if(cam.loopState==0||cam.loopState==1) {
                // 处理 位置, 缩放和旋转的支点, 尺寸, 缩放比, 旋转角度
                if(bgRegion !=null){
                    batch.draw(
                            bgRegion,
                        //    getX()-textureRegionDAO.getRefx()*getScaleX() +(bgTextureRegionDAO.getRefx()-textureRegionDAO.getRefx()) *getScaleX(), getY()-textureRegionDAO.getRefy()*getScaleY()+(bgTextureRegionDAO.getRefy()-textureRegionDAO.getRefy())*getScaleY(),
                            getX()+ textureRegionDAO.getRefx()- bgTextureRegionDAO.getRefx(),getY()+ textureRegionDAO.getRefy()- bgTextureRegionDAO.getRefy(),
                            getOriginX()-textureRegionDAO.getRefx()+bgTextureRegionDAO.getRefx(),getOriginY()-textureRegionDAO.getRefy()+bgTextureRegionDAO.getRefy(),
                            bgRegion.getRegionWidth(), bgRegion.getRegionHeight(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                batch.draw(
                        region,
                        getX(), getY(),
                        getOriginX(), getOriginY(),
                        region.getRegionWidth(), region.getRegionHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
            if(cam.loopState==2||cam.loopState==1) {
                if(bgRegion !=null){
                    batch.draw(
                            bgRegion,
                            mapW_px+getX()-textureRegionDAO.getRefx()*getScaleX() +(textureRegionDAO.getRefx()*2-bgTextureRegionDAO.getRefx())/2 *getScaleX(), getY()-textureRegionDAO.getRefy()*getScaleY()+(textureRegionDAO.getRefy()*2-bgTextureRegionDAO.getRefy())/2*getScaleY(),
                            getOriginX(), getOriginY(),
                            bgRegion.getRegionWidth(), bgRegion.getRegionHeight(),
                            getScaleX(), getScaleY(),
                            getRotation()
                    );
                }
                batch.draw(
                        region,
                        mapW_px+getX(), getY(),
                        getOriginX(), getOriginY(),
                        region.getRegionWidth(), region.getRegionHeight(),
                        getScaleX(), getScaleY(),
                        getRotation()
                );
            }
        }else {
            batch.draw(
                    region,
                    getX(), getY(),
                    getOriginX(), getOriginY(),
                    getWidth(), getHeight(),
                    getScaleX(), getScaleY(),
                    getRotation()
            );
        }


        batch.setColor(color.r, color.g, color.b,  parentAlpha);

        // 将 batch 原本的 color 设置回去
        batch.setColor(tempBatchColor);

        if(batchBlendState){
            batch.enableBlending();
        }else {
            batch.disableBlending();
        }
    }


    @Override
    public void reset() {
        setBgRegion(null);
        setVisible(false);
        setRotation(0);
        setOriginY(0);
        setOriginX(0);
        alive=false;
    }

    //会把中心点设置图片的中心
    public void setCenterPotionAndOriginByTextureRegionRef(float sourceX, float sourceY){
        setX(sourceX- textureRegionDAO.getRefx());
        setY(sourceY- textureRegionDAO.getRefy());
        setOrigin(textureRegionDAO.getRefx(), textureRegionDAO.getRefy());
    }
//需要提前设置中心点
    public void setCenterPotionByHaveOrigin(float sourceX, float sourceY){
        setX(sourceX- getOriginX());
        setY(sourceY- getOriginY());
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
        setX(centerX - getWidth() / 2.0F);
    }

    /**
     * 设置竖直方向中心点坐标
     * @param centerY
     */
    public void setCenterY(float centerY) {
        setY(centerY - getHeight() / 2.0F);
    }



}
