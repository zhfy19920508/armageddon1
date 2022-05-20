package com.zhfy.game.model.framework;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class DrawListDAO {
private Array<DrawDAO> drawLandList;
    
    public Array<DrawDAO> getdrawLandList() {
        return drawLandList;
    }
    
    public void setdrawLandList(Array<DrawDAO> drawLandList) {
        this.drawLandList = drawLandList;
    }
    
    public void init() {
        drawLandList = new Array<DrawDAO>();
    }
    
    public void add(DrawDAO drawCycle) {
        if (drawLandList == null) {
            init();
        }
        drawLandList.add(drawCycle);
    }
    
    public int size() {
        if (drawLandList == null) {
            init();
        }
        return drawLandList.size;
    }
    
    public void remove(int i) {
        drawLandList.removeIndex(i);
    }
    
    
    public void clear() {
        if (drawLandList != null) {
            drawLandList.clear();
        }
        
    }
    
    public DrawDAO get(int i) {
        return drawLandList.get(i);
    }
}
