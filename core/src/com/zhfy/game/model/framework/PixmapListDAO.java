package com.zhfy.game.model.framework;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class PixmapListDAO {
    private Array<PixmapDAO> pixmapList;

    //FSearchTool tool;
    
    private void init() {
        pixmapList = new Array<>();
    }
    
    
    
    public Array<PixmapDAO> getPixmapList() {
        return pixmapList;
    }



    public void setPixmapList(Array<PixmapDAO> pixmapList) {
        this.pixmapList = pixmapList;
    }



    public PixmapDAO getPixmapByName(String name) {
        if(name==null){
            return null;
        }
        //忽略png后缀
        name=name.replace(".png", "");
        
        /*for (int i = 0; i < pixmapList.size(); i++) {
            if (pixmapList.get(i).getName().equals(name)) {
                return pixmapList.get(i);
            }
        }*/
        
       /* try {
             tool= new FSearchTool(pixmapList, "name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (PixmapDAO) tool.searchTask((name));*/
     for(int i=0;i<pixmapList.size;i++){
         if(pixmapList.get(i).getName().equals(name)){
             return pixmapList.get(i);
         }
     }
       return null;
    }
    
    public void add(PixmapDAO pixmap) {
        if (pixmapList == null) {
            init();
        }
        pixmapList.add(pixmap);
    }
    
    public void remove(String fileName) {
      /* try {
            tool= new FSearchTool(pixmapList, "name");
       } catch (Exception e) {
           e.printStackTrace();
       }
      int[] index1 =  tool.getSearchIndex(fileName);
      for (int i = 0,iMax=index1.length; i < iMax; i++) {
          //Gdx.app.log("清除图片:"+fileName, "清除:"+pixmapList.get(index1[i]).getName());
          pixmapList.remove(index1[i]);
      }*/
      //Gdx.app.log("清除图片完成", "清除"+index1.length+"张图片");
        for(int i=pixmapList.size-1;i>=0;i--){
            if(pixmapList.get(i).getName().equals(fileName)){
                pixmapList.removeIndex(i);
            }
        }
    }
    
    public int size() {
        return pixmapList.size;
    }
    
    public PixmapDAO get(int i) {
        return pixmapList.get(i);
    }
    
    
    
    // 数据检查
    public void check() {
        //去重
        int j;
        for (int i = 0,iMax=pixmapList.size ; i < iMax; i++) {
            for ( j = pixmapList.size -1; j > i; j--) {
                if (pixmapList.get(j).getName().equals(pixmapList.get(i).getName())) {
                    pixmapList.removeIndex(j);
                }
            }
        }
    }

    public void dispose() {
        if(pixmapList!=null){
            for(int i = 0,iMax=pixmapList.size ; i < iMax; i++){
                pixmapList.get(i).dispose();
            }
            pixmapList=null;
        }
    }
}
