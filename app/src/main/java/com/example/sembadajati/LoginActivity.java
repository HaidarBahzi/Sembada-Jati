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
import com.example.sembadajati.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private Animation showPopUp;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showPopUp = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        preference = new Preference(this);
        if (preference.getSessionLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setupViews();
    }

    private void setupViews() {
        binding.createAccount.setOnClickListener(this);
        binding.forgetPassword.setOnClickListener(this);
        binding.inputSubmit.setOnClickListener(this);
        binding.dialogError.dialogErrorNetworkButton.setOnClickListener(this);
        binding.dialogBlocked.dialogErrorBlockedButton.setOnClickListener(this);

        binding.loadingOverlay.getRoot().setVisibility(View.GONE);
        Glide.with(LoginActivity.this)
                .load(R.drawable.loading_anim)
                .placeholder(R.drawable.loading_anim)
                .into(binding.loadingOverlay.gifShow);

        binding.dialogError.getRoot().setVisibility(View.GONE);
        Glide.with(this)
                .load(R.drawable.error_network)
                .placeholder(R.drawable.error_network)
                .into(binding.dialogError.dialogErrorNetworkImage);

        binding.dialogBlocked.getRoot().setVisibility(View.GONE);
        Glide.with(this)
                .load(R.drawable.error_blocked)
                .placeholder(R.drawable.error_blocked)
                .into(binding.dialogBlocked.dialogErrorBlockedImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_account:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.forget_password:
                startActivity(new Intent(LoginActivity.this, ResetActivity.class));
                break;
            case R.id.input_submit:
                binding.container.setAlpha(0);
                binding.loadingOverlay.getRoot().setVisibility(View.VISIBLE);
                loginProcess();
                break;
            case R.id.dialog_error_network_button:
                finish();
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(LoginActivity.this, SplashScreen.class));
                    overridePendingTransition(0, 0);
                }, 1000);
                break;
            case R.id.dialog_error_blocked_button:
                binding.dialogBlocked.getRoot().setVisibility(View.GONE);
                binding.parentContainer.setBackgroundColor(getColor(R.color.white));
                binding.inputEmail.setText("");
                binding.inputPassword.setText("");
                binding.container.setAlpha(1);
                break;
        }
    }

    private void loginProcess() {
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();

        JSONObject obj = new JSONObject();

        try {
            obj.put("email", email);
            obj.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String jsonBody = obj.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseUserLogin> call = service.UserLogin(requestBody);

        call.enqueue(new Callback<ResponseUserLogin>() {
            @Override
            public void onResponse(Call<ResponseUserLogin> call, Response<ResponseUserLogin> response) {
                binding.loadingOverlay.getRoot().setVisibility(View.GONE);
                binding.container.setAlpha(1);

                String status = response.body().getStatus();
                String message = response.body().getMessage();

                if (status.equals("success")) {
                    preference.setUserLogin(email);
                    preference.setSessionLogin(true);
                    binding.inputResponse.setText("");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else if (status.equals("blocked")) {
                    binding.container.setAlpha(0);
                    binding.parentContainer.setBackgroundColor(getColor(R.color.light_grey));
                    binding.inputResponse.setText("");
                    binding.dialogBlocked.getRoot().startAnimation(showPopUp);
                    binding.dialogBlocked.getRoot().setVisibility(View.VISIBLE);
                } else {
                    binding.inputResponse.setText(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseUserLogin> call, Throwable t) {
                binding.loadingOverlay.getRoot().setVisibility(View.GONE);
                binding.container.setAlpha(1);
                binding.parentContainer.setBackgroundColor(getColor(R.color.light_grey));
                binding.dialogError.getRoot().startAnimation(showPopUp);
                binding.dialogError.getRoot().setVisibility(View.VISIBLE);
            }
        });
    }
}
