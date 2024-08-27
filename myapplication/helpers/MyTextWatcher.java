package com.example.yuvallehman.myapplication.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class MyTextWatcher implements TextWatcher {
    private static final String TAG = "MyTextWatcher";
    private Runnable runnable;

    public MyTextWatcher(Runnable runnable2) {
        Log.d(TAG, "MyTextWatcher: constructor");
        this.runnable = runnable2;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        this.runnable.run();
    }
}
