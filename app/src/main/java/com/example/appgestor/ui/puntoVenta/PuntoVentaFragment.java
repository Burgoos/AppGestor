package com.example.appgestor.ui.puntoVenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.appgestor.R;

public class PuntoVentaFragment extends Fragment {

    private PuntoVentaViewModel puntoVentaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        puntoVentaViewModel = ViewModelProviders.of(this).get(PuntoVentaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_punto_venta, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setText("Punto Venta Fragment");
        return root;
    }
}
