package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.sembadajati.databinding.ActivityProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preference = new Preference(this);

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseUserGet> call = service.UserGet(preference.getUserLogin());

        call.enqueue(new Callback<ResponseUserGet>() {
            @Override
            public void onResponse(Call<ResponseUserGet> call, Response<ResponseUserGet> response) {
                ResponseUserGet responseUserGet = response.body();

                List<User> osas = responseUserGet.getResult();

                binding.username.setText(osas.get(0).getNamaLengkap());
            }

            @Override
            public void onFailure(Call<ResponseUserGet> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        int color = ContextCompat.getColor(this, R.color.white);
        binding.navigationBar.btnNavbarProfile.setColorFilter(color);

        binding.navigationBar.btnNavbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navigationBar.btnNavbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navigationBar.btnNavbarCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CartActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutProcess();
                preference.clearAll();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });

        binding.topNavigationBar.btnTopNavbarLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FavouriteActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProfile();
            }
        });

    }

    private void ChangeProfile() {

        String newName, newEmail, newPhone, newPassword, newChange;

        newName = binding.username.getText().toString();
        newEmail = binding.emailInput.getText().toString();
        newPhone = binding.phoneInput.getText().toString();
        newPassword = binding.newPassword.getText().toString();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email", newEmail);
            jsonObject.put("nama_lengkap", newName);
            jsonObject.put("nomor_hp", newPhone);
            jsonObject.put("password", newPassword);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        newChange = jsonObject.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), newChange);

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseUserLogin> call = service.UserChangeProfile(requestBody);
        call.enqueue(new Callback<ResponseUserLogin>() {
            @Override
            public void onResponse(Call<ResponseUserLogin> call, Response<ResponseUserLogin> response) {
                Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                recreate();
            }

            @Override
            public void onFailure(Call<ResponseUserLogin> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LogoutProcess() {

        String emailLogin, emailLoginJson;

        emailLogin = String.valueOf(preference.getUserLogin());

        JSONObject object = new JSONObject();

        try {
            object.put("email", emailLogin);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        emailLoginJson = object.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), emailLoginJson);

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseUserLogin> call = service.UserLogout(requestBody);

        call.enqueue(new Callback<ResponseUserLogin>() {
            @Override
            public void onResponse(Call<ResponseUserLogin> call, Response<ResponseUserLogin> response) {
                String status, message;

                status = response.body().getStatus();
                message = response.body().getMessage();

                if (status.equals("success")) {
                    preference.clearAll();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserLogin> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}