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

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private List<CartFav> localDataSet;

    public FavouriteAdapter(List<CartFav> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favourite_item, viewGroup, false);

        return new FavouriteAdapter.ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView favName, favPrice;
        final ImageButton favDelete;
        final ImageView favPoster;

        public ViewHolder(View view) {
            super(view);



            favName = view.findViewById(R.id.fav_name);
            favPrice = view.findViewById(R.id.fav_price);
            favDelete = view.findViewById(R.id.button_fav_delete);
            favPoster = view.findViewById(R.id.fav_poster);

        }
    }

    @Override
    public void onBindViewHolder(FavouriteAdapter.ViewHolder viewHolder, final int position) {

        CartFav favouriteItem = localDataSet.get(position);

        String favId ,favPosterUrl, favTitle, favPrice, favAvailable, imagePath, rupiahCurrency;
        int originalPrice;

        favPosterUrl = favouriteItem.getProductPoster();
        favTitle = favouriteItem.getProductTitle();
        originalPrice = Integer.parseInt(favouriteItem.getProductPrice());
        favId = favouriteItem.getProductId();

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        favPrice = decimalFormat.format(originalPrice);

        imagePath = viewHolder.itemView.getResources().getString(R.string.image_path);
        rupiahCurrency = viewHolder.itemView.getResources().getString(R.string.harga);

        viewHolder.favName.setText(favTitle);
        viewHolder.favPrice.setText( rupiahCurrency + favPrice );

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("product_id", favId);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

        viewHolder.favDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject obj = new JSONObject();

                try {
                    obj.put("delete_id", favouriteItem.getId());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                String jsonBody = obj.toString();

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

                ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
                Call<ResponseProductFavouriteDelete> call = service.ProductFavDelete(requestBody);


                call.enqueue(new Callback<ResponseProductFavouriteDelete>() {
                    @Override
                    public void onResponse(Call<ResponseProductFavouriteDelete> call, Response<ResponseProductFavouriteDelete> response) {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

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
                        .load(imagePath + favPosterUrl)
                                .placeholder(R.drawable.baseline_broken_image_24)
                                        .error(R.drawable.baseline_broken_image_24)
                                                .into(viewHolder.favPoster);
    }



    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
