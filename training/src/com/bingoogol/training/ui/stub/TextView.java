package com.bingoogol.training.ui.stub;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextView extends android.widget.TextView {
	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}



	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Typeface mFace;



	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		mPaint.setTypeface(mFace);
		canvas.drawText(this.getText().toString(), 10, 200, mPaint);
	}
	
	

}
