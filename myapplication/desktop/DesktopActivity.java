package com.example.yuvallehman.myapplication.desktop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.advanced_registration.PayRegActivity;
import com.example.yuvallehman.myapplication.desktop.contract.ContractFragment;
import com.example.yuvallehman.myapplication.desktop.desktop.AddEventFragment;
import com.example.yuvallehman.myapplication.desktop.desktop.DesktopFragment;
import com.example.yuvallehman.myapplication.desktop.desktop.EditEventFragment;
import com.example.yuvallehman.myapplication.desktop.paycheck.PaycheckFragment;
import com.example.yuvallehman.myapplication.interfaces.FragStates;
import com.example.yuvallehman.myapplication.interfaces.MyActions;
import com.example.yuvallehman.myapplication.interfaces.MyResults;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.login.MainActivity;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.GregorianCalendar;

public class DesktopActivity extends AppCompatActivity implements MyServerCallback, BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static final String START_ACTION = "startAction";
    public static final String START_MAKE_PAYCHECK = "startMakePayCheck";
    private static final String TAG = "DesktopActivity";
    public static int chosenMonth;
    public static int chosenYear;
    public static String[] eventTypeListUnique;
    public static String[] eventsTypeForDisplay;
    public static FragStates fragState;
    public static String[] moneyTypeListUnique;
    GregorianCalendar calendar;
    FrameLayout container;
    private ContractFragment contractFragment;
    DesktopFragment desktopFragment;
    EditEventFragment editEventFragment;
    private FragmentManager fragmentManager;
    Menu menu;
    CardView monthCV;
    String[] monthList = new String[12];
    TextView monthTV;
    public BottomNavigationView navigationBar;
    private PaycheckFragment paycheckFragment;
    private ServerDataSupplier sds;
    private SettingsFragment settingsFragment;
    private String startCommand;
    private LinearLayout upperLinearLine;
    CardView yearCV;
    TextView yearTV;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_desktop);
        this.sds = ServerDataSupplier.getInstance();
        this.calendar = new GregorianCalendar();
        Intent intent = getIntent();
        setTitle(getString(R.string.app_title) + " - " + this.sds.getUser().getFirstName() + "'s User Page");
        eventsTypeForDisplay = getResources().getStringArray(R.array.event_types_for_display);
        eventTypeListUnique = getResources().getStringArray(R.array.event_types_unique);
        moneyTypeListUnique = getResources().getStringArray(R.array.money_types_unique);
        this.container = (FrameLayout) findViewById(R.id.desktopContainer);
        this.navigationBar = (BottomNavigationView) findViewById(R.id.desktopNavigationView);
        this.monthTV = (TextView) findViewById(R.id.desktopMonthPickerTV);
        this.yearTV = (TextView) findViewById(R.id.desktopYearPickerTV);
        this.monthCV = (CardView) findViewById(R.id.deskMonthCardView);
        this.yearCV = (CardView) findViewById(R.id.deskYearCardView);
        this.upperLinearLine = (LinearLayout) findViewById(R.id.deskoupperline);
        this.monthCV.setOnClickListener(this);
        this.yearCV.setOnClickListener(this);
        chosenYear = this.calendar.get(1);
        this.monthList = getResources().getStringArray(R.array.month_list);
        chosenMonth = this.calendar.get(2);
        this.monthTV.setText(this.monthList[chosenMonth]);
        this.yearTV.setText(String.valueOf(chosenYear));
        this.navigationBar.setOnNavigationItemSelectedListener(this);
        this.fragmentManager = getSupportFragmentManager();
        this.startCommand = intent.getStringExtra(START_ACTION);
    }

    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.deskMonthCardView /*2131296403*/:
            case R.id.deskYearCardView /*2131296404*/:
                openDateDialog();
                return;
            default:
                return;
        }
    }

    private void openDateDialog() {
        new MyDatePickerDialogFragment().show(this.fragmentManager, "dateDialog");
    }

    private void launchDesktopFragment() {
        Log.d(TAG, "launchDesktopFragment: ");
        if (this.desktopFragment == null) {
            this.desktopFragment = new DesktopFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.desktopContainer, this.desktopFragment).addToBackStack((String) null).commit();
    }

    /* access modifiers changed from: private */
    public void logout() {
        Toast.makeText(getApplicationContext(), "Logging You Out...", 0).show();
        this.sds.logout(this);
    }

    private void goBack() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Logout Confirmation").setMessage((CharSequence) "Are you sure you want to LOGOUT and go back to login screen?").setPositiveButton((int) R.string.logout, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                DesktopActivity.this.logout();
            }
        }).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null).create().show();
    }

    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        switch (fragState) {
            case DESKTOP:
                Log.d(TAG, "onBackPressed: DESKTOP");
                goBack();
                return;
            case SETTINGS:
                displaySetToDesktopView(true);
                super.onBackPressed();
                return;
            default:
                Log.d(TAG, "onBackPressed: SUPER");
                super.onBackPressed();
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        String str = this.startCommand;
        if (str != null && str.equals(START_MAKE_PAYCHECK)) {
            launchMakePaycheck();
        } else if (!this.sds.advancedUserDataReady) {
            this.sds.fetchAdvanceUserData(this);
        } else {
            Log.d(TAG, "onCreate: just before launchDesktopFragment");
            launchDesktopFragment();
        }
    }

    public void serverDone(MyActions action, MyResults results, Exception e) {
        Log.d(TAG, "serverDone: ");
        switch (action) {
            case SAVE_EVENT:
                Log.d(TAG, "serverDone: addevent");
                if (results == MyResults.SUCCESS) {
                    this.desktopFragment.addEventFinished();
                    Toast.makeText(getApplicationContext(), "Event Saved", 0).show();
                    return;
                } else if (e != null) {
                    showError(e);
                    return;
                } else {
                    return;
                }
            case LOGOUT:
                Log.d(TAG, "serverDone: serverCaller");
                if (results == MyResults.SUCCESS) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return;
                } else if (e != null) {
                    showError(e);
                    return;
                } else {
                    return;
                }
            case FETCH_ADVANCED_USER_DATA:
                Log.d(TAG, "serverDone: FETCH_ADVANCED_USER_DATA");
                launchDesktopFragment();
                return;
            default:
                return;
        }
    }

    private void showError(Exception e) {
        Log.d(TAG, "showError: ");
        FrameLayout frameLayout = this.container;
        Snackbar.make((View) frameLayout, (CharSequence) e.getMessage() + "\nTry again later...", 0).show();
    }

    public void updateDisplay() {
        Log.d(TAG, "updateDisplay: ");
        this.monthTV.setText(this.monthList[chosenMonth]);
        this.yearTV.setText(String.valueOf(chosenYear));
        int i = AnonymousClass1.$SwitchMap$com$example$yuvallehman$myapplication$interfaces$FragStates[fragState.ordinal()];
        if (i == 1) {
            Log.d(TAG, "updateDisplay: DESKTOP");
            this.desktopFragment.refreshEventList();
        } else if (i == 3) {
            Log.d(TAG, "updateDisplay: MAKE_PAYCHECK");
            this.paycheckFragment.refreshPayList();
        }
    }

    public void openEditEventFragment(int position) {
        Log.d(TAG, "openEditEventFragment: ");
        fragState = FragStates.EDIT;
        if (this.editEventFragment == null) {
            this.editEventFragment = new EditEventFragment();
        }
        Bundle args = new Bundle();
        args.putInt(AddEventFragment.POSITION, position);
        this.editEventFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.desktopContainer, this.editEventFragment).addToBackStack((String) null).commit();
    }

    private void registerPreferences() {
        Log.d(TAG, "registerPreferences: ");
        startActivity(new Intent(getApplicationContext(), PayRegActivity.class));
    }

    private void launchMakePaycheck() {
        Log.d(TAG, "launchMakePaycheck: ");
        if (this.paycheckFragment == null) {
            this.paycheckFragment = new PaycheckFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.desktopContainer, this.paycheckFragment).addToBackStack((String) null).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.main_menu, menu2);
        this.menu = menu2;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.desktopMenuItem /*2131296411*/:
                displaySetToDesktopView(true);
                launchDesktopFragment();
                break;
            case R.id.logoutMenuItem /*2131296535*/:
                logout();
                break;
            case R.id.saveconstparams /*2131296652*/:
                Log.d(TAG, "onOptionsItemSelected: save const params!");
                this.sds.tempSaveConstParams();
                break;
            case R.id.settingMenuItem /*2131296679*/:
                Log.d(TAG, "onOptionsItemSelected: Going to setting!");
                launchSettingsFragment();
                displaySetToDesktopView(false);
                break;
        }
        return true;
    }

    private void displaySetToDesktopView(boolean show) {
        if (show) {
            this.menu.findItem(R.id.desktopMenuItem).setVisible(false);
            this.menu.findItem(R.id.settingMenuItem).setVisible(true);
            this.navigationBar.setVisibility(0);
            this.upperLinearLine.setVisibility(0);
            return;
        }
        this.menu.findItem(R.id.desktopMenuItem).setVisible(true);
        this.menu.findItem(R.id.settingMenuItem).setVisible(false);
        this.navigationBar.setVisibility(8);
        this.upperLinearLine.setVisibility(8);
    }

    private void launchSettingsFragment() {
        if (this.settingsFragment == null) {
            this.settingsFragment = new SettingsFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.desktopContainer, this.settingsFragment).addToBackStack((String) null).commit();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.desknavcontractitem /*2131296405*/:
                launchContractFragment();
                return true;
            case R.id.desknavdeskitem /*2131296406*/:
                Log.d(TAG, "onNavigationItemSelected: just before launchDesktopFragment");
                launchDesktopFragment();
                return true;
            case R.id.desknavpaycheckitem /*2131296407*/:
                if (this.sds.advancedUserDataReady) {
                    launchMakePaycheck();
                    return true;
                }
                registerPreferences();
                return true;
            default:
                return true;
        }
    }

    private void launchContractFragment() {
        Log.d(TAG, "launchContractFragment: ");
        if (this.contractFragment == null) {
            this.contractFragment = new ContractFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.desktopContainer, this.contractFragment).addToBackStack((String) null).commit();
    }
}
