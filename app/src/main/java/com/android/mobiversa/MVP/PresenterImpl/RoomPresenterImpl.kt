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

    override fun getRoomsDetails(database: FirebaseDatabase, availableRooms: String, bookedRooms: String) {

        database.getReference("Rooms").child("Available Rooms").setValue("6")
        database.getReference("Rooms").child("Booked Rooms").setValue("4")

        val myReference = database.getReference("Rooms")

        // Read from the database
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: HashMap<String, String> = dataSnapshot.value as HashMap<String, String>
                val availableRooms = value["Available Rooms"]
                val bookedRooms = value["Booked Rooms"]

                if (availableRooms != null) {
                    roomsView.setAvailableRoomsDetails(availableRooms)

                }

                if (bookedRooms != null) {
                    roomsView.setAvailableRoomsDetails(bookedRooms)

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
