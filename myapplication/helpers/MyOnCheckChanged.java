package com.example.yuvallehman.myapplication.helpers;

import android.content.Context;
import android.widget.CompoundButton;
import com.example.yuvallehman.myapplication.R;

public class MyOnCheckChanged implements CompoundButton.OnCheckedChangeListener {
    CheckType checkType;
    Context context;
    Runnable runnable;

    public enum CheckType {
        YesNo,
        WeekMonth
    }

    public MyOnCheckChanged(Context context2, CheckType checkType2, Runnable runnable2) {
        this.context = context2;
        this.checkType = checkType2;
        this.runnable = runnable2;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.runnable.run();
        switch (this.checkType) {
            case YesNo:
                if (isChecked) {
                    buttonView.setText(this.context.getString(R.string.yes));
                    return;
                } else {
                    buttonView.setText(this.context.getString(R.string.no));
                    return;
                }
            case WeekMonth:
                if (isChecked) {
                    buttonView.setText(this.context.getString(R.string.per_month));
                    return;
                } else {
                    buttonView.setText(this.context.getString(R.string.per_week));
                    return;
                }
            default:
                return;
        }
    }
}
