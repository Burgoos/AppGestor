package com.example.appgestor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgestor.adapters.ListPuntoVentaAdapter;
import com.example.appgestor.clases.DbBitmapUtility;
import com.example.appgestor.clases.Producto;
import com.example.appgestor.clases.PuntoVenta;
import com.example.appgestor.db.BDHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ReportePreciosActivity extends AppCompatActivity {

    TableLayout tblProductos;
    ArrayList<Producto> listProductos, listFilterProductos;
    ImageView btnBack, btnSave;
    TextView txtTitle;
    PuntoVenta pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_precios);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        txtTitle = findViewById(R.id.txtTitle);
        tblProductos = findViewById(R.id.tblProductos);

        if(!getIntent().getStringExtra("puntoVenta").equals("")) {
            Gson gson = new Gson();
            pv = gson.fromJson(getIntent().getStringExtra("puntoVenta"), PuntoVenta.class);
            txtTitle.setText(pv.getNombre());
        }

        consultarProductos();

        String[] cabecera = {"P. Costo", "P. Rvta Mayor", "Stock"};

        // Primero dibujar el encabezado; esto es poner "TALLAS" y a la derecha todas las cabecera
        TableRow fila = new TableRow(ReportePreciosActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(lp);

        TextView txtProductoCabecera = new TextView(ReportePreciosActivity.this);
        txtProductoCabecera.setText("Productos");
        txtProductoCabecera.setBackgroundResource(R.drawable.edit_text_tabla1);
        txtProductoCabecera.setWidth(300);
        txtProductoCabecera.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        fila.addView(txtProductoCabecera);

        // Ahora agregar las cabecera
        for (int x = 0; x < cabecera.length; x++) {
            TextView txtCabecera = new TextView(ReportePreciosActivity.this);
            txtCabecera.setText(cabecera[x]);
            txtCabecera.setBackgroundResource(R.drawable.edit_text_tabla1);
            txtCabecera.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            fila.addView(txtCabecera);
        }
        // Finalmente agregar la fila en la primera posición
        tblProductos.addView(fila, 0);

        // Ahora por cada color hacer casi lo mismo
        for (int x = 0; x < listProductos.size(); x++) {
            TableRow filaColor = new TableRow(ReportePreciosActivity.this);
            filaColor.setLayoutParams(lp);
            // Borde
            //filaColor.setBackgroundResource(R.drawable.borde_tabla);
            // El nombre del color
            EditText txtProducto = new EditText(ReportePreciosActivity.this);
            txtProducto.setText(listProductos.get(x).getNombre());
            txtProducto.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            txtProducto.setWidth(300);
            txtProducto.setBackgroundResource(R.drawable.edit_text_tabla2);
            txtProducto.setEnabled(false);
            txtProducto.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            txtProducto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            filaColor.addView(txtProducto);

            // Y ahora por cada talla, agregar un campo de texto

            EditText editTextCosto = new EditText(ReportePreciosActivity.this);
            editTextCosto.setText(String.valueOf(listProductos.get(x).getpCosto()));
            editTextCosto.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editTextCosto.setWidth(200);
            editTextCosto.setBackgroundResource(R.drawable.edit_text_tabla2);
            editTextCosto.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            editTextCosto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            filaColor.addView(editTextCosto);
            filaColor.setMinimumWidth(200);

            EditText editTextMayor = new EditText(ReportePreciosActivity.this);
            editTextMayor.setText(String.valueOf(listProductos.get(x).getpMayor()));
            editTextMayor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editTextMayor.setWidth(200);
            editTextMayor.setBackgroundResource(R.drawable.edit_text_tabla2);
            editTextMayor.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            editTextMayor.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            filaColor.addView(editTextMayor);
            filaColor.setMinimumWidth(200);

            EditText editTextStock = new EditText(ReportePreciosActivity.this);
            editTextStock.setText(String.valueOf(listProductos.get(x).getStock()));
            editTextStock.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editTextStock.setWidth(200);
            editTextStock.setBackgroundResource(R.drawable.edit_text_tabla2);
            editTextStock.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            editTextStock.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            filaColor.addView(editTextStock);
            filaColor.setMinimumWidth(200);

            // Finalmente agregar la fila
            tblProductos.addView(filaColor);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private void consultarProductos(){
        Log.d("Producto Consulta", "Entra consulta");

        BDHelper bdHelper = new BDHelper(ReportePreciosActivity.this, null);
        SQLiteDatabase bd = bdHelper.getReadableDatabase();

        Producto p;

        listProductos = new ArrayList<>();

        Cursor cursor = bd.rawQuery("SELECT * FROM " + BDHelper.getTableProductos(), null);

        while (cursor.moveToNext()){
            p = new Producto(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getFloat(2),
                    cursor.getFloat(3),
                    cursor.getInt(4)
            );
            listProductos.add(p);
        }
    }
}
