package com.example.mvvmsantabanta.fragments.jokes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mvvmsantabanta.BR;
import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.utility.ShareableIntents;
import com.example.mvvmsantabanta.utility.Utils;
import com.example.mvvmsantabanta.activity.home.Main2Activity;
import com.example.mvvmsantabanta.databinding.AdItemBinding;
import com.example.mvvmsantabanta.databinding.JokesItemBinding;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDetailModel;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesFavouriteModel;

public class JokesItemAdapter extends PagedListAdapter<JokesDetailModel, RecyclerView.ViewHolder> implements  View.OnClickListener {
    //instead of paged Recyclerview.Adapter we extended PagedListAdapter of Paging Library

    private static final int AD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;
    private static Context mCtx;
    private static DiffUtil.ItemCallback<JokesDetailModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<JokesDetailModel>() {
        @Override
        public boolean areItemsTheSame(JokesDetailModel oldItem, JokesDetailModel newItem) {
            return oldItem.getContent() == newItem.getContent();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(JokesDetailModel oldItem, JokesDetailModel newItem) {
            return oldItem.equals(newItem);
        }
    };
    private final ShareableIntents shareableIntents;
    private JokesItemBinding itemBinding;
    FragmentJokes fragmentJokes;
    ProgressBar progressBar;
    private boolean isSharelayoutVisible = false;
    private boolean isDialogSharelayoutVisible = false;
    private JokesDetailModel jokeTOBeShared;


    protected JokesItemAdapter(Context mCtx, FragmentJokes fragmentJokes, ProgressBar progressBar) {
        super(DIFF_CALLBACK);
        JokesItemAdapter.mCtx = mCtx;
        this.fragmentJokes = fragmentJokes;
        this.progressBar = progressBar;
        shareableIntents = new ShareableIntents(mCtx);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
          /*  case AD_TYPE:
                AdItemBinding adItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ad_item, parent, false);
                return new JokesItemAdapter.AdViewHolder(adItemBinding);*/
            case CONTENT_TYPE:
                JokesItemBinding jokesItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.jokes_item, parent, false);
                return new JokesViewHolder(jokesItemBinding);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        progressBar.setVisibility(View.GONE);
        if (holder instanceof AdViewHolder) {

//            ((JokesItemAdapter.AdViewHolder) holder).adItemBinding.tvAd.setText("Google ads");
        } else if (holder instanceof JokesViewHolder) {

            JokesDetailModel dataModel = getItem(position);
            if (dataModel != null) {

                ((JokesViewHolder) holder).bind(dataModel, position);
//            }
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
//        if (position % Constants.COMMON.AdPlacementPosition == 0 && position != 0)
//            return AD_TYPE;
//        else
        return CONTENT_TYPE;
    }






    public void fullScreenDialog(JokesDetailModel model, int position, boolean ischecked) {
        Dialog dialog = new Dialog(mCtx, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.full_screen_joke_dialog);
        TextView tv_content = dialog.findViewById(R.id.tvContent);
        LinearLayout ll_share_joke = dialog.findViewById(R.id.ll_share_joke);
        LinearLayout ll_share_options_joke = dialog.findViewById(R.id.ll_share_options_joke);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_categories = dialog.findViewById(R.id.tv_categories);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        TextView tv_fav_count = dialog.findViewById(R.id.tv_fav_count);
        ProgressBar progress_bar = dialog.findViewById(R.id.progress_bar);
        ScrollView content_scroll = dialog.findViewById(R.id.content_scroll);

        tv_fav_count.setText(String.valueOf(model.getFav_count()));

        ll_share_joke.setOnClickListener(v -> {
            if (isDialogSharelayoutVisible) {
                DialogshareLayoutGone(ll_share_joke,ll_share_options_joke);
            } else {
                ll_share_joke.setBackgroundDrawable(mCtx.getDrawable(R.drawable.bottom_round_corner_bg));
                int padding = (int) mCtx.getResources().getDimension(R.dimen._10sdp);
                ll_share_joke.setPadding(padding, padding, padding, padding);
                ll_share_options_joke.setVisibility(View.VISIBLE);
                isDialogSharelayoutVisible = true;
            }
        });
        content_scroll.setOnClickListener(v -> DialogshareLayoutGone(ll_share_joke,ll_share_options_joke));
        iv_close.setOnClickListener(v -> dialog.dismiss());
        CheckBox cb_like = dialog.findViewById(R.id.cb_like);
        cb_like.setChecked(ischecked);

        tv_content.setText(Html.fromHtml(model.getContent()));
        tv_title.setText(Html.fromHtml(model.getTitle()));
        if (model.getCategories() != null && model.getCategories().size() != 0) {
            tv_categories.setText(Html.fromHtml(model.getCategories().get(0).getName()));
        }

        cb_like.setOnCheckedChangeListener((buttonView, isChecked) -> {

            ((Main2Activity)mCtx).vibrate();

            if (isChecked){
                if (!tv_fav_count.getText().toString().equals("")) {
                    tv_fav_count.setText(String.valueOf(Integer.parseInt(tv_fav_count.getText().toString())+1));
                }else {
                    tv_fav_count.setText("0");
                }
            }else {
                if (!tv_fav_count.getText().equals("0")) {
                    tv_fav_count.setText(String.valueOf(Integer.parseInt(tv_fav_count.getText().toString())-1));
                }
            }
            progress_bar.setVisibility(View.VISIBLE);
            cb_like.setClickable(false);
            onFavCheckChanged(isChecked, model, position, cb_like, progress_bar);
            //FUNCTIONALITY NOT IN SCOPE FOR THE TIME BEING
//                    SmsRepository.getInstance().InsertFavourite(obj,fragmentSms);
        });
        dialog.show();
    }

    private void DialogshareLayoutGone(LinearLayout ll_share_joke, LinearLayout ll_share_options_joke) {
        isDialogSharelayoutVisible = false;
        ll_share_joke.setBackground(null);
        ll_share_options_joke.setVisibility(View.GONE);
    }

    private void onFavCheckChanged(boolean isChecked, JokesDetailModel obj, int position, CheckBox cbLike, ProgressBar progress_bar) {
        if (isChecked) {
            fragmentJokes.addJokeToFav(obj, position, cbLike, progress_bar);
        } else {
            if (obj.getmFavourite() != null) {
                for (JokesFavouriteModel favouriteModel : obj.getmFavourite()) {
                    if (favouriteModel.getDeviceId().equals(Utils.getMyDeviceId(mCtx))) {
                        fragmentJokes.removeFromFav(obj,favouriteModel.getId(), position, cbLike, progress_bar);
                        break;

                    }
                }
            }

        }
    }

    @Override
    public void onClick(View v) {



    }

    public class JokesViewHolder extends RecyclerView.ViewHolder {
        public JokesItemBinding jokesItemBinding;


        public JokesViewHolder(@NonNull JokesItemBinding jokesItemBinding) {
            super(jokesItemBinding.getRoot());
            this.jokesItemBinding = jokesItemBinding;
            itemBinding = jokesItemBinding;


        }

        public void bind(JokesDetailModel obj, int position) {
            if (position % 2 == 0) {
                jokesItemBinding.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.brown));
            } else {
                jokesItemBinding.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.purple));
            }


