package com.example.mvvmsantabanta.fragments.memes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mvvmsantabanta.databinding.MemesVideoItemmBinding;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesDetailModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


public class PlayerViewHolder extends RecyclerView.ViewHolder {
    public RequestManager requestManager;
    public MemesVideoItemmBinding memesVideoItemBinding;
    MemesDetailModel currentList;
    View parent;
    public PlayerViewHolder(MemesVideoItemmBinding memesVideoItemBinding) {
        super(memesVideoItemBinding.getRoot());
        this.memesVideoItemBinding = memesVideoItemBinding;
        parent = memesVideoItemBinding.getRoot();
    }

    public void onBind(Context mCtx, MemesDetailModel currentList, RequestManager requestManager) {
        this.requestManager = requestManager;
        this.parent.setTag(this);


    }
}
