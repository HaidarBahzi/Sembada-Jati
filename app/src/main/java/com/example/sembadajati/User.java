package com.example.sembadajati;

import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("nama_lengkap")
	private String namaLengkap;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("nomor_hp")
	private String nomorHp;

	public String getNamaLengkap(){
		return namaLengkap;
	}

	public String getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}

	public String getNomorHp(){
		return nomorHp;
	}
}