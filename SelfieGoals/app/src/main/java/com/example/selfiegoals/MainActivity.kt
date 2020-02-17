package com.example.selfiegoals

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.util.TypedValue
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 101)

        // getting todays date as a string
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val todayDate = Calendar.getInstance().time
        val todayString = formatter.format(todayDate)

        // opening db file
        val db = GoalDB(this)
        val goals = db.selectAllGoal()

        // looping through the db
        for (goal in goals) {
            Log.e("MainActivity", "gid ${goal.gid} gname ${goal.gname} gdate ${goal.gdate}")
            //if (todayString == "${goal.gdate}") {
            makeCheckBox(goal.gname!!, (todayString == "${goal.gdate}"))
        }

        // close the database so no memory leak
        db.close()

        // assigning buttons
        val btnNewGoal = findViewById<Button>(R.id.btn_newgoal)
        val btnViewGallery =findViewById<Button>(R.id.btn_viewGallery)
        val btnCalendar =findViewById<Button>(R.id.btn_calendar)

        // assigning intents, used to switch between activities
        val intentNewGoal = Intent(this, NewGoalActivity::class.java)
        val intentGallery = Intent(this, GalleryActivity::class.java)

        // on click for buttons
        btnNewGoal.setOnClickListener{
            startActivity(intentNewGoal)
        }

        btnViewGallery.setOnClickListener{
            startActivity(intentGallery)
        }

        btnCalendar.setOnClickListener{
            Toast.makeText(this, "To be added...", Toast.LENGTH_SHORT).show()

        }
    }

    private fun makeCheckBox(checkBoxText: String, checked: Boolean) {
        // Assigning the constraint layout which is the linearlayout on the XML file
        val constraintLayout = findViewById<LinearLayout>(R.id.linearLayout2)

        // Creating a new checkbox
        val newCheckBox = CheckBox(this)

        // Assigning the camera intent, used to switch to the camera activity
        val intentCamera = Intent(this, CustomCamera::class.java)

        // setting the checkbox text and size of the text
        newCheckBox.text = checkBoxText
        newCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (32).toFloat())

        // checking if the textbox has been checked already
        if (checked) { newCheckBox.isChecked = true}

        // adding the checkbox to the constraint layout
        constraintLayout.addView(newCheckBox)

        // assigning a context menu to the checkbox. context menu is when you hold click and more options appear
        var newCheckBoxContextMenu = registerForContextMenu(newCheckBox)
        //newCheckBoxContextMenu = ContextMenu.

        // on click for the checkbox open the camera intent
        newCheckBox.setOnClickListener {
            // Extra message sends information about which goal checkbox is related to
            intent = intentCamera.apply { putExtra(EXTRA_MESSAGE, checkBoxText)}
            startActivity(intent)
            }
        }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val checkbox = v as CheckBox
        checkbox.text.toString()
        menu!!.setHeaderTitle(checkbox.text.toString())
        menu.add(0, v!!.id, 0, "Call")
        menu.add(0, v.id, 1, "SMS")
        menu.add(0, v.id, 2, "Email")
        menu.add(0, v.id, 3, "WhatsApp")
        //getMenuInflater().inflate(R.menu.checkbox_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.option_1 -> {Toast.makeText(this, item.menuInfo.toString(), Toast.LENGTH_SHORT).show()

                return true }
            R.id.option_2 -> {Toast.makeText(this, "option 2 selected", Toast.LENGTH_SHORT).show()
                return true }
            R.id.option_3 -> {Toast.makeText(this, "option 3 selected", Toast.LENGTH_SHORT).show()
                return true }
            else -> return super.onContextItemSelected(item)
        }
    }
    fun deleteFromDB(gid: Int) {
        val db = GoalDB(this)
        db.deleteGoalWith(gid)
        db.close()
    }

}


