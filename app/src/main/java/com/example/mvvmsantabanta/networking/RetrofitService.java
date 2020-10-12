package com.example.mvvmsantabanta.networking;


import android.os.SystemClock;

import com.example.mvvmsantabanta.utility.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static RetrofitService mInstance;
    Retrofit retrofit;
    private static Interceptor interceptor = chain -> {
        SystemClock.sleep(700);
        return chain.proceed(chain.request());
    };

    private  static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor()) //TODO
            .addNetworkInterceptor(interceptor)
            .build();


    public RetrofitService() {

        retrofit = new Retrofit.Builder()
//            .baseUrl("https://newsapi.org/v2/")
               .baseUrl(Constants.COMMON.BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .client(okHttpClient)
               .build();
    }




    private static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    public static synchronized RetrofitService getInsance(){
        if(mInstance == null){
            mInstance = new RetrofitService();
        }
        return mInstance;
    }


    public  <S> S getApi(Class<S> serviceClass) {


        return retrofit.create(serviceClass);
    }

}
