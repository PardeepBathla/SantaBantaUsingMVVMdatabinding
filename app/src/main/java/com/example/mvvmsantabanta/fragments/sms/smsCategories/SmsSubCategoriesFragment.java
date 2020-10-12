package com.example.mvvmsantabanta.fragments.sms.smsCategories;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.activity.home.homeModels.MenuCategoryChild;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuInfo;
import com.example.mvvmsantabanta.databinding.FragmentSubCategoriesBinding;
import com.example.mvvmsantabanta.fragments.sms.FragmentSms;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SubCategoryResponse;
import com.example.mvvmsantabanta.nestedBackStack.BaseFragment;
import com.example.mvvmsantabanta.transformScrollView.DSVOrientation;
import com.example.mvvmsantabanta.transformScrollView.DiscreteScrollView;
import com.example.mvvmsantabanta.transformScrollView.InfiniteScrollAdapter;
import com.example.mvvmsantabanta.transformScrollView.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SmsSubCategoriesFragment extends BaseFragment implements DiscreteScrollView.OnItemChangedListener {
    private FragmentSubCategoriesBinding fragmentSubCategoriesBinding;
    private ArrayList<SmsCategoriesResponseModel> smsCategoriesArrayList;
    private ArrayList<NavMenuInfo> smsNavCategoriesArrayList;
    private SmsSubCategoriesAdapter smsSubCategoriesAdapter;
    private InfiniteScrollAdapter infiniteAdapter;
    private int selected_category_position;


    public SmsSubCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSubCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_categories, container, false);
        View view = fragmentSubCategoriesBinding.getRoot();

        if (getArguments() != null) {
            selected_category_position = getArguments().getInt(Constants.COMMON.SELECTED_CATEGORY_POSITION);
            if (Constants.REDIRECTING_FROM.equalsIgnoreCase(Constants.SMS_CATEGORIES_SCREEN)) {
                smsCategoriesArrayList = (ArrayList<SmsCategoriesResponseModel>) getArguments().getSerializable(Constants.COMMON.CATEGORIES_SUBCATEGORIES);
            } else if (Constants.REDIRECTING_FROM.equalsIgnoreCase(Constants.NAVIGATION_DRAWER_SCREEN)) {
                smsNavCategoriesArrayList = (ArrayList<NavMenuInfo>) getArguments().getSerializable(Constants.COMMON.CATEGORIES_SUBCATEGORIES);
                convertDataFRomNavModelTOSMsModel();
            }

        }

        setDiscreetItemScrollView();
        setSubCategoriesAdapter();
        onItemChanged(selected_category_position);
        return view;
    }

    private void convertDataFRomNavModelTOSMsModel() {

        smsCategoriesArrayList = new ArrayList<>();
        for (NavMenuInfo navMenuInfo :smsNavCategoriesArrayList) {


            SmsCategoriesResponseModel smsCategoriesResponseModel = new SmsCategoriesResponseModel();
            smsCategoriesResponseModel.setId(navMenuInfo.getId());
            smsCategoriesResponseModel.setName(navMenuInfo.getName());
            smsCategoriesResponseModel.setIcon(navMenuInfo.getIcon());

            List<SubCategoryResponse> subCategoryResponseList = new ArrayList<>();
            for (MenuCategoryChild menuCategoryChild:navMenuInfo.getMenuCategoryChildren()) {
                SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
                subCategoryResponse.setId(menuCategoryChild.getId());
                subCategoryResponse.setName(menuCategoryChild.getName());
                subCategoryResponse.setSubcatId(menuCategoryChild.getSubcatId());
                subCategoryResponse.setSlug(menuCategoryChild.getSlug());

                subCategoryResponseList.add(subCategoryResponse);
            }

            smsCategoriesResponseModel.setChildren(subCategoryResponseList);
            smsCategoriesArrayList.add(smsCategoriesResponseModel);
        }
    }

    private void setSubCategoriesAdapter() {
        smsSubCategoriesAdapter = new SmsSubCategoriesAdapter(this, getActivity(), smsCategoriesArrayList.get(selected_category_position));
        fragmentSubCategoriesBinding.setSubCatAdapter(smsSubCategoriesAdapter);
    }

    private void setDiscreetItemScrollView() {
        fragmentSubCategoriesBinding.itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        fragmentSubCategoriesBinding.itemPicker.setOverScrollEnabled(false);

        fragmentSubCategoriesBinding.itemPicker.addOnItemChangedListener(this::onCurrentItemChanged);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new DiscreteScollAnimationAdapter(smsCategoriesArrayList));
        fragmentSubCategoriesBinding.itemPicker.setAdapter(infiniteAdapter);
        fragmentSubCategoriesBinding.itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        fragmentSubCategoriesBinding.itemPicker.scrollToPosition(infiniteAdapter.getClosestPosition(selected_category_position)); //position becomes selected
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int i) {
        onItemChanged(infiniteAdapter.getRealPosition(i));
    }

    private void onItemChanged(int i) {
//        Toast.makeText(getActivity(), i+"", Toast.LENGTH_SHORT).show();
        smsSubCategoriesAdapter.updateSubCategoryAccordingToCategory(smsCategoriesArrayList.get(i));
    }

    public void enterNextFragment(FragmentSms smsFragment, String slug, int id) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG_ID, id);
        bundle.putString(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG, slug);
        smsFragment.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // Store the Fragment in stack
        transaction.addToBackStack(null);

        transaction.replace(R.id.rootlay, smsFragment).commit();
    }

}
