package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.framework.GameUtil;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.TextureRegionDAO;

import java.util.Iterator;



/**
 * 存储窗口
 *
 * @xietansheng
 */
public  class WindowGroup extends Group implements Pool.Poolable {

    private MainGame mainGame;
    public XmlReader.Element groupE;
    private float stageW;
    private float stageH;
    private float refX;
    private float refY;
    private float moblieMaxW;
    private float moblieMaxH;
    private float moblieX;
    private float moblieY;
    private Vector2 tempVector2;


    // private IntIntMap imageIdsMap;
    // private IntIntMap buttonIdsMap;
    // private IntIntMap tbuttonIdsMap;
    // private IntIntMap labelsMap;
    //private IntIntMap textFieldsMap;


    public IntMap<Image> images;
    public IntMap<ImageButton> buttons;
    public IntMap<TextButton> tbuttons;
    public IntMap<Label> labels;
    public Table table;
    //public Array<TextField> textFields;
    public ObjectMap<String,XmlReader.Element> xmlEs;
    public int windowId;
    public boolean ifSlide;
    private boolean ifVisable;//用来计算effect,防止冲突

    //public ObjectMap<ImageButton,Integer> buttonFunctions;//用来添加按钮监听

    public WindowGroup(MainGame mainGame, XmlReader.Element groupE,float stageW,float stageH, Vector2 tempVector2) {
        init(mainGame,groupE,stageW,stageH,tempVector2);
    }

    public void setImageRefPotion(int imageId,float refx,float refy){
        if(groupE.getChildByName("images")!=null&&images.containsKey(imageId)){
            Array<XmlReader.Element> imageEs= groupE.getChildByName("images").getChildrenByName("image");
            XmlReader.Element imageE2=null;
            for( XmlReader.Element imageE:imageEs){
                if(imageE.getInt("id")==imageId){
                    imageE2=imageE;
                    break;
                }
            }
            if(imageE2==null){
                hidImage(imageId);
                Gdx.app.error("setImageRefPotion is error","not imageE");
            }else {
                Image image=images.get(imageId);
                image= GameUtil.resetImagePotion(image,imageE2,stageW,stageH);
                image.setPosition(image.getX()+refx,image.getY()-refy);
            }
        }
    }


    public void init() {
        setX(0);
        setY(0);
        String  scaleAdaptive=  groupE.get("adaptScale","");
        String coverImage=groupE.get("coverImage","none");

        if(!ComUtil.isEmpty(scaleAdaptive)){
            switch (scaleAdaptive){
                case "actorAdaptive":
                    stageH=this.stageH*1280/this.stageW;
                    stageW=1280;
                    refX=(getMainGame().getWorldWidth()-stageW)/2;
                    refY=(getMainGame().getWorldHeight()-stageH)/2;
                    break;
                case "screenAdaptive":
                    stageH=this.stageH*1920/this.stageW;
                    stageW=1920;
                    refX=(getMainGame().getWorldWidth()-stageW)/2;
                    refY=(getMainGame().getWorldHeight()-stageH)/2;
                    break;
            }

        }
        if(!coverImage.equals("none")){
            TextureRegionDAO t=getMainGame().getImgLists().getTextureByName(coverImage);
            if(t!=null){
                Image image=new Image(t.getTextureRegion());
                image.setWidth(stageW);
                image.setHeight(stageH);
                image.setName("coverImage");
                addActor(image);
            }
        }


        boolean ifSaveXml=groupE.getBoolean("ifSaveXmlE",false);

        if(ifSaveXml){
            xmlEs=new ObjectMap<>();
        }
        if(groupE.getChildByName("images")!=null){
            //  imageIdsMap=new IntIntMap();
            images=new IntMap<>();
            Image image=null;
            Array<XmlReader.Element> imageEs= groupE.getChildByName("images").getChildrenByName("image");

            for (int i=0,iMax=imageEs.size;i<iMax;i++) {
                if(ifSaveXml){
                    xmlEs.put("image_"+imageEs.get(i).get("id"),imageEs.get(i));
                }
                image= GameUtil.initImage(image,mainGame,imageEs.get(i),stageW,stageH);
                //  imageIdsMap.put(imageEs.get(i).getInt("id"),i);
                try{
                    if(imageEs.get(i).getBoolean("ifVisible")){
                        image.setVisible(true);
                    }else{
                        image.setVisible(false);
                    }
                }catch (Exception e){
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace();
                    }else if(!getMainGame().gameConfig.getIfIgnoreBug()){
                        Gdx.app.error("initWindowGroupImageError",imageEs.get(i).toString());
                        getMainGame().remindBugFeedBack();
                    }
                    getMainGame().recordLog("WindowGroup imageEs ",e);
                }
                this.addActor(image);
                images.put(imageEs.get(i).getInt("id"),image);
                // GameUtil.logImageInfo(image);
            }
        }
        if(groupE.getChildByName("buttons")!=null){

            //buttonIdsMap=new IntIntMap();
            buttons=new IntMap<>();
            ImageButton button=null;
            //遍历window的buttons按钮




            Array<XmlReader.Element> buttonEs = groupE.getChildByName("buttons").getChildrenByNameRecursively("button");  // 递归遍历，否则的话返回null
            for (int b=0,bMax=buttonEs.size;b<bMax;b++) {
                if(ifSaveXml){
                    xmlEs.put("button_"+buttonEs.get(b).get("id"),buttonEs.get(b));
                }



                //Gdx.app.log("ui测试", button.get("remark"));
                button=GameUtil.initImageButton(button,getMainGame(),buttonEs.get(b),stageW,stageH);

                if(buttonEs.get(b).getBoolean("ifVisible",true)){
                    button.setVisible(true);
                }else{
                    button.setVisible(false);
                }
               /* try{

                }catch (Exception e){
                    Gdx.app.error("initWindowGroupButtonError",buttonEs.get(b).toString());
                     GameUtil.recordLog("ResGameConfig ",e);
                    getMainGame().remindBugFeedBack();
                }*/
                this.addActor(button);
                if(buttonEs.get(b).getBoolean("ifUseAble",true)){
                    buttons.put(buttonEs.get(b).getInt("id"),button);
                    getMainGame().functionMap.put(button,buttonEs.get(b));
                }else{
                    button.getImage().setColor(Color.GRAY);
                }


                //  GameUtil.logImageButtonInfo(button);
                //Gdx.input.setInputProcessor(uiStage);
            }
        }
        if(groupE.getChildByName("tbuttons")!=null){

            //tbuttonIdsMap=new IntIntMap();
            tbuttons=new IntMap<>();
            TextButton tbutton=null;
            //遍历window的buttons按钮

            Array<XmlReader.Element> tbuttonEs = groupE.getChildByName("tbuttons").getChildrenByNameRecursively("tbutton");  // 递归遍历，否则的话返回null
            for (int b=0,bMax=tbuttonEs.size;b<bMax;b++) {
                if(ifSaveXml){
                    xmlEs.put("tbutton_"+tbuttonEs.get(b).get("id"),tbuttonEs.get(b));
                }
                //Gdx.app.log("ui测试", button.get("remark"));
                tbutton=GameUtil.initTextButton(tbutton,getMainGame(),tbuttonEs.get(b),stageW,stageH);
                //getMainGame().functionMap.put(ResConfig.StringName.functionId, buttonEs.get(b).getInt("functionId"));
                //tbuttonIdsMap.put(tbuttonEs.get(b).getInt("id"),b);
                try{
                    if(tbuttonEs.get(b).getBoolean("ifVisible")){
                        tbutton.setVisible(true);
                    }else{
                        tbutton.setVisible(false);
                    }
                }catch (Exception e){
                   if(ResDefaultConfig.ifDebug){
                       e.printStackTrace();
                   }else if(!getMainGame().gameConfig.getIfIgnoreBug()){
                        Gdx.app.error("initWindowGroupButtonError",tbuttonEs.get(b).toString());
                       getMainGame().remindBugFeedBack();
                    }
                    getMainGame().recordLog("WindowGroup tbutton ",e);
                }
                this.addActor(tbutton);
                tbuttons.put(tbuttonEs.get(b).getInt("id"),tbutton);
                getMainGame().functionMap.put(tbutton,tbuttonEs.get(b));
                //  GameUtil.logImageButtonInfo(button);
                //Gdx.input.setInputProcessor(uiStage);
            }
        }


