package com.example.songapp.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.songapp.R;
import com.example.songapp.model.Helper;

public class LoginFragment extends Fragment {

    private LoginFragmentCallback loginFragmentCallback;
    private Button loginButton;
    private Button signUpButton;
    private EditText emailField;
    private EditText passwordField;
    private TextView errorMessage;
    private ProgressBar loadingBar;
    private TextView emailHint;
    private TextView passwordHint;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);
        loginFragmentCallback = (LoginFragmentCallback) getActivity();
        initializeUI(view);

        loginButton.setOnClickListener(button -> signUserIn());
        signUpButton.setOnClickListener(button -> loginFragmentCallback.showSignUpFragment());

        return view;
    }

    private void initializeUI(View view) {
        loginButton = view.findViewById(R.id.sign_in_button);
        signUpButton = view.findViewById(R.id.sign_up_button);
        emailField = view.findViewById(R.id.email_field_login);
        passwordField = view.findViewById(R.id.password_field_login);
        errorMessage = view.findViewById(R.id.error_message_login);
        loadingBar = view.findViewById(R.id.logging_progress_bar);
        emailHint = view.findViewById(R.id.email_hint_login);
        passwordHint = view.findViewById(R.id.password_hint_login);
        applyEditTextOnFocusColorListener(emailField,emailHint);
        applyEditTextOnFocusColorListener(passwordField,passwordHint);
    }


    public void toggleLoadingBar() {
        if(loadingBar.getVisibility() == View.INVISIBLE) {
            loadingBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            signUpButton.setVisibility(View.INVISIBLE);
        }else {
            loadingBar.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.VISIBLE);
        }

    }
    /**
     * This method is used to apply an OnFocusChangeListener object to the
     * passed editText in order to change the line color and hint color
     * depending on the focus
     * IF THE EditText has focus change
     * @param editText
     * @param hint
     */
    private void applyEditTextOnFocusColorListener(EditText editText,TextView hint) {
       editText.setOnFocusChangeListener((v,hasFocus)->{
            if(hasFocus){
                editText.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#76B900"), PorterDuff.Mode.SRC_ATOP);
                hint.setTextColor(Color.parseColor("#A5B253"));
            }else {
                editText.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                hint.setTextColor(Color.parseColor("#ffffff"));
            }
               }
       );
    }

    private void signUserIn() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if(areLoginCredentialsValid(email,password)){
            toggleLoadingBar();
            loginFragmentCallback.login(email,password);
        }
    }

    /**
     * This method is used to check if the user has entered the right details
     * @param email- the email as a String format
     * @param password - the password as a String format
     * @return - true if the credentials are valid
     *          -false otherwise
     */
    private boolean areLoginCredentialsValid(String email, String password) {
        if(email.isEmpty()){
            showErrorMessage(getString(R.string.invalid_email));
            return false;
        }
        if(!Helper.isEmailValid(email)) {
            showErrorMessage(getString(R.string.invalid_email));
            return false;
        }
        if(password.isEmpty()){
            showErrorMessage(getString(R.string.invalid_password));
            return false;
        }
        return true;

    }

    /**
     * This method is used to display a specific error message on the
     * screen
     * @param errorMessageText - the error message to be displayed on the screen
     */
    private void showErrorMessage(String errorMessageText) {
       errorMessage.setText(errorMessageText);
       errorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * The interface used to communicate with the parent activity
     *
     */
    public interface LoginFragmentCallback {
        void login(String email,String password);
        void showSignUpFragment();
    }
}
