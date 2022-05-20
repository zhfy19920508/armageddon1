package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameMap;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.screen.stage.SMapEffectStage;

//绘制特效
public class EffectActorListDAO {

   // private   Array<ComActor> comActorLists ;

    //private   Pool<ComActor> comActorPool;
    private MainGame game;
    private int mapW_px;//横向移动需要的值 像素
    private int mapH_px;
    private int mapW;//格子数,辅助计算坐标
    private float scale;
    private CamerDAO camerDAO;
    private SMapEffectStage stage;

    //mainGame,gameStage.scale,gameStage.getMapW_px(),gameStage.getMapH_px(),gameStage.getMapW()
    public EffectActorListDAO(MainGame game,SMapEffectStage stage, CamerDAO camerDAO,float scale,int mapW_px,int mapH_px,int mapW){
      this.camerDAO=camerDAO;
        this.game=game;
        this.mapW_px=mapW_px;
        this.mapH_px=mapH_px;
        this.mapW=mapW;
        this.scale=scale;
        this.stage=stage;

       // Array<ComActor> comActorLists = new Array<ComActor>();
      /* comActorPool =new Pool<ComActor>() {
            @Override
            protected ComActor newObject() {
                // 池中无对象时自动调用该方法创建一个对象
                return new ComActor();
            }
        };*/
    }

    public void removeComActor(ComActor actor){
        stage.getRoot().removeActor(actor);
        actor.remove();
        actor.clearListeners();
     //   comActorPool.free(actor);
    }

    //1.士气上升
    //2.建造
    //3.恐惧
    public void darwEffect(int effectId,int hexagon1,int hexagon2,float delaySeconds){
        if(!camerDAO.inScreen(hexagon1)&&!camerDAO.inScreen(hexagon2)){
            return;
        }
        float x1=0,x2=0,y1=0,y2=0;
        if(hexagon1!=-1){
            int  x = (hexagon1 % mapW) + 1;
            int  y = (hexagon1 / mapW) + 1;
            x1= GameMap.getX_pxByHexagon(x,scale,0);
            y1=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        }
        if(hexagon2!=-1){
            int  x = (hexagon2 % mapW) + 1;
            int  y = (hexagon2 / mapW) + 1;
            x2= GameMap.getX_pxByHexagon(x,scale,0);
            y2=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        }

        final ComActor comActor = BaseActor.obtain(ComActor.class);
        String regionName;
        //float scale= ResDefaultConfig.Map.MAP_SCALE;
        if(effectId>1500&&effectId<1600){
            if(camerDAO.rescource.drawType==1){
                regionName="army_1500";
            }else {
                 regionName=DefDAO.getImageNameByEffectId(effectId);
            }
        }else {
            regionName=DefDAO.getImageNameByEffectId(effectId);
        }

        if(regionName==null){return;}
        comActor.init(game,camerDAO,mapW_px,regionName);
      if(!stage.getActors().contains(comActor,false)){
          stage.addActor(comActor);
      }else {
          comActor.setVisible(true);
      }


        //释放内存
        RunnableAction endAction = Actions.run(new Runnable() {
            @Override public void run() {
                removeComActor(comActor);
            }
        });
        comActor.setCenterPotionAndOriginByTextureRegionRef(x1,y1);
        comActor.setScale(scale);
        if(effectId<100){
            SequenceAction action1=null;
            if(delaySeconds>0){
                DelayAction delay = Actions.delay(delaySeconds);
                //闪烁2秒
                 action1 = Actions.sequence(
                        delay,
                        Actions.alpha(0.0F, 1f),
                        Actions.alpha(1.0F, 1f)
                );
            }else{
                //闪烁2秒
                 action1 = Actions.sequence(
                        Actions.alpha(0.0F, 1f),
                        Actions.alpha(1.0F, 1f)
                );
            }


            // 顺序动作, 先闪烁n秒,然后消失还原
            SequenceAction sequenceAction = Actions.sequence(action1, endAction);
            // 执行顺序动作
            comActor.addAction(sequenceAction);
        }else if(effectId>1500&&effectId<1600&&hexagon2!=-1){//飞机

            comActor.setCenterPotionAndOriginByTextureRegionRef(x1,y1);
            float rotation=(float) ComUtil.getRadian(x2,mapH_px-y2,x1,mapH_px-y1)-90;
            //1.设定旋转方向
            comActor.setRotation(rotation);
          //  Gdx.app.log("air effect",hexagon2+"("+x2+","+y2+")-->"+hexagon1+"("+x1+","+y1+") r:"+rotation+" cr:"+comActor.getRotation());

            if(delaySeconds<=0){
                delaySeconds=1f;
            }

            //2.向2坐标移动
            MoveToAction action2 = Actions.moveTo(x2-comActor.textureRegionDAO.getRefx(), y2-comActor.textureRegionDAO.getRefy(), delaySeconds);
            SequenceAction action1 = null;
            action1 = Actions.sequence(
                    action2, endAction
            );

            comActor.addAction(action1);
        }

    }


