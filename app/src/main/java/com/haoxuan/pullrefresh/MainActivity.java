package com.haoxuan.pullrefresh;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity{
    private LinearLayout relv;
    private FrameLayout refresh;
    private LinearLayout pullLayout;
    private ArrayAdapter adapter;
    private MyListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(MyListView)findViewById(R.id.lv);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3);
        adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3);
        adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3);
        adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3); adapter.add(3);
        lv.setAdapter(adapter);
    }
}
