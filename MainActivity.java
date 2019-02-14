package com.example.yuvallehman.myapplication.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.interfaces.FragStates;
import com.example.yuvallehman.myapplication.interfaces.MyActions;
import com.example.yuvallehman.myapplication.interfaces.MyResults;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.yuvallehman.myapplication.interfaces.MyActions.SIGN_UP;
import static com.example.yuvallehman.myapplication.interfaces.MyResults.SUCCESS;

public class MainActivity extends AppCompatActivity implements MyServerCallback, LoginFragment.LoginCallBack {
    public static final String TAG = "MainActivity";


    private ServerDataSupplier serverDataSupplier;

    FrameLayout container;

    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;
    private FragStates fragState;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: start");
        setContentView(R.layout.activity_main);

        serverDataSupplier = ServerDataSupplier.getInstance();

        container = findViewById(R.id.mainContainer);
        progressBar = findViewById(R.id.mainProgressBar);

        showProgress(true);

        serverDataSupplier.init(getApplicationContext());
        serverDataSupplier.fetchConstParams(this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (serverDataSupplier.constReady) launchLoginFrag();
    }

    private void launchLoginFrag() {
        fragState = FragStates.LOGIN;
        if (loginFragment == null) loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, loginFragment)
                .addToBackStack(null)
                .commit();
    }

    private void launchSignUpFrag() {
        fragState = FragStates.SIGN_UP;
        if (signUpFragment == null) signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, signUpFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void serverDone(MyActions action, MyResults results, Exception e) {
        Log.d(TAG, "serverDone: start");
        showProgress(false);
        switch (action) {
            case FETCH_CONST:
                Log.d(TAG, "serverDone: FETCH_CONST");
                if (results == SUCCESS) {
                    Log.d(TAG, "serverDone: FETCH_CONST if");
                    if (serverDataSupplier.checkIfLoggedInAndConnect()) {
                        Log.d(TAG, "serverDone: FETCH_CONST if2");
                        goToDesktop();
                    } else {
                        Log.d(TAG, "serverDone: FETCH_CONST else2");
                        launchLoginFrag();
                    }
                } else {
                    Log.d(TAG, "serverDone: FETCH_CONST else");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.problem)
                            .setMessage(R.string.problem_with_internet_massage)
                            .create().show();
                }
                break;
            case LOGIN:
                Log.d(TAG, "serverDone: LOGIN");
                if (results == SUCCESS) {
                    goToDesktop();
                } else {
                    Snackbar.make(container, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
                break;
            case SIGN_UP:
                Log.d(TAG, "serverDone: SIGN_UP");
                if (results == SUCCESS) {
                    goToDesktop();
                } else {
                    if (e != null) {
                        Snackbar.make(container, e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.login_instead_massage, v -> launchLoginFrag()).show();
                    }
                }
                break;
            case LOGOUT:
                Log.d(TAG, "serverDone: LOGOUT");
                if (results == SUCCESS) {
                    launchLoginFrag();
                } else {
                    Snackbar.make(container, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            case TIMER_FINISHED:
                Log.d(TAG, "serverDone: TIMER_FINISHED");
                Snackbar.make(container, e.getMessage() + "\nTry getting better internet service.", Snackbar.LENGTH_INDEFINITE)
                        .show();

                break;
        }
    }

    public void goToDesktop() {
        startActivity(new Intent(getApplicationContext(), DesktopActivity.class));
    }

    @Override
    public void loginCallback(MyActions action) {
        Log.d(TAG, "loginCallback: ");
        if (action == SIGN_UP) {
            launchSignUpFrag();
        } else if (action == MyActions.PASSWORD) {
            launchForgotPasswordFrag();
        }


    }

    @Override
    public void onBackPressed() {
        if (fragState == FragStates.SIGN_UP) {
            launchLoginFrag();
        } else if (fragState == FragStates.LOGIN) {
            finishAndRemoveTask();
        }
    }

    private void launchForgotPasswordFrag() {
        Log.d(TAG, "launchForgotPasswordFrag: ");
    }

    public void showProgress(final boolean show) {
        Log.d(TAG, "showProgress: " + show);

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        container.setVisibility(show ? View.GONE : View.VISIBLE);
        container.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}
