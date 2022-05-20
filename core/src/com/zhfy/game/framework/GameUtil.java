package com.zhfy.game.framework;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.model.content.DefDAO;
import com.zhfy.game.model.content.conversion.Fb2Map;
import com.zhfy.game.model.content.conversion.Fb2Smap;
import com.zhfy.game.model.framework.TextureRegionDAO;
import com.zhfy.game.model.framework.TextureRegionListDAO;
import com.zhfy.game.screen.actor.framework.ScrollLabel;
import com.zhfy.game.screen.actor.framework.WindowGroup;
import com.zhfy.game.screen.actor.framework.ComActor;

public class GameUtil {
    private static final Locale ROOT_LOCALE = new Locale("", "", "");
    private  static final GlyphLayout layout =new GlyphLayout();
    //本工具类主要存储涉及到libgdx的类


    private GameUtil() {
        throw new IllegalStateException("GameUtil class");
    }

    //泛型方法 根据传入的类型,返回list类型
    public static <T> List<T> getDaoListByClass(MainGame game, T item, String path) throws Exception {
        List<T> ts = new ArrayList<T>();
        Class clazz = item.getClass();
        XmlReader reader = game.gameConfig.reader;
        Element root = reader.parse(Gdx.files.internal(path));
        Array<Element> elements = root.getChildrenByNameRecursively(root.getChild(0).getName());
        Field[] fieldName;
        Class clazs;
        Field f;
        //获得条目属性的数量,然后通过反射,把值给了类

        // MethodAccess access = MethodAccess.get(clazz);

        for (int e = 0, eMax = elements.size; e < eMax; e++) {
            fieldName = clazz.getDeclaredFields();
            item = (T) clazz.newInstance();
            clazs = item.getClass();
            for (int i = 0, iMax = fieldName.length; i < iMax; i++) {
                // 创建实例
                f = clazs.getDeclaredField(fieldName[i].getName());
                f.setAccessible(true);
                if (f.getType().getName().equals(String.class.getName())) {
                    String str = elements.get(e).get(fieldName[i].getName());
                    f.set(item, str);
                    // access.invoke(item, "set"+ComUtil.captureName(fieldName[i].getName()), str);
                } else if (f.getType().getName().equals(int.class.getName())) {
                    int str = elements.get(e).getInt(fieldName[i].getName());
                    f.set(item, str);
                    // access.invoke(item, "set"+ComUtil.captureName(fieldName[i].getName()), str);
                } else if (f.getType().getName().equals(float.class.getName())) {
                    float str = elements.get(e).getFloat(fieldName[i].getName());
                    f.set(item, str);
                    //access.invoke(item, "set"+ComUtil.captureName(fieldName[i].getName()), str);
                } else if (f.getType().getName().equals(boolean.class.getName())) {
                    boolean str = elements.get(e).getBoolean(fieldName[i].getName());
                    f.set(item, str);
                    //access.invoke(item, "set"+ComUtil.captureName(fieldName[i].getName()), str);
                }
            }
            ts.add(item);
            //ts.add(tempItem);
        }
        return ts;
    }


