package com.example.appgestor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appgestor.R;
import com.example.appgestor.clases.PuntoVenta;

import java.util.ArrayList;

public class ListPuntoVentaAdapter extends ArrayAdapter<PuntoVenta> {

    public ListPuntoVentaAdapter(Context context, ArrayList<PuntoVenta> puntoVentaArrayList){
        super(context, R.layout.item_list_punto_venta, puntoVentaArrayList);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PuntoVenta pv = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_punto_venta, parent, false);
        }

        TextView txtNombre = convertView.findViewById(R.id.txtNombre);
        TextView txtCodigo = convertView.findViewById(R.id.txtCodigo);
        TextView txtDireccion = convertView.findViewById(R.id.txtDirección);

        txtNombre.setText(pv.getNombre());
        txtCodigo.setText((pv.getCodigo()));
        txtDireccion.setText(pv.getDirección());



        return super.getView(position, convertView, parent);
    }
}
