package com.example.mvvmsantabanta.fragments.memes;

import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDataModel;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesResposeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MemesApi {

    @GET("memes/latest/{language}")
    Call<MemesResposeModel> getMemesList(@Path("language") String lang, @Query("page") int page_num);
}
