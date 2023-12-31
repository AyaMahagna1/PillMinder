package com.application.pillminderplus.splash.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.pillminderplus.R;

import java.util.Objects;

public class SecondScreenFragment extends Fragment {

    private ViewPager2 viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.viewPager);

        view.findViewById(R.id.btnNext2).setOnClickListener(view1 -> viewPager.setCurrentItem(2));
    }
}