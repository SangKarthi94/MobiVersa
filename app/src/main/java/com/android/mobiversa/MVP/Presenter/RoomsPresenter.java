package com.android.mobiversa.MVP.Presenter;


import com.google.firebase.database.FirebaseDatabase;

public interface RoomsPresenter {
    void getRoomsDetails(FirebaseDatabase firebaseDatabase, String date);

    void setAvailableRooms(FirebaseDatabase firebaseDatabase,String availableRooms, String date);
    void setBookedRooms(FirebaseDatabase firebaseDatabase,String bookedRooms, String date);
}
