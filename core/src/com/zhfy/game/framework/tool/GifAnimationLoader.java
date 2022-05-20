package com.zhfy.game.framework.tool;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class GifAnimationLoader extends AsynchronousAssetLoader<Animation, GifAnimationLoader.AnimationParameter> {


    public GifAnimationLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AnimationParameter parameter) {

    }

    @Override
    public Animation loadSync(AssetManager manager, String fileName, FileHandle file, AnimationParameter parameter) {
        //Animation anim = com.holidaystudios.tools.GifDecoder.loadGIFAnimation(Animation.LOOP, Gdx.files.internal("my-gif-anumation.gif").read());
     return    GifDecoder.loadGIFAnimation(file.read());
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimationParameter parameter) {
        return null;
    }

    public class AnimationParameter extends AssetLoaderParameters<Animation> {
    }


}
