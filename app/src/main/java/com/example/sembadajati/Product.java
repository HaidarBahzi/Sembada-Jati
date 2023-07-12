package com.example.sembadajati;

import com.google.gson.annotations.SerializedName;

public class Product{

	@SerializedName("product_desc")
	private String productDesc;

	@SerializedName("product_buy_count")
	private String productBuyCount;

	@SerializedName("product_poster")
	private String productPoster;

	@SerializedName("product_rate")
	private String productRate;

	@SerializedName("id")
	private String id;

	@SerializedName("product_price")
	private String productPrice;

	@SerializedName("product_title")
	private String productTitle;

	@SerializedName("product_category")
	private String productCategory;

	@SerializedName("product_available_count")
	private String productAvailableCount;

	public String getProductDesc(){
		return productDesc;
	}

	public String getProductBuyCount(){
		return productBuyCount;
	}

	public String getProductPoster(){
		return productPoster;
	}

	public String getProductRate(){
		return productRate;
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

	public String getProductCategory(){
		return productCategory;
	}

	public String getProductAvailableCount(){
		return productAvailableCount;
	}
}