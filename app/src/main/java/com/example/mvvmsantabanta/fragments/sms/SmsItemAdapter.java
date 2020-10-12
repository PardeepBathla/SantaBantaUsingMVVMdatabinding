package com.example.mvvmsantabanta.fragments.sms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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

import com.example.mvvmsantabanta.BR;
import com.example.mvvmsantabanta.R;

import com.example.mvvmsantabanta.utility.CheckPermissions;
import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.utility.ShareableIntents;
import com.example.mvvmsantabanta.utility.Utils;
import com.example.mvvmsantabanta.activity.home.Main2Activity;
import com.example.mvvmsantabanta.databinding.AdItemBinding;
import com.example.mvvmsantabanta.databinding.SmsItemBinding;
import com.example.mvvmsantabanta.fragments.jokes.BitmapLoadedCallback;
import com.example.mvvmsantabanta.fragments.jokes.LoadImageBitmap;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsDetailModel;

import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsFavouriteModel;
import com.example.mvvmsantabanta.interfaces.OnClickHandlerInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class SmsItemAdapter extends PagedListAdapter<SmsDetailModel, RecyclerView.ViewHolder> implements BitmapLoadedCallback, View.OnClickListener, OnClickHandlerInterface {
    //instead of paged Recyclerview.Adapter we extended PagedListAdapter of Paging Library

    private static final int AD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;
    private static Context mCtx;
    private static DiffUtil.ItemCallback<SmsDetailModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<SmsDetailModel>() {
        @Override
        public boolean areItemsTheSame(SmsDetailModel oldItem, SmsDetailModel newItem) {
            return oldItem.getContent() == newItem.getContent();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(SmsDetailModel oldItem, SmsDetailModel newItem) {
            return oldItem.equals(newItem);
        }
    };
    public SmsItemBinding itemBinding;
    ProgressBar spinKit;
    FragmentSms fragmentSms;
    private SmsDetailModel smsTOBeShared;
    private ShareableIntents shareableIntents;
    private boolean isSharelayoutVisible = false;

    protected SmsItemAdapter(Context mCtx, FragmentSms fragmentSms, ProgressBar spinKit) {
        super(DIFF_CALLBACK);
        SmsItemAdapter.mCtx = mCtx;
        this.fragmentSms = fragmentSms;
        this.spinKit = spinKit;
        shareableIntents = new ShareableIntents(mCtx);
    }

    @BindingAdapter("sms_image")
    public static void setImage(ImageView ivSms, String url) { // make method static always when use @BindingAdapter
//        url = Constants.COMMON.IMAGE_BASE_URL+url;
        if (url != null && !url.equals("")) {
            Log.d("sms_image", url);


            Picasso.get()
                    .load(url)
//                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_santa_banta_logo)
                    .fit()
                    .error(R.drawable.ic_santa_banta_logo)
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
            ivSms.setImageDrawable(mCtx.getDrawable(R.drawable.ic_santa_banta_logo));
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case AD_TYPE:
                AdItemBinding adItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ad_item, parent, false);
                return new AdViewHolder(adItemBinding);
            case CONTENT_TYPE:
                SmsItemBinding smsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sms_item, parent, false);
                return new SmsViewHolder(smsItemBinding);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        spinKit.setVisibility(View.GONE);
        if (holder instanceof AdViewHolder) {
//            ((SmsItemAdapter.AdViewHolder) holder).adItemBinding.tvAd.setText("Google ads");
        } else if (holder instanceof SmsViewHolder) {

            SmsDetailModel dataModel = getItem(position);
            if (dataModel != null) {
                ((SmsViewHolder) holder).bind(dataModel, position);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        /*if (position % Constants.COMMON.AdPlacementPosition == 0 && position != 0)
            return AD_TYPE;
        else*/
        return CONTENT_TYPE;
    }

    @Override
    public void onClick(View view, Object model) {
//        SmsRepository.getInstance().InsertFavourite((SmsDetail) model, fragmentSms);

    }

    @Override
    public void onShareClick(View view) {
        shareTextUrl();

    }



    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");

        mCtx.startActivity(Intent.createChooser(share, "Share link!"));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap,String platform) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mCtx.getContentResolver(), bitmap, "SantaBantaShare", null);
        Uri imageUri = Uri.parse(path);

        switch (platform){
            case Constants.COMMON.WHATSAPP :
                shareableIntents.shareOnWhatsapp(imageUri);
                break;
            /*    case Constants.COMMON.FACEBOOK :
                shareableIntents.shareOnWhatsapp(imageUri);
                break;*/
                case Constants.COMMON.TWITTER :
//                shareableIntents.shareOnTwi1tter(imageUri);
                break;
               /* case Constants.COMMON.INSTAGRAM :
                shareableIntents.shareOnWhatsapp(imageUri);
                break;
                case Constants.COMMON.PINTREST :
                shareableIntents.shareOnWhatsapp(imageUri);
                break;
                case Constants.COMMON.SNAPCHAT :
                shareableIntents.shareOnWhatsapp(imageUri);
                break;*/
        }


    }


    public class SmsViewHolder extends RecyclerView.ViewHolder {
        public SmsItemBinding smsItemBinding;


        public SmsViewHolder(@NonNull SmsItemBinding smsItemBinding) {
            super(smsItemBinding.getRoot());
            this.smsItemBinding = smsItemBinding;
            itemBinding = smsItemBinding;
            this.smsItemBinding.setClickHandler(SmsItemAdapter.this);

        }

        public void bind(SmsDetailModel obj, int position) {
            smsItemBinding.setVariable(BR.smsDetailModel, obj); //smsModel name will be same as in xml model object name
            smsItemBinding.executePendingBindings();

            smsTOBeShared = obj;

            if (obj.getmFavourite() != null && obj.getmFavourite().size() != 0) {
                for (SmsFavouriteModel favouriteModel : obj.getmFavourite()) {
                    if (favouriteModel.getDeviceId().equals(Utils.getMyDeviceId(mCtx))) {
                        smsItemBinding.cbLike.setChecked(true);
                        break;

                    } else {
                        smsItemBinding.cbLike.setChecked(false);
                    }
                }
            } else {
                smsItemBinding.cbLike.setChecked(false);
            }

            smsItemListeners(obj, position);
        }


        private void smsItemListeners(SmsDetailModel obj, int position) {

            smsItemBinding.ivWhatsapp.setOnClickListener(v -> {
                shareLayoutGone();
                ((Main2Activity) mCtx).vibrate();
                if (CheckPermissions.isStoragePermissionGranted(mCtx)) {
                    BitmapConversion(obj,Constants.COMMON.WHATSAPP);
                }
            });
            smsItemBinding.ivFacebook.setOnClickListener(v -> {
                shareLayoutGone();
                ((Main2Activity) mCtx).vibrate();
                shareableIntents.shareOnFbMesenger(smsTOBeShared.getImage());

            });
            smsItemBinding.ivTwitter.setOnClickListener(v -> {
                shareableIntents.shareOnTwitter(v, smsTOBeShared.getImage());
                shareLayoutGone();
                ((Main2Activity) mCtx).vibrate();
            });
            smsItemBinding.ivInstagram.setOnClickListener(v -> {
                shareableIntents.shareOnInstagram(smsTOBeShared.getImage());
                shareLayoutGone();
                ((Main2Activity) mCtx).vibrate();
            });
            smsItemBinding.ivPintrest.setOnClickListener(v -> {
                shareableIntents.shareOnPintrest(v, smsTOBeShared.getImage());
                shareLayoutGone();
                ((Main2Activity) mCtx).vibrate();
            });
            smsItemBinding.ivSnapchat.setOnClickListener(v -> {
                shareableIntents.shareOnSnapChat(smsTOBeShared.getImage());
                shareLayoutGone();
                ((Main2Activity) mCtx).vibrate();
            });
            smsItemBinding.ivSms.setOnClickListener(v -> {
                shareLayoutGone();

            });
            smsItemBinding.rlSms.setOnClickListener(v -> {
                shareLayoutGone();
            });

            smsItemBinding.cbLike.setOnCheckedChangeListener((buttonView, isChecked) -> {

                ((Main2Activity) mCtx).vibrate();

                smsItemBinding.progressBar.setVisibility(View.VISIBLE);
                smsItemBinding.cbLike.setClickable(false);
                if (isChecked) {
                    fragmentSms.addSmsToFav(obj, position, smsItemBinding.progressBar, smsItemBinding.cbLike);
                } else {
                    for (SmsFavouriteModel favouriteModel : obj.getmFavourite()) {
                        if (favouriteModel.getDeviceId().equals(Utils.getMyDeviceId(mCtx))) {
                            fragmentSms.removeFromFav(obj,position,favouriteModel.getId(), smsItemBinding.progressBar, smsItemBinding.cbLike);
                            break;

                        }
                    }

                }
                //FUNCTIONALITY NOT IN SCOPE FOR THE TIME BEING
//                    SmsRepository.getInstance().InsertFavourite(obj,fragmentSms);
            });
            smsItemBinding.llShareSms.setOnClickListener(v -> {
                ((Main2Activity) mCtx).vibrate();
                if (isSharelayoutVisible) {
                    shareLayoutGone();
                } else {

                    smsItemBinding.llShareSms.setBackgroundDrawable(mCtx.getDrawable(R.drawable.bottom_round_corner_bg));
                    int padding = (int) mCtx.getResources().getDimension(R.dimen._10sdp);
                    smsItemBinding.llShareSms.setPadding(padding, padding, padding, padding);
                    smsItemBinding.llShareOptionsSms.setVisibility(View.VISIBLE);
                    isSharelayoutVisible = true;
                }
            });
        }

        private void BitmapConversion(SmsDetailModel obj, String platform) {
            shareLayoutGone();
            try {
                URL url = new URL(obj.getImage());
                new LoadImageBitmap(mCtx,SmsItemAdapter.this::onBitmapLoaded,platform).execute(url);

            } catch (IOException e) {
                System.out.println(e);
            }
        }

        private void shareLayoutGone() {

            isSharelayoutVisible = false;
            smsItemBinding.llShareSms.setBackground(null);
            smsItemBinding.llShareOptionsSms.setVisibility(View.GONE);
        }

    }




    public class AdViewHolder extends RecyclerView.ViewHolder {
        public AdItemBinding adItemBinding;


        public AdViewHolder(@NonNull AdItemBinding adItemBinding) {
            super(adItemBinding.getRoot());
            this.adItemBinding = adItemBinding;

        }


    }


}
