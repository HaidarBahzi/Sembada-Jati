package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.sembadajati.databinding.ActivityResetBinding;

public class ResetActivity extends AppCompatActivity {

    private ActivityResetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityResetBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetActivity.this, LoginActivity.class));
            }
        });
    }
}