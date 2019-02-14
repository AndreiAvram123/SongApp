package com.example.songapp.model;

public class Helper {

    public static boolean isEmailValid(String email){
        return email.matches("[a-zA-Z0-9]{4}[a-zA-Z0-9]*@[a-z]+\\.[a-z]+");
    }
}
