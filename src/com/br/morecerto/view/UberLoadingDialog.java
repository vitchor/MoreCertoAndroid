package com.br.morecerto.view;

import com.br.morecerto.R;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class UberLoadingDialog extends UberDialog {

	private TextView mClickableCancelTextView;
	
	public UberLoadingDialog(Context context){
		super(context, R.layout.uber_loading_dialog);
		//mClickableCancelTextView = (TextView) findViewById(R.id.cancel_text);
		mClickableCancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cancel();
			}			
		});
	}
	
	public void setCancelButtonEnabled(boolean enabled) {
		mClickableCancelTextView.setVisibility(enabled ? View.VISIBLE : View.GONE);
	}
	
}
