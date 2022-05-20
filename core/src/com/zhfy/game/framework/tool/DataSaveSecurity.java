package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.zhfy.game.config.ResDefaultConfig;

//210318
public class DataSaveSecurity {
    private Save save;
    private FileHandle file;

    public DataSaveSecurity(String path) {
        file = Gdx.files.local(path);
        save = getSave();
    }

    private Save getSave() {
        Save save = new Save();

        if (file.exists()) {
            Json json = new Json();
            // 读取文件，并且解密
            save = json.fromJson(Save.class, Base64Coder.decodeString(file.readString()));
        }
        return save;
    }

    public void flush() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        file.writeString(Base64Coder.encodeString(json.prettyPrint(save)), false);
    }

    public void put(String key, Object object){
        save.data.put(key, object);
        flush(); // 立即保存数据
    }



    public int getInteger(String key,int defaultValue){
        if(save.data.containsKey(key))
            return (int) save.data.get(key);
        else
            save.data.put(key, defaultValue);
            return defaultValue;
    }

    public String get(String key,String defaultValue){
        if(save.data.containsKey(key))
            return (String) save.data.get(key);
        else
            save.data.put(key, defaultValue);
        return defaultValue;
    }

    public Boolean getBoolean(String key,Boolean defaultValue){
        if(save.data.containsKey(key))
            return (Boolean) save.data.get(key);
        else
            save.data.put(key, defaultValue);
        return defaultValue;
    }

    public void putInteger(String key, int i) {
        save.data.put(key, i);
        flush(); // 立即保存数据
    }

    public void putBoolean(String key, boolean i) {
        save.data.put(key, i);
        flush(); // 立即保存数据
    }

    public String getString(String s, String no_stage_name) {
        return get(s,no_stage_name);
    }

    public void putString(String s, String s1) {
        put(s,s1);
    }


    /** 根据需要T替换要读取的类型
     * public <T> T loadDataValue(String key, Class type){
     if(save.data.containsKey(key))return (T) save.data.get(key);
     else return null;   //this if() avoids exception, but check for null on load.

     }
     */
    private static class Save {
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }
}
