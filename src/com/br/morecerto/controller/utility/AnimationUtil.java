package com.br.morecerto.controller.utility;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.br.morecerto.R;

public class AnimationUtil {

	public static void fadeIn(Context context, View view) {
		if (view != null && (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE)) {
			view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
			view.setVisibility(View.VISIBLE);
		}
	}

	public static void fadeOut(Context context, View view, int visibility) {
		if (view != null && view.getVisibility() == View.VISIBLE) {
			view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
			view.setVisibility(visibility);
		}
	}

	public static void executeAnimation(Context context, View view, int visibility, int delay) {
		if (view != null && view.getVisibility() == View.VISIBLE) {
			Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
			animation.setStartOffset(delay);
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
