package com.example.mvvmsantabanta.fragments.jokes.jokesPaging;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.mvvmsantabanta.fragments.jokes.JokesRepository;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDetailModel;


public class ItemJokesDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, JokesDetailModel>> itemLiveDataSource = new MutableLiveData<>();


    @Override
    public DataSource create() {
        JokesRepository jokesRepository = new JokesRepository();
        itemLiveDataSource.postValue(jokesRepository);
        return jokesRepository;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, JokesDetailModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
