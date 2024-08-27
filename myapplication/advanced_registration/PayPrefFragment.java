package com.example.yuvallehman.myapplication.advanced_registration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.helpers.MyOnCheckChanged;
import com.example.yuvallehman.myapplication.helpers.MyTextWatcher;
import com.example.yuvallehman.myapplication.interfaces.MyTypes;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.example.yuvallehman.myapplication.simple_java_classes.ConstantParams;

public class PayPrefFragment extends Fragment {
    private static final long ALERT_DELAY = 1000;
    public static final String HEALTH_INSURANCE_DEDUCTION = "healthInsuranceDeduction";
    public static final String IS_ELDER_HOUSE = "isElderHouse";
    public static final String LIVING_EXPENSES_DEDUCTION = "livingExpensesDeduction";
    public static final String NUTRITION_PAY = "nutritionPay";
    public static final String NUTRITION_WEEK_OR_MONTH = "separateEconomicWeekOrMonth";
    public static final String POCKET_MONEY = "allowance";
    public static final String POCKET_MONEY_PART_OF_PAY = "pocketMoneyPartOfPay";
    public static final String POCKET_MONEY_WEEK_OR_MONTH = "pocketMoneyWeekOrMonth";
    public static final String RENT_DEDUCTION = "rentDeduction";
    public static final String SATURDAY_BONUS = "saturdayBonus";
    private static final long SATURDAY_DELAY = 1000;
    public static final String TAG = "PayPrefFragment";
    public static final String TOTAL_PAY = "totalPay";
    public static final String WELL_FARE_FROM_COMPANY = "wellFareFromCompany";
    public static final String WORK_7_DAYS = "work7days";
    private Runnable alertMassageRunnable;
    private boolean comingFromEditing = false;
    private boolean comingFromRestore = false;
    private ConstantParams constantParams;
    private Context context;
    /* access modifiers changed from: private */
    public int dayOfPay = 7;
    LinearLayout dayOfPayLayout;
    private LinearLayout estimatePayLayout;
    private float estimatedFinalPay = 0.0f;
    private TextView estimatedSalaryErrorTV;
    private TextView estimatedSalaryTV;
    private boolean finalSalaryTooLow = true;
    private Handler handler;
    private boolean isPocketMoneyPartOfPay;
    private boolean isWork7days;
    private PrefFragCallback mListener;
    private int minimumSaturdayBonus;
    private float nutritionPay = 0.0f;
    private EditText nutritionPayET;
    private Switch nutritionPayPicker;
    private MyOnCheckChanged nutritionPayPickerCheck;
    private MyTextWatcher nutritionTextWatcher;
    private int nutritionWeekOrMonth = 101;
    private float pocketMoney = 0.0f;
    private EditText pocketMoneyET;
    private boolean pocketMoneyIsZero = true;
    private LinearLayout pocketMoneyPartOfPayLayout;
    private Switch pocketMoneyPartOfPayPicker;
    private MyOnCheckChanged pocketMoneyPartOfPayPickerCheck;
    private MyTextWatcher pocketMoneyTextWatcher;
    private int pocketMoneyWeekOrMonth;
    private Switch pocketMoneyWeekOrMonthPicker;
    private MyOnCheckChanged pocketMoneyWeekOrMonthPickerCheck;
    private float saturdayBonus = 0.0f;
    private EditText saturdayBonusET;
    private MyTextWatcher saturdayBonusTextWatcher;
    private Runnable saturdayRunnable;
    private float totalPay = 0.0f;
    private EditText totalPayET;
    private MyTextWatcher totalPayTextWatcher;
    private Switch work7daysPicker;
    private MyOnCheckChanged work7daysPickerCheck;

    public interface PrefFragCallback {
        void callingNextFromFrag(MyTypes myTypes);

        void onClick(View view);
    }

    /* access modifiers changed from: package-private */
    public boolean isPocketMoneyIsZero() {
        return this.pocketMoneyIsZero;
    }

    /* access modifiers changed from: package-private */
    public boolean isFinalSalaryTooLow() {
        return this.finalSalaryTooLow;
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        this.comingFromRestore = false;
    }

    public void onPause() {
        this.comingFromRestore = true;
        super.onPause();
    }

