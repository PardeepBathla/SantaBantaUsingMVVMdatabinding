package com.example.mvvmsantabanta.fragments.jokes;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.mvvmsantabanta.roomDb.SmsAndJoke;
import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDataModel;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDetailModel;
import com.example.mvvmsantabanta.networking.RetrofitService;

import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JokesRepository extends PageKeyedDataSource<Integer, JokesDetailModel> {

    private static JokesRepository newsRepository;
    private JokesApi jokesApi;
    public static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE =1;

    public static JokesRepository getInstance(){
        if (newsRepository == null){
            newsRepository = new JokesRepository();
        }
        return newsRepository;
    }



    public JokesRepository(){
        jokesApi = new RetrofitService().getApi(JokesApi.class);
    }





    public void InsertFavourite(JokesDetailModel model, FragmentJokes fragmentJokes) {
        SmsAndJoke smsAndJoke = new SmsAndJoke(model.getTitle(),model.getContent(),model.getImage());
        new InsertTask(fragmentJokes, smsAndJoke).execute();

    }




    class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<FragmentJokes> activityReference;
        private SmsAndJoke smsAndJoke;

        // only retain a weak reference to the activity
        InsertTask(FragmentJokes context, SmsAndJoke smsAndJoke) {
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
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
              /*  if (bool) {
                    activityReference.get().setResult(note, 1);
                    activityReference.get().finish();
                }*/
        }
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, JokesDetailModel> callback) {
        RetrofitService.getInsance()
                .getApi(JokesApi.class)
                .getJokesList(FragmentJokes.cat_slug_name,FIRST_PAGE)
//                .getSmsList(FIRST_PAGE, PAGE_SIZE, SITE_NAME)
                .enqueue(new Callback<JokesDataModel>() {
                    @Override
                    public void onResponse(Call<JokesDataModel> call, Response<JokesDataModel> response) {

                        if(response.body() != null){

                            callback.onResult(response.body().getData(), null, FIRST_PAGE + 1);

                        }

                    }

                    @Override
                    public void onFailure(Call<JokesDataModel> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, JokesDetailModel> callback) {
        RetrofitService.getInsance()
                .getApi(JokesApi.class)
                .getJokesList(  FragmentJokes.cat_slug_name,params.key)
                .enqueue(new Callback<JokesDataModel>() {
                    @Override
                    public void onResponse(Call<JokesDataModel> call, Response<JokesDataModel> response) {



                        if(response.body() != null){
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getData(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<JokesDataModel> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, JokesDetailModel> callback) {
        RetrofitService.getInsance()
                .getApi(JokesApi.class)
                .getJokesList( FragmentJokes.cat_slug_name,params.key)
                .enqueue(new Callback<JokesDataModel>() {
                    @Override
                    public void onResponse(Call<JokesDataModel> call, Response<JokesDataModel> response) {

                        if(response.body() != null){
                            Integer key =  (long)response.body().getLastPage()!=params.key?params.key+1:null;
//                            Integer key = response.body().has_more ? params.key + 1 : null;
                            callback.onResult(response.body().getData(), key);
                        }

                    }

                    @Override
                    public void onFailure(Call<JokesDataModel> call, Throwable t) {

                    }
                });

    }






    public MutableLiveData<ResponseBody> addToFav(AddFavouriteRequest addFavouriteRequest){
        MutableLiveData<ResponseBody> smsFavData = new MutableLiveData<>();
        jokesApi.saveFavouriteJoke(addFavouriteRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
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
        jokesApi.removeJokeFromFav(intValue).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    removeFavData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                removeFavData.setValue(null);
            }
        });
        return removeFavData;
    }


}
