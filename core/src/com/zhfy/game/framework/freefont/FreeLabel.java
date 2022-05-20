package com.zhfy.game.framework.freefont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class FreeLabel extends Label {
	private boolean isStroke = false;// 是否描边
	private Color strokeColor;
	private float strokeWidth;

	public FreeLabel (CharSequence text, LabelStyle style) {
		super(append(text, style), style);
		setSize(getPrefWidth(), getPrefHeight());
	}

	private static CharSequence append (CharSequence text, LabelStyle style) {
		((FreeBitmapFont)style.font).appendText("" + text);
		return text;
	}

	public void setText (CharSequence newText) {
		super.setText(append(newText, getStyle()));
	}

	/** 设置字体缩放 */
	public void setFontScale (float fontScale) {
		super.setFontScale(fontScale);
	}

	/** 设置加粗(参数0-1为宜) */
	public void setBold (float width) {
		setStroke(getColor().cpy(), width);
	}

	/** 设置描边(参数0-2为宜) */
	public void setStroke (Color strokeColor, float strokeWidth) {
		this.strokeColor = strokeColor;
		this.strokeWidth = strokeWidth;
		isStroke = true;
	}

	private float dxs[] = {1, 1, -1, -1, 1, -1, 0, 0};
	private float dys[] = {1, -1, 1, -1, 0, 0, 1, -1};

	public void draw (Batch batch, float parentAlpha) {
		if (isStroke) {
			validate();
			for (int i = 0; i < dxs.length; i++) {
				getBitmapFontCache().tint(strokeColor);
				getBitmapFontCache().setPosition(getX() + dxs[i] * strokeWidth, getY() + dys[i] * strokeWidth + strokeWidth);
				getBitmapFontCache().draw(batch);
			}
			getBitmapFontCache().tint(getColor());
			getBitmapFontCache().setPosition(getX(), getY() + strokeWidth);
			getBitmapFontCache().draw(batch);
		} else {
			super.draw(batch, parentAlpha);
		}
	}

	public int getFontSize () {
		return ((FreeBitmapFont)getStyle().font).getSize();
	}

	/**
	 * 真实文本的高度
	 * @return
	 */
	public float getTextHeight () {
		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(getStyle().font, getText(), Color.WHITE, getWidth(), Align.topLeft, true);
		return glyphLayout.height;
	}

	/**
	 * 真实文本的宽度
	 * @return
	 */
	public float getTextWidth () {
		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(getStyle().font, getText(), Color.WHITE, getWidth(), Align.topLeft, false);
		return glyphLayout.width;
	}
}
