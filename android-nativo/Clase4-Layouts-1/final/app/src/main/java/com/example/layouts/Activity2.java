package com.example.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.layouts.databinding.Activity2Binding;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity2 extends AppCompatActivity {
    private Activity2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_2);

        binding = Activity2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Se recibe el String que contiene los detalles del país seleccionado y se guarda en la variable "paisSeleccionado"
        String paisSeleccionado = getIntent().getStringExtra("paisSeleccionado");

        // Se crea un objeto "JSONObject" llamado "pais" para almacenar los detalles el país seleccionado
        // Se utiliza solo el nombre "Name" del país para cargar en el WebView la página de wikipedia del país
        // Aquí se podrían utilizar los otros datos del país para mostrar más información
        try {
            JSONObject pais;
            pais = new JSONObject(paisSeleccionado);
            WebView myWebView = (WebView) binding.webView;
            myWebView.setWebViewClient(new WebViewClient());
            myWebView.loadUrl(String.format("https://en.wikipedia.org/wiki/%s",pais.getString("Name")));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
}