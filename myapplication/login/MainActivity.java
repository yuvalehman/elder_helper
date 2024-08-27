package com.example.yuvallehman.myapplication.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.interfaces.FragStates;
import com.example.yuvallehman.myapplication.interfaces.MyActions;
import com.example.yuvallehman.myapplication.interfaces.MyResults;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.login.LoginFragment;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements MyServerCallback, LoginFragment.LoginCallBack {
    public static final String TAG = "MainActivity";
    FrameLayout container;
    private FragStates fragState;
    private LoginFragment loginFragment;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;
    private ServerDataSupplier serverDataSupplier;
    private SignUpFragment signUpFragment;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: start");
        setContentView((int) R.layout.activity_main);
        this.serverDataSupplier = ServerDataSupplier.getInstance();
        this.container = (FrameLayout) findViewById(R.id.mainContainer);
        this.progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        showProgress(true);
        this.serverDataSupplier.init(getApplicationContext());
        this.serverDataSupplier.fetchConstParams(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (this.serverDataSupplier.constReady) {
            launchLoginFrag();
        }
    }

    /* access modifiers changed from: private */
    public void launchLoginFrag() {
        this.fragState = FragStates.LOGIN;
        if (this.loginFragment == null) {
            this.loginFragment = new LoginFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, this.loginFragment).addToBackStack((String) null).commit();
    }

    private void launchSignUpFrag() {
        this.fragState = FragStates.SIGN_UP;
        if (this.signUpFragment == null) {
            this.signUpFragment = new SignUpFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, this.signUpFragment).addToBackStack((String) null).commit();
    }

    public void serverDone(MyActions action, MyResults results, Exception e) {
        Log.d(TAG, "serverDone: start");
        showProgress(false);
        switch (action) {
            case FETCH_CONST:
                Log.d(TAG, "serverDone: FETCH_CONST");
                if (results == MyResults.SUCCESS) {
                    Log.d(TAG, "serverDone: FETCH_CONST if");
                    if (this.serverDataSupplier.checkIfLoggedInAndConnect()) {
                        Log.d(TAG, "serverDone: FETCH_CONST if2");
                        goToDesktop();
                        return;
                    }
                    Log.d(TAG, "serverDone: FETCH_CONST else2");
                    launchLoginFrag();
                    return;
                }
                Log.d(TAG, "serverDone: FETCH_CONST else");
                new AlertDialog.Builder(this).setTitle((int) R.string.problem).setMessage((int) R.string.problem_with_internet_massage).create().show();
                return;
            case LOGIN:
                Log.d(TAG, "serverDone: LOGIN");
                if (results == MyResults.SUCCESS) {
                    goToDesktop();
                    return;
                } else {
                    Snackbar.make((View) this.container, (CharSequence) e.getMessage(), 0).show();
                    return;
                }
            case SIGN_UP:
                Log.d(TAG, "serverDone: SIGN_UP");
                if (results == MyResults.SUCCESS) {
                    goToDesktop();
                    return;
                } else if (e != null) {
                    Snackbar.make((View) this.container, (CharSequence) e.getMessage(), -2).setAction((int) R.string.login_instead_massage, (View.OnClickListener) new View.OnClickListener() {
                        public final void onClick(View view) {
                            MainActivity.this.launchLoginFrag();
                        }
                    }).show();
                    return;
                } else {
                    return;
                }
            case LOGOUT:
                Log.d(TAG, "serverDone: LOGOUT");
                if (results != MyResults.SUCCESS) {
                    Snackbar.make((View) this.container, (CharSequence) e.getMessage(), 0).show();
                    break;
                } else {
                    launchLoginFrag();
                    break;
                }
            case TIMER_FINISHED:
                break;
            default:
                return;
        }
        Log.d(TAG, "serverDone: TIMER_FINISHED");
        FrameLayout frameLayout = this.container;
        Snackbar.make((View) frameLayout, (CharSequence) e.getMessage() + "\nTry getting better internet service.", -2).show();
    }

    public void goToDesktop() {
        startActivity(new Intent(getApplicationContext(), DesktopActivity.class));
    }

    public void loginCallback(MyActions action) {
        Log.d(TAG, "loginCallback: ");
        if (action == MyActions.SIGN_UP) {
            launchSignUpFrag();
        } else if (action == MyActions.PASSWORD) {
            launchForgotPasswordFrag();
        }
    }

    public void onBackPressed() {
        if (this.fragState == FragStates.SIGN_UP) {
            launchLoginFrag();
        } else if (this.fragState == FragStates.LOGIN) {
            finishAndRemoveTask();
        }
    }

    private void launchForgotPasswordFrag() {
        Log.d(TAG, "launchForgotPasswordFrag: ");
    }

    public void showProgress(final boolean show) {
        Log.d(TAG, "showProgress: " + show);
        int shortAnimTime = getResources().getInteger(17694720);
        int i = 8;
        this.container.setVisibility(show ? 8 : 0);
        float f = 0.0f;
        this.container.animate().setDuration((long) shortAnimTime).alpha(show ? 0.0f : 1.0f).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                MainActivity.this.container.setVisibility(show ? 8 : 0);
            }
        });
        ProgressBar progressBar2 = this.progressBar;
        if (show) {
            i = 0;
        }
        progressBar2.setVisibility(i);
        ViewPropertyAnimator duration = this.progressBar.animate().setDuration((long) shortAnimTime);
        if (show) {
            f = 1.0f;
        }
        duration.alpha(f).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                MainActivity.this.progressBar.setVisibility(show ? 0 : 8);
            }
        });
    }
}
