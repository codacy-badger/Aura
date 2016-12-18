package com.bleadingsmile.utildemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by larryhsiao on 2016/12/14.
 */
public class EntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayAdapter<DemoItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.add(new OneShotPermissionGranter(this));

        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DemoItem demoItem = adapter.getItem(position);
                if (demoItem != null) {
                    demoItem.triggerDemo();
                }
            }
        });
        setContentView(listView);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}