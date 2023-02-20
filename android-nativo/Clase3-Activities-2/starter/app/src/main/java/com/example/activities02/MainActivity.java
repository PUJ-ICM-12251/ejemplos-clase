package com.example.activities02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.activities02.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // Se agrega una variable entera en la clase MainActivity para guardar la cantidad de clics:


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // Llama al método inflate() estático incluido en la clase de vinculación generada. Esto crea una instancia de la clase de vinculación para la actividad que se usará.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // llama al método getRoot() para obtener una referencia a la vista raíz
        View view = binding.getRoot();
        // Pasa la vista raíz a setContentView() para que sea la vista activa en la pantalla.
        setContentView(view);

        // Se incrementa el contador de clics en el listener del botón y se muestra la información en un log:



        // Se agrega un listener para el segundo botón y crea un Intent para lanzar la nueva actividad.
        // El dato del campo de texto se envía como un extra o con un objeto Bundle
        // Intent intent = new Intent(MainActivity.this, Activity2.class);
        // intent.putExtra("text", text);
        // startActivity(intent);
    }
}