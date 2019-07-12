package com.csb.loadrecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.csb.loadrecyclerview.adapter.BaseRecycleAdapter;
import com.csb.loadrecyclerview.adapter.MyAdapter;
import com.csb.loadrecyclerview.widget.LoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LoadRecyclerView recyclerView;
    List<String>mList = new ArrayList<>();
    private MyAdapter mAdapter;
    private SwipeRefreshLayout srlRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void setListener() {
        recyclerView.setOnLoadListener(new LoadRecyclerView.OnLoadListener() {
            @Override
            public void onLoad() {
                //如果是setAdapter()则需要判断是否可以加载  如果是setBaseAdapter()则不需要自己判断  只要在加载数据完成时修改状态
                getData();
            }
        });
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @SuppressLint("WrongConstant")
    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        srlRefresh = findViewById(R.id.srlRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter = new MyAdapter(mList,this);
        //
        recyclerView.setBaseAdapter(mAdapter);
        srlRefresh.setRefreshing(true);
        getData();
    }

    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String>list = new ArrayList<>(10);
                for (int i = 0;i < 10;i++){
                    list.add(i+"");
                }
                SystemClock.sleep(2000);
                Message message = Message.obtain();
                message.what = 1;
                message.obj = list;
                handler.sendMessage(message);

            }
        }).start();

    }


    private MyHandler handler = new MyHandler(new Handler.Callback(){

        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (isFinishing()){
                return false;
            }
            mList.addAll((List<String>) message.obj);
            mAdapter.notifyDataSetChanged();
            srlRefresh.setRefreshing(false);
            if (mList.size() > 40){
                mAdapter.setLoadState(BaseRecycleAdapter.STATE_NOMORE);
            }
            return false;
        }
    });

    public static class MyHandler extends Handler{

        public MyHandler(Callback callback) {
            super(callback);
        }
    }
}
