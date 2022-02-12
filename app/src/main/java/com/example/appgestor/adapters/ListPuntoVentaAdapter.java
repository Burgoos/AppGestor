package com.example.appgestor.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appgestor.MapsActivity;
import com.example.appgestor.R;
import com.example.appgestor.clases.PuntoVenta;

import java.util.ArrayList;

public class ListPuntoVentaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<PuntoVenta> listPV;
    public ListPuntoVentaAdapter(Context context, int layout, ArrayList<PuntoVenta> listPV){
        this.context = context;
        this.layout = layout;
        this.listPV = listPV;
    }

    @Override
    public int getCount() {
        return this.listPV.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listPV.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;

        LayoutInflater layoutInflater = LayoutInflater.from(this.context);

        v= layoutInflater.inflate(R.layout.item_list_punto_venta, null);


        String nombre  = listPV.get(position).getNombre();


        TextView txtNombrePV = (TextView) v.findViewById(R.id.txtNombre_Item);
        ImageView imgLocation = (ImageView) v.findViewById(R.id.imgLocation_Item);
        txtNombrePV.setText(nombre);

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                context.startActivity(intent);
            }
        });

        return v;
    }
}
