package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.ComUtil;
import com.zhfy.game.model.content.conversion.Fb2Smap;

import java.lang.reflect.Field;
import java.util.Stack;

public class CHAsyncManager implements Disposable {
    MainGame game;
    // 任务堆栈
    private final Stack<CHAsyncTask> tasks = new Stack<CHAsyncTask>();

    private AsyncExecutor executor;
    private int taskMax;
   // private ObjectMap<String, CHAsyncTask> checks;

    public CHAsyncManager(MainGame game) {
        // 池中允许的最大线程数1。超过最大上限会等待前面的执行完才接着执行。
        executor = new AsyncExecutor(1);
        this.game=game;
        //checks=new ObjectMap<>();
    }

    public boolean update() {
        if (tasks.size() == 0){
            taskMax=0;
            //checks.clear();
            return true;
        }
      //  Gdx.app.log("CHAsyncTask",tasks.size()+":"+tasks.get(0).getContent());
        return updateTask() && tasks.size() == 0;
    }

    private boolean updateTask() {


        try {
            //任务更新情况
            CHAsyncTask task = tasks.peek();

            if (task.update(game)) {
               /* if(!ComUtil.isEmpty(task.getContent())){
                    checks.remove(task.getContent());
                }*/
                // 处理完毕了，移除掉
                tasks.pop();
                return true;
            }
        } catch (Exception e) {
           if(ResDefaultConfig.ifDebug){
               e.printStackTrace();
           }else{
               game.recordLog("MainGame render ",e);
               CHAsyncTask task = tasks.peek();
               if (task.update(game)) {
                  /* if (!ComUtil.isEmpty(task.getContent())) {
                       checks.remove(task.getContent());
                   }*/
                   // 处理完毕了，移除掉
                   tasks.pop();
                   return true;
               }
           }
        }

        return false;
    }

    /**
     *预先加载任务
     */
    public void loadTask(CHAsyncTask task) {
        if (task.getAsyncManager() == null) {
            task.setAsyncManager(this);
        }
      /*  if(!ComUtil.isEmpty(task.getContent())){
            if(checks.containsKey(task.getContent())){//清理之前的任务
                CHAsyncTask ot=checks.remove(task.getContent());
                if(ot!=null){
                    tasks.remove(ot);
                }
            }
            checks.put(task.getContent(),task);
        }*/
        tasks.push(task);
        taskMax=tasks.size();
        Gdx.app.log("loadTask:"+task.getContent(),taskMax+"");
    }

    @Override
    public void dispose() {
        executor.dispose();
    }

    public AsyncExecutor getExecutor() {
        return executor;
    }


    public float getProgress(){
        if(taskMax==0){
            return 1f;
        }else {
            return   1-((tasks.size()*1f)/taskMax);
        }
    }

    public int getTaskSize(){
        return tasks.size();
    }

    public void clearTask(){
        //checks.clear();
        taskMax=0;
        tasks.clear();
    }

    public void logTask() {
        for(CHAsyncTask task:tasks){
            Gdx.app.log("task:",task.getContent()+":"+task.getTimeInfo());
        }
    }
}