            jokesItemBinding.setVariable(BR.jokesDetailModel, obj); //jokesDetailModel name will be same as in xml model object name
            jokesItemBinding.executePendingBindings();

            jokeTOBeShared = obj;

            if (obj.getmFavourite() != null && obj.getmFavourite().size() != 0) {
                for (JokesFavouriteModel favouriteModel : obj.getmFavourite()) {
                    if (favouriteModel.getDeviceId().equals(Utils.getMyDeviceId(mCtx))) {
                        jokesItemBinding.cbLike.setChecked(true);
                        break;

                    } else {
                        jokesItemBinding.cbLike.setChecked(false);
                    }
                }
            } else {
                jokesItemBinding.cbLike.setChecked(false);
            }

            jokesItemListener(obj, position);


        }



        private void jokesItemListener(JokesDetailModel obj, int position) {

            jokesItemBinding.ivWhatsapp.setOnClickListener(v -> {
                shareLayoutGone();
                ((Main2Activity)mCtx).vibrate();
                shareableIntents.shareOnWhatsapp(obj.getContent());

            });
            jokesItemBinding.ivFacebook.setOnClickListener(v -> {
                shareLayoutGone();
                ((Main2Activity)mCtx).vibrate();
                shareableIntents.shareOnFbMesenger(obj.getContent());

            });
            jokesItemBinding.ivTwitter.setOnClickListener(v -> {
                shareableIntents.shareOnTwitter(v,obj.getContent());
                ((Main2Activity)mCtx).vibrate();
                shareLayoutGone();
            });
            jokesItemBinding.ivInstagram.setOnClickListener(v -> {
                shareableIntents.shareOnInstagram(obj.getContent());
                ((Main2Activity)mCtx).vibrate();
                shareLayoutGone();
            });
            jokesItemBinding.ivPintrest.setOnClickListener(v -> {
                shareableIntents.shareOnPintrest(v,obj.getContent());
                ((Main2Activity)mCtx).vibrate();
                shareLayoutGone();
            });
            jokesItemBinding.ivSnapchat.setOnClickListener(v -> {
                shareableIntents.shareOnSnapChat(obj.getContent());
                ((Main2Activity)mCtx).vibrate();
                shareLayoutGone();
            });

            jokesItemBinding.tvContent.setOnClickListener(v -> {
                shareLayoutGone();
                fullScreenDialog(obj, position, jokesItemBinding.cbLike.isChecked());
            });

                jokesItemBinding.cbLike.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (buttonView.isPressed()) {
                        jokesItemBinding.progressBar.setVisibility(View.VISIBLE);
                        jokesItemBinding.cbLike.setClickable(false);
                        onFavCheckChanged(isChecked, obj, position, jokesItemBinding.cbLike, jokesItemBinding.progressBar);
                    }
                    //FUNCTIONALITY NOT IN SCOPE FOR THE TIME BEING
    //                    SmsRepository.getInstance().InsertFavourite(obj,fragmentSms);
                });



            jokesItemBinding.llShareJoke.setOnClickListener(v -> {

                ((Main2Activity)mCtx).vibrate();

                if (isSharelayoutVisible) {
                    shareLayoutGone();
                } else {
                    jokesItemBinding.llShareJoke.setBackgroundDrawable(mCtx.getDrawable(R.drawable.bottom_round_corner_bg));
                    int padding = (int) mCtx.getResources().getDimension(R.dimen._10sdp);
                    jokesItemBinding.llShareJoke.setPadding(padding, padding, padding, padding);
                    jokesItemBinding.llShareOptionsJoke.setVisibility(View.VISIBLE);
                    isSharelayoutVisible = true;
                }
            });
        }

        private void shareLayoutGone() {

            isSharelayoutVisible = false;
            jokesItemBinding.llShareJoke.setBackground(null);
            jokesItemBinding.llShareOptionsJoke.setVisibility(View.GONE);
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
