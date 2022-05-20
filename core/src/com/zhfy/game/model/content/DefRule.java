package com.zhfy.game.model.content;

import java.util.ArrayList;
import java.util.List;


public class DefRule {
    private String id;
    private String name;
    private String type;//StringName,Long,Byte,Integer,Single
    private String remark;
    private String defaul;//由于关键字 所有换为defaul
    private int size;
    private String function;//no
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getDefaul() {
        return defaul;
    }
    public void setDefaul(String defaul) {
        this.defaul = defaul;
    }
    public String getFunction() {
        return function;
    }
    public void setFunction(String function) {
        this.function = function;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
