package com.example.yuvallehman.myapplication.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.interfaces.MyActions;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private Activity activity;
    private LoginCallBack listener;
    /* access modifiers changed from: private */
    public LinearLayout mainLayout;
    private TextView passwordTV;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;
    private ServerDataSupplier serverDataSupplier;
    private TextView usernameTV;

    public interface LoginCallBack {
        void loginCallback(MyActions myActions);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        this.usernameTV = (TextView) view.findViewById(R.id.usernameTV);
        this.passwordTV = (TextView) view.findViewById(R.id.passwordTV);
        Button loginB = (Button) view.findViewById(R.id.loginFragLoginB);
        this.progressBar = (ProgressBar) view.findViewById(R.id.mainProgressBar);
        this.mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout2);
        Button backToDeskB = (Button) view.findViewById(R.id.backtodesktopbutton);
        Button logoutB = (Button) view.findViewById(R.id.logoutfromloginfragbutton);
        loginB.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                LoginFragment.this.login(view);
            }
        });
        ((TextView) view.findViewById(R.id.loginFragSignUpB)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                LoginFragment.this.signUp(view);
            }
        });
        ((TextView) view.findViewById(R.id.forgotPasswordTV)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                LoginFragment.this.forgotPassword(view);
            }
        });
        logoutB.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                LoginFragment.this.logout(view);
            }
        });
        backToDeskB.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                LoginFragment.this.backToDesk(view);
            }
        });
        logoutB.setVisibility(4);
        backToDeskB.setVisibility(4);
        loginB.setText(getString(R.string.log_in));
        ((TextView) view.findViewById(R.id.logintitleTV)).setText(getString(R.string.welcome));
    }

    /* access modifiers changed from: private */
    public void logout(View view) {
        this.serverDataSupplier.logout((MyServerCallback) this.activity);
    }

    /* access modifiers changed from: private */
    public void backToDesk(View view) {
        ((MainActivity) this.activity).goToDesktop();
    }

    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        this.activity = getActivity();
        this.serverDataSupplier = ServerDataSupplier.getInstance();
        this.listener = (LoginCallBack) this.activity;
    }

    public void login(View view) {
        Log.d(TAG, "login: ");
        showProgress(true);
        String username = this.usernameTV.getText().toString();
        if (username.equals("") || !username.contains("@")) {
            showProgress(false);
            this.usernameTV.setError(getString(R.string.email_missing_massage));
            Log.d(TAG, "login: idVerify returned false!");
            return;
        }
        this.serverDataSupplier.login((MyServerCallback) this.activity, username, this.passwordTV.getText().toString());
    }

    private void showProgress(final boolean show) {
        Log.d(TAG, "showProgress: " + show);
        int shortAnimTime = getResources().getInteger(17694720);
        int i = 8;
        this.mainLayout.setVisibility(show ? 8 : 0);
        float f = 0.0f;
        this.mainLayout.animate().setDuration((long) shortAnimTime).alpha(show ? 0.0f : 1.0f).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                LoginFragment.this.mainLayout.setVisibility(show ? 8 : 0);
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
                LoginFragment.this.progressBar.setVisibility(show ? 0 : 8);
            }
        });
    }

    /* access modifiers changed from: private */
    public void signUp(View view) {
        Log.d(TAG, "signUp: ");
        this.listener.loginCallback(MyActions.SIGN_UP);
    }

    /* access modifiers changed from: private */
    public void forgotPassword(View view) {
        Log.d(TAG, "forgotPassword: ");
        this.listener.loginCallback(MyActions.PASSWORD);
    }
}
