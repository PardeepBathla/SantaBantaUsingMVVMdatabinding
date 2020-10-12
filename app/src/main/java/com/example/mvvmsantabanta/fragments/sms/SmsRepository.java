package com.example.mvvmsantabanta.fragments.sms;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;


import com.example.mvvmsantabanta.roomDb.SmsAndJoke;
import com.example.mvvmsantabanta.utility.Utils;
import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsDetailModel;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsResponseModel;
import com.example.mvvmsantabanta.networking.RetrofitService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsRepository extends PageKeyedDataSource<Integer, SmsDetailModel> {

    public static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 1;
    private static SmsRepository newsRepository;
    private final SmsApi smsApi;
    static FragmentSms fragmentSms;

    public SmsRepository() {
        smsApi = new RetrofitService().getApi(SmsApi.class);
    }

    public static SmsRepository getInstance(FragmentSms fragmentSmss) {

        fragmentSms = fragmentSmss;
        if (newsRepository == null) {
            newsRepository = new SmsRepository();
        }
        return newsRepository;
    }



    /*  public MutableLiveData<SmsResponseModel> getSms(){
          MutableLiveData<SmsResponseModel> newsData = new MutableLiveData<>();
          smsApi.getSmsList().enqueue(new Callback<SmsResponseModel>() {
              @Override
              public void onResponse(Call<SmsResponseModel> call, Response<SmsResponseModel> response) {
                  if (response.isSuccessful()){
                      newsData.setValue(response.body());
                  }
              }

              @Override
              public void onFailure(Call<SmsResponseModel> call, Throwable t) {
                  newsData.setValue(null);
              }
          });
          return newsData;
      }
  */
    public MutableLiveData<ResponseBody> addToFav(AddFavouriteRequest addFavouriteRequest) {
        MutableLiveData<ResponseBody> smsFavData = new MutableLiveData<>();
        smsApi.saveFavouriteSMs(addFavouriteRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    smsFavData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                smsFavData.setValue(null);
            }
        });
        return smsFavData;
    }

    public LiveData<ResponseBody> deleteFromFav(int intValue) {
        MutableLiveData<ResponseBody> removeFavData = new MutableLiveData<>();
        smsApi.removeSmsFromFav(intValue).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    removeFavData.setValue(response.body());
                    Log.d("TAG", "onResponse: ");
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                removeFavData.setValue(null);
            }
        });
        return removeFavData;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, SmsDetailModel> callback) {

        RetrofitService.getInsance()
                .getApi(SmsApi.class)
                .getSmsList(FragmentSms.subcat_slug_name, FIRST_PAGE)
//                .getSmsList(FragmentSms.subcat_slug_name, FragmentSms.subcat_slug_id, FIRST_PAGE)


                .enqueue(new Callback<SmsResponseModel>() {
                    @Override
                    public void onResponse(Call<SmsResponseModel> call, Response<SmsResponseModel> response) {

                        if (response.code() == 404) {
                            fragmentSms.ApiError();

                        } else {
                            if (response.body() != null) {

                                callback.onResult(response.body().getData(), null, FIRST_PAGE + 1);

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<SmsResponseModel> call, Throwable t) {

                        callback.onResult(new ArrayList<SmsDetailModel>(), null, null);
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, SmsDetailModel> callback) {

        RetrofitService.getInsance()
                .getApi(SmsApi.class)
                .getSmsList(FragmentSms.subcat_slug_name,  params.key)
//                .getSmsList(FragmentSms.subcat_slug_name, FragmentSms.subcat_slug_id, params.key)

                .enqueue(new Callback<SmsResponseModel>() {
                    @Override
                    public void onResponse(Call<SmsResponseModel> call, Response<SmsResponseModel> response) {

                        if (response.code() == 404) {
                            fragmentSms.ApiError();

                        } else {

                            if (response.body() != null) {
                                Integer key = (params.key > 1) ? params.key - 1 : null;
                                callback.onResult(response.body().getData(), key);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SmsResponseModel> call, Throwable t) {
                        callback.onResult(new ArrayList<SmsDetailModel>(), null);
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, SmsDetailModel> callback) {

        RetrofitService.getInsance()
                .getApi(SmsApi.class)
                .getSmsList(FragmentSms.subcat_slug_name, params.key)
//                .getSmsList(FragmentSms.subcat_slug_name, FragmentSms.subcat_slug_id, params.key)

                .enqueue(new Callback<SmsResponseModel>() {
                    @Override
                    public void onResponse(Call<SmsResponseModel> call, Response<SmsResponseModel> response) {

                        if (response.code() == 404) {
                            fragmentSms.ApiError();

                        } else {
                            if (response.body() != null) {
                                Integer key = (long) response.body().getLastPage() != params.key ? params.key + 1 : null;
//                            Integer key = response.body().has_more ? params.key + 1 : null;
                                callback.onResult(response.body().getData(), key);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<SmsResponseModel> call, Throwable t) {
                        callback.onResult(new ArrayList<SmsDetailModel>(), null);
                    }
                });


    }

    public void InsertFavourite(SmsDetailModel model, FragmentSms fragmentSms) {
        SmsAndJoke smsAndJoke = new SmsAndJoke(model.getContent(), model.getImage(), model.getCategories().get(0).getName());
        new InsertTask(fragmentSms, smsAndJoke).execute();

    }


    class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<FragmentSms> activityReference;
        private SmsAndJoke smsAndJoke;

        // only retain a weak reference to the activity
        InsertTask(FragmentSms context, SmsAndJoke smsAndJoke) {
            activityReference = new WeakReference<>(context);
            this.smsAndJoke = smsAndJoke;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented note id
            long j = activityReference.get().smsAndJokesDatabase.getSmsAndJokesDao().insertNote(smsAndJoke);
            smsAndJoke.setFavourite_id(j);
            Log.e("ID ", "doInBackground: " + j);
            Log.d("doInBackground: ","hfhdfh");
            return true;

        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            Utils.ShowToast(activityReference.get().getActivity(), "Added To Favourite!!");
        }
    }
}
