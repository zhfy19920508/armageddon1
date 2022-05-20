package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.LabelList;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.DrawDAO;
import com.zhfy.game.model.framework.DrawListDAO;
import com.zhfy.game.model.framework.SpriteDAO;
import com.zhfy.game.model.framework.SpriteListDAO;

import java.util.Iterator;

public class SMapFVActor extends BaseActor {
    //用来显示一些前景特效




    private int w;
    private int h;
    private int i,iMax;
    private CamerDAO cam;

    private float x;
    private float y;
    private float zoom;

    private float vx;
    private float vy;

    private AssetManager am;
    private int screenId;
    private boolean ifNeedUnload;//是否需要卸载,在资源加载完后卸载,如果未加载完退出,在退出时顺带清理资源
    private LabelList damageLabels;
    private int mapTimeMax;

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    private DrawListDAO drawMarkList;
    private DrawDAO drawMark;
    private int drawI,drawIMax;
    //private SpriteListDAO arrowSprite;

  //  public DrawDAO airRangeMark;
    public float airRangeScale;
    public float airRangeScale2;//用来画安全区域
    public float airRangeX;
    public float airRangeY;
    public float airRangeLength;
    public float airRangeLength2;


    // 颜色层
    //private Texture textureGrid;
    private SpriteDAO spriteGrid;
    private SpriteListDAO spriteGrids;
    private int gw;
    private int gh;
    private MainGame game;
    private Fb2Smap smapDao;

    //计时器
    private float cycleActDeltaSum;
    private float airCycleActDeltaSum;
    private boolean actDeltaSumD;
    private float scale;

    public SMapFVActor(MainGame game, Stage stage, CamerDAO cam, int screenId, Fb2Smap smapDao, float scale, int mapW_px, int mapH_px) {
        super();
        this.smapDao=smapDao;
        this.scale=scale;
        this.game=game;
        this.am=game.getAssetManager();
        ifNeedUnload=false;
        this.screenId=screenId;
        drawMarkList =new DrawListDAO();
        this.x=0;
        this.y=0;
        this.zoom=1f;
        this.cam=cam;
        this.w = smapDao.masterData.getWidth();
        this.h = smapDao.masterData.getHeight();
       // circleMark=new DrawDAO();
       // circleMark.setTextureRegionDAO(game.getImgLists().getTextureByName("mark_attackCircle"));
       // circleMark.setColor(Color.WHITE);

        damageLabels=new LabelList(stage,game.gameConfig.gameFont, 1f);

    }



