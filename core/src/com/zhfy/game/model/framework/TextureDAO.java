package com.zhfy.game.model.framework;


import com.badlogic.gdx.graphics.Texture;

public class TextureDAO {
	private Texture texture;
	private String name ;
	private int refx;
	private int refy;

	public TextureDAO(Texture texture,String name,int refx,int refy){
	    this.texture=texture;
	    this.name=name;
	    this.refx=refx;
	    this.refy=refy;
    }
    public TextureDAO(){

    }
	
    public Texture getTexture() {
        return texture;
    }
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public void dispose() {
	    this.texture.dispose(); this.texture=null;
	}
    public int getRefx() {
        return refx;
    }
    public void setRefx(int refx) {
        this.refx = refx;
    }
    public int getRefy() {
        return refy;
    }
    public void setRefy(int refy) {
        this.refy = refy;
    }
	
	
}
