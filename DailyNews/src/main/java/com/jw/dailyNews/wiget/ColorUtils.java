package com.jw.dailyNews.wiget;

import android.content.res.ColorStateList;

/**
 * 创建时间：15/11/4
 * 更新时间：2017/11/11 0011 上午 12:46
 * 作者：Mr.jin
 * 描述：kyle
 */

public class ColorUtils {
	private static final int ENABLE_ATTR = android.R.attr.state_enabled;
	private static final int CHECKED_ATTR = android.R.attr.state_checked;
	private static final int PRESSED_ATTR = android.R.attr.state_pressed;

	public static ColorStateList generateThumbColorWithTintColor(final int tintColor) {
		int[][] states = new int[][]{
				{-ENABLE_ATTR, CHECKED_ATTR},
				{-ENABLE_ATTR},
				{PRESSED_ATTR, -CHECKED_ATTR},
				{PRESSED_ATTR, CHECKED_ATTR},
				{CHECKED_ATTR},
				{-CHECKED_ATTR}
		};

		int[] colors = new int[] {
				tintColor - 0xAA000000,
				0xFFBABABA,
				tintColor - 0x99000000,
				tintColor - 0x99000000,
				tintColor | 0xFF000000,
				0xFFEEEEEE
		};
		return new ColorStateList(states, colors);
	}

	public static ColorStateList generateBackColorWithTintColor(final int tintColor) {
		int[][] states = new int[][]{
				{-ENABLE_ATTR, CHECKED_ATTR},
				{-ENABLE_ATTR},
				{CHECKED_ATTR, PRESSED_ATTR},
				{-CHECKED_ATTR, PRESSED_ATTR},
				{CHECKED_ATTR},
				{-CHECKED_ATTR}
		};

		int[] colors = new int[] {
				tintColor - 0xE1000000,
				0x10000000,
				tintColor - 0xD0000000,
				0x20000000,
				tintColor - 0xD0000000,
				0x20000000
		};
		return new ColorStateList(states, colors);
	}
}
