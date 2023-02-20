package com.example.activities01;

// Importamos las clases de los paquetes necesarios
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Instancias
    private EditText mEditText;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asignación de las vistas de la UI a los objetos
        mButton = (Button) findViewById(R.id.button);
        mEditText = (EditText) findViewById(R.id.editText);

        // Listener de clic para el botón
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = mEditText.getText().toString();
                String saludo = getString(R.string.saludo, nombre);

                Toast.makeText(MainActivity.this, saludo, Toast.LENGTH_SHORT).show();
            }
        });

    }
}