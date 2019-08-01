package com.android.mobiversa.MVP.View;


public interface LoginView {
    void loginSuccess();
    void gettingError(String s);
    void showLoadingProgressBar();
    void hideLoadingProgressBar();
}
