package com.br.morecerto.controller.utility;

import android.app.Activity;
import android.app.Dialog;

import com.br.morecerto.Commons;
import com.flurry.android.FlurryAgent;

public class IdearActivityUtil {

	private Activity mActivity;
	public static Activity sCurrentActivity;
	
	private Dialog mLoadingDialog;

	public IdearActivityUtil(Activity activity) {
		this.mActivity = activity;
	}

	public void onStart() {
		if (!Commons.DEBUG) {
			FlurryAgent.setCaptureUncaughtExceptions(false);
			FlurryAgent.onStartSession(mActivity, "KXE2MDZ6LBSZ24K13463");
			FlurryAgent.onPageView();
		}
	}

	public void onStop() {
		if (!Commons.DEBUG) {
			FlurryAgent.onEndSession(mActivity);
		}
	}

	public void showLoadingDialog(String message) {
		if (mLoadingDialog == null) {
			mLoadingDialog = DialogUtil.createLoadingDialog(mActivity, message);
			mLoadingDialog.show();
		}
	}

	public void hideLoadingDialog() {
		if (mLoadingDialog != null) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}
}