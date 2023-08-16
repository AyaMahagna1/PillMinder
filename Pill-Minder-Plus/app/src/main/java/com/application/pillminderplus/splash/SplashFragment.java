package com.application.pillminderplus.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.home.HomeActivity;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class SplashFragment extends Fragment  {

    private SplashPresenter presenter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        Glide.with(getContext()).load(R.drawable.pill_minder_plus).into((ImageView) view.findViewById(R.id.imageView));
        presenter = new SplashPresenter(this, SplashRepository.getInstance(view.getContext()));
        presenter.isBoardingFinish();
    }

    public void isBoardingFinish(boolean isFinish) {
        new Handler().postDelayed(() -> {
            if (isFinish) {
                presenter.isUserLogin();
            } else {
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_viewPagerFragment);
            }
        }, 3000);
    }

    public void isUserLogin(boolean isLogin) {
        if (isLogin) {
            startActivity(new Intent(getActivity(), HomeActivity.class));
            Objects.requireNonNull(getActivity()).finish();
        } else {
            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment);
        }
    }
}