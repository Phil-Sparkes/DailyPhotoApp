package com.example.selfiegoals

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

var imageBitmap = CustomCamera.bitmap

class PictureActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.picture_activity)

        imageBitmap = CustomCamera.bitmap

        val matrix = Matrix()
        matrix.postRotate((90).toFloat())

        val rotatedBitmap = Bitmap.createBitmap(imageBitmap,0,0, imageBitmap.width, imageBitmap.height, matrix, true)

        val currentGoal = intent.getStringExtra(EXTRA_MESSAGE)
        toast(currentGoal)

        val db = GoalDB(this)
        val goals = db.selectAllGoal()
        var currentID = 0

        for (goal in goals) {
            if (currentGoal == goal.gname) {
                currentID = goal.gid!!
            }
        }
        db.close()

        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val imageView = findViewById<ImageView>(R.id.imageCapture)

        imageView.setImageBitmap(rotatedBitmap)

        val mainIntent = Intent(this, MainActivity::class.java)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val todayDate = Calendar.getInstance().time
        val todayString = formatter.format(todayDate)

        btnBack.setOnClickListener {
            startActivity(mainIntent)
        }
        btnSave.setOnClickListener {
            saveImage(currentGoal!!, todayString, rotatedBitmap)
            db.updateGoalWith(currentID, currentGoal, todayString)
            startActivity(mainIntent)
        }
    }


    private fun saveImage(currentGoal: String, todayString: String, bitmap: Bitmap) {

        val path = Environment.getExternalStorageDirectory().toString()

        // Create a file to save the image
        val file = File(path, "$currentGoal-$todayString.jpg")
        toast("$currentGoal-$todayString.jpg")
        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the output stream
            stream.flush()

            // Close the output stream
            stream.close()

            toast("Image saved successful.")
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
            toast("Error to save image.")
        }
    }

    // Extension function to show toast message
    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
