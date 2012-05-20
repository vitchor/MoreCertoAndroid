package com.br.morecerto.controller.utility;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationUtil {

	public static void executeAnimation(Context context, int animationRes, View view, int visibility) {
		executeAnimation(context, animationRes, view, visibility, -1);
	}

	public static void executeAnimation(Context context, int animationRes, View view, int visibility, int delay) {
		if (view != null) {
			Animation animation = AnimationUtils.loadAnimation(context, animationRes);
			if (delay != -1) {
				animation.setStartOffset(delay);
			}
			view.startAnimation(animation);
			view.setVisibility(visibility);
		}
	}

	public static void setActivitiesTransitionAnimation(Object receiver, int enterAnim, int exitAnim) {
		try {
			Method overridePendingTransition = Activity.class.getMethod("overridePendingTransition", new Class[] { Integer.TYPE, Integer.TYPE });
			if (overridePendingTransition != null) {
				overridePendingTransition.invoke(receiver, enterAnim, exitAnim);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
