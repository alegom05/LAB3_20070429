package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static String TAG = "MAINACTDEBUG";
    Button btnSoftware, btnCiber, btnOpticas;



    public boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "oncreate");

        Button btnComenzar = findViewById(R.id.btnComenzar);


        View btnComprobarConexion = null;
        btnComprobarConexion.setOnClickListener(v -> {
            if (validarEntradas()) {
                if (isInternetAvailable(this)) {
                    Toast.makeText(this, "¡Conexión exitosa!", Toast.LENGTH_SHORT).show();
                    btnComenzar.setEnabled(true);
                } else {
                    Toast.makeText(this, "Sin conexión a Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    private boolean validarEntradas() {
        // Aquí puedes poner tu lógica para validar, por ejemplo:
        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        Spinner spinnerDificultad = findViewById(R.id.spinnerDificultad);
        EditText etCantidad = findViewById(R.id.etCantidad);

        String categoria = spinnerCategoria.getSelectedItem().toString();
        String dificultad = spinnerDificultad.getSelectedItem().toString();
        String cantidad = etCantidad.getText().toString();

        return !categoria.isEmpty() && !dificultad.isEmpty() && !cantidad.isEmpty();
    }


    private void abrirJuego(String tema) {
        Intent intent = new Intent(this, MyGameActivity.class);
        intent.putExtra("tema", tema);
        startActivity(intent);
    }







}