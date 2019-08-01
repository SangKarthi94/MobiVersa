package com.android.mobiversa.MVP.View;


public interface RoomsView {

    void setAvailableRoomsDetails(String availabeRooms);
    void setBookedRoomsDetails(String bookedRooms);
    void gettingError(String s);
    void showLoadingProgressBar();
    void hideLoadingProgressBar();
}
