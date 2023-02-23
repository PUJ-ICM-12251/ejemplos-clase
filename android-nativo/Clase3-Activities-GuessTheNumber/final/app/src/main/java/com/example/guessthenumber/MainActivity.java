package com.example.guessthenumber;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guessthenumber.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    int RandomNumber;
    int tries = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Se genera el número aleatorio secreto entre 0 y 1000
        Random rand = new Random();
        RandomNumber = rand.nextInt(1000);

        // Se bbtiene una referencia al botón
        Button botonAdivinar = binding.button;

        // Se agrega un listener OnClickListener al botón
        botonAdivinar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                adivinarNumero();
            }
        });
    }

    // Función que verifica el número ingresado
    public void adivinarNumero() {
        EditText editTextNumero = binding.editText1;
        int numeroIngresado = Integer.parseInt(editTextNumero.getText().toString());
        tries++;
        if (numeroIngresado == RandomNumber) {
            Toast.makeText(this, "¡ Adivinaste el número en " + tries + " intentos 🥳!", Toast.LENGTH_LONG).show();
        } else if (numeroIngresado < RandomNumber) {
            Toast.makeText(this, "El número ingresado es menor.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "El número ingresado es mayor.", Toast.LENGTH_SHORT).show();
        }
    }
}