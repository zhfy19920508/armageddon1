package com.zhfy.game.model.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.XmlReader;

import java.util.HashMap;
import java.util.Map;

public class XmlIntDAO {
    private IntIntMap map;//id:index
    public XmlReader.Element e;

    public XmlIntDAO(XmlReader.Element e){
        map=new IntIntMap();
        int childNum = e.getChildCount();
        for (int i = childNum-1; i>=0; i--) {
            if(!e.getChild(i).getBoolean("visible",true)){
                e.removeChild(i);
                continue;
            }
           map.put(e.getChild(i).getInt("id"),i);
        }
        this.e=e;
    }

    public XmlReader.Element getElementById(int id){
        if(map.containsKey(id)){
            return e.getChild(map.get(id,0));
        }else{
            Gdx.app.error("XmlIntDAO Error","id:"+id);
            return null;
        }
    }

    public int getInt(int id,String str){
        if(map.containsKey(id)){
            return e.getChild(map.get(id,0)).getInt(str,-1);
        }else{
            Gdx.app.error("XmlIntDAO getInt Error","id:"+id);
            return -1;
        }
    }
    public boolean getBoolean(int id,String str){
        if(map.containsKey(id)){
            return e.getChild(map.get(id,0)).getBoolean(str,false);
        }else{
            Gdx.app.error("XmlIntDAO getBoolean Error","id:"+id);
            return false;
        }
    }

    public String getString(int id,String str){
        if(map.containsKey(id)){
            return e.getChild(map.get(id,0)).get(str,"");
        }else{
            Gdx.app.error("XmlIntDAO getString Error","id:"+id);
            return "";
        }
    }


    public boolean contain(int key) {
        return map.containsKey(key);
    }
}
