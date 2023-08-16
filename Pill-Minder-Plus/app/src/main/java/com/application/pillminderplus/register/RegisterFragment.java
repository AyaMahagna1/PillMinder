package com.application.pillminderplus.register;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
//Register screen
public class RegisterFragment extends Fragment  {
    private View view;
    private RegisterPresenter presenter;
    private CircleImageView profile_image;
    private ProgressBar progressBarImg, progressBar;
    private Button btnRegister;
    private TextView txtViewAlreadyRegistered;
    private TextInputEditText textInputEditTextName, textInputEditTextEmail, textInputEditTextPassword, textInputEditTextRePassword;
    private String profileImageURI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        initComponents();
        profile_image.setOnClickListener(view1 -> getProfileImage());
        txtViewAlreadyRegistered.setOnClickListener(view12 -> Navigation.findNavController(view12).navigate(R.id.action_registerFragment_to_loginFragment));
        btnRegister.setOnClickListener(view13 -> {
            if (checkInputsData()) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.createUserOnFirebase(
                        Objects.requireNonNull(textInputEditTextName.getText()).toString().trim(),
                        Objects.requireNonNull(textInputEditTextEmail.getText()).toString().trim(),
                        Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim(),
                        profileImageURI);
            }
        });
    }

    private void initComponents() {
        presenter = new RegisterPresenter(this,
                RegisterRepository.getInstance(FirebaseClient.getInstance(), LocalSourceUser.getInstance(view.getContext()),view.getContext()));

        profile_image = view.findViewById(R.id.profile_image);
        progressBarImg = view.findViewById(R.id.progressBarImg);
        progressBar = view.findViewById(R.id.progressBar);
        btnRegister = view.findViewById(R.id.btnRegister);
        txtViewAlreadyRegistered = view.findViewById(R.id.txtViewAlreadyRegistered);
        textInputEditTextName = view.findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = view.findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = view.findViewById(R.id.textInputEditTextPassword);
        textInputEditTextRePassword = view.findViewById(R.id.textInputEditTextRePassword);
    }

    private void getProfileImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(galleryIntent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // here we will handle the result of our intent
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // image picked ... get uri of image
                    Intent data = result.getData();
                    Uri uriProfileImage = Objects.requireNonNull(data).getData();

                    profile_image.setImageURI(uriProfileImage);
                    progressBarImg.setVisibility(View.VISIBLE);
                    presenter.uploadProfileImage(uriProfileImage);
                } else {
                    // cancelled
                    Toast.makeText(view.getContext(), R.string.img_picked_cancelled, Toast.LENGTH_SHORT).show();
                }
            }
    );

    public void setProfileImageURI(String uri) {
        profileImageURI = uri;
    }

    public void onImgUploadError(String error) {
        Helper.showAlert(view.getContext(), R.string.error, error, R.drawable.error_icon);
    }

    public void hideImgProgressbar() {
        progressBarImg.setVisibility(View.GONE);
    }

    public void navigateToHomeScreen() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        Objects.requireNonNull(getActivity()).finish();
    }

    public void onError(String error) {
        Helper.showAlert(view.getContext(), R.string.error, error, R.drawable.error_icon);
    }

    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    private boolean checkInputsData() {
        boolean validInputs = true;

        if (profileImageURI == null || profileImageURI.isEmpty()) {
            Toast.makeText(view.getContext(), R.string.set_your_profile_img, Toast.LENGTH_SHORT).show();
            validInputs = false;
        } else if (Objects.requireNonNull(textInputEditTextName.getText()).toString().trim().isEmpty()) {
            textInputEditTextName.setError(getText(R.string.type_your_full_name));
            textInputEditTextName.requestFocus();
            validInputs = false;
        } else if (Objects.requireNonNull(textInputEditTextEmail.getText()).toString().trim().isEmpty()) {
            textInputEditTextEmail.setError(getText(R.string.type_your_email));
            textInputEditTextEmail.requestFocus();
            validInputs = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(textInputEditTextEmail.getText().toString().trim()).matches()) {
            textInputEditTextEmail.setError(getText(R.string.type_email_address_correctly));
            textInputEditTextEmail.requestFocus();
            validInputs = false;
        } else if (Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim().isEmpty()) {
            textInputEditTextPassword.setError(getText(R.string.type_your_password));
            textInputEditTextPassword.requestFocus();
            validInputs = false;
        } else if (textInputEditTextPassword.getText().toString().trim().length() < 6) {
            textInputEditTextPassword.setError(getText(R.string.password_length));
            textInputEditTextPassword.requestFocus();
            validInputs = false;
        } else if (Objects.requireNonNull(textInputEditTextRePassword.getText()).toString().trim().isEmpty()) {
            textInputEditTextRePassword.setError(getText(R.string.confirm_your_password));
            textInputEditTextRePassword.requestFocus();
            validInputs = false;
        } else if (!textInputEditTextPassword.getText().toString().trim().equals(textInputEditTextRePassword.getText().toString().trim())) {
            Toast.makeText(view.getContext(), R.string.error_msg_password_incompatible, Toast.LENGTH_SHORT).show();
            validInputs = false;
        }

        return validInputs;
    }
}