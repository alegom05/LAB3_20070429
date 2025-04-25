package com.example.lab2.bean;

import java.util.ArrayList;

public class Pregunta {

        private String texto;
        private ArrayList<String> opciones;
        private String respuestaCorrecta;

        public Pregunta(String texto, ArrayList<String> opciones, String respuestaCorrecta) {
            this.texto = texto;
            this.opciones = opciones;
            this.respuestaCorrecta = respuestaCorrecta;
        }

        public String getTexto() {
            return texto;
        }

        public ArrayList<String> getOpciones() {
            return opciones;
        }

        public String getRespuestaCorrecta() {
            return respuestaCorrecta;
        }


}
