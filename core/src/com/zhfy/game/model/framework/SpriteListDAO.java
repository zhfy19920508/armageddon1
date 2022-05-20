package com.zhfy.game.model.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteListDAO {
    private Array<SpriteDAO> spriteList;
    //用来记录
    private ObjectIntMap spriteMap;

    
    //通过TextureListDAO 加载为sprite,然后通过map获取i
    public ObjectIntMap add(TextureListDAO textureList){
        if(textureList==null||textureList.getTextureList()==null) {
            Gdx.app.error("警告", "SpriteList在init时textureList资源为空");
            return null;
        }
        if(spriteList==null) {
            spriteList = new Array<>();
        }
        if(spriteMap==null){
            spriteMap=new ObjectIntMap<>();
        }


        SpriteDAO sprite;
        int i;
        for(i=0;i<textureList.getTextureList().size;i++) {
            sprite=new SpriteDAO();
            sprite.setSprite(new Sprite(textureList.getTextureList().get(i).getTexture()));
            sprite.setName(textureList.get(i).getName()) ;
            sprite.setRefx(textureList.get(i).getRefx());
            sprite.setRefy(textureList.get(i).getRefy());
            spriteList.add(sprite);
            spriteMap.put(textureList.get(i).getName().replace(".png", ""), i);
        }
        return spriteMap;
    }

    public ObjectIntMap add(TextureRegionDAO texture){
        if(texture==null) {
            Gdx.app.error("警告", "SpriteList在init时textureList资源为空");
            return null;
        }
        if(spriteList==null) {
            spriteList = new Array<>();
        }
        if(spriteMap==null){
            spriteMap=new ObjectIntMap();
        }
        SpriteDAO sprite;
        int i;
        sprite=new SpriteDAO();
        sprite.setSprite(new Sprite(texture.getTextureRegion()));
        sprite.setName(texture.getName()) ;
        sprite.setRefx(texture.getRefx());
        sprite.setRefy(texture.getRefy());
        spriteMap.put(texture.getName().replace(".png", ""), spriteList.size);
        spriteList.add(sprite);
        return spriteMap;
    }

    public ObjectIntMap add(TextureRegionDAO texture,String name){
        if(texture==null) {
            Gdx.app.error("警告", "SpriteList在init时textureList资源为空");
            return null;
        }
        if(spriteList==null) {
            spriteList = new Array<>();
        }
        if(spriteMap==null){
            spriteMap=new ObjectIntMap();
        }


        SpriteDAO sprite;
        int i;
        sprite=new SpriteDAO();
        sprite.setSprite(new Sprite(texture.getTextureRegion()));
        sprite.setName(name) ;
        sprite.setRefx(texture.getRefx());
        sprite.setRefy(texture.getRefy());
        spriteMap.put(name, spriteList.size);
        spriteList.add(sprite);
        return spriteMap;
    }


    public Array<SpriteDAO> getSpriteList() {
        return spriteList;
    }


    public void setSpriteList(Array<SpriteDAO> spriteList) {
        this.spriteList = spriteList;
    }


    public SpriteDAO getSpriteByName(String name) {
        //忽略png后缀
        name=name.replace(".png", "");

        
        /*try {
             tool= new FSearchTool(spriteList, "name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (SpriteDAO) tool.searchTask((name));*/
        for(int i=0;i<spriteList.size;i++){
            if(spriteList.get(i).getName().equals(name)){
                return spriteList.get(i);
            }
        }
        return null;
    }
    
    public void add(SpriteDAO pixmap) {
        if (spriteList == null) {
            spriteList = new Array<>();
        }
        spriteList.add(pixmap);
    }
    
    public void remove(String fileName) {
       /*try {
            tool= new FSearchTool(spriteList, "name");
       } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
      int[] index1 =  tool.getSearchIndex(fileName);
      for (int i = 0,iMax=index1.length; i < iMax; i++) {
          //Gdx.app.log("清除图片:"+fileName, "清除:"+spriteList.get(index1[i]).getName());
          spriteList.remove(index1[i]);*/
        for(int i=spriteList.size-1;i>=0;i--){
            if(spriteList.get(i).getName().equals(fileName)){
                spriteList.removeIndex(i);
            }
        }
      //Gdx.app.log("清除图片完成", "清除"+index1.length+"张图片");
    }
    
    public int size() {
        return spriteList.size;
    }
    
    public SpriteDAO get(int i) {
        return spriteList.get(i);
    }
    
    
    
    // 数据检查
    public void check() {
        //去重
        int j;
        for (int i = 0; i < spriteList.size; i++) {
            for ( j = spriteList.size -1; j > i; j--) {
                if (spriteList.get(j).getName().equals(spriteList.get(i).getName())) {
                    spriteList.removeIndex(j);
                }
            }
        }
        /*for (int i = 0; i < spriteList.size() - 1; i++) {
            for ( j = spriteList.size() - 1; j > i; j--) {
                if (spriteList.get(j).getName().equals(spriteList.get(i).getName())) {
                    spriteList.remove(j);
                }
            }
        }*/
    }
    
    
}
