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
import com.android.mobiversa.MVP.Presenter.RoomsPresenter
import com.android.mobiversa.MVP.PresenterImpl.RoomPresenterImpl
import com.android.mobiversa.MVP.View.RoomsView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(), View.OnClickListener, RoomsView {

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

    private var roomPresenter: RoomsPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.login_bg)

        roomPresenter = RoomPresenterImpl(this, this);



        check_in_btn.setOnClickListener(this)
        check_out_btn.setOnClickListener(this)
        (roomPresenter as RoomPresenterImpl).getRoomsDetails(FirebaseDatabase.getInstance(), "6", "4")
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
    override fun setAvailableRoomsDetails(availabeRooms: String?) {
        check_in_btn.isClickable = availabeRooms!!.toInt() >= 1
    }

    override fun setBookedRoomsDetails(bookedRooms: String?) {
        check_out_btn.isClickable = bookedRooms!!.toInt() < 10
    }

    override fun showLoadingProgressBar() {

    }

    override fun hideLoadingProgressBar() {

    }

    override fun gettingError(s: String?) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
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
            }
            else -> {
                print("Logout")
                Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }
}
