package com.example.yuvallehman.myapplication.advanced_registration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.helpers.MyOnCheckChanged;

public class PastFragment extends Fragment {
    public static final String GOT_FROM_COMPANY = "gotFromCompany";
    public static final String HOLIDAYS = "pastHolidays";
    public static final String SICK_DAYS = "pastSickDays";
    public static final String TAG = "PastFragment";
    public static final String TOTAL_COMPANY_PAY = "totalCompanyPay";
    public static final String VACATION_DAYS = "pastVacationDays";
    public static final String WAS_USING_COMPANY = "wasUsingCompany";
    public static final String WELL_FARE_DAYS = "wellFareDays";
    Activity activity;
    private Context context;
    int holidays;
    EditText holidaysET;
    int sickDays;
    EditText sickET;
    float totalCompanyPay;
    EditText totalCompanyPayET;
    LinearLayout totalCompanyPayLayout;
    int vacationDays;
    EditText vacationET;
    boolean wasUsingCompany;
    Switch wasUsingCompanySwitch;
    EditText wellFareDaysET;
    int wellFareDaysPaid;

    public PastFragment() {
        Log.d(TAG, "PastFragment: constructor");
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.vacationET = (EditText) view.findViewById(R.id.vacationDaysET);
        this.sickET = (EditText) view.findViewById(R.id.sickDaysET);
        this.holidaysET = (EditText) view.findViewById(R.id.holidayET);
        this.wellFareDaysET = (EditText) view.findViewById(R.id.wellFareDaysET);
        this.totalCompanyPayET = (EditText) view.findViewById(R.id.totalCompanyPayET);
        this.wasUsingCompanySwitch = (Switch) view.findViewById(R.id.pastnurstingswitch);
        this.totalCompanyPayLayout = (LinearLayout) view.findViewById(R.id.totalCompanyPayLay);
        this.wasUsingCompanySwitch.setOnCheckedChangeListener(new MyOnCheckChanged(this.context, MyOnCheckChanged.CheckType.YesNo, new Runnable() {
            public final void run() {
                PastFragment.this.companySwitchPressed();
            }
        }));
    }

    /* access modifiers changed from: private */
    public void companySwitchPressed() {
        if (this.wasUsingCompanySwitch.isChecked()) {
            this.wasUsingCompany = true;
            this.totalCompanyPayLayout.setVisibility(0);
            return;
        }
        this.wasUsingCompany = false;
        this.totalCompanyPayLayout.setVisibility(8);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_past, container, false);
    }

    public void onAttach(Context context2) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context2);
        this.activity = getActivity();
        this.context = context2;
    }

    public Bundle saveData() {
        String s = this.vacationET.getText().toString();
        int i = 0;
        this.vacationDays = s.equals("") ? 0 : Integer.parseInt(s);
        String s2 = this.sickET.getText().toString();
        this.sickDays = s2.equals("") ? 0 : Integer.parseInt(s2);
        String s3 = this.holidaysET.getText().toString();
        this.holidays = s3.equals("") ? 0 : Integer.parseInt(s3);
        String s4 = this.wellFareDaysET.getText().toString();
        if (!s4.equals("")) {
            i = Integer.parseInt(s4);
        }
        this.wellFareDaysPaid = i;
        String s5 = this.totalCompanyPayET.getText().toString();
        this.totalCompanyPay = s5.equals("") ? 0.0f : Float.parseFloat(s5);
        this.wasUsingCompany = this.wasUsingCompanySwitch.isChecked();
        Bundle bundle = new Bundle();
        bundle.putInt(VACATION_DAYS, this.vacationDays);
        bundle.putInt(SICK_DAYS, this.sickDays);
        bundle.putInt(HOLIDAYS, this.holidays);
        bundle.putInt(WELL_FARE_DAYS, this.wellFareDaysPaid);
        bundle.putFloat(TOTAL_COMPANY_PAY, this.totalCompanyPay);
        bundle.putBoolean(WAS_USING_COMPANY, this.wasUsingCompany);
        return bundle;
    }
}
