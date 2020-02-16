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

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 101)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val todayDate = Calendar.getInstance().time
        val todayString = formatter.format(todayDate)

        val db = GoalDB(this)
        val goals = db.selectAllGoal()

        for (goal in goals) {
            Log.e("MainActivity", "gid ${goal.gid} gname ${goal.gname} gdate ${goal.gdate}")
            //if (todayString == "${goal.gdate}") {
            makeCheckBox(goal.gname!!, (todayString == "${goal.gdate}"))
        }
        db.close()

        val btnNewGoal = findViewById<Button>(R.id.btn_newgoal)
        val btnViewGallery =findViewById<Button>(R.id.btn_viewGallery)
        val btnCalendar =findViewById<Button>(R.id.btn_calendar)

        val intentNewGoal = Intent(this, NewGoalActivity::class.java)
        val intentGallery = Intent(this, GalleryActivity::class.java)

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
        val constraintLayout = findViewById<LinearLayout>(R.id.linearLayout2)
        val newCheckBox = CheckBox(this)
        val intentCamera = Intent(this, CustomCamera::class.java)

        newCheckBox.text = checkBoxText
        newCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (32).toFloat())

        if (checked) { newCheckBox.isChecked = true}

        constraintLayout.addView(newCheckBox)
        registerForContextMenu(newCheckBox)

        newCheckBox.setOnClickListener {
            intent = intentCamera.apply { putExtra(EXTRA_MESSAGE, checkBoxText)}
            startActivity(intent)
            }
        }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        getMenuInflater().inflate(R.menu.checkbox_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.option_1 -> {Toast.makeText(this, "option 1 selected", Toast.LENGTH_SHORT).show()
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


