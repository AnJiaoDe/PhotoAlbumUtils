package com.cy.cyphotoalbumutils.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cy.cyphotoalbumutils.R;


/**
 * 正方形的RelativeLayout
 * 
 * @author Administrator
 * 
 */
public class RectangleImageView extends ImageView {
	private float aspectRatio = 1; //高 / 宽 比例

	public RectangleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray arr = context.obtainStyledAttributes(attrs,
				R.styleable.SquareLayout);
		aspectRatio = arr.getFloat(R.styleable.SquareLayout_aspectRatio, 1F);
		arr.recycle();
	}

	public RectangleImageView(Context context) {
		super(context);
	}
	
	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));

		int childWidthSize = getMeasuredWidth();
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				(int) (childWidthSize * aspectRatio), MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
