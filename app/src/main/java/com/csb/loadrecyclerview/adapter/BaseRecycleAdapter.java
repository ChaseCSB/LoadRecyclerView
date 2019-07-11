package com.csb.loadrecyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.csb.loadrecyclerview.R;

import java.util.List;

/**
 * effect:
 */
public abstract class BaseRecycleAdapter<T extends RecyclerView.ViewHolder,E> extends RecyclerView.Adapter{
    private List<E> list;
    private Context context;
    private View footView;
    private View emptyView;
    private final int TYPE_EMPTY = 10;
    private final int TYPE_FOOT = 11;
    private final int TYPE_NOR = 12;
    public static final int STATE_LOADING = 1001;
    public static final int STATE_FINISH = 1002;
    public static final int STATE_ERROR = 1003;
    public static final int STATE_NOMORE = 1004;
    private int loadState = STATE_LOADING;

    public BaseRecycleAdapter(List<E> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY){
            if (emptyView == null) {
                emptyView = LayoutInflater.from(context).inflate(R.layout.emptyview,parent,false);
            }
            return new EmptyHolder(emptyView);
        }else if(viewType == TYPE_FOOT){
            if (footView == null){
                footView = LayoutInflater.from(context).inflate(R.layout.footview_layout,parent,false);
            }
            return new FootViewHolder(footView);
        }
        return getHolderView(parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (list.size() <= 0){
            Log.e("http","emptyView");
            return;
        }
        if (position == list.size()){
            changeState((FootViewHolder) holder,loadState,context);
            Log.e("http","footView");
            return;
        }
        bindHolder((T) holder,position,list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null){
            return 0;
        }
        return list.size() + 1;
    }

    @Override
    public final int getItemViewType(int position) {
        if (list == null){
            return TYPE_EMPTY;
        }
        if (list.size() == 0){
            return TYPE_EMPTY;
        }
        if (list.size() == position){
            return TYPE_FOOT;
        }

        return getViewType(position);
    }
    public boolean isCanLoad(){
        return loadState == STATE_LOADING;
    }
    protected int getViewType(int position){
        return TYPE_NOR;
    }
    public class EmptyHolder extends RecyclerView.ViewHolder{

        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }
    public class FootViewHolder extends RecyclerView.ViewHolder{
        TextView tvLoading;
        ImageView ivLoad;
        public FootViewHolder(View itemView) {
            super(itemView);
            tvLoading = itemView.findViewById(R.id.tvLoading);
            ivLoad = itemView.findViewById(R.id.ivLoad);
        }
    }

    /**
     *
     * @param holder
     * @param state
     * @param context
     */
    public void changeState(FootViewHolder holder, int state, Context context){
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.progressrotate);
        switch (state){
            case STATE_LOADING:
                holder.tvLoading.setText("正在加载");
                holder.tvLoading.setVisibility(View.VISIBLE);
                holder.ivLoad.setVisibility(View.VISIBLE);
                holder.ivLoad.startAnimation(animation);
                return;
            case STATE_FINISH:
                holder.itemView.setVisibility(View.INVISIBLE);

                break;
            case STATE_NOMORE:
                holder.tvLoading.setVisibility(View.VISIBLE);
                holder.tvLoading.setText("没有更多数据啦");
                holder.ivLoad.setVisibility(View.INVISIBLE);
                break;
            case STATE_ERROR:
                holder.ivLoad.setVisibility(View.INVISIBLE);
                holder.tvLoading.setVisibility(View.VISIBLE);
                holder.tvLoading.setText("加载出错");
                break;
                default:
                    throw new RuntimeException("this state is error,Please check whether your method(setLoadState) has passed the illegal parameters ");
        }
        if (holder.ivLoad.getAnimation() != null){
            holder.ivLoad.getAnimation().cancel();
        }

    }


    /**
     * change footview state
     * @param state only STATE_LOADING STATE_FINISH STATE_ERROR STATE_NOMORE
     */
    public void setLoadState(int state){
        this.loadState = state;
    }
    public abstract T getHolderView(ViewGroup parent, int viewType);
    public abstract void bindHolder(T viewHolder,int position,E bean);
    protected View getLayoutView(int layout){
        return LayoutInflater.from(context).inflate(layout,null,false);
    }
    protected View getLayoutView(int layout, ViewGroup parent){
        return LayoutInflater.from(context).inflate(layout,parent,false);
    }
}
