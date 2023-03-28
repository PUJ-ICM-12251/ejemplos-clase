package com.example.mapas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.mapas.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void loadMapNYC(View view) {
        Intent intent = new Intent(this, MapsNYCActivity.class);
        startActivity(intent);
    }

    public void loadMapBOG(View view) {
        Intent intent = new Intent(this, MapsBOGActivity.class);
        startActivity(intent);
    }
}