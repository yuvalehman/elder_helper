package com.example.yuvallehman.myapplication.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;

public class SignUpFragment extends Fragment {
    public static final String TAG = "SignUpFragment";
    private final int PASSWORD_MIN_LENGTH = 8;
    private Activity activity;
    private EditText pass1TV;
    private EditText pass2TV;
    private EditText patientsFirst;
    private EditText patientsLast;
    private Button saveButton;
    private EditText usernameTV;
    private EditText workerIDET;

    public SignUpFragment() {
        Log.d(TAG, "SignUpFragment: constructor");
    }

    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        this.activity = getActivity();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        this.patientsFirst = (EditText) view.findViewById(R.id.firstName);
        this.patientsLast = (EditText) view.findViewById(R.id.lastName);
        this.usernameTV = (EditText) view.findViewById(R.id.username);
        this.pass1TV = (EditText) view.findViewById(R.id.password1);
        this.pass2TV = (EditText) view.findViewById(R.id.password2);
        this.workerIDET = (EditText) view.findViewById(R.id.signupworkerid);
        this.saveButton = (Button) view.findViewById(R.id.userDetButton);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SignUpFragment.this.signUpButtonPressed(view);
            }
        });
    }

    /* access modifiers changed from: private */
    public void signUpButtonPressed(View view) {
        View focusView;
        Log.d(TAG, "signUpButtonPressed: ");
        boolean formOk = true;
        String firstName = "";
        String name = this.patientsFirst.getText().toString();
        if (name.length() > 0) {
            firstName = name.substring(0, 1).toUpperCase().concat(name.substring(1, name.length()));
        }
        String lastName = "";
        String name2 = this.patientsLast.getText().toString();
        if (name2.length() > 0) {
            lastName = name2.substring(0, 1).toUpperCase().concat(name2.substring(1, name2.length()));
        }
        String username = this.usernameTV.getText().toString();
        String pass1 = this.pass1TV.getText().toString();
        String pass2 = this.pass2TV.getText().toString();
        String workerID = this.workerIDET.getText().toString();
        View focusView2 = null;
        if (username.equals("") || !username.contains("@")) {
            formOk = false;
            this.usernameTV.setError(getString(R.string.email_missing_massage));
            focusView2 = this.usernameTV;
        }
        if (firstName.equals("")) {
            formOk = false;
            this.patientsFirst.setError(getString(R.string.first_name_missing_massage));
            focusView2 = this.patientsFirst;
        }
        if (lastName.equals("")) {
            formOk = false;
            this.patientsLast.setError(getString(R.string.last_name_missing_massage));
            focusView2 = this.patientsLast;
        }
        if (pass1.length() < 8) {
            formOk = false;
            this.pass1TV.setError(getString(R.string.pass_error));
            focusView2 = this.pass1TV;
        }
        if (!pass1.equals(pass2)) {
            formOk = false;
            this.pass2TV.setError(getString(R.string.mismatch_password_fields));
            focusView2 = this.pass2TV;
        }
        if (workerID.equals("")) {
            formOk = false;
            this.workerIDET.setError(getString(R.string.can_not_be_empty));
            focusView = this.workerIDET;
        } else {
            focusView = focusView2;
        }
        if (formOk) {
            ServerDataSupplier.getInstance().signUpUser((MyServerCallback) this.activity, username, pass1, firstName, lastName, workerID);
        } else {
            focusView.requestFocus();
        }
    }
}
