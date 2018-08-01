package com.cy.cyphotoalbumutils.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by cy on 2017/7/2.
 */

public class GridRecyclerView extends RecyclerView {
    private Context context;
    public GridRecyclerView(Context context) {
        this(context, null);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        setOverScrollMode(OVER_SCROLL_NEVER);

    }
    public void setAdapter(Adapter adapter, int spanCount,int orientation) {
        LayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        setLayoutManager(layoutManager);
        setAdapter(adapter);

    }
    public void setAdapterAddHead( Adapter adapter, int spanCount,int orientation){
        setAdapter(adapter);

        //初始化布局管理器
        final GridLayoutManager lm = new GridLayoutManager(context,spanCount,orientation,false);

        /*
        *设置SpanSizeLookup，它将决定view会横跨多少列。这个方法是为RecyclerView添加Header和Footer的关键。
        *当判断position指向的View为Header或者Footer时候，返回总列数（ lm.getSpanCount()）,即可让其独占一行。
        */

        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position==0? lm.getSpanCount():1;
            }
        });

        //设置布局管理器
        setLayoutManager(lm);
    }
    public void setAdapterHeadFoot(final Adapter adapter, int spanCount, int orientation){
        setAdapter(adapter);

        //初始化布局管理器
        final GridLayoutManager lm = new GridLayoutManager(context,spanCount,orientation,false);

        /*
        *设置SpanSizeLookup，它将决定view会横跨多少列。这个方法是为RecyclerView添加Header和Footer的关键。
        *当判断position指向的View为Header或者Footer时候，返回总列数（ lm.getSpanCount()）,即可让其独占一行。
        */

        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position==0||position==adapter.getItemCount()-1){
                    return lm.getSpanCount();
                }
                return 1;

            }
        });

        //设置布局管理器
        setLayoutManager(lm);
    }

    public void setAdapter(Adapter adapter, int spanCount,int orientation,OnRVScrollListener onRVScrollListener){
        setAdapter(adapter,spanCount,orientation);

        setOnScrollListener(onRVScrollListener);
    }
    public void setAdapterAddHead(Adapter adapter, int spanCount,int orientation,OnRVScrollListener onRVScrollListener){
        setAdapterAddHead(adapter,spanCount,orientation);
        setOnScrollListener(onRVScrollListener);

    }

}
