package com.bleadingsmile.utildemo.demoitem;

import android.content.Context;
import android.content.Intent;

import com.bleadingsmile.utildemo.SpeechRecognizeActivity;

/**
 * Created by Larry Hsiao on 2016/12/18.
 */

class SpeechRecognize extends DemoItem {
    private Context context;

    SpeechRecognize(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Default speech recognize";
    }

    @Override
    public void triggerDemo() {
        context.startActivity(new Intent(context, SpeechRecognizeActivity.class));
    }
}