    //TODO 0正常 1多云 2雨天 3雪天 4雾天 5大风 6酷热 7永冬
    public void drawWether(int weatherId) {
       // comActorPool.clear();
       // ComActor c=BaseActor.obtain(ComActor.class);

    }
    //type 0feature 1skill
    public void darwSkillEffect( int hexagon,int type, int skillId) {
        if(!camerDAO.inScreen(hexagon)){
            return;
        }
        float x1=0,y1=0;
        if(hexagon!=-1){
            int  x = (hexagon % mapW) + 1;
            int  y = (hexagon / mapW) + 1;
            x1= GameMap.getX_pxByHexagon(x,scale,0);
            y1=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        }

        final ComActor comActor = BaseActor.obtain(ComActor.class);
        String regionName;
        float scale= ResDefaultConfig.Map.UNITMODEL_SCALE;
        regionName=DefDAO.getImageNameBySkill(type,skillId);

        if(regionName==null||comActor==null){return;}
        comActor.init(game,camerDAO,mapW_px,regionName);
        comActor.setBgRegion("skill_border");
        if(!stage.getActors().contains(comActor,false)){
            stage.addActor(comActor);
        }else {
            comActor.setVisible(true);
        }
        comActor.setCenterPotionAndOriginByTextureRegionRef(x1,y1);
        x1=comActor.getX();
        y1=comActor.getY();
        comActor.setScale(scale*ResDefaultConfig.Map.MAP_SCALE/2);
        ParallelAction action=Actions.parallel(//并行动作
                 /*Actions.sequence(
                //金币的跳跃动作
                //从当前位置跳跃到上部  0.3f 同时由小到大
                Actions.moveTo(x1,  y1+ ResDefaultConfig.Map.HEXAGON_HEIGHT_REF*ResDefaultConfig.Map.MAP_SCALE, 0.5F),
                //从上部跳跃回当前 0.1f
                Actions.moveTo(x1, y1, 0.9F)
        ),*/
                Actions.moveTo(x1,  y1+ ResDefaultConfig.Map.HEXAGON_HEIGHT_REF*ResDefaultConfig.Map.MAP_SCALE, 1.5F),
                Actions.sequence(
                        Actions.scaleTo(scale*0.1f,scale*0.3f,0.3f),
                        Actions.scaleTo(scale*0.5f,scale*0.3f,0.2f),
                        Actions.scaleTo(scale*0.1f,scale*0.3f,0.3f),
                        Actions.scaleTo(scale*0.8f,scale*0.8f,0.6f)
                        )

        );
        //释放内存
        RunnableAction endAction = Actions.run(new Runnable() {
            @Override public void run() {
                removeComActor(comActor);
            }
        });
        if(comActor!=null){
            // 顺序动作, 先闪烁n秒,然后消失还原
            SequenceAction sequenceAction = Actions.sequence(action, Actions.alpha(0.0F, 0.7f),endAction);
            // 执行顺序动作
            comActor.addAction(sequenceAction); /**/
        }

    }

