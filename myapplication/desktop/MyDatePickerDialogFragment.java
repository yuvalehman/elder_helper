package com.example.yuvallehman.myapplication.desktop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.yuvallehman.myapplication.R;
import java.util.Objects;

public class MyDatePickerDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {
    private static final String TAG = "MyDatePickerDialogFragm";
    private DesktopActivity activity;
    private TextView display;
    private String[] months;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.activity = (DesktopActivity) getActivity();
        Dialog dialog = new Dialog((Context) Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.date_picker);
        this.months = this.activity.monthList;
        ((ImageView) dialog.findViewById(R.id.datePickerArrowDown)).setOnClickListener(this);
        ((ImageView) dialog.findViewById(R.id.datePickerArrowUp)).setOnClickListener(this);
        ((ImageView) dialog.findViewById(R.id.datePickerDone)).setOnClickListener(this);
        this.display = (TextView) dialog.findViewById(R.id.date_picker_tv);
        updateDisplay();
        ((Window) Objects.requireNonNull(dialog.getWindow())).setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }

    private void updateDisplay() {
        this.display.setText(getString(R.string.date_picker_display, this.months[DesktopActivity.chosenMonth], Integer.valueOf(DesktopActivity.chosenYear)));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.datePickerArrowDown /*2131296384*/:
                DesktopActivity.chosenMonth--;
                if (DesktopActivity.chosenMonth == -1) {
                    DesktopActivity.chosenYear--;
                    DesktopActivity.chosenMonth = 11;
                    break;
                }
                break;
            case R.id.datePickerArrowUp /*2131296385*/:
                DesktopActivity.chosenMonth++;
                if (DesktopActivity.chosenMonth == 12) {
                    DesktopActivity.chosenYear++;
                    DesktopActivity.chosenMonth = 0;
                    break;
                }
                break;
            case R.id.datePickerDone /*2131296387*/:
                this.activity.updateDisplay();
                dismiss();
                break;
        }
        Log.d(TAG, "onClick: " + DesktopActivity.chosenMonth + "__" + DesktopActivity.chosenYear);
        updateDisplay();
    }
}
