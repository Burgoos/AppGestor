package com.example.appgestor.ui.puntoVenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.appgestor.PuntoVentaDetalleActivity;
import com.example.appgestor.R;
import com.example.appgestor.adapters.ListPuntoVentaAdapter;
import com.example.appgestor.clases.PuntoVenta;

import java.util.ArrayList;

public class PuntoVentaFragment extends Fragment {

    private PuntoVentaViewModel puntoVentaViewModel;

    ListView lstPuntoVenta;
    ListPuntoVentaAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        puntoVentaViewModel = ViewModelProviders.of(this).get(PuntoVentaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_punto_venta, container, false);
        lstPuntoVenta = root.findViewById(R.id.lstPuntoVenta);

        final ArrayList<PuntoVenta> listPuntoVenta = new ArrayList<>();
        PuntoVenta pv = new PuntoVenta("Bolognesi", "45454", "Av. asddasd6", 45.4444, 5444.4);
        PuntoVenta pv2 = new PuntoVenta("Metro", "45454", "Av. a5444", 45.4444, 5444.4);
        listPuntoVenta.add(pv);
        listPuntoVenta.add(pv2);


        ListPuntoVentaAdapter myAdapter = new ListPuntoVentaAdapter(getContext(), R.layout.item_list_punto_venta, listPuntoVenta);
        lstPuntoVenta.setAdapter(myAdapter);

        /*myAdapter = new ListPuntoVentaAdapter(getContext(), R.layout.item_list_punto_venta, newList);
        lstPuntoVenta.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();*/


        lstPuntoVenta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PuntoVenta objeto = listPuntoVenta.get(i);
                Intent intent = new Intent(getContext(), PuntoVentaDetalleActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
