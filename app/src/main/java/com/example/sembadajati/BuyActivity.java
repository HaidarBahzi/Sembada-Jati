package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.sembadajati.databinding.ActivityBuyBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends AppCompatActivity {

    private ActivityBuyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityBuyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String productId = intent.getStringExtra("product_id");

        Process(productId);
    }

    private void Process(String productId) {
        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBuy> callBuy = service.ProductBuy(productId);
        Call<ResponseProduct> callProduct = service.ProductGetId(productId);

        callProduct.enqueue(new Callback<ResponseProduct>() {
            @Override
            public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {
                ResponseProduct product = response.body();

                List<Product> osas = product.getResult();

                int price = Integer.parseInt(osas.get(0).getProductPrice());
                String title = osas.get(0).getProductTitle();
                String poster = osas.get(0).getProductPoster();

                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(price);

                String imagePath = getResources().getString(R.string.image_path);
                String rupiah = getResources().getString(R.string.harga);

                Picasso.get().load(imagePath + poster).into(binding.imageProduct);
                binding.priceProduct.setText(rupiah + formattedPrice);
                binding.textProduct.setText(title);
            }

            @Override
            public void onFailure(Call<ResponseProduct> call, Throwable t) {

            }
        });

    }
}