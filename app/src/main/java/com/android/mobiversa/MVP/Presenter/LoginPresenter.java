package com.android.mobiversa.MVP.Presenter;


public interface LoginPresenter {
    void login(String useName,String password,boolean signUp);
    void logOut();
}