    public static Pixmap getPixMapFromRegion(TextureRegion region) {
        Texture texture = region.getTexture();
        TextureData data = texture.getTextureData();
        if (!data.isPrepared()) {
            data.prepare();
        }
        Pixmap pixmap = data.consumePixmap();
        int width = region.getRegionWidth();
        int height = region.getRegionHeight();
        Pixmap px = new Pixmap(width, height, Format.RGBA4444);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int colorInt = pixmap.getPixel(region.getRegionX() + x,
                        region.getRegionY() + y);
            }
        }
        return px;
    }

    //获取颜色rgba
    public static int toIntColor(int r, int g, int b, int a) {
        return (r << 24) | (g << 16) | (b << 8) | a;
    }

    //根据一串数字随机生成一种颜色码值
    public static int getColorByNum(int num) {
        int r = num * 30 % 255;
        int g = (num * 10 / 255) % 255;
        int b = (r * g * 7) % 255;
        int a = 120 + num % 50;
        return toIntColor(r, g, b, a);

    }


    public static void setColorByNum(Batch batch, Pixmap pixmap, int num) {
        int r = num * 30 % 255;
        int g = (num * 10 / 255) % 255;
        int b = (r * g * 7) % 255;
        int a = 120 + num % 50;
        int value = toIntColor(r, g, b, a);
        float r1 = ((value & 0xff000000) >>> 24) / 255f;
        float g1 = ((value & 0x00ff0000) >>> 16) / 255f;
        float b1 = ((value & 0x0000ff00) >>> 8) / 255f;
        float a1 = ((value & 0x000000ff)) / 255f;

        if (batch != null) {
            batch.setColor(r1, g1, b1, a1);
        }

        if (pixmap != null) {
            pixmap.setColor(r1, g1, b1, a1);
        }

    }

    public static void setColorForSR(Batch batch, Pixmap pixmap, int num, int a) {
        float i = num;
        if (i < 10) i = i * 302.3f;
        if (i < 100) i = i * 31.2f;
        for (; i > 255; i *= 0.98) ;
        String temp = (i + "").substring((i + "").length() - 3);
        i += (int) (Float.parseFloat(temp));


        for (; i > 255; i -= 255) ;
        if (i < 10) i += 10;

        float R = i * (i / 100);
        for (; R > 255; R -= 255) ;
        if (R < 50) R += 60;

        float G = i * (i % 100);
        for (; G > 255; G -= 255) ;
        if (G < 50) G += 60;

        float B = i * (i % 10);
        for (; B > 255; B -= 255) ;

        //toIntColor((int)R, (int)G, (int)B, 150)
        int value = toIntColor((int) R, (int) G, (int) B, a == -1 ? 150 : a);
        float r1 = ((value & 0xff000000) >>> 24) / 255f;
        float g1 = ((value & 0x00ff0000) >>> 16) / 255f;
        float b1 = ((value & 0x0000ff00) >>> 8) / 255f;
        float a1 = ((value & 0x000000ff)) / 255f;

        if (batch != null) {
            batch.setColor(r1, g1, b1, a1);
        }

        if (pixmap != null) {
            pixmap.setColor(r1, g1, b1, a1);
        }
    }


    public static void setInverseColor(Color color) {
        color.r = (255 - color.r);
        color.g = (255 - color.g);
        color.b = (255 - color.b);
        //color.a=(255-color.a);
    }

    public static Color changeBright(Color color, float brightness) {
        float r = color.r;
        float g = color.g;
        float b = color.b;
        float h;
        float s;
        float v;
        float min, max, delta;
        min = ComUtil.min(r, ComUtil.min(g, b));
        max = ComUtil.max(r, ComUtil.max(g, b));
        v = max;               // v
        delta = max - min;
        if (max != 0)
            s = delta / max;       // s
        else {
            // r = g = b = 0        // s = 0, v is undefined
            s = 0;
            h = -1;
            return color.fromHsv(h, s, v * brightness);
        }
        if (r == max)
            h = (g - b) / delta;     // between yellow & magenta
        else if (g == max)
            h = 2 + (b - r) / delta; // between cyan & yellow
        else
            h = 4 + (r - g) / delta; // between magenta & cyan
        h *= 60;               // degrees
        if (h < 0)
            h += 360;
        return color.fromHsv(h, s, v * brightness);
    }

    //根据文件路径打包散图 path为一个文件夹位置  picLv打包等级
    public static TextureAtlas packPicByPath(String path, int picLv) {
        int picSide = ResDefaultConfig.Map.PACK_PIC_SIDE;
        boolean isDirectory = Gdx.files.external(path).isDirectory();
        PixmapPacker packer = new PixmapPacker(picLv * picSide, picLv * picSide, Format.RGBA8888, 2, true);
        TextureAtlas textureAtlas = null;
        if (isDirectory) {
            FileHandle[] files = Gdx.files.local(path).list();
            for (FileHandle file : files) {
                packer.pack(file.name(), new Pixmap(Gdx.files.internal(file.path())));
            }
            textureAtlas = packer.generateTextureAtlas(TextureFilter.Nearest, TextureFilter.Nearest, false);
            //am.load(packer.generateTextureAtlas( TextureFilter.Nearest, TextureFilter.Nearest, false ),TextureAtlas.class);

        } else {
            Gdx.app.log("警告:资源加载", path + " 不是一个文件夹路径");
        }
        return textureAtlas;
    }


    //根据规则配置加载资源 config_res 仅仅在初始的时候加载相关资源
    public static AssetManager loadResByConfig(MainGame game, AssetManager am, int resId) {
        Element xmlFile = game.gameConfig.getCONFIG_RES().getElementById(resId);
        for (int j = 0, jMax = xmlFile.getChildCount(); j < jMax; j++) {
            loadRes(game, am, xmlFile.getChild(j));
        }
        return am;
    }

    //通过resId获取xml的元素
    public static List<Element> getXmlEByResId(MainGame game, int resId) {
        List<Element> rs = new ArrayList<Element>();
        Element xmlFile = game.gameConfig.getCONFIG_RES().getElementById(resId);
        for (int j = 0, jMax = xmlFile.getChildCount(); j < jMax; j++) {
            rs.add(xmlFile.getChild(j));
        }
        return rs;
    }

    //根据xmlE来加载资源
    public static void loadResByXmlE(MainGame game, AssetManager am, List<Element> e) {
        for (Element xmlFile : e) {
            loadRes(game, am, xmlFile);
        }
    }

    public static void loadRes(MainGame game, AssetManager am, Element xmlFile) {
        XmlReader reader = game.gameConfig.reader;
        StringBuilder path = new StringBuilder();
        /* boolean ifHd=game.gameConfig.getIfAnimation();*/
        String resPath = null;
        if (xmlFile.getBoolean("ifOnlyLoadInEffect", false) && !game.gameConfig.ifEffect) {
            return;
        }

        boolean ifOriginal = xmlFile.getBoolean("ifOriginal");
        if (ifOriginal) {
            resPath = xmlFile.get("res");
        } else {
            resPath = game.gameConfig.getModPath() + xmlFile.get("res");
        }
        String type = xmlFile.get("type");
        if (type.equals("gif")) {
            am.load(resPath, Animation.class);
        } else if ((type.equals("texture") || type.equals("textures"))) {
            am.load(resPath, Texture.class);
            // Gdx.app.log("加载图片资源", xmlFile.get("res"));
        } /*else if (type.equals("atlasP")&&ifLoadRes(xmlFile,ifHd)) {
            path.delete(0, path.length());
            path.append(resPath);
            path.append("/").append(xmlFile.get("name")).append(".xml");
            //Element altas = reader.parse(Gdx.files.internal(xmlFile.get("res")+"/" + xmlFile.get("name") + ".xml"));
            Element altas = reader.parse(Gdx.files.internal(path.toString()));

            // 每个图片添加的时候都要加使用场景,多场景用;分割screenid="1"
            Array<Element> images = altas.getChildrenByNameRecursively("sprite");
            for (int i = 0, iMax = images.size; i < iMax; i++) {
                am.load(resPath + "/" + images.get(i).get("n"), Pixmap.class);

               // Gdx.app.log("加载图集资源",  xmlFile.get("res")+"/" +images.get(i).get("n"));
            }

        }*/ else if (type.equals("atlasT")) {
            path.delete(0, path.length());
            path.append(resPath);
            path.append("/").append(xmlFile.get("name")).append(".xml");
            Element altas = null;
            if (!ifOriginal) {
                // game.gameConfig.checkModFile(new java.lang.StringBuilder(xmlFile.get("res")).append("/").append(xmlFile.get("name")).append(".xml").toString(),path.toString());
                altas = reader.parse(Gdx.files.local(path.toString()));
            } else {
                altas = reader.parse(Gdx.files.internal(path.toString()));
            }


            //Element altas = reader.parse(Gdx.files.internal(xmlFile.get("res")+"/" + xmlFile.get("name") + ".xml"));
            // 每个图片添加的时候都要加使用场景,多场景用;分割screenid="1"
            Array<Element> images = altas.getChildrenByNameRecursively("sprite");
            for (int i = 0, iMax = images.size; i < iMax; i++) {
                am.load(resPath + "/" + images.get(i).get("n"), Texture.class);
                //  Gdx.app.log("加载图集资源",  xmlFile.get("res")+"/" +images.get(i).get("n"));
            }
        } else if ((type.equals("pixmap") || type.equals("pixmaps"))) {
            am.load(resPath, Pixmap.class);
            // Gdx.app.log("加载内存图资源", xmlFile.get("res"));
        } /*else if (type.equals("smallMapP")&&ifLoadRes(xmlFile,ifHd)) {
            xmlFile = game.gameConfig.getDEF_SMALLMAP().getElementById(xmlFile.getInt("res"));
            String imgFileName = xmlFile.get("name");
            for (int j = 0, jMax = xmlFile.getChildCount(); j < jMax; j++) {
                am.load(("image/" + imgFileName + "/" + xmlFile.getChild(j).get("n")), Pixmap.class);
            }
        } else if (type.equals("pixmapZM")&&ifLoadRes(xmlFile,ifHd) ) {
               String floder=resPath+ game.mapFolder+"/";
                if(Gdx.files.local(floder).isDirectory()){
                    FileHandle[] files=Gdx.files.local(floder).list();
                    for(FileHandle file:files){
                        am.load(file.path(), Pixmap.class);
                      //  Gdx.app.log("加载内存图资源", file.path());
                    }
                }else {
                    Gdx.app.error("loadError","errorFloder"+floder);
                }
        } */ else if (type.equals("textureZM")) {

            String floder = resPath + game.mapFolder + "/";
            if (Gdx.files.local(floder).isDirectory()) {
                FileHandle[] files = Gdx.files.local(floder).list();
                for (FileHandle file : files) {
                    am.load(file.path(), Texture.class);
                    //    Gdx.app.log("加载内存图资源", file.path());
                }
            } else {
                Gdx.app.error("loadError", "errorFloder" + floder);
            }
        }
    }


    //根据xmlE来卸载资源  ifUnload 卸载资源类型
    public static void unloadResByXmlE(MainGame game, AssetManager am, List<Element> xmlFile, boolean ifUnload) {
        XmlReader reader = game.gameConfig.reader;
        StringBuilder path = new StringBuilder();
        // boolean ifHd=game.gameConfig.getIfAnimation();
        for (int e = 0, eMax = xmlFile.size(); e < eMax; e++) {
            /*if("bt_tiles".equals(xmlFile.get(e).get("name"))){
                int s=0;
            }*/


            String type = xmlFile.get(e).get("type");
            boolean ifResUnload = xmlFile.get(e).getBoolean("ifUnload");
            String resPath = xmlFile.get(e).get("res");

            //直接卸载
            if ((type.equals("gif") || type.equals("texture") || type.equals("textures")) && ifResUnload == (ifUnload)) {
                if (!am.contains(resPath)) {
                    continue;
                }
                am.unload(resPath);
                //Gdx.app.log("卸载图片资源", resPath);
            } else if (type.equals("atlasT") && ifResUnload == (ifUnload)) {
                path.delete(0, path.length());
                path.append(resPath).append("/").append(xmlFile.get(e).get("name")).append(".xml");
                //Element altas = reader.parse(Gdx.files.internal(resPath+"/" + xmlFile.get(e).get("name") + ".xml"));
                Element altas = reader.parse(Gdx.files.internal(path.toString()));
                // 每个图片添加的时候都要加使用场景,多场景用;分割screenid="1"
                Array<Element> images = altas.getChildrenByNameRecursively("sprite");
                for (int i = 0, iMax = images.size; i < iMax; i++) {
                    String resPathStr = resPath + "/" + images.get(i).get("n");
                    if (!am.contains(resPath)) {
                        continue;
                    }
                    am.unload(resPathStr);
                    Gdx.app.log("卸载图集资源", resPath + "/" + images.get(i).get("n"));
                }
            } else if (type.equals("atlasP") && ifResUnload == (ifUnload)) {//查资源集合
                path.delete(0, path.length());
                path.append(resPath);
                path.append("/").append(xmlFile.get(e).get("name")).append(".xml");
                //Element altas = reader.parse(Gdx.files.internal(resPath+"/" + xmlFile.get(e).get("name") + ".xml"));
                Element altas = reader.parse(Gdx.files.internal(path.toString()));

                // 每个图片添加的时候都要加使用场景,多场景用;分割screenid="1"
                Array<Element> images = altas.getChildrenByNameRecursively("sprite");
                //判断是否存在
                if (!am.contains(resPath + "/" + images.get(0).get("n"))) {
                    continue;
                }
                for (int i = 0, iMax = images.size; i < iMax; i++) {
                    String resPathStr = resPath + "/" + images.get(i).get("n");
                    if (!am.contains(resPath)) {
                        continue;
                    }
                    am.unload(resPathStr);
                    Gdx.app.log("卸载图集资源", resPath + "/" + images.get(i).get("n"));
                }
            } else if (type.equals("smallMapP") && ifResUnload == (ifUnload)) {//查分属文件

                int mapId = xmlFile.get(e).getInt("res");
                Element root = game.gameConfig.getDEF_SMALLMAP().getElementById(mapId);
                String imgFileName = root.get("name");
                for (int j = 0, jMax = root.getChildCount(); j < jMax; j++) {
                    String resPathStr = ("image/" + imgFileName + "/" + root.getChild(j).get("n"));
                    if (!am.contains(resPath)) {
                        continue;
                    }
                    am.unload(resPathStr);
                }
            } else if ((type.equals("pixmapZM") || type.equals("textureZM")) && ifResUnload == (ifUnload)) {//查文件夹

                String floder = resPath + game.mapFolder + "/";
                if (Gdx.files.local(floder).isDirectory()) {
                    FileHandle[] files = Gdx.files.local(floder).list();
                    for (FileHandle file : files) {
                        if (!am.contains(file.path())) {
                            continue;
                        }
                        am.unload(file.path());
                        //  Gdx.app.log("卸载内存地图资源", file.path());
                    }
                } else {
                    Gdx.app.error("loadError", "errorFloder" + floder);
                }


            } else if (xmlFile.get(e).getBoolean("ifUnload") == (ifUnload)) {
                Gdx.app.error("警告,资源未卸载", resPath);
            }
        }
    }


    //根据场景配置,比对资源,剔除多余的,添加未加载的
    public static AssetManager loadResByScreen(MainGame game, AssetManager am, int beforeScreenId, int nowScreenId) {
        //1.先获得当前场景和之前场景对应的资源id
        int nowRsId = -1, befRsId = -1;
        nowRsId = game.gameConfig.getCONFIG_LAYOUT().getElementById(nowScreenId).getInt("resId");
        if (beforeScreenId == -2) {
            befRsId = -2;
        } else {
            befRsId = game.gameConfig.getCONFIG_LAYOUT().getElementById(beforeScreenId).getInt("resId");
        }

        //2.判断前场景的资源值与要加载的场景资源值id是否相同,相同则返回
        if (nowRsId == befRsId) {
            return am;
        } else if (befRsId == -2) {
            List<Element> load = getXmlEByResId(game, nowRsId);
            List<Element> loadc = new ArrayList<Element>();//拷贝元素
            loadc.addAll(load);
            loadResByXmlE(game, am, loadc);
            return am;
        } else {
            //3.如果不同,加载新资源,卸载旧资源
            //unloadResByConfig(am,befRsId);
            //loadResByConfig(am,nowRsId);
            List<Element> load = getXmlEByResId(game, nowRsId);
            List<Element> unload = getXmlEByResId(game, befRsId);
            List<Element> loadc = new ArrayList<Element>();//拷贝元素
            List<Element> unloadc = new ArrayList<Element>();
            loadc.addAll(load);
            unloadc.addAll(unload);

            //旧的且没有用到的数据,卸载  差集 unload.removeAll(load);
            //旧的且用到的,不管 交集
            //新的且没有加载的,加载   差集 load.removeAll(unload);
            //新的且在旧的已加载的,不管 交集
            unload.removeAll(load);
            loadc.removeAll(unloadc);
            unloadResByXmlE(game, am, unload, true);
            loadResByXmlE(game, am, loadc);
            return am;
        }
    }

    //手动卸载指定的screenId的一次性资源
    public static void unloadSingleResByScreenId(MainGame game, AssetManager am, int screenId) {
        int rsId = game.gameConfig.getCONFIG_LAYOUT().getElementById(screenId).getInt("resId");
        //卸载rsId中的一次性资源
        List<Element> e = getXmlEByResId(game, rsId);
        unloadResByXmlE(game, am, e, false);
    }

    //判断资源是否一样,是否需要加载场景
    public static boolean ifNeedLoad(MainGame game, int beforeScreenId, int nowScreenId) {
        //1.先获得当前场景和之前场景对应的资源id
        int nowRsId = -1, befRsId = -1;
        nowRsId = game.gameConfig.getCONFIG_LAYOUT().getElementById(nowScreenId).getInt("resId");

        if (beforeScreenId == -2) {
            befRsId = -2;
        } else {
            befRsId = game.gameConfig.getCONFIG_LAYOUT().getElementById(beforeScreenId).getInt("resId");
        }

        //2.判断前场景的资源值与要加载的场景资源值id是否相同,相同则返回
        if (nowRsId == befRsId || befRsId == 0) {
            return false;
        } else {
            return true;
        }
    }


    // 根据screenid,获取要加载的图片资源  210318
    public static TextureRegionListDAO loadTextureReigonByScreenId(MainGame game, TextureRegionListDAO imgList, int screenId, AssetManager am, boolean ifLoadComRes) {
        // 验证imgLists是否有东西
        // List<String> nameList = getTextureNameByScreenId(game,screenId, "textures",ifLoadComRes);
        if (imgList != null) {
            imgList.clear();
        }

        int resId = game.gameConfig.getCONFIG_LAYOUT().getElementById(screenId).getInt("resId");
        List<String> strs = new ArrayList<String>();
        // boolean ifHd=game.gameConfig.getIfAnimation();
        if (ifLoadComRes) {
            //加载默认通用资源
            Array<Element> xmlFiles = game.gameConfig.getCONFIG_RES().getElementById(0)
                    .getChildrenByNameRecursively("xmlFile");
            for (int e = 0, eMax = xmlFiles.size; e < eMax; e++) {
                if (xmlFiles.get(e).get("type").equals("textures")) {
                    // strs.add(xmlFiles.get(e).get("res"));
                    imgList.addRegionDAO(am, xmlFiles.get(e));
                }
            }
        }


        Array<Element> xmlFiles1 = game.gameConfig.getCONFIG_RES().getElementById(resId).getChildrenByNameRecursively("xmlFile");
        for (int e = 0, eMax = xmlFiles1.size; e < eMax; e++) {
            if (xmlFiles1.get(e).get("type").equals("textures")) {
                //strs.add(xmlFiles1.get(e).get("res"));
                imgList.addRegionDAO(am, xmlFiles1.get(e));
            }
        }


        //imgList.check();
        return imgList;
    }


    // 通过screenId获取要读取的资源
    private static List<String> getTextureNameByScreenId(MainGame game, int screenId, String type, boolean ifLoadDefault) {
        int resId = game.gameConfig.getCONFIG_LAYOUT().getElementById(screenId).getInt("resId");
        List<String> strs = new ArrayList<String>();
        // boolean ifHd=game.gameConfig.getIfAnimation();
        if (ifLoadDefault) {
            //加载默认通用资源
            Array<Element> xmlFiles = game.gameConfig.getCONFIG_RES().getElementById(0)
                    .getChildrenByNameRecursively("xmlFile");
            for (int e = 0, eMax = xmlFiles.size; e < eMax; e++) {
                if (xmlFiles.get(e).get("type").equals(type)) {
                    strs.add(xmlFiles.get(e).get("res"));
                }
            }
        }


        Array<Element> xmlFiles1 = game.gameConfig.getCONFIG_RES().getElementById(resId).getChildrenByNameRecursively("xmlFile");
        for (int e = 0, eMax = xmlFiles1.size; e < eMax; e++) {
            if (xmlFiles1.get(e).get("type").equals(type)) {
                strs.add(xmlFiles1.get(e).get("res"));
            }
        }
        return strs;
    }


    /*public static Texture getBgTextureByScreenId(MainGame game,int screenId, AssetManager as) {
        String str = "";
        Element root = game.gameConfig.getCONFIG_LAYOUT().getElementById(screenId);
        //Gdx.app.log("", "screenId:"+screenId);
        str = root.get("bg");
        //imgBg = new Texture(Gdx.files.internal("image/" + str + ".png"));
        return as.get("image/" + str + ".png", Texture.class);
    }*/


    public static Element getXmlEByName(Element root, String name) {
        for (int j = 0, jMax = root.getChildCount(); j < jMax; j++) {
            if (root.getChild(j).get("name").equals(name)) {
                return (root.getChild(j));
            }
        }
        Gdx.app.error("getXmlEByName", "name:" + name + " :" + root.toString());
        return null;
    }


    //卸载 pixmap资源通过名称
    public static void unloadPixmapByFileName(MainGame game, String fileName, AssetManager am) {
        //1读取路径下说明文件
        XmlReader reader = game.gameConfig.reader;
        StringBuilder path = new StringBuilder("pixmap/");
        path.append(fileName.substring(3)).append("/").append(fileName).append(".xml");
        Element root = reader
                .parse(Gdx.files.internal(path.toString()));
        /*Element root = reader
                .parse(Gdx.files.internal("pixmap/"+fileName.substring(3)+"/" + fileName + ".xml"));*/
        // 每个图片添加的时候都要加使用场景,多场景用;分割screenid="1"
        Array<Element> images = root.getChildrenByNameRecursively("sprite");
        //验证pixmapLists是否有东西
        //2根据说明文件添加到pixmapLists
        for (int i = 0, iMax = images.size; i < iMax; i++) {
            am.unload("pixmap/" + fileName.substring(3) + "/" + images.get(i).get("n"));
        }
    }

    private static float getButtonPotionXByView(Element buttonE, float uiStageWidth, float buttonWidth) {
      /*if(buttonE.getBoolean("ifBorder",false)){
            return buttonE.getFloat("x") * uiStageWidth / 100 + buttonWidth / 2 > uiStageWidth ? uiStageWidth - buttonWidth : buttonE.getFloat("x") * uiStageWidth / 100 - buttonWidth / 2 < 0 ? 0 : buttonE.getFloat("x") * uiStageWidth / 100 - buttonWidth / 2;
        }else {
            return buttonE.getFloat("x") * uiStageWidth / 100 - buttonWidth / 2;
        }*/
        return getButtonPotionXByView(buttonE.getFloat("x"), buttonE.getFloat("y"), uiStageWidth, buttonWidth / 2, buttonE.getBoolean("ifBorder", true));
    }

    private static float getButtonPotionXByView(float x, float y, float uiStageWidth, float refX, boolean ifBorder) {
        if (ifBorder) {
            return x * uiStageWidth / 100 + refX > uiStageWidth ? uiStageWidth - refX * 2 : x * uiStageWidth / 100 - refX < 0 ? 0 : x * uiStageWidth / 100 - refX;
        } else {
            return x * uiStageWidth / 100 - refX;
        }
    }


    private static float getButtonPotionYByView(Element buttonE, float uiStageHeight, float buttonHeight) {
        return getButtonPotionYByView(buttonE.getFloat("x"), buttonE.getFloat("y"), uiStageHeight, buttonHeight / 2, buttonE.getBoolean("ifBorder", true));
    }

    private static float getButtonPotionYByView(float x, float y, float uiStageHeight, float refy, boolean ifBorder) {
        if (ifBorder) {
            return y * uiStageHeight / 100 + refy > uiStageHeight ? uiStageHeight - refy * 2 : y * uiStageHeight / 100 - refy < 0 ? 0 : y * uiStageHeight / 100 - refy;
        } else {
            return y * uiStageHeight / 100 - refy;
        }
    }

    public static ImageButton initImageButton(ImageButton button, MainGame game, int x, int y, int w, int h, int refx, int refy, int blank, String imgUpName, String imgDownName, boolean ifBorder, float uiStageWidth, float uiStageHeight) {

        //blank   1 下,按下,按起不显示,但是点击显示up的东西  2 不显示
        int buttonRefx = 0, buttonRefy = 0;
        if (blank == -1) {

            TextureRegionDAO ud = game.getImgLists().getTextureByName(imgUpName);
            buttonRefx = ud.getRefx();
            buttonRefy = ud.getRefy();
            TextureRegion u = ud.getTextureRegion();
            TextureRegion d = game.getImgLists().getTextureByName(imgDownName).getTextureRegion();
            button = new ImageButton(new TextureRegionDrawable(u), new TextureRegionDrawable(d), new TextureRegionDrawable(d));
            /*if(w==250){
                int s=11;
            }
*/
            button.setSize(w == 0 ? u.getRegionWidth() : w * u.getRegionWidth() / 100, h == 0 ? u.getRegionHeight() : h * u.getRegionHeight() / 100);

            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinHeight(button.getHeight());

        } else if (blank == 0) {
            TextureRegionDAO u = game.getImgLists().getTextureByName(imgUpName);
            buttonRefx = u.getRefx();
            buttonRefy = u.getRefy();

            button = new ImageButton(new TextureRegionDrawable(u.getTextureRegion()));
            button.setSize(w == 0 ? u.getTextureRegion().getRegionWidth() : w * u.getTextureRegion().getRegionWidth() / 100, h == 0 ? u.getTextureRegion().getRegionHeight() : h * u.getTextureRegion().getRegionHeight() / 100);

            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());

        } else if (blank == 1) {
            TextureRegionDAO u = game.getImgLists().getBlankRegionDAO(imgUpName);
            TextureRegionDAO d = game.getImgLists().getBlankRegionDAO(imgDownName);
            TextureRegion c = game.getImgLists().getTextureByName(imgUpName).getTextureRegion();

            button = new ImageButton(new TextureRegionDrawable(u.getTextureRegion()), new TextureRegionDrawable(d.getTextureRegion()), new TextureRegionDrawable(c));
            button.setSize(w == 0 ? u.getTextureRegion().getRegionWidth() : w * u.getTextureRegion().getRegionWidth() / 100, h == 0 ? u.getTextureRegion().getRegionHeight() : h * u.getTextureRegion().getRegionHeight() / 100);

            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinHeight(button.getHeight());


            buttonRefx = u.getRefx();
            buttonRefy = u.getRefy();
        }


        //button.getImage().setSize(button.getWidth(),button.getHeight());


        button.setPosition(
                getButtonPotionXByView(x, y, uiStageWidth, buttonRefx, ifBorder) + refx,
                //   GameUtil.getButtonPotionYByView(buttonE, uiStageWidth, uiStageHeight, button.getWidth(), button.getHeight())+buttonE.getFloat("refy",0)
                getButtonPotionYByView(x, y, uiStageHeight, buttonRefy, ifBorder) + refy
        );
        return button;
    }


    public static ImageButton initImageButton(ImageButton button, MainGame game, Element buttonE, float uiStageWidth, float uiStageHeight) {
       /* if(buttonE.getInt("id",-1)==8&&buttonE.get("imgUpName","").equals("flag_35")){
            int s=0;
        }*/
        int w = buttonE.getInt("w");
        int h = buttonE.getInt("h");
        if (w == -1) {
            w = (int) uiStageWidth;
        }
        if (h == -1) {
            h = (int) uiStageHeight;
        }
        int blank = buttonE.getInt("blankType", -1);

        int refx = 0, refy = 0;
        if (blank == -1) {
            if (buttonE.getBoolean("ifTexture", false)) {
                Texture t = game.getAssetManager().get(game.gameConfig.getFileNameForPath(buttonE.getParent().getParent().getParent().getInt("resId"), buttonE.get("imgUpName")), Texture.class);
                TextureRegionDrawable image = new TextureRegionDrawable(t);
                button = new ImageButton(image, image, image);
                button.setSize(w == 0 ? image.getMinWidth() : w * image.getMinWidth() / 100, h == 0 ? image.getMinHeight() : h * image.getMinHeight() / 100);
            } else {
                TextureRegionDAO u = game.getImgLists().getTextureByName(buttonE.get("imgUpName"));
                TextureRegionDAO d = game.getImgLists().getTextureByName(buttonE.get("imgDownName"));
                button = new ImageButton(new TextureRegionDrawable(u.getTextureRegion()), new TextureRegionDrawable(d.getTextureRegion()), new TextureRegionDrawable(d.getTextureRegion()));
                button.setSize(w == 0 ? u.getTextureRegion().getRegionWidth() : w * u.getTextureRegion().getRegionWidth() / 100, h == 0 ? u.getTextureRegion().getRegionHeight() : h * u.getTextureRegion().getRegionHeight() / 100);
                refx = u.getRefx();
                refy = u.getRefy();
            }
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinHeight(button.getHeight());

        } else if (blank == 0) {//全部隐藏
            TextureRegionDAO u = game.getImgLists().getBlankRegionDAO(buttonE.get("imgUpName"));
            button = new ImageButton(new TextureRegionDrawable(u.getTextureRegion()));
            button.setSize(w == 0 ? u.getTextureRegion().getRegionWidth() : w * u.getTextureRegion().getRegionWidth() / 100, h == 0 ? u.getTextureRegion().getRegionHeight() : h * u.getTextureRegion().getRegionHeight() / 100);

            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());
            refx = u.getRefx();
            refy = u.getRefy();
        } else if (blank == 1) {//只有抬起时显示
            TextureRegionDAO u = game.getImgLists().getBlankRegionDAO(buttonE.get("imgUpName"));
            TextureRegionDAO d = game.getImgLists().getBlankRegionDAO(buttonE.get("imgDownName"));
            TextureRegionDAO c = game.getImgLists().getTextureByName(buttonE.get("imgUpName"));

            button = new ImageButton(new TextureRegionDrawable(u.getTextureRegion()), new TextureRegionDrawable(d.getTextureRegion()), new TextureRegionDrawable(c.getTextureRegion()));
            button.setSize(w == 0 ? u.getTextureRegion().getRegionWidth() : w * u.getTextureRegion().getRegionWidth() / 100, h == 0 ? u.getTextureRegion().getRegionHeight() : h * u.getTextureRegion().getRegionHeight() / 100);

            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinHeight(button.getHeight());
            refx = u.getRefx();
            refy = u.getRefy();
        } else {
            Gdx.app.error("initImageButton error", "blankType is invalid");
        }


        //blank   1 下,按下,按起不显示,但是点击显示up的东西  2 不显示
        if (buttonE.getBoolean("ifDebug", false)) {
            button.setDebug(true);
        }

       /* //button.getImage().setSize(button.getWidth(),button.getHeight());
        if(buttonE==null||  ComUtil.isEmpty(buttonE.get("refy"))||  ComUtil.isEmpty(buttonE.get("refx"))){
            int s=0;
        }
*/
        if (buttonE.getBoolean("ifRegionOffset", false)) {
            button.setPosition(
                    //GameUtil.getButtonPotionXByView(buttonE, uiStageWidth, button.getWidth())+buttonE.getFloat("refx",0),
                    //GameUtil.getButtonPotionYByView(buttonE,  uiStageHeight, button.getHeight())+buttonE.getFloat("refy",0)

                    getButtonPotionXByView(buttonE.getFloat("x"), buttonE.getFloat("y"), uiStageWidth, refx, buttonE.getBoolean("ifBorder", true)) + buttonE.getFloat("refx", 0),
                    getButtonPotionYByView(buttonE.getFloat("x"), buttonE.getFloat("y"), uiStageHeight, refy, buttonE.getBoolean("ifBorder", true)) + buttonE.getFloat("refy", 0)
            );
        } else {
            button.setPosition(
                    GameUtil.getButtonPotionXByView(buttonE, uiStageWidth, button.getWidth()) + buttonE.getFloat("refx", 0),
                    GameUtil.getButtonPotionYByView(buttonE, uiStageHeight, button.getHeight()) + buttonE.getFloat("refy", 0)
            );
        }


        return button;
    }

    public static ImageButton resetImageButtonPotion(ImageButton button, MainGame game, Element buttonE, float uiStageWidth, float uiStageHeight) {
       /* if(buttonE.getInt("id",-1)==8&&buttonE.get("imgUpName","").equals("flag_35")){
            int s=0;
        }*/
        int w = buttonE.getInt("w");
        int h = buttonE.getInt("h");
        if (w == -1) {
            w = (int) uiStageWidth;
        }
        if (h == -1) {
            h = (int) uiStageHeight;
        }
        int blank = buttonE.getInt("blankType", -1);

        int refx = 0, refy = 0;
        if (blank == -1) {
            if (buttonE.getBoolean("ifTexture", false)) {
                Texture t = game.getAssetManager().get(game.gameConfig.getFileNameForPath(buttonE.getParent().getParent().getParent().getInt("resId"), buttonE.get("imgUpName")), Texture.class);
                TextureRegionDrawable image = new TextureRegionDrawable(t);
                button.setSize(w == 0 ? image.getMinWidth() : w * image.getMinWidth() / 100, h == 0 ? image.getMinHeight() : h * image.getMinHeight() / 100);
            } else {
                TextureRegionDAO u = game.getImgLists().getTextureByName(buttonE.get("imgUpName"));
                TextureRegionDAO d = game.getImgLists().getTextureByName(buttonE.get("imgDownName"));
                button.setSize(w == 0 ? u.getTextureRegion().getRegionWidth() : w * u.getTextureRegion().getRegionWidth() / 100, h == 0 ? u.getTextureRegion().getRegionHeight() : h * u.getTextureRegion().getRegionHeight() / 100);
                refx = u.getRefx();
                refy = u.getRefy();
            }
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinHeight(button.getHeight());

        } else if (blank == 0) {//全部隐藏
            TextureRegionDAO u = game.getImgLists().getBlankRegionDAO(buttonE.get("imgUpName"));
            button.setSize(w == 0 ? u.getTextureRegion().getRegionWidth() : w * u.getTextureRegion().getRegionWidth() / 100, h == 0 ? u.getTextureRegion().getRegionHeight() : h * u.getTextureRegion().getRegionHeight() / 100);

            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());
            refx = u.getRefx();
            refy = u.getRefy();
        } else if (blank == 1) {//只有抬起时显示
            TextureRegionDAO u = game.getImgLists().getBlankRegionDAO(buttonE.get("imgUpName"));
            TextureRegionDAO d = game.getImgLists().getBlankRegionDAO(buttonE.get("imgDownName"));
            TextureRegionDAO c = game.getImgLists().getTextureByName(buttonE.get("imgUpName"));

            button.setSize(w == 0 ? u.getTextureRegion().getRegionWidth() : w * u.getTextureRegion().getRegionWidth() / 100, h == 0 ? u.getTextureRegion().getRegionHeight() : h * u.getTextureRegion().getRegionHeight() / 100);

            ((TextureRegionDrawable) button.getStyle().imageUp).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageUp).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageDown).setMinHeight(button.getHeight());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinWidth(button.getWidth());
            ((TextureRegionDrawable) button.getStyle().imageChecked).setMinHeight(button.getHeight());
            refx = u.getRefx();
            refy = u.getRefy();
        } else {
            Gdx.app.error("initImageButton error", "blankType is invalid");
        }


        //blank   1 下,按下,按起不显示,但是点击显示up的东西  2 不显示
        if (buttonE.getBoolean("ifDebug", false)) {
            button.setDebug(true);
        }

       /* //button.getImage().setSize(button.getWidth(),button.getHeight());
        if(buttonE==null||  ComUtil.isEmpty(buttonE.get("refy"))||  ComUtil.isEmpty(buttonE.get("refx"))){
            int s=0;
        }
*/
        if (buttonE.getBoolean("ifRegionOffset", false)) {
            button.setPosition(
                    //GameUtil.getButtonPotionXByView(buttonE, uiStageWidth, button.getWidth())+buttonE.getFloat("refx",0),
                    //GameUtil.getButtonPotionYByView(buttonE,  uiStageHeight, button.getHeight())+buttonE.getFloat("refy",0)

                    getButtonPotionXByView(buttonE.getFloat("x"), buttonE.getFloat("y"), uiStageWidth, refx, buttonE.getBoolean("ifBorder", true)) + buttonE.getFloat("refx", 0),
                    getButtonPotionYByView(buttonE.getFloat("x"), buttonE.getFloat("y"), uiStageHeight, refy, buttonE.getBoolean("ifBorder", true)) + buttonE.getFloat("refy", 0)
            );
        } else {
            button.setPosition(
                    GameUtil.getButtonPotionXByView(buttonE, uiStageWidth, button.getWidth()) + buttonE.getFloat("refx", 0),
                    GameUtil.getButtonPotionYByView(buttonE, uiStageHeight, button.getHeight()) + buttonE.getFloat("refy", 0)
            );
        }


        return button;
    }
    public static ImageButton resetImageButtonPotion(ImageButton button, MainGame game, int x, int y, int w, int h, int refx, int refy, float uiStageWidth, float uiStageHeight, boolean ifBorder) {

        if (w == -1) {
            w = (int) uiStageWidth;
        }
        if (h == -1) {
            h = (int) uiStageHeight;
        }

        TextureRegionDrawable d = null;
        if (button.getStyle().imageUp != null) {
            d = (TextureRegionDrawable) button.getStyle().imageUp;
        } else if (button.getStyle().imageDown != null) {
            d = (TextureRegionDrawable) button.getStyle().imageDown;
        } else if (button.getStyle().imageChecked != null) {
            d = (TextureRegionDrawable) button.getStyle().imageChecked;
        }
        if (d != null) {
            button.setSize(w == 0 ? d.getRegion().getRegionWidth() : w * d.getRegion().getRegionWidth() / 100, h == 0 ? d.getRegion().getRegionHeight() : h * d.getRegion().getRegionHeight() / 100);
        }

        button.setPosition(
                getButtonPotionXByView(x, y, uiStageWidth, button.getWidth() / 2, ifBorder) + refx,
                getButtonPotionYByView(x, y, uiStageHeight, button.getHeight() / 2, ifBorder) + refy
        );

        return button;
    }

    public static TextButton initTextButton(TextButton button, MainGame game, Element buttonE, float uiStageWidth, float uiStageHeight) {
        int w = buttonE.getInt("w");
        int h = buttonE.getInt("h");

        //blank   1 下,按下,按起不显示,但是点击显示up的东西  2 不显示
        TextureRegionDrawable t = new TextureRegionDrawable(game.getImgLists().getTextureByName(buttonE.get("img")).getTextureRegion());
        if (w == -1) {
            w = (int) uiStageWidth;
        } else if (w == 0) {
            w = (int) t.getMinWidth();
        }
        if (h == -1) {
            h = (int) uiStageHeight;
        } else if (h == 0) {
            h = (int) t.getMinHeight();
        }
        t.setMinWidth(w == 0 ? 100 : w);
        t.setMinHeight(h == 0 ? 100 : h);


        button = new TextButton(game.gameMethod.getStrValueAndHaveDefault(buttonE.get("text",""),buttonE.get("text","")), new TextButton.TextButtonStyle(t, t, t, game.gameMethod.getFont(buttonE.get("font"))));
        if (buttonE.getBoolean("ifWrap", false)) {
            button.getLabel().setWrap(true);
        }
        if (buttonE.getBoolean("ifDebug", false)) {
            button.setDebug(true);
            button.getLabel().setDebug(true);
        }

        button.setSize(t.getMinWidth(), t.getMinHeight());
        button.getLabel().setColor(DefDAO.getColor(buttonE.get("color")));
        if (buttonE.get("font").equals("default")) {
            if (buttonE.getFloat("scale") != 0) {
                button.getLabel().setFontScale(buttonE.getFloat("scale"));
            }
        } else {
            if (buttonE.getFloat("scale") != 0) {
                button.getLabel().setFontScale(buttonE.getFloat("scale")*game.gameConfig.gameFontScale);
            }
        }
        button.setPosition(
                GameUtil.getButtonPotionXByView(buttonE, uiStageWidth, button.getWidth()) + buttonE.getFloat("refx", 0),
                GameUtil.getButtonPotionYByView(buttonE, uiStageHeight, button.getHeight()) + buttonE.getFloat("refy", 0)
        );

        String align = buttonE.get("textAlign", "center");

        if (align.equals("bottomLeft")) {
            button.getLabel().setAlignment(Align.bottomLeft);
        } else if (align.equals("bottom")) {
            button.getLabel().setAlignment(Align.bottom);
        } else if (align.equals("center")) {
            button.getLabel().setAlignment(Align.center);
        } else if (align.equals("bottomLeft")) {
            button.getLabel().setAlignment(Align.bottomLeft);
        } else if (align.equals("right")) {
            button.getLabel().setAlignment(Align.right);
        } else if (align.equals("bottomRight")) {
            button.getLabel().setAlignment(Align.bottomRight);
        } else if (align.equals("left")) {
            button.getLabel().setAlignment(Align.left);
        } else if (align.equals("top")) {
            button.getLabel().setAlignment(Align.top);
        } else if (align.equals("topLeft")) {
            button.getLabel().setAlignment(Align.topLeft);
        } else if (align.equals("topRight")) {
            button.getLabel().setAlignment(Align.topRight);
        }

        buttonE.get("labelAlign", "center");

        if (align.equals("bottomLeft")) {
            button.align(Align.bottomLeft);
        } else if (align.equals("bottom")) {
            button.align(Align.bottom);
        } else if (align.equals("center")) {
            button.align(Align.center);
        } else if (align.equals("bottomLeft")) {
            button.align(Align.bottomLeft);
        } else if (align.equals("right")) {
            button.align(Align.right);
        } else if (align.equals("bottomRight")) {
            button.align(Align.bottomRight);
        } else if (align.equals("left")) {
            button.align(Align.left);
        } else if (align.equals("top")) {
            button.align(Align.top);
        } else if (align.equals("topLeft")) {
            button.align(Align.topLeft);
        } else if (align.equals("topRight")) {
            button.align(Align.topRight);
        }
        return button;
    }

    public static TextButton resetTextButtonPotion(TextButton button, MainGame game, Element buttonE, float uiStageWidth, float uiStageHeight) {
        int w = buttonE.getInt("w");
        int h = buttonE.getInt("h");

        //blank   1 下,按下,按起不显示,但是点击显示up的东西  2 不显示
        TextureRegionDrawable t = new TextureRegionDrawable(game.getImgLists().getTextureByName(buttonE.get("img")).getTextureRegion());
        if (w == -1) {
            w = (int) uiStageWidth;
        } else if (w == 0) {
            w = (int) t.getMinWidth();
        }
        if (h == -1) {
            h = (int) uiStageHeight;
        } else if (h == 0) {
            h = (int) t.getMinHeight();
        }
        t.setMinWidth(w == 0 ? 100 : w);
        t.setMinHeight(h == 0 ? 100 : h);


       if (buttonE.getBoolean("ifWrap", false)) {
            button.getLabel().setWrap(true);
        }
        if (buttonE.getBoolean("ifDebug", false)) {
            button.setDebug(true);
            button.getLabel().setDebug(true);
        }

        button.setSize(t.getMinWidth(), t.getMinHeight());
        button.getLabel().setColor(DefDAO.getColor(buttonE.get("color")));
        if (buttonE.get("font").equals("default")) {
            if (buttonE.getFloat("scale") != 0) {
                button.getLabel().setFontScale(buttonE.getFloat("scale"));
            }
        } else {
            if (buttonE.getFloat("scale") != 0) {
                button.getLabel().setFontScale(buttonE.getFloat("scale")*game.gameConfig.gameFontScale);
            }
        }

        button.setPosition(
                GameUtil.getButtonPotionXByView(buttonE, uiStageWidth, button.getWidth()) + buttonE.getFloat("refx", 0),
                GameUtil.getButtonPotionYByView(buttonE, uiStageHeight, button.getHeight()) + buttonE.getFloat("refy", 0)
        );

        String align = buttonE.get("textAlign", "center");

        if (align.equals("bottomLeft")) {
            button.getLabel().setAlignment(Align.bottomLeft);
        } else if (align.equals("bottom")) {
            button.getLabel().setAlignment(Align.bottom);
        } else if (align.equals("center")) {
            button.getLabel().setAlignment(Align.center);
        } else if (align.equals("bottomLeft")) {
            button.getLabel().setAlignment(Align.bottomLeft);
        } else if (align.equals("right")) {
            button.getLabel().setAlignment(Align.right);
        } else if (align.equals("bottomRight")) {
            button.getLabel().setAlignment(Align.bottomRight);
        } else if (align.equals("left")) {
            button.getLabel().setAlignment(Align.left);
        } else if (align.equals("top")) {
            button.getLabel().setAlignment(Align.top);
        } else if (align.equals("topLeft")) {
            button.getLabel().setAlignment(Align.topLeft);
        } else if (align.equals("topRight")) {
            button.getLabel().setAlignment(Align.topRight);
        }

        buttonE.get("labelAlign", "center");

        if (align.equals("bottomLeft")) {
            button.align(Align.bottomLeft);
        } else if (align.equals("bottom")) {
            button.align(Align.bottom);
        } else if (align.equals("center")) {
            button.align(Align.center);
        } else if (align.equals("bottomLeft")) {
            button.align(Align.bottomLeft);
        } else if (align.equals("right")) {
            button.align(Align.right);
        } else if (align.equals("bottomRight")) {
            button.align(Align.bottomRight);
        } else if (align.equals("left")) {
            button.align(Align.left);
        } else if (align.equals("top")) {
            button.align(Align.top);
        } else if (align.equals("topLeft")) {
            button.align(Align.topLeft);
        } else if (align.equals("topRight")) {
            button.align(Align.topRight);
        }
        return button;
    }
    public static TextButton initTextButton(TextButton button, MainGame game, String imgName, String fontName, int x, int y, int w, int h, float fontScale, float uiStageWidth, float uiStageHeight, boolean ifWrap, String color, String textAlign, String labelAlign, boolean ifBorder, int refX, int refY) {
        // int w=buttonE.getInt("w");
        // int h=buttonE.getInt("h");

        //blank   1 下,按下,按起不显示,但是点击显示up的东西  2 不显示
        TextureRegionDAO td = game.getImgLists().getTextureByName(imgName);

        TextureRegionDrawable t = new TextureRegionDrawable(td.getTextureRegion());
        t.setMinWidth(w == 0 ? 100 : w);
        t.setMinHeight(h == 0 ? 100 : h);


        button = new TextButton("", new TextButton.TextButtonStyle(t, t, t, game.gameMethod.getFont(fontName)));
        if (ifWrap) {
            button.getLabel().setWrap(true);
        }

        button.setSize(t.getMinWidth(), t.getMinHeight());
        button.getLabel().setColor(DefDAO.getColor(color));
        if (fontScale != 0) {
            button.getLabel().setFontScale(fontScale);
        }


        button.setPosition(
                getButtonPotionXByView(x, y, uiStageWidth, td.getRefx(), ifBorder) + refX,
                getButtonPotionYByView(x, y, uiStageHeight, td.getRefy(), ifBorder) + refY

        );


        if (textAlign.equals("bottomLeft")) {
            button.getLabel().setAlignment(Align.bottomLeft);
        } else if (textAlign.equals("bottom")) {
            button.getLabel().setAlignment(Align.bottom);
        } else if (textAlign.equals("center")) {
            button.getLabel().setAlignment(Align.center);
        } else if (textAlign.equals("bottomLeft")) {
            button.getLabel().setAlignment(Align.bottomLeft);
        } else if (textAlign.equals("right")) {
            button.getLabel().setAlignment(Align.right);
        } else if (textAlign.equals("bottomRight")) {
            button.getLabel().setAlignment(Align.bottomRight);
        } else if (textAlign.equals("left")) {
            button.getLabel().setAlignment(Align.left);
        } else if (textAlign.equals("top")) {
            button.getLabel().setAlignment(Align.top);
        } else if (textAlign.equals("topLeft")) {
            button.getLabel().setAlignment(Align.topLeft);
        } else if (textAlign.equals("topRight")) {
            button.getLabel().setAlignment(Align.topRight);
        }


        if (labelAlign.equals("bottomLeft")) {
            button.align(Align.bottomLeft);
        } else if (labelAlign.equals("bottom")) {
            button.align(Align.bottom);
        } else if (labelAlign.equals("center")) {
            button.align(Align.center);
        } else if (labelAlign.equals("bottomLeft")) {
            button.align(Align.bottomLeft);
        } else if (labelAlign.equals("right")) {
            button.align(Align.right);
        } else if (labelAlign.equals("bottomRight")) {
            button.align(Align.bottomRight);
        } else if (labelAlign.equals("left")) {
            button.align(Align.left);
        } else if (labelAlign.equals("top")) {
            button.align(Align.top);
        } else if (labelAlign.equals("topLeft")) {
            button.align(Align.topLeft);
        } else if (labelAlign.equals("topRight")) {
            button.align(Align.topRight);
        }
        return button;
    }


    public static Image initImage(Image image, MainGame mainGame, Element imageE, float uiStageWidth, float uiStageHeight) {
        if (ComUtil.isEmpty(imageE.get("imgName"))) {
            Gdx.app.error("initImageError1", "imgName is null");
        }
        int refX = 0;
        int refY = 0;

        if (imageE.getBoolean("ifTexture", false)) {
            image = new Image(mainGame.getAssetManager().get(mainGame.gameConfig.getFileNameForPath(imageE.getParent().getParent().getParent().getInt("resId"), imageE.get("imgName")), Texture.class));
            refX = (int) (image.getWidth() / 2);
            refY = (int) (image.getHeight() / 2);
        } else {
            TextureRegionDAO td = mainGame.getImgLists().getTextureByName(imageE.get("imgName"));
            image = new Image(td.getTextureRegion());
            refX = td.getRefx();
            refY = td.getRefy();
        }

       Color color=DefDAO.getColor(imageE.get("color","WHITE"));
        float alpha=imageE.getFloat("alpha",1f);
        if(alpha==1f){
            image.setColor(color);
        }else{
            image.setColor(color.r,color.g,color.b,alpha);
        }


        if (imageE.getBoolean("ifDebug", false)) {
            image.setDebug(true);
        }
        // image.setSize(imageE.getInt("w") == 0 ? mainGame.getImgLists().getTextureByName(imageE.get("imgName")).getTextureRegion().getRegionWidth() : imageE.getInt("w") * mainGame.getImgLists().getTextureByName(imageE.get("imgName")).getTextureRegion().getRegionWidth() / 100, imageE.getInt("h") == 0 ? mainGame.getImgLists().getTextureByName(imageE.get("imgName")).getTextureRegion().getRegionHeight() : imageE.getInt("h") * mainGame.getImgLists().getTextureByName(imageE.get("imgName")).getTextureRegion().getRegionHeight() / 100);

        if (imageE.getBoolean("ifFull", false)) {
            image.setSize(mainGame.getWorldWidth(), mainGame.getWorldHeight());
        } else {
            image.setSize(imageE.getInt("w") == 0 ? image.getWidth() : imageE.getInt("w") * image.getWidth() / 100, imageE.getInt("h") == 0 ? image.getHeight() : imageE.getInt("h") * image.getHeight() / 100);
            if (imageE.getInt("w") == -1) {
                image.setWidth(uiStageWidth);
            }
            if (imageE.getInt("h") == -1) {
                image.setHeight(uiStageHeight);
            }
        }

        if (imageE.getBoolean("ifRegionOffset", false)) {
            image.setPosition(
                    getButtonPotionXByView(imageE.getFloat("x"), imageE.getFloat("y"), uiStageWidth, refX, imageE.getBoolean("ifBorder", true)) + imageE.getFloat("refx", 0),
                    getButtonPotionYByView(imageE.getFloat("x"), imageE.getFloat("y"), uiStageHeight, refY, imageE.getBoolean("ifBorder", true)) + imageE.getFloat("refy", 0)
            );
        } else {
            image.setPosition(
                    GameUtil.getButtonPotionXByView(imageE, uiStageWidth, image.getWidth()) + imageE.getFloat("refx", 0),
                    GameUtil.getButtonPotionYByView(imageE, uiStageHeight, image.getHeight()) + imageE.getFloat("refy", 0)
            );
        }


        image.setName(imageE.get("imgName"));
        return image;
    }

    public static Image resetImagePotion(MainGame mainGame, Image image, int x, int y, int w, int h, int refx, int refy, float uiStageWidth, float uiStageHeight, boolean ifBorder, boolean ifRegionOffset) {

        image.setSize(w == 0 ? image.getDrawable().getMinWidth() : w * image.getDrawable().getMinWidth() / 100, h == 0 ? image.getDrawable().getMinHeight() : h * image.getDrawable().getMinHeight() / 100);
        if (w == -1) {
            image.setWidth(uiStageWidth);
        }
        if (h == -1) {
            image.setHeight(uiStageHeight);
        }


        if (ifRegionOffset) {
            int refX = 0;
            int refY = 0;
            TextureRegionDAO td = mainGame.getImgLists().getTextureByName(image.getName());
            if (td != null) {
                image = new Image(td.getTextureRegion());
                refX = td.getRefx();
                refY = td.getRefy();
            }

            image.setPosition(
                    getButtonPotionXByView(x, y, uiStageWidth, refX, ifBorder) + refx,
                    getButtonPotionYByView(x, y, uiStageHeight, refY, ifBorder) + refy
            );
        } else {
            image.setPosition(
                    GameUtil.getButtonPotionXByView(x, y, uiStageWidth, image.getWidth() / 2, ifBorder) + refx,  //float x,float y, float uiStageWidth, float refX,boolean ifBorder
                    GameUtil.getButtonPotionYByView(x, y, uiStageHeight, image.getHeight() / 2, ifBorder) + refy
            );
        }

        return image;
    }


    public static Image resetImagePotion(Image image, Element imageE, float uiStageWidth, float uiStageHeight) {

        if (imageE.get("imgName") == null) {
            Gdx.app.error("initImageError1", "imgName is null");
        }
        image.setSize(imageE.getInt("w") == 0 ? image.getDrawable().getMinWidth() : imageE.getInt("w") * image.getDrawable().getMinWidth() / 100, imageE.getInt("h") == 0 ? image.getDrawable().getMinHeight() : imageE.getInt("h") * image.getDrawable().getMinHeight() / 100);
        if (imageE.getInt("w") == -1) {
            image.setWidth(uiStageWidth);
        }
        if (imageE.getInt("h") == -1) {
            image.setHeight(uiStageHeight);
        }
        image.setPosition(
                GameUtil.getButtonPotionXByView(imageE, uiStageWidth, image.getWidth()) + imageE.getInt("refx", 0),
                GameUtil.getButtonPotionYByView(imageE, uiStageHeight, image.getHeight()) + imageE.getInt("refy", 0)
        );
        return image;
    }

    //imgW,imgH为原来的百分比,若为0则为原来的大小 如需特制则另外重设置
    public static Image initImage(Image image, MainGame mainGame, String imgName, float imgX, float imgY, int imgW, int imgH, float uiStageWidth, float uiStageHeight, boolean ifBorder) {
        TextureRegionDAO td = mainGame.getImgLists().getTextureByName(imgName);
        image = new Image(td.getTextureRegion());
        image.setSize(imgW == 0 ? mainGame.getImgLists().getTextureByName(imgName).getTextureRegion().getRegionWidth() : imgW * mainGame.getImgLists().getTextureByName(imgName).getTextureRegion().getRegionWidth() / 100,
                imgH == 0 ? mainGame.getImgLists().getTextureByName(imgName).getTextureRegion().getRegionHeight() : imgH * mainGame.getImgLists().getTextureByName(imgName).getTextureRegion().getRegionHeight() / 100);
        /*image.setPosition(
                imgX * uiStageWidth / 100 + image.getWidth() / 2 > uiStageWidth ? uiStageWidth - image.getWidth() : imgX * uiStageWidth / 100 - image.getWidth() / 2 < 0 ? 0 : imgX * uiStageWidth / 100 - image.getWidth() / 2,
                imgY * uiStageHeight / 100 + image.getHeight() / 2 > uiStageHeight ? uiStageHeight - image.getHeight() : imgY * uiStageHeight / 100 - image.getHeight() / 2 < 0 ? 0 : imgY * uiStageHeight / 100 - image.getHeight() / 2);
*/
        image.setPosition(
                GameUtil.getButtonPotionXByView(imgX, imgY, uiStageWidth, td.getRefx(), ifBorder),
                GameUtil.getButtonPotionYByView(imgX, imgY, uiStageHeight, td.getRefy(), ifBorder)
        );
        return image;

    }

    //大改
    public static Label initLabel(Label label, BitmapFont font, String text, Color color, Element labelE, float uiStageWidth, float uiStageHeight, float fontScale) {


        label = new Label(text, new Label.LabelStyle(font, Color.WHITE));
        label.setColor(color);
        if (labelE.get("font").equals("default")) {
            if (labelE.getFloat("scale") != 0) {
                label.setFontScale(labelE.getFloat("scale"));
            }
        } else {
            if (labelE.getFloat("scale") != 0) {
                label.setFontScale(labelE.getFloat("scale") * fontScale);
            }
        }
        float w = labelE.getFloat("w", 0);
        float h = labelE.getFloat("h", 0);
        if (labelE.getBoolean("ifDebug", false)) {
            label.setDebug(true);
        }
        if (h == -1) {
            label.setHeight(720);
        } else if (h != 0) {
            label.setHeight(h /** uiStageHeight / 100*/);
        } else {
            label.setHeight(0);
        }
        if (w == -1) {
            label.setWidth(1280);
            label.setWrap(true);
            label.setAlignment(Align.bottomLeft, Align.left);
        } else if (w != 0) {
            label.setWidth(w /** uiStageWidth / 100*/);
            label.setWrap(true);
            label.setAlignment(Align.bottomLeft, Align.left);
        } else {
            label.setWidth(0);
        }
        /*label.setPosition(
                labelE.getFloat("x") * uiStageWidth / 100 + label.getWidth() / 2 > uiStageWidth ? uiStageWidth - label.getWidth() : labelE.getFloat("x") * uiStageWidth / 100 - label.getWidth() / 2 < 0 ? 0 : labelE.getFloat("x") * uiStageWidth / 100 - label.getWidth() / 2,
                labelE.getFloat("y") * uiStageHeight / 100 + label.getHeight() / 2 > uiStageHeight ? uiStageHeight - label.getHeight() : labelE.getFloat("y") * uiStageHeight / 100 - label.getHeight() / 2 < 0 ? 0 : labelE.getFloat("y") * uiStageHeight / 100 - label.getHeight() / 2);
*/
        label.setPosition(
                GameUtil.getButtonPotionXByView(labelE, uiStageWidth, label.getWidth()) + labelE.getInt("refx", 0),
                GameUtil.getButtonPotionYByView(labelE, uiStageHeight, label.getHeight()) + labelE.getInt("refy", 0));
        String align = labelE.get("align");

        if (align.equals("bottomLeft")) {
            label.setAlignment(Align.bottomLeft);
        } else if (align.equals("bottom")) {
            label.setAlignment(Align.bottom);
        } else if (align.equals("center")) {
            label.setAlignment(Align.center);
        } else if (align.equals("right")) {
            label.setAlignment(Align.right);
        } else if (align.equals("bottomRight")) {
            label.setAlignment(Align.bottomRight);
        } else if (align.equals("left")) {
            label.setAlignment(Align.left);
        } else if (align.equals("top")) {
            label.setAlignment(Align.top);
        } else if (align.equals("topLeft")) {
            label.setAlignment(Align.topLeft);
        } else if (align.equals("topRight")) {
            label.setAlignment(Align.topRight);
        }
        return label;
    }


    // 这个功能暂时封闭
    //    <table  x="50" y="50" w="80" h="80" refx="0" refy="0"  ifBorder="true" ifVisible="true" remark="表格组件,每个window只容许有一个该组件" formatPath="" showContent="bm0" ifDebug="true"/>
    public static Table initTable(MainGame mainGame, Element tableE, float uiStageWidth, float uiStageHeight) {
        Table table = new Table();
        float w = tableE.getFloat("w", 0);
        float h = tableE.getFloat("h", 0);
        if (tableE.getBoolean("ifDebug", false)) {
            table.setDebug(true);
        }
        if (h == -1) {
            table.setHeight(uiStageHeight);
        } else if (h != 0) {
            table.setHeight(h * uiStageHeight / 100);
        } else {
            table.setHeight(0);
        }
        if (w == -1) {
            table.setWidth(uiStageWidth);
        } else if (w != 0) {
            table.setWidth(w * uiStageWidth / 100);
        } else {
            table.setWidth(0);
        }
        table.setPosition(
                GameUtil.getButtonPotionXByView(tableE, uiStageWidth, table.getWidth()) + tableE.getInt("refx", 0),
                GameUtil.getButtonPotionYByView(tableE, uiStageHeight, table.getHeight()) + tableE.getInt("refy", 0));
        return table;
    }

    public static Label resetLabelPotion(Label label, int x, int y, int w, int h, int refx, int refy, float uiStageWidth, float uiStageHeight, boolean ifBorder) {


        if (h == -1) {
            label.setHeight(720);
        } else if (h != 0) {
            label.setHeight(h /** uiStageHeight / 100*/);
        } else {
            label.setHeight(0);
        }
        if (w == -1) {
            label.setWidth(1280);
            label.setWrap(true);
            label.setAlignment(Align.bottomLeft, Align.left);
        } else if (w != 0) {
            label.setWidth(w /** uiStageWidth / 100*/);
            label.setWrap(true);
            label.setAlignment(Align.bottomLeft, Align.left);
        } else {
            label.setWidth(0);
        }
        label.setPosition(
                GameUtil.getButtonPotionXByView(x, y, uiStageWidth, label.getWidth() / 2, ifBorder) + refx,  //float x,float y, float uiStageWidth, float refX,boolean ifBorder
                GameUtil.getButtonPotionYByView(x, y, uiStageHeight, label.getHeight() / 2, ifBorder) + refy);
        return label;
    }

    public static Label resetLabelPotion(Label label, Element labelE, float uiStageWidth, float uiStageHeight) {

        float w = labelE.getFloat("w", 0);
        float h = labelE.getFloat("h", 0);
        if (labelE.getBoolean("ifDebug", false)) {
            label.setDebug(true);
        }
        if (h == -1) {
            label.setHeight(720);
        } else if (h != 0) {
            label.setHeight(h/* * uiStageHeight / 100*/);
        } else {
            label.setHeight(0);
        }
        if (w == -1) {
            label.setWidth(1280);
            label.setWrap(true);
            label.setAlignment(Align.bottomLeft, Align.left);
        } else if (w != 0) {
            label.setWidth(w /** uiStageWidth / 100*/);
            label.setWrap(true);
            label.setAlignment(Align.bottomLeft, Align.left);
        } else {
            label.setWidth(0);
        }
        label.setPosition(
                GameUtil.getButtonPotionXByView(labelE, uiStageWidth, label.getWidth()) + labelE.getInt("refx", 0),
                GameUtil.getButtonPotionYByView(labelE, uiStageHeight, label.getHeight()) + labelE.getInt("refy", 0));


        return label;
    }

    public static ScrollLabel initCHScrollLabel(MainGame game, BitmapFont font, String text, Color color, Element labelE, float uiStageWidth, float uiStageHeight, float gameFontScale) {
        ScrollLabel scrollLabel = new ScrollLabel(game.getImgLists().getTextureByName(labelE.get("scrollBg", "colorBlock_0")).getTextureRegion(), font, color, text, labelE.getBoolean("ifCenter", true));

        if (labelE.get("font").equals("default")) {
            if (labelE.getFloat("scale") != 0) {
                scrollLabel.getLabel().setFontScale(labelE.getFloat("scale"));
            }
        } else {
            if (labelE.getFloat("scale") != 0) {
                scrollLabel.getLabel().setFontScale(labelE.getFloat("scale") * gameFontScale);
                scrollLabel.resetTextHeight();
            }
        }


        float w = labelE.getFloat("w", 0);
        float h = labelE.getFloat("h", 0);
        if (h == -1) {
            scrollLabel.setHeight(720);
        } else if (h != 0) {
            // scrollLabel.setHeight(h * uiStageHeight / 100);
            if (labelE.getBoolean("ifAdapt", false)) {
                scrollLabel.setHeight(h / 720 * game.getWorldHeight());
            } else {
                scrollLabel.setHeight(h);
            }
        }
        scrollLabel.setAlignment(labelE.get("align", "center"));
        if (w == -1) {
            scrollLabel.setWidth(1280);
            scrollLabel.setWrap(true);
        } else if (w != 0) {
            scrollLabel.setWidth(w /** uiStageWidth / 100*/);
            scrollLabel.setWrap(true);
            //  scrollLabel.setAlignment(Align.bottomLeft,Align.left);
        }

        //scrollLabel的align属性没用
        //scrollLabel.setAlignment(labelE.get("align"));
        scrollLabel.setPosition(
                GameUtil.getButtonPotionXByView(labelE, uiStageWidth, scrollLabel.getWidth()) + labelE.getInt("refx", 0),
                GameUtil.getButtonPotionYByView(labelE, uiStageHeight, scrollLabel.getHeight()) + labelE.getInt("refy", 0));

        return scrollLabel;
    }

    public static ScrollLabel resetCHScrollLabelPotion( ScrollLabel scrollLabel,MainGame game, BitmapFont font, String text, Color color, Element labelE, float uiStageWidth, float uiStageHeight, float gameFontScale) {

        if (labelE.get("font").equals("default")) {
            if (labelE.getFloat("scale") != 0) {
                scrollLabel.getLabel().setFontScale(labelE.getFloat("scale"));
            }
        } else {
            if (labelE.getFloat("scale") != 0) {
                scrollLabel.getLabel().setFontScale(labelE.getFloat("scale") * gameFontScale);
                scrollLabel.resetTextHeight();
            }
        }


        float w = labelE.getFloat("w", 0);
        float h = labelE.getFloat("h", 0);
        if (h == -1) {
            scrollLabel.setHeight(720);
        } else if (h != 0) {
            // scrollLabel.setHeight(h * uiStageHeight / 100);
            if (labelE.getBoolean("ifAdapt", false)) {
                scrollLabel.setHeight(h / 720 * game.getWorldHeight());
            } else {
                scrollLabel.setHeight(h);
            }
        }
        scrollLabel.setAlignment(labelE.get("align", "center"));
        if (w == -1) {
            scrollLabel.setWidth(1280);
            scrollLabel.setWrap(true);
        } else if (w != 0) {
            scrollLabel.setWidth(w /** uiStageWidth / 100*/);
            scrollLabel.setWrap(true);
            //  scrollLabel.setAlignment(Align.bottomLeft,Align.left);
        }

        //scrollLabel的align属性没用
        //scrollLabel.setAlignment(labelE.get("align"));
        scrollLabel.setPosition(
                GameUtil.getButtonPotionXByView(labelE, uiStageWidth, scrollLabel.getWidth()) + labelE.getInt("refx", 0),
                GameUtil.getButtonPotionYByView(labelE, uiStageHeight, scrollLabel.getHeight()) + labelE.getInt("refy", 0));
        return scrollLabel;
    }
    public static void setImagePotion(Image image, Element imageE, float uiStageWidth, float uiStageHeight) {
       /* image.setPosition(
                imageE.getFloat("x") * uiStageWidth / 100 + image.getWidth() / 2 > uiStageWidth ? uiStageWidth - image.getWidth() : imageE.getFloat("x") * uiStageWidth / 100 - image.getWidth() / 2 < 0 ? 0 : imageE.getFloat("x") * uiStageWidth / 100 - image.getWidth() / 2,
                imageE.getFloat("y") * uiStageHeight / 100 + image.getHeight() / 2 > uiStageHeight ? uiStageHeight - image.getHeight() : imageE.getFloat("y") * uiStageHeight / 100 - image.getHeight() / 2 < 0 ? 0 : imageE.getFloat("y") * uiStageHeight / 100 - image.getHeight() / 2);

    */
        image.setPosition(
                GameUtil.getButtonPotionXByView(imageE, uiStageWidth, image.getWidth()),
                GameUtil.getButtonPotionYByView(imageE, uiStageHeight, image.getHeight())
        );
    }


    public static void setImageButtonPotion(ImageButton button, Element buttonE, float uiStageWidth, float uiStageHeight) {
        button.setPosition(
                GameUtil.getButtonPotionXByView(buttonE, uiStageWidth, button.getWidth()) + buttonE.getFloat("refx", 0),
                GameUtil.getButtonPotionYByView(buttonE, uiStageHeight, button.getHeight()) + buttonE.getFloat("refy", 0)
        );
    }

    public static void setLabelPotion(Label label, Element labelE, float uiStageWidth, float uiStageHeight) {
        /*label.setPosition(
                labelE.getFloat("x") * uiStageWidth / 100 + label.getWidth() / 2 > uiStageWidth ? uiStageWidth - label.getWidth() : labelE.getFloat("x") * uiStageWidth / 100 - label.getWidth() / 2 < 0 ? 0 : labelE.getFloat("x") * uiStageWidth / 100 - label.getWidth() / 2,
                labelE.getFloat("y") * uiStageHeight / 100 + label.getHeight() / 2 > uiStageHeight ? uiStageHeight - label.getHeight() : labelE.getFloat("y") * uiStageHeight / 100 - label.getHeight() / 2 < 0 ? 0 : labelE.getFloat("y") * uiStageHeight / 100 - label.getHeight() / 2);
*/
        label.setPosition(
                GameUtil.getButtonPotionXByView(labelE, uiStageWidth, label.getWidth()),
                GameUtil.getButtonPotionYByView(labelE, uiStageHeight, label.getHeight()));

    }


    public static void logImageInfo(Image image) {
        //Gdx.app.log("imageInfo",image.getName()+":"+image.getImageX()+":"+image.getImageY()+":"+image.getImageWidth()+":"+image.getImageHeight());
        Gdx.app.log("imageInfo", image.getName() + ":" + image.getX() + ":" + image.getY() + ":" + image.getWidth() + ":" + image.getHeight());
    }

    public static void logImageButtonInfo(ImageButton button) {
        Gdx.app.log("imageButtonInfo", button.getName() + ":" + button.getX() + ":" + button.getY() + ":" + button.getWidth() + ":" + button.getHeight());
    }


    /**
     * 传入字符串，返回一个Window对象
     * param StringName
     * texUri 文件的位置
     * param StringName
     * title 标题
     * param float width	宽度 0-1
     * param float height 	高度
     * return Window对象
     */
    public static Window getWindow(MainGame mainGame, String title, float width, float height) {
        Window window;
        TextureRegionDrawable windowDrable = new TextureRegionDrawable(
                new TextureRegion(mainGame.getImgLists().getTextureByName("colorBlock_1").getTextureRegion()));
        BitmapFont font = mainGame.defaultFont;
        Window.WindowStyle style = new Window.WindowStyle(font, Color.RED, windowDrable);
        window = new Window(title, style);
        // 设置窗口的大小；
        window.setWidth(Gdx.graphics.getWidth() * width);
        window.setHeight(Gdx.graphics.getHeight() * height);
        window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2, (Gdx.graphics.getHeight() - window.getHeight()) / 2);
        window.setMovable(false);
        return window;
    }

    //设置卡牌位置
    public static void setCardPriceRefPotion(float cardX, float cardY, float stageW, float stageH, WindowGroup cardPrice) {
        cardPrice.setX(cardX - cardPrice.groupE.getFloat("x", 100) * stageW / 100);
        cardPrice.setY(cardY - cardPrice.groupE.getFloat("y", 100) * stageH / 100);
        Gdx.app.log("cardPotion1", "x:" + cardX + " y:" + cardY);
        Gdx.app.log("cardPotion2", "x:" + cardPrice.getX() + " y:" + cardPrice.getY());
    }


    //TODO 根据defArray设置卡牌位置
    //WindowGroup 设置的窗口
    //cardEs 设置的卡牌

    public static void setCardWindowsCardPotion(MainGame mainGame, WindowGroup window, Array<Element> cardEs, float stageW, float stageH) {
        window.hideAllButton();
        window.hideAllLabel();

        Element cardStyle = mainGame.gameConfig.getDEF_ARRAY().getElementById(ResDefaultConfig.StringName.DefArrayId_card);
        Array<Element> styleEs = cardStyle.getChildrenByName("style");
        for (int i = 0; i < styleEs.size; i++) {
            if (styleEs.get(i).getInt("id") == cardEs.size) {
                if (styleEs.get(i).getChildByName("buttons") != null) {
                    Array<Element> buttonEs = styleEs.get(i).getChildByName("buttons").getChildrenByName("button");
                    for (int j = 0, jMax = buttonEs.size; j < jMax; j++) {
                        ImageButton button = window.getButton(buttonEs.get(j).getInt("id"));
                        if (button != null) {
                            button.setVisible(true);
                            /*button.setPosition(
                                    GameUtil.getButtonPotionXByView(buttonEs.get(j), stageW, button.getWidth()),
                                    GameUtil.getButtonPotionYByView(buttonEs.get(j), stageW, stageH, button.getWidth(), button.getHeight())
                            );*/
                            button.setPosition(stageW / 2, 0);
                            MoveToAction action = Actions.moveTo(GameUtil.getButtonPotionXByView(buttonEs.get(j), stageW, button.getWidth()),
                                    GameUtil.getButtonPotionYByView(buttonEs.get(j), stageH, button.getHeight()), ResDefaultConfig.Effect.popupCard);
                            button.addAction(action);
                            //TextureRegion region = mainGame.getImgLists().getTextureByName(cardEs.get(j).get("image")).getTextureRegion();

                            TextureRegion region = mainGame.getImgLists().getTextureByName(DefDAO.getImageNameForCard(mainGame, cardEs.get(j).getInt("id"), 0)).getTextureRegion();

                            if (region != null) {
                                ((TextureRegionDrawable) button.getStyle().imageUp).setRegion(region);
                                ((TextureRegionDrawable) button.getStyle().imageDown).setRegion(region);
                                ((TextureRegionDrawable) button.getStyle().imageChecked).setRegion(region);
                            }
                            //Gdx.app.log("button", button.getWidth() + ":" + button.getHeight());
                        }
                    }
                }
                if (styleEs.get(i).getChildByName("labels") != null) {
                    Array<Element> labelEs = styleEs.get(i).getChildByName("labels").getChildrenByName("label");
                    for (int j = 0, jMax = labelEs.size; j < jMax; j++) {
                        Label label = window.getLabel(labelEs.get(j).getInt("id"));
                        if (label != null) {
                            label.setVisible(true);
                            label.setPosition(stageW / 2, 0);
                            MoveToAction action = Actions.moveTo(labelEs.get(j).getFloat("x") * stageW / 100 + label.getWidth() / 2 > stageW ? stageW - label.getWidth() : labelEs.get(j).getFloat("x") * stageW / 100 - label.getWidth() / 2 < 0 ? 0 : labelEs.get(j).getFloat("x") * stageW / 100 - label.getWidth() / 2,
                                    labelEs.get(j).getFloat("y") * stageH / 100 + label.getHeight() / 2 > stageH ? stageH - label.getHeight() : labelEs.get(j).getFloat("y") * stageH / 100 - label.getHeight() / 2 < 0 ? 0 : labelEs.get(j).getFloat("y") * stageH / 100 - label.getHeight() / 2, ResDefaultConfig.Effect.popupCard);
                            label.addAction(action);
                            /*label.setPosition(labelEs.get(j).getFloat("x") * stageW / 100 + label.getWidth() / 2 > stageW ? stageW - label.getWidth() : labelEs.get(j).getFloat("x") * stageW / 100 - label.getWidth() / 2 < 0 ? 0 : labelEs.get(j).getFloat("x") * stageW / 100 - label.getWidth() / 2,
                                    labelEs.get(j).getFloat("y") * stageH / 100 + label.getHeight() / 2 > stageH ? stageH - label.getHeight() : labelEs.get(j).getFloat("y") * stageH / 100 - label.getHeight() / 2 < 0 ? 0 : labelEs.get(j).getFloat("y") * stageH / 100 - label.getHeight() / 2);
                            */
                            Gdx.app.log("label", label.getWidth() + ":" + label.getHeight());
                        }


                    }

                    break;
                }
            }
        }
    }

    public static void setCombatWindowCountryPotion(MainGame mainGame, WindowGroup window, IntArray leftCountryIds, IntArray rightCountryIds, float stageW, float stageH) {
        window.hidImage(1);
        window.hidImage(2);
        window.hidImage(3);
        window.hidImage(4);
        window.hidImage(5);
        window.hidImage(6);
        window.hidImage(7);
        window.hidImage(8);
        window.hidImage(9);
        window.hidImage(10);
        Element combatLCountryStyle = mainGame.gameConfig.getDEF_ARRAY().getElementById(ResDefaultConfig.StringName.DefArrayId_combatLeftCountry);
        Element combatRCountryStyle = mainGame.gameConfig.getDEF_ARRAY().getElementById(ResDefaultConfig.StringName.DefArrayId_combatRightCountry);
        Element styleLE = combatLCountryStyle.getChildrenByName("style").get(leftCountryIds.size);
        Element styleRE = combatRCountryStyle.getChildrenByName("style").get(rightCountryIds.size);
        int imageId;
        if (styleLE.getChildByName("images") != null) {
            Array<Element> imageEs = styleLE.getChildByName("images").getChildrenByName("image");
            for (int j = 0, jMax = imageEs.size; j < jMax; j++) {
                imageId = imageEs.get(j).getInt("id", -1);
                Image image = window.getImage(imageId);
                if (imageId != -1 && image != null) {

                    image.setVisible(true);
                    image.setPosition(
                            GameUtil.getButtonPotionXByView(imageEs.get(j), stageW, image.getWidth()),
                            GameUtil.getButtonPotionYByView(imageEs.get(j), stageH, image.getHeight())
                    );
                    window.setImageRegion(imageId, mainGame.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(leftCountryIds.get(j))));
                }
            }
        }
        if (styleRE.getChildByName("images") != null) {
            Array<Element> imageEs = styleRE.getChildByName("images").getChildrenByName("image");
            for (int j = 0, jMax = imageEs.size; j < jMax; j++) {
                imageId = imageEs.get(j).getInt("id", -1);
                Image image = window.getImage(imageId);
                if (imageId != -1 && image != null) {
                    image.setVisible(true);
                    image.setPosition(
                            GameUtil.getButtonPotionXByView(imageEs.get(j), stageW, image.getWidth()),
                            GameUtil.getButtonPotionYByView(imageEs.get(j), stageH, image.getHeight())
                    );
                    window.setImageRegion(imageId, mainGame.getImgLists().getTextureByName(DefDAO.getImageNameForCountryFlag(rightCountryIds.get(j))));
                }
            }
        }

    }


    public static int getCoverStr(StringBuilder buf, int bufTag, int cutLength) {
        int str = 0;
        if (cutLength != 0) {
            if ((buf.substring(bufTag, bufTag + cutLength).equals("ffffffff") && cutLength == 8) || (buf.substring(bufTag, bufTag + cutLength).equals("ffff") && cutLength == 4) || (buf.substring(bufTag, bufTag + cutLength).equals("ff") && cutLength == 2)) {
                str = -1;
            } else if (cutLength == 8) {
                //str = Integer.parseInt(buf.substring(bufTag, bufTag + cutLength), 32);
                String rs = buf.substring(bufTag, bufTag + cutLength);

                for (int i = 0; i < 4; i++) {
                    int shift = (3 - i) * 8;
                    String st = rs.substring(i * 2, i * 2 + 2);
                    //  byte b= ;
                    str += (Integer.parseInt(st, 16)) << shift;
                }
                //Gdx.app.log("CoverStr8",rs+":"+str);

                //str=-1;
            } else {
                //
                str = Integer.parseInt(buf.substring(bufTag, bufTag + cutLength), 16);
            }
            /**/
        }
        return str;
    }

    public static void setComActorPotionByHexagon(ComActor actor, int mapW, int mapH_px, float scale, int hexagon) {
        int x = (hexagon % mapW) + 1;
        int y = (hexagon / mapW) + 1;
        int sourceX = (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        int sourceY = mapH_px - (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));


        actor.setCenterPotionAndOriginByTextureRegionRef(actor.getX() + sourceX, actor.getY() + sourceY);
    }

    public static void setComActorRotation(ComActor actor, int mapW, int mapH_px, int sourceHexagon, int targetHexagon, float scale) {
        // float scale=getMainGame().getMapScale();
        int x = (sourceHexagon % mapW) + 1;
        int y = (sourceHexagon / mapW) + 1;
        int sourceX = (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        int sourceY = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));

        x = (targetHexagon % mapW) + 1;
        y = (targetHexagon / mapW) + 1;
        int targetX = (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        int targetY = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));


        float degrees = (float) (ComUtil.getAngle(targetX, targetY, sourceX, sourceY));
        float radian = (float) ComUtil.getRadian(degrees);

        //   Gdx.app.log("setComActorRotation",targetX+":"+targetY+":"+sourceX+":"+sourceY+" angle:"+degrees+" radian:"+radian);
        actor.setRotation(radian);
    }

    public static float getRotationByHexagon(int mapW, float mapH_px, int sourceHexagon, int targetHexagon, float scale) {
        // float scale=game.getMapScale();
        int x = (sourceHexagon % mapW) + 1;
        int y = (sourceHexagon / mapW) + 1;
        int sourceX = (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        int sourceY = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));

        x = (targetHexagon % mapW) + 1;
        y = (targetHexagon / mapW) + 1;
        int targetX = (int) (scale * (((x - 1) * ResDefaultConfig.Map.GRID_WIDTH) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_WIDTH * ResDefaultConfig.Map.MAP_SCALE / 2));
        int targetY = (int) (scale * ((((x & 1) == 1 ? (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT : (y - 1) * ResDefaultConfig.Map.HEXAGON_HEIGHT + ResDefaultConfig.Map.HEXAGON_HEIGHT_REF)) * ResDefaultConfig.Map.MAP_SCALE + ResDefaultConfig.Map.HEXAGON_HEIGHT * ResDefaultConfig.Map.MAP_SCALE / 2));


        float degrees = (float) (ComUtil.getAngle(targetX, targetY, sourceX, sourceY));
        float radian = (float) ComUtil.getRadian(degrees);

        //   Gdx.app.log("setComActorRotation",targetX+":"+targetY+":"+sourceX+":"+sourceY+" angle:"+degrees+" radian:"+radian);
        return radian;
    }

    //计算两个坐标的距离,相邻坐标距离为1
    public static int getDistance(int x1, int y1, int x2, int y2) {
        y1 = y1 + (x1 + 1) / 2;
        y2 = y2 + (x2 + 1) / 2;

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        int d = 0;
        if ((x1 - x2) * (y1 - y2) < 0) {
            d = dx + dy;
        } else {
            d = dx > dy ? dx : dy;
        }


        // System.out.println("p1:"+x1+","+y1+"   p2:"+x2+","+y2 + "  d"+d);
        return d;
    }

    /*public static int getDamageForArmy(MainGame game, Fb2Smap.ArmyData army, Fb2Smap.ArmyData tarmy, Fb2Smap.BuildData buildData, float damageTime) {
        return getDamageForArmy(game, army, tarmy, buildData, true, damageTime);
    }*/


   /* //isCombat 是否实战  如果是否则屏蔽一些功能
    public static int getDamageForArmy(MainGame game, Fb2Smap.ArmyData army, Fb2Smap.ArmyData tarmy, Fb2Smap.BuildData buildData, boolean isCombat, float damageTime) {
       *//* if(buildData==null){
            Gdx.app.error("getDamageForArmy is error",army.getHexagonIndex()+":"+tarmy.getHexagonIndex());
        }*//*
        if (army.isUnitGroup()) {
            return getDamageForGroupUnit(game, army, tarmy, buildData, isCombat, damageTime);
        }
        int armyId = army.getUnitArmyId0();
        //根据海陆计算伤害差,如果是非4,8的则伤害削减
        if (army.getArmyType() != 4 && army.getArmyType() != 8 && army.potionIsSea()) {
            armyId = army.getTransportType() + 1400;
        }
        Element armyE = game.gameConfig.getDEF_ARMY().getElementById(armyId);
        Element tarmyE = game.gameConfig.getDEF_ARMY().getElementById(tarmy.getUnitArmyId0());

        Fb2Smap.LegionData l = army.getLegionData();
        Fb2Smap.LegionData tl = tarmy.getLegionData();

        int direct = army.getDirect(tarmy.getHexagonIndex());
        int tm = 0;
        int am = 0;
        //获得伤害
        int damage;
        if (army.getGameMode() == 2) {
            damage = (int) (game.gameMethod.getUnitDamage(army.getLegionData(), army.getBuildData(), armyId, 0) * (100 + army.getWealMaxLv() * game.resGameConfig.addDamageForAckLv) / 100 * army.getGroupRate() + army.getUnitAbility() * game.resGameConfig.addAtkEachRank);
        } else {
            damage = (int) (game.gameMethod.getUnitDamage(army.getLegionData(), army.getBuildData(), armyId, 0) * (100 + army.getUnitAbility(1) * game.resGameConfig.addDamageForAckLv) / 100 * army.getGroupRate() + army.getUnitAbility() * game.resGameConfig.addAtkEachRank);
        }
        if (buildData != null && buildData.getLegionIndex() == army.getLegionIndex() && buildData.getBuildWonder() > 0) {
            Element xE = game.gameConfig.getDEF_WONDER().getElementById(buildData.getBuildWonder());
            if (xE != null) {
                int function = xE.getInt("fucntion", 0);
                int effect = xE.getInt("effect", 0);
                int value = xE.getInt("value", 0);
                if (function == 5 && effect == 3) {
                    damage += value;
                }
            }
        }
        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && army.getGeneralIndex() > 0) {
                damage = damage * 2;
            }
        }
        if (tarmy.getArmyType() == 6 && tarmy.getUnitArmyId0() != 1606) {
            damage = damage - tarmy.getLegionData().getFortLvMax();
        }
        if (damage <= 0) {
            damage = 1;
        }
        Fb2Map.MapHexagon hexagon = tarmy.getHexagonData();
        //结算反击
        if (army.ifHaveFeature(25) && !army.isRound()) {
            damage = damage * (100 + army.getFeatureEffect(25)) / 100;
        }
        int weatherId = army.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("armyEfficiency", 100);
        if (army.ifHaveFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && army.ifHaveSkill(113) && ae < 100) {
            if (isCombat) {
                army.drawSkill(113);
            }
            ae = 100;
        }
        damage = damage * ae / 100;

        //获得武器伤害加成
        float weapBonus;
        if (army.getGameMode() == 2) {
            weapBonus = game.gameMethod.getWeaponValue(armyE.getInt("weapon"), tarmyE.getInt("type"), army.getWealMaxLv()) / 100f;
        } else {
            weapBonus = game.gameMethod.getWeaponValue(armyE.getInt("weapon"), tarmyE.getInt("type"), army.getUnitWealv0Value()) / 100f;
        }
        //public boolean ifFortReduceDamage(int armyType,int armyId,int armyFeature,int armyTransport,int tHexagon) {

        int missLv = 0;
        int climateZone = 0;
        if (buildData != null) {
            if (buildData.getLegionIndex() == army.getLegionIndex()) {
                missLv = buildData.getMineralLv();
            }
            climateZone = buildData.getClimateZone();
        }


        if (tarmy.getArmyType() == 5) {
            if (missLv > 0) {
                weapBonus = weapBonus + missLv * 3f / 100;
            }
        }


        if (weapBonus == 0) {
            return 0;
        }
        if (game.getSMapDAO().ifFortReduceDamage(army.getArmyType(), army.getUnitArmyId0(), army.getFeature(), army.getTransportType(), tarmy.getHexagonIndex())) {
            weapBonus = weapBonus * 0.5f;
        }

        damage = (int) (damage * weapBonus);

        int crit = army.getBorderAttackCrit();
        //进攻拥有下潜特性的单位时,暴击率增加
        if (tarmy.ifHaveFeature(17) && army.ifHaveFeature(18)) {
            damage = damage * (100 + army.getFeatureEffect(18)) / 100;
            if (isCombat) {
                army.drawFeature(18);
            }
        }
        if (army.ifHaveFeature(21) && army.getIfMove() == 0) {
            crit = crit + army.getFeatureEffect(21);
            if (isCombat) {
                army.drawFeature(21);
            }
        }

        if (army.ifHaveFeature(23) && tarmy.potionIsSea()) {
            crit = crit + army.getFeatureEffect(23);
            if (isCombat) {
                army.drawFeature(23);
            }
        }
        if (l.ifAiCheatChance() && game.getSMapDAO().roundState != 0 && !tarmy.isPlayer() && !army.isPlayer() && tarmy.isPlayerAlly() && !army.isPlayerAlly()) {//队友会受到敌人额外的暴击
            damage = damage * 2 + Math.abs(tl.varRegionCount - l.varRegionCount) + Math.abs(tl.incomeMoney - l.incomeMoney);
            // Gdx.app.log("ai_crit: " ,crit+"");
        }
        if (game.getSMapDAO().masterData.getPlayerMode() != 2) {
            int terrainId = army.getTerrainId();
            damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, army.getArmyType(), army.getUnitArmyId0(), terrainId)) / 100;
            crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, army.getArmyType(), army.getUnitArmyId0());
            if (terrainId == 0 && !army.potionIsSea() && army.ifHaveSkill(73)) {
                damage = damage * (100 + army.getSkillEffect(73)) / 100;
                if (isCombat) {
                    army.drawSkill(73);
                }
            }
            if (!army.potionIsSea() && (climateZone == 8 || terrainId == 4 || weatherId == 5 || weatherId == 6) && army.ifHaveSkill(83)) {
                damage = damage * (100 + army.getSkillEffect(83)) / 100;
                if (isCombat) {
                    army.drawSkill(83);
                }
            }
            if (!army.potionIsSea() && (terrainId == 4 || terrainId == 5 || terrainId == 6) && army.ifHaveSkill(84)) {
                damage = damage * (100 + army.getSkillEffect(84)) / 100;
                if (isCombat) {
                    army.drawSkill(84);
                }
            }
            if ((army.potionIsSea() || terrainId == 1 || terrainId == 2 || terrainId == 11 || weatherId == 2 || weatherId == 4) && army.ifHaveSkill(94)) {
                damage = damage * (100 + army.getSkillEffect(94)) / 100;
                if (isCombat) {
                    army.drawSkill(94);
                }
            }
            if (!army.potionIsSea() && (climateZone == 11 || weatherId == 3 || weatherId == 7) && army.ifHaveSkill(102)) {
                damage = damage * (100 + army.getSkillEffect(102)) / 100;
                if (isCombat) {
                    army.drawSkill(102);
                }
            }
        }
        //插入技能结算 陆:陆
        boolean ifRound = army.isRound();
        int skillEffect = army.getSkillEffect(91);
        if (army.ifHaveSkill(91) && army.getHpRate() < skillEffect) {
            damage = damage * (100 + skillEffect) / 100;
            if (isCombat) {
                army.drawSkill(91);
            }
        }
        if (tarmy.ifHaveSkill(8) && army.getArmyType() == 1) {
            damage = damage * (100 - tarmy.getSkillEffect(8)) / 100;
            if (isCombat) {
                tarmy.drawSkill(8);
            }
        }
        if (tarmy.ifHaveSkill(50) && army.getArmyType() == 2) {
            damage = damage * (100 - tarmy.getSkillEffect(50)) / 100;
            if (isCombat) {
                tarmy.drawSkill(50);
            }
        }
        if (tarmy.ifHaveSkill(56) && army.getArmyType() == 4) {
            damage = damage * (100 - tarmy.getSkillEffect(56)) / 100;
            if (isCombat) {
                tarmy.drawSkill(56);
            }
        }
        if (tarmy.ifHaveSkill(80) && army.getArmyType() == 8) {
            damage = damage * (100 - tarmy.getSkillEffect(80)) / 100;
            if (isCombat) {
                tarmy.drawSkill(80);
            }
        }
        if (tarmy.ifHaveSkill(121) && army.getArmyType() == 3) {
            damage = damage * (100 - tarmy.getSkillEffect(121)) / 100;
            if (isCombat) {
                tarmy.drawSkill(121);
            }
        }
        if (tarmy.ifHaveSkill(30) && army.getArmyType() == 5) {
            damage = damage * (100 - tarmy.getSkillEffect(30)) / 100;
            if (isCombat) {
                tarmy.drawSkill(30);
            }
        }
        if (tarmy.ifHaveSkill(57) && army.getMaxRange() < 2) {
            damage = damage * (100 - tarmy.getSkillEffect(57)) / 100;
            if (isCombat) {
                tarmy.drawSkill(57);
            }
        }
        if (tarmy.ifHaveSkill(59) && army.getMaxRange() > 1) {
            damage = damage * (100 - tarmy.getSkillEffect(59)) / 100;
            if (isCombat) {
                tarmy.drawSkill(59);
            }
        }
        if (tarmy.ifHaveSkill(67)) {
            damage = damage * (100 + tarmy.getSkillEffect(67)) / 100;
            if (isCombat) {
                tarmy.drawSkill(67);
            }
        }
        if (army.ifHaveSkill(67)) {
            damage = damage * (100 + army.getSkillEffect(67)) / 100;
            if (isCombat) {
                army.drawSkill(67);
            }
        }
        if (tarmy.ifHaveSkill(103)) {
            damage = damage * (100 + tarmy.getSkillEffect(103)) / 100;
            if (isCombat) {
                tarmy.drawSkill(103);
            }
        }
        if (army.ifHaveSkill(103)) {
            damage = damage * (100 + army.getSkillEffect(103)) / 100;
            if (isCombat) {
                army.drawSkill(103);
            }
        }
        if (army.ifHaveSkill(104)) {
            damage = damage * (100 - ComUtil.min(army.getArmyMorale(), army.getHpRate())) / 100;
            if (isCombat) {
                army.drawSkill(104);
            }
        }
        if (army.ifHaveSkill(10) && army.inCity()) {
            damage = damage * (100 + army.getSkillEffect(10)) / 100;
            if (isCombat) {
                army.drawSkill(10);
            }
        }
        if (army.ifHaveSkill(69)) {
            damage = damage * (100 + army.getSkillEffect(69) * army.getDistance(tarmy.getHexagonIndex())) / 100;
            if (isCombat) {
                army.drawSkill(69);
            }
        }
        if (army.ifHaveSkill(53) && tarmy.getArmyType() == 1) {//当目标是步兵时,必定对其暴击
            crit = 100;
            if (isCombat) {
                army.drawSkill(53);
            }
        }
        if (army.ifHaveSkill(4)) {//暴击几率+{0}
            crit += army.getSkillEffect(4);
            if (isCombat) {
                army.drawSkill(4);
            }
        }
        if (army.ifHaveSkill(5) && tarmy.getArmyType() == 6) {
            damage = damage * (100 + army.getSkillEffect(5)) / 100;
            if (isCombat) {
                army.drawSkill(5);
            }
        }
        if (ifRound) {
            if (army.ifHaveSkill(2) && army.getIfMove() == 0) {
                damage = damage * (100 + army.getSkillEffect(2)) / 100;
                if (isCombat) {
                    army.drawSkill(2);
                }
            }
            if (army.ifHaveSkill(3) && army.getHpRate() > tarmy.getHpRate()) {
                damage = damage * (100 + army.getSkillEffect(3)) / 100;
                if (isCombat) {
                    army.drawSkill(3);
                }
            }
            if (isCombat && army.ifHaveSkill(6)) {//每次攻击额外损失20%血量但使伤害增加99999
                damage = 99999;
                army.setArmyHpNow((int) (army.getArmyHpNow() * 0.8f) + 1);
                if (isCombat) {
                    army.drawSkill(6);
                }
            }
            if (army.ifHaveSkill(15) && tarmy.getArmyType() == 8) {
                damage = damage * (100 + army.getSkillEffect(15)) / 100;
                if (isCombat) {
                    army.drawSkill(15);
                }
            }
            if (isCombat && army.triggerSkill(31) && tarmy.getArmyType() == 6) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tarmy.addArmyRound(1);
                if (isCombat) {
                    army.drawSkill(31);
                }
            }
            if (isCombat && army.triggerSkill(34)) {//攻击后,有{1}%几率使敌人无法行动
                tarmy.addArmyRound(1);
                if (isCombat) {
                    army.drawSkill(34);
                }
            }
            if (isCombat && army.triggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                am += army.getSkillEffect(37);
                army.drawSkill(37);
            }
            if (army.ifHaveSkill(40) && tarmy.getArmyType() != 4 && tarmy.getArmyType() != 8 && tarmy.getTransportType() == 0) {
                damage = damage * (100 + army.getSkillEffect(40)) / 100;
                if (isCombat) {
                    army.drawSkill(40);
                }
            }
            if (army.ifHaveSkill(42) && tarmy.getAroundUnitCountIA1(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                damage = damage * (100 + army.getSkillEffect(42)) / 100;
                if (isCombat) {
                    army.drawSkill(42);
                }
            }
            if (army.ifHaveSkill(43) && tarmy.getAroundUnitCountIA1(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                damage = damage * (100 + army.getSkillEffect(43)) / 100;
                if (isCombat) {
                    army.drawSkill(43);
                }
            }
            if (army.ifHaveSkill(51) && (army.getHpRate() > tarmy.getHpRate() || army.getArmyHpNow() > tarmy.getArmyHpNow() || army.getArmyMorale() > tarmy.getArmyMorale())) {
                damage = damage * (100 + army.getSkillEffect(51)) / 100;
                if (isCombat) {
                    army.drawSkill(51);
                }
            }
            if (isCombat && army.triggerSkill(63) && tarmy.getGeneralIndex() == 0) {//进攻后有{1}%几率使敌方普通单位叛变
                tarmy.setLegionIndex(army.getLegionIndex());
                if (isCombat) {
                    army.drawSkill(63);
                }
            }
            if (army.ifHaveSkill(64)) {//暴击几率随血量减少增加
                crit += (100 - army.getHpRate()) / 2;
                if (isCombat) {
                    army.drawSkill(64);
                }
            }
            if (army.ifHaveSkill(71) && tarmy.getHpRate() < army.getSkillEffect(71)) {
                damage = 99999;
                if (isCombat) {
                    army.drawSkill(71);
                }
            }
            if (army.ifHaveSkill(74) && tarmy.getArmyType() == 6) {
                damage = damage * (100 + army.getSkillEffect(74)) / 100;
                if (isCombat) {
                    army.drawSkill(74);
                }
            }
            if (army.ifHaveSkill(75) && (army.getHpRate() < tarmy.getHpRate() || army.getArmyHpNow() < tarmy.getArmyHpNow() || army.getArmyMorale() < tarmy.getArmyMorale())) {
                damage = damage * (100 + army.getSkillEffect(75)) / 100;
                if (isCombat) {
                    army.drawSkill(75);
                }
            }
            if (army.ifHaveSkill(77)) {//攻击时必然暴击
                crit = 100;
                if (isCombat) {
                    army.drawSkill(77);
                }
            }
            if (army.ifHaveSkill(81)) {
                damage = damage * (100 + army.getSkillEffect(81)) / 100;
                if (isCombat) {
                    army.drawSkill(81);
                }
            }
            if (isCombat && army.ifHaveSkill(105) && army.getArmyRank() > tarmy.getArmyRank() && tarmy.getArmyType() != 6) {
                int effect = army.getSkillEffect(105);
                damage = damage * (100 + effect) / 100;
                tm += -effect;
                if (isCombat) {
                    army.drawSkill(105);
                }
            }
            if (army.ifHaveSkill(111) && tarmy.potionIsSea() && (tarmy.getTransportType() != 1 && tarmy.getTransportType() != 2 && tarmy.getUnitArmyId0() != 1401 && tarmy.getUnitArmyId0() != 1402)) {
                damage = damage * (100 + army.getSkillEffect(111)) / 100;
                if (isCombat) {
                    army.drawSkill(111);
                }
            }
            if (army.ifHaveSkill(116) && tarmy.getArmyType() == 3) {
                damage = damage * (100 + army.getSkillEffect(116)) / 100;
                if (isCombat) {
                    army.drawSkill(116);
                }
            }
            if (army.ifHaveSkill(124) && tarmy.getArmyType() == 1) {
                damage = damage * (100 + army.getSkillEffect(124)) / 100;
                if (isCombat) {
                    army.drawSkill(124);
                }
            }

            if (isCombat && army.triggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tarmy.addArmyRound(army.getSkillEffect(126));
                if (isCombat) {
                    army.drawSkill(126);
                }
            }
        } else {
            if (army.ifHaveSkill(86)) {
                damage = damage * (100 + army.getSkillEffect(86)) / 100;
                if (isCombat) {
                    army.drawSkill(86);
                }
            }
        }
        //有5%*攻击等级的几率攻击力翻倍
        if (ComUtil.ifGet(crit)) {
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
            if (isCombat && ifRound) {
                if (army.ifHaveSkill(13)) {//对敌人暴击时可以使敌人士气下降{0}
                    tm += -army.getSkillEffect(13);
                    if (isCombat) {
                        army.drawSkill(13);
                    }
                }
                if (army.ifHaveSkill(44)) {//对敌人造成暴击伤害时,敌人无法行动
                    tarmy.addArmyRound(1);
                    if (isCombat) {
                        army.drawSkill(44);
                    }
                }
                if (army.ifHaveSkill(85) && army.getArmyMorale() > tarmy.getArmyMorale()) {//暴击可以降低低于我方士气敌人的能力
                    tarmy.randomReduceSkillLv(1);
                    if (isCombat) {
                        army.drawSkill(85);
                    }
                }
            }
        }

        if (game.resGameConfig.ifTerrainEffect) {
            int terrain = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrain);
            if (terrainE != null) {
                int te = game.gameMethod.getTerrainEffect(terrainE, army.getArmyType());//我方类型对目标类型造成的伤害比
                if (te < 100) {
                    if (army.ifHaveFeature(27)) {
                        te = 100;
                        if (isCombat) {
                            army.drawFeature(27);
                        }
                    } else if (army.ifHaveSkill(17)) {
                        te = 100;
                        if (isCombat) {
                            army.drawSkill(17);
                        }
                    }
                }
                if (hexagon.isSea() && army.ifHaveSkill(113) && te < 100) {
                    te = 100;
                    if (isCombat) {
                        army.drawSkill(113);
                    }
                }
                damage = damage * te / 100;
                tm += terrainE.getInt("exrLos", 0);
            }
        }


        //如果是建筑,则根据actlv来增加坦度,因为actlv没用
        if (tarmy.ifHaveFeature(11) && tarmy.triggerFeature(11)) {
            if (ComUtil.ifGet((tarmy.getArmyRank() - army.getArmyRank()) * 2 * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
                if (isCombat) {
                    tarmy.drawFeature(11);
                }
            } else if (ComUtil.ifGet((tarmy.getArmyRank() - army.getArmyRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
            }
        } else {   //有5%*防御等级的几率豁免受到的伤害减半
            if (ComUtil.ifGet((tarmy.getArmyRank() - army.getArmyRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
            }
        }

        //如果进攻的是飞机
        if (army.getArmyType() == 5) {
            if (buildData.getLegionIndex() == tarmy.getLegionIndex()) {
                damage = damage * (100 - buildData.getMissileLvNow() * 5) / 100;
            }
        }


        //根据士气对伤害进行判断
        if (army.ifHaveSkill(61) && army.getHpRate() < 50) {
            if (isCombat) {
                army.drawSkill(61);
            }
        } else {
            damage = damage * (army.getHpRate() / 2 + 50) / 100;
        }


        if (damage > tarmy.getArmyHpMax() / 2) {
            int od = damage;
            damage = ComUtil.max(damage / 2, tarmy.getArmyHpMax() / 2);
            if (isCombat && army.canCreateRDialogue() && tl.getInternIndex() != 0) {
                game.getSMapDAO().addDialogueData(army, 7, tl.legionName);
            }
            tm += -(od - damage) * 100 / tarmy.getArmyHpMax();
        }

        if (game.getSMapDAO().masterData.getPlayerMode() != 2 && tarmy.getArmyType() != 6) {
            //根据位置计算伤害差值
            float damageRate = getDamageByDirect(army, tarmy, game.getSMapDAO().masterData.getWidth());

            damage = (int) (damage * damageRate);
            if (damageRate > 1 && army.ifHaveSkill(9)) {//对非正面敌军造成的伤害增加{0}%
                damage = damage * (100 + army.getSkillEffect(9)) / 100;
                if (isCombat) {
                    army.drawSkill(9);
                }
            }
            if (damageRate > 1 && tarmy.ifHaveSkill(118)) {
                damage = damage * (100 - tarmy.getSkillEffect(118)) / 100;
                if (isCombat) {
                    tarmy.drawSkill(118);
                }
            }
            if (isCombat && damageRate == 2f) {
                if (tarmy.armyActor != null) {
                    tarmy.armyActor.updArmyModelDirect();
                }
                tm += -ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax / 2, game.resGameConfig.unitMoraleChangeValueMax);
                army.setArmyMoraleChange(ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax / 2, game.resGameConfig.unitMoraleChangeValueMax));
            }

            if (tarmy.isUnitGroup() && tarmy.getArmyType() != 6 && !tarmy.potionIsSea()) {
                direct = tarmy.getDirect(army.getHexagonIndex());
                if (!tarmy.ifUnitGroupIsFormation(direct, false)) {
                    int uc = ComUtil.max(tarmy.getUnitGroup(), 1);
                    damage = damage * uc;
                }
            }
        }

        if (isCombat) {
            army.setArmyMoraleChange(am);
            tarmy.setArmyMoraleChange(tm);
            army.drawMorale(am, damageTime);
            tarmy.drawMorale(tm, damageTime);
        }
        if (army.triggerSkill(21) && tarmy.getArmyType() == 1) {
            if (isCombat) {
                army.drawSkill(21);
            }
        } else if (army.triggerSkill(22) && tarmy.potionIsSea()) {
            if (isCombat) {
                army.drawSkill(22);
            }
        } else if (army.triggerSkill(24) && tarmy.getArmyType() == 6) {
            if (isCombat) {
                army.drawSkill(24);
            }
        } else if (army.triggerSkill(25) && tarmy.getArmyType() == 4) {
            if (isCombat) {
                army.drawSkill(25);
            }
        } else if (army.triggerSkill(26) && tarmy.getArmyType() == 5) {
            if (isCombat) {
                army.drawSkill(26);
            }
        } else if (army.triggerSkill(27) && tarmy.getArmyType() == 2) {
            if (isCombat) {
                army.drawSkill(27);
            }
        } else if (army.triggerSkill(76) && tarmy.getArmyType() == 1) {
            if (isCombat) {
                army.drawSkill(76);
            }
        } else if (army.triggerSkill(28) && tarmy.getArmyType() == 8) {
            if (isCombat) {
                army.drawSkill(28);
            }
        } else if (army.triggerSkill(29) && tarmy.getArmyType() == 3) {
            if (isCombat) {
                army.drawSkill(29);
            }
        } else if (army.triggerSkill(96)) {
            if (isCombat) {
                army.drawSkill(96);
            }
        } else {
            damage = damage * 100 / (100 + (tarmy.getArmor() * (50 + tarmy.getArmyMorale()) / 100));
        }
        if (tarmy.getArmyType() == 5) {
            int minD = Math.min(10, tarmy.getArmyLife() / 10 + 1);
            if (damage < minD) {
                damage = minD;
            }
        } else {
            int minD = Math.min(10, tarmy.getArmyLife() / 7 + 1);
            if (damage < minD) {
                damage = minD;
            }
        }
        return damage;
    }
*/

    //unitGroup 群体战斗 TODO

   /* private static int getDamageForGroupUnit(MainGame game, Fb2Smap.ArmyData army, Fb2Smap.ArmyData tarmy, Fb2Smap.BuildData buildData, boolean isCombat, float damageTime) {


        int armyId = army.getUnitArmyId0();
        int direct = army.getDirect(tarmy.getHexagonIndex());
        //根据海陆计算伤害差,如果是非4,8的则伤害削减
        if (army.getArmyType() != 4 && army.getArmyType() != 8 && army.potionIsSea()) {
            armyId = army.getTransportType() + 1400;
        } else if (direct != 0) {
            armyId = army.getUnitGroupFormationArmyId(direct);
        }
        // Element armyE=game.gameConfig.getDEF_ARMY().getElementById(armyId);
        int tDirect = tarmy.getDirect(army.getHexagonIndex());
        boolean isRangeAtk = tarmy.getDirectByBorderId(army.getHexagonIndex()) == -1;
        Element tarmyE;
        //tarmyE 指受到攻击的类型
        if (isRangeAtk) {//远程
            tarmyE = game.gameConfig.getDEF_ARMY().getElementById(tarmy.getUnitArmyId0());
        } else {
            int tarmyId = tarmy.getUnitGroupFormationArmyId(tDirect);
            if (tarmyId == 0) {
                tarmyE = game.gameConfig.getDEF_ARMY().getElementById(tarmy.getUnitArmyId0());
            } else {
                tarmyE = game.gameConfig.getDEF_ARMY().getElementById(tarmyId);
            }
        }

        Fb2Smap.LegionData l = army.getLegionData();
        Fb2Smap.LegionData tl = tarmy.getLegionData();

        int damage;
        //获得伤害
        if (isRangeAtk) {//远程
            damage = game.gameMethod.getUnitGroupUnitDamage(army.getLegionData(), army, 0, tarmy.getHexagonIndex()) + army.getUnitAbility() * game.resGameConfig.addAtkEachRank;
        } else {//近战
            damage = game.gameMethod.getUnitGroupUnitDamage(army.getLegionData(), army, 0, tarmy.getHexagonIndex()) + army.getUnitAbility() * game.resGameConfig.addAtkEachRank;
            if (army.ifInArmyRange(tarmy.getHexagonIndex()) && army.ifUnitGroupIsFormation(direct, false)) {
                damage += game.gameMethod.getUnitGroupUnitDamage(army.getLegionData(), army, 0, tarmy.getHexagonIndex()) / 2;
            }
        }
        if (buildData != null && buildData.getLegionIndex() == army.getLegionIndex() && buildData.getBuildWonder() > 0) {
            Element xE = game.gameConfig.getDEF_WONDER().getElementById(buildData.getBuildWonder());
            if (xE != null) {
                int function = xE.getInt("fucntion", 0);
                int effect = xE.getInt("effect", 0);
                int value = xE.getInt("value", 0);
                if (function == 5 && effect == 3) {
                    damage += value;
                }
            }
        }
        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && army.getGeneralIndex() > 0) {
                damage = damage * 2;
            }
        }
        if (tarmy.getArmyType() == 6 && tarmy.getUnitArmyId0() != 1606) {
            damage = damage - tarmy.getLegionData().getFortLvMax();
        }
        if (damage <= 0) {
            damage = 1;
        }
        Fb2Map.MapHexagon hexagon = tarmy.getHexagonData();
        //结算反击
        if (army.ifHaveFeature(25) && !army.isRound()) {
            damage = damage * (100 + army.getFeatureEffect(25)) / 100;
        }
        int weatherId = army.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("armyEfficiency", 100);
        if (army.ifHaveFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && army.ifHaveSkill(113) && ae < 100) {
            army.drawSkill(113);
            ae = 100;
        }
        damage = damage * ae / 100;

        boolean ifAttackHaveFormation = false;
        if (direct > 0 && army.ifUnitGroupIsFormation(direct, true)) {
            ifAttackHaveFormation = true;
        }
        int am = 0;
        int tm = 0;
        //获得武器伤害加成
        float weapBonus = game.gameMethod.getWeaponValue(army, direct, tarmyE.getInt("type")) / 100f;
        //public boolean ifFortReduceDamage(int armyType,int armyId,int armyFeature,int armyTransport,int tHexagon) {

        int missLv = 0;
        int climateZone = 0;
        if (buildData != null) {
            if (buildData.getLegionIndex() == army.getLegionIndex()) {
                missLv = buildData.getMineralLv();
            }
            climateZone = buildData.getClimateZone();
        }


        if (tarmy.getArmyType() == 5) {
            if (missLv > 0) {
                weapBonus = weapBonus + missLv * 3f / 100;
            }*//*

            if(weapBonus==0){
                Fb2Smap.BuildData aBuild=army.getBuildData();
                if(aBuild.getLegionIndex()==army.getLegionIndex()){
                    weapBonus= weapBonus+aBuild.getMissileLvNow()*3f/100;
                }
            }*//*
        }


        if (weapBonus == 0) {
            return 0;
        }
        if (game.getSMapDAO().ifFortReduceDamage(army.getArmyType(), army.getUnitArmyId0(), army.getFeature(), army.getTransportType(), tarmy.getHexagonIndex())) {
            weapBonus = weapBonus * 0.5f;
        }

        damage = (int) (damage * weapBonus);

        int crit;
        if (direct == -1) {
            crit = army.rangeCritChance;
        } else {
            crit = army.getBorderAttackCrit();
        }
        boolean ifTArmyHaveFormation = tarmy.ifUnitGroupIsFormation(tDirect, true);
        if (!ifTArmyHaveFormation) {
            crit *= 2;
        }
        //进攻拥有下潜特性的单位时,暴击率增加
        if (tarmy.ifHaveFeature(17) && army.ifHaveFeature(18)) {
            damage = damage * (100 + army.getFeatureEffect(18)) / 100;
            army.drawFeature(18);
        }
        if (army.ifHaveFeature(21) && army.getIfMove() == 0) {
            crit = crit + army.getFeatureEffect(21);
            army.drawFeature(21);
        }

        if (army.ifHaveFeature(23) && tarmy.potionIsSea()) {
            crit = crit + army.getFeatureEffect(23);
            army.drawFeature(23);
        }
        if (l.ifAiCheatChance() && game.getSMapDAO().roundState != 0 && !tarmy.isPlayer() && !army.isPlayer() && tarmy.isPlayerAlly() && !army.isPlayerAlly()) {//队友会受到敌人额外的暴击
            damage = damage * 2 + Math.abs(tl.varRegionCount - l.varRegionCount) + Math.abs(tl.incomeMoney - l.incomeMoney);
            // Gdx.app.log("ai_crit: " ,crit+"");
        }
        if (game.getSMapDAO().masterData.getPlayerMode() != 2) {
            int terrainId = army.getTerrainId();
            damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, army.getArmyType(), army.getUnitArmyId0(), terrainId)) / 100;
            crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, army.getArmyType(), army.getUnitArmyId0());
            if (terrainId == 0 && !army.potionIsSea() && army.ifHaveSkill(73)) {
                damage = damage * (100 + army.getSkillEffect(73)) / 100;
                army.drawSkill(73);
            }
            if (!army.potionIsSea() && (climateZone == 8 || terrainId == 4 || weatherId == 5 || weatherId == 6) && army.ifHaveSkill(83)) {
                damage = damage * (100 + army.getSkillEffect(83)) / 100;
                army.drawSkill(83);
            }
            if (!army.potionIsSea() && (terrainId == 4 || terrainId == 5 || terrainId == 6) && army.ifHaveSkill(84)) {
                damage = damage * (100 + army.getSkillEffect(84)) / 100;
                army.drawSkill(84);
            }
            if ((army.potionIsSea() || terrainId == 1 || terrainId == 2 || terrainId == 11 || weatherId == 2 || weatherId == 4) && army.ifHaveSkill(94)) {
                damage = damage * (100 + army.getSkillEffect(94)) / 100;
                army.drawSkill(94);
            }
            if (!army.potionIsSea() && (climateZone == 11 || weatherId == 3 || weatherId == 7) && army.ifHaveSkill(102)) {
                damage = damage * (100 + army.getSkillEffect(102)) / 100;
                army.drawSkill(102);
            }
        }
        //插入技能结算 陆:陆
        boolean ifRound = army.isRound();
        int skillEffect = army.getSkillEffect(91);
        if (army.ifHaveSkill(91) && army.getHpRate() < skillEffect) {
            damage = damage * (100 + skillEffect) / 100;
            army.drawSkill(91);
        }
        if (tarmy.ifHaveSkill(8) && army.getArmyType() == 1) {
            damage = damage * (100 - tarmy.getSkillEffect(8)) / 100;
            tarmy.drawSkill(8);
        }
        if (tarmy.ifHaveSkill(50) && army.getArmyType() == 2) {
            damage = damage * (100 - tarmy.getSkillEffect(50)) / 100;
            tarmy.drawSkill(50);
        }
        if (tarmy.ifHaveSkill(56) && army.getArmyType() == 4) {
            damage = damage * (100 - tarmy.getSkillEffect(56)) / 100;
            tarmy.drawSkill(56);
        }
        if (tarmy.ifHaveSkill(80) && army.getArmyType() == 8) {
            damage = damage * (100 - tarmy.getSkillEffect(80)) / 100;
            tarmy.drawSkill(80);
        }
        if (tarmy.ifHaveSkill(121) && army.getArmyType() == 3) {
            damage = damage * (100 - tarmy.getSkillEffect(121)) / 100;
            tarmy.drawSkill(121);
        }
        if (tarmy.ifHaveSkill(30) && army.getArmyType() == 5) {
            damage = damage * (100 - tarmy.getSkillEffect(30)) / 100;
            tarmy.drawSkill(30);
        }
        if (tarmy.ifHaveSkill(57) && army.getMaxRange() < 2) {
            damage = damage * (100 - tarmy.getSkillEffect(57)) / 100;
            tarmy.drawSkill(57);
        }
        if (tarmy.ifHaveSkill(59) && army.getMaxRange() > 1) {
            damage = damage * (100 - tarmy.getSkillEffect(59)) / 100;
            tarmy.drawSkill(59);
        }
        if (tarmy.ifHaveSkill(67)) {
            damage = damage * (100 + tarmy.getSkillEffect(67)) / 100;
            tarmy.drawSkill(67);
        }
        if (army.ifHaveSkill(67)) {
            damage = damage * (100 + army.getSkillEffect(67)) / 100;
            army.drawSkill(67);
        }
        if (tarmy.ifHaveSkill(103)) {
            damage = damage * (100 + tarmy.getSkillEffect(103)) / 100;
            tarmy.drawSkill(103);
        }
        if (army.ifHaveSkill(103)) {
            damage = damage * (100 + army.getSkillEffect(103)) / 100;
            army.drawSkill(103);
        }
        if (army.ifHaveSkill(104)) {
            damage = damage * (100 - ComUtil.min(army.getArmyMorale(), army.getHpRate())) / 100;
            army.drawSkill(104);
        }
        if (army.ifHaveSkill(10) && army.inCity()) {
            damage = damage * (100 + army.getSkillEffect(10)) / 100;
            army.drawSkill(10);
        }
        if (army.ifHaveSkill(69)) {
            damage = damage * (100 + army.getSkillEffect(69) * army.getDistance(tarmy.getHexagonIndex())) / 100;
            army.drawSkill(69);
        }
        if (army.ifHaveSkill(4)) {//暴击几率+{0}
            crit += army.getSkillEffect(4);
            army.drawSkill(4);
        }
        if (army.ifHaveSkill(5) && tarmy.getArmyType() == 6) {
            damage = damage * (100 + army.getSkillEffect(5)) / 100;
            army.drawSkill(5);
        }
        if (ifRound) {
            if (army.ifHaveSkill(2) && army.getIfMove() == 0) {
                damage = damage * (100 + army.getSkillEffect(2)) / 100;
                army.drawSkill(2);
            }
            if (army.ifHaveSkill(3) && army.getHpRate() > tarmy.getHpRate()) {
                damage = damage * (100 + army.getSkillEffect(3)) / 100;
                army.drawSkill(3);
            }
            if (isCombat && army.ifHaveSkill(6)) {//每次攻击额外损失20%血量但使伤害增加99999
                damage = 99999;
                army.setArmyHpNow((int) (army.getArmyHpNow() * 0.8f) + 1);
                army.drawSkill(6);
            }
            if (army.ifHaveSkill(15) && tarmy.getArmyType() == 8) {
                damage = damage * (100 + army.getSkillEffect(15)) / 100;
                army.drawSkill(15);
            }
            if (isCombat && army.triggerSkill(31) && tarmy.getArmyType() == 6) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tarmy.addArmyRound(1);
                army.drawSkill(31);
            }
            if (isCombat && army.triggerSkill(34)) {//攻击后,有{1}%几率使敌人无法行动
                tarmy.addArmyRound(1);
                army.drawSkill(34);
            }
            if (isCombat && army.triggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                am += army.getSkillEffect(37);
                army.drawSkill(37);
            }
            if (army.ifHaveSkill(40) && tarmy.getArmyType() != 4 && tarmy.getArmyType() != 8 && tarmy.getTransportType() == 0) {
                damage = damage * (100 + army.getSkillEffect(40)) / 100;
                army.drawSkill(40);
            }
            if (army.ifHaveSkill(42) && tarmy.getAroundUnitCountIA1(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                damage = damage * (100 + army.getSkillEffect(42)) / 100;
                army.drawSkill(42);
            }
            if (army.ifHaveSkill(43) && tarmy.getAroundUnitCountIA1(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                damage = damage * (100 + army.getSkillEffect(43)) / 100;
                army.drawSkill(43);
            }
            if (army.ifHaveSkill(51) && (army.getHpRate() > tarmy.getHpRate() || army.getArmyHpNow() > tarmy.getArmyHpNow() || army.getArmyMorale() > tarmy.getArmyMorale())) {
                damage = damage * (100 + army.getSkillEffect(51)) / 100;
                army.drawSkill(51);
            }
            if (isCombat && army.triggerSkill(63) && tarmy.getGeneralIndex() == 0) {//进攻后有{1}%几率使敌方普通单位叛变
                tarmy.setLegionIndex(army.getLegionIndex());
                army.drawSkill(63);
            }
            if (army.ifHaveSkill(64)) {//暴击几率随血量减少增加
                crit += (100 - army.getHpRate()) / 2;
                army.drawSkill(64);
            }
            if (army.ifHaveSkill(71) && tarmy.getHpRate() < army.getSkillEffect(71)) {
                damage = 99999;
                army.drawSkill(71);
            }
            if (army.ifHaveSkill(74) && tarmy.getArmyType() == 6) {
                damage = damage * (100 + army.getSkillEffect(74)) / 100;
                army.drawSkill(74);
            }
            if (army.ifHaveSkill(75) && (army.getHpRate() < tarmy.getHpRate() || army.getArmyHpNow() < tarmy.getArmyHpNow() || army.getArmyMorale() < tarmy.getArmyMorale())) {
                damage = damage * (100 + army.getSkillEffect(75)) / 100;
                army.drawSkill(75);
            }
            if (army.ifHaveSkill(77)) {//攻击时必然暴击
                crit = 100;
                army.drawSkill(77);
            }

            if (army.ifHaveSkill(53) && tarmy.getArmyType() == 1) {//当目标是步兵时,必定对其暴击
                crit = 100;
                army.drawSkill(53);
            }
            if (army.ifHaveSkill(81)) {
                damage = damage * (100 + army.getSkillEffect(81)) / 100;
                army.drawSkill(81);
            }
            if (isCombat && army.ifHaveSkill(105) && army.getArmyRank() > tarmy.getArmyRank() && tarmy.getArmyType() != 6) {
                int effect = army.getSkillEffect(105);
                damage = damage * (100 + effect) / 100;
                tm += effect;
                army.drawSkill(105);
            }
            if (army.ifHaveSkill(111) && tarmy.potionIsSea() && (tarmy.getTransportType() != 1 && tarmy.getTransportType() != 2 && tarmy.getUnitArmyId0() != 1401 && tarmy.getUnitArmyId0() != 1402)) {
                damage = damage * (100 + army.getSkillEffect(111)) / 100;
                army.drawSkill(111);
            }
            if (army.ifHaveSkill(116) && tarmy.getArmyType() == 3) {
                damage = damage * (100 + army.getSkillEffect(116)) / 100;
                army.drawSkill(116);
            }
            if (army.ifHaveSkill(124) && tarmy.getArmyType() == 1) {
                damage = damage * (100 + army.getSkillEffect(124)) / 100;
                army.drawSkill(124);
            }

            if (isCombat && army.triggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tarmy.addArmyRound(army.getSkillEffect(126));
                army.drawSkill(126);
            }
        } else {
            if (army.ifHaveSkill(86)) {
                damage = damage * (100 + army.getSkillEffect(86)) / 100;
                army.drawSkill(86);
            }
        }
        //有5%*攻击等级的几率攻击力翻倍
        if (ComUtil.ifGet(crit)) {
            if (crit > 100) {//如果暴击率超过100,则多余的暴击会转化为额外伤害
                damage = damage * crit / 100;
            }
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
            if (isCombat && ifRound) {
                if (army.ifHaveSkill(13)) {//对敌人暴击时可以使敌人士气下降{0}
                    tm += -army.getSkillEffect(13);
                    army.drawSkill(13);
                }
                if (army.ifHaveSkill(44)) {//对敌人造成暴击伤害时,敌人无法行动
                    tarmy.addArmyRound(1);
                    army.drawSkill(44);
                }
                if (army.ifHaveSkill(85) && army.getArmyMorale() > tarmy.getArmyMorale()) {//暴击可以降低低于我方士气敌人的能力
                    tarmy.randomReduceSkillLv(1);
                    army.drawSkill(85);
                }
            }
        }

        if (game.resGameConfig.ifTerrainEffect) {
            int terrain = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrain);
            if (terrainE != null) {
                int te = game.gameMethod.getTerrainEffect(terrainE, army.getArmyType());//我方类型对目标类型造成的伤害比
                if (te < 100) {
                    if (army.ifHaveFeature(27)) {
                        te = 100;
                        army.drawFeature(27);
                    } else if (army.ifHaveSkill(17)) {
                        te = 100;
                        army.drawSkill(17);
                    }
                }
                if (hexagon.isSea() && army.ifHaveSkill(113) && te < 100) {
                    te = 100;
                    army.drawSkill(113);
                }
                damage = damage * te / 100;
                tm += +terrainE.getInt("exrLos", 0);
            }
        }


        //如果是建筑,则根据等级差来增加坦度
        if (tarmy.ifHaveFeature(11) && tarmy.triggerFeature(11)) {
            if (ComUtil.ifGet((tarmy.getArmyRank() - army.getArmyRank()) * 2 * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
                tarmy.drawFeature(11);
            } else if (ComUtil.ifGet((tarmy.getArmyRank() - army.getArmyRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
            }
        } else {   //有5%*防御等级的几率豁免受到的伤害减半
            if (ComUtil.ifGet((tarmy.getArmyRank() - army.getArmyRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
            }
        }


        //如果进攻的是飞机
        if (army.getArmyType() == 5) {
            if (buildData.getLegionIndex() == tarmy.getLegionIndex()) {
                damage = damage * (100 - buildData.getMissileLvNow() * 5) / 100;
            }
        }


        //根据士气对伤害进行判断

        if (army.ifHaveSkill(61) && army.getHpRate() < 50) {
            army.drawSkill(61);
        } else {
            damage = damage * (army.getHpRate() / 2 + 50) / 100;
        }

       *//* if(damage>tarmy.getArmyHpMax()/2){
            int od=damage;
            damage=ComUtil.max(damage/2,tarmy.getArmyHpMax()/2);
            if(isCombat&&army.canCreateRDialogue()&&tl.getInternIndex()!=0){
                game.getSMapDAO().addDialogueData(army,7,tl.legionName);
            }
            tarmyMoraleChange+=-(od-damage)*100/tarmy.getArmyHpMax();
        }*//*

        if (game.getSMapDAO().masterData.getPlayerMode() != 2 && tarmy.getArmyType() != 6) {
            //根据位置计算伤害差值
            //根据位置计算伤害差值
            float damageRate = getDamageByDirect(army, tarmy, game.getSMapDAO().masterData.getWidth());
            damage = (int) (damage * damageRate);
            if (!ifTArmyHaveFormation && army.ifHaveSkill(9)) {//对非正面敌军造成的伤害增加{0}%
                damage = damage * (100 + army.getSkillEffect(9)) / 100;
                army.drawSkill(9);
            }
            if (ifTArmyHaveFormation && tarmy.ifHaveSkill(118)) {
                damage = damage * (100 - tarmy.getSkillEffect(118)) / 100;
                tarmy.drawSkill(118);
            }
            if (isCombat && GameMap.getHX(army.getHexagonIndex(), army.getMapW()) != GameMap.getHX(tarmy.getHexagonIndex(), tarmy.getMapW())) {
                if (tarmy.armyActor != null) {
                    tarmy.armyActor.updArmyModelDirect();
                }
                tm += -ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax / 2, game.resGameConfig.unitMoraleChangeValueMax);
                am += ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax / 2, game.resGameConfig.unitMoraleChangeValueMax);
            }
        }
        if (ifAttackHaveFormation && tarmy.isUnitGroup() && tarmy.getArmyType() != 6 && !tarmy.potionIsSea()) {
            direct = tarmy.getDirect(army.getHexagonIndex());
            if (!tarmy.ifUnitGroupIsFormation(direct, false)) {
                int uc = ComUtil.max(tarmy.getUnitGroup(), 1);
                damage = damage * uc;
            }
        }

        if (isCombat) {
            if (!ifTArmyHaveFormation) {
                if (tm > 0) {
                    tm = -tm;
                } else {
                    tm = tm * 2;
                }
                if (am < 0) {
                    am = -am;
                } else {
                    am = am * 2;
                }
            }
            if (tm != 0) {
                tarmy.setArmyMoraleChange(tm);
                tarmy.drawMorale(tm, damageTime);
            }
            if (am != 0) {
                army.setArmyMoraleChange(am);
                army.drawMorale(am, damageTime);
            }
        }


        *//*if(game.getSMapDAO().masterData.getPlayerMode()!=2&&tarmy.getArmyType()!=6){
            //根据位置计算伤害差值
            float damageRate=getDamageByDirect(army,tarmy,game.getSMapDAO().masterData.getWidth());

            damage= (int) (damage*damageRate);
            if(damageRate>1&&army.ifHaveSkill(9)){//对非正面敌军造成的伤害增加{0}%
                damage=damage*(100+army.getSkillEffect(9))/100;
                if(isCombat) {
                    army.drawSkill(9);}
            }
            if(damageRate>1&&tarmy.ifHaveSkill(118)){
                damage=damage*(100-tarmy.getSkillEffect(118))/100;
                if(isCombat) {
                    tarmy.drawSkill(118);}
            }
            if(isCombat&&damageRate==2f){
                if(tarmy.getArmyDirection()==0){
                    tarmy.setArmyDirection(1);
                }else {
                    tarmy.setArmyDirection(0);
                }
                if(tarmy.armyActor!=null){
                    tarmy.armyActor.updArmyModelDirect();
                }
                tm+=-ComUtil.getRandom( game.resGameConfig.unitMoraleChangeValueMax/2, game.resGameConfig.unitMoraleChangeValueMax);
                army.setArmyMoraleChange(ComUtil.getRandom( game.resGameConfig.unitMoraleChangeValueMax/2, game.resGameConfig.unitMoraleChangeValueMax));
            }
        }*//*

        if (army.triggerSkill(21) && tarmy.getArmyType() == 1) {
            army.drawSkill(21);
        } else if (army.triggerSkill(22) && tarmy.potionIsSea()) {
            army.drawSkill(22);
        } else if (army.triggerSkill(24) && tarmy.getArmyType() == 6) {
            army.drawSkill(24);
        } else if (army.triggerSkill(25) && tarmy.getArmyType() == 4) {
            army.drawSkill(25);
        } else if (army.triggerSkill(26) && tarmy.getArmyType() == 5) {
            army.drawSkill(26);
        } else if (army.triggerSkill(27) && tarmy.getArmyType() == 2) {
            army.drawSkill(27);
        } else if (army.triggerSkill(76) && tarmy.getArmyType() == 1) {
            army.drawSkill(76);
        } else if (army.triggerSkill(28) && tarmy.getArmyType() == 8) {
            army.drawSkill(28);
        } else if (army.triggerSkill(29) && tarmy.getArmyType() == 3) {
            army.drawSkill(29);
        } else if (army.triggerSkill(96)) {
            army.drawSkill(96);
        } else {
            damage = damage * 100 / (100 + (tarmy.getArmyFormationArmor(tDirect) * (50 + tarmy.getArmyMorale()) / 100));
        }
        if (tarmy.getArmyType() == 5) {
            int minD = Math.min(10, tarmy.getArmyLife() / 10 + 1);
            if (damage < minD) {
                damage = minD;
            }
        } else {
            int minD = Math.min(10, tarmy.getArmyLife() / 7 + 1);
            if (damage < minD) {
                damage = minD;
            }
        }
        return damage;
    }
*/
   /* private static int getDamageForGroupUnit(MainGame game, Fb2Smap.UnitData army, Fb2Smap.UnitData tarmy, Fb2Smap.BuildData buildData, boolean isCombat, float damageTime) {
        int direct = army.getDirect(tarmy.getHexagonIndex());
        int armyId = army.getUnitId(direct);
        // Element armyE=game.gameConfig.getDEF_ARMY().getElementById(armyId);
        int tDirect = tarmy.getDirect(army.getHexagonIndex());
        int tArmyId = tarmy.getUnitId(tDirect);
        Element tarmyE = game.gameConfig.getDEF_ARMY().getElementById(tArmyId);
        //tarmyE 指受到攻击的类型

        Fb2Smap.LegionData l = army.getLegionData();
        Fb2Smap.LegionData tl = tarmy.getLegionData();
        int unitType = army.getUnitType();
        int tUnitType = tarmy.getUnitType();

        int damage = army.getUnitDamage(buildData, tarmy.getHexagonIndex());
        //获得伤害

        if (buildData != null && buildData.getLegionIndex() == army.getLegionIndex() && buildData.getBuildWonder() > 0) {
            Element xE = game.gameConfig.getDEF_WONDER().getElementById(buildData.getBuildWonder());
            if (xE != null) {
                int function = xE.getInt("fucntion", 0);
                int effect = xE.getInt("effect", 0);
                int value = xE.getInt("value", 0);
                if (function == 5 && effect == 3) {
                    damage += value;
                }
            }
        }
        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && army.getGeneralIndex() > 0) {
                damage = damage * 2;
            }
        }
        if (tarmy.getUnitType() == 6 && tArmyId != 1606) {
            damage = damage - tarmy.getLegionData().getFortLvMax();
        }
        if (damage <= 0) {
            damage = 1;
        }
        Fb2Map.MapHexagon hexagon = tarmy.getHexagonData();
        //结算反击
        if (army.ifUnitHaveFeature(25) && !army.isRound()) {
            damage = damage * (100 + army.getUnitFeatureEffect(25)) / 100;
        }
        int weatherId = army.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("armyEfficiency", 100);
        if (army.ifUnitHaveFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && army.ifUnitHaveSkill(113) && ae < 100) {
            army.drawSkill(113);
            ae = 100;
        }
        damage = damage * ae / 100;

        boolean ifAttackHaveFormation = false;
        if (direct > 0 && army.ifUnitGroupIsFormation(direct, true)) {
            ifAttackHaveFormation = true;
        }
        int am = 0;
        int tm = 0;
        //获得武器伤害加成
        float weapBonus = army.getWeaponValue(direct, tarmyE.getInt("type")) / 100f;
        //public boolean ifFortReduceDamage(int armyType,int armyId,int armyFeature,int armyTransport,int tHexagon) {

        int missLv = 0;
        int climateZone = 0;
        if (buildData != null) {
            if (buildData.getLegionIndex() == army.getLegionIndex()) {
                missLv = buildData.getMineralLv();
            }
            climateZone = buildData.getClimateZone();
        }


        if (tarmy.getUnitType() == 5) {
            if (missLv > 0) {
                weapBonus = weapBonus + missLv * 3f / 100;
            }
        }


        if (weapBonus == 0) {
            return 0;
        }
        if (game.getSMapDAO().ifFortReduceDamage(unitType, armyId, army.getFeature(), army.getTransportType(), tarmy.getHexagonIndex())) {
            weapBonus = weapBonus * 0.5f;
        }

        damage = (int) (damage * weapBonus);

        int crit;
        if (direct == -1) {
            crit = army.getRangeAttackCrit();
        } else {
            crit = army.getBorderAttackCrit();
        }
        boolean ifTArmyHaveFormation = tarmy.ifUnitGroupIsFormation(tDirect, true);
        if (!ifTArmyHaveFormation) {
            crit *= 2;
        }
        //进攻拥有下潜特性的单位时,暴击率增加
        if (tarmy.ifUnitHaveFeature(17) && army.ifUnitHaveFeature(18)) {
            damage = damage * (100 + army.getUnitFeatureEffect(18)) / 100;
            army.drawFeature(18);
        }
        if (army.ifUnitHaveFeature(21) && army.getIfMove() == 0) {
            crit = crit + army.getUnitFeatureEffect(21);
            army.drawFeature(21);
        }

        if (army.ifUnitHaveFeature(23) && tarmy.potionIsSea()) {
            crit = crit + army.getUnitFeatureEffect(23);
            army.drawFeature(23);
        }
        if (l.ifAiCheatChance() && game.getSMapDAO().roundState != 0 && !tarmy.isPlayer() && !army.isPlayer() && tarmy.isPlayerAlly() && !army.isPlayerAlly()) {//队友会受到敌人额外的暴击
            damage = damage * 2 + Math.abs(tl.varRegionCount - l.varRegionCount) + Math.abs(tl.incomeMoney - l.incomeMoney);
            // Gdx.app.log("ai_crit: " ,crit+"");
        }
        if (game.getSMapDAO().masterData.getPlayerMode() != 2) {
            int terrainId = army.getTerrainId();
            damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, unitType, armyId, terrainId)) / 100;
            crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, unitType, armyId);
            if (terrainId == 0 && !army.potionIsSea() && army.ifUnitHaveSkill(73)) {
                damage = damage * (100 + army.getUnitSkillEffect(73)) / 100;
                army.drawSkill(73);
            }
            if (!army.potionIsSea() && (climateZone == 8 || terrainId == 4 || weatherId == 5 || weatherId == 6) && army.ifUnitHaveSkill(83)) {
                damage = damage * (100 + army.getUnitSkillEffect(83)) / 100;
                army.drawSkill(83);
            }
            if (!army.potionIsSea() && (terrainId == 4 || terrainId == 5 || terrainId == 6) && army.ifUnitHaveSkill(84)) {
                damage = damage * (100 + army.getUnitSkillEffect(84)) / 100;
                army.drawSkill(84);
            }
            if ((army.potionIsSea() || terrainId == 1 || terrainId == 2 || terrainId == 11 || weatherId == 2 || weatherId == 4) && army.ifUnitHaveSkill(94)) {
                damage = damage * (100 + army.getUnitSkillEffect(94)) / 100;
                army.drawSkill(94);
            }
            if (!army.potionIsSea() && (climateZone == 11 || weatherId == 3 || weatherId == 7) && army.ifUnitHaveSkill(102)) {
                damage = damage * (100 + army.getUnitSkillEffect(102)) / 100;
                army.drawSkill(102);
            }
        }
        //插入技能结算 陆:陆
        boolean ifRound = army.isRound();
        int skillEffect = army.getUnitSkillEffect(91);
        if (army.ifUnitHaveSkill(91) && army.getUnitHpRate() < skillEffect) {
            damage = damage * (100 + skillEffect) / 100;
            army.drawSkill(91);
        }
        if (tarmy.ifUnitHaveSkill(8) && unitType == 1) {
            damage = damage * (100 - tarmy.getUnitSkillEffect(8)) / 100;
            tarmy.drawSkill(8);
        }
        if (tarmy.ifUnitHaveSkill(50) && unitType == 2) {
            damage = damage * (100 - tarmy.getUnitSkillEffect(50)) / 100;
            tarmy.drawSkill(50);
        }
        if (tarmy.ifUnitHaveSkill(56) && unitType == 4) {
            damage = damage * (100 - tarmy.getUnitSkillEffect(56)) / 100;
            tarmy.drawSkill(56);
        }
        if (tarmy.ifUnitHaveSkill(80) && unitType == 8) {
            damage = damage * (100 - tarmy.getUnitSkillEffect(80)) / 100;
            tarmy.drawSkill(80);
        }
        if (tarmy.ifUnitHaveSkill(121) && unitType == 3) {
            damage = damage * (100 - tarmy.getUnitSkillEffect(121)) / 100;
            tarmy.drawSkill(121);
        }
        if (tarmy.ifUnitHaveSkill(30) && unitType == 5) {
            damage = damage * (100 - tarmy.getUnitSkillEffect(30)) / 100;
            tarmy.drawSkill(30);
        }
        if (tarmy.ifUnitHaveSkill(57) && army.getMaxRange() < 2) {
            damage = damage * (100 - tarmy.getUnitSkillEffect(57)) / 100;
            tarmy.drawSkill(57);
        }
        if (tarmy.ifUnitHaveSkill(59) && army.getMaxRange() > 1) {
            damage = damage * (100 - tarmy.getUnitSkillEffect(59)) / 100;
            tarmy.drawSkill(59);
        }
        if (tarmy.ifUnitHaveSkill(67)) {
            damage = damage * (100 + tarmy.getUnitSkillEffect(67)) / 100;
            tarmy.drawSkill(67);
        }
        if (army.ifUnitHaveSkill(67)) {
            damage = damage * (100 + army.getUnitSkillEffect(67)) / 100;
            army.drawSkill(67);
        }
        if (tarmy.ifUnitHaveSkill(103)) {
            damage = damage * (100 + tarmy.getUnitSkillEffect(103)) / 100;
            tarmy.drawSkill(103);
        }
        if (army.ifUnitHaveSkill(103)) {
            damage = damage * (100 + army.getUnitSkillEffect(103)) / 100;
            army.drawSkill(103);
        }
        if (army.ifUnitHaveSkill(104)) {
            damage = damage * (100 - ComUtil.min(army.getUnitMorale(), army.getUnitHpRate())) / 100;
            army.drawSkill(104);
        }
        if (army.ifUnitHaveSkill(10) && army.inCity()) {
            damage = damage * (100 + army.getUnitSkillEffect(10)) / 100;
            army.drawSkill(10);
        }
        if (army.ifUnitHaveSkill(69)) {
            damage = damage * (100 + army.getUnitSkillEffect(69) * army.getDistance(tarmy.getHexagonIndex())) / 100;
            army.drawSkill(69);
        }
        if (army.ifUnitHaveSkill(4)) {//暴击几率+{0}
            crit += army.getUnitSkillEffect(4);
            army.drawSkill(4);
        }
        if (army.ifUnitHaveSkill(5) && tUnitType == 6) {
            damage = damage * (100 + army.getUnitSkillEffect(5)) / 100;
            army.drawSkill(5);
        }
        if (ifRound) {
            if (army.ifUnitHaveSkill(2) && army.getIfMove() == 0) {
                damage = damage * (100 + army.getUnitSkillEffect(2)) / 100;
                army.drawSkill(2);
            }
            if (army.ifUnitHaveSkill(3) && army.getUnitHpRate() > tarmy.getUnitHpRate()) {
                damage = damage * (100 + army.getUnitSkillEffect(3)) / 100;
                army.drawSkill(3);
            }
            if (isCombat && army.ifUnitHaveSkill(6)) {//每次攻击额外损失20%血量但使伤害增加99999
                damage = 99999;
                army.setUnitHpNow((int) (army.getUnitHpNow() * 0.8f) + 1);
                army.drawSkill(6);
            }
            if (army.ifUnitHaveSkill(15) && tUnitType == 8) {
                damage = damage * (100 + army.getUnitSkillEffect(15)) / 100;
                army.drawSkill(15);
            }
            if (isCombat && army.ifUnitTriggerSkill(31) && tUnitType == 6) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tarmy.addUnitRound(1);
                army.drawSkill(31);
            }
            if (isCombat && army.ifUnitTriggerSkill(34)) {//攻击后,有{1}%几率使敌人无法行动
                tarmy.addUnitRound(1);
                army.drawSkill(34);
            }
            if (isCombat && army.ifUnitTriggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                am += army.getUnitSkillEffect(37);
                army.drawSkill(37);
            }
            if (army.ifUnitHaveSkill(40) && tUnitType != 4 && tUnitType != 8 && tarmy.getTransportType() == 0) {
                damage = damage * (100 + army.getUnitSkillEffect(40)) / 100;
                army.drawSkill(40);
            }
            if (army.ifUnitHaveSkill(42) && tarmy.getAroundUnitCount(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                damage = damage * (100 + army.getUnitSkillEffect(42)) / 100;
                army.drawSkill(42);
            }
            if (army.ifUnitHaveSkill(43) && tarmy.getAroundUnitCount(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                damage = damage * (100 + army.getUnitSkillEffect(43)) / 100;
                army.drawSkill(43);
            }
            if (army.ifUnitHaveSkill(51) && (army.getUnitHpRate() > tarmy.getUnitHpRate() || army.getUnitHpNow() > tarmy.getUnitHpNow() || army.getUnitMorale() > tarmy.getUnitMorale())) {
                damage = damage * (100 + army.getUnitSkillEffect(51)) / 100;
                army.drawSkill(51);
            }
            if (isCombat && army.ifUnitTriggerSkill(63) && tarmy.getGeneralIndex() == 0) {//进攻后有{1}%几率使敌方普通单位叛变
                tarmy.setLegionIndex(army.getLegionIndex());
                army.drawSkill(63);
            }
            if (army.ifUnitHaveSkill(64)) {//暴击几率随血量减少增加
                crit += (100 - army.getUnitHpRate()) / 2;
                army.drawSkill(64);
            }
            if (army.ifUnitHaveSkill(71) && tarmy.getUnitHpRate() < army.getUnitSkillEffect(71)) {
                damage = 99999;
                army.drawSkill(71);
            }
            if (army.ifUnitHaveSkill(74) && tUnitType == 6) {
                damage = damage * (100 + army.getUnitSkillEffect(74)) / 100;
                army.drawSkill(74);
            }
            if (army.ifUnitHaveSkill(75) && (army.getUnitHpRate() < tarmy.getUnitHpRate() || army.getUnitHpNow() < tarmy.getUnitHpNow() || army.getUnitMorale() < tarmy.getUnitMorale())) {
                damage = damage * (100 + army.getUnitSkillEffect(75)) / 100;
                army.drawSkill(75);
            }
            if (army.ifUnitHaveSkill(77)) {//攻击时必然暴击
                crit = 100;
                army.drawSkill(77);
            }

            if (army.ifUnitHaveSkill(53) && tUnitType == 1) {//当目标是步兵时,必定对其暴击
                crit = 100;
                army.drawSkill(53);
            }
            if (army.ifUnitHaveSkill(81)) {
                damage = damage * (100 + army.getUnitSkillEffect(81)) / 100;
                army.drawSkill(81);
            }
            if (isCombat && army.ifUnitHaveSkill(105) && army.getUnitRank() > tarmy.getUnitRank() && tUnitType != 6) {
                int effect = army.getUnitSkillEffect(105);
                damage = damage * (100 + effect) / 100;
                tm += effect;
                army.drawSkill(105);
            }
            if (army.ifUnitHaveSkill(111) && tarmy.potionIsSea() && (tarmy.getTransportType() != 1 && tarmy.getTransportType() != 2 && tArmyId != 1401 && tArmyId != 1402)) {
                damage = damage * (100 + army.getUnitSkillEffect(111)) / 100;
                army.drawSkill(111);
            }
            if (army.ifUnitHaveSkill(116) && tUnitType == 3) {
                damage = damage * (100 + army.getUnitSkillEffect(116)) / 100;
                army.drawSkill(116);
            }
            if (army.ifUnitHaveSkill(124) && tUnitType == 1) {
                damage = damage * (100 + army.getUnitSkillEffect(124)) / 100;
                army.drawSkill(124);
            }

            if (isCombat && army.ifUnitTriggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tarmy.addUnitRound(army.getUnitSkillEffect(126));
                army.drawSkill(126);
            }
        } else {
            if (army.ifUnitHaveSkill(86)) {
                damage = damage * (100 + army.getUnitSkillEffect(86)) / 100;
                army.drawSkill(86);
            }
        }
        //有5%*攻击等级的几率攻击力翻倍
        if (ComUtil.ifGet(crit)) {
            if (crit > 100) {//如果暴击率超过100,则多余的暴击会转化为额外伤害
                damage = damage * crit / 100;
            }
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
            if (isCombat && ifRound) {
                if (army.ifUnitHaveSkill(13)) {//对敌人暴击时可以使敌人士气下降{0}
                    tm += -army.getUnitSkillEffect(13);
                    army.drawSkill(13);
                }
                if (army.ifUnitHaveSkill(44)) {//对敌人造成暴击伤害时,敌人无法行动
                    tarmy.addUnitRound(1);
                    army.drawSkill(44);
                }
                if (army.ifUnitHaveSkill(85) && army.getUnitMorale() > tarmy.getUnitMorale()) {//暴击可以降低低于我方士气敌人的能力
                    tarmy.randomReduceSkillLv(1);
                    army.drawSkill(85);
                }
            }
        }

        if (game.resGameConfig.ifTerrainEffect) {
            int terrain = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrain);
            if (terrainE != null) {
                int te = game.gameMethod.getTerrainEffect(terrainE, unitType);//我方类型对目标类型造成的伤害比
                if (te < 100) {
                    if (army.ifUnitHaveFeature(27)) {
                        te = 100;
                        army.drawFeature(27);
                    } else if (army.ifUnitHaveSkill(17)) {
                        te = 100;
                        army.drawSkill(17);
                    }
                }
                if (hexagon.isSea() && army.ifUnitHaveSkill(113) && te < 100) {
                    te = 100;
                    army.drawSkill(113);
                }
                damage = damage * te / 100;
                tm += +terrainE.getInt("exrLos", 0);
            }
        }


        //如果是建筑,则根据等级差来增加坦度
        if (tarmy.ifUnitHaveFeature(11) && tarmy.ifUnitTriggerFeature(11)) {
            if (ComUtil.ifGet((tarmy.getUnitRank() - army.getUnitRank()) * 2 * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
                tarmy.drawFeature(11);
            } else if (ComUtil.ifGet((tarmy.getUnitRank() - army.getUnitRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
            }
        } else {   //有5%*防御等级的几率豁免受到的伤害减半
            if (ComUtil.ifGet((tarmy.getUnitRank() - army.getUnitRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
            }
        }


        //如果进攻的是飞机
        if (unitType == 5) {
            if (buildData.getLegionIndex() == tarmy.getLegionIndex()) {
                damage = damage * (100 - buildData.getMissileLvNow() * 5) / 100;
            }
        }


        //根据士气对伤害进行判断

        if (army.ifUnitHaveSkill(61) && army.getUnitHpRate() < 50) {
            army.drawSkill(61);
        } else {
            damage = damage * (army.getUnitHpRate() / 2 + 50) / 100;
        }

       *//* if(damage>tarmy.getArmyHpMax()/2){
            int od=damage;
            damage=ComUtil.max(damage/2,tarmy.getArmyHpMax()/2);
            if(isCombat&&army.canCreateRDialogue()&&tl.getInternIndex()!=0){
                game.getSMapDAO().addDialogueData(army,7,tl.legionName);
            }
            tarmyMoraleChange+=-(od-damage)*100/tarmy.getArmyHpMax();
        }*//*

        if (game.getSMapDAO().masterData.getPlayerMode() != 2 && tUnitType != 6) {
            //根据位置计算伤害差值
            //根据位置计算伤害差值
            float damageRate = getDamageByDirect(army, tarmy, game.getSMapDAO().masterData.getWidth());
            damage = (int) (damage * damageRate);
            if (!ifTArmyHaveFormation && army.ifUnitHaveSkill(9)) {//对非正面敌军造成的伤害增加{0}%
                damage = damage * (100 + army.getUnitSkillEffect(9)) / 100;
                army.drawSkill(9);
            }
            if (ifTArmyHaveFormation && tarmy.ifUnitHaveSkill(118)) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(118)) / 100;
                tarmy.drawSkill(118);
            }
            if (isCombat && GameMap.getHX(army.getHexagonIndex(), army.getMapW()) != GameMap.getHX(tarmy.getHexagonIndex(), tarmy.getMapW())) {
                tarmy.updUnitModelDirect();
                tm += -ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax / 2, game.resGameConfig.unitMoraleChangeValueMax);
                am += ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax / 2, game.resGameConfig.unitMoraleChangeValueMax);
            }
        }
        if (ifAttackHaveFormation && tarmy.isUnitGroup() && tUnitType != 6 && !tarmy.potionIsSea()) {
            direct = tarmy.getDirect(army.getHexagonIndex());
            if (!tarmy.ifUnitGroupIsFormation(direct, false)) {
                int uc = ComUtil.max(tUnitType, 1);
                damage = damage * uc;
            }
        }

        if (isCombat) {
            if (!ifTArmyHaveFormation) {
                if (tm > 0) {
                    tm = -tm;
                } else {
                    tm = tm * 2;
                }
                if (am < 0) {
                    am = -am;
                } else {
                    am = am * 2;
                }
            }
            if (tm != 0) {
                tarmy.setUnitMoraleChange(tm);
                tarmy.drawMorale(tm, damageTime);
            }
            if (am != 0) {
                army.setUnitMoraleChange(am);
                army.drawMorale(am, damageTime);
            }
        }


        if (army.ifUnitTriggerSkill(21) && tUnitType == 1) {
            army.drawSkill(21);
        } else if (army.ifUnitTriggerSkill(22) && tarmy.potionIsSea()) {
            army.drawSkill(22);
        } else if (army.ifUnitTriggerSkill(24) && tUnitType == 6) {
            army.drawSkill(24);
        } else if (army.ifUnitTriggerSkill(25) && tUnitType == 4) {
            army.drawSkill(25);
        } else if (army.ifUnitTriggerSkill(26) && tUnitType == 5) {
            army.drawSkill(26);
        } else if (army.ifUnitTriggerSkill(27) && tUnitType == 2) {
            army.drawSkill(27);
        } else if (army.ifUnitTriggerSkill(76) && tUnitType == 1) {
            army.drawSkill(76);
        } else if (army.ifUnitTriggerSkill(28) && tUnitType == 8) {
            army.drawSkill(28);
        } else if (army.ifUnitTriggerSkill(29) && tUnitType == 3) {
            army.drawSkill(29);
        } else if (army.ifUnitTriggerSkill(96)) {
            army.drawSkill(96);
        } else {
            damage = damage * 100 / (100 + (tarmy.getUnitArmor(tDirect) * (50 + tarmy.getUnitMorale()) / 100));
        }
        if (tUnitType == 5) {
            int minD = Math.min(10, tarmy.getUnitLife() / 10 + 1);
            if (damage < minD) {
                damage = minD;
            }
        } else {
            int minD = Math.min(10, tarmy.getUnitLife() / 7 + 1);
            if (damage < minD) {
                damage = minD;
            }
        }
        return damage;
    }
*/

   /* public static int getDamageForArmyToAir(MainGame game, Fb2Smap.ArmyData army, Fb2Smap.AirData tair) {
        int armyId = army.getUnitArmyId0();
        //根据海陆计算伤害差,如果是非4,8的则伤害削减
        if (army.getArmyType() != 4 && army.getArmyType() != 8 && army.potionIsSea()) {
            armyId = army.getTransportType() + 1400;
        }
        Element armyE = game.gameConfig.getDEF_ARMY().getElementById(armyId);

        Element tarmyE = game.gameConfig.getDEF_ARMY().getElementById(tair.getAirId());
        Fb2Smap.BuildData build = army.getBuildData();
        Fb2Smap.LegionData l = army.getLegionData();
        //获得伤害
        int damage;
        if (army.isUnitGroup()) {
            damage = game.gameMethod.getUnitGroupUnitDamage(army.getLegionData(), army, -2, -1) + army.getUnitAbility() * game.resGameConfig.addAtkEachRank;
        } else {
            damage = (int) (game.gameMethod.getUnitDamage(army.getLegionData(), army.getBuildData(), armyId, 0) * army.getGroupRate() + army.getUnitAbility() * game.resGameConfig.addAtkEachRank);
        }
        if (build != null && build.getLegionIndex() == army.getLegionIndex() && build.getBuildWonder() > 0) {
            Element xE = game.gameConfig.getDEF_WONDER().getElementById(build.getBuildWonder());
            if (xE != null) {
                int function = xE.getInt("fucntion", 0);
                int effect = xE.getInt("effect", 0);
                int value = xE.getInt("value", 0);
                if (function == 5 && effect == 3) {
                    damage += value;
                }
            }
        }

        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && army.getGeneralIndex() > 0) {
                damage = damage * 2;
            }
        }
        Fb2Smap.AirData awacs = tair.getReadyAir(31, army.getHexagonIndex());
        if (awacs != null) {
            damage = damage / 2;
        }
        if (ComUtil.ifGet(tair.getActLv() * game.resGameConfig.airReduceDamageChanceForActLv)) {
            damage = damage / 2;
        }


        if (army.ifHaveFeature(25) && !army.isRound()) {
            damage = damage * (100 + army.getFeatureEffect(25)) / 100;
        }
        Fb2Map.MapHexagon hexagon = tair.getBuildData().getHexagonData();
        int weatherId = army.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("armyEfficiency", 100);
        if (army.ifHaveFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && army.ifHaveSkill(113) && ae < 100) {
            ae = 100;
            army.drawSkill(113);
        }
        damage = damage * ae / 100;
        //获得武器伤害加成
        float weapBonus;
        if (army.isUnitGroup()) {
            weapBonus = army.airStrikeBonus / 100f;
        } else {
            weapBonus = game.gameMethod.getWeaponValue(armyE.getInt("weapon"), tarmyE.getInt("type"), army.getUnitWealv0Value()) / 100f;
        }


        if (weapBonus == 0) {
            return 0;
        }
        damage = (int) (damage * weapBonus);

        int crit = army.getBorderAttackCrit();

        if (army.ifHaveFeature(21) && army.getIfMove() == 0) {
            crit = crit + army.getFeatureEffect(21);
            army.drawFeature(21);
        }
        int terrainId = army.getTerrainId();
        if (game.getSMapDAO().masterData.getPlayerMode() != 2) {
            damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, army.getArmyType(), army.getUnitArmyId0(), terrainId)) / 100;
            crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, army.getArmyType(), army.getUnitArmyId0());
            if (terrainId == 0 && !army.potionIsSea() && army.ifHaveSkill(73)) {
                damage = damage * (100 + army.getSkillEffect(73)) / 100;
                army.drawSkill(73);
            }
            if (!army.potionIsSea() && (build.getClimateZone() == 8 || terrainId == 4 || weatherId == 5 || weatherId == 6) && army.ifHaveSkill(83)) {
                damage = damage * (100 + army.getSkillEffect(83)) / 100;
                army.drawSkill(83);
            }
            if (!army.potionIsSea() && (terrainId == 4 || terrainId == 5 || terrainId == 6) && army.ifHaveSkill(84)) {
                damage = damage * (100 + army.getSkillEffect(84)) / 100;
                army.drawSkill(84);
            }
            if ((army.potionIsSea() || terrainId == 1 || terrainId == 2 || terrainId == 11 || weatherId == 2 || weatherId == 4) && army.ifHaveSkill(94)) {
                damage = damage * (100 + army.getSkillEffect(94)) / 100;
                army.drawSkill(94);
            }
            if (!army.potionIsSea() && (build.getClimateZone() == 11 || weatherId == 3 || weatherId == 7) && army.ifHaveSkill(102)) {
                damage = damage * (100 + army.getSkillEffect(102)) / 100;
                army.drawSkill(102);
            }
        }

        //插入技能结算 陆:空
        boolean ifRound = army.isRound();
        int skillEffect = army.getSkillEffect(91);
        if (army.ifHaveSkill(91) && army.getHpRate() < skillEffect) {
            damage = damage * (100 + skillEffect) / 100;
            army.drawSkill(91);
        }
        if (tair.ifHaveSkill(8) && army.getArmyType() == 1) {
            damage = damage * (100 - tair.getSkillEffect(8)) / 100;
            tair.drawSkill(8);
        }
        if (tair.ifHaveSkill(50) && army.getArmyType() == 2) {
            damage = damage * (100 - tair.getSkillEffect(50)) / 100;
            tair.drawSkill(50);
        }
        if (tair.ifHaveSkill(56) && army.getArmyType() == 4) {
            damage = damage * (100 - tair.getSkillEffect(56)) / 100;
            tair.drawSkill(56);
        }
        if (tair.ifHaveSkill(80) && army.getArmyType() == 8) {
            damage = damage * (100 - tair.getSkillEffect(80)) / 100;
            tair.drawSkill(80);
        }
        if (tair.ifHaveSkill(121) && army.getArmyType() == 3) {
            damage = damage * (100 - tair.getSkillEffect(121)) / 100;
            tair.drawSkill(121);
        }
        if (tair.ifHaveSkill(30) && army.getArmyType() == 5) {
            damage = damage * (100 - tair.getSkillEffect(30)) / 100;
            tair.drawSkill(30);
        }
        if (tair.ifHaveSkill(57) && army.getMaxRange() < 2) {
            damage = damage * (100 - tair.getSkillEffect(57)) / 100;
            tair.drawSkill(57);
        }
        if (tair.ifHaveSkill(59) && army.getMaxRange() > 1) {
            damage = damage * (100 - tair.getSkillEffect(59)) / 100;
            tair.drawSkill(59);
        }
        if (tair.ifHaveSkill(67)) {
            damage = damage * (100 + tair.getSkillEffect(67)) / 100;
            tair.drawSkill(67);
        }
        if (army.ifHaveSkill(67)) {
            damage = damage * (100 + army.getSkillEffect(67)) / 100;
            army.drawSkill(67);
        }
        if (tair.ifHaveSkill(103)) {
            damage = damage * (100 + tair.getSkillEffect(103)) / 100;
            tair.drawSkill(103);
        }
        if (army.ifHaveSkill(103)) {
            damage = damage * (100 + army.getSkillEffect(103)) / 100;
            army.drawSkill(103);
        }
        if (army.ifHaveSkill(104)) {
            damage = damage * (100 - ComUtil.min(army.getArmyMorale(), army.getHpRate())) / 100;
            army.drawSkill(104);
        }
        if (army.ifHaveSkill(10) && army.inCity()) {
            damage = damage * (100 + army.getSkillEffect(10)) / 100;
            army.drawSkill(10);
        }
        if (army.ifHaveSkill(4)) {
            crit += army.getSkillEffect(4);
            army.drawSkill(4);
        }
        if (ifRound) {
            if (army.ifHaveSkill(75) && (army.getHpRate() < tair.getHpRate() || army.getArmyHpNow() < tair.getAirHpNow() || army.getArmyMorale() < tair.getAirMorale())) {
                damage = damage * (100 + army.getSkillEffect(75)) / 100;
                army.drawSkill(75);
            }
            if (army.ifHaveSkill(51) && (army.getHpRate() > tair.getHpRate() || army.getArmyHpNow() > tair.getAirHpNow() || army.getArmyMorale() > tair.getAirMorale())) {
                damage = damage * (100 + army.getSkillEffect(51)) / 100;
                army.drawSkill(51);
            }
            if (army.ifHaveSkill(3) && army.getHpRate() > tair.getHpRate()) {
                damage = damage * (100 + army.getSkillEffect(3)) / 100;
                army.drawSkill(3);
            }
            if (army.ifHaveSkill(81)) {
                damage = damage * (100 + army.getSkillEffect(81)) / 100;
                army.drawSkill(81);
            }
            if (army.ifHaveSkill(2) && army.getIfMove() == 0) {
                damage = damage * (100 + army.getSkillEffect(2)) / 100;
                army.drawSkill(2);
            }
            if (army.ifHaveSkill(77)) {//攻击时必然暴击
                crit = 100;
                army.drawSkill(77);
            }
            if (army.ifHaveSkill(64)) {//暴击几率随血量减少增加
                crit += (100 - army.getHpRate()) / 2;
                army.drawSkill(64);
            }
            if (army.ifHaveSkill(6)) {
                damage = 99999;
                army.setArmyHpNow((int) (army.getArmyHpNow() * 0.8f) + 1);
                army.drawSkill(6);
            }
            if (army.ifHaveSkill(71) && tair.getHpRate() < army.getSkillEffect(71)) {
                damage = 99999;
                army.drawSkill(71);
            }
            if (army.ifHaveSkill(105) && army.getArmyRank() > tair.getAirRank()) {
                int effect = army.getSkillEffect(105);
                damage = damage * (100 + effect) / 100;
                tair.setAirMoraleChange(-effect);
                army.drawSkill(105);
            }
            if (army.triggerSkill(34)) {//攻击后,有{1}%几率使敌人无法行动
                tair.addAirRound(1);
                army.drawSkill(34);
            }
            if (army.triggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tair.addAirRound(army.getSkillEffect(126));
                army.drawSkill(126);
            }
            if (army.triggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                army.setArmyMoraleChange(army.getSkillEffect(37));
                army.drawSkill(37);
            }
        } else {
            if (army.ifHaveSkill(86)) {
                damage = damage * (100 + army.getSkillEffect(86)) / 100;
                army.drawSkill(86);
            }
        }


        //有5%*攻击等级的几率攻击力翻倍
        if (ComUtil.ifGet(crit)) {
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
            if (ifRound) {
                if (army.ifHaveSkill(13)) {//对敌人暴击时可以使敌人士气下降{0}
                    tair.setAirMoraleChange(-army.getSkillEffect(13));
                    army.drawSkill(13);
                }
                if (army.ifHaveSkill(44)) {//对敌人造成暴击伤害时,敌人无法行动
                    tair.addAirRound(1);
                    army.drawSkill(44);
                }
                if (army.ifHaveSkill(85) && army.getArmyMorale() > tair.getAirMorale()) {//暴击可以降低低于我方士气敌人的能力
                    tair.randomReduceSkillLv(1);
                    army.drawSkill(85);
                }
            }
        }

        if (game.resGameConfig.ifTerrainEffect) {
            int terrain = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrain);
            if (terrainE != null) {
                int te = game.gameMethod.getTerrainEffect(terrainE, army.getArmyType());//我方类型对目标类型造成的伤害比
                if (te < 100) {
                    if (army.ifHaveFeature(27)) {
                        te = 100;
                        army.drawFeature(27);
                    } else if (army.ifHaveSkill(17)) {
                        te = 100;
                        army.drawSkill(17);
                    }
                }
                if (hexagon.isSea() && army.ifHaveSkill(113) && te < 100) {
                    te = 100;
                    army.drawSkill(113);
                }
                damage = damage * te / 100;
                tair.setAirMoraleChange(terrainE.getInt("exrLos", 0));
            }
        }


        //有5%*防御等级的几率豁免受到的伤害减半
        if (ComUtil.ifGet(tair.getDefLv() * game.resGameConfig.addDefChanceForRankLvPoor)) {
            damage = damage / 2;
        }
        //减免受到的空袭伤害,额外增加对敌方空军的反击伤害
        if (army.ifHaveFeature(19) && army.triggerFeature(19)) {
            damage = (int) (damage * 2f);
            army.drawFeature(19);
        }

        //根据士气对伤害进行判断

        if (army.ifHaveSkill(61) && army.getHpRate() < 50) {
            army.drawSkill(61);
        } else {
            damage = damage * (army.getHpRate() / 2 + 50) / 100;
        }

        if (damage > tair.getAirHpMax() / 2) {
            int od = damage;
            damage = ComUtil.max(damage / 2, tair.getAirHpMax() / 2);
            //     tair.setAirMoraleChange(-ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax/2, game.resGameConfig.unitMoraleChangeValueMax));
            tair.setAirMoraleChange(-(od - damage) * 100 / tair.getAirHpMax());
        }

        if (army.triggerSkill(26)) {
            army.drawSkill(26);
        } else if (army.triggerSkill(96)) {
            army.drawSkill(96);
        } else {
            damage = damage * 100 / (100 + (tair.getArmor() * (50 + tair.getAirMorale()) / 100));
        }

        int minD = Math.min(10, tair.getAirHpMax() / 7 + 1);
        if (damage < minD) {
            damage = minD;
        }
        return damage;
    }

*/

    private static float getDamageByDirect(Fb2Smap.UnitData army, Fb2Smap.UnitData tArmy, int w) {
        int direct = army.getDirectByBorderId(tArmy.getHexagonIndex());
        if(tArmy.getUnitType()==6){
            return army.getGame().resGameConfig.generalUnitFrontStrikeRate;
        }else if(direct<=0&&tArmy.getUnitType()!=6&&army.isUnitGroup()&&!army.potionIsSea()){//距离伤害削减对要塞不起作用
                return army.getAttackRateByRange(tArmy.getHexagonIndex());
        } else if (tArmy.isUnitGroup() && !tArmy.potionIsSea()&&tArmy.getUnitCount()>1) {
            boolean ifAttackArmyIsFormation = army.ifUnitGroupIsFormation(direct, true);
            direct = tArmy.getDirect(army.getHexagonIndex());
            boolean ifTargetArmyIsFormation = tArmy.ifUnitGroupIsFormation(direct, true);
            if (ifAttackArmyIsFormation) {//进攻的有完整防线
                if (ifTargetArmyIsFormation || direct <= 0) {
                    return army.getGame().resGameConfig.unityGroupHaveAllFormationStrikeRate;
                } else if (tArmy.ifUnitGroupIsFormation(direct, false)) {
                    return army.getGame().resGameConfig.unityGroupNotOneFormationStrikeRate;
                } else {
                    return army.getGame().resGameConfig.unityGroupNotAllFormationStrikeRate;
                }
            } else {
                if (ifTargetArmyIsFormation) {//组合部队受击但是进攻的部队不构成防线时的伤害
                    return army.getGame().resGameConfig.unityGroupHaveAllFormationStrikeAndReduceRate;
                } else {
                    return army.getGame().resGameConfig.unityGroupHaveAllFormationStrikeRate;
                }
            }
        } else {
           if ( army.getHexagonIndex() == tArmy.getHexagonIndex()) {
                return army.getGame().resGameConfig.generalUnitFrontStrikeRate;
            } else {
               int arrowDirect=  tArmy.getArrowDirect();
               if(arrowDirect==0){
                   return army.getGame().resGameConfig.generalUnitFrontStrikeRate;
               }else{
                   if(arrowDirect==direct){//背后受击
                       return army.getGame().resGameConfig.generalUnitBackStrikeRate;
                   }else if(GameUtil.getOppositeDirect(arrowDirect)==direct){//正面攻击
                       return army.getGame().resGameConfig.generalUnitFrontStrikeRate;
                   }
                   return army.getGame().resGameConfig.generalUnitSideStrikeRate;
               }
            }
        }
    }

   /* public static int getDamageForBuild(MainGame game, Fb2Smap.ArmyData army, Fb2Smap.BuildData tBuild) {
        int armyId = army.getUnitArmyId0();
        //根据海陆计算伤害差,如果是非4,8的则伤害削减
        if (army.getArmyType() != 4 && army.getArmyType() != 8 && army.potionIsSea()) {
            armyId = army.getTransportType() + 1400;
        }
        Element armyE = game.gameConfig.getDEF_ARMY().getElementById(armyId);
        Fb2Smap.LegionData l = army.getLegionData();
        //获得伤害

        int direct = army.getDirect(tBuild.getRegionId());
        int damage;
        //获得伤害

        if (army.isUnitGroup()) {
            if (army.getDirectByBorderId(tBuild.getRegionId()) == -1) {//远程
                damage = game.gameMethod.getUnitGroupUnitDamage(army.getLegionData(), army, 0, tBuild.getRegionId()) + army.getUnitAbility() * game.resGameConfig.addAtkEachRank;
            } else {//近战
                damage = game.gameMethod.getUnitGroupUnitDamage(army.getLegionData(), army, 0, tBuild.getRegionId()) + army.getUnitAbility() * game.resGameConfig.addAtkEachRank;
                if (army.ifInArmyRange(tBuild.getRegionId()) && army.ifUnitGroupIsFormation(direct, false)) {
                    damage += game.gameMethod.getUnitGroupUnitDamage(army.getLegionData(), army, 0, tBuild.getRegionId()) / 2;
                }
            }
        } else {
            damage = (int) (game.gameMethod.getUnitDamage(army.getLegionData(), army.getBuildData(), armyId, 0) * army.getGroupRate() + army.getUnitAbility() * game.resGameConfig.addAtkEachRank);
        }


        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && army.getGeneralIndex() > 0) {
                damage = damage * 2;
            }
        }
        if (damage <= 0) {
            damage = 1;
        }
        if (army.ifHaveFeature(25) && !army.isRound()) {
            damage = damage * (100 + army.getFeatureEffect(25)) / 100;
        }


        Fb2Map.MapHexagon hexagon = tBuild.getHexagonData();
        int weatherId = army.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("armyEfficiency", 100);
        if (army.ifHaveFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && army.ifHaveSkill(113) && ae < 100) {
            ae = 100;
            army.drawSkill(113);
        }
        damage = damage * ae / 100;

        int crit = army.getBorderAttackCrit();
        if (army.ifHaveFeature(21) && army.getIfMove() == 0) {
            crit = crit + army.getFeatureEffect(21);
            army.drawFeature(21);
        }

        if (game.getSMapDAO().masterData.getPlayerMode() != 2) {
            int terrainId = army.getTerrainId();
            damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, army.getArmyType(), army.getUnitArmyId0(), terrainId)) / 100;
            crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, army.getArmyType(), army.getUnitArmyId0());
            if (terrainId == 0 && !army.potionIsSea() && army.ifHaveSkill(73)) {
                damage = damage * (100 + army.getSkillEffect(73)) / 100;
                army.drawSkill(73);
            }
            if (!army.potionIsSea() && (tBuild.getClimateZone() == 8 || terrainId == 4 || weatherId == 5 || weatherId == 6) && army.ifHaveSkill(83)) {
                damage = damage * (100 + army.getSkillEffect(83)) / 100;
                army.drawSkill(83);
            }
            if (!army.potionIsSea() && (terrainId == 4 || terrainId == 5 || terrainId == 6) && army.ifHaveSkill(84)) {
                damage = damage * (100 + army.getSkillEffect(84)) / 100;
                army.drawSkill(84);
            }
            if ((army.potionIsSea() || terrainId == 1 || terrainId == 2 || terrainId == 11 || weatherId == 2 || weatherId == 4) && army.ifHaveSkill(94)) {
                damage = damage * (100 + army.getSkillEffect(94)) / 100;
                army.drawSkill(94);
            }
            if (!army.potionIsSea() && (tBuild.getClimateZone() == 11 || weatherId == 3 || weatherId == 7) && army.ifHaveSkill(102)) {
                damage = damage * (100 + army.getSkillEffect(102)) / 100;
                army.drawSkill(102);
            }
        }
        if (game.resGameConfig.ifTerrainEffect) {
            int terrain = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrain);
            if (terrainE != null) {

                int te = game.gameMethod.getTerrainEffect(terrainE, army.getArmyType());//我方类型对目标类型造成的伤害比
                if (te < 100) {
                    if (army.ifHaveFeature(27)) {
                        te = 100;
                        army.drawFeature(27);
                    } else if (army.ifHaveSkill(17)) {
                        te = 100;
                        army.drawSkill(17);
                    }
                }
                if (hexagon.isSea() && army.ifHaveSkill(113) && te < 100) {
                    te = 100;
                    army.drawSkill(113);
                }
                damage = damage * te / 100;
                tBuild.changeCityStability(terrainE.getInt("exrLos", 0));
            }
        }

        //获得武器伤害加成
        float weapBonus;
        if (army.isUnitGroup()) {
            weapBonus = game.gameMethod.getWeaponValue(army, direct, 6) / 100f;
        } else {
            weapBonus = game.gameMethod.getWeaponValue(armyE.getInt("weapon"), 6, army.getUnitWealv0Value()) / 100f;
        }
        if (weapBonus == 0) {
            return 0;
        }

        damage = (int) (damage * weapBonus);

        //插入技能结算 陆:建
        boolean ifRound = army.isRound();
        int skillEffect = army.getSkillEffect(91);
        if (army.ifHaveSkill(91) && army.getHpRate() < skillEffect) {
            damage = damage * (100 + skillEffect) / 100;
            army.drawSkill(91);
        }
        if (army.ifHaveSkill(67)) {
            damage = damage * (100 + army.getSkillEffect(67)) / 100;
            army.drawSkill(67);
        }
        if (army.ifHaveSkill(103)) {
            damage = damage * (100 + army.getSkillEffect(103)) / 100;
            army.drawSkill(103);
        }
        if (army.ifHaveSkill(104)) {
            damage = damage * (100 - ComUtil.min(army.getArmyMorale(), army.getHpRate())) / 100;
            army.drawSkill(104);
        }
        if (army.ifHaveSkill(10) && army.inCity()) {
            damage = damage * (100 + army.getSkillEffect(10)) / 100;
            army.drawSkill(10);
        }
        if (army.ifHaveSkill(69)) {
            damage = damage * (100 + army.getSkillEffect(69) * army.getDistance(tBuild.getRegionId())) / 100;
            army.drawSkill(69);
        }
        if (army.ifHaveSkill(4)) {
            crit += army.getSkillEffect(4);
            army.drawSkill(4);
        }
        if (army.ifHaveSkill(5)) {
            damage = damage * (100 + army.getSkillEffect(5)) / 100;
            army.drawSkill(5);
        }
        if (ifRound) {
            if (army.ifHaveSkill(74)) {
                damage = damage * (100 + army.getSkillEffect(74)) / 100;
                army.drawSkill(74);
            }
            if (army.ifHaveSkill(81)) {
                damage = damage * (100 + army.getSkillEffect(81)) / 100;
                army.drawSkill(81);
            }
            if (army.ifHaveSkill(2) && army.getIfMove() == 0) {
                damage = damage * (100 + army.getSkillEffect(2)) / 100;
                army.drawSkill(2);
            }
            if (army.ifHaveSkill(77)) {//攻击时必然暴击
                crit = 100;
                army.drawSkill(77);
            }
            if (army.ifHaveSkill(64)) {//暴击几率随血量减少增加
                crit += (100 - army.getHpRate()) / 2;
                army.drawSkill(64);
            }
            if (army.ifHaveSkill(6)) {
                damage = 99999;
                army.setArmyHpNow((int) (army.getArmyHpNow() * 0.8f) + 1);
                army.drawSkill(6);
            }
            if (army.ifHaveSkill(71) && tBuild.getHpRate() < army.getSkillEffect(71)) {
                damage = 99999;
                army.drawSkill(71);
            }
            if (army.triggerSkill(31)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tBuild.addBuildRound(1);
                army.drawSkill(31);
            }
            if (army.triggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tBuild.addBuildRound(army.getSkillEffect(126));
                army.drawSkill(126);
            }
            if (army.triggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                army.setArmyMoraleChange(army.getSkillEffect(37));
                army.drawSkill(37);
            }
        } else {
            if (army.ifHaveSkill(86)) {
                damage = damage * (100 + army.getSkillEffect(86)) / 100;
                army.drawSkill(86);
            }
        }


        //有5%*攻击等级的几率攻击力翻倍
        if (ComUtil.ifGet(crit)) {
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
        }

        //有5%*防御等级的几率豁免受到的伤害减半
       *//* if(ComUtil.ifGet(tBuild.getDefenceLvNow()*ResConfig.Game.addChanceForDefLv)){
            damage= damage/ResConfig.Game.reductDamageForDefLv;
        }*//*
        //  damage= (int) (damage*(1-tBuild.getDefenceLvNow()* game.resGameConfig.reductDamageForBuildDLv));

        //根据士气对伤害进行判断

        if (army.ifHaveSkill(61) && army.getHpRate() < 50) {
            army.drawSkill(61);
        } else {
            damage = damage * (army.getHpRate() / 2 + 50) / 100;
        }

        if (tBuild.isCapital()) {
            damage = damage / 2;
        }


        *//*if(!tBuild.ifHaveGarrison()){
            damage=damage*2;
        }*//*


        if (army.triggerSkill(24)) {
            army.drawSkill(24);
        } else if (army.triggerSkill(76)) {
            army.drawSkill(76);
        } else if (army.triggerSkill(96)) {
            army.drawSkill(86);
        } else {
            damage = damage * 100 / (100 + (tBuild.getDefenceLvNow() + tBuild.getCityLvNow()) * game.resGameConfig.addDefEachRank);
        }

        int minD = Math.min(10, tBuild.getCityHpMax() / 10 + 1);
        if (damage < minD) {
            damage = minD;
        }

        return damage;
    }
*/
    /*public static int getDamageForArmy(MainGame game, Fb2Smap.AirData air, Fb2Smap.ArmyData tarmy, float damageTime) {
        Element armyE = game.gameConfig.getDEF_ARMY().getElementById(air.getAirId());
        Element tarmyE = game.gameConfig.getDEF_ARMY().getElementById(tarmy.getUnitArmyId0());
        Fb2Smap.LegionData l = air.getLegionData();
        //获得伤害
        int am = 0;
        int tm = 0;

        int damage = (int) (game.gameMethod.getUnitDamage(air.getLegionData(), air.getBuildData(), air.getAirId(), 0) * air.getRankRate() + air.getAirAbility() * game.resGameConfig.addAtkEachRank);

        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && air.getGeneralIndex() > 0) {
                damage = damage * 2;
            }
        }
        Fb2Smap.AirData awacs = air.getReadyAir(31, tarmy.getHexagonIndex());
        if (awacs != null) {
            awacs.setAirGoodsNow(awacs.getAirGoodsMax() - 1);
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
        }

        if (tarmy.getArmyType() == 6 && tarmy.getUnitArmyId0() != 1606) {
            damage = damage - tarmy.getLegionData().getFortLvMax();
        }
        if (damage <= 0) {
            damage = 1;
        }
        //结算反击
        if (air.ifHaveAirFeature(25) && !air.isRound()) {
            damage = damage * (100 + air.getAirFeatureEffect(25)) / 100;
        }
        Fb2Map.MapHexagon hexagon = tarmy.getHexagonData();
        int weatherId = air.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("airEfficiency", 100);
        if (air.ifHaveAirFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && air.ifHaveSkill(113) && ae < 100) {
            ae = 100;
            air.drawSkill(113);
        }
        damage = damage * ae / 100;
        //获得武器伤害加成
        float weapBonus = game.gameMethod.getWeaponValue(armyE.getInt("weapon"), tarmyE.getInt("type"), air.getWeaLv()) / 100f;
        if (weapBonus == 0) {
            return 0;
        }
        if (game.getSMapDAO().ifFortReduceDamage(air.getAirType(), air.getAirId(), air.getFeature(), 0, tarmy.getHexagonIndex())) {
            weapBonus = weapBonus * 0.5f;
        }
        damage = (int) (damage * weapBonus);


        int crit = air.getCrit();
        //进攻拥有下潜特性的单位时,暴击率增加
        if (tarmy.ifHaveFeature(17) && air.ifHaveAirFeature(18)) {
            damage = damage * (100 + air.getAirFeatureEffect(18)) / 100;
            air.drawFeature(18);
        }

        if (air.ifHaveAirFeature(21) && air.getIfMove() == 0) {
            crit = crit + air.getAirFeatureEffect(21);
        }
        if (air.ifHaveAirFeature(23) && tarmy.potionIsSea()) {
            crit = crit + air.getAirFeatureEffect(23);
            air.drawFeature(23);
        }

        if (game.getSMapDAO().masterData.getPlayerMode() == 0) {
            damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, air.getAirType(), air.getAirId(), 0)) / 100;
            crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, air.getAirType(), air.getAirId());
        }
        //插入技能结算 空:陆
        boolean ifRound = air.isRound();
        int skillEffect = air.getSkillEffect(91);
        if (air.ifHaveSkill(91) && air.getHpRate() < skillEffect) {
            damage = damage * (100 + skillEffect) / 100;
            air.drawSkill(91);
        }
        if (tarmy.ifHaveSkill(30)) {
            damage = damage * (100 - tarmy.getSkillEffect(30)) / 100;
            tarmy.drawSkill(30);
        }
        if (tarmy.ifHaveSkill(67)) {
            damage = damage * (100 + tarmy.getSkillEffect(67)) / 100;
            tarmy.drawSkill(67);
        }
        if (air.ifHaveSkill(67)) {
            damage = damage * (100 + air.getSkillEffect(67)) / 100;
            air.drawSkill(67);
        }
        if (tarmy.ifHaveSkill(103)) {
            damage = damage * (100 + tarmy.getSkillEffect(103)) / 100;
            tarmy.drawSkill(103);
        }
        if (air.ifHaveSkill(103)) {
            damage = damage * (100 + air.getSkillEffect(103)) / 100;
            air.drawSkill(103);
        }
        if (air.ifHaveSkill(104)) {
            damage = damage * (100 - ComUtil.min(air.getAirMorale(), air.getHpRate())) / 100;
            air.drawSkill(104);
        }
        if (air.ifHaveSkill(53) && tarmy.getArmyType() == 1) {//当目标是步兵时,必定对其暴击
            crit = 100;
            air.drawSkill(53);
        }
        if (air.ifHaveSkill(4)) {
            crit += air.getSkillEffect(4);
            air.drawSkill(4);
        }
        if (air.ifHaveSkill(5) && tarmy.getArmyType() == 6) {
            damage = damage * (100 + air.getSkillEffect(5)) / 100;
            air.drawSkill(5);
        }
        if (ifRound) {
            Fb2Smap.BuildData build = air.getBuildData();
            if (build != null) {
                int effect = build.getSkillEffect(112, false);
                if (effect > 0) {
                    crit += effect;
                    air.drawSkill(112);
                }
            }
            if (air.ifHaveSkill(15) && tarmy.getArmyType() == 8) {
                damage = damage * (100 + air.getSkillEffect(15)) / 100;
                air.drawSkill(15);
            }
            if (air.ifHaveSkill(116) && tarmy.getArmyType() == 3) {
                damage = damage * (100 + air.getSkillEffect(116)) / 100;
                air.drawSkill(116);
            }
            if (air.ifHaveSkill(124) && tarmy.getArmyType() == 1) {
                damage = damage * (100 + air.getSkillEffect(124)) / 100;
                air.drawSkill(124);
            }
            if (air.ifHaveSkill(40) && tarmy.getArmyType() != 4 && tarmy.getArmyType() != 8 && tarmy.getTransportType() == 0) {
                damage = damage * (100 + air.getSkillEffect(40)) / 100;
                air.drawSkill(40);
            }
            if (air.ifHaveSkill(111) && tarmy.potionIsSea() && (tarmy.getTransportType() != 1 && tarmy.getTransportType() != 2 && tarmy.getUnitArmyId0() != 1401 && tarmy.getUnitArmyId0() != 1402)) {
                damage = damage * (100 + air.getSkillEffect(111)) / 100;
                air.drawSkill(111);
            }
            if (air.ifHaveSkill(74) && tarmy.getArmyType() == 6) {
                damage = damage * (100 + air.getSkillEffect(74)) / 100;
                air.drawSkill(74);
            }
            if (air.ifHaveSkill(75) && (air.getHpRate() < tarmy.getHpRate() || air.getAirHpNow() < tarmy.getArmyHpNow() || air.getAirMorale() < tarmy.getArmyMorale())) {
                damage = damage * (100 + air.getSkillEffect(75)) / 100;
                air.drawSkill(75);
            }
            if (air.ifHaveSkill(51) && (air.getHpRate() > tarmy.getHpRate() || air.getAirHpNow() > tarmy.getArmyHpNow() || air.getAirMorale() > tarmy.getArmyMorale())) {
                damage = damage * (100 + air.getSkillEffect(51)) / 100;
                air.drawSkill(51);
            }
            if (air.ifHaveSkill(3) && air.getHpRate() > tarmy.getHpRate()) {
                damage = damage * (100 + air.getSkillEffect(3)) / 100;
                air.drawSkill(3);
            }
            if (air.ifHaveSkill(81)) {
                damage = damage * (100 + air.getSkillEffect(81)) / 100;
                air.drawSkill(81);
            }
            if (air.ifHaveSkill(2) && air.getIfMove() == 0) {
                damage = damage * (100 + air.getSkillEffect(2)) / 100;
                air.drawSkill(2);
            }
            if (air.ifHaveSkill(77)) {//攻击时必然暴击
                crit = 100;
                air.drawSkill(77);
            }
            if (air.ifHaveSkill(64)) {//暴击几率随血量减少增加
                crit += (100 - air.getHpRate()) / 2;
                air.drawSkill(64);
            }
            if (air.ifHaveSkill(6)) {
                damage = 99999;
                air.setAirHpNow((int) (air.getAirHpNow() * 0.8f) + 1);
                air.drawSkill(6);
            }
            if (air.ifHaveSkill(71) && tarmy.getHpRate() < air.getSkillEffect(71)) {
                damage = 99999;
                air.drawSkill(71);
            }
            if (air.triggerSkill(34)) {//攻击后,有{1}%几率使敌人无法行动
                tarmy.addArmyRound(1);
                air.drawSkill(34);
            }
            if (air.triggerSkill(31) && tarmy.getArmyType() == 6) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tarmy.addArmyRound(1);
                air.drawSkill(31);
            }
            if (air.triggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tarmy.addArmyRound(air.getSkillEffect(126));
                air.drawSkill(126);
            }
            if (air.triggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                am += air.getSkillEffect(37);
                air.drawSkill(37);
            }
            if (air.ifHaveSkill(105) && air.getAirRank() > tarmy.getArmyRank() && tarmy.getArmyType() != 6) {
                int effect = air.getSkillEffect(105);
                damage = damage * (100 + effect) / 100;
                tm += -effect;
                air.drawSkill(105);
            }
            if (air.triggerSkill(63) && tarmy.getGeneralIndex() == 0) {//进攻后有{1}%几率使敌方普通单位叛变
                tarmy.setLegionIndex(air.getLegionIndex());
                air.drawSkill(63);
            }
            if (air.ifHaveSkill(42) && tarmy.getAroundUnitCountIA1(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                damage = damage * (100 + air.getSkillEffect(42)) / 100;
                air.drawSkill(42);
            }
            if (air.ifHaveSkill(43) && tarmy.getAroundUnitCountIA1(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                damage = damage * (100 + air.getSkillEffect(43)) / 100;
                air.drawSkill(43);
            }
        } else {
            if (air.ifHaveSkill(86)) {
                damage = damage * (100 + air.getSkillEffect(86)) / 100;
                air.drawSkill(86);
            }
        }

        if (tarmy.ifHaveSkill(62)) {//受到来自空中伤害减少{0}%
            damage = damage * (100 - tarmy.getSkillEffect(62)) / 100;
            tarmy.drawSkill(62);
        }
        if (tarmy.ifHaveSkill(64)) {//来自空中的伤害减免{0}%,在城市中,该防御效果加倍
            if (tarmy.getHexagonIndex() == tarmy.getRegionId()) {
                damage = damage * (100 - tarmy.getSkillEffect(64) * 2) / 100;
            } else {
                damage = damage * (100 - tarmy.getSkillEffect(64)) / 100;
            }
            tarmy.drawSkill(64);
        }

        if (tarmy.isUnitGroup() && tarmy.getArmyType() != 6 && !tarmy.potionIsSea() && ComUtil.ifGet(crit)) {
            int uc = ComUtil.max(tarmy.getUnitGroup(), 1);
            damage = damage * uc;
            crit = crit / 2;
        }
        //有5%*攻击等级的几率攻击力翻倍
        if (ComUtil.ifGet(crit)) {
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
            if (ifRound) {
                if (air.ifHaveSkill(13)) {//对敌人暴击时可以使敌人士气下降{0}
                    tm += -air.getSkillEffect(13);
                    tarmy.drawSkill(13);
                }
                if (air.ifHaveSkill(44)) {//对敌人造成暴击伤害时,敌人无法行动
                    tarmy.addArmyRound(1);
                    tarmy.drawSkill(44);
                }
                if (air.ifHaveSkill(85) && air.getAirMorale() > tarmy.getArmyMorale()) {//暴击可以降低低于我方士气敌人的能力
                    tarmy.randomReduceSkillLv(1);
                    tarmy.drawSkill(85);
                }
            }
        }


        if (game.resGameConfig.ifTerrainEffect) {
            int terrain = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrain);
            if (terrainE != null) {
                int te = game.gameMethod.getTerrainEffect(terrainE, 5);//我方类型对目标类型造成的伤害比
                if (te < 100) {
                    if (air.ifHaveAirFeature(27)) {
                        te = 100;
                        air.drawFeature(27);
                    } else if (air.ifHaveSkill(17)) {
                        te = 100;
                        air.drawSkill(17);
                    }
                }
                if (hexagon.isSea() && air.ifHaveSkill(113)) {
                    te = 100;
                    air.drawSkill(113);
                }
                damage = damage * te / 100;
                tm += terrainE.getInt("exrLos", 0);
            }
        }


        //如果是建筑,则根据actlv来增加坦度,因为actlv没用

        if (tarmy.ifHaveFeature(11) && tarmy.triggerFeature(11)) {
            if (ComUtil.ifGet((tarmy.getArmyRank() - air.getAirRank()) * 2 * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
                tarmy.drawFeature(11);
            } else if (ComUtil.ifGet((tarmy.getArmyRank() - air.getAirRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
            }
        } else {   //有5%*防御等级的几率豁免受到的伤害减半
            if (ComUtil.ifGet((tarmy.getArmyRank() - air.getAirRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                damage = damage / 2;
            }
        }
        //减免受到的空袭伤害,额外增加对敌方空军的反击伤害
        if (tarmy.ifHaveFeature(19) && tarmy.triggerFeature(19)) {
            damage = (int) (damage * 0.5f);
            tarmy.drawFeature(19);
        }
        int defAirLv = tarmy.getRegionDefAirLv();
        if (defAirLv > 0) {
            damage = damage * (100 - defAirLv * 5) / 100;
        }

        //根据士气对伤害进行判断

        if (air.ifHaveSkill(61) && air.getHpRate() < 50) {
            air.drawSkill(61);
        } else {
            damage = damage * (air.getHpRate() / 2 + 50) / 100;
        }
        damage = damage * (100 + air.getAckLv() * game.resGameConfig.addDamageForAckLv) / 100;


        if (air.triggerSkill(21) && tarmy.getArmyType() == 1) {
            air.drawSkill(21);
        } else if (air.triggerSkill(22) && tarmy.potionIsSea()) {
            air.drawSkill(22);
        } else if (air.triggerSkill(24) && tarmy.getArmyType() == 6) {
            air.drawSkill(24);
        } else if (air.triggerSkill(25) && tarmy.getArmyType() == 4) {
            air.drawSkill(25);
        } else if (air.triggerSkill(26) && tarmy.getArmyType() == 5) {
            air.drawSkill(26);
        } else if (air.triggerSkill(27) && tarmy.getArmyType() == 2) {
            air.drawSkill(27);
        } else if (air.triggerSkill(76) && tarmy.getArmyType() == 1) {
            air.drawSkill(76);
        } else if (air.triggerSkill(28) && tarmy.getArmyType() == 8) {
            air.drawSkill(28);
        } else if (air.triggerSkill(29) && tarmy.getArmyType() == 3) {
            air.drawSkill(29);
        } else if (air.triggerSkill(96)) {
            air.drawSkill(96);
        } else {
            if (tarmy.isUnitGroup()) {
                damage = damage * 100 / (100 + tarmy.getArmor() * (100 + tarmy.airDefendBonus) / 100);
            } else {
                damage = damage * (100 + tarmy.getUnitAbility(2) * game.resGameConfig.airDefenseForAdfLv) / 100;
            }
        }
        int minD = Math.min(10, tarmy.getArmyLife() / 7 + 1);
        if (damage < minD) {
            damage = minD;
        }
        air.setAirMoraleChange(am);
        tarmy.setArmyMoraleChange(tm);
        tarmy.drawMorale(tm, damageTime);
        return damage;
    }
*/
    /*public static int getDamageForAir(MainGame game, Fb2Smap.AirData air, Fb2Smap.AirData tair, Fb2Smap.BuildData buildData) {
        Element armyE = game.gameConfig.getDEF_ARMY().getElementById(air.getAirId());
        Element tarmyE = game.gameConfig.getDEF_ARMY().getElementById(tair.getAirId());
        Fb2Smap.LegionData l = air.getLegionData();
        //获得伤害
        int damage = (int) (game.gameMethod.getUnitDamage(air.getLegionData(), air.getBuildData(), air.getAirId(), 0) * air.getRankRate() + air.getAirAbility() * game.resGameConfig.addAtkEachRank);

        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && air.getGeneralIndex() > 0) {
                damage = damage * 2;
            }
        }

        if (ComUtil.ifGet(tair.getActLv() * game.resGameConfig.airReduceDamageChanceForActLv)) {
            damage = damage / 2;
        }
        Fb2Smap.AirData awacs = air.getReadyAir(31, tair.getHexagon());
        if (awacs != null) {
            awacs.setAirGoodsNow(awacs.getAirGoodsMax() - 1);
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
        }
        awacs = tair.getReadyAir(31, air.getHexagon());
        if (awacs != null) {
            awacs.setAirGoodsNow(awacs.getAirGoodsMax() - 1);
            damage = damage / 2;
        }


        if (air.ifHaveAirFeature(25) && !air.isRound()) {
            damage = damage * (100 + air.getAirFeatureEffect(25)) / 100;
        }
        Fb2Map.MapHexagon hexagon = tair.getBuildData().getHexagonData();
        int weatherId = air.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("airEfficiency", 100);
        if (air.ifHaveAirFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && air.ifHaveSkill(113) && ae < 100) {
            ae = 100;
            air.drawSkill(113);
        }
        damage = damage * ae / 100;

        //获得武器伤害加成
        float weapBonus = game.gameMethod.getWeaponValue(armyE.getInt("weapon"), tarmyE.getInt("type"), air.getWeaLv()) / 100f;
        if (buildData.getLegionIndex() == air.getLegionIndex()) {
            weapBonus = weapBonus + buildData.getMissileLvNow() * 3f / 100;
        }

        if (weapBonus == 0) {
            return 0;
        }
        damage = (int) (damage * weapBonus);


        int crit = air.getCrit();

        if (air.ifHaveAirFeature(21) && air.getIfMove() == 0) {
            crit = crit + air.getAirFeatureEffect(21);
        }

        if (game.getSMapDAO().masterData.getPlayerMode() == 0) {
            damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, air.getAirType(), air.getAirId(), 0)) / 100;
            crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, air.getAirType(), air.getAirId());
        }

        //插入技能结算 空:空
        boolean ifRound = air.isRound();
        int skillEffect = air.getSkillEffect(91);
        if (air.ifHaveSkill(91) && air.getHpRate() < skillEffect) {
            damage = damage * (100 + skillEffect) / 100;
            air.drawSkill(91);
        }
        if (tair.ifHaveSkill(67)) {
            damage = damage * (100 + tair.getSkillEffect(67)) / 100;
            tair.drawSkill(67);
        }
        if (air.ifHaveSkill(67)) {
            damage = damage * (100 + air.getSkillEffect(67)) / 100;
            air.drawSkill(67);
        }
        if (tair.ifHaveSkill(103)) {
            damage = damage * (100 + tair.getSkillEffect(103)) / 100;
            tair.drawSkill(103);
        }
        if (air.ifHaveSkill(103)) {
            damage = damage * (100 + air.getSkillEffect(103)) / 100;
            air.drawSkill(103);
        }
        if (air.ifHaveSkill(104)) {
            damage = damage * (100 - ComUtil.min(air.getAirMorale(), air.getHpRate())) / 100;
            air.drawSkill(104);
        }
        if (air.ifHaveSkill(4)) {
            crit += air.getSkillEffect(4);
            air.drawSkill(4);
        }
        if (ifRound) {
            Fb2Smap.BuildData build = air.getBuildData();
            if (build != null) {
                int effect = build.getSkillEffect(112, false);
                if (effect > 0) {
                    crit += effect;
                    air.drawSkill(112);
                }
            }
            if (air.ifHaveSkill(75) && (air.getHpRate() < tair.getHpRate() || air.getAirHpNow() < tair.getAirHpNow() || air.getAirMorale() < tair.getAirMorale())) {
                damage = damage * (100 + air.getSkillEffect(75)) / 100;
                air.drawSkill(75);
            }
            if (air.ifHaveSkill(51) && (air.getHpRate() > tair.getHpRate() || air.getAirHpNow() > tair.getAirHpNow() || air.getAirMorale() > tair.getAirMorale())) {
                damage = damage * (100 + air.getSkillEffect(51)) / 100;
                air.drawSkill(51);
            }
            if (air.ifHaveSkill(3) && air.getHpRate() > tair.getHpRate()) {
                damage = damage * (100 + air.getSkillEffect(3)) / 100;
                air.drawSkill(3);
            }
            if (air.ifHaveSkill(81)) {
                damage = damage * (100 + air.getSkillEffect(81)) / 100;
                air.drawSkill(81);
            }
            if (air.ifHaveSkill(2) && air.getIfMove() == 0) {
                damage = damage * (100 + air.getSkillEffect(2)) / 100;
                air.drawSkill(2);
            }
            if (air.ifHaveSkill(77)) {//攻击时必然暴击
                crit = 100;
                air.drawSkill(77);
            }
            if (air.ifHaveSkill(64)) {//暴击几率随血量减少增加
                crit += (100 - air.getHpRate()) / 2;
                air.drawSkill(64);
            }
            if (air.ifHaveSkill(6)) {
                damage = 99999;
                air.setAirHpNow((int) (air.getAirHpNow() * 0.8f) + 1);
                air.drawSkill(6);
            }
            if (air.ifHaveSkill(71) && tair.getHpRate() < air.getSkillEffect(71)) {
                damage = 99999;
                air.drawSkill(71);
            }
            if (air.triggerSkill(34)) {//攻击后,有{1}%几率使敌人无法行动
                tair.addAirRound(1);
                air.drawSkill(34);
            }
            if (air.triggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tair.addAirRound(air.getSkillEffect(126));
                air.drawSkill(126);
            }
            if (air.triggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                air.setAirMoraleChange(air.getSkillEffect(37));
                air.drawSkill(37);
            }
            if (air.ifHaveSkill(105) && air.getAirRank() > tair.getAirRank()) {
                int effect = air.getSkillEffect(105);
                damage = damage * (100 + effect) / 100;
                tair.setAirMoraleChange(-effect);
                air.drawSkill(105);
            }
        } else {
            if (air.ifHaveSkill(86)) {
                damage = damage * (100 + air.getSkillEffect(86)) / 100;
                air.drawSkill(86);
            }
        }
        if (tair.ifHaveSkill(62)) {//受到来自空中伤害减少{0}%
            damage = damage * (100 - tair.getSkillEffect(62)) / 100;
            tair.drawSkill(62);
        }
        if (tair.ifHaveSkill(64)) {//来自空中的伤害减免{0}%,在城市中,该防御效果加倍
            if (tair.getBuildData() != null) {
                damage = damage * (100 - tair.getSkillEffect(64) * 2) / 100;
            } else {
                damage = damage * (100 - tair.getSkillEffect(64)) / 100;
            }
            tair.drawSkill(64);
        }
        //有5%*攻击等级的几率攻击力翻倍
        if (ComUtil.ifGet(crit)) {
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
            if (ifRound) {
                if (air.ifHaveSkill(13)) {//对敌人暴击时可以使敌人士气下降{0}
                    tair.setAirMoraleChange(-air.getSkillEffect(13));
                    air.drawSkill(13);
                }
                if (air.ifHaveSkill(44)) {//对敌人造成暴击伤害时,敌人无法行动
                    tair.addAirRound(1);
                    air.drawSkill(44);
                }
                if (air.ifHaveSkill(85) && air.getAirMorale() > tair.getAirMorale()) {//暴击可以降低低于我方士气敌人的能力
                    tair.randomReduceSkillLv(1);
                    air.drawSkill(85);
                }
            }
        }

        if (game.resGameConfig.ifTerrainEffect) {
            int terrain = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrain);
            if (terrainE != null) {
                int te = game.gameMethod.getTerrainEffect(terrainE, 5);//我方类型对目标类型造成的伤害比
                if (te < 100) {
                    if (air.ifHaveAirFeature(27)) {
                        te = 100;
                        air.drawFeature(27);
                    } else if (air.ifHaveSkill(17)) {
                        te = 100;
                        air.drawSkill(17);
                    }
                }
                if (hexagon.isSea() && air.ifHaveSkill(113) && te < 100) {
                    te = 100;
                    air.drawSkill(113);
                }
                damage = damage * te / 100;
                tair.setAirMoraleChange(terrainE.getInt("exrLos", 0));
            }
        }

        //有5%*防御等级的几率豁免受到的伤害减半
        if (ComUtil.ifGet(tair.getDefLv() * game.resGameConfig.addDefChanceForRankLvPoor)) {
            damage = damage / 2;
        }


        if (tair.getLegionIndex() == buildData.getLegionIndex()) {
            damage = damage * (100 - buildData.getMissileLvNow() * 5) / 100;
        }

        //根据士气对伤害进行判断
        if (air.ifHaveSkill(61) && air.getHpRate() < 50) {
            air.drawSkill(61);
        } else {
            damage = damage * (air.getHpRate() / 2 + 50) / 100;
        }
        damage = damage * (100 + air.getAckLv() * game.resGameConfig.addDamageForAckLv) / 100;


        if (damage > tair.getAirHpMax() / 2) {
            int od = damage;
            damage = ComUtil.max(damage / 2, tair.getAirHpMax() / 2);
            //飞机对话,随后可以加上
            *//*if(air.canCreateRDialogue()){
                game.getSMapDAO().addDialogueData(air,7,tair.getCountryStr());
            }*//*
            tair.setAirMoraleChange(-(od - damage) * 100 / tair.getAirHpMax());
        }

        if (air.triggerSkill(26)) {
            air.drawSkill(26);
        } else if (air.triggerSkill(96)) {
            air.drawSkill(96);
        } else {
            damage = damage * 100 / (100 + (tair.getArmor() * (50 + tair.getAirMorale()) / 100));
        }

        int minD = Math.min(10, tair.getAirHpMax() / 9 + 1);
        if (damage < minD) {
            damage = minD;
        }
        return damage;
    }

*/
   /* public static int getDamageForBuild(MainGame game, Fb2Smap.AirData air, Fb2Smap.BuildData tBuild) {
        Element armyE = game.gameConfig.getDEF_ARMY().getElementById(air.getAirId());
        Fb2Smap.LegionData l = air.getLegionData();
        //获得伤害
        int damage = (int) (game.gameMethod.getUnitDamage(air.getLegionData(), air.getBuildData(), air.getAirId(), 0) * air.getRankRate() + air.getAirAbility() * game.resGameConfig.addAtkEachRank);
        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && air.getGeneralIndex() > 0) {
                damage = damage * 2;
            }
        }
        if (damage <= 0) {
            damage = 1;
        }
        Fb2Smap.AirData awacs = air.getReadyAir(31, tBuild.getRegionId());
        if (awacs != null) {
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
        }
        if (air.ifHaveAirFeature(25) && !air.isRound()) {
            damage = damage * (100 + air.getAirFeatureEffect(25)) / 100;
        }
        Fb2Map.MapHexagon hexagon = tBuild.getHexagonData();
        int weatherId = air.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("airEfficiency", 100);
        if (air.ifHaveAirFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && air.ifHaveSkill(113) && ae < 100) {
            ae = 100;
            air.drawSkill(113);
        }
        damage = damage * ae / 100;

        int crit = air.getCrit();


        if (air.ifHaveAirFeature(21) && air.getIfMove() == 0) {
            crit = crit + air.getAirFeatureEffect(21);
        }

        if (game.getSMapDAO().masterData.getPlayerMode() == 0) {
            damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, air.getAirType(), air.getAirId(), 0)) / 100;
            crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, air.getAirType(), air.getAirId());
        }

        if (game.resGameConfig.ifTerrainEffect) {
            int terrain = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrain);
            if (terrainE != null) {
                int te = game.gameMethod.getTerrainEffect(terrainE, 5);//我方类型对目标类型造成的伤害比
                if (te < 100) {
                    if (air.ifHaveAirFeature(27)) {
                        te = 100;
                        air.drawFeature(27);
                    } else if (air.ifHaveSkill(17)) {
                        te = 100;
                        air.drawSkill(17);
                    }
                }
                if (hexagon.isSea() && air.ifHaveSkill(113) && te < 100) {
                    te = 100;
                    air.drawSkill(113);
                }
                damage = damage * te / 100;
                tBuild.changeCityStability(terrainE.getInt("exrLos", 0));
            }
        }

        //获得武器伤害加成
        float weapBonus = game.gameMethod.getWeaponValue(armyE.getInt("weapon"), 6, air.getWeaLv()) / 100f;
        if (weapBonus == 0) {
            return 0;
        }
        damage = (int) (damage * weapBonus);

        //插入技能结算 空:建
        boolean ifRound = air.isRound();
        int skillEffect = air.getSkillEffect(91);
        if (air.ifHaveSkill(91) && air.getHpRate() < skillEffect) {
            damage = damage * (100 + skillEffect) / 100;
            air.drawSkill(91);
        }
        if (air.ifHaveSkill(67)) {
            damage = damage * (100 + air.getSkillEffect(67)) / 100;
            air.drawSkill(67);
        }
        if (air.ifHaveSkill(103)) {
            damage = damage * (100 + air.getSkillEffect(103)) / 100;
            air.drawSkill(103);
        }
        if (air.ifHaveSkill(104)) {
            damage = damage * (100 - ComUtil.min(air.getAirMorale(), air.getHpRate())) / 100;
            air.drawSkill(104);
        }
        if (air.ifHaveSkill(4)) {
            crit += air.getSkillEffect(4);
            air.drawSkill(4);
        }
        if (air.ifHaveSkill(5)) {
            damage = damage * (100 + air.getSkillEffect(5)) / 100;
            air.drawSkill(5);
        }
        if (ifRound) {
            Fb2Smap.BuildData build = air.getBuildData();
            if (build != null) {
                int effect = build.getSkillEffect(112, false);
                if (effect > 0) {
                    crit += effect;
                    air.drawSkill(112);
                }
            }
            if (air.ifHaveSkill(74)) {
                damage = damage * (100 + air.getSkillEffect(74)) / 100;
                air.drawSkill(74);
            }
            if (air.ifHaveSkill(81)) {
                damage = damage * (100 + air.getSkillEffect(81)) / 100;
                air.drawSkill(81);
            }
            if (air.ifHaveSkill(2) && air.getIfMove() == 0) {
                damage = damage * (100 + air.getSkillEffect(2)) / 100;
                air.drawSkill(2);
            }
            if (air.ifHaveSkill(77)) {//攻击时必然暴击
                crit = 100;
                air.drawSkill(77);
            }
            if (air.ifHaveSkill(64)) {//暴击几率随血量减少增加
                crit += (100 - air.getHpRate()) / 2;
                air.drawSkill(64);
            }
            if (air.ifHaveSkill(6)) {
                damage = 99999;
                air.setAirHpNow((int) (air.getAirHpNow() * 0.8f) + 1);
                air.drawSkill(6);
            }
            if (air.ifHaveSkill(71) && tBuild.getHpRate() < air.getSkillEffect(71)) {
                damage = 99999;
                air.drawSkill(71);
            }
            if (air.triggerSkill(31)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tBuild.addBuildRound(1);
                air.drawSkill(31);
            }
            if (air.triggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                tBuild.addBuildRound(air.getSkillEffect(126));
                air.drawSkill(126);
            }
            if (air.triggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                air.setAirMoraleChange(air.getSkillEffect(37));
                air.drawSkill(37);
            }
        } else {
            if (air.ifHaveSkill(86)) {
                damage = damage * (100 + air.getSkillEffect(86)) / 100;
                air.drawSkill(86);
            }
        }

        //有5%*攻击等级的几率攻击力翻倍
        if (ComUtil.ifGet(crit)) {
            damage = (int) (damage * game.resGameConfig.critDamageRatio);
        }
        damage = damage - (tBuild.getDefenceLvNow() + tBuild.getCityLvNow());
        //有5%*防御等级的几率豁免受到的伤害减半
       *//* if(ComUtil.ifGet(tBuild.getDefenceLvNow()*ResConfig.Game.addChanceForDefLv)){
            damage= damage/ResConfig.Game.reductDamageForDefLv;
        }*//*
        //    damage= (int) (damage*(1-tBuild.getDefenceLvNow()* game.resGameConfig.reductDamageForBuildDLv));

        //根据士气对伤害进行判断
        if (air.ifHaveSkill(61) && air.getHpRate() < 50) {
            air.drawSkill(61);
        } else {
            damage = damage * (air.getHpRate() / 2 + 50) / 100;
        }
        damage = damage * (100 + air.getAckLv() * game.resGameConfig.addDamageForAckLv) / 100;

        if (air.triggerSkill(24)) {
            air.drawSkill(24);
        } else if (air.triggerSkill(76)) {
            air.drawSkill(76);
        } else if (air.triggerSkill(96)) {
            air.drawSkill(96);
        } else {
            damage = damage * 100 / (100 + (tBuild.getMissileLvNow() * 2) * game.resGameConfig.addDefEachRank);
        }

        if (tBuild.isCapital()) {
            damage = damage / 2;
        }
        *//*if(!tBuild.ifHaveGarrison()){
            damage=damage*2;
        }*//*

        int minD = Math.min(10, tBuild.getCityHpMax() / 10 + 1);
        if (damage < minD) {
            damage = minD;
        }
        return damage;
    }

*/
    public static void textureDrawPixmap(Texture texture, Pixmap pixmap, int x, int y, int w, int h) {
        if (texture.isManaged()) throw new GdxRuntimeException("can't draw to a managed texture");

        texture.bind();
        Gdx.gl.glTexSubImage2D(texture.glTarget, 0, x, y, w, h, pixmap.getGLFormat(), pixmap.getGLType(),
                pixmap.getPixels());
    }

    public static int getArraySize(Array array) {
        int v = 0;
        for (int i = 0, iMax = array.items.length; i < iMax; i++) {
            if (array.items[i] != null) {
                v++;
            }
        }
        return v;
    }

    public static void drawSectorByShapeRenderer(ShapeRenderer shapeRenderer, float x, float y, float radius, float startAngle, int num) {
        //int num = (int) interval;
        float[][] points = new float[num][2];
        for (int i = 0; i < num; i++) {
            points[i][0] = x + radius * MathUtils.cosDeg(startAngle + i);
            points[i][1] = y + radius * MathUtils.sinDeg(startAngle + i);
        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < num - 1; i++) {
            shapeRenderer.triangle(x, y, points[i][0], points[i][1], points[i + 1][0], points[i + 1][1]);
        }
    }

    /**
     * 异常信息转换为字符串
     *
     * @param t 异常对象
     * @return
     */
    public static String ex2String(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }

    public static void recordLog(String str,Throwable t) {
        recordLog(ResDefaultConfig.version+str+ex2String(t));
    }

    //写出错误信息到日志
    public static void recordLog(String logStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        FileHandle file =null;
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            file = Gdx.files.local(ResDefaultConfig.Path.LogFolderPath+ResDefaultConfig.game + "_" + formatter.format(date) + ".log");
        }else{
            file = Gdx.files.external(ResDefaultConfig.game + "_" + formatter.format(date) + ".log");
        }
        file.writeString("\n" + logStr, true);
    }


    public static TextField initTextField(TextField textField, BitmapFont font, String text, Color color, Drawable cursor, Drawable selection, Drawable background, Element textFieldE, float uiStageWidth, float uiStageHeight, float gamtFontScale) {


        textField = new TextField(text, new TextField.TextFieldStyle(font, Color.WHITE, cursor, selection, background));
        textField.setColor(color);
        if (textFieldE.getFloat("scale") != 0) {
            //textField.setFontScale(textFieldE.getFloat("scale"));
            textField.setScale(textFieldE.getFloat("scale"));
        }
        if (textFieldE.get("font").equals("default")) {
            if (textFieldE.getFloat("scale") != 0) {
                textField.setScale(textFieldE.getFloat("scale"));
            }
        } else {
            if (textFieldE.getFloat("scale") != 0) {
                textField.setScale(textFieldE.getFloat("scale") * gamtFontScale);
            }
        }


        float w = textFieldE.getFloat("w", 0);
        float h = textFieldE.getFloat("h", 0);
        if (h == -1) {
            textField.setHeight(uiStageHeight);
        } else if (h != 0) {
            textField.setHeight(h * uiStageHeight / 100);
        }
        if (w == -1) {
            textField.setWidth(uiStageWidth);
        } else if (w != 0) {
            textField.setWidth(w * uiStageWidth / 100);
            //textField.setWrap(true);
            textField.setAlignment(Align.center);
        }
        /*label.setPosition(
                labelE.getFloat("x") * uiStageWidth / 100 + label.getWidth() / 2 > uiStageWidth ? uiStageWidth - label.getWidth() : labelE.getFloat("x") * uiStageWidth / 100 - label.getWidth() / 2 < 0 ? 0 : labelE.getFloat("x") * uiStageWidth / 100 - label.getWidth() / 2,
                labelE.getFloat("y") * uiStageHeight / 100 + label.getHeight() / 2 > uiStageHeight ? uiStageHeight - label.getHeight() : labelE.getFloat("y") * uiStageHeight / 100 - label.getHeight() / 2 < 0 ? 0 : labelE.getFloat("y") * uiStageHeight / 100 - label.getHeight() / 2);
*/
        textField.setPosition(
                GameUtil.getButtonPotionXByView(textFieldE, uiStageWidth, textField.getWidth()) + textFieldE.getInt("refx", 0),
                GameUtil.getButtonPotionYByView(textFieldE, uiStageHeight, textField.getHeight()) + textFieldE.getInt("refy", 0));


        return textField;
    }


    public static int maxValue(IntArray xs) {
        int maxValue = xs.get(0);
        for (int i = 0; i < xs.size; i++) {
            if (xs.get(i) > maxValue) {
                maxValue = xs.get(i);
            }
        }
        return maxValue;
    }

    public static int minValue(IntArray xs) {
        int minValue = xs.get(0);
        for (int i = 0; i < xs.size; i++) {
            if (xs.get(i) < minValue) {
                minValue = xs.get(i);
            }
        }
        return minValue;
    }


    //获取rs中出现最多的数据
    public static int getMostRepeatValue(IntArray rs) {
        IntIntMap c = new IntIntMap();
        for (int i = 0; i < rs.size; i++) {
            if (c.containsKey(rs.get(i))) {
                c.put(rs.get(i), c.get(rs.get(i), 0) + 1);
            } else {
                c.put(rs.get(i), 1);
            }
        }

        Iterator<IntIntMap.Entry> it = c.iterator();
        Fb2Smap.LegionData l;
        int maxKey = -1;
        int maxCount = 0;
        while (it.hasNext()) {
            IntIntMap.Entry b = it.next();
            if (b.value > maxCount) {
                maxKey = b.key;
                maxCount = b.value;
            }
        }
        return maxKey;
    }

    public static IntArray clone(IntArray rs, IntArray targetRs) {
        if (targetRs == null || rs.equals(targetRs)) {
            targetRs = new IntArray();
        } else {
            targetRs.clear();
        }
        targetRs.addAll(rs);
        return targetRs;
    }

    public static boolean ifIntArrayContainIntArrayByInteger(IntArray a, IntArray b) {
        boolean rs = true;
        for (int i1 = 0; i1 < b.size; i1++) {
            int i = b.get(i1);
            if (!a.contains(i)) {
                rs = false;
            }
        }
        return rs;
    }


    public static void drawActorBounds(Actor actor, ShapeRenderer shapes) {
        if (!actor.getDebug()) return;
        shapes.set(ShapeRenderer.ShapeType.Line);
        if (actor.getStage() != null) shapes.setColor(actor.getStage().getDebugColor());
        shapes.rect(actor.getX(), actor.getY(), actor.getOriginX(), actor.getOriginY(), actor.getWidth(), actor.getHeight(), actor.getScaleX(), actor.getScaleY(), actor.getRotation());
    }


    public static void copyLogToExteral() {
        FileHandle[] files = Gdx.files.local("").list(".log");
        for (FileHandle file : files) {
            file.copyTo(Gdx.files.external(file.path()));
            file.delete();
        }
    }

    public static FileHandle toFileHandle(FileHandle baseFileHandle, Locale locale) {
        StringBuilder sb = new StringBuilder(baseFileHandle.name());
        if (!locale.equals(ROOT_LOCALE)) {
            String language = locale.getLanguage();
            String country = locale.getCountry();
            String variant = locale.getVariant();
            boolean emptyLanguage = "".equals(language);
            boolean emptyCountry = "".equals(country);
            boolean emptyVariant = "".equals(variant);

            if (!(emptyLanguage && emptyCountry && emptyVariant)) {
                sb.append('_');
                if (!emptyVariant) {
                    sb.append(language).append('_').append(country).append('_').append(variant);
                } else if (!emptyCountry) {
                    sb.append(language).append('_').append(country);
                } else {
                    sb.append(language);
                }
            }
        }
        return baseFileHandle.sibling(sb.append(".properties").toString());
    }


    public static Texture transTataToTexture(FileHandle file) {
        byte[] allBytes = file.readBytes();
        int byteCount = 0;
        for (int i = 0; i < allBytes.length; i++) {

            // 每个字节异或密码，请保证解密时密码前后相同
            byteCount++;
            if (byteCount <= 20) {
                // 加密20个字节,停止解密密
                allBytes[i] ^= ResDefaultConfig.ImageKey.hashCode();
            } else {
                allBytes[i] = allBytes[i];
            }
        }
        Pixmap pixmap = new Pixmap(allBytes, 0, allBytes.length);
        Texture texture = new Texture(pixmap);
        return texture;
    }

    //翻转绘制对象
    public static void filpActor(Actor tempActor, boolean x, boolean y) {

        if (tempActor instanceof Image) {
            Image i = (Image) tempActor;
            ((TextureRegionDrawable) i.getDrawable()).getRegion().flip(x, y);
        } else if (tempActor instanceof ImageButton) {
            ImageButton i = (ImageButton) tempActor;
            if (i.getStyle().imageUp != null) {
                ((TextureRegionDrawable) i.getStyle().imageUp).getRegion().flip(x, y);
            }
            if (i.getStyle().imageChecked != null) {
                ((TextureRegionDrawable) i.getStyle().imageChecked).getRegion().flip(x, y);
            }
            if (i.getStyle().imageDown != null) {
                ((TextureRegionDrawable) i.getStyle().imageDown).getRegion().flip(x, y);
            }
        }
    }


    //左上1,上2,右上3,左下4,下5,右下6
    public static int getEBorderId(boolean t1, boolean t2, boolean t3, boolean t4, boolean t5, boolean t6) {


        if (t1) {
            if (t2) {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 0;
                            } else {
                                return 60;
                            }
                        } else {
                            if (t6) {
                                return 56;
                            } else {
                                return 52;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 48;
                            } else {
                                return 44;
                            }
                        } else {
                            if (t6) {
                                return 40;
                            } else {
                                return 36;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 62;
                            } else {
                                return 58;
                            }
                        } else {
                            if (t6) {
                                return 54;
                            } else {
                                return 50;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 46;
                            } else {
                                return 42;
                            }
                        } else {
                            if (t6) {
                                return 38;
                            } else {
                                return 34;
                            }
                        }
                    }
                }
            } else {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 63;
                            } else {
                                return 59;
                            }
                        } else {
                            if (t6) {
                                return 55;
                            } else {
                                return 51;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 47;
                            } else {
                                return 43;
                            }
                        } else {
                            if (t6) {
                                return 39;
                            } else {
                                return 35;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 61;
                            } else {
                                return 57;
                            }
                        } else {
                            if (t6) {
                                return 53;
                            } else {
                                return 49;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 45;
                            } else {
                                return 41;
                            }
                        } else {
                            if (t6) {
                                return 37;
                            } else {
                                return 33;
                            }
                        }
                    }
                }
            }

        } else {//t1
            if (t2) {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 32;
                            } else {
                                return 28;
                            }
                        } else {
                            if (t6) {
                                return 24;
                            } else {
                                return 20;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 16;
                            } else {
                                return 12;
                            }
                        } else {
                            if (t6) {
                                return 8;
                            } else {
                                return 4;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 30;
                            } else {
                                return 26;
                            }
                        } else {
                            if (t6) {
                                return 22;
                            } else {
                                return 18;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 14;
                            } else {
                                return 10;
                            }
                        } else {
                            if (t6) {
                                return 6;
                            } else {
                                return 2;
                            }
                        }
                    }
                }
            } else {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 31;
                            } else {
                                return 27;
                            }
                        } else {
                            if (t6) {
                                return 23;
                            } else {
                                return 19;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 15;
                            } else {
                                return 11;
                            }
                        } else {
                            if (t6) {
                                return 7;
                            } else {
                                return 3;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 29;
                            } else {
                                return 25;
                            }
                        } else {
                            if (t6) {
                                return 21;
                            } else {
                                return 17;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 13;
                            } else {
                                return 9;
                            }
                        } else {
                            if (t6) {
                                return 5;
                            } else {
                                return 1;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void removeXmlEById(Array<Element> xmlEs, int id) {
        int v = -1;
        for (int i = 0, iMax = xmlEs.size; i < iMax; i++) {
            if (xmlEs.get(i).getInt("id") == id) {
                v = i;
                break;
            }
        }
        if (v != -1) {
            xmlEs.removeIndex(v);
        }
    }

    //获取绘制的类型     1 ↖ 2 ↙  3 ↓  4 ↘ 5 ↗ 6↑
    public static int getFBorderId(boolean t1, boolean t2, boolean t3, boolean t4, boolean t5, boolean t6) {
        if (t1) {
            if (t2) {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 63;
                            } else {
                                return 62;
                            }
                        } else {
                            if (t6) {
                                return 61;
                            } else {
                                return 60;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 59;
                            } else {
                                return 58;
                            }
                        } else {
                            if (t6) {
                                return 57;
                            } else {
                                return 56;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 55;
                            } else {
                                return 54;
                            }
                        } else {
                            if (t6) {
                                return 53;
                            } else {
                                return 52;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 51;
                            } else {
                                return 50;
                            }
                        } else {
                            if (t6) {
                                return 49;
                            } else {
                                return 48;
                            }
                        }
                    }
                }
            } else {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 47;
                            } else {
                                return 46;
                            }
                        } else {
                            if (t6) {
                                return 45;
                            } else {
                                return 44;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 43;
                            } else {
                                return 42;
                            }
                        } else {
                            if (t6) {
                                return 41;
                            } else {
                                return 40;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 39;
                            } else {
                                return 38;
                            }
                        } else {
                            if (t6) {
                                return 37;
                            } else {
                                return 36;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 35;
                            } else {
                                return 34;
                            }
                        } else {
                            if (t6) {
                                return 33;
                            } else {
                                return 32;
                            }
                        }
                    }
                }
            }

        } else {//t1
            if (t2) {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 31;
                            } else {
                                return 30;
                            }
                        } else {
                            if (t6) {
                                return 29;
                            } else {
                                return 28;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 27;
                            } else {
                                return 26;
                            }
                        } else {
                            if (t6) {
                                return 25;
                            } else {
                                return 24;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 23;
                            } else {
                                return 22;
                            }
                        } else {
                            if (t6) {
                                return 21;
                            } else {
                                return 20;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 19;
                            } else {
                                return 18;
                            }
                        } else {
                            if (t6) {
                                return 17;
                            } else {
                                return 16;
                            }
                        }
                    }
                }
            } else {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 15;
                            } else {
                                return 14;
                            }
                        } else {
                            if (t6) {
                                return 13;
                            } else {
                                return 12;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 11;
                            } else {
                                return 10;
                            }
                        } else {
                            if (t6) {
                                return 9;
                            } else {
                                return 8;//3
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 7;
                            } else {
                                return 6;
                            }
                        } else {
                            if (t6) {
                                return 5;
                            } else {
                                return 4;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 3;
                            } else {
                                return 2;
                            }
                        } else {
                            if (t6) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    }
                }
            }
        }
    }


    public static int getFBorderIndex(boolean t1, boolean t2, boolean t3, boolean t4, boolean t5, boolean t6) {
        if (t1) {
            if (t2) {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 111111;
                            } else {
                                return 111110;
                            }
                        } else {
                            if (t6) {
                                return 111101;
                            } else {
                                return 111100;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 111011;
                            } else {
                                return 111010;
                            }
                        } else {
                            if (t6) {
                                return 111001;
                            } else {
                                return 111000;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 110111;
                            } else {
                                return 110110;
                            }
                        } else {
                            if (t6) {
                                return 110101;
                            } else {
                                return 110100;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 110011;
                            } else {
                                return 110010;
                            }
                        } else {
                            if (t6) {
                                return 110001;
                            } else {
                                return 110000;
                            }
                        }
                    }
                }
            } else {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 101111;
                            } else {
                                return 101110;
                            }
                        } else {
                            if (t6) {
                                return 101101;
                            } else {
                                return 101100;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 101011;
                            } else {
                                return 101010;
                            }
                        } else {
                            if (t6) {
                                return 101001;
                            } else {
                                return 101000;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 100111;
                            } else {
                                return 100110;
                            }
                        } else {
                            if (t6) {
                                return 100101;
                            } else {
                                return 100100;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 100011;
                            } else {
                                return 100010;
                            }
                        } else {
                            if (t6) {
                                return 100001;
                            } else {
                                return 100000;
                            }
                        }
                    }
                }
            }

        } else {//t1
            if (t2) {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 011111;
                            } else {
                                return 011110;
                            }
                        } else {
                            if (t6) {
                                return 011101;
                            } else {
                                return 011100;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 011011;
                            } else {
                                return 011010;
                            }
                        } else {
                            if (t6) {
                                return 011001;
                            } else {
                                return 011000;
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 010111;
                            } else {
                                return 010110;
                            }
                        } else {
                            if (t6) {
                                return 010101;
                            } else {
                                return 010100;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 010011;
                            } else {
                                return 010010;
                            }
                        } else {
                            if (t6) {
                                return 010001;
                            } else {
                                return 010000;
                            }
                        }
                    }
                }
            } else {
                if (t3) {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 001111;
                            } else {
                                return 001110;
                            }
                        } else {
                            if (t6) {
                                return 001101;
                            } else {
                                return 001100;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 001011;
                            } else {
                                return 001010;
                            }
                        } else {
                            if (t6) {
                                return 001001;
                            } else {
                                return 001000;//3
                            }
                        }
                    }
                } else {
                    if (t4) {
                        if (t5) {
                            if (t6) {
                                return 000111;
                            } else {
                                return 000110;
                            }
                        } else {
                            if (t6) {
                                return 000101;
                            } else {
                                return 000100;
                            }
                        }
                    } else {
                        if (t5) {
                            if (t6) {
                                return 000011;
                            } else {
                                return 000010;
                            }
                        } else {
                            if (t6) {
                                return 000001;
                            } else {
                                return 000000;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] argv) {
        Integer[] indexArrays = {1, 2, 3, 4, 5, 6};
        Integer[] valueArrays = {81, 25, 34, 13, 22, 91};
        IntArray indexs = new IntArray();
        IntArray values = new IntArray();
        for (int i = 0; i < indexArrays.length; i++) {
            indexs.add(indexArrays[i]);
            values.add(valueArrays[i]);
        }
        sortIntArrays(indexs, values, false);
        System.out.println("indexArrays:" + indexs.toString());
        System.out.println("valueArrays:" + values.toString());
    }

    //排序 依据 valueArrays的值的大小,对两个列表进行排序
    //ifSortOrder true 从小到大  false 从大到小
    public static void sortIntArrays(IntArray indexArrays, IntArray valueArrays, boolean ifSortOrder) {
        if (indexArrays == null || valueArrays == null || indexArrays.size != valueArrays.size) {
            return;
        }
        if (ifSortOrder) {//
            for (int i = 0; i < valueArrays.size - 1; i++) {
                // 初始化一个布尔值
                boolean flag = true;
                for (int j = 0; j < valueArrays.size - i - 1; j++) {
                    if (valueArrays.get(j) > valueArrays.get(j + 1)) {
                        valueArrays.swap(j, j + 1);
                        indexArrays.swap(j, j + 1);
                        // 改变flag
                        flag = false;
                    }
                }
                if (flag) {
                    break;
                }
            }
        } else {//从大到小
            for (int i = 0; i < valueArrays.size - 1; i++) {
                // 初始化一个布尔值
                boolean flag = true;
                for (int j = 0; j < valueArrays.size - i - 1; j++) {
                    if (valueArrays.get(j) < valueArrays.get(j + 1)) {
                        valueArrays.swap(j, j + 1);
                        indexArrays.swap(j, j + 1);
                        // 改变flag
                        flag = false;
                    }
                }
                if (flag) {
                    break;
                }
            }
        }
    }

    //1↖ 2↑ 3↗ 4↙ 5↓ 6↘ 根据位置给予顺时针值
    public static int[] getPotionSortByDirect(int direct) {
        switch (direct) {
            case 1:
                return ResDefaultConfig.Map.POTION_SORT_DIRECT_1;
            case 2:
                return ResDefaultConfig.Map.POTION_SORT_DIRECT_2;
            case 3:
                return ResDefaultConfig.Map.POTION_SORT_DIRECT_3;
            case 4:
                return ResDefaultConfig.Map.POTION_SORT_DIRECT_4;
            case 5:
                return ResDefaultConfig.Map.POTION_SORT_DIRECT_5;
            case 6:
                return ResDefaultConfig.Map.POTION_SORT_DIRECT_6;
        }

        return null;
    }

    //判断array的集合的有数据的数量是否超过 个数
    public static boolean ifHaveMultipleArrays(int multipleCount, Array... arrays) {
        int s = 0;
        for (int i = 0; i < arrays.length; i++) {
            Array a = arrays[i];
            if (a != null && a.size > 0) {
                s++;
            }
            if (s > multipleCount) {
                return true;
            }
        }

        return false;
    }

    //战斗百科
    public static int getDamageForUnit(MainGame game, Fb2Smap.UnitData army, Fb2Smap.UnitData tarmy, Fb2Smap.BuildData buildData, boolean isCombat, float damageTime) {
        //  Fb2Smap.BuildData buildData=tarmy.getBuildData();
        int armyId = army.getUnitId(0);
        int direct = army.getDirect(tarmy.getHexagonIndex());
        boolean ifBorderAttack=army.getDirectByBorderId(tarmy.getHexagonIndex())>0;
        boolean isRound=army.isRound();
        boolean ifRecord=army.getRoundState()==0;
       // float damageRate = getDamageByDirect(army, tarmy, game.getSMapDAO().masterData.getWidth());
        //根据海陆计算伤害差,如果是非4,8的则伤害削减
        if (army.getUnitType() != 4 && army.getUnitType() != 8 && army.potionIsSea()) {
            armyId = army.getTransportType() + 1400;
        } else if (army.isUnitGroup()) {
            armyId = army.getUnitId(direct);
        }
        //  Element armyE=game.gameConfig.getDEF_ARMY().getElementById(armyId);
        int tDirect = tarmy.getDirect(army.getHexagonIndex());
        int tArmyId = tarmy.getUnitId(tDirect);


        //boolean isRangeAtk=tarmy.getDirectByBorderId(army.getHexagonIndex())==-1;
        Element tarmyE =   tarmy.getArmyE(tDirect);;
        //tarmyE 指受到攻击的类型
        /*if(tarmyE==null){
            int s=0;
        }*/

        Fb2Smap.LegionData l = army.getLegionData();
        Fb2Smap.LegionData tl = tarmy.getLegionData();


        int tm = 0;
        int am = 0;
        boolean changeMorale=true;

        //int limitMinDamage=0;
        //int limitMaxDamage=0;

        //获得伤害 TODO
        int damage = army.getUnitDamage(buildData, tarmy.getHexagonIndex());
        if(ifRecord){
            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"damage:"+damage);
        }
        if (buildData != null && buildData.getLegionIndex() == army.getLegionIndex() && buildData.getBuildWonder() > 0) {
            Element xE = game.gameConfig.getDEF_WONDER().getElementById(buildData.getBuildWonder());
            if (xE != null) {
                int function = xE.getInt("fucntion", 0);
                int effect = xE.getInt("effect", 0);
                int value = xE.getInt("value", 0);
                if (function == 5 && effect == 3) {
                    damage += value;
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," buildWonder:"+buildData.getBuildWonder()+" damage:"+damage);
                    }
                }
            }


        }
        if (l.getSpiritMap() != null && l.ifEffective(16)) {
            if (l.getSpiritMap().containsKey(42) && army.getGeneralIndex() > 0) {
                damage = damage * 2;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"spirit 42 effect: damage:"+damage);
                }
            }
        }
        if (tarmy.getUnitType() == 6 && tarmy.getUnitId(direct) != 1606) {
            damage = damage - tarmy.getLegionData().getFortLvMax();
            if(ifRecord){
                Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"target fort reduceDamage: damage:"+damage);
            }
        }

        if (damage <= 0) {
            damage = 1;
            if(ifRecord){
                Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"minDamage to min: damage:"+damage);
            }
        }
        if (tarmy.getUnitType() == 5 && ComUtil.ifGet(tarmy.getUnitAbility(6) * game.resGameConfig.airReduceDamageChanceForActLv)) {
            damage = damage / 2;
            if(ifRecord){
                Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"airReduceDamage: damage:"+damage);
            }
        }
        Fb2Map.MapHexagon hexagon = tarmy.getHexagonData();
        //结算反击
        if (army.ifUnitHaveFeature(25) && !isRound) {
            damage = damage * (100 + army.getUnitFeatureEffect(25)) / 100;
            if(ifRecord){
                Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," feature:"+game.gameMethod.getFeatureStr(25)+" damage:"+damage);
            }
        }
        int weatherId = army.getWeatherId();
        int ae = game.gameConfig.getDEF_WEATHER().getElementById(weatherId).getInt("armyEfficiency", 100);
        if(ifRecord){
            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," weather unitEffect:"+ae);
        }
        if (army.ifUnitHaveFeature(26) && ae < 100) {
            ae = 100;
        }
        if (hexagon.isSea() && army.ifUnitHaveSkill(113) && ae < 100) {
            if (isCombat) {
                army.drawSkill(113);
            }
            ae = 100;
        }
            damage = damage * ae / 100;

        if(ifRecord){
            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," weather unitEffect:"+ae+" damage:"+damage);
        }
            boolean ifArmyHaveFormation = false;
            if (ifBorderAttack && army.ifUnitGroupIsFormation(direct, true)&&!army.potionIsSea()) {
                ifArmyHaveFormation = true;
            }
            boolean ifTArmyHaveFormation = tarmy.ifUnitGroupIsFormation(tDirect, true);
            //获得武器伤害加成
            float weapBonus = army.getWeaponValue(direct, tarmyE.getInt("type")) / 100f;

        if(ifRecord){
            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," weapBonus:"+weapBonus);
        }
            int missLv = 0;
            int climateZone = 0;
            if (buildData != null) {
                if (buildData.getLegionIndex() == army.getLegionIndex()) {
                    missLv = buildData.getMineralLv();
                }
                climateZone = buildData.getClimateZone();
            }


            if (tarmy.getUnitType() == 5) {
                if (missLv > 0) {
                    weapBonus = weapBonus + missLv * 3f / 100;
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," target is air: weapBonus:"+weapBonus);
                    }
                }
            }


            if (weapBonus == 0) {
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," weapBonus is 0,end it");
                }
                return 0;
            }
            if (game.getSMapDAO().ifFortReduceDamage(army.getUnitType(), army.getUnitId(0), army.getFeature(), army.getTransportType(), army.getHexagonIndex(), tarmy.getHexagonIndex())) {
                weapBonus = weapBonus * 0.5f;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," target have fort: weapBonus:"+weapBonus);
                }
            }

            damage = (int) (damage * weapBonus);
        if(ifRecord){
            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," damage:"+damage +" endWeapBonus:"+weapBonus);
        }

            int crit;
            if (army.potionIsSea()||ifBorderAttack) {
                crit = army.getBorderAttackCrit();
            } else {
                crit = army.getRangeAttackCrit();
            }
            if (ifBorderAttack&&ifArmyHaveFormation && !ifTArmyHaveFormation) {
                crit *= 2;
            }
            //进攻拥有下潜特性的单位时,暴击率增加
            if (tarmy.ifUnitHaveFeature(17) && army.ifUnitHaveFeature(18)) {
                damage = damage * (100 + army.getUnitFeatureEffect(18)) / 100;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," feature:"+game.gameMethod.getFeatureStr(18)+" damage:"+damage);
                }
                if (isCombat) {
                    army.drawFeature(18);
                }
            }
        //对建筑和工事造成额外伤害
        if (army.ifUnitHaveFeature(34) && tarmy.getUnitType() == 6) {
            damage = damage * (100 + army.getUnitFeatureEffect(34)) / 100;
            if(ifRecord){
                Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," feature:"+game.gameMethod.getFeatureStr(34)+" damage:"+damage);
            }
            if (isCombat) {
                army.drawFeature(34);
            }
        }
            if (army.ifUnitHaveFeature(21) && army.getIfMove() == 0) {
                crit = crit + army.getUnitFeatureEffect(21);
                if (isCombat) {
                    army.drawFeature(21);
                }
            }

            if (army.ifUnitHaveFeature(23) && tarmy.potionIsSea()) {
                crit = crit + army.getUnitFeatureEffect(23);
                if (isCombat) {
                    army.drawFeature(23);
                }
            }
            if (l.ifAiCheatChance() && game.getSMapDAO().roundState != 0 && !tarmy.isPlayer() && !army.isPlayer() && tarmy.isPlayerAlly() && !army.isPlayerAlly()) {//队友会受到敌人额外的暴击
                damage = damage * 2 + Math.abs(tl.varRegionCount - l.varRegionCount) + Math.abs(tl.incomeMoney - l.incomeMoney);
                // Gdx.app.log("ai_crit: " ,crit+"");
            }

        if (game.getSMapDAO().masterData.getPlayerMode() != 2&&game.resGameConfig.ifTerrainEffect) {
            int terrainId = hexagon.getActualTerrain();
            Element terrainE = game.gameConfig.getDEF_TERRAIN().getElementById(terrainId);

            if (terrainE != null) {
                int te = game.gameMethod.getTerrainEffect(terrainE, army.getUnitType());//我方类型对目标类型造成的伤害比
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," terrainDamageRate:"+te);
                }
                if (te < 100) {
                    if (army.ifUnitHaveFeature(27)) {
                        te = 100;
                        if (isCombat) {
                            army.drawFeature(27);
                        }
                    } else if (army.ifUnitHaveSkill(17)) {
                        te = 100;
                        army.drawSkill(17);
                    }
                }
                if (hexagon.isSea() && army.ifUnitHaveSkill(113) && te < 100) {
                    te = 100;
                    army.drawSkill(113);
                }
                damage = damage * te / 100;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," terrainDamageRate:"+te+", damage:"+damage);
                }
                tm += terrainE.getInt("exrLos", 0);
                //根据军团特性来结算伤害
                damage = damage * (100 + game.gameMethod.getLegionEffectValueForUnitDamage(l, army.getUnitType(), army.getUnitId(0), terrainId)) / 100;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"getLegionEffectValueForUnitDamage, damage:"+damage);
                }
                crit = crit + game.gameMethod.getLegionEffectValueForUnitCrit(l, army.getUnitType(), army.getUnitId(0));
                if (terrainId == 0 && !army.potionIsSea() && army.ifUnitHaveSkill(73)) {
                    damage = damage * (100 + army.getUnitSkillEffect(73)) / 100;
                    army.drawSkill(73);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(73)+" damage:"+damage);
                    }
                }
                if (!army.potionIsSea() && (climateZone == 8 || terrainId == 4 || weatherId == 5 || weatherId == 6) && army.ifUnitHaveSkill(83)) {
                    damage = damage * (100 + army.getUnitSkillEffect(83)) / 100;
                    army.drawSkill(83);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(83)+" damage:"+damage);
                    }
                }
                if (!army.potionIsSea() && (terrainId == 4 || terrainId == 5 || terrainId == 6) && army.ifUnitHaveSkill(84)) {
                    damage = damage * (100 + army.getUnitSkillEffect(84)) / 100;
                    army.drawSkill(84);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(84)+" damage:"+damage);
                    }
                }
                if ((army.potionIsSea() || terrainId == 1 || terrainId == 2 || terrainId == 11 || weatherId == 2 || weatherId == 4) && army.ifUnitHaveSkill(94)) {
                    damage = damage * (100 + army.getUnitSkillEffect(94)) / 100;
                    army.drawSkill(94);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(94)+" damage:"+damage);
                    }
                }
                if (!army.potionIsSea() && (climateZone == 11 || weatherId == 3 || weatherId == 7) && army.ifUnitHaveSkill(102)) {
                    damage = damage * (100 + army.getUnitSkillEffect(102)) / 100;
                    army.drawSkill(102);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(102)+" damage:"+damage);
                    }
                }
            }

        }
            //插入技能结算 陆:陆
            boolean ifRound = army.isRound();
            int skillEffect = army.getUnitSkillEffect(91);
            if (army.ifUnitHaveSkill(91) && army.getUnitHpRate() < skillEffect) {
                damage = damage * (100 + skillEffect) / 100;
                army.drawSkill(91);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(91)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(8) && army.getUnitType() == 1) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(8)) / 100;
                tarmy.drawSkill(8);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(8)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(50) && army.getUnitType() == 2) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(50)) / 100;
                tarmy.drawSkill(50);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(50)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(56) && army.getUnitType() == 4) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(56)) / 100;
                tarmy.drawSkill(56);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(56)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(80) && army.getUnitType() == 8) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(80)) / 100;
                tarmy.drawSkill(80);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(80)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(121) && army.getUnitType() == 3) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(121)) / 100;
                tarmy.drawSkill(121);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(121)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(30) && army.getUnitType() == 5) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(30)) / 100;
                tarmy.drawSkill(30);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(30)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(57) && army.getMaxRange() < 2) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(57)) / 100;
                tarmy.drawSkill(57);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(57)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(59) && army.getMaxRange() > 1) {
                damage = damage * (100 - tarmy.getUnitSkillEffect(59)) / 100;
                tarmy.drawSkill(59);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(59)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(67)) {
                damage = damage * (100 + tarmy.getUnitSkillEffect(67)) / 100;
                tarmy.drawSkill(67);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(67)+" damage:"+damage);
                }
            }
            if (army.ifUnitHaveSkill(67)) {
                damage = damage * (100 + army.getUnitSkillEffect(67)) / 100;
                army.drawSkill(67);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(67)+" damage:"+damage);
                }
            }
            if (tarmy.ifUnitHaveSkill(103)) {
                damage = damage * (100 + tarmy.getUnitSkillEffect(103)) / 100;
                tarmy.drawSkill(103);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(103)+" damage:"+damage);
                }
            }
            if (army.ifUnitHaveSkill(103)) {
                damage = damage * (100 + army.getUnitSkillEffect(103)) / 100;
                army.drawSkill(103);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(103)+" damage:"+damage);
                }
            }
            if (army.ifUnitHaveSkill(104)) {
                damage = damage * (100 - ComUtil.min(army.getUnitMorale(), army.getUnitHpRate())) / 100;
                army.drawSkill(104);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(104)+" damage:"+damage);
                }
            }
            if (army.ifUnitHaveSkill(10) && army.inCity()) {
                damage = damage * (100 + army.getUnitSkillEffect(10)) / 100;
                army.drawSkill(10);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(10)+" damage:"+damage);
                }
            }
            if (army.ifUnitHaveSkill(69)) {
                damage = damage * (100 + army.getUnitSkillEffect(69) * army.getDistance(tarmy.getHexagonIndex())) / 100;
                army.drawSkill(69);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(69)+" damage:"+damage);
                }
            }
            if (army.ifUnitHaveSkill(4)) {//暴击几率+{0}
                crit += army.getUnitSkillEffect(4);
                army.drawSkill(4);
            }
            if (army.ifUnitHaveSkill(5) && tarmy.getUnitType() == 6) {
                damage = damage * (100 + army.getUnitSkillEffect(5)) / 100;
                army.drawSkill(5);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(5)+" damage:"+damage);
                }
            }
            if (ifRound) {
                if (buildData != null) {
                    int effect = buildData.getUnitSkillEffect(112);
                    if (effect > 0) {
                        crit += effect;
                        buildData.drawSkill(112);
                    }
                }
                if (army.ifUnitHaveSkill(2) && army.getIfMove() == 0) {
                    damage = damage * (100 + army.getUnitSkillEffect(2)) / 100;
                    army.drawSkill(2);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(2)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(3) && army.getUnitHpRate() > tarmy.getUnitHpRate()) {
                    damage = damage * (100 + army.getUnitSkillEffect(3)) / 100;
                    army.drawSkill(3);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(3)+" damage:"+damage);
                    }
                }
                if (isCombat && army.ifUnitHaveSkill(6)) {//每次攻击额外损失20%血量但使伤害增加99999
                    damage = 99999;
                    army.setUnitHpNow((int) (army.getUnitHpNow() * 0.8f) + 1);
                    army.drawSkill(6);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(6)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(15) && tarmy.getUnitType() == 8) {
                    damage = damage * (100 + army.getUnitSkillEffect(15)) / 100;
                    army.drawSkill(15);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(15)+" damage:"+damage);
                    };
                }
                if (isCombat && army.ifUnitTriggerSkill(31) && tarmy.getUnitType() == 6) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                    tarmy.addUnitRound(1);
                    army.drawSkill(31);
                }
                if (isCombat && army.ifUnitTriggerSkill(34)) {//攻击后,有{1}%几率使敌人无法行动
                    tarmy.addUnitRound(1);
                    army.drawSkill(34);
                }
                if (isCombat && army.ifUnitTriggerSkill(37)) {//攻击后,有{1}%几率士气上升额外加10
                    am += army.getUnitSkillEffect(37);
                    army.drawSkill(37);
                }
                if (army.ifUnitHaveSkill(40) && tarmy.getUnitType() != 4 && tarmy.getUnitType() != 8 && tarmy.getTransportType() == 0) {
                    damage = damage * (100 + army.getUnitSkillEffect(40)) / 100;
                    army.drawSkill(40);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(40)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(42) && tarmy.getAroundUnitCount(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                    damage = damage * (100 + army.getUnitSkillEffect(42)) / 100;
                    army.drawSkill(42);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(42)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(43) && tarmy.getAroundUnitCount(2) > 2) {//进攻受到夹击的敌人后,造成的伤害增加{0}%
                    damage = damage * (100 + army.getUnitSkillEffect(43)) / 100;
                    army.drawSkill(43);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(43)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(51) && (army.getUnitHpRate() > tarmy.getUnitHpRate() || army.getUnitHpNow() > tarmy.getUnitHpNow() || army.getUnitMorale() > tarmy.getUnitMorale())) {
                    damage = damage * (100 + army.getUnitSkillEffect(51)) / 100;
                    army.drawSkill(51);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(51)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(53) && tarmy.getUnitType() == 1) {//当目标是步兵时,必定对其暴击
                    crit = 100;
                    army.drawSkill(53);
                }
                if (isCombat && army.ifUnitTriggerSkill(63)&&!tarmy.inCity() && tarmy.getGeneralIndex() == 0) {//进攻后有{1}%几率使敌方普通单位叛变
                    tarmy.getHexagonData().setAllLegionIndex(army.getLegionIndex());
                    army.drawSkill(63);
                }
                if (army.ifUnitHaveSkill(64)) {//暴击几率随血量减少增加
                    crit += (100 - army.getUnitHpRate()) / 2;
                    army.drawSkill(64);
                }
                if (army.ifUnitHaveSkill(71) && tarmy.getUnitHpRate() < army.getUnitSkillEffect(71)) {
                    damage = 99999;
                    army.drawSkill(71);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(71)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(74) && tarmy.getUnitType() == 6) {
                    damage = damage * (100 + army.getUnitSkillEffect(74)) / 100;
                    army.drawSkill(74);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(74)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(75) && (army.getUnitHpRate() < tarmy.getUnitHpRate() || army.getUnitHpNow() < tarmy.getUnitHpNow() || army.getUnitMorale() < tarmy.getUnitMorale())) {
                    damage = damage * (100 + army.getUnitSkillEffect(75)) / 100;
                    army.drawSkill(75);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(75)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(77)) {//攻击时必然暴击
                    crit = 100;
                    army.drawSkill(77);
                }

                if (army.ifUnitHaveSkill(81)) {
                    damage = damage * (100 + army.getUnitSkillEffect(81)) / 100;
                    army.drawSkill(81);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(81)+" damage:"+damage);
                    }
                }
                if (isCombat && army.ifUnitHaveSkill(105) && army.getUnitRank() > tarmy.getUnitRank() && tarmy.getUnitType() != 6) {
                    int effect = army.getUnitSkillEffect(105);
                    damage = damage * (100 + effect) / 100;
                    tm += -effect;
                    army.drawSkill(105);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(105)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(111) && tarmy.potionIsSea() && (tarmy.getTransportType() != 1 && tarmy.getTransportType() != 2 && tArmyId != 1401 && tArmyId != 1402)) {
                    damage = damage * (100 + army.getUnitSkillEffect(111)) / 100;
                    army.drawSkill(111);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(111)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(116) && tarmy.getUnitType() == 3) {
                    damage = damage * (100 + army.getUnitSkillEffect(116)) / 100;
                    army.drawSkill(116);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(116)+" damage:"+damage);
                    }
                }
                if (army.ifUnitHaveSkill(124) && tarmy.getUnitType() == 1) {
                    damage = damage * (100 + army.getUnitSkillEffect(124)) / 100;
                    army.drawSkill(124);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(124)+" damage:"+damage);
                    }
                }

                if (isCombat && army.ifUnitTriggerSkill(126)) {//进攻敌方建筑后,有{1}%几率使建筑无法使用
                    tarmy.addUnitRound(army.getUnitSkillEffect(126));
                    army.drawSkill(126);
                }
            } else {
                if (army.ifUnitHaveSkill(86)) {
                    damage = damage * (100 + army.getUnitSkillEffect(86)) / 100;
                    army.drawSkill(86);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(86)+" damage:"+damage);
                    }
                }
            }
            if (tarmy.ifUnitHaveSkill(62)) {//受到来自空中伤害减少{0}%
                damage = damage * (100 - tarmy.getUnitSkillEffect(62)) / 100;
                tarmy.drawSkill(62);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(62)+" damage:"+damage);
                }
            }
            boolean ifCrit=false;
            //有5%*攻击等级的几率攻击力翻倍
            if (Math.max(army.getUnitMorale(),crit)>game.resGameConfig.unitMoraleMaxLimit&&ComUtil.ifGet(crit)) {
                damage = (int) (damage * game.resGameConfig.critDamageRatio);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"trigger crit, damage:"+damage);
                }
                if (army.getUnitType() == 5 && tarmy.isUnitGroup() && tarmy.getUnitType() != 6 && !tarmy.potionIsSea()) {
                    damage = damage *  ComUtil.max(tarmy.getUnitCount(), 1);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"unitCountAddDamage, damage"+damage);
                    }
                } else {
                    if (crit > 100) {//如果暴击率超过100,则多余的暴击会转化为额外伤害
                        damage = damage * crit / 100;
                        if(ifRecord){
                            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"critAddDamage, damage:"+damage);
                        }
                    }
                }
                if (isCombat && ifRound) {
                    if (army.ifUnitHaveSkill(13)) {//对敌人暴击时可以使敌人士气下降{0}
                        tm += -army.getUnitSkillEffect(13);
                        army.drawSkill(13);
                    }
                    if (army.ifUnitHaveSkill(44)) {//对敌人造成暴击伤害时,敌人无法行动
                        tarmy.addUnitRound(1);
                        army.drawSkill(44);
                    }
                    if (army.ifUnitHaveSkill(85) && army.getUnitMorale() > tarmy.getUnitMorale()) {//暴击可以降低低于我方士气敌人的能力
                        tarmy.randomReduceSkillLv(1);
                        army.drawSkill(85);
                    }
                }
                ifCrit=true;
            }
            //重置暴击
            if(isCombat && ifRound   ){
                if(ifCrit||(!ifBorderAttack&&!army.potionIsSea()&&army.getUnitMorale()>game.resGameConfig.unitMoraleMaxLimit)){
                    if(army.getUnitMorale()>game.resGameConfig.resetUnitMoraleMax){
                        army.setUnitMorale(game.resGameConfig.resetUnitMoraleMax);
                        changeMorale=false;
                    }
                }/*else if(!ifBorderAttack){//如果是远程攻击
                    if(army.getUnitMorale()>game.resGameConfig.unitMoraleMaxLimit-1){
                        army.setUnitMorale(game.resGameConfig.unitMoraleMaxLimit-1);
                        changeMorale=false;
                    }
                }*/
            }

            if (ifArmyHaveFormation && tarmy.isUnitGroup() && tarmy.getUnitType() != 6 && !tarmy.potionIsSea()&&ifBorderAttack) {
                if (!tarmy.ifUnitGroupIsFormation(tDirect, false)) {
                    int uc = ComUtil.max(tarmy.getUnitCount(), 1);
                    damage = damage * uc;
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"formationAttack: tarmyUnitCount:"+uc+", damage:"+damage);
                    }
                }
            }



            //如果是建筑,则根据actlv来增加坦度,因为actlv没用
            if (tarmy.ifUnitHaveFeature(11) && tarmy.ifUnitTriggerFeature(11)) {
                if (ComUtil.ifGet((tarmy.getUnitRank() - army.getUnitRank()) * 2 * game.resGameConfig.addDefChanceForRankLvPoor)) {
                    damage = damage / 2;
                    if (isCombat) {
                        tarmy.drawFeature(11);
                    }
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," tarmyfeature:"+game.gameMethod.getFeatureStr(11)+" damage:"+damage);
                    }
                } else if (ComUtil.ifGet((tarmy.getUnitRank() - army.getUnitRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                    damage = damage / 2;
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," tarmyfeature:"+game.gameMethod.getFeatureStr(11)+" damage:"+damage);
                    }
                }
            } else {   //有5%*防御等级的几率豁免受到的伤害减半
                if (tarmy.getUnitType() == 5) {
                    if (ComUtil.ifGet(tarmy.getUnitAbility(4) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                        damage = damage / 2;
                        if(ifRecord){
                            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"reduceDamageByDefLv,  damage:"+damage);
                        }
                    }
                } else if (ComUtil.ifGet((tarmy.getUnitRank() - army.getUnitRank()) * game.resGameConfig.addDefChanceForRankLvPoor)) {
                    damage = damage / 2;
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"reduceDamageByRankPoor, damage:"+damage);
                    }
                }
            }

            //如果进攻的是飞机
            if (army.getUnitType() == 5) {
                if (buildData.getLegionIndex() == tarmy.getLegionIndex()) {
                    damage = damage * (100 - buildData.getMissileLvNow() * 5) / 100;
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"buildMissileLv reduce damage, damage:"+damage);
                    }
                }
            }
            //减免受到的空袭伤害,额外增加对敌方空军的反击伤害
            if (army.ifUnitHaveFeature(19) && army.ifUnitTriggerFeature(19) && tarmy.getUnitType() == 5) {
                damage = (int) (damage * 2f);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," feature:"+game.gameMethod.getFeatureStr(19)+" damage:"+damage);
                }
                if (isCombat) {
                    army.drawFeature(19);
                }
            }
            if (tarmy.ifUnitHaveFeature(19) && tarmy.ifUnitTriggerFeature(19) && army.getUnitType() == 5) {
                damage = damage / 2;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," tarmyfeature:"+game.gameMethod.getFeatureStr(19)+" damage:"+damage);
                }
                if (isCombat) {
                    army.drawFeature(19);
                }
            }


            //根据士气对伤害进行判断
            if (army.ifUnitHaveSkill(61) && army.getUnitHpRate() < 50) {
                army.drawSkill(61);
            } else {
                damage = damage * (army.getUnitHpRate() / 2 + 50) / 100;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"unitHpRate change damage, damage:"+damage+" hpRate:"+army.getUnitHpRate());
                }
            }
            if (!army.isUnitGroup()) {
                damage = damage * (100 + army.getUnitAbility(1) * game.resGameConfig.addDamageForAckLv) / 100;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"no groupUnit addDamage for ackLv, damage:"+damage);
                }
            }
            if (tarmy.inCapital()) {
                damage = damage / 2;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"reduce damage because target is capital, damage:"+damage);
                }
            }

            if (game.getSMapDAO().masterData.getPlayerMode() != 2 ) {
                //根据位置计算伤害差值
                float damageRate = getDamageByDirect(army, tarmy, game.getSMapDAO().masterData.getWidth());

                damage = (int) (damage * damageRate);
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"change damage by potion, damage:"+damage +" damageRate:"+damageRate);
                }
                if (!ifTArmyHaveFormation && army.ifUnitHaveSkill(9)) {//对非正面敌军造成的伤害增加{0}%
                    damage = damage * (100 + army.getUnitSkillEffect(9)) / 100;
                    army.drawSkill(9);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(9)+" damage:"+damage);
                    }
                }
                if (ifTArmyHaveFormation && tarmy.ifUnitHaveSkill(118)) {
                    damage = damage * (100 - tarmy.getUnitSkillEffect(118)) / 100;
                    tarmy.drawSkill(118);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex()," skill:"+game.gameMethod.getSkillStr(118)+" damage:"+damage);
                    }
                }
                if (isCombat && GameMap.getHX(army.getHexagonIndex(), army.getMapW()) != GameMap.getHX(tarmy.getHexagonIndex(), tarmy.getMapW())) {
                    tarmy.updUnitModelDirect();
                    tm += -ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax / 2, game.resGameConfig.unitMoraleChangeValueMax);
                    am += ComUtil.getRandom(game.resGameConfig.unitMoraleChangeValueMax / 2, game.resGameConfig.unitMoraleChangeValueMax);
                }
            }
            if (isCombat) {
                if (!ifTArmyHaveFormation) {
                    if (tm > 0) {
                        tm = -tm;
                    } else {
                        tm = tm * 2;
                    }
                    if (am < 0) {
                        am = -am;
                    } else {
                        am = am * 2;
                    }
                }
                if (tm != 0) {
                    tarmy.setUnitMoraleChange(tm);
                    tarmy.drawMorale(tm, damageTime);
                }
                if (am != 0&&changeMorale) {
                    army.setUnitMoraleChange(am);
                    army.drawMorale(am, damageTime);
                }
            }
            if (army.ifUnitTriggerSkill(21) && tarmy.getUnitType() == 1) {
                army.drawSkill(21);
            } else if (army.ifUnitTriggerSkill(22) && tarmy.potionIsSea()) {
                army.drawSkill(22);
            } else if (army.ifUnitTriggerSkill(24) && tarmy.getUnitType() == 6) {
                army.drawSkill(24);
            } else if (army.ifUnitTriggerSkill(25) && tarmy.getUnitType() == 4) {
                army.drawSkill(25);
            } else if (army.ifUnitTriggerSkill(26) && tarmy.getUnitType() == 5) {
                army.drawSkill(26);
            } else if (army.ifUnitTriggerSkill(27) && tarmy.getUnitType() == 2) {
                army.drawSkill(27);
            } else if (army.ifUnitTriggerSkill(76) && tarmy.getUnitType() == 1) {
                army.drawSkill(76);
            } else if (army.ifUnitTriggerSkill(28) && tarmy.getUnitType() == 8) {
                army.drawSkill(28);
            } else if (army.ifUnitTriggerSkill(29) && tarmy.getUnitType() == 3) {
                army.drawSkill(29);
            } else if (army.ifUnitTriggerSkill(96)) {
                army.drawSkill(96);
            } else {
                // damage=damage*100/(100+(tarmy.getArmyFormationArmor(tDirect)*(50+tarmy.getArmyMorale())/100));

                if(tarmy.getUnitType()!=5&&army.getUnitType()==5){//飞机进攻地面单位
                    damage = damage * game.resGameConfig.unitArmorForairDenfenceBaseBeardamage / (game.resGameConfig.unitArmorForairDenfenceBaseBeardamage + (tarmy.getUnitArmor(0) * (50 + tarmy.getUnitMorale()) / 100));
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"air attack groundUnit, damage:"+damage);
                    }
                }else if (tarmy.isUnitGroup()) {
                    if (army.getUnitType() == 5) {
                        damage = damage * game.resGameConfig.airArmorBaseBeardamage / (game.resGameConfig.airArmorBaseBeardamage + tarmy.getUnitArmor(tDirect) * (100 + tarmy.getDefAirLv() * game.resGameConfig.airDefenseForAdfLv) / 100);
                        if(ifRecord){
                            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"air attack unitGroup, damage:"+damage);
                        }
                    } else {
                        damage = damage * game.resGameConfig.unitArmorBaseBeardamage / (game.resGameConfig.unitArmorBaseBeardamage + (tarmy.getUnitArmor(tDirect) * (50 + tarmy.getUnitMorale()) / 100));
                        if(ifRecord){
                            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"unit attack unitGroup, damage:"+damage);
                        }
                    }
                }  else {
                    if (army.getUnitType() == 5) {
                        damage = damage * (game.resGameConfig.airArmorBaseBeardamage + tarmy.getUnitAbility(2) * game.resGameConfig.airDefenseForAdfLv) / game.resGameConfig.airArmorBaseBeardamage;
                        if(ifRecord){
                            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"air attack singleUnit, damage:"+damage);
                        }
                    } else {
                        damage = damage * game.resGameConfig.unitArmorBaseBeardamage / (game.resGameConfig.unitArmorBaseBeardamage + (tarmy.getUnitArmor(0) * (50 + tarmy.getUnitMorale()) / 100));
                        if(ifRecord){
                            Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"unit attack unitGroup, damage:"+damage);
                        }
                    }
                }
                if (buildData != null && buildData.getLegionIndex() == tarmy.getLegionIndex() && army.getUnitType() == 5 && tarmy.getUnitType() == 6) {
                    damage = damage * 100 / (100 + (buildData.getMissileLvNow() * 2) * game.resGameConfig.addDefEachRank);
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"build reduce air damage, damage:"+damage);
                    }
                }
            }

            if (tarmy.getUnitType() == 5) {
                int minD = Math.min(10, tarmy.getUnitHpMax() / 10 + 1);
                if (damage < minD) {
                    damage = minD;
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"minDamge change, damage:"+damage);
                    }
                }
            } else {
                int minD = Math.min(10, tarmy.getUnitHpMax() / 7 + 1);
                if (damage < minD) {
                    damage = minD;
                    if(ifRecord){
                        Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"minDamge change, damage:"+damage);
                    }
                }
            }
            if(tarmy.isEmptyBuild()){
                damage*=2;
                if(ifRecord){
                    Gdx.app.log("getDamageForUnit:"+army.getHexagonIndex()+">"+tarmy.getHexagonIndex(),"target is emptyBuild, damage:"+damage);
                }
            }
            return damage;
        }

    public static float getTextWidth(BitmapFont font,String str){
            layout.setText(font,str);
            return layout.width;
        }

    public static String formmatString(BitmapFont font, String str, int length, boolean ifLeft,String str1) {
        // int num = computeDisplayLen(str);
        // int numj=computeDisplayLen(str1);
        int num = (int) getTextWidth(font,str);
        int numj= (int) getTextWidth(font,str1);
        length=numj*length;
        if (num == length) {
            return str;
        } else if (num< length) {
            for (int i = 0, iMax = length - num; i < iMax; i+=numj) {
                if (ifLeft) {
                    str = str1 + str;
                } else {
                    str = str + str1;
                }
            }
            if(getTextWidth(font,str)<num&&str1.length()>1){
                if (ifLeft) {
                    str = str1.substring(0,1) + str;
                } else {
                    str = str + str1.substring(0,1);
                }
            }
            Gdx.app.log("formmatString:"+ num,str);
            return str;
        } else {
            return ComUtil.formmatString(str,length,ifLeft,str1);
        }
    }


    public static IntArray  setValueInIntArray(String coreAreaSRs, IntArray coreAreaSRs1) {
        if(coreAreaSRs1==null){
            coreAreaSRs1=new IntArray();
        }else {
            coreAreaSRs1.clear();
        }
        if(ComUtil.isEmpty(coreAreaSRs)){
            return coreAreaSRs1;
        }
        String[] strs = coreAreaSRs.split(",");
        for(int i=0;i<strs.length;i++){
            if(!ComUtil.isNumeric(strs[i])){
                continue;
            }
            int area=Integer.parseInt(strs[i]);
            coreAreaSRs1.add(area);
        }
        return coreAreaSRs1;
    }

    //direct 1↖ 2↑ 3↗ 4↙ 5↓ 6↘
    public static int getOppositeDirect(int direct){
        switch (direct){
            case 1:
                return 6;
            case 2:
                return 5;
            case 3:
                return 4;
            case 4:
                return 3;
            case 5:
                return 2;
            case 6:
                return 1;
        }
        return 0;
    }

    //判定a单位进攻的是不是b单位的背面
    public static boolean ifRaidAttack(Fb2Smap.ArmyData a1, Fb2Smap.ArmyData a2) {
        int arrowDirect=  a2.getArrowDirect();
        if(arrowDirect==0){
            return false;
        }else{
            int direct=a1.getDirect(a2.getHexagonIndex());
            if(arrowDirect==direct){
                return true;
            }
            return false;
        }
    }
}
