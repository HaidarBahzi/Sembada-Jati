package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.sembadajati.databinding.ActivityFavouriteBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteActivity extends AppCompatActivity {

    private ActivityFavouriteBinding binding;
    Preference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        pref = new Preference(this);

        binding.loading.startShimmer();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();

                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        String userEmail = pref.getUserLogin();

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseProductFavourite> call = service.ProductFavGet(userEmail);

        call.enqueue(new Callback<ResponseProductFavourite>() {
            @Override
            public void onResponse(Call<ResponseProductFavourite> call, Response<ResponseProductFavourite> response) {
                binding.loading.stopShimmer();
                binding.loading.setVisibility(View.GONE);
                List<CartFav> productFav = response.body().getResult();
                ProductFavSingleton.getInstance().setProductFav(productFav);

                if (productFav == null) {
                    Toast.makeText(FavouriteActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                } else {
                    FavouriteAdapter adapter = new FavouriteAdapter(productFav);
                    binding.rvProductFav.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseProductFavourite> call, Throwable t) {
                Toast.makeText(FavouriteActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });


        binding.navbar.btnNavbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navbar.btnNavbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, SearchActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navbar.btnNavbarCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, CartActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navbar.btnNavbarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }
}