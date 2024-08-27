package com.example.yuvallehman.myapplication.desktop.desktop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.helpers.Calculations;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.example.yuvallehman.myapplication.simple_java_classes.Event;
import java.util.GregorianCalendar;
import java.util.Objects;

public class AddEventFragment extends AppCompatDialogFragment implements View.OnClickListener {
    public static final String POSITION = "eventTypePos";
    public static final String TAG = "AddEventFragment";
    DesktopActivity activity;
    SpinnerAdapter adapter;
    Context context;
    String eventType;
    int eventTypePos = 0;
    TextView eventTypeTV;
    EditText moneyET;
    Spinner moneyTypesSpinner;
    MyServerCallback myServerCallback;
    GregorianCalendar pickedDate;
    private ProgressBar progressBar;
    ServerDataSupplier sds;
    TextView startDateET;
    ImageView submitButton;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.context = (Context) Objects.requireNonNull(getContext());
        this.myServerCallback = (DesktopActivity) getActivity();
        this.sds = ServerDataSupplier.getInstance();
        Dialog dialog = new Dialog(this.context);
        this.pickedDate = new GregorianCalendar();
        this.activity = (DesktopActivity) getActivity();
        Bundle args = getArguments();
        if (args != null) {
            this.eventType = args.getString(ServerDataSupplier.EVENT_TYPE);
            this.eventTypePos = args.getInt(POSITION);
        }
        dialog.setContentView(R.layout.add_event_dialog);
        String eventTypeForDisplay = DesktopActivity.eventsTypeForDisplay[this.eventTypePos];
        this.adapter = ArrayAdapter.createFromResource(this.context, R.array.money_types_for_display, R.layout.spinner_item);
        this.eventTypeTV = (TextView) dialog.findViewById(R.id.addEventTypeTV);
        this.startDateET = (TextView) dialog.findViewById(R.id.addEventStartDateTV);
        this.moneyET = (EditText) dialog.findViewById(R.id.addMoneyTV);
        this.submitButton = (ImageView) dialog.findViewById(R.id.addEventButton);
        this.moneyTypesSpinner = (Spinner) dialog.findViewById(R.id.addEventSpinner);
        this.progressBar = (ProgressBar) dialog.findViewById(R.id.addEventProgressBar);
        this.moneyTypesSpinner.setAdapter(this.adapter);
        this.eventTypeTV.setText(eventTypeForDisplay);
        this.startDateET.setOnClickListener(this);
        if (!(this.pickedDate.get(1) == DesktopActivity.chosenYear && this.pickedDate.get(2) == DesktopActivity.chosenMonth)) {
            this.pickedDate.set(DesktopActivity.chosenYear, DesktopActivity.chosenMonth, 1);
        }
        this.startDateET.setText(Calculations.displayDate(this.context, this.pickedDate.getTimeInMillis()));
        this.submitButton.setOnClickListener(this);
        if (this.eventType.equals(DesktopActivity.eventTypeListUnique[2])) {
            this.moneyET.setVisibility(0);
            this.moneyTypesSpinner.setVisibility(0);
        } else if (this.eventType.equals(DesktopActivity.eventTypeListUnique[6])) {
            this.eventTypeTV.setLines(2);
            this.moneyET.setVisibility(0);
        }
        ((Window) Objects.requireNonNull(dialog.getWindow())).setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.addEventButton) {
            submit();
        } else if (id == R.id.addEventStartDateTV) {
            openDatePicker();
        }
    }

    private void openDatePicker() {
        int mYear = this.pickedDate.get(1);
        int mMonth = this.pickedDate.get(2);
        int mDay = this.pickedDate.get(5);
        if (!(mYear == DesktopActivity.chosenYear && mMonth == DesktopActivity.chosenMonth)) {
            mYear = DesktopActivity.chosenYear;
            mMonth = DesktopActivity.chosenMonth;
            mDay = 1;
        }
        new DatePickerDialog(this.context, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                AddEventFragment.lambda$openDatePicker$0(AddEventFragment.this, datePicker, i, i2, i3);
            }
        }, mYear, mMonth, mDay).show();
    }

    public static /* synthetic */ void lambda$openDatePicker$0(AddEventFragment addEventFragment, DatePicker view1, int year, int month, int dayOfMonth) {
        addEventFragment.pickedDate.set(year, month, dayOfMonth);
        addEventFragment.startDateET.setText(Calculations.displayDate(addEventFragment.context, addEventFragment.pickedDate.getTimeInMillis()));
    }

    private void submit() {
        double moneyGiven = 0.0d;
        String textMoney = this.moneyET.getText().toString();
        if (!textMoney.equals("")) {
            moneyGiven = Double.parseDouble(textMoney);
        }
        if (this.startDateET.getText().toString().equals("")) {
            this.startDateET.setError(getString(R.string.can_not_be_empty));
        } else if (this.moneyET.getVisibility() != 0 || !textMoney.equals("")) {
            String moneyType = DesktopActivity.moneyTypeListUnique[this.moneyTypesSpinner.getSelectedItemPosition()];
            this.submitButton.setVisibility(8);
            this.progressBar.setVisibility(0);
            this.sds.saveEventToServer(this.activity, new Event(this.sds.getUser().getUsername(), this.sds.getWorker().getWorkerIdNumber(), this.eventType, moneyGiven, this.pickedDate.getTimeInMillis(), new GregorianCalendar().getTimeInMillis(), moneyType, this.sds.getConstantParams().getCreatedAt(), this.sds.getPreferences().getCreatedAt()));
        } else {
            this.moneyET.setError(getString(R.string.can_not_be_empty));
        }
    }
}
