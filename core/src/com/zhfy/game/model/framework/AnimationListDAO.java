package com.zhfy.game.model.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.XmlReader;
import com.zhfy.game.MainGame;
public class AnimationListDAO {
    private IntMap<Animation> animationMap;
    private MainGame game;

    public AnimationListDAO(MainGame game){
        game.gameConfig.getDEF_ANIMATION();
        animationMap=new IntMap<>();
    }

    public Animation getAnimation(int id){
        if(animationMap.containsKey(id)){
            return animationMap.get(id);
        }else {
            XmlReader.Element anE = game.gameConfig.getDEF_ANIMATION().getElementById(id);
            if (anE != null) {
                String[] imageListNames = anE.get("imgListName").split(",");
                TextureRegion[] textureRs = new TextureRegion[imageListNames.length];
                for (int i = 0; i < imageListNames.length; i++) {
                    if (game.getImgLists().contains(imageListNames[i])) {
                        textureRs[i] = game.getImgLists().getTextureByName(imageListNames[i]).getTextureRegion();
                    } else {
                        Gdx.app.error("no animationImage1", "" + imageListNames[i]);
                    }
                }
                Animation a = new Animation(anE.getInt("duration", 1),(Object) textureRs);
                animationMap.put(id,a);
                return a;
            } else {
                Gdx.app.error("no animation", "" + id);
                return null;
            }
        }
    }
    //切换场景时需要调用这个方法,防止因为图片卸载导致的动画bug
    public void clear(){
        animationMap.clear();
    }


}