    //0 高涨 1上升 2平淡 3下降 4低落 5混乱
    public void darwFaceEffect( int hexagon,int face) {
        if(!camerDAO.inScreen(hexagon)){
            return;
        }
        float x1=0,y1=0;
        if(hexagon!=-1){
            int  x = (hexagon % mapW) + 1;
            int  y = (hexagon / mapW) + 1;
            x1= GameMap.getX_pxByHexagon(x,scale,0);
            y1=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true);
        }

        final ComActor comActor = BaseActor.obtain(ComActor.class);
        String regionName;
        float scale= ResDefaultConfig.Map.UNITMODEL_SCALE;
        regionName="unit_face"+face;

        if(regionName==null){return;}
        comActor.init(game,camerDAO,mapW_px,regionName);
        comActor.setScale(scale);
        if(!stage.getActors().contains(comActor,false)){
            stage.addActor(comActor);
        }else {
            comActor.setVisible(true);
        }


        comActor.setCenterPotionAndOriginByTextureRegionRef(x1+=scale*ResDefaultConfig.Map.HEXAGON_WIDTH/2,y1);
        x1=comActor.getX();
        y1=comActor.getY()+scale*ResDefaultConfig.Map.HEXAGON_HEIGHT_REF*2*camerDAO.getZoom();

        ParallelAction action3=Actions.parallel(//并行动作
                Actions.moveTo(x1,  y1, 1F),  Actions.alpha(0.0F, 1f)
        );
        //释放内存
        RunnableAction endAction = Actions.run(new Runnable() {
            @Override public void run() {
                removeComActor(comActor);
            }
        });

        // 顺序动作, 先闪烁n秒,然后消失还原
        SequenceAction sequenceAction = Actions.sequence(action3,endAction);

