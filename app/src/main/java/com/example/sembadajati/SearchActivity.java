package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sembadajati.databinding.ActivitySearchBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        int color = ContextCompat.getColor(this, R.color.white);
        binding.navigationBar.btnNavbarSearch.setColorFilter(color);

        binding.navigationBar.btnNavbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navigationBar.btnNavbarCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, CartActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.navigationBar.btnNavbarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        binding.searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchQuery = binding.searchBar.getText().toString();

                    binding.loading.setVisibility(View.VISIBLE);
                    binding.loading.startShimmer();

                    SearchProcess(searchQuery);

                    return true;
                }
                return false;
            }
        });

        binding.topNavigationBar.btnTopNavbarLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, FavouriteActivity.class));
                overridePendingTransition(0, 0);
            }
        });

    }

    private void SearchProcess(String productName) {
        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseProduct> call = service.ProductGetName(productName);

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
                Toast.makeText(SearchActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}