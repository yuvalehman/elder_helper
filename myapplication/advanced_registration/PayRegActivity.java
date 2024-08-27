package com.example.yuvallehman.myapplication.advanced_registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.advanced_registration.DeductFragment;
import com.example.yuvallehman.myapplication.advanced_registration.PayPrefFragment;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.interfaces.FragStates;
import com.example.yuvallehman.myapplication.interfaces.MyActions;
import com.example.yuvallehman.myapplication.interfaces.MyResults;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.interfaces.MyTypes;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.google.android.material.snackbar.Snackbar;
import java.util.GregorianCalendar;

public class PayRegActivity extends AppCompatActivity implements PayPrefFragment.PrefFragCallback, DeductFragment.DeductFragCallback, View.OnClickListener, MyServerCallback {
    private static final String CURRENT_SCREEN = "currentScreen";
    private static final String TAG = "PayRegActivity";
    public static long dateOfHire;
    private boolean alreadyShowPayAlert = false;
    private boolean alreadyShownDateOfHireAlert = false;
    private boolean alreadyShownPocketMoneyAlert = false;
    ImageButton backB;
    FrameLayout container;
    int currentScreen = 0;
    DeductFragment deductFragment;
    FragStates fragState;
    ImageButton nextB;
    PastFragment pastFragment;
    PayPrefFragment payPrefFragment;
    ImageButton saveB;
    ServerDataSupplier serverDataSupplier;
    WorkerFragment workerFragment;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_pay_reg);
        setTitle(getString(R.string.pref_frag_title));
        this.serverDataSupplier = ServerDataSupplier.getInstance();
        dateOfHire = this.serverDataSupplier.getUser().getStartLoggingDate();
        if (savedInstanceState != null) {
            this.currentScreen = savedInstanceState.getInt(CURRENT_SCREEN);
        }
        this.backB = (ImageButton) findViewById(R.id.payregbackb);
        this.nextB = (ImageButton) findViewById(R.id.payregnextb);
        this.saveB = (ImageButton) findViewById(R.id.payregsaveb);
        this.container = (FrameLayout) findViewById(R.id.payRegContainer);
        this.backB.setOnClickListener(this);
        this.nextB.setOnClickListener(this);
        this.saveB.setOnClickListener(this);
        switchScreen();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putInt(CURRENT_SCREEN, this.currentScreen);
        super.onSaveInstanceState(outState);
    }

    /* access modifiers changed from: package-private */
    public void launchPayPrefFrag() {
        Log.d(TAG, "launchPayPrefFrag: ");
        this.fragState = FragStates.PAY_PREF_REG;
        if (this.payPrefFragment == null) {
            this.payPrefFragment = new PayPrefFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.payRegContainer, this.payPrefFragment).addToBackStack((String) null).commit();
    }

    /* access modifiers changed from: package-private */
    public void launchDeductFrag() {
        Log.d(TAG, "launchDeductFrag: ");
        this.fragState = FragStates.DEDUCT_REG;
        if (this.deductFragment == null) {
            this.deductFragment = new DeductFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.payRegContainer, this.deductFragment).addToBackStack((String) null).commit();
    }

    /* access modifiers changed from: package-private */
    public void launchWorkerRegFrag() {
        Log.d(TAG, "launchWorkerRegFrag: ");
        this.fragState = FragStates.WORKER_REG;
        if (this.workerFragment == null) {
            this.workerFragment = new WorkerFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.payRegContainer, this.workerFragment).addToBackStack((String) null).commit();
    }

    /* access modifiers changed from: package-private */
    public void launchPastRegFrag() {
        Log.d(TAG, "launchWorkerRegFrag: ");
        this.fragState = FragStates.PAST_REG;
        if (this.pastFragment == null) {
            this.pastFragment = new PastFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.payRegContainer, this.pastFragment).addToBackStack((String) null).commit();
    }

    public void deductFragCallback() {
        Log.d(TAG, "deductFragCallback: ");
    }

    public void onClick(View v) {
        Log.d(TAG, "onClick: " + this.currentScreen);
        switch (v.getId()) {
            case R.id.payregbackb /*2131296605*/:
                this.currentScreen--;
                switchScreen();
                return;
            case R.id.payregnextb /*2131296606*/:
                switch (this.fragState) {
                    case PAY_PREF_REG:
                        if (this.payPrefFragment.isFinalSalaryTooLow() && !this.alreadyShowPayAlert) {
                            this.payPrefFragment.showAlertDialog(MyTypes.TOTAL_PAY);
                            return;
                        } else if (!this.alreadyShownPocketMoneyAlert && this.payPrefFragment.isPocketMoneyIsZero()) {
                            this.payPrefFragment.showAlertDialog(MyTypes.POCKET_MONEY);
                            return;
                        }
                        break;
                    case WORKER_REG:
                        if (!this.workerFragment.userInsertedDate && !this.alreadyShownDateOfHireAlert) {
                            this.workerFragment.showAlertDialog();
                            return;
                        }
                }
                this.currentScreen++;
                switchScreen();
                return;
            case R.id.payregsaveb /*2131296607*/:
                saveData();
                return;
            default:
                return;
        }
    }

    private void switchScreen() {
        Log.d(TAG, "switchScreen: ");
        switch (this.currentScreen) {
            case 0:
                this.backB.setVisibility(4);
                this.saveB.setVisibility(4);
                this.nextB.setVisibility(0);
                launchWorkerRegFrag();
                return;
            case 1:
                this.backB.setVisibility(0);
                this.nextB.setVisibility(0);
                this.saveB.setVisibility(4);
                launchPayPrefFrag();
                return;
            case 2:
                if (isPastRecordNeeded()) {
                    this.backB.setVisibility(0);
                    this.nextB.setVisibility(0);
                    this.saveB.setVisibility(4);
                } else {
                    this.backB.setVisibility(0);
                    this.nextB.setVisibility(4);
                    this.saveB.setVisibility(0);
                }
                launchDeductFrag();
                return;
            case 3:
                this.backB.setVisibility(0);
                this.nextB.setVisibility(4);
                this.saveB.setVisibility(0);
                launchPastRegFrag();
                return;
            default:
                return;
        }
    }

    private boolean isPastRecordNeeded() {
        if (this.workerFragment != null) {
            long startLoggingLong = this.serverDataSupplier.getUser().getStartLoggingDate();
            GregorianCalendar startLoggingDate = new GregorianCalendar();
            startLoggingDate.setTimeInMillis(startLoggingLong);
            GregorianCalendar startHireDate = new GregorianCalendar();
            startHireDate.setTimeInMillis(dateOfHire);
            if (startHireDate.get(1) == startLoggingDate.get(1) && startHireDate.get(2) == startLoggingDate.get(2)) {
                Log.d(TAG, "isPastRecordNeeded: Same month so pastRecord not needed...");
                return false;
            }
        }
        Log.d(TAG, "isPastRecordNeeded: YES");
        return true;
    }

    /* access modifiers changed from: private */
    public void saveData() {
        Log.d(TAG, "saveData: ");
        showProgress(true);
        Bundle bundle = new Bundle();
        DeductFragment deductFragment2 = this.deductFragment;
        if (deductFragment2 != null) {
            bundle.putAll(deductFragment2.saveData());
        }
        PayPrefFragment payPrefFragment2 = this.payPrefFragment;
        if (payPrefFragment2 != null) {
            bundle.putAll(payPrefFragment2.saveData());
        }
        WorkerFragment workerFragment2 = this.workerFragment;
        if (workerFragment2 != null) {
            bundle.putAll(workerFragment2.saveData());
        }
        PastFragment pastFragment2 = this.pastFragment;
        if (pastFragment2 != null) {
            bundle.putAll(pastFragment2.saveData());
        }
        this.serverDataSupplier.savePayReg(this, bundle);
    }

    public void callingNextFromFrag(MyTypes type) {
        Log.d(TAG, "callingNextFromFrag: ");
        switch (type) {
            case POCKET_MONEY:
                this.alreadyShownPocketMoneyAlert = true;
                break;
            case TOTAL_PAY:
                this.alreadyShowPayAlert = true;
                break;
            case DATE_OF_HIRE:
                this.alreadyShownDateOfHireAlert = true;
                break;
        }
        onClick(this.nextB);
    }

    public void onBackPressed() {
        int i = this.currentScreen;
        if (i == 0) {
            finish();
            return;
        }
        this.currentScreen = i - 1;
        switchScreen();
    }

    public void serverDone(MyActions action, MyResults results, Exception e) {
        Log.d(TAG, "serverDone: ");
        showProgress(false);
        if (results == MyResults.SUCCESS) {
            Intent intent = new Intent(getApplicationContext(), DesktopActivity.class);
            intent.putExtra(DesktopActivity.START_ACTION, DesktopActivity.START_MAKE_PAYCHECK);
            startActivity(intent);
            finish();
            return;
        }
        Snackbar.make((View) this.container, (CharSequence) "Couldn't save the data at this time. Try again later...", -2).setAction((CharSequence) "Try Again", (View.OnClickListener) new View.OnClickListener() {
            public final void onClick(View view) {
                PayRegActivity.this.saveData();
            }
        }).show();
    }

    private void showProgress(boolean b) {
        Log.d(TAG, "showProgress: " + b);
    }
}
