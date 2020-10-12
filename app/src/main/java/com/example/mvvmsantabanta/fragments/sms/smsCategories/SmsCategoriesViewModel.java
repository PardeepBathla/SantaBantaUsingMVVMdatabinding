package com.example.mvvmsantabanta.fragments.sms.smsCategories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;

import java.util.List;

public class SmsCategoriesViewModel  extends ViewModel {
    private SmsCategoryRepository smsCategoryRepository;

    public SmsCategoriesViewModel() {

        smsCategoryRepository = SmsCategoryRepository.getInstance();

    }

    public LiveData<List<SmsCategoriesResponseModel>> getSmsCategoriesLiveData() {
        return smsCategoryRepository.getSmsCategories();
    }
}
