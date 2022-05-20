
package com.zhfy.game.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.framework.CamerDAO;
import com.zhfy.game.model.framework.Coord;
import com.zhfy.game.model.framework.DrawGridListDAO;
import com.zhfy.game.model.framework.PixmapDAO;
import com.zhfy.game.model.framework.PixmapListDAO;
import com.zhfy.game.model.framework.SpriteDAO;

public class GameMap {
    // 六边形地图绘制 全部static方法

    private GameMap() {
        throw new IllegalStateException("GameMap class");
    }

    public static void main(String[] args) throws Exception {

        // saveBinByWirteByte(fb,"123");
        // MapBinDAO fb=apBin("123");
        // saveMapBin(fb,"123");
    }





    // 循环遍历 在地块图上 是海洋和海岸的位置打窟窿,海岸使用id为2的抹除相应地形
    public static Pixmap getLandByDAO(Fb2Map mapBinDAO, Pixmap pixmap, PixmapListDAO pixmapLists, int mapx, int mapy, int mapw, int maph) {

        int x = 0;
        int y = 0;
        //int x_px_circle = 0;
        //int y_px_circle = 0;
        int x_px = 0;
        int y_px = 0;
        PixmapDAO shadowPixmap;
        Pixmap shadowTargetPixmap = new Pixmap(46, 40, Pixmap.Format.RGBA8888);
        ;
        String shadowName;

        HashMap<String, String> terrainimgMap = new HashMap<String, String>();

        //六边形四个角的坐标
        int hexgaon_x1 = 0;//左上,左下
        int hexgaon_x2 = 0;//右上,右下
        int hexgaon_x3 = 0;//左中
        int hexgaon_x4 = 0;//右中
        int hexgaon_y1 = 0;//上
        int hexgaon_y2 = 0;//中
        int hexgaon_y3 = 0;//下

        //TODO 随后增加对地图属性的解析

        int beginIndex = 0;
        int EndIndex = mapw * maph;

        int xC;
        int yC;


        Blending blend = pixmap.getBlending();

        pixmap.setBlending(Blending.None);

        for (int id = beginIndex; id < EndIndex; id++) {
            x = (id % mapBinDAO.getMapWidth()) + 1;
            y = (id / mapBinDAO.getMapWidth()) + 1;
            //x_px_circle = (int) (((x - 1) * ResConfig.Map.GRID_WIDTH) * GameConfig.Map.MAP_SCALE + GameConfig.Map.HEXAGON_WIDTH * GameConfig.Map.MAP_SCALE / 2);
            //y_px_circle = (int) ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * GameConfig.Map.MAP_SCALE + GameConfig.Map.HEXAGON_HEIGHT * GameConfig.Map.MAP_SCALE / 2);

            x_px = getX_px(x,1f);
            y_px = getY_px(x,y,1f);


            if (mapBinDAO.getMapbin().get(id).getBackTile() == 1) {

                // 这里填充圆，或四个三角形组合的六边形
                // pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((GameConfig.Map.HEXAGON_WIDTH+12) * GameConfig.Map.MAP_SCALE / 2));
                //六个点 
               // hexgaon_x1 = (int) (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE);//左上,左下 原来是36
               // hexgaon_x2 = (int) (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE);//右上,右下 ResConfig.Map.GRID_WIDTH
               // hexgaon_x3 = (int) (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1);//左中
               // hexgaon_x4 = (int) (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE);//右中
               // hexgaon_y1 = (int) ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1);//上
               // hexgaon_y2 = (int) ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2);//中
               // hexgaon_y3 = (int) ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE);//下
                 hexgaon_x1 = getHexagonX1(x,y,1f);//左上,左下
                 hexgaon_x2 =  getHexagonX2(x,y,1f);//右上,右下
                 hexgaon_x3 =  getHexagonX3(x,y,1f);//左中
                 hexgaon_x4 =  getHexagonX4(x,y,1f);//右中
                 hexgaon_y1 = getHexagonY1(x,y,1f);//上
                 hexgaon_y2 =  getHexagonY2(x,y,1f);//中
                 hexgaon_y3 =  getHexagonY3(x,y,1f);//下

                pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);


                // Gdx.app.log("切除陆地", "id:" + id +" x:"+x+" y:"+ y+"
                // x_px:"+x_px+" y_px:"+ y_px);
            } else if (mapBinDAO.getMapbin().get(id).getBackTile()==2) {
                //pixmap.setBlending(blend);
                shadowName = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx());
                if (shadowName != null) {
                    shadowPixmap = pixmapLists.getPixmapByName(shadowName);
                    /*
                    pixmap.drawPixmap(shadowPixmap.getPixmap(), 0, 0, (int)(shadowPixmap.getPixmap().getWidth()), (int)(shadowPixmap.getPixmap().getHeight()), x_px,
                            y_px, (int) (shadowPixmap.getPixmap().getWidth() * GameConfig.Map.MAP_SCALE), (int) (shadowPixmap.getPixmap().getHeight() * GameConfig.Map.MAP_SCALE));
                     */

                    //Gdx.app.log("切除海岸边缘","shadowName:"+shadowName);
                   /*
                    TODO
                                                       * 暂时未完成              
                                                       *根据阴影的形状来切割掉像素,实现边界对齐
                    * */
                    //pixmap.setBlending(Blending.None);
                    shadowTargetPixmap.drawPixmap(shadowPixmap.getPixmap(), 0, 0, (int) (shadowPixmap.getPixmap().getWidth()), (int) (shadowPixmap.getPixmap().getHeight()), 0,
                            0, (int) (shadowPixmap.getPixmap().getWidth() * ResDefaultConfig.Map.MAP_SCALE), (int) (shadowPixmap.getPixmap().getHeight() * ResDefaultConfig.Map.MAP_SCALE));

                    for (xC = 0; xC < (int) (shadowTargetPixmap.getWidth()); xC++) {
                        for (yC = 0; yC < (int) (shadowTargetPixmap.getHeight()); yC++) {
                            //int color = pixmap.getPixel(xC, yC);
                             /*if(shadowPixmap.getPixmap().getPixel(xC, yC)>256&&shadowPixmap.getPixmap().getPixel(xC, yC)<66000){
                                 pixmap.drawPixel(xC+x_px, yC+y_px, 0);
                             }*/
                            /*if(shadowPixmap.getName().equals("coast_58")) {
                                //Gdx.app.log("切除海岸边缘","color:"+shadowTargetPixmap.getPixel(xC, yC)+" x:"+xC+" y:"+yC);
                                Gdx.app.log("",""+shadowTargetPixmap.getPixel(xC, yC));
                            }*/


                            if (shadowTargetPixmap.getPixel(xC, yC) == 65535 || shadowTargetPixmap.getPixel(xC, yC) == 65022) {
                                pixmap.drawPixel(xC + x_px, yC + y_px, 0);


                                Gdx.app.log("切除海岸边缘", "color:" + shadowPixmap.getPixmap().getPixel(xC, yC) + " x:" + xC + " y:" + yC);

                            }/*else {
                                Gdx.app.log("切除海岸边缘","color:"+pixmap.getPixel(xC, yC)+" x:"+xC+" y:"+yC);
                            }*/


                        }
                    }


                }

            }
        }
        // 把blending设置回来
        pixmap.setBlending(blend);
        return pixmap;
    }




    // 通过mapBinDAO绘制地图
    // pixmap要绘制的目标图片
    // pixmapLists使用图片
    // defTerrainimgs图片说明文件
    public static Pixmap getPixmapByDao(MainGame game, Fb2Map mapBinDAO, Pixmap pixmap, PixmapListDAO pixmapLists, int xH, int yH, int wH, int hH, float scale) {
        // 解析dao
        if (mapBinDAO.getMapbin() != null) {

            int x = 0;
            int y = 0;
            int x_px = 0;
            int y_px = 0;
            ObjectMap<String,String> terrainimgMap = game.gameConfig.getTERRAINIMG_MAP(false);

            String imgBack;
            String imgFore;
            PixmapDAO backPixmap;
            PixmapDAO forePixmap;
            int refSize = 0;//对水面大小补位,否则可能有空缺
            //Pixmap.Filter filter;
            // 单线程绘制图片
            for (int id = 0,iMax=mapBinDAO.getMapbin().size; id < iMax; id++) {

                x = (id % mapBinDAO.getMapWidth()) + 1;
                y = (id / mapBinDAO.getMapWidth()) + 1;
                if(x<xH||x>xH+wH||y<yH||y>yH+hH){
                    continue;
                }
              //x_px = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));;
               // y_px =(int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));

                x_px=getX_px(x,scale);
                y_px=getY_px(x,y,scale);
                //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
                if (mapBinDAO.getMapbin().get(id).getBackTile() != 0 /*&& mapBinDAO.getMapbin().get(id).getBackTile() != 1*/) {
                    if (mapBinDAO.getMapbin().get(id).getBackTile() == 1 && scale < 0.5f) {
                        refSize = 1;
                    } else {
                        refSize = 0;
                    }
                    // 获取该位置要画的地形 z
                    imgBack = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx());
                    if (imgBack == null) {
                        //Gdx.app.log("地图层1未找到", mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx());
                        imgBack = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getBackTile() + "_" + ComUtil.getRandom(1, 4));
                    }
                    // 获取图像并绘制到上面
                    backPixmap = pixmapLists.getPixmapByName(imgBack);
                    if (backPixmap != null) {
                        pixmap.drawPixmap
                                (backPixmap.getPixmap(),
                                        0,
                                        0,
                                        backPixmap.getPixmap().getWidth(),
                                        backPixmap.getPixmap().getHeight(),
                                        (int) (scale * ((  mapBinDAO.getMapbin().get(id).getBackRefX() + backPixmap.getRefx()) * ResDefaultConfig.Map.MAP_SCALE)) +x_px,
                                        (int) (scale * ((  mapBinDAO.getMapbin().get(id).getBackRefY() + backPixmap.getRefy()) * ResDefaultConfig.Map.MAP_SCALE)) +y_px,
                                        (int) (scale * backPixmap.getPixmap().getWidth() * ResDefaultConfig.Map.MAP_SCALE) ,
                                        (int) (scale * backPixmap.getPixmap().getHeight() * ResDefaultConfig.Map.MAP_SCALE)+1) ;


                        //Gdx.app.log("地图层1构建成功", " backid:" + mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx() + "img:" + imgBack);
                    } /*
                     * else { Gdx.app.log("地图层1构建失败", " backid:" +
                     * mapBinDAO.getMapbin().get(id).getBackTile() + "_" +
                     * mapBinDAO.getMapbin().get(id).getBackIdx()); }
                     */
                } /*
                 * else { Gdx.app.log("地图层1忽略构建", ""); }
                 */
                // 忽略底图和海洋
                if (mapBinDAO.getMapbin().get(id).getForeTile() != 0 && mapBinDAO.getMapbin().get(id).getForeTile() != 1) {
                    imgFore = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getForeTile() + "_" + mapBinDAO.getMapbin().get(id).getForeIdx());
                    if (imgFore == null) {
                        Gdx.app.log("地图层2未找到", mapBinDAO.getMapbin().get(id).getForeTile() + "_" + mapBinDAO.getMapbin().get(id).getForeIdx());
                        imgFore = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getForeTile() + "_1");
                    }
                    forePixmap = pixmapLists.getPixmapByName(imgFore);
                    if (forePixmap != null) {
                        pixmap.drawPixmap
                                (forePixmap.getPixmap(),
                                        0,
                                        0,
                                        forePixmap.getPixmap().getWidth(),
                                        forePixmap.getPixmap().getHeight(),
                                        (int) (scale * ((  mapBinDAO.getMapbin().get(id).getForeRefX() + forePixmap.getRefx()) * ResDefaultConfig.Map.MAP_SCALE))+x_px,
                                        (int) (scale * ((  mapBinDAO.getMapbin().get(id).getForeRefY() + forePixmap.getRefy()) * ResDefaultConfig.Map.MAP_SCALE))+y_px,
                                        (int) (scale * forePixmap.getPixmap().getWidth() * ResDefaultConfig.Map.MAP_SCALE),
                                        (int) (scale * forePixmap.getPixmap().getHeight() * ResDefaultConfig.Map.MAP_SCALE)+1);
                    } /*
                     * else { Gdx.app.log("地图层2构建成功", " backid:" +
                     * mapBinDAO.getMapbin().get(id).getForeTile() + "_" +
                     * mapBinDAO.getMapbin().get(id).getForeIdx()); }
                     */
                } /*
                 * else { Gdx.app.log("地图层2", "忽略构建"); }
                 */
            }
            Gdx.app.log("地图构建", "完成");
            //  PixmapIO.writePNG(Gdx.files.external("texture_world.png"), pixmap);

            return pixmap;
        } else {
            Gdx.app.log("地图构建", "失败");
            return null;
        }
    }


    public static Pixmap getPixmapByDaoForSea(MainGame game, Fb2Map mapBinDAO, Pixmap pixmap, PixmapListDAO pixmapLists, int xH, int yH, int wH, int hH, float scale) {
        // 解析dao
        if (mapBinDAO.getMapbin() != null) {

            int x = 0;
            int y = 0;
            int x_px = 0;
            int y_px = 0;
            ObjectMap<String,String> terrainimgMap = game.gameConfig.getTERRAINIMG_MAP(false);

            String imgBack;
            String imgFore;
            PixmapDAO backPixmap;
            PixmapDAO forePixmap;
            int refSize = 0;//对水面大小补位,否则可能有空缺
            //Pixmap.Filter filter;
            // 单线程绘制图片
            for (int id = 0,iMax=mapBinDAO.getMapbin().size; id < iMax; id++) {

                x=GameMap.getHX(id,mapBinDAO.getMapWidth())+1;
                y=GameMap.getHY(id,mapBinDAO.getMapWidth())+1;
                if(x<xH||x>xH+wH||y<yH||y>yH+hH){
                    continue;
                }
                //x_px = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));;
                // y_px =(int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));

                x_px= (int) GameMap.getX_pxByHexagon(x,1,0);
                y_px= (int) GameMap.getY_pxByHexagon(x,y,pixmap.getHeight(),1,0,false);

                //float  mapH_px=GameMap.getH_px(masterData.getWidth(), masterData.getHeight(), 1);
                //因为地图的边缘相接部分并不是取最右,而是右数第二个坐标相接来实现地图循环

                //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
                Fb2Map.MapHexagon hexagon=mapBinDAO.getMapbin().get(id);
                if (hexagon!=null&&hexagon.isSea()) {
                    if (hexagon.getBackTile() == 1 && scale < 0.5f) {
                        refSize = 1;
                    } else {
                        refSize = 0;
                    }
                    // 获取该位置要画的地形 z
                    imgBack = terrainimgMap.get(hexagon.getBackTile() + "_" + hexagon.getBackIdx());
                    if (imgBack == null) {
                        //Gdx.app.log("地图层1未找到", hexagon.getBackTile() + "_" + hexagon.getBackIdx());
                        imgBack = terrainimgMap.get(hexagon.getBackTile() + "_" + ComUtil.getRandom(1, 4));
                    }
                    // 获取图像并绘制到上面
                    backPixmap = pixmapLists.getPixmapByName(imgBack);
                   /* if(id==55130||id==369){
                        float x1=(int) (scale * ((  hexagon.getBackRefX() + backPixmap.getRefx()) * ResDefaultConfig.Map.MAP_SCALE)) +x_px;
                        float y1=(int) (scale * ((  hexagon.getBackRefY() + backPixmap.getRefy()) * ResDefaultConfig.Map.MAP_SCALE)) +y_px;
                        int s=1;
                    }*/
                    /*if (backPixmap != null) {
                        pixmap.drawPixmap
                                (backPixmap.getPixmap(),
                                        0,
                                        0,
                                        backPixmap.getPixmap().getWidth(),
                                        backPixmap.getPixmap().getHeight(),
                                        (int) (scale * ((  hexagon.getBackRefX() + backPixmap.getRefx()) * ResDefaultConfig.Map.MAP_SCALE)) +x_px,
                                        (int) (scale * ((  hexagon.getBackRefY() + backPixmap.getRefy()) * ResDefaultConfig.Map.MAP_SCALE)) +y_px,
                                        (int) (scale * backPixmap.getPixmap().getWidth() * ResDefaultConfig.Map.MAP_SCALE) ,
                                        (int) (scale * backPixmap.getPixmap().getHeight() * ResDefaultConfig.Map.MAP_SCALE)+1) ;
                         }*/
                    if (backPixmap != null) {
                        pixmap.drawPixmap
                                (backPixmap.getPixmap(),
                                        0,
                                        0,
                                        backPixmap.getPixmap().getWidth(),
                                        backPixmap.getPixmap().getHeight(),
                                        (int)(x_px+hexagon.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-hexagon.getTile1().getRefx()* ResDefaultConfig.Map.MAP_SCALE),
                                        (int)(y_px+hexagon.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-hexagon.getTile1().getRefy()* ResDefaultConfig.Map.MAP_SCALE),
                                        (int) (scale * backPixmap.getPixmap().getWidth() * ResDefaultConfig.Map.MAP_SCALE) ,
                                        (int) (scale * backPixmap.getPixmap().getHeight() * ResDefaultConfig.Map.MAP_SCALE)+1) ;
                    }
                }
            }
            Gdx.app.log("地图构建", "完成");
            //  PixmapIO.writePNG(Gdx.files.external("texture_world.png"), pixmap);

            return pixmap;
        } else {
            Gdx.app.log("地图构建", "失败");
            return null;
        }
    }



    //通过区域划分绘制颜色区块图
    public static Pixmap getPixmapByDaoForAreaColor(Fb2Map mapBinDAO, Pixmap pixmap, float scale, IntArray coastGrid) {

        // 解析dao
        if (mapBinDAO.getMapbin() != null) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            int hexgaon_x1;
            int hexgaon_x2;
            int hexgaon_x3;
            int hexgaon_x4;
            int hexgaon_y1;
            int hexgaon_y2;
            int hexgaon_y3;

            //沿海地块用圆形来填充
            //List<Integer> coastGrid=mapBinDAO.getIdsByAround(12);

            int color;
            int nextRegionId = 0;
            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);
            for (int i1=0;i1<coastGrid.size;i1++) {
                int id = coastGrid.get(i1);
                if (mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getRegionId() != -1) {
                    x = (id % mapBinDAO.getMapWidth()) + 1;
                    y = (id / mapBinDAO.getMapWidth()) + 1;
                   // x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
                   // y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));
                    x_px_circle=getX_px_Circle(x,scale);
                    y_px_circle=getY_px_Circle(x,y,scale);



                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充
                    if (mapBinDAO.getMapbin().get(id).getRegionId() != nextRegionId) {
                        color = GameUtil.getColorByNum(mapBinDAO.getMapbin().get(id).getRegionId());
                        //获取颜色
                        pixmap.setColor(color);
                        nextRegionId = mapBinDAO.getMapbin().get(id).getRegionId();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));
                }
            }

            // 单线程绘制所有图片
            for (int id = 0,idMax=mapBinDAO.getMapbin().size; id < idMax; id++) {

                x = (id % mapBinDAO.getMapWidth()) + 1;
                y = (id / mapBinDAO.getMapWidth()) + 1;

              //  hexgaon_x1 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE));//左上,左下 原来是36
              //  hexgaon_x2 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
             //   hexgaon_x3 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));//左中
             //   hexgaon_x4 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE));//右中
             //   hexgaon_y1 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));//上
             //   hexgaon_y2 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));//中
             //   hexgaon_y3 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE));//下
                hexgaon_x1 = getHexagonX1(x,y,scale);//左上,左下
                hexgaon_x2 =  getHexagonX2(x,y,scale);//右上,右下
                hexgaon_x3 =  getHexagonX3(x,y,scale);//左中
                hexgaon_x4 =  getHexagonX4(x,y,scale);//右中
                hexgaon_y1 = getHexagonY1(x,y,scale);//上
                hexgaon_y2 =  getHexagonY2(x,y,scale);//中
                hexgaon_y3 =  getHexagonY3(x,y,scale);//下
                if (mapBinDAO.getMapbin().get(id).getRegionId() != nextRegionId) {
                    color = GameUtil.getColorByNum(mapBinDAO.getMapbin().get(id).getRegionId());
                    //获取颜色
                    pixmap.setColor(color);
                    nextRegionId = mapBinDAO.getMapbin().get(id).getRegionId();
                    //Gdx.app.log("setColor","id:"+id+" color:"+color);
                }

                //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
                if (mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getRegionId() != -1) {

                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                }

            }

            //Gdx.app.log("色图构建", "完成");
            //  PixmapIO.writePNG(Gdx.files.external("texture_world.png"), pixmap);

            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败5");
            return null;
        }
    }

    //通过history的国家划分区块图
    public static Pixmap drawPixmapForLegionColor(Fb2Map mapBinDAO, Pixmap pixmap, float scale, List<Integer> coastGrid, List<Color> legionColors, Map regionColors) {

        // 解析dao
        if (mapBinDAO.getMapbin() != null) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            int hexgaon_x1;
            int hexgaon_x2;
            int hexgaon_x3;
            int hexgaon_x4;
            int hexgaon_y1;
            int hexgaon_y2;
            int hexgaon_y3;

            //沿海地块用圆形来填充
            //List<Integer> coastGrid=mapBinDAO.getIdsByAround(12);


            //颜色
            //List<Color> legionColors= DefDAO.getAllCountryColorByXml();
            //Map regionColors=StageDAOEdit.getRegionColor(stage);

            Color color;
            int nextRegionId = 0;
            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);
            for (int id : coastGrid) {
                if (id<mapBinDAO.getMapbin().size&&mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getRegionId() != -1) {
                    x = (id % mapBinDAO.getMapWidth()) + 1;
                    y = (id / mapBinDAO.getMapWidth()) + 1;
                   // x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
                   // y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));
                    x_px_circle=getX_px_Circle(x,scale);
                    y_px_circle=getY_px_Circle(x,y,scale);

                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充
                    if (mapBinDAO.getMapbin().get(id).getRegionId() != nextRegionId) {

                        //color = GameUtil.getColorByNum(mapBinDAO.getMapbin().get(id).getPolitical());
                        //获取颜色
                        //pixmap.setColor(color);
                        if (mapBinDAO.getMapbin().get(id).getRegionId() != -1) {
                            // Gdx.app.log("getColor","regionId:"+mapBinDAO.getMapbin().get(id).getPolitical()+" colors:"+regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                            color = legionColors.get((Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getRegionId()));
                        } else {
                            color = Color.WHITE;
                        }
                        pixmap.setColor(color);


                        nextRegionId = mapBinDAO.getMapbin().get(id).getRegionId();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));
                }
            }

            // 单线程绘制所有图片
            for (int id = 0,idMax=mapBinDAO.getMapbin().size; id < idMax; id++) {

                x = (id % mapBinDAO.getMapWidth()) + 1;
                y = (id / mapBinDAO.getMapWidth()) + 1;

               // hexgaon_x1 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE));//左上,左下 原来是36
                // hexgaon_x2 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
                //hexgaon_x3 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));//左中
                // hexgaon_x4 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE));//右中
                //hexgaon_y1 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));//上
                //hexgaon_y2 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));//中
                //hexgaon_y3 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE));//下

                hexgaon_x1 = getHexagonX1(x,y,scale);//左上,左下
                hexgaon_x2 =  getHexagonX2(x,y,scale);//右上,右下
                hexgaon_x3 =  getHexagonX3(x,y,scale);//左中
                hexgaon_x4 =  getHexagonX4(x,y,scale);//右中
                hexgaon_y1 = getHexagonY1(x,y,scale);//上
                hexgaon_y2 =  getHexagonY2(x,y,scale);//中
                hexgaon_y3 =  getHexagonY3(x,y,scale);//下

                if (mapBinDAO.getMapbin().get(id).getRegionId() != nextRegionId) {
                    if (mapBinDAO.getMapbin().get(id).getRegionId() != -1&&regionColors.get(mapBinDAO.getMapbin().get(id).getRegionId())!=null) {

                       //Gdx.app.log("colorDebug"," id:"+id+" regionId:"+mapBinDAO.getMapbin().get(id).getPolitical()+" legion:"+regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));

                        color = legionColors.get((Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getRegionId()));
                    } else {
                        color = Color.WHITE;
                    }

                    //获取颜色
                    pixmap.setColor(color);
                    nextRegionId = mapBinDAO.getMapbin().get(id).getRegionId();
                    //Gdx.app.log("setColor","id:"+id+" color:"+color);
                }

                //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
                if (mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getRegionId() != -1) {

                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                }

            }

            //Gdx.app.log("色图构建", "完成");
            //  PixmapIO.writePNG(Gdx.files.external("texture_world.png"), pixmap);

            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败6");
            return null;
        }
    }


    //通过history的国家划分区块图  mapWidth:mapBinDAO.getMapWidth()
    public static Pixmap drawPixmapForLegionColor(int mapWidth, List<Integer> regionIds, Pixmap pixmap, float scale, List<Integer> blockTypeIds, List<Integer> coastGrid, List<Color> legionColors, Map regionColors) {

        // 解析dao
        int x = 0;
        int y = 0;
        int x_px_circle = 0;
        int y_px_circle = 0;
        int hexgaon_x1;
        int hexgaon_x2;
        int hexgaon_x3;
        int hexgaon_x4;
        int hexgaon_y1;
        int hexgaon_y2;
        int hexgaon_y3;

        //沿海地块用圆形来填充
        //List<Integer> coastGrid=mapBinDAO.getIdsByAround(12);


        //颜色
        //List<Color> legionColors= DefDAO.getAllCountryColorByXml();
        //Map regionColors=StageDAOEdit.getRegionColor(stage);

        Color color;
        int nextRegionId = 0;
        //先绘制海岸  //沿海地块用圆形来填充
        pixmap.setBlending(Blending.None);
        for (int id : coastGrid) {
            if (id<regionIds.size()&&blockTypeIds.get(id)!=1&&regionIds.get(id) != -1 ) {
                x = (id % mapWidth) + 1;
                y = (id / mapWidth) + 1;
             //   x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
             //   y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));
                x_px_circle=getX_px_Circle(x,scale);
                y_px_circle=getY_px_Circle(x,y,scale);

                //Gdx.app.log("海陆绘制圆", "" + id);
                //沿海地块用圆形来填充
                if (regionIds.get(id) != nextRegionId) {

                    //color = GameUtil.getColorByNum(mapBinDAO.getMapbin().get(id).getPolitical());
                    //获取颜色
                    //pixmap.setColor(color);
                    if (regionIds.get(id) != -1) {
                        // Gdx.app.log("getColor","regionId:"+mapBinDAO.getMapbin().get(id).getPolitical()+" colors:"+regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                        color = legionColors.get((Integer) regionColors.get(regionIds.get(id)));
                    } else {
                        color = Color.WHITE;
                    }
                    pixmap.setColor(color);


                    nextRegionId = regionIds.get(id);
                    //Gdx.app.log("setColor","id:"+id+" color:"+color);
                }
                pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));
            }
        }

        // 单线程绘制所有图片
        for (int id = 0,idMax=regionIds.size(); id < idMax; id++) {

            x = (id % mapWidth) + 1;
            y = (id / mapWidth) + 1;

           // hexgaon_x1 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE));//左上,左下 原来是36
           // hexgaon_x2 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
           // hexgaon_x3 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));//左中
           // hexgaon_x4 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE));//右中
           // hexgaon_y1 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));//上
           // hexgaon_y2 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));//中
           // hexgaon_y3 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE));//下

            hexgaon_x1 = getHexagonX1(x,y,scale);//左上,左下
            hexgaon_x2 =  getHexagonX2(x,y,scale);//右上,右下
            hexgaon_x3 =  getHexagonX3(x,y,scale);//左中
            hexgaon_x4 =  getHexagonX4(x,y,scale);//右中
            hexgaon_y1 = getHexagonY1(x,y,scale);//上
            hexgaon_y2 =  getHexagonY2(x,y,scale);//中
            hexgaon_y3 =  getHexagonY3(x,y,scale);//下

            if (regionIds.get(id) != nextRegionId) {
                if (regionIds.get(id) != -1&&regionColors.get(regionIds.get(id))!=null) {
                    color = legionColors.get((Integer) regionColors.get(regionIds.get(id)));
                } else {
                    color = Color.WHITE;
                }

                //获取颜色
                pixmap.setColor(color);
                nextRegionId = regionIds.get(id);
                //Gdx.app.log("setColor","id:"+id+" color:"+color);
            }

            //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
            if (blockTypeIds.get(id)!=1&&regionIds.get(id) != -1) {
                pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
            }
        }

        //Gdx.app.log("色图构建", "完成");
        //  PixmapIO.writePNG(Gdx.files.external("texture_world.png"), pixmap);

        return pixmap;

    }




    //通过history的国家划分区块图
    /*public static Pixmap drawPixmapForLegionColor(MapBinDAO mapBinDAO, Pixmap pixmap, float scale, List<Integer> coastGrid, List<Color> legionColors, HexagonDAO hexagons, GameDAO gameBtl) {

        // 解析dao
        if (mapBinDAO.getMapbin() != null) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            int hexgaon_x1;
            int hexgaon_x2;
            int hexgaon_x3;
            int hexgaon_x4;
            int hexgaon_y1;
            int hexgaon_y2;
            int hexgaon_y3;

            //沿海地块用圆形来填充
            //List<Integer> coastGrid=mapBinDAO.getIdsByAround(12);


            //颜色
            //List<Color> legionColors= DefDAO.getAllCountryColorByXml();
            //Map regionColors=StageDAOEdit.getRegionColor(stage);

            Color color;
            int nextRegionId = 0;
            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);
            for (int id : coastGrid) {
                if (id<mapBinDAO.getMapbin().size()&&mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getPolitical() != -1) {
                    x = (id % mapBinDAO.getMapWidth()) + 1;
                    y = (id / mapBinDAO.getMapWidth()) + 1;
                    x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
                    y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));


                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充
                    if (mapBinDAO.getMapbin().get(id).getPolitical() != nextRegionId) {

                        //color = GameUtil.getColorByNum(mapBinDAO.getMapbin().get(id).getPolitical());
                        //获取颜色
                        //pixmap.setColor(color);
                        if (mapBinDAO.getMapbin().get(id).getPolitical() != -1) {
                            //Gdx.app.log("getColor1","regionId:"+mapBinDAO.getMapbin().get(id).getPolitical()+" colors:"+hexagons.getLegionIndexByHexagon(mapBinDAO.getMapbin().get(id).getPolitical()));
                            color = legionColors.get(gameBtl.getBtl().getBm2().get(hexagons.getBuildPolicy(mapBinDAO.getMapbin().get(id).getPolitical())).getBm2_4());
                        } else {
                            //Gdx.app.log("getColor2","regionId:"+mapBinDAO.getMapbin().get(id).getPolitical()+" colors:"+hexagons.getLegionIndexByHexagon(mapBinDAO.getMapbin().get(id).getPolitical()));
                            color = Color.WHITE;
                        }
                        pixmap.setColor(color);


                        nextRegionId = mapBinDAO.getMapbin().get(id).getPolitical();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResConfig.Map.HEXAGON_WIDTH + 13) * ResConfig.Map.MAP_SCALE / 2 * scale));
                }
            }

            // 单线程绘制所有图片
            for (int id = 0,idMax=mapBinDAO.getMapbin().size(); id < idMax; id++) {

                x = (id % mapBinDAO.getMapWidth()) + 1;
                y = (id / mapBinDAO.getMapWidth()) + 1;

                hexgaon_x1 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE));//左上,左下 原来是36
                hexgaon_x2 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
                hexgaon_x3 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));//左中
                hexgaon_x4 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE));//右中
                hexgaon_y1 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));//上
                hexgaon_y2 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));//中
                hexgaon_y3 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE));//下

                if (mapBinDAO.getMapbin().get(id).getPolitical() != nextRegionId) {
                    if (mapBinDAO.getMapbin().get(id).getPolitical() != -1&&hexagons.getBuildPolicy(mapBinDAO.getMapbin().get(id).getPolitical())!=-1) {
                        color = legionColors.get(gameBtl.getBtl().getBm2().get(hexagons.getBuildPolicy(mapBinDAO.getMapbin().get(id).getPolitical())).getBm2_4());
                    } else {
                        color = Color.WHITE;
                    }

                    //获取颜色
                    pixmap.setColor(color);
                    nextRegionId = mapBinDAO.getMapbin().get(id).getPolitical();
                    //Gdx.app.log("setColor","id:"+id+" color:"+color);
                }

                //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
                if (mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getPolitical() != -1) {

                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                }

            }

            //Gdx.app.log("色图构建", "完成");
            //  PixmapIO.writePNG(Gdx.files.external("texture_world.png"), pixmap);

            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败");
            return null;
        }
    }

*/

    //修改region的颜色
    public static Pixmap updColorByRegion(Fb2Map mapBinDAO, Pixmap pixmap, float scale, IntArray regionIds, List<Color> legionColors, Map regionColors) {
        IntArray coastGrid=mapBinDAO.getCoastGrid();
        IntArray ids = mapBinDAO.getIdsByRegionIds(regionIds);
        ;
        if (mapBinDAO.getMapbin() != null && ids.size > 0) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            int hexgaon_x1;
            int hexgaon_x2;
            int hexgaon_x3;
            int hexgaon_x4;
            int hexgaon_y1;
            int hexgaon_y2;
            int hexgaon_y3;
            int nextRegionId = 0;
            Color color;

            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);
            for (int i1=0;i1<ids.size;i1++) {
                int id = ids.get(i1);
                if (mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getRegionId() != -1 && coastGrid.contains(id)) {
                    x = (id % mapBinDAO.getMapWidth()) + 1;
                    y = (id / mapBinDAO.getMapWidth()) + 1;

                   // x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
                   // y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));

                    x_px_circle=getX_px_Circle(x,scale);
                    y_px_circle=getY_px_Circle(x,y,scale);

                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充

                    if (mapBinDAO.getMapbin().get(id).getRegionId() != nextRegionId) {
                        if (mapBinDAO.getMapbin().get(id).getRegionId() != -1) {
                            color = legionColors.get((Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getRegionId()));
                            // Gdx.app.log("updColor","id:"+id+" color:"+color.toString());
                            //color = legionColors.get((Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                            //color=Color.RED;
                        } else {
                            color = Color.WHITE;
                        }
                        //获取颜色
                        pixmap.setColor(color);
                        nextRegionId = mapBinDAO.getMapbin().get(id).getRegionId();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));
                }
            }
            nextRegionId=0;

            // 单线程绘制所有图片
            for (int i1=0;i1<ids.size;i1++) {
                int id = ids.get(i1);
                //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                x = (id % mapBinDAO.getMapWidth()) + 1;
                y = (id / mapBinDAO.getMapWidth()) + 1;
                //hexgaon_x1 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE));//左上,左下 原来是36
                //hexgaon_x2 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
                //hexgaon_x3 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));//左中
                //hexgaon_x4 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE));//右中
                //hexgaon_y1 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));//上
                //hexgaon_y2 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));//中
                //hexgaon_y3 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE));//下
                hexgaon_x1 = getHexagonX1(x,y,scale);//左上,左下
                hexgaon_x2 =  getHexagonX2(x,y,scale);//右上,右下
                hexgaon_x3 =  getHexagonX3(x,y,scale);//左中
                hexgaon_x4 =  getHexagonX4(x,y,scale);//右中
                hexgaon_y1 = getHexagonY1(x,y,scale);//上
                hexgaon_y2 =  getHexagonY2(x,y,scale);//中
                hexgaon_y3 =  getHexagonY3(x,y,scale);//下
                if (mapBinDAO.getMapbin().get(id).getRegionId() != nextRegionId) {
                    if (mapBinDAO.getMapbin().get(id).getRegionId() != -1) {
                        color = legionColors.get((Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getRegionId()));
                        // Gdx.app.log("updColor","id:"+id+" color:"+color.toString());
                        //color = legionColors.get((Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                        //color=Color.RED;
                    } else {
                        color = Color.WHITE;
                    }
                    //获取颜色
                    pixmap.setColor(color);
                    nextRegionId = mapBinDAO.getMapbin().get(id).getRegionId();
                    //Gdx.app.log("setColor","id:"+id+" color:"+color);
                }
                //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
                if (mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getRegionId() != -1) {

                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                }
            }
            // Gdx.app.log("色图构建", "完成  color:"+color);
            //PixmapIO.writePNG(Gdx.files.external("updateColor_"+ids.toString()+".png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败:"+ ids.size);
            return pixmap;
        }
    }

    //修改region的颜色
    public static Pixmap drawPixmapForLegionColor(Fb2Smap smapDao, Pixmap pixmap, float scale,Boolean ifDrawSea,Boolean ifDrawWarLine) {
        //,  List<Color> legionColors, Map regionColors;
        //IntArray coastGrids=smapDao.coastGrids;
        //IntArray ids = smapDao.getIdsByRegionIds(regionIds);
       // IntArray excludeRegions=smapDao.excludeRegions;
        if (smapDao.hexagonDatas != null &&smapDao.legionColors!=null&&smapDao.masterData.getIfColor()==1 && smapDao.hexagonDatas.size > 0) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            int hexgaon_x1;
            int hexgaon_x2;
            int hexgaon_x3;
            int hexgaon_x4;
            int hexgaon_y1;
            int hexgaon_y2;
            int hexgaon_y3;
            int nextRegionId = 0;
            Color color = Color.CLEAR;

            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);
            for (int i = 0, iMax = smapDao.hexagonDatas.size; i<iMax; i++) {
                if ( smapDao.hexagonDatas.get(i).getRegionId() != -1 ) {
                    if(!ifDrawSea&& smapDao.hexagonDatas.get(i).getBlockType() == 1){
                        continue;
                    }

                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充
                    if (smapDao.hexagonDatas.get(i).getRegionId() != nextRegionId) {

                        if (smapDao.hexagonDatas.get(i).getRegionId() != -1) {

                            // Gdx.app.log("updColor","id:"+id+" color:"+color.toString());
                           /*if(smapDao.masterData.getIfFog()==1&& smapDao.hexagonDatas.get(i).getIfFog() ==1){
                                color=smapDao.getColorByHexagon(smapDao.hexagonDatas.get(i).getRegionId());
                            }else{//绘制深色地块
                                color=smapDao.getFogColorByHexagon(smapDao.hexagonDatas.get(i).getRegionId());
                            }*/
                            color=smapDao.getColorForRegion(smapDao.hexagonDatas.get(i).getRegionId());
                        } else {
                            color = Color.CLEAR;
                        }
                        //获取颜色
                        pixmap.setColor(color);
                        nextRegionId =smapDao.hexagonDatas.get(i).getRegionId();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    x = (i % smapDao.masterData.getWidth()) + 1;
                    y = (i / smapDao.masterData.getWidth()) + 1;
                    hexgaon_x1 = getHexagonX1(x,y,scale);//左上,左下
                    hexgaon_x2 =  getHexagonX2(x,y,scale);//右上,右下
                    hexgaon_x3 =  getHexagonX3(x,y,scale);//左中
                    hexgaon_x4 =  getHexagonX4(x,y,scale);//右中
                    hexgaon_y1 = getHexagonY1(x,y,scale);//上
                    hexgaon_y2 =  getHexagonY2(x,y,scale);//中
                    hexgaon_y3 =  getHexagonY3(x,y,scale);//下
                    // x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
                    // y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));
                    // hexgaon_x1 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE));//左上,左下 原来是36
                    // hexgaon_x2 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
                    // hexgaon_x3 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));//左中
                    //hexgaon_x4 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE));//右中
                    //hexgaon_y1 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));//上
                    //hexgaon_y2 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));//中
                    //hexgaon_y3 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE));//下



                    x_px_circle=getX_px_Circle(x,scale);
                    y_px_circle=getY_px_Circle(x,y,scale);
                    if(smapDao.hexagonDatas.get(i).getIfCoast()>0){
                        pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));

                    }else{
                       pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                        pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                        pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                        pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                    }

                    if(ifDrawWarLine&&smapDao.buildIfDrawWarLine(smapDao.hexagonDatas.get(i).getRegionId())){
                        pixmap.setColor(Color.RED);
                      /*  pixmap.drawLine(hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2  );
                        pixmap.drawLine(hexgaon_x1,hexgaon_y1,hexgaon_x2,hexgaon_y3  );
                        pixmap.drawLine(hexgaon_x3,hexgaon_y2,hexgaon_x1,hexgaon_y3  );
*/
                        drawDottedLine(pixmap,1,hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2 );
                        drawDottedLine(pixmap,1,hexgaon_x1,hexgaon_y1,hexgaon_x2,hexgaon_y3 );
                        drawDottedLine(pixmap,1,hexgaon_x3,hexgaon_y2,hexgaon_x1,hexgaon_y3);

                        pixmap.setColor(color);
                    }

                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                 }
            }



            // Gdx.app.log("色图构建", "完成  color:"+color);
            //PixmapIO.writePNG(Gdx.files.external("updateColor_1.png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败:");
            return pixmap;
        }
    }


    //修改region的颜色
    public static Pixmap drawPixmapForHexagon(Fb2Smap smapDao, Pixmap pixmap, float scale,int hexagon,Color color) {
        //,  List<Color> legionColors, Map regionColors;
        //IntArray coastGrids=smapDao.coastGrids;
        //IntArray ids = smapDao.getIdsByRegionIds(regionIds);
        // IntArray excludeRegions=smapDao.excludeRegions;
        if (smapDao.hexagonDatas != null  && smapDao.hexagonDatas.size > hexagon) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);


                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充

                    pixmap.setColor(color);

                    x = (hexagon % smapDao.masterData.getWidth()) + 1;
                    y = (hexagon / smapDao.masterData.getWidth()) + 1;


                   //     x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
                    //    y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));
            x_px_circle=getX_px_Circle(x,scale);
            y_px_circle=getY_px_Circle(x,y,scale);
                        pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH ) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));

                    Gdx.app.log("drawPixmapForHexagon","id:"+hexagon+ " xc:"+x_px_circle+" xy:"+y_px_circle);

            // Gdx.app.log("色图构建", "完成  color:"+color);
            //PixmapIO.writePNG(Gdx.files.external("updateColor_"+ids.toString()+".png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败:");
            return pixmap;
        }
    }

    //修改region的颜色
    public static Pixmap drawPixmapForRegion(Fb2Smap smapDao, Pixmap pixmap, float scale) {
        //,  List<Color> legionColors, Map regionColors;
        //IntArray coastGrids=smapDao.coastGrids;
        //IntArray ids = smapDao.getIdsByRegionIds(regionIds);
        // IntArray excludeRegions=smapDao.excludeRegions;
        if (smapDao.hexagonDatas != null  && smapDao.hexagonDatas.size > 0) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            Color color;

            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);
            for (int i = 0, iMax = smapDao.hexagonDatas.size; i<iMax; i++) {
                if (smapDao.hexagonDatas.get(i).getRegionId()==smapDao.hexagonDatas.get(i).getHexagonIndex()) {


                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充

                    pixmap.setColor(Color.RED);

                    x = (i % smapDao.masterData.getWidth()) + 1;
                    y = (i / smapDao.masterData.getWidth()) + 1;


                  //  x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
                    //y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));

                    x_px_circle=getX_px_Circle(x,scale);
                    y_px_circle=getY_px_Circle(x,y,scale);
                    pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH ) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));


                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                }
            }



            // Gdx.app.log("色图构建", "完成  color:"+color);
            //PixmapIO.writePNG(Gdx.files.external("updateColor_"+ids.toString()+".png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败:");
            return pixmap;
        }
    }



    //修改单个region的颜色
    public static Pixmap updateColorByOneRegion(Fb2Map mapBinDAO, Pixmap pixmap, float scale, int regionId, Color updColor) {
       IntArray coastGrid=mapBinDAO.getCoastGrid();
        IntArray ids = mapBinDAO.getIdsByRegionId(regionId);
        if (mapBinDAO.getMapbin() != null && ids.size > 0) {
            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            int hexgaon_x1;
            int hexgaon_x2;
            int hexgaon_x3;
            int hexgaon_x4;
            int hexgaon_y1;
            int hexgaon_y2;
            int hexgaon_y3;
            int nextRegionId = 0;
            Color color;
            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);
            for (int i1=0;i1<ids.size;i1++) {
                int id = ids.get(i1);
                if (mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getRegionId() != -1 && coastGrid.contains(id)) {
                    x = (id % mapBinDAO.getMapWidth()) + 1;
                    y = (id / mapBinDAO.getMapWidth()) + 1;
                 //   x_px_circle = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE / 2));
                 //   y_px_circle = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));
                    x_px_circle=getX_px_Circle(x,scale);
                    y_px_circle=getY_px_Circle(x,y,scale);
                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充

                    if (mapBinDAO.getMapbin().get(id).getRegionId() != nextRegionId) {
                        if (mapBinDAO.getMapbin().get(id).getRegionId() != -1) {
                            color = updColor;
                            // Gdx.app.log("updColor","id:"+id+" color:"+color.toString());
                            //color = legionColors.get((Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                            //color=Color.RED;
                        } else {
                            color = Color.WHITE;
                        }
                        //获取颜色
                        pixmap.setColor(color);
                        nextRegionId = mapBinDAO.getMapbin().get(id).getRegionId();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));
                }
            }
            nextRegionId=0;

            // 单线程绘制所有图片
            for (int i1=0;i1<ids.size;i1++) {
                int id = ids.get(i1);
                //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                x = (id % mapBinDAO.getMapWidth()) + 1;
                y = (id / mapBinDAO.getMapWidth()) + 1;
               // hexgaon_x1 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE));//左上,左下 原来是36
               // hexgaon_x2 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
               // hexgaon_x3 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));//左中
               // hexgaon_x4 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE));//右中
               // hexgaon_y1 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));//上
               // hexgaon_y2 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));//中
               // hexgaon_y3 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE));//下
                hexgaon_x1 = getHexagonX1(x,y,scale);//左上,左下
                hexgaon_x2 =  getHexagonX2(x,y,scale);//右上,右下
                hexgaon_x3 =  getHexagonX3(x,y,scale);//左中
                hexgaon_x4 =  getHexagonX4(x,y,scale);//右中
                hexgaon_y1 = getHexagonY1(x,y,scale);//上
                hexgaon_y2 =  getHexagonY2(x,y,scale);//中
                hexgaon_y3 =  getHexagonY3(x,y,scale);//下
                if (mapBinDAO.getMapbin().get(id).getRegionId() != nextRegionId) {
                    if (mapBinDAO.getMapbin().get(id).getRegionId() != -1) {
                        color = updColor;
                        // Gdx.app.log("updColor","id:"+id+" color:"+color.toString());
                        //color = legionColors.get((Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                        //color=Color.RED;
                    } else {
                        color = Color.WHITE;
                    }
                    //获取颜色
                    pixmap.setColor(color);
                    nextRegionId = mapBinDAO.getMapbin().get(id).getRegionId();
                    //Gdx.app.log("setColor","id:"+id+" color:"+color);
                }
                //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
                if (mapBinDAO.getMapbin().get(id).getBlockType() != 1 && mapBinDAO.getMapbin().get(id).getRegionId() != -1) {

                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                    pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                }
            }
            // Gdx.app.log("色图构建", "完成  color:"+color);
            //PixmapIO.writePNG(Gdx.files.external("updateColor_"+ids.toString()+".png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败");
            return pixmap;
        }
    }

    // 绘制分区图片 512*512
    // pixmap要绘制的目标图片
    // pixmapLists使用图片
    // defTerrainimgs图片说明文件
    //beginX,beginY绘制的起始坐标
    public static Pixmap getPixmapForArea(MainGame game, Fb2Map mapBinDAO, Pixmap pixmap, PixmapListDAO pixmapLists, int beginX, int beginY) {

        int areaW = ResDefaultConfig.Map.PT_GRID_WIDTH;
        int areaH = ResDefaultConfig.Map.PT_GRID_HEIGHT + 1;


        beginX = beginX + 1;
        beginY = beginY + 1;

        int mapRefX = 0;
        int mapRefXPx = 0;

        //如果beginX不是1,则要从这个开始的再开始绘制
        if (beginX > 1) {
            mapRefX = -1;
            mapRefXPx = (int) ((beginX - 1) * ResDefaultConfig.Map.PT_SIDE % (ResDefaultConfig.Map.GRID_WIDTH * ResDefaultConfig.Map.MAP_SCALE) / ResDefaultConfig.Map.MAP_SCALE);
        }/**/


        int areaSum = areaW * areaH;
        int mapW = mapBinDAO.mapWidth;

        //区域偏移量


        // 解析dao
        if (mapBinDAO.getMapbin() != null) {

            int x = 0;
            int y = 0;
            int x_px = 0;
            int y_px = 0;
            ObjectMap<String,String> terrainimgMap =game.gameConfig.getTERRAINIMG_MAP(false);
            // 将要取得的图片的id和名字放入位置

            String imgBack;
            String imgFore;
            PixmapDAO backPixmap;
            PixmapDAO forePixmap;
            int id;


            // 单线程绘制图片
            for (int i = 0; i < areaSum; i++) {

                x = (i % areaW) + 1;
                y = (i / areaW) + 1;

                //判断是否跨行
                if (((beginX - 1) * areaW + x - 1) < mapW) {
                    id = ((beginX - 1) * areaW + x - 1 + mapRefX) + ((beginY - 1) * (areaH) + y - 1) * mapW;//计算当前绘制的id
                } else {
                    id = ((beginX - 1) * areaW + x - 1 + mapRefX) + ((beginY - 1) * (areaH) + y - 1) * mapW - mapW;
                }

                x_px = (int) ((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) - mapRefXPx;
                y_px = (int) (((id & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT));
                
                /*if(beginX==2&&beginY==1) {
                    Gdx.app.log("测试:", "id:"+id+" x:"+x+" y:"+y+" x_px:"+x_px+" y_px:"+y_px);
                }*/


                if (id >= mapBinDAO.getMapbin().size) {
                    break;
                }
                if (mapBinDAO.getMapbin().get(id).getBackTile() != 0 /*&& mapBinDAO.getMapbin().get(id).getBackTile() != 1*/) {

                    //Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y);
                    // 获取该位置要画的地形 z
                    imgBack = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx());
                    if (imgBack == null) {
                        //Gdx.app.log("地图层1未找到", mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx());
                        imgBack = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getBackTile() + "_" + ComUtil.getRandom(1, 4));
                    }
                    // 获取图像并绘制到上面
                    backPixmap = pixmapLists.getPixmapByName(imgBack);
                    if (backPixmap != null) {

                        pixmap.drawPixmap(backPixmap.getPixmap(), 0, 0, backPixmap.getPixmap().getWidth(), backPixmap.getPixmap().getHeight(), (int) ((x_px + mapBinDAO.getMapbin().get(id).getBackRefX()) * ResDefaultConfig.Map.MAP_SCALE),
                                (int) ((y_px + mapBinDAO.getMapbin().get(id).getBackRefY()) * ResDefaultConfig.Map.MAP_SCALE), (int) (backPixmap.getPixmap().getWidth() * ResDefaultConfig.Map.MAP_SCALE), (int) (backPixmap.getPixmap().getHeight() * ResDefaultConfig.Map.MAP_SCALE));
                    }
                }
                // 忽略底图和海洋
                if (mapBinDAO.getMapbin().get(id).getForeTile() != 0 && mapBinDAO.getMapbin().get(id).getForeTile() != 1) {
                    imgFore = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getForeTile() + "_" + mapBinDAO.getMapbin().get(id).getForeIdx());
                    if (imgFore == null) {
                        Gdx.app.log("地图层2未找到", mapBinDAO.getMapbin().get(id).getForeTile() + "_" + mapBinDAO.getMapbin().get(id).getForeIdx());
                        imgFore = terrainimgMap.get(mapBinDAO.getMapbin().get(id).getForeTile() + "_1");
                    }
                    forePixmap = pixmapLists.getPixmapByName(imgFore);
                    if (forePixmap != null) {
                        pixmap.drawPixmap(forePixmap.getPixmap(), x_px + mapBinDAO.getMapbin().get(id).getForeRefX(), y_px + mapBinDAO.getMapbin().get(id).getForeRefY(), 0, 0, forePixmap.getPixmap().getWidth(), forePixmap.getPixmap().getHeight());
                    }
                }
            }
            /*if(beginX==2&&beginY==1) {
                PixmapIO.writePNG(Gdx.files.external("texture_"+beginY+"."+beginX+".png"), pixmap); 
            }*/
            //PixmapIO.writePNG(Gdx.files.external("texture_"+beginY+"."+beginX+".png"), pixmap);

            Gdx.app.log("地图构建", "完成");
            return pixmap;
        } else {
            Gdx.app.log("地图构建", "失败");
            return null;
        }
    }




    public static int getWidth(int w) {
        return ((148 + ResDefaultConfig.Map.GRID_WIDTH * (w - 1)));
    }

    public static int getHeight(int h) {
        return  ((192 + ResDefaultConfig.Map.HEXAGON_HEIGHT * (h - 1)));
    }

    // 六边形网格定位
    // @param xPos 输入，所需查询的点的x坐标
    // @param yPos 输入，所需查询的点的y坐标
    // @param cell_x 输出，改点所在网格的x坐标
    // @param cell_y 输出，改点所在网格的y坐标
    public static Coord getHotCell(Coord coord,float xPos, float yPos, float zoom,float GridZoom) {

        float GRID_WIDTH = ResDefaultConfig.Map.GRID_WIDTH*GridZoom;// (CELL_BORDER*1.5f)
        float GRID_HEIGHT = ResDefaultConfig.Map.HEXAGON_HEIGHT*GridZoom;// (CELL_BORDER*0.8660254f)
        float Grid_BORDER = GRID_WIDTH / 1.5f;

        int cell_y;
        int cell_x;
        xPos = xPos / ResDefaultConfig.Map.MAP_SCALE;
        yPos = yPos / ResDefaultConfig.Map.MAP_SCALE;
        cell_x = (int) (xPos / GRID_WIDTH);
        float x = xPos - cell_x * GRID_WIDTH;

        cell_y = (int) (yPos / GRID_HEIGHT);
        float y = yPos - cell_y * GRID_HEIGHT;


        //if(! (Grid_BORDER-Math.abs((x-1/2*GRID_WIDTH))>Math.abs(y-1/2*GRID_HEIGHT)/Math.sqrt(3))) {
        if (!(Math.abs(GRID_WIDTH / 2 - x) + Math.abs(GRID_HEIGHT / 2 - y) / Math.sqrt(3) <= Grid_BORDER * Math.sqrt(3))) {
            //不在六边形内部
            if (x > GRID_WIDTH / 2) {
                //在右边
                cell_x++;
            }
        }
        //Coord coord = new Coord(cell_x, cell_y);
        if(coord==null){
            coord = new Coord(cell_x, cell_y);
        }else {
            coord = coord.initCoord(cell_x, cell_y);
        }
        return coord;

    }

    // 六边形网格定位
    // @param xPos 输入，所需查询的点的x坐标
    // @param yPos 输入，所需查询的点的y坐标
    // @param cell_x 输出，改点所在网格的x坐标
    // @param cell_y 输出，改点所在网格的y坐标
    // @param refX 偏移横坐标
    // @param refY 偏移纵坐标
    public static Coord getHotCell(Coord coord,float xPos, float yPos, float zoom,float GridZoom,int mapW,int refX,int refY) {

        float GRID_WIDTH = ResDefaultConfig.Map.GRID_WIDTH*GridZoom;// (CELL_BORDER*1.5f)
        float GRID_HEIGHT = ResDefaultConfig.Map.HEXAGON_HEIGHT*GridZoom;// (CELL_BORDER*0.8660254f)
        float Grid_BORDER = GRID_WIDTH / 1.5f;

        int cell_y;
        int cell_x;
        xPos = xPos / ResDefaultConfig.Map.MAP_SCALE;
        yPos = yPos / ResDefaultConfig.Map.MAP_SCALE;
        cell_x = (int) (xPos / GRID_WIDTH);
        float x = xPos - cell_x * GRID_WIDTH;

        cell_y = (int) (yPos / GRID_HEIGHT);
        float y = yPos - cell_y * GRID_HEIGHT;


       if (!(Math.abs(GRID_WIDTH / 2 - x) + Math.abs(GRID_HEIGHT / 2 - y) / Math.sqrt(3) <= Grid_BORDER * Math.sqrt(3))) {
            //不在六边形内部
            if (x > GRID_WIDTH / 2) {
                //在右边
                cell_x++;
            }
        }
        int mapX=cell_x+refX;
        int mapY;
       if((mapX&1)==1){
           if(y>GRID_HEIGHT/2){
               mapY=cell_y+refY;
           }else {
               mapY=cell_y+refY-1;
           }
       }else{
           mapY=cell_y+refY;
       }

        int mapId=mapX+mapY*mapW;

       if(coord==null){
           coord = new Coord(mapX,mapY,mapId );
       }else {
           coord.initCoord(mapX,mapY,mapId);
       }
        return coord;
    }

    //给旧的赋值 而不是新建对象了,适用于单一的点击
    public static Coord getHotCellforOld(float xPos, float yPos, float zoom,Coord coord,float mapHegight,int mapW) {

        float GRID_WIDTH = ResDefaultConfig.Map.GRID_WIDTH;// (CELL_BORDER*1.5f)
        float GRID_HEIGHT = ResDefaultConfig.Map.HEXAGON_HEIGHT;// (CELL_BORDER*0.8660254f)
        float Grid_BORDER = GRID_WIDTH / 1.5f;

        int cell_y;
        int cell_x;
        xPos = xPos / ResDefaultConfig.Map.MAP_SCALE;
        yPos = yPos / ResDefaultConfig.Map.MAP_SCALE;
        cell_x = (int) (xPos / GRID_WIDTH);
        float x = xPos - cell_x * GRID_WIDTH;

        cell_y = (int) (yPos / GRID_HEIGHT);
        float y = yPos - cell_y * GRID_HEIGHT;


        //if(! (Grid_BORDER-Math.abs((x-1/2*GRID_WIDTH))>Math.abs(y-1/2*GRID_HEIGHT)/Math.sqrt(3))) {
        if (!(Math.abs(GRID_WIDTH / 2 - x) + Math.abs(GRID_HEIGHT / 2 - y) / Math.sqrt(3) <= Grid_BORDER * Math.sqrt(3))) {
            //不在六边形内部
            if (x > GRID_WIDTH / 2) {
                //在右边
                cell_x++;
            }
        }
        if((cell_x % 2)==1&&y<GRID_WIDTH / 2){
            cell_y--;
        }
        if(coord==null){
             coord = new Coord(cell_x, cell_y);
        }

        coord.setVByMap(cell_x,cell_y,zoom,mapHegight,mapW);

        return coord;

    }

    // 六边形网格定位
    // @param xPos 输入，所需查询的点的x坐标 
    // @param yPos 输入，所需查询的点的y坐标,y必须转换,因为以左上开始计算
    // @param cell_x 输出，改点所在网格的x坐标
    // @param cell_y 输出，改点所在网格的y坐标
    public static Coord getHotCell2(float xPos, float yPos) {

        float GRID_WIDTH = ResDefaultConfig.Map.GRID_WIDTH;
        float GRID_HEIGHT = ResDefaultConfig.Map.HEXAGON_HEIGHT;// (CELL_BORDER*0.8660254f)
        float GRID_W_B = (GRID_WIDTH / 4);//宽-变成/2 即小长

        int cell_y;
        int cell_x;
        float vertex_x_px;
        float vertex_y_px;
        xPos = xPos / ResDefaultConfig.Map.MAP_SCALE;//不知道为什么会出现偏移,所以通过这里来减少偏移
        yPos = yPos / ResDefaultConfig.Map.MAP_SCALE;
        cell_x = (int) (xPos / GRID_WIDTH);
        float x = Math.abs(xPos - cell_x * GRID_WIDTH);
        cell_y = (int) (yPos / GRID_HEIGHT);
        float y = Math.abs(yPos - cell_y * GRID_HEIGHT);

        //先判断位置
        if (x < GRID_W_B) {
            if (y < GRID_HEIGHT / 2) {
                //在上
                if (x / Math.tan(60) < y) {
                    cell_x++;
                }
            } else {
                //在下
                if (x / Math.tan(60) < (y - GRID_HEIGHT / 2)) {
                    cell_x++;
                }
            }
            //右边
        }/**/


        if (cell_x % 2 == 0 && y < 64) {
            cell_y--;
        } else if (cell_x % 2 != 0) {
            cell_y--;
        }
        /*if((cell_x&1)!=1&& y>64) {
            cell_y=cell_y-1;
        }
        if(cell_y<0) {
            cell_y=0;
        }*/

        vertex_x_px = cell_x * GRID_WIDTH * ResDefaultConfig.Map.MAP_SCALE;
        vertex_y_px = cell_x % 2 != 0 ? (cell_y + 1) * GRID_HEIGHT * ResDefaultConfig.Map.MAP_SCALE : (cell_y + 0.5f) * GRID_HEIGHT * ResDefaultConfig.Map.MAP_SCALE;
        Coord coord = new Coord(-cell_x, cell_y - 1, vertex_x_px, vertex_y_px);
        return coord;
    }

    public static float getX_pxByHexagon(int x,float scale,float refx){
      return  (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2))-refx* scale;
    }
    //ifFlip true 则下为原点 false 上为原点
    public static float getY_pxByHexagon(int x,int y,float mapH_px,float scale,float refy,boolean ifFlip){
       if(ifFlip){
           return mapH_px- ( (scale * ((((x & 1) == 1 ?
                   (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT
                   :
                   (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT  + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF/**/
           )) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2))+refy*scale);
        }else {
           return   (scale * ((((x & 1) == 1 ?
                   (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT :
                   (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF
           )) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2))+refy*scale;
        }
     }
   //  dy_px = -(vh % (ResConfig.Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) + ((vh) / zoom) * (zoom - 1) + getImgY() * zoom + vh / zoom - ((((int) ((getImgY() * zoom) / (ResConfig.Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) - 1) * (ResConfig.Map.HEXAGON_HEIGHT * Map.MAP_SCALE * zoom)) + (((gy - 1) * ResConfig.Map.HEXAGON_HEIGHT + ((gx & 1) == 1 ? 0 : ((ul_parity_x & 1) == 1 ? ResConfig.Map.HEXAGON_HEIGHT_REF : -ResConfig.Map.HEXAGON_HEIGHT_REF))) * zoom * Map.MAP_SCALE) + ((ul_parity_x & 1) == 1 ? -ResConfig.Map.HEXAGON_HEIGHT_REF : 0) * Map.MAP_SCALE * zoom);



    //获得坐标
    public static int getHX(int id, int w) {
       return (id % w) ;
    }
    public static int getHY(int id, int w) {
        return (id / w) ;
    }

    public static int getAreaId(ArrayList<SpriteDAO> mapSprites,int mapW ,int sx, int sy) {
        int v=0;
        if(sx>mapW){
           sx=sx-mapW;
           v=1000;
        }
        for(int i=0,iMax=mapSprites.size();i<iMax;i++){
            if(sx>mapSprites.get(i).getRefx()&&sx<mapSprites.get(i).getRefx()+ ResDefaultConfig.Map.PT_SIDE
            &&sy>mapSprites.get(i).getRefy()&&sy<mapSprites.get(i).getRefy()+ ResDefaultConfig.Map.PT_SIDE){
                return i+v;
            }
        }
        return 0;
    }

    public static int getId(int x, int y, int w) {
        return x+y*w;
    }

    //获得该id放大一倍后的位置 w是放大前的w
    public static int getZoomId(int oId,int w) {
        int ox=getHX(oId,w);
        int oy=getHY(oId,w);
        return getId(ox*2,oy*2,w*2);
    }

    public static Texture getMapImage(MainGame game,XmlReader.Element xmlE) {
        if(xmlE!=null&&!ComUtil.isEmpty(xmlE.get("image",""))){
            FileHandle f;
            String imagePath= ResDefaultConfig.Path.MapFolderPath+xmlE.get("image","");
            if(game.gameConfig.ifColor &&!ComUtil.isEmpty(xmlE.get("colorImage",""))){
                imagePath= ResDefaultConfig.Path.MapFolderPath+xmlE.get("colorImage","");
            }
            if(game.gameConfig.getModId()==0){
                f=Gdx.files.internal(imagePath);
            }else {
                f= Gdx.files.local(game.gameConfig.getModPath()+imagePath);
                FileHandle f1=Gdx.files.internal(imagePath);
                if(!f.exists()&&f1.exists()){
                    f1.copyTo(f);
                }
            }
            if(f!=null&&f.exists()){
                return new Texture(f);
            }
        }
        return null;
    }

    public static Texture getFloorMapImage(MainGame game,XmlReader.Element xmlE) {
        if(xmlE!=null&&!ComUtil.isEmpty(xmlE.get("floorImage",""))){
            FileHandle f;
            String imagePath= ResDefaultConfig.Path.MapFolderPath+xmlE.get("floorImage","");
            if(game.gameConfig.getModId()==0){
                f=Gdx.files.internal(imagePath);
            }else {
                f= Gdx.files.local(game.gameConfig.getModPath()+imagePath);
                FileHandle f1=Gdx.files.internal(imagePath);
                if(!f.exists()&&f1.exists()){
                    f1.copyTo(f);
                }
            }
            if(f!=null&&f.exists()){
                return new Texture(f);
            }
        }
        return null;
    }
    // TODO
    // 横六边形地图类
    class hexagonMap {
        private int w;
        private int h;
        private float w_px;// 单个像素长度
        private float h_px;// 单个像素宽度
        private float sum_w_px;
        private float sum_h_px;
    }

    //画六边形网格
    public static Pixmap drawHexagonGrid(Pixmap pixmap, Fb2Map mapBinDAO)// 画网格
    {
        // 加载边框
        int x = 0;
        int y = 0;
        int x_px = 0;
        int y_px = 0;
        // 将要取得的图片的id和名字放入位置
        Pixmap gridPixmap = new Pixmap(Gdx.files.internal("pixmap/tiles/grid.png"));
        // 绘制网格图片
        for (int id = 0,idMax=mapBinDAO.getMapbin().size; id < idMax; id++) {
            x = (id % mapBinDAO.getMapWidth()) + 1;
            y = (id / mapBinDAO.getMapWidth()) + 1;
         //  x_px = (int) ((x - 1) * ResConfig.Map.GRID_WIDTH);
           // y_px = (int) (((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF));

            x_px=getX_px(x,1f);
            y_px=getY_px(x,y,1f);
            // Gdx.app.log("绘制地图网格", "mapId:" + id + " x:" + x + " y:" + y);
            pixmap.drawPixmap(gridPixmap, 0, 0, gridPixmap.getWidth(), gridPixmap.getHeight(), (int) ((x_px + mapBinDAO.getMapbin().get(id).getBackRefX()) * ResDefaultConfig.Map.MAP_SCALE), (int) ((y_px + mapBinDAO.getMapbin().get(id).getBackRefY()) * ResDefaultConfig.Map.MAP_SCALE),
                    (int) (gridPixmap.getWidth() * ResDefaultConfig.Map.MAP_SCALE), (int) (gridPixmap.getHeight() * ResDefaultConfig.Map.MAP_SCALE));
        }
        // 清除边框
        gridPixmap.dispose();
        gridPixmap=null;
        return pixmap;
    }

    //画六边形网格
    public static Pixmap drawHexagonGridByView(Pixmap pixmap, AssetManager am)// 画网格
    {
        // 加载边框
        int x = 0;
        int y = 0;
        int x_px = 0;
        int y_px = 0;
        // 将要取得的图片的id和名字放入位置
        //Pixmap gridPixmap = new Pixmap(Gdx.files.internal("pixmap/tiles/grid.png"));
        Pixmap gridPixmap = am.get(("pixmap/tiles/grid.png"), Pixmap.class);

        int w = (int) (pixmap.getWidth() / ResDefaultConfig.Map.GRID_WIDTH / ResDefaultConfig.Map.MAP_SCALE) + 3;
        int h = (int) (pixmap.getHeight() / ResDefaultConfig.Map.HEXAGON_HEIGHT / ResDefaultConfig.Map.MAP_SCALE) + 3;
        int size = w * h;

        // 绘制网格图片
        for (int id = 0; id < size; id++) {
            x = (id % w) + 1;
            y = (id / w) + 1;
            // x_px=(int) ((x-1)*139.5);
            // y_px=(int) ((x&1) == 1?(y-1)*161:(y-1)*161+80.5);
            //x_px = (int) ((x - 1) * ResConfig.Map.GRID_WIDTH);
            //y_px = (int) (((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF));
            x_px=getX_px(x,1f);
            y_px=getY_px(x,y,1f);

            //Gdx.app.log("绘制地图网格", "mapId:" + id + " x:" + x + " y:" + y+ " x_px:" + x_px + " y_px:" + y_px);
            pixmap.drawPixmap(gridPixmap, 0, 0, gridPixmap.getWidth(), gridPixmap.getHeight(), (int) ((x_px) * ResDefaultConfig.Map.MAP_SCALE), (int) ((y_px) * ResDefaultConfig.Map.MAP_SCALE),
                    (int) (gridPixmap.getWidth() * ResDefaultConfig.Map.MAP_SCALE), (int) (gridPixmap.getHeight() * ResDefaultConfig.Map.MAP_SCALE));
        }
        // 清除边框
        //gridPixmap.dispose();
        return pixmap;
    }


    // Pixmap大小计算

    public static Pixmap createPixmap(int mapW, int mapH,float scale) {

        int x_px= getX_px(mapW,scale)+ (int) (scale * ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE);
        int y_px= getY_px(mapW,mapH,scale)+(int) (scale * ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE);
            return new Pixmap(x_px, y_px, Pixmap.Format.RGBA8888);
    }

    //创建一个当前窗口大小的画布
    public static Pixmap createPixmapByView(float vw, float vh) {
        int w = (int) vw;
        int h = (int) vh;
        //Gdx.app.log("海洋地图:"," w:" + w + " h:" +h);

        return new Pixmap(w, h, Pixmap.Format.RGBA8888);
    }


    // 使用前必须初始化 pixmap
    // ptId-->type -1固定 1海洋 0底图通用 2沙漠 3雪地 4草地 5泥地 6外星 7热带草地
    public static Pixmap coverImgByPtimgId(MainGame game,Pixmap pixmap, int ptId) {
        // 通过配置文件获取读取的pt
        //Array<XmlReader.Element> defPts = ResConfig.gameConfig.getDEF_PT().getChildrenByName("pt");


        /*for (int i = 0,iMax=defPts.size; i < iMax; i++) {
            if (defPts.get(i).getInt("id") == ptId) {
                imgPtStr = defPts.get(i).get("image");
                break;
            }
        }*/
       // imgPtStr=game.gameConfig.getDEF_PT().getElementById(ptId).get("image");
        if (pixmap != null) {
            // 获取图片资源
             Pixmap imgPt = new Pixmap(Gdx.files.internal("pixmap/pts/pt_"+ptId+".png"));
            // 获取目标长宽,并循环绘制
            int w = (int) ((pixmap.getWidth() / imgPt.getWidth()) + 1);
            int h = (int) ((pixmap.getHeight() / imgPt.getHeight()) + 1);
            int x = 0;
            int y = 0;
            int x_px;
            int y_px;
            int coverCount = w * h;

            // Gdx.app.log("地图底图开始构建", "w:" + w + " h:" + h );
            for (int i = 0; i < coverCount; i++) {
                x = i % w;
                y = (int) (i / w);
                x_px = x * imgPt.getWidth();
                y_px = y * imgPt.getHeight();
                Gdx.app.log("地图底图开始构建", " id:" + i + " x:" + x + " y:" + y + "x_px:" + x_px + " y_px:" + y_px);
                pixmap.drawPixmap(imgPt, x_px, y_px, 0, 0, imgPt.getWidth(), imgPt.getHeight());
            }

            // 清除资源
            imgPt.dispose();
            imgPt=null;
        }
        return pixmap;
    }

    //获得地形装饰的最高上限,最低下限为0
    //储存方式为 Map(id_type+"_min",idx_min) Map(id_type+"_max",idx_max)
    public static Map getDecorateRandMaxMap(MainGame game) {
        Array<XmlReader.Element> defTerrainimgs = game.gameConfig.getDEF_TERRAINIMG().e.getChildrenByName("mapterrain");
        int i, iLength;
        iLength = defTerrainimgs.size;
        Map map = new HashMap();
        for (i = 0; i < iLength; i++) {//省略掉类型为-1的值
            if (defTerrainimgs.get(i).getInt("type") == -1) {
            } else if (!map.containsKey((defTerrainimgs.get(i).getInt("id") + "_" + defTerrainimgs.get(i).getInt("type") + "_min"))) {
                map.put(defTerrainimgs.get(i).getInt("id") + "_" + defTerrainimgs.get(i).getInt("type") + "_min", defTerrainimgs.get(i).getInt("idx"));
            } else if (!map.containsKey((defTerrainimgs.get(i).getInt("id") + "_" + defTerrainimgs.get(i).getInt("type") + "_max"))) {
                map.put(defTerrainimgs.get(i).getInt("id") + "_" + defTerrainimgs.get(i).getInt("type") + "_max", defTerrainimgs.get(i).getInt("idx"));
            } else if ((Integer) map.get(defTerrainimgs.get(i).getInt("id") + "_" + defTerrainimgs.get(i).getInt("type") + "_min") > defTerrainimgs.get(i).getInt("idx")) {
                map.put(defTerrainimgs.get(i).getInt("id") + "_" + defTerrainimgs.get(i).getInt("type") + "_min", defTerrainimgs.get(i).getInt("idx"));
            } else if ((Integer) map.get(defTerrainimgs.get(i).getInt("id") + "_" + defTerrainimgs.get(i).getInt("type") + "_max") < defTerrainimgs.get(i).getInt("idx")) {
                map.put(defTerrainimgs.get(i).getInt("id") + "_" + defTerrainimgs.get(i).getInt("type") + "_max", defTerrainimgs.get(i).getInt("idx"));
            }
        }
        return map;
    }/**/


    //添加周围的id坐标
    public static List<Coord> addCoordByIds(List<Integer> aroundIds, int w) {
        int cell_x, cell_y;
        float vertex_x_px, vertex_y_px;
        List<Coord> coords = new ArrayList<Coord>();

        for (Integer id : aroundIds) {
            cell_x = id - (int) (id / w) * w;
            cell_y = (int) (id / w) + 1;
            vertex_x_px = -cell_x * ResDefaultConfig.Map.GRID_WIDTH * ResDefaultConfig.Map.MAP_SCALE;
            vertex_y_px = -cell_x % 2 != 0 ? (cell_y + 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE : (cell_y + 0.5f) * ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE;
            coords.add(new Coord(cell_x, cell_y, vertex_x_px, vertex_y_px));
        }
        return coords;
    }



    //根据smallMap文件绘制图片顺便清理图片 Texture
    public static Texture mergeGameMapAndClearPixmap(MainGame game, int mapId, AssetManager am) {
       // XmlReader reader = ResConfig.reader;
        String imgFileName;
        int w = 0;
        int h = 0;
        Texture texture = null;
        XmlReader.Element xmlFile= game.gameConfig.getDEF_SMALLMAP().getElementById(mapId);
                imgFileName = xmlFile.get("name");
                texture=new Texture(xmlFile.getInt("width"), xmlFile.getInt("height"), Pixmap.Format.RGBA8888);

                for (int j = 0,jMax=xmlFile.getChildCount(); j < jMax; j++) {
                    texture.draw(am.get(("image/" + imgFileName + "/" + xmlFile.getChild(j).get("n")), Pixmap.class), xmlFile.getChild(j).getInt("x"), xmlFile.getChild(j).getInt("y"));
                    am.unload("image/" + imgFileName + "/" + xmlFile.getChild(j).get("n"));
                }

        //PixmapIO.writePNG(Gdx.files.external("smallWorld.png"), pixmap);
        return texture;
    }


    public static Texture mergeGameMapByZip(MainGame game, int mapId){
        XmlReader.Element defMap=game.gameConfig.getDEF_MAP().getElementById(mapId);
        String mapName=defMap.get("name");
        mapName=mapName+"_hd";
        /*if(game.gameConfig.getIfAnimation()){
            mapName=mapName+"_hd";
        }else {
            mapName=mapName+"_sd";
        }*/
      //  int w = GameMap.getWidth(defMap.getInt("width"));
      //  int h = GameMap.getHeight(defMap.getInt("height"));
        float scale=game.getMapScale();

       // w = (int) (w * ResConfig.Map.MAP_SCALE*scale);
       // h = (int) (h * ResConfig.Map.MAP_SCALE*scale);

     //   Texture gameMap=new Texture(w, h, Pixmap.Format.RGBA8888);
        Pixmap gameMap=createPixmap(defMap.getInt("width"),defMap.getInt("height"),scale);
       int  w = (int) ((gameMap.getWidth() / ResDefaultConfig.Map.PT_SIDE) + 1);
       int  h = (int) ((gameMap.getHeight() / ResDefaultConfig.Map.PT_SIDE) + 1);
        int x = 0;
        int y = 0;
        int x_px;
        int y_px;
        int spliteCount = w * h;
        int spliteW;
        int spliteH;
        String pixmapName;
        Texture texture=null;
        // Gdx.app.log("地图底图开始构建", "w:" + w + " h:" + h );
        for (int i = 0; i < spliteCount; i++) {
            x = i % w;
            y = (int) (i / w);
            x_px = x * ResDefaultConfig.Map.PT_SIDE;
            y_px = y * ResDefaultConfig.Map.PT_SIDE;
            if(x_px+ ResDefaultConfig.Map.PT_SIDE>gameMap.getWidth()){
                spliteW=gameMap.getWidth()-x_px;
            }else {
                spliteW= ResDefaultConfig.Map.PT_SIDE;
            }
            if(y_px+ ResDefaultConfig.Map.PT_SIDE>gameMap.getHeight()){
                spliteH=gameMap.getHeight()-y_px;
            }else {
                spliteH= ResDefaultConfig.Map.PT_SIDE;
            }
           pixmapName= ResDefaultConfig.Path.ZipFolderPath+mapName+"/"+i+".png";

           Pixmap pixmap=game.getAssetManager().get(pixmapName, Pixmap.class);
            gameMap.drawPixmap(pixmap,x_px,y_px,0,0,spliteW,spliteH);
           // GameUtil.textureDrawPixmap(gameMap,pixmap,x_px,y_px,spliteW,spliteH);

            Gdx.app.log("mergeMap","pixmapName:"+pixmapName+" x:"+x_px+" y:"+y_px+" w:"+spliteW+" h:"+spliteH);
            game.getAssetManager().unload(pixmapName);
         }
        texture=new Texture(gameMap);
      //  PixmapIO.writePNG(Gdx.files.external(mapName+".png"), gameMap);
        gameMap.dispose();
        return  texture;
    }

    //废弃 获得需要集合的图片
    //ifFlip true,则y值为高-y,false 从左上开始记录
    public static ArrayList<SpriteDAO> getGameMapSprites(MainGame game,int mapW_px,int mapH_px, ArrayList<SpriteDAO> rs,boolean ifFlip,int refY){
        if(rs==null){
            rs=new ArrayList<SpriteDAO>();
        }else {
            rs.clear();
        }

        int w = (int) ((mapW_px / ResDefaultConfig.Map.PT_SIDE) + 1);
        int h = (int) ((mapH_px/ ResDefaultConfig.Map.PT_SIDE) + 1);
        int x = 0;
        int y = 0;
        int x_px;
        int y_px;
        int spliteCount = w * h;
        // Gdx.app.log("地图底图开始构建", "w:" + w + " h:" + h );
        String mapName=game.mapFolder;
        String tName;
   //   int  refY=mapH_px-(h-1)*ResConfig.Map.PT_SIDE;
        for (int i = 0; i < spliteCount; i++) {
            x = i % w;
            if(ifFlip){
                y = h-2-(int) (i / w);
            }else {
                y = (int) (i / w);
            }
            x_px = x * ResDefaultConfig.Map.PT_SIDE;
            y_px = (int) (y * ResDefaultConfig.Map.PT_SIDE+refY);
            tName=i+".png";
          Texture texture=game.getAssetManager().get( ResDefaultConfig.Path.ZipFolderPath+mapName+"/"+tName,Texture.class) ;
            SpriteDAO t=new SpriteDAO(new Sprite(texture),tName,x_px,y_px);
            rs.add(t);
        }
        return rs;
    }

    public static ArrayList<SpriteDAO> getPTMapSprites(MainGame game,int ptId,int mapW_px,int mapH_px, ArrayList<SpriteDAO> rs,boolean ifFlip,int refY){
        if(rs==null){
            rs=new ArrayList<SpriteDAO>();
        }else {
            rs.clear();
        }

        int w = (int) ((mapW_px / ResDefaultConfig.Map.PT_SIDE) + 1);
        int h = (int) ((mapH_px/ ResDefaultConfig.Map.PT_SIDE) + 1);
        int x = 0;
        int y = 0;
        int x_px;
        int y_px;
        int spliteCount = w * h;
        // Gdx.app.log("地图底图开始构建", "w:" + w + " h:" + h );
        String mapName=game.mapFolder;
        String tName;
        //   int  refY=mapH_px-(h-1)*ResConfig.Map.PT_SIDE;
        TextureRegion r=game.getPTTextureRegion(ptId);

        for (int i = 0; i < spliteCount; i++) {
            x = i % w;
            if(ifFlip){
                y = h-2-(int) (i / w);
            }else {
                y = (int) (i / w);
            }
            x_px = x * ResDefaultConfig.Map.PT_SIDE;
            y_px = (int) (y * ResDefaultConfig.Map.PT_SIDE+refY);
            tName=i+".png";

            SpriteDAO t=new SpriteDAO(new Sprite(r),tName,x_px,y_px);
            rs.add(t);
        }
        return rs;
    }


    public static Texture drawGameMapByZipFolder(MainGame game,int mapId, AssetManager am) {
        // XmlReader reader = ResConfig.reader;
        String imgFileName;
        int w = 0;
        int h = 0;
        Texture texture = null;
        XmlReader.Element xmlFile= game.gameConfig.getDEF_SMALLMAP().getElementById(mapId);
        imgFileName = xmlFile.get("name");
        texture=new Texture(xmlFile.getInt("width"), xmlFile.getInt("height"), Pixmap.Format.RGBA8888);

        for (int j = 0,jMax=xmlFile.getChildCount(); j < jMax; j++) {
            texture.draw(am.get(("image/" + imgFileName + "/" + xmlFile.getChild(j).get("n")), Pixmap.class), xmlFile.getChild(j).getInt("x"), xmlFile.getChild(j).getInt("y"));
            am.unload("image/" + imgFileName + "/" + xmlFile.getChild(j).get("n"));
        }

        //PixmapIO.writePNG(Gdx.files.external("smallWorld.png"), pixmap);
        return texture;
    }
    //获取坐标中心点
    public static void getCenterPotionById(Fb2Map mapBin, int sourceX, int sourceY, float scale, int id){
        int x = (id % mapBin.getMapWidth()) + 1;
        int y = (id / mapBin.getMapWidth()) + 1;
        sourceX = (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        sourceY = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
    }

    //获取坐标中心点 起点为左上
    public static Map<String,Integer> getLeftTopDirectCoordById(int mapW,int sourceX,int sourceY,float scale,int id){
        int x = (id % mapW) + 1;
        int y = (id / mapW) + 1;
        sourceX = (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        sourceY = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
        Map<String,Integer> rs=new HashMap<>();
        rs.put("sourceX",sourceX);
        rs.put("sourceY",sourceY);
        return rs;
    }

    //获取坐标中心点 起点为左下
    public static Map<String,Integer> getUpleftPotionById(int mapW,float scale,int id){
        int x = (id % mapW) + 1;
        int y = (id / mapW) + 1;
       int sourceX = (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        int sourceY = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
        Map<String,Integer> rs=new HashMap<>();
        rs.put("sourceX",sourceX);
        rs.put("sourceY",sourceY);
        return rs;
    }

    //获取坐标中心点 起点为左下  mapH_px为地图高
    // 推荐使用 getX_pxByHexagon getY_pxByHexagon 两个方法代替
    public static Map<String,Integer> getDownleftPotionById(int mapW,int mapH_px,float scale,int id,Map<String,Integer> rs){
        int x = (id % mapW) + 1;
        int y = (id / mapW) + 1;

        int sourceX= (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        int sourceY=mapH_px- (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
        rs.put("sourceX",sourceX);
        rs.put("sourceY",sourceY);
        return rs;
    }

    /**绘制虚线
     * Draws a dotted line between to points (x1,y1) and (x2,y2).
     * @param shapeRenderer
     * @param dotDist (distance between dots)
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public static void drawDottedLine(ShapeRenderer shapeRenderer, int dotDist, float x1, float y1, float x2, float y2) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);

        Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
        float length = vec2.len();
        for(int i = 0; i < length; i += dotDist) {
            vec2.clamp(length - i, length - i);
            shapeRenderer.point(x1 + vec2.x, y1 + vec2.y, 0);
        }
        shapeRenderer.end();
    }


    public static void drawDottedLine(Pixmap pixmap,int dotDist, float x1, float y1, float x2, float y2){
         Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
        float length = vec2.len();
        for(int i = 0; i < length; i += dotDist) {
            vec2.clamp(length - i, length - i);
            pixmap.drawPixel((int)(x1 + vec2.x),(int) (y1 + vec2.y) );
        }
    }
    //绘制贝塞尔曲线  起点，控制点，控制点，终点
    public static void drawBezierLine(Pixmap pixmap,int dotDist,
                                      float x1, float y1, float x2, float y2,
                                      float x3, float y3, float x4, float y4


    ){
        Bezier<Vector2> bezier = new Bezier<Vector2>(
                new Vector2(x1, y1),
                new Vector2(x2, y2),
                new Vector2(x3, y3),
                new Vector2(x4, y4));
        int dis = (int)(bezier.points.get(bezier.points.size - 1).x - bezier.points.get(0).x);
        if (dis == 0) {
            dis = (int)(bezier.points.get(bezier.points.size - 1).y - bezier.points.get(0).y);
        }
        Vector2[] points = new Vector2[dis];
        for (int i = 0; i < dis; i++) {
            float t = i * 1f / dis;
            Vector2 out = new Vector2();
            bezier.valueAt(out, t);
            points[i] = out;
        }
        for (int i = 0, l = points.length - 1; i < l; i++) {
            Vector2 cur = points[i];
            Vector2 next = points[i + 1];
            pixmap.drawLine((int)cur.x,(int)cur.y,(int)next.x,(int)next.y );
        }
    }



    /*绘制全图边界线 TODO

     */
    /*public static Pixmap drawCountryBorder(MapBinDAO mapBinDAO, Pixmap pixmap, float scale,PixmapListDAO pixmapLists,HexagonDAO hexagons){
        StringName border;
        int x = 0;
        int y = 0;
        int x_px;
        int y_px;
        int hexgaon_x1;//左上,左下
        int hexgaon_x2;//右上,右下
        int hexgaon_x3;//左中
        int hexgaon_x4;//右中
        int hexgaon_y1;//上
        int hexgaon_y2;//中
        int hexgaon_y3;//下

        PixmapDAO tempPixmap ;

        for(int id=0;id<mapBinDAO.getMapbin().size();id++){
            border=mapBinDAO.getMapbin().get(id).getBorder();
            if(!border.equals(ResConfig.StringName.noBorder)){

                x = (id % mapBinDAO.getMapWidth()) + 1;
                y = (id / mapBinDAO.getMapWidth()) + 1;
                x_px = (int) ((x - 1) * ResConfig.Map.GRID_WIDTH);
                y_px = (int) (((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF));

                hexgaon_x1 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 35 * ResConfig.Map.MAP_SCALE));//左上,左下 原来是36
                hexgaon_x2 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + 113 * ResConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
                hexgaon_x3 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE - 1));//左中
                hexgaon_x4 = (int) (scale * (((x - 1) * ResConfig.Map.GRID_WIDTH) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_WIDTH * ResConfig.Map.MAP_SCALE));//右中
                hexgaon_y1 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE - 1));//上
                hexgaon_y2 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE / 2));//中
                hexgaon_y3 = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF)) * ResConfig.Map.MAP_SCALE + ResConfig.Map.HEXAGON_HEIGHT * ResConfig.Map.MAP_SCALE));//下

                if(border.substring(0,1).equals(ResConfig.StringName.one)){//左中

                    tempPixmap=pixmapLists.getPixmapByName("grid_border1");
                    pixmap.drawPixmap(tempPixmap.getPixmap(), 0, 0, tempPixmap.getPixmap().getWidth(), tempPixmap.getPixmap().getHeight(), (int) ((x_px+tempPixmap.getRefx()) * ResConfig.Map.MAP_SCALE), (int) ((y_px+tempPixmap.getRefy()) * ResConfig.Map.MAP_SCALE),
                            (int) (tempPixmap.getPixmap().getWidth() * ResConfig.Map.MAP_SCALE), (int) (tempPixmap.getPixmap().getHeight() * ResConfig.Map.MAP_SCALE));

                }else if(border.substring(0,1).equals(ResConfig.StringName.two)){
                    //pixmap.setColor(Color.RED);
                    //pixmap.drawLine(hexgaon_x3,hexgaon_y2,hexgaon_x1,hexgaon_y1);

                    tempPixmap=pixmapLists.getPixmapByName("grid_border4");
                    pixmap.drawPixmap(tempPixmap.getPixmap(), 0, 0, tempPixmap.getPixmap().getWidth(), tempPixmap.getPixmap().getHeight(), (int) ((x_px+tempPixmap.getRefx()) * ResConfig.Map.MAP_SCALE), (int) ((y_px+tempPixmap.getRefy()) * ResConfig.Map.MAP_SCALE),(int) (tempPixmap.getPixmap().getWidth() * ResConfig.Map.MAP_SCALE), (int) (tempPixmap.getPixmap().getHeight() * ResConfig.Map.MAP_SCALE));
                }



                if(border.substring(1,2).equals(ResConfig.StringName.one)) {//左上
                    //pixmap.setColor(Color.CYAN);
                    //pixmap.drawLine(hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                    tempPixmap=pixmapLists.getPixmapByName("grid_border2");
                    pixmap.drawPixmap(tempPixmap.getPixmap(), 0, 0, tempPixmap.getPixmap().getWidth(), tempPixmap.getPixmap().getHeight(), (int) ((x_px+tempPixmap.getRefx()) * ResConfig.Map.MAP_SCALE), (int) ((y_px+tempPixmap.getRefy()) * ResConfig.Map.MAP_SCALE), (int) (tempPixmap.getPixmap().getWidth() * ResConfig.Map.MAP_SCALE), (int) (tempPixmap.getPixmap().getHeight() * ResConfig.Map.MAP_SCALE));
                }else if(border.substring(1,2).equals(ResConfig.StringName.two)){

                    pixmap.setColor(Color.YELLOW);
                    pixmap.drawLine(hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                    //tempPixmap=pixmapLists.getPixmapByName("grid_border5");
                    //pixmap.drawPixmap(tempPixmap.getPixmap(), 0, 0, tempPixmap.getPixmap().getWidth(), tempPixmap.getPixmap().getHeight(), (int) ((x_px+tempPixmap.getRefx()) * ResConfig.Map.MAP_SCALE), (int) ((y_px+tempPixmap.getRefy()) * ResConfig.Map.MAP_SCALE),(int) (tempPixmap.getPixmap().getWidth() * ResConfig.Map.MAP_SCALE), (int) (tempPixmap.getPixmap().getHeight() * ResConfig.Map.MAP_SCALE));
                }
                if(border.substring(2,3).equals(ResConfig.StringName.one)){//右上
                    //pixmap.setColor(Color.BROWN);
                    //pixmap.drawLine(hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2);

                    tempPixmap=pixmapLists.getPixmapByName("grid_border3");
                    pixmap.drawPixmap(tempPixmap.getPixmap(), 0, 0, tempPixmap.getPixmap().getWidth(), tempPixmap.getPixmap().getHeight(), (int) ((x_px+tempPixmap.getRefx()) * ResConfig.Map.MAP_SCALE), (int) ((y_px+tempPixmap.getRefy()) * ResConfig.Map.MAP_SCALE),(int) (tempPixmap.getPixmap().getWidth() * ResConfig.Map.MAP_SCALE), (int) (tempPixmap.getPixmap().getHeight() * ResConfig.Map.MAP_SCALE));
                }else if(border.substring(2,3).equals(ResConfig.StringName.two)){
                    pixmap.setColor(Color.BLUE);
                    pixmap.drawLine(hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2);
                }
            }
        }
        return pixmap;
    }
*/


    //修改region的颜色,颜色根据region归属来决定
    public static Pixmap updColorByRegion(Fb2Smap smapDao, Pixmap pixmap, float scale, IntArray ids, boolean ifDrawSea, boolean ifDrawWarLine) {

        if (smapDao.hexagonDatas != null &&smapDao.legionColors!=null&&smapDao.masterData.getIfColor()==1 && smapDao.hexagonDatas.size > 0) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            int hexgaon_x1;
            int hexgaon_x2;
            int hexgaon_x3;
            int hexgaon_x4;
            int hexgaon_y1;
            int hexgaon_y2;
            int hexgaon_y3;
            int nextRegionId = 0;
            Color color = Color.CLEAR;
            boolean ifDrawIdWarLine;
            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.None);
            for (int id=0,iMax=ids.size,i=0;id<iMax;id++) {
                i=ids.get(id);
                if (  smapDao.hexagonDatas.get(i).getRegionId() != -1 ) {
                    if(!ifDrawSea&& smapDao.hexagonDatas.get(i).getBlockType() == 1){
                        continue;
                    }
                   /* if(i==17327){
                        Gdx.app.log("setColor test","id:"+id+" color:"+color);
                    }*/
                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充
                    if (smapDao.hexagonDatas.get(i).getRegionId() != nextRegionId) {
                        //获取颜色
                        color=smapDao.getColorForRegion(smapDao.hexagonDatas.get(i).getRegionId());
                        pixmap.setColor(color);
                        nextRegionId =smapDao.hexagonDatas.get(i).getRegionId();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    x = (i % smapDao.masterData.getWidth()) + 1;
                    y = (i / smapDao.masterData.getWidth()) + 1;


                    hexgaon_x1 = getHexagonX1(x,y,scale);//左上,左下
                    hexgaon_x2 =  getHexagonX2(x,y,scale);//右上,右下
                    hexgaon_x3 =  getHexagonX3(x,y,scale);//左中
                    hexgaon_x4 =  getHexagonX4(x,y,scale);//右中
                    hexgaon_y1 = getHexagonY1(x,y,scale);//上
                    hexgaon_y2 =  getHexagonY2(x,y,scale);//中
                    hexgaon_y3 =  getHexagonY3(x,y,scale);//下
                    x_px_circle=getX_px_Circle(x,scale);
                    y_px_circle=getY_px_Circle(x,y,scale);
                    ifDrawIdWarLine=ifDrawWarLine&&smapDao.buildIfDrawWarLine(smapDao.hexagonDatas.get(i).getRegionId());
                    /*if(ifDrawIdWarLine&&color==Color.CLEAR){
                        Gdx.app.log("setColor test","id:"+id+" color:"+color);
                    }*/
                   /* if(ifDrawWarLine&&!ifDrawIdWarLine){
                            pixmap.setColor(Color.CLEAR);
                            drawDottedLine(pixmap,1,hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2  );
                            drawDottedLine(pixmap,1,hexgaon_x1,hexgaon_y1,hexgaon_x2,hexgaon_y3  );
                            drawDottedLine(pixmap,1,hexgaon_x3,hexgaon_y2,hexgaon_x1,hexgaon_y3  );
                            pixmap.setColor(color);
                        }*/


                   /*if(ifDrawIdWarLine&&smapDao.hexagonDatas.get(i).getIfCoast()==1){
                       pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResConfig.Map.HEXAGON_WIDTH + 13) * ResConfig.Map.MAP_SCALE / 2 * scale));
                   }else */if(smapDao.hexagonDatas.get(i).getIfCoast()>0){
                      pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));
                    }else{
                        pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                        pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                        pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                        pixmap.fillTriangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                    }
                    if(ifDrawIdWarLine){
                        pixmap.setColor(Color.RED);
                        drawDottedLine(pixmap,1,hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2  );
                        drawDottedLine(pixmap,1,hexgaon_x1,hexgaon_y1,hexgaon_x2,hexgaon_y3  );
                        drawDottedLine(pixmap,1,hexgaon_x3,hexgaon_y2,hexgaon_x1,hexgaon_y3  );
                        pixmap.setColor(color);
                    }


                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                }
            }



            // Gdx.app.log("色图构建", "完成  color:"+color);
         //  PixmapIO.writePNG(Gdx.files.external("updateColor_"+ComUtil.getRandom(0,11111)+".png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败1:");
            return pixmap;
        }
    }




    //绘制build
    public static Pixmap drawAllBuildForSmap(Fb2Smap smapDao, AssetManager am,Pixmap pixmap, float scale) {
        //,  List<Color> legionColors, Map regionColors;
        //IntArray coastGrids=smapDao.coastGrids;
        //IntArray ids = smapDao.getIdsByRegionIds(regionIds);
        // IntArray excludeRegions=smapDao.excludeRegions;
        if (smapDao.hexagonDatas != null&& smapDao.buildRDatas.size() > 0) {

            int x = 0;
            int y = 0;

            Pixmap city_1 = am.get(("pixmap/smap/city_1.png"), Pixmap.class);
            Pixmap city_2 = am.get(("pixmap/smap/city_2.png"), Pixmap.class);
            Pixmap city_3 = am.get(("pixmap/smap/city_3.png"), Pixmap.class);
            Pixmap city_4 = am.get(("pixmap/smap/city_3.png"), Pixmap.class);
            Pixmap city_5 = am.get(("pixmap/smap/city_3.png"), Pixmap.class);
            Pixmap city_island = am.get(("pixmap/smap/city_island.png"), Pixmap.class);
            Pixmap city_port = am.get(("pixmap/smap/city_port.png"), Pixmap.class);
            Pixmap drawCity;

            int x_px;
            int y_px;

            //int ageRate=smapDao.getAge()==0?1:2;
            //先绘制海岸  //沿海地块用圆形来填充
            pixmap.setBlending(Blending.SourceOver);
         /*   Iterator<IntMap.Entry<Fb2Smap.BuildData>> it = smapDao.buildRDatas.iterator();
            while (it.hasNext()) {
                IntMap.Entry<Fb2Smap.BuildData> buildData = it.next();

                Fb2Smap.BuildData b=buildData.value;*/
            for(int bi=0;bi<smapDao.buildRDatas.size();bi++) {
                Fb2Smap.BuildData b=smapDao.buildRDatas.getByIndex(bi);
                if(b==null){continue;}
                if(b.getLegionIndex()==0){continue;}


                    if(smapDao.capitalLegionsMap.containsKey(b.getRegionId())){//首都
                        drawCity=city_5;
                    }else if(b.getBuildType()==2){//海岛
                        drawCity=city_island;
                    }else if(b.getBuildType()==1){//港口
                        drawCity=city_port;
                    }else if(b.getCityLvNow()>7){//大城市
                        drawCity=city_4;
                    }else if(b.getCityLvNow()>5){//大城市
                        drawCity=city_3;
                    }else if(b.getCityLvNow()>3){//大城市
                        drawCity=city_2;
                    }else{//普通城市
                        drawCity=city_1;
                    }
                    x = (b.getRegionId() % smapDao.masterData.getWidth()) + 1;
                    y = (b.getRegionId() / smapDao.masterData.getWidth()) + 1;

                    //x_px = (int) ((x - 1) * ResConfig.Map.GRID_WIDTH);
                    //y_px = (int) (((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF));

                    x_px=getX_px(x,scale);
                    y_px=getY_px(x,y,scale);

                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));

                        pixmap.drawPixmap
                                (drawCity,
                                        0,
                                        0,
                                        drawCity.getWidth(),
                                        drawCity.getHeight(),
                                        x_px,
                                        y_px ,
                                        (int) (scale *  drawCity.getWidth() ) ,
                                        (int) (scale *  drawCity.getHeight() ) );


                        //Gdx.app.log("地图层1构建成功", " backid:" + mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx() + "img:" + imgBack);



                }// Gdx.app.log("色图构建", "完成  color:"+color);
            //PixmapIO.writePNG(Gdx.files.external("updateBuild.png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败2:");
            return pixmap;
        }
    }
    //绘制build
    public static Pixmap drawBuildForSmapByHexagon(Fb2Smap smapDao, AssetManager am,Pixmap pixmap, float scale,int hexagon) {
        //,  List<Color> legionColors, Map regionColors;
        //IntArray coastGrids=smapDao.coastGrids;
        //IntArray ids = smapDao.getIdsByRegionIds(regionIds);
        // IntArray excludeRegions=smapDao.excludeRegions;
        if (smapDao.hexagonDatas != null&& smapDao.buildRDatas.size() > 0&&smapDao.hexagonDatas.get(hexagon).getRegionId()==hexagon&&smapDao.hexagonDatas.get(hexagon).getLegionIndex()>0&&smapDao.buildRDatas.containsKey(hexagon) ) {

            int x = 0;
            int y = 0;

            Pixmap city_1 = am.get(("pixmap/smap/city_1.png"), Pixmap.class);
            Pixmap city_2 = am.get(("pixmap/smap/city_2.png"), Pixmap.class);
            Pixmap city_3 = am.get(("pixmap/smap/city_3.png"), Pixmap.class);
            Pixmap city_4 = am.get(("pixmap/smap/city_3.png"), Pixmap.class);
            Pixmap city_5 = am.get(("pixmap/smap/city_3.png"), Pixmap.class);
            Pixmap city_island = am.get(("pixmap/smap/city_island.png"), Pixmap.class);
            Pixmap city_port = am.get(("pixmap/smap/city_port.png"), Pixmap.class);
            Pixmap drawCity;

            int x_px;
            int y_px;

            //先绘制海岸  //沿海地块用圆形来填充


            pixmap.setBlending(Blending.SourceOver);
            Fb2Smap.BuildData b=smapDao.getBuildDataByRegion(hexagon);

                if(smapDao.capitalLegionsMap.containsKey(b.getRegionId())){//首都
                    drawCity=city_5;
                }else if(b.getBuildType()==2){//海岛
                    drawCity=city_island;
                }else if(b.getBuildType()==1){//港口
                    drawCity=city_port;
                }else if(b.getCityLvNow()>7){//大城市
                    drawCity=city_4;
                }else if(b.getCityLvNow()>5){//大城市
                    drawCity=city_3;
                }else if(b.getCityLvNow()>3){//大城市
                    drawCity=city_2;
                }else{//普通城市
                    drawCity=city_1;
                }
                    x = (hexagon % smapDao.masterData.getWidth()) + 1;
                    y = (hexagon / smapDao.masterData.getWidth()) + 1;

                  //  x_px = (int) ((x - 1) * ResConfig.Map.GRID_WIDTH);
                   // y_px = (int) (((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF));
            x_px=getX_px(x,scale);
            y_px=getY_px(x,y,scale);

                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));

                    pixmap.drawPixmap
                            (drawCity,
                                    0,
                                    0,
                                    drawCity.getWidth(),
                                    drawCity.getHeight(),
                                    x_px,
                                    y_px ,
                                    (int) (scale *  drawCity.getWidth() ) ,
                                    (int) (scale *  drawCity.getHeight() ) );


                    //Gdx.app.log("地图层1构建成功", " backid:" + mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx() + "img:" + imgBack);





            // Gdx.app.log("色图构建", "完成  color:"+color);
            //PixmapIO.writePNG(Gdx.files.external("updateColor_"+ids.toString()+".png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败3:");
            return pixmap;
        }
    }


    public static Pixmap updateCapital(Fb2Smap smapDao, AssetManager am, Pixmap pixmap, float scale, int w, int mapH_px) {

        if(smapDao!=null&&smapDao.legionDatas.size>0){
            Pixmap pointW = am.get(("pixmap/smap/mark_point_w.png"), Pixmap.class);
            Pixmap pointG = am.get(("pixmap/smap/mark_point_g.png"), Pixmap.class);
            Pixmap pointB = am.get(("pixmap/smap/mark_point_b.png"), Pixmap.class);
            Pixmap pointR = am.get(("pixmap/smap/mark_point_r.png"), Pixmap.class);
            pixmap.setBlending(Blending.SourceOver);
            for(Fb2Smap.LegionData l:smapDao.legionDatas){
                if(l.getCapitalId()>=0&&l.getInternIndex()!=0){
                    int x,y;
                    int x_px;
                    int y_px;
                    x = (l.getCapitalId() % smapDao.masterData.getWidth()) + 1;
                    y = (l.getCapitalId() / smapDao.masterData.getWidth()) + 1;

                  //  x_px = (int) ((x - 1) * ResConfig.Map.GRID_WIDTH);
                   // y_px = (int) (((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF));
                    x_px=getX_px(x,scale);
                    y_px=getY_px(x,y,scale);
                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    if(l.getInternIndex()==0){
                        pixmap.drawPixmap
                                (pointW,
                                        0,
                                        0,
                                        pointW.getWidth(),
                                        pointW.getHeight(),
                                        x_px,
                                        y_px,
                                        (int) (scale *  pointW.getWidth() ) ,
                                        (int) (scale *  pointW.getHeight() ) );
                    }else if(l.getLegionIndex()==smapDao.masterData.getPlayerLegionIndex()){
                        pixmap.drawPixmap
                                (pointG,
                                        0,
                                        0,
                                        pointG.getWidth(),
                                        pointG.getHeight(),
                                        x_px,
                                        y_px,
                                        (int) (scale *  pointG.getWidth() ) ,
                                        (int) (scale *  pointG.getHeight() ) );
                    }else if(l.getInternIndex()==smapDao.getPlayerLegionData().getInternIndex()){
                         pixmap.drawPixmap
                                (pointB,
                                        0,
                                        0,
                                        pointB.getWidth(),
                                        pointB.getHeight(),
                                        x_px,
                                        y_px ,
                                        (int) (scale *  pointB.getWidth() ) ,
                                        (int) (scale *  pointB.getHeight() ) );
                    }else {
                        pixmap.drawPixmap
                                (pointR,
                                        0,
                                        0,
                                        pointR.getWidth(),
                                        pointR.getHeight(),
                                        x_px,
                                        y_px ,
                                        (int) (scale *  pointR.getWidth() ) ,
                                        (int) (scale *  pointR.getHeight() ) );
                    }

                }
            }
        }
        return pixmap;
    }


    public static Pixmap drawBuildForSmapByHexagons(Fb2Smap smapDao, AssetManager am,Pixmap pixmap, float scale,IntArray hexagons) {
        //,  List<Color> legionColors, Map regionColors;
        //IntArray coastGrids=smapDao.coastGrids;
        //IntArray ids = smapDao.getIdsByRegionIds(regionIds);
        // IntArray excludeRegions=smapDao.excludeRegions;
        if (smapDao.hexagonDatas != null&& smapDao.buildRDatas.size() > 0&&hexagons.size>0 ) {

            int x = 0;
            int y = 0;

            Pixmap city_1 = am.get(("pixmap/smap/city_1.png"), Pixmap.class);
            Pixmap city_2 = am.get(("pixmap/smap/city_2.png"), Pixmap.class);
            Pixmap city_3 = am.get(("pixmap/smap/city_3.png"), Pixmap.class);
            Pixmap city_island = am.get(("pixmap/smap/city_island.png"), Pixmap.class);
            Pixmap city_port = am.get(("pixmap/smap/city_port.png"), Pixmap.class);
            Pixmap drawCity=null;

            int x_px;
            int y_px;

            //先绘制海岸  //沿海地块用圆形来填充


            pixmap.setBlending(Blending.SourceOver);


            for(int i=0;i<hexagons.size;i++){
                int hexagon=hexagons.get(i);
                if(smapDao.hexagonDatas.get(hexagon).getRegionId()==hexagon&&smapDao.hexagonDatas.get(hexagon).getLegionIndex()>0&&smapDao.buildRDatas.containsKey(hexagon)){
                  //  int i=smapDao.buildDataIndexMap.get(hexagon,0);
                    Fb2Smap.BuildData b=smapDao.getBuildDataByRegion(hexagon);

                    if(smapDao.capitalLegionsMap.containsKey(hexagon)){//首都
                        drawCity=city_3;
                    }else if(b.getBuildType()==2){//海岛
                        drawCity=city_island;
                        continue;
                    }else if(b.getBuildType()==1){//港口
                        drawCity=city_port;
                    }else if(b.getBuildName()>0&&b.getCityLvNow()>2+smapDao.worldData.getWorldAge()){//大城市
                        drawCity=city_2;
                    }else if(smapDao.hexagonDatas.get(hexagon).getBlockType()!=1){//普通城市
                        drawCity=city_1;
                    }else{
                        continue;
                    }
                    x = (hexagon % smapDao.masterData.getWidth()) + 1;
                    y = (hexagon / smapDao.masterData.getWidth()) + 1;

               //     x_px = (int) ((x - 1) * ResConfig.Map.GRID_WIDTH);
                 //   y_px = (int) (((x & 1) == 1 ? (y - 1) * ResConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResConfig.Map.HEXAGON_HEIGHT + ResConfig.Map.HEXAGON_HEIGHT_REF));
                    x_px=getX_px(x,scale);
                    y_px=getY_px(x,y,scale);
                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));

                    pixmap.drawPixmap
                            (drawCity,
                                    0,
                                    0,
                                    drawCity.getWidth(),
                                    drawCity.getHeight(),
                                    x_px,
                                    y_px ,
                                    (int) (scale *  drawCity.getWidth() ) ,
                                    (int) (scale *  drawCity.getHeight() ) );

                }
            }



            //Gdx.app.log("地图层1构建成功", " backid:" + mapBinDAO.getMapbin().get(id).getBackTile() + "_" + mapBinDAO.getMapbin().get(id).getBackIdx() + "img:" + imgBack);





            // Gdx.app.log("色图构建", "完成  color:"+color);
            //PixmapIO.writePNG(Gdx.files.external("updateColor_"+ids.toString()+".png"), pixmap);
            return pixmap;
        } else {
            Gdx.app.error("色图构建", "失败4:");
            return pixmap;
        }
    }


    public static void splitePixmapAndWriteXml(String mapName,Pixmap mapPixmap,Pixmap tempPixmap, XmlWriter xmlWriter ) throws IOException {
        int w = (int) ((mapPixmap.getWidth() / ResDefaultConfig.Map.PT_SIDE) + 1);
        int h = (int) ((mapPixmap.getHeight() / ResDefaultConfig.Map.PT_SIDE) + 1);
        int x = 0;
        int y = 0;
        int x_px;
        int y_px;
        int spliteCount = w * h;
        // Gdx.app.log("地图底图开始构建", "w:" + w + " h:" + h );
        for (int i = 0; i < spliteCount; i++) {
            x = i % w;
            y = (int) (i / w);
            x_px = x * ResDefaultConfig.Map.PT_SIDE;
            y_px = y * ResDefaultConfig.Map.PT_SIDE;
           // Gdx.app.log("地图底图开始构建", " id:" + i + " x:" + x + " y:" + y + "x_px:" + x_px + " y_px:" + y_px);
            //pixmap.drawPixmap(imgPt, x_px, y_px, 0, 0, imgPt.getWidth(), imgPt.getHeight());
            tempPixmap.fill();
            tempPixmap.drawPixmap(mapPixmap,0,0,x_px,y_px,tempPixmap.getWidth(),tempPixmap.getHeight());
            xmlWriter.element("map").attribute("n",i+".png").attribute("x",x_px).attribute("y",y_px).pop();
            PixmapIO.writePNG(Gdx.files.local(ResDefaultConfig.Path.ZipFolderPath+mapName+"/"+i+".png"), tempPixmap);
        }
    }

    public static void splitePixmap(String mapName,Pixmap mapPixmap,Pixmap tempPixmap ) throws IOException {
        int w = (int) ((mapPixmap.getWidth() / ResDefaultConfig.Map.PT_SIDE) + 1);
        int h = (int) ((mapPixmap.getHeight() / ResDefaultConfig.Map.PT_SIDE) + 1);
        int x = 0;
        int y = 0;
        int x_px;
        int y_px;
        int spliteCount = w * h;
        // Gdx.app.log("地图底图开始构建", "w:" + w + " h:" + h );
        for (int i = 0; i < spliteCount; i++) {
            x = i % w;
            y = (int) (i / w);
            x_px = x * ResDefaultConfig.Map.PT_SIDE;
            y_px = y * ResDefaultConfig.Map.PT_SIDE;
            // Gdx.app.log("地图底图开始构建", " id:" + i + " x:" + x + " y:" + y + "x_px:" + x_px + " y_px:" + y_px);
            //pixmap.drawPixmap(imgPt, x_px, y_px, 0, 0, imgPt.getWidth(), imgPt.getHeight());
            tempPixmap.fill();
            tempPixmap.drawPixmap(mapPixmap,0,0,x_px,y_px,tempPixmap.getWidth(),tempPixmap.getHeight());
            PixmapIO.writePNG(Gdx.files.local(ResDefaultConfig.Path.ZipFolderPath+mapName+"/"+i+".png"), tempPixmap);
        }
    }

    public static int getW_px(int w,float scale){
      return  GameMap.getX_px(w,scale)+ (int) (scale * ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE);
    }
    public static int getH_px(int w,int h,float scale){
       return GameMap.getY_px(w,h,scale)+(int) (scale * ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE);
    }

    private static int getX_px(int x,float scale){
        return (int) ((x - 1) * ResDefaultConfig.Map.GRID_WIDTH * ResDefaultConfig.Map.MAP_SCALE*scale);
    }
    private static int getY_px(int x,int y,float scale){
        return (int) (((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF) * ResDefaultConfig.Map.MAP_SCALE*scale);
    }
    public static int getHexagonX1(int x,int y,float scale){
        return (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + 35 * ResDefaultConfig.Map.MAP_SCALE));//左上,左下 原来是36
    }
    private static int getHexagonX2(int x,int y,float scale){
        return (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + 113 * ResDefaultConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
    }
    private static int getHexagonX3(int x,int y,float scale){
        return (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE - 1));//左中

    }
    private static int getHexagonX4(int x,int y,float scale){
        return (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE));//右中
    }
    public static int getHexagonY1(int x,int y,float scale){
        return (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE - 1));//上
    }
    private static int getHexagonY2(int x,int y,float scale){
        return (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));//中
    }
    private static int getHexagonY3(int x,int y,float scale){
        return (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE));//下
    }



    public static float getHexagonX1F(int x,int y,float scale){
        return  (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + 36 * ResDefaultConfig.Map.MAP_SCALE));//左上,左下 原来是36
    }
    private static float getHexagonX2F(int x,int y,float scale){
        return  (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + 114 * ResDefaultConfig.Map.MAP_SCALE));//右上,右下 ResConfig.Map.GRID_WIDTH
    }
    private static float getHexagonX3F(int x,int y,float scale){
        return (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE));//左中

    }
    private static float getHexagonX4F(int x,int y,float scale){
        return  (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE));//右中
    }
    public static float getHexagonY1F(int x,int y,float scale){
        return (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE - 1));//上
    }
    private static float getHexagonY2F(int x,int y,float scale){
        return  (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));//中
    }
    private static float getHexagonY3F(int x,int y,float scale){
        return (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE));//下
    }
    private static float getX_px_CircleF(int x,float scale){
        return  (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
    }
    private static float getY_px_CircleF(int x,int y,float scale){
        return  (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
    }


    private static int getX_px_Circle(int x,float scale){
        return (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
    }
    private static int getY_px_Circle(int x,int y,float scale){
        return (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));
    }


    public static void drawMapRegionByShapeRendererForGameBg(Fb2Smap smapDao, ShapeRenderer shapeRenderer, float scale, IntArray ids, boolean ifDrawSea, int mapW_px, int mapH_px, float alpha) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (smapDao.hexagonDatas != null &&smapDao.legionColors!=null&&smapDao.masterData.getIfColor()==1 && smapDao.hexagonDatas.size > 0) {

            int x = 0;
            int y = 0;
           // float x_px_circle = 0;
            //float y_px_circle = 0;
            float hexgaon_x1;
            float hexgaon_x2;
            float hexgaon_x3;
            float hexgaon_x4;
            float hexgaon_y1;
            float hexgaon_y2;
            float hexgaon_y3;
            int nextRegionId = 0;
            Color color = Color.CLEAR;
            boolean ifDrawIdWarLine;
            //先绘制海岸  //沿海地块用圆形来填充
            //pixmap.setBlending(Blending.None);
            for (int id=0,iMax=ids.size,i=0;id<iMax;id++) {
                i=ids.get(id);
                if (  smapDao.hexagonDatas.get(i).getRegionId() != -1 ) {
                    if(!ifDrawSea&& smapDao.hexagonDatas.get(i).getBlockType() == 1){
                        continue;
                    }
                    //沿海地块用圆形来填充
                    if (smapDao.hexagonDatas.get(i).getRegionId() != nextRegionId) {
                        //获取颜色
                        color=smapDao.getColorForRegion(smapDao.hexagonDatas.get(i).getRegionId());
                        shapeRenderer.setColor(color.r,color.g,color.b,alpha);
                        nextRegionId =smapDao.hexagonDatas.get(i).getRegionId();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    x = (i % smapDao.masterData.getWidth()) + 1;
                    y = (i / smapDao.masterData.getWidth()) + 1;


                    hexgaon_x1 = getHexagonX1F(x,y,scale)+mapW_px;//左上,左下
                    hexgaon_x2 =  getHexagonX2F(x,y,scale)+mapW_px;//右上,右下
                    hexgaon_x3 =  getHexagonX3F(x,y,scale)+mapW_px;//左中
                    hexgaon_x4 =  getHexagonX4F(x,y,scale)+mapW_px;//右中
                    hexgaon_y1 = mapH_px-getHexagonY1F(x,y,scale);//上
                    hexgaon_y2 =   mapH_px-getHexagonY2F(x,y,scale);//中
                    hexgaon_y3 =  mapH_px- getHexagonY3F(x,y,scale);//下
                    //x_px_circle=getX_px_CircleF(x,scale)+mapW_px;
                   // y_px_circle=mapH_px-getY_px_CircleF(x,y,scale);


                   /* if(smapDao.hexagonDatas.get(i).getIfCoast()>0){
                        shapeRenderer.circle(x_px_circle, y_px_circle, (int) ((ResConfig.Map.HEXAGON_WIDTH + 13) * ResConfig.Map.MAP_SCALE / 2 * scale));
                        //pixmap.fillCircle(x_px_circle, y_px_circle, (int) ((ResConfig.Map.HEXAGON_WIDTH + 13) * ResConfig.Map.MAP_SCALE / 2 * scale));
                    }else*/{
                        shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                        shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                        shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                        shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                    }



                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                }
            }

        }
        shapeRenderer.end();
    }


    public static void drawBorderByShapeRendererForGameBg(Fb2Smap smapDao, ShapeRenderer shapeRenderer, CamerDAO cam,float scale,  boolean ifDrawSea, int mapW_px, int mapH_px, float alpha) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (smapDao.hexagonDatas != null &&smapDao.legionColors!=null&&smapDao.masterData.getIfColor()==1 && smapDao.hexagonDatas.size > 0) {

            int x = 0;
            int y = 0;
            // float x_px_circle = 0;
            //float y_px_circle = 0;
            float hexgaon_x1;
            float hexgaon_x2;
            float hexgaon_x3;
            float hexgaon_x4;
            float hexgaon_y1;
            float hexgaon_y2;
            float hexgaon_y3,x_px_circle,y_px_circle;
            int nextRegionId = 0;
            Color color = Color.CLEAR;
            boolean ifDrawIdWarLine;
            int circleLength=(int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale);

            //先绘制海岸  //沿海地块用圆形来填充
            //pixmap.setBlending(Blending.None);
            int policy;
            int i = 0,  j = 0,id,iMax = cam.cw+2,jMax=cam.ch+2;
            for (; j<jMax; j++) {
                for (i=0;i<iMax; i++) {
                    //  x = (i % smapDao.masterData.getWidth()) + 1;
                    //  y = (i / smapDao.masterData.getWidth()) + 1;
                    x=cam.csx+i;
                    if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                        x=x-smapDao.masterData.getWidth();
                    }
                    y=cam.csy+j;
                    id=x-1+(y-1)*smapDao.masterData.getWidth();
                    // Gdx.app.log("setColor","id:"+id+" x:"+x+" y:"+y);

                    /*if(id==49913){
                        Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }*/
                    if ( id>=0&&id<smapDao.hexagonDatas.size ) {
                        Fb2Map.MapHexagon h = smapDao.hexagonDatas.get(id);
                        if (h.getRegionId() != -1) {
                            if (!ifDrawSea && h.getBlockType() == 1) {
                                continue;
                            }
                            //沿海地块用圆形来填充

                            x = (i % smapDao.masterData.getWidth()) + 1;
                            y = (i / smapDao.masterData.getWidth()) + 1;


                            hexgaon_x1 = getHexagonX1F(x, y, scale) + mapW_px;//左上,左下
                            hexgaon_x2 = getHexagonX2F(x, y, scale) + mapW_px;//右上,右下
                            hexgaon_x3 = getHexagonX3F(x, y, scale) + mapW_px;//左中
                            hexgaon_x4 = getHexagonX4F(x, y, scale) + mapW_px;//右中
                            hexgaon_y1 = mapH_px - getHexagonY1F(x, y, scale);//上
                            hexgaon_y2 = mapH_px - getHexagonY2F(x, y, scale);//中
                            hexgaon_y3 = mapH_px - getHexagonY3F(x, y, scale);//下
                            x_px_circle = getX_px_CircleF(x, scale) + mapW_px;
                            y_px_circle = mapH_px - getY_px_CircleF(x, y, scale);


                            //shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                            //shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                            //shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                            //shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);

                            //绘制交界
                            drawCountryBorderByShapeRenderer(smapDao, shapeRenderer, id, h, hexgaon_x1, hexgaon_x2, hexgaon_x3, hexgaon_x4, hexgaon_y1, hexgaon_y2, hexgaon_y3);

                            policy = smapDao.getBuildPolicyByHexagon(id);
                            if (policy != 0) {
                                Color c = DefDAO.getColorForBuildPolicy(policy);
                                shapeRenderer.setColor(c.r, c.g, c.b, 0.1f);
                                shapeRenderer.circle(x_px_circle, y_px_circle, circleLength);
                                // shapeRenderer.line(hexgaon_x1,hexgaon_y1,hexgaon_x2,hexgaon_y1 );
                                // shapeRenderer.line(hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2 );
                                // shapeRenderer.line(hexgaon_x4,hexgaon_y2,hexgaon_x2,hexgaon_y3 );
                                // shapeRenderer.line(hexgaon_x2,hexgaon_y3,hexgaon_x1,hexgaon_y3 );
                                // shapeRenderer.line(hexgaon_x1,hexgaon_y3,hexgaon_x3,hexgaon_y2 );
                                // shapeRenderer.line(hexgaon_x3,hexgaon_y2,hexgaon_x1,hexgaon_y1 );
                                shapeRenderer.setColor(color);
                            }


                            //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                        }
                    }}
            }

        }
        shapeRenderer.end();
    }

    public static void drawLegionMapRegionForMapEditByCamShapeRenderer(Fb2Map mapBinDAO, ShapeRenderer shapeRenderer, DrawGridListDAO drawGridListDAO, float scale, Boolean ifDrawSea, int mapW_px, int mapH_px) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (mapBinDAO!=null) {

            int x = 0;
            int y = 0;
            float x_px_circle = 0;
            float y_px_circle = 0;
            float hexgaon_x1;
            float hexgaon_x2;
            float hexgaon_x3;
            float hexgaon_x4;
            float hexgaon_y1;
            float hexgaon_y2;
            float hexgaon_y3;
            float gridH= ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2*scale;
            float gridW=13 * ResDefaultConfig.Map.MAP_SCALE;

            int nextRegionId = 0;
            Color color = Color.CLEAR;
            int circleLength=(int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale);


            //先绘制海岸  //沿海地块用圆形来填充
            //pixmap.setBlending(Blending.None);
            //  Gdx.app.log("setColor1","begin"+" ");
           int i,id,iMax;
                for (i=0,iMax=drawGridListDAO.size();i<iMax; i++) {


                    id=drawGridListDAO.get(i).getDraw_gd();
                    // Gdx.app.log("setColor","id:"+id+" x:"+x+" y:"+y);

                    /*if(id==49913){
                        Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }*/
                    if ( id>=0&&id<mapBinDAO.mapHexagons.size ) {
                        Fb2Map.MapHexagon m=mapBinDAO.mapHexagons.get(id);
                        if ( m.getRegionId() == -1 ) {
                            continue;
                        }
                        if(!ifDrawSea&&m.getBlockType() == 1){
                            continue;
                        }
                        //Gdx.app.log("海陆绘制圆", "" + id);
                        //沿海地块用圆形来填充
                        if (m.getRegionId() != nextRegionId) {
                            if (m.getRegionId() != -1) {
                                color=new Color(GameUtil.getColorByNum(m.getRegionId()));;
                            } else {
                                color = Color.CLEAR;
                            }
                            //获取颜色
                            shapeRenderer.setColor(color);
                            nextRegionId =m.getRegionId();
                            //Gdx.app.log("setColor","id:"+id+" color:"+color);
                        }

                        hexgaon_x1 = getHexagonX1F(x,y,scale)+mapW_px;//左上,左下
                        hexgaon_x2 =  getHexagonX2F(x,y,scale)+mapW_px;//右上,右下
                        hexgaon_x3 =  getHexagonX3F(x,y,scale)+mapW_px;//左中
                        hexgaon_x4 =  getHexagonX4F(x,y,scale)+mapW_px;//右中
                        hexgaon_y1 = mapH_px-getHexagonY1F(x,y,scale);//上
                        hexgaon_y2 =   mapH_px-getHexagonY2F(x,y,scale);//中
                        hexgaon_y3 =  mapH_px- getHexagonY3F(x,y,scale);//下
                        x_px_circle=getX_px_CircleF(x,scale)+mapW_px;
                        y_px_circle=mapH_px-getY_px_CircleF(x,y,scale);

                    /*if(id==37699){
                        Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }*/
                        //Fb2Smap smapDao=null;
                        //1111
                        if(m.getIfCoast()>0){
                            // shapeRenderer.circle(x_px_circle, y_px_circle, (int) ((ResConfig.Map.HEXAGON_WIDTH + 13) * ResConfig.Map.MAP_SCALE / 2 * scale));
                            drawCoastByShapeRenderer(mapBinDAO,shapeRenderer,id,m.getIfCoast(),x_px_circle, y_px_circle,hexgaon_x1,hexgaon_x2,hexgaon_x3,hexgaon_x4,hexgaon_y1,hexgaon_y2,hexgaon_y3,circleLength,scale,gridW,gridH);
                        }else{
                            //shapeRenderer.setColor(Color.RED);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                            //  shapeRenderer.setColor(Color.WHITE);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                            // shapeRenderer.setColor(Color.BLUE);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                            //   shapeRenderer.setColor(Color.YELLOW);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                        }
                        //绘制交界1111
                        drawCountryBorderByShapeRenderer(mapBinDAO,shapeRenderer,id,null,hexgaon_x1,hexgaon_x2,hexgaon_x3,hexgaon_x4,hexgaon_y1,hexgaon_y2,hexgaon_y3);


                        //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    }
                }


            //    Gdx.app.log("setColor1","end");
        }
        shapeRenderer.end();
    }

    public static void  drawAllMapRegionByShapeRenderer(Fb2Smap smapDao,ShapeRenderer shapeRenderer, float scale,Boolean ifDrawSea,int mapW_px,int mapH_px) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (smapDao.hexagonDatas != null &&smapDao.legionColors!=null&&smapDao.masterData.getIfColor()==1 && smapDao.hexagonDatas.size > 0) {

            int x = 0;
            int y = 0;
            int x_px_circle = 0;
            int y_px_circle = 0;
            int hexgaon_x1;
            int hexgaon_x2;
            int hexgaon_x3;
            int hexgaon_x4;
            int hexgaon_y1;
            int hexgaon_y2;
            int hexgaon_y3;
            int nextRegionId = 0;
            Color color = Color.CLEAR;

            //先绘制海岸  //沿海地块用圆形来填充
            //pixmap.setBlending(Blending.None);
            for (int i = 0, iMax = smapDao.hexagonDatas.size; i<iMax; i++) {
                if ( smapDao.hexagonDatas.get(i).getRegionId() != -1 ) {
                    if(!ifDrawSea&& smapDao.hexagonDatas.get(i).getBlockType() == 1){
                        continue;
                    }
                    //Gdx.app.log("海陆绘制圆", "" + id);
                    //沿海地块用圆形来填充
                    if (smapDao.hexagonDatas.get(i).getRegionId() != nextRegionId) {
                        if (smapDao.hexagonDatas.get(i).getRegionId() != -1) {
                           color=smapDao.getColorForRegion(smapDao.hexagonDatas.get(i).getRegionId());
                        } else {
                            color = Color.CLEAR;
                        }
                        //获取颜色
                        shapeRenderer.setColor(color);
                        nextRegionId =smapDao.hexagonDatas.get(i).getRegionId();
                        //Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }
                    x = (i % smapDao.masterData.getWidth()) + 1;
                    y = (i / smapDao.masterData.getWidth()) + 1;
                    hexgaon_x1 = getHexagonX1(x,y,scale)+mapW_px;//左上,左下
                    hexgaon_x2 =  getHexagonX2(x,y,scale)+mapW_px;//右上,右下
                    hexgaon_x3 =  getHexagonX3(x,y,scale)+mapW_px;//左中
                    hexgaon_x4 =  getHexagonX4(x,y,scale)+mapW_px;//右中
                    hexgaon_y1 = mapH_px-getHexagonY1(x,y,scale);//上
                    hexgaon_y2 =   mapH_px-getHexagonY2(x,y,scale);//中
                    hexgaon_y3 =  mapH_px- getHexagonY3(x,y,scale);//下
                    x_px_circle=getX_px_Circle(x,scale)+mapW_px;
                    y_px_circle=mapH_px-getY_px_Circle(x,y,scale);

                    if(smapDao.hexagonDatas.get(i).getIfCoast()>0){
                        shapeRenderer.circle(x_px_circle, y_px_circle, (int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale));

                    }else{
                        shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                        shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                        shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                        shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3); }
                    //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                }
            }
        }
        shapeRenderer.end();
    }


    private static void drawCountryBorderByShapeRenderer(Fb2Smap smapDao, ShapeRenderer shapeRenderer, int id, Fb2Map.MapHexagon h, float hexgaon_x1, float hexgaon_x2, float hexgaon_x3, float hexgaon_x4, float hexgaon_y1, float hexgaon_y2, float hexgaon_y3) {

        //Color c=shapeRenderer.getColor();
        // shapeRenderer.setColor(Color.BLACK);

        if(smapDao.ifCountryBorder(id,1,false)){
            shapeRenderer.rectLine(hexgaon_x1,hexgaon_y1,hexgaon_x3,hexgaon_y2,3,Color.DARK_GRAY,Color.DARK_GRAY);
        }
        if(smapDao.ifCountryBorder(id,2,false)){
            shapeRenderer.rectLine(hexgaon_x1,hexgaon_y1,hexgaon_x2,hexgaon_y1,3,Color.DARK_GRAY,Color.DARK_GRAY);
        }
        if(smapDao.ifCountryBorder(id,3,false)){
            shapeRenderer.rectLine(hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2,3,Color.DARK_GRAY,Color.DARK_GRAY);
        }
        // shapeRenderer.setColor(c);
    }


    //绘制交界处
    private static void drawCountryBorderByShapeRenderer(Fb2Map smapDao, ShapeRenderer shapeRenderer, int id, Fb2Map.MapHexagon h, float hexgaon_x1, float hexgaon_x2, float hexgaon_x3, float hexgaon_x4, float hexgaon_y1, float hexgaon_y2, float hexgaon_y3) {

       //Color c=shapeRenderer.getColor();
       // shapeRenderer.setColor(Color.BLACK);

        if(smapDao.ifRegionBorder(id,1)){
            shapeRenderer.rectLine(hexgaon_x1,hexgaon_y1,hexgaon_x3,hexgaon_y2,3,Color.DARK_GRAY,Color.DARK_GRAY);
        }
        if(smapDao.ifRegionBorder(id,2)){
            shapeRenderer.rectLine(hexgaon_x1,hexgaon_y1,hexgaon_x2,hexgaon_y1,3,Color.DARK_GRAY,Color.DARK_GRAY);
        }
        if(smapDao.ifRegionBorder(id,3)){
            shapeRenderer.rectLine(hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2,3,Color.DARK_GRAY,Color.DARK_GRAY);
        }
       // shapeRenderer.setColor(c);
    }
    private static void drawCoastByShapeRenderer(Fb2Map smapDao, ShapeRenderer shapeRenderer, int id, int coastBorderId, float x_px_circle, float y_px_circle, float hexgaon_x1, float hexgaon_x2, float hexgaon_x3, float hexgaon_x4, float hexgaon_y1, float hexgaon_y2, float hexgaon_y3, int circleRadius, float scale, float gridW, float gridH) {
       /*if(id==44877){
            int i1=1;
        } */

        if(coastBorderId==63){//是111111绘制圆形
            shapeRenderer.circle(x_px_circle, y_px_circle, circleRadius);
        }else {
            //1.coastType 转换  cId垂直方向的id  bId斜方向的id
            String rs=GameMethod.transFBorderIdForString(coastBorderId);
            int luId,uId,ruId,ldId,dId,rdId;
            luId=smapDao.getBorderIdByDirect(id,1);
            uId=smapDao.getBorderIdByDirect(id,2);
            ruId=smapDao.getBorderIdByDirect(id,3);
            ldId=smapDao.getBorderIdByDirect(id,4);
            dId=smapDao.getBorderIdByDirect(id,5);
            rdId=smapDao.getBorderIdByDirect(id,6);
            boolean a1,a2;
            //2.解析绘制, 0:三角形,1:扇形
            if(rs.substring(0,1).equals(ResDefaultConfig.StringName.zero)){
                shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x3, hexgaon_y2);
            }else {
                //如果左上角是海,并且上方也是coast 则左上绘制梯形
                if(smapDao.ifSea(luId)&&smapDao.getCoast(uId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x3, hexgaon_y2);
                    //判断
                    shapeRenderer.triangle(hexgaon_x3, hexgaon_y1, hexgaon_x1, hexgaon_y1, hexgaon_x3, hexgaon_y2);

                    if(!smapDao.ifSea(ldId)){
                        shapeRenderer.triangle(hexgaon_x3-gridW, hexgaon_y2, hexgaon_x3, hexgaon_y1-gridH/2, hexgaon_x3, hexgaon_y2);
                    }

                    //id判断斜上方向是否是coast,否则绘制扇形,是则绘制三角
                }else if(smapDao.getCoast(ldId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x3, hexgaon_y2);
                }else {
                    shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,120,60);
                }
            }
            if(rs.substring(1,2).equals(ResDefaultConfig.StringName.zero)){
                shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
            }else {
                //斜边补角
                if (smapDao.getCoast(luId)>0||smapDao.getCoast(ruId)>0) {
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                    //判断斜边是否是梯形,如果斜边是梯形,则不用补角
                    if(!smapDao.ifDrawTrapezoidForDirect(luId,3)){
                        if(smapDao.getCoast(luId)>0){
                            shapeRenderer.triangle(x_px_circle,hexgaon_y1,hexgaon_x1,hexgaon_y1,hexgaon_x3,hexgaon_y1+gridH);
                        }
                    }
                    if(!smapDao.ifDrawTrapezoidForDirect(ruId,1)){
                        if(smapDao.getCoast(ruId)>0){
                            shapeRenderer.triangle(x_px_circle,hexgaon_y1,hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y1+gridH);
                        }
                    }

                }else {
                    shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,60,60);
                }
            }
            if(rs.substring(2,3).equals(ResDefaultConfig.StringName.zero)){
                shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
            }else {
                //如果右上角是海,并且上方也是coast 则左上绘制梯形
                if(smapDao.ifSea(ruId)&&smapDao.getCoast(uId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                    shapeRenderer.triangle(hexgaon_x4, hexgaon_y1, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);

                    if(!smapDao.ifSea(rdId)){
                        shapeRenderer.triangle(hexgaon_x4,hexgaon_y1-gridH/2,hexgaon_x4,hexgaon_y2,hexgaon_x4+gridW*3,hexgaon_y2);
                    }
                }else if(smapDao.getCoast(rdId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                }else {
                    shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,0,60);
                }
            }

            if(rs.substring(3,4).equals(ResDefaultConfig.StringName.zero)){
                shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x3, hexgaon_y2);
            }else {
                //如果左下角是海,并且下方也是coast 则左下绘制梯形
                if(smapDao.ifSea(ldId)&&smapDao.getCoast(dId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x3, hexgaon_y2);
                    shapeRenderer.triangle(hexgaon_x3, hexgaon_y3, hexgaon_x1, hexgaon_y3, hexgaon_x3, hexgaon_y2);
                    if(!smapDao.ifSea(luId)){
                        shapeRenderer.triangle(hexgaon_x3-gridW*3,hexgaon_y2,hexgaon_x3,hexgaon_y2,hexgaon_x3,hexgaon_y2-gridH/2);
                    }

                }else if(smapDao.getCoast(luId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x3, hexgaon_y2);
                }else {
                    shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,180,60);
                }
            }
            if(rs.substring(4,5).equals(ResDefaultConfig.StringName.zero)){
                shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
            }else {
                if (smapDao.getCoast(ldId)>0||smapDao.getCoast(rdId)>0) {
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                    a1=smapDao.ifDrawTrapezoidForDirect(ldId,6);
                    a2=smapDao.ifDrawTrapezoidForDirect(rdId,4);


                    if(!a1&&!a2&&smapDao.getCoast(ldId)>0&&smapDao.getCoast(rdId)>0){
                        shapeRenderer.triangle(x_px_circle,hexgaon_y3,hexgaon_x1,hexgaon_y3,hexgaon_x3,hexgaon_y3-gridH);
                        shapeRenderer.triangle(x_px_circle,hexgaon_y3,hexgaon_x2,hexgaon_y3,hexgaon_x4,hexgaon_y3-gridH);
                    }else  if(!a1&&smapDao.getCoast(ldId)>0){
                        shapeRenderer.triangle(hexgaon_x2,hexgaon_y3,hexgaon_x1,hexgaon_y3,hexgaon_x3,hexgaon_y3-gridH);
                    }else if(!a2&&smapDao.getCoast(rdId)>0){
                        shapeRenderer.triangle(hexgaon_x1,hexgaon_y3,hexgaon_x2,hexgaon_y3,hexgaon_x4,hexgaon_y3-gridH);
                    }



                }else {
                    shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,240,60);
                }
            }
            if(rs.substring(5,6).equals(ResDefaultConfig.StringName.zero)){
                shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
            }else {
                //如果右下角是海,并且下方也是coast 则右下绘制梯形
                if(smapDao.ifSea(rdId)&&smapDao.getCoast(dId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                    shapeRenderer.triangle(hexgaon_x4, hexgaon_y3, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                    if(!smapDao.ifSea(ruId)){
                        shapeRenderer.triangle(hexgaon_x4, hexgaon_y2,hexgaon_x4, hexgaon_y3+gridH/2,hexgaon_x4+gridW*3, hexgaon_y2);
                    }

                }else if(smapDao.getCoast(ruId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                }else {
                    shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,300,60);
                }
            }
        }
    }


    //绘制海岸  1
    private static void drawCoastByShapeRenderer(Fb2Smap smapDao,ShapeRenderer shapeRenderer,int id, int coastBorderId, float x_px_circle, float y_px_circle, float hexgaon_x1, float hexgaon_x2, float hexgaon_x3, float hexgaon_x4, float hexgaon_y1, float hexgaon_y2, float hexgaon_y3,int circleRadius,float scale,float gridW,float gridH) {
       /*if(id==44877){
            int i1=1;
        } */

       if(coastBorderId==63){//是111111绘制圆形
           shapeRenderer.circle(x_px_circle, y_px_circle, circleRadius);
       }else {
           //1.coastType 转换  cId垂直方向的id  bId斜方向的id
           String rs=GameMethod.transFBorderIdForString(coastBorderId);
           int luId,uId,ruId,ldId,dId,rdId;
           luId=smapDao.getBorderIdByDirect(id,1);
           uId=smapDao.getBorderIdByDirect(id,2);
           ruId=smapDao.getBorderIdByDirect(id,3);
           ldId=smapDao.getBorderIdByDirect(id,4);
           dId=smapDao.getBorderIdByDirect(id,5);
           rdId=smapDao.getBorderIdByDirect(id,6);
           boolean a1,a2;
           //2.解析绘制, 0:三角形,1:扇形
           if(rs.substring(0,1).equals(ResDefaultConfig.StringName.zero)){
               shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x3, hexgaon_y2);
           }else {
               //如果左上角是海,并且上方也是coast 则左上绘制梯形
               if(smapDao.ifSea(luId)&&smapDao.getCoast(uId)>0){
                    shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x3, hexgaon_y2);
                  //判断
                   shapeRenderer.triangle(hexgaon_x3, hexgaon_y1, hexgaon_x1, hexgaon_y1, hexgaon_x3, hexgaon_y2);

                   if(!smapDao.ifSea(ldId)){
                        shapeRenderer.triangle(hexgaon_x3-gridW, hexgaon_y2, hexgaon_x3, hexgaon_y1-gridH/2, hexgaon_x3, hexgaon_y2);
                  }

               //id判断斜上方向是否是coast,否则绘制扇形,是则绘制三角
                }else if(smapDao.getCoast(ldId)>0){
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x3, hexgaon_y2);
               }else {
                    shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,120,60);
                }
           }
           if(rs.substring(1,2).equals(ResDefaultConfig.StringName.zero)){
               shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
           }else {
               //斜边补角
               if (smapDao.getCoast(luId)>0||smapDao.getCoast(ruId)>0) {
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                   //判断斜边是否是梯形,如果斜边是梯形,则不用补角
                if(!smapDao.ifDrawTrapezoidForDirect(luId,3)){
                    if(smapDao.getCoast(luId)>0){
                        shapeRenderer.triangle(x_px_circle,hexgaon_y1,hexgaon_x1,hexgaon_y1,hexgaon_x3,hexgaon_y1+gridH);
                    }
                }
                   if(!smapDao.ifDrawTrapezoidForDirect(ruId,1)){
                       if(smapDao.getCoast(ruId)>0){
                           shapeRenderer.triangle(x_px_circle,hexgaon_y1,hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y1+gridH);
                       }
                   }

               }else {
                   shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,60,60);
               }
           }
           if(rs.substring(2,3).equals(ResDefaultConfig.StringName.zero)){
               shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
           }else {
               //如果右上角是海,并且上方也是coast 则左上绘制梯形
               if(smapDao.ifSea(ruId)&&smapDao.getCoast(uId)>0){
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                   shapeRenderer.triangle(hexgaon_x4, hexgaon_y1, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);

                   if(!smapDao.ifSea(rdId)){
                       shapeRenderer.triangle(hexgaon_x4,hexgaon_y1-gridH/2,hexgaon_x4,hexgaon_y2,hexgaon_x4+gridW*3,hexgaon_y2);
                   }
               }else if(smapDao.getCoast(rdId)>0){
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
               }else {
                   shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,0,60);
               }
           }

           if(rs.substring(3,4).equals(ResDefaultConfig.StringName.zero)){
               shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x3, hexgaon_y2);
           }else {
               //如果左下角是海,并且下方也是coast 则左下绘制梯形
               if(smapDao.ifSea(ldId)&&smapDao.getCoast(dId)>0){
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x3, hexgaon_y2);
                   shapeRenderer.triangle(hexgaon_x3, hexgaon_y3, hexgaon_x1, hexgaon_y3, hexgaon_x3, hexgaon_y2);
                   if(!smapDao.ifSea(luId)){
                       shapeRenderer.triangle(hexgaon_x3-gridW*3,hexgaon_y2,hexgaon_x3,hexgaon_y2,hexgaon_x3,hexgaon_y2-gridH/2);
                   }

               }else if(smapDao.getCoast(luId)>0){
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x3, hexgaon_y2);
               }else {
                   shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,180,60);
               }
           }
           if(rs.substring(4,5).equals(ResDefaultConfig.StringName.zero)){
               shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
           }else {
               if (smapDao.getCoast(ldId)>0||smapDao.getCoast(rdId)>0) {
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                   a1=smapDao.ifDrawTrapezoidForDirect(ldId,6);
                   a2=smapDao.ifDrawTrapezoidForDirect(rdId,4);


                   if(!a1&&!a2&&smapDao.getCoast(ldId)>0&&smapDao.getCoast(rdId)>0){
                       shapeRenderer.triangle(x_px_circle,hexgaon_y3,hexgaon_x1,hexgaon_y3,hexgaon_x3,hexgaon_y3-gridH);
                       shapeRenderer.triangle(x_px_circle,hexgaon_y3,hexgaon_x2,hexgaon_y3,hexgaon_x4,hexgaon_y3-gridH);
                   }else  if(!a1&&smapDao.getCoast(ldId)>0){
                       shapeRenderer.triangle(hexgaon_x2,hexgaon_y3,hexgaon_x1,hexgaon_y3,hexgaon_x3,hexgaon_y3-gridH);
                   }else if(!a2&&smapDao.getCoast(rdId)>0){
                       shapeRenderer.triangle(hexgaon_x1,hexgaon_y3,hexgaon_x2,hexgaon_y3,hexgaon_x4,hexgaon_y3-gridH);
                   }



               }else {
                   shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,240,60);
               }
           }
           if(rs.substring(5,6).equals(ResDefaultConfig.StringName.zero)){
               shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
           }else {
               //如果右下角是海,并且下方也是coast 则右下绘制梯形
               if(smapDao.ifSea(rdId)&&smapDao.getCoast(dId)>0){
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                   shapeRenderer.triangle(hexgaon_x4, hexgaon_y3, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                   if(!smapDao.ifSea(ruId)){
                       shapeRenderer.triangle(hexgaon_x4, hexgaon_y2,hexgaon_x4, hexgaon_y3+gridH/2,hexgaon_x4+gridW*3, hexgaon_y2);
                   }

               }else if(smapDao.getCoast(ruId)>0){
                   shapeRenderer.triangle(x_px_circle, y_px_circle, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
               }else {
                   shapeRenderer.arc(x_px_circle, y_px_circle,circleRadius,300,60);
               }
           }
       }
    }



     public static void drawTileForMap(Batch batch,Fb2Smap smapDao,  CamerDAO cam, float scale,  int mapW_px, int mapH_px) {

        if (smapDao.hexagonDatas != null && smapDao.hexagonDatas.size > 0) {
            //smapDao.setHexagonDataIfDrawIsFalse();
            int x = 0;
            int y = 0;
            float hexgaon_x1;
            float hexgaon_y1;
            //先绘制海岸  //沿海地块用圆形来填充
            //  Gdx.app.log("setColor1","begin"+" ");
            int i = 0,  j = 0,id,iMax = cam.cw+2,jMax=cam.ch+3;
            for (; j<jMax; j++) {
                for (i=0;i<iMax; i++) {

                    x=cam.csx+i;
                    if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                        x=x-smapDao.masterData.getWidth();
                    }
                    y=cam.csy+j;
                    id=x-1+(y-1)*smapDao.masterData.getWidth();

                    if ( id>=0&&id<smapDao.hexagonDatas.size ) {
                        Fb2Map.MapHexagon h=smapDao.hexagonDatas.get(id);
                       // h.setIfDraw(true);
                        //Gdx.app.log("海陆绘制圆", "" + id);
                        if(h.getTile1()!=null){
                            batch.draw(h.getTile1().getTextureRegion(),
                                    h.source_x+mapW_px+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.getTile1().getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.getTile1().getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.getTile1().getW(),h.getTile1().getH(),1,1,0);
                        }
                        if(h.getTile2()!=null){
                            batch.draw(h.getTile2().getTextureRegion(),
                                    h.source_x+mapW_px+h.getForeRefX()* ResDefaultConfig.Map.MAP_SCALE-h.getTile2().getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y+h.getForeRefY()* ResDefaultConfig.Map.MAP_SCALE-h.getTile2().getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.getTile2().getW(),h.getTile2().getH(),1,1,0);
                        }

                        //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    }
                }
            }


            //    Gdx.app.log("setColor1","end");
        }
    }

    public static void drawBuildNameForMap(MainGame game,Batch batch,Fb2Smap smapDao,  CamerDAO cam) {
        /*if(cam.getZoom()>1.3f){
           return;
        }*/
        if (smapDao.hexagonDatas != null && smapDao.hexagonDatas.size > 0) {
            //smapDao.setHexagonDataIfDrawIsFalse();
            int x = 0;
            int y = 0;
            float hexgaon_x1;
            float hexgaon_y1;
            //先绘制海岸  //沿海地块用圆形来填充
            //  Gdx.app.log("setColor1","begin"+" ");
            int i = 0,  j = 0,id,iMax = cam.cw+2,jMax=cam.ch+3;
            float s=cam.getZoom()-0.5f;
            if(s<=0.4f){s=0.4f;}
            game.gameConfig.gameFont.getData().setScale(s);
            for (; j<jMax; j++) {
                for (i=0;i<iMax; i++) {

                    x=cam.csx+i;
                    if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                        x=x-smapDao.masterData.getWidth();
                    }
                    y=cam.csy+j;
                    //id=x-1+(y-1)*smapDao.masterData.getWidth();
                    id=getId(x-1,y-1,smapDao.masterData.getWidth());
                    if ( id>=0&&id<smapDao.hexagonDatas.size ) {
                        Fb2Map.MapHexagon h=smapDao.hexagonDatas.get(id);
                       /* if(h.getHexagonIndex()==1859){
                            int s2=0;
                        }*/
                        if( h.buildData!=null&&h.buildData.getCityLvNow()>smapDao.getAge()&&h.buildData.getBuildName()>0&&h.buildData.areaName!=null){
                            if(cam.loopState==0||cam.loopState==1){
                                    game.gameConfig.gameFont.draw(batch,h.buildData.areaName, h.source_x-15 , h.source_y-35,30, Align.center,false);
                            }
                            if(cam.loopState==1||cam.loopState==2){
                                    game.gameConfig.gameFont.draw(batch,h.buildData.areaName, cam.getMapW_px()+h.source_x -15, h.source_y-35,30, Align.center,false);
                            }
                        } }
                }
            }

            game.gameConfig.gameFont.getData().setScale(1f);

            //    Gdx.app.log("setColor1","end");
        }
    }


    public static void drawLegionMapRegionForGameBgByCamShapeRenderer(Fb2Smap smapDao, ShapeRenderer shapeRenderer, CamerDAO cam, float scale, Boolean ifDrawSea, int mapW_px, int mapH_px, int potionHexagon, int potionRange) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (smapDao.hexagonDatas != null &&smapDao.legionColors!=null&&smapDao.masterData.getIfColor()==1 && smapDao.hexagonDatas.size > 0) {

            int x = 0;
            int y = 0;
            float x_px_circle = 0;
            float y_px_circle = 0;
            float hexgaon_x1;
            float hexgaon_x2;
            float hexgaon_x3;
            float hexgaon_x4;
            float hexgaon_y1;
            float hexgaon_y2;
            float hexgaon_y3;
            int policy=0;
            float gridH= ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2*scale;
            float gridW=13 * ResDefaultConfig.Map.MAP_SCALE;

            int nextRegionId = 0;
            Color color = Color.CLEAR;
            int circleLength=(int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale);


            //先绘制海岸  //沿海地块用圆形来填充
            //pixmap.setBlending(Blending.None);
            //  Gdx.app.log("setColor1","begin"+" ");
            int i = 0,  j = 0,id,iMax = cam.cw+2,jMax=cam.ch+2;
            for (; j<jMax; j++) {
                for (i=0;i<iMax; i++) {
                    //  x = (i % smapDao.masterData.getWidth()) + 1;
                    //  y = (i / smapDao.masterData.getWidth()) + 1;
                    x=cam.csx+i;
                    if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                        x=x-smapDao.masterData.getWidth();
                    }
                    y=cam.csy+j;
                    id=x-1+(y-1)*smapDao.masterData.getWidth();
                    // Gdx.app.log("setColor","id:"+id+" x:"+x+" y:"+y);

                    /*if(id==49913){
                        Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }*/
                    if ( id>=0&&id<smapDao.hexagonDatas.size ) {
                        Fb2Map.MapHexagon h=smapDao.hexagonDatas.get(id);
                        if ( h.getRegionId() == -1 ) {
                            continue;
                        }
                        if(!ifDrawSea&&h.getBlockType() == 1){
                            continue;
                        }
                        //Gdx.app.log("海陆绘制圆", "" + id);
                        //沿海地块用圆形来填充
                        if (h.getRegionId() != nextRegionId) {
                            if (h.getRegionId() != -1) {
                                color=smapDao.getColorForRegion(h.getRegionId());
                            } else {
                                color = Color.CLEAR;
                            }
                            //获取颜色
                            shapeRenderer.setColor(color);
                            nextRegionId =h.getRegionId();
                            //Gdx.app.log("setColor","id:"+id+" color:"+color);
                        }

                        hexgaon_x1 = getHexagonX1F(x,y,scale)+mapW_px;//左上,左下
                        hexgaon_x2 =  getHexagonX2F(x,y,scale)+mapW_px;//右上,右下
                        hexgaon_x3 =  getHexagonX3F(x,y,scale)+mapW_px;//左中
                        hexgaon_x4 =  getHexagonX4F(x,y,scale)+mapW_px;//右中
                        hexgaon_y1 = mapH_px-getHexagonY1F(x,y,scale);//上
                        hexgaon_y2 =   mapH_px-getHexagonY2F(x,y,scale);//中
                        hexgaon_y3 =  mapH_px- getHexagonY3F(x,y,scale);//下
                        x_px_circle=getX_px_CircleF(x,scale)+mapW_px;
                        y_px_circle=mapH_px-getY_px_CircleF(x,y,scale);

                    /*if(id==37699){
                        Gdx.app.log("setColor","id:"+id+" color:"+color);
                    }*/


                        if(h.getIfCoast()>0){
                            // shapeRenderer.circle(x_px_circle, y_px_circle, (int) ((ResConfig.Map.HEXAGON_WIDTH + 13) * ResConfig.Map.MAP_SCALE / 2 * scale));
                            drawCoastByShapeRenderer(smapDao,shapeRenderer,id,h.getIfCoast(),x_px_circle, y_px_circle,hexgaon_x1,hexgaon_x2,hexgaon_x3,hexgaon_x4,hexgaon_y1,hexgaon_y2,hexgaon_y3,circleLength,scale,gridW,gridH);
                        }else{
                            //shapeRenderer.setColor(Color.RED);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                            //  shapeRenderer.setColor(Color.WHITE);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                            // shapeRenderer.setColor(Color.BLUE);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                            //   shapeRenderer.setColor(Color.YELLOW);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                        }
                        //绘制交界
                        drawCountryBorderByShapeRenderer(smapDao,shapeRenderer,id,h,hexgaon_x1,hexgaon_x2,hexgaon_x3,hexgaon_x4,hexgaon_y1,hexgaon_y2,hexgaon_y3);

                        policy=smapDao.getBuildPolicyByHexagon(id);
                        if(policy!=0){
                            Color c=DefDAO.getColorForBuildPolicy(policy);
                            shapeRenderer.setColor(c.r,c.g,c.b,0.1f);
                            shapeRenderer.circle(x_px_circle, y_px_circle,circleLength);
                            // shapeRenderer.line(hexgaon_x1,hexgaon_y1,hexgaon_x2,hexgaon_y1 );
                            // shapeRenderer.line(hexgaon_x2,hexgaon_y1,hexgaon_x4,hexgaon_y2 );
                            // shapeRenderer.line(hexgaon_x4,hexgaon_y2,hexgaon_x2,hexgaon_y3 );
                            // shapeRenderer.line(hexgaon_x2,hexgaon_y3,hexgaon_x1,hexgaon_y3 );
                            // shapeRenderer.line(hexgaon_x1,hexgaon_y3,hexgaon_x3,hexgaon_y2 );
                            // shapeRenderer.line(hexgaon_x3,hexgaon_y2,hexgaon_x1,hexgaon_y1 );
                            shapeRenderer.setColor(color);
                        }
                        //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    }
                }
            }

            if(potionHexagon!=-1&&potionRange>0){
                shapeRenderer.setColor(Color.GRAY.r,Color.GRAY.g,Color.GRAY.b,0.3f);
                  x = (potionHexagon % smapDao.masterData.getWidth()) + 1;
                  y = (potionHexagon / smapDao.masterData.getWidth()) + 1;
                x_px_circle=getX_px_CircleF(x,scale)+mapW_px;
                y_px_circle=mapH_px-getY_px_CircleF(x,y,scale);
                shapeRenderer.circle(x_px_circle,y_px_circle,circleLength*potionRange);
            }
            //    Gdx.app.log("setColor1","end");
        }
        shapeRenderer.end();
    }


    //绘制外交关系地图
    public static void  drawCampMapRegionByCamShapeRenderer(Fb2Smap smapDao, ShapeRenderer shapeRenderer, CamerDAO cam, float scale, Boolean ifDrawSea, int mapW_px, int mapH_px,int potionHexagon,int potionRange) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (smapDao.hexagonDatas != null &&smapDao.legionColors!=null&&smapDao.masterData.getIfColor()==1 && smapDao.hexagonDatas.size > 0) {

            int x = 0;
            int y = 0;
            float x_px_circle = 0;
            float y_px_circle = 0;
            float hexgaon_x1;
            float hexgaon_x2;
            float hexgaon_x3;
            float hexgaon_x4;
            float hexgaon_y1;
            float hexgaon_y2;
            float hexgaon_y3;
            float gridH= ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2*scale;
            float gridW=13 * ResDefaultConfig.Map.MAP_SCALE;
            int nowCamp=-2;
            int nextCamp = -2;
            Color color = Color.CLEAR;
            int circleLength=(int) ((ResDefaultConfig.Map.HEXAGON_WIDTH + 13) * ResDefaultConfig.Map.MAP_SCALE / 2 * scale);


            //先绘制海岸  //沿海地块用圆形来填充
            //pixmap.setBlending(Blending.None);
            //  Gdx.app.log("setColor1","begin"+" ");
            int i = 0,  j = 0,id,iMax = cam.cw+2,jMax=cam.ch+2;
            for (; j<jMax; j++) {
                for (i=0;i<iMax; i++) {
                    //  x = (i % smapDao.masterData.getWidth()) + 1;
                    //  y = (i / smapDao.masterData.getWidth()) + 1;
                    x=cam.csx+i;
                    if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                        x=x-smapDao.masterData.getWidth();
                    }
                    y=cam.csy+j;
                    id=x-1+(y-1)*smapDao.masterData.getWidth();
                    // Gdx.app.log("setColor","id:"+id+" x:"+x+" y:"+y);
                    if ( id>0&&id<smapDao.hexagonDatas.size ) {
                        Fb2Map.MapHexagon h=smapDao.hexagonDatas.get(id);
                        if ( h.getRegionId() == -1 ) {
                            continue;
                        }
                        if(!ifDrawSea&&h.getBlockType() == 1){
                            continue;
                        }
                        //Gdx.app.log("海陆绘制圆", "" + id);
                        nowCamp=h.getCamp();
                        if (nowCamp != nextCamp) {
                              //  color=smapDao.getColorByCamp(h.getRegionId());
                            color=DefDAO.getColorForCamp(nowCamp);

                            //获取颜色
                            shapeRenderer.setColor(color.r,color.g,color.b,0.3f);
                            nextCamp =nowCamp;
                            //Gdx.app.log("setColor","id:"+id+" color:"+color);
                        }
                        hexgaon_x1 = getHexagonX1F(x,y,scale)+mapW_px;//左上,左下
                        hexgaon_x2 =  getHexagonX2F(x,y,scale)+mapW_px;//右上,右下
                        hexgaon_x3 =  getHexagonX3F(x,y,scale)+mapW_px;//左中
                        hexgaon_x4 =  getHexagonX4F(x,y,scale)+mapW_px;//右中
                        hexgaon_y1 = mapH_px-getHexagonY1F(x,y,scale);//上
                        hexgaon_y2 =   mapH_px-getHexagonY2F(x,y,scale);//中
                        hexgaon_y3 =  mapH_px- getHexagonY3F(x,y,scale);//下
                        x_px_circle=getX_px_CircleF(x,scale)+mapW_px;
                        y_px_circle=mapH_px-getY_px_CircleF(x,y,scale);
                        if(h.getIfCoast()>0){
                            // shapeRenderer.circle(x_px_circle, y_px_circle, (int) ((ResConfig.Map.HEXAGON_WIDTH + 13) * ResConfig.Map.MAP_SCALE / 2 * scale));
                            drawCoastByShapeRenderer(smapDao,shapeRenderer,id,h.getIfCoast(),x_px_circle, y_px_circle,hexgaon_x1,hexgaon_x2,hexgaon_x3,hexgaon_x4,hexgaon_y1,hexgaon_y2,hexgaon_y3,circleLength,scale,gridW,gridH);
                        }else{
                            //shapeRenderer.setColor(Color.RED);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y1, hexgaon_x2, hexgaon_y1);
                            //  shapeRenderer.setColor(Color.WHITE);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y1, hexgaon_x4, hexgaon_y2);
                            // shapeRenderer.setColor(Color.BLUE);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x2, hexgaon_y3, hexgaon_x4, hexgaon_y2);
                            //   shapeRenderer.setColor(Color.YELLOW);
                            shapeRenderer.triangle(hexgaon_x3, hexgaon_y2, hexgaon_x1, hexgaon_y3, hexgaon_x2, hexgaon_y3);
                        }
                        //绘制交界
                        drawCountryBorderByShapeRenderer(smapDao,shapeRenderer,id,h,hexgaon_x1,hexgaon_x2,hexgaon_x3,hexgaon_x4,hexgaon_y1,hexgaon_y2,hexgaon_y3);



                        //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    }
                }
            }
            if(potionHexagon!=-1&&potionRange>0){
                x = (potionHexagon % smapDao.masterData.getWidth()) + 1;
                y = (potionHexagon / smapDao.masterData.getWidth()) + 1;
                x_px_circle=getX_px_CircleF(x,scale)+mapW_px;
                y_px_circle=mapH_px-getY_px_CircleF(x,y,scale);
                shapeRenderer.circle(x,y,circleLength*potionRange);
            }
            //    Gdx.app.log("setColor1","end");
        }
        shapeRenderer.end();
    }

    //绘制预览图   0颜色图 1阵营图
    public static Pixmap getPriviewMapBySMapDAO( Pixmap pixmap,Fb2Smap sMapDAO,int drawType) {

        if(pixmap==null){
            pixmap=new Pixmap(sMapDAO.masterData.getWidth()*2, (int) sMapDAO.masterData.getHeight()*2+1, Pixmap.Format.RGBA8888);
        }else{
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
        }

        int x = 0;
        int y = 0,px_x,px_y,li;
        int w=sMapDAO.masterData.getWidth();
        int h=sMapDAO.masterData.getHeight();
        Color color=Color.WHITE;
      //  Color color1=null;
        Fb2Map.MapHexagon hexagon;
        int lastLi=-1;
        // 单线程绘制图片
        for (int id = 0,iMax=sMapDAO.hexagonDatas.size; id < iMax; id++) {

            x = (id %w) + 1;
            y = (id / w) + 1;
            hexagon=sMapDAO.hexagonDatas.get(id);
            li=sMapDAO.getLegionIndexByRegion(id);
            if(drawType==1){
                if(hexagon.getBlockType()==1||(li<0||li>sMapDAO.legionDatas.size)){
                    color=DefDAO.getColorByBackTile(hexagon.getBackTile());
                    lastLi=-1;
                }else  if(lastLi!=li){
                    lastLi=li;
                    color=sMapDAO.legionColors.get(li);
                }
                pixmap.setColor(color.r,color.g,color.b,1f);
            }else if(drawType==0){
                if(hexagon.getBackTile()!=1&&hexagon.getBackTile()!=2&&li>=0){
                    color=DefDAO.getColorForCamp(hexagon.getCamp());
                }else{
                    color=DefDAO.getColorByBackTile(hexagon.getBackTile());
                }
                pixmap.setColor(color);
            }else if(drawType==2){

                if(hexagon.getBackTile()!=1&&hexagon.getBackTile()!=2&&li>=0){
                    GameUtil.setColorForSR(null,pixmap,hexagon.getBuildData().getStrategicRegion(),-1);

                }else{
                    color=DefDAO.getColorByBackTile(hexagon.getBackTile());
                    pixmap.setColor(color);
                }

            }

            px_x=2*(x-1);
            if((x&1)==1){
                px_y=2*(y-1);
            }else{
                px_y=2*(y-1)+1;
            }

            /*if(id<400){
                Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y+" BackTile:"+h.getBackTile()+" x_px:"+px_x+" y_px:"+px_y);
            }*/

            pixmap.drawPixel( px_x ,px_y);
            pixmap.drawPixel( px_x+1 ,px_y);
            pixmap.drawPixel( px_x ,px_y+1);
            pixmap.drawPixel( px_x+1 ,px_y+1);
        }
        //Gdx.app.log("地图构建", "完成");
        //PixmapIO.writePNG(Gdx.files.local(path), pixmap);
        return pixmap;
    }

    //绘制玩家的主要领土
    public static Pixmap getPriviewMapForPlayerTerritory(Pixmap pixmap, Fb2Smap sMapDAO) {
        //最大比例尺应该为2:1
        if(pixmap==null){
            pixmap=new Pixmap(sMapDAO.masterData.getWidth()*2, (int) sMapDAO.masterData.getHeight()*2+1, Pixmap.Format.RGBA8888);
        }else{
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
        }
        int px_x,px_y,li;
        int x = 0;
        int y = 0;
        int w=sMapDAO.masterData.getWidth();
        int h=sMapDAO.masterData.getHeight();
        Color color=Color.WHITE;
        //  Color color1=null;
        Fb2Map.MapHexagon hexagon;
     //   int lastLi=-1;
        // 单线程绘制图片
        for (int id = 0,iMax=sMapDAO.hexagonDatas.size; id < iMax; id++) {

            x = (id %w) + 1;
            y = (id / w) + 1;
            hexagon=sMapDAO.hexagonDatas.get(id);
            li=sMapDAO.getLegionIndexByRegion(id);

             if(hexagon.isCountryBorder&&li==sMapDAO.masterData.getPlayerLegionIndex()){
             //   color=sMapDAO.legionColors.get(li);
              //  pixmap.setColor(color.r,color.g,color.b,1);
                 pixmap.setColor(Color.WHITE);
            }else if(!hexagon.isSea()&&li==0){
                 pixmap.setColor(Color.WHITE);
            }   else if(hexagon.getBackTile()!=1&&hexagon.getBackTile()!=2&&li>=0){
                GameUtil.setColorForSR(null,pixmap,hexagon.getBuildData().getStrategicRegion(),255);
            }else{
                color=DefDAO.getColorByBackTile(hexagon.getBackTile());
                pixmap.setColor(color);
            }

            px_x=2*(x-1);
            if((x&1)==1){
                px_y=2*(y-1);
            }else{
                px_y=2*(y-1)+1;
            }

            /*if(id<400){
                Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y+" BackTile:"+h.getBackTile()+" x_px:"+px_x+" y_px:"+px_y);
            }*/

            pixmap.drawPixel( px_x ,px_y);
            pixmap.drawPixel( px_x+1 ,px_y);
            pixmap.drawPixel( px_x ,px_y+1);
            pixmap.drawPixel( px_x+1 ,px_y+1);
        }
        //Gdx.app.log("地图构建", "完成");
        //PixmapIO.writePNG(Gdx.files.local(path), pixmap);
        return pixmap;
    }
    //210318
    public static void drawLegionPriviewMapBySMapDAO( Fb2Smap sMapDAO,String path) {
        Pixmap pixmap=new Pixmap(sMapDAO.masterData.getWidth()*2, (int) sMapDAO.masterData.getHeight()*2+1, Pixmap.Format.RGBA8888);


        int x = 0;
        int y = 0,px_x,px_y,li;
        int w=sMapDAO.masterData.getWidth();
        int h=sMapDAO.masterData.getHeight();
        Color color=Color.WHITE;
        Fb2Map.MapHexagon hexagon;
        int lastLi=-1;
        // 单线程绘制图片
        for (int id = 0,iMax=sMapDAO.hexagonDatas.size; id < iMax; id++) {

            x = (id %w) + 1;
            y = (id / w) + 1;
            hexagon=sMapDAO.hexagonDatas.get(id);
            li=sMapDAO.getLegionIndexByRegion(id);
            if(hexagon.getBlockType()==1||(li<0||li>sMapDAO.legionDatas.size)){
                color=DefDAO.getColorByBackTile(hexagon.getBackTile());
                lastLi=-1;
            }else  if(lastLi!=li){
                lastLi=li;
                color=sMapDAO.legionColors.get(li);
            }
            px_x=2*(x-1);
            if((x&1)==1){
                px_y=2*(y-1);
            }else{
                px_y=2*(y-1)+1;
            }

            /*if(id<400){
                Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y+" BackTile:"+h.getBackTile()+" x_px:"+px_x+" y_px:"+px_y);
            }*/


            pixmap.setColor(color.r,color.g,color.b,1);
            pixmap.drawPixel( px_x ,px_y);
            pixmap.drawPixel( px_x+1 ,px_y);
            pixmap.drawPixel( px_x ,px_y+1);
            pixmap.drawPixel( px_x+1 ,px_y+1);
        }
        //Gdx.app.log("地图构建", "完成");
        PixmapIO.writePNG(Gdx.files.external(path), pixmap);
        pixmap.dispose();
    }


    //
    public static void drawPixmapForPriviewMap(Fb2Map mapBinDAO, String path){
// 解析dao
        if (mapBinDAO.getMapbin() != null) {

            Pixmap pixmap=new Pixmap(mapBinDAO.mapWidth*2, (int) mapBinDAO.mapHeight*2+1, Pixmap.Format.RGBA8888);

            int x = 0;
            int y = 0,t,px_x,px_y;
            Color color;

            // 单线程绘制图片
            for (int id = 0,iMax=mapBinDAO.getMapbin().size; id < iMax; id++) {

                x = (id % mapBinDAO.getMapWidth()) + 1;
                y = (id / mapBinDAO.getMapWidth()) + 1;
                t=mapBinDAO.getMapbin().get(id).getBackTile();

                color=DefDAO.getColorByBackTile(t);
                px_x=2*(x-1);
                if((x&1)==1){
                    px_y=2*(y-1);
                }else{
                    px_y=2*(y-1)+1;
                }

                if(id<400){
                    Gdx.app.log("地图开始构建", "mapId:" + id + " x:" + x + " y:" + y+" BackTile:"+t+" x_px:"+px_x+" y_px:"+px_y);
                }
                pixmap.setColor(color);
                pixmap.drawPixel( px_x ,px_y);
                pixmap.drawPixel( px_x+1 ,px_y);
                pixmap.drawPixel( px_x ,px_y+1);
                pixmap.drawPixel( px_x+1 ,px_y+1);
            }

            Gdx.app.log("地图构建", "完成");
              PixmapIO.writePNG(Gdx.files.external(path), pixmap);
        } else {
            Gdx.app.log("地图构建", "失败");

        }
    }


    public static void drawPixmapForCountryBorder(){
        Pixmap c1=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_1.png"));
        Pixmap c2=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_2.png"));
        Pixmap c3=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_3.png"));
        Pixmap c4=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_4.png"));
        Pixmap c5=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_5.png"));
        Pixmap c6=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_6.png"));
        Pixmap c11=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_11.png"));
        Pixmap c12=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_12.png"));
        Pixmap c13=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_13.png"));
        Pixmap c14=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_14.png"));
        Pixmap c15=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_15.png"));
        Pixmap c16=new Pixmap(Gdx.files.internal(ResDefaultConfig.Path.TempPixmapFolderPath+"countryBorder_16.png"));

        Pixmap pixmap1=new Pixmap(c1.getWidth(), c1.getHeight(), Pixmap.Format.RGBA8888);
        Pixmap pixmap2=new Pixmap(c1.getWidth(), c1.getHeight(), Pixmap.Format.RGBA8888);

        for(int i=1;i<=63;i++){
            pixmap1.fill();
            pixmap2.fill();
            String border=GameMethod.transFBorderIdForString(i);
            boolean b1=true,b2=true,b3=true,b4=true,b5=true,b6=true;
            if(border.substring(0,1).equals("0")){
                b1=false;
            }
            if(border.substring(1,2).equals("0")){
                b2=false;
            }
            if(border.substring(2,3).equals("0")){
                b3=false;
            }
            if(border.substring(3,4).equals("0")){
                b4=false;
            }
            if(border.substring(4,5).equals("0")){
                b5=false;
            }
            if(border.substring(5,6).equals("0")){
                b6=false;
            }
            if(b1){
                pixmap1.drawPixmap(c1,0,0);
                pixmap2.drawPixmap(c11,0,0);
            }
            if(b2){
                pixmap1.drawPixmap(c2,0,0);
                pixmap2.drawPixmap(c12,0,0);
            }
            if(b3){
                pixmap1.drawPixmap(c3,0,0);
                pixmap2.drawPixmap(c13,0,0);
            }
            if(b4){
                pixmap1.drawPixmap(c4,0,0);
                pixmap2.drawPixmap(c14,0,0);
            }
            if(b5){
                pixmap1.drawPixmap(c5,0,0);
                pixmap2.drawPixmap(c15,0,0);
            }
            if(b6){
                pixmap1.drawPixmap(c6,0,0);
                pixmap2.drawPixmap(c16,0,0);
            }
            PixmapIO.writePNG(Gdx.files.external("countryInteralBorder_"+i+".png"), pixmap1);
            PixmapIO.writePNG(Gdx.files.external("countryExternalBorder_"+i+".png"), pixmap2);
        }

        c1.dispose();
        c2.dispose();
        c3.dispose();
        c4.dispose();
        c5.dispose();
        c6.dispose();
        c11.dispose();
        c12.dispose();
        c13.dispose();
        c14.dispose();
        c15.dispose();
        c16.dispose();
        pixmap1.dispose();
        pixmap2.dispose();

    }

    //drawType
    public static void old_drawAllTileForMap(MainGame game,Batch batch,Fb2Smap smapDao,  CamerDAO cam, float scale,  int mapW_px, int mapH_px,int showType,boolean ifDrawLegionName,int flashRegion) {

        if (smapDao.hexagonDatas != null && smapDao.hexagonDatas.size > 0) {
            //smapDao.setHexagonDataIfDrawIsFalse();
            int x = 0;
            int y = 0;

            //先绘制海岸
            //  Gdx.app.log("setColor1","begin"+" ");
            boolean isEditMode=smapDao.isEditMode(true);

            int i = 0, j = 0, id, iMax = cam.cw + 2, jMax = cam.ch + 3;
            Color color = Color.CLEAR;
            if(cam.rescource.drawType==0 ||(cam.rescource.drawType==1 &&smapDao.getGame().gameConfig.ifEffect)  ){
            for (; j < jMax; j++) {
                for (i = 0; i < iMax; i++) {
                    x = cam.csx + i;
                    if (smapDao.ifLoop && x > smapDao.masterData.getWidth()) {
                        x = x - smapDao.masterData.getWidth();
                    }
                    y = cam.csy + j;
                    id = x - 1 + (y - 1) * smapDao.masterData.getWidth();
                    if (id >= 0 && id < smapDao.hexagonDatas.size) {
                        Fb2Map.MapHexagon h = smapDao.hexagonDatas.get(id);
                        if( h.getBlockType()!= 1){
                        //Gdx.app.log("海陆绘制圆", "" + id);
                            if (cam.loopState == 0 || cam.loopState == 1) {
                                if (h.getTile1() != null ) {
                                    batch.draw(h.getTile1().getTextureRegion(),
                                            h.source_x + h.getBackRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y + h.getBackRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.getTile1().getW(), h.getTile1().getH(), 1, 1, 0);
                                }
                                if (h.getTile2() != null) {
                                   batch.draw(h.getTile2().getTextureRegion(),
                                            h.source_x + h.getForeRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y + h.getForeRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.getTile2().getW(), h.getTile2().getH(), 1, 1, 0); /**/
                                }
                                if(smapDao.ifSystemEffective(22)) {
                                    if (h.railwayTile != null) {
                                        batch.draw(h.railwayTile.getTextureRegion(),
                                                h.source_x - h.railwayTile.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - h.railwayTile.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, h.railwayTile.getW(), h.railwayTile.getH(), 1, 1, 0); /**/
                                    } else if (isEditMode && h.getPresetRailway() == 1) {
                                        batch.draw(cam.rescource.preRailwayRegionDAO.getTextureRegion(),
                                                h.source_x - cam.rescource.preRailwayRegionDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.preRailwayRegionDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.preRailwayRegionDAO.getW(), cam.rescource.preRailwayRegionDAO.getH(), 1, 1, 0);
                                    }
                                }
                            }
                            if (cam.loopState == 1 || cam.loopState == 2) {
                                if (h.getTile1() != null ) {
                                    batch.draw(h.getTile1().getTextureRegion(),
                                            h.source_x + mapW_px + h.getBackRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y + h.getBackRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.getTile1().getW(), h.getTile1().getH(), 1, 1, 0);
                                }
                                if (h.getTile2() != null) {
                                    batch.draw(h.getTile2().getTextureRegion(),
                                            h.source_x + mapW_px + h.getForeRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y + h.getForeRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.getTile2().getW(), h.getTile2().getH(), 1, 1, 0);
                                }
                                if(smapDao.ifSystemEffective(22)){
                                    if (h.railwayTile != null) {
                                        batch.draw(h.railwayTile.getTextureRegion(),
                                                h.source_x + mapW_px  - h.railwayTile.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y  - h.railwayTile.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, h.railwayTile.getW(), h.railwayTile.getH(), 1, 1, 0); /**/
                                    }else if(isEditMode&&h.getPresetRailway()==1){
                                        batch.draw(cam.rescource.preRailwayRegionDAO.getTextureRegion(),
                                                h.source_x+ mapW_px   - cam.rescource.preRailwayRegionDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y  - cam.rescource.preRailwayRegionDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.preRailwayRegionDAO.getW(), cam.rescource.preRailwayRegionDAO.getH(), 1, 1, 0);
                                    }
                                }
                            }
                        }
                        //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    }
                }
            }

        }

            //if(true){return;}
            //再额外绘制一层海陆以及陆地颜色
            batch.setColor(Color.WHITE);
            i = 0;  j = 0;
            for (; j<jMax; j++) {
                for (i=0;i<iMax; i++) {

                    x=cam.csx+i;
                    if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                        x=x-smapDao.masterData.getWidth();
                    }
                    y=cam.csy+j;
                    id=x-1+(y-1)*smapDao.masterData.getWidth();
                    if ( id>=0&&id<smapDao.hexagonDatas.size ) {
                        Fb2Map.MapHexagon h=smapDao.hexagonDatas.get(id);
                               if(h.getBlockType()==1&&h.getTile1()!=null){
                                   if(cam.loopState==0||cam.loopState==1){
                                    batch.draw(h.getTile1().getTextureRegion(),
                                            h.source_x+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.getTile1().getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.getTile1().getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.getTile1().getW(),h.getTile1().getH(),1,1,0);
                                   }
                                   if(cam.loopState==1||cam.loopState==2) {
                                       batch.draw(h.getTile1().getTextureRegion(),
                                               mapW_px +h.source_x + h.getBackRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                               h.source_y + h.getBackRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                               , 0, 0, h.getTile1().getW(), h.getTile1().getH(), 1, 1, 0);
                                   }
                                }

                        //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    }
                }
            }
            //绘制边界
            i = 0;  j = 0;;
            for (; j<jMax; j++) {
                for (i=0;i<iMax; i++) {

                    x=cam.csx+i;
                    if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                        x=x-smapDao.masterData.getWidth();
                    }
                    y=cam.csy+j;
                    id=x-1+(y-1)*smapDao.masterData.getWidth();


                    if ( id>=0&&id<smapDao.hexagonDatas.size ) {
                        Fb2Map.MapHexagon h=smapDao.hexagonDatas.get(id);
                            if(h.getBlockType()!=1){
                                if (h.getRegionId() != -1) {
                                    color = smapDao.getColorForRegion(h.getHexagonIndex(), true, true,smapDao.masterData.getPlayerMode()==2);
                                } else {
                                    color = Color.CLEAR;
                                }
                                if(smapDao.masterData.getPlayerMode()==2&&h.getRegionId()==flashRegion){
                                    batch.setColor(color.r-cam.getAlphaFlash()/3,color.g-cam.getAlphaFlash()/3,color.b-cam.getAlphaFlash()/3,0.5f+cam.getAlphaFlash()/3);
                                }else{
                                    batch.setColor(color);
                                }
                                if ( ( cam.rescource.drawType==1||cam.rescource.drawType==2)  &&h.getBuildData().ifDrawWarLine()) {
                                    if (cam.loopState == 0 || cam.loopState == 1) {
                                        batch.draw(cam.rescource.hexagonDAO1.getTextureRegion(),
                                                h.source_x - cam.rescource.hexagonDAO1.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.hexagonDAO1.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.hexagonDAO1.getW(), cam.rescource.hexagonDAO1.getH(), 1, 1, 0);

                                    }
                                    if (cam.loopState == 1 || cam.loopState == 2) {
                                        batch.draw(cam.rescource.hexagonDAO1.getTextureRegion(),
                                                mapW_px + h.source_x - cam.rescource.hexagonDAO1.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.hexagonDAO1.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.hexagonDAO1.getW(), cam.rescource.hexagonDAO1.getH(), 1, 1, 0);

                                    }
                                } else {

                                    if (cam.loopState == 0 || cam.loopState == 1) {
                                        batch.draw(cam.rescource.hexagonDAO.getTextureRegion(),
                                                h.source_x - cam.rescource.hexagonDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.hexagonDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.hexagonDAO.getW(), cam.rescource.hexagonDAO.getH(), 1, 1, 0);
                                    }
                                    if (cam.loopState == 1 || cam.loopState == 2) {
                                        batch.draw(cam.rescource.hexagonDAO.getTextureRegion(),
                                                mapW_px + h.source_x - cam.rescource.hexagonDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.hexagonDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.hexagonDAO.getW(), cam.rescource.hexagonDAO.getH(), 1, 1, 0);
                                    }
                                    batch.setColor(Color.WHITE);
                                }
                            }





                    //海浪
                        if (h.getBlockType()==1) {
                            if(h.getTile2() != null){
                                batch.setColor(Color.WHITE);
                                if(cam.loopState==0||cam.loopState==1){
                                    batch.draw(h.getTile2().getTextureRegion(),
                                            h.source_x + h.getBackRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y + h.getBackRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.getTile2().getW(), h.getTile2().getH(), 1, 1, 0);
                                }
                                if (cam.loopState == 1 || cam.loopState == 2) {
                                    batch.draw(h.getTile2().getTextureRegion(),
                                            mapW_px+h.source_x  + h.getForeRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y + h.getForeRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.getTile2().getW(), h.getTile2().getH(), 1, 1, 0);
                                }
                            }
                        }


                       /* if(h.getHexagonIndex()==7354){
                            int s=0;
                        }*/


                        if(( cam.rescource.drawType==1||cam.rescource.drawType==2)  &&cam.rescource.drawType<2  ){
                            if(h.regionLineBorderTile!=null){
                                batch.setColor(Color.WHITE);
                                if(cam.loopState==0||cam.loopState==1){
                                    batch.draw(h.regionLineBorderTile.getTextureRegion(),
                                            h.source_x+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.regionLineBorderTile.getW(),h.regionLineBorderTile.getH(),1,1,0);
                                }

                                if(cam.loopState==1||cam.loopState==2){
                                    batch.draw(h.regionLineBorderTile.getTextureRegion(),
                                            mapW_px+h.source_x+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.regionLineBorderTile.getW(),h.regionLineBorderTile.getH(),1,1,0);

                                }
                            }

                        }

                        /*if(h.getHexagonIndex()==8494){
                            int s=0;
                        }*/

                        if( cam.rescource.drawType>0 &&smapDao.getGame().gameConfig.ifEffect  ){
                            /*if(h.getHexagonIndex()==19918){
                                int s=0;
                            }*/
                            if(h.countryBorder!=0){
                                //int borderId1=smapDao.getBorderIdByDirect(h.getHexagonIndex(), 1);
                                //int borderId2=smapDao.getBorderIdByDirect(h.getHexagonIndex(), 2);
                                //int borderId3=smapDao.getBorderIdByDirect(h.getHexagonIndex(), 3);
                                //int borderId4=smapDao.getBorderIdByDirect(h.getHexagonIndex(), 4);
                                //int borderId5=smapDao.getBorderIdByDirect(h.getHexagonIndex(), 5);
                                //int borderId6=smapDao.getBorderIdByDirect(h.getHexagonIndex(), 6);
                                if(cam.loopState==0||cam.loopState==1){
                                    if(h.countryInnerBorderTile!=null&&h.countryInnerBorderColor!=null){
                                        //  color=smapDao.getOppositeColorForRegion(h.getHexagonIndex(),false,defaultColor);
                                        //  batch.setColor(color.r,color.g,color.b,1);
                                        batch.setColor(h.countryInnerBorderColor.r,h.countryInnerBorderColor.g,h.countryInnerBorderColor.b,1);
                                        batch.draw(h.countryInnerBorderTile.getTextureRegion(),
                                                h.source_x-h.countryInnerBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y-h.countryInnerBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                                ,0,0,h.countryInnerBorderTile.getW(),h.countryInnerBorderTile.getH(),1,1,0);
                                    }
                                    if(h.ifMoreCountryBorder){


                                        if (h.countryBorderColor1!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor1.r,h.countryBorderColor1.g,h.countryBorderColor1.b,1f);
                                            //       color=smapDao.getOppositeColorForRegion(borderId2,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            //    batch.setColor(color.r,color.g,color.b,1);

                                            batch.draw(cam.rescource.countryBorderDAO11.getTextureRegion(),
                                                    h.source_x - cam.rescource.countryBorderDAO11.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO11.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO11.getW(), cam.rescource.countryBorderDAO11.getH(), 1, 1, 0);
                                        }
                                       /* if(h.getHexagonIndex()==4087){
                                            int s=0;
                                        }*/
                                        if (h.countryBorderColor2!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor2.r,h.countryBorderColor2.g,h.countryBorderColor2.b,1f);
                                            //  color=smapDao.getOppositeColorForRegion(borderId3,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            //   batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO12.getTextureRegion(),
                                                    h.source_x - cam.rescource.countryBorderDAO12.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO12.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO12.getW(), cam.rescource.countryBorderDAO12.getH(), 1, 1, 0);
                                        }
                                        if (h.countryBorderColor3!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor3.r,h.countryBorderColor3.g,h.countryBorderColor3.b,1f);
                                            //  color=smapDao.getOppositeColorForRegion(borderId6,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            //   batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO13.getTextureRegion(),
                                                    h.source_x - cam.rescource.countryBorderDAO13.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO13.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO13.getW(), cam.rescource.countryBorderDAO13.getH(), 1, 1, 0);
                                        }
                                        if (h.countryBorderColor4!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor4.r,h.countryBorderColor4.g,h.countryBorderColor4.b,1f);
                                            // color=smapDao.getOppositeColorForRegion(borderId5,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            // batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO14.getTextureRegion(),
                                                    h.source_x - cam.rescource.countryBorderDAO14.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO14.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO14.getW(), cam.rescource.countryBorderDAO14.getH(), 1, 1, 0);
                                        }



                                        if (h.countryBorderColor5!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor5.r,h.countryBorderColor5.g,h.countryBorderColor5.b,1f);
                                            //  color=smapDao.getOppositeColorForRegion(borderId4,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            //  batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO15.getTextureRegion(),
                                                    h.source_x - cam.rescource.countryBorderDAO15.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO15.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO15.getW(), cam.rescource.countryBorderDAO15.getH(), 1, 1, 0);
                                        }
                                        if (h.countryBorderColor6!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor6.r,h.countryBorderColor6.g,h.countryBorderColor6.b,1f);
                                            //  color=smapDao.getOppositeColorForRegion(borderId1,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            //  batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO16.getTextureRegion(),
                                                    h.source_x - cam.rescource.countryBorderDAO16.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO16.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO16.getW(), cam.rescource.countryBorderDAO16.getH(), 1, 1, 0);
                                        }

                                    }else{
                                        if(h.countryOuterBorderTile!=null&&h.countryOuterBorderColor!=null){
                                            // color=h.getOuterColor(h.countryBorder1,h.countryBorder2,h.countryBorder3,h.countryBorder4,h.countryBorder5,h.countryBorder6,defaultColor);
                                            //  batch.setColor(color.r,color.g,color.b,1);
                                            batch.setColor(h.countryOuterBorderColor.r,h.countryOuterBorderColor.g,h.countryOuterBorderColor.b,1f);
                                            batch.draw(h.countryOuterBorderTile.getTextureRegion(),
                                                    h.source_x-h.countryOuterBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y-h.countryOuterBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                                    ,0,0,h.countryOuterBorderTile.getW(),h.countryOuterBorderTile.getH(),1,1,0);
                                        }
                                    }
                                    /**/
                                }

                                if(cam.loopState==1||cam.loopState==2){
                                    if(h.countryInnerBorderTile!=null&&h.countryInnerBorderColor!=null){
                                        //color=smapDao.getOppositeColorForRegion(h.getHexagonIndex(),false,defaultColor);
                                        // batch.setColor(color.r,color.g,color.b,1);
                                        batch.setColor(h.countryInnerBorderColor.r,h.countryInnerBorderColor.g,h.countryInnerBorderColor.b,1);
                                        batch.draw(h.countryInnerBorderTile.getTextureRegion(),
                                                mapW_px+h.source_x-h.countryInnerBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.countryInnerBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                                ,0,0,h.countryInnerBorderTile.getW(),h.countryInnerBorderTile.getH(),1,1,0);
                                    }
                                    if(h.ifMoreCountryBorder){
                                        if (h.countryBorderColor1!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor1.r,h.countryBorderColor1.g,h.countryBorderColor1.b,1);
                                            // color=smapDao.getOppositeColorForRegion(borderId2,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            // batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO11.getTextureRegion(),
                                                    cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO11.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO11.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO11.getW(), cam.rescource.countryBorderDAO11.getH(), 1, 1, 0);
                                        }
                                        if (h.countryBorderColor2!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor2.r,h.countryBorderColor2.g,h.countryBorderColor2.b,1);
                                            //  color=smapDao.getOppositeColorForRegion(borderId3,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            //  batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO12.getTextureRegion(),
                                                    cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO12.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO12.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO12.getW(), cam.rescource.countryBorderDAO12.getH(), 1, 1, 0);
                                        }
                                        if (h.countryBorderColor3!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor3.r,h.countryBorderColor3.g,h.countryBorderColor3.b,1);
                                            //color=smapDao.getOppositeColorForRegion(borderId6,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            // batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO13.getTextureRegion(),
                                                    cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO13.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO13.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO13.getW(), cam.rescource.countryBorderDAO13.getH(), 1, 1, 0);
                                        }
                                        if (h.countryBorderColor4!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor4.r,h.countryBorderColor4.g,h.countryBorderColor4.b,1);
                                            //color=smapDao.getOppositeColorForRegion(borderId5,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            // batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO14.getTextureRegion(),
                                                    cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO14.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO14.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO14.getW(), cam.rescource.countryBorderDAO14.getH(), 1, 1, 0);
                                        }



                                        if (h.countryBorderColor5!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor5.r,h.countryBorderColor5.g,h.countryBorderColor5.b,1);
                                            //color=smapDao.getOppositeColorForRegion(borderId4,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            //batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO15.getTextureRegion(),
                                                    cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO15.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO15.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO15.getW(), cam.rescource.countryBorderDAO15.getH(), 1, 1, 0);
                                        }
                                        if (h.countryBorderColor6!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                            batch.setColor(h.countryBorderColor6.r,h.countryBorderColor6.g,h.countryBorderColor6.b,1);
                                            //color=smapDao.getOppositeColorForRegion(borderId1,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                            //batch.setColor(color.r,color.g,color.b,1);
                                            batch.draw(cam.rescource.countryBorderDAO16.getTextureRegion(),
                                                    cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO16.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y - cam.rescource.countryBorderDAO16.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                    , 0, 0, cam.rescource.countryBorderDAO16.getW(), cam.rescource.countryBorderDAO16.getH(), 1, 1, 0);
                                        }

                                    }else{
                                        if(h.countryOuterBorderTile!=null&&h.countryOuterBorderColor!=null){
                                            //  color=h.getOuterColor(h.countryBorder1,h.countryBorder2,h.countryBorder3,h.countryBorder4,h.countryBorder5,h.countryBorder6,defaultColor);
                                            //  batch.setColor(color.r,color.g,color.b,1);
                                            batch.setColor(h.countryOuterBorderColor.r,h.countryOuterBorderColor.g,h.countryOuterBorderColor.b,1f);
                                            batch.draw(h.countryOuterBorderTile.getTextureRegion(),
                                                    mapW_px+h.source_x-h.countryOuterBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                                    h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.countryOuterBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                                    ,0,0,h.countryOuterBorderTile.getW(),h.countryOuterBorderTile.getH(),1,1,0);
                                        }
                                    }
                                }
                            }
                        } else {
                            if(h.countryLineBorderTile!=null){

                                if(cam.loopState==0||cam.loopState==1){
                                    batch.draw(h.countryLineBorderTile.getTextureRegion(),
                                            h.source_x-h.countryLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y-h.countryLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.countryLineBorderTile.getW(),h.countryLineBorderTile.getH(),1,1,0);
                                }

                                if(cam.loopState==1||cam.loopState==2){
                                    batch.draw(h.countryLineBorderTile.getTextureRegion(),
                                            mapW_px+h.source_x-h.countryLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y-h.countryLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.countryLineBorderTile.getW(),h.countryLineBorderTile.getH(),1,1,0);
                                }
                            }
                        }
                        //Gdx.app.log("updColor","id:"+id+" legion:"+(Integer) regionColors.get(mapBinDAO.getMapbin().get(id).getPolitical()));
                    }
                }
            }


           if(ifDrawLegionName&&game.gameConfig.ifEffect){
                //绘制势力文字 国家名
                x=cam.csx-1;
                if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                    x=x-smapDao.masterData.getWidth();
                }
                y=cam.csy-1;
                int cw=cam.cw+2;
                int ch=cam.ch+3;
                for ( i=0;i<smapDao.legionDatas.size;i++) {

                    Fb2Smap.LegionData l=smapDao.legionDatas.get(i);
                    if(l.varRegionCount==0||l.getInternIndex()==0){
                        continue;
                    }
                    if(l.legionNameScale!=0&&l.legionNameIntersect(x,y,cw,ch,cam.loopState)){
                        game.gameConfig.gameFont.getData().setScale(l.legionNameScale);
                        if(game.gameConfig.ifColor &&!game.getSMapDAO().ifHaveImageBg()){
                            color=l.getColorByAlly2();
                        }else {
                            color=l.getReallyFogColor();
                        }
                        game.gameConfig.gameFont.setColor(   color.r, color.g, color.b, 1f);
                        if (l.varW>=l.varH) {
                            if(cam.loopState==0||cam.loopState==1){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,l.centerPx_x , l.centerPx_y+ game.gameConfig.gameFont.getCapHeight()/2,30, Align.center,false);
                            }
                            if(cam.loopState==1||cam.loopState==2){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,cam.getMapW_px()+l.centerPx_x , l.centerPx_y+ game.gameConfig.gameFont.getCapHeight()/2,30, Align.center,false);
                            }
                        }else{
                            if(cam.loopState==0||cam.loopState==1){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,l.centerPx_x, l.centerPx_y+  game.gameConfig.gameFont.getCapHeight()*(l.legionName.length()-1)   ,5*l.legionNameScale, Align.center,true);
                            }
                            if(cam.loopState==1||cam.loopState==2){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,cam.getMapW_px()+l.centerPx_x , l.centerPx_y+  game.gameConfig.gameFont.getCapHeight()*(l.legionName.length()-1)   ,5*l.legionNameScale, Align.center,true);
                            }
                        }
                        game.gameConfig.gameFont.getData().setScale(1f);
                        game.gameConfig.gameFont.setColor(Color.WHITE);
                    }
                }
            }

            //    Gdx.app.log("setColor1","end");
            batch.setColor(Color.WHITE);
        }
    }


    public static void drawAllTileForMap(MainGame game,Batch batch,Fb2Smap smapDao,  CamerDAO cam, float scale,  int mapW_px, int mapH_px,int showType,boolean ifDrawLegionName,int flashRegion) {

        if (cam.rescource!=null&&cam.rescource.allMapHexagons!=null) {
            //smapDao.setHexagonDataIfDrawIsFalse();
            int x = 0;
            int y = 0;
            //int size=cam.rescource.allMapHexagons.size;
            //先绘制海岸
            //  Gdx.app.log("setColor1","begin"+" ");
            boolean isEditMode=smapDao.isEditMode(true);

            Color color;
            if( (cam.rescource.drawType==0 ||(cam.rescource.drawType==1 &&smapDao.getGame().gameConfig.ifEffect))&&cam.rescource.tileMapHexagons.size>0){
                for (int i = 0; i<cam.rescource.tileMapHexagons.size; i++) {

                    if(i>=cam.rescource.tileMapHexagons.size){break;}
                    Fb2Map.MapHexagon h=cam.rescource.tileMapHexagons.get(i);
                    if(h==null){break;}
                    if(h.getHexagonIndex()==36586){
                        int s=0;
                    }
                    if(cam.rescource.drawType==0&&game.gameConfig.ifEffect&&h.getBlockType()==1&&h.getBackTile()==2){
                        float addA;
                        if((smapDao.masterData.getPlayerMode()==2||cam.rescource.drawType>0)&&h.getRegionId()==flashRegion){
                            if(h.isSea()){
                                addA= 0.1f+cam.getAlphaFlash()/2;
                            }else {
                                addA= 0.5f+cam.getAlphaFlash()/3;
                            }
                        }else {
                            addA= cam.rescource.drawType/10f;
                        }

                        float floorHexagonAlphaRate=1f;
                        if(smapDao.mapbin!=null&&smapDao.mapbin.floorHexagonAlphaRate>0){
                            floorHexagonAlphaRate=smapDao.mapbin.floorHexagonAlphaRate;
                        }
                        if (h.getRegionId() != -1) {
                            if(showType==1){
                                color=DefDAO.getColorForCamp(h.getCamp());
                            }else {
                                color = smapDao.getColorForRegion(h.getHexagonIndex(), true, true,false);
                            }
                        } else {
                            color = Color.CLEAR;
                        }
                        if((smapDao.masterData.getPlayerMode()==2||cam.rescource.drawType>0)&&h.getRegionId()==flashRegion){
                            batch.setColor(color.r-cam.getAlphaFlash()/3,color.g-cam.getAlphaFlash()/3,color.b-cam.getAlphaFlash()/3,addA);
                        }else {
                            if(showType==1){
                                batch.setColor(color.r,color.g,color.b,(color.a/3+addA));
                            }else{
                                batch.setColor(color.r,color.g,color.b,(color.a+addA)*floorHexagonAlphaRate);
                            }
                            if(h.isSea()&&h.ifSeaLand){
                                batch.setColor( Color.CLEAR);
                            }
                        }
                        if (cam.loopState == 0 || cam.loopState == 1) {
                            batch.draw(cam.rescource.hexagonDAO.getTextureRegion(),
                                    h.source_x - cam.rescource.hexagonDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO.getW(), cam.rescource.hexagonDAO.getH(),  scale, scale, 0);

                        }
                        if (cam.loopState == 1 || cam.loopState == 2) {
                            batch.draw(cam.rescource.hexagonDAO.getTextureRegion(),
                                    mapW_px + h.source_x - cam.rescource.hexagonDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO.getW(), cam.rescource.hexagonDAO.getH(),  scale, scale, 0);

                        }
                    }else{
                        batch.setColor(Color.WHITE);
                    }


                    //Gdx.app.log("海陆绘制圆", "" + id);
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        if (h.getTile1() != null ) {
                            batch.draw(h.getTile1().getTextureRegion(),
                                    h.source_x + h.getBackRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y + h.getBackRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, h.getTile1().getW(), h.getTile1().getH(), scale, scale, 0);
                        }
                        if (h.getTile2() != null) {
                            batch.draw(h.getTile2().getTextureRegion(),
                                    h.source_x + h.getForeRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y + h.getForeRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, h.getTile2().getW(), h.getTile2().getH(),  scale, scale, 0);
                        }
                        if(smapDao.ifSystemEffective(22)) {
                            if (h.railwayTile != null) {
                                if((h.ifRailWayMark&&cam.rescource.drawType==1)||cam.rescource.drawType!=1 ){
                                    batch.draw(h.railwayTile.getTextureRegion(),
                                            h.source_x - h.railwayTile.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - h.railwayTile.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.railwayTile.getW(), h.railwayTile.getH(), 1, 1, 0);
                                }
                                /**/
                            } else if (isEditMode && h.getPresetRailway() == 1) {
                                batch.draw(cam.rescource.preRailwayRegionDAO.getTextureRegion(),
                                        h.source_x - cam.rescource.preRailwayRegionDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                        h.source_y - cam.rescource.preRailwayRegionDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                        , 0, 0, cam.rescource.preRailwayRegionDAO.getW(), cam.rescource.preRailwayRegionDAO.getH(),  scale, scale, 0);
                            }
                        }
                    }
                    if (cam.loopState == 1 || cam.loopState == 2) {
                        if (h.getTile1() != null ) {
                            batch.draw(h.getTile1().getTextureRegion(),
                                    h.source_x + mapW_px + h.getBackRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y + h.getBackRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, h.getTile1().getW(), h.getTile1().getH(),  scale, scale, 0);
                        }
                        if (h.getTile2() != null) {
                            batch.draw(h.getTile2().getTextureRegion(),
                                    h.source_x + mapW_px + h.getForeRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y + h.getForeRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, h.getTile2().getW(), h.getTile2().getH(),  scale, scale, 0);
                        }
                        if(smapDao.ifSystemEffective(22)){
                            if (h.railwayTile != null) {
                                if((h.ifRailWayMark&&cam.rescource.drawType==1)||cam.rescource.drawType!=1 ) {
                                    batch.draw(h.railwayTile.getTextureRegion(),
                                            h.source_x + mapW_px - h.railwayTile.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - h.railwayTile.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.railwayTile.getW(), h.railwayTile.getH(), scale, scale, 0);
                                }
                            }else if(isEditMode&&h.getPresetRailway()==1){
                                batch.draw(cam.rescource.preRailwayRegionDAO.getTextureRegion(),
                                        h.source_x+ mapW_px   - cam.rescource.preRailwayRegionDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                        h.source_y  - cam.rescource.preRailwayRegionDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                        , 0, 0, cam.rescource.preRailwayRegionDAO.getW(), cam.rescource.preRailwayRegionDAO.getH(), 1, 1, 0);
                            }
                        }
                    }
                }
            }

            //if(true){return;}
            //再额外绘制一层海陆以及陆地颜色
            batch.setColor(Color.WHITE);



            for (int i=0;i<cam.rescource.seaMapHexagons.size;i++) {
                if(i>=cam.rescource.seaMapHexagons.size){break;}
                Fb2Map.MapHexagon h=cam.rescource.seaMapHexagons.get(i);
                if(h==null){break;}
                if(h.getHexagonIndex()==36586){
                    int s=0;
                }
                //Gdx.app.log("海陆绘制圆", "" + id);
               /* if (h.getHexagonIndex()==55130||h.getHexagonIndex()==369) {
                    int s=0;
                }*/
                if(h.getTile1()!=null){//海岸给一份颜色
                    if(cam.loopState==0||cam.loopState==1){
                        batch.draw(h.getTile1().getTextureRegion(),
                                h.source_x+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.getTile1().getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.getTile1().getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                ,0,0,h.getTile1().getW(),h.getTile1().getH(), scale, scale, 0);
                    }
                    if(cam.loopState==1||cam.loopState==2) {
                        batch.draw(h.getTile1().getTextureRegion(),
                                mapW_px +h.source_x + h.getBackRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                h.source_y + h.getBackRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile1().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                , 0, 0, h.getTile1().getW(), h.getTile1().getH(), scale, scale, 0);
                    }
                }
            }
            //绘制颜色
            for (int i=0;i<cam.rescource.allMapHexagons.size;i++) {

                if (i >= cam.rescource.allMapHexagons.size) {
                    break;
                }
                Fb2Map.MapHexagon h = cam.rescource.allMapHexagons.get(i);
                if (h == null) {break;}
                if(h.getHexagonIndex()==36586){
                    int s=0;
                }
               float addA;
                if((smapDao.masterData.getPlayerMode()==2||cam.rescource.drawType>0)&&h.getRegionId()==flashRegion){
                    if(h.isSea()){
                        addA= 0.1f+cam.getAlphaFlash()/2;
                    }else {
                        addA= 0.5f+cam.getAlphaFlash()/3;
                    }
                }else {
                    addA= cam.rescource.drawType/10f;
                }

                float floorHexagonAlphaRate=1f;
                if(smapDao.mapbin!=null&&smapDao.mapbin.floorHexagonAlphaRate>0){
                    floorHexagonAlphaRate=smapDao.mapbin.floorHexagonAlphaRate;
                }
                if(  (((smapDao.masterData.getPlayerMode()==2||cam.rescource.drawType>0)&&h.getRegionId()==flashRegion)||cam.rescource.drawType!=0&&!h.getRegionHexagonData().isSea())|| (cam.rescource.drawType==0&&!h.isSea())||h.facilityData!=null){

                    if (h.getRegionId() != -1) {
                        if(showType==1){
                            color=DefDAO.getColorForCamp(h.getCamp());
                        }else {
                            color = smapDao.getColorForRegion(h.getHexagonIndex(), true, true,smapDao.masterData.getPlayerMode()==2);
                        }
                    } else {
                        color = Color.CLEAR;
                    }
                    if((smapDao.masterData.getPlayerMode()==2||cam.rescource.drawType>0)&&h.getRegionId()==flashRegion){
                        batch.setColor(color.r-cam.getAlphaFlash()/3,color.g-cam.getAlphaFlash()/3,color.b-cam.getAlphaFlash()/3,addA);
                    }else {
                        if(color==Color.CLEAR){
                            batch.setColor( Color.CLEAR);
                        }else if(showType==1){
                            batch.setColor(color.r,color.g,color.b,(color.a/3+addA));
                        }else{
                            batch.setColor(color.r,color.g,color.b,(color.a+addA)*floorHexagonAlphaRate);
                        }
                        if(h.isSea()&&h.ifSeaLand){
                            batch.setColor( Color.CLEAR);
                        }
                    }

                    if ( ( cam.rescource.drawType==1||cam.rescource.drawType==2)  &&h.getBuildData().ifDrawWarLine()) {
                        if (cam.loopState == 0 || cam.loopState == 1) {
                            batch.draw(cam.rescource.hexagonDAO1.getTextureRegion(),
                                    h.source_x - cam.rescource.hexagonDAO1.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO1.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO1.getW(), cam.rescource.hexagonDAO1.getH(),  scale, scale, 0);

                        }
                        if (cam.loopState == 1 || cam.loopState == 2) {
                            batch.draw(cam.rescource.hexagonDAO1.getTextureRegion(),
                                    mapW_px + h.source_x - cam.rescource.hexagonDAO1.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO1.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO1.getW(), cam.rescource.hexagonDAO1.getH(),  scale, scale, 0);

                        }
                    } else {

                        if (cam.loopState == 0 || cam.loopState == 1) {
                            batch.draw(cam.rescource.hexagonDAO.getTextureRegion(),
                                    h.source_x - cam.rescource.hexagonDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO.getW(), cam.rescource.hexagonDAO.getH(),  scale, scale, 0);
                        }
                        if (cam.loopState == 1 || cam.loopState == 2) {
                            batch.draw(cam.rescource.hexagonDAO.getTextureRegion(),
                                    mapW_px + h.source_x - cam.rescource.hexagonDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO.getW(), cam.rescource.hexagonDAO.getH(),  scale, scale, 0);
                        }
                        batch.setColor(Color.WHITE);
                    }
                }else{
                    if(h.getTile2() != null){
                        batch.setColor(Color.WHITE);
                        if(cam.loopState==0||cam.loopState==1){
                            batch.draw(h.getTile2().getTextureRegion(),
                                    h.source_x + h.getBackRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y + h.getBackRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, h.getTile2().getW(), h.getTile2().getH(),  scale, scale, 0);
                        }
                        if (cam.loopState == 1 || cam.loopState == 2) {
                            batch.draw(h.getTile2().getTextureRegion(),
                                    mapW_px+h.source_x  + h.getForeRefX() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y + h.getForeRefY() * ResDefaultConfig.Map.MAP_SCALE - h.getTile2().getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, h.getTile2().getW(), h.getTile2().getH(),  scale, scale, 0);
                        }
                    }
                }
                if(cam.rescource.drawType==0){
                    batch.setColor(1f,1f,1f,0.6f);
                }else {
                    batch.setColor(Color.WHITE);
                }
                if(cam.rescource.drawType==0 ){
                    if(h.countryLineBorderTile!=null){

                        if(cam.loopState==0||cam.loopState==1){
                            batch.draw(h.countryLineBorderTile.getTextureRegion(),
                                    h.source_x-h.countryLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y-h.countryLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.countryLineBorderTile.getW(),h.countryLineBorderTile.getH(), scale, scale, 0);
                        }

                        if(cam.loopState==1||cam.loopState==2){
                            batch.draw(h.countryLineBorderTile.getTextureRegion(),
                                    mapW_px+h.source_x-h.countryLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y-h.countryLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.countryLineBorderTile.getW(),h.countryLineBorderTile.getH(), scale, scale, 0);
                        }
                    }
                }
                if(cam.rescource.drawType<2  ){
                    if(h.regionLineBorderTile!=null){
                        if(cam.loopState==0||cam.loopState==1){
                            batch.draw(h.regionLineBorderTile.getTextureRegion(),
                                    h.source_x+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.regionLineBorderTile.getW(),h.regionLineBorderTile.getH(), scale, scale, 0);
                        }

                        if(cam.loopState==1||cam.loopState==2){
                            batch.draw(h.regionLineBorderTile.getTextureRegion(),
                                    mapW_px+h.source_x+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.regionLineBorderTile.getW(),h.regionLineBorderTile.getH(), scale, scale, 0);

                        }
                    }
                }
            }

            //绘制国家边界
            if( cam.rescource.drawType>0&&smapDao.getGame().gameConfig.ifEffect){

                for (int i=0;i<cam.rescource.allMapHexagons.size;i++) {

                    if(i>=cam.rescource.allMapHexagons.size){break;}
                    Fb2Map.MapHexagon h=cam.rescource.allMapHexagons.get(i);
                    if(h==null){break;}
                    if(h.getHexagonIndex()==36586){
                        int s=0;
                    }
                    /**/
                        if(h.countryBorder!=0){
                            if(cam.loopState==0||cam.loopState==1){
                                if(h.countryInnerBorderTile!=null&&h.countryInnerBorderColor!=null){
                                    batch.setColor(h.countryInnerBorderColor.r,h.countryInnerBorderColor.g,h.countryInnerBorderColor.b,1);
                                    batch.draw(h.countryInnerBorderTile.getTextureRegion(),
                                            h.source_x-h.countryInnerBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y-h.countryInnerBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.countryInnerBorderTile.getW(),h.countryInnerBorderTile.getH(), scale, scale, 0);
                                }
                                if(h.ifMoreCountryBorder){


                                    if (h.countryBorderColor1!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor1.r,h.countryBorderColor1.g,h.countryBorderColor1.b,1f);
                                        //       color=smapDao.getOppositeColorForRegion(borderId2,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                        //    batch.setColor(color.r,color.g,color.b,1);

                                        batch.draw(cam.rescource.countryBorderDAO11.getTextureRegion(),
                                                h.source_x - cam.rescource.countryBorderDAO11.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO11.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO11.getW(), cam.rescource.countryBorderDAO11.getH(),  scale, scale, 0);
                                    }
                                    if (h.countryBorderColor2!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor2.r,h.countryBorderColor2.g,h.countryBorderColor2.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO12.getTextureRegion(),
                                                h.source_x - cam.rescource.countryBorderDAO12.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO12.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO12.getW(), cam.rescource.countryBorderDAO12.getH(),  scale, scale, 0);
                                    }
                                    if (h.countryBorderColor3!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor3.r,h.countryBorderColor3.g,h.countryBorderColor3.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO13.getTextureRegion(),
                                                h.source_x - cam.rescource.countryBorderDAO13.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO13.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO13.getW(), cam.rescource.countryBorderDAO13.getH(),  scale, scale, 0);
                                    }
                                    if (h.countryBorderColor4!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor4.r,h.countryBorderColor4.g,h.countryBorderColor4.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO14.getTextureRegion(),
                                                h.source_x - cam.rescource.countryBorderDAO14.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO14.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO14.getW(), cam.rescource.countryBorderDAO14.getH(),  scale, scale, 0);
                                    }



                                    if (h.countryBorderColor5!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor5.r,h.countryBorderColor5.g,h.countryBorderColor5.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO15.getTextureRegion(),
                                                h.source_x - cam.rescource.countryBorderDAO15.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO15.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO15.getW(), cam.rescource.countryBorderDAO15.getH(),  scale, scale, 0);
                                    }
                                    if (h.countryBorderColor6!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor6.r,h.countryBorderColor6.g,h.countryBorderColor6.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO16.getTextureRegion(),
                                                h.source_x - cam.rescource.countryBorderDAO16.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO16.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO16.getW(), cam.rescource.countryBorderDAO16.getH(),  scale, scale, 0);
                                    }

                                }else{
                                    if(h.countryOuterBorderTile!=null&&h.countryOuterBorderColor!=null){
                                        batch.setColor(h.countryOuterBorderColor.r,h.countryOuterBorderColor.g,h.countryOuterBorderColor.b,1f);
                                        batch.draw(h.countryOuterBorderTile.getTextureRegion(),
                                                h.source_x-h.countryOuterBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y-h.countryOuterBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                                ,0,0,h.countryOuterBorderTile.getW(),h.countryOuterBorderTile.getH(), scale, scale, 0);
                                    }
                                }
                                /**/
                            }

                            if(cam.loopState==1||cam.loopState==2){

                                if(h.countryInnerBorderTile!=null&&h.countryInnerBorderColor!=null){
                                    batch.setColor(h.countryInnerBorderColor.r,h.countryInnerBorderColor.g,h.countryInnerBorderColor.b,1);
                                    batch.draw(h.countryInnerBorderTile.getTextureRegion(),
                                            cam.getMapW_px()+h.source_x-h.countryInnerBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y-h.countryInnerBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.countryInnerBorderTile.getW(),h.countryInnerBorderTile.getH(), scale, scale, 0);
                                }
                                if(h.ifMoreCountryBorder){


                                    if (h.countryBorderColor1!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor1.r,h.countryBorderColor1.g,h.countryBorderColor1.b,1f);
                                        //       color=smapDao.getOppositeColorForRegion(borderId2,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                        //    batch.setColor(color.r,color.g,color.b,1);

                                        batch.draw(cam.rescource.countryBorderDAO11.getTextureRegion(),
                                                cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO11.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO11.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO11.getW(), cam.rescource.countryBorderDAO11.getH(),  scale, scale, 0);
                                    }
                                    if (h.countryBorderColor2!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor2.r,h.countryBorderColor2.g,h.countryBorderColor2.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO12.getTextureRegion(),
                                                cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO12.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO12.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO12.getW(), cam.rescource.countryBorderDAO12.getH(),  scale, scale, 0);
                                    }
                                    if (h.countryBorderColor3!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor3.r,h.countryBorderColor3.g,h.countryBorderColor3.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO13.getTextureRegion(),
                                                cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO13.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO13.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO13.getW(), cam.rescource.countryBorderDAO13.getH(),  scale, scale, 0);
                                    }
                                    if (h.countryBorderColor4!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor4.r,h.countryBorderColor4.g,h.countryBorderColor4.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO14.getTextureRegion(),
                                                cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO14.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO14.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO14.getW(), cam.rescource.countryBorderDAO14.getH(),  scale, scale, 0);
                                    }



                                    if (h.countryBorderColor5!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor5.r,h.countryBorderColor5.g,h.countryBorderColor5.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO15.getTextureRegion(),
                                                cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO15.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO15.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO15.getW(), cam.rescource.countryBorderDAO15.getH(),  scale, scale, 0);
                                    }
                                    if (h.countryBorderColor6!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                        batch.setColor(h.countryBorderColor6.r,h.countryBorderColor6.g,h.countryBorderColor6.b,1f);
                                        batch.draw(cam.rescource.countryBorderDAO16.getTextureRegion(),
                                                cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO16.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - cam.rescource.countryBorderDAO16.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, cam.rescource.countryBorderDAO16.getW(), cam.rescource.countryBorderDAO16.getH(),  scale, scale, 0);
                                    }

                                }else{
                                    if(h.countryOuterBorderTile!=null&&h.countryOuterBorderColor!=null){
                                        batch.setColor(h.countryOuterBorderColor.r,h.countryOuterBorderColor.g,h.countryOuterBorderColor.b,1f);
                                        batch.draw(h.countryOuterBorderTile.getTextureRegion(),
                                                cam.getMapW_px()+h.source_x-h.countryOuterBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y-h.countryOuterBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                                ,0,0,h.countryOuterBorderTile.getW(),h.countryOuterBorderTile.getH(), scale, scale, 0);
                                    }
                                }
                        }
                    }
                }
            }


            if(ifDrawLegionName&&game.gameConfig.ifEffect){

                //绘制势力文字 国家名
                x=cam.csx-1;
                if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                    x=x-smapDao.masterData.getWidth();
                }
                y=cam.csy-1;
                int cw=cam.cw+2;
                int ch=cam.ch+3;
                for ( int i=0;i<cam.rescource.legionDatas.size;i++) {
                    Fb2Smap.LegionData l=smapDao.getLegionDataByLi(cam.rescource.legionDatas.get(i));
                    if(l==null){
                        break;
                    }
                    if(l.varRegionCount==0||l.getLegionIndex()==0){
                        continue;
                    }
                    if(l.legionNameScale!=0&&l.legionNameIntersect(x,y,cw,ch,cam.loopState)){
                        game.gameConfig.gameFont.getData().setScale(l.legionNameScale);
                        if(game.gameConfig.ifColor &&!game.getSMapDAO().ifHaveImageBg()){
                            color=l.getColorByAlly2();
                        }else {
                            color=l.getReallyFogColor();
                        }
                        game.gameConfig.gameFont.setColor(   color.r, color.g, color.b, 0.5f);
                        if (l.varW>=l.varH) {
                            if(cam.loopState==0||cam.loopState==1){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,l.centerPx_x , l.centerPx_y+ game.gameConfig.gameFont.getCapHeight()/2,30, Align.center,false);
                            }
                            if(cam.loopState==1||cam.loopState==2){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,cam.getMapW_px()+l.centerPx_x , l.centerPx_y+ game.gameConfig.gameFont.getCapHeight()/2,30, Align.center,false);
                            }
                        }else{
                            if(cam.loopState==0||cam.loopState==1){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,l.centerPx_x, l.centerPx_y+  game.gameConfig.gameFont.getCapHeight()*(l.legionName.length()-1)   ,5*l.legionNameScale, Align.center,true);
                            }
                            if(cam.loopState==1||cam.loopState==2){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,cam.getMapW_px()+l.centerPx_x , l.centerPx_y+  game.gameConfig.gameFont.getCapHeight()*(l.legionName.length()-1)   ,5*l.legionNameScale, Align.center,true);
                            }
                        }
                        game.gameConfig.gameFont.getData().setScale(1f*scale);
                        game.gameConfig.gameFont.setColor(Color.WHITE);
                    }
                }
            }
        }
    }

    //不绘制底图
    public static void drawMapTileForMap(MainGame game,Batch batch,Fb2Smap smapDao,  CamerDAO cam, float scale,  int mapW_px, int mapH_px,int showType,boolean ifDrawLegionName,int flashRegion,float alphaRate) {
        if (cam.rescource!=null&&cam.rescource.allMapHexagons!=null) {
            //smapDao.setHexagonDataIfDrawIsFalse();
            int x = 0;
            int y = 0;
            //int size=cam.rescource.allMapHexagons.size;
            //先绘制海岸
            //  Gdx.app.log("setColor1","begin"+" ");
            boolean isEditMode=smapDao.isEditMode(true);

            Color color;
            if( (cam.rescource.drawType==0 ||(cam.rescource.drawType==1 &&smapDao.getGame().gameConfig.ifEffect))&&cam.rescource.tileMapHexagons.size>0){
                for (int i = 0; i<cam.rescource.tileMapHexagons.size; i++) {

                    if(i>=cam.rescource.tileMapHexagons.size){break;}
                    Fb2Map.MapHexagon h=cam.rescource.tileMapHexagons.get(i);
                    if(h==null){break;}
                    if(h.getHexagonIndex()==36586){
                        int s=0;
                    }
                    //Gdx.app.log("海陆绘制圆", "" + id);
                    if (cam.loopState == 0 || cam.loopState == 1) {
                        if(smapDao.ifSystemEffective(22)) {
                            if (h.railwayTile != null) {
                                    if (h.ifRailWayMark) {
                                        batch.draw(h.railwayTile.getTextureRegion(),
                                                h.source_x - h.railwayTile.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                                h.source_y - h.railwayTile.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                                , 0, 0, h.railwayTile.getW(), h.railwayTile.getH(), 1, 1, 0); /**/
                                    }
                            } else if (isEditMode && h.getPresetRailway() == 1) {
                                batch.draw(cam.rescource.preRailwayRegionDAO.getTextureRegion(),
                                        h.source_x - cam.rescource.preRailwayRegionDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                        h.source_y - cam.rescource.preRailwayRegionDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                        , 0, 0, cam.rescource.preRailwayRegionDAO.getW(), cam.rescource.preRailwayRegionDAO.getH(),  scale, scale, 0);
                            }
                        }
                    }
                    if (cam.loopState == 1 || cam.loopState == 2) {
                        if(smapDao.ifSystemEffective(22)){
                            if (h.railwayTile != null) {
                                if (h.ifRailWayMark) {
                                    batch.draw(h.railwayTile.getTextureRegion(),
                                            mapW_px+h.source_x - h.railwayTile.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - h.railwayTile.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, h.railwayTile.getW(), h.railwayTile.getH(), 1, 1, 0); /**/
                                }
                            }else if(isEditMode&&h.getPresetRailway()==1){
                                batch.draw(cam.rescource.preRailwayRegionDAO.getTextureRegion(),
                                        mapW_px+h.source_x   - cam.rescource.preRailwayRegionDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                        h.source_y  - cam.rescource.preRailwayRegionDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                        , 0, 0, cam.rescource.preRailwayRegionDAO.getW(), cam.rescource.preRailwayRegionDAO.getH(), 1, 1, 0);
                            }
                        }
                    }
                }
            }


            //绘制颜色
            for (int i=0;i<cam.rescource.allMapHexagons.size;i++) {

                if (i >= cam.rescource.allMapHexagons.size) {
                    break;
                }
                Fb2Map.MapHexagon h = cam.rescource.allMapHexagons.get(i);
                if (h == null) {
                    break;
                }
                float addA= 0;
                if(h.getRegionId()==flashRegion){
                    addA= 0.1f+cam.getAlphaFlash()/2;
                }else {
                    addA= cam.rescource.drawType*0.1f;
                }
                if( h.getRegionId()==flashRegion ||(cam.rescource.drawType!=0&&!h.getRegionHexagonData().isSea())|| (cam.rescource.drawType==0&&!h.isSea())||h.facilityData!=null){

                    if (h.getRegionId() != -1) {
                        if(showType==1){
                            color=DefDAO.getColorForCamp(h.getCamp());
                        }else {
                            color = smapDao.getColorForRegion(h.getHexagonIndex(), true, true,smapDao.masterData.getPlayerMode()==2);
                        }
                    } else {
                        color = Color.CLEAR;
                    }
                    if(h.getRegionId()==flashRegion){
                        batch.setColor(color.r-cam.getAlphaFlash()/3,color.g-cam.getAlphaFlash()/3,color.b-cam.getAlphaFlash()/3,color.a/3+addA*0.7f);
                    }else {
                        if(showType==1){
                            batch.setColor(color.r,color.g,color.b,(color.a/3+addA)*0.5f);
                        }else{
                            batch.setColor(color.r,color.g,color.b,(color.a*alphaRate));
                        }
                        if(h.isSea()&&h.ifSeaLand){
                            batch.setColor( Color.CLEAR);
                        }
                    }

                    if ( ( cam.rescource.drawType==1||cam.rescource.drawType==2)  &&h.getBuildData().ifDrawWarLine()) {
                        if (cam.loopState == 0 || cam.loopState == 1) {
                            batch.draw(cam.rescource.hexagonDAO1.getTextureRegion(),
                                    h.source_x - cam.rescource.hexagonDAO1.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO1.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO1.getW(), cam.rescource.hexagonDAO1.getH(),  scale, scale, 0);

                        }
                        if (cam.loopState == 1 || cam.loopState == 2) {
                            batch.draw(cam.rescource.hexagonDAO1.getTextureRegion(),
                                    mapW_px + h.source_x - cam.rescource.hexagonDAO1.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO1.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO1.getW(), cam.rescource.hexagonDAO1.getH(),  scale, scale, 0);

                        }
                    } else {

                        if (cam.loopState == 0 || cam.loopState == 1) {
                            batch.draw(cam.rescource.hexagonDAO.getTextureRegion(),
                                    h.source_x - cam.rescource.hexagonDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO.getW(), cam.rescource.hexagonDAO.getH(),  scale, scale, 0);
                        }
                        if (cam.loopState == 1 || cam.loopState == 2) {
                            batch.draw(cam.rescource.hexagonDAO.getTextureRegion(),
                                    mapW_px + h.source_x - cam.rescource.hexagonDAO.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y - cam.rescource.hexagonDAO.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                    , 0, 0, cam.rescource.hexagonDAO.getW(), cam.rescource.hexagonDAO.getH(),  scale, scale, 0);
                        }
                        batch.setColor(Color.WHITE);
                    }
                }
                if(cam.rescource.drawType==0 ){
                    if(h.countryLineBorderTile!=null){

                        if(cam.loopState==0||cam.loopState==1){
                            batch.draw(h.countryLineBorderTile.getTextureRegion(),
                                    h.source_x-h.countryLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y-h.countryLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.countryLineBorderTile.getW(),h.countryLineBorderTile.getH(), scale, scale, 0);
                        }

                        if(cam.loopState==1||cam.loopState==2){
                            batch.draw(h.countryLineBorderTile.getTextureRegion(),
                                    mapW_px+h.source_x-h.countryLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y-h.countryLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.countryLineBorderTile.getW(),h.countryLineBorderTile.getH(), scale, scale, 0);
                        }
                    }
                }
                if(cam.rescource.drawType<2  ){
                    if(h.regionLineBorderTile!=null){
                        batch.setColor(Color.WHITE);
                        if(cam.loopState==0||cam.loopState==1){
                            batch.draw(h.regionLineBorderTile.getTextureRegion(),
                                    h.source_x+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.regionLineBorderTile.getW(),h.regionLineBorderTile.getH(), scale, scale, 0);
                        }

                        if(cam.loopState==1||cam.loopState==2){
                            batch.draw(h.regionLineBorderTile.getTextureRegion(),
                                    mapW_px+h.source_x+h.getBackRefX()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                    h.source_y+h.getBackRefY()* ResDefaultConfig.Map.MAP_SCALE-h.regionLineBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                    ,0,0,h.regionLineBorderTile.getW(),h.regionLineBorderTile.getH(), scale, scale, 0);

                        }
                    }
                }
            }

            //绘制国家边界
            if( cam.rescource.drawType>0&&smapDao.getGame().gameConfig.ifEffect){

                for (int i=0;i<cam.rescource.allMapHexagons.size;i++) {

                    if(i>=cam.rescource.allMapHexagons.size){break;}
                    Fb2Map.MapHexagon h=cam.rescource.allMapHexagons.get(i);
                    if(h==null){break;}
                    if(h.getHexagonIndex()==36586){
                        int s=0;
                    }
                    if(h.countryBorder!=0){
                        if(cam.loopState==0||cam.loopState==1){
                            if(h.countryInnerBorderTile!=null&&h.countryInnerBorderColor!=null){
                                batch.setColor(h.countryInnerBorderColor.r,h.countryInnerBorderColor.g,h.countryInnerBorderColor.b,1);
                                batch.draw(h.countryInnerBorderTile.getTextureRegion(),
                                        h.source_x-h.countryInnerBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                        h.source_y-h.countryInnerBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                        ,0,0,h.countryInnerBorderTile.getW(),h.countryInnerBorderTile.getH(), scale, scale, 0);
                            }
                            if(h.ifMoreCountryBorder){


                                if (h.countryBorderColor1!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor1.r,h.countryBorderColor1.g,h.countryBorderColor1.b,1f);
                                    //       color=smapDao.getOppositeColorForRegion(borderId2,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                    //    batch.setColor(color.r,color.g,color.b,1);

                                    batch.draw(cam.rescource.countryBorderDAO11.getTextureRegion(),
                                            h.source_x - cam.rescource.countryBorderDAO11.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO11.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO11.getW(), cam.rescource.countryBorderDAO11.getH(),  scale, scale, 0);
                                }
                                if (h.countryBorderColor2!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor2.r,h.countryBorderColor2.g,h.countryBorderColor2.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO12.getTextureRegion(),
                                            h.source_x - cam.rescource.countryBorderDAO12.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO12.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO12.getW(), cam.rescource.countryBorderDAO12.getH(),  scale, scale, 0);
                                }
                                if (h.countryBorderColor3!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor3.r,h.countryBorderColor3.g,h.countryBorderColor3.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO13.getTextureRegion(),
                                            h.source_x - cam.rescource.countryBorderDAO13.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO13.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO13.getW(), cam.rescource.countryBorderDAO13.getH(),  scale, scale, 0);
                                }
                                if (h.countryBorderColor4!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor4.r,h.countryBorderColor4.g,h.countryBorderColor4.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO14.getTextureRegion(),
                                            h.source_x - cam.rescource.countryBorderDAO14.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO14.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO14.getW(), cam.rescource.countryBorderDAO14.getH(),  scale, scale, 0);
                                }



                                if (h.countryBorderColor5!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor5.r,h.countryBorderColor5.g,h.countryBorderColor5.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO15.getTextureRegion(),
                                            h.source_x - cam.rescource.countryBorderDAO15.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO15.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO15.getW(), cam.rescource.countryBorderDAO15.getH(),  scale, scale, 0);
                                }
                                if (h.countryBorderColor6!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor6.r,h.countryBorderColor6.g,h.countryBorderColor6.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO16.getTextureRegion(),
                                            h.source_x - cam.rescource.countryBorderDAO16.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO16.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO16.getW(), cam.rescource.countryBorderDAO16.getH(),  scale, scale, 0);
                                }

                            }else{
                                if(h.countryOuterBorderTile!=null&&h.countryOuterBorderColor!=null){
                                    batch.setColor(h.countryOuterBorderColor.r,h.countryOuterBorderColor.g,h.countryOuterBorderColor.b,1f);
                                    batch.draw(h.countryOuterBorderTile.getTextureRegion(),
                                            h.source_x-h.countryOuterBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y-h.countryOuterBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.countryOuterBorderTile.getW(),h.countryOuterBorderTile.getH(), scale, scale, 0);
                                }
                            }
                            /**/
                        }

                        if(cam.loopState==1||cam.loopState==2){

                            if(h.countryInnerBorderTile!=null&&h.countryInnerBorderColor!=null){
                                batch.setColor(h.countryInnerBorderColor.r,h.countryInnerBorderColor.g,h.countryInnerBorderColor.b,1);
                                batch.draw(h.countryInnerBorderTile.getTextureRegion(),
                                        cam.getMapW_px()+h.source_x-h.countryInnerBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                        h.source_y-h.countryInnerBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                        ,0,0,h.countryInnerBorderTile.getW(),h.countryInnerBorderTile.getH(), scale, scale, 0);
                            }
                            if(h.ifMoreCountryBorder){


                                if (h.countryBorderColor1!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor1.r,h.countryBorderColor1.g,h.countryBorderColor1.b,1f);
                                    //       color=smapDao.getOppositeColorForRegion(borderId2,false,defaultColor);//1↖ 2上 3↗ 4↙ 5↓ 6↘
                                    //    batch.setColor(color.r,color.g,color.b,1);

                                    batch.draw(cam.rescource.countryBorderDAO11.getTextureRegion(),
                                            cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO11.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO11.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO11.getW(), cam.rescource.countryBorderDAO11.getH(),  scale, scale, 0);
                                }
                                if (h.countryBorderColor2!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor2.r,h.countryBorderColor2.g,h.countryBorderColor2.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO12.getTextureRegion(),
                                            cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO12.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO12.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO12.getW(), cam.rescource.countryBorderDAO12.getH(),  scale, scale, 0);
                                }
                                if (h.countryBorderColor3!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor3.r,h.countryBorderColor3.g,h.countryBorderColor3.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO13.getTextureRegion(),
                                            cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO13.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO13.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO13.getW(), cam.rescource.countryBorderDAO13.getH(),  scale, scale, 0);
                                }
                                if (h.countryBorderColor4!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor4.r,h.countryBorderColor4.g,h.countryBorderColor4.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO14.getTextureRegion(),
                                            cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO14.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO14.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO14.getW(), cam.rescource.countryBorderDAO14.getH(),  scale, scale, 0);
                                }



                                if (h.countryBorderColor5!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor5.r,h.countryBorderColor5.g,h.countryBorderColor5.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO15.getTextureRegion(),
                                            cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO15.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO15.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO15.getW(), cam.rescource.countryBorderDAO15.getH(),  scale, scale, 0);
                                }
                                if (h.countryBorderColor6!=null) {//1↗ 2→ 3↘ 4↙ 5← 6↖
                                    batch.setColor(h.countryBorderColor6.r,h.countryBorderColor6.g,h.countryBorderColor6.b,1f);
                                    batch.draw(cam.rescource.countryBorderDAO16.getTextureRegion(),
                                            cam.getMapW_px()+h.source_x - cam.rescource.countryBorderDAO16.getRefx() * ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y - cam.rescource.countryBorderDAO16.getRefy() * ResDefaultConfig.Map.MAP_SCALE
                                            , 0, 0, cam.rescource.countryBorderDAO16.getW(), cam.rescource.countryBorderDAO16.getH(),  scale, scale, 0);
                                }

                            }else{
                                if(h.countryOuterBorderTile!=null&&h.countryOuterBorderColor!=null){
                                    batch.setColor(h.countryOuterBorderColor.r,h.countryOuterBorderColor.g,h.countryOuterBorderColor.b,1f);
                                    batch.draw(h.countryOuterBorderTile.getTextureRegion(),
                                            cam.getMapW_px()+h.source_x-h.countryOuterBorderTile.getRefx()* ResDefaultConfig.Map.MAP_SCALE,
                                            h.source_y-h.countryOuterBorderTile.getRefy()* ResDefaultConfig.Map.MAP_SCALE
                                            ,0,0,h.countryOuterBorderTile.getW(),h.countryOuterBorderTile.getH(), scale, scale, 0);
                                }
                            }
                        }
                    }
                }
            }


            if(ifDrawLegionName&&game.gameConfig.ifEffect){

                //绘制势力文字 国家名
                x=cam.csx-1;
                if(smapDao.ifLoop&&x>smapDao.masterData.getWidth()){
                    x=x-smapDao.masterData.getWidth();
                }
                y=cam.csy-1;
                int cw=cam.cw+2;
                int ch=cam.ch+3;
                for ( int i=0;i<cam.rescource.legionDatas.size;i++) {
                    Fb2Smap.LegionData l=smapDao.getLegionDataByLi(cam.rescource.legionDatas.get(i));
                    if(l==null){
                        break;
                    }
                    if(l.varRegionCount==0||l.getLegionIndex()==0){
                        continue;
                    }
                    if(l.legionNameScale!=0&&l.legionNameIntersect(x,y,cw,ch,cam.loopState)){
                        game.gameConfig.gameFont.getData().setScale(l.legionNameScale);
                        if(game.gameConfig.ifColor &&!game.getSMapDAO().ifHaveImageBg()){
                            color=l.getReallyFogColor();
                        }else {
                            color=l.getColorByAlly2();
                        }
                        if(cam.rescource.drawType==3){
                            game.gameConfig.gameFont.setColor(   color.r, color.g, color.b, 0.8f);
                        }else {
                            game.gameConfig.gameFont.setColor(   color.r, color.g, color.b, 0.5f);
                        }

                        if (l.varW>=l.varH) {
                            if(cam.loopState==0||cam.loopState==1){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,l.centerPx_x , l.centerPx_y+ game.gameConfig.gameFont.getCapHeight()/2,30, Align.center,false);
                            }
                            if(cam.loopState==1||cam.loopState==2){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,cam.getMapW_px()+l.centerPx_x , l.centerPx_y+ game.gameConfig.gameFont.getCapHeight()/2,30, Align.center,false);
                            }
                        }else{
                            if(cam.loopState==0||cam.loopState==1){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,l.centerPx_x, l.centerPx_y+  game.gameConfig.gameFont.getCapHeight()*(l.legionName.length()-1)   ,5*l.legionNameScale, Align.center,true);
                            }
                            if(cam.loopState==1||cam.loopState==2){
                                game.gameConfig.gameFont.draw(batch,l.countryNameStr,cam.getMapW_px()+l.centerPx_x , l.centerPx_y+  game.gameConfig.gameFont.getCapHeight()*(l.legionName.length()-1)   ,5*l.legionNameScale, Align.center,true);
                            }
                        }
                        game.gameConfig.gameFont.getData().setScale(1f*scale);
                        game.gameConfig.gameFont.setColor(Color.WHITE);
                    }
                }
            }
        }
    }

}
