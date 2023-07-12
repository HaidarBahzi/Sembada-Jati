package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.sembadajati.databinding.ActivityCartBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;

    Preference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        pref = new Preference(this);

        int color = ContextCompat.getColor(this, R.color.white);
        binding.navbar.btnNavbarCart.setColorFilter(color);

        binding.navbar.btnNavbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navbar.btnNavbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, SearchActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navbar.btnNavbarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.topNavbar.btnTopNavbarLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, FavouriteActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        String userEmail = pref.getUserLogin();

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseProductFavourite> call = service.ProductCartGet(userEmail);

        call.enqueue(new Callback<ResponseProductFavourite>() {
            @Override
            public void onResponse(Call<ResponseProductFavourite> call, Response<ResponseProductFavourite> response) {
                binding.loading.stopShimmer();
                binding.loading.setVisibility(View.GONE);
                List<CartFav> productCart = response.body().getResult();

                if (productCart == null) {
                    Toast.makeText(CartActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                } else {
                    CartAdapter adapter = new CartAdapter(productCart);
                    binding.rvProductCart.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseProductFavourite> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}