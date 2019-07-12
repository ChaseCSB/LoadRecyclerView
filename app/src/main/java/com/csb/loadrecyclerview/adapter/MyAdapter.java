package com.csb.loadrecyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.csb.loadrecyclerview.R;

import java.util.List;


public class MyAdapter extends BaseRecycleAdapter<MyAdapter.VH,String> {


    public MyAdapter(List<String> list, Context context) {
        super(list, context);
    }

    @Override
    public VH getHolderView(ViewGroup parent, int viewType) {
        return new VH(getLayoutView(R.layout.item_data,parent));
    }

    @Override
    public void bindHolder(VH viewHolder, int position, String bean) {
        viewHolder.tvName.setText(bean);
    }

    public class VH extends RecyclerView.ViewHolder{
        TextView tvName;
        public VH(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

}
