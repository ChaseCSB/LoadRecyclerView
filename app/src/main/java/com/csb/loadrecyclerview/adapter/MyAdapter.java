package com.csb.loadrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyAdapter extends BaseRecycleAdapter<MyAdapter.VH,String> {


    public MyAdapter(List<String> list, Context context) {
        super(list, context);
    }

    @Override
    public VH getHolderView(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void bindHolder(VH viewHolder, int position, String bean) {

    }

    public class VH extends RecyclerView.ViewHolder{

        public VH(View itemView) {
            super(itemView);
        }
    }

}
