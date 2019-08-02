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

    override fun setAvailableRooms(firebaseDatabase: FirebaseDatabase?, availableRooms: String?, date: String) {
        //Add Value Programatically
        firebaseDatabase?.getReference(date)?.child("Available Rooms")?.setValue(availableRooms)
    }

    override fun setBookedRooms(firebaseDatabase: FirebaseDatabase?, bookedRooms: String?, date: String) {
        firebaseDatabase?.getReference(date)?.child("Booked Rooms")?.setValue(bookedRooms)
    }

    override fun getRoomsDetails(database: FirebaseDatabase, date: String) {

        val myReference = database.getReference(date)

        // Read from the database
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value  = dataSnapshot.value

                if (value != null){
                    val data = value as HashMap<*, *>
                    val availableRooms = value["Available Rooms"]
                    val bookedRooms = value["Booked Rooms"]
                    if (availableRooms != null) {
                        roomsView.setAvailableRoomsDetails(availableRooms.toString())
                    }
                    if (bookedRooms != null) {
                        roomsView.setBookedRoomsDetails(bookedRooms.toString())
                    }
                    Log.d("Mobiversa", "Available Room: $availableRooms Booked Room: $bookedRooms ")
                }else{
                    database.getReference(date).child("Available Rooms").setValue("10")
                    database.getReference(date).child("Booked Rooms").setValue("0")
                }



            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                roomsView.gettingError(error.message)
                Log.w("Mobiversa", "Failed to read value.", error.toException())
            }
        })



}
}
