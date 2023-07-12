package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sembadajati.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Preference preference;
    private Animation showPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showPopUp = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        preference = new Preference(this);
        if (preference.getSessionLogin()) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        binding.createAccount.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        binding.loadingOverlay.getRoot().setVisibility(View.GONE);
        Glide.with(RegisterActivity.this)
                .load(R.drawable.loading_anim)
                .placeholder(R.drawable.loading_anim)
                .into(binding.loadingOverlay.gifShow);

        binding.dialogError.getRoot().setVisibility(View.GONE);
        Glide.with(RegisterActivity.this)
                .load(R.drawable.error_network)
                .placeholder(R.drawable.error_network)
                .into(binding.dialogError.dialogErrorNetworkImage);

        binding.inputSubmit.setOnClickListener(v -> {
            binding.container.setAlpha(0);
            binding.loadingOverlay.getRoot().setVisibility(View.VISIBLE);
            registerProcess();
        });

        binding.dialogError.dialogErrorNetworkButton.setOnClickListener(v -> {
            finish();

            new Handler().postDelayed(() -> {
                startActivity(new Intent(RegisterActivity.this, SplashScreen.class));
                overridePendingTransition(0, 0);
            }, 1000);
        });
    }

    private void registerProcess() {
        String namaLengkap, email, nomorHp, password, jsonBody;

        namaLengkap = binding.inputName.getText().toString();
        email = binding.inputEmail.getText().toString();
        nomorHp = binding.inputPhone.getText().toString();
        password = binding.inputPassword.getText().toString();

        JSONObject obj = new JSONObject();
        try {
            obj.put("nama_lengkap", namaLengkap);
            obj.put("email", email);
            obj.put("nomor_hp", nomorHp);
            obj.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        jsonBody = obj.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseUserRegister> call = service.UserRegister(requestBody);

        call.enqueue(new Callback<ResponseUserRegister>() {
            @Override
            public void onResponse(Call<ResponseUserRegister> call, Response<ResponseUserRegister> response) {
                binding.loadingOverlay.getRoot().setVisibility(View.GONE);
                binding.container.setAlpha(1);

                ResponseUserRegister responseBody = response.body();
                String status = responseBody.getStatus();
                String result = responseBody.getResult();

                if (status.equals("success")) {
                    Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    binding.inputResponse.setText(result);
                }
            }

            @Override
            public void onFailure(Call<ResponseUserRegister> call, Throwable t) {
                binding.loadingOverlay.getRoot().setVisibility(View.GONE);
                binding.container.setAlpha(1);
                binding.parentContainer.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.light_grey));
                binding.dialogError.getRoot().startAnimation(showPopUp);
                binding.dialogError.getRoot().setVisibility(View.VISIBLE);
            }
        });
    }
}
