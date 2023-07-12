package com.example.sembadajati;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("userregister.php")
    Call<ResponseUserRegister> UserRegister(@Body RequestBody inputRegister);

    @POST("userlogin.php")
    Call<ResponseUserLogin> UserLogin(@Body RequestBody inputLogin);

    @POST("userlogout.php")
    Call<ResponseUserLogin> UserLogout(@Body RequestBody emailLogin);

    @GET("usernetwork.php")
    Call<ResponseUserNetwork> UserNetwork();

    @GET("userget.php")
    Call<ResponseUserGet> UserGet(@Query("email") String userEmail);

    @POST("userget.php")
    Call<ResponseUserLogin> UserChangeProfile(@Body RequestBody userDetail);

    @GET("productget.php")
    Call<ResponseProduct> ProductGet();

    @GET("productget.php")
    Call<ResponseProduct> ProductGetId(@Query("id") String productId);

    @GET("productget.php")
    Call<ResponseProduct> ProductGetName(@Query("product_title") String productName);

    @GET("productget.php")
    Call<ResponseProduct> ProductGetCategory(@Query("product_category") String productCategory);

    @GET("productbuy.php")
    Call<ResponseBuy> ProductBuy(@Query("id") String productId);

    @GET("productfav.php")
    Call<ResponseProductFavourite> ProductFavGet(@Query("user_email") String userEmail);

    @POST("productfav.php")
    Call<ResponseProductFavouriteDelete> ProductFavAdd(@Body RequestBody favAdd);

    @POST("productfavdelete.php")
    Call<ResponseProductFavouriteDelete> ProductFavDelete(@Body RequestBody favDelete);

    @GET("productcart.php")
    Call<ResponseProductFavourite> ProductCartGet(@Query("user_email") String userEmail);

    @POST("productcart.php")
    Call<ResponseProductFavouriteDelete> ProductCartAdd(@Body RequestBody cartAdd);

    @POST("productcartdelete.php")
    Call<ResponseProductFavouriteDelete> ProductCartDelete(@Body RequestBody cartDelete);
}
