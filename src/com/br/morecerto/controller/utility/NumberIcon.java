package com.br.morecerto.controller.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class NumberIcon {

	public static Drawable getIcon(Context context, int number) {
		final int resID = context.getResources().getIdentifier("number_" + number, "drawable", context.getPackageName());
		return context.getResources().getDrawable(resID);

	}
}
