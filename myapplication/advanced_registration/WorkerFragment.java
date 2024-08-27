package com.example.yuvallehman.myapplication.advanced_registration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.helpers.Calculations;
import com.example.yuvallehman.myapplication.interfaces.MyTypes;
import java.util.GregorianCalendar;

public class WorkerFragment extends Fragment {
    public static final String DATE_OF_HIRE = "dateOfHire";
    public static final String TAG = "WorkerFragment";
    public static final String WORKER_COUNTRY = "workerCountry";
    public static final String WORKER_EMAIL = "workerEmail";
    public static final String WORKER_FIRST_NAME = "workerFirstName";
    public static final String WORKER_ID = "workerID";
    public static final String WORKER_LAST_NAME = "workerLastName";
    PayRegActivity activity;
    GregorianCalendar calendar;
    Context context;
    private TextView dateOfHireTV;
    public boolean userInsertedDate = false;
    private EditText workerCountryTV;
    private TextView workerEmailTV;
    private TextView workerFirstNameTV;
    private TextView workerLastNameTV;

    public WorkerFragment() {
        Log.d(TAG, "WorkerFragment: constructor");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_worker, container, false);
    }

    public Bundle saveData() {
        Log.d(TAG, "saveData: ");
        String workerFirstName = this.workerFirstNameTV.getText().toString();
        String workerLastName = this.workerLastNameTV.getText().toString();
        String workerEmail = this.workerEmailTV.getText().toString();
        String workerCountry = this.workerCountryTV.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(WORKER_FIRST_NAME, workerFirstName);
        bundle.putString(WORKER_LAST_NAME, workerLastName);
        bundle.putString(WORKER_EMAIL, workerEmail);
        bundle.putString(WORKER_COUNTRY, workerCountry);
        bundle.putLong(DATE_OF_HIRE, PayRegActivity.dateOfHire);
        return bundle;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        this.workerFirstNameTV = (TextView) view.findViewById(R.id.workerfirstname);
        this.workerLastNameTV = (TextView) view.findViewById(R.id.workerlastname);
        this.workerEmailTV = (TextView) view.findViewById(R.id.workeremail);
        this.workerCountryTV = (EditText) view.findViewById(R.id.workercontry);
        this.dateOfHireTV = (TextView) view.findViewById(R.id.workerdatestarthire);
        this.dateOfHireTV.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                WorkerFragment.this.openDatePicker(view);
            }
        });
        this.calendar = new GregorianCalendar();
        this.dateOfHireTV.setText(Calculations.displayDate(this.context, PayRegActivity.dateOfHire));
    }

    /* access modifiers changed from: private */
    public void openDatePicker(View v) {
        Log.d(TAG, "openDatePicker: ");
        this.userInsertedDate = true;
        if (PayRegActivity.dateOfHire != -1) {
            this.calendar.setTimeInMillis(PayRegActivity.dateOfHire);
        }
        new DatePickerDialog(this.context, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                WorkerFragment.lambda$openDatePicker$0(WorkerFragment.this, datePicker, i, i2, i3);
            }
        }, this.calendar.get(1), this.calendar.get(2), this.calendar.get(5)).show();
    }

    public static /* synthetic */ void lambda$openDatePicker$0(WorkerFragment workerFragment, DatePicker datePicker, int year, int month, int dayOfMonth) {
        workerFragment.calendar.set(year, month, dayOfMonth);
        PayRegActivity.dateOfHire = workerFragment.calendar.getTimeInMillis();
        workerFragment.dateOfHireTV.setText(Calculations.displayDate(workerFragment.context, PayRegActivity.dateOfHire));
    }

    public void onAttach(Context context2) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context2);
        this.context = context2;
        this.activity = (PayRegActivity) getActivity();
    }

    public void showAlertDialog() {
        Log.d(TAG, "showAlertDialog: ");
        new AlertDialog.Builder(this.context).setTitle("Date Of Hire").setMessage("Are you sure the start of hire date is the current date?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                WorkerFragment.this.activity.callingNextFromFrag(MyTypes.DATE_OF_HIRE);
            }
        }).setNegativeButton(R.string.no, (DialogInterface.OnClickListener) null).create().show();
    }
}