        if(groupE.getChildByName("labels")!=null){

            //labelsMap=new IntIntMap();
            labels=new IntMap<>();
            Label label=null;
            BitmapFont font=null;
            Color color=null;
            String text=null;
            Array<XmlReader.Element> labelEs = groupE.getChildByName("labels").getChildrenByNameRecursively("label");  // 递归遍历，否则的话返回null
            for (int l=0,lMax=labelEs.size;l<lMax;l++) {
                if(ifSaveXml){
                    xmlEs.put("label_"+labelEs.get(l).get("id"),labelEs.get(l));
                }

                font=mainGame.gameMethod.getFont(labelEs.get(l).get("font"));
                color=DefDAO.getColor(labelEs.get(l).get("color","WHITE"));
                //如果是自动调节的,需要保证text设置的内容可以获取到信息
                text=mainGame.gameMethod.getStrValueAndHaveDefault(labelEs.get(l).get("text",""),"");
                if(labelEs.get(l).getBoolean("ifInit",false)){
                    text=mainGame.gameMethod.getStrValueT(text);
                }
                //Label label, BitmapFont font, Color color,  Element labelE, float uiStageWidth, float uiStageHeight
                if(labelEs.get(l).getBoolean("isScroll",false)){
                    ScrollLabel scrollLabel =GameUtil.initCHScrollLabel(mainGame,font,text,color, labelEs.get(l),stageW,stageH,getMainGame().gameConfig.gameFontScale);
                    this.addActor(scrollLabel);
                    label=scrollLabel.getLabel();
                    //scrollLabel.setDebug(true);
                    scrollLabel.setName("scrollLabel_"+labelEs.get(l).getInt("id"));
                    if(labelEs.get(l).getBoolean("ifDebug",false)){
                        scrollLabel.setDebug(true);
                    }
                }else{
                    label=GameUtil.initLabel(label,font,text,color, labelEs.get(l),stageW,stageH,getMainGame().gameConfig.gameFontScale);
                    this.addActor(label);
                    if(labelEs.get(l).getBoolean("ifDebug",false)){
                        label.setDebug(true);
                    }
                }


                //labelsMap.put(labelEs.get(l).getInt("id"),l);
                try{
                    if(labelEs.get(l).getBoolean("ifVisible")){
                        label.setVisible(true);
                    }else{
                        label.setVisible(false);
                    }
                }catch (Exception e){
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace();
                    }else if(!getMainGame().gameConfig.getIfIgnoreBug()){
                        Gdx.app.error("initWindowGroupLabelError",labelEs.get(l).toString());
                        getMainGame().remindBugFeedBack();
                    }
                    getMainGame().recordLog("WindowGroup labelEs ",e);
                }

                // label.setSize(0,0);
                labels.put(labelEs.get(l).getInt("id"),label);
            }
        }
        if(groupE.getChildByName("table")!=null){
            table=GameUtil.initTable(getMainGame(),groupE.getChildByName("table"),stageW,stageH);
            addActor(table);
        }



        moblieMaxH=-1;
        moblieMaxW=-1;
        if(groupE.getBoolean("ifMobile",false)){
            this.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    moblieX=x;
                    moblieY=y;
                    ifSlide=false;
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    super.touchDragged(event, x, y, pointer);
                    if(moblieMaxW!=-1){
                        setX(ComUtil.limitFValue(getX()+(x-moblieX)    ,-moblieMaxW+getStage().getWidth(),0));
                        //  Gdx.app.log("touchDragged window",getX()+":"+(x-moblieX));
                    }
                    if(moblieMaxH!=-1){
                        setY(ComUtil.limitFValue(getY()+(y-moblieY),moblieMaxH,0));
                    }
                    ifSlide=true;

                }
            });
            //resetAllPotion();
        }

        setOrigin(Align.center);
        setOriginX(getMainGame().getWorldWidth()/2);
        setOriginY(getMainGame().getWorldHeight()/2);
        //0 minScaleAdaptive //在缩小后维持界面大小
        switch (scaleAdaptive){
            case "minScaleAdaptive"://在缩小后维持界面大小
                float x=0;
                float y=0;
                float w=0;
                float h=0;
                for(int i = 0, iMax = getChildren().size;i<iMax;i++){
                    Actor a=getChild(i);
                    if(a.getX()<x){
                        x=a.getX();
                    }
                    if(a.getY()<y){
                        y=a.getY();
                    }
                    if(a.getX()+a.getWidth()>w){
                        w=a.getX()+a.getWidth();
                    }
                    if(a.getY()+a.getHeight()>h){
                        h=a.getY()+a.getHeight();
                    }
                }
                if(x<0){
                    w=w-x;
                }
                if(y<0){
                    h=h-y;
                }
                float scaleX=getMainGame().getWorldWidth()/ w;
                float scaleY=getMainGame().getWorldHeight()/h;
                float v=Math.min(scaleX,scaleY);
                if(v<getScaleX()){
                    setScale(v);
                }
                break;

            case "minScaleAdaptiveAndInFoot"://在缩小后维持界面大小
                 x=0;
                 y=0;
                 w=0;
                 h=0;
                for(int i = 0, iMax = getChildren().size;i<iMax;i++){
                    Actor a=getChild(i);
                    if(a.getX()<x){
                        x=a.getX();
                    }
                    if(a.getY()<y){
                        y=a.getY();
                    }
                    if(a.getX()+a.getWidth()>w){
                        w=a.getX()+a.getWidth();
                    }
                    if(a.getY()+a.getHeight()>h){
                        h=a.getY()+a.getHeight();
                    }
                }
                if(x<0){
                    w=w-x;
                }
                if(y<0){
                    h=h-y;
                }
                 scaleX=getMainGame().getWorldWidth()/ w;
                 scaleY=getMainGame().getWorldHeight()/h;
                 v=Math.min(scaleX,scaleY);
                if(v<getScaleX()){
                     y=getY()-getStageH()*(getScaleY()-v)/2;
                    setScale(v);
                    setY(y/v);
                }
                break;
            case "scaleAdaptive"://将界面保持在
                x=0;
                y=0;
                w=0;
                h=0;
                for(int i = 0, iMax = getChildren().size;i<iMax;i++){
                    Actor a=getChild(i);
                    if(x==0||a.getX()<x){
                        x=a.getX();
                    }
                    if(y==0||a.getY()<y){
                        y=a.getY();
                    }
                    if(a.getX()+a.getWidth()>w){
                        w=a.getX()+a.getWidth();
                    }
                    if(a.getY()+a.getHeight()>h){
                        h=a.getY()+a.getHeight();
                    }
                }
                if(x<0){
                    w=w-x;
                }
                if(y<0){
                    h=h-y;
                }
                scaleX=getMainGame().getWorldWidth()/(w+x);
                scaleY=getMainGame().getWorldHeight()/(h+y);
                if(getMainGame().getWorldWidth()<=(w+x)||getMainGame().getWorldHeight()<=(h+y)){
                    v=Math.min(scaleX,scaleY);
                    if(v<getScaleX()){
                        setScale(v);
                    }
                }else {
                    v=Math.max(scaleX,scaleY);
                    if(v>getScaleX()){
                        setScale(v);
                    }
                }
                break;
            case "actorAdaptive":
            case "screenAdaptive":
                scaleX=getMainGame().getWorldWidth()/stageW;
                scaleY=getMainGame().getWorldHeight()/stageH;
                v=Math.min(scaleX,scaleY);
                setScale(v);/*
                for(int i = 0, iMax = getChildren().size;i<iMax;i++){
                    Actor a=getChild(i);
                    a.setX(a.getX()+refX);
                    a.setY(a.getY()+refY);
                }*/
                break;
        }
        setX(getX()+refX*getScaleX());
        setY(getY()+refY*getScaleY());

    }


    public void init(MainGame mainGame, XmlReader.Element groupE, float stageW, float stageH, Vector2 tempVector2){
        this.mainGame = mainGame;
        this.groupE=groupE;//存储窗口信息
        this.windowId=groupE.getInt("id");
        this.tempVector2=tempVector2;
        //textFieldsMap=new IntIntMap();
        //textFields=new Array<>();



        this.stageW=stageW;
        this.stageH=stageH;
        //必须先初始变量,才能引用变量
        init();
        this.setVisible(groupE.getBoolean("ifVisible",false));

        //   this.setVisible(false);
    }


    public MainGame getMainGame() {
        return mainGame;
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    /* 下面是便捷设置坐标的方法封装 */

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
        setX(centerX - getWidth() / 2.0F);
    }

    /**
     * 设置竖直方向中心点坐标
     * @param centerY
     */
    public void setCenterY(float centerY) {
        setY(centerY - getHeight() / 2.0F);
    }


    public void setButtonImage(int buttonId, TextureRegion regionUp, TextureRegion regionDown){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(regionUp);
            ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(regionDown);
            button.setVisible(true);
        }
    }
    public void setButtonImage(int buttonId, TextureRegion region){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region);
            button.setHeight(region.getRegionHeight());
            button.setWidth(region.getRegionWidth());
            button.setVisible(true);
        }
    }
    public void setButtonImage(int buttonId, TextureRegion region,float scale){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region);
            button.setHeight(region.getRegionHeight());
            button.setWidth(region.getRegionWidth());
            button.setVisible(true);
            float w=region.getRegionWidth()*scale;
            float h=region.getRegionHeight()*scale;
            button.setWidth(w);
            button.setHeight(h);
            ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(button.getHeight());
            button.setVisible(true);
        }
    }
    public void setButtonImageUpNotChangeSize(int buttonId, TextureRegion region){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            float th= ((TextureRegionDrawable)button.getStyle().imageUp).getMinHeight();
            float tw=((TextureRegionDrawable)button.getStyle().imageUp).getMinWidth();
            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(tw);
            ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(th);
            button.setVisible(true);
        }
    } public void setButtonImageDownNotChangeSize(int buttonId, TextureRegion region){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            float th= ((TextureRegionDrawable)button.getStyle().imageUp).getMinHeight();
            float tw=((TextureRegionDrawable)button.getStyle().imageUp).getMinWidth();

            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(tw);
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(th);

            button.setVisible(true);
        }
    }
    public void setButtonImageCheckedNotChangeSize(int buttonId, TextureRegion region){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            float th= ((TextureRegionDrawable)button.getStyle().imageUp).getMinHeight();
            float tw=((TextureRegionDrawable)button.getStyle().imageUp).getMinWidth();

            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(tw);
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(th);

            button.setVisible(true);
        }
    }
    public void setButtonImageNotChangeSize(int buttonId, TextureRegion region){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            float th= ((TextureRegionDrawable)button.getStyle().imageUp).getMinHeight();
            float tw=((TextureRegionDrawable)button.getStyle().imageUp).getMinWidth();

            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);

            if(button.getStyle().imageDown!=null){
                ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(th);
            }
            if(button.getStyle().imageChecked!=null){
                ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(th);
            }
            if(button.getStyle().imageUp!=null){
                ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(th);
            }


            button.setVisible(true);
        }
    }

    public void setButtonImageToFitImageSize(int buttonId,int targetImageId, TextureRegion region,float scaleX,float scaleY){
        if(buttons.containsKey(buttonId)&&images.containsKey(targetImageId)){
            ImageButton button=getButton(buttonId);
            Image image=getImage(targetImageId);
            float th= image.getWidth()*scaleX;
            float tw=image.getHeight()*scaleY;

            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);

            if(button.getStyle().imageDown!=null){
                ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(th);
            }
            if(button.getStyle().imageChecked!=null){
                ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(th);
            }
            if(button.getStyle().imageUp!=null){
                ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(th);
            }


            button.setVisible(true);
        }
    }
    public void replaceButtonImage(int buttonId, TextureRegionDAO region){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            if(!button.isVisible()){
                button.setVisible(true);
            }
            if(button.getStyle().imageUp!=null){
                ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region.getTextureRegion());
            }
            if(button.getStyle().imageDown!=null){
                ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region.getTextureRegion());
            }
            if(button.getStyle().imageChecked!=null){
                ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region.getTextureRegion());
            }



        }
    }


    //这个位置会在原基础上重新计算偏移
    public void setButtonImage(int buttonId, TextureRegionDAO region){
        if(buttons.containsKey(buttonId)){
            //((TextureRegionDrawable)images.get(imageIdsMap.get(buttonId, 0)).getDrawable()).setBuildRegion(region);
            Array<XmlReader.Element> buttonEs = groupE.getChildByName("buttons").getChildrenByNameRecursively("button");  // 递归遍历，否则的话返回null
            for (int b=0,bMax=buttonEs.size;b<bMax;b++) {
                //Gdx.app.log("ui测试", button.get("remark"));
                if(buttonEs.get(b).getInt("id")==buttonId){
                    ImageButton button=getButton(buttonEs.get(b).getInt("id"));
                    ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region.getTextureRegion());
                    ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region.getTextureRegion());
                    ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region.getTextureRegion());
                    GameUtil.setImageButtonPotion(button,buttonEs.get(b),stageW,stageH);
                    button.setVisible(true);
                    break;
                }
            }
            // ImageButton b=getButton(buttonId);
            // b.setPosition(b.getX()+region.getRefx(),b.getY()-region.getRefy());


        }
    }
    public void setButtonImage(int buttonId, String imageName){
        TextureRegion region =mainGame.getImgLists().getTextureByName(imageName).getTextureRegion();
        setButtonImage(buttonId,region);
    }
    public void setImageRegionNotChangeSize(int imageId, TextureRegion region){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            if(region!=null){
                TextureRegionDrawable temp=    ((TextureRegionDrawable)images.get(imageId).getDrawable());
                if(temp==null){
                    image.setDrawable(new TextureRegionDrawable(region));
                }else{
                    temp.setRegion(region);
                }

                // image.setHeight(region.getRegionHeight());
                //  image.setWidth(region.getRegionWidth());
                image.setVisible(true);
            }else {
                image.setVisible(false);
            }
        }else {
            Gdx.app.error("setImageRegionError","no image,id:"+imageId);
        }
    }

    //这个会额外存储图片名字
    public void setImageRegionNotChangeSize(int imageId, TextureRegionDAO region){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            if(region!=null){
                TextureRegionDrawable temp=    ((TextureRegionDrawable)images.get(imageId).getDrawable());
                temp.setRegion(region.getTextureRegion());
                image.setName(region.getName());
                // image.setHeight(region.getRegionHeight());
                //  image.setWidth(region.getRegionWidth());
                image.setVisible(true);
            }else {
                image.setVisible(false);
            }
        }else {
            Gdx.app.error("setImageRegionError","no image,id:"+imageId);
        }
    }



    public void setImageRegion(int imageId, TextureRegion region){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            if(region!=null){
                TextureRegionDrawable temp=    ((TextureRegionDrawable)images.get(imageId).getDrawable());
                temp.setRegion(region);
                image.setHeight(region.getRegionHeight());
                image.setWidth(region.getRegionWidth());
                image.setVisible(true);
            }else {
                image.setVisible(false);
            }
        }else {
            Gdx.app.error("setImageRegionError","no image,id:"+imageId);
        }
    }
    public void setImageRegionAndCentered(int imageId, TextureRegion region,int center){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            if(region!=null){
                TextureRegionDrawable temp=    ((TextureRegionDrawable)images.get(imageId).getDrawable());
                temp.setRegion(region);
                image.setHeight(region.getRegionHeight());
                image.setWidth(region.getRegionWidth());
                image.setVisible(true);
                if(center==0){
                    image.setPosition( (stageW-image.getWidth())/2   ,(stageH-image.getHeight())/2);

                }else if(center==1){
                    image.setPosition( (stageW-image.getWidth())/2   ,image.getY());

                }else if(center==2){
                    image.setPosition( image.getX()   ,(stageH-image.getHeight())/2);
                }
            }else {
                image.setVisible(false);
            }
        }else {
            Gdx.app.error("setImageRegionError","no image,id:"+imageId);
        }
    }
    public void setImageRegion(int imageId, TextureRegion region,float scale){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            if(region!=null){
                TextureRegionDrawable temp=    ((TextureRegionDrawable)images.get(imageId).getDrawable());
                temp.setRegion(region);
                image.setHeight(region.getRegionHeight());
                image.setWidth(region.getRegionWidth());
                image.setScale(scale);
                image.setVisible(true);
            }else {
                image.setVisible(false);
            }
        }else {
            Gdx.app.error("setImageRegionError","no image,id:"+imageId);
        }
    }



    public void setImageRegion(int imageId, TextureRegionDAO region){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            if(region!=null){
                float w=image.getImageWidth();
                float h=image.getImageHeight();
                ((TextureRegionDrawable)image.getDrawable()).setRegion(region.getTextureRegion());
                image.setHeight(region.getTextureRegion().getRegionHeight());
                image.setWidth(region.getTextureRegion().getRegionWidth());
                if(w!=0&h!=0){
                    image.setWidth(w);
                    image.setHeight(h);
                }
                image.setName(region.getName());
                image.setVisible(true);
            }else if(imageId<images.size){
                image.setVisible(false);
            }else {
                Gdx.app.error("setImageRegionError","no image,id:"+imageId);
            }
        }else {
            Gdx.app.error("setImageRegionError","no image,id:"+imageId);
        }
    }
    //必须保证region的大小合适  暂时是 横长方形处理为 矩形
    public void setImageRegionForCenter(int imageId, TextureRegionDAO region){
        if(windowId==15){
            int s=0;
        }
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            if(region!=null){
                int w= (int) image.getImageWidth();
                int h= (int) image.getImageHeight();
                if (w==0) {
                    w= (int) image.getDrawable().getMinWidth();
                }
                if(h==0){
                    h= (int) image.getDrawable().getMinHeight();
                }
                int x=region.getTextureRegion().getRegionX()+(region.getW()-region.getH())/2;
                int y=region.getTextureRegion().getRegionY();
                w=h;

                ((TextureRegionDrawable)image.getDrawable()).setRegion( new TextureRegion( region.getTextureRegion().getTexture(),x,y,w,h));
                image.setHeight(region.getTextureRegion().getRegionHeight());
                image.setWidth(region.getTextureRegion().getRegionWidth());
                if(w!=0&h!=0){
                    image.setWidth(w);
                    image.setHeight(h);
                }
                image.setName(region.getName());
                image.setVisible(true);
            }else if(imageId<images.size){
                image.setVisible(false);
            }else {
                Gdx.app.error("setImageRegionError","no image,id:"+imageId);
            }
        }else {
            Gdx.app.error("setImageRegionError","no image,id:"+imageId);
        }
    }

    public void setImageRegion(int imageId, TextureRegion region,float w,float h){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            if(region!=null){
                //( (TextureRegionDrawable)images.get(imageId)).setBuildRegion(region);
                ((TextureRegionDrawable)image.getDrawable()).setRegion(region);
                if(h!=0){
                    image.setHeight(h);
                }
                if(w!=0){
                    image.setWidth(w);
                }
                //image.setName(region.getName());
                image.setVisible(true);
            }else {
                image.setVisible(false);
            }
        }else {
            Gdx.app.error("setImageRegionError","no image,id:"+imageId);
        }
    }


    public void setButtonImageRegion(int buttonId, TextureRegion region,float tw,float th){
        if(buttons.containsKey(buttonId)){
            ImageButton button= getButton(buttonId);
            if(region!=null){
                //( (TextureRegionDrawable)images.get(imageId)).setBuildRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region);

                ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(th);
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(th);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(th);


                if(th!=0){
                    button.setHeight(th);
                }
                if(tw!=0){
                    button.setWidth(tw);
                }
                //image.setName(region.getName());
                button.setVisible(true);
            }else {
                button.setVisible(false);
            }
        }else {
            Gdx.app.error("setButtonImageRegion","no image,id:"+buttonId);
        }
    }

    public void setImageRegionWidth(int imageId,TextureRegion region,float width,float rate){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            ((TextureRegionDrawable)image.getDrawable()).setRegion(region);
            image.setVisible(true);
            image.setWidth(width);

            image.setScale(rate);

        }
    }
    public void setImageRegionWidth(int imageId,float width,float rate){
        if(images.containsKey(imageId)){
            Image image= images.get(imageId);
            //( (TextureRegionDrawable)images.get(imageId)).setBuildRegion(region);
            image.setVisible(true);
            image.setWidth(width);

            image.setScale(rate);

        }
    }

    public void setImageRegionHeight(int imageId,float height,float rate){
        if(images.containsKey(imageId)){
            //( (TextureRegionDrawable)images.get(imageId)).setBuildRegion(region);
            Image image= images.get(imageId);
            image.setHeight(height);
            image.setVisible(true);
            image.setScale(rate);
        }
    }

    //只有这个检测labelscroll

    public void setScrollLabel(int labelId,String value){
        XmlReader.Element labelE=null;
        Array<XmlReader.Element> labelEs = groupE.getChildByName("labels").getChildrenByNameRecursively("label");  // 递归遍历，否则的话返回null
        for (int lE=0,lMax=labelEs.size;lE<lMax;lE++) {
            if(labelEs.get(lE).getInt("id")==labelId){
                labelE=labelEs.get(lE);
                break;
            }
        }
        if(labelE!=null){
            if(labelE.getBoolean("isScroll",false)){
                ScrollLabel scrollLabel=findActor("scrollLabel_"+labelE.getInt("id"));
                setLabelText(labelId,value,true);
                if(scrollLabel!=null){
                    scrollLabel.resetTextHeight();
                }
            }else{
                setLabelText(labelId,value,true);
            }
        }else{
            setLabelText(labelId,value,true);
        }
    }

    public void setLabelText(int labelId,String value,boolean ifWrap){
        if(labels.containsKey(labelId)&&ifWrap){
            Label l= labels.get(labelId);
            float w=l.getWidth();
            float h=l.getHeight();
            float x=0,y=0;
            XmlReader.Element labelE=null;
            if(w==0||h==0){
                Array<XmlReader.Element> labelEs = groupE.getChildByName("labels").getChildrenByNameRecursively("label");  // 递归遍历，否则的话返回null
                for (int lE=0,lMax=labelEs.size;lE<lMax;lE++) {
                    if(labelEs.get(lE).getInt("id")==labelId){
                        labelE=labelEs.get(lE);
                        break;
                    }
                }
                if(labelE!=null){
                    w=labelE.getFloat("w",0) ;
                    if(w==-1){
                        w=1280;
                    }
                    h=labelE.getFloat("h",0) ;
                    if(h==-1){
                        h=720;
                    }
                    x=labelE.getFloat("x",0) * stageW / 100+labelE.getFloat("refx",0);
                    y=labelE.getFloat("y",0) * stageH / 100+labelE.getFloat("refy",0);
                    //x=labelE.getFloat("x",0) * stageW / 100;
                    if(labelE.getBoolean("isScroll",false)){
                        ScrollLabel scrollLabel=findActor("scrollLabel_"+labelE.getInt("id"));
                        if(scrollLabel!=null){
                            scrollLabel.resetTextHeight();
                        }
                    }
                }
            }else {
                x=l.getX();
                y=l.getY();
            }
            l.setText(value);
            l.setWidth(w);
            l.setHeight(h);
            l.setX(x);
            if(labelE!=null){
                l.setY(y-h/2);
            }
            l.setVisible(true);
            l.setWrap(ifWrap);
        }else {
            setLabelText(labelId,value);
        }
    }

    public void setLabelText(int labelId,String value){
        if(labels!=null&&labels.containsKey(labelId)){
            Label label= labels.get(labelId);
            label.setText(value);
            label.setVisible(true);
        }
    }
    public void setLabelText(int labelId,int value){
        if(labels!=null&&labels.containsKey(labelId)){
            Label label= labels.get(labelId);
            label.setText(value);
            label.setVisible(true);
        }
    }
    public void setLabelText(int labelId,String value,String value2,Color color,float scale){
        if(labels.containsKey(labelId)){
            Label label= labels.get(labelId);
            label.setFontScale(scale);
            label.getText().clear();
            label.getText().appendLine(value);
            label.getText().appendLine(value2);
            label.setColor(color);
            label.setVisible(true);
        }
    }
    public void setLabelText(int labelId,String value,Color color,float scale){
        if(labels.containsKey(labelId)){
            //Gdx.app.error("setLabelText",labelId+":"+value);
            Label label=labels.get(labelId);
            label.setFontScale(scale);
            label.setText(value);
            label.setColor(color);
            label.setVisible(true);
        }
    }
    public void setLabelText(int labelId,String value,Color color){
        if(labels.containsKey(labelId)){
            //Gdx.app.error("setLabelText",labelId+":"+value);
            Label label=labels.get(labelId);
            //  label.setStyle(new Label.LabelStyle(label.getStyle().font,color));
            label.setColor(color);
            label.setText(value);
            label.setVisible(true);
        }
    }

    public void hidImage(int imageId){
        if(images.containsKey(imageId)){
            images.get(imageId).setVisible(false);
        }
    }
    public void hidButton(int buttonId){
        if(buttons!=null&&buttons.containsKey(buttonId)){
            getButton(buttonId).setVisible(false);
        }
    }
    public void hidTButton(int tbuttonId){
        if(tbuttons!=null&&tbuttons.containsKey(tbuttonId)){
            tbuttons.get(tbuttonId).setVisible(false);
        }
    }
    public TextButton getTButton(int tbuttonId){
        if(tbuttons!=null&&tbuttons.containsKey(tbuttonId)){
            return tbuttons.get(tbuttonId);
        }else{
            return null;
        }
    }


    public void hidLabel(int labelId){
        if(labels.containsKey(labelId)){
            labels.get(labelId).setVisible(false);
        }
    }


    public void showImage(int imageId){
        if(images!=null&&images.containsKey(imageId)){
            images.get(imageId).setVisible(true);
        }
    }
    public void showButton(int buttonId){
        if(buttonId==0&&windowId==0){
            int s=0;
        }
        if(buttons!=null&&buttons.containsKey(buttonId)){
            getButton(buttonId).setVisible(true);
        }
    }
    public void showTButton(int tbuttonId){
        if(tbuttons!=null&&tbuttons.containsKey(tbuttonId)){
            tbuttons.get(tbuttonId).setVisible(true);
        }
    }
    public void showLabel(int labelId){
        if(labels!=null&&labels.containsKey(labelId)){
            labels.get(labelId).setVisible(true);
        }
    }

    public void resize(float stageW,float stageH ){
        this.stageH=stageH;
        this.stageW=stageW;


        String  scaleAdaptive=  groupE.get("adaptScale","");
        String coverImage=groupE.get("coverImage","none");

        if(!ComUtil.isEmpty(scaleAdaptive)){
            switch (scaleAdaptive){
                case "actorAdaptive":
                    stageH=this.stageH*1280/this.stageW;
                    stageW=1280;
                    refX=(getMainGame().getWorldWidth()-stageW)/2;
                    refY=(getMainGame().getWorldHeight()-stageH)/2;
                    break;
                case "screenAdaptive":
                    stageH=this.stageH*1920/this.stageW;
                    stageW=1920;
                    refX=(getMainGame().getWorldWidth()-stageW)/2;
                    refY=(getMainGame().getWorldHeight()-stageH)/2;
                    break;
            }
        }
        if(!coverImage.equals("none")){
            Image image=findActor("coverImage");
            if(image!=null){
                image.setWidth(stageW);
                image.setHeight(stageH);
            }
        }



        if(groupE.getChildByName("images")!=null){
            //  imageIdsMap=new IntIntMap();
            Array<XmlReader.Element> imageEs= groupE.getChildByName("images").getChildrenByName("image");

            for (int i=0,iMax=imageEs.size;i<iMax;i++) {
                    Image image=getImage(imageEs.get(i).getInt("id"));
                if(image!=null){
                    GameUtil.resetImagePotion(image,imageEs.get(i),stageW,stageH);
                }
            }
        }
        if(groupE.getChildByName("buttons")!=null){

            Array<XmlReader.Element> buttonEs = groupE.getChildByName("buttons").getChildrenByNameRecursively("button");  // 递归遍历，否则的话返回null
            for (int b=0,bMax=buttonEs.size;b<bMax;b++) {
                ImageButton button=getButton(buttonEs.get(b).getInt("id"));
                if(button!=null){
                    GameUtil.resetImageButtonPotion(button,getMainGame(),buttonEs.get(b),stageW,stageH);
                }
            }
        }
        if(groupE.getChildByName("tbuttons")!=null){

            //tbuttonIdsMap=new IntIntMap();
            tbuttons=new IntMap<>();
            TextButton tbutton=null;
            //遍历window的buttons按钮

            Array<XmlReader.Element> tbuttonEs = groupE.getChildByName("tbuttons").getChildrenByNameRecursively("tbutton");  // 递归遍历，否则的话返回null
            for (int b=0,bMax=tbuttonEs.size;b<bMax;b++) {
                //Gdx.app.log("ui测试", button.get("remark"));
                tbutton=getTButton(tbuttonEs.get(b).getInt("id"));
                if(tbutton!=null){
                    tbutton=GameUtil.resetTextButtonPotion(tbutton,getMainGame(),tbuttonEs.get(b),stageW,stageH);
                }
                //  GameUtil.logImageButtonInfo(button);
                //Gdx.input.setInputProcessor(uiStage);
            }
        }


        if(groupE.getChildByName("labels")!=null){

            //labelsMap=new IntIntMap();
            Label label=null;
            BitmapFont font=null;
            Color color=null;
            String text=null;
            Array<XmlReader.Element> labelEs = groupE.getChildByName("labels").getChildrenByNameRecursively("label");  // 递归遍历，否则的话返回null
            for (int l=0,lMax=labelEs.size;l<lMax;l++) {

                font=mainGame.gameMethod.getFont(labelEs.get(l).get("font"));
                color=DefDAO.getColor(labelEs.get(l).get("color","WHITE"));
                //如果是自动调节的,需要保证text设置的内容可以获取到信息
                text=mainGame.gameMethod.getStrValueAndHaveDefault(labelEs.get(l).get("text",""),"");
                //Label label, BitmapFont font, Color color,  Element labelE, float uiStageWidth, float uiStageHeight
                if(labelEs.get(l).getBoolean("isScroll",false)){
                    ScrollLabel scrollLabel=findActor("scrollLabel_"+labelEs.get(l).getInt("id"));
                    if(scrollLabel!=null){
                        GameUtil.resetCHScrollLabelPotion(scrollLabel,mainGame,font,text,color, labelEs.get(l),stageW,stageH,getMainGame().gameConfig.gameFontScale);
                    }
                }else{
                    label=getLabel(labelEs.get(l).getInt("id"));
                    if(label!=null){
                        GameUtil.resetLabelPotion(label,labelEs.get(l),stageW,stageH);
                    }

                }
            }
        }

        moblieMaxH=-1;
        moblieMaxW=-1;

        setOrigin(Align.center);
        setOriginX(getMainGame().getWorldWidth()/2);
        setOriginY(getMainGame().getWorldHeight()/2);
        //0 minScaleAdaptive //在缩小后维持界面大小
        switch (scaleAdaptive){
            case "minScaleAdaptive"://在缩小后维持界面大小
                float x=0;
                float y=0;
                float w=0;
                float h=0;
                for(int i = 0, iMax = getChildren().size;i<iMax;i++){
                    Actor a=getChild(i);
                    if(a.getX()<x){
                        x=a.getX();
                    }
                    if(a.getY()<y){
                        y=a.getY();
                    }
                    if(a.getX()+a.getWidth()>w){
                        w=a.getX()+a.getWidth();
                    }
                    if(a.getY()+a.getHeight()>h){
                        h=a.getY()+a.getHeight();
                    }
                }
                if(x<0){
                    w=w-x;
                }
                if(y<0){
                    h=h-y;
                }
                float scaleX=getMainGame().getWorldWidth()/ w;
                float scaleY=getMainGame().getWorldHeight()/h;
                float v=Math.min(scaleX,scaleY);
                if(v<getScaleX()){
                    setScale(v);
                }
                break;
            case "minScaleAdaptiveAndInFoot"://在缩小后维持界面大小
                x=0;
                y=0;
                w=0;
                h=0;
                for(int i = 0, iMax = getChildren().size;i<iMax;i++){
                    Actor a=getChild(i);
                    if(a.getX()<x){
                        x=a.getX();
                    }
                    if(a.getY()<y){
                        y=a.getY();
                    }
                    if(a.getX()+a.getWidth()>w){
                        w=a.getX()+a.getWidth();
                    }
                    if(a.getY()+a.getHeight()>h){
                        h=a.getY()+a.getHeight();
                    }
                }
                if(x<0){
                    w=w-x;
                }
                if(y<0){
                    h=h-y;
                }
                scaleX=getMainGame().getWorldWidth()/ w;
                scaleY=getMainGame().getWorldHeight()/h;
                v=Math.min(scaleX,scaleY);
                if(v<getScaleX()){
                    y=getY()-getStageH()*(getScaleY()-v)/2;
                    setScale(v);
                    setY(y/v);
                }
                break;


            case "scaleAdaptive"://将界面保持在
                x=0;
                y=0;
                w=0;
                h=0;
                for(int i = 0, iMax = getChildren().size;i<iMax;i++){
                    Actor a=getChild(i);
                    if(a.getX()<x){
                        x=a.getX();
                    }
                    if(a.getY()<y){
                        y=a.getY();
                    }
                    if(a.getX()+a.getWidth()>w){
                        w=a.getX()+a.getWidth();
                    }
                    if(a.getY()+a.getHeight()>h){
                        h=a.getY()+a.getHeight();
                    }
                }
                if(x<0){
                    w=w-x;
                }
                if(y<0){
                    h=h-y;
                }
                scaleX=getMainGame().getWorldWidth()/w;
                scaleY=getMainGame().getWorldHeight()/h;
                if(getMainGame().getWorldWidth()<=w||getMainGame().getWorldHeight()<=h){
                    v=Math.min(scaleX,scaleY);
                    if(v<getScaleX()){
                        setScale(v);
                    }
                }else {
                    v=Math.max(scaleX,scaleY);
                    if(v>getScaleX()){
                        setScale(v);
                    }
                }
                break;
            case "actorAdaptive":
            case "screenAdaptive":
                scaleX=getMainGame().getWorldWidth()/stageW;
                scaleY=getMainGame().getWorldHeight()/stageH;
                v=Math.min(scaleX,scaleY);
                setScale(v);/*
                for(int i = 0, iMax = getChildren().size;i<iMax;i++){
                    Actor a=getChild(i);
                    a.setX(a.getX()+refX);
                    a.setY(a.getY()+refY);
                }*/
                break;
        }
        setX(getX()+refX*getScaleX());
        setY(getY()+refY*getScaleY());

    }

    public void hideAll() {

        if(images!=null){
            Iterator<IntMap.Entry<Image>> itB = images.iterator();
            while (itB.hasNext()) {
                Image i=itB.next().value;
                if(i!=null){
                    i.setVisible(false);
                }
            }
        }
        if(buttons!=null){
            Iterator<IntMap.Entry<ImageButton>> itB = buttons.iterator();
            while (itB.hasNext()) {
                ImageButton i=itB.next().value;
                if(i!=null){
                    i.setVisible(false);
                }
            }
        }
        if(labels!=null){
            Iterator<IntMap.Entry<Label>> itB = labels.iterator();
            while (itB.hasNext()) {
                Label i=itB.next().value;
                if(i!=null){
                    i.setVisible(false);
                }
            }
        }
        if(tbuttons!=null){
            Iterator<IntMap.Entry<TextButton>> itB = tbuttons.iterator();
            while (itB.hasNext()) {
                TextButton i=itB.next().value;
                if(i!=null){
                    i.setVisible(false);
                }
            }
        }
    }

    public void hideAllImage() {
        if(images!=null){
            Iterator<IntMap.Entry<Image>> itB = images.iterator();
            while (itB.hasNext()) {
                Image i=itB.next().value;
                if(i!=null){
                    i.setVisible(false);
                }
            }
        }
    }

    public void hideAllButton() {
        if(buttons!=null){
            Iterator<IntMap.Entry<ImageButton>> itB = buttons.iterator();
            while (itB.hasNext()) {
                ImageButton i=itB.next().value;
                if(i!=null){
                    i.setVisible(false);
                }
            }
        }
    }

    public void hideAllLabel() {
        if(labels!=null){
            Iterator<IntMap.Entry<Label>> itB = labels.iterator();
            while (itB.hasNext()) {
                Label i=itB.next().value;
                if(i!=null){
                    i.setVisible(false);
                }
            }
        }
    }


    public Image getImage(int imageId){
        if(images.containsKey(imageId)){
            return images.get(imageId);
        }
        return null;
    }

    public void imageToOffset(int imageId,float refX,float refY){
        if(images.containsKey(imageId)){
            Image image=images.get(imageId);
            image.setX(image.getX()+refX);
            image.setY(image.getY()+refY);
        }
        return ;
    }

    public void buttonToOffset(int buttonId,float refX,float refY){
        if(buttons.containsKey(buttonId)){
            ImageButton button=getButton(buttonId);
            button.setX(button.getX()+refX);
            button.setY(button.getY()+refY);
        }
        return ;
    }


    public Label getLabel(int labelId){
        if(labels==null){
            return null;
        }
        if(labels.containsKey(labelId)){
            return labels.get(labelId);
        }
        return null;
    }

    public ImageButton getButton(int buttonId){
        /*if(buttonId==10&&windowId==0){
            int s=0;
        }*/
        if(buttons.containsKey(buttonId)){
            return buttons.get(buttonId);
        }
        return null;
    }




    public WindowGroup (){

    }

    public static <T extends WindowGroup> T obtain(Class<T> type) {
        Pool<WindowGroup> pool = (Pool<WindowGroup>) Pools.get(type);
        WindowGroup actor = pool.obtain();
        return (T) actor;
    }

    public static WindowGroup obtain() {
        return obtain(WindowGroup.class);
    }



    @Override
    public void reset() {
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
        refX=0;
        refY=0;
        moblieMaxW=-1;
        moblieMaxH=-1;
        moblieX=0;
        moblieY=0;
    }


    @Override
    public boolean remove() {
        boolean remove = super.remove();
        if (remove) {
            Pools.free(this);
        }
        for(int i=0;i<getListeners().size;i++){
            removeListener(getListeners().get(i));
        }
        if(images!=null){
            images.clear();
            images=null;
        }
        if(buttons!=null){
            buttons.clear();
            buttons=null;
        }
        if(tbuttons!=null){
            tbuttons.clear();
            tbuttons=null;
        }
        if(labels!=null){
            labels.clear();
            labels=null;
        }
        setDebug(false);
        setVisible(false);
        return remove;
    }


    //启用该功能,应该在window标签设置  ifSaveXmlE="true"
    public XmlReader.Element getImageXmlE(int id) {
        if(xmlEs==null){
            return null;
        }
        return xmlEs.get("image_"+id);
    }
    public XmlReader.Element getButtonXmlE(int id) {
        if(xmlEs==null){
            return null;
        }
        return xmlEs.get("button_"+id);
    }
    public XmlReader.Element getLabelXmlE(int id) {
        if(xmlEs==null){
            return null;
        }
        return xmlEs.get("label_"+id);
    }


    public void setTBLabelAndFixWidth(int i, CharSequence text,int refW) {
        TextButton t=getTButton(i);
        if(t!=null){
            t.setVisible(true);
            t.getLabel().setText(text);
            t.setWidth(t.getLabel().getPrefWidth()+refW);
        }
    }

    public void setTBLabel(int i, CharSequence text) {
        TextButton t=getTButton(i);
        if(t!=null){
            t.setVisible(true);
            t.getLabel().setText(text);
        }
    }
    public void setTBLabel(int i, String text,Color color) {
        TextButton t=getTButton(i);
        if(t!=null){
            t.setVisible(true);
            t.getLabel().setText(text);
            t.getLabel().setColor(color);
        }
    }
    public void setTBLabel(int i, CharSequence text,Color color) {
        TextButton t=getTButton(i);
        if(t!=null){
            t.setVisible(true);
            t.getLabel().setText(text);
            t.getLabel().setColor(color);
        }
    }

    //h 一般为对齐的行长度  lineCount 字体长度

    public void setTButtonTextAndPotionAndAdaptWidth(int i, float x, float y, float h, int lineCount, String text) {
        TextButton t=getTButton(i);
        if(t!=null){
            t.setVisible(true);
            t.getLabel().setText(text);
            t.getLabel().setHeight(h);
            // t.getLabel().setWidth(w);
            t.getLabel().setWrap(false);
            float width=t.getLabel().getPrefWidth()/lineCount+10;
            t.setWidth(width);
            t.setHeight(h);
            t.setX(x);
            t.setY(y);
            t.getLabel().setWrap(true);
        }
    }


    //needTest
    public void setTButtonTextAndPotionAndAdaptHeight(int i, float x, float y, float w, int columnCount, String text) {
        TextButton t=getTButton(i);
        if(t!=null){
            t.setVisible(true);
            t.getLabel().setText(text);
            t.getLabel().setWidth(w);
            t.getLabel().setWrap(false);
            float height=t.getLabel().getPrefHeight()/columnCount+10;
            t.setWidth(w);
            t.setHeight(height);
            t.setX(x);
            t.setY(y);
            t.getLabel().setWrap(true);
        }
    }

    //设置去适应对平图片的大小
    //ifFixWidth  如果是true则适应宽,改变高 是false则适应高,改变宽
    public Texture setImageToFixSize(int imageId, Pixmap priviewMap,boolean ifFixWidth) {
        Image image=getImage(imageId);
        float w,h,x,y,refv;
        x=image.getX();
        y=image.getY();
        if(ifFixWidth){
            w=image.getWidth();
            h=image.getHeight()/w*priviewMap.getHeight();
            refv=(image.getHeight()-h)/2;
            image.setHeight(h);
            image.setY(y+refv);
        }else{
            h=image.getHeight();
            w=image.getWidth()/h*priviewMap.getWidth();
            refv=(image.getWidth()-w)/2;
            image.setWidth(w);
            image.setX(x+refv);
        }
        Texture t=null;
        if(image!=null){
            t=new Texture(priviewMap);
            ((TextureRegionDrawable)image.getDrawable()).setRegion(new TextureRegion(t));
        }
        return t;
    }


    //210318
    public void setImageToFixSize(int imageId, TextureRegion priviewMap,boolean ifFixWidth) {
        if(priviewMap==null){
            hidImage(imageId);
            return;
        }
        Image image=getImage(imageId);
        float w,h,x,y,refv;
        x=image.getX();
        y=image.getY();
        if(ifFixWidth){
            w=image.getWidth();
            h=image.getHeight()/w*priviewMap.getRegionHeight();
            refv=(image.getHeight()-h)/2;
            image.setHeight(h);
            image.setY(y+refv);
        }else{
            h=image.getHeight();
            w=h/priviewMap.getRegionHeight()*priviewMap.getRegionWidth();
            refv=(image.getWidth()-w)/2;
            image.setWidth(w);
            image.setX(x+refv);
        }
        if(image!=null){
            ((TextureRegionDrawable)image.getDrawable()).setRegion(priviewMap);
            image.setVisible(true);
        }else {
            image.setVisible(false);
        }
    }

    //210318
    public void setImageButtonToFixSize(int buttonId, TextureRegion region,boolean ifFixWidth) {
        ImageButton button=getButton(buttonId);
        float w,h,x,y,refv;
        x=button.getX();
        y=button.getY();
        if(ifFixWidth){
            w=button.getWidth();
            h=button.getHeight()/w*region.getRegionHeight();
            refv=(button.getHeight()-h)/2;
            button.setHeight(h);
            button.setY(y+refv);
        }else{
            h=button.getHeight();
            w=h/region.getRegionHeight()*region.getRegionWidth();
            refv=(button.getWidth()-w)/2;
            button.setWidth(w);
            button.setX(x+refv);
        }
        if(button!=null){
            // setButtonImageNotChangeSize(buttonId,region);
            ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(region);
            ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(region);

            ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(button.getHeight());

            button.setVisible(true);
        }else {
            button.setVisible(false);
        }
    }

    public void setImageRegionAndToFixImage(int imageId, int targetImageid, TextureRegionDAO region, int refX, int refY) {
        Image image=getImage(imageId);
        Image image2=getImage(targetImageid);
        if(image!=null&&image2!=null){
            ((TextureRegionDrawable)image.getDrawable()).setRegion(region.getTextureRegion());
            image.setHeight(region.getTextureRegion().getRegionHeight());
            image.setWidth(region.getTextureRegion().getRegionWidth());

            image.setName(region.getName());
            image.setX(image2.getX()+refX);
            image.setY(image2.getY()+refY);
            image.setVisible(true);
        }
    }

    public void setImageToFixImage(int imageId, int targetImageid,int refX,int refY) {
        Image image=getImage(imageId);
        Image image2=getImage(targetImageid);
        if(image!=null&&image2!=null){
            image.setWidth(image2.getWidth()+refX);
            image.setHeight(image2.getHeight()+refY);
            image.setX(image2.getX()-refX/2);
            image.setY(image2.getY()-refY/2);
            image.setVisible(true);
        }
    }

    public void setImageToFix(int imageId, int refW,int refH){
        Image image=getImage(imageId);
        if(image!=null){
            image.setWidth(image.getWidth()+refW);
            image.setHeight(image.getHeight()+refH);
            image.setX(image.getX()-refW/2);
            image.setY(image.getY()-refH/2);
            image.setVisible(true);
        }
    }


    public void setImageToFixImage(int imageId, int targetImageid,int borderLW,int borderRW,int borderTH,int borderFH) {
        Image image=getImage(imageId);
        Image image2=getImage(targetImageid);
        if(image!=null&&image2!=null){
            image.setWidth(image2.getWidth()+borderLW+borderRW);
            image.setHeight(image2.getHeight()+borderTH+borderFH);
            image.setX(image2.getX()-borderLW);
            image.setY(image2.getY()-borderFH);
            image.setVisible(true);
        }
    }


    //设置某个会变换坐标的,使用图片2作为参考标志
    public void setImageButtonToFixImageButton(int buttonId, int targetButtonId,int refW,int refH) {

        ImageButton button=getButton(buttonId);
        ImageButton button2=getButton(targetButtonId);
        if(button!=null&&button2!=null){
            button.setWidth(button2.getWidth()+refW);
            button.setHeight(button2.getHeight()+refH);


            ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(button.getHeight());


            button.setX(button2.getX()-refW/2);
            button.setY(button2.getY()-refH/2);
            button.setVisible(true);
        }

    }

    public void setImageButtonToFixImage(int buttonId, int targetImageId,int refW,int refH) {

        ImageButton button=getButton(buttonId);
        Image targetImage=getImage(targetImageId);
        if(button!=null&&targetImage!=null){
            button.setWidth(targetImage.getWidth()+refW);
            button.setHeight(targetImage.getHeight()+refH);

            if(button.getStyle().imageUp!=null){
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(button.getWidth());
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(button.getHeight());
            }
            if(button.getStyle().imageDown!=null){
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(button.getWidth());
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(button.getHeight());
            }
            if(button.getStyle().imageChecked!=null){
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(button.getWidth());
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(button.getHeight());
            }
            button.setX(targetImage.getX()-refW/2);
            button.setY(targetImage.getY()-refH/2);
            button.setVisible(true);


        }

    }

    //设置某个会变换坐标的,使用图片2作为参考标志
    public void setImageByTargetImage(int imageId, int targetImageid,TextureRegionDAO textureRegionDAO,int refx,int refy) {
        Image image=getImage(imageId);
        Image image2=getImage(targetImageid);
        XmlReader.Element xmlE=getImageXmlE(imageId);
        if(textureRegionDAO==null){
            image.setVisible(false);
            return;
        }
        if(xmlE!=null){
            int h=xmlE.getInt("w",0);
            int w=xmlE.getInt("h",0);
            if(h==0){ h=100;}
            if(w==0){ w=100; }
            ((TextureRegionDrawable)image.getDrawable()).setRegion(textureRegionDAO.getTextureRegion());
            image.setHeight(textureRegionDAO.getTextureRegion().getRegionHeight()*h/100);
            image.setWidth(textureRegionDAO.getTextureRegion().getRegionWidth()*w/100);

            image.setName(textureRegionDAO.getName());
            image.setVisible(true);

            image.setX(image2.getX()-textureRegionDAO.getRefx()*w/100+image2.getWidth()/2 +refx);
            image.setY(image2.getY()-textureRegionDAO.getRefy()*h/100+image2.getHeight()/2+refy);
        }else {
            ((TextureRegionDrawable)image.getDrawable()).setRegion(textureRegionDAO.getTextureRegion());
            image.setHeight(textureRegionDAO.getTextureRegion().getRegionHeight());
            image.setWidth(textureRegionDAO.getTextureRegion().getRegionWidth());

            image.setName(textureRegionDAO.getName());
            image.setVisible(true);

            image.setX(image2.getX()-textureRegionDAO.getRefx()+image2.getWidth()/2 +refx);
            image.setY(image2.getY()-textureRegionDAO.getRefy()+image2.getHeight()/2+refy);
        }
    }
    public void setButtonImageByTargetImage(int buttonId, int targetImageid,TextureRegionDAO textureRegionDAO,int refx,int refy) {
        ImageButton button=getButton(buttonId);
        Image image2=getImage(targetImageid);
        XmlReader.Element xmlE=getButtonXmlE(buttonId);
        if(textureRegionDAO==null){
            button.setVisible(false);
            return;
        }
        if(xmlE!=null){
            int h=xmlE.getInt("w",0);
            int w=xmlE.getInt("h",0);
            if(h==0){ h=100;}
            if(w==0){ w=100; }
            int tw=textureRegionDAO.getTextureRegion().getRegionWidth()*w/100;
            int th=textureRegionDAO.getTextureRegion().getRegionHeight()*h/100;
            button.setHeight(th);
            button.setWidth(tw);


            if(button.getStyle().imageDown!=null){
                ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(textureRegionDAO.getTextureRegion());
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(th);
            }
            if(button.getStyle().imageChecked!=null){
                ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(textureRegionDAO.getTextureRegion());
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(th);
            }
            if(button.getStyle().imageUp!=null){
                ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(textureRegionDAO.getTextureRegion());
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(th);
            }

            button.setName(textureRegionDAO.getName());
            button.setVisible(true);

            button.setX(image2.getX()-textureRegionDAO.getRefx()*w/100+image2.getWidth()/2 +refx);
            button.setY(image2.getY()-textureRegionDAO.getRefy()*h/100+image2.getHeight()/2+refy);
        }else {

            int tw=textureRegionDAO.getTextureRegion().getRegionWidth();
            int th=textureRegionDAO.getTextureRegion().getRegionHeight();
            button.setHeight(th);
            button.setWidth(tw);


            if(button.getStyle().imageDown!=null){
                ((TextureRegionDrawable)button.getStyle().imageDown).setRegion(textureRegionDAO.getTextureRegion());
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(th);
            }
            if(button.getStyle().imageChecked!=null){
                ((TextureRegionDrawable)button.getStyle().imageChecked).setRegion(textureRegionDAO.getTextureRegion());
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(th);
            }
            if(button.getStyle().imageUp!=null){
                ((TextureRegionDrawable)button.getStyle().imageUp).setRegion(textureRegionDAO.getTextureRegion());
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(tw);
                ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(th);
            }
            button.setName(textureRegionDAO.getName());
            button.setVisible(true);

            button.setX(image2.getX()-textureRegionDAO.getRefx()+image2.getWidth()/2 +refx);
            button.setY(image2.getY()-textureRegionDAO.getRefy()+image2.getHeight()/2+refy);
        }
    }


    public void addImg(int imageId,Image bgImg) {
        if(!images.containsKey(imageId)){
            addActor(bgImg);
            bgImg.setVisible(true);
        }else{
            Image l=getImage(imageId);
            removeActor(l);
            addActor(bgImg);
            bgImg.setVisible(true);
        }
        images.put(imageId,bgImg);
    }

    public void addImgButton(int buttonId,ImageButton button){
        if(!buttons.containsKey(buttonId)){
            addActor(button);
            button.setVisible(true);
        }else{
            ImageButton l=getButton(buttonId);
            removeActor(l);
            addActor(button);
            button.setVisible(true);
        }
        buttons.put(buttonId,button);
    }

    public void addLabel(int labelId,Label label){
        if(!labels.containsKey(labelId)){
            addActor(label);
            label.setVisible(true);
        }else{
            Label l=getLabel(labelId);
            removeActor(l);
            addActor(label);
            l.setVisible(true);
        }
        labels.put(labelId,label);
    }

    public void setImageButtonForScale(int buttonId, TextureRegionDAO regionDAO,int x,int y, float scale) {
        ImageButton button=getButton(buttonId);
        if(button!=null){
            float w=regionDAO.getW()*scale;
            float h=regionDAO.getH()*scale;
            button.setWidth(w);
            button.setHeight(h);
            ((TextureRegionDrawable)button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable)button.getStyle().imageChecked).setMinHeight(button.getHeight());
            x= (int) (x-regionDAO.getRefx()*scale);
            y= (int) (y-regionDAO.getRefy()*scale);
            button.setX(x);
            button.setY(y);
            button.setVisible(true);
        }
    }
    public void setImageForScale(int imageId, TextureRegionDAO regionDAO,int x,int y, float refx,float refy,float scale) {
        Image image=getImage(imageId);
        if(image!=null){
            float w=regionDAO.getW()*scale;
            float h=regionDAO.getH()*scale;
            image.setWidth(w);
            image.setHeight(h);
            ((TextureRegionDrawable)image.getDrawable()).setRegion(regionDAO.getTextureRegion());
            ((TextureRegionDrawable)image.getDrawable()).setMinWidth(image.getWidth());
            ((TextureRegionDrawable)image.getDrawable()).setMinHeight(image.getHeight());
            x= (int) (x-regionDAO.getRefx()*scale+refx);
            y= (int) (y-regionDAO.getRefy()*scale+refy);
            image.setX(x);
            image.setY(y);
            image.setVisible(true);
        }
    }

    //设置滑动监听
    public void setMobileListener(final float moblieMaxW,final float moblieMaxH) {
        this.moblieMaxH = moblieMaxH;
        this.moblieMaxW = moblieMaxW;
        if (moblieMaxW == -1 && moblieMaxH == -1) {
            return;
        }
    }


    public void setGroupPotion(float x,float y) {
        float refX=0;
        float refY=0;
        if(x!=-1f){
            refX=getX()-x;
            setX(x);
        }
        if(y!=-1f){
            refY=getY()-y;
            setY(y);
        }
        if(images!=null){
            Iterator<IntMap.Entry<Image>> itB = images.iterator();
            while (itB.hasNext()) {
                Image i=itB.next().value;
                if(i!=null){
                    i.setPosition(i.getX()+refX,i.getY()+refY);
                }
            }
        }
        if(buttons!=null){
            Iterator<IntMap.Entry<ImageButton>> itB = buttons.iterator();
            while (itB.hasNext()) {
                ImageButton i=itB.next().value;
                if(i!=null){
                    i.setPosition(i.getX()+refX,i.getY()+refY);
                }
            }
        }
        if(labels!=null){
            Iterator<IntMap.Entry<Label>> itB = labels.iterator();
            while (itB.hasNext()) {
                Label i=itB.next().value;
                if(i!=null){
                    i.setPosition(i.getX()+refX,i.getY()+refY);
                }
            }
        }

    }

    public void printLog() {
        Gdx.app.log("windowGroupLog","x:"+getX()+" y:"+getY()+" w:"+getWidth()+" h:"+getHeight());
    }

    public void moveActor(float moveX,float moveY,float second,int[] imageIds,int[] buttonIds,int[] labelIds,int[] tButtonIds) {



        if(images!=null&&imageIds!=null){
            for(int i=0;i<imageIds.length;i++){
                Image image=getImage(imageIds[i]);
                if(image!=null){
                    MoveByAction action = Actions.moveBy(moveX, moveY, second);
                    image.addAction(action);
                }
            }
        }
        if(buttons!=null&&buttonIds!=null){
            for(int i=0;i<buttonIds.length;i++){
                ImageButton button=getButton(buttonIds[i]);
                if(button!=null){
                    MoveByAction action = Actions.moveBy(moveX, moveY, second);
                    button.addAction(action);
                }
            }
        }
        if(labels!=null&&labelIds!=null){
            for(int i=0;i<labelIds.length;i++){
                Label label=getLabel(labelIds[i]);
                if(label!=null){
                    MoveByAction action = Actions.moveBy(moveX, moveY, second);
                    label.addAction(action);
                }
            }
        }
        if(tbuttons!=null&&tButtonIds!=null){
            for(int i=0;i<tButtonIds.length;i++){
                TextButton tButton=getTButton(tButtonIds[i]);
                if(tButton!=null){
                    MoveByAction action = Actions.moveBy(moveX, moveY, second);
                    tButton.addAction(action);
                }
            }
        }


    }

    public void createLabels() {
        if(labels==null){
            labels=new IntMap();
        }
    }



    public void setLabelRefPotion(int labelId,float refx,float refy){
        if(groupE.getChildByName("labels")!=null&&images.containsKey(labelId)){
            Array<XmlReader.Element> labelEs= groupE.getChildByName("labels").getChildrenByName("label");
            XmlReader.Element imageE2=null;
            for( XmlReader.Element labelE:labelEs){
                if(labelE.getInt("id")==labelId){
                    imageE2=labelE;
                    break;
                }
            }
            if(imageE2==null){
                hidLabel(labelId);
                Gdx.app.error("setImageRefPotion is error","not imageE");
            }else {
                Label image=labels.get(labelId);
                image= GameUtil.resetLabelPotion(image,imageE2,stageW,stageH);
                image.setPosition(image.getX()+refx*getScaleX(),image.getY()-refy*getScaleY());
            }
        }
    }

    public void setLabelPotionByImageButton(int labelId,int buttionId,float refx,float refy){
        Label label=labels.get(labelId);
        ImageButton imageButton =buttons.get(buttionId);
        if(label!=null&&imageButton!=null){
            label.setPosition(imageButton.getX()+refx,imageButton.getY()-refy);
        }
    }



    public float getStageH() {
        return stageH;
    }

    public float getStageW() {
        return stageW;
    }

    public void resetButtonPotion(int buttonId, int x, int y, int w, int h, int refX, int refY,boolean ifBorder) {
        ImageButton b=getButton(buttonId);
        if(b!=null){
            b=GameUtil.resetImageButtonPotion(b,getMainGame(),x,y,w,h,refX,refY,stageW,stageH,ifBorder);
            b.setVisible(true);
        }else{
            b.setVisible(false);
        }
    }

    public void resetImagePotion(int imageId, int x, int y, int w, int h, int refX, int refY,boolean ifBorder) {
        Image i=getImage(imageId);
        if(i!=null){
            i=GameUtil.resetImagePotion(getMainGame(),i,x,y,w,h,refX,refY,stageW,stageH,ifBorder,false);//Image image,  int x, int y, int w, int h, int refx, int refy, float uiStageWidth, float uiStageHeight,boolean ifBorder
            i.setVisible(true);
        }else{
            i.setVisible(false);
        }
    }

    public void resetLabelPotion(int labelId, int x, int y, int w, int h, int refX, int refY,boolean ifBorder) {
        Label l=getLabel(labelId);
        if(l!=null){
            l=GameUtil.resetLabelPotion(l,x,y,w,h,refX,refY,stageW,stageH,ifBorder);//Image image,  int x, int y, int w, int h, int refx, int refy, float uiStageWidth, float uiStageHeight,boolean ifBorder
            l.setVisible(true);
        }else{
            l.setVisible(false);
        }
    }


    public void addImgPotionForX(int imagId, float refX) {
        Image i= getImage(imagId);
        if(i!=null){
            i.setX(i.getX()+refX);
        }
    }
    public void addImgPotionForY(int imagId, float refY) {
        Image i= getImage(imagId);
        if(i!=null){
            i.setY(i.getY()+refY);
        }
    }

    public void addLabelPotionForX(int labelId, float refX) {
        Label l= getLabel(labelId);
        if(l!=null){
            l.setX(l.getX()+refX);
        }
    }

    public void addLabelPotionForY(int labelId, float refY) {
        Label l= getLabel(labelId);
        if(l!=null){
            l.setY(l.getY()+refY);
        }
    }

    public void addTButtonPotionForX(int tButtonId, float refX) {
        TextButton t= getTButton(tButtonId);
        if(t!=null){
            t.setX(t.getX()+refX);
        }
    }
    public void addTButtonPotionForY(int tButtonId, float refY) {
        TextButton t= getTButton(tButtonId);
        if(t!=null){
            t.setY(t.getY()+refY);
        }
    }

    public void addButtonPotionForX(int buttonId, float refX) {
        Button b= getButton(buttonId);
        if(b!=null){
            b.setX(b.getX()+refX);
        }
    }
    public void addButtonPotionForY(int buttonId, float refY) {
        Button b= getTButton(buttonId);
        if(b!=null){
            b.setY(b.getY()+refY);
        }
    }


    public void setTButtonPotionToFixTButton(int tbId, int ttbId, int refX, int refY) {
        TextButton tb=getTButton(tbId);
        TextButton ttb=getTButton(ttbId);
        if(tb!=null&&ttb!=null){
            tb.setX(ttb.getX()+refX);
            tb.setY(ttb.getY()+refY);
            tb.setVisible(true);
        }
    }


    public void setTButtonXAndWToFixImage(int tButtonId, int targetImageId) {

        TextButton button=getTButton(tButtonId);
        Image targetImage=getImage(targetImageId);
        if(button!=null&&targetImage!=null){
            button.setWidth(targetImage.getWidth());

            button.setX(targetImage.getX());
            button.setVisible(true);

        }

    }

    //使用此功能xml需要对应开启ifSaveXmlE="true"
    public void logAllActorForStageCoord(){

       // public IntMap<Image> images;
       // public IntMap<ImageButton> buttons;
       // public IntMap<TextButton> tbuttons;
       // public IntMap<Label> labels;
        if(xmlEs==null){
            return;
        }
        if(tempVector2==null){
            tempVector2=new Vector2(0,0);
        }

        if(images!=null&&images.size>0){
            Iterator<IntMap.Entry<Image>> it = images.iterator();
            while (it.hasNext()) {
                IntMap.Entry<Image> c = it.next();
                if (c.value != null) {
                    XmlReader.Element xmlE=xmlEs.get("image_"+c.key);
                    if(xmlE==null){
                        continue;
                    }
                    tempVector2.setZero();
                    tempVector2=c.value.localToStageCoordinates(tempVector2);
                    int x=xmlE.getInt("x",100);
                    int y=xmlE.getInt("y",100);
                    int refX= (int) (tempVector2.x-1980*x/100);
                    int refY= (int) (tempVector2.y-1080*y/100);

                    Gdx.app.log("logAllActorForStageCoord image "+c.key,
                            " x:"+x+
                                    " y:"+y+
                                    " refX:"+refX+
                                    " refY:"+refY+" rs:"+
                            tempVector2.x+" "+tempVector2.y);
                }
            }
        }

        if(buttons!=null&&buttons.size>0){
            Iterator<IntMap.Entry<ImageButton>> it = buttons.iterator();
            while (it.hasNext()) {
                IntMap.Entry<ImageButton> c = it.next();
                if (c.value != null) {
                    tempVector2.setZero();
                    tempVector2=c.value.localToStageCoordinates(tempVector2);
                    Gdx.app.log("logAllActorForStageCoord button "+c.key,tempVector2.x+" "+tempVector2.y);
                }
            }
        }
        if(tbuttons!=null&&tbuttons.size>0){
            Iterator<IntMap.Entry<TextButton>> it = tbuttons.iterator();
            while (it.hasNext()) {
                IntMap.Entry<TextButton> c = it.next();
                if (c.value != null) {
                    tempVector2.setZero();
                    tempVector2=c.value.localToStageCoordinates(tempVector2);
                    Gdx.app.log("logAllActorForStageCoord tbutton "+c.key,tempVector2.x+" "+tempVector2.y);
                }
            }
        }
        if(labels!=null&&labels.size>0){
            Iterator<IntMap.Entry<Label>> it = labels.iterator();
            while (it.hasNext()) {
                IntMap.Entry<Label> c = it.next();
                if (c.value != null) {
                    tempVector2.setZero();
                    tempVector2=c.value.localToStageCoordinates(tempVector2);
                    Gdx.app.log("logAllActorForStageCoord label "+c.key,tempVector2.x+" "+tempVector2.y);
                }
            }
        }
    }

    public void setLabelColor(int i, Color green) {
        Label l=getLabel(i);
        if(l!=null){
            l.setColor(green);
        }
    }

    public void setButtonColor(int i, Color color) {
        ImageButton b=getButton(i);
        if(b!=null){
            b.setColor(color);
        }
    }
    public void setImageColor(int i, Color color) {
        Image b=getImage(i);
        if(b!=null){
            b.setColor(color);
        }
    }



    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
       // super.drawChildren(batch, parentAlpha);
        Color c=batch.getColor();
        parentAlpha *= getColor().a;
        SnapshotArray<Actor> children = getChildren();
        Actor[] actors = children.begin();
        Rectangle cullingArea =getCullingArea();
        if (cullingArea != null) {
            // Draw children only if inside culling area.
            float cullLeft = cullingArea.x;
            float cullRight = cullLeft + cullingArea.width;
            float cullBottom = cullingArea.y;
            float cullTop = cullBottom + cullingArea.height;
            if (isTransform()) {
                for (int i = 0, n = children.size; i < n; i++) {
                    Actor child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.getX(), cy = child.getY();
                    if (cx <= cullRight && cy <= cullTop && cx + child.getWidth() >= cullLeft && cy + child.getHeight() >= cullBottom)
                        batch.setColor(child.getColor());
                        child.draw(batch, parentAlpha);
                }
            } else {
                // No transform for this group, offset each child.
                float offsetX = getX(), offsetY = getY();
             float   x = 0;
                float   y = 0;
                for (int i = 0, n = children.size; i < n; i++) {
                    Actor child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.getX(), cy = child.getY();
                    if (cx <= cullRight && cy <= cullTop && cx + child.getWidth() >= cullLeft && cy + child.getHeight() >= cullBottom) {
                        batch.setColor(child.getColor());
                        child.setX(cx + offsetX);
                        child.setY(cy + offsetY);
                        child.draw(batch, parentAlpha);
                        child.setX(cx);
                        child.setY(cy);
                    }
                }
                x = offsetX;
                y = offsetY;
                setX(x);
                setY(y);
            }
        } else {
            // No culling, draw all children.
            if (isTransform()) {
                for (int i = 0, n = children.size; i < n; i++) {
                    Actor child = actors[i];
                    if (!child.isVisible()) continue;
                    batch.setColor(child.getColor());
                    child.draw(batch, parentAlpha);
                }
            } else {
                // No transform for this group, offset each child.
                float offsetX = getX(), offsetY = getY();
                float  x = 0;
                float  y = 0;
                for (int i = 0, n = children.size; i < n; i++) {
                    Actor child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.getX(), cy = child.getY();
                    batch.setColor(child.getColor());
                    child.setX(cx + offsetX);
                    child.setY(cy + offsetY);
                    child.draw(batch, parentAlpha);
                    child.setX(cx);
                    child.setY(cy);
                }
                x = offsetX;
                y = offsetY;
                setX(x);
                setY(y);
            }
        }
        children.end();
        batch.setColor(c);
    }


    @Override
    public void setVisible ( boolean setValue) {
        super.setVisible(setValue);
    }

    public void setVisibleForEffect(final boolean setValue){
        Gdx.app.log("setVisibleForEffect:"+windowId,setValue+"");
        if(setValue){
            ifVisable=true;
           setVisible(setValue);
            switch(groupE.get("effect")){
                case "popup":
                   /* int imageId=groupE.getInt("effectImageId",0);
                    if(imageId>0&&images.containsKey(imageId)){
                        Image image=getImage(imageId);
                        //setWidth(image.getWidth());
                        //setHeight(image.getHeight());
                        setOrigin(image.getX()+image.getWidth()/2,image.getY()+image.getHeight()/2);
                        setScale(0.5f);
                        ScaleToAction  action = Actions.scaleTo(1f,1f,0.2f);
                        addAction(action);
                    }*/
                    float ox=getX()+getWidth()/2;
                    float oy=getY()+getHeight()/2;
                    if(ox==0){ ox=stageW/2;}
                    if(oy==0){ oy=stageH/2;}
                    setOrigin(ox,oy);
                    setScale(0.5f);
                    ScaleToAction  action = Actions.scaleTo(1f,1f,0.2f);
                    addAction(action);
                    break;
            }
        }else{
            ifVisable=false;
            switch(groupE.get("effect")){
                case "popup":
                    float ox=getX()+getWidth()/2;
                    float oy=getY()+getHeight()/2;
                    if(ox==0){ ox=stageW/2;}
                    if(oy==0){ oy=stageH/2;}
                    setOrigin(ox,oy);
                    //setScale(0.5f);

// Runnable 动作
                    RunnableAction endAction = Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            if(!ifVisable){
                                setVisible(setValue);
                            }
                        }
                    });
                    SequenceAction sequence = Actions.sequence(
                            Actions.scaleTo(0f,0f,0.2f),endAction
                    );

                    addAction(sequence);
                    break;


                default:
                   setVisible(setValue);

                    break;
            }
        }
    }
}
















