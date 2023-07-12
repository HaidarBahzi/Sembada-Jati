package com.example.sembadajati;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseUserGet{

	@SerializedName("result")
	private List<User> result;

	@SerializedName("status code")
	private int statusCode;

	public List<User> getResult(){
		return result;
	}

	public int getStatusCode(){
		return statusCode;
	}
}