package com.zhfy.game.framework.freefont;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.PixmapPacker.Page;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/** 自由文本类1.1版本 此自由文本方案基于libgdx 1.6编写，需libgdx 1.6或以上版本支持
 * 
 * @author 贡献者:重庆-Var3D(感谢libgdx官方群友 千水流云，重庆-坏蛋，广州-Raise me up等人鼎力协助) */
public class FreeBitmapFont extends BitmapFont {
	private FreeListener listener;// 从本地端返回纹理的接口
	private int pageWidth = 512;// 单张纹理的尺寸(针对动态文本)
	private FreePaint paint = new FreePaint();// 默认画笔
	private Set<String> charSet = new HashSet<String>();// 已拥有的字符
	private PixmapPacker packer = null;// 用于将单个字符合成到大纹理的packer
	private TextureFilter minFilter = TextureFilter.Linear;
	private TextureFilter magFilter = TextureFilter.Linear;
	private BitmapFontData data;
	private int size;

	public FreeBitmapFont (FreeListener listener) {
		this(listener, new FreePaint());
	}

	public FreeBitmapFont (FreeListener listener, FreePaint paint) {
		super(new BitmapFontData(), new TextureRegion(), false);
		updataSize(paint.getTextSize());
		this.listener = listener;
		this.paint = paint;
	}

	private void updataSize (int prefSize) {
		data = getData();
		size = Math.max(prefSize, paint.getTextSize());
		data.down = -size;
		data.ascent = -size;
		data.capHeight = size;
		data.lineHeight = size;
	}

	public FreeBitmapFont setTextColor (Color color) {
		paint.setColor(color);
		return this;
	}

	public FreeBitmapFont setStrokeColor (Color color) {
		paint.setStrokeColor(color);
		return this;
	}

	public FreeBitmapFont setStrokeWidth (int width) {
		paint.setStrokeWidth(width);
		return this;
	}

	public FreeBitmapFont setSize (int size) {
		paint.setTextSize(size);
		return this;
	}

	public FreeBitmapFont setBold (boolean istrue) {
		paint.setFakeBoldText(istrue);
		return this;
	}

	public FreeBitmapFont setUnderline (boolean istrue) {
		paint.setUnderlineText(istrue);
		return this;
	}

	public FreeBitmapFont setStrikeThru (boolean istrue) {
		paint.setStrikeThruText(istrue);
		return this;
	}

	public FreeBitmapFont setPaint (FreePaint paint) {
		this.paint = paint;
		return this;
	}

	/** 设置一个字符为自定义图片 */
	public FreeBitmapFont appendEmoji (String txt, String imgname) {
		Pixmap pixmap = new Pixmap(Gdx.files.internal(imgname));
		appendEmoji(txt, pixmap, 0, 0, paint.getTextSize(), paint.getTextSize());
		return this;
	}

	public FreeBitmapFont appendEmoji (String txt, String imgname, int offsetX, int offsetY) {
		Pixmap pixmap = new Pixmap(Gdx.files.internal(imgname));
		appendEmoji(txt, pixmap, offsetX, offsetY, paint.getTextSize(), paint.getTextSize());
		return this;
	}

	public FreeBitmapFont appendEmoji (String txt, String imgname, int offsetX, int offsetY, int offsetW, int offsetH) {
		Pixmap pixmap = new Pixmap(Gdx.files.internal(imgname));
		appendEmoji(txt, pixmap, offsetX, offsetY, offsetW, offsetH);
		return this;
	}

