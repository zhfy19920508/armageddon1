package com.zhfy.game.model.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by gang.zhou on 2018/1/5.
 */

    public class AssetsManagerPool implements Disposable {
        public static int SIZE = Math.max(Runtime.getRuntime().availableProcessors() / 2, 1);
        private Array<Assets> mAssetsArray = new Array(SIZE);
        private int counter = 0;
        private static HashMap<Class, Array<String>> pathM = new HashMap<>();
        private static AssetsManagerPool instance;
        private volatile boolean isLoad;

        public static AssetsManagerPool getInstance() {
            synchronized (AssetsManagerPool.class) {
                if (null == instance) {
                    instance = new AssetsManagerPool();
                }
            }
            return instance;
        }

        private AssetsManagerPool() {
            for (int i = 0; i < SIZE; i++) {
                mAssetsArray.add(new Assets(new InternalFileHandleResolver()));
            }
            for (final Assets assets : mAssetsArray) {
                assets.setErrorListener(new AssetErrorListener() {
                    @Override
                    public void error(AssetDescriptor asset, Throwable throwable) {
                        throw new GdxRuntimeException(throwable);
                    }
                });
            }
        }

        public synchronized void add(final String filePath) {
            for (final Assets assets : mAssetsArray) {
                if (assets.isLoaded(filePath)) {
                    return;
                }
            }
            mAssetsArray.get(counter % SIZE).add(filePath);
            ++counter;
            isLoad = true;
        }


        public static Array<String> getPaths(Class<?> cls) {
            if (pathM.containsKey(cls)) {
                return pathM.get(cls);
            }
            final Array<String> filePaths = new Array<>();
            final Class<?>[] classes = cls.getDeclaredClasses();
            if (null != classes) {
                for (final Class<?> c : classes) {
                    filePaths.addAll(getPaths(c));
                }
            }
            final Field[] fields = cls.getFields();
            try {
                for (final Field f : fields) {
                    if (String.class == f.getType()) {
                        f.setAccessible(true);
                        filePaths.add((String) f.get(null));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pathM.put(cls, filePaths);
            return filePaths;
        }

        public synchronized void addDir(final Class<?> cls) {
            final Array<String> filePaths = getPaths(cls);
            for (final String path : filePaths) {
                if (path.endsWith(".png")) {
                    if (filePaths.contains(path.replace(".png", ".fnt"), false)
                            || filePaths.contains(path.replace(".png", ".atlas"), false)
                            || filePaths.contains(path.replace(".png", ".skel"), false)
                            || filePaths.contains(path.replace(".png", ".json"), false)) {
                        continue;
                    }
                    boolean isFind = false;
                    for (int i = 2; i < 20; i++) {
                        if (path.endsWith(i + ".png")) {
                            if (filePaths.contains(path.replace(i + ".png", ".atlas"), false)
                                    || filePaths.contains(path.replace(i + ".png", ".fnt"), false)
                                    || filePaths.contains(path.replace(i + ".png", ".skel"), false)
                                    || filePaths.contains(path.replace(i + ".png", ".json"), false)) {
                                isFind = true;
                            }
                            break;
                        }
                    }
                    if (isFind) {
                        continue;
                    }
                } else if (path.endsWith(".atlas")) {
                    if (filePaths.contains(path.replace(".atlas", ".json"), false)
                            || filePaths.contains(path.replace(".atlas", ".skel"), false)) {
                        continue;
                    }
                } else if (path.endsWith(".mp3") || path.endsWith(".wav")) {
                    if (!path.contains("sfx") && !path.contains("svo")) {
                        continue;
                    }
                }
                add(path);
            }
        }

        public synchronized void removeDir(Class<?> cls) {
            final Array<String> filePaths = getPaths(cls);
            for (final String path : filePaths) {
                if (path.endsWith(".png")) {
                    if (filePaths.contains(path.replace(".png", ".fnt"), false)
                            || filePaths.contains(path.replace(".png", ".atlas"), false)
                            || filePaths.contains(path.replace(".png", ".skel"), false)
                            || filePaths.contains(path.replace(".png", ".json"), false)) {
                        continue;
                    } else {
                        boolean isFind = false;
                        for (int i = 2; i < 15; i++) {
                            if (path.endsWith(i + ".png")) {
                                if (filePaths.contains(path.replace(i + ".png", ".atlas"), false)
                                        || filePaths.contains(path.replace(i + ".png", ".fnt"), false)
                                        || filePaths.contains(path.replace(i + ".png", ".skel"), false)
                                        || filePaths.contains(path.replace(i + ".png", ".json"),
                                        false)) {
                                    isFind = true;
                                }
                                break;
                            }
                        }
                        if (isFind) {
                            continue;
                        }
                    }
                } else if (path.endsWith(".atlas")) {
                    if (filePaths.contains(path.replace(".atlas", ".json"), false)
                            || filePaths.contains(path.replace(".atlas", ".skel"), false)) {
                        continue;
                    }
                } else if (path.endsWith(".mp3") || path.endsWith(".wav")) {
                    if (!path.contains("sfx") && !path.contains("svo")) {
                        continue;
                    }
                }
                unload(path);
            }
        }


        public synchronized void unload(final String path) {
            for (final Assets assets : mAssetsArray) {
                try {
                    assets.unload(path);
                } catch (Exception e) {
                    Gdx.app.error("am unload",path);
                }
            }
        }

        public synchronized Array<String> getAssetNames(){
            final Array<String> assetNameArray = new Array<>();
            for (final Assets assets : mAssetsArray) {
                assetNameArray.addAll(assets.getAssetNames());
            }
            return assetNameArray;
        }

        public boolean update() {
            boolean complete = true;
            for (final Assets assets : mAssetsArray) {
                complete = assets.update() && complete;
            }
            if (complete) {
                counter = 0;
            }
            return complete;
        }

        public float getProgress() {
            float progress = 0;
            for (final Assets assets : mAssetsArray) {
                progress += assets.getProgress();
            }
            return progress / SIZE;
        }

        public synchronized <T> T get(String path) {
            for (final Assets assets : mAssetsArray) {
                if (assets.isLoaded(path)) {
                    return (T) assets.get(path, assets.getAssetType(path));
                }
            }
            mAssetsArray.get(counter % SIZE).add(path);
            mAssetsArray.get(counter % SIZE).finishLoadingAsset(path);
            return mAssetsArray.get(counter++ % SIZE).get(path);
        }

        public void render() {
            if (null != instance && isLoad) {
                isLoad = !update();
            }
        }

        public FileHandle file(String path) {
            return mAssetsArray.get(0).getFileHandleResolver().resolve(path);
        }

        public synchronized void clear() {
            for (final Assets assets : mAssetsArray) {
                assets.finishLoading();
                assets.clear();
            }
        }

        public void finishLoading() {
            for (final Assets assets : mAssetsArray) {
                assets.finishLoading();
            }
        }

        @Override
        public synchronized void dispose() {
            synchronized (AssetsManagerPool.class) {
                instance = null;
            }
            for (final Assets assets : mAssetsArray) {
                try {
                    assets.dispose();
                } catch (Exception e) {
                    Gdx.app.error("am dispose","");
                }
            }
            mAssetsArray.clear();
        }
    }

