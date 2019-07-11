package com.csb.loadrecyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.csb.loadrecyclerview.widget.LoadRecyclerView;

public class MainActivity extends AppCompatActivity {

    private LoadRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);

    }
}
