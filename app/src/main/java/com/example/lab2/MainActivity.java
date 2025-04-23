package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public static String TAG = "MAINACTDEBUG";
    Button btnSoftware, btnCiber, btnOpticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "oncreate");

        btnSoftware = findViewById(R.id.buttonSoft);
        btnCiber = findViewById(R.id.buttonCiber);
        btnOpticas = findViewById(R.id.buttonOpt);

        btnSoftware.setOnClickListener(v -> abrirJuego("Software"));
        btnCiber.setOnClickListener(v -> abrirJuego("Ciberseguridad"));
        btnOpticas.setOnClickListener(v -> abrirJuego("Ópticas"));

        SharedPreferences prefs = getSharedPreferences("stats", MODE_PRIVATE);
        prefs.edit().remove("historial").apply(); // limpia historial al abrir app
        prefs.edit().putInt("numero_juego", 1).apply(); // reinicia contador

    }

    private void abrirJuego(String tema) {
        Intent intent = new Intent(this, MyGameActivity.class);
        intent.putExtra("tema", tema);
        startActivity(intent);
    }

}