	/** 设置一个字符为自定义图片 */
	public FreeBitmapFont appendEmoji (String txt, Pixmap pixmap, int offsetX, int offsetY, int offsetW, int offsetH) {
		// 如果不是新字符，中断创建
		if (!charSet.add(txt)) return this;
		if (packer == null) {
			packer = new PixmapPacker(pageWidth, pageWidth, Format.RGBA8888, 2, false);
		}
		Pixmap pixmap2 = new Pixmap(paint.getTextSize(), paint.getTextSize(), Format.RGBA8888);
		pixmap2.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), offsetX, offsetY, offsetW, offsetH);
		pixmap.dispose();
		pixmap = null;
		char c = txt.charAt(0);
		putGlyph(c, pixmap2);
		updataSize(paint.getTextSize());
		upData();
		return this;
	}

	/** 创建静态文本(用此方法创建的FreeBitmapFont不能再次增加字符,内存占用较少) */
	public FreeBitmapFont createText (String characters) {
		if (characters == null || characters.length() == 0) return this;
		create(characters, true);
		end();
		return this;
	}

	/** 创建或新增字符(动态创建，可动态增加字符，但内存占用更多,不能与createText()方法共存) */
	public FreeBitmapFont appendText (String characters) {
		if (characters == null || characters.length() == 0) return this;
		create(characters, false);
		return this;
	}

	private void create (String characters, boolean haveMinPageSize) {
		characters = characters.replaceAll("[\\t\\n\\x0B\\f\\r]", "");
		Array<String> cs = new Array<String>();
		char[] chars = characters.toCharArray();
		for (char c : chars) {
			String tmp = String.valueOf(c);
			boolean isNewChar = charSet.add(tmp);
			if (isNewChar) cs.add(tmp);
		}
		// 根据字符参数和字符数量计算一个最小尺寸
		if (haveMinPageSize) {
			pageWidth = (paint.getTextSize() + 2) * (int)(Math.sqrt(cs.size) + 1);
		}
		if (packer == null) {
			packer = new PixmapPacker(pageWidth, pageWidth, Format.RGBA8888, 2, false);
		}
	  
		
		// 从android，desktop平台获取纹理
		for (int i = 0, s = cs.size; i < s; i++) {
			String txt = cs.get(i);
			char c = txt.charAt(0);
			Pixmap pixmap = listener.getFontPixmap(txt, paint);
			putGlyph(c, pixmap);
		}
	 
		updataSize(size);
		upData();
		// 如果只有一张纹理，则设置使用一张纹理，提高运行速度
		if (getRegions().size == 1) {
			setOwnsTexture(true);
		} else {
			setOwnsTexture(false);
		}
	}

	private void putGlyph (char c, Pixmap pixmap) {
		Rectangle rect = packer.pack(c + "", pixmap);
		pixmap.dispose();
		int pIndex = packer.getPageIndex(c + "");
		Glyph glyph = new Glyph();
		glyph.id = c;
		glyph.page = pIndex;
		glyph.srcX = (int)rect.x;
		glyph.srcY = (int)rect.y;
		glyph.width = (int)rect.width;
		glyph.height = (int)rect.height;
		glyph.xadvance = glyph.width;
		data.setGlyph(c, glyph);
	}

	private void upData () {
		Glyph spaceGlyph = data.getGlyph(' ');
		if (spaceGlyph == null) {
			spaceGlyph = new Glyph();
			Glyph xadvanceGlyph = data.getGlyph('l');
			if (xadvanceGlyph == null) xadvanceGlyph = data.getFirstGlyph();
			spaceGlyph.xadvance = xadvanceGlyph.xadvance;
			spaceGlyph.id = (int)' ';
			data.setGlyph(' ', spaceGlyph);
		}
		data.spaceXadvance = spaceGlyph != null ? spaceGlyph.xadvance + spaceGlyph.width : 1;
		Array<Page> pages = packer.getPages();
		Array<TextureRegion> regions = getRegions();
		int pageSize = pages.size;
		for (int i = 0, regSize = regions.size - 1; i < pageSize; i++) {
			Page p = pages.get(i);
			if (i > regSize) {
				p.updateTexture(minFilter, magFilter, false);
				regions.add(new TextureRegion(p.getTexture()));
			} else if (p.updateTexture(minFilter, magFilter, false)) {
				regions.set(i, new TextureRegion(p.getTexture()));
			}
		}
		for (Glyph[] page : data.glyphs) {
			if (page == null) continue;
			for (Glyph glyph : page) {
				if (glyph == null) continue;
				TextureRegion region = getRegions().get(glyph.page);
				if (region == null) {
					throw new IllegalArgumentException("BitmapFont texture region array cannot contain null elements.");
				}
				data.setGlyphRegion(glyph, region);
			}
		}
	}

	/** 终止增加字符(如果不再需要动态增加字符，调用此方法可以节省内存) */
	public FreeBitmapFont end () {
		paint = null;
		charSet.clear();
		charSet = null;
		packer.dispose();
		packer = null;
		return this;
	}

	public void dispose () {
		end();
		super.dispose();
	}

	public int getSize () {
		return size;
	}
	
	
}
