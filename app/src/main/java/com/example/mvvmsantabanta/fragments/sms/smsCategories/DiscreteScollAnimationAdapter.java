package com.example.mvvmsantabanta.fragments.sms.smsCategories;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.BR;
import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.databinding.DiscreetScrollItemBinding;

import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by yarolegovich on 07.03.2017.
 */


public class DiscreteScollAnimationAdapter extends RecyclerView.Adapter<DiscreteScollAnimationAdapter.MyViewHolder> {

    private List<SmsCategoriesResponseModel> data;

    public DiscreteScollAnimationAdapter(List<SmsCategoriesResponseModel> data) {
        this.data = data;
    }


    @BindingAdapter("sms_subcategory_image")
    public static void setImage(ImageView ivSms, String url) { // make method static always when use @BindingAdapter
//        url = Constants.COMMON.CATEGORY_IMAGE_BASE_URL+url;
//        Log.d("sms_image", url);


        Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
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
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DiscreetScrollItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.discreet_scroll_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        SmsCategoriesResponseModel smsCategoriesResponseModel = data.get(position);
        ((MyViewHolder) holder).bind(smsCategoriesResponseModel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        DiscreetScrollItemBinding binding ;
        private ImageView image;

        public MyViewHolder(DiscreetScrollItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void bind(SmsCategoriesResponseModel smsCategoriesResponseModel) {
            binding.setVariable(BR.smsCategoriesmodel, smsCategoriesResponseModel); //smsModel name will be same as in xml model object name
            binding.executePendingBindings();
        }
    }
}
