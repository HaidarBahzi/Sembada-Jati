package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.sembadajati.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    Preference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        pref = new Preference(this);

        Intent intent = getIntent();
        String productId = intent.getStringExtra("product_id");

        GetDetailProduct(productId);

        if (isProductFav(productId)) {
            binding.buttonFav.setImageDrawable(getDrawable(R.drawable.favorite_on));
        }

        binding.buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFavProduct(productId);
            }
        });

        binding.buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCartProduct(productId);
            }
        });

        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, BuyActivity.class);
                intent.putExtra("product_id", productId);
                startActivity(intent);
            }
        });
    }

    private boolean isProductFav(String productId) {
        List<CartFav> productFav = ProductFavSingleton.getInstance().getProductFav();
        for (CartFav cartFav : productFav) {
            if (cartFav.getProductId().equals(productId)) {
                return true;
            }
        }
        return false;
    }

    private void AddFavProduct(String Id) {
        String jsonBody;

        JSONObject obj = new JSONObject();

        try {
            obj.put("user_email", pref.getUserLogin());
            obj.put("product_id", Id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        jsonBody = obj.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseProductFavouriteDelete> call = service.ProductFavAdd(requestBody);

        call.enqueue(new Callback<ResponseProductFavouriteDelete>() {
            @Override
            public void onResponse(Call<ResponseProductFavouriteDelete> call, Response<ResponseProductFavouriteDelete> response) {
                Toast.makeText(DetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseProductFavouriteDelete> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error To Add", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AddCartProduct(String Id) {
        String jsonBody;

        JSONObject obj = new JSONObject();

        try {
            obj.put("user_email", pref.getUserLogin());
            obj.put("product_id", Id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        jsonBody = obj.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseProductFavouriteDelete> call = service.ProductCartAdd(requestBody);

        call.enqueue(new Callback<ResponseProductFavouriteDelete>() {
            @Override
            public void onResponse(Call<ResponseProductFavouriteDelete> call, Response<ResponseProductFavouriteDelete> response) {
                Toast.makeText(DetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseProductFavouriteDelete> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error To Add", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetDetailProduct(String id) {
        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseProduct> call = service.ProductGetId(id);

        call.enqueue(new Callback<ResponseProduct>() {
            @Override
            public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {
                ResponseProduct product = response.body();

                List<Product> osas = product.getResult();

                String desc = osas.get(0).getProductDesc();
                int price = Integer.parseInt(osas.get(0).getProductPrice());
                String title = osas.get(0).getProductTitle();
                String count = osas.get(0).getProductBuyCount();
                String poster = osas.get(0).getProductPoster();
                String availableCount = osas.get(0).getProductAvailableCount();
                String productCategory = osas.get(0).getProductCategory();
                int rating = Integer.parseInt(osas.get(0).getProductRate());

                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(price);

                String imagePath = getResources().getString(R.string.image_path);
                String rupiah = getResources().getString(R.string.harga);

                binding.productDetail.setText(desc);
                binding.productPrice.setText(rupiah + formattedPrice);
                binding.productTitle.setText(title);
                binding.productRatingSold.setText(rating + " | Terjual " + count);
                binding.productStockText.setText("Tersedia " + availableCount);

                Picasso.get()
                        .load(imagePath  + poster)
                        .placeholder(R.drawable.baseline_broken_image_24)
                        .error(R.drawable.baseline_broken_image_24)
                        .into(binding.productPoster);

            }

            @Override
            public void onFailure(Call<ResponseProduct> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Eror", Toast.LENGTH_SHORT).show();
            }
        });


    }
}