package com.example.sembadajati;

import java.util.ArrayList;
import java.util.List;

public class ProductFavSingleton {
    private static ProductFavSingleton instance;
    private List<CartFav> productFav;

    private ProductFavSingleton() {
        productFav = new ArrayList<>();
    }

    public static synchronized ProductFavSingleton getInstance() {
        if (instance == null) {
            instance = new ProductFavSingleton();
        }
        return instance;
    }

    public List<CartFav> getProductFav() {
        return productFav;
    }

    public void setProductFav(List<CartFav> productFav) {
        this.productFav = productFav;
    }
}
