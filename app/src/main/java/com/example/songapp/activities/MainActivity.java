package com.example.songapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.songapp.R;
import com.example.songapp.fragments.LoginFragment;
import com.example.songapp.fragments.SignUpFragment;
import com.example.songapp.model.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * This activity is used in order
 * to allow the user to login
 * or to sign up
 *
 */
public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentCallback,
        SignUpFragment.SignUpFragmentCallback, CustomDialog.CustomDialogCallback {

    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;
    private FirebaseAuth firebaseAuth;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isInternetAvailable()) {
            displayDialog( getString(R.string.no_internet_connection_error));
            setContentView(R.layout.no_internet_connection_layout);
            ImageView refreshState = findViewById(R.id.refresh_image_no_internet);

            refreshState.setOnClickListener(view -> {

                if (isInternetAvailable())
                    initializeUI();
                else {
                    displayDialog( getString(R.string.no_internet_connection_error));
                }
            });
        } else {
            initializeUI();
        }
    }

    private void initializeUI() {


        setContentView(R.layout.activity_main);

        initializeFragments();

        firebaseAuth = FirebaseAuth.getInstance();

        showLoginFragment();

    }


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return currentNetworkInfo != null && currentNetworkInfo.isConnected();
    }

    private void initializeFragments() {
        fragmentManager = getSupportFragmentManager();
        loginFragment = new LoginFragment();
        signUpFragment = new SignUpFragment();
    }

    private void showLoginFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraint_layout_main, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
         FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
            if(currentUser.isEmailVerified()){
                startPlayerActivity();
            }else{

            }
        }
    }

    private void startPlayerActivity() {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    /**
     * *****************************THIS METHODS ARE FROM FRAGMENTS INTERFACES *******************************
     */

    @Override
    public void login(String email, String password) {
        if (isInternetAvailable()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                startPlayerActivity();
                            } else {
                                displayDialog(getString(R.string.verification_email_sent));
                                loginFragment.toggleLoadingBar();
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                            loginFragment.toggleLoadingBar();

                        }
                    });
        }
    }

    @Override
    public void showSignUpFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraint_layout_main, signUpFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void signUpUser(String email, String password, String nickname) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        changeUserNickname(nickname);
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        displayDialog(getString(R.string.verification_email_sent));
                        Toast.makeText(this, getString(R.string.profile_created), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.error_create_account), Toast.LENGTH_SHORT).show();
                        signUpFragment.toggleLoadingBar();
                        signUpFragment.clearFields();
                    }
                });
    }

    private void displayDialog(String text) {
         customDialog = new CustomDialog(this, text, this);
         customDialog.show();
    }


    private void changeUserNickname(String nickname) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(nickname)
                .build();
        firebaseAuth.getCurrentUser().updateProfile(userProfileChangeRequest);
    }

    @Override
    public void switchBackToLoginFragment() {
        showLoginFragment();
    }

    /**
     * ***************************************Methods from the Custom Dialog interface *************************************
     */
    @Override
    public void positiveButtonClicked() {
        customDialog.hide();
    }


    @Override
    public void secondButtonClicked() {


    }
}







