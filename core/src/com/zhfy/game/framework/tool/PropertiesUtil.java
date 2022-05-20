package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

public class PropertiesUtil {

    private Properties save;
    private FileHandle file;


    public PropertiesUtil(String path,boolean ifGdx) {
        if(ifGdx){
            file = Gdx.files.local(path);
        }else{
            file = new FileHandle(new File(path));
        }

        save=new Properties();
        if(file.exists()){
            try {
                save.load(file.read());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void flush() {
       /* Iterator<String> iterator = save.stringPropertyNames().iterator();

        while (iterator.hasNext()){
            String key = iterator.next();

        }*/
        file.writeString(ComUtil.transStrForProperty(save),false);
     /* FileOutputStream oFile=null;
        try {
            oFile = new FileOutputStream(file.file());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(oFile!=null){
            try {
                save.store(oFile,null);
                oFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

  /**/  public void put(String key, Object object){
        save.put(key, object);
       // flush(); // 立即保存数据
    }



    public int getInteger(String key,int defaultValue){
        if(save.containsKey(key))
            return Integer.parseInt(save.get(key).toString());
        else
            save.put(key, String.valueOf(defaultValue));
        return defaultValue;
    }

    public String get(String key,String defaultValue){
        if(save.containsKey(key))
            return (String) save.get(key);
        else
            save.put(key, defaultValue);
        return defaultValue;
    }

    public Boolean getBoolean(String key,Boolean defaultValue){
        if(save.containsKey(key)){
            String str=save.get(key).toString();
            return str.equals("true")||str=="true";
        }else{
            save.put(key, String.valueOf(defaultValue));
            return defaultValue;
        }
    }

    public void putInteger(String key, int i) {
        save.put(key,String.valueOf( i));
        //flush(); // 立即保存数据
    }

    public void putBoolean(String key, boolean i) {
        save.put(key, String.valueOf(i));
      //  flush(); // 立即保存数据
    }

    public String getString(String s, String no_stage_name) {
        return get(s,no_stage_name);
    }

    public void putString(String s, String s1) {
        put(s,s1);
    }




}
