package com.br.morecerto.view;

import com.br.morecerto.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class IdearQuestionDialog extends IdearDialog {

	private TextView mTitleTextView;
	private ImageView mIconImageView;
	private Button mNegativeButton;
	private Button mPositiveButton;

	public IdearQuestionDialog(Context context) {
		this(context, null);
	}

	public IdearQuestionDialog(Context context, Bitmap image) {
		super(context, R.layout.idear_question_dialog);
		mNegativeButton = (Button) findViewById(R.id.negativeButton);
		mPositiveButton = (Button) findViewById(R.id.positiveButton);
		mTitleTextView = (TextView) findViewById(R.id.title);
		if (image != null) {
			mIconImageView = (ImageView) findViewById(R.id.iconView);
			mIconImageView.setVisibility(View.VISIBLE);
			mIconImageView.setImageBitmap(image);
		}
	}

	public void setTitle(String title) {
		mTitleTextView.setVisibility(View.VISIBLE);
		mTitleTextView.setText(title);
	}

	public void setButton(int kind, String text, DialogInterface.OnClickListener listener) {
		final DialogInterface.OnClickListener dialogListener = listener;
		if (kind == Dialog.BUTTON_POSITIVE) {
			mPositiveButton.setVisibility(View.VISIBLE);
			mPositiveButton.setText(text);
			mPositiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialogListener.onClick(IdearQuestionDialog.this, Dialog.BUTTON_POSITIVE);
				}
			});
		} else if (kind == Dialog.BUTTON_NEGATIVE) {
			mNegativeButton.setVisibility(View.VISIBLE);
			mNegativeButton.setText(text);
			mNegativeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialogListener.onClick(IdearQuestionDialog.this, Dialog.BUTTON_NEGATIVE);
				}
			});
		}
	}
}