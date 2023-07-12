package com.example.sembadajati;

import com.google.gson.annotations.SerializedName;

public class CartFav {

	@SerializedName("user_email")
	private String userEmail;

	@SerializedName("product_poster")
	private String productPoster;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("id")
	private String id;

	@SerializedName("product_price")
	private String productPrice;

	@SerializedName("product_title")
	private String productTitle;

	@SerializedName("product_available_count")
	private String productAvailableCount;

	public String getUserEmail(){
		return userEmail;
	}

	public String getProductPoster(){
		return productPoster;
	}

	public String getProductId(){
		return productId;
	}

	public String getId(){
		return id;
	}

	public String getProductPrice(){
		return productPrice;
	}

	public String getProductTitle(){
		return productTitle;
	}

	public String getProductAvailableCount(){
		return productAvailableCount;
	}
}