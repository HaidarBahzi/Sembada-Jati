package com.example.sembadajati;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartFav> localDataSet;

    public CartAdapter(List<CartFav> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favourite_item, viewGroup, false);

        return new CartAdapter.ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView cartName, cartPrice;
        final ImageButton cartDelete;
        final ImageView cartPoster;


        public ViewHolder(View view) {
            super(view);

            cartName = view.findViewById(R.id.fav_name);
            cartPrice = view.findViewById(R.id.fav_price);
            cartDelete = view.findViewById(R.id.button_fav_delete);
            cartPoster = view.findViewById(R.id.fav_poster);

        }
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder viewHolder, final int position) {

        CartFav cartItem = localDataSet.get(position);

        String cartId ,cartPosterUrl, cartTitle, cartPrice, imagePath, rupiahCurrency;
        int originalPrice;

        cartPosterUrl = cartItem.getProductPoster();
        cartTitle = cartItem.getProductTitle();
        originalPrice = Integer.parseInt(cartItem.getProductPrice());
        cartId = cartItem.getProductId();

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        cartPrice = decimalFormat.format(originalPrice);

        imagePath = viewHolder.itemView.getResources().getString(R.string.image_path);
        rupiahCurrency = viewHolder.itemView.getResources().getString(R.string.harga);

        viewHolder.cartName.setText(cartTitle);
        viewHolder.cartPrice.setText( rupiahCurrency + cartPrice );

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("product_id", cartId);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

        viewHolder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject obj = new JSONObject();

                try {
                    obj.put("delete_id", cartItem.getId());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                String jsonBody = obj.toString();

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

                ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
                Call<ResponseProductFavouriteDelete> call = service.ProductCartDelete(requestBody);


                call.enqueue(new Callback<ResponseProductFavouriteDelete>() {
                    @Override
                    public void onResponse(Call<ResponseProductFavouriteDelete> call, Response<ResponseProductFavouriteDelete> response) {
                        String message = response.body().getMessage();
                        String status = response.body().getStatus();

                        if (status.equals("success")){
                            Toast.makeText(viewHolder.itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                            localDataSet.remove(viewHolder.getAdapterPosition());
                            notifyItemRemoved(viewHolder.getAdapterPosition());
                        } else {
                            Toast.makeText(viewHolder.itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseProductFavouriteDelete> call, Throwable t) {
                        Toast.makeText(viewHolder.itemView.getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Picasso.get()
                .load(imagePath + cartPosterUrl)
                .placeholder(R.drawable.baseline_broken_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(viewHolder.cartPoster);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
