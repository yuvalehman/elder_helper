package com.example.yuvallehman.myapplication.advanced_registration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.helpers.MyOnCheckChanged;
import com.example.yuvallehman.myapplication.helpers.MyTextWatcher;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.example.yuvallehman.myapplication.simple_java_classes.ConstantParams;

public class DeductFragment extends Fragment {
    private static final long DELAY = 1000;
    public static final String HEALTH_INSURANCE_DEDUCTION = "healthInsuranceDeduction";
    public static final String IS_ELDER_HOUSE = "isElderHouse";
    public static final String LIVING_EXPENSES_DEDUCTION = "livingExpensesDeduction";
    private static final long REGION_NOT_PICKED_DELAY = 5000;
    public static final String RENT_DEDUCTION = "rentDeduction";
    private static final String TAG = "DeductFragment";
    public static final String WELL_FARE_FROM_COMPANY = "wellFareFromCompany";
    Activity activity;
    ConstantParams constantParams;
    private Context context;
    private boolean controller;
    private Handler handler;
    private float healthInsuranceDeduction;
    EditText healthInsuranceDeductionET;
    private Runnable healthInsuranceRunnable;
    private MyTextWatcher healthInsuranceTextWatcher;
    boolean isElderHouse;
    LinearLayout isElderHouseLayout;
    Switch isElderHousePicker;
    boolean isWellFareFromCompany;
    private float livingExpensesDeduction;
    EditText livingExpensesDeductionET;
    private Runnable livingExpensesRunnable;
    private MyTextWatcher livingExpensesTextWatcher;
    /* access modifiers changed from: private */
    public String region;
    Spinner regionsSpinner;
    /* access modifiers changed from: private */
    public String[] regionsUnique;
    private float rentDeduction;
    EditText rentDeductionET;
    /* access modifiers changed from: private */
    public double rentLimit;
    private Runnable rentRunnable;
    private MyTextWatcher rentTextWatcher;
    ServerDataSupplier serverDataSupplier;
    Switch wellFareFromCompanyPicker;

    public interface DeductFragCallback {
        void deductFragCallback();
    }

