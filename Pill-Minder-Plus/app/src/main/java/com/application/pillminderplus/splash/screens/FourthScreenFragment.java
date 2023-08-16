package com.application.pillminderplus.splash.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.application.pillminderplus.R;
import com.application.pillminderplus.splash.FourthScreenPresenter;
import com.application.pillminderplus.splash.FourthScreenRepository;

public class FourthScreenFragment extends Fragment {

    private View view;
    private FourthScreenPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fourth_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        presenter = new FourthScreenPresenter(this, FourthScreenRepository.getInstance(view.getContext()));

        view.findViewById(R.id.btnFinish).setOnClickListener(view1 -> {
            presenter.setBoardingFinish(true);
        });
    }

    public void setBoardingFinishComplete() {
        Navigation.findNavController(view).navigate(R.id.action_viewPagerFragment_to_loginFragment);
    }}

