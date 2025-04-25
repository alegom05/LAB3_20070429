package com.example.lab2;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab2.bean.Pregunta;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity {

    TextView tvPregunta, tvContador;
    Button btnSiguiente;
    RadioGroup opcionesGroup;
    RadioButton op1, op2, op3, op4;

    ArrayList<Pregunta> preguntas;
    int indice = 0, correctas = 0, incorrectas = 0;
    CountDownTimer countDownTimer;
    long tiempoRestante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        tvPregunta = findViewById(R.id.tvPregunta);
        tvContador = findViewById(R.id.tvContador);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        opcionesGroup = findViewById(R.id.opcionesGroup);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);

        preguntas = new ArrayList<>();

        String categoria = getIntent().getStringExtra("categoria");
        int cantidad = getIntent().getIntExtra("cantidad", 5);
        String dificultad = getIntent().getStringExtra("dificultad");

        obtenerPreguntas(categoria, cantidad, dificultad);

        int segundosPorPregunta = dificultad.equals("fácil") ? 5 : dificultad.equals("medio") ? 7 : 10;
        tiempoRestante = cantidad * segundosPorPregunta * 1000L;

        iniciarTemporizador();

        btnSiguiente.setOnClickListener(v -> {
            validarRespuesta();
            indice++;
            if (indice < preguntas.size()) mostrarPregunta();
            else irAResultado();
        });
    }
}
