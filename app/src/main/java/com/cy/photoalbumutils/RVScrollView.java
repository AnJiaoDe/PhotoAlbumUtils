package com.cy.photoalbumutils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by lenovo on 2018/1/2.
 */

public class RVScrollView extends ScrollView {
    private float downX;
    private float downY;
    public RVScrollView(Context context) {
        this(context,null);
    }

    public RVScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 竖直滑动时，去拦截
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                // 竖直滑动

                if ( Math.abs(moveY - downY)>Math.abs(moveX - downX)) {


                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
