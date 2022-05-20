package com.zhfy.game.model.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.async.AsyncExecutor;
/*import com.esotericsoftware.spine.SkeletonData;
import com.mytian.mgarden.stages.classes.BaseClassLoadingGroup;
import com.mytian.mgarden.stages.classes.ClassLoadingGroup;
import com.mytian.mgarden.stages.classes.hh.ClassLoadingGroupHH;
import com.mytian.mgarden.utils.particleutil.CCDictionaryLoader;*/

import java.lang.reflect.Field;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Assets extends AssetManager {
    private static Assets instance;
    public final static TextureLoader.TextureParameter RGB4444_TEXTURE_PARAMETER = new TextureLoader.TextureParameter();
    public final static TextureLoader.TextureParameter RGB888_TEXTURE_PARAMETER = new TextureLoader.TextureParameter();
    public final static TextureLoader.TextureParameter RGB565_TEXTURE_PARAMETER = new TextureLoader.TextureParameter();


    public final static  BitmapFontLoader.BitmapFontParameter BITMAP_FONT_PARAMETER_TEXTURE_FILTER =
            new BitmapFontLoader.BitmapFontParameter();
    static{
        BITMAP_FONT_PARAMETER_TEXTURE_FILTER.minFilter = Texture.TextureFilter.Linear;
        BITMAP_FONT_PARAMETER_TEXTURE_FILTER.magFilter = Texture.TextureFilter.Linear;
    }

    private volatile boolean isLoad;

    static {
        RGB4444_TEXTURE_PARAMETER.format = Pixmap.Format.RGBA4444;
        RGB888_TEXTURE_PARAMETER.format = Pixmap.Format.RGB888;
        RGB565_TEXTURE_PARAMETER.format = Pixmap.Format.RGB565;
    }

    protected Assets(FileHandleResolver resolver) {
        super(resolver);
        instance = this;
        //TODO
        /*setLoader(SkeletonData.class, new SkeletonLoader(resolver));
        setLoader(mytian.esotericsoftware.spine.SkeletonData.class
                , new SkeletonLoader3(resolver));
        setLoader(ObjectMap.class, new CCDictionaryLoader(resolver));*/
        try {
            Field executor = AssetManager.class.getDeclaredField("executor");
            executor.setAccessible(true);
            final AsyncExecutor mAsyncExecutor = (AsyncExecutor) executor.get(this);
            executor = AsyncExecutor.class.getDeclaredField("executor");
            executor.setAccessible(true);
            final ThreadPoolExecutor mThreadPoolExecutor = (ThreadPoolExecutor) executor.get(mAsyncExecutor);
            mThreadPoolExecutor.setThreadFactory(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    final Thread thread = new Thread(runnable, "AsynchExecutor-Thread " + System.currentTimeMillis());
                    thread.setPriority(Thread.MIN_PRIORITY);
                    thread.setDaemon(true);
                    return thread;
                }
            });
            mThreadPoolExecutor.setCorePoolSize(0);
            mThreadPoolExecutor.setMaximumPoolSize(1);
            mThreadPoolExecutor.setKeepAliveTime(3, TimeUnit.MINUTES);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 可以获取到已经加载好的texture、textureAtlas、particleEffect、sound。
     * 如果未加载或未加载完成，会采用同步方式加载一个并返回。
     *
     * @param <T>
     */

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        if (isLoaded(path)) {
            return (T) get(path, getAssetType(path));
        } else {
            add(path);
            finishLoadingAsset(path);
            return get(path);
        }
    }

    public FileHandle file(String path) {
        return getFileHandleResolver().resolve(path);
    }

    /**
     * 添加要加载的资源到队列，此时并未加载。加载为异步。 默认仅加载png/jpg、p/plist（粒子文件）、atlas、mp3/wav格式的文件。
     *
     * @param filePath 资源的internal路径。可直接传入com.mytian.R包中的变量
     */
    public void add(final String filePath) {
        if (isLoaded(filePath)) {
            return;
        }
        final String lowerPath = filePath.toLowerCase();
        if (lowerPath.endsWith(".png") || lowerPath.endsWith(".jpg")) {
            if (lowerPath.contains("rgb4444_")) {
                load(filePath, Texture.class, RGB4444_TEXTURE_PARAMETER);
            } else if (lowerPath.contains("rgb888_")) {
                load(filePath, Texture.class, RGB888_TEXTURE_PARAMETER);
            } else if (lowerPath.contains("rgb565_")) {
                load(filePath, Texture.class, RGB565_TEXTURE_PARAMETER);
            } else {
                load(filePath, Texture.class);
            }
        } else if (lowerPath.endsWith(".mp3") || lowerPath.endsWith(".wav")) {
            load(filePath, Sound.class);
            //TODO
        /*} else if (lowerPath.endsWith(".json") || lowerPath.endsWith(".skel")) {
            if (lowerPath.contains("3_6_52_1")) {
                if (lowerPath.endsWith("erji59.skel") || lowerPath.endsWith("erji60.skel")
                        || lowerPath.endsWith("erji61.skel") || lowerPath.endsWith("erji62.skel")
                        || lowerPath.endsWith("erji63.skel")) {
                    load(filePath, mytian.esotericsoftware.spine.SkeletonData.class,new
                            SkeletonLoader3.SkeletonDataParam(0.90f));
                } else {
                    load(filePath, mytian.esotericsoftware.spine.SkeletonData.class);
                }
            } else {
                load(filePath, SkeletonData.class);
            }
        */} else if (lowerPath.endsWith(".atlas")) {
            load(filePath, TextureAtlas.class);
        } else if (lowerPath.endsWith(".fnt")) {
            load(filePath, BitmapFont.class, BITMAP_FONT_PARAMETER_TEXTURE_FILTER);
        } else if (lowerPath.endsWith(".plist")) {
            load(filePath, ObjectMap.class);
        }
    }

    /**
     * 遍历所传R包中的类下的所有文件路径。
     *
     * @param cls 传入com.mytian.R包中的类或内部类。
     */
    public synchronized void addDir(final Class<?> cls) {
        Array<String> filePaths = getPaths(cls);
        for (String path : filePaths) {
            if (path.endsWith(".png") && (filePaths.contains(path.replace(".png", ".atlas"), false)
                    || filePaths.contains(path.replace(".png", ".p"), false))) {
                continue;
            }
            if (path.endsWith(".json") && !filePaths.contains(path.replace(".json", ".atlas"), false)) {
                continue;
            }
            if (path.endsWith(".mp3") || path.endsWith(".wav")) {
                if (!path.contains("sfx") && !path.contains("svo")) {
                    continue;
                }
            }
            add(path);
        }
    }

    public synchronized void addDir(String className) {
        try {
            addDir(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 反射遍历R类下的文件路径
    protected Array<String> getPaths(Class<?> cls) {
        Array<String> filePaths = new Array<String>();
        Class<?>[] classes = cls.getDeclaredClasses();
        if (classes != null) {
            for (Class<?> c : classes) {
                filePaths.addAll(getPaths(c));
            }
        }
        Field[] fileds = cls.getFields();
        try {
            for (Field f : fileds) {
                if (f.getType() == String.class) {
                    f.setAccessible(true);
                    String value = (String) f.get(null);
                    filePaths.add(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePaths;
    }

    /**
     * 释放该R包中的类下的所有文件路径对应的资源
     *
     * @param cls
     */

    public synchronized void removeDir(Class<?> cls) {
        Array<String> filePaths = getPaths(cls);
        for (String path : filePaths) {
            if (isLoaded(path)) {
                unload(path);
            }
        }
    }


    /**
     * 销毁所有已加载的资源，销毁异步执行器，并停止仍在进行的异步加载
     */
    @Override
    public synchronized void dispose() {
        synchronized (Assets.class) {
            instance = null;
            try {
                super.dispose();
            } catch (Exception e) {
                Gdx.app.error("assets dispose","");
            }
            /*try { TODO
                if (null != ClassLoadingGroup.mNativeFont) {
                    ClassLoadingGroup.mNativeFont.dispose();
                    ClassLoadingGroup.mNativeFont = null;
                }
            } catch (Exception e) {

            }
            try {
                if (null != ClassLoadingGroupHH.mNativeFont) {
                    ClassLoadingGroupHH.mNativeFont.dispose();
                    ClassLoadingGroupHH.mNativeFont = null;
                }
            } catch (Exception e) {

            }
            try {
                if (null != BaseClassLoadingGroup.mNativeFont) {
                    BaseClassLoadingGroup.mNativeFont.dispose();
                    BaseClassLoadingGroup.mNativeFont = null;
                }
            } catch (Exception e) {

            }*/
        }
    }

    @Override
    public synchronized <T> void load(final String fileName, final Class<T> type
            , AssetLoaderParameters<T> parameter) {
        super.load(fileName, type, parameter);
        isLoad = true;
    }

    public void render() {
        if (null != instance && isLoad) {
            isLoad = !update();
        }
    }

}
