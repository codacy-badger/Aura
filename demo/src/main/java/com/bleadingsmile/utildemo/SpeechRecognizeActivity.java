package com.bleadingsmile.utildemo;

import android.widget.Toast;

import com.bleadingsmile.util.activity.SpeechRecognizeActivityAbstract;

/**
 * Created by Larry Hsiao on 2016/12/18.
 */

public class SpeechRecognizeActivity extends SpeechRecognizeActivityAbstract {
    @Override
    protected void onRecognizeFinish(String keyword) {
        Toast.makeText(this, "Keyword:" + keyword, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRecognizeFailed(String message) {
        Toast.makeText(this, "Failed:" + message, Toast.LENGTH_SHORT).show();
    }
}
