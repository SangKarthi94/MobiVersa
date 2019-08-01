package com.android.mobiversa

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.android.mobiversa.MVP.View.RoomsView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(), View.OnClickListener,RoomsView {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.check_in_btn -> {
                Toast.makeText(applicationContext, "Check In", Toast.LENGTH_SHORT).show()
            }
            R.id.check_out_btn -> {
                Toast.makeText(applicationContext, "Check Out", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.login_bg)

        check_in_btn.setOnClickListener(this)
        check_out_btn.setOnClickListener(this)

// Write a message to the database
        val database = FirebaseDatabase.getInstance()
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
                    check_in_btn.isClickable = availableRooms.toInt() >= 1
                }

                if (bookedRooms != null) {
                    check_out_btn.isClickable = bookedRooms.toInt() < 10
                }


                Log.d("Mobiversa", "Available Room: $availableRooms Booked Room: $bookedRooms ")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Mobiversa", "Failed to read value.", error.toException())
            }
        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //MVP implementation here
    override fun setRoomsDetails() {

    }

    override fun showLoadingProgressBar() {

    }

    override fun hideLoadingProgressBar() {

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout -> {
                print("Logout")
                Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
                true
            }else -> {
                print("Logout")
                Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }
}
