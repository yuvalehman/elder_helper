package com.example.yuvallehman.myapplication.old;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.helpers.Calculations;

public class ElderFragment extends Fragment {
    public static final String IS_EMPLOYER = "employerIsPatient";
    public static final String PATIENTS_ADDRESS = "patientsAddressTV";
    public static final String PATIENTS_FIRST = "patientsFirst";
    public static final String PATIENTS_ID = "patientsIdTV";
    public static final String PATIENTS_LAST = "patientsLast";
    public static final String PATIENTS_PHONE = "patientsPhone";
    public static final String TAG = "ElderFragment";
    Activity activity;
    ArrayAdapter adapter;
    private EditText elderAddressEV;
    private EditText elderFirstEV;
    private EditText elderIdEV;
    private EditText elderLastEV;
    private EditText elderPhoneEV;
    boolean employerIsPatient = false;
    private CheckBox isElderCheckBox;
    private OnFragmentInteractionListener mListener;
    String[] regionsUnique;
    private Button saveB;
    private Spinner spinner;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String str);
    }

    public ElderFragment() {
        Log.d(TAG, "ElderFragment: constructor");
    }

    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context;
            if (getActivity() != null) {
                this.activity = getActivity();
            }
            this.adapter = ArrayAdapter.createFromResource(context, R.array.regions_for_display, R.layout.spinner_item);
            this.regionsUnique = getResources().getStringArray(R.array.regions_unique);
            return;
        }
        throw new RuntimeException(context.toString() + " must implement PaycheckFragCallback");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_elder, container, false);
    }

    public void onButtonPressed(View view) {
        String first = "";
        String name = this.elderFirstEV.getText().toString();
        if (name.length() > 0) {
            first = name.substring(0, 1).toUpperCase().concat(name.substring(1, name.length()));
        }
        String last = "";
        String name2 = this.elderLastEV.getText().toString();
        if (name2.length() > 0) {
            last = name2.substring(0, 1).toUpperCase().concat(name2.substring(1, name2.length()));
        }
        String phone = this.elderPhoneEV.getText().toString();
        String address = this.elderAddressEV.getText().toString();
        String id = this.elderIdEV.getText().toString();
        View focusView = null;
        boolean formOk = true;
        if (first.equals("")) {
            this.elderFirstEV.setError(getString(R.string.first_name_missing_massage));
            formOk = false;
            focusView = this.elderFirstEV;
        }
        if (last.equals("")) {
            this.elderLastEV.setError(getString(R.string.last_name_missing_massage));
            formOk = false;
            focusView = this.elderLastEV;
        }
        if (phone.equals("")) {
            this.elderPhoneEV.setError(getString(R.string.phone_missing_massage));
            formOk = false;
            focusView = this.elderPhoneEV;
        }
        if (address.equals("")) {
            this.elderAddressEV.setError(getString(R.string.email_missing_massage));
            formOk = false;
            focusView = this.elderAddressEV;
        }
        if (!Calculations.idVerify(id)) {
            this.elderIdEV.setError(getString(R.string.invalid_id));
            formOk = false;
            focusView = this.elderIdEV;
        }
        if (formOk) {
            OnFragmentInteractionListener onFragmentInteractionListener = this.mListener;
            if (onFragmentInteractionListener != null) {
                onFragmentInteractionListener.onFragmentInteraction(TAG);
                return;
            }
            return;
        }
        focusView.requestFocus();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.elderFirstEV = (EditText) view.findViewById(R.id.elderFirst);
        this.elderLastEV = (EditText) view.findViewById(R.id.elderLast);
        this.elderPhoneEV = (EditText) view.findViewById(R.id.elderPhone);
        this.elderIdEV = (EditText) view.findViewById(R.id.elderId);
        this.elderAddressEV = (EditText) view.findViewById(R.id.elderAddress);
        this.isElderCheckBox = (CheckBox) view.findViewById(R.id.isElder);
        this.spinner = (Spinner) view.findViewById(R.id.elderSpinner);
        this.saveB = (Button) view.findViewById(R.id.elderSaveB);
        this.saveB.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ElderFragment.this.onButtonPressed(view);
            }
        });
        this.spinner.setAdapter(this.adapter);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
        this.mListener = null;
    }
}
