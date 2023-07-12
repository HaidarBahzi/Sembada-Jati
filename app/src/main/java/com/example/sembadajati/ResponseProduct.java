package com.example.sembadajati;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseProduct{

	@SerializedName("result")
	private List<Product> result;

	@SerializedName("status code")
	private int statusCode;

	public List<Product> getResult(){
		return result;
	}

	public int getStatusCode(){
		return statusCode;
	}
}