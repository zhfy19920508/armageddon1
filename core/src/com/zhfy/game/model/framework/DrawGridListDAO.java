package com.zhfy.game.model.framework;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class DrawGridListDAO {
    private Array<DrawGridDAO> drawCycleList;
    
    public Array<DrawGridDAO> getdrawCycleList() {
        return drawCycleList;
    }
    
    public void setdrawCycleList(Array<DrawGridDAO> drawCycleList) {
        this.drawCycleList = drawCycleList;
    }
    
    public void init() {
        drawCycleList = new Array<DrawGridDAO>();
    }
    
    public void add(DrawGridDAO drawCycle) {
        if (drawCycleList == null) {
            init();
        }
        drawCycleList.add(drawCycle);
    }
    
    public int size() {
        if (drawCycleList == null) {
            init();
        }
        return drawCycleList.size;
    }
    
    public void remove(int i) {
        drawCycleList.removeIndex(i);
    }
    
    
    public void clear() {
        if (drawCycleList != null) {
            drawCycleList.clear();
        }
        
    }
    
    public DrawGridDAO get(int i) {
        return drawCycleList.get(i);
    }
}
