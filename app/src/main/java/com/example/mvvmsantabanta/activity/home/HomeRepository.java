package com.example.mvvmsantabanta.activity.home;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuDetails;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuResponse;
import com.example.mvvmsantabanta.networking.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {
    private static HomeRepository homeRepository;
    HomeApi homeApi;

    public static HomeRepository getInstance(){
        if (homeRepository == null){
            homeRepository = new HomeRepository();
        }
        return homeRepository;
    }


      public HomeRepository(){
          homeApi = new RetrofitService().getApi(HomeApi.class);
    }

     public MutableLiveData<ArrayList<NavMenuDetails>> getSideMenuData(){
        MutableLiveData<ArrayList<NavMenuDetails>> newsData = new MutableLiveData<>();
        homeApi.getNavList(Constants.LANGUAGE_SELECTED).enqueue(new Callback<NavMenuResponse>() {
            @Override
            public void onResponse(Call<NavMenuResponse> call, Response<NavMenuResponse> response) {
                if (response.isSuccessful()){
                    newsData.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<NavMenuResponse> call, Throwable t) {
                newsData.setValue(null);
            }
        });
        return newsData;
    }
}
