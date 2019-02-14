package com.example.songapp.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.songapp.R;

/**
 * THE DIALOG CLASS FROM WHICH WE BUILD
 * OUR CUSTOM DIALOG IS PRETTY SIMILAR
 * TO THE ACTIVITY CLASS.
 * There are some methods that you will
 * see in the Dialog class such
 * as getContext() or setContentView()
 */
public class CustomDialog extends Dialog {

private Button positiveButton;
private TextView messageTextView;
private Button secondaryButton;
private String message;
private CustomDialogCallback customDialogCallback;
    /**
     * You need the following parameters in order to build a custom
     * dialog
     * @param context - the context of the activity* @param message - the main message that is gonna be displayed
     * @param message
     * @param  activity
     */
    public CustomDialog(@NonNull Context context, String message, Activity activity) {
        super(context);
        this.message = message;
        customDialogCallback = (CustomDialogCallback) activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);
        //initialize the interface

        //we want this message to be clear to the user
        //and maybe the user may hide our dialog by mistake
        setCanceledOnTouchOutside(false);

        messageTextView = findViewById(R.id.message_custom_dialog);
        messageTextView.setText(message);

        positiveButton = findViewById(R.id.positive_button_dialog);
        positiveButton.setOnClickListener(view->customDialogCallback.positiveButtonClicked());



    }


    /**
     * YOU NEED TO IMPLEMENT THIS INTERFACE IF YOU WANT YOU DIALOG
     * TO DO SOMETHING
     */
    public interface CustomDialogCallback {
        //YOU DON'T NEED TO HAVE THE KEYWORD PUBLIC FOR THE METHODS
        void positiveButtonClicked();
        void secondButtonClicked();
    }



}
