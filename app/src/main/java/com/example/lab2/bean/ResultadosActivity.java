package com.example.lab2.bean;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab2.MainActivity;
import com.example.lab2.R;

public class ResultadosActivity extends AppCompatActivity {

    TextView tvCorrectas, tvIncorrectas, tvNoRespondidas;
    Button btnVolverJugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        tvCorrectas = findViewById(R.id.tvCorrectas);
        tvIncorrectas = findViewById(R.id.tvIncorrectas);
        tvNoRespondidas = findViewById(R.id.tvNoRespondidas);
        btnVolverJugar = findViewById(R.id.btnVolverJugar);

        int correctas = getIntent().getIntExtra("correctas", 0);
        int incorrectas = getIntent().getIntExtra("incorrectas", 0);
        int total = getIntent().getIntExtra("total", 0);

        int noRespondidas = total - correctas - incorrectas;

        tvCorrectas.setText("Correctas: " + correctas);
        tvIncorrectas.setText("Incorrectas: " + incorrectas);
        tvNoRespondidas.setText("No Respondidas: " + noRespondidas);

        btnVolverJugar.setOnClickListener(v -> {
            Intent intent = new Intent(ResultadosActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
