package com.bleadingsmile.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by 1500242A on 2016/4/28.
 */
public class SDumpScreen extends FrameLayout {
    private ArrayAdapter<String> mAdapter;
    private ListView             mListView;

    public SDumpScreen(Context context) {
        super(context);
        initialize(context);
    }

    public SDumpScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SDumpScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SDumpScreen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(final Context context) {
        mListView =new ListView(context);
        mAdapter  =new ArrayAdapter<String>(context, R.layout.item_log_line){
            @SuppressLint("SetTextI18n")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View itemView;
                if (convertView == null){
                    itemView = LayoutInflater.from(context).inflate(R.layout.item_log_line,parent,false);
                }else{
                    itemView = convertView;
                }
                TextView itemText =((TextView)itemView);
                itemText.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                itemText.setText(" " + (position + 1) + " " + mAdapter.getItem(position));
                itemText.setTextColor(getResources().getColor(android.R.color.black));
                if (0 == (position % 2)){
                    itemText.setBackgroundColor(0x00000000);
                }else{
                    itemText.setBackgroundColor(0xFFCDCFC7);
                }
                return itemView;
            }
        };
        mListView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mListView.setAdapter(mAdapter);
        addView(mListView);

        Button clearButton = new Button(context);
        clearButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.BOTTOM|Gravity.END));
        clearButton.setText("清除");
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        addView(clearButton);
    }

    public void info(String message) {
        mAdapter.add(message);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mAdapter.getCount()-1);
    }

    public void clear(){
        mAdapter.clear();
    }
}
