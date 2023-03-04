package com.example.permissions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.permissions.databinding.ActivitySelectorBinding;

public class ActivitySelector extends AppCompatActivity {
    private ActivitySelectorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void loadCamera(View view) {
        Intent intent = new Intent(ActivitySelector.this, MainActivity.class);
        startActivity(intent);
    }

    public void loadCameraX(View view) {
        Intent intent = new Intent(ActivitySelector.this, CameraX.class);
        startActivity(intent);
    }
}