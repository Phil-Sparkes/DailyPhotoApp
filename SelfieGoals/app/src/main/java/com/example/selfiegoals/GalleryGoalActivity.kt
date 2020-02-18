package com.example.selfiegoals

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.AlarmClock
import android.widget.ImageView
import android.widget.LinearLayout
import java.io.File

class GalleryGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_goal)

        val currentGoal = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE)
        val imageLayout = findViewById<LinearLayout>(R.id.imageLayout)

        val imageBitmaps = loadImages(currentGoal)
        for (imageBitmap in imageBitmaps) {
            makeImageView(imageBitmap,imageLayout)
        }

    }
    private fun makeImageView(imageBitmap: Bitmap, constraintLayout: LinearLayout) {
        // creates a new image view
        val newImageView = ImageView(this)

        // sets the image of the image view
        newImageView.setImageBitmap(imageBitmap)

        // adds it to the constraint layout so visible
        constraintLayout.addView(newImageView)
    }

    private fun loadImages(currentGoal: String): MutableList<Bitmap> {
        // get path to the storage directory
        val path = Environment.getExternalStorageDirectory()
        // create an array of all the files in the directory
        val files: Array<File> = path.listFiles()
        // create a mutableList (Mutable just means you can add to it) of bitmaps
        val imageBitmaps = mutableListOf<Bitmap>()
        // search through all the files and get the ones that are associated with current goal
        for (file in files) {
            if (file.getName().startsWith(currentGoal))
            // get the bitmaps from these image files
                imageBitmaps.add(BitmapFactory.decodeFile(file.getAbsolutePath()))
        }
        // return the list of bitmaps
        return imageBitmaps
    }
}
