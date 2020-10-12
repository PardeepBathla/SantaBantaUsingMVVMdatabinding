package com.example.mvvmsantabanta.fragments.jokes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;


import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDetailModel;
import com.example.mvvmsantabanta.fragments.jokes.jokesPaging.ItemJokesDataSourceFactory;

import okhttp3.ResponseBody;


public class FragmentJokesViewModel extends ViewModel {

    private JokesRepository jokesRepository;

    LiveData<PageKeyedDataSource<Integer, JokesDetailModel>> liveDataSource;
    LiveData<PagedList<JokesDetailModel>> itemPagedList;

    public FragmentJokesViewModel() {

        jokesRepository = JokesRepository.getInstance();

    }

    public MutableLiveData<ResponseBody> postFavJoke(AddFavouriteRequest addFavouriteRequest) {

        return jokesRepository.addToFav(addFavouriteRequest);
    }

    public LiveData<ResponseBody> deleteFavJoke(int intValue) {

        return jokesRepository.deleteFromFav(intValue);
    }

    public LiveData<PagedList<JokesDetailModel>> getJokesLiveData() {
        return getJokes();
    }

    private LiveData<PagedList<JokesDetailModel>> getJokes() {

        ItemJokesDataSourceFactory itemJokesDataSourceFactory = new ItemJokesDataSourceFactory();
        liveDataSource = itemJokesDataSourceFactory.getItemLiveDataSource();

            PagedList.Config config =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(false)
                            .setPageSize(JokesRepository.PAGE_SIZE)
                            .build();

            itemPagedList = (new LivePagedListBuilder(itemJokesDataSourceFactory, config)).build();

        return itemPagedList;
    }
}
