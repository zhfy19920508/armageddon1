package com.zhfy.game.framework;

import com.zhfy.game.MainGame;
import com.zhfy.game.model.content.conversion.Fb2Smap;

public class GameAct {
   private Fb2Smap fb2Smap;
   private MainGame game;


   public GameAct(MainGame game,Fb2Smap fb2Smap){
       this.fb2Smap=fb2Smap;
       this.game=game;
   }

    public Fb2Smap getBtl(){
       return fb2Smap;
    }


}
