package com.android.mobiversa

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.*
import android.widget.*
import com.android.mobiversa.MVP.Presenter.RoomsPresenter
import com.android.mobiversa.MVP.PresenterImpl.RoomPresenterImpl
import com.android.mobiversa.MVP.View.RoomsView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener, RoomsView {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.check_in_btn -> {
                showDialog(this, "Check In", availabeRooms)
            }
            R.id.check_out_btn -> {
                showDialog(this, "Check Out", bookedRooms)
            }
            R.id.txt_date -> {
                datePicker(txt_date)
            }
        }
    }

    private var roomPresenter: RoomsPresenter? = null
    private var availabeRooms: Int = 0
    private var bookedRooms: Int = 0
    private var selectedDate: String = ""

    @SuppressLint("SimpleDateFormat")
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
        txt_date.setOnClickListener(this)

        val c = Calendar.getInstance().time
        println("Current time => $c")

        val firebaseFormat = SimpleDateFormat("MMddyy")

        val viewDate = SimpleDateFormat("dd MMM yy")
        selectedDate = firebaseFormat.format(c)

        txt_date.text = viewDate.format(c)

        Log.e("Firebase", selectedDate)
        //Listening to Firebase
        (roomPresenter as RoomPresenterImpl).getRoomsDetails(FirebaseDatabase.getInstance(), selectedDate)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Sending Feedback Under Developing", Snackbar.LENGTH_LONG)
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
        if (availabeRooms.toInt() >= 1) {
            this.availabeRooms = availabeRooms.toInt()
            check_in_btn.setBackgroundResource(R.drawable.rect_bor_select)
        } else {
            this.availabeRooms = 0
            check_in_btn.setBackgroundResource(R.drawable.rect_bor_filled)
        }
    }

    override fun setBookedRoomsDetails(bookedRooms: String?) {
        check_out_btn.isClickable = bookedRooms!!.toInt() >= 1
        if (bookedRooms.toInt() >= 1) {
            this.bookedRooms = bookedRooms.toInt()
            check_out_btn.setBackgroundResource(R.drawable.rect_bor_select)
        } else {
            this.bookedRooms = 0
            check_out_btn.setBackgroundResource(R.drawable.rect_bor_filled)
        }
    }

    override fun showLoadingProgressBar() {

    }

    override fun hideLoadingProgressBar() {

    }

    override fun gettingError(s: String?) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(activity: MainActivity, title: String, rooms: Int) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_room_book)

        var spnrInt = 0

        val checkBtn = dialog.findViewById(R.id.alert_btn) as Button
        checkBtn.text = title
        val spinnerSelect = dialog.findViewById(R.id.alert_room_count_spinner) as Spinner
        val numbers = (1..rooms).toList()
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerSelect.adapter = aa
        spinnerSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                spnrInt = position + 1
            }

        }
        checkBtn.setOnClickListener {

            when (title) {
                "Check In" -> {
                    //10 is the maximum Number of rooms available
                    (roomPresenter as RoomPresenterImpl).setAvailableRooms(
                        FirebaseDatabase.getInstance(),
                        (rooms - spnrInt).toString(),
                        selectedDate
                    )
                    (roomPresenter as RoomPresenterImpl).setBookedRooms(
                        FirebaseDatabase.getInstance(),
                        (10 - (rooms - spnrInt)).toString(),
                        selectedDate
                    )
                }
                "Check Out" -> {
                    (roomPresenter as RoomPresenterImpl).setAvailableRooms(
                        FirebaseDatabase.getInstance(),
                        (10 - (rooms - spnrInt)).toString(),
                        selectedDate
                    )
                    (roomPresenter as RoomPresenterImpl).setBookedRooms(
                        FirebaseDatabase.getInstance(),
                        (rooms - spnrInt).toString(),
                        selectedDate
                    )
                }
            }

            dialog.dismiss()
        }
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun datePicker(date_txt: TextView) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //Nov-07-2018
        val dpd = DatePickerDialog(
            this,
            R.style.MyDatePickerStyle,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox Nov-07-2018
                val dateStr =
                    String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", dayOfMonth) + "/" + year

                val curFormater = SimpleDateFormat("MM/dd/yyyy")
                val dateObj = curFormater.parse(dateStr)

                //For FireBase
                val firebaseFormat = SimpleDateFormat("MMddyy")
                //ForView
                val viewDate = SimpleDateFormat("dd MMM yy")

                selectedDate = firebaseFormat.format(dateObj)
                date_txt.text = viewDate.format(dateObj)

                //Listening to Firebase
                (roomPresenter as RoomPresenterImpl).getRoomsDetails(FirebaseDatabase.getInstance(), selectedDate)
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout -> {
                //Save to shared prefs
                val session = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit()
                session.putBoolean("isSignup", false)
                session.apply()

                //Intent to MainActivity
                val hotelIntent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(hotelIntent)

                Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                return true
            }
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}
