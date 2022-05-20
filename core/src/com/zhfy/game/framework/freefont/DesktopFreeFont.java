package com.zhfy.game.framework.freefont;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class DesktopFreeFont implements FreeListener {
	public static void Start () {
		FreeFont.Start(new DesktopFreeFont());
	}

	public Pixmap getFontPixmap (String txt, FreePaint vpaint) {
		//Pixmap.setFilter(Filter.BiLinear);
		BufferedImage bi = getFontBufferedImage(txt, vpaint);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "png", buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pixmap pixmap = new Pixmap(buffer.toByteArray(), 0, buffer.toByteArray().length);
		pixmap.setFilter(Filter.BiLinear);
		return pixmap;
	}

	private BufferedImage getFontBufferedImage (String txt, FreePaint vpaint) {
		Font font = getFont(vpaint.getTextSize(), vpaint.getFakeBoldText() || vpaint.getStrokeColor() != null);
		FontMetrics fm = metrics.get(vpaint.getTextSize());
		int strWidth = fm.stringWidth(txt);
		int strHeight = fm.getAscent() + fm.getDescent();
		if (strWidth == 0) {
			strWidth = strHeight = vpaint.getTextSize();
		}
		BufferedImage bi = new BufferedImage(strWidth, strHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(font);
		if (vpaint.getStrokeColor() != null) {
			// 描边
			GlyphVector v = font.createGlyphVector(fm.getFontRenderContext(), txt);
			Shape shape = v.getOutline();
			g.setColor(getColor(vpaint.getColor()));
			g.translate(0, fm.getAscent());
			g.fill(shape);
			g.setStroke(new BasicStroke(vpaint.getStrokeWidth()));
			g.setColor(getColor(vpaint.getStrokeColor()));
			g.draw(shape);
		} else if (vpaint.getUnderlineText() == true) {
			// 下划线
			AttributedString as = new AttributedString(txt);
			as.addAttribute(TextAttribute.FONT, font);
			as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			g.setColor(getColor(vpaint.getColor()));
			g.drawString(as.getIterator(), 0, fm.getAscent());
		} else if (vpaint.getStrikeThruText() == true) {
			// 删除线
			AttributedString as = new AttributedString(txt);
			as.addAttribute(TextAttribute.FONT, font);
			as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			g.setColor(getColor(vpaint.getColor()));
			g.drawString(as.getIterator(), 0, fm.getAscent());
		} else {
			// 普通
			g.setColor(getColor(vpaint.getColor()));
			g.drawString(txt, 0, fm.getAscent());
		}
		return bi;
	}

	private HashMap<Integer, Font> fonts = new HashMap<Integer, Font>();
	private HashMap<Integer, FontMetrics> metrics = new HashMap<Integer, FontMetrics>();

	private Font getFont (int defaultFontSize, boolean isBolo) {
		Font font = fonts.get(defaultFontSize);
		if (font == null) {
			font = new Font("", isBolo ? Font.BOLD : Font.PLAIN, defaultFontSize);
			fonts.put(defaultFontSize, font);
			BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bi.createGraphics();
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			metrics.put(defaultFontSize, fm);
		}
		return font;
	}

	private java.awt.Color getColor (Color libColor) {
		return new java.awt.Color(libColor.r, libColor.g, libColor.b, libColor.a);
	}


	public ArrayMap<String, Pixmap> getBatchFontPixmap (Array<String> cs, FreePaint vpaint) {
		ArrayMap<String, Pixmap> resultArrayMap = new ArrayMap<String, Pixmap>();
		StringBuffer stringBuffer = new StringBuffer();
		int size = cs.size;
		for (String charString : cs) {
			stringBuffer.append(charString);
		}

		ArrayList<FreeCharInfo> charInfos = new ArrayList<FreeCharInfo>();
		BufferedImage bi = getFontBufferedImageForBatch(charInfos, stringBuffer.toString(), vpaint);
		
		// 分割
		//Pixmap.setFilter(Filter.BiLinear);
		for (int i = 0; i < size; i++) {
			FreeCharInfo charInfo = charInfos.get(i);
			BufferedImage smallBufferedImage = bi.getSubimage(charInfo.x, charInfo.y, charInfo.w, charInfo.h);
			// 单个字符的图片信息
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			try {
				ImageIO.write(smallBufferedImage, "png", buffer);
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			}
			Pixmap pixmap = new Pixmap(buffer.toByteArray(), 0, buffer.toByteArray().length);
			pixmap.setFilter(Filter.BiLinear);
			resultArrayMap.put(charInfo.charString, pixmap);
		}
		
		return resultArrayMap;
	}

	/** 描边，下划线，删除线等可以由Label实现
	 * @param txt
	 * @param vpaint
	 * @return */
	private BufferedImage getFontBufferedImageForBatch (ArrayList<FreeCharInfo> charInfos, String txt, FreePaint vpaint) {
		Font font = getFont(vpaint.getTextSize(), vpaint.getFakeBoldText() || vpaint.getStrokeColor() != null);
		FontMetrics fm = metrics.get(vpaint.getTextSize());
		// 字符串宽
		int strWidth = fm.stringWidth(txt);
		// 字符串高
		int strHeight = fm.getAscent() + fm.getDescent();
		if (strWidth == 0) {
			strWidth = strHeight = vpaint.getTextSize();
		}
		BufferedImage bi = new BufferedImage(strWidth, strHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(font);

		// 普通
		g.setColor(getColor(vpaint.getColor()));
		char[] chars = txt.toCharArray();
		int csize = chars.length;
		int px = 0;
		for (int i = 0; i < csize; i++) {
			String charsString = String.valueOf(chars[i]);
			FreeCharInfo charInfo = new FreeCharInfo();
			charInfo.charString = charsString;
			int cw = fm.stringWidth(charsString);
			int ch = fm.getHeight();
			charInfo.w = cw;
			charInfo.h = ch;
			charInfo.x = px;
			charInfos.add(charInfo);
			g.drawString(charsString, px, fm.getAscent());

			px += cw;
		}

		return bi;
	}
}
