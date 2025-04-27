package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//En Stats es simplemente la cantidad de preguntas correctas,
//incorrectas o sin responder.

public class StatsActivity extends AppCompatActivity {

    TextView tvCorrectas, tvIncorrectas, tvNoRespondidas;
    Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        tvCorrectas = findViewById(R.id.tvCorrectas);
        tvIncorrectas = findViewById(R.id.tvIncorrectas);
        tvNoRespondidas = findViewById(R.id.tvNoRespondidas);
        btnVolver = findViewById(R.id.btnVolver);

        int correctas = getIntent().getIntExtra("correctas", 0);
        int incorrectas = getIntent().getIntExtra("incorrectas", 0);
        int noRespondidas = getIntent().getIntExtra("noRespondidas", 0);

        tvCorrectas.setText("Correctas: " + correctas);
        tvIncorrectas.setText("Incorrectas: " + incorrectas);
        tvNoRespondidas.setText("No Respondidas: " + noRespondidas);

        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(StatsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
