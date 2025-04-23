package com.example.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

/*En el caso de MyGameActivity, hice uso de la IA para facilitar
los métodos. Voy a proceder a explicarlos*/
public class MyGameActivity extends AppCompatActivity {

    private TextView tvTema, tvIntentos;
    private GridLayout gridPalabras;
    private Button btnNuevoJuego;
    private List<String> oracionCorrecta;
    private List<String> palabrasSeleccionadas = new ArrayList<>();
    private int intentoActual = 0;
    private final int MAX_INTENTOS = 3;
    private long startTime;
    private String tema;
    private int numeroJuego;
    private List<Button> botonesSeleccionados = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        tvTema = findViewById(R.id.tvTema); // Ahora mostrará "Juego N"
        tvIntentos = findViewById(R.id.tvIntentos);
        gridPalabras = findViewById(R.id.gridPalabras);
        btnNuevoJuego = findViewById(R.id.btnNuevoJuego);

        tema = getIntent().getStringExtra("tema");

        // Uso de SharedPreferences
        SharedPreferences prefs = getSharedPreferences("stats", MODE_PRIVATE);
        numeroJuego = prefs.getInt("numero_juego", 1); // por defecto 1
        tvTema.setText("Juego " + numeroJuego);

        // Inicio de cronómetro
        startTime = System.currentTimeMillis();

        generarPalabras();

        btnNuevoJuego.setOnClickListener(v -> {
            SharedPreferences p = getSharedPreferences("stats", MODE_PRIVATE);
            Set<String> historial = new HashSet<>(p.getStringSet("historial", new HashSet<>()));
            historial.add("Canceló – Juego " + numeroJuego + " – Tema: " + tema);
            p.edit().putStringSet("historial", historial).apply();

            // Reiniciar
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnEstadisticas = findViewById(R.id.buttonStats);
        btnEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyStatisticsActivity.class);
            startActivity(intent);
        });
    }

    /* Método para generar las palabras de manera aleatoria */
    private void generarPalabras() {
        oracionCorrecta = obtenerOracionAleatoria(tema);
        List<String> palabrasDesordenadas = new ArrayList<>(oracionCorrecta);
        Collections.shuffle(palabrasDesordenadas);

        gridPalabras.removeAllViews();

        for (String palabra : palabrasDesordenadas) {
            Button btn = new Button(this);
            btn.setText("");
            btn.setTag(palabra);

            // Estilo con márgenes y centrado
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(16, 16, 16, 16);
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setGravity(Gravity.CENTER);
            btn.setLayoutParams(params);
            btn.setBackgroundColor(Color.parseColor("#DDDDDD"));

            btn.setOnClickListener(v -> manejarSeleccion(btn));
            gridPalabras.addView(btn);
        }
    }

    // Maneja los aciertos de palabras.
    private void manejarSeleccion(Button btn) {
        String palabra = btn.getTag().toString();

        if (palabra.equals(oracionCorrecta.get(palabrasSeleccionadas.size()))) {
            palabrasSeleccionadas.add(palabra);
            botonesSeleccionados.add(btn);

            btn.setText(palabra); // Uso de tag.
            btn.setTextColor(Color.WHITE);
            btn.setEnabled(false);
            btn.setBackgroundColor(Color.parseColor("#4CAF50"));

            btn.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction(() ->
                    btn.animate().scaleX(1f).scaleY(1f).setDuration(150)
            ).start();

            if (palabrasSeleccionadas.size() == oracionCorrecta.size()) {
                mostrarResultado(true);
            }
        }

        else {
            intentoActual++;
            palabrasSeleccionadas.clear();
            tvIntentos.setText("Intentos: " + intentoActual + " / " + MAX_INTENTOS);
            Toast.makeText(this, "Palabra incorrecta. Intentos restantes: " + (MAX_INTENTOS - intentoActual), Toast.LENGTH_SHORT).show();

            // Reiniciar botones
            for (int i = 0; i < gridPalabras.getChildCount(); i++) {
                View child = gridPalabras.getChildAt(i);
                if (child instanceof Button) {
                    Button boton = (Button) child;
                    boton.setEnabled(true);
                    boton.setBackgroundColor(Color.parseColor("#DDDDDD"));
                    boton.setText(""); // Ocultar todas las palabras (incluso las correctas)
                }
            }

            palabrasSeleccionadas.clear();
            botonesSeleccionados.clear();

            if (intentoActual >= MAX_INTENTOS) {
                mostrarResultado(false);
            }
        }
    }

    /* Aquí se controla el modal que sale cuando el juego
    * ha acabado*/
    private void mostrarResultado(boolean gano) {
        long tiempo = (System.currentTimeMillis() - startTime) / 1000;

        String mensaje = "Juego " + numeroJuego + "\n" +
                (gano ? "Ganó" : "Perdió") + "\n" +
                "Intentos usados: " + intentoActual;

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

        // Guardar resultado en historial
        String resultado = "Juego " + numeroJuego + " – " +
                (gano ? "Ganó" : "Perdió") +
                " – Intentos: " + intentoActual +
                " – Tiempo: " + tiempo + "s";

        SharedPreferences prefs = getSharedPreferences("stats", MODE_PRIVATE);
        Set<String> historial = new HashSet<>(prefs.getStringSet("historial", new HashSet<>()));
        historial.add(resultado);

        // Guardar nuevo número de juego
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("historial", historial);
        editor.putInt("numero_juego", numeroJuego + 1);
        editor.apply();

        for (int i = 0; i < gridPalabras.getChildCount(); i++) {
            gridPalabras.getChildAt(i).setEnabled(false);
        }
    }

    /*Aquí se manejan las oraciones de las que consta
    * el juego*/
    private List<String> obtenerOracionAleatoria(String tema) {
        Random rand = new Random();
        String frase;

        switch (tema) {
            case "Software":
                frase = rand.nextBoolean() ?
                        "La fibra óptica envía datos a gran velocidad evitando cualquier interferencia eléctrica" :
                        "Los amplificadores EDFA mejoran la señal óptica en redes de larga distancia";
                break;
            case "Ciberseguridad":
                frase = rand.nextBoolean() ?
                        "Una VPN encripta tu conexión para navegar de forma anónima y segura" :
                        "El ataque DDoS satura servidores con tráfico falso y causa caídas masivas";
                break;
            case "Ópticas":
            default:
                frase = rand.nextBoolean() ?
                        "Los fragments reutilizan partes de pantalla en distintas actividades de la app" :
                        "Los intents permiten acceder a apps como la cámara o WhatsApp directamente";
                break;
        }

        return Arrays.asList(frase.split(" "));
    }
}
