package com.zhfy.game.model.framework;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
import com.zhfy.game.framework.tool.ImageDecrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TextureRegionListDAO {
	private Array<TextureRegionDAO> textureRegionList;
    private ObjectMap<String,Integer> regionMap;
    private ObjectMap<String,String> shieldMap;
    private MainGame game;

    public TextureRegionListDAO(MainGame game,ObjectMap<String,String> shieldMap){
        init();
        this.game=game;
        this.shieldMap=shieldMap;
    }


	private void init() {
	    textureRegionList=new Array<TextureRegionDAO>();
        regionMap=new ObjectMap<>();
	}

    public Array<TextureRegionDAO> getTextureRegionList() {
        return textureRegionList;
    }

    public void setTextureRegionList(Array<TextureRegionDAO> textureRegionList) {
	    int i=0;
        this.textureRegionList = textureRegionList;
        for(TextureRegionDAO region:textureRegionList){
            regionMap.put(region.getName(),i);
            i++;
        }
    }
    
    public TextureRegionDAO getTextureByName(String name){
        /*for(int i=0,iMax=textureRegionList.size();i<iMax;i++) {
            if(textureRegionList.get(i).getName().equals(Name)) {
                return textureRegionList.get(i);
            }
        }*/
        if(name==null){
            return null;
        }
        if(game.gameConfig.ifShield&&shieldMap!=null&&shieldMap.containsKey(name)){
            name=shieldMap.get(name);
        }
        if(regionMap.containsKey(name)){
            return textureRegionList.get(regionMap.get(name));
        }else{
            Gdx.app.error("getTextureByName1","textName:"+name);
        }
        return textureRegionList.get(regionMap.get("err_img"));
    }

    public boolean contains(String name){
        if(game.gameConfig.ifShield&&shieldMap!=null&&shieldMap.containsKey(name)){
            name=shieldMap.get(name);
        }
	    if(regionMap.containsKey(name)){
	        return true;
        }else {
	        return false;
        }
    }


   /* public TextureRegion getNewRegion(String Name,int w,int h){
        TextureRegion region=null;
        if(regionMap.containsKey(Name)){
            TextureRegionDAO temp=textureRegionList.get(regionMap.get(Name));
            //region= new TextureRegion(temp.getTextureRegion().getTexture(),temp.get,y,w,h);
            region= new TextureRegion(temp.getTextureRegion(),0,0,w,h);
            //region.setRegionWidth(w);
            //region.setRegionHeight(h);
        }else{
            Gdx.app.error("getTextureByName","textName:"+Name);
        }
        return region;
    }*/

        //当值为负数时,用原来的值
    public TextureRegion getNewRegion(String name,int x,int y,int w,int h){
        TextureRegion region=null;
        if(game.gameConfig.ifShield&&shieldMap!=null&&shieldMap.containsKey(name)){
            name=shieldMap.get(name);
        }
        if(regionMap.containsKey(name)){
            TextureRegionDAO temp=textureRegionList.get(regionMap.get(name));
            if(w<0){
                w=temp.getTextureRegion().getRegionWidth();
            }
            if(h<0){
                h=temp.getTextureRegion().getRegionHeight();
            }
            region= new TextureRegion(temp.getTextureRegion(),x,y,w,h);
        }else{
            Gdx.app.error("getTextureByName2","textName:"+name);
        }
        return region;
    }


    public void add(TextureRegionDAO region) {
        if(textureRegionList ==null) {
            init();
        }
        if(!regionMap.containsKey(region.getName())){
            textureRegionList.add(region);
            regionMap.put(region.getName(),textureRegionList.size-1);
        }
    }

    public int size() {
        return textureRegionList.size;
    }

    public TextureRegionDAO get(int i) {
        return textureRegionList.get(i);
    }

    
    /*// 数据检查
    public void check() {
        int j;
        //去重
        for (int i = 0; i < textureRegionList.size() ; i++) {
            for ( j = textureRegionList.size() - 1; j >i; j--) {
                if (textureRegionList.get(j).getName().equals(textureRegionList.get(i).getName())) {
                    Gdx.app.error("check",textureRegionList.get(j).getName());
                    textureRegionList.remove(j);
                }
            }
        }
    }*/


    public ObjectMap<String, Integer> getRegionMap() {
        return regionMap;
    }

    public void setRegionMap(ObjectMap<String, Integer> regionMap) {
        this.regionMap = regionMap;
    }

    public void clear(){
        textureRegionList.clear();
        regionMap.clear();
    }

    public TextureRegion getFlipRegion(String name){
        TextureRegion region=null;
        if(game.gameConfig.ifShield&&shieldMap!=null&&shieldMap.containsKey(name)){
            name=shieldMap.get(name);
        }
        String fName=name+"_filp";
        if(regionMap.containsKey(fName)){
           return textureRegionList.get(regionMap.get(fName)).getTextureRegion();
        }else if(regionMap.containsKey(name)){
            TextureRegionDAO temp=textureRegionList.get(regionMap.get(name)).cpy(fName);
            temp.getTextureRegion().flip(true,false);
            this.add(temp);
            return temp.getTextureRegion();
        }else{
            Gdx.app.error("getTextureByName2","textName:"+name);
        }
        return region;
    }


    public TextureRegionDAO getBlankRegionDAO(String name) {
        if(game.gameConfig.ifShield&&shieldMap!=null&&shieldMap.containsKey(name)){
            name=shieldMap.get(name);
        }
        String fName=name+"_blank";
        if(regionMap.containsKey(fName)){
            return textureRegionList.get(regionMap.get(fName));
        }else if(regionMap.containsKey(name)){
            TextureRegionDAO temp=textureRegionList.get(regionMap.get(name)).cpyBlank();
            this.add(temp);
            return temp;
        }else{
            Gdx.app.error("getTextureByName2","textName:"+name);
        }
        return null;
    }

    public void addRegionByUpdName(ObjectMap tilesMap) {
        for(String s:regionMap.keys()){
            if(tilesMap.containsKey(s)){
                regionMap.put((String) tilesMap.get(s),regionMap.get(s));
                //Gdx.app.log("addRegionByUpdName",s+":"+regionMap.get(s)+":"+tilesMap.get(s));
            }/*else{
                Gdx.app.error("no img to upd",s+":"+regionMap.get(s)+":"+tilesMap.get(s));
            }*/
        }
    }


    //210318
    public void addRegionDAO(AssetManager am,XmlReader.Element element) {
        String path=element.get("res").replace(".png", "");
        path=path.replace(".tata", "")+".xml";
        XmlReader reader = game.gameConfig.reader;
                XmlReader.Element root = reader.parse(
                Gdx.files.internal(path)
                );
        //String imgFile = root.getAttribute("imagePath");
        // 每个图片添加的时候都要加使用场景,多场景用;分割screenid="1"
        Texture t;
        Array<XmlReader.Element> images = root.getChildrenByNameRecursively("sprite");
        for (int a = 0, aMax = images.size; a < aMax; a++) {
           /* if(element.getBoolean("ifEncrypt",false)){
                    t=am.get(element.get("res"), Texture.class);
             //  ImageDecrypt.getTexture(t);
            }else{

            }*/
            t=am.get(element.get("res"), Texture.class);
            TextureRegion imgRegion = new TextureRegion(
                    //imgLists.getName(nameList.get(i)).getTexture(),
                    t,
            images.get(a).getInt("x"), images.get(a).getInt("y"), images.get(a).getInt("w"),
            images.get(a).getInt("h"));
            TextureRegionDAO imgRegionDAO = new TextureRegionDAO();
            imgRegionDAO.setTextureRegion(imgRegion);
            imgRegionDAO.setRefx(images.get(a).getInt("refx",imgRegion.getRegionWidth()/2));
            imgRegionDAO.setRefy(images.get(a).getInt("refy",imgRegion.getRegionHeight()/2));
            imgRegionDAO.setName(images.get(a).get("n").replace(".png", ""));
            /*if(imgRegionDAO.getName().equals("resource_bar0")){
                int s=0;
            }*/



            imgRegionDAO.setW(imgRegion.getRegionWidth());
            imgRegionDAO.setH(imgRegion.getRegionHeight());
            add(imgRegionDAO);

            //Gdx.app.log("addImgRegion",root.getAttribute("imagePath")+ ":"+images.get(a).get("n").replace(".png", "")+" x:"+images.get(a).getInt("refx")+" y:"+images.get(a).getInt("refy"));
        }

    }

    public TextureRegionDAO getFilpRegionDAO(String name) {
        TextureRegionDAO regionDAO=null;
        if(game.gameConfig.ifShield&&shieldMap!=null&&shieldMap.containsKey(name)){
            name=shieldMap.get(name);
        }
        String fName=name+"_filp";
        if(regionMap.containsKey(fName)){
            return textureRegionList.get(regionMap.get(fName));
        }else if(regionMap.containsKey(name)){
            regionDAO=textureRegionList.get(regionMap.get(name)).cpy(fName);
            regionDAO.getTextureRegion().flip(true,false);
            this.add(regionDAO);
            return regionDAO;
        }else{
            Gdx.app.error("getTextureByName2","textName:"+name);
        }
        return regionDAO;
    }

}
