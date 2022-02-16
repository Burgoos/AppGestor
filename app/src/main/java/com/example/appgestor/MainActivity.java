package com.example.appgestor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgestor.ui.soporte.SoporteFragment;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;
    private AppBarConfiguration mAppBarConfiguration;

    ImageView imgAdd, imgFoto;
    TextView txtNombreNavigationDrawer, txtCorreoNavigationDrawer;
    Bitmap foto;
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
        imgAdd= headerView.findViewById(R.id.imgAddNavigation);
        imgFoto = headerView.findViewById(R.id.imgFotoNavigation);

        txtNombreNavigationDrawer.setText(nombre);
        txtCorreoNavigationDrawer.setText(correo);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_punto_venta:
                        return true;
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

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCrop();
            }
        });

    }

    private void startCrop(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,4)
                .start(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), resultUri);
                    foto = getCroppedBitmap(bitmap);
                    imgFoto.setImageBitmap(foto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 3, bitmap.getHeight() / 3,
                bitmap.getWidth() / 3, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
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
