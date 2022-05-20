package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.zhfy.game.config.ResDefaultConfig;

//210318
public class EncryptTextureLoader extends AsynchronousAssetLoader<Texture, EncryptTextureLoader.TextureParameter> {


    public EncryptTextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureParameter parameter) {

    }

    @Override
    public Texture loadSync(AssetManager manager, String fileName, FileHandle file, TextureParameter parameter) {
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
        Gdx.app.log("加载特殊资源",fileName);
        Pixmap   pixmap = new Pixmap(allBytes, 0, allBytes.length);
     Texture   texture = new Texture(pixmap);
        return texture;
    }


    public class TextureParameter extends AssetLoaderParameters<Texture> {
    }


}
