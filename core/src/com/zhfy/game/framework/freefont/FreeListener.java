package com.zhfy.game.framework.freefont;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

/** freeFont接口
 * 
 * @author Var3D */
public interface FreeListener {
	// 返回一个字符纹理
	public Pixmap getFontPixmap(String txt, FreePaint vpaint);

	// 批量返回字符纹理
	public ArrayMap<String, Pixmap> getBatchFontPixmap(Array<String> cs, FreePaint vpaint);
}
