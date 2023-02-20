package com.example.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.layouts.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String COUNTRIES_FILE = "countries.json";
    Spinner countrySpinner;
    ListView countryList;
    JSONArray countries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        countrySpinner = binding.spinner;
        countryList = binding.countryList;

        // Se crea una lista vacía de "cadenas de texto" llamada "countriesArray" que se utilizará para almacenar los nombres de los países.
        // A partir del objeto JSON devuelto por la función "loadCountriesByJson()", se obtiene un array JSON llamado "countries" que contiene los detalles de cada país.
        // Se itera a través de cada elemento del array JSON "countries" y se agrega el nombre de cada país en la lista "countriesArray".
        List<String> countriesArray = new ArrayList<String>();
        try {
            JSONObject jsonFile = loadCountriesByJson();
            countries = jsonFile.getJSONArray("Countries");
            for (int i = 0; i < countries.length(); i++) {
                countriesArray.add(countries.getJSONObject(i).getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Spinner
        // Con el adapter se llena el spinner con los nombres de cada país utilizando la lista "countriesArray"
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, countriesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        // Listener para el Spinner
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Aquí se obtiene el valor del item seleccionado
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Se muestra en un Toast el valor seleccionado
                Toast.makeText(getBaseContext(),String.format("País seleccionado: %S", selectedItem),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no se ha seleccionado ningún item en el spinner
            }
        });

        // ListView
        // Con el adapter se llena el ListView con los nombres de cada país utilizando la lista "countriesArray"
        ArrayAdapter<String> adapterListView = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, countriesArray);
        countryList.setAdapter(adapterListView);

        // Listener para el ListView
        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Aquí se obtiene el valor del item seleccionado
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Se llama a la actividad 2 y se envían los detalles del país seleccionado utilizando la posición y el array JSON "countries"
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                try {
                    intent.putExtra("paisSeleccionado", countries.getJSONObject(position).toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);

            }
        });
    }

    public String loadJSONFromAsset(String assetName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open("paises.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public JSONObject loadCountriesByJson() throws JSONException {
        return new JSONObject(loadJSONFromAsset(COUNTRIES_FILE));
    }
}