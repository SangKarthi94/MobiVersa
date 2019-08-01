package com.android.mobiversa.MVP.Presenter;


import com.google.firebase.database.FirebaseDatabase;

public interface RoomsPresenter {
    void getRoomsDetails(FirebaseDatabase firebaseDatabase);

    void setAvailableRooms(FirebaseDatabase firebaseDatabase,String availableRooms);
    void setBookedRooms(FirebaseDatabase firebaseDatabase,String bookedRooms);
}
