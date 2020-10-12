package com.example.mvvmsantabanta.fragments.jokes.jokesCategories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;

import java.util.List;

public class JokesCategoriesViewModel extends ViewModel {
    private JokesCategoriesRepository jokesCategoriesRepository;

    public JokesCategoriesViewModel() {

        jokesCategoriesRepository = JokesCategoriesRepository.getInstance();

    }
    public LiveData<List<JokesCategoriesResponseModel>> getJokesCategoriesLiveData() {

        return jokesCategoriesRepository.getJokesCategories();

    }
}
