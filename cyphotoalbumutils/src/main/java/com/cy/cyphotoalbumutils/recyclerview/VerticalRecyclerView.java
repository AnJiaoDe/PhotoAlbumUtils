package com.cy.cyphotoalbumutils.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by cy on 2017/7/2.
 */

public class VerticalRecyclerView extends RecyclerView {
    public VerticalRecyclerView(Context context) {
        this(context, null);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);
    }

    public void setAdapter(Adapter adapter, OnRVScrollListener onRVScrollListener){
        setAdapter(adapter);

        setOnScrollListener(onRVScrollListener);
    }

}
