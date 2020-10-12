package com.example.mvvmsantabanta.fragments.jokes.jokesCategories;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.fragments.jokes.JokesApi;
import com.example.mvvmsantabanta.networking.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JokesCategoriesRepository {
    private static JokesCategoriesRepository smsCategoryRepository;
    private JokesApi jokesApi;
    public static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE =1;

    public static JokesCategoriesRepository getInstance(){
        if (smsCategoryRepository == null){
            smsCategoryRepository = new JokesCategoriesRepository();
        }
        return smsCategoryRepository;
    }
    public JokesCategoriesRepository(){
        jokesApi = new RetrofitService().getApi(JokesApi.class);
    }

    public MutableLiveData<List<JokesCategoriesResponseModel>> getJokesCategories(){
        MutableLiveData<List<JokesCategoriesResponseModel>> newsData = new MutableLiveData<>();
        jokesApi.getJokesCategories(Constants.LANGUAGE_SELECTED).enqueue(new Callback<List<JokesCategoriesResponseModel>>() {
            @Override
            public void onResponse(Call<List<JokesCategoriesResponseModel>> call, Response<List<JokesCategoriesResponseModel>> response) {
                if (response.isSuccessful()){
                    newsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<JokesCategoriesResponseModel>> call, Throwable t) {
                newsData.setValue(null);
            }
        });
        return newsData;
    }
}
