package com.example.sembadajati;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseProductFavourite{

	@SerializedName("result")
	private List<CartFav> result;

	@SerializedName("status")
	private String status;

	public List<CartFav> getResult(){
		return result;
	}

	public String getStatus(){
		return status;
	}
}