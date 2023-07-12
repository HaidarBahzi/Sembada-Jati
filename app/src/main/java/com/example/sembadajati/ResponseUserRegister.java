package com.example.sembadajati;

import com.google.gson.annotations.SerializedName;

public class ResponseUserRegister{

	@SerializedName("result")
	private String result;

	@SerializedName("status")
	private String status;

	public String getResult(){
		return result;
	}

	public String getStatus(){
		return status;
	}
}