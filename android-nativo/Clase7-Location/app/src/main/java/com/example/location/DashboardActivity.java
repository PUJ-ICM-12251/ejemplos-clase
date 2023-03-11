package com.example.location;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.location.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {
    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void loadLocationSingleUse(View view) {
        Intent intent = new Intent(DashboardActivity.this, LocationSingleUseActivity.class);
        startActivity(intent);
    }

    public void loadLocationAware(View view) {
        Intent intent = new Intent(DashboardActivity.this, LocationAwareActivity.class);
        startActivity(intent);
    }
}