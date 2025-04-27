package com.example.lab3;

import java.util.ArrayList;

//Bean de Pregunta
public class Pregunta {
    private String pregunta;
    private String respuestaCorrecta;
    private ArrayList<String> opciones;

    public Pregunta(String pregunta, String respuestaCorrecta, ArrayList<String> opciones) {
        this.pregunta = pregunta;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = opciones;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public ArrayList<String> getOpciones() {
        return opciones;
    }
}
