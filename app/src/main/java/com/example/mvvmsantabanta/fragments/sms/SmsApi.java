package com.example.mvvmsantabanta.fragments.sms;


import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsResponseModel;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SmsApi {


/*

    @GET("sms")
    Call<SmsResponseModel> getSmsList(@Query("page") int page_num);
*/


    // encode = false means it will not encode the special chars in value like / & ' because we cant to send these special chars as it is
    //By dEfault encode is false

   /* @GET("sms/{slug1}/{categoryid}")
    Call<SmsResponseModel> getSmsList(@Path(value = "slug1",encoded = true) String slug,@Path(value = "categoryid",encoded = true) int categoryid,@Query("page") int page_num);*/

    @GET("newsms/{slug1}")
    Call<SmsResponseModel> getSmsList(@Path(value = "slug1", encoded = true) String slug, @Query("page") int page_num);

    @GET("sidebarmenus/{language}")
    Call<List<SmsCategoriesResponseModel>> getSmsCategories(@Path("language") String lang);

    @POST("favourites/save")
    Call<ResponseBody> saveFavouriteSMs(@Body AddFavouriteRequest addFavouriteRequest);

    @GET("favourites/delete/{id}")
    Call<ResponseBody> removeSmsFromFav(@Path("id") int id);
}
