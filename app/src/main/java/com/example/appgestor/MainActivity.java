package com.example.appgestor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appgestor.ui.soporte.SoporteFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;
    private AppBarConfiguration mAppBarConfiguration;

    ImageView imgNavigationDrawer;
    TextView txtNombreNavigationDrawer, txtCorreoNavigationDrawer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPreferences =  PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        myEditor = myPreferences.edit();

        String nombre = myPreferences.getString("nombreUsuario", "");
        String correo = myPreferences.getString("correoUsuario", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_punto_venta, R.id.nav_soporte, R.id.nav_cerrar_sesion)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //NavigationView navigationHeaderView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        txtNombreNavigationDrawer =  (TextView) headerView.findViewById(R.id.txtNombreNavigationDrawer);
        txtCorreoNavigationDrawer = (TextView) headerView.findViewById(R.id.txtCorreoNavigationDrawer);
        txtNombreNavigationDrawer.setText(nombre);
        txtCorreoNavigationDrawer.setText(correo);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_punto_venta:
                    case R.id.nav_soporte:
                        Intent i = new Intent(MainActivity.this, SoporteActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.nav_cerrar_sesion:
                        myEditor.putInt("idUsuario", -1);
                        myEditor.putString("nombreUsuario", "");
                        myEditor.putString("correoUsuario", "");
                        myEditor.putString("fotoUsuario", "");
                        myEditor.commit();
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
