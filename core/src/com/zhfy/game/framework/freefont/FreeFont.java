package com.zhfy.game.framework.freefont;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;

 class FreeFont {
	static FreeListener listener;
	static HashMap<String, FreeBitmapFont> fonts = new HashMap<String, FreeBitmapFont>();
	static public final String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F\u00A0\u00A1\u00A2\u00A3\u00A4\u00A5\u00A6\u00A7\u00A8\u00A9\u00AA\u00AB\u00AC\u00AD\u00AE\u00AF\u00B0\u00B1\u00B2\u00B3\u00B4\u00B5\u00B6\u00B7\u00B8\u00B9\u00BA\u00BB\u00BC\u00BD\u00BE\u00BF\u00C0\u00C1\u00C2\u00C3\u00C4\u00C5\u00C6\u00C7\u00C8\u00C9\u00CA\u00CB\u00CC\u00CD\u00CE\u00CF\u00D0\u00D1\u00D2\u00D3\u00D4\u00D5\u00D6\u00D7\u00D8\u00D9\u00DA\u00DB\u00DC\u00DD\u00DE\u00DF\u00E0\u00E1\u00E2\u00E3\u00E4\u00E5\u00E6\u00E7\u00E8\u00E9\u00EA\u00EB\u00EC\u00ED\u00EE\u00EF\u00F0\u00F1\u00F2\u00F3\u00F4\u00F5\u00F6\u00F7\u00F8\u00F9\u00FA\u00FB\u00FC\u00FD\u00FE\u00FF";

	public static final String DEFAULT_FONT_NAME = "freefont";

	public static void Start (FreeListener freeListener) {
		listener = freeListener;
	}

	public static Pixmap getPixmap (String txt) {
		return listener.getFontPixmap(txt, new FreePaint());
	}

	public static Pixmap getPixmap (String txt, FreePaint paint) {
		return listener.getFontPixmap(txt, paint);
	}

	public static FreeBitmapFont getBitmapFont () {
		return getBitmapFont(DEFAULT_FONT_NAME);
	}

	public static FreeBitmapFont getBitmapFont (String fontName) {
		FreeBitmapFont font = fonts.get(fontName);
		if (font == null) {
			if (listener == null) {
				throw new GdxRuntimeException("FreeListener为空，请初始化FreeFont，FreeFont.Start(...)");
			}
			font = new FreeBitmapFont(listener);
			fonts.put(fontName, font);
			return font;
		} else
			return font;
	}

	public static FreeLabel getLabel (String text) {
		return new FreeLabel(text, new LabelStyle(getBitmapFont(), Color.WHITE));
	}

	public static FreeLabel getLabel (Color color, String text) {
		return new FreeLabel(text, new LabelStyle(getBitmapFont(), color));
	}

	public static FreeLabel getLabel (String fontName, String text) {
		return new FreeLabel(text, new LabelStyle(getBitmapFont(fontName), Color.WHITE));
	}

	public static FreeLabel getLabel (String fontName, Color color, String text) {
		return new FreeLabel(text, new LabelStyle(getBitmapFont(fontName), color));
	}

	/*public static FreeLabel getLabel (BitmapFont font, Color color, String text) {
		return new FreeLabel(text, new LabelStyle(font, color));
	}*/

	/** 默认样式输入框
	 * 
	 * @param text
	 * @return */
	public static FreeTextField getTextField (String text) {
		TextFieldStyle style = new TextFieldStyle(FreeFont.getBitmapFont(), Color.BLACK, getDrawable(Color.BLUE, 2, 1),
			getDrawable(new Color(0, 0, 1, 0.3f), 1, 1), getRectLineDrawable(Color.WHITE, Color.BLACK, 40, 40));
		return new FreeTextField(text, style);
	}

	/** 纯色
	 * 
	 * @param color
	 * @param w
	 * @param h
	 * @return */
	public static Drawable getDrawable (Color color, int w, int h) {
		Pixmap pixmap = new Pixmap(w, h, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		Texture colorPoint = new Texture(pixmap);
		colorPoint.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		pixmap.dispose();
		return new TextureRegionDrawable(new TextureRegion(colorPoint));
	}

	/** 矩形线条纯色Drawable
	 * 
	 * @param color
	 * @param out
	 * @param w
	 * @param h
	 * @return */
	public static Drawable getRectLineDrawable (Color color, Color out, int w, int h) {
		Pixmap pixmap = new Pixmap(w, h, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		pixmap.setColor(out);
		pixmap.drawRectangle(0, 0, w, h);
		Texture colorPoint = new Texture(pixmap);
		colorPoint.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		pixmap.dispose();
		NinePatchDrawable nine = new NinePatchDrawable(new NinePatch(colorPoint, 2, 2, 2, 2));
		return nine;
	}

}
