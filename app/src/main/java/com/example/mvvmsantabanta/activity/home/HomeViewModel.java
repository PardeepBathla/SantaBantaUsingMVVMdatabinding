package com.example.mvvmsantabanta.activity.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuDetails;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuResponse;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<NavMenuDetails>> mutableLiveData;
    private HomeRepository homeRepository;


    public HomeViewModel() {
        if (mutableLiveData != null) {
            return;
        }
        homeRepository = HomeRepository.getInstance();
;
    }

    public LiveData<ArrayList<NavMenuDetails>> getSideMenuLiveData() {
        return homeRepository.getSideMenuData();
    }
}
