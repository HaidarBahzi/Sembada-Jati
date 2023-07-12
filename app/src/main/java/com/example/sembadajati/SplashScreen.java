package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.example.sembadajati.databinding.ActivitySplashScreenBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;
    private Preference preference;
    private Animation showPopUp, fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showPopUp = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        preference = new Preference(this);

        Glide.with(SplashScreen.this)
                .load(R.drawable.splash)
                .placeholder(R.drawable.splash)
                .into(binding.imageSplash);
        binding.imageSplash.startAnimation(fade);

        binding.dialogError.getRoot().setVisibility(View.GONE);
        Glide.with(this)
                .load(R.drawable.error_network)
                .placeholder(R.drawable.error_network)
                .into(binding.dialogError.dialogErrorNetworkImage);

        if (preference.getSessionLogin()) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }, 2000);
        } else {
            isUserConnected();
        }

        binding.dialogError.dialogErrorNetworkButton.setOnClickListener(v -> {
            finish();

            new Handler().postDelayed(() -> {
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }, 1000);
        });
    }

    private void isUserConnected() {
        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseUserNetwork> call = service.UserNetwork();

        call.enqueue(new Callback<ResponseUserNetwork>() {
            @Override
            public void onResponse(Call<ResponseUserNetwork> call, Response<ResponseUserNetwork> response) {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(SplashScreen.this, Onboarding.class));
                    finish();
                }, 2000);
            }

            @Override
            public void onFailure(Call<ResponseUserNetwork> call, Throwable t) {
                new Handler().postDelayed(() -> {
                    binding.imageSplash.setAlpha(0f);
                    binding.parentContainer.setBackgroundColor(getColor(R.color.light_grey));
                    binding.dialogError.getRoot().startAnimation(showPopUp);
                    binding.dialogError.getRoot().setVisibility(View.VISIBLE);
                }, 3500);
            }
        });
    }
}
