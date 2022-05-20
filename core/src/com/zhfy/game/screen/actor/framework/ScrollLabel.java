package com.zhfy.game.screen.actor.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

/**
 *
 *
 * 可以滚动的文本
 *
 * @author haocao
 *
 */
public class ScrollLabel extends BaseActor {

    private Label label;
    // 滚动偏移量，左下角为(0,0)
    private float scrollY;
    private boolean ifCenter;
    public TextureRegion background;
    private  float textHeight;
    private int align;

    public void setTextHeight(String text){
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(label.getStyle().font, text, Color.WHITE, getWidth(), Align.topLeft, true);
        textHeight= glyphLayout.height*label.getFontScaleY();
    }
    public void resetTextHeight(){
       // GlyphLayout glyphLayout = new GlyphLayout();
       // glyphLayout.setText(label.getStyle().font, label.getText(), Color.WHITE, label.getWidth(),align, true);
      //  float s1=glyphLayout.height;//1137.1995
       // float s2=label.getStyle().font.getCapHeight();//14
       // float s3=label.getStyle().font.getXHeight();//11
       // textHeight=s1*label.getFontScaleY()* (s2/s3);;
        //textHeight= glyphLayout.height*label.getFontScaleY()* (label.getStyle().font.getCapHeight()/label.getStyle().font.getXHeight());
        resetScrollYPotion();
        label.setAlignment(align);
        textHeight=label.getPrefHeight();
        if(textHeight<getHeight()&&ifCenter){
            label.setAlignment(Align.center,label.getLineAlign());
        }
    }
    public ScrollLabel(TextureRegion background, BitmapFont font, Color textColor, String text,Boolean ifCenter) {
        this.background = background;
        label = new Label(text, new Label.LabelStyle(font, Color.WHITE));
        label.setWrap(true);
        align=Align.topLeft;
        label.setAlignment(Align.topLeft);
        this.ifCenter=ifCenter;
        setText(text);
        setTextColor(textColor);
        //GlyphLayout glyphLayout = new GlyphLayout();
       // glyphLayout.setText(label.getStyle().font, label.getText(), Color.WHITE, getWidth(), Align.topLeft, true);
        //textHeight= glyphLayout.height*label.getFontScaleY();
        resetTextHeight();
        addListener(new ActorGestureListener() {
            @Override
            public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
                scrollY(deltaY);
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });
    }





    public void drawAfterBg (Batch batch) {

        //背景
        if (background != null) {
            //scrollLabelStyle.background.draw(batch, getX(), getY(), getWidth(), getHeight());
            batch.draw(background,getX(), getY(), getWidth(), getHeight());
        }

        // 设置裁剪区域
        if (getClipRectangleArr().size == 0) {
            addClipRectangle(new Rectangle(0, 0, getWidth(), getHeight()));
        }

        label.setPosition(getX(), getY() + scrollY);
        label.setWidth(getWidth() );
        label.setHeight(getHeight());
        label.draw(batch, 1);

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * 0.8f);

    }

    public void setText (String text) {
        label.setText(text);
    }

    public void setTextColor (Color textColor) {
        label.setColor(textColor);
    }

    public void scrollY (float value) {
        float temp = textHeight- getHeight();
        if (temp <= 0) {
            return;
        }

        scrollY += value;

        // 边界检测
        if (scrollY > temp) {
            scrollY = temp;
        }
        if (scrollY < 0) {
            scrollY = 0;
        }
       // Gdx.app.log("scrollY",scrollY+"");
    }

    public Label getLabel () {
        return label;
    }

    public void setFontScale(float scale) {
        label.setFontScale(scale);
    }

    public void setWidth (float width) {
        if (getWidth() != width) {
            super.setWidth(width);
            sizeChanged();
            label.setWidth(width);
            //label.pack();
        }
    }

    public void setHeight (float height) {
        if (getHeight() != height) {
            super.setHeight(height);
            sizeChanged();
        }
    }


    public void resetScrollYPotion(){
        scrollY=0;
    }

    public void setPosition (float x, float y) {
        if (getX()!= x || getY() != y) {
           setX(x);
           setY(y);
            positionChanged();
            label.setPosition(x,y);
        }
    }

    public void setDebug(Boolean b){
        super.setDebug(b);
        label.setDebug(b);
    }

    @Override
    public boolean isVisible() {
        if(super.isVisible()&&getLabel().isVisible()){
            return true;
        }
        return false;
    }

    public void setWrap(boolean b) {
        label.setWrap(b);
    }

    public void setAlignment(int bottomLeft, int left) {
        label.setAlignment(bottomLeft,left);
        this.align=label.getLabelAlign();
    }

    public void setAlignment(String align) {
        if(align.equals("bottomLeft")){
            label.setAlignment(Align.bottomLeft);
        }else if(align.equals("bottom")){
            label.setAlignment(Align.bottom);
        }else if(align.equals("center")){
            //label.setAlignment(Align.center);
            label.setAlignment(Align.top,Align.center);
        }else if(align.equals("bottomLeft")){
            label.setAlignment(Align.bottomLeft);
        }else if(align.equals("right")){
            label.setAlignment(Align.right);
        }else if(align.equals("bottomRight")){
            label.setAlignment(Align.bottomRight);
        }else if(align.equals("left")){
            label.setAlignment(Align.left);
        }else if(align.equals("top")){
            label.setAlignment(Align.top);
        }else if(align.equals("topLeft")){
            label.setAlignment(Align.topLeft);
        }else if(align.equals("topRight")){
            label.setAlignment(Align.topRight);
        }/*else if(align.equals("topCenter")){
            label.setAlignment(Align.top,Align.center);
        }*/
        this.align=label.getLabelAlign();
    }

    private Array<Rectangle> clipRectangleArr = new Array<Rectangle>();

    public Array<Rectangle> getClipRectangleArr () {
        return clipRectangleArr;
    }
    /**
     * 添加裁剪矩形，范围为当前Actor的显示区域即：(0,0)~(w,h) 原点为左下角
     * @param rectangle
     */
    public void addClipRectangle (Rectangle rectangle) {
        clipRectangleArr.add(rectangle);
    }



    @Override
    public void draw (Batch batch, float parentAlpha) {
        if (clipRectangleArr.size > 0) {
            for (Rectangle cRectangle : clipRectangleArr) {
                this.drawCore(batch, parentAlpha, cRectangle);
            }
        } else {
            this.drawCore(batch, parentAlpha, null);
        }
    }


    /**
     * 核心绘制
     * @param batch
     * @param parentAlpha
     * @param clipRectangle
     */
    private void drawCore (Batch batch, float parentAlpha, Rectangle clipRectangle) {
        boolean clipok = false;
        // 开始裁剪
        if (clipRectangle != null) {
            batch.flush(); // 绘制之前添加的元素，如果不添加此处代码，后面的裁剪会导致之前的纹理也会被裁剪
            clipok = clipBegin(getX() + clipRectangle.x, getY() + clipRectangle.y, clipRectangle.width, clipRectangle.height);
        }

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);

        float x = getX();
        float y = getY();
        float scaleX = getScaleX();
        float scaleY = getScaleY();

        float width = getWidth();
        float height = getHeight();


        // 绘制完背景后进行其他内容绘制
        drawAfterBg(batch);

        // 提交裁剪内容
        if (clipok) {
            batch.flush();
            clipEnd();
        }
    }
}