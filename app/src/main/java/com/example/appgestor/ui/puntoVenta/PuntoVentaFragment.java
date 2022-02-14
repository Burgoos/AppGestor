package com.example.appgestor.ui.puntoVenta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.appgestor.LoginActivity;
import com.example.appgestor.PuntoVentaDetalleActivity;
import com.example.appgestor.R;
import com.example.appgestor.adapters.ListPuntoVentaAdapter;
import com.example.appgestor.clases.DbBitmapUtility;
import com.example.appgestor.clases.PuntoVenta;
import com.example.appgestor.db.BDHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PuntoVentaFragment extends Fragment {

    private PuntoVentaViewModel puntoVentaViewModel;

    ListView lstViewPuntoVenta;
    EditText txtBuscar;
    ArrayList<PuntoVenta> listPuntoVenta, listFilterPuntoVenta;
    ListPuntoVentaAdapter myAdapter;
    Boolean filter=false;
    CharSequence charSeq="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        puntoVentaViewModel = ViewModelProviders.of(this).get(PuntoVentaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_punto_venta, container, false);

        lstViewPuntoVenta = root.findViewById(R.id.lstPuntoVenta);
        txtBuscar = root.findViewById(R.id.txtBuscarPuntoVenta);

        consultarPuntoVentas();

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charSeq = charSequence;
                if(charSequence.length()>0){
                    listFilterPuntoVenta = buscar(charSequence);
                    myAdapter = new ListPuntoVentaAdapter(getContext(), R.layout.item_list_punto_venta, listFilterPuntoVenta);
                    lstViewPuntoVenta.setAdapter(myAdapter);
                    filter = true;
                    myAdapter.notifyDataSetChanged();
                }else{
                    myAdapter = new ListPuntoVentaAdapter(getContext(), R.layout.item_list_punto_venta, listPuntoVenta);
                    lstViewPuntoVenta.setAdapter(myAdapter);
                    filter = false;
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lstViewPuntoVenta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(listPuntoVenta.get(i).getNombre());
                dialog.setMessage("¿Atenderá el pdv?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(filter){
                            Gson gson = new Gson();
                            PuntoVenta objeto = listFilterPuntoVenta.get(i);
                            Intent intent = new Intent(getContext(), PuntoVentaDetalleActivity.class);
                            intent.putExtra("puntoVenta", gson.toJson(objeto));
                            dialog.dismiss();
                            startActivity(intent);
                        }else{
                            Gson gson = new Gson();
                            PuntoVenta objeto = listPuntoVenta.get(i);
                            Intent intent = new Intent(getContext(), PuntoVentaDetalleActivity.class);
                            intent.putExtra("puntoVenta", gson.toJson(objeto));
                            dialog.dismiss();
                            startActivity(intent);
                        }
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(charSeq.length()>0){
            listFilterPuntoVenta = buscar(charSeq);
            myAdapter = new ListPuntoVentaAdapter(getContext(), R.layout.item_list_punto_venta, listFilterPuntoVenta);
            lstViewPuntoVenta.setAdapter(myAdapter);
            filter = true;
            myAdapter.notifyDataSetChanged();
        }else{
            myAdapter = new ListPuntoVentaAdapter(getContext(), R.layout.item_list_punto_venta, listPuntoVenta);
            lstViewPuntoVenta.setAdapter(myAdapter);
            filter = false;
            myAdapter.notifyDataSetChanged();
        }
    }

    private  ArrayList<PuntoVenta> buscar(CharSequence charSequence){
        ArrayList<PuntoVenta> listFilter = new ArrayList<>();
        for (PuntoVenta pv : listPuntoVenta) {
            if (pv.getNombre().toUpperCase().contains(charSequence.toString().toUpperCase())){
                listFilter.add(pv);
            }
        }
        return listFilter;
    }
    private void consultarPuntoVentas(){
        Log.d("Consulta", "Entra consulta");
        BDHelper bdHelper = new BDHelper(getContext(), null);
        SQLiteDatabase bd = bdHelper.getReadableDatabase();

        PuntoVenta pv;

        listPuntoVenta = new ArrayList<>();

        Cursor cursor = bd.rawQuery("SELECT * FROM " + BDHelper.getTablePuntoVenta(), null);

        while (cursor.moveToNext()){
            pv = new PuntoVenta(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getDouble(4),
                    null
            );
            if(cursor.getBlob(5)!=null){
                Bitmap foto = DbBitmapUtility.getImage(cursor.getBlob(5));
                pv.setFoto(foto);
            }else{
                Bitmap foto = null;
                pv.setFoto(foto);
            }
            listPuntoVenta.add(pv);
        }

        myAdapter = new ListPuntoVentaAdapter(getContext(), R.layout.item_list_punto_venta, listPuntoVenta);
        lstViewPuntoVenta.setAdapter(myAdapter);
    }
}
