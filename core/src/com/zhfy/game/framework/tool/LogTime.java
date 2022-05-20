package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;

public class LogTime {

    private long  start;
    private long  end;
    private boolean ifEable;
    private int debugTime;

    public LogTime(){
        start=System.currentTimeMillis();
        ifEable=true;
        debugTime=0;
    }

    public void reStar(){
        if(ifEable){
            start=System.currentTimeMillis();
        }

    }



    public void log(String str){
        if(ifEable){
            end = System.currentTimeMillis();
            if(debugTime>0){
                if((end-start)>debugTime){
                    Gdx.app.log("logTime",str+":"+(end-start));
                }
            }else {
                Gdx.app.log("logTime",str+":"+(end-start));
            }

            start=System.currentTimeMillis();
        }
    }

    public void setState(boolean ifEable) {
        this.ifEable = ifEable;
    }

    public void setDebugTime(int time) {
        debugTime=time;
    }
}
