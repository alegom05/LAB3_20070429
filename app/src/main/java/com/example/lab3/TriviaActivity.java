package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TriviaActivity extends AppCompatActivity {

    TextView tvTimer, tvPregunta, tvProgreso, tvCategoria;
    RadioGroup groupOpciones;
    Button btnSiguiente;

    ArrayList<Pregunta> preguntas = new ArrayList<>();
    int currentPregunta = 0;
    int correctas = 0;
    int incorrectas = 0;
    int noRespondidas = 0;
    long tiempoRestante;
    Thread hiloContador;
    boolean tiempoTerminado = false;

    String categoriaElegida;
    String dificultadElegida;
    int cantidadPreguntas;

    Handler handler = new Handler(Looper.getMainLooper());
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        tvTimer = findViewById(R.id.tvTimer);
        tvPregunta = findViewById(R.id.tvPregunta);
        tvProgreso = findViewById(R.id.tvProgreso);
        tvCategoria = findViewById(R.id.tvCategoria);
        groupOpciones = findViewById(R.id.groupOpciones);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        categoriaElegida = getIntent().getStringExtra("categoria");
        dificultadElegida = getIntent().getStringExtra("dificultad");
        cantidadPreguntas = getIntent().getIntExtra("cantidad", 5);

        tvCategoria.setText(categoriaElegida);

        cargarPreguntasDesdeAPI();

        btnSiguiente.setOnClickListener(v -> {
            mostrarSiguientePregunta();
        });

    }

    //Uso de IA para conectarme al API. A partir del API
    //se reciben las preguntas y respuestas, que están originalmente
    //en formato JSON como se aprendió en GTICS.
    private void cargarPreguntasDesdeAPI() {
        int categoryId = obtenerCategoryId(categoriaElegida);
        String url = "https://opentdb.com/api.php?amount=" + cantidadPreguntas +
                "&category=" + categoryId +
                "&difficulty=" + dificultadElegida +
                "&type=multiple";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> Toast.makeText(TriviaActivity.this, "Error al obtener preguntas", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    try {
                        JSONObject json = new JSONObject(body);
                        int responseCode = json.getInt("response_code");

                        if (responseCode == 0) {
                            JSONArray results = json.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject obj = results.getJSONObject(i);

                                String preguntaTexto = obj.getString("question")
                                        .replace("&quot;", "\"")
                                        .replace("&#039;", "'")
                                        .replace("&amp;", "&")
                                        .replace("&eacute;", "é");

                                String respuestaCorrecta = obj.getString("correct_answer")
                                        .replace("&quot;", "\"")
                                        .replace("&#039;", "'")
                                        .replace("&amp;", "&")
                                        .replace("&eacute;", "é");

                                JSONArray respuestasIncorrectas = obj.getJSONArray("incorrect_answers");

                                ArrayList<String> opciones = new ArrayList<>();
                                opciones.add(respuestaCorrecta);
                                for (int j = 0; j < respuestasIncorrectas.length(); j++) {
                                    String opcionIncorrecta = respuestasIncorrectas.getString(j)
                                            .replace("&quot;", "\"")
                                            .replace("&#039;", "'")
                                            .replace("&amp;", "&")
                                            .replace("&eacute;", "é");
                                    opciones.add(opcionIncorrecta);
                                }

                                Collections.shuffle(opciones);

                                preguntas.add(new Pregunta(preguntaTexto, respuestaCorrecta, opciones));
                            }

                            handler.post(() -> {
                                iniciarContador();
                                mostrarPreguntaActual();
                            });

                        } else {
                            handler.post(() -> Toast.makeText(TriviaActivity.this, "No se encontraron preguntas. Intenta otra configuración.", Toast.LENGTH_LONG).show());
                        }
                        JSONArray results = json.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject obj = results.getJSONObject(i);

                            String preguntaTexto = obj.getString("question")
                                    .replace("&quot;", "\"")
                                    .replace("&#039;", "'")
                                    .replace("&amp;", "&")
                                    .replace("&eacute;", "é");

                            String respuestaCorrecta = obj.getString("correct_answer")
                                    .replace("&quot;", "\"")
                                    .replace("&#039;", "'")
                                    .replace("&amp;", "&")
                                    .replace("&eacute;", "é");

                            JSONArray respuestasIncorrectas = obj.getJSONArray("incorrect_answers");

                            ArrayList<String> opciones = new ArrayList<>();
                            opciones.add(respuestaCorrecta);
                            for (int j = 0; j < respuestasIncorrectas.length(); j++) {
                                String opcionIncorrecta = respuestasIncorrectas.getString(j)
                                        .replace("&quot;", "\"")
                                        .replace("&#039;", "'")
                                        .replace("&amp;", "&")
                                        .replace("&eacute;", "é");
                                opciones.add(opcionIncorrecta);
                            }

                            Collections.shuffle(opciones);

                            preguntas.add(new Pregunta(preguntaTexto, respuestaCorrecta, opciones));
                        }

                        handler.post(() -> {
                            if (preguntas.size() > cantidadPreguntas) {
                                preguntas = new ArrayList<>(preguntas.subList(0, cantidadPreguntas));
                            }
                            iniciarContador();
                            mostrarPreguntaActual();
                        });


                    } catch (Exception e) {
                        handler.post(() -> Toast.makeText(TriviaActivity.this, "Error de formato en JSON", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }

    //Contador
    private void iniciarContador() {
        int segundosPorPregunta = dificultadElegida.equals("easy") ? 5 : (dificultadElegida.equals("medium") ? 7 : 10);
        tiempoRestante = cantidadPreguntas * segundosPorPregunta;

        //Aquí el uso de hilos
        hiloContador = new Thread(() -> {
            long inicio = SystemClock.elapsedRealtime();
            while (tiempoRestante > 0 && !tiempoTerminado) {
                long ahora = SystemClock.elapsedRealtime();
                tiempoRestante -= (ahora - inicio) / 1000;
                inicio = ahora;

                handler.post(() -> tvTimer.setText("Tiempo restante: " + tiempoRestante + "s"));
                SystemClock.sleep(1000);
            }
            if (!tiempoTerminado) {
                tiempoTerminado = true;
                handler.post(this::terminarTrivia);
            }
        });
        hiloContador.start();
    }

    //Los siguientes métodos son para gestionar las preguntas y las respuestas.
    private void mostrarPreguntaActual() {
        if (currentPregunta < preguntas.size()) {
            Pregunta p = preguntas.get(currentPregunta);
            tvPregunta.setText(p.getPregunta());

            groupOpciones.removeAllViews();
            for (String opcion : p.getOpciones()) {
                RadioButton rb = new RadioButton(this);
                rb.setText(opcion);
                groupOpciones.addView(rb);
            }

            tvProgreso.setText("Pregunta " + (currentPregunta + 1) + "/" + preguntas.size());
        }
    }

    private void verificarRespuesta() {
        int seleccion = groupOpciones.getCheckedRadioButtonId();
        if (seleccion == -1) {
            noRespondidas++;
        } else {
            RadioButton rb = findViewById(seleccion);
            String respuesta = rb.getText().toString();
            if (respuesta.equals(preguntas.get(currentPregunta).getRespuestaCorrecta())) {
                correctas++;
            } else {
                incorrectas++;
            }
        }
    }

    private void mostrarSiguientePregunta() {
        verificarRespuesta(); // Verificar antes de avanzar
        currentPregunta++;
        if (currentPregunta < preguntas.size()) {
            mostrarPreguntaActual();
        } else {
            tiempoTerminado = true;
            terminarTrivia();
        }
    }


    private void terminarTrivia() {
        if (hiloContador != null && hiloContador.isAlive()) {
            hiloContador.interrupt();
        }

        Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
        intent.putExtra("correctas", correctas);
        intent.putExtra("incorrectas", incorrectas);
        intent.putExtra("noRespondidas", noRespondidas);
        startActivity(intent);
        finish();
    }

    private int obtenerCategoryId(String categoria) {
        switch (categoria) {
            case "General Knowledge": return 9;
            case "Entertainment: Books": return 10;
            case "Entertainment: Film": return 11;
            case "Entertainment: Music": return 12;
            case "Entertainment: Musicals & Theatres": return 13;
            case "Entertainment: Television": return 14;
            case "Entertainment: Video Games": return 15;
            case "Entertainment: Board Games": return 16;
            case "Science & Nature": return 17;
            case "Science: Computers": return 18;
            case "Science: Mathematics": return 19;
            case "Mythology": return 20;
            case "Sports": return 21;
            case "Geography": return 22;
            case "History": return 23;
            default: return 9; // General Knowledge
        }
    }

}
