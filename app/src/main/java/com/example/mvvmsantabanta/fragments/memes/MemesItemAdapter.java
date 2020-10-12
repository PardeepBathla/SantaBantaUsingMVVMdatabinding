package com.example.mvvmsantabanta.fragments.memes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.app.santabanta.BR;
import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.databinding.MemesImageItemBinding;

import com.example.mvvmsantabanta.databinding.MemesVideoItemmBinding;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesDetailModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MemesItemAdapter extends PagedListAdapter<MemesDetailModel, RecyclerView.ViewHolder> {

    private static final int VIDEO = 1;
    private static final int IMAGE = 0;
    private static DiffUtil.ItemCallback<MemesDetailModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<MemesDetailModel>() {
        @Override
        public boolean areItemsTheSame(MemesDetailModel oldItem, MemesDetailModel newItem) {
            return oldItem.getContent() == newItem.getContent();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(MemesDetailModel oldItem, MemesDetailModel newItem) {
            return oldItem.equals(newItem);
        }
    };
    private static Context mCtx;
    private MemesFragment memesFragment;
    private ProgressBar progressBar;


    protected MemesItemAdapter(Context mCtx, MemesFragment memesFragment, ProgressBar progressBar) {
        super(DIFF_CALLBACK);
        this.memesFragment = memesFragment;
        MemesItemAdapter.mCtx = mCtx;
        this.progressBar = progressBar;
    }


    @BindingAdapter("meme_image")
    public static void setImage(ImageView ivMeme, String url) { // make method static always when use @BindingAdapter
//        url = Constants.COMMON.IMAGE_BASE_URL+url;
        if (url != null && !url.equals("")) {
            Log.d("sms_image", url);


            Picasso.get()
                    .load(url)
//                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_santa_banta_logo)
                    .fit()
                    .error(R.drawable.ic_santa_banta_logo)
                    .into(ivMeme, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("picasso", e.getMessage());
                        }
                    });

        } else {
            ivMeme.setImageDrawable(mCtx.getDrawable(R.drawable.ic_santa_banta_logo));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIDEO:
                MemesVideoItemmBinding memesVideoItemmBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.memes_video_itemm, parent, false);
                viewHolder = new PlayerViewHolder(memesVideoItemmBinding);
                viewHolder.setIsRecyclable(false);

                break;

            case IMAGE:
                MemesImageItemBinding memesImageItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.memes_image_item, parent, false);

                viewHolder = new ImageViewHolder(memesImageItemBinding);
                viewHolder.setIsRecyclable(false);
                break;

        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        progressBar.setVisibility(View.GONE);
        if (holder instanceof PlayerViewHolder)
            ((PlayerViewHolder) holder).onBind(mCtx, Objects.requireNonNull(getItem(position)), Glide.with(mCtx).setDefaultRequestOptions(new RequestOptions()));
        else if (holder instanceof ImageViewHolder)
            ((ImageViewHolder) holder).bindData(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (Objects.requireNonNull(getItem(position)).getMemeType().equals("video"))
            return VIDEO;
        else
            return IMAGE;
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        MemesImageItemBinding memesImageItemBinding;

        ImageViewHolder(MemesImageItemBinding memesImageItemBinding) {
            super(memesImageItemBinding.getRoot());
            this.memesImageItemBinding = memesImageItemBinding;
        }

        void bindData(MemesDetailModel item) {
            memesImageItemBinding.setVariable(BR.memesDetailModel, item); //smsModel name will be same as in xml model object name
            memesImageItemBinding.executePendingBindings();

        }
    }
}
