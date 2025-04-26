package com.example.lab3;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab2.R;
import com.example.lab2.TriviaActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView categoryDropdown, difficultyDropdown;
    TextInputEditText amountInput;
    Button checkConnectionButton, startButton;

    String[] categorias = {"Cultura General", "Libros", "Películas", "Música", "Computación", "Matemática", "Deportes", "Historia"};
    String[] dificultades = {"fácil", "medio", "difícil"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryDropdown = findViewById(R.id.categoryDropdown);
        difficultyDropdown = findViewById(R.id.difficultyDropdown);
        amountInput = findViewById(R.id.amountInput);
        checkConnectionButton = findViewById(R.id.checkConnectionButton);
        startButton = findViewById(R.id.startButton);

        // Adaptadores para los dropdowns
        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categorias);
        categoryDropdown.setAdapter(adapterCategoria);

        ArrayAdapter<String> adapterDificultad = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dificultades);
        difficultyDropdown.setAdapter(adapterDificultad);

        startButton.setEnabled(false);

        checkConnectionButton.setOnClickListener(v -> {
            if (validarEntradas()) {
                if (hayConexionInternet()) {
                    Toast.makeText(this, "¡Conexión exitosa!", Toast.LENGTH_SHORT).show();
                    startButton.setEnabled(true);
                } else {
                    Toast.makeText(this, "No tienes conexión a Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
            intent.putExtra("categoria", categoryDropdown.getText().toString());
            intent.putExtra("cantidad", Integer.parseInt(amountInput.getText().toString()));
            intent.putExtra("dificultad", difficultyDropdown.getText().toString());
            startActivity(intent);
        });
    }

    private boolean validarEntradas() {
        String categoria = categoryDropdown.getText().toString();
        String dificultad = difficultyDropdown.getText().toString();
        String cantidadStr = amountInput.getText().toString();

        if (categoria.isEmpty()) {
            categoryDropdown.setError("Seleccione una categoría");
            return false;
        }

        if (dificultad.isEmpty()) {
            difficultyDropdown.setError("Seleccione una dificultad");
            return false;
        }

        if (cantidadStr.isEmpty()) {
            amountInput.setError("Ingrese una cantidad");
            return false;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                amountInput.setError("Debe ser mayor que 0");
                return false;
            }
        } catch (NumberFormatException e) {
            amountInput.setError("Ingrese un número válido");
            return false;
        }

        return true;
    }

    private boolean hayConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