    public PayPrefFragment() {
        Log.d(TAG, "PayPrefFragment: constructor");
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewStateRestored: ");
        super.onViewStateRestored(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.pocketMoneyPartOfPayLayout = (LinearLayout) view.findViewById(R.id.pocketmoneypartofpaylinearlay);
        this.saturdayBonusET = (EditText) view.findViewById(R.id.saturdayBonusA);
        this.totalPayET = (EditText) view.findViewById(R.id.totalPayA);
        this.pocketMoneyET = (EditText) view.findViewById(R.id.pocketMoneyA);
        this.pocketMoneyWeekOrMonthPicker = (Switch) view.findViewById(R.id.pocketMoneyPicker);
        this.work7daysPicker = (Switch) view.findViewById(R.id.work7daysA);
        this.pocketMoneyPartOfPayPicker = (Switch) view.findViewById(R.id.pocketMoneyPartOfPayA);
        this.nutritionPayPicker = (Switch) view.findViewById(R.id.separateEconomicPayPickerA);
        this.nutritionPayET = (EditText) view.findViewById(R.id.separateEconomicPayA);
        this.estimatedSalaryTV = (TextView) view.findViewById(R.id.estimatedSalaryTV);
        this.estimatePayLayout = (LinearLayout) view.findViewById(R.id.estimatedfinalpaylinearlay);
        this.estimatedSalaryErrorTV = (TextView) view.findViewById(R.id.estimateerrormassageTV);
        this.dayOfPayLayout = (LinearLayout) view.findViewById(R.id.dayofpaylayout);
        this.totalPayET.addTextChangedListener(this.totalPayTextWatcher);
        this.pocketMoneyET.addTextChangedListener(this.pocketMoneyTextWatcher);
        this.nutritionPayET.addTextChangedListener(this.nutritionTextWatcher);
        this.saturdayBonusET.addTextChangedListener(this.saturdayBonusTextWatcher);
        this.nutritionPayPicker.setOnCheckedChangeListener(this.nutritionPayPickerCheck);
        this.work7daysPicker.setOnCheckedChangeListener(this.work7daysPickerCheck);
        this.pocketMoneyPartOfPayPicker.setOnCheckedChangeListener(this.pocketMoneyPartOfPayPickerCheck);
        this.pocketMoneyWeekOrMonthPicker.setOnCheckedChangeListener(this.pocketMoneyWeekOrMonthPickerCheck);
        ((Spinner) view.findViewById(R.id.dayofpayspinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int unused = PayPrefFragment.this.dayOfPay = position + 1;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.estimatedSalaryTV.setText(String.valueOf(this.estimatedFinalPay));
        String pocketS = this.pocketMoneyET.getText().toString();
        if (pocketS.equals("") || (!pocketS.equals("") && Integer.parseInt(pocketS) == 0)) {
            this.pocketMoneyPartOfPayLayout.setVisibility(8);
        } else {
            this.pocketMoneyPartOfPayLayout.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void pocketMoneyPartOfPayCheckChanged() {
        Log.d(TAG, "pocketMoneyPartOfPayCheckChanged: ");
        this.isPocketMoneyPartOfPay = this.pocketMoneyPartOfPayPicker.isChecked();
        calAndShowFinalSalary();
    }

    /* access modifiers changed from: private */
    public void work7dayCheckChanged() {
        Log.d(TAG, "work7dayCheckChanged: ");
        this.isWork7days = this.work7daysPicker.isChecked();
    }

    /* access modifiers changed from: private */
    public void nutritionCheckChanged() {
        Log.d(TAG, "nutritionCheckChanged: ");
        if (this.nutritionPayPicker.isChecked()) {
            this.nutritionWeekOrMonth = 102;
        } else {
            this.nutritionWeekOrMonth = 101;
        }
    }

    /* access modifiers changed from: private */
    public void pocketMoneyWeekCheckChanged() {
        Log.d(TAG, "pocketMoneyWeekCheckChanged: ");
        if (this.pocketMoneyWeekOrMonthPicker.isChecked()) {
            this.pocketMoneyWeekOrMonth = 102;
            this.dayOfPayLayout.setVisibility(8);
        } else {
            this.pocketMoneyWeekOrMonth = 101;
            this.dayOfPayLayout.setVisibility(0);
        }
        calAndShowFinalSalary();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_pay_pref, container, false);
    }

    /* access modifiers changed from: package-private */
    public Bundle saveData() {
        Log.d(TAG, "saveData: ");
        Bundle bundle = new Bundle();
        bundle.putInt(POCKET_MONEY_WEEK_OR_MONTH, this.pocketMoneyWeekOrMonth);
        bundle.putInt(NUTRITION_WEEK_OR_MONTH, this.nutritionWeekOrMonth);
        bundle.putBoolean(WORK_7_DAYS, this.isWork7days);
        bundle.putBoolean(POCKET_MONEY_PART_OF_PAY, this.isPocketMoneyPartOfPay);
        bundle.putFloat(SATURDAY_BONUS, this.saturdayBonus);
        bundle.putFloat(TOTAL_PAY, this.totalPay);
        bundle.putFloat(POCKET_MONEY, this.pocketMoney);
        bundle.putFloat(NUTRITION_PAY, this.nutritionPay);
        bundle.putInt(ServerDataSupplier.DAY_OF_PAY, this.dayOfPay);
        return bundle;
    }

    public void onAttach(@NonNull Context context2) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context2);
        this.constantParams = ServerDataSupplier.getInstance().getConstantParams();
        this.minimumSaturdayBonus = (int) Math.round(this.constantParams.getSaturdayOrHolidayBonus() * this.constantParams.getDailyMinPay());
        this.context = context2;
        this.mListener = (PrefFragCallback) context2;
        this.totalPayTextWatcher = new MyTextWatcher(new Runnable() {
            public final void run() {
                PayPrefFragment.this.finishEditTotalPay();
            }
        });
        this.pocketMoneyTextWatcher = new MyTextWatcher(new Runnable() {
            public final void run() {
                PayPrefFragment.this.finishEditPocketMoney();
            }
        });
        this.nutritionTextWatcher = new MyTextWatcher(new Runnable() {
            public final void run() {
                PayPrefFragment.this.finishEditingNutrition();
            }
        });
        this.saturdayBonusTextWatcher = new MyTextWatcher(new Runnable() {
            public final void run() {
                PayPrefFragment.this.finishEditingSaturday();
            }
        });
        this.nutritionPayPickerCheck = new MyOnCheckChanged(context2, MyOnCheckChanged.CheckType.WeekMonth, new Runnable() {
            public final void run() {
                PayPrefFragment.this.nutritionCheckChanged();
            }
        });
        this.work7daysPickerCheck = new MyOnCheckChanged(context2, MyOnCheckChanged.CheckType.YesNo, new Runnable() {
            public final void run() {
                PayPrefFragment.this.work7dayCheckChanged();
            }
        });
        this.pocketMoneyPartOfPayPickerCheck = new MyOnCheckChanged(context2, MyOnCheckChanged.CheckType.YesNo, new Runnable() {
            public final void run() {
                PayPrefFragment.this.pocketMoneyPartOfPayCheckChanged();
            }
        });
        this.pocketMoneyWeekOrMonthPickerCheck = new MyOnCheckChanged(context2, MyOnCheckChanged.CheckType.WeekMonth, new Runnable() {
            public final void run() {
                PayPrefFragment.this.pocketMoneyWeekCheckChanged();
            }
        });
        this.alertMassageRunnable = new Runnable() {
            public final void run() {
                PayPrefFragment.this.showPocketMoneyError();
            }
        };
        this.saturdayRunnable = new Runnable() {
            public final void run() {
                PayPrefFragment.this.showSaturdayAlert();
            }
        };
        this.handler = new Handler();
    }

    /* access modifiers changed from: private */
    public void showPocketMoneyError() {
        Log.d(TAG, "showPocketMoneyError: ");
        this.pocketMoneyET.setError(getString(R.string.pocket_money_alert_massage));
    }

    /* access modifiers changed from: private */
    public void showSaturdayAlert() {
        Log.d(TAG, "showSaturdayAlert: ");
        EditText editText = this.saturdayBonusET;
        editText.setError("Too low, minimum value should be " + this.minimumSaturdayBonus);
    }

    /* access modifiers changed from: private */
    public void finishEditingSaturday() {
        Log.d(TAG, "finishEditingSaturday: ");
        if (!this.comingFromRestore) {
            String s = this.saturdayBonusET.getText().toString();
            this.saturdayBonus = s.equals("") ? 0.0f : (float) Integer.parseInt(s);
            this.handler.removeCallbacks(this.saturdayRunnable);
            if (this.saturdayBonus < ((float) this.minimumSaturdayBonus)) {
                this.handler.postDelayed(this.saturdayRunnable, 1000);
            } else {
                this.saturdayBonusET.setError((CharSequence) null);
            }
        }
    }

    /* access modifiers changed from: private */
    public void finishEditPocketMoney() {
        if (!this.comingFromRestore) {
            this.pocketMoneyIsZero = false;
            this.handler.removeCallbacks(this.alertMassageRunnable);
            String s = this.pocketMoneyET.getText().toString();
            this.pocketMoney = s.equals("") ? 0.0f : (float) Integer.parseInt(s);
            if (this.pocketMoney == 0.0f) {
                Log.d(TAG, "finishEditPocketMoney: if1");
                this.pocketMoneyPartOfPayLayout.setVisibility(8);
                this.dayOfPayLayout.setVisibility(8);
                if (!this.comingFromRestore) {
                    Log.d(TAG, "finishEditPocketMoney: if2");
                    this.comingFromEditing = true;
                    this.pocketMoneyIsZero = true;
                    this.handler.postDelayed(this.alertMassageRunnable, 1000);
                }
            } else {
                Log.d(TAG, "finishEditPocketMoney: else");
                this.pocketMoneyPartOfPayLayout.setVisibility(0);
                if (this.pocketMoneyWeekOrMonth == 101) {
                    this.dayOfPayLayout.setVisibility(0);
                }
            }
            finishEditTotalPay();
        }
    }

    /* access modifiers changed from: private */
    public void finishEditingNutrition() {
        Log.d(TAG, "finishEditingNutrition: ");
        String s = this.nutritionPayET.getText().toString();
        this.nutritionPay = s.equals("") ? 0.0f : (float) Integer.parseInt(s);
    }

    /* access modifiers changed from: private */
    public void finishEditTotalPay() {
        if (!this.comingFromRestore) {
            calAndShowFinalSalary();
            String s = this.totalPayET.getText().toString();
            this.totalPay = s.equals("") ? 0.0f : (float) Integer.parseInt(s);
        }
    }

    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
        this.mListener = null;
    }

    private void calAndShowFinalSalary() {
        float monthlyPocketMoney;
        String pocketMoneyText = this.pocketMoneyET.getText().toString();
        this.pocketMoney = pocketMoneyText.equals("") ? 0.0f : (float) Integer.parseInt(pocketMoneyText);
        this.pocketMoneyWeekOrMonth = this.pocketMoneyWeekOrMonthPicker.isChecked() ? 102 : 101;
        String totalPayText = this.totalPayET.getText().toString();
        this.totalPay = totalPayText.equals("") ? 0.0f : (float) Integer.parseInt(totalPayText);
        if (this.isPocketMoneyPartOfPay) {
            this.estimatedFinalPay = this.totalPay;
        } else {
            if (this.pocketMoneyWeekOrMonth == 102) {
                monthlyPocketMoney = this.pocketMoney;
            } else {
                monthlyPocketMoney = this.pocketMoney * 4.0f;
            }
            this.estimatedFinalPay = this.totalPay + monthlyPocketMoney;
        }
        this.estimatedSalaryTV.setText(String.valueOf(this.estimatedFinalPay));
        this.estimatePayLayout.setVisibility(0);
        if (this.estimatedFinalPay < ((float) this.constantParams.getMinimumSalary())) {
            this.estimatedSalaryTV.setTextColor(SupportMenu.CATEGORY_MASK);
            this.estimatedSalaryErrorTV.setTextColor(SupportMenu.CATEGORY_MASK);
            this.finalSalaryTooLow = true;
            if (this.totalPay < 4900.0f) {
                this.estimatedSalaryErrorTV.setText(getString(R.string.belowminimonerrormassage, Integer.valueOf(this.constantParams.getMinimumSalary())));
            } else if (this.pocketMoney == 0.0f) {
                this.estimatedSalaryErrorTV.setText(getString(R.string.try_adding_pocket_money_error_massage));
            } else if (this.isPocketMoneyPartOfPay) {
                this.estimatedSalaryErrorTV.setText(getString(R.string.pocket_money_part_of_pay_error, Integer.valueOf(this.constantParams.getMinimumSalary())));
            } else {
                this.estimatedSalaryErrorTV.setText(getString(R.string.still_too_low));
            }
        } else {
            this.estimatedSalaryTV.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.estimatedSalaryErrorTV.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.estimatedSalaryErrorTV.setText(getString(R.string.now_its_ok));
            this.finalSalaryTooLow = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void showAlertDialog(MyTypes type) {
        Log.d(TAG, "showAlertDialog: ");
        String s = "";
        String massage = "";
        String title = "";
        DialogInterface.OnClickListener onClickListener = null;
        switch (type) {
            case TOTAL_PAY:
                s = this.totalPayET.getText().toString();
                title = getString(R.string.salary_too_low_alert_title);
                massage = getString(R.string.salary_too_low_alert_massage);
                onClickListener = new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        PayPrefFragment.this.mListener.callingNextFromFrag(MyTypes.TOTAL_PAY);
                    }
                };
                break;
            case POCKET_MONEY:
                s = this.pocketMoneyET.getText().toString();
                title = getString(R.string.pocket_money_alert_title);
                massage = getString(R.string.pocket_money_alert_massage);
                if (!this.comingFromEditing) {
                    onClickListener = new DialogInterface.OnClickListener() {
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            PayPrefFragment.this.mListener.callingNextFromFrag(MyTypes.POCKET_MONEY);
                        }
                    };
                }
                this.comingFromEditing = false;
                break;
        }
        if (s.equals("") || (!s.equals("") && Integer.parseInt(s) == 0)) {
            new AlertDialog.Builder(this.context).setTitle(title).setMessage(massage).setPositiveButton(R.string.yes, onClickListener).setNegativeButton(R.string.no, (DialogInterface.OnClickListener) null).create().show();
        }
    }
}
