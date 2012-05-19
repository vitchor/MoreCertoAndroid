package com.br.morecerto.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

public class IdearDialog extends Dialog {

	private TextView mMessageTextView;

	public IdearDialog(Context context, int layout) {
		super(context);
		final Window window = getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(layout);
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		// TODO create color 19/05
		//window.setBackgroundDrawableResource(R.color.dialog_bg);
		//mMessageTextView = (TextView) findViewById(R.id.message);
	}
	
	public void setMessage(String message) {
		mMessageTextView.setVisibility(View.VISIBLE);
		mMessageTextView.setText(message);
	}

}
