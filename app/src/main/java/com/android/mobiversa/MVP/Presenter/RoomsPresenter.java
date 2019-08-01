package com.android.mobiversa.MVP.Presenter;


import com.google.firebase.database.FirebaseDatabase;

public interface RoomsPresenter {
    void getRoomsDetails(FirebaseDatabase firebaseDatabase,String availableRooms,String bookedRooms);
}
