package com.example.sembadajati;

import com.google.gson.annotations.SerializedName;

public class ResponseBuy{

	@SerializedName("status code")
	private int statusCode;

	@SerializedName("message")
	private String message;

	public int getStatusCode(){
		return statusCode;
	}

	public String getMessage(){
		return message;
	}
}