package com.coupon.go.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.coupon.go.R;


public class TextViewRegular extends TextView {
	private static final String TAG = "TextViewRegular";

	public TextViewRegular(Context context) {
		super(context);
        setCustomFont();
	}

	public TextViewRegular(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont();
	}

	public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont();
	}

	private void setCustomFont() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.american_typewriter));
            setTypeface(tf);
        }
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
        tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }
}
