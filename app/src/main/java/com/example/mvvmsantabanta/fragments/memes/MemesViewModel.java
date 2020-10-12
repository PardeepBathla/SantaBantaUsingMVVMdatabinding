package com.example.mvvmsantabanta.fragments.memes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.jokes.JokesRepository;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDetailModel;
import com.example.mvvmsantabanta.fragments.jokes.jokesPaging.ItemJokesDataSourceFactory;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesDetailModel;
import com.example.mvvmsantabanta.fragments.sms.SmsRepository;

import okhttp3.ResponseBody;

public class MemesViewModel extends ViewModel {

    private MemesRepository memesRepository;

    LiveData<PageKeyedDataSource<Integer, MemesDetailModel>> liveDataSource;
    LiveData<PagedList<MemesDetailModel>> itemPagedList;

    public MemesViewModel() {

        memesRepository = MemesRepository.getInstance();

    }

    public MutableLiveData<ResponseBody> postFavMeme(AddFavouriteRequest addFavouriteRequest) {

        return memesRepository.addMemeToFav(addFavouriteRequest);
    }

    public LiveData<ResponseBody> deleteFavMeme(int intValue) {

        return memesRepository.deleteMemeFromFav(intValue);
    }

    public LiveData<PagedList<MemesDetailModel>> getMemesLiveData() {
        return getMemes();
    }

    private LiveData<PagedList<MemesDetailModel>> getMemes() {

        ItemMemesDataSourceFactory itemMemesDataSourceFactory = new ItemMemesDataSourceFactory();
        liveDataSource = itemMemesDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(SmsRepository.PAGE_SIZE)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemMemesDataSourceFactory, config)).build();

        return itemPagedList;
    }
}
