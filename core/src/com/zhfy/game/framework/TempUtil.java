package com.zhfy.game.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.zhfy.game.MainGame;

import java.util.Iterator;

public class TempUtil {
    //临时工具类,使用Intarray等所有的都从这里获取

    private Array<IntArray> tempIntArrays;
    private Array<IntIntMap> tempIntIntMaps;
    private Array<Array> tempArrays;
   // private ObjectMap<Object,Boolean> ifUse;
   private ObjectMap<Object,Boolean> ifUse;
    private MainGame game;
      //  int c=0;

    public int getUseCount(){
        return ifUse.size;
    }

    public TempUtil(MainGame game){
        tempIntArrays=new Array<>();
        tempIntIntMaps=new Array<>();
        tempArrays=new Array();
        ifUse=new ObjectMap<>();
        this.game=game;
    }

    public void dispose(){
        tempIntArrays.clear();
        tempIntIntMaps.clear();
        tempArrays.clear();
        ifUse.clear();
    }
    //state -1不存在 0 size==0 1 size>0
    public boolean  ifUse(Object i){
       return ifUse.containsKey(i);
    }

    public void log(){
        Gdx.app.log("tempIntArrays",tempIntArrays.size+":"+tempIntArrays.toString());
        Gdx.app.log("tempIntIntMaps",tempIntIntMaps.size+":"+tempIntIntMaps.toString());
    }
    public void disposeTempIntArray(IntArray i){
        if(ifUse.containsKey(i)){
            ifUse.remove(i);
        }
        i.clear();
    }
    public void disposeTempArray(Array i){
        if(ifUse.containsKey(i)){
        ifUse.remove(i);
    }
        i.clear();
    }

    public void disposeTempIntIntMap(IntIntMap i){
        if(ifUse.containsKey(i)){
            ifUse.remove(i);
        }
        i.clear();
    }

    public IntArray getTempIntArray(){
        for(int c=0,cMax=tempIntArrays.size;c<cMax;c++){
            IntArray i=tempIntArrays.get(c);
            if(i==null){
                i=new IntArray();
            }
            if(i.size==0&&!ifUse.containsKey(i)){
                ifUse.put(i,true);
                return i;
            }
        }
        /*if(tempIntArrays.size>10){
            int s=0;
        }*/
        IntArray n=new IntArray();
        tempIntArrays.add(n);
        ifUse.put(n,true);
        return n;
    }


    public IntIntMap getTempIntIntMap(){
        /*if(game.sMapScreen!=null&&game.sMapScreen.smapGameStage!=null&&tempIntIntMaps.contains(game.sMapScreen.smapGameStage.selectedHexagons,false)){
            int s=0;
        }*/
        for(int c=0,cMax=tempIntIntMaps.size;c<cMax;c++){
            IntIntMap i=tempIntIntMaps.get(c);
            if(i==null){
                i=new IntIntMap();
            }
            if(i.size==0&&!ifUse.containsKey(i)){
                ifUse.put(i,true);
                return i;
            }
        }
        IntIntMap n=new IntIntMap();
        tempIntIntMaps.add(n);
        ifUse.put(n,true);
        return n;
    }

    public Array getTempArray(){
        for(int c=0,cMax=tempArrays.size;c<cMax;c++){
            Array i=tempArrays.get(c);
            if(i==null){
                i=new Array();
            }
            if(i.size==0&&!ifUse.containsKey(i)){
                ifUse.put(i,true);
                return i;
            }
        }
        Array n=new Array();
        tempArrays.add(n);
        ifUse.put(n,true);
        return n;
    }

    public void clearAllUse() {
        Iterator<ObjectMap.Entry<Object, Boolean>> itb = ifUse.iterator();
        while (itb.hasNext()) {
            ObjectMap.Entry<Object, Boolean> n=itb.next();
            if(n.key instanceof IntArray){
                IntArray i= (IntArray) n.key;
                if(i.size==0){
                    ifUse.remove(i);
                }
                i.clear();
            }else  if(n.key instanceof IntIntMap){
                IntIntMap i= (IntIntMap) n.key;
                if(i.size==0){
                    ifUse.remove(i);
                }
                i.clear();
            }else if(n.key instanceof Array){
                Array i= (Array) n.key;
                if(i.size==0){
                    ifUse.remove(i);
                }
                i.clear();
            }
        }
    }
    public void clearUse() {
        Iterator<ObjectMap.Entry<Object, Boolean>> itb = ifUse.iterator();
        while (itb.hasNext()) {
            ObjectMap.Entry<Object, Boolean> n=itb.next();
            if(n.key instanceof IntArray){
                IntArray i= (IntArray) n.key;
                if(i.size==0){
                    ifUse.remove(i);
                }
            }else  if(n.key instanceof IntIntMap){
                IntIntMap i= (IntIntMap) n.key;
                if(i.size==0){
                    ifUse.remove(i);
                }
            }else if(n.key instanceof Array){
                Array i= (Array) n.key;
                if(i.size==0){
                    ifUse.remove(i);
                }
            }
        }
    }
}
