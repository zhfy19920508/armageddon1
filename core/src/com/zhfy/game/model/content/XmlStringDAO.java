package com.zhfy.game.model.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.XmlReader;

import java.util.HashMap;
import java.util.Map;

//用来存储id为String的情况
public class XmlStringDAO {
    private ObjectIntMap map;//id:index
    public XmlReader.Element e;

    public XmlStringDAO(XmlReader.Element e){
        map=new ObjectIntMap();
        int childNum = e.getChildCount();
        /*for (int i = 0; i < childNum; i++) {
           map.put(e.getChild(i).get("id"),i);
        }*/
        for (int i = childNum-1; i>=0; i--) {
            if(!e.getChild(i).getBoolean("visible",true)){
                e.removeChild(i);
                continue;
            }
            map.put(e.getChild(i).get("id"),i);
        }
        this.e=e;
    }

    public boolean contain(String id){
        if(map.containsKey(id)){
            return true;
        }else{
            return false;
        }
    }

    public XmlReader.Element getElementById(String id){
        if(map.containsKey(id)){
            return e.getChild(map.get(id,0));
        }else{
            Gdx.app.error("XmlStringDAO Error",id);
            return null;
        }
    }

    public Array<XmlReader.Element> getElementsById(String id,String childName){
        if(map.containsKey(id)){
            return e.getChild(map.get(id,0)).getChildrenByName(childName);
        }else{
            Gdx.app.error("XmlStringDAO Error",id);
            return null;
        }
    }

}
