package com.br.morecerto.controller.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.Toast;

import com.br.morecerto.view.UberLoadingDialog;
import com.br.morecerto.view.UberQuestionDialog;

public class DialogUtil {
	
	public static Dialog createLoadingDialog(Context context, String message) {
		final UberLoadingDialog loadingDialog = new UberLoadingDialog(context);
		loadingDialog.setMessage(message);
		return loadingDialog;
	}

	public static void showAlert(Context context, String message) {
		final UberQuestionDialog dialog = new UberQuestionDialog(context);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	public static void showAlert(Context context, String title, String message) {
		showAlert(context, title, message, null);
	}

	public static void showAlert(Context context, String title, String message, Bitmap image) {
		final UberQuestionDialog dialog = new UberQuestionDialog(context, image);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	public static void showToast(Context context, CharSequence text, int duration) {
		final Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}