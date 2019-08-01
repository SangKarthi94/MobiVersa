package com.android.mobiversa.MVP.Presenter;


import com.google.firebase.database.FirebaseDatabase;

public interface LoginPresenter {
    void login(String useName,String password,boolean signUp);

    void logOut();
}
