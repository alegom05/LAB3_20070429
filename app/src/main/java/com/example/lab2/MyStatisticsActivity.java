package com.example.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class MyStatisticsActivity extends AppCompatActivity {

    private LinearLayout layoutHistorial;
    private Button btnVolverInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        layoutHistorial = findViewById(R.id.layoutHistorial);
        btnVolverInicio = findViewById(R.id.btnVolverInicio);

        SharedPreferences prefs = getSharedPreferences("stats", MODE_PRIVATE);
        Set<String> historial = prefs.getStringSet("historial", null);

        /* El if que maneja hasta 10 registros en el historial
        *  porque me pareció adecuado que no se sobrecargara*/
        if (historial != null) {
            int count = 0;
            for (String entrada : historial) {
                if (count >= 10) break; // mostrar solo 10

                TextView tv = new TextView(this);
                tv.setText(entrada);
                tv.setPadding(8, 8, 8, 8);
                layoutHistorial.addView(tv);

                count++;
            }
        }

        btnVolverInicio.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}