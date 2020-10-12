package com.example.mvvmsantabanta.fragments.sms.smspaging;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.mvvmsantabanta.fragments.sms.FragmentSms;
import com.example.mvvmsantabanta.fragments.sms.SmsRepository;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsDetailModel;


public class ItemSmsDataSourceFactory extends DataSource.Factory {
    FragmentSms fragmentSms;

    private MutableLiveData<PageKeyedDataSource<Integer, SmsDetailModel>> itemLiveDataSource = new MutableLiveData<>();

    public ItemSmsDataSourceFactory(FragmentSms fragmentSms) {
        this.fragmentSms = fragmentSms;
    }


    @Override
    public DataSource create() {
        SmsRepository smsRepository = SmsRepository.getInstance(fragmentSms);
        itemLiveDataSource.postValue(smsRepository);
        return smsRepository;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, SmsDetailModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
