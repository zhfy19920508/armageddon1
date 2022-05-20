package com.zhfy.game.model.framework;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
public class PixmapDAO {
	private Pixmap  pixmap ;
	private String name ;
	private int refx ;
	private int refy ;

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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Pixmap getPixmap() {
        return pixmap;
    }
    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }
    
    public void dispose() {
        pixmap.dispose();
		pixmap=null;
    }
}
