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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.songapp.R;

public class SignUpFragment extends Fragment {

    private EditText emailField;
    private EditText passwordField;
    private EditText reenteredPasswordField;
    private EditText nicknameField;
    private Button finishButton;
    private SignUpFragmentCallback signUpFragmentCallback;
    private TextView errorMessage;
    private ImageView backButton;
    private ProgressBar loadingBar;
    private TextView emailHint;
    private TextView passwordHint;
    private TextView reenteredPasswordHint;
    private TextView nicknameHint;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up,container,false);
        signUpFragmentCallback = (SignUpFragmentCallback) getActivity();


        initializeUI(view);

        finishButton.setOnClickListener(button -> signUserUp());

        backButton.setOnClickListener(image -> signUpFragmentCallback.switchBackToLoginFragment());
        return view;
    }

    private void signUserUp() {
        if(areCredentialsValid()){
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String nickname = nicknameField.getText().toString().trim();
            signUpFragmentCallback.signUpUser(email,password,nickname);
            toggleLoadingBar();
        }

    }

    public  void toggleLoadingBar() {
        if(loadingBar.getVisibility() == View.INVISIBLE) {
            loadingBar.setVisibility(View.VISIBLE);
            finishButton.setVisibility(View.INVISIBLE);
        }else {
            loadingBar.setVisibility(View.INVISIBLE);
            finishButton.setVisibility(View.VISIBLE);
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

    private boolean areCredentialsValid() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String reenteredPassword = reenteredPasswordField.getText().toString().trim();
        String nickname = nicknameField.getText().toString().trim();

          if(email.isEmpty()) {
              showErrorMessage(getString(R.string.invalid_email));
              return false;
          }

          if(password.isEmpty()) {
              showErrorMessage(getString(R.string.invalid_password));
              return false;
          }

          if(reenteredPassword.isEmpty())
          {
              showErrorMessage(getString(R.string.error_reenter_password));
              return false;
          }

          if(!reenteredPassword.equals(password)){
              showErrorMessage(getString(R.string.passwords_not_match));
              return false;
          }

          if(nickname.isEmpty())
          {
              showErrorMessage(getString(R.string.error_choose_nickname));
              return false;
          }

          return true;
    }

    private void showErrorMessage(String errorMessageText) {
        errorMessage.setText(errorMessageText);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void initializeUI(View view) {
        emailField = view.findViewById(R.id.email_field_sign_up);
        passwordField = view.findViewById(R.id.password_field_sign_up);
        reenteredPasswordField = view.findViewById(R.id.reentered_password_field);
        nicknameField = view.findViewById(R.id.nickname_field);


        emailHint = view.findViewById(R.id.email_hint_sign_up);
        passwordHint = view.findViewById(R.id.password_hint_sign_up);
        reenteredPasswordHint = view.findViewById(R.id.reentered_password_hint_sign_up);
        nicknameHint = view.findViewById(R.id.nickname_hint_sign_up);

        finishButton = view.findViewById(R.id.finish_button_sign_up);
        errorMessage = view.findViewById(R.id.error_message_sign_up);
        backButton = view.findViewById(R.id.back_button_sign_up);
        loadingBar = view.findViewById(R.id.loading_progress_bar_sign_up);


        applyEditTextOnFocusColorListener(emailField,emailHint);
        applyEditTextOnFocusColorListener(passwordField,passwordHint);
        applyEditTextOnFocusColorListener(reenteredPasswordField,reenteredPasswordHint);
        applyEditTextOnFocusColorListener(nicknameField,nicknameHint);


    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        reenteredPasswordField.setText("");
        nicknameField.setText("");
    }

    public  interface SignUpFragmentCallback {
        void signUpUser(String email,String password,String nickname);
        void switchBackToLoginFragment();
    }
}
