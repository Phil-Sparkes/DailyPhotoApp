package com.example.selfiegoals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText


class NewGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_goal)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        val editTextNewGoal = findViewById<EditText>(R.id.editText_newGoal)
        val mainIntent = Intent(this, MainActivity::class.java)

        btnBack.setOnClickListener {
            startActivity(mainIntent)
        }
        btnConfirm.setOnClickListener {
            val db = GoalDB(this)
            val setGoal = (editTextNewGoal.text.toString())
            var newgid = 1
            var gidtaken = true
            while (gidtaken) {
                if (db.checkIfInDB(newgid)) {
                    newgid += 1
                }
                else gidtaken = false
            }
            db.insertIntoGoal(newgid, setGoal, "")
            db.close()
            startActivity(mainIntent)
        }
    }
}
