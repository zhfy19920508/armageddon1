package com.zhfy.game.model.content;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.zhfy.game.model.content.conversion.Fb2Smap;

import java.util.Iterator;


public class LabelList {
    private Array<Label> labels;
    private BitmapFont font;
    private Color color;
    private Stage stage;
    private float scale;

    public LabelList(Stage stage, BitmapFont font,float scale){
        labels=new Array<>();
        this.font=font;
        this.stage=stage;
        this.scale=scale;
    }
    public Label get(int i,Color color){
        if(i>=labels.size){
            Label l=new Label("",new Label.LabelStyle(font,Color.WHITE));
            l.setFontScale(scale);
            l.setColor(color);
            labels.add(l);
            stage.addActor(l);
            return labels.get(i);
        }else {
            Label l=labels.get(i);
            l.setColor(color);
            /*if(stage.getRoot().removeActor(l)){
                stage.addActor(l);
            }*/



            return l;
        }
    }
    public void dispose(){
        labels.clear();
    }

    public Label getLabel(Color color){
        Iterator<Label> it = labels.iterator();
        while (it.hasNext()) {
            Label ca = it.next();
            if(ca.getColor().a==0){
                ca.setColor(color);
               /* if(stage.getRoot().removeActor(ca)){
                    stage.addActor(ca);
                }*/
                return ca;
            }
        }
                Label l=new Label("",new Label.LabelStyle(font,Color.WHITE));
                l.setFontScale(scale);
                l.setColor(color);
                labels.add(l);
                stage.addActor(l);
                return l;
    }

}
