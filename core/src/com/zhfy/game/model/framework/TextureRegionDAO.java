package com.zhfy.game.model.framework;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRegionDAO {
	private TextureRegion textureRegion;
	private String name ;
	//refx,refy的坐标是中心点到左下角的坐标偏移
	/* actor中算法如下,参考 MapImageActor
	Map<StringName,Integer> rs= GameMap.getDownleftPotionById(mapW,mapH_px, game.getMapScale(),id);
        this.sourceX=rs.get("sourceX")-refx;
        this.sourceY=-rs.get("sourceY")+refy;
	 */

	private int width;
	private int height;
	private int refx ;
	private int refy ;
	public TextureRegion getTextureRegion() {
		return textureRegion;
	}
	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	public void setTextureRegionAndSize(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
		this.width=this.getTextureRegion().getRegionWidth();
		this.height=this.getTextureRegion().getRegionHeight();
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
    public String getName() {
        return name;
    }
    public void setW(int w){
		width=w;
	}
    public void setH(int h){
    	height=h;
	}
	public int getH(){
		return height;
	}

	public int getW(){
		return width;
	}

    public void setName(String name) {
        this.name = name;
    }

    public TextureRegionDAO cpy(String name) {
		TextureRegionDAO t=new TextureRegionDAO();
		if(name!=null){;
			t.setName(name);
		}
		t.setRefx(this.getRefx());
		t.setRefy(this.getRefy());
		t.setTextureRegion(new TextureRegion(this.getTextureRegion()));
		t.width=t.getTextureRegion().getRegionWidth();
		t.height=t.getTextureRegion().getRegionHeight();
		return t;
    }

	public TextureRegionDAO cpyBlank() {
		TextureRegionDAO t=new TextureRegionDAO();
		t.setName(name+"_blank");
		t.setRefx(this.getRefx());
		t.setRefy(this.getRefy());
		t.setTextureRegion(new TextureRegion(new Texture(getTextureRegion().getRegionWidth(),getTextureRegion().getRegionHeight(), Pixmap.Format.RGBA8888)));
		t.width=t.getTextureRegion().getRegionWidth();
		t.height=t.getTextureRegion().getRegionHeight();
		return t;

	}

	// w,h-1时取原值
	public TextureRegion getNewRegion(int x,int y,int w,int h){
		if(w<0){
			w=getTextureRegion().getRegionWidth();
		}
		if(h<0){
			h=getTextureRegion().getRegionHeight();
		}
		TextureRegion region= new TextureRegion(getTextureRegion(),x,y,w,h);
		return region;
	}

    public TextureRegion getNewRegion(int x, int y) {
		return getNewRegion(x,y,getTextureRegion().getRegionWidth()-x,getTextureRegion().getRegionHeight()-y);
    }

}
