package com.application.pillminderplus.forgotpassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.application.pillminderplus.R;
import com.application.pillminderplus.Helper;
import com.application.pillminderplus.network.FirebaseClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
//Screen of forget password were you can reset the password by entering the mail and sending request for reset to firebase
public class ForgotPasswordFragment extends Fragment  {
    private View view;
    private ForgotPasswordPresenter presenter;
    private ProgressBar progressBar;
    private TextInputEditText textInputEditTextEmail;
    private Button btnReset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        initComponents();
        btnReset.setOnClickListener(view1 -> {
            if (checkInputsData()) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.sendPasswordResetEmail(
                        Objects.requireNonNull(textInputEditTextEmail.getText()).toString().trim());
            }
        });
    }

    private void initComponents() {
        presenter = new ForgotPasswordPresenter(this,
                ForgotPasswordRepository.getInstance(FirebaseClient.getInstance()));
        progressBar = view.findViewById(R.id.progressBar);
        textInputEditTextEmail = view.findViewById(R.id.textInputEditTextEmail);
        btnReset = view.findViewById(R.id.btnReset);
    }

    public void linkSentSuccessfully() {
        Toast.makeText(view.getContext(), R.string.reset_link_sent, Toast.LENGTH_LONG).show();
    }

    public void navigateToLoginScreen() {
        Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
    }

    public void onError(String error) {
        Helper.showAlert(view.getContext(), R.string.error, error, R.drawable.error_icon);
    }

    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    private boolean checkInputsData() {
        boolean validInputs = true;

        if (Objects.requireNonNull(textInputEditTextEmail.getText()).toString().trim().isEmpty()) {
            textInputEditTextEmail.setError(getText(R.string.type_your_email));
            textInputEditTextEmail.requestFocus();
            validInputs = false;
        }
        return validInputs;
    }
}