package com.android.mobiversa.MVP.PresenterImpl

import android.content.Context
import android.support.design.widget.Snackbar
import android.util.Log
import com.android.mobiversa.MVP.Presenter.RoomsPresenter
import com.android.mobiversa.MVP.View.RoomsView
import com.android.mobiversa.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class RoomPresenterImpl(private val mContext: Context, private val roomsView: RoomsView) : RoomsPresenter {

    override fun setAvailableRooms(firebaseDatabase: FirebaseDatabase?, availableRooms: String?) {
        //Add Value Programatically
        firebaseDatabase?.getReference("Rooms")?.child("Available Rooms")?.setValue(availableRooms)
    }

    override fun setBookedRooms(firebaseDatabase: FirebaseDatabase?, bookedRooms: String?) {
        firebaseDatabase?.getReference("Rooms")?.child("Booked Rooms")?.setValue(bookedRooms)
    }

    override fun getRoomsDetails(database: FirebaseDatabase) {

        val myReference = database.getReference("Rooms")

        // Read from the database
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: HashMap<String, Any> = dataSnapshot.value as HashMap<String, Any>
                val availableRooms = value["Available Rooms"]
                val bookedRooms = value["Booked Rooms"]
                if (availableRooms != null) {
                    roomsView.setAvailableRoomsDetails(availableRooms.toString())
                }
                if (bookedRooms != null) {
                    roomsView.setBookedRoomsDetails(bookedRooms.toString())
                }
                Log.d("Mobiversa", "Available Room: $availableRooms Booked Room: $bookedRooms ")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                roomsView.gettingError(error.message)
                Log.w("Mobiversa", "Failed to read value.", error.toException())
            }
        })



}
}
