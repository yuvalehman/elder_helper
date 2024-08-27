package com.example.yuvallehman.myapplication.desktop.contract;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.interfaces.FragStates;

public class ContractFragment extends Fragment {
    private static final String TAG = "ContractFragment";
    private DesktopActivity activity;

    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        this.activity = (DesktopActivity) getActivity();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_contract, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        DesktopActivity.fragState = FragStates.CONTRACT;
        this.activity.navigationBar.getMenu().findItem(R.id.desknavcontractitem).setChecked(true);
    }
}
