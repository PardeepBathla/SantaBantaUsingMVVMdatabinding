package com.example.mvvmsantabanta.fragments.sms.smsCategories;

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
import com.example.mvvmsantabanta.activity.home.Main2Activity;
import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.databinding.SmsCategoryItemBinding;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;
import com.example.mvvmsantabanta.interfaces.OnClickHandlerInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SmsCategoriesAdapter extends RecyclerView.Adapter<SmsCategoriesAdapter.MyViewHolder> implements OnClickHandlerInterface {

    Context context;
    SmsCategoriesFragment smsCategoriesFragment;
    ArrayList<SmsCategoriesResponseModel> smsCategoriesArrayList;
    ProgressBar progressBar;

    public SmsCategoriesAdapter(Context context, ArrayList<SmsCategoriesResponseModel> smsCategoriesArrayList, SmsCategoriesFragment smsCategoriesFragment, ProgressBar progressBar) {
        this.context = context;
        this.smsCategoriesFragment = smsCategoriesFragment;
        this.smsCategoriesArrayList = smsCategoriesArrayList;
        this.progressBar = progressBar;
    }

    @BindingAdapter("sms_category_image")
    public static void setImage(ImageView ivSms, String url) { // make method static always when use @BindingAdapter
        if (url!=null) {
            Log.d("sms_image", url);
        }


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
                            Log.e("picasso", e.getMessage() );
                        }
                    });


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SmsCategoryItemBinding smsCategoryItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sms_category_item, parent, false);
        return new MyViewHolder(smsCategoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (position ==0){
            progressBar.setVisibility(View.GONE);
        }
        SmsCategoriesResponseModel smsCategoriesResponseModel = smsCategoriesArrayList.get(position);
        holder.bind(smsCategoriesResponseModel);
    }

    @Override
    public int getItemCount() {
        return smsCategoriesArrayList.size();
    }

    @Override
    public void onClick(View view, Object model) {

     int pos =  smsCategoriesArrayList.indexOf(model);
        Constants.REDIRECTING_FROM = Constants.SMS_CATEGORIES_SCREEN;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.COMMON.CATEGORIES_SUBCATEGORIES,smsCategoriesArrayList);
        bundle.putInt(Constants.COMMON.SELECTED_CATEGORY_POSITION,pos);
        SmsSubCategoriesFragment smsSubCategoriesFragment = new SmsSubCategoriesFragment();
        smsSubCategoriesFragment.setArguments(bundle);
        smsCategoriesFragment.enterNextFragment(smsSubCategoriesFragment);

    }

    @Override
    public void onShareClick(View view) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SmsCategoryItemBinding smsCategoryItemBinding;

        public MyViewHolder(@NonNull SmsCategoryItemBinding smsCategoryItemBinding) {
            super(smsCategoryItemBinding.getRoot());
            this.smsCategoryItemBinding = smsCategoryItemBinding;
            this.smsCategoryItemBinding.setClickListener(SmsCategoriesAdapter.this);

            setLayoutWidth(smsCategoryItemBinding, getDisplayWidth());
        }

        public void bind(SmsCategoriesResponseModel obj) {
            smsCategoryItemBinding.setVariable(BR.smsCategoryDetailModel, obj); //smsModel name will be same as in xml model object name
            smsCategoryItemBinding.executePendingBindings();
        }
    }

    private void setLayoutWidth(@NonNull com.example.mvvmsantabanta.databinding.SmsCategoryItemBinding smsCategoryItemBinding, int width) {
        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(width, width);
        int margin_6 = (int) context.getResources().getDimension(R.dimen._6sdp);
        int margin_3 = (int) context.getResources().getDimension(R.dimen._3sdp);
        rel_btn.setMargins(margin_3,margin_3,margin_3,margin_3);
        smsCategoryItemBinding.cardCategory.setLayoutParams(rel_btn);
    }

    private int getDisplayWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Main2Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width =  displayMetrics.widthPixels;

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            width = ((width/3));
        }else {
            width = ((width/5));
        }

        return width;
    }
}
