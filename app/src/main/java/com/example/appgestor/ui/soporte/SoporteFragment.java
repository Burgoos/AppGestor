package com.example.appgestor.ui.soporte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.appgestor.R;

public class SoporteFragment extends Fragment {

    private SoporteViewModel soporteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        soporteViewModel = ViewModelProviders.of(this).get(SoporteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_soporte, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        textView.setText("Soporte Fragment");
        return root;
    }
}
