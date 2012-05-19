package com.br.morecerto.view;

import com.br.morecerto.R;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class IdearLoadingDialog extends IdearDialog {

	private TextView mClickableCancelTextView;
	
	public IdearLoadingDialog(Context context){
		super(context, R.layout.idear_loading_dialog);
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
