package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.sembadajati.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preference = new Preference(this);

        binding.loading.startShimmer();

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseProduct> call = service.ProductGet();

        call.enqueue(new Callback<ResponseProduct>() {

            @Override
            public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {
                binding.loading.stopShimmer();
                binding.loading.setVisibility(View.GONE);
                List<Product> product = response.body().getResult();
                ProductAdapter adapter = new ProductAdapter(product);
                binding.rvProduct.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseProduct> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        int color = ContextCompat.getColor(this, R.color.white);
        binding.navigationBar.btnNavbarHome.setColorFilter(color);

        binding.navigationBar.btnNavbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navigationBar.btnNavbarCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navigationBar.btnNavbarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.categoryMenu.toggleChair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = binding.categoryMenu.toggleChair.isChecked();

                if (isChecked) {
                    binding.categoryMenu.toggleTable.setChecked(false);
                    SortCategory("Kursi");
                } else {
                    binding.categoryMenu.toggleTable.setChecked(true);
                }
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();

                binding.categoryMenu.toggleChair.setChecked(false);
                binding.categoryMenu.toggleTable.setChecked(false);
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });



        binding.categoryMenu.toggleTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = binding.categoryMenu.toggleTable.isChecked();

                if (isChecked) {
                    binding.categoryMenu.toggleChair.setChecked(false);
                    SortCategory("Meja");
                } else {
                    binding.categoryMenu.toggleChair.setChecked(true);
                }
            }
        });

        binding.topNavigationBar.btnTopNavbarLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
                overridePendingTransition(0, 0);
            }
        });

    }

    private void SortCategory(String productCategory) {

        binding.loading.startShimmer();
        binding.loading.setVisibility(View.VISIBLE);
        binding.rvProduct.setVisibility(View.GONE);

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseProduct> call = service.ProductGetCategory(productCategory);

        call.enqueue(new Callback<ResponseProduct>() {
            @Override
            public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {
                binding.loading.stopShimmer();
                binding.loading.setVisibility(View.GONE);
                binding.rvProduct.setVisibility(View.VISIBLE);
                List<Product> product = response.body().getResult();
                ProductAdapter adapter = new ProductAdapter(product);
                binding.rvProduct.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseProduct> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Errror", Toast.LENGTH_SHORT).show();
            }
        });
    }

}