    @Override
    public void act(float delta) {
        super.act(delta);

            if(actDeltaSumD){
                cycleActDeltaSum +=delta;
                if(cycleActDeltaSum >1){
                    actDeltaSumD=false;
                }
            }else{
                cycleActDeltaSum -=delta;
                if(cycleActDeltaSum <0){
                    actDeltaSumD=true;
                }
            }
        airCycleActDeltaSum +=delta;
            if(airCycleActDeltaSum >4){
                airCycleActDeltaSum =0;
            }

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (/*spriteMain == null ||*/ !isVisible()) {
            return;
        }
        {

             // Color color = getColor();
         //   game.defaultFont.getData().setScale(0.5f);
         //   game.defaultFont.setColor(Color.RED);

            Color   color = getColor();
            float zoom=0.7f+ cycleActDeltaSum /3;
         //   float alpha=(actDeltaSum/2+0.5f);
            if (drawMarkList != null && drawMarkList.size() > 0) {

                for (drawI = 0, drawIMax = drawMarkList.size(); drawI < drawIMax; drawI++) {
                    DrawDAO d=drawMarkList.get(drawI) ;
                    batch.setColor(d.getR(), d.getG(), d.getB(), zoom);
                    if(cam.loopState==0||cam.loopState==1){

                    batch.draw(d.getTextureRegionDAO().getTextureRegion(), getX() + d.getLx_px(), getY() + d.getLy_px(), d.getTextureRegionDAO().getRefx(), d.getTextureRegionDAO().getRefy(),d.getW_px(),d.getH_px(),
                            zoom, zoom, getRotation());
                    /*if(ResDefaultConfig.ifDebug){
                        game.defaultFont.draw(batch,drawMarkList.get(drawI).getId()+"",getX() + drawMarkList.get(drawI).getLx_px()+15, getY() + drawMarkList.get(drawI).getLy_px()+30 );
                    }*/
               }
                    if(cam.loopState==1||cam.loopState==2) {
                        batch.draw(d.getTextureRegionDAO().getTextureRegion(),  getX() + cam.getMapW_px()+getX() + d.getLx_px(), getY() + d.getLy_px(), d.getTextureRegionDAO().getRefx(), d.getTextureRegionDAO().getRefy(),d.getW_px(),d.getH_px(),
                                zoom, zoom, getRotation());
                    }
                   /* if(ResDefaultConfig.ifDebug){
                        game.defaultFont.draw(batch,drawMarkList.get(drawI).getId()+"", getX() + cam.getMapW_px() * this.zoom +getX() + drawMarkList.get(drawI).getLx_px()+15, getY() + drawMarkList.get(drawI).getLy_px()+30 );
                    }*/

                    }
                }


            //绘制飞机范围
            /*if(circleMark.isVisable){
                batch.setColor(circleMark.getR(), circleMark.getG(), circleMark.getB(), zoom);
                if(cam.loopState==0||cam.loopState==1){
                    batch.draw(circleMark.getTextureRegionDAO().getTextureRegion(), getX() + circleMark.getLx_px(), getY() + circleMark.getLy_px(), circleMark.getTextureRegionDAO().getRefx(), circleMark.getTextureRegionDAO().getRefy(),circleMark.getTextureRegionDAO().getW(),circleMark.getTextureRegionDAO().getH(),
                            circleMarkScale, circleMarkScale, airCycleActDeltaSum *90);
                }
                if(cam.loopState==1||cam.loopState==2) {
                    batch.draw(circleMark.getTextureRegionDAO().getTextureRegion(), cam.getMapW_px()+getX() + circleMark.getLx_px(), getY() + circleMark.getLy_px(), circleMark.getTextureRegionDAO().getRefx(), circleMark.getTextureRegionDAO().getRefy(),circleMark.getTextureRegionDAO().getW(),circleMark.getTextureRegionDAO().getH(),
                            circleMarkScale, circleMarkScale, airCycleActDeltaSum *90);
                }
            }*/
            if(airRangeScale>0){
                batch.end();
                //batch.setColor(Color.WHITE.r,Color.WHITE.g,Color.WHITE.b,Color.WHITE.a/2);
                Gdx.gl.glEnable(GL30.GL_BLEND);
                Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
                game.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                game.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
                game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                game.shapeRenderer.setColor(Color.DARK_GRAY.r,Color.DARK_GRAY.g,Color.DARK_GRAY.b,Color.DARK_GRAY.a/5f);
                if(cam.loopState==0||cam.loopState==1){
                    game.shapeRenderer.circle(getX()+airRangeX,getY()+airRangeY,airRangeLength);
                }
                if(cam.loopState==1||cam.loopState==2){
                    game.shapeRenderer.circle(getX()+cam.getMapW_px()+airRangeX,getY()+airRangeY,airRangeLength);
                }
                if(airRangeScale2>0){//绘制射程安全区域
                    game.shapeRenderer.setColor(Color.GREEN.r,Color.GREEN.g,Color.GREEN.b,Color.GREEN.a/10f);
                    if(cam.loopState==0||cam.loopState==1){
                        game.shapeRenderer.circle(getX()+airRangeX,getY()+airRangeY,airRangeLength2);
                    }
                    if(cam.loopState==1||cam.loopState==2){
                        game.shapeRenderer.circle(getX()+cam.getMapW_px()+airRangeX,getY()+airRangeY,airRangeLength2);
                    }
                }
                game.shapeRenderer.end();
                Gdx.gl.glDisable(GL30.GL_BLEND);
                batch.begin();
            }
            batch.setColor(color.WHITE);

            //绘制地名
            if(cam.rescource.drawType<2){
                if(game.gameConfig.shieldAreaName&&game.gameConfig.ifShield){

                }else {
                    GameMap.drawBuildNameForMap(game,batch,smapDao,cam);
                }
            }
        }

    }


    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }



    // 将↓→坐标(左上角为0,0,越向右下越大)转为←↓act坐标(左下角为0,0,越向右上越小)
    public float getActXCoordByImgX(float print_x) {
        return (-print_x);
    }

    public float getActYCoordByImgY(float print_y) {
        return (print_y - cam.getMapH_px());
    }

    // 将←↓act坐标(左下角为0,0,越向右上越小)转化为↓→坐标(左上角为0,0,越向右下越大)
    // 将↓→坐标(左上角为0,0,越向右下越大)转为←↓act坐标(左下角为0,0,越向右上越小)
    public float getImgXCoordByActX(float print_x) {
        /*if(print_x!=0f) {
            return (-print_x);
        }else {
            return 0f;
        }*/
        print_x=-print_x;
        //Gdx.app.log("print_x:","print_x:"+print_x+ " rgw:"+getTextureMain().getWidth());
        if(print_x< cam.getMapW_px()){
            return print_x;
        }else{
            return print_x- cam.getMapW_px();
        }
    }



    // 清理5张图层
    public void dispose() {

        if(damageLabels!=null){
            damageLabels.dispose();
            damageLabels=null;
        }
    }



   /* public Texture getTextureMain() {
        return textureMain;
    }

    public void setTextureMain(Texture textureMain) {
        this.textureMain = textureMain;
    }*/


    /*public Sprite getSpriteMain() {
        return spriteMain;
    }

    public void setSpriteMain(Sprite spriteMain) {
        this.spriteMain = spriteMain;
    }*/


    public SpriteDAO getSpriteGrid() {
        return spriteGrid;
    }

    public void setSpriteGrid(SpriteDAO spriteGrid) {
        this.spriteGrid = spriteGrid;
    }



    public int getGw() {
        return gw;
    }

    public void setGw(int gw) {
        this.gw = gw;
    }

    public int getGh() {
        return gh;
    }

    public void setGh(int gh) {
        this.gh = gh;
    }

    public SpriteListDAO getSpriteGrids() {
        return spriteGrids;
    }

    public void setSpriteGrids(SpriteListDAO spriteGrids) {
        this.spriteGrids = spriteGrids;
    }



    public void syncPotion(float x, float y, float zoom,int loopState){
        this.x=x;
        this.y=y;
        this.zoom=zoom;
    }








    public float getImgYCoordByActY(float print_y) {
        return (cam.getMapH_px() + print_y);
    }



    public void removeDrawMark(int index){
        if(drawMarkList !=null&& drawMarkList.size()>index&&index>=0){
            drawMarkList.remove(index);
        }
    }
    public void clearDrawMark(){
        if(drawMarkList !=null&& drawMarkList.size()>0){
            drawMarkList.clear();
        }
        airRangeScale=0;
    }


    public void setDrawMark(IntIntMap drawHexagon,int clickId, int type){

        clearDrawMark();
        if(drawHexagon==null||drawHexagon.size==0){return;}
        Iterator<IntIntMap.Entry> it = drawHexagon.iterator();
        int drawType=0;
        while (it.hasNext()) {
            IntIntMap.Entry c= it.next();
           int id=c.key;
            if(id==-1){
                continue;
            }
            drawType= DefDAO.getDrawType(type,c.value);
            drawMark =new DrawDAO();
            // drawMark.setTextureName(DefDAO.getDrawMark(drawType));
           // drawType=id%13;
            drawMark.setTextureRegionDAO(game.getImgLists().getTextureByName(DefDAO.getDrawMark(drawType)));

            int x = (id % w) + 1;
            int y = (id / w) + 1;
            float refx=0;
            float refy=0;

            drawMark.setLx_px(GameMap.getX_pxByHexagon(x,scale,0)-(drawMark.getTextureRegionDAO().getRefx()+refx)*scale);
            drawMark.setLy_px(GameMap.getY_pxByHexagon(x,y,cam.getMapH_px(),scale,0,true)-(drawMark.getTextureRegionDAO().getRefy()+refy)*scale);
            drawMark.setW_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionWidth()*scale);
            drawMark.setH_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionHeight()*scale);
           // drawMark.setColor(DefDAO.transColorForDrawMark(c.value));
            drawMark.setColor(DefDAO.transColorForDrawMark(c.value));
            //TODO 存储一些东西测试
            /*if(ResDefaultConfig.ifDebug){
                drawMark.setId(smapDao.getHexagonScore(clickId,c.key,c.value));
                //drawMark.setId(game.getSMapDAO().getDistanceForMap(clickId,c.key));
            }*/
            drawMarkList.add(drawMark);
            //Gdx.app.log("itNext",x+":"+y);
        }
    }

    public void addDrawMark(int id1,int drawType,int color){
        drawMark =new DrawDAO();
        // drawMark.setTextureName(DefDAO.getDrawMark(drawType));
        drawMark.setTextureRegionDAO(game.getImgLists().getTextureByName(DefDAO.getDrawMark(drawType)));
        drawMark.setId(id1);
        int x = (id1 % w) + 1;
        int y = (id1 / w) + 1;

        drawMark.setLx_px(GameMap.getX_pxByHexagon(x,scale,0)-drawMark.getTextureRegionDAO().getRefx()*scale);
        drawMark.setLy_px(GameMap.getY_pxByHexagon(x,y,cam.getMapH_px(),scale,0,true)-drawMark.getTextureRegionDAO().getRefy()*scale);
        drawMark.setW_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionWidth()*scale);
        drawMark.setH_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionHeight()*scale);

        drawMark.setColor(DefDAO.getColor(color));
        drawMarkList.add(drawMark);
    }


    public void setDrawMark(int id0,IntArray drawHexagons, int drawType){
        clearDrawMark();
        if(drawHexagons==null||drawHexagons.size==0){return;}

       for(int i=0,iMax=drawHexagons.size;i<iMax;i++){
            int id1=drawHexagons.get(i);
            if(id1==-1){  continue; }

            drawMark =new DrawDAO();
            // drawMark.setTextureName(DefDAO.getDrawMark(drawType));
            drawMark.setTextureRegionDAO(game.getImgLists().getTextureByName(DefDAO.getDrawMark(drawType)));
            drawMark.setId(id1);
            int x = (id1 % w) + 1;
            int y = (id1 / w) + 1;

            drawMark.setLx_px(GameMap.getX_pxByHexagon(x,scale,0)-drawMark.getTextureRegionDAO().getRefx()*scale);
            drawMark.setLy_px(GameMap.getY_pxByHexagon(x,y,cam.getMapH_px(),scale,0,true)-drawMark.getTextureRegionDAO().getRefy()*scale);
            drawMark.setW_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionWidth()*scale);
            drawMark.setH_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionHeight()*scale);

            drawMark.setColor(game.getSMapDAO().getColorByAlly(id0,id1));
            drawMarkList.add(drawMark);
            //Gdx.app.log("itNext",x+":"+y);
        }
    }


    //绘制进攻动画
    public void drawNumerChange(int hexagon, int v,float time,Color color,boolean ifUpd) {

        if(!cam.inScreen(hexagon)){
            return;
        }

        if(v==0){
            return;
        }
       // Gdx.app.log("drawNumerChange",hexagon+":"+v);
       /* Color color;
        if(v>0){
            color=Color.GREEN;
        }else {
            color=Color.RED;
        }*/

        //绘制伤害
        int x,y;float x_px,y_px;
            x=GameMap.getHX(hexagon,w)+1;
            y=GameMap.getHY(hexagon,w)+1;
            x_px=GameMap.getX_pxByHexagon(x,scale,10);
            y_px=GameMap.getY_pxByHexagon(x,y,cam.getMapH_px(),scale,0,true);
            if(cam.loopState>0){
                if(cam.loopState>1||x<w/2){
                    x_px=x_px+cam.getMapW_px();
                }
            }
            Label label= damageLabels.getLabel(color);
        label.setText(ComUtil.getSymbolNumer(v));
        /*if(v>0){
           label.setText("+"+v);
        }else {
            label.setText(v);
        }*/

            label.setX(x_px);
            label.setY(y_px);
            if(cam.getZoom()<0.8){
                label.setFontScale(1);
            }else {
                label.setFontScale(cam.getZoom());
            }
        label.setFontScale(label.getFontScaleX()*game.gameConfig.legionNameScale);

        label.setZIndex(99999);
            label.getColor().a=0.01f;
        DelayAction action1 = Actions.delay(time);
        AlphaAction  action2 = Actions.alpha(1.0F, 0);
        float updY=0;
        if(ifUpd){
            updY=50*scale;
        }else{
            updY=-50*scale;
        }
        ParallelAction action3 = Actions.parallel(
             Actions.moveBy(0,updY , 1F),
           Actions.alpha(0.0F, 1F)
        );
        //顺序动作
        SequenceAction sequenceAction  = Actions.sequence(action1,action2,action3);
            label.addAction(sequenceAction );
            i++;
    }


    public void setDrawMarkForAir(int airId, int hexagon, IntArray drawHexagons) {
        clearDrawMark();
        if(drawHexagons==null||drawHexagons.size==0){return;}
        int drawType=0;
        for(int i=0,iMax=drawHexagons.size;i<iMax;i++){
            int targetHexagon=drawHexagons.get(i);
            if(targetHexagon<0){  continue; }
            drawType=game.gameMethod.getDrawTypeByAir(airId,hexagon,targetHexagon);
            drawMark =new DrawDAO();
            drawMark.setTextureRegionDAO(game.getImgLists().getTextureByName(DefDAO.getDrawMark(drawType)));
            drawMark.setId(targetHexagon);
            int x = (targetHexagon % w) + 1;
            int y = (targetHexagon / w) + 1;

            drawMark.setLx_px(GameMap.getX_pxByHexagon(x,scale,0)-drawMark.getTextureRegionDAO().getRefx()*scale);
            drawMark.setLy_px(GameMap.getY_pxByHexagon(x,y,cam.getMapH_px(),scale,0,true)-drawMark.getTextureRegionDAO().getRefy()*scale);
            drawMark.setW_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionWidth()*scale);
            drawMark.setH_px(drawMark.getTextureRegionDAO().getTextureRegion().getRegionHeight()*scale);

            drawMark.setColor(game.getSMapDAO().getColorByAlly(hexagon,targetHexagon));
            drawMarkList.add(drawMark);
            //Gdx.app.log("itNext",x+":"+y);
        }

    }

    public void setDrawCircle(int hexagon,int minRange ,int maxRange) {
        int x = (hexagon % w) + 1;
        int y = (hexagon / w) + 1;
        airRangeScale =maxRange/ ResDefaultConfig.Map.MAP_SCALE;
        /*airRangeMark.isVisable=true;

        airRangeMark.setLx_px(GameMap.getX_pxByHexagon(x,scale,0)- airRangeMark.getTextureRegionDAO().getRefx()*scale);
        airRangeMark.setLy_px(GameMap.getY_pxByHexagon(x,y,cam.getMapH_px(),scale,0,true)- airRangeMark.getTextureRegionDAO().getRefy()*scale);
        airRangeMark.setW_px(airRangeMark.getTextureRegionDAO().getTextureRegion().getRegionWidth()*scale);
        airRangeMark.setH_px(airRangeMark.getTextureRegionDAO().getTextureRegion().getRegionHeight()*scale);
        airRangeMark.setId(hexagon);
        airRangeMark.setColor(DefDAO.getColorByBuildAirforcePolicy(smapDao.getBuildDataByRegion(smapDao.getRegionId(hexagon)).getAirforcePolicy()));*/
        airRangeLength=ResDefaultConfig.Map.HEXAGON_WIDTH*airRangeScale;
        airRangeX=GameMap.getX_pxByHexagon(x,scale,0);
        airRangeY=GameMap.getY_pxByHexagon(x,y,cam.getMapH_px(),scale,0,true);
        minRange--;
        if(minRange>0&&minRange<maxRange){//小于最低射程
            airRangeScale2=  minRange/ ResDefaultConfig.Map.MAP_SCALE;
            airRangeLength2=ResDefaultConfig.Map.HEXAGON_WIDTH*airRangeScale2;
        }else{
            airRangeScale2=0;
        }
    }
}