package com.zhfy.game.model.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

public class ZHIntMap<v> {

    public IntIntMap keys;
    public Array<v> values;

    public int size(){
        return values.size;
    }

    public ZHIntMap(){
        keys=new IntIntMap();
        values=new Array();
    }

    public void clear(){
        keys=null;
        values=null;
    }
    public void put(int key,v value){
        if(keys.containsKey(key)){
          values.set(keys.get(key,-1),value);
        }else{
            keys.put(key,values.size);
            values.add(value);
        }
    }

    public v getByIndex(int index){

        if(index>=0&&index<values.size){
            return values.get(index);
        }
        return null;
    }
    public v getByKey(int key){
        if(keys.containsKey(key)){
            return values.get(keys.get(key,-1));
        }
        Gdx.app.error("key is null",key+"");
        return null;
    }


    public boolean containsKey(int key){
        if(keys.containsKey(key)){
            return true;
        }
        return false;
    }

    public void remove(int key) {
        if(keys.containsKey(key)){
           values.removeIndex(keys.get(key,-1));
            keys.remove(key,-1);
        }
    }

}
