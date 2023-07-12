package com.example.sembadajati;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> localDataSet;

    public ProductAdapter(List<Product> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName, productPrice, productAvability, productRatingSold;
        private final ImageView productPoster;

        public ViewHolder(View view) {
            super(view);

            productName = view.findViewById(R.id.product_title);
            productPrice = view.findViewById(R.id.product_price);
            productPoster = view.findViewById(R.id.product_poster);
            productAvability = view.findViewById(R.id.product_avability);
            productRatingSold = view.findViewById(R.id.product_rating_sold);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Product product = localDataSet.get(position);
        String title = product.getProductTitle();
        String price = product.getProductPrice();
        String count = product.getProductBuyCount();
        String poster = product.getProductPoster();
        String rating = product.getProductRate();
        String available = product.getProductAvailableCount();


        int priceParse = Integer.parseInt(price);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(priceParse);

        String imagePath = viewHolder.itemView.getResources().getString(R.string.image_path);
        String rupiah = viewHolder.itemView.getResources().getString(R.string.harga);

        viewHolder.productName.setText(title);
        viewHolder.productPrice.setText(rupiah + formattedPrice);
        viewHolder.productAvability.setText("Tersedia " + available);
        viewHolder.productRatingSold.setText(rating + " | Terjual " + count);

        Picasso.get()
                .load(imagePath  + poster)
                .placeholder(R.drawable.baseline_broken_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(viewHolder.productPoster);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("product_id", product.getId());
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}