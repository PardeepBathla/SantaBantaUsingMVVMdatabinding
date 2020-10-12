package com.example.mvvmsantabanta.activity.home;

import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuDetails;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HomeApi {

    @GET("categories/all/{language}")
     Call<NavMenuResponse> getNavList(@Path("language") String lang) ;


}