        // 执行顺序动作
        comActor.addAction(sequenceAction);
    }

    //effectName
    //绘制特效
    //ifFlip 是否偏转,默认位置是向左 如果为false,且hexagon为-1,应该在前一步中计算他的偏移位置
    //hexagon 为-1,则不额外计算坐标位置
    public void darwEffect(String effectName, int hexagon, float refx, float refy, int rot, float atTime,boolean ifFlip) {

        float sX=0,sY=0;
        XmlReader.Element effectModeE=game.gameConfig.getDEF_EFFECT().getElementById(effectName);
        if(effectModeE==null||!game.gameConfig.ifEffect){
            return;
        }
        if(hexagon!=-1){
            if(!camerDAO.inScreen(hexagon)){
                return;
            }
            int  x = (hexagon % mapW) + 1;
            int  y = (hexagon / mapW) + 1;
            //TODO refx refy ifFlip 需要根据方向处理一下
            if (!ifFlip){
                sX= GameMap.getX_pxByHexagon(x,scale,0)-refx;
                sY=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true)+refy;
            }else {
                sX= GameMap.getX_pxByHexagon(x,scale,0)+refx;
                sY=GameMap.getY_pxByHexagon(x,y,mapH_px,scale,0,true)+refy;
            }
            //增加一个偏移,以让其居中
            sX+=ResDefaultConfig.Map.EFFECT_HEXAGON_REFX;
        }else {//这个的sx应该在外部计算
            sX=refx;
            sY=refy;
        }
        Gdx.app.log("darwEffect 1:"+effectName,hexagon+":"+refx+" "+refy+" "+rot+" "+atTime+" "+ifFlip);

        Gdx.app.log("darwEffect 2:"+effectName,hexagon+":"+sX+" "+sY+" "+rot+" "+atTime+" "+ifFlip);
       Array<XmlReader.Element>  effectsEs= effectModeE.getChildrenByName("mode");
        for(int i=0;i<effectsEs.size;i++){
            parseEffects(effectsEs.get(i),sX,sY,rot,atTime,ifFlip);
        }
    }

    //对进行解析
    //开炮默认开火应该是向左
    //因为是左,所以rot应该是个正数,这里做减处理
    private void parseEffects(XmlReader.Element element, float sX, float sY, int rot,float atTime,boolean ifFlip) {
        final ComActor comActor = BaseActor.obtain(ComActor.class);
      //  float scale= ResDefaultConfig.Map.MAP_SCALE;
        comActor.init(game,camerDAO,mapW_px,element.get("image"),ifFlip);
        if(!stage.getActors().contains(comActor,false)){
            stage.addActor(comActor);
        }else {
            comActor.setVisible(true);
        }
        int cv=element.getInt("cx",-1);

        if(!ifFlip){//左
            if(cv==-1){
                comActor.setOriginX(comActor.getWidth()/2);
            }else {
                comActor.setOriginX(cv);
            }
        }else {//右
            if(cv==-1){
                comActor.setOriginX(comActor.getWidth()/2);
            }else {
                comActor.setOriginX(comActor.getWidth()-cv);
            }
        }
        cv=element.getInt("cy",-1);
        if(cv==-1){
            comActor.setOriginY(comActor.getHeight()/2);
        }else {
            comActor.setOriginY(cv);
        }

        if(!ifFlip){
            comActor.setCenterPotionByHaveOrigin(sX+element.getInt("x",0),sY+element.getInt("y",0));
        }else {
            comActor.setCenterPotionByHaveOrigin(sX-element.getInt("x",0),sY+element.getInt("y",0));
        }
       // comActor.setCenterPotionByHaveOrigin(sX+element.getInt("x",0),sY+element.getInt("y",0));

        // sX=comActor.getX();
       // sY=comActor.getY();
        comActor.setScale(element.getFloat("scaleX",1.0f),element.getFloat("scaleY",1.0f));
        if(!ifFlip){
            comActor.setRotation(element.getInt("angle",0)-rot);
        }else {
            comActor.setRotation(-(element.getInt("angle",0)-rot));
        }

        comActor.setColor(element.getInt("r",0)/255f,element.getInt("g",0)/255f,element.getInt("b",0)/255f,0);


        //释放内存
        RunnableAction endAction = Actions.run(new Runnable() {
            @Override public void run() {
                removeComActor(comActor);
            }
        });
        ParallelAction action=Actions.parallel(//并行动作
                parseActionByXmlE(element,ifFlip)
                //  addActionByXmlE();
        );
        // 顺序动作, 先闪烁n秒,然后消失还原
        SequenceAction sequenceAction = Actions.sequence(Actions.delay(element.getFloat("at",0)+atTime),Actions.alpha(element.getInt("a",0)/255f),action,endAction);
        // 执行顺序动作
        comActor.addAction(sequenceAction); /**/
    }

    private Action[] parseActionByXmlE(XmlReader.Element xmls,boolean ifFlip){
        Action[] rs=new Action[xmls.getChildCount()+1];
        rs[0]=Actions.delay(xmls.getFloat("life",1.0f));
        if(xmls.getChildCount()>0){
            for(int i=0;i<xmls.getChildCount();i++){
                XmlReader.Element xmlE=xmls.getChild(i);
                String strs=xmlE.get("value");
                switch (xmlE.get("type")){
                    case "MoveToAction"://将演员从当前位置移动到指定的位置
                        if(!ifFlip){
                            rs[i+1]=Actions.moveTo(ComUtil.getIntByStrs(strs,0), ComUtil.getIntByStrs(strs,1), ComUtil.getFloatByStrs(strs,2));
                        }else {
                            rs[i+1]=Actions.moveTo(-ComUtil.getIntByStrs(strs,0), ComUtil.getIntByStrs(strs,1), ComUtil.getFloatByStrs(strs,2));
                        }
                        break;
                    case "RotateToAction":// 将演员从当前角度旋转到指定的角度
                        if(!ifFlip) {
                            rs[i + 1] = Actions.rotateTo(ComUtil.getIntByStrs(strs, 0), ComUtil.getFloatByStrs(strs, 1));
                        }else {
                            rs[i + 1] = Actions.rotateTo(180-ComUtil.getIntByStrs(strs, 0), ComUtil.getFloatByStrs(strs, 1));
                        }
                        break;
                    case "ScaleToAction":// 将演员从当前的缩放值过渡到指定的缩放值
                        rs[i + 1] = Actions.scaleTo(ComUtil.getFloatByStrs(strs, 0), ComUtil.getFloatByStrs(strs, 1), ComUtil.getFloatByStrs(strs, 2));
                        break;
                    case "SizeToAction":// 将演员从当前尺寸（宽高）过渡到指定尺寸（宽高）
                        rs[i+1]= Actions.sizeTo(ComUtil.getIntByStrs(strs,0), ComUtil.getIntByStrs(strs,1), ComUtil.getFloatByStrs(strs,2));
                        break;
                    case "AlphaAction"://将演员的透明度从当前 alpha 值过渡到指定的 alpha 值
                        rs[i+1]=Actions.alpha(ComUtil.getFloatByStrs(strs,0), ComUtil.getFloatByStrs(strs,1));
                        break;
                    case "MoveByAction"://演员在当前位置基础上移动指定的距离
                        if(!ifFlip) {
                            rs[i + 1] = Actions.moveBy(ComUtil.getIntByStrs(strs, 0), ComUtil.getIntByStrs(strs, 1), ComUtil.getFloatByStrs(strs, 2));
                        }else {
                            rs[i + 1] = Actions.moveBy(-ComUtil.getIntByStrs(strs, 0), ComUtil.getIntByStrs(strs, 1), ComUtil.getFloatByStrs(strs, 2));
                        }
                        break;
                    case "RotateByAction"://演员在当前角度值的基础上旋转指定的角度
                        if(!ifFlip) {
                            rs[i + 1] = Actions.rotateBy(ComUtil.getIntByStrs(strs, 0), ComUtil.getFloatByStrs(strs, 1));
                        }else {
                            rs[i + 1] = Actions.rotateBy(ComUtil.getIntByStrs(strs, 0), ComUtil.getFloatByStrs(strs, 1)-180);
                        }
                        break;
                    case "ScaleByAction"://演员在当前缩放值的基础上进行再次缩放
                        rs[i+1]= Actions.scaleBy(ComUtil.getFloatByStrs(strs,0), ComUtil.getFloatByStrs(strs,1), ComUtil.getFloatByStrs(strs,2));
                        break;
                    case "SizeByAction"://演员在当前尺寸基础上增加指定的尺寸
                        rs[i+1]=Actions.sizeBy(ComUtil.getIntByStrs(strs,0), ComUtil.getIntByStrs(strs,1), ComUtil.getFloatByStrs(strs,2));
                        break;
                }
            }
        }


        return rs;
    }

    //播放兵模动画 注意hexagon
    //darwEffect(String effectName, int hexagon, int refx, int refy, int rot, float atTime,boolean ifFlip) {
    //hexagon 如果作战坐标已经提前计算,则只需要为-1,该绘制应该提前检查坐标
    public void drawFiresEffect(int hexagon,float sourceX, float sourceY,int modelWidth,float damageTime,boolean ifFlip, Array<XmlReader.Element> fireXmlEs,float modelScale) {
        if(fireXmlEs==null||fireXmlEs.size==0||!game.gameConfig.ifEffect){
            return;
        }

        for(int i=0;i<fireXmlEs.size;i++){
            XmlReader.Element xmlE=fireXmlEs.get(i);
            float refX=sourceX;
            if(!(xmlE.getBoolean("ifFlip",false)&&ifFlip)){//左
                refX=sourceX+xmlE.getInt("x",0)* ResDefaultConfig.Map.MAP_SCALE*scale*modelScale;
            }else {//右
                refX=sourceX+(modelWidth- xmlE.getInt("x",0))* ResDefaultConfig.Map.MAP_SCALE*scale*modelScale;
            }
            darwEffect(xmlE.get("name","mgunfire"),
                    hexagon,refX,
                    xmlE.getInt("y",0)* ResDefaultConfig.Map.MAP_SCALE*scale*modelScale+sourceY,
                    xmlE.getInt("rot",0),
                    xmlE.getFloat("atTime",0)+damageTime,
                    xmlE.getBoolean("ifFlip",false)&&ifFlip);
        }
    }
}