    public void onPause() {
        Log.d(TAG, "onPause: ");
        this.controller = false;
        super.onPause();
    }

    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        this.controller = true;
    }

    public void onAttach(Context context2) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context2);
        this.context = context2;
        this.activity = getActivity();
        this.serverDataSupplier = ServerDataSupplier.getInstance();
        this.constantParams = this.serverDataSupplier.getConstantParams();
        this.rentTextWatcher = new MyTextWatcher(new Runnable() {
            public final void run() {
                DeductFragment.this.finishedEditingRent();
            }
        });
        this.healthInsuranceTextWatcher = new MyTextWatcher(new Runnable() {
            public final void run() {
                DeductFragment.this.finishedEditingHealthInsurance();
            }
        });
        this.livingExpensesTextWatcher = new MyTextWatcher(new Runnable() {
            public final void run() {
                DeductFragment.this.finishedEditingLivingExpenses();
            }
        });
        this.healthInsuranceRunnable = new Runnable() {
            public final void run() {
                DeductFragment.this.showHealthInsuranceError();
            }
        };
        this.livingExpensesRunnable = new Runnable() {
            public final void run() {
                DeductFragment.this.showLivingExpensesError();
            }
        };
        this.rentRunnable = new Runnable() {
            public final void run() {
                DeductFragment.this.showRentError();
            }
        };
        this.handler = new Handler();
        this.regionsUnique = this.activity.getResources().getStringArray(R.array.regions_unique);
    }

    /* access modifiers changed from: private */
    public void showRentError() {
        Log.d(TAG, "showRentError: ");
        this.rentDeductionET.setError(getString(R.string.too_high_error, Integer.valueOf((int) this.rentLimit)));
    }

    /* access modifiers changed from: private */
    public void showLivingExpensesError() {
        Log.d(TAG, "showLivingExpensesError: ");
        this.livingExpensesDeductionET.setError(getString(R.string.too_high_error, Integer.valueOf((int) this.constantParams.getLivingExpensesOthersLimit())));
    }

    /* access modifiers changed from: private */
    public void finishedEditingLivingExpenses() {
        Log.d(TAG, "finishedEditingLivingExpenses: ");
        this.handler.removeCallbacks(this.livingExpensesRunnable);
        String s = this.livingExpensesDeductionET.getText().toString();
        this.livingExpensesDeduction = s.equals("") ? 0.0f : Float.parseFloat(s);
        if (((double) this.livingExpensesDeduction) > this.constantParams.getLivingExpensesOthersLimit()) {
            this.handler.postDelayed(this.livingExpensesRunnable, DELAY);
        }
    }

    /* access modifiers changed from: private */
    public void showHealthInsuranceError() {
        Log.d(TAG, "showHealthInsuranceError: ");
        this.healthInsuranceDeductionET.setError(getString(R.string.too_high_error, Integer.valueOf((int) this.constantParams.getHealthInsuranceLimit())));
    }

    /* access modifiers changed from: private */
    public void finishedEditingHealthInsurance() {
        Log.d(TAG, "finishedEditingHealthInsurance: ");
        String s = this.healthInsuranceDeductionET.getText().toString();
        this.healthInsuranceDeduction = s.equals("") ? 0.0f : Float.parseFloat(s);
        this.handler.removeCallbacks(this.healthInsuranceRunnable);
        if (((double) this.healthInsuranceDeduction) > this.constantParams.getHealthInsuranceLimit()) {
            Log.d(TAG, "finishedEditingHealthInsurance: if");
            this.handler.postDelayed(this.healthInsuranceRunnable, DELAY);
        }
    }

    /* access modifiers changed from: private */
    public void finishedEditingRent() {
        Log.d(TAG, "finishedEditingRent: ");
        this.handler.removeCallbacks(this.rentRunnable);
        String s = this.rentDeductionET.getText().toString();
        int i = this.regionsSpinner.getSelectedItemPosition();
        this.rentDeduction = s.equals("") ? 0.0f : Float.parseFloat(s);
        if (!this.controller) {
            return;
        }
        if (this.rentDeduction == 0.0f) {
            this.regionsSpinner.setVisibility(8);
            this.isElderHouseLayout.setVisibility(8);
            return;
        }
        this.regionsSpinner.setVisibility(0);
        this.isElderHouseLayout.setVisibility(0);
        if (i != 0) {
            Log.d(TAG, "finishedEditingRent: got to here" + this.rentLimit);
            if (((double) this.rentDeduction) > this.rentLimit) {
                this.handler.postDelayed(this.rentRunnable, DELAY);
            } else {
                this.rentDeductionET.setError((CharSequence) null);
            }
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deduct, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rentDeductionET = (EditText) view.findViewById(R.id.rentDeductionA);
        this.livingExpensesDeductionET = (EditText) view.findViewById(R.id.livingExpensesDeductionA);
        this.wellFareFromCompanyPicker = (Switch) view.findViewById(R.id.wellFareFromCompanyA);
        this.isElderHousePicker = (Switch) view.findViewById(R.id.isElderHouseA);
        this.regionsSpinner = (Spinner) view.findViewById(R.id.deductRegionspinner);
        this.healthInsuranceDeductionET = (EditText) view.findViewById(R.id.healthInsuranceDeductionA);
        this.isElderHouseLayout = (LinearLayout) view.findViewById(R.id.iselderhouselinearlayout);
        this.regionsSpinner.setAdapter(ArrayAdapter.createFromResource(this.context, R.array.regions_for_display, R.layout.spinner_item));
        this.wellFareFromCompanyPicker.setOnCheckedChangeListener(new MyOnCheckChanged(this.context, MyOnCheckChanged.CheckType.YesNo, new Runnable() {
            public final void run() {
                DeductFragment.this.wellFareCheckChanged();
            }
        }));
        this.isElderHousePicker.setOnCheckedChangeListener(new MyOnCheckChanged(this.context, MyOnCheckChanged.CheckType.YesNo, new Runnable() {
            public final void run() {
                DeductFragment.this.isElderHouseCheckChanged();
            }
        }));
        this.rentDeductionET.addTextChangedListener(this.rentTextWatcher);
        this.healthInsuranceDeductionET.addTextChangedListener(this.healthInsuranceTextWatcher);
        this.livingExpensesDeductionET.addTextChangedListener(this.livingExpensesTextWatcher);
        this.regionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                DeductFragment deductFragment = DeductFragment.this;
                String unused = deductFragment.region = deductFragment.regionsUnique[position];
                Double temp = DeductFragment.this.constantParams.getLivingExpensesRentLimitMap().get(DeductFragment.this.region);
                double unused2 = DeductFragment.this.rentLimit = temp == null ? -1.0d : temp.doubleValue();
                if (DeductFragment.this.isElderHouse) {
                    DeductFragment deductFragment2 = DeductFragment.this;
                    double unused3 = deductFragment2.rentLimit = deductFragment2.rentLimit / 2.0d;
                }
                DeductFragment.this.finishedEditingRent();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void isElderHouseCheckChanged() {
        Log.d(TAG, "isElderHouseCheckChanged: ");
        this.isElderHouse = this.isElderHousePicker.isChecked();
        if (this.isElderHouse) {
            this.rentLimit /= 2.0d;
        } else {
            this.rentLimit *= 2.0d;
        }
        finishedEditingRent();
    }

    /* access modifiers changed from: private */
    public void wellFareCheckChanged() {
        Log.d(TAG, "wellFareCheckChanged: ");
        this.isWellFareFromCompany = this.wellFareFromCompanyPicker.isChecked();
    }

    public Bundle saveData() {
        Log.d(TAG, "saveData: ");
        Bundle bundle = new Bundle();
        bundle.putBoolean("isElderHouse", this.isElderHouse);
        bundle.putBoolean("wellFareFromCompany", this.isWellFareFromCompany);
        bundle.putFloat("rentDeduction", this.rentDeduction);
        bundle.putFloat("livingExpensesDeduction", this.livingExpensesDeduction);
        bundle.putFloat("healthInsuranceDeduction", this.healthInsuranceDeduction);
        return bundle;
    }
}
