package com.example.mvvmsantabanta.fragments.sms.smsCategories;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.fragments.sms.SmsApi;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;
import com.example.mvvmsantabanta.networking.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsCategoryRepository {

    private static SmsCategoryRepository smsCategoryRepository;
    private SmsApi smsApi;
    public static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE =1;

    public static SmsCategoryRepository getInstance(){
        if (smsCategoryRepository == null){
            smsCategoryRepository = new SmsCategoryRepository();
        }
        return smsCategoryRepository;
    }
      public SmsCategoryRepository(){
        smsApi = new RetrofitService().getApi(SmsApi.class);
    }

    public MutableLiveData<List<SmsCategoriesResponseModel>> getSmsCategories(){
        MutableLiveData<List<SmsCategoriesResponseModel>> newsData = new MutableLiveData<>();
        smsApi.getSmsCategories(Constants.LANGUAGE_SELECTED).enqueue(new Callback<List<SmsCategoriesResponseModel>>() {
            @Override
            public void onResponse(Call<List<SmsCategoriesResponseModel>> call, Response<List<SmsCategoriesResponseModel>> response) {
                if (response.isSuccessful()){
                    newsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<SmsCategoriesResponseModel>> call, Throwable t) {
                newsData.setValue(null);
            }
        });
        return newsData;
    }
}
