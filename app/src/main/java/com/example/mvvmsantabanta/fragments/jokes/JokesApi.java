package com.example.mvvmsantabanta.fragments.jokes;


import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.jokes.jokesCategories.JokesCategoriesResponseModel;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDataModel;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JokesApi {



    /*@GET("jokes/latest/{language}")
    Call<JokesDataModel> getJokesList(@Path("language") String lang,@Query("page") int page_num); */
/*

    @GET("jokes/{name}/{id}")
    Call<JokesDataModel> getJokesList(@Path(value = "id",encoded = true) int slugid,@Path(value = "name",encoded = true) String slugname,@Query("page") int page_num);
*/

    @GET("newjokes/{name}")
    Call<JokesDataModel> getJokesList(@Path(value = "name", encoded = true) String slugname, @Query("page") int page_num);

    @POST("favourites/save")
    Call<ResponseBody> saveFavouriteJoke(@Body AddFavouriteRequest addFavouriteRequest);

    @GET("favourites/delete/{id}")
    Call<ResponseBody> removeJokeFromFav(@Path("id") int id);

    @GET("jokes/sidebarmenus/{language}")
    Call<List<JokesCategoriesResponseModel>> getJokesCategories(@Path("language") String lang);
}
