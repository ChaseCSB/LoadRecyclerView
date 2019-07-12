package com.csb.loadrecyclerview.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.csb.loadrecyclerview.adapter.BaseRecycleAdapter;


public class LoadRecyclerView extends RecyclerView {

    private OnLoadListener onLoadListener;

    private boolean isBaseAdapter = false;

    public LoadRecyclerView(Context context) {
        super(context);
    }

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnLoadListener(OnLoadListener onLoadListener){
        this.onLoadListener = onLoadListener;
        addOnScrollListener(onScrollListener);
    }

    OnScrollListener onScrollListener = new OnScrollListener() {

        private int lastVisibleItemPosition;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LayoutManager layoutManager = getLayoutManager();
            int itemCount = layoutManager.getItemCount();
            int childCount = layoutManager.getChildCount();

            if (newState == SCROLL_STATE_IDLE && itemCount > 0 && lastVisibleItemPosition >= childCount - 1){
                if (isBaseAdapter && ((BaseRecycleAdapter)getAdapter()).isCanLoad()){
                    onLoadListener.onLoad();
                }else if (!isBaseAdapter){
                    onLoadListener.onLoad();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager){
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }else if (layoutManager instanceof GridLayoutManager){
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }else if (layoutManager instanceof StaggeredGridLayoutManager){
                StaggeredGridLayoutManager staggeredManager = (StaggeredGridLayoutManager) layoutManager;
                int spanCount = staggeredManager.getSpanCount();
                if (spanCount <= 0){
                    return;
                }
                int [] lastPositions = new int[spanCount];
                staggeredManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = lastPositions[0];
                for (int i :lastPositions) {
                    if (i > lastVisibleItemPosition){
                        lastVisibleItemPosition = i;
                    }
                }
            }
        }
    };


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }
    public<T extends BaseRecycleAdapter> void setBaseAdapter(T adapter){
        isBaseAdapter = true;
        setAdapter(adapter);
    }

    public interface OnLoadListener{
        void onLoad();
    }
}
