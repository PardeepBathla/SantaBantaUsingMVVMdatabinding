package com.example.mvvmsantabanta.fragments.jokes.jokesCategories;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.BR;
import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.activity.home.Main2Activity;
import com.example.mvvmsantabanta.databinding.JokesCategoryItemBinding;
import com.example.mvvmsantabanta.fragments.jokes.FragmentJokes;
import com.example.mvvmsantabanta.interfaces.OnClickHandlerInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JokesCategoriesAdapter extends RecyclerView.Adapter<JokesCategoriesAdapter.MyViewHolder> implements OnClickHandlerInterface {
    private static Context context;
    FragmentJokesCategories fragmentJokesCategories;
    ArrayList<JokesCategoriesResponseModel> jokesCategoriesArrayList;
    ProgressBar progressBar;

    public JokesCategoriesAdapter(Context context, ArrayList<JokesCategoriesResponseModel> jokesCategoriesArrayList, FragmentJokesCategories fragmentJokesCategories, ProgressBar progressBar) {

        this.context = context;
        this.fragmentJokesCategories = fragmentJokesCategories;
        this.jokesCategoriesArrayList = jokesCategoriesArrayList;
        this.progressBar = progressBar;

    }

    @BindingAdapter("jokes_category_image")
    public static void jokes_category_image(ImageView ivSms, String url) { // make method static always when use @BindingAdapter
        if (url != null && !url.equals("")) {
            Log.d("joke_category_image", url);


            Picasso.get()
                    .load(url)
//                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .fit().error(R.mipmap.ic_launcher)
                    .into(ivSms, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("picasso", e.getMessage());
                        }
                    });

        } else {
            ivSms.setImageDrawable(context.getDrawable(R.drawable.ic_santa_banta_logo));
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JokesCategoryItemBinding jokesCategoryItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.jokes_category_item, parent, false);
        return new MyViewHolder(jokesCategoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (position == 0) {
            progressBar.setVisibility(View.GONE);
        }
        JokesCategoriesResponseModel jokesCategoriesResponseModel = jokesCategoriesArrayList.get(position);
        holder.bind(jokesCategoriesResponseModel);
    }

    @Override
    public int getItemCount() {
        return jokesCategoriesArrayList.size();
    }

    @Override
    public void onClick(View view, Object model1) {
        JokesCategoriesResponseModel model = (JokesCategoriesResponseModel) model1;

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG_ID, model.getId());
        bundle.putString(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG, model.getSlug());

        FragmentJokes fragmentJokes = new FragmentJokes();
        fragmentJokes.setArguments(bundle);
        fragmentJokesCategories.enterNextFragment(fragmentJokes);
    }

    @Override
    public void onShareClick(View view) {

    }

    private void setLayoutWidth(@NonNull JokesCategoryItemBinding jokesCategoryItemBinding, int width) {
        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(width, width);
        int margin_6 = (int) context.getResources().getDimension(R.dimen._6sdp);
        int margin_3 = (int) context.getResources().getDimension(R.dimen._3sdp);
        rel_btn.setMargins(margin_3, margin_3, margin_3, margin_3);
        jokesCategoryItemBinding.cardCategory.setLayoutParams(rel_btn);
    }

    private int getDisplayWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Main2Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = ((width / 3));
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            width = ((width / 5));
        }
        return width;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        JokesCategoryItemBinding jokesCategoryItemBinding;

        public MyViewHolder(@NonNull JokesCategoryItemBinding jokesCategoryItemBinding) {
            super(jokesCategoryItemBinding.getRoot());
            this.jokesCategoryItemBinding = jokesCategoryItemBinding;
            this.jokesCategoryItemBinding.setClickListnr(JokesCategoriesAdapter.this);

            setLayoutWidth(jokesCategoryItemBinding, getDisplayWidth());
        }

        public void bind(JokesCategoriesResponseModel obj) {
            jokesCategoryItemBinding.setVariable(BR.jokeCategoryDetailModel, obj); //smsModel name will be same as in xml model object name
            jokesCategoryItemBinding.executePendingBindings();
        }
    }
}
