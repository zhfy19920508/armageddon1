package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.zhfy.game.MainGame;
import com.zhfy.game.config.ResDefaultConfig;
import com.zhfy.game.framework.GameUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileByte {
    private int bytesLength;
    private List<Integer> bytes;
    
    public void init() {
        bytes=new ArrayList<Integer>();
        bytesLength=0;
    }

    
    //4
    public final void writeInt(int v) throws IOException {
        if(bytes==null) {
            init();
        }
        
        bytes.add((v >>> 24) & 0xFF);
        bytes.add((v >>> 16) & 0xFF);
        bytes.add((v >>>  8) & 0xFF);
        bytes.add((v >>>  0) & 0xFF);
        incCount(4);
    }
    
    //2
    public final void writeShort(int v) throws IOException {
        if(bytes==null) {
            init();
        }
        bytes.add((v >>> 8) & 0xFF);
        bytes.add((v >>> 0) & 0xFF);
        incCount(2);
    }
    
    //1
    public final void writeByte(int v) throws IOException {
        if(bytes==null) {
            init();
        }
        bytes.add(v);
        incCount(1);
    }
    
    
    private void incCount(int value) {
        int temp = bytesLength + value;
        if (temp < 0) {
            temp = Integer.MAX_VALUE;
        }
        bytesLength = temp;
    }
    
    
    public byte[] getByte(){
        byte[] bLocalArr = new byte[bytesLength];
        int i,iMax;iMax=bytes.size();
        
        for ( i = 0; i<iMax; i++) {
            bLocalArr[i] = (byte) (bytes.get(i)& 0xFF);
        }
        init();
        return bLocalArr;
    }


    public void  write(int value ,int size)throws IOException {
        if(size==8){
            if(value<-1||value>100000){
                Gdx.app.error("FileByte error8",value+":"+size);
               // value=0;
                if(ResDefaultConfig.ifDebug){
                    throw new IOException("FileByte write error: "+value+":"+size);
                }else {
                    value=0;
                }
            }
            writeInt(value);
        }else if(size==4){
            if(value<-1||value>65000){
                Gdx.app.error("FileByte error4",value+":"+size);
                //value=0;
                if(ResDefaultConfig.ifDebug){
                    throw new IOException("FileByte write error: "+value+":"+size);
                }else {
                    value=0;
                }
            }
            writeShort(value);
        }else if(size==2){
            if(value<-1||value>255){
                Gdx.app.error("FileByte error2",value+":"+size);
                //value=0;
                if(ResDefaultConfig.ifDebug){
                    throw new IOException("FileByte write error: "+value+":"+size);
                }else {
                    value=0;
                }
            }
            writeByte(value);
        }else{
            Gdx.app.error("writeError",value+":"+size);
            throw new IOException("FileByte write error: "+value+":"+size);
        }
    }

}
