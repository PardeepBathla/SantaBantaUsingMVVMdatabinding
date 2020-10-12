package com.example.mvvmsantabanta.fragments.memes;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.mvvmsantabanta.fragments.jokes.JokesRepository;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDetailModel;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesDetailModel;


public class ItemMemesDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, MemesDetailModel>> itemLiveDataSource = new MutableLiveData<>();


    @Override
    public DataSource create() {
        MemesRepository memesRepository = new MemesRepository();
        itemLiveDataSource.postValue(memesRepository);
        return memesRepository;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, MemesDetailModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
