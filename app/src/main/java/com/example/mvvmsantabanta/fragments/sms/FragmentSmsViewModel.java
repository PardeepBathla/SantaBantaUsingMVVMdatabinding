package com.example.mvvmsantabanta.fragments.sms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsDetailModel;
import com.example.mvvmsantabanta.fragments.sms.smspaging.ItemSmsDataSourceFactory;

import okhttp3.ResponseBody;

public class FragmentSmsViewModel extends ViewModel {
    private MutableLiveData<SmsDetailModel> uploadFavSmsLiveData;
    LiveData<PageKeyedDataSource<Integer, SmsDetailModel>> liveDataSource;
    LiveData<PagedList<SmsDetailModel>> itemPagedList;
    FragmentSms fragmentSms;
    SmsRepository smsRepository;



    private LiveData<PagedList<SmsDetailModel>> getSMsLiveData() {
        ItemSmsDataSourceFactory itemSmsDataSourceFactory = new ItemSmsDataSourceFactory(fragmentSms);
        liveDataSource = itemSmsDataSourceFactory.getItemLiveDataSource();


        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(SmsRepository.PAGE_SIZE)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemSmsDataSourceFactory, config)).build();
        return itemPagedList;
    }

    public MutableLiveData<ResponseBody> postFavSms(AddFavouriteRequest addFavouriteRequest) {

        return smsRepository.addToFav(addFavouriteRequest);
    }

    public LiveData<ResponseBody> deleteFavSms(int intValue) {

        return smsRepository.deleteFromFav(intValue);
    }



    public LiveData<PagedList<SmsDetailModel>> getSmsLiveData( FragmentSms fragmentSms) {

        this.fragmentSms = fragmentSms;
        smsRepository = SmsRepository.getInstance(fragmentSms);
        return getSMsLiveData();
    }
}
