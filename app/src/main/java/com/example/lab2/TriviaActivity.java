package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.example.lab2.bean.Pregunta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        setContentView(R.layout.activity_game);

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

    private void iniciarTemporizador() {
        countDownTimer = new CountDownTimer(tiempoRestante, 1000) {
            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished;
                tvContador.setText("Tiempo: " + millisUntilFinished / 1000 + "s");
            }
            public void onFinish() {
                irAResultado();
            }
        }.start();
    }

    private void obtenerPreguntas(String categoria, int cantidad, String dificultad) {
        String url = "https://opentdb.com/api.php?amount=" + cantidad +
                "&category=9&difficulty=" + dificultad + "&type=multiple"; // Nota: el category ID puede ser mejorado

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject obj = results.getJSONObject(i);
                            String pregunta = obj.getString("question");
                            String correcta = obj.getString("correct_answer");
                            JSONArray incorrectas = obj.getJSONArray("incorrect_answers");
                            ArrayList<String> opciones = new ArrayList<>();
                            for (int j = 0; j < incorrectas.length(); j++) {
                                opciones.add(incorrectas.getString(j));
                            }
                            opciones.add(correcta); // agrega la correcta
                            preguntas.add(new Pregunta(pregunta, opciones, correcta));
                        }
                        mostrarPregunta();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error cargando preguntas", Toast.LENGTH_SHORT).show()
        );
        queue.add(request);
    }

    private void mostrarPregunta() {
        Pregunta p = preguntas.get(indice);
        tvPregunta.setText(p.getTexto());
        ArrayList<String> ops = p.getOpciones();
        op1.setText(ops.get(0));
        op2.setText(ops.get(1));
        op3.setText(ops.get(2));
        op4.setText(ops.get(3));
        opcionesGroup.clearCheck();
    }

    private void validarRespuesta() {
        int idSeleccionado = opcionesGroup.getCheckedRadioButtonId();
        if (idSeleccionado == -1) return;
        RadioButton seleccionado = findViewById(idSeleccionado);
        String respuesta = seleccionado.getText().toString();
        if (respuesta.equals(preguntas.get(indice).getRespuestaCorrecta())) correctas++;
        else incorrectas++;
    }

    private void irAResultado() {
        countDownTimer.cancel();
        Intent intent = new Intent(this, ResultadosActivity.class);
        intent.putExtra("correctas", correctas);
        intent.putExtra("incorrectas", incorrectas);
        intent.putExtra("total", preguntas.size());
        startActivity(intent);
        finish();
    }
}
