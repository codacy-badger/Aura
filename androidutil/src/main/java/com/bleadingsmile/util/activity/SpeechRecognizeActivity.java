package com.bleadingsmile.util.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.bleadingsmile.util.SSystemUtil;
import com.bleadingsmile.util.android.R;

import java.util.ArrayList;

import static android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH;
import static android.speech.RecognizerIntent.EXTRA_LANGUAGE;
import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.EXTRA_RESULTS;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
import static java.util.Locale.TRADITIONAL_CHINESE;

/**
 * Helper class for implementing speech recognize quickly.
 * Override method {@link #onRecognizeFinish(String)} to handle the recognize result
 * Created by 1500242A on 2016/10/14.
 */
public abstract class SpeechRecognizeActivity extends Activity {
    private static final int REQUEST_CODE_SPEECH_RECOGNIZE = 55123;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(FLAG_DISMISS_KEYGUARD);
        window.addFlags(FLAG_KEEP_SCREEN_ON);
        window.addFlags(FLAG_TURN_SCREEN_ON);

        if (!SSystemUtil.isNetworkAvailable(this)){
            onRecognizeFailed(getString(R.string.app_noConnection));
            finish();
            return;
        }

        try {
            Intent intent = new Intent(ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(EXTRA_LANGUAGE, TRADITIONAL_CHINESE.toString());
            startActivityForResult(intent, REQUEST_CODE_SPEECH_RECOGNIZE);
        }catch (ActivityNotFoundException e){
            finish();
            onRecognizeFailed(getString(R.string.speechRecognize_errorSpeechRecognizeNotAvailable));
        }
    }

    @Override
    protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_CODE_SPEECH_RECOGNIZE) {
            return;
        }

        if (resultCode != RESULT_OK) {
            onRecognizeFailed(getString(R.string.speechRecognizeE_errorUserCanceled));
            finish();
            return;
        }
        ArrayList<String> recognizeResults = data.getStringArrayListExtra(EXTRA_RESULTS);
        if (recognizeResults == null || recognizeResults.size() == 0) {
            onRecognizeFailed(getString(R.string.speechRecognize_errorNoMatch));
            finish();
            return;
        }

        onRecognizeFinish(recognizeResults.get(0));
        finish();
    }

    protected abstract void onRecognizeFinish(String keyword);
    protected abstract void onRecognizeFailed(String message);
}
