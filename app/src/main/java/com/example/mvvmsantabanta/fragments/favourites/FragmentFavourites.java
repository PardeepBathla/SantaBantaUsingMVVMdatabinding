package com.example.mvvmsantabanta.fragments.favourites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvmsantabanta.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavourites extends Fragment {

    public FragmentFavourites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);

        //get Favourite Sms From Table in RoomDb Table
    }
}
