package com.example.mvvmsantabanta.fragments.memes;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesDetailModel;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesResposeModel;
import com.example.mvvmsantabanta.networking.RetrofitService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemesRepository extends PageKeyedDataSource<Integer, MemesDetailModel> {
    private static final int FIRST_PAGE =1;
    private static MemesRepository memesRepository;
    public static MemesRepository getInstance() {
        if (memesRepository == null){
            memesRepository = new MemesRepository();
        }
        return memesRepository;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MemesDetailModel> callback) {
        RetrofitService.getInsance()
                .getApi(MemesApi.class)
                .getMemesList(Constants.LANGUAGE_SELECTED,FIRST_PAGE)
//                .getSmsList(FIRST_PAGE, PAGE_SIZE, SITE_NAME)
                .enqueue(new Callback<MemesResposeModel>() {
                    @Override
                    public void onResponse(Call<MemesResposeModel> call, Response<MemesResposeModel> response) {

                        if(response.body() != null){

                            callback.onResult(response.body().getData(), null, FIRST_PAGE + 1);

                        }

                    }

                    @Override
                    public void onFailure(Call<MemesResposeModel> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MemesDetailModel> callback) {
        RetrofitService.getInsance()
                .getApi(MemesApi.class)
                .getMemesList( Constants.LANGUAGE_SELECTED,params.key)
                .enqueue(new Callback<MemesResposeModel>() {
                    @Override
                    public void onResponse(Call<MemesResposeModel> call, Response<MemesResposeModel> response) {



                        if(response.body() != null){
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getData(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<MemesResposeModel> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MemesDetailModel> callback) {

        RetrofitService.getInsance()
                .getApi(MemesApi.class)
                .getMemesList( Constants.LANGUAGE_SELECTED,params.key)
                .enqueue(new Callback<MemesResposeModel>() {
                    @Override
                    public void onResponse(Call<MemesResposeModel> call, Response<MemesResposeModel> response) {

                        if(response.body() != null){
                            Integer key =  (long)response.body().getLastPage()!=params.key?params.key+1:null;
//                            Integer key = response.body().has_more ? params.key + 1 : null;
                            callback.onResult(response.body().getData(), key);
                        }

                    }

                    @Override
                    public void onFailure(Call<MemesResposeModel> call, Throwable t) {

                    }
                });

    }

    public MutableLiveData<ResponseBody> addMemeToFav(AddFavouriteRequest addFavouriteRequest) {
        return null;
    }

    public LiveData<ResponseBody> deleteMemeFromFav(int intValue) {
        return null;
    }
}
