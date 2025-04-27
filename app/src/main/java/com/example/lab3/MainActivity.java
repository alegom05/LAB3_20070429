package com.example.lab3;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategoria, spinnerDificultad;
    EditText editCantidad;
    Button btnCheckConexion, btnComenzar;

    boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerDificultad = findViewById(R.id.spinnerDificultad);
        editCantidad = findViewById(R.id.editCantidad);
        btnCheckConexion = findViewById(R.id.btnCheckConexion);
        btnComenzar = findViewById(R.id.btnComenzar);

        //Categorías
        String[] categorias = {
                "General Knowledge",
                "Entertainment: Books",
                "Entertainment: Film",
                "Entertainment: Music",
                "Entertainment: Musicals & Theatres",
                "Entertainment: Television",
                "Entertainment: Video Games",
                "Entertainment: Board Games",
                "Science & Nature",
                "Science: Computers",
                "Science: Mathematics",
                "Mythology",
                "Sports",
                "Geography",
                "History"
        };
        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        spinnerCategoria.setAdapter(adapterCategoria);

        // Dificultades
        String[] dificultades = {"fácil", "medio", "difícil"};
        ArrayAdapter<String> adapterDificultad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dificultades);
        spinnerDificultad.setAdapter(adapterDificultad);

        btnCheckConexion.setOnClickListener(v -> {
            if (validarCampos()) {
                if (hayConexionInternet()) {
                    Toast.makeText(this, "¡Conexión Exitosa!", Toast.LENGTH_SHORT).show();
                    isConnected = true;
                    btnComenzar.setEnabled(true);
                } else {
                    Toast.makeText(this, "¡No hay Conexión a Internet!", Toast.LENGTH_SHORT).show();
                    isConnected = false;
                }
            }
        });

        btnComenzar.setOnClickListener(v -> {
            if (isConnected) {
                String categoria = spinnerCategoria.getSelectedItem().toString();
                String dificultadEsp = spinnerDificultad.getSelectedItem().toString();
                int cantidad = Integer.parseInt(editCantidad.getText().toString());

                // Traducción manual de dificultad
                String dificultad;
                switch (dificultadEsp.toLowerCase()) {
                    case "fácil":
                        dificultad = "easy";
                        break;
                    case "medio":
                        dificultad = "medium";
                        break;
                    case "difícil":
                        dificultad = "hard";
                        break;
                    default:
                        dificultad = "easy";
                        break;
                }

                Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
                intent.putExtra("categoria", categoria);
                intent.putExtra("dificultad", dificultad); // Ahora sí correctamente en inglés
                intent.putExtra("cantidad", cantidad);
                startActivity(intent);
            }
        });


    }

    private boolean validarCampos() {
        String cantidadTexto = editCantidad.getText().toString();
        if (cantidadTexto.isEmpty() || Integer.parseInt(cantidadTexto) <= 0) {
            Toast.makeText(this, "Ingrese una cantidad positiva", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinnerCategoria.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione una categoría", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinnerDificultad.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione una dificultad", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //Método para conectarse a internet
    private boolean hayConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());
    }
}
