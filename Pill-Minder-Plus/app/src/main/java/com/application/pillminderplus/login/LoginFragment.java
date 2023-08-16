package com.application.pillminderplus.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.application.pillminderplus.R;
import com.application.pillminderplus.Helper;
import com.application.pillminderplus.database.room.user.LocalSourceUser;
import com.application.pillminderplus.home.HomeActivity;
import com.application.pillminderplus.network.FirebaseClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
//Log in screen
public class LoginFragment extends Fragment {
    private View view;
    private LoginPresenter presenter;
    private TextView txtViewRegister, txtViewForgotPassword;
    private ProgressBar progressBar;
    private TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    private Button btnLogin;
    private ImageView imgGoogle, imgFacebook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
//        Glide.with(this).load(R.drawable.pill_minder_plus).into((ImageView) view.findViewById(R.id.imageView));

        initComponents();

        txtViewRegister.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.action_loginFragment_to_registerFragment));
        txtViewForgotPassword.setOnClickListener(view13 -> Navigation.findNavController(view13).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));
        btnLogin.setOnClickListener(view12 -> {
            if (checkInputsData()) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.signInWithEmailAndPassword(
                        Objects.requireNonNull(textInputEditTextEmail.getText()).toString().trim(),
                        Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim());
            }
        });
        imgGoogle.setOnClickListener(view14 -> {
            Intent signInIntent = presenter.getGoogleSignInClient(getActivity()).getSignInIntent();
            signInResultLauncher.launch(signInIntent);
        });
        imgFacebook.setOnClickListener(view15 -> Toast.makeText(view15.getContext(), R.string.coming_soon, Toast.LENGTH_LONG).show());
    }

    private final ActivityResultLauncher<Intent> signInResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // here we will handle the result of our intent
                if (result.getResultCode() == Activity.RESULT_OK) {
                    progressBar.setVisibility(View.VISIBLE);
                    Task<GoogleSignInAccount> signedInAccountFromIntent = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = signedInAccountFromIntent.getResult(ApiException.class);
                        presenter.signInWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Helper.showAlert(view.getContext(), R.string.error, getString(R.string.google_sign_in_failed) + e.getMessage(), R.drawable.error_icon);
                        e.printStackTrace();
                    }
                } else {
                    // cancelled
                    Toast.makeText(view.getContext(), String.valueOf(result.getResultCode()), Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void initComponents() {
        presenter = new LoginPresenter(this,
                LoginRepository.getInstance(FirebaseClient.getInstance(), LocalSourceUser.getInstance(view.getContext()), view.getContext()));

        txtViewRegister = view.findViewById(R.id.txtViewRegister);
        txtViewForgotPassword = view.findViewById(R.id.txtViewForgotPassword);
        progressBar = view.findViewById(R.id.progressBar);
        textInputEditTextEmail = view.findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = view.findViewById(R.id.textInputEditTextPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        imgGoogle = view.findViewById(R.id.imgViewGooglePlus);
        imgFacebook = view.findViewById(R.id.imgViewFacebook);
    }

    private boolean checkInputsData() {
        boolean validInputs = true;

        if (Objects.requireNonNull(textInputEditTextEmail.getText()).toString().trim().isEmpty()) {
            textInputEditTextEmail.setError(getText(R.string.type_your_email));
            textInputEditTextEmail.requestFocus();
            validInputs = false;
        } else if (Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim().isEmpty()) {
            textInputEditTextPassword.setError(getText(R.string.type_your_password));
            textInputEditTextPassword.requestFocus();
            validInputs = false;
        }

        return validInputs;
    }

    public void navigateToHomeScreen() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        requireActivity().finish();
    }

    public void onError(String error) {
        Helper.showAlert(view.getContext(), R.string.error, error, R.drawable.error_icon);
    }

    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }
}