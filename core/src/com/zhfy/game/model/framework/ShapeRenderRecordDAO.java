package com.zhfy.game.model.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.zhfy.game.framework.GameUtil;


//废弃
public class ShapeRenderRecordDAO {
    Array<ShapeRecord> shapeRecords;
    public ShapeRenderer shapeRenderer;

    public ShapeRenderRecordDAO(){
        init();
    }

    public class ShapeRecord{

        public ShapeRecord(int type,Color color,float... args){
            this.type=type;
            this.color=color;
            values=args;
        }

        private int type;
        private float values[];
        private Color color;

        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }
        public float[] getValues() {
            return values;
        }
        public void setValues(float[] values) {
            this.values = values;
        }
        public Color getColor() {
            return color;
        }
    }

    public void init(){
        if(shapeRecords==null){
            shapeRecords=new Array<>();
        }else {
            shapeRecords.clear();
        }
        if(shapeRenderer==null){
            shapeRenderer=new ShapeRenderer();
        }
    }

    public void record(int type,Color color,float... args){
       if(shapeRecords==null){
           init();
       }
        shapeRecords.add(new ShapeRecord(type,color,args));
    }

    public void drawShaperRenders(){
        if(shapeRecords==null||shapeRecords.size==0){
            return;
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int i=0,iMax=shapeRecords.size;i<iMax;i++){
            drawShaperRender(shapeRecords.get(i));
        }
        shapeRenderer.end();
    }

    //0圆形
    //1三角形
    //2扇形
    private void drawShaperRender(ShapeRecord shapeRecord) {
       if(!shapeRenderer.getColor().equals(shapeRecord.getColor())){
           shapeRenderer.setColor(shapeRecord.getColor());
       }
        switch (shapeRecord.getType()){
            case 0:shapeRenderer.circle((shapeRecord.getValues()[0]),(shapeRecord.getValues()[1]),(shapeRecord.getValues()[2]));   break;
            case 1:shapeRenderer.triangle((shapeRecord.getValues()[0]),(shapeRecord.getValues()[1]), (shapeRecord.getValues()[2]), (shapeRecord.getValues()[3]), (shapeRecord.getValues()[4]), (shapeRecord.getValues()[5]));  break;
           // case 2:  GameUtil.drawSectorByShapeRenderer(shapeRenderer,(shapeRecord.getValues()[0]),(shapeRecord.getValues()[1]),(shapeRecord.getValues()[2]),(shapeRecord.getValues()[3]),(shapeRecord.getValues()[4])); break;
        }
    }



    public void dispose(){
        if(shapeRenderer!=null){
            shapeRenderer.dispose();
            shapeRenderer=null;
        }
        if(shapeRecords!=null){
            shapeRecords.clear();
            shapeRecords=null;
        }
    }
}


