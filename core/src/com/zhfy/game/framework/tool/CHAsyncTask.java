package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.GameUtil;

public abstract class CHAsyncTask implements AsyncTask<String> {
    private CHAsyncManager asyncManager;
    // 处理结果
    private AsyncResult<String> depsFuture = null;

    // 处理OK?
    volatile boolean asyncDone = false;

    private float timeMax;//毫秒 0标示不限制事件
    private float timeSum;
    private String content;

    public String getContent(){
        return content;
    }

    public CHAsyncTask(String content,float timeMax) {
        if(timeMax!=-1){
            this.timeMax=timeMax;
        }
        timeSum=0;
        this.content=content;
    }

    public CHAsyncTask(CHAsyncManager manager) {
        asyncManager = manager;
        timeSum=0;
        timeMax=0;
    }

    @Override
    public String call() throws Exception {

        try {
            // 执行任务
            String result = doInBackground();
            asyncDone = true;
            return result;
    } catch (Exception e) {
            if (ResDefaultConfig.ifDebug) {
                e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
            }
        }
        return null;
    }

    /**开始执行*/
    public abstract void onPreExecute();

    /**执行结束返回结果*/
    public abstract void onPostExecute(String result);

    /**异步执行*/
    public abstract String doInBackground();

    public Boolean ifTimeOut(){
        if(timeMax>0&&timeSum>timeMax){
            return true;
        }
        return false;
    }


    //
    public boolean update(MainGame game) {

        if(timeMax>0){
            timeSum+=Gdx.graphics.getDeltaTime();
        }
       if(ifTimeOut()){
            Gdx.app.error("任务执行已超时 "+timeSum+":"+timeMax,getContent());
            depsFuture = asyncManager.getExecutor().submit(this);
            asyncDone=true;
        }else if (!asyncDone) {
           //没有执行完成
           if (depsFuture == null) {
               // 也没有提交任务，进行提交
               try {
                   onPreExecute();
                   depsFuture = asyncManager.getExecutor().submit(this);
               } catch (Exception e) {
                   if (ResDefaultConfig.ifDebug) {
                       e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                   } else if (!game.gameConfig.getIfIgnoreBug()) {
                       game.remindBugFeedBack();
                   }
                   game.recordLog("CHAsyncTask update1 ", e);
               }
           }
       } else {
           if (depsFuture.isDone()) {
               // 通过结果发现执行完毕了
               try {
                   onPostExecute(depsFuture.get());
               } catch (Exception e) {
                   if (ResDefaultConfig.ifDebug) {
                       e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                   } else if ( !game.gameConfig.getIfIgnoreBug()) {
                       game.remindBugFeedBack();
                   }
                   game.recordLog("CHAsyncTask update2 ", e);
               }

           }
       }/*if (!asyncDone) {
            //没有执行完成
            if (depsFuture == null) {
                // 也没有提交任务，进行提交
                try {
                    onPreExecute();
                    depsFuture = asyncManager.getExecutor().submit(this);
                } catch (Exception e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                    }else if(!game.gameConfig.getIfIgnoreBug()){
                        game.remindBugFeedBack();
                    }
                    game.recordLog("CHAsyncTask update1 ",e);
                }
            }else {
                if (depsFuture!=null&&depsFuture.isDone()) {
                    // 通过结果发现执行完毕了
                    try {
                        onPostExecute(depsFuture.get());
                    } catch (Exception e) {
                        if (ResDefaultConfig.ifDebug) {
                            e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                        } else if (!game.gameConfig.getIfIgnoreBug()) {
                            game.remindBugFeedBack();
                        }
                        game.recordLog("CHAsyncTask update2 ", e);
                    }
                    asyncDone = true;
                }else{
                    try {
                        onPreExecute();
                        depsFuture = asyncManager.getExecutor().submit(this);
                    } catch (Exception e) {
                        if(ResDefaultConfig.ifDebug){
                            e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                        }else if(!game.gameConfig.getIfIgnoreBug()){
                            game.remindBugFeedBack();
                        }
                        game.recordLog("CHAsyncTask update3 ",e);
                    }
                }
            }
        } else {
           if (depsFuture!=null&&depsFuture.isDone()) {
                // 通过结果发现执行完毕了
                try {
                    onPostExecute(depsFuture.get());
                } catch (Exception e) {
                    if(ResDefaultConfig.ifDebug){
                        e.printStackTrace(); // throw new GdxRuntimeException("depsFuture.get() failed!!!!");
                    }else if(!game.gameConfig.getIfIgnoreBug()){
                        game.remindBugFeedBack();
                    }
                    game.recordLog("CHAsyncTask update4 ",e);
                }
            }
        }*/
        return asyncDone;
    }

    public void setAsyncManager(CHAsyncManager asyncManager) {
        this.asyncManager = asyncManager;
    }

    public CHAsyncManager getAsyncManager() {
        return asyncManager;
    }

    /**
     *
     * <pre>
     * 是否执行完毕
     *
     * date: 2015-1-18
     * </pre>
     * @author
     * @return
     */
    public boolean isDone() {
        return asyncDone;
    }

    public  String getTimeInfo(){
        return  timeSum+"/"+timeMax;
    }
}