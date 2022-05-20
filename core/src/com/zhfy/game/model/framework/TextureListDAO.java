package com.zhfy.game.model.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class TextureListDAO {
    private Array<TextureDAO> textureList;
    private ObjectMap<String,Integer> textureMap;//textureName:i,i为list中的位置序列

    public TextureListDAO(){
        init();
    }
    public void init() {
        textureList = new Array<>();
        textureMap=new ObjectMap<>();
    }
    
    public Array<TextureDAO> getTextureList() {
        return textureList;
    }
    
    public void setTextureList(Array<TextureDAO> textureList) {
        this.textureList = textureList;
        int i=0;
        for(TextureDAO texture:textureList){
            textureMap.put(texture.getName(),i);
            i++;
        }
    }
    
    public void add(TextureDAO texture) {
        if (textureList == null) {
            init();
        }
        textureList.add(texture);
        textureMap.put(texture.getName(),textureList.size-1);
    }
    
    // 根据传入图片姓名 清理使用的图片
    /*public void clearTexture(List<StringName> nameList) {
        int j ,jMax;
        for (int i = 0,iMax=nameList.size(); i < iMax; i++) {
            for ( j = 0,jMax=textureList.size; j < jMax; j++) {
                if (nameList.get(j).equals(textureList.get(i).getName())) {
                    textureList.get(i).getTexture().dispose();
                    textureList.remove(i);
                }
            }
        }
    }*/
    
    public void clearAll() {
        for (int j = 0; j < textureList.size; j++) {
            textureList.get(j).getTexture().dispose();
        }
        textureList = null;
    }
    
    public TextureDAO get(int i) {
        return textureList.get(i);
    }
    
    public TextureDAO getName(String name) {
        /*for (int i = 0; i < textureList.size; i++) {
            if (textureList.get(i).getName().equals(name)) {
                return textureList.get(i);
            }
        }*/

        return textureList.get(textureMap.get(name));
    }
    
    // 数据检查
    public void check() {
        // 去重
        int j;
        for (int i = 0; i < textureList.size ; i++) {
            for ( j = textureList.size - 1; j > i; j--) {
                if (textureList.get(j).getName().equals(textureList.get(i).getName())) {
                    textureList.removeIndex(j);
                }
            }
        }
    }
    
    //通过文件夹名加载pixmap文件夹下的图片资源
    //加载内存图片PixmapListDAO pixmapLists 目标pm_tiles 则传入pm_tiles
    public void addPixmapByFileName(XmlReader reader,String fileName,AssetManager am) {
        if(textureList==null) {
            textureList=new Array<>();
        }
      //1读取路径下说明文件
       // XmlReader reader = ResConfig.reader;
        StringBuilder path=new StringBuilder("pixmap/");
        path.append(fileName.substring(3)).append("/").append(fileName).append(".xml");
        /*Element root = reader
                .parse(Gdx.files.internal("pixmap/"+fileName.substring(3)+"/" + fileName + ".xml"));*/
        Element root = reader
                .parse(Gdx.files.internal(path.toString()));
        // 每个图片添加的时候都要加使用场景,多场景用;分割screenid="1"
        Array<Element> images = root.getChildrenByNameRecursively("sprite");
    for (int i=0;i<images.size;i++) {
        TextureDAO textureDAO = new TextureDAO();
        //textureDAO.setTexture( new Texture(Gdx.files.internal("pixmap/"+fileName.substring(3)+"/" +image.get("n"))));;

        if(am.contains("pixmap/"+fileName.substring(3)+"/" +images.get(i).get("n"),Texture.class)){
            textureDAO.setTexture( am.get(("pixmap/"+fileName.substring(3)+"/" +images.get(i).get("n")),Texture.class) );
        }else{
            textureDAO.setTexture(new Texture(Gdx.files.internal("pixmap/"+fileName.substring(3)+"/" +images.get(i).get("n"))) );
        }
        textureDAO.setRefx(images.get(i).getInt("refx"));
        textureDAO.setRefy(images.get(i).getInt("refy"));
        textureDAO.setName(images.get(i).get("n").replace(".png", ""));
        textureList.add(textureDAO);
    }
    //check();
    }



    public ObjectMap<String, Integer> getTextureMap() {
        return textureMap;
    }

    public void setTextureMap(ObjectMap<String, Integer> textureMap) {
        this.textureMap = textureMap;
    }